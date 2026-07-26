package android.hardware.tv.cec.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class MaxLength {
    public static final int MESSAGE_BODY = 15;

    public static final java.lang.String toString(int o) {
        if (o == 15) {
            return "MESSAGE_BODY";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 15) == 15) {
            list.add("MESSAGE_BODY");
            flipped = 0 | 15;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
