package com.android.server.policy.devicestate.config;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceStateConfig {
    private java.util.List<com.android.server.policy.devicestate.config.DeviceState> deviceState;

    public java.util.List<com.android.server.policy.devicestate.config.DeviceState> getDeviceState() {
        if (this.deviceState == null) {
            this.deviceState = new java.util.ArrayList();
        }
        return this.deviceState;
    }

    static com.android.server.policy.devicestate.config.DeviceStateConfig read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.policy.devicestate.config.DeviceStateConfig _instance = new com.android.server.policy.devicestate.config.DeviceStateConfig();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("device-state")) {
                    com.android.server.policy.devicestate.config.DeviceState _value = com.android.server.policy.devicestate.config.DeviceState.read(_parser);
                    _instance.getDeviceState().add(_value);
                } else {
                    com.android.server.policy.devicestate.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("DeviceStateConfig is not closed");
        }
        return _instance;
    }
}
