package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IActivityManagerServiceExt {
    public static final int OPLUS_AMS_BG_HANDLER = 3;
    public static final int OPLUS_AMS_KILL_HANDLER = 4;
    public static final int OPLUS_AMS_MAIN_HANLDER = 1;
    public static final int OPLUS_AMS_MSG_INDEX = 500;
    public static final int OPLUS_AMS_UI_HANDLER = 2;
    public static final java.lang.String TAG = "ActivityManager";
    public static final int TYPE_BROADCAST = 1;
    public static final int TYPE_DUMP_HEAP = 1;
    public static final int TYPE_DUMP_MEM = 2;
    public static final int TYPE_SERVICE = 2;
    public static final int POWER_KEY_DUMP = android.os.SystemProperties.getInt("persist.sys.powerkeydump", 0);
    public static final boolean DEBUG_OPLUS_AMS = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);

    default void onOplusStart() {
    }

    default void onOplusSystemReady() {
    }

    default void handleOplusMessage(android.os.Message msg, int whichHandler) {
    }

    default java.util.List<java.lang.String> getAllTopPkgName() {
        return null;
    }

    default android.content.pm.ApplicationInfo getFreeFormAppInfo() {
        return null;
    }

    default java.util.List<android.content.pm.ApplicationInfo> getAllTopAppInfo() {
        return null;
    }

    default android.content.ComponentName getDockTopAppName() {
        return null;
    }

    default int getWindowMode(android.os.IBinder token) throws android.os.RemoteException {
        return 0;
    }

    default int startActivityForFreeform(android.content.Intent intent, android.os.Bundle bOptions, int userId, java.lang.String callPkg) {
        return -1;
    }

    default void exitOplusFreeform(android.os.Bundle bOptions) {
    }

    default void addTimeInfo(java.lang.StringBuilder sb) {
    }

    default void setErrorPackageName(java.lang.String pkg) {
    }

    default int getDataFileSizeAjusted(int prevSize, int lineSize, java.io.File file) {
        return -1;
    }

    default void appendCpuInfo(java.lang.StringBuilder sb, java.lang.String eventType) {
    }

    default void setCrashProcessRecord(com.android.server.am.ProcessRecord processRecord) {
    }

    default void collectExceptionStatistics(java.lang.SecurityException ex1, java.lang.String callerPackage) {
    }

    default void reportBindApplicationFinished(java.lang.String pkgName, int userId, int pid) {
    }

    default void publishOplusAmsInternal() {
    }

    default com.android.server.am.BroadcastQueue broadcastSpecialIntent(android.content.Intent intent, int callingUid, boolean isForOptimize, java.lang.String tagBroadcast) {
        return null;
    }

    default void cleanupAppByProvider(com.android.server.am.ActivityManagerService ams, com.android.server.am.ContentProviderRecord cpr, java.lang.String tagMu, int userId) {
    }

    default boolean shouldPrintOplusBroadcastLog(android.content.Intent intent) {
        return false;
    }

    default void initAmsExAndInner(android.content.Context systemContext, com.android.server.am.ActivityManagerService ams, com.android.server.wm.ActivityTaskManagerService atm) {
    }

    default void hookInterceptClearUserDataIfNeeded(java.lang.String pkgName) {
    }

    default void hookSystemReady(android.content.Context mUiContext, android.os.Handler mUiHandler, android.content.Context mContext, com.android.server.am.ActivityManagerService ams) {
    }

    default void hookAMSConstructEnd() {
    }

    default boolean hookOnTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        return false;
    }

    default void hookAddErrorToDropBox(android.content.Context context, java.lang.String dropboxTag, java.lang.String eventType, com.android.server.am.ProcessRecord process, java.lang.String subject, java.io.File dataFile, android.app.ApplicationErrorReport.CrashInfo crashInfo) {
        com.android.server.am.OplusCrashInfo oplusCrashIfno = new com.android.server.am.OplusCrashInfo();
        oplusCrashIfno.context = context;
        oplusCrashIfno.dropboxTag = dropboxTag;
        oplusCrashIfno.eventType = eventType;
        oplusCrashIfno.process = process;
        oplusCrashIfno.subject = subject;
        oplusCrashIfno.dataFile = dataFile;
        oplusCrashIfno.crashInfo = crashInfo;
        hookAddErrorToDropBox(oplusCrashIfno);
    }

    default void hookAddErrorToDropBox(com.android.server.am.OplusCrashInfo oplusCrashInfo) {
    }

    default void hookHandleAppNotResponding(com.android.server.am.OplusCrashInfo oplusCrashInfo) {
    }

    default void handleApplicationCrash(android.app.usage.UsageStatsManagerInternal mUsageStatsService, android.os.IBinder app, com.android.server.am.ProcessRecord r, int event, int pid) {
    }

    default void removeIsolatedUid(int isolatedUid, int appUid, java.lang.String packageName) {
    }

    default void updateDumpUid(int uid, boolean add, int type) {
    }

    default void hookBootCompleted() {
    }

    default boolean hookDoDump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, java.lang.String cmd) {
        return false;
    }

    default boolean dynamicLogDump(com.android.server.am.ActivityManagerService ams, java.lang.String cmd, java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti) {
        return false;
    }

    default boolean isAllowedCallerKillProcess(int callerUid) {
        return false;
    }

    default void optimizeVoldMsg(android.os.Handler mHandler, android.os.Message msg, java.lang.String reason) {
    }

    default void setKeyLockModeNormal(android.content.Context context, java.lang.String processName, boolean systemReady) {
    }

    default void handleProcessDied(com.android.server.am.ProcessRecord app) {
    }

    default void recordAppCrash(java.lang.String eventType, com.android.server.am.ProcessRecord app) {
    }

    default void initBroadcastAndBootPressure(com.android.server.am.ActivityManagerService ams) {
    }

    default void instanceBroadcast(com.android.server.am.BroadcastConstants foreConstants, com.android.server.am.BroadcastConstants backConstants) {
    }

    default boolean isWaitingPermissionChoice(com.android.server.am.ProcessRecord proc) {
        return false;
    }

    default boolean handleServiceTimeOut(android.os.Message msg) {
        return false;
    }

    default void sendApplicationStop(android.os.Handler mHandler, android.content.Context mContext, java.lang.String processName, int reason) {
    }

    default void sendApplicationStopByForceStop(android.os.Handler mHandler, int callingPid, android.content.Context mContext, java.lang.String packageName, com.android.server.am.ActivityManagerService.PidMap mPidsSelfLocked) {
    }

    default void hookUpdateForegroundServiceState(int uid, java.lang.String pkgName, boolean isForeground) {
    }

    default void broadcastIntentLocked(android.content.Intent intent, int callingUid, java.lang.String callerPackage, int userId) {
    }

    default java.util.List<android.content.pm.ResolveInfo> collectReceivers(java.util.List<android.content.pm.ResolveInfo> receivers, android.content.Intent intent, java.lang.String resolvedType, int callingUid, int callingPid, int[] users, int[] broadcastWhitelist, java.lang.String callerPkg) {
        return receivers;
    }

    default java.util.List<com.android.server.am.BroadcastFilter> collectReceivers(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, android.content.Intent intent, int callingUid, int userId, int[] users, com.android.server.IntentResolver<com.android.server.am.BroadcastFilter, com.android.server.am.BroadcastFilter> resolver, java.lang.String resolvedType, java.util.List<com.android.server.am.BroadcastFilter> registeredReceivers) {
        return registeredReceivers;
    }

    default boolean killBackgroundProcessFilter(java.lang.String packageName, int callingUid) {
        return false;
    }

    default java.lang.String updateStopReasonIfNeeded(java.lang.String reason) {
        return reason;
    }

    default void notifyBindApplicationFinished(java.lang.String pkgName, int userId, int pid) {
    }

    default com.android.server.am.BroadcastQueue createOptimizeQueue(android.content.Intent intent, boolean isForOptimize, int callingUid) {
        return null;
    }

    default java.util.List adjustReceiverList(java.util.List receivers, android.content.Intent intent) {
        return receivers;
    }

    default java.util.List adjustQueueOrderedBroadcastLocked(com.android.server.am.BroadcastQueue queue, android.content.Intent intent, com.android.server.am.ProcessRecord callerApp, java.lang.String callerPackage, int callingPid, int callingUid, boolean callerInstantApp, java.lang.String resolvedType, java.lang.String[] requiredPermissions, java.lang.String[] excludedPermissions, java.lang.String[] excludedPackages, int appOp, android.app.BroadcastOptions options, java.util.List receivers, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, boolean serialized, boolean sticky, boolean initialSticky, int userId, android.app.BackgroundStartPrivileges backgroundStartPrivileges, boolean timeoutExempt) {
        return receivers;
    }

    default void scheduleNextDispatch(android.content.Intent intent) {
    }

    default com.android.server.am.BroadcastQueue getQueueFromFlag(int flags) {
        return null;
    }

    default void waitForDumpCondition(boolean isSystemServer, java.lang.String eventType) {
    }

    default void dumpBinderProxies(java.io.PrintWriter pw, int waterMark) {
    }

    default boolean isReceivingBroadcastLocked(com.android.server.am.ProcessRecord app) {
        return false;
    }

    default void debugReceiverIssue(boolean flag, int totalReceiversForApp) {
    }

    default void debugBroadcast(java.lang.String tag, android.content.Intent intent, boolean sticky, boolean ordered, int userId, android.content.IIntentReceiver resultTo, int callingPid, int callingUid) {
    }

    default boolean isOnBackgroundServiceWhitelist(java.lang.String packageName, int uid) {
        return false;
    }

    default void sendTheiaEvent(boolean foreground, java.lang.String processName, java.lang.String pkgName) {
    }

    default void onBackPressedOnTheiaMonitor(long pressNow) {
    }

    default void sendTheiaEvent(long category, android.content.Intent args) {
    }

    default void setBootstage() {
    }

    default void hookHandleApplicationCrashBeforeInner(com.android.server.am.ProcessRecord r, android.app.ApplicationErrorReport.CrashInfo crashInfo) {
    }

    default void hookHandleApplicationCrashDialog(com.android.server.am.ProcessRecord r, com.android.server.am.AppErrorDialog.Data data) {
    }

    default boolean isChinaModel() {
        return false;
    }

    default void hookCleanUpApplicationRecordAfterRestartProc(com.android.server.am.ProcessRecord app) {
    }

    default boolean setRestartBeforeRestartProc(boolean restart, com.android.server.am.ProcessRecord app) {
        return restart;
    }

    default void hookBindBackupAgentAfterStartProc(com.android.server.am.ProcessRecord proc, android.content.pm.ApplicationInfo app) {
    }

    default void hookBindBackupAgentAfterPutBackupTargets(com.android.server.am.BackupRecord r) {
    }

    default com.android.server.am.BackupRecord hookGetBackupTargets(int uid, com.android.server.am.BackupRecord r) {
        return r;
    }

    default void hookUnbindBackupAgent(android.content.pm.ApplicationInfo app) {
    }

    default void hookAfterDeleteBackupTargets(int uid) {
    }

    default void hookClearPendingBackup(int uid) {
    }

    default boolean interceptStartInstrumentation(int callingPid, int callingUid, android.content.ComponentName componentName, android.content.pm.InstrumentationInfo ii, android.content.pm.ApplicationInfo ai) {
        return false;
    }

    default boolean setAllowRestartBeforeCleanUpApplicationRecord(boolean allowRestart, com.android.server.am.ProcessRecord app) {
        return allowRestart;
    }

    default boolean setRestartAfterCleanUpApplicationRecord(boolean restart, com.android.server.am.ProcessRecord app) {
        return restart;
    }

    default boolean enforceCallingOplusWindowPermission(com.android.server.am.ActivityManagerService ams, java.lang.String permission) {
        return false;
    }

    default void hookUpdateConfigForFontFlip(android.content.res.Configuration configuration) {
    }

    default void updateBurmeseConfig(android.content.res.Configuration configuration) {
    }

    default void ormsSetNotification(boolean isScreenOn) {
    }

    default void dumpActivityAndWindow() {
    }

    default void hookAttachApplicationLocked(com.android.server.am.ProcessRecord app) {
    }

    default void hookBinderProxyCountCallback(android.os.Handler mHandler, int uid) {
    }

    default void benchStepCheck(android.content.Context context, android.content.Intent intent, int pid) {
    }

    default void sendForcestopInfoToPreload(java.lang.String pkgName, int pid, int userId) {
    }

    default void addCustomServiceToMap() {
    }

    default void handleAppDiedLocked(java.lang.String packageName, com.android.server.wm.WindowProcessController windowProcessController, int userId) {
    }

    default void preBindApplicationInfo(com.android.server.wm.WindowProcessController windowProcessController, android.content.pm.ApplicationInfo appInfo) {
    }

    default void hookHandlerMarketCrash(java.lang.String processName, android.app.ApplicationErrorReport.CrashInfo crashInfo) {
    }

    default void clearCustomUIMode(java.lang.String packageName, int userId) {
    }

    default void handleForceStopPackage(java.lang.String pkgName, int userId) {
    }

    default void handlePackageDisabled(java.lang.String pkgName, int userId, boolean packageDisabled) {
    }

    default void recordBootSuccess() {
    }

    default boolean addProxyBinder(android.os.IBinder bpBinder, int uid, int pid) {
        return false;
    }

    default boolean removeProxyBinder(android.os.IBinder bpBinder, int uid) {
        return false;
    }

    default void noteAssociation(int sourceUid, int targetUid, boolean add) {
    }

    default void cameraActiveChanged(int uid) {
    }

    public interface IStaticExt {
        default boolean isSkipAnrDump() {
            return false;
        }

        default boolean checkSafeWindowPermission(java.lang.String permission, int uid) {
            return false;
        }

        default void writeTransactionToTrace(java.lang.String tracesFile) {
        }

        default void handleProcessStop(com.android.server.am.ProcessRecord app, int pid) {
        }
    }

    default void hookAfterPerformReceive(com.android.server.am.BroadcastRecord r, com.android.server.am.BroadcastFilter filter, com.android.server.am.ProcessRecord callerApp) {
    }

    default void activityPreloadHandleAppDied(java.lang.String pkg, int uid, int pid) {
    }

    default boolean isRecentLockTask(java.lang.String pkgName, int userId) {
        return false;
    }

    default int adjustExcessivePowerUsage(int cpuLimit, com.android.server.am.ProcessRecord app, int level1, int level2, int level3, int level4) {
        return cpuLimit;
    }

    default void addMonitor(java.lang.Object object) {
    }

    default void cancelCheck(com.android.server.am.ProcessRecord processRecord) {
    }

    default void isDisableDelayMCPKill(com.android.server.am.ActivityManagerService ams) {
    }

    default void enableProcessMainThreadLooperLog(java.io.PrintWriter pw, java.lang.String[] args, int opti, java.util.ArrayList<com.android.server.am.ProcessRecord> lruProcesses) {
    }

    default boolean preventSendBroadcast(android.content.Intent intent) {
        return false;
    }

    default void filterReceiverBeforeEnqueue(com.android.server.am.BroadcastRecord r) {
    }

    default boolean forbidClearAppUserData(java.lang.String packageName, android.content.pm.IPackageDataObserver observer, int userId) {
        return false;
    }

    default void onDeathRecipient(com.android.server.am.ActivityManagerService ams, com.android.server.am.ProcessRecord app, int pid, android.app.IApplicationThread thread) {
    }

    default android.os.Handler getBroadcastHandler(android.os.Handler handler) {
        return handler;
    }

    default void setConfiguration(android.content.res.Configuration config, android.content.res.Configuration overrideConfig) {
    }

    default void hookDumpApplicationMemoryUsage() {
    }

    default long adjustQueryReceiverPmFlags(long pmFlags) {
        return pmFlags;
    }

    default void reorderPersistAppsIfNeeded(java.util.List<android.content.pm.ApplicationInfo> apps) {
    }

    default void hookBeforeCheckExportState(java.lang.String callerPackageName, com.android.server.am.ProcessRecord callerApp, android.content.IntentFilter filter, boolean enabled) {
    }

    default void grantUriPermissionToUser(android.app.IApplicationThread caller, java.lang.String targetPkg, android.net.Uri uri, int modeFlags, int userId) {
    }

    default void setThreadSchedPolicy(int tid, java.lang.String tidName, int group) {
    }

    default void startSystemUIService() {
    }

    default void addAppMonitoredUid(java.lang.String monitorPkg, int monitoredUid) {
    }

    default void removeAppMonitoredUid(java.lang.String monitorPkg, int monitoredUid) {
    }

    default void adjustIntentFlag(android.content.Intent intent, int type) {
    }

    default void notifyApplicationLaunchStatus(java.lang.String processName, int pid, int uid, int state) {
    }

    default int startProcess(java.lang.String packageName, int userId) {
        return 0;
    }

    default boolean putFastStartConfig(java.lang.String packageName) {
        return false;
    }

    default boolean clearFastStartConfig() {
        return false;
    }

    default boolean enterFastStart(java.util.List<java.lang.String> pkgList, int duration, android.os.IBinder binder) {
        return false;
    }

    default boolean setDynamicBlackList(java.util.List<java.lang.String> blackList) {
        return false;
    }

    default boolean exitFastStart(android.os.IBinder binder) {
        return false;
    }

    default boolean isLogToolRun() {
        return false;
    }

    default void adjustIntentFilterWhenRegister(android.content.IntentFilter filter, android.content.IIntentReceiver receiver, com.android.server.am.ProcessRecord callerApp, int callingPid, java.lang.String callerPackage) {
    }

    default void hookBeforeGetContentProvider(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String name, int userId, boolean stable) {
    }

    default void hookAfterGetContentProvider() {
    }

    default void hookShutdown() {
    }

    default void hookProcessStartTimeout(com.android.server.am.ProcessRecord processRecord) {
    }

    default boolean isFilterRemovePackage(java.lang.String pkgName) {
        return false;
    }

    default boolean isOplusHideForRemoveBroadcast(android.content.Intent intent) {
        return false;
    }

    default boolean shouldSkipProcessEndAdj(int setAdj) {
        return false;
    }

    default boolean interceptMaybeSendBootCompleted(com.android.server.am.ProcessRecord app) {
        return false;
    }

    default void hookWatchDeviceProvisioning(boolean settingsChanged) {
    }

    default boolean isProvisioned() {
        return true;
    }

    default boolean isProvisionedStable() {
        return true;
    }

    default boolean shouldRestrictStickyBroadcast(int callingUid, com.android.server.am.ProcessRecord app, com.android.server.am.ActivityManagerService.StickyBroadcast broadcast) {
        return false;
    }

    default boolean blockPendingIntent(android.app.IApplicationThread caller, com.android.server.am.PendingIntentRecord pendingIntentRecord, android.os.IBinder allowlistToken, int code, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) {
        return false;
    }
}
