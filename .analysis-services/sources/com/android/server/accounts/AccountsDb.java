package com.android.server.accounts;

/* JADX INFO: loaded from: classes.dex */
class AccountsDb implements java.lang.AutoCloseable {
    private static final java.lang.String ACCOUNTS_ID = "_id";
    private static final java.lang.String ACCOUNTS_LAST_AUTHENTICATE_TIME_EPOCH_MILLIS = "last_password_entry_time_millis_epoch";
    private static final java.lang.String ACCOUNTS_NAME = "name";
    private static final java.lang.String ACCOUNTS_PASSWORD = "password";
    private static final java.lang.String ACCOUNTS_PREVIOUS_NAME = "previous_name";
    private static final java.lang.String ACCOUNTS_TYPE = "type";
    private static final java.lang.String ACCOUNT_ACCESS_GRANTS = "SELECT name, uid FROM accounts, grants WHERE accounts_id=_id";
    private static final java.lang.String AUTHTOKENS_ACCOUNTS_ID = "accounts_id";
    private static final java.lang.String AUTHTOKENS_ID = "_id";
    private static final java.lang.String AUTHTOKENS_TYPE = "type";
    static final java.lang.String CE_DATABASE_NAME = "accounts_ce.db";
    private static final int CE_DATABASE_VERSION = 10;
    private static final java.lang.String CE_DB_PREFIX = "ceDb.";
    private static final java.lang.String CE_TABLE_ACCOUNTS = "ceDb.accounts";
    private static final java.lang.String CE_TABLE_AUTHTOKENS = "ceDb.authtokens";
    private static final java.lang.String CE_TABLE_EXTRAS = "ceDb.extras";
    private static final java.lang.String COUNT_OF_MATCHING_GRANTS = "SELECT COUNT(*) FROM grants, accounts WHERE accounts_id=_id AND uid=? AND auth_token_type=? AND name=? AND type=?";
    private static final java.lang.String COUNT_OF_MATCHING_GRANTS_ANY_TOKEN = "SELECT COUNT(*) FROM grants, accounts WHERE accounts_id=_id AND uid=? AND name=? AND type=?";
    private static final java.lang.String DATABASE_NAME = "accounts.db";
    static final java.lang.String DE_DATABASE_NAME = "accounts_de.db";
    private static final int DE_DATABASE_VERSION = 3;
    private static final java.lang.String EXTRAS_ACCOUNTS_ID = "accounts_id";
    private static final java.lang.String EXTRAS_ID = "_id";
    private static final java.lang.String EXTRAS_KEY = "key";
    private static final java.lang.String EXTRAS_VALUE = "value";
    private static final java.lang.String GRANTS_ACCOUNTS_ID = "accounts_id";
    private static final java.lang.String GRANTS_AUTH_TOKEN_TYPE = "auth_token_type";
    private static final java.lang.String GRANTS_GRANTEE_UID = "uid";
    static final int MAX_DEBUG_DB_SIZE = 64;
    private static final java.lang.String META_KEY = "key";
    private static final java.lang.String META_KEY_DELIMITER = ":";
    private static final java.lang.String META_KEY_FOR_AUTHENTICATOR_UID_FOR_TYPE_PREFIX = "auth_uid_for_type:";
    private static final java.lang.String META_VALUE = "value";
    private static final int PRE_N_DATABASE_VERSION = 9;
    private static final java.lang.String SELECTION_ACCOUNTS_ID_BY_ACCOUNT = "accounts_id=(select _id FROM accounts WHERE name=? AND type=?)";
    private static final java.lang.String SELECTION_META_BY_AUTHENTICATOR_TYPE = "key LIKE ?";
    private static final java.lang.String SHARED_ACCOUNTS_ID = "_id";
    static final java.lang.String TABLE_ACCOUNTS = "accounts";
    private static final java.lang.String TABLE_AUTHTOKENS = "authtokens";
    private static final java.lang.String TABLE_EXTRAS = "extras";
    private static final java.lang.String TABLE_GRANTS = "grants";
    private static final java.lang.String TABLE_META = "meta";
    static final java.lang.String TABLE_SHARED_ACCOUNTS = "shared_accounts";
    private static final java.lang.String TABLE_VISIBILITY = "visibility";
    private static final java.lang.String TAG = "AccountsDb";
    private static final java.lang.String VISIBILITY_ACCOUNTS_ID = "accounts_id";
    private static final java.lang.String VISIBILITY_PACKAGE = "_package";
    private static final java.lang.String VISIBILITY_VALUE = "value";
    private final android.content.Context mContext;
    private final com.android.server.accounts.AccountsDb.DeDatabaseHelper mDeDatabase;
    private volatile android.database.sqlite.SQLiteStatement mDebugStatementForLogging;
    private final java.io.File mPreNDatabaseFile;
    private static java.lang.String TABLE_DEBUG = "debug_table";
    private static java.lang.String DEBUG_TABLE_ACTION_TYPE = "action_type";
    private static java.lang.String DEBUG_TABLE_TIMESTAMP = "time";
    private static java.lang.String DEBUG_TABLE_CALLER_UID = "caller_uid";
    private static java.lang.String DEBUG_TABLE_TABLE_NAME = "table_name";
    private static java.lang.String DEBUG_TABLE_KEY = "primary_key";
    static java.lang.String DEBUG_ACTION_SET_PASSWORD = "action_set_password";
    static java.lang.String DEBUG_ACTION_CLEAR_PASSWORD = "action_clear_password";
    static java.lang.String DEBUG_ACTION_ACCOUNT_ADD = "action_account_add";
    static java.lang.String DEBUG_ACTION_ACCOUNT_REMOVE = "action_account_remove";
    static java.lang.String DEBUG_ACTION_ACCOUNT_REMOVE_DE = "action_account_remove_de";
    static java.lang.String DEBUG_ACTION_AUTHENTICATOR_REMOVE = "action_authenticator_remove";
    static java.lang.String DEBUG_ACTION_ACCOUNT_RENAME = "action_account_rename";
    static java.lang.String DEBUG_ACTION_CALLED_ACCOUNT_ADD = "action_called_account_add";
    static java.lang.String DEBUG_ACTION_CALLED_ACCOUNT_REMOVE = "action_called_account_remove";
    static java.lang.String DEBUG_ACTION_SYNC_DE_CE_ACCOUNTS = "action_sync_de_ce_accounts";
    static java.lang.String DEBUG_ACTION_CALLED_START_ACCOUNT_ADD = "action_called_start_account_add";
    static java.lang.String DEBUG_ACTION_CALLED_ACCOUNT_SESSION_FINISH = "action_called_account_session_finish";
    private static final java.lang.String ACCOUNTS_TYPE_COUNT = "count(type)";
    private static final java.lang.String[] ACCOUNT_TYPE_COUNT_PROJECTION = {"type", ACCOUNTS_TYPE_COUNT};
    private static final java.lang.String AUTHTOKENS_AUTHTOKEN = "authtoken";
    private static final java.lang.String[] COLUMNS_AUTHTOKENS_TYPE_AND_AUTHTOKEN = {"type", AUTHTOKENS_AUTHTOKEN};
    private static final java.lang.String[] COLUMNS_EXTRAS_KEY_AND_VALUE = {"key", "value"};
    final java.lang.Object mDebugStatementLock = new java.lang.Object();
    private volatile long mDebugDbInsertionPoint = -1;

    AccountsDb(com.android.server.accounts.AccountsDb.DeDatabaseHelper deDatabase, android.content.Context context, java.io.File preNDatabaseFile) {
        this.mDeDatabase = deDatabase;
        this.mContext = context;
        this.mPreNDatabaseFile = preNDatabaseFile;
    }

    private static class CeDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
        CeDatabaseHelper(android.content.Context context, java.lang.String ceDatabaseName) {
            super(context, ceDatabaseName, (android.database.sqlite.SQLiteDatabase.CursorFactory) null, 10);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            android.util.Log.i(com.android.server.accounts.AccountsDb.TAG, "Creating CE database " + getDatabaseName());
            db.execSQL("CREATE TABLE accounts ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, type TEXT NOT NULL, password TEXT, UNIQUE(name,type))");
            db.execSQL("CREATE TABLE authtokens (  _id INTEGER PRIMARY KEY AUTOINCREMENT,  accounts_id INTEGER NOT NULL, type TEXT NOT NULL,  authtoken TEXT,  UNIQUE (accounts_id,type))");
            db.execSQL("CREATE TABLE extras ( _id INTEGER PRIMARY KEY AUTOINCREMENT, accounts_id INTEGER, key TEXT NOT NULL, value TEXT, UNIQUE(accounts_id,key))");
            createAccountsDeletionTrigger(db);
        }

        private void createAccountsDeletionTrigger(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL(" CREATE TRIGGER accountsDelete DELETE ON accounts BEGIN   DELETE FROM authtokens     WHERE accounts_id=OLD._id ;   DELETE FROM extras     WHERE accounts_id=OLD._id ; END");
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
            android.util.Log.i(com.android.server.accounts.AccountsDb.TAG, "Upgrade CE from version " + oldVersion + " to version " + newVersion);
            if (oldVersion == 9) {
                if (android.util.Log.isLoggable(com.android.server.accounts.AccountsDb.TAG, 2)) {
                    android.util.Log.v(com.android.server.accounts.AccountsDb.TAG, "onUpgrade upgrading to v10");
                }
                db.execSQL("DROP TABLE IF EXISTS meta");
                db.execSQL("DROP TABLE IF EXISTS shared_accounts");
                db.execSQL("DROP TRIGGER IF EXISTS accountsDelete");
                createAccountsDeletionTrigger(db);
                db.execSQL("DROP TABLE IF EXISTS grants");
                db.execSQL("DROP TABLE IF EXISTS " + com.android.server.accounts.AccountsDb.TABLE_DEBUG);
                oldVersion++;
            }
            if (oldVersion != newVersion) {
                android.util.Log.e(com.android.server.accounts.AccountsDb.TAG, "failed to upgrade version " + oldVersion + " to version " + newVersion);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onDowngrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
            android.util.Log.e(com.android.server.accounts.AccountsDb.TAG, "onDowngrade: recreate accounts CE table");
            com.android.server.accounts.AccountsDb.resetDatabase(db);
            onCreate(db);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(android.database.sqlite.SQLiteDatabase db) {
            if (android.util.Log.isLoggable(com.android.server.accounts.AccountsDb.TAG, 2)) {
                android.util.Log.v(com.android.server.accounts.AccountsDb.TAG, "opened database accounts_ce.db");
            }
        }

        static com.android.server.accounts.AccountsDb.CeDatabaseHelper create(android.content.Context context, java.io.File preNDatabaseFile, java.io.File ceDatabaseFile) {
            boolean newDbExists = ceDatabaseFile.exists();
            if (android.util.Log.isLoggable(com.android.server.accounts.AccountsDb.TAG, 2)) {
                android.util.Log.v(com.android.server.accounts.AccountsDb.TAG, "CeDatabaseHelper.create ceDatabaseFile=" + ceDatabaseFile + " oldDbExists=" + preNDatabaseFile.exists() + " newDbExists=" + newDbExists);
            }
            boolean removeOldDb = false;
            if (!newDbExists && preNDatabaseFile.exists()) {
                removeOldDb = migratePreNDbToCe(preNDatabaseFile, ceDatabaseFile);
            }
            com.android.server.accounts.AccountsDb.CeDatabaseHelper ceHelper = new com.android.server.accounts.AccountsDb.CeDatabaseHelper(context, ceDatabaseFile.getPath());
            ceHelper.getWritableDatabase();
            ceHelper.close();
            if (removeOldDb) {
                android.util.Slog.i(com.android.server.accounts.AccountsDb.TAG, "Migration complete - removing pre-N db " + preNDatabaseFile);
                if (!android.database.sqlite.SQLiteDatabase.deleteDatabase(preNDatabaseFile)) {
                    android.util.Slog.e(com.android.server.accounts.AccountsDb.TAG, "Cannot remove pre-N db " + preNDatabaseFile);
                }
            }
            return ceHelper;
        }

        private static boolean migratePreNDbToCe(java.io.File oldDbFile, java.io.File ceDbFile) {
            android.util.Slog.i(com.android.server.accounts.AccountsDb.TAG, "Moving pre-N DB " + oldDbFile + " to CE " + ceDbFile);
            try {
                android.os.FileUtils.copyFileOrThrow(oldDbFile, ceDbFile);
                return true;
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.accounts.AccountsDb.TAG, "Cannot copy file to " + ceDbFile + " from " + oldDbFile, e);
                com.android.server.accounts.AccountsDb.deleteDbFileWarnIfFailed(ceDbFile);
                return false;
            }
        }
    }

    android.database.Cursor findAuthtokenForAllAccounts(java.lang.String accountType, java.lang.String authToken) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabaseUserIsUnlocked();
        return db.rawQuery("SELECT ceDb.authtokens._id, ceDb.accounts.name, ceDb.authtokens.type FROM ceDb.accounts JOIN ceDb.authtokens ON ceDb.accounts._id = ceDb.authtokens.accounts_id WHERE ceDb.authtokens.authtoken = ? AND ceDb.accounts.type = ?", new java.lang.String[]{authToken, accountType});
    }

    java.util.Map<java.lang.String, java.lang.String> findAuthTokensByAccount(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabaseUserIsUnlocked();
        java.util.HashMap<java.lang.String, java.lang.String> authTokensForAccount = new java.util.HashMap<>();
        android.database.Cursor cursor = db.query(CE_TABLE_AUTHTOKENS, COLUMNS_AUTHTOKENS_TYPE_AND_AUTHTOKEN, SELECTION_ACCOUNTS_ID_BY_ACCOUNT, new java.lang.String[]{account.name, account.type}, null, null, null);
        while (cursor.moveToNext()) {
            try {
                java.lang.String type = cursor.getString(0);
                java.lang.String authToken = cursor.getString(1);
                authTokensForAccount.put(type, authToken);
            } finally {
                cursor.close();
            }
        }
        return authTokensForAccount;
    }

    boolean deleteAuthtokensByAccountIdAndType(long accountId, java.lang.String authtokenType) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        return db.delete(CE_TABLE_AUTHTOKENS, "accounts_id=? AND type=?", new java.lang.String[]{java.lang.String.valueOf(accountId), authtokenType}) > 0;
    }

    boolean deleteAuthToken(java.lang.String authTokenId) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        return db.delete(CE_TABLE_AUTHTOKENS, "_id= ?", new java.lang.String[]{authTokenId}) > 0;
    }

    long insertAuthToken(long accountId, java.lang.String authTokenType, java.lang.String authToken) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("accounts_id", java.lang.Long.valueOf(accountId));
        values.put("type", authTokenType);
        values.put(AUTHTOKENS_AUTHTOKEN, authToken);
        return db.insert(CE_TABLE_AUTHTOKENS, AUTHTOKENS_AUTHTOKEN, values);
    }

    int updateCeAccountPassword(long accountId, java.lang.String password) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("password", password);
        return db.update(CE_TABLE_ACCOUNTS, values, "_id=?", new java.lang.String[]{java.lang.String.valueOf(accountId)});
    }

    boolean renameCeAccount(long accountId, java.lang.String newName) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("name", newName);
        java.lang.String[] argsAccountId = {java.lang.String.valueOf(accountId)};
        return db.update(CE_TABLE_ACCOUNTS, values, "_id=?", argsAccountId) > 0;
    }

    boolean deleteAuthTokensByAccountId(long accountId) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        return db.delete(CE_TABLE_AUTHTOKENS, "accounts_id=?", new java.lang.String[]{java.lang.String.valueOf(accountId)}) > 0;
    }

    long findExtrasIdByAccountId(long accountId, java.lang.String key) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabaseUserIsUnlocked();
        android.database.Cursor cursor = db.query(CE_TABLE_EXTRAS, new java.lang.String[]{"_id"}, "accounts_id=" + accountId + " AND key=?", new java.lang.String[]{key}, null, null, null);
        try {
            if (cursor.moveToNext()) {
                return cursor.getLong(0);
            }
            cursor.close();
            return -1L;
        } finally {
            cursor.close();
        }
    }

    boolean updateExtra(long extrasId, java.lang.String value) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("value", value);
        int rows = db.update(TABLE_EXTRAS, values, "_id=?", new java.lang.String[]{java.lang.String.valueOf(extrasId)});
        return rows == 1;
    }

    long insertExtra(long accountId, java.lang.String key, java.lang.String value) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("key", key);
        values.put("accounts_id", java.lang.Long.valueOf(accountId));
        values.put("value", value);
        return db.insert(CE_TABLE_EXTRAS, "key", values);
    }

    java.util.Map<java.lang.String, java.lang.String> findUserExtrasForAccount(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabaseUserIsUnlocked();
        java.util.Map<java.lang.String, java.lang.String> userExtrasForAccount = new java.util.HashMap<>();
        java.lang.String[] selectionArgs = {account.name, account.type};
        android.database.Cursor cursor = db.query(CE_TABLE_EXTRAS, COLUMNS_EXTRAS_KEY_AND_VALUE, SELECTION_ACCOUNTS_ID_BY_ACCOUNT, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            try {
                java.lang.String tmpkey = cursor.getString(0);
                java.lang.String value = cursor.getString(1);
                userExtrasForAccount.put(tmpkey, value);
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
        if (cursor != null) {
            cursor.close();
        }
        return userExtrasForAccount;
    }

    long findCeAccountId(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabaseUserIsUnlocked();
        java.lang.String[] columns = {"_id"};
        java.lang.String[] selectionArgs = {account.name, account.type};
        android.database.Cursor cursor = db.query(CE_TABLE_ACCOUNTS, columns, "name=? AND type=?", selectionArgs, null, null, null);
        try {
            if (cursor.moveToNext()) {
                long j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
                return j;
            }
            if (cursor != null) {
                cursor.close();
                return -1L;
            }
            return -1L;
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

    java.lang.String findAccountPasswordByNameAndType(java.lang.String name, java.lang.String type) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabaseUserIsUnlocked();
        java.lang.String[] selectionArgs = {name, type};
        java.lang.String[] columns = {"password"};
        android.database.Cursor cursor = db.query(CE_TABLE_ACCOUNTS, columns, "name=? AND type=?", selectionArgs, null, null, null);
        try {
            if (cursor.moveToNext()) {
                java.lang.String string = cursor.getString(0);
                if (cursor != null) {
                    cursor.close();
                }
                return string;
            }
            if (cursor != null) {
                cursor.close();
                return null;
            }
            return null;
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

    long insertCeAccount(android.accounts.Account account, java.lang.String password) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("name", account.name);
        values.put("type", account.type);
        values.put("password", password);
        return db.insert(CE_TABLE_ACCOUNTS, "name", values);
    }

    static class DeDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
        private volatile boolean mCeAttached;
        private final int mUserId;

        private DeDatabaseHelper(android.content.Context context, int userId, java.lang.String deDatabaseName) {
            super(context, deDatabaseName, (android.database.sqlite.SQLiteDatabase.CursorFactory) null, 3);
            this.mUserId = userId;
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            android.util.Log.i(com.android.server.accounts.AccountsDb.TAG, "Creating DE database for user " + this.mUserId);
            db.execSQL("CREATE TABLE accounts ( _id INTEGER PRIMARY KEY, name TEXT NOT NULL, type TEXT NOT NULL, previous_name TEXT, last_password_entry_time_millis_epoch INTEGER DEFAULT 0, UNIQUE(name,type))");
            db.execSQL("CREATE TABLE meta ( key TEXT PRIMARY KEY NOT NULL, value TEXT)");
            createGrantsTable(db);
            createSharedAccountsTable(db);
            createAccountsDeletionTrigger(db);
            createDebugTable(db);
            createAccountsVisibilityTable(db);
            createAccountsDeletionVisibilityCleanupTrigger(db);
        }

        private void createSharedAccountsTable(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("CREATE TABLE shared_accounts ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, type TEXT NOT NULL, UNIQUE(name,type))");
        }

        private void createAccountsDeletionTrigger(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL(" CREATE TRIGGER accountsDelete DELETE ON accounts BEGIN   DELETE FROM grants     WHERE accounts_id=OLD._id ; END");
        }

        private void createGrantsTable(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("CREATE TABLE grants (  accounts_id INTEGER NOT NULL, auth_token_type STRING NOT NULL,  uid INTEGER NOT NULL,  UNIQUE (accounts_id,auth_token_type,uid))");
        }

        private void createAccountsVisibilityTable(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("CREATE TABLE visibility ( accounts_id INTEGER NOT NULL, _package TEXT NOT NULL, value INTEGER, PRIMARY KEY(accounts_id,_package))");
        }

        static void createDebugTable(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + com.android.server.accounts.AccountsDb.TABLE_DEBUG + " ( _id INTEGER," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_ACTION_TYPE + " TEXT NOT NULL, " + com.android.server.accounts.AccountsDb.DEBUG_TABLE_TIMESTAMP + " DATETIME," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_CALLER_UID + " INTEGER NOT NULL," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_TABLE_NAME + " TEXT NOT NULL," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_KEY + " INTEGER PRIMARY KEY)");
            db.execSQL("CREATE INDEX timestamp_index ON " + com.android.server.accounts.AccountsDb.TABLE_DEBUG + " (" + com.android.server.accounts.AccountsDb.DEBUG_TABLE_TIMESTAMP + ")");
        }

        private void createAccountsDeletionVisibilityCleanupTrigger(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL(" CREATE TRIGGER accountsDeleteVisibility DELETE ON accounts BEGIN   DELETE FROM visibility     WHERE accounts_id=OLD._id ; END");
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
            android.util.Log.i(com.android.server.accounts.AccountsDb.TAG, "upgrade from version " + oldVersion + " to version " + newVersion);
            if (oldVersion == 1) {
                createAccountsVisibilityTable(db);
                createAccountsDeletionVisibilityCleanupTrigger(db);
                oldVersion = 3;
            }
            if (oldVersion == 2) {
                db.execSQL("DROP TRIGGER IF EXISTS accountsDeleteVisibility");
                db.execSQL("DROP TABLE IF EXISTS visibility");
                createAccountsVisibilityTable(db);
                createAccountsDeletionVisibilityCleanupTrigger(db);
                oldVersion++;
            }
            if (oldVersion != newVersion) {
                android.util.Log.e(com.android.server.accounts.AccountsDb.TAG, "failed to upgrade version " + oldVersion + " to version " + newVersion);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onDowngrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
            android.util.Log.e(com.android.server.accounts.AccountsDb.TAG, "onDowngrade: recreate accounts DE table");
            com.android.server.accounts.AccountsDb.resetDatabase(db);
            onCreate(db);
        }

        public android.database.sqlite.SQLiteDatabase getReadableDatabaseUserIsUnlocked() {
            if (!this.mCeAttached) {
                android.util.Log.wtf(com.android.server.accounts.AccountsDb.TAG, "getReadableDatabaseUserIsUnlocked called while user " + this.mUserId + " is still locked. CE database is not yet available.", new java.lang.Throwable());
            }
            return super.getReadableDatabase();
        }

        public android.database.sqlite.SQLiteDatabase getWritableDatabaseUserIsUnlocked() {
            if (!this.mCeAttached) {
                android.util.Log.wtf(com.android.server.accounts.AccountsDb.TAG, "getWritableDatabaseUserIsUnlocked called while user " + this.mUserId + " is still locked. CE database is not yet available.", new java.lang.Throwable());
            }
            return super.getWritableDatabase();
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(android.database.sqlite.SQLiteDatabase db) {
            if (android.util.Log.isLoggable(com.android.server.accounts.AccountsDb.TAG, 2)) {
                android.util.Log.v(com.android.server.accounts.AccountsDb.TAG, "opened database accounts_de.db");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void migratePreNDbToDe(java.io.File preNDbFile) {
            android.util.Log.i(com.android.server.accounts.AccountsDb.TAG, "Migrate pre-N database to DE preNDbFile=" + preNDbFile);
            android.database.sqlite.SQLiteDatabase db = getWritableDatabase();
            db.execSQL("ATTACH DATABASE '" + preNDbFile.getPath() + "' AS preNDb");
            db.beginTransaction();
            db.execSQL("INSERT INTO accounts(_id,name,type, previous_name, last_password_entry_time_millis_epoch) SELECT _id,name,type, previous_name, last_password_entry_time_millis_epoch FROM preNDb.accounts");
            db.execSQL("INSERT INTO shared_accounts(_id,name,type) SELECT _id,name,type FROM preNDb.shared_accounts");
            db.execSQL("INSERT INTO " + com.android.server.accounts.AccountsDb.TABLE_DEBUG + "(_id," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_ACTION_TYPE + "," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_TIMESTAMP + "," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_CALLER_UID + "," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_TABLE_NAME + "," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_KEY + ") SELECT _id," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_ACTION_TYPE + "," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_TIMESTAMP + "," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_CALLER_UID + "," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_TABLE_NAME + "," + com.android.server.accounts.AccountsDb.DEBUG_TABLE_KEY + " FROM preNDb." + com.android.server.accounts.AccountsDb.TABLE_DEBUG);
            db.execSQL("INSERT INTO grants(accounts_id,auth_token_type,uid) SELECT accounts_id,auth_token_type,uid FROM preNDb.grants");
            db.execSQL("INSERT INTO meta(key,value) SELECT key,value FROM preNDb.meta");
            db.setTransactionSuccessful();
            db.endTransaction();
            db.execSQL("DETACH DATABASE preNDb");
        }
    }

    boolean deleteDeAccount(long accountId) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        return db.delete(TABLE_ACCOUNTS, new java.lang.StringBuilder().append("_id=").append(accountId).toString(), null) > 0;
    }

    long insertSharedAccount(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("name", account.name);
        values.put("type", account.type);
        return db.insert(TABLE_SHARED_ACCOUNTS, "name", values);
    }

    boolean deleteSharedAccount(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        return db.delete(TABLE_SHARED_ACCOUNTS, "name=? AND type=?", new java.lang.String[]{account.name, account.type}) > 0;
    }

    int renameSharedAccount(android.accounts.Account account, java.lang.String newName) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("name", newName);
        return db.update(TABLE_SHARED_ACCOUNTS, values, "name=? AND type=?", new java.lang.String[]{account.name, account.type});
    }

    java.util.List<android.accounts.Account> getSharedAccounts() {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        java.util.ArrayList<android.accounts.Account> accountList = new java.util.ArrayList<>();
        android.database.Cursor cursor = null;
        try {
            cursor = db.query(TABLE_SHARED_ACCOUNTS, new java.lang.String[]{"name", "type"}, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("name");
                int typeIndex = cursor.getColumnIndex("type");
                do {
                    accountList.add(new android.accounts.Account(cursor.getString(nameIndex), cursor.getString(typeIndex)));
                } while (cursor.moveToNext());
            }
            return accountList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    long findSharedAccountId(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        android.database.Cursor cursor = db.query(TABLE_SHARED_ACCOUNTS, new java.lang.String[]{"_id"}, "name=? AND type=?", new java.lang.String[]{account.name, account.type}, null, null, null);
        try {
            if (cursor.moveToNext()) {
                return cursor.getLong(0);
            }
            cursor.close();
            return -1L;
        } finally {
            cursor.close();
        }
    }

    long findAccountLastAuthenticatedTime(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        return android.database.DatabaseUtils.longForQuery(db, "SELECT last_password_entry_time_millis_epoch FROM accounts WHERE name=? AND type=?", new java.lang.String[]{account.name, account.type});
    }

    boolean updateAccountLastAuthenticatedTime(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(ACCOUNTS_LAST_AUTHENTICATE_TIME_EPOCH_MILLIS, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
        int rowCount = db.update(TABLE_ACCOUNTS, values, "name=? AND type=?", new java.lang.String[]{account.name, account.type});
        return rowCount > 0;
    }

    void dumpDeAccountsTable(java.io.PrintWriter pw) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        android.database.Cursor cursor = db.query(TABLE_ACCOUNTS, ACCOUNT_TYPE_COUNT_PROJECTION, null, null, "type", null, null);
        while (cursor.moveToNext()) {
            try {
                pw.println(cursor.getString(0) + "," + cursor.getString(1));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    long findDeAccountId(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        java.lang.String[] columns = {"_id"};
        java.lang.String[] selectionArgs = {account.name, account.type};
        android.database.Cursor cursor = db.query(TABLE_ACCOUNTS, columns, "name=? AND type=?", selectionArgs, null, null, null);
        try {
            if (cursor.moveToNext()) {
                long j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
                return j;
            }
            if (cursor != null) {
                cursor.close();
                return -1L;
            }
            return -1L;
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

    java.util.Map<java.lang.Long, android.accounts.Account> findAllDeAccounts() {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        java.util.LinkedHashMap<java.lang.Long, android.accounts.Account> map = new java.util.LinkedHashMap<>();
        java.lang.String[] columns = {"_id", "type", "name"};
        android.database.Cursor cursor = db.query(TABLE_ACCOUNTS, columns, null, null, null, null, "_id");
        while (cursor.moveToNext()) {
            try {
                long accountId = cursor.getLong(0);
                java.lang.String accountType = cursor.getString(1);
                java.lang.String accountName = cursor.getString(2);
                android.accounts.Account account = new android.accounts.Account(accountName, accountType);
                map.put(java.lang.Long.valueOf(accountId), account);
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
        if (cursor != null) {
            cursor.close();
        }
        return map;
    }

    java.lang.String findDeAccountPreviousName(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        java.lang.String[] columns = {ACCOUNTS_PREVIOUS_NAME};
        java.lang.String[] selectionArgs = {account.name, account.type};
        android.database.Cursor cursor = db.query(TABLE_ACCOUNTS, columns, "name=? AND type=?", selectionArgs, null, null, null);
        try {
            if (cursor.moveToNext()) {
                java.lang.String string = cursor.getString(0);
                if (cursor != null) {
                    cursor.close();
                }
                return string;
            }
            if (cursor != null) {
                cursor.close();
                return null;
            }
            return null;
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

    long insertDeAccount(android.accounts.Account account, long accountId) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("_id", java.lang.Long.valueOf(accountId));
        values.put("name", account.name);
        values.put("type", account.type);
        values.put(ACCOUNTS_LAST_AUTHENTICATE_TIME_EPOCH_MILLIS, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
        return db.insert(TABLE_ACCOUNTS, "name", values);
    }

    boolean renameDeAccount(long accountId, java.lang.String newName, java.lang.String previousName) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("name", newName);
        values.put(ACCOUNTS_PREVIOUS_NAME, previousName);
        java.lang.String[] argsAccountId = {java.lang.String.valueOf(accountId)};
        return db.update(TABLE_ACCOUNTS, values, "_id=?", argsAccountId) > 0;
    }

    boolean deleteGrantsByAccountIdAuthTokenTypeAndUid(long accountId, java.lang.String authTokenType, long uid) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        return db.delete(TABLE_GRANTS, "accounts_id=? AND auth_token_type=? AND uid=?", new java.lang.String[]{java.lang.String.valueOf(accountId), authTokenType, java.lang.String.valueOf(uid)}) > 0;
    }

    java.util.List<java.lang.Integer> findAllUidGrants() {
        java.util.List<java.lang.Integer> result = new java.util.ArrayList<>();
        try {
            android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
            android.database.Cursor cursor = db.query(TABLE_GRANTS, new java.lang.String[]{"uid"}, null, null, "uid", null, null);
            while (cursor.moveToNext()) {
                try {
                    int uid = cursor.getInt(0);
                    result.add(java.lang.Integer.valueOf(uid));
                } finally {
                    cursor.close();
                }
            }
            return result;
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Log.e(TAG, "Fail to open grants table " + e);
            return result;
        }
    }

    long findMatchingGrantsCount(int uid, java.lang.String authTokenType, android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        java.lang.String[] args = {java.lang.String.valueOf(uid), authTokenType, account.name, account.type};
        return android.database.DatabaseUtils.longForQuery(db, COUNT_OF_MATCHING_GRANTS, args);
    }

    long findMatchingGrantsCountAnyToken(int uid, android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        java.lang.String[] args = {java.lang.String.valueOf(uid), account.name, account.type};
        return android.database.DatabaseUtils.longForQuery(db, COUNT_OF_MATCHING_GRANTS_ANY_TOKEN, args);
    }

    long insertGrant(long accountId, java.lang.String authTokenType, int uid) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("accounts_id", java.lang.Long.valueOf(accountId));
        values.put(GRANTS_AUTH_TOKEN_TYPE, authTokenType);
        values.put("uid", java.lang.Integer.valueOf(uid));
        return db.insert(TABLE_GRANTS, "accounts_id", values);
    }

    boolean deleteGrantsByUid(int uid) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        return db.delete(TABLE_GRANTS, "uid=?", new java.lang.String[]{java.lang.Integer.toString(uid)}) > 0;
    }

    boolean setAccountVisibility(long accountId, java.lang.String packageName, int visibility) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("accounts_id", java.lang.String.valueOf(accountId));
        values.put(VISIBILITY_PACKAGE, packageName);
        values.put("value", java.lang.String.valueOf(visibility));
        return db.replace(TABLE_VISIBILITY, "value", values) != -1;
    }

    java.lang.Integer findAccountVisibility(android.accounts.Account account, java.lang.String packageName) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        android.database.Cursor cursor = db.query(TABLE_VISIBILITY, new java.lang.String[]{"value"}, "accounts_id=(select _id FROM accounts WHERE name=? AND type=?) AND _package=? ", new java.lang.String[]{account.name, account.type, packageName}, null, null, null);
        try {
            if (cursor.moveToNext()) {
                return java.lang.Integer.valueOf(cursor.getInt(0));
            }
            cursor.close();
            return null;
        } finally {
            cursor.close();
        }
    }

    java.lang.Integer findAccountVisibility(long accountId, java.lang.String packageName) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        android.database.Cursor cursor = db.query(TABLE_VISIBILITY, new java.lang.String[]{"value"}, "accounts_id=? AND _package=? ", new java.lang.String[]{java.lang.String.valueOf(accountId), packageName}, null, null, null);
        try {
            if (cursor.moveToNext()) {
                return java.lang.Integer.valueOf(cursor.getInt(0));
            }
            cursor.close();
            return null;
        } finally {
            cursor.close();
        }
    }

    android.accounts.Account findDeAccountByAccountId(long accountId) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        android.database.Cursor cursor = db.query(TABLE_ACCOUNTS, new java.lang.String[]{"name", "type"}, "_id=? ", new java.lang.String[]{java.lang.String.valueOf(accountId)}, null, null, null);
        try {
            if (cursor.moveToNext()) {
                return new android.accounts.Account(cursor.getString(0), cursor.getString(1));
            }
            cursor.close();
            return null;
        } finally {
            cursor.close();
        }
    }

    java.util.Map<java.lang.String, java.lang.Integer> findAllVisibilityValuesForAccount(android.accounts.Account account) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        java.util.Map<java.lang.String, java.lang.Integer> result = new java.util.HashMap<>();
        android.database.Cursor cursor = db.query(TABLE_VISIBILITY, new java.lang.String[]{VISIBILITY_PACKAGE, "value"}, SELECTION_ACCOUNTS_ID_BY_ACCOUNT, new java.lang.String[]{account.name, account.type}, null, null, null);
        while (cursor.moveToNext()) {
            try {
                result.put(cursor.getString(0), java.lang.Integer.valueOf(cursor.getInt(1)));
            } finally {
                cursor.close();
            }
        }
        return result;
    }

    java.util.Map<android.accounts.Account, java.util.Map<java.lang.String, java.lang.Integer>> findAllVisibilityValues() {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        java.util.Map<android.accounts.Account, java.util.Map<java.lang.String, java.lang.Integer>> result = new java.util.HashMap<>();
        android.database.Cursor cursor = db.rawQuery("SELECT visibility._package, visibility.value, accounts.name, accounts.type FROM visibility JOIN accounts ON accounts._id = visibility.accounts_id", null);
        while (cursor.moveToNext()) {
            try {
                java.lang.String packageName = cursor.getString(0);
                java.lang.Integer visibility = java.lang.Integer.valueOf(cursor.getInt(1));
                java.lang.String accountName = cursor.getString(2);
                java.lang.String accountType = cursor.getString(3);
                android.accounts.Account account = new android.accounts.Account(accountName, accountType);
                java.util.Map<java.lang.String, java.lang.Integer> accountVisibility = result.get(account);
                if (accountVisibility == null) {
                    accountVisibility = new java.util.HashMap<>();
                    result.put(account, accountVisibility);
                }
                accountVisibility.put(packageName, visibility);
            } finally {
                cursor.close();
            }
        }
        return result;
    }

    boolean deleteAccountVisibilityForPackage(java.lang.String packageName) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        return db.delete(TABLE_VISIBILITY, "_package=? ", new java.lang.String[]{packageName}) > 0;
    }

    long insertOrReplaceMetaAuthTypeAndUid(java.lang.String authenticatorType, int uid) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("key", META_KEY_FOR_AUTHENTICATOR_UID_FOR_TYPE_PREFIX + authenticatorType);
        values.put("value", java.lang.Integer.valueOf(uid));
        return db.insertWithOnConflict(TABLE_META, null, values, 5);
    }

    java.util.Map<java.lang.String, java.lang.Integer> findMetaAuthUid() {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        android.database.Cursor metaCursor = db.query(TABLE_META, new java.lang.String[]{"key", "value"}, SELECTION_META_BY_AUTHENTICATOR_TYPE, new java.lang.String[]{"auth_uid_for_type:%"}, null, null, "key");
        java.util.Map<java.lang.String, java.lang.Integer> map = new java.util.LinkedHashMap<>();
        while (metaCursor.moveToNext()) {
            try {
                java.lang.String type = android.text.TextUtils.split(metaCursor.getString(0), META_KEY_DELIMITER)[1];
                java.lang.String uidStr = metaCursor.getString(1);
                if (android.text.TextUtils.isEmpty(type) || android.text.TextUtils.isEmpty(uidStr)) {
                    android.util.Slog.e(TAG, "Auth type empty: " + android.text.TextUtils.isEmpty(type) + ", uid empty: " + android.text.TextUtils.isEmpty(uidStr));
                } else {
                    int uid = java.lang.Integer.parseInt(metaCursor.getString(1));
                    map.put(type, java.lang.Integer.valueOf(uid));
                }
            } finally {
                metaCursor.close();
            }
        }
        return map;
    }

    boolean deleteMetaByAuthTypeAndUid(java.lang.String type, int uid) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        return db.delete(TABLE_META, "key=? AND value=?", new java.lang.String[]{new java.lang.StringBuilder().append(META_KEY_FOR_AUTHENTICATOR_UID_FOR_TYPE_PREFIX).append(type).toString(), java.lang.String.valueOf(uid)}) > 0;
    }

    java.util.List<android.util.Pair<java.lang.String, java.lang.Integer>> findAllAccountGrants() {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(ACCOUNT_ACCESS_GRANTS, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    java.util.List<android.util.Pair<java.lang.String, java.lang.Integer>> results = new java.util.ArrayList<>();
                    do {
                        java.lang.String accountName = cursor.getString(0);
                        int uid = cursor.getInt(1);
                        results.add(android.util.Pair.create(accountName, java.lang.Integer.valueOf(uid)));
                    } while (cursor.moveToNext());
                    if (cursor != null) {
                        cursor.close();
                    }
                    return results;
                }
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
        java.util.List<android.util.Pair<java.lang.String, java.lang.Integer>> results2 = java.util.Collections.emptyList();
        if (cursor != null) {
            cursor.close();
        }
        return results2;
    }

    private static class PreNDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
        private final android.content.Context mContext;
        private final int mUserId;

        PreNDatabaseHelper(android.content.Context context, int userId, java.lang.String preNDatabaseName) {
            super(context, preNDatabaseName, (android.database.sqlite.SQLiteDatabase.CursorFactory) null, 9);
            this.mContext = context;
            this.mUserId = userId;
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            throw new java.lang.IllegalStateException("Legacy database cannot be created - only upgraded!");
        }

        private void createSharedAccountsTable(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("CREATE TABLE shared_accounts ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, type TEXT NOT NULL, UNIQUE(name,type))");
        }

        private void addLastSuccessfullAuthenticatedTimeColumn(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("ALTER TABLE accounts ADD COLUMN last_password_entry_time_millis_epoch DEFAULT 0");
        }

        private void addOldAccountNameColumn(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("ALTER TABLE accounts ADD COLUMN previous_name");
        }

        private void addDebugTable(android.database.sqlite.SQLiteDatabase db) {
            com.android.server.accounts.AccountsDb.DeDatabaseHelper.createDebugTable(db);
        }

        private void createAccountsDeletionTrigger(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL(" CREATE TRIGGER accountsDelete DELETE ON accounts BEGIN   DELETE FROM authtokens     WHERE accounts_id=OLD._id ;   DELETE FROM extras     WHERE accounts_id=OLD._id ;   DELETE FROM grants     WHERE accounts_id=OLD._id ; END");
        }

        private void createGrantsTable(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("CREATE TABLE grants (  accounts_id INTEGER NOT NULL, auth_token_type STRING NOT NULL,  uid INTEGER NOT NULL,  UNIQUE (accounts_id,auth_token_type,uid))");
        }

        static long insertMetaAuthTypeAndUid(android.database.sqlite.SQLiteDatabase db, java.lang.String authenticatorType, int uid) {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put("key", com.android.server.accounts.AccountsDb.META_KEY_FOR_AUTHENTICATOR_UID_FOR_TYPE_PREFIX + authenticatorType);
            values.put("value", java.lang.Integer.valueOf(uid));
            return db.insert(com.android.server.accounts.AccountsDb.TABLE_META, null, values);
        }

        private void populateMetaTableWithAuthTypeAndUID(android.database.sqlite.SQLiteDatabase db, java.util.Map<java.lang.String, java.lang.Integer> authTypeAndUIDMap) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : authTypeAndUIDMap.entrySet()) {
                insertMetaAuthTypeAndUid(db, entry.getKey(), entry.getValue().intValue());
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
            android.util.Log.e(com.android.server.accounts.AccountsDb.TAG, "upgrade from version " + oldVersion + " to version " + newVersion);
            if (oldVersion == 1) {
                oldVersion++;
            }
            if (oldVersion == 2) {
                createGrantsTable(db);
                db.execSQL("DROP TRIGGER accountsDelete");
                createAccountsDeletionTrigger(db);
                oldVersion++;
            }
            if (oldVersion == 3) {
                db.execSQL("UPDATE accounts SET type = 'com.google' WHERE type == 'com.google.GAIA'");
                oldVersion++;
            }
            if (oldVersion == 4) {
                createSharedAccountsTable(db);
                oldVersion++;
            }
            if (oldVersion == 5) {
                addOldAccountNameColumn(db);
                oldVersion++;
            }
            if (oldVersion == 6) {
                addLastSuccessfullAuthenticatedTimeColumn(db);
                oldVersion++;
            }
            if (oldVersion == 7) {
                addDebugTable(db);
                oldVersion++;
            }
            if (oldVersion == 8) {
                populateMetaTableWithAuthTypeAndUID(db, com.android.server.accounts.AccountManagerService.getAuthenticatorTypeAndUIDForUser(this.mContext, this.mUserId));
                oldVersion++;
            }
            if (oldVersion != newVersion) {
                android.util.Log.e(com.android.server.accounts.AccountsDb.TAG, "failed to upgrade version " + oldVersion + " to version " + newVersion);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(android.database.sqlite.SQLiteDatabase db) {
            if (android.util.Log.isLoggable(com.android.server.accounts.AccountsDb.TAG, 2)) {
                android.util.Log.v(com.android.server.accounts.AccountsDb.TAG, "opened database accounts.db");
            }
        }
    }

    java.util.List<android.accounts.Account> findCeAccountsNotInDe() {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabaseUserIsUnlocked();
        android.database.Cursor cursor = db.rawQuery("SELECT name,type FROM ceDb.accounts WHERE NOT EXISTS  (SELECT _id FROM accounts WHERE _id=ceDb.accounts._id )", null);
        try {
            java.util.List<android.accounts.Account> accounts = new java.util.ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                java.lang.String accountName = cursor.getString(0);
                java.lang.String accountType = cursor.getString(1);
                accounts.add(new android.accounts.Account(accountName, accountType));
            }
            return accounts;
        } finally {
            cursor.close();
        }
    }

    boolean deleteCeAccount(long accountId) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        return db.delete(CE_TABLE_ACCOUNTS, new java.lang.StringBuilder().append("_id=").append(accountId).toString(), null) > 0;
    }

    boolean isCeDatabaseAttached() {
        return this.mDeDatabase.mCeAttached;
    }

    void beginTransaction() {
        this.mDeDatabase.getWritableDatabase().beginTransaction();
    }

    void setTransactionSuccessful() {
        this.mDeDatabase.getWritableDatabase().setTransactionSuccessful();
    }

    void endTransaction() {
        this.mDeDatabase.getWritableDatabase().endTransaction();
    }

    void attachCeDatabase(java.io.File ceDbFile) {
        com.android.server.accounts.AccountsDb.CeDatabaseHelper.create(this.mContext, this.mPreNDatabaseFile, ceDbFile);
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        db.execSQL("ATTACH DATABASE '" + ceDbFile.getPath() + "' AS ceDb");
        this.mDeDatabase.mCeAttached = true;
    }

    long calculateDebugTableInsertionPoint() {
        try {
            android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
            java.lang.String queryCountDebugDbRows = "SELECT COUNT(*) FROM " + TABLE_DEBUG;
            int size = (int) android.database.DatabaseUtils.longForQuery(db, queryCountDebugDbRows, null);
            if (size < 64) {
                return size;
            }
            java.lang.String queryCountDebugDbRows2 = "SELECT " + DEBUG_TABLE_KEY + " FROM " + TABLE_DEBUG + " ORDER BY " + DEBUG_TABLE_TIMESTAMP + "," + DEBUG_TABLE_KEY + " LIMIT 1";
            return android.database.DatabaseUtils.longForQuery(db, queryCountDebugDbRows2, null);
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Log.e(TAG, "Failed to open debug table" + e);
            return -1L;
        }
    }

    android.database.sqlite.SQLiteStatement compileSqlStatementForLogging() {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getWritableDatabase();
        java.lang.String sql = "INSERT OR REPLACE INTO " + TABLE_DEBUG + " VALUES (?,?,?,?,?,?)";
        return db.compileStatement(sql);
    }

    android.database.sqlite.SQLiteStatement getStatementForLogging() {
        if (this.mDebugStatementForLogging != null) {
            return this.mDebugStatementForLogging;
        }
        try {
            this.mDebugStatementForLogging = compileSqlStatementForLogging();
            return this.mDebugStatementForLogging;
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Log.e(TAG, "Failed to open debug table" + e);
            return null;
        }
    }

    void closeDebugStatement() {
        synchronized (this.mDebugStatementLock) {
            if (this.mDebugStatementForLogging != null) {
                this.mDebugStatementForLogging.close();
                this.mDebugStatementForLogging = null;
            }
        }
    }

    long reserveDebugDbInsertionPoint() {
        if (this.mDebugDbInsertionPoint == -1) {
            this.mDebugDbInsertionPoint = calculateDebugTableInsertionPoint();
            return this.mDebugDbInsertionPoint;
        }
        this.mDebugDbInsertionPoint = (this.mDebugDbInsertionPoint + 1) % 64;
        return this.mDebugDbInsertionPoint;
    }

    void dumpDebugTable(java.io.PrintWriter pw) {
        android.database.sqlite.SQLiteDatabase db = this.mDeDatabase.getReadableDatabase();
        android.database.Cursor cursor = db.query(TABLE_DEBUG, null, null, null, null, null, DEBUG_TABLE_TIMESTAMP);
        pw.println("AccountId, Action_Type, timestamp, UID, TableName, Key");
        pw.println("Accounts History");
        while (cursor.moveToNext()) {
            try {
                pw.println(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3) + "," + cursor.getString(4) + "," + cursor.getString(5));
            } finally {
                cursor.close();
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mDeDatabase.close();
    }

    static void deleteDbFileWarnIfFailed(java.io.File dbFile) {
        if (!android.database.sqlite.SQLiteDatabase.deleteDatabase(dbFile)) {
            android.util.Log.w(TAG, "Database at " + dbFile + " was not deleted successfully");
        }
    }

    public static com.android.server.accounts.AccountsDb create(android.content.Context context, int userId, java.io.File preNDatabaseFile, java.io.File deDatabaseFile) {
        boolean newDbExists = deDatabaseFile.exists();
        com.android.server.accounts.AccountsDb.DeDatabaseHelper deDatabaseHelper = new com.android.server.accounts.AccountsDb.DeDatabaseHelper(context, userId, deDatabaseFile.getPath());
        if (!newDbExists && preNDatabaseFile.exists()) {
            com.android.server.accounts.AccountsDb.PreNDatabaseHelper preNDatabaseHelper = new com.android.server.accounts.AccountsDb.PreNDatabaseHelper(context, userId, preNDatabaseFile.getPath());
            preNDatabaseHelper.getWritableDatabase();
            preNDatabaseHelper.close();
            deDatabaseHelper.migratePreNDbToDe(preNDatabaseFile);
        }
        return new com.android.server.accounts.AccountsDb(deDatabaseHelper, context, preNDatabaseFile);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetDatabase(android.database.sqlite.SQLiteDatabase db) {
        android.database.Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table'", null);
        while (c.moveToNext()) {
            try {
                java.lang.String name = c.getString(0);
                if (!"android_metadata".equals(name) && !"sqlite_sequence".equals(name)) {
                    db.execSQL("DROP TABLE IF EXISTS " + name);
                }
            } finally {
            }
        }
        if (c != null) {
            c.close();
        }
        c = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='trigger'", null);
        while (c.moveToNext()) {
            try {
                db.execSQL("DROP TRIGGER IF EXISTS " + c.getString(0));
            } finally {
            }
        }
        if (c != null) {
            c.close();
        }
    }
}
