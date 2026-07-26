package android.hardware.soundtrigger.V2_3;

/* JADX INFO: loaded from: classes.dex */
public final class ModelParameter {
    public static final int INVALID = -1;
    public static final int THRESHOLD_FACTOR = 0;

    public static final java.lang.String toString(int o) {
        if (o == -1) {
            return "INVALID";
        }
        if (o == 0) {
            return "THRESHOLD_FACTOR";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & (-1)) == -1) {
            list.add("INVALID");
            flipped = 0 | (-1);
        }
        list.add("THRESHOLD_FACTOR");
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
