package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface ISystemServiceManagerExt {
    default void colorSystemServiceOnBootPhase(int phase) {
    }

    default void initTimeCosted() {
    }

    default boolean isDebuggable() {
        return false;
    }

    default void setCustomOnWhatToSwitch() {
    }

    default void setCustomOnWhatToStart() {
    }

    default void recordTimeOut(long time, int warnTime, java.lang.String serviceName) {
    }

    default void onUserExit(int curUserId) {
    }
}
