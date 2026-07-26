package com.android.server.health;

/* JADX INFO: loaded from: classes2.dex */
class HealthServiceWrapperAidl extends com.android.server.health.HealthServiceWrapper {
    static final java.lang.String SERVICE_NAME = android.hardware.health.IHealth.DESCRIPTOR + "/default";
    private static final java.lang.String TAG = "HealthServiceWrapperAidl";
    private final com.android.server.health.HealthRegCallbackAidl mRegCallback;
    private final android.os.HandlerThread mHandlerThread = new android.os.HandlerThread("HealthServiceBinder");
    private final java.util.concurrent.atomic.AtomicReference<android.hardware.health.IHealth> mLastService = new java.util.concurrent.atomic.AtomicReference<>();
    private final android.os.IServiceCallback mServiceCallback = new com.android.server.health.HealthServiceWrapperAidl.ServiceCallback();

    interface ServiceManagerStub {
        default android.hardware.health.IHealth waitForDeclaredService(java.lang.String name) {
            return android.hardware.health.IHealth.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(name));
        }

        default void registerForNotifications(java.lang.String name, android.os.IServiceCallback callback) throws android.os.RemoteException {
            android.os.ServiceManager.registerForNotifications(name, callback);
        }
    }

    HealthServiceWrapperAidl(com.android.server.health.HealthRegCallbackAidl regCallback, com.android.server.health.HealthServiceWrapperAidl.ServiceManagerStub serviceManager) throws android.os.RemoteException, java.util.NoSuchElementException {
        traceBegin("HealthInitGetServiceAidl");
        try {
            android.hardware.health.IHealth newService = serviceManager.waitForDeclaredService(SERVICE_NAME);
            if (newService == null) {
                throw new java.util.NoSuchElementException("IHealth service instance isn't available. Perhaps no permission?");
            }
            this.mLastService.set(newService);
            this.mRegCallback = regCallback;
            if (this.mRegCallback != null) {
                this.mRegCallback.onRegistration(null, newService);
            }
            traceBegin("HealthInitRegisterNotificationAidl");
            this.mHandlerThread.start();
            try {
                serviceManager.registerForNotifications(SERVICE_NAME, this.mServiceCallback);
                traceEnd();
                android.util.Slog.i(TAG, "health: HealthServiceWrapper listening to AIDL HAL");
            } finally {
            }
        } finally {
        }
    }

    @Override // com.android.server.health.HealthServiceWrapper
    public android.os.HandlerThread getHandlerThread() {
        return this.mHandlerThread;
    }

    @Override // com.android.server.health.HealthServiceWrapper
    public int getProperty(int id, android.os.BatteryProperty prop) throws android.os.RemoteException {
        traceBegin("HealthGetPropertyAidl");
        try {
            return getPropertyInternal(id, prop);
        } finally {
            traceEnd();
        }
    }

    private int getPropertyInternal(int id, android.os.BatteryProperty prop) throws android.os.RemoteException {
        android.hardware.health.IHealth service = this.mLastService.get();
        if (service == null) {
            throw new android.os.RemoteException("no health service");
        }
        try {
            switch (id) {
                case 1:
                    prop.setLong(service.getChargeCounterUah());
                    break;
                case 2:
                    prop.setLong(service.getCurrentNowMicroamps());
                    break;
                case 3:
                    prop.setLong(service.getCurrentAverageMicroamps());
                    break;
                case 4:
                    prop.setLong(service.getCapacity());
                    break;
                case 5:
                    prop.setLong(service.getEnergyCounterNwh());
                    break;
                case 6:
                    prop.setLong(service.getChargeStatus());
                    break;
                case 7:
                    android.hardware.health.BatteryHealthData healthData = service.getBatteryHealthData();
                    prop.setLong(healthData.batteryManufacturingDateSeconds);
                    break;
                case 8:
                    android.hardware.health.BatteryHealthData healthData2 = service.getBatteryHealthData();
                    prop.setLong(healthData2.batteryFirstUsageSeconds);
                    break;
                case 9:
                    prop.setLong(service.getChargingPolicy());
                    break;
                case 10:
                    android.hardware.health.BatteryHealthData healthData3 = service.getBatteryHealthData();
                    prop.setLong(healthData3.batteryStateOfHealth);
                    break;
                case 11:
                    if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.batteryPartStatusApi()) {
                        android.hardware.health.BatteryHealthData healthData4 = service.getBatteryHealthData();
                        prop.setString(healthData4.batterySerialNumber);
                    }
                    break;
                case 12:
                    if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.batteryPartStatusApi()) {
                        android.hardware.health.BatteryHealthData healthData5 = service.getBatteryHealthData();
                        prop.setLong(healthData5.batteryPartStatus);
                    }
                    break;
                default:
                    return 0;
            }
            return 0;
        } catch (java.lang.UnsupportedOperationException e) {
            return -1;
        } catch (android.os.ServiceSpecificException e2) {
            return -2;
        }
    }

    @Override // com.android.server.health.HealthServiceWrapper
    public void scheduleUpdate() throws android.os.RemoteException {
        getHandlerThread().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.health.HealthServiceWrapperAidl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleUpdate$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdate$0() {
        android.hardware.health.IHealth service;
        traceBegin("HealthScheduleUpdate");
        try {
            try {
                service = this.mLastService.get();
            } catch (android.os.RemoteException | android.os.ServiceSpecificException ex) {
                android.util.Slog.e(TAG, "Cannot call update on health AIDL HAL", ex);
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

    @Override // com.android.server.health.HealthServiceWrapper
    public android.hardware.health.HealthInfo getHealthInfo() throws android.os.RemoteException {
        android.hardware.health.IHealth service = this.mLastService.get();
        if (service == null) {
            return null;
        }
        try {
            return service.getHealthInfo();
        } catch (java.lang.UnsupportedOperationException | android.os.ServiceSpecificException e) {
            return null;
        }
    }

    public void setChargingPolicy(int policy) throws android.os.RemoteException {
        android.hardware.health.IHealth service = this.mLastService.get();
        if (service == null) {
            return;
        }
        try {
            service.setChargingPolicy(policy);
        } catch (java.lang.UnsupportedOperationException | android.os.ServiceSpecificException e) {
        }
    }

    private static void traceBegin(java.lang.String name) {
        android.os.Trace.traceBegin(524288L, name);
    }

    private static void traceEnd() {
        android.os.Trace.traceEnd(524288L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ServiceCallback extends android.os.IServiceCallback.Stub {
        private ServiceCallback() {
        }

        public void onRegistration(java.lang.String name, final android.os.IBinder newBinder) throws android.os.RemoteException {
            if (com.android.server.health.HealthServiceWrapperAidl.SERVICE_NAME.equals(name)) {
                com.android.server.health.HealthServiceWrapperAidl.this.getHandlerThread().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.health.HealthServiceWrapperAidl$ServiceCallback$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onRegistration$0(newBinder);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRegistration$0(android.os.IBinder newBinder) {
            android.hardware.health.IHealth newService = android.hardware.health.IHealth.Stub.asInterface(android.os.Binder.allowBlocking(newBinder));
            android.hardware.health.IHealth oldService = (android.hardware.health.IHealth) com.android.server.health.HealthServiceWrapperAidl.this.mLastService.getAndSet(newService);
            android.os.IBinder oldBinder = oldService != null ? oldService.asBinder() : null;
            if (java.util.Objects.equals(newBinder, oldBinder)) {
                return;
            }
            android.util.Slog.i(com.android.server.health.HealthServiceWrapperAidl.TAG, "New health AIDL HAL service registered");
            if (com.android.server.health.HealthServiceWrapperAidl.this.mRegCallback != null) {
                com.android.server.health.HealthServiceWrapperAidl.this.mRegCallback.onRegistration(oldService, newService);
            }
        }
    }
}
