package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class LetterboxConfiguration {
    static final float DEFAULT_LETTERBOX_ASPECT_RATIO_FOR_MULTI_WINDOW = 1.01f;
    private static final boolean DEFAULT_VALUE_ALLOW_IGNORE_ORIENTATION_REQUEST = true;
    private static final boolean DEFAULT_VALUE_ENABLE_CAMERA_COMPAT_TREATMENT = true;
    private static final boolean DEFAULT_VALUE_ENABLE_COMPAT_FAKE_FOCUS = true;
    private static final boolean DEFAULT_VALUE_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY = true;
    private static final boolean DEFAULT_VALUE_ENABLE_LETTERBOX_BACKGROUND_WALLPAPER = false;
    private static final boolean DEFAULT_VALUE_ENABLE_LETTERBOX_TRANSLUCENT_ACTIVITY = true;
    private static final boolean DEFAULT_VALUE_ENABLE_USER_ASPECT_RATIO_FULLSCREEN = true;
    private static final boolean DEFAULT_VALUE_ENABLE_USER_ASPECT_RATIO_SETTINGS = true;
    private static final java.lang.String KEY_ALLOW_IGNORE_ORIENTATION_REQUEST = "allow_ignore_orientation_request";
    private static final java.lang.String KEY_ENABLE_CAMERA_COMPAT_TREATMENT = "enable_compat_camera_treatment";
    private static final java.lang.String KEY_ENABLE_COMPAT_FAKE_FOCUS = "enable_compat_fake_focus";
    private static final java.lang.String KEY_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY = "enable_display_rotation_immersive_app_compat_policy";
    private static final java.lang.String KEY_ENABLE_LETTERBOX_BACKGROUND_WALLPAPER = "enable_letterbox_background_wallpaper";
    private static final java.lang.String KEY_ENABLE_LETTERBOX_TRANSLUCENT_ACTIVITY = "enable_letterbox_translucent_activity";
    private static final java.lang.String KEY_ENABLE_USER_ASPECT_RATIO_FULLSCREEN = "enable_app_compat_user_aspect_ratio_fullscreen";
    private static final java.lang.String KEY_ENABLE_USER_ASPECT_RATIO_SETTINGS = "enable_app_compat_aspect_ratio_user_settings";
    static final int LETTERBOX_BACKGROUND_APP_COLOR_BACKGROUND = 1;
    static final int LETTERBOX_BACKGROUND_APP_COLOR_BACKGROUND_FLOATING = 2;
    static final int LETTERBOX_BACKGROUND_OVERRIDE_UNSET = -1;
    static final int LETTERBOX_BACKGROUND_SOLID_COLOR = 0;
    static final int LETTERBOX_BACKGROUND_WALLPAPER = 3;
    static final int LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_CENTER = 1;
    static final int LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_LEFT = 0;
    static final int LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_RIGHT = 2;
    static final float LETTERBOX_POSITION_MULTIPLIER_CENTER = 0.5f;
    static final int LETTERBOX_VERTICAL_REACHABILITY_POSITION_BOTTOM = 2;
    static final int LETTERBOX_VERTICAL_REACHABILITY_POSITION_CENTER = 1;
    static final int LETTERBOX_VERTICAL_REACHABILITY_POSITION_TOP = 0;
    static final float MIN_FIXED_ORIENTATION_LETTERBOX_ASPECT_RATIO = 1.0f;
    private static final java.lang.String TAG = "ActivityTaskManager";
    final android.content.Context mContext;
    private float mDefaultMinAspectRatioForUnresizableApps;
    private int mDefaultPositionForHorizontalReachability;
    private int mDefaultPositionForVerticalReachability;
    private final com.android.server.wm.SynchedDeviceConfig mDeviceConfig;
    private float mFixedOrientationLetterboxAspectRatio;
    private boolean mIsAutomaticReachabilityInBookModeEnabled;
    private boolean mIsCameraCompatRefreshCycleThroughStopEnabled;
    private final boolean mIsCameraCompatSplitScreenAspectRatioEnabled;
    private boolean mIsCameraCompatTreatmentRefreshEnabled;
    private boolean mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox;
    private boolean mIsEducationEnabled;
    private boolean mIsHorizontalReachabilityEnabled;
    private final boolean mIsPolicyForIgnoringRequestedOrientationEnabled;
    private boolean mIsSplitScreenAspectRatioForUnresizableAppsEnabled;
    private boolean mIsVerticalReachabilityEnabled;
    private int mLetterboxActivityCornersRadius;
    private android.graphics.Color mLetterboxBackgroundColorOverride;
    private java.lang.Integer mLetterboxBackgroundColorResourceIdOverride;
    private final int mLetterboxBackgroundType;
    private int mLetterboxBackgroundTypeOverride;
    private int mLetterboxBackgroundWallpaperBlurRadiusPx;
    private float mLetterboxBackgroundWallpaperDarkScrimAlpha;
    private float mLetterboxBookModePositionMultiplier;
    private final com.android.server.wm.LetterboxConfigurationPersister mLetterboxConfigurationPersister;
    private float mLetterboxHorizontalPositionMultiplier;
    private float mLetterboxTabletopModePositionMultiplier;
    private float mLetterboxVerticalPositionMultiplier;
    private final com.android.server.wm.LetterboxConfiguration.DimenPxIntSupplier mThinLetterboxHeightPxSupplier;
    private final com.android.server.wm.LetterboxConfiguration.DimenPxIntSupplier mThinLetterboxWidthPxSupplier;
    private boolean mTranslucentLetterboxingOverrideEnabled;
    private boolean mUserAppAspectRatioFullscreenOverrideEnabled;
    private boolean mUserAppAspectRatioSettingsOverrideEnabled;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface LetterboxBackgroundType {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface LetterboxHorizontalReachabilityPosition {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface LetterboxVerticalReachabilityPosition {
    }

    private static class DimenPxIntSupplier implements java.util.function.IntSupplier {
        private final android.content.Context mContext;
        private float mLastDensity;
        private final int mResourceId;
        private int mValue;

        private DimenPxIntSupplier(android.content.Context context, int resourceId) {
            this.mLastDensity = Float.MIN_VALUE;
            this.mValue = 0;
            this.mContext = context;
            this.mResourceId = resourceId;
        }

        @Override // java.util.function.IntSupplier
        public int getAsInt() {
            float newDensity = this.mContext.getResources().getDisplayMetrics().density;
            if (newDensity != this.mLastDensity) {
                this.mLastDensity = newDensity;
                this.mValue = this.mContext.getResources().getDimensionPixelSize(this.mResourceId);
            }
            return this.mValue;
        }
    }

    LetterboxConfiguration(final android.content.Context systemUiContext) {
        this(systemUiContext, new com.android.server.wm.LetterboxConfigurationPersister(new java.util.function.Supplier() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Integer.valueOf(com.android.server.wm.LetterboxConfiguration.readLetterboxHorizontalReachabilityPositionFromConfig(systemUiContext, false));
            }
        }, new java.util.function.Supplier() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Integer.valueOf(com.android.server.wm.LetterboxConfiguration.readLetterboxVerticalReachabilityPositionFromConfig(systemUiContext, false));
            }
        }, new java.util.function.Supplier() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Integer.valueOf(com.android.server.wm.LetterboxConfiguration.readLetterboxHorizontalReachabilityPositionFromConfig(systemUiContext, true));
            }
        }, new java.util.function.Supplier() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Integer.valueOf(com.android.server.wm.LetterboxConfiguration.readLetterboxVerticalReachabilityPositionFromConfig(systemUiContext, true));
            }
        }));
    }

    LetterboxConfiguration(android.content.Context systemUiContext, com.android.server.wm.LetterboxConfigurationPersister letterboxConfigurationPersister) {
        this.mLetterboxBackgroundTypeOverride = -1;
        this.mIsCameraCompatTreatmentRefreshEnabled = true;
        this.mIsCameraCompatRefreshCycleThroughStopEnabled = true;
        this.mContext = systemUiContext;
        this.mFixedOrientationLetterboxAspectRatio = this.mContext.getResources().getFloat(android.R.dimen.chooser_width);
        this.mLetterboxBackgroundType = readLetterboxBackgroundTypeFromConfig(this.mContext);
        this.mLetterboxActivityCornersRadius = this.mContext.getResources().getInteger(android.R.integer.config_flipToScreenOffMaxLatencyMicros);
        this.mLetterboxBackgroundWallpaperBlurRadiusPx = this.mContext.getResources().getDimensionPixelSize(android.R.dimen.config_backGestureInset);
        this.mLetterboxBackgroundWallpaperDarkScrimAlpha = this.mContext.getResources().getFloat(android.R.dimen.config_autoKeyboardBrightnessSmoothingConstant);
        setLetterboxHorizontalPositionMultiplier(this.mContext.getResources().getFloat(android.R.dimen.config_buttonCornerRadius));
        setLetterboxVerticalPositionMultiplier(this.mContext.getResources().getFloat(android.R.dimen.config_dialogCornerRadius));
        setLetterboxBookModePositionMultiplier(this.mContext.getResources().getFloat(android.R.dimen.config_batterySaver_full_adjustBrightnessFactor));
        setLetterboxTabletopModePositionMultiplier(this.mContext.getResources().getFloat(android.R.dimen.config_closeToSquareDisplayMaxAspectRatio));
        this.mIsHorizontalReachabilityEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_isDesktopModeSupported);
        this.mIsVerticalReachabilityEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_isPreApprovalRequestAvailable);
        this.mIsAutomaticReachabilityInBookModeEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_ignoreVibrationsOnWirelessCharger);
        this.mDefaultPositionForHorizontalReachability = readLetterboxHorizontalReachabilityPositionFromConfig(this.mContext, false);
        this.mDefaultPositionForVerticalReachability = readLetterboxVerticalReachabilityPositionFromConfig(this.mContext, false);
        this.mIsEducationEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_isCompatFakeFocusEnabled);
        setDefaultMinAspectRatioForUnresizableApps(this.mContext.getResources().getFloat(android.R.dimen.config_bottomDialogCornerRadius));
        this.mIsSplitScreenAspectRatioForUnresizableAppsEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_isMainUserPermanentAdmin);
        this.mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox = this.mContext.getResources().getBoolean(android.R.bool.config_imeDrawsImeNavBar);
        this.mIsCameraCompatSplitScreenAspectRatioEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_handleVolumeKeysInWindowManager);
        this.mIsPolicyForIgnoringRequestedOrientationEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_isDisplayHingeAlwaysSeparating);
        this.mThinLetterboxWidthPxSupplier = new com.android.server.wm.LetterboxConfiguration.DimenPxIntSupplier(this.mContext, android.R.dimen.config_defaultBinderHeavyHitterWatcherThreshold);
        this.mThinLetterboxHeightPxSupplier = new com.android.server.wm.LetterboxConfiguration.DimenPxIntSupplier(this.mContext, android.R.dimen.config_defaultBinderHeavyHitterAutoSamplerThreshold);
        this.mLetterboxConfigurationPersister = letterboxConfigurationPersister;
        this.mLetterboxConfigurationPersister.start();
        this.mDeviceConfig = com.android.server.wm.SynchedDeviceConfig.builder("window_manager", systemUiContext.getMainExecutor()).addDeviceConfigEntry(KEY_ENABLE_CAMERA_COMPAT_TREATMENT, true, this.mContext.getResources().getBoolean(android.R.bool.config_hasPermanentDpad)).addDeviceConfigEntry(KEY_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY, true, this.mContext.getResources().getBoolean(android.R.bool.config_intrusiveNotificationLed)).addDeviceConfigEntry(KEY_ALLOW_IGNORE_ORIENTATION_REQUEST, true, true).addDeviceConfigEntry(KEY_ENABLE_COMPAT_FAKE_FOCUS, true, this.mContext.getResources().getBoolean(android.R.bool.config_goToSleepOnButtonPressTheaterMode)).addDeviceConfigEntry(KEY_ENABLE_LETTERBOX_TRANSLUCENT_ACTIVITY, true, this.mContext.getResources().getBoolean(android.R.bool.config_isDesktopModeDevOptionSupported)).addDeviceConfigEntry(KEY_ENABLE_USER_ASPECT_RATIO_SETTINGS, true, this.mContext.getResources().getBoolean(android.R.bool.config_animateScreenLights)).addDeviceConfigEntry(KEY_ENABLE_LETTERBOX_BACKGROUND_WALLPAPER, false, true).addDeviceConfigEntry(KEY_ENABLE_USER_ASPECT_RATIO_FULLSCREEN, true, this.mContext.getResources().getBoolean(android.R.bool.config_am_disablePssProfiling)).build();
    }

    boolean isIgnoreOrientationRequestAllowed() {
        return this.mDeviceConfig.getFlagValue(KEY_ALLOW_IGNORE_ORIENTATION_REQUEST);
    }

    void setFixedOrientationLetterboxAspectRatio(float aspectRatio) {
        this.mFixedOrientationLetterboxAspectRatio = aspectRatio;
    }

    void resetFixedOrientationLetterboxAspectRatio() {
        this.mFixedOrientationLetterboxAspectRatio = this.mContext.getResources().getFloat(android.R.dimen.chooser_width);
    }

    float getFixedOrientationLetterboxAspectRatio() {
        return this.mFixedOrientationLetterboxAspectRatio;
    }

    void resetDefaultMinAspectRatioForUnresizableApps() {
        setDefaultMinAspectRatioForUnresizableApps(this.mContext.getResources().getFloat(android.R.dimen.config_bottomDialogCornerRadius));
    }

    float getDefaultMinAspectRatioForUnresizableApps() {
        return this.mDefaultMinAspectRatioForUnresizableApps;
    }

    void setDefaultMinAspectRatioForUnresizableApps(float aspectRatio) {
        this.mDefaultMinAspectRatioForUnresizableApps = aspectRatio;
    }

    void setLetterboxActivityCornersRadius(int cornersRadius) {
        this.mLetterboxActivityCornersRadius = cornersRadius;
    }

    void resetLetterboxActivityCornersRadius() {
        this.mLetterboxActivityCornersRadius = this.mContext.getResources().getInteger(android.R.integer.config_flipToScreenOffMaxLatencyMicros);
    }

    boolean isLetterboxActivityCornersRounded() {
        return getLetterboxActivityCornersRadius() != 0;
    }

    int getLetterboxActivityCornersRadius() {
        return this.mLetterboxActivityCornersRadius;
    }

    android.graphics.Color getLetterboxBackgroundColor() {
        int colorId;
        if (this.mLetterboxBackgroundColorOverride != null) {
            return this.mLetterboxBackgroundColorOverride;
        }
        if (this.mLetterboxBackgroundColorResourceIdOverride != null) {
            colorId = this.mLetterboxBackgroundColorResourceIdOverride.intValue();
        } else {
            colorId = android.R.color.car_yellow_800;
        }
        return android.graphics.Color.valueOf(this.mContext.getResources().getColor(colorId));
    }

    void setLetterboxBackgroundColor(android.graphics.Color color) {
        this.mLetterboxBackgroundColorOverride = color;
    }

    void setLetterboxBackgroundColorResourceId(int colorId) {
        this.mLetterboxBackgroundColorResourceIdOverride = java.lang.Integer.valueOf(colorId);
    }

    void resetLetterboxBackgroundColor() {
        this.mLetterboxBackgroundColorOverride = null;
        this.mLetterboxBackgroundColorResourceIdOverride = null;
    }

    int getLetterboxBackgroundType() {
        if (this.mLetterboxBackgroundTypeOverride != -1) {
            return this.mLetterboxBackgroundTypeOverride;
        }
        return getDefaultLetterboxBackgroundType();
    }

    void setLetterboxBackgroundTypeOverride(int backgroundType) {
        this.mLetterboxBackgroundTypeOverride = backgroundType;
    }

    void resetLetterboxBackgroundType() {
        this.mLetterboxBackgroundTypeOverride = -1;
    }

    private int getDefaultLetterboxBackgroundType() {
        if (this.mDeviceConfig.getFlagValue(KEY_ENABLE_LETTERBOX_BACKGROUND_WALLPAPER)) {
            return 3;
        }
        return this.mLetterboxBackgroundType;
    }

    static java.lang.String letterboxBackgroundTypeToString(int backgroundType) {
        switch (backgroundType) {
            case 0:
                return "LETTERBOX_BACKGROUND_SOLID_COLOR";
            case 1:
                return "LETTERBOX_BACKGROUND_APP_COLOR_BACKGROUND";
            case 2:
                return "LETTERBOX_BACKGROUND_APP_COLOR_BACKGROUND_FLOATING";
            case 3:
                return "LETTERBOX_BACKGROUND_WALLPAPER";
            default:
                return "unknown=" + backgroundType;
        }
    }

    private static int readLetterboxBackgroundTypeFromConfig(android.content.Context context) {
        int backgroundType = context.getResources().getInteger(android.R.integer.config_globalActionsKeyTimeout);
        if (backgroundType == 0 || backgroundType == 1 || backgroundType == 2 || backgroundType == 3) {
            return backgroundType;
        }
        return 0;
    }

    void setLetterboxBackgroundWallpaperDarkScrimAlpha(float alpha) {
        this.mLetterboxBackgroundWallpaperDarkScrimAlpha = alpha;
    }

    void resetLetterboxBackgroundWallpaperDarkScrimAlpha() {
        this.mLetterboxBackgroundWallpaperDarkScrimAlpha = this.mContext.getResources().getFloat(android.R.dimen.config_autoKeyboardBrightnessSmoothingConstant);
    }

    float getLetterboxBackgroundWallpaperDarkScrimAlpha() {
        return this.mLetterboxBackgroundWallpaperDarkScrimAlpha;
    }

    void setLetterboxBackgroundWallpaperBlurRadiusPx(int radius) {
        this.mLetterboxBackgroundWallpaperBlurRadiusPx = radius;
    }

    void resetLetterboxBackgroundWallpaperBlurRadiusPx() {
        this.mLetterboxBackgroundWallpaperBlurRadiusPx = this.mContext.getResources().getDimensionPixelSize(android.R.dimen.config_backGestureInset);
    }

    int getLetterboxBackgroundWallpaperBlurRadiusPx() {
        return this.mLetterboxBackgroundWallpaperBlurRadiusPx;
    }

    float getLetterboxHorizontalPositionMultiplier(boolean isInBookMode) {
        return isInBookMode ? this.mLetterboxBookModePositionMultiplier : this.mLetterboxHorizontalPositionMultiplier;
    }

    float getLetterboxVerticalPositionMultiplier(boolean isInTabletopMode) {
        return isInTabletopMode ? this.mLetterboxTabletopModePositionMultiplier : this.mLetterboxVerticalPositionMultiplier;
    }

    void setLetterboxHorizontalPositionMultiplier(float multiplier) {
        this.mLetterboxHorizontalPositionMultiplier = assertValidMultiplier(multiplier, "mLetterboxHorizontalPositionMultiplier");
    }

    void setLetterboxVerticalPositionMultiplier(float multiplier) {
        this.mLetterboxVerticalPositionMultiplier = assertValidMultiplier(multiplier, "mLetterboxVerticalPositionMultiplier");
    }

    void resetLetterboxHorizontalPositionMultiplier() {
        this.mLetterboxHorizontalPositionMultiplier = this.mContext.getResources().getFloat(android.R.dimen.config_buttonCornerRadius);
    }

    void resetLetterboxVerticalPositionMultiplier() {
        this.mLetterboxVerticalPositionMultiplier = this.mContext.getResources().getFloat(android.R.dimen.config_dialogCornerRadius);
    }

    void setLetterboxTabletopModePositionMultiplier(float multiplier) {
        this.mLetterboxTabletopModePositionMultiplier = assertValidMultiplier(multiplier, "mLetterboxTabletopModePositionMultiplier");
    }

    void setLetterboxBookModePositionMultiplier(float multiplier) {
        this.mLetterboxBookModePositionMultiplier = assertValidMultiplier(multiplier, "mLetterboxBookModePositionMultiplier");
    }

    boolean getIsHorizontalReachabilityEnabled() {
        return this.mIsHorizontalReachabilityEnabled;
    }

    boolean getIsVerticalReachabilityEnabled() {
        return this.mIsVerticalReachabilityEnabled;
    }

    boolean getIsAutomaticReachabilityInBookModeEnabled() {
        return this.mIsAutomaticReachabilityInBookModeEnabled;
    }

    void setIsHorizontalReachabilityEnabled(boolean enabled) {
        this.mIsHorizontalReachabilityEnabled = enabled;
    }

    void setIsVerticalReachabilityEnabled(boolean enabled) {
        this.mIsVerticalReachabilityEnabled = enabled;
    }

    void setIsAutomaticReachabilityInBookModeEnabled(boolean enabled) {
        this.mIsAutomaticReachabilityInBookModeEnabled = enabled;
    }

    void resetIsHorizontalReachabilityEnabled() {
        this.mIsHorizontalReachabilityEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_isDesktopModeSupported);
    }

    void resetIsVerticalReachabilityEnabled() {
        this.mIsVerticalReachabilityEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_isPreApprovalRequestAvailable);
    }

    void resetEnabledAutomaticReachabilityInBookMode() {
        this.mIsAutomaticReachabilityInBookModeEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_ignoreVibrationsOnWirelessCharger);
    }

    int getDefaultPositionForHorizontalReachability() {
        return this.mDefaultPositionForHorizontalReachability;
    }

    int getDefaultPositionForVerticalReachability() {
        return this.mDefaultPositionForVerticalReachability;
    }

    void setDefaultPositionForHorizontalReachability(int position) {
        this.mDefaultPositionForHorizontalReachability = position;
    }

    void setDefaultPositionForVerticalReachability(int position) {
        this.mDefaultPositionForVerticalReachability = position;
    }

    void resetDefaultPositionForHorizontalReachability() {
        this.mDefaultPositionForHorizontalReachability = readLetterboxHorizontalReachabilityPositionFromConfig(this.mContext, false);
    }

    void resetDefaultPositionForVerticalReachability() {
        this.mDefaultPositionForVerticalReachability = readLetterboxVerticalReachabilityPositionFromConfig(this.mContext, false);
    }

    void setPersistentLetterboxPositionForHorizontalReachability(boolean forBookMode, int position) {
        this.mLetterboxConfigurationPersister.setLetterboxPositionForHorizontalReachability(forBookMode, position);
    }

    void setPersistentLetterboxPositionForVerticalReachability(boolean forTabletopMode, int position) {
        this.mLetterboxConfigurationPersister.setLetterboxPositionForVerticalReachability(forTabletopMode, position);
    }

    void resetPersistentLetterboxPositionForHorizontalReachability() {
        this.mLetterboxConfigurationPersister.setLetterboxPositionForHorizontalReachability(false, readLetterboxHorizontalReachabilityPositionFromConfig(this.mContext, false));
        this.mLetterboxConfigurationPersister.setLetterboxPositionForHorizontalReachability(true, readLetterboxHorizontalReachabilityPositionFromConfig(this.mContext, true));
    }

    void resetPersistentLetterboxPositionForVerticalReachability() {
        this.mLetterboxConfigurationPersister.setLetterboxPositionForVerticalReachability(false, readLetterboxVerticalReachabilityPositionFromConfig(this.mContext, false));
        this.mLetterboxConfigurationPersister.setLetterboxPositionForVerticalReachability(true, readLetterboxVerticalReachabilityPositionFromConfig(this.mContext, true));
    }

    private static int readLetterboxHorizontalReachabilityPositionFromConfig(android.content.Context context, boolean forBookMode) {
        int i;
        android.content.res.Resources resources = context.getResources();
        if (forBookMode) {
            i = android.R.integer.config_hotwordDetectedResultMaxBundleSize;
        } else {
            i = android.R.integer.config_hoverTapTimeoutMillis;
        }
        int position = resources.getInteger(i);
        if (position == 0 || position == 1 || position == 2) {
            return position;
        }
        return 1;
    }

    private static int readLetterboxVerticalReachabilityPositionFromConfig(android.content.Context context, boolean forTabletopMode) {
        int i;
        android.content.res.Resources resources = context.getResources();
        if (forTabletopMode) {
            i = android.R.integer.config_hsumBootStrategy;
        } else {
            i = android.R.integer.config_immersive_mode_confirmation_panic;
        }
        int position = resources.getInteger(i);
        if (position == 0 || position == 1 || position == 2) {
            return position;
        }
        return 1;
    }

    float getHorizontalMultiplierForReachability(boolean isDeviceInBookMode) {
        int letterboxPositionForHorizontalReachability = this.mLetterboxConfigurationPersister.getLetterboxPositionForHorizontalReachability(isDeviceInBookMode);
        switch (letterboxPositionForHorizontalReachability) {
            case 0:
                return 0.0f;
            case 1:
                return 0.5f;
            case 2:
                return 1.0f;
            default:
                throw new java.lang.AssertionError("Unexpected letterbox position type: " + letterboxPositionForHorizontalReachability);
        }
    }

    float getVerticalMultiplierForReachability(boolean isDeviceInTabletopMode) {
        int letterboxPositionForVerticalReachability = this.mLetterboxConfigurationPersister.getLetterboxPositionForVerticalReachability(isDeviceInTabletopMode);
        switch (letterboxPositionForVerticalReachability) {
            case 0:
                return 0.0f;
            case 1:
                return 0.5f;
            case 2:
                return 1.0f;
            default:
                throw new java.lang.AssertionError("Unexpected letterbox position type: " + letterboxPositionForVerticalReachability);
        }
    }

    int getLetterboxPositionForHorizontalReachability(boolean isInFullScreenBookMode) {
        return this.mLetterboxConfigurationPersister.getLetterboxPositionForHorizontalReachability(isInFullScreenBookMode);
    }

    int getLetterboxPositionForVerticalReachability(boolean isInFullScreenTabletopMode) {
        return this.mLetterboxConfigurationPersister.getLetterboxPositionForVerticalReachability(isInFullScreenTabletopMode);
    }

    static java.lang.String letterboxHorizontalReachabilityPositionToString(int position) {
        switch (position) {
            case 0:
                return "LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_LEFT";
            case 1:
                return "LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_CENTER";
            case 2:
                return "LETTERBOX_HORIZONTAL_REACHABILITY_POSITION_RIGHT";
            default:
                throw new java.lang.AssertionError("Unexpected letterbox position type: " + position);
        }
    }

    static java.lang.String letterboxVerticalReachabilityPositionToString(int position) {
        switch (position) {
            case 0:
                return "LETTERBOX_VERTICAL_REACHABILITY_POSITION_TOP";
            case 1:
                return "LETTERBOX_VERTICAL_REACHABILITY_POSITION_CENTER";
            case 2:
                return "LETTERBOX_VERTICAL_REACHABILITY_POSITION_BOTTOM";
            default:
                throw new java.lang.AssertionError("Unexpected letterbox position type: " + position);
        }
    }

    void movePositionForHorizontalReachabilityToNextRightStop(final boolean isDeviceInBookMode) {
        updatePositionForHorizontalReachability(isDeviceInBookMode, new java.util.function.Function() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(java.lang.Math.min(((java.lang.Integer) obj).intValue() + (isDeviceInBookMode ? 2 : 1), 2));
            }
        });
    }

    void movePositionForHorizontalReachabilityToNextLeftStop(final boolean isDeviceInBookMode) {
        updatePositionForHorizontalReachability(isDeviceInBookMode, new java.util.function.Function() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(java.lang.Math.max(((java.lang.Integer) obj).intValue() - (isDeviceInBookMode ? 2 : 1), 0));
            }
        });
    }

    void movePositionForVerticalReachabilityToNextBottomStop(final boolean isDeviceInTabletopMode) {
        updatePositionForVerticalReachability(isDeviceInTabletopMode, new java.util.function.Function() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(java.lang.Math.min(((java.lang.Integer) obj).intValue() + (isDeviceInTabletopMode ? 2 : 1), 2));
            }
        });
    }

    void movePositionForVerticalReachabilityToNextTopStop(final boolean isDeviceInTabletopMode) {
        updatePositionForVerticalReachability(isDeviceInTabletopMode, new java.util.function.Function() { // from class: com.android.server.wm.LetterboxConfiguration$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(java.lang.Math.max(((java.lang.Integer) obj).intValue() - (isDeviceInTabletopMode ? 2 : 1), 0));
            }
        });
    }

    boolean getIsEducationEnabled() {
        return this.mIsEducationEnabled;
    }

    void setIsEducationEnabled(boolean enabled) {
        this.mIsEducationEnabled = enabled;
    }

    void resetIsEducationEnabled() {
        this.mIsEducationEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_isCompatFakeFocusEnabled);
    }

    boolean getIsSplitScreenAspectRatioForUnresizableAppsEnabled() {
        return this.mIsSplitScreenAspectRatioForUnresizableAppsEnabled;
    }

    boolean getIsDisplayAspectRatioEnabledForFixedOrientationLetterbox() {
        return this.mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox;
    }

    int getThinLetterboxWidthPx() {
        return this.mThinLetterboxWidthPxSupplier.getAsInt();
    }

    int getThinLetterboxHeightPx() {
        return this.mThinLetterboxHeightPxSupplier.getAsInt();
    }

    void setIsSplitScreenAspectRatioForUnresizableAppsEnabled(boolean enabled) {
        this.mIsSplitScreenAspectRatioForUnresizableAppsEnabled = enabled;
    }

    void setIsDisplayAspectRatioEnabledForFixedOrientationLetterbox(boolean enabled) {
        this.mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox = enabled;
    }

    void resetIsSplitScreenAspectRatioForUnresizableAppsEnabled() {
        this.mIsSplitScreenAspectRatioForUnresizableAppsEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_isMainUserPermanentAdmin);
    }

    void resetIsDisplayAspectRatioEnabledForFixedOrientationLetterbox() {
        this.mIsDisplayAspectRatioEnabledForFixedOrientationLetterbox = this.mContext.getResources().getBoolean(android.R.bool.config_imeDrawsImeNavBar);
    }

    boolean isTranslucentLetterboxingEnabled() {
        return this.mTranslucentLetterboxingOverrideEnabled || this.mDeviceConfig.getFlagValue(KEY_ENABLE_LETTERBOX_TRANSLUCENT_ACTIVITY);
    }

    void setTranslucentLetterboxingOverrideEnabled(boolean translucentLetterboxingOverrideEnabled) {
        this.mTranslucentLetterboxingOverrideEnabled = translucentLetterboxingOverrideEnabled;
    }

    void resetTranslucentLetterboxingEnabled() {
        setTranslucentLetterboxingOverrideEnabled(false);
    }

    private void updatePositionForHorizontalReachability(boolean isDeviceInBookMode, java.util.function.Function<java.lang.Integer, java.lang.Integer> newHorizonalPositionFun) {
        int letterboxPositionForHorizontalReachability = this.mLetterboxConfigurationPersister.getLetterboxPositionForHorizontalReachability(isDeviceInBookMode);
        int nextHorizontalPosition = newHorizonalPositionFun.apply(java.lang.Integer.valueOf(letterboxPositionForHorizontalReachability)).intValue();
        this.mLetterboxConfigurationPersister.setLetterboxPositionForHorizontalReachability(isDeviceInBookMode, nextHorizontalPosition);
    }

    private void updatePositionForVerticalReachability(boolean isDeviceInTabletopMode, java.util.function.Function<java.lang.Integer, java.lang.Integer> newVerticalPositionFun) {
        int letterboxPositionForVerticalReachability = this.mLetterboxConfigurationPersister.getLetterboxPositionForVerticalReachability(isDeviceInTabletopMode);
        int nextVerticalPosition = newVerticalPositionFun.apply(java.lang.Integer.valueOf(letterboxPositionForVerticalReachability)).intValue();
        this.mLetterboxConfigurationPersister.setLetterboxPositionForVerticalReachability(isDeviceInTabletopMode, nextVerticalPosition);
    }

    boolean isCompatFakeFocusEnabled() {
        return this.mDeviceConfig.getFlagValue(KEY_ENABLE_COMPAT_FAKE_FOCUS);
    }

    boolean isPolicyForIgnoringRequestedOrientationEnabled() {
        return this.mIsPolicyForIgnoringRequestedOrientationEnabled;
    }

    boolean isCameraCompatSplitScreenAspectRatioEnabled() {
        return this.mIsCameraCompatSplitScreenAspectRatioEnabled;
    }

    boolean isCameraCompatTreatmentEnabled() {
        return this.mDeviceConfig.getFlagValue(KEY_ENABLE_CAMERA_COMPAT_TREATMENT);
    }

    boolean isCameraCompatTreatmentEnabledAtBuildTime() {
        return this.mDeviceConfig.isBuildTimeFlagEnabled(KEY_ENABLE_CAMERA_COMPAT_TREATMENT);
    }

    boolean isCameraCompatRefreshEnabled() {
        return this.mIsCameraCompatTreatmentRefreshEnabled;
    }

    void setCameraCompatRefreshEnabled(boolean enabled) {
        this.mIsCameraCompatTreatmentRefreshEnabled = enabled;
    }

    void resetCameraCompatRefreshEnabled() {
        this.mIsCameraCompatTreatmentRefreshEnabled = true;
    }

    boolean isCameraCompatRefreshCycleThroughStopEnabled() {
        return this.mIsCameraCompatRefreshCycleThroughStopEnabled;
    }

    void setCameraCompatRefreshCycleThroughStopEnabled(boolean enabled) {
        this.mIsCameraCompatRefreshCycleThroughStopEnabled = enabled;
    }

    void resetCameraCompatRefreshCycleThroughStopEnabled() {
        this.mIsCameraCompatRefreshCycleThroughStopEnabled = true;
    }

    boolean isDisplayRotationImmersiveAppCompatPolicyEnabledAtBuildTime() {
        return this.mDeviceConfig.isBuildTimeFlagEnabled(KEY_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY);
    }

    boolean isDisplayRotationImmersiveAppCompatPolicyEnabled() {
        return this.mDeviceConfig.getFlagValue(KEY_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY);
    }

    boolean isUserAppAspectRatioSettingsEnabled() {
        return this.mUserAppAspectRatioSettingsOverrideEnabled || this.mDeviceConfig.getFlagValue(KEY_ENABLE_USER_ASPECT_RATIO_SETTINGS);
    }

    void setUserAppAspectRatioSettingsOverrideEnabled(boolean enabled) {
        this.mUserAppAspectRatioSettingsOverrideEnabled = enabled;
    }

    void resetUserAppAspectRatioSettingsEnabled() {
        setUserAppAspectRatioSettingsOverrideEnabled(false);
    }

    boolean isUserAppAspectRatioFullscreenEnabled() {
        return isUserAppAspectRatioSettingsEnabled() && (this.mUserAppAspectRatioFullscreenOverrideEnabled || this.mDeviceConfig.getFlagValue(KEY_ENABLE_USER_ASPECT_RATIO_FULLSCREEN));
    }

    void setUserAppAspectRatioFullscreenOverrideEnabled(boolean enabled) {
        this.mUserAppAspectRatioFullscreenOverrideEnabled = enabled;
    }

    void resetUserAppAspectRatioFullscreenEnabled() {
        setUserAppAspectRatioFullscreenOverrideEnabled(false);
    }

    private float assertValidMultiplier(float multiplier, java.lang.String multiplierName) throws java.lang.IllegalArgumentException {
        if (multiplier < 0.0f || multiplier > 1.0f) {
            throw new java.lang.IllegalArgumentException("Trying to set " + multiplierName + " out of bounds: " + multiplier);
        }
        return multiplier;
    }
}
