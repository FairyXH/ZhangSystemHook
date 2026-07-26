package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public abstract class AnrTimer<V> implements java.lang.AutoCloseable {
    static final java.lang.String TAG = "AnrTimer";
    private static final long TRACE_TAG = 64;
    private static final java.lang.String TRACK = "AnrTimerTrack";
    private final com.android.server.utils.AnrTimer.Args mArgs;
    private final com.android.server.utils.AnrTimer<V>.FeatureSwitch mFeature;
    private final android.os.Handler mHandler;
    private final java.lang.String mLabel;
    private final java.lang.Object mLock;
    private int mMaxStarted;
    private final android.util.SparseArray<V> mTimerArgMap;
    private final android.util.ArrayMap<V, java.lang.Integer> mTimerIdMap;
    private int mTotalErrors;
    private int mTotalExpired;
    private int mTotalStarted;
    private final int mWhat;
    private static boolean DEBUG = false;
    private static final com.android.server.utils.AnrTimer.Injector sDefaultInjector = new com.android.server.utils.AnrTimer.Injector();
    private static final com.android.internal.util.RingBuffer<com.android.server.utils.AnrTimer.Error> sErrors = new com.android.internal.util.RingBuffer<>(com.android.server.utils.AnrTimer.Error.class, 20);
    private static final android.util.LongSparseArray<java.lang.ref.WeakReference<com.android.server.utils.AnrTimer>> sAnrTimerList = new android.util.LongSparseArray<>();
    private static final java.util.Comparator<com.android.server.utils.AnrTimer> sComparator = java.util.Comparator.nullsLast(new java.util.Comparator<com.android.server.utils.AnrTimer>() { // from class: com.android.server.utils.AnrTimer.1
        @Override // java.util.Comparator
        public int compare(com.android.server.utils.AnrTimer o1, com.android.server.utils.AnrTimer o2) {
            return o1.mLabel.compareTo(o2.mLabel);
        }
    });

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeAnrTimerAccept(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeAnrTimerCancel(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nativeAnrTimerClose(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public native long nativeAnrTimerCreate(java.lang.String str, boolean z, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeAnrTimerDiscard(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native java.lang.String[] nativeAnrTimerDump(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeAnrTimerRelease(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nativeAnrTimerStart(long j, int i, int i2, long j2);

    private static native boolean nativeAnrTimerSupported();

    public abstract int getPid(V v);

    public abstract int getUid(V v);

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean anrTimerServiceEnabled() {
        return com.android.server.utils.Flags.anrTimerService();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean anrTimerFreezerEnabled() {
        return com.android.server.utils.Flags.anrTimerFreezer();
    }

    static class Injector {
        Injector() {
        }

        boolean anrTimerServiceEnabled() {
            return com.android.server.utils.AnrTimer.anrTimerServiceEnabled();
        }

        boolean anrTimerFreezerEnabled() {
            return com.android.server.utils.AnrTimer.anrTimerFreezerEnabled();
        }
    }

    public static class Args {
        private com.android.server.utils.AnrTimer.Injector mInjector = com.android.server.utils.AnrTimer.sDefaultInjector;
        private boolean mExtend = false;
        boolean mFreeze = false;

        com.android.server.utils.AnrTimer.Args injector(com.android.server.utils.AnrTimer.Injector injector) {
            this.mInjector = injector;
            return this;
        }

        public com.android.server.utils.AnrTimer.Args extend(boolean flag) {
            this.mExtend = flag;
            return this;
        }

        public com.android.server.utils.AnrTimer.Args freeze(boolean enable) {
            this.mFreeze = enable;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public class TimerLock implements java.lang.AutoCloseable {
        final int mTimerId;
        private final android.util.CloseGuard mGuard = new android.util.CloseGuard();
        private final java.lang.Object mLock = new java.lang.Object();
        private boolean mClosed = false;

        TimerLock(int timerId) {
            this.mTimerId = timerId;
            this.mGuard.open("AnrTimer.release");
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            synchronized (this.mLock) {
                if (!this.mClosed) {
                    com.android.server.utils.AnrTimer.this.release(this);
                    this.mGuard.close();
                    this.mClosed = true;
                }
            }
        }

        protected void finalize() throws java.lang.Throwable {
            try {
                if (this.mGuard != null) {
                    this.mGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    private static final class Error {
        final java.lang.String arg;
        final java.lang.String issue;
        final java.lang.String operation;
        final java.lang.StackTraceElement[] stack;
        final java.lang.String tag;
        final long timestamp = android.os.SystemClock.elapsedRealtime();

        Error(java.lang.String issue, java.lang.String operation, java.lang.String tag, java.lang.StackTraceElement[] stack, java.lang.String arg) {
            this.issue = issue;
            this.operation = operation;
            this.tag = tag;
            this.stack = stack;
            this.arg = arg;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(android.util.IndentingPrintWriter ipw, int seq) {
            ipw.format("%2d: op:%s tag:%s issue:%s arg:%s\n", new java.lang.Object[]{java.lang.Integer.valueOf(seq), this.operation, this.tag, this.issue, this.arg});
            long offset = java.lang.System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime();
            long etime = this.timestamp + offset;
            ipw.println("    date:" + android.text.format.TimeMigrationUtils.formatMillisWithFixedFormat(etime));
            ipw.increaseIndent();
            for (int i = 0; i < this.stack.length; i++) {
                ipw.println("    " + this.stack[i].toString());
            }
            ipw.decreaseIndent();
        }
    }

    public AnrTimer(android.os.Handler handler, int what, java.lang.String label, com.android.server.utils.AnrTimer.Args args) {
        this.mLock = new java.lang.Object();
        this.mTimerIdMap = new android.util.ArrayMap<>();
        this.mTimerArgMap = new android.util.SparseArray<>();
        boolean enabled = false;
        this.mMaxStarted = 0;
        this.mTotalStarted = 0;
        this.mTotalErrors = 0;
        this.mTotalExpired = 0;
        this.mHandler = handler;
        this.mWhat = what;
        this.mLabel = label;
        this.mArgs = args;
        if (args.mInjector.anrTimerServiceEnabled() && nativeTimersSupported()) {
            enabled = true;
        }
        this.mFeature = createFeatureSwitch(enabled);
    }

    private com.android.server.utils.AnrTimer<V>.FeatureSwitch createFeatureSwitch(boolean enabled) {
        if (!enabled) {
            return new com.android.server.utils.AnrTimer.FeatureDisabled();
        }
        try {
            return new com.android.server.utils.AnrTimer.FeatureEnabled();
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(TAG, e.toString());
            return new com.android.server.utils.AnrTimer.FeatureDisabled();
        }
    }

    public AnrTimer(android.os.Handler handler, int what, java.lang.String label) {
        this(handler, what, label, new com.android.server.utils.AnrTimer.Args());
    }

    public boolean serviceEnabled() {
        return this.mFeature.enabled();
    }

    private void trace(java.lang.String op, int timerId, int pid, int uid, long milliseconds) {
        java.lang.String label = android.text.TextUtils.formatSimple("%s(%d,%d,%d,%s,%d)", new java.lang.Object[]{op, java.lang.Integer.valueOf(timerId), java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid), this.mLabel, java.lang.Long.valueOf(milliseconds)});
        android.os.Trace.instantForTrack(TRACE_TAG, TRACK, label);
        if (DEBUG) {
            android.util.Log.i(TAG, label);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trace(java.lang.String op, int timerId) {
        java.lang.String label = android.text.TextUtils.formatSimple("%s(%d)", new java.lang.Object[]{op, java.lang.Integer.valueOf(timerId)});
        android.os.Trace.instantForTrack(TRACE_TAG, TRACK, label);
        if (DEBUG) {
            android.util.Log.i(TAG, label);
        }
    }

    private static void trace(java.lang.String op, int pid, int uid) {
        java.lang.String label = android.text.TextUtils.formatSimple("%s(%d,%d)", new java.lang.Object[]{op, java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid)});
        android.os.Trace.instantForTrack(TRACE_TAG, TRACK, label);
        if (DEBUG) {
            android.util.Log.i(TAG, label);
        }
    }

    private abstract class FeatureSwitch {
        abstract com.android.server.utils.AnrTimer<V>.TimerLock accept(V v);

        abstract boolean cancel(V v);

        abstract void close();

        abstract boolean discard(V v);

        abstract void dump(android.util.IndentingPrintWriter indentingPrintWriter, boolean z);

        abstract boolean enabled();

        abstract void release(com.android.server.utils.AnrTimer<V>.TimerLock timerLock);

        abstract void start(V v, int i, int i2, long j);

        private FeatureSwitch() {
        }
    }

    private class FeatureDisabled extends com.android.server.utils.AnrTimer<V>.FeatureSwitch {
        private FeatureDisabled() {
            super();
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        void start(V arg, int pid, int uid, long timeoutMs) {
            android.os.Message msg = com.android.server.utils.AnrTimer.this.mHandler.obtainMessage(com.android.server.utils.AnrTimer.this.mWhat, arg);
            com.android.server.utils.AnrTimer.this.mHandler.sendMessageDelayed(msg, timeoutMs);
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        boolean cancel(V arg) {
            com.android.server.utils.AnrTimer.this.mHandler.removeMessages(com.android.server.utils.AnrTimer.this.mWhat, arg);
            return true;
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        com.android.server.utils.AnrTimer<V>.TimerLock accept(V arg) {
            return null;
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        boolean discard(V arg) {
            return true;
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        void release(com.android.server.utils.AnrTimer<V>.TimerLock timer) {
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        boolean enabled() {
            return false;
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        void dump(android.util.IndentingPrintWriter pw, boolean verbose) {
            synchronized (com.android.server.utils.AnrTimer.this.mLock) {
                pw.format("started=%d maxStarted=%d running=%d expired=%d errors=%d\n", new java.lang.Object[]{java.lang.Integer.valueOf(com.android.server.utils.AnrTimer.this.mTotalStarted), java.lang.Integer.valueOf(com.android.server.utils.AnrTimer.this.mMaxStarted), java.lang.Integer.valueOf(com.android.server.utils.AnrTimer.this.mTimerIdMap.size()), java.lang.Integer.valueOf(com.android.server.utils.AnrTimer.this.mTotalExpired), java.lang.Integer.valueOf(com.android.server.utils.AnrTimer.this.mTotalErrors)});
            }
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        void close() {
        }
    }

    private class FeatureEnabled extends com.android.server.utils.AnrTimer<V>.FeatureSwitch {
        private long mNative;
        private int mTotalRestarted;

        FeatureEnabled() {
            super();
            this.mNative = 0L;
            boolean z = false;
            this.mTotalRestarted = 0;
            java.lang.String str = com.android.server.utils.AnrTimer.this.mLabel;
            boolean z2 = com.android.server.utils.AnrTimer.this.mArgs.mExtend;
            if (com.android.server.utils.AnrTimer.this.mArgs.mFreeze && com.android.server.utils.AnrTimer.this.mArgs.mInjector.anrTimerFreezerEnabled()) {
                z = true;
            }
            this.mNative = com.android.server.utils.AnrTimer.this.nativeAnrTimerCreate(str, z2, z);
            if (this.mNative == 0) {
                throw new java.lang.IllegalArgumentException("unable to create native timer");
            }
            synchronized (com.android.server.utils.AnrTimer.sAnrTimerList) {
                com.android.server.utils.AnrTimer.sAnrTimerList.put(this.mNative, new java.lang.ref.WeakReference(com.android.server.utils.AnrTimer.this));
            }
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        void start(V arg, int pid, int uid, long timeoutMs) {
            synchronized (com.android.server.utils.AnrTimer.this.mLock) {
                if (cancel(arg)) {
                    this.mTotalRestarted++;
                }
                int timerId = com.android.server.utils.AnrTimer.nativeAnrTimerStart(this.mNative, pid, uid, timeoutMs);
                if (timerId > 0) {
                    com.android.server.utils.AnrTimer.this.mTimerIdMap.put(arg, java.lang.Integer.valueOf(timerId));
                    com.android.server.utils.AnrTimer.this.mTimerArgMap.put(timerId, arg);
                    com.android.server.utils.AnrTimer.this.mTotalStarted++;
                    com.android.server.utils.AnrTimer.this.mMaxStarted = java.lang.Math.max(com.android.server.utils.AnrTimer.this.mMaxStarted, com.android.server.utils.AnrTimer.this.mTimerIdMap.size());
                } else {
                    throw new java.lang.RuntimeException("unable to start timer");
                }
            }
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        boolean cancel(V arg) {
            synchronized (com.android.server.utils.AnrTimer.this.mLock) {
                java.lang.Integer timer = removeLocked(arg);
                if (timer == null) {
                    return false;
                }
                if (com.android.server.utils.AnrTimer.nativeAnrTimerCancel(this.mNative, timer.intValue())) {
                    return true;
                }
                com.android.server.utils.AnrTimer.this.mHandler.removeMessages(com.android.server.utils.AnrTimer.this.mWhat, arg);
                return false;
            }
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        com.android.server.utils.AnrTimer<V>.TimerLock accept(V arg) {
            synchronized (com.android.server.utils.AnrTimer.this.mLock) {
                java.lang.Integer timer = removeLocked(arg);
                if (timer == null) {
                    com.android.server.utils.AnrTimer.this.notFoundLocked("accept", arg);
                    return null;
                }
                boolean accepted = com.android.server.utils.AnrTimer.nativeAnrTimerAccept(this.mNative, timer.intValue());
                com.android.server.utils.AnrTimer.this.trace("accept", timer.intValue());
                return accepted ? new com.android.server.utils.AnrTimer.TimerLock(timer.intValue()) : null;
            }
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        boolean discard(V arg) {
            synchronized (com.android.server.utils.AnrTimer.this.mLock) {
                java.lang.Integer timer = removeLocked(arg);
                if (timer == null) {
                    com.android.server.utils.AnrTimer.this.notFoundLocked("discard", arg);
                    return false;
                }
                com.android.server.utils.AnrTimer.nativeAnrTimerDiscard(this.mNative, timer.intValue());
                com.android.server.utils.AnrTimer.this.trace("discard", timer.intValue());
                return true;
            }
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        void release(com.android.server.utils.AnrTimer<V>.TimerLock t) {
            if (t.mTimerId != 0 && !com.android.server.utils.AnrTimer.nativeAnrTimerRelease(this.mNative, t.mTimerId)) {
                android.util.Log.e(com.android.server.utils.AnrTimer.TAG, "failed to release id=" + t.mTimerId, new java.lang.Exception(com.android.server.utils.AnrTimer.TAG));
            }
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        boolean enabled() {
            return true;
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        void dump(android.util.IndentingPrintWriter pw, boolean verbose) {
            synchronized (com.android.server.utils.AnrTimer.this.mLock) {
                if (this.mNative == 0) {
                    pw.println("closed");
                    return;
                }
                java.lang.String[] nativeDump = com.android.server.utils.AnrTimer.nativeAnrTimerDump(this.mNative);
                if (nativeDump == null) {
                    pw.println("no-data");
                    return;
                }
                for (java.lang.String s : nativeDump) {
                    pw.println(s);
                }
                pw.println("restarted:" + this.mTotalRestarted);
            }
        }

        @Override // com.android.server.utils.AnrTimer.FeatureSwitch
        void close() {
            synchronized (com.android.server.utils.AnrTimer.sAnrTimerList) {
                com.android.server.utils.AnrTimer.sAnrTimerList.remove(this.mNative);
            }
            synchronized (com.android.server.utils.AnrTimer.this.mLock) {
                if (this.mNative != 0) {
                    com.android.server.utils.AnrTimer.nativeAnrTimerClose(this.mNative);
                }
                this.mNative = 0L;
            }
        }

        private java.lang.Integer removeLocked(V arg) {
            java.lang.Integer r = (java.lang.Integer) com.android.server.utils.AnrTimer.this.mTimerIdMap.remove(arg);
            if (r != null) {
                com.android.server.utils.AnrTimer.this.mTimerArgMap.remove(r.intValue());
            }
            return r;
        }
    }

    public void start(V arg, long timeoutMs) {
        if (timeoutMs < 0) {
            timeoutMs = 0;
        }
        this.mFeature.start(arg, getPid(arg), getUid(arg), timeoutMs);
    }

    public boolean cancel(V arg) {
        return this.mFeature.cancel(arg);
    }

    public com.android.server.utils.AnrTimer<V>.TimerLock accept(V arg) {
        return this.mFeature.accept(arg);
    }

    public boolean discard(V arg) {
        return this.mFeature.discard(arg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void release(com.android.server.utils.AnrTimer<V>.TimerLock t) {
        this.mFeature.release(t);
    }

    private boolean expire(int timerId, int pid, int uid, long elapsedMs) {
        trace("expired", timerId, pid, uid, elapsedMs);
        synchronized (this.mLock) {
            V arg = this.mTimerArgMap.get(timerId);
            if (arg != null) {
                this.mTotalExpired++;
                this.mHandler.sendMessage(android.os.Message.obtain(this.mHandler, this.mWhat, arg));
                return true;
            }
            android.util.Log.e(TAG, android.text.TextUtils.formatSimple("failed to expire timer %s:%d : arg not found", new java.lang.Object[]{this.mLabel, java.lang.Integer.valueOf(timerId)}));
            this.mTotalErrors++;
            return false;
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mFeature.close();
    }

    protected void finalize() throws java.lang.Throwable {
        close();
        super.finalize();
    }

    private void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.format("timer: %s\n", new java.lang.Object[]{this.mLabel});
            pw.increaseIndent();
            this.mFeature.dump(pw, false);
            pw.decreaseIndent();
        }
    }

    static void debug(boolean f) {
        DEBUG = f;
    }

    private static long now() {
        return android.os.SystemClock.uptimeMillis();
    }

    private static void dumpErrors(android.util.IndentingPrintWriter ipw) {
        synchronized (sErrors) {
            if (sErrors.size() == 0) {
                return;
            }
            com.android.server.utils.AnrTimer.Error[] errors = (com.android.server.utils.AnrTimer.Error[]) sErrors.toArray();
            ipw.println("Errors");
            ipw.increaseIndent();
            for (int i = 0; i < errors.length; i++) {
                if (errors[i] != null) {
                    errors[i].dump(ipw, i);
                }
            }
            ipw.decreaseIndent();
        }
    }

    private void recordErrorLocked(java.lang.String operation, java.lang.String errorMsg, java.lang.Object arg) {
        java.lang.StackTraceElement[] s = java.lang.Thread.currentThread().getStackTrace();
        java.lang.String what = java.util.Objects.toString(arg);
        java.lang.StackTraceElement[] location = (java.lang.StackTraceElement[]) java.util.Arrays.copyOfRange(s, 6, 9);
        synchronized (sErrors) {
            sErrors.append(new com.android.server.utils.AnrTimer.Error(errorMsg, operation, this.mLabel, location, what));
        }
        if (DEBUG) {
            android.util.Log.w(TAG, operation + " " + errorMsg + " " + this.mLabel + " timer " + what);
        }
        this.mTotalErrors++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notFoundLocked(java.lang.String operation, java.lang.Object arg) {
        recordErrorLocked(operation, "notFound", arg);
    }

    static void dump(java.io.PrintWriter pw, boolean verbose, com.android.server.utils.AnrTimer.Injector injector) {
        if (injector.anrTimerServiceEnabled()) {
            android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
            ipw.println("AnrTimer statistics");
            ipw.increaseIndent();
            synchronized (sAnrTimerList) {
                int size = sAnrTimerList.size();
                com.android.server.utils.AnrTimer[] active = new com.android.server.utils.AnrTimer[size];
                int valid = 0;
                for (int i = 0; i < size; i++) {
                    com.android.server.utils.AnrTimer a = sAnrTimerList.valueAt(i).get();
                    if (a != null) {
                        active[valid] = a;
                        valid++;
                    }
                }
                java.util.Arrays.sort(active, 0, valid, sComparator);
                for (int i2 = 0; i2 < valid; i2++) {
                    if (active[i2] != null) {
                        active[i2].dump(ipw);
                    }
                }
            }
            if (verbose) {
                dumpErrors(ipw);
            }
            ipw.format("AnrTimerEnd\n", new java.lang.Object[0]);
            ipw.decreaseIndent();
        }
    }

    public static void dump(java.io.PrintWriter pw, boolean verbose) {
        dump(pw, verbose, sDefaultInjector);
    }

    public static boolean nativeTimersSupported() {
        try {
            return nativeAnrTimerSupported();
        } catch (java.lang.UnsatisfiedLinkError e) {
            return false;
        }
    }
}
