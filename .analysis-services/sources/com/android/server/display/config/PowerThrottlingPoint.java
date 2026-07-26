package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class PowerThrottlingPoint {
    private java.math.BigDecimal powerQuotaMilliWatts;
    private com.android.server.display.config.ThermalStatus thermalStatus;

    public final com.android.server.display.config.ThermalStatus getThermalStatus() {
        return this.thermalStatus;
    }

    boolean hasThermalStatus() {
        if (this.thermalStatus == null) {
            return false;
        }
        return true;
    }

    public final void setThermalStatus(com.android.server.display.config.ThermalStatus thermalStatus) {
        this.thermalStatus = thermalStatus;
    }

    public final java.math.BigDecimal getPowerQuotaMilliWatts() {
        return this.powerQuotaMilliWatts;
    }

    boolean hasPowerQuotaMilliWatts() {
        if (this.powerQuotaMilliWatts == null) {
            return false;
        }
        return true;
    }

    public final void setPowerQuotaMilliWatts(java.math.BigDecimal powerQuotaMilliWatts) {
        this.powerQuotaMilliWatts = powerQuotaMilliWatts;
    }

    static com.android.server.display.config.PowerThrottlingPoint read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.PowerThrottlingPoint _instance = new com.android.server.display.config.PowerThrottlingPoint();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("thermalStatus")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    com.android.server.display.config.ThermalStatus _value = com.android.server.display.config.ThermalStatus.fromString(_raw);
                    _instance.setThermalStatus(_value);
                } else if (_tagName.equals("powerQuotaMilliWatts")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value2 = new java.math.BigDecimal(_raw2);
                    _instance.setPowerQuotaMilliWatts(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("PowerThrottlingPoint is not closed");
        }
        return _instance;
    }
}
