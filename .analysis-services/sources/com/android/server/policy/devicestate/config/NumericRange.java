package com.android.server.policy.devicestate.config;

/* JADX INFO: loaded from: classes3.dex */
public class NumericRange {
    private java.math.BigDecimal maxInclusive_optional;
    private java.math.BigDecimal max_optional;
    private java.math.BigDecimal minInclusive_optional;
    private java.math.BigDecimal min_optional;

    public java.math.BigDecimal getMin_optional() {
        return this.min_optional;
    }

    boolean hasMin_optional() {
        if (this.min_optional == null) {
            return false;
        }
        return true;
    }

    public void setMin_optional(java.math.BigDecimal min_optional) {
        this.min_optional = min_optional;
    }

    public java.math.BigDecimal getMinInclusive_optional() {
        return this.minInclusive_optional;
    }

    boolean hasMinInclusive_optional() {
        if (this.minInclusive_optional == null) {
            return false;
        }
        return true;
    }

    public void setMinInclusive_optional(java.math.BigDecimal minInclusive_optional) {
        this.minInclusive_optional = minInclusive_optional;
    }

    public java.math.BigDecimal getMax_optional() {
        return this.max_optional;
    }

    boolean hasMax_optional() {
        if (this.max_optional == null) {
            return false;
        }
        return true;
    }

    public void setMax_optional(java.math.BigDecimal max_optional) {
        this.max_optional = max_optional;
    }

    public java.math.BigDecimal getMaxInclusive_optional() {
        return this.maxInclusive_optional;
    }

    boolean hasMaxInclusive_optional() {
        if (this.maxInclusive_optional == null) {
            return false;
        }
        return true;
    }

    public void setMaxInclusive_optional(java.math.BigDecimal maxInclusive_optional) {
        this.maxInclusive_optional = maxInclusive_optional;
    }

    static com.android.server.policy.devicestate.config.NumericRange read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.policy.devicestate.config.NumericRange _instance = new com.android.server.policy.devicestate.config.NumericRange();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("min")) {
                    java.lang.String _raw = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value = new java.math.BigDecimal(_raw);
                    _instance.setMin_optional(_value);
                } else if (_tagName.equals("min-inclusive")) {
                    java.lang.String _raw2 = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value2 = new java.math.BigDecimal(_raw2);
                    _instance.setMinInclusive_optional(_value2);
                } else if (_tagName.equals("max")) {
                    java.lang.String _raw3 = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value3 = new java.math.BigDecimal(_raw3);
                    _instance.setMax_optional(_value3);
                } else if (_tagName.equals("max-inclusive")) {
                    java.lang.String _raw4 = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    java.math.BigDecimal _value4 = new java.math.BigDecimal(_raw4);
                    _instance.setMaxInclusive_optional(_value4);
                } else {
                    com.android.server.policy.devicestate.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("NumericRange is not closed");
        }
        return _instance;
    }
}
