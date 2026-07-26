package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class RefreshRateZoneProfiles {
    private java.util.List<com.android.server.display.config.RefreshRateZone> refreshRateZoneProfile;

    public final java.util.List<com.android.server.display.config.RefreshRateZone> getRefreshRateZoneProfile() {
        if (this.refreshRateZoneProfile == null) {
            this.refreshRateZoneProfile = new java.util.ArrayList();
        }
        return this.refreshRateZoneProfile;
    }

    static com.android.server.display.config.RefreshRateZoneProfiles read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.RefreshRateZoneProfiles _instance = new com.android.server.display.config.RefreshRateZoneProfiles();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("refreshRateZoneProfile")) {
                    com.android.server.display.config.RefreshRateZone _value = com.android.server.display.config.RefreshRateZone.read(_parser);
                    _instance.getRefreshRateZoneProfile().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("RefreshRateZoneProfiles is not closed");
        }
        return _instance;
    }
}
