package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemDeviceIdleHelper extends com.android.server.location.injector.DeviceIdleHelper {
    private final android.content.Context mContext;
    private android.os.PowerManager mPowerManager;
    private android.content.BroadcastReceiver mReceiver;
    private boolean mRegistrationRequired;
    private boolean mSystemReady;

    public SystemDeviceIdleHelper(android.content.Context context) {
        this.mContext = context;
    }

    public synchronized void onSystemReady() {
        this.mSystemReady = true;
        this.mPowerManager = (android.os.PowerManager) java.util.Objects.requireNonNull((android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class));
        onRegistrationStateChanged();
    }

    @Override // com.android.server.location.injector.DeviceIdleHelper
    protected synchronized void registerInternal() {
        this.mRegistrationRequired = true;
        onRegistrationStateChanged();
    }

    @Override // com.android.server.location.injector.DeviceIdleHelper
    protected synchronized void unregisterInternal() {
        this.mRegistrationRequired = false;
        onRegistrationStateChanged();
    }

    private void onRegistrationStateChanged() {
        if (!this.mSystemReady) {
            return;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mRegistrationRequired && this.mReceiver == null) {
                android.content.BroadcastReceiver receiver = new android.content.BroadcastReceiver() { // from class: com.android.server.location.injector.SystemDeviceIdleHelper.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(android.content.Context context, android.content.Intent intent) {
                        com.android.server.location.injector.SystemDeviceIdleHelper.this.notifyDeviceIdleChanged();
                    }
                };
                this.mContext.registerReceiver(receiver, new android.content.IntentFilter("android.os.action.DEVICE_IDLE_MODE_CHANGED"), null, com.android.server.FgThread.getHandler());
                this.mReceiver = receiver;
            } else if (!this.mRegistrationRequired && this.mReceiver != null) {
                android.content.BroadcastReceiver receiver2 = this.mReceiver;
                this.mReceiver = null;
                this.mContext.unregisterReceiver(receiver2);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.DeviceIdleHelper
    public boolean isDeviceIdle() {
        com.android.internal.util.Preconditions.checkState(this.mPowerManager != null);
        return this.mPowerManager.isDeviceIdleMode();
    }
}
