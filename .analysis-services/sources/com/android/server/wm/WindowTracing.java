package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowTracing {
    private static final int BUFFER_CAPACITY_ALL = 20971520;
    private static final int BUFFER_CAPACITY_CRITICAL = 5242880;
    private static final int BUFFER_CAPACITY_TRIM = 10485760;
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final long MAGIC_NUMBER_VALUE = 4990904633914181975L;
    private static final java.lang.String TAG = "WindowTracing";
    private static final java.lang.String TRACE_FILENAME = "/data/misc/wmtrace/wm_trace.winscope";
    static final java.lang.String WINSCOPE_EXT = ".winscope";
    private final com.android.internal.util.TraceBuffer mBuffer;
    private final android.view.Choreographer mChoreographer;
    private boolean mEnabled;
    private final java.lang.Object mEnabledLock;
    private volatile boolean mEnabledLockFree;
    private final android.view.Choreographer.FrameCallback mFrameCallback;
    private final com.android.server.wm.WindowManagerGlobalLock mGlobalLock;
    private int mLogLevel;
    private boolean mLogOnFrame;
    private final com.android.internal.protolog.common.IProtoLog mProtoLog;
    private boolean mScheduled;
    private final com.android.server.wm.WindowManagerService mService;
    private final java.io.File mTraceFile;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(long frameTimeNanos) {
        log("onFrame");
    }

    static com.android.server.wm.WindowTracing createDefaultAndStartLooper(com.android.server.wm.WindowManagerService service, android.view.Choreographer choreographer) {
        java.io.File file = new java.io.File(TRACE_FILENAME);
        return new com.android.server.wm.WindowTracing(file, service, choreographer, BUFFER_CAPACITY_TRIM);
    }

    private WindowTracing(java.io.File file, com.android.server.wm.WindowManagerService service, android.view.Choreographer choreographer, int bufferCapacity) {
        this(file, service, choreographer, service.mGlobalLock, bufferCapacity);
    }

    WindowTracing(java.io.File file, com.android.server.wm.WindowManagerService service, android.view.Choreographer choreographer, com.android.server.wm.WindowManagerGlobalLock globalLock, int bufferCapacity) {
        this.mEnabledLock = new java.lang.Object();
        this.mFrameCallback = new android.view.Choreographer.FrameCallback() { // from class: com.android.server.wm.WindowTracing$$ExternalSyntheticLambda0
            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long j) {
                this.f$0.lambda$new$0(j);
            }
        };
        this.mLogLevel = 1;
        this.mLogOnFrame = false;
        this.mChoreographer = choreographer;
        this.mService = service;
        this.mGlobalLock = globalLock;
        this.mTraceFile = file;
        this.mBuffer = new com.android.internal.util.TraceBuffer(bufferCapacity);
        setLogLevel(1, null);
        this.mProtoLog = com.android.internal.protolog.ProtoLogImpl_209941506.getSingleInstance();
    }

    void startTrace(java.io.PrintWriter pw) {
        if (android.os.Build.IS_USER && !DEBUG) {
            logAndPrintln(pw, "Error: Tracing is not supported on user builds.");
            return;
        }
        synchronized (this.mEnabledLock) {
            if (!android.tracing.Flags.perfettoProtologTracing()) {
                com.android.internal.protolog.ProtoLogImpl_209941506.getSingleInstance().startProtoLog(pw);
            }
            logAndPrintln(pw, "Start tracing to " + this.mTraceFile + ".");
            this.mBuffer.resetBuffer();
            this.mEnabledLockFree = true;
            this.mEnabled = true;
        }
        log("trace.enable");
    }

    void stopTrace(java.io.PrintWriter pw) {
        if (android.os.Build.IS_USER && !DEBUG) {
            logAndPrintln(pw, "Error: Tracing is not supported on user builds.");
            return;
        }
        synchronized (this.mEnabledLock) {
            logAndPrintln(pw, "Stop tracing to " + this.mTraceFile + ". Waiting for traces to flush.");
            this.mEnabledLockFree = false;
            this.mEnabled = false;
            if (this.mEnabled) {
                logAndPrintln(pw, "ERROR: tracing was re-enabled while waiting for flush.");
                throw new java.lang.IllegalStateException("tracing enabled while waiting for flush.");
            }
            writeTraceToFileLocked();
            logAndPrintln(pw, "Trace written to " + this.mTraceFile + ".");
        }
        if (!android.tracing.Flags.perfettoProtologTracing()) {
            com.android.internal.protolog.ProtoLogImpl_209941506.getSingleInstance().stopProtoLog(pw, true);
        }
    }

    void saveForBugreport(java.io.PrintWriter pw) {
        if (android.os.Build.IS_USER) {
            logAndPrintln(pw, "Error: Tracing is not supported on user builds.");
            return;
        }
        synchronized (this.mEnabledLock) {
            if (this.mEnabled) {
                this.mEnabledLockFree = false;
                this.mEnabled = false;
                logAndPrintln(pw, "Stop tracing to " + this.mTraceFile + ". Waiting for traces to flush.");
                writeTraceToFileLocked();
                logAndPrintln(pw, "Trace written to " + this.mTraceFile + ".");
                if (!android.tracing.Flags.perfettoProtologTracing()) {
                    this.mProtoLog.stopProtoLog(pw, true);
                }
                logAndPrintln(pw, "Start tracing to " + this.mTraceFile + ".");
                this.mBuffer.resetBuffer();
                this.mEnabledLockFree = true;
                this.mEnabled = true;
                if (!android.tracing.Flags.perfettoProtologTracing()) {
                    this.mProtoLog.startProtoLog(pw);
                }
            }
        }
    }

    private void setLogLevel(int logLevel, java.io.PrintWriter pw) {
        logAndPrintln(pw, "Setting window tracing log level to " + logLevel);
        this.mLogLevel = logLevel;
        switch (logLevel) {
            case 0:
                setBufferCapacity(BUFFER_CAPACITY_ALL, pw);
                break;
            case 1:
                setBufferCapacity(BUFFER_CAPACITY_TRIM, pw);
                break;
            case 2:
                setBufferCapacity(BUFFER_CAPACITY_CRITICAL, pw);
                break;
        }
    }

    private void setLogFrequency(boolean onFrame, java.io.PrintWriter pw) {
        logAndPrintln(pw, "Setting window tracing log frequency to " + (onFrame ? "frame" : "transaction"));
        this.mLogOnFrame = onFrame;
    }

    private void setBufferCapacity(int capacity, java.io.PrintWriter pw) {
        logAndPrintln(pw, "Setting window tracing buffer capacity to " + capacity + "bytes");
        this.mBuffer.setCapacity(capacity);
    }

    boolean isEnabled() {
        return this.mEnabledLockFree;
    }

    int onShellCommand(android.os.ShellCommand shell) {
        byte b;
        java.io.PrintWriter pw = shell.getOutPrintWriter();
        java.lang.String cmd = shell.getNextArgRequired();
        byte b2 = -1;
        switch (cmd.hashCode()) {
            case -892481550:
                b = !cmd.equals("status") ? (byte) -1 : (byte) 3;
                break;
            case -390772652:
                b = !cmd.equals("save-for-bugreport") ? (byte) -1 : (byte) 2;
                break;
            case 3530753:
                b = !cmd.equals("size") ? (byte) -1 : (byte) 7;
                break;
            case 3540994:
                b = !cmd.equals("stop") ? (byte) -1 : (byte) 1;
                break;
            case 97692013:
                b = !cmd.equals("frame") ? (byte) -1 : (byte) 4;
                break;
            case 102865796:
                b = !cmd.equals("level") ? (byte) -1 : (byte) 6;
                break;
            case 109757538:
                b = !cmd.equals("start") ? (byte) -1 : (byte) 0;
                break;
            case 2141246174:
                b = !cmd.equals("transaction") ? (byte) -1 : (byte) 5;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                startTrace(pw);
                return 0;
            case 1:
                stopTrace(pw);
                return 0;
            case 2:
                saveForBugreport(pw);
                return 0;
            case 3:
                logAndPrintln(pw, getStatus());
                return 0;
            case 4:
                setLogFrequency(true, pw);
                this.mBuffer.resetBuffer();
                return 0;
            case 5:
                setLogFrequency(false, pw);
                this.mBuffer.resetBuffer();
                return 0;
            case 6:
                java.lang.String logLevelStr = shell.getNextArgRequired().toLowerCase();
                switch (logLevelStr.hashCode()) {
                    case 96673:
                        if (logLevelStr.equals("all")) {
                            b2 = 0;
                        }
                        break;
                    case 3568674:
                        if (logLevelStr.equals("trim")) {
                            b2 = 1;
                        }
                        break;
                    case 1952151455:
                        if (logLevelStr.equals("critical")) {
                            b2 = 2;
                        }
                        break;
                }
                switch (b2) {
                    case 0:
                        setLogLevel(0, pw);
                        break;
                    case 1:
                        setLogLevel(1, pw);
                        break;
                    case 2:
                        setLogLevel(2, pw);
                        break;
                    default:
                        setLogLevel(1, pw);
                        break;
                }
                this.mBuffer.resetBuffer();
                return 0;
            case 7:
                setBufferCapacity(java.lang.Integer.parseInt(shell.getNextArgRequired()) * 1024, pw);
                this.mBuffer.resetBuffer();
                return 0;
            default:
                pw.println("Unknown command: " + cmd);
                pw.println("Window manager trace options:");
                pw.println("  start: Start logging");
                pw.println("  stop: Stop logging");
                pw.println("  save-for-bugreport: Save logging data to file if it's running.");
                pw.println("  frame: Log trace once per frame");
                pw.println("  transaction: Log each transaction");
                pw.println("  size: Set the maximum log size (in KB)");
                pw.println("  status: Print trace status");
                pw.println("  level [lvl]: Set the log level between");
                pw.println("    lvl may be one of:");
                pw.println("      critical: Only visible windows with reduced information");
                pw.println("      trim: All windows with reduced");
                pw.println("      all: All window and information");
                return -1;
        }
    }

    java.lang.String getStatus() {
        return "Status: " + (isEnabled() ? "Enabled" : "Disabled") + "\nLog level: " + this.mLogLevel + "\n" + this.mBuffer.getStatus();
    }

    void logState(java.lang.String where) {
        if (!isEnabled()) {
            return;
        }
        if (this.mLogOnFrame) {
            schedule();
        } else {
            log(where);
        }
    }

    private void schedule() {
        if (this.mScheduled) {
            return;
        }
        this.mScheduled = true;
        this.mChoreographer.postFrameCallback(this.mFrameCallback);
    }

    private void log(java.lang.String where) {
        android.os.Trace.traceBegin(32L, "traceStateLocked");
        try {
            try {
                android.util.proto.ProtoOutputStream os = new android.util.proto.ProtoOutputStream();
                long tokenOuter = os.start(2246267895810L);
                os.write(1125281431553L, android.os.SystemClock.elapsedRealtimeNanos());
                os.write(1138166333442L, where);
                long tokenInner = os.start(1146756268035L);
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        android.os.Trace.traceBegin(32L, "dumpDebugLocked");
                        try {
                            this.mService.dumpDebugLocked(os, this.mLogLevel);
                            android.os.Trace.traceEnd(32L);
                        } finally {
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                os.end(tokenInner);
                os.end(tokenOuter);
                this.mBuffer.add(os);
                this.mScheduled = false;
            } catch (java.lang.Exception e) {
                android.util.Log.wtf(TAG, "Exception while tracing state", e);
            }
        } finally {
        }
    }

    private void logAndPrintln(java.io.PrintWriter pw, java.lang.String msg) {
        android.util.Log.i(TAG, msg);
        if (pw != null) {
            pw.println(msg);
            pw.flush();
        }
    }

    private void writeTraceToFileLocked() {
        try {
            try {
                android.os.Trace.traceBegin(32L, "writeTraceToFileLocked");
                android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
                proto.write(1125281431553L, MAGIC_NUMBER_VALUE);
                long timeOffsetNs = java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(java.lang.System.currentTimeMillis()) - android.os.SystemClock.elapsedRealtimeNanos();
                proto.write(1125281431555L, timeOffsetNs);
                this.mBuffer.writeTraceToFile(this.mTraceFile, proto);
            } catch (java.io.IOException e) {
                android.util.Log.e(TAG, "Unable to write buffer to file", e);
            }
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }
}
