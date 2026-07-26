package android.hardware.usb.V1_2;

/* JADX INFO: loaded from: classes.dex */
public final class ContaminantDetectionStatus {
    public static final int DETECTED = 3;
    public static final int DISABLED = 1;
    public static final int NOT_DETECTED = 2;
    public static final int NOT_SUPPORTED = 0;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "NOT_SUPPORTED";
        }
        if (o == 1) {
            return "DISABLED";
        }
        if (o == 2) {
            return "NOT_DETECTED";
        }
        if (o == 3) {
            return "DETECTED";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("NOT_SUPPORTED");
        if ((o & 1) == 1) {
            list.add("DISABLED");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("NOT_DETECTED");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("DETECTED");
            flipped |= 3;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
