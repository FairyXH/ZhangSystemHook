package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public interface IPowerManagerServiceWrapper {
    default java.lang.Object getLock() {
        return new java.lang.Object();
    }

    default com.android.server.power.IPowerManagerServiceExt getPmsExt() {
        return null;
    }

    default int getDozeScreenStateOverrideFromDreamManager() {
        return 0;
    }

    default com.android.server.lights.LightsManager getLightsManager() {
        return new com.android.server.lights.LightsManager() { // from class: com.android.server.power.IPowerManagerServiceWrapper.1
            @Override // com.android.server.lights.LightsManager
            public com.android.server.lights.LogicalLight getLight(int id) {
                return new com.android.server.lights.LogicalLight() { // from class: com.android.server.power.IPowerManagerServiceWrapper.1.1
                    @Override // com.android.server.lights.LogicalLight
                    public void setBrightness(float brightness) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void setBrightness(float brightness, int brightnessMode) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void setColor(int color) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void setFlashing(int color, int mode, int onMS, int offMS) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void pulse() {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void pulse(int color, int onMS) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void turnOff() {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void setVrMode(boolean enabled) {
                    }
                };
            }
        };
    }

    default boolean getProximityPositive() {
        return false;
    }

    default void setProximityPositive(boolean value) {
    }

    default void setInterceptedPowerKeyForProximity(boolean value) {
    }

    default boolean getDreamsEnabledOnBatteryConfig() {
        return false;
    }

    default void setDreamsEnabledOnBatteryConfig(boolean value) {
    }

    default int getDreamsBatteryLevelMinimumWhenNotPoweredConfig() {
        return 0;
    }

    default void setDreamsBatteryLevelMinimumWhenNotPoweredConfig(int value) {
    }

    default int getMSG_SCREEN_BRIGHTNESS_BOOST_TIMEOUT() {
        return 0;
    }

    default int getMSG_USER_ACTIVITY_TIMEOUT() {
        return 0;
    }

    default android.os.Handler getHandler() {
        return new android.os.Handler();
    }

    default float getScreenBrightnessOverrideFromWindowManager() {
        return 0.0f;
    }

    default void setScreenBrightnessOverrideFromWindowManager(float value) {
    }

    default int getWakeLockSummary() {
        return 0;
    }

    default long getUserActivityTimeoutOverrideFromWindowManager() {
        return 0L;
    }

    default void setUserActivityTimeoutOverrideFromWindowManager(long value) {
    }

    default long getScreenOffTimeoutSetting() {
        return 0L;
    }

    default java.util.ArrayList<com.android.server.power.PowerManagerService.WakeLock> getWakeLocks() {
        return new java.util.ArrayList<>();
    }

    default int getDirty() {
        return 0;
    }

    default void setDirty(int value) {
    }

    default int getDIRTY_WAKE_LOCKS() {
        return 0;
    }

    default int getDIRTY_USER_ACTIVITY() {
        return 0;
    }

    default android.util.SparseArray<com.android.server.power.PowerManagerService.UidState> getUidState() {
        return new android.util.SparseArray<>();
    }

    default android.util.SparseArray<com.android.server.power.PowerGroup> getPowerGroups() {
        return new android.util.SparseArray<>();
    }

    default android.service.dreams.DreamManagerInternal getDreamManager() {
        return new android.service.dreams.DreamManagerInternal() { // from class: com.android.server.power.IPowerManagerServiceWrapper.2
            public void startDream(boolean doze, java.lang.String reason) {
            }

            public void stopDream(boolean immediate, java.lang.String reason) {
            }

            public boolean isDreaming() {
                return false;
            }

            public void requestDream() {
            }

            public boolean canStartDreaming(boolean isScreenOn) {
                return false;
            }

            public void registerDreamManagerStateListener(android.service.dreams.DreamManagerInternal.DreamManagerStateListener listener) {
            }

            public void unregisterDreamManagerStateListener(android.service.dreams.DreamManagerInternal.DreamManagerStateListener listener) {
            }
        };
    }

    default float getScreenBrightnessSettingMinimum() {
        return 0.0f;
    }

    default void setScreenBrightnessSettingMinimum(float value) {
    }

    default float getScreenBrightnessSettingMaximum() {
        return 0.0f;
    }

    default void setScreenBrightnessSettingMaximum(float value) {
    }

    default float getScreenBrightnessSettingDefault() {
        return 0.0f;
    }

    default void setScreenBrightnessSettingDefault(float value) {
    }

    default int getScreenBrightnessModeSetting() {
        return 0;
    }

    default boolean getBootCompleted() {
        return false;
    }

    default void setDozeAfterScreenOff(boolean value) {
    }

    default void setDecoupleHalAutoSuspendModeFromDisplayConfig(boolean value) {
    }

    default void setDecoupleHalInteractiveModeFromDisplayConfig(boolean value) {
    }

    default void setDreamsActivateOnSleepSetting(boolean value) {
    }

    default void updatePowerStateLocked() {
    }

    default boolean userActivityNoUpdateLocked(long eventTime, int event, int flags, int uid) {
        return false;
    }

    default boolean isInteractiveInternal() {
        return false;
    }

    default long getAttentiveTimeoutLocked() {
        return 0L;
    }

    default long getSleepTimeoutLocked(long attentiveTimeout) {
        return 0L;
    }

    default long getScreenOffTimeoutLocked(long sleepTimeout, long attentiveTimeout) {
        return 0L;
    }

    default boolean setWakeLockDisabledStateLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        return false;
    }

    default void notifyWakeLockReleasedLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
    }

    default void notifyWakeLockAcquiredLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
    }

    default void releaseWakeLockInternal(android.os.IBinder lock, int flags) {
    }

    default void userActivityInternal(int displayId, long eventTime, int event, int flags, int uid) {
    }

    default void notifyWakeLockChangingLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int flags, java.lang.String tag, java.lang.String packageName, int uid, int pid, android.os.WorkSource ws, java.lang.String historyTag, android.os.IWakeLockCallback callback) {
    }

    default int findWakeLockIndexLocked(android.os.IBinder lock) {
        return 0;
    }

    default void applyWakeLockFlagsOnAcquireLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, boolean isCallerPrivileged) {
    }

    default void removeWakeLockLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int index) {
    }

    default void setRequestWaitForNegativeProximity(boolean requestWaitForNegativeProximity) {
    }

    default com.android.server.ServiceThread getHandlerThread() {
        return null;
    }

    default int getBatteryLevel() {
        return -1;
    }
}
