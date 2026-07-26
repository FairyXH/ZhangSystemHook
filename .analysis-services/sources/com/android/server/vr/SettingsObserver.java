package com.android.server.vr;

/* JADX INFO: loaded from: classes3.dex */
public class SettingsObserver {
    private final android.database.ContentObserver mContentObserver;
    private final java.lang.String mSecureSettingName;
    private final android.content.BroadcastReceiver mSettingRestoreReceiver;
    private final java.util.Set<com.android.server.vr.SettingsObserver.SettingChangeListener> mSettingsListeners = new android.util.ArraySet();

    public interface SettingChangeListener {
        void onSettingChanged();

        void onSettingRestored(java.lang.String str, java.lang.String str2, int i);
    }

    private SettingsObserver(android.content.Context context, android.os.Handler handler, final android.net.Uri settingUri, final java.lang.String secureSettingName) {
        this.mSecureSettingName = secureSettingName;
        this.mSettingRestoreReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.vr.SettingsObserver.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.os.action.SETTING_RESTORED".equals(intent.getAction())) {
                    java.lang.String element = intent.getStringExtra("setting_name");
                    if (java.util.Objects.equals(element, secureSettingName)) {
                        java.lang.String prevValue = intent.getStringExtra("previous_value");
                        java.lang.String newValue = intent.getStringExtra("new_value");
                        com.android.server.vr.SettingsObserver.this.sendSettingRestored(prevValue, newValue, getSendingUserId());
                    }
                }
            }
        };
        this.mContentObserver = new android.database.ContentObserver(handler) { // from class: com.android.server.vr.SettingsObserver.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, android.net.Uri uri) {
                if (uri == null || settingUri.equals(uri)) {
                    com.android.server.vr.SettingsObserver.this.sendSettingChanged();
                }
            }
        };
        android.content.ContentResolver resolver = context.getContentResolver();
        resolver.registerContentObserver(settingUri, false, this.mContentObserver, -1);
    }

    public static com.android.server.vr.SettingsObserver build(android.content.Context context, android.os.Handler handler, java.lang.String settingName) {
        android.net.Uri settingUri = android.provider.Settings.Secure.getUriFor(settingName);
        return new com.android.server.vr.SettingsObserver(context, handler, settingUri, settingName);
    }

    public void addListener(com.android.server.vr.SettingsObserver.SettingChangeListener listener) {
        this.mSettingsListeners.add(listener);
    }

    public void removeListener(com.android.server.vr.SettingsObserver.SettingChangeListener listener) {
        this.mSettingsListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSettingChanged() {
        for (com.android.server.vr.SettingsObserver.SettingChangeListener l : this.mSettingsListeners) {
            l.onSettingChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSettingRestored(java.lang.String prevValue, java.lang.String newValue, int userId) {
        for (com.android.server.vr.SettingsObserver.SettingChangeListener l : this.mSettingsListeners) {
            l.onSettingRestored(prevValue, newValue, userId);
        }
    }
}
