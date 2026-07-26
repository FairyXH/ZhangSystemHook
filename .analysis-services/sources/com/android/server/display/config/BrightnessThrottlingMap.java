package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessThrottlingMap {
    private java.util.List<com.android.server.display.config.BrightnessThrottlingPoint> brightnessThrottlingPoint;
    private java.lang.String id;

    public final java.util.List<com.android.server.display.config.BrightnessThrottlingPoint> getBrightnessThrottlingPoint() {
        if (this.brightnessThrottlingPoint == null) {
            this.brightnessThrottlingPoint = new java.util.ArrayList();
        }
        return this.brightnessThrottlingPoint;
    }

    public java.lang.String getId() {
        return this.id;
    }

    boolean hasId() {
        if (this.id == null) {
            return false;
        }
        return true;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    static com.android.server.display.config.BrightnessThrottlingMap read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.BrightnessThrottlingMap _instance = new com.android.server.display.config.BrightnessThrottlingMap();
        java.lang.String _raw = _parser.getAttributeValue(null, "id");
        if (_raw != null) {
            _instance.setId(_raw);
        }
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("brightnessThrottlingPoint")) {
                    com.android.server.display.config.BrightnessThrottlingPoint _value = com.android.server.display.config.BrightnessThrottlingPoint.read(_parser);
                    _instance.getBrightnessThrottlingPoint().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("BrightnessThrottlingMap is not closed");
        }
        return _instance;
    }
}
