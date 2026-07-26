package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class SdrHdrRatioPoint {
    private java.math.BigDecimal hdrRatio;
    private java.math.BigDecimal sdrNits;

    public final java.math.BigDecimal getSdrNits() {
        return this.sdrNits;
    }

    boolean hasSdrNits() {
        if (this.sdrNits == null) {
            return false;
        }
        return true;
    }

    public final void setSdrNits(java.math.BigDecimal sdrNits) {
        this.sdrNits = sdrNits;
    }

    public final java.math.BigDecimal getHdrRatio() {
        return this.hdrRatio;
    }

    boolean hasHdrRatio() {
        if (this.hdrRatio == null) {
            return false;
        }
        return true;
    }

    public final void setHdrRatio(java.math.BigDecimal hdrRatio) {
        this.hdrRatio = hdrRatio;
    }

    static com.android.server.display.config.SdrHdrRatioPoint read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.SdrHdrRatioPoint _instance = new com.android.server.display.config.SdrHdrRatioPoint();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("sdrNits")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value = new java.math.BigDecimal(_raw);
                    _instance.setSdrNits(_value);
                } else if (_tagName.equals("hdrRatio")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value2 = new java.math.BigDecimal(_raw2);
                    _instance.setHdrRatio(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("SdrHdrRatioPoint is not closed");
        }
        return _instance;
    }
}
