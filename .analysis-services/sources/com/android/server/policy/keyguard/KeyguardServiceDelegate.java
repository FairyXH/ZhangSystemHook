package com.android.server.policy.keyguard;

/* JADX INFO: loaded from: classes3.dex */
public class KeyguardServiceDelegate {
    private static final boolean DEBUG = false;
    private static final int INTERACTIVE_STATE_AWAKE = 2;
    private static final int INTERACTIVE_STATE_GOING_TO_SLEEP = 3;
    private static final int INTERACTIVE_STATE_SLEEP = 0;
    private static final int INTERACTIVE_STATE_WAKING = 1;
    private static final java.lang.String REQUEST_COMMAND_ON_SYSTEM_REBOOTED = "system.rebooted";
    private static final int SCREEN_STATE_OFF = 0;
    private static final int SCREEN_STATE_ON = 2;
    private static final int SCREEN_STATE_TURNING_OFF = 3;
    private static final int SCREEN_STATE_TURNING_ON = 1;
    private static final java.lang.String TAG = "KeyguardServiceDelegate";
    private final com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback mCallback;
    private final android.content.Context mContext;
    private com.android.server.policy.keyguard.KeyguardServiceDelegate.DrawnListener mDrawnListenerWhenConnect;
    protected com.android.server.policy.keyguard.KeyguardServiceWrapper mKeyguardService;
    private final com.android.server.policy.keyguard.KeyguardServiceDelegate.KeyguardState mKeyguardState = new com.android.server.policy.keyguard.KeyguardServiceDelegate.KeyguardState();
    private final android.service.dreams.DreamManagerInternal.DreamManagerStateListener mDreamManagerStateListener = new android.service.dreams.DreamManagerInternal.DreamManagerStateListener() { // from class: com.android.server.policy.keyguard.KeyguardServiceDelegate.1
        public void onDreamingStarted() {
            com.android.server.policy.keyguard.KeyguardServiceDelegate.this.onDreamingStarted();
        }

        public void onDreamingStopped() {
            com.android.server.policy.keyguard.KeyguardServiceDelegate.this.onDreamingStopped();
        }
    };
    private final android.content.ServiceConnection mKeyguardConnection = new com.android.server.policy.keyguard.KeyguardServiceDelegate.AnonymousClass2();
    private final android.os.Handler mHandler = com.android.server.UiThread.getHandler();
    private boolean mIsSystemRebooted = true;

    public interface DrawnListener {
        void onDrawn();
    }

    private static final class KeyguardState {
        public boolean bootCompleted;
        public int currentUser;
        boolean deviceHasKeyguard;
        boolean dreaming;
        public boolean enabled;
        boolean inputRestricted;
        public int interactiveState;
        volatile boolean occluded;
        public int offReason;
        public int screenState;
        boolean secure;
        boolean showing;
        boolean systemIsReady;

        KeyguardState() {
            reset();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reset() {
            this.showing = true;
            this.occluded = false;
            this.secure = true;
            this.deviceHasKeyguard = true;
            this.enabled = true;
            this.currentUser = -10000;
        }
    }

    private final class KeyguardShowDelegate extends com.android.internal.policy.IKeyguardDrawnCallback.Stub {
        private com.android.server.policy.keyguard.KeyguardServiceDelegate.DrawnListener mDrawnListener;

        KeyguardShowDelegate(com.android.server.policy.keyguard.KeyguardServiceDelegate.DrawnListener drawnListener) {
            this.mDrawnListener = drawnListener;
        }

        public void onDrawn() throws android.os.RemoteException {
            ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Keyguard onDrawn");
            if (this.mDrawnListener != null) {
                this.mDrawnListener.onDrawn();
            }
        }
    }

    public void setKeyguardExitUnlock(long tokenHandle, byte[] token) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.setExitKeyguardForNearbyUnlock(tokenHandle, token);
        } else {
            android.util.Log.i(TAG, "mKeyguardService == null");
        }
    }

    private final class KeyguardExitDelegate extends com.android.internal.policy.IKeyguardExitCallback.Stub {
        private com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult mOnKeyguardExitResult;

        KeyguardExitDelegate(com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult onKeyguardExitResult) {
            this.mOnKeyguardExitResult = onKeyguardExitResult;
        }

        public void onKeyguardExitResult(boolean success) throws android.os.RemoteException {
            if (this.mOnKeyguardExitResult != null) {
                this.mOnKeyguardExitResult.onKeyguardExitResult(success);
            }
        }
    }

    public KeyguardServiceDelegate(android.content.Context context, com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public void bindService(android.content.Context context) {
        android.content.Intent intent = new android.content.Intent();
        android.content.res.Resources resources = context.getApplicationContext().getResources();
        android.content.ComponentName keyguardComponent = android.content.ComponentName.unflattenFromString(resources.getString(android.R.string.config_mms_user_agent));
        intent.addFlags(256);
        intent.setComponent(keyguardComponent);
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Keyguard bindService");
        if (!context.bindServiceAsUser(intent, this.mKeyguardConnection, 1, this.mHandler, android.os.UserHandle.SYSTEM)) {
            android.util.Log.v(TAG, "*** Keyguard: can't bind to " + keyguardComponent);
            this.mKeyguardState.showing = false;
            this.mKeyguardState.secure = false;
            synchronized (this.mKeyguardState) {
                this.mKeyguardState.deviceHasKeyguard = false;
            }
        } else {
            ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Keyguard started");
        }
        android.service.dreams.DreamManagerInternal dreamManager = (android.service.dreams.DreamManagerInternal) com.android.server.LocalServices.getService(android.service.dreams.DreamManagerInternal.class);
        dreamManager.registerDreamManagerStateListener(this.mDreamManagerStateListener);
    }

    /* JADX INFO: renamed from: com.android.server.policy.keyguard.KeyguardServiceDelegate$2, reason: invalid class name */
    class AnonymousClass2 implements android.content.ServiceConnection {
        AnonymousClass2() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            android.util.Log.v(com.android.server.policy.keyguard.KeyguardServiceDelegate.TAG, "*** Keyguard connected (yay!)");
            ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Keyguard connected");
            com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService = new com.android.server.policy.keyguard.KeyguardServiceWrapper(com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mContext, com.android.internal.policy.IKeyguardService.Stub.asInterface(service), com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mCallback);
            if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.systemIsReady) {
                if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mIsSystemRebooted) {
                    com.android.server.policy.keyguard.KeyguardServiceDelegate.this.requestKeyguard(com.android.server.policy.keyguard.KeyguardServiceDelegate.REQUEST_COMMAND_ON_SYSTEM_REBOOTED);
                    com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mIsSystemRebooted = false;
                }
                com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.onSystemReady();
                if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.currentUser != -10000) {
                    com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.setCurrentUser(com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.currentUser);
                }
                if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.interactiveState == 2 || com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.interactiveState == 1) {
                    com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.onStartedWakingUp(0, false);
                }
                if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.interactiveState == 2) {
                    com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.onFinishedWakingUp();
                }
                if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.screenState == 2 || com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.screenState == 1) {
                    com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.onScreenTurningOn(com.android.server.policy.keyguard.KeyguardServiceDelegate.this.new KeyguardShowDelegate(com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mDrawnListenerWhenConnect));
                }
                if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.screenState == 2) {
                    com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.onScreenTurnedOn();
                }
                com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mDrawnListenerWhenConnect = null;
            }
            if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.bootCompleted) {
                com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.onBootCompleted();
            }
            if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.occluded) {
                com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.setOccluded(com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.occluded, false);
            }
            if (!com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.enabled) {
                com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.setKeyguardEnabled(com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.enabled);
            }
            if (com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.dreaming) {
                com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService.onDreamingStarted();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            android.util.Log.v(com.android.server.policy.keyguard.KeyguardServiceDelegate.TAG, "*** Keyguard disconnected (boo!)");
            com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardService = null;
            com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mKeyguardState.reset();
            com.android.server.policy.keyguard.KeyguardServiceDelegate.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.policy.keyguard.KeyguardServiceDelegate$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    android.app.ActivityTaskManager.getService().setLockScreenShown(true, false);
                }
            });
        }
    }

    public boolean isShowing() {
        if (this.mKeyguardService != null) {
            this.mKeyguardState.showing = this.mKeyguardService.isShowing();
        }
        return this.mKeyguardState.showing;
    }

    public boolean isTrusted() {
        if (this.mKeyguardService != null) {
            return this.mKeyguardService.isTrusted();
        }
        return false;
    }

    public boolean hasKeyguard() {
        return this.mKeyguardState.deviceHasKeyguard;
    }

    public boolean isInputRestricted() {
        if (this.mKeyguardService != null) {
            this.mKeyguardState.inputRestricted = this.mKeyguardService.isInputRestricted();
        }
        return this.mKeyguardState.inputRestricted;
    }

    public void verifyUnlock(com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult onKeyguardExitResult) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.verifyUnlock(new com.android.server.policy.keyguard.KeyguardServiceDelegate.KeyguardExitDelegate(onKeyguardExitResult));
        }
    }

    public void setOccluded(boolean z, boolean z2) {
        if (this.mKeyguardService != null && z2) {
            com.android.server.wm.EventLogTags.writeWmSetKeyguardOccluded(z ? 1 : 0, 0, 0, "setOccluded");
            this.mKeyguardService.setOccluded(z, false);
        }
        this.mKeyguardState.occluded = z;
    }

    public boolean isOccluded() {
        return this.mKeyguardState.occluded;
    }

    public void dismiss(com.android.internal.policy.IKeyguardDismissCallback callback, java.lang.CharSequence message) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.dismiss(callback, message);
        }
    }

    public boolean isSecure(int userId) {
        if (this.mKeyguardService != null) {
            this.mKeyguardState.secure = this.mKeyguardService.isSecure(userId);
        }
        return this.mKeyguardState.secure;
    }

    public void onDreamingStarted() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onDreamingStarted();
        }
        this.mKeyguardState.dreaming = true;
    }

    public void onDreamingStopped() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onDreamingStopped();
        }
        this.mKeyguardState.dreaming = false;
    }

    public void onStartedWakingUp(int pmWakeReason, boolean cameraGestureTriggered) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onStartedWakingUp(pmWakeReason, cameraGestureTriggered);
        }
        this.mKeyguardState.interactiveState = 1;
    }

    public void onFinishedWakingUp() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onFinishedWakingUp();
        }
        this.mKeyguardState.interactiveState = 2;
    }

    public void onScreenTurningOff() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onScreenTurningOff();
        }
        this.mKeyguardState.screenState = 3;
    }

    public void onScreenTurnedOff() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onScreenTurnedOff();
        }
        this.mKeyguardState.screenState = 0;
    }

    public void onScreenTurningOn(com.android.server.policy.keyguard.KeyguardServiceDelegate.DrawnListener drawnListener) {
        if (this.mKeyguardService != null) {
            android.util.Log.v(TAG, "onScreenTurnedOn(showListener = " + drawnListener + ")");
            this.mKeyguardService.onScreenTurningOn(new com.android.server.policy.keyguard.KeyguardServiceDelegate.KeyguardShowDelegate(drawnListener));
        } else {
            android.util.Slog.w(TAG, "onScreenTurningOn(): no keyguard service!");
            this.mDrawnListenerWhenConnect = drawnListener;
        }
        this.mKeyguardState.screenState = 1;
    }

    public void onScreenTurnedOn() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onScreenTurnedOn();
        }
        this.mKeyguardState.screenState = 2;
    }

    public void onStartedGoingToSleep(int pmSleepReason) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onStartedGoingToSleep(pmSleepReason);
        }
        this.mKeyguardState.offReason = android.view.WindowManagerPolicyConstants.translateSleepReasonToOffReason(pmSleepReason);
        this.mKeyguardState.interactiveState = 3;
    }

    public void onFinishedGoingToSleep(int pmSleepReason, boolean cameraGestureTriggered) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onFinishedGoingToSleep(pmSleepReason, cameraGestureTriggered);
        }
        this.mKeyguardState.interactiveState = 0;
    }

    public void setKeyguardEnabled(boolean enabled) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.setKeyguardEnabled(enabled);
        }
        this.mKeyguardState.enabled = enabled;
    }

    public void onSystemReady() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onSystemReady();
        } else {
            this.mKeyguardState.systemIsReady = true;
        }
    }

    public void doKeyguardTimeout(android.os.Bundle options) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.doKeyguardTimeout(options);
        }
    }

    public void showDismissibleKeyguard() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.showDismissibleKeyguard();
        }
    }

    public void setCurrentUser(int newUserId) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.setCurrentUser(newUserId);
        }
        this.mKeyguardState.currentUser = newUserId;
    }

    public void setSwitchingUser(boolean switching) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.setSwitchingUser(switching);
        }
    }

    public void startKeyguardExitAnimation(long startTime) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.startKeyguardExitAnimation(startTime, 0L);
        }
    }

    public void onBootCompleted() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onBootCompleted();
        }
        this.mKeyguardState.bootCompleted = true;
    }

    public void onShortPowerPressedGoHome() {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onShortPowerPressedGoHome();
        }
    }

    public void dismissKeyguardToLaunch(android.content.Intent intentToLaunch) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.dismissKeyguardToLaunch(intentToLaunch);
        }
    }

    public void onSystemKeyPressed(int keycode) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.onSystemKeyPressed(keycode);
        }
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1133871366145L, this.mKeyguardState.showing);
        proto.write(1133871366146L, this.mKeyguardState.occluded);
        proto.write(1133871366147L, this.mKeyguardState.secure);
        proto.write(1159641169924L, this.mKeyguardState.screenState);
        proto.write(1159641169925L, this.mKeyguardState.interactiveState);
        proto.end(token);
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + TAG);
        java.lang.String prefix2 = prefix + "  ";
        pw.println(prefix2 + "showing=" + this.mKeyguardState.showing);
        pw.println(prefix2 + "inputRestricted=" + this.mKeyguardState.inputRestricted);
        pw.println(prefix2 + "occluded=" + this.mKeyguardState.occluded);
        pw.println(prefix2 + "secure=" + this.mKeyguardState.secure);
        pw.println(prefix2 + "dreaming=" + this.mKeyguardState.dreaming);
        pw.println(prefix2 + "systemIsReady=" + this.mKeyguardState.systemIsReady);
        pw.println(prefix2 + "deviceHasKeyguard=" + this.mKeyguardState.deviceHasKeyguard);
        pw.println(prefix2 + "enabled=" + this.mKeyguardState.enabled);
        pw.println(prefix2 + "offReason=" + android.view.WindowManagerPolicyConstants.offReasonToString(this.mKeyguardState.offReason));
        pw.println(prefix2 + "currentUser=" + this.mKeyguardState.currentUser);
        pw.println(prefix2 + "bootCompleted=" + this.mKeyguardState.bootCompleted);
        pw.println(prefix2 + "screenState=" + screenStateToString(this.mKeyguardState.screenState));
        pw.println(prefix2 + "interactiveState=" + interactiveStateToString(this.mKeyguardState.interactiveState));
        if (this.mKeyguardService != null) {
            this.mKeyguardService.dump(prefix2, pw);
        }
    }

    private static java.lang.String screenStateToString(int screen) {
        switch (screen) {
            case 0:
                return "SCREEN_STATE_OFF";
            case 1:
                return "SCREEN_STATE_TURNING_ON";
            case 2:
                return "SCREEN_STATE_ON";
            case 3:
                return "SCREEN_STATE_TURNING_OFF";
            default:
                return java.lang.Integer.toString(screen);
        }
    }

    private static java.lang.String interactiveStateToString(int interactive) {
        switch (interactive) {
            case 0:
                return "INTERACTIVE_STATE_SLEEP";
            case 1:
                return "INTERACTIVE_STATE_WAKING";
            case 2:
                return "INTERACTIVE_STATE_AWAKE";
            case 3:
                return "INTERACTIVE_STATE_GOING_TO_SLEEP";
            default:
                return java.lang.Integer.toString(interactive);
        }
    }

    public void requestKeyguard(java.lang.String command) {
        if (this.mKeyguardService != null) {
            this.mKeyguardService.requestKeyguard(command);
        }
    }
}
