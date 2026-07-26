package com.android.server.display.config.layout;

/* JADX INFO: loaded from: classes2.dex */
public class Layout {
    private java.util.List<com.android.server.display.config.layout.Display> display;
    private java.math.BigInteger state;

    public java.math.BigInteger getState() {
        return this.state;
    }

    boolean hasState() {
        if (this.state == null) {
            return false;
        }
        return true;
    }

    public void setState(java.math.BigInteger state) {
        this.state = state;
    }

    public java.util.List<com.android.server.display.config.layout.Display> getDisplay() {
        if (this.display == null) {
            this.display = new java.util.ArrayList();
        }
        return this.display;
    }

    static com.android.server.display.config.layout.Layout read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.layout.Layout _instance = new com.android.server.display.config.layout.Layout();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("state")) {
                    java.lang.String _raw = com.android.server.display.config.layout.XmlParser.readText(_parser);
                    java.math.BigInteger _value = new java.math.BigInteger(_raw);
                    _instance.setState(_value);
                } else if (_tagName.equals("display")) {
                    com.android.server.display.config.layout.Display _value2 = com.android.server.display.config.layout.Display.read(_parser);
                    _instance.getDisplay().add(_value2);
                } else {
                    com.android.server.display.config.layout.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Layout is not closed");
        }
        return _instance;
    }
}
