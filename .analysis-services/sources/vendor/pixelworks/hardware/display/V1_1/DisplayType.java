package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class DisplayType {
    public static final long DISPLAY_BUILTIN_2 = 3;
    public static final long DISPLAY_EXTERNAL = 1;
    public static final long DISPLAY_EXTERNAL_2 = 4;
    public static final long DISPLAY_MAX = 5;
    public static final long DISPLAY_PLUGGABLE = 1;
    public static final long DISPLAY_PRIMARY = 0;
    public static final long DISPLAY_SECONDARY = 3;
    public static final long DISPLAY_VIRTUAL = 2;

    public static final java.lang.String toString(long o) {
        if (o == 0) {
            return "DISPLAY_PRIMARY";
        }
        if (o == 1) {
            return "DISPLAY_EXTERNAL";
        }
        if (o == 1) {
            return "DISPLAY_PLUGGABLE";
        }
        if (o == 2) {
            return "DISPLAY_VIRTUAL";
        }
        if (o == 3) {
            return "DISPLAY_BUILTIN_2";
        }
        if (o == 3) {
            return "DISPLAY_SECONDARY";
        }
        if (o == 4) {
            return "DISPLAY_EXTERNAL_2";
        }
        if (o == 5) {
            return "DISPLAY_MAX";
        }
        return "0x" + java.lang.Long.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(long o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        long flipped = 0;
        list.add("DISPLAY_PRIMARY");
        if ((o & 1) == 1) {
            list.add("DISPLAY_EXTERNAL");
            flipped = 0 | 1;
        }
        if ((o & 1) == 1) {
            list.add("DISPLAY_PLUGGABLE");
            flipped |= 1;
        }
        if ((o & 2) == 2) {
            list.add("DISPLAY_VIRTUAL");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("DISPLAY_BUILTIN_2");
            flipped |= 3;
        }
        if ((o & 3) == 3) {
            list.add("DISPLAY_SECONDARY");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("DISPLAY_EXTERNAL_2");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("DISPLAY_MAX");
            flipped |= 5;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Long.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
