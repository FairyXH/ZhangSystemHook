package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class DualChannelOsdStatus {
    public static final int GET_STATE = 0;
    public static final int IS_OVERFLOW = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "GET_STATE";
        }
        if (o == 1) {
            return "IS_OVERFLOW";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("GET_STATE");
        if ((o & 1) == 1) {
            list.add("IS_OVERFLOW");
            flipped = 0 | 1;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
