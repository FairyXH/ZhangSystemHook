package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DisplayWindowSettings {
    private com.android.server.wm.IDisplayWindowSettingsExt mDisplayWindowSettingsExt = (com.android.server.wm.IDisplayWindowSettingsExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayWindowSettingsExt.class).base(this).create();
    private final com.android.server.wm.WindowManagerService mService;
    private final com.android.server.wm.DisplayWindowSettings.SettingsProvider mSettingsProvider;

    DisplayWindowSettings(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayWindowSettings.SettingsProvider settingsProvider) {
        this.mService = service;
        this.mSettingsProvider = settingsProvider;
    }

    void setUserRotation(com.android.server.wm.DisplayContent displayContent, int rotationMode, int rotation) {
        android.view.DisplayInfo displayInfo = displayContent.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mUserRotationMode = java.lang.Integer.valueOf(rotationMode);
        overrideSettings.mUserRotation = java.lang.Integer.valueOf(rotation);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    void setForcedSize(com.android.server.wm.DisplayContent displayContent, int width, int height) {
        if (displayContent.isDefaultDisplay) {
            java.lang.String sizeString = (width == 0 || height == 0) ? "" : width + "," + height;
            android.provider.Settings.Global.putString(this.mService.mContext.getContentResolver(), "display_size_forced", sizeString);
        }
        android.view.DisplayInfo displayInfo = displayContent.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mForcedWidth = width;
        overrideSettings.mForcedHeight = height;
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    void setForcedDensity(android.view.DisplayInfo info, int density, int userId) {
        if (info.displayId == 0) {
            java.lang.String densityString = density == 0 ? "" : java.lang.Integer.toString(density);
            android.provider.Settings.Secure.putStringForUser(this.mService.mContext.getContentResolver(), "display_density_forced", densityString, userId);
        }
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(info);
        overrideSettings.mForcedDensity = density;
        this.mSettingsProvider.updateOverrideSettings(info, overrideSettings);
    }

    void setForcedDensity(android.view.DisplayInfo displayInfo, int density) {
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mForcedDensity = density;
        android.util.Slog.d("DisplayWindowSettings", " setForcedDensity  density= " + density + ", " + displayInfo.uniqueId);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    void setForcedScalingMode(com.android.server.wm.DisplayContent displayContent, int mode) {
        if (displayContent.isDefaultDisplay) {
            android.provider.Settings.Global.putInt(this.mService.mContext.getContentResolver(), "display_scaling_force", mode);
        }
        android.view.DisplayInfo displayInfo = displayContent.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mForcedScalingMode = java.lang.Integer.valueOf(mode);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    void setFixedToUserRotation(com.android.server.wm.DisplayContent displayContent, int fixedToUserRotation) {
        android.view.DisplayInfo displayInfo = displayContent.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mFixedToUserRotation = java.lang.Integer.valueOf(fixedToUserRotation);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    void setIgnoreOrientationRequest(com.android.server.wm.DisplayContent displayContent, boolean ignoreOrientationRequest) {
        android.view.DisplayInfo displayInfo = displayContent.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mIgnoreOrientationRequest = java.lang.Boolean.valueOf(ignoreOrientationRequest);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    private int getWindowingModeLocked(com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings, com.android.server.wm.DisplayContent dc) {
        int windowingMode = settings.mWindowingMode;
        int windowingMode2 = 1;
        if (windowingMode == 5 && !this.mService.mAtmService.mSupportsFreeformWindowManagement) {
            return 1;
        }
        if (windowingMode == 0) {
            if (this.mService.mAtmService.mSupportsFreeformWindowManagement && (this.mService.mIsPc || dc.forceDesktopMode())) {
                windowingMode2 = 5;
            }
            return windowingMode2;
        }
        return windowingMode;
    }

    int getWindowingModeLocked(com.android.server.wm.DisplayContent dc) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayInfo);
        return getWindowingModeLocked(settings, dc);
    }

    void setWindowingModeLocked(com.android.server.wm.DisplayContent dc, int mode) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mWindowingMode = mode;
        com.android.server.wm.TaskDisplayArea defaultTda = dc.getDefaultTaskDisplayArea();
        if (defaultTda != null) {
            defaultTda.setWindowingMode(mode);
        }
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    int getRemoveContentModeLocked(com.android.server.wm.DisplayContent dc) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayInfo);
        if (settings.mRemoveContentMode == 0) {
            if (dc.isPrivate()) {
                return 2;
            }
            return 1;
        }
        return settings.mRemoveContentMode;
    }

    void setRemoveContentModeLocked(com.android.server.wm.DisplayContent dc, int mode) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mRemoveContentMode = mode;
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    boolean shouldShowWithInsecureKeyguardLocked(com.android.server.wm.DisplayContent dc) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayInfo);
        if (settings.mShouldShowWithInsecureKeyguard != null) {
            return settings.mShouldShowWithInsecureKeyguard.booleanValue();
        }
        return false;
    }

    void setShouldShowWithInsecureKeyguardLocked(com.android.server.wm.DisplayContent dc, boolean shouldShow) {
        if (!dc.isPrivate() && shouldShow) {
            throw new java.lang.IllegalArgumentException("Public display can't be allowed to show content when locked");
        }
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mShouldShowWithInsecureKeyguard = java.lang.Boolean.valueOf(shouldShow);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    void setDontMoveToTop(com.android.server.wm.DisplayContent dc, boolean dontMoveToTop) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getSettings(displayInfo);
        overrideSettings.mDontMoveToTop = java.lang.Boolean.valueOf(dontMoveToTop);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    boolean shouldShowSystemDecorsLocked(com.android.server.wm.DisplayContent dc) {
        if (dc.getDisplayId() == 0) {
            return true;
        }
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayInfo);
        if (settings.mShouldShowSystemDecors != null) {
            return settings.mShouldShowSystemDecors.booleanValue();
        }
        return false;
    }

    void setShouldShowSystemDecorsLocked(com.android.server.wm.DisplayContent dc, boolean shouldShow) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mShouldShowSystemDecors = java.lang.Boolean.valueOf(shouldShow);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    boolean isHomeSupportedLocked(com.android.server.wm.DisplayContent dc) {
        if (dc.getDisplayId() == 0) {
            return true;
        }
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayInfo);
        if (settings.mIsHomeSupported != null) {
            return settings.mIsHomeSupported.booleanValue();
        }
        return shouldShowSystemDecorsLocked(dc);
    }

    void setHomeSupportedOnDisplayLocked(java.lang.String displayUniqueId, int displayType, boolean supported) {
        android.view.DisplayInfo displayInfo = new android.view.DisplayInfo();
        displayInfo.uniqueId = displayUniqueId;
        displayInfo.type = displayType;
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mIsHomeSupported = java.lang.Boolean.valueOf(supported);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    void clearDisplaySettings(java.lang.String displayUniqueId, int displayType) {
        android.view.DisplayInfo displayInfo = new android.view.DisplayInfo();
        displayInfo.uniqueId = displayUniqueId;
        displayInfo.type = displayType;
        this.mSettingsProvider.clearDisplaySettings(displayInfo);
    }

    int getImePolicyLocked(com.android.server.wm.DisplayContent dc) {
        if (dc.getDisplayId() == 0) {
            return 0;
        }
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayInfo);
        if (settings.mImePolicy != null) {
            return settings.mImePolicy.intValue();
        }
        return 1;
    }

    void setDisplayImePolicy(com.android.server.wm.DisplayContent dc, int imePolicy) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry overrideSettings = this.mSettingsProvider.getOverrideSettings(displayInfo);
        overrideSettings.mImePolicy = java.lang.Integer.valueOf(imePolicy);
        this.mSettingsProvider.updateOverrideSettings(displayInfo, overrideSettings);
    }

    void applySettingsToDisplayLocked(com.android.server.wm.DisplayContent dc) {
        applySettingsToDisplayLocked(dc, true);
    }

    void applySettingsToDisplayLocked(com.android.server.wm.DisplayContent dc, boolean includeRotationSettings) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayInfo);
        int windowingMode = getWindowingModeLocked(settings, dc);
        com.android.server.wm.TaskDisplayArea defaultTda = dc.getDefaultTaskDisplayArea();
        if (defaultTda != null && !this.mDisplayWindowSettingsExt.skipSetWindowingMode(dc, includeRotationSettings, windowingMode)) {
            defaultTda.setWindowingMode(windowingMode);
        }
        int userRotationMode = settings.mUserRotationMode != null ? settings.mUserRotationMode.intValue() : 0;
        int userRotation = settings.mUserRotation != null ? settings.mUserRotation.intValue() : 0;
        int mFixedToUserRotation = settings.mFixedToUserRotation != null ? settings.mFixedToUserRotation.intValue() : 0;
        dc.getDisplayRotation().restoreSettings(userRotationMode, userRotation, mFixedToUserRotation);
        boolean hasDensityOverride = settings.mForcedDensity != 0;
        boolean hasSizeOverride = (settings.mForcedWidth == 0 || settings.mForcedHeight == 0) ? false : true;
        dc.mIsDensityForced = hasDensityOverride;
        dc.mIsSizeForced = hasSizeOverride;
        dc.mIgnoreDisplayCutout = settings.mIgnoreDisplayCutout != null ? settings.mIgnoreDisplayCutout.booleanValue() : false;
        int width = hasSizeOverride ? settings.mForcedWidth : dc.mInitialDisplayWidth;
        int height = hasSizeOverride ? settings.mForcedHeight : dc.mInitialDisplayHeight;
        int density = hasDensityOverride ? settings.mForcedDensity : dc.getInitialDisplayDensity();
        dc.updateBaseDisplayMetrics(width, height, density, dc.mBaseDisplayPhysicalXDpi, dc.mBaseDisplayPhysicalYDpi);
        int forcedScalingMode = settings.mForcedScalingMode != null ? settings.mForcedScalingMode.intValue() : 0;
        dc.mDisplayScalingDisabled = forcedScalingMode == 1;
        boolean dontMoveToTop = settings.mDontMoveToTop != null ? settings.mDontMoveToTop.booleanValue() : false;
        dc.mDontMoveToTop = !dc.canStealTopFocus() || dontMoveToTop;
        if (includeRotationSettings) {
            applyRotationSettingsToDisplayLocked(dc);
        }
    }

    void applyRotationSettingsToDisplayLocked(com.android.server.wm.DisplayContent dc) {
        android.view.DisplayInfo displayInfo = dc.getDisplayInfo();
        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settings = this.mSettingsProvider.getSettings(displayInfo);
        boolean ignoreOrientationRequest = settings.mIgnoreOrientationRequest != null ? settings.mIgnoreOrientationRequest.booleanValue() : false;
        dc.setIgnoreOrientationRequest(ignoreOrientationRequest);
        dc.getDisplayRotation().resetAllowAllRotations();
    }

    boolean updateSettingsForDisplay(com.android.server.wm.DisplayContent dc) {
        com.android.server.wm.TaskDisplayArea defaultTda = dc.getDefaultTaskDisplayArea();
        if (defaultTda != null && defaultTda.getWindowingMode() != getWindowingModeLocked(dc)) {
            defaultTda.setWindowingMode(getWindowingModeLocked(dc));
            return true;
        }
        return false;
    }

    void onDisplayRemoved(com.android.server.wm.DisplayContent dc) {
        this.mSettingsProvider.onDisplayRemoved(dc.getDisplayInfo());
    }

    interface SettingsProvider {
        void clearDisplaySettings(android.view.DisplayInfo displayInfo);

        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry getOverrideSettings(android.view.DisplayInfo displayInfo);

        com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry getSettings(android.view.DisplayInfo displayInfo);

        void onDisplayRemoved(android.view.DisplayInfo displayInfo);

        void updateOverrideSettings(android.view.DisplayInfo displayInfo, com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry settingsEntry);

        public static class SettingsEntry {
            java.lang.Boolean mDontMoveToTop;
            java.lang.Integer mFixedToUserRotation;
            int mForcedDensity;
            int mForcedHeight;
            java.lang.Integer mForcedScalingMode;
            int mForcedWidth;
            java.lang.Boolean mIgnoreDisplayCutout;
            java.lang.Boolean mIgnoreOrientationRequest;
            java.lang.Integer mImePolicy;
            java.lang.Boolean mIsHomeSupported;
            java.lang.Boolean mShouldShowSystemDecors;
            java.lang.Boolean mShouldShowWithInsecureKeyguard;
            java.lang.Integer mUserRotation;
            java.lang.Integer mUserRotationMode;
            int mWindowingMode = 0;
            int mRemoveContentMode = 0;

            SettingsEntry() {
            }

            SettingsEntry(com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry copyFrom) {
                setTo(copyFrom);
            }

            boolean setTo(com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry other) {
                boolean changed = false;
                if (other.mWindowingMode != this.mWindowingMode) {
                    this.mWindowingMode = other.mWindowingMode;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mUserRotationMode, this.mUserRotationMode)) {
                    this.mUserRotationMode = other.mUserRotationMode;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mUserRotation, this.mUserRotation)) {
                    this.mUserRotation = other.mUserRotation;
                    changed = true;
                }
                if (other.mForcedWidth != this.mForcedWidth) {
                    this.mForcedWidth = other.mForcedWidth;
                    changed = true;
                }
                if (other.mForcedHeight != this.mForcedHeight) {
                    this.mForcedHeight = other.mForcedHeight;
                    changed = true;
                }
                if (other.mForcedDensity != this.mForcedDensity) {
                    this.mForcedDensity = other.mForcedDensity;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mForcedScalingMode, this.mForcedScalingMode)) {
                    this.mForcedScalingMode = other.mForcedScalingMode;
                    changed = true;
                }
                if (other.mRemoveContentMode != this.mRemoveContentMode) {
                    this.mRemoveContentMode = other.mRemoveContentMode;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mShouldShowWithInsecureKeyguard, this.mShouldShowWithInsecureKeyguard)) {
                    this.mShouldShowWithInsecureKeyguard = other.mShouldShowWithInsecureKeyguard;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mShouldShowSystemDecors, this.mShouldShowSystemDecors)) {
                    this.mShouldShowSystemDecors = other.mShouldShowSystemDecors;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mIsHomeSupported, this.mIsHomeSupported)) {
                    this.mIsHomeSupported = other.mIsHomeSupported;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mImePolicy, this.mImePolicy)) {
                    this.mImePolicy = other.mImePolicy;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mFixedToUserRotation, this.mFixedToUserRotation)) {
                    this.mFixedToUserRotation = other.mFixedToUserRotation;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mIgnoreOrientationRequest, this.mIgnoreOrientationRequest)) {
                    this.mIgnoreOrientationRequest = other.mIgnoreOrientationRequest;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mIgnoreDisplayCutout, this.mIgnoreDisplayCutout)) {
                    this.mIgnoreDisplayCutout = other.mIgnoreDisplayCutout;
                    changed = true;
                }
                if (!java.util.Objects.equals(other.mDontMoveToTop, this.mDontMoveToTop)) {
                    this.mDontMoveToTop = other.mDontMoveToTop;
                    return true;
                }
                return changed;
            }

            boolean updateFrom(com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry delta) {
                boolean changed = false;
                if (delta.mWindowingMode != 0 && delta.mWindowingMode != this.mWindowingMode) {
                    this.mWindowingMode = delta.mWindowingMode;
                    changed = true;
                }
                if (delta.mUserRotationMode != null && !java.util.Objects.equals(delta.mUserRotationMode, this.mUserRotationMode)) {
                    this.mUserRotationMode = delta.mUserRotationMode;
                    changed = true;
                }
                if (delta.mUserRotation != null && !java.util.Objects.equals(delta.mUserRotation, this.mUserRotation)) {
                    this.mUserRotation = delta.mUserRotation;
                    changed = true;
                }
                if (delta.mForcedWidth != 0 && delta.mForcedWidth != this.mForcedWidth) {
                    this.mForcedWidth = delta.mForcedWidth;
                    changed = true;
                }
                if (delta.mForcedHeight != 0 && delta.mForcedHeight != this.mForcedHeight) {
                    this.mForcedHeight = delta.mForcedHeight;
                    changed = true;
                }
                if (delta.mForcedDensity != 0 && delta.mForcedDensity != this.mForcedDensity) {
                    this.mForcedDensity = delta.mForcedDensity;
                    changed = true;
                }
                if (delta.mForcedScalingMode != null && !java.util.Objects.equals(delta.mForcedScalingMode, this.mForcedScalingMode)) {
                    this.mForcedScalingMode = delta.mForcedScalingMode;
                    changed = true;
                }
                if (delta.mRemoveContentMode != 0 && delta.mRemoveContentMode != this.mRemoveContentMode) {
                    this.mRemoveContentMode = delta.mRemoveContentMode;
                    changed = true;
                }
                if (delta.mShouldShowWithInsecureKeyguard != null && !java.util.Objects.equals(delta.mShouldShowWithInsecureKeyguard, this.mShouldShowWithInsecureKeyguard)) {
                    this.mShouldShowWithInsecureKeyguard = delta.mShouldShowWithInsecureKeyguard;
                    changed = true;
                }
                if (delta.mShouldShowSystemDecors != null && !java.util.Objects.equals(delta.mShouldShowSystemDecors, this.mShouldShowSystemDecors)) {
                    this.mShouldShowSystemDecors = delta.mShouldShowSystemDecors;
                    changed = true;
                }
                if (delta.mIsHomeSupported != null && !java.util.Objects.equals(delta.mIsHomeSupported, this.mIsHomeSupported)) {
                    this.mIsHomeSupported = delta.mIsHomeSupported;
                    changed = true;
                }
                if (delta.mImePolicy != null && !java.util.Objects.equals(delta.mImePolicy, this.mImePolicy)) {
                    this.mImePolicy = delta.mImePolicy;
                    changed = true;
                }
                if (delta.mFixedToUserRotation != null && !java.util.Objects.equals(delta.mFixedToUserRotation, this.mFixedToUserRotation)) {
                    this.mFixedToUserRotation = delta.mFixedToUserRotation;
                    changed = true;
                }
                if (delta.mIgnoreOrientationRequest != null && !java.util.Objects.equals(delta.mIgnoreOrientationRequest, this.mIgnoreOrientationRequest)) {
                    this.mIgnoreOrientationRequest = delta.mIgnoreOrientationRequest;
                    changed = true;
                }
                if (delta.mIgnoreDisplayCutout != null && !java.util.Objects.equals(delta.mIgnoreDisplayCutout, this.mIgnoreDisplayCutout)) {
                    this.mIgnoreDisplayCutout = delta.mIgnoreDisplayCutout;
                    changed = true;
                }
                if (delta.mDontMoveToTop != null && !java.util.Objects.equals(delta.mDontMoveToTop, this.mDontMoveToTop)) {
                    this.mDontMoveToTop = delta.mDontMoveToTop;
                    return true;
                }
                return changed;
            }

            boolean isEmpty() {
                return this.mWindowingMode == 0 && this.mUserRotationMode == null && this.mUserRotation == null && this.mForcedWidth == 0 && this.mForcedHeight == 0 && this.mForcedDensity == 0 && this.mForcedScalingMode == null && this.mRemoveContentMode == 0 && this.mShouldShowWithInsecureKeyguard == null && this.mShouldShowSystemDecors == null && this.mIsHomeSupported == null && this.mImePolicy == null && this.mFixedToUserRotation == null && this.mIgnoreOrientationRequest == null && this.mIgnoreDisplayCutout == null && this.mDontMoveToTop == null;
            }

            public boolean equals(java.lang.Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry that = (com.android.server.wm.DisplayWindowSettings.SettingsProvider.SettingsEntry) o;
                if (this.mWindowingMode == that.mWindowingMode && this.mForcedWidth == that.mForcedWidth && this.mForcedHeight == that.mForcedHeight && this.mForcedDensity == that.mForcedDensity && this.mRemoveContentMode == that.mRemoveContentMode && java.util.Objects.equals(this.mUserRotationMode, that.mUserRotationMode) && java.util.Objects.equals(this.mUserRotation, that.mUserRotation) && java.util.Objects.equals(this.mForcedScalingMode, that.mForcedScalingMode) && java.util.Objects.equals(this.mShouldShowWithInsecureKeyguard, that.mShouldShowWithInsecureKeyguard) && java.util.Objects.equals(this.mShouldShowSystemDecors, that.mShouldShowSystemDecors) && java.util.Objects.equals(this.mIsHomeSupported, that.mIsHomeSupported) && java.util.Objects.equals(this.mImePolicy, that.mImePolicy) && java.util.Objects.equals(this.mFixedToUserRotation, that.mFixedToUserRotation) && java.util.Objects.equals(this.mIgnoreOrientationRequest, that.mIgnoreOrientationRequest) && java.util.Objects.equals(this.mIgnoreDisplayCutout, that.mIgnoreDisplayCutout) && java.util.Objects.equals(this.mDontMoveToTop, that.mDontMoveToTop)) {
                    return true;
                }
                return false;
            }

            public int hashCode() {
                return java.util.Objects.hash(java.lang.Integer.valueOf(this.mWindowingMode), this.mUserRotationMode, this.mUserRotation, java.lang.Integer.valueOf(this.mForcedWidth), java.lang.Integer.valueOf(this.mForcedHeight), java.lang.Integer.valueOf(this.mForcedDensity), this.mForcedScalingMode, java.lang.Integer.valueOf(this.mRemoveContentMode), this.mShouldShowWithInsecureKeyguard, this.mShouldShowSystemDecors, this.mIsHomeSupported, this.mImePolicy, this.mFixedToUserRotation, this.mIgnoreOrientationRequest, this.mIgnoreDisplayCutout, this.mDontMoveToTop);
            }

            public java.lang.String toString() {
                return "SettingsEntry{mWindowingMode=" + this.mWindowingMode + ", mUserRotationMode=" + this.mUserRotationMode + ", mUserRotation=" + this.mUserRotation + ", mForcedWidth=" + this.mForcedWidth + ", mForcedHeight=" + this.mForcedHeight + ", mForcedDensity=" + this.mForcedDensity + ", mForcedScalingMode=" + this.mForcedScalingMode + ", mRemoveContentMode=" + this.mRemoveContentMode + ", mShouldShowWithInsecureKeyguard=" + this.mShouldShowWithInsecureKeyguard + ", mShouldShowSystemDecors=" + this.mShouldShowSystemDecors + ", mIsHomeSupported=" + this.mIsHomeSupported + ", mShouldShowIme=" + this.mImePolicy + ", mFixedToUserRotation=" + this.mFixedToUserRotation + ", mIgnoreOrientationRequest=" + this.mIgnoreOrientationRequest + ", mIgnoreDisplayCutout=" + this.mIgnoreDisplayCutout + ", mDontMoveToTop=" + this.mDontMoveToTop + '}';
            }
        }
    }
}
