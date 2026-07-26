package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class DualChannelState {
    public static final int CFGOFF = 5;
    public static final int CFGOFF_PRE = 4;
    public static final int CFGON = 3;
    public static final int CFGON_PRE = 2;
    public static final int PWROFF = 7;
    public static final int PWROFF_PREPARE = 6;
    public static final int PWRON = 1;
    public static final int PWRON_PREPARE = 0;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "PWRON_PREPARE";
        }
        if (o == 1) {
            return "PWRON";
        }
        if (o == 2) {
            return "CFGON_PRE";
        }
        if (o == 3) {
            return "CFGON";
        }
        if (o == 4) {
            return "CFGOFF_PRE";
        }
        if (o == 5) {
            return "CFGOFF";
        }
        if (o == 6) {
            return "PWROFF_PREPARE";
        }
        if (o == 7) {
            return "PWROFF";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("PWRON_PREPARE");
        if ((o & 1) == 1) {
            list.add("PWRON");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("CFGON_PRE");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("CFGON");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("CFGOFF_PRE");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("CFGOFF");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("PWROFF_PREPARE");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("PWROFF");
            flipped |= 7;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
