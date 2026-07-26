package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessLimitMap {
    private com.android.server.display.config.NonNegativeFloatToFloatMap map;
    private com.android.server.display.config.PredefinedBrightnessLimitNames type;

    public final com.android.server.display.config.PredefinedBrightnessLimitNames getType() {
        return this.type;
    }

    boolean hasType() {
        if (this.type == null) {
            return false;
        }
        return true;
    }

    public final void setType(com.android.server.display.config.PredefinedBrightnessLimitNames type) {
        this.type = type;
    }

    public final com.android.server.display.config.NonNegativeFloatToFloatMap getMap() {
        return this.map;
    }

    boolean hasMap() {
        if (this.map == null) {
            return false;
        }
        return true;
    }

    public final void setMap(com.android.server.display.config.NonNegativeFloatToFloatMap map) {
        this.map = map;
    }

    static com.android.server.display.config.BrightnessLimitMap read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.BrightnessLimitMap _instance = new com.android.server.display.config.BrightnessLimitMap();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("type")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    com.android.server.display.config.PredefinedBrightnessLimitNames _value = com.android.server.display.config.PredefinedBrightnessLimitNames.fromString(_raw);
                    _instance.setType(_value);
                } else if (_tagName.equals("map")) {
                    com.android.server.display.config.NonNegativeFloatToFloatMap _value2 = com.android.server.display.config.NonNegativeFloatToFloatMap.read(_parser);
                    _instance.setMap(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("BrightnessLimitMap is not closed");
        }
        return _instance;
    }
}
