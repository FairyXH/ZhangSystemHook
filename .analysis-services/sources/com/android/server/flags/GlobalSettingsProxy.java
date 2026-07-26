package com.android.server.flags;

/* JADX INFO: loaded from: classes2.dex */
class GlobalSettingsProxy implements com.android.server.flags.SettingsProxy {
    private final android.content.ContentResolver mContentResolver;

    GlobalSettingsProxy(android.content.ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    @Override // com.android.server.flags.SettingsProxy
    public android.content.ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    @Override // com.android.server.flags.SettingsProxy
    public android.net.Uri getUriFor(java.lang.String name) {
        return android.provider.Settings.Global.getUriFor(name);
    }

    @Override // com.android.server.flags.SettingsProxy
    public java.lang.String getStringForUser(java.lang.String name, int userHandle) {
        return android.provider.Settings.Global.getStringForUser(this.mContentResolver, name, userHandle);
    }

    @Override // com.android.server.flags.SettingsProxy
    public boolean putString(java.lang.String name, java.lang.String value, boolean overrideableByRestore) {
        throw new java.lang.UnsupportedOperationException("This method only exists publicly for Settings.System and Settings.Secure");
    }

    @Override // com.android.server.flags.SettingsProxy
    public boolean putStringForUser(java.lang.String name, java.lang.String value, int userHandle) {
        return android.provider.Settings.Global.putStringForUser(this.mContentResolver, name, value, userHandle);
    }

    @Override // com.android.server.flags.SettingsProxy
    public boolean putStringForUser(java.lang.String name, java.lang.String value, java.lang.String tag, boolean makeDefault, int userHandle, boolean overrideableByRestore) {
        return android.provider.Settings.Global.putStringForUser(this.mContentResolver, name, value, tag, makeDefault, userHandle, overrideableByRestore);
    }

    @Override // com.android.server.flags.SettingsProxy
    public boolean putString(java.lang.String name, java.lang.String value, java.lang.String tag, boolean makeDefault) {
        return android.provider.Settings.Global.putString(this.mContentResolver, name, value, tag, makeDefault);
    }
}
