package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class TraceErrorLogger {
    private static final java.lang.String COUNTER_PREFIX = "ErrorId:";
    private static final int PLACEHOLDER_VALUE = 1;

    public boolean isAddErrorIdEnabled() {
        return true;
    }

    public java.util.UUID generateErrorId() {
        return java.util.UUID.randomUUID();
    }

    public void addProcessInfoAndErrorIdToTrace(java.lang.String processName, int pid, java.util.UUID errorId) {
        android.os.Trace.traceCounter(64L, COUNTER_PREFIX + processName + " " + pid + "#" + errorId.toString(), 1);
    }

    public void addSubjectToTrace(java.lang.String subject, java.util.UUID errorId) {
        android.os.Trace.traceCounter(64L, java.lang.String.format("Subject(for ErrorId %s):%s", errorId.toString(), subject), 1);
    }
}
