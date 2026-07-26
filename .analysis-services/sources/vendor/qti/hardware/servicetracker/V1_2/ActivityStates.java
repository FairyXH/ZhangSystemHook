package vendor.qti.hardware.servicetracker.V1_2;

/* JADX INFO: loaded from: classes4.dex */
public final class ActivityStates {
    public static final int DESTROYED = 9;
    public static final int DESTROYING = 8;
    public static final int FINISHING = 7;
    public static final int INITIALIZING = 0;
    public static final int PAUSED = 4;
    public static final int PAUSING = 3;
    public static final int RESTARTING_PROCESS = 10;
    public static final int RESUMED = 2;
    public static final int STARTED = 1;
    public static final int STOPPED = 6;
    public static final int STOPPING = 5;
    public static final int UNKNOWN = 11;

    public static final java.lang.String toString(int o) {
        if (o == 0) {
            return "INITIALIZING";
        }
        if (o == 1) {
            return "STARTED";
        }
        if (o == 2) {
            return "RESUMED";
        }
        if (o == 3) {
            return "PAUSING";
        }
        if (o == 4) {
            return "PAUSED";
        }
        if (o == 5) {
            return "STOPPING";
        }
        if (o == 6) {
            return "STOPPED";
        }
        if (o == 7) {
            return "FINISHING";
        }
        if (o == 8) {
            return "DESTROYING";
        }
        if (o == 9) {
            return "DESTROYED";
        }
        if (o == 10) {
            return "RESTARTING_PROCESS";
        }
        if (o == 11) {
            return "UNKNOWN";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("INITIALIZING");
        if ((o & 1) == 1) {
            list.add("STARTED");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("RESUMED");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("PAUSING");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("PAUSED");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("STOPPING");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("STOPPED");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("FINISHING");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("DESTROYING");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("DESTROYED");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("RESTARTING_PROCESS");
            flipped |= 10;
        }
        if ((o & 11) == 11) {
            list.add("UNKNOWN");
            flipped |= 11;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
