package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
interface Vote {
    public static final int APP_REQUEST_REFRESH_RATE_RANGE_PRIORITY_CUTOFF = 5;
    public static final int INVALID_SIZE = -1;
    public static final int MAX_PRIORITY = 20;
    public static final int MIN_PRIORITY = 0;
    public static final int PRIORITY_APP_REQUEST_BASE_MODE_REFRESH_RATE = 6;
    public static final int PRIORITY_APP_REQUEST_RENDER_FRAME_RATE_RANGE = 5;
    public static final int PRIORITY_APP_REQUEST_SIZE = 7;
    public static final int PRIORITY_AUTH_OPTIMIZER_RENDER_FRAME_RATE = 12;
    public static final int PRIORITY_DEFAULT_RENDER_FRAME_RATE = 0;
    public static final int PRIORITY_FLICKER_REFRESH_RATE = 1;
    public static final int PRIORITY_FLICKER_REFRESH_RATE_SWITCH = 17;
    public static final int PRIORITY_HIGH_BRIGHTNESS_MODE = 2;
    public static final int PRIORITY_LAYOUT_LIMITED_FRAME_RATE = 13;
    public static final int PRIORITY_LIMIT_MODE = 11;
    public static final int PRIORITY_LOW_POWER_MODE_MODES = 15;
    public static final int PRIORITY_LOW_POWER_MODE_RENDER_RATE = 16;
    public static final int PRIORITY_PROXIMITY = 19;
    public static final int PRIORITY_SKIN_TEMPERATURE = 18;
    public static final int PRIORITY_SYNCHRONIZED_REFRESH_RATE = 10;
    public static final int PRIORITY_SYSTEM_REQUESTED_MODES = 14;
    public static final int PRIORITY_UDFPS = 20;
    public static final int PRIORITY_USER_SETTING_DISPLAY_PREFERRED_SIZE = 4;
    public static final int PRIORITY_USER_SETTING_MIN_RENDER_FRAME_RATE = 3;
    public static final int PRIORITY_USER_SETTING_PEAK_REFRESH_RATE = 8;
    public static final int PRIORITY_USER_SETTING_PEAK_RENDER_FRAME_RATE = 9;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Priority {
    }

    void updateSummary(com.android.server.display.mode.VoteSummary voteSummary);

    static com.android.server.display.mode.Vote forPhysicalRefreshRates(float minRefreshRate, float maxRefreshRate) {
        return new com.android.server.display.mode.CombinedVote(java.util.List.of(new com.android.server.display.mode.RefreshRateVote.PhysicalVote(minRefreshRate, maxRefreshRate), new com.android.server.display.mode.DisableRefreshRateSwitchingVote(minRefreshRate == maxRefreshRate)));
    }

    static com.android.server.display.mode.Vote forRenderFrameRates(float minFrameRate, float maxFrameRate) {
        return new com.android.server.display.mode.RefreshRateVote.RenderVote(minFrameRate, maxFrameRate);
    }

    static com.android.server.display.mode.Vote forSize(int width, int height) {
        return new com.android.server.display.mode.SizeVote(width, height, width, height);
    }

    static com.android.server.display.mode.Vote forSizeAndPhysicalRefreshRatesRange(int minWidth, int minHeight, int width, int height, float minRefreshRate, float maxRefreshRate) {
        return new com.android.server.display.mode.CombinedVote(java.util.List.of(new com.android.server.display.mode.SizeVote(width, height, minWidth, minHeight), new com.android.server.display.mode.RefreshRateVote.PhysicalVote(minRefreshRate, maxRefreshRate), new com.android.server.display.mode.DisableRefreshRateSwitchingVote(minRefreshRate == maxRefreshRate)));
    }

    static com.android.server.display.mode.Vote forDisableRefreshRateSwitching() {
        return new com.android.server.display.mode.DisableRefreshRateSwitchingVote(true);
    }

    static com.android.server.display.mode.Vote forBaseModeRefreshRate(float baseModeRefreshRate) {
        return new com.android.server.display.mode.BaseModeRefreshRateVote(baseModeRefreshRate);
    }

    static com.android.server.display.mode.Vote forRequestedRefreshRate(float refreshRate) {
        return new com.android.server.display.mode.RequestedRefreshRateVote(refreshRate);
    }

    static com.android.server.display.mode.Vote forSupportedRefreshRates(java.util.List<com.android.server.display.config.SupportedModeData> supportedModes) {
        if (supportedModes.isEmpty()) {
            return null;
        }
        java.util.List<com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates> rates = new java.util.ArrayList<>();
        for (com.android.server.display.config.SupportedModeData data : supportedModes) {
            rates.add(new com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates(data.refreshRate, data.vsyncRate));
        }
        return new com.android.server.display.mode.SupportedRefreshRatesVote(rates);
    }

    static com.android.server.display.mode.Vote forSupportedModes(java.util.List<java.lang.Integer> modeIds) {
        return new com.android.server.display.mode.SupportedModesVote(modeIds);
    }

    static java.lang.String priorityToString(int priority) {
        switch (priority) {
            case 0:
                return "PRIORITY_DEFAULT_REFRESH_RATE";
            case 1:
                return "PRIORITY_FLICKER_REFRESH_RATE";
            case 2:
                return "PRIORITY_HIGH_BRIGHTNESS_MODE";
            case 3:
                return "PRIORITY_USER_SETTING_MIN_RENDER_FRAME_RATE";
            case 4:
                return "PRIORITY_USER_SETTING_DISPLAY_PREFERRED_SIZE";
            case 5:
                return "PRIORITY_APP_REQUEST_RENDER_FRAME_RATE_RANGE";
            case 6:
                return "PRIORITY_APP_REQUEST_BASE_MODE_REFRESH_RATE";
            case 7:
                return "PRIORITY_APP_REQUEST_SIZE";
            case 8:
                return "PRIORITY_USER_SETTING_PEAK_REFRESH_RATE";
            case 9:
                return "PRIORITY_USER_SETTING_PEAK_RENDER_FRAME_RATE";
            case 10:
                return "PRIORITY_SYNCHRONIZED_REFRESH_RATE";
            case 11:
                return "PRIORITY_LIMIT_MODE";
            case 12:
                return "PRIORITY_AUTH_OPTIMIZER_RENDER_FRAME_RATE";
            case 13:
                return "PRIORITY_LAYOUT_LIMITED_FRAME_RATE";
            case 14:
                return "PRIORITY_SYSTEM_REQUESTED_MODES";
            case 15:
                return "PRIORITY_LOW_POWER_MODE_MODES";
            case 16:
                return "PRIORITY_LOW_POWER_MODE_RENDER_RATE";
            case 17:
                return "PRIORITY_FLICKER_REFRESH_RATE_SWITCH";
            case 18:
                return "PRIORITY_SKIN_TEMPERATURE";
            case 19:
                return "PRIORITY_PROXIMITY";
            case 20:
                return "PRIORITY_UDFPS";
            default:
                return java.lang.Integer.toString(priority);
        }
    }
}
