package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
class EventDispatcher {
    private static final int CLICK_LOCATION_ACCESSIBILITY_FOCUS = 1;
    private static final int CLICK_LOCATION_LAST_TOUCH_EXPLORED = 2;
    private static final int CLICK_LOCATION_NONE = 0;
    private static final java.lang.String LOG_TAG = "EventDispatcher";
    private final com.android.server.accessibility.AccessibilityManagerService mAms;
    private android.content.Context mContext;
    private int mLongPressingPointerDeltaX;
    private int mLongPressingPointerDeltaY;
    private com.android.server.accessibility.EventStreamTransformation mReceiver;
    private com.android.server.accessibility.gestures.TouchState mState;
    private int mLongPressingPointerId = -1;
    private final android.graphics.Point mTempPoint = new android.graphics.Point();

    EventDispatcher(android.content.Context context, com.android.server.accessibility.AccessibilityManagerService ams, com.android.server.accessibility.EventStreamTransformation receiver, com.android.server.accessibility.gestures.TouchState state) {
        this.mContext = context;
        this.mAms = ams;
        this.mReceiver = receiver;
        this.mState = state;
    }

    public void setReceiver(com.android.server.accessibility.EventStreamTransformation receiver) {
        this.mReceiver = receiver;
    }

    void sendMotionEvent(android.view.MotionEvent prototype, int action, android.view.MotionEvent rawEvent, int pointerIdBits, int policyFlags) {
        android.view.MotionEvent event;
        long downTime;
        prototype.setAction(action);
        if (pointerIdBits == -1) {
            event = prototype;
        } else {
            try {
                event = prototype.split(pointerIdBits);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(LOG_TAG, "sendMotionEvent: Failed to split motion event: " + e);
                return;
            }
        }
        if (action == 0) {
            downTime = event.getEventTime();
        } else {
            downTime = this.mState.getLastInjectedDownEventTime();
        }
        android.view.MotionEvent.PointerProperties[] properties = new android.view.MotionEvent.PointerProperties[event.getPointerCount()];
        android.view.MotionEvent.PointerCoords[] coords = new android.view.MotionEvent.PointerCoords[event.getPointerCount()];
        for (int i = 0; i < event.getPointerCount(); i++) {
            android.view.MotionEvent.PointerCoords c = new android.view.MotionEvent.PointerCoords();
            event.getPointerCoords(i, c);
            coords[i] = c;
            android.view.MotionEvent.PointerProperties p = new android.view.MotionEvent.PointerProperties();
            event.getPointerProperties(i, p);
            properties[i] = p;
        }
        android.view.MotionEvent event2 = android.view.MotionEvent.obtain(downTime, event.getEventTime(), event.getAction(), event.getPointerCount(), properties, coords, event.getMetaState(), event.getButtonState(), event.getXPrecision(), event.getYPrecision(), rawEvent.getDeviceId(), event.getEdgeFlags(), rawEvent.getSource(), event.getDisplayId(), event.getFlags(), event.getClassification());
        if (this.mLongPressingPointerId >= 0) {
            event2 = offsetEvent(event2, -this.mLongPressingPointerDeltaX, -this.mLongPressingPointerDeltaY);
        }
        if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
            android.util.Slog.d(LOG_TAG, "Injecting event: " + event2 + ", policyFlags=0x" + java.lang.Integer.toHexString(policyFlags));
        }
        int policyFlags2 = policyFlags | 1073741824;
        if (this.mReceiver != null) {
            this.mReceiver.onMotionEvent(event2, rawEvent, policyFlags2);
        } else {
            android.util.Slog.e(LOG_TAG, "Error sending event: no receiver specified.");
        }
        this.mState.onInjectedMotionEvent(event2);
        if (event2 != prototype) {
            event2.recycle();
        }
    }

    void sendAccessibilityEvent(int type) {
        android.view.accessibility.AccessibilityManager accessibilityManager = android.view.accessibility.AccessibilityManager.getInstance(this.mContext);
        if (accessibilityManager.isEnabled()) {
            android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain(type);
            event.setWindowId(this.mAms.getActiveWindowId());
            accessibilityManager.sendAccessibilityEvent(event);
            if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                android.util.Slog.d(LOG_TAG, "Sending accessibility event" + android.view.accessibility.AccessibilityEvent.eventTypeToString(type));
            }
        }
        this.mState.onInjectedAccessibilityEvent(type);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("=========================");
        builder.append("\nDown pointers #");
        builder.append(java.lang.Integer.bitCount(this.mState.getInjectedPointersDown()));
        builder.append(" [ ");
        for (int i = 0; i < 32; i++) {
            if (this.mState.isInjectedPointerDown(i)) {
                builder.append(i);
                builder.append(" ");
            }
        }
        builder.append("]");
        builder.append("\n=========================");
        return builder.toString();
    }

    private android.view.MotionEvent offsetEvent(android.view.MotionEvent event, int offsetX, int offsetY) {
        if (offsetX != 0 || offsetY != 0) {
            int remappedIndex = event.findPointerIndex(this.mLongPressingPointerId);
            int pointerCount = event.getPointerCount();
            android.view.MotionEvent.PointerProperties[] props = android.view.MotionEvent.PointerProperties.createArray(pointerCount);
            android.view.MotionEvent.PointerCoords[] coords = android.view.MotionEvent.PointerCoords.createArray(pointerCount);
            for (int i = 0; i < pointerCount; i++) {
                event.getPointerProperties(i, props[i]);
                event.getPointerCoords(i, coords[i]);
                if (i == remappedIndex) {
                    coords[i].x += offsetX;
                    coords[i].y += offsetY;
                }
            }
            return android.view.MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getPointerCount(), props, coords, event.getMetaState(), event.getButtonState(), 1.0f, 1.0f, event.getDeviceId(), event.getEdgeFlags(), event.getSource(), event.getDisplayId(), event.getFlags());
        }
        return event;
    }

    private int computeInjectionAction(int actionMasked, int pointerIndex) {
        switch (actionMasked) {
            case 0:
            case 5:
                if (this.mState.getInjectedPointerDownCount() == 0) {
                    return 0;
                }
                return (pointerIndex << 8) | 5;
            case 6:
                if (this.mState.getInjectedPointerDownCount() == 1) {
                    return 1;
                }
                return (pointerIndex << 8) | 6;
            default:
                return actionMasked;
        }
    }

    void sendDownForAllNotInjectedPointers(android.view.MotionEvent prototype, int policyFlags) {
        int pointerIdBits = 0;
        int pointerCount = prototype.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            try {
                int pointerId = prototype.getPointerId(i);
                if (!this.mState.isInjectedPointerDown(pointerId)) {
                    pointerIdBits |= 1 << pointerId;
                    int action = computeInjectionAction(0, i);
                    sendMotionEvent(prototype, action, this.mState.getLastReceivedEvent(), pointerIdBits, policyFlags);
                }
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(LOG_TAG, "sendDownForAllNotInjectedPointers: ignore invalid pointers: " + e);
                return;
            }
        }
    }

    void sendDownForAllNotInjectedPointersWithOriginalDown(android.view.MotionEvent prototype, int policyFlags) {
        int pointerIdBits = 0;
        int pointerCount = prototype.getPointerCount();
        android.view.MotionEvent event = computeInjectionDownEvent(prototype);
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = prototype.getPointerId(i);
            if (!this.mState.isInjectedPointerDown(pointerId)) {
                pointerIdBits |= 1 << pointerId;
                int action = computeInjectionAction(0, i);
                sendMotionEvent(event, action, this.mState.getLastReceivedEvent(), pointerIdBits, policyFlags);
            }
        }
    }

    private android.view.MotionEvent computeInjectionDownEvent(android.view.MotionEvent prototype) {
        int pointerCount = prototype.getPointerCount();
        if (pointerCount != this.mState.getReceivedPointerTracker().getReceivedPointerDownCount()) {
            android.util.Slog.w(LOG_TAG, "The pointer count doesn't match the received count.");
            return android.view.MotionEvent.obtain(prototype);
        }
        android.view.MotionEvent.PointerCoords[] coords = new android.view.MotionEvent.PointerCoords[pointerCount];
        android.view.MotionEvent.PointerProperties[] properties = new android.view.MotionEvent.PointerProperties[pointerCount];
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = prototype.getPointerId(i);
            float x = this.mState.getReceivedPointerTracker().getReceivedPointerDownX(pointerId);
            float y = this.mState.getReceivedPointerTracker().getReceivedPointerDownY(pointerId);
            coords[i] = new android.view.MotionEvent.PointerCoords();
            coords[i].x = x;
            coords[i].y = y;
            properties[i] = new android.view.MotionEvent.PointerProperties();
            properties[i].id = pointerId;
            properties[i].toolType = 1;
        }
        android.view.MotionEvent event = android.view.MotionEvent.obtain(prototype.getDownTime(), prototype.getDownTime(), prototype.getAction(), pointerCount, properties, coords, prototype.getMetaState(), prototype.getButtonState(), prototype.getXPrecision(), prototype.getYPrecision(), prototype.getDeviceId(), prototype.getEdgeFlags(), prototype.getSource(), prototype.getFlags());
        return event;
    }

    void sendUpForInjectedDownPointers(android.view.MotionEvent prototype, int policyFlags) {
        int pointerIdBits = prototype.getPointerIdBits();
        int pointerCount = prototype.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = prototype.getPointerId(i);
            if (this.mState.isInjectedPointerDown(pointerId)) {
                int action = computeInjectionAction(6, i);
                sendMotionEvent(prototype, action, this.mState.getLastReceivedEvent(), pointerIdBits, policyFlags);
                pointerIdBits &= ~(1 << pointerId);
            }
        }
    }

    public boolean longPressWithTouchEvents(android.view.MotionEvent event, int policyFlags) {
        android.graphics.Point clickLocation = this.mTempPoint;
        int result = computeClickLocation(clickLocation);
        if (result == 0 || event == null) {
            return false;
        }
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        this.mLongPressingPointerId = pointerId;
        this.mLongPressingPointerDeltaX = ((int) event.getX(pointerIndex)) - clickLocation.x;
        this.mLongPressingPointerDeltaY = ((int) event.getY(pointerIndex)) - clickLocation.y;
        sendDownForAllNotInjectedPointers(event, policyFlags);
        return true;
    }

    void clear() {
        this.mLongPressingPointerId = -1;
        this.mLongPressingPointerDeltaX = 0;
        this.mLongPressingPointerDeltaY = 0;
    }

    public void clickWithTouchEvents(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        int pointerIndex = event.getActionIndex();
        event.getPointerId(pointerIndex);
        android.graphics.Point clickLocation = this.mTempPoint;
        int result = computeClickLocation(clickLocation);
        if (result == 0) {
            android.util.Slog.e(LOG_TAG, "Unable to compute click location.");
            return;
        }
        android.view.MotionEvent.PointerProperties[] properties = {new android.view.MotionEvent.PointerProperties()};
        event.getPointerProperties(pointerIndex, properties[0]);
        android.view.MotionEvent.PointerCoords[] coords = {new android.view.MotionEvent.PointerCoords()};
        coords[0].x = clickLocation.x;
        coords[0].y = clickLocation.y;
        android.view.MotionEvent clickEvent = android.view.MotionEvent.obtain(event.getDownTime(), event.getEventTime(), 0, 1, properties, coords, 0, 0, 1.0f, 1.0f, event.getDeviceId(), 0, event.getSource(), event.getDisplayId(), event.getFlags());
        boolean targetAccessibilityFocus = result == 1;
        sendActionDownAndUp(clickEvent, rawEvent, policyFlags, targetAccessibilityFocus);
        clickEvent.recycle();
    }

    private int computeClickLocation(android.graphics.Point outLocation) {
        if (this.mState.getLastInjectedHoverEventForClick() != null) {
            int lastExplorePointerIndex = this.mState.getLastInjectedHoverEventForClick().getActionIndex();
            outLocation.x = (int) this.mState.getLastInjectedHoverEventForClick().getX(lastExplorePointerIndex);
            outLocation.y = (int) this.mState.getLastInjectedHoverEventForClick().getY(lastExplorePointerIndex);
            if (!this.mAms.accessibilityFocusOnlyInActiveWindow() || this.mState.getLastTouchedWindowId() == this.mAms.getActiveWindowId()) {
                return this.mAms.getAccessibilityFocusClickPointInScreen(outLocation) ? 1 : 2;
            }
        }
        return this.mAms.getAccessibilityFocusClickPointInScreen(outLocation) ? 1 : 0;
    }

    private void sendActionDownAndUp(android.view.MotionEvent prototype, android.view.MotionEvent rawEvent, int policyFlags, boolean targetAccessibilityFocus) {
        int pointerId = prototype.getPointerId(prototype.getActionIndex());
        int pointerIdBits = 1 << pointerId;
        prototype.setTargetAccessibilityFocus(targetAccessibilityFocus);
        sendMotionEvent(prototype, 0, rawEvent, pointerIdBits, policyFlags);
        prototype.setTargetAccessibilityFocus(targetAccessibilityFocus);
        sendMotionEvent(prototype, 1, rawEvent, pointerIdBits, policyFlags);
    }
}
