package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
class OverlayControl {
    private final android.app.AppOpsManager mAppOpsManager;
    private final android.os.IBinder mToken = new android.os.Binder();

    OverlayControl(android.content.Context context) {
        this.mAppOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
    }

    void hideOverlays() {
        setOverlayAllowed(false);
    }

    void showOverlays() {
        setOverlayAllowed(true);
    }

    private void setOverlayAllowed(boolean allowed) {
        if (this.mAppOpsManager != null) {
            this.mAppOpsManager.setUserRestrictionForUser(24, !allowed, this.mToken, null, -1);
            this.mAppOpsManager.setUserRestrictionForUser(45, !allowed, this.mToken, null, -1);
        }
    }
}
