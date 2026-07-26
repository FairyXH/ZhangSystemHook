package android.hardware.usb.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class Status {
    public static final int ERROR = 1;
    public static final int INVALID_ARGUMENT = 2;
    public static final int SUCCESS = 0;
    public static final int UNRECOGNIZED_ROLE = 3;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "SUCCESS";
        }
        if (o == 1) {
            return "ERROR";
        }
        if (o == 2) {
            return "INVALID_ARGUMENT";
        }
        if (o == 3) {
            return "UNRECOGNIZED_ROLE";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("SUCCESS");
        if ((o & 1) == 1) {
            list.add("ERROR");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("INVALID_ARGUMENT");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("UNRECOGNIZED_ROLE");
            flipped |= 3;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
