package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioPortRole {
    public static final int NONE = 0;
    public static final int SINK = 2;
    public static final int SOURCE = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "NONE";
        }
        if (o == 1) {
            return "SOURCE";
        }
        if (o == 2) {
            return "SINK";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("NONE");
        if ((o & 1) == 1) {
            list.add("SOURCE");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("SINK");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
