package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayQuirks {
    private java.util.List<java.lang.String> quirk;

    public java.util.List<java.lang.String> getQuirk() {
        if (this.quirk == null) {
            this.quirk = new java.util.ArrayList();
        }
        return this.quirk;
    }

    static com.android.server.display.config.DisplayQuirks read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.DisplayQuirks _instance = new com.android.server.display.config.DisplayQuirks();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("quirk")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    _instance.getQuirk().add(_raw);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("DisplayQuirks is not closed");
        }
        return _instance;
    }
}
