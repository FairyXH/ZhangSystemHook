package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class Thresholds {
    private com.android.server.display.config.BrightnessThresholds brighteningThresholds;
    private com.android.server.display.config.BrightnessThresholds darkeningThresholds;

    public final com.android.server.display.config.BrightnessThresholds getBrighteningThresholds() {
        return this.brighteningThresholds;
    }

    boolean hasBrighteningThresholds() {
        if (this.brighteningThresholds == null) {
            return false;
        }
        return true;
    }

    public final void setBrighteningThresholds(com.android.server.display.config.BrightnessThresholds brighteningThresholds) {
        this.brighteningThresholds = brighteningThresholds;
    }

    public final com.android.server.display.config.BrightnessThresholds getDarkeningThresholds() {
        return this.darkeningThresholds;
    }

    boolean hasDarkeningThresholds() {
        if (this.darkeningThresholds == null) {
            return false;
        }
        return true;
    }

    public final void setDarkeningThresholds(com.android.server.display.config.BrightnessThresholds darkeningThresholds) {
        this.darkeningThresholds = darkeningThresholds;
    }

    static com.android.server.display.config.Thresholds read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.Thresholds _instance = new com.android.server.display.config.Thresholds();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("brighteningThresholds")) {
                    com.android.server.display.config.BrightnessThresholds _value = com.android.server.display.config.BrightnessThresholds.read(_parser);
                    _instance.setBrighteningThresholds(_value);
                } else if (_tagName.equals("darkeningThresholds")) {
                    com.android.server.display.config.BrightnessThresholds _value2 = com.android.server.display.config.BrightnessThresholds.read(_parser);
                    _instance.setDarkeningThresholds(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Thresholds is not closed");
        }
        return _instance;
    }
}
