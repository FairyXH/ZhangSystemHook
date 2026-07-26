package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class HighBrightnessMode {
    private java.lang.Boolean allowInLowPowerMode_all;
    private java.lang.Boolean enabled;
    private java.math.BigDecimal minimumHdrPercentOfScreen_all;
    private java.math.BigDecimal minimumLux_all;
    private com.android.server.display.config.RefreshRateRange refreshRate_all;
    private com.android.server.display.config.SdrHdrRatioMap sdrHdrRatioMap_all;
    private com.android.server.display.config.HbmTiming timing_all;
    private java.math.BigDecimal transitionPoint_all;

    public final java.math.BigDecimal getTransitionPoint_all() {
        return this.transitionPoint_all;
    }

    boolean hasTransitionPoint_all() {
        if (this.transitionPoint_all == null) {
            return false;
        }
        return true;
    }

    public final void setTransitionPoint_all(java.math.BigDecimal transitionPoint_all) {
        this.transitionPoint_all = transitionPoint_all;
    }

    public final java.math.BigDecimal getMinimumLux_all() {
        return this.minimumLux_all;
    }

    boolean hasMinimumLux_all() {
        if (this.minimumLux_all == null) {
            return false;
        }
        return true;
    }

    public final void setMinimumLux_all(java.math.BigDecimal minimumLux_all) {
        this.minimumLux_all = minimumLux_all;
    }

    public com.android.server.display.config.HbmTiming getTiming_all() {
        return this.timing_all;
    }

    boolean hasTiming_all() {
        if (this.timing_all == null) {
            return false;
        }
        return true;
    }

    public void setTiming_all(com.android.server.display.config.HbmTiming timing_all) {
        this.timing_all = timing_all;
    }

    public final com.android.server.display.config.RefreshRateRange getRefreshRate_all() {
        return this.refreshRate_all;
    }

    boolean hasRefreshRate_all() {
        if (this.refreshRate_all == null) {
            return false;
        }
        return true;
    }

    public final void setRefreshRate_all(com.android.server.display.config.RefreshRateRange refreshRate_all) {
        this.refreshRate_all = refreshRate_all;
    }

    public final boolean getAllowInLowPowerMode_all() {
        if (this.allowInLowPowerMode_all == null) {
            return false;
        }
        return this.allowInLowPowerMode_all.booleanValue();
    }

    boolean hasAllowInLowPowerMode_all() {
        if (this.allowInLowPowerMode_all == null) {
            return false;
        }
        return true;
    }

    public final void setAllowInLowPowerMode_all(boolean allowInLowPowerMode_all) {
        this.allowInLowPowerMode_all = java.lang.Boolean.valueOf(allowInLowPowerMode_all);
    }

    public final java.math.BigDecimal getMinimumHdrPercentOfScreen_all() {
        return this.minimumHdrPercentOfScreen_all;
    }

    boolean hasMinimumHdrPercentOfScreen_all() {
        if (this.minimumHdrPercentOfScreen_all == null) {
            return false;
        }
        return true;
    }

    public final void setMinimumHdrPercentOfScreen_all(java.math.BigDecimal minimumHdrPercentOfScreen_all) {
        this.minimumHdrPercentOfScreen_all = minimumHdrPercentOfScreen_all;
    }

    public final com.android.server.display.config.SdrHdrRatioMap getSdrHdrRatioMap_all() {
        return this.sdrHdrRatioMap_all;
    }

    boolean hasSdrHdrRatioMap_all() {
        if (this.sdrHdrRatioMap_all == null) {
            return false;
        }
        return true;
    }

    public final void setSdrHdrRatioMap_all(com.android.server.display.config.SdrHdrRatioMap sdrHdrRatioMap_all) {
        this.sdrHdrRatioMap_all = sdrHdrRatioMap_all;
    }

    public boolean getEnabled() {
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

    static com.android.server.display.config.HighBrightnessMode read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.HighBrightnessMode _instance = new com.android.server.display.config.HighBrightnessMode();
        java.lang.String _raw = _parser.getAttributeValue(null, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
        if (_raw != null) {
            boolean _value = java.lang.Boolean.parseBoolean(_raw);
            _instance.setEnabled(_value);
        }
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("transitionPoint")) {
                    java.math.BigDecimal _value2 = new java.math.BigDecimal(com.android.server.display.config.XmlParser.readText(_parser));
                    _instance.setTransitionPoint_all(_value2);
                } else if (_tagName.equals("minimumLux")) {
                    java.math.BigDecimal _value3 = new java.math.BigDecimal(com.android.server.display.config.XmlParser.readText(_parser));
                    _instance.setMinimumLux_all(_value3);
                } else if (_tagName.equals("timing")) {
                    com.android.server.display.config.HbmTiming _value4 = com.android.server.display.config.HbmTiming.read(_parser);
                    _instance.setTiming_all(_value4);
                } else if (_tagName.equals("refreshRate")) {
                    com.android.server.display.config.RefreshRateRange _value5 = com.android.server.display.config.RefreshRateRange.read(_parser);
                    _instance.setRefreshRate_all(_value5);
                } else if (_tagName.equals("allowInLowPowerMode")) {
                    boolean _value6 = java.lang.Boolean.parseBoolean(com.android.server.display.config.XmlParser.readText(_parser));
                    _instance.setAllowInLowPowerMode_all(_value6);
                } else if (_tagName.equals("minimumHdrPercentOfScreen")) {
                    java.math.BigDecimal _value7 = new java.math.BigDecimal(com.android.server.display.config.XmlParser.readText(_parser));
                    _instance.setMinimumHdrPercentOfScreen_all(_value7);
                } else if (_tagName.equals("sdrHdrRatioMap")) {
                    com.android.server.display.config.SdrHdrRatioMap _value8 = com.android.server.display.config.SdrHdrRatioMap.read(_parser);
                    _instance.setSdrHdrRatioMap_all(_value8);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("HighBrightnessMode is not closed");
        }
        return _instance;
    }
}
