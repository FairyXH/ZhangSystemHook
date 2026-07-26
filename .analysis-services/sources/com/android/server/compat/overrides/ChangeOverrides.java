package com.android.server.compat.overrides;

/* JADX INFO: loaded from: classes.dex */
public class ChangeOverrides {
    private java.lang.Long changeId;
    private com.android.server.compat.overrides.ChangeOverrides.Deferred deferred;
    private com.android.server.compat.overrides.ChangeOverrides.Raw raw;
    private com.android.server.compat.overrides.ChangeOverrides.Validated validated;

    public static class Validated {
        private java.util.List<com.android.server.compat.overrides.OverrideValue> overrideValue;

        public java.util.List<com.android.server.compat.overrides.OverrideValue> getOverrideValue() {
            if (this.overrideValue == null) {
                this.overrideValue = new java.util.ArrayList();
            }
            return this.overrideValue;
        }

        static com.android.server.compat.overrides.ChangeOverrides.Validated read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
            int type;
            com.android.server.compat.overrides.ChangeOverrides.Validated _instance = new com.android.server.compat.overrides.ChangeOverrides.Validated();
            _parser.getDepth();
            while (true) {
                type = _parser.next();
                if (type == 1 || type == 3) {
                    break;
                }
                if (_parser.getEventType() == 2) {
                    java.lang.String _tagName = _parser.getName();
                    if (_tagName.equals("override-value")) {
                        com.android.server.compat.overrides.OverrideValue _value = com.android.server.compat.overrides.OverrideValue.read(_parser);
                        _instance.getOverrideValue().add(_value);
                    } else {
                        com.android.server.compat.overrides.XmlParser.skip(_parser);
                    }
                }
            }
            if (type != 3) {
                throw new javax.xml.datatype.DatatypeConfigurationException("ChangeOverrides.Validated is not closed");
            }
            return _instance;
        }

        void write(com.android.server.compat.overrides.XmlWriter _out, java.lang.String _name) throws java.io.IOException {
            _out.print("<" + _name);
            _out.print(">\n");
            _out.increaseIndent();
            for (com.android.server.compat.overrides.OverrideValue value : getOverrideValue()) {
                value.write(_out, "override-value");
            }
            _out.decreaseIndent();
            _out.print("</" + _name + ">\n");
        }
    }

    public static class Deferred {
        private java.util.List<com.android.server.compat.overrides.OverrideValue> overrideValue;

        public java.util.List<com.android.server.compat.overrides.OverrideValue> getOverrideValue() {
            if (this.overrideValue == null) {
                this.overrideValue = new java.util.ArrayList();
            }
            return this.overrideValue;
        }

        static com.android.server.compat.overrides.ChangeOverrides.Deferred read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
            int type;
            com.android.server.compat.overrides.ChangeOverrides.Deferred _instance = new com.android.server.compat.overrides.ChangeOverrides.Deferred();
            _parser.getDepth();
            while (true) {
                type = _parser.next();
                if (type == 1 || type == 3) {
                    break;
                }
                if (_parser.getEventType() == 2) {
                    java.lang.String _tagName = _parser.getName();
                    if (_tagName.equals("override-value")) {
                        com.android.server.compat.overrides.OverrideValue _value = com.android.server.compat.overrides.OverrideValue.read(_parser);
                        _instance.getOverrideValue().add(_value);
                    } else {
                        com.android.server.compat.overrides.XmlParser.skip(_parser);
                    }
                }
            }
            if (type != 3) {
                throw new javax.xml.datatype.DatatypeConfigurationException("ChangeOverrides.Deferred is not closed");
            }
            return _instance;
        }

        void write(com.android.server.compat.overrides.XmlWriter _out, java.lang.String _name) throws java.io.IOException {
            _out.print("<" + _name);
            _out.print(">\n");
            _out.increaseIndent();
            for (com.android.server.compat.overrides.OverrideValue value : getOverrideValue()) {
                value.write(_out, "override-value");
            }
            _out.decreaseIndent();
            _out.print("</" + _name + ">\n");
        }
    }

    public static class Raw {
        private java.util.List<com.android.server.compat.overrides.RawOverrideValue> rawOverrideValue;

        public java.util.List<com.android.server.compat.overrides.RawOverrideValue> getRawOverrideValue() {
            if (this.rawOverrideValue == null) {
                this.rawOverrideValue = new java.util.ArrayList();
            }
            return this.rawOverrideValue;
        }

        static com.android.server.compat.overrides.ChangeOverrides.Raw read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
            int type;
            com.android.server.compat.overrides.ChangeOverrides.Raw _instance = new com.android.server.compat.overrides.ChangeOverrides.Raw();
            _parser.getDepth();
            while (true) {
                type = _parser.next();
                if (type == 1 || type == 3) {
                    break;
                }
                if (_parser.getEventType() == 2) {
                    java.lang.String _tagName = _parser.getName();
                    if (_tagName.equals("raw-override-value")) {
                        com.android.server.compat.overrides.RawOverrideValue _value = com.android.server.compat.overrides.RawOverrideValue.read(_parser);
                        _instance.getRawOverrideValue().add(_value);
                    } else {
                        com.android.server.compat.overrides.XmlParser.skip(_parser);
                    }
                }
            }
            if (type != 3) {
                throw new javax.xml.datatype.DatatypeConfigurationException("ChangeOverrides.Raw is not closed");
            }
            return _instance;
        }

        void write(com.android.server.compat.overrides.XmlWriter _out, java.lang.String _name) throws java.io.IOException {
            _out.print("<" + _name);
            _out.print(">\n");
            _out.increaseIndent();
            for (com.android.server.compat.overrides.RawOverrideValue value : getRawOverrideValue()) {
                value.write(_out, "raw-override-value");
            }
            _out.decreaseIndent();
            _out.print("</" + _name + ">\n");
        }
    }

    public com.android.server.compat.overrides.ChangeOverrides.Validated getValidated() {
        return this.validated;
    }

    boolean hasValidated() {
        if (this.validated == null) {
            return false;
        }
        return true;
    }

    public void setValidated(com.android.server.compat.overrides.ChangeOverrides.Validated validated) {
        this.validated = validated;
    }

    public com.android.server.compat.overrides.ChangeOverrides.Deferred getDeferred() {
        return this.deferred;
    }

    boolean hasDeferred() {
        if (this.deferred == null) {
            return false;
        }
        return true;
    }

    public void setDeferred(com.android.server.compat.overrides.ChangeOverrides.Deferred deferred) {
        this.deferred = deferred;
    }

    public com.android.server.compat.overrides.ChangeOverrides.Raw getRaw() {
        return this.raw;
    }

    boolean hasRaw() {
        if (this.raw == null) {
            return false;
        }
        return true;
    }

    public void setRaw(com.android.server.compat.overrides.ChangeOverrides.Raw raw) {
        this.raw = raw;
    }

    public long getChangeId() {
        if (this.changeId == null) {
            return 0L;
        }
        return this.changeId.longValue();
    }

    boolean hasChangeId() {
        if (this.changeId == null) {
            return false;
        }
        return true;
    }

    public void setChangeId(long changeId) {
        this.changeId = java.lang.Long.valueOf(changeId);
    }

    static com.android.server.compat.overrides.ChangeOverrides read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.compat.overrides.ChangeOverrides _instance = new com.android.server.compat.overrides.ChangeOverrides();
        java.lang.String _raw = _parser.getAttributeValue(null, "changeId");
        if (_raw != null) {
            long _value = java.lang.Long.parseLong(_raw);
            _instance.setChangeId(_value);
        }
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("validated")) {
                    com.android.server.compat.overrides.ChangeOverrides.Validated _value2 = com.android.server.compat.overrides.ChangeOverrides.Validated.read(_parser);
                    _instance.setValidated(_value2);
                } else if (_tagName.equals("deferred")) {
                    com.android.server.compat.overrides.ChangeOverrides.Deferred _value3 = com.android.server.compat.overrides.ChangeOverrides.Deferred.read(_parser);
                    _instance.setDeferred(_value3);
                } else if (_tagName.equals("raw")) {
                    com.android.server.compat.overrides.ChangeOverrides.Raw _value4 = com.android.server.compat.overrides.ChangeOverrides.Raw.read(_parser);
                    _instance.setRaw(_value4);
                } else {
                    com.android.server.compat.overrides.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("ChangeOverrides is not closed");
        }
        return _instance;
    }

    void write(com.android.server.compat.overrides.XmlWriter _out, java.lang.String _name) throws java.io.IOException {
        _out.print("<" + _name);
        if (hasChangeId()) {
            _out.print(" changeId=\"");
            _out.print(java.lang.Long.toString(getChangeId()));
            _out.print("\"");
        }
        _out.print(">\n");
        _out.increaseIndent();
        if (hasValidated()) {
            getValidated().write(_out, "validated");
        }
        if (hasDeferred()) {
            getDeferred().write(_out, "deferred");
        }
        if (hasRaw()) {
            getRaw().write(_out, "raw");
        }
        _out.decreaseIndent();
        _out.print("</" + _name + ">\n");
    }
}
