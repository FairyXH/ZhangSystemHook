package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayBrightnessMappingConfig {
    private static final java.lang.String DEFAULT_BRIGHTNESS_MAPPING_KEY = com.android.server.display.config.AutoBrightnessModeName._default.getRawName() + "_" + com.android.server.display.config.AutoBrightnessSettingName.normal.getRawName();
    private float[] mBrightnessLevelsNits;
    private final java.util.Map<java.lang.String, float[]> mBrightnessLevelsMap = new java.util.HashMap();
    private final java.util.Map<java.lang.String, float[]> mBrightnessLevelsLuxMap = new java.util.HashMap();

    public DisplayBrightnessMappingConfig(android.content.Context context, com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.config.AutoBrightness autoBrightnessConfig, android.util.Spline backlightToBrightnessSpline) {
        java.lang.String rawName;
        java.lang.String rawName2;
        if (flags.areAutoBrightnessModesEnabled() && autoBrightnessConfig != null && autoBrightnessConfig.getLuxToBrightnessMapping() != null && autoBrightnessConfig.getLuxToBrightnessMapping().size() > 0) {
            for (com.android.server.display.config.LuxToBrightnessMapping mapping : autoBrightnessConfig.getLuxToBrightnessMapping()) {
                int size = mapping.getMap().getPoint().size();
                float[] brightnessLevels = new float[size];
                float[] brightnessLevelsLux = new float[size];
                for (int i = 0; i < size; i++) {
                    float backlight = mapping.getMap().getPoint().get(i).getSecond().floatValue();
                    brightnessLevels[i] = backlightToBrightnessSpline.interpolate(backlight);
                    brightnessLevelsLux[i] = mapping.getMap().getPoint().get(i).getFirst().floatValue();
                }
                if (size == 0) {
                    throw new java.lang.IllegalArgumentException("A display brightness mapping should not be empty");
                }
                if (brightnessLevelsLux[0] != 0.0f) {
                    throw new java.lang.IllegalArgumentException("The first lux value in the display brightness mapping must be 0");
                }
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                if (mapping.getMode() == null) {
                    rawName = com.android.server.display.config.AutoBrightnessModeName._default.getRawName();
                } else {
                    rawName = mapping.getMode().getRawName();
                }
                java.lang.StringBuilder sbAppend = sb.append(rawName).append("_");
                if (mapping.getSetting() == null) {
                    rawName2 = com.android.server.display.config.AutoBrightnessSettingName.normal.getRawName();
                } else {
                    rawName2 = mapping.getSetting().getRawName();
                }
                java.lang.String key = sbAppend.append(rawName2).toString();
                if (this.mBrightnessLevelsMap.containsKey(key) || this.mBrightnessLevelsLuxMap.containsKey(key)) {
                    throw new java.lang.IllegalArgumentException("A display brightness mapping with key " + key + " already exists");
                }
                this.mBrightnessLevelsMap.put(key, brightnessLevels);
                this.mBrightnessLevelsLuxMap.put(key, brightnessLevelsLux);
            }
        }
        if (!this.mBrightnessLevelsMap.containsKey(DEFAULT_BRIGHTNESS_MAPPING_KEY) || !this.mBrightnessLevelsLuxMap.containsKey(DEFAULT_BRIGHTNESS_MAPPING_KEY)) {
            this.mBrightnessLevelsNits = com.android.server.display.DisplayDeviceConfig.getFloatArray(context.getResources().obtainTypedArray(android.R.array.config_autoBrightnessButtonBacklightValues), -1.0f);
            this.mBrightnessLevelsLuxMap.put(DEFAULT_BRIGHTNESS_MAPPING_KEY, com.android.server.display.DisplayDeviceConfig.getLuxLevels(context.getResources().getIntArray(android.R.array.config_autoBrightnessLcdBacklightValues_doze)));
            this.mBrightnessLevelsMap.put(DEFAULT_BRIGHTNESS_MAPPING_KEY, brightnessArrayIntToFloat(context.getResources().getIntArray(android.R.array.config_autoBrightnessDisplayValuesNitsIdle), backlightToBrightnessSpline));
        }
    }

    public float[] getLuxArray(int mode, int preset) {
        float[] luxArray = this.mBrightnessLevelsLuxMap.get(autoBrightnessModeToString(mode) + "_" + autoBrightnessPresetToString(preset));
        if (luxArray != null) {
            return luxArray;
        }
        return this.mBrightnessLevelsLuxMap.get(autoBrightnessModeToString(mode) + "_" + com.android.server.display.config.AutoBrightnessSettingName.normal.getRawName());
    }

    public float[] getNitsArray() {
        return this.mBrightnessLevelsNits;
    }

    public float[] getBrightnessArray(int mode, int preset) {
        float[] brightnessArray = this.mBrightnessLevelsMap.get(autoBrightnessModeToString(mode) + "_" + autoBrightnessPresetToString(preset));
        if (brightnessArray != null) {
            return brightnessArray;
        }
        return this.mBrightnessLevelsMap.get(autoBrightnessModeToString(mode) + "_" + com.android.server.display.config.AutoBrightnessSettingName.normal.getRawName());
    }

    public java.lang.String toString() {
        java.lang.StringBuilder brightnessLevelsLuxMapString = new java.lang.StringBuilder("{");
        for (java.util.Map.Entry<java.lang.String, float[]> entry : this.mBrightnessLevelsLuxMap.entrySet()) {
            brightnessLevelsLuxMapString.append(entry.getKey()).append("=").append(java.util.Arrays.toString(entry.getValue())).append(", ");
        }
        if (brightnessLevelsLuxMapString.length() > 2) {
            brightnessLevelsLuxMapString.delete(brightnessLevelsLuxMapString.length() - 2, brightnessLevelsLuxMapString.length());
        }
        brightnessLevelsLuxMapString.append("}");
        java.lang.StringBuilder brightnessLevelsMapString = new java.lang.StringBuilder("{");
        for (java.util.Map.Entry<java.lang.String, float[]> entry2 : this.mBrightnessLevelsMap.entrySet()) {
            brightnessLevelsMapString.append(entry2.getKey()).append("=").append(java.util.Arrays.toString(entry2.getValue())).append(", ");
        }
        if (brightnessLevelsMapString.length() > 2) {
            brightnessLevelsMapString.delete(brightnessLevelsMapString.length() - 2, brightnessLevelsMapString.length());
        }
        brightnessLevelsMapString.append("}");
        return "mBrightnessLevelsNits= " + java.util.Arrays.toString(this.mBrightnessLevelsNits) + ", mBrightnessLevelsLuxMap= " + ((java.lang.Object) brightnessLevelsLuxMapString) + ", mBrightnessLevelsMap= " + ((java.lang.Object) brightnessLevelsMapString);
    }

    public static java.lang.String autoBrightnessModeToString(int mode) {
        switch (mode) {
            case 0:
                return com.android.server.display.config.AutoBrightnessModeName._default.getRawName();
            case 1:
                return com.android.server.display.config.AutoBrightnessModeName.idle.getRawName();
            case 2:
                return com.android.server.display.config.AutoBrightnessModeName.doze.getRawName();
            default:
                throw new java.lang.IllegalArgumentException("Unknown auto-brightness mode: " + mode);
        }
    }

    public static java.lang.String autoBrightnessPresetToString(int preset) {
        switch (preset) {
            case 1:
                return com.android.server.display.config.AutoBrightnessSettingName.bright.getRawName();
            case 2:
                return com.android.server.display.config.AutoBrightnessSettingName.normal.getRawName();
            case 3:
                return com.android.server.display.config.AutoBrightnessSettingName.dim.getRawName();
            default:
                throw new java.lang.IllegalArgumentException("Unknown auto-brightness preset value: " + preset);
        }
    }

    private float[] brightnessArrayIntToFloat(int[] brightnessInt, android.util.Spline backlightToBrightnessSpline) {
        float[] brightnessFloat = new float[brightnessInt.length];
        for (int i = 0; i < brightnessInt.length; i++) {
            brightnessFloat[i] = backlightToBrightnessSpline.interpolate(com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(brightnessInt[i]));
        }
        return brightnessFloat;
    }
}
