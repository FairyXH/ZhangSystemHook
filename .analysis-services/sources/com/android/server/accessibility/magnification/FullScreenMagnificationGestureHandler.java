package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class FullScreenMagnificationGestureHandler extends com.android.server.accessibility.magnification.MagnificationGestureHandler {
    private static final float MIN_SCALE = 1.0f;
    static final int OVERSCROLL_LEFT_EDGE = 1;
    static final int OVERSCROLL_NONE = 0;
    static final int OVERSCROLL_RIGHT_EDGE = 2;
    static final int OVERSCROLL_VERTICAL_EDGE = 3;
    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State mCurrentState;
    final com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DelegatingState mDelegatingState;
    final com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DetectingState mDetectingState;
    final com.android.server.accessibility.magnification.FullScreenMagnificationController mFullScreenMagnificationController;
    private final com.android.server.accessibility.magnification.FullScreenMagnificationVibrationHelper mFullScreenMagnificationVibrationHelper;
    private final boolean mIsWatch;
    private final com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback mMagnificationInfoChangedCallback;
    private final com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MagnificationLogger mMagnificationLogger;
    private final int mMaximumVelocity;
    private final int mMinimumVelocity;
    final com.android.server.accessibility.magnification.OneFingerPanningSettingsProvider mOneFingerPanningSettingsProvider;
    private final float mOverscrollEdgeSlop;
    final com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.OverscrollHandler mOverscrollHandler;
    final com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.PanningScalingState mPanningScalingState;
    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State mPreviousState;
    private final com.android.server.accessibility.magnification.WindowMagnificationPromptController mPromptController;
    private final com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.ScreenStateReceiver mScreenStateReceiver;
    final com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.SinglePanningState mSinglePanningState;
    private android.view.MotionEvent.PointerCoords[] mTempPointerCoords;
    private android.view.MotionEvent.PointerProperties[] mTempPointerProperties;
    private android.view.VelocityTracker mVelocityTracker;
    final com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.ViewportDraggingState mViewportDraggingState;
    private static final boolean DEBUG_STATE_TRANSITIONS = DEBUG_ALL | false;
    private static final boolean DEBUG_DETECTING = DEBUG_ALL | false;
    private static final boolean DEBUG_PANNING_SCALING = DEBUG_ALL | false;
    private static final float MAX_SCALE = com.android.server.accessibility.magnification.MagnificationScaleProvider.MAX_SCALE;

    interface MagnificationLogger {
        void logMagnificationTripleTap(boolean z);

        void logMagnificationTwoFingerTripleTap(boolean z);
    }

    public @interface OverscrollState {
    }

    public FullScreenMagnificationGestureHandler(android.content.Context context, com.android.server.accessibility.magnification.FullScreenMagnificationController fullScreenMagnificationController, com.android.server.accessibility.AccessibilityTraceManager trace, com.android.server.accessibility.magnification.MagnificationGestureHandler.Callback callback, boolean detectSingleFingerTripleTap, boolean detectTwoFingerTripleTap, boolean detectShortcutTrigger, com.android.server.accessibility.magnification.WindowMagnificationPromptController promptController, int displayId, com.android.server.accessibility.magnification.FullScreenMagnificationVibrationHelper fullScreenMagnificationVibrationHelper) {
        this(context, fullScreenMagnificationController, trace, callback, detectSingleFingerTripleTap, detectTwoFingerTripleTap, detectShortcutTrigger, promptController, displayId, fullScreenMagnificationVibrationHelper, null, android.view.ViewConfiguration.get(context), new com.android.server.accessibility.magnification.OneFingerPanningSettingsProvider(context, com.android.server.accessibility.Flags.enableMagnificationOneFingerPanningGesture()));
    }

    FullScreenMagnificationGestureHandler(android.content.Context context, com.android.server.accessibility.magnification.FullScreenMagnificationController fullScreenMagnificationController, com.android.server.accessibility.AccessibilityTraceManager trace, com.android.server.accessibility.magnification.MagnificationGestureHandler.Callback callback, boolean detectSingleFingerTripleTap, boolean detectTwoFingerTripleTap, boolean detectShortcutTrigger, com.android.server.accessibility.magnification.WindowMagnificationPromptController promptController, int displayId, com.android.server.accessibility.magnification.FullScreenMagnificationVibrationHelper fullScreenMagnificationVibrationHelper, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MagnificationLogger magnificationLogger, android.view.ViewConfiguration viewConfiguration, com.android.server.accessibility.magnification.OneFingerPanningSettingsProvider oneFingerPanningSettingsProvider) {
        com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DetectingState detectingState;
        com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.ViewportDraggingState viewportDraggingState;
        super(displayId, detectSingleFingerTripleTap, detectTwoFingerTripleTap, detectShortcutTrigger, trace, callback);
        if (DEBUG_ALL) {
            android.util.Log.i(this.mLogTag, "FullScreenMagnificationGestureHandler(detectSingleFingerTripleTap = " + detectSingleFingerTripleTap + ", detectTwoFingerTripleTap = " + detectTwoFingerTripleTap + ", detectShortcutTrigger = " + detectShortcutTrigger + ")");
        }
        if (com.android.server.accessibility.Flags.fullscreenFlingGesture()) {
            this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        } else {
            this.mMinimumVelocity = 0;
            this.mMaximumVelocity = 0;
        }
        this.mFullScreenMagnificationController = fullScreenMagnificationController;
        this.mMagnificationInfoChangedCallback = new com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.1
            @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
            public void onRequestMagnificationSpec(int displayId2, int serviceId) {
            }

            @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
            public void onFullScreenMagnificationActivationState(int displayId2, boolean activated) {
                if (displayId2 == com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId && !activated) {
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectingState.setShortcutTriggered(false);
                }
            }

            @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
            public void onImeWindowVisibilityChanged(int displayId2, boolean shown) {
            }

            @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
            public void onFullScreenMagnificationChanged(int displayId2, android.graphics.Region region, android.accessibilityservice.MagnificationConfig config) {
            }
        };
        this.mFullScreenMagnificationController.addInfoChangedCallback(this.mMagnificationInfoChangedCallback);
        this.mPromptController = promptController;
        if (magnificationLogger == null) {
            this.mMagnificationLogger = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MagnificationLogger() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.2
                @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MagnificationLogger
                public void logMagnificationTripleTap(boolean enabled) {
                    com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logMagnificationTripleTap(enabled);
                }

                @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MagnificationLogger
                public void logMagnificationTwoFingerTripleTap(boolean enabled) {
                    com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logMagnificationTwoFingerTripleTap(enabled);
                }
            };
        } else {
            this.mMagnificationLogger = magnificationLogger;
        }
        this.mDelegatingState = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DelegatingState();
        if (com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture()) {
            detectingState = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DetectingStateWithMultiFinger(context);
        } else {
            detectingState = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DetectingState(context);
        }
        this.mDetectingState = detectingState;
        if (com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture()) {
            viewportDraggingState = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.ViewportDraggingStateWithMultiFinger();
        } else {
            viewportDraggingState = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.ViewportDraggingState();
        }
        this.mViewportDraggingState = viewportDraggingState;
        this.mPanningScalingState = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.PanningScalingState(context);
        this.mSinglePanningState = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.SinglePanningState(context);
        this.mFullScreenMagnificationVibrationHelper = fullScreenMagnificationVibrationHelper;
        this.mOneFingerPanningSettingsProvider = oneFingerPanningSettingsProvider;
        boolean overscrollHandlerSupported = context.getResources().getBoolean(android.R.bool.config_enableSecondaryLocationTimeZoneProvider);
        this.mOverscrollHandler = overscrollHandlerSupported ? new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.OverscrollHandler() : null;
        this.mOverscrollEdgeSlop = context.getResources().getDimensionPixelSize(android.R.dimen.accessibility_autoclick_type_panel_button_size);
        this.mIsWatch = context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
        if (this.mDetectShortcutTrigger) {
            this.mScreenStateReceiver = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.ScreenStateReceiver(context, this);
            this.mScreenStateReceiver.register();
        } else {
            this.mScreenStateReceiver = null;
        }
        transitionTo(this.mDetectingState);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler
    void onMotionEventInternal(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (event.getActionMasked() == 0) {
            cancelFling();
        }
        handleEventWith(this.mCurrentState, event, rawEvent, policyFlags);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEventWith(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State stateHandler, android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mPanningScalingState.mScrollGestureDetector.onTouchEvent(event);
        this.mPanningScalingState.mScaleGestureDetector.onTouchEvent(event);
        this.mSinglePanningState.mScrollGestureDetector.onTouchEvent(event);
        try {
            stateHandler.onMotionEvent(event, rawEvent, policyFlags);
        } catch (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.GestureException e) {
            android.util.Slog.e(this.mLogTag, "Error processing motion event", e);
            clearAndTransitionToStateDetecting();
        }
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void clearEvents(int inputSource) {
        if (inputSource == 4098) {
            clearAndTransitionToStateDetecting();
        }
        super.clearEvents(inputSource);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onDestroy() {
        if (DEBUG_STATE_TRANSITIONS) {
            android.util.Slog.i(this.mLogTag, "onDestroy(); delayed = " + com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo.toString(this.mDetectingState.mDelayedEventQueue));
        }
        this.mOneFingerPanningSettingsProvider.unregister();
        if (this.mScreenStateReceiver != null) {
            this.mScreenStateReceiver.unregister();
        }
        this.mPromptController.onDestroy();
        this.mFullScreenMagnificationController.resetIfNeeded(this.mDisplayId, 0);
        this.mFullScreenMagnificationController.removeInfoChangedCallback(this.mMagnificationInfoChangedCallback);
        clearAndTransitionToStateDetecting();
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler
    public void handleShortcutTriggered() {
        boolean isActivated = this.mFullScreenMagnificationController.isActivated(this.mDisplayId);
        if (isActivated) {
            zoomOff();
            clearAndTransitionToStateDetecting();
        } else {
            this.mDetectingState.toggleShortcutTriggered();
        }
        if (this.mDetectingState.isShortcutTriggered()) {
            this.mPromptController.showNotificationIfNeeded();
            zoomToScale(1.0f, Float.NaN, Float.NaN);
        }
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler
    public int getMode() {
        return 1;
    }

    void clearAndTransitionToStateDetecting() {
        this.mCurrentState = this.mDetectingState;
        this.mDetectingState.clear();
        this.mViewportDraggingState.clear();
        this.mPanningScalingState.clear();
    }

    private android.view.MotionEvent.PointerCoords[] getTempPointerCoordsWithMinSize(int size) {
        int oldSize = this.mTempPointerCoords != null ? this.mTempPointerCoords.length : 0;
        if (oldSize < size) {
            android.view.MotionEvent.PointerCoords[] oldTempPointerCoords = this.mTempPointerCoords;
            this.mTempPointerCoords = new android.view.MotionEvent.PointerCoords[size];
            if (oldTempPointerCoords != null) {
                java.lang.System.arraycopy(oldTempPointerCoords, 0, this.mTempPointerCoords, 0, oldSize);
            }
        }
        for (int i = oldSize; i < size; i++) {
            this.mTempPointerCoords[i] = new android.view.MotionEvent.PointerCoords();
        }
        return this.mTempPointerCoords;
    }

    private android.view.MotionEvent.PointerProperties[] getTempPointerPropertiesWithMinSize(int size) {
        int oldSize = this.mTempPointerProperties != null ? this.mTempPointerProperties.length : 0;
        if (oldSize < size) {
            android.view.MotionEvent.PointerProperties[] oldTempPointerProperties = this.mTempPointerProperties;
            this.mTempPointerProperties = new android.view.MotionEvent.PointerProperties[size];
            if (oldTempPointerProperties != null) {
                java.lang.System.arraycopy(oldTempPointerProperties, 0, this.mTempPointerProperties, 0, oldSize);
            }
        }
        for (int i = oldSize; i < size; i++) {
            this.mTempPointerProperties[i] = new android.view.MotionEvent.PointerProperties();
        }
        return this.mTempPointerProperties;
    }

    void transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State state) {
        if (DEBUG_STATE_TRANSITIONS) {
            android.util.Slog.i(this.mLogTag, (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State.nameOf(this.mCurrentState) + " -> " + com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State.nameOf(state) + " at " + java.util.Arrays.asList((java.lang.StackTraceElement[]) java.util.Arrays.copyOfRange(new java.lang.RuntimeException().getStackTrace(), 1, 5))).replace(getClass().getName(), ""));
        }
        this.mPreviousState = this.mCurrentState;
        if (state == this.mPanningScalingState) {
            this.mPanningScalingState.prepareForState();
        }
        this.mCurrentState = state;
    }

    interface State {
        void onMotionEvent(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i) throws com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.GestureException;

        default void clear() {
        }

        default java.lang.String name() {
            return getClass().getSimpleName();
        }

        static java.lang.String nameOf(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State s) {
            return s != null ? s.name() : "null";
        }
    }

    final class PanningScalingState extends android.view.GestureDetector.SimpleOnGestureListener implements android.view.ScaleGestureDetector.OnScaleGestureListener, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State {
        static final float CHECK_DETECTING_PASS_PERSISTED_SCALE_THRESHOLD = 0.2f;
        static final float PASSING_PERSISTED_SCALE_THRESHOLD = 0.01f;
        private final android.content.Context mContext;
        boolean mDetectingPassPersistedScale;
        float mInitialScaleFactor = -1.0f;
        private final android.view.ScaleGestureDetector mScaleGestureDetector;
        boolean mScaling;
        final float mScalingThreshold;
        private final android.view.GestureDetector mScrollGestureDetector;

        PanningScalingState(android.content.Context context) {
            android.util.TypedValue scaleValue = new android.util.TypedValue();
            context.getResources().getValue(android.R.dimen.config_minPercentageMultiWindowSupportHeight, scaleValue, false);
            this.mContext = context;
            this.mScalingThreshold = scaleValue.getFloat();
            this.mScaleGestureDetector = new android.view.ScaleGestureDetector(context, this, android.os.Handler.getMain());
            this.mScaleGestureDetector.setQuickScaleEnabled(false);
            this.mScrollGestureDetector = new android.view.GestureDetector(context, this, android.os.Handler.getMain());
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            int action = event.getActionMasked();
            if (action == 6 && event.getPointerCount() == 2 && com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mPreviousState == com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mViewportDraggingState) {
                if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler != null) {
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler.setScaleAndCenterToEdgeIfNeeded();
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler.clearEdgeState();
                }
                persistScaleAndTransitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mViewportDraggingState);
                return;
            }
            if (action == 1 || action == 3) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.onPanningFinished(event);
                if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler != null) {
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler.setScaleAndCenterToEdgeIfNeeded();
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler.clearEdgeState();
                }
                persistScaleAndTransitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectingState);
            }
        }

        void prepareForState() {
            checkShouldDetectPassPersistedScale();
        }

        private void checkShouldDetectPassPersistedScale() {
            if (this.mDetectingPassPersistedScale) {
                return;
            }
            float currentScale = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId);
            float persistedScale = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getPersistedScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId);
            this.mDetectingPassPersistedScale = java.lang.Math.abs(currentScale - persistedScale) / persistedScale >= CHECK_DETECTING_PASS_PERSISTED_SCALE_THRESHOLD;
        }

        public void persistScaleAndTransitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State state) {
            if (!com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mIsWatch) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.persistScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId);
            }
            clear();
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(state);
        }

        void setScaleAndClearIfNeeded(float scale, float pivotX, float pivotY) {
            if (this.mDetectingPassPersistedScale) {
                float persistedScale = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getPersistedScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId);
                if (java.lang.Math.abs(scale - persistedScale) / persistedScale < 0.01f) {
                    scale = persistedScale;
                    android.os.Vibrator vibrator = (android.os.Vibrator) this.mContext.getSystemService(android.os.Vibrator.class);
                    if (vibrator != null) {
                        vibrator.vibrate(android.os.VibrationEffect.createPredefined(2));
                    }
                    clear();
                }
            }
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DEBUG_PANNING_SCALING) {
                android.util.Slog.i(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mLogTag, "Scaled content to: " + scale + "x");
            }
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.setScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, scale, pivotX, pivotY, false, 0);
            checkShouldDetectPassPersistedScale();
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(android.view.MotionEvent first, android.view.MotionEvent second, float distanceX, float distanceY) {
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mCurrentState != com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mPanningScalingState) {
                return true;
            }
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DEBUG_PANNING_SCALING) {
                android.util.Slog.i(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mLogTag, "Panned content by scrollX: " + distanceX + " scrollY: " + distanceY);
            }
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.onPan(second);
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.offsetMagnifiedRegion(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, distanceX, distanceY, 0);
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler != null) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler.onScrollStateChanged(first, second);
            }
            return true;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(android.view.ScaleGestureDetector detector) {
            float scale;
            if (!this.mScaling) {
                if (this.mInitialScaleFactor < 0.0f) {
                    this.mInitialScaleFactor = detector.getScaleFactor();
                    return false;
                }
                float deltaScale = detector.getScaleFactor() - this.mInitialScaleFactor;
                this.mScaling = java.lang.Math.abs(deltaScale) > this.mScalingThreshold;
                return this.mScaling;
            }
            float initialScale = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId);
            float targetScale = detector.getScaleFactor() * initialScale;
            if (targetScale > com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MAX_SCALE && targetScale > initialScale) {
                scale = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MAX_SCALE;
            } else if (targetScale < 1.0f && targetScale < initialScale) {
                scale = 1.0f;
            } else {
                scale = targetScale;
            }
            setScaleAndClearIfNeeded(scale, detector.getFocusX(), detector.getFocusY());
            return true;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(android.view.ScaleGestureDetector detector) {
            return com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mCurrentState == com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mPanningScalingState;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(android.view.ScaleGestureDetector detector) {
            clear();
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void clear() {
            this.mInitialScaleFactor = -1.0f;
            this.mScaling = false;
            this.mDetectingPassPersistedScale = false;
        }

        public java.lang.String toString() {
            return "PanningScalingState{mInitialScaleFactor=" + this.mInitialScaleFactor + ", mScaling=" + this.mScaling + '}';
        }
    }

    final class ViewportDraggingStateWithMultiFinger extends com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.ViewportDraggingState {
        ViewportDraggingStateWithMultiFinger() {
            super();
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.ViewportDraggingState, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) throws com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.GestureException {
            int action = event.getActionMasked();
            switch (action) {
                case 0:
                    throw new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.GestureException("Unexpected event type: " + android.view.MotionEvent.actionToString(action));
                case 1:
                case 3:
                    if (this.mScaleToRecoverAfterDraggingEnd >= 1.0f) {
                        com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.zoomToScale(this.mScaleToRecoverAfterDraggingEnd, event.getX(), event.getY());
                    } else {
                        com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.zoomOff();
                    }
                    clear();
                    this.mScaleToRecoverAfterDraggingEnd = Float.NaN;
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectingState);
                    return;
                case 2:
                    if (event.getPointerCount() > 2) {
                        throw new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.GestureException("Should have one pointer down.");
                    }
                    float eventX = event.getX();
                    float eventY = event.getY();
                    if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.magnificationRegionContains(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, eventX, eventY)) {
                        com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.setCenter(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, eventX, eventY, this.mLastMoveOutsideMagnifiedRegion, 0);
                        this.mLastMoveOutsideMagnifiedRegion = false;
                        return;
                    } else {
                        this.mLastMoveOutsideMagnifiedRegion = true;
                        return;
                    }
                case 4:
                default:
                    return;
                case 5:
                    clearAndTransitToPanningScalingState();
                    return;
            }
        }
    }

    class ViewportDraggingState implements com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State {
        protected boolean mLastMoveOutsideMagnifiedRegion;
        protected float mScaleToRecoverAfterDraggingEnd = Float.NaN;

        ViewportDraggingState() {
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) throws com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.GestureException {
            int action = event.getActionMasked();
            switch (action) {
                case 0:
                case 6:
                    throw new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.GestureException("Unexpected event type: " + android.view.MotionEvent.actionToString(action));
                case 1:
                case 3:
                    if (this.mScaleToRecoverAfterDraggingEnd >= 1.0f) {
                        com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.zoomToScale(this.mScaleToRecoverAfterDraggingEnd, event.getX(), event.getY());
                    } else {
                        com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.zoomOff();
                    }
                    clear();
                    this.mScaleToRecoverAfterDraggingEnd = Float.NaN;
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectingState);
                    return;
                case 2:
                    if (event.getPointerCount() != 1) {
                        throw new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.GestureException("Should have one pointer down.");
                    }
                    float eventX = event.getX();
                    float eventY = event.getY();
                    if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.magnificationRegionContains(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, eventX, eventY)) {
                        com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.setCenter(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, eventX, eventY, this.mLastMoveOutsideMagnifiedRegion, 0);
                        this.mLastMoveOutsideMagnifiedRegion = false;
                        return;
                    } else {
                        this.mLastMoveOutsideMagnifiedRegion = true;
                        return;
                    }
                case 4:
                default:
                    return;
                case 5:
                    clearAndTransitToPanningScalingState();
                    return;
            }
        }

        private boolean isAlwaysOnMagnificationEnabled() {
            return com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.isAlwaysOnMagnificationEnabled();
        }

        public void prepareForZoomInTemporary(boolean shortcutTriggered) {
            boolean shouldRecoverAfterDraggingEnd;
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.isActivated(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId)) {
                if (shortcutTriggered) {
                    shouldRecoverAfterDraggingEnd = isAlwaysOnMagnificationEnabled();
                } else {
                    shouldRecoverAfterDraggingEnd = true;
                }
            } else {
                shouldRecoverAfterDraggingEnd = false;
            }
            this.mScaleToRecoverAfterDraggingEnd = shouldRecoverAfterDraggingEnd ? com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId) : Float.NaN;
        }

        protected void clearAndTransitToPanningScalingState() {
            float scaleToRecovery = this.mScaleToRecoverAfterDraggingEnd;
            clear();
            this.mScaleToRecoverAfterDraggingEnd = scaleToRecovery;
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mPanningScalingState);
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void clear() {
            this.mLastMoveOutsideMagnifiedRegion = false;
            this.mScaleToRecoverAfterDraggingEnd = Float.NaN;
        }

        public java.lang.String toString() {
            return "ViewportDraggingState{mScaleToRecoverAfterDraggingEnd=" + this.mScaleToRecoverAfterDraggingEnd + ", mLastMoveOutsideMagnifiedRegion=" + this.mLastMoveOutsideMagnifiedRegion + '}';
        }
    }

    final class DelegatingState implements com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State {
        public long mLastDelegatedDownEventTime;

        DelegatingState() {
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            switch (event.getActionMasked()) {
                case 0:
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDelegatingState);
                    this.mLastDelegatedDownEventTime = event.getDownTime();
                    break;
                case 1:
                case 3:
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectingState);
                    break;
            }
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.getNext() != null) {
                event.setDownTime(this.mLastDelegatedDownEventTime);
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.dispatchTransformedEvent(event, rawEvent, policyFlags);
            }
        }
    }

    final class DetectingStateWithMultiFinger extends com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DetectingState {
        private static final int TWO_FINGER_GESTURE_MAX_TAPS = 2;
        private int mCompletedTapCount;
        private boolean mIsTwoFingerCountReached;

        DetectingStateWithMultiFinger(android.content.Context context) {
            super(context);
            this.mIsTwoFingerCountReached = false;
            this.mCompletedTapCount = 0;
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DetectingState, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            cacheDelayedMotionEvent(event, rawEvent, policyFlags);
            switch (event.getActionMasked()) {
                case 0:
                    this.mLastDetectingDownEventTime = event.getDownTime();
                    this.mHandler.removeMessages(2);
                    this.mFirstPointerDownLocation.set(event.getX(), event.getY());
                    if (!com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.magnificationRegionContains(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, event.getX(), event.getY())) {
                        transitionToDelegatingStateAndClear();
                    } else if (isMultiTapTriggered(2)) {
                        afterLongTapTimeoutTransitionToDraggingState(event);
                    } else if (isTapOutOfDistanceSlop()) {
                        transitionToDelegatingStateAndClear();
                    } else if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectSingleFingerTripleTap || com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectTwoFingerTripleTap || isActivated()) {
                        afterMultiTapTimeoutTransitionToDelegatingState();
                    } else {
                        transitionToDelegatingStateAndClear();
                    }
                    break;
                case 1:
                    this.mHandler.removeMessages(1);
                    this.mHandler.removeMessages(3);
                    if (!com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.magnificationRegionContains(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, event.getX(), event.getY())) {
                        transitionToDelegatingStateAndClear();
                    } else if (isMultiFingerMultiTapTriggered(2, event)) {
                        onTripleTap(event);
                    } else if (isMultiTapTriggered(3)) {
                        onTripleTap(event);
                    } else if (isFingerDown()) {
                        if ((timeBetween(this.mLastDown, this.mLastUp) >= this.mLongTapMinDelay || com.android.server.accessibility.gestures.GestureUtils.distance(this.mLastDown, this.mLastUp) >= this.mSwipeMinDistance) && this.mCompletedTapCount == 0) {
                            transitionToDelegatingStateAndClear();
                        }
                    }
                    break;
                case 2:
                    if (isFingerDown() && com.android.server.accessibility.gestures.GestureUtils.distance(this.mLastDown, event) > this.mSwipeMinDistance) {
                        if (isMultiTapTriggered(2) && event.getPointerCount() == 1) {
                            transitionToViewportDraggingStateAndClear(event);
                        } else if (isMultiFingerMultiTapTriggered(1, event) && event.getPointerCount() == 2) {
                            transitionToViewportDraggingStateAndClear(event);
                        } else if (isActivated() && event.getPointerCount() == 2) {
                            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler != null && com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.overscrollState(event, this.mFirstPointerDownLocation) == 3) {
                                transitionToDelegatingStateAndClear();
                            } else {
                                transitToPanningScalingStateAndClear();
                            }
                        } else if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOneFingerPanningSettingsProvider.isOneFingerPanningEnabled() && isActivated() && event.getPointerCount() == 1) {
                            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler != null && com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.overscrollState(event, this.mFirstPointerDownLocation) == 3) {
                                transitionToDelegatingStateAndClear();
                            } else if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.overscrollState(event, this.mFirstPointerDownLocation) != 0) {
                                transitionToDelegatingStateAndClear();
                            } else {
                                transitToSinglePanningStateAndClear();
                            }
                        } else if (!this.mIsTwoFingerCountReached) {
                            transitionToDelegatingStateAndClear();
                        }
                        break;
                    } else if (isActivated() && pointerDownValid(this.mSecondPointerDownLocation) && com.android.server.accessibility.gestures.GestureUtils.distanceClosestPointerToPoint(this.mSecondPointerDownLocation, event) > this.mSwipeMinDistance) {
                        storePointerDownLocation(this.mSecondPointerDownLocation, event);
                        this.mHandler.sendEmptyMessageDelayed(3, android.view.ViewConfiguration.getTapTimeout());
                        break;
                    }
                    break;
                case 5:
                    this.mIsTwoFingerCountReached = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectTwoFingerTripleTap && event.getPointerCount() == 2;
                    this.mHandler.removeMessages(2);
                    if (event.getPointerCount() == 2) {
                        if (isMultiFingerMultiTapTriggered(1, event)) {
                            afterLongTapTimeoutTransitionToDraggingState(event);
                        } else {
                            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectTwoFingerTripleTap) {
                                afterMultiTapTimeoutTransitionToDelegatingState();
                            }
                            if (isActivated()) {
                                storePointerDownLocation(this.mSecondPointerDownLocation, event);
                                this.mHandler.sendEmptyMessageDelayed(3, android.view.ViewConfiguration.getTapTimeout());
                            }
                        }
                    } else {
                        transitionToDelegatingStateAndClear();
                    }
                    break;
                case 6:
                    if (!this.mIsTwoFingerCountReached) {
                        transitionToDelegatingStateAndClear();
                    }
                    break;
            }
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DetectingState, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void clear() {
            this.mCompletedTapCount = 0;
            setShortcutTriggered(false);
            removePendingDelayedMessages();
            clearDelayedMotionEvents();
            this.mFirstPointerDownLocation.set(Float.NaN, Float.NaN);
            this.mSecondPointerDownLocation.set(Float.NaN, Float.NaN);
        }

        private boolean isMultiFingerMultiTapTriggered(int targetTapCount, android.view.MotionEvent event) {
            if (event.getActionMasked() == 1 && this.mIsTwoFingerCountReached) {
                this.mCompletedTapCount++;
                this.mIsTwoFingerCountReached = false;
            }
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectTwoFingerTripleTap && this.mCompletedTapCount > 1) {
                boolean enabled = !isActivated();
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mMagnificationLogger.logMagnificationTwoFingerTripleTap(enabled);
            }
            return com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectTwoFingerTripleTap && this.mCompletedTapCount == targetTapCount;
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DetectingState
        void transitionToDelegatingStateAndClear() {
            this.mCompletedTapCount = 0;
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDelegatingState);
            sendDelayedMotionEvents();
            removePendingDelayedMessages();
            this.mFirstPointerDownLocation.set(Float.NaN, Float.NaN);
            this.mSecondPointerDownLocation.set(Float.NaN, Float.NaN);
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DetectingState
        void transitionToViewportDraggingStateAndClear(android.view.MotionEvent down) {
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DEBUG_DETECTING) {
                android.util.Slog.i(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mLogTag, "onTripleTapAndHold()");
            }
            boolean shortcutTriggered = this.mShortcutTriggered;
            if (!shortcutTriggered) {
                boolean enabled = !isActivated();
                if (this.mCompletedTapCount == 1) {
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mMagnificationLogger.logMagnificationTwoFingerTripleTap(enabled);
                } else {
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mMagnificationLogger.logMagnificationTripleTap(enabled);
                }
            }
            clear();
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mViewportDraggingState.prepareForZoomInTemporary(shortcutTriggered);
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.zoomInTemporary(down.getX(), down.getY(), shortcutTriggered);
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mViewportDraggingState);
        }
    }

    class DetectingState implements com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State, android.os.Handler.Callback {
        protected static final int MESSAGE_ON_TRIPLE_TAP_AND_HOLD = 1;
        protected static final int MESSAGE_TRANSITION_TO_DELEGATING_STATE = 2;
        protected static final int MESSAGE_TRANSITION_TO_PANNINGSCALING_STATE = 3;
        protected com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo mDelayedEventQueue;
        protected long mLastDetectingDownEventTime;
        protected android.view.MotionEvent mLastDown;
        protected android.view.MotionEvent mLastUp;
        final int mMultiTapMaxDelay;
        final int mMultiTapMaxDistance;
        protected android.view.MotionEvent mPreLastDown;
        protected android.view.MotionEvent mPreLastUp;
        boolean mShortcutTriggered;
        final int mSwipeMinDistance;
        protected android.graphics.PointF mFirstPointerDownLocation = new android.graphics.PointF(Float.NaN, Float.NaN);
        protected android.graphics.PointF mSecondPointerDownLocation = new android.graphics.PointF(Float.NaN, Float.NaN);
        android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper(), this);
        final int mLongTapMinDelay = android.view.ViewConfiguration.getLongPressTimeout();

        DetectingState(android.content.Context context) {
            this.mMultiTapMaxDelay = android.view.ViewConfiguration.getDoubleTapTimeout() + context.getResources().getInteger(android.R.integer.config_reduceBrightColorsStrengthMin);
            this.mSwipeMinDistance = android.view.ViewConfiguration.get(context).getScaledTouchSlop();
            this.mMultiTapMaxDistance = android.view.ViewConfiguration.get(context).getScaledDoubleTapSlop();
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(android.os.Message message) {
            int type = message.what;
            switch (type) {
                case 1:
                    android.view.MotionEvent down = (android.view.MotionEvent) message.obj;
                    transitionToViewportDraggingStateAndClear(down);
                    down.recycle();
                    return true;
                case 2:
                    transitionToDelegatingStateAndClear();
                    return true;
                case 3:
                    transitToPanningScalingStateAndClear();
                    return true;
                default:
                    throw new java.lang.IllegalArgumentException("Unknown message type: " + type);
            }
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            cacheDelayedMotionEvent(event, rawEvent, policyFlags);
            switch (event.getActionMasked()) {
                case 0:
                    this.mLastDetectingDownEventTime = event.getDownTime();
                    this.mHandler.removeMessages(2);
                    this.mFirstPointerDownLocation.set(event.getX(), event.getY());
                    if (!com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.magnificationRegionContains(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, event.getX(), event.getY())) {
                        transitionToDelegatingStateAndClear();
                    } else if (isMultiTapTriggered(2)) {
                        afterLongTapTimeoutTransitionToDraggingState(event);
                    } else if (isTapOutOfDistanceSlop()) {
                        transitionToDelegatingStateAndClear();
                    } else if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectSingleFingerTripleTap || isActivated()) {
                        afterMultiTapTimeoutTransitionToDelegatingState();
                    } else {
                        transitionToDelegatingStateAndClear();
                    }
                    break;
                case 1:
                    this.mHandler.removeMessages(1);
                    if (!com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.magnificationRegionContains(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, event.getX(), event.getY())) {
                        transitionToDelegatingStateAndClear();
                    } else if (isMultiTapTriggered(3)) {
                        onTripleTap(event);
                    } else if (isFingerDown()) {
                        if (timeBetween(this.mLastDown, this.mLastUp) >= this.mLongTapMinDelay || com.android.server.accessibility.gestures.GestureUtils.distance(this.mLastDown, this.mLastUp) >= this.mSwipeMinDistance) {
                            transitionToDelegatingStateAndClear();
                        }
                    }
                    break;
                case 2:
                    if (isFingerDown() && com.android.server.accessibility.gestures.GestureUtils.distance(this.mLastDown, event) > this.mSwipeMinDistance) {
                        if (isMultiTapTriggered(2) && event.getPointerCount() == 1) {
                            transitionToViewportDraggingStateAndClear(event);
                        } else if (isActivated() && event.getPointerCount() == 2) {
                            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler != null && com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.overscrollState(event, this.mFirstPointerDownLocation) == 3) {
                                transitionToDelegatingStateAndClear();
                            } else {
                                transitToPanningScalingStateAndClear();
                            }
                        } else if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOneFingerPanningSettingsProvider.isOneFingerPanningEnabled() && isActivated() && event.getPointerCount() == 1) {
                            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler != null && com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.overscrollState(event, this.mFirstPointerDownLocation) == 3) {
                                transitionToDelegatingStateAndClear();
                            } else if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.overscrollState(event, this.mFirstPointerDownLocation) != 0) {
                                transitionToDelegatingStateAndClear();
                            } else {
                                transitToSinglePanningStateAndClear();
                            }
                        } else {
                            transitionToDelegatingStateAndClear();
                        }
                        break;
                    } else if (isActivated() && pointerDownValid(this.mSecondPointerDownLocation) && com.android.server.accessibility.gestures.GestureUtils.distanceClosestPointerToPoint(this.mSecondPointerDownLocation, event) > this.mSwipeMinDistance) {
                        transitToPanningScalingStateAndClear();
                        break;
                    }
                    break;
                case 5:
                    if (isActivated() && event.getPointerCount() == 2) {
                        storePointerDownLocation(this.mSecondPointerDownLocation, event);
                        this.mHandler.sendEmptyMessageDelayed(3, android.view.ViewConfiguration.getTapTimeout());
                    } else {
                        transitionToDelegatingStateAndClear();
                    }
                    break;
                case 6:
                    transitionToDelegatingStateAndClear();
                    break;
            }
        }

        protected void storePointerDownLocation(android.graphics.PointF pointerDownLocation, android.view.MotionEvent event) {
            int index = event.getActionIndex();
            pointerDownLocation.set(event.getX(index), event.getY(index));
        }

        protected boolean pointerDownValid(android.graphics.PointF pointerDownLocation) {
            return (java.lang.Float.isNaN(pointerDownLocation.x) && java.lang.Float.isNaN(pointerDownLocation.y)) ? false : true;
        }

        protected void transitToPanningScalingStateAndClear() {
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mPanningScalingState);
            clear();
        }

        protected void transitToSinglePanningStateAndClear() {
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mSinglePanningState);
            clear();
        }

        public boolean isMultiTapTriggered(int numTaps) {
            boolean z = false;
            if (this.mShortcutTriggered) {
                return tapCount() + 2 >= numTaps;
            }
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectSingleFingerTripleTap && tapCount() >= numTaps && isMultiTap(this.mPreLastDown, this.mLastDown) && isMultiTap(this.mPreLastUp, this.mLastUp)) {
                z = true;
            }
            boolean multitapTriggered = z;
            if (multitapTriggered && numTaps > 2) {
                boolean enabled = !isActivated();
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mMagnificationLogger.logMagnificationTripleTap(enabled);
            }
            return multitapTriggered;
        }

        private boolean isMultiTap(android.view.MotionEvent first, android.view.MotionEvent second) {
            return com.android.server.accessibility.gestures.GestureUtils.isMultiTap(first, second, this.mMultiTapMaxDelay, this.mMultiTapMaxDistance);
        }

        public boolean isFingerDown() {
            return this.mLastDown != null;
        }

        protected long timeBetween(android.view.MotionEvent a, android.view.MotionEvent b) {
            if (a == null && b == null) {
                return 0L;
            }
            return java.lang.Math.abs(timeOf(a) - timeOf(b));
        }

        private long timeOf(android.view.MotionEvent event) {
            if (event != null) {
                return event.getEventTime();
            }
            return Long.MIN_VALUE;
        }

        public int tapCount() {
            return com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo.countOf(this.mDelayedEventQueue, 1);
        }

        public void afterMultiTapTimeoutTransitionToDelegatingState() {
            this.mHandler.sendEmptyMessageDelayed(2, this.mMultiTapMaxDelay);
        }

        public void afterLongTapTimeoutTransitionToDraggingState(android.view.MotionEvent event) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, android.view.MotionEvent.obtain(event)), android.view.ViewConfiguration.getLongPressTimeout());
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void clear() {
            setShortcutTriggered(false);
            removePendingDelayedMessages();
            clearDelayedMotionEvents();
            this.mFirstPointerDownLocation.set(Float.NaN, Float.NaN);
            this.mSecondPointerDownLocation.set(Float.NaN, Float.NaN);
        }

        protected void removePendingDelayedMessages() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
        }

        protected void cacheDelayedMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            if (event.getActionMasked() == 0) {
                this.mPreLastDown = this.mLastDown;
                this.mLastDown = android.view.MotionEvent.obtain(event);
            } else if (event.getActionMasked() == 1) {
                this.mPreLastUp = this.mLastUp;
                this.mLastUp = android.view.MotionEvent.obtain(event);
            }
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo info = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo.obtain(event, rawEvent, policyFlags);
            if (this.mDelayedEventQueue == null) {
                this.mDelayedEventQueue = info;
                return;
            }
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo tail = this.mDelayedEventQueue;
            while (tail.mNext != null) {
                tail = tail.mNext;
            }
            tail.mNext = info;
        }

        protected void sendDelayedMotionEvents() {
            if (this.mDelayedEventQueue == null) {
                return;
            }
            long offset = java.lang.Math.min(android.os.SystemClock.uptimeMillis() - this.mLastDetectingDownEventTime, this.mMultiTapMaxDelay);
            do {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo info = this.mDelayedEventQueue;
                this.mDelayedEventQueue = info.mNext;
                info.event.setDownTime(info.event.getDownTime() + offset);
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.handleEventWith(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDelegatingState, info.event, info.rawEvent, info.policyFlags);
                info.recycle();
            } while (this.mDelayedEventQueue != null);
        }

        protected void clearDelayedMotionEvents() {
            while (this.mDelayedEventQueue != null) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo info = this.mDelayedEventQueue;
                this.mDelayedEventQueue = info.mNext;
                info.recycle();
            }
            this.mPreLastDown = null;
            this.mPreLastUp = null;
            this.mLastDown = null;
            this.mLastUp = null;
        }

        void transitionToDelegatingStateAndClear() {
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDelegatingState);
            sendDelayedMotionEvents();
            removePendingDelayedMessages();
            this.mSecondPointerDownLocation.set(Float.NaN, Float.NaN);
        }

        protected void onTripleTap(android.view.MotionEvent up) {
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DEBUG_DETECTING) {
                android.util.Slog.i(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mLogTag, "onTripleTap(); delayed: " + com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo.toString(this.mDelayedEventQueue));
            }
            if (!isActivated() || this.mShortcutTriggered) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mPromptController.showNotificationIfNeeded();
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.zoomOn(up.getX(), up.getY());
            } else {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.zoomOff();
            }
            clear();
        }

        protected boolean isActivated() {
            return com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.isActivated(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId);
        }

        void transitionToViewportDraggingStateAndClear(android.view.MotionEvent down) {
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DEBUG_DETECTING) {
                android.util.Slog.i(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mLogTag, "onTripleTapAndHold()");
            }
            boolean shortcutTriggered = this.mShortcutTriggered;
            if (!shortcutTriggered) {
                boolean enabled = !isActivated();
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mMagnificationLogger.logMagnificationTripleTap(enabled);
            }
            clear();
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mViewportDraggingState.prepareForZoomInTemporary(shortcutTriggered);
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.zoomInTemporary(down.getX(), down.getY(), shortcutTriggered);
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mViewportDraggingState);
        }

        public java.lang.String toString() {
            return "DetectingState{tapCount()=" + tapCount() + ", mShortcutTriggered=" + this.mShortcutTriggered + ", mDelayedEventQueue=" + com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo.toString(this.mDelayedEventQueue) + '}';
        }

        void toggleShortcutTriggered() {
            setShortcutTriggered(!this.mShortcutTriggered);
        }

        void setShortcutTriggered(boolean state) {
            if (this.mShortcutTriggered == state) {
                return;
            }
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DEBUG_DETECTING) {
                android.util.Slog.i(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mLogTag, "setShortcutTriggered(" + state + ")");
            }
            this.mShortcutTriggered = state;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isShortcutTriggered() {
            return this.mShortcutTriggered;
        }

        boolean isTapOutOfDistanceSlop() {
            if (!com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectSingleFingerTripleTap || this.mPreLastDown == null || this.mLastDown == null) {
                return false;
            }
            boolean outOfDistanceSlop = com.android.server.accessibility.gestures.GestureUtils.distance(this.mPreLastDown, this.mLastDown) > ((double) this.mMultiTapMaxDistance);
            if (tapCount() > 0) {
                return outOfDistanceSlop;
            }
            return outOfDistanceSlop && !com.android.server.accessibility.gestures.GestureUtils.isTimedOut(this.mPreLastDown, this.mLastDown, this.mMultiTapMaxDelay);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomInTemporary(float centerX, float centerY, boolean shortcutTriggered) {
        float currentScale = this.mFullScreenMagnificationController.getScale(this.mDisplayId);
        float persistedScale = android.util.MathUtils.constrain(this.mFullScreenMagnificationController.getPersistedScale(this.mDisplayId), 1.0f, MAX_SCALE);
        boolean isActivated = this.mFullScreenMagnificationController.isActivated(this.mDisplayId);
        boolean isZoomedOutFromService = this.mFullScreenMagnificationController.isZoomedOutFromService(this.mDisplayId);
        boolean zoomInWithPersistedScale = !isActivated || shortcutTriggered || isZoomedOutFromService;
        float scale = zoomInWithPersistedScale ? persistedScale : 1.0f + currentScale;
        zoomToScale(scale, centerX, centerY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomOn(float centerX, float centerY) {
        if (DEBUG_DETECTING) {
            android.util.Slog.i(this.mLogTag, "zoomOn(" + centerX + ", " + centerY + ")");
        }
        float scale = android.util.MathUtils.constrain(this.mFullScreenMagnificationController.getPersistedScale(this.mDisplayId), 1.0f, MAX_SCALE);
        zoomToScale(scale, centerX, centerY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomToScale(float scale, float centerX, float centerY) {
        this.mFullScreenMagnificationController.setScaleAndCenter(this.mDisplayId, android.util.MathUtils.constrain(scale, 1.0f, MAX_SCALE), centerX, centerY, true, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomOff() {
        if (DEBUG_DETECTING) {
            android.util.Slog.i(this.mLogTag, "zoomOff()");
        }
        this.mFullScreenMagnificationController.reset(this.mDisplayId, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.view.MotionEvent recycleAndNullify(android.view.MotionEvent event) {
        if (event != null) {
            event.recycle();
            return null;
        }
        return null;
    }

    public java.lang.String toString() {
        return "MagnificationGesture{mDetectingState=" + this.mDetectingState + ", mDelegatingState=" + this.mDelegatingState + ", mMagnifiedInteractionState=" + this.mPanningScalingState + ", mViewportDraggingState=" + this.mViewportDraggingState + ", mSinglePanningState=" + this.mSinglePanningState + ", mDetectSingleFingerTripleTap=" + this.mDetectSingleFingerTripleTap + ", mDetectShortcutTrigger=" + this.mDetectShortcutTrigger + ", mCurrentState=" + com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State.nameOf(this.mCurrentState) + ", mPreviousState=" + com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State.nameOf(this.mPreviousState) + ", mMagnificationController=" + this.mFullScreenMagnificationController + ", mDisplayId=" + this.mDisplayId + ", mIsSinglePanningEnabled=" + this.mOneFingerPanningSettingsProvider.isOneFingerPanningEnabled() + ", mOverscrollHandler=" + this.mOverscrollHandler + '}';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int overscrollState(android.view.MotionEvent event, android.graphics.PointF firstPointerDownLocation) {
        if (!pointerValid(firstPointerDownLocation)) {
            return 0;
        }
        float dX = event.getX() - firstPointerDownLocation.x;
        float dY = event.getY() - firstPointerDownLocation.y;
        if (isAtLeftEdge() && dX > 0.0f) {
            return 1;
        }
        if (isAtRightEdge() && dX < 0.0f) {
            return 2;
        }
        if (!isAtTopEdge() || dY <= 0.0f) {
            return (!isAtBottomEdge() || dY >= 0.0f) ? 0 : 3;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAtLeftEdge() {
        return this.mFullScreenMagnificationController.isAtLeftEdge(this.mDisplayId, this.mOverscrollEdgeSlop);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAtRightEdge() {
        return this.mFullScreenMagnificationController.isAtRightEdge(this.mDisplayId, this.mOverscrollEdgeSlop);
    }

    private boolean isAtTopEdge() {
        return this.mFullScreenMagnificationController.isAtTopEdge(this.mDisplayId, this.mOverscrollEdgeSlop);
    }

    private boolean isAtBottomEdge() {
        return this.mFullScreenMagnificationController.isAtBottomEdge(this.mDisplayId, this.mOverscrollEdgeSlop);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean pointerValid(android.graphics.PointF pointerDownLocation) {
        return (java.lang.Float.isNaN(pointerDownLocation.x) && java.lang.Float.isNaN(pointerDownLocation.y)) ? false : true;
    }

    private static final class MotionEventInfo {
        private static final int MAX_POOL_SIZE = 10;
        private static final java.lang.Object sLock = new java.lang.Object();
        private static com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo sPool;
        private static int sPoolSize;
        public android.view.MotionEvent event;
        private boolean mInPool;
        private com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo mNext;
        public int policyFlags;
        public android.view.MotionEvent rawEvent;

        private MotionEventInfo() {
        }

        public static com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo obtain(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo info;
            synchronized (sLock) {
                info = obtainInternal();
                info.initialize(event, rawEvent, policyFlags);
            }
            return info;
        }

        private static com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo obtainInternal() {
            if (sPoolSize > 0) {
                sPoolSize--;
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo info = sPool;
                sPool = info.mNext;
                info.mNext = null;
                info.mInPool = false;
                return info;
            }
            return new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo();
        }

        private void initialize(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            this.event = android.view.MotionEvent.obtain(event);
            this.rawEvent = android.view.MotionEvent.obtain(rawEvent);
            this.policyFlags = policyFlags;
        }

        public void recycle() {
            synchronized (sLock) {
                if (this.mInPool) {
                    throw new java.lang.IllegalStateException("Already recycled.");
                }
                clear();
                if (sPoolSize < 10) {
                    sPoolSize++;
                    this.mNext = sPool;
                    sPool = this;
                    this.mInPool = true;
                }
            }
        }

        private void clear() {
            this.event = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.recycleAndNullify(this.event);
            this.rawEvent = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.recycleAndNullify(this.rawEvent);
            this.policyFlags = 0;
        }

        static int countOf(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo info, int eventType) {
            if (info == null) {
                return 0;
            }
            return (info.event.getAction() == eventType ? 1 : 0) + countOf(info.mNext, eventType);
        }

        public static java.lang.String toString(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo info) {
            if (info == null) {
                return "";
            }
            return android.view.MotionEvent.actionToString(info.event.getAction()).replace("ACTION_", "") + " " + toString(info.mNext);
        }
    }

    private static class ScreenStateReceiver extends android.content.BroadcastReceiver {
        private final android.content.Context mContext;
        private final com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler mGestureHandler;
        private boolean mRegistered = false;

        ScreenStateReceiver(android.content.Context context, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler gestureHandler) {
            this.mContext = context;
            this.mGestureHandler = gestureHandler;
        }

        public void register() {
            if (!this.mRegistered) {
                this.mContext.registerReceiver(this, new android.content.IntentFilter("android.intent.action.SCREEN_OFF"));
                this.mRegistered = true;
            }
        }

        public void unregister() {
            if (this.mRegistered) {
                this.mContext.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            this.mGestureHandler.mDetectingState.setShortcutTriggered(false);
        }
    }

    private static class GestureException extends java.lang.Exception {
        GestureException(java.lang.String message) {
            super(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPan(android.view.MotionEvent event) {
        if (!com.android.server.accessibility.Flags.fullscreenFlingGesture()) {
            return;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = android.view.VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPanningFinished(android.view.MotionEvent event) {
        if (!com.android.server.accessibility.Flags.fullscreenFlingGesture()) {
            return;
        }
        if (this.mVelocityTracker == null) {
            android.util.Log.e(this.mLogTag, "onPanningFinished: mVelocityTracker is null");
            return;
        }
        this.mVelocityTracker.addMovement(event);
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        float xPixelsPerSecond = this.mVelocityTracker.getXVelocity();
        float yPixelsPerSecond = this.mVelocityTracker.getYVelocity();
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        if (DEBUG_PANNING_SCALING) {
            android.util.Slog.v(this.mLogTag, "onPanningFinished: pixelsPerSecond: " + xPixelsPerSecond + ", " + yPixelsPerSecond + " mMinimumVelocity: " + this.mMinimumVelocity);
        }
        if (java.lang.Math.abs(yPixelsPerSecond) > this.mMinimumVelocity || java.lang.Math.abs(xPixelsPerSecond) > this.mMinimumVelocity) {
            this.mFullScreenMagnificationController.startFling(this.mDisplayId, xPixelsPerSecond, yPixelsPerSecond, 0);
        }
    }

    private void cancelFling() {
        if (!com.android.server.accessibility.Flags.fullscreenFlingGesture()) {
            return;
        }
        this.mFullScreenMagnificationController.cancelFling(this.mDisplayId, 0);
    }

    final class SinglePanningState extends android.view.GestureDetector.SimpleOnGestureListener implements com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State {
        private com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.MotionEventInfo mEvent;
        private final android.view.GestureDetector mScrollGestureDetector;

        SinglePanningState(android.content.Context context) {
            this.mScrollGestureDetector = new android.view.GestureDetector(context, this, android.os.Handler.getMain());
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            int action = event.getActionMasked();
            switch (action) {
                case 1:
                    com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.onPanningFinished(event);
                    break;
                case 2:
                default:
                    return;
                case 3:
                    break;
            }
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler != null) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler.setScaleAndCenterToEdgeIfNeeded();
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler.clearEdgeState();
            }
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDetectingState);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(android.view.MotionEvent first, android.view.MotionEvent second, float distanceX, float distanceY) {
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mCurrentState != com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mSinglePanningState) {
                return true;
            }
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.onPan(second);
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.offsetMagnifiedRegion(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, distanceX, distanceY, 0);
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.DEBUG_PANNING_SCALING) {
                android.util.Slog.i(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mLogTag, "SinglePanningState Panned content by scrollX: " + distanceX + " scrollY: " + distanceY + " isAtEdge: " + com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.isAtEdge(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId));
            }
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler != null) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mOverscrollHandler.onScrollStateChanged(first, second);
            }
            return true;
        }

        public java.lang.String toString() {
            return "SinglePanningState{isEdgeOfView=" + com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.isAtEdge(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId);
        }
    }

    final class OverscrollHandler {
        int mOverscrollState = 0;
        private final android.graphics.PointF mPivotEdge = new android.graphics.PointF(Float.NaN, Float.NaN);
        private final android.graphics.PointF mReachedEdgeCoord = new android.graphics.PointF(Float.NaN, Float.NaN);
        private boolean mEdgeCooldown = false;

        OverscrollHandler() {
        }

        protected boolean warpEffectReset(android.view.MotionEvent second) {
            float scale = calculateOverscrollScale(second);
            if (scale < 0.0f) {
                return false;
            }
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.setScaleAndCenter(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, scale, this.mPivotEdge.x, this.mPivotEdge.y, true, 0);
            return scale == 1.0f;
        }

        private float calculateOverscrollScale(android.view.MotionEvent second) {
            float overshootDistX = second.getX() - this.mReachedEdgeCoord.x;
            if ((this.mOverscrollState == 1 && overshootDistX < 0.0f) || (this.mOverscrollState == 2 && overshootDistX > 0.0f)) {
                clearEdgeState();
                return -1.0f;
            }
            float overshootDistY = second.getY() - this.mReachedEdgeCoord.y;
            float overshootDist = (float) java.lang.Math.hypot(java.lang.Math.abs(overshootDistX), java.lang.Math.abs(overshootDistY));
            android.graphics.Rect bounds = new android.graphics.Rect();
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getMagnificationBounds(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, bounds);
            float overShootFraction = overshootDist / bounds.width();
            float minDist = bounds.width() * 0.05f;
            if (this.mEdgeCooldown && overshootDist > minDist) {
                this.mEdgeCooldown = false;
            }
            float scale = (1.0f - overShootFraction) * getSensitivityScale();
            return android.util.MathUtils.constrain(scale, 1.0f, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getPersistedScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId));
        }

        private float getSensitivityScale() {
            float sensitivityFactor;
            float magnificationScale = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getPersistedScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId);
            if (magnificationScale < 1.7f || magnificationScale < 2.0f) {
                sensitivityFactor = 1.0f;
            } else if (magnificationScale < 2.2f) {
                sensitivityFactor = 0.95f;
            } else if (magnificationScale < 2.5f) {
                sensitivityFactor = 1.1f;
            } else if (magnificationScale < 2.7f) {
                sensitivityFactor = 1.3f;
            } else if (magnificationScale < 3.0f) {
                sensitivityFactor = 1.0f;
            } else {
                sensitivityFactor = 1.0f;
            }
            return magnificationScale * sensitivityFactor;
        }

        private void vibrateIfNeeded(android.view.MotionEvent event) {
            if (this.mOverscrollState != 0) {
                return;
            }
            if ((com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.isAtLeftEdge() || com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.isAtRightEdge()) && !this.mEdgeCooldown) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationVibrationHelper.vibrateIfSettingEnabled();
            }
        }

        private void setPivotEdge(android.view.MotionEvent event) {
            if (!com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.pointerValid(this.mPivotEdge)) {
                android.graphics.Rect bounds = new android.graphics.Rect();
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getMagnificationBounds(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, bounds);
                if (this.mOverscrollState == 1) {
                    this.mPivotEdge.set(bounds.left, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getCenterY(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId));
                } else if (this.mOverscrollState == 2) {
                    this.mPivotEdge.set(bounds.right, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getCenterY(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId));
                }
                this.mReachedEdgeCoord.set(event.getX(), event.getY());
                this.mEdgeCooldown = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onScrollStateChanged(android.view.MotionEvent first, android.view.MotionEvent second) {
            if (com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.isAtEdge(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId)) {
                vibrateIfNeeded(second);
                setPivotEdge(second);
            }
            switch (this.mOverscrollState) {
                case 0:
                    onNoOverscroll(first, second);
                    break;
                case 1:
                case 2:
                    onHorizontalOverscroll(second);
                    break;
                case 3:
                    onVerticalOverscroll();
                    break;
                default:
                    android.util.Slog.d(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mLogTag, "Invalid overscroll state");
                    break;
            }
        }

        public void onNoOverscroll(android.view.MotionEvent first, android.view.MotionEvent second) {
            this.mOverscrollState = com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.overscrollState(second, new android.graphics.PointF(first.getX(), first.getY()));
        }

        public void onVerticalOverscroll() {
            clearEdgeState();
            com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDelegatingState);
        }

        public void onHorizontalOverscroll(android.view.MotionEvent second) {
            boolean reset = warpEffectReset(second);
            if (reset) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.reset(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, true);
                clearEdgeState();
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.transitionTo(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDelegatingState);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setScaleAndCenterToEdgeIfNeeded() {
            if (this.mOverscrollState == 1 || this.mOverscrollState == 2) {
                com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.setScaleAndCenter(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId, com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.getPersistedScale(com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.this.mDisplayId), this.mPivotEdge.x, this.mPivotEdge.y, true, 0);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearEdgeState() {
            this.mOverscrollState = 0;
            this.mPivotEdge.set(Float.NaN, Float.NaN);
            this.mReachedEdgeCoord.set(Float.NaN, Float.NaN);
            this.mEdgeCooldown = false;
        }

        public java.lang.String toString() {
            return "OverscrollHandler {mOverscrollState=" + this.mOverscrollState + "mPivotEdge.x=" + this.mPivotEdge.x + "mPivotEdge.y=" + this.mPivotEdge.y + "mReachedEdgeCoord.x=" + this.mReachedEdgeCoord.x + "mReachedEdgeCoord.y=" + this.mReachedEdgeCoord.y + "mEdgeCooldown=" + this.mEdgeCooldown + "}";
        }
    }
}
