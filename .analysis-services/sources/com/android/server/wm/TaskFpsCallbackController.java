package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class TaskFpsCallbackController {
    private final android.content.Context mContext;
    private final java.util.HashMap<android.os.IBinder, java.lang.Long> mTaskFpsCallbacks = new java.util.HashMap<>();
    private final java.util.HashMap<android.os.IBinder, android.os.IBinder.DeathRecipient> mDeathRecipients = new java.util.HashMap<>();

    private static native long nativeRegister(android.window.ITaskFpsCallback iTaskFpsCallback, int i);

    private static native void nativeUnregister(long j);

    TaskFpsCallbackController(android.content.Context context) {
        this.mContext = context;
    }

    void registerListener(int taskId, final android.window.ITaskFpsCallback callback) {
        if (callback == null) {
            return;
        }
        android.os.IBinder binder = callback.asBinder();
        if (this.mTaskFpsCallbacks.containsKey(binder)) {
            return;
        }
        long nativeListener = nativeRegister(callback, taskId);
        this.mTaskFpsCallbacks.put(binder, java.lang.Long.valueOf(nativeListener));
        android.os.IBinder.DeathRecipient deathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wm.TaskFpsCallbackController$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                this.f$0.lambda$registerListener$0(callback);
            }
        };
        try {
            binder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipients.put(binder, deathRecipient);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: unregisterListener, reason: merged with bridge method [inline-methods] */
    public void lambda$registerListener$0(android.window.ITaskFpsCallback callback) {
        if (callback == null) {
            return;
        }
        android.os.IBinder binder = callback.asBinder();
        if (!this.mTaskFpsCallbacks.containsKey(binder)) {
            return;
        }
        binder.unlinkToDeath(this.mDeathRecipients.get(binder), 0);
        this.mDeathRecipients.remove(binder);
        nativeUnregister(this.mTaskFpsCallbacks.get(binder).longValue());
        this.mTaskFpsCallbacks.remove(binder);
    }
}
