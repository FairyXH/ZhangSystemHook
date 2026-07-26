package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class NitsMap {
    private java.lang.String interpolation;
    private java.util.List<com.android.server.display.config.Point> point;

    public final java.util.List<com.android.server.display.config.Point> getPoint() {
        if (this.point == null) {
            this.point = new java.util.ArrayList();
        }
        return this.point;
    }

    public java.lang.String getInterpolation() {
        return this.interpolation;
    }

    boolean hasInterpolation() {
        if (this.interpolation == null) {
            return false;
        }
        return true;
    }

    public void setInterpolation(java.lang.String interpolation) {
        this.interpolation = interpolation;
    }

    static com.android.server.display.config.NitsMap read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.NitsMap _instance = new com.android.server.display.config.NitsMap();
        java.lang.String _raw = _parser.getAttributeValue(null, "interpolation");
        if (_raw != null) {
            _instance.setInterpolation(_raw);
        }
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("point")) {
                    com.android.server.display.config.Point _value = com.android.server.display.config.Point.read(_parser);
                    _instance.getPoint().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("NitsMap is not closed");
        }
        return _instance;
    }
}
