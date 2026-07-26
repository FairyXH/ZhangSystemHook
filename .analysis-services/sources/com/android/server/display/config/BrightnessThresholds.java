package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessThresholds {
    private com.android.server.display.config.ThresholdPoints brightnessThresholdPoints;
    private java.math.BigDecimal minimum;

    public final java.math.BigDecimal getMinimum() {
        return this.minimum;
    }

    boolean hasMinimum() {
        if (this.minimum == null) {
            return false;
        }
        return true;
    }

    public final void setMinimum(java.math.BigDecimal minimum) {
        this.minimum = minimum;
    }

    public final com.android.server.display.config.ThresholdPoints getBrightnessThresholdPoints() {
        return this.brightnessThresholdPoints;
    }

    boolean hasBrightnessThresholdPoints() {
        if (this.brightnessThresholdPoints == null) {
            return false;
        }
        return true;
    }

    public final void setBrightnessThresholdPoints(com.android.server.display.config.ThresholdPoints brightnessThresholdPoints) {
        this.brightnessThresholdPoints = brightnessThresholdPoints;
    }

    static com.android.server.display.config.BrightnessThresholds read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.BrightnessThresholds _instance = new com.android.server.display.config.BrightnessThresholds();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("minimum")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value = new java.math.BigDecimal(_raw);
                    _instance.setMinimum(_value);
                } else if (_tagName.equals("brightnessThresholdPoints")) {
                    com.android.server.display.config.ThresholdPoints _value2 = com.android.server.display.config.ThresholdPoints.read(_parser);
                    _instance.setBrightnessThresholdPoints(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("BrightnessThresholds is not closed");
        }
        return _instance;
    }
}
