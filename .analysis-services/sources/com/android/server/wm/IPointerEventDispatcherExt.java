package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IPointerEventDispatcherExt {
    default void debugInputEventDuration(android.view.MotionEvent motionEvent, android.view.WindowManagerPolicyConstants.PointerEventListener listener, long startTime) {
    }

    default android.os.Looper getOptLooper(android.os.Looper defaultLooper) {
        return defaultLooper;
    }
}
