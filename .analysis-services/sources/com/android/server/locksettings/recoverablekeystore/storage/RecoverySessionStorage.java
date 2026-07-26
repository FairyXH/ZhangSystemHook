package com.android.server.locksettings.recoverablekeystore.storage;

/* JADX INFO: loaded from: classes2.dex */
public class RecoverySessionStorage implements javax.security.auth.Destroyable {
    private final android.util.SparseArray<java.util.ArrayList<com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry>> mSessionsByUid = new android.util.SparseArray<>();

    public com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry get(int uid, java.lang.String sessionId) {
        java.util.ArrayList<com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry> userEntries = this.mSessionsByUid.get(uid);
        if (userEntries == null) {
            return null;
        }
        for (com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry entry : userEntries) {
            if (sessionId.equals(entry.mSessionId)) {
                return entry;
            }
        }
        return null;
    }

    public void add(int uid, com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry entry) {
        if (this.mSessionsByUid.get(uid) == null) {
            this.mSessionsByUid.put(uid, new java.util.ArrayList<>());
        }
        this.mSessionsByUid.get(uid).add(entry);
    }

    public void remove(int uid, final java.lang.String sessionId) {
        if (this.mSessionsByUid.get(uid) == null) {
            return;
        }
        this.mSessionsByUid.get(uid).removeIf(new java.util.function.Predicate() { // from class: com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry) obj).mSessionId.equals(sessionId);
            }
        });
    }

    public void remove(int uid) {
        java.util.ArrayList<com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry> entries = this.mSessionsByUid.get(uid);
        if (entries == null) {
            return;
        }
        for (com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry entry : entries) {
            entry.destroy();
        }
        this.mSessionsByUid.remove(uid);
    }

    public int size() {
        int size = 0;
        int numberOfUsers = this.mSessionsByUid.size();
        for (int i = 0; i < numberOfUsers; i++) {
            java.util.ArrayList<com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry> entries = this.mSessionsByUid.valueAt(i);
            size += entries.size();
        }
        return size;
    }

    @Override // javax.security.auth.Destroyable
    public void destroy() {
        int numberOfUids = this.mSessionsByUid.size();
        for (int i = 0; i < numberOfUids; i++) {
            java.util.ArrayList<com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry> entries = this.mSessionsByUid.valueAt(i);
            for (com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry entry : entries) {
                entry.destroy();
            }
        }
    }

    public static class Entry implements javax.security.auth.Destroyable {
        private final byte[] mKeyClaimant;
        private final byte[] mLskfHash;
        private final java.lang.String mSessionId;
        private final byte[] mVaultParams;

        public Entry(java.lang.String sessionId, byte[] lskfHash, byte[] keyClaimant, byte[] vaultParams) {
            this.mLskfHash = lskfHash;
            this.mSessionId = sessionId;
            this.mKeyClaimant = keyClaimant;
            this.mVaultParams = vaultParams;
        }

        public byte[] getLskfHash() {
            return this.mLskfHash;
        }

        public byte[] getKeyClaimant() {
            return this.mKeyClaimant;
        }

        public byte[] getVaultParams() {
            return this.mVaultParams;
        }

        @Override // javax.security.auth.Destroyable
        public void destroy() {
            java.util.Arrays.fill(this.mLskfHash, (byte) 0);
            java.util.Arrays.fill(this.mKeyClaimant, (byte) 0);
        }
    }
}
