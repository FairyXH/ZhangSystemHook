package vendor.oplus.hardware.cwb.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class Error {
    public static final int INPUT_ERROR = 2;
    public static final int NONE = 0;
    public static final int RESOUCE_BUSY = 1;
    public static final int SCREEN_STAT_OFF = 3;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "NONE";
        }
        if (o == 1) {
            return "RESOUCE_BUSY";
        }
        if (o == 2) {
            return "INPUT_ERROR";
        }
        if (o == 3) {
            return "SCREEN_STAT_OFF";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("NONE");
        if ((o & 1) == 1) {
            list.add("RESOUCE_BUSY");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("INPUT_ERROR");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("SCREEN_STAT_OFF");
            flipped |= 3;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
