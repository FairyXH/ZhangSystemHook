package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class HandwritingModeController {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long AFTER_STYLUS_UP_ALLOW_PERIOD_MS = 200;
    private static final int EVENT_BUFFER_SIZE = 100;
    private static final long HANDWRITING_DELEGATION_IDLE_TIMEOUT_MS = 3000;
    private static final int LONG_EVENT_BUFFER_SIZE = 2000;
    private final android.content.Context mContext;
    private java.lang.String mDelegatePackageName;
    private boolean mDelegationConnectionlessFlow;
    private android.os.Handler mDelegationIdleTimeoutHandler;
    private java.lang.Runnable mDelegationIdleTimeoutRunnable;
    private boolean mDelegatorFromDefaultHomePackage;
    private java.lang.String mDelegatorPackageName;
    private final java.lang.Runnable mDiscardDelegationTextRunnable;
    private java.util.ArrayList<android.view.MotionEvent> mHandwritingBuffer;
    private android.view.InputEventReceiver mHandwritingEventReceiver;
    private com.android.server.inputmethod.HandwritingEventReceiverSurface mHandwritingSurface;
    private java.lang.Runnable mInkWindowInitRunnable;
    private final android.os.Looper mLooper;
    private boolean mRecordingGesture;
    private boolean mRecordingGestureAfterStylusUp;
    public static final java.lang.String TAG = com.android.server.inputmethod.HandwritingModeController.class.getSimpleName();
    static boolean DEBUG = false;
    private int mCurrentDisplayId = -1;
    private final com.android.server.input.InputManagerInternal mInputManagerInternal = (com.android.server.input.InputManagerInternal) com.android.server.LocalServices.getService(com.android.server.input.InputManagerInternal.class);
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
    private int mCurrentRequestId = 0;

    HandwritingModeController(android.content.Context context, android.os.Looper uiThreadLooper, java.lang.Runnable inkWindowInitRunnable, java.lang.Runnable discardDelegationTextRunnable) {
        this.mContext = context;
        this.mLooper = uiThreadLooper;
        this.mInkWindowInitRunnable = inkWindowInitRunnable;
        this.mDiscardDelegationTextRunnable = discardDelegationTextRunnable;
    }

    void initializeHandwritingSpy(int displayId) {
        reset(displayId == this.mCurrentDisplayId);
        this.mCurrentDisplayId = displayId;
        if (this.mHandwritingBuffer == null) {
            this.mHandwritingBuffer = new java.util.ArrayList<>(getHandwritingBufferSize());
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Initializing handwriting spy monitor for display: " + displayId);
        }
        java.lang.String name = "stylus-handwriting-event-receiver-" + displayId;
        android.view.InputChannel channel = this.mInputManagerInternal.createInputChannel(name);
        java.util.Objects.requireNonNull(channel, "Failed to create input channel");
        android.view.SurfaceControl surface = this.mHandwritingSurface != null ? this.mHandwritingSurface.getSurface() : this.mWindowManagerInternal.getHandwritingSurfaceForDisplay(displayId);
        if (surface == null) {
            android.util.Slog.e(TAG, "Failed to create input surface");
            return;
        }
        this.mHandwritingSurface = new com.android.server.inputmethod.HandwritingEventReceiverSurface(name, displayId, surface, channel);
        this.mHandwritingEventReceiver = new android.view.BatchedInputEventReceiver.SimpleBatchedInputEventReceiver(channel.dup(), this.mLooper, android.view.Choreographer.getInstance(), new android.view.BatchedInputEventReceiver.SimpleBatchedInputEventReceiver.InputEventListener() { // from class: com.android.server.inputmethod.HandwritingModeController$$ExternalSyntheticLambda2
            public final boolean onInputEvent(android.view.InputEvent inputEvent) {
                return this.f$0.onInputEvent(inputEvent);
            }
        });
        this.mCurrentRequestId++;
    }

    java.util.OptionalInt getCurrentRequestId() {
        if (this.mHandwritingSurface == null) {
            android.util.Slog.e(TAG, "Cannot get requestId: Handwriting was not initialized.");
            return java.util.OptionalInt.empty();
        }
        return java.util.OptionalInt.of(this.mCurrentRequestId);
    }

    void setNotTouchable(boolean notTouchable) {
        if (!getCurrentRequestId().isPresent()) {
            return;
        }
        this.mHandwritingSurface.setNotTouchable(notTouchable);
    }

    boolean isStylusGestureOngoing() {
        if (this.mRecordingGestureAfterStylusUp && !this.mHandwritingBuffer.isEmpty()) {
            android.view.MotionEvent lastEvent = this.mHandwritingBuffer.get(this.mHandwritingBuffer.size() - 1);
            if (lastEvent.getActionMasked() == 1) {
                return android.os.SystemClock.uptimeMillis() - lastEvent.getEventTime() < AFTER_STYLUS_UP_ALLOW_PERIOD_MS;
            }
        }
        return this.mRecordingGesture;
    }

    boolean hasOngoingStylusHandwritingSession() {
        return this.mHandwritingSurface != null && this.mHandwritingSurface.isIntercepting();
    }

    void prepareStylusHandwritingDelegation(int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, boolean connectionless) {
        android.content.ComponentName defaultHomeActivity;
        this.mDelegatePackageName = delegatePackageName;
        this.mDelegatorPackageName = delegatorPackageName;
        this.mDelegatorFromDefaultHomePackage = false;
        if (!delegatorPackageName.equals(delegatePackageName) && (defaultHomeActivity = this.mPackageManagerInternal.getDefaultHomeActivity(userId)) != null) {
            this.mDelegatorFromDefaultHomePackage = delegatorPackageName.equals(defaultHomeActivity.getPackageName());
        }
        this.mDelegationConnectionlessFlow = connectionless;
        if (!connectionless) {
            if (this.mHandwritingBuffer == null) {
                this.mHandwritingBuffer = new java.util.ArrayList<>(getHandwritingBufferSize());
            } else {
                this.mHandwritingBuffer.ensureCapacity(getHandwritingBufferSize());
            }
        }
        scheduleHandwritingDelegationTimeout();
        com.android.server.inputmethod.InputMethodManagerService.getStaticExtImpl().logDebugIme(TAG, "prepareStylusHandwritingDelegation: mDelegatePackageName = " + this.mDelegatePackageName + ", mDelegatorPackageName = " + this.mDelegatorPackageName + ", mDelegatorFromDefaultHomePackage = " + this.mDelegatorFromDefaultHomePackage);
    }

    java.lang.String getDelegatePackageName() {
        return this.mDelegatePackageName;
    }

    java.lang.String getDelegatorPackageName() {
        return this.mDelegatorPackageName;
    }

    boolean isDelegatorFromDefaultHomePackage() {
        return this.mDelegatorFromDefaultHomePackage;
    }

    boolean isDelegationUsingConnectionlessFlow() {
        return this.mDelegationConnectionlessFlow;
    }

    private void scheduleHandwritingDelegationTimeout() {
        if (this.mDelegationIdleTimeoutHandler == null) {
            this.mDelegationIdleTimeoutHandler = new android.os.Handler(this.mLooper);
        } else {
            this.mDelegationIdleTimeoutHandler.removeCallbacks(this.mDelegationIdleTimeoutRunnable);
        }
        this.mDelegationIdleTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.inputmethod.HandwritingModeController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleHandwritingDelegationTimeout$0();
            }
        };
        this.mDelegationIdleTimeoutHandler.postDelayed(this.mDelegationIdleTimeoutRunnable, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleHandwritingDelegationTimeout$0() {
        android.util.Slog.d(TAG, "Stylus handwriting delegation idle timed-out.");
        clearPendingHandwritingDelegation();
        if (this.mHandwritingBuffer != null) {
            this.mHandwritingBuffer.forEach(new com.android.server.inputmethod.HandwritingModeController$$ExternalSyntheticLambda1());
            this.mHandwritingBuffer.clear();
            this.mHandwritingBuffer.trimToSize();
            this.mHandwritingBuffer.ensureCapacity(getHandwritingBufferSize());
        }
    }

    private int getHandwritingBufferSize() {
        if (this.mDelegatePackageName != null && this.mDelegatorPackageName != null) {
            return 2000;
        }
        return 100;
    }

    void clearPendingHandwritingDelegation() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "clearPendingHandwritingDelegation");
        }
        if (this.mDelegationIdleTimeoutHandler != null) {
            this.mDelegationIdleTimeoutHandler.removeCallbacks(this.mDelegationIdleTimeoutRunnable);
            this.mDelegationIdleTimeoutHandler = null;
        }
        this.mDelegationIdleTimeoutRunnable = null;
        this.mDelegatorPackageName = null;
        this.mDelegatePackageName = null;
        this.mDelegatorFromDefaultHomePackage = false;
        if (this.mDelegationConnectionlessFlow) {
            this.mDelegationConnectionlessFlow = false;
            this.mDiscardDelegationTextRunnable.run();
        }
    }

    com.android.server.inputmethod.HandwritingModeController.HandwritingSession startHandwritingSession(int requestId, int imePid, int imeUid, android.os.IBinder focusedWindowToken) {
        clearPendingHandwritingDelegation();
        if (this.mHandwritingSurface == null) {
            android.util.Slog.e(TAG, "Cannot start handwriting session: Handwriting was not initialized.");
            return null;
        }
        if (requestId != this.mCurrentRequestId) {
            android.util.Slog.e(TAG, "Cannot start handwriting session: Invalid request id: " + requestId);
            return null;
        }
        if (!isStylusGestureOngoing()) {
            android.util.Slog.e(TAG, "Cannot start handwriting session: No stylus gesture is being recorded.");
            return null;
        }
        java.util.Objects.requireNonNull(this.mHandwritingEventReceiver, "Handwriting session was already transferred to IME.");
        android.view.MotionEvent downEvent = this.mHandwritingBuffer.get(0);
        if (!this.mWindowManagerInternal.isPointInsideWindow(focusedWindowToken, this.mCurrentDisplayId, downEvent.getRawX(), downEvent.getRawY())) {
            android.util.Slog.e(TAG, "Cannot start handwriting session: Stylus gesture did not start inside the focused window.");
            return null;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Starting handwriting session in display: " + this.mCurrentDisplayId);
        }
        android.hardware.input.InputManagerGlobal.getInstance().pilferPointers(this.mHandwritingSurface.getInputChannel().getToken());
        this.mHandwritingEventReceiver.dispose();
        this.mHandwritingEventReceiver = null;
        this.mRecordingGesture = false;
        this.mRecordingGestureAfterStylusUp = false;
        if (this.mHandwritingSurface.isIntercepting()) {
            throw new java.lang.IllegalStateException("Handwriting surface should not be already intercepting.");
        }
        this.mHandwritingSurface.startIntercepting(imePid, imeUid);
        ((android.hardware.input.InputManager) java.util.Objects.requireNonNull((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class))).setPointerIcon(android.view.PointerIcon.getSystemIcon(this.mContext, 1), downEvent.getDisplayId(), downEvent.getDeviceId(), downEvent.getPointerId(0), this.mHandwritingSurface.getInputChannel().getToken());
        return new com.android.server.inputmethod.HandwritingModeController.HandwritingSession(this.mCurrentRequestId, this.mHandwritingSurface.getInputChannel(), this.mHandwritingBuffer);
    }

    void reset() {
        reset(false);
    }

    void setInkWindowInitializer(java.lang.Runnable inkWindowInitializer) {
        this.mInkWindowInitRunnable = inkWindowInitializer;
    }

    private void reset(boolean reinitializing) {
        if (this.mHandwritingEventReceiver != null) {
            this.mHandwritingEventReceiver.dispose();
            this.mHandwritingEventReceiver = null;
        }
        if (this.mHandwritingBuffer != null) {
            this.mHandwritingBuffer.forEach(new com.android.server.inputmethod.HandwritingModeController$$ExternalSyntheticLambda1());
            this.mHandwritingBuffer.clear();
            if (!reinitializing) {
                this.mHandwritingBuffer = null;
            }
        }
        if (this.mHandwritingSurface != null) {
            this.mHandwritingSurface.getInputChannel().dispose();
            if (!reinitializing) {
                this.mHandwritingSurface.remove();
                this.mHandwritingSurface = null;
            }
        }
        if (!this.mDelegationConnectionlessFlow) {
            clearPendingHandwritingDelegation();
        }
        this.mRecordingGesture = false;
        this.mRecordingGestureAfterStylusUp = false;
        com.android.server.inputmethod.InputMethodManagerService.getStaticExtImpl().logDebugIme(TAG, "reset");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onInputEvent(android.view.InputEvent ev) {
        if (this.mHandwritingEventReceiver == null) {
            throw new java.lang.IllegalStateException("Input Event should not be processed when IME has the spy channel.");
        }
        if (!(ev instanceof android.view.MotionEvent)) {
            android.util.Slog.wtf(TAG, "Received non-motion event in stylus monitor.");
            return false;
        }
        android.view.MotionEvent event = (android.view.MotionEvent) ev;
        if (!event.isStylusPointer()) {
            return false;
        }
        if (event.getDisplayId() != this.mCurrentDisplayId) {
            android.util.Slog.wtf(TAG, "Received stylus event associated with the incorrect display.");
            return false;
        }
        onStylusEvent(event);
        return true;
    }

    private void onStylusEvent(android.view.MotionEvent event) {
        int action = event.getActionMasked();
        if (DEBUG && (action == 0 || action == 1 || action == 3)) {
            com.android.server.inputmethod.InputMethodManagerService.getStaticExtImpl().logDebugIme(TAG, "onStylusEvent: event = " + event);
        }
        if (this.mInkWindowInitRunnable != null && (action == 9 || event.getAction() == 9)) {
            this.mInkWindowInitRunnable.run();
            this.mInkWindowInitRunnable = null;
            com.android.server.inputmethod.InputMethodManagerService.getStaticExtImpl().logDebugIme(TAG, "onStylusEvent: run mInkWindowInitRunnable");
            return;
        }
        if (event.isHoverEvent()) {
            return;
        }
        if ((android.text.TextUtils.isEmpty(this.mDelegatePackageName) || this.mDelegationConnectionlessFlow) && (action == 1 || action == 3)) {
            this.mRecordingGesture = false;
            com.android.server.inputmethod.InputMethodManagerService.getStaticExtImpl().logDebugIme(TAG, "onStylusEvent: mRecordingGesture set to false");
            if (com.android.text.flags.Flags.handwritingEndOfLineTap() && action == 1) {
                this.mRecordingGestureAfterStylusUp = true;
                com.android.server.inputmethod.InputMethodManagerService.getStaticExtImpl().logDebugIme(TAG, "onStylusEvent: mRecordingGestureAfterStylusUp set to true");
            } else {
                this.mHandwritingBuffer.clear();
                return;
            }
        }
        if (action == 0) {
            clearBufferIfRecordingAfterStylusUp();
            this.mRecordingGesture = true;
            com.android.server.inputmethod.InputMethodManagerService.getStaticExtImpl().logDebugIme(TAG, "onStylusEvent: mRecordingGesture set to true");
        }
        if (!this.mRecordingGesture && !this.mRecordingGestureAfterStylusUp) {
            return;
        }
        if (this.mHandwritingBuffer.size() >= getHandwritingBufferSize()) {
            if (DEBUG) {
                android.util.Slog.w(TAG, "Current gesture exceeds the buffer capacity. The rest of the gesture will not be recorded.");
            }
            this.mRecordingGesture = false;
            clearBufferIfRecordingAfterStylusUp();
            return;
        }
        this.mHandwritingBuffer.add(android.view.MotionEvent.obtain(event));
    }

    private void clearBufferIfRecordingAfterStylusUp() {
        if (this.mRecordingGestureAfterStylusUp) {
            this.mHandwritingBuffer.clear();
            this.mRecordingGestureAfterStylusUp = false;
            com.android.server.inputmethod.InputMethodManagerService.getStaticExtImpl().logDebugIme(TAG, "clearBufferIfRecordingAfterStylusUp");
        }
    }

    static final class HandwritingSession {
        private final android.view.InputChannel mHandwritingChannel;
        private final java.util.List<android.view.MotionEvent> mRecordedEvents;
        private final int mRequestId;

        private HandwritingSession(int requestId, android.view.InputChannel handwritingChannel, java.util.List<android.view.MotionEvent> recordedEvents) {
            this.mRequestId = requestId;
            this.mHandwritingChannel = handwritingChannel;
            this.mRecordedEvents = recordedEvents;
        }

        int getRequestId() {
            return this.mRequestId;
        }

        android.view.InputChannel getHandwritingChannel() {
            return this.mHandwritingChannel;
        }

        java.util.List<android.view.MotionEvent> getRecordedEvents() {
            return this.mRecordedEvents;
        }
    }
}
