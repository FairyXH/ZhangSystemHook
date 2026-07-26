package com.android.server.policy.devicestate.config;

/* JADX INFO: loaded from: classes3.dex */
public class KeyguardCondition {
    private java.lang.Boolean show;

    public boolean getShow() {
        if (this.show == null) {
            return false;
        }
        return this.show.booleanValue();
    }

    boolean hasShow() {
        if (this.show == null) {
            return false;
        }
        return true;
    }

    public void setShow(boolean show) {
        this.show = java.lang.Boolean.valueOf(show);
    }

    static com.android.server.policy.devicestate.config.KeyguardCondition read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.policy.devicestate.config.KeyguardCondition _instance = new com.android.server.policy.devicestate.config.KeyguardCondition();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("show")) {
                    java.lang.String _raw = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    boolean _value = java.lang.Boolean.parseBoolean(_raw);
                    _instance.setShow(_value);
                } else {
                    com.android.server.policy.devicestate.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("KeyguardCondition is not closed");
        }
        return _instance;
    }
}
