package com.android.server.deviceidle;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothConstraint implements com.android.server.deviceidle.IDeviceIdleConstraint {
    private static final long INACTIVITY_TIMEOUT_MS = 1200000;
    private static final java.lang.String TAG = com.android.server.deviceidle.BluetoothConstraint.class.getSimpleName();
    private final android.bluetooth.BluetoothManager mBluetoothManager;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final com.android.server.DeviceIdleInternal mLocalService;
    private volatile boolean mConnected = true;
    private volatile boolean mMonitoring = false;
    final android.content.BroadcastReceiver mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.deviceidle.BluetoothConstraint.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.bluetooth.device.action.ACL_CONNECTED".equals(intent.getAction())) {
                com.android.server.deviceidle.BluetoothConstraint.this.mLocalService.exitIdle("bluetooth");
            } else {
                com.android.server.deviceidle.BluetoothConstraint.this.updateAndReportActiveLocked();
            }
        }
    };
    private final java.lang.Runnable mTimeoutCallback = new java.lang.Runnable() { // from class: com.android.server.deviceidle.BluetoothConstraint$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };

    public BluetoothConstraint(android.content.Context context, android.os.Handler handler, com.android.server.DeviceIdleInternal localService) {
        this.mContext = context;
        this.mHandler = handler;
        this.mLocalService = localService;
        this.mBluetoothManager = (android.bluetooth.BluetoothManager) this.mContext.getSystemService(android.bluetooth.BluetoothManager.class);
    }

    public synchronized void startMonitoring() {
        this.mConnected = true;
        this.mMonitoring = true;
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        filter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, filter);
        this.mHandler.sendMessageDelayed(android.os.Message.obtain(this.mHandler, this.mTimeoutCallback), INACTIVITY_TIMEOUT_MS);
        updateAndReportActiveLocked();
    }

    public synchronized void stopMonitoring() {
        this.mContext.unregisterReceiver(this.mReceiver);
        this.mHandler.removeCallbacks(this.mTimeoutCallback);
        this.mMonitoring = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: cancelMonitoringDueToTimeout, reason: merged with bridge method [inline-methods] */
    public synchronized void lambda$new$0() {
        if (this.mMonitoring) {
            this.mMonitoring = false;
            this.mLocalService.onConstraintStateChanged(this, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAndReportActiveLocked() {
        boolean connected = isBluetoothConnected(this.mBluetoothManager);
        if (connected != this.mConnected) {
            this.mConnected = connected;
            this.mLocalService.onConstraintStateChanged(this, this.mConnected);
        }
    }

    static boolean isBluetoothConnected(android.bluetooth.BluetoothManager bluetoothManager) {
        android.bluetooth.BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled() && bluetoothManager.getConnectedDevices(7).size() > 0;
    }
}
