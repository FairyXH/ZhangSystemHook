package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class DisplayPowerState {
    private final java.lang.String TAG;
    private java.util.concurrent.Executor mAsyncDestroyExecutor;
    private final com.android.server.display.DisplayBlanker mBlanker;
    private final android.view.Choreographer mChoreographer;
    private java.lang.Runnable mCleanListener;
    private final com.android.server.display.ColorFade mColorFade;
    private boolean mColorFadeDrawPending;
    final java.lang.Runnable mColorFadeDrawRunnable;
    private float mColorFadeLevel;
    private boolean mColorFadePrepared;
    private boolean mColorFadeReady;
    private final int mDisplayId;
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private com.android.server.display.DisplayPowerState.DisplayPowerStateWrapper mDpsWrapper;
    private final android.os.Handler mHandler;
    private final com.android.server.display.DisplayPowerState.PhotonicModulator mPhotonicModulator;
    private float mScreenBrightness;
    private boolean mScreenReady;
    private int mScreenState;
    private boolean mScreenUpdatePending;
    private final java.lang.Runnable mScreenUpdateRunnable;
    private float mSdrScreenBrightness;
    private volatile boolean mStopped;
    private static final boolean PANIC_DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static boolean DEBUG = android.os.SystemProperties.getBoolean("dbg.dms.dps", false);
    private static java.lang.String COUNTER_COLOR_FADE = "ColorFadeLevel";
    public static final boolean VRR_BRIGHTNESS_RM = android.os.SystemProperties.getBoolean("ro.oplus.display.vrr.brightness.rm", false);
    public static final android.util.FloatProperty<com.android.server.display.DisplayPowerState> COLOR_FADE_LEVEL = new android.util.FloatProperty<com.android.server.display.DisplayPowerState>("electronBeamLevel") { // from class: com.android.server.display.DisplayPowerState.1
        @Override // android.util.FloatProperty
        public void setValue(com.android.server.display.DisplayPowerState object, float value) {
            object.setColorFadeLevel(value);
        }

        @Override // android.util.Property
        public java.lang.Float get(com.android.server.display.DisplayPowerState object) {
            return java.lang.Float.valueOf(object.getColorFadeLevel());
        }
    };
    public static final android.util.FloatProperty<com.android.server.display.DisplayPowerState> SCREEN_BRIGHTNESS_FLOAT = new android.util.FloatProperty<com.android.server.display.DisplayPowerState>("screenBrightnessFloat") { // from class: com.android.server.display.DisplayPowerState.2
        @Override // android.util.FloatProperty
        public void setValue(com.android.server.display.DisplayPowerState object, float value) {
            object.setScreenBrightness(value);
        }

        @Override // android.util.Property
        public java.lang.Float get(com.android.server.display.DisplayPowerState object) {
            return java.lang.Float.valueOf(object.getScreenBrightness());
        }
    };
    public static final android.util.FloatProperty<com.android.server.display.DisplayPowerState> SCREEN_SDR_BRIGHTNESS_FLOAT = new android.util.FloatProperty<com.android.server.display.DisplayPowerState>("sdrScreenBrightnessFloat") { // from class: com.android.server.display.DisplayPowerState.3
        @Override // android.util.FloatProperty
        public void setValue(com.android.server.display.DisplayPowerState object, float value) {
            object.setSdrScreenBrightness(value);
        }

        @Override // android.util.Property
        public java.lang.Float get(com.android.server.display.DisplayPowerState object) {
            return java.lang.Float.valueOf(object.getSdrScreenBrightness());
        }
    };

    DisplayPowerState(com.android.server.display.DisplayBlanker blanker, com.android.server.display.ColorFade colorFade, int displayId, int displayState, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        this(blanker, colorFade, displayId, displayState, dpcExt, com.android.internal.os.BackgroundThread.getExecutor());
    }

    DisplayPowerState(com.android.server.display.DisplayBlanker blanker, com.android.server.display.ColorFade colorFade, int displayId, int displayState, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt, java.util.concurrent.Executor asyncDestroyExecutor) {
        this.mDpcExt = null;
        this.mScreenUpdateRunnable = new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerState.4
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.DisplayPowerState.this.mScreenUpdatePending = false;
                float f = -1.0f;
                float brightnessState = (com.android.server.display.DisplayPowerState.this.mScreenState == 1 || com.android.server.display.DisplayPowerState.this.mColorFadeLevel <= 0.0f) ? -1.0f : com.android.server.display.DisplayPowerState.this.mScreenBrightness;
                if (com.android.server.display.DisplayPowerState.this.mScreenState != 1 && com.android.server.display.DisplayPowerState.this.mColorFadeLevel > 0.0f) {
                    f = com.android.server.display.DisplayPowerState.this.mSdrScreenBrightness;
                }
                float sdrBrightnessState = f;
                android.util.Pair<java.lang.Float, java.lang.Float> pair = com.android.server.display.DisplayPowerState.this.mDpsWrapper.getExtImpl().screenUpdateExt(com.android.server.display.DisplayPowerState.this.mScreenState, brightnessState, sdrBrightnessState, com.android.server.display.DisplayPowerState.this.mScreenBrightness, com.android.server.display.DisplayPowerState.this.mColorFadeLevel, com.android.server.display.DisplayPowerState.this.mDisplayId);
                float brightnessState2 = ((java.lang.Float) pair.first).floatValue();
                float sdrBrightnessState2 = ((java.lang.Float) pair.second).floatValue();
                if (com.android.server.display.DisplayPowerState.this.mColorFadeLevel == 0.0f) {
                    android.util.Slog.d(com.android.server.display.DisplayPowerState.this.TAG, "updateRunnable run fadeLevle=" + com.android.server.display.DisplayPowerState.this.mColorFadeLevel + " state=" + android.view.Display.stateToString(com.android.server.display.DisplayPowerState.this.mScreenState) + " brightness changed:" + com.android.server.display.DisplayPowerState.this.mScreenBrightness + "->" + brightnessState2);
                }
                if (com.android.server.display.DisplayPowerState.this.mPhotonicModulator.setState(com.android.server.display.DisplayPowerState.this.mScreenState, brightnessState2, sdrBrightnessState2)) {
                    if (com.android.server.display.DisplayPowerState.DEBUG) {
                        android.util.Slog.d(com.android.server.display.DisplayPowerState.this.TAG, "Screen ready");
                    }
                    com.android.server.display.DisplayPowerState.this.mScreenReady = true;
                    com.android.server.display.DisplayPowerState.this.invokeCleanListenerIfNeeded();
                    return;
                }
                if (com.android.server.display.DisplayPowerState.DEBUG) {
                    android.util.Slog.d(com.android.server.display.DisplayPowerState.this.TAG, "Screen not ready");
                }
            }
        };
        this.mColorFadeDrawRunnable = new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerState.5
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.DisplayPowerState.this.mColorFadeDrawPending = false;
                if (com.android.server.display.DisplayPowerState.this.mColorFadePrepared) {
                    com.android.server.display.DisplayPowerState.this.mColorFade.draw(com.android.server.display.DisplayPowerState.this.mColorFadeLevel);
                    android.os.Trace.traceCounter(131072L, com.android.server.display.DisplayPowerState.COUNTER_COLOR_FADE, java.lang.Math.round(com.android.server.display.DisplayPowerState.this.mColorFadeLevel * 100.0f));
                }
                com.android.server.display.DisplayPowerState.this.mColorFadeReady = true;
                com.android.server.display.DisplayPowerState.this.invokeCleanListenerIfNeeded();
            }
        };
        this.mDpsWrapper = new com.android.server.display.DisplayPowerState.DisplayPowerStateWrapper();
        this.mHandler = new android.os.Handler(true);
        this.mChoreographer = android.view.Choreographer.getInstance();
        this.mBlanker = blanker;
        this.mColorFade = colorFade;
        this.mPhotonicModulator = new com.android.server.display.DisplayPowerState.PhotonicModulator();
        this.mPhotonicModulator.start();
        this.mDisplayId = displayId;
        this.mAsyncDestroyExecutor = asyncDestroyExecutor;
        this.mScreenState = displayState;
        this.mScreenBrightness = this.mDpsWrapper.getExtImpl().getBootupBrightness();
        this.mDpcExt = dpcExt;
        this.TAG = "DisplayPowerState[" + this.mDisplayId + "]";
        this.mSdrScreenBrightness = this.mScreenBrightness;
        scheduleScreenUpdate();
        this.mColorFadePrepared = false;
        this.mColorFadeLevel = 1.0f;
        this.mColorFadeReady = true;
    }

    public void setScreenState(int state, int reason) {
        if (this.mScreenState != state) {
            android.util.Slog.d(this.TAG, "setScreenState changed:" + android.view.Display.stateToString(this.mScreenState) + "->" + android.view.Display.stateToString(state));
            this.mScreenState = state;
            this.mScreenReady = false;
            scheduleScreenUpdate();
        }
    }

    public int getScreenState() {
        return this.mScreenState;
    }

    public void setSdrScreenBrightness(float brightness) {
        if (!com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mSdrScreenBrightness, brightness)) {
            if (DEBUG) {
                android.util.Slog.d(this.TAG, "setSdrScreenBrightness: brightness=" + brightness);
            }
            this.mSdrScreenBrightness = brightness;
            if (this.mScreenState != 1) {
                this.mScreenReady = false;
                scheduleScreenUpdate();
            }
        }
    }

    public float getSdrScreenBrightness() {
        return this.mSdrScreenBrightness;
    }

    public void setScreenBrightness(float brightness) {
        if (!com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mScreenBrightness, brightness)) {
            if (DEBUG) {
                android.util.Slog.d(this.TAG, "setScreenBrightness: brightness=" + brightness);
            }
            this.mScreenBrightness = brightness;
            if (this.mScreenState != 1) {
                this.mScreenReady = false;
                scheduleScreenUpdate();
            }
        }
    }

    public float getScreenBrightness() {
        return this.mScreenBrightness;
    }

    public boolean prepareColorFade(android.content.Context context, int mode) {
        if (this.mColorFade == null || !this.mColorFade.prepare(context, mode)) {
            this.mColorFadePrepared = false;
            this.mColorFadeReady = true;
            return false;
        }
        this.mColorFadePrepared = true;
        this.mColorFadeReady = false;
        scheduleColorFadeDraw();
        return true;
    }

    public void dismissColorFade() {
        android.os.Trace.traceCounter(131072L, COUNTER_COLOR_FADE, 100);
        if (this.mColorFade != null) {
            this.mColorFade.dismiss();
        }
        this.mColorFadePrepared = false;
        this.mColorFadeReady = true;
    }

    public void dismissColorFadeResources() {
        if (this.mColorFade != null) {
            this.mColorFade.dismissResources();
        }
    }

    public void setColorFadeLevel(float level) {
        if (this.mColorFadeLevel != level) {
            android.util.Slog.d(this.TAG, "setColorFadeLevel: level=" + level);
            this.mColorFadeLevel = level;
            if (this.mScreenState != 1) {
                this.mScreenReady = false;
                scheduleScreenUpdate();
            }
            if (this.mColorFadePrepared) {
                this.mColorFadeReady = false;
                scheduleColorFadeDraw();
            }
        }
    }

    public float getColorFadeLevel() {
        return this.mColorFadeLevel;
    }

    public boolean waitUntilClean(java.lang.Runnable listener) {
        if (!this.mScreenReady || !this.mColorFadeReady) {
            this.mCleanListener = listener;
            return false;
        }
        this.mCleanListener = null;
        return true;
    }

    public void stop() {
        this.mStopped = true;
        this.mPhotonicModulator.interrupt();
        this.mColorFadePrepared = false;
        this.mColorFadeReady = true;
        if (this.mColorFade != null) {
            java.util.concurrent.Executor executor = this.mAsyncDestroyExecutor;
            final com.android.server.display.ColorFade colorFade = this.mColorFade;
            java.util.Objects.requireNonNull(colorFade);
            executor.execute(new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerState$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    colorFade.destroy();
                }
            });
        }
        this.mCleanListener = null;
        this.mHandler.removeCallbacksAndMessages(null);
        this.mDpcExt.dismissEglContext(this.mColorFade, this.mDisplayId);
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println();
        pw.println("Display Power State:");
        pw.println("  mDisplayId=" + this.mDisplayId);
        pw.println("  mStopped=" + this.mStopped);
        pw.println("  mScreenState=" + android.view.Display.stateToString(this.mScreenState));
        pw.println("  mScreenBrightness=" + this.mScreenBrightness);
        pw.println("  mSdrScreenBrightness=" + this.mSdrScreenBrightness);
        pw.println("  mScreenReady=" + this.mScreenReady);
        pw.println("  mScreenUpdatePending=" + this.mScreenUpdatePending);
        pw.println("  mColorFadePrepared=" + this.mColorFadePrepared);
        pw.println("  mColorFadeLevel=" + this.mColorFadeLevel);
        pw.println("  mColorFadeReady=" + this.mColorFadeReady);
        pw.println("  mColorFadeDrawPending=" + this.mColorFadeDrawPending);
        this.mPhotonicModulator.dump(pw);
        if (this.mColorFade != null) {
            this.mColorFade.dump(pw);
        }
    }

    void resetScreenState() {
        this.mScreenState = 0;
        this.mScreenReady = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleScreenUpdate() {
        if (!this.mScreenUpdatePending) {
            this.mScreenUpdatePending = true;
            postScreenUpdateThreadSafe();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postScreenUpdateThreadSafe() {
        this.mHandler.removeCallbacks(this.mScreenUpdateRunnable);
        this.mHandler.post(this.mScreenUpdateRunnable);
    }

    private void scheduleColorFadeDraw() {
        if (!this.mColorFadeDrawPending) {
            this.mColorFadeDrawPending = true;
            this.mChoreographer.postCallback(3, this.mColorFadeDrawRunnable, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeCleanListenerIfNeeded() {
        java.lang.Runnable listener = this.mCleanListener;
        if (listener != null && this.mScreenReady && this.mColorFadeReady) {
            this.mCleanListener = null;
            listener.run();
        }
    }

    private final class PhotonicModulator extends java.lang.Thread {
        private static final float INITIAL_BACKLIGHT_FLOAT = Float.NaN;
        private static final int INITIAL_SCREEN_STATE = 0;
        private float mActualBacklight;
        private float mActualSdrBacklight;
        private int mActualState;
        private boolean mBacklightChangeInProgress;
        private final java.lang.Object mLock;
        private float mPendingBacklight;
        private float mPendingSdrBacklight;
        private int mPendingState;
        private boolean mStateChangeInProgress;

        public PhotonicModulator() {
            super("PhotonicModulator");
            this.mLock = new java.lang.Object();
            this.mPendingState = 0;
            this.mPendingBacklight = Float.NaN;
            this.mPendingSdrBacklight = Float.NaN;
            this.mActualState = 0;
            this.mActualBacklight = Float.NaN;
            this.mActualSdrBacklight = Float.NaN;
        }

        public boolean setState(int state, float brightnessState, float sdrBrightnessState) {
            boolean z;
            synchronized (this.mLock) {
                z = true;
                boolean stateChanged = state != this.mPendingState;
                boolean backlightChanged = (com.android.internal.display.BrightnessSynchronizer.floatEquals(brightnessState, this.mPendingBacklight) && com.android.internal.display.BrightnessSynchronizer.floatEquals(sdrBrightnessState, this.mPendingSdrBacklight)) ? false : true;
                if (stateChanged || backlightChanged) {
                    if (com.android.server.display.DisplayPowerState.DEBUG) {
                        android.util.Slog.d(com.android.server.display.DisplayPowerState.this.TAG, "Requesting new screen state: state=" + android.view.Display.stateToString(state) + ", backlight=" + brightnessState);
                    }
                    this.mPendingState = state;
                    this.mPendingBacklight = brightnessState;
                    this.mPendingSdrBacklight = sdrBrightnessState;
                    boolean changeInProgress = this.mStateChangeInProgress || this.mBacklightChangeInProgress;
                    this.mStateChangeInProgress = stateChanged || this.mStateChangeInProgress;
                    this.mBacklightChangeInProgress = backlightChanged || this.mBacklightChangeInProgress;
                    if (!changeInProgress) {
                        this.mLock.notifyAll();
                    }
                }
                boolean changeInProgress2 = this.mStateChangeInProgress;
                if (changeInProgress2) {
                    z = false;
                }
            }
            return z;
        }

        public void dump(java.io.PrintWriter pw) {
            synchronized (this.mLock) {
                pw.println();
                pw.println("Photonic Modulator State:");
                pw.println("  mDisplayId=" + com.android.server.display.DisplayPowerState.this.mDisplayId);
                pw.println("  mPendingState=" + android.view.Display.stateToString(this.mPendingState));
                pw.println("  mPendingBacklight=" + this.mPendingBacklight);
                pw.println("  mPendingSdrBacklight=" + this.mPendingSdrBacklight);
                pw.println("  mActualState=" + android.view.Display.stateToString(this.mActualState));
                pw.println("  mActualBacklight=" + this.mActualBacklight);
                pw.println("  mActualSdrBacklight=" + this.mActualSdrBacklight);
                pw.println("  mStateChangeInProgress=" + this.mStateChangeInProgress);
                pw.println("  mBacklightChangeInProgress=" + this.mBacklightChangeInProgress);
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            com.android.server.display.DisplayPowerState.this.mDpsWrapper.getExtImpl().setDisplayThreadSched(android.os.Process.myTid(), -10);
            com.android.server.display.DisplayPowerState.this.mDpsWrapper.getExtImpl().setUxThread();
            while (true) {
                synchronized (this.mLock) {
                    int state = this.mPendingState;
                    boolean changed = true;
                    boolean stateChanged = state != this.mActualState;
                    float brightnessState = this.mPendingBacklight;
                    float sdrBrightnessState = this.mPendingSdrBacklight;
                    boolean backlightChanged = (com.android.internal.display.BrightnessSynchronizer.floatEquals(brightnessState, this.mActualBacklight) && com.android.internal.display.BrightnessSynchronizer.floatEquals(sdrBrightnessState, this.mActualSdrBacklight)) ? false : true;
                    if (!stateChanged) {
                        com.android.server.display.DisplayPowerState.this.postScreenUpdateThreadSafe();
                        this.mStateChangeInProgress = false;
                    }
                    if (!backlightChanged) {
                        this.mBacklightChangeInProgress = false;
                    }
                    boolean valid = (state == 0 || java.lang.Float.isNaN(brightnessState)) ? false : true;
                    if (!stateChanged && !backlightChanged) {
                        changed = false;
                    }
                    if (!valid || !changed) {
                        this.mStateChangeInProgress = false;
                        this.mBacklightChangeInProgress = false;
                        try {
                            this.mLock.wait();
                        } catch (java.lang.InterruptedException e) {
                            if (com.android.server.display.DisplayPowerState.this.mStopped) {
                                return;
                            }
                        }
                    } else {
                        this.mActualState = state;
                        this.mActualBacklight = brightnessState;
                        this.mActualSdrBacklight = sdrBrightnessState;
                        if (com.android.server.display.DisplayPowerState.DEBUG || stateChanged) {
                            android.util.Slog.d(com.android.server.display.DisplayPowerState.this.TAG, "Updating screen state: id=" + com.android.server.display.DisplayPowerState.this.mDisplayId + ", state=" + android.view.Display.stateToString(state) + ", backlight=" + brightnessState + ", sdrBacklight=" + sdrBrightnessState);
                        }
                        com.android.server.display.DisplayPowerState.this.mBlanker.requestDisplayState(com.android.server.display.DisplayPowerState.this.mDisplayId, state, brightnessState, sdrBrightnessState);
                        if (com.android.server.display.DisplayPowerState.this.mDpcExt != null) {
                            com.android.server.display.DisplayPowerState.this.mDpcExt.notifyBrightnessChange(brightnessState);
                        }
                    }
                }
            }
        }
    }

    public com.android.server.display.IDisplayPowerStateWrapper getWrapper() {
        return this.mDpsWrapper;
    }

    private class DisplayPowerStateWrapper implements com.android.server.display.IDisplayPowerStateWrapper {
        private com.android.server.display.IDisplayPowerStateExt mDpsExt;

        private DisplayPowerStateWrapper() {
            this.mDpsExt = (com.android.server.display.IDisplayPowerStateExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IDisplayPowerStateExt.class).base(com.android.server.display.DisplayPowerState.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.display.IDisplayPowerStateExt getExtImpl() {
            return this.mDpsExt;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public boolean getDebug() {
            return com.android.server.display.DisplayPowerState.DEBUG;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public void setLoggingEnabled(boolean loggingEnabled) {
            com.android.server.display.DisplayPowerState.DEBUG = loggingEnabled;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public boolean getColorFadePrepared() {
            return com.android.server.display.DisplayPowerState.this.mColorFadePrepared;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public void setScreenReady(boolean ready) {
            com.android.server.display.DisplayPowerState.this.mScreenReady = ready;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public void scheduleScreenUpdate() {
            com.android.server.display.DisplayPowerState.this.scheduleScreenUpdate();
        }
    }
}
