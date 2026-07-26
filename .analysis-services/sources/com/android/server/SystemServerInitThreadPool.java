package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class SystemServerInitThreadPool implements android.util.Dumpable {
    private static final int SHUTDOWN_TIMEOUT_MILLIS = 20000;
    private static com.android.server.SystemServerInitThreadPool sInstance;
    private final java.util.concurrent.ExecutorService mService;
    private boolean mShutDown;
    private static final java.lang.String TAG = com.android.server.SystemServerInitThreadPool.class.getSimpleName();
    private static final boolean IS_DEBUGGABLE = android.os.Build.IS_DEBUGGABLE;
    private static final java.lang.Object LOCK = new java.lang.Object();
    private final java.util.List<java.lang.String> mPendingTasks = new java.util.ArrayList();
    private final int mSize = java.lang.Runtime.getRuntime().availableProcessors();

    private SystemServerInitThreadPool() {
        android.util.Slog.i(TAG, "Creating instance with " + this.mSize + " threads");
        this.mService = com.android.internal.util.ConcurrentUtils.newFixedThreadPool(this.mSize, "system-server-init-thread", -2);
    }

    public static java.util.concurrent.Future<?> submit(java.lang.Runnable runnable, java.lang.String description) {
        com.android.server.SystemServerInitThreadPool instance;
        java.util.Objects.requireNonNull(description, "description cannot be null");
        synchronized (LOCK) {
            com.android.internal.util.Preconditions.checkState(sInstance != null, "Cannot get " + TAG + " - it has been shut down");
            instance = sInstance;
        }
        return instance.submitTask(runnable, description);
    }

    private java.util.concurrent.Future<?> submitTask(final java.lang.Runnable runnable, final java.lang.String description) {
        synchronized (this.mPendingTasks) {
            com.android.internal.util.Preconditions.checkState(!this.mShutDown, TAG + " already shut down");
            this.mPendingTasks.add(description);
        }
        return this.mService.submit(new java.lang.Runnable() { // from class: com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$submitTask$0(description, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$submitTask$0(java.lang.String description, java.lang.Runnable runnable) {
        com.android.server.utils.TimingsTraceAndSlog traceLog = com.android.server.utils.TimingsTraceAndSlog.newAsyncLog();
        traceLog.traceBegin("InitThreadPoolExec:" + description);
        if (IS_DEBUGGABLE) {
            android.util.Slog.d(TAG, "Started executing " + description);
        }
        try {
            runnable.run();
            synchronized (this.mPendingTasks) {
                this.mPendingTasks.remove(description);
            }
            if (IS_DEBUGGABLE) {
                android.util.Slog.d(TAG, "Finished executing " + description);
            }
            traceLog.traceEnd();
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.e(TAG, "Failure in " + description + ": " + e, e);
            traceLog.traceEnd();
            throw e;
        }
    }

    static com.android.server.SystemServerInitThreadPool start() {
        com.android.server.SystemServerInitThreadPool instance;
        synchronized (LOCK) {
            com.android.internal.util.Preconditions.checkState(sInstance == null, TAG + " already started");
            instance = new com.android.server.SystemServerInitThreadPool();
            sInstance = instance;
        }
        return instance;
    }

    static void shutdown() {
        android.util.Slog.d(TAG, "Shutdown requested");
        synchronized (LOCK) {
            com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
            t.traceBegin("WaitInitThreadPoolShutdown");
            if (sInstance == null) {
                t.traceEnd();
                android.util.Slog.wtf(TAG, "Already shutdown", new java.lang.Exception());
                return;
            }
            synchronized (sInstance.mPendingTasks) {
                sInstance.mShutDown = true;
            }
            sInstance.mService.shutdown();
            try {
                boolean terminated = sInstance.mService.awaitTermination(20000L, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (!terminated) {
                    dumpStackTraces();
                }
                java.util.List<java.lang.Runnable> unstartedRunnables = sInstance.mService.shutdownNow();
                if (!terminated) {
                    java.util.List<java.lang.String> copy = new java.util.ArrayList<>();
                    synchronized (sInstance.mPendingTasks) {
                        copy.addAll(sInstance.mPendingTasks);
                    }
                    t.traceEnd();
                    throw new java.lang.IllegalStateException("Cannot shutdown. Unstarted tasks " + unstartedRunnables + " Unfinished tasks " + copy);
                }
                sInstance = null;
                android.util.Slog.d(TAG, "Shutdown successful");
                t.traceEnd();
            } catch (java.lang.InterruptedException e) {
                java.lang.Thread.currentThread().interrupt();
                dumpStackTraces();
                t.traceEnd();
                throw new java.lang.IllegalStateException(TAG + " init interrupted");
            }
        }
    }

    private static void dumpStackTraces() {
        java.util.ArrayList<java.lang.Integer> pids = new java.util.ArrayList<>();
        pids.add(java.lang.Integer.valueOf(android.os.Process.myPid()));
        com.android.server.am.StackTracesDumpHelper.dumpStackTraces(pids, null, null, java.util.concurrent.CompletableFuture.completedFuture(com.android.server.Watchdog.getInterestingNativePids()), null, null, null, new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), null);
    }

    @Override // android.util.Dumpable
    public java.lang.String getDumpableName() {
        return com.android.server.SystemServerInitThreadPool.class.getSimpleName();
    }

    @Override // android.util.Dumpable
    public void dump(java.io.PrintWriter pw, java.lang.String[] args) {
        synchronized (LOCK) {
            pw.printf("has instance: %b\n", java.lang.Boolean.valueOf(sInstance != null));
        }
        pw.printf("number of threads: %d\n", java.lang.Integer.valueOf(this.mSize));
        pw.printf("service: %s\n", this.mService);
        synchronized (this.mPendingTasks) {
            pw.printf("is shutdown: %b\n", java.lang.Boolean.valueOf(this.mShutDown));
            int pendingTasks = this.mPendingTasks.size();
            if (pendingTasks == 0) {
                pw.println("no pending tasks");
            } else {
                pw.printf("%d pending tasks: %s\n", java.lang.Integer.valueOf(pendingTasks), this.mPendingTasks);
            }
        }
    }
}
