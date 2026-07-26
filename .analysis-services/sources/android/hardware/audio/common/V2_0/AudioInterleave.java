package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioInterleave {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "LEFT";
        }
        if (o == 1) {
            return "RIGHT";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("LEFT");
        if ((o & 1) == 1) {
            list.add("RIGHT");
            flipped = 0 | 1;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
