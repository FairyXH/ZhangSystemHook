package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class IdleScreenRefreshRateTimeout {
    private com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholds luxThresholds;

    public final com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholds getLuxThresholds() {
        return this.luxThresholds;
    }

    boolean hasLuxThresholds() {
        if (this.luxThresholds == null) {
            return false;
        }
        return true;
    }

    public final void setLuxThresholds(com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholds luxThresholds) {
        this.luxThresholds = luxThresholds;
    }

    static com.android.server.display.config.IdleScreenRefreshRateTimeout read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.IdleScreenRefreshRateTimeout _instance = new com.android.server.display.config.IdleScreenRefreshRateTimeout();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("luxThresholds")) {
                    com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholds _value = com.android.server.display.config.IdleScreenRefreshRateTimeoutLuxThresholds.read(_parser);
                    _instance.setLuxThresholds(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("IdleScreenRefreshRateTimeout is not closed");
        }
        return _instance;
    }
}
