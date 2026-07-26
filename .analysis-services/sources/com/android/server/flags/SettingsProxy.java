package com.android.server.flags;

/* JADX INFO: loaded from: classes2.dex */
public interface SettingsProxy {
    android.content.ContentResolver getContentResolver();

    java.lang.String getStringForUser(java.lang.String str, int i);

    android.net.Uri getUriFor(java.lang.String str);

    boolean putString(java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z);

    boolean putString(java.lang.String str, java.lang.String str2, boolean z);

    boolean putStringForUser(java.lang.String str, java.lang.String str2, int i);

    boolean putStringForUser(java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z, int i, boolean z2);

    default int getUserId() {
        return getContentResolver().getUserId();
    }

    default java.lang.String getString(java.lang.String name) {
        return getStringForUser(name, getUserId());
    }

    default boolean putString(java.lang.String name, java.lang.String value) {
        return putStringForUser(name, value, getUserId());
    }

    default int getIntForUser(java.lang.String name, int def, int userHandle) {
        java.lang.String v = getStringForUser(name, userHandle);
        if (v == null) {
            return def;
        }
        try {
            return java.lang.Integer.parseInt(v);
        } catch (java.lang.NumberFormatException e) {
            return def;
        }
    }

    default int getInt(java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
        return getIntForUser(name, getUserId());
    }

    default int getIntForUser(java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
        java.lang.String v = getStringForUser(name, userHandle);
        try {
            return java.lang.Integer.parseInt(v);
        } catch (java.lang.NumberFormatException e) {
            throw new android.provider.Settings.SettingNotFoundException(name);
        }
    }

    default boolean putInt(java.lang.String name, int value) {
        return putIntForUser(name, value, getUserId());
    }

    default boolean putIntForUser(java.lang.String name, int value, int userHandle) {
        return putStringForUser(name, java.lang.Integer.toString(value), userHandle);
    }

    default boolean getBool(java.lang.String name, boolean def) {
        return getBoolForUser(name, def, getUserId());
    }

    default boolean getBoolForUser(java.lang.String str, boolean z, int i) {
        return getIntForUser(str, z ? 1 : 0, i) != 0;
    }

    default boolean getBool(java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
        return getBoolForUser(name, getUserId());
    }

    default boolean getBoolForUser(java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
        return getIntForUser(name, userHandle) != 0;
    }

    default boolean putBool(java.lang.String name, boolean value) {
        return putBoolForUser(name, value, getUserId());
    }

    default boolean putBoolForUser(java.lang.String str, boolean z, int i) {
        return putIntForUser(str, z ? 1 : 0, i);
    }

    default long getLong(java.lang.String name, long def) {
        return getLongForUser(name, def, getUserId());
    }

    default long getLongForUser(java.lang.String name, long def, int userHandle) {
        long j;
        java.lang.String valString = getStringForUser(name, userHandle);
        if (valString == null) {
            j = def;
        } else {
            try {
                j = java.lang.Long.parseLong(valString);
            } catch (java.lang.NumberFormatException e) {
                return def;
            }
        }
        long value = j;
        return value;
    }

    default long getLong(java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
        return getLongForUser(name, getUserId());
    }

    default long getLongForUser(java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
        java.lang.String valString = getStringForUser(name, userHandle);
        try {
            return java.lang.Long.parseLong(valString);
        } catch (java.lang.NumberFormatException e) {
            throw new android.provider.Settings.SettingNotFoundException(name);
        }
    }

    default boolean putLong(java.lang.String name, long value) {
        return putLongForUser(name, value, getUserId());
    }

    default boolean putLongForUser(java.lang.String name, long value, int userHandle) {
        return putStringForUser(name, java.lang.Long.toString(value), userHandle);
    }

    default float getFloat(java.lang.String name, float def) {
        return getFloatForUser(name, def, getUserId());
    }

    default float getFloatForUser(java.lang.String name, float def, int userHandle) {
        java.lang.String v = getStringForUser(name, userHandle);
        if (v == null) {
            return def;
        }
        try {
            return java.lang.Float.parseFloat(v);
        } catch (java.lang.NumberFormatException e) {
            return def;
        }
    }

    default float getFloat(java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
        return getFloatForUser(name, getUserId());
    }

    default float getFloatForUser(java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
        java.lang.String v = getStringForUser(name, userHandle);
        if (v == null) {
            throw new android.provider.Settings.SettingNotFoundException(name);
        }
        try {
            return java.lang.Float.parseFloat(v);
        } catch (java.lang.NumberFormatException e) {
            throw new android.provider.Settings.SettingNotFoundException(name);
        }
    }

    default boolean putFloat(java.lang.String name, float value) {
        return putFloatForUser(name, value, getUserId());
    }

    default boolean putFloatForUser(java.lang.String name, float value, int userHandle) {
        return putStringForUser(name, java.lang.Float.toString(value), userHandle);
    }

    default void registerContentObserver(java.lang.String name, android.database.ContentObserver settingsObserver) {
        registerContentObserver(getUriFor(name), settingsObserver);
    }

    default void registerContentObserver(android.net.Uri uri, android.database.ContentObserver settingsObserver) {
        registerContentObserverForUser(uri, settingsObserver, getUserId());
    }

    default void registerContentObserver(java.lang.String name, boolean notifyForDescendants, android.database.ContentObserver settingsObserver) {
        registerContentObserver(getUriFor(name), notifyForDescendants, settingsObserver);
    }

    default void registerContentObserver(android.net.Uri uri, boolean notifyForDescendants, android.database.ContentObserver settingsObserver) {
        registerContentObserverForUser(uri, notifyForDescendants, settingsObserver, getUserId());
    }

    default void registerContentObserverForUser(java.lang.String name, android.database.ContentObserver settingsObserver, int userHandle) {
        registerContentObserverForUser(getUriFor(name), settingsObserver, userHandle);
    }

    default void registerContentObserverForUser(android.net.Uri uri, android.database.ContentObserver settingsObserver, int userHandle) {
        registerContentObserverForUser(uri, false, settingsObserver, userHandle);
    }

    default void registerContentObserverForUser(java.lang.String name, boolean notifyForDescendants, android.database.ContentObserver settingsObserver, int userHandle) {
        registerContentObserverForUser(getUriFor(name), notifyForDescendants, settingsObserver, userHandle);
    }

    default void registerContentObserverForUser(android.net.Uri uri, boolean notifyForDescendants, android.database.ContentObserver settingsObserver, int userHandle) {
        getContentResolver().registerContentObserver(uri, notifyForDescendants, settingsObserver, userHandle);
    }

    default void unregisterContentObserver(android.database.ContentObserver settingsObserver) {
        getContentResolver().unregisterContentObserver(settingsObserver);
    }
}
