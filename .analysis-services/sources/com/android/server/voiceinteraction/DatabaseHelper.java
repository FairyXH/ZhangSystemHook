package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
public class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper implements com.android.server.voiceinteraction.IEnrolledModelDb {
    private static final java.lang.String CREATE_TABLE_SOUND_MODEL = "CREATE TABLE sound_model(model_uuid TEXT,vendor_uuid TEXT,keyphrase_id INTEGER,type INTEGER,data BLOB,recognition_modes INTEGER,locale TEXT,hint_text TEXT,users TEXT,model_version INTEGER,PRIMARY KEY (keyphrase_id,locale,users))";
    static final boolean DBG = false;
    private static final java.lang.String NAME = "sound_model.db";
    static final java.lang.String TAG = "SoundModelDBHelper";
    private static final int VERSION = 7;

    public interface SoundModelContract {
        public static final java.lang.String KEY_DATA = "data";
        public static final java.lang.String KEY_HINT_TEXT = "hint_text";
        public static final java.lang.String KEY_KEYPHRASE_ID = "keyphrase_id";
        public static final java.lang.String KEY_LOCALE = "locale";
        public static final java.lang.String KEY_MODEL_UUID = "model_uuid";
        public static final java.lang.String KEY_MODEL_VERSION = "model_version";
        public static final java.lang.String KEY_RECOGNITION_MODES = "recognition_modes";
        public static final java.lang.String KEY_TYPE = "type";
        public static final java.lang.String KEY_USERS = "users";
        public static final java.lang.String KEY_VENDOR_UUID = "vendor_uuid";
        public static final java.lang.String TABLE = "sound_model";
    }

    public DatabaseHelper(android.content.Context context) {
        super(context, NAME, (android.database.sqlite.SQLiteDatabase.CursorFactory) null, 7);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SOUND_MODEL);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("DROP TABLE IF EXISTS sound_model");
            onCreate(db);
        } else if (oldVersion == 4) {
            android.util.Slog.d(TAG, "Adding vendor UUID column");
            db.execSQL("ALTER TABLE sound_model ADD COLUMN vendor_uuid TEXT");
            oldVersion++;
        }
        if (oldVersion == 5) {
            android.database.Cursor c = db.rawQuery("SELECT * FROM sound_model", null);
            java.util.List<com.android.server.voiceinteraction.DatabaseHelper.SoundModelRecord> old_records = new java.util.ArrayList<>();
            try {
                if (c.moveToFirst()) {
                    do {
                        try {
                            old_records.add(new com.android.server.voiceinteraction.DatabaseHelper.SoundModelRecord(5, c));
                        } catch (java.lang.Exception e) {
                            android.util.Slog.e(TAG, "Failed to extract V5 record", e);
                        }
                    } while (c.moveToNext());
                }
                c.close();
                db.execSQL("DROP TABLE IF EXISTS sound_model");
                onCreate(db);
                for (com.android.server.voiceinteraction.DatabaseHelper.SoundModelRecord record : old_records) {
                    if (record.ifViolatesV6PrimaryKeyIsFirstOfAnyDuplicates(old_records)) {
                        try {
                            long return_value = record.writeToDatabase(6, db);
                            if (return_value == -1) {
                                android.util.Slog.e(TAG, "Database write failed " + record.modelUuid + ": " + return_value);
                            }
                        } catch (java.lang.Exception e2) {
                            android.util.Slog.e(TAG, "Failed to update V6 record " + record.modelUuid, e2);
                        }
                    }
                }
                oldVersion++;
            } catch (java.lang.Throwable th) {
                c.close();
                throw th;
            }
        }
        if (oldVersion == 6) {
            android.util.Slog.d(TAG, "Adding model version column");
            db.execSQL("ALTER TABLE sound_model ADD COLUMN model_version INTEGER DEFAULT -1");
            int i = oldVersion + 1;
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public boolean updateKeyphraseSoundModel(android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel soundModel) {
        synchronized (this) {
            android.database.sqlite.SQLiteDatabase db = getWritableDatabase();
            android.content.ContentValues values = new android.content.ContentValues();
            values.put("model_uuid", soundModel.getUuid().toString());
            if (soundModel.getVendorUuid() != null) {
                values.put("vendor_uuid", soundModel.getVendorUuid().toString());
            }
            values.put("type", (java.lang.Integer) 0);
            values.put("data", soundModel.getData());
            values.put("model_version", java.lang.Integer.valueOf(soundModel.getVersion()));
            if (soundModel.getKeyphrases() == null || soundModel.getKeyphrases().length != 1) {
                return false;
            }
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_KEYPHRASE_ID, java.lang.Integer.valueOf(soundModel.getKeyphrases()[0].getId()));
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_RECOGNITION_MODES, java.lang.Integer.valueOf(soundModel.getKeyphrases()[0].getRecognitionModes()));
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_USERS, getCommaSeparatedString(soundModel.getKeyphrases()[0].getUsers()));
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE, soundModel.getKeyphrases()[0].getLocale().toLanguageTag());
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_HINT_TEXT, soundModel.getKeyphrases()[0].getText());
            try {
                return db.insertWithOnConflict(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.TABLE, null, values, 5) != -1;
            } finally {
                db.close();
            }
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public boolean deleteKeyphraseSoundModel(int keyphraseId, int userHandle, java.lang.String bcp47Locale) {
        java.lang.String bcp47Locale2 = java.util.Locale.forLanguageTag(bcp47Locale).toLanguageTag();
        synchronized (this) {
            android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel soundModel = getKeyphraseSoundModel(keyphraseId, userHandle, bcp47Locale2);
            if (soundModel == null) {
                return false;
            }
            android.database.sqlite.SQLiteDatabase db = getWritableDatabase();
            java.lang.String soundModelClause = "model_uuid='" + soundModel.getUuid().toString() + "'";
            try {
                return db.delete(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.TABLE, soundModelClause, null) != 0;
            } finally {
                db.close();
            }
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int keyphraseId, int userHandle, java.lang.String bcp47Locale) {
        android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel validKeyphraseSoundModelForUser;
        java.lang.String bcp47Locale2 = java.util.Locale.forLanguageTag(bcp47Locale).toLanguageTag();
        synchronized (this) {
            java.lang.String selectQuery = "SELECT  * FROM sound_model WHERE keyphrase_id= '" + keyphraseId + "' AND " + com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE + "='" + bcp47Locale2 + "'";
            validKeyphraseSoundModelForUser = getValidKeyphraseSoundModelForUser(selectQuery, userHandle);
        }
        return validKeyphraseSoundModelForUser;
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(java.lang.String keyphrase, int userHandle, java.lang.String bcp47Locale) {
        android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel validKeyphraseSoundModelForUser;
        java.lang.String bcp47Locale2 = java.util.Locale.forLanguageTag(bcp47Locale).toLanguageTag();
        synchronized (this) {
            java.lang.String selectQuery = "SELECT  * FROM sound_model WHERE hint_text= '" + keyphrase + "' AND " + com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE + "='" + bcp47Locale2 + "'";
            validKeyphraseSoundModelForUser = getValidKeyphraseSoundModelForUser(selectQuery, userHandle);
        }
        return validKeyphraseSoundModelForUser;
    }

    private android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel getValidKeyphraseSoundModelForUser(java.lang.String selectQuery, int userHandle) throws java.lang.Throwable {
        android.database.sqlite.SQLiteDatabase db = getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);
        try {
        } catch (java.lang.Throwable th) {
            th = th;
        }
        if (c.moveToFirst()) {
            do {
                int type = c.getInt(c.getColumnIndex("type"));
                if (type == 0) {
                    java.lang.String modelUuid = c.getString(c.getColumnIndex("model_uuid"));
                    if (modelUuid == null) {
                        android.util.Slog.w(TAG, "Ignoring SoundModel since it doesn't specify an ID");
                    } else {
                        int vendorUuidColumn = c.getColumnIndex("vendor_uuid");
                        java.lang.String vendorUuidString = vendorUuidColumn != -1 ? c.getString(vendorUuidColumn) : null;
                        int keyphraseId = c.getInt(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_KEYPHRASE_ID));
                        byte[] data = c.getBlob(c.getColumnIndex("data"));
                        int recognitionModes = c.getInt(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_RECOGNITION_MODES));
                        int[] users = getArrayForCommaSeparatedString(c.getString(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_USERS)));
                        java.util.Locale modelLocale = java.util.Locale.forLanguageTag(c.getString(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE)));
                        java.lang.String text = c.getString(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_HINT_TEXT));
                        int version = c.getInt(c.getColumnIndex("model_version"));
                        if (users != null) {
                            boolean isAvailableForCurrentUser = false;
                            int length = users.length;
                            int i = 0;
                            while (true) {
                                if (i >= length) {
                                    break;
                                }
                                int user = users[i];
                                if (userHandle == user) {
                                    isAvailableForCurrentUser = true;
                                    break;
                                }
                                i++;
                            }
                            if (isAvailableForCurrentUser) {
                                android.hardware.soundtrigger.SoundTrigger.Keyphrase[] keyphrases = {new android.hardware.soundtrigger.SoundTrigger.Keyphrase(keyphraseId, recognitionModes, modelLocale, text, users)};
                                java.util.UUID vendorUuid = vendorUuidString != null ? java.util.UUID.fromString(vendorUuidString) : null;
                                android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel model = new android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel(java.util.UUID.fromString(modelUuid), vendorUuid, data, keyphrases, version);
                                c.close();
                                db.close();
                                return model;
                            }
                            th = th;
                            c.close();
                            db.close();
                            throw th;
                        }
                        android.util.Slog.w(TAG, "Ignoring SoundModel since it doesn't specify users");
                    }
                }
                try {
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return null;
    }

    private static java.lang.String getCommaSeparatedString(int[] users) {
        if (users == null) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < users.length; i++) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(users[i]);
        }
        return sb.toString();
    }

    private static int[] getArrayForCommaSeparatedString(java.lang.String text) {
        if (android.text.TextUtils.isEmpty(text)) {
            return null;
        }
        java.lang.String[] usersStr = text.split(",");
        int[] users = new int[usersStr.length];
        for (int i = 0; i < usersStr.length; i++) {
            users[i] = java.lang.Integer.parseInt(usersStr[i]);
        }
        return users;
    }

    private static class SoundModelRecord {
        public final byte[] data;
        public final java.lang.String hintText;
        public final int keyphraseId;
        public final java.lang.String locale;
        public final java.lang.String modelUuid;
        public final int recognitionModes;
        public final int type;
        public final java.lang.String users;
        public final java.lang.String vendorUuid;

        public SoundModelRecord(int version, android.database.Cursor c) {
            this.modelUuid = c.getString(c.getColumnIndex("model_uuid"));
            if (version >= 5) {
                this.vendorUuid = c.getString(c.getColumnIndex("vendor_uuid"));
            } else {
                this.vendorUuid = null;
            }
            this.keyphraseId = c.getInt(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_KEYPHRASE_ID));
            this.type = c.getInt(c.getColumnIndex("type"));
            this.data = c.getBlob(c.getColumnIndex("data"));
            this.recognitionModes = c.getInt(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_RECOGNITION_MODES));
            this.locale = c.getString(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE));
            this.hintText = c.getString(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_HINT_TEXT));
            this.users = c.getString(c.getColumnIndex(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_USERS));
        }

        private boolean V6PrimaryKeyMatches(com.android.server.voiceinteraction.DatabaseHelper.SoundModelRecord record) {
            return this.keyphraseId == record.keyphraseId && stringComparisonHelper(this.locale, record.locale) && stringComparisonHelper(this.users, record.users);
        }

        public boolean ifViolatesV6PrimaryKeyIsFirstOfAnyDuplicates(java.util.List<com.android.server.voiceinteraction.DatabaseHelper.SoundModelRecord> records) {
            for (com.android.server.voiceinteraction.DatabaseHelper.SoundModelRecord record : records) {
                if (this != record && V6PrimaryKeyMatches(record) && !java.util.Arrays.equals(this.data, record.data)) {
                    return false;
                }
            }
            java.util.Iterator<com.android.server.voiceinteraction.DatabaseHelper.SoundModelRecord> it = records.iterator();
            while (it.hasNext()) {
                com.android.server.voiceinteraction.DatabaseHelper.SoundModelRecord record2 = it.next();
                if (V6PrimaryKeyMatches(record2)) {
                    return this == record2;
                }
            }
            return true;
        }

        public long writeToDatabase(int version, android.database.sqlite.SQLiteDatabase db) {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put("model_uuid", this.modelUuid);
            if (version >= 5) {
                values.put("vendor_uuid", this.vendorUuid);
            }
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_KEYPHRASE_ID, java.lang.Integer.valueOf(this.keyphraseId));
            values.put("type", java.lang.Integer.valueOf(this.type));
            values.put("data", this.data);
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_RECOGNITION_MODES, java.lang.Integer.valueOf(this.recognitionModes));
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE, this.locale);
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_HINT_TEXT, this.hintText);
            values.put(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_USERS, this.users);
            return db.insertWithOnConflict(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.TABLE, null, values, 5);
        }

        private static boolean stringComparisonHelper(java.lang.String a, java.lang.String b) {
            if (a != null) {
                return a.equals(b);
            }
            return a == b;
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public void dump(java.io.PrintWriter pw) {
        synchronized (this) {
            android.database.sqlite.SQLiteDatabase db = getReadableDatabase();
            android.database.Cursor c = db.rawQuery("SELECT  * FROM sound_model", null);
            try {
                pw.println("  Enrolled KeyphraseSoundModels:");
                if (c.moveToFirst()) {
                    java.lang.String[] columnNames = c.getColumnNames();
                    do {
                        for (java.lang.String name : columnNames) {
                            int colNameIndex = c.getColumnIndex(name);
                            int type = c.getType(colNameIndex);
                            switch (type) {
                                case 0:
                                    pw.printf("    %s: null\n", name);
                                    break;
                                case 1:
                                    pw.printf("    %s: %d\n", name, java.lang.Integer.valueOf(c.getInt(colNameIndex)));
                                    break;
                                case 2:
                                    pw.printf("    %s: %f\n", name, java.lang.Float.valueOf(c.getFloat(colNameIndex)));
                                    break;
                                case 3:
                                    pw.printf("    %s: %s\n", name, c.getString(colNameIndex));
                                    break;
                                case 4:
                                    pw.printf("    %s: data blob\n", name);
                                    break;
                            }
                        }
                        pw.println();
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
                db.close();
            }
        }
    }
}
