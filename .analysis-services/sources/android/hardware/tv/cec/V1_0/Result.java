package android.hardware.tv.cec.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class Result {
    public static final int FAILURE_BUSY = 5;
    public static final int FAILURE_INVALID_ARGS = 2;
    public static final int FAILURE_INVALID_STATE = 3;
    public static final int FAILURE_NOT_SUPPORTED = 4;
    public static final int FAILURE_UNKNOWN = 1;
    public static final int SUCCESS = 0;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "SUCCESS";
        }
        if (o == 1) {
            return "FAILURE_UNKNOWN";
        }
        if (o == 2) {
            return "FAILURE_INVALID_ARGS";
        }
        if (o == 3) {
            return "FAILURE_INVALID_STATE";
        }
        if (o == 4) {
            return "FAILURE_NOT_SUPPORTED";
        }
        if (o == 5) {
            return "FAILURE_BUSY";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("SUCCESS");
        if ((o & 1) == 1) {
            list.add("FAILURE_UNKNOWN");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("FAILURE_INVALID_ARGS");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("FAILURE_INVALID_STATE");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("FAILURE_NOT_SUPPORTED");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("FAILURE_BUSY");
            flipped |= 5;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
