package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class PlatformEncryptionKey {
    private final int mGenerationId;
    private final javax.crypto.SecretKey mKey;

    public PlatformEncryptionKey(int generationId, javax.crypto.SecretKey key) {
        this.mGenerationId = generationId;
        this.mKey = key;
    }

    public int getGenerationId() {
        return this.mGenerationId;
    }

    public javax.crypto.SecretKey getKey() {
        return this.mKey;
    }
}
