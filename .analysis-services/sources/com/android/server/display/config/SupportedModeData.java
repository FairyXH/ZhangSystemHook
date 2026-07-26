package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class SupportedModeData {
    public final float refreshRate;
    public final float vsyncRate;

    public SupportedModeData(float refreshRate, float vsyncRate) {
        this.refreshRate = refreshRate;
        this.vsyncRate = vsyncRate;
    }

    public java.lang.String toString() {
        return "SupportedModeData{refreshRate= " + this.refreshRate + ", vsyncRate= " + this.vsyncRate + '}';
    }

    static java.util.List<com.android.server.display.config.SupportedModeData> load(com.android.server.display.config.NonNegativeFloatToFloatMap configMap) {
        java.util.ArrayList<com.android.server.display.config.SupportedModeData> supportedModes = new java.util.ArrayList<>();
        if (configMap != null) {
            for (com.android.server.display.config.NonNegativeFloatToFloatPoint supportedMode : configMap.getPoint()) {
                supportedModes.add(new com.android.server.display.config.SupportedModeData(supportedMode.getFirst().floatValue(), supportedMode.getSecond().floatValue()));
            }
        }
        return supportedModes;
    }
}
