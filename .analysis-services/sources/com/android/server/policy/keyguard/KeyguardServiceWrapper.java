package com.android.server.policy.keyguard;

/* JADX INFO: loaded from: classes3.dex */
public class KeyguardServiceWrapper implements com.android.internal.policy.IKeyguardService {
    private java.lang.String TAG = "KeyguardServiceWrapper";
    private com.android.server.policy.keyguard.KeyguardStateMonitor mKeyguardStateMonitor;
    private com.android.internal.policy.IKeyguardService mService;

    public KeyguardServiceWrapper(android.content.Context context, com.android.internal.policy.IKeyguardService service, com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback callback) {
        this.mService = service;
        this.mKeyguardStateMonitor = new com.android.server.policy.keyguard.KeyguardStateMonitor(context, service, callback);
    }

    public void verifyUnlock(com.android.internal.policy.IKeyguardExitCallback callback) {
        try {
            this.mService.verifyUnlock(callback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void setOccluded(boolean isOccluded, boolean animate) {
        try {
            this.mService.setOccluded(isOccluded, animate);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void setExitKeyguardForNearbyUnlock(long tokenHandle, byte[] token) {
        try {
            if (this.mService != null) {
                this.mService.setExitKeyguardForNearbyUnlock(tokenHandle, token);
            } else {
                android.util.Slog.w(this.TAG, "mService == null");
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void addStateMonitorCallback(com.android.internal.policy.IKeyguardStateCallback callback) {
        try {
            this.mService.addStateMonitorCallback(callback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void dismiss(com.android.internal.policy.IKeyguardDismissCallback callback, java.lang.CharSequence message) {
        try {
            this.mService.dismiss(callback, message);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onDreamingStarted() {
        try {
            this.mService.onDreamingStarted();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onDreamingStopped() {
        try {
            this.mService.onDreamingStopped();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onStartedGoingToSleep(int pmSleepReason) {
        try {
            this.mService.onStartedGoingToSleep(pmSleepReason);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onFinishedGoingToSleep(int pmSleepReason, boolean cameraGestureTriggered) {
        try {
            this.mService.onFinishedGoingToSleep(pmSleepReason, cameraGestureTriggered);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onStartedWakingUp(int pmWakeReason, boolean cameraGestureTriggered) {
        try {
            this.mService.onStartedWakingUp(pmWakeReason, cameraGestureTriggered);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onFinishedWakingUp() {
        try {
            this.mService.onFinishedWakingUp();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onScreenTurningOn(com.android.internal.policy.IKeyguardDrawnCallback callback) {
        try {
            this.mService.onScreenTurningOn(callback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onScreenTurnedOn() {
        try {
            this.mService.onScreenTurnedOn();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onScreenTurningOff() {
        try {
            this.mService.onScreenTurningOff();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onScreenTurnedOff() {
        try {
            this.mService.onScreenTurnedOff();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void setKeyguardEnabled(boolean enabled) {
        try {
            this.mService.setKeyguardEnabled(enabled);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onSystemReady() {
        try {
            this.mService.onSystemReady();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void doKeyguardTimeout(android.os.Bundle options) {
        int userId = this.mKeyguardStateMonitor.getCurrentUser();
        if (this.mKeyguardStateMonitor.isSecure(userId)) {
            this.mKeyguardStateMonitor.onShowingStateChanged(true, userId);
        }
        try {
            this.mService.doKeyguardTimeout(options);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void showDismissibleKeyguard() {
        try {
            this.mService.showDismissibleKeyguard();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void setSwitchingUser(boolean switching) {
        try {
            this.mService.setSwitchingUser(switching);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void setCurrentUser(int userId) {
        this.mKeyguardStateMonitor.setCurrentUser(userId);
        try {
            this.mService.setCurrentUser(userId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onBootCompleted() {
        try {
            this.mService.onBootCompleted();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void startKeyguardExitAnimation(long startTime, long fadeoutDuration) {
        try {
            this.mService.startKeyguardExitAnimation(startTime, fadeoutDuration);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onShortPowerPressedGoHome() {
        try {
            this.mService.onShortPowerPressedGoHome();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void dismissKeyguardToLaunch(android.content.Intent intentToLaunch) {
        try {
            this.mService.dismissKeyguardToLaunch(intentToLaunch);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onSystemKeyPressed(int keycode) {
        try {
            this.mService.onSystemKeyPressed(keycode);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public android.os.IBinder asBinder() {
        return this.mService.asBinder();
    }

    public boolean isShowing() {
        if (this.mKeyguardStateMonitor == null) {
            android.util.Slog.w(this.TAG, "mKeyguardStateMonitor null return");
            return false;
        }
        return this.mKeyguardStateMonitor.isShowing();
    }

    public boolean isTrusted() {
        return this.mKeyguardStateMonitor.isTrusted();
    }

    public boolean isSecure(int userId) {
        return this.mKeyguardStateMonitor.isSecure(userId);
    }

    public boolean isInputRestricted() {
        return this.mKeyguardStateMonitor.isInputRestricted();
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        this.mKeyguardStateMonitor.dump(prefix, pw);
    }

    public void requestKeyguard(java.lang.String command) {
        try {
            if (this.mService != null) {
                this.mService.requestKeyguard(command);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(this.TAG, "Remote Exception", e);
        }
    }
}
