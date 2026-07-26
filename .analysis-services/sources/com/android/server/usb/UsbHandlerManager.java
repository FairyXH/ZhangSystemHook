package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class UsbHandlerManager {
    private static final java.lang.String LOG_TAG = com.android.server.usb.UsbHandlerManager.class.getSimpleName();
    private final android.content.Context mContext;

    UsbHandlerManager(android.content.Context context) {
        this.mContext = context;
    }

    void showUsbAccessoryUriActivity(android.hardware.usb.UsbAccessory accessory, android.os.UserHandle user) {
        java.lang.String uri = accessory.getUri();
        if (uri != null && uri.length() > 0) {
            android.content.Intent dialogIntent = createDialogIntent();
            dialogIntent.setComponent(android.content.ComponentName.unflattenFromString(this.mContext.getResources().getString(android.R.string.config_usbResolverActivity)));
            dialogIntent.putExtra("accessory", accessory);
            dialogIntent.putExtra("uri", uri);
            try {
                this.mContext.startActivityAsUser(dialogIntent, user);
            } catch (android.content.ActivityNotFoundException e) {
                android.util.Slog.e(LOG_TAG, "unable to start UsbAccessoryUriActivity");
            }
        }
    }

    void confirmUsbHandler(android.content.pm.ResolveInfo rInfo, android.hardware.usb.UsbDevice device, android.hardware.usb.UsbAccessory accessory) {
        android.content.Intent resolverIntent = createDialogIntent();
        resolverIntent.setComponent(android.content.ComponentName.unflattenFromString(this.mContext.getResources().getString(android.R.string.config_useragentprofile_url)));
        resolverIntent.putExtra("rinfo", rInfo);
        android.os.UserHandle user = android.os.UserHandle.getUserHandleForUid(rInfo.activityInfo.applicationInfo.uid);
        if (device != null) {
            resolverIntent.putExtra("device", device);
        } else {
            resolverIntent.putExtra("accessory", accessory);
        }
        try {
            this.mContext.startActivityAsUser(resolverIntent, user);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Slog.e(LOG_TAG, "unable to start activity " + resolverIntent, e);
        }
    }

    void selectUsbHandler(java.util.ArrayList<android.content.pm.ResolveInfo> matches, android.os.UserHandle user, android.content.Intent intent) {
        android.content.Intent resolverIntent = createDialogIntent();
        resolverIntent.setComponent(android.content.ComponentName.unflattenFromString(this.mContext.getResources().getString(android.R.string.config_wallpaperManagerServiceName)));
        resolverIntent.putParcelableArrayListExtra("rlist", matches);
        resolverIntent.putExtra("android.intent.extra.INTENT", intent);
        try {
            this.mContext.startActivityAsUser(resolverIntent, user);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Slog.e(LOG_TAG, "unable to start activity " + resolverIntent, e);
        }
    }

    private android.content.Intent createDialogIntent() {
        android.content.Intent intent = new android.content.Intent();
        intent.addFlags(268435456);
        return intent;
    }
}
