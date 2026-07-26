package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusAlarmAlignment extends android.common.IOplusCommonFeature {
    public static final int ALIGN_MODE_EARLY = 0;
    public static final int ALIGN_MODE_INVALID = Integer.MAX_VALUE;
    public static final int ALIGN_MODE_LATE = 2;
    public static final int ALIGN_MODE_NEAR = 1;
    public static final int ALIGN_TYPE_BY_PKG = 0;
    public static final int ALIGN_TYPE_BY_PROC = 1;
    public static final com.android.server.alarm.IOplusAlarmAlignment DEFAULT = new com.android.server.alarm.IOplusAlarmAlignment() { // from class: com.android.server.alarm.IOplusAlarmAlignment.1
    };
    public static final int MAX_REQUESTERS = 4;
    public static final int MSG_ACME_OFF = 4;
    public static final int MSG_ACME_ON = 3;
    public static final int MSG_ADJUST_ALARMS = 6;
    public static final int MSG_ADJUST_ALL_ALARMS = 5;
    public static final int MSG_PROXY_SCENE_CHANGED = 7;
    public static final int MSG_SCREEN_OFF = 2;
    public static final int MSG_SCREEN_ON = 1;
    public static final int MSG_UPDATE_NATPKG = 8;
    public static final java.lang.String NAME = "IOplusAlarmAlignment";
    public static final int PROXY_SCENE_NULL = 0;
    public static final int PROXY_SCENE_RES_CPU_PSI = 4;
    public static final int REQUESTER_OFREEZER = 1;
    public static final int REQUESTER_OGUARD = 2;
    public static final int REQUESTER_OSENSE = 3;
    public static final int REQUESTER_SCREENOFF = 0;

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusAlarmAlignment;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void initArgs(android.content.Context context, java.lang.Object lock, com.android.server.alarm.AlarmManagerService alarmMS, android.os.Looper looper) {
    }

    default void onScreenOn() {
    }

    default void onScreenOff() {
    }

    default void adjustAlarmLocked(com.android.server.alarm.Alarm a, boolean isScreenOn) {
    }

    default boolean updateHeartBeatPolicy(com.android.server.alarm.Alarm a, boolean isScreenOn) {
        return false;
    }

    default void adjustAlarms() {
    }

    default void alignAlarmsByUid(int uid, java.lang.String pkgName, java.util.Set<java.lang.String> actions, java.util.Set<java.lang.String> procs, int requester) {
    }

    default void unalignAlarmsByUid(int uid, java.lang.String pkgName, java.util.Set<java.lang.String> actions, java.util.Set<java.lang.String> procs, int requester) {
    }

    default void proxyAlarmsByUid(int uid, java.lang.String pkgName, int requester) {
    }

    default void unproxyAlarmsByUid(int uid, java.lang.String pkgName, int requester) {
    }

    default void proxyAlarmsByPkgActions(int uid, java.lang.String pkgName, java.util.Set<java.lang.String> actions, int requester) {
    }

    default void unproxyAlarmsByPkgActions(int uid, java.lang.String pkgName, java.util.Set<java.lang.String> actions, int requester) {
    }

    default void dump(java.io.PrintWriter pw) {
    }

    default void addProxyScene(int requester, int scene) {
    }

    default void removeProxyScene(int requester, int scene) {
    }

    default void resetProxyScene(int requester) {
    }

    default android.util.ArrayMap<java.lang.Integer, java.lang.String> getOSenseStandardHistory() {
        return null;
    }

    default void resetOSenseHistory() {
    }
}
