package vendor.pixelworks.hardware.display.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class AppFilter {
    public static final int BLACK = 1;
    public static final int NORMAL = 0;
    public static final int WHITE = 2;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL;
        }
        if (o == 1) {
            return "BLACK";
        }
        if (o == 2) {
            return "WHITE";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add(com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL);
        if ((o & 1) == 1) {
            list.add("BLACK");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("WHITE");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
