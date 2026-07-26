package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class RemoteDisplayChangeController {
    private static final int REMOTE_DISPLAY_CHANGE_TIMEOUT_MS = 800;
    private static final java.lang.String REMOTE_DISPLAY_CHANGE_TRACE_TAG = "RemoteDisplayChange";
    private static final java.lang.String TAG = "RemoteDisplayChangeController";
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private final com.android.server.wm.WindowManagerService mService;
    private final java.lang.Runnable mTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.RemoteDisplayChangeController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.onContinueTimedOut();
        }
    };
    private final java.util.List<com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback> mCallbacks = new java.util.ArrayList();

    public interface ContinueRemoteDisplayChangeCallback {
        void onContinueRemoteDisplayChange(android.window.WindowContainerTransaction windowContainerTransaction);
    }

    RemoteDisplayChangeController(com.android.server.wm.DisplayContent displayContent) {
        this.mService = displayContent.mWmService;
        this.mDisplayContent = displayContent;
    }

    public boolean isWaitingForRemoteDisplayChange() {
        return !this.mCallbacks.isEmpty();
    }

    public boolean performRemoteDisplayChange(int fromRotation, int toRotation, android.window.DisplayAreaInfo newDisplayAreaInfo, com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback callback) {
        if (this.mService.mDisplayChangeController == null) {
            return false;
        }
        this.mCallbacks.add(callback);
        if (android.os.Trace.isTagEnabled(32L)) {
            android.os.Trace.beginAsyncSection(REMOTE_DISPLAY_CHANGE_TRACE_TAG, callback.hashCode());
        }
        if (newDisplayAreaInfo != null && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
            long protoLogParam0 = fromRotation;
            long protoLogParam1 = newDisplayAreaInfo.configuration.windowConfiguration.getMaxBounds().width();
            long protoLogParam2 = newDisplayAreaInfo.configuration.windowConfiguration.getMaxBounds().height();
            long protoLogParam3 = toRotation;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, 1736084564226683342L, 85, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2), java.lang.Long.valueOf(protoLogParam3));
        }
        android.view.IDisplayChangeWindowCallback remoteCallback = createCallback(callback);
        try {
            this.mService.mDisplayChangeController.onDisplayChange(this.mDisplayContent.mDisplayId, fromRotation, toRotation, newDisplayAreaInfo, remoteCallback);
            this.mService.mH.postDelayed(this.mTimeoutRunnable, callback, 800L);
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Exception while dispatching remote display-change", e);
            this.mCallbacks.remove(callback);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onContinueTimedOut() {
        android.util.Slog.e(TAG, "RemoteDisplayChange timed-out, UI might get messed-up after this.");
        this.mService.mH.removeCallbacks(this.mTimeoutRunnable);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                try {
                    com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback callback = this.mCallbacks.get(i);
                    if (i == this.mCallbacks.size() - 1) {
                        this.mCallbacks.clear();
                    }
                    callback.onContinueRemoteDisplayChange(null);
                    if (android.os.Trace.isTagEnabled(32L)) {
                        android.os.Trace.endAsyncSection(REMOTE_DISPLAY_CHANGE_TRACE_TAG, callback.hashCode());
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            onCompleted();
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    private void onCompleted() {
        if (this.mDisplayContent.mWaitingForConfig) {
            this.mDisplayContent.sendNewConfiguration();
        }
    }

    void continueDisplayChange(com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback callback, android.window.WindowContainerTransaction transaction) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int idx = this.mCallbacks.indexOf(callback);
                if (idx < 0) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                for (int i = 0; i < idx; i++) {
                    com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback currentCallback = this.mCallbacks.get(i);
                    currentCallback.onContinueRemoteDisplayChange(null);
                    if (android.os.Trace.isTagEnabled(32L)) {
                        android.os.Trace.endAsyncSection(REMOTE_DISPLAY_CHANGE_TRACE_TAG, currentCallback.hashCode());
                    }
                }
                this.mCallbacks.subList(0, idx + 1).clear();
                boolean completed = this.mCallbacks.isEmpty();
                callback.onContinueRemoteDisplayChange(transaction);
                if (completed) {
                    onCompleted();
                }
                if (android.os.Trace.isTagEnabled(32L)) {
                    android.os.Trace.endAsyncSection(REMOTE_DISPLAY_CHANGE_TRACE_TAG, callback.hashCode());
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.wm.RemoteDisplayChangeController$1, reason: invalid class name */
    class AnonymousClass1 extends android.view.IDisplayChangeWindowCallback.Stub {
        final /* synthetic */ com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback val$callback;

        AnonymousClass1(com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback continueRemoteDisplayChangeCallback) {
            this.val$callback = continueRemoteDisplayChangeCallback;
        }

        public void continueDisplayChange(final android.window.WindowContainerTransaction t) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.RemoteDisplayChangeController.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (!com.android.server.wm.RemoteDisplayChangeController.this.mCallbacks.contains(this.val$callback)) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.WindowManagerService.H h = com.android.server.wm.RemoteDisplayChangeController.this.mService.mH;
                    final com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback continueRemoteDisplayChangeCallback = this.val$callback;
                    h.post(new java.lang.Runnable() { // from class: com.android.server.wm.RemoteDisplayChangeController$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$continueDisplayChange$0(continueRemoteDisplayChangeCallback, t);
                        }
                    });
                    com.android.server.wm.RemoteDisplayChangeController.this.mService.mH.removeCallbacks(com.android.server.wm.RemoteDisplayChangeController.this.mTimeoutRunnable, this.val$callback);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$continueDisplayChange$0(com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback callback, android.window.WindowContainerTransaction t) {
            com.android.server.wm.RemoteDisplayChangeController.this.continueDisplayChange(callback, t);
        }
    }

    private android.view.IDisplayChangeWindowCallback createCallback(com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback callback) {
        return new com.android.server.wm.RemoteDisplayChangeController.AnonymousClass1(callback);
    }
}
