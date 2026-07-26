package com.android.server.display.util;

/* JADX INFO: loaded from: classes2.dex */
public class OplusDisplayPanelFeatureHelper {
    public static final int FINGER_LAYER_HIDE = 0;
    public static final int FINGER_LAYER_SHOW = 1;
    public static final int MTK_FINGER_LAYER_HIDE = 1;
    public static final int MTK_FINGER_LAYER_SHOW = 0;
    public static final int OMMDPAOD = 7;
    public static final int OMMDPFFL = 6;
    public static final int OMMDPMAX_BRIGHTNESS = 8;
    public static final int OMMDPPANEL_ID = 5;
    public static final int OMMDPPANEL_INFO = 9;
    public static final int OMMDPPOWER_VDDI = 1;
    public static final int OMMDPPOWER_VDDR = 2;
    public static final int OMMDPPOWER_VGATE = 3;
    public static final int OMMDPSEED = 4;
    public static final int OMMDP_AUDIO_READY = 15;
    public static final int OMMDP_AUTO_BRIGHTNESS = 245;
    public static final int OMMDP_CABC_STATUS = 184;
    public static final int OMMDP_CCD_CHECK = 10;
    public static final int OMMDP_DCC_MONTH = 220;
    public static final int OMMDP_DIMLAYER_BL_EN = 23;
    public static final int OMMDP_DIMLAY_HBM = 22;
    public static final int OMMDP_DIM_ALPHA = 13;
    public static final int OMMDP_DIM_DC_ALPHA = 14;
    public static final int OMMDP_DMR_GET = 244;
    public static final int OMMDP_DMR_SET = 241;
    public static final int OMMDP_DOC = 27;
    public static final int OMMDP_DUMP_INFO = 16;
    public static final int OMMDP_FPPRESS = 28;
    public static final int OMMDP_HBM = 12;
    public static final int OMMDP_NORMAL_MAXBRIGHTNESS = 247;
    public static final int OMMDP_OPLUS_SHUTDOWN_FLAG = 210;
    public static final int OMMDP_PANEL_BLANK = 24;
    public static final int OMMDP_PANEL_DSC = 17;
    public static final int OMMDP_PANEL_REG = 21;
    public static final int OMMDP_POWER_STATUS = 18;
    public static final int OMMDP_PQ_TRIGGER = 68;
    public static final int OMMDP_PWM_PULSE = 201;
    public static final int OMMDP_PWM_TURBO = 199;
    public static final int OMMDP_REGULATOR_CONTROL = 19;
    public static final int OMMDP_ROUND_CORNER = 26;
    public static final int OMMDP_SAU_CLOSEBL = 20;
    public static final int OMMDP_SERIAL_NUMBER = 11;
    public static final int OMMDP_SPR = 25;
    public static final int OMMDP_UIR = 226;
    private static final java.lang.String TAG = "OplusDisplayPanelFeature";

    public static java.util.ArrayList<java.lang.Integer> getDisplayPanelFeatureValue(int featureID) {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.AidlDisplayPanelFeature.getDisplayPanelFeatureValue(featureID);
        }
        if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.HidlDisplayPanelFeature.getDisplayPanelFeatureValue(featureID);
        }
        return null;
    }

    public static java.lang.String getDisplayPanelFeatureValueAsString(int featureID) {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.AidlDisplayPanelFeature.getDisplayPanelFeatureValueAsString(featureID);
        }
        if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.HidlDisplayPanelFeature.getDisplayPanelFeatureValueAsString(featureID);
        }
        return null;
    }

    public static int[] getDisplayPanelFeatureValueAsIntArray(int featureID) {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.AidlDisplayPanelFeature.getDisplayPanelFeatureValueAsIntArray(featureID);
        }
        if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.HidlDisplayPanelFeature.getDisplayPanelFeatureValueAsIntArray(featureID);
        }
        return null;
    }

    public static int getDisplayPanelFeatureValueAsInt(int featureID) {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.AidlDisplayPanelFeature.getDisplayPanelFeatureValueAsInt(featureID);
        }
        if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.HidlDisplayPanelFeature.getDisplayPanelFeatureValueAsInt(featureID);
        }
        return -1;
    }

    public static boolean isFODHwDimlayer() {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.AidlDisplayPanelFeature.isFODHwDimlayer();
        }
        if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.HidlDisplayPanelFeature.isFODHwDimlayer();
        }
        return false;
    }

    public static void setDisplayPanelFeatureValue(int featureID, int mode) {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            com.android.server.display.util.AidlDisplayPanelFeature.setDisplayPanelFeatureValue(featureID, mode);
        } else if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            com.android.server.display.util.HidlDisplayPanelFeature.setDisplayPanelFeatureValue(featureID, mode);
        }
    }

    public static void setDisplayPanelFeatureValueArray(int featureID, int[] modes) {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            com.android.server.display.util.AidlDisplayPanelFeature.setDisplayPanelFeatureValueArray(featureID, modes);
        } else if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            com.android.server.display.util.HidlDisplayPanelFeature.setDisplayPanelFeatureValueArray(featureID, modes);
        }
    }

    public static int setDisplayPanelFeatureValueForMtk(int featureID, int mode) {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.AidlDisplayPanelFeature.setDisplayPanelFeatureValueForMtk(featureID, mode);
        }
        if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.HidlDisplayPanelFeature.setDisplayPanelFeatureValueForMtk(featureID, mode);
        }
        return -1;
    }

    public static java.util.ArrayList<java.lang.String> getDisplayPanelInfo(int featureID) {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.AidlDisplayPanelFeature.getDisplayPanelInfo(featureID);
        }
        if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.HidlDisplayPanelFeature.getDisplayPanelInfo(featureID);
        }
        return null;
    }

    public static java.lang.String getDisplayPanelInfoAsString(int featureID) {
        if (com.android.server.display.util.AidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.AidlDisplayPanelFeature.getDisplayPanelInfoAsString(featureID);
        }
        if (com.android.server.display.util.HidlDisplayPanelFeature.isAvailable()) {
            return com.android.server.display.util.HidlDisplayPanelFeature.getDisplayPanelInfoAsString(featureID);
        }
        return null;
    }
}
