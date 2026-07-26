package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class EvenDimmerMode {
    private com.android.server.display.config.ComprehensiveBrightnessMap brightnessMapping;
    private java.lang.Boolean enabled;
    private com.android.server.display.config.NitsMap luxToMinimumNitsMap;
    private java.math.BigDecimal transitionPoint;

    public java.math.BigDecimal getTransitionPoint() {
        return this.transitionPoint;
    }

    boolean hasTransitionPoint() {
        if (this.transitionPoint == null) {
            return false;
        }
        return true;
    }

    public void setTransitionPoint(java.math.BigDecimal transitionPoint) {
        this.transitionPoint = transitionPoint;
    }

    public com.android.server.display.config.ComprehensiveBrightnessMap getBrightnessMapping() {
        return this.brightnessMapping;
    }

    boolean hasBrightnessMapping() {
        if (this.brightnessMapping == null) {
            return false;
        }
        return true;
    }

    public void setBrightnessMapping(com.android.server.display.config.ComprehensiveBrightnessMap brightnessMapping) {
        this.brightnessMapping = brightnessMapping;
    }

    public com.android.server.display.config.NitsMap getLuxToMinimumNitsMap() {
        return this.luxToMinimumNitsMap;
    }

    boolean hasLuxToMinimumNitsMap() {
        if (this.luxToMinimumNitsMap == null) {
            return false;
        }
        return true;
    }

    public void setLuxToMinimumNitsMap(com.android.server.display.config.NitsMap luxToMinimumNitsMap) {
        this.luxToMinimumNitsMap = luxToMinimumNitsMap;
    }

    public boolean getEnabled() {
        if (this.enabled == null) {
            return false;
        }
        return this.enabled.booleanValue();
    }

    boolean hasEnabled() {
        if (this.enabled == null) {
            return false;
        }
        return true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = java.lang.Boolean.valueOf(enabled);
    }

    static com.android.server.display.config.EvenDimmerMode read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.EvenDimmerMode _instance = new com.android.server.display.config.EvenDimmerMode();
        java.lang.String _raw = _parser.getAttributeValue(null, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
        if (_raw != null) {
            boolean _value = java.lang.Boolean.parseBoolean(_raw);
            _instance.setEnabled(_value);
        }
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("transitionPoint")) {
                    java.math.BigDecimal _value2 = new java.math.BigDecimal(com.android.server.display.config.XmlParser.readText(_parser));
                    _instance.setTransitionPoint(_value2);
                } else if (_tagName.equals("brightnessMapping")) {
                    com.android.server.display.config.ComprehensiveBrightnessMap _value3 = com.android.server.display.config.ComprehensiveBrightnessMap.read(_parser);
                    _instance.setBrightnessMapping(_value3);
                } else if (_tagName.equals("luxToMinimumNitsMap")) {
                    com.android.server.display.config.NitsMap _value4 = com.android.server.display.config.NitsMap.read(_parser);
                    _instance.setLuxToMinimumNitsMap(_value4);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("EvenDimmerMode is not closed");
        }
        return _instance;
    }
}
