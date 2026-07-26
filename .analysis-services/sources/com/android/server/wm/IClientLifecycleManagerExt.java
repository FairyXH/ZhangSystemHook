package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IClientLifecycleManagerExt {
    default void hookScheduleTransactionItem(android.app.servertransaction.ClientTransaction transaction, android.app.servertransaction.ClientTransactionItem transactionItem) {
    }

    default void hookSetBinderUxFlag(boolean applyToUx) {
    }
}
