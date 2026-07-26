package com.android.server.policy.devicestate.config;

/* JADX INFO: loaded from: classes3.dex */
public class SensorCondition {
    private java.lang.String name;
    private java.lang.String type;
    private java.lang.Boolean unregister;
    private java.util.List<com.android.server.policy.devicestate.config.NumericRange> value;

    public java.lang.String getType() {
        return this.type;
    }

    boolean hasType() {
        if (this.type == null) {
            return false;
        }
        return true;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.String getName() {
        return this.name;
    }

    boolean hasName() {
        if (this.name == null) {
            return false;
        }
        return true;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.util.List<com.android.server.policy.devicestate.config.NumericRange> getValue() {
        if (this.value == null) {
            this.value = new java.util.ArrayList();
        }
        return this.value;
    }

    public boolean getUnregister() {
        if (this.unregister == null) {
            return false;
        }
        return this.unregister.booleanValue();
    }

    boolean hasUnregister() {
        if (this.unregister == null) {
            return false;
        }
        return true;
    }

    public void setUnregister(boolean unregister) {
        this.unregister = java.lang.Boolean.valueOf(unregister);
    }

    static com.android.server.policy.devicestate.config.SensorCondition read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.policy.devicestate.config.SensorCondition _instance = new com.android.server.policy.devicestate.config.SensorCondition();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("type")) {
                    java.lang.String _raw = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    _instance.setType(_raw);
                } else if (_tagName.equals("name")) {
                    java.lang.String _raw2 = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    _instance.setName(_raw2);
                } else if (_tagName.equals("value")) {
                    com.android.server.policy.devicestate.config.NumericRange _value = com.android.server.policy.devicestate.config.NumericRange.read(_parser);
                    _instance.getValue().add(_value);
                } else if (_tagName.equals("unregister")) {
                    java.lang.String _raw3 = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    boolean _value2 = java.lang.Boolean.parseBoolean(_raw3);
                    _instance.setUnregister(_value2);
                } else {
                    com.android.server.policy.devicestate.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("SensorCondition is not closed");
        }
        return _instance;
    }
}
