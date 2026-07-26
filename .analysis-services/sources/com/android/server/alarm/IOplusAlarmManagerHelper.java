package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusAlarmManagerHelper extends android.common.IOplusCommonFeature {
    public static final long ACME_ALIGN_INTERVAL = 15;
    public static final long ACME_SCREENOFF_TIME = 30;
    public static final long ALIGN_INTERVAL = 5;
    public static final com.android.server.alarm.IOplusAlarmManagerHelper DEFAULT = new com.android.server.alarm.IOplusAlarmManagerHelper() { // from class: com.android.server.alarm.IOplusAlarmManagerHelper.1
    };
    public static final java.lang.String NAME = "IOplusAlarmManagerHelper";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusAlarmManagerHelper;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void init(android.content.Context context, com.android.server.alarm.AlarmManagerService alarm) {
    }

    default void init(android.content.Context context, com.android.server.alarm.AlarmManagerService alarm, android.os.Looper looper) {
        android.util.Slog.d(NAME, "init");
    }

    default long setInexactAlarm(long windowLength, java.lang.String callingPackage, java.lang.String action, java.lang.String listenerTag) {
        return windowLength;
    }

    default boolean inPackageNameWhiteList(java.lang.String pkgName) {
        return false;
    }

    default boolean inUidWhiteList(int uid) {
        return false;
    }

    default boolean isAcmeBlackWord(java.lang.String pkgName) {
        return false;
    }

    default java.lang.Integer getAcmeBlackConfig(java.lang.String pkgName, java.lang.String tag) {
        return 0;
    }

    default long getAcmeScreenOffTime() {
        return 30L;
    }

    default long getAcmeAlignInterval() {
        return 15L;
    }

    default boolean isFilterRemovePackage(java.lang.String pkg) {
        return false;
    }

    default void dump(java.io.PrintWriter pw) {
    }

    default void dumpWhiteListVersion(java.io.PrintWriter pw) {
    }

    default boolean isInAlignWhiteList(java.lang.String pkgName) {
        return false;
    }

    default boolean isInAlignEnforcedWhiteList(java.lang.String pkgName) {
        return false;
    }

    default boolean containKeyWord(java.lang.String pkgName) {
        return false;
    }

    default long getAlignInterval() {
        return 5L;
    }

    default boolean isNatOpen() {
        return false;
    }

    default java.util.ArrayList<java.lang.String> getNatOption(int natIndex) {
        return new java.util.ArrayList<>();
    }

    default long getAlignFirstDelay() {
        return 0L;
    }

    default boolean isAlignmentForDualAppsEnabled() {
        return false;
    }

    default long getWindowLengthForDualApps() {
        return 0L;
    }

    default boolean isMatchDeepSleepRule(java.lang.String pkg, java.lang.String tag, int netStatus) {
        return false;
    }

    default boolean isMatchDeepSleepRule(android.content.ComponentName component) {
        return false;
    }

    default java.util.ArrayList<java.lang.String> getDeepSleepWhiteList() {
        return new java.util.ArrayList<>();
    }

    default void processDied(java.lang.String processName) {
    }

    default void removeAlarmLocked(int uid, int reason) {
    }

    default int getPendingJobCount() {
        return -1;
    }

    default boolean isBlackJobList(java.lang.String pkgName, java.lang.String shortName) {
        return false;
    }

    default void addCustomAppAlarmWhiteList(java.util.List<java.lang.String> packageNames) {
    }

    default java.util.List<java.lang.String> getCustomAppAlarmWhiteList() {
        return new java.util.ArrayList();
    }

    default boolean removeCustomAppAlarmWhiteList(java.util.List<java.lang.String> packageNames) {
        return false;
    }

    default boolean removeCustomAllAppAlarmWhiteList() {
        return false;
    }

    default boolean isMatchExDeepsleepRule(java.lang.String pkg, boolean deepsleep, boolean inactive) {
        return true;
    }

    default boolean isMatchExDeepsleepBlockRule(java.lang.String pkg) {
        return false;
    }

    default boolean isMatchExsleepBlockRuleJob(android.content.ComponentName component, boolean deepsleep) {
        return false;
    }

    default boolean isMatchExsleepAllowRuleJob(android.content.ComponentName component, boolean deepsleep) {
        return true;
    }

    default boolean isMatchRetoreNetworkRule(java.lang.String pkg) {
        return true;
    }

    default boolean extremeSleepFeature(java.lang.String flag) {
        return false;
    }

    default void maxAlarmsPerUidHandle(java.lang.String callingPackage, int callingUid, int maxAlarmsPerUid) {
    }

    default void alarmToStringExtend(java.lang.StringBuilder sb, long whenElapsed, long windowLength, long maxWhenElapsed, long repeatInterval, android.app.PendingIntent operation, java.lang.String listenerTag, int flags, int uid, java.lang.String action, java.lang.String component, java.lang.String procName) {
    }

    default void initSmartDozeAlarmExemptionBroadcast(android.content.Context context, android.os.Handler handler) {
    }

    default void monitorAlarmWakeup(com.android.server.alarm.Alarm alarm) {
    }

    default void monitorFrameworkWakeupEvent() {
    }

    default boolean isInSuperPowerSaveBlackList(java.lang.String pkgName) {
        return false;
    }

    default boolean isInDualAppActionBlackList(java.lang.String pkgName, java.lang.String action) {
        return false;
    }

    default void dumpOSenseSceneWhiteList(java.io.PrintWriter pw) {
    }

    default boolean inOSenseSceneWhiteList(int scene, java.lang.String pkgName) {
        return false;
    }

    default boolean updateOSenseSceneWhiteList(int scene, java.lang.String pkgName, boolean isAdd) {
        return false;
    }

    default java.util.List<java.lang.String> getTagFromDuplicateBlackList(java.lang.String pkgName) {
        return null;
    }

    default boolean inDuplicateBlackList(java.lang.String pkgName, java.lang.String tag) {
        return false;
    }
}
