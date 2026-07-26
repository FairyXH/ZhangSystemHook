package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SplashScreenStartingData extends com.android.server.wm.StartingData {
    private final int mTheme;

    SplashScreenStartingData(com.android.server.wm.WindowManagerService service, int theme, int typeParams) {
        super(service, typeParams);
        this.mTheme = theme;
    }

    @Override // com.android.server.wm.StartingData
    com.android.server.wm.StartingSurfaceController.StartingSurface createStartingSurface(com.android.server.wm.ActivityRecord activity) {
        return this.mService.mStartingSurfaceController.createSplashScreenStartingSurface(activity, this.mTheme);
    }

    @Override // com.android.server.wm.StartingData
    boolean needRevealAnimation() {
        return true;
    }
}
