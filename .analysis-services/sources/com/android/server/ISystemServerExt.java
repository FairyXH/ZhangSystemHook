package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface ISystemServerExt {
    default void initFontsForserializeFontMap() {
    }

    default void initSystemServer(android.content.Context systemContext) {
    }

    default void setDataNormalizationManager() {
    }

    default void addOplusDevicePolicyService() {
    }

    default void waitForFutureNoInterrupt() {
    }

    default void startBootstrapServices() {
    }

    default void startCoreServices() {
    }

    default com.android.server.input.InputManagerService getInputManagerService(android.content.Context context) {
        return new com.android.server.input.InputManagerService(context);
    }

    default com.android.server.policy.PhoneWindowManager getSubPhoneWindowManager() {
        return new com.android.server.policy.PhoneWindowManager();
    }

    default boolean startJobSchedulerService() {
        return false;
    }

    default void addLinearmotorVibratorService(android.content.Context context) {
    }

    default void addOtaDexoptService(android.content.Context context, com.android.server.pm.PackageManagerService packageManagerService) {
    }

    default void addStorageHealthInfoService(android.content.Context context) {
    }

    default void startOtherServices() {
    }

    default void linearVibratorSystemReady() {
    }

    default void systemReady() {
    }

    default void systemRunning() {
    }

    default void startUsageStatsService(com.android.server.SystemServiceManager systemServiceManager) {
    }

    default void writeAgingCriticalEvent() {
    }

    default void setBootstage(boolean start) {
    }

    default void startDynamicFilterService(com.android.server.SystemServiceManager systemServiceManager) {
    }

    default void dynamicFilterServiceSystemReady(com.android.server.utils.TimingsTraceAndSlog t) {
    }

    default void addCabcService(android.content.Context context, com.android.server.utils.TimingsTraceAndSlog t) {
    }

    default void addOplusTestService(android.content.Context context) {
    }

    default void addRenderAcceleratingService(android.content.Context context, com.android.server.utils.TimingsTraceAndSlog t) {
    }

    default void runBootProtector(int startCount) {
    }

    default int getSystemThemeStyle() {
        return -1;
    }

    default void addOplusTileManagerService(android.content.Context context) {
    }

    default void addCrossDeviceService(android.content.Context context, com.android.server.am.ActivityManagerService activityManagerService, com.android.server.utils.TimingsTraceAndSlog t) {
    }

    default boolean stabilityDynamicLogConfig(java.io.PrintWriter pw, java.lang.String[] args) {
        return false;
    }
}
