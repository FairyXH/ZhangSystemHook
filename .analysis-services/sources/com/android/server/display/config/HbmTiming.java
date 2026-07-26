package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class HbmTiming {
    private java.math.BigInteger timeMaxSecs_all;
    private java.math.BigInteger timeMinSecs_all;
    private java.math.BigInteger timeWindowSecs_all;

    public final java.math.BigInteger getTimeWindowSecs_all() {
        return this.timeWindowSecs_all;
    }

    boolean hasTimeWindowSecs_all() {
        if (this.timeWindowSecs_all == null) {
            return false;
        }
        return true;
    }

    public final void setTimeWindowSecs_all(java.math.BigInteger timeWindowSecs_all) {
        this.timeWindowSecs_all = timeWindowSecs_all;
    }

    public final java.math.BigInteger getTimeMaxSecs_all() {
        return this.timeMaxSecs_all;
    }

    boolean hasTimeMaxSecs_all() {
        if (this.timeMaxSecs_all == null) {
            return false;
        }
        return true;
    }

    public final void setTimeMaxSecs_all(java.math.BigInteger timeMaxSecs_all) {
        this.timeMaxSecs_all = timeMaxSecs_all;
    }

    public final java.math.BigInteger getTimeMinSecs_all() {
        return this.timeMinSecs_all;
    }

    boolean hasTimeMinSecs_all() {
        if (this.timeMinSecs_all == null) {
            return false;
        }
        return true;
    }

    public final void setTimeMinSecs_all(java.math.BigInteger timeMinSecs_all) {
        this.timeMinSecs_all = timeMinSecs_all;
    }

    static com.android.server.display.config.HbmTiming read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.HbmTiming _instance = new com.android.server.display.config.HbmTiming();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("timeWindowSecs")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value = new java.math.BigInteger(_raw);
                    _instance.setTimeWindowSecs_all(_value);
                } else if (_tagName.equals("timeMaxSecs")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw2);
                    _instance.setTimeMaxSecs_all(_value2);
                } else if (_tagName.equals("timeMinSecs")) {
                    java.lang.String _raw3 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value3 = new java.math.BigInteger(_raw3);
                    _instance.setTimeMinSecs_all(_value3);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("HbmTiming is not closed");
        }
        return _instance;
    }
}
