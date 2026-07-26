package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class AutoclickController extends com.android.server.accessibility.BaseEventStreamTransformation {
    private static final java.lang.String LOG_TAG = com.android.server.accessibility.AutoclickController.class.getSimpleName();
    private com.android.server.accessibility.AutoclickController.ClickDelayObserver mClickDelayObserver;
    private com.android.server.accessibility.AutoclickController.ClickScheduler mClickScheduler;
    private final android.content.Context mContext;
    private final com.android.server.accessibility.AccessibilityTraceManager mTrace;
    private final int mUserId;

    public AutoclickController(android.content.Context context, int userId, com.android.server.accessibility.AccessibilityTraceManager trace) {
        this.mTrace = trace;
        this.mContext = context;
        this.mUserId = userId;
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mTrace.isA11yTracingEnabledForTypes(4096L)) {
            this.mTrace.logTrace(LOG_TAG + ".onMotionEvent", 4096L, "event=" + event + ";rawEvent=" + rawEvent + ";policyFlags=" + policyFlags);
        }
        if (event.isFromSource(8194)) {
            if (this.mClickScheduler == null) {
                android.os.Handler handler = new android.os.Handler(this.mContext.getMainLooper());
                this.mClickScheduler = new com.android.server.accessibility.AutoclickController.ClickScheduler(handler, 600);
                this.mClickDelayObserver = new com.android.server.accessibility.AutoclickController.ClickDelayObserver(this.mUserId, handler);
                this.mClickDelayObserver.start(this.mContext.getContentResolver(), this.mClickScheduler);
            }
            handleMouseMotion(event, policyFlags);
        } else if (this.mClickScheduler != null) {
            this.mClickScheduler.cancel();
        }
        super.onMotionEvent(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onKeyEvent(android.view.KeyEvent event, int policyFlags) {
        if (this.mTrace.isA11yTracingEnabledForTypes(4096L)) {
            this.mTrace.logTrace(LOG_TAG + ".onKeyEvent", 4096L, "event=" + event + ";policyFlags=" + policyFlags);
        }
        if (this.mClickScheduler != null) {
            if (android.view.KeyEvent.isModifierKey(event.getKeyCode())) {
                this.mClickScheduler.updateMetaState(event.getMetaState());
            } else {
                this.mClickScheduler.cancel();
            }
        }
        super.onKeyEvent(event, policyFlags);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void clearEvents(int inputSource) {
        if (inputSource == 8194 && this.mClickScheduler != null) {
            this.mClickScheduler.cancel();
        }
        super.clearEvents(inputSource);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onDestroy() {
        if (this.mClickDelayObserver != null) {
            this.mClickDelayObserver.stop();
            this.mClickDelayObserver = null;
        }
        if (this.mClickScheduler != null) {
            this.mClickScheduler.cancel();
            this.mClickScheduler = null;
        }
    }

    private void handleMouseMotion(android.view.MotionEvent event, int policyFlags) {
        switch (event.getActionMasked()) {
            case 7:
                if (event.getPointerCount() == 1) {
                    this.mClickScheduler.update(event, policyFlags);
                } else {
                    this.mClickScheduler.cancel();
                }
                break;
            case 8:
            default:
                this.mClickScheduler.cancel();
                break;
            case 9:
            case 10:
                break;
        }
    }

    private static final class ClickDelayObserver extends android.database.ContentObserver {
        private final android.net.Uri mAutoclickDelaySettingUri;
        private com.android.server.accessibility.AutoclickController.ClickScheduler mClickScheduler;
        private android.content.ContentResolver mContentResolver;
        private final int mUserId;

        public ClickDelayObserver(int userId, android.os.Handler handler) {
            super(handler);
            this.mAutoclickDelaySettingUri = android.provider.Settings.Secure.getUriFor("accessibility_autoclick_delay");
            this.mUserId = userId;
        }

        public void start(android.content.ContentResolver contentResolver, com.android.server.accessibility.AutoclickController.ClickScheduler clickScheduler) {
            if (this.mContentResolver != null || this.mClickScheduler != null) {
                throw new java.lang.IllegalStateException("Observer already started.");
            }
            if (contentResolver == null) {
                throw new java.lang.NullPointerException("contentResolver not set.");
            }
            if (clickScheduler == null) {
                throw new java.lang.NullPointerException("clickScheduler not set.");
            }
            this.mContentResolver = contentResolver;
            this.mClickScheduler = clickScheduler;
            this.mContentResolver.registerContentObserver(this.mAutoclickDelaySettingUri, false, this, this.mUserId);
            onChange(true, this.mAutoclickDelaySettingUri);
        }

        public void stop() {
            if (this.mContentResolver == null || this.mClickScheduler == null) {
                throw new java.lang.IllegalStateException("ClickDelayObserver not started.");
            }
            this.mContentResolver.unregisterContentObserver(this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (this.mAutoclickDelaySettingUri.equals(uri)) {
                int delay = android.provider.Settings.Secure.getIntForUser(this.mContentResolver, "accessibility_autoclick_delay", 600, this.mUserId);
                this.mClickScheduler.updateDelay(delay);
            }
        }
    }

    private final class ClickScheduler implements java.lang.Runnable {
        private static final double MOVEMENT_SLOPE = 20.0d;
        private boolean mActive;
        private android.view.MotionEvent.PointerCoords mAnchorCoords;
        private int mDelay;
        private int mEventPolicyFlags;
        private android.os.Handler mHandler;
        private android.view.MotionEvent mLastMotionEvent = null;
        private int mMetaState;
        private long mScheduledClickTime;
        private android.view.MotionEvent.PointerCoords[] mTempPointerCoords;
        private android.view.MotionEvent.PointerProperties[] mTempPointerProperties;

        public ClickScheduler(android.os.Handler handler, int delay) {
            this.mHandler = handler;
            resetInternalState();
            this.mDelay = delay;
            this.mAnchorCoords = new android.view.MotionEvent.PointerCoords();
        }

        @Override // java.lang.Runnable
        public void run() {
            long now = android.os.SystemClock.uptimeMillis();
            if (now < this.mScheduledClickTime) {
                this.mHandler.postDelayed(this, this.mScheduledClickTime - now);
            } else {
                sendClick();
                resetInternalState();
            }
        }

        public void update(android.view.MotionEvent event, int policyFlags) {
            this.mMetaState = event.getMetaState();
            boolean moved = detectMovement(event);
            cacheLastEvent(event, policyFlags, this.mLastMotionEvent == null || moved);
            if (moved) {
                rescheduleClick(this.mDelay);
            }
        }

        public void cancel() {
            if (!this.mActive) {
                return;
            }
            resetInternalState();
            this.mHandler.removeCallbacks(this);
        }

        public void updateMetaState(int state) {
            this.mMetaState = state;
        }

        public void updateDelay(int delay) {
            this.mDelay = delay;
        }

        private void rescheduleClick(int delay) {
            long clickTime = android.os.SystemClock.uptimeMillis() + ((long) delay);
            if (this.mActive && clickTime > this.mScheduledClickTime) {
                this.mScheduledClickTime = clickTime;
                return;
            }
            if (this.mActive) {
                this.mHandler.removeCallbacks(this);
            }
            this.mActive = true;
            this.mScheduledClickTime = clickTime;
            this.mHandler.postDelayed(this, delay);
        }

        private void cacheLastEvent(android.view.MotionEvent event, int policyFlags, boolean useAsAnchor) {
            if (this.mLastMotionEvent != null) {
                this.mLastMotionEvent.recycle();
            }
            this.mLastMotionEvent = android.view.MotionEvent.obtain(event);
            this.mEventPolicyFlags = policyFlags;
            if (useAsAnchor) {
                int pointerIndex = this.mLastMotionEvent.getActionIndex();
                this.mLastMotionEvent.getPointerCoords(pointerIndex, this.mAnchorCoords);
            }
        }

        private void resetInternalState() {
            this.mActive = false;
            if (this.mLastMotionEvent != null) {
                this.mLastMotionEvent.recycle();
                this.mLastMotionEvent = null;
            }
            this.mScheduledClickTime = -1L;
        }

        private boolean detectMovement(android.view.MotionEvent event) {
            if (this.mLastMotionEvent == null) {
                return false;
            }
            int pointerIndex = event.getActionIndex();
            float deltaX = this.mAnchorCoords.x - event.getX(pointerIndex);
            float deltaY = this.mAnchorCoords.y - event.getY(pointerIndex);
            double delta = java.lang.Math.hypot(deltaX, deltaY);
            return delta > 20.0d;
        }

        private void sendClick() {
            if (this.mLastMotionEvent == null || com.android.server.accessibility.AutoclickController.this.getNext() == null) {
                return;
            }
            int pointerIndex = this.mLastMotionEvent.getActionIndex();
            if (this.mTempPointerProperties == null) {
                this.mTempPointerProperties = new android.view.MotionEvent.PointerProperties[1];
                this.mTempPointerProperties[0] = new android.view.MotionEvent.PointerProperties();
            }
            this.mLastMotionEvent.getPointerProperties(pointerIndex, this.mTempPointerProperties[0]);
            if (this.mTempPointerCoords == null) {
                this.mTempPointerCoords = new android.view.MotionEvent.PointerCoords[1];
                this.mTempPointerCoords[0] = new android.view.MotionEvent.PointerCoords();
            }
            this.mLastMotionEvent.getPointerCoords(pointerIndex, this.mTempPointerCoords[0]);
            long now = android.os.SystemClock.uptimeMillis();
            android.view.MotionEvent downEvent = android.view.MotionEvent.obtain(now, now, 0, 1, this.mTempPointerProperties, this.mTempPointerCoords, this.mMetaState, 1, 1.0f, 1.0f, this.mLastMotionEvent.getDeviceId(), 0, this.mLastMotionEvent.getSource(), this.mLastMotionEvent.getFlags());
            android.view.MotionEvent pressEvent = android.view.MotionEvent.obtain(downEvent);
            pressEvent.setAction(11);
            pressEvent.setActionButton(1);
            android.view.MotionEvent releaseEvent = android.view.MotionEvent.obtain(downEvent);
            releaseEvent.setAction(12);
            releaseEvent.setActionButton(1);
            releaseEvent.setButtonState(0);
            android.view.MotionEvent upEvent = android.view.MotionEvent.obtain(downEvent);
            upEvent.setAction(1);
            upEvent.setButtonState(0);
            com.android.server.accessibility.AutoclickController.super.onMotionEvent(downEvent, downEvent, this.mEventPolicyFlags);
            downEvent.recycle();
            com.android.server.accessibility.AutoclickController.super.onMotionEvent(pressEvent, pressEvent, this.mEventPolicyFlags);
            pressEvent.recycle();
            com.android.server.accessibility.AutoclickController.super.onMotionEvent(releaseEvent, releaseEvent, this.mEventPolicyFlags);
            releaseEvent.recycle();
            com.android.server.accessibility.AutoclickController.super.onMotionEvent(upEvent, upEvent, this.mEventPolicyFlags);
            upEvent.recycle();
        }

        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append("ClickScheduler: { active=").append(this.mActive);
            builder.append(", delay=").append(this.mDelay);
            builder.append(", scheduledClickTime=").append(this.mScheduledClickTime);
            builder.append(", anchor={x:").append(this.mAnchorCoords.x);
            builder.append(", y:").append(this.mAnchorCoords.y).append("}");
            builder.append(", metastate=").append(this.mMetaState);
            builder.append(", policyFlags=").append(this.mEventPolicyFlags);
            builder.append(", lastMotionEvent=").append(this.mLastMotionEvent);
            builder.append(" }");
            return builder.toString();
        }
    }
}
