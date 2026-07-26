package com.android.server.locksettings.recoverablekeystore.storage;

/* JADX INFO: loaded from: classes2.dex */
public class CleanupManager {
    private static final java.lang.String TAG = "CleanupManager";
    private final com.android.server.locksettings.recoverablekeystore.storage.ApplicationKeyStorage mApplicationKeyStorage;
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb mDatabase;
    private java.util.Map<java.lang.Integer, java.lang.Long> mSerialNumbers;
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage mSnapshotStorage;
    private final android.os.UserManager mUserManager;

    public static com.android.server.locksettings.recoverablekeystore.storage.CleanupManager getInstance(android.content.Context context, com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage snapshotStorage, com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb recoverableKeyStoreDb, com.android.server.locksettings.recoverablekeystore.storage.ApplicationKeyStorage applicationKeyStorage) {
        return new com.android.server.locksettings.recoverablekeystore.storage.CleanupManager(snapshotStorage, recoverableKeyStoreDb, android.os.UserManager.get(context), applicationKeyStorage);
    }

    CleanupManager(com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage snapshotStorage, com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb recoverableKeyStoreDb, android.os.UserManager userManager, com.android.server.locksettings.recoverablekeystore.storage.ApplicationKeyStorage applicationKeyStorage) {
        this.mSnapshotStorage = snapshotStorage;
        this.mDatabase = recoverableKeyStoreDb;
        this.mUserManager = userManager;
        this.mApplicationKeyStorage = applicationKeyStorage;
    }

    public synchronized void registerRecoveryAgent(int userId, int uid) {
        if (this.mSerialNumbers == null) {
            verifyKnownUsers();
        }
        java.lang.Long storedSerialNumber = this.mSerialNumbers.get(java.lang.Integer.valueOf(userId));
        if (storedSerialNumber == null) {
            storedSerialNumber = -1L;
        }
        if (storedSerialNumber.longValue() != -1) {
            return;
        }
        long currentSerialNumber = this.mUserManager.getSerialNumberForUser(android.os.UserHandle.of(userId));
        if (currentSerialNumber != -1) {
            storeUserSerialNumber(userId, currentSerialNumber);
        }
    }

    public synchronized void verifyKnownUsers() {
        this.mSerialNumbers = this.mDatabase.getUserSerialNumbers();
        java.util.List<java.lang.Integer> deletedUserIds = new java.util.ArrayList<java.lang.Integer>() { // from class: com.android.server.locksettings.recoverablekeystore.storage.CleanupManager.1
        };
        for (java.util.Map.Entry<java.lang.Integer, java.lang.Long> entry : this.mSerialNumbers.entrySet()) {
            java.lang.Integer userId = entry.getKey();
            java.lang.Long storedSerialNumber = entry.getValue();
            if (storedSerialNumber == null) {
                storedSerialNumber = -1L;
            }
            long currentSerialNumber = this.mUserManager.getSerialNumberForUser(android.os.UserHandle.of(userId.intValue()));
            if (currentSerialNumber == -1) {
                deletedUserIds.add(userId);
                removeDataForUser(userId.intValue());
            } else if (storedSerialNumber.longValue() == -1) {
                storeUserSerialNumber(userId.intValue(), currentSerialNumber);
            } else if (storedSerialNumber.longValue() != currentSerialNumber) {
                deletedUserIds.add(userId);
                removeDataForUser(userId.intValue());
                storeUserSerialNumber(userId.intValue(), currentSerialNumber);
            }
        }
        for (java.lang.Integer deletedUser : deletedUserIds) {
            this.mSerialNumbers.remove(deletedUser);
        }
    }

    private void storeUserSerialNumber(int userId, long userSerialNumber) {
        android.util.Log.d(TAG, "Storing serial number for user " + userId + ".");
        this.mSerialNumbers.put(java.lang.Integer.valueOf(userId), java.lang.Long.valueOf(userSerialNumber));
        this.mDatabase.setUserSerialNumber(userId, userSerialNumber);
    }

    private void removeDataForUser(int userId) {
        android.util.Log.d(TAG, "Removing data for user " + userId + ".");
        java.util.List<java.lang.Integer> recoveryAgents = this.mDatabase.getRecoveryAgents(userId);
        for (java.lang.Integer uid : recoveryAgents) {
            this.mSnapshotStorage.remove(uid.intValue());
            removeAllKeysForRecoveryAgent(userId, uid.intValue());
        }
        this.mDatabase.removeUserFromAllTables(userId);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    private void removeAllKeysForRecoveryAgent(int userId, int uid) {
        int generationId = this.mDatabase.getPlatformKeyGenerationId(userId);
        java.util.Map<java.lang.String, com.android.server.locksettings.recoverablekeystore.WrappedKey> allKeys = this.mDatabase.getAllKeys(userId, uid, generationId);
        for (java.lang.String alias : allKeys.keySet()) {
            try {
                this.mApplicationKeyStorage.deleteEntry(userId, uid, alias);
            } catch (android.os.ServiceSpecificException e) {
                android.util.Log.e(TAG, "Error while removing recoverable key " + alias + " : " + e);
            }
        }
    }
}
