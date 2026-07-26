package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class ThermalThrottling {
    private java.util.List<com.android.server.display.config.BrightnessThrottlingMap> brightnessThrottlingMap;
    private java.util.List<com.android.server.display.config.RefreshRateThrottlingMap> refreshRateThrottlingMap;

    public final java.util.List<com.android.server.display.config.BrightnessThrottlingMap> getBrightnessThrottlingMap() {
        if (this.brightnessThrottlingMap == null) {
            this.brightnessThrottlingMap = new java.util.ArrayList();
        }
        return this.brightnessThrottlingMap;
    }

    public final java.util.List<com.android.server.display.config.RefreshRateThrottlingMap> getRefreshRateThrottlingMap() {
        if (this.refreshRateThrottlingMap == null) {
            this.refreshRateThrottlingMap = new java.util.ArrayList();
        }
        return this.refreshRateThrottlingMap;
    }

    static com.android.server.display.config.ThermalThrottling read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.ThermalThrottling _instance = new com.android.server.display.config.ThermalThrottling();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("brightnessThrottlingMap")) {
                    com.android.server.display.config.BrightnessThrottlingMap _value = com.android.server.display.config.BrightnessThrottlingMap.read(_parser);
                    _instance.getBrightnessThrottlingMap().add(_value);
                } else if (_tagName.equals("refreshRateThrottlingMap")) {
                    com.android.server.display.config.RefreshRateThrottlingMap _value2 = com.android.server.display.config.RefreshRateThrottlingMap.read(_parser);
                    _instance.getRefreshRateThrottlingMap().add(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("ThermalThrottling is not closed");
        }
        return _instance;
    }
}
