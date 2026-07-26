package com.android.server.compat.overrides;

/* JADX INFO: loaded from: classes.dex */
public class Overrides {
    private java.util.List<com.android.server.compat.overrides.ChangeOverrides> changeOverrides;

    public java.util.List<com.android.server.compat.overrides.ChangeOverrides> getChangeOverrides() {
        if (this.changeOverrides == null) {
            this.changeOverrides = new java.util.ArrayList();
        }
        return this.changeOverrides;
    }

    static com.android.server.compat.overrides.Overrides read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.compat.overrides.Overrides _instance = new com.android.server.compat.overrides.Overrides();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("change-overrides")) {
                    com.android.server.compat.overrides.ChangeOverrides _value = com.android.server.compat.overrides.ChangeOverrides.read(_parser);
                    _instance.getChangeOverrides().add(_value);
                } else {
                    com.android.server.compat.overrides.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Overrides is not closed");
        }
        return _instance;
    }

    void write(com.android.server.compat.overrides.XmlWriter _out, java.lang.String _name) throws java.io.IOException {
        _out.print("<" + _name);
        _out.print(">\n");
        _out.increaseIndent();
        for (com.android.server.compat.overrides.ChangeOverrides value : getChangeOverrides()) {
            value.write(_out, "change-overrides");
        }
        _out.decreaseIndent();
        _out.print("</" + _name + ">\n");
    }
}
