package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioSource {
    public static final int CAMCORDER = 5;
    public static final int CNT = 10;
    public static final int DEFAULT = 0;
    public static final int FM_TUNER = 1998;
    public static final int HOTWORD = 1999;
    public static final int MAX = 9;
    public static final int MIC = 1;
    public static final int REMOTE_SUBMIX = 8;
    public static final int UNPROCESSED = 9;
    public static final int VOICE_CALL = 4;
    public static final int VOICE_COMMUNICATION = 7;
    public static final int VOICE_DOWNLINK = 3;
    public static final int VOICE_RECOGNITION = 6;
    public static final int VOICE_UPLINK = 2;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "DEFAULT";
        }
        if (o == 1) {
            return "MIC";
        }
        if (o == 2) {
            return "VOICE_UPLINK";
        }
        if (o == 3) {
            return "VOICE_DOWNLINK";
        }
        if (o == 4) {
            return "VOICE_CALL";
        }
        if (o == 5) {
            return "CAMCORDER";
        }
        if (o == 6) {
            return "VOICE_RECOGNITION";
        }
        if (o == 7) {
            return "VOICE_COMMUNICATION";
        }
        if (o == 8) {
            return "REMOTE_SUBMIX";
        }
        if (o == 9) {
            return "UNPROCESSED";
        }
        if (o == 10) {
            return "CNT";
        }
        if (o == 9) {
            return "MAX";
        }
        if (o == 1998) {
            return "FM_TUNER";
        }
        if (o == 1999) {
            return "HOTWORD";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("DEFAULT");
        if ((o & 1) == 1) {
            list.add("MIC");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("VOICE_UPLINK");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("VOICE_DOWNLINK");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("VOICE_CALL");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("CAMCORDER");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("VOICE_RECOGNITION");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("VOICE_COMMUNICATION");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("REMOTE_SUBMIX");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("UNPROCESSED");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("CNT");
            flipped |= 10;
        }
        if ((o & 9) == 9) {
            list.add("MAX");
            flipped |= 9;
        }
        if ((o & FM_TUNER) == 1998) {
            list.add("FM_TUNER");
            flipped |= FM_TUNER;
        }
        if ((o & 1999) == 1999) {
            list.add("HOTWORD");
            flipped |= 1999;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
