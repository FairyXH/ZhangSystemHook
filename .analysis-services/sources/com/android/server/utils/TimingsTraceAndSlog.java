package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public final class TimingsTraceAndSlog extends android.util.TimingsTraceLog {
    private static final long BOTTLENECK_DURATION_MS = -1;
    private static final java.lang.String SYSTEM_SERVER_TIMING_ASYNC_TAG = "SystemServerTimingAsync";
    public static final java.lang.String SYSTEM_SERVER_TIMING_TAG = "SystemServerTiming";
    private final java.lang.String mTag;

    public static com.android.server.utils.TimingsTraceAndSlog newAsyncLog() {
        return new com.android.server.utils.TimingsTraceAndSlog(SYSTEM_SERVER_TIMING_ASYNC_TAG, 524288L);
    }

    public TimingsTraceAndSlog() {
        this(SYSTEM_SERVER_TIMING_TAG);
    }

    public TimingsTraceAndSlog(java.lang.String tag) {
        this(tag, 524288L);
    }

    public TimingsTraceAndSlog(java.lang.String tag, long traceTag) {
        super(tag, traceTag);
        this.mTag = tag;
    }

    public TimingsTraceAndSlog(com.android.server.utils.TimingsTraceAndSlog other) {
        super(other);
        this.mTag = other.mTag;
    }

    public void traceBegin(java.lang.String name) {
        android.util.Slog.d(this.mTag, name);
        super.traceBegin(name);
    }

    public void logDuration(java.lang.String name, long timeMs) {
        super.logDuration(name, timeMs);
    }

    public java.lang.String toString() {
        return "TimingsTraceAndSlog[" + this.mTag + "]";
    }
}
