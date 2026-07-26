package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
class MtpNotificationManager {
    private static final java.lang.String ACTION_OPEN_IN_APPS = "com.android.server.usb.ACTION_OPEN_IN_APPS";
    private static final int PROTOCOL_MTP = 0;
    private static final int PROTOCOL_PTP = 1;
    private static final int SUBCLASS_MTP = 255;
    private static final int SUBCLASS_STILL_IMAGE_CAPTURE = 1;
    private static final java.lang.String TAG = "UsbMtpNotificationManager";
    private final android.content.Context mContext;
    private final com.android.server.usb.MtpNotificationManager.OnOpenInAppListener mListener;
    private final com.android.server.usb.MtpNotificationManager.Receiver mReceiver = new com.android.server.usb.MtpNotificationManager.Receiver();

    interface OnOpenInAppListener {
        void onOpenInApp(android.hardware.usb.UsbDevice usbDevice);
    }

    MtpNotificationManager(android.content.Context context, com.android.server.usb.MtpNotificationManager.OnOpenInAppListener listener) {
        this.mContext = context;
        this.mListener = listener;
        context.registerReceiver(this.mReceiver, new android.content.IntentFilter(ACTION_OPEN_IN_APPS));
    }

    void showNotification(android.hardware.usb.UsbDevice device) {
        android.content.res.Resources resources = this.mContext.getResources();
        java.lang.String title = resources.getString(android.R.string.system_error_manufacturer, device.getProductName());
        java.lang.String description = resources.getString(android.R.string.sync_undo_deletes);
        android.app.Notification.Builder builder = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.USB).setContentTitle(title).setContentText(description).setSmallIcon(android.R.drawable.spinner_ab_disabled_holo_dark).setCategory("sys");
        android.content.Intent intent = new android.content.Intent(ACTION_OPEN_IN_APPS);
        intent.putExtra("device", device);
        intent.addFlags(1342177280);
        android.app.PendingIntent openIntent = android.app.PendingIntent.getBroadcastAsUser(this.mContext, device.getDeviceId(), intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, android.os.UserHandle.SYSTEM);
        builder.setContentIntent(openIntent);
        android.app.Notification notification = builder.build();
        notification.flags |= 256;
        ((android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class)).notify(java.lang.Integer.toString(device.getDeviceId()), 25, notification);
    }

    void hideNotification(int deviceId) {
        ((android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class)).cancel(java.lang.Integer.toString(deviceId), 25);
    }

    private class Receiver extends android.content.BroadcastReceiver {
        private Receiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            byte b;
            android.hardware.usb.UsbDevice device = (android.hardware.usb.UsbDevice) intent.getExtras().getParcelable("device", android.hardware.usb.UsbDevice.class);
            if (device == null) {
            }
            java.lang.String action = intent.getAction();
            switch (action.hashCode()) {
                case 768361239:
                    if (action.equals(com.android.server.usb.MtpNotificationManager.ACTION_OPEN_IN_APPS)) {
                        b = 0;
                        break;
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    com.android.server.usb.MtpNotificationManager.this.mListener.onOpenInApp(device);
                    break;
            }
        }
    }

    static boolean shouldShowNotification(android.content.pm.PackageManager packageManager, android.hardware.usb.UsbDevice device) {
        return !packageManager.hasSystemFeature("android.hardware.type.automotive") && isMtpDevice(device);
    }

    private static boolean isMtpDevice(android.hardware.usb.UsbDevice device) {
        for (int i = 0; i < device.getInterfaceCount(); i++) {
            android.hardware.usb.UsbInterface usbInterface = device.getInterface(i);
            if (usbInterface.getInterfaceClass() == 6 && usbInterface.getInterfaceSubclass() == 1 && usbInterface.getInterfaceProtocol() == 1) {
                return true;
            }
            if (usbInterface.getInterfaceClass() == 255 && usbInterface.getInterfaceSubclass() == 255 && usbInterface.getInterfaceProtocol() == 0 && "MTP".equals(usbInterface.getName())) {
                return true;
            }
        }
        return false;
    }

    public void unregister() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }
}
