package com.android.server.display.utils;

/* JADX INFO: loaded from: classes2.dex */
public class SensorUtils {
    public static final int NO_FALLBACK = 0;

    public static android.hardware.Sensor findSensor(android.hardware.SensorManager sensorManager, com.android.server.display.config.SensorData sensorData, int fallbackType) {
        if (sensorData == null) {
            return null;
        }
        return findSensor(sensorManager, sensorData.type, sensorData.name, fallbackType);
    }

    public static android.hardware.Sensor findSensor(android.hardware.SensorManager sensorManager, java.lang.String sensorType, java.lang.String sensorName, int fallbackType) {
        if (sensorManager == null) {
            return null;
        }
        boolean isNameSpecified = !android.text.TextUtils.isEmpty(sensorName);
        boolean isTypeSpecified = !android.text.TextUtils.isEmpty(sensorType);
        if (isNameSpecified || isTypeSpecified) {
            java.util.List<android.hardware.Sensor> sensors = sensorManager.getSensorList(-1);
            for (android.hardware.Sensor sensor : sensors) {
                if (!isNameSpecified || sensorName.equals(sensor.getName())) {
                    if (!isTypeSpecified || sensorType.equals(sensor.getStringType())) {
                        return sensor;
                    }
                }
            }
        }
        if (fallbackType == 0) {
            return null;
        }
        return sensorManager.getDefaultSensor(fallbackType);
    }

    public static int getSensorTemperatureType(com.android.server.display.config.SensorData tempSensor) {
        if (tempSensor.type.equalsIgnoreCase(com.android.server.display.config.SensorData.TEMPERATURE_TYPE_DISPLAY)) {
            return 11;
        }
        if (tempSensor.type.equalsIgnoreCase(com.android.server.display.config.SensorData.TEMPERATURE_TYPE_SKIN)) {
            return 3;
        }
        throw new java.lang.IllegalArgumentException("tempSensor doesn't support type: " + tempSensor.type);
    }
}
