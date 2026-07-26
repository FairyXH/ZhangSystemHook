package com.android.server.display.whitebalance;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayWhiteBalanceFactory {
    private static final java.lang.String BRIGHTNESS_FILTER_TAG = "AmbientBrightnessFilter";
    private static final java.lang.String COLOR_TEMPERATURE_FILTER_TAG = "AmbientColorTemperatureFilter";

    public static com.android.server.display.whitebalance.DisplayWhiteBalanceController create(android.os.Handler handler, android.hardware.SensorManager sensorManager, android.content.res.Resources resources) {
        com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor brightnessSensor = createBrightnessSensor(handler, sensorManager, resources);
        com.android.server.display.utils.AmbientFilter brightnessFilter = com.android.server.display.utils.AmbientFilterFactory.createBrightnessFilter(BRIGHTNESS_FILTER_TAG, resources);
        com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor colorTemperatureSensor = createColorTemperatureSensor(handler, sensorManager, resources);
        com.android.server.display.utils.AmbientFilter colorTemperatureFilter = com.android.server.display.utils.AmbientFilterFactory.createColorTemperatureFilter(COLOR_TEMPERATURE_FILTER_TAG, resources);
        com.android.server.display.whitebalance.DisplayWhiteBalanceThrottler throttler = createThrottler(resources);
        float[] displayWhiteBalanceLowLightAmbientBrightnesses = getFloatArray(resources, android.R.array.config_displayWhiteBalanceLowLightAmbientBiases);
        float[] displayWhiteBalanceLowLightAmbientBrightnessesStrong = getFloatArray(resources, android.R.array.config_displayWhiteBalanceLowLightAmbientBiasesStrong);
        float[] displayWhiteBalanceLowLightAmbientBiases = getFloatArray(resources, android.R.array.config_displayWhiteBalanceHighLightAmbientBrightnessesStrong);
        float[] displayWhiteBalanceLowLightAmbientBiasesStrong = getFloatArray(resources, android.R.array.config_displayWhiteBalanceIncreaseThresholds);
        float lowLightAmbientColorTemperature = getFloat(resources, android.R.dimen.chooser_row_text_option_translate);
        float lowLightAmbientColorTemperatureStrong = getFloat(resources, android.R.dimen.chooser_view_spacing);
        float[] displayWhiteBalanceHighLightAmbientBrightnesses = getFloatArray(resources, android.R.array.config_displayWhiteBalanceHighLightAmbientBiases);
        float[] displayWhiteBalanceHighLightAmbientBrightnessesStrong = getFloatArray(resources, android.R.array.config_displayWhiteBalanceHighLightAmbientBiasesStrong);
        float[] displayWhiteBalanceHighLightAmbientBiases = getFloatArray(resources, android.R.array.config_displayWhiteBalanceDisplayRangeMinimums);
        float[] displayWhiteBalanceHighLightAmbientBiasesStrong = getFloatArray(resources, android.R.array.config_displayWhiteBalanceDisplaySteps);
        float highLightAmbientColorTemperature = getFloat(resources, android.R.dimen.chooser_preview_image_max_dimen);
        float highLightAmbientColorTemperatureStrong = getFloat(resources, android.R.dimen.chooser_preview_width);
        float[] ambientColorTemperatures = getFloatArray(resources, android.R.array.config_displayShapeArray);
        float[] displayColorTemperatures = getFloatArray(resources, android.R.array.config_displayWhiteBalanceBaseThresholds);
        float[] strongAmbientColorTemperatures = getFloatArray(resources, android.R.array.config_displayWhiteBalanceLowLightAmbientBrightnesses);
        float[] strongDisplayColorTemperatures = getFloatArray(resources, android.R.array.config_displayWhiteBalanceLowLightAmbientBrightnessesStrong);
        boolean lightModeAllowed = resources.getBoolean(android.R.bool.config_displayWhiteBalanceAvailable);
        com.android.server.display.whitebalance.DisplayWhiteBalanceController controller = new com.android.server.display.whitebalance.DisplayWhiteBalanceController(brightnessSensor, brightnessFilter, colorTemperatureSensor, colorTemperatureFilter, throttler, displayWhiteBalanceLowLightAmbientBrightnesses, displayWhiteBalanceLowLightAmbientBrightnessesStrong, displayWhiteBalanceLowLightAmbientBiases, displayWhiteBalanceLowLightAmbientBiasesStrong, lowLightAmbientColorTemperature, lowLightAmbientColorTemperatureStrong, displayWhiteBalanceHighLightAmbientBrightnesses, displayWhiteBalanceHighLightAmbientBrightnessesStrong, displayWhiteBalanceHighLightAmbientBiases, displayWhiteBalanceHighLightAmbientBiasesStrong, highLightAmbientColorTemperature, highLightAmbientColorTemperatureStrong, ambientColorTemperatures, displayColorTemperatures, strongAmbientColorTemperatures, strongDisplayColorTemperatures, lightModeAllowed);
        brightnessSensor.setCallbacks(controller);
        colorTemperatureSensor.setCallbacks(controller);
        return controller;
    }

    private DisplayWhiteBalanceFactory() {
    }

    public static com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor createBrightnessSensor(android.os.Handler handler, android.hardware.SensorManager sensorManager, android.content.res.Resources resources) {
        int rate = resources.getInteger(android.R.integer.config_default_cellular_usage_setting);
        return new com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor(handler, sensorManager, rate);
    }

    public static com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor createColorTemperatureSensor(android.os.Handler handler, android.hardware.SensorManager sensorManager, android.content.res.Resources resources) {
        java.lang.String name = resources.getString(android.R.string.config_ethernet_iface_regex);
        int rate = resources.getInteger(android.R.integer.config_deskDockRotation);
        return new com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor(handler, sensorManager, name, rate);
    }

    private static com.android.server.display.whitebalance.DisplayWhiteBalanceThrottler createThrottler(android.content.res.Resources resources) {
        int increaseDebounce = resources.getInteger(android.R.integer.config_deviceStateConcurrentRearDisplay);
        int decreaseDebounce = resources.getInteger(android.R.integer.config_displayWhiteBalanceBrightnessFilterHorizon);
        float[] baseThresholds = getFloatArray(resources, android.R.array.config_displayUniqueIdArray);
        float[] increaseThresholds = getFloatArray(resources, android.R.array.config_displayWhiteBalanceHighLightAmbientBrightnesses);
        float[] decreaseThresholds = getFloatArray(resources, android.R.array.config_displayWhiteBalanceAmbientColorTemperatures);
        return new com.android.server.display.whitebalance.DisplayWhiteBalanceThrottler(increaseDebounce, decreaseDebounce, baseThresholds, increaseThresholds, decreaseThresholds);
    }

    private static float getFloat(android.content.res.Resources resources, int id) {
        android.util.TypedValue value = new android.util.TypedValue();
        resources.getValue(id, value, true);
        if (value.type != 4) {
            return Float.NaN;
        }
        return value.getFloat();
    }

    private static float[] getFloatArray(android.content.res.Resources resources, int id) {
        android.content.res.TypedArray array = resources.obtainTypedArray(id);
        try {
            if (array.length() == 0) {
                return null;
            }
            float[] values = new float[array.length()];
            for (int i = 0; i < values.length; i++) {
                values[i] = array.getFloat(i, Float.NaN);
                if (java.lang.Float.isNaN(values[i])) {
                    return null;
                }
            }
            return values;
        } finally {
            array.recycle();
        }
    }
}
