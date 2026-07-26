package com.android.server.backup.internal;

/* JADX INFO: loaded from: classes.dex */
public class LifecycleOperationStorage implements com.android.server.backup.OperationStorage {
    private static final java.lang.String TAG = "LifecycleOperationStorage";
    private final int mUserId;
    private final java.lang.Object mOperationsLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.backup.internal.Operation> mOperations = new android.util.SparseArray<>();
    private final java.util.Map<java.lang.String, java.util.Set<java.lang.Integer>> mOpTokensByPackage = new java.util.HashMap();

    public LifecycleOperationStorage(int userId) {
        this.mUserId = userId;
    }

    @Override // com.android.server.backup.OperationStorage
    public void registerOperation(int token, int initialState, com.android.server.backup.BackupRestoreTask task, int type) {
        registerOperationForPackages(token, initialState, com.google.android.collect.Sets.newHashSet(), task, type);
    }

    @Override // com.android.server.backup.OperationStorage
    public void registerOperationForPackages(int token, int initialState, java.util.Set<java.lang.String> packageNames, com.android.server.backup.BackupRestoreTask task, int type) {
        synchronized (this.mOperationsLock) {
            this.mOperations.put(token, new com.android.server.backup.internal.Operation(initialState, task, type));
            for (java.lang.String packageName : packageNames) {
                java.util.Set<java.lang.Integer> tokens = this.mOpTokensByPackage.get(packageName);
                if (tokens == null) {
                    tokens = new java.util.HashSet();
                }
                tokens.add(java.lang.Integer.valueOf(token));
                this.mOpTokensByPackage.put(packageName, tokens);
            }
        }
    }

    @Override // com.android.server.backup.OperationStorage
    public void removeOperation(int token) {
        synchronized (this.mOperationsLock) {
            this.mOperations.remove(token);
            java.util.Set<java.lang.String> packagesWithTokens = this.mOpTokensByPackage.keySet();
            for (java.lang.String packageName : packagesWithTokens) {
                java.util.Set<java.lang.Integer> tokens = this.mOpTokensByPackage.get(packageName);
                if (tokens != null) {
                    tokens.remove(java.lang.Integer.valueOf(token));
                    this.mOpTokensByPackage.put(packageName, tokens);
                }
            }
        }
    }

    @Override // com.android.server.backup.OperationStorage
    public int numOperations() {
        int size;
        synchronized (this.mOperationsLock) {
            size = this.mOperations.size();
        }
        return size;
    }

    @Override // com.android.server.backup.OperationStorage
    public boolean isBackupOperationInProgress() {
        synchronized (this.mOperationsLock) {
            for (int i = 0; i < this.mOperations.size(); i++) {
                com.android.server.backup.internal.Operation op = this.mOperations.valueAt(i);
                if (op.type == 2 && op.state == 0) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.android.server.backup.OperationStorage
    public java.util.Set<java.lang.Integer> operationTokensForPackage(java.lang.String packageName) {
        java.util.Set<java.lang.Integer> result;
        synchronized (this.mOperationsLock) {
            java.util.Collection<? extends java.lang.Integer> tokens = (java.util.Set) this.mOpTokensByPackage.get(packageName);
            result = com.google.android.collect.Sets.newHashSet();
            if (tokens != null) {
                result.addAll(tokens);
            }
        }
        return result;
    }

    @Override // com.android.server.backup.OperationStorage
    public java.util.Set<java.lang.Integer> operationTokensForOpType(int type) {
        java.util.Set<java.lang.Integer> tokens = com.google.android.collect.Sets.newHashSet();
        synchronized (this.mOperationsLock) {
            for (int i = 0; i < this.mOperations.size(); i++) {
                com.android.server.backup.internal.Operation op = this.mOperations.valueAt(i);
                int token = this.mOperations.keyAt(i);
                if (op.type == type) {
                    tokens.add(java.lang.Integer.valueOf(token));
                }
            }
        }
        return tokens;
    }

    @Override // com.android.server.backup.OperationStorage
    public java.util.Set<java.lang.Integer> operationTokensForOpState(int state) {
        java.util.Set<java.lang.Integer> tokens = com.google.android.collect.Sets.newHashSet();
        synchronized (this.mOperationsLock) {
            for (int i = 0; i < this.mOperations.size(); i++) {
                com.android.server.backup.internal.Operation op = this.mOperations.valueAt(i);
                int token = this.mOperations.keyAt(i);
                if (op.state == state) {
                    tokens.add(java.lang.Integer.valueOf(token));
                }
            }
        }
        return tokens;
    }

    public boolean waitUntilOperationComplete(int token, java.util.function.IntConsumer callback) {
        com.android.server.backup.internal.Operation op;
        int finalState = 0;
        synchronized (this.mOperationsLock) {
            while (true) {
                op = this.mOperations.get(token);
                if (op == null) {
                    break;
                }
                if (op.state == 0) {
                    try {
                        this.mOperationsLock.wait();
                    } catch (java.lang.InterruptedException e) {
                        android.util.Slog.w(TAG, "Waiting on mOperationsLock: ", e);
                    }
                } else {
                    finalState = op.state;
                    break;
                }
            }
        }
        removeOperation(token);
        if (op != null) {
            callback.accept(op.type);
        }
        return finalState == 1;
    }

    public void onOperationComplete(int token, long result, java.util.function.Consumer<com.android.server.backup.BackupRestoreTask> callback) {
        com.android.server.backup.internal.Operation op;
        synchronized (this.mOperationsLock) {
            op = this.mOperations.get(token);
            if (op != null) {
                if (op.state == -1) {
                    op = null;
                    this.mOperations.remove(token);
                } else if (op.state == 1) {
                    android.util.Slog.w(TAG, "[UserID:" + this.mUserId + "] Received duplicate ack for token=" + java.lang.Integer.toHexString(token));
                    op = null;
                    this.mOperations.remove(token);
                } else if (op.state == 0) {
                    op.state = 1;
                }
            }
            this.mOperationsLock.notifyAll();
        }
        if (op != null && op.callback != null) {
            callback.accept(op.callback);
        }
    }

    public void cancelOperation(int token, final boolean cancelAll, java.util.function.IntConsumer operationTimedOutCallback) {
        com.android.server.backup.internal.Operation op;
        synchronized (this.mOperationsLock) {
            op = this.mOperations.get(token);
            int state = op != null ? op.state : -1;
            if (state == 1) {
                android.util.Slog.w(TAG, "[UserID:" + this.mUserId + "] Operation already got an ack.Should have been removed from mCurrentOperations.");
                op = null;
                this.mOperations.delete(token);
            } else if (state == 0) {
                android.util.Slog.v(TAG, "[UserID:" + this.mUserId + "] Cancel: token=" + java.lang.Integer.toHexString(token));
                op.state = -1;
                operationTimedOutCallback.accept(op.type);
            }
            this.mOperationsLock.notifyAll();
        }
        if (op != null && op.callback != null) {
            final com.android.server.backup.internal.Operation finalOp = op;
            java.lang.Runnable callbackCancel = new java.lang.Runnable() { // from class: com.android.server.backup.internal.LifecycleOperationStorage.1
                @Override // java.lang.Runnable
                public void run() {
                    finalOp.callback.handleCancel(cancelAll);
                }
            };
            new java.lang.Thread(callbackCancel, "callbackCancel").start();
        }
    }
}
