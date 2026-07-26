package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class BlockingZoneConfig {
    private com.android.server.display.config.BlockingZoneThreshold blockingZoneThreshold;
    private java.math.BigInteger defaultRefreshRate;
    private java.lang.String refreshRateThermalThrottlingId;
    private com.android.server.display.config.NonNegativeFloatToFloatMap supportedModes;

    public final com.android.server.display.config.NonNegativeFloatToFloatMap getSupportedModes() {
        return this.supportedModes;
    }

    boolean hasSupportedModes() {
        if (this.supportedModes == null) {
            return false;
        }
        return true;
    }

    public final void setSupportedModes(com.android.server.display.config.NonNegativeFloatToFloatMap supportedModes) {
        this.supportedModes = supportedModes;
    }

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

    public final java.lang.String getRefreshRateThermalThrottlingId() {
        return this.refreshRateThermalThrottlingId;
    }

    boolean hasRefreshRateThermalThrottlingId() {
        if (this.refreshRateThermalThrottlingId == null) {
            return false;
        }
        return true;
    }

    public final void setRefreshRateThermalThrottlingId(java.lang.String refreshRateThermalThrottlingId) {
        this.refreshRateThermalThrottlingId = refreshRateThermalThrottlingId;
    }

    public final com.android.server.display.config.BlockingZoneThreshold getBlockingZoneThreshold() {
        return this.blockingZoneThreshold;
    }

    boolean hasBlockingZoneThreshold() {
        if (this.blockingZoneThreshold == null) {
            return false;
        }
        return true;
    }

    public final void setBlockingZoneThreshold(com.android.server.display.config.BlockingZoneThreshold blockingZoneThreshold) {
        this.blockingZoneThreshold = blockingZoneThreshold;
    }

    static com.android.server.display.config.BlockingZoneConfig read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.BlockingZoneConfig _instance = new com.android.server.display.config.BlockingZoneConfig();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("supportedModes")) {
                    com.android.server.display.config.NonNegativeFloatToFloatMap _value = com.android.server.display.config.NonNegativeFloatToFloatMap.read(_parser);
                    _instance.setSupportedModes(_value);
                } else if (_tagName.equals("defaultRefreshRate")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw);
                    _instance.setDefaultRefreshRate(_value2);
                } else if (_tagName.equals("refreshRateThermalThrottlingId")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    _instance.setRefreshRateThermalThrottlingId(_raw2);
                } else if (_tagName.equals("blockingZoneThreshold")) {
                    com.android.server.display.config.BlockingZoneThreshold _value3 = com.android.server.display.config.BlockingZoneThreshold.read(_parser);
                    _instance.setBlockingZoneThreshold(_value3);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("BlockingZoneConfig is not closed");
        }
        return _instance;
    }
}
