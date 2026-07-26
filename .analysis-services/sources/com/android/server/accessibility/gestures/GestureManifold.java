package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
public class GestureManifold implements com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener {
    private static final java.lang.String LOG_TAG = "GestureManifold";
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private com.android.server.accessibility.gestures.GestureManifold.Listener mListener;
    private com.android.server.accessibility.gestures.TouchState mState;
    private final java.util.List<com.android.server.accessibility.gestures.GestureMatcher> mGestures = new java.util.ArrayList();
    private boolean mServiceHandlesDoubleTap = false;
    private boolean mSendMotionEventsEnabled = false;
    private final java.util.List<com.android.server.accessibility.gestures.GestureMatcher> mMultiFingerGestures = new java.util.ArrayList();
    private final java.util.List<com.android.server.accessibility.gestures.GestureMatcher> mTwoFingerSwipes = new java.util.ArrayList();
    private java.util.List<android.view.MotionEvent> mEvents = new java.util.ArrayList();
    boolean mMultiFingerGesturesEnabled = false;
    private boolean mTwoFingerPassthroughEnabled = false;

    public interface Listener {
        boolean onDoubleTap(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i);

        void onDoubleTapAndHold(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i);

        boolean onGestureCancelled(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i);

        boolean onGestureCompleted(android.accessibilityservice.AccessibilityGestureEvent accessibilityGestureEvent);

        boolean onGestureStarted();
    }

    public GestureManifold(android.content.Context context, com.android.server.accessibility.gestures.GestureManifold.Listener listener, com.android.server.accessibility.gestures.TouchState state, android.os.Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        this.mListener = listener;
        this.mState = state;
        this.mGestures.add(new com.android.server.accessibility.gestures.MultiTap(context, 2, 17, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.MultiTapAndHold(context, 2, 18, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.SecondFingerMultiTap(context, 2, 17, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 1, 4, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 0, 3, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 2, 1, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 3, 2, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 0, 1, 5, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 0, 2, 9, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 0, 3, 10, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 1, 2, 11, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 1, 3, 12, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 1, 0, 6, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 3, 2, 8, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 3, 0, 15, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 3, 1, 16, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 2, 3, 7, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 2, 0, 13, this));
        this.mGestures.add(new com.android.server.accessibility.gestures.Swipe(context, 2, 1, 14, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 2, 1, 19, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 2, 2, 20, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTapAndHold(this.mContext, 2, 2, 40, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 2, 3, 21, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTapAndHold(this.mContext, 2, 3, 43, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 3, 1, 22, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 3, 2, 23, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTapAndHold(this.mContext, 3, 1, 44, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTapAndHold(this.mContext, 3, 2, 41, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 3, 3, 24, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTapAndHold(this.mContext, 3, 3, 45, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 3, 3, 24, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 4, 1, 37, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 4, 2, 38, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTapAndHold(this.mContext, 4, 2, 42, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerMultiTap(this.mContext, 4, 3, 39, this));
        this.mTwoFingerSwipes.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 2, 3, 26, this));
        this.mTwoFingerSwipes.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 2, 0, 27, this));
        this.mTwoFingerSwipes.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 2, 1, 28, this));
        this.mTwoFingerSwipes.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 2, 2, 25, this));
        this.mMultiFingerGestures.addAll(this.mTwoFingerSwipes);
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 3, 3, 30, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 3, 0, 31, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 3, 1, 32, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 3, 2, 29, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 4, 3, 34, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 4, 0, 35, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 4, 1, 36, this));
        this.mMultiFingerGestures.add(new com.android.server.accessibility.gestures.MultiFingerSwipe(context, 4, 2, 33, this));
    }

    public boolean onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mState.isClear()) {
            if (event.getActionMasked() != 0) {
                return false;
            }
            clear();
        }
        if (this.mSendMotionEventsEnabled) {
            this.mEvents.add(android.view.MotionEvent.obtainNoHistory(rawEvent));
        }
        for (com.android.server.accessibility.gestures.GestureMatcher matcher : this.mGestures) {
            if (matcher.getState() != 3) {
                if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                    android.util.Slog.d(LOG_TAG, matcher.toString());
                }
                matcher.onMotionEvent(event, rawEvent, policyFlags);
                if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                    android.util.Slog.d(LOG_TAG, matcher.toString());
                }
                if (matcher.getState() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clear() {
        for (com.android.server.accessibility.gestures.GestureMatcher matcher : this.mGestures) {
            matcher.clear();
        }
        if (this.mEvents != null) {
            while (this.mEvents.size() > 0) {
                this.mEvents.remove(0).recycle();
            }
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener
    public void onStateChanged(int gestureId, int state, android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (state == 1 && !this.mState.isGestureDetecting()) {
            if (gestureId == 17 || gestureId == 18) {
                if (this.mServiceHandlesDoubleTap) {
                    this.mListener.onGestureStarted();
                    return;
                }
                return;
            }
            this.mListener.onGestureStarted();
            return;
        }
        if (state == 2) {
            onGestureCompleted(gestureId, event, rawEvent, policyFlags);
            return;
        }
        if (state == 3 && this.mState.isGestureDetecting()) {
            for (com.android.server.accessibility.gestures.GestureMatcher matcher : this.mGestures) {
                if (matcher.getState() == 1) {
                    return;
                }
            }
            if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                android.util.Slog.d(LOG_TAG, "Cancelling.");
            }
            this.mListener.onGestureCancelled(event, rawEvent, policyFlags);
        }
    }

    private void onGestureCompleted(int gestureId, android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        switch (gestureId) {
            case 17:
                if (this.mServiceHandlesDoubleTap) {
                    android.accessibilityservice.AccessibilityGestureEvent gestureEvent = new android.accessibilityservice.AccessibilityGestureEvent(gestureId, event.getDisplayId(), this.mEvents);
                    this.mListener.onGestureCompleted(gestureEvent);
                } else {
                    this.mListener.onDoubleTap(event, rawEvent, policyFlags);
                }
                break;
            case 18:
                if (this.mServiceHandlesDoubleTap) {
                    android.accessibilityservice.AccessibilityGestureEvent gestureEvent2 = new android.accessibilityservice.AccessibilityGestureEvent(gestureId, event.getDisplayId(), this.mEvents);
                    this.mListener.onGestureCompleted(gestureEvent2);
                } else {
                    this.mListener.onDoubleTapAndHold(event, rawEvent, policyFlags);
                }
                break;
            default:
                android.accessibilityservice.AccessibilityGestureEvent gestureEvent3 = new android.accessibilityservice.AccessibilityGestureEvent(gestureId, event.getDisplayId(), this.mEvents);
                this.mListener.onGestureCompleted(gestureEvent3);
                break;
        }
        clear();
    }

    public boolean isMultiFingerGesturesEnabled() {
        return this.mMultiFingerGesturesEnabled;
    }

    public void setMultiFingerGesturesEnabled(boolean mode) {
        if (this.mMultiFingerGesturesEnabled != mode) {
            this.mMultiFingerGesturesEnabled = mode;
            if (mode) {
                this.mGestures.addAll(this.mMultiFingerGestures);
            } else {
                this.mGestures.removeAll(this.mMultiFingerGestures);
            }
        }
    }

    public boolean isTwoFingerPassthroughEnabled() {
        return this.mTwoFingerPassthroughEnabled;
    }

    public void setTwoFingerPassthroughEnabled(boolean mode) {
        if (this.mTwoFingerPassthroughEnabled != mode) {
            this.mTwoFingerPassthroughEnabled = mode;
            if (!mode) {
                this.mMultiFingerGestures.addAll(this.mTwoFingerSwipes);
                if (this.mMultiFingerGesturesEnabled) {
                    this.mGestures.addAll(this.mTwoFingerSwipes);
                    return;
                }
                return;
            }
            this.mMultiFingerGestures.removeAll(this.mTwoFingerSwipes);
            this.mGestures.removeAll(this.mTwoFingerSwipes);
        }
    }

    public void setServiceHandlesDoubleTap(boolean mode) {
        this.mServiceHandlesDoubleTap = mode;
    }

    public boolean isServiceHandlesDoubleTapEnabled() {
        return this.mServiceHandlesDoubleTap;
    }

    public void setSendMotionEventsEnabled(boolean mode) {
        this.mSendMotionEventsEnabled = mode;
        if (!mode) {
            while (this.mEvents.size() > 0) {
                this.mEvents.remove(0).recycle();
            }
        }
    }

    public boolean isSendMotionEventsEnabled() {
        return this.mSendMotionEventsEnabled;
    }

    public java.util.List<android.view.MotionEvent> getMotionEvents() {
        return this.mEvents;
    }
}
