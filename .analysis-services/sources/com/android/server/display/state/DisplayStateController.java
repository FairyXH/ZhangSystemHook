package com.android.server.display.state;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayStateController {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private com.android.server.display.DisplayPowerProximityStateController mDisplayPowerProximityStateController;
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private boolean mPerformScreenOffTransition = false;
    private int mDozeStateOverride = 0;
    private int mDozeStateOverrideReason = 0;

    public DisplayStateController(com.android.server.display.DisplayPowerProximityStateController displayPowerProximityStateController, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        this.mDpcExt = dpcExt;
        this.mDisplayPowerProximityStateController = displayPowerProximityStateController;
    }

    public android.util.Pair<java.lang.Integer, java.lang.Integer> updateDisplayState(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, boolean isDisplayEnabled, boolean isDisplayInTransition, int oldState, int displayId, boolean isSecondaryDisplayEnabled, java.lang.String uniqueDisplayId) {
        int state;
        this.mPerformScreenOffTransition = false;
        int reason = 1;
        switch (displayPowerRequest.policy) {
            case 0:
                state = 1;
                this.mPerformScreenOffTransition = true;
                break;
            case 1:
                int state2 = this.mDozeStateOverride;
                if (state2 != 0) {
                    state = this.mDozeStateOverride;
                    reason = this.mDozeStateOverrideReason;
                } else {
                    int state3 = displayPowerRequest.dozeScreenState;
                    if (state3 != 0) {
                        state = displayPowerRequest.dozeScreenState;
                        reason = displayPowerRequest.dozeScreenStateReason;
                    } else if (oldState != 0) {
                        state = oldState;
                    } else {
                        state = 3;
                    }
                }
                break;
            default:
                state = 2;
                break;
        }
        this.mDisplayPowerProximityStateController.updateProximityState(displayPowerRequest, state);
        if (!isDisplayEnabled || isDisplayInTransition || isSecondaryDisplayEnabled || this.mDpcExt.shouldIgnoreDoze(state) || (this.mDisplayPowerProximityStateController.isScreenOffBecauseOfProximity() && !this.mDpcExt.getUseProximityForceSuspendState(displayId))) {
            if (!isDisplayInTransition && ((!isDisplayEnabled || isSecondaryDisplayEnabled || this.mDpcExt.shouldIgnoreDoze(state)) && this.mDpcExt.isRemapDisplayDevice())) {
                this.mDpcExt.setIgnoreReadyState(true);
            }
            state = 1;
            android.util.Slog.i("DisplayStateController", "logicalDisplay enable=" + isDisplayEnabled + " transition=" + isDisplayInTransition + " isSecondaryDisplayEnabled=" + isSecondaryDisplayEnabled + " id=" + uniqueDisplayId);
        } else {
            if (oldState == 0 && state != 1 && displayPowerRequest.policy == 1 && this.mDpcExt.needScreenOffWhenDeviceStateClose()) {
                android.util.Slog.i("DisplayStateController", "screen off when devicestate closed id=" + uniqueDisplayId + ", state " + state);
                state = 1;
                this.mDpcExt.resetNeedScreenOffWhenDeviceStateClose();
            }
            this.mDpcExt.setIgnoreReadyState(false);
        }
        if (this.mDisplayPowerProximityStateController.isScreenOffBecauseOfProximity() && !this.mDpcExt.getUseProximityForceSuspendState(displayId)) {
            this.mDpcExt.cancelPwkBecauseProximity();
        }
        return new android.util.Pair<>(java.lang.Integer.valueOf(state), java.lang.Integer.valueOf(reason));
    }

    public void overrideDozeScreenState(int displayState, int reason) {
        this.mDozeStateOverride = displayState;
        this.mDozeStateOverrideReason = reason;
    }

    public boolean shouldPerformScreenOffTransition() {
        return this.mPerformScreenOffTransition;
    }

    public void dumpsys(java.io.PrintWriter pw) {
        pw.println();
        pw.println("DisplayStateController:");
        pw.println("  mPerformScreenOffTransition:" + this.mPerformScreenOffTransition);
        pw.println("  mDozeStateOverride=" + this.mDozeStateOverride);
        java.io.PrintWriter indentingPrintWriter = new android.util.IndentingPrintWriter(pw, " ");
        if (this.mDisplayPowerProximityStateController != null) {
            this.mDisplayPowerProximityStateController.dumpLocal(indentingPrintWriter);
        }
    }
}
