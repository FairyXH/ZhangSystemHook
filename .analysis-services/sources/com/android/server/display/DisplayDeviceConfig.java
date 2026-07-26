package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayDeviceConfig {
    private static final int AMBIENT_LIGHT_LONG_HORIZON_MILLIS = 10000;
    private static final int AMBIENT_LIGHT_SHORT_HORIZON_MILLIS = 2000;
    static final float BRIGHTNESS_DEFAULT = 0.5f;
    private static final java.lang.String CONFIG_FILE_FORMAT = "display_%s.xml";
    private static final java.lang.String DEFAULT_CONFIG_FILE = "default.xml";
    private static final java.lang.String DEFAULT_CONFIG_FILE_WITH_UIMODE_FORMAT = "default_%s.xml";
    private static final int DEFAULT_HIGH_REFRESH_RATE = 0;
    public static final java.lang.String DEFAULT_ID = "default";
    public static final int DEFAULT_LOW_REFRESH_RATE = 60;
    private static final java.lang.String DISPLAY_CONFIG_DIR = "displayconfig";
    private static final java.lang.String ETC_DIR = "etc";
    static final float HDR_PERCENT_OF_SCREEN_REQUIRED_DEFAULT = 0.5f;
    public static final float HIGH_BRIGHTNESS_MODE_UNSUPPORTED = Float.NaN;
    private static final int INTERPOLATION_DEFAULT = 0;
    private static final int INTERPOLATION_LINEAR = 1;
    private static final int INVALID_AUTO_BRIGHTNESS_LIGHT_DEBOUNCE = -1;
    private static final float INVALID_BRIGHTNESS_IN_CONFIG = -2.0f;
    private static final java.lang.String NO_SUFFIX_FORMAT = "%d";
    private static final java.lang.String PORT_SUFFIX_FORMAT = "port_%d";
    public static final java.lang.String QUIRK_CAN_SET_BRIGHTNESS_VIA_HWC = "canSetBrightnessViaHwc";
    private static final long STABLE_FLAG = 4611686018427387904L;
    private static final java.lang.String STABLE_ID_SUFFIX_FORMAT = "id_%d";
    private com.android.server.display.config.SensorData mAmbientLightSensor;
    private float[] mBacklight;
    private android.util.Spline mBacklightToBrightnessSpline;
    private android.util.Spline mBacklightToNitsSpline;
    private float[] mBrightness;
    private float mBrightnessCapForWearBedtimeMode;
    private android.util.Spline mBrightnessToBacklightSpline;
    private final android.content.Context mContext;
    private com.android.server.display.DensityMapping mDensityMapping;
    private com.android.server.display.config.DisplayBrightnessMappingConfig mDisplayBrightnessMapping;
    public com.android.server.display.config.EvenDimmerBrightnessData mEvenDimmerBrightnessData;
    private final com.android.server.display.feature.DisplayManagerFlags mFlags;
    private com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData mHbmData;
    private com.android.server.display.config.HdrBrightnessData mHdrBrightnessData;
    private android.hardware.input.HostUsiVersion mHostUsiVersion;
    private int mInterpolationType;
    private java.lang.String mName;
    private float[] mNits;
    private android.util.Spline mNitsToBacklightSpline;
    private com.android.server.display.DisplayDeviceConfig.PowerThrottlingConfigData mPowerThrottlingConfigData;
    private com.android.server.display.config.SensorData mProximitySensor;
    private java.util.List<java.lang.String> mQuirks;
    private float[] mRawBacklight;
    private float[] mRawNits;
    private com.android.server.display.config.SensorData mScreenOffBrightnessSensor;
    private int[] mScreenOffBrightnessSensorValueToLux;
    private android.util.Spline mSdrToHdrRatioSpline;
    private com.android.server.display.config.SensorData mTempSensor;
    private boolean mVrrSupportEnabled;
    private static final java.lang.String TAG = "DisplayDeviceConfig";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);
    private static final float[] DEFAULT_BRIGHTNESS_THRESHOLDS = new float[0];
    private final java.util.List<android.hardware.display.DisplayManagerInternal.RefreshRateLimitation> mRefreshRateLimitations = new java.util.ArrayList(2);
    private float mBacklightMinimum = Float.NaN;
    private float mBacklightMaximum = Float.NaN;
    private float mBrightnessDefault = Float.NaN;
    private float mBrightnessRampFastDecrease = Float.NaN;
    private float mBrightnessRampFastIncrease = Float.NaN;
    private float mBrightnessRampSlowDecrease = Float.NaN;
    private float mBrightnessRampSlowIncrease = Float.NaN;
    private float mBrightnessRampSlowDecreaseIdle = Float.NaN;
    private float mBrightnessRampSlowIncreaseIdle = Float.NaN;
    private long mBrightnessRampDecreaseMaxMillis = 0;
    private long mBrightnessRampIncreaseMaxMillis = 0;
    private long mBrightnessRampDecreaseMaxIdleMillis = 0;
    private long mBrightnessRampIncreaseMaxIdleMillis = 0;
    private int mAmbientHorizonLong = 10000;
    private int mAmbientHorizonShort = 2000;
    private com.android.server.display.config.HysteresisLevels mScreenBrightnessHysteresis = com.android.server.display.config.HysteresisLevels.loadDisplayBrightnessConfig(null, null);
    private com.android.server.display.config.HysteresisLevels mScreenBrightnessIdleHysteresis = com.android.server.display.config.HysteresisLevels.loadDisplayBrightnessIdleConfig(null, null);
    private com.android.server.display.config.HysteresisLevels mAmbientBrightnessHysteresis = com.android.server.display.config.HysteresisLevels.loadAmbientBrightnessConfig(null, null);
    private com.android.server.display.config.HysteresisLevels mAmbientBrightnessIdleHysteresis = com.android.server.display.config.HysteresisLevels.loadAmbientBrightnessIdleConfig(null, null);
    private boolean mIsHighBrightnessModeEnabled = false;
    private java.lang.String mLoadedFrom = null;
    private long mAutoBrightnessBrighteningLightDebounce = -1;
    private long mAutoBrightnessDarkeningLightDebounce = -1;
    private long mAutoBrightnessBrighteningLightDebounceIdle = -1;
    private long mAutoBrightnessDarkeningLightDebounceIdle = -1;
    private boolean mAutoBrightnessAvailable = false;
    private boolean mDdcAutoBrightnessAvailable = true;
    private int mDefaultHighBlockingZoneRefreshRate = 0;
    private int mDefaultLowBlockingZoneRefreshRate = 60;
    private final java.util.Map<java.lang.String, android.view.SurfaceControl.RefreshRateRange> mRefreshRateZoneProfiles = new java.util.HashMap();
    private float[] mLowDisplayBrightnessThresholds = DEFAULT_BRIGHTNESS_THRESHOLDS;
    private float[] mLowAmbientBrightnessThresholds = DEFAULT_BRIGHTNESS_THRESHOLDS;
    private float[] mHighDisplayBrightnessThresholds = DEFAULT_BRIGHTNESS_THRESHOLDS;
    private float[] mHighAmbientBrightnessThresholds = DEFAULT_BRIGHTNESS_THRESHOLDS;
    private java.lang.String mLowBlockingZoneThermalMapId = null;
    private java.lang.String mHighBlockingZoneThermalMapId = null;
    private final java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData> mThermalBrightnessThrottlingDataMapByThrottlingId = new java.util.HashMap();
    private final java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.PowerThrottlingData> mPowerThrottlingDataMapByThrottlingId = new java.util.HashMap();
    private final java.util.Map<java.lang.String, android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange>> mRefreshRateThrottlingMap = new java.util.HashMap();
    private final java.util.Map<com.android.server.display.DisplayDeviceConfig.BrightnessLimitMapType, java.util.Map<java.lang.Float, java.lang.Float>> mLuxThrottlingData = new java.util.HashMap();
    private java.util.List<com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholdPoint> mIdleScreenRefreshRateTimeoutLuxThresholds = new java.util.ArrayList();
    private com.android.server.display.config.RefreshRateData mRefreshRateData = com.android.server.display.config.RefreshRateData.DEFAULT_REFRESH_RATE_DATA;

    DisplayDeviceConfig(android.content.Context context, com.android.server.display.feature.DisplayManagerFlags flags) {
        this.mContext = context;
        this.mFlags = flags;
    }

    public static com.android.server.display.DisplayDeviceConfig create(android.content.Context context, long physicalDisplayId, boolean isFirstDisplay, com.android.server.display.feature.DisplayManagerFlags flags) {
        com.android.server.display.DisplayDeviceConfig config = createWithoutDefaultValues(context, physicalDisplayId, isFirstDisplay, flags);
        config.copyUninitializedValuesFromSecondaryConfig(loadDefaultConfigurationXml(context));
        return config;
    }

    public static com.android.server.display.DisplayDeviceConfig create(android.content.Context context, boolean useConfigXml, com.android.server.display.feature.DisplayManagerFlags flags) {
        if (useConfigXml) {
            com.android.server.display.DisplayDeviceConfig config = getConfigFromGlobalXml(context, flags);
            return config;
        }
        com.android.server.display.DisplayDeviceConfig config2 = getConfigFromPmValues(context, flags);
        return config2;
    }

    private static com.android.server.display.DisplayDeviceConfig createWithoutDefaultValues(android.content.Context context, long physicalDisplayId, boolean isFirstDisplay, com.android.server.display.feature.DisplayManagerFlags flags) {
        com.android.server.display.DisplayDeviceConfig config = loadConfigFromDirectory(context, android.os.Environment.getProductDirectory(), physicalDisplayId, flags);
        if (config != null) {
            return config;
        }
        com.android.server.display.DisplayDeviceConfig config2 = loadConfigFromDirectory(context, android.os.Environment.getVendorDirectory(), physicalDisplayId, flags);
        if (config2 != null) {
            return config2;
        }
        return create(context, isFirstDisplay, flags);
    }

    private static com.android.server.display.config.DisplayConfiguration loadDefaultConfigurationXml(android.content.Context context) {
        java.util.List<java.io.File> defaultXmlLocations = new java.util.ArrayList<>();
        defaultXmlLocations.add(android.os.Environment.buildPath(android.os.Environment.getProductDirectory(), new java.lang.String[]{ETC_DIR, DISPLAY_CONFIG_DIR, DEFAULT_CONFIG_FILE}));
        defaultXmlLocations.add(android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{ETC_DIR, DISPLAY_CONFIG_DIR, DEFAULT_CONFIG_FILE}));
        int uiModeType = context.getResources().getInteger(android.R.integer.config_defaultPeakRefreshRate);
        java.lang.String uiModeTypeStr = android.content.res.Configuration.getUiModeTypeString(uiModeType);
        if (uiModeTypeStr != null) {
            defaultXmlLocations.add(android.os.Environment.buildPath(android.os.Environment.getRootDirectory(), new java.lang.String[]{ETC_DIR, DISPLAY_CONFIG_DIR, java.lang.String.format(DEFAULT_CONFIG_FILE_WITH_UIMODE_FORMAT, uiModeTypeStr)}));
        }
        defaultXmlLocations.add(android.os.Environment.buildPath(android.os.Environment.getRootDirectory(), new java.lang.String[]{ETC_DIR, DISPLAY_CONFIG_DIR, DEFAULT_CONFIG_FILE}));
        java.io.File configFile = getFirstExistingFile(defaultXmlLocations);
        if (configFile == null) {
            return null;
        }
        com.android.server.display.config.DisplayConfiguration defaultConfig = null;
        try {
            java.io.InputStream in = new java.io.BufferedInputStream(new java.io.FileInputStream(configFile));
            try {
                defaultConfig = com.android.server.display.config.XmlParser.read(in);
                if (defaultConfig == null) {
                    android.util.Slog.i(TAG, "Default DisplayDeviceConfig file is null");
                }
                in.close();
            } catch (java.lang.Throwable th) {
                try {
                    in.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | javax.xml.datatype.DatatypeConfigurationException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Encountered an error while reading/parsing display config file: " + configFile, e);
        }
        return defaultConfig;
    }

    private static java.io.File getFirstExistingFile(java.util.Collection<java.io.File> files) {
        for (java.io.File file : files) {
            if (file.exists() && file.isFile()) {
                return file;
            }
        }
        return null;
    }

    private static com.android.server.display.DisplayDeviceConfig loadConfigFromDirectory(android.content.Context context, java.io.File baseDirectory, long physicalDisplayId, com.android.server.display.feature.DisplayManagerFlags flags) {
        com.android.server.display.DisplayDeviceConfig config = getConfigFromSuffix(context, baseDirectory, STABLE_ID_SUFFIX_FORMAT, physicalDisplayId, flags);
        if (config != null) {
            return config;
        }
        long withoutStableFlag = (-4611686018427387905L) & physicalDisplayId;
        com.android.server.display.DisplayDeviceConfig config2 = getConfigFromSuffix(context, baseDirectory, NO_SUFFIX_FORMAT, withoutStableFlag, flags);
        if (config2 != null) {
            return config2;
        }
        android.view.DisplayAddress.Physical physicalAddress = android.view.DisplayAddress.fromPhysicalDisplayId(physicalDisplayId);
        int port = physicalAddress.getPort();
        return getConfigFromSuffix(context, baseDirectory, PORT_SUFFIX_FORMAT, port, flags);
    }

    public java.lang.String getName() {
        return this.mName;
    }

    public float[] getNits() {
        if (this.mEvenDimmerBrightnessData != null) {
            return this.mEvenDimmerBrightnessData.mNits;
        }
        return this.mNits;
    }

    public float[] getBacklight() {
        if (this.mEvenDimmerBrightnessData != null) {
            return this.mEvenDimmerBrightnessData.mBacklight;
        }
        return this.mBacklight;
    }

    public float getBacklightFromBrightness(float brightness) {
        if (this.mEvenDimmerBrightnessData != null) {
            return this.mEvenDimmerBrightnessData.mBrightnessToBacklight.interpolate(brightness);
        }
        return this.mBrightnessToBacklightSpline.interpolate(brightness);
    }

    public float getBrightnessFromBacklight(float backlight) {
        if (this.mEvenDimmerBrightnessData != null) {
            return this.mEvenDimmerBrightnessData.mBacklightToBrightness.interpolate(backlight);
        }
        return this.mBacklightToBrightnessSpline.interpolate(backlight);
    }

    private android.util.Spline getBacklightToBrightnessSpline() {
        if (this.mEvenDimmerBrightnessData != null) {
            return this.mEvenDimmerBrightnessData.mBacklightToBrightness;
        }
        return this.mBacklightToBrightnessSpline;
    }

    public float getNitsFromBacklight(float backlight) {
        if (this.mEvenDimmerBrightnessData != null) {
            if (this.mEvenDimmerBrightnessData.mBacklightToNits == null) {
                return -1.0f;
            }
            return this.mEvenDimmerBrightnessData.mBacklightToNits.interpolate(java.lang.Math.max(backlight, this.mBacklightMinimum));
        }
        if (this.mBacklightToNitsSpline == null) {
            return -1.0f;
        }
        return this.mBacklightToNitsSpline.interpolate(java.lang.Math.max(backlight, this.mBacklightMinimum));
    }

    public float getBacklightFromNits(float nits) {
        if (this.mEvenDimmerBrightnessData != null) {
            return this.mEvenDimmerBrightnessData.mNitsToBacklight.interpolate(nits);
        }
        return this.mNitsToBacklightSpline.interpolate(nits);
    }

    private android.util.Spline getNitsToBacklightSpline() {
        if (this.mEvenDimmerBrightnessData != null) {
            return this.mEvenDimmerBrightnessData.mNitsToBacklight;
        }
        return this.mNitsToBacklightSpline;
    }

    public float getMinNitsFromLux(float lux) {
        if (this.mEvenDimmerBrightnessData == null) {
            return -1.0f;
        }
        return this.mEvenDimmerBrightnessData.mMinLuxToNits.interpolate(lux);
    }

    public float getEvenDimmerTransitionPoint() {
        if (this.mEvenDimmerBrightnessData == null) {
            return 0.0f;
        }
        return this.mEvenDimmerBrightnessData.mTransitionPoint;
    }

    public boolean hasSdrToHdrRatioSpline() {
        return this.mSdrToHdrRatioSpline != null;
    }

    public float getHdrBrightnessFromSdr(float brightness, float maxDesiredHdrSdrRatio) {
        if (this.mSdrToHdrRatioSpline == null) {
            return -1.0f;
        }
        float backlight = getBacklightFromBrightness(brightness);
        float nits = getNitsFromBacklight(backlight);
        if (nits == -1.0f) {
            return -1.0f;
        }
        float ratio = java.lang.Math.min(this.mSdrToHdrRatioSpline.interpolate(nits), maxDesiredHdrSdrRatio);
        float hdrNits = nits * ratio;
        if (getNitsToBacklightSpline() == null) {
            return -1.0f;
        }
        float hdrBacklight = java.lang.Math.max(this.mBacklightMinimum, java.lang.Math.min(this.mBacklightMaximum, getBacklightFromNits(hdrNits)));
        float hdrBrightness = getBrightnessFromBacklight(hdrBacklight);
        if (DEBUG) {
            android.util.Slog.d(TAG, "getHdrBrightnessFromSdr: sdr brightness " + brightness + " backlight " + backlight + " nits " + nits + " ratio " + ratio + " hdrNits " + hdrNits + " hdrBacklight " + hdrBacklight + " hdrBrightness " + hdrBrightness);
        }
        return hdrBrightness;
    }

    public float[] getBrightness() {
        if (this.mEvenDimmerBrightnessData != null) {
            return this.mEvenDimmerBrightnessData.mBrightness;
        }
        return this.mBrightness;
    }

    public float getBrightnessDefault() {
        return this.mBrightnessDefault;
    }

    public float getBrightnessRampFastDecrease() {
        return this.mBrightnessRampFastDecrease;
    }

    public float getBrightnessRampFastIncrease() {
        return this.mBrightnessRampFastIncrease;
    }

    public float getBrightnessRampSlowDecrease() {
        return this.mBrightnessRampSlowDecrease;
    }

    public float getBrightnessRampSlowIncrease() {
        return this.mBrightnessRampSlowIncrease;
    }

    public float getBrightnessRampSlowDecreaseIdle() {
        return this.mBrightnessRampSlowDecreaseIdle;
    }

    public float getBrightnessRampSlowIncreaseIdle() {
        return this.mBrightnessRampSlowIncreaseIdle;
    }

    public long getBrightnessRampDecreaseMaxMillis() {
        return this.mBrightnessRampDecreaseMaxMillis;
    }

    public long getBrightnessRampIncreaseMaxMillis() {
        return this.mBrightnessRampIncreaseMaxMillis;
    }

    public long getBrightnessRampDecreaseMaxIdleMillis() {
        return this.mBrightnessRampDecreaseMaxIdleMillis;
    }

    public long getBrightnessRampIncreaseMaxIdleMillis() {
        return this.mBrightnessRampIncreaseMaxIdleMillis;
    }

    public int getAmbientHorizonLong() {
        return this.mAmbientHorizonLong;
    }

    public int getAmbientHorizonShort() {
        return this.mAmbientHorizonShort;
    }

    public com.android.server.display.config.HysteresisLevels getAmbientBrightnessHysteresis() {
        return this.mAmbientBrightnessHysteresis;
    }

    public com.android.server.display.config.HysteresisLevels getAmbientBrightnessIdleHysteresis() {
        return this.mAmbientBrightnessIdleHysteresis;
    }

    public com.android.server.display.config.HysteresisLevels getScreenBrightnessHysteresis() {
        return this.mScreenBrightnessHysteresis;
    }

    public com.android.server.display.config.HysteresisLevels getScreenBrightnessIdleHysteresis() {
        return this.mScreenBrightnessIdleHysteresis;
    }

    public com.android.server.display.config.SensorData getAmbientLightSensor() {
        return this.mAmbientLightSensor;
    }

    public com.android.server.display.config.SensorData getScreenOffBrightnessSensor() {
        return this.mScreenOffBrightnessSensor;
    }

    public com.android.server.display.config.SensorData getProximitySensor() {
        return this.mProximitySensor;
    }

    public com.android.server.display.config.SensorData getTempSensor() {
        return this.mTempSensor;
    }

    boolean isAutoBrightnessAvailable() {
        return this.mAutoBrightnessAvailable;
    }

    public boolean hasQuirk(java.lang.String quirkValue) {
        return this.mQuirks != null && this.mQuirks.contains(quirkValue);
    }

    public com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData getHighBrightnessModeData() {
        if (!this.mIsHighBrightnessModeEnabled || this.mHbmData == null) {
            return null;
        }
        com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData hbmData = new com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData();
        this.mHbmData.copyTo(hbmData);
        return hbmData;
    }

    public com.android.server.display.DisplayDeviceConfig.PowerThrottlingConfigData getPowerThrottlingConfigData() {
        return this.mPowerThrottlingConfigData;
    }

    public java.util.Map<com.android.server.display.DisplayDeviceConfig.BrightnessLimitMapType, java.util.Map<java.lang.Float, java.lang.Float>> getLuxThrottlingData() {
        return this.mLuxThrottlingData;
    }

    public java.util.List<android.hardware.display.DisplayManagerInternal.RefreshRateLimitation> getRefreshRateLimitations() {
        return this.mRefreshRateLimitations;
    }

    public com.android.server.display.DensityMapping getDensityMapping() {
        return this.mDensityMapping;
    }

    public java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData> getThermalBrightnessThrottlingDataMapByThrottlingId() {
        return this.mThermalBrightnessThrottlingDataMapByThrottlingId;
    }

    public android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> getThermalRefreshRateThrottlingData(java.lang.String id) {
        java.lang.String key = id == null ? "default" : id;
        return this.mRefreshRateThrottlingMap.get(key);
    }

    public java.util.Map<java.lang.String, com.android.server.display.DisplayDeviceConfig.PowerThrottlingData> getPowerThrottlingDataMapByThrottlingId() {
        return this.mPowerThrottlingDataMapByThrottlingId;
    }

    public long getAutoBrightnessDarkeningLightDebounce() {
        return this.mAutoBrightnessDarkeningLightDebounce;
    }

    public long getAutoBrightnessBrighteningLightDebounce() {
        return this.mAutoBrightnessBrighteningLightDebounce;
    }

    public long getAutoBrightnessDarkeningLightDebounceIdle() {
        return this.mAutoBrightnessDarkeningLightDebounceIdle;
    }

    public long getAutoBrightnessBrighteningLightDebounceIdle() {
        return this.mAutoBrightnessBrighteningLightDebounceIdle;
    }

    public float[] getAutoBrightnessBrighteningLevelsLux(int mode, int preset) {
        if (this.mDisplayBrightnessMapping == null) {
            return null;
        }
        return this.mDisplayBrightnessMapping.getLuxArray(mode, preset);
    }

    public float[] getAutoBrightnessBrighteningLevelsNits() {
        if (this.mDisplayBrightnessMapping == null) {
            return null;
        }
        return this.mDisplayBrightnessMapping.getNitsArray();
    }

    public float[] getAutoBrightnessBrighteningLevels(int mode, int preset) {
        if (this.mDisplayBrightnessMapping == null) {
            return null;
        }
        return this.mDisplayBrightnessMapping.getBrightnessArray(mode, preset);
    }

    public com.android.server.display.config.RefreshRateData getRefreshRateData() {
        return this.mRefreshRateData;
    }

    public int getDefaultHighBlockingZoneRefreshRate() {
        return this.mDefaultHighBlockingZoneRefreshRate;
    }

    public int getDefaultLowBlockingZoneRefreshRate() {
        return this.mDefaultLowBlockingZoneRefreshRate;
    }

    public com.android.server.display.config.HdrBrightnessData getHdrBrightnessData() {
        return this.mHdrBrightnessData;
    }

    public android.view.SurfaceControl.RefreshRateRange getRefreshRange(java.lang.String id) {
        if (android.text.TextUtils.isEmpty(id)) {
            return null;
        }
        return this.mRefreshRateZoneProfiles.get(id);
    }

    java.util.Map<java.lang.String, android.view.SurfaceControl.RefreshRateRange> getRefreshRangeProfiles() {
        return this.mRefreshRateZoneProfiles;
    }

    public float[] getLowDisplayBrightnessThresholds() {
        return this.mLowDisplayBrightnessThresholds;
    }

    public float[] getLowAmbientBrightnessThresholds() {
        return this.mLowAmbientBrightnessThresholds;
    }

    public android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> getLowBlockingZoneThermalMap() {
        return getThermalRefreshRateThrottlingData(this.mLowBlockingZoneThermalMapId);
    }

    public float[] getHighDisplayBrightnessThresholds() {
        return this.mHighDisplayBrightnessThresholds;
    }

    public float[] getHighAmbientBrightnessThresholds() {
        return this.mHighAmbientBrightnessThresholds;
    }

    public android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> getHighBlockingZoneThermalMap() {
        return getThermalRefreshRateThrottlingData(this.mHighBlockingZoneThermalMapId);
    }

    public int[] getScreenOffBrightnessSensorValueToLux() {
        return this.mScreenOffBrightnessSensorValueToLux;
    }

    public android.hardware.input.HostUsiVersion getHostUsiVersion() {
        return this.mHostUsiVersion;
    }

    public boolean isEvenDimmerAvailable() {
        return this.mEvenDimmerBrightnessData != null;
    }

    public float getBrightnessCapForWearBedtimeMode() {
        return this.mBrightnessCapForWearBedtimeMode;
    }

    public boolean isVrrSupportEnabled() {
        return this.mVrrSupportEnabled;
    }

    public java.lang.String toString() {
        return "DisplayDeviceConfig{mLoadedFrom=" + this.mLoadedFrom + "\nmBacklight=" + java.util.Arrays.toString(this.mBacklight) + ", mNits=" + java.util.Arrays.toString(this.mNits) + ", mRawBacklight=" + java.util.Arrays.toString(this.mRawBacklight) + ", mRawNits=" + java.util.Arrays.toString(this.mRawNits) + ", mInterpolationType=" + this.mInterpolationType + "mBrightness=" + java.util.Arrays.toString(this.mBrightness) + "\nmBrightnessToBacklightSpline=" + this.mBrightnessToBacklightSpline + ", mBacklightToBrightnessSpline=" + this.mBacklightToBrightnessSpline + ", mNitsToBacklightSpline=" + this.mNitsToBacklightSpline + ", mBacklightMinimum=" + this.mBacklightMinimum + ", mBacklightMaximum=" + this.mBacklightMaximum + ", mBrightnessDefault=" + this.mBrightnessDefault + ", mQuirks=" + this.mQuirks + ", mIsHighBrightnessModeEnabled=" + this.mIsHighBrightnessModeEnabled + "\nmLuxThrottlingData=" + this.mLuxThrottlingData + ", mHbmData=" + this.mHbmData + ", mSdrToHdrRatioSpline=" + this.mSdrToHdrRatioSpline + ", mThermalBrightnessThrottlingDataMapByThrottlingId=" + this.mThermalBrightnessThrottlingDataMapByThrottlingId + "\n, mPowerThrottlingDataMapByThrottlingId=" + this.mPowerThrottlingDataMapByThrottlingId + "\nmBrightnessRampFastDecrease=" + this.mBrightnessRampFastDecrease + ", mBrightnessRampFastIncrease=" + this.mBrightnessRampFastIncrease + ", mBrightnessRampSlowDecrease=" + this.mBrightnessRampSlowDecrease + ", mBrightnessRampSlowIncrease=" + this.mBrightnessRampSlowIncrease + ", mBrightnessRampSlowDecreaseIdle=" + this.mBrightnessRampSlowDecreaseIdle + ", mBrightnessRampSlowIncreaseIdle=" + this.mBrightnessRampSlowIncreaseIdle + ", mBrightnessRampDecreaseMaxMillis=" + this.mBrightnessRampDecreaseMaxMillis + ", mBrightnessRampIncreaseMaxMillis=" + this.mBrightnessRampIncreaseMaxMillis + ", mBrightnessRampDecreaseMaxIdleMillis=" + this.mBrightnessRampDecreaseMaxIdleMillis + ", mBrightnessRampIncreaseMaxIdleMillis=" + this.mBrightnessRampIncreaseMaxIdleMillis + "\nmAmbientHorizonLong=" + this.mAmbientHorizonLong + ", mAmbientHorizonShort=" + this.mAmbientHorizonShort + "\nmAmbientBrightnessHysteresis=" + this.mAmbientBrightnessHysteresis + "\nmAmbientIdleHysteresis=" + this.mAmbientBrightnessIdleHysteresis + "\nmScreenBrightnessHysteresis=" + this.mScreenBrightnessHysteresis + "\nmScreenBrightnessIdleHysteresis=" + this.mScreenBrightnessIdleHysteresis + "\nmAmbientLightSensor=" + this.mAmbientLightSensor + ", mScreenOffBrightnessSensor=" + this.mScreenOffBrightnessSensor + ", mProximitySensor=" + this.mProximitySensor + ", mTempSensor=" + this.mTempSensor + ", mRefreshRateLimitations= " + java.util.Arrays.toString(this.mRefreshRateLimitations.toArray()) + ", mDensityMapping= " + this.mDensityMapping + ", mAutoBrightnessBrighteningLightDebounce= " + this.mAutoBrightnessBrighteningLightDebounce + ", mAutoBrightnessDarkeningLightDebounce= " + this.mAutoBrightnessDarkeningLightDebounce + ", mAutoBrightnessBrighteningLightDebounceIdle= " + this.mAutoBrightnessBrighteningLightDebounceIdle + ", mAutoBrightnessDarkeningLightDebounceIdle= " + this.mAutoBrightnessDarkeningLightDebounceIdle + ", mDisplayBrightnessMapping= " + this.mDisplayBrightnessMapping + ", mDdcAutoBrightnessAvailable= " + this.mDdcAutoBrightnessAvailable + ", mAutoBrightnessAvailable= " + this.mAutoBrightnessAvailable + "\nmDefaultLowBlockingZoneRefreshRate= " + this.mDefaultLowBlockingZoneRefreshRate + ", mDefaultHighBlockingZoneRefreshRate= " + this.mDefaultHighBlockingZoneRefreshRate + ", mRefreshRateData= " + this.mRefreshRateData + ", mRefreshRateZoneProfiles= " + this.mRefreshRateZoneProfiles + ", mRefreshRateThrottlingMap= " + this.mRefreshRateThrottlingMap + ", mLowBlockingZoneThermalMapId= " + this.mLowBlockingZoneThermalMapId + ", mHighBlockingZoneThermalMapId= " + this.mHighBlockingZoneThermalMapId + "\nmLowDisplayBrightnessThresholds= " + java.util.Arrays.toString(this.mLowDisplayBrightnessThresholds) + ", mLowAmbientBrightnessThresholds= " + java.util.Arrays.toString(this.mLowAmbientBrightnessThresholds) + ", mHighDisplayBrightnessThresholds= " + java.util.Arrays.toString(this.mHighDisplayBrightnessThresholds) + ", mHighAmbientBrightnessThresholds= " + java.util.Arrays.toString(this.mHighAmbientBrightnessThresholds) + "\nmScreenOffBrightnessSensorValueToLux= " + java.util.Arrays.toString(this.mScreenOffBrightnessSensorValueToLux) + "\nmUsiVersion= " + this.mHostUsiVersion + "\nmHdrBrightnessData= " + this.mHdrBrightnessData + "\nmBrightnessCapForWearBedtimeMode= " + this.mBrightnessCapForWearBedtimeMode + "\nmEvenDimmerBrightnessData:" + (this.mEvenDimmerBrightnessData != null ? this.mEvenDimmerBrightnessData.toString() : "null") + "\nmVrrSupported= " + this.mVrrSupportEnabled + "\n}";
    }

    private static com.android.server.display.DisplayDeviceConfig getConfigFromSuffix(android.content.Context context, java.io.File baseDirectory, java.lang.String suffixFormat, long idNumber, com.android.server.display.feature.DisplayManagerFlags flags) {
        java.lang.String suffix = java.lang.String.format(java.util.Locale.ROOT, suffixFormat, java.lang.Long.valueOf(idNumber));
        java.lang.String filename = java.lang.String.format(java.util.Locale.ROOT, CONFIG_FILE_FORMAT, suffix);
        java.io.File filePath = android.os.Environment.buildPath(baseDirectory, new java.lang.String[]{ETC_DIR, DISPLAY_CONFIG_DIR, filename});
        com.android.server.display.DisplayDeviceConfig config = new com.android.server.display.DisplayDeviceConfig(context, flags);
        if (config.initFromFile(filePath)) {
            return config;
        }
        return null;
    }

    private static com.android.server.display.DisplayDeviceConfig getConfigFromGlobalXml(android.content.Context context, com.android.server.display.feature.DisplayManagerFlags flags) {
        com.android.server.display.DisplayDeviceConfig config = new com.android.server.display.DisplayDeviceConfig(context, flags);
        config.initFromGlobalXml();
        return config;
    }

    private static com.android.server.display.DisplayDeviceConfig getConfigFromPmValues(android.content.Context context, com.android.server.display.feature.DisplayManagerFlags flags) {
        com.android.server.display.DisplayDeviceConfig config = new com.android.server.display.DisplayDeviceConfig(context, flags);
        config.initFromDefaultValues();
        return config;
    }

    boolean initFromFile(java.io.File configFile) {
        if (!configFile.exists()) {
            return false;
        }
        if (!configFile.isFile()) {
            android.util.Slog.e(TAG, "Display configuration is not a file: " + configFile + ", skipping");
            return false;
        }
        try {
            java.io.InputStream in = new java.io.BufferedInputStream(new java.io.FileInputStream(configFile));
            try {
                com.android.server.display.config.DisplayConfiguration config = com.android.server.display.config.XmlParser.read(in);
                if (config != null) {
                    loadName(config);
                    loadDensityMapping(config);
                    loadBrightnessDefaultFromDdcXml(config);
                    loadBrightnessConstraintsFromConfigXml();
                    if (this.mFlags.isEvenDimmerEnabled() && this.mContext.getResources().getBoolean(android.R.bool.config_enableUdcSysfsUsbStateUpdate)) {
                        this.mEvenDimmerBrightnessData = com.android.server.display.config.EvenDimmerBrightnessData.loadConfig(config);
                    }
                    loadBrightnessMap(config);
                    loadThermalThrottlingConfig(config);
                    loadPowerThrottlingConfigData(config);
                    loadHighBrightnessModeData(config);
                    loadLuxThrottling(config);
                    loadQuirks(config);
                    loadBrightnessRamps(config);
                    this.mAmbientLightSensor = com.android.server.display.config.SensorData.loadAmbientLightSensorConfig(config, this.mContext.getResources());
                    this.mScreenOffBrightnessSensor = com.android.server.display.config.SensorData.loadScreenOffBrightnessSensorConfig(config);
                    this.mProximitySensor = com.android.server.display.config.SensorData.loadProxSensorConfig(this.mFlags, config);
                    this.mTempSensor = com.android.server.display.config.SensorData.loadTempSensorConfig(this.mFlags, config);
                    this.mRefreshRateData = com.android.server.display.config.RefreshRateData.loadRefreshRateData(config, this.mContext.getResources());
                    loadAmbientHorizonFromDdc(config);
                    loadBrightnessChangeThresholds(config);
                    loadAutoBrightnessConfigValues(config);
                    loadRefreshRateSetting(config);
                    loadScreenOffBrightnessSensorValueToLuxFromDdc(config);
                    loadUsiVersion(config);
                    this.mHdrBrightnessData = com.android.server.display.config.HdrBrightnessData.loadConfig(config);
                    loadBrightnessCapForWearBedtimeMode(config);
                    loadIdleScreenRefreshRateTimeoutConfigs(config);
                    this.mVrrSupportEnabled = config.getSupportsVrr();
                } else {
                    android.util.Slog.w(TAG, "DisplayDeviceConfig file is null");
                }
                in.close();
            } catch (java.lang.Throwable th) {
                try {
                    in.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | javax.xml.datatype.DatatypeConfigurationException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Encountered an error while reading/parsing display config file: " + configFile, e);
        }
        this.mLoadedFrom = configFile.toString();
        return true;
    }

    private void initFromGlobalXml() {
        loadBrightnessDefaultFromConfigXml();
        loadBrightnessConstraintsFromConfigXml();
        loadBrightnessMapFromConfigXml();
        loadBrightnessRampsFromConfigXml();
        this.mAmbientLightSensor = com.android.server.display.config.SensorData.loadAmbientLightSensorConfig(this.mContext.getResources());
        this.mProximitySensor = com.android.server.display.config.SensorData.loadSensorUnspecifiedConfig();
        this.mTempSensor = com.android.server.display.config.SensorData.loadTempSensorUnspecifiedConfig();
        this.mRefreshRateData = com.android.server.display.config.RefreshRateData.loadRefreshRateData(null, this.mContext.getResources());
        loadBrightnessChangeThresholdsFromXml();
        loadAutoBrightnessConfigsFromConfigXml();
        loadAutoBrightnessAvailableFromConfigXml();
        loadRefreshRateSetting(null);
        loadBrightnessCapForWearBedtimeModeFromConfigXml();
        loadIdleScreenRefreshRateTimeoutConfigs(null);
        this.mLoadedFrom = "<config.xml>";
    }

    private void initFromDefaultValues() {
        this.mLoadedFrom = "Static values";
        this.mBacklightMinimum = 0.0f;
        this.mBacklightMaximum = 1.0f;
        this.mBrightnessDefault = 0.5f;
        this.mBrightnessRampFastDecrease = 1.0f;
        this.mBrightnessRampFastIncrease = 1.0f;
        this.mBrightnessRampSlowDecrease = 1.0f;
        this.mBrightnessRampSlowIncrease = 1.0f;
        this.mBrightnessRampSlowDecreaseIdle = 1.0f;
        this.mBrightnessRampSlowIncreaseIdle = 1.0f;
        this.mBrightnessRampDecreaseMaxMillis = 0L;
        this.mBrightnessRampIncreaseMaxMillis = 0L;
        this.mBrightnessRampDecreaseMaxIdleMillis = 0L;
        this.mBrightnessRampIncreaseMaxIdleMillis = 0L;
        setSimpleMappingStrategyValues();
        this.mAmbientLightSensor = com.android.server.display.config.SensorData.loadAmbientLightSensorConfig(this.mContext.getResources());
        this.mProximitySensor = com.android.server.display.config.SensorData.loadSensorUnspecifiedConfig();
        this.mTempSensor = com.android.server.display.config.SensorData.loadTempSensorUnspecifiedConfig();
        loadAutoBrightnessAvailableFromConfigXml();
        this.mRefreshRateData = com.android.server.display.config.RefreshRateData.loadRefreshRateData(null, this.mContext.getResources());
    }

    private void copyUninitializedValuesFromSecondaryConfig(com.android.server.display.config.DisplayConfiguration defaultConfig) {
        if (defaultConfig != null && this.mDensityMapping == null) {
            loadDensityMapping(defaultConfig);
        }
    }

    private void loadName(com.android.server.display.config.DisplayConfiguration config) {
        this.mName = config.getName();
    }

    private void loadDensityMapping(com.android.server.display.config.DisplayConfiguration config) {
        if (config.getDensityMapping() == null) {
            return;
        }
        java.util.List<com.android.server.display.config.Density> entriesFromXml = config.getDensityMapping().getDensity();
        com.android.server.display.DensityMapping.Entry[] entries = new com.android.server.display.DensityMapping.Entry[entriesFromXml.size()];
        for (int i = 0; i < entriesFromXml.size(); i++) {
            com.android.server.display.config.Density density = entriesFromXml.get(i);
            entries[i] = new com.android.server.display.DensityMapping.Entry(density.getWidth().intValue(), density.getHeight().intValue(), density.getDensity().intValue());
        }
        this.mDensityMapping = com.android.server.display.DensityMapping.createByOwning(entries);
    }

    private void loadBrightnessDefaultFromDdcXml(com.android.server.display.config.DisplayConfiguration config) {
        if (config != null) {
            java.math.BigDecimal configBrightnessDefault = config.getScreenBrightnessDefault();
            if (configBrightnessDefault != null) {
                this.mBrightnessDefault = configBrightnessDefault.floatValue();
            } else {
                loadBrightnessDefaultFromConfigXml();
            }
        }
    }

    private void loadBrightnessDefaultFromConfigXml() {
        float def = this.mContext.getResources().getFloat(android.R.dimen.config_letterboxVerticalPositionMultiplier);
        if (def == INVALID_BRIGHTNESS_IN_CONFIG) {
            this.mBrightnessDefault = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_radioScanningTimeout));
        } else {
            this.mBrightnessDefault = def;
        }
    }

    private void loadBrightnessConstraintsFromConfigXml() {
        float min = this.mContext.getResources().getFloat(android.R.dimen.config_mediaMetadataBitmapMaxSize);
        float max = this.mContext.getResources().getFloat(android.R.dimen.config_lowResTaskSnapshotScale);
        if (min == INVALID_BRIGHTNESS_IN_CONFIG || max == INVALID_BRIGHTNESS_IN_CONFIG) {
            this.mBacklightMinimum = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_reduceBrightColorsStrengthDefault));
            this.mBacklightMaximum = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_recentVibrationsDumpSizeLimit));
        } else {
            this.mBacklightMinimum = min;
            this.mBacklightMaximum = max;
        }
    }

    private void loadBrightnessMap(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.NitsMap map = config.getScreenBrightnessMap();
        if (map == null) {
            loadBrightnessMapFromConfigXml();
            return;
        }
        java.util.List<com.android.server.display.config.Point> points = map.getPoint();
        int size = points.size();
        float[] nits = new float[size];
        float[] backlight = new float[size];
        this.mInterpolationType = convertInterpolationType(map.getInterpolation());
        int i = 0;
        for (com.android.server.display.config.Point point : points) {
            nits[i] = point.getNits().floatValue();
            backlight[i] = point.getValue().floatValue();
            if (i > 0) {
                if (nits[i] < nits[i - 1]) {
                    android.util.Slog.e(TAG, "screenBrightnessMap must be non-decreasing, ignoring rest  of configuration. Nits: " + nits[i] + " < " + nits[i - 1]);
                    return;
                } else if (backlight[i] < backlight[i - 1]) {
                    android.util.Slog.e(TAG, "screenBrightnessMap must be non-decreasing, ignoring rest  of configuration. Value: " + backlight[i] + " < " + backlight[i - 1]);
                    return;
                }
            }
            i++;
        }
        this.mRawNits = nits;
        this.mRawBacklight = backlight;
        constrainNitsAndBacklightArrays();
    }

    private android.util.Spline loadSdrHdrRatioMap(com.android.server.display.config.HighBrightnessMode hbmConfig) {
        java.util.List<com.android.server.display.config.SdrHdrRatioPoint> points;
        int size;
        com.android.server.display.config.SdrHdrRatioMap sdrHdrRatioMap = hbmConfig.getSdrHdrRatioMap_all();
        if (sdrHdrRatioMap == null || (size = (points = sdrHdrRatioMap.getPoint()).size()) == 0) {
            return null;
        }
        float[] nits = new float[size];
        float[] ratios = new float[size];
        int i = 0;
        for (com.android.server.display.config.SdrHdrRatioPoint point : points) {
            nits[i] = point.getSdrNits().floatValue();
            if (i > 0 && nits[i] < nits[i - 1]) {
                android.util.Slog.e(TAG, "sdrHdrRatioMap must be non-decreasing, ignoring rest  of configuration. nits: " + nits[i] + " < " + nits[i - 1]);
                return null;
            }
            ratios[i] = point.getHdrRatio().floatValue();
            i++;
        }
        return android.util.Spline.createSpline(nits, ratios);
    }

    private void loadThermalThrottlingConfig(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.ThermalThrottling throttlingConfig = config.getThermalThrottling();
        if (throttlingConfig == null) {
            android.util.Slog.i(TAG, "No thermal throttling config found");
        } else {
            loadThermalBrightnessThrottlingMaps(throttlingConfig);
            loadThermalRefreshRateThrottlingMap(throttlingConfig);
        }
    }

    private void loadThermalBrightnessThrottlingMaps(com.android.server.display.config.ThermalThrottling throttlingConfig) {
        java.util.List<com.android.server.display.config.BrightnessThrottlingMap> maps = throttlingConfig.getBrightnessThrottlingMap();
        if (maps == null || maps.isEmpty()) {
            android.util.Slog.i(TAG, "No brightness throttling map found");
            return;
        }
        for (com.android.server.display.config.BrightnessThrottlingMap map : maps) {
            java.util.List<com.android.server.display.config.BrightnessThrottlingPoint> points = map.getBrightnessThrottlingPoint();
            java.util.List<com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel> throttlingLevels = new java.util.ArrayList<>(points.size());
            boolean badConfig = false;
            java.util.Iterator<com.android.server.display.config.BrightnessThrottlingPoint> it = points.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.display.config.BrightnessThrottlingPoint point = it.next();
                com.android.server.display.config.ThermalStatus status = point.getThermalStatus();
                if (!thermalStatusIsValid(status)) {
                    badConfig = true;
                    break;
                }
                throttlingLevels.add(new com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel(convertThermalStatus(status), point.getBrightness().floatValue()));
            }
            if (!badConfig) {
                java.lang.String id = map.getId() == null ? "default" : map.getId();
                if (this.mThermalBrightnessThrottlingDataMapByThrottlingId.containsKey(id)) {
                    throw new java.lang.RuntimeException("Brightness throttling data with ID " + id + " already exists");
                }
                this.mThermalBrightnessThrottlingDataMapByThrottlingId.put(id, com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.create(throttlingLevels));
            }
        }
    }

    private void loadThermalRefreshRateThrottlingMap(com.android.server.display.config.ThermalThrottling throttlingConfig) {
        java.util.List<com.android.server.display.config.RefreshRateThrottlingMap> maps = throttlingConfig.getRefreshRateThrottlingMap();
        if (maps == null || maps.isEmpty()) {
            android.util.Slog.w(TAG, "RefreshRateThrottling: map not found");
            return;
        }
        for (com.android.server.display.config.RefreshRateThrottlingMap map : maps) {
            java.util.List<com.android.server.display.config.RefreshRateThrottlingPoint> points = map.getRefreshRateThrottlingPoint();
            java.lang.String id = map.getId() == null ? "default" : map.getId();
            if (points == null || points.isEmpty()) {
                android.util.Slog.w(TAG, "RefreshRateThrottling: points not found for mapId=" + id);
            } else if (this.mRefreshRateThrottlingMap.containsKey(id)) {
                android.util.Slog.wtf(TAG, "RefreshRateThrottling: map already exists, mapId=" + id);
            } else {
                android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> refreshRates = new android.util.SparseArray<>();
                for (com.android.server.display.config.RefreshRateThrottlingPoint point : points) {
                    com.android.server.display.config.ThermalStatus status = point.getThermalStatus();
                    if (!thermalStatusIsValid(status)) {
                        android.util.Slog.wtf(TAG, "RefreshRateThrottling: Invalid thermalStatus=" + status.getRawName() + ",mapId=" + id);
                    } else {
                        int thermalStatusInt = convertThermalStatus(status);
                        if (refreshRates.contains(thermalStatusInt)) {
                            android.util.Slog.wtf(TAG, "RefreshRateThrottling: thermalStatus=" + status.getRawName() + " is already in the map, mapId=" + id);
                        } else {
                            refreshRates.put(thermalStatusInt, new android.view.SurfaceControl.RefreshRateRange(point.getRefreshRateRange().getMinimum().floatValue(), point.getRefreshRateRange().getMaximum().floatValue()));
                        }
                    }
                }
                if (refreshRates.size() == 0) {
                    android.util.Slog.w(TAG, "RefreshRateThrottling: no valid throttling points found for map, mapId=" + id);
                } else {
                    this.mRefreshRateThrottlingMap.put(id, refreshRates);
                }
            }
        }
    }

    private boolean loadPowerThrottlingMaps(com.android.server.display.config.PowerThrottlingConfig throttlingConfig) {
        java.util.List<com.android.server.display.config.PowerThrottlingMap> maps = throttlingConfig.getPowerThrottlingMap();
        if (maps == null || maps.isEmpty()) {
            android.util.Slog.i(TAG, "No power throttling map found");
            return false;
        }
        for (com.android.server.display.config.PowerThrottlingMap map : maps) {
            java.util.List<com.android.server.display.config.PowerThrottlingPoint> points = map.getPowerThrottlingPoint();
            java.util.List<com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel> throttlingLevels = new java.util.ArrayList<>(points.size());
            boolean badConfig = false;
            java.util.Iterator<com.android.server.display.config.PowerThrottlingPoint> it = points.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.display.config.PowerThrottlingPoint point = it.next();
                com.android.server.display.config.ThermalStatus status = point.getThermalStatus();
                if (!thermalStatusIsValid(status)) {
                    badConfig = true;
                    break;
                }
                throttlingLevels.add(new com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel(convertThermalStatus(status), point.getPowerQuotaMilliWatts().floatValue()));
            }
            if (!badConfig) {
                java.lang.String id = map.getId() == null ? "default" : map.getId();
                if (this.mPowerThrottlingDataMapByThrottlingId.containsKey(id)) {
                    throw new java.lang.RuntimeException("Power throttling data with ID " + id + " already exists");
                }
                this.mPowerThrottlingDataMapByThrottlingId.put(id, com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.create(throttlingLevels));
            }
        }
        return true;
    }

    private void loadPowerThrottlingConfigData(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.PowerThrottlingConfig powerThrottlingCfg = config.getPowerThrottlingConfig();
        if (powerThrottlingCfg == null || !loadPowerThrottlingMaps(powerThrottlingCfg)) {
            return;
        }
        float lowestBrightnessCap = powerThrottlingCfg.getBrightnessLowestCapAllowed().floatValue();
        int pollingWindowMillis = powerThrottlingCfg.getPollingWindowMillis().intValue();
        this.mPowerThrottlingConfigData = new com.android.server.display.DisplayDeviceConfig.PowerThrottlingConfigData(lowestBrightnessCap, pollingWindowMillis);
    }

    private void loadRefreshRateSetting(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.BlockingZoneConfig lowerBlockingZoneConfig;
        com.android.server.display.config.BlockingZoneConfig higherBlockingZoneConfig = null;
        com.android.server.display.config.RefreshRateConfigs refreshRateConfigs = config == null ? null : config.getRefreshRate();
        if (refreshRateConfigs == null) {
            lowerBlockingZoneConfig = null;
        } else {
            lowerBlockingZoneConfig = refreshRateConfigs.getLowerBlockingZoneConfigs();
        }
        if (refreshRateConfigs != null) {
            higherBlockingZoneConfig = refreshRateConfigs.getHigherBlockingZoneConfigs();
        }
        loadLowerRefreshRateBlockingZones(lowerBlockingZoneConfig);
        loadHigherRefreshRateBlockingZones(higherBlockingZoneConfig);
        loadRefreshRateZoneProfiles(refreshRateConfigs);
    }

    private void loadRefreshRateZoneProfiles(com.android.server.display.config.RefreshRateConfigs refreshRateConfigs) {
        if (refreshRateConfigs == null || refreshRateConfigs.getRefreshRateZoneProfiles() == null) {
            return;
        }
        for (com.android.server.display.config.RefreshRateZone zone : refreshRateConfigs.getRefreshRateZoneProfiles().getRefreshRateZoneProfile()) {
            com.android.server.display.config.RefreshRateRange range = zone.getRefreshRateRange();
            this.mRefreshRateZoneProfiles.put(zone.getId(), new android.view.SurfaceControl.RefreshRateRange(range.getMinimum().floatValue(), range.getMaximum().floatValue()));
        }
    }

    private void loadLowerRefreshRateBlockingZones(com.android.server.display.config.BlockingZoneConfig lowerBlockingZoneConfig) {
        if (lowerBlockingZoneConfig != null) {
            this.mLowBlockingZoneThermalMapId = lowerBlockingZoneConfig.getRefreshRateThermalThrottlingId();
        }
        loadLowerBlockingZoneDefaultRefreshRate(lowerBlockingZoneConfig);
        loadLowerBrightnessThresholds(lowerBlockingZoneConfig);
    }

    private void loadHigherRefreshRateBlockingZones(com.android.server.display.config.BlockingZoneConfig upperBlockingZoneConfig) {
        if (upperBlockingZoneConfig != null) {
            this.mHighBlockingZoneThermalMapId = upperBlockingZoneConfig.getRefreshRateThermalThrottlingId();
        }
        loadHigherBlockingZoneDefaultRefreshRate(upperBlockingZoneConfig);
        loadHigherBrightnessThresholds(upperBlockingZoneConfig);
    }

    private void loadHigherBlockingZoneDefaultRefreshRate(com.android.server.display.config.BlockingZoneConfig upperBlockingZoneConfig) {
        if (upperBlockingZoneConfig == null) {
            this.mDefaultHighBlockingZoneRefreshRate = this.mContext.getResources().getInteger(android.R.integer.config_dropboxLowPriorityBroadcastRateLimitPeriod);
        } else {
            this.mDefaultHighBlockingZoneRefreshRate = upperBlockingZoneConfig.getDefaultRefreshRate().intValue();
        }
    }

    private void loadLowerBlockingZoneDefaultRefreshRate(com.android.server.display.config.BlockingZoneConfig lowerBlockingZoneConfig) {
        if (lowerBlockingZoneConfig == null) {
            this.mDefaultLowBlockingZoneRefreshRate = this.mContext.getResources().getInteger(android.R.integer.config_defaultNotificationLedOn);
        } else {
            this.mDefaultLowBlockingZoneRefreshRate = lowerBlockingZoneConfig.getDefaultRefreshRate().intValue();
        }
    }

    private void loadLowerBrightnessThresholds(com.android.server.display.config.BlockingZoneConfig lowerBlockingZoneConfig) {
        if (lowerBlockingZoneConfig == null) {
            int[] lowDisplayBrightnessThresholdsInt = this.mContext.getResources().getIntArray(android.R.array.config_bg_current_drain_threshold_to_bg_restricted);
            int[] lowAmbientBrightnessThresholdsInt = this.mContext.getResources().getIntArray(android.R.array.config_ambientThresholdsOfPeakRefreshRate);
            if (lowDisplayBrightnessThresholdsInt == null || lowAmbientBrightnessThresholdsInt == null || lowDisplayBrightnessThresholdsInt.length != lowAmbientBrightnessThresholdsInt.length) {
                throw new java.lang.RuntimeException("display low brightness threshold array and ambient brightness threshold array have different length: lowDisplayBrightnessThresholdsInt=" + java.util.Arrays.toString(lowDisplayBrightnessThresholdsInt) + ", lowAmbientBrightnessThresholdsInt=" + java.util.Arrays.toString(lowAmbientBrightnessThresholdsInt));
            }
            this.mLowDisplayBrightnessThresholds = com.android.server.display.utils.DeviceConfigParsingUtils.displayBrightnessThresholdsIntToFloat(lowDisplayBrightnessThresholdsInt);
            this.mLowAmbientBrightnessThresholds = com.android.server.display.utils.DeviceConfigParsingUtils.ambientBrightnessThresholdsIntToFloat(lowAmbientBrightnessThresholdsInt);
            return;
        }
        java.util.List<com.android.server.display.config.DisplayBrightnessPoint> lowerThresholdDisplayBrightnessPoints = lowerBlockingZoneConfig.getBlockingZoneThreshold().getDisplayBrightnessPoint();
        int size = lowerThresholdDisplayBrightnessPoints.size();
        this.mLowDisplayBrightnessThresholds = new float[size];
        this.mLowAmbientBrightnessThresholds = new float[size];
        for (int i = 0; i < size; i++) {
            float thresholdNits = lowerThresholdDisplayBrightnessPoints.get(i).getNits().floatValue();
            if (thresholdNits < 0.0f) {
                this.mLowDisplayBrightnessThresholds[i] = thresholdNits;
            } else {
                float thresholdBacklight = getBacklightFromNits(thresholdNits);
                this.mLowDisplayBrightnessThresholds[i] = getBrightnessFromBacklight(thresholdBacklight);
            }
            this.mLowAmbientBrightnessThresholds[i] = lowerThresholdDisplayBrightnessPoints.get(i).getLux().floatValue();
        }
    }

    private void loadHigherBrightnessThresholds(com.android.server.display.config.BlockingZoneConfig blockingZoneConfig) {
        if (blockingZoneConfig == null) {
            int[] highDisplayBrightnessThresholdsInt = this.mContext.getResources().getIntArray(android.R.array.config_hideWhenDisabled_packageNames);
            int[] highAmbientBrightnessThresholdsInt = this.mContext.getResources().getIntArray(android.R.array.config_healthConnectRestoreKnownSigners);
            if (highDisplayBrightnessThresholdsInt == null || highAmbientBrightnessThresholdsInt == null || highDisplayBrightnessThresholdsInt.length != highAmbientBrightnessThresholdsInt.length) {
                throw new java.lang.RuntimeException("display high brightness threshold array and ambient brightness threshold array have different length: highDisplayBrightnessThresholdsInt=" + java.util.Arrays.toString(highDisplayBrightnessThresholdsInt) + ", highAmbientBrightnessThresholdsInt=" + java.util.Arrays.toString(highAmbientBrightnessThresholdsInt));
            }
            this.mHighDisplayBrightnessThresholds = com.android.server.display.utils.DeviceConfigParsingUtils.displayBrightnessThresholdsIntToFloat(highDisplayBrightnessThresholdsInt);
            this.mHighAmbientBrightnessThresholds = com.android.server.display.utils.DeviceConfigParsingUtils.ambientBrightnessThresholdsIntToFloat(highAmbientBrightnessThresholdsInt);
            return;
        }
        java.util.List<com.android.server.display.config.DisplayBrightnessPoint> higherThresholdDisplayBrightnessPoints = blockingZoneConfig.getBlockingZoneThreshold().getDisplayBrightnessPoint();
        int size = higherThresholdDisplayBrightnessPoints.size();
        this.mHighDisplayBrightnessThresholds = new float[size];
        this.mHighAmbientBrightnessThresholds = new float[size];
        for (int i = 0; i < size; i++) {
            float thresholdNits = higherThresholdDisplayBrightnessPoints.get(i).getNits().floatValue();
            if (thresholdNits < 0.0f) {
                this.mHighDisplayBrightnessThresholds[i] = thresholdNits;
            } else {
                float thresholdBacklight = getBacklightFromNits(thresholdNits);
                this.mHighDisplayBrightnessThresholds[i] = getBrightnessFromBacklight(thresholdBacklight);
            }
            this.mHighAmbientBrightnessThresholds[i] = higherThresholdDisplayBrightnessPoints.get(i).getLux().floatValue();
        }
    }

    private void loadAutoBrightnessConfigValues(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.AutoBrightness autoBrightness = config.getAutoBrightness();
        loadAutoBrightnessBrighteningLightDebounce(autoBrightness);
        loadAutoBrightnessDarkeningLightDebounce(autoBrightness);
        loadAutoBrightnessBrighteningLightDebounceIdle(autoBrightness);
        loadAutoBrightnessDarkeningLightDebounceIdle(autoBrightness);
        this.mDisplayBrightnessMapping = new com.android.server.display.config.DisplayBrightnessMappingConfig(this.mContext, this.mFlags, autoBrightness, getBacklightToBrightnessSpline());
        loadEnableAutoBrightness(autoBrightness);
    }

    private void loadAutoBrightnessBrighteningLightDebounce(com.android.server.display.config.AutoBrightness autoBrightnessConfig) {
        if (autoBrightnessConfig == null || autoBrightnessConfig.getBrighteningLightDebounceMillis() == null) {
            this.mAutoBrightnessBrighteningLightDebounce = this.mContext.getResources().getInteger(android.R.integer.config_attentiveWarningDuration);
        } else {
            this.mAutoBrightnessBrighteningLightDebounce = autoBrightnessConfig.getBrighteningLightDebounceMillis().intValue();
        }
    }

    private void loadAutoBrightnessDarkeningLightDebounce(com.android.server.display.config.AutoBrightness autoBrightnessConfig) {
        if (autoBrightnessConfig == null || autoBrightnessConfig.getDarkeningLightDebounceMillis() == null) {
            this.mAutoBrightnessDarkeningLightDebounce = this.mContext.getResources().getInteger(android.R.integer.config_audio_alarm_min_vol);
        } else {
            this.mAutoBrightnessDarkeningLightDebounce = autoBrightnessConfig.getDarkeningLightDebounceMillis().intValue();
        }
    }

    private void loadAutoBrightnessBrighteningLightDebounceIdle(com.android.server.display.config.AutoBrightness autoBrightnessConfig) {
        if (autoBrightnessConfig == null || autoBrightnessConfig.getBrighteningLightDebounceIdleMillis() == null) {
            this.mAutoBrightnessBrighteningLightDebounceIdle = this.mAutoBrightnessBrighteningLightDebounce;
        } else {
            this.mAutoBrightnessBrighteningLightDebounceIdle = autoBrightnessConfig.getBrighteningLightDebounceIdleMillis().intValue();
        }
    }

    private void loadAutoBrightnessDarkeningLightDebounceIdle(com.android.server.display.config.AutoBrightness autoBrightnessConfig) {
        if (autoBrightnessConfig == null || autoBrightnessConfig.getDarkeningLightDebounceIdleMillis() == null) {
            this.mAutoBrightnessDarkeningLightDebounceIdle = this.mAutoBrightnessDarkeningLightDebounce;
        } else {
            this.mAutoBrightnessDarkeningLightDebounceIdle = autoBrightnessConfig.getDarkeningLightDebounceIdleMillis().intValue();
        }
    }

    private void loadAutoBrightnessAvailableFromConfigXml() {
        this.mAutoBrightnessAvailable = this.mContext.getResources().getBoolean(android.R.bool.config_autoPowerModeUseMotionSensor);
    }

    private void loadBrightnessMapFromConfigXml() {
        android.content.res.Resources res = this.mContext.getResources();
        float[] sysNits = com.android.server.display.BrightnessMappingStrategy.getFloatArray(res.obtainTypedArray(android.R.array.config_roundedCornerRadiusArray));
        int[] sysBrightness = res.getIntArray(android.R.array.config_roundedCornerRadiusAdjustmentArray);
        float[] sysBrightnessFloat = new float[sysBrightness.length];
        for (int i = 0; i < sysBrightness.length; i++) {
            sysBrightnessFloat[i] = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(sysBrightness[i]);
        }
        int i2 = sysBrightnessFloat.length;
        if (i2 == 0 || sysNits.length == 0) {
            setSimpleMappingStrategyValues();
            return;
        }
        this.mRawNits = sysNits;
        this.mRawBacklight = sysBrightnessFloat;
        constrainNitsAndBacklightArrays();
    }

    private void setSimpleMappingStrategyValues() {
        this.mNits = null;
        this.mBacklight = null;
        float[] simpleMappingStrategyArray = {0.0f, 1.0f};
        this.mBrightnessToBacklightSpline = android.util.Spline.createSpline(simpleMappingStrategyArray, simpleMappingStrategyArray);
        this.mBacklightToBrightnessSpline = android.util.Spline.createSpline(simpleMappingStrategyArray, simpleMappingStrategyArray);
    }

    private void constrainNitsAndBacklightArrays() {
        float newBacklightVal;
        float newNitsVal;
        if (this.mRawBacklight[0] > this.mBacklightMinimum || this.mRawBacklight[this.mRawBacklight.length - 1] < this.mBacklightMaximum || this.mBacklightMinimum > this.mBacklightMaximum) {
            throw new java.lang.IllegalStateException("Min or max values are invalid; raw min=" + this.mRawBacklight[0] + "; raw max=" + this.mRawBacklight[this.mRawBacklight.length - 1] + "; backlight min=" + this.mBacklightMinimum + "; backlight max=" + this.mBacklightMaximum);
        }
        float[] newNits = new float[this.mRawBacklight.length];
        float[] newBacklight = new float[this.mRawBacklight.length];
        int newStart = 0;
        int i = 0;
        while (true) {
            if (i >= this.mRawBacklight.length - 1) {
                break;
            }
            if (this.mRawBacklight[i + 1] <= this.mBacklightMinimum) {
                i++;
            } else {
                newStart = i;
                break;
            }
        }
        int i2 = 0;
        int newIndex = 0;
        int i3 = newStart;
        while (i3 < this.mRawBacklight.length && i2 == 0) {
            newIndex = i3 - newStart;
            i2 = (this.mRawBacklight[i3] >= this.mBacklightMaximum || i3 >= this.mRawBacklight.length - 1) ? 1 : 0;
            if (newIndex == 0) {
                newBacklightVal = android.util.MathUtils.max(this.mRawBacklight[i3], this.mBacklightMinimum);
                newNitsVal = rawBacklightToNits(i3, newBacklightVal);
            } else if (i2 != 0) {
                newBacklightVal = android.util.MathUtils.min(this.mRawBacklight[i3], this.mBacklightMaximum);
                newNitsVal = rawBacklightToNits(i3 - 1, newBacklightVal);
            } else {
                newBacklightVal = this.mRawBacklight[i3];
                newNitsVal = this.mRawNits[i3];
            }
            newBacklight[newIndex] = newBacklightVal;
            newNits[newIndex] = newNitsVal;
            i3++;
        }
        this.mBacklight = java.util.Arrays.copyOf(newBacklight, newIndex + 1);
        this.mNits = java.util.Arrays.copyOf(newNits, newIndex + 1);
        createBacklightConversionSplines();
    }

    private float rawBacklightToNits(int i, float backlight) {
        return android.util.MathUtils.map(this.mRawBacklight[i], this.mRawBacklight[i + 1], this.mRawNits[i], this.mRawNits[i + 1], backlight);
    }

    private void createBacklightConversionSplines() {
        android.util.Spline splineCreateSpline;
        android.util.Spline splineCreateSpline2;
        android.util.Spline splineCreateSpline3;
        android.util.Spline splineCreateSpline4;
        this.mBrightness = new float[this.mBacklight.length];
        for (int i = 0; i < this.mBrightness.length; i++) {
            this.mBrightness[i] = android.util.MathUtils.map(this.mBacklight[0], this.mBacklight[this.mBacklight.length - 1], 0.0f, 1.0f, this.mBacklight[i]);
        }
        int i2 = this.mInterpolationType;
        if (i2 == 1) {
            splineCreateSpline = android.util.Spline.createLinearSpline(this.mBrightness, this.mBacklight);
        } else {
            splineCreateSpline = android.util.Spline.createSpline(this.mBrightness, this.mBacklight);
        }
        this.mBrightnessToBacklightSpline = splineCreateSpline;
        if (this.mInterpolationType == 1) {
            splineCreateSpline2 = android.util.Spline.createLinearSpline(this.mBacklight, this.mBrightness);
        } else {
            splineCreateSpline2 = android.util.Spline.createSpline(this.mBacklight, this.mBrightness);
        }
        this.mBacklightToBrightnessSpline = splineCreateSpline2;
        if (this.mInterpolationType == 1) {
            splineCreateSpline3 = android.util.Spline.createLinearSpline(this.mBacklight, this.mNits);
        } else {
            splineCreateSpline3 = android.util.Spline.createSpline(this.mBacklight, this.mNits);
        }
        this.mBacklightToNitsSpline = splineCreateSpline3;
        if (this.mInterpolationType == 1) {
            splineCreateSpline4 = android.util.Spline.createLinearSpline(this.mNits, this.mBacklight);
        } else {
            splineCreateSpline4 = android.util.Spline.createSpline(this.mNits, this.mBacklight);
        }
        this.mNitsToBacklightSpline = splineCreateSpline4;
    }

    private void loadQuirks(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.DisplayQuirks quirks = config.getQuirks();
        if (quirks != null) {
            this.mQuirks = new java.util.ArrayList(quirks.getQuirk());
        }
    }

    private void loadHighBrightnessModeData(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.HighBrightnessMode hbm = config.getHighBrightnessMode();
        if (hbm != null) {
            this.mIsHighBrightnessModeEnabled = hbm.getEnabled();
            this.mHbmData = new com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData();
            this.mHbmData.minimumLux = hbm.getMinimumLux_all().floatValue();
            float transitionPointBacklightScale = hbm.getTransitionPoint_all().floatValue();
            if (transitionPointBacklightScale >= this.mBacklightMaximum) {
                throw new java.lang.IllegalArgumentException("HBM transition point invalid. " + this.mHbmData.transitionPoint + " is not less than " + this.mBacklightMaximum);
            }
            this.mHbmData.transitionPoint = getBrightnessFromBacklight(transitionPointBacklightScale);
            com.android.server.display.config.HbmTiming hbmTiming = hbm.getTiming_all();
            this.mHbmData.timeWindowMillis = hbmTiming.getTimeWindowSecs_all().longValue() * 1000;
            this.mHbmData.timeMaxMillis = hbmTiming.getTimeMaxSecs_all().longValue() * 1000;
            this.mHbmData.timeMinMillis = hbmTiming.getTimeMinSecs_all().longValue() * 1000;
            this.mHbmData.allowInLowPowerMode = hbm.getAllowInLowPowerMode_all();
            com.android.server.display.config.RefreshRateRange rr = hbm.getRefreshRate_all();
            if (rr != null) {
                float min = rr.getMinimum().floatValue();
                float max = rr.getMaximum().floatValue();
                this.mRefreshRateLimitations.add(new android.hardware.display.DisplayManagerInternal.RefreshRateLimitation(1, min, max));
            }
            java.math.BigDecimal minHdrPctOfScreen = hbm.getMinimumHdrPercentOfScreen_all();
            if (minHdrPctOfScreen == null) {
                this.mHbmData.minimumHdrPercentOfScreen = 0.5f;
            } else {
                this.mHbmData.minimumHdrPercentOfScreen = minHdrPctOfScreen.floatValue();
                if (this.mHbmData.minimumHdrPercentOfScreen > 1.0f || this.mHbmData.minimumHdrPercentOfScreen < 0.0f) {
                    android.util.Slog.w(TAG, "Invalid minimum HDR percent of screen: " + java.lang.String.valueOf(this.mHbmData.minimumHdrPercentOfScreen));
                    this.mHbmData.minimumHdrPercentOfScreen = 0.5f;
                }
            }
            this.mSdrToHdrRatioSpline = loadSdrHdrRatioMap(hbm);
        }
    }

    private void loadLuxThrottling(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.LuxThrottling cfg = config.getLuxThrottling();
        if (cfg != null) {
            com.android.server.display.config.HighBrightnessMode hbm = config.getHighBrightnessMode();
            float hbmTransitionPoint = hbm != null ? hbm.getTransitionPoint_all().floatValue() : 1.0f;
            java.util.List<com.android.server.display.config.BrightnessLimitMap> limitMaps = cfg.getBrightnessLimitMap();
            for (com.android.server.display.config.BrightnessLimitMap map : limitMaps) {
                com.android.server.display.config.PredefinedBrightnessLimitNames type = map.getType();
                com.android.server.display.DisplayDeviceConfig.BrightnessLimitMapType mappedType = com.android.server.display.DisplayDeviceConfig.BrightnessLimitMapType.convert(type);
                if (mappedType == null) {
                    android.util.Slog.wtf(TAG, "Invalid NBM config: unsupported map type=" + type);
                } else if (this.mLuxThrottlingData.containsKey(mappedType)) {
                    android.util.Slog.wtf(TAG, "Invalid NBM config: duplicate map type=" + mappedType);
                } else {
                    java.util.Map<java.lang.Float, java.lang.Float> luxToTransitionPointMap = new java.util.HashMap<>();
                    java.util.List<com.android.server.display.config.NonNegativeFloatToFloatPoint> points = map.getMap().getPoint();
                    for (com.android.server.display.config.NonNegativeFloatToFloatPoint point : points) {
                        float lux = point.getFirst().floatValue();
                        float maxBrightness = point.getSecond().floatValue();
                        com.android.server.display.config.LuxThrottling cfg2 = cfg;
                        if (maxBrightness > hbmTransitionPoint) {
                            android.util.Slog.wtf(TAG, "Invalid NBM config: maxBrightness is greater than hbm.transitionPoint. type=" + type + "; lux=" + lux + "; maxBrightness=" + maxBrightness);
                            hbm = hbm;
                            cfg = cfg2;
                            hbmTransitionPoint = hbmTransitionPoint;
                        } else {
                            com.android.server.display.config.HighBrightnessMode hbm2 = hbm;
                            float hbmTransitionPoint2 = hbmTransitionPoint;
                            if (luxToTransitionPointMap.containsKey(java.lang.Float.valueOf(lux))) {
                                android.util.Slog.wtf(TAG, "Invalid NBM config: duplicate lux key. type=" + type + "; lux=" + lux);
                                hbm = hbm2;
                                cfg = cfg2;
                                hbmTransitionPoint = hbmTransitionPoint2;
                            } else {
                                luxToTransitionPointMap.put(java.lang.Float.valueOf(lux), java.lang.Float.valueOf(getBrightnessFromBacklight(maxBrightness)));
                                hbm = hbm2;
                                cfg = cfg2;
                                hbmTransitionPoint = hbmTransitionPoint2;
                            }
                        }
                    }
                    com.android.server.display.config.LuxThrottling cfg3 = cfg;
                    com.android.server.display.config.HighBrightnessMode hbm3 = hbm;
                    float hbmTransitionPoint3 = hbmTransitionPoint;
                    if (!luxToTransitionPointMap.isEmpty()) {
                        this.mLuxThrottlingData.put(mappedType, luxToTransitionPointMap);
                    }
                    hbm = hbm3;
                    cfg = cfg3;
                    hbmTransitionPoint = hbmTransitionPoint3;
                }
            }
        }
    }

    private void loadBrightnessRamps(com.android.server.display.config.DisplayConfiguration config) {
        loadBrightnessRampsInteractive(config);
        loadBrightnessRampsIdle(config);
    }

    private void loadBrightnessRampsInteractive(com.android.server.display.config.DisplayConfiguration config) {
        java.math.BigDecimal fastDownDecimal = config.getScreenBrightnessRampFastDecrease();
        java.math.BigDecimal fastUpDecimal = config.getScreenBrightnessRampFastIncrease();
        java.math.BigDecimal slowDownDecimal = config.getScreenBrightnessRampSlowDecrease();
        java.math.BigDecimal slowUpDecimal = config.getScreenBrightnessRampSlowIncrease();
        if (fastDownDecimal != null && fastUpDecimal != null && slowDownDecimal != null && slowUpDecimal != null) {
            this.mBrightnessRampFastDecrease = fastDownDecimal.floatValue();
            this.mBrightnessRampFastIncrease = fastUpDecimal.floatValue();
            this.mBrightnessRampSlowDecrease = slowDownDecimal.floatValue();
            this.mBrightnessRampSlowIncrease = slowUpDecimal.floatValue();
        } else {
            if (fastDownDecimal != null || fastUpDecimal != null || slowDownDecimal != null || slowUpDecimal != null) {
                android.util.Slog.w(TAG, "Per display brightness ramp values ignored because not all values are present in display device config");
            }
            loadBrightnessRampsFromConfigXml();
        }
        java.math.BigInteger increaseMax = config.getScreenBrightnessRampIncreaseMaxMillis();
        if (increaseMax != null) {
            this.mBrightnessRampIncreaseMaxMillis = increaseMax.intValue();
        }
        java.math.BigInteger decreaseMax = config.getScreenBrightnessRampDecreaseMaxMillis();
        if (decreaseMax != null) {
            this.mBrightnessRampDecreaseMaxMillis = decreaseMax.intValue();
        }
    }

    private void loadBrightnessRampsIdle(com.android.server.display.config.DisplayConfiguration config) {
        java.math.BigDecimal slowDownDecimalIdle = config.getScreenBrightnessRampSlowDecreaseIdle();
        java.math.BigDecimal slowUpDecimalIdle = config.getScreenBrightnessRampSlowIncreaseIdle();
        if (slowDownDecimalIdle != null && slowUpDecimalIdle != null) {
            this.mBrightnessRampSlowDecreaseIdle = slowDownDecimalIdle.floatValue();
            this.mBrightnessRampSlowIncreaseIdle = slowUpDecimalIdle.floatValue();
        } else {
            if (slowDownDecimalIdle != null || slowUpDecimalIdle != null) {
                android.util.Slog.w(TAG, "Per display idle brightness ramp values ignored because not all values are present in display device config");
            }
            this.mBrightnessRampSlowDecreaseIdle = this.mBrightnessRampSlowDecrease;
            this.mBrightnessRampSlowIncreaseIdle = this.mBrightnessRampSlowIncrease;
        }
        java.math.BigInteger increaseMaxIdle = config.getScreenBrightnessRampIncreaseMaxIdleMillis();
        if (increaseMaxIdle != null) {
            this.mBrightnessRampIncreaseMaxIdleMillis = increaseMaxIdle.intValue();
        } else {
            this.mBrightnessRampIncreaseMaxIdleMillis = this.mBrightnessRampIncreaseMaxMillis;
        }
        java.math.BigInteger decreaseMaxIdle = config.getScreenBrightnessRampDecreaseMaxIdleMillis();
        if (decreaseMaxIdle != null) {
            this.mBrightnessRampDecreaseMaxIdleMillis = decreaseMaxIdle.intValue();
        } else {
            this.mBrightnessRampDecreaseMaxIdleMillis = this.mBrightnessRampDecreaseMaxMillis;
        }
    }

    private void loadBrightnessRampsFromConfigXml() {
        this.mBrightnessRampFastIncrease = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_bluetooth_idle_cur_ma));
        this.mBrightnessRampSlowIncrease = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_bluetooth_operating_voltage_mv));
        this.mBrightnessRampFastDecrease = this.mBrightnessRampFastIncrease;
        this.mBrightnessRampSlowDecrease = this.mBrightnessRampSlowIncrease;
    }

    private void loadAutoBrightnessConfigsFromConfigXml() {
        this.mDisplayBrightnessMapping = new com.android.server.display.config.DisplayBrightnessMappingConfig(this.mContext, this.mFlags, null, getBacklightToBrightnessSpline());
    }

    private void loadBrightnessChangeThresholdsFromXml() {
        loadBrightnessChangeThresholds(null);
    }

    private void loadBrightnessChangeThresholds(com.android.server.display.config.DisplayConfiguration config) {
        android.content.res.Resources res = this.mContext.getResources();
        this.mScreenBrightnessHysteresis = com.android.server.display.config.HysteresisLevels.loadDisplayBrightnessConfig(config, res);
        this.mScreenBrightnessIdleHysteresis = com.android.server.display.config.HysteresisLevels.loadDisplayBrightnessIdleConfig(config, res);
        this.mAmbientBrightnessHysteresis = com.android.server.display.config.HysteresisLevels.loadAmbientBrightnessConfig(config, res);
        this.mAmbientBrightnessIdleHysteresis = com.android.server.display.config.HysteresisLevels.loadAmbientBrightnessIdleConfig(config, res);
    }

    private boolean thermalStatusIsValid(com.android.server.display.config.ThermalStatus value) {
        if (value == null) {
            return false;
        }
        switch (value) {
        }
        return false;
    }

    static int convertThermalStatus(com.android.server.display.config.ThermalStatus value) {
        if (value == null) {
            return 0;
        }
        switch (value) {
            case none:
                break;
            case light:
                break;
            case moderate:
                break;
            case severe:
                break;
            case critical:
                break;
            case emergency:
                break;
            case shutdown:
                break;
            default:
                android.util.Slog.wtf(TAG, "Unexpected Thermal Status: " + value);
                break;
        }
        return 0;
    }

    private int convertInterpolationType(java.lang.String value) {
        if (android.text.TextUtils.isEmpty(value)) {
            return 0;
        }
        if ("linear".equals(value)) {
            return 1;
        }
        android.util.Slog.wtf(TAG, "Unexpected Interpolation Type: " + value);
        return 0;
    }

    private void loadAmbientHorizonFromDdc(com.android.server.display.config.DisplayConfiguration config) {
        java.math.BigInteger configLongHorizon = config.getAmbientLightHorizonLong();
        if (configLongHorizon != null) {
            this.mAmbientHorizonLong = configLongHorizon.intValue();
        }
        java.math.BigInteger configShortHorizon = config.getAmbientLightHorizonShort();
        if (configShortHorizon != null) {
            this.mAmbientHorizonShort = configShortHorizon.intValue();
        }
    }

    private void loadIdleScreenRefreshRateTimeoutConfigs(com.android.server.display.config.DisplayConfiguration config) {
        if (this.mFlags.isIdleScreenRefreshRateTimeoutEnabled() && config != null && config.getIdleScreenRefreshRateTimeout() != null) {
            validateIdleScreenRefreshRateTimeoutConfig(config.getIdleScreenRefreshRateTimeout());
            this.mIdleScreenRefreshRateTimeoutLuxThresholds = config.getIdleScreenRefreshRateTimeout().getLuxThresholds().getPoint();
        }
    }

    private void validateIdleScreenRefreshRateTimeoutConfig(com.android.server.display.config.IdleScreenRefreshRateTimeout idleScreenRefreshRateTimeoutConfig) {
        com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholds idleScreenRefreshRateTimeoutLuxThresholds = idleScreenRefreshRateTimeoutConfig.getLuxThresholds();
        if (idleScreenRefreshRateTimeoutLuxThresholds != null) {
            int previousLux = -1;
            for (com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholdPoint point : idleScreenRefreshRateTimeoutLuxThresholds.getPoint()) {
                int newLux = point.getLux().intValue();
                if (previousLux >= newLux) {
                    throw new java.lang.RuntimeException("Lux values should be in ascending order in the idle screen refresh rate timeout config");
                }
                int timeout = point.getTimeout().intValue();
                if (timeout < 0) {
                    throw new java.lang.RuntimeException("The timeout value cannot be negative in idle screen refresh rate timeout config");
                }
                previousLux = newLux;
            }
        }
    }

    public java.util.List<com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholdPoint> getIdleScreenRefreshRateTimeoutLuxThresholdPoint() {
        return this.mIdleScreenRefreshRateTimeoutLuxThresholds;
    }

    public static float[] getFloatArray(android.content.res.TypedArray array, float defaultValue) {
        int n = array.length();
        float[] vals = new float[n];
        for (int i = 0; i < n; i++) {
            vals[i] = array.getFloat(i, defaultValue);
        }
        array.recycle();
        return vals;
    }

    public static float[] getLuxLevels(int[] lux) {
        float[] levels = new float[lux.length + 1];
        for (int i = 0; i < lux.length; i++) {
            levels[i + 1] = lux[i];
        }
        return levels;
    }

    private void loadEnableAutoBrightness(com.android.server.display.config.AutoBrightness autobrightness) {
        this.mDdcAutoBrightnessAvailable = true;
        if (autobrightness != null) {
            this.mDdcAutoBrightnessAvailable = autobrightness.getEnabled();
        }
        this.mAutoBrightnessAvailable = this.mContext.getResources().getBoolean(android.R.bool.config_autoPowerModeUseMotionSensor) && this.mDdcAutoBrightnessAvailable;
    }

    private void loadScreenOffBrightnessSensorValueToLuxFromDdc(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.IntegerArray sensorValueToLux = config.getScreenOffBrightnessSensorValueToLux();
        if (sensorValueToLux == null) {
            return;
        }
        java.util.List<java.math.BigInteger> items = sensorValueToLux.getItem();
        this.mScreenOffBrightnessSensorValueToLux = new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            this.mScreenOffBrightnessSensorValueToLux[i] = items.get(i).intValue();
        }
    }

    private void loadUsiVersion(com.android.server.display.config.DisplayConfiguration config) {
        android.hardware.input.HostUsiVersion hostUsiVersion;
        com.android.server.display.config.UsiVersion usiVersion = config.getUsiVersion();
        if (usiVersion != null) {
            hostUsiVersion = new android.hardware.input.HostUsiVersion(usiVersion.getMajorVersion().intValue(), usiVersion.getMinorVersion().intValue());
        } else {
            hostUsiVersion = null;
        }
        this.mHostUsiVersion = hostUsiVersion;
    }

    private void loadBrightnessCapForWearBedtimeMode(com.android.server.display.config.DisplayConfiguration config) {
        if (config != null) {
            java.math.BigDecimal configBrightnessCap = config.getScreenBrightnessCapForWearBedtimeMode();
            if (configBrightnessCap != null) {
                this.mBrightnessCapForWearBedtimeMode = configBrightnessCap.floatValue();
            } else {
                loadBrightnessCapForWearBedtimeModeFromConfigXml();
            }
        }
    }

    private void loadBrightnessCapForWearBedtimeModeFromConfigXml() {
        this.mBrightnessCapForWearBedtimeMode = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_pressedStateDurationMillis));
    }

    static class HighBrightnessModeData {
        public boolean allowInLowPowerMode;
        public float minimumHdrPercentOfScreen;
        public float minimumLux;
        public long timeMaxMillis;
        public long timeMinMillis;
        public long timeWindowMillis;
        public float transitionPoint;

        HighBrightnessModeData() {
        }

        HighBrightnessModeData(float minimumLux, float transitionPoint, long timeWindowMillis, long timeMaxMillis, long timeMinMillis, boolean allowInLowPowerMode, float minimumHdrPercentOfScreen) {
            this.minimumLux = minimumLux;
            this.transitionPoint = transitionPoint;
            this.timeWindowMillis = timeWindowMillis;
            this.timeMaxMillis = timeMaxMillis;
            this.timeMinMillis = timeMinMillis;
            this.allowInLowPowerMode = allowInLowPowerMode;
            this.minimumHdrPercentOfScreen = minimumHdrPercentOfScreen;
        }

        public void copyTo(com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData other) {
            other.minimumLux = this.minimumLux;
            other.timeWindowMillis = this.timeWindowMillis;
            other.timeMaxMillis = this.timeMaxMillis;
            other.timeMinMillis = this.timeMinMillis;
            other.transitionPoint = this.transitionPoint;
            other.allowInLowPowerMode = this.allowInLowPowerMode;
            other.minimumHdrPercentOfScreen = this.minimumHdrPercentOfScreen;
        }

        public java.lang.String toString() {
            return "HBM{minLux: " + this.minimumLux + ", transition: " + this.transitionPoint + ", timeWindow: " + this.timeWindowMillis + "ms, timeMax: " + this.timeMaxMillis + "ms, timeMin: " + this.timeMinMillis + "ms, allowInLowPowerMode: " + this.allowInLowPowerMode + ", minimumHdrPercentOfScreen: " + this.minimumHdrPercentOfScreen + "} ";
        }
    }

    public static class PowerThrottlingConfigData {
        public final float brightnessLowestCapAllowed;
        public final int pollingWindowMillis;

        public PowerThrottlingConfigData(float brightnessLowestCapAllowed, int pollingWindowMillis) {
            this.brightnessLowestCapAllowed = brightnessLowestCapAllowed;
            this.pollingWindowMillis = pollingWindowMillis;
        }

        public java.lang.String toString() {
            return "PowerThrottlingConfigData{brightnessLowestCapAllowed: " + this.brightnessLowestCapAllowed + ", pollingWindowMillis: " + this.pollingWindowMillis + "} ";
        }
    }

    public static class PowerThrottlingData {
        public java.util.List<com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel> throttlingLevels;

        public static class ThrottlingLevel {
            public float powerQuotaMilliWatts;
            public int thermalStatus;

            public ThrottlingLevel(int thermalStatus, float powerQuotaMilliWatts) {
                this.thermalStatus = thermalStatus;
                this.powerQuotaMilliWatts = powerQuotaMilliWatts;
            }

            public java.lang.String toString() {
                return "[" + this.thermalStatus + "," + this.powerQuotaMilliWatts + "]";
            }

            public boolean equals(java.lang.Object obj) {
                if (!(obj instanceof com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel)) {
                    return false;
                }
                com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel otherThrottlingLevel = (com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel) obj;
                return otherThrottlingLevel.thermalStatus == this.thermalStatus && otherThrottlingLevel.powerQuotaMilliWatts == this.powerQuotaMilliWatts;
            }

            public int hashCode() {
                int result = (1 * 31) + this.thermalStatus;
                return (result * 31) + java.lang.Float.hashCode(this.powerQuotaMilliWatts);
            }
        }

        public static com.android.server.display.DisplayDeviceConfig.PowerThrottlingData create(java.util.List<com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel> throttlingLevels) {
            if (throttlingLevels == null || throttlingLevels.size() == 0) {
                android.util.Slog.e(com.android.server.display.DisplayDeviceConfig.TAG, "PowerThrottlingData received null or empty throttling levels");
                return null;
            }
            com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel prevLevel = throttlingLevels.get(0);
            int numLevels = throttlingLevels.size();
            for (int i = 1; i < numLevels; i++) {
                com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel thisLevel = throttlingLevels.get(i);
                if (thisLevel.thermalStatus <= prevLevel.thermalStatus) {
                    android.util.Slog.e(com.android.server.display.DisplayDeviceConfig.TAG, "powerThrottlingMap must be strictly increasing, ignoring configuration. ThermalStatus " + thisLevel.thermalStatus + " <= " + prevLevel.thermalStatus);
                    return null;
                }
                if (thisLevel.powerQuotaMilliWatts >= prevLevel.powerQuotaMilliWatts) {
                    android.util.Slog.e(com.android.server.display.DisplayDeviceConfig.TAG, "powerThrottlingMap must be strictly decreasing, ignoring configuration. powerQuotaMilliWatts " + thisLevel.powerQuotaMilliWatts + " >= " + prevLevel.powerQuotaMilliWatts);
                    return null;
                }
                prevLevel = thisLevel;
            }
            return new com.android.server.display.DisplayDeviceConfig.PowerThrottlingData(throttlingLevels);
        }

        public java.lang.String toString() {
            return "PowerThrottlingData{throttlingLevels:" + this.throttlingLevels + "} ";
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof com.android.server.display.DisplayDeviceConfig.PowerThrottlingData)) {
                return false;
            }
            com.android.server.display.DisplayDeviceConfig.PowerThrottlingData otherData = (com.android.server.display.DisplayDeviceConfig.PowerThrottlingData) obj;
            return this.throttlingLevels.equals(otherData.throttlingLevels);
        }

        public int hashCode() {
            return this.throttlingLevels.hashCode();
        }

        PowerThrottlingData(java.util.List<com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel> inLevels) {
            this.throttlingLevels = new java.util.ArrayList(inLevels.size());
            for (com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel level : inLevels) {
                this.throttlingLevels.add(new com.android.server.display.DisplayDeviceConfig.PowerThrottlingData.ThrottlingLevel(level.thermalStatus, level.powerQuotaMilliWatts));
            }
        }
    }

    public static class ThermalBrightnessThrottlingData {
        public java.util.List<com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel> throttlingLevels;

        public static class ThrottlingLevel {
            public float brightness;
            public int thermalStatus;

            public ThrottlingLevel(int thermalStatus, float brightness) {
                this.thermalStatus = thermalStatus;
                this.brightness = brightness;
            }

            public java.lang.String toString() {
                return "[" + this.thermalStatus + "," + this.brightness + "]";
            }

            public boolean equals(java.lang.Object obj) {
                if (!(obj instanceof com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel)) {
                    return false;
                }
                com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel otherThrottlingLevel = (com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel) obj;
                return otherThrottlingLevel.thermalStatus == this.thermalStatus && otherThrottlingLevel.brightness == this.brightness;
            }

            public int hashCode() {
                int result = (1 * 31) + this.thermalStatus;
                return (result * 31) + java.lang.Float.hashCode(this.brightness);
            }
        }

        public static com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData create(java.util.List<com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel> throttlingLevels) {
            if (throttlingLevels == null || throttlingLevels.size() == 0) {
                android.util.Slog.e(com.android.server.display.DisplayDeviceConfig.TAG, "BrightnessThrottlingData received null or empty throttling levels");
                return null;
            }
            com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel prevLevel = throttlingLevels.get(0);
            int numLevels = throttlingLevels.size();
            for (int i = 1; i < numLevels; i++) {
                com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel thisLevel = throttlingLevels.get(i);
                if (thisLevel.thermalStatus <= prevLevel.thermalStatus) {
                    android.util.Slog.e(com.android.server.display.DisplayDeviceConfig.TAG, "brightnessThrottlingMap must be strictly increasing, ignoring configuration. ThermalStatus " + thisLevel.thermalStatus + " <= " + prevLevel.thermalStatus);
                    return null;
                }
                if (thisLevel.brightness >= prevLevel.brightness) {
                    android.util.Slog.e(com.android.server.display.DisplayDeviceConfig.TAG, "brightnessThrottlingMap must be strictly decreasing, ignoring configuration. Brightness " + thisLevel.brightness + " >= " + thisLevel.brightness);
                    return null;
                }
                prevLevel = thisLevel;
            }
            for (com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel level : throttlingLevels) {
                if (level.brightness > 1.0f) {
                    android.util.Slog.e(com.android.server.display.DisplayDeviceConfig.TAG, "brightnessThrottlingMap contains a brightness value exceeding system max. Brightness " + level.brightness + " > 1.0");
                    return null;
                }
            }
            return new com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData(throttlingLevels);
        }

        public java.lang.String toString() {
            return "ThermalBrightnessThrottlingData{throttlingLevels:" + this.throttlingLevels + "} ";
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData)) {
                return false;
            }
            com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData otherData = (com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData) obj;
            return this.throttlingLevels.equals(otherData.throttlingLevels);
        }

        public int hashCode() {
            return this.throttlingLevels.hashCode();
        }

        ThermalBrightnessThrottlingData(java.util.List<com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel> inLevels) {
            this.throttlingLevels = new java.util.ArrayList(inLevels.size());
            for (com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel level : inLevels) {
                this.throttlingLevels.add(new com.android.server.display.DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel(level.thermalStatus, level.brightness));
            }
        }
    }

    public enum BrightnessLimitMapType {
        DEFAULT,
        ADAPTIVE;

        /* JADX INFO: Access modifiers changed from: private */
        public static com.android.server.display.DisplayDeviceConfig.BrightnessLimitMapType convert(com.android.server.display.config.PredefinedBrightnessLimitNames type) {
            switch (type) {
                case _default:
                    return DEFAULT;
                case adaptive:
                    return ADAPTIVE;
                default:
                    return null;
            }
        }
    }
}
