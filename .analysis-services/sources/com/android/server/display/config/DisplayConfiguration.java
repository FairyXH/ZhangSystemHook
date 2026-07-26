package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayConfiguration {
    private com.android.server.display.config.Thresholds ambientBrightnessChangeThresholds;
    private com.android.server.display.config.Thresholds ambientBrightnessChangeThresholdsIdle;
    private java.math.BigInteger ambientLightHorizonLong;
    private java.math.BigInteger ambientLightHorizonShort;
    private com.android.server.display.config.AutoBrightness autoBrightness;
    private com.android.server.display.config.DensityMapping densityMapping;
    private com.android.server.display.config.Thresholds displayBrightnessChangeThresholds;
    private com.android.server.display.config.Thresholds displayBrightnessChangeThresholdsIdle;
    private com.android.server.display.config.EvenDimmerMode evenDimmer;
    private com.android.server.display.config.HdrBrightnessConfig hdrBrightnessConfig;
    private com.android.server.display.config.HighBrightnessMode highBrightnessMode;
    private com.android.server.display.config.IdleScreenRefreshRateTimeout idleScreenRefreshRateTimeout;
    private com.android.server.display.config.SensorDetails lightSensor;
    private com.android.server.display.config.LuxThrottling luxThrottling;
    private java.lang.String name;
    private com.android.server.display.config.PowerThrottlingConfig powerThrottlingConfig;
    private java.util.List<com.android.server.display.config.SensorDetails> proxSensor;
    private com.android.server.display.config.DisplayQuirks quirks;
    private com.android.server.display.config.RefreshRateConfigs refreshRate;
    private java.math.BigDecimal screenBrightnessCapForWearBedtimeMode;
    private java.math.BigDecimal screenBrightnessDefault;
    private com.android.server.display.config.NitsMap screenBrightnessMap;
    private java.math.BigInteger screenBrightnessRampDecreaseMaxIdleMillis;
    private java.math.BigInteger screenBrightnessRampDecreaseMaxMillis;
    private java.math.BigDecimal screenBrightnessRampFastDecrease;
    private java.math.BigDecimal screenBrightnessRampFastIncrease;
    private java.math.BigInteger screenBrightnessRampIncreaseMaxIdleMillis;
    private java.math.BigInteger screenBrightnessRampIncreaseMaxMillis;
    private java.math.BigDecimal screenBrightnessRampSlowDecrease;
    private java.math.BigDecimal screenBrightnessRampSlowDecreaseIdle;
    private java.math.BigDecimal screenBrightnessRampSlowIncrease;
    private java.math.BigDecimal screenBrightnessRampSlowIncreaseIdle;
    private com.android.server.display.config.SensorDetails screenOffBrightnessSensor;
    private com.android.server.display.config.IntegerArray screenOffBrightnessSensorValueToLux;
    private java.lang.Boolean supportsVrr;
    private com.android.server.display.config.SensorDetails tempSensor;
    private com.android.server.display.config.ThermalThrottling thermalThrottling;
    private com.android.server.display.config.UsiVersion usiVersion;

    public final java.lang.String getName() {
        return this.name;
    }

    boolean hasName() {
        if (this.name == null) {
            return false;
        }
        return true;
    }

    public final void setName(java.lang.String name) {
        this.name = name;
    }

    public final com.android.server.display.config.DensityMapping getDensityMapping() {
        return this.densityMapping;
    }

    boolean hasDensityMapping() {
        if (this.densityMapping == null) {
            return false;
        }
        return true;
    }

    public final void setDensityMapping(com.android.server.display.config.DensityMapping densityMapping) {
        this.densityMapping = densityMapping;
    }

    public final com.android.server.display.config.NitsMap getScreenBrightnessMap() {
        return this.screenBrightnessMap;
    }

    boolean hasScreenBrightnessMap() {
        if (this.screenBrightnessMap == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessMap(com.android.server.display.config.NitsMap screenBrightnessMap) {
        this.screenBrightnessMap = screenBrightnessMap;
    }

    public final java.math.BigDecimal getScreenBrightnessDefault() {
        return this.screenBrightnessDefault;
    }

    boolean hasScreenBrightnessDefault() {
        if (this.screenBrightnessDefault == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessDefault(java.math.BigDecimal screenBrightnessDefault) {
        this.screenBrightnessDefault = screenBrightnessDefault;
    }

    public final com.android.server.display.config.ThermalThrottling getThermalThrottling() {
        return this.thermalThrottling;
    }

    boolean hasThermalThrottling() {
        if (this.thermalThrottling == null) {
            return false;
        }
        return true;
    }

    public final void setThermalThrottling(com.android.server.display.config.ThermalThrottling thermalThrottling) {
        this.thermalThrottling = thermalThrottling;
    }

    public com.android.server.display.config.PowerThrottlingConfig getPowerThrottlingConfig() {
        return this.powerThrottlingConfig;
    }

    boolean hasPowerThrottlingConfig() {
        if (this.powerThrottlingConfig == null) {
            return false;
        }
        return true;
    }

    public void setPowerThrottlingConfig(com.android.server.display.config.PowerThrottlingConfig powerThrottlingConfig) {
        this.powerThrottlingConfig = powerThrottlingConfig;
    }

    public com.android.server.display.config.LuxThrottling getLuxThrottling() {
        return this.luxThrottling;
    }

    boolean hasLuxThrottling() {
        if (this.luxThrottling == null) {
            return false;
        }
        return true;
    }

    public void setLuxThrottling(com.android.server.display.config.LuxThrottling luxThrottling) {
        this.luxThrottling = luxThrottling;
    }

    public com.android.server.display.config.HighBrightnessMode getHighBrightnessMode() {
        return this.highBrightnessMode;
    }

    boolean hasHighBrightnessMode() {
        if (this.highBrightnessMode == null) {
            return false;
        }
        return true;
    }

    public void setHighBrightnessMode(com.android.server.display.config.HighBrightnessMode highBrightnessMode) {
        this.highBrightnessMode = highBrightnessMode;
    }

    public final com.android.server.display.config.HdrBrightnessConfig getHdrBrightnessConfig() {
        return this.hdrBrightnessConfig;
    }

    boolean hasHdrBrightnessConfig() {
        if (this.hdrBrightnessConfig == null) {
            return false;
        }
        return true;
    }

    public final void setHdrBrightnessConfig(com.android.server.display.config.HdrBrightnessConfig hdrBrightnessConfig) {
        this.hdrBrightnessConfig = hdrBrightnessConfig;
    }

    public com.android.server.display.config.DisplayQuirks getQuirks() {
        return this.quirks;
    }

    boolean hasQuirks() {
        if (this.quirks == null) {
            return false;
        }
        return true;
    }

    public void setQuirks(com.android.server.display.config.DisplayQuirks quirks) {
        this.quirks = quirks;
    }

    public com.android.server.display.config.AutoBrightness getAutoBrightness() {
        return this.autoBrightness;
    }

    boolean hasAutoBrightness() {
        if (this.autoBrightness == null) {
            return false;
        }
        return true;
    }

    public void setAutoBrightness(com.android.server.display.config.AutoBrightness autoBrightness) {
        this.autoBrightness = autoBrightness;
    }

    public com.android.server.display.config.RefreshRateConfigs getRefreshRate() {
        return this.refreshRate;
    }

    boolean hasRefreshRate() {
        if (this.refreshRate == null) {
            return false;
        }
        return true;
    }

    public void setRefreshRate(com.android.server.display.config.RefreshRateConfigs refreshRate) {
        this.refreshRate = refreshRate;
    }

    public final java.math.BigDecimal getScreenBrightnessRampFastDecrease() {
        return this.screenBrightnessRampFastDecrease;
    }

    boolean hasScreenBrightnessRampFastDecrease() {
        if (this.screenBrightnessRampFastDecrease == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampFastDecrease(java.math.BigDecimal screenBrightnessRampFastDecrease) {
        this.screenBrightnessRampFastDecrease = screenBrightnessRampFastDecrease;
    }

    public final java.math.BigDecimal getScreenBrightnessRampFastIncrease() {
        return this.screenBrightnessRampFastIncrease;
    }

    boolean hasScreenBrightnessRampFastIncrease() {
        if (this.screenBrightnessRampFastIncrease == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampFastIncrease(java.math.BigDecimal screenBrightnessRampFastIncrease) {
        this.screenBrightnessRampFastIncrease = screenBrightnessRampFastIncrease;
    }

    public final java.math.BigDecimal getScreenBrightnessRampSlowDecrease() {
        return this.screenBrightnessRampSlowDecrease;
    }

    boolean hasScreenBrightnessRampSlowDecrease() {
        if (this.screenBrightnessRampSlowDecrease == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampSlowDecrease(java.math.BigDecimal screenBrightnessRampSlowDecrease) {
        this.screenBrightnessRampSlowDecrease = screenBrightnessRampSlowDecrease;
    }

    public final java.math.BigDecimal getScreenBrightnessRampSlowIncrease() {
        return this.screenBrightnessRampSlowIncrease;
    }

    boolean hasScreenBrightnessRampSlowIncrease() {
        if (this.screenBrightnessRampSlowIncrease == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampSlowIncrease(java.math.BigDecimal screenBrightnessRampSlowIncrease) {
        this.screenBrightnessRampSlowIncrease = screenBrightnessRampSlowIncrease;
    }

    public final java.math.BigDecimal getScreenBrightnessRampSlowDecreaseIdle() {
        return this.screenBrightnessRampSlowDecreaseIdle;
    }

    boolean hasScreenBrightnessRampSlowDecreaseIdle() {
        if (this.screenBrightnessRampSlowDecreaseIdle == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampSlowDecreaseIdle(java.math.BigDecimal screenBrightnessRampSlowDecreaseIdle) {
        this.screenBrightnessRampSlowDecreaseIdle = screenBrightnessRampSlowDecreaseIdle;
    }

    public final java.math.BigDecimal getScreenBrightnessRampSlowIncreaseIdle() {
        return this.screenBrightnessRampSlowIncreaseIdle;
    }

    boolean hasScreenBrightnessRampSlowIncreaseIdle() {
        if (this.screenBrightnessRampSlowIncreaseIdle == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampSlowIncreaseIdle(java.math.BigDecimal screenBrightnessRampSlowIncreaseIdle) {
        this.screenBrightnessRampSlowIncreaseIdle = screenBrightnessRampSlowIncreaseIdle;
    }

    public final java.math.BigInteger getScreenBrightnessRampIncreaseMaxMillis() {
        return this.screenBrightnessRampIncreaseMaxMillis;
    }

    boolean hasScreenBrightnessRampIncreaseMaxMillis() {
        if (this.screenBrightnessRampIncreaseMaxMillis == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampIncreaseMaxMillis(java.math.BigInteger screenBrightnessRampIncreaseMaxMillis) {
        this.screenBrightnessRampIncreaseMaxMillis = screenBrightnessRampIncreaseMaxMillis;
    }

    public final java.math.BigInteger getScreenBrightnessRampDecreaseMaxMillis() {
        return this.screenBrightnessRampDecreaseMaxMillis;
    }

    boolean hasScreenBrightnessRampDecreaseMaxMillis() {
        if (this.screenBrightnessRampDecreaseMaxMillis == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampDecreaseMaxMillis(java.math.BigInteger screenBrightnessRampDecreaseMaxMillis) {
        this.screenBrightnessRampDecreaseMaxMillis = screenBrightnessRampDecreaseMaxMillis;
    }

    public final java.math.BigInteger getScreenBrightnessRampIncreaseMaxIdleMillis() {
        return this.screenBrightnessRampIncreaseMaxIdleMillis;
    }

    boolean hasScreenBrightnessRampIncreaseMaxIdleMillis() {
        if (this.screenBrightnessRampIncreaseMaxIdleMillis == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampIncreaseMaxIdleMillis(java.math.BigInteger screenBrightnessRampIncreaseMaxIdleMillis) {
        this.screenBrightnessRampIncreaseMaxIdleMillis = screenBrightnessRampIncreaseMaxIdleMillis;
    }

    public final java.math.BigInteger getScreenBrightnessRampDecreaseMaxIdleMillis() {
        return this.screenBrightnessRampDecreaseMaxIdleMillis;
    }

    boolean hasScreenBrightnessRampDecreaseMaxIdleMillis() {
        if (this.screenBrightnessRampDecreaseMaxIdleMillis == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampDecreaseMaxIdleMillis(java.math.BigInteger screenBrightnessRampDecreaseMaxIdleMillis) {
        this.screenBrightnessRampDecreaseMaxIdleMillis = screenBrightnessRampDecreaseMaxIdleMillis;
    }

    public final com.android.server.display.config.SensorDetails getLightSensor() {
        return this.lightSensor;
    }

    boolean hasLightSensor() {
        if (this.lightSensor == null) {
            return false;
        }
        return true;
    }

    public final void setLightSensor(com.android.server.display.config.SensorDetails lightSensor) {
        this.lightSensor = lightSensor;
    }

    public final com.android.server.display.config.SensorDetails getScreenOffBrightnessSensor() {
        return this.screenOffBrightnessSensor;
    }

    boolean hasScreenOffBrightnessSensor() {
        if (this.screenOffBrightnessSensor == null) {
            return false;
        }
        return true;
    }

    public final void setScreenOffBrightnessSensor(com.android.server.display.config.SensorDetails screenOffBrightnessSensor) {
        this.screenOffBrightnessSensor = screenOffBrightnessSensor;
    }

    public final java.util.List<com.android.server.display.config.SensorDetails> getProxSensor() {
        if (this.proxSensor == null) {
            this.proxSensor = new java.util.ArrayList();
        }
        return this.proxSensor;
    }

    public final com.android.server.display.config.SensorDetails getTempSensor() {
        return this.tempSensor;
    }

    boolean hasTempSensor() {
        if (this.tempSensor == null) {
            return false;
        }
        return true;
    }

    public final void setTempSensor(com.android.server.display.config.SensorDetails tempSensor) {
        this.tempSensor = tempSensor;
    }

    public final java.math.BigInteger getAmbientLightHorizonLong() {
        return this.ambientLightHorizonLong;
    }

    boolean hasAmbientLightHorizonLong() {
        if (this.ambientLightHorizonLong == null) {
            return false;
        }
        return true;
    }

    public final void setAmbientLightHorizonLong(java.math.BigInteger ambientLightHorizonLong) {
        this.ambientLightHorizonLong = ambientLightHorizonLong;
    }

    public final java.math.BigInteger getAmbientLightHorizonShort() {
        return this.ambientLightHorizonShort;
    }

    boolean hasAmbientLightHorizonShort() {
        if (this.ambientLightHorizonShort == null) {
            return false;
        }
        return true;
    }

    public final void setAmbientLightHorizonShort(java.math.BigInteger ambientLightHorizonShort) {
        this.ambientLightHorizonShort = ambientLightHorizonShort;
    }

    public final com.android.server.display.config.Thresholds getDisplayBrightnessChangeThresholds() {
        return this.displayBrightnessChangeThresholds;
    }

    boolean hasDisplayBrightnessChangeThresholds() {
        if (this.displayBrightnessChangeThresholds == null) {
            return false;
        }
        return true;
    }

    public final void setDisplayBrightnessChangeThresholds(com.android.server.display.config.Thresholds displayBrightnessChangeThresholds) {
        this.displayBrightnessChangeThresholds = displayBrightnessChangeThresholds;
    }

    public final com.android.server.display.config.Thresholds getAmbientBrightnessChangeThresholds() {
        return this.ambientBrightnessChangeThresholds;
    }

    boolean hasAmbientBrightnessChangeThresholds() {
        if (this.ambientBrightnessChangeThresholds == null) {
            return false;
        }
        return true;
    }

    public final void setAmbientBrightnessChangeThresholds(com.android.server.display.config.Thresholds ambientBrightnessChangeThresholds) {
        this.ambientBrightnessChangeThresholds = ambientBrightnessChangeThresholds;
    }

    public final com.android.server.display.config.Thresholds getDisplayBrightnessChangeThresholdsIdle() {
        return this.displayBrightnessChangeThresholdsIdle;
    }

    boolean hasDisplayBrightnessChangeThresholdsIdle() {
        if (this.displayBrightnessChangeThresholdsIdle == null) {
            return false;
        }
        return true;
    }

    public final void setDisplayBrightnessChangeThresholdsIdle(com.android.server.display.config.Thresholds displayBrightnessChangeThresholdsIdle) {
        this.displayBrightnessChangeThresholdsIdle = displayBrightnessChangeThresholdsIdle;
    }

    public final com.android.server.display.config.Thresholds getAmbientBrightnessChangeThresholdsIdle() {
        return this.ambientBrightnessChangeThresholdsIdle;
    }

    boolean hasAmbientBrightnessChangeThresholdsIdle() {
        if (this.ambientBrightnessChangeThresholdsIdle == null) {
            return false;
        }
        return true;
    }

    public final void setAmbientBrightnessChangeThresholdsIdle(com.android.server.display.config.Thresholds ambientBrightnessChangeThresholdsIdle) {
        this.ambientBrightnessChangeThresholdsIdle = ambientBrightnessChangeThresholdsIdle;
    }

    public final com.android.server.display.config.IntegerArray getScreenOffBrightnessSensorValueToLux() {
        return this.screenOffBrightnessSensorValueToLux;
    }

    boolean hasScreenOffBrightnessSensorValueToLux() {
        if (this.screenOffBrightnessSensorValueToLux == null) {
            return false;
        }
        return true;
    }

    public final void setScreenOffBrightnessSensorValueToLux(com.android.server.display.config.IntegerArray screenOffBrightnessSensorValueToLux) {
        this.screenOffBrightnessSensorValueToLux = screenOffBrightnessSensorValueToLux;
    }

    public final com.android.server.display.config.UsiVersion getUsiVersion() {
        return this.usiVersion;
    }

    boolean hasUsiVersion() {
        if (this.usiVersion == null) {
            return false;
        }
        return true;
    }

    public final void setUsiVersion(com.android.server.display.config.UsiVersion usiVersion) {
        this.usiVersion = usiVersion;
    }

    public final com.android.server.display.config.EvenDimmerMode getEvenDimmer() {
        return this.evenDimmer;
    }

    boolean hasEvenDimmer() {
        if (this.evenDimmer == null) {
            return false;
        }
        return true;
    }

    public final void setEvenDimmer(com.android.server.display.config.EvenDimmerMode evenDimmer) {
        this.evenDimmer = evenDimmer;
    }

    public final java.math.BigDecimal getScreenBrightnessCapForWearBedtimeMode() {
        return this.screenBrightnessCapForWearBedtimeMode;
    }

    boolean hasScreenBrightnessCapForWearBedtimeMode() {
        if (this.screenBrightnessCapForWearBedtimeMode == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessCapForWearBedtimeMode(java.math.BigDecimal screenBrightnessCapForWearBedtimeMode) {
        this.screenBrightnessCapForWearBedtimeMode = screenBrightnessCapForWearBedtimeMode;
    }

    public final com.android.server.display.config.IdleScreenRefreshRateTimeout getIdleScreenRefreshRateTimeout() {
        return this.idleScreenRefreshRateTimeout;
    }

    boolean hasIdleScreenRefreshRateTimeout() {
        if (this.idleScreenRefreshRateTimeout == null) {
            return false;
        }
        return true;
    }

    public final void setIdleScreenRefreshRateTimeout(com.android.server.display.config.IdleScreenRefreshRateTimeout idleScreenRefreshRateTimeout) {
        this.idleScreenRefreshRateTimeout = idleScreenRefreshRateTimeout;
    }

    public final boolean getSupportsVrr() {
        if (this.supportsVrr == null) {
            return false;
        }
        return this.supportsVrr.booleanValue();
    }

    boolean hasSupportsVrr() {
        if (this.supportsVrr == null) {
            return false;
        }
        return true;
    }

    public final void setSupportsVrr(boolean supportsVrr) {
        this.supportsVrr = java.lang.Boolean.valueOf(supportsVrr);
    }

    /* JADX WARN: Failed to analyze thrown exceptions
    java.util.ConcurrentModificationException
    	at java.base/java.util.ArrayList$Itr.checkForComodification(Unknown Source)
    	at java.base/java.util.ArrayList$Itr.next(Unknown Source)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.processInstructions(MethodThrowsVisitor.java:117)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.visit(MethodThrowsVisitor.java:68)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.checkInsn(MethodThrowsVisitor.java:178)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.processInstructions(MethodThrowsVisitor.java:131)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.visit(MethodThrowsVisitor.java:68)
     */
    static com.android.server.display.config.DisplayConfiguration read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.DisplayConfiguration _instance = new com.android.server.display.config.DisplayConfiguration();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("name")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    _instance.setName(_raw);
                } else if (_tagName.equals("densityMapping")) {
                    com.android.server.display.config.DensityMapping _value = com.android.server.display.config.DensityMapping.read(_parser);
                    _instance.setDensityMapping(_value);
                } else if (_tagName.equals("screenBrightnessMap")) {
                    com.android.server.display.config.NitsMap _value2 = com.android.server.display.config.NitsMap.read(_parser);
                    _instance.setScreenBrightnessMap(_value2);
                } else if (_tagName.equals("screenBrightnessDefault")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value3 = new java.math.BigDecimal(_raw2);
                    _instance.setScreenBrightnessDefault(_value3);
                } else if (_tagName.equals("thermalThrottling")) {
                    com.android.server.display.config.ThermalThrottling _value4 = com.android.server.display.config.ThermalThrottling.read(_parser);
                    _instance.setThermalThrottling(_value4);
                } else if (_tagName.equals("powerThrottlingConfig")) {
                    com.android.server.display.config.PowerThrottlingConfig _value5 = com.android.server.display.config.PowerThrottlingConfig.read(_parser);
                    _instance.setPowerThrottlingConfig(_value5);
                } else if (_tagName.equals("luxThrottling")) {
                    com.android.server.display.config.LuxThrottling _value6 = com.android.server.display.config.LuxThrottling.read(_parser);
                    _instance.setLuxThrottling(_value6);
                } else if (_tagName.equals("highBrightnessMode")) {
                    com.android.server.display.config.HighBrightnessMode _value7 = com.android.server.display.config.HighBrightnessMode.read(_parser);
                    _instance.setHighBrightnessMode(_value7);
                } else if (_tagName.equals("hdrBrightnessConfig")) {
                    com.android.server.display.config.HdrBrightnessConfig _value8 = com.android.server.display.config.HdrBrightnessConfig.read(_parser);
                    _instance.setHdrBrightnessConfig(_value8);
                } else if (_tagName.equals("quirks")) {
                    com.android.server.display.config.DisplayQuirks _value9 = com.android.server.display.config.DisplayQuirks.read(_parser);
                    _instance.setQuirks(_value9);
                } else if (_tagName.equals("autoBrightness")) {
                    com.android.server.display.config.AutoBrightness _value10 = com.android.server.display.config.AutoBrightness.read(_parser);
                    _instance.setAutoBrightness(_value10);
                } else if (_tagName.equals("refreshRate")) {
                    com.android.server.display.config.RefreshRateConfigs _value11 = com.android.server.display.config.RefreshRateConfigs.read(_parser);
                    _instance.setRefreshRate(_value11);
                } else if (_tagName.equals("screenBrightnessRampFastDecrease")) {
                    java.lang.String _raw3 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value12 = new java.math.BigDecimal(_raw3);
                    _instance.setScreenBrightnessRampFastDecrease(_value12);
                } else if (_tagName.equals("screenBrightnessRampFastIncrease")) {
                    java.lang.String _raw4 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value13 = new java.math.BigDecimal(_raw4);
                    _instance.setScreenBrightnessRampFastIncrease(_value13);
                } else if (_tagName.equals("screenBrightnessRampSlowDecrease")) {
                    java.lang.String _raw5 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value14 = new java.math.BigDecimal(_raw5);
                    _instance.setScreenBrightnessRampSlowDecrease(_value14);
                } else if (_tagName.equals("screenBrightnessRampSlowIncrease")) {
                    java.lang.String _raw6 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value15 = new java.math.BigDecimal(_raw6);
                    _instance.setScreenBrightnessRampSlowIncrease(_value15);
                } else if (_tagName.equals("screenBrightnessRampSlowDecreaseIdle")) {
                    java.lang.String _raw7 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value16 = new java.math.BigDecimal(_raw7);
                    _instance.setScreenBrightnessRampSlowDecreaseIdle(_value16);
                } else if (_tagName.equals("screenBrightnessRampSlowIncreaseIdle")) {
                    java.lang.String _raw8 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value17 = new java.math.BigDecimal(_raw8);
                    _instance.setScreenBrightnessRampSlowIncreaseIdle(_value17);
                } else if (_tagName.equals("screenBrightnessRampIncreaseMaxMillis")) {
                    java.lang.String _raw9 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value18 = new java.math.BigInteger(_raw9);
                    _instance.setScreenBrightnessRampIncreaseMaxMillis(_value18);
                } else if (_tagName.equals("screenBrightnessRampDecreaseMaxMillis")) {
                    java.lang.String _raw10 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value19 = new java.math.BigInteger(_raw10);
                    _instance.setScreenBrightnessRampDecreaseMaxMillis(_value19);
                } else if (_tagName.equals("screenBrightnessRampIncreaseMaxIdleMillis")) {
                    java.lang.String _raw11 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value20 = new java.math.BigInteger(_raw11);
                    _instance.setScreenBrightnessRampIncreaseMaxIdleMillis(_value20);
                } else if (_tagName.equals("screenBrightnessRampDecreaseMaxIdleMillis")) {
                    java.lang.String _raw12 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value21 = new java.math.BigInteger(_raw12);
                    _instance.setScreenBrightnessRampDecreaseMaxIdleMillis(_value21);
                } else if (_tagName.equals("lightSensor")) {
                    com.android.server.display.config.SensorDetails _value22 = com.android.server.display.config.SensorDetails.read(_parser);
                    _instance.setLightSensor(_value22);
                } else if (_tagName.equals("screenOffBrightnessSensor")) {
                    com.android.server.display.config.SensorDetails _value23 = com.android.server.display.config.SensorDetails.read(_parser);
                    _instance.setScreenOffBrightnessSensor(_value23);
                } else if (_tagName.equals("proxSensor")) {
                    com.android.server.display.config.SensorDetails _value24 = com.android.server.display.config.SensorDetails.read(_parser);
                    _instance.getProxSensor().add(_value24);
                } else if (_tagName.equals("tempSensor")) {
                    com.android.server.display.config.SensorDetails _value25 = com.android.server.display.config.SensorDetails.read(_parser);
                    _instance.setTempSensor(_value25);
                } else if (_tagName.equals("ambientLightHorizonLong")) {
                    java.lang.String _raw13 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value26 = new java.math.BigInteger(_raw13);
                    _instance.setAmbientLightHorizonLong(_value26);
                } else if (_tagName.equals("ambientLightHorizonShort")) {
                    java.lang.String _raw14 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value27 = new java.math.BigInteger(_raw14);
                    _instance.setAmbientLightHorizonShort(_value27);
                } else if (_tagName.equals("displayBrightnessChangeThresholds")) {
                    com.android.server.display.config.Thresholds _value28 = com.android.server.display.config.Thresholds.read(_parser);
                    _instance.setDisplayBrightnessChangeThresholds(_value28);
                } else if (_tagName.equals("ambientBrightnessChangeThresholds")) {
                    com.android.server.display.config.Thresholds _value29 = com.android.server.display.config.Thresholds.read(_parser);
                    _instance.setAmbientBrightnessChangeThresholds(_value29);
                } else if (_tagName.equals("displayBrightnessChangeThresholdsIdle")) {
                    com.android.server.display.config.Thresholds _value30 = com.android.server.display.config.Thresholds.read(_parser);
                    _instance.setDisplayBrightnessChangeThresholdsIdle(_value30);
                } else if (_tagName.equals("ambientBrightnessChangeThresholdsIdle")) {
                    com.android.server.display.config.Thresholds _value31 = com.android.server.display.config.Thresholds.read(_parser);
                    _instance.setAmbientBrightnessChangeThresholdsIdle(_value31);
                } else if (_tagName.equals("screenOffBrightnessSensorValueToLux")) {
                    com.android.server.display.config.IntegerArray _value32 = com.android.server.display.config.IntegerArray.read(_parser);
                    _instance.setScreenOffBrightnessSensorValueToLux(_value32);
                } else if (_tagName.equals("usiVersion")) {
                    com.android.server.display.config.UsiVersion _value33 = com.android.server.display.config.UsiVersion.read(_parser);
                    _instance.setUsiVersion(_value33);
                } else if (_tagName.equals("evenDimmer")) {
                    com.android.server.display.config.EvenDimmerMode _value34 = com.android.server.display.config.EvenDimmerMode.read(_parser);
                    _instance.setEvenDimmer(_value34);
                } else if (_tagName.equals("screenBrightnessCapForWearBedtimeMode")) {
                    java.lang.String _raw15 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value35 = new java.math.BigDecimal(_raw15);
                    _instance.setScreenBrightnessCapForWearBedtimeMode(_value35);
                } else if (_tagName.equals("idleScreenRefreshRateTimeout")) {
                    com.android.server.display.config.IdleScreenRefreshRateTimeout _value36 = com.android.server.display.config.IdleScreenRefreshRateTimeout.read(_parser);
                    _instance.setIdleScreenRefreshRateTimeout(_value36);
                } else if (_tagName.equals("supportsVrr")) {
                    java.lang.String _raw16 = com.android.server.display.config.XmlParser.readText(_parser);
                    boolean _value37 = java.lang.Boolean.parseBoolean(_raw16);
                    _instance.setSupportsVrr(_value37);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("DisplayConfiguration is not closed");
        }
        return _instance;
    }
}
