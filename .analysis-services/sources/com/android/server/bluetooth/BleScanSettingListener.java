package com.android.server.bluetooth;

/* JADX INFO: compiled from: BleScanSettingListener.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nH\u0002J&\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u0010H\u0007R&\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00048\u0006@BX\u0087\u000e¢\u0006\u000e\n\u0000\u0012\u0004\b\u0006\u0010\u0002\u001a\u0004\b\u0005\u0010\u0007¨\u0006\u0011"}, d2 = {"Lcom/android/server/bluetooth/BleScanSettingListener;", "", "()V", "<set-?>", "", "isScanAllowed", "isScanAllowed$annotations", "()Z", "getScanSettingValue", "resolver", "Landroid/content/ContentResolver;", "initialize", "", "looper", "Landroid/os/Looper;", "callback", "Lkotlin/Function0;", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class BleScanSettingListener {
    public static final com.android.server.bluetooth.BleScanSettingListener INSTANCE = new com.android.server.bluetooth.BleScanSettingListener();
    private static boolean isScanAllowed;

    @kotlin.jvm.JvmStatic
    public static /* synthetic */ void isScanAllowed$annotations() {
    }

    private BleScanSettingListener() {
    }

    public static final boolean isScanAllowed() {
        return isScanAllowed;
    }

    @kotlin.jvm.JvmStatic
    public static final void initialize(android.os.Looper looper, final android.content.ContentResolver resolver, final kotlin.jvm.functions.Function0<kotlin.Unit> callback) {
        resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("ble_scan_always_enabled"), false, new android.database.ContentObserver(new android.os.Handler(looper)) { // from class: com.android.server.bluetooth.BleScanSettingListener.initialize.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                boolean previousValue = com.android.server.bluetooth.BleScanSettingListener.isScanAllowed();
                com.android.server.bluetooth.BleScanSettingListener.isScanAllowed = com.android.server.bluetooth.BleScanSettingListener.INSTANCE.getScanSettingValue(resolver);
                if (com.android.server.bluetooth.BleScanSettingListener.isScanAllowed()) {
                    com.android.server.bluetooth.Log.INSTANCE.i("BleScanSettingListener", "Ble Scan mode is now allowed. Nothing to do");
                } else if (previousValue == com.android.server.bluetooth.BleScanSettingListener.isScanAllowed()) {
                    com.android.server.bluetooth.Log.INSTANCE.i("BleScanSettingListener", "Ble Scan mode was already considered as false. Discarding");
                } else {
                    com.android.server.bluetooth.Log.INSTANCE.i("BleScanSettingListener", "Trigger callback to disable BLE_ONLY mode");
                    callback.invoke();
                }
            }
        });
        isScanAllowed = INSTANCE.getScanSettingValue(resolver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean getScanSettingValue(android.content.ContentResolver resolver) {
        try {
            return android.provider.Settings.Global.getInt(resolver, "ble_scan_always_enabled") != 0;
        } catch (android.provider.Settings.SettingNotFoundException e) {
            com.android.server.bluetooth.Log.INSTANCE.i("BleScanSettingListener", "Settings not found. Default to false");
            return false;
        }
    }
}
