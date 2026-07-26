package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class PowerThrottlingMap {
    private java.lang.String id;
    private java.util.List<com.android.server.display.config.PowerThrottlingPoint> powerThrottlingPoint;

    public final java.util.List<com.android.server.display.config.PowerThrottlingPoint> getPowerThrottlingPoint() {
        if (this.powerThrottlingPoint == null) {
            this.powerThrottlingPoint = new java.util.ArrayList();
        }
        return this.powerThrottlingPoint;
    }

    public java.lang.String getId() {
        return this.id;
    }

    boolean hasId() {
        if (this.id == null) {
            return false;
        }
        return true;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    static com.android.server.display.config.PowerThrottlingMap read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.PowerThrottlingMap _instance = new com.android.server.display.config.PowerThrottlingMap();
        java.lang.String _raw = _parser.getAttributeValue(null, "id");
        if (_raw != null) {
            _instance.setId(_raw);
        }
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("powerThrottlingPoint")) {
                    com.android.server.display.config.PowerThrottlingPoint _value = com.android.server.display.config.PowerThrottlingPoint.read(_parser);
                    _instance.getPowerThrottlingPoint().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("PowerThrottlingMap is not closed");
        }
        return _instance;
    }
}
