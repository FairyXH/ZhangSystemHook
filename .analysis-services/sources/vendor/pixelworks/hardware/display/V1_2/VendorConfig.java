package vendor.pixelworks.hardware.display.V1_2;

/* JADX INFO: loaded from: classes4.dex */
public final class VendorConfig {
    public static final int CALIBRATION = 263;
    public static final int FRAME_RATE_CHANGED = 269;
    public static final int GET_SECONDARY_DISPLAY = 268;
    public static final int SET_CALI_PATTERN = 265;
    public static final int SET_FRAMEBUFFER_RESOLUTION = 271;
    public static final int SET_GAME_MODE = 261;
    public static final int SET_HDR_FORMAL = 258;
    public static final int SET_HDR_SETTING = 260;
    public static final int SET_LOW_LATENCY = 270;
    public static final int SET_MEMC_SETTING = 266;
    public static final int SET_N2M_ENABLE = 262;
    public static final int SET_ORIENTATION = 257;
    public static final int SET_PANEL_INFO = 1024;
    public static final int SET_SDR2HDR_SETTING = 267;
    public static final int SET_SERVICE_DEBUG = 259;
    public static final int SET_WCG_GAMUT = 264;
    public static final int SR_ENABLE = 273;
    public static final int START_TRANSITION = 256;
    public static final int TYPE_MAX = 268;
    public static final int TYPE_MAX_V1_1 = 272;
    public static final int TYPE_MAX_V1_2 = 1279;
    public static final int TYPE_MIN_V1_2 = 1024;

    public static final java.lang.String toString(int o) {
        if (o == 256) {
            return "START_TRANSITION";
        }
        if (o == 257) {
            return "SET_ORIENTATION";
        }
        if (o == 258) {
            return "SET_HDR_FORMAL";
        }
        if (o == 259) {
            return "SET_SERVICE_DEBUG";
        }
        if (o == 260) {
            return "SET_HDR_SETTING";
        }
        if (o == 261) {
            return "SET_GAME_MODE";
        }
        if (o == 262) {
            return "SET_N2M_ENABLE";
        }
        if (o == 263) {
            return "CALIBRATION";
        }
        if (o == 264) {
            return "SET_WCG_GAMUT";
        }
        if (o == 265) {
            return "SET_CALI_PATTERN";
        }
        if (o == 266) {
            return "SET_MEMC_SETTING";
        }
        if (o == 267) {
            return "SET_SDR2HDR_SETTING";
        }
        if (o == 268) {
            return "TYPE_MAX";
        }
        if (o == 268) {
            return "GET_SECONDARY_DISPLAY";
        }
        if (o == 269) {
            return "FRAME_RATE_CHANGED";
        }
        if (o == 270) {
            return "SET_LOW_LATENCY";
        }
        if (o == 271) {
            return "SET_FRAMEBUFFER_RESOLUTION";
        }
        if (o == 272) {
            return "TYPE_MAX_V1_1";
        }
        if (o == 273) {
            return "SR_ENABLE";
        }
        if (o == 1024) {
            return "TYPE_MIN_V1_2";
        }
        if (o == 1024) {
            return "SET_PANEL_INFO";
        }
        if (o == 1279) {
            return "TYPE_MAX_V1_2";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 256) == 256) {
            list.add("START_TRANSITION");
            flipped = 0 | 256;
        }
        if ((o & 257) == 257) {
            list.add("SET_ORIENTATION");
            flipped |= 257;
        }
        if ((o & 258) == 258) {
            list.add("SET_HDR_FORMAL");
            flipped |= 258;
        }
        if ((o & 259) == 259) {
            list.add("SET_SERVICE_DEBUG");
            flipped |= 259;
        }
        if ((o & 260) == 260) {
            list.add("SET_HDR_SETTING");
            flipped |= 260;
        }
        if ((o & 261) == 261) {
            list.add("SET_GAME_MODE");
            flipped |= 261;
        }
        if ((o & 262) == 262) {
            list.add("SET_N2M_ENABLE");
            flipped |= 262;
        }
        if ((o & 263) == 263) {
            list.add("CALIBRATION");
            flipped |= 263;
        }
        if ((o & 264) == 264) {
            list.add("SET_WCG_GAMUT");
            flipped |= 264;
        }
        if ((o & 265) == 265) {
            list.add("SET_CALI_PATTERN");
            flipped |= 265;
        }
        if ((o & 266) == 266) {
            list.add("SET_MEMC_SETTING");
            flipped |= 266;
        }
        if ((o & 267) == 267) {
            list.add("SET_SDR2HDR_SETTING");
            flipped |= 267;
        }
        if ((o & 268) == 268) {
            list.add("TYPE_MAX");
            flipped |= 268;
        }
        if ((o & 268) == 268) {
            list.add("GET_SECONDARY_DISPLAY");
            flipped |= 268;
        }
        if ((o & 269) == 269) {
            list.add("FRAME_RATE_CHANGED");
            flipped |= 269;
        }
        if ((o & 270) == 270) {
            list.add("SET_LOW_LATENCY");
            flipped |= 270;
        }
        if ((o & 271) == 271) {
            list.add("SET_FRAMEBUFFER_RESOLUTION");
            flipped |= 271;
        }
        if ((o & 272) == 272) {
            list.add("TYPE_MAX_V1_1");
            flipped |= 272;
        }
        if ((o & 273) == 273) {
            list.add("SR_ENABLE");
            flipped |= 273;
        }
        if ((o & 1024) == 1024) {
            list.add("TYPE_MIN_V1_2");
            flipped |= 1024;
        }
        if ((o & 1024) == 1024) {
            list.add("SET_PANEL_INFO");
            flipped |= 1024;
        }
        if ((o & 1279) == 1279) {
            list.add("TYPE_MAX_V1_2");
            flipped |= 1279;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
