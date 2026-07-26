package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public class ConcurrentUtils {
    public static final java.util.concurrent.Executor DIRECT_EXECUTOR = new com.android.internal.util.jobs.ConcurrentUtils.DirectExecutor();

    private ConcurrentUtils() {
    }

    public static java.util.concurrent.ExecutorService newFixedThreadPool(int nThreads, final java.lang.String poolName, final int linuxThreadPriority) {
        return java.util.concurrent.Executors.newFixedThreadPool(nThreads, new java.util.concurrent.ThreadFactory() { // from class: com.android.internal.util.jobs.ConcurrentUtils.1
            private final java.util.concurrent.atomic.AtomicInteger threadNum = new java.util.concurrent.atomic.AtomicInteger(0);

            @Override // java.util.concurrent.ThreadFactory
            public java.lang.Thread newThread(final java.lang.Runnable r) {
                return new java.lang.Thread(poolName + this.threadNum.incrementAndGet()) { // from class: com.android.internal.util.jobs.ConcurrentUtils.1.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        android.os.Process.setThreadPriority(linuxThreadPriority);
                        r.run();
                    }
                };
            }
        });
    }

    public static <T> T waitForFutureNoInterrupt(java.util.concurrent.Future<T> future, java.lang.String description) {
        try {
            return future.get();
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            throw new java.lang.IllegalStateException(description + " interrupted");
        } catch (java.util.concurrent.ExecutionException e2) {
            throw new java.lang.RuntimeException(description + " failed", e2);
        }
    }

    public static void waitForCountDownNoInterrupt(java.util.concurrent.CountDownLatch countDownLatch, long timeoutMs, java.lang.String description) {
        try {
            if (!countDownLatch.await(timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                throw new java.lang.IllegalStateException(description + " timed out.");
            }
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            throw new java.lang.IllegalStateException(description + " interrupted.");
        }
    }

    public static void wtfIfLockHeld(java.lang.String tag, java.lang.Object lock) {
        if (java.lang.Thread.holdsLock(lock)) {
            android.util.Slog.wtf(tag, "Lock mustn't be held");
        }
    }

    public static void wtfIfLockNotHeld(java.lang.String tag, java.lang.Object lock) {
        if (!java.lang.Thread.holdsLock(lock)) {
            android.util.Slog.wtf(tag, "Lock must be held");
        }
    }

    private static class DirectExecutor implements java.util.concurrent.Executor {
        private DirectExecutor() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(java.lang.Runnable command) {
            command.run();
        }

        public java.lang.String toString() {
            return "DIRECT_EXECUTOR";
        }
    }
}
