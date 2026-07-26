package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
public class MultiTapAndHold extends com.android.server.accessibility.gestures.MultiTap {
    public MultiTapAndHold(android.content.Context context, int taps, int gesture, com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        super(context, taps, gesture, listener);
    }

    @Override // com.android.server.accessibility.gestures.MultiTap, com.android.server.accessibility.gestures.GestureMatcher
    protected void onDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        super.onDown(event, rawEvent, policyFlags);
        if (this.mCurrentTaps + 1 == this.mTargetTaps) {
            completeAfterLongPressTimeout(event, rawEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.gestures.MultiTap, com.android.server.accessibility.gestures.GestureMatcher
    protected void onUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        super.onUp(event, rawEvent, policyFlags);
        cancelAfterDoubleTapTimeout(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.MultiTap, com.android.server.accessibility.gestures.GestureMatcher
    public java.lang.String getGestureName() {
        switch (this.mTargetTaps) {
            case 2:
                return "Double Tap and Hold";
            case 3:
                return "Triple Tap and Hold";
            default:
                return java.lang.Integer.toString(this.mTargetTaps) + " Taps and Hold";
        }
    }
}
