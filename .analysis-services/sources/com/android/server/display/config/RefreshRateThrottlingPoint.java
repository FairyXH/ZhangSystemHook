package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class RefreshRateThrottlingPoint {
    private com.android.server.display.config.RefreshRateRange refreshRateRange;
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

    public final com.android.server.display.config.RefreshRateRange getRefreshRateRange() {
        return this.refreshRateRange;
    }

    boolean hasRefreshRateRange() {
        if (this.refreshRateRange == null) {
            return false;
        }
        return true;
    }

    public final void setRefreshRateRange(com.android.server.display.config.RefreshRateRange refreshRateRange) {
        this.refreshRateRange = refreshRateRange;
    }

    static com.android.server.display.config.RefreshRateThrottlingPoint read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.RefreshRateThrottlingPoint _instance = new com.android.server.display.config.RefreshRateThrottlingPoint();
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
                } else if (_tagName.equals("refreshRateRange")) {
                    com.android.server.display.config.RefreshRateRange _value2 = com.android.server.display.config.RefreshRateRange.read(_parser);
                    _instance.setRefreshRateRange(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("RefreshRateThrottlingPoint is not closed");
        }
        return _instance;
    }
}
