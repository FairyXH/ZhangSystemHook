package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class IntegerArray {
    private java.util.List<java.math.BigInteger> item;

    public java.util.List<java.math.BigInteger> getItem() {
        if (this.item == null) {
            this.item = new java.util.ArrayList();
        }
        return this.item;
    }

    static com.android.server.display.config.IntegerArray read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.IntegerArray _instance = new com.android.server.display.config.IntegerArray();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value = new java.math.BigInteger(_raw);
                    _instance.getItem().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("IntegerArray is not closed");
        }
        return _instance;
    }
}
