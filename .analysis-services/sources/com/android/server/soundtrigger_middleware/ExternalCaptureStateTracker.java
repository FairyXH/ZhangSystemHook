package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
class ExternalCaptureStateTracker implements com.android.server.soundtrigger_middleware.ICaptureStateNotifier {
    private static final java.lang.String TAG = "CaptureStateTracker";
    private final java.util.List<com.android.server.soundtrigger_middleware.ICaptureStateNotifier.Listener> mListeners = new java.util.LinkedList();
    private boolean mCaptureActive = true;
    private final java.util.concurrent.Semaphore mNeedToConnect = new java.util.concurrent.Semaphore(1);

    private native void connect();

    ExternalCaptureStateTracker() {
        new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.ExternalCaptureStateTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.run();
            }
        }).start();
    }

    @Override // com.android.server.soundtrigger_middleware.ICaptureStateNotifier
    public boolean registerListener(com.android.server.soundtrigger_middleware.ICaptureStateNotifier.Listener listener) {
        boolean z;
        synchronized (this.mListeners) {
            this.mListeners.add(listener);
            z = this.mCaptureActive;
        }
        return z;
    }

    @Override // com.android.server.soundtrigger_middleware.ICaptureStateNotifier
    public void unregisterListener(com.android.server.soundtrigger_middleware.ICaptureStateNotifier.Listener listener) {
        synchronized (this.mListeners) {
            this.mListeners.remove(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void run() {
        while (true) {
            this.mNeedToConnect.acquireUninterruptibly();
            connect();
        }
    }

    private void setCaptureState(boolean active) {
        try {
            synchronized (this.mListeners) {
                this.mCaptureActive = active;
                for (com.android.server.soundtrigger_middleware.ICaptureStateNotifier.Listener listener : this.mListeners) {
                    listener.onCaptureStateChange(active);
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Exception caught while setting capture state", e);
        }
    }

    private void binderDied() {
        android.util.Slog.w(TAG, "Audio policy service died");
        this.mNeedToConnect.release();
    }
}
