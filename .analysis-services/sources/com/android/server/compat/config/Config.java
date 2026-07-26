package com.android.server.compat.config;

/* JADX INFO: loaded from: classes.dex */
public class Config {
    private java.util.List<com.android.server.compat.config.Change> compatChange;

    public java.util.List<com.android.server.compat.config.Change> getCompatChange() {
        if (this.compatChange == null) {
            this.compatChange = new java.util.ArrayList();
        }
        return this.compatChange;
    }

    static com.android.server.compat.config.Config read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.compat.config.Config _instance = new com.android.server.compat.config.Config();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("compat-change")) {
                    com.android.server.compat.config.Change _value = com.android.server.compat.config.Change.read(_parser);
                    _instance.getCompatChange().add(_value);
                } else {
                    com.android.server.compat.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Config is not closed");
        }
        return _instance;
    }
}
