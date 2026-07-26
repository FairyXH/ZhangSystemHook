package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IActiveServicesExt {
    default void hookStartServiceLockedBegin(android.content.Context context, android.content.Intent service, int userId) {
    }

    default boolean interceptStartServiceLockedIfCallerNotNull(android.content.Intent service, com.android.server.am.ProcessRecord callerApp, java.lang.String callingPackage) {
        return false;
    }

    default boolean checkAllowIfAppStartModeNotNormal(java.lang.String callingPackage, android.content.Intent service, com.android.server.am.ServiceRecord r) {
        return false;
    }

    default boolean interceptStartServiceLockedAfterStartMode(int callingPid, int callingUid, java.lang.String callingPackage, com.android.server.am.ServiceRecord r, android.content.Intent service) {
        return false;
    }

    default boolean interceptStartServiceLockedBeforeStartInner(com.android.server.am.ActivityManagerService ams, com.android.server.am.ServiceRecord r, android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, boolean fgRequired, java.lang.String callingFeatureId, int userId, int callingUid, int callingPid, java.lang.String callingPkg) {
        return false;
    }

    default boolean skipStopInBackgroundBegin(com.android.server.am.ServiceRecord serviceRecord, int uid) {
        return false;
    }

    default boolean interceptBindServiceLockedBegin(android.content.Context context, android.content.Intent service, int userId, com.android.server.am.ProcessRecord callerApp, java.lang.String callingPackage) {
        return false;
    }

    default boolean interceptBindServiceLockedBeforeConnection(com.android.server.am.ActivityManagerService ams, int callingPid, int callingUid, java.lang.String callingPackage, com.android.server.am.ServiceRecord r, android.app.IApplicationThread caller, android.os.IBinder token, android.content.Intent service, java.lang.String resolvedType, android.app.IServiceConnection connection, long flags, java.lang.String instanceName, int userId, com.android.server.am.ProcessRecord callerApp) {
        return false;
    }

    default void hookBindServiceLockedEnd(int callingUid, android.content.Intent service, com.android.server.am.ServiceRecord r) {
    }

    default void setCallerAppPackage(com.android.server.am.ServiceRecord r, java.lang.String callingPackage) {
    }

    default void removeCallerAppPackage(com.android.server.am.ServiceRecord r) {
    }

    default boolean delayRestartServices(com.android.server.am.ServiceRecord sr, com.android.server.am.ActivityManagerService ams) {
        return false;
    }

    default void hookUnBindServiceLockedAfterRemoveConnection(com.android.server.am.ConnectionRecord connectionRecord) {
    }

    default boolean setTimeoutNeededToFalseIfNeed(com.android.server.am.ServiceRecord r, boolean fg, java.lang.String why) {
        return false;
    }

    default boolean interceptBringDownServiceIfNeeded(com.android.server.am.ActiveServices.ServiceMap serviceMap, com.android.server.am.ServiceRecord serviceRecord) {
        return false;
    }

    default boolean interceptProcessStartTimedOutBeforeBringDown(com.android.server.am.ActiveServices.ServiceMap serviceMap, com.android.server.am.ServiceRecord serviceRecord) {
        return false;
    }

    default boolean interceptBringDownDisabledPackageServicesBeforeBringDown(com.android.server.am.ActiveServices.ServiceMap serviceMap, com.android.server.am.ServiceRecord serviceRecord) {
        return false;
    }

    default boolean hookRetrieveServiceChangeUserIdToSystemIfNeed(android.content.pm.ResolveInfo rInfo, int callingUid, int userId) {
        return false;
    }

    default void setActiveServicesDynamicalLogEnable(boolean on) {
    }

    default void hookBindServiceAfterStartAssociation(com.android.server.am.ConnectionRecord c, com.android.server.am.AppBindRecord b, com.android.server.am.ServiceRecord s) {
    }

    default void hookPerformRestartServiceBegin(com.android.server.am.ServiceRecord sr) {
    }

    default boolean interceptBringUpServices(com.android.server.am.ServiceRecord sr, com.android.server.am.ActivityManagerService ams, int callingUid, int callingPid) {
        return false;
    }

    default void hookBringUpServicesAfterStartProc(com.android.server.am.ServiceRecord sr, int callingUid, int callingPid) {
    }

    default void hookScheduleServiceRestart(com.android.server.am.ServiceRecord sr, long now, long minDuration) {
    }

    default void hookKillServicesWhenRemoveServiceConnection(com.android.server.am.ProcessRecord app, long costTime) {
    }

    default com.android.server.am.ConnectionRecord retrieveConnectionRecordLocked(java.util.ArrayList<com.android.server.am.ConnectionRecord> clist, android.os.IBinder binder, com.android.server.am.AppBindRecord b, com.android.server.wm.ActivityServiceConnectionsHolder<com.android.server.am.ConnectionRecord> activity, long flags, int clientLabel, android.app.PendingIntent clientIntent, int clientUid, java.lang.String processName, java.lang.String callingPackage) {
        return null;
    }

    default void hookUpdateServiceBindStatus(com.android.server.am.ServiceRecord serviceRecord, java.lang.String action, boolean state) {
    }

    default void updateExecutingComponent(int uid, java.lang.String component, int mode) {
    }

    default boolean logFgsBackgroundStart() {
        return true;
    }

    default void handleAfterStartInnerService(android.content.ComponentName realResult, com.android.server.am.ServiceRecord r, java.lang.String callingPackage, int callingUid, boolean procStart, int callingProcessState) {
    }

    default void handleAfterBindInnerService(com.android.server.am.ServiceRecord r, java.lang.String callingPackage, int callingUid, boolean procStart, int callingProcessState) {
    }

    default void noteAssociation(int sourceUid, int targetUid, boolean add) {
    }

    default void handleExceptionWhenBringUpService(com.android.server.am.ServiceRecord r, boolean execInFg) {
    }

    default void onServiceConnectionInfoCollect(java.lang.String callingPackage, int size) {
    }

    default void hookBindServiceLockedAfterConnected(long beginTime) {
    }

    default void recordStateToIntentBindRecord(com.android.server.am.IntentBindRecord intentBindRecord, com.android.server.am.ServiceRecord serviceRecord, boolean wasStartRequested, boolean hadConnections, long beginTime) {
    }

    default void hookPublishServiceLockedAfterConnected(com.android.server.am.IntentBindRecord intentBindRecord, long publishTime) {
    }

    default void hookAfterScheduleBindService(com.android.server.am.IntentBindRecord intentBindRecord, boolean rebind) {
    }

    default boolean rescheduleServiceIfNeeded(com.android.server.am.ServiceRecord r, android.os.Handler handler) {
        return false;
    }

    default boolean adjustRescheduleServiceRestartDelayIfNeed(com.android.server.am.ServiceRecord r, long oldVal, long now, long restartTimeBetween) {
        return false;
    }
}
