package com.android.server.compat.config;

/* JADX INFO: loaded from: classes.dex */
public class Change {
    private java.lang.String description;
    private java.lang.Boolean disabled;
    private java.lang.Integer enableAfterTargetSdk;
    private java.lang.Integer enableSinceTargetSdk;
    private java.lang.Long id;
    private java.lang.Boolean loggingOnly;
    private java.lang.String name;
    private java.lang.Boolean overridable;
    private java.lang.String value;

    public long getId() {
        if (this.id == null) {
            return 0L;
        }
        return this.id.longValue();
    }

    boolean hasId() {
        if (this.id == null) {
            return false;
        }
        return true;
    }

    public void setId(long id) {
        this.id = java.lang.Long.valueOf(id);
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

    public boolean getDisabled() {
        if (this.disabled == null) {
            return false;
        }
        return this.disabled.booleanValue();
    }

    boolean hasDisabled() {
        if (this.disabled == null) {
            return false;
        }
        return true;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = java.lang.Boolean.valueOf(disabled);
    }

    public boolean getLoggingOnly() {
        if (this.loggingOnly == null) {
            return false;
        }
        return this.loggingOnly.booleanValue();
    }

    boolean hasLoggingOnly() {
        if (this.loggingOnly == null) {
            return false;
        }
        return true;
    }

    public void setLoggingOnly(boolean loggingOnly) {
        this.loggingOnly = java.lang.Boolean.valueOf(loggingOnly);
    }

    public int getEnableAfterTargetSdk() {
        if (this.enableAfterTargetSdk == null) {
            return 0;
        }
        return this.enableAfterTargetSdk.intValue();
    }

    boolean hasEnableAfterTargetSdk() {
        if (this.enableAfterTargetSdk == null) {
            return false;
        }
        return true;
    }

    public void setEnableAfterTargetSdk(int enableAfterTargetSdk) {
        this.enableAfterTargetSdk = java.lang.Integer.valueOf(enableAfterTargetSdk);
    }

    public int getEnableSinceTargetSdk() {
        if (this.enableSinceTargetSdk == null) {
            return 0;
        }
        return this.enableSinceTargetSdk.intValue();
    }

    boolean hasEnableSinceTargetSdk() {
        if (this.enableSinceTargetSdk == null) {
            return false;
        }
        return true;
    }

    public void setEnableSinceTargetSdk(int enableSinceTargetSdk) {
        this.enableSinceTargetSdk = java.lang.Integer.valueOf(enableSinceTargetSdk);
    }

    public java.lang.String getDescription() {
        return this.description;
    }

    boolean hasDescription() {
        if (this.description == null) {
            return false;
        }
        return true;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public boolean getOverridable() {
        if (this.overridable == null) {
            return false;
        }
        return this.overridable.booleanValue();
    }

    boolean hasOverridable() {
        if (this.overridable == null) {
            return false;
        }
        return true;
    }

    public void setOverridable(boolean overridable) {
        this.overridable = java.lang.Boolean.valueOf(overridable);
    }

    public java.lang.String getValue() {
        return this.value;
    }

    boolean hasValue() {
        if (this.value == null) {
            return false;
        }
        return true;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    static com.android.server.compat.config.Change read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        com.android.server.compat.config.Change _instance = new com.android.server.compat.config.Change();
        java.lang.String _raw = _parser.getAttributeValue(null, "id");
        if (_raw != null) {
            long _value = java.lang.Long.parseLong(_raw);
            _instance.setId(_value);
        }
        java.lang.String _raw2 = _parser.getAttributeValue(null, "name");
        if (_raw2 != null) {
            _instance.setName(_raw2);
        }
        java.lang.String _raw3 = _parser.getAttributeValue(null, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        if (_raw3 != null) {
            boolean _value2 = java.lang.Boolean.parseBoolean(_raw3);
            _instance.setDisabled(_value2);
        }
        java.lang.String _raw4 = _parser.getAttributeValue(null, "loggingOnly");
        if (_raw4 != null) {
            boolean _value3 = java.lang.Boolean.parseBoolean(_raw4);
            _instance.setLoggingOnly(_value3);
        }
        java.lang.String _raw5 = _parser.getAttributeValue(null, "enableAfterTargetSdk");
        if (_raw5 != null) {
            int _value4 = java.lang.Integer.parseInt(_raw5);
            _instance.setEnableAfterTargetSdk(_value4);
        }
        java.lang.String _raw6 = _parser.getAttributeValue(null, "enableSinceTargetSdk");
        if (_raw6 != null) {
            int _value5 = java.lang.Integer.parseInt(_raw6);
            _instance.setEnableSinceTargetSdk(_value5);
        }
        java.lang.String _raw7 = _parser.getAttributeValue(null, "description");
        if (_raw7 != null) {
            _instance.setDescription(_raw7);
        }
        java.lang.String _raw8 = _parser.getAttributeValue(null, "overridable");
        if (_raw8 != null) {
            boolean _value6 = java.lang.Boolean.parseBoolean(_raw8);
            _instance.setOverridable(_value6);
        }
        java.lang.String _raw9 = com.android.server.compat.config.XmlParser.readText(_parser);
        if (_raw9 != null) {
            _instance.setValue(_raw9);
        }
        return _instance;
    }
}
