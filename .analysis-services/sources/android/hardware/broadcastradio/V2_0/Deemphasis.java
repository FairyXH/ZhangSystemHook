package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class Deemphasis {
    public static final byte D50 = 1;
    public static final byte D75 = 2;

    public static final java.lang.String toString(byte o) {
        if (o == 1) {
            return "D50";
        }
        if (o == 2) {
            return "D75";
        }
        return "0x" + java.lang.Integer.toHexString(java.lang.Byte.toUnsignedInt(o));
    }

    public static final java.lang.String dumpBitfield(byte o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        byte flipped = 0;
        if ((o & 1) == 1) {
            list.add("D50");
            flipped = (byte) (0 | 1);
        }
        if ((o & 2) == 2) {
            list.add("D75");
            flipped = (byte) (flipped | 2);
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString(java.lang.Byte.toUnsignedInt((byte) ((~flipped) & o))));
        }
        return java.lang.String.join(" | ", list);
    }
}
