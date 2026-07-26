package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
abstract class ContextHubServiceTransaction {
    private boolean mIsComplete;
    private final java.lang.Integer mMessageSequenceNumber;
    private final java.lang.Long mNanoAppId;
    private final java.lang.String mPackage;
    private final int mTransactionId;
    private final int mTransactionType;

    abstract int onTransact();

    ContextHubServiceTransaction(int id, int type, java.lang.String packageName) {
        this.mIsComplete = false;
        this.mTransactionId = id;
        this.mTransactionType = type;
        this.mNanoAppId = null;
        this.mPackage = packageName;
        this.mMessageSequenceNumber = null;
    }

    ContextHubServiceTransaction(int id, int type, long nanoAppId, java.lang.String packageName) {
        this.mIsComplete = false;
        this.mTransactionId = id;
        this.mTransactionType = type;
        this.mNanoAppId = java.lang.Long.valueOf(nanoAppId);
        this.mPackage = packageName;
        this.mMessageSequenceNumber = null;
    }

    ContextHubServiceTransaction(int id, int type, java.lang.String packageName, int messageSequenceNumber) {
        this.mIsComplete = false;
        this.mTransactionId = id;
        this.mTransactionType = type;
        this.mNanoAppId = null;
        this.mPackage = packageName;
        this.mMessageSequenceNumber = java.lang.Integer.valueOf(messageSequenceNumber);
    }

    void onTransactionComplete(int result) {
    }

    void onQueryResponse(int result, java.util.List<android.hardware.location.NanoAppState> nanoAppStateList) {
    }

    int getTransactionId() {
        return this.mTransactionId;
    }

    int getTransactionType() {
        return this.mTransactionType;
    }

    java.lang.Integer getMessageSequenceNumber() {
        return this.mMessageSequenceNumber;
    }

    long getTimeout(java.util.concurrent.TimeUnit unit) {
        switch (this.mTransactionType) {
            case 0:
                return unit.convert(30L, java.util.concurrent.TimeUnit.SECONDS);
            case 5:
                return unit.convert(1000L, java.util.concurrent.TimeUnit.MILLISECONDS);
            default:
                return unit.convert(5L, java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    void setComplete() {
        this.mIsComplete = true;
    }

    boolean isComplete() {
        return this.mIsComplete;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder out = new java.lang.StringBuilder();
        out.append(android.hardware.location.ContextHubTransaction.typeToString(this.mTransactionType, true));
        out.append(" (");
        if (this.mNanoAppId != null) {
            out.append("appId = 0x");
            out.append(java.lang.Long.toHexString(this.mNanoAppId.longValue()));
            out.append(", ");
        }
        out.append("package = ");
        out.append(this.mPackage);
        if (this.mMessageSequenceNumber != null) {
            out.append(", messageSequenceNumber = ");
            out.append(this.mMessageSequenceNumber);
        }
        out.append(")");
        return out.toString();
    }
}
