package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessPoint {
    private java.math.BigDecimal backlight;
    private java.math.BigDecimal brightness;
    private java.math.BigDecimal nits;

    public final java.math.BigDecimal getNits() {
        return this.nits;
    }

    boolean hasNits() {
        if (this.nits == null) {
            return false;
        }
        return true;
    }

    public final void setNits(java.math.BigDecimal nits) {
        this.nits = nits;
    }

    public final java.math.BigDecimal getBacklight() {
        return this.backlight;
    }

    boolean hasBacklight() {
        if (this.backlight == null) {
            return false;
        }
        return true;
    }

    public final void setBacklight(java.math.BigDecimal backlight) {
        this.backlight = backlight;
    }

    public final java.math.BigDecimal getBrightness() {
        return this.brightness;
    }

    boolean hasBrightness() {
        if (this.brightness == null) {
            return false;
        }
        return true;
    }

    public final void setBrightness(java.math.BigDecimal brightness) {
        this.brightness = brightness;
    }

    static com.android.server.display.config.BrightnessPoint read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.BrightnessPoint _instance = new com.android.server.display.config.BrightnessPoint();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("nits")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value = new java.math.BigDecimal(_raw);
                    _instance.setNits(_value);
                } else if (_tagName.equals("backlight")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value2 = new java.math.BigDecimal(_raw2);
                    _instance.setBacklight(_value2);
                } else if (_tagName.equals("brightness")) {
                    java.lang.String _raw3 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value3 = new java.math.BigDecimal(_raw3);
                    _instance.setBrightness(_value3);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("BrightnessPoint is not closed");
        }
        return _instance;
    }
}
