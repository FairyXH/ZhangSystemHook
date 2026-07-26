package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioInputFlag {
    public static final int FAST = 1;
    public static final int HW_HOTWORD = 2;
    public static final int MMAP_NOIRQ = 16;
    public static final int NONE = 0;
    public static final int RAW = 4;
    public static final int SYNC = 8;
    public static final int VOIP_TX = 32;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "NONE";
        }
        if (o == 1) {
            return "FAST";
        }
        if (o == 2) {
            return "HW_HOTWORD";
        }
        if (o == 4) {
            return "RAW";
        }
        if (o == 8) {
            return "SYNC";
        }
        if (o == 16) {
            return "MMAP_NOIRQ";
        }
        if (o == 32) {
            return "VOIP_TX";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("NONE");
        if ((o & 1) == 1) {
            list.add("FAST");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("HW_HOTWORD");
            flipped |= 2;
        }
        if ((o & 4) == 4) {
            list.add("RAW");
            flipped |= 4;
        }
        if ((o & 8) == 8) {
            list.add("SYNC");
            flipped |= 8;
        }
        if ((o & 16) == 16) {
            list.add("MMAP_NOIRQ");
            flipped |= 16;
        }
        if ((o & 32) == 32) {
            list.add("VOIP_TX");
            flipped |= 32;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
