package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IProcessListWrapper {
    public static final int PRELOAD_APP_ADJ = 850;
    public static final int THREAD_GROUP_SS_FG = 15;
    public static final int THREAD_GROUP_SS_TOP = 14;

    default boolean writeLmkd(java.nio.ByteBuffer buf, java.nio.ByteBuffer repl) {
        return false;
    }

    default void onBootComplete() {
    }
}
