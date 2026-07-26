package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ImmediateDisplayUpdater implements com.android.server.wm.DisplayUpdater {
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private final android.view.DisplayInfo mDisplayInfo = new android.view.DisplayInfo();

    public ImmediateDisplayUpdater(com.android.server.wm.DisplayContent displayContent) {
        this.mDisplayContent = displayContent;
        this.mDisplayInfo.copyFrom(this.mDisplayContent.getDisplayInfo());
    }

    @Override // com.android.server.wm.DisplayUpdater
    public void updateDisplayInfo(java.lang.Runnable callback) {
        this.mDisplayContent.mWmService.mDisplayManagerInternal.getNonOverrideDisplayInfo(this.mDisplayContent.mDisplayId, this.mDisplayInfo);
        this.mDisplayContent.onDisplayInfoUpdated(this.mDisplayInfo);
        callback.run();
    }

    @Override // com.android.server.wm.DisplayUpdater
    public void onDisplayContentDisplayPropertiesPreChanged(int displayId, int initialDisplayWidth, int initialDisplayHeight, int newWidth, int newHeight) {
        this.mDisplayContent.mDisplaySwitchTransitionLauncher.requestDisplaySwitchTransitionIfNeeded(displayId, initialDisplayWidth, initialDisplayHeight, newWidth, newHeight);
    }

    @Override // com.android.server.wm.DisplayUpdater
    public void onDisplayContentDisplayPropertiesPostChanged(int previousRotation, int newRotation, android.window.DisplayAreaInfo newDisplayAreaInfo) {
        this.mDisplayContent.mDisplaySwitchTransitionLauncher.onDisplayUpdated(previousRotation, newRotation, newDisplayAreaInfo);
    }
}
