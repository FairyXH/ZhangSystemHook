package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusSceneManager extends android.common.IOplusCommonFeature {
    public static final java.lang.String APP_SCENE_AUDIO_FOCUS = "audioFocus";
    public static final java.lang.String APP_SCENE_AUDIO_RECORDER = "audioRecorder";
    public static final java.lang.String APP_SCENE_DEFAULT_DIALER = "dialer";
    public static final java.lang.String APP_SCENE_DEFAULT_INPUT = "input";
    public static final java.lang.String APP_SCENE_DEFAULT_LAUNCHER = "launcher";
    public static final java.lang.String APP_SCENE_DEFAULT_LIVE_WALLPAPER = "wallpaper";
    public static final java.lang.String APP_SCENE_DEFAULT_SMS = "sms";
    public static final java.lang.String APP_SCENE_GPS = "gps";
    public static final java.lang.String APP_SCENE_PERSIST_NOTIFICATION = "persistNotification";
    public static final java.lang.String APP_SCENE_SCREEN_RECORDER = "screenRecorder";
    public static final java.lang.String APP_SCENE_SENSOR = "sensor";
    public static final java.lang.String APP_SCENE_TOP_APP = "topApp";
    public static final java.lang.String APP_SCENE_TRAFFIC = "traffic";
    public static final java.lang.String APP_SCENE_VIDEO = "video";
    public static final java.lang.String APP_SCENE_VISIBLE_WINDOW = "visibleWindow";
    public static final java.lang.String APP_SCENE_VPN = "vpn";
    public static final java.lang.String APP_SCENE_WIDGET = "widget";
    public static final java.lang.String APP_STATE_CHANGE = "appState";
    public static final java.lang.String TAG = "OplusSceneManager";
    public static final boolean LOG_DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final com.android.server.am.IOplusSceneManager DEFAULT = new com.android.server.am.IOplusSceneManager() { // from class: com.android.server.am.IOplusSceneManager.1
    };

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusSceneManager;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void onInit(com.android.server.am.IOplusActivityManagerServiceEx amsEx) {
    }

    default void putAppScene(int uid, java.lang.String packageName, java.lang.String config) {
    }

    default void removeAppScene(int uid, java.lang.String packageName, java.lang.String config) {
    }

    default void requestServiceBinding(int uid, java.lang.String packageName, java.lang.String action, boolean add) {
    }

    default void updateVisibleWindow(int uid, int pid, int windowId, int windowType, boolean isVisible, boolean isShown) {
    }

    default boolean isFloatWindowList(int uid) {
        return false;
    }

    default void updatePersistentNotification(int uid, java.lang.String packageName, boolean show) {
    }

    default void updatePersistentNotification(int uid, java.lang.String packageName, java.lang.String key, boolean show) {
    }

    default void updateAppWidget(int hash, int uid, java.lang.String pkgName, boolean add) {
    }

    default void updateAppWidgetTimeLocked(int uid) {
    }

    default java.util.ArrayList<java.lang.String> getAppStateByUid(int uid, java.lang.String sceneType) {
        java.util.ArrayList<java.lang.String> appMap = new java.util.ArrayList<>();
        return appMap;
    }

    default android.util.SparseArray<java.util.ArrayList<java.lang.String>> getAppStateByScene(java.lang.String sceneType) {
        android.util.SparseArray<java.util.ArrayList<java.lang.String>> appMap = new android.util.SparseArray<>();
        return appMap;
    }

    default boolean updateTrafficList(android.os.Bundle data) {
        return true;
    }

    default java.util.List<com.oplus.util.OplusPackageFreezeData> getRunningProcesses() {
        return null;
    }

    default android.util.SparseArray<java.lang.Long> getTrafficBytesList(java.util.ArrayList<java.lang.Integer> uids) {
        return null;
    }

    default android.util.SparseArray<java.lang.Long> getTrafficPacketList(java.util.ArrayList<java.lang.Integer> uids) {
        return null;
    }

    default java.util.ArrayList<java.lang.String> getAudioRecordList() {
        return null;
    }

    default void updateAudioFocusInfo(int uid, boolean add) {
    }

    default void resetAudioFocusInfo() {
    }

    default void updateVideoInfo(int uid, boolean add) {
    }

    default void resetVideoInfo() {
    }

    default void updateNavigation(android.os.WorkSource oldWs, android.os.WorkSource newWs) {
    }

    default void updateNavigation(int uid, java.lang.String pkgName, boolean start) {
    }

    default void updateSensorInfo(int uid, int handle, boolean add) {
    }

    default java.util.ArrayList<java.lang.Integer> getAudioFocusByAudioManager() {
        return null;
    }

    default java.util.ArrayList<java.lang.Integer> getAudioFocusByHook() {
        return null;
    }

    default java.util.ArrayList<java.lang.Integer> getVideoListByHook() {
        return null;
    }

    default java.util.List<java.lang.Integer> getBluetoothList() {
        return null;
    }

    default boolean isVisibleWindow(int uid) {
        return false;
    }

    default void bootCompleted() {
    }

    default boolean checkActivityIfRestricted(int callingUid, java.lang.String callingPkg, int uid, java.lang.String packageName, android.content.ComponentName componentName) {
        return false;
    }

    default boolean checkStartServiceIfRestricted(int callingPid, int callingUid, java.lang.String callingPkg, int uid, java.lang.String packageName, java.lang.String processName, android.content.ComponentName componentName, java.lang.String action, boolean isBound) {
        return false;
    }

    default boolean checkBumpServiceIfRestricted(int uid, java.lang.String pkgName, java.lang.String why) {
        return false;
    }

    default void checkReStartServiceIfRestricted(int uid, java.lang.String pkgName) {
    }

    default void noteFgService(int uid, java.lang.String packageName, boolean isForeground) {
    }

    default boolean checkProviderIfRestricted(int callingPid, int callingUid, java.lang.String callingPackage, int uid, java.lang.String pkgName, java.lang.String processName, java.lang.String cpnName) {
        return false;
    }

    default boolean checkReceiverIfRestricted(com.android.server.am.BroadcastRecord r, java.lang.Object o) {
        return false;
    }

    default boolean checkSyncIfRestricted(int uid, java.lang.String packageName) {
        return false;
    }

    default boolean checkJobIfRestricted(int uid, java.lang.String packageName) {
        return false;
    }

    default boolean checkJobIfRestricted(int uid, java.lang.String packageName, android.app.job.JobInfo jobInfo) {
        return false;
    }

    default boolean checkAlarmIfRestricted(int uid, java.lang.String packageName, java.lang.String action) {
        return false;
    }

    default void noteIsolatedApp(int isolatedUid, int appUid, java.lang.String packageName, boolean add) {
    }

    default void noteAssociation(int sourceUid, int targetUid, boolean add) {
    }

    default boolean appAssociationCheck(int sourceUid, int targetUid) {
        return false;
    }

    default void noteWatchdog() {
    }

    default void noteSysShutdown() {
    }

    default void noteSysStateChanged(int state, int type, java.lang.String spare) {
    }

    default long getTotalCpuLoadPercent() {
        return 0L;
    }

    default java.util.List<java.lang.String> getTopLoadPidsInfos(int num) {
        return null;
    }

    default void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
    }
}
