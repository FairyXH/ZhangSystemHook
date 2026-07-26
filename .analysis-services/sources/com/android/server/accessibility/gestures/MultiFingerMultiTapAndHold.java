package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
public class MultiFingerMultiTapAndHold extends com.android.server.accessibility.gestures.MultiFingerMultiTap {
    public MultiFingerMultiTapAndHold(android.content.Context context, int fingers, int taps, int gestureId, com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        super(context, fingers, taps, gestureId, listener);
    }

    @Override // com.android.server.accessibility.gestures.MultiFingerMultiTap, com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        super.onPointerDown(event, rawEvent, policyFlags);
        if (this.mIsTargetFingerCountReached && this.mCompletedTapCount + 1 == this.mTargetTapCount) {
            completeAfterLongPressTimeout(event, rawEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.gestures.MultiFingerMultiTap, com.android.server.accessibility.gestures.GestureMatcher
    protected void onUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mCompletedTapCount + 1 == this.mTargetTapCount) {
            cancelGesture(event, rawEvent, policyFlags);
        } else {
            super.onUp(event, rawEvent, policyFlags);
            cancelAfterDoubleTapTimeout(event, rawEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.gestures.MultiFingerMultiTap, com.android.server.accessibility.gestures.GestureMatcher
    public java.lang.String getGestureName() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append(this.mTargetFingerCount).append("-Finger ");
        if (this.mTargetTapCount == 1) {
            builder.append("Single");
        } else if (this.mTargetTapCount == 2) {
            builder.append("Double");
        } else if (this.mTargetTapCount == 3) {
            builder.append("Triple");
        } else if (this.mTargetTapCount > 3) {
            builder.append(this.mTargetTapCount);
        }
        return builder.append(" Tap and hold").toString();
    }
}
