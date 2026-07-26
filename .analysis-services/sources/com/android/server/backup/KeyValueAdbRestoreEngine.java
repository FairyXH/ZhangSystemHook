package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class KeyValueAdbRestoreEngine implements java.lang.Runnable {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "KeyValueAdbRestoreEngine";
    private final android.app.IBackupAgent mAgent;
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final java.io.File mDataDir;
    private final android.os.ParcelFileDescriptor mInFD;
    private final com.android.server.backup.FileMetadata mInfo;
    private final int mToken;

    public KeyValueAdbRestoreEngine(com.android.server.backup.UserBackupManagerService backupManagerService, java.io.File dataDir, com.android.server.backup.FileMetadata info, android.os.ParcelFileDescriptor inFD, android.app.IBackupAgent agent, int token) {
        this.mBackupManagerService = backupManagerService;
        this.mDataDir = dataDir;
        this.mInfo = info;
        this.mInFD = inFD;
        this.mAgent = agent;
        this.mToken = token;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            java.io.File restoreData = prepareRestoreData(this.mInfo, this.mInFD);
            invokeAgentForAdbRestore(this.mAgent, this.mInfo, restoreData);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private java.io.File prepareRestoreData(com.android.server.backup.FileMetadata info, android.os.ParcelFileDescriptor inFD) throws java.io.IOException {
        java.lang.String pkg = info.packageName;
        java.io.File restoreDataName = new java.io.File(this.mDataDir, pkg + ".restore");
        java.io.File sortedDataName = new java.io.File(this.mDataDir, pkg + ".sorted");
        android.app.backup.FullBackup.restoreFile(inFD, info.size, info.type, info.mode, info.mtime, restoreDataName);
        sortKeyValueData(restoreDataName, sortedDataName);
        return sortedDataName;
    }

    private void invokeAgentForAdbRestore(android.app.IBackupAgent agent, com.android.server.backup.FileMetadata info, java.io.File restoreData) throws java.io.IOException {
        java.lang.String pkg = info.packageName;
        java.io.File newStateName = new java.io.File(this.mDataDir, pkg + com.android.server.backup.keyvalue.KeyValueBackupTask.NEW_STATE_FILE_SUFFIX);
        try {
            android.os.ParcelFileDescriptor backupData = android.os.ParcelFileDescriptor.open(restoreData, 268435456);
            android.os.ParcelFileDescriptor newState = android.os.ParcelFileDescriptor.open(newStateName, 1006632960);
            agent.doRestore(backupData, info.version, newState, this.mToken, this.mBackupManagerService.getBackupManagerBinder());
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Exception calling doRestore on agent: " + e);
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Exception opening file. " + e2);
        }
    }

    private void sortKeyValueData(java.io.File restoreData, java.io.File sortedData) throws java.io.IOException {
        java.io.FileInputStream inputStream = null;
        java.io.FileOutputStream outputStream = null;
        try {
            inputStream = new java.io.FileInputStream(restoreData);
            outputStream = new java.io.FileOutputStream(sortedData);
            android.app.backup.BackupDataInput reader = new android.app.backup.BackupDataInput(inputStream.getFD());
            android.app.backup.BackupDataOutput writer = new android.app.backup.BackupDataOutput(outputStream.getFD());
            copyKeysInLexicalOrder(reader, writer);
            libcore.io.IoUtils.closeQuietly(inputStream);
            libcore.io.IoUtils.closeQuietly(outputStream);
        } catch (java.lang.Throwable th) {
            if (inputStream != null) {
                libcore.io.IoUtils.closeQuietly(inputStream);
            }
            if (outputStream != null) {
                libcore.io.IoUtils.closeQuietly(outputStream);
            }
            throw th;
        }
    }

    private void copyKeysInLexicalOrder(android.app.backup.BackupDataInput in, android.app.backup.BackupDataOutput out) throws java.io.IOException {
        java.util.Map<java.lang.String, byte[]> data = new java.util.HashMap<>();
        while (in.readNextHeader()) {
            java.lang.String key = in.getKey();
            int size = in.getDataSize();
            if (size < 0) {
                in.skipEntityData();
            } else {
                byte[] value = new byte[size];
                in.readEntityData(value, 0, size);
                data.put(key, value);
            }
        }
        java.util.List<java.lang.String> keys = new java.util.ArrayList<>(data.keySet());
        java.util.Collections.sort(keys);
        for (java.lang.String key2 : keys) {
            byte[] value2 = data.get(key2);
            out.writeEntityHeader(key2, value2.length);
            out.writeEntityData(value2, value2.length);
        }
    }
}
