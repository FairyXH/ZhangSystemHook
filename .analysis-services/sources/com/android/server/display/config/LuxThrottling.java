package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class LuxThrottling {
    private java.util.List<com.android.server.display.config.BrightnessLimitMap> brightnessLimitMap;

    public final java.util.List<com.android.server.display.config.BrightnessLimitMap> getBrightnessLimitMap() {
        if (this.brightnessLimitMap == null) {
            this.brightnessLimitMap = new java.util.ArrayList();
        }
        return this.brightnessLimitMap;
    }

    static com.android.server.display.config.LuxThrottling read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.LuxThrottling _instance = new com.android.server.display.config.LuxThrottling();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("brightnessLimitMap")) {
                    com.android.server.display.config.BrightnessLimitMap _value = com.android.server.display.config.BrightnessLimitMap.read(_parser);
                    _instance.getBrightnessLimitMap().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("LuxThrottling is not closed");
        }
        return _instance;
    }
}
