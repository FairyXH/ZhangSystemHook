package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public final class VibratorControllerHolder implements android.os.IBinder.DeathRecipient {
    private static final java.lang.String TAG = "VibratorControllerHolder";
    private android.frameworks.vibrator.IVibratorController mVibratorController;

    public android.frameworks.vibrator.IVibratorController getVibratorController() {
        return this.mVibratorController;
    }

    public void setVibratorController(android.frameworks.vibrator.IVibratorController controller) {
        try {
            if (this.mVibratorController != null) {
                this.mVibratorController.asBinder().unlinkToDeath(this, 0);
            }
            this.mVibratorController = controller;
            if (this.mVibratorController != null) {
                this.mVibratorController.asBinder().linkToDeath(this, 0);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.wtf(TAG, "Failed to set IVibratorController: " + this, e);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(android.os.IBinder deadBinder) {
        if (this.mVibratorController != null && deadBinder == this.mVibratorController.asBinder()) {
            setVibratorController(null);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        android.util.Slog.wtf(TAG, "binderDied() called unexpectedly.");
    }
}
