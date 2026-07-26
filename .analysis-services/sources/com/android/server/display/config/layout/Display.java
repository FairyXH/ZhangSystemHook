package com.android.server.display.config.layout;

/* JADX INFO: loaded from: classes2.dex */
public class Display {
    private java.math.BigInteger address_optional;
    private java.lang.String brightnessThrottlingMapId;
    private java.lang.Boolean defaultDisplay;
    private java.lang.String displayGroup;
    private java.lang.Boolean enabled;
    private java.math.BigInteger leadDisplayAddress;
    private java.math.BigInteger port_optional;
    private java.lang.String position;
    private java.lang.String powerThrottlingMapId;
    private java.lang.String refreshRateThermalThrottlingMapId;
    private java.lang.String refreshRateZoneId;

    public java.math.BigInteger getAddress_optional() {
        return this.address_optional;
    }

    boolean hasAddress_optional() {
        if (this.address_optional == null) {
            return false;
        }
        return true;
    }

    public void setAddress_optional(java.math.BigInteger address_optional) {
        this.address_optional = address_optional;
    }

    public java.math.BigInteger getPort_optional() {
        return this.port_optional;
    }

    boolean hasPort_optional() {
        if (this.port_optional == null) {
            return false;
        }
        return true;
    }

    public void setPort_optional(java.math.BigInteger port_optional) {
        this.port_optional = port_optional;
    }

    public java.lang.String getPosition() {
        return this.position;
    }

    boolean hasPosition() {
        if (this.position == null) {
            return false;
        }
        return true;
    }

    public void setPosition(java.lang.String position) {
        this.position = position;
    }

    public java.lang.String getBrightnessThrottlingMapId() {
        return this.brightnessThrottlingMapId;
    }

    boolean hasBrightnessThrottlingMapId() {
        if (this.brightnessThrottlingMapId == null) {
            return false;
        }
        return true;
    }

    public void setBrightnessThrottlingMapId(java.lang.String brightnessThrottlingMapId) {
        this.brightnessThrottlingMapId = brightnessThrottlingMapId;
    }

    public java.lang.String getPowerThrottlingMapId() {
        return this.powerThrottlingMapId;
    }

    boolean hasPowerThrottlingMapId() {
        if (this.powerThrottlingMapId == null) {
            return false;
        }
        return true;
    }

    public void setPowerThrottlingMapId(java.lang.String powerThrottlingMapId) {
        this.powerThrottlingMapId = powerThrottlingMapId;
    }

    public java.lang.String getRefreshRateThermalThrottlingMapId() {
        return this.refreshRateThermalThrottlingMapId;
    }

    boolean hasRefreshRateThermalThrottlingMapId() {
        if (this.refreshRateThermalThrottlingMapId == null) {
            return false;
        }
        return true;
    }

    public void setRefreshRateThermalThrottlingMapId(java.lang.String refreshRateThermalThrottlingMapId) {
        this.refreshRateThermalThrottlingMapId = refreshRateThermalThrottlingMapId;
    }

    public java.math.BigInteger getLeadDisplayAddress() {
        return this.leadDisplayAddress;
    }

    boolean hasLeadDisplayAddress() {
        if (this.leadDisplayAddress == null) {
            return false;
        }
        return true;
    }

    public void setLeadDisplayAddress(java.math.BigInteger leadDisplayAddress) {
        this.leadDisplayAddress = leadDisplayAddress;
    }

    public boolean isEnabled() {
        if (this.enabled == null) {
            return false;
        }
        return this.enabled.booleanValue();
    }

    boolean hasEnabled() {
        if (this.enabled == null) {
            return false;
        }
        return true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = java.lang.Boolean.valueOf(enabled);
    }

    public boolean isDefaultDisplay() {
        if (this.defaultDisplay == null) {
            return false;
        }
        return this.defaultDisplay.booleanValue();
    }

    boolean hasDefaultDisplay() {
        if (this.defaultDisplay == null) {
            return false;
        }
        return true;
    }

    public void setDefaultDisplay(boolean defaultDisplay) {
        this.defaultDisplay = java.lang.Boolean.valueOf(defaultDisplay);
    }

    public java.lang.String getRefreshRateZoneId() {
        return this.refreshRateZoneId;
    }

    boolean hasRefreshRateZoneId() {
        if (this.refreshRateZoneId == null) {
            return false;
        }
        return true;
    }

    public void setRefreshRateZoneId(java.lang.String refreshRateZoneId) {
        this.refreshRateZoneId = refreshRateZoneId;
    }

    public java.lang.String getDisplayGroup() {
        return this.displayGroup;
    }

    boolean hasDisplayGroup() {
        if (this.displayGroup == null) {
            return false;
        }
        return true;
    }

    public void setDisplayGroup(java.lang.String displayGroup) {
        this.displayGroup = displayGroup;
    }

    static com.android.server.display.config.layout.Display read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.layout.Display _instance = new com.android.server.display.config.layout.Display();
        java.lang.String _raw = _parser.getAttributeValue(null, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
        if (_raw != null) {
            boolean _value = java.lang.Boolean.parseBoolean(_raw);
            _instance.setEnabled(_value);
        }
        java.lang.String _raw2 = _parser.getAttributeValue(null, "defaultDisplay");
        if (_raw2 != null) {
            boolean _value2 = java.lang.Boolean.parseBoolean(_raw2);
            _instance.setDefaultDisplay(_value2);
        }
        java.lang.String _raw3 = _parser.getAttributeValue(null, "refreshRateZoneId");
        if (_raw3 != null) {
            _instance.setRefreshRateZoneId(_raw3);
        }
        java.lang.String _raw4 = _parser.getAttributeValue(null, "displayGroup");
        if (_raw4 != null) {
            _instance.setDisplayGroup(_raw4);
        }
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("address")) {
                    java.math.BigInteger _value3 = new java.math.BigInteger(com.android.server.display.config.layout.XmlParser.readText(_parser));
                    _instance.setAddress_optional(_value3);
                } else if (_tagName.equals("port")) {
                    java.math.BigInteger _value4 = new java.math.BigInteger(com.android.server.display.config.layout.XmlParser.readText(_parser));
                    _instance.setPort_optional(_value4);
                } else if (_tagName.equals("position")) {
                    _instance.setPosition(com.android.server.display.config.layout.XmlParser.readText(_parser));
                } else if (_tagName.equals("brightnessThrottlingMapId")) {
                    _instance.setBrightnessThrottlingMapId(com.android.server.display.config.layout.XmlParser.readText(_parser));
                } else if (_tagName.equals("powerThrottlingMapId")) {
                    _instance.setPowerThrottlingMapId(com.android.server.display.config.layout.XmlParser.readText(_parser));
                } else if (_tagName.equals("refreshRateThermalThrottlingMapId")) {
                    _instance.setRefreshRateThermalThrottlingMapId(com.android.server.display.config.layout.XmlParser.readText(_parser));
                } else if (_tagName.equals("leadDisplayAddress")) {
                    java.math.BigInteger _value5 = new java.math.BigInteger(com.android.server.display.config.layout.XmlParser.readText(_parser));
                    _instance.setLeadDisplayAddress(_value5);
                } else {
                    com.android.server.display.config.layout.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Display is not closed");
        }
        return _instance;
    }
}
