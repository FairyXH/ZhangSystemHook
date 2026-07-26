package com.android.server.health;

/* JADX INFO: loaded from: classes2.dex */
final class HealthServiceWrapperHidl extends com.android.server.health.HealthServiceWrapper {
    public static final java.lang.String INSTANCE_VENDOR = "default";
    private static final java.lang.String TAG = "HealthServiceWrapperHidl";
    private com.android.server.health.HealthServiceWrapperHidl.Callback mCallback;
    private com.android.server.health.HealthServiceWrapperHidl.IHealthSupplier mHealthSupplier;
    private java.lang.String mInstanceName;
    private final android.hidl.manager.V1_0.IServiceNotification mNotification = new com.android.server.health.HealthServiceWrapperHidl.Notification();
    private final android.os.HandlerThread mHandlerThread = new android.os.HandlerThread("HealthServiceHwbinder");
    private final java.util.concurrent.atomic.AtomicReference<android.hardware.health.V2_0.IHealth> mLastService = new java.util.concurrent.atomic.AtomicReference<>();

    interface Callback {
        void onRegistration(android.hardware.health.V2_0.IHealth iHealth, android.hardware.health.V2_0.IHealth iHealth2, java.lang.String str);
    }

    private static void traceBegin(java.lang.String name) {
        android.os.Trace.traceBegin(524288L, name);
    }

    private static void traceEnd() {
        android.os.Trace.traceEnd(524288L);
    }

    @Override // com.android.server.health.HealthServiceWrapper
    public int getProperty(int id, final android.os.BatteryProperty prop) throws android.os.RemoteException {
        traceBegin("HealthGetProperty");
        try {
            android.hardware.health.V2_0.IHealth service = this.mLastService.get();
            if (service == null) {
                throw new android.os.RemoteException("no health service");
            }
            final android.util.MutableInt outResult = new android.util.MutableInt(1);
            switch (id) {
                case 1:
                    service.getChargeCounter(new android.hardware.health.V2_0.IHealth.getChargeCounterCallback() { // from class: com.android.server.health.HealthServiceWrapperHidl$$ExternalSyntheticLambda1
                        @Override // android.hardware.health.V2_0.IHealth.getChargeCounterCallback
                        public final void onValues(int i, int i2) {
                            com.android.server.health.HealthServiceWrapperHidl.lambda$getProperty$0(outResult, prop, i, i2);
                        }
                    });
                    break;
                case 2:
                    service.getCurrentNow(new android.hardware.health.V2_0.IHealth.getCurrentNowCallback() { // from class: com.android.server.health.HealthServiceWrapperHidl$$ExternalSyntheticLambda2
                        @Override // android.hardware.health.V2_0.IHealth.getCurrentNowCallback
                        public final void onValues(int i, int i2) {
                            com.android.server.health.HealthServiceWrapperHidl.lambda$getProperty$1(outResult, prop, i, i2);
                        }
                    });
                    break;
                case 3:
                    service.getCurrentAverage(new android.hardware.health.V2_0.IHealth.getCurrentAverageCallback() { // from class: com.android.server.health.HealthServiceWrapperHidl$$ExternalSyntheticLambda3
                        @Override // android.hardware.health.V2_0.IHealth.getCurrentAverageCallback
                        public final void onValues(int i, int i2) {
                            com.android.server.health.HealthServiceWrapperHidl.lambda$getProperty$2(outResult, prop, i, i2);
                        }
                    });
                    break;
                case 4:
                    service.getCapacity(new android.hardware.health.V2_0.IHealth.getCapacityCallback() { // from class: com.android.server.health.HealthServiceWrapperHidl$$ExternalSyntheticLambda4
                        @Override // android.hardware.health.V2_0.IHealth.getCapacityCallback
                        public final void onValues(int i, int i2) {
                            com.android.server.health.HealthServiceWrapperHidl.lambda$getProperty$3(outResult, prop, i, i2);
                        }
                    });
                    break;
                case 5:
                    service.getEnergyCounter(new android.hardware.health.V2_0.IHealth.getEnergyCounterCallback() { // from class: com.android.server.health.HealthServiceWrapperHidl$$ExternalSyntheticLambda6
                        @Override // android.hardware.health.V2_0.IHealth.getEnergyCounterCallback
                        public final void onValues(int i, long j) {
                            com.android.server.health.HealthServiceWrapperHidl.lambda$getProperty$5(outResult, prop, i, j);
                        }
                    });
                    break;
                case 6:
                    service.getChargeStatus(new android.hardware.health.V2_0.IHealth.getChargeStatusCallback() { // from class: com.android.server.health.HealthServiceWrapperHidl$$ExternalSyntheticLambda5
                        @Override // android.hardware.health.V2_0.IHealth.getChargeStatusCallback
                        public final void onValues(int i, int i2) {
                            com.android.server.health.HealthServiceWrapperHidl.lambda$getProperty$4(outResult, prop, i, i2);
                        }
                    });
                    break;
            }
            return outResult.value;
        } finally {
            traceEnd();
        }
    }

    static /* synthetic */ void lambda$getProperty$0(android.util.MutableInt outResult, android.os.BatteryProperty prop, int result, int value) {
        outResult.value = result;
        if (result == 0) {
            prop.setLong(value);
        }
    }

    static /* synthetic */ void lambda$getProperty$1(android.util.MutableInt outResult, android.os.BatteryProperty prop, int result, int value) {
        outResult.value = result;
        if (result == 0) {
            prop.setLong(value);
        }
    }

    static /* synthetic */ void lambda$getProperty$2(android.util.MutableInt outResult, android.os.BatteryProperty prop, int result, int value) {
        outResult.value = result;
        if (result == 0) {
            prop.setLong(value);
        }
    }

    static /* synthetic */ void lambda$getProperty$3(android.util.MutableInt outResult, android.os.BatteryProperty prop, int result, int value) {
        outResult.value = result;
        if (result == 0) {
            prop.setLong(value);
        }
    }

    static /* synthetic */ void lambda$getProperty$4(android.util.MutableInt outResult, android.os.BatteryProperty prop, int result, int value) {
        outResult.value = result;
        if (result == 0) {
            prop.setLong(value);
        }
    }

    static /* synthetic */ void lambda$getProperty$5(android.util.MutableInt outResult, android.os.BatteryProperty prop, int result, long value) {
        outResult.value = result;
        if (result == 0) {
            prop.setLong(value);
        }
    }

    @Override // com.android.server.health.HealthServiceWrapper
    public void scheduleUpdate() throws android.os.RemoteException {
        getHandlerThread().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.health.HealthServiceWrapperHidl$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleUpdate$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdate$6() {
        android.hardware.health.V2_0.IHealth service;
        traceBegin("HealthScheduleUpdate");
        try {
            try {
                service = this.mLastService.get();
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(TAG, "Cannot call update on health HAL", ex);
            }
            if (service == null) {
                android.util.Slog.e(TAG, "no health service");
            } else {
                service.update();
            }
        } finally {
            traceEnd();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Mutable<T> {
        public T value;

        private Mutable() {
        }
    }

    @Override // com.android.server.health.HealthServiceWrapper
    public android.hardware.health.HealthInfo getHealthInfo() throws android.os.RemoteException {
        android.hardware.health.V2_0.IHealth service = this.mLastService.get();
        if (service == null) {
            return null;
        }
        final com.android.server.health.HealthServiceWrapperHidl.Mutable<android.hardware.health.HealthInfo> ret = new com.android.server.health.HealthServiceWrapperHidl.Mutable<>();
        service.getHealthInfo(new android.hardware.health.V2_0.IHealth.getHealthInfoCallback() { // from class: com.android.server.health.HealthServiceWrapperHidl$$ExternalSyntheticLambda0
            @Override // android.hardware.health.V2_0.IHealth.getHealthInfoCallback
            public final void onValues(int i, android.hardware.health.V2_0.HealthInfo healthInfo) {
                com.android.server.health.HealthServiceWrapperHidl.lambda$getHealthInfo$7(ret, i, healthInfo);
            }
        });
        return ret.value;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [T, android.hardware.health.HealthInfo] */
    static /* synthetic */ void lambda$getHealthInfo$7(com.android.server.health.HealthServiceWrapperHidl.Mutable ret, int result, android.hardware.health.V2_0.HealthInfo value) {
        if (result == 0) {
            ret.value = android.hardware.health.Translate.h2aTranslate(value.legacy);
        }
    }

    HealthServiceWrapperHidl(com.android.server.health.HealthServiceWrapperHidl.Callback callback, com.android.server.health.HealthServiceWrapperHidl.IServiceManagerSupplier managerSupplier, com.android.server.health.HealthServiceWrapperHidl.IHealthSupplier healthSupplier) throws android.os.RemoteException, java.util.NoSuchElementException, java.lang.NullPointerException {
        if (managerSupplier == null || healthSupplier == null) {
            throw new java.lang.NullPointerException();
        }
        this.mHealthSupplier = healthSupplier;
        android.hardware.health.V2_0.IHealth newService = null;
        traceBegin("HealthInitGetService_default");
        try {
            newService = healthSupplier.get("default");
        } catch (java.util.NoSuchElementException e) {
        } catch (java.lang.Throwable th) {
            throw th;
        }
        if (newService != null) {
            this.mInstanceName = "default";
            this.mLastService.set(newService);
        }
        if (this.mInstanceName == null || newService == null) {
            throw new java.util.NoSuchElementException(java.lang.String.format("IHealth service instance %s isn't available. Perhaps no permission?", "default"));
        }
        if (callback != null) {
            this.mCallback = callback;
            this.mCallback.onRegistration(null, newService, this.mInstanceName);
        }
        traceBegin("HealthInitRegisterNotification");
        this.mHandlerThread.start();
        try {
            managerSupplier.get().registerForNotifications(android.hardware.health.V2_0.IHealth.kInterfaceName, this.mInstanceName, this.mNotification);
            traceEnd();
            android.util.Slog.i(TAG, "health: HealthServiceWrapper listening to instance " + this.mInstanceName);
        } finally {
            traceEnd();
        }
    }

    @Override // com.android.server.health.HealthServiceWrapper
    public android.os.HandlerThread getHandlerThread() {
        return this.mHandlerThread;
    }

    interface IServiceManagerSupplier {
        default android.hidl.manager.V1_0.IServiceManager get() throws android.os.RemoteException, java.util.NoSuchElementException {
            return android.hidl.manager.V1_0.IServiceManager.getService();
        }
    }

    interface IHealthSupplier {
        default android.hardware.health.V2_0.IHealth get(java.lang.String name) throws android.os.RemoteException, java.util.NoSuchElementException {
            return android.hardware.health.V2_0.IHealth.getService(name, true);
        }
    }

    private class Notification extends android.hidl.manager.V1_0.IServiceNotification.Stub {
        private Notification() {
        }

        @Override // android.hidl.manager.V1_0.IServiceNotification
        public final void onRegistration(java.lang.String interfaceName, java.lang.String instanceName, boolean preexisting) {
            if (android.hardware.health.V2_0.IHealth.kInterfaceName.equals(interfaceName) && com.android.server.health.HealthServiceWrapperHidl.this.mInstanceName.equals(instanceName)) {
                com.android.server.health.HealthServiceWrapperHidl.this.mHandlerThread.getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.health.HealthServiceWrapperHidl.Notification.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            android.hardware.health.V2_0.IHealth newService = com.android.server.health.HealthServiceWrapperHidl.this.mHealthSupplier.get(com.android.server.health.HealthServiceWrapperHidl.this.mInstanceName);
                            android.hardware.health.V2_0.IHealth oldService = (android.hardware.health.V2_0.IHealth) com.android.server.health.HealthServiceWrapperHidl.this.mLastService.getAndSet(newService);
                            if (java.util.Objects.equals(newService, oldService)) {
                                return;
                            }
                            android.util.Slog.i(com.android.server.health.HealthServiceWrapperHidl.TAG, "health: new instance registered " + com.android.server.health.HealthServiceWrapperHidl.this.mInstanceName);
                            if (com.android.server.health.HealthServiceWrapperHidl.this.mCallback == null) {
                                return;
                            }
                            com.android.server.health.HealthServiceWrapperHidl.this.mCallback.onRegistration(oldService, newService, com.android.server.health.HealthServiceWrapperHidl.this.mInstanceName);
                        } catch (android.os.RemoteException | java.util.NoSuchElementException ex) {
                            android.util.Slog.e(com.android.server.health.HealthServiceWrapperHidl.TAG, "health: Cannot get instance '" + com.android.server.health.HealthServiceWrapperHidl.this.mInstanceName + "': " + ex.getMessage() + ". Perhaps no permission?");
                        }
                    }
                });
            }
        }
    }
}
