package vendor.oplus.hardware.felica.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class FelicaStatus {
    public static final byte FAILED = 1;
    public static final byte IOERROR = 3;
    public static final byte SUCCESS = 0;
    public static final byte UNSUPPORTED_OPERATION = 2;

    public static final java.lang.String toString(byte o) {
        if (o == 0) {
            return "SUCCESS";
        }
        if (o == 1) {
            return "FAILED";
        }
        if (o == 2) {
            return "UNSUPPORTED_OPERATION";
        }
        if (o == 3) {
            return "IOERROR";
        }
        return "0x" + java.lang.Integer.toHexString(java.lang.Byte.toUnsignedInt(o));
    }

    public static final java.lang.String dumpBitfield(byte o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        byte flipped = 0;
        list.add("SUCCESS");
        if ((o & 1) == 1) {
            list.add("FAILED");
            flipped = (byte) (0 | 1);
        }
        if ((o & 2) == 2) {
            list.add("UNSUPPORTED_OPERATION");
            flipped = (byte) (flipped | 2);
        }
        if ((o & 3) == 3) {
            list.add("IOERROR");
            flipped = (byte) (flipped | 3);
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString(java.lang.Byte.toUnsignedInt((byte) ((~flipped) & o))));
        }
        return java.lang.String.join(" | ", list);
    }
}
