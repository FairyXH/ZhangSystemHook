package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class PendingIntentRecord extends android.content.IIntentSender.Stub {
    public static final int CANCEL_REASON_HOSTING_ACTIVITY_DESTROYED = 16;
    public static final int CANCEL_REASON_NULL = 0;
    public static final int CANCEL_REASON_ONE_SHOT_SENT = 64;
    public static final int CANCEL_REASON_OWNER_CANCELED = 8;
    public static final int CANCEL_REASON_OWNER_FORCE_STOPPED = 4;
    public static final int CANCEL_REASON_OWNER_UNINSTALLED = 2;
    public static final int CANCEL_REASON_SUPERSEDED = 32;
    public static final int CANCEL_REASON_USER_STOPPED = 1;
    private static final long DEFAULT_RESCIND_BAL_PRIVILEGES_FROM_PENDING_INTENT_SENDER = 244637991;
    public static final int FLAG_ACTIVITY_SENDER = 1;
    public static final int FLAG_BROADCAST_SENDER = 2;
    public static final int FLAG_SERVICE_SENDER = 4;
    private static final java.lang.String TAG = "ActivityManager";
    final com.android.server.am.PendingIntentController controller;
    final com.android.server.am.PendingIntentRecord.Key key;
    java.lang.String lastTag;
    java.lang.String lastTagPrefix;
    private android.util.ArrayMap<android.os.IBinder, com.android.server.am.PendingIntentRecord.TempAllowListDuration> mAllowlistDuration;
    private android.os.RemoteCallbackList<com.android.internal.os.IResultReceiver> mCancelCallbacks;
    java.lang.String stringName;
    final int uid;
    boolean sent = false;
    boolean canceled = false;
    int cancelReason = 0;
    private android.util.ArraySet<android.os.IBinder> mAllowBgActivityStartsForActivitySender = new android.util.ArraySet<>();
    private android.util.ArraySet<android.os.IBinder> mAllowBgActivityStartsForBroadcastSender = new android.util.ArraySet<>();
    private android.util.ArraySet<android.os.IBinder> mAllowBgActivityStartsForServiceSender = new android.util.ArraySet<>();
    private com.android.server.am.IPendingIntentRecordExt mPendingIntentRecordExt = (com.android.server.am.IPendingIntentRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IPendingIntentRecordExt.class).base(this).create();
    public final java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> ref = new java.lang.ref.WeakReference<>(this);

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface CancellationReason {
    }

    static final class Key {
        private static final int ODD_PRIME_NUMBER = 37;
        final android.os.IBinder activity;
        android.content.Intent[] allIntents;
        java.lang.String[] allResolvedTypes;
        final java.lang.String featureId;
        final int flags;
        final int hashCode;
        final com.android.server.wm.SafeActivityOptions options;
        final java.lang.String packageName;
        final int requestCode;
        final android.content.Intent requestIntent;
        final java.lang.String requestResolvedType;
        final int type;
        final int userId;
        final java.lang.String who;

        Key(int _t, java.lang.String _p, java.lang.String _featureId, android.os.IBinder _a, java.lang.String _w, int _r, android.content.Intent[] _i, java.lang.String[] _it, int _f, com.android.server.wm.SafeActivityOptions _o, int _userId) {
            this.type = _t;
            this.packageName = _p;
            this.featureId = _featureId;
            this.activity = _a;
            this.who = _w;
            this.requestCode = _r;
            this.requestIntent = _i != null ? _i[_i.length - 1] : null;
            this.requestResolvedType = _it != null ? _it[_it.length - 1] : null;
            this.allIntents = _i;
            this.allResolvedTypes = _it;
            this.flags = _f;
            this.options = _o;
            this.userId = _userId;
            int hash = (((((23 * 37) + _f) * 37) + _r) * 37) + _userId;
            hash = _w != null ? (hash * 37) + _w.hashCode() : hash;
            hash = _a != null ? (hash * 37) + _a.hashCode() : hash;
            hash = this.requestIntent != null ? (hash * 37) + this.requestIntent.filterHashCode() : hash;
            this.hashCode = ((((this.requestResolvedType != null ? (hash * 37) + this.requestResolvedType.hashCode() : hash) * 37) + (_p != null ? _p.hashCode() : 0)) * 37) + _t;
        }

        public boolean equals(java.lang.Object otherObj) {
            if (otherObj == null) {
                return false;
            }
            try {
                com.android.server.am.PendingIntentRecord.Key other = (com.android.server.am.PendingIntentRecord.Key) otherObj;
                if (this.type != other.type || this.userId != other.userId || !java.util.Objects.equals(this.packageName, other.packageName) || !java.util.Objects.equals(this.featureId, other.featureId) || this.activity != other.activity || !java.util.Objects.equals(this.who, other.who) || this.requestCode != other.requestCode) {
                    return false;
                }
                if (this.requestIntent != other.requestIntent) {
                    if (this.requestIntent != null) {
                        if (!this.requestIntent.filterEquals(other.requestIntent)) {
                            return false;
                        }
                    } else if (other.requestIntent != null) {
                        return false;
                    }
                }
                if (!java.util.Objects.equals(this.requestResolvedType, other.requestResolvedType)) {
                    return false;
                }
                if (this.flags != other.flags) {
                    return false;
                }
                return true;
            } catch (java.lang.ClassCastException e) {
                return false;
            }
        }

        public int hashCode() {
            return this.hashCode;
        }

        public java.lang.String toString() {
            return "Key{" + typeName() + " pkg=" + this.packageName + (this.featureId != null ? com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.featureId : "") + " intent=" + (this.requestIntent != null ? this.requestIntent.toShortString(false, true, false, false) : "<null>") + " flags=0x" + java.lang.Integer.toHexString(this.flags) + " u=" + this.userId + "} requestCode=" + this.requestCode;
        }

        java.lang.String typeName() {
            switch (this.type) {
                case 1:
                    return "broadcastIntent";
                case 2:
                    return "startActivity";
                case 3:
                    return "activityResult";
                case 4:
                    return "startService";
                case 5:
                    return "startForegroundService";
                default:
                    return java.lang.Integer.toString(this.type);
            }
        }
    }

    static final class TempAllowListDuration {
        long duration;
        java.lang.String reason;
        int reasonCode;
        int type;

        TempAllowListDuration(long _duration, int _type, int _reasonCode, java.lang.String _reason) {
            this.duration = _duration;
            this.type = _type;
            this.reasonCode = _reasonCode;
            this.reason = _reason;
        }
    }

    PendingIntentRecord(com.android.server.am.PendingIntentController _controller, com.android.server.am.PendingIntentRecord.Key _k, int _u) {
        this.controller = _controller;
        this.key = _k;
        this.uid = _u;
    }

    void setAllowlistDurationLocked(android.os.IBinder allowlistToken, long duration, int type, int reasonCode, java.lang.String reason) {
        if (duration > 0) {
            if (this.mAllowlistDuration == null) {
                this.mAllowlistDuration = new android.util.ArrayMap<>();
            }
            this.mAllowlistDuration.put(allowlistToken, new com.android.server.am.PendingIntentRecord.TempAllowListDuration(duration, type, reasonCode, reason));
        } else if (this.mAllowlistDuration != null) {
            this.mAllowlistDuration.remove(allowlistToken);
            if (this.mAllowlistDuration.size() <= 0) {
                this.mAllowlistDuration = null;
            }
        }
        this.stringName = null;
    }

    void setAllowBgActivityStarts(android.os.IBinder token, int flags) {
        if (token == null) {
            return;
        }
        if ((flags & 1) != 0) {
            this.mAllowBgActivityStartsForActivitySender.add(token);
        }
        if ((flags & 2) != 0) {
            this.mAllowBgActivityStartsForBroadcastSender.add(token);
        }
        if ((flags & 4) != 0) {
            this.mAllowBgActivityStartsForServiceSender.add(token);
        }
    }

    void clearAllowBgActivityStarts(android.os.IBinder token) {
        if (token == null) {
            return;
        }
        this.mAllowBgActivityStartsForActivitySender.remove(token);
        this.mAllowBgActivityStartsForBroadcastSender.remove(token);
        this.mAllowBgActivityStartsForServiceSender.remove(token);
    }

    public void registerCancelListenerLocked(com.android.internal.os.IResultReceiver receiver) {
        if (this.mCancelCallbacks == null) {
            this.mCancelCallbacks = new android.os.RemoteCallbackList<>();
        }
        this.mCancelCallbacks.register(receiver);
    }

    public void unregisterCancelListenerLocked(com.android.internal.os.IResultReceiver receiver) {
        if (this.mCancelCallbacks == null) {
            return;
        }
        this.mCancelCallbacks.unregister(receiver);
        if (this.mCancelCallbacks.getRegisteredCallbackCount() <= 0) {
            this.mCancelCallbacks = null;
        }
    }

    public android.os.RemoteCallbackList<com.android.internal.os.IResultReceiver> detachCancelListenersLocked() {
        android.os.RemoteCallbackList<com.android.internal.os.IResultReceiver> listeners = this.mCancelCallbacks;
        this.mCancelCallbacks = null;
        return listeners;
    }

    public void send(int code, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder allowlistToken, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) throws java.lang.Throwable {
        sendInner(null, code, intent, resolvedType, allowlistToken, finishedReceiver, requiredPermission, null, null, 0, 0, 0, options);
    }

    public void send(android.app.IApplicationThread caller, int code, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder allowlistToken, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) throws java.lang.Throwable {
        sendInner(caller, code, intent, resolvedType, allowlistToken, finishedReceiver, requiredPermission, null, null, 0, 0, 0, options);
    }

    public int sendWithResult(android.app.IApplicationThread caller, int code, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder allowlistToken, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) {
        return sendInner(caller, code, intent, resolvedType, allowlistToken, finishedReceiver, requiredPermission, null, null, 0, 0, 0, options);
    }

    public static boolean isPendingIntentBalAllowedByPermission(android.app.ActivityOptions activityOptions) {
        if (activityOptions == null) {
            return false;
        }
        return activityOptions.isPendingIntentBackgroundActivityLaunchAllowedByPermission();
    }

    public static android.app.BackgroundStartPrivileges getBackgroundStartPrivilegesAllowedByCaller(android.app.ActivityOptions activityOptions, int callingUid, java.lang.String callingPackage) {
        if (activityOptions == null) {
            return getDefaultBackgroundStartPrivileges(callingUid, callingPackage);
        }
        return getBackgroundStartPrivilegesAllowedByCaller(activityOptions.toBundle(), callingUid, callingPackage);
    }

    private static android.app.BackgroundStartPrivileges getBackgroundStartPrivilegesAllowedByCaller(android.os.Bundle options, int callingUid, java.lang.String callingPackage) {
        if (options == null) {
            return getDefaultBackgroundStartPrivileges(callingUid, callingPackage);
        }
        switch (options.getInt("android.pendingIntent.backgroundActivityAllowed", 0)) {
            case 0:
                return getDefaultBackgroundStartPrivileges(callingUid, callingPackage);
            case 1:
            default:
                return android.app.BackgroundStartPrivileges.ALLOW_BAL;
            case 2:
                return android.app.BackgroundStartPrivileges.NONE;
        }
    }

    public static android.app.BackgroundStartPrivileges getDefaultBackgroundStartPrivileges(int callingUid, java.lang.String callingPackage) {
        boolean isChangeEnabledForApp;
        if (callingUid == 0 || callingUid == 1000) {
            return android.app.BackgroundStartPrivileges.ALLOW_FGS;
        }
        if (callingPackage != null) {
            isChangeEnabledForApp = android.app.compat.CompatChanges.isChangeEnabled(DEFAULT_RESCIND_BAL_PRIVILEGES_FROM_PENDING_INTENT_SENDER, callingPackage, android.os.UserHandle.getUserHandleForUid(callingUid));
        } else {
            isChangeEnabledForApp = android.app.compat.CompatChanges.isChangeEnabled(DEFAULT_RESCIND_BAL_PRIVILEGES_FROM_PENDING_INTENT_SENDER, callingUid);
        }
        if (isChangeEnabledForApp) {
            return android.app.BackgroundStartPrivileges.ALLOW_FGS;
        }
        return android.app.BackgroundStartPrivileges.ALLOW_BAL;
    }

    @java.lang.Deprecated
    public int sendInner(int code, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder allowlistToken, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int flagsMask, int flagsValues, android.os.Bundle options) {
        return sendInner(null, code, intent, resolvedType, allowlistToken, finishedReceiver, requiredPermission, resultTo, resultWho, requestCode, flagsMask, flagsValues, options);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x0212  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0442  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0066 A[Catch: all -> 0x0053, TRY_ENTER, TryCatch #31 {all -> 0x0053, blocks: (B:11:0x0029, B:18:0x0066, B:20:0x006b, B:22:0x0075, B:23:0x0081, B:28:0x0090, B:31:0x009b), top: B:320:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0525  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x055d  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0083 A[Catch: all -> 0x05b2, TRY_ENTER, TRY_LEAVE, TryCatch #6 {all -> 0x05b2, blocks: (B:9:0x0025, B:15:0x005f, B:25:0x0083, B:29:0x0095, B:33:0x00a5), top: B:289:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:283:0x019e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0129 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:309:0x01ba A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0185 A[Catch: all -> 0x0592, TRY_ENTER, TRY_LEAVE, TryCatch #30 {all -> 0x0592, blocks: (B:62:0x0123, B:75:0x0185, B:80:0x0195), top: B:318:0x0123 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x018c A[Catch: all -> 0x017e, TRY_ENTER, TRY_LEAVE, TryCatch #14 {all -> 0x017e, blocks: (B:64:0x0129, B:66:0x012f, B:68:0x0168, B:69:0x016d, B:70:0x0175, B:71:0x0176, B:78:0x018c), top: B:301:0x0129 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0195 A[Catch: all -> 0x0592, TRY_ENTER, TRY_LEAVE, TryCatch #30 {all -> 0x0592, blocks: (B:62:0x0123, B:75:0x0185, B:80:0x0195), top: B:318:0x0123 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int sendInner(android.app.IApplicationThread r45, int r46, android.content.Intent r47, java.lang.String r48, android.os.IBinder r49, android.content.IIntentReceiver r50, java.lang.String r51, android.os.IBinder r52, java.lang.String r53, int r54, int r55, int r56, android.os.Bundle r57) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1488
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.PendingIntentRecord.sendInner(android.app.IApplicationThread, int, android.content.Intent, java.lang.String, android.os.IBinder, android.content.IIntentReceiver, java.lang.String, android.os.IBinder, java.lang.String, int, int, int, android.os.Bundle):int");
    }

    private android.app.BackgroundStartPrivileges getBackgroundStartPrivilegesForActivitySender(android.os.IBinder allowlistToken) {
        if (this.mAllowBgActivityStartsForActivitySender.contains(allowlistToken)) {
            return android.app.BackgroundStartPrivileges.allowBackgroundActivityStarts(allowlistToken);
        }
        return android.app.BackgroundStartPrivileges.NONE;
    }

    private android.app.BackgroundStartPrivileges getBackgroundStartPrivilegesForActivitySender(android.util.ArraySet<android.os.IBinder> allowedTokenSet, android.os.IBinder allowlistToken, android.os.Bundle options, int callingUid) {
        if (allowedTokenSet.contains(allowlistToken)) {
            return android.app.BackgroundStartPrivileges.allowBackgroundActivityStarts(allowlistToken);
        }
        if (this.uid != callingUid && this.controller.mAtmInternal.isUidForeground(callingUid)) {
            return getBackgroundStartPrivilegesAllowedByCaller(options, callingUid, (java.lang.String) null);
        }
        return android.app.BackgroundStartPrivileges.NONE;
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            if (!this.canceled) {
                this.controller.mH.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.am.PendingIntentRecord$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.am.PendingIntentRecord) obj).completeFinalize();
                    }
                }, this));
            }
        } finally {
            super/*java.lang.Object*/.finalize();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void completeFinalize() {
        synchronized (this.controller.mLock) {
            java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> current = this.controller.mIntentSenderRecords.get(this.key);
            if (current == this.ref) {
                this.controller.mIntentSenderRecords.remove(this.key);
                this.controller.decrementUidStatLocked(this);
            }
        }
    }

    static java.lang.String cancelReasonToString(int cancelReason) {
        switch (cancelReason) {
            case 0:
                return "NULL";
            case 1:
                return "USER_STOPPED";
            case 2:
                return "OWNER_UNINSTALLED";
            case 4:
                return "OWNER_FORCE_STOPPED";
            case 8:
                return "OWNER_CANCELED";
            case 16:
                return "HOSTING_ACTIVITY_DESTROYED";
            case 32:
                return "SUPERSEDED";
            case 64:
                return "ONE_SHOT_SENT";
            default:
                return "UNKNOWN";
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("uid=");
        pw.print(this.uid);
        pw.print(" packageName=");
        pw.print(this.key.packageName);
        pw.print(" featureId=");
        pw.print(this.key.featureId);
        pw.print(" type=");
        pw.print(this.key.typeName());
        pw.print(" flags=0x");
        pw.println(java.lang.Integer.toHexString(this.key.flags));
        if (this.key.activity != null || this.key.who != null) {
            pw.print(prefix);
            pw.print("activity=");
            pw.print(this.key.activity);
            pw.print(" who=");
            pw.println(this.key.who);
        }
        if (this.key.requestCode != 0 || this.key.requestResolvedType != null) {
            pw.print(prefix);
            pw.print("requestCode=");
            pw.print(this.key.requestCode);
            pw.print(" requestResolvedType=");
            pw.println(this.key.requestResolvedType);
        }
        if (this.key.requestIntent != null) {
            pw.print(prefix);
            pw.print("requestIntent=");
            pw.println(this.key.requestIntent.toShortString(false, true, true, false));
        }
        if (this.sent || this.canceled) {
            pw.print(prefix);
            pw.print("sent=");
            pw.print(this.sent);
            pw.print(" canceled=");
            pw.print(this.canceled);
            pw.print(" cancelReason=");
            pw.println(cancelReasonToString(this.cancelReason));
        }
        if (this.mAllowlistDuration != null) {
            pw.print(prefix);
            pw.print("allowlistDuration=");
            for (int i = 0; i < this.mAllowlistDuration.size(); i++) {
                if (i != 0) {
                    pw.print(", ");
                }
                com.android.server.am.PendingIntentRecord.TempAllowListDuration entry = this.mAllowlistDuration.valueAt(i);
                pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.mAllowlistDuration.keyAt(i))));
                pw.print(":");
                android.util.TimeUtils.formatDuration(entry.duration, pw);
                pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                pw.print(entry.type);
                pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                pw.print(android.os.PowerWhitelistManager.reasonCodeToString(entry.reasonCode));
                pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                pw.print(entry.reason);
            }
            pw.println();
        }
        if (this.mCancelCallbacks != null) {
            pw.print(prefix);
            pw.println("mCancelCallbacks:");
            for (int i2 = 0; i2 < this.mCancelCallbacks.getRegisteredCallbackCount(); i2++) {
                pw.print(prefix);
                pw.print("  #");
                pw.print(i2);
                pw.print(": ");
                pw.println(this.mCancelCallbacks.getRegisteredCallbackItem(i2));
            }
        }
    }

    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("PendingIntentRecord{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        sb.append(this.key.packageName);
        if (this.key.featureId != null) {
            sb.append('/');
            sb.append(this.key.featureId);
        }
        sb.append(' ');
        sb.append(this.key.typeName());
        if (this.mAllowlistDuration != null) {
            sb.append(" (allowlist: ");
            for (int i = 0; i < this.mAllowlistDuration.size(); i++) {
                if (i != 0) {
                    sb.append(",");
                }
                com.android.server.am.PendingIntentRecord.TempAllowListDuration entry = this.mAllowlistDuration.valueAt(i);
                sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.mAllowlistDuration.keyAt(i))));
                sb.append(":");
                android.util.TimeUtils.formatDuration(entry.duration, sb);
                sb.append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                sb.append(entry.type);
                sb.append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                sb.append(android.os.PowerWhitelistManager.reasonCodeToString(entry.reasonCode));
                sb.append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                sb.append(entry.reason);
            }
            sb.append(")");
        }
        sb.append('}');
        java.lang.String string = sb.toString();
        this.stringName = string;
        return string;
    }
}
