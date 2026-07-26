package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class UsiVersion {
    private java.math.BigInteger majorVersion;
    private java.math.BigInteger minorVersion;

    public final java.math.BigInteger getMajorVersion() {
        return this.majorVersion;
    }

    boolean hasMajorVersion() {
        if (this.majorVersion == null) {
            return false;
        }
        return true;
    }

    public final void setMajorVersion(java.math.BigInteger majorVersion) {
        this.majorVersion = majorVersion;
    }

    public final java.math.BigInteger getMinorVersion() {
        return this.minorVersion;
    }

    boolean hasMinorVersion() {
        if (this.minorVersion == null) {
            return false;
        }
        return true;
    }

    public final void setMinorVersion(java.math.BigInteger minorVersion) {
        this.minorVersion = minorVersion;
    }

    static com.android.server.display.config.UsiVersion read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.UsiVersion _instance = new com.android.server.display.config.UsiVersion();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("majorVersion")) {
                    java.lang.String _raw = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value = new java.math.BigInteger(_raw);
                    _instance.setMajorVersion(_value);
                } else if (_tagName.equals("minorVersion")) {
                    java.lang.String _raw2 = com.android.server.display.config.XmlParser.readText(_parser);
                    java.math.BigInteger _value2 = new java.math.BigInteger(_raw2);
                    _instance.setMinorVersion(_value2);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("UsiVersion is not closed");
        }
        return _instance;
    }
}
