package com.android.server.locksettings.recoverablekeystore.storage;

/* JADX INFO: loaded from: classes2.dex */
public class RemoteLockscreenValidationSessionStorage {
    private static final long SESSION_TIMEOUT_MILLIS = 600000;
    private static final java.lang.String TAG = "RemoteLockscreenValidation";
    final android.util.SparseArray<com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.LockscreenVerificationSession> mSessionsByUserId = new android.util.SparseArray<>(0);

    public com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.LockscreenVerificationSession get(int userId) {
        com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.LockscreenVerificationSession lockscreenVerificationSession;
        synchronized (this.mSessionsByUserId) {
            lockscreenVerificationSession = this.mSessionsByUserId.get(userId);
        }
        return lockscreenVerificationSession;
    }

    public com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.LockscreenVerificationSession startSession(int userId) {
        com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.LockscreenVerificationSession newSession;
        synchronized (this.mSessionsByUserId) {
            if (this.mSessionsByUserId.get(userId) != null) {
                this.mSessionsByUserId.delete(userId);
            }
            try {
                java.security.KeyPair newKeyPair = com.android.security.SecureBox.genKeyPair();
                newSession = new com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.LockscreenVerificationSession(newKeyPair, android.os.SystemClock.elapsedRealtime());
                this.mSessionsByUserId.put(userId, newSession);
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
        return newSession;
    }

    public void finishSession(int userId) {
        synchronized (this.mSessionsByUserId) {
            this.mSessionsByUserId.delete(userId);
        }
    }

    public java.lang.Runnable getLockscreenValidationCleanupTask() {
        return new com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.LockscreenValidationCleanupTask();
    }

    public class LockscreenVerificationSession {
        private final long mElapsedStartTime;
        private final java.security.KeyPair mKeyPair;

        LockscreenVerificationSession(java.security.KeyPair keyPair, long elapsedStartTime) {
            this.mKeyPair = keyPair;
            this.mElapsedStartTime = elapsedStartTime;
        }

        public java.security.KeyPair getKeyPair() {
            return this.mKeyPair;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long getElapsedStartTimeMillis() {
            return this.mElapsedStartTime;
        }
    }

    private class LockscreenValidationCleanupTask implements java.lang.Runnable {
        private LockscreenValidationCleanupTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                synchronized (com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId) {
                    java.util.ArrayList<java.lang.Integer> keysToRemove = new java.util.ArrayList<>();
                    for (int i = 0; i < com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId.size(); i++) {
                        long now = android.os.SystemClock.elapsedRealtime();
                        long startTime = com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId.valueAt(i).getElapsedStartTimeMillis();
                        if (now - startTime > 600000) {
                            int userId = com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId.keyAt(i);
                            keysToRemove.add(java.lang.Integer.valueOf(userId));
                        }
                    }
                    for (java.lang.Integer userId2 : keysToRemove) {
                        com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId.delete(userId2.intValue());
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Log.e(com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.TAG, "Unexpected exception thrown during LockscreenValidationCleanupTask", e);
            }
        }
    }
}
