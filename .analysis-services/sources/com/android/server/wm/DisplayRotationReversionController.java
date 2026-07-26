package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class DisplayRotationReversionController {
    private static final int NUM_SLOTS = 3;
    static final int REVERSION_TYPE_CAMERA_COMPAT = 1;
    static final int REVERSION_TYPE_HALF_FOLD = 2;
    static final int REVERSION_TYPE_NOSENSOR = 0;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private int mUserRotationOverridden = -1;
    private final boolean[] mSlots = new boolean[3];

    DisplayRotationReversionController(com.android.server.wm.DisplayContent content) {
        this.mDisplayContent = content;
    }

    boolean isRotationReversionEnabled() {
        return (this.mDisplayContent.mDisplayRotationCompatPolicy == null && this.mDisplayContent.getDisplayRotation().mFoldController == null && !this.mDisplayContent.getIgnoreOrientationRequest()) ? false : true;
    }

    void beforeOverrideApplied(int slotIndex) {
        if (this.mSlots[slotIndex]) {
            return;
        }
        maybeSaveUserRotation();
        this.mSlots[slotIndex] = true;
    }

    boolean isOverrideActive(int slotIndex) {
        return this.mSlots[slotIndex];
    }

    boolean[] getSlotsCopy() {
        if (isRotationReversionEnabled()) {
            return (boolean[]) this.mSlots.clone();
        }
        return null;
    }

    void updateForNoSensorOverride() {
        if (!this.mSlots[0]) {
            if (isTopFullscreenActivityNoSensor()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -6949326633913532620L, 0, null, null);
                }
                beforeOverrideApplied(0);
                return;
            }
            return;
        }
        if (!isTopFullscreenActivityNoSensor()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -2060428960792625366L, 0, null, null);
            }
            revertOverride(0);
        }
    }

    boolean isAnyOverrideActive() {
        for (int i = 0; i < 3; i++) {
            if (this.mSlots[i]) {
                return true;
            }
        }
        return false;
    }

    boolean revertOverride(int slotIndex) {
        if (!this.mSlots[slotIndex]) {
            return false;
        }
        this.mSlots[slotIndex] = false;
        if (isAnyOverrideActive()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -4296736202875980050L, 0, null, null);
            }
            return false;
        }
        com.android.server.wm.DisplayRotation displayRotation = this.mDisplayContent.getDisplayRotation();
        if (this.mUserRotationOverridden == -1 || displayRotation.getUserRotationMode() != 1) {
            return false;
        }
        displayRotation.setUserRotation(1, this.mUserRotationOverridden, "DisplayRotationReversionController#revertOverride");
        this.mUserRotationOverridden = -1;
        return true;
    }

    private void maybeSaveUserRotation() {
        com.android.server.wm.DisplayRotation displayRotation = this.mDisplayContent.getDisplayRotation();
        if (!isAnyOverrideActive() && displayRotation.getUserRotationMode() == 1) {
            this.mUserRotationOverridden = displayRotation.getUserRotation();
        }
    }

    private boolean isTopFullscreenActivityNoSensor() {
        com.android.server.wm.ActivityRecord topActivity;
        com.android.server.wm.Task topFullscreenTask = this.mDisplayContent.getTask(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayRotationReversionController$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.DisplayRotationReversionController.lambda$isTopFullscreenActivityNoSensor$0((com.android.server.wm.Task) obj);
            }
        });
        return (topFullscreenTask == null || (topActivity = topFullscreenTask.topRunningActivity()) == null || topActivity.getOrientation() != 5) ? false : true;
    }

    static /* synthetic */ boolean lambda$isTopFullscreenActivityNoSensor$0(com.android.server.wm.Task t) {
        return t.getWindowingMode() == 1;
    }
}
