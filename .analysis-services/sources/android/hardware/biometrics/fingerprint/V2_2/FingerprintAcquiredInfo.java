package android.hardware.biometrics.fingerprint.V2_2;

/* JADX INFO: loaded from: classes.dex */
public final class FingerprintAcquiredInfo {
    public static final int ACQUIRED_GOOD = 0;
    public static final int ACQUIRED_IMAGER_DIRTY = 3;
    public static final int ACQUIRED_INSUFFICIENT = 2;
    public static final int ACQUIRED_PARTIAL = 1;
    public static final int ACQUIRED_TOO_FAST = 5;
    public static final int ACQUIRED_TOO_SLOW = 4;
    public static final int ACQUIRED_VENDOR = 6;
    public static final int START = 7;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "ACQUIRED_GOOD";
        }
        if (o == 1) {
            return "ACQUIRED_PARTIAL";
        }
        if (o == 2) {
            return "ACQUIRED_INSUFFICIENT";
        }
        if (o == 3) {
            return "ACQUIRED_IMAGER_DIRTY";
        }
        if (o == 4) {
            return "ACQUIRED_TOO_SLOW";
        }
        if (o == 5) {
            return "ACQUIRED_TOO_FAST";
        }
        if (o == 6) {
            return "ACQUIRED_VENDOR";
        }
        if (o == 7) {
            return "START";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("ACQUIRED_GOOD");
        if ((o & 1) == 1) {
            list.add("ACQUIRED_PARTIAL");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("ACQUIRED_INSUFFICIENT");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("ACQUIRED_IMAGER_DIRTY");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("ACQUIRED_TOO_SLOW");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("ACQUIRED_TOO_FAST");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("ACQUIRED_VENDOR");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("START");
            flipped |= 7;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
