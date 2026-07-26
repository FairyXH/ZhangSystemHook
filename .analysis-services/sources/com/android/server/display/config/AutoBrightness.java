package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class AutoBrightness {
    private java.math.BigInteger brighteningLightDebounceIdleMillis;
    private java.math.BigInteger brighteningLightDebounceMillis;
    private java.math.BigInteger darkeningLightDebounceIdleMillis;
    private java.math.BigInteger darkeningLightDebounceMillis;
    private java.lang.Boolean enabled;
    private java.util.List<com.android.server.display.config.LuxToBrightnessMapping> luxToBrightnessMapping;

    public final java.math.BigInteger getBrighteningLightDebounceMillis() {
        return this.brighteningLightDebounceMillis;
    }

    boolean hasBrighteningLightDebounceMillis() {
        if (this.brighteningLightDebounceMillis == null) {
            return false;
        }
        return true;
    }

    public final void setBrighteningLightDebounceMillis(java.math.BigInteger brighteningLightDebounceMillis) {
        this.brighteningLightDebounceMillis = brighteningLightDebounceMillis;
    }

    public final java.math.BigInteger getDarkeningLightDebounceMillis() {
        return this.darkeningLightDebounceMillis;
    }

    boolean hasDarkeningLightDebounceMillis() {
        if (this.darkeningLightDebounceMillis == null) {
            return false;
        }
        return true;
    }

    public final void setDarkeningLightDebounceMillis(java.math.BigInteger darkeningLightDebounceMillis) {
        this.darkeningLightDebounceMillis = darkeningLightDebounceMillis;
    }

    public final java.math.BigInteger getBrighteningLightDebounceIdleMillis() {
        return this.brighteningLightDebounceIdleMillis;
    }

    boolean hasBrighteningLightDebounceIdleMillis() {
        if (this.brighteningLightDebounceIdleMillis == null) {
            return false;
        }
        return true;
    }

    public final void setBrighteningLightDebounceIdleMillis(java.math.BigInteger brighteningLightDebounceIdleMillis) {
        this.brighteningLightDebounceIdleMillis = brighteningLightDebounceIdleMillis;
    }

    public final java.math.BigInteger getDarkeningLightDebounceIdleMillis() {
        return this.darkeningLightDebounceIdleMillis;
    }

    boolean hasDarkeningLightDebounceIdleMillis() {
        if (this.darkeningLightDebounceIdleMillis == null) {
            return false;
        }
        return true;
    }

    public final void setDarkeningLightDebounceIdleMillis(java.math.BigInteger darkeningLightDebounceIdleMillis) {
        this.darkeningLightDebounceIdleMillis = darkeningLightDebounceIdleMillis;
    }

    public final java.util.List<com.android.server.display.config.LuxToBrightnessMapping> getLuxToBrightnessMapping() {
        if (this.luxToBrightnessMapping == null) {
            this.luxToBrightnessMapping = new java.util.ArrayList();
        }
        return this.luxToBrightnessMapping;
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

    static com.android.server.display.config.AutoBrightness read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.AutoBrightness _instance = new com.android.server.display.config.AutoBrightness();
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
                if (_tagName.equals("brighteningLightDebounceMillis")) {
                    java.math.BigInteger _value2 = new java.math.BigInteger(com.android.server.display.config.XmlParser.readText(_parser));
                    _instance.setBrighteningLightDebounceMillis(_value2);
                } else if (_tagName.equals("darkeningLightDebounceMillis")) {
                    java.math.BigInteger _value3 = new java.math.BigInteger(com.android.server.display.config.XmlParser.readText(_parser));
                    _instance.setDarkeningLightDebounceMillis(_value3);
                } else if (_tagName.equals("brighteningLightDebounceIdleMillis")) {
                    java.math.BigInteger _value4 = new java.math.BigInteger(com.android.server.display.config.XmlParser.readText(_parser));
                    _instance.setBrighteningLightDebounceIdleMillis(_value4);
                } else if (_tagName.equals("darkeningLightDebounceIdleMillis")) {
                    java.math.BigInteger _value5 = new java.math.BigInteger(com.android.server.display.config.XmlParser.readText(_parser));
                    _instance.setDarkeningLightDebounceIdleMillis(_value5);
                } else if (_tagName.equals("luxToBrightnessMapping")) {
                    com.android.server.display.config.LuxToBrightnessMapping _value6 = com.android.server.display.config.LuxToBrightnessMapping.read(_parser);
                    _instance.getLuxToBrightnessMapping().add(_value6);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("AutoBrightness is not closed");
        }
        return _instance;
    }
}
