package com.android.server.display.feature;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayManagerFlags {
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mAdaptiveToneImprovements1;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mAdaptiveToneImprovements2;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mAlwaysRotateDisplayDevice;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mAutoBrightnessModesFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mBackUpSmoothDisplayAndForcePeakRefreshRateFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mBrightnessIntRangeUserPerceptionFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mBrightnessWearBedtimeModeClamperFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mConnectedDisplayErrorHandlingFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mConnectedDisplayManagementFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mDisplayOffloadFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mEvenDimmerFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mExternalDisplayLimitModeState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mFastHdrTransitions;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mHdrClamperFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mIdleScreenRefreshRateTimeout;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mIgnoreAppPreferredRefreshRate;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mNbmControllerFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mOffloadControlsDozeAutoBrightness;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mOffloadDozeOverrideHoldsWakelock;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mPeakRefreshRatePhysicalLimit;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mPixelAnisotropyCorrectionEnabled;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mPortInDisplayLayoutFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mPowerThrottlingClamperFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mRefactorDisplayPowerController;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mRefreshRateVotingTelemetry;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mResolutionBackupRestore;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mRestrictDisplayModes;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mSensorBasedBrightnessThrottling;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mSmallAreaDetectionFlagState;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mSynthetic60hzModes;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mUseFusionProxSensor;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mVsyncLowLightVote;
    private final com.android.server.display.feature.DisplayManagerFlags.FlagState mVsyncLowPowerVote;
    private static final java.lang.String TAG = "DisplayManagerFlags";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    public DisplayManagerFlags() {
        this.mPortInDisplayLayoutFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_port_in_display_layout", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enablePortInDisplayLayout());
            }
        });
        this.mConnectedDisplayManagementFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_connected_display_management", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda11
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableConnectedDisplayManagement());
            }
        });
        this.mNbmControllerFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_nbm_controller", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda22
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableNbmController());
            }
        });
        this.mHdrClamperFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_hdr_clamper", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda26
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableHdrClamper());
            }
        });
        this.mAdaptiveToneImprovements1 = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_adaptive_tone_improvements_1", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda27
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableAdaptiveToneImprovements1());
            }
        });
        this.mAdaptiveToneImprovements2 = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_adaptive_tone_improvements_2", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda28
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableAdaptiveToneImprovements2());
            }
        });
        this.mDisplayOffloadFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_display_offload", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda29
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableDisplayOffload());
            }
        });
        this.mExternalDisplayLimitModeState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_mode_limit_for_external_display", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda30
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableModeLimitForExternalDisplay());
            }
        });
        this.mConnectedDisplayErrorHandlingFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_connected_display_error_handling", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda31
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableConnectedDisplayErrorHandling());
            }
        });
        this.mBackUpSmoothDisplayAndForcePeakRefreshRateFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.back_up_smooth_display_and_force_peak_refresh_rate", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda32
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.backUpSmoothDisplayAndForcePeakRefreshRate());
            }
        });
        this.mPowerThrottlingClamperFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_power_throttling_clamper", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enablePowerThrottlingClamper());
            }
        });
        this.mEvenDimmerFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.even_dimmer", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.evenDimmer());
            }
        });
        this.mSmallAreaDetectionFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.graphics.surfaceflinger.flags.enable_small_area_detection", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.graphics.surfaceflinger.flags.Flags.enableSmallAreaDetection());
            }
        });
        this.mBrightnessIntRangeUserPerceptionFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.brightness_int_range_user_perception", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.brightnessIntRangeUserPerception());
            }
        });
        this.mRestrictDisplayModes = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_restrict_display_modes", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableRestrictDisplayModes());
            }
        });
        this.mResolutionBackupRestore = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.resolution_backup_restore", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda6
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.resolutionBackupRestore());
            }
        });
        this.mVsyncLowPowerVote = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_vsync_low_power_vote", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda7
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableVsyncLowPowerVote());
            }
        });
        this.mVsyncLowLightVote = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_vsync_low_light_vote", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda8
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableVsyncLowLightVote());
            }
        });
        this.mBrightnessWearBedtimeModeClamperFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.brightness_wear_bedtime_mode_clamper", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda9
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.brightnessWearBedtimeModeClamper());
            }
        });
        this.mAutoBrightnessModesFlagState = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.auto_brightness_modes", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda10
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.autoBrightnessModes());
            }
        });
        this.mFastHdrTransitions = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.fast_hdr_transitions", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda12
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.fastHdrTransitions());
            }
        });
        this.mAlwaysRotateDisplayDevice = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.always_rotate_display_device", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda13
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.alwaysRotateDisplayDevice());
            }
        });
        this.mRefreshRateVotingTelemetry = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.refresh_rate_voting_telemetry", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda14
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.refreshRateVotingTelemetry());
            }
        });
        this.mPixelAnisotropyCorrectionEnabled = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_pixel_anisotropy_correction", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda15
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enablePixelAnisotropyCorrection());
            }
        });
        this.mSensorBasedBrightnessThrottling = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.sensor_based_brightness_throttling", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda16
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.sensorBasedBrightnessThrottling());
            }
        });
        this.mIdleScreenRefreshRateTimeout = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.idle_screen_refresh_rate_timeout", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda17
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.idleScreenRefreshRateTimeout());
            }
        });
        this.mRefactorDisplayPowerController = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.refactor_display_power_controller", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda18
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.refactorDisplayPowerController());
            }
        });
        this.mUseFusionProxSensor = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.use_fusion_prox_sensor", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda19
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.useFusionProxSensor());
            }
        });
        this.mOffloadControlsDozeAutoBrightness = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.offload_controls_doze_auto_brightness", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda20
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.offloadControlsDozeAutoBrightness());
            }
        });
        this.mPeakRefreshRatePhysicalLimit = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_peak_refresh_rate_physical_limit", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda21
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enablePeakRefreshRatePhysicalLimit());
            }
        });
        this.mIgnoreAppPreferredRefreshRate = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.ignore_app_preferred_refresh_rate_request", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda23
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.ignoreAppPreferredRefreshRateRequest());
            }
        });
        this.mSynthetic60hzModes = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.enable_synthetic_60hz_modes", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda24
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.enableSynthetic60hzModes());
            }
        });
        this.mOffloadDozeOverrideHoldsWakelock = new com.android.server.display.feature.DisplayManagerFlags.FlagState("com.android.server.display.feature.flags.offload_doze_override_holds_wakelock", new java.util.function.Supplier() { // from class: com.android.server.display.feature.DisplayManagerFlags$$ExternalSyntheticLambda25
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Boolean.valueOf(com.android.server.display.feature.flags.Flags.offloadDozeOverrideHoldsWakelock());
            }
        });
    }

    public boolean isPortInDisplayLayoutEnabled() {
        return this.mPortInDisplayLayoutFlagState.isEnabled();
    }

    public boolean isConnectedDisplayManagementEnabled() {
        return this.mConnectedDisplayManagementFlagState.isEnabled();
    }

    public boolean isNbmControllerEnabled() {
        return this.mNbmControllerFlagState.isEnabled();
    }

    public boolean isHdrClamperEnabled() {
        return this.mHdrClamperFlagState.isEnabled();
    }

    public boolean isPowerThrottlingClamperEnabled() {
        return this.mPowerThrottlingClamperFlagState.isEnabled();
    }

    public boolean isAdaptiveTone1Enabled() {
        return this.mAdaptiveToneImprovements1.isEnabled();
    }

    public boolean isAdaptiveTone2Enabled() {
        return this.mAdaptiveToneImprovements2.isEnabled();
    }

    public boolean isDisplayResolutionRangeVotingEnabled() {
        return isExternalDisplayLimitModeEnabled();
    }

    public boolean isUserPreferredModeVoteEnabled() {
        return isExternalDisplayLimitModeEnabled();
    }

    public boolean isExternalDisplayLimitModeEnabled() {
        return this.mExternalDisplayLimitModeState.isEnabled();
    }

    public boolean isDisplaysRefreshRatesSynchronizationEnabled() {
        return isExternalDisplayLimitModeEnabled();
    }

    public boolean isDisplayOffloadEnabled() {
        return this.mDisplayOffloadFlagState.isEnabled();
    }

    public boolean isConnectedDisplayErrorHandlingEnabled() {
        return this.mConnectedDisplayErrorHandlingFlagState.isEnabled();
    }

    public boolean isBackUpSmoothDisplayAndForcePeakRefreshRateEnabled() {
        return this.mBackUpSmoothDisplayAndForcePeakRefreshRateFlagState.isEnabled();
    }

    public boolean isEvenDimmerEnabled() {
        return this.mEvenDimmerFlagState.isEnabled();
    }

    public boolean isSmallAreaDetectionEnabled() {
        return this.mSmallAreaDetectionFlagState.isEnabled();
    }

    public boolean isBrightnessIntRangeUserPerceptionEnabled() {
        return this.mBrightnessIntRangeUserPerceptionFlagState.isEnabled();
    }

    public boolean isRestrictDisplayModesEnabled() {
        return this.mRestrictDisplayModes.isEnabled();
    }

    public boolean isResolutionBackupRestoreEnabled() {
        return this.mResolutionBackupRestore.isEnabled();
    }

    public boolean isVsyncLowPowerVoteEnabled() {
        return this.mVsyncLowPowerVote.isEnabled();
    }

    public boolean isVsyncLowLightVoteEnabled() {
        return this.mVsyncLowLightVote.isEnabled();
    }

    public boolean isBrightnessWearBedtimeModeClamperEnabled() {
        return this.mBrightnessWearBedtimeModeClamperFlagState.isEnabled();
    }

    public boolean areAutoBrightnessModesEnabled() {
        return this.mAutoBrightnessModesFlagState.isEnabled();
    }

    public boolean isFastHdrTransitionsEnabled() {
        return this.mFastHdrTransitions.isEnabled();
    }

    public boolean isAlwaysRotateDisplayDeviceEnabled() {
        return this.mAlwaysRotateDisplayDevice.isEnabled();
    }

    public boolean isRefreshRateVotingTelemetryEnabled() {
        return this.mRefreshRateVotingTelemetry.isEnabled();
    }

    public boolean isPixelAnisotropyCorrectionInLogicalDisplayEnabled() {
        return this.mPixelAnisotropyCorrectionEnabled.isEnabled();
    }

    public boolean isSensorBasedBrightnessThrottlingEnabled() {
        return this.mSensorBasedBrightnessThrottling.isEnabled();
    }

    public boolean isIdleScreenRefreshRateTimeoutEnabled() {
        return this.mIdleScreenRefreshRateTimeout.isEnabled();
    }

    public boolean isRefactorDisplayPowerControllerEnabled() {
        return this.mRefactorDisplayPowerController.isEnabled();
    }

    public boolean isUseFusionProxSensorEnabled() {
        return this.mUseFusionProxSensor.isEnabled();
    }

    public java.lang.String getUseFusionProxSensorFlagName() {
        return this.mUseFusionProxSensor.getName();
    }

    public boolean offloadControlsDozeAutoBrightness() {
        return this.mOffloadControlsDozeAutoBrightness.isEnabled();
    }

    public boolean isPeakRefreshRatePhysicalLimitEnabled() {
        return this.mPeakRefreshRatePhysicalLimit.isEnabled();
    }

    public boolean isOffloadDozeOverrideHoldsWakelockEnabled() {
        return this.mOffloadDozeOverrideHoldsWakelock.isEnabled();
    }

    public boolean ignoreAppPreferredRefreshRateRequest() {
        return this.mIgnoreAppPreferredRefreshRate.isEnabled();
    }

    public boolean isSynthetic60HzModesEnabled() {
        return this.mSynthetic60hzModes.isEnabled();
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("DisplayManagerFlags:");
        pw.println(" " + this.mAdaptiveToneImprovements1);
        pw.println(" " + this.mAdaptiveToneImprovements2);
        pw.println(" " + this.mBackUpSmoothDisplayAndForcePeakRefreshRateFlagState);
        pw.println(" " + this.mConnectedDisplayErrorHandlingFlagState);
        pw.println(" " + this.mConnectedDisplayManagementFlagState);
        pw.println(" " + this.mDisplayOffloadFlagState);
        pw.println(" " + this.mExternalDisplayLimitModeState);
        pw.println(" " + this.mHdrClamperFlagState);
        pw.println(" " + this.mNbmControllerFlagState);
        pw.println(" " + this.mPowerThrottlingClamperFlagState);
        pw.println(" " + this.mEvenDimmerFlagState);
        pw.println(" " + this.mSmallAreaDetectionFlagState);
        pw.println(" " + this.mBrightnessIntRangeUserPerceptionFlagState);
        pw.println(" " + this.mRestrictDisplayModes);
        pw.println(" " + this.mBrightnessWearBedtimeModeClamperFlagState);
        pw.println(" " + this.mAutoBrightnessModesFlagState);
        pw.println(" " + this.mFastHdrTransitions);
        pw.println(" " + this.mAlwaysRotateDisplayDevice);
        pw.println(" " + this.mRefreshRateVotingTelemetry);
        pw.println(" " + this.mPixelAnisotropyCorrectionEnabled);
        pw.println(" " + this.mSensorBasedBrightnessThrottling);
        pw.println(" " + this.mIdleScreenRefreshRateTimeout);
        pw.println(" " + this.mRefactorDisplayPowerController);
        pw.println(" " + this.mResolutionBackupRestore);
        pw.println(" " + this.mUseFusionProxSensor);
        pw.println(" " + this.mOffloadControlsDozeAutoBrightness);
        pw.println(" " + this.mPeakRefreshRatePhysicalLimit);
        pw.println(" " + this.mIgnoreAppPreferredRefreshRate);
        pw.println(" " + this.mSynthetic60hzModes);
        pw.println(" " + this.mOffloadDozeOverrideHoldsWakelock);
    }

    private static class FlagState {
        private boolean mEnabled;
        private boolean mEnabledSet;
        private final java.util.function.Supplier<java.lang.Boolean> mFlagFunction;
        private final java.lang.String mName;

        private FlagState(java.lang.String name, java.util.function.Supplier<java.lang.Boolean> flagFunction) {
            this.mName = name;
            this.mFlagFunction = flagFunction;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String getName() {
            return this.mName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEnabled() {
            if (this.mEnabledSet) {
                if (com.android.server.display.feature.DisplayManagerFlags.DEBUG) {
                    android.util.Slog.d(com.android.server.display.feature.DisplayManagerFlags.TAG, this.mName + ": mEnabled. Recall = " + this.mEnabled);
                }
                return this.mEnabled;
            }
            this.mEnabled = flagOrSystemProperty(this.mFlagFunction, this.mName);
            if (com.android.server.display.feature.DisplayManagerFlags.DEBUG) {
                android.util.Slog.d(com.android.server.display.feature.DisplayManagerFlags.TAG, this.mName + ": mEnabled. Flag value = " + this.mEnabled);
            }
            this.mEnabledSet = true;
            return this.mEnabled;
        }

        private boolean flagOrSystemProperty(java.util.function.Supplier<java.lang.Boolean> flagFunction, java.lang.String flagName) {
            boolean flagValue = flagFunction.get().booleanValue();
            if (android.os.Build.IS_ENG || android.os.Build.IS_USERDEBUG) {
                return android.os.SystemProperties.getBoolean("persist.sys." + flagName + "-override", flagValue);
            }
            return flagValue;
        }

        public java.lang.String toString() {
            int nameLength = this.mName.length();
            return android.text.TextUtils.substring(this.mName, 41, nameLength) + ": " + android.text.TextUtils.formatSimple("%" + (93 - nameLength) + "s%s", new java.lang.Object[]{" ", java.lang.Boolean.valueOf(isEnabled())}) + " (def:" + this.mFlagFunction.get() + ")";
        }
    }
}
