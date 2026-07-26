package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class IdleScreenRefreshRateTimeoutLuxThresholdPoint {
    private java.math.BigInteger lux;
    private java.math.BigInteger timeout;

    public final java.math.BigInteger getLux() {
        return this.lux;
    }

    boolean hasLux() {
        if (this.lux == null) {
            return false;
        }
        return true;
    }

    public final void setLux(java.math.BigInteger lux) {
        this.lux = lux;
    }

    public final java.math.BigInteger getTimeout() {
        return this.timeout;
    }

    boolean hasTimeout() {
        if (this.timeout == null) {
            return false;
        }
        return true;
    }

    public final void setTimeout(java.math.BigInteger timeout) {
        this.timeout = timeout;
    }

    static com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholdPoint read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholdPoint _instance = new com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholdPoint();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("lux")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value = new java.math.BigInteger(_raw);
                    _instance.setLux(_value);
                } else if (_tagName.equals("timeout")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw2);
                    _instance.setTimeout(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("IdleScreenRefreshRateTimeoutLuxThresholdPoint is not closed");
        }
        return _instance;
    }
}
