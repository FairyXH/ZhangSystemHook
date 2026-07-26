package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class LockSettingsStorage {
    private static final java.lang.String CHILD_PROFILE_LOCK_FILE = "gatekeeper.profile.key";
    private static final java.lang.String COLUMN_KEY = "name";
    private static final java.lang.String COLUMN_USERID = "user";
    private static final java.lang.String REBOOT_ESCROW_FILE = "reboot.escrow.key";
    private static final java.lang.String REBOOT_ESCROW_SERVER_BLOB_FILE = "reboot.escrow.server.blob.key";
    private static final java.lang.String REPAIR_MODE_DIRECTORY = "repair-mode/";
    private static final java.lang.String REPAIR_MODE_PERSISTENT_FILE = "pst";
    private static final java.lang.String SYNTHETIC_PASSWORD_DIRECTORY = "spblob/";
    private static final java.lang.String TABLE = "locksettings";
    private static final java.lang.String TAG = "LockSettingsStorage";
    private final android.content.Context mContext;
    private final com.android.server.locksettings.LockSettingsStorage.DatabaseHelper mOpenHelper;
    private com.android.server.pdb.PersistentDataBlockManagerInternal mPersistentDataBlockManagerInternal;
    private static final java.lang.String COLUMN_VALUE = "value";
    private static final java.lang.String[] COLUMNS_FOR_QUERY = {COLUMN_VALUE};
    private static final java.lang.String[] COLUMNS_FOR_PREFETCH = {"name", COLUMN_VALUE};
    private static final java.lang.Object DEFAULT = new java.lang.Object();
    private static final java.lang.String[] SETTINGS_TO_BACKUP = {"lock_screen_owner_info_enabled", "lock_screen_owner_info", "lock_pattern_visible_pattern", "lockscreen.power_button_instantly_locks"};
    private final com.android.server.locksettings.LockSettingsStorage.Cache mCache = new com.android.server.locksettings.LockSettingsStorage.Cache();
    private final java.lang.Object mFileWriteLock = new java.lang.Object();

    public interface Callback {
        void initialize(android.database.sqlite.SQLiteDatabase sQLiteDatabase);
    }

    public LockSettingsStorage(android.content.Context context) {
        this.mContext = context;
        this.mOpenHelper = new com.android.server.locksettings.LockSettingsStorage.DatabaseHelper(context);
    }

    public void setDatabaseOnCreateCallback(com.android.server.locksettings.LockSettingsStorage.Callback callback) {
        this.mOpenHelper.setCallback(callback);
    }

    public void writeKeyValue(java.lang.String key, java.lang.String value, int userId) {
        writeKeyValue(this.mOpenHelper.getWritableDatabase(), key, value, userId);
    }

    public boolean isAutoPinConfirmSettingEnabled(int userId) {
        return getBoolean("lockscreen.auto_pin_confirm", false, userId);
    }

    public void writeKeyValue(android.database.sqlite.SQLiteDatabase db, java.lang.String key, java.lang.String value, int userId) {
        android.content.ContentValues cv = new android.content.ContentValues();
        cv.put("name", key);
        cv.put(COLUMN_USERID, java.lang.Integer.valueOf(userId));
        cv.put(COLUMN_VALUE, value);
        db.beginTransaction();
        try {
            db.delete(TABLE, "name=? AND user=?", new java.lang.String[]{key, java.lang.Integer.toString(userId)});
            db.insert(TABLE, null, cv);
            db.setTransactionSuccessful();
            this.mCache.putKeyValue(key, value, userId);
        } finally {
            db.endTransaction();
        }
    }

    public java.lang.String readKeyValue(java.lang.String key, java.lang.String defaultValue, int userId) {
        synchronized (this.mCache) {
            if (this.mCache.hasKeyValue(key, userId)) {
                return this.mCache.peekKeyValue(key, defaultValue, userId);
            }
            int version = this.mCache.getVersion();
            java.lang.Object result = DEFAULT;
            android.database.sqlite.SQLiteDatabase db = this.mOpenHelper.getReadableDatabase();
            android.database.Cursor cursor = db.query(TABLE, COLUMNS_FOR_QUERY, "user=? AND name=?", new java.lang.String[]{java.lang.Integer.toString(userId), key}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    result = cursor.getString(0);
                }
                cursor.close();
            }
            this.mCache.putKeyValueIfUnchanged(key, result, userId, version);
            return result == DEFAULT ? defaultValue : (java.lang.String) result;
        }
    }

    boolean isKeyValueCached(java.lang.String key, int userId) {
        return this.mCache.hasKeyValue(key, userId);
    }

    boolean isUserPrefetched(int userId) {
        return this.mCache.isFetched(userId);
    }

    public void removeKey(java.lang.String key, int userId) {
        removeKey(this.mOpenHelper.getWritableDatabase(), key, userId);
    }

    private void removeKey(android.database.sqlite.SQLiteDatabase db, java.lang.String key, int userId) {
        android.content.ContentValues cv = new android.content.ContentValues();
        cv.put("name", key);
        cv.put(COLUMN_USERID, java.lang.Integer.valueOf(userId));
        db.beginTransaction();
        try {
            db.delete(TABLE, "name=? AND user=?", new java.lang.String[]{key, java.lang.Integer.toString(userId)});
            db.setTransactionSuccessful();
            this.mCache.removeKey(key, userId);
        } finally {
            db.endTransaction();
        }
    }

    public void prefetchUser(int userId) {
        synchronized (this.mCache) {
            if (this.mCache.isFetched(userId)) {
                return;
            }
            this.mCache.setFetched(userId);
            int version = this.mCache.getVersion();
            android.database.sqlite.SQLiteDatabase db = this.mOpenHelper.getReadableDatabase();
            android.database.Cursor cursor = db.query(TABLE, COLUMNS_FOR_PREFETCH, "user=?", new java.lang.String[]{java.lang.Integer.toString(userId)}, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    java.lang.String key = cursor.getString(0);
                    java.lang.String value = cursor.getString(1);
                    this.mCache.putKeyValueIfUnchanged(key, value, userId, version);
                }
                cursor.close();
            }
        }
    }

    public void removeChildProfileLock(int userId) {
        deleteFile(getChildProfileLockFile(userId));
    }

    public void writeChildProfileLock(int userId, byte[] lock) {
        writeFile(getChildProfileLockFile(userId), lock);
    }

    public byte[] readChildProfileLock(int userId) {
        return readFile(getChildProfileLockFile(userId));
    }

    public boolean hasChildProfileLock(int userId) {
        return hasFile(getChildProfileLockFile(userId));
    }

    public void writeRebootEscrow(int userId, byte[] rebootEscrow) {
        writeFile(getRebootEscrowFile(userId), rebootEscrow);
    }

    public byte[] readRebootEscrow(int userId) {
        return readFile(getRebootEscrowFile(userId));
    }

    public boolean hasRebootEscrow(int userId) {
        return hasFile(getRebootEscrowFile(userId));
    }

    public void removeRebootEscrow(int userId) {
        deleteFile(getRebootEscrowFile(userId));
    }

    public void writeRebootEscrowServerBlob(byte[] serverBlob) {
        writeFile(getRebootEscrowServerBlobFile(), serverBlob);
    }

    public byte[] readRebootEscrowServerBlob() {
        return readFile(getRebootEscrowServerBlobFile());
    }

    public boolean hasRebootEscrowServerBlob() {
        return hasFile(getRebootEscrowServerBlobFile());
    }

    public void removeRebootEscrowServerBlob() {
        deleteFile(getRebootEscrowServerBlobFile());
    }

    private boolean hasFile(java.io.File path) {
        byte[] contents = readFile(path);
        return contents != null && contents.length > 0;
    }

    private byte[] readFile(java.io.File path) {
        synchronized (this.mCache) {
            if (this.mCache.hasFile(path)) {
                return this.mCache.peekFile(path);
            }
            int version = this.mCache.getVersion();
            byte[] data = null;
            try {
                java.io.RandomAccessFile raf = new java.io.RandomAccessFile(path, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
                try {
                    data = new byte[(int) raf.length()];
                    raf.readFully(data, 0, data.length);
                    raf.close();
                    raf.close();
                } finally {
                }
            } catch (java.io.FileNotFoundException e) {
                android.util.Slog.e(TAG, "File not found " + e);
            } catch (java.io.IOException e2) {
                android.util.Slog.e(TAG, "Cannot read file " + e2);
            }
            this.mCache.putFileIfUnchanged(path, data, version);
            return data;
        }
    }

    private void fsyncDirectory(java.io.File directory) {
        try {
            java.nio.channels.FileChannel file = java.nio.channels.FileChannel.open(directory.toPath(), java.nio.file.StandardOpenOption.READ);
            try {
                file.force(true);
                if (file != null) {
                    file.close();
                }
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Error syncing directory: " + directory, e);
        }
    }

    private void writeFile(java.io.File path, byte[] data) {
        writeFile(path, data, true);
    }

    private void writeFile(java.io.File path, byte[] data, boolean syncParentDir) {
        synchronized (this.mFileWriteLock) {
            android.util.AtomicFile file = new android.util.AtomicFile(path);
            java.io.FileOutputStream out = null;
            try {
                try {
                    out = file.startWrite();
                    out.write(data);
                    file.finishWrite(out);
                    out = null;
                } catch (java.io.IOException e) {
                    android.util.Slog.e(TAG, "Error writing file " + path, e);
                }
                if (syncParentDir) {
                    fsyncDirectory(path.getParentFile());
                }
                this.mCache.putFile(path, data);
            } finally {
                file.failWrite(out);
            }
        }
    }

    private void deleteFile(java.io.File path) {
        synchronized (this.mFileWriteLock) {
            if (path.exists()) {
                try {
                    java.io.RandomAccessFile raf = new java.io.RandomAccessFile(path, "rws");
                    try {
                        int fileSize = (int) raf.length();
                        raf.write(new byte[fileSize]);
                        raf.close();
                    } catch (java.lang.Throwable th) {
                        try {
                            raf.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.w(TAG, "Failed to zeroize " + path, e);
                }
            }
            new android.util.AtomicFile(path).delete();
            this.mCache.putFile(path, null);
        }
    }

    java.io.File getChildProfileLockFile(int userId) {
        return getLockCredentialFileForUser(userId, CHILD_PROFILE_LOCK_FILE);
    }

    java.io.File getRebootEscrowFile(int userId) {
        return getLockCredentialFileForUser(userId, REBOOT_ESCROW_FILE);
    }

    java.io.File getRebootEscrowServerBlobFile() {
        return getLockCredentialFileForUser(0, REBOOT_ESCROW_SERVER_BLOB_FILE);
    }

    private java.io.File getLockCredentialFileForUser(int userId, java.lang.String fileName) {
        if (userId == 0) {
            return new java.io.File(android.os.Environment.getDataSystemDirectory(), fileName);
        }
        return new java.io.File(android.os.Environment.getUserSystemDirectory(userId), fileName);
    }

    java.io.File getRepairModePersistentDataFile() {
        java.io.File directory = new java.io.File(android.os.Environment.getMetadataDirectory(), REPAIR_MODE_DIRECTORY);
        return new java.io.File(directory, REPAIR_MODE_PERSISTENT_FILE);
    }

    public com.android.server.locksettings.LockSettingsStorage.PersistentData readRepairModePersistentData() {
        byte[] data = readFile(getRepairModePersistentDataFile());
        if (data == null) {
            return com.android.server.locksettings.LockSettingsStorage.PersistentData.NONE;
        }
        return com.android.server.locksettings.LockSettingsStorage.PersistentData.fromBytes(data);
    }

    public void writeRepairModePersistentData(int persistentType, int userId, byte[] payload) {
        writeFile(getRepairModePersistentDataFile(), com.android.server.locksettings.LockSettingsStorage.PersistentData.toBytes(persistentType, userId, 0, payload));
    }

    public void deleteRepairModePersistentData() {
        deleteFile(getRepairModePersistentDataFile());
    }

    public void writeSyntheticPasswordState(int userId, long protectorId, java.lang.String name, byte[] data) {
        android.util.Slog.d(TAG, "[writeSyntheticPasswordState] userId = " + userId + ", name = " + name + ", data = " + java.util.Arrays.toString(data));
        ensureSyntheticPasswordDirectoryForUser(userId);
        writeFile(getSyntheticPasswordStateFileForUser(userId, protectorId, name), data, false);
    }

    public byte[] readSyntheticPasswordState(int userId, long protectorId, java.lang.String name) {
        android.util.Slog.d(TAG, "[readSyntheticPasswordState] userId = " + userId + ", name = " + name);
        return readFile(getSyntheticPasswordStateFileForUser(userId, protectorId, name));
    }

    public void deleteSyntheticPasswordState(int userId, long protectorId, java.lang.String name) {
        android.util.Slog.d(TAG, "[deleteSyntheticPasswordState] userId = " + userId + ", name = " + name);
        deleteFile(getSyntheticPasswordStateFileForUser(userId, protectorId, name));
    }

    public void syncSyntheticPasswordState(int userId) {
        fsyncDirectory(getSyntheticPasswordDirectoryForUser(userId));
    }

    public java.util.Map<java.lang.Integer, java.util.List<java.lang.Long>> listSyntheticPasswordProtectorsForAllUsers(java.lang.String stateName) {
        java.util.Map<java.lang.Integer, java.util.List<java.lang.Long>> result = new android.util.ArrayMap<>();
        android.os.UserManager um = android.os.UserManager.get(this.mContext);
        for (android.content.pm.UserInfo user : um.getUsers()) {
            result.put(java.lang.Integer.valueOf(user.id), listSyntheticPasswordProtectorsForUser(stateName, user.id));
        }
        return result;
    }

    public java.util.List<java.lang.Long> listSyntheticPasswordProtectorsForUser(java.lang.String stateName, int userId) {
        java.io.File baseDir = getSyntheticPasswordDirectoryForUser(userId);
        java.util.List<java.lang.Long> result = new java.util.ArrayList<>();
        java.io.File[] files = baseDir.listFiles();
        if (files == null) {
            return result;
        }
        for (java.io.File file : files) {
            java.lang.String[] parts = file.getName().split("\\.");
            if (parts.length == 2 && parts[1].equals(stateName)) {
                try {
                    android.util.Slog.d(TAG, "[listSyntheticPasswordHandlesForUser] userId = " + userId + ", stateName = " + stateName + ", filename = " + file.getName());
                    result.add(java.lang.Long.valueOf(java.lang.Long.parseUnsignedLong(parts[0], 16)));
                } catch (java.lang.NumberFormatException e) {
                    android.util.Slog.e(TAG, "Failed to parse protector ID " + parts[0]);
                }
            }
        }
        return result;
    }

    protected java.io.File getSyntheticPasswordDirectoryForUser(int userId) {
        return new java.io.File(android.os.Environment.getDataSystemDeDirectory(userId), SYNTHETIC_PASSWORD_DIRECTORY);
    }

    private void ensureSyntheticPasswordDirectoryForUser(int userId) {
        java.io.File baseDir = getSyntheticPasswordDirectoryForUser(userId);
        if (!baseDir.exists()) {
            baseDir.mkdir();
        }
    }

    private java.io.File getSyntheticPasswordStateFileForUser(int userId, long protectorId, java.lang.String name) {
        java.lang.String fileName = android.text.TextUtils.formatSimple("%016x.%s", new java.lang.Object[]{java.lang.Long.valueOf(protectorId), name});
        return new java.io.File(getSyntheticPasswordDirectoryForUser(userId), fileName);
    }

    public void removeUser(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        android.os.UserManager um = (android.os.UserManager) this.mContext.getSystemService(COLUMN_USERID);
        android.content.pm.UserInfo parentInfo = um.getProfileParent(userId);
        if (parentInfo == null) {
            deleteFile(getRebootEscrowFile(userId));
        } else {
            removeChildProfileLock(userId);
        }
        java.io.File spStateDir = getSyntheticPasswordDirectoryForUser(userId);
        try {
            db.beginTransaction();
            db.delete(TABLE, "user='" + userId + "'", null);
            db.setTransactionSuccessful();
            this.mCache.removeUser(userId);
            this.mCache.purgePath(spStateDir);
            android.util.Slog.d(TAG, "[removeUser] userId = " + userId + ", spStateDir = " + spStateDir.getAbsolutePath());
        } finally {
            db.endTransaction();
        }
    }

    public void setBoolean(java.lang.String key, boolean value, int userId) {
        setString(key, value ? "1" : "0", userId);
    }

    public void setLong(java.lang.String key, long value, int userId) {
        setString(key, java.lang.Long.toString(value), userId);
    }

    public void setInt(java.lang.String key, int value, int userId) {
        setString(key, java.lang.Integer.toString(value), userId);
    }

    public void setString(java.lang.String key, java.lang.String value, int userId) {
        com.android.internal.util.Preconditions.checkArgument(!com.android.internal.widget.LockPatternUtils.isSpecialUserId(userId), "cannot store lock settings for special user: %d", new java.lang.Object[]{java.lang.Integer.valueOf(userId)});
        writeKeyValue(key, value, userId);
        if (com.android.internal.util.ArrayUtils.contains(SETTINGS_TO_BACKUP, key)) {
            android.app.backup.BackupManager.dataChanged(com.android.server.backup.UserBackupManagerService.SETTINGS_PACKAGE);
        }
    }

    public boolean getBoolean(java.lang.String key, boolean defaultValue, int userId) {
        java.lang.String value = getString(key, null, userId);
        if (android.text.TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value.equals("1") || value.equals("true");
    }

    public long getLong(java.lang.String key, long defaultValue, int userId) {
        java.lang.String value = getString(key, null, userId);
        return android.text.TextUtils.isEmpty(value) ? defaultValue : java.lang.Long.parseLong(value);
    }

    public int getInt(java.lang.String key, int defaultValue, int userId) {
        java.lang.String value = getString(key, null, userId);
        return android.text.TextUtils.isEmpty(value) ? defaultValue : java.lang.Integer.parseInt(value);
    }

    public java.lang.String getString(java.lang.String key, java.lang.String defaultValue, int userId) {
        if (com.android.internal.widget.LockPatternUtils.isSpecialUserId(userId)) {
            return null;
        }
        return readKeyValue(key, defaultValue, userId);
    }

    void closeDatabase() {
        this.mOpenHelper.close();
    }

    void clearCache() {
        this.mCache.clear();
    }

    com.android.server.pdb.PersistentDataBlockManagerInternal getPersistentDataBlockManager() {
        if (this.mPersistentDataBlockManagerInternal == null) {
            this.mPersistentDataBlockManagerInternal = (com.android.server.pdb.PersistentDataBlockManagerInternal) com.android.server.LocalServices.getService(com.android.server.pdb.PersistentDataBlockManagerInternal.class);
        }
        return this.mPersistentDataBlockManagerInternal;
    }

    public void writePersistentDataBlock(int persistentType, int userId, int qualityForUi, byte[] payload) {
        com.android.server.pdb.PersistentDataBlockManagerInternal persistentDataBlock = getPersistentDataBlockManager();
        if (persistentDataBlock == null) {
            return;
        }
        persistentDataBlock.setFrpCredentialHandle(com.android.server.locksettings.LockSettingsStorage.PersistentData.toBytes(persistentType, userId, qualityForUi, payload));
    }

    public com.android.server.locksettings.LockSettingsStorage.PersistentData readPersistentDataBlock() {
        com.android.server.pdb.PersistentDataBlockManagerInternal persistentDataBlock = getPersistentDataBlockManager();
        if (persistentDataBlock == null) {
            return com.android.server.locksettings.LockSettingsStorage.PersistentData.NONE;
        }
        try {
            return com.android.server.locksettings.LockSettingsStorage.PersistentData.fromBytes(persistentDataBlock.getFrpCredentialHandle());
        } catch (java.lang.IllegalStateException e) {
            android.util.Slog.e(TAG, "Error reading persistent data block", e);
            return com.android.server.locksettings.LockSettingsStorage.PersistentData.NONE;
        }
    }

    public void deactivateFactoryResetProtectionWithoutSecret() {
        com.android.server.pdb.PersistentDataBlockManagerInternal persistentDataBlock = getPersistentDataBlockManager();
        if (persistentDataBlock != null) {
            persistentDataBlock.deactivateFactoryResetProtectionWithoutSecret();
        } else {
            android.util.Slog.wtf(TAG, "Failed to get PersistentDataBlockManagerInternal");
        }
    }

    public boolean isFactoryResetProtectionActive() {
        android.service.persistentdata.PersistentDataBlockManager persistentDataBlockManager = (android.service.persistentdata.PersistentDataBlockManager) this.mContext.getSystemService(android.service.persistentdata.PersistentDataBlockManager.class);
        if (persistentDataBlockManager != null) {
            return persistentDataBlockManager.isFactoryResetProtectionActive();
        }
        android.util.Slog.wtf(TAG, "Failed to get PersistentDataBlockManager");
        return false;
    }

    public static class PersistentData {
        public static final com.android.server.locksettings.LockSettingsStorage.PersistentData NONE = new com.android.server.locksettings.LockSettingsStorage.PersistentData(0, -10000, 0, null);
        public static final int TYPE_NONE = 0;
        public static final int TYPE_SP_GATEKEEPER = 1;
        public static final int TYPE_SP_WEAVER = 2;
        static final byte VERSION_1 = 1;
        static final int VERSION_1_HEADER_SIZE = 10;
        final byte[] payload;
        final int qualityForUi;
        final int type;
        final int userId;

        private PersistentData(int type, int userId, int qualityForUi, byte[] payload) {
            this.type = type;
            this.userId = userId;
            this.qualityForUi = qualityForUi;
            this.payload = payload;
        }

        public boolean isBadFormatFromAndroid14Beta() {
            return (this.type == 1 || this.type == 2) && com.android.server.locksettings.SyntheticPasswordManager.PasswordData.isBadFormatFromAndroid14Beta(this.payload);
        }

        public static com.android.server.locksettings.LockSettingsStorage.PersistentData fromBytes(byte[] frpData) {
            if (frpData == null || frpData.length == 0) {
                return NONE;
            }
            java.io.DataInputStream is = new java.io.DataInputStream(new java.io.ByteArrayInputStream(frpData));
            try {
                byte version = is.readByte();
                if (version != 1) {
                    android.util.Slog.wtf(com.android.server.locksettings.LockSettingsStorage.TAG, "Unknown PersistentData version code: " + ((int) version));
                    return NONE;
                }
                int type = is.readByte() & 255;
                int userId = is.readInt();
                int qualityForUi = is.readInt();
                byte[] payload = new byte[frpData.length - 10];
                java.lang.System.arraycopy(frpData, 10, payload, 0, payload.length);
                return new com.android.server.locksettings.LockSettingsStorage.PersistentData(type, userId, qualityForUi, payload);
            } catch (java.io.IOException e) {
                android.util.Slog.wtf(com.android.server.locksettings.LockSettingsStorage.TAG, "Could not parse PersistentData", e);
                return NONE;
            }
        }

        public static byte[] toBytes(int persistentType, int userId, int qualityForUi, byte[] payload) {
            if (persistentType == 0) {
                com.android.internal.util.Preconditions.checkArgument(payload == null, "TYPE_NONE must have empty payload");
                return null;
            }
            if (payload != null && payload.length > 0) {
                z = true;
            }
            com.android.internal.util.Preconditions.checkArgument(z, "empty payload must only be used with TYPE_NONE");
            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream(payload.length + 10);
            java.io.DataOutputStream dos = new java.io.DataOutputStream(os);
            try {
                dos.writeByte(1);
                dos.writeByte(persistentType);
                dos.writeInt(userId);
                dos.writeInt(qualityForUi);
                dos.write(payload);
                return os.toByteArray();
            } catch (java.io.IOException e) {
                throw new java.lang.IllegalStateException("ByteArrayOutputStream cannot throw IOException");
            }
        }
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        android.os.UserManager um = android.os.UserManager.get(this.mContext);
        java.util.Iterator it = um.getUsers().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.content.pm.UserInfo user = (android.content.pm.UserInfo) it.next();
            java.io.File userPath = getSyntheticPasswordDirectoryForUser(user.id);
            pw.println(android.text.TextUtils.formatSimple("User %d [%s]:", new java.lang.Object[]{java.lang.Integer.valueOf(user.id), userPath}));
            pw.increaseIndent();
            java.io.File[] files = userPath.listFiles();
            if (files != null) {
                java.util.Arrays.sort(files);
                int length = files.length;
                while (i < length) {
                    java.io.File file = files[i];
                    pw.println(android.text.TextUtils.formatSimple("%6d %s %s", new java.lang.Object[]{java.lang.Long.valueOf(file.length()), com.android.server.locksettings.LockSettingsService.timestampToString(file.lastModified()), file.getName()}));
                    i++;
                }
            } else {
                pw.println("[Not found]");
            }
            pw.decreaseIndent();
        }
        java.io.File repairModeFile = getRepairModePersistentDataFile();
        if (repairModeFile.exists()) {
            pw.println(android.text.TextUtils.formatSimple("Repair Mode [%s]:", new java.lang.Object[]{repairModeFile.getParent()}));
            pw.increaseIndent();
            pw.println(android.text.TextUtils.formatSimple("%6d %s %s", new java.lang.Object[]{java.lang.Long.valueOf(repairModeFile.length()), com.android.server.locksettings.LockSettingsService.timestampToString(repairModeFile.lastModified()), repairModeFile.getName()}));
            com.android.server.locksettings.LockSettingsStorage.PersistentData data = readRepairModePersistentData();
            pw.println(android.text.TextUtils.formatSimple("type: %d, user id: %d, payload size: %d", new java.lang.Object[]{java.lang.Integer.valueOf(data.type), java.lang.Integer.valueOf(data.userId), java.lang.Integer.valueOf(data.payload != null ? data.payload.length : 0)}));
            pw.decreaseIndent();
        }
    }

    static class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
        private static final java.lang.String DATABASE_NAME = "locksettings.db";
        private static final int DATABASE_VERSION = 2;
        private static final int IDLE_CONNECTION_TIMEOUT_MS = 30000;
        private static final java.lang.String TAG = "LockSettingsDB";
        private com.android.server.locksettings.LockSettingsStorage.Callback mCallback;

        public DatabaseHelper(android.content.Context context) {
            super(context, DATABASE_NAME, (android.database.sqlite.SQLiteDatabase.CursorFactory) null, 2);
            setWriteAheadLoggingEnabled(false);
            setIdleConnectionTimeout(30000L);
        }

        public void setCallback(com.android.server.locksettings.LockSettingsStorage.Callback callback) {
            this.mCallback = callback;
        }

        private void createTable(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("CREATE TABLE locksettings (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,user INTEGER,value TEXT);");
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            createTable(db);
            if (this.mCallback != null) {
                this.mCallback.initialize(db);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int currentVersion) {
            int upgradeVersion = oldVersion;
            if (upgradeVersion == 1) {
                upgradeVersion = 2;
            }
            if (upgradeVersion != 2) {
                android.util.Slog.w(TAG, "Failed to upgrade database!");
            }
        }
    }

    private static class Cache {
        private final android.util.ArrayMap<com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey, java.lang.Object> mCache;
        private final com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey mCacheKey;
        private int mVersion;

        private Cache() {
            this.mCache = new android.util.ArrayMap<>();
            this.mCacheKey = new com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey();
            this.mVersion = 0;
        }

        java.lang.String peekKeyValue(java.lang.String key, java.lang.String defaultValue, int userId) {
            java.lang.Object cached = peek(0, key, userId);
            return cached == com.android.server.locksettings.LockSettingsStorage.DEFAULT ? defaultValue : (java.lang.String) cached;
        }

        boolean hasKeyValue(java.lang.String key, int userId) {
            return contains(0, key, userId);
        }

        void putKeyValue(java.lang.String key, java.lang.String value, int userId) {
            put(0, key, value, userId);
        }

        void putKeyValueIfUnchanged(java.lang.String key, java.lang.Object value, int userId, int version) {
            putIfUnchanged(0, key, value, userId, version);
        }

        void removeKey(java.lang.String key, int userId) {
            remove(0, key, userId);
        }

        byte[] peekFile(java.io.File path) {
            return copyOf((byte[]) peek(1, path.toString(), -1));
        }

        boolean hasFile(java.io.File path) {
            return contains(1, path.toString(), -1);
        }

        void putFile(java.io.File path, byte[] data) {
            put(1, path.toString(), copyOf(data), -1);
        }

        void putFileIfUnchanged(java.io.File path, byte[] data, int version) {
            putIfUnchanged(1, path.toString(), copyOf(data), -1, version);
        }

        void setFetched(int userId) {
            put(2, "", "true", userId);
        }

        boolean isFetched(int userId) {
            return contains(2, "", userId);
        }

        private synchronized void remove(int type, java.lang.String key, int userId) {
            this.mCache.remove(this.mCacheKey.set(type, key, userId));
            this.mVersion++;
        }

        private synchronized void put(int type, java.lang.String key, java.lang.Object value, int userId) {
            this.mCache.put(new com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey().set(type, key, userId), value);
            this.mVersion++;
        }

        private synchronized void putIfUnchanged(int type, java.lang.String key, java.lang.Object value, int userId, int version) {
            if (!contains(type, key, userId) && this.mVersion == version) {
                this.mCache.put(new com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey().set(type, key, userId), value);
            }
        }

        private synchronized boolean contains(int type, java.lang.String key, int userId) {
            return this.mCache.containsKey(this.mCacheKey.set(type, key, userId));
        }

        private synchronized java.lang.Object peek(int type, java.lang.String key, int userId) {
            return this.mCache.get(this.mCacheKey.set(type, key, userId));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized int getVersion() {
            return this.mVersion;
        }

        synchronized void removeUser(int userId) {
            for (int i = this.mCache.size() - 1; i >= 0; i--) {
                if (this.mCache.keyAt(i).userId == userId) {
                    this.mCache.removeAt(i);
                }
            }
            int i2 = this.mVersion;
            this.mVersion = i2 + 1;
        }

        private byte[] copyOf(byte[] data) {
            if (data != null) {
                return java.util.Arrays.copyOf(data, data.length);
            }
            return null;
        }

        synchronized void purgePath(java.io.File path) {
            java.lang.String pathStr = path.toString();
            for (int i = this.mCache.size() - 1; i >= 0; i--) {
                com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey entry = this.mCache.keyAt(i);
                if (entry.type == 1 && entry.key.startsWith(pathStr)) {
                    this.mCache.removeAt(i);
                }
            }
            int i2 = this.mVersion;
            this.mVersion = i2 + 1;
        }

        synchronized void clear() {
            this.mCache.clear();
            this.mVersion++;
        }

        private static final class CacheKey {
            static final int TYPE_FETCHED = 2;
            static final int TYPE_FILE = 1;
            static final int TYPE_KEY_VALUE = 0;
            java.lang.String key;
            int type;
            int userId;

            private CacheKey() {
            }

            public com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey set(int type, java.lang.String key, int userId) {
                this.type = type;
                this.key = key;
                this.userId = userId;
                return this;
            }

            public boolean equals(java.lang.Object obj) {
                if (!(obj instanceof com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey)) {
                    return false;
                }
                com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey o = (com.android.server.locksettings.LockSettingsStorage.Cache.CacheKey) obj;
                return this.userId == o.userId && this.type == o.type && java.util.Objects.equals(this.key, o.key);
            }

            public int hashCode() {
                int hashCode = java.util.Objects.hashCode(this.key);
                return (((hashCode * 31) + this.userId) * 31) + this.type;
            }
        }
    }
}
