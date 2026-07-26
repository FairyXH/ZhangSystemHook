package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class Constants {
    public static final int ANTENNA_DISCONNECTED_TIMEOUT_MS = 100;
    public static final int INVALID_IMAGE = 0;
    public static final int LIST_COMPLETE_TIMEOUT_MS = 300000;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "INVALID_IMAGE";
        }
        if (o == 100) {
            return "ANTENNA_DISCONNECTED_TIMEOUT_MS";
        }
        if (o == 300000) {
            return "LIST_COMPLETE_TIMEOUT_MS";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("INVALID_IMAGE");
        if ((o & 100) == 100) {
            list.add("ANTENNA_DISCONNECTED_TIMEOUT_MS");
            flipped = 0 | 100;
        }
        if ((o & 300000) == 300000) {
            list.add("LIST_COMPLETE_TIMEOUT_MS");
            flipped |= 300000;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
