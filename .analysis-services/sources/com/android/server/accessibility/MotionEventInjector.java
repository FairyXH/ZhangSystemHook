package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class MotionEventInjector extends com.android.server.accessibility.BaseEventStreamTransformation implements android.os.Handler.Callback {
    private static final int EVENT_BUTTON_STATE = 0;
    private static final int EVENT_EDGE_FLAGS = 0;
    private static final int EVENT_FLAGS = 0;
    private static final int EVENT_META_STATE = 0;
    private static final int EVENT_SOURCE = 4098;
    private static final float EVENT_X_PRECISION = 1.0f;
    private static final float EVENT_Y_PRECISION = 1.0f;
    private static final java.lang.String LOG_TAG = "MotionEventInjector";
    private static final int MESSAGE_INJECT_EVENTS = 2;
    private static final int MESSAGE_SEND_MOTION_EVENT = 1;
    private static android.view.MotionEvent.PointerCoords[] sPointerCoords;
    private static android.view.MotionEvent.PointerProperties[] sPointerProps;
    private long mDownTime;
    private final android.os.Handler mHandler;
    private long mLastScheduledEventTime;
    private android.accessibilityservice.GestureDescription.TouchPoint[] mLastTouchPoints;
    private int mNumLastTouchPoints;
    private android.accessibilityservice.IAccessibilityServiceClient mServiceInterfaceForCurrentGesture;
    private final com.android.server.accessibility.AccessibilityTraceManager mTrace;
    private final android.util.SparseArray<java.lang.Boolean> mOpenGesturesInProgress = new android.util.SparseArray<>();
    private android.util.IntArray mSequencesInProgress = new android.util.IntArray(5);
    private boolean mIsDestroyed = false;
    private android.util.SparseIntArray mStrokeIdToPointerId = new android.util.SparseIntArray(5);

    public MotionEventInjector(android.os.Looper looper, com.android.server.accessibility.AccessibilityTraceManager trace) {
        this.mHandler = new android.os.Handler(looper, this);
        this.mTrace = trace;
    }

    public MotionEventInjector(android.os.Handler handler, com.android.server.accessibility.AccessibilityTraceManager trace) {
        this.mHandler = handler;
        this.mTrace = trace;
    }

    public void injectEvents(java.util.List<android.accessibilityservice.GestureDescription.GestureStep> gestureSteps, android.accessibilityservice.IAccessibilityServiceClient serviceInterface, int sequence, int displayId) {
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = gestureSteps;
        args.arg2 = serviceInterface;
        args.argi1 = sequence;
        args.argi2 = displayId;
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2, args));
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mTrace.isA11yTracingEnabledForTypes(12288L)) {
            this.mTrace.logTrace("MotionEventInjector.onMotionEvent", 12288L, "event=" + event + ";rawEvent=" + rawEvent + ";policyFlags=" + policyFlags);
        }
        if (event.isFromSource(8194) && event.getActionMasked() == 7 && this.mOpenGesturesInProgress.get(4098, false).booleanValue()) {
            return;
        }
        cancelAnyPendingInjectedEvents();
        sendMotionEventToNext(event, rawEvent, policyFlags | 131072);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void clearEvents(int inputSource) {
        if (!this.mHandler.hasMessages(1)) {
            this.mOpenGesturesInProgress.put(inputSource, false);
        }
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onDestroy() {
        cancelAnyPendingInjectedEvents();
        this.mIsDestroyed = true;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(android.os.Message message) {
        if (message.what == 2) {
            com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) message.obj;
            injectEventsMainThread((java.util.List) args.arg1, (android.accessibilityservice.IAccessibilityServiceClient) args.arg2, args.argi1, args.argi2);
            args.recycle();
            return true;
        }
        if (message.what != 1) {
            android.util.Slog.e(LOG_TAG, "Unknown message: " + message.what);
            return false;
        }
        android.view.MotionEvent motionEvent = (android.view.MotionEvent) message.obj;
        sendMotionEventToNext(motionEvent, motionEvent, 1073872896);
        boolean isEndOfSequence = message.arg1 != 0;
        if (isEndOfSequence) {
            notifyService(this.mServiceInterfaceForCurrentGesture, this.mSequencesInProgress.get(0), true);
            this.mSequencesInProgress.remove(0);
        }
        return true;
    }

    private void injectEventsMainThread(java.util.List<android.accessibilityservice.GestureDescription.GestureStep> gestureSteps, android.accessibilityservice.IAccessibilityServiceClient serviceInterface, int sequence, int displayId) {
        com.android.server.accessibility.MotionEventInjector motionEventInjector = this;
        int i = 0;
        if (motionEventInjector.mIsDestroyed) {
            if (serviceInterface != null) {
                try {
                    serviceInterface.onPerformGestureResult(sequence, false);
                    return;
                } catch (android.os.RemoteException re) {
                    android.util.Slog.e(LOG_TAG, "Error sending status with mIsDestroyed to " + serviceInterface, re);
                    return;
                }
            }
            return;
        }
        if (getNext() == null) {
            motionEventInjector.notifyService(serviceInterface, sequence, false);
            return;
        }
        boolean continuingGesture = newGestureTriesToContinueOldOne(gestureSteps);
        if (continuingGesture && (serviceInterface != motionEventInjector.mServiceInterfaceForCurrentGesture || !prepareToContinueOldGesture(gestureSteps))) {
            cancelAnyPendingInjectedEvents();
            motionEventInjector.notifyService(serviceInterface, sequence, false);
            return;
        }
        if (!continuingGesture) {
            cancelAnyPendingInjectedEvents();
            motionEventInjector.cancelAnyGestureInProgress(4098);
        }
        motionEventInjector.mServiceInterfaceForCurrentGesture = serviceInterface;
        long currentTime = android.os.SystemClock.uptimeMillis();
        java.util.List<android.view.MotionEvent> events = motionEventInjector.getMotionEventsFromGestureSteps(gestureSteps, motionEventInjector.mSequencesInProgress.size() == 0 ? currentTime : motionEventInjector.mLastScheduledEventTime);
        if (events.isEmpty()) {
            motionEventInjector.notifyService(serviceInterface, sequence, false);
            return;
        }
        motionEventInjector.mSequencesInProgress.add(sequence);
        int i2 = 0;
        while (i2 < events.size()) {
            android.view.MotionEvent event = events.get(i2);
            event.setDisplayId(displayId);
            int isEndOfSequence = i2 == events.size() - 1 ? 1 : i;
            android.os.Message message = motionEventInjector.mHandler.obtainMessage(1, isEndOfSequence, i, event);
            motionEventInjector.mLastScheduledEventTime = event.getEventTime();
            motionEventInjector.mHandler.sendMessageDelayed(message, java.lang.Math.max(0L, event.getEventTime() - currentTime));
            i2++;
            motionEventInjector = this;
            continuingGesture = continuingGesture;
            currentTime = currentTime;
            i = 0;
        }
    }

    private boolean newGestureTriesToContinueOldOne(java.util.List<android.accessibilityservice.GestureDescription.GestureStep> gestureSteps) {
        if (gestureSteps.isEmpty()) {
            return false;
        }
        android.accessibilityservice.GestureDescription.GestureStep firstStep = gestureSteps.get(0);
        for (int i = 0; i < firstStep.numTouchPoints; i++) {
            if (!firstStep.touchPoints[i].mIsStartOfPath) {
                return true;
            }
        }
        return false;
    }

    private boolean prepareToContinueOldGesture(java.util.List<android.accessibilityservice.GestureDescription.GestureStep> gestureSteps) {
        if (gestureSteps.isEmpty() || this.mLastTouchPoints == null || this.mNumLastTouchPoints == 0) {
            return false;
        }
        android.accessibilityservice.GestureDescription.GestureStep firstStep = gestureSteps.get(0);
        int numContinuedStrokes = 0;
        for (int i = 0; i < firstStep.numTouchPoints; i++) {
            android.accessibilityservice.GestureDescription.TouchPoint touchPoint = firstStep.touchPoints[i];
            if (!touchPoint.mIsStartOfPath) {
                int continuedPointerId = this.mStrokeIdToPointerId.get(touchPoint.mContinuedStrokeId, -1);
                if (continuedPointerId == -1) {
                    android.util.Slog.w(LOG_TAG, "Can't continue gesture due to unknown continued stroke id in " + touchPoint);
                    return false;
                }
                this.mStrokeIdToPointerId.put(touchPoint.mStrokeId, continuedPointerId);
                int lastPointIndex = findPointByStrokeId(this.mLastTouchPoints, this.mNumLastTouchPoints, touchPoint.mContinuedStrokeId);
                if (lastPointIndex < 0) {
                    android.util.Slog.w(LOG_TAG, "Can't continue gesture due continued gesture id of " + touchPoint + " not matching any previous strokes in " + java.util.Arrays.asList(this.mLastTouchPoints));
                    return false;
                }
                if (this.mLastTouchPoints[lastPointIndex].mIsEndOfPath || this.mLastTouchPoints[lastPointIndex].mX != touchPoint.mX || this.mLastTouchPoints[lastPointIndex].mY != touchPoint.mY) {
                    android.util.Slog.w(LOG_TAG, "Can't continue gesture due to points mismatch between " + this.mLastTouchPoints[lastPointIndex] + " and " + touchPoint);
                    return false;
                }
                this.mLastTouchPoints[lastPointIndex].mStrokeId = touchPoint.mStrokeId;
            }
            numContinuedStrokes++;
        }
        for (int i2 = 0; i2 < this.mNumLastTouchPoints; i2++) {
            if (!this.mLastTouchPoints[i2].mIsEndOfPath) {
                numContinuedStrokes--;
            }
        }
        return numContinuedStrokes == 0;
    }

    private void sendMotionEventToNext(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (getNext() != null) {
            super.onMotionEvent(event, rawEvent, policyFlags);
            if (event.getActionMasked() == 0) {
                this.mOpenGesturesInProgress.put(event.getSource(), true);
            }
            if (event.getActionMasked() == 1 || event.getActionMasked() == 3) {
                this.mOpenGesturesInProgress.put(event.getSource(), false);
            }
        }
    }

    private void cancelAnyGestureInProgress(int source) {
        if (getNext() != null && this.mOpenGesturesInProgress.get(source, false).booleanValue()) {
            long now = android.os.SystemClock.uptimeMillis();
            android.view.MotionEvent cancelEvent = obtainMotionEvent(now, now, 3, getLastTouchPoints(), 1);
            sendMotionEventToNext(cancelEvent, cancelEvent, 1073872896);
            this.mOpenGesturesInProgress.put(source, false);
        }
    }

    private void cancelAnyPendingInjectedEvents() {
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
            cancelAnyGestureInProgress(4098);
            for (int i = this.mSequencesInProgress.size() - 1; i >= 0; i--) {
                notifyService(this.mServiceInterfaceForCurrentGesture, this.mSequencesInProgress.get(i), false);
                this.mSequencesInProgress.remove(i);
            }
        } else if (this.mNumLastTouchPoints != 0) {
            cancelAnyGestureInProgress(4098);
        }
        this.mNumLastTouchPoints = 0;
        this.mStrokeIdToPointerId.clear();
    }

    private void notifyService(android.accessibilityservice.IAccessibilityServiceClient service, int sequence, boolean success) {
        try {
            service.onPerformGestureResult(sequence, success);
        } catch (java.lang.Exception re) {
            android.util.Slog.e(LOG_TAG, "Error sending motion event injection status to " + this.mServiceInterfaceForCurrentGesture, re);
        }
    }

    private java.util.List<android.view.MotionEvent> getMotionEventsFromGestureSteps(java.util.List<android.accessibilityservice.GestureDescription.GestureStep> steps, long startTime) {
        java.util.List<android.view.MotionEvent> motionEvents = new java.util.ArrayList<>();
        android.accessibilityservice.GestureDescription.TouchPoint[] lastTouchPoints = getLastTouchPoints();
        for (int i = 0; i < steps.size(); i++) {
            android.accessibilityservice.GestureDescription.GestureStep step = steps.get(i);
            int currentTouchPointSize = step.numTouchPoints;
            if (currentTouchPointSize > lastTouchPoints.length) {
                this.mNumLastTouchPoints = 0;
                motionEvents.clear();
                return motionEvents;
            }
            appendMoveEventIfNeeded(motionEvents, step.touchPoints, currentTouchPointSize, startTime + step.timeSinceGestureStart);
            appendUpEvents(motionEvents, step.touchPoints, currentTouchPointSize, startTime + step.timeSinceGestureStart);
            appendDownEvents(motionEvents, step.touchPoints, currentTouchPointSize, startTime + step.timeSinceGestureStart);
        }
        return motionEvents;
    }

    private android.accessibilityservice.GestureDescription.TouchPoint[] getLastTouchPoints() {
        if (this.mLastTouchPoints == null) {
            int capacity = android.accessibilityservice.GestureDescription.getMaxStrokeCount();
            this.mLastTouchPoints = new android.accessibilityservice.GestureDescription.TouchPoint[capacity];
            for (int i = 0; i < capacity; i++) {
                this.mLastTouchPoints[i] = new android.accessibilityservice.GestureDescription.TouchPoint();
            }
        }
        return this.mLastTouchPoints;
    }

    private void appendMoveEventIfNeeded(java.util.List<android.view.MotionEvent> motionEvents, android.accessibilityservice.GestureDescription.TouchPoint[] currentTouchPoints, int currentTouchPointsSize, long currentTime) {
        boolean moveFound = false;
        android.accessibilityservice.GestureDescription.TouchPoint[] lastTouchPoints = getLastTouchPoints();
        for (int i = 0; i < currentTouchPointsSize; i++) {
            int lastPointsIndex = findPointByStrokeId(lastTouchPoints, this.mNumLastTouchPoints, currentTouchPoints[i].mStrokeId);
            if (lastPointsIndex >= 0) {
                moveFound |= (lastTouchPoints[lastPointsIndex].mX == currentTouchPoints[i].mX && lastTouchPoints[lastPointsIndex].mY == currentTouchPoints[i].mY) ? false : true;
                lastTouchPoints[lastPointsIndex].copyFrom(currentTouchPoints[i]);
            }
        }
        if (moveFound) {
            motionEvents.add(obtainMotionEvent(this.mDownTime, currentTime, 2, lastTouchPoints, this.mNumLastTouchPoints));
        }
    }

    private void appendUpEvents(java.util.List<android.view.MotionEvent> motionEvents, android.accessibilityservice.GestureDescription.TouchPoint[] currentTouchPoints, int currentTouchPointsSize, long currentTime) {
        int indexOfUpEvent;
        android.accessibilityservice.GestureDescription.TouchPoint[] lastTouchPoints = getLastTouchPoints();
        for (int i = 0; i < currentTouchPointsSize; i++) {
            if (currentTouchPoints[i].mIsEndOfPath && (indexOfUpEvent = findPointByStrokeId(lastTouchPoints, this.mNumLastTouchPoints, currentTouchPoints[i].mStrokeId)) >= 0) {
                int action = this.mNumLastTouchPoints == 1 ? 1 : 6;
                motionEvents.add(obtainMotionEvent(this.mDownTime, currentTime, action | (indexOfUpEvent << 8), lastTouchPoints, this.mNumLastTouchPoints));
                for (int j = indexOfUpEvent; j < this.mNumLastTouchPoints - 1; j++) {
                    lastTouchPoints[j].copyFrom(this.mLastTouchPoints[j + 1]);
                }
                int j2 = this.mNumLastTouchPoints;
                this.mNumLastTouchPoints = j2 - 1;
                if (this.mNumLastTouchPoints == 0) {
                    this.mStrokeIdToPointerId.clear();
                }
            }
        }
    }

    private void appendDownEvents(java.util.List<android.view.MotionEvent> motionEvents, android.accessibilityservice.GestureDescription.TouchPoint[] currentTouchPoints, int currentTouchPointsSize, long currentTime) {
        android.accessibilityservice.GestureDescription.TouchPoint[] lastTouchPoints = getLastTouchPoints();
        for (int i = 0; i < currentTouchPointsSize; i++) {
            if (currentTouchPoints[i].mIsStartOfPath) {
                int i2 = this.mNumLastTouchPoints;
                this.mNumLastTouchPoints = i2 + 1;
                lastTouchPoints[i2].copyFrom(currentTouchPoints[i]);
                int action = this.mNumLastTouchPoints == 1 ? 0 : 5;
                if (action == 0) {
                    this.mDownTime = currentTime;
                }
                motionEvents.add(obtainMotionEvent(this.mDownTime, currentTime, action | (i << 8), lastTouchPoints, this.mNumLastTouchPoints));
            }
        }
    }

    private android.view.MotionEvent obtainMotionEvent(long downTime, long eventTime, int action, android.accessibilityservice.GestureDescription.TouchPoint[] touchPoints, int touchPointsSize) {
        if (sPointerCoords == null || sPointerCoords.length < touchPointsSize) {
            sPointerCoords = new android.view.MotionEvent.PointerCoords[touchPointsSize];
            for (int i = 0; i < touchPointsSize; i++) {
                sPointerCoords[i] = new android.view.MotionEvent.PointerCoords();
            }
        }
        if (sPointerProps == null || sPointerProps.length < touchPointsSize) {
            sPointerProps = new android.view.MotionEvent.PointerProperties[touchPointsSize];
            for (int i2 = 0; i2 < touchPointsSize; i2++) {
                sPointerProps[i2] = new android.view.MotionEvent.PointerProperties();
            }
        }
        for (int i3 = 0; i3 < touchPointsSize; i3++) {
            int pointerId = this.mStrokeIdToPointerId.get(touchPoints[i3].mStrokeId, -1);
            if (pointerId == -1) {
                pointerId = getUnusedPointerId();
                this.mStrokeIdToPointerId.put(touchPoints[i3].mStrokeId, pointerId);
            }
            sPointerProps[i3].id = pointerId;
            sPointerProps[i3].toolType = 0;
            sPointerCoords[i3].clear();
            sPointerCoords[i3].pressure = 1.0f;
            sPointerCoords[i3].size = 1.0f;
            sPointerCoords[i3].x = touchPoints[i3].mX;
            sPointerCoords[i3].y = touchPoints[i3].mY;
        }
        return android.view.MotionEvent.obtain(downTime, eventTime, action, touchPointsSize, sPointerProps, sPointerCoords, 0, 0, 1.0f, 1.0f, -1, 0, 4098, 0);
    }

    private static int findPointByStrokeId(android.accessibilityservice.GestureDescription.TouchPoint[] touchPoints, int touchPointsSize, int strokeId) {
        for (int i = 0; i < touchPointsSize; i++) {
            if (touchPoints[i].mStrokeId == strokeId) {
                return i;
            }
        }
        return -1;
    }

    private int getUnusedPointerId() {
        int pointerId = 0;
        while (this.mStrokeIdToPointerId.indexOfValue(pointerId) >= 0) {
            pointerId++;
            if (pointerId >= 10) {
                return 10;
            }
        }
        return pointerId;
    }
}
