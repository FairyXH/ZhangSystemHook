package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public final class PowerAction extends com.android.internal.globalactions.SinglePressAction implements com.android.internal.globalactions.LongPressAction {
    private final android.content.Context mContext;
    private final com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs mWindowManagerFuncs;

    public PowerAction(android.content.Context context, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
        super(android.R.drawable.ic_lock_power_off, android.R.string.global_action_logout);
        this.mContext = context;
        this.mWindowManagerFuncs = windowManagerFuncs;
    }

    public boolean onLongPress() {
        if (android.app.ActivityManager.isUserAMonkey()) {
            return false;
        }
        android.os.UserManager um = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        if (um.hasUserRestriction("no_safe_boot")) {
            return false;
        }
        this.mWindowManagerFuncs.rebootSafeMode(true);
        return true;
    }

    public boolean showDuringKeyguard() {
        return true;
    }

    public boolean showBeforeProvisioning() {
        return true;
    }

    public void onPress() {
        if (android.app.ActivityManager.isUserAMonkey()) {
            return;
        }
        this.mWindowManagerFuncs.shutdown(false);
    }
}
