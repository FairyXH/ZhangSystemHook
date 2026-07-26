package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class Result {
    public static final int INTERNAL_ERROR = 2;
    public static final int INVALID_ARGUMENTS = 3;
    public static final int INVALID_STATE = 4;
    public static final int NOT_SUPPORTED = 5;
    public static final int OK = 0;
    public static final int TIMEOUT = 6;
    public static final int UNKNOWN_ERROR = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "OK";
        }
        if (o == 1) {
            return "UNKNOWN_ERROR";
        }
        if (o == 2) {
            return "INTERNAL_ERROR";
        }
        if (o == 3) {
            return "INVALID_ARGUMENTS";
        }
        if (o == 4) {
            return "INVALID_STATE";
        }
        if (o == 5) {
            return "NOT_SUPPORTED";
        }
        if (o == 6) {
            return "TIMEOUT";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("OK");
        if ((o & 1) == 1) {
            list.add("UNKNOWN_ERROR");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("INTERNAL_ERROR");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("INVALID_ARGUMENTS");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("INVALID_STATE");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("NOT_SUPPORTED");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("TIMEOUT");
            flipped |= 6;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
