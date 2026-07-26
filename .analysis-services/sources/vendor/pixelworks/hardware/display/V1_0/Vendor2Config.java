package vendor.pixelworks.hardware.display.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class Vendor2Config {
    public static final int BYPASS_MODE = 520;
    public static final int CALIBRATION = 518;
    public static final int CM_COLOR_GAMUT = 516;
    public static final int CM_COLOR_TEMP_MODE = 515;
    public static final int COLOR_TEMP_VALUE = 517;
    public static final int DATA_GAIN = 512;
    public static final int DISPLAY_BRIGHTNESS = 514;
    public static final int FORCE_LUT = 513;
    public static final int PQ_TARGET = 519;
    public static final int TYPE_MAX = 521;

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
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
