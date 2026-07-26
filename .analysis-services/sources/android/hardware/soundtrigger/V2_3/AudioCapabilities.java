package android.hardware.soundtrigger.V2_3;

/* JADX INFO: loaded from: classes.dex */
public final class AudioCapabilities {
    public static final int ECHO_CANCELLATION = 1;
    public static final int NOISE_SUPPRESSION = 2;

    public static final java.lang.String toString(int o) {
        if (o == 1) {
            return "ECHO_CANCELLATION";
        }
        if (o == 2) {
            return "NOISE_SUPPRESSION";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 1) == 1) {
            list.add("ECHO_CANCELLATION");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("NOISE_SUPPRESSION");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
