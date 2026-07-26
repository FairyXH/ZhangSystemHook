package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class LocalDisplayAdapter extends com.android.server.display.DisplayAdapter {
    private static final float BRIGHTNESS_MIN = 0.0f;
    private static final float EVEN_DIMMER_MAX_STRENGTH = 90.0f;
    private static final float EVEN_DIMMER_MIN_STRENGTH = 0.0f;
    private static final java.lang.String PROPERTY_EMULATOR_CIRCULAR = "ro.boot.emulator.circular";
    private static final java.lang.String UNIQUE_ID_PREFIX = "local:";
    private static final float ZERO_NIT = 0.0f;
    private final int mBrightnessThreshold;
    private com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal mCdsi;
    private final android.util.LongSparseArray<com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice> mDevices;
    private final com.android.server.display.notifications.DisplayNotificationManager mDisplayNotificationManager;
    private int mEvenDimmerStrength;
    private boolean mHasSoftIris;
    private final int mHightBrightness;
    private final com.android.server.display.LocalDisplayAdapter.Injector mInjector;
    private int mIris514command;
    private int mIris514ext;
    private com.pixelworks.hardware.IrisHal mIrisHal;
    private final boolean mIsBootDisplayModeSupported;
    private com.android.server.display.LocalDisplayAdapter.LocalDisplayAdapterWrapper mLdaWrapper;
    private final int mLowBrightness;
    private android.content.Context mOverlayContext;
    private final com.android.server.display.LocalDisplayAdapter.SurfaceControlProxy mSurfaceControlProxy;
    private boolean mUseHWCbacklight;
    private int mlast_level;
    private static final boolean PANIC_DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.lang.String TAG = "LocalDisplayAdapter";
    private static boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);
    private static final boolean MTK_DEBUG = "eng".equals(android.os.Build.TYPE);

    public interface DisplayEventListener {
        void onFrameRateOverridesChanged(long j, long j2, android.view.DisplayEventReceiver.FrameRateOverride[] frameRateOverrideArr);

        void onHdcpLevelsChanged(long j, int i, int i2);

        void onHotplug(long j, long j2, boolean z);

        void onHotplugConnectionError(long j, int i);

        void onModeChanged(long j, long j2, int i, long j3);
    }

    private enum IrisTable {
        LOWLEVEL,
        MEDIUMLEVEL,
        HIGHTLEVEL
    }

    LocalDisplayAdapter(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.content.Context context, android.os.Handler handler, com.android.server.display.DisplayAdapter.Listener listener, com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.notifications.DisplayNotificationManager displayNotificationManager) {
        this(syncRoot, context, handler, listener, flags, displayNotificationManager, new com.android.server.display.LocalDisplayAdapter.Injector());
    }

    LocalDisplayAdapter(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.content.Context context, android.os.Handler handler, com.android.server.display.DisplayAdapter.Listener listener, com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.notifications.DisplayNotificationManager displayNotificationManager, com.android.server.display.LocalDisplayAdapter.Injector injector) {
        super(syncRoot, context, handler, listener, TAG, flags);
        this.mDevices = new android.util.LongSparseArray<>();
        this.mEvenDimmerStrength = -1;
        this.mIrisHal = null;
        this.mIris514command = 514;
        this.mIris514ext = 3;
        this.mlast_level = -1;
        this.mHasSoftIris = false;
        this.mLowBrightness = 2500;
        this.mHightBrightness = 4334;
        this.mBrightnessThreshold = 12;
        this.mUseHWCbacklight = false;
        this.mLdaWrapper = new com.android.server.display.LocalDisplayAdapter.LocalDisplayAdapterWrapper();
        this.mDisplayNotificationManager = displayNotificationManager;
        this.mInjector = injector;
        this.mSurfaceControlProxy = this.mInjector.getSurfaceControlProxy();
        this.mLdaWrapper.getExtImpl().init(context);
        this.mIsBootDisplayModeSupported = this.mSurfaceControlProxy.getBootDisplayModeSupport();
        if (getChipFeatue() != 0) {
            this.mIrisHal = new com.pixelworks.hardware.IrisHal();
            if (this.mIrisHal == null) {
                android.util.Slog.e(TAG, "IRIS_LOG_LIGT Get IrisHal failed");
            }
        } else {
            android.util.Slog.e(TAG, "Failed to get Iris feature");
        }
        this.mHasSoftIris = getSoftIrisCapability();
        this.mUseHWCbacklight = getUseHWCbacklightProject();
    }

    @Override // com.android.server.display.DisplayAdapter
    public void registerLocked() {
        int i;
        super.registerLocked();
        this.mInjector.setDisplayEventListenerLocked(getHandler().getLooper(), new com.android.server.display.LocalDisplayAdapter.LocalDisplayEventListener());
        long[] ids = this.mSurfaceControlProxy.getPhysicalDisplayIds();
        int i2 = 0;
        while (true) {
            if (i2 >= ids.length) {
                break;
            }
            for (int j = i2 + 1; j < ids.length; j++) {
                android.view.SurfaceControl.StaticDisplayInfo sinfo_i = this.mSurfaceControlProxy.getStaticDisplayInfo(ids[i2]);
                android.view.SurfaceControl.StaticDisplayInfo sinfo_j = this.mSurfaceControlProxy.getStaticDisplayInfo(ids[j]);
                if (sinfo_i != null && sinfo_j != null && sinfo_i.isInternal && sinfo_j.isInternal) {
                    android.view.SurfaceControl.DynamicDisplayInfo dinfo_i = this.mSurfaceControlProxy.getDynamicDisplayInfo(ids[i2]);
                    android.view.SurfaceControl.DynamicDisplayInfo dinfo_j = this.mSurfaceControlProxy.getDynamicDisplayInfo(ids[j]);
                    if (dinfo_i != null && dinfo_j != null && dinfo_i.supportedDisplayModes != null && dinfo_j.supportedDisplayModes != null && dinfo_i.supportedDisplayModes[0].width < dinfo_j.supportedDisplayModes[0].width) {
                        android.util.Slog.e(TAG, "registerLocked: change ids[" + i2 + "]=" + ids[i2] + " w=" + dinfo_i.supportedDisplayModes[0].width + " to ids[" + j + "]=" + ids[j] + " w=" + dinfo_j.supportedDisplayModes[0].width);
                        long tempId = ids[i2];
                        ids[i2] = ids[j];
                        ids[j] = tempId;
                    }
                }
            }
            i2++;
        }
        for (long physicalDisplayId : ids) {
            tryConnectDisplayLocked(physicalDisplayId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryConnectDisplayLocked(long physicalDisplayId) {
        android.os.IBinder displayToken = this.mSurfaceControlProxy.getPhysicalDisplayToken(physicalDisplayId);
        if (displayToken != null) {
            android.view.SurfaceControl.StaticDisplayInfo staticInfo = this.mSurfaceControlProxy.getStaticDisplayInfo(physicalDisplayId);
            if (staticInfo == null) {
                android.util.Slog.w(TAG, "No valid static info found for display device " + physicalDisplayId);
                return;
            }
            android.view.SurfaceControl.DynamicDisplayInfo dynamicInfo = this.mSurfaceControlProxy.getDynamicDisplayInfo(physicalDisplayId);
            if (dynamicInfo == null) {
                android.util.Slog.w(TAG, "No valid dynamic info found for display device " + physicalDisplayId);
                return;
            }
            if (dynamicInfo.supportedDisplayModes == null) {
                android.util.Slog.w(TAG, "No valid modes found for display device " + physicalDisplayId);
                return;
            }
            if (dynamicInfo.activeDisplayModeId < 0) {
                android.util.Slog.w(TAG, "No valid active mode found for display device " + physicalDisplayId);
                return;
            }
            if (dynamicInfo.activeColorMode < 0) {
                android.util.Slog.w(TAG, "No valid active color mode for display device " + physicalDisplayId);
                dynamicInfo.activeColorMode = -1;
            }
            android.view.SurfaceControl.DesiredDisplayModeSpecs modeSpecs = this.mSurfaceControlProxy.getDesiredDisplayModeSpecs(displayToken);
            if (modeSpecs == null) {
                android.util.Slog.w(TAG, "Desired display mode specs from SurfaceFlinger are null");
                return;
            }
            com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice device = this.mDevices.get(physicalDisplayId);
            android.util.Slog.d(TAG, "tryConnectDisplayLocked id=" + physicalDisplayId + " staticInfo=" + staticInfo + " dynamicInfo=" + dynamicInfo);
            if (device == null) {
                boolean isFirstDisplay = this.mDevices.size() == 0;
                if (isFirstDisplay) {
                    this.mLdaWrapper.getExtImpl().setPrimaryPhysicalDisplayId(physicalDisplayId);
                }
                com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice device2 = new com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice(displayToken, physicalDisplayId, staticInfo, dynamicInfo, modeSpecs, isFirstDisplay);
                this.mDevices.put(physicalDisplayId, device2);
                sendDisplayDeviceEventLocked(device2, 1);
                return;
            }
            if (device.updateDisplayPropertiesLocked(staticInfo, dynamicInfo, modeSpecs)) {
                sendDisplayDeviceEventLocked(device, 2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryDisconnectDisplayLocked(long physicalDisplayId) {
        com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice device = this.mDevices.get(physicalDisplayId);
        if (device != null) {
            this.mDevices.remove(physicalDisplayId);
            sendDisplayDeviceEventLocked(device, 3);
        }
    }

    static int getPowerModeForState(int state) {
        switch (state) {
            case 1:
                return 0;
            case 2:
            case 5:
            default:
                return 2;
            case 3:
                return 1;
            case 4:
                return 3;
            case 6:
                return 4;
        }
    }

    public int getChipFeatue() {
        com.pixelworks.hardware.IrisFeatureHal irisFeatureHal = new com.pixelworks.hardware.IrisFeatureHal();
        return irisFeatureHal.getFeature();
    }

    public boolean getSoftIrisCapability() {
        com.pixelworks.hardware.IrisFeatureHal irisFeatureHal = new com.pixelworks.hardware.IrisFeatureHal();
        return irisFeatureHal.getSoftIrisCapability() > 0;
    }

    public boolean getUseHWCbacklightProject() {
        java.lang.String projectName = android.os.SystemProperties.get("ro.boot.prjname", "0");
        return projectName.equals("22825") || projectName.equals("22877");
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class LocalDisplayDevice extends com.android.server.display.DisplayDevice {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private int mActiveColorMode;
        private int mActiveModeId;
        private float mActiveRenderFrameRate;
        private android.view.SurfaceControl.DisplayMode mActiveSfDisplayMode;
        private int mActiveSfDisplayModeAtStartId;
        private boolean mAllmRequested;
        private boolean mAllmSupported;
        private final com.android.server.display.LocalDisplayAdapter.BacklightAdapter mBacklightAdapter;
        private float mBrightnessState;
        private int mCommittedState;
        private int mConnectedHdcpLevel;
        private int mCurrentBacklightType;
        private float mCurrentHdrSdrRatio;
        private int mDcThreshold;
        private int mDefaultModeGroup;
        private int mDefaultModeId;
        private final com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs mDisplayModeSpecs;
        private boolean mDisplayModeSpecsInvalid;
        private android.view.DisplayEventReceiver.FrameRateOverride[] mFrameRateOverrides;
        private boolean mGameContentTypeRequested;
        private boolean mGameContentTypeSupported;
        private boolean mHavePendingChanges;
        private android.view.Display.HdrCapabilities mHdrCapabilities;
        private com.android.server.display.DisplayDeviceInfo mInfo;
        private final boolean mIsFirstDisplay;
        private final long mPhysicalDisplayId;
        private float mSdrBrightnessState;
        private android.view.SurfaceControl.DisplayMode[] mSfDisplayModes;
        private boolean mSidekickActive;
        private final android.hardware.sidekick.SidekickInternal mSidekickInternal;
        private int mState;
        private android.view.SurfaceControl.StaticDisplayInfo mStaticDisplayInfo;
        private final java.util.ArrayList<java.lang.Integer> mSupportedColorModes;
        private final android.util.SparseArray<com.android.server.display.LocalDisplayAdapter.DisplayModeRecord> mSupportedModes;
        private int mSystemPreferredModeId;
        private android.view.Display.Mode mUserPreferredMode;
        private int mUserPreferredModeId;

        LocalDisplayDevice(android.os.IBinder displayToken, long physicalDisplayId, android.view.SurfaceControl.StaticDisplayInfo staticDisplayInfo, android.view.SurfaceControl.DynamicDisplayInfo dynamicInfo, android.view.SurfaceControl.DesiredDisplayModeSpecs modeSpecs, boolean isFirstDisplay) {
            super(com.android.server.display.LocalDisplayAdapter.this, displayToken, com.android.server.display.LocalDisplayAdapter.UNIQUE_ID_PREFIX + physicalDisplayId, com.android.server.display.LocalDisplayAdapter.this.getContext(), com.android.server.display.LocalDisplayAdapter.this.getFeatureFlags().isPixelAnisotropyCorrectionInLogicalDisplayEnabled());
            this.mSupportedModes = new android.util.SparseArray<>();
            this.mSupportedColorModes = new java.util.ArrayList<>();
            this.mDisplayModeSpecs = new com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs();
            this.mState = 0;
            this.mCommittedState = 0;
            this.mBrightnessState = Float.NaN;
            this.mSdrBrightnessState = Float.NaN;
            this.mCurrentHdrSdrRatio = Float.NaN;
            this.mCurrentBacklightType = 0;
            this.mDefaultModeId = -1;
            this.mSystemPreferredModeId = -1;
            this.mUserPreferredModeId = -1;
            this.mActiveSfDisplayModeAtStartId = -1;
            this.mActiveModeId = -1;
            this.mDcThreshold = android.os.SystemProperties.getInt("ro.oplus.dc.brightness.threshold", 0);
            this.mFrameRateOverrides = new android.view.DisplayEventReceiver.FrameRateOverride[0];
            com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setStaticDisplayDensity(staticDisplayInfo, physicalDisplayId);
            this.mPhysicalDisplayId = physicalDisplayId;
            this.mIsFirstDisplay = isFirstDisplay;
            updateDisplayPropertiesLocked(staticDisplayInfo, dynamicInfo, modeSpecs);
            this.mSidekickInternal = (android.hardware.sidekick.SidekickInternal) com.android.server.LocalServices.getService(android.hardware.sidekick.SidekickInternal.class);
            this.mBacklightAdapter = new com.android.server.display.LocalDisplayAdapter.BacklightAdapter(displayToken, isFirstDisplay, com.android.server.display.LocalDisplayAdapter.this.mSurfaceControlProxy);
            this.mActiveSfDisplayModeAtStartId = dynamicInfo.activeDisplayModeId;
            getExtImpl().setPhysicalDisplayId(physicalDisplayId, isFirstDisplay);
        }

        @Override // com.android.server.display.DisplayDevice
        public boolean hasStableUniqueId() {
            return true;
        }

        @Override // com.android.server.display.DisplayDevice
        public android.view.Display.Mode getActiveDisplayModeAtStartLocked() {
            return findMode(findMatchingModeIdLocked(this.mActiveSfDisplayModeAtStartId));
        }

        public boolean updateDisplayPropertiesLocked(android.view.SurfaceControl.StaticDisplayInfo staticInfo, android.view.SurfaceControl.DynamicDisplayInfo dynamicInfo, android.view.SurfaceControl.DesiredDisplayModeSpecs modeSpecs) {
            boolean changed = updateDisplayModesLocked(dynamicInfo.supportedDisplayModes, dynamicInfo.preferredBootDisplayMode, dynamicInfo.activeDisplayModeId, dynamicInfo.renderFrameRate, modeSpecs) | updateStaticInfo(staticInfo) | updateColorModesLocked(dynamicInfo.supportedColorModes, dynamicInfo.activeColorMode) | updateHdrCapabilitiesLocked(dynamicInfo.hdrCapabilities) | updateAllmSupport(dynamicInfo.autoLowLatencyModeSupported) | updateGameContentTypeSupport(dynamicInfo.gameContentTypeSupported);
            if (changed) {
                this.mHavePendingChanges = true;
            }
            return changed;
        }

        public boolean updateDisplayModesLocked(android.view.SurfaceControl.DisplayMode[] displayModes, int preferredSfDisplayModeId, int activeSfDisplayModeId, float renderFrameRate, android.view.SurfaceControl.DesiredDisplayModeSpecs modeSpecs) {
            int activeBaseMode;
            android.view.SurfaceControl.DisplayMode[] displayModeArr = displayModes;
            this.mSfDisplayModes = (android.view.SurfaceControl.DisplayMode[]) java.util.Arrays.copyOf(displayModeArr, displayModeArr.length);
            this.mActiveSfDisplayMode = getModeById(displayModeArr, activeSfDisplayModeId);
            android.view.SurfaceControl.DisplayMode preferredSfDisplayMode = getModeById(displayModes, preferredSfDisplayModeId);
            android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "updateDisplayModesLocked activeMode=" + this.mActiveSfDisplayMode);
            com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().updateDisplayModes(this.mIsFirstDisplay, this.mPhysicalDisplayId);
            java.util.ArrayList<com.android.server.display.LocalDisplayAdapter.DisplayModeRecord> records = new java.util.ArrayList<>();
            boolean modesAdded = false;
            int i = 0;
            while (i < displayModeArr.length) {
                android.view.SurfaceControl.DisplayMode mode = displayModeArr[i];
                java.util.List<java.lang.Float> alternativeRefreshRates = new java.util.ArrayList<>();
                int j = 0;
                while (j < displayModeArr.length) {
                    android.view.SurfaceControl.DisplayMode other = displayModeArr[j];
                    boolean isAlternative = j != i && other.width == mode.width && other.height == mode.height && other.peakRefreshRate != mode.peakRefreshRate && other.group == mode.group;
                    if (isAlternative) {
                        alternativeRefreshRates.add(java.lang.Float.valueOf(displayModeArr[j].peakRefreshRate));
                    }
                    j++;
                }
                java.util.Collections.sort(alternativeRefreshRates);
                boolean existingMode = false;
                java.util.Iterator<com.android.server.display.LocalDisplayAdapter.DisplayModeRecord> it = records.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    com.android.server.display.LocalDisplayAdapter.DisplayModeRecord record = it.next();
                    if (record.hasMatchingMode(mode) && refreshRatesEquals(alternativeRefreshRates, record.mMode.getAlternativeRefreshRates())) {
                        existingMode = true;
                        break;
                    }
                }
                if (!existingMode) {
                    com.android.server.display.LocalDisplayAdapter.DisplayModeRecord record2 = findDisplayModeRecord(mode, alternativeRefreshRates);
                    if (record2 == null) {
                        float[] alternativeRates = new float[alternativeRefreshRates.size()];
                        for (int j2 = 0; j2 < alternativeRates.length; j2++) {
                            alternativeRates[j2] = alternativeRefreshRates.get(j2).floatValue();
                        }
                        record2 = new com.android.server.display.LocalDisplayAdapter.DisplayModeRecord(mode, alternativeRates);
                        modesAdded = true;
                    }
                    records.add(record2);
                }
                i++;
                displayModeArr = displayModes;
            }
            com.android.server.display.LocalDisplayAdapter.DisplayModeRecord activeRecord = null;
            java.util.Iterator<com.android.server.display.LocalDisplayAdapter.DisplayModeRecord> it2 = records.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                com.android.server.display.LocalDisplayAdapter.DisplayModeRecord record3 = it2.next();
                if (record3.hasMatchingMode(this.mActiveSfDisplayMode)) {
                    activeRecord = record3;
                    break;
                }
            }
            boolean preferredModeChanged = false;
            if (preferredSfDisplayModeId != -1 && preferredSfDisplayMode != null) {
                com.android.server.display.LocalDisplayAdapter.DisplayModeRecord preferredRecord = null;
                java.util.Iterator<com.android.server.display.LocalDisplayAdapter.DisplayModeRecord> it3 = records.iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        break;
                    }
                    com.android.server.display.LocalDisplayAdapter.DisplayModeRecord record4 = it3.next();
                    if (record4.hasMatchingMode(preferredSfDisplayMode)) {
                        preferredRecord = record4;
                        break;
                    }
                }
                if (preferredRecord != null) {
                    int preferredModeId = preferredRecord.mMode.getModeId();
                    if (com.android.server.display.LocalDisplayAdapter.this.mIsBootDisplayModeSupported && this.mSystemPreferredModeId != preferredModeId) {
                        this.mSystemPreferredModeId = preferredModeId;
                        preferredModeChanged = true;
                    }
                }
            }
            boolean activeModeChanged = false;
            if (this.mActiveModeId != -1 && this.mActiveModeId != activeRecord.mMode.getModeId()) {
                android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "The active mode was changed from SurfaceFlinger or the display device to " + activeRecord.mMode);
                this.mActiveModeId = activeRecord.mMode.getModeId();
                activeModeChanged = true;
                com.android.server.display.LocalDisplayAdapter.this.sendTraversalRequestLocked();
            }
            boolean renderFrameRateChanged = false;
            if (this.mActiveRenderFrameRate > 0.0f && this.mActiveRenderFrameRate != renderFrameRate) {
                android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "The render frame rate was changed from SurfaceFlinger or the display device to " + renderFrameRate);
                this.mActiveRenderFrameRate = renderFrameRate;
                renderFrameRateChanged = true;
                com.android.server.display.LocalDisplayAdapter.this.sendTraversalRequestLocked();
            }
            if (this.mDisplayModeSpecs.baseModeId != -1 && ((activeBaseMode = findMatchingModeIdLocked(modeSpecs.defaultMode)) == -1 || this.mDisplayModeSpecs.baseModeId != activeBaseMode || !this.mDisplayModeSpecs.primary.equals(modeSpecs.primaryRanges) || !this.mDisplayModeSpecs.appRequest.equals(modeSpecs.appRequestRanges))) {
                this.mDisplayModeSpecsInvalid = true;
                com.android.server.display.LocalDisplayAdapter.this.sendTraversalRequestLocked();
            }
            boolean recordsChanged = records.size() != this.mSupportedModes.size() || modesAdded;
            if (!recordsChanged) {
                return activeModeChanged || preferredModeChanged || renderFrameRateChanged;
            }
            this.mSupportedModes.clear();
            for (com.android.server.display.LocalDisplayAdapter.DisplayModeRecord record5 : records) {
                this.mSupportedModes.put(record5.mMode.getModeId(), record5);
            }
            if (this.mDefaultModeId == -1) {
                this.mDefaultModeId = activeRecord.mMode.getModeId();
                this.mDefaultModeGroup = this.mActiveSfDisplayMode.group;
                this.mActiveRenderFrameRate = renderFrameRate;
            } else if (!modesAdded || !activeModeChanged) {
                if (findSfDisplayModeIdLocked(this.mDefaultModeId, this.mDefaultModeGroup) < 0) {
                    android.util.Slog.w(com.android.server.display.LocalDisplayAdapter.TAG, "Default display mode no longer available, using currently active mode as default.");
                    this.mDefaultModeId = activeRecord.mMode.getModeId();
                    this.mDefaultModeGroup = this.mActiveSfDisplayMode.group;
                    this.mActiveRenderFrameRate = renderFrameRate;
                }
            } else {
                android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "New display modes are added and the active mode has changed, use active mode as default mode.");
                this.mDefaultModeId = activeRecord.mMode.getModeId();
                this.mDefaultModeGroup = this.mActiveSfDisplayMode.group;
                this.mActiveRenderFrameRate = renderFrameRate;
            }
            if (this.mSupportedModes.indexOfKey(this.mDisplayModeSpecs.baseModeId) < 0) {
                if (this.mDisplayModeSpecs.baseModeId != -1) {
                    android.util.Slog.w(com.android.server.display.LocalDisplayAdapter.TAG, "DisplayModeSpecs base mode no longer available, using currently active mode.");
                }
                this.mDisplayModeSpecs.baseModeId = activeRecord.mMode.getModeId();
                this.mDisplayModeSpecsInvalid = true;
            }
            if (this.mUserPreferredMode != null) {
                this.mUserPreferredModeId = findUserPreferredModeIdLocked(this.mUserPreferredMode);
            }
            if (this.mSupportedModes.indexOfKey(this.mActiveModeId) < 0) {
                if (this.mActiveModeId != -1) {
                    android.util.Slog.w(com.android.server.display.LocalDisplayAdapter.TAG, "Active display mode no longer available, reverting to default mode.");
                }
                this.mActiveModeId = getPreferredModeId();
            }
            com.android.server.display.LocalDisplayAdapter.this.sendTraversalRequestLocked();
            return true;
        }

        @Override // com.android.server.display.DisplayDevice
        public com.android.server.display.DisplayDeviceConfig getDisplayDeviceConfig() {
            if (this.mDisplayDeviceConfig == null) {
                loadDisplayDeviceConfig();
            }
            return this.mDisplayDeviceConfig;
        }

        private int getPreferredModeId() {
            if (this.mUserPreferredModeId != -1) {
                return this.mUserPreferredModeId;
            }
            return this.mDefaultModeId;
        }

        private int getLogicalDensity() {
            com.android.server.display.DensityMapping densityMapping = getDisplayDeviceConfig().getDensityMapping();
            if (densityMapping == null) {
                return (int) (((double) (this.mStaticDisplayInfo.density * 160.0f)) + 0.5d);
            }
            return densityMapping.getDensityForResolution(this.mInfo.width, this.mInfo.height);
        }

        private void loadDisplayDeviceConfig() {
            android.content.Context context = com.android.server.display.LocalDisplayAdapter.this.getOverlayContext();
            this.mDisplayDeviceConfig = com.android.server.display.LocalDisplayAdapter.this.mInjector.createDisplayDeviceConfig(context, this.mPhysicalDisplayId, this.mIsFirstDisplay, com.android.server.display.LocalDisplayAdapter.this.getFeatureFlags());
            this.mBacklightAdapter.setForceSurfaceControl(this.mDisplayDeviceConfig.hasQuirk(com.android.server.display.DisplayDeviceConfig.QUIRK_CAN_SET_BRIGHTNESS_VIA_HWC));
            this.mBacklightAdapter.setForceSurfaceControl(true);
        }

        private boolean updateStaticInfo(android.view.SurfaceControl.StaticDisplayInfo info) {
            if (java.util.Objects.equals(this.mStaticDisplayInfo, info)) {
                return false;
            }
            this.mStaticDisplayInfo = info;
            return true;
        }

        private boolean updateColorModesLocked(int[] colorModes, int activeColorMode) {
            if (colorModes == null) {
                return false;
            }
            java.util.List<java.lang.Integer> pendingColorModes = new java.util.ArrayList<>();
            boolean colorModesAdded = false;
            for (int colorMode : colorModes) {
                if (!this.mSupportedColorModes.contains(java.lang.Integer.valueOf(colorMode))) {
                    colorModesAdded = true;
                }
                pendingColorModes.add(java.lang.Integer.valueOf(colorMode));
            }
            boolean colorModesChanged = pendingColorModes.size() != this.mSupportedColorModes.size() || colorModesAdded;
            if (!colorModesChanged) {
                return false;
            }
            this.mSupportedColorModes.clear();
            this.mSupportedColorModes.addAll(pendingColorModes);
            java.util.Collections.sort(this.mSupportedColorModes);
            if (!this.mSupportedColorModes.contains(java.lang.Integer.valueOf(this.mActiveColorMode))) {
                if (this.mActiveColorMode != 0) {
                    android.util.Slog.w(com.android.server.display.LocalDisplayAdapter.TAG, "Active color mode no longer available, reverting to default mode.");
                    this.mActiveColorMode = 0;
                } else if (!this.mSupportedColorModes.isEmpty()) {
                    android.util.Slog.e(com.android.server.display.LocalDisplayAdapter.TAG, "Default and active color mode is no longer available! Reverting to first available mode.");
                    this.mActiveColorMode = this.mSupportedColorModes.get(0).intValue();
                } else {
                    android.util.Slog.e(com.android.server.display.LocalDisplayAdapter.TAG, "No color modes available!");
                }
            }
            return true;
        }

        private boolean updateHdrCapabilitiesLocked(android.view.Display.HdrCapabilities newHdrCapabilities) {
            if (java.util.Objects.equals(this.mHdrCapabilities, newHdrCapabilities)) {
                return false;
            }
            this.mHdrCapabilities = newHdrCapabilities;
            return true;
        }

        private boolean updateAllmSupport(boolean supported) {
            if (this.mAllmSupported == supported) {
                return false;
            }
            this.mAllmSupported = supported;
            return true;
        }

        private boolean updateGameContentTypeSupport(boolean supported) {
            if (this.mGameContentTypeSupported == supported) {
                return false;
            }
            this.mGameContentTypeSupported = supported;
            return true;
        }

        private android.view.SurfaceControl.DisplayMode getModeById(android.view.SurfaceControl.DisplayMode[] supportedModes, int modeId) {
            for (android.view.SurfaceControl.DisplayMode mode : supportedModes) {
                if (mode.id == modeId) {
                    return mode;
                }
            }
            android.util.Slog.e(com.android.server.display.LocalDisplayAdapter.TAG, "Can't find display mode with id " + modeId);
            return null;
        }

        private com.android.server.display.LocalDisplayAdapter.DisplayModeRecord findDisplayModeRecord(android.view.SurfaceControl.DisplayMode mode, java.util.List<java.lang.Float> alternativeRefreshRates) {
            for (int i = 0; i < this.mSupportedModes.size(); i++) {
                com.android.server.display.LocalDisplayAdapter.DisplayModeRecord record = this.mSupportedModes.valueAt(i);
                if (record.hasMatchingMode(mode) && refreshRatesEquals(alternativeRefreshRates, record.mMode.getAlternativeRefreshRates()) && com.android.server.display.LocalDisplayAdapter.this.hdrTypesEqual(mode.supportedHdrTypes, record.mMode.getSupportedHdrTypes())) {
                    return record;
                }
            }
            return null;
        }

        private boolean refreshRatesEquals(java.util.List<java.lang.Float> list, float[] array) {
            if (list.size() != array.length) {
                return false;
            }
            for (int i = 0; i < list.size(); i++) {
                if (java.lang.Float.floatToIntBits(list.get(i).floatValue()) != java.lang.Float.floatToIntBits(array[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.android.server.display.DisplayDevice
        public void applyPendingDisplayDeviceInfoChangesLocked() {
            if (this.mHavePendingChanges) {
                this.mInfo = null;
                this.mHavePendingChanges = false;
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public com.android.server.display.DisplayDeviceInfo getDisplayDeviceInfoLocked() {
            java.lang.String str;
            if (this.mInfo == null) {
                this.mInfo = new com.android.server.display.DisplayDeviceInfo();
                this.mInfo.width = this.mActiveSfDisplayMode.width;
                this.mInfo.height = this.mActiveSfDisplayMode.height;
                this.mInfo.modeId = this.mActiveModeId;
                this.mInfo.renderFrameRate = this.mActiveRenderFrameRate;
                this.mInfo.defaultModeId = getPreferredModeId();
                this.mInfo.userPreferredModeId = this.mUserPreferredModeId;
                this.mInfo.supportedModes = getDisplayModes(this.mSupportedModes);
                this.mInfo.colorMode = this.mActiveColorMode;
                this.mInfo.allmSupported = this.mAllmSupported;
                this.mInfo.gameContentTypeSupported = this.mGameContentTypeSupported;
                this.mInfo.supportedColorModes = new int[this.mSupportedColorModes.size()];
                for (int i = 0; i < this.mSupportedColorModes.size(); i++) {
                    this.mInfo.supportedColorModes[i] = this.mSupportedColorModes.get(i).intValue();
                }
                this.mInfo.hdrCapabilities = this.mHdrCapabilities;
                this.mInfo.appVsyncOffsetNanos = this.mActiveSfDisplayMode.appVsyncOffsetNanos;
                this.mInfo.presentationDeadlineNanos = this.mActiveSfDisplayMode.presentationDeadlineNanos;
                this.mInfo.state = this.mState;
                this.mInfo.committedState = this.mCommittedState;
                this.mInfo.uniqueId = getUniqueId();
                android.view.DisplayAddress.Physical physicalAddress = android.view.DisplayAddress.fromPhysicalDisplayId(this.mPhysicalDisplayId);
                this.mInfo.address = physicalAddress;
                this.mInfo.densityDpi = getLogicalDensity();
                com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setDisplayInfoDpi(this.mInfo, this.mPhysicalDisplayId);
                this.mInfo.xDpi = this.mActiveSfDisplayMode.xDpi;
                this.mInfo.yDpi = this.mActiveSfDisplayMode.yDpi;
                this.mInfo.deviceProductInfo = this.mStaticDisplayInfo.deviceProductInfo;
                if (this.mConnectedHdcpLevel != 0) {
                    this.mStaticDisplayInfo.secure = this.mConnectedHdcpLevel >= 2;
                }
                if (this.mStaticDisplayInfo.secure) {
                    this.mInfo.flags = 12;
                }
                android.content.res.Resources res = com.android.server.display.LocalDisplayAdapter.this.getOverlayContext().getResources();
                boolean isBuiltIn = this.mInfo.address != null && (this.mInfo.address.getPort() & 128) == 128;
                this.mInfo.flags |= 1;
                if (this.mIsFirstDisplay) {
                    if (res.getBoolean(android.R.bool.config_leftRightSplitInPortrait) || (android.os.Build.IS_EMULATOR && android.os.SystemProperties.getBoolean(com.android.server.display.LocalDisplayAdapter.PROPERTY_EMULATOR_CIRCULAR, false))) {
                        this.mInfo.flags |= 256;
                    }
                } else if (isBuiltIn) {
                    this.mInfo.type = 1;
                    this.mInfo.touch = 1;
                    this.mInfo.name = com.android.server.display.LocalDisplayAdapter.this.getContext().getResources().getString(android.R.string.dump_heap_ready_notification);
                    this.mInfo.flags |= 2;
                    if (android.os.SystemProperties.getBoolean("vendor.display.builtin_presentation", false)) {
                        this.mInfo.flags |= 64;
                    }
                    if (!android.os.SystemProperties.getBoolean("vendor.display.builtin_mirroring", false)) {
                        this.mInfo.flags |= 128;
                    }
                    int smallest_width = android.os.SystemProperties.getInt("vendor.display.smallest_width", 360);
                    android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "densityDpi=" + this.mInfo.densityDpi + " smallWidth=" + smallest_width + " width=" + this.mInfo.width + " activeMode=" + this.mActiveSfDisplayMode);
                } else {
                    if (!res.getBoolean(android.R.bool.config_is_powerbutton_fps)) {
                        this.mInfo.flags |= 128;
                    }
                    if (isDisplayPrivate(physicalAddress)) {
                        this.mInfo.flags |= 16;
                    }
                }
                if (android.view.DisplayCutout.getMaskBuiltInDisplayCutout(res, this.mInfo.uniqueId)) {
                    this.mInfo.flags |= 2048;
                }
                android.view.Display.Mode maxDisplayMode = android.util.DisplayUtils.getMaximumResolutionDisplayMode(this.mInfo.supportedModes);
                int maxWidth = maxDisplayMode == null ? this.mInfo.width : maxDisplayMode.getPhysicalWidth();
                int maxHeight = maxDisplayMode == null ? this.mInfo.height : maxDisplayMode.getPhysicalHeight();
                if (this.mStaticDisplayInfo.isInternal) {
                    this.mInfo.displayCutout = android.view.DisplayCutout.fromResourcesRectApproximation(res, this.mInfo.uniqueId, maxWidth, maxHeight, this.mInfo.width, this.mInfo.height);
                    com.android.server.display.DisplayDeviceInfo displayDeviceInfo = this.mInfo;
                    java.lang.String str2 = this.mInfo.uniqueId;
                    int i2 = this.mInfo.width;
                    int i3 = this.mInfo.height;
                    str = com.android.server.display.LocalDisplayAdapter.TAG;
                    displayDeviceInfo.roundedCorners = android.view.RoundedCorners.fromResources(res, str2, maxWidth, maxHeight, i2, i3);
                } else {
                    str = com.android.server.display.LocalDisplayAdapter.TAG;
                }
                this.mInfo.installOrientation = this.mStaticDisplayInfo.installOrientation;
                this.mInfo.displayShape = android.view.DisplayShape.fromResources(res, this.mInfo.uniqueId, maxWidth, maxHeight, this.mInfo.width, this.mInfo.height);
                this.mInfo.name = getDisplayDeviceConfig().getName();
                if (this.mStaticDisplayInfo.isInternal) {
                    this.mInfo.type = 1;
                    this.mInfo.touch = 1;
                    com.android.server.display.DisplayDeviceInfo displayDeviceInfo2 = this.mInfo;
                    displayDeviceInfo2.flags = 2 | displayDeviceInfo2.flags;
                    if (this.mInfo.name == null) {
                        this.mInfo.name = res.getString(android.R.string.dump_heap_ready_notification);
                    }
                    int densityDpi = com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getSecondaryLcdDensity();
                    if (!this.mIsFirstDisplay && densityDpi != 0) {
                        android.util.Slog.d(str, "secondary lcd density override " + this.mInfo.densityDpi + "->" + densityDpi);
                        this.mInfo.densityDpi = densityDpi;
                    }
                } else {
                    this.mInfo.type = 2;
                    this.mInfo.touch = 2;
                    this.mInfo.flags |= 64;
                    if (this.mInfo.name == null) {
                        this.mInfo.name = com.android.server.display.LocalDisplayAdapter.this.getContext().getResources().getString(android.R.string.dump_heap_ready_text);
                    }
                }
                this.mInfo.frameRateOverrides = this.mFrameRateOverrides;
                this.mInfo.flags |= 8192;
                this.mInfo.brightnessMinimum = 0.0f;
                this.mInfo.brightnessMaximum = 1.0f;
                this.mInfo.brightnessDefault = com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getDefaultDisplayBrightness(this.mPhysicalDisplayId);
                this.mInfo.backlightType = this.mCurrentBacklightType;
                this.mInfo.hdrSdrRatio = this.mCurrentHdrSdrRatio;
            }
            return this.mInfo;
        }

        @Override // com.android.server.display.DisplayDevice
        public java.lang.Runnable requestDisplayStateLocked(final int state, final float brightnessState, final float sdrBrightnessState, final com.android.server.display.DisplayOffloadSessionImpl displayOffloadSession) {
            boolean z = true;
            boolean stateChanged = this.mState != state;
            if (com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mBrightnessState, brightnessState) && com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mSdrBrightnessState, sdrBrightnessState)) {
                z = false;
            }
            final boolean brightnessChanged = z;
            if (stateChanged || brightnessChanged) {
                final long physicalDisplayId = this.mPhysicalDisplayId;
                final android.os.IBinder token = getDisplayTokenLocked();
                final int oldState = this.mState;
                if (stateChanged) {
                    this.mState = state;
                    getExtImpl().requestDisplayStateChanged(state, brightnessState, sdrBrightnessState);
                    updateDeviceInfoLocked(android.os.SystemClock.elapsedRealtime());
                }
                return new java.lang.Runnable() { // from class: com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.1
                    @Override // java.lang.Runnable
                    public void run() throws java.lang.Throwable {
                        int currentState = oldState;
                        if (android.view.Display.isSuspendedState(oldState) || oldState == 0) {
                            if (!android.view.Display.isSuspendedState(state)) {
                                setDisplayState(state);
                                currentState = state;
                            } else if (state == 4 || oldState == 4) {
                                setDisplayState(3);
                                currentState = 3;
                            } else if (state == 6 || oldState == 6) {
                                setDisplayState(2);
                                currentState = 2;
                            } else if (oldState != 0) {
                                return;
                            }
                        }
                        boolean isLongTakeDozeToOn = com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().isLongTakeAodToOn(currentState, state, com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mPhysicalDisplayId);
                        if (isLongTakeDozeToOn && state != currentState) {
                            setDisplayState(state);
                        }
                        if (brightnessChanged) {
                            setDisplayBrightness(brightnessState, sdrBrightnessState);
                            com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mBrightnessState = brightnessState;
                            com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mSdrBrightnessState = sdrBrightnessState;
                        }
                        if (!isLongTakeDozeToOn && state != currentState) {
                            setDisplayState(state);
                        }
                    }

                    private void setDisplayState(int state2) {
                        if (com.android.server.display.LocalDisplayAdapter.DEBUG || com.android.server.display.LocalDisplayAdapter.MTK_DEBUG) {
                            android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "setDisplayState(id=" + physicalDisplayId + ", state=" + android.view.Display.stateToString(state2) + ")");
                        }
                        boolean isDisplayOffloadEnabled = com.android.server.display.LocalDisplayAdapter.this.getFeatureFlags().isDisplayOffloadEnabled();
                        if (isDisplayOffloadEnabled) {
                            if (displayOffloadSession != null && !android.hardware.display.DisplayManagerInternal.DisplayOffloadSession.isSupportedOffloadState(state2)) {
                                displayOffloadSession.stopOffload();
                            }
                        } else if (com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mSidekickActive) {
                            android.os.Trace.traceBegin(131072L, "SidekickInternal#endDisplayControl");
                            try {
                                com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mSidekickInternal.endDisplayControl();
                                android.os.Trace.traceEnd(131072L);
                                com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mSidekickActive = false;
                            } finally {
                            }
                        }
                        int mode = com.android.server.display.LocalDisplayAdapter.getPowerModeForState(state2);
                        android.os.Trace.traceBegin(131072L, "setDisplayState(id=" + physicalDisplayId + ", state=" + android.view.Display.stateToString(state2) + ")");
                        com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setSwitchingTrackerPowerEventLog(state2, true);
                        try {
                            com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().requestDisplayState(com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mIsFirstDisplay, state2);
                            com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.getExtImpl().setDisplayState(oldState, state2);
                            com.android.server.display.LocalDisplayAdapter.this.mSurfaceControlProxy.setDisplayPowerMode(token, mode);
                            com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setDisplayPowerModeFinished(com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mIsFirstDisplay, state2);
                            android.os.Trace.traceCounter(131072L, "DisplayPowerMode", mode);
                            android.os.Trace.traceEnd(131072L);
                            com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setSwitchingTrackerPowerEventLog(state2, false);
                            setCommittedState(state2);
                            if (isDisplayOffloadEnabled) {
                                if (displayOffloadSession != null && android.hardware.display.DisplayManagerInternal.DisplayOffloadSession.isSupportedOffloadState(state2)) {
                                    displayOffloadSession.startOffload();
                                    return;
                                }
                                return;
                            }
                            if (android.view.Display.isSuspendedState(state2) && state2 != 1 && com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mSidekickInternal != null && !com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mSidekickActive) {
                                android.os.Trace.traceBegin(131072L, "SidekickInternal#startDisplayControl");
                                try {
                                    com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mSidekickActive = com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mSidekickInternal.startDisplayControl(state2);
                                } finally {
                                }
                            }
                        } catch (java.lang.Throwable th) {
                            android.os.Trace.traceEnd(131072L);
                            com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setSwitchingTrackerPowerEventLog(state2, false);
                            throw th;
                        }
                    }

                    private void setCommittedState(int state2) {
                        synchronized (com.android.server.display.LocalDisplayAdapter.this.getSyncRoot()) {
                            com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mCommittedState = state2;
                            com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.updateDeviceInfoLocked(android.os.SystemClock.elapsedRealtime());
                        }
                    }

                    /* JADX WARN: Can't wrap try/catch for region: R(28:7|(1:9)(1:10)|11|(1:13)|14|137|15|(3:125|17|18)(1:21)|22|23|129|24|25|(2:149|26)|(5:123|28|(1:34)(12:35|139|36|37|145|48|141|(3:151|50|(6:147|52|53|133|54|55)(5:60|143|61|62|63))(4:69|135|70|(1:72)(1:76))|77|(1:79)|80|(9:82|(3:86|(4:88|89|131|90)(1:91)|92)(1:85)|93|94|127|95|(2:97|(1:99))(1:101)|102|103)(0))|119|120)(1:44)|45|46|47|145|48|141|(0)(0)|77|(0)|80|(0)(0)|119|120) */
                    /* JADX WARN: Code restructure failed: missing block: B:110:0x04f0, code lost:
                    
                        r0 = th;
                     */
                    /* JADX WARN: Code restructure failed: missing block: B:111:0x04f1, code lost:
                    
                        r14 = 131072;
                     */
                    /* JADX WARN: Code restructure failed: missing block: B:74:0x036f, code lost:
                    
                        r0 = th;
                     */
                    /* JADX WARN: Code restructure failed: missing block: B:75:0x0370, code lost:
                    
                        r14 = 131072;
                     */
                    /* JADX WARN: Removed duplicated region for block: B:151:0x0288 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                    /* JADX WARN: Removed duplicated region for block: B:69:0x0328  */
                    /* JADX WARN: Removed duplicated region for block: B:79:0x039b A[Catch: all -> 0x036f, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x036f, blocks: (B:79:0x039b, B:82:0x03b6, B:63:0x030b, B:72:0x0351), top: B:141:0x0286 }] */
                    /* JADX WARN: Removed duplicated region for block: B:82:0x03b6 A[Catch: all -> 0x036f, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x036f, blocks: (B:79:0x039b, B:82:0x03b6, B:63:0x030b, B:72:0x0351), top: B:141:0x0286 }] */
                    /* JADX WARN: Removed duplicated region for block: B:86:0x03cd A[Catch: all -> 0x04e9, TRY_ENTER, TryCatch #6 {all -> 0x04e9, blocks: (B:77:0x0393, B:80:0x03ab, B:86:0x03cd, B:88:0x0444, B:70:0x033d, B:76:0x0377), top: B:135:0x033d }] */
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    private void setDisplayBrightness(float r49, float r50) throws java.lang.Throwable {
                        /*
                            Method dump skipped, instruction units count: 1305
                            To view this dump add '--comments-level debug' option
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.AnonymousClass1.setDisplayBrightness(float, float):void");
                    }

                    private float brightnessToBacklight(float brightness) {
                        if (com.android.internal.display.BrightnessSynchronizer.floatEquals(brightness, -1.0f)) {
                            return -1.0f;
                        }
                        return com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.getDisplayDeviceConfig().getBacklightFromBrightness(brightness);
                    }

                    private float backlightToNits(float backlight) {
                        return com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.getDisplayDeviceConfig().getNitsFromBacklight(backlight);
                    }

                    void handleHdrSdrNitsChanged(float displayNits, float sdrNits) {
                        float newHdrSdrRatio;
                        if (displayNits != -1.0f && sdrNits != -1.0f) {
                            newHdrSdrRatio = java.lang.Math.max(1.0f, displayNits / sdrNits);
                        } else {
                            newHdrSdrRatio = Float.NaN;
                        }
                        if (!com.android.internal.display.BrightnessSynchronizer.floatEquals(com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mCurrentHdrSdrRatio, newHdrSdrRatio)) {
                            com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().notifyBacklightAnimFinished(newHdrSdrRatio);
                            synchronized (com.android.server.display.LocalDisplayAdapter.this.getSyncRoot()) {
                                com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mCurrentHdrSdrRatio = newHdrSdrRatio;
                                com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.updateDeviceInfoLocked();
                            }
                        }
                    }

                    private void applyColorMatrixBasedDimming(float brightnessState2) {
                        int strength = (int) (((double) android.util.MathUtils.constrainedMap(com.android.server.display.LocalDisplayAdapter.EVEN_DIMMER_MAX_STRENGTH, 0.0f, 0.0f, com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.this.mDisplayDeviceConfig.getEvenDimmerTransitionPoint(), brightnessState2)) + 0.5d);
                        if (com.android.server.display.LocalDisplayAdapter.this.mEvenDimmerStrength < 0 || android.util.MathUtils.abs(com.android.server.display.LocalDisplayAdapter.this.mEvenDimmerStrength - strength) > 1.0f || strength <= 1) {
                            com.android.server.display.LocalDisplayAdapter.this.mEvenDimmerStrength = strength;
                        }
                        boolean enabled = ((float) com.android.server.display.LocalDisplayAdapter.this.mEvenDimmerStrength) > 0.0f;
                        if (com.android.server.display.LocalDisplayAdapter.this.mCdsi == null) {
                            com.android.server.display.LocalDisplayAdapter.this.mCdsi = (com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal) com.android.server.LocalServices.getService(com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal.class);
                        }
                        if (com.android.server.display.LocalDisplayAdapter.this.mCdsi != null) {
                            com.android.server.display.LocalDisplayAdapter.this.mCdsi.applyEvenDimmerColorChanges(enabled, strength);
                        }
                    }
                };
            }
            return null;
        }

        @Override // com.android.server.display.DisplayDevice
        public void setUserPreferredDisplayModeLocked(android.view.Display.Mode mode) {
            android.view.Display.Mode matchingSupportedMode;
            int oldModeId = getPreferredModeId();
            this.mUserPreferredMode = mode;
            if (mode == null && this.mSystemPreferredModeId != -1) {
                this.mDefaultModeId = this.mSystemPreferredModeId;
            }
            if (mode != null && ((mode.isRefreshRateSet() || mode.isResolutionSet()) && (matchingSupportedMode = findMode(mode.getPhysicalWidth(), mode.getPhysicalHeight(), mode.getRefreshRate())) != null)) {
                this.mUserPreferredMode = matchingSupportedMode;
            }
            this.mUserPreferredModeId = findUserPreferredModeIdLocked(this.mUserPreferredMode);
            if (oldModeId == getPreferredModeId()) {
                return;
            }
            updateDeviceInfoLocked();
            if (com.android.server.display.LocalDisplayAdapter.this.mIsBootDisplayModeSupported) {
                if (this.mUserPreferredModeId == -1) {
                    com.android.server.display.LocalDisplayAdapter.this.mSurfaceControlProxy.clearBootDisplayMode(getDisplayTokenLocked());
                } else {
                    int preferredSfDisplayModeId = findSfDisplayModeIdLocked(this.mUserPreferredMode.getModeId(), this.mDefaultModeGroup);
                    com.android.server.display.LocalDisplayAdapter.this.mSurfaceControlProxy.setBootDisplayMode(getDisplayTokenLocked(), preferredSfDisplayModeId);
                }
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public android.view.Display.Mode getUserPreferredDisplayModeLocked() {
            return this.mUserPreferredMode;
        }

        @Override // com.android.server.display.DisplayDevice
        public android.view.Display.Mode getSystemPreferredDisplayModeLocked() {
            return findMode(this.mSystemPreferredModeId);
        }

        @Override // com.android.server.display.DisplayDevice
        public void setRequestedColorModeLocked(int colorMode) {
            requestColorModeLocked(colorMode);
        }

        @Override // com.android.server.display.DisplayDevice
        public void setDesiredDisplayModeSpecsLocked(com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs displayModeSpecs) {
            if (displayModeSpecs.baseModeId == 0) {
                return;
            }
            int baseSfModeId = findSfDisplayModeIdLocked(displayModeSpecs.baseModeId, this.mDefaultModeGroup);
            if (baseSfModeId < 0) {
                android.util.Slog.w(com.android.server.display.LocalDisplayAdapter.TAG, "Ignoring request for invalid base mode id " + displayModeSpecs.baseModeId);
                updateDeviceInfoLocked();
                return;
            }
            int baseSfModeId2 = com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().findDisplayModeIdByPolicy(this.mIsFirstDisplay, displayModeSpecs.vrrPolicy, 0, baseSfModeId);
            if (this.mDisplayModeSpecsInvalid || !displayModeSpecs.equals(this.mDisplayModeSpecs)) {
                this.mDisplayModeSpecsInvalid = false;
                this.mDisplayModeSpecs.copyFrom(displayModeSpecs);
                com.android.server.display.LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getOPlusRefreshRateHandler(com.android.server.display.LocalDisplayAdapter.this.getHandler()).sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.display.LocalDisplayAdapter$LocalDisplayDevice$$ExternalSyntheticLambda0
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice) obj).setDesiredDisplayModeSpecsAsync((android.os.IBinder) obj2, (android.view.SurfaceControl.DesiredDisplayModeSpecs) obj3);
                    }
                }, this, getDisplayTokenLocked(), new android.view.SurfaceControl.DesiredDisplayModeSpecs(baseSfModeId2, this.mDisplayModeSpecs.allowGroupSwitching, this.mDisplayModeSpecs.primary, this.mDisplayModeSpecs.appRequest, this.mDisplayModeSpecs.mIdleScreenRefreshRateConfig)));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDesiredDisplayModeSpecsAsync(android.os.IBinder displayToken, android.view.SurfaceControl.DesiredDisplayModeSpecs modeSpecs) {
            android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "setDesiredDisplayModeSpecsAsync: display unique id = " + getUniqueId() + ", display mode specs = " + modeSpecs);
            com.android.server.display.LocalDisplayAdapter.this.mSurfaceControlProxy.setDesiredDisplayModeSpecs(displayToken, modeSpecs);
        }

        @Override // com.android.server.display.DisplayDevice
        public void onOverlayChangedLocked() {
            updateDeviceInfoLocked();
        }

        public void onActiveDisplayModeChangedLocked(int sfModeId, float renderFrameRate) {
            if (updateActiveModeLocked(sfModeId, renderFrameRate)) {
                updateDeviceInfoLocked();
            }
        }

        public void onFrameRateOverridesChanged(android.view.DisplayEventReceiver.FrameRateOverride[] overrides) {
            if (updateFrameRateOverridesLocked(overrides)) {
                updateDeviceInfoLocked();
            }
        }

        public void onHdcpLevelsChangedLocked(int connectedLevel, int maxLevel) {
            if (updateHdcpLevelsLocked(connectedLevel, maxLevel)) {
                updateDeviceInfoLocked();
            }
        }

        public boolean updateActiveModeLocked(int activeSfModeId, float renderFrameRate) {
            if (this.mActiveSfDisplayMode.id == activeSfModeId && this.mActiveRenderFrameRate == renderFrameRate) {
                return false;
            }
            this.mActiveSfDisplayMode = getModeById(this.mSfDisplayModes, activeSfModeId);
            this.mActiveModeId = findMatchingModeIdLocked(activeSfModeId);
            if (this.mActiveModeId == -1) {
                android.util.Slog.w(com.android.server.display.LocalDisplayAdapter.TAG, "In unknown mode after setting allowed modes, activeModeId=" + activeSfModeId);
            }
            this.mActiveRenderFrameRate = renderFrameRate;
            return true;
        }

        public boolean updateFrameRateOverridesLocked(android.view.DisplayEventReceiver.FrameRateOverride[] overrides) {
            if (java.util.Arrays.equals(overrides, this.mFrameRateOverrides)) {
                return false;
            }
            this.mFrameRateOverrides = overrides;
            return true;
        }

        public boolean updateHdcpLevelsLocked(int connectedLevel, int maxLevel) {
            if (connectedLevel > maxLevel) {
                android.util.Slog.w(com.android.server.display.LocalDisplayAdapter.TAG, "HDCP connected level: " + connectedLevel + " is larger than max level: " + maxLevel + ", ignoring request.");
                return false;
            }
            if (this.mConnectedHdcpLevel == connectedLevel) {
                return false;
            }
            this.mConnectedHdcpLevel = connectedLevel;
            return true;
        }

        public void requestColorModeLocked(int colorMode) {
            if (this.mActiveColorMode == colorMode) {
                return;
            }
            if (!this.mSupportedColorModes.contains(java.lang.Integer.valueOf(colorMode))) {
                android.util.Slog.w(com.android.server.display.LocalDisplayAdapter.TAG, "Unable to find color mode " + colorMode + ", ignoring request.");
            } else {
                this.mActiveColorMode = colorMode;
                com.android.server.display.LocalDisplayAdapter.this.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.display.LocalDisplayAdapter$LocalDisplayDevice$$ExternalSyntheticLambda1
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice) obj).requestColorModeAsync((android.os.IBinder) obj2, ((java.lang.Integer) obj3).intValue());
                    }
                }, this, getDisplayTokenLocked(), java.lang.Integer.valueOf(colorMode)));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void requestColorModeAsync(android.os.IBinder displayToken, int colorMode) {
            com.android.server.display.LocalDisplayAdapter.this.mSurfaceControlProxy.setActiveColorMode(displayToken, colorMode);
            synchronized (com.android.server.display.LocalDisplayAdapter.this.getSyncRoot()) {
                updateDeviceInfoLocked();
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public void setAutoLowLatencyModeLocked(boolean on) {
            if (this.mAllmRequested == on) {
                return;
            }
            this.mAllmRequested = on;
            if (!this.mAllmSupported) {
                android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "Unable to set ALLM because the connected display does not support ALLM.");
            } else {
                com.android.server.display.LocalDisplayAdapter.this.mSurfaceControlProxy.setAutoLowLatencyMode(getDisplayTokenLocked(), on);
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public void setGameContentTypeLocked(boolean on) {
            if (this.mGameContentTypeRequested == on) {
                return;
            }
            this.mGameContentTypeRequested = on;
            com.android.server.display.LocalDisplayAdapter.this.mSurfaceControlProxy.setGameContentType(getDisplayTokenLocked(), on);
        }

        @Override // com.android.server.display.DisplayDevice
        public void dumpLocked(java.io.PrintWriter pw) {
            super.dumpLocked(pw);
            pw.println("mPhysicalDisplayId=" + this.mPhysicalDisplayId);
            pw.println("mDisplayModeSpecs={" + this.mDisplayModeSpecs + "}");
            pw.println("mDisplayModeSpecsInvalid=" + this.mDisplayModeSpecsInvalid);
            pw.println("mActiveModeId=" + this.mActiveModeId);
            pw.println("mActiveColorMode=" + this.mActiveColorMode);
            pw.println("mDefaultModeId=" + this.mDefaultModeId);
            pw.println("mUserPreferredModeId=" + this.mUserPreferredModeId);
            pw.println("mState=" + android.view.Display.stateToString(this.mState));
            pw.println("mCommittedState=" + android.view.Display.stateToString(this.mCommittedState));
            pw.println("mBrightnessState=" + this.mBrightnessState);
            pw.println("mBacklightAdapter=" + this.mBacklightAdapter);
            pw.println("mAllmSupported=" + this.mAllmSupported);
            pw.println("mAllmRequested=" + this.mAllmRequested);
            pw.println("mGameContentTypeSupported=" + this.mGameContentTypeSupported);
            pw.println("mGameContentTypeRequested=" + this.mGameContentTypeRequested);
            pw.println("mStaticDisplayInfo=" + this.mStaticDisplayInfo);
            pw.println("mSfDisplayModes=");
            for (android.view.SurfaceControl.DisplayMode sfDisplayMode : this.mSfDisplayModes) {
                pw.println("  " + sfDisplayMode);
            }
            pw.println("mActiveSfDisplayMode=" + this.mActiveSfDisplayMode);
            pw.println("mActiveRenderFrameRate=" + this.mActiveRenderFrameRate);
            pw.println("mSupportedModes=");
            for (int i = 0; i < this.mSupportedModes.size(); i++) {
                pw.println("  " + this.mSupportedModes.valueAt(i));
            }
            pw.println("mSupportedColorModes=" + this.mSupportedColorModes);
            pw.println("mDisplayDeviceConfig=" + this.mDisplayDeviceConfig);
        }

        private int findSfDisplayModeIdLocked(int displayModeId, int modeGroup) {
            int matchingSfDisplayModeId = -1;
            com.android.server.display.LocalDisplayAdapter.DisplayModeRecord record = this.mSupportedModes.get(displayModeId);
            if (record != null) {
                for (android.view.SurfaceControl.DisplayMode mode : this.mSfDisplayModes) {
                    if (record.hasMatchingMode(mode)) {
                        if (matchingSfDisplayModeId == -1) {
                            matchingSfDisplayModeId = mode.id;
                        }
                        if (mode.group == modeGroup) {
                            return mode.id;
                        }
                    }
                }
            }
            return matchingSfDisplayModeId;
        }

        private android.view.Display.Mode findMode(int modeId) {
            for (int i = 0; i < this.mSupportedModes.size(); i++) {
                android.view.Display.Mode supportedMode = this.mSupportedModes.valueAt(i).mMode;
                if (supportedMode.getModeId() == modeId) {
                    return supportedMode;
                }
            }
            return null;
        }

        private android.view.Display.Mode findMode(int width, int height, float refreshRate) {
            for (int i = 0; i < this.mSupportedModes.size(); i++) {
                android.view.Display.Mode supportedMode = this.mSupportedModes.valueAt(i).mMode;
                if (supportedMode.matchesIfValid(width, height, refreshRate)) {
                    return supportedMode;
                }
            }
            return null;
        }

        private int findUserPreferredModeIdLocked(android.view.Display.Mode userPreferredMode) {
            if (userPreferredMode != null) {
                for (int i = 0; i < this.mSupportedModes.size(); i++) {
                    android.view.Display.Mode supportedMode = this.mSupportedModes.valueAt(i).mMode;
                    if (userPreferredMode.matches(supportedMode.getPhysicalWidth(), supportedMode.getPhysicalHeight(), supportedMode.getRefreshRate())) {
                        return supportedMode.getModeId();
                    }
                }
                return -1;
            }
            return -1;
        }

        private int findMatchingModeIdLocked(int sfModeId) {
            android.view.SurfaceControl.DisplayMode mode = getModeById(this.mSfDisplayModes, sfModeId);
            if (mode == null) {
                android.util.Slog.e(com.android.server.display.LocalDisplayAdapter.TAG, "Invalid display mode ID " + sfModeId);
                return -1;
            }
            for (int i = 0; i < this.mSupportedModes.size(); i++) {
                com.android.server.display.LocalDisplayAdapter.DisplayModeRecord record = this.mSupportedModes.valueAt(i);
                if (record.hasMatchingMode(mode)) {
                    return record.mMode.getModeId();
                }
            }
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateDeviceInfoLocked() {
            this.mInfo = null;
            com.android.server.display.LocalDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateDeviceInfoLocked(long timestamp) {
            this.mInfo = null;
            com.android.server.display.LocalDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2, timestamp);
        }

        private android.view.Display.Mode[] getDisplayModes(android.util.SparseArray<com.android.server.display.LocalDisplayAdapter.DisplayModeRecord> records) {
            int size = records.size();
            android.view.Display.Mode[] modes = new android.view.Display.Mode[size];
            for (int i = 0; i < size; i++) {
                com.android.server.display.LocalDisplayAdapter.DisplayModeRecord record = records.valueAt(i);
                modes[i] = record.mMode;
            }
            return modes;
        }

        private boolean isDisplayPrivate(android.view.DisplayAddress.Physical physicalAddress) {
            if (physicalAddress == null) {
                return false;
            }
            android.content.res.Resources res = com.android.server.display.LocalDisplayAdapter.this.getOverlayContext().getResources();
            int[] ports = res.getIntArray(android.R.array.config_integrityRuleProviderPackages);
            if (ports != null) {
                int port = physicalAddress.getPort();
                for (int p : ports) {
                    if (p == port) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override // com.android.server.display.DisplayDevice
        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(super.toString());
            sb.append(",isFirst=").append(this.mIsFirstDisplay).append(",activeMode=").append(this.mActiveModeId);
            sb.append(",state=").append(android.view.Display.stateToString(this.mState));
            sb.append(",commitState=").append(android.view.Display.stateToString(this.mCommittedState));
            sb.append(",devInfo:").append(this.mInfo);
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setIrisBrightness(float brightnessState) {
            if (com.android.server.display.LocalDisplayAdapter.this.mIrisHal != null && com.android.server.display.LocalDisplayAdapter.this.mHasSoftIris && !com.android.server.display.LocalDisplayAdapter.this.mUseHWCbacklight) {
                int level = (int) brightnessState;
                com.android.server.display.LocalDisplayAdapter.IrisTable irisTable = com.android.server.display.LocalDisplayAdapter.IrisTable.LOWLEVEL;
                com.android.server.display.LocalDisplayAdapter.IrisTable irisTable2 = com.android.server.display.LocalDisplayAdapter.IrisTable.LOWLEVEL;
                boolean needfresh = false;
                if (com.android.server.display.LocalDisplayAdapter.this.mlast_level == -1) {
                    com.android.server.display.LocalDisplayAdapter.this.mlast_level = level;
                    needfresh = true;
                }
                if (!needfresh) {
                    com.android.server.display.LocalDisplayAdapter.IrisTable current_table = getIrisBrightnessTable(level);
                    com.android.server.display.LocalDisplayAdapter.IrisTable last_table = getIrisBrightnessTable(com.android.server.display.LocalDisplayAdapter.this.mlast_level);
                    if (current_table != last_table) {
                        com.android.server.display.LocalDisplayAdapter.this.mlast_level = level;
                        needfresh = true;
                    } else if ((current_table == com.android.server.display.LocalDisplayAdapter.IrisTable.LOWLEVEL || current_table == com.android.server.display.LocalDisplayAdapter.IrisTable.HIGHTLEVEL) && java.lang.Math.abs(level - com.android.server.display.LocalDisplayAdapter.this.mlast_level) >= 12) {
                        com.android.server.display.LocalDisplayAdapter.this.mlast_level = level;
                        needfresh = true;
                    }
                }
                if (needfresh) {
                    int[] values = {level, com.android.server.display.LocalDisplayAdapter.this.mIris514ext};
                    com.android.server.display.LocalDisplayAdapter.this.mIrisHal.irisConfigureSet(com.android.server.display.LocalDisplayAdapter.this.mIris514command, values);
                }
            }
        }

        private com.android.server.display.LocalDisplayAdapter.IrisTable getIrisBrightnessTable(int value) {
            com.android.server.display.LocalDisplayAdapter.IrisTable irisTable = com.android.server.display.LocalDisplayAdapter.IrisTable.LOWLEVEL;
            if (value < 2500) {
                com.android.server.display.LocalDisplayAdapter.IrisTable ret = com.android.server.display.LocalDisplayAdapter.IrisTable.LOWLEVEL;
                return ret;
            }
            if (value > 2500 && value < 4334) {
                com.android.server.display.LocalDisplayAdapter.IrisTable ret2 = com.android.server.display.LocalDisplayAdapter.IrisTable.MEDIUMLEVEL;
                return ret2;
            }
            com.android.server.display.LocalDisplayAdapter.IrisTable ret3 = com.android.server.display.LocalDisplayAdapter.IrisTable.HIGHTLEVEL;
            return ret3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hdrTypesEqual(int[] modeHdrTypes, int[] recordHdrTypes) {
        int[] modeHdrTypesCopy = java.util.Arrays.copyOf(modeHdrTypes, modeHdrTypes.length);
        java.util.Arrays.sort(modeHdrTypesCopy);
        return java.util.Arrays.equals(modeHdrTypesCopy, recordHdrTypes);
    }

    android.content.Context getOverlayContext() {
        if (this.mOverlayContext == null) {
            this.mOverlayContext = android.app.ActivityThread.currentActivityThread().getSystemUiContext();
        }
        return this.mOverlayContext;
    }

    private static final class DisplayModeRecord {
        public final android.view.Display.Mode mMode;

        DisplayModeRecord(android.view.SurfaceControl.DisplayMode mode, float[] alternativeRefreshRates) {
            this.mMode = com.android.server.display.DisplayAdapter.createMode(mode.width, mode.height, mode.peakRefreshRate, mode.vsyncRate, alternativeRefreshRates, mode.supportedHdrTypes);
        }

        public boolean hasMatchingMode(android.view.SurfaceControl.DisplayMode mode) {
            return this.mMode.getPhysicalWidth() == mode.width && this.mMode.getPhysicalHeight() == mode.height && java.lang.Float.floatToIntBits(this.mMode.getRefreshRate()) == java.lang.Float.floatToIntBits(mode.peakRefreshRate) && java.lang.Float.floatToIntBits(this.mMode.getVsyncRate()) == java.lang.Float.floatToIntBits(mode.vsyncRate);
        }

        public java.lang.String toString() {
            return "DisplayModeRecord{mMode=" + this.mMode + "}";
        }
    }

    public static class Injector {
        private com.android.server.display.LocalDisplayAdapter.ProxyDisplayEventReceiver mReceiver;

        public void setDisplayEventListenerLocked(android.os.Looper looper, com.android.server.display.LocalDisplayAdapter.DisplayEventListener listener) {
            this.mReceiver = new com.android.server.display.LocalDisplayAdapter.ProxyDisplayEventReceiver(looper, listener);
        }

        public com.android.server.display.LocalDisplayAdapter.SurfaceControlProxy getSurfaceControlProxy() {
            return new com.android.server.display.LocalDisplayAdapter.SurfaceControlProxy();
        }

        public com.android.server.display.DisplayDeviceConfig createDisplayDeviceConfig(android.content.Context context, long physicalDisplayId, boolean isFirstDisplay, com.android.server.display.feature.DisplayManagerFlags flags) {
            return com.android.server.display.DisplayDeviceConfig.create(context, physicalDisplayId, isFirstDisplay, flags);
        }
    }

    public static final class ProxyDisplayEventReceiver extends android.view.DisplayEventReceiver {
        private final com.android.server.display.LocalDisplayAdapter.DisplayEventListener mListener;

        ProxyDisplayEventReceiver(android.os.Looper looper, com.android.server.display.LocalDisplayAdapter.DisplayEventListener listener) {
            super(looper, 0, 3);
            this.mListener = listener;
        }

        public void onHotplug(long timestampNanos, long physicalDisplayId, boolean connected) {
            this.mListener.onHotplug(timestampNanos, physicalDisplayId, connected);
        }

        public void onHotplugConnectionError(long timestampNanos, int errorCode) {
            this.mListener.onHotplugConnectionError(timestampNanos, errorCode);
        }

        public void onModeChanged(long timestampNanos, long physicalDisplayId, int modeId, long renderPeriod) {
            this.mListener.onModeChanged(timestampNanos, physicalDisplayId, modeId, renderPeriod);
        }

        public void onFrameRateOverridesChanged(long timestampNanos, long physicalDisplayId, android.view.DisplayEventReceiver.FrameRateOverride[] overrides) {
            this.mListener.onFrameRateOverridesChanged(timestampNanos, physicalDisplayId, overrides);
        }

        public void onHdcpLevelsChanged(long physicalDisplayId, int connectedLevel, int maxLevel) {
            this.mListener.onHdcpLevelsChanged(physicalDisplayId, connectedLevel, maxLevel);
        }
    }

    private final class LocalDisplayEventListener implements com.android.server.display.LocalDisplayAdapter.DisplayEventListener {
        private LocalDisplayEventListener() {
        }

        @Override // com.android.server.display.LocalDisplayAdapter.DisplayEventListener
        public void onHotplug(long timestampNanos, long physicalDisplayId, boolean connected) {
            if (com.android.server.display.LocalDisplayAdapter.DEBUG || com.android.server.display.LocalDisplayAdapter.PANIC_DEBUG) {
                android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "onHotplug connected=" + connected + " id=" + physicalDisplayId);
            }
            synchronized (com.android.server.display.LocalDisplayAdapter.this.getSyncRoot()) {
                if (connected) {
                    com.android.server.display.LocalDisplayAdapter.this.tryConnectDisplayLocked(physicalDisplayId);
                } else {
                    com.android.server.display.LocalDisplayAdapter.this.tryDisconnectDisplayLocked(physicalDisplayId);
                }
            }
        }

        @Override // com.android.server.display.LocalDisplayAdapter.DisplayEventListener
        public void onHotplugConnectionError(long timestampNanos, int connectionError) {
            if (com.android.server.display.LocalDisplayAdapter.DEBUG) {
                android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "onHotplugConnectionError(timestampNanos=" + timestampNanos + ", connectionError=" + connectionError + ")");
            }
            com.android.server.display.LocalDisplayAdapter.this.mDisplayNotificationManager.onHotplugConnectionError();
        }

        @Override // com.android.server.display.LocalDisplayAdapter.DisplayEventListener
        public void onModeChanged(long timestampNanos, long physicalDisplayId, int modeId, long renderPeriod) {
            if (com.android.server.display.LocalDisplayAdapter.DEBUG || com.android.server.display.LocalDisplayAdapter.PANIC_DEBUG) {
                android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "onModeChanged(timestampNanos=" + timestampNanos + ", physicalDisplayId=" + physicalDisplayId + ", modeId=" + modeId + ", renderPeriod=" + renderPeriod + ")");
            }
            synchronized (com.android.server.display.LocalDisplayAdapter.this.getSyncRoot()) {
                com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice device = (com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice) com.android.server.display.LocalDisplayAdapter.this.mDevices.get(physicalDisplayId);
                if (device == null) {
                    if (com.android.server.display.LocalDisplayAdapter.DEBUG) {
                        android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "Received mode change for unhandled physical display: physicalDisplayId=" + physicalDisplayId);
                    }
                } else {
                    float renderFrameRate = 1.0E9f / renderPeriod;
                    device.onActiveDisplayModeChangedLocked(modeId, renderFrameRate);
                }
            }
        }

        @Override // com.android.server.display.LocalDisplayAdapter.DisplayEventListener
        public void onFrameRateOverridesChanged(long timestampNanos, long physicalDisplayId, android.view.DisplayEventReceiver.FrameRateOverride[] overrides) {
            if (com.android.server.display.LocalDisplayAdapter.DEBUG || com.android.server.display.LocalDisplayAdapter.PANIC_DEBUG) {
                android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "onFrameRateOverrideChanged(timestampNanos=" + timestampNanos + ", physicalDisplayId=" + physicalDisplayId + " overrides=" + java.util.Arrays.toString(overrides) + ")");
            }
            synchronized (com.android.server.display.LocalDisplayAdapter.this.getSyncRoot()) {
                com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice device = (com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice) com.android.server.display.LocalDisplayAdapter.this.mDevices.get(physicalDisplayId);
                if (device == null) {
                    if (com.android.server.display.LocalDisplayAdapter.DEBUG) {
                        android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "Received frame rate override event for unhandled physical display: physicalDisplayId=" + physicalDisplayId);
                    }
                } else {
                    device.onFrameRateOverridesChanged(overrides);
                }
            }
        }

        @Override // com.android.server.display.LocalDisplayAdapter.DisplayEventListener
        public void onHdcpLevelsChanged(long physicalDisplayId, int connectedLevel, int maxLevel) {
            if (com.android.server.display.LocalDisplayAdapter.DEBUG) {
                android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "onHdcpLevelsChanged(physicalDisplayId=" + physicalDisplayId + ", connectedLevel=" + connectedLevel + ", maxLevel=" + maxLevel + ")");
            }
            synchronized (com.android.server.display.LocalDisplayAdapter.this.getSyncRoot()) {
                com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice device = (com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice) com.android.server.display.LocalDisplayAdapter.this.mDevices.get(physicalDisplayId);
                if (device == null) {
                    if (com.android.server.display.LocalDisplayAdapter.DEBUG) {
                        android.util.Slog.d(com.android.server.display.LocalDisplayAdapter.TAG, "Received hdcp levels change for unhandled physical display: physicalDisplayId=" + physicalDisplayId);
                    }
                } else {
                    device.onHdcpLevelsChangedLocked(connectedLevel, maxLevel);
                }
            }
        }
    }

    public static class SurfaceControlProxy {
        public android.view.SurfaceControl.DynamicDisplayInfo getDynamicDisplayInfo(long displayId) {
            return android.view.SurfaceControl.getDynamicDisplayInfo(displayId);
        }

        public long[] getPhysicalDisplayIds() {
            return com.android.server.display.DisplayControl.getPhysicalDisplayIds();
        }

        public android.os.IBinder getPhysicalDisplayToken(long physicalDisplayId) {
            return com.android.server.display.DisplayControl.getPhysicalDisplayToken(physicalDisplayId);
        }

        public android.view.SurfaceControl.StaticDisplayInfo getStaticDisplayInfo(long displayId) {
            return android.view.SurfaceControl.getStaticDisplayInfo(displayId);
        }

        public android.view.SurfaceControl.DesiredDisplayModeSpecs getDesiredDisplayModeSpecs(android.os.IBinder displayToken) {
            return android.view.SurfaceControl.getDesiredDisplayModeSpecs(displayToken);
        }

        public boolean setDesiredDisplayModeSpecs(android.os.IBinder token, android.view.SurfaceControl.DesiredDisplayModeSpecs specs) {
            return android.view.SurfaceControl.setDesiredDisplayModeSpecs(token, specs);
        }

        public void setDisplayPowerMode(android.os.IBinder displayToken, int mode) {
            android.view.SurfaceControl.setDisplayPowerMode(displayToken, mode);
        }

        public boolean setActiveColorMode(android.os.IBinder displayToken, int colorMode) {
            return android.view.SurfaceControl.setActiveColorMode(displayToken, colorMode);
        }

        public boolean getBootDisplayModeSupport() {
            android.os.Trace.traceBegin(32L, "getBootDisplayModeSupport");
            try {
                return android.view.SurfaceControl.getBootDisplayModeSupport();
            } finally {
                android.os.Trace.traceEnd(32L);
            }
        }

        public void setBootDisplayMode(android.os.IBinder displayToken, int modeId) {
            android.view.SurfaceControl.setBootDisplayMode(displayToken, modeId);
        }

        public void clearBootDisplayMode(android.os.IBinder displayToken) {
            android.view.SurfaceControl.clearBootDisplayMode(displayToken);
        }

        public void setAutoLowLatencyMode(android.os.IBinder displayToken, boolean on) {
            android.view.SurfaceControl.setAutoLowLatencyMode(displayToken, on);
        }

        public void setGameContentType(android.os.IBinder displayToken, boolean on) {
            android.view.SurfaceControl.setGameContentType(displayToken, on);
        }

        public boolean getDisplayBrightnessSupport(android.os.IBinder displayToken) {
            return android.view.SurfaceControl.getDisplayBrightnessSupport(displayToken);
        }

        public boolean setDisplayBrightness(android.os.IBinder displayToken, float brightness) {
            return android.view.SurfaceControl.setDisplayBrightness(displayToken, brightness);
        }

        public boolean setDisplayBrightness(android.os.IBinder displayToken, float sdrBacklight, float sdrNits, float displayBacklight, float displayNits) {
            return android.view.SurfaceControl.setDisplayBrightness(displayToken, sdrBacklight, sdrNits, displayBacklight, displayNits);
        }

        public boolean setDisplayBrightness(android.os.IBinder displayToken, float brightness, float color) {
            return android.view.SurfaceControl.setDisplayBrightness(displayToken, brightness, color);
        }

        public boolean setDisplayBrightness(android.os.IBinder displayToken, float sdrBacklight, float sdrNits, float displayBacklight, float displayNits, float color, float scale, float displayBrightnessOri, int edrType, boolean isAnimating) {
            return android.view.SurfaceControl.setDisplayBrightness(displayToken, sdrBacklight, sdrNits, displayBacklight, displayNits, color, scale, displayBrightnessOri, edrType, isAnimating);
        }

        public boolean setDisplayBrightness(android.os.IBinder displayToken, float sdrBacklight, float sdrNits, float displayBacklight, float displayNits, float color, float scale, float displayBrightnessOri, float level, int edrType, boolean isAnimating) {
            return android.view.SurfaceControl.setDisplayBrightness(displayToken, sdrBacklight, sdrNits, displayBacklight, displayNits, color, scale, displayBrightnessOri, level, edrType, isAnimating);
        }
    }

    static class BacklightAdapter {
        private final com.android.server.lights.LogicalLight mBacklight;
        private final android.os.IBinder mDisplayToken;
        private final com.android.server.display.LocalDisplayAdapter.SurfaceControlProxy mSurfaceControlProxy;
        private final boolean mUseSurfaceControlBrightness;
        private boolean mForceSurfaceControl = false;
        private int mDcThreshold = android.os.SystemProperties.getInt("ro.oplus.dc.brightness.threshold", 0);

        BacklightAdapter(android.os.IBinder displayToken, boolean isFirstDisplay, com.android.server.display.LocalDisplayAdapter.SurfaceControlProxy surfaceControlProxy) {
            this.mDisplayToken = displayToken;
            this.mSurfaceControlProxy = surfaceControlProxy;
            this.mUseSurfaceControlBrightness = this.mSurfaceControlProxy.getDisplayBrightnessSupport(this.mDisplayToken);
            if (isFirstDisplay) {
                com.android.server.lights.LightsManager lights = (com.android.server.lights.LightsManager) com.android.server.LocalServices.getService(com.android.server.lights.LightsManager.class);
                this.mBacklight = lights.getLight(0);
            } else {
                this.mBacklight = null;
            }
        }

        void setBacklight(float sdrBacklight, float sdrNits, float backlight, float nits) {
            if (this.mUseSurfaceControlBrightness || this.mForceSurfaceControl) {
                if (com.android.internal.display.BrightnessSynchronizer.floatEquals(sdrBacklight, Float.NaN)) {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, backlight);
                    return;
                } else {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, sdrBacklight, sdrNits, backlight, nits);
                    return;
                }
            }
            if (this.mBacklight != null) {
                this.mBacklight.setBrightness(backlight);
            }
        }

        void setForceSurfaceControl(boolean forceSurfaceControl) {
            this.mForceSurfaceControl = forceSurfaceControl;
        }

        void setBacklight(float sdrBacklight, float sdrNits, float backlight, float nits, float color, float scale, float displayBrightnessOri, int edrType, boolean isAnimating) {
            if (this.mUseSurfaceControlBrightness || this.mForceSurfaceControl) {
                if (com.android.internal.display.BrightnessSynchronizer.floatEquals(sdrBacklight, Float.NaN)) {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, backlight, color);
                    return;
                } else if (this.mDcThreshold == 0) {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, sdrBacklight, sdrNits, backlight, nits, color, scale, displayBrightnessOri, edrType, isAnimating);
                    return;
                } else {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, backlight, -1.0f, backlight, nits, color, scale, displayBrightnessOri, edrType, isAnimating);
                    return;
                }
            }
            if (this.mBacklight != null) {
                this.mBacklight.setBrightness(backlight);
            }
        }

        void setBacklight(float sdrBacklight, float sdrNits, float backlight, float nits, float color, float scale, float displayBrightnessOri, float level, int edrType, boolean isAnimating) {
            if (this.mUseSurfaceControlBrightness || this.mForceSurfaceControl) {
                if (com.android.internal.display.BrightnessSynchronizer.floatEquals(sdrBacklight, Float.NaN)) {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, backlight, color, scale, displayBrightnessOri);
                    return;
                } else {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, sdrBacklight, sdrNits, backlight, nits, color, scale, displayBrightnessOri, level, edrType, isAnimating);
                    return;
                }
            }
            if (this.mBacklight != null) {
                this.mBacklight.setBrightness(backlight);
            }
        }

        public java.lang.String toString() {
            return "BacklightAdapter [useSurfaceControl=" + this.mUseSurfaceControlBrightness + " (force_anyway? " + this.mForceSurfaceControl + "), backlight=" + this.mBacklight + "]";
        }
    }

    public com.android.server.display.ILocalDisplayAdapterWrapper getWrapper() {
        return this.mLdaWrapper;
    }

    private class LocalDisplayAdapterWrapper implements com.android.server.display.ILocalDisplayAdapterWrapper {
        private com.android.server.display.ILocalDisplayAdapterExt mLdaExt;

        private LocalDisplayAdapterWrapper() {
            this.mLdaExt = (com.android.server.display.ILocalDisplayAdapterExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.ILocalDisplayAdapterExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.display.ILocalDisplayAdapterExt getExtImpl() {
            return this.mLdaExt;
        }

        @Override // com.android.server.display.ILocalDisplayAdapterWrapper
        public boolean getDebug() {
            return com.android.server.display.LocalDisplayAdapter.DEBUG;
        }
    }
}
