package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class CaptioningManagerImpl implements android.view.accessibility.CaptioningManager.SystemAudioCaptioningAccessing {
    private static final boolean SYSTEM_AUDIO_CAPTIONING_UI_DEFAULT_ENABLED = false;
    private final android.content.Context mContext;

    public CaptioningManagerImpl(android.content.Context context) {
        this.mContext = context;
    }

    public void setSystemAudioCaptioningEnabled(boolean isEnabled, int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "odi_captions_enabled", isEnabled ? 1 : 0, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean isSystemAudioCaptioningUiEnabled(int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "odi_captions_volume_ui_enabled", 0, userId) == 1;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void setSystemAudioCaptioningUiEnabled(boolean isEnabled, int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "odi_captions_volume_ui_enabled", isEnabled ? 1 : 0, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }
}
