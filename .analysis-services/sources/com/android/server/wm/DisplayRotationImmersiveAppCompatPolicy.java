package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class DisplayRotationImmersiveAppCompatPolicy {
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private final com.android.server.wm.DisplayRotation mDisplayRotation;
    private final com.android.server.wm.LetterboxConfiguration mLetterboxConfiguration;

    static com.android.server.wm.DisplayRotationImmersiveAppCompatPolicy createIfNeeded(com.android.server.wm.LetterboxConfiguration letterboxConfiguration, com.android.server.wm.DisplayRotation displayRotation, com.android.server.wm.DisplayContent displayContent) {
        if (!letterboxConfiguration.isDisplayRotationImmersiveAppCompatPolicyEnabledAtBuildTime()) {
            return null;
        }
        return new com.android.server.wm.DisplayRotationImmersiveAppCompatPolicy(letterboxConfiguration, displayRotation, displayContent);
    }

    private DisplayRotationImmersiveAppCompatPolicy(com.android.server.wm.LetterboxConfiguration letterboxConfiguration, com.android.server.wm.DisplayRotation displayRotation, com.android.server.wm.DisplayContent displayContent) {
        this.mDisplayRotation = displayRotation;
        this.mLetterboxConfiguration = letterboxConfiguration;
        this.mDisplayContent = displayContent;
    }

    boolean isRotationLockEnforced(int proposedRotation) {
        boolean zIsRotationLockEnforcedLocked;
        if (!this.mLetterboxConfiguration.isDisplayRotationImmersiveAppCompatPolicyEnabled()) {
            return false;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mDisplayContent.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                zIsRotationLockEnforcedLocked = isRotationLockEnforcedLocked(proposedRotation);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return zIsRotationLockEnforcedLocked;
    }

    private boolean isRotationLockEnforcedLocked(int proposedRotation) {
        com.android.server.wm.ActivityRecord activityRecord;
        return (!this.mDisplayContent.getIgnoreOrientationRequest() || (activityRecord = this.mDisplayContent.topRunningActivity()) == null || !hasRequestedToHideStatusAndNavBars(activityRecord) || activityRecord.getTask() == null || activityRecord.getTask().getWindowingMode() != 1 || activityRecord.areBoundsLetterboxed() || activityRecord.getRequestedConfigurationOrientation() == 0 || activityRecord.getRequestedConfigurationOrientation() == surfaceRotationToConfigurationOrientation(proposedRotation)) ? false : true;
    }

    private boolean hasRequestedToHideStatusAndNavBars(com.android.server.wm.ActivityRecord activity) {
        com.android.server.wm.WindowState mainWindow = activity.findMainWindow();
        return mainWindow != null && (mainWindow.getRequestedVisibleTypes() & (android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars())) == 0;
    }

    private int surfaceRotationToConfigurationOrientation(int rotation) {
        if (this.mDisplayRotation.isAnyPortrait(rotation)) {
            return 1;
        }
        if (this.mDisplayRotation.isLandscapeOrSeascape(rotation)) {
            return 2;
        }
        return 0;
    }
}
