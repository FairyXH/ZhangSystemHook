package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IUiModeManagerServiceExt {
    default void init(android.content.Context context, com.android.server.UiModeManagerService umss, android.os.Binder binder) {
    }

    default void notifyFlingerUiMode(int uiMode) {
    }

    default void darkModeOnStartInit(android.content.Context mContext, com.android.server.UiModeManagerService umss) {
    }

    default void darkModeInitSettings(android.content.Context mContext) {
    }

    default void darkModeRegisterThermalProtect(com.android.server.UiModeManagerService umss) {
    }

    default void darkModeRegisterShutdownReceiver(com.android.server.UiModeManagerService umss, android.os.Handler mHandler) {
    }

    default void darkModeNightModeChange(com.android.server.UiModeManagerService umss, int uiMode, int mSetUiMode) {
    }

    default void darkModeDumpUiModeManagerServiceMessage(java.io.PrintWriter pw, com.android.server.twilight.TwilightManager mTwilightManager) {
    }

    default int darkModeGetSuperSaveUiMode(int uiMode) {
        return uiMode;
    }

    default boolean darkModeGetAutoFirst() {
        return true;
    }

    default boolean darkModeIsSuperSaveMode() {
        return false;
    }

    default boolean darkModeShouldHideSaveMode() {
        return false;
    }

    default boolean darkModeOverrideComputedNightMode(int mNightMode, boolean autoFirst, boolean mComputedNightMode) {
        return mComputedNightMode;
    }

    default void upCommonStatistics(android.content.Context mContext, int user, int lastMode, int currentMode) {
    }

    default void persistNightModeStatistics(android.content.Context mContext, int user) {
    }

    default void fontUpdateConfigurationInUIMode(android.content.Context mContext, android.content.res.Configuration mConfiguration, int userId) {
    }

    default void darkModeSetValueForState(android.content.Context mContext, int user, int mSetUimode) {
    }
}
