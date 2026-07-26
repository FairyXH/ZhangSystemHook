package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class RefreshRateRange {
    private java.math.BigInteger maximum;
    private java.math.BigInteger minimum;

    public final java.math.BigInteger getMinimum() {
        return this.minimum;
    }

    boolean hasMinimum() {
        if (this.minimum == null) {
            return false;
        }
        return true;
    }

    public final void setMinimum(java.math.BigInteger minimum) {
        this.minimum = minimum;
    }

    public final java.math.BigInteger getMaximum() {
        return this.maximum;
    }

    boolean hasMaximum() {
        if (this.maximum == null) {
            return false;
        }
        return true;
    }

    public final void setMaximum(java.math.BigInteger maximum) {
        this.maximum = maximum;
    }

    static com.android.server.display.config.RefreshRateRange read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.RefreshRateRange _instance = new com.android.server.display.config.RefreshRateRange();
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
                    java.math.BigInteger _value = new java.math.BigInteger(_raw);
                    _instance.setMinimum(_value);
                } else if (_tagName.equals("maximum")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw2);
                    _instance.setMaximum(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("RefreshRateRange is not closed");
        }
        return _instance;
    }
}
