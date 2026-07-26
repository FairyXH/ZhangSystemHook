package vendor.pixelworks.hardware.feature.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class ChipFeature {
    public static final int IRIS2 = 0;
    public static final int IRIS2_PLUS = 1;
    public static final int IRIS3 = 2;
    public static final int IRIS5 = 3;
    public static final int IRIS5_DUAL = 6;
    public static final int IRIS6 = 4;
    public static final int IRIS7 = 7;
    public static final int IRIS7_DUAL = 8;
    public static final int IRISSOFT = 5;
    public static final int IRIS_FPGA = 30;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "IRIS2";
        }
        if (o == 1) {
            return "IRIS2_PLUS";
        }
        if (o == 2) {
            return "IRIS3";
        }
        if (o == 3) {
            return "IRIS5";
        }
        if (o == 4) {
            return "IRIS6";
        }
        if (o == 5) {
            return "IRISSOFT";
        }
        if (o == 6) {
            return "IRIS5_DUAL";
        }
        if (o == 7) {
            return "IRIS7";
        }
        if (o == 8) {
            return "IRIS7_DUAL";
        }
        if (o == 30) {
            return "IRIS_FPGA";
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
            list.add("IRIS3");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("IRIS5");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("IRIS6");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("IRISSOFT");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("IRIS5_DUAL");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("IRIS7");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("IRIS7_DUAL");
            flipped |= 8;
        }
        if ((o & 30) == 30) {
            list.add("IRIS_FPGA");
            flipped |= 30;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
