package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public abstract class MagnificationGestureHandler extends com.android.server.accessibility.BaseEventStreamTransformation {
    protected static final boolean DEBUG_ALL = android.util.Log.isLoggable("MagnificationGestureHandler", 3);
    protected static final boolean DEBUG_EVENT_STREAM = DEBUG_ALL | false;
    protected final com.android.server.accessibility.magnification.MagnificationGestureHandler.Callback mCallback;
    private final java.util.Queue<android.view.MotionEvent> mDebugInputEventHistory;
    private final java.util.Queue<android.view.MotionEvent> mDebugOutputEventHistory;
    protected final boolean mDetectShortcutTrigger;
    protected final boolean mDetectSingleFingerTripleTap;
    protected final boolean mDetectTwoFingerTripleTap;
    protected final int mDisplayId;
    protected final java.lang.String mLogTag = getClass().getSimpleName();
    private final com.android.server.accessibility.AccessibilityTraceManager mTrace;

    public interface Callback {
        void onTouchInteractionEnd(int i, int i2);

        void onTouchInteractionStart(int i, int i2);
    }

    public abstract int getMode();

    abstract void handleShortcutTriggered();

    abstract void onMotionEventInternal(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i);

    protected MagnificationGestureHandler(int displayId, boolean detectSingleFingerTripleTap, boolean detectTwoFingerTripleTap, boolean detectShortcutTrigger, com.android.server.accessibility.AccessibilityTraceManager trace, com.android.server.accessibility.magnification.MagnificationGestureHandler.Callback callback) {
        this.mDisplayId = displayId;
        this.mDetectSingleFingerTripleTap = detectSingleFingerTripleTap;
        this.mDetectTwoFingerTripleTap = com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture() && detectTwoFingerTripleTap;
        this.mDetectShortcutTrigger = detectShortcutTrigger;
        this.mTrace = trace;
        this.mCallback = callback;
        this.mDebugInputEventHistory = DEBUG_EVENT_STREAM ? new java.util.ArrayDeque() : null;
        this.mDebugOutputEventHistory = DEBUG_EVENT_STREAM ? new java.util.ArrayDeque() : null;
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public final void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (DEBUG_ALL) {
            android.util.Slog.i(this.mLogTag, "onMotionEvent(" + event + ")");
        }
        if (this.mTrace.isA11yTracingEnabledForTypes(12288L)) {
            this.mTrace.logTrace("MagnificationGestureHandler.onMotionEvent", 12288L, "event=" + event + ";rawEvent=" + rawEvent + ";policyFlags=" + policyFlags);
        }
        if (DEBUG_EVENT_STREAM) {
            storeEventInto(this.mDebugInputEventHistory, event);
        }
        if (shouldDispatchTransformedEvent(event)) {
            dispatchTransformedEvent(event, rawEvent, policyFlags);
            return;
        }
        onMotionEventInternal(event, rawEvent, policyFlags);
        int action = event.getAction();
        if (action == 0) {
            this.mCallback.onTouchInteractionStart(this.mDisplayId, getMode());
        } else if (action == 1 || action == 3) {
            this.mCallback.onTouchInteractionEnd(this.mDisplayId, getMode());
        }
    }

    private boolean shouldDispatchTransformedEvent(android.view.MotionEvent event) {
        if ((!this.mDetectSingleFingerTripleTap && !this.mDetectTwoFingerTripleTap && !this.mDetectShortcutTrigger) || !event.isFromSource(4098)) {
            return true;
        }
        return false;
    }

    final void dispatchTransformedEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (DEBUG_EVENT_STREAM) {
            storeEventInto(this.mDebugOutputEventHistory, event);
            try {
                super.onMotionEvent(event, rawEvent, policyFlags);
                return;
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException("Exception downstream following input events: " + this.mDebugInputEventHistory + "\nTransformed into output events: " + this.mDebugOutputEventHistory, e);
            }
        }
        super.onMotionEvent(event, rawEvent, policyFlags);
    }

    private static void storeEventInto(java.util.Queue<android.view.MotionEvent> queue, android.view.MotionEvent event) {
        queue.add(android.view.MotionEvent.obtain(event));
        while (!queue.isEmpty() && event.getEventTime() - queue.peek().getEventTime() > 5000) {
            queue.remove().recycle();
        }
    }

    public void notifyShortcutTriggered() {
        if (DEBUG_ALL) {
            android.util.Slog.i(this.mLogTag, "notifyShortcutTriggered():");
        }
        if (this.mDetectShortcutTrigger) {
            handleShortcutTriggered();
        }
    }
}
