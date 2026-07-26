package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioUsage {
    public static final int ALARM = 4;
    public static final int ASSISTANCE_ACCESSIBILITY = 11;
    public static final int ASSISTANCE_NAVIGATION_GUIDANCE = 12;
    public static final int ASSISTANCE_SONIFICATION = 13;
    public static final int ASSISTANT = 16;
    public static final int CNT = 17;
    public static final int GAME = 14;
    public static final int MAX = 16;
    public static final int MEDIA = 1;
    public static final int NOTIFICATION = 5;
    public static final int NOTIFICATION_COMMUNICATION_DELAYED = 9;
    public static final int NOTIFICATION_COMMUNICATION_INSTANT = 8;
    public static final int NOTIFICATION_COMMUNICATION_REQUEST = 7;
    public static final int NOTIFICATION_EVENT = 10;
    public static final int NOTIFICATION_TELEPHONY_RINGTONE = 6;
    public static final int UNKNOWN = 0;
    public static final int VIRTUAL_SOURCE = 15;
    public static final int VOICE_COMMUNICATION = 2;
    public static final int VOICE_COMMUNICATION_SIGNALLING = 3;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "UNKNOWN";
        }
        if (o == 1) {
            return "MEDIA";
        }
        if (o == 2) {
            return "VOICE_COMMUNICATION";
        }
        if (o == 3) {
            return "VOICE_COMMUNICATION_SIGNALLING";
        }
        if (o == 4) {
            return "ALARM";
        }
        if (o == 5) {
            return "NOTIFICATION";
        }
        if (o == 6) {
            return "NOTIFICATION_TELEPHONY_RINGTONE";
        }
        if (o == 7) {
            return "NOTIFICATION_COMMUNICATION_REQUEST";
        }
        if (o == 8) {
            return "NOTIFICATION_COMMUNICATION_INSTANT";
        }
        if (o == 9) {
            return "NOTIFICATION_COMMUNICATION_DELAYED";
        }
        if (o == 10) {
            return "NOTIFICATION_EVENT";
        }
        if (o == 11) {
            return "ASSISTANCE_ACCESSIBILITY";
        }
        if (o == 12) {
            return "ASSISTANCE_NAVIGATION_GUIDANCE";
        }
        if (o == 13) {
            return "ASSISTANCE_SONIFICATION";
        }
        if (o == 14) {
            return "GAME";
        }
        if (o == 15) {
            return "VIRTUAL_SOURCE";
        }
        if (o == 16) {
            return "ASSISTANT";
        }
        if (o == 17) {
            return "CNT";
        }
        if (o == 16) {
            return "MAX";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("UNKNOWN");
        if ((o & 1) == 1) {
            list.add("MEDIA");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("VOICE_COMMUNICATION");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("VOICE_COMMUNICATION_SIGNALLING");
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
            list.add("NOTIFICATION_TELEPHONY_RINGTONE");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("NOTIFICATION_COMMUNICATION_REQUEST");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("NOTIFICATION_COMMUNICATION_INSTANT");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("NOTIFICATION_COMMUNICATION_DELAYED");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("NOTIFICATION_EVENT");
            flipped |= 10;
        }
        if ((o & 11) == 11) {
            list.add("ASSISTANCE_ACCESSIBILITY");
            flipped |= 11;
        }
        if ((o & 12) == 12) {
            list.add("ASSISTANCE_NAVIGATION_GUIDANCE");
            flipped |= 12;
        }
        if ((o & 13) == 13) {
            list.add("ASSISTANCE_SONIFICATION");
            flipped |= 13;
        }
        if ((o & 14) == 14) {
            list.add("GAME");
            flipped |= 14;
        }
        if ((o & 15) == 15) {
            list.add("VIRTUAL_SOURCE");
            flipped |= 15;
        }
        if ((o & 16) == 16) {
            list.add("ASSISTANT");
            flipped |= 16;
        }
        if ((o & 17) == 17) {
            list.add("CNT");
            flipped |= 17;
        }
        if ((o & 16) == 16) {
            list.add("MAX");
            flipped |= 16;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
