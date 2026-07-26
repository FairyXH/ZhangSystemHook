package android.hardware.usb.V1_1;

/* JADX INFO: loaded from: classes.dex */
public final class PortMode_1_1 {
    public static final int AUDIO_ACCESSORY = 4;
    public static final int DEBUG_ACCESSORY = 8;
    public static final int DFP = 2;
    public static final int DRP = 3;
    public static final int NONE = 0;
    public static final int NUM_MODES = 4;
    public static final int NUM_MODES_1_1 = 16;
    public static final int UFP = 1;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "NONE";
        }
        if (o == 1) {
            return "UFP";
        }
        if (o == 2) {
            return "DFP";
        }
        if (o == 3) {
            return "DRP";
        }
        if (o == 4) {
            return "NUM_MODES";
        }
        if (o == 4) {
            return "AUDIO_ACCESSORY";
        }
        if (o == 8) {
            return "DEBUG_ACCESSORY";
        }
        if (o == 16) {
            return "NUM_MODES_1_1";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("NONE");
        if ((o & 1) == 1) {
            list.add("UFP");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("DFP");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("DRP");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("NUM_MODES");
            flipped |= 4;
        }
        if ((o & 4) == 4) {
            list.add("AUDIO_ACCESSORY");
            flipped |= 4;
        }
        if ((o & 8) == 8) {
            list.add("DEBUG_ACCESSORY");
            flipped |= 8;
        }
        if ((o & 16) == 16) {
            list.add("NUM_MODES_1_1");
            flipped |= 16;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
