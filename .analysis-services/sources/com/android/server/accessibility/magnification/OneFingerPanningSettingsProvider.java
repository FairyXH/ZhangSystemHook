package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class OneFingerPanningSettingsProvider {
    static final java.lang.String KEY = "accessibility_single_finger_panning_enabled";
    private static final android.net.Uri URI = android.provider.Settings.Secure.getUriFor(KEY);
    private java.util.concurrent.atomic.AtomicBoolean mCached = new java.util.concurrent.atomic.AtomicBoolean();
    android.content.ContentResolver mContentResolver;
    android.database.ContentObserver mObserver;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface State {
        public static final int OFF = 0;
        public static final int ON = 1;
    }

    public OneFingerPanningSettingsProvider(final android.content.Context context, boolean featureFlagEnabled) {
        final boolean defaultValue = isOneFingerPanningEnabledDefault(context);
        if (featureFlagEnabled) {
            this.mContentResolver = context.getContentResolver();
            this.mObserver = new android.database.ContentObserver(context.getMainThreadHandler()) { // from class: com.android.server.accessibility.magnification.OneFingerPanningSettingsProvider.1
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange) {
                    com.android.server.accessibility.magnification.OneFingerPanningSettingsProvider.this.mCached.set(com.android.server.accessibility.magnification.OneFingerPanningSettingsProvider.this.isOneFingerPanningEnabledInSetting(context, defaultValue));
                }
            };
            this.mCached.set(isOneFingerPanningEnabledInSetting(context, defaultValue));
            this.mContentResolver.registerContentObserver(URI, false, this.mObserver);
            return;
        }
        this.mCached.set(defaultValue);
    }

    public boolean isOneFingerPanningEnabled() {
        return this.mCached.get();
    }

    public void unregister() {
        if (this.mContentResolver != null) {
            this.mContentResolver.unregisterContentObserver(this.mObserver);
        }
        this.mContentResolver = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOneFingerPanningEnabledInSetting(android.content.Context context, boolean z) {
        return 1 == android.provider.Settings.Secure.getIntForUser(this.mContentResolver, KEY, z ? 1 : 0, context.getUserId());
    }

    static boolean isOneFingerPanningEnabledDefault(android.content.Context context) {
        try {
            boolean oneFingerPanningDefaultValue = context.getResources().getBoolean(android.R.bool.config_enableServerNotificationEffectsForAutomotive);
            return oneFingerPanningDefaultValue;
        } catch (android.content.res.Resources.NotFoundException e) {
            return false;
        }
    }
}
