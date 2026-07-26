package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class BiometricStateCallback<T extends com.android.server.biometrics.sensors.BiometricServiceProvider<P>, P extends android.hardware.biometrics.SensorPropertiesInternal> implements com.android.server.biometrics.sensors.ClientMonitorCallback, android.os.IBinder.DeathRecipient {
    private static final java.lang.String TAG = "BiometricStateCallback";
    private final android.os.UserManager mUserManager;
    private final java.util.concurrent.CopyOnWriteArrayList<android.hardware.biometrics.IBiometricStateListener> mBiometricStateListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
    private java.util.List<T> mProviders = java.util.List.of();
    private int mBiometricState = 0;

    public BiometricStateCallback(android.os.UserManager userManager) {
        this.mUserManager = userManager;
    }

    public synchronized void start(java.util.List<T> allProviders) {
        this.mProviders = java.util.Collections.unmodifiableList(allProviders);
        broadcastCurrentEnrollmentState(null);
    }

    public int getBiometricState() {
        return this.mBiometricState;
    }

    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor client) {
        int previousBiometricState = this.mBiometricState;
        if (client instanceof com.android.server.biometrics.sensors.AuthenticationClient) {
            com.android.server.biometrics.sensors.AuthenticationClient<?, ?> authClient = (com.android.server.biometrics.sensors.AuthenticationClient) client;
            if (authClient.isKeyguard()) {
                this.mBiometricState = 2;
            } else if (authClient.isBiometricPrompt()) {
                this.mBiometricState = 3;
            } else {
                this.mBiometricState = 4;
            }
        } else if (client instanceof com.android.server.biometrics.sensors.EnrollClient) {
            this.mBiometricState = 1;
        } else {
            android.util.Slog.w(TAG, "Other authentication client: " + com.android.server.biometrics.Utils.getClientName(client));
            this.mBiometricState = 0;
        }
        android.util.Slog.d(TAG, "State updated from " + previousBiometricState + " to " + this.mBiometricState + ", client " + client);
        notifyBiometricStateListeners(this.mBiometricState);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor baseClientMonitor, boolean success) {
        this.mBiometricState = 0;
        android.util.Slog.d(TAG, "Client finished, state updated to " + this.mBiometricState + ", client " + baseClientMonitor);
        if (baseClientMonitor instanceof com.android.server.biometrics.sensors.EnrollmentModifier) {
            com.android.server.biometrics.sensors.EnrollmentModifier enrollmentModifier = (com.android.server.biometrics.sensors.EnrollmentModifier) baseClientMonitor;
            boolean enrollmentStateChanged = enrollmentModifier.hasEnrollmentStateChanged();
            android.util.Slog.d(TAG, "Enrollment state changed: " + enrollmentStateChanged);
            if (enrollmentStateChanged) {
                notifyAllEnrollmentStateChanged(baseClientMonitor.getTargetUserId(), baseClientMonitor.getSensorId(), enrollmentModifier.hasEnrollments());
            }
        }
        notifyBiometricStateListeners(this.mBiometricState);
    }

    private void notifyBiometricStateListeners(int newState) {
        for (android.hardware.biometrics.IBiometricStateListener listener : this.mBiometricStateListeners) {
            try {
                listener.onStateChanged(newState);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in biometric state change", e);
                this.mBiometricStateListeners.remove(listener);
                android.util.Slog.d(TAG, "[notifyBiometricStateListeners]remove listener: " + listener + " ,mBiometricStateListeners.length: " + this.mBiometricStateListeners.size());
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public void onBiometricAction(int action) {
        for (android.hardware.biometrics.IBiometricStateListener listener : this.mBiometricStateListeners) {
            try {
                listener.onBiometricAction(action);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception in onBiometricAction", e);
            }
        }
    }

    public synchronized void registerBiometricStateListener(android.hardware.biometrics.IBiometricStateListener listener) {
        android.util.Slog.d(TAG, "[registerBiometricStateListener]add listener : " + listener);
        this.mBiometricStateListeners.add(listener);
        broadcastCurrentEnrollmentState(listener);
        try {
            listener.asBinder().linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to link to death", e);
        }
    }

    private synchronized void broadcastCurrentEnrollmentState(android.hardware.biometrics.IBiometricStateListener listener) {
        for (T provider : this.mProviders) {
            for (android.hardware.biometrics.SensorPropertiesInternal prop : provider.getSensorProperties()) {
                for (android.content.pm.UserInfo userInfo : this.mUserManager.getAliveUsers()) {
                    boolean enrolled = provider.hasEnrollments(prop.sensorId, userInfo.id);
                    if (listener != null) {
                        notifyEnrollmentStateChanged(listener, userInfo.id, prop.sensorId, enrolled);
                    } else {
                        notifyAllEnrollmentStateChanged(userInfo.id, prop.sensorId, enrolled);
                    }
                }
            }
        }
    }

    private void notifyAllEnrollmentStateChanged(int userId, int sensorId, boolean hasEnrollments) {
        for (android.hardware.biometrics.IBiometricStateListener listener : this.mBiometricStateListeners) {
            notifyEnrollmentStateChanged(listener, userId, sensorId, hasEnrollments);
        }
    }

    private void notifyEnrollmentStateChanged(android.hardware.biometrics.IBiometricStateListener listener, int userId, int sensorId, boolean hasEnrollments) {
        try {
            listener.onEnrollmentsChanged(userId, sensorId, hasEnrollments);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(final android.os.IBinder who) {
        android.util.Slog.w(TAG, "Callback binder died: " + who);
        if (this.mBiometricStateListeners.removeIf(new java.util.function.Predicate() { // from class: com.android.server.biometrics.sensors.BiometricStateCallback$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.hardware.biometrics.IBiometricStateListener) obj).asBinder().equals(who);
            }
        })) {
            android.util.Slog.w(TAG, "Removed dead listener for " + who);
        } else {
            android.util.Slog.w(TAG, "No dead listeners found");
        }
    }
}
