package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IHostingRecordExt {
    default boolean isRPLaunch() {
        return false;
    }

    default void setRPLaunch(boolean rpLaunch) {
    }

    default long getOrder() {
        return 0L;
    }

    default void setOrder(long order) {
    }

    default java.lang.String getCallerName() {
        return null;
    }

    default void setCallerName(java.lang.String callerName) {
    }

    default int getCallerUid() {
        return 0;
    }

    default void setCallerUid(int callerUid) {
    }

    default java.lang.String getAction() {
        return null;
    }

    default void setAction(java.lang.String callerAction) {
    }

    default void setServiceStartType(java.lang.String serviceStartType) {
    }

    default java.lang.String getServiceStartType() {
        return null;
    }
}
