package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
class AccessibilityInputFilter extends android.view.InputFilter implements com.android.server.accessibility.EventStreamTransformation {
    private static final boolean DEBUG = false;
    static final int FEATURES_AFFECTING_MOTION_EVENTS = 7131;
    static final int FLAG_FEATURE_AUTOCLICK = 8;
    static final int FLAG_FEATURE_CONTROL_SCREEN_MAGNIFIER = 32;
    static final int FLAG_FEATURE_FILTER_KEY_EVENTS = 4;
    static final int FLAG_FEATURE_INJECT_MOTION_EVENTS = 16;
    static final int FLAG_FEATURE_INTERCEPT_GENERIC_MOTION_EVENTS = 2048;
    static final int FLAG_FEATURE_MAGNIFICATION_SINGLE_FINGER_TRIPLE_TAP = 1;
    static final int FLAG_FEATURE_MAGNIFICATION_TWO_FINGER_TRIPLE_TAP = 4096;
    static final int FLAG_FEATURE_TOUCH_EXPLORATION = 2;
    static final int FLAG_FEATURE_TRIGGERED_SCREEN_MAGNIFIER = 64;
    static final int FLAG_REQUEST_2_FINGER_PASSTHROUGH = 512;
    static final int FLAG_REQUEST_MULTI_FINGER_GESTURES = 256;
    static final int FLAG_SEND_MOTION_EVENTS = 1024;
    static final int FLAG_SERVICE_HANDLES_DOUBLE_TAP = 128;
    private static final java.lang.String TAG = com.android.server.accessibility.AccessibilityInputFilter.class.getSimpleName();
    private final com.android.server.accessibility.AccessibilityManagerService mAms;
    private com.android.server.accessibility.AutoclickController mAutoclickController;
    private int mCombinedGenericMotionEventSources;
    private int mCombinedMotionEventObservedSources;
    private final android.content.Context mContext;
    private int mEnabledFeatures;
    private final android.util.SparseArray<com.android.server.accessibility.EventStreamTransformation> mEventHandler;
    private com.android.server.accessibility.AccessibilityInputFilter.GenericMotionEventStreamState mGenericMotionEventStreamState;
    private boolean mInstalled;
    private com.android.server.accessibility.KeyboardInterceptor mKeyboardInterceptor;
    private com.android.server.accessibility.AccessibilityInputFilter.EventStreamState mKeyboardStreamState;
    private android.view.MotionEvent mLastActiveDeviceMotionEvent;
    private final android.util.SparseArray<com.android.server.accessibility.magnification.MagnificationGestureHandler> mMagnificationGestureHandler;
    private final android.util.SparseArray<com.android.server.accessibility.MotionEventInjector> mMotionEventInjectors;
    private final android.util.SparseArray<com.android.server.accessibility.AccessibilityInputFilter.EventStreamState> mMouseStreamStates;
    private final android.os.PowerManager mPm;
    private android.util.SparseArray<java.lang.Boolean> mServiceDetectsGestures;
    private final android.util.SparseArray<com.android.server.accessibility.gestures.TouchExplorer> mTouchExplorer;
    private final android.util.SparseArray<com.android.server.accessibility.AccessibilityInputFilter.EventStreamState> mTouchScreenStreamStates;
    private int mUserId;

    /* JADX WARN: Incorrect condition in loop: B:21:0x004b */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.view.MotionEvent cancelMotion(android.view.MotionEvent r24) {
        /*
            Method dump skipped, instruction units count: 220
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.AccessibilityInputFilter.cancelMotion(android.view.MotionEvent):android.view.MotionEvent");
    }

    AccessibilityInputFilter(android.content.Context context, com.android.server.accessibility.AccessibilityManagerService service) {
        this(context, service, new android.util.SparseArray(0));
    }

    AccessibilityInputFilter(android.content.Context context, com.android.server.accessibility.AccessibilityManagerService service, android.util.SparseArray<com.android.server.accessibility.EventStreamTransformation> eventHandler) {
        super(context.getMainLooper());
        this.mTouchExplorer = new android.util.SparseArray<>(0);
        this.mMagnificationGestureHandler = new android.util.SparseArray<>(0);
        this.mMotionEventInjectors = new android.util.SparseArray<>(0);
        this.mServiceDetectsGestures = new android.util.SparseArray<>();
        this.mMouseStreamStates = new android.util.SparseArray<>(0);
        this.mTouchScreenStreamStates = new android.util.SparseArray<>(0);
        this.mCombinedGenericMotionEventSources = 0;
        this.mCombinedMotionEventObservedSources = 0;
        this.mLastActiveDeviceMotionEvent = null;
        this.mContext = context;
        this.mAms = service;
        this.mPm = (android.os.PowerManager) context.getSystemService("power");
        this.mEventHandler = eventHandler;
    }

    public void onInstalled() {
        this.mInstalled = true;
        disableFeatures();
        enableFeatures();
        this.mAms.onInputFilterInstalled(true);
        super.onInstalled();
    }

    public void onUninstalled() {
        this.mInstalled = false;
        disableFeatures();
        this.mAms.onInputFilterInstalled(false);
        super.onUninstalled();
    }

    void onDisplayAdded(android.view.Display display) {
        enableFeaturesForDisplayIfInstalled(display);
    }

    void onDisplayRemoved(int displayId) {
        disableFeaturesForDisplayIfInstalled(displayId);
    }

    public void onInputEvent(android.view.InputEvent event, int policyFlags) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(4096L)) {
            this.mAms.getTraceManager().logTrace(TAG + ".onInputEvent", 4096L, "event=" + event + ";policyFlags=" + policyFlags);
        }
        if (com.android.server.accessibility.Flags.handleMultiDeviceInput() && !shouldProcessMultiDeviceEvent(event, policyFlags)) {
            return;
        }
        onInputEventInternal(event, policyFlags);
    }

    private void onInputEventInternal(android.view.InputEvent event, int policyFlags) {
        if (this.mEventHandler.size() == 0) {
            super.onInputEvent(event, policyFlags);
            return;
        }
        com.android.server.accessibility.AccessibilityInputFilter.EventStreamState state = getEventStreamState(event);
        if (state == null) {
            super.onInputEvent(event, policyFlags);
            return;
        }
        int eventSource = event.getSource();
        int displayId = event.getDisplayId();
        if ((1073741824 & policyFlags) == 0) {
            if (!com.android.server.accessibility.Flags.doNotResetKeyEventState()) {
                state.reset();
                clearEventStreamHandler(displayId, eventSource);
            }
            super.onInputEvent(event, policyFlags);
            return;
        }
        if (state.updateInputSource(event.getSource())) {
            clearEventStreamHandler(displayId, eventSource);
        }
        if (!state.inputSourceValid()) {
            super.onInputEvent(event, policyFlags);
            return;
        }
        if (event instanceof android.view.MotionEvent) {
            if ((this.mEnabledFeatures & FEATURES_AFFECTING_MOTION_EVENTS) != 0) {
                android.view.MotionEvent motionEvent = (android.view.MotionEvent) event;
                processMotionEvent(state, motionEvent, policyFlags);
                return;
            } else {
                super.onInputEvent(event, policyFlags);
                return;
            }
        }
        if (event instanceof android.view.KeyEvent) {
            android.view.KeyEvent keyEvent = (android.view.KeyEvent) event;
            processKeyEvent(state, keyEvent, policyFlags);
        }
    }

    private com.android.server.accessibility.AccessibilityInputFilter.EventStreamState getEventStreamState(android.view.InputEvent event) {
        if (event instanceof android.view.MotionEvent) {
            int displayId = event.getDisplayId();
            if (this.mGenericMotionEventStreamState == null) {
                this.mGenericMotionEventStreamState = new com.android.server.accessibility.AccessibilityInputFilter.GenericMotionEventStreamState();
            }
            if (this.mGenericMotionEventStreamState.shouldProcessMotionEvent((android.view.MotionEvent) event)) {
                return this.mGenericMotionEventStreamState;
            }
            if (event.isFromSource(4098)) {
                com.android.server.accessibility.AccessibilityInputFilter.EventStreamState touchScreenStreamState = this.mTouchScreenStreamStates.get(displayId);
                if (touchScreenStreamState == null) {
                    com.android.server.accessibility.AccessibilityInputFilter.EventStreamState touchScreenStreamState2 = new com.android.server.accessibility.AccessibilityInputFilter.TouchScreenEventStreamState();
                    this.mTouchScreenStreamStates.put(displayId, touchScreenStreamState2);
                    return touchScreenStreamState2;
                }
                return touchScreenStreamState;
            }
            if (event.isFromSource(8194)) {
                com.android.server.accessibility.AccessibilityInputFilter.EventStreamState mouseStreamState = this.mMouseStreamStates.get(displayId);
                if (mouseStreamState == null) {
                    com.android.server.accessibility.AccessibilityInputFilter.EventStreamState mouseStreamState2 = new com.android.server.accessibility.AccessibilityInputFilter.MouseEventStreamState();
                    this.mMouseStreamStates.put(displayId, mouseStreamState2);
                    return mouseStreamState2;
                }
                return mouseStreamState;
            }
        } else if ((event instanceof android.view.KeyEvent) && event.isFromSource(257)) {
            if (this.mKeyboardStreamState == null) {
                this.mKeyboardStreamState = new com.android.server.accessibility.AccessibilityInputFilter.KeyboardEventStreamState();
            }
            return this.mKeyboardStreamState;
        }
        return null;
    }

    private void clearEventStreamHandler(int displayId, int eventSource) {
        com.android.server.accessibility.EventStreamTransformation eventHandler = this.mEventHandler.get(displayId);
        if (eventHandler != null) {
            eventHandler.clearEvents(eventSource);
        }
    }

    boolean shouldProcessMultiDeviceEvent(android.view.InputEvent event, int policyFlags) {
        if (event instanceof android.view.MotionEvent) {
            android.view.MotionEvent motion = (android.view.MotionEvent) event;
            if (motion.isFromSource(2) && motion.getAction() != 8) {
                boolean eventIsFromCurrentDevice = this.mLastActiveDeviceMotionEvent != null && this.mLastActiveDeviceMotionEvent.getDeviceId() == motion.getDeviceId();
                int actionMasked = motion.getActionMasked();
                switch (actionMasked) {
                    case 0:
                    case 7:
                    case 9:
                        if (this.mLastActiveDeviceMotionEvent != null && this.mLastActiveDeviceMotionEvent.getDeviceId() != motion.getDeviceId()) {
                            android.view.MotionEvent canceled = cancelMotion(this.mLastActiveDeviceMotionEvent);
                            onInputEventInternal(canceled, policyFlags);
                        }
                        android.view.MotionEvent canceled2 = android.view.MotionEvent.obtain(motion);
                        this.mLastActiveDeviceMotionEvent = canceled2;
                        return true;
                    case 1:
                    case 3:
                    case 10:
                        if (!eventIsFromCurrentDevice) {
                            return false;
                        }
                        this.mLastActiveDeviceMotionEvent = null;
                        return true;
                    case 2:
                    case 5:
                    case 6:
                        if (!eventIsFromCurrentDevice) {
                            return false;
                        }
                        this.mLastActiveDeviceMotionEvent = android.view.MotionEvent.obtain(motion);
                        return true;
                    case 4:
                    case 8:
                    default:
                        if (this.mLastActiveDeviceMotionEvent != null && event.getDeviceId() != this.mLastActiveDeviceMotionEvent.getDeviceId()) {
                            return false;
                        }
                        break;
                }
            } else {
                return true;
            }
        }
        return true;
    }

    private void processMotionEvent(com.android.server.accessibility.AccessibilityInputFilter.EventStreamState state, android.view.MotionEvent event, int policyFlags) {
        if (!state.shouldProcessScroll() && event.getActionMasked() == 8) {
            super.onInputEvent(event, policyFlags);
        } else {
            if (!state.shouldProcessMotionEvent(event)) {
                return;
            }
            handleMotionEvent(event, policyFlags);
        }
    }

    private void processKeyEvent(com.android.server.accessibility.AccessibilityInputFilter.EventStreamState state, android.view.KeyEvent event, int policyFlags) {
        if (!state.shouldProcessKeyEvent(event)) {
            super.onInputEvent(event, policyFlags);
        } else {
            this.mEventHandler.get(0).onKeyEvent(event, policyFlags);
        }
    }

    private void handleMotionEvent(android.view.MotionEvent event, int policyFlags) {
        this.mPm.userActivity(event.getEventTime(), false);
        android.view.MotionEvent transformedEvent = android.view.MotionEvent.obtain(event);
        if (transformedEvent.getAction() == 0) {
            float touchMinor = transformedEvent.getTouchMinor();
            if (java.lang.Float.compare(touchMinor, 25.5f) == 0) {
                android.util.Slog.w(TAG, "reject SP motion event to improve performance");
                transformedEvent.recycle();
                return;
            }
        }
        int displayId = event.getDisplayId();
        try {
            com.android.server.accessibility.EventStreamTransformation eventStreamTransformation = this.mEventHandler.get(isDisplayIdValid(displayId) ? displayId : 0);
            if (eventStreamTransformation != null) {
                eventStreamTransformation.onMotionEvent(transformedEvent, event, policyFlags);
            }
        } catch (java.lang.IllegalArgumentException e) {
            e.printStackTrace();
        }
        transformedEvent.recycle();
    }

    private boolean isDisplayIdValid(int displayId) {
        return this.mEventHandler.get(displayId) != null;
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onMotionEvent(android.view.MotionEvent transformedEvent, android.view.MotionEvent rawEvent, int policyFlags) {
        if (!this.mInstalled) {
            android.util.Slog.w(TAG, "onMotionEvent called before input filter installed!");
        } else {
            sendInputEvent(transformedEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onKeyEvent(android.view.KeyEvent event, int policyFlags) {
        if (!this.mInstalled) {
            android.util.Slog.w(TAG, "onKeyEvent called before input filter installed!");
        } else {
            sendInputEvent(event, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void setNext(com.android.server.accessibility.EventStreamTransformation sink) {
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public com.android.server.accessibility.EventStreamTransformation getNext() {
        return null;
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void clearEvents(int inputSource) {
    }

    void setUserAndEnabledFeatures(int userId, int enabledFeatures) {
        if (this.mEnabledFeatures == enabledFeatures && this.mUserId == userId) {
            return;
        }
        if (this.mInstalled) {
            disableFeatures();
        }
        this.mUserId = userId;
        this.mEnabledFeatures = enabledFeatures;
        if (this.mInstalled) {
            enableFeatures();
        }
    }

    void notifyAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        for (int i = 0; i < this.mEventHandler.size(); i++) {
            com.android.server.accessibility.EventStreamTransformation eventHandler = this.mEventHandler.valueAt(i);
            if (eventHandler != null) {
                eventHandler.onAccessibilityEvent(event);
            }
        }
    }

    void notifyAccessibilityButtonClicked(int displayId) {
        com.android.server.accessibility.magnification.MagnificationGestureHandler handler;
        if (this.mMagnificationGestureHandler.size() != 0 && (handler = this.mMagnificationGestureHandler.get(displayId)) != null) {
            handler.notifyShortcutTriggered();
        }
    }

    private void enableFeatures() {
        resetAllStreamState();
        java.util.ArrayList<android.view.Display> displaysList = this.mAms.getValidDisplayList();
        for (int i = displaysList.size() - 1; i >= 0; i--) {
            enableFeaturesForDisplay(displaysList.get(i));
        }
        enableDisplayIndependentFeatures();
    }

    private void enableFeaturesForDisplay(android.view.Display display) {
        android.content.Context displayContext = this.mContext.createDisplayContext(display);
        int displayId = display.getDisplayId();
        if (this.mAms.isDisplayProxyed(displayId)) {
            return;
        }
        if (!this.mServiceDetectsGestures.contains(displayId)) {
            this.mServiceDetectsGestures.put(displayId, false);
        }
        if ((this.mEnabledFeatures & 8) != 0) {
            if (this.mAutoclickController == null) {
                this.mAutoclickController = new com.android.server.accessibility.AutoclickController(this.mContext, this.mUserId, this.mAms.getTraceManager());
            }
            addFirstEventHandler(displayId, this.mAutoclickController);
        }
        if ((this.mEnabledFeatures & 2) != 0) {
            com.android.server.accessibility.gestures.TouchExplorer explorer = new com.android.server.accessibility.gestures.TouchExplorer(displayContext, this.mAms);
            if ((this.mEnabledFeatures & 128) != 0) {
                explorer.setServiceHandlesDoubleTap(true);
            }
            if ((this.mEnabledFeatures & 256) != 0) {
                explorer.setMultiFingerGesturesEnabled(true);
            }
            if ((this.mEnabledFeatures & 512) != 0) {
                explorer.setTwoFingerPassthroughEnabled(true);
            }
            if ((this.mEnabledFeatures & 1024) != 0) {
                explorer.setSendMotionEventsEnabled(true);
            }
            explorer.setServiceDetectsGestures(this.mServiceDetectsGestures.get(displayId).booleanValue());
            addFirstEventHandler(displayId, explorer);
            this.mTouchExplorer.put(displayId, explorer);
        }
        if ((this.mEnabledFeatures & 2048) != 0) {
            addFirstEventHandler(displayId, new com.android.server.accessibility.BaseEventStreamTransformation() { // from class: com.android.server.accessibility.AccessibilityInputFilter.1
                @Override // com.android.server.accessibility.EventStreamTransformation
                public void onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
                    boolean passAlongEvent = true;
                    if (com.android.server.accessibility.AccessibilityInputFilter.this.anyServiceWantsGenericMotionEvent(event)) {
                        if (com.android.server.accessibility.AccessibilityInputFilter.this.mAms.sendMotionEventToListeningServices(event)) {
                            passAlongEvent = false;
                        }
                        if (com.android.server.accessibility.AccessibilityInputFilter.this.anyServiceWantsToObserveMotionEvent(event)) {
                            passAlongEvent = true;
                        }
                    }
                    if (passAlongEvent) {
                        super.onMotionEvent(event, rawEvent, policyFlags);
                    }
                }
            });
        }
        if ((this.mEnabledFeatures & 32) != 0 || (this.mEnabledFeatures & 1) != 0 || (this.mEnabledFeatures & 4096) != 0 || (this.mEnabledFeatures & 64) != 0) {
            com.android.server.accessibility.magnification.MagnificationGestureHandler magnificationGestureHandler = createMagnificationGestureHandler(displayId, displayContext);
            addFirstEventHandler(displayId, magnificationGestureHandler);
            this.mMagnificationGestureHandler.put(displayId, magnificationGestureHandler);
        }
        if ((this.mEnabledFeatures & 16) != 0) {
            com.android.server.accessibility.MotionEventInjector injector = new com.android.server.accessibility.MotionEventInjector(this.mContext.getMainLooper(), this.mAms.getTraceManager());
            addFirstEventHandler(displayId, injector);
            this.mMotionEventInjectors.put(displayId, injector);
        }
    }

    private void enableDisplayIndependentFeatures() {
        if ((this.mEnabledFeatures & 16) != 0) {
            this.mAms.setMotionEventInjectors(this.mMotionEventInjectors);
        }
        if ((this.mEnabledFeatures & 4) != 0) {
            this.mKeyboardInterceptor = new com.android.server.accessibility.KeyboardInterceptor(this.mAms, (com.android.server.policy.WindowManagerPolicy) com.android.server.LocalServices.getService(com.android.server.policy.WindowManagerPolicy.class));
            addFirstEventHandler(0, this.mKeyboardInterceptor);
        }
    }

    private void addFirstEventHandler(int displayId, com.android.server.accessibility.EventStreamTransformation handler) {
        com.android.server.accessibility.EventStreamTransformation eventHandler = this.mEventHandler.get(displayId);
        if (eventHandler != null) {
            handler.setNext(eventHandler);
        } else {
            handler.setNext(this);
        }
        this.mEventHandler.put(displayId, handler);
    }

    private void disableFeatures() {
        java.util.ArrayList<android.view.Display> displaysList = this.mAms.getValidDisplayList();
        for (int i = displaysList.size() - 1; i >= 0; i--) {
            disableFeaturesForDisplay(displaysList.get(i).getDisplayId());
        }
        this.mAms.setMotionEventInjectors(null);
        disableDisplayIndependentFeatures();
        resetAllStreamState();
    }

    private void disableFeaturesForDisplay(int displayId) {
        com.android.server.accessibility.MotionEventInjector injector = this.mMotionEventInjectors.get(displayId);
        if (injector != null) {
            injector.onDestroy();
            this.mMotionEventInjectors.remove(displayId);
        }
        com.android.server.accessibility.gestures.TouchExplorer explorer = this.mTouchExplorer.get(displayId);
        if (explorer != null) {
            explorer.onDestroy();
            this.mTouchExplorer.remove(displayId);
        }
        com.android.server.accessibility.magnification.MagnificationGestureHandler handler = this.mMagnificationGestureHandler.get(displayId);
        if (handler != null) {
            handler.onDestroy();
            this.mMagnificationGestureHandler.remove(displayId);
        }
        com.android.server.accessibility.EventStreamTransformation eventStreamTransformation = this.mEventHandler.get(displayId);
        if (eventStreamTransformation != null) {
            this.mEventHandler.remove(displayId);
        }
    }

    void enableFeaturesForDisplayIfInstalled(android.view.Display display) {
        if (this.mInstalled) {
            resetStreamStateForDisplay(display.getDisplayId());
            enableFeaturesForDisplay(display);
        }
    }

    void disableFeaturesForDisplayIfInstalled(int displayId) {
        if (this.mInstalled) {
            disableFeaturesForDisplay(displayId);
            resetStreamStateForDisplay(displayId);
        }
    }

    private void disableDisplayIndependentFeatures() {
        if (this.mAutoclickController != null) {
            this.mAutoclickController.onDestroy();
            this.mAutoclickController = null;
        }
        if (this.mKeyboardInterceptor != null) {
            this.mKeyboardInterceptor.onDestroy();
            this.mKeyboardInterceptor = null;
        }
    }

    private com.android.server.accessibility.magnification.MagnificationGestureHandler createMagnificationGestureHandler(int displayId, android.content.Context displayContext) {
        boolean detectControlGestures = (this.mEnabledFeatures & 1) != 0;
        boolean detectTwoFingerTripleTap = (this.mEnabledFeatures & 4096) != 0;
        boolean triggerable = (this.mEnabledFeatures & 64) != 0;
        if (this.mAms.getMagnificationMode(displayId) == 2) {
            com.android.server.accessibility.magnification.MagnificationGestureHandler magnificationGestureHandler = new com.android.server.accessibility.magnification.WindowMagnificationGestureHandler(displayContext.createWindowContext(2039, null), this.mAms.getMagnificationConnectionManager(), this.mAms.getTraceManager(), this.mAms.getMagnificationController(), detectControlGestures, detectTwoFingerTripleTap, triggerable, displayId);
            return magnificationGestureHandler;
        }
        android.content.Context uiContext = displayContext.createWindowContext(2027, null);
        com.android.server.accessibility.magnification.FullScreenMagnificationVibrationHelper fullScreenMagnificationVibrationHelper = new com.android.server.accessibility.magnification.FullScreenMagnificationVibrationHelper(uiContext);
        com.android.server.accessibility.magnification.MagnificationGestureHandler magnificationGestureHandler2 = new com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler(uiContext, this.mAms.getMagnificationController().getFullScreenMagnificationController(), this.mAms.getTraceManager(), this.mAms.getMagnificationController(), detectControlGestures, detectTwoFingerTripleTap, triggerable, new com.android.server.accessibility.magnification.WindowMagnificationPromptController(displayContext, this.mUserId), displayId, fullScreenMagnificationVibrationHelper);
        return magnificationGestureHandler2;
    }

    void resetAllStreamState() {
        java.util.ArrayList<android.view.Display> displaysList = this.mAms.getValidDisplayList();
        for (int i = displaysList.size() - 1; i >= 0; i--) {
            resetStreamStateForDisplay(displaysList.get(i).getDisplayId());
        }
        if (this.mKeyboardStreamState != null) {
            this.mKeyboardStreamState.reset();
        }
    }

    void resetStreamStateForDisplay(int displayId) {
        com.android.server.accessibility.AccessibilityInputFilter.EventStreamState touchScreenStreamState = this.mTouchScreenStreamStates.get(displayId);
        if (touchScreenStreamState != null) {
            touchScreenStreamState.reset();
            this.mTouchScreenStreamStates.remove(displayId);
        }
        com.android.server.accessibility.AccessibilityInputFilter.EventStreamState mouseStreamState = this.mMouseStreamStates.get(displayId);
        if (mouseStreamState != null) {
            mouseStreamState.reset();
            this.mMouseStreamStates.remove(displayId);
        }
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onDestroy() {
    }

    public void refreshMagnificationMode(android.view.Display display) {
        int displayId = display.getDisplayId();
        com.android.server.accessibility.magnification.MagnificationGestureHandler magnificationGestureHandler = this.mMagnificationGestureHandler.get(displayId);
        if (magnificationGestureHandler == null || magnificationGestureHandler.getMode() == this.mAms.getMagnificationMode(displayId)) {
            return;
        }
        magnificationGestureHandler.onDestroy();
        com.android.server.accessibility.magnification.MagnificationGestureHandler currentMagnificationGestureHandler = createMagnificationGestureHandler(displayId, this.mContext.createDisplayContext(display));
        switchEventStreamTransformation(displayId, magnificationGestureHandler, currentMagnificationGestureHandler);
        this.mMagnificationGestureHandler.put(displayId, currentMagnificationGestureHandler);
    }

    private void switchEventStreamTransformation(int displayId, com.android.server.accessibility.EventStreamTransformation oldStreamTransformation, com.android.server.accessibility.EventStreamTransformation currentStreamTransformation) {
        com.android.server.accessibility.EventStreamTransformation eventStreamTransformation = this.mEventHandler.get(displayId);
        if (eventStreamTransformation == null) {
            return;
        }
        if (eventStreamTransformation == oldStreamTransformation) {
            currentStreamTransformation.setNext(oldStreamTransformation.getNext());
            this.mEventHandler.put(displayId, currentStreamTransformation);
            return;
        }
        while (eventStreamTransformation != null) {
            if (eventStreamTransformation.getNext() == oldStreamTransformation) {
                eventStreamTransformation.setNext(currentStreamTransformation);
                currentStreamTransformation.setNext(oldStreamTransformation.getNext());
                return;
            }
            eventStreamTransformation = eventStreamTransformation.getNext();
        }
    }

    private static class EventStreamState {
        private int mSource = -1;

        EventStreamState() {
        }

        public boolean updateInputSource(int source) {
            if (this.mSource == source) {
                return false;
            }
            reset();
            this.mSource = source;
            return true;
        }

        public boolean inputSourceValid() {
            return this.mSource >= 0;
        }

        public void reset() {
            this.mSource = -1;
        }

        public boolean shouldProcessScroll() {
            return false;
        }

        public boolean shouldProcessMotionEvent(android.view.MotionEvent event) {
            return false;
        }

        public boolean shouldProcessKeyEvent(android.view.KeyEvent event) {
            return false;
        }
    }

    private static class MouseEventStreamState extends com.android.server.accessibility.AccessibilityInputFilter.EventStreamState {
        private boolean mMotionSequenceStarted;

        public MouseEventStreamState() {
            reset();
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public final void reset() {
            super.reset();
            this.mMotionSequenceStarted = false;
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public final boolean shouldProcessScroll() {
            return true;
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public final boolean shouldProcessMotionEvent(android.view.MotionEvent event) {
            boolean z = true;
            if (this.mMotionSequenceStarted) {
                return true;
            }
            int action = event.getActionMasked();
            if (action != 0 && action != 7) {
                z = false;
            }
            this.mMotionSequenceStarted = z;
            return this.mMotionSequenceStarted;
        }
    }

    private static class TouchScreenEventStreamState extends com.android.server.accessibility.AccessibilityInputFilter.EventStreamState {
        private boolean mHoverSequenceStarted;
        private boolean mTouchSequenceStarted;

        public TouchScreenEventStreamState() {
            reset();
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public final void reset() {
            super.reset();
            this.mTouchSequenceStarted = false;
            this.mHoverSequenceStarted = false;
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public final boolean shouldProcessMotionEvent(android.view.MotionEvent event) {
            if (event.isTouchEvent()) {
                if (this.mTouchSequenceStarted) {
                    return true;
                }
                this.mTouchSequenceStarted = event.getActionMasked() == 0;
                return this.mTouchSequenceStarted;
            }
            if (this.mHoverSequenceStarted) {
                return true;
            }
            this.mHoverSequenceStarted = event.getActionMasked() == 9;
            return this.mHoverSequenceStarted;
        }
    }

    private class GenericMotionEventStreamState extends com.android.server.accessibility.AccessibilityInputFilter.EventStreamState {
        private GenericMotionEventStreamState() {
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public boolean shouldProcessMotionEvent(android.view.MotionEvent event) {
            return com.android.server.accessibility.AccessibilityInputFilter.this.anyServiceWantsGenericMotionEvent(event);
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public boolean shouldProcessScroll() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean anyServiceWantsToObserveMotionEvent(android.view.MotionEvent event) {
        if (event.isFromSource(4098) && (this.mEnabledFeatures & 2) != 0) {
            return false;
        }
        int eventSourceWithoutClass = event.getSource() & (-256);
        return ((this.mCombinedGenericMotionEventSources & this.mCombinedMotionEventObservedSources) & eventSourceWithoutClass) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean anyServiceWantsGenericMotionEvent(android.view.MotionEvent event) {
        if (event.isFromSource(4098) && (this.mEnabledFeatures & 2) != 0) {
            return false;
        }
        int eventSourceWithoutClass = event.getSource() & (-256);
        return (this.mCombinedGenericMotionEventSources & eventSourceWithoutClass) != 0;
    }

    public void setCombinedGenericMotionEventSources(int sources) {
        this.mCombinedGenericMotionEventSources = sources;
    }

    public void setCombinedMotionEventObservedSources(int sources) {
        this.mCombinedMotionEventObservedSources = sources;
    }

    private static class KeyboardEventStreamState extends com.android.server.accessibility.AccessibilityInputFilter.EventStreamState {
        private android.util.SparseBooleanArray mEventSequenceStartedMap = new android.util.SparseBooleanArray();

        public KeyboardEventStreamState() {
            reset();
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public final void reset() {
            super.reset();
            this.mEventSequenceStartedMap.clear();
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public boolean updateInputSource(int deviceId) {
            return false;
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public boolean inputSourceValid() {
            return true;
        }

        @Override // com.android.server.accessibility.AccessibilityInputFilter.EventStreamState
        public final boolean shouldProcessKeyEvent(android.view.KeyEvent event) {
            int deviceId = event.getDeviceId();
            if (this.mEventSequenceStartedMap.get(deviceId, false)) {
                return true;
            }
            boolean shouldProcess = event.getAction() == 0;
            this.mEventSequenceStartedMap.put(deviceId, shouldProcess);
            return shouldProcess;
        }
    }

    public void setGestureDetectionPassthroughRegion(int displayId, android.graphics.Region region) {
        if (region != null && this.mTouchExplorer.contains(displayId)) {
            this.mTouchExplorer.get(displayId).setGestureDetectionPassthroughRegion(region);
        }
    }

    public void setTouchExplorationPassthroughRegion(int displayId, android.graphics.Region region) {
        if (region != null && this.mTouchExplorer.contains(displayId)) {
            this.mTouchExplorer.get(displayId).setTouchExplorationPassthroughRegion(region);
        }
    }

    public void setServiceDetectsGesturesEnabled(int displayId, boolean mode) {
        if (this.mTouchExplorer.contains(displayId)) {
            this.mTouchExplorer.get(displayId).setServiceDetectsGestures(mode);
        }
        this.mServiceDetectsGestures.put(displayId, java.lang.Boolean.valueOf(mode));
    }

    public void resetServiceDetectsGestures() {
        this.mServiceDetectsGestures.clear();
    }

    public void requestTouchExploration(int displayId) {
        if (this.mTouchExplorer.contains(displayId)) {
            this.mTouchExplorer.get(displayId).requestTouchExploration();
        }
    }

    public void requestDragging(int displayId, int pointerId) {
        if (this.mTouchExplorer.contains(displayId)) {
            this.mTouchExplorer.get(displayId).requestDragging(pointerId);
        }
    }

    public void requestDelegating(int displayId) {
        if (this.mTouchExplorer.contains(displayId)) {
            this.mTouchExplorer.get(displayId).requestDelegating();
        }
    }

    public void onDoubleTap(int displayId) {
        if (this.mTouchExplorer.contains(displayId)) {
            this.mTouchExplorer.get(displayId).onDoubleTap();
        }
    }

    public void onDoubleTapAndHold(int displayId) {
        if (this.mTouchExplorer.contains(displayId)) {
            this.mTouchExplorer.get(displayId).onDoubleTapAndHold();
        }
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (this.mEventHandler == null) {
            return;
        }
        pw.append("A11yInputFilter Info : ");
        pw.println();
        java.util.ArrayList<android.view.Display> displaysList = this.mAms.getValidDisplayList();
        for (int i = 0; i < displaysList.size(); i++) {
            int displayId = displaysList.get(i).getDisplayId();
            com.android.server.accessibility.EventStreamTransformation next = this.mEventHandler.get(displayId);
            if (next != null) {
                pw.append("Enabled features of Display [");
                pw.append((java.lang.CharSequence) java.lang.Integer.toString(displayId));
                pw.append("] = ");
                java.util.StringJoiner joiner = new java.util.StringJoiner(",", "[", "]");
                while (next != null) {
                    if (next instanceof com.android.server.accessibility.magnification.MagnificationGestureHandler) {
                        joiner.add("MagnificationGesture");
                    } else if (next instanceof com.android.server.accessibility.KeyboardInterceptor) {
                        joiner.add("KeyboardInterceptor");
                    } else if (next instanceof com.android.server.accessibility.gestures.TouchExplorer) {
                        joiner.add("TouchExplorer");
                    } else if (next instanceof com.android.server.accessibility.AutoclickController) {
                        joiner.add("AutoclickController");
                    } else if (next instanceof com.android.server.accessibility.MotionEventInjector) {
                        joiner.add("MotionEventInjector");
                    }
                    next = next.getNext();
                }
                pw.append((java.lang.CharSequence) joiner.toString());
            }
            pw.println();
        }
    }
}
