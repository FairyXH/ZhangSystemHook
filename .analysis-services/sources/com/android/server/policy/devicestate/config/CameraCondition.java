package com.android.server.policy.devicestate.config;

/* JADX INFO: loaded from: classes3.dex */
public class CameraCondition {
    private java.lang.Boolean open;

    public boolean getOpen() {
        if (this.open == null) {
            return false;
        }
        return this.open.booleanValue();
    }

    boolean hasOpen() {
        if (this.open == null) {
            return false;
        }
        return true;
    }

    public void setOpen(boolean open) {
        this.open = java.lang.Boolean.valueOf(open);
    }

    static com.android.server.policy.devicestate.config.CameraCondition read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.policy.devicestate.config.CameraCondition _instance = new com.android.server.policy.devicestate.config.CameraCondition();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("open")) {
                    java.lang.String _raw = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    boolean _value = java.lang.Boolean.parseBoolean(_raw);
                    _instance.setOpen(_value);
                } else {
                    com.android.server.policy.devicestate.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("CameraCondition is not closed");
        }
        return _instance;
    }
}
