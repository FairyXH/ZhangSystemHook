package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class RefreshRateConfigs {
    private java.math.BigInteger defaultPeakRefreshRate;
    private java.math.BigInteger defaultRefreshRate;
    private java.math.BigInteger defaultRefreshRateInHbmHdr;
    private java.math.BigInteger defaultRefreshRateInHbmSunlight;
    private com.android.server.display.config.BlockingZoneConfig higherBlockingZoneConfigs;
    private com.android.server.display.config.NonNegativeFloatToFloatMap lowPowerSupportedModes;
    private com.android.server.display.config.BlockingZoneConfig lowerBlockingZoneConfigs;
    private com.android.server.display.config.RefreshRateZoneProfiles refreshRateZoneProfiles;

    public final java.math.BigInteger getDefaultRefreshRate() {
        return this.defaultRefreshRate;
    }

    boolean hasDefaultRefreshRate() {
        if (this.defaultRefreshRate == null) {
            return false;
        }
        return true;
    }

    public final void setDefaultRefreshRate(java.math.BigInteger defaultRefreshRate) {
        this.defaultRefreshRate = defaultRefreshRate;
    }

    public final java.math.BigInteger getDefaultPeakRefreshRate() {
        return this.defaultPeakRefreshRate;
    }

    boolean hasDefaultPeakRefreshRate() {
        if (this.defaultPeakRefreshRate == null) {
            return false;
        }
        return true;
    }

    public final void setDefaultPeakRefreshRate(java.math.BigInteger defaultPeakRefreshRate) {
        this.defaultPeakRefreshRate = defaultPeakRefreshRate;
    }

    public final com.android.server.display.config.RefreshRateZoneProfiles getRefreshRateZoneProfiles() {
        return this.refreshRateZoneProfiles;
    }

    boolean hasRefreshRateZoneProfiles() {
        if (this.refreshRateZoneProfiles == null) {
            return false;
        }
        return true;
    }

    public final void setRefreshRateZoneProfiles(com.android.server.display.config.RefreshRateZoneProfiles refreshRateZoneProfiles) {
        this.refreshRateZoneProfiles = refreshRateZoneProfiles;
    }

    public final java.math.BigInteger getDefaultRefreshRateInHbmHdr() {
        return this.defaultRefreshRateInHbmHdr;
    }

    boolean hasDefaultRefreshRateInHbmHdr() {
        if (this.defaultRefreshRateInHbmHdr == null) {
            return false;
        }
        return true;
    }

    public final void setDefaultRefreshRateInHbmHdr(java.math.BigInteger defaultRefreshRateInHbmHdr) {
        this.defaultRefreshRateInHbmHdr = defaultRefreshRateInHbmHdr;
    }

    public final java.math.BigInteger getDefaultRefreshRateInHbmSunlight() {
        return this.defaultRefreshRateInHbmSunlight;
    }

    boolean hasDefaultRefreshRateInHbmSunlight() {
        if (this.defaultRefreshRateInHbmSunlight == null) {
            return false;
        }
        return true;
    }

    public final void setDefaultRefreshRateInHbmSunlight(java.math.BigInteger defaultRefreshRateInHbmSunlight) {
        this.defaultRefreshRateInHbmSunlight = defaultRefreshRateInHbmSunlight;
    }

    public final com.android.server.display.config.BlockingZoneConfig getLowerBlockingZoneConfigs() {
        return this.lowerBlockingZoneConfigs;
    }

    boolean hasLowerBlockingZoneConfigs() {
        if (this.lowerBlockingZoneConfigs == null) {
            return false;
        }
        return true;
    }

    public final void setLowerBlockingZoneConfigs(com.android.server.display.config.BlockingZoneConfig lowerBlockingZoneConfigs) {
        this.lowerBlockingZoneConfigs = lowerBlockingZoneConfigs;
    }

    public final com.android.server.display.config.BlockingZoneConfig getHigherBlockingZoneConfigs() {
        return this.higherBlockingZoneConfigs;
    }

    boolean hasHigherBlockingZoneConfigs() {
        if (this.higherBlockingZoneConfigs == null) {
            return false;
        }
        return true;
    }

    public final void setHigherBlockingZoneConfigs(com.android.server.display.config.BlockingZoneConfig higherBlockingZoneConfigs) {
        this.higherBlockingZoneConfigs = higherBlockingZoneConfigs;
    }

    public final com.android.server.display.config.NonNegativeFloatToFloatMap getLowPowerSupportedModes() {
        return this.lowPowerSupportedModes;
    }

    boolean hasLowPowerSupportedModes() {
        if (this.lowPowerSupportedModes == null) {
            return false;
        }
        return true;
    }

    public final void setLowPowerSupportedModes(com.android.server.display.config.NonNegativeFloatToFloatMap lowPowerSupportedModes) {
        this.lowPowerSupportedModes = lowPowerSupportedModes;
    }

    static com.android.server.display.config.RefreshRateConfigs read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.RefreshRateConfigs _instance = new com.android.server.display.config.RefreshRateConfigs();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("defaultRefreshRate")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value = new java.math.BigInteger(_raw);
                    _instance.setDefaultRefreshRate(_value);
                } else if (_tagName.equals("defaultPeakRefreshRate")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw2);
                    _instance.setDefaultPeakRefreshRate(_value2);
                } else if (_tagName.equals("refreshRateZoneProfiles")) {
                    com.android.server.display.config.RefreshRateZoneProfiles _value3 = com.android.server.display.config.RefreshRateZoneProfiles.read(_parser);
                    _instance.setRefreshRateZoneProfiles(_value3);
                } else if (_tagName.equals("defaultRefreshRateInHbmHdr")) {
                    java.lang.String _raw3 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value4 = new java.math.BigInteger(_raw3);
                    _instance.setDefaultRefreshRateInHbmHdr(_value4);
                } else if (_tagName.equals("defaultRefreshRateInHbmSunlight")) {
                    java.lang.String _raw4 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value5 = new java.math.BigInteger(_raw4);
                    _instance.setDefaultRefreshRateInHbmSunlight(_value5);
                } else if (_tagName.equals("lowerBlockingZoneConfigs")) {
                    com.android.server.display.config.BlockingZoneConfig _value6 = com.android.server.display.config.BlockingZoneConfig.read(_parser);
                    _instance.setLowerBlockingZoneConfigs(_value6);
                } else if (_tagName.equals("higherBlockingZoneConfigs")) {
                    com.android.server.display.config.BlockingZoneConfig _value7 = com.android.server.display.config.BlockingZoneConfig.read(_parser);
                    _instance.setHigherBlockingZoneConfigs(_value7);
                } else if (_tagName.equals("lowPowerSupportedModes")) {
                    com.android.server.display.config.NonNegativeFloatToFloatMap _value8 = com.android.server.display.config.NonNegativeFloatToFloatMap.read(_parser);
                    _instance.setLowPowerSupportedModes(_value8);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("RefreshRateConfigs is not closed");
        }
        return _instance;
    }
}
