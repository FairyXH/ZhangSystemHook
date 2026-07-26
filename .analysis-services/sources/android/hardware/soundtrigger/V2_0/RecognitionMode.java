package android.hardware.soundtrigger.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class RecognitionMode {
    public static final int GENERIC_TRIGGER = 8;
    public static final int USER_AUTHENTICATION = 4;
    public static final int USER_IDENTIFICATION = 2;
    public static final int VOICE_TRIGGER = 1;

    public static final java.lang.String toString(int o) {
        if (o == 1) {
            return "VOICE_TRIGGER";
        }
        if (o == 2) {
            return "USER_IDENTIFICATION";
        }
        if (o == 4) {
            return "USER_AUTHENTICATION";
        }
        if (o == 8) {
            return "GENERIC_TRIGGER";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 1) == 1) {
            list.add("VOICE_TRIGGER");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("USER_IDENTIFICATION");
            flipped |= 2;
        }
        if ((o & 4) == 4) {
            list.add("USER_AUTHENTICATION");
            flipped |= 4;
        }
        if ((o & 8) == 8) {
            list.add("GENERIC_TRIGGER");
            flipped |= 8;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
