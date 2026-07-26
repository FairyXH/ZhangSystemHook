package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioMixLatencyClass {
    public static final int LOW = 0;
    public static final int NORMAL = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "LOW";
        }
        if (o == 1) {
            return com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL;
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("LOW");
        if ((o & 1) == 1) {
            list.add(com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL);
            flipped = 0 | 1;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
