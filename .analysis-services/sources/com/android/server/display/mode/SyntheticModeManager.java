package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
public class SyntheticModeManager {
    private static final float FLOAT_TOLERANCE = 0.01f;
    private static final float SYNTHETIC_MODE_HIGH_BOUNDARY = 60.01f;
    private static final float SYNTHETIC_MODE_REFRESH_RATE = 60.0f;
    private final boolean mSynthetic60HzModesEnabled;

    public SyntheticModeManager(com.android.server.display.feature.DisplayManagerFlags flags) {
        this.mSynthetic60HzModesEnabled = flags.isSynthetic60HzModesEnabled();
    }

    public android.view.Display.Mode[] createAppSupportedModes(com.android.server.display.DisplayDeviceConfig config, android.view.Display.Mode[] modes) {
        if (config.isVrrSupportEnabled() && this.mSynthetic60HzModesEnabled) {
            java.util.List<android.view.Display.Mode> appSupportedModes = new java.util.ArrayList<>();
            java.util.Map<android.util.Size, int[]> sizes = new java.util.LinkedHashMap<>();
            int nextModeId = 0;
            int i = 0;
            for (android.view.Display.Mode mode : modes) {
                if (mode.getRefreshRate() > SYNTHETIC_MODE_HIGH_BOUNDARY) {
                    appSupportedModes.add(mode);
                }
                if (mode.getModeId() > nextModeId) {
                    nextModeId = mode.getModeId();
                }
                float divisor = mode.getVsyncRate() / 60.0f;
                boolean is60HzAchievable = java.lang.Math.abs(divisor - ((float) java.lang.Math.round(divisor))) < 0.01f;
                if (is60HzAchievable) {
                    sizes.put(new android.util.Size(mode.getPhysicalWidth(), mode.getPhysicalHeight()), mode.getSupportedHdrTypes());
                }
            }
            for (java.util.Map.Entry<android.util.Size, int[]> entry : sizes.entrySet()) {
                nextModeId++;
                android.util.Size size = entry.getKey();
                int[] hdrTypes = entry.getValue();
                appSupportedModes.add(new android.view.Display.Mode(nextModeId, size.getWidth(), size.getHeight(), 60.0f, 60.0f, true, new float[i], hdrTypes));
                i = 0;
            }
            android.view.Display.Mode[] appSupportedModesArr = new android.view.Display.Mode[appSupportedModes.size()];
            return (android.view.Display.Mode[]) appSupportedModes.toArray(appSupportedModesArr);
        }
        return modes;
    }
}
