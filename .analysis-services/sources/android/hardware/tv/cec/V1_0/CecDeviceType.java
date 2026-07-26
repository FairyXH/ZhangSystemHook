package android.hardware.tv.cec.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class CecDeviceType {
    public static final int AUDIO_SYSTEM = 5;
    public static final int INACTIVE = -1;
    public static final int MAX = 5;
    public static final int PLAYBACK = 4;
    public static final int RECORDER = 1;
    public static final int TUNER = 3;
    public static final int TV = 0;

    public static final java.lang.String toString(int o) {
        if (o == -1) {
            return "INACTIVE";
        }
        if (o == 0) {
            return "TV";
        }
        if (o == 1) {
            return "RECORDER";
        }
        if (o == 3) {
            return "TUNER";
        }
        if (o == 4) {
            return "PLAYBACK";
        }
        if (o == 5) {
            return "AUDIO_SYSTEM";
        }
        if (o == 5) {
            return "MAX";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & (-1)) == -1) {
            list.add("INACTIVE");
            flipped = 0 | (-1);
        }
        list.add("TV");
        if ((o & 1) == 1) {
            list.add("RECORDER");
            flipped |= 1;
        }
        if ((o & 3) == 3) {
            list.add("TUNER");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("PLAYBACK");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("AUDIO_SYSTEM");
            flipped |= 5;
        }
        if ((o & 5) == 5) {
            list.add("MAX");
            flipped |= 5;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
