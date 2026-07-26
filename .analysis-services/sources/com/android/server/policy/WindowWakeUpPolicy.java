package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
class WindowWakeUpPolicy {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "WindowWakeUpPolicy";
    private final boolean mAllowTheaterModeWakeFromCameraLens;
    private final boolean mAllowTheaterModeWakeFromKey;
    private final boolean mAllowTheaterModeWakeFromLidSwitch;
    private final boolean mAllowTheaterModeWakeFromMotion;
    private final boolean mAllowTheaterModeWakeFromPowerKey;
    private final boolean mAllowTheaterModeWakeFromWakeGesture;
    private final com.android.internal.os.Clock mClock;
    private final android.content.Context mContext;
    private com.android.server.policy.WindowWakeUpPolicyInternal.InputWakeUpDelegate mInputWakeUpDelegate;
    private final android.os.PowerManager mPowerManager;
    private final android.view.WindowManager mWindowManager;

    WindowWakeUpPolicy(android.content.Context context) {
        this(context, com.android.internal.os.Clock.SYSTEM_CLOCK);
    }

    WindowWakeUpPolicy(android.content.Context context, com.android.internal.os.Clock clock) {
        this.mContext = context;
        this.mPowerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        this.mWindowManager = (android.view.WindowManager) context.getSystemService(android.view.WindowManager.class);
        this.mClock = clock;
        android.content.res.Resources res = context.getResources();
        this.mAllowTheaterModeWakeFromKey = res.getBoolean(android.R.bool.config_allowTheaterModeWakeFromCameraLens);
        this.mAllowTheaterModeWakeFromPowerKey = this.mAllowTheaterModeWakeFromKey || res.getBoolean(android.R.bool.config_allowTheaterModeWakeFromLidSwitch);
        this.mAllowTheaterModeWakeFromMotion = res.getBoolean(android.R.bool.config_allowTheaterModeWakeFromGesture);
        this.mAllowTheaterModeWakeFromCameraLens = res.getBoolean(android.R.bool.config_allowRotationResolver);
        this.mAllowTheaterModeWakeFromLidSwitch = res.getBoolean(android.R.bool.config_allowTheaterModeWakeFromDock);
        this.mAllowTheaterModeWakeFromWakeGesture = res.getBoolean(android.R.bool.config_allowStartActivityForLongPressOnPowerInSetup);
        if (com.android.server.policy.Flags.supportInputWakeupDelegate()) {
            com.android.server.LocalServices.addService(com.android.server.policy.WindowWakeUpPolicyInternal.class, new com.android.server.policy.WindowWakeUpPolicy.LocalService());
        }
    }

    private final class LocalService implements com.android.server.policy.WindowWakeUpPolicyInternal {
        private LocalService() {
        }

        @Override // com.android.server.policy.WindowWakeUpPolicyInternal
        public void setInputWakeUpDelegate(com.android.server.policy.WindowWakeUpPolicyInternal.InputWakeUpDelegate delegate) {
            if (!com.android.server.policy.Flags.supportInputWakeupDelegate()) {
                android.util.Slog.w(com.android.server.policy.WindowWakeUpPolicy.TAG, "Input wake up delegates not supported.");
            } else {
                com.android.server.policy.WindowWakeUpPolicy.this.mInputWakeUpDelegate = delegate;
            }
        }
    }

    boolean wakeUpFromKey(long eventTime, int keyCode, boolean isDown) {
        boolean wakeAllowedDuringTheaterMode;
        if (keyCode == 26) {
            wakeAllowedDuringTheaterMode = this.mAllowTheaterModeWakeFromPowerKey;
        } else {
            wakeAllowedDuringTheaterMode = this.mAllowTheaterModeWakeFromKey;
        }
        if (!canWakeUp(wakeAllowedDuringTheaterMode)) {
            return false;
        }
        if (this.mInputWakeUpDelegate != null && this.mInputWakeUpDelegate.wakeUpFromKey(eventTime, keyCode, isDown)) {
            return true;
        }
        wakeUp(eventTime, keyCode == 26 ? 1 : 6, keyCode == 26 ? "POWER" : "KEY");
        return true;
    }

    boolean wakeUpFromMotion(long eventTime, int source, boolean isDown) {
        if (!canWakeUp(this.mAllowTheaterModeWakeFromMotion)) {
            return false;
        }
        if (this.mInputWakeUpDelegate != null && this.mInputWakeUpDelegate.wakeUpFromMotion(eventTime, source, isDown)) {
            return true;
        }
        wakeUp(eventTime, 7, "MOTION");
        return true;
    }

    boolean wakeUpFromCameraCover(long eventTime) {
        if (!canWakeUp(this.mAllowTheaterModeWakeFromCameraLens)) {
            return false;
        }
        wakeUp(eventTime, 5, "CAMERA_COVER");
        return true;
    }

    boolean wakeUpFromLid() {
        if (!canWakeUp(this.mAllowTheaterModeWakeFromLidSwitch)) {
            return false;
        }
        wakeUp(this.mClock.uptimeMillis(), 9, "LID");
        return true;
    }

    boolean wakeUpFromPowerKeyCameraGesture() {
        if (!canWakeUp(this.mAllowTheaterModeWakeFromPowerKey)) {
            return false;
        }
        wakeUp(this.mClock.uptimeMillis(), 5, "CAMERA_GESTURE_PREVENT_LOCK");
        return true;
    }

    boolean wakeUpFromWakeGesture() {
        if (!canWakeUp(this.mAllowTheaterModeWakeFromWakeGesture)) {
            return false;
        }
        wakeUp(this.mClock.uptimeMillis(), 4, "GESTURE");
        return true;
    }

    private boolean canWakeUp(boolean wakeInTheaterMode) {
        if (com.android.server.policy.Flags.supportInputWakeupDelegate() && isDefaultDisplayOn()) {
            return true;
        }
        boolean isTheaterModeEnabled = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "theater_mode_on", 0) == 1;
        return wakeInTheaterMode || !isTheaterModeEnabled;
    }

    private boolean isDefaultDisplayOn() {
        return android.view.Display.isOnState(this.mWindowManager.getDefaultDisplay().getState());
    }

    private void wakeUp(long wakeTime, int reason, java.lang.String details) {
        this.mPowerManager.wakeUp(wakeTime, reason, "android.policy:" + details);
    }
}
