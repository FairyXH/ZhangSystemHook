package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class SettingsAdapter {
    public static com.android.server.audio.SettingsAdapter getDefaultAdapter() {
        return new com.android.server.audio.SettingsAdapter();
    }

    public int getGlobalInt(android.content.ContentResolver cr, java.lang.String name, int def) {
        return android.provider.Settings.Global.getInt(cr, name, def);
    }

    public java.lang.String getGlobalString(android.content.ContentResolver resolver, java.lang.String name) {
        return android.provider.Settings.Global.getString(resolver, name);
    }

    public boolean putGlobalInt(android.content.ContentResolver cr, java.lang.String name, int value) {
        return android.provider.Settings.Global.putInt(cr, name, value);
    }

    public boolean putGlobalString(android.content.ContentResolver resolver, java.lang.String name, java.lang.String value) {
        return android.provider.Settings.Global.putString(resolver, name, value);
    }

    public int getSystemIntForUser(android.content.ContentResolver cr, java.lang.String name, int def, int userHandle) {
        return android.provider.Settings.System.getIntForUser(cr, name, def, userHandle);
    }

    public boolean putSystemIntForUser(android.content.ContentResolver cr, java.lang.String name, int value, int userHandle) {
        return android.provider.Settings.System.putIntForUser(cr, name, value, userHandle);
    }

    public int getSecureIntForUser(android.content.ContentResolver cr, java.lang.String name, int def, int userHandle) {
        return android.provider.Settings.Secure.getIntForUser(cr, name, def, userHandle);
    }

    public java.lang.String getSecureStringForUser(android.content.ContentResolver resolver, java.lang.String name, int userHandle) {
        return android.provider.Settings.Secure.getStringForUser(resolver, name, userHandle);
    }

    public boolean putSecureIntForUser(android.content.ContentResolver cr, java.lang.String name, int value, int userHandle) {
        return android.provider.Settings.Secure.putIntForUser(cr, name, value, userHandle);
    }

    public boolean putSecureStringForUser(android.content.ContentResolver cr, java.lang.String name, java.lang.String value, int userHandle) {
        return android.provider.Settings.Secure.putStringForUser(cr, name, value, userHandle);
    }
}
