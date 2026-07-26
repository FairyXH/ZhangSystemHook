package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class BlockingZoneThreshold {
    private java.util.List<com.android.server.display.config.DisplayBrightnessPoint> displayBrightnessPoint;

    public final java.util.List<com.android.server.display.config.DisplayBrightnessPoint> getDisplayBrightnessPoint() {
        if (this.displayBrightnessPoint == null) {
            this.displayBrightnessPoint = new java.util.ArrayList();
        }
        return this.displayBrightnessPoint;
    }

    static com.android.server.display.config.BlockingZoneThreshold read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.BlockingZoneThreshold _instance = new com.android.server.display.config.BlockingZoneThreshold();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("displayBrightnessPoint")) {
                    com.android.server.display.config.DisplayBrightnessPoint _value = com.android.server.display.config.DisplayBrightnessPoint.read(_parser);
                    _instance.getDisplayBrightnessPoint().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("BlockingZoneThreshold is not closed");
        }
        return _instance;
    }
}
