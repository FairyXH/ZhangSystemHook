package vendor.pixelworks.hardware.display.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class ChipVersion {
    public static final int IRIS2 = 0;
    public static final int IRIS2_PLUS = 1;
    public static final int IRIS3_LITE = 2;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "IRIS2";
        }
        if (o == 1) {
            return "IRIS2_PLUS";
        }
        if (o == 2) {
            return "IRIS3_LITE";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("IRIS2");
        if ((o & 1) == 1) {
            list.add("IRIS2_PLUS");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("IRIS3_LITE");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
