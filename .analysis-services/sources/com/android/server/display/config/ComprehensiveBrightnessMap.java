package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class ComprehensiveBrightnessMap {
    private java.util.List<com.android.server.display.config.BrightnessPoint> brightnessPoint;
    private java.lang.String interpolation;

    public final java.util.List<com.android.server.display.config.BrightnessPoint> getBrightnessPoint() {
        if (this.brightnessPoint == null) {
            this.brightnessPoint = new java.util.ArrayList();
        }
        return this.brightnessPoint;
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

    static com.android.server.display.config.ComprehensiveBrightnessMap read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.ComprehensiveBrightnessMap _instance = new com.android.server.display.config.ComprehensiveBrightnessMap();
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
                if (_tagName.equals("brightnessPoint")) {
                    com.android.server.display.config.BrightnessPoint _value = com.android.server.display.config.BrightnessPoint.read(_parser);
                    _instance.getBrightnessPoint().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("ComprehensiveBrightnessMap is not closed");
        }
        return _instance;
    }
}
