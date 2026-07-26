package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityTaskManagerServiceWrapper {
    default boolean canShowDialogs() {
        return false;
    }

    default com.android.server.wm.WindowProcessController getHomeProcess() {
        return null;
    }

    default com.android.server.wm.IActivityTaskManagerServiceExt getExtImpl() {
        return null;
    }

    default com.android.server.wm.IFlexibleWindowManagerExt getFlexibleExtImpl() {
        return null;
    }

    default boolean isIOPreloadPkg(java.lang.String pkgName, int userId) {
        return false;
    }
}
