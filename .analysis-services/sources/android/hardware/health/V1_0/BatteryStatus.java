package android.hardware.health.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class BatteryStatus {
    public static final int CHARGING = 2;
    public static final int DISCHARGING = 3;
    public static final int FULL = 5;
    public static final int NOT_CHARGING = 4;
    public static final int UNKNOWN = 1;

    public static final java.lang.String toString(int o) {
        if (o == 1) {
            return "UNKNOWN";
        }
        if (o == 2) {
            return "CHARGING";
        }
        if (o == 3) {
            return "DISCHARGING";
        }
        if (o == 4) {
            return "NOT_CHARGING";
        }
        if (o == 5) {
            return "FULL";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & 1) == 1) {
            list.add("UNKNOWN");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("CHARGING");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("DISCHARGING");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("NOT_CHARGING");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("FULL");
            flipped |= 5;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
