package com.android.server.flags;

/* JADX INFO: loaded from: classes2.dex */
public class FlagOverrideStore {
    private static final java.lang.String KEYNAME_PREFIX = "flag|";
    private static final java.lang.String NAMESPACE_NAME_SEPARATOR = ".";
    private com.android.server.flags.FlagOverrideStore.FlagChangeCallback mCallback;
    private final com.android.server.flags.SettingsProxy mSettingsProxy;

    interface FlagChangeCallback {
        void onFlagChanged(java.lang.String str, java.lang.String str2, java.lang.String str3);
    }

    FlagOverrideStore(com.android.server.flags.SettingsProxy settingsProxy) {
        this.mSettingsProxy = settingsProxy;
    }

    void setChangeCallback(com.android.server.flags.FlagOverrideStore.FlagChangeCallback callback) {
        this.mCallback = callback;
    }

    boolean contains(java.lang.String namespace, java.lang.String name) {
        return get(namespace, name) != null;
    }

    public void set(java.lang.String namespace, java.lang.String name, java.lang.String value) {
        this.mSettingsProxy.putString(getPropName(namespace, name), value);
        this.mCallback.onFlagChanged(namespace, name, value);
    }

    public java.lang.String get(java.lang.String namespace, java.lang.String name) {
        return this.mSettingsProxy.getString(getPropName(namespace, name));
    }

    public void erase(java.lang.String namespace, java.lang.String name) {
        set(namespace, name, null);
    }

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getFlags() {
        return getFlagsForNamespace(null);
    }

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getFlagsForNamespace(java.lang.String namespace) {
        java.lang.String value;
        android.database.Cursor c = this.mSettingsProxy.getContentResolver().query(android.provider.Settings.Global.CONTENT_URI, new java.lang.String[]{"name", "value"}, null, null, null);
        if (c == null) {
            return java.util.Map.of();
        }
        int keynamePrefixLength = KEYNAME_PREFIX.length();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> results = new java.util.HashMap<>();
        while (c.moveToNext()) {
            java.lang.String key = c.getString(0);
            if (key.startsWith(KEYNAME_PREFIX) && key.indexOf(NAMESPACE_NAME_SEPARATOR, keynamePrefixLength) >= 0 && (value = c.getString(1)) != null && !value.isEmpty()) {
                java.lang.String ns = key.substring(keynamePrefixLength, key.indexOf(NAMESPACE_NAME_SEPARATOR));
                if (namespace == null || namespace.equals(ns)) {
                    java.lang.String name = key.substring(key.indexOf(NAMESPACE_NAME_SEPARATOR) + 1);
                    results.putIfAbsent(ns, new java.util.HashMap<>());
                    results.get(ns).put(name, value);
                }
            }
        }
        c.close();
        return results;
    }

    static java.lang.String getPropName(java.lang.String namespace, java.lang.String name) {
        return KEYNAME_PREFIX + namespace + NAMESPACE_NAME_SEPARATOR + name;
    }
}
