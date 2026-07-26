package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class Density {
    private java.math.BigInteger density;
    private java.math.BigInteger height;
    private java.math.BigInteger width;

    public final java.math.BigInteger getWidth() {
        return this.width;
    }

    boolean hasWidth() {
        if (this.width == null) {
            return false;
        }
        return true;
    }

    public final void setWidth(java.math.BigInteger width) {
        this.width = width;
    }

    public final java.math.BigInteger getHeight() {
        return this.height;
    }

    boolean hasHeight() {
        if (this.height == null) {
            return false;
        }
        return true;
    }

    public final void setHeight(java.math.BigInteger height) {
        this.height = height;
    }

    public final java.math.BigInteger getDensity() {
        return this.density;
    }

    boolean hasDensity() {
        if (this.density == null) {
            return false;
        }
        return true;
    }

    public final void setDensity(java.math.BigInteger density) {
        this.density = density;
    }

    static com.android.server.display.config.Density read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.Density _instance = new com.android.server.display.config.Density();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("width")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value = new java.math.BigInteger(_raw);
                    _instance.setWidth(_value);
                } else if (_tagName.equals("height")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw2);
                    _instance.setHeight(_value2);
                } else if (_tagName.equals("density")) {
                    java.lang.String _raw3 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value3 = new java.math.BigInteger(_raw3);
                    _instance.setDensity(_value3);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Density is not closed");
        }
        return _instance;
    }
}
