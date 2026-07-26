package vendor.qti.hardware.servicetracker.V1_2;

/* JADX INFO: loaded from: classes4.dex */
public final class Status {
    public static final int CALLBACK_DIED = 6;
    public static final int CALLBACK_EXIST = 5;
    public static final int ERROR_INVALID_ARGS = 2;
    public static final int ERROR_NOT_AVAILABLE = 1;
    public static final int ERROR_NOT_SUPPORTED = 3;
    public static final int ERROR_UNKNOWN = 4;
    public static final int SUCCESS = 0;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "SUCCESS";
        }
        if (o == 1) {
            return "ERROR_NOT_AVAILABLE";
        }
        if (o == 2) {
            return "ERROR_INVALID_ARGS";
        }
        if (o == 3) {
            return "ERROR_NOT_SUPPORTED";
        }
        if (o == 4) {
            return "ERROR_UNKNOWN";
        }
        if (o == 5) {
            return "CALLBACK_EXIST";
        }
        if (o == 6) {
            return "CALLBACK_DIED";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("SUCCESS");
        if ((o & 1) == 1) {
            list.add("ERROR_NOT_AVAILABLE");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("ERROR_INVALID_ARGS");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("ERROR_NOT_SUPPORTED");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("ERROR_UNKNOWN");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("CALLBACK_EXIST");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("CALLBACK_DIED");
            flipped |= 6;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
