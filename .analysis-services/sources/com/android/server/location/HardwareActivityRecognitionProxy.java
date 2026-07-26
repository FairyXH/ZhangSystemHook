package com.android.server.location;

/* JADX INFO: loaded from: classes2.dex */
public class HardwareActivityRecognitionProxy implements com.android.server.servicewatcher.ServiceWatcher.ServiceListener<com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo> {
    private static final java.lang.String SERVICE_ACTION = "com.android.location.service.ActivityRecognitionProvider";
    private static final java.lang.String TAG = "ARProxy";
    private final android.hardware.location.ActivityRecognitionHardware mInstance;
    private final boolean mIsSupported = android.hardware.location.ActivityRecognitionHardware.isSupported();
    private final com.android.server.servicewatcher.ServiceWatcher mServiceWatcher;

    public static com.android.server.location.HardwareActivityRecognitionProxy createAndRegister(android.content.Context context) {
        com.android.server.location.HardwareActivityRecognitionProxy arProxy = new com.android.server.location.HardwareActivityRecognitionProxy(context);
        if (arProxy.register()) {
            return arProxy;
        }
        return null;
    }

    private HardwareActivityRecognitionProxy(android.content.Context context) {
        if (this.mIsSupported) {
            this.mInstance = android.hardware.location.ActivityRecognitionHardware.getInstance(context);
        } else {
            this.mInstance = null;
        }
        this.mServiceWatcher = com.android.server.servicewatcher.ServiceWatcher.create(context, "HardwareActivityRecognitionProxy", com.android.server.servicewatcher.CurrentUserServiceSupplier.createFromConfig(context, SERVICE_ACTION, android.R.bool.config_earcFeatureEnabled_allowed, android.R.string.config_batterymeterBoltPath), this);
    }

    private boolean register() {
        boolean resolves = this.mServiceWatcher.checkServiceResolves();
        if (resolves) {
            this.mServiceWatcher.register();
        }
        return resolves;
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onBind(android.os.IBinder binder, com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo boundServiceInfo) throws android.os.RemoteException {
        java.lang.String descriptor = binder.getInterfaceDescriptor();
        if (android.hardware.location.IActivityRecognitionHardwareWatcher.class.getCanonicalName().equals(descriptor)) {
            android.hardware.location.IActivityRecognitionHardwareWatcher watcher = android.hardware.location.IActivityRecognitionHardwareWatcher.Stub.asInterface(binder);
            if (this.mInstance != null) {
                watcher.onInstanceChanged(this.mInstance);
                return;
            }
            return;
        }
        if (android.hardware.location.IActivityRecognitionHardwareClient.class.getCanonicalName().equals(descriptor)) {
            android.hardware.location.IActivityRecognitionHardwareClient client = android.hardware.location.IActivityRecognitionHardwareClient.Stub.asInterface(binder);
            client.onAvailabilityChanged(this.mIsSupported, this.mInstance);
        } else {
            android.util.Log.e(TAG, "Unknown descriptor: " + descriptor);
        }
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onUnbind() {
    }
}
