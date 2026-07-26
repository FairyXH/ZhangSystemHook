package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayModeDirector {
    private static final int MSG_DEFAULT_PEAK_REFRESH_RATE_CHANGED = 3;
    private static final int MSG_HIGH_BRIGHTNESS_THRESHOLDS_CHANGED = 6;
    private static final int MSG_LOW_BRIGHTNESS_THRESHOLDS_CHANGED = 2;
    private static final int MSG_REFRESH_RATE_IN_HBM_HDR_CHANGED = 8;
    private static final int MSG_REFRESH_RATE_IN_HBM_SUNLIGHT_CHANGED = 7;
    private static final int MSG_REFRESH_RATE_IN_HIGH_ZONE_CHANGED = 5;
    private static final int MSG_REFRESH_RATE_IN_LOW_ZONE_CHANGED = 4;
    private static final int MSG_REFRESH_RATE_RANGE_CHANGED = 1;
    public static final float SYNCHRONIZED_REFRESH_RATE_TARGET = 60.0f;
    public static final float SYNCHRONIZED_REFRESH_RATE_TOLERANCE = 1.0f;
    private static final java.lang.String TAG = "DisplayModeDirector";
    public static com.android.server.display.IDisplayModeDirectorExt mDmdExt;
    private boolean mAlwaysRespectAppRequest;
    private final com.android.server.display.mode.DisplayModeDirector.AppRequestObserver mAppRequestObserver;
    private android.util.SparseArray<android.view.Display.Mode[]> mAppSupportedModesByDisplay;
    private com.android.server.display.mode.DisplayModeDirector.BrightnessObserver mBrightnessObserver;
    private final com.android.server.display.feature.DeviceConfigParameterProvider mConfigParameterProvider;
    private final android.content.Context mContext;
    private com.android.server.display.DisplayDeviceConfig mDefaultDisplayDeviceConfig;
    private android.util.SparseArray<android.view.Display.Mode> mDefaultModeByDisplay;
    private com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecsListener mDesiredDisplayModeSpecsListener;
    private final com.android.server.display.mode.DisplayModeDirector.DeviceConfigDisplaySettings mDeviceConfigDisplaySettings;
    private android.util.SparseArray<com.android.server.display.DisplayDeviceConfig> mDisplayDeviceConfigByDisplay;
    private final com.android.server.display.mode.DisplayModeDirector.DisplayDeviceConfigProvider mDisplayDeviceConfigProvider;
    private final com.android.server.display.feature.DisplayManagerFlags mDisplayManagerFlags;
    private final com.android.server.display.mode.DisplayModeDirector.DisplayObserver mDisplayObserver;
    private final com.android.server.display.mode.DisplayModeDirector.DisplayModeDirectorHandler mHandler;
    private final com.android.server.display.mode.DisplayModeDirector.HbmObserver mHbmObserver;
    private final com.android.server.display.mode.DisplayModeDirector.Injector mInjector;
    private final boolean mIsBackUpSmoothDisplayAndForcePeakRefreshRateEnabled;
    private final boolean mIsDisplayResolutionRangeVotingEnabled;
    private final boolean mIsDisplaysRefreshRatesSynchronizationEnabled;
    private final boolean mIsExternalDisplayLimitModeEnabled;
    private boolean mIsResolutionSwitchByMode;
    private final boolean mIsUserPreferredModeVoteEnabled;
    private final java.lang.Object mLock;
    private boolean mLoggingEnabled;
    private int mModeSwitchingType;
    private final com.android.server.display.mode.ProximitySensorObserver mSensorObserver;
    private final com.android.server.display.mode.DisplayModeDirector.SettingsObserver mSettingsObserver;
    private final com.android.server.display.mode.SkinThermalStatusObserver mSkinThermalStatusObserver;
    private android.util.SparseArray<android.view.Display.Mode[]> mSupportedModesByDisplay;
    private final boolean mSupportsFrameRateOverride;
    private final com.android.server.display.mode.SystemRequestObserver mSystemRequestObserver;
    private final com.android.server.display.mode.DisplayModeDirector.UdfpsObserver mUdfpsObserver;
    private final com.android.server.display.mode.VotesStatsReporter mVotesStatsReporter;
    private final com.android.server.display.mode.VotesStorage mVotesStorage;

    public interface DesiredDisplayModeSpecsListener {
        void onDesiredDisplayModeSpecsChanged();
    }

    public interface DisplayDeviceConfigProvider {
        com.android.server.display.DisplayDeviceConfig getDisplayDeviceConfig(int i);
    }

    interface Injector {
        public static final android.net.Uri PEAK_REFRESH_RATE_URI = android.provider.Settings.System.getUriFor("peak_refresh_rate");
        public static final android.net.Uri MIN_REFRESH_RATE_URI = android.provider.Settings.System.getUriFor("min_refresh_rate");

        android.hardware.display.BrightnessInfo getBrightnessInfo(int i);

        android.provider.DeviceConfigInterface getDeviceConfig();

        android.view.Display getDisplay(int i);

        boolean getDisplayInfo(int i, android.view.DisplayInfo displayInfo);

        android.hardware.display.DisplayManagerInternal getDisplayManagerInternal();

        android.view.Display[] getDisplays();

        com.android.server.sensors.SensorManagerInternal getSensorManagerInternal();

        com.android.server.statusbar.StatusBarManagerInternal getStatusBarManagerInternal();

        com.android.server.display.mode.VotesStatsReporter getVotesStatsReporter(boolean z);

        boolean isDozeState(android.view.Display display);

        void registerDisplayListener(android.hardware.display.DisplayManager.DisplayListener displayListener, android.os.Handler handler);

        void registerDisplayListener(android.hardware.display.DisplayManager.DisplayListener displayListener, android.os.Handler handler, long j);

        void registerMinRefreshRateObserver(android.content.ContentResolver contentResolver, android.database.ContentObserver contentObserver);

        void registerPeakRefreshRateObserver(android.content.ContentResolver contentResolver, android.database.ContentObserver contentObserver);

        boolean registerThermalServiceListener(android.os.IThermalEventListener iThermalEventListener);

        boolean supportsFrameRateOverride();

        void unregisterThermalServiceListener(android.os.IThermalEventListener iThermalEventListener);
    }

    public DisplayModeDirector(android.content.Context context, android.os.Handler handler, com.android.server.display.feature.DisplayManagerFlags displayManagerFlags, com.android.server.display.mode.DisplayModeDirector.DisplayDeviceConfigProvider displayDeviceConfigProvider) {
        this(context, handler, new com.android.server.display.mode.DisplayModeDirector.RealInjector(context), displayManagerFlags, displayDeviceConfigProvider);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DisplayModeDirector(android.content.Context context, android.os.Handler handler, com.android.server.display.mode.DisplayModeDirector.Injector injector, com.android.server.display.feature.DisplayManagerFlags displayManagerFlags, com.android.server.display.mode.DisplayModeDirector.DisplayDeviceConfigProvider displayDeviceConfigProvider) {
        this.mLock = new java.lang.Object();
        this.mDisplayDeviceConfigByDisplay = new android.util.SparseArray<>();
        this.mModeSwitchingType = 1;
        this.mIsDisplayResolutionRangeVotingEnabled = displayManagerFlags.isDisplayResolutionRangeVotingEnabled();
        this.mIsUserPreferredModeVoteEnabled = displayManagerFlags.isUserPreferredModeVoteEnabled();
        this.mIsExternalDisplayLimitModeEnabled = displayManagerFlags.isExternalDisplayLimitModeEnabled();
        this.mIsDisplaysRefreshRatesSynchronizationEnabled = displayManagerFlags.isDisplaysRefreshRatesSynchronizationEnabled();
        this.mIsBackUpSmoothDisplayAndForcePeakRefreshRateEnabled = displayManagerFlags.isBackUpSmoothDisplayAndForcePeakRefreshRateEnabled();
        this.mDisplayManagerFlags = displayManagerFlags;
        this.mDisplayDeviceConfigProvider = displayDeviceConfigProvider;
        this.mContext = context;
        this.mHandler = new com.android.server.display.mode.DisplayModeDirector.DisplayModeDirectorHandler(handler.getLooper());
        this.mInjector = injector;
        this.mVotesStatsReporter = injector.getVotesStatsReporter(displayManagerFlags.isRefreshRateVotingTelemetryEnabled());
        this.mSupportedModesByDisplay = new android.util.SparseArray<>();
        this.mAppSupportedModesByDisplay = new android.util.SparseArray<>();
        this.mDefaultModeByDisplay = new android.util.SparseArray<>();
        this.mAppRequestObserver = new com.android.server.display.mode.DisplayModeDirector.AppRequestObserver(displayManagerFlags);
        this.mConfigParameterProvider = new com.android.server.display.feature.DeviceConfigParameterProvider(injector.getDeviceConfig());
        this.mDeviceConfigDisplaySettings = new com.android.server.display.mode.DisplayModeDirector.DeviceConfigDisplaySettings();
        this.mSettingsObserver = new com.android.server.display.mode.DisplayModeDirector.SettingsObserver(context, handler, displayManagerFlags);
        this.mBrightnessObserver = new com.android.server.display.mode.DisplayModeDirector.BrightnessObserver(context, handler, injector, displayManagerFlags);
        this.mDefaultDisplayDeviceConfig = null;
        this.mUdfpsObserver = new com.android.server.display.mode.DisplayModeDirector.UdfpsObserver();
        this.mVotesStorage = new com.android.server.display.mode.VotesStorage(new com.android.server.display.mode.VotesStorage.Listener() { // from class: com.android.server.display.mode.DisplayModeDirector$$ExternalSyntheticLambda0
            @Override // com.android.server.display.mode.VotesStorage.Listener
            public final void onChanged() {
                this.f$0.lambda$start$0();
            }
        }, this.mVotesStatsReporter);
        this.mDisplayObserver = new com.android.server.display.mode.DisplayModeDirector.DisplayObserver(context, handler, this.mVotesStorage, injector);
        this.mSensorObserver = new com.android.server.display.mode.ProximitySensorObserver(this.mVotesStorage, injector);
        this.mSkinThermalStatusObserver = new com.android.server.display.mode.SkinThermalStatusObserver(injector, this.mVotesStorage);
        this.mHbmObserver = new com.android.server.display.mode.DisplayModeDirector.HbmObserver(injector, this.mVotesStorage, com.android.internal.os.BackgroundThread.getHandler(), this.mDeviceConfigDisplaySettings);
        if (displayManagerFlags.isRestrictDisplayModesEnabled()) {
            this.mSystemRequestObserver = new com.android.server.display.mode.SystemRequestObserver(this.mVotesStorage);
        } else {
            this.mSystemRequestObserver = null;
        }
        this.mAlwaysRespectAppRequest = false;
        this.mSupportsFrameRateOverride = injector.supportsFrameRateOverride();
        mDmdExt = (com.android.server.display.IDisplayModeDirectorExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IDisplayModeDirectorExt.class).create();
        this.mIsResolutionSwitchByMode = false;
    }

    public void start(android.hardware.SensorManager sensorManager) {
        mDmdExt.registerResolutionChangeListener(new java.lang.Runnable() { // from class: com.android.server.display.mode.DisplayModeDirector$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$start$0();
            }
        });
        this.mDisplayObserver.observe();
        this.mSettingsObserver.observe();
        this.mBrightnessObserver.observe(sensorManager);
        this.mSensorObserver.observe();
        this.mHbmObserver.observe();
        this.mSkinThermalStatusObserver.observe();
        synchronized (this.mLock) {
            lambda$start$0();
        }
    }

    public void onBootCompleted() {
        this.mUdfpsObserver.observe();
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        if (this.mLoggingEnabled == loggingEnabled) {
            return;
        }
        this.mLoggingEnabled = loggingEnabled;
        this.mBrightnessObserver.setLoggingEnabled(loggingEnabled);
        this.mSkinThermalStatusObserver.setLoggingEnabled(loggingEnabled);
        this.mVotesStorage.setLoggingEnabled(loggingEnabled);
    }

    public com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs getDesiredDisplayModeSpecs(int displayId) {
        synchronized (this.mLock) {
            android.util.SparseArray<com.android.server.display.mode.Vote> votes = this.mVotesStorage.getVotes(displayId);
            android.view.Display.Mode[] modes = this.mSupportedModesByDisplay.get(displayId);
            android.view.Display.Mode defaultMode = this.mDefaultModeByDisplay.get(displayId);
            if (modes == null || defaultMode == null) {
                android.util.Slog.e(TAG, "Asked about unknown display, returning empty display mode specs!(id=" + displayId + ")");
                return new com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs();
            }
            java.util.List<android.view.Display.Mode> availableModes = new java.util.ArrayList<>();
            availableModes.add(defaultMode);
            com.android.server.display.mode.VoteSummary primarySummary = new com.android.server.display.mode.VoteSummary(this.mIsDisplayResolutionRangeVotingEnabled, isVrrSupportedLocked(displayId), this.mLoggingEnabled, this.mSupportsFrameRateOverride);
            int lowestConsideredPriority = 0;
            int highestConsideredPriority = 20;
            if (this.mAlwaysRespectAppRequest) {
                lowestConsideredPriority = 5;
                highestConsideredPriority = 7;
            }
            while (true) {
                if (lowestConsideredPriority > highestConsideredPriority) {
                    break;
                }
                primarySummary.applyVotes(votes, lowestConsideredPriority, highestConsideredPriority);
                if (this.mIsResolutionSwitchByMode && filterResolutionSupport(modes) != null) {
                    this.mIsResolutionSwitchByMode = false;
                    android.view.Display.Mode resolutionSwitchSupportMode = filterResolutionSupport(modes);
                    primarySummary.adjustSize(resolutionSwitchSupportMode, modes);
                } else {
                    primarySummary.adjustSize(defaultMode, modes);
                }
                if (displayId == 0) {
                    primarySummary.width = mDmdExt.getWidth(primarySummary.width);
                    primarySummary.height = mDmdExt.getHeight(primarySummary.height);
                }
                availableModes = primarySummary.filterModes(modes);
                if (!availableModes.isEmpty()) {
                    if (this.mLoggingEnabled) {
                        android.util.Slog.w(TAG, "Found available modes=" + availableModes + " with lowest priority considered " + com.android.server.display.mode.Vote.priorityToString(lowestConsideredPriority) + " and summary: " + primarySummary);
                    }
                } else {
                    if (this.mLoggingEnabled) {
                        android.util.Slog.w(TAG, "Couldn't find available modes with lowest priority set to " + com.android.server.display.mode.Vote.priorityToString(lowestConsideredPriority) + " and with the following summary: " + primarySummary);
                    }
                    lowestConsideredPriority++;
                }
            }
            com.android.server.display.mode.VoteSummary appRequestSummary = new com.android.server.display.mode.VoteSummary(this.mIsDisplayResolutionRangeVotingEnabled, isVrrSupportedLocked(displayId), this.mLoggingEnabled, this.mSupportsFrameRateOverride);
            appRequestSummary.applyVotes(votes, 5, 20);
            appRequestSummary.limitRefreshRanges(primarySummary);
            android.view.Display.Mode baseMode = primarySummary.selectBaseMode(availableModes, defaultMode);
            if (this.mVotesStatsReporter != null) {
                this.mVotesStatsReporter.reportVotesActivated(displayId, lowestConsideredPriority, baseMode, votes);
            }
            if (baseMode == null) {
                android.util.Slog.w(TAG, "Can't find a set of allowed modes which satisfies the votes. Falling back to the default mode. Display = " + displayId + ", votes = " + votes + ", supported modes = " + java.util.Arrays.toString(modes));
                float fps = defaultMode.getRefreshRate();
                android.view.SurfaceControl.RefreshRateRange range = new android.view.SurfaceControl.RefreshRateRange(fps, fps);
                android.view.SurfaceControl.RefreshRateRanges ranges = new android.view.SurfaceControl.RefreshRateRanges(range, range);
                return new com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs(defaultMode.getModeId(), false, ranges, ranges, this.mBrightnessObserver.getIdleScreenRefreshRateConfig());
            }
            boolean modeSwitchingDisabled = this.mModeSwitchingType == 0 || this.mModeSwitchingType == 3;
            if (modeSwitchingDisabled || primarySummary.disableRefreshRateSwitching) {
                float fps2 = baseMode.getRefreshRate();
                primarySummary.disableModeSwitching(fps2);
                if (modeSwitchingDisabled) {
                    appRequestSummary.disableModeSwitching(fps2);
                    primarySummary.disableRenderRateSwitching(fps2);
                    if (this.mModeSwitchingType == 0) {
                        appRequestSummary.disableRenderRateSwitching(fps2);
                    }
                }
            }
            boolean allowGroupSwitching = this.mModeSwitchingType == 2;
            if (this.mDisplayObserver.isExternalDisplayLocked(displayId)) {
                primarySummary.maxRenderFrameRate = java.lang.Math.max(baseMode.getRefreshRate(), primarySummary.maxRenderFrameRate);
                appRequestSummary.maxRenderFrameRate = java.lang.Math.max(baseMode.getRefreshRate(), appRequestSummary.maxRenderFrameRate);
            }
            return new com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs(baseMode.getModeId(), allowGroupSwitching, new android.view.SurfaceControl.RefreshRateRanges(new android.view.SurfaceControl.RefreshRateRange(primarySummary.minPhysicalRefreshRate, primarySummary.maxPhysicalRefreshRate), new android.view.SurfaceControl.RefreshRateRange(primarySummary.minRenderFrameRate, primarySummary.maxRenderFrameRate)), new android.view.SurfaceControl.RefreshRateRanges(new android.view.SurfaceControl.RefreshRateRange(appRequestSummary.minPhysicalRefreshRate, appRequestSummary.maxPhysicalRefreshRate), new android.view.SurfaceControl.RefreshRateRange(appRequestSummary.minRenderFrameRate, appRequestSummary.maxRenderFrameRate)), this.mBrightnessObserver.getIdleScreenRefreshRateConfig());
        }
    }

    private android.view.Display.Mode filterResolutionSupport(android.view.Display.Mode[] supportedModes) {
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        java.lang.String resolution = android.provider.Settings.System.getStringForUser(cr, getMtkSettingsExtSystemSetting("SWITCH_RESOLUTION_BY_MODE"), cr.getUserId());
        int modeId = java.lang.Integer.parseInt(resolution);
        for (android.view.Display.Mode m : supportedModes) {
            if (modeId == m.getModeId()) {
                android.util.Slog.w(TAG, "Discarding mode " + m.getModeId() + ": actualWidth=" + m.getPhysicalWidth() + ": actualHeight=" + m.getPhysicalHeight());
                return m;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getMtkSettingsExtSystemSetting(java.lang.String name) {
        try {
            java.lang.Class<?> rCls = java.lang.Class.forName("com.mediatek.provider.MtkSettingsExt$System", false, java.lang.ClassLoader.getSystemClassLoader());
            java.lang.reflect.Field field = rCls.getField(name);
            field.setAccessible(true);
            return (java.lang.String) field.get(rCls);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Cannot get MTK settings - " + e);
            return "";
        }
    }

    public com.android.server.display.mode.DisplayModeDirector.AppRequestObserver getAppRequestObserver() {
        return this.mAppRequestObserver;
    }

    private boolean isVrrSupportedLocked(int displayId) {
        com.android.server.display.DisplayDeviceConfig config = this.mDisplayDeviceConfigByDisplay.get(displayId);
        return config != null && config.isVrrSupportEnabled();
    }

    public void setDesiredDisplayModeSpecsListener(com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecsListener desiredDisplayModeSpecsListener) {
        synchronized (this.mLock) {
            this.mDesiredDisplayModeSpecsListener = desiredDisplayModeSpecsListener;
        }
    }

    public void defaultDisplayDeviceUpdated(com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        synchronized (this.mLock) {
            this.mDefaultDisplayDeviceConfig = displayDeviceConfig;
            this.mSettingsObserver.setRefreshRates(displayDeviceConfig, true);
            this.mBrightnessObserver.updateBlockingZoneThresholds(displayDeviceConfig, true);
            this.mBrightnessObserver.reloadLightSensor(displayDeviceConfig);
            this.mHbmObserver.setupHdrRefreshRates(displayDeviceConfig);
        }
    }

    public void setShouldAlwaysRespectAppRequestedMode(boolean enabled) {
        synchronized (this.mLock) {
            if (this.mAlwaysRespectAppRequest != enabled) {
                this.mAlwaysRespectAppRequest = enabled;
                lambda$start$0();
            }
        }
    }

    public boolean shouldAlwaysRespectAppRequestedMode() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mAlwaysRespectAppRequest;
        }
        return z;
    }

    public void setModeSwitchingType(int newType) {
        synchronized (this.mLock) {
            if (newType != this.mModeSwitchingType) {
                this.mModeSwitchingType = newType;
                lambda$start$0();
            }
        }
    }

    public int getModeSwitchingType() {
        int i;
        synchronized (this.mLock) {
            i = this.mModeSwitchingType;
        }
        return i;
    }

    com.android.server.display.mode.Vote getVote(int displayId, int priority) {
        android.util.SparseArray<com.android.server.display.mode.Vote> votes = this.mVotesStorage.getVotes(displayId);
        return votes.get(priority);
    }

    public void requestDisplayModes(android.os.IBinder token, int displayId, int[] modeIds) {
        boolean vrrSupported;
        if (this.mSystemRequestObserver != null) {
            synchronized (this.mLock) {
                vrrSupported = isVrrSupportedLocked(displayId);
            }
            if (vrrSupported) {
                this.mSystemRequestObserver.requestDisplayModes(token, displayId, modeIds);
            }
        }
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println(TAG);
        synchronized (this.mLock) {
            pw.println("  mSupportedModesByDisplay:");
            for (int i = 0; i < this.mSupportedModesByDisplay.size(); i++) {
                int id = this.mSupportedModesByDisplay.keyAt(i);
                android.view.Display.Mode[] modes = this.mSupportedModesByDisplay.valueAt(i);
                pw.println("    " + id + " -> " + java.util.Arrays.toString(modes));
            }
            pw.println("  mAppSupportedModesByDisplay:");
            for (int i2 = 0; i2 < this.mAppSupportedModesByDisplay.size(); i2++) {
                int id2 = this.mAppSupportedModesByDisplay.keyAt(i2);
                android.view.Display.Mode[] modes2 = this.mAppSupportedModesByDisplay.valueAt(i2);
                pw.println("    " + id2 + " -> " + java.util.Arrays.toString(modes2));
            }
            pw.println("  mDefaultModeByDisplay:");
            for (int i3 = 0; i3 < this.mDefaultModeByDisplay.size(); i3++) {
                int id3 = this.mDefaultModeByDisplay.keyAt(i3);
                android.view.Display.Mode mode = this.mDefaultModeByDisplay.valueAt(i3);
                pw.println("    " + id3 + " -> " + mode);
            }
            pw.println("  mModeSwitchingType: " + switchingTypeToString(this.mModeSwitchingType));
            pw.println("  mAlwaysRespectAppRequest: " + this.mAlwaysRespectAppRequest);
            this.mSettingsObserver.dumpLocked(pw);
            this.mAppRequestObserver.dumpLocked(pw);
            this.mBrightnessObserver.dumpLocked(pw);
            this.mUdfpsObserver.dumpLocked(pw);
            this.mHbmObserver.dumpLocked(pw);
            this.mSkinThermalStatusObserver.dumpLocked(pw);
        }
        this.mVotesStorage.dump(pw);
        this.mSensorObserver.dump(pw);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getMaxRefreshRateLocked(int displayId) {
        android.view.Display.Mode[] modes = this.mSupportedModesByDisplay.get(displayId);
        float maxRefreshRate = 0.0f;
        for (android.view.Display.Mode mode : modes) {
            if (mode.getRefreshRate() > maxRefreshRate) {
                maxRefreshRate = mode.getRefreshRate();
            }
        }
        return maxRefreshRate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: notifyDesiredDisplayModeSpecsChangedLocked, reason: merged with bridge method [inline-methods] */
    public void lambda$start$0() {
        if (this.mDesiredDisplayModeSpecsListener != null && !this.mHandler.hasMessages(1)) {
            android.os.Message msg = this.mHandler.obtainMessage(1, this.mDesiredDisplayModeSpecsListener);
            msg.sendToTarget();
        } else if (mDmdExt.isHighLoadRefreshRateEnabled() && this.mDesiredDisplayModeSpecsListener != null && this.mHandler.hasMessages(1)) {
            this.mHandler.sendMessageAtFrontOfQueue(this.mHandler.obtainMessage(1, this.mDesiredDisplayModeSpecsListener));
        }
    }

    private static java.lang.String switchingTypeToString(int type) {
        switch (type) {
            case 0:
                return "SWITCHING_TYPE_NONE";
            case 1:
                return "SWITCHING_TYPE_WITHIN_GROUPS";
            case 2:
                return "SWITCHING_TYPE_ACROSS_AND_WITHIN_GROUPS";
            case 3:
                return "SWITCHING_TYPE_RENDER_FRAME_RATE_ONLY";
            default:
                return "Unknown SwitchingType " + type;
        }
    }

    void injectSupportedModesByDisplay(android.util.SparseArray<android.view.Display.Mode[]> supportedModesByDisplay) {
        this.mSupportedModesByDisplay = supportedModesByDisplay;
    }

    void injectAppSupportedModesByDisplay(android.util.SparseArray<android.view.Display.Mode[]> appSupportedModesByDisplay) {
        this.mAppSupportedModesByDisplay = appSupportedModesByDisplay;
    }

    void injectDefaultModeByDisplay(android.util.SparseArray<android.view.Display.Mode> defaultModeByDisplay) {
        this.mDefaultModeByDisplay = defaultModeByDisplay;
    }

    void injectDisplayDeviceConfigByDisplay(android.util.SparseArray<com.android.server.display.DisplayDeviceConfig> ddcByDisplay) {
        this.mDisplayDeviceConfigByDisplay = ddcByDisplay;
    }

    void injectVotesByDisplay(android.util.SparseArray<android.util.SparseArray<com.android.server.display.mode.Vote>> votesByDisplay) {
        this.mVotesStorage.injectVotesByDisplay(votesByDisplay);
    }

    void injectBrightnessObserver(com.android.server.display.mode.DisplayModeDirector.BrightnessObserver brightnessObserver) {
        this.mBrightnessObserver = brightnessObserver;
    }

    com.android.server.display.mode.DisplayModeDirector.BrightnessObserver getBrightnessObserver() {
        return this.mBrightnessObserver;
    }

    com.android.server.display.mode.DisplayModeDirector.SettingsObserver getSettingsObserver() {
        return this.mSettingsObserver;
    }

    com.android.server.display.mode.DisplayModeDirector.UdfpsObserver getUdpfsObserver() {
        return this.mUdfpsObserver;
    }

    com.android.server.display.mode.DisplayModeDirector.HbmObserver getHbmObserver() {
        return this.mHbmObserver;
    }

    com.android.server.display.mode.DisplayModeDirector.DisplayObserver getDisplayObserver() {
        return this.mDisplayObserver;
    }

    com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs getDesiredDisplayModeSpecsWithInjectedFpsSettings(float minRefreshRate, float peakRefreshRate, float defaultRefreshRate) {
        com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs;
        synchronized (this.mLock) {
            this.mSettingsObserver.updateRefreshRateSettingLocked(minRefreshRate, peakRefreshRate, defaultRefreshRate, 0);
            desiredDisplayModeSpecs = getDesiredDisplayModeSpecs(0);
        }
        return desiredDisplayModeSpecs;
    }

    private final class DisplayModeDirectorHandler extends android.os.Handler {
        DisplayModeDirectorHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecsListener desiredDisplayModeSpecsListener = (com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecsListener) msg.obj;
                    desiredDisplayModeSpecsListener.onDesiredDisplayModeSpecsChanged();
                    break;
                case 2:
                    android.util.Pair<float[], float[]> thresholds = (android.util.Pair) msg.obj;
                    com.android.server.display.mode.DisplayModeDirector.this.mBrightnessObserver.onDeviceConfigLowBrightnessThresholdsChanged((float[]) thresholds.first, (float[]) thresholds.second);
                    break;
                case 3:
                    java.lang.Float defaultPeakRefreshRate = (java.lang.Float) msg.obj;
                    com.android.server.display.mode.DisplayModeDirector.this.mSettingsObserver.onDeviceConfigDefaultPeakRefreshRateChanged(defaultPeakRefreshRate);
                    break;
                case 4:
                    int refreshRateInZone = msg.arg1;
                    com.android.server.display.mode.DisplayModeDirector.this.mBrightnessObserver.onDeviceConfigRefreshRateInLowZoneChanged(refreshRateInZone);
                    break;
                case 5:
                    int refreshRateInZone2 = msg.arg1;
                    com.android.server.display.mode.DisplayModeDirector.this.mBrightnessObserver.onDeviceConfigRefreshRateInHighZoneChanged(refreshRateInZone2);
                    break;
                case 6:
                    android.util.Pair<float[], float[]> thresholds2 = (android.util.Pair) msg.obj;
                    com.android.server.display.mode.DisplayModeDirector.this.mBrightnessObserver.onDeviceConfigHighBrightnessThresholdsChanged((float[]) thresholds2.first, (float[]) thresholds2.second);
                    break;
                case 7:
                    int refreshRateInHbmHdr = msg.arg1;
                    com.android.server.display.mode.DisplayModeDirector.this.mHbmObserver.onDeviceConfigRefreshRateInHbmSunlightChanged(refreshRateInHbmHdr);
                    break;
                case 8:
                    int refreshRateInHbmHdr2 = msg.arg1;
                    com.android.server.display.mode.DisplayModeDirector.this.mHbmObserver.onDeviceConfigRefreshRateInHbmHdrChanged(refreshRateInHbmHdr2);
                    break;
            }
        }
    }

    public static final class DesiredDisplayModeSpecs {
        public boolean allowGroupSwitching;
        public final android.view.SurfaceControl.RefreshRateRanges appRequest;
        public int baseModeId;
        public android.view.SurfaceControl.IdleScreenRefreshRateConfig mIdleScreenRefreshRateConfig;
        public final android.view.SurfaceControl.RefreshRateRanges primary;
        public int vrrPolicy;

        public DesiredDisplayModeSpecs() {
            this.primary = new android.view.SurfaceControl.RefreshRateRanges();
            this.appRequest = new android.view.SurfaceControl.RefreshRateRanges();
        }

        public DesiredDisplayModeSpecs(int baseModeId, boolean allowGroupSwitching, android.view.SurfaceControl.RefreshRateRanges primary, android.view.SurfaceControl.RefreshRateRanges appRequest, android.view.SurfaceControl.IdleScreenRefreshRateConfig idleScreenRefreshRateConfig) {
            this.baseModeId = baseModeId;
            this.allowGroupSwitching = allowGroupSwitching;
            this.primary = primary;
            this.appRequest = appRequest;
            this.vrrPolicy = com.android.server.display.mode.DisplayModeDirector.mDmdExt.getVrrPolicy(this.primary.render.max);
            this.mIdleScreenRefreshRateConfig = idleScreenRefreshRateConfig;
        }

        public java.lang.String toString() {
            return java.lang.String.format("baseModeId=%d allowGroupSwitching=%b primary=%s appRequest=%s idleScreenRefreshRateConfig=%s", java.lang.Integer.valueOf(this.baseModeId), java.lang.Boolean.valueOf(this.allowGroupSwitching), this.primary.toString(), this.appRequest.toString(), java.lang.String.valueOf(this.mIdleScreenRefreshRateConfig));
        }

        public boolean equals(java.lang.Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs)) {
                return false;
            }
            com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs = (com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs) other;
            return this.baseModeId == desiredDisplayModeSpecs.baseModeId && this.allowGroupSwitching == desiredDisplayModeSpecs.allowGroupSwitching && this.primary.equals(desiredDisplayModeSpecs.primary) && this.appRequest.equals(desiredDisplayModeSpecs.appRequest) && this.vrrPolicy == desiredDisplayModeSpecs.vrrPolicy && java.util.Objects.equals(this.mIdleScreenRefreshRateConfig, desiredDisplayModeSpecs.mIdleScreenRefreshRateConfig);
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.baseModeId), java.lang.Boolean.valueOf(this.allowGroupSwitching), this.primary, this.appRequest, this.mIdleScreenRefreshRateConfig, java.lang.Integer.valueOf(this.vrrPolicy));
        }

        public void copyFrom(com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs other) {
            this.baseModeId = other.baseModeId;
            this.allowGroupSwitching = other.allowGroupSwitching;
            this.primary.physical.min = other.primary.physical.min;
            this.primary.physical.max = other.primary.physical.max;
            this.primary.render.min = other.primary.render.min;
            this.primary.render.max = other.primary.render.max;
            this.appRequest.physical.min = other.appRequest.physical.min;
            this.appRequest.physical.max = other.appRequest.physical.max;
            this.appRequest.render.min = other.appRequest.render.min;
            this.appRequest.render.max = other.appRequest.render.max;
            this.vrrPolicy = other.vrrPolicy;
            if (other.mIdleScreenRefreshRateConfig == null) {
                this.mIdleScreenRefreshRateConfig = null;
            } else {
                this.mIdleScreenRefreshRateConfig = new android.view.SurfaceControl.IdleScreenRefreshRateConfig(other.mIdleScreenRefreshRateConfig.timeoutMillis);
            }
        }
    }

    final class SettingsObserver extends android.database.ContentObserver {
        private final android.content.Context mContext;
        private float mDefaultPeakRefreshRate;
        private float mDefaultRefreshRate;
        private final android.hardware.display.DisplayManager.DisplayListener mDisplayListener;
        private final android.os.Handler mHandler;
        private boolean mIsLowPower;
        private final android.net.Uri mLowPowerModeSetting;
        private final android.net.Uri mMatchContentFrameRateSetting;
        private final android.net.Uri mMinRefreshRateSetting;
        private final boolean mPeakRefreshRatePhysicalLimitEnabled;
        private final android.net.Uri mPeakRefreshRateSetting;
        private final android.net.Uri mPolicyChanged;
        private final android.net.Uri mSwitchResolutionByMode;
        private final boolean mVsyncLowPowerVoteEnabled;

        SettingsObserver(android.content.Context context, android.os.Handler handler, com.android.server.display.feature.DisplayManagerFlags flags) {
            super(handler);
            this.mPeakRefreshRateSetting = android.provider.Settings.System.getUriFor("peak_refresh_rate");
            this.mMinRefreshRateSetting = android.provider.Settings.System.getUriFor("min_refresh_rate");
            this.mLowPowerModeSetting = android.provider.Settings.Global.getUriFor("low_power");
            this.mMatchContentFrameRateSetting = android.provider.Settings.Secure.getUriFor("match_content_frame_rate");
            this.mSwitchResolutionByMode = android.provider.Settings.System.getUriFor(com.android.server.display.mode.DisplayModeDirector.this.getMtkSettingsExtSystemSetting("SWITCH_RESOLUTION_BY_MODE"));
            this.mPolicyChanged = android.provider.Settings.System.getUriFor("adfr_policy_change");
            this.mIsLowPower = false;
            this.mDisplayListener = new android.hardware.display.DisplayManager.DisplayListener() { // from class: com.android.server.display.mode.DisplayModeDirector.SettingsObserver.1
                @Override // android.hardware.display.DisplayManager.DisplayListener
                public void onDisplayAdded(int displayId) {
                    synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                        com.android.server.display.mode.DisplayModeDirector.SettingsObserver.this.updateLowPowerModeAllowedModesLocked();
                    }
                }

                @Override // android.hardware.display.DisplayManager.DisplayListener
                public void onDisplayRemoved(int displayId) {
                    com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 15, null);
                }

                @Override // android.hardware.display.DisplayManager.DisplayListener
                public void onDisplayChanged(int displayId) {
                    synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                        com.android.server.display.mode.DisplayModeDirector.SettingsObserver.this.updateLowPowerModeAllowedModesLocked();
                    }
                }
            };
            this.mContext = context;
            this.mHandler = handler;
            this.mVsyncLowPowerVoteEnabled = flags.isVsyncLowPowerVoteEnabled();
            this.mPeakRefreshRatePhysicalLimitEnabled = flags.isPeakRefreshRatePhysicalLimitEnabled();
            setRefreshRates(null, false);
        }

        void setRefreshRates(com.android.server.display.DisplayDeviceConfig displayDeviceConfig, boolean attemptReadFromFeatureParams) {
            com.android.server.display.config.RefreshRateData refreshRateData = displayDeviceConfig == null ? null : displayDeviceConfig.getRefreshRateData();
            setDefaultPeakRefreshRate(displayDeviceConfig, attemptReadFromFeatureParams);
            this.mDefaultRefreshRate = refreshRateData == null ? this.mContext.getResources().getInteger(android.R.integer.config_defaultNightDisplayCustomStartTime) : refreshRateData.defaultRefreshRate;
        }

        public void observe() {
            android.content.ContentResolver cr = this.mContext.getContentResolver();
            com.android.server.display.mode.DisplayModeDirector.this.mInjector.registerPeakRefreshRateObserver(cr, this);
            com.android.server.display.mode.DisplayModeDirector.this.mInjector.registerMinRefreshRateObserver(cr, this);
            cr.registerContentObserver(this.mLowPowerModeSetting, false, this, 0);
            cr.registerContentObserver(this.mMatchContentFrameRateSetting, false, this);
            com.android.server.display.mode.DisplayModeDirector.this.mInjector.registerDisplayListener(this.mDisplayListener, this.mHandler);
            if (com.android.server.display.mode.DisplayModeDirector.mDmdExt.isAdfrEnabled()) {
                cr.registerContentObserver(this.mPolicyChanged, false, this, 0);
            }
            cr.registerContentObserver(this.mSwitchResolutionByMode, false, this);
            float deviceConfigDefaultPeakRefresh = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getPeakRefreshRateDefault();
            if (deviceConfigDefaultPeakRefresh != -1.0f) {
                this.mDefaultPeakRefreshRate = deviceConfigDefaultPeakRefresh;
            }
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                updateRefreshRateSettingLocked();
                updateLowPowerModeSettingLocked();
                updateModeSwitchingTypeSettingLocked();
            }
        }

        public void setDefaultRefreshRate(float refreshRate) {
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                this.mDefaultRefreshRate = refreshRate;
                updateRefreshRateSettingLocked();
            }
        }

        public void onDeviceConfigDefaultPeakRefreshRateChanged(java.lang.Float defaultPeakRefreshRate) {
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                if (defaultPeakRefreshRate == null) {
                    setDefaultPeakRefreshRate(com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig, false);
                } else if (this.mDefaultPeakRefreshRate != defaultPeakRefreshRate.floatValue()) {
                    this.mDefaultPeakRefreshRate = defaultPeakRefreshRate.floatValue();
                }
                updateRefreshRateSettingLocked();
            }
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                if (this.mPeakRefreshRateSetting.equals(uri) || this.mMinRefreshRateSetting.equals(uri)) {
                    updateRefreshRateSettingLocked();
                } else if (this.mLowPowerModeSetting.equals(uri)) {
                    updateLowPowerModeSettingLocked();
                } else if (this.mMatchContentFrameRateSetting.equals(uri)) {
                    updateModeSwitchingTypeSettingLocked();
                } else if (this.mSwitchResolutionByMode.equals(uri)) {
                    com.android.server.display.mode.DisplayModeDirector.this.mIsResolutionSwitchByMode = true;
                    com.android.server.display.mode.DisplayModeDirector.this.lambda$start$0();
                }
                if (com.android.server.display.mode.DisplayModeDirector.mDmdExt.isAdfrEnabled() && this.mPolicyChanged.equals(uri)) {
                    com.android.server.display.mode.DisplayModeDirector.this.lambda$start$0();
                }
            }
        }

        float getDefaultRefreshRate() {
            return this.mDefaultRefreshRate;
        }

        float getDefaultPeakRefreshRate() {
            return this.mDefaultPeakRefreshRate;
        }

        private void setDefaultPeakRefreshRate(com.android.server.display.DisplayDeviceConfig displayDeviceConfig, boolean attemptReadFromFeatureParams) {
            float defaultPeakRefreshRate = -1.0f;
            if (attemptReadFromFeatureParams) {
                try {
                    defaultPeakRefreshRate = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getPeakRefreshRateDefault();
                } catch (java.lang.Exception e) {
                }
            }
            if (defaultPeakRefreshRate == -1.0f) {
                defaultPeakRefreshRate = displayDeviceConfig == null ? this.mContext.getResources().getInteger(android.R.integer.config_defaultNightDisplayAutoMode) : displayDeviceConfig.getRefreshRateData().defaultPeakRefreshRate;
            }
            this.mDefaultPeakRefreshRate = defaultPeakRefreshRate;
        }

        private void updateLowPowerModeSettingLocked() {
            com.android.server.display.mode.Vote vote;
            this.mIsLowPower = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "low_power", 0) != 0;
            if (this.mIsLowPower) {
                vote = null;
                android.util.Slog.e(com.android.server.display.mode.DisplayModeDirector.TAG, "DO NOT switch to 60hz when low power");
            } else {
                vote = null;
            }
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateGlobalVote(16, vote);
            com.android.server.display.mode.DisplayModeDirector.this.mBrightnessObserver.onLowPowerModeEnabledLocked(this.mIsLowPower);
            updateLowPowerModeAllowedModesLocked();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateLowPowerModeAllowedModesLocked() {
            if (!this.mVsyncLowPowerVoteEnabled) {
                return;
            }
            if (this.mIsLowPower) {
                for (int i = 0; i < com.android.server.display.mode.DisplayModeDirector.this.mDisplayDeviceConfigByDisplay.size(); i++) {
                    com.android.server.display.DisplayDeviceConfig config = (com.android.server.display.DisplayDeviceConfig) com.android.server.display.mode.DisplayModeDirector.this.mDisplayDeviceConfigByDisplay.valueAt(i);
                    if (config != null) {
                        java.util.List<com.android.server.display.config.SupportedModeData> supportedModes = config.getRefreshRateData().lowPowerSupportedModes;
                        com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(com.android.server.display.mode.DisplayModeDirector.this.mDisplayDeviceConfigByDisplay.keyAt(i), 15, com.android.server.display.mode.Vote.forSupportedRefreshRates(supportedModes));
                    }
                }
                return;
            }
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.removeAllVotesForPriority(15);
        }

        private void updateRefreshRateSettingLocked() {
            for (int i = 0; i < com.android.server.display.mode.DisplayModeDirector.this.mSupportedModesByDisplay.size(); i++) {
                updateRefreshRateSettingLocked(com.android.server.display.mode.DisplayModeDirector.this.mSupportedModesByDisplay.keyAt(i));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateRefreshRateSettingLocked(int displayId) {
            android.content.ContentResolver cr = this.mContext.getContentResolver();
            if (!com.android.server.display.mode.DisplayModeDirector.this.mSupportedModesByDisplay.contains(displayId)) {
                android.util.Slog.e(com.android.server.display.mode.DisplayModeDirector.TAG, "Cannot update refresh rate setting: no supported modes for display " + displayId);
                return;
            }
            float highestRefreshRate = com.android.server.display.mode.DisplayModeDirector.this.getMaxRefreshRateLocked(displayId);
            float minRefreshRate = android.provider.Settings.System.getFloatForUser(cr, "min_refresh_rate", 0.0f, cr.getUserId());
            if (java.lang.Float.isInfinite(minRefreshRate)) {
                minRefreshRate = highestRefreshRate;
            }
            float peakRefreshRate = android.provider.Settings.System.getFloatForUser(cr, "peak_refresh_rate", this.mDefaultPeakRefreshRate, cr.getUserId());
            if (java.lang.Float.isInfinite(peakRefreshRate)) {
                peakRefreshRate = highestRefreshRate;
            }
            updateRefreshRateSettingLocked(minRefreshRate, peakRefreshRate, this.mDefaultRefreshRate, displayId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateRefreshRateSettingLocked(float minRefreshRate, float peakRefreshRate, float defaultRefreshRate, int displayId) {
            com.android.server.display.mode.Vote peakRenderVote;
            float maxRefreshRate;
            com.android.server.display.mode.Vote peakVote;
            if (this.mPeakRefreshRatePhysicalLimitEnabled) {
                if (peakRefreshRate <= 0.0f) {
                    peakVote = null;
                } else {
                    peakVote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(0.0f, java.lang.Math.max(minRefreshRate, peakRefreshRate));
                }
                com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 8, peakVote);
            }
            if (peakRefreshRate <= 0.0f) {
                peakRenderVote = null;
            } else {
                peakRenderVote = com.android.server.display.mode.Vote.forRenderFrameRates(0.0f, java.lang.Math.max(minRefreshRate, peakRefreshRate));
            }
            if (minRefreshRate < 0.0f) {
                minRefreshRate = 0.0f;
            }
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 9, peakRenderVote);
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 3, com.android.server.display.mode.Vote.forRenderFrameRates(minRefreshRate, Float.POSITIVE_INFINITY));
            com.android.server.display.mode.Vote defaultVote = defaultRefreshRate != 0.0f ? com.android.server.display.mode.Vote.forRenderFrameRates(0.0f, defaultRefreshRate) : null;
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateGlobalVote(0, defaultVote);
            if (peakRefreshRate == 0.0f && defaultRefreshRate == 0.0f) {
                android.util.Slog.e(com.android.server.display.mode.DisplayModeDirector.TAG, "Default and peak refresh rates are both 0. One of them should be set to a valid value.");
                maxRefreshRate = minRefreshRate;
            } else if (peakRefreshRate == 0.0f) {
                maxRefreshRate = defaultRefreshRate;
            } else if (defaultRefreshRate == 0.0f) {
                maxRefreshRate = peakRefreshRate;
            } else {
                maxRefreshRate = java.lang.Math.min(defaultRefreshRate, peakRefreshRate);
            }
            if (displayId == 0) {
                com.android.server.display.mode.DisplayModeDirector.this.mBrightnessObserver.onRefreshRateSettingChangedLocked(minRefreshRate, maxRefreshRate);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeRefreshRateSetting(int displayId) {
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 9, null);
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 3, null);
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 0, null);
        }

        private void updateModeSwitchingTypeSettingLocked() {
            android.content.ContentResolver cr = this.mContext.getContentResolver();
            int switchingType = android.provider.Settings.Secure.getIntForUser(cr, "match_content_frame_rate", com.android.server.display.mode.DisplayModeDirector.this.mModeSwitchingType, cr.getUserId());
            if (switchingType != com.android.server.display.mode.DisplayModeDirector.this.mModeSwitchingType) {
                com.android.server.display.mode.DisplayModeDirector.this.mModeSwitchingType = switchingType;
                com.android.server.display.mode.DisplayModeDirector.this.lambda$start$0();
            }
        }

        public void dumpLocked(java.io.PrintWriter pw) {
            pw.println("  SettingsObserver");
            pw.println("    mDefaultRefreshRate: " + this.mDefaultRefreshRate);
            pw.println("    mDefaultPeakRefreshRate: " + this.mDefaultPeakRefreshRate);
        }
    }

    public final class AppRequestObserver {
        private final boolean mIgnorePreferredRefreshRate;
        private int mDisplayId = -1;
        private int mModeId = 0;
        private float mRequestedRefreshRate = 0.0f;
        private float mRequestedMinRefreshRateRange = 0.0f;
        private float mRequestedMaxRefreshRateRange = 0.0f;

        AppRequestObserver(com.android.server.display.feature.DisplayManagerFlags flags) {
            this.mIgnorePreferredRefreshRate = flags.ignoreAppPreferredRefreshRateRequest();
        }

        public void setAppRequest(int displayId, int modeId, float requestedRefreshRate, float requestedMinRefreshRateRange, float requestedMaxRefreshRateRange) {
            android.view.Display.Mode requestedMode;
            if (this.mDisplayId != displayId || this.mModeId != modeId || this.mRequestedRefreshRate != requestedRefreshRate || this.mRequestedMinRefreshRateRange != requestedMinRefreshRateRange || this.mRequestedMaxRefreshRateRange != requestedMaxRefreshRateRange) {
                this.mDisplayId = displayId;
                this.mModeId = modeId;
                this.mRequestedRefreshRate = requestedRefreshRate;
                this.mRequestedMinRefreshRateRange = requestedMinRefreshRateRange;
                this.mRequestedMaxRefreshRateRange = requestedMaxRefreshRateRange;
                android.util.Slog.d(com.android.server.display.mode.DisplayModeDirector.TAG, "setAppRequest displayId : " + displayId + "  modeId: " + modeId + "  requestedRefreshRate: " + requestedRefreshRate + "  requestedMinRefreshRateRange: " + requestedMinRefreshRateRange + "  requestedMaxRefreshRateRange: " + requestedMaxRefreshRateRange);
            }
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                requestedMode = findModeLocked(displayId, modeId, requestedRefreshRate);
            }
            com.android.server.display.mode.Vote frameRateVote = getFrameRateVote(requestedMinRefreshRateRange, requestedMaxRefreshRateRange);
            com.android.server.display.mode.Vote baseModeRefreshRateVote = getBaseModeVote(requestedMode, requestedRefreshRate);
            com.android.server.display.mode.Vote sizeVote = getSizeVote(requestedMode);
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 5, frameRateVote);
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 6, baseModeRefreshRateVote);
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, 7, sizeVote);
        }

        private android.view.Display.Mode findModeLocked(int displayId, int modeId, float requestedRefreshRate) {
            if (modeId != 0) {
                return findAppModeByIdLocked(displayId, modeId);
            }
            if (requestedRefreshRate == 0.0f || this.mIgnorePreferredRefreshRate) {
                return null;
            }
            android.view.Display.Mode mode = findDefaultModeByRefreshRateLocked(displayId, requestedRefreshRate);
            if (mode == null) {
                android.util.Slog.e(com.android.server.display.mode.DisplayModeDirector.TAG, "Couldn't find a mode for the requestedRefreshRate: " + requestedRefreshRate + " on Display: " + displayId);
                return mode;
            }
            return mode;
        }

        private com.android.server.display.mode.Vote getFrameRateVote(float minRefreshRate, float maxRefreshRate) {
            android.view.SurfaceControl.RefreshRateRange refreshRateRange = null;
            if (minRefreshRate > 0.0f || maxRefreshRate > 0.0f) {
                float max = maxRefreshRate > 0.0f ? maxRefreshRate : Float.POSITIVE_INFINITY;
                refreshRateRange = new android.view.SurfaceControl.RefreshRateRange(minRefreshRate, max);
                if (refreshRateRange.min == 0.0f && refreshRateRange.max == 0.0f) {
                    refreshRateRange = null;
                }
            }
            if (refreshRateRange != null) {
                return com.android.server.display.mode.Vote.forRenderFrameRates(refreshRateRange.min, refreshRateRange.max);
            }
            return null;
        }

        private com.android.server.display.mode.Vote getSizeVote(android.view.Display.Mode mode) {
            if (mode != null) {
                return com.android.server.display.mode.Vote.forSize(mode.getPhysicalWidth(), mode.getPhysicalHeight());
            }
            return null;
        }

        private com.android.server.display.mode.Vote getBaseModeVote(android.view.Display.Mode mode, float requestedRefreshRate) {
            if (mode != null) {
                if (mode.isSynthetic()) {
                    com.android.server.display.mode.Vote vote = com.android.server.display.mode.Vote.forRequestedRefreshRate(mode.getRefreshRate());
                    return vote;
                }
                com.android.server.display.mode.Vote vote2 = com.android.server.display.mode.Vote.forBaseModeRefreshRate(mode.getRefreshRate());
                return vote2;
            }
            if (requestedRefreshRate == 0.0f || !this.mIgnorePreferredRefreshRate) {
                return null;
            }
            com.android.server.display.mode.Vote vote3 = com.android.server.display.mode.Vote.forRequestedRefreshRate(requestedRefreshRate);
            return vote3;
        }

        private android.view.Display.Mode findDefaultModeByRefreshRateLocked(int displayId, float refreshRate) {
            android.view.Display.Mode[] modes = (android.view.Display.Mode[]) com.android.server.display.mode.DisplayModeDirector.this.mAppSupportedModesByDisplay.get(displayId);
            android.view.Display.Mode defaultMode = (android.view.Display.Mode) com.android.server.display.mode.DisplayModeDirector.this.mDefaultModeByDisplay.get(displayId);
            for (int i = 0; i < modes.length; i++) {
                if (modes[i].matches(defaultMode.getPhysicalWidth(), defaultMode.getPhysicalHeight(), refreshRate)) {
                    return modes[i];
                }
            }
            return null;
        }

        private android.view.Display.Mode findAppModeByIdLocked(int displayId, int modeId) {
            android.view.Display.Mode[] modes = (android.view.Display.Mode[]) com.android.server.display.mode.DisplayModeDirector.this.mAppSupportedModesByDisplay.get(displayId);
            if (modes == null) {
                return null;
            }
            for (android.view.Display.Mode mode : modes) {
                if (mode.getModeId() == modeId) {
                    return mode;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpLocked(java.io.PrintWriter pw) {
            pw.println("  AppRequestObserver");
            pw.println("    mIgnorePreferredRefreshRate: " + this.mIgnorePreferredRefreshRate);
        }
    }

    public final class DisplayObserver implements android.hardware.display.DisplayManager.DisplayListener {
        private final android.content.Context mContext;
        private int mExternalDisplayPeakHeight;
        private int mExternalDisplayPeakRefreshRate;
        private int mExternalDisplayPeakWidth;
        private final java.util.Set<java.lang.Integer> mExternalDisplaysConnected = new java.util.HashSet();
        private final android.os.Handler mHandler;
        private final boolean mRefreshRateSynchronizationEnabled;
        private final com.android.server.display.mode.VotesStorage mVotesStorage;

        DisplayObserver(android.content.Context context, android.os.Handler handler, com.android.server.display.mode.VotesStorage votesStorage, com.android.server.display.mode.DisplayModeDirector.Injector injector) {
            this.mContext = context;
            this.mHandler = handler;
            this.mVotesStorage = votesStorage;
            this.mExternalDisplayPeakRefreshRate = this.mContext.getResources().getInteger(android.R.integer.config_drawLockTimeoutMillis);
            this.mExternalDisplayPeakWidth = this.mContext.getResources().getInteger(android.R.integer.config_dreamCloseAnimationDuration);
            this.mExternalDisplayPeakHeight = this.mContext.getResources().getInteger(android.R.integer.config_dozeWakeLockScreenDebounce);
            this.mRefreshRateSynchronizationEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_offsetWallpaperToCenterOfLargestDisplay);
        }

        private boolean isExternalDisplayLimitModeEnabled() {
            return this.mExternalDisplayPeakWidth > 0 && this.mExternalDisplayPeakHeight > 0 && this.mExternalDisplayPeakRefreshRate > 0 && com.android.server.display.mode.DisplayModeDirector.this.mIsExternalDisplayLimitModeEnabled && com.android.server.display.mode.DisplayModeDirector.this.mIsDisplayResolutionRangeVotingEnabled && com.android.server.display.mode.DisplayModeDirector.this.mIsUserPreferredModeVoteEnabled;
        }

        private boolean isRefreshRateSynchronizationEnabled() {
            return this.mRefreshRateSynchronizationEnabled && com.android.server.display.mode.DisplayModeDirector.this.mIsDisplaysRefreshRatesSynchronizationEnabled;
        }

        public void observe() {
            com.android.server.display.mode.DisplayModeDirector.this.mInjector.registerDisplayListener(this, this.mHandler);
            android.util.SparseArray<android.view.Display.Mode[]> modes = new android.util.SparseArray<>();
            android.util.SparseArray<android.view.Display.Mode[]> appModes = new android.util.SparseArray<>();
            android.util.SparseArray<android.view.Display.Mode> defaultModes = new android.util.SparseArray<>();
            android.view.Display[] displays = com.android.server.display.mode.DisplayModeDirector.this.mInjector.getDisplays();
            for (android.view.Display d : displays) {
                int displayId = d.getDisplayId();
                android.view.DisplayInfo info = getDisplayInfo(displayId);
                modes.put(displayId, info.supportedModes);
                appModes.put(displayId, info.appsSupportedModes);
                defaultModes.put(displayId, info.getDefaultMode());
            }
            com.android.server.display.DisplayDeviceConfig defaultDisplayConfig = com.android.server.display.mode.DisplayModeDirector.this.mDisplayDeviceConfigProvider.getDisplayDeviceConfig(0);
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                int size = modes.size();
                for (int i = 0; i < size; i++) {
                    com.android.server.display.mode.DisplayModeDirector.this.mSupportedModesByDisplay.put(modes.keyAt(i), modes.valueAt(i));
                    com.android.server.display.mode.DisplayModeDirector.this.mAppSupportedModesByDisplay.put(appModes.keyAt(i), appModes.valueAt(i));
                    com.android.server.display.mode.DisplayModeDirector.this.mDefaultModeByDisplay.put(defaultModes.keyAt(i), defaultModes.valueAt(i));
                }
                com.android.server.display.mode.DisplayModeDirector.this.mDisplayDeviceConfigByDisplay.put(0, defaultDisplayConfig);
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
            updateDisplayDeviceConfig(displayId);
            android.view.DisplayInfo displayInfo = getDisplayInfo(displayId);
            updateDisplayModes(displayId, displayInfo);
            updateLayoutLimitedFrameRate(displayId, displayInfo);
            updateUserSettingDisplayPreferredSize(displayInfo);
            updateDisplaysPeakRefreshRateAndResolution(displayInfo);
            addDisplaysSynchronizedPeakRefreshRate(displayInfo);
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                com.android.server.display.mode.DisplayModeDirector.this.mSupportedModesByDisplay.remove(displayId);
                com.android.server.display.mode.DisplayModeDirector.this.mAppSupportedModesByDisplay.remove(displayId);
                com.android.server.display.mode.DisplayModeDirector.this.mDefaultModeByDisplay.remove(displayId);
                com.android.server.display.mode.DisplayModeDirector.this.mDisplayDeviceConfigByDisplay.remove(displayId);
                com.android.server.display.mode.DisplayModeDirector.this.mSettingsObserver.removeRefreshRateSetting(displayId);
            }
            updateLayoutLimitedFrameRate(displayId, null);
            removeUserSettingDisplayPreferredSize(displayId);
            removeDisplaysPeakRefreshRateAndResolution(displayId);
            removeDisplaysSynchronizedPeakRefreshRate(displayId);
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            updateDisplayDeviceConfig(displayId);
            android.view.DisplayInfo displayInfo = getDisplayInfo(displayId);
            updateDisplayModes(displayId, displayInfo);
            updateLayoutLimitedFrameRate(displayId, displayInfo);
            updateUserSettingDisplayPreferredSize(displayInfo);
        }

        boolean isExternalDisplayLocked(int displayId) {
            return this.mExternalDisplaysConnected.contains(java.lang.Integer.valueOf(displayId));
        }

        private android.view.DisplayInfo getDisplayInfo(int displayId) {
            android.view.DisplayInfo info = new android.view.DisplayInfo();
            if (com.android.server.display.mode.DisplayModeDirector.this.mInjector.getDisplayInfo(displayId, info)) {
                return info;
            }
            return null;
        }

        private void updateLayoutLimitedFrameRate(int displayId, android.view.DisplayInfo info) {
            com.android.server.display.mode.Vote vote;
            if (info != null && info.layoutLimitedRefreshRate != null) {
                vote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(info.layoutLimitedRefreshRate.min, info.layoutLimitedRefreshRate.max);
            } else {
                vote = null;
            }
            this.mVotesStorage.updateVote(displayId, 13, vote);
        }

        private void removeUserSettingDisplayPreferredSize(int displayId) {
            if (!com.android.server.display.mode.DisplayModeDirector.this.mIsUserPreferredModeVoteEnabled) {
                return;
            }
            this.mVotesStorage.updateVote(displayId, 4, null);
        }

        private void updateUserSettingDisplayPreferredSize(android.view.DisplayInfo info) {
            if (info == null || !com.android.server.display.mode.DisplayModeDirector.this.mIsUserPreferredModeVoteEnabled) {
                return;
            }
            android.view.Display.Mode preferredMode = findDisplayPreferredMode(info);
            if (preferredMode == null) {
                removeUserSettingDisplayPreferredSize(info.displayId);
            } else {
                this.mVotesStorage.updateVote(info.displayId, 4, com.android.server.display.mode.Vote.forSize(preferredMode.getPhysicalWidth(), preferredMode.getPhysicalHeight()));
            }
        }

        private android.view.Display.Mode findDisplayPreferredMode(android.view.DisplayInfo info) {
            if (info.userPreferredModeId == -1) {
                return null;
            }
            for (android.view.Display.Mode mode : info.supportedModes) {
                if (mode.getModeId() == info.userPreferredModeId) {
                    return mode;
                }
            }
            return null;
        }

        private void removeDisplaysPeakRefreshRateAndResolution(int displayId) {
            if (!isExternalDisplayLimitModeEnabled()) {
                return;
            }
            this.mVotesStorage.updateVote(displayId, 11, null);
        }

        private void updateDisplaysPeakRefreshRateAndResolution(android.view.DisplayInfo info) {
            if (info == null || info.type != 2 || !isExternalDisplayLimitModeEnabled()) {
                return;
            }
            this.mVotesStorage.updateVote(info.displayId, 11, com.android.server.display.mode.Vote.forSizeAndPhysicalRefreshRatesRange(0, 0, this.mExternalDisplayPeakWidth, this.mExternalDisplayPeakHeight, 0.0f, this.mExternalDisplayPeakRefreshRate));
        }

        private void addDisplaysSynchronizedPeakRefreshRate(android.view.DisplayInfo info) {
            if (info == null || info.type != 2 || !isRefreshRateSynchronizationEnabled()) {
                return;
            }
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                this.mExternalDisplaysConnected.add(java.lang.Integer.valueOf(info.displayId));
                if (this.mExternalDisplaysConnected.size() != 1) {
                    return;
                }
                this.mVotesStorage.updateGlobalVote(10, com.android.server.display.mode.Vote.forPhysicalRefreshRates(59.0f, 61.0f));
            }
        }

        private void removeDisplaysSynchronizedPeakRefreshRate(int displayId) {
            if (!isRefreshRateSynchronizationEnabled()) {
                return;
            }
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                if (isExternalDisplayLocked(displayId)) {
                    this.mExternalDisplaysConnected.remove(java.lang.Integer.valueOf(displayId));
                    if (this.mExternalDisplaysConnected.size() != 0) {
                        return;
                    }
                    this.mVotesStorage.updateGlobalVote(10, null);
                }
            }
        }

        private void updateDisplayDeviceConfig(int displayId) {
            com.android.server.display.DisplayDeviceConfig config = com.android.server.display.mode.DisplayModeDirector.this.mDisplayDeviceConfigProvider.getDisplayDeviceConfig(displayId);
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                com.android.server.display.mode.DisplayModeDirector.this.mDisplayDeviceConfigByDisplay.put(displayId, config);
            }
        }

        private void updateDisplayModes(int displayId, android.view.DisplayInfo info) {
            if (info == null) {
                return;
            }
            boolean changed = false;
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                if (!java.util.Arrays.equals((java.lang.Object[]) com.android.server.display.mode.DisplayModeDirector.this.mSupportedModesByDisplay.get(displayId), info.supportedModes)) {
                    com.android.server.display.mode.DisplayModeDirector.this.mSupportedModesByDisplay.put(displayId, info.supportedModes);
                    changed = true;
                }
                if (!java.util.Arrays.equals((java.lang.Object[]) com.android.server.display.mode.DisplayModeDirector.this.mAppSupportedModesByDisplay.get(displayId), info.appsSupportedModes)) {
                    com.android.server.display.mode.DisplayModeDirector.this.mAppSupportedModesByDisplay.put(displayId, info.appsSupportedModes);
                    changed = true;
                }
                if (!java.util.Objects.equals(com.android.server.display.mode.DisplayModeDirector.this.mDefaultModeByDisplay.get(displayId), info.getDefaultMode())) {
                    changed = true;
                    com.android.server.display.mode.DisplayModeDirector.this.mDefaultModeByDisplay.put(displayId, info.getDefaultMode());
                }
                if (changed) {
                    com.android.server.display.mode.DisplayModeDirector.this.lambda$start$0();
                    com.android.server.display.mode.DisplayModeDirector.this.mSettingsObserver.updateRefreshRateSettingLocked(displayId);
                }
            }
        }
    }

    public class BrightnessObserver implements android.hardware.display.DisplayManager.DisplayListener {
        private static final int LIGHT_SENSOR_RATE_MS = 250;
        private com.android.server.display.utils.AmbientFilter mAmbientFilter;
        private final android.content.Context mContext;
        private final android.os.Handler mHandler;
        private float[] mHighAmbientBrightnessThresholds;
        private float[] mHighDisplayBrightnessThresholds;
        private android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> mHighZoneRefreshRateForThermals;
        private android.view.SurfaceControl.IdleScreenRefreshRateConfig mIdleScreenRefreshRateConfig;
        private final com.android.server.display.mode.DisplayModeDirector.Injector mInjector;
        private android.hardware.Sensor mLightSensor;
        private java.lang.String mLightSensorName;
        private java.lang.String mLightSensorType;
        private boolean mLoggingEnabled;
        private float[] mLowAmbientBrightnessThresholds;
        private float[] mLowDisplayBrightnessThresholds;
        private android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> mLowZoneRefreshRateForThermals;
        private int mRefreshRateInHighZone;
        private int mRefreshRateInLowZone;
        private android.hardware.Sensor mRegisteredLightSensor;
        private android.hardware.SensorManager mSensorManager;
        private boolean mShouldObserveAmbientHighChange;
        private boolean mShouldObserveAmbientLowChange;
        private boolean mShouldObserveDisplayHighChange;
        private boolean mShouldObserveDisplayLowChange;
        private boolean mThermalRegistered;
        private final boolean mVsyncLowLightBlockingVoteEnabled;
        private final com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener mLightSensorListener = new com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener();
        private float mAmbientLux = -1.0f;
        private float mBrightness = Float.NaN;
        private final android.os.IThermalEventListener.Stub mThermalListener = new android.os.IThermalEventListener.Stub() { // from class: com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.1
            public void notifyThrottling(android.os.Temperature temp) {
                int currentStatus = temp.getStatus();
                synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                    if (com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mThermalStatus != currentStatus) {
                        com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mThermalStatus = currentStatus;
                    }
                    com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.onBrightnessChangedLocked();
                }
            }
        };
        private int mDefaultDisplayState = 0;
        private boolean mRefreshRateChangeable = false;
        private boolean mLowPowerModeEnabled = false;
        private int mThermalStatus = 0;

        BrightnessObserver(android.content.Context context, android.os.Handler handler, com.android.server.display.mode.DisplayModeDirector.Injector injector, com.android.server.display.feature.DisplayManagerFlags flags) {
            this.mContext = context;
            this.mHandler = handler;
            this.mInjector = injector;
            updateBlockingZoneThresholds(null, false);
            this.mRefreshRateInHighZone = context.getResources().getInteger(android.R.integer.config_dropboxLowPriorityBroadcastRateLimitPeriod);
            this.mVsyncLowLightBlockingVoteEnabled = flags.isVsyncLowLightVoteEnabled();
        }

        public void updateBlockingZoneThresholds(com.android.server.display.DisplayDeviceConfig displayDeviceConfig, boolean attemptReadFromFeatureParams) {
            loadLowBrightnessThresholds(displayDeviceConfig, attemptReadFromFeatureParams);
            loadHighBrightnessThresholds(displayDeviceConfig, attemptReadFromFeatureParams);
        }

        float[] getLowDisplayBrightnessThresholds() {
            return this.mLowDisplayBrightnessThresholds;
        }

        float[] getLowAmbientBrightnessThresholds() {
            return this.mLowAmbientBrightnessThresholds;
        }

        float[] getHighDisplayBrightnessThresholds() {
            return this.mHighDisplayBrightnessThresholds;
        }

        float[] getHighAmbientBrightnessThresholds() {
            return this.mHighAmbientBrightnessThresholds;
        }

        int getRefreshRateInHighZone() {
            return this.mRefreshRateInHighZone;
        }

        int getRefreshRateInLowZone() {
            return this.mRefreshRateInLowZone;
        }

        android.view.SurfaceControl.IdleScreenRefreshRateConfig getIdleScreenRefreshRateConfig() {
            return this.mIdleScreenRefreshRateConfig;
        }

        private void loadLowBrightnessThresholds(final com.android.server.display.DisplayDeviceConfig displayDeviceConfig, boolean attemptReadFromFeatureParams) {
            loadRefreshRateInHighZone(displayDeviceConfig, attemptReadFromFeatureParams);
            loadRefreshRateInLowZone(displayDeviceConfig, attemptReadFromFeatureParams);
            this.mLowDisplayBrightnessThresholds = loadBrightnessThresholds(new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda6
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return this.f$0.lambda$loadLowBrightnessThresholds$0();
                }
            }, new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda7
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return displayDeviceConfig.getLowDisplayBrightnessThresholds();
                }
            }, android.R.array.config_bg_current_drain_threshold_to_bg_restricted, displayDeviceConfig, attemptReadFromFeatureParams, new com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda2());
            this.mLowAmbientBrightnessThresholds = loadBrightnessThresholds(new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda8
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return this.f$0.lambda$loadLowBrightnessThresholds$2();
                }
            }, new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda9
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return displayDeviceConfig.getLowAmbientBrightnessThresholds();
                }
            }, android.R.array.config_ambientThresholdsOfPeakRefreshRate, displayDeviceConfig, attemptReadFromFeatureParams, new com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda5());
            if (this.mLowDisplayBrightnessThresholds.length != this.mLowAmbientBrightnessThresholds.length) {
                throw new java.lang.RuntimeException("display low brightness threshold array and ambient brightness threshold array have different length: displayBrightnessThresholds=" + java.util.Arrays.toString(this.mLowDisplayBrightnessThresholds) + ", ambientBrightnessThresholds=" + java.util.Arrays.toString(this.mLowAmbientBrightnessThresholds));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ float[] lambda$loadLowBrightnessThresholds$0() throws java.lang.Exception {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getLowDisplayBrightnessThresholds();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ float[] lambda$loadLowBrightnessThresholds$2() throws java.lang.Exception {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getLowAmbientBrightnessThresholds();
        }

        private void loadRefreshRateInLowZone(com.android.server.display.DisplayDeviceConfig displayDeviceConfig, boolean attemptReadFromFeatureParams) {
            int defaultLowBlockingZoneRefreshRate;
            int refreshRateInLowZone = -1;
            if (attemptReadFromFeatureParams) {
                try {
                    refreshRateInLowZone = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getRefreshRateInLowZone();
                } catch (java.lang.Exception e) {
                }
            }
            if (refreshRateInLowZone == -1) {
                if (displayDeviceConfig == null) {
                    defaultLowBlockingZoneRefreshRate = this.mContext.getResources().getInteger(android.R.integer.config_defaultNotificationLedOn);
                } else {
                    defaultLowBlockingZoneRefreshRate = displayDeviceConfig.getDefaultLowBlockingZoneRefreshRate();
                }
                refreshRateInLowZone = defaultLowBlockingZoneRefreshRate;
            }
            this.mLowZoneRefreshRateForThermals = displayDeviceConfig == null ? null : displayDeviceConfig.getLowBlockingZoneThermalMap();
            this.mRefreshRateInLowZone = refreshRateInLowZone;
        }

        private void loadRefreshRateInHighZone(com.android.server.display.DisplayDeviceConfig displayDeviceConfig, boolean attemptReadFromFeatureParams) {
            int defaultHighBlockingZoneRefreshRate;
            int refreshRateInHighZone = -1;
            if (attemptReadFromFeatureParams) {
                try {
                    refreshRateInHighZone = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getRefreshRateInHighZone();
                } catch (java.lang.Exception e) {
                }
            }
            if (refreshRateInHighZone == -1) {
                if (displayDeviceConfig == null) {
                    defaultHighBlockingZoneRefreshRate = this.mContext.getResources().getInteger(android.R.integer.config_dropboxLowPriorityBroadcastRateLimitPeriod);
                } else {
                    defaultHighBlockingZoneRefreshRate = displayDeviceConfig.getDefaultHighBlockingZoneRefreshRate();
                }
                refreshRateInHighZone = defaultHighBlockingZoneRefreshRate;
            }
            this.mHighZoneRefreshRateForThermals = displayDeviceConfig == null ? null : displayDeviceConfig.getHighBlockingZoneThermalMap();
            this.mRefreshRateInHighZone = refreshRateInHighZone;
        }

        private void loadHighBrightnessThresholds(final com.android.server.display.DisplayDeviceConfig displayDeviceConfig, boolean attemptReadFromFeatureParams) {
            this.mHighDisplayBrightnessThresholds = loadBrightnessThresholds(new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda10
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return this.f$0.lambda$loadHighBrightnessThresholds$4();
                }
            }, new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda11
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return displayDeviceConfig.getHighDisplayBrightnessThresholds();
                }
            }, android.R.array.config_hideWhenDisabled_packageNames, displayDeviceConfig, attemptReadFromFeatureParams, new com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda2());
            this.mHighAmbientBrightnessThresholds = loadBrightnessThresholds(new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda12
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return this.f$0.lambda$loadHighBrightnessThresholds$6();
                }
            }, new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda13
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return displayDeviceConfig.getHighAmbientBrightnessThresholds();
                }
            }, android.R.array.config_healthConnectRestoreKnownSigners, displayDeviceConfig, attemptReadFromFeatureParams, new com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda5());
            if (this.mHighDisplayBrightnessThresholds.length != this.mHighAmbientBrightnessThresholds.length) {
                throw new java.lang.RuntimeException("display high brightness threshold array and ambient brightness threshold array have different length: displayBrightnessThresholds=" + java.util.Arrays.toString(this.mHighDisplayBrightnessThresholds) + ", ambientBrightnessThresholds=" + java.util.Arrays.toString(this.mHighAmbientBrightnessThresholds));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ float[] lambda$loadHighBrightnessThresholds$4() throws java.lang.Exception {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getHighDisplayBrightnessThresholds();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ float[] lambda$loadHighBrightnessThresholds$6() throws java.lang.Exception {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getHighAmbientBrightnessThresholds();
        }

        private float[] loadBrightnessThresholds(java.util.concurrent.Callable<float[]> loadFromDeviceConfigDisplaySettingsCallable, java.util.concurrent.Callable<float[]> loadFromDisplayDeviceConfigCallable, int brightnessThresholdOfFixedRefreshRateKey, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, boolean attemptReadFromFeatureParams, java.util.function.Function<int[], float[]> conversion) throws java.lang.Exception {
            float[] fArrCall;
            float[] brightnessThresholds = null;
            if (attemptReadFromFeatureParams) {
                try {
                    brightnessThresholds = loadFromDeviceConfigDisplaySettingsCallable.call();
                } catch (java.lang.Exception e) {
                }
            }
            if (brightnessThresholds == null) {
                try {
                    if (displayDeviceConfig == null) {
                        fArrCall = conversion.apply(this.mContext.getResources().getIntArray(brightnessThresholdOfFixedRefreshRateKey));
                    } else {
                        fArrCall = loadFromDisplayDeviceConfigCallable.call();
                    }
                    brightnessThresholds = fArrCall;
                    return brightnessThresholds;
                } catch (java.lang.Exception e2) {
                    android.util.Slog.e(com.android.server.display.mode.DisplayModeDirector.TAG, "Unexpectedly failed to load display brightness threshold");
                    e2.printStackTrace();
                    return brightnessThresholds;
                }
            }
            return brightnessThresholds;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void observe(android.hardware.SensorManager sensorManager) {
            this.mSensorManager = sensorManager;
            this.mBrightness = getBrightness(0);
            float[] lowDisplayBrightnessThresholds = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getLowDisplayBrightnessThresholds();
            float[] lowAmbientBrightnessThresholds = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getLowAmbientBrightnessThresholds();
            if (lowDisplayBrightnessThresholds != null && lowAmbientBrightnessThresholds != null && lowDisplayBrightnessThresholds.length == lowAmbientBrightnessThresholds.length) {
                this.mLowDisplayBrightnessThresholds = lowDisplayBrightnessThresholds;
                this.mLowAmbientBrightnessThresholds = lowAmbientBrightnessThresholds;
            }
            float[] highDisplayBrightnessThresholds = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getHighDisplayBrightnessThresholds();
            float[] highAmbientBrightnessThresholds = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getHighAmbientBrightnessThresholds();
            if (highDisplayBrightnessThresholds != null && highAmbientBrightnessThresholds != null && highDisplayBrightnessThresholds.length == highAmbientBrightnessThresholds.length) {
                this.mHighDisplayBrightnessThresholds = highDisplayBrightnessThresholds;
                this.mHighAmbientBrightnessThresholds = highAmbientBrightnessThresholds;
            }
            int refreshRateInLowZone = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getRefreshRateInLowZone();
            if (refreshRateInLowZone != -1) {
                this.mRefreshRateInLowZone = refreshRateInLowZone;
            }
            int refreshRateInHighZone = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getRefreshRateInHighZone();
            if (refreshRateInHighZone != -1) {
                this.mRefreshRateInHighZone = refreshRateInHighZone;
            }
            restartObserver();
            com.android.server.display.mode.DisplayModeDirector.this.mDeviceConfigDisplaySettings.startListening();
            this.mInjector.registerDisplayListener(this, this.mHandler, 12L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLoggingEnabled(boolean loggingEnabled) {
            if (this.mLoggingEnabled == loggingEnabled) {
                return;
            }
            this.mLoggingEnabled = loggingEnabled;
            this.mLightSensorListener.setLoggingEnabled(loggingEnabled);
        }

        public void onRefreshRateSettingChangedLocked(float min, float max) {
            boolean changeable = max - min > 1.0f && max > 60.0f;
            if (this.mRefreshRateChangeable != changeable) {
                this.mRefreshRateChangeable = changeable;
                updateSensorStatus();
                if (!changeable) {
                    removeFlickerRefreshRateVotes();
                }
            }
        }

        void onLowPowerModeEnabledLocked(boolean enabled) {
            if (this.mLowPowerModeEnabled != enabled) {
                this.mLowPowerModeEnabled = enabled;
                updateSensorStatus();
                if (enabled) {
                    removeFlickerRefreshRateVotes();
                }
            }
        }

        private void removeFlickerRefreshRateVotes() {
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateGlobalVote(1, null);
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateGlobalVote(17, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDeviceConfigLowBrightnessThresholdsChanged(float[] displayThresholds, float[] ambientThresholds) {
            final com.android.server.display.DisplayDeviceConfig displayDeviceConfig;
            if (displayThresholds != null && ambientThresholds != null && displayThresholds.length == ambientThresholds.length) {
                this.mLowDisplayBrightnessThresholds = displayThresholds;
                this.mLowAmbientBrightnessThresholds = ambientThresholds;
            } else {
                synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                    displayDeviceConfig = com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig;
                }
                this.mLowDisplayBrightnessThresholds = loadBrightnessThresholds(new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda14
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        return this.f$0.lambda$onDeviceConfigLowBrightnessThresholdsChanged$8();
                    }
                }, new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda15
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        return displayDeviceConfig.getLowDisplayBrightnessThresholds();
                    }
                }, android.R.array.config_bg_current_drain_threshold_to_bg_restricted, displayDeviceConfig, false, new com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda2());
                this.mLowAmbientBrightnessThresholds = loadBrightnessThresholds(new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda16
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        return this.f$0.lambda$onDeviceConfigLowBrightnessThresholdsChanged$10();
                    }
                }, new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda17
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        return displayDeviceConfig.getLowAmbientBrightnessThresholds();
                    }
                }, android.R.array.config_ambientThresholdsOfPeakRefreshRate, displayDeviceConfig, false, new com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda5());
            }
            restartObserver();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ float[] lambda$onDeviceConfigLowBrightnessThresholdsChanged$8() throws java.lang.Exception {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getLowDisplayBrightnessThresholds();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ float[] lambda$onDeviceConfigLowBrightnessThresholdsChanged$10() throws java.lang.Exception {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getLowAmbientBrightnessThresholds();
        }

        public void onDeviceConfigRefreshRateInLowZoneChanged(int refreshRate) {
            if (refreshRate == -1) {
                synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                    loadRefreshRateInLowZone(com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig, false);
                }
                restartObserver();
                return;
            }
            if (refreshRate != this.mRefreshRateInLowZone) {
                this.mRefreshRateInLowZone = refreshRate;
                restartObserver();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDeviceConfigHighBrightnessThresholdsChanged(float[] displayThresholds, float[] ambientThresholds) {
            final com.android.server.display.DisplayDeviceConfig displayDeviceConfig;
            if (displayThresholds != null && ambientThresholds != null && displayThresholds.length == ambientThresholds.length) {
                this.mHighDisplayBrightnessThresholds = displayThresholds;
                this.mHighAmbientBrightnessThresholds = ambientThresholds;
            } else {
                synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                    displayDeviceConfig = com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig;
                }
                this.mHighDisplayBrightnessThresholds = loadBrightnessThresholds(new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda0
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        return this.f$0.lambda$onDeviceConfigHighBrightnessThresholdsChanged$12();
                    }
                }, new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda1
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        return displayDeviceConfig.getHighDisplayBrightnessThresholds();
                    }
                }, android.R.array.config_hideWhenDisabled_packageNames, displayDeviceConfig, false, new com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda2());
                this.mHighAmbientBrightnessThresholds = loadBrightnessThresholds(new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda3
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        return this.f$0.lambda$onDeviceConfigHighBrightnessThresholdsChanged$14();
                    }
                }, new java.util.concurrent.Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda4
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        return displayDeviceConfig.getHighAmbientBrightnessThresholds();
                    }
                }, android.R.array.config_healthConnectRestoreKnownSigners, displayDeviceConfig, false, new com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda5());
            }
            restartObserver();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ float[] lambda$onDeviceConfigHighBrightnessThresholdsChanged$12() throws java.lang.Exception {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getLowDisplayBrightnessThresholds();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ float[] lambda$onDeviceConfigHighBrightnessThresholdsChanged$14() throws java.lang.Exception {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getHighAmbientBrightnessThresholds();
        }

        public void onDeviceConfigRefreshRateInHighZoneChanged(int refreshRate) {
            if (refreshRate == -1) {
                synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                    loadRefreshRateInHighZone(com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig, false);
                }
                restartObserver();
                return;
            }
            if (refreshRate != this.mRefreshRateInHighZone) {
                this.mRefreshRateInHighZone = refreshRate;
                restartObserver();
            }
        }

        void dumpLocked(java.io.PrintWriter pw) {
            pw.println("  BrightnessObserver");
            pw.println("    mAmbientLux: " + this.mAmbientLux);
            pw.println("    mBrightness: " + this.mBrightness);
            pw.println("    mDefaultDisplayState: " + this.mDefaultDisplayState);
            pw.println("    mLowPowerModeEnabled: " + this.mLowPowerModeEnabled);
            pw.println("    mRefreshRateChangeable: " + this.mRefreshRateChangeable);
            pw.println("    mShouldObserveDisplayLowChange: " + this.mShouldObserveDisplayLowChange);
            pw.println("    mShouldObserveAmbientLowChange: " + this.mShouldObserveAmbientLowChange);
            pw.println("    mRefreshRateInLowZone: " + this.mRefreshRateInLowZone);
            for (float d : this.mLowDisplayBrightnessThresholds) {
                pw.println("    mDisplayLowBrightnessThreshold: " + d);
            }
            for (float d2 : this.mLowAmbientBrightnessThresholds) {
                pw.println("    mAmbientLowBrightnessThreshold: " + d2);
            }
            pw.println("    mShouldObserveDisplayHighChange: " + this.mShouldObserveDisplayHighChange);
            pw.println("    mShouldObserveAmbientHighChange: " + this.mShouldObserveAmbientHighChange);
            pw.println("    mRefreshRateInHighZone: " + this.mRefreshRateInHighZone);
            for (float d3 : this.mHighDisplayBrightnessThresholds) {
                pw.println("    mDisplayHighBrightnessThresholds: " + d3);
            }
            for (float d4 : this.mHighAmbientBrightnessThresholds) {
                pw.println("    mAmbientHighBrightnessThresholds: " + d4);
            }
            pw.println("    mRegisteredLightSensor: " + this.mRegisteredLightSensor);
            pw.println("    mLightSensor: " + this.mLightSensor);
            pw.println("    mLightSensorName: " + this.mLightSensorName);
            pw.println("    mLightSensorType: " + this.mLightSensorType);
            this.mLightSensorListener.dumpLocked(pw);
            if (this.mAmbientFilter != null) {
                this.mAmbientFilter.dump(new android.util.IndentingPrintWriter(pw, "    "));
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            if (displayId == 0) {
                updateDefaultDisplayState();
                float brightness = getBrightness(displayId);
                synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                    if (!com.android.internal.display.BrightnessSynchronizer.floatEquals(brightness, this.mBrightness)) {
                        this.mBrightness = brightness;
                        onBrightnessChangedLocked();
                    }
                }
            }
        }

        private boolean hasLowLightVrrConfig() {
            com.android.server.display.DisplayDeviceConfig config;
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                config = com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig;
            }
            return this.mVsyncLowLightBlockingVoteEnabled && config != null && config.isVrrSupportEnabled() && !config.getRefreshRateData().lowLightBlockingZoneSupportedModes.isEmpty();
        }

        private void restartObserver() {
            if (this.mRefreshRateInLowZone > 0 || hasLowLightVrrConfig()) {
                this.mShouldObserveDisplayLowChange = hasValidThreshold(this.mLowDisplayBrightnessThresholds);
                this.mShouldObserveAmbientLowChange = hasValidThreshold(this.mLowAmbientBrightnessThresholds);
            } else {
                this.mShouldObserveDisplayLowChange = false;
                this.mShouldObserveAmbientLowChange = false;
            }
            if (this.mRefreshRateInHighZone > 0) {
                this.mShouldObserveDisplayHighChange = hasValidThreshold(this.mHighDisplayBrightnessThresholds);
                this.mShouldObserveAmbientHighChange = hasValidThreshold(this.mHighAmbientBrightnessThresholds);
            } else {
                this.mShouldObserveDisplayHighChange = false;
                this.mShouldObserveAmbientHighChange = false;
            }
            if (this.mShouldObserveAmbientLowChange || this.mShouldObserveAmbientHighChange) {
                android.hardware.Sensor lightSensor = getLightSensor();
                if (lightSensor != null && lightSensor != this.mLightSensor) {
                    android.content.res.Resources res = this.mContext.getResources();
                    this.mAmbientFilter = com.android.server.display.utils.AmbientFilterFactory.createBrightnessFilter(com.android.server.display.mode.DisplayModeDirector.TAG, res);
                    this.mLightSensor = lightSensor;
                }
            } else {
                this.mAmbientFilter = null;
                this.mLightSensor = null;
            }
            updateSensorStatus();
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                onBrightnessChangedLocked();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reloadLightSensor(com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
            reloadLightSensorData(displayDeviceConfig);
            restartObserver();
        }

        private void reloadLightSensorData(com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
            if (displayDeviceConfig != null && displayDeviceConfig.getAmbientLightSensor() != null) {
                this.mLightSensorType = displayDeviceConfig.getAmbientLightSensor().type;
                this.mLightSensorName = displayDeviceConfig.getAmbientLightSensor().name;
            } else if (this.mLightSensorName == null && this.mLightSensorType == null) {
                android.content.res.Resources resources = this.mContext.getResources();
                this.mLightSensorType = resources.getString(android.R.string.config_emergency_dialer_package);
                this.mLightSensorName = "";
            }
        }

        private android.hardware.Sensor getLightSensor() {
            return com.android.server.display.utils.SensorUtils.findSensor(this.mSensorManager, this.mLightSensorType, this.mLightSensorName, 5);
        }

        private boolean hasValidThreshold(float[] a) {
            for (float d : a) {
                if (d >= 0.0f) {
                    return true;
                }
            }
            return false;
        }

        private boolean isInsideLowZone(float brightness, float lux) {
            for (int i = 0; i < this.mLowDisplayBrightnessThresholds.length; i++) {
                float disp = this.mLowDisplayBrightnessThresholds[i];
                float ambi = this.mLowAmbientBrightnessThresholds[i];
                if (disp >= 0.0f && ambi >= 0.0f) {
                    if (brightness <= disp && lux <= ambi) {
                        return true;
                    }
                } else if (disp >= 0.0f) {
                    if (brightness <= disp) {
                        return true;
                    }
                } else if (ambi >= 0.0f && lux <= ambi) {
                    return true;
                }
            }
            return false;
        }

        private boolean isInsideHighZone(float brightness, float lux) {
            for (int i = 0; i < this.mHighDisplayBrightnessThresholds.length; i++) {
                float disp = this.mHighDisplayBrightnessThresholds[i];
                float ambi = this.mHighAmbientBrightnessThresholds[i];
                if (disp >= 0.0f && ambi >= 0.0f) {
                    if (brightness >= disp && lux >= ambi) {
                        return true;
                    }
                } else if (disp >= 0.0f) {
                    if (brightness >= disp) {
                        return true;
                    }
                } else if (ambi >= 0.0f && lux >= ambi) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onBrightnessChangedLocked() {
            android.view.SurfaceControl.RefreshRateRange range;
            android.view.SurfaceControl.RefreshRateRange range2;
            if (!this.mRefreshRateChangeable || this.mLowPowerModeEnabled) {
                return;
            }
            com.android.server.display.mode.Vote refreshRateVote = null;
            com.android.server.display.mode.Vote refreshRateSwitchingVote = null;
            if (java.lang.Float.isNaN(this.mBrightness)) {
                return;
            }
            boolean insideHighZone = false;
            boolean insideLowZone = hasValidLowZone() && isInsideLowZone(this.mBrightness, this.mAmbientLux);
            if (insideLowZone) {
                if (hasLowLightVrrConfig()) {
                    refreshRateVote = com.android.server.display.mode.Vote.forSupportedRefreshRates(com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig.getRefreshRateData().lowLightBlockingZoneSupportedModes);
                } else {
                    refreshRateVote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(this.mRefreshRateInLowZone, this.mRefreshRateInLowZone);
                    refreshRateSwitchingVote = com.android.server.display.mode.Vote.forDisableRefreshRateSwitching();
                }
                if (this.mLowZoneRefreshRateForThermals != null && (range2 = com.android.server.display.mode.SkinThermalStatusObserver.findBestMatchingRefreshRateRange(this.mThermalStatus, this.mLowZoneRefreshRateForThermals)) != null) {
                    refreshRateVote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(range2.min, range2.max);
                }
            }
            if (hasValidHighZone() && isInsideHighZone(this.mBrightness, this.mAmbientLux)) {
                insideHighZone = true;
            }
            if (insideHighZone) {
                refreshRateVote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(this.mRefreshRateInHighZone, this.mRefreshRateInHighZone);
                if (this.mHighZoneRefreshRateForThermals != null && (range = com.android.server.display.mode.SkinThermalStatusObserver.findBestMatchingRefreshRateRange(this.mThermalStatus, this.mHighZoneRefreshRateForThermals)) != null) {
                    refreshRateVote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(range.min, range.max);
                }
                refreshRateSwitchingVote = com.android.server.display.mode.Vote.forDisableRefreshRateSwitching();
            }
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.mode.DisplayModeDirector.TAG, "Display brightness " + this.mBrightness + ", ambient lux " + this.mAmbientLux + ", Vote " + refreshRateVote);
            }
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateGlobalVote(1, refreshRateVote);
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateGlobalVote(17, refreshRateSwitchingVote);
        }

        private boolean hasValidLowZone() {
            return (this.mRefreshRateInLowZone > 0 || hasLowLightVrrConfig()) && (this.mShouldObserveDisplayLowChange || this.mShouldObserveAmbientLowChange);
        }

        private boolean hasValidHighZone() {
            return this.mRefreshRateInHighZone > 0 && (this.mShouldObserveDisplayHighChange || this.mShouldObserveAmbientHighChange);
        }

        private void updateDefaultDisplayState() {
            android.view.Display display = this.mInjector.getDisplay(0);
            if (display == null) {
                return;
            }
            setDefaultDisplayState(display.getState());
        }

        void setDefaultDisplayState(int state) {
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.mode.DisplayModeDirector.TAG, "setDefaultDisplayState: mDefaultDisplayState = " + this.mDefaultDisplayState + ", state = " + state);
            }
            if (this.mDefaultDisplayState != state) {
                this.mDefaultDisplayState = state;
                updateSensorStatus();
            }
        }

        private void updateSensorStatus() {
            if (this.mSensorManager == null || this.mLightSensorListener == null) {
                return;
            }
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.mode.DisplayModeDirector.TAG, "updateSensorStatus: mShouldObserveAmbientLowChange = " + this.mShouldObserveAmbientLowChange + ", mShouldObserveAmbientHighChange = " + this.mShouldObserveAmbientHighChange);
                android.util.Slog.d(com.android.server.display.mode.DisplayModeDirector.TAG, "updateSensorStatus: mLowPowerModeEnabled = " + this.mLowPowerModeEnabled + ", mRefreshRateChangeable = " + this.mRefreshRateChangeable);
            }
            boolean registerForThermals = false;
            if ((this.mShouldObserveAmbientLowChange || this.mShouldObserveAmbientHighChange) && isDeviceActive() && !this.mLowPowerModeEnabled && this.mRefreshRateChangeable) {
                registerLightSensor();
                registerForThermals = (this.mLowZoneRefreshRateForThermals == null && this.mHighZoneRefreshRateForThermals == null) ? false : true;
            } else {
                unregisterSensorListener();
            }
            if (registerForThermals && !this.mThermalRegistered) {
                this.mThermalRegistered = this.mInjector.registerThermalServiceListener(this.mThermalListener);
                return;
            }
            if (!registerForThermals && this.mThermalRegistered) {
                this.mInjector.unregisterThermalServiceListener(this.mThermalListener);
                this.mThermalRegistered = false;
                synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                    this.mThermalStatus = 0;
                }
            }
        }

        private void registerLightSensor() {
            if (this.mRegisteredLightSensor == this.mLightSensor) {
                return;
            }
            if (this.mRegisteredLightSensor != null) {
                unregisterSensorListener();
            }
            this.mSensorManager.registerListener(this.mLightSensorListener, this.mLightSensor, 250000, this.mHandler);
            this.mRegisteredLightSensor = this.mLightSensor;
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.mode.DisplayModeDirector.TAG, "updateSensorStatus: registerListener");
            }
        }

        private void unregisterSensorListener() {
            this.mLightSensorListener.removeCallbacks();
            this.mSensorManager.unregisterListener(this.mLightSensorListener);
            this.mRegisteredLightSensor = null;
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.mode.DisplayModeDirector.TAG, "updateSensorStatus: unregisterListener");
            }
        }

        private boolean isDeviceActive() {
            return this.mDefaultDisplayState == 2;
        }

        private float getBrightness(int displayId) {
            android.hardware.display.BrightnessInfo info = this.mInjector.getBrightnessInfo(displayId);
            if (info != null) {
                return info.adjustedBrightness;
            }
            return Float.NaN;
        }

        private final class LightSensorEventListener implements android.hardware.SensorEventListener {
            private static final int INJECT_EVENTS_INTERVAL_MS = 250;
            private final java.lang.Runnable mInjectSensorEventRunnable;
            private float mLastSensorData;
            private boolean mLoggingEnabled;
            private long mTimestamp;

            private LightSensorEventListener() {
                this.mInjectSensorEventRunnable = new java.lang.Runnable() { // from class: com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener.1
                    @Override // java.lang.Runnable
                    public void run() {
                        long now = android.os.SystemClock.uptimeMillis();
                        com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener.this.processSensorData(now);
                        if (com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener.this.isDifferentZone(com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener.this.mLastSensorData, com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux, com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mLowAmbientBrightnessThresholds) || com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener.this.isDifferentZone(com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener.this.mLastSensorData, com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux, com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mHighAmbientBrightnessThresholds)) {
                            com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mHandler.postDelayed(com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener.this.mInjectSensorEventRunnable, 250L);
                        }
                    }
                };
            }

            public void dumpLocked(java.io.PrintWriter pw) {
                pw.println("    mLastSensorData: " + this.mLastSensorData);
                pw.println("    mTimestamp: " + formatTimestamp(this.mTimestamp));
            }

            public void setLoggingEnabled(boolean loggingEnabled) {
                if (this.mLoggingEnabled == loggingEnabled) {
                    return;
                }
                this.mLoggingEnabled = loggingEnabled;
            }

            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(android.hardware.SensorEvent event) {
                this.mLastSensorData = event.values[0];
                if (this.mLoggingEnabled) {
                    android.util.Slog.d(com.android.server.display.mode.DisplayModeDirector.TAG, "On sensor changed: " + this.mLastSensorData);
                }
                boolean lowZoneChanged = isDifferentZone(this.mLastSensorData, com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux, com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mLowAmbientBrightnessThresholds);
                boolean highZoneChanged = isDifferentZone(this.mLastSensorData, com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux, com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mHighAmbientBrightnessThresholds);
                if (((lowZoneChanged && this.mLastSensorData < com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux) || (highZoneChanged && this.mLastSensorData > com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux)) && com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientFilter != null) {
                    com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientFilter.clear();
                }
                long now = android.os.SystemClock.uptimeMillis();
                this.mTimestamp = java.lang.System.currentTimeMillis();
                if (com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientFilter != null) {
                    com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientFilter.addValue(now, this.mLastSensorData);
                }
                com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mHandler.removeCallbacks(this.mInjectSensorEventRunnable);
                processSensorData(now);
                if ((lowZoneChanged && this.mLastSensorData > com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux) || (highZoneChanged && this.mLastSensorData < com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux)) {
                    com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mHandler.postDelayed(this.mInjectSensorEventRunnable, 250L);
                }
                if (com.android.server.display.mode.DisplayModeDirector.this.mDisplayManagerFlags.isIdleScreenRefreshRateTimeoutEnabled()) {
                    com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.updateIdleScreenRefreshRate(com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux);
                }
            }

            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
            }

            public void removeCallbacks() {
                com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mHandler.removeCallbacks(this.mInjectSensorEventRunnable);
            }

            private java.lang.String formatTimestamp(long time) {
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.US);
                return dateFormat.format(new java.util.Date(time));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void processSensorData(long now) {
                if (com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientFilter != null) {
                    com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux = com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientFilter.getEstimate(now);
                } else {
                    com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.mAmbientLux = this.mLastSensorData;
                }
                synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                    com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.this.onBrightnessChangedLocked();
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public boolean isDifferentZone(float lux1, float lux2, float[] luxThresholds) {
                for (float boundary : luxThresholds) {
                    if (lux1 <= boundary && lux2 > boundary) {
                        return true;
                    }
                    if (lux1 > boundary && lux2 <= boundary) {
                        return true;
                    }
                }
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateIdleScreenRefreshRate(float ambientLux) {
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                if (com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig != null && !com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig.getIdleScreenRefreshRateTimeoutLuxThresholdPoint().isEmpty()) {
                    java.util.List<com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholdPoint> idleScreenRefreshRateTimeoutLuxThresholdPoints = com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig.getIdleScreenRefreshRateTimeoutLuxThresholdPoint();
                    int newTimeout = -1;
                    for (com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholdPoint point : idleScreenRefreshRateTimeoutLuxThresholdPoints) {
                        int newLux = point.getLux().intValue();
                        if (newLux <= ambientLux) {
                            newTimeout = point.getTimeout().intValue();
                        }
                    }
                    if (this.mIdleScreenRefreshRateConfig == null || newTimeout != this.mIdleScreenRefreshRateConfig.timeoutMillis) {
                        this.mIdleScreenRefreshRateConfig = new android.view.SurfaceControl.IdleScreenRefreshRateConfig(newTimeout);
                        synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                            com.android.server.display.mode.DisplayModeDirector.this.lambda$start$0();
                        }
                        return;
                    }
                    return;
                }
                this.mIdleScreenRefreshRateConfig = null;
            }
        }
    }

    private class UdfpsObserver extends android.hardware.fingerprint.IUdfpsRefreshRateRequestCallback.Stub {
        private final android.util.SparseBooleanArray mAuthenticationPossible;
        private final android.util.SparseBooleanArray mUdfpsRefreshRateEnabled;

        private UdfpsObserver() {
            this.mUdfpsRefreshRateEnabled = new android.util.SparseBooleanArray();
            this.mAuthenticationPossible = new android.util.SparseBooleanArray();
        }

        public void observe() {
            com.android.server.statusbar.StatusBarManagerInternal statusBar = com.android.server.display.mode.DisplayModeDirector.this.mInjector.getStatusBarManagerInternal();
            if (statusBar == null) {
                return;
            }
            boolean ignoreUdfpsVote = com.android.server.display.mode.DisplayModeDirector.this.mContext.getResources().getBoolean(android.R.bool.config_forceWindowDrawsStatusBarBackground);
            if (!ignoreUdfpsVote) {
                statusBar.setUdfpsRefreshRateCallback(this);
            }
        }

        public void onRequestEnabled(int displayId) {
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                this.mUdfpsRefreshRateEnabled.put(displayId, true);
                updateVoteLocked(displayId, true, 20);
            }
        }

        public void onRequestDisabled(int displayId) {
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                this.mUdfpsRefreshRateEnabled.put(displayId, false);
                updateVoteLocked(displayId, false, 20);
            }
        }

        public void onAuthenticationPossible(int displayId, boolean isPossible) {
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                this.mAuthenticationPossible.put(displayId, isPossible);
                updateVoteLocked(displayId, isPossible, 12);
            }
        }

        private void updateVoteLocked(int displayId, boolean enabled, int votePriority) {
            com.android.server.display.mode.Vote vote;
            if (enabled) {
                float maxRefreshRate = com.android.server.display.mode.DisplayModeDirector.this.getMaxRefreshRateLocked(displayId);
                vote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(maxRefreshRate, maxRefreshRate);
            } else {
                vote = null;
            }
            com.android.server.display.mode.DisplayModeDirector.this.mVotesStorage.updateVote(displayId, votePriority, vote);
        }

        void dumpLocked(java.io.PrintWriter pw) {
            pw.println("  UdfpsObserver");
            pw.println("    mUdfpsRefreshRateEnabled: ");
            for (int i = 0; i < this.mUdfpsRefreshRateEnabled.size(); i++) {
                int displayId = this.mUdfpsRefreshRateEnabled.keyAt(i);
                java.lang.String enabled = this.mUdfpsRefreshRateEnabled.valueAt(i) ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED;
                pw.println("      Display " + displayId + ": " + enabled);
            }
            pw.println("    mAuthenticationPossible: ");
            for (int i2 = 0; i2 < this.mAuthenticationPossible.size(); i2++) {
                int displayId2 = this.mAuthenticationPossible.keyAt(i2);
                java.lang.String isPossible = this.mAuthenticationPossible.valueAt(i2) ? "possible" : "impossible";
                pw.println("      Display " + displayId2 + ": " + isPossible);
            }
        }
    }

    public class HbmObserver implements android.hardware.display.DisplayManager.DisplayListener {
        private final com.android.server.display.mode.DisplayModeDirector.DeviceConfigDisplaySettings mDeviceConfigDisplaySettings;
        private android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
        private final android.os.Handler mHandler;
        private final com.android.server.display.mode.DisplayModeDirector.Injector mInjector;
        private int mRefreshRateInHbmHdr;
        private int mRefreshRateInHbmSunlight;
        private final com.android.server.display.mode.VotesStorage mVotesStorage;
        private final android.util.SparseIntArray mHbmMode = new android.util.SparseIntArray();
        private final android.util.SparseBooleanArray mHbmActive = new android.util.SparseBooleanArray();

        HbmObserver(com.android.server.display.mode.DisplayModeDirector.Injector injector, com.android.server.display.mode.VotesStorage votesStorage, android.os.Handler handler, com.android.server.display.mode.DisplayModeDirector.DeviceConfigDisplaySettings displaySettings) {
            this.mInjector = injector;
            this.mVotesStorage = votesStorage;
            this.mHandler = handler;
            this.mDeviceConfigDisplaySettings = displaySettings;
        }

        public void setupHdrRefreshRates(com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
            this.mRefreshRateInHbmHdr = this.mDeviceConfigDisplaySettings.getRefreshRateInHbmHdr(displayDeviceConfig);
            this.mRefreshRateInHbmSunlight = this.mDeviceConfigDisplaySettings.getRefreshRateInHbmSunlight(displayDeviceConfig);
        }

        public void observe() {
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                setupHdrRefreshRates(com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig);
            }
            this.mDisplayManagerInternal = this.mInjector.getDisplayManagerInternal();
            this.mInjector.registerDisplayListener(this, this.mHandler, 10L);
        }

        int getRefreshRateInHbmSunlight() {
            return this.mRefreshRateInHbmSunlight;
        }

        int getRefreshRateInHbmHdr() {
            return this.mRefreshRateInHbmHdr;
        }

        public void onDeviceConfigRefreshRateInHbmSunlightChanged(int refreshRate) {
            if (refreshRate != this.mRefreshRateInHbmSunlight) {
                this.mRefreshRateInHbmSunlight = refreshRate;
                onDeviceConfigRefreshRateInHbmChanged();
            }
        }

        public void onDeviceConfigRefreshRateInHbmHdrChanged(int refreshRate) {
            if (refreshRate != this.mRefreshRateInHbmHdr) {
                this.mRefreshRateInHbmHdr = refreshRate;
                onDeviceConfigRefreshRateInHbmChanged();
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
            this.mVotesStorage.updateVote(displayId, 2, null);
            this.mHbmMode.delete(displayId);
            this.mHbmActive.delete(displayId);
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            android.hardware.display.BrightnessInfo info = this.mInjector.getBrightnessInfo(displayId);
            if (info == null) {
                return;
            }
            int hbmMode = info.highBrightnessMode;
            boolean isHbmActive = hbmMode != 0 && info.adjustedBrightness > info.highBrightnessTransitionPoint;
            if (hbmMode == this.mHbmMode.get(displayId) && isHbmActive == this.mHbmActive.get(displayId)) {
                return;
            }
            this.mHbmMode.put(displayId, hbmMode);
            this.mHbmActive.put(displayId, isHbmActive);
            recalculateVotesForDisplay(displayId);
        }

        private void onDeviceConfigRefreshRateInHbmChanged() {
            int[] displayIds = this.mHbmMode.copyKeys();
            if (displayIds != null) {
                for (int id : displayIds) {
                    recalculateVotesForDisplay(id);
                }
            }
        }

        private void recalculateVotesForDisplay(int displayId) {
            com.android.server.display.mode.Vote vote = null;
            if (this.mHbmActive.get(displayId, false)) {
                int hbmMode = this.mHbmMode.get(displayId, 0);
                if (hbmMode == 1) {
                    if (this.mRefreshRateInHbmSunlight > 0) {
                        vote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(this.mRefreshRateInHbmSunlight, this.mRefreshRateInHbmSunlight);
                    } else {
                        java.util.List<android.hardware.display.DisplayManagerInternal.RefreshRateLimitation> limits = this.mDisplayManagerInternal.getRefreshRateLimitations(displayId);
                        int i = 0;
                        while (true) {
                            if (limits == null || i >= limits.size()) {
                                break;
                            }
                            android.hardware.display.DisplayManagerInternal.RefreshRateLimitation limitation = limits.get(i);
                            if (limitation.type != 1) {
                                i++;
                            } else {
                                vote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(limitation.range.min, limitation.range.max);
                                break;
                            }
                        }
                    }
                } else if (hbmMode == 2 && this.mRefreshRateInHbmHdr > 0) {
                    vote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(this.mRefreshRateInHbmHdr, this.mRefreshRateInHbmHdr);
                } else {
                    android.util.Slog.w(com.android.server.display.mode.DisplayModeDirector.TAG, "Unexpected HBM mode " + hbmMode + " for display ID " + displayId);
                }
            }
            this.mVotesStorage.updateVote(displayId, 2, vote);
        }

        void dumpLocked(java.io.PrintWriter pw) {
            pw.println("   HbmObserver");
            pw.println("     mHbmMode: " + this.mHbmMode);
            pw.println("     mHbmActive: " + this.mHbmActive);
            pw.println("     mRefreshRateInHbmSunlight: " + this.mRefreshRateInHbmSunlight);
            pw.println("     mRefreshRateInHbmHdr: " + this.mRefreshRateInHbmHdr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DeviceConfigDisplaySettings implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private DeviceConfigDisplaySettings() {
        }

        public void startListening() {
            com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.addOnPropertiesChangedListener(com.android.internal.os.BackgroundThread.getExecutor(), this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getRefreshRateInHbmHdr(final com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
            return getRefreshRate(new java.util.function.IntSupplier() { // from class: com.android.server.display.mode.DisplayModeDirector$DeviceConfigDisplaySettings$$ExternalSyntheticLambda0
                @Override // java.util.function.IntSupplier
                public final int getAsInt() {
                    return this.f$0.lambda$getRefreshRateInHbmHdr$0();
                }
            }, new java.util.function.IntSupplier() { // from class: com.android.server.display.mode.DisplayModeDirector$DeviceConfigDisplaySettings$$ExternalSyntheticLambda1
                @Override // java.util.function.IntSupplier
                public final int getAsInt() {
                    return displayDeviceConfig.getRefreshRateData().defaultRefreshRateInHbmHdr;
                }
            }, android.R.integer.config_defaultNightMode, displayDeviceConfig);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int lambda$getRefreshRateInHbmHdr$0() {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getRefreshRateInHbmHdr();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getRefreshRateInHbmSunlight(final com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
            return getRefreshRate(new java.util.function.IntSupplier() { // from class: com.android.server.display.mode.DisplayModeDirector$DeviceConfigDisplaySettings$$ExternalSyntheticLambda2
                @Override // java.util.function.IntSupplier
                public final int getAsInt() {
                    return this.f$0.lambda$getRefreshRateInHbmSunlight$2();
                }
            }, new java.util.function.IntSupplier() { // from class: com.android.server.display.mode.DisplayModeDirector$DeviceConfigDisplaySettings$$ExternalSyntheticLambda3
                @Override // java.util.function.IntSupplier
                public final int getAsInt() {
                    return displayDeviceConfig.getRefreshRateData().defaultRefreshRateInHbmSunlight;
                }
            }, android.R.integer.config_defaultNotificationLedOff, displayDeviceConfig);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int lambda$getRefreshRateInHbmSunlight$2() {
            return com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getRefreshRateInHbmSunlight();
        }

        private int getRefreshRate(java.util.function.IntSupplier fromConfigPram, java.util.function.IntSupplier fromDisplayDeviceConfig, int configKey, com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
            int asInt;
            int refreshRate = -1;
            try {
                refreshRate = fromConfigPram.getAsInt();
            } catch (java.lang.NullPointerException e) {
            }
            if (refreshRate == -1) {
                if (displayDeviceConfig == null) {
                    asInt = com.android.server.display.mode.DisplayModeDirector.this.mContext.getResources().getInteger(configKey);
                } else {
                    asInt = fromDisplayDeviceConfig.getAsInt();
                }
                int refreshRate2 = asInt;
                return refreshRate2;
            }
            return refreshRate;
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            float defaultPeakRefreshRate = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getPeakRefreshRateDefault();
            com.android.server.display.mode.DisplayModeDirector.this.mHandler.obtainMessage(3, defaultPeakRefreshRate == -1.0f ? null : java.lang.Float.valueOf(defaultPeakRefreshRate)).sendToTarget();
            float[] lowDisplayBrightnessThresholds = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getLowDisplayBrightnessThresholds();
            float[] lowAmbientBrightnessThresholds = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getLowAmbientBrightnessThresholds();
            int refreshRateInLowZone = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getRefreshRateInLowZone();
            com.android.server.display.mode.DisplayModeDirector.this.mHandler.obtainMessage(2, new android.util.Pair(lowDisplayBrightnessThresholds, lowAmbientBrightnessThresholds)).sendToTarget();
            com.android.server.display.mode.DisplayModeDirector.this.mHandler.obtainMessage(4, refreshRateInLowZone, 0).sendToTarget();
            float[] highDisplayBrightnessThresholds = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getHighDisplayBrightnessThresholds();
            float[] highAmbientBrightnessThresholds = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getHighAmbientBrightnessThresholds();
            int refreshRateInHighZone = com.android.server.display.mode.DisplayModeDirector.this.mConfigParameterProvider.getRefreshRateInHighZone();
            com.android.server.display.mode.DisplayModeDirector.this.mHandler.obtainMessage(6, new android.util.Pair(highDisplayBrightnessThresholds, highAmbientBrightnessThresholds)).sendToTarget();
            com.android.server.display.mode.DisplayModeDirector.this.mHandler.obtainMessage(5, refreshRateInHighZone, 0).sendToTarget();
            synchronized (com.android.server.display.mode.DisplayModeDirector.this.mLock) {
                int refreshRateInHbmSunlight = getRefreshRateInHbmSunlight(com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig);
                com.android.server.display.mode.DisplayModeDirector.this.mHandler.obtainMessage(7, refreshRateInHbmSunlight, 0).sendToTarget();
                int refreshRateInHbmHdr = getRefreshRateInHbmHdr(com.android.server.display.mode.DisplayModeDirector.this.mDefaultDisplayDeviceConfig);
                com.android.server.display.mode.DisplayModeDirector.this.mHandler.obtainMessage(8, refreshRateInHbmHdr, 0).sendToTarget();
            }
        }
    }

    static class RealInjector implements com.android.server.display.mode.DisplayModeDirector.Injector {
        private final android.content.Context mContext;
        private android.hardware.display.DisplayManager mDisplayManager;

        RealInjector(android.content.Context context) {
            this.mContext = context;
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public android.provider.DeviceConfigInterface getDeviceConfig() {
            return android.provider.DeviceConfigInterface.REAL;
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public void registerPeakRefreshRateObserver(android.content.ContentResolver cr, android.database.ContentObserver observer) {
            cr.registerContentObserver(PEAK_REFRESH_RATE_URI, false, observer, 0);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public void registerMinRefreshRateObserver(android.content.ContentResolver cr, android.database.ContentObserver observer) {
            cr.registerContentObserver(MIN_REFRESH_RATE_URI, false, observer, 0);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public void registerDisplayListener(android.hardware.display.DisplayManager.DisplayListener listener, android.os.Handler handler) {
            getDisplayManager().registerDisplayListener(listener, handler);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public void registerDisplayListener(android.hardware.display.DisplayManager.DisplayListener listener, android.os.Handler handler, long flags) {
            getDisplayManager().registerDisplayListener(listener, handler, flags);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public android.view.Display getDisplay(int displayId) {
            return getDisplayManager().getDisplay(displayId);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public android.view.Display[] getDisplays() {
            return getDisplayManager().getDisplays("android.hardware.display.category.ALL_INCLUDING_DISABLED");
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public boolean getDisplayInfo(int displayId, android.view.DisplayInfo displayInfo) {
            android.view.Display display = getDisplayManager().getDisplay(displayId);
            if (display == null) {
                return false;
            }
            return display.getDisplayInfo(displayInfo);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public android.hardware.display.BrightnessInfo getBrightnessInfo(int displayId) {
            android.view.Display display = getDisplayManager().getDisplay(displayId);
            if (display != null) {
                return display.getBrightnessInfo();
            }
            return null;
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public boolean isDozeState(android.view.Display d) {
            if (d == null) {
                return false;
            }
            return android.view.Display.isDozeState(d.getState());
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public boolean registerThermalServiceListener(android.os.IThermalEventListener listener) {
            android.os.IThermalService thermalService = getThermalService();
            if (thermalService == null) {
                android.util.Slog.w(com.android.server.display.mode.DisplayModeDirector.TAG, "Could not observe thermal status. Service not available");
                return false;
            }
            try {
                thermalService.registerThermalEventListenerWithType(listener, 3);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.display.mode.DisplayModeDirector.TAG, "Failed to register thermal status listener", e);
                return false;
            }
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public void unregisterThermalServiceListener(android.os.IThermalEventListener listener) {
            android.os.IThermalService thermalService = getThermalService();
            if (thermalService == null) {
                android.util.Slog.w(com.android.server.display.mode.DisplayModeDirector.TAG, "Could not unregister thermal status. Service not available");
            }
            try {
                thermalService.unregisterThermalEventListener(listener);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.display.mode.DisplayModeDirector.TAG, "Failed to unregister thermal status listener", e);
            }
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public boolean supportsFrameRateOverride() {
            return android.sysprop.SurfaceFlingerProperties.enable_frame_rate_override().orElse(true).booleanValue();
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public android.hardware.display.DisplayManagerInternal getDisplayManagerInternal() {
            return (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public com.android.server.statusbar.StatusBarManagerInternal getStatusBarManagerInternal() {
            return (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public com.android.server.sensors.SensorManagerInternal getSensorManagerInternal() {
            return (com.android.server.sensors.SensorManagerInternal) com.android.server.LocalServices.getService(com.android.server.sensors.SensorManagerInternal.class);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public com.android.server.display.mode.VotesStatsReporter getVotesStatsReporter(boolean refreshRateVotingTelemetryEnabled) {
            return new com.android.server.display.mode.VotesStatsReporter(supportsFrameRateOverride(), refreshRateVotingTelemetryEnabled);
        }

        private android.hardware.display.DisplayManager getDisplayManager() {
            if (this.mDisplayManager == null) {
                this.mDisplayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
            }
            return this.mDisplayManager;
        }

        private android.os.IThermalService getThermalService() {
            return android.os.IThermalService.Stub.asInterface(android.os.ServiceManager.getService("thermalservice"));
        }
    }
}
