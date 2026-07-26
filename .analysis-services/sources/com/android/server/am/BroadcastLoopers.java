package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class BroadcastLoopers {
    private static final java.lang.String TAG = "BroadcastLoopers";
    private static final android.util.ArraySet<android.os.Looper> sLoopers = new android.util.ArraySet<>();

    public static void addLooper(android.os.Looper looper) {
        synchronized (sLoopers) {
            sLoopers.add((android.os.Looper) java.util.Objects.requireNonNull(looper));
        }
    }

    public static void addMyLooper() {
        android.os.Looper looper = android.os.Looper.myLooper();
        if (looper != null) {
            synchronized (sLoopers) {
                if (sLoopers.add(looper)) {
                    android.util.Slog.w(TAG, "Found previously unknown looper " + looper.getThread());
                }
            }
        }
    }

    public static void waitForIdle(java.io.PrintWriter pw) {
        waitForCondition(pw, new java.util.function.BiConsumer() { // from class: com.android.server.am.BroadcastLoopers$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.am.BroadcastLoopers.lambda$waitForIdle$1((android.os.Looper) obj, (java.util.concurrent.CountDownLatch) obj2);
            }
        });
    }

    static /* synthetic */ void lambda$waitForIdle$1(android.os.Looper looper, final java.util.concurrent.CountDownLatch latch) {
        android.os.MessageQueue queue = looper.getQueue();
        queue.addIdleHandler(new android.os.MessageQueue.IdleHandler() { // from class: com.android.server.am.BroadcastLoopers$$ExternalSyntheticLambda0
            @Override // android.os.MessageQueue.IdleHandler
            public final boolean queueIdle() {
                return com.android.server.am.BroadcastLoopers.lambda$waitForIdle$0(latch);
            }
        });
    }

    static /* synthetic */ boolean lambda$waitForIdle$0(java.util.concurrent.CountDownLatch latch) {
        latch.countDown();
        return false;
    }

    public static void waitForBarrier(java.io.PrintWriter pw) {
        waitForCondition(pw, new java.util.function.BiConsumer() { // from class: com.android.server.am.BroadcastLoopers$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                new android.os.Handler((android.os.Looper) obj).post(new java.lang.Runnable() { // from class: com.android.server.am.BroadcastLoopers$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        countDownLatch.countDown();
                    }
                });
            }
        });
    }

    private static void waitForCondition(java.io.PrintWriter pw, java.util.function.BiConsumer<android.os.Looper, java.util.concurrent.CountDownLatch> condition) {
        java.util.concurrent.CountDownLatch latch;
        synchronized (sLoopers) {
            int N = sLoopers.size();
            latch = new java.util.concurrent.CountDownLatch(N);
            for (int i = 0; i < N; i++) {
                android.os.Looper looper = sLoopers.valueAt(i);
                android.os.MessageQueue queue = looper.getQueue();
                if (queue.isIdle()) {
                    latch.countDown();
                } else {
                    condition.accept(looper, latch);
                }
            }
        }
        long lastPrint = 0;
        while (latch.getCount() > 0) {
            long now = android.os.SystemClock.uptimeMillis();
            if (now >= 1000 + lastPrint) {
                lastPrint = now;
                pw.println("Waiting for " + latch.getCount() + " loopers to drain...");
                pw.flush();
            }
            android.os.SystemClock.sleep(100L);
        }
        pw.println("Loopers drained!");
        pw.flush();
    }
}
