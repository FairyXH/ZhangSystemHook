package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface ISingleKeyGestureDetectorExt {
    default void endHookInterceptKeyUp() {
    }

    default long modifyPressTimeout(int pressType, long veryLongPressTimeout, android.view.KeyEvent event) {
        return veryLongPressTimeout;
    }
}
