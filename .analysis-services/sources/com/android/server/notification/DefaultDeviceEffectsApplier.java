package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
class DefaultDeviceEffectsApplier implements android.service.notification.DeviceEffectsApplier {
    private static final int SATURATION_LEVEL_FULL_COLOR = 100;
    private static final int SATURATION_LEVEL_GRAYSCALE = 0;
    private static final android.content.IntentFilter SCREEN_OFF_INTENT_FILTER = new android.content.IntentFilter("android.intent.action.SCREEN_OFF");
    private static final java.lang.String SUPPRESS_AMBIENT_DISPLAY_TOKEN = "DefaultDeviceEffectsApplier:SuppressAmbientDisplay";
    private static final java.lang.String TAG = "DeviceEffectsApplier";
    private static final float WALLPAPER_DIM_AMOUNT_DIMMED = 0.6f;
    private static final float WALLPAPER_DIM_AMOUNT_NORMAL = 0.0f;
    private final android.hardware.display.ColorDisplayManager mColorDisplayManager;
    private final android.content.Context mContext;
    private boolean mIsScreenOffReceiverRegistered;
    private boolean mPendingNightMode;
    private final android.os.PowerManager mPowerManager;
    private final android.app.UiModeManager mUiModeManager;
    private final android.app.WallpaperManager mWallpaperManager;
    private final java.lang.Object mRegisterReceiverLock = new java.lang.Object();
    private android.service.notification.ZenDeviceEffects mLastAppliedEffects = new android.service.notification.ZenDeviceEffects.Builder().build();
    private final android.content.BroadcastReceiver mNightModeWhenScreenOff = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.DefaultDeviceEffectsApplier.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.notification.DefaultDeviceEffectsApplier.this.unregisterScreenOffReceiver();
            com.android.server.notification.DefaultDeviceEffectsApplier.this.updateNightModeImmediately(com.android.server.notification.DefaultDeviceEffectsApplier.this.mPendingNightMode);
        }
    };

    DefaultDeviceEffectsApplier(android.content.Context context) {
        this.mContext = context;
        this.mColorDisplayManager = (android.hardware.display.ColorDisplayManager) context.getSystemService(android.hardware.display.ColorDisplayManager.class);
        this.mPowerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        this.mUiModeManager = (android.app.UiModeManager) context.getSystemService(android.app.UiModeManager.class);
        android.app.WallpaperManager wallpaperManager = (android.app.WallpaperManager) context.getSystemService(android.app.WallpaperManager.class);
        this.mWallpaperManager = (wallpaperManager == null || !wallpaperManager.isWallpaperSupported()) ? null : wallpaperManager;
    }

    public void apply(final android.service.notification.ZenDeviceEffects effects, final int origin) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.DefaultDeviceEffectsApplier$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$apply$0(effects, origin);
            }
        });
        this.mLastAppliedEffects = effects;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$apply$0(android.service.notification.ZenDeviceEffects effects, int origin) throws java.lang.Exception {
        if (this.mLastAppliedEffects.shouldSuppressAmbientDisplay() != effects.shouldSuppressAmbientDisplay()) {
            try {
                this.mPowerManager.suppressAmbientDisplay(SUPPRESS_AMBIENT_DISPLAY_TOKEN, effects.shouldSuppressAmbientDisplay());
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Could not change AOD override", e);
            }
        }
        if (this.mLastAppliedEffects.shouldDisplayGrayscale() != effects.shouldDisplayGrayscale() && this.mColorDisplayManager != null) {
            try {
                this.mColorDisplayManager.setSaturationLevel(effects.shouldDisplayGrayscale() ? 0 : 100);
            } catch (java.lang.Exception e2) {
                android.util.Slog.e(TAG, "Could not change grayscale override", e2);
            }
        }
        if (this.mLastAppliedEffects.shouldDimWallpaper() != effects.shouldDimWallpaper() && this.mWallpaperManager != null) {
            try {
                this.mWallpaperManager.setWallpaperDimAmount(effects.shouldDimWallpaper() ? WALLPAPER_DIM_AMOUNT_DIMMED : 0.0f);
            } catch (java.lang.Exception e3) {
                android.util.Slog.e(TAG, "Could not change wallpaper override", e3);
            }
        }
        if (this.mLastAppliedEffects.shouldUseNightMode() != effects.shouldUseNightMode()) {
            try {
                updateOrScheduleNightMode(effects.shouldUseNightMode(), origin);
            } catch (java.lang.Exception e4) {
                android.util.Slog.e(TAG, "Could not change dark theme override", e4);
            }
        }
    }

    private void updateOrScheduleNightMode(boolean useNightMode, int origin) {
        this.mPendingNightMode = useNightMode;
        if (origin == 1 || origin == 2 || origin == 3 || !this.mPowerManager.isInteractive()) {
            unregisterScreenOffReceiver();
            updateNightModeImmediately(useNightMode);
        } else {
            registerScreenOffReceiver();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNightModeImmediately(final boolean useNightMode) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.DefaultDeviceEffectsApplier$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$updateNightModeImmediately$1(useNightMode);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateNightModeImmediately$1(boolean useNightMode) throws java.lang.Exception {
        try {
            this.mUiModeManager.setAttentionModeThemeOverlay(useNightMode ? 1001 : 1000);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Could not change wallpaper override", e);
        }
    }

    private void registerScreenOffReceiver() {
        synchronized (this.mRegisterReceiverLock) {
            if (!this.mIsScreenOffReceiverRegistered) {
                this.mContext.registerReceiver(this.mNightModeWhenScreenOff, SCREEN_OFF_INTENT_FILTER, 4);
                this.mIsScreenOffReceiverRegistered = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterScreenOffReceiver() {
        synchronized (this.mRegisterReceiverLock) {
            if (this.mIsScreenOffReceiverRegistered) {
                this.mIsScreenOffReceiverRegistered = false;
                this.mContext.unregisterReceiver(this.mNightModeWhenScreenOff);
            }
        }
    }
}
