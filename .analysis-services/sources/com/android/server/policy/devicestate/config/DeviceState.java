package com.android.server.policy.devicestate.config;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceState {
    private com.android.server.policy.devicestate.config.Conditions conditions;
    private java.math.BigInteger identifier;
    private java.lang.String name;
    private com.android.server.policy.devicestate.config.Properties properties;

    public java.math.BigInteger getIdentifier() {
        return this.identifier;
    }

    boolean hasIdentifier() {
        if (this.identifier == null) {
            return false;
        }
        return true;
    }

    public void setIdentifier(java.math.BigInteger identifier) {
        this.identifier = identifier;
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

    public com.android.server.policy.devicestate.config.Properties getProperties() {
        return this.properties;
    }

    boolean hasProperties() {
        if (this.properties == null) {
            return false;
        }
        return true;
    }

    public void setProperties(com.android.server.policy.devicestate.config.Properties properties) {
        this.properties = properties;
    }

    public com.android.server.policy.devicestate.config.Conditions getConditions() {
        return this.conditions;
    }

    boolean hasConditions() {
        if (this.conditions == null) {
            return false;
        }
        return true;
    }

    public void setConditions(com.android.server.policy.devicestate.config.Conditions conditions) {
        this.conditions = conditions;
    }

    static com.android.server.policy.devicestate.config.DeviceState read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.policy.devicestate.config.DeviceState _instance = new com.android.server.policy.devicestate.config.DeviceState();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("identifier")) {
                    java.lang.String _raw = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value = new java.math.BigInteger(_raw);
                    _instance.setIdentifier(_value);
                } else if (_tagName.equals("name")) {
                    java.lang.String _raw2 = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    _instance.setName(_raw2);
                } else if (_tagName.equals("properties")) {
                    com.android.server.policy.devicestate.config.Properties _value2 = com.android.server.policy.devicestate.config.Properties.read(_parser);
                    _instance.setProperties(_value2);
                } else if (_tagName.equals("conditions")) {
                    com.android.server.policy.devicestate.config.Conditions _value3 = com.android.server.policy.devicestate.config.Conditions.read(_parser);
                    _instance.setConditions(_value3);
                } else {
                    com.android.server.policy.devicestate.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("DeviceState is not closed");
        }
        return _instance;
    }
}
