package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ServiceRecord extends android.os.Binder implements android.content.ComponentName.WithComponentName {
    static final int MAX_DELIVERY_COUNT = 3;
    static final int MAX_DONE_EXECUTING_COUNT = 6;
    private static final java.lang.String TAG = "ActivityManager";
    static final long USE_NEW_BFSL_LOGIC = 311208749;
    static final long USE_NEW_WIU_LOGIC_FOR_CAPABILITIES = 313677553;
    static final long USE_NEW_WIU_LOGIC_FOR_START = 311208629;
    boolean allowlistManager;
    final com.android.server.am.ActivityManagerService ams;
    com.android.server.am.ProcessRecord app;
    android.content.pm.ApplicationInfo appInfo;
    final android.util.ArrayMap<android.content.Intent.FilterComparison, com.android.server.am.IntentBindRecord> bindings;
    boolean callStart;
    private final android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> connections;
    int crashCount;
    final long createRealTime;
    boolean createdFromFg;
    final java.lang.String definingPackageName;
    final int definingUid;
    boolean delayed;
    boolean delayedStop;
    final java.util.ArrayList<com.android.server.am.ServiceRecord.StartItem> deliveredStarts;
    long destroyTime;
    boolean destroying;
    boolean executeFg;
    int executeNesting;
    long executingStart;
    final boolean exported;
    long fgDisplayTime;
    boolean fgRequired;
    boolean fgWaiting;
    int foregroundId;
    android.app.Notification foregroundNoti;
    int foregroundServiceType;
    boolean inSharedIsolatedProcess;
    final android.content.ComponentName instanceName;
    final android.content.Intent.FilterComparison intent;
    boolean isForeground;
    boolean isNotAppComponentUsage;
    final boolean isSdkSandbox;
    com.android.server.am.ProcessRecord isolationHostProc;
    long lastActivity;
    private int lastStartId;
    long lastTopAlmostPerceptibleBindRequestUptimeMs;
    int mAdjSeq;
    int mAllowStartForegroundAtEntering;
    int mAllowStart_byBindings;
    int mAllowStart_inBindService;
    int mAllowStart_noBinding;
    boolean mAllowUiJobScheduling;
    boolean mAllowWhileInUsePermissionInFgsAtEntering;
    int mAllowWiu_byBindings;
    int mAllowWiu_inBindService;
    int mAllowWiu_noBinding;
    private com.android.server.am.ProcessRecord mAppForAllowingBgActivityStartsByStart;
    private java.util.ArrayList<android.app.BackgroundStartPrivileges> mBackgroundStartPrivilegesByStart;
    private android.app.BackgroundStartPrivileges mBackgroundStartPrivilegesByStartMerged;
    private java.lang.Runnable mCleanUpAllowBgActivityStartsByStartCallback;
    long mEarliestRestartTime;
    com.android.server.am.ForegroundServiceDelegation mFgsDelegation;
    long mFgsEnterTime;
    long mFgsExitTime;
    boolean mFgsHasNotificationPermission;
    boolean mFgsNotificationDeferred;
    boolean mFgsNotificationShown;
    boolean mFgsNotificationWasDeferred;
    java.lang.String mInfoAllowStartForeground;
    com.android.server.am.ActivityManagerService.FgsTempAllowListItem mInfoTempFgsAllowListReason;
    private boolean mIsAllowedBgActivityStartsByBinding;
    boolean mIsFgsDelegate;
    boolean mKeepWarming;
    boolean mLoggedInfoAllowStartForeground;
    int mProcessStateOnRequest;
    android.content.pm.ApplicationInfo mRecentCallerApplicationInfo;
    java.lang.String mRecentCallingPackage;
    int mRecentCallingUid;
    long mRestartSchedulingTime;
    public com.android.server.am.IServiceRecordExt mServiceRecordExt;
    private com.android.server.am.ServiceRecord.ShortFgsInfo mShortFgsInfo;
    int mStartForegroundCount;
    final android.content.ComponentName name;
    long nextRestartTime;
    final java.lang.String packageName;
    int pendingConnectionGroup;
    int pendingConnectionImportance;
    final java.util.ArrayList<com.android.server.am.ServiceRecord.StartItem> pendingStarts;
    final java.lang.String permission;
    final java.lang.String processName;
    int restartCount;
    long restartDelay;
    long restartTime;
    com.android.internal.app.procstats.ServiceState restartTracker;
    final java.lang.Runnable restarter;
    final java.lang.String sdkSandboxClientAppPackage;
    final int sdkSandboxClientAppUid;
    final android.content.pm.ServiceInfo serviceInfo;
    final java.lang.String shortInstanceName;
    int startCommandResult;
    boolean startRequested;
    long startingBgTimeout;
    boolean stopIfKilled;
    java.lang.String stringName;
    int totalRestartCount;
    com.android.internal.app.procstats.ServiceState tracker;
    final int userId;

    private boolean useNewWiuLogic_forStart() {
        return com.android.server.am.Flags.newFgsRestrictionLogic() && android.app.compat.CompatChanges.isChangeEnabled(USE_NEW_WIU_LOGIC_FOR_START, this.appInfo.uid);
    }

    private boolean useNewWiuLogic_forCapabilities() {
        return com.android.server.am.Flags.newFgsRestrictionLogic() && android.app.compat.CompatChanges.isChangeEnabled(USE_NEW_WIU_LOGIC_FOR_CAPABILITIES, this.appInfo.uid);
    }

    private boolean useNewBfslLogic() {
        return com.android.server.am.Flags.newFgsRestrictionLogic() && android.app.compat.CompatChanges.isChangeEnabled(USE_NEW_BFSL_LOGIC, this.appInfo.uid);
    }

    private int getFgsAllowWiu_legacy() {
        return reasonOr(this.mAllowWiu_noBinding, this.mAllowWiu_inBindService);
    }

    private int getFgsAllowWiu_new() {
        return reasonOr(this.mAllowWiu_noBinding, this.mAllowWiu_byBindings);
    }

    int getFgsAllowWiu_forStart() {
        if (useNewWiuLogic_forStart()) {
            return getFgsAllowWiu_new();
        }
        return getFgsAllowWiu_legacy();
    }

    int getFgsAllowWiu_forCapabilities() {
        if (useNewWiuLogic_forCapabilities()) {
            return getFgsAllowWiu_new();
        }
        return getFgsAllowWiu_forStart();
    }

    boolean isFgsAllowedWiu_forStart() {
        return getFgsAllowWiu_forStart() != -1;
    }

    boolean isFgsAllowedWiu_forCapabilities() {
        return getFgsAllowWiu_forCapabilities() != -1;
    }

    private int getFgsAllowStart_legacy() {
        return reasonOr(this.mAllowStart_noBinding, this.mAllowStart_inBindService, this.mAllowStart_byBindings);
    }

    private int getFgsAllowStart_new() {
        return reasonOr(this.mAllowStart_noBinding, this.mAllowStart_byBindings);
    }

    int getFgsAllowStart() {
        if (useNewBfslLogic()) {
            return getFgsAllowStart_new();
        }
        return getFgsAllowStart_legacy();
    }

    boolean isFgsAllowedStart() {
        return getFgsAllowStart() != -1;
    }

    void clearFgsAllowWiu() {
        this.mAllowWiu_noBinding = -1;
        this.mAllowWiu_inBindService = -1;
        this.mAllowWiu_byBindings = -1;
    }

    void clearFgsAllowStart() {
        this.mAllowStart_noBinding = -1;
        this.mAllowStart_inBindService = -1;
        this.mAllowStart_byBindings = -1;
    }

    static int reasonOr(int first, int second) {
        return first != -1 ? first : second;
    }

    static int reasonOr(int first, int second, int third) {
        return first != -1 ? first : reasonOr(second, third);
    }

    boolean allowedChanged(int legacyCode, int newCode) {
        return (legacyCode == -1) != (newCode == -1);
    }

    private java.lang.String getFgsInfoForWtf() {
        return " cmp: " + getComponentName().toShortString() + " sdk: " + this.appInfo.targetSdkVersion;
    }

    void maybeLogFgsLogicChange() {
        int wiuLegacy = getFgsAllowWiu_legacy();
        int wiuNew = getFgsAllowWiu_new();
        int startLegacy = getFgsAllowStart_legacy();
        int startNew = getFgsAllowStart_new();
        boolean wiuChanged = allowedChanged(wiuLegacy, wiuNew);
        boolean startChanged = allowedChanged(startLegacy, startNew);
        if (!wiuChanged && !startChanged) {
            return;
        }
        java.lang.String message = "FGS logic changed:" + (wiuChanged ? " [WIU changed]" : "") + (startChanged ? " [BFSL changed]" : "") + " Orig WIU:" + android.os.PowerExemptionManager.reasonCodeToString(wiuLegacy) + " New WIU:" + android.os.PowerExemptionManager.reasonCodeToString(wiuNew) + " Orig BFSL:" + android.os.PowerExemptionManager.reasonCodeToString(startLegacy) + " New BFSL:" + android.os.PowerExemptionManager.reasonCodeToString(startNew) + getFgsInfoForWtf();
        android.util.Slog.wtf(com.android.server.am.ActiveServices.TAG_SERVICE, message);
    }

    static class StartItem {
        final int callingId;
        long deliveredTime;
        int deliveryCount;
        int doneExecutingCount;
        final int id;
        final android.content.Intent intent;
        final java.lang.String mCallingPackageName;
        final java.lang.String mCallingProcessName;
        final int mCallingProcessState;
        final com.android.server.uri.NeededUriGrants neededGrants;
        final com.android.server.am.ServiceRecord sr;
        java.lang.String stringName;
        final boolean taskRemoved;
        com.android.server.uri.UriPermissionOwner uriPermissions;

        StartItem(com.android.server.am.ServiceRecord _sr, boolean _taskRemoved, int _id, android.content.Intent _intent, com.android.server.uri.NeededUriGrants _neededGrants, int _callingId, java.lang.String callingProcessName, java.lang.String callingPackageName, int callingProcessState) {
            this.sr = _sr;
            this.taskRemoved = _taskRemoved;
            this.id = _id;
            this.intent = _intent;
            this.neededGrants = _neededGrants;
            this.callingId = _callingId;
            this.mCallingProcessName = callingProcessName;
            this.mCallingPackageName = callingPackageName;
            this.mCallingProcessState = callingProcessState;
        }

        com.android.server.uri.UriPermissionOwner getUriPermissionsLocked() {
            if (this.uriPermissions == null) {
                this.uriPermissions = new com.android.server.uri.UriPermissionOwner(this.sr.ams.mUgmInternal, this);
            }
            return this.uriPermissions;
        }

        void removeUriPermissionsLocked() {
            if (this.uriPermissions != null) {
                this.uriPermissions.removeUriPermissions();
                this.uriPermissions = null;
            }
        }

        public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, long now) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, this.id);
            android.util.proto.ProtoUtils.toDuration(proto, 1146756268034L, this.deliveredTime, now);
            proto.write(1120986464259L, this.deliveryCount);
            proto.write(1120986464260L, this.doneExecutingCount);
            if (this.intent != null) {
                this.intent.dumpDebug(proto, 1146756268037L, true, true, true, false);
            }
            if (this.neededGrants != null) {
                this.neededGrants.dumpDebug(proto, 1146756268038L);
            }
            if (this.uriPermissions != null) {
                this.uriPermissions.dumpDebug(proto, 1146756268039L);
            }
            proto.end(token);
        }

        public java.lang.String toString() {
            if (this.stringName != null) {
                return this.stringName;
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("ServiceRecord{").append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.sr))).append(' ').append(this.sr.shortInstanceName).append(" StartItem ").append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))).append(" id=").append(this.id).append('}');
            java.lang.String string = sb.toString();
            this.stringName = string;
            return string;
        }
    }

    class ShortFgsInfo {
        private int mStartForegroundCount;
        private int mStartId;
        private final long mStartTime;

        ShortFgsInfo(long startTime) {
            this.mStartTime = startTime;
            update();
        }

        public void update() {
            this.mStartForegroundCount = com.android.server.am.ServiceRecord.this.mStartForegroundCount;
            this.mStartId = com.android.server.am.ServiceRecord.this.getLastStartId();
        }

        long getStartTime() {
            return this.mStartTime;
        }

        int getStartForegroundCount() {
            return this.mStartForegroundCount;
        }

        int getStartId() {
            return this.mStartId;
        }

        boolean isCurrent() {
            return this.mStartForegroundCount == com.android.server.am.ServiceRecord.this.mStartForegroundCount;
        }

        long getTimeoutTime() {
            return this.mStartTime + com.android.server.am.ServiceRecord.this.ams.mConstants.mShortFgsTimeoutDuration;
        }

        long getProcStateDemoteTime() {
            return this.mStartTime + com.android.server.am.ServiceRecord.this.ams.mConstants.mShortFgsTimeoutDuration + com.android.server.am.ServiceRecord.this.ams.mConstants.mShortFgsProcStateExtraWaitDuration;
        }

        long getAnrTime() {
            return this.mStartTime + com.android.server.am.ServiceRecord.this.ams.mConstants.mShortFgsTimeoutDuration + com.android.server.am.ServiceRecord.this.ams.mConstants.mShortFgsAnrExtraWaitDuration;
        }

        java.lang.String getDescription() {
            return "sfc=" + this.mStartForegroundCount + " sid=" + this.mStartId + " stime=" + this.mStartTime + " tt=" + getTimeoutTime() + " dt=" + getProcStateDemoteTime() + " at=" + getAnrTime();
        }
    }

    static class TimeLimitedFgsInfo {
        private long mFirstFgsStartRealtime;
        private long mFirstFgsStartUptime;
        private long mLastFgsStartTime;
        private long mTimeLimitExceededAt = Long.MIN_VALUE;
        private long mTotalRuntime = 0;
        private int mNumParallelServices = 0;

        TimeLimitedFgsInfo() {
        }

        public void noteFgsFgsStart(long startTime) {
            this.mNumParallelServices++;
            if (this.mNumParallelServices == 1) {
                this.mFirstFgsStartUptime = startTime;
                this.mFirstFgsStartRealtime = android.os.SystemClock.elapsedRealtime();
            }
            this.mLastFgsStartTime = startTime;
        }

        public long getFirstFgsStartUptime() {
            return this.mFirstFgsStartUptime;
        }

        public long getFirstFgsStartRealtime() {
            return this.mFirstFgsStartRealtime;
        }

        public long getLastFgsStartTime() {
            return this.mLastFgsStartTime;
        }

        public void decNumParallelServices() {
            if (this.mNumParallelServices > 0) {
                this.mNumParallelServices--;
            }
            if (this.mNumParallelServices == 0) {
                this.mLastFgsStartTime = 0L;
            }
        }

        public void updateTotalRuntime(long nowUptime) {
            this.mTotalRuntime += nowUptime - this.mLastFgsStartTime;
            this.mLastFgsStartTime = nowUptime;
        }

        public long getTotalRuntime() {
            return this.mTotalRuntime;
        }

        public void setTimeLimitExceededAt(long timeLimitExceededAt) {
            this.mTimeLimitExceededAt = timeLimitExceededAt;
        }

        public long getTimeLimitExceededAt() {
            return this.mTimeLimitExceededAt;
        }

        public void reset() {
            this.mNumParallelServices = 0;
            this.mFirstFgsStartUptime = 0L;
            this.mFirstFgsStartRealtime = 0L;
            this.mLastFgsStartTime = 0L;
            this.mTotalRuntime = 0L;
            this.mTimeLimitExceededAt = Long.MIN_VALUE;
        }
    }

    void dumpStartList(java.io.PrintWriter pw, java.lang.String prefix, java.util.List<com.android.server.am.ServiceRecord.StartItem> list, long now) {
        int N = list.size();
        for (int i = 0; i < N; i++) {
            com.android.server.am.ServiceRecord.StartItem si = list.get(i);
            pw.print(prefix);
            pw.print("#");
            pw.print(i);
            pw.print(" id=");
            pw.print(si.id);
            if (now != 0) {
                pw.print(" dur=");
                android.util.TimeUtils.formatDuration(si.deliveredTime, now, pw);
            }
            if (si.deliveryCount != 0) {
                pw.print(" dc=");
                pw.print(si.deliveryCount);
            }
            if (si.doneExecutingCount != 0) {
                pw.print(" dxc=");
                pw.print(si.doneExecutingCount);
            }
            pw.println("");
            pw.print(prefix);
            pw.print("  intent=");
            if (si.intent != null) {
                pw.println(si.intent.toString());
            } else {
                pw.println("null");
            }
            if (si.neededGrants != null) {
                pw.print(prefix);
                pw.print("  neededGrants=");
                pw.println(si.neededGrants);
            }
            if (si.uriPermissions != null) {
                si.uriPermissions.dump(pw, prefix);
            }
        }
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long j;
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.shortInstanceName);
        proto.write(1133871366146L, this.app != null);
        if (this.app != null) {
            proto.write(1120986464259L, this.app.getPid());
        }
        if (this.intent == null) {
            j = 1120986464259L;
        } else {
            j = 1120986464259L;
            this.intent.getIntent().dumpDebug(proto, 1146756268036L, false, true, false, false);
        }
        proto.write(1138166333445L, this.packageName);
        proto.write(1138166333446L, this.processName);
        proto.write(1138166333447L, this.permission);
        long now = android.os.SystemClock.uptimeMillis();
        long nowReal = android.os.SystemClock.elapsedRealtime();
        if (this.appInfo != null) {
            long appInfoToken = proto.start(1146756268040L);
            proto.write(1138166333441L, this.appInfo.sourceDir);
            if (!java.util.Objects.equals(this.appInfo.sourceDir, this.appInfo.publicSourceDir)) {
                proto.write(1138166333442L, this.appInfo.publicSourceDir);
            }
            proto.write(1138166333443L, this.appInfo.dataDir);
            proto.write(1120986464260L, this.appInfo.targetSdkVersion);
            proto.end(appInfoToken);
        }
        if (this.app != null) {
            this.app.dumpDebug(proto, 1146756268041L);
        }
        if (this.isolationHostProc != null) {
            this.isolationHostProc.dumpDebug(proto, 1146756268042L);
        }
        proto.write(1133871366155L, this.allowlistManager);
        proto.write(1133871366156L, this.delayed);
        if (this.isForeground || this.foregroundId != 0) {
            long fgToken = proto.start(1146756268045L);
            proto.write(1120986464257L, this.foregroundId);
            this.foregroundNoti.dumpDebug(proto, 1146756268034L);
            proto.write(j, this.foregroundServiceType);
            proto.end(fgToken);
        }
        android.util.proto.ProtoUtils.toDuration(proto, 1146756268046L, this.createRealTime, nowReal);
        android.util.proto.ProtoUtils.toDuration(proto, 1146756268047L, this.startingBgTimeout, now);
        android.util.proto.ProtoUtils.toDuration(proto, 1146756268048L, this.lastActivity, now);
        android.util.proto.ProtoUtils.toDuration(proto, 1146756268049L, this.restartTime, now);
        proto.write(1133871366162L, this.createdFromFg);
        proto.write(1133871366171L, isFgsAllowedWiu_forCapabilities());
        if (this.startRequested || this.delayedStop || this.lastStartId != 0) {
            long startToken = proto.start(1146756268051L);
            proto.write(1133871366145L, this.startRequested);
            proto.write(1133871366146L, this.delayedStop);
            proto.write(1133871366147L, this.stopIfKilled);
            proto.write(1120986464261L, this.lastStartId);
            proto.write(1120986464262L, this.startCommandResult);
            proto.end(startToken);
        }
        if (this.executeNesting != 0) {
            long executNestingToken = proto.start(1146756268052L);
            proto.write(1120986464257L, this.executeNesting);
            proto.write(1133871366146L, this.executeFg);
            android.util.proto.ProtoUtils.toDuration(proto, 1146756268035L, this.executingStart, now);
            proto.end(executNestingToken);
        }
        if (this.destroying || this.destroyTime != 0) {
            android.util.proto.ProtoUtils.toDuration(proto, 1146756268053L, this.destroyTime, now);
        }
        if (this.crashCount != 0 || this.restartCount != 0 || this.nextRestartTime - this.mRestartSchedulingTime != 0 || this.nextRestartTime != 0) {
            long crashToken = proto.start(1146756268054L);
            proto.write(1120986464257L, this.restartCount);
            android.util.proto.ProtoUtils.toDuration(proto, 1146756268034L, this.nextRestartTime - this.mRestartSchedulingTime, now);
            android.util.proto.ProtoUtils.toDuration(proto, 1146756268035L, this.nextRestartTime, now);
            proto.write(1120986464260L, this.crashCount);
            proto.end(crashToken);
        }
        if (this.deliveredStarts.size() > 0) {
            int N = this.deliveredStarts.size();
            for (int i = 0; i < N; i++) {
                this.deliveredStarts.get(i).dumpDebug(proto, 2246267895831L, now);
            }
        }
        if (this.pendingStarts.size() > 0) {
            int N2 = this.pendingStarts.size();
            for (int i2 = 0; i2 < N2; i2++) {
                this.pendingStarts.get(i2).dumpDebug(proto, 2246267895832L, now);
            }
        }
        if (this.bindings.size() > 0) {
            int N3 = this.bindings.size();
            for (int i3 = 0; i3 < N3; i3++) {
                com.android.server.am.IntentBindRecord b = this.bindings.valueAt(i3);
                b.dumpDebug(proto, 2246267895833L);
            }
        }
        if (this.connections.size() > 0) {
            int N4 = this.connections.size();
            for (int conni = 0; conni < N4; conni++) {
                java.util.ArrayList<com.android.server.am.ConnectionRecord> c = this.connections.valueAt(conni);
                for (int i4 = 0; i4 < c.size(); i4++) {
                    c.get(i4).dumpDebug(proto, 2246267895834L);
                }
            }
        }
        if (this.mShortFgsInfo != null && this.mShortFgsInfo.isCurrent()) {
            long shortFgsToken = proto.start(1146756268060L);
            proto.write(1112396529665L, this.mShortFgsInfo.getStartTime());
            proto.write(1120986464259L, this.mShortFgsInfo.getStartId());
            proto.write(1112396529668L, this.mShortFgsInfo.getTimeoutTime());
            proto.write(1112396529669L, this.mShortFgsInfo.getProcStateDemoteTime());
            proto.write(1112396529670L, this.mShortFgsInfo.getAnrTime());
            proto.end(shortFgsToken);
        }
        proto.end(token);
    }

    void dumpReasonCode(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String fieldName, int code) {
        pw.print(prefix);
        pw.print(fieldName);
        pw.print("=");
        pw.println(android.os.PowerExemptionManager.reasonCodeToString(code));
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("intent={");
        pw.print(this.intent.getIntent().toShortString(false, true, false, false));
        pw.println('}');
        pw.print(prefix);
        pw.print("packageName=");
        pw.println(this.packageName);
        pw.print(prefix);
        pw.print("processName=");
        pw.println(this.processName);
        pw.print(prefix);
        pw.print("targetSdkVersion=");
        pw.println(this.appInfo.targetSdkVersion);
        if (this.permission != null) {
            pw.print(prefix);
            pw.print("permission=");
            pw.println(this.permission);
        }
        long now = android.os.SystemClock.uptimeMillis();
        long nowReal = android.os.SystemClock.elapsedRealtime();
        if (this.appInfo != null) {
            pw.print(prefix);
            pw.print("baseDir=");
            pw.println(this.appInfo.sourceDir);
            if (!java.util.Objects.equals(this.appInfo.sourceDir, this.appInfo.publicSourceDir)) {
                pw.print(prefix);
                pw.print("resDir=");
                pw.println(this.appInfo.publicSourceDir);
            }
            pw.print(prefix);
            pw.print("dataDir=");
            pw.println(this.appInfo.dataDir);
        }
        pw.print(prefix);
        pw.print("app=");
        pw.println(this.app);
        if (this.isolationHostProc != null) {
            pw.print(prefix);
            pw.print("isolationHostProc=");
            pw.println(this.isolationHostProc);
        }
        if (this.allowlistManager) {
            pw.print(prefix);
            pw.print("allowlistManager=");
            pw.println(this.allowlistManager);
        }
        if (this.mIsAllowedBgActivityStartsByBinding) {
            pw.print(prefix);
            pw.print("mIsAllowedBgActivityStartsByBinding=");
            pw.println(this.mIsAllowedBgActivityStartsByBinding);
        }
        if (this.mBackgroundStartPrivilegesByStartMerged.allowsAny()) {
            pw.print(prefix);
            pw.print("mIsAllowedBgActivityStartsByStart=");
            pw.println(this.mBackgroundStartPrivilegesByStartMerged);
        }
        pw.print(prefix);
        pw.print("useNewWiuLogic_forCapabilities()=");
        pw.println(useNewWiuLogic_forCapabilities());
        pw.print(prefix);
        pw.print("useNewWiuLogic_forStart()=");
        pw.println(useNewWiuLogic_forStart());
        pw.print(prefix);
        pw.print("useNewBfslLogic()=");
        pw.println(useNewBfslLogic());
        dumpReasonCode(pw, prefix, "mAllowWiu_noBinding", this.mAllowWiu_noBinding);
        dumpReasonCode(pw, prefix, "mAllowWiu_inBindService", this.mAllowWiu_inBindService);
        dumpReasonCode(pw, prefix, "mAllowWiu_byBindings", this.mAllowWiu_byBindings);
        dumpReasonCode(pw, prefix, "getFgsAllowWiu_legacy", getFgsAllowWiu_legacy());
        dumpReasonCode(pw, prefix, "getFgsAllowWiu_new", getFgsAllowWiu_new());
        dumpReasonCode(pw, prefix, "getFgsAllowWiu_forStart", getFgsAllowWiu_forStart());
        dumpReasonCode(pw, prefix, "getFgsAllowWiu_forCapabilities", getFgsAllowWiu_forCapabilities());
        pw.print(prefix);
        pw.print("allowUiJobScheduling=");
        pw.println(this.mAllowUiJobScheduling);
        pw.print(prefix);
        pw.print("recentCallingPackage=");
        pw.println(this.mRecentCallingPackage);
        pw.print(prefix);
        pw.print("recentCallingUid=");
        pw.println(this.mRecentCallingUid);
        dumpReasonCode(pw, prefix, "mAllowStart_noBinding", this.mAllowStart_noBinding);
        dumpReasonCode(pw, prefix, "mAllowStart_inBindService", this.mAllowStart_inBindService);
        dumpReasonCode(pw, prefix, "mAllowStart_byBindings", this.mAllowStart_byBindings);
        dumpReasonCode(pw, prefix, "getFgsAllowStart_legacy", getFgsAllowStart_legacy());
        dumpReasonCode(pw, prefix, "getFgsAllowStart_new", getFgsAllowStart_new());
        dumpReasonCode(pw, prefix, "getFgsAllowStart", getFgsAllowStart());
        pw.print(prefix);
        pw.print("startForegroundCount=");
        pw.println(this.mStartForegroundCount);
        pw.print(prefix);
        pw.print("infoAllowStartForeground=");
        pw.println(this.mInfoAllowStartForeground);
        if (this.delayed) {
            pw.print(prefix);
            pw.print("delayed=");
            pw.println(this.delayed);
        }
        if (this.isForeground || this.foregroundId != 0) {
            pw.print(prefix);
            pw.print("isForeground=");
            pw.print(this.isForeground);
            pw.print(" foregroundId=");
            pw.print(this.foregroundId);
            pw.printf(" types=0x%08X", java.lang.Integer.valueOf(this.foregroundServiceType));
            pw.print(" foregroundNoti=");
            pw.println(this.foregroundNoti);
            if (isShortFgs() && this.mShortFgsInfo != null) {
                pw.print(prefix);
                pw.print("isShortFgs=true");
                pw.print(" startId=");
                pw.print(this.mShortFgsInfo.getStartId());
                pw.print(" startForegroundCount=");
                pw.print(this.mShortFgsInfo.getStartForegroundCount());
                pw.print(" startTime=");
                android.util.TimeUtils.formatDuration(this.mShortFgsInfo.getStartTime(), now, pw);
                pw.print(" timeout=");
                android.util.TimeUtils.formatDuration(this.mShortFgsInfo.getTimeoutTime(), now, pw);
                pw.print(" demoteTime=");
                android.util.TimeUtils.formatDuration(this.mShortFgsInfo.getProcStateDemoteTime(), now, pw);
                pw.print(" anrTime=");
                android.util.TimeUtils.formatDuration(this.mShortFgsInfo.getAnrTime(), now, pw);
                pw.println();
            }
        }
        if (this.mIsFgsDelegate) {
            pw.print(prefix);
            pw.print("isFgsDelegate=");
            pw.println(this.mIsFgsDelegate);
        }
        pw.print(prefix);
        pw.print("createTime=");
        android.util.TimeUtils.formatDuration(this.createRealTime, nowReal, pw);
        pw.print(" startingBgTimeout=");
        android.util.TimeUtils.formatDuration(this.startingBgTimeout, now, pw);
        pw.println();
        pw.print(prefix);
        pw.print("lastActivity=");
        android.util.TimeUtils.formatDuration(this.lastActivity, now, pw);
        pw.print(" restartTime=");
        android.util.TimeUtils.formatDuration(this.restartTime, now, pw);
        pw.print(" createdFromFg=");
        pw.println(this.createdFromFg);
        if (this.pendingConnectionGroup != 0) {
            pw.print(prefix);
            pw.print(" pendingConnectionGroup=");
            pw.print(this.pendingConnectionGroup);
            pw.print(" Importance=");
            pw.println(this.pendingConnectionImportance);
        }
        if (this.startRequested || this.delayedStop || this.lastStartId != 0) {
            pw.print(prefix);
            pw.print("startRequested=");
            pw.print(this.startRequested);
            pw.print(" delayedStop=");
            pw.print(this.delayedStop);
            pw.print(" stopIfKilled=");
            pw.print(this.stopIfKilled);
            pw.print(" callStart=");
            pw.print(this.callStart);
            pw.print(" lastStartId=");
            pw.println(this.lastStartId);
            pw.print(" startCommandResult=");
            pw.println(this.startCommandResult);
        }
        if (this.executeNesting != 0) {
            pw.print(prefix);
            pw.print("executeNesting=");
            pw.print(this.executeNesting);
            pw.print(" executeFg=");
            pw.print(this.executeFg);
            pw.print(" executingStart=");
            android.util.TimeUtils.formatDuration(this.executingStart, now, pw);
            pw.println();
        }
        if (this.destroying || this.destroyTime != 0) {
            pw.print(prefix);
            pw.print("destroying=");
            pw.print(this.destroying);
            pw.print(" destroyTime=");
            android.util.TimeUtils.formatDuration(this.destroyTime, now, pw);
            pw.println();
        }
        if (this.crashCount != 0 || this.restartCount != 0 || this.nextRestartTime - this.mRestartSchedulingTime != 0 || this.nextRestartTime != 0) {
            pw.print(prefix);
            pw.print("restartCount=");
            pw.print(this.restartCount);
            pw.print(" restartDelay=");
            android.util.TimeUtils.formatDuration(this.nextRestartTime - this.mRestartSchedulingTime, now, pw);
            pw.print(" nextRestartTime=");
            android.util.TimeUtils.formatDuration(this.nextRestartTime, now, pw);
            pw.print(" crashCount=");
            pw.println(this.crashCount);
        }
        if (this.deliveredStarts.size() > 0) {
            pw.print(prefix);
            pw.println("Delivered Starts:");
            dumpStartList(pw, prefix, this.deliveredStarts, now);
        }
        if (this.pendingStarts.size() > 0) {
            pw.print(prefix);
            pw.println("Pending Starts:");
            dumpStartList(pw, prefix, this.pendingStarts, 0L);
        }
        if (this.bindings.size() > 0) {
            pw.print(prefix);
            pw.println("Bindings:");
            for (int i = 0; i < this.bindings.size(); i++) {
                com.android.server.am.IntentBindRecord b = this.bindings.valueAt(i);
                pw.print(prefix);
                pw.print("* IntentBindRecord{");
                pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(b)));
                if ((b.collectFlags() & 1) != 0) {
                    pw.append(" CREATE");
                }
                pw.println("}:");
                b.dumpInService(pw, prefix + "  ");
            }
        }
        if (this.connections.size() > 0) {
            pw.print(prefix);
            pw.println("All Connections:");
            for (int conni = 0; conni < this.connections.size(); conni++) {
                java.util.ArrayList<com.android.server.am.ConnectionRecord> c = this.connections.valueAt(conni);
                for (int i2 = 0; i2 < c.size(); i2++) {
                    pw.print(prefix);
                    pw.print("  ");
                    pw.println(c.get(i2));
                }
            }
        }
        this.mServiceRecordExt.hookEndOfDump(pw, prefix);
    }

    private ServiceRecord(com.android.server.am.ActivityManagerService ams) {
        this.bindings = new android.util.ArrayMap<>();
        this.connections = new android.util.ArrayMap<>();
        this.mBackgroundStartPrivilegesByStart = new java.util.ArrayList<>();
        this.mBackgroundStartPrivilegesByStartMerged = android.app.BackgroundStartPrivileges.NONE;
        this.mAllowWiu_noBinding = -1;
        this.mFgsEnterTime = 0L;
        this.mFgsExitTime = 0L;
        this.mAllowStart_noBinding = -1;
        this.mAllowStartForegroundAtEntering = -1;
        this.mAllowWiu_inBindService = -1;
        this.mAllowWiu_byBindings = -1;
        this.mAllowStart_inBindService = -1;
        this.mAllowStart_byBindings = -1;
        this.mServiceRecordExt = (com.android.server.am.IServiceRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IServiceRecordExt.class).base(this).create();
        this.deliveredStarts = new java.util.ArrayList<>();
        this.pendingStarts = new java.util.ArrayList<>();
        this.ams = ams;
        this.name = null;
        this.instanceName = null;
        this.shortInstanceName = null;
        this.definingPackageName = null;
        this.definingUid = 0;
        this.intent = null;
        this.serviceInfo = null;
        this.userId = 0;
        this.packageName = null;
        this.processName = null;
        this.permission = null;
        this.exported = false;
        this.restarter = null;
        this.createRealTime = 0L;
        this.isSdkSandbox = false;
        this.sdkSandboxClientAppUid = 0;
        this.sdkSandboxClientAppPackage = null;
        this.inSharedIsolatedProcess = false;
    }

    public static com.android.server.am.ServiceRecord newEmptyInstanceForTest(com.android.server.am.ActivityManagerService ams) {
        return new com.android.server.am.ServiceRecord(ams);
    }

    ServiceRecord(com.android.server.am.ActivityManagerService ams, android.content.ComponentName name, android.content.ComponentName instanceName, java.lang.String definingPackageName, int definingUid, android.content.Intent.FilterComparison intent, android.content.pm.ServiceInfo sInfo, boolean callerIsFg, java.lang.Runnable restarter) {
        this(ams, name, instanceName, definingPackageName, definingUid, intent, sInfo, callerIsFg, restarter, sInfo.processName, -1, null, false);
    }

    ServiceRecord(com.android.server.am.ActivityManagerService ams, android.content.ComponentName name, android.content.ComponentName instanceName, java.lang.String definingPackageName, int definingUid, android.content.Intent.FilterComparison intent, android.content.pm.ServiceInfo sInfo, boolean callerIsFg, java.lang.Runnable restarter, java.lang.String processName, int sdkSandboxClientAppUid, java.lang.String sdkSandboxClientAppPackage, boolean inSharedIsolatedProcess) {
        this.bindings = new android.util.ArrayMap<>();
        this.connections = new android.util.ArrayMap<>();
        this.mBackgroundStartPrivilegesByStart = new java.util.ArrayList<>();
        this.mBackgroundStartPrivilegesByStartMerged = android.app.BackgroundStartPrivileges.NONE;
        this.mAllowWiu_noBinding = -1;
        this.mFgsEnterTime = 0L;
        this.mFgsExitTime = 0L;
        this.mAllowStart_noBinding = -1;
        this.mAllowStartForegroundAtEntering = -1;
        this.mAllowWiu_inBindService = -1;
        this.mAllowWiu_byBindings = -1;
        this.mAllowStart_inBindService = -1;
        this.mAllowStart_byBindings = -1;
        this.mServiceRecordExt = (com.android.server.am.IServiceRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IServiceRecordExt.class).base(this).create();
        this.deliveredStarts = new java.util.ArrayList<>();
        this.pendingStarts = new java.util.ArrayList<>();
        this.ams = ams;
        this.name = name;
        this.instanceName = instanceName;
        this.shortInstanceName = instanceName.flattenToShortString();
        this.definingPackageName = definingPackageName;
        this.definingUid = definingUid;
        this.intent = intent;
        this.serviceInfo = sInfo;
        this.appInfo = sInfo.applicationInfo;
        this.packageName = sInfo.applicationInfo.packageName;
        this.isSdkSandbox = sdkSandboxClientAppUid != -1;
        this.sdkSandboxClientAppUid = sdkSandboxClientAppUid;
        this.sdkSandboxClientAppPackage = sdkSandboxClientAppPackage;
        this.inSharedIsolatedProcess = inSharedIsolatedProcess;
        this.processName = processName;
        this.permission = sInfo.permission;
        this.exported = sInfo.exported;
        this.restarter = restarter;
        this.createRealTime = android.os.SystemClock.elapsedRealtime();
        this.lastActivity = android.os.SystemClock.uptimeMillis();
        this.userId = android.os.UserHandle.getUserId(this.appInfo.uid);
        this.createdFromFg = callerIsFg;
        updateKeepWarmLocked();
        updateFgsHasNotificationPermission();
    }

    public com.android.internal.app.procstats.ServiceState getTracker() {
        if (this.tracker != null) {
            return this.tracker;
        }
        if ((this.serviceInfo.applicationInfo.flags & 8) == 0) {
            this.tracker = this.ams.mProcessStats.getServiceState(this.serviceInfo.packageName, this.serviceInfo.applicationInfo.uid, this.serviceInfo.applicationInfo.longVersionCode, this.serviceInfo.processName, this.serviceInfo.name);
            if (this.tracker != null) {
                this.tracker.applyNewOwner(this);
            }
        }
        return this.tracker;
    }

    public void forceClearTracker() {
        if (this.tracker != null) {
            this.tracker.clearCurrentOwner(this, true);
            this.tracker = null;
        }
    }

    public void makeRestarting(int memFactor, long now) {
        if (this.restartTracker == null) {
            if ((this.serviceInfo.applicationInfo.flags & 8) == 0) {
                this.restartTracker = this.ams.mProcessStats.getServiceState(this.serviceInfo.packageName, this.serviceInfo.applicationInfo.uid, this.serviceInfo.applicationInfo.longVersionCode, this.serviceInfo.processName, this.serviceInfo.name);
            }
            if (this.restartTracker == null) {
                return;
            }
        }
        this.restartTracker.setRestarting(true, memFactor, now);
    }

    public void setProcess(com.android.server.am.ProcessRecord proc, android.app.IApplicationThread thread, int pid, com.android.server.am.UidRecord uidRecord) {
        if (proc != null) {
            if (this.mAppForAllowingBgActivityStartsByStart != null && this.mAppForAllowingBgActivityStartsByStart != proc) {
                this.mAppForAllowingBgActivityStartsByStart.removeBackgroundStartPrivileges(this);
                this.ams.mHandler.removeCallbacks(this.mCleanUpAllowBgActivityStartsByStartCallback);
            }
            this.mAppForAllowingBgActivityStartsByStart = this.mBackgroundStartPrivilegesByStartMerged.allowsAny() ? proc : null;
            android.app.BackgroundStartPrivileges backgroundStartPrivileges = getBackgroundStartPrivilegesWithExclusiveToken();
            if (backgroundStartPrivileges.allowsAny()) {
                proc.addOrUpdateBackgroundStartPrivileges(this, backgroundStartPrivileges);
            } else {
                proc.removeBackgroundStartPrivileges(this);
            }
        }
        if (this.app != null && this.app != proc) {
            if (this.mBackgroundStartPrivilegesByStartMerged.allowsNothing()) {
                this.app.removeBackgroundStartPrivileges(this);
            }
            this.app.mServices.updateBoundClientUids();
            this.app.mServices.updateHostingComonentTypeForBindingsLocked();
        }
        this.app = proc;
        updateProcessStateOnRequest();
        if (this.pendingConnectionGroup > 0 && proc != null) {
            com.android.server.am.ProcessServiceRecord psr = proc.mServices;
            psr.setConnectionService(this);
            psr.setConnectionGroup(this.pendingConnectionGroup);
            psr.setConnectionImportance(this.pendingConnectionImportance);
            this.pendingConnectionImportance = 0;
            this.pendingConnectionGroup = 0;
        }
        for (int conni = this.connections.size() - 1; conni >= 0; conni--) {
            java.util.ArrayList<com.android.server.am.ConnectionRecord> cr = this.connections.valueAt(conni);
            for (int i = 0; i < cr.size(); i++) {
                com.android.server.am.ConnectionRecord conn = cr.get(i);
                if (proc != null) {
                    conn.startAssociationIfNeeded();
                } else {
                    conn.stopAssociation();
                }
            }
        }
        if (proc != null) {
            proc.mServices.updateBoundClientUids();
            proc.mServices.updateHostingComonentTypeForBindingsLocked();
        }
    }

    void updateProcessStateOnRequest() {
        this.mProcessStateOnRequest = (this.app == null || this.app.getThread() == null || this.app.isKilled()) ? 20 : this.app.mState.getCurProcState();
    }

    android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> getConnections() {
        return this.connections;
    }

    void addConnection(android.os.IBinder binder, com.android.server.am.ConnectionRecord c) {
        java.util.ArrayList<com.android.server.am.ConnectionRecord> clist = this.connections.get(binder);
        if (clist == null) {
            clist = new java.util.ArrayList<>();
            this.connections.put(binder, clist);
        }
        clist.add(c);
        if (this.app != null) {
            this.app.mServices.addBoundClientUid(c.clientUid, c.clientPackageName, c.getFlags());
            this.app.mProfile.addHostingComponentType(512);
        }
    }

    void removeConnection(android.os.IBinder binder) {
        this.connections.remove(binder);
        if (this.app != null) {
            this.app.mServices.updateBoundClientUids();
            this.app.mServices.updateHostingComonentTypeForBindingsLocked();
        }
    }

    boolean canStopIfKilled(boolean isStartCanceled) {
        if (isShortFgs()) {
            return true;
        }
        return this.startRequested && (this.stopIfKilled || isStartCanceled) && this.pendingStarts.isEmpty();
    }

    void updateIsAllowedBgActivityStartsByBinding() {
        boolean isAllowedByBinding = false;
        for (int conni = this.connections.size() - 1; conni >= 0; conni--) {
            java.util.ArrayList<com.android.server.am.ConnectionRecord> cr = this.connections.valueAt(conni);
            int i = 0;
            while (true) {
                if (i >= cr.size()) {
                    break;
                }
                if (!cr.get(i).hasFlag(1048576)) {
                    i++;
                } else {
                    isAllowedByBinding = true;
                    break;
                }
            }
            if (isAllowedByBinding) {
                break;
            }
        }
        setAllowedBgActivityStartsByBinding(isAllowedByBinding);
    }

    void setAllowedBgActivityStartsByBinding(boolean newValue) {
        this.mIsAllowedBgActivityStartsByBinding = newValue;
        updateParentProcessBgActivityStartsToken();
    }

    void allowBgActivityStartsOnServiceStart(android.app.BackgroundStartPrivileges backgroundStartPrivileges) {
        com.android.internal.util.Preconditions.checkArgument(backgroundStartPrivileges.allowsAny());
        this.mBackgroundStartPrivilegesByStart.add(backgroundStartPrivileges);
        setAllowedBgActivityStartsByStart(backgroundStartPrivileges.merge(this.mBackgroundStartPrivilegesByStartMerged));
        if (this.app != null) {
            this.mAppForAllowingBgActivityStartsByStart = this.app;
        }
        if (this.mCleanUpAllowBgActivityStartsByStartCallback == null) {
            this.mCleanUpAllowBgActivityStartsByStartCallback = new java.lang.Runnable() { // from class: com.android.server.am.ServiceRecord$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$allowBgActivityStartsOnServiceStart$0();
                }
            };
        }
        this.ams.mHandler.postDelayed(this.mCleanUpAllowBgActivityStartsByStartCallback, this.ams.mConstants.SERVICE_BG_ACTIVITY_START_TIMEOUT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$allowBgActivityStartsOnServiceStart$0() {
        com.android.server.am.ActivityManagerService activityManagerService = this.ams;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mBackgroundStartPrivilegesByStart.remove(0);
                if (!this.mBackgroundStartPrivilegesByStart.isEmpty()) {
                    this.mBackgroundStartPrivilegesByStartMerged = android.app.BackgroundStartPrivileges.merge(this.mBackgroundStartPrivilegesByStart);
                    if (this.mBackgroundStartPrivilegesByStartMerged.allowsAny()) {
                        if (this.mAppForAllowingBgActivityStartsByStart != null) {
                            this.mAppForAllowingBgActivityStartsByStart.addOrUpdateBackgroundStartPrivileges(this, getBackgroundStartPrivilegesWithExclusiveToken());
                        }
                    } else {
                        android.util.Slog.wtf("ActivityManager", "Service callback to revoke bg activity starts by service start triggered but mBackgroundStartPrivilegesByStartMerged = " + this.mBackgroundStartPrivilegesByStartMerged + ". This should never happen.");
                    }
                } else {
                    if (this.app == this.mAppForAllowingBgActivityStartsByStart) {
                        setAllowedBgActivityStartsByStart(android.app.BackgroundStartPrivileges.NONE);
                    } else if (this.mAppForAllowingBgActivityStartsByStart != null) {
                        this.mAppForAllowingBgActivityStartsByStart.removeBackgroundStartPrivileges(this);
                    }
                    this.mAppForAllowingBgActivityStartsByStart = null;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    void updateAllowUiJobScheduling(boolean allowUiJobScheduling) {
        if (this.mAllowUiJobScheduling == allowUiJobScheduling) {
            return;
        }
        this.mAllowUiJobScheduling = allowUiJobScheduling;
    }

    private void setAllowedBgActivityStartsByStart(android.app.BackgroundStartPrivileges newValue) {
        if (this.mBackgroundStartPrivilegesByStartMerged == newValue) {
            return;
        }
        this.mBackgroundStartPrivilegesByStartMerged = newValue;
        updateParentProcessBgActivityStartsToken();
    }

    private void updateParentProcessBgActivityStartsToken() {
        if (this.app == null) {
            return;
        }
        android.app.BackgroundStartPrivileges backgroundStartPrivileges = getBackgroundStartPrivilegesWithExclusiveToken();
        if (backgroundStartPrivileges.allowsAny()) {
            this.app.addOrUpdateBackgroundStartPrivileges(this, backgroundStartPrivileges);
        } else {
            this.app.removeBackgroundStartPrivileges(this);
        }
    }

    private android.app.BackgroundStartPrivileges getBackgroundStartPrivilegesWithExclusiveToken() {
        if (this.mIsAllowedBgActivityStartsByBinding) {
            return android.app.BackgroundStartPrivileges.ALLOW_BAL;
        }
        if (this.mBackgroundStartPrivilegesByStart.isEmpty()) {
            return android.app.BackgroundStartPrivileges.NONE;
        }
        return this.mBackgroundStartPrivilegesByStartMerged;
    }

    void updateKeepWarmLocked() {
        this.mKeepWarming = this.ams.mConstants.KEEP_WARMING_SERVICES.contains(this.name) && (this.ams.mUserController.getCurrentUserId() == this.userId || this.ams.mUserController.isCurrentProfile(this.userId) || this.ams.isSingleton(this.processName, this.appInfo, this.instanceName.getClassName(), this.serviceInfo.flags));
    }

    public com.android.server.am.AppBindRecord retrieveAppBindingLocked(android.content.Intent intent, com.android.server.am.ProcessRecord app, com.android.server.am.ProcessRecord attributedApp) {
        android.content.Intent.FilterComparison filter = new android.content.Intent.FilterComparison(intent);
        com.android.server.am.IntentBindRecord i = this.bindings.get(filter);
        if (i == null) {
            i = new com.android.server.am.IntentBindRecord(this, filter);
            this.bindings.put(filter, i);
        }
        com.android.server.am.AppBindRecord a = i.apps.get(app);
        if (a != null) {
            return a;
        }
        com.android.server.am.AppBindRecord a2 = new com.android.server.am.AppBindRecord(this, i, app, attributedApp);
        i.apps.put(app, a2);
        return a2;
    }

    public boolean hasAutoCreateConnections() {
        for (int conni = this.connections.size() - 1; conni >= 0; conni--) {
            java.util.ArrayList<com.android.server.am.ConnectionRecord> cr = this.connections.valueAt(conni);
            for (int i = 0; i < cr.size(); i++) {
                if (cr.get(i).hasFlag(1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateAllowlistManager() {
        this.allowlistManager = false;
        for (int conni = this.connections.size() - 1; conni >= 0; conni--) {
            java.util.ArrayList<com.android.server.am.ConnectionRecord> cr = this.connections.valueAt(conni);
            for (int i = 0; i < cr.size(); i++) {
                if (cr.get(i).hasFlag(16777216)) {
                    this.allowlistManager = true;
                    return;
                }
            }
        }
    }

    public void resetRestartCounter() {
        this.restartCount = 0;
        this.restartDelay = 0L;
        this.restartTime = 0L;
        this.mEarliestRestartTime = 0L;
        this.mRestartSchedulingTime = 0L;
    }

    public com.android.server.am.ServiceRecord.StartItem findDeliveredStart(int id, boolean taskRemoved, boolean remove) {
        int N = this.deliveredStarts.size();
        for (int i = 0; i < N; i++) {
            com.android.server.am.ServiceRecord.StartItem si = this.deliveredStarts.get(i);
            if (si.id == id && si.taskRemoved == taskRemoved) {
                if (remove) {
                    this.deliveredStarts.remove(i);
                }
                return si;
            }
        }
        return null;
    }

    public int getLastStartId() {
        return this.lastStartId;
    }

    public int makeNextStartId() {
        this.lastStartId++;
        if (this.lastStartId < 1) {
            this.lastStartId = 1;
        }
        return this.lastStartId;
    }

    private void updateFgsHasNotificationPermission() {
        final java.lang.String localPackageName = this.packageName;
        final int appUid = this.appInfo.uid;
        this.ams.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ServiceRecord.1
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.notification.NotificationManagerInternal nm = (com.android.server.notification.NotificationManagerInternal) com.android.server.LocalServices.getService(com.android.server.notification.NotificationManagerInternal.class);
                if (nm == null) {
                    return;
                }
                com.android.server.am.ServiceRecord.this.mFgsHasNotificationPermission = nm.areNotificationsEnabledForPackage(localPackageName, appUid);
            }
        });
    }

    public void postNotification(final boolean byForegroundService) {
        if (this.isForeground && this.foregroundNoti != null && this.app != null) {
            final int appUid = this.appInfo.uid;
            final int appPid = this.app.getPid();
            final java.lang.String localPackageName = this.packageName;
            final int localForegroundId = this.foregroundId;
            final android.app.Notification _foregroundNoti = this.foregroundNoti;
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                android.util.Slog.d("ActivityManager", "Posting notification " + _foregroundNoti + " for foreground service " + this);
            }
            this.ams.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ServiceRecord.2
                /* JADX WARN: Removed duplicated region for block: B:20:0x0123  */
                /* JADX WARN: Removed duplicated region for block: B:31:0x016b A[Catch: RuntimeException -> 0x01b5, TryCatch #0 {RuntimeException -> 0x01b5, blocks: (B:18:0x0115, B:21:0x0125, B:27:0x0147, B:28:0x0164, B:29:0x0165, B:31:0x016b, B:32:0x0197, B:33:0x01b4), top: B:39:0x0115 }] */
                /* JADX WARN: Removed duplicated region for block: B:32:0x0197 A[Catch: RuntimeException -> 0x01b5, TryCatch #0 {RuntimeException -> 0x01b5, blocks: (B:18:0x0115, B:21:0x0125, B:27:0x0147, B:28:0x0164, B:29:0x0165, B:31:0x016b, B:32:0x0197, B:33:0x01b4), top: B:39:0x0115 }] */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public void run() {
                    /*
                        Method dump skipped, instruction units count: 466
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ServiceRecord.AnonymousClass2.run():void");
                }
            });
        }
    }

    public void cancelNotification() {
        final java.lang.String localPackageName = this.packageName;
        final int localForegroundId = this.foregroundId;
        final int appUid = this.appInfo.uid;
        final int appPid = this.app != null ? this.app.getPid() : 0;
        this.ams.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ServiceRecord.3
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.notification.NotificationManagerInternal nm = (com.android.server.notification.NotificationManagerInternal) com.android.server.LocalServices.getService(com.android.server.notification.NotificationManagerInternal.class);
                if (nm == null) {
                    return;
                }
                try {
                    nm.cancelNotification(localPackageName, localPackageName, appUid, appPid, null, localForegroundId, com.android.server.am.ServiceRecord.this.userId);
                } catch (java.lang.RuntimeException e) {
                    android.util.Slog.w("ActivityManager", "Error canceling notification for service", e);
                }
                com.android.server.am.ServiceRecord.this.signalForegroundServiceNotification(com.android.server.am.ServiceRecord.this.packageName, com.android.server.am.ServiceRecord.this.appInfo.uid, localForegroundId, true);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void signalForegroundServiceNotification(java.lang.String packageName, int uid, int foregroundId, boolean canceling) {
        com.android.server.am.ActivityManagerService activityManagerService = this.ams;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                for (int i = this.ams.mForegroundServiceStateListeners.size() - 1; i >= 0; i--) {
                    this.ams.mForegroundServiceStateListeners.get(i).onForegroundServiceNotificationUpdated(packageName, this.appInfo.uid, foregroundId, canceling);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    public void stripForegroundServiceFlagFromNotification() {
        final int localForegroundId = this.foregroundId;
        final int localUserId = this.userId;
        final java.lang.String localPackageName = this.packageName;
        this.ams.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ServiceRecord.4
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.notification.NotificationManagerInternal nmi = (com.android.server.notification.NotificationManagerInternal) com.android.server.LocalServices.getService(com.android.server.notification.NotificationManagerInternal.class);
                if (nmi == null) {
                    return;
                }
                nmi.removeForegroundServiceFlagFromNotification(localPackageName, localForegroundId, localUserId);
            }
        });
    }

    public void clearDeliveredStartsLocked() {
        for (int i = this.deliveredStarts.size() - 1; i >= 0; i--) {
            this.deliveredStarts.get(i).removeUriPermissionsLocked();
        }
        this.deliveredStarts.clear();
    }

    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("ServiceRecord{").append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))).append(" u").append(this.userId).append(' ').append(this.shortInstanceName);
        if (this.mRecentCallingPackage != null) {
            sb.append(" c:").append(this.mRecentCallingPackage);
        }
        sb.append('}');
        java.lang.String string = sb.toString();
        this.stringName = string;
        return string;
    }

    public android.content.ComponentName getComponentName() {
        return this.name;
    }

    public boolean isShortFgs() {
        return this.startRequested && this.isForeground && this.foregroundServiceType == 2048;
    }

    public com.android.server.am.ServiceRecord.ShortFgsInfo getShortFgsInfo() {
        if (isShortFgs()) {
            return this.mShortFgsInfo;
        }
        return null;
    }

    public void setShortFgsInfo(long uptimeNow) {
        this.mShortFgsInfo = new com.android.server.am.ServiceRecord.ShortFgsInfo(uptimeNow);
    }

    public boolean hasShortFgsInfo() {
        return this.mShortFgsInfo != null;
    }

    public void clearShortFgsInfo() {
        this.mShortFgsInfo = null;
    }

    private boolean shouldTriggerShortFgsTimedEvent(long targetTime, long nowUptime) {
        return isAppAlive() && this.startRequested && isShortFgs() && this.mShortFgsInfo != null && this.mShortFgsInfo.isCurrent() && targetTime <= nowUptime;
    }

    public boolean shouldTriggerShortFgsTimeout(long nowUptime) {
        return shouldTriggerShortFgsTimedEvent(this.mShortFgsInfo == null ? 0L : this.mShortFgsInfo.getTimeoutTime(), nowUptime);
    }

    public boolean shouldDemoteShortFgsProcState(long nowUptime) {
        return shouldTriggerShortFgsTimedEvent(this.mShortFgsInfo == null ? 0L : this.mShortFgsInfo.getProcStateDemoteTime(), nowUptime);
    }

    public boolean shouldTriggerShortFgsAnr(long nowUptime) {
        return shouldTriggerShortFgsTimedEvent(this.mShortFgsInfo == null ? 0L : this.mShortFgsInfo.getAnrTime(), nowUptime);
    }

    public java.lang.String getShortFgsTimedEventDescription(long nowUptime) {
        return "aa=" + isAppAlive() + " sreq=" + this.startRequested + " isfg=" + this.isForeground + " type=" + java.lang.Integer.toHexString(this.foregroundServiceType) + " sfc=" + this.mStartForegroundCount + " now=" + nowUptime + " " + (this.mShortFgsInfo == null ? "" : this.mShortFgsInfo.getDescription());
    }

    public com.android.server.am.ServiceRecord.TimeLimitedFgsInfo createTimeLimitedFgsInfo() {
        return new com.android.server.am.ServiceRecord.TimeLimitedFgsInfo();
    }

    public boolean isFgsTimeLimited() {
        return this.startRequested && this.isForeground && this.ams.mServices.getTimeLimitedFgsType(this.foregroundServiceType) != 0;
    }

    private boolean isAppAlive() {
        return (this.app == null || this.app.getThread() == null || this.app.isKilled() || this.app.isKilledByAm()) ? false : true;
    }

    boolean wasOomAdjUpdated() {
        return this.app != null && this.app.mState.getAdjSeq() > this.mAdjSeq;
    }

    void updateOomAdjSeq() {
        if (this.app != null) {
            this.mAdjSeq = this.app.mState.getAdjSeq();
        }
    }
}
