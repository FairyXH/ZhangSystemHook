package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public interface EventStreamTransformation {
    com.android.server.accessibility.EventStreamTransformation getNext();

    void setNext(com.android.server.accessibility.EventStreamTransformation eventStreamTransformation);

    default void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        com.android.server.accessibility.EventStreamTransformation next = getNext();
        if (next != null) {
            next.onMotionEvent(event, rawEvent, policyFlags);
        }
    }

    default void onKeyEvent(android.view.KeyEvent event, int policyFlags) {
        com.android.server.accessibility.EventStreamTransformation next = getNext();
        if (next != null) {
            next.onKeyEvent(event, policyFlags);
        }
    }

    default void onAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        com.android.server.accessibility.EventStreamTransformation next = getNext();
        if (next != null) {
            next.onAccessibilityEvent(event);
        }
    }

    default void clearEvents(int inputSource) {
        com.android.server.accessibility.EventStreamTransformation next = getNext();
        if (next != null) {
            next.clearEvents(inputSource);
        }
    }

    default void onDestroy() {
    }
}
