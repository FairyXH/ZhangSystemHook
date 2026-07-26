package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class NonNegativeFloatToFloatPoint {
    private java.math.BigDecimal first;
    private java.math.BigDecimal second;

    public final java.math.BigDecimal getFirst() {
        return this.first;
    }

    boolean hasFirst() {
        if (this.first == null) {
            return false;
        }
        return true;
    }

    public final void setFirst(java.math.BigDecimal first) {
        this.first = first;
    }

    public final java.math.BigDecimal getSecond() {
        return this.second;
    }

    boolean hasSecond() {
        if (this.second == null) {
            return false;
        }
        return true;
    }

    public final void setSecond(java.math.BigDecimal second) {
        this.second = second;
    }

    static com.android.server.display.config.NonNegativeFloatToFloatPoint read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.NonNegativeFloatToFloatPoint _instance = new com.android.server.display.config.NonNegativeFloatToFloatPoint();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("first")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value = new java.math.BigDecimal(_raw);
                    _instance.setFirst(_value);
                } else if (_tagName.equals("second")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value2 = new java.math.BigDecimal(_raw2);
                    _instance.setSecond(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("NonNegativeFloatToFloatPoint is not closed");
        }
        return _instance;
    }
}
