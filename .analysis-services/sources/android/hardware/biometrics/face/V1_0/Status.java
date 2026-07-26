package android.hardware.biometrics.face.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class Status {
    public static final int ILLEGAL_ARGUMENT = 1;
    public static final int INTERNAL_ERROR = 3;
    public static final int NOT_ENROLLED = 4;
    public static final int OK = 0;
    public static final int OPERATION_NOT_SUPPORTED = 2;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "OK";
        }
        if (o == 1) {
            return "ILLEGAL_ARGUMENT";
        }
        if (o == 2) {
            return "OPERATION_NOT_SUPPORTED";
        }
        if (o == 3) {
            return "INTERNAL_ERROR";
        }
        if (o == 4) {
            return "NOT_ENROLLED";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("OK");
        if ((o & 1) == 1) {
            list.add("ILLEGAL_ARGUMENT");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("OPERATION_NOT_SUPPORTED");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("INTERNAL_ERROR");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("NOT_ENROLLED");
            flipped |= 4;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
