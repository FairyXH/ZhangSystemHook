package com.android.server.biometrics.log;

/* JADX INFO: loaded from: classes.dex */
class BiometricContextSessionInfo {
    private final com.android.internal.logging.InstanceId mId;
    private final java.util.concurrent.atomic.AtomicInteger mOrder = new java.util.concurrent.atomic.AtomicInteger(0);

    BiometricContextSessionInfo(com.android.internal.logging.InstanceId id) {
        this.mId = id;
    }

    public int getId() {
        return this.mId.getId();
    }

    public int getOrder() {
        return this.mOrder.get();
    }

    public int getOrderAndIncrement() {
        return this.mOrder.getAndIncrement();
    }

    public java.lang.String toString() {
        return "[sid: " + this.mId.getId() + "]";
    }
}
