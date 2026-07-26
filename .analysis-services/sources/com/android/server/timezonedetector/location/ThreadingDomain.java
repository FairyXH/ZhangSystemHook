package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
abstract class ThreadingDomain {
    private final java.lang.Object mLockObject = new java.lang.Object();

    abstract java.lang.Thread getThread();

    abstract void post(java.lang.Runnable runnable);

    abstract <V> V postAndWait(java.util.concurrent.Callable<V> callable, long j) throws java.lang.Exception;

    abstract void postDelayed(java.lang.Runnable runnable, long j);

    abstract void postDelayed(java.lang.Runnable runnable, java.lang.Object obj, long j);

    abstract void removeQueuedRunnables(java.lang.Object obj);

    ThreadingDomain() {
    }

    java.lang.Object getLockObject() {
        return this.mLockObject;
    }

    void assertCurrentThread() {
        com.android.internal.util.Preconditions.checkState(java.lang.Thread.currentThread() == getThread());
    }

    void assertNotCurrentThread() {
        com.android.internal.util.Preconditions.checkState(java.lang.Thread.currentThread() != getThread());
    }

    final void postAndWait(final java.lang.Runnable runnable, long durationMillis) {
        try {
            postAndWait(new java.util.concurrent.Callable() { // from class: com.android.server.timezonedetector.location.ThreadingDomain$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return com.android.server.timezonedetector.location.ThreadingDomain.lambda$postAndWait$0(runnable);
                }
            }, durationMillis);
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    static /* synthetic */ java.lang.Object lambda$postAndWait$0(java.lang.Runnable runnable) throws java.lang.Exception {
        runnable.run();
        return null;
    }

    com.android.server.timezonedetector.location.ThreadingDomain.SingleRunnableQueue createSingleRunnableQueue() {
        return new com.android.server.timezonedetector.location.ThreadingDomain.SingleRunnableQueue();
    }

    final class SingleRunnableQueue {
        private long mDelayMillis;
        private boolean mIsQueued;

        SingleRunnableQueue() {
        }

        void runDelayed(final java.lang.Runnable r, long delayMillis) {
            cancel();
            this.mIsQueued = true;
            this.mDelayMillis = delayMillis;
            com.android.server.timezonedetector.location.ThreadingDomain.this.postDelayed(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.ThreadingDomain$SingleRunnableQueue$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$runDelayed$0(r);
                }
            }, this, delayMillis);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$runDelayed$0(java.lang.Runnable r) {
            this.mIsQueued = false;
            this.mDelayMillis = -2L;
            r.run();
        }

        boolean hasQueued() {
            com.android.server.timezonedetector.location.ThreadingDomain.this.assertCurrentThread();
            return this.mIsQueued;
        }

        long getQueuedDelayMillis() {
            com.android.server.timezonedetector.location.ThreadingDomain.this.assertCurrentThread();
            if (!this.mIsQueued) {
                throw new java.lang.IllegalStateException("No item queued");
            }
            return this.mDelayMillis;
        }

        public void cancel() {
            com.android.server.timezonedetector.location.ThreadingDomain.this.assertCurrentThread();
            if (this.mIsQueued) {
                com.android.server.timezonedetector.location.ThreadingDomain.this.removeQueuedRunnables(this);
            }
            this.mIsQueued = false;
            this.mDelayMillis = -1L;
        }
    }
}
