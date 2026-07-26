package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class HardeningEnforcer {
    private static final boolean DEBUG = false;
    private static final int LOG_NB_EVENTS = 20;
    public static final int METHOD_AUDIO_MANAGER_ADJUST_STREAM_VOLUME = 103;
    public static final int METHOD_AUDIO_MANAGER_ADJUST_SUGGESTED_STREAM_VOLUME = 102;
    public static final int METHOD_AUDIO_MANAGER_ADJUST_VOLUME = 101;
    public static final int METHOD_AUDIO_MANAGER_REQUEST_AUDIO_FOCUS = 300;
    public static final int METHOD_AUDIO_MANAGER_SET_RINGER_MODE = 200;
    public static final int METHOD_AUDIO_MANAGER_SET_STREAM_VOLUME = 100;
    private static final java.lang.String TAG = "AS.HardeningEnforcer";
    final android.app.ActivityManager mActivityManager;
    final android.app.AppOpsManager mAppOps;
    final android.content.Context mContext;
    final com.android.server.utils.EventLogger mEventLogger = new com.android.server.utils.EventLogger(20, "Hardening enforcement");
    final boolean mIsAutomotive;
    final android.content.pm.PackageManager mPackageManager;

    public HardeningEnforcer(android.content.Context ctxt, boolean isAutomotive, android.app.AppOpsManager appOps, android.content.pm.PackageManager pm) {
        this.mContext = ctxt;
        this.mIsAutomotive = isAutomotive;
        this.mAppOps = appOps;
        this.mActivityManager = (android.app.ActivityManager) ctxt.getSystemService(android.app.ActivityManager.class);
        this.mPackageManager = pm;
    }

    protected void dump(java.io.PrintWriter pw) {
        this.mEventLogger.dump(pw);
    }

    protected boolean blockVolumeMethod(int volumeMethod) {
        if (!this.mIsAutomotive || !android.media.audio.Flags.autoPublicVolumeApiHardening() || this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_SETTINGS_PRIVILEGED") == 0 || android.os.Binder.getCallingUid() < 10000) {
            return false;
        }
        android.util.Slog.e(TAG, "Preventing volume method " + volumeMethod + " for " + getPackNameForUid(android.os.Binder.getCallingUid()));
        return true;
    }

    protected boolean blockFocusMethod(int callingUid, int focusMethod, java.lang.String clientId, int durationHint, java.lang.String packageName, java.lang.String attributionTag, int targetSdk) {
        if (packageName.isEmpty()) {
            packageName = getPackNameForUid(callingUid);
        }
        if (noteOp(32, callingUid, packageName, attributionTag) || targetSdk < 35) {
            return false;
        }
        java.lang.String errorMssg = "Focus request DENIED for uid:" + callingUid + " clientId:" + clientId + " req:" + durationHint + " procState:" + this.mActivityManager.getUidProcessState(callingUid);
        this.mEventLogger.enqueueAndSlog(errorMssg, 0, TAG);
        return true;
    }

    private java.lang.String getPackNameForUid(int uid) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String[] names = this.mPackageManager.getPackagesForUid(uid);
            if (names != null && names.length != 0 && !android.text.TextUtils.isEmpty(names[0])) {
                return names[0];
            }
            return "[" + uid + "]";
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean noteOp(int op, int uid, java.lang.String packageName, java.lang.String attributionTag) {
        if (this.mAppOps.noteOpNoThrow(op, uid, packageName, attributionTag, (java.lang.String) null) != 0) {
            return false;
        }
        return true;
    }
}
