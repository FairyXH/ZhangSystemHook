package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IBackgroundActivityStartControllerExt {
    public static final int BAL_ALLOW_OPLUS_WHITE_LIST = 99;

    default boolean interceptBackgroundActivityStartBegin(android.content.Intent intent, int callingUid, int callingPid, java.lang.String callingPackage, int realCallingUid, int realCallingPid) {
        return false;
    }

    default void monitorActivityStartInfoIfNeed(java.lang.String allowStartActivityType, boolean needCheckTopPkg, boolean needChangeCallingUid) {
    }

    default boolean isFromBackgroundWhiteList(com.android.server.wm.ActivityTaskManagerService service, int realCallingUid) {
        return false;
    }

    default boolean startAllowedIfRealCallingUidIsHome(com.android.server.wm.ActivityTaskManagerService service, int realCallingUid) {
        return false;
    }

    default boolean checkBackgroundActivityPermission(com.android.server.wm.ActivityTaskManagerService mService, int callingUid, java.lang.String callingPackage, android.content.Intent intent) {
        return true;
    }
}
