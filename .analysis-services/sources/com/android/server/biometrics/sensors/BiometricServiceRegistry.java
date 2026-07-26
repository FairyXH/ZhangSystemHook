package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class BiometricServiceRegistry<T extends com.android.server.biometrics.sensors.BiometricServiceProvider<P>, P extends android.hardware.biometrics.SensorPropertiesInternal, C extends android.os.IInterface> {
    private static final java.lang.String TAG = "BiometricServiceRegistry";
    private volatile java.util.List<P> mAllProps;
    private final java.util.function.Supplier<android.hardware.biometrics.IBiometricService> mBiometricServiceSupplier;
    private final android.os.RemoteCallbackList<C> mRegisteredCallbacks = new android.os.RemoteCallbackList<>();
    private volatile java.util.List<T> mServiceProviders;

    protected abstract void invokeRegisteredCallback(C c, java.util.List<P> list) throws android.os.RemoteException;

    protected abstract void registerService(android.hardware.biometrics.IBiometricService iBiometricService, P p);

    public BiometricServiceRegistry(java.util.function.Supplier<android.hardware.biometrics.IBiometricService> biometricSupplier) {
        this.mBiometricServiceSupplier = biometricSupplier;
    }

    public void registerAll(final java.util.function.Supplier<java.util.List<T>> serviceProvider) {
        com.android.server.ServiceThread thread = new com.android.server.ServiceThread(TAG, 10, true);
        thread.start();
        android.os.Handler handler = new android.os.Handler(thread.getLooper());
        handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.BiometricServiceRegistry$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$registerAll$0(serviceProvider);
            }
        });
        thread.quitSafely();
    }

    /* JADX INFO: renamed from: registerAllInBackground, reason: merged with bridge method [inline-methods] */
    public void lambda$registerAll$0(java.util.function.Supplier<java.util.List<T>> serviceProvider) {
        java.util.List<T> providers = serviceProvider.get();
        if (providers == null) {
            providers = new java.util.ArrayList();
        }
        android.hardware.biometrics.IBiometricService biometricService = this.mBiometricServiceSupplier.get();
        if (biometricService == null) {
            throw new java.lang.IllegalStateException("biometric service cannot be null");
        }
        java.util.List<P> allProps = new java.util.ArrayList<>();
        for (T provider : providers) {
            java.util.List<P> props = provider.getSensorProperties();
            for (P prop : props) {
                registerService(biometricService, prop);
            }
            allProps.addAll(props);
        }
        finishRegistration(providers, allProps);
    }

    private synchronized void finishRegistration(java.util.List<T> providers, java.util.List<P> allProps) {
        this.mServiceProviders = java.util.Collections.unmodifiableList(providers);
        this.mAllProps = java.util.Collections.unmodifiableList(allProps);
        broadcastAllAuthenticatorsRegistered();
    }

    public synchronized void addAllRegisteredCallback(C callback) {
        if (callback == null) {
            android.util.Slog.e(TAG, "addAllRegisteredCallback, callback is null");
            return;
        }
        boolean registered = this.mRegisteredCallbacks.register(callback);
        boolean allRegistered = this.mServiceProviders != null;
        if (registered && allRegistered) {
            broadcastAllAuthenticatorsRegistered();
        } else if (!registered) {
            android.util.Slog.e(TAG, "addAllRegisteredCallback failed to register callback");
        }
    }

    private synchronized void broadcastAllAuthenticatorsRegistered() {
        int n = this.mRegisteredCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            android.os.IInterface broadcastItem = this.mRegisteredCallbacks.getBroadcastItem(i);
            try {
                invokeRegisteredCallback(broadcastItem, this.mAllProps);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in broadcastAllAuthenticatorsRegistered", e);
            } finally {
                this.mRegisteredCallbacks.unregister(broadcastItem);
            }
        }
        this.mRegisteredCallbacks.finishBroadcast();
    }

    public java.util.List<T> getProviders() {
        return this.mServiceProviders != null ? this.mServiceProviders : java.util.Collections.emptyList();
    }

    public T getProviderForSensor(int sensorId) {
        if (this.mServiceProviders != null) {
            for (T provider : this.mServiceProviders) {
                if (provider.containsSensor(sensorId)) {
                    return provider;
                }
            }
            return null;
        }
        return null;
    }

    public android.util.Pair<java.lang.Integer, T> getSingleProvider() {
        java.lang.String extra;
        if (this.mAllProps == null || this.mAllProps.isEmpty()) {
            android.util.Slog.e(TAG, "No sensors found");
            return null;
        }
        try {
            if (this.mAllProps.size() > 1) {
                android.util.Slog.e(TAG, "getSingleProvider() called but multiple sensors present: " + this.mAllProps.size());
            }
            int sensorId = ((android.hardware.biometrics.SensorPropertiesInternal) this.mAllProps.get(0)).sensorId;
            com.android.server.biometrics.sensors.BiometricServiceProvider providerForSensor = getProviderForSensor(sensorId);
            if (providerForSensor != null) {
                return new android.util.Pair<>(java.lang.Integer.valueOf(sensorId), providerForSensor);
            }
            android.util.Slog.e(TAG, "Single sensor: " + sensorId + ", but provider not found");
            return null;
        } catch (java.lang.NullPointerException e) {
            if (this.mAllProps == null) {
                extra = "mAllProps: null";
            } else {
                extra = "mAllProps.size(): " + this.mAllProps.size();
            }
            android.util.Slog.e(TAG, "This shouldn't happen. " + extra, e);
            throw e;
        }
    }

    public java.util.List<P> getAllProperties() {
        return this.mAllProps != null ? this.mAllProps : java.util.Collections.emptyList();
    }
}
