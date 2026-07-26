package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
class ContextHubTransactionManager {
    private static final int MAX_PENDING_REQUESTS = 10000;
    private static final int NUM_TRANSACTION_RECORDS = 20;
    private static final java.lang.String TAG = "ContextHubTransactionManager";
    private final com.android.server.location.contexthub.ContextHubClientManager mClientManager;
    private final com.android.server.location.contexthub.IContextHubWrapper mContextHubProxy;
    private final com.android.server.location.contexthub.NanoAppStateManager mNanoAppStateManager;
    private final java.util.ArrayDeque<com.android.server.location.contexthub.ContextHubServiceTransaction> mTransactionQueue = new java.util.ArrayDeque<>();
    private final java.util.concurrent.atomic.AtomicInteger mNextAvailableId = new java.util.concurrent.atomic.AtomicInteger();
    private final java.util.concurrent.atomic.AtomicInteger mNextAvailableMessageSequenceNumber = new java.util.concurrent.atomic.AtomicInteger(new java.util.Random().nextInt(kotlinx.coroutines.internal.LockFreeTaskQueueCore.MAX_CAPACITY_MASK));
    private final java.util.concurrent.ScheduledThreadPoolExecutor mTimeoutExecutor = new java.util.concurrent.ScheduledThreadPoolExecutor(1);
    private java.util.concurrent.ScheduledFuture<?> mTimeoutFuture = null;
    private final com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<com.android.server.location.contexthub.ContextHubTransactionManager.TransactionRecord> mTransactionRecordDeque = new com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<>(20);

    private class TransactionRecord {
        private final long mTimestamp = java.lang.System.currentTimeMillis();
        private final java.lang.String mTransaction;

        TransactionRecord(java.lang.String transaction) {
            this.mTransaction = transaction;
        }

        public java.lang.String toString() {
            return com.android.server.location.contexthub.ContextHubServiceUtil.formatDateFromTimestamp(this.mTimestamp) + " " + this.mTransaction;
        }
    }

    ContextHubTransactionManager(com.android.server.location.contexthub.IContextHubWrapper contextHubProxy, com.android.server.location.contexthub.ContextHubClientManager clientManager, com.android.server.location.contexthub.NanoAppStateManager nanoAppStateManager) {
        this.mContextHubProxy = contextHubProxy;
        this.mClientManager = clientManager;
        this.mNanoAppStateManager = nanoAppStateManager;
    }

    com.android.server.location.contexthub.ContextHubServiceTransaction createLoadTransaction(final int contextHubId, final android.hardware.location.NanoAppBinary nanoAppBinary, final android.hardware.location.IContextHubTransactionCallback onCompleteCallback, java.lang.String packageName) {
        return new com.android.server.location.contexthub.ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 0, nanoAppBinary.getNanoAppId(), packageName) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.1
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            int onTransact() {
                try {
                    return com.android.server.location.contexthub.ContextHubTransactionManager.this.mContextHubProxy.loadNanoapp(contextHubId, nanoAppBinary, getTransactionId());
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while trying to load nanoapp with ID 0x" + java.lang.Long.toHexString(nanoAppBinary.getNanoAppId()), e);
                    return 1;
                }
            }

            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            void onTransactionComplete(int result) {
                com.android.server.location.contexthub.ContextHubStatsLog.write(401, nanoAppBinary.getNanoAppId(), nanoAppBinary.getNanoAppVersion(), 1, com.android.server.location.contexthub.ContextHubTransactionManager.this.toStatsTransactionResult(result));
                com.android.server.location.contexthub.ContextHubEventLogger.getInstance().logNanoappLoad(contextHubId, nanoAppBinary.getNanoAppId(), nanoAppBinary.getNanoAppVersion(), nanoAppBinary.getBinary().length, result == 0);
                if (result == 0) {
                    com.android.server.location.contexthub.ContextHubTransactionManager.this.mNanoAppStateManager.addNanoAppInstance(contextHubId, nanoAppBinary.getNanoAppId(), nanoAppBinary.getNanoAppVersion());
                }
                try {
                    onCompleteCallback.onTransactionComplete(result);
                    if (result == 0) {
                        com.android.server.location.contexthub.ContextHubTransactionManager.this.mClientManager.onNanoAppLoaded(contextHubId, nanoAppBinary.getNanoAppId());
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while calling client onTransactionComplete", e);
                }
            }
        };
    }

    com.android.server.location.contexthub.ContextHubServiceTransaction createUnloadTransaction(final int contextHubId, final long nanoAppId, final android.hardware.location.IContextHubTransactionCallback onCompleteCallback, java.lang.String packageName) {
        return new com.android.server.location.contexthub.ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 1, nanoAppId, packageName) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.2
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            int onTransact() {
                try {
                    return com.android.server.location.contexthub.ContextHubTransactionManager.this.mContextHubProxy.unloadNanoapp(contextHubId, nanoAppId, getTransactionId());
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while trying to unload nanoapp with ID 0x" + java.lang.Long.toHexString(nanoAppId), e);
                    return 1;
                }
            }

            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            void onTransactionComplete(int result) {
                com.android.server.location.contexthub.ContextHubStatsLog.write(401, nanoAppId, 0, 2, com.android.server.location.contexthub.ContextHubTransactionManager.this.toStatsTransactionResult(result));
                com.android.server.location.contexthub.ContextHubEventLogger.getInstance().logNanoappUnload(contextHubId, nanoAppId, result == 0);
                if (result == 0) {
                    com.android.server.location.contexthub.ContextHubTransactionManager.this.mNanoAppStateManager.removeNanoAppInstance(contextHubId, nanoAppId);
                }
                try {
                    onCompleteCallback.onTransactionComplete(result);
                    if (result == 0) {
                        com.android.server.location.contexthub.ContextHubTransactionManager.this.mClientManager.onNanoAppUnloaded(contextHubId, nanoAppId);
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while calling client onTransactionComplete", e);
                }
            }
        };
    }

    com.android.server.location.contexthub.ContextHubServiceTransaction createEnableTransaction(final int contextHubId, final long nanoAppId, final android.hardware.location.IContextHubTransactionCallback onCompleteCallback, java.lang.String packageName) {
        return new com.android.server.location.contexthub.ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 2, packageName) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.3
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            int onTransact() {
                try {
                    return com.android.server.location.contexthub.ContextHubTransactionManager.this.mContextHubProxy.enableNanoapp(contextHubId, nanoAppId, getTransactionId());
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while trying to enable nanoapp with ID 0x" + java.lang.Long.toHexString(nanoAppId), e);
                    return 1;
                }
            }

            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            void onTransactionComplete(int result) {
                try {
                    onCompleteCallback.onTransactionComplete(result);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while calling client onTransactionComplete", e);
                }
            }
        };
    }

    com.android.server.location.contexthub.ContextHubServiceTransaction createDisableTransaction(final int contextHubId, final long nanoAppId, final android.hardware.location.IContextHubTransactionCallback onCompleteCallback, java.lang.String packageName) {
        return new com.android.server.location.contexthub.ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 3, packageName) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.4
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            int onTransact() {
                try {
                    return com.android.server.location.contexthub.ContextHubTransactionManager.this.mContextHubProxy.disableNanoapp(contextHubId, nanoAppId, getTransactionId());
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while trying to disable nanoapp with ID 0x" + java.lang.Long.toHexString(nanoAppId), e);
                    return 1;
                }
            }

            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            void onTransactionComplete(int result) {
                try {
                    onCompleteCallback.onTransactionComplete(result);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while calling client onTransactionComplete", e);
                }
            }
        };
    }

    com.android.server.location.contexthub.ContextHubServiceTransaction createMessageTransaction(final short hostEndpointId, final int contextHubId, final android.hardware.location.NanoAppMessage message, final android.hardware.location.IContextHubTransactionCallback transactionCallback, java.lang.String packageName) {
        return new com.android.server.location.contexthub.ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 5, packageName, this.mNextAvailableMessageSequenceNumber.getAndIncrement()) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.5
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            int onTransact() {
                try {
                    message.setIsReliable(true);
                    message.setMessageSequenceNumber(getMessageSequenceNumber().intValue());
                    return com.android.server.location.contexthub.ContextHubTransactionManager.this.mContextHubProxy.sendMessageToContextHub(hostEndpointId, contextHubId, message);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while trying to send a reliable message", e);
                    return 1;
                }
            }

            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            void onTransactionComplete(int result) {
                try {
                    transactionCallback.onTransactionComplete(result);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while calling client onTransactionComplete", e);
                }
            }
        };
    }

    com.android.server.location.contexthub.ContextHubServiceTransaction createQueryTransaction(final int contextHubId, final android.hardware.location.IContextHubTransactionCallback onCompleteCallback, java.lang.String packageName) {
        return new com.android.server.location.contexthub.ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 4, packageName) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.6
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            int onTransact() {
                try {
                    return com.android.server.location.contexthub.ContextHubTransactionManager.this.mContextHubProxy.queryNanoapps(contextHubId);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while trying to query for nanoapps", e);
                    return 1;
                }
            }

            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            void onTransactionComplete(int result) {
                onQueryResponse(result, java.util.Collections.emptyList());
            }

            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            void onQueryResponse(int result, java.util.List<android.hardware.location.NanoAppState> nanoAppStateList) {
                try {
                    onCompleteCallback.onQueryResponse(result, nanoAppStateList);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubTransactionManager.TAG, "RemoteException while calling client onQueryComplete", e);
                }
            }
        };
    }

    synchronized void addTransaction(com.android.server.location.contexthub.ContextHubServiceTransaction transaction) throws java.lang.IllegalStateException {
        if (transaction == null) {
            return;
        }
        if (this.mTransactionQueue.size() == 10000) {
            throw new java.lang.IllegalStateException("Transaction queue is full (capacity = 10000)");
        }
        this.mTransactionQueue.add(transaction);
        this.mTransactionRecordDeque.add(new com.android.server.location.contexthub.ContextHubTransactionManager.TransactionRecord(transaction.toString()));
        if (this.mTransactionQueue.size() == 1) {
            startNextTransaction();
        }
    }

    synchronized void onTransactionResponse(int transactionId, boolean success) {
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionQueue.peek();
        if (transaction == null) {
            android.util.Log.w(TAG, "Received unexpected transaction response (no transaction pending)");
        } else if (transaction.getTransactionId() != transactionId) {
            android.util.Log.w(TAG, "Received unexpected transaction response (expected ID = " + transaction.getTransactionId() + ", received ID = " + transactionId + ")");
        } else {
            transaction.onTransactionComplete(success ? 0 : 5);
            removeTransactionAndStartNext();
        }
    }

    synchronized void onMessageDeliveryResponse(int messageSequenceNumber, boolean success) {
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionQueue.peek();
        if (transaction == null) {
            android.util.Log.w(TAG, "Received unexpected transaction response (no transaction pending)");
            return;
        }
        java.lang.Integer transactionMessageSequenceNumber = transaction.getMessageSequenceNumber();
        if (transaction.getTransactionType() == 5 && transactionMessageSequenceNumber != null && transactionMessageSequenceNumber.intValue() == messageSequenceNumber) {
            transaction.onTransactionComplete(success ? 0 : 5);
            removeTransactionAndStartNext();
            return;
        }
        android.util.Log.w(TAG, "Received unexpected message transaction response (expected message sequence number = " + transaction.getMessageSequenceNumber() + ", received messageSequenceNumber = " + messageSequenceNumber + ")");
    }

    synchronized void onQueryResponse(java.util.List<android.hardware.location.NanoAppState> nanoAppStateList) {
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionQueue.peek();
        if (transaction == null) {
            android.util.Log.w(TAG, "Received unexpected query response (no transaction pending)");
        } else if (transaction.getTransactionType() != 4) {
            android.util.Log.w(TAG, "Received unexpected query response (expected " + transaction + ")");
        } else {
            transaction.onQueryResponse(0, nanoAppStateList);
            removeTransactionAndStartNext();
        }
    }

    synchronized void onHubReset() {
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionQueue.peek();
        if (transaction == null) {
            return;
        }
        removeTransactionAndStartNext();
    }

    private void removeTransactionAndStartNext() {
        if (this.mTimeoutFuture != null) {
            this.mTimeoutFuture.cancel(false);
            this.mTimeoutFuture = null;
        }
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionQueue.remove();
        transaction.setComplete();
        if (!this.mTransactionQueue.isEmpty()) {
            startNextTransaction();
        }
    }

    private void startNextTransaction() {
        int result = 1;
        while (result != 0 && !this.mTransactionQueue.isEmpty()) {
            final com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionQueue.peek();
            result = transaction.onTransact();
            if (result == 0) {
                java.lang.Runnable onTimeoutFunc = new java.lang.Runnable() { // from class: com.android.server.location.contexthub.ContextHubTransactionManager$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$startNextTransaction$0(transaction);
                    }
                };
                long timeoutMs = transaction.getTimeout(java.util.concurrent.TimeUnit.MILLISECONDS);
                try {
                    this.mTimeoutFuture = this.mTimeoutExecutor.schedule(onTimeoutFunc, timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS);
                } catch (java.lang.Exception e) {
                    android.util.Log.e(TAG, "Error when schedule a timer", e);
                }
            } else {
                transaction.onTransactionComplete(com.android.server.location.contexthub.ContextHubServiceUtil.toTransactionResult(result));
                this.mTransactionQueue.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startNextTransaction$0(com.android.server.location.contexthub.ContextHubServiceTransaction transaction) {
        synchronized (this) {
            if (!transaction.isComplete()) {
                android.util.Log.d(TAG, transaction + " timed out");
                transaction.onTransactionComplete(6);
                removeTransactionAndStartNext();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int toStatsTransactionResult(int result) {
        switch (result) {
            case 0:
                return 0;
            case 1:
            default:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
        }
    }

    public java.lang.String toString() {
        com.android.server.location.contexthub.ContextHubServiceTransaction[] arr;
        java.lang.StringBuilder sb = new java.lang.StringBuilder(100);
        synchronized (this) {
            arr = (com.android.server.location.contexthub.ContextHubServiceTransaction[]) this.mTransactionQueue.toArray(new com.android.server.location.contexthub.ContextHubServiceTransaction[0]);
        }
        for (int i = 0; i < arr.length; i++) {
            sb.append(i + ": " + arr[i] + "\n");
        }
        sb.append("Transaction History:\n");
        java.util.Iterator<com.android.server.location.contexthub.ContextHubTransactionManager.TransactionRecord> iterator = this.mTransactionRecordDeque.descendingIterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next() + "\n");
        }
        return sb.toString();
    }
}
