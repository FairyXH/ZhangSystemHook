package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class HdrBrightnessConfig {
    private java.math.BigInteger brightnessDecreaseDebounceMillis;
    private java.math.BigInteger brightnessIncreaseDebounceMillis;
    private com.android.server.display.config.NonNegativeFloatToFloatMap brightnessMap;
    private java.math.BigDecimal screenBrightnessRampDecrease;
    private java.math.BigDecimal screenBrightnessRampIncrease;

    public final com.android.server.display.config.NonNegativeFloatToFloatMap getBrightnessMap() {
        return this.brightnessMap;
    }

    boolean hasBrightnessMap() {
        if (this.brightnessMap == null) {
            return false;
        }
        return true;
    }

    public final void setBrightnessMap(com.android.server.display.config.NonNegativeFloatToFloatMap brightnessMap) {
        this.brightnessMap = brightnessMap;
    }

    public final java.math.BigInteger getBrightnessIncreaseDebounceMillis() {
        return this.brightnessIncreaseDebounceMillis;
    }

    boolean hasBrightnessIncreaseDebounceMillis() {
        if (this.brightnessIncreaseDebounceMillis == null) {
            return false;
        }
        return true;
    }

    public final void setBrightnessIncreaseDebounceMillis(java.math.BigInteger brightnessIncreaseDebounceMillis) {
        this.brightnessIncreaseDebounceMillis = brightnessIncreaseDebounceMillis;
    }

    public final java.math.BigInteger getBrightnessDecreaseDebounceMillis() {
        return this.brightnessDecreaseDebounceMillis;
    }

    boolean hasBrightnessDecreaseDebounceMillis() {
        if (this.brightnessDecreaseDebounceMillis == null) {
            return false;
        }
        return true;
    }

    public final void setBrightnessDecreaseDebounceMillis(java.math.BigInteger brightnessDecreaseDebounceMillis) {
        this.brightnessDecreaseDebounceMillis = brightnessDecreaseDebounceMillis;
    }

    public final java.math.BigDecimal getScreenBrightnessRampIncrease() {
        return this.screenBrightnessRampIncrease;
    }

    boolean hasScreenBrightnessRampIncrease() {
        if (this.screenBrightnessRampIncrease == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampIncrease(java.math.BigDecimal screenBrightnessRampIncrease) {
        this.screenBrightnessRampIncrease = screenBrightnessRampIncrease;
    }

    public final java.math.BigDecimal getScreenBrightnessRampDecrease() {
        return this.screenBrightnessRampDecrease;
    }

    boolean hasScreenBrightnessRampDecrease() {
        if (this.screenBrightnessRampDecrease == null) {
            return false;
        }
        return true;
    }

    public final void setScreenBrightnessRampDecrease(java.math.BigDecimal screenBrightnessRampDecrease) {
        this.screenBrightnessRampDecrease = screenBrightnessRampDecrease;
    }

    static com.android.server.display.config.HdrBrightnessConfig read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.HdrBrightnessConfig _instance = new com.android.server.display.config.HdrBrightnessConfig();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("brightnessMap")) {
                    com.android.server.display.config.NonNegativeFloatToFloatMap _value = com.android.server.display.config.NonNegativeFloatToFloatMap.read(_parser);
                    _instance.setBrightnessMap(_value);
                } else if (_tagName.equals("brightnessIncreaseDebounceMillis")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw);
                    _instance.setBrightnessIncreaseDebounceMillis(_value2);
                } else if (_tagName.equals("brightnessDecreaseDebounceMillis")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value3 = new java.math.BigInteger(_raw2);
                    _instance.setBrightnessDecreaseDebounceMillis(_value3);
                } else if (_tagName.equals("screenBrightnessRampIncrease")) {
                    java.lang.String _raw3 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value4 = new java.math.BigDecimal(_raw3);
                    _instance.setScreenBrightnessRampIncrease(_value4);
                } else if (_tagName.equals("screenBrightnessRampDecrease")) {
                    java.lang.String _raw4 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value5 = new java.math.BigDecimal(_raw4);
                    _instance.setScreenBrightnessRampDecrease(_value5);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("HdrBrightnessConfig is not closed");
        }
        return _instance;
    }
}
