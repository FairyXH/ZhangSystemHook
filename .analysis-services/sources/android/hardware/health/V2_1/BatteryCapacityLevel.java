package android.hardware.health.V2_1;

/* JADX INFO: loaded from: classes.dex */
public final class BatteryCapacityLevel {
    public static final int CRITICAL = 1;
    public static final int FULL = 5;
    public static final int HIGH = 4;
    public static final int LOW = 2;
    public static final int NORMAL = 3;
    public static final int UNKNOWN = 0;
    public static final int UNSUPPORTED = -1;

    public static final java.lang.String toString(int o) {
        if (o == -1) {
            return "UNSUPPORTED";
        }
        if (o == 0) {
            return "UNKNOWN";
        }
        if (o == 1) {
            return com.android.server.utils.PriorityDump.PRIORITY_ARG_CRITICAL;
        }
        if (o == 2) {
            return "LOW";
        }
        if (o == 3) {
            return com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL;
        }
        if (o == 4) {
            return com.android.server.utils.PriorityDump.PRIORITY_ARG_HIGH;
        }
        if (o == 5) {
            return "FULL";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & (-1)) == -1) {
            list.add("UNSUPPORTED");
            flipped = 0 | (-1);
        }
        list.add("UNKNOWN");
        if ((o & 1) == 1) {
            list.add(com.android.server.utils.PriorityDump.PRIORITY_ARG_CRITICAL);
            flipped |= 1;
        }
        if ((o & 2) == 2) {
            list.add("LOW");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add(com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL);
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add(com.android.server.utils.PriorityDump.PRIORITY_ARG_HIGH);
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
