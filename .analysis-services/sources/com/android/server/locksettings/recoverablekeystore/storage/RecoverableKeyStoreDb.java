package com.android.server.locksettings.recoverablekeystore.storage;

/* JADX INFO: loaded from: classes2.dex */
public class RecoverableKeyStoreDb {
    private static final java.lang.String CERT_PATH_ENCODING = "PkiPath";
    private static final int IDLE_TIMEOUT_SECONDS = 30;
    private static final int LAST_SYNCED_AT_UNSYNCED = -1;
    private static final java.lang.String TAG = "RecoverableKeyStoreDb";
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDbHelper mKeyStoreDbHelper;
    private final com.android.server.locksettings.recoverablekeystore.TestOnlyInsecureCertificateHelper mTestOnlyInsecureCertificateHelper = new com.android.server.locksettings.recoverablekeystore.TestOnlyInsecureCertificateHelper();

    public static com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb newInstance(android.content.Context context) {
        com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDbHelper helper = new com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDbHelper(context);
        helper.setWriteAheadLoggingEnabled(true);
        helper.setIdleConnectionTimeout(30L);
        return new com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb(helper);
    }

    private RecoverableKeyStoreDb(com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDbHelper keyStoreDbHelper) {
        this.mKeyStoreDbHelper = keyStoreDbHelper;
    }

    public long insertKey(int userId, int uid, java.lang.String alias, com.android.server.locksettings.recoverablekeystore.WrappedKey wrappedKey) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("user_id", java.lang.Integer.valueOf(userId));
        values.put("uid", java.lang.Integer.valueOf(uid));
        values.put("alias", alias);
        values.put("nonce", wrappedKey.getNonce());
        values.put("wrapped_key", wrappedKey.getKeyMaterial());
        values.put("last_synced_at", (java.lang.Integer) (-1));
        values.put("platform_key_generation_id", java.lang.Integer.valueOf(wrappedKey.getPlatformKeyGenerationId()));
        values.put("recovery_status", java.lang.Integer.valueOf(wrappedKey.getRecoveryStatus()));
        byte[] keyMetadata = wrappedKey.getKeyMetadata();
        if (keyMetadata == null) {
            values.putNull("key_metadata");
        } else {
            values.put("key_metadata", keyMetadata);
        }
        return db.replace("keys", null, values);
    }

    public com.android.server.locksettings.recoverablekeystore.WrappedKey getKey(int uid, java.lang.String alias) {
        byte[] keyMetadata;
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"_id", "nonce", "wrapped_key", "platform_key_generation_id", "recovery_status", "key_metadata"};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(uid), alias};
        android.database.Cursor cursor = db.query("keys", projection, "uid = ? AND alias = ?", selectionArguments, null, null, null);
        try {
            int count = cursor.getCount();
            if (count != 0) {
                if (count > 1) {
                    android.util.Log.wtf(TAG, java.lang.String.format(java.util.Locale.US, "%d WrappedKey entries found for uid=%d alias='%s'. Should only ever be 0 or 1.", java.lang.Integer.valueOf(count), java.lang.Integer.valueOf(uid), alias));
                    if (cursor != null) {
                        cursor.close();
                    }
                    return null;
                }
                cursor.moveToFirst();
                byte[] nonce = cursor.getBlob(cursor.getColumnIndexOrThrow("nonce"));
                byte[] keyMaterial = cursor.getBlob(cursor.getColumnIndexOrThrow("wrapped_key"));
                int generationId = cursor.getInt(cursor.getColumnIndexOrThrow("platform_key_generation_id"));
                int recoveryStatus = cursor.getInt(cursor.getColumnIndexOrThrow("recovery_status"));
                int metadataIdx = cursor.getColumnIndexOrThrow("key_metadata");
                if (cursor.isNull(metadataIdx)) {
                    keyMetadata = null;
                } else {
                    keyMetadata = cursor.getBlob(metadataIdx);
                }
                com.android.server.locksettings.recoverablekeystore.WrappedKey wrappedKey = new com.android.server.locksettings.recoverablekeystore.WrappedKey(nonce, keyMaterial, keyMetadata, generationId, recoveryStatus);
                if (cursor != null) {
                    cursor.close();
                }
                return wrappedKey;
            }
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (java.lang.Throwable th) {
            if (cursor == null) {
                throw th;
            }
            try {
                cursor.close();
                throw th;
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    public boolean removeKey(int uid, java.lang.String alias) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        java.lang.String[] selectionArgs = {java.lang.Integer.toString(uid), alias};
        return db.delete("keys", "uid = ? AND alias = ?", selectionArgs) > 0;
    }

    public java.util.Map<java.lang.String, java.lang.Integer> getStatusForAllKeys(int uid) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"_id", "alias", "recovery_status"};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(uid)};
        android.database.Cursor cursor = db.query("keys", projection, "uid = ?", selectionArguments, null, null, null);
        try {
            java.util.HashMap<java.lang.String, java.lang.Integer> statuses = new java.util.HashMap<>();
            while (cursor.moveToNext()) {
                java.lang.String alias = cursor.getString(cursor.getColumnIndexOrThrow("alias"));
                int recoveryStatus = cursor.getInt(cursor.getColumnIndexOrThrow("recovery_status"));
                statuses.put(alias, java.lang.Integer.valueOf(recoveryStatus));
            }
            if (cursor != null) {
                cursor.close();
            }
            return statuses;
        } catch (java.lang.Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public int setRecoveryStatus(int uid, java.lang.String alias, int status) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("recovery_status", java.lang.Integer.valueOf(status));
        return db.update("keys", values, "uid = ? AND alias = ?", new java.lang.String[]{java.lang.String.valueOf(uid), alias});
    }

    public java.util.Map<java.lang.String, com.android.server.locksettings.recoverablekeystore.WrappedKey> getAllKeys(int userId, int recoveryAgentUid, int platformKeyGenerationId) {
        byte[] keyMetadata;
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"_id", "nonce", "wrapped_key", "alias", "recovery_status", "key_metadata"};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(recoveryAgentUid), java.lang.Integer.toString(platformKeyGenerationId)};
        android.database.Cursor cursor = db.query("keys", projection, "user_id = ? AND uid = ? AND platform_key_generation_id = ?", selectionArguments, null, null, null);
        try {
            java.util.HashMap<java.lang.String, com.android.server.locksettings.recoverablekeystore.WrappedKey> keys = new java.util.HashMap<>();
            while (cursor.moveToNext()) {
                byte[] nonce = cursor.getBlob(cursor.getColumnIndexOrThrow("nonce"));
                byte[] keyMaterial = cursor.getBlob(cursor.getColumnIndexOrThrow("wrapped_key"));
                java.lang.String alias = cursor.getString(cursor.getColumnIndexOrThrow("alias"));
                int recoveryStatus = cursor.getInt(cursor.getColumnIndexOrThrow("recovery_status"));
                int metadataIdx = cursor.getColumnIndexOrThrow("key_metadata");
                if (cursor.isNull(metadataIdx)) {
                    keyMetadata = null;
                } else {
                    keyMetadata = cursor.getBlob(metadataIdx);
                }
                keys.put(alias, new com.android.server.locksettings.recoverablekeystore.WrappedKey(nonce, keyMaterial, keyMetadata, platformKeyGenerationId, recoveryStatus));
            }
            if (cursor != null) {
                cursor.close();
            }
            return keys;
        } catch (java.lang.Throwable th) {
            if (cursor == null) {
                throw th;
            }
            try {
                cursor.close();
                throw th;
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    public long setPlatformKeyGenerationId(int userId, int generationId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("user_id", java.lang.Integer.valueOf(userId));
        values.put("platform_key_generation_id", java.lang.Integer.valueOf(generationId));
        java.lang.String[] selectionArguments = {java.lang.String.valueOf(userId)};
        ensureUserMetadataEntryExists(userId);
        invalidateKeysForUser(userId);
        return db.update("user_metadata", values, "user_id = ?", selectionArguments);
    }

    public java.util.Map<java.lang.Integer, java.lang.Long> getUserSerialNumbers() {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"user_id", "user_serial_number"};
        java.lang.String[] selectionArguments = new java.lang.String[0];
        android.database.Cursor cursor = db.query("user_metadata", projection, null, selectionArguments, null, null, null);
        try {
            java.util.Map<java.lang.Integer, java.lang.Long> serialNumbers = new android.util.ArrayMap<>();
            while (cursor.moveToNext()) {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                long serialNumber = cursor.getLong(cursor.getColumnIndexOrThrow("user_serial_number"));
                serialNumbers.put(java.lang.Integer.valueOf(userId), java.lang.Long.valueOf(serialNumber));
            }
            if (cursor != null) {
                cursor.close();
            }
            return serialNumbers;
        } catch (java.lang.Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public long setUserSerialNumber(int userId, long serialNumber) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("user_id", java.lang.Integer.valueOf(userId));
        values.put("user_serial_number", java.lang.Long.valueOf(serialNumber));
        java.lang.String[] selectionArguments = {java.lang.String.valueOf(userId)};
        ensureUserMetadataEntryExists(userId);
        return db.update("user_metadata", values, "user_id = ?", selectionArguments);
    }

    public long setBadRemoteGuessCounter(int userId, int badGuessCounter) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("user_id", java.lang.Integer.valueOf(userId));
        values.put("bad_remote_guess_counter", java.lang.Integer.valueOf(badGuessCounter));
        java.lang.String[] selectionArguments = {java.lang.String.valueOf(userId)};
        ensureUserMetadataEntryExists(userId);
        return db.update("user_metadata", values, "user_id = ?", selectionArguments);
    }

    public int getBadRemoteGuessCounter(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"bad_remote_guess_counter"};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId)};
        android.database.Cursor cursor = db.query("user_metadata", projection, "user_id = ?", selectionArguments, null, null, null);
        try {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                int i = cursor.getInt(cursor.getColumnIndexOrThrow("bad_remote_guess_counter"));
                if (cursor != null) {
                    cursor.close();
                }
                return i;
            }
            if (cursor != null) {
                cursor.close();
                return 0;
            }
            return 0;
        } catch (java.lang.Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public void invalidateKeysForUser(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("recovery_status", (java.lang.Integer) 3);
        db.update("keys", values, "user_id = ?", new java.lang.String[]{java.lang.String.valueOf(userId)});
    }

    public void invalidateKeysForUserIdOnCustomScreenLock(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("recovery_status", (java.lang.Integer) 3);
        db.update("keys", values, "user_id = ?", new java.lang.String[]{java.lang.String.valueOf(userId)});
    }

    public int getPlatformKeyGenerationId(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"platform_key_generation_id"};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId)};
        android.database.Cursor cursor = db.query("user_metadata", projection, "user_id = ?", selectionArguments, null, null, null);
        try {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                int i = cursor.getInt(cursor.getColumnIndexOrThrow("platform_key_generation_id"));
                if (cursor != null) {
                    cursor.close();
                }
                return i;
            }
            if (cursor != null) {
                cursor.close();
                return -1;
            }
            return -1;
        } catch (java.lang.Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public long setRecoveryServicePublicKey(int userId, int uid, java.security.PublicKey publicKey) {
        return setBytes(userId, uid, "public_key", publicKey.getEncoded());
    }

    public java.lang.Long getRecoveryServiceCertSerial(int userId, int uid, java.lang.String rootAlias) {
        return getLong(userId, uid, rootAlias, "cert_serial");
    }

    public long setRecoveryServiceCertSerial(int userId, int uid, java.lang.String rootAlias, long serial) {
        return setLong(userId, uid, rootAlias, "cert_serial", serial);
    }

    public java.security.cert.CertPath getRecoveryServiceCertPath(int userId, int uid, java.lang.String rootAlias) {
        byte[] bytes = getBytes(userId, uid, rootAlias, "cert_path");
        if (bytes == null) {
            return null;
        }
        try {
            return decodeCertPath(bytes);
        } catch (java.security.cert.CertificateException e) {
            android.util.Log.wtf(TAG, java.lang.String.format(java.util.Locale.US, "Recovery service CertPath entry cannot be decoded for userId=%d uid=%d.", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid)), e);
            return null;
        }
    }

    public long setRecoveryServiceCertPath(int userId, int uid, java.lang.String rootAlias, java.security.cert.CertPath certPath) throws java.security.cert.CertificateEncodingException {
        if (certPath.getCertificates().size() == 0) {
            throw new java.security.cert.CertificateEncodingException("No certificate contained in the cert path.");
        }
        return setBytes(userId, uid, rootAlias, "cert_path", certPath.getEncoded(CERT_PATH_ENCODING));
    }

    public java.util.List<java.lang.Integer> getRecoveryAgents(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"uid"};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId)};
        android.database.Cursor cursor = db.query("recovery_service_metadata", projection, "user_id = ?", selectionArguments, null, null, null);
        try {
            int count = cursor.getCount();
            java.util.ArrayList<java.lang.Integer> result = new java.util.ArrayList<>(count);
            while (cursor.moveToNext()) {
                int uid = cursor.getInt(cursor.getColumnIndexOrThrow("uid"));
                result.add(java.lang.Integer.valueOf(uid));
            }
            if (cursor != null) {
                cursor.close();
            }
            return result;
        } catch (java.lang.Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public java.security.PublicKey getRecoveryServicePublicKey(int userId, int uid) {
        byte[] keyBytes = getBytes(userId, uid, "public_key");
        if (keyBytes == null) {
            return null;
        }
        try {
            return decodeX509Key(keyBytes);
        } catch (java.security.spec.InvalidKeySpecException e) {
            android.util.Log.wtf(TAG, java.lang.String.format(java.util.Locale.US, "Recovery service public key entry cannot be decoded for userId=%d uid=%d.", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid)));
            return null;
        }
    }

    public long setRecoverySecretTypes(int userId, int uid, int[] secretTypes) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        final java.util.StringJoiner joiner = new java.util.StringJoiner(",");
        java.util.Arrays.stream(secretTypes).forEach(new java.util.function.IntConsumer() { // from class: com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb$$ExternalSyntheticLambda0
            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                joiner.add(java.lang.Integer.toString(i));
            }
        });
        java.lang.String typesAsCsv = joiner.toString();
        values.put("secret_types", typesAsCsv);
        ensureRecoveryServiceMetadataEntryExists(userId, uid);
        return db.update("recovery_service_metadata", values, "user_id = ? AND uid = ?", new java.lang.String[]{java.lang.String.valueOf(userId), java.lang.String.valueOf(uid)});
    }

    public int[] getRecoverySecretTypes(int userId, int uid) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"_id", "user_id", "uid", "secret_types"};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid)};
        android.database.Cursor cursor = db.query("recovery_service_metadata", projection, "user_id = ? AND uid = ?", selectionArguments, null, null, null);
        try {
            int count = cursor.getCount();
            if (count == 0) {
                int[] iArr = new int[0];
                if (cursor != null) {
                    cursor.close();
                }
                return iArr;
            }
            if (count > 1) {
                android.util.Log.wtf(TAG, java.lang.String.format(java.util.Locale.US, "%d deviceId entries found for userId=%d uid=%d. Should only ever be 0 or 1.", java.lang.Integer.valueOf(count), java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid)));
                int[] iArr2 = new int[0];
                if (cursor != null) {
                    cursor.close();
                }
                return iArr2;
            }
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow("secret_types");
            if (cursor.isNull(idx)) {
                int[] iArr3 = new int[0];
                if (cursor != null) {
                    cursor.close();
                }
                return iArr3;
            }
            java.lang.String csv = cursor.getString(idx);
            if (android.text.TextUtils.isEmpty(csv)) {
                int[] iArr4 = new int[0];
                if (cursor != null) {
                    cursor.close();
                }
                return iArr4;
            }
            java.lang.String[] types = csv.split(",");
            int[] result = new int[types.length];
            for (int i = 0; i < types.length; i++) {
                try {
                    result[i] = java.lang.Integer.parseInt(types[i]);
                } catch (java.lang.NumberFormatException e) {
                    android.util.Log.wtf(TAG, "String format error " + e);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return result;
        } finally {
        }
    }

    public long setActiveRootOfTrust(int userId, int uid, java.lang.String rootAlias) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("active_root_of_trust", rootAlias);
        ensureRecoveryServiceMetadataEntryExists(userId, uid);
        return db.update("recovery_service_metadata", values, "user_id = ? AND uid = ?", new java.lang.String[]{java.lang.String.valueOf(userId), java.lang.String.valueOf(uid)});
    }

    public java.lang.String getActiveRootOfTrust(int userId, int uid) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"_id", "user_id", "uid", "active_root_of_trust"};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid)};
        android.database.Cursor cursor = db.query("recovery_service_metadata", projection, "user_id = ? AND uid = ?", selectionArguments, null, null, null);
        try {
            int count = cursor.getCount();
            if (count == 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            if (count > 1) {
                android.util.Log.wtf(TAG, java.lang.String.format(java.util.Locale.US, "%d deviceId entries found for userId=%d uid=%d. Should only ever be 0 or 1.", java.lang.Integer.valueOf(count), java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid)));
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow("active_root_of_trust");
            if (cursor.isNull(idx)) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            java.lang.String result = cursor.getString(idx);
            if (android.text.TextUtils.isEmpty(result)) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            if (cursor != null) {
                cursor.close();
            }
            return result;
        } catch (java.lang.Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public long setCounterId(int userId, int uid, long counterId) {
        return setLong(userId, uid, "counter_id", counterId);
    }

    public java.lang.Long getCounterId(int userId, int uid) {
        return getLong(userId, uid, "counter_id");
    }

    public long setServerParams(int userId, int uid, byte[] serverParams) {
        return setBytes(userId, uid, "server_params", serverParams);
    }

    public byte[] getServerParams(int userId, int uid) {
        return getBytes(userId, uid, "server_params");
    }

    public long setSnapshotVersion(int userId, int uid, long snapshotVersion) {
        return setLong(userId, uid, "snapshot_version", snapshotVersion);
    }

    public java.lang.Long getSnapshotVersion(int userId, int uid) {
        return getLong(userId, uid, "snapshot_version");
    }

    public long setShouldCreateSnapshot(int userId, int uid, boolean pending) {
        return setLong(userId, uid, "should_create_snapshot", pending ? 1L : 0L);
    }

    public boolean getShouldCreateSnapshot(int userId, int uid) {
        java.lang.Long res = getLong(userId, uid, "should_create_snapshot");
        return (res == null || res.longValue() == 0) ? false : true;
    }

    private java.lang.Long getLong(int userId, int uid, java.lang.String key) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"_id", "user_id", "uid", key};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid)};
        android.database.Cursor cursor = db.query("recovery_service_metadata", projection, "user_id = ? AND uid = ?", selectionArguments, null, null, null);
        try {
            int count = cursor.getCount();
            if (count == 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            if (count > 1) {
                android.util.Log.wtf(TAG, java.lang.String.format(java.util.Locale.US, "%d entries found for userId=%d uid=%d. Should only ever be 0 or 1.", java.lang.Integer.valueOf(count), java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid)));
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(key);
            if (cursor.isNull(idx)) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            java.lang.Long lValueOf = java.lang.Long.valueOf(cursor.getLong(idx));
            if (cursor != null) {
                cursor.close();
            }
            return lValueOf;
        } catch (java.lang.Throwable th) {
            if (cursor == null) {
                throw th;
            }
            try {
                cursor.close();
                throw th;
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    private long setLong(int userId, int uid, java.lang.String key, long value) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(key, java.lang.Long.valueOf(value));
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid)};
        ensureRecoveryServiceMetadataEntryExists(userId, uid);
        return db.update("recovery_service_metadata", values, "user_id = ? AND uid = ?", selectionArguments);
    }

    private byte[] getBytes(int userId, int uid, java.lang.String key) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"_id", "user_id", "uid", key};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid)};
        android.database.Cursor cursor = db.query("recovery_service_metadata", projection, "user_id = ? AND uid = ?", selectionArguments, null, null, null);
        try {
            int count = cursor.getCount();
            if (count == 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            if (count > 1) {
                android.util.Log.wtf(TAG, java.lang.String.format(java.util.Locale.US, "%d entries found for userId=%d uid=%d. Should only ever be 0 or 1.", java.lang.Integer.valueOf(count), java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid)));
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(key);
            if (cursor.isNull(idx)) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            byte[] blob = cursor.getBlob(idx);
            if (cursor != null) {
                cursor.close();
            }
            return blob;
        } catch (java.lang.Throwable th) {
            if (cursor == null) {
                throw th;
            }
            try {
                cursor.close();
                throw th;
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    private long setBytes(int userId, int uid, java.lang.String key, byte[] value) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(key, value);
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid)};
        ensureRecoveryServiceMetadataEntryExists(userId, uid);
        return db.update("recovery_service_metadata", values, "user_id = ? AND uid = ?", selectionArguments);
    }

    private byte[] getBytes(int userId, int uid, java.lang.String rootAlias, java.lang.String key) {
        java.lang.String rootAlias2 = this.mTestOnlyInsecureCertificateHelper.getDefaultCertificateAliasIfEmpty(rootAlias);
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"_id", "user_id", "uid", "root_alias", key};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid), rootAlias2};
        android.database.Cursor cursor = db.query("root_of_trust", projection, "user_id = ? AND uid = ? AND root_alias = ?", selectionArguments, null, null, null);
        try {
            int count = cursor.getCount();
            if (count == 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            if (count > 1) {
                android.util.Log.wtf(TAG, java.lang.String.format(java.util.Locale.US, "%d entries found for userId=%d uid=%d. Should only ever be 0 or 1.", java.lang.Integer.valueOf(count), java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid)));
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(key);
            if (cursor.isNull(idx)) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            byte[] blob = cursor.getBlob(idx);
            if (cursor != null) {
                cursor.close();
            }
            return blob;
        } catch (java.lang.Throwable th) {
            if (cursor == null) {
                throw th;
            }
            try {
                cursor.close();
                throw th;
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    private long setBytes(int userId, int uid, java.lang.String rootAlias, java.lang.String key, byte[] value) {
        java.lang.String rootAlias2 = this.mTestOnlyInsecureCertificateHelper.getDefaultCertificateAliasIfEmpty(rootAlias);
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(key, value);
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid), rootAlias2};
        ensureRootOfTrustEntryExists(userId, uid, rootAlias2);
        return db.update("root_of_trust", values, "user_id = ? AND uid = ? AND root_alias = ?", selectionArguments);
    }

    private java.lang.Long getLong(int userId, int uid, java.lang.String rootAlias, java.lang.String key) {
        java.lang.String rootAlias2 = this.mTestOnlyInsecureCertificateHelper.getDefaultCertificateAliasIfEmpty(rootAlias);
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getReadableDatabase();
        java.lang.String[] projection = {"_id", "user_id", "uid", "root_alias", key};
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid), rootAlias2};
        android.database.Cursor cursor = db.query("root_of_trust", projection, "user_id = ? AND uid = ? AND root_alias = ?", selectionArguments, null, null, null);
        try {
            int count = cursor.getCount();
            if (count == 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            if (count > 1) {
                android.util.Log.wtf(TAG, java.lang.String.format(java.util.Locale.US, "%d entries found for userId=%d uid=%d. Should only ever be 0 or 1.", java.lang.Integer.valueOf(count), java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid)));
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(key);
            if (cursor.isNull(idx)) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            java.lang.Long lValueOf = java.lang.Long.valueOf(cursor.getLong(idx));
            if (cursor != null) {
                cursor.close();
            }
            return lValueOf;
        } catch (java.lang.Throwable th) {
            if (cursor == null) {
                throw th;
            }
            try {
                cursor.close();
                throw th;
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    private long setLong(int userId, int uid, java.lang.String rootAlias, java.lang.String key, long value) {
        java.lang.String rootAlias2 = this.mTestOnlyInsecureCertificateHelper.getDefaultCertificateAliasIfEmpty(rootAlias);
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(key, java.lang.Long.valueOf(value));
        java.lang.String[] selectionArguments = {java.lang.Integer.toString(userId), java.lang.Integer.toString(uid), rootAlias2};
        ensureRootOfTrustEntryExists(userId, uid, rootAlias2);
        return db.update("root_of_trust", values, "user_id = ? AND uid = ? AND root_alias = ?", selectionArguments);
    }

    public void removeUserFromAllTables(int userId) {
        removeUserFromKeysTable(userId);
        removeUserFromUserMetadataTable(userId);
        removeUserFromRecoveryServiceMetadataTable(userId);
        removeUserFromRootOfTrustTable(userId);
    }

    private boolean removeUserFromKeysTable(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        java.lang.String[] selectionArgs = {java.lang.Integer.toString(userId)};
        return db.delete("keys", "user_id = ?", selectionArgs) > 0;
    }

    private boolean removeUserFromUserMetadataTable(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        java.lang.String[] selectionArgs = {java.lang.Integer.toString(userId)};
        return db.delete("user_metadata", "user_id = ?", selectionArgs) > 0;
    }

    private boolean removeUserFromRecoveryServiceMetadataTable(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        java.lang.String[] selectionArgs = {java.lang.Integer.toString(userId)};
        return db.delete("recovery_service_metadata", "user_id = ?", selectionArgs) > 0;
    }

    private boolean removeUserFromRootOfTrustTable(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        java.lang.String[] selectionArgs = {java.lang.Integer.toString(userId)};
        return db.delete("root_of_trust", "user_id = ?", selectionArgs) > 0;
    }

    private void ensureRecoveryServiceMetadataEntryExists(int userId, int uid) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("user_id", java.lang.Integer.valueOf(userId));
        values.put("uid", java.lang.Integer.valueOf(uid));
        db.insertWithOnConflict("recovery_service_metadata", null, values, 4);
    }

    private void ensureRootOfTrustEntryExists(int userId, int uid, java.lang.String rootAlias) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("user_id", java.lang.Integer.valueOf(userId));
        values.put("uid", java.lang.Integer.valueOf(uid));
        values.put("root_alias", rootAlias);
        db.insertWithOnConflict("root_of_trust", null, values, 4);
    }

    private void ensureUserMetadataEntryExists(int userId) {
        android.database.sqlite.SQLiteDatabase db = this.mKeyStoreDbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("user_id", java.lang.Integer.valueOf(userId));
        db.insertWithOnConflict("user_metadata", null, values, 4);
    }

    public void close() {
        this.mKeyStoreDbHelper.close();
    }

    private static java.security.PublicKey decodeX509Key(byte[] keyBytes) throws java.security.spec.InvalidKeySpecException {
        java.security.spec.X509EncodedKeySpec publicKeySpec = new java.security.spec.X509EncodedKeySpec(keyBytes);
        try {
            return java.security.KeyFactory.getInstance("EC").generatePublic(publicKeySpec);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private static java.security.cert.CertPath decodeCertPath(byte[] bytes) throws java.security.cert.CertificateException {
        try {
            java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
            return certFactory.generateCertPath(new java.io.ByteArrayInputStream(bytes), CERT_PATH_ENCODING);
        } catch (java.security.cert.CertificateException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}
