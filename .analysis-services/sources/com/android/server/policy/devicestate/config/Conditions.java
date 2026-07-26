package com.android.server.policy.devicestate.config;

/* JADX INFO: loaded from: classes3.dex */
public class Conditions {
    private com.android.server.policy.devicestate.config.CameraCondition camera;
    private com.android.server.policy.devicestate.config.DisplayCondition display;
    private com.android.server.policy.devicestate.config.KeyguardCondition keyguard;
    private com.android.server.policy.devicestate.config.LidSwitchCondition lidSwitch;
    private java.util.List<com.android.server.policy.devicestate.config.SensorCondition> sensor;

    public com.android.server.policy.devicestate.config.LidSwitchCondition getLidSwitch() {
        return this.lidSwitch;
    }

    boolean hasLidSwitch() {
        if (this.lidSwitch == null) {
            return false;
        }
        return true;
    }

    public void setLidSwitch(com.android.server.policy.devicestate.config.LidSwitchCondition lidSwitch) {
        this.lidSwitch = lidSwitch;
    }

    public java.util.List<com.android.server.policy.devicestate.config.SensorCondition> getSensor() {
        if (this.sensor == null) {
            this.sensor = new java.util.ArrayList();
        }
        return this.sensor;
    }

    public com.android.server.policy.devicestate.config.KeyguardCondition getKeyguard() {
        return this.keyguard;
    }

    boolean hasKeyguard() {
        if (this.keyguard == null) {
            return false;
        }
        return true;
    }

    public void setKeyguard(com.android.server.policy.devicestate.config.KeyguardCondition keyguard) {
        this.keyguard = keyguard;
    }

    public com.android.server.policy.devicestate.config.CameraCondition getCamera() {
        return this.camera;
    }

    boolean hasCamera() {
        if (this.camera == null) {
            return false;
        }
        return true;
    }

    public void setCamera(com.android.server.policy.devicestate.config.CameraCondition camera) {
        this.camera = camera;
    }

    public com.android.server.policy.devicestate.config.DisplayCondition getDisplay() {
        return this.display;
    }

    boolean hasDisplay() {
        if (this.display == null) {
            return false;
        }
        return true;
    }

    public void setDisplay(com.android.server.policy.devicestate.config.DisplayCondition display) {
        this.display = display;
    }

    static com.android.server.policy.devicestate.config.Conditions read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.policy.devicestate.config.Conditions _instance = new com.android.server.policy.devicestate.config.Conditions();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("lid-switch")) {
                    com.android.server.policy.devicestate.config.LidSwitchCondition _value = com.android.server.policy.devicestate.config.LidSwitchCondition.read(_parser);
                    _instance.setLidSwitch(_value);
                } else if (_tagName.equals(com.android.server.am.IOplusSceneManager.APP_SCENE_SENSOR)) {
                    com.android.server.policy.devicestate.config.SensorCondition _value2 = com.android.server.policy.devicestate.config.SensorCondition.read(_parser);
                    _instance.getSensor().add(_value2);
                } else if (_tagName.equals("keyguard")) {
                    com.android.server.policy.devicestate.config.KeyguardCondition _value3 = com.android.server.policy.devicestate.config.KeyguardCondition.read(_parser);
                    _instance.setKeyguard(_value3);
                } else if (_tagName.equals("camera")) {
                    com.android.server.policy.devicestate.config.CameraCondition _value4 = com.android.server.policy.devicestate.config.CameraCondition.read(_parser);
                    _instance.setCamera(_value4);
                } else if (_tagName.equals("display")) {
                    com.android.server.policy.devicestate.config.DisplayCondition _value5 = com.android.server.policy.devicestate.config.DisplayCondition.read(_parser);
                    _instance.setDisplay(_value5);
                } else {
                    com.android.server.policy.devicestate.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("Conditions is not closed");
        }
        return _instance;
    }
}
