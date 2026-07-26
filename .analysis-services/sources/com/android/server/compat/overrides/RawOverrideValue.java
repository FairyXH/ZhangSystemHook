package com.android.server.compat.overrides;

/* JADX INFO: loaded from: classes.dex */
public class RawOverrideValue {
    private java.lang.Boolean enabled;
    private java.lang.Long maxVersionCode;
    private java.lang.Long minVersionCode;
    private java.lang.String packageName;

    public java.lang.String getPackageName() {
        return this.packageName;
    }

    boolean hasPackageName() {
        if (this.packageName == null) {
            return false;
        }
        return true;
    }

    public void setPackageName(java.lang.String packageName) {
        this.packageName = packageName;
    }

    public long getMinVersionCode() {
        if (this.minVersionCode == null) {
            return 0L;
        }
        return this.minVersionCode.longValue();
    }

    boolean hasMinVersionCode() {
        if (this.minVersionCode == null) {
            return false;
        }
        return true;
    }

    public void setMinVersionCode(long minVersionCode) {
        this.minVersionCode = java.lang.Long.valueOf(minVersionCode);
    }

    public long getMaxVersionCode() {
        if (this.maxVersionCode == null) {
            return 0L;
        }
        return this.maxVersionCode.longValue();
    }

    boolean hasMaxVersionCode() {
        if (this.maxVersionCode == null) {
            return false;
        }
        return true;
    }

    public void setMaxVersionCode(long maxVersionCode) {
        this.maxVersionCode = java.lang.Long.valueOf(maxVersionCode);
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

    static com.android.server.compat.overrides.RawOverrideValue read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        com.android.server.compat.overrides.RawOverrideValue _instance = new com.android.server.compat.overrides.RawOverrideValue();
        java.lang.String _raw = _parser.getAttributeValue(null, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        if (_raw != null) {
            _instance.setPackageName(_raw);
        }
        java.lang.String _raw2 = _parser.getAttributeValue(null, "minVersionCode");
        if (_raw2 != null) {
            long _value = java.lang.Long.parseLong(_raw2);
            _instance.setMinVersionCode(_value);
        }
        java.lang.String _raw3 = _parser.getAttributeValue(null, "maxVersionCode");
        if (_raw3 != null) {
            long _value2 = java.lang.Long.parseLong(_raw3);
            _instance.setMaxVersionCode(_value2);
        }
        java.lang.String _raw4 = _parser.getAttributeValue(null, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
        if (_raw4 != null) {
            boolean _value3 = java.lang.Boolean.parseBoolean(_raw4);
            _instance.setEnabled(_value3);
        }
        com.android.server.compat.overrides.XmlParser.skip(_parser);
        return _instance;
    }

    void write(com.android.server.compat.overrides.XmlWriter _out, java.lang.String _name) throws java.io.IOException {
        _out.print("<" + _name);
        if (hasPackageName()) {
            _out.print(" packageName=\"");
            _out.print(getPackageName());
            _out.print("\"");
        }
        if (hasMinVersionCode()) {
            _out.print(" minVersionCode=\"");
            _out.print(java.lang.Long.toString(getMinVersionCode()));
            _out.print("\"");
        }
        if (hasMaxVersionCode()) {
            _out.print(" maxVersionCode=\"");
            _out.print(java.lang.Long.toString(getMaxVersionCode()));
            _out.print("\"");
        }
        if (hasEnabled()) {
            _out.print(" enabled=\"");
            _out.print(java.lang.Boolean.toString(getEnabled()));
            _out.print("\"");
        }
        _out.print(">\n");
        _out.print("</" + _name + ">\n");
    }
}
