package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class HdrFormalType {
    public static final int HDR_FORMAL_HDR = 2;
    public static final int HDR_FORMAL_HDR_MEMC = 12;
    public static final int HDR_FORMAL_HDR_MEMC_DUAL2 = 42;
    public static final int HDR_FORMAL_HDR_SDR = 1;
    public static final int HDR_FORMAL_MEMC = 10;
    public static final int HDR_FORMAL_MEMC_DUAL2 = 40;
    public static final int HDR_FORMAL_NONE = 0;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "HDR_FORMAL_NONE";
        }
        if (o == 1) {
            return "HDR_FORMAL_HDR_SDR";
        }
        if (o == 2) {
            return "HDR_FORMAL_HDR";
        }
        if (o == 10) {
            return "HDR_FORMAL_MEMC";
        }
        if (o == 12) {
            return "HDR_FORMAL_HDR_MEMC";
        }
        if (o == 40) {
            return "HDR_FORMAL_MEMC_DUAL2";
        }
        if (o == 42) {
            return "HDR_FORMAL_HDR_MEMC_DUAL2";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("HDR_FORMAL_NONE");
        if ((o & 1) == 1) {
            list.add("HDR_FORMAL_HDR_SDR");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("HDR_FORMAL_HDR");
            flipped |= 2;
        }
        if ((o & 10) == 10) {
            list.add("HDR_FORMAL_MEMC");
            flipped |= 10;
        }
        if ((o & 12) == 12) {
            list.add("HDR_FORMAL_HDR_MEMC");
            flipped |= 12;
        }
        if ((o & 40) == 40) {
            list.add("HDR_FORMAL_MEMC_DUAL2");
            flipped |= 40;
        }
        if ((o & 42) == 42) {
            list.add("HDR_FORMAL_HDR_MEMC_DUAL2");
            flipped |= 42;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
