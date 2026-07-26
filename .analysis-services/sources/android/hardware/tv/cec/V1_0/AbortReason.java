package android.hardware.tv.cec.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class AbortReason {
    public static final int CANNOT_PROVIDE_SOURCE = 2;
    public static final int INVALID_OPERAND = 3;
    public static final int NOT_IN_CORRECT_MODE = 1;
    public static final int REFUSED = 4;
    public static final int UNABLE_TO_DETERMINE = 5;
    public static final int UNRECOGNIZED_MODE = 0;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "UNRECOGNIZED_MODE";
        }
        if (o == 1) {
            return "NOT_IN_CORRECT_MODE";
        }
        if (o == 2) {
            return "CANNOT_PROVIDE_SOURCE";
        }
        if (o == 3) {
            return "INVALID_OPERAND";
        }
        if (o == 4) {
            return "REFUSED";
        }
        if (o == 5) {
            return "UNABLE_TO_DETERMINE";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("UNRECOGNIZED_MODE");
        if ((o & 1) == 1) {
            list.add("NOT_IN_CORRECT_MODE");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("CANNOT_PROVIDE_SOURCE");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("INVALID_OPERAND");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("REFUSED");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("UNABLE_TO_DETERMINE");
            flipped |= 5;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
