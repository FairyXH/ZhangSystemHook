package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class FixedChannelCount {
    public static final int FCC_2 = 2;
    public static final int FCC_8 = 8;

    public static final java.lang.String toString(int o) {
        if (o == 2) {
            return "FCC_2";
        }
        if (o == 8) {
            return "FCC_8";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 2) == 2) {
            list.add("FCC_2");
            flipped = 0 | 2;
        }
        if ((o & 8) == 8) {
            list.add("FCC_8");
            flipped |= 8;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
