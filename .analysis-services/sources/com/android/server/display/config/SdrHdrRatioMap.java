package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class SdrHdrRatioMap {
    private java.util.List<com.android.server.display.config.SdrHdrRatioPoint> point;

    public final java.util.List<com.android.server.display.config.SdrHdrRatioPoint> getPoint() {
        if (this.point == null) {
            this.point = new java.util.ArrayList();
        }
        return this.point;
    }

    static com.android.server.display.config.SdrHdrRatioMap read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.SdrHdrRatioMap _instance = new com.android.server.display.config.SdrHdrRatioMap();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("point")) {
                    com.android.server.display.config.SdrHdrRatioPoint _value = com.android.server.display.config.SdrHdrRatioPoint.read(_parser);
                    _instance.getPoint().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("SdrHdrRatioMap is not closed");
        }
        return _instance;
    }
}
