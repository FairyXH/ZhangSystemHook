package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public final class DisplayPowerProximityStateController {
    private static final boolean DEBUG_PRETEND_PROXIMITY_SENSOR_ABSENT = false;
    private static final int MSG_IGNORE_PROXIMITY = 2;
    static final int MSG_PROXIMITY_SENSOR_DEBOUNCED = 1;
    private static final int PROXIMITY_NEGATIVE = 0;
    static final int PROXIMITY_POSITIVE = 1;
    private static final int PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY = 0;
    static final int PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY = 0;
    static final int PROXIMITY_UNKNOWN = -1;
    private static final float TYPICAL_PROXIMITY_THRESHOLD = 5.0f;
    private com.android.server.display.DisplayPowerProximityStateController.Clock mClock;
    private com.android.server.display.DisplayDeviceConfig mDisplayDeviceConfig;
    private int mDisplayId;
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private final com.android.server.display.DisplayPowerProximityStateController.DisplayPowerProximityStateHandler mHandler;
    private boolean mIgnoreProximityUntilChanged;
    private final java.lang.Runnable mNudgeUpdatePowerState;
    private boolean mPendingWaitForNegativeProximityLocked;
    private android.hardware.Sensor mProximitySensor;
    private boolean mProximitySensorEnabled;
    private float mProximityThreshold;
    private boolean mScreenOffBecauseOfProximity;
    private final android.hardware.SensorManager mSensorManager;
    private final java.lang.String mTag;
    private boolean mWaitingForNegativeProximity;
    private final com.android.server.display.WakelockController mWakelockController;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.hardware.SensorEventListener mProximitySensorListener = new android.hardware.SensorEventListener() { // from class: com.android.server.display.DisplayPowerProximityStateController.1
        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            if (com.android.server.display.DisplayPowerProximityStateController.this.mProximitySensorEnabled) {
                long time = com.android.server.display.DisplayPowerProximityStateController.this.mClock.uptimeMillis();
                boolean positive = false;
                float distance = event.values[0];
                if (distance >= 0.0f && distance < com.android.server.display.DisplayPowerProximityStateController.this.mProximityThreshold && !com.android.server.display.DisplayPowerProximityStateController.this.mDpcExt.isIgnoreProximity()) {
                    positive = true;
                }
                com.android.server.display.DisplayPowerProximityStateController.this.handleProximitySensorEvent(time, positive);
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }
    };
    private int mPendingProximity = -1;
    private long mPendingProximityDebounceTime = -1;
    private int mProximity = -1;
    private boolean mSkipRampBecauseOfProximityChangeToNegative = false;
    private final com.android.server.display.IDisplayPowerProximityStateControllerWrapper mWrapper = new com.android.server.display.DisplayPowerProximityStateController.DisplayPowerProximityStateControllerWrapper();

    interface Clock {
        long uptimeMillis();
    }

    public DisplayPowerProximityStateController(com.android.server.display.WakelockController wakeLockController, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, android.os.Looper looper, java.lang.Runnable nudgeUpdatePowerState, int displayId, android.hardware.SensorManager sensorManager, com.android.server.display.DisplayPowerProximityStateController.Injector injector, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        this.mClock = (injector == null ? new com.android.server.display.DisplayPowerProximityStateController.Injector() : injector).createClock();
        this.mWakelockController = wakeLockController;
        this.mHandler = new com.android.server.display.DisplayPowerProximityStateController.DisplayPowerProximityStateHandler(looper);
        this.mNudgeUpdatePowerState = nudgeUpdatePowerState;
        this.mDisplayDeviceConfig = displayDeviceConfig;
        this.mDisplayId = displayId;
        this.mTag = "DisplayPowerProximityStateController[" + this.mDisplayId + "]";
        this.mSensorManager = sensorManager;
        this.mDpcExt = dpcExt;
        loadProximitySensor();
    }

    public void updatePendingProximityRequestsLocked() {
        synchronized (this.mLock) {
            this.mWaitingForNegativeProximity |= this.mPendingWaitForNegativeProximityLocked;
            this.mPendingWaitForNegativeProximityLocked = false;
            if (this.mIgnoreProximityUntilChanged) {
                this.mWaitingForNegativeProximity = false;
            }
        }
    }

    public void cleanup() {
        setProximitySensorEnabled(false);
    }

    public boolean isProximitySensorAvailable() {
        return this.mProximitySensor != null;
    }

    public boolean setPendingWaitForNegativeProximityLocked(boolean requestWaitForNegativeProximity) {
        synchronized (this.mLock) {
            if (requestWaitForNegativeProximity) {
                if (!this.mPendingWaitForNegativeProximityLocked) {
                    this.mPendingWaitForNegativeProximityLocked = true;
                    return true;
                }
            }
            return false;
        }
    }

    public void updateProximityState(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int displayState) {
        if (!this.mDpcExt.applyOplusProximitySensorLocked(displayPowerRequest, this.mProximity, this.mProximitySensorEnabled, this.mWaitingForNegativeProximity, this.mScreenOffBecauseOfProximity, displayState, this.mProximitySensor != null, this.mDisplayId)) {
            this.mSkipRampBecauseOfProximityChangeToNegative = false;
            if (this.mProximitySensor != null) {
                if (displayPowerRequest.useProximitySensor && displayState != 1) {
                    setProximitySensorEnabled(true);
                    if (!this.mScreenOffBecauseOfProximity && this.mProximity == 1 && !this.mIgnoreProximityUntilChanged) {
                        this.mScreenOffBecauseOfProximity = true;
                        sendOnProximityPositiveWithWakelock();
                    }
                } else if (this.mWaitingForNegativeProximity && this.mScreenOffBecauseOfProximity && this.mProximity == 1 && displayState != 1) {
                    setProximitySensorEnabled(true);
                } else {
                    setProximitySensorEnabled(false);
                    this.mWaitingForNegativeProximity = false;
                }
                if (this.mScreenOffBecauseOfProximity) {
                    if (this.mProximity != 1 || this.mIgnoreProximityUntilChanged) {
                        this.mScreenOffBecauseOfProximity = false;
                        this.mSkipRampBecauseOfProximityChangeToNegative = true;
                        sendOnProximityNegativeWithWakelock();
                        return;
                    }
                    return;
                }
                return;
            }
            setProximitySensorEnabled(false);
            this.mWaitingForNegativeProximity = false;
            this.mIgnoreProximityUntilChanged = false;
            if (this.mScreenOffBecauseOfProximity) {
                this.mScreenOffBecauseOfProximity = false;
                this.mSkipRampBecauseOfProximityChangeToNegative = true;
                sendOnProximityNegativeWithWakelock();
            }
        }
    }

    public boolean shouldSkipRampBecauseOfProximityChangeToNegative() {
        return this.mSkipRampBecauseOfProximityChangeToNegative;
    }

    public boolean isScreenOffBecauseOfProximity() {
        return this.mScreenOffBecauseOfProximity;
    }

    public void ignoreProximitySensorUntilChanged() {
        this.mHandler.sendEmptyMessage(2);
    }

    public void notifyDisplayDeviceChanged(com.android.server.display.DisplayDeviceConfig displayDeviceConfig) {
        this.mDisplayDeviceConfig = displayDeviceConfig;
        loadProximitySensor();
    }

    public void dumpLocal(java.io.PrintWriter pw) {
        pw.println();
        pw.println("DisplayPowerProximityStateController:");
        synchronized (this.mLock) {
            pw.println("  mPendingWaitForNegativeProximityLocked=" + this.mPendingWaitForNegativeProximityLocked);
        }
        pw.println("  mDisplayId=" + this.mDisplayId);
        pw.println("  mWaitingForNegativeProximity=" + this.mWaitingForNegativeProximity);
        pw.println("  mIgnoreProximityUntilChanged=" + this.mIgnoreProximityUntilChanged);
        pw.println("  mProximitySensor=" + this.mProximitySensor);
        pw.println("  mProximitySensorEnabled=" + this.mProximitySensorEnabled);
        pw.println("  mProximityThreshold=" + this.mProximityThreshold);
        pw.println("  mProximity=" + proximityToString(this.mProximity));
        pw.println("  mPendingProximity=" + proximityToString(this.mPendingProximity));
        pw.println("  mPendingProximityDebounceTime=" + android.util.TimeUtils.formatUptime(this.mPendingProximityDebounceTime));
        pw.println("  mScreenOffBecauseOfProximity=" + this.mScreenOffBecauseOfProximity);
        pw.println("  mSkipRampBecauseOfProximityChangeToNegative=" + this.mSkipRampBecauseOfProximityChangeToNegative);
    }

    void ignoreProximitySensorUntilChangedInternal() {
        if (!this.mIgnoreProximityUntilChanged && this.mProximity == 1) {
            this.mIgnoreProximityUntilChanged = true;
            android.util.Slog.i(this.mTag, "Ignoring proximity");
            this.mNudgeUpdatePowerState.run();
        }
    }

    private void sendOnProximityPositiveWithWakelock() {
        this.mWakelockController.acquireWakelock(1);
        this.mHandler.post(this.mWakelockController.getOnProximityPositiveRunnable());
    }

    private void sendOnProximityNegativeWithWakelock() {
        this.mWakelockController.acquireWakelock(2);
        this.mHandler.post(this.mWakelockController.getOnProximityNegativeRunnable());
    }

    private void loadProximitySensor() {
        if (this.mDisplayId != 0) {
            return;
        }
        this.mProximitySensor = com.android.server.display.utils.SensorUtils.findSensor(this.mSensorManager, this.mDisplayDeviceConfig.getProximitySensor(), 8);
        if (this.mProximitySensor != null) {
            this.mProximityThreshold = java.lang.Math.min(this.mProximitySensor.getMaximumRange(), TYPICAL_PROXIMITY_THRESHOLD);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProximitySensorEnabled(boolean enable) {
        if (enable) {
            if (!this.mProximitySensorEnabled) {
                this.mProximitySensorEnabled = true;
                this.mIgnoreProximityUntilChanged = false;
                if (!this.mDpcExt.registerPSensor(this.mSensorManager, this.mProximitySensorListener, 3, this.mHandler, this.mProximitySensor)) {
                    this.mSensorManager.registerListener(this.mProximitySensorListener, this.mProximitySensor, 3, this.mHandler);
                    return;
                }
                return;
            }
            return;
        }
        if (this.mProximitySensorEnabled) {
            this.mProximitySensorEnabled = false;
            this.mProximity = -1;
            this.mIgnoreProximityUntilChanged = false;
            this.mPendingProximity = -1;
            this.mHandler.removeMessages(1);
            this.mSensorManager.unregisterListener(this.mProximitySensorListener);
            boolean proxDebounceSuspendBlockerReleased = this.mWakelockController.releaseWakelock(3);
            if (proxDebounceSuspendBlockerReleased) {
                this.mPendingProximityDebounceTime = -1L;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleProximitySensorEvent(long time, boolean positive) {
        if (this.mProximitySensorEnabled) {
            if (this.mPendingProximity == 0 && !positive) {
                return;
            }
            if (this.mPendingProximity == 1 && positive) {
                return;
            }
            this.mHandler.removeMessages(1);
            if (positive) {
                if (this.mDpcExt.interceptProximityEvent()) {
                    return;
                }
                this.mPendingProximity = 1;
                this.mPendingProximityDebounceTime = 0 + time;
                this.mWakelockController.acquireWakelock(3);
            } else {
                this.mPendingProximity = 0;
                this.mPendingProximityDebounceTime = 0 + time;
                this.mWakelockController.acquireWakelock(3);
            }
            debounceProximitySensor();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void debounceProximitySensor() {
        if (this.mProximitySensorEnabled && this.mPendingProximity != -1 && this.mPendingProximityDebounceTime >= 0) {
            long now = this.mClock.uptimeMillis();
            if (this.mPendingProximityDebounceTime <= now) {
                if (this.mProximity != this.mPendingProximity) {
                    this.mIgnoreProximityUntilChanged = false;
                    android.util.Slog.i(this.mTag, "No longer ignoring proximity [" + this.mPendingProximity + "]");
                }
                this.mProximity = this.mPendingProximity;
                this.mDpcExt.onProximityDebounceTimeArrived(this.mDisplayId, this.mProximity);
                this.mNudgeUpdatePowerState.run();
                boolean proxDebounceSuspendBlockerReleased = this.mWakelockController.releaseWakelock(3);
                if (proxDebounceSuspendBlockerReleased) {
                    this.mPendingProximityDebounceTime = -1L;
                    return;
                }
                return;
            }
            android.os.Message msg = this.mHandler.obtainMessage(1);
            this.mHandler.sendMessageAtTime(msg, this.mPendingProximityDebounceTime);
        }
    }

    private class DisplayPowerProximityStateHandler extends android.os.Handler {
        DisplayPowerProximityStateHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.display.DisplayPowerProximityStateController.this.debounceProximitySensor();
                    break;
                case 2:
                    com.android.server.display.DisplayPowerProximityStateController.this.ignoreProximitySensorUntilChangedInternal();
                    break;
            }
        }
    }

    private java.lang.String proximityToString(int state) {
        switch (state) {
            case -1:
                return "Unknown";
            case 0:
                return "Negative";
            case 1:
                return "Positive";
            default:
                return java.lang.Integer.toString(state);
        }
    }

    boolean getPendingWaitForNegativeProximityLocked() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPendingWaitForNegativeProximityLocked;
        }
        return z;
    }

    boolean getWaitingForNegativeProximity() {
        return this.mWaitingForNegativeProximity;
    }

    boolean shouldIgnoreProximityUntilChanged() {
        return this.mIgnoreProximityUntilChanged;
    }

    boolean isProximitySensorEnabled() {
        return this.mProximitySensorEnabled;
    }

    android.os.Handler getHandler() {
        return this.mHandler;
    }

    int getPendingProximity() {
        return this.mPendingProximity;
    }

    int getProximity() {
        return this.mProximity;
    }

    long getPendingProximityDebounceTime() {
        return this.mPendingProximityDebounceTime;
    }

    android.hardware.SensorEventListener getProximitySensorListener() {
        return this.mProximitySensorListener;
    }

    static class Injector {
        Injector() {
        }

        com.android.server.display.DisplayPowerProximityStateController.Clock createClock() {
            return new com.android.server.display.DisplayPowerProximityStateController.Clock() { // from class: com.android.server.display.DisplayPowerProximityStateController$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.display.DisplayPowerProximityStateController.Clock
                public final long uptimeMillis() {
                    return android.os.SystemClock.uptimeMillis();
                }
            };
        }
    }

    public com.android.server.display.IDisplayPowerProximityStateControllerWrapper getWrapper() {
        return this.mWrapper;
    }

    private class DisplayPowerProximityStateControllerWrapper implements com.android.server.display.IDisplayPowerProximityStateControllerWrapper {
        private DisplayPowerProximityStateControllerWrapper() {
        }

        @Override // com.android.server.display.IDisplayPowerProximityStateControllerWrapper
        public void setProximitySensorEnabled(boolean enable) {
            com.android.server.display.DisplayPowerProximityStateController.this.setProximitySensorEnabled(enable);
        }

        @Override // com.android.server.display.IDisplayPowerProximityStateControllerWrapper
        public void setScreenOffBecauseOfProximity(boolean val) {
            com.android.server.display.DisplayPowerProximityStateController.this.mScreenOffBecauseOfProximity = val;
        }

        @Override // com.android.server.display.IDisplayPowerProximityStateControllerWrapper
        public void setWaitingForNegativeProximity(boolean val) {
            com.android.server.display.DisplayPowerProximityStateController.this.mWaitingForNegativeProximity = val;
        }

        @Override // com.android.server.display.IDisplayPowerProximityStateControllerWrapper
        public void handleCoverModeProximitySensorEvent(long time, boolean positive) {
            com.android.server.display.DisplayPowerProximityStateController.this.handleProximitySensorEvent(time, positive);
        }

        @Override // com.android.server.display.IDisplayPowerProximityStateControllerWrapper
        public com.android.server.display.WakelockController getWakelockController() {
            return com.android.server.display.DisplayPowerProximityStateController.this.mWakelockController;
        }
    }
}
