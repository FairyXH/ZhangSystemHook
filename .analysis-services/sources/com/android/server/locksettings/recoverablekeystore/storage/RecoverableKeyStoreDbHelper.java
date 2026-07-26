package com.android.server.locksettings.recoverablekeystore.storage;

/* JADX INFO: loaded from: classes2.dex */
class RecoverableKeyStoreDbHelper extends android.database.sqlite.SQLiteOpenHelper {
    private static final java.lang.String DATABASE_NAME = "recoverablekeystore.db";
    static final int DATABASE_VERSION_7 = 7;
    private static final java.lang.String SQL_CREATE_KEYS_ENTRY = "CREATE TABLE keys( _id INTEGER PRIMARY KEY,user_id INTEGER,uid INTEGER,alias TEXT,nonce BLOB,wrapped_key BLOB,platform_key_generation_id INTEGER,last_synced_at INTEGER,recovery_status INTEGER,key_metadata BLOB,UNIQUE(uid,alias))";
    private static final java.lang.String SQL_CREATE_RECOVERY_SERVICE_METADATA_ENTRY = "CREATE TABLE recovery_service_metadata (_id INTEGER PRIMARY KEY,user_id INTEGER,uid INTEGER,snapshot_version INTEGER,should_create_snapshot INTEGER,active_root_of_trust TEXT,public_key BLOB,cert_path BLOB,cert_serial INTEGER,secret_types TEXT,counter_id INTEGER,server_params BLOB,UNIQUE(user_id,uid))";
    private static final java.lang.String SQL_CREATE_ROOT_OF_TRUST_ENTRY = "CREATE TABLE root_of_trust (_id INTEGER PRIMARY KEY,user_id INTEGER,uid INTEGER,root_alias TEXT,cert_path BLOB,cert_serial INTEGER,UNIQUE(user_id,uid,root_alias))";
    private static final java.lang.String SQL_CREATE_USER_METADATA_ENTRY = "CREATE TABLE user_metadata( _id INTEGER PRIMARY KEY,user_id INTEGER UNIQUE,platform_key_generation_id INTEGER,user_serial_number INTEGER DEFAULT -1)";
    private static final java.lang.String SQL_CREATE_USER_METADATA_ENTRY_FOR_V7 = "CREATE TABLE user_metadata( _id INTEGER PRIMARY KEY,user_id INTEGER UNIQUE,platform_key_generation_id INTEGER,user_serial_number INTEGER DEFAULT -1,bad_remote_guess_counter INTEGER DEFAULT 0)";
    private static final java.lang.String SQL_DELETE_KEYS_ENTRY = "DROP TABLE IF EXISTS keys";
    private static final java.lang.String SQL_DELETE_RECOVERY_SERVICE_METADATA_ENTRY = "DROP TABLE IF EXISTS recovery_service_metadata";
    private static final java.lang.String SQL_DELETE_ROOT_OF_TRUST_ENTRY = "DROP TABLE IF EXISTS root_of_trust";
    private static final java.lang.String SQL_DELETE_USER_METADATA_ENTRY = "DROP TABLE IF EXISTS user_metadata";
    private static final java.lang.String TAG = "RecoverableKeyStoreDbHp";

    RecoverableKeyStoreDbHelper(android.content.Context context) {
        super(context, DATABASE_NAME, (android.database.sqlite.SQLiteDatabase.CursorFactory) null, getDbVersion(context));
    }

    private static int getDbVersion(android.content.Context context) {
        return 7;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_KEYS_ENTRY);
        db.execSQL(SQL_CREATE_USER_METADATA_ENTRY_FOR_V7);
        db.execSQL(SQL_CREATE_RECOVERY_SERVICE_METADATA_ENTRY);
        db.execSQL(SQL_CREATE_ROOT_OF_TRUST_ENTRY);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.e(TAG, "Recreating recoverablekeystore after unexpected version downgrade.");
        dropAllKnownTables(db);
        onCreate(db);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion < 2) {
                dropAllKnownTables(db);
                onCreate(db);
                return;
            }
            if (oldVersion < 3 && newVersion >= 3) {
                upgradeDbForVersion3(db);
                oldVersion = 3;
            }
            if (oldVersion < 4 && newVersion >= 4) {
                upgradeDbForVersion4(db);
                oldVersion = 4;
            }
            if (oldVersion < 5 && newVersion >= 5) {
                upgradeDbForVersion5(db);
                oldVersion = 5;
            }
            if (oldVersion < 6 && newVersion >= 6) {
                upgradeDbForVersion6(db);
                oldVersion = 6;
            }
            if (oldVersion < 7 && newVersion >= 7) {
                try {
                    upgradeDbForVersion7(db);
                } catch (android.database.sqlite.SQLiteException e) {
                    android.util.Log.w(TAG, "Column was added without version update - ignore error", e);
                }
                oldVersion = 7;
            }
            if (oldVersion != newVersion) {
                android.util.Log.e(TAG, "Failed to update recoverablekeystore database to the most recent version");
            }
        } catch (android.database.sqlite.SQLiteException e2) {
            android.util.Log.e(TAG, "Recreating recoverablekeystore after unexpected upgrade error.", e2);
            dropAllKnownTables(db);
            onCreate(db);
        }
    }

    private void dropAllKnownTables(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_KEYS_ENTRY);
        db.execSQL(SQL_DELETE_USER_METADATA_ENTRY);
        db.execSQL(SQL_DELETE_RECOVERY_SERVICE_METADATA_ENTRY);
        db.execSQL(SQL_DELETE_ROOT_OF_TRUST_ENTRY);
    }

    private void upgradeDbForVersion3(android.database.sqlite.SQLiteDatabase db) {
        addColumnToTable(db, "recovery_service_metadata", "cert_path", "BLOB", null);
        addColumnToTable(db, "recovery_service_metadata", "cert_serial", "INTEGER", null);
    }

    private void upgradeDbForVersion4(android.database.sqlite.SQLiteDatabase db) {
        android.util.Log.d(TAG, "Updating recoverable keystore database to version 4");
        db.execSQL(SQL_CREATE_ROOT_OF_TRUST_ENTRY);
        addColumnToTable(db, "recovery_service_metadata", "active_root_of_trust", "TEXT", null);
    }

    private void upgradeDbForVersion5(android.database.sqlite.SQLiteDatabase db) {
        android.util.Log.d(TAG, "Updating recoverable keystore database to version 5");
        addColumnToTable(db, "keys", "key_metadata", "BLOB", null);
    }

    private void upgradeDbForVersion6(android.database.sqlite.SQLiteDatabase db) {
        android.util.Log.d(TAG, "Updating recoverable keystore database to version 6");
        addColumnToTable(db, "user_metadata", "user_serial_number", "INTEGER DEFAULT -1", null);
    }

    private void upgradeDbForVersion7(android.database.sqlite.SQLiteDatabase db) {
        android.util.Log.d(TAG, "Updating recoverable keystore database to version 7");
        addColumnToTable(db, "user_metadata", "bad_remote_guess_counter", "INTEGER DEFAULT 0", null);
    }

    private static void addColumnToTable(android.database.sqlite.SQLiteDatabase db, java.lang.String tableName, java.lang.String column, java.lang.String columnType, java.lang.String defaultStr) {
        android.util.Log.d(TAG, "Adding column " + column + " to " + tableName + ".");
        java.lang.String alterStr = "ALTER TABLE " + tableName + " ADD COLUMN " + column + " " + columnType;
        if (defaultStr != null && !defaultStr.isEmpty()) {
            alterStr = alterStr + " DEFAULT " + defaultStr;
        }
        db.execSQL(alterStr + ";");
    }
}
