package vendor.pixelworks.hardware.feature.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class FeatureFlag {
    public static final int ASIC_PLUS = 512;
    public static final int CHIP_TYPE = 255;
    public static final int FPGA = 256;
    public static final int SUPPORT_DUAL = 1;
    public static final int SUPPORT_EMV = 32;
    public static final int SUPPORT_HDR10 = 128;
    public static final int SUPPORT_IMV = 8;
    public static final int SUPPORT_IMV_DUAL = 16;
    public static final int SUPPORT_MEMC = 2;
    public static final int SUPPORT_MEMC_DUAL = 4;
    public static final int SUPPORT_SR = 64;

    public static final java.lang.String toString(int o) {
        if (o == 1) {
            return "SUPPORT_DUAL";
        }
        if (o == 2) {
            return "SUPPORT_MEMC";
        }
        if (o == 4) {
            return "SUPPORT_MEMC_DUAL";
        }
        if (o == 8) {
            return "SUPPORT_IMV";
        }
        if (o == 16) {
            return "SUPPORT_IMV_DUAL";
        }
        if (o == 32) {
            return "SUPPORT_EMV";
        }
        if (o == 64) {
            return "SUPPORT_SR";
        }
        if (o == 128) {
            return "SUPPORT_HDR10";
        }
        if (o == 255) {
            return "CHIP_TYPE";
        }
        if (o == 256) {
            return "FPGA";
        }
        if (o == 512) {
            return "ASIC_PLUS";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 1) == 1) {
            list.add("SUPPORT_DUAL");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("SUPPORT_MEMC");
            flipped |= 2;
        }
        if ((o & 4) == 4) {
            list.add("SUPPORT_MEMC_DUAL");
            flipped |= 4;
        }
        if ((o & 8) == 8) {
            list.add("SUPPORT_IMV");
            flipped |= 8;
        }
        if ((o & 16) == 16) {
            list.add("SUPPORT_IMV_DUAL");
            flipped |= 16;
        }
        if ((o & 32) == 32) {
            list.add("SUPPORT_EMV");
            flipped |= 32;
        }
        if ((o & 64) == 64) {
            list.add("SUPPORT_SR");
            flipped |= 64;
        }
        if ((o & 128) == 128) {
            list.add("SUPPORT_HDR10");
            flipped |= 128;
        }
        if ((o & 255) == 255) {
            list.add("CHIP_TYPE");
            flipped |= 255;
        }
        if ((o & 256) == 256) {
            list.add("FPGA");
            flipped |= 256;
        }
        if ((o & 512) == 512) {
            list.add("ASIC_PLUS");
            flipped |= 512;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
