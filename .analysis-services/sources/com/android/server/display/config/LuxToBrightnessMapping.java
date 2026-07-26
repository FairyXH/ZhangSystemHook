package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class LuxToBrightnessMapping {
    private com.android.server.display.config.NonNegativeFloatToFloatMap map;
    private com.android.server.display.config.AutoBrightnessModeName mode;
    private com.android.server.display.config.AutoBrightnessSettingName setting;

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

    public com.android.server.display.config.AutoBrightnessModeName getMode() {
        return this.mode;
    }

    boolean hasMode() {
        if (this.mode == null) {
            return false;
        }
        return true;
    }

    public void setMode(com.android.server.display.config.AutoBrightnessModeName mode) {
        this.mode = mode;
    }

    public com.android.server.display.config.AutoBrightnessSettingName getSetting() {
        return this.setting;
    }

    boolean hasSetting() {
        if (this.setting == null) {
            return false;
        }
        return true;
    }

    public void setSetting(com.android.server.display.config.AutoBrightnessSettingName setting) {
        this.setting = setting;
    }

    static com.android.server.display.config.LuxToBrightnessMapping read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.LuxToBrightnessMapping _instance = new com.android.server.display.config.LuxToBrightnessMapping();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("map")) {
                    com.android.server.display.config.NonNegativeFloatToFloatMap _value = com.android.server.display.config.NonNegativeFloatToFloatMap.read(_parser);
                    _instance.setMap(_value);
                } else if (_tagName.equals(com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY)) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    com.android.server.display.config.AutoBrightnessModeName _value2 = com.android.server.display.config.AutoBrightnessModeName.fromString(_raw);
                    _instance.setMode(_value2);
                } else if (_tagName.equals("setting")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    com.android.server.display.config.AutoBrightnessSettingName _value3 = com.android.server.display.config.AutoBrightnessSettingName.fromString(_raw2);
                    _instance.setSetting(_value3);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("LuxToBrightnessMapping is not closed");
        }
        return _instance;
    }
}
