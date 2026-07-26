package com.android.server.compat.overrides;

/* JADX INFO: loaded from: classes.dex */
public class OverrideValue {
    private java.lang.Boolean enabled;
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

    static com.android.server.compat.overrides.OverrideValue read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        com.android.server.compat.overrides.OverrideValue _instance = new com.android.server.compat.overrides.OverrideValue();
        java.lang.String _raw = _parser.getAttributeValue(null, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        if (_raw != null) {
            _instance.setPackageName(_raw);
        }
        java.lang.String _raw2 = _parser.getAttributeValue(null, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
        if (_raw2 != null) {
            boolean _value = java.lang.Boolean.parseBoolean(_raw2);
            _instance.setEnabled(_value);
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
        if (hasEnabled()) {
            _out.print(" enabled=\"");
            _out.print(java.lang.Boolean.toString(getEnabled()));
            _out.print("\"");
        }
        _out.print(">\n");
        _out.print("</" + _name + ">\n");
    }
}
