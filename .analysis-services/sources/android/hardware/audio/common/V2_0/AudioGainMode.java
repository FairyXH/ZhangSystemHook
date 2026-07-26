package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioGainMode {
    public static final int CHANNELS = 2;
    public static final int JOINT = 1;
    public static final int RAMP = 4;

    public static final java.lang.String toString(int o) {
        if (o == 1) {
            return "JOINT";
        }
        if (o == 2) {
            return "CHANNELS";
        }
        if (o == 4) {
            return "RAMP";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 1) == 1) {
            list.add("JOINT");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("CHANNELS");
            flipped |= 2;
        }
        if ((o & 4) == 4) {
            list.add("RAMP");
            flipped |= 4;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
