package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class InattentiveSleepWarningController {
    private static final java.lang.String TAG = "InattentiveSleepWarning";
    private final android.os.Handler mHandler = new android.os.Handler();
    private boolean mIsShown;
    private com.android.internal.statusbar.IStatusBarService mStatusBarService;

    InattentiveSleepWarningController() {
    }

    public boolean isShown() {
        return this.mIsShown;
    }

    public void show() {
        if (isShown()) {
            return;
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.InattentiveSleepWarningController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.showInternal();
            }
        });
        this.mIsShown = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showInternal() {
        try {
            getStatusBar().showInattentiveSleepWarning();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Failed to show inattentive sleep warning", e);
            this.mIsShown = false;
        }
    }

    public void dismiss(final boolean animated) {
        if (!isShown()) {
            return;
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.InattentiveSleepWarningController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dismiss$0(animated);
            }
        });
        this.mIsShown = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: dismissInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$dismiss$0(boolean animated) {
        try {
            getStatusBar().dismissInattentiveSleepWarning(animated);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Failed to dismiss inattentive sleep warning", e);
        }
    }

    private com.android.internal.statusbar.IStatusBarService getStatusBar() {
        if (this.mStatusBarService == null) {
            this.mStatusBarService = com.android.internal.statusbar.IStatusBarService.Stub.asInterface(android.os.ServiceManager.getService("statusbar"));
        }
        return this.mStatusBarService;
    }
}
