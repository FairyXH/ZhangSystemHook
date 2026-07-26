package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class Point {
    private java.math.BigDecimal nits;
    private java.math.BigDecimal value;

    public final java.math.BigDecimal getValue() {
        return this.value;
    }

    boolean hasValue() {
        if (this.value == null) {
            return false;
        }
        return true;
    }

    public final void setValue(java.math.BigDecimal value) {
        this.value = value;
    }

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

    static com.android.server.display.config.Point read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.Point _instance = new com.android.server.display.config.Point();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("value")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value = new java.math.BigDecimal(_raw);
                    _instance.setValue(_value);
                } else if (_tagName.equals("nits")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value2 = new java.math.BigDecimal(_raw2);
                    _instance.setNits(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Point is not closed");
        }
        return _instance;
    }
}
