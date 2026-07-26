package android.hardware.health.V2_1;

/* JADX INFO: loaded from: classes.dex */
public final class Constants {
    public static final long BATTERY_CHARGE_TIME_TO_FULL_NOW_SECONDS_UNSUPPORTED = -1;

    public static final java.lang.String toString(long o) {
        if (o == -1) {
            return "BATTERY_CHARGE_TIME_TO_FULL_NOW_SECONDS_UNSUPPORTED";
        }
        return "0x" + java.lang.Long.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(long o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        long flipped = 0;
        if ((o & (-1)) == -1) {
            list.add("BATTERY_CHARGE_TIME_TO_FULL_NOW_SECONDS_UNSUPPORTED");
            flipped = 0 | (-1);
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Long.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
