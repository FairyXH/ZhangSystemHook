package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioStreamType {
    public static final int ACCESSIBILITY = 10;
    public static final int ALARM = 4;
    public static final int BLUETOOTH_SCO = 6;
    public static final int CNT = 13;
    public static final int DEFAULT = -1;
    public static final int DTMF = 8;
    public static final int ENFORCED_AUDIBLE = 7;
    public static final int FOR_POLICY_CNT = 12;
    public static final int MIN = 0;
    public static final int MUSIC = 3;
    public static final int NOTIFICATION = 5;
    public static final int PATCH = 12;
    public static final int PUBLIC_CNT = 11;
    public static final int REROUTING = 11;
    public static final int RING = 2;
    public static final int SYSTEM = 1;
    public static final int TTS = 9;
    public static final int VOICE_CALL = 0;

    public static final java.lang.String toString(int o) {
        if (o == -1) {
            return "DEFAULT";
        }
        if (o == 0) {
            return "MIN";
        }
        if (o == 0) {
            return "VOICE_CALL";
        }
        if (o == 1) {
            return "SYSTEM";
        }
        if (o == 2) {
            return "RING";
        }
        if (o == 3) {
            return "MUSIC";
        }
        if (o == 4) {
            return "ALARM";
        }
        if (o == 5) {
            return "NOTIFICATION";
        }
        if (o == 6) {
            return "BLUETOOTH_SCO";
        }
        if (o == 7) {
            return "ENFORCED_AUDIBLE";
        }
        if (o == 8) {
            return "DTMF";
        }
        if (o == 9) {
            return "TTS";
        }
        if (o == 10) {
            return "ACCESSIBILITY";
        }
        if (o == 11) {
            return "REROUTING";
        }
        if (o == 12) {
            return "PATCH";
        }
        if (o == 11) {
            return "PUBLIC_CNT";
        }
        if (o == 12) {
            return "FOR_POLICY_CNT";
        }
        if (o == 13) {
            return "CNT";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & (-1)) == -1) {
            list.add("DEFAULT");
            flipped = 0 | (-1);
        }
        list.add("MIN");
        list.add("VOICE_CALL");
        if ((o & 1) == 1) {
            list.add("SYSTEM");
            flipped |= 1;
        }
        if ((o & 2) == 2) {
            list.add("RING");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("MUSIC");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("ALARM");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("NOTIFICATION");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("BLUETOOTH_SCO");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("ENFORCED_AUDIBLE");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("DTMF");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("TTS");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("ACCESSIBILITY");
            flipped |= 10;
        }
        if ((o & 11) == 11) {
            list.add("REROUTING");
            flipped |= 11;
        }
        if ((o & 12) == 12) {
            list.add("PATCH");
            flipped |= 12;
        }
        if ((o & 11) == 11) {
            list.add("PUBLIC_CNT");
            flipped |= 11;
        }
        if ((o & 12) == 12) {
            list.add("FOR_POLICY_CNT");
            flipped |= 12;
        }
        if ((o & 13) == 13) {
            list.add("CNT");
            flipped |= 13;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
