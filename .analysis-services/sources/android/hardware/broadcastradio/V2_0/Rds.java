package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class Rds {
    public static final byte RBDS = 2;
    public static final byte RDS = 1;

    public static final java.lang.String toString(byte o) {
        if (o == 1) {
            return "RDS";
        }
        if (o == 2) {
            return "RBDS";
        }
        return "0x" + java.lang.Integer.toHexString(java.lang.Byte.toUnsignedInt(o));
    }

    public static final java.lang.String dumpBitfield(byte o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        byte flipped = 0;
        if ((o & 1) == 1) {
            list.add("RDS");
            flipped = (byte) (0 | 1);
        }
        if ((o & 2) == 2) {
            list.add("RBDS");
            flipped = (byte) (flipped | 2);
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString(java.lang.Byte.toUnsignedInt((byte) ((~flipped) & o))));
        }
        return java.lang.String.join(" | ", list);
    }
}
