package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioPortType {
    public static final int DEVICE = 1;
    public static final int MIX = 2;
    public static final int NONE = 0;
    public static final int SESSION = 3;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "NONE";
        }
        if (o == 1) {
            return "DEVICE";
        }
        if (o == 2) {
            return "MIX";
        }
        if (o == 3) {
            return "SESSION";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("NONE");
        if ((o & 1) == 1) {
            list.add("DEVICE");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("MIX");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("SESSION");
            flipped |= 3;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
