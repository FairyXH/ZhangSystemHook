package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public interface RebootEscrowProviderInterface {
    public static final int TYPE_HAL = 0;
    public static final int TYPE_SERVER_BASED = 1;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface RebootEscrowProviderType {
    }

    void clearRebootEscrowKey();

    com.android.server.locksettings.RebootEscrowKey getAndClearRebootEscrowKey(javax.crypto.SecretKey secretKey) throws java.io.IOException;

    int getType();

    boolean hasRebootEscrowSupport();

    boolean storeRebootEscrowKey(com.android.server.locksettings.RebootEscrowKey rebootEscrowKey, javax.crypto.SecretKey secretKey);
}
