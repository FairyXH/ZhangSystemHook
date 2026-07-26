package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
public class TouchState {
    public static final int ALL_POINTER_ID_BITS = -1;
    private static final java.lang.String LOG_TAG = "TouchState";
    public static final int MAX_POINTER_COUNT = 32;
    public static final int STATE_CLEAR = 0;
    public static final int STATE_DELEGATING = 4;
    public static final int STATE_DRAGGING = 3;
    public static final int STATE_GESTURE_DETECTING = 5;
    public static final int STATE_TOUCH_EXPLORING = 2;
    public static final int STATE_TOUCH_INTERACTING = 1;
    private com.android.server.accessibility.AccessibilityManagerService mAms;
    private int mDisplayId;
    private int mInjectedPointersDown;
    private long mLastInjectedDownEventTime;
    private android.view.MotionEvent mLastInjectedHoverEvent;
    private android.view.MotionEvent mLastInjectedHoverEventForClick;
    private android.view.MotionEvent mLastReceivedEvent;
    int mLastReceivedPolicyFlags;
    private android.view.MotionEvent mLastReceivedRawEvent;
    private int mLastTouchedWindowId;
    private int mState = 0;
    private boolean mServiceDetectsGestures = false;
    private boolean mServiceDetectsGesturesRequested = false;
    private final com.android.server.accessibility.gestures.TouchState.ReceivedPointerTracker mReceivedPointerTracker = new com.android.server.accessibility.gestures.TouchState.ReceivedPointerTracker();

    public @interface State {
    }

    public TouchState(int displayId, com.android.server.accessibility.AccessibilityManagerService ams) {
        this.mDisplayId = -1;
        this.mDisplayId = displayId;
        this.mAms = ams;
    }

    public void clear() {
        setState(0);
        this.mServiceDetectsGestures = this.mServiceDetectsGesturesRequested;
        if (this.mLastReceivedEvent != null) {
            this.mLastReceivedEvent.recycle();
            this.mLastReceivedEvent = null;
        }
        this.mReceivedPointerTracker.clear();
        this.mInjectedPointersDown = 0;
    }

    public void onReceivedMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (isClear() && event.getActionMasked() == 0) {
            clear();
        }
        if (this.mLastReceivedEvent != null) {
            this.mLastReceivedEvent.recycle();
        }
        if (this.mLastReceivedRawEvent != null) {
            this.mLastReceivedRawEvent.recycle();
        }
        this.mLastReceivedEvent = android.view.MotionEvent.obtain(event);
        this.mLastReceivedRawEvent = android.view.MotionEvent.obtain(rawEvent);
        this.mLastReceivedPolicyFlags = policyFlags;
        this.mReceivedPointerTracker.onMotionEvent(rawEvent);
    }

    public void onInjectedMotionEvent(android.view.MotionEvent event) {
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(event.getActionIndex());
        int pointerFlag = 1 << pointerId;
        switch (action) {
            case 0:
            case 5:
                this.mInjectedPointersDown |= pointerFlag;
                this.mLastInjectedDownEventTime = event.getDownTime();
                break;
            case 1:
            case 6:
                this.mInjectedPointersDown &= ~pointerFlag;
                if (this.mInjectedPointersDown == 0) {
                    this.mLastInjectedDownEventTime = 0L;
                }
                break;
            case 7:
            case 9:
                if (this.mLastInjectedHoverEvent != null) {
                    this.mLastInjectedHoverEvent.recycle();
                }
                this.mLastInjectedHoverEvent = android.view.MotionEvent.obtain(event);
                break;
            case 10:
                if (this.mLastInjectedHoverEvent != null) {
                    this.mLastInjectedHoverEvent.recycle();
                }
                this.mLastInjectedHoverEvent = android.view.MotionEvent.obtain(event);
                if (this.mLastInjectedHoverEventForClick != null) {
                    this.mLastInjectedHoverEventForClick.recycle();
                }
                this.mLastInjectedHoverEventForClick = android.view.MotionEvent.obtain(event);
                break;
        }
        if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
            android.util.Slog.i(LOG_TAG, "Injected pointer:\n" + toString());
        }
    }

    public void onReceivedAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        switch (event.getEventType()) {
            case 32:
            case 32768:
                if (this.mLastInjectedHoverEventForClick != null) {
                    this.mLastInjectedHoverEventForClick.recycle();
                    this.mLastInjectedHoverEventForClick = null;
                }
                this.mLastTouchedWindowId = -1;
                break;
            case 128:
            case 256:
                this.mLastTouchedWindowId = event.getWindowId();
                break;
            case 2097152:
                this.mAms.moveNonProxyTopFocusedDisplayToTopIfNeeded();
                break;
        }
    }

    public void onInjectedAccessibilityEvent(int type) {
        switch (type) {
            case 512:
                startTouchExploring();
                break;
            case 1024:
                startTouchInteracting();
                break;
            case 262144:
                startGestureDetecting();
                break;
            case 524288:
                clear();
                break;
            case 1048576:
                startTouchInteracting();
                break;
            case 2097152:
                setState(0);
                break;
        }
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int state) {
        if (this.mState == state) {
            return;
        }
        if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
            android.util.Slog.i(LOG_TAG, getStateSymbolicName(this.mState) + "->" + getStateSymbolicName(state));
        }
        this.mState = state;
        if (this.mServiceDetectsGestures) {
            this.mAms.onTouchStateChanged(this.mDisplayId, state);
        }
    }

    public boolean isTouchExploring() {
        return this.mState == 2;
    }

    public void startTouchExploring() {
        setState(2);
    }

    public boolean isDelegating() {
        return this.mState == 4;
    }

    public void startDelegating() {
        setState(4);
    }

    public boolean isGestureDetecting() {
        return this.mState == 5;
    }

    public void startGestureDetecting() {
        setState(5);
    }

    public boolean isDragging() {
        return this.mState == 3;
    }

    public void startDragging() {
        setState(3);
    }

    public boolean isTouchInteracting() {
        return this.mState == 1;
    }

    public void startTouchInteracting() {
        setState(1);
    }

    public boolean isClear() {
        return this.mState == 0;
    }

    public java.lang.String toString() {
        return "TouchState { mState: " + getStateSymbolicName(this.mState) + " }";
    }

    public static java.lang.String getStateSymbolicName(int state) {
        switch (state) {
            case 0:
                return "STATE_CLEAR";
            case 1:
                return "STATE_TOUCH_INTERACTING";
            case 2:
                return "STATE_TOUCH_EXPLORING";
            case 3:
                return "STATE_DRAGGING";
            case 4:
                return "STATE_DELEGATING";
            case 5:
                return "STATE_GESTURE_DETECTING";
            default:
                return "Unknown state: " + state;
        }
    }

    public com.android.server.accessibility.gestures.TouchState.ReceivedPointerTracker getReceivedPointerTracker() {
        return this.mReceivedPointerTracker;
    }

    public android.view.MotionEvent getLastReceivedEvent() {
        return this.mLastReceivedEvent;
    }

    public int getLastReceivedPolicyFlags() {
        return this.mLastReceivedPolicyFlags;
    }

    public android.view.MotionEvent getLastReceivedRawEvent() {
        return this.mLastReceivedRawEvent;
    }

    public android.view.MotionEvent getLastInjectedHoverEvent() {
        return this.mLastInjectedHoverEvent;
    }

    public long getLastInjectedDownEventTime() {
        return this.mLastInjectedDownEventTime;
    }

    public int getLastTouchedWindowId() {
        return this.mLastTouchedWindowId;
    }

    public int getInjectedPointerDownCount() {
        return java.lang.Integer.bitCount(this.mInjectedPointersDown);
    }

    public int getInjectedPointersDown() {
        return this.mInjectedPointersDown;
    }

    public boolean isInjectedPointerDown(int pointerId) {
        int pointerFlag = 1 << pointerId;
        return (this.mInjectedPointersDown & pointerFlag) != 0;
    }

    public android.view.MotionEvent getLastInjectedHoverEventForClick() {
        return this.mLastInjectedHoverEventForClick;
    }

    public boolean isServiceDetectingGestures() {
        return this.mServiceDetectsGestures;
    }

    public void setServiceDetectsGestures(boolean mode) {
        if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
            android.util.Slog.d(LOG_TAG, "serviceDetectsGestures: " + mode);
        }
        this.mServiceDetectsGesturesRequested = mode;
    }

    class ReceivedPointerTracker {
        private static final java.lang.String LOG_TAG_RECEIVED_POINTER_TRACKER = "ReceivedPointerTracker";
        private int mLastReceivedDownEdgeFlags;
        private int mPrimaryPointerId;
        private final com.android.server.accessibility.gestures.TouchState.PointerDownInfo[] mReceivedPointers = new com.android.server.accessibility.gestures.TouchState.PointerDownInfo[32];
        private int mReceivedPointersDown;

        ReceivedPointerTracker() {
            clear();
        }

        public void clear() {
            this.mReceivedPointersDown = 0;
            this.mPrimaryPointerId = 0;
            for (int i = 0; i < 32; i++) {
                this.mReceivedPointers[i] = com.android.server.accessibility.gestures.TouchState.this.new PointerDownInfo();
            }
        }

        public void onMotionEvent(android.view.MotionEvent event) {
            int action = event.getActionMasked();
            switch (action) {
                case 0:
                    handleReceivedPointerDown(event.getActionIndex(), event);
                    break;
                case 1:
                    handleReceivedPointerUp(event.getActionIndex(), event);
                    break;
                case 5:
                    handleReceivedPointerDown(event.getActionIndex(), event);
                    break;
                case 6:
                    handleReceivedPointerUp(event.getActionIndex(), event);
                    break;
            }
            if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                android.util.Slog.i(LOG_TAG_RECEIVED_POINTER_TRACKER, "Received pointer:\n" + toString());
            }
        }

        public int getReceivedPointerDownCount() {
            return java.lang.Integer.bitCount(this.mReceivedPointersDown);
        }

        public boolean isReceivedPointerDown(int pointerId) {
            int pointerFlag = 1 << pointerId;
            return (this.mReceivedPointersDown & pointerFlag) != 0;
        }

        public float getReceivedPointerDownX(int pointerId) {
            return this.mReceivedPointers[pointerId].mX;
        }

        public float getReceivedPointerDownY(int pointerId) {
            return this.mReceivedPointers[pointerId].mY;
        }

        public long getReceivedPointerDownTime(int pointerId) {
            return this.mReceivedPointers[pointerId].mTime;
        }

        public int getPrimaryPointerId() {
            if (this.mPrimaryPointerId == -1) {
                this.mPrimaryPointerId = findPrimaryPointerId();
            }
            return this.mPrimaryPointerId;
        }

        public int getLastReceivedDownEdgeFlags() {
            return this.mLastReceivedDownEdgeFlags;
        }

        private void handleReceivedPointerDown(int pointerIndex, android.view.MotionEvent event) {
            int pointerId = event.getPointerId(pointerIndex);
            int pointerFlag = 1 << pointerId;
            this.mLastReceivedDownEdgeFlags = event.getEdgeFlags();
            this.mReceivedPointersDown |= pointerFlag;
            this.mReceivedPointers[pointerId].set(event.getX(pointerIndex), event.getY(pointerIndex), event.getEventTime());
            if (event.getActionMasked() == 0) {
                this.mPrimaryPointerId = pointerId;
            }
        }

        private void handleReceivedPointerUp(int pointerIndex, android.view.MotionEvent event) {
            int pointerId = event.getPointerId(pointerIndex);
            int pointerFlag = 1 << pointerId;
            this.mReceivedPointersDown &= ~pointerFlag;
            this.mReceivedPointers[pointerId].clear();
            if (this.mPrimaryPointerId == pointerId) {
                this.mPrimaryPointerId = -1;
            }
        }

        private int findPrimaryPointerId() {
            int primaryPointerId = -1;
            long minDownTime = Long.MAX_VALUE;
            int pointerIdBits = this.mReceivedPointersDown;
            while (pointerIdBits > 0) {
                int pointerId = java.lang.Integer.numberOfTrailingZeros(pointerIdBits);
                pointerIdBits &= ~(1 << pointerId);
                long downPointerTime = this.mReceivedPointers[pointerId].mTime;
                if (downPointerTime < minDownTime) {
                    minDownTime = downPointerTime;
                    primaryPointerId = pointerId;
                }
            }
            return primaryPointerId;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append("=========================");
            builder.append("\nDown pointers #");
            builder.append(getReceivedPointerDownCount());
            builder.append(" [ ");
            for (int i = 0; i < 32; i++) {
                if (isReceivedPointerDown(i)) {
                    builder.append(i);
                    builder.append(" ");
                }
            }
            builder.append("]");
            builder.append("\nPrimary pointer id [ ");
            builder.append(getPrimaryPointerId());
            builder.append(" ]");
            builder.append("\n=========================");
            return builder.toString();
        }
    }

    class PointerDownInfo {
        private long mTime;
        private float mX;
        private float mY;

        PointerDownInfo() {
        }

        public void set(float x, float y, long time) {
            this.mX = x;
            this.mY = y;
            this.mTime = time;
        }

        public void clear() {
            this.mX = 0.0f;
            this.mY = 0.0f;
            this.mTime = 0L;
        }
    }
}
