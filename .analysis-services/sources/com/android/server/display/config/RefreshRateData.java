package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class RefreshRateData {
    private static final int DEFAULT_PEAK_REFRESH_RATE = 0;
    private static final int DEFAULT_REFRESH_RATE = 60;
    public static com.android.server.display.config.RefreshRateData DEFAULT_REFRESH_RATE_DATA = loadRefreshRateData(null, null);
    private static final int DEFAULT_REFRESH_RATE_IN_HBM = 0;
    public final int defaultPeakRefreshRate;
    public final int defaultRefreshRate;
    public final int defaultRefreshRateInHbmHdr;
    public final int defaultRefreshRateInHbmSunlight;
    public final java.util.List<com.android.server.display.config.SupportedModeData> lowLightBlockingZoneSupportedModes;
    public final java.util.List<com.android.server.display.config.SupportedModeData> lowPowerSupportedModes;

    public RefreshRateData(int defaultRefreshRate, int defaultPeakRefreshRate, int defaultRefreshRateInHbmHdr, int defaultRefreshRateInHbmSunlight, java.util.List<com.android.server.display.config.SupportedModeData> lowPowerSupportedModes, java.util.List<com.android.server.display.config.SupportedModeData> lowLightBlockingZoneSupportedModes) {
        this.defaultRefreshRate = defaultRefreshRate;
        this.defaultPeakRefreshRate = defaultPeakRefreshRate;
        this.defaultRefreshRateInHbmHdr = defaultRefreshRateInHbmHdr;
        this.defaultRefreshRateInHbmSunlight = defaultRefreshRateInHbmSunlight;
        this.lowPowerSupportedModes = java.util.Collections.unmodifiableList(lowPowerSupportedModes);
        this.lowLightBlockingZoneSupportedModes = java.util.Collections.unmodifiableList(lowLightBlockingZoneSupportedModes);
    }

    public java.lang.String toString() {
        return "RefreshRateData {defaultRefreshRate: " + this.defaultRefreshRate + ", defaultPeakRefreshRate: " + this.defaultPeakRefreshRate + ", defaultRefreshRateInHbmHdr: " + this.defaultRefreshRateInHbmHdr + ", defaultRefreshRateInHbmSunlight: " + this.defaultRefreshRateInHbmSunlight + ", lowPowerSupportedModes=" + this.lowPowerSupportedModes + ", lowLightBlockingZoneSupportedModes=" + this.lowLightBlockingZoneSupportedModes + "} ";
    }

    public static com.android.server.display.config.RefreshRateData loadRefreshRateData(com.android.server.display.config.DisplayConfiguration config, android.content.res.Resources resources) {
        com.android.server.display.config.RefreshRateConfigs refreshRateConfigs = config == null ? null : config.getRefreshRate();
        int defaultRefreshRate = loadDefaultRefreshRate(refreshRateConfigs, resources);
        int defaultPeakRefreshRate = loadDefaultPeakRefreshRate(refreshRateConfigs, resources);
        int defaultRefreshRateInHbmHdr = loadDefaultRefreshRateInHbm(refreshRateConfigs, resources);
        int defaultRefreshRateInHbmSunlight = loadDefaultRefreshRateInHbmSunlight(refreshRateConfigs, resources);
        com.android.server.display.config.NonNegativeFloatToFloatMap lowPowerModes = refreshRateConfigs == null ? null : refreshRateConfigs.getLowPowerSupportedModes();
        java.util.List<com.android.server.display.config.SupportedModeData> lowPowerSupportedModes = com.android.server.display.config.SupportedModeData.load(lowPowerModes);
        com.android.server.display.config.BlockingZoneConfig lowerZoneConfig = refreshRateConfigs == null ? null : refreshRateConfigs.getLowerBlockingZoneConfigs();
        com.android.server.display.config.NonNegativeFloatToFloatMap lowerZoneModes = lowerZoneConfig != null ? lowerZoneConfig.getSupportedModes() : null;
        java.util.List<com.android.server.display.config.SupportedModeData> lowLightSupportedModes = com.android.server.display.config.SupportedModeData.load(lowerZoneModes);
        return new com.android.server.display.config.RefreshRateData(defaultRefreshRate, defaultPeakRefreshRate, defaultRefreshRateInHbmHdr, defaultRefreshRateInHbmSunlight, lowPowerSupportedModes, lowLightSupportedModes);
    }

    private static int loadDefaultRefreshRate(com.android.server.display.config.RefreshRateConfigs refreshRateConfigs, android.content.res.Resources resources) {
        if (refreshRateConfigs != null && refreshRateConfigs.getDefaultRefreshRate() != null) {
            return refreshRateConfigs.getDefaultRefreshRate().intValue();
        }
        if (resources != null) {
            return resources.getInteger(android.R.integer.config_defaultNightDisplayCustomStartTime);
        }
        return 60;
    }

    private static int loadDefaultPeakRefreshRate(com.android.server.display.config.RefreshRateConfigs refreshRateConfigs, android.content.res.Resources resources) {
        if (refreshRateConfigs != null && refreshRateConfigs.getDefaultPeakRefreshRate() != null) {
            return refreshRateConfigs.getDefaultPeakRefreshRate().intValue();
        }
        if (resources != null) {
            return resources.getInteger(android.R.integer.config_defaultNightDisplayAutoMode);
        }
        return 0;
    }

    private static int loadDefaultRefreshRateInHbm(com.android.server.display.config.RefreshRateConfigs refreshRateConfigs, android.content.res.Resources resources) {
        if (refreshRateConfigs != null && refreshRateConfigs.getDefaultRefreshRateInHbmHdr() != null) {
            return refreshRateConfigs.getDefaultRefreshRateInHbmHdr().intValue();
        }
        if (resources != null) {
            return resources.getInteger(android.R.integer.config_defaultNightMode);
        }
        return 0;
    }

    private static int loadDefaultRefreshRateInHbmSunlight(com.android.server.display.config.RefreshRateConfigs refreshRateConfigs, android.content.res.Resources resources) {
        if (refreshRateConfigs != null && refreshRateConfigs.getDefaultRefreshRateInHbmSunlight() != null) {
            return refreshRateConfigs.getDefaultRefreshRateInHbmSunlight().intValue();
        }
        if (resources != null) {
            return resources.getInteger(android.R.integer.config_defaultNotificationLedOff);
        }
        return 0;
    }
}
