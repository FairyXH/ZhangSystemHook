package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
public class TouchExplorer extends com.android.server.accessibility.BaseEventStreamTransformation implements com.android.server.accessibility.gestures.GestureManifold.Listener {
    private static final float EDGE_SWIPE_HEIGHT_CM = 0.25f;
    private static final int EXIT_GESTURE_DETECTION_TIMEOUT = 2000;
    private static final long LOGGING_FLAGS = 12288;
    private static final float MAX_DRAGGING_ANGLE_COS = 0.52532196f;
    private final com.android.server.accessibility.AccessibilityManagerService mAms;
    private final android.content.Context mContext;
    private final int mDetermineUserIntentTimeout;
    private final com.android.server.accessibility.gestures.EventDispatcher mDispatcher;
    private int mDisplayId;
    private final int mDoubleTapSlop;
    private int mDraggingPointerId;
    private final float mEdgeSwipeHeightPixels;
    private final com.android.server.accessibility.gestures.TouchExplorer.ExitGestureDetectionModeDelayed mExitGestureDetectionModeDelayed;
    private android.graphics.Region mGestureDetectionPassthroughRegion;
    private final com.android.server.accessibility.gestures.GestureManifold mGestureDetector;
    private final android.os.Handler mHandler;
    private final com.android.server.accessibility.gestures.TouchState.ReceivedPointerTracker mReceivedPointerTracker;
    private final com.android.server.accessibility.gestures.TouchExplorer.SendHoverEnterAndMoveDelayed mSendHoverEnterAndMoveDelayed;
    private final com.android.server.accessibility.gestures.TouchExplorer.SendHoverExitDelayed mSendHoverExitDelayed;
    private final com.android.server.accessibility.gestures.TouchExplorer.SendAccessibilityEventDelayed mSendTouchExplorationEndDelayed;
    private final com.android.server.accessibility.gestures.TouchExplorer.SendAccessibilityEventDelayed mSendTouchInteractionEndDelayed;
    private com.android.server.accessibility.gestures.TouchState mState;
    private android.graphics.Region mTouchExplorationPassthroughRegion;
    private final int mTouchSlop;
    private static final java.lang.String LOG_TAG = "TouchExplorer";
    static final boolean DEBUG = android.util.Log.isLoggable(LOG_TAG, 3);

    public TouchExplorer(android.content.Context context, com.android.server.accessibility.AccessibilityManagerService service) {
        this(context, service, null);
    }

    public TouchExplorer(android.content.Context context, com.android.server.accessibility.AccessibilityManagerService service, com.android.server.accessibility.gestures.GestureManifold detector) {
        this(context, service, detector, new android.os.Handler(context.getMainLooper()));
    }

    TouchExplorer(android.content.Context context, com.android.server.accessibility.AccessibilityManagerService service, com.android.server.accessibility.gestures.GestureManifold detector, android.os.Handler mainHandler) {
        this.mDisplayId = -1;
        this.mContext = context;
        this.mDisplayId = context.getDisplayId();
        this.mAms = service;
        this.mState = new com.android.server.accessibility.gestures.TouchState(this.mDisplayId, this.mAms);
        this.mReceivedPointerTracker = this.mState.getReceivedPointerTracker();
        this.mDispatcher = new com.android.server.accessibility.gestures.EventDispatcher(context, this.mAms, super.getNext(), this.mState);
        this.mDetermineUserIntentTimeout = android.view.ViewConfiguration.getDoubleTapTimeout();
        this.mDoubleTapSlop = android.view.ViewConfiguration.get(context).getScaledDoubleTapSlop();
        this.mTouchSlop = android.view.ViewConfiguration.get(context).getScaledTouchSlop();
        android.util.DisplayMetrics metrics = this.mContext.getResources().getDisplayMetrics();
        this.mEdgeSwipeHeightPixels = (metrics.ydpi / com.android.server.accessibility.gestures.GestureUtils.CM_PER_INCH) * EDGE_SWIPE_HEIGHT_CM;
        this.mHandler = mainHandler;
        this.mExitGestureDetectionModeDelayed = new com.android.server.accessibility.gestures.TouchExplorer.ExitGestureDetectionModeDelayed();
        this.mSendHoverEnterAndMoveDelayed = new com.android.server.accessibility.gestures.TouchExplorer.SendHoverEnterAndMoveDelayed();
        this.mSendHoverExitDelayed = new com.android.server.accessibility.gestures.TouchExplorer.SendHoverExitDelayed();
        this.mSendTouchExplorationEndDelayed = new com.android.server.accessibility.gestures.TouchExplorer.SendAccessibilityEventDelayed(1024, this.mDetermineUserIntentTimeout);
        this.mSendTouchInteractionEndDelayed = new com.android.server.accessibility.gestures.TouchExplorer.SendAccessibilityEventDelayed(2097152, this.mDetermineUserIntentTimeout);
        if (detector == null) {
            this.mGestureDetector = new com.android.server.accessibility.gestures.GestureManifold(context, this, this.mState, this.mHandler);
        } else {
            this.mGestureDetector = detector;
        }
        this.mGestureDetectionPassthroughRegion = new android.graphics.Region();
        this.mTouchExplorationPassthroughRegion = new android.graphics.Region();
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void clearEvents(int inputSource) {
        if (inputSource == 4098) {
            clear();
        }
        super.clearEvents(inputSource);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onDestroy() {
        clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clear() {
        android.view.MotionEvent event = this.mState.getLastReceivedEvent();
        if (event != null) {
            clear(event, 33554432);
        }
    }

    private void clear(android.view.MotionEvent event, int policyFlags) {
        if (this.mState.isTouchExploring() || com.android.server.accessibility.Flags.sendHoverEventsBasedOnEventStream()) {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(policyFlags);
        }
        this.mDraggingPointerId = -1;
        this.mDispatcher.sendUpForInjectedDownPointers(event, policyFlags);
        this.mSendHoverEnterAndMoveDelayed.cancel();
        this.mSendHoverExitDelayed.cancel();
        this.mExitGestureDetectionModeDelayed.cancel();
        this.mSendTouchExplorationEndDelayed.cancel();
        this.mSendTouchInteractionEndDelayed.cancel();
        this.mGestureDetector.clear();
        this.mDispatcher.clear();
        this.mState.clear();
        this.mAms.onTouchInteractionEnd();
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onMotionEvent", LOGGING_FLAGS, "event=" + event + ";rawEvent=" + rawEvent + ";policyFlags=" + policyFlags);
        }
        if (!event.isFromSource(4098)) {
            super.onMotionEvent(event, rawEvent, policyFlags);
            return;
        }
        try {
            checkForMalformedEvent(event);
            checkForMalformedEvent(rawEvent);
            if (DEBUG) {
                android.util.Slog.d(LOG_TAG, "Received event: " + event + ", policyFlags=0x" + java.lang.Integer.toHexString(policyFlags));
                android.util.Slog.d(LOG_TAG, this.mState.toString());
            }
            this.mState.onReceivedMotionEvent(event, rawEvent, policyFlags);
            if (shouldPerformGestureDetection(event) && this.mGestureDetector.onMotionEvent(event, rawEvent, policyFlags)) {
                return;
            }
            if (event.getActionMasked() == 3) {
                clear(event, policyFlags);
                return;
            }
            if (this.mState.isClear()) {
                handleMotionEventStateClear(event, rawEvent, policyFlags);
                return;
            }
            if (this.mState.isTouchInteracting()) {
                handleMotionEventStateTouchInteracting(event, rawEvent, policyFlags);
                return;
            }
            if (this.mState.isTouchExploring()) {
                handleMotionEventStateTouchExploring(event, rawEvent, policyFlags);
                return;
            }
            if (this.mState.isDragging()) {
                handleMotionEventStateDragging(event, rawEvent, policyFlags);
                return;
            }
            if (this.mState.isDelegating()) {
                handleMotionEventStateDelegating(event, rawEvent, policyFlags);
                return;
            }
            if (!this.mState.isGestureDetecting()) {
                android.util.Slog.e(LOG_TAG, "Illegal state: " + this.mState);
                clear(event, policyFlags);
            } else {
                this.mSendTouchInteractionEndDelayed.cancel();
                if (this.mState.isServiceDetectingGestures()) {
                    this.mAms.sendMotionEventToListeningServices(rawEvent);
                }
            }
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(LOG_TAG, "Ignoring malformed event: " + event.toString(), e);
        }
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onAccessibilityEvent", LOGGING_FLAGS, "event=" + event);
        }
        int eventType = event.getEventType();
        if (eventType == 256) {
            sendsPendingA11yEventsIfNeeded();
        }
        this.mState.onReceivedAccessibilityEvent(event);
        super.onAccessibilityEvent(event);
    }

    private void sendsPendingA11yEventsIfNeeded() {
        if (this.mSendHoverExitDelayed.isPending()) {
            return;
        }
        if (com.android.server.accessibility.Flags.sendA11yEventsBasedOnState()) {
            if (this.mSendTouchExplorationEndDelayed.isPending()) {
                this.mSendTouchExplorationEndDelayed.cancel();
            }
            if (this.mState.isTouchExploring()) {
                this.mDispatcher.sendAccessibilityEvent(1024);
            }
            if (this.mSendTouchInteractionEndDelayed.isPending()) {
                this.mSendTouchInteractionEndDelayed.cancel();
            }
            if (this.mState.isTouchInteracting()) {
                this.mDispatcher.sendAccessibilityEvent(2097152);
                return;
            }
            return;
        }
        if (this.mSendTouchExplorationEndDelayed.isPending()) {
            this.mSendTouchExplorationEndDelayed.cancel();
            this.mDispatcher.sendAccessibilityEvent(1024);
        }
        if (this.mSendTouchInteractionEndDelayed.isPending()) {
            this.mSendTouchInteractionEndDelayed.cancel();
            this.mDispatcher.sendAccessibilityEvent(2097152);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public void onDoubleTapAndHold(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onDoubleTapAndHold", LOGGING_FLAGS, "event=" + event + ";rawEvent=" + rawEvent + ";policyFlags=" + policyFlags);
        }
        if (this.mDispatcher.longPressWithTouchEvents(event, policyFlags)) {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(policyFlags);
            if (isSendMotionEventsEnabled()) {
                android.accessibilityservice.AccessibilityGestureEvent gestureEvent = new android.accessibilityservice.AccessibilityGestureEvent(18, this.mDisplayId, this.mGestureDetector.getMotionEvents());
                dispatchGesture(gestureEvent);
            }
            this.mState.startDelegating();
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public boolean onDoubleTap(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onDoubleTap", LOGGING_FLAGS, "event=" + event + ";rawEvent=" + rawEvent + ";policyFlags=" + policyFlags);
        }
        this.mAms.onTouchInteractionEnd();
        this.mSendHoverEnterAndMoveDelayed.cancel();
        this.mSendHoverExitDelayed.cancel();
        if (isSendMotionEventsEnabled()) {
            android.accessibilityservice.AccessibilityGestureEvent gestureEvent = new android.accessibilityservice.AccessibilityGestureEvent(17, this.mDisplayId, this.mGestureDetector.getMotionEvents());
            dispatchGesture(gestureEvent);
        }
        if (this.mSendTouchExplorationEndDelayed.isPending()) {
            this.mSendTouchExplorationEndDelayed.forceSendAndRemove();
        }
        this.mDispatcher.sendAccessibilityEvent(2097152);
        this.mSendTouchInteractionEndDelayed.cancel();
        android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.setDisplayID(this.mDisplayId);
        if (this.mAms.performActionOnAccessibilityFocusedItem(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK)) {
            return true;
        }
        android.util.Slog.e(LOG_TAG, "ACTION_CLICK failed. Dispatching motion events to simulate click.");
        if (event != null && rawEvent != null) {
            this.mDispatcher.clickWithTouchEvents(event, rawEvent, policyFlags);
        }
        return true;
    }

    public void onDoubleTap() {
        android.view.MotionEvent event = this.mState.getLastReceivedEvent();
        android.view.MotionEvent rawEvent = this.mState.getLastReceivedRawEvent();
        int policyFlags = this.mState.getLastReceivedPolicyFlags();
        onDoubleTap(event, rawEvent, policyFlags);
    }

    public void onDoubleTapAndHold() {
        android.view.MotionEvent event = this.mState.getLastReceivedEvent();
        android.view.MotionEvent rawEvent = this.mState.getLastReceivedRawEvent();
        int policyFlags = this.mState.getLastReceivedPolicyFlags();
        onDoubleTapAndHold(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public boolean onGestureStarted() {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onGestureStarted", LOGGING_FLAGS);
        }
        this.mSendHoverEnterAndMoveDelayed.cancel();
        this.mSendHoverExitDelayed.cancel();
        this.mExitGestureDetectionModeDelayed.post();
        this.mDispatcher.sendAccessibilityEvent(262144);
        return false;
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public boolean onGestureCompleted(android.accessibilityservice.AccessibilityGestureEvent gestureEvent) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onGestureCompleted", LOGGING_FLAGS, "event=" + gestureEvent);
        }
        endGestureDetection(true);
        this.mSendTouchInteractionEndDelayed.cancel();
        dispatchGesture(gestureEvent);
        return true;
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public boolean onGestureCancelled(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onGestureCancelled", LOGGING_FLAGS, "event=" + event + ";rawEvent=" + rawEvent + ";policyFlags=" + policyFlags);
        }
        if (this.mState.isGestureDetecting()) {
            endGestureDetection(event.getActionMasked() == 1);
            return true;
        }
        if (this.mState.isTouchExploring() && event.getActionMasked() == 2) {
            int pointerId = this.mReceivedPointerTracker.getPrimaryPointerId();
            int pointerIdBits = 1 << pointerId;
            this.mSendHoverEnterAndMoveDelayed.addEvent(event, this.mState.getLastReceivedEvent());
            this.mSendHoverEnterAndMoveDelayed.forceSendAndRemove();
            this.mSendHoverExitDelayed.cancel();
            this.mDispatcher.sendMotionEvent(event, 7, event, pointerIdBits, policyFlags);
            return true;
        }
        if (isSendMotionEventsEnabled()) {
            android.accessibilityservice.AccessibilityGestureEvent gestureEvent = new android.accessibilityservice.AccessibilityGestureEvent(0, this.mDisplayId, this.mGestureDetector.getMotionEvents());
            dispatchGesture(gestureEvent);
        }
        return false;
    }

    private void handleMotionEventStateClear(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        switch (event.getActionMasked()) {
            case 0:
                handleActionDown(event, rawEvent, policyFlags);
                break;
        }
    }

    private void handleActionDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mAms.onTouchInteractionStart();
        this.mSendHoverEnterAndMoveDelayed.cancel();
        this.mSendHoverEnterAndMoveDelayed.clear();
        this.mSendHoverExitDelayed.cancel();
        if (this.mState.isTouchExploring() || com.android.server.accessibility.Flags.sendHoverEventsBasedOnEventStream()) {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(policyFlags);
        }
        if (this.mState.isClear()) {
            if (!this.mSendHoverEnterAndMoveDelayed.isPending()) {
                int pointerId = this.mReceivedPointerTracker.getPrimaryPointerId();
                int pointerIdBits = 1 << pointerId;
                if (this.mState.isServiceDetectingGestures()) {
                    this.mSendHoverEnterAndMoveDelayed.setPointerIdBits(pointerIdBits);
                    this.mSendHoverEnterAndMoveDelayed.setPolicyFlags(policyFlags);
                    this.mSendHoverEnterAndMoveDelayed.addEvent(event, rawEvent);
                } else {
                    this.mSendHoverEnterAndMoveDelayed.post(event, rawEvent, pointerIdBits, policyFlags);
                }
            } else {
                this.mSendHoverEnterAndMoveDelayed.addEvent(event, rawEvent);
            }
            this.mSendTouchExplorationEndDelayed.forceSendAndRemove();
            this.mSendTouchInteractionEndDelayed.forceSendAndRemove();
            this.mDispatcher.sendAccessibilityEvent(1048576);
            if (this.mTouchExplorationPassthroughRegion.contains((int) event.getX(), (int) event.getY())) {
                this.mState.startDelegating();
                android.view.MotionEvent event2 = android.view.MotionEvent.obtainNoHistory(event);
                this.mDispatcher.sendMotionEvent(event2, event2.getAction(), rawEvent, -1, policyFlags);
                this.mSendHoverEnterAndMoveDelayed.cancel();
            } else if (this.mGestureDetectionPassthroughRegion.contains((int) event.getX(), (int) event.getY())) {
                this.mSendHoverEnterAndMoveDelayed.forceSendAndRemove();
            }
        } else {
            this.mSendTouchInteractionEndDelayed.cancel();
        }
        if (this.mState.isServiceDetectingGestures()) {
            this.mAms.sendMotionEventToListeningServices(rawEvent);
        }
    }

    private void handleMotionEventStateTouchInteracting(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        switch (event.getActionMasked()) {
            case 0:
                this.mSendTouchInteractionEndDelayed.cancel();
                handleActionDown(event, rawEvent, policyFlags);
                break;
            case 1:
                handleActionUp(event, rawEvent, policyFlags);
                break;
            case 2:
                handleActionMoveStateTouchInteracting(event, rawEvent, policyFlags);
                break;
            case 5:
                handleActionPointerDown(event, rawEvent, policyFlags);
                break;
            case 6:
                if (this.mState.isServiceDetectingGestures()) {
                    this.mAms.sendMotionEventToListeningServices(rawEvent);
                }
                break;
        }
    }

    private void handleMotionEventStateTouchExploring(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        switch (event.getActionMasked()) {
            case 0:
                handleActionDownStateTouchExploring(event, rawEvent, policyFlags);
                break;
            case 1:
                handleActionUp(event, rawEvent, policyFlags);
                break;
            case 2:
                handleActionMoveStateTouchExploring(event, rawEvent, policyFlags);
                break;
            case 5:
                handleActionPointerDown(event, rawEvent, policyFlags);
                break;
        }
    }

    private void handleActionPointerDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
            this.mSendHoverEnterAndMoveDelayed.cancel();
            this.mSendHoverExitDelayed.cancel();
        } else {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(policyFlags);
        }
        if (this.mState.isServiceDetectingGestures()) {
            this.mAms.sendMotionEventToListeningServices(rawEvent);
        }
    }

    private void handleActionMoveStateTouchInteracting(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        int pointerIdBits;
        int pointerIdBits2;
        int pointerId = this.mReceivedPointerTracker.getPrimaryPointerId();
        int pointerIndex = event.findPointerIndex(pointerId);
        int pointerIdBits3 = 1 << pointerId;
        if (this.mState.isServiceDetectingGestures()) {
            this.mAms.sendMotionEventToListeningServices(rawEvent);
            this.mSendHoverEnterAndMoveDelayed.addEvent(event, rawEvent);
            return;
        }
        switch (event.getPointerCount()) {
            case 1:
                pointerIdBits = pointerIdBits3;
                if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
                    this.mSendHoverEnterAndMoveDelayed.addEvent(event, rawEvent);
                }
                break;
            case 2:
                if (this.mGestureDetector.isMultiFingerGesturesEnabled() && !this.mGestureDetector.isTwoFingerPassthroughEnabled()) {
                    return;
                }
                this.mSendHoverEnterAndMoveDelayed.cancel();
                this.mSendHoverExitDelayed.cancel();
                if (!this.mGestureDetector.isMultiFingerGesturesEnabled() || !this.mGestureDetector.isTwoFingerPassthroughEnabled()) {
                    pointerIdBits2 = pointerIdBits3;
                } else {
                    if (pointerIndex < 0) {
                        return;
                    }
                    int index = 0;
                    while (index < event.getPointerCount()) {
                        int id = event.getPointerId(index);
                        if (!this.mReceivedPointerTracker.isReceivedPointerDown(id)) {
                            android.util.Slog.e(LOG_TAG, "Invalid pointer id: " + id);
                        }
                        float deltaX = this.mReceivedPointerTracker.getReceivedPointerDownX(id) - rawEvent.getX(index);
                        float deltaY = this.mReceivedPointerTracker.getReceivedPointerDownY(id) - rawEvent.getY(index);
                        int pointerIdBits4 = pointerIdBits3;
                        double moveDelta = java.lang.Math.hypot(deltaX, deltaY);
                        if (moveDelta >= this.mTouchSlop * 2) {
                            index++;
                            pointerIdBits3 = pointerIdBits4;
                        } else {
                            return;
                        }
                    }
                    pointerIdBits2 = pointerIdBits3;
                }
                android.view.MotionEvent event2 = android.view.MotionEvent.obtainNoHistory(event);
                if (isDraggingGesture(event2)) {
                    if (isSendMotionEventsEnabled()) {
                        android.accessibilityservice.AccessibilityGestureEvent gestureEvent = new android.accessibilityservice.AccessibilityGestureEvent(-1, this.mDisplayId, this.mGestureDetector.getMotionEvents());
                        dispatchGesture(gestureEvent);
                    }
                    computeDraggingPointerIdIfNeeded(event2);
                    int pointerIdBits5 = 1 << this.mDraggingPointerId;
                    event2.setEdgeFlags(this.mReceivedPointerTracker.getLastReceivedDownEdgeFlags());
                    android.view.MotionEvent downEvent = computeDownEventForDrag(event2);
                    if (downEvent != null) {
                        this.mDispatcher.sendMotionEvent(downEvent, 0, rawEvent, pointerIdBits5, policyFlags);
                        this.mDispatcher.sendMotionEvent(event2, 2, rawEvent, pointerIdBits5, policyFlags);
                    } else {
                        this.mDispatcher.sendMotionEvent(event2, 0, rawEvent, pointerIdBits5, policyFlags);
                    }
                    this.mState.startDragging();
                    return;
                }
                if (isSendMotionEventsEnabled()) {
                    android.accessibilityservice.AccessibilityGestureEvent gestureEvent2 = new android.accessibilityservice.AccessibilityGestureEvent(-1, this.mDisplayId, this.mGestureDetector.getMotionEvents());
                    dispatchGesture(gestureEvent2);
                }
                this.mState.startDelegating();
                this.mDispatcher.sendDownForAllNotInjectedPointers(event2, policyFlags);
                return;
            default:
                pointerIdBits = pointerIdBits3;
                if (this.mGestureDetector.isMultiFingerGesturesEnabled()) {
                    if (this.mGestureDetector.isTwoFingerPassthroughEnabled() && event.getPointerCount() == 3 && allPointersDownOnBottomEdge(event)) {
                        if (DEBUG) {
                            android.util.Slog.d(LOG_TAG, "Three-finger edge swipe detected.");
                        }
                        if (isSendMotionEventsEnabled()) {
                            android.accessibilityservice.AccessibilityGestureEvent gestureEvent3 = new android.accessibilityservice.AccessibilityGestureEvent(-1, this.mDisplayId, this.mGestureDetector.getMotionEvents());
                            dispatchGesture(gestureEvent3);
                        }
                        this.mState.startDelegating();
                        if (this.mState.isTouchExploring()) {
                            this.mDispatcher.sendDownForAllNotInjectedPointers(event, policyFlags);
                        } else {
                            this.mDispatcher.sendDownForAllNotInjectedPointersWithOriginalDown(event, policyFlags);
                        }
                    }
                } else {
                    if (isSendMotionEventsEnabled()) {
                        android.accessibilityservice.AccessibilityGestureEvent gestureEvent4 = new android.accessibilityservice.AccessibilityGestureEvent(-1, this.mDisplayId, this.mGestureDetector.getMotionEvents());
                        dispatchGesture(gestureEvent4);
                    }
                    this.mState.startDelegating();
                    this.mDispatcher.sendDownForAllNotInjectedPointers(android.view.MotionEvent.obtainNoHistory(event), policyFlags);
                    return;
                }
                break;
        }
    }

    private void handleActionUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mState.isServiceDetectingGestures() && this.mState.isTouchInteracting()) {
            this.mAms.sendMotionEventToListeningServices(rawEvent);
        }
        this.mAms.onTouchInteractionEnd();
        int pointerId = event.getPointerId(event.getActionIndex());
        int pointerIdBits = 1 << pointerId;
        if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
            this.mSendHoverEnterAndMoveDelayed.repost();
            this.mSendHoverExitDelayed.post(event, rawEvent, pointerIdBits, policyFlags);
        } else {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(policyFlags);
        }
        if (!this.mSendTouchInteractionEndDelayed.isPending()) {
            this.mSendTouchInteractionEndDelayed.post();
        }
    }

    private void handleActionDownStateTouchExploring(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mSendTouchExplorationEndDelayed.cancel();
        this.mSendTouchInteractionEndDelayed.cancel();
        sendTouchExplorationGestureStartAndHoverEnterIfNeeded(policyFlags);
    }

    private void handleActionMoveStateTouchExploring(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        int pointerId = this.mReceivedPointerTracker.getPrimaryPointerId();
        int pointerIdBits = 1 << pointerId;
        int pointerIndex = event.findPointerIndex(pointerId);
        switch (event.getPointerCount()) {
            case 1:
                sendTouchExplorationGestureStartAndHoverEnterIfNeeded(policyFlags);
                this.mDispatcher.sendMotionEvent(event, 7, rawEvent, pointerIdBits, policyFlags);
                break;
            case 2:
                if (!this.mGestureDetector.isMultiFingerGesturesEnabled() || this.mGestureDetector.isTwoFingerPassthroughEnabled()) {
                    if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
                        this.mSendHoverEnterAndMoveDelayed.cancel();
                        this.mSendHoverExitDelayed.cancel();
                    }
                    float deltaX = this.mReceivedPointerTracker.getReceivedPointerDownX(pointerId) - rawEvent.getX(pointerIndex);
                    float deltaY = this.mReceivedPointerTracker.getReceivedPointerDownY(pointerId) - rawEvent.getY(pointerIndex);
                    double moveDelta = java.lang.Math.hypot(deltaX, deltaY);
                    if (moveDelta > this.mDoubleTapSlop) {
                        handleActionMoveStateTouchInteracting(event, rawEvent, policyFlags);
                    } else {
                        sendHoverExitAndTouchExplorationGestureEndIfNeeded(policyFlags);
                    }
                }
                break;
            default:
                if (!this.mGestureDetector.isMultiFingerGesturesEnabled()) {
                    if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
                        this.mSendHoverEnterAndMoveDelayed.cancel();
                        this.mSendHoverExitDelayed.cancel();
                    } else {
                        sendHoverExitAndTouchExplorationGestureEndIfNeeded(policyFlags);
                    }
                    handleActionMoveStateTouchInteracting(event, rawEvent, policyFlags);
                    break;
                }
                break;
        }
    }

    private void handleMotionEventStateDragging(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mGestureDetector.isMultiFingerGesturesEnabled() && !this.mGestureDetector.isTwoFingerPassthroughEnabled()) {
        }
        int pointerIdBits = 0;
        if (event.findPointerIndex(this.mDraggingPointerId) == -1) {
            android.util.Slog.e(LOG_TAG, "mDraggingPointerId doesn't match any pointers on current event. mDraggingPointerId: " + java.lang.Integer.toString(this.mDraggingPointerId) + ", Event: " + event);
            this.mDraggingPointerId = -1;
        } else {
            pointerIdBits = 1 << this.mDraggingPointerId;
        }
        switch (event.getActionMasked()) {
            case 0:
                android.util.Slog.e(LOG_TAG, "Dragging state can be reached only if two pointers are already down");
                clear(event, policyFlags);
                break;
            case 1:
                if (event.getPointerId(com.android.server.accessibility.gestures.GestureUtils.getActionIndex(event)) == this.mDraggingPointerId) {
                    this.mDraggingPointerId = -1;
                    this.mDispatcher.sendMotionEvent(event, 1, rawEvent, pointerIdBits, policyFlags);
                }
                this.mAms.onTouchInteractionEnd();
                this.mDispatcher.sendAccessibilityEvent(2097152);
                break;
            case 2:
                if (this.mDraggingPointerId != -1) {
                    if (this.mState.isServiceDetectingGestures()) {
                        this.mAms.sendMotionEventToListeningServices(rawEvent);
                        computeDraggingPointerIdIfNeeded(event);
                        this.mDispatcher.sendMotionEvent(event, 2, rawEvent, pointerIdBits, policyFlags);
                        break;
                    } else {
                        switch (event.getPointerCount()) {
                            case 1:
                                break;
                            case 2:
                                if (isDraggingGesture(event)) {
                                    computeDraggingPointerIdIfNeeded(event);
                                    this.mDispatcher.sendMotionEvent(event, 2, rawEvent, pointerIdBits, policyFlags);
                                } else {
                                    this.mState.startDelegating();
                                    this.mDraggingPointerId = -1;
                                    android.view.MotionEvent event2 = android.view.MotionEvent.obtainNoHistory(event);
                                    this.mDispatcher.sendMotionEvent(event2, 1, rawEvent, pointerIdBits, policyFlags);
                                    this.mDispatcher.sendDownForAllNotInjectedPointers(event2, policyFlags);
                                }
                                break;
                            default:
                                if (this.mState.isServiceDetectingGestures()) {
                                    this.mAms.sendMotionEventToListeningServices(rawEvent);
                                } else {
                                    this.mState.startDelegating();
                                    this.mDraggingPointerId = -1;
                                    android.view.MotionEvent event3 = android.view.MotionEvent.obtainNoHistory(event);
                                    this.mDispatcher.sendMotionEvent(event3, 1, rawEvent, pointerIdBits, policyFlags);
                                    this.mDispatcher.sendDownForAllNotInjectedPointers(event3, policyFlags);
                                }
                                break;
                        }
                    }
                }
                break;
            case 5:
                if (this.mDraggingPointerId != -1) {
                    this.mDispatcher.sendMotionEvent(event, 1, rawEvent, pointerIdBits, policyFlags);
                }
                if (this.mState.isServiceDetectingGestures()) {
                    this.mAms.sendMotionEventToListeningServices(rawEvent);
                } else {
                    this.mState.startDelegating();
                    this.mDispatcher.sendDownForAllNotInjectedPointers(event, policyFlags);
                }
                break;
            case 6:
                this.mDraggingPointerId = -1;
                this.mDispatcher.sendMotionEvent(event, 1, rawEvent, pointerIdBits, policyFlags);
                break;
        }
    }

    private void handleMotionEventStateDelegating(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        switch (event.getActionMasked()) {
            case 0:
                android.util.Slog.e(LOG_TAG, "Delegating state can only be reached if there is at least one pointer down!");
                clear(event, policyFlags);
                break;
            case 1:
                this.mDispatcher.sendMotionEvent(event, event.getAction(), rawEvent, -1, policyFlags);
                this.mAms.onTouchInteractionEnd();
                this.mDispatcher.clear();
                this.mDispatcher.sendAccessibilityEvent(2097152);
                break;
            default:
                this.mDispatcher.sendMotionEvent(event, event.getAction(), rawEvent, -1, policyFlags);
                break;
        }
    }

    private void endGestureDetection(boolean interactionEnd) {
        this.mAms.onTouchInteractionEnd();
        this.mDispatcher.sendAccessibilityEvent(524288);
        if (interactionEnd) {
            this.mDispatcher.sendAccessibilityEvent(2097152);
        }
        this.mExitGestureDetectionModeDelayed.cancel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendHoverExitAndTouchExplorationGestureEndIfNeeded(int policyFlags) {
        android.view.MotionEvent event = this.mState.getLastInjectedHoverEvent();
        if (event != null && event.getActionMasked() != 10) {
            int pointerIdBits = event.getPointerIdBits();
            if (!this.mSendTouchExplorationEndDelayed.isPending()) {
                this.mSendTouchExplorationEndDelayed.post();
            }
            this.mDispatcher.sendMotionEvent(event, 10, this.mState.getLastReceivedEvent(), pointerIdBits, policyFlags);
        }
    }

    private void sendTouchExplorationGestureStartAndHoverEnterIfNeeded(int policyFlags) {
        if (!this.mState.isTouchExploring()) {
            this.mDispatcher.sendAccessibilityEvent(512);
        }
        android.view.MotionEvent event = this.mState.getLastInjectedHoverEvent();
        if (event != null && event.getActionMasked() == 10) {
            int pointerIdBits = event.getPointerIdBits();
            this.mDispatcher.sendMotionEvent(event, 9, this.mState.getLastReceivedEvent(), pointerIdBits, policyFlags);
        }
    }

    private boolean isDraggingGesture(android.view.MotionEvent event) {
        float firstPtrX = event.getX(0);
        float firstPtrY = event.getY(0);
        float secondPtrX = event.getX(1);
        float secondPtrY = event.getY(1);
        float firstPtrDownX = this.mReceivedPointerTracker.getReceivedPointerDownX(0);
        float firstPtrDownY = this.mReceivedPointerTracker.getReceivedPointerDownY(0);
        float secondPtrDownX = this.mReceivedPointerTracker.getReceivedPointerDownX(1);
        float secondPtrDownY = this.mReceivedPointerTracker.getReceivedPointerDownY(1);
        return com.android.server.accessibility.gestures.GestureUtils.isDraggingGesture(firstPtrDownX, firstPtrDownY, secondPtrDownX, secondPtrDownY, firstPtrX, firstPtrY, secondPtrX, secondPtrY, MAX_DRAGGING_ANGLE_COS);
    }

    private void computeDraggingPointerIdIfNeeded(android.view.MotionEvent event) {
        if (event.getPointerCount() != 2) {
            this.mDraggingPointerId = -1;
            return;
        }
        if (this.mDraggingPointerId != -1) {
            int pointerIndex = event.findPointerIndex(this.mDraggingPointerId);
            if (event.findPointerIndex(pointerIndex) >= 0) {
                return;
            }
        }
        float firstPtrX = event.getX(0);
        float firstPtrY = event.getY(0);
        int firstPtrId = event.getPointerId(0);
        float secondPtrX = event.getX(1);
        float secondPtrY = event.getY(1);
        int secondPtrId = event.getPointerId(1);
        this.mDraggingPointerId = getDistanceToClosestEdge(firstPtrX, firstPtrY) < getDistanceToClosestEdge(secondPtrX, secondPtrY) ? firstPtrId : secondPtrId;
    }

    private float getDistanceToClosestEdge(float x, float y) {
        float distance;
        long width = this.mContext.getResources().getDisplayMetrics().widthPixels;
        long height = this.mContext.getResources().getDisplayMetrics().heightPixels;
        if (x < width - x) {
            distance = x;
        } else {
            distance = width - x;
        }
        if (distance > y) {
            distance = y;
        }
        if (distance > height - y) {
            float distance2 = height - y;
            return distance2;
        }
        return distance;
    }

    private android.view.MotionEvent computeDownEventForDrag(android.view.MotionEvent event) {
        if (!this.mState.isTouchExploring() && this.mDraggingPointerId != -1 && event != null) {
            float x = this.mReceivedPointerTracker.getReceivedPointerDownX(this.mDraggingPointerId);
            float y = this.mReceivedPointerTracker.getReceivedPointerDownY(this.mDraggingPointerId);
            long time = this.mReceivedPointerTracker.getReceivedPointerDownTime(this.mDraggingPointerId);
            android.view.MotionEvent.PointerCoords[] coords = {new android.view.MotionEvent.PointerCoords()};
            coords[0].x = x;
            coords[0].y = y;
            android.view.MotionEvent.PointerProperties[] properties = {new android.view.MotionEvent.PointerProperties()};
            properties[0].id = this.mDraggingPointerId;
            properties[0].toolType = 1;
            android.view.MotionEvent downEvent = android.view.MotionEvent.obtain(time, time, 0, 1, properties, coords, event.getMetaState(), event.getButtonState(), event.getXPrecision(), event.getYPrecision(), event.getDeviceId(), event.getEdgeFlags(), event.getSource(), event.getDisplayId(), event.getFlags());
            event.setDownTime(time);
            return downEvent;
        }
        return null;
    }

    private boolean allPointersDownOnBottomEdge(android.view.MotionEvent event) {
        long screenHeight = this.mContext.getResources().getDisplayMetrics().heightPixels;
        for (int i = 0; i < event.getPointerCount(); i++) {
            int pointerId = event.getPointerId(i);
            float pointerDownY = this.mReceivedPointerTracker.getReceivedPointerDownY(pointerId);
            if (pointerDownY < screenHeight - this.mEdgeSwipeHeightPixels) {
                if (DEBUG) {
                    android.util.Slog.d(LOG_TAG, "The pointer is not on the bottom edge" + pointerDownY);
                    return false;
                }
                return false;
            }
        }
        return true;
    }

    public com.android.server.accessibility.gestures.TouchState getState() {
        return this.mState;
    }

    @Override // com.android.server.accessibility.BaseEventStreamTransformation, com.android.server.accessibility.EventStreamTransformation
    public void setNext(com.android.server.accessibility.EventStreamTransformation next) {
        this.mDispatcher.setReceiver(next);
        super.setNext(next);
    }

    public void setServiceHandlesDoubleTap(boolean mode) {
        this.mGestureDetector.setServiceHandlesDoubleTap(mode);
    }

    public void setMultiFingerGesturesEnabled(boolean enabled) {
        this.mGestureDetector.setMultiFingerGesturesEnabled(enabled);
    }

    public void setTwoFingerPassthroughEnabled(boolean enabled) {
        this.mGestureDetector.setTwoFingerPassthroughEnabled(enabled);
    }

    public void setGestureDetectionPassthroughRegion(android.graphics.Region region) {
        this.mGestureDetectionPassthroughRegion = region;
    }

    public void setTouchExplorationPassthroughRegion(android.graphics.Region region) {
        this.mTouchExplorationPassthroughRegion = region;
    }

    public void setSendMotionEventsEnabled(boolean mode) {
        this.mGestureDetector.setSendMotionEventsEnabled(mode);
    }

    public boolean isSendMotionEventsEnabled() {
        return this.mGestureDetector.isSendMotionEventsEnabled();
    }

    public void setServiceDetectsGestures(boolean mode) {
        this.mState.setServiceDetectsGestures(mode);
    }

    private boolean shouldPerformGestureDetection(android.view.MotionEvent event) {
        if (this.mState.isServiceDetectingGestures() || this.mState.isDelegating() || this.mState.isDragging()) {
            return false;
        }
        if (event.getActionMasked() == 0) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            return (this.mTouchExplorationPassthroughRegion.contains(x, y) || this.mGestureDetectionPassthroughRegion.contains(x, y)) ? false : true;
        }
        return true;
    }

    public void requestTouchExploration() {
        android.view.MotionEvent event;
        if (DEBUG) {
            android.util.Slog.d(LOG_TAG, "Starting touch explorer from service.");
        }
        if (this.mState.isServiceDetectingGestures() && this.mState.isTouchInteracting()) {
            this.mHandler.removeCallbacks(this.mSendHoverEnterAndMoveDelayed);
            int pointerId = this.mReceivedPointerTracker.getPrimaryPointerId();
            if (pointerId == -1 && (event = this.mState.getLastReceivedEvent()) != null) {
                pointerId = event.getPointerId(0);
            }
            if (pointerId == -1) {
                android.util.Slog.e(LOG_TAG, "Unable to find a valid pointer for touch exploration.");
                return;
            }
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(pointerId);
            int pointerIdBits = 1 << pointerId;
            int policyFlags = this.mState.getLastReceivedPolicyFlags();
            this.mSendHoverEnterAndMoveDelayed.setPointerIdBits(pointerIdBits);
            this.mSendHoverEnterAndMoveDelayed.setPolicyFlags(policyFlags);
            this.mSendHoverEnterAndMoveDelayed.run();
            this.mSendHoverEnterAndMoveDelayed.clear();
            if (this.mReceivedPointerTracker.getReceivedPointerDownCount() == 0) {
                sendHoverExitAndTouchExplorationGestureEndIfNeeded(policyFlags);
            }
        }
    }

    public void requestDragging(int pointerId) {
        if (this.mState.isServiceDetectingGestures()) {
            if (pointerId < 0 || pointerId > 32 || !this.mReceivedPointerTracker.isReceivedPointerDown(pointerId)) {
                android.util.Slog.e(LOG_TAG, "Trying to drag with invalid pointer: " + pointerId);
                return;
            }
            if (this.mState.isTouchExploring()) {
                if (this.mSendHoverExitDelayed.isPending()) {
                    this.mSendHoverExitDelayed.forceSendAndRemove();
                }
                if (this.mSendTouchExplorationEndDelayed.isPending()) {
                    this.mSendTouchExplorationEndDelayed.forceSendAndRemove();
                }
            }
            if (!this.mState.isTouchInteracting()) {
                android.util.Slog.e(LOG_TAG, "Error: Trying to drag from " + com.android.server.accessibility.gestures.TouchState.getStateSymbolicName(this.mState.getState()));
                return;
            }
            this.mDraggingPointerId = pointerId;
            if (DEBUG) {
                android.util.Slog.d(LOG_TAG, "Drag requested on pointer " + this.mDraggingPointerId);
            }
            android.view.MotionEvent event = this.mState.getLastReceivedEvent();
            android.view.MotionEvent rawEvent = this.mState.getLastReceivedRawEvent();
            if (event == null || rawEvent == null) {
                android.util.Slog.e(LOG_TAG, "Unable to start dragging: unable to get last event.");
                return;
            }
            int policyFlags = this.mState.getLastReceivedPolicyFlags();
            int pointerIdBits = 1 << this.mDraggingPointerId;
            event.setEdgeFlags(this.mReceivedPointerTracker.getLastReceivedDownEdgeFlags());
            android.view.MotionEvent downEvent = computeDownEventForDrag(event);
            this.mState.startDragging();
            if (downEvent != null) {
                this.mDispatcher.sendMotionEvent(downEvent, 0, rawEvent, pointerIdBits, policyFlags);
                this.mDispatcher.sendMotionEvent(event, 2, rawEvent, pointerIdBits, policyFlags);
            } else {
                this.mDispatcher.sendMotionEvent(event, 0, rawEvent, pointerIdBits, policyFlags);
            }
        }
    }

    public void requestDelegating() {
        if (this.mState.isServiceDetectingGestures()) {
            if (this.mState.isTouchExploring()) {
                if (this.mSendHoverExitDelayed.isPending()) {
                    this.mSendHoverExitDelayed.forceSendAndRemove();
                }
                if (this.mSendTouchExplorationEndDelayed.isPending()) {
                    this.mSendTouchExplorationEndDelayed.forceSendAndRemove();
                }
            }
            if (!this.mState.isTouchInteracting() && !this.mState.isDragging()) {
                android.util.Slog.e(LOG_TAG, "Error: Trying to delegate from " + com.android.server.accessibility.gestures.TouchState.getStateSymbolicName(this.mState.getState()));
                return;
            }
            android.view.MotionEvent event = this.mState.getLastReceivedEvent();
            android.view.MotionEvent rawEvent = this.mState.getLastReceivedRawEvent();
            if (event == null || rawEvent == null) {
                android.util.Slog.d(LOG_TAG, "Unable to start delegating: unable to get last received event.");
                return;
            }
            int policyFlags = this.mState.getLastReceivedPolicyFlags();
            if (this.mState.isDragging()) {
                int pointerIdBits = 1 << this.mDraggingPointerId;
                this.mDispatcher.sendMotionEvent(event, 1, rawEvent, pointerIdBits, policyFlags);
            }
            this.mState.startDelegating();
            this.mDispatcher.sendDownForAllNotInjectedPointers(event, policyFlags);
        }
    }

    private final class ExitGestureDetectionModeDelayed implements java.lang.Runnable {
        private ExitGestureDetectionModeDelayed() {
        }

        public void post() {
            com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.postDelayed(this, 2000L);
        }

        public void cancel() {
            com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.removeCallbacks(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.accessibility.gestures.TouchExplorer.this.mDispatcher.sendAccessibilityEvent(524288);
            com.android.server.accessibility.gestures.TouchExplorer.this.clear();
        }
    }

    private static void checkForMalformedEvent(android.view.MotionEvent event) {
        if (event.getPointerCount() < 0) {
            throw new java.lang.IllegalArgumentException("Invalid pointer count: " + event.getPointerCount());
        }
        for (int i = 0; i < event.getPointerCount(); i++) {
            try {
                event.getPointerId(i);
                float x = event.getX(i);
                float y = event.getY(i);
                if (java.lang.Float.isNaN(x) || java.lang.Float.isNaN(y) || x < 0.0f || y < 0.0f) {
                    throw new java.lang.IllegalArgumentException("Invalid coordinates: (" + x + ", " + y + ")");
                }
            } catch (java.lang.Exception e) {
                throw new java.lang.IllegalArgumentException("Encountered exception getting details of pointer " + i + " / " + event.getPointerCount(), e);
            }
        }
    }

    class SendHoverEnterAndMoveDelayed implements java.lang.Runnable {
        private int mPointerIdBits;
        private int mPolicyFlags;
        private final java.lang.String LOG_TAG_SEND_HOVER_DELAYED = "SendHoverEnterAndMoveDelayed";
        private final java.util.List<android.view.MotionEvent> mEvents = new java.util.ArrayList();
        private final java.util.List<android.view.MotionEvent> mRawEvents = new java.util.ArrayList();

        SendHoverEnterAndMoveDelayed() {
        }

        public void post(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int pointerIdBits, int policyFlags) {
            cancel();
            addEvent(event, rawEvent);
            this.mPointerIdBits = pointerIdBits;
            this.mPolicyFlags = policyFlags;
            com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.postDelayed(this, com.android.server.accessibility.gestures.TouchExplorer.this.mDetermineUserIntentTimeout);
        }

        public void addEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent) {
            this.mEvents.add(android.view.MotionEvent.obtain(event));
            this.mRawEvents.add(android.view.MotionEvent.obtain(rawEvent));
        }

        public void cancel() {
            if (isPending()) {
                com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.removeCallbacks(this);
                clear();
            }
        }

        public void repost() {
            if (isPending()) {
                com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.removeCallbacks(this);
                com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.postDelayed(this, com.android.server.accessibility.gestures.TouchExplorer.this.mDetermineUserIntentTimeout);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isPending() {
            return com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.hasCallbacks(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clear() {
            this.mPointerIdBits = -1;
            this.mPolicyFlags = 0;
            int eventCount = this.mEvents.size();
            for (int i = eventCount - 1; i >= 0; i--) {
                this.mEvents.remove(i).recycle();
            }
            int rawEventcount = this.mRawEvents.size();
            for (int i2 = rawEventcount - 1; i2 >= 0; i2--) {
                this.mRawEvents.remove(i2).recycle();
            }
        }

        public void forceSendAndRemove() {
            if (isPending()) {
                run();
                cancel();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.accessibility.gestures.TouchExplorer.this.mReceivedPointerTracker.getReceivedPointerDownCount() > 1) {
                android.util.Slog.e(com.android.server.accessibility.gestures.TouchExplorer.LOG_TAG, "Attempted touch exploration with " + com.android.server.accessibility.gestures.TouchExplorer.this.mReceivedPointerTracker.getReceivedPointerDownCount() + " pointers down.");
                return;
            }
            if (this.mEvents.size() == 0) {
                return;
            }
            if (com.android.server.accessibility.Flags.sendHoverEventsBasedOnEventStream()) {
                com.android.server.accessibility.gestures.TouchExplorer.this.sendHoverExitAndTouchExplorationGestureEndIfNeeded(this.mPolicyFlags);
            }
            com.android.server.accessibility.gestures.TouchExplorer.this.mDispatcher.sendAccessibilityEvent(512);
            if (com.android.server.accessibility.gestures.TouchExplorer.this.isSendMotionEventsEnabled()) {
                android.accessibilityservice.AccessibilityGestureEvent gestureEvent = new android.accessibilityservice.AccessibilityGestureEvent(-2, com.android.server.accessibility.gestures.TouchExplorer.this.mState.getLastReceivedEvent().getDisplayId(), com.android.server.accessibility.gestures.TouchExplorer.this.mGestureDetector.getMotionEvents());
                com.android.server.accessibility.gestures.TouchExplorer.this.dispatchGesture(gestureEvent);
            }
            if (!this.mEvents.isEmpty() && !this.mRawEvents.isEmpty()) {
                com.android.server.accessibility.gestures.TouchExplorer.this.mDispatcher.sendMotionEvent(this.mEvents.get(0), 9, this.mRawEvents.get(0), this.mPointerIdBits, this.mPolicyFlags);
                if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                    android.util.Slog.d("SendHoverEnterAndMoveDelayed", "Injecting motion event: ACTION_HOVER_ENTER");
                }
                int eventCount = this.mEvents.size();
                for (int i = 1; i < eventCount; i++) {
                    com.android.server.accessibility.gestures.TouchExplorer.this.mDispatcher.sendMotionEvent(this.mEvents.get(i), 7, this.mRawEvents.get(i), this.mPointerIdBits, this.mPolicyFlags);
                    if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                        android.util.Slog.d("SendHoverEnterAndMoveDelayed", "Injecting motion event: ACTION_HOVER_MOVE");
                    }
                }
            }
            clear();
        }

        public void setPointerIdBits(int pointerIdBits) {
            this.mPointerIdBits = pointerIdBits;
        }

        public void setPolicyFlags(int policyFlags) {
            this.mPolicyFlags = policyFlags;
        }
    }

    class SendHoverExitDelayed implements java.lang.Runnable {
        private final java.lang.String LOG_TAG_SEND_HOVER_DELAYED = "SendHoverExitDelayed";
        private int mPointerIdBits;
        private int mPolicyFlags;
        private android.view.MotionEvent mPrototype;
        private android.view.MotionEvent mRawEvent;

        SendHoverExitDelayed() {
        }

        public void post(android.view.MotionEvent prototype, android.view.MotionEvent rawEvent, int pointerIdBits, int policyFlags) {
            cancel();
            this.mPrototype = android.view.MotionEvent.obtain(prototype);
            this.mRawEvent = android.view.MotionEvent.obtain(rawEvent);
            this.mPointerIdBits = pointerIdBits;
            this.mPolicyFlags = policyFlags;
            com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.postDelayed(this, com.android.server.accessibility.gestures.TouchExplorer.this.mDetermineUserIntentTimeout);
        }

        public void cancel() {
            if (isPending()) {
                com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.removeCallbacks(this);
                clear();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isPending() {
            return com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.hasCallbacks(this);
        }

        private void clear() {
            if (this.mPrototype != null) {
                this.mPrototype.recycle();
            }
            if (this.mRawEvent != null) {
                this.mRawEvent.recycle();
            }
            this.mPrototype = null;
            this.mRawEvent = null;
            this.mPointerIdBits = -1;
            this.mPolicyFlags = 0;
        }

        public void forceSendAndRemove() {
            if (isPending()) {
                run();
                cancel();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                android.util.Slog.d("SendHoverExitDelayed", "Injecting motion event: ACTION_HOVER_EXIT");
            }
            com.android.server.accessibility.gestures.TouchExplorer.this.mDispatcher.sendMotionEvent(this.mPrototype, 10, this.mRawEvent, this.mPointerIdBits, this.mPolicyFlags);
            if (!com.android.server.accessibility.gestures.TouchExplorer.this.mSendTouchExplorationEndDelayed.isPending()) {
                com.android.server.accessibility.gestures.TouchExplorer.this.mSendTouchExplorationEndDelayed.cancel();
                com.android.server.accessibility.gestures.TouchExplorer.this.mSendTouchExplorationEndDelayed.post();
            }
            if (com.android.server.accessibility.gestures.TouchExplorer.this.mSendTouchInteractionEndDelayed.isPending()) {
                com.android.server.accessibility.gestures.TouchExplorer.this.mSendTouchInteractionEndDelayed.cancel();
                com.android.server.accessibility.gestures.TouchExplorer.this.mSendTouchInteractionEndDelayed.post();
            }
            clear();
        }
    }

    private class SendAccessibilityEventDelayed implements java.lang.Runnable {
        private final int mDelay;
        private final int mEventType;

        public SendAccessibilityEventDelayed(int eventType, int delay) {
            this.mEventType = eventType;
            this.mDelay = delay;
        }

        public void cancel() {
            com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.removeCallbacks(this);
        }

        public void post() {
            com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.postDelayed(this, this.mDelay);
        }

        public boolean isPending() {
            return com.android.server.accessibility.gestures.TouchExplorer.this.mHandler.hasCallbacks(this);
        }

        public void forceSendAndRemove() {
            if (isPending()) {
                run();
                cancel();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.accessibility.gestures.TouchExplorer.this.mDispatcher.sendAccessibilityEvent(this.mEventType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchGesture(android.accessibilityservice.AccessibilityGestureEvent gestureEvent) {
        if (DEBUG) {
            android.util.Slog.d(LOG_TAG, "Dispatching gesture event:" + gestureEvent.toString());
        }
        this.mAms.onGesture(gestureEvent);
    }

    public java.lang.String toString() {
        return "TouchExplorer { mTouchState: " + this.mState + ", mDetermineUserIntentTimeout: " + this.mDetermineUserIntentTimeout + ", mDoubleTapSlop: " + this.mDoubleTapSlop + ", mDraggingPointerId: " + this.mDraggingPointerId + " }";
    }
}
