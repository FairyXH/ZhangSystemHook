package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class ThresholdPoint {
    private java.math.BigDecimal percentage;
    private java.math.BigDecimal threshold;

    public final java.math.BigDecimal getThreshold() {
        return this.threshold;
    }

    boolean hasThreshold() {
        if (this.threshold == null) {
            return false;
        }
        return true;
    }

    public final void setThreshold(java.math.BigDecimal threshold) {
        this.threshold = threshold;
    }

    public final java.math.BigDecimal getPercentage() {
        return this.percentage;
    }

    boolean hasPercentage() {
        if (this.percentage == null) {
            return false;
        }
        return true;
    }

    public final void setPercentage(java.math.BigDecimal percentage) {
        this.percentage = percentage;
    }

    static com.android.server.display.config.ThresholdPoint read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.ThresholdPoint _instance = new com.android.server.display.config.ThresholdPoint();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("threshold")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value = new java.math.BigDecimal(_raw);
                    _instance.setThreshold(_value);
                } else if (_tagName.equals("percentage")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value2 = new java.math.BigDecimal(_raw2);
                    _instance.setPercentage(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("ThresholdPoint is not closed");
        }
        return _instance;
    }
}
