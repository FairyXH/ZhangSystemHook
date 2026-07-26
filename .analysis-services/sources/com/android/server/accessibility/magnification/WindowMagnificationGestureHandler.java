package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class WindowMagnificationGestureHandler extends com.android.server.accessibility.magnification.MagnificationGestureHandler {
    private static final float MIN_SCALE = 1.0f;
    private final android.content.Context mContext;
    com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State mCurrentState;
    final com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.DelegatingState mDelegatingState;
    final com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.DetectingState mDetectingState;
    private final com.android.server.accessibility.magnification.MagnificationConnectionManager mMagnificationConnectionManager;
    private com.android.server.accessibility.magnification.MotionEventDispatcherDelegate mMotionEventDispatcherDelegate;
    final com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.PanningScalingGestureState mObservePanningScalingState;
    com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State mPreviousState;
    private final android.graphics.Point mTempPoint;
    private long mTripleTapAndHoldStartedTime;
    final com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.ViewportDraggingState mViewportDraggingState;
    private static final boolean DEBUG_STATE_TRANSITIONS = DEBUG_ALL | false;
    private static final boolean DEBUG_DETECTING = DEBUG_ALL | false;
    private static final float MAX_SCALE = com.android.server.accessibility.magnification.MagnificationScaleProvider.MAX_SCALE;

    public WindowMagnificationGestureHandler(android.content.Context context, com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager, com.android.server.accessibility.AccessibilityTraceManager trace, com.android.server.accessibility.magnification.MagnificationGestureHandler.Callback callback, boolean detectSingleFingerTripleTap, boolean detectTwoFingerTripleTap, boolean detectShortcutTrigger, int displayId) {
        super(displayId, detectSingleFingerTripleTap, detectTwoFingerTripleTap, detectShortcutTrigger, trace, callback);
        this.mTempPoint = new android.graphics.Point();
        this.mTripleTapAndHoldStartedTime = 0L;
        if (DEBUG_ALL) {
            android.util.Slog.i(this.mLogTag, "WindowMagnificationGestureHandler() , displayId = " + displayId + ")");
        }
        this.mContext = context;
        this.mMagnificationConnectionManager = magnificationConnectionManager;
        this.mMotionEventDispatcherDelegate = new com.android.server.accessibility.magnification.MotionEventDispatcherDelegate(context, new com.android.server.accessibility.magnification.MotionEventDispatcherDelegate.EventDispatcher() { // from class: com.android.server.accessibility.magnification.WindowMagnificationGestureHandler$$ExternalSyntheticLambda0
            @Override // com.android.server.accessibility.magnification.MotionEventDispatcherDelegate.EventDispatcher
            public final void dispatchMotionEvent(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i) {
                this.f$0.lambda$new$0(motionEvent, motionEvent2, i);
            }
        });
        this.mDelegatingState = new com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.DelegatingState(this.mMotionEventDispatcherDelegate);
        this.mDetectingState = new com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.DetectingState(context);
        this.mViewportDraggingState = new com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.ViewportDraggingState();
        this.mObservePanningScalingState = new com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.PanningScalingGestureState(new com.android.server.accessibility.magnification.PanningScalingHandler(context, MAX_SCALE, 1.0f, true, new com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate() { // from class: com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.1
            @Override // com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate
            public boolean processScroll(int displayId2, float distanceX, float distanceY) {
                return com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mMagnificationConnectionManager.processScroll(displayId2, distanceX, distanceY);
            }

            @Override // com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate
            public void setScale(int displayId2, float scale) {
                com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mMagnificationConnectionManager.setScale(displayId2, scale);
            }

            @Override // com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate
            public float getScale(int displayId2) {
                return com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mMagnificationConnectionManager.getScale(displayId2);
            }
        }));
        transitionTo(this.mDetectingState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        dispatchTransformedEvent(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler
    void onMotionEventInternal(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mObservePanningScalingState.mPanningScalingHandler.onTouchEvent(event);
        this.mCurrentState.onMotionEvent(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void clearEvents(int inputSource) {
        if (inputSource == 4098) {
            resetToDetectState();
        }
        super.clearEvents(inputSource);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onDestroy() {
        if (DEBUG_ALL) {
            android.util.Slog.i(this.mLogTag, "onDestroy(); delayed = " + this.mDetectingState.toString());
        }
        this.mMagnificationConnectionManager.disableWindowMagnification(this.mDisplayId, true);
        resetToDetectState();
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler
    public void handleShortcutTriggered() {
        android.graphics.Point screenSize = this.mTempPoint;
        getScreenSize(this.mTempPoint);
        toggleMagnification(screenSize.x / 2.0f, screenSize.y / 2.0f, 0);
    }

    private void getScreenSize(android.graphics.Point outSize) {
        android.view.Display display = this.mContext.getDisplay();
        display.getRealSize(outSize);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler
    public int getMode() {
        return 2;
    }

    private void enableWindowMagnifier(float centerX, float centerY, int windowPosition) {
        if (DEBUG_ALL) {
            android.util.Slog.i(this.mLogTag, "enableWindowMagnifier :" + centerX + ", " + centerY + ", " + windowPosition);
        }
        float scale = android.util.MathUtils.constrain(this.mMagnificationConnectionManager.getPersistedScale(this.mDisplayId), 1.0f, MAX_SCALE);
        this.mMagnificationConnectionManager.enableWindowMagnification(this.mDisplayId, scale, centerX, centerY, windowPosition);
    }

    private void disableWindowMagnifier() {
        if (DEBUG_ALL) {
            android.util.Slog.i(this.mLogTag, "disableWindowMagnifier()");
        }
        this.mMagnificationConnectionManager.disableWindowMagnification(this.mDisplayId, false);
    }

    private void toggleMagnification(float centerX, float centerY, int windowPosition) {
        if (this.mMagnificationConnectionManager.isWindowMagnifierEnabled(this.mDisplayId)) {
            disableWindowMagnifier();
        } else {
            enableWindowMagnifier(centerX, centerY, windowPosition);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTripleTap(android.view.MotionEvent up) {
        if (DEBUG_DETECTING) {
            android.util.Slog.i(this.mLogTag, "onTripleTap()");
        }
        toggleMagnification(up.getX(), up.getY(), 0);
    }

    void onTripleTapAndHold(android.view.MotionEvent up) {
        if (DEBUG_DETECTING) {
            android.util.Slog.i(this.mLogTag, "onTripleTapAndHold()");
        }
        this.mViewportDraggingState.mEnabledBeforeDrag = this.mMagnificationConnectionManager.isWindowMagnifierEnabled(this.mDisplayId);
        enableWindowMagnifier(up.getX(), up.getY(), 1);
        this.mTripleTapAndHoldStartedTime = android.os.SystemClock.uptimeMillis();
        transitionTo(this.mViewportDraggingState);
    }

    void releaseTripleTapAndHold() {
        if (!this.mViewportDraggingState.mEnabledBeforeDrag) {
            this.mMagnificationConnectionManager.disableWindowMagnification(this.mDisplayId, true);
        }
        transitionTo(this.mDetectingState);
        if (this.mTripleTapAndHoldStartedTime != 0) {
            long duration = android.os.SystemClock.uptimeMillis() - this.mTripleTapAndHoldStartedTime;
            logMagnificationTripleTapAndHoldSession(duration);
            this.mTripleTapAndHoldStartedTime = 0L;
        }
    }

    void logMagnificationTripleTapAndHoldSession(long duration) {
        com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logMagnificationTripleTapAndHoldSession(duration);
    }

    void resetToDetectState() {
        transitionTo(this.mDetectingState);
    }

    interface State {
        void onMotionEvent(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i);

        default void clear() {
        }

        default void onEnter() {
        }

        default void onExit() {
        }

        default java.lang.String name() {
            return getClass().getSimpleName();
        }

        static java.lang.String nameOf(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State s) {
            return s != null ? s.name() : "null";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transitionTo(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State state) {
        if (DEBUG_STATE_TRANSITIONS) {
            android.util.Slog.i(this.mLogTag, "state transition: " + (com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State.nameOf(this.mCurrentState) + " -> " + com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State.nameOf(state) + " at " + java.util.Arrays.asList((java.lang.StackTraceElement[]) java.util.Arrays.copyOfRange(new java.lang.RuntimeException().getStackTrace(), 1, 5))).replace(getClass().getName(), ""));
        }
        this.mPreviousState = this.mCurrentState;
        if (this.mPreviousState != null) {
            this.mPreviousState.onExit();
        }
        this.mCurrentState = state;
        if (this.mCurrentState != null) {
            this.mCurrentState.onEnter();
        }
    }

    final class PanningScalingGestureState implements com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State {
        private final com.android.server.accessibility.magnification.PanningScalingHandler mPanningScalingHandler;

        PanningScalingGestureState(com.android.server.accessibility.magnification.PanningScalingHandler panningScalingHandler) {
            this.mPanningScalingHandler = panningScalingHandler;
        }

        @Override // com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State
        public void onEnter() {
            this.mPanningScalingHandler.setEnabled(true);
        }

        @Override // com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State
        public void onExit() {
            this.mPanningScalingHandler.setEnabled(false);
            com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mMagnificationConnectionManager.persistScale(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDisplayId);
            clear();
        }

        @Override // com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            int action = event.getActionMasked();
            if (action == 1 || action == 3) {
                com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectingState);
            }
        }

        @Override // com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State
        public void clear() {
            this.mPanningScalingHandler.clear();
        }

        public java.lang.String toString() {
            return "PanningScalingState{mPanningScalingHandler=" + this.mPanningScalingHandler + '}';
        }
    }

    final class DelegatingState implements com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State {
        private final com.android.server.accessibility.magnification.MotionEventDispatcherDelegate mMotionEventDispatcherDelegate;

        DelegatingState(com.android.server.accessibility.magnification.MotionEventDispatcherDelegate motionEventDispatcherDelegate) {
            this.mMotionEventDispatcherDelegate = motionEventDispatcherDelegate;
        }

        @Override // com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            this.mMotionEventDispatcherDelegate.dispatchMotionEvent(event, rawEvent, policyFlags);
            switch (event.getActionMasked()) {
                case 1:
                case 3:
                    com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectingState);
                    break;
            }
        }
    }

    final class ViewportDraggingState implements com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State {
        boolean mEnabledBeforeDrag;
        private float mLastX = Float.NaN;
        private float mLastY = Float.NaN;

        ViewportDraggingState() {
        }

        @Override // com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            int action = event.getActionMasked();
            switch (action) {
                case 1:
                case 3:
                    com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.releaseTripleTapAndHold();
                    break;
                case 2:
                    if (!java.lang.Float.isNaN(this.mLastX) && !java.lang.Float.isNaN(this.mLastY)) {
                        float offsetX = event.getX() - this.mLastX;
                        float offsetY = event.getY() - this.mLastY;
                        com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mMagnificationConnectionManager.moveWindowMagnification(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDisplayId, offsetX, offsetY);
                    }
                    float offsetX2 = event.getX();
                    this.mLastX = offsetX2;
                    this.mLastY = event.getY();
                    break;
            }
        }

        @Override // com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State
        public void clear() {
            this.mLastX = Float.NaN;
            this.mLastY = Float.NaN;
        }

        @Override // com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State
        public void onExit() {
            clear();
        }

        public java.lang.String toString() {
            return "ViewportDraggingState{mLastX=" + this.mLastX + ",mLastY=" + this.mLastY + '}';
        }
    }

    final class DetectingState implements com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State, com.android.server.accessibility.magnification.MagnificationGesturesObserver.Callback {
        private final com.android.server.accessibility.magnification.MagnificationGesturesObserver mGesturesObserver;

        DetectingState(android.content.Context context) {
            if (com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture()) {
                java.util.List<com.android.server.accessibility.gestures.GestureMatcher> mGestureMatchers = new java.util.ArrayList<>();
                mGestureMatchers.add(new com.android.server.accessibility.magnification.SimpleSwipe(context));
                mGestureMatchers.add(new com.android.server.accessibility.gestures.MultiTap(context, com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectSingleFingerTripleTap ? 3 : 1, com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectSingleFingerTripleTap ? 105 : 103, null));
                mGestureMatchers.add(new com.android.server.accessibility.gestures.MultiTapAndHold(context, com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectSingleFingerTripleTap ? 3 : 1, com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectSingleFingerTripleTap ? 106 : 104, null));
                mGestureMatchers.add(new com.android.server.accessibility.magnification.TwoFingersDownOrSwipe(context));
                if (com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectTwoFingerTripleTap) {
                    mGestureMatchers.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(context, 2, 2, 105, null));
                    mGestureMatchers.add(new com.android.server.accessibility.gestures.MultiFingerMultiTapAndHold(context, 2, 2, 106, null));
                }
                this.mGesturesObserver = new com.android.server.accessibility.magnification.MagnificationGesturesObserver(this, (com.android.server.accessibility.gestures.GestureMatcher[]) mGestureMatchers.toArray(new com.android.server.accessibility.gestures.GestureMatcher[mGestureMatchers.size()]));
                return;
            }
            com.android.server.accessibility.gestures.MultiTap multiTap = new com.android.server.accessibility.gestures.MultiTap(context, com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectSingleFingerTripleTap ? 3 : 1, com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectSingleFingerTripleTap ? 105 : 103, null);
            com.android.server.accessibility.gestures.MultiTapAndHold multiTapAndHold = new com.android.server.accessibility.gestures.MultiTapAndHold(context, com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectSingleFingerTripleTap ? 3 : 1, com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectSingleFingerTripleTap ? 106 : 104, null);
            this.mGesturesObserver = new com.android.server.accessibility.magnification.MagnificationGesturesObserver(this, new com.android.server.accessibility.magnification.SimpleSwipe(context), multiTap, multiTapAndHold, new com.android.server.accessibility.magnification.TwoFingersDownOrSwipe(context));
        }

        @Override // com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            this.mGesturesObserver.onMotionEvent(event, rawEvent, policyFlags);
        }

        public java.lang.String toString() {
            return "DetectingState{mGestureTimeoutObserver=" + this.mGesturesObserver + '}';
        }

        @Override // com.android.server.accessibility.magnification.MagnificationGesturesObserver.Callback
        public boolean shouldStopDetection(android.view.MotionEvent motionEvent) {
            return (com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mMagnificationConnectionManager.isWindowMagnifierEnabled(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDisplayId) || com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectSingleFingerTripleTap || (com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDetectTwoFingerTripleTap && com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture())) ? false : true;
        }

        @Override // com.android.server.accessibility.magnification.MagnificationGesturesObserver.Callback
        public void onGestureCompleted(int gestureId, long lastDownEventTime, java.util.List<com.android.server.accessibility.magnification.MotionEventInfo> delayedEventQueue, android.view.MotionEvent motionEvent) {
            if (com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.DEBUG_DETECTING) {
                android.util.Slog.d(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mLogTag, "onGestureDetected : gesture = " + com.android.server.accessibility.magnification.MagnificationGestureMatcher.gestureIdToString(gestureId));
                android.util.Slog.d(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mLogTag, "onGestureDetected : delayedEventQueue = " + delayedEventQueue);
            }
            if (gestureId == 101 && com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mMagnificationConnectionManager.pointersInWindow(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDisplayId, motionEvent) > 0) {
                com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mObservePanningScalingState);
                return;
            }
            if (gestureId == 105) {
                com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.onTripleTap(motionEvent);
            } else if (gestureId == 106) {
                com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.onTripleTapAndHold(motionEvent);
            } else {
                com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mMotionEventDispatcherDelegate.sendDelayedMotionEvents(delayedEventQueue, lastDownEventTime);
                changeToDelegateStateIfNeed(motionEvent);
            }
        }

        @Override // com.android.server.accessibility.magnification.MagnificationGesturesObserver.Callback
        public void onGestureCancelled(long lastDownEventTime, java.util.List<com.android.server.accessibility.magnification.MotionEventInfo> delayedEventQueue, android.view.MotionEvent motionEvent) {
            if (com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.DEBUG_DETECTING) {
                android.util.Slog.d(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mLogTag, "onGestureCancelled : delayedEventQueue = " + delayedEventQueue);
            }
            com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mMotionEventDispatcherDelegate.sendDelayedMotionEvents(delayedEventQueue, lastDownEventTime);
            changeToDelegateStateIfNeed(motionEvent);
        }

        private void changeToDelegateStateIfNeed(android.view.MotionEvent motionEvent) {
            if (motionEvent != null && (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3)) {
                return;
            }
            com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.this.mDelegatingState);
        }
    }

    public java.lang.String toString() {
        return "WindowMagnificationGestureHandler{mDetectingState=" + this.mDetectingState + ", mDelegatingState=" + this.mDelegatingState + ", mViewportDraggingState=" + this.mViewportDraggingState + ", mMagnifiedInteractionState=" + this.mObservePanningScalingState + ", mCurrentState=" + com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State.nameOf(this.mCurrentState) + ", mPreviousState=" + com.android.server.accessibility.magnification.WindowMagnificationGestureHandler.State.nameOf(this.mPreviousState) + ", mMagnificationConnectionManager=" + this.mMagnificationConnectionManager + ", mDisplayId=" + this.mDisplayId + '}';
    }
}
