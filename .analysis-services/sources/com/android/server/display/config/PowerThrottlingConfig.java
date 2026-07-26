package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class PowerThrottlingConfig {
    private java.math.BigDecimal brightnessLowestCapAllowed;
    private java.math.BigInteger pollingWindowMillis;
    private java.util.List<com.android.server.display.config.PowerThrottlingMap> powerThrottlingMap;

    public final java.math.BigDecimal getBrightnessLowestCapAllowed() {
        return this.brightnessLowestCapAllowed;
    }

    boolean hasBrightnessLowestCapAllowed() {
        if (this.brightnessLowestCapAllowed == null) {
            return false;
        }
        return true;
    }

    public final void setBrightnessLowestCapAllowed(java.math.BigDecimal brightnessLowestCapAllowed) {
        this.brightnessLowestCapAllowed = brightnessLowestCapAllowed;
    }

    public final java.math.BigInteger getPollingWindowMillis() {
        return this.pollingWindowMillis;
    }

    boolean hasPollingWindowMillis() {
        if (this.pollingWindowMillis == null) {
            return false;
        }
        return true;
    }

    public final void setPollingWindowMillis(java.math.BigInteger pollingWindowMillis) {
        this.pollingWindowMillis = pollingWindowMillis;
    }

    public final java.util.List<com.android.server.display.config.PowerThrottlingMap> getPowerThrottlingMap() {
        if (this.powerThrottlingMap == null) {
            this.powerThrottlingMap = new java.util.ArrayList();
        }
        return this.powerThrottlingMap;
    }

    static com.android.server.display.config.PowerThrottlingConfig read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.PowerThrottlingConfig _instance = new com.android.server.display.config.PowerThrottlingConfig();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("brightnessLowestCapAllowed")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value = new java.math.BigDecimal(_raw);
                    _instance.setBrightnessLowestCapAllowed(_value);
                } else if (_tagName.equals("pollingWindowMillis")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw2);
                    _instance.setPollingWindowMillis(_value2);
                } else if (_tagName.equals("powerThrottlingMap")) {
                    com.android.server.display.config.PowerThrottlingMap _value3 = com.android.server.display.config.PowerThrottlingMap.read(_parser);
                    _instance.getPowerThrottlingMap().add(_value3);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("PowerThrottlingConfig is not closed");
        }
        return _instance;
    }
}
