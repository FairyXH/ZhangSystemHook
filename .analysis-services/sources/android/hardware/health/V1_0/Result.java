package android.hardware.health.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class Result {
    public static final int NOT_SUPPORTED = 1;
    public static final int SUCCESS = 0;
    public static final int UNKNOWN = 2;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "SUCCESS";
        }
        if (o == 1) {
            return "NOT_SUPPORTED";
        }
        if (o == 2) {
            return "UNKNOWN";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("SUCCESS");
        if ((o & 1) == 1) {
            list.add("NOT_SUPPORTED");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("UNKNOWN");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
