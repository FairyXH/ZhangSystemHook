package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class Vendor2Config {
    public static final int BRIGHTNESS_BLEND_CURVE = 525;
    public static final int BYPASS_MODE = 520;
    public static final int CALIBRATION = 518;
    public static final int CM_COLOR_GAMUT = 516;
    public static final int CM_COLOR_TEMP_MODE = 515;
    public static final int CM_RATIO_SET = 526;
    public static final int COLOR_TEMP_VALUE = 517;
    public static final int DATA_GAIN = 512;
    public static final int DISPLAY_BRIGHTNESS = 514;
    public static final int FORCE_LUT = 513;
    public static final int GAMUT_BLEND_GAIN = 529;
    public static final int PANEL_APL_VALUE = 527;
    public static final int PANEL_OEM_ID = 524;
    public static final int PANEL_POWER_MODE = 522;
    public static final int PANEL_REFRESH_RATE = 521;
    public static final int PQ_TARGET = 519;
    public static final int SET_FEATURE = 528;
    public static final int TYPE_MAX = 521;
    public static final int TYPE_MAX_V1_1 = 530;
    public static final int WHITE_POINT_SHIFT = 523;

    public static final java.lang.String toString(int o) {
        if (o == 512) {
            return "DATA_GAIN";
        }
        if (o == 513) {
            return "FORCE_LUT";
        }
        if (o == 514) {
            return "DISPLAY_BRIGHTNESS";
        }
        if (o == 515) {
            return "CM_COLOR_TEMP_MODE";
        }
        if (o == 516) {
            return "CM_COLOR_GAMUT";
        }
        if (o == 517) {
            return "COLOR_TEMP_VALUE";
        }
        if (o == 518) {
            return "CALIBRATION";
        }
        if (o == 519) {
            return "PQ_TARGET";
        }
        if (o == 520) {
            return "BYPASS_MODE";
        }
        if (o == 521) {
            return "TYPE_MAX";
        }
        if (o == 521) {
            return "PANEL_REFRESH_RATE";
        }
        if (o == 522) {
            return "PANEL_POWER_MODE";
        }
        if (o == 523) {
            return "WHITE_POINT_SHIFT";
        }
        if (o == 524) {
            return "PANEL_OEM_ID";
        }
        if (o == 525) {
            return "BRIGHTNESS_BLEND_CURVE";
        }
        if (o == 526) {
            return "CM_RATIO_SET";
        }
        if (o == 527) {
            return "PANEL_APL_VALUE";
        }
        if (o == 528) {
            return "SET_FEATURE";
        }
        if (o == 529) {
            return "GAMUT_BLEND_GAIN";
        }
        if (o == 530) {
            return "TYPE_MAX_V1_1";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 512) == 512) {
            list.add("DATA_GAIN");
            flipped = 0 | 512;
        }
        if ((o & 513) == 513) {
            list.add("FORCE_LUT");
            flipped |= 513;
        }
        if ((o & 514) == 514) {
            list.add("DISPLAY_BRIGHTNESS");
            flipped |= 514;
        }
        if ((o & 515) == 515) {
            list.add("CM_COLOR_TEMP_MODE");
            flipped |= 515;
        }
        if ((o & 516) == 516) {
            list.add("CM_COLOR_GAMUT");
            flipped |= 516;
        }
        if ((o & 517) == 517) {
            list.add("COLOR_TEMP_VALUE");
            flipped |= 517;
        }
        if ((o & 518) == 518) {
            list.add("CALIBRATION");
            flipped |= 518;
        }
        if ((o & 519) == 519) {
            list.add("PQ_TARGET");
            flipped |= 519;
        }
        if ((o & 520) == 520) {
            list.add("BYPASS_MODE");
            flipped |= 520;
        }
        if ((o & 521) == 521) {
            list.add("TYPE_MAX");
            flipped |= 521;
        }
        if ((o & 521) == 521) {
            list.add("PANEL_REFRESH_RATE");
            flipped |= 521;
        }
        if ((o & 522) == 522) {
            list.add("PANEL_POWER_MODE");
            flipped |= 522;
        }
        if ((o & 523) == 523) {
            list.add("WHITE_POINT_SHIFT");
            flipped |= 523;
        }
        if ((o & 524) == 524) {
            list.add("PANEL_OEM_ID");
            flipped |= 524;
        }
        if ((o & 525) == 525) {
            list.add("BRIGHTNESS_BLEND_CURVE");
            flipped |= 525;
        }
        if ((o & 526) == 526) {
            list.add("CM_RATIO_SET");
            flipped |= 526;
        }
        if ((o & 527) == 527) {
            list.add("PANEL_APL_VALUE");
            flipped |= 527;
        }
        if ((o & 528) == 528) {
            list.add("SET_FEATURE");
            flipped |= 528;
        }
        if ((o & 529) == 529) {
            list.add("GAMUT_BLEND_GAIN");
            flipped |= 529;
        }
        if ((o & 530) == 530) {
            list.add("TYPE_MAX_V1_1");
            flipped |= 530;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
