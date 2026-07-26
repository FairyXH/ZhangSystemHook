package com.android.server.appwidget;

/* JADX INFO: loaded from: classes.dex */
public interface IAppWidgetServiceImplExt {
    default void notifyBindAppWidget(java.lang.String packageName, int uid) {
    }

    default void notifyOnWidgetProviderAddedOrChangedLocked(int hash, int uid, java.lang.String pkgName, boolean add) {
    }

    default void notifyRemoveAppWidget(int hash, java.lang.String packageName, int uid) {
    }

    default void notifyClearWidgetsLocked() {
    }

    default void notifyRestoreAppWidget(java.lang.String packageName, int uid) {
    }

    default int hookqueryIntent(int flag) {
        return flag;
    }

    default boolean hookaddProviderLocked(android.content.pm.ResolveInfo ri) {
        return false;
    }

    default void notifyBindLoadedWidgets(java.lang.String packageName, int uid) {
    }

    default java.lang.String hookHostPackageNameOnReadProfileState(java.lang.String packageName) {
        return packageName;
    }

    default void hookUpdateWidgetSate(int uid, java.lang.String packageName, boolean state) {
    }

    default void notifyUpdateAppWidgetTimeLocked(int uid) {
    }

    default int hookGetRepeatAlarmType(int type) {
        return type;
    }
}
