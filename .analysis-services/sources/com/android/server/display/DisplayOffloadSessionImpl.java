package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayOffloadSessionImpl implements android.hardware.display.DisplayManagerInternal.DisplayOffloadSession {
    private final android.hardware.display.DisplayManagerInternal.DisplayOffloader mDisplayOffloader;
    private final com.android.server.display.DisplayPowerControllerInterface mDisplayPowerController;
    private boolean mIsActive;
    private static final java.lang.String TAG = "DisplayOffloadSessionImpl";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    public DisplayOffloadSessionImpl(android.hardware.display.DisplayManagerInternal.DisplayOffloader displayOffloader, com.android.server.display.DisplayPowerControllerInterface displayPowerController) {
        this.mDisplayOffloader = displayOffloader;
        this.mDisplayPowerController = displayPowerController;
    }

    public void setDozeStateOverride(int displayState) {
        this.mDisplayPowerController.overrideDozeScreenState(displayState, 3);
    }

    public boolean isActive() {
        return this.mIsActive;
    }

    public boolean allowAutoBrightnessInDoze() {
        if (this.mDisplayOffloader == null) {
            return false;
        }
        return this.mDisplayOffloader.allowAutoBrightnessInDoze();
    }

    public void updateBrightness(float brightness) {
        if (this.mIsActive) {
            this.mDisplayPowerController.setBrightnessFromOffload(brightness);
        }
    }

    public boolean blockScreenOn(java.lang.Runnable unblocker) {
        if (this.mDisplayOffloader == null) {
            return false;
        }
        this.mDisplayOffloader.onBlockingScreenOn(unblocker);
        return true;
    }

    public float[] getAutoBrightnessLevels(int mode) {
        if (mode < 0 || mode > 2) {
            throw new java.lang.IllegalArgumentException("Unknown auto-brightness mode: " + mode);
        }
        return this.mDisplayPowerController.getAutoBrightnessLevels(mode);
    }

    public float[] getAutoBrightnessLuxLevels(int mode) {
        if (mode < 0 || mode > 2) {
            throw new java.lang.IllegalArgumentException("Unknown auto-brightness mode: " + mode);
        }
        return this.mDisplayPowerController.getAutoBrightnessLuxLevels(mode);
    }

    public boolean startOffload() {
        if (this.mDisplayOffloader == null || this.mIsActive) {
            return false;
        }
        android.os.Trace.traceBegin(131072L, "DisplayOffloader#startOffload");
        try {
            this.mIsActive = this.mDisplayOffloader.startOffload();
            if (DEBUG) {
                android.util.Slog.d(TAG, "startOffload = " + this.mIsActive);
            }
            return this.mIsActive;
        } finally {
            android.os.Trace.traceEnd(131072L);
        }
    }

    public void stopOffload() {
        if (this.mDisplayOffloader == null || !this.mIsActive) {
            return;
        }
        android.os.Trace.traceBegin(131072L, "DisplayOffloader#stopOffload");
        try {
            this.mDisplayOffloader.stopOffload();
            this.mIsActive = false;
            if (DEBUG) {
                android.util.Slog.i(TAG, "stopOffload");
            }
        } finally {
            android.os.Trace.traceEnd(131072L);
        }
    }

    public float getBrightness() {
        return this.mDisplayPowerController.getScreenBrightnessSetting();
    }

    public float getDozeBrightness() {
        return this.mDisplayPowerController.getDozeBrightnessForOffload();
    }
}
