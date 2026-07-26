package com.android.server.policy.devicestate.config;

/* JADX INFO: loaded from: classes3.dex */
public class Properties {
    private java.util.List<java.lang.String> property;

    public java.util.List<java.lang.String> getProperty() {
        if (this.property == null) {
            this.property = new java.util.ArrayList();
        }
        return this.property;
    }

    static com.android.server.policy.devicestate.config.Properties read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.policy.devicestate.config.Properties _instance = new com.android.server.policy.devicestate.config.Properties();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("property")) {
                    java.lang.String _raw = com.android.server.policy.devicestate.config.XmlParser.readText(_parser);
                    _instance.getProperty().add(_raw);
                } else {
                    com.android.server.policy.devicestate.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Properties is not closed");
        }
        return _instance;
    }
}
