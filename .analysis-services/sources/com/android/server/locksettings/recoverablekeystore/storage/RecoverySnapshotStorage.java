package com.android.server.locksettings.recoverablekeystore.storage;

/* JADX INFO: loaded from: classes2.dex */
public class RecoverySnapshotStorage {
    private static final java.lang.String ROOT_PATH = "system";
    private static final java.lang.String STORAGE_PATH = "recoverablekeystore/snapshots/";
    private static final java.lang.String TAG = "RecoverySnapshotStorage";
    private final android.util.SparseArray<android.security.keystore.recovery.KeyChainSnapshot> mSnapshotByUid = new android.util.SparseArray<>();
    private final java.io.File rootDirectory;

    public static com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage newInstance() {
        return new com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage(new java.io.File(android.os.Environment.getDataDirectory(), "system"));
    }

    public RecoverySnapshotStorage(java.io.File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public synchronized void put(int uid, android.security.keystore.recovery.KeyChainSnapshot snapshot) {
        this.mSnapshotByUid.put(uid, snapshot);
        try {
            writeToDisk(uid, snapshot);
        } catch (java.io.IOException | java.security.cert.CertificateEncodingException e) {
            android.util.Log.e(TAG, java.lang.String.format(java.util.Locale.US, "Error persisting snapshot for %d to disk", java.lang.Integer.valueOf(uid)), e);
        }
    }

    public synchronized android.security.keystore.recovery.KeyChainSnapshot get(int uid) {
        android.security.keystore.recovery.KeyChainSnapshot snapshot = this.mSnapshotByUid.get(uid);
        if (snapshot != null) {
            return snapshot;
        }
        try {
            return readFromDisk(uid);
        } catch (com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException | java.io.IOException e) {
            android.util.Log.e(TAG, java.lang.String.format(java.util.Locale.US, "Error reading snapshot for %d from disk", java.lang.Integer.valueOf(uid)), e);
            return null;
        }
    }

    public synchronized void remove(int uid) {
        this.mSnapshotByUid.remove(uid);
        getSnapshotFile(uid).delete();
    }

    private void writeToDisk(int uid, android.security.keystore.recovery.KeyChainSnapshot snapshot) throws java.lang.Exception {
        java.io.File snapshotFile = getSnapshotFile(uid);
        try {
            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(snapshotFile);
            try {
                com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSerializer.serialize(snapshot, fileOutputStream);
                fileOutputStream.close();
            } catch (java.lang.Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | java.security.cert.CertificateEncodingException e) {
            snapshotFile.delete();
            throw e;
        }
    }

    private android.security.keystore.recovery.KeyChainSnapshot readFromDisk(int uid) throws java.lang.Exception {
        java.io.File snapshotFile = getSnapshotFile(uid);
        try {
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(snapshotFile);
            try {
                android.security.keystore.recovery.KeyChainSnapshot keyChainSnapshotDeserialize = com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotDeserializer.deserialize(fileInputStream);
                fileInputStream.close();
                return keyChainSnapshotDeserialize;
            } catch (java.lang.Throwable th) {
                try {
                    fileInputStream.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException | java.io.IOException e) {
            snapshotFile.delete();
            throw e;
        }
    }

    private java.io.File getSnapshotFile(int uid) {
        java.io.File folder = getStorageFolder();
        java.lang.String fileName = getSnapshotFileName(uid);
        return new java.io.File(folder, fileName);
    }

    private java.lang.String getSnapshotFileName(int uid) {
        return java.lang.String.format(java.util.Locale.US, "%d.xml", java.lang.Integer.valueOf(uid));
    }

    private java.io.File getStorageFolder() {
        java.io.File folder = new java.io.File(this.rootDirectory, STORAGE_PATH);
        folder.mkdirs();
        return folder;
    }
}
