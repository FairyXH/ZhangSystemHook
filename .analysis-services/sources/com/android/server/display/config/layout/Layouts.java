package com.android.server.display.config.layout;

/* JADX INFO: loaded from: classes2.dex */
public class Layouts {
    private java.util.List<com.android.server.display.config.layout.Layout> layout;

    public java.util.List<com.android.server.display.config.layout.Layout> getLayout() {
        if (this.layout == null) {
            this.layout = new java.util.ArrayList();
        }
        return this.layout;
    }

    static com.android.server.display.config.layout.Layouts read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.layout.Layouts _instance = new com.android.server.display.config.layout.Layouts();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("layout")) {
                    com.android.server.display.config.layout.Layout _value = com.android.server.display.config.layout.Layout.read(_parser);
                    _instance.getLayout().add(_value);
                } else {
                    com.android.server.display.config.layout.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Layouts is not closed");
        }
        return _instance;
    }
}
