package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public interface IPowerManagerServiceExt {
    default boolean getProximityLockFromInCallUiValueLocked() {
        return false;
    }

    default android.os.Looper getCustomPowerManagerLooper() {
        return null;
    }

    default void init(android.content.Context context) {
    }

    default void onStart() {
    }

    default void systemReady(com.android.server.power.SuspendBlocker suspendBlocker) {
    }

    default void onBootComplete() {
    }

    default long getScreenDimDurationLocked(long screenOffTimeout, boolean isFaceDown) {
        return -1L;
    }

    default void userActivityNoUpdateChangeLightsLocked() {
    }

    default void onUserActivityNoUpdateLocked(boolean isInteractiveInternal, int event) {
    }

    default void onUserActivityNoUpdateLocked(boolean isInteractiveInternal, int event, int uid) {
    }

    default void wakeDisplayGroupNoUpdateLockedEnd(int groupId, java.lang.String wakeupReason) {
    }

    default void wakeDisplayGroupNoUpdateLockedStart(int groupId, long eventTime, java.lang.String details, int uid, java.lang.String opPackageName, int opUid) {
    }

    default void onSleepDisplayGroupNoUpdateLockedEnd(int groupId, int reason) {
    }

    default boolean notAllowedSetUserActivityTimeoutOverrideFromWindowManager(long userActivityTimeoutOverrideFromWindowManager, boolean isInteractive, int userActivitySummary) {
        return false;
    }

    default boolean dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        return false;
    }

    default void onAcquireNewWakeLockInternal(com.android.server.power.PowerManagerService.WakeLock wakeLock, android.os.IBinder lock, int flags, java.lang.String tag, java.lang.String packageName, android.os.WorkSource ws, java.lang.String historyTag, int uid, int pid) {
    }

    default void onRemoveWakeLockLocked(int pid, com.android.server.power.PowerManagerService.WakeLock wakeLock) {
    }

    default boolean isCustomPowerSaveModeDisabled() {
        return false;
    }

    default void printStackTraceInfo() {
    }

    default void onPowerManagerHandlerHandleMessage(android.os.Message msg) {
    }

    default void onReadConfigurationLocked() {
    }

    default boolean onApplyWakeLockFlagsOnAcquireLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int uid) {
        return false;
    }

    default void releaseWakeLockInternalLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int flags) {
    }

    default void noteWakeLockChange(com.android.server.power.PowerManagerService.WakeLock wl, boolean acquire) {
    }

    default void noteWorkSourceChange(com.android.server.power.PowerManagerService.WakeLock wl, android.os.WorkSource newWorkSource) {
    }

    default void onDeviceIdle() {
    }

    default java.lang.String handleWakeUpdetailsEarly(java.lang.String details, int uid, java.lang.String opPackageName, int opUid) {
        return details;
    }

    default int handleWakeUpReasonEarly(int reason, java.lang.String details) {
        return reason;
    }

    default boolean interceptWakeDisplayGroupNoUpdateLocked(int groupId, long eventTime, int reason, java.lang.String details, int reasonUid, java.lang.String opPackageName, int opUid, boolean debugSPEW) {
        return false;
    }

    default boolean interceptSleepDisplayGroupNoUpdateLocked(int groupId, long eventTime, int reason, int flags, int uid) {
        return false;
    }

    default void setGlobalWakefulnessLocked(int wakefulness, int reason, long eventTime, int plugType, java.lang.String details) {
    }

    default void onWakefulnessChangeFinished(int wakefulness) {
    }

    default void updateWakeLockSummaryLockedStart() {
    }

    default void getWakeLockSummaryFlags(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
    }

    default boolean isBeingKeptAwakeLocked(int groupId, boolean proximityPositive) {
        return proximityPositive;
    }

    default void updateDisplayPowerStateLockedStart() {
    }

    default void updateDisplayPowerStateLocked(int groupId, int wakefulness, int dozeScreenState, int policy) {
    }

    default void onDisplayStateChange(boolean alloff) {
    }

    default boolean interceptShutdownOrRebootInternal(java.lang.String reason) {
        return false;
    }

    default void acquireSuspendBlockerStart() {
    }

    default void acquireSuspendBlockerEnd(java.lang.String name) {
    }

    default void releaseSuspendBlocker(java.lang.String name) {
    }

    default boolean interceptAcquireWakeLockInternal(android.os.IBinder lock, java.lang.String packageName, int flags, android.os.WorkSource ws, int uid, java.lang.String tag) {
        return false;
    }

    default boolean interceptSetLowPowerModeInternalIsPowered() {
        return false;
    }

    default void setLowPowerModeInternalEnd(boolean enabled) {
    }

    default boolean isBlockedByBiometrics() {
        return false;
    }

    default void screenOnWakelockCheck(int wakefulness, boolean isUserActivitySummaryDream, boolean hasMsg) {
    }

    default boolean isFingerprintOpticalSupport() {
        return false;
    }

    default boolean isSideFingerprintSupport() {
        return false;
    }

    default void systemReady() {
    }

    default float updateAutoBrightness(float defaultBrightness) {
        return defaultBrightness;
    }

    default boolean isValidBrightness(int value) {
        return false;
    }

    default void setScreenBrightnessOverrideFromWindowManager(float brightness) {
    }

    default int getScreenBrightnessSetting() {
        return 0;
    }

    default void cancelCheck(java.lang.String detail) {
    }

    default void notePowerkeyProcessStagePoint(java.lang.String stage) {
    }

    default void notePowerkeyProcessEvent(java.lang.String eventStr, boolean cancelWakeCheck, boolean cancelSleepCheck) {
    }

    default long getScreenOffTimeoutLocked(long screenoffSystem) {
        return screenoffSystem;
    }

    default void handleAodChanged() {
    }

    default void onAodsystemReady() {
    }

    default void setAodSettingStatus() {
    }

    default boolean isShouldGoAod() {
        return false;
    }

    default void onDisplayStateChange(android.service.dreams.DreamManagerInternal dreamManager, int state) {
    }

    default void setDozeOverrideFromDreamManager(int screenState, int screenBrightness) {
    }

    default int setDozeOverrideFromDreamManagerInternal(int screenState, int screenBrightness) {
        return screenState;
    }

    default void registerOtherContentObserver(android.content.ContentResolver resolver, android.database.ContentObserver settingsObserver) {
    }

    default void stopDream(android.service.dreams.DreamManagerInternal dreamManager) {
        dreamManager.stopDream(false, "unknownReason");
    }

    default void setScreenOffPositive(java.lang.String value) {
    }

    default void stopDreamByMessage(android.service.dreams.DreamManagerInternal dreamManager) {
    }

    default void updateSettingsLocked(android.content.ContentResolver resolver) {
    }

    default boolean acquireBaseProxyWakeLock(android.os.IBinder lock, int displayId, int flags, java.lang.String tag, java.lang.String packageName, android.os.WorkSource ws, java.lang.String historyTag, int uid, int pid) {
        return false;
    }

    default boolean acquireBaseProxyWakeLock(android.os.IBinder lock, int displayId, int flags, java.lang.String tag, java.lang.String packageName, android.os.WorkSource ws, java.lang.String historyTag, int uid, int pid, android.os.IWakeLockCallback callback) {
        return false;
    }

    default boolean acquireBaseProxyWakeLock(android.os.IBinder lock, int displayId, int flags, java.lang.String tag, java.lang.String packageName, android.os.WorkSource ws, java.lang.String historyTag, int uid, int pid, android.os.IWakeLockCallback callback, boolean isCallerPrivileged) {
        return false;
    }

    default void releaseBaseProxyedWakeLockInternalLocked(android.os.IBinder lock) {
    }

    default void dumpBaseProxyWakeLock(java.io.PrintWriter pw) {
    }

    default void handleBaseWakeLockDeath(com.android.server.power.PowerManagerService.WakeLock wakelock) {
    }

    default void updateProxyedWakeLockWorkSource(android.os.IBinder lock, android.os.WorkSource ws, java.lang.String historyTag) {
    }

    default boolean updateProxyedWakeLockWorkSource(android.os.IBinder lock, android.os.WorkSource ws, java.lang.String historyTag, com.android.server.power.PowerManagerService.WakeLock wakelock) {
        return false;
    }

    default void uploadAttentionChangeTimeout(long eventTime) {
    }

    default void setDeviceState(int state) {
    }

    default boolean shouldCommitScreenBrightnessOverrideMap() {
        return false;
    }

    default void userActivity(int displayId, long eventTime, int event, int flags, int uid) {
    }

    default boolean getOnDozeSwitch() {
        return false;
    }

    default void disableScreenStayAwakeOfApp(boolean disable, int uid) {
    }

    default boolean getIgnoreBright(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        return false;
    }

    default void dumpSmartLauncher(java.io.PrintWriter pw) {
    }

    default boolean isWakelockNeedIgnoreOnAfterRelease(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        return false;
    }

    default boolean getCastMode() {
        return false;
    }

    default void oplusUserActivityInternal(int displayId, long eventTime, int event, int flags, int uid, android.os.IAnrLogEnhancementHelperExt anrHelperExt, java.lang.String message) {
    }

    default void setNextTimeout(long nextTimeout) {
    }

    default int getBatteryLevel() {
        return -1;
    }

    default boolean getOplusDozeAfterOff(int sleepReason) {
        return false;
    }
}
