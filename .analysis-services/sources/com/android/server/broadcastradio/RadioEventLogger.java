package com.android.server.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public final class RadioEventLogger {
    private final boolean mDebug;
    private final android.util.LocalLog mEventLogger;
    private final java.lang.String mTag;

    public RadioEventLogger(java.lang.String tag, int loggerQueueSize) {
        this.mTag = tag;
        this.mDebug = android.util.Log.isLoggable(this.mTag, 3);
        this.mEventLogger = new android.util.LocalLog(loggerQueueSize);
    }

    public void logRadioEvent(java.lang.String logFormat, java.lang.Object... args) {
        java.lang.String log = android.text.TextUtils.formatSimple(logFormat, args);
        this.mEventLogger.log(log);
        if (this.mDebug) {
            com.android.server.utils.Slogf.d(this.mTag, logFormat, args);
        }
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        this.mEventLogger.dump(pw);
    }
}
