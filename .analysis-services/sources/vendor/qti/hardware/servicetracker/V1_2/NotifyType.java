package vendor.qti.hardware.servicetracker.V1_2;

/* JADX INFO: loaded from: classes4.dex */
public final class NotifyType {
    public static final int BOTH = 2;
    public static final int EARLY = 0;
    public static final int LATE = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "EARLY";
        }
        if (o == 1) {
            return "LATE";
        }
        if (o == 2) {
            return "BOTH";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("EARLY");
        if ((o & 1) == 1) {
            list.add("LATE");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("BOTH");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
