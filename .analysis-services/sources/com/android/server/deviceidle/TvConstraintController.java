package com.android.server.deviceidle;

/* JADX INFO: loaded from: classes.dex */
public class TvConstraintController implements com.android.server.deviceidle.ConstraintController {
    private final com.android.server.deviceidle.BluetoothConstraint mBluetoothConstraint;
    private final android.content.Context mContext;
    private final com.android.server.DeviceIdleInternal mDeviceIdleService = (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class);
    private final android.os.Handler mHandler;

    public TvConstraintController(android.content.Context context, android.os.Handler handler) {
        com.android.server.deviceidle.BluetoothConstraint bluetoothConstraint;
        this.mContext = context;
        this.mHandler = handler;
        android.content.pm.PackageManager pm = context.getPackageManager();
        if (pm.hasSystemFeature("android.hardware.bluetooth")) {
            bluetoothConstraint = new com.android.server.deviceidle.BluetoothConstraint(this.mContext, this.mHandler, this.mDeviceIdleService);
        } else {
            bluetoothConstraint = null;
        }
        this.mBluetoothConstraint = bluetoothConstraint;
    }

    public void start() {
        if (this.mBluetoothConstraint != null) {
            this.mDeviceIdleService.registerDeviceIdleConstraint(this.mBluetoothConstraint, "bluetooth", 1);
        }
    }

    public void stop() {
        if (this.mBluetoothConstraint != null) {
            this.mDeviceIdleService.unregisterDeviceIdleConstraint(this.mBluetoothConstraint);
        }
    }
}
