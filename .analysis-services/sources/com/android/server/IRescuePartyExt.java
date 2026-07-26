package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IRescuePartyExt {
    default boolean checkAndWaitForToFinishDumpService() {
        return false;
    }

    default void checkForDumpService() {
    }
}
