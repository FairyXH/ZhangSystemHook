package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
final class HandlerThreadingDomain extends com.android.server.timezonedetector.location.ThreadingDomain {
    private final android.os.Handler mHandler;

    HandlerThreadingDomain(android.os.Handler handler) {
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
    }

    android.os.Handler getHandler() {
        return this.mHandler;
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    java.lang.Thread getThread() {
        return getHandler().getLooper().getThread();
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    void post(java.lang.Runnable r) {
        getHandler().post(r);
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    <V> V postAndWait(final java.util.concurrent.Callable<V> callable, long durationMillis) throws java.lang.Exception {
        assertNotCurrentThread();
        final java.util.concurrent.atomic.AtomicReference<V> resultReference = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<java.lang.Exception> exceptionReference = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        post(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.HandlerThreadingDomain$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.timezonedetector.location.HandlerThreadingDomain.lambda$postAndWait$0(resultReference, callable, exceptionReference, latch);
            }
        });
        try {
            if (!latch.await(durationMillis, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                throw new java.lang.RuntimeException("Timed out");
            }
            if (exceptionReference.get() != null) {
                throw exceptionReference.get();
            }
            return resultReference.get();
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    static /* synthetic */ void lambda$postAndWait$0(java.util.concurrent.atomic.AtomicReference resultReference, java.util.concurrent.Callable callable, java.util.concurrent.atomic.AtomicReference exceptionReference, java.util.concurrent.CountDownLatch latch) {
        try {
            try {
                resultReference.set(callable.call());
            } catch (java.lang.Exception e) {
                exceptionReference.set(e);
            }
        } finally {
            latch.countDown();
        }
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    void postDelayed(java.lang.Runnable r, long delayMillis) {
        getHandler().postDelayed(r, delayMillis);
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    void postDelayed(java.lang.Runnable r, java.lang.Object token, long delayMillis) {
        getHandler().postDelayed(r, token, delayMillis);
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    void removeQueuedRunnables(java.lang.Object token) {
        getHandler().removeCallbacksAndMessages(token);
    }
}
