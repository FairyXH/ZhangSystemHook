package com.android.server.policy.devicestate.config;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayCondition {
    private java.math.BigInteger displayId;
    private java.lang.Boolean displayOn;

    public boolean getDisplayOn() {
        if (this.displayOn == null) {
            return false;
        }
        return this.displayOn.booleanValue();
    }

    boolean hasDisplayOn() {
        if (this.displayOn == null) {
            return false;
        }
        return true;
    }

    public void setDisplayOn(boolean displayOn) {
        this.displayOn = java.lang.Boolean.valueOf(displayOn);
    }

    public java.math.BigInteger getDisplayId() {
        return this.displayId;
    }

    boolean hasDisplayId() {
        if (this.displayId == null) {
            return false;
        }
        return true;
    }

    public void setDisplayId(java.math.BigInteger displayId) {
        this.displayId = displayId;
    }

    static com.android.server.policy.devicestate.config.DisplayCondition read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.policy.devicestate.config.DisplayCondition _instance = new com.android.server.policy.devicestate.config.DisplayCondition();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("displayOn")) {
                    java.lang.String _raw = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    boolean _value = java.lang.Boolean.parseBoolean(_raw);
                    _instance.setDisplayOn(_value);
                } else if (_tagName.equals("displayId")) {
                    java.lang.String _raw2 = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw2);
                    _instance.setDisplayId(_value2);
                } else {
                    com.android.server.policy.devicestate.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("DisplayCondition is not closed");
        }
        return _instance;
    }
}
