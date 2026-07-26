package com.android.server.bluetooth;

/* JADX INFO: loaded from: classes.dex */
class BluetoothServerProxy {
    private static com.android.server.bluetooth.BluetoothServerProxy sInstance;
    private static final java.lang.String TAG = com.android.server.bluetooth.BluetoothServerProxy.class.getSimpleName();
    private static final java.lang.Object INSTANCE_LOCK = new java.lang.Object();

    private BluetoothServerProxy() {
    }

    static com.android.server.bluetooth.BluetoothServerProxy getInstance() {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new com.android.server.bluetooth.BluetoothServerProxy();
            }
        }
        return sInstance;
    }

    static void setInstanceForTesting(com.android.server.bluetooth.BluetoothServerProxy proxy) {
        synchronized (INSTANCE_LOCK) {
            com.android.server.bluetooth.Log.d(TAG, "setInstanceForTesting(), set to " + proxy);
            sInstance = proxy;
        }
    }

    com.android.server.bluetooth.AdapterBinder createAdapterBinder(android.os.IBinder binder) {
        return new com.android.server.bluetooth.AdapterBinder(binder);
    }

    java.lang.String settingsSecureGetString(android.content.ContentResolver contentResolver, java.lang.String name) {
        return android.provider.Settings.Secure.getString(contentResolver, name);
    }

    int settingsGlobalGetInt(android.content.ContentResolver contentResolver, java.lang.String name, int def) {
        return android.provider.Settings.Global.getInt(contentResolver, name, def);
    }

    int getBluetoothPersistedState(android.content.ContentResolver resolver, int defaultValue) {
        return android.provider.Settings.Global.getInt(resolver, "bluetooth_on", defaultValue);
    }

    void setBluetoothPersistedState(android.content.ContentResolver resolver, int state) {
        com.android.server.bluetooth.Log.i(TAG, "setBluetoothPersistedState(" + state + ")");
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Global.putInt(resolver, "bluetooth_on", state);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }
}
