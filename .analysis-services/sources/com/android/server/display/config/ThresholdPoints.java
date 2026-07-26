package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class ThresholdPoints {
    private java.util.List<com.android.server.display.config.ThresholdPoint> brightnessThresholdPoint;

    public final java.util.List<com.android.server.display.config.ThresholdPoint> getBrightnessThresholdPoint() {
        if (this.brightnessThresholdPoint == null) {
            this.brightnessThresholdPoint = new java.util.ArrayList();
        }
        return this.brightnessThresholdPoint;
    }

    static com.android.server.display.config.ThresholdPoints read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.ThresholdPoints _instance = new com.android.server.display.config.ThresholdPoints();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("brightnessThresholdPoint")) {
                    com.android.server.display.config.ThresholdPoint _value = com.android.server.display.config.ThresholdPoint.read(_parser);
                    _instance.getBrightnessThresholdPoint().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("ThresholdPoints is not closed");
        }
        return _instance;
    }
}
