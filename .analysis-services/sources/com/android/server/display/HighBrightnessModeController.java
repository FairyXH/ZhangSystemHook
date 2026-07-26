package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class HighBrightnessModeController {
    private static final float DEFAULT_MAX_DESIRED_HDR_SDR_RATIO = 1.0f;
    static final float HBM_TRANSITION_POINT_INVALID = Float.POSITIVE_INFINITY;
    private float mAmbientLux;
    private float mBrightness;
    private final float mBrightnessMax;
    private final float mBrightnessMin;
    private final com.android.server.display.DisplayManagerService.Clock mClock;
    private final android.content.Context mContext;
    private int mDisplayStatsId;
    private boolean mForceHbmChangeCallback;
    private final android.os.Handler mHandler;
    private final java.lang.Runnable mHbmChangeCallback;
    private com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData mHbmData;
    private int mHbmMode;
    private int mHbmStatsState;
    private com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig mHdrBrightnessCfg;
    private com.android.server.display.HighBrightnessModeController.HdrListener mHdrListener;
    private int mHeight;
    private com.android.server.display.HighBrightnessModeMetadata mHighBrightnessModeMetadata;
    private final com.android.server.display.HighBrightnessModeController.Injector mInjector;
    private boolean mIsAutoBrightnessEnabled;
    private boolean mIsAutoBrightnessOffByState;
    private boolean mIsBlockedByLowPowerMode;
    private boolean mIsHdrLayerPresent;
    private boolean mIsInAllowedAmbientRange;
    private boolean mIsTimeAvailable;
    private float mMaxDesiredHdrSdrRatio;
    private final java.lang.Runnable mRecalcRunnable;
    private android.os.IBinder mRegisteredDisplayToken;
    private final com.android.server.display.HighBrightnessModeController.SettingsObserver mSettingsObserver;
    private int mThrottlingReason;
    private float mUnthrottledBrightness;
    private int mWidth;
    private static final java.lang.String TAG = "HighBrightnessModeController";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    public interface HdrBrightnessDeviceConfig {
        float getHdrBrightnessFromSdr(float f, float f2);
    }

    HighBrightnessModeController(android.os.Handler handler, int width, int height, android.os.IBinder displayToken, java.lang.String displayUniqueId, float brightnessMin, float brightnessMax, com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData hbmData, com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig hdrBrightnessCfg, java.lang.Runnable hbmChangeCallback, com.android.server.display.HighBrightnessModeMetadata hbmMetadata, android.content.Context context) {
        this(new com.android.server.display.HighBrightnessModeController.Injector(), handler, width, height, displayToken, displayUniqueId, brightnessMin, brightnessMax, hbmData, hdrBrightnessCfg, hbmChangeCallback, hbmMetadata, context);
    }

    HighBrightnessModeController(com.android.server.display.HighBrightnessModeController.Injector injector, android.os.Handler handler, int width, int height, android.os.IBinder displayToken, java.lang.String displayUniqueId, float brightnessMin, float brightnessMax, com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData hbmData, com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig hdrBrightnessCfg, java.lang.Runnable hbmChangeCallback, com.android.server.display.HighBrightnessModeMetadata hbmMetadata, android.content.Context context) {
        this.mIsInAllowedAmbientRange = false;
        this.mIsTimeAvailable = false;
        this.mIsAutoBrightnessEnabled = false;
        this.mIsAutoBrightnessOffByState = false;
        this.mThrottlingReason = 0;
        this.mHbmMode = 0;
        this.mIsHdrLayerPresent = false;
        this.mMaxDesiredHdrSdrRatio = 1.0f;
        this.mForceHbmChangeCallback = false;
        this.mIsBlockedByLowPowerMode = false;
        this.mHbmStatsState = 1;
        this.mInjector = injector;
        this.mContext = context;
        this.mClock = injector.getClock();
        this.mHandler = handler;
        this.mBrightness = brightnessMin;
        this.mBrightnessMin = brightnessMin;
        this.mBrightnessMax = brightnessMax;
        this.mHbmChangeCallback = hbmChangeCallback;
        this.mHighBrightnessModeMetadata = hbmMetadata;
        this.mSettingsObserver = new com.android.server.display.HighBrightnessModeController.SettingsObserver(this.mHandler);
        this.mRecalcRunnable = new java.lang.Runnable() { // from class: com.android.server.display.HighBrightnessModeController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.recalculateTimeAllowance();
            }
        };
        this.mHdrListener = new com.android.server.display.HighBrightnessModeController.HdrListener();
        resetHbmData(width, height, displayToken, displayUniqueId, hbmData, hdrBrightnessCfg);
    }

    void setAutoBrightnessEnabled(int state) {
        boolean isEnabled = state == 1;
        this.mIsAutoBrightnessOffByState = state == 3;
        if (!deviceSupportsHbm() || isEnabled == this.mIsAutoBrightnessEnabled) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "setAutoBrightnessEnabled( " + isEnabled + " )");
        }
        this.mIsAutoBrightnessEnabled = isEnabled;
        this.mIsInAllowedAmbientRange = false;
        recalculateTimeAllowance();
    }

    float getCurrentBrightnessMin() {
        return this.mBrightnessMin;
    }

    float getCurrentBrightnessMax() {
        if (!deviceSupportsHbm() || isHbmCurrentlyAllowed()) {
            return this.mBrightnessMax;
        }
        return this.mHbmData.transitionPoint;
    }

    float getNormalBrightnessMax() {
        return deviceSupportsHbm() ? this.mHbmData.transitionPoint : this.mBrightnessMax;
    }

    float getHdrBrightnessValue() {
        if (this.mHdrBrightnessCfg != null) {
            float hdrBrightness = this.mHdrBrightnessCfg.getHdrBrightnessFromSdr(this.mBrightness, this.mMaxDesiredHdrSdrRatio);
            if (hdrBrightness != -1.0f) {
                return hdrBrightness;
            }
        }
        return android.util.MathUtils.map(getCurrentBrightnessMin(), getCurrentBrightnessMax(), this.mBrightnessMin, this.mBrightnessMax, this.mBrightness);
    }

    void onAmbientLuxChange(float ambientLux) {
        this.mAmbientLux = ambientLux;
        if (!deviceSupportsHbm() || !this.mIsAutoBrightnessEnabled) {
            return;
        }
        boolean isHighLux = ambientLux >= this.mHbmData.minimumLux;
        if (isHighLux != this.mIsInAllowedAmbientRange) {
            this.mIsInAllowedAmbientRange = isHighLux;
            recalculateTimeAllowance();
        }
    }

    void onBrightnessChanged(float brightness, float unthrottledBrightness, int throttlingReason) {
        if (!deviceSupportsHbm()) {
            return;
        }
        this.mBrightness = brightness;
        this.mUnthrottledBrightness = unthrottledBrightness;
        this.mThrottlingReason = throttlingReason;
        long runningStartTime = this.mHighBrightnessModeMetadata.getRunningStartTimeMillis();
        boolean wasHbmDrainingAvailableTime = runningStartTime != -1;
        boolean shouldHbmDrainAvailableTime = this.mBrightness > this.mHbmData.transitionPoint && !this.mIsHdrLayerPresent;
        if (wasHbmDrainingAvailableTime != shouldHbmDrainAvailableTime) {
            long currentTime = this.mClock.uptimeMillis();
            if (shouldHbmDrainAvailableTime) {
                this.mHighBrightnessModeMetadata.setRunningStartTimeMillis(currentTime);
            } else {
                com.android.server.display.HbmEvent hbmEvent = new com.android.server.display.HbmEvent(runningStartTime, currentTime);
                this.mHighBrightnessModeMetadata.addHbmEvent(hbmEvent);
                this.mHighBrightnessModeMetadata.setRunningStartTimeMillis(-1L);
                if (DEBUG) {
                    android.util.Slog.d(TAG, "New HBM event: " + this.mHighBrightnessModeMetadata.getHbmEventQueue().peekFirst());
                }
            }
        }
        recalculateTimeAllowance();
    }

    int getHighBrightnessMode() {
        return this.mHbmMode;
    }

    float getTransitionPoint() {
        if (deviceSupportsHbm()) {
            return this.mHbmData.transitionPoint;
        }
        return Float.POSITIVE_INFINITY;
    }

    void stop() {
        registerHdrListener(null);
        this.mSettingsObserver.stopObserving();
    }

    void setHighBrightnessModeMetadata(com.android.server.display.HighBrightnessModeMetadata hbmInfo) {
        this.mHighBrightnessModeMetadata = hbmInfo;
    }

    void resetHbmData(int width, int height, android.os.IBinder displayToken, java.lang.String displayUniqueId, com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData hbmData, com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig hdrBrightnessCfg) {
        this.mWidth = width;
        this.mHeight = height;
        this.mHbmData = hbmData;
        this.mHdrBrightnessCfg = hdrBrightnessCfg;
        this.mDisplayStatsId = displayUniqueId.hashCode();
        unregisterHdrListener();
        this.mSettingsObserver.stopObserving();
        if (deviceSupportsHbm()) {
            registerHdrListener(displayToken);
            recalculateTimeAllowance();
            if (!this.mHbmData.allowInLowPowerMode) {
                this.mIsBlockedByLowPowerMode = false;
                this.mSettingsObserver.startObserving();
            }
        }
    }

    void dump(final java.io.PrintWriter pw) {
        this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.display.HighBrightnessModeController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dump$0(pw);
            }
        }, 1000L);
    }

    com.android.server.display.HighBrightnessModeController.HdrListener getHdrListener() {
        return this.mHdrListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: dumpLocal, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$0(java.io.PrintWriter pw) {
        pw.println("HighBrightnessModeController:");
        pw.println("  mBrightness=" + this.mBrightness);
        pw.println("  mUnthrottledBrightness=" + this.mUnthrottledBrightness);
        pw.println("  mThrottlingReason=" + android.hardware.display.BrightnessInfo.briMaxReasonToString(this.mThrottlingReason));
        pw.println("  mCurrentMin=" + getCurrentBrightnessMin());
        pw.println("  mCurrentMax=" + getCurrentBrightnessMax());
        pw.println("  mHbmMode=" + android.hardware.display.BrightnessInfo.hbmToString(this.mHbmMode) + (this.mHbmMode == 2 ? "(" + getHdrBrightnessValue() + ")" : ""));
        pw.println("  mHbmStatsState=" + hbmStatsStateToString(this.mHbmStatsState));
        pw.println("  mHbmData=" + this.mHbmData);
        pw.println("  mAmbientLux=" + this.mAmbientLux + (this.mIsAutoBrightnessEnabled ? "" : " (old/invalid)"));
        pw.println("  mIsInAllowedAmbientRange=" + this.mIsInAllowedAmbientRange);
        pw.println("  mIsAutoBrightnessEnabled=" + this.mIsAutoBrightnessEnabled);
        pw.println("  mIsAutoBrightnessOffByState=" + this.mIsAutoBrightnessOffByState);
        pw.println("  mIsHdrLayerPresent=" + this.mIsHdrLayerPresent);
        pw.println("  mBrightnessMin=" + this.mBrightnessMin);
        pw.println("  mBrightnessMax=" + this.mBrightnessMax);
        pw.println("  remainingTime=" + calculateRemainingTime(this.mClock.uptimeMillis()));
        pw.println("  mIsTimeAvailable= " + this.mIsTimeAvailable);
        pw.println("  mIsBlockedByLowPowerMode=" + this.mIsBlockedByLowPowerMode);
        pw.println("  width*height=" + this.mWidth + com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER + this.mHeight);
        if (this.mHighBrightnessModeMetadata != null) {
            pw.println("  mRunningStartTimeMillis=" + android.util.TimeUtils.formatUptime(this.mHighBrightnessModeMetadata.getRunningStartTimeMillis()));
            pw.println("  mEvents=");
            long currentTime = this.mClock.uptimeMillis();
            long lastStartTime = currentTime;
            long runningStartTimeMillis = this.mHighBrightnessModeMetadata.getRunningStartTimeMillis();
            if (runningStartTimeMillis != -1) {
                lastStartTime = dumpHbmEvent(pw, new com.android.server.display.HbmEvent(runningStartTimeMillis, currentTime));
            }
            for (com.android.server.display.HbmEvent event : this.mHighBrightnessModeMetadata.getHbmEventQueue()) {
                if (lastStartTime > event.getEndTimeMillis()) {
                    pw.println("    event: [normal brightness]: " + android.util.TimeUtils.formatDuration(lastStartTime - event.getEndTimeMillis()));
                }
                lastStartTime = dumpHbmEvent(pw, event);
            }
            return;
        }
        pw.println("  mHighBrightnessModeMetadata=null");
    }

    private long dumpHbmEvent(java.io.PrintWriter pw, com.android.server.display.HbmEvent event) {
        long duration = event.getEndTimeMillis() - event.getStartTimeMillis();
        pw.println("    event: [" + android.util.TimeUtils.formatUptime(event.getStartTimeMillis()) + ", " + android.util.TimeUtils.formatUptime(event.getEndTimeMillis()) + "] (" + android.util.TimeUtils.formatDuration(duration) + ")");
        return event.getStartTimeMillis();
    }

    boolean isHbmCurrentlyAllowed() {
        return !this.mIsHdrLayerPresent && this.mIsAutoBrightnessEnabled && this.mIsTimeAvailable && this.mIsInAllowedAmbientRange && !this.mIsBlockedByLowPowerMode;
    }

    boolean deviceSupportsHbm() {
        return (this.mHbmData == null || this.mHighBrightnessModeMetadata == null) ? false : true;
    }

    private long calculateRemainingTime(long currentTime) {
        if (!deviceSupportsHbm()) {
            return 0L;
        }
        long timeAlreadyUsed = 0;
        long runningStartTimeMillis = this.mHighBrightnessModeMetadata.getRunningStartTimeMillis();
        if (runningStartTimeMillis > 0) {
            if (runningStartTimeMillis > currentTime) {
                android.util.Slog.e(TAG, "Start time set to the future. curr: " + currentTime + ", start: " + runningStartTimeMillis);
                this.mHighBrightnessModeMetadata.setRunningStartTimeMillis(currentTime);
                runningStartTimeMillis = currentTime;
            }
            timeAlreadyUsed = currentTime - runningStartTimeMillis;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Time already used after current session: " + timeAlreadyUsed);
        }
        long windowstartTimeMillis = currentTime - this.mHbmData.timeWindowMillis;
        java.util.Iterator<com.android.server.display.HbmEvent> it = this.mHighBrightnessModeMetadata.getHbmEventQueue().iterator();
        while (it.hasNext()) {
            com.android.server.display.HbmEvent event = it.next();
            if (event.getEndTimeMillis() < windowstartTimeMillis) {
                it.remove();
            } else {
                long startTimeMillis = java.lang.Math.max(event.getStartTimeMillis(), windowstartTimeMillis);
                timeAlreadyUsed += event.getEndTimeMillis() - startTimeMillis;
            }
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Time already used after all sessions: " + timeAlreadyUsed);
        }
        return java.lang.Math.max(0L, this.mHbmData.timeMaxMillis - timeAlreadyUsed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recalculateTimeAllowance() {
        long nextTimeout;
        long currentTime = this.mClock.uptimeMillis();
        long remainingTime = calculateRemainingTime(currentTime);
        boolean z = true;
        boolean isAllowedWithoutRestrictions = remainingTime >= this.mHbmData.timeMinMillis;
        boolean isOnlyAllowedToStayOn = !isAllowedWithoutRestrictions && remainingTime > 0 && this.mBrightness > this.mHbmData.transitionPoint;
        if (!isAllowedWithoutRestrictions && !isOnlyAllowedToStayOn) {
            z = false;
        }
        this.mIsTimeAvailable = z;
        java.util.ArrayDeque<com.android.server.display.HbmEvent> hbmEvents = this.mHighBrightnessModeMetadata.getHbmEventQueue();
        if (this.mBrightness > this.mHbmData.transitionPoint) {
            nextTimeout = currentTime + remainingTime;
        } else if (!this.mIsTimeAvailable && hbmEvents.size() > 0) {
            long windowstartTimeMillis = currentTime - this.mHbmData.timeWindowMillis;
            com.android.server.display.HbmEvent lastEvent = hbmEvents.peekLast();
            long jMax = java.lang.Math.max(windowstartTimeMillis, lastEvent.getStartTimeMillis());
            long nextTimeout2 = this.mHbmData.timeMinMillis;
            long startTimePlusMinMillis = jMax + nextTimeout2;
            nextTimeout = ((startTimePlusMinMillis - windowstartTimeMillis) + currentTime) - remainingTime;
        } else {
            nextTimeout = -1;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "HBM recalculated.  IsAllowedWithoutRestrictions: " + isAllowedWithoutRestrictions + ", isOnlyAllowedToStayOn: " + isOnlyAllowedToStayOn + ", remainingAllowedTime: " + remainingTime + ", isLuxHigh: " + this.mIsInAllowedAmbientRange + ", isHBMCurrentlyAllowed: " + isHbmCurrentlyAllowed() + ", isHdrLayerPresent: " + this.mIsHdrLayerPresent + ", mMaxDesiredHdrSdrRatio: " + this.mMaxDesiredHdrSdrRatio + ", isAutoBrightnessEnabled: " + this.mIsAutoBrightnessEnabled + ", mIsTimeAvailable: " + this.mIsTimeAvailable + ", mIsInAllowedAmbientRange: " + this.mIsInAllowedAmbientRange + ", mIsBlockedByLowPowerMode: " + this.mIsBlockedByLowPowerMode + ", mBrightness: " + this.mBrightness + ", mUnthrottledBrightness: " + this.mUnthrottledBrightness + ", mThrottlingReason: " + android.hardware.display.BrightnessInfo.briMaxReasonToString(this.mThrottlingReason) + ", RunningStartTimeMillis: " + this.mHighBrightnessModeMetadata.getRunningStartTimeMillis() + ", nextTimeout: " + (nextTimeout != -1 ? nextTimeout - currentTime : -1L) + ", events: " + hbmEvents);
        }
        if (nextTimeout != -1) {
            this.mHandler.removeCallbacks(this.mRecalcRunnable);
            this.mHandler.postAtTime(this.mRecalcRunnable, 1 + nextTimeout);
        }
        updateHbmMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHbmMode() {
        int newHbmMode = calculateHighBrightnessMode();
        updateHbmStats(newHbmMode);
        if (this.mHbmMode != newHbmMode || this.mForceHbmChangeCallback) {
            this.mForceHbmChangeCallback = false;
            this.mHbmMode = newHbmMode;
            this.mHbmChangeCallback.run();
        }
    }

    private void updateHbmStats(int newMode) {
        int state = 1;
        if (newMode == 2 && getHdrBrightnessValue() > this.mHbmData.transitionPoint) {
            state = 2;
        } else if (newMode == 1 && this.mBrightness > this.mHbmData.transitionPoint) {
            state = 3;
        }
        if (state == this.mHbmStatsState) {
            return;
        }
        int reason = 0;
        boolean oldHbmSv = this.mHbmStatsState == 3;
        boolean newHbmSv = state == 3;
        if (oldHbmSv && !newHbmSv) {
            if (!this.mIsAutoBrightnessEnabled && this.mIsAutoBrightnessOffByState) {
                reason = 6;
            } else if (!this.mIsAutoBrightnessEnabled) {
                reason = 7;
            } else if (!this.mIsInAllowedAmbientRange) {
                reason = 1;
            } else if (!this.mIsTimeAvailable) {
                reason = 2;
            } else if (isThermalThrottlingActive()) {
                reason = 3;
            } else if (this.mIsHdrLayerPresent) {
                reason = 4;
            } else if (this.mIsBlockedByLowPowerMode) {
                reason = 5;
            } else if (this.mBrightness <= this.mHbmData.transitionPoint) {
                reason = 9;
            }
        }
        this.mInjector.reportHbmStateChange(this.mDisplayStatsId, state, reason);
        this.mHbmStatsState = state;
    }

    boolean isThermalThrottlingActive() {
        return this.mUnthrottledBrightness > this.mHbmData.transitionPoint && this.mBrightness <= this.mHbmData.transitionPoint && this.mThrottlingReason == 1;
    }

    private java.lang.String hbmStatsStateToString(int hbmStatsState) {
        switch (hbmStatsState) {
            case 1:
                return "HBM_OFF";
            case 2:
                return "HBM_ON_HDR";
            case 3:
                return "HBM_ON_SUNLIGHT";
            default:
                return java.lang.String.valueOf(hbmStatsState);
        }
    }

    private int calculateHighBrightnessMode() {
        if (!deviceSupportsHbm()) {
            return 0;
        }
        if (this.mIsHdrLayerPresent) {
            return 2;
        }
        return isHbmCurrentlyAllowed() ? 1 : 0;
    }

    private void registerHdrListener(android.os.IBinder displayToken) {
        if (this.mRegisteredDisplayToken == displayToken) {
            return;
        }
        unregisterHdrListener();
        this.mRegisteredDisplayToken = displayToken;
        if (this.mRegisteredDisplayToken != null) {
            this.mHdrListener.register(this.mRegisteredDisplayToken);
        }
    }

    private void unregisterHdrListener() {
        if (this.mRegisteredDisplayToken != null) {
            this.mHdrListener.unregister(this.mRegisteredDisplayToken);
            this.mIsHdrLayerPresent = false;
        }
    }

    class HdrListener extends android.view.SurfaceControlHdrLayerInfoListener {
        HdrListener() {
        }

        public void onHdrInfoChanged(android.os.IBinder displayToken, final int numberOfHdrLayers, final int maxW, final int maxH, int flags, final float maxDesiredHdrSdrRatio) {
            com.android.server.display.HighBrightnessModeController.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.HighBrightnessModeController$HdrListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onHdrInfoChanged$0(numberOfHdrLayers, maxW, maxH, maxDesiredHdrSdrRatio);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHdrInfoChanged$0(int numberOfHdrLayers, int maxW, int maxH, float maxDesiredHdrSdrRatio) {
            float candidateDesiredHdrSdrRatio;
            android.os.Trace.traceBegin(131072L, "HBMController#onHdrInfoChanged");
            com.android.server.display.HighBrightnessModeController.this.mIsHdrLayerPresent = numberOfHdrLayers > 0 && ((float) (maxW * maxH)) >= ((float) (com.android.server.display.HighBrightnessModeController.this.mWidth * com.android.server.display.HighBrightnessModeController.this.mHeight)) * com.android.server.display.HighBrightnessModeController.this.mHbmData.minimumHdrPercentOfScreen;
            if (com.android.server.display.HighBrightnessModeController.this.mIsHdrLayerPresent && com.android.server.display.HighBrightnessModeController.this.mHdrBrightnessCfg != null) {
                candidateDesiredHdrSdrRatio = maxDesiredHdrSdrRatio;
            } else {
                candidateDesiredHdrSdrRatio = 1.0f;
            }
            if (candidateDesiredHdrSdrRatio < 1.0f) {
                android.util.Slog.w(com.android.server.display.HighBrightnessModeController.TAG, "Ignoring invalid desired HDR/SDR Ratio: " + candidateDesiredHdrSdrRatio);
                candidateDesiredHdrSdrRatio = 1.0f;
            }
            if (!com.android.internal.display.BrightnessSynchronizer.floatEquals(com.android.server.display.HighBrightnessModeController.this.mMaxDesiredHdrSdrRatio, candidateDesiredHdrSdrRatio)) {
                com.android.server.display.HighBrightnessModeController.this.mForceHbmChangeCallback = true;
                com.android.server.display.HighBrightnessModeController.this.mMaxDesiredHdrSdrRatio = candidateDesiredHdrSdrRatio;
            }
            com.android.server.display.HighBrightnessModeController.this.onBrightnessChanged(com.android.server.display.HighBrightnessModeController.this.mBrightness, com.android.server.display.HighBrightnessModeController.this.mUnthrottledBrightness, com.android.server.display.HighBrightnessModeController.this.mThrottlingReason);
            android.os.Trace.traceEnd(131072L);
        }
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        private final android.net.Uri mLowPowerModeSetting;
        private boolean mStarted;

        SettingsObserver(android.os.Handler handler) {
            super(handler);
            this.mLowPowerModeSetting = android.provider.Settings.Global.getUriFor("low_power");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            updateLowPower();
        }

        void startObserving() {
            if (!this.mStarted) {
                com.android.server.display.HighBrightnessModeController.this.mContext.getContentResolver().registerContentObserver(this.mLowPowerModeSetting, false, this, -1);
                this.mStarted = true;
                updateLowPower();
            }
        }

        void stopObserving() {
            com.android.server.display.HighBrightnessModeController.this.mIsBlockedByLowPowerMode = false;
            if (this.mStarted) {
                com.android.server.display.HighBrightnessModeController.this.mContext.getContentResolver().unregisterContentObserver(this);
                this.mStarted = false;
            }
        }

        private void updateLowPower() {
            boolean isLowPowerMode = isLowPowerMode();
            if (isLowPowerMode == com.android.server.display.HighBrightnessModeController.this.mIsBlockedByLowPowerMode) {
                return;
            }
            if (com.android.server.display.HighBrightnessModeController.DEBUG) {
                android.util.Slog.d(com.android.server.display.HighBrightnessModeController.TAG, "Settings.Global.LOW_POWER_MODE enabled: " + isLowPowerMode);
            }
            com.android.server.display.HighBrightnessModeController.this.mIsBlockedByLowPowerMode = isLowPowerMode;
            com.android.server.display.HighBrightnessModeController.this.updateHbmMode();
        }

        private boolean isLowPowerMode() {
            return android.provider.Settings.Global.getInt(com.android.server.display.HighBrightnessModeController.this.mContext.getContentResolver(), "low_power", 0) != 0;
        }
    }

    public static class Injector {
        public com.android.server.display.DisplayManagerService.Clock getClock() {
            return new com.android.server.display.DisplayManagerService.Clock() { // from class: com.android.server.display.HighBrightnessModeController$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.display.DisplayManagerService.Clock
                public final long uptimeMillis() {
                    return android.os.SystemClock.uptimeMillis();
                }
            };
        }

        public void reportHbmStateChange(int display, int state, int reason) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DISPLAY_HBM_STATE_CHANGED, display, state, reason);
        }
    }
}
