package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class RefreshRateZone {
    private java.lang.String id;
    private com.android.server.display.config.RefreshRateRange refreshRateRange;

    public final com.android.server.display.config.RefreshRateRange getRefreshRateRange() {
        return this.refreshRateRange;
    }

    boolean hasRefreshRateRange() {
        if (this.refreshRateRange == null) {
            return false;
        }
        return true;
    }

    public final void setRefreshRateRange(com.android.server.display.config.RefreshRateRange refreshRateRange) {
        this.refreshRateRange = refreshRateRange;
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

    static com.android.server.display.config.RefreshRateZone read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.RefreshRateZone _instance = new com.android.server.display.config.RefreshRateZone();
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
                if (_tagName.equals("refreshRateRange")) {
                    com.android.server.display.config.RefreshRateRange _value = com.android.server.display.config.RefreshRateRange.read(_parser);
                    _instance.setRefreshRateRange(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("RefreshRateZone is not closed");
        }
        return _instance;
    }
}
