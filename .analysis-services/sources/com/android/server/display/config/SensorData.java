package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class SensorData {
    public static final java.lang.String TEMPERATURE_TYPE_DISPLAY = "DISPLAY";
    public static final java.lang.String TEMPERATURE_TYPE_SKIN = "SKIN";
    public final float maxRefreshRate;
    public final float minRefreshRate;
    public final java.lang.String name;
    public final java.util.List<com.android.server.display.config.SupportedModeData> supportedModes;
    public final java.lang.String type;

    public SensorData() {
        this(null, null);
    }

    public SensorData(java.lang.String type, java.lang.String name) {
        this(type, name, 0.0f, Float.POSITIVE_INFINITY);
    }

    public SensorData(java.lang.String type, java.lang.String name, float minRefreshRate, float maxRefreshRate) {
        this(type, name, minRefreshRate, maxRefreshRate, java.util.List.of());
    }

    public SensorData(java.lang.String type, java.lang.String name, float minRefreshRate, float maxRefreshRate, java.util.List<com.android.server.display.config.SupportedModeData> supportedModes) {
        this.type = type;
        this.name = name;
        this.minRefreshRate = minRefreshRate;
        this.maxRefreshRate = maxRefreshRate;
        this.supportedModes = java.util.Collections.unmodifiableList(supportedModes);
    }

    public boolean matches(java.lang.String sensorName, java.lang.String sensorType) {
        boolean isNameSpecified = !android.text.TextUtils.isEmpty(sensorName);
        boolean isTypeSpecified = !android.text.TextUtils.isEmpty(sensorType);
        return (isNameSpecified || isTypeSpecified) && (!isNameSpecified || sensorName.equals(this.name)) && (!isTypeSpecified || sensorType.equals(this.type));
    }

    public java.lang.String toString() {
        return "SensorData{type= " + this.type + ", name= " + this.name + ", refreshRateRange: [" + this.minRefreshRate + ", " + this.maxRefreshRate + "], supportedModes=" + this.supportedModes + '}';
    }

    public static com.android.server.display.config.SensorData loadAmbientLightSensorConfig(com.android.server.display.config.DisplayConfiguration config, android.content.res.Resources resources) {
        com.android.server.display.config.SensorDetails sensorDetails = config.getLightSensor();
        if (sensorDetails != null) {
            return loadSensorData(sensorDetails);
        }
        return loadAmbientLightSensorConfig(resources);
    }

    public static com.android.server.display.config.SensorData loadAmbientLightSensorConfig(android.content.res.Resources resources) {
        return new com.android.server.display.config.SensorData(resources.getString(android.R.string.config_emergency_dialer_package), "");
    }

    public static com.android.server.display.config.SensorData loadScreenOffBrightnessSensorConfig(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.SensorDetails sensorDetails = config.getScreenOffBrightnessSensor();
        if (sensorDetails != null) {
            return loadSensorData(sensorDetails);
        }
        return new com.android.server.display.config.SensorData();
    }

    public static com.android.server.display.config.SensorData loadProxSensorConfig(com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.SensorData DEFAULT_SENSOR = new com.android.server.display.config.SensorData();
        java.util.List<com.android.server.display.config.SensorDetails> sensorDetailsList = config.getProxSensor();
        if (sensorDetailsList.isEmpty()) {
            return DEFAULT_SENSOR;
        }
        com.android.server.display.config.SensorData selectedSensor = DEFAULT_SENSOR;
        java.util.Iterator<com.android.server.display.config.SensorDetails> it = sensorDetailsList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.android.server.display.config.SensorDetails sensorDetails = it.next();
            java.lang.String flagStr = sensorDetails.getFeatureFlag();
            if (flags.isUseFusionProxSensorEnabled() && flags.getUseFusionProxSensorFlagName().equals(flagStr)) {
                selectedSensor = loadSensorData(sensorDetails);
                break;
            }
        }
        if (DEFAULT_SENSOR == selectedSensor) {
            java.util.Iterator<com.android.server.display.config.SensorDetails> it2 = sensorDetailsList.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                com.android.server.display.config.SensorDetails sensorDetails2 = it2.next();
                if (sensorDetails2.getFeatureFlag() == null) {
                    selectedSensor = loadSensorData(sensorDetails2);
                    break;
                }
            }
        }
        if (DEFAULT_SENSOR != selectedSensor && "".equals(selectedSensor.name) && "".equals(selectedSensor.type)) {
            return null;
        }
        return selectedSensor;
    }

    public static com.android.server.display.config.SensorData loadTempSensorUnspecifiedConfig() {
        return new com.android.server.display.config.SensorData(TEMPERATURE_TYPE_SKIN, null);
    }

    public static com.android.server.display.config.SensorData loadTempSensorConfig(com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.SensorDetails sensorDetails = config.getTempSensor();
        if (!flags.isSensorBasedBrightnessThrottlingEnabled() || sensorDetails == null) {
            return new com.android.server.display.config.SensorData(TEMPERATURE_TYPE_SKIN, null);
        }
        java.lang.String name = sensorDetails.getName();
        java.lang.String type = sensorDetails.getType();
        if (android.text.TextUtils.isEmpty(type) || android.text.TextUtils.isEmpty(name)) {
            type = TEMPERATURE_TYPE_SKIN;
            name = null;
        }
        return new com.android.server.display.config.SensorData(type, name);
    }

    public static com.android.server.display.config.SensorData loadSensorUnspecifiedConfig() {
        return new com.android.server.display.config.SensorData();
    }

    private static com.android.server.display.config.SensorData loadSensorData(com.android.server.display.config.SensorDetails sensorDetails) {
        float minRefreshRate = 0.0f;
        float maxRefreshRate = Float.POSITIVE_INFINITY;
        com.android.server.display.config.RefreshRateRange rr = sensorDetails.getRefreshRate();
        if (rr != null) {
            minRefreshRate = rr.getMinimum().floatValue();
            maxRefreshRate = rr.getMaximum().floatValue();
        }
        java.util.List<com.android.server.display.config.SupportedModeData> supportedModes = com.android.server.display.config.SupportedModeData.load(sensorDetails.getSupportedModes());
        return new com.android.server.display.config.SensorData(sensorDetails.getType(), sensorDetails.getName(), minRefreshRate, maxRefreshRate, supportedModes);
    }
}
