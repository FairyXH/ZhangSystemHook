package android.hardware.tv.cec.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class CecLogicalAddress {
    public static final int AUDIO_SYSTEM = 5;
    public static final int BROADCAST = 15;
    public static final int FREE_USE = 14;
    public static final int PLAYBACK_1 = 4;
    public static final int PLAYBACK_2 = 8;
    public static final int PLAYBACK_3 = 11;
    public static final int RECORDER_1 = 1;
    public static final int RECORDER_2 = 2;
    public static final int RECORDER_3 = 9;
    public static final int TUNER_1 = 3;
    public static final int TUNER_2 = 6;
    public static final int TUNER_3 = 7;
    public static final int TUNER_4 = 10;
    public static final int TV = 0;
    public static final int UNREGISTERED = 15;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "TV";
        }
        if (o == 1) {
            return "RECORDER_1";
        }
        if (o == 2) {
            return "RECORDER_2";
        }
        if (o == 3) {
            return "TUNER_1";
        }
        if (o == 4) {
            return "PLAYBACK_1";
        }
        if (o == 5) {
            return "AUDIO_SYSTEM";
        }
        if (o == 6) {
            return "TUNER_2";
        }
        if (o == 7) {
            return "TUNER_3";
        }
        if (o == 8) {
            return "PLAYBACK_2";
        }
        if (o == 9) {
            return "RECORDER_3";
        }
        if (o == 10) {
            return "TUNER_4";
        }
        if (o == 11) {
            return "PLAYBACK_3";
        }
        if (o == 14) {
            return "FREE_USE";
        }
        if (o == 15) {
            return "UNREGISTERED";
        }
        if (o == 15) {
            return "BROADCAST";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("TV");
        if ((o & 1) == 1) {
            list.add("RECORDER_1");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("RECORDER_2");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("TUNER_1");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("PLAYBACK_1");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("AUDIO_SYSTEM");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("TUNER_2");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("TUNER_3");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("PLAYBACK_2");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("RECORDER_3");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("TUNER_4");
            flipped |= 10;
        }
        if ((o & 11) == 11) {
            list.add("PLAYBACK_3");
            flipped |= 11;
        }
        if ((o & 14) == 14) {
            list.add("FREE_USE");
            flipped |= 14;
        }
        if ((o & 15) == 15) {
            list.add("UNREGISTERED");
            flipped |= 15;
        }
        if ((o & 15) == 15) {
            list.add("BROADCAST");
            flipped |= 15;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
