package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IStackTracesDumpHelperExt {

    public interface IStaticExt {
        default boolean isSkipAnrDump() {
            return false;
        }

        default void writeTransactionToTrace(java.lang.String tracesFile) {
        }
    }
}
