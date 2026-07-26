package android.hardware.boot.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class BoolResult {
    public static final int FALSE = 0;
    public static final int INVALID_SLOT = -1;
    public static final int TRUE = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "FALSE";
        }
        if (o == 1) {
            return "TRUE";
        }
        if (o == -1) {
            return "INVALID_SLOT";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("FALSE");
        if ((o & 1) == 1) {
            list.add("TRUE");
            flipped = 0 | 1;
        }
        if ((o & (-1)) == -1) {
            list.add("INVALID_SLOT");
            flipped |= -1;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
