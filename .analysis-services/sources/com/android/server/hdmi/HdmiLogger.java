package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class HdmiLogger {
    private static final long ERROR_LOG_DURATION_MILLIS = 20000;
    private static final java.lang.String TAG = "HDMI";
    private static final boolean DEBUG = android.util.Log.isLoggable("HDMI", 3);
    private static final java.lang.ThreadLocal<com.android.server.hdmi.HdmiLogger> sLogger = new java.lang.ThreadLocal<>();
    private final java.util.HashMap<java.lang.String, android.util.Pair<java.lang.Long, java.lang.Integer>> mWarningTimingCache = new java.util.HashMap<>();
    private final java.util.HashMap<java.lang.String, android.util.Pair<java.lang.Long, java.lang.Integer>> mErrorTimingCache = new java.util.HashMap<>();

    private HdmiLogger() {
    }

    static final void warning(java.lang.String logMessage, java.lang.Object... objs) {
        getLogger().warningInternal(toLogString(logMessage, objs));
    }

    private void warningInternal(java.lang.String logMessage) {
        java.lang.String log = updateLog(this.mWarningTimingCache, logMessage);
        if (!log.isEmpty()) {
            android.util.Slog.w("HDMI", log);
        }
    }

    static final void error(java.lang.String logMessage, java.lang.Object... objs) {
        getLogger().errorInternal(toLogString(logMessage, objs));
    }

    static final void error(java.lang.String logMessage, java.lang.Exception e, java.lang.Object... objs) {
        getLogger().errorInternal(toLogString(logMessage + e, objs));
    }

    private void errorInternal(java.lang.String logMessage) {
        java.lang.String log = updateLog(this.mErrorTimingCache, logMessage);
        if (!log.isEmpty()) {
            android.util.Slog.e("HDMI", log);
        }
    }

    static final void debug(java.lang.String logMessage, java.lang.Object... objs) {
        getLogger().debugInternal(toLogString(logMessage, objs));
    }

    private void debugInternal(java.lang.String logMessage) {
        if (DEBUG) {
            android.util.Slog.d("HDMI", logMessage);
        }
    }

    private static final java.lang.String toLogString(java.lang.String logMessage, java.lang.Object[] objs) {
        if (objs.length > 0) {
            return java.lang.String.format(logMessage, objs);
        }
        return logMessage;
    }

    private static com.android.server.hdmi.HdmiLogger getLogger() {
        com.android.server.hdmi.HdmiLogger logger = sLogger.get();
        if (logger == null) {
            com.android.server.hdmi.HdmiLogger logger2 = new com.android.server.hdmi.HdmiLogger();
            sLogger.set(logger2);
            return logger2;
        }
        return logger;
    }

    private static java.lang.String updateLog(java.util.HashMap<java.lang.String, android.util.Pair<java.lang.Long, java.lang.Integer>> cache, java.lang.String logMessage) {
        long curTime = android.os.SystemClock.uptimeMillis();
        android.util.Pair<java.lang.Long, java.lang.Integer> timing = cache.get(logMessage);
        if (shouldLogNow(timing, curTime)) {
            java.lang.String log = buildMessage(logMessage, timing);
            cache.put(logMessage, new android.util.Pair<>(java.lang.Long.valueOf(curTime), 1));
            return log;
        }
        increaseLogCount(cache, logMessage);
        return "";
    }

    private static java.lang.String buildMessage(java.lang.String message, android.util.Pair<java.lang.Long, java.lang.Integer> timing) {
        return "[" + (timing == null ? 1 : ((java.lang.Integer) timing.second).intValue()) + "]:" + message;
    }

    private static void increaseLogCount(java.util.HashMap<java.lang.String, android.util.Pair<java.lang.Long, java.lang.Integer>> cache, java.lang.String message) {
        android.util.Pair<java.lang.Long, java.lang.Integer> timing = cache.get(message);
        if (timing != null) {
            cache.put(message, new android.util.Pair<>((java.lang.Long) timing.first, java.lang.Integer.valueOf(((java.lang.Integer) timing.second).intValue() + 1)));
        }
    }

    private static boolean shouldLogNow(android.util.Pair<java.lang.Long, java.lang.Integer> timing, long curTime) {
        return timing == null || curTime - ((java.lang.Long) timing.first).longValue() > ERROR_LOG_DURATION_MILLIS;
    }
}
