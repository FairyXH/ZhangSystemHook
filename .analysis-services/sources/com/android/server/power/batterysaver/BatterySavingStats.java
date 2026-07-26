package com.android.server.power.batterysaver;

/* JADX INFO: loaded from: classes3.dex */
public class BatterySavingStats {
    private static final boolean DEBUG = false;
    private static final int STATE_NOT_INITIALIZED = -1;
    private static final java.lang.String TAG = "BatterySavingStats";
    private final java.lang.Object mLock;
    private int mCurrentState = -1;
    final android.util.SparseArray<com.android.server.power.batterysaver.BatterySavingStats.Stat> mStats = new android.util.SparseArray<>();
    private int mBatterySaverEnabledCount = 0;
    private long mLastBatterySaverEnabledTime = 0;
    private long mLastBatterySaverDisabledTime = 0;
    private int mAdaptiveBatterySaverEnabledCount = 0;
    private long mLastAdaptiveBatterySaverEnabledTime = 0;
    private long mLastAdaptiveBatterySaverDisabledTime = 0;
    private android.os.BatteryManagerInternal mBatteryManagerInternal = (android.os.BatteryManagerInternal) com.android.server.LocalServices.getService(android.os.BatteryManagerInternal.class);

    interface BatterySaverState {
        public static final int ADAPTIVE = 2;
        public static final int BITS = 2;
        public static final int MASK = 3;
        public static final int OFF = 0;
        public static final int ON = 1;
        public static final int SHIFT = 0;

        static int fromIndex(int index) {
            return (index >> 0) & 3;
        }
    }

    interface InteractiveState {
        public static final int BITS = 1;
        public static final int INTERACTIVE = 1;
        public static final int MASK = 1;
        public static final int NON_INTERACTIVE = 0;
        public static final int SHIFT = 2;

        static int fromIndex(int index) {
            return (index >> 2) & 1;
        }
    }

    interface DozeState {
        public static final int BITS = 2;
        public static final int DEEP = 2;
        public static final int LIGHT = 1;
        public static final int MASK = 3;
        public static final int NOT_DOZING = 0;
        public static final int SHIFT = 3;

        static int fromIndex(int index) {
            return (index >> 3) & 3;
        }
    }

    interface PlugState {
        public static final int BITS = 1;
        public static final int MASK = 1;
        public static final int PLUGGED = 1;
        public static final int SHIFT = 5;
        public static final int UNPLUGGED = 0;

        static int fromIndex(int index) {
            return (index >> 5) & 1;
        }
    }

    static class Stat {
        public int endBatteryLevel;
        public int endBatteryPercent;
        public long endTime;
        public int startBatteryLevel;
        public int startBatteryPercent;
        public long startTime;
        public int totalBatteryDrain;
        public int totalBatteryDrainPercent;
        public long totalTimeMillis;

        Stat() {
        }

        public long totalMinutes() {
            return this.totalTimeMillis / 60000;
        }

        public double drainPerHour() {
            if (this.totalTimeMillis == 0) {
                return 0.0d;
            }
            return ((double) this.totalBatteryDrain) / (this.totalTimeMillis / 3600000.0d);
        }

        public double drainPercentPerHour() {
            if (this.totalTimeMillis == 0) {
                return 0.0d;
            }
            return ((double) this.totalBatteryDrainPercent) / (this.totalTimeMillis / 3600000.0d);
        }

        java.lang.String toStringForTest() {
            return "{" + totalMinutes() + "m," + this.totalBatteryDrain + "," + java.lang.String.format("%.2f", java.lang.Double.valueOf(drainPerHour())) + "uA/H," + java.lang.String.format("%.2f", java.lang.Double.valueOf(drainPercentPerHour())) + "%}";
        }
    }

    public BatterySavingStats(java.lang.Object lock) {
        this.mLock = lock;
    }

    private android.os.BatteryManagerInternal getBatteryManagerInternal() {
        if (this.mBatteryManagerInternal == null) {
            this.mBatteryManagerInternal = (android.os.BatteryManagerInternal) com.android.server.LocalServices.getService(android.os.BatteryManagerInternal.class);
            if (this.mBatteryManagerInternal == null) {
                android.util.Slog.wtf(TAG, "BatteryManagerInternal not initialized");
            }
        }
        return this.mBatteryManagerInternal;
    }

    static int statesToIndex(int batterySaverState, int interactiveState, int dozeState, int plugState) {
        int ret = batterySaverState & 3;
        return ret | ((interactiveState & 1) << 2) | ((dozeState & 3) << 3) | ((plugState & 1) << 5);
    }

    static java.lang.String stateToString(int state) {
        switch (state) {
            case -1:
                return "NotInitialized";
            default:
                return "BS=" + com.android.server.power.batterysaver.BatterySavingStats.BatterySaverState.fromIndex(state) + ",I=" + com.android.server.power.batterysaver.BatterySavingStats.InteractiveState.fromIndex(state) + ",D=" + com.android.server.power.batterysaver.BatterySavingStats.DozeState.fromIndex(state) + ",P=" + com.android.server.power.batterysaver.BatterySavingStats.PlugState.fromIndex(state);
        }
    }

    com.android.server.power.batterysaver.BatterySavingStats.Stat getStat(int stateIndex) {
        com.android.server.power.batterysaver.BatterySavingStats.Stat stat;
        synchronized (this.mLock) {
            stat = this.mStats.get(stateIndex);
            if (stat == null) {
                stat = new com.android.server.power.batterysaver.BatterySavingStats.Stat();
                this.mStats.put(stateIndex, stat);
            }
        }
        return stat;
    }

    private com.android.server.power.batterysaver.BatterySavingStats.Stat getStat(int batterySaverState, int interactiveState, int dozeState, int plugState) {
        return getStat(statesToIndex(batterySaverState, interactiveState, dozeState, plugState));
    }

    long injectCurrentTime() {
        return android.os.SystemClock.elapsedRealtime();
    }

    int injectBatteryLevel() {
        android.os.BatteryManagerInternal bmi = getBatteryManagerInternal();
        if (bmi == null) {
            return 0;
        }
        return bmi.getBatteryChargeCounter();
    }

    int injectBatteryPercent() {
        android.os.BatteryManagerInternal bmi = getBatteryManagerInternal();
        if (bmi == null) {
            return 0;
        }
        return bmi.getBatteryLevel();
    }

    void transitionState(int batterySaverState, int interactiveState, int dozeState, int plugState) {
        synchronized (this.mLock) {
            int newState = statesToIndex(batterySaverState, interactiveState, dozeState, plugState);
            transitionStateLocked(newState);
        }
    }

    private void transitionStateLocked(int newState) {
        if (this.mCurrentState == newState) {
            return;
        }
        long now = injectCurrentTime();
        int batteryLevel = injectBatteryLevel();
        int batteryPercent = injectBatteryPercent();
        int oldBatterySaverState = this.mCurrentState < 0 ? 0 : com.android.server.power.batterysaver.BatterySavingStats.BatterySaverState.fromIndex(this.mCurrentState);
        int newBatterySaverState = newState >= 0 ? com.android.server.power.batterysaver.BatterySavingStats.BatterySaverState.fromIndex(newState) : 0;
        if (oldBatterySaverState != newBatterySaverState) {
            switch (newBatterySaverState) {
                case 0:
                    if (oldBatterySaverState == 1) {
                        this.mLastBatterySaverDisabledTime = now;
                    } else {
                        this.mLastAdaptiveBatterySaverDisabledTime = now;
                    }
                    break;
                case 1:
                    this.mBatterySaverEnabledCount++;
                    this.mLastBatterySaverEnabledTime = now;
                    if (oldBatterySaverState == 2) {
                        this.mLastAdaptiveBatterySaverDisabledTime = now;
                    }
                    break;
                case 2:
                    this.mAdaptiveBatterySaverEnabledCount++;
                    this.mLastAdaptiveBatterySaverEnabledTime = now;
                    if (oldBatterySaverState == 1) {
                        this.mLastBatterySaverDisabledTime = now;
                    }
                    break;
            }
        }
        endLastStateLocked(now, batteryLevel, batteryPercent);
        startNewStateLocked(newState, now, batteryLevel, batteryPercent);
    }

    private void endLastStateLocked(long now, int batteryLevel, int batteryPercent) {
        if (this.mCurrentState >= 0) {
            com.android.server.power.batterysaver.BatterySavingStats.Stat stat = getStat(this.mCurrentState);
            stat.endBatteryLevel = batteryLevel;
            stat.endBatteryPercent = batteryPercent;
            stat.endTime = now;
            long deltaTime = stat.endTime - stat.startTime;
            int deltaDrain = stat.startBatteryLevel - stat.endBatteryLevel;
            int deltaPercent = stat.startBatteryPercent - stat.endBatteryPercent;
            stat.totalTimeMillis += deltaTime;
            stat.totalBatteryDrain += deltaDrain;
            stat.totalBatteryDrainPercent += deltaPercent;
            com.android.server.EventLogTags.writeBatterySavingStats(com.android.server.power.batterysaver.BatterySavingStats.BatterySaverState.fromIndex(this.mCurrentState), com.android.server.power.batterysaver.BatterySavingStats.InteractiveState.fromIndex(this.mCurrentState), com.android.server.power.batterysaver.BatterySavingStats.DozeState.fromIndex(this.mCurrentState), deltaTime, deltaDrain, deltaPercent, stat.totalTimeMillis, stat.totalBatteryDrain, stat.totalBatteryDrainPercent);
        }
    }

    private void startNewStateLocked(int newState, long now, int batteryLevel, int batteryPercent) {
        this.mCurrentState = newState;
        if (this.mCurrentState < 0) {
            return;
        }
        com.android.server.power.batterysaver.BatterySavingStats.Stat stat = getStat(this.mCurrentState);
        stat.startBatteryLevel = batteryLevel;
        stat.startBatteryPercent = batteryPercent;
        stat.startTime = now;
        stat.endTime = 0L;
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        pw.println("Battery saving stats:");
        pw.increaseIndent();
        synchronized (this.mLock) {
            long now = java.lang.System.currentTimeMillis();
            long nowElapsed = injectCurrentTime();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            pw.print("Battery Saver is currently: ");
            switch (com.android.server.power.batterysaver.BatterySavingStats.BatterySaverState.fromIndex(this.mCurrentState)) {
                case 0:
                    pw.println("OFF");
                    break;
                case 1:
                    pw.println("ON");
                    break;
                case 2:
                    pw.println("ADAPTIVE");
                    break;
            }
            pw.increaseIndent();
            if (this.mLastBatterySaverEnabledTime > 0) {
                pw.print("Last ON time: ");
                pw.print(sdf.format(new java.util.Date((now - nowElapsed) + this.mLastBatterySaverEnabledTime)));
                pw.print(" ");
                android.util.TimeUtils.formatDuration(this.mLastBatterySaverEnabledTime, nowElapsed, pw);
                pw.println();
            }
            if (this.mLastBatterySaverDisabledTime > 0) {
                pw.print("Last OFF time: ");
                pw.print(sdf.format(new java.util.Date((now - nowElapsed) + this.mLastBatterySaverDisabledTime)));
                pw.print(" ");
                android.util.TimeUtils.formatDuration(this.mLastBatterySaverDisabledTime, nowElapsed, pw);
                pw.println();
            }
            pw.print("Times full enabled: ");
            pw.println(this.mBatterySaverEnabledCount);
            if (this.mLastAdaptiveBatterySaverEnabledTime > 0) {
                pw.print("Last ADAPTIVE ON time: ");
                pw.print(sdf.format(new java.util.Date((now - nowElapsed) + this.mLastAdaptiveBatterySaverEnabledTime)));
                pw.print(" ");
                android.util.TimeUtils.formatDuration(this.mLastAdaptiveBatterySaverEnabledTime, nowElapsed, pw);
                pw.println();
            }
            if (this.mLastAdaptiveBatterySaverDisabledTime > 0) {
                pw.print("Last ADAPTIVE OFF time: ");
                pw.print(sdf.format(new java.util.Date((now - nowElapsed) + this.mLastAdaptiveBatterySaverDisabledTime)));
                pw.print(" ");
                android.util.TimeUtils.formatDuration(this.mLastAdaptiveBatterySaverDisabledTime, nowElapsed, pw);
                pw.println();
            }
            pw.print("Times adaptive enabled: ");
            pw.println(this.mAdaptiveBatterySaverEnabledCount);
            pw.decreaseIndent();
            pw.println();
            pw.println("Drain stats:");
            pw.println("                   Battery saver OFF                          ON");
            dumpLineLocked(pw, 0, "NonIntr", 0, "NonDoze");
            dumpLineLocked(pw, 1, "   Intr", 0, "       ");
            dumpLineLocked(pw, 0, "NonIntr", 2, "Deep   ");
            dumpLineLocked(pw, 1, "   Intr", 2, "       ");
            dumpLineLocked(pw, 0, "NonIntr", 1, "Light  ");
            dumpLineLocked(pw, 1, "   Intr", 1, "       ");
        }
        pw.decreaseIndent();
    }

    private void dumpLineLocked(android.util.IndentingPrintWriter pw, int interactiveState, java.lang.String interactiveLabel, int dozeState, java.lang.String dozeLabel) {
        pw.print(dozeLabel);
        pw.print(" ");
        pw.print(interactiveLabel);
        pw.print(": ");
        com.android.server.power.batterysaver.BatterySavingStats.Stat offStat = getStat(0, interactiveState, dozeState, 0);
        com.android.server.power.batterysaver.BatterySavingStats.Stat onStat = getStat(1, interactiveState, dozeState, 0);
        pw.println(java.lang.String.format("%6dm %6dmAh(%3d%%) %8.1fmAh/h     %6dm %6dmAh(%3d%%) %8.1fmAh/h", java.lang.Long.valueOf(offStat.totalMinutes()), java.lang.Integer.valueOf(offStat.totalBatteryDrain / 1000), java.lang.Integer.valueOf(offStat.totalBatteryDrainPercent), java.lang.Double.valueOf(offStat.drainPerHour() / 1000.0d), java.lang.Long.valueOf(onStat.totalMinutes()), java.lang.Integer.valueOf(onStat.totalBatteryDrain / 1000), java.lang.Integer.valueOf(onStat.totalBatteryDrainPercent), java.lang.Double.valueOf(onStat.drainPerHour() / 1000.0d)));
    }
}
