package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public final class GesturesObserver implements com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener {
    private final com.android.server.accessibility.magnification.GesturesObserver.Listener mListener;
    private final java.util.List<com.android.server.accessibility.gestures.GestureMatcher> mGestureMatchers = new java.util.ArrayList();
    private boolean mObserveStarted = false;
    private boolean mProcessMotionEvent = false;
    private int mCancelledMatcherSize = 0;

    public interface Listener {
        void onGestureCancelled(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i);

        void onGestureCompleted(int i, android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i2);
    }

    public GesturesObserver(com.android.server.accessibility.magnification.GesturesObserver.Listener listener, com.android.server.accessibility.gestures.GestureMatcher... matchers) {
        this.mListener = listener;
        for (int i = 0; i < matchers.length; i++) {
            matchers[i].setListener(this);
            this.mGestureMatchers.add(matchers[i]);
        }
    }

    public boolean onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (!this.mObserveStarted) {
            if (event.getActionMasked() != 0) {
                this.mListener.onGestureCancelled(event, rawEvent, policyFlags);
                clear();
                return false;
            }
            this.mObserveStarted = true;
        }
        this.mProcessMotionEvent = true;
        for (int i = 0; i < this.mGestureMatchers.size(); i++) {
            com.android.server.accessibility.gestures.GestureMatcher matcher = this.mGestureMatchers.get(i);
            matcher.onMotionEvent(event, rawEvent, policyFlags);
            if (matcher.getState() == 2) {
                clear();
                this.mProcessMotionEvent = false;
                return true;
            }
        }
        this.mProcessMotionEvent = false;
        return false;
    }

    private void clear() {
        for (com.android.server.accessibility.gestures.GestureMatcher matcher : this.mGestureMatchers) {
            matcher.clear();
        }
        this.mCancelledMatcherSize = 0;
        this.mObserveStarted = false;
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener
    public void onStateChanged(int gestureId, int state, android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (state == 2) {
            this.mListener.onGestureCompleted(gestureId, event, rawEvent, policyFlags);
            if (!this.mProcessMotionEvent) {
                clear();
                return;
            }
            return;
        }
        if (state == 3) {
            this.mCancelledMatcherSize++;
            if (this.mCancelledMatcherSize == this.mGestureMatchers.size()) {
                this.mListener.onGestureCancelled(event, rawEvent, policyFlags);
                clear();
            }
        }
    }
}
