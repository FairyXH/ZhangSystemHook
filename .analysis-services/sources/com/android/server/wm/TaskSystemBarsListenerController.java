package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class TaskSystemBarsListenerController {
    private final java.util.HashSet<com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener> mListeners = new java.util.HashSet<>();
    private final java.util.concurrent.Executor mBackgroundExecutor = com.android.internal.os.BackgroundThread.getExecutor();

    TaskSystemBarsListenerController() {
    }

    void registerListener(com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener listener) {
        this.mListeners.add(listener);
    }

    void unregisterListener(com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener listener) {
        this.mListeners.remove(listener);
    }

    void dispatchTransientSystemBarVisibilityChanged(final int taskId, final boolean visible, final boolean wereRevealedFromSwipeOnSystemBar) {
        final java.util.HashSet<com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener> localListeners = new java.util.HashSet<>(this.mListeners);
        this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.wm.TaskSystemBarsListenerController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.wm.TaskSystemBarsListenerController.lambda$dispatchTransientSystemBarVisibilityChanged$0(localListeners, taskId, visible, wereRevealedFromSwipeOnSystemBar);
            }
        });
    }

    static /* synthetic */ void lambda$dispatchTransientSystemBarVisibilityChanged$0(java.util.HashSet localListeners, int taskId, boolean visible, boolean wereRevealedFromSwipeOnSystemBar) {
        java.util.Iterator it = localListeners.iterator();
        while (it.hasNext()) {
            com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener listener = (com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener) it.next();
            listener.onTransientSystemBarsVisibilityChanged(taskId, visible, wereRevealedFromSwipeOnSystemBar);
        }
    }
}
