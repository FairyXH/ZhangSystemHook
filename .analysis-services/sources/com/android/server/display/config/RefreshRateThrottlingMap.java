package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class RefreshRateThrottlingMap {
    private java.lang.String id;
    private java.util.List<com.android.server.display.config.RefreshRateThrottlingPoint> refreshRateThrottlingPoint;

    public final java.util.List<com.android.server.display.config.RefreshRateThrottlingPoint> getRefreshRateThrottlingPoint() {
        if (this.refreshRateThrottlingPoint == null) {
            this.refreshRateThrottlingPoint = new java.util.ArrayList();
        }
        return this.refreshRateThrottlingPoint;
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

    static com.android.server.display.config.RefreshRateThrottlingMap read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.RefreshRateThrottlingMap _instance = new com.android.server.display.config.RefreshRateThrottlingMap();
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
                if (_tagName.equals("refreshRateThrottlingPoint")) {
                    com.android.server.display.config.RefreshRateThrottlingPoint _value = com.android.server.display.config.RefreshRateThrottlingPoint.read(_parser);
                    _instance.getRefreshRateThrottlingPoint().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("RefreshRateThrottlingMap is not closed");
        }
        return _instance;
    }
}
