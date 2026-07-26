package android.hardware.soundtrigger.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class SoundModelType {
    public static final int GENERIC = 1;
    public static final int KEYPHRASE = 0;
    public static final int UNKNOWN = -1;

    public static final java.lang.String toString(int o) {
        if (o == -1) {
            return "UNKNOWN";
        }
        if (o == 0) {
            return "KEYPHRASE";
        }
        if (o == 1) {
            return "GENERIC";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & (-1)) == -1) {
            list.add("UNKNOWN");
            flipped = 0 | (-1);
        }
        list.add("KEYPHRASE");
        if ((o & 1) == 1) {
            list.add("GENERIC");
            flipped |= 1;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
