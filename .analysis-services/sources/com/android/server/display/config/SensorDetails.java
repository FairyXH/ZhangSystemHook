package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class SensorDetails {
    private java.lang.String featureFlag;
    private java.lang.String name;
    private com.android.server.display.config.RefreshRateRange refreshRate;
    private com.android.server.display.config.NonNegativeFloatToFloatMap supportedModes;
    private java.lang.String type;

    public final java.lang.String getType() {
        return this.type;
    }

    boolean hasType() {
        if (this.type == null) {
            return false;
        }
        return true;
    }

    public final void setType(java.lang.String type) {
        this.type = type;
    }

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

    public final com.android.server.display.config.RefreshRateRange getRefreshRate() {
        return this.refreshRate;
    }

    boolean hasRefreshRate() {
        if (this.refreshRate == null) {
            return false;
        }
        return true;
    }

    public final void setRefreshRate(com.android.server.display.config.RefreshRateRange refreshRate) {
        this.refreshRate = refreshRate;
    }

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

    public java.lang.String getFeatureFlag() {
        return this.featureFlag;
    }

    boolean hasFeatureFlag() {
        if (this.featureFlag == null) {
            return false;
        }
        return true;
    }

    public void setFeatureFlag(java.lang.String featureFlag) {
        this.featureFlag = featureFlag;
    }

    static com.android.server.display.config.SensorDetails read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.SensorDetails _instance = new com.android.server.display.config.SensorDetails();
        java.lang.String _raw = _parser.getAttributeValue(null, "featureFlag");
        if (_raw != null) {
            _instance.setFeatureFlag(_raw);
        }
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("type")) {
                    _instance.setType(com.android.server.display.config.XmlParser.readText(_parser));
                } else if (_tagName.equals("name")) {
                    _instance.setName(com.android.server.display.config.XmlParser.readText(_parser));
                } else if (_tagName.equals("refreshRate")) {
                    com.android.server.display.config.RefreshRateRange _value = com.android.server.display.config.RefreshRateRange.read(_parser);
                    _instance.setRefreshRate(_value);
                } else if (_tagName.equals("supportedModes")) {
                    com.android.server.display.config.NonNegativeFloatToFloatMap _value2 = com.android.server.display.config.NonNegativeFloatToFloatMap.read(_parser);
                    _instance.setSupportedModes(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("SensorDetails is not closed");
        }
        return _instance;
    }
}
