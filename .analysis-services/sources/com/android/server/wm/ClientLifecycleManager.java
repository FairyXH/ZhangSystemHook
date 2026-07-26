package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ClientLifecycleManager {
    private static final long ENABLE_BUNDLE_LAUNCH_ACTIVITY_ITEM = 324203798;
    private static final java.lang.String TAG = "ClientLifecycleManager";
    private com.android.server.wm.WindowManagerService mWms;
    final android.util.ArrayMap<android.os.IBinder, android.app.servertransaction.ClientTransaction> mPendingTransactions = new android.util.ArrayMap<>();
    private com.android.server.wm.IClientLifecycleManagerExt mLifecycleManagerExt = (com.android.server.wm.IClientLifecycleManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IClientLifecycleManagerExt.class).base(this).create();

    ClientLifecycleManager() {
    }

    void setWindowManager(com.android.server.wm.WindowManagerService wms) {
        this.mWms = wms;
    }

    @java.lang.Deprecated
    void scheduleTransaction(android.app.servertransaction.ClientTransaction transaction) throws android.os.RemoteException {
        android.app.IApplicationThread client = transaction.getClient();
        try {
            try {
                this.mLifecycleManagerExt.hookSetBinderUxFlag(true);
                transaction.schedule();
                this.mLifecycleManagerExt.hookSetBinderUxFlag(false);
                if (!(client instanceof android.os.Binder)) {
                    try {
                        transaction.recycle();
                    } catch (java.lang.IllegalStateException e) {
                        android.util.Slog.e(TAG, "transaction is already recycle:" + transaction, e);
                    }
                }
            } catch (android.os.RemoteException e2) {
                android.util.Slog.w(TAG, "Failed to deliver transaction for " + client + "\ntransaction=" + transaction);
                throw e2;
            }
        } catch (java.lang.Throwable th) {
            if (!(client instanceof android.os.Binder)) {
                try {
                    transaction.recycle();
                } catch (java.lang.IllegalStateException e3) {
                    android.util.Slog.e(TAG, "transaction is already recycle:" + transaction, e3);
                }
            }
            throw th;
        }
    }

    void scheduleTransactionItemNow(android.app.IApplicationThread client, android.app.servertransaction.ClientTransactionItem transactionItem) throws android.os.RemoteException {
        android.app.servertransaction.ClientTransaction clientTransaction = android.app.servertransaction.ClientTransaction.obtain(client);
        clientTransaction.addTransactionItem(transactionItem);
        scheduleTransaction(clientTransaction);
    }

    void scheduleTransactionItem(android.app.IApplicationThread client, android.app.servertransaction.ClientTransactionItem transactionItem) throws android.os.RemoteException {
        if (com.android.window.flags.Flags.bundleClientTransactionFlag()) {
            android.app.servertransaction.ClientTransaction clientTransaction = getOrCreatePendingTransaction(client);
            clientTransaction.addTransactionItem(transactionItem);
            this.mLifecycleManagerExt.hookScheduleTransactionItem(clientTransaction, transactionItem);
            onClientTransactionItemScheduled(clientTransaction, false);
            return;
        }
        android.app.servertransaction.ClientTransaction clientTransaction2 = android.app.servertransaction.ClientTransaction.obtain(client);
        clientTransaction2.addTransactionItem(transactionItem);
        this.mLifecycleManagerExt.hookScheduleTransactionItem(clientTransaction2, transactionItem);
        scheduleTransaction(clientTransaction2);
    }

    void scheduleTransactionAndLifecycleItems(android.app.IApplicationThread client, android.app.servertransaction.ClientTransactionItem transactionItem, android.app.servertransaction.ActivityLifecycleItem lifecycleItem) throws android.os.RemoteException {
        scheduleTransactionAndLifecycleItems(client, transactionItem, lifecycleItem, false);
    }

    void scheduleTransactionAndLifecycleItems(android.app.IApplicationThread client, android.app.servertransaction.ClientTransactionItem transactionItem, android.app.servertransaction.ActivityLifecycleItem lifecycleItem, boolean shouldDispatchImmediately) throws android.os.RemoteException {
        if (com.android.window.flags.Flags.bundleClientTransactionFlag()) {
            android.app.servertransaction.ClientTransaction clientTransaction = getOrCreatePendingTransaction(client);
            clientTransaction.addTransactionItem(transactionItem);
            clientTransaction.addTransactionItem(lifecycleItem);
            this.mLifecycleManagerExt.hookScheduleTransactionItem(clientTransaction, lifecycleItem);
            onClientTransactionItemScheduled(clientTransaction, shouldDispatchImmediately);
            return;
        }
        android.app.servertransaction.ClientTransaction clientTransaction2 = android.app.servertransaction.ClientTransaction.obtain(client);
        clientTransaction2.addTransactionItem(transactionItem);
        clientTransaction2.addTransactionItem(lifecycleItem);
        this.mLifecycleManagerExt.hookScheduleTransactionItem(clientTransaction2, lifecycleItem);
        scheduleTransaction(clientTransaction2);
    }

    void dispatchPendingTransactions() {
        if (!com.android.window.flags.Flags.bundleClientTransactionFlag() || this.mPendingTransactions.isEmpty()) {
            return;
        }
        android.os.Trace.traceBegin(32L, "clientTransactionsDispatched");
        int size = this.mPendingTransactions.size();
        for (int i = 0; i < size; i++) {
            android.app.servertransaction.ClientTransaction transaction = this.mPendingTransactions.valueAt(i);
            if (transaction == null) {
                android.util.Slog.w(TAG, "skip null transaction while dispatchPendingTransactions.");
            } else {
                try {
                    scheduleTransaction(transaction);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Failed to deliver pending transaction", e);
                }
            }
        }
        this.mPendingTransactions.clear();
        android.os.Trace.traceEnd(32L);
    }

    void dispatchPendingTransaction(android.app.IApplicationThread client) {
        if (com.android.window.flags.Flags.bundleClientTransactionFlag()) {
            if (client == null) {
                android.util.Slog.w(TAG, "skip dispatchPendingTransaction");
                return;
            }
            android.app.servertransaction.ClientTransaction pendingTransaction = this.mPendingTransactions.remove(client.asBinder());
            if (pendingTransaction != null) {
                try {
                    scheduleTransaction(pendingTransaction);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Failed to deliver pending transaction", e);
                }
            }
        }
    }

    void onLayoutContinued() {
        if (shouldDispatchPendingTransactionsImmediately()) {
            dispatchPendingTransactions();
        }
    }

    private android.app.servertransaction.ClientTransaction getOrCreatePendingTransaction(android.app.IApplicationThread client) {
        android.os.IBinder clientBinder = client.asBinder();
        android.app.servertransaction.ClientTransaction pendingTransaction = this.mPendingTransactions.get(clientBinder);
        if (pendingTransaction != null) {
            return pendingTransaction;
        }
        android.app.servertransaction.ClientTransaction transaction = android.app.servertransaction.ClientTransaction.obtain(client);
        this.mPendingTransactions.put(clientBinder, transaction);
        return transaction;
    }

    private void onClientTransactionItemScheduled(android.app.servertransaction.ClientTransaction clientTransaction, boolean shouldDispatchImmediately) throws android.os.RemoteException {
        if (shouldDispatchImmediately || shouldDispatchPendingTransactionsImmediately()) {
            this.mPendingTransactions.remove(clientTransaction.getClient().asBinder());
            scheduleTransaction(clientTransaction);
        }
    }

    private boolean shouldDispatchPendingTransactionsImmediately() {
        if (this.mWms == null) {
            return true;
        }
        return (this.mWms.mWindowPlacerLocked.isLayoutDeferred() || this.mWms.mWindowPlacerLocked.isTraversalScheduled() || this.mWms.mWindowPlacerLocked.isInLayout()) ? false : true;
    }

    static boolean shouldDispatchLaunchActivityItemIndependently(java.lang.String appPackageName, int appUid) {
        return !android.app.compat.CompatChanges.isChangeEnabled(ENABLE_BUNDLE_LAUNCH_ACTIVITY_ITEM, appPackageName, android.os.UserHandle.getUserHandleForUid(appUid));
    }
}
