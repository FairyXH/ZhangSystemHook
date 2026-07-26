package com.android.server.am.trace;

/* JADX INFO: loaded from: classes.dex */
public class SmartTraceUtils {
    public static final int DUMP_MAX_COUNT = 10;
    private static final java.lang.String PROP_DUMP_CMD = "sys.smtrace.cmd";
    public static final java.lang.String PROP_DUMP_CMDLINES = "persist.sys.smtrace.dump.cmdlines.extra";
    public static final java.lang.String PROP_ENABLE_DUMP_PREDEFINED_PIDS = "persist.sys.smtrace.dump.predefined_pids.enable";
    public static final java.lang.String PROP_ENABLE_ON_BG_APP = "persist.sys.smtrace.bgapp.enable";
    public static final java.lang.String PROP_ENABLE_PERFETTO_DUMP = "persist.sys.perfetto_dump.enable";
    public static final java.lang.String PROP_ENABLE_PERFETTO_ON_BG_APP = "persist.sys.perfetto.bgapp.enable";
    public static final java.lang.String PROP_ENABLE_RECURSIVE_MODE = "persist.sys.smtrace.recursivemode.enable";
    public static final java.lang.String PROP_ENABLE_SMART_TRACE = "persist.sys.smtrace.enable";
    private static final java.lang.String PROP_PERFETTO_COMMAND = "sys.perfetto.cmd";
    private static final java.lang.String PROP_PERFETTO_MAX_TRACE_COUNT = "persist.sys.perfetto.max_trace_count";
    private static final java.lang.String TAG = "SmartTraceUtils";
    private static final java.lang.String TRACE_DIRECTORY = "/data/misc/perfetto-traces/";

    public static boolean isSmartTraceEnabled() {
        return android.os.SystemProperties.getBoolean(PROP_ENABLE_SMART_TRACE, false);
    }

    public static boolean isSmartTraceEnabledOnBgApp() {
        return android.os.SystemProperties.getBoolean(PROP_ENABLE_ON_BG_APP, true);
    }

    public static boolean isDumpPredefinedPidsEnabled() {
        return android.os.SystemProperties.getBoolean(PROP_ENABLE_DUMP_PREDEFINED_PIDS, false);
    }

    public static boolean isPerfettoDumpEnabled() {
        return android.os.SystemProperties.getBoolean(PROP_ENABLE_PERFETTO_DUMP, false);
    }

    public static boolean isPerfettoDumpEnabledOnBgApp() {
        return android.os.SystemProperties.getBoolean(PROP_ENABLE_PERFETTO_ON_BG_APP, true);
    }

    public static boolean isRecursiveModeEnabled() {
        return android.os.SystemProperties.getBoolean(PROP_ENABLE_RECURSIVE_MODE, true);
    }

    public static void dumpStackTraces(int pid, java.util.ArrayList<java.lang.Integer> firstPids, java.util.ArrayList<java.lang.Integer> nativePids, java.io.File outputFile) {
        if (!isSmartTraceEnabled()) {
            return;
        }
        if (isDumpingOn()) {
            android.util.Slog.e(TAG, "Attempting to run smart trace dump but trace is already in progress, skip it");
            return;
        }
        java.util.Set<java.lang.Integer> pidSet = getTargetPidsStuckInBinder(pid, firstPids, nativePids, outputFile);
        if (pidSet != null && pidSet.size() != 0) {
            final java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(pid);
            pidSet.forEach(new java.util.function.Consumer() { // from class: com.android.server.am.trace.SmartTraceUtils$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.am.trace.SmartTraceUtils.lambda$dumpStackTraces$0(sb, (java.lang.Integer) obj);
                }
            });
            sb.append(":" + outputFile.getPath());
            android.os.SystemProperties.set(PROP_DUMP_CMD, sb.toString());
            android.util.Slog.i(TAG, "Start collect stack trace for " + sb.toString());
        }
    }

    static /* synthetic */ void lambda$dumpStackTraces$0(java.lang.StringBuilder sb, java.lang.Integer p) {
        sb.append(",");
        sb.append(p);
    }

    public static boolean traceStart() {
        if (isTracingOn()) {
            android.util.Slog.e(TAG, "Attempting to start perfetto trace but trace is already in progress, skip it");
            return false;
        }
        android.util.Slog.i(TAG, "Perfetto trace start..");
        android.os.SystemProperties.set(PROP_PERFETTO_COMMAND, "START");
        return true;
    }

    private static boolean isDumpingOn() {
        return !android.text.TextUtils.isEmpty(android.os.SystemProperties.get(PROP_DUMP_CMD, ""));
    }

    private static boolean isTracingOn() {
        return !android.text.TextUtils.isEmpty(android.os.SystemProperties.get(PROP_PERFETTO_COMMAND, ""));
    }

    private static java.util.Set<java.lang.Integer> getTargetPidsStuckInBinder(int pid, java.util.ArrayList<java.lang.Integer> firstPids, java.util.ArrayList<java.lang.Integer> nativePids, java.io.File outputFile) {
        com.android.server.am.trace.BinderTransactions transactions = new com.android.server.am.trace.BinderTransactions(isRecursiveModeEnabled());
        transactions.binderStateRead(outputFile);
        java.util.Set<java.lang.Integer> pidSet = transactions.getTargetPidsStuckInBinder(pid);
        pidSet.removeAll(firstPids);
        if (nativePids != null) {
            pidSet.removeAll(nativePids);
        }
        int[] extraPids = readExtraCmdlinesFromProperty();
        if (extraPids != null) {
            for (int p : extraPids) {
                pidSet.add(java.lang.Integer.valueOf(p));
            }
        }
        return pidSet;
    }

    private static int[] readExtraCmdlinesFromProperty() {
        java.lang.String cmdlines = android.os.SystemProperties.get(PROP_DUMP_CMDLINES, "");
        if (android.text.TextUtils.isEmpty(cmdlines)) {
            return null;
        }
        try {
            return android.os.Process.getPidsForCommands(cmdlines.split(","));
        } catch (java.lang.NullPointerException e) {
            android.util.Slog.e(TAG, "Exception get pid for commonds " + cmdlines, e);
            return null;
        } catch (java.lang.OutOfMemoryError e2) {
            android.util.Slog.e(TAG, "Out of Memory when get pid for commonds " + cmdlines, e2);
            return null;
        }
    }
}
