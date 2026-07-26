package android.hardware.configstore.V1_1;

/* JADX INFO: loaded from: classes.dex */
public final class DisplayOrientation {
    public static final byte ORIENTATION_0 = 0;
    public static final byte ORIENTATION_180 = 2;
    public static final byte ORIENTATION_270 = 3;
    public static final byte ORIENTATION_90 = 1;

    public static final java.lang.String toString(byte o) {
        if (o == 0) {
            return "ORIENTATION_0";
        }
        if (o == 1) {
            return "ORIENTATION_90";
        }
        if (o == 2) {
            return "ORIENTATION_180";
        }
        if (o == 3) {
            return "ORIENTATION_270";
        }
        return "0x" + java.lang.Integer.toHexString(java.lang.Byte.toUnsignedInt(o));
    }

    public static final java.lang.String dumpBitfield(byte o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        byte flipped = 0;
        list.add("ORIENTATION_0");
        if ((o & 1) == 1) {
            list.add("ORIENTATION_90");
            flipped = (byte) (0 | 1);
        }
        if ((o & 2) == 2) {
            list.add("ORIENTATION_180");
            flipped = (byte) (flipped | 2);
        }
        if ((o & 3) == 3) {
            list.add("ORIENTATION_270");
            flipped = (byte) (flipped | 3);
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString(java.lang.Byte.toUnsignedInt((byte) ((~flipped) & o))));
        }
        return java.lang.String.join(" | ", list);
    }
}
