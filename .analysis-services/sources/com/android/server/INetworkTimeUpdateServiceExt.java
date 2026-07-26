package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface INetworkTimeUpdateServiceExt {
    default void init(android.content.Context context, com.android.server.timedetector.NetworkTimeUpdateService timeUpdateService) {
    }

    default void checkSystemTime() {
    }

    default boolean isAutoTimeOrSkipPollNetworkTime() {
        return false;
    }
}
