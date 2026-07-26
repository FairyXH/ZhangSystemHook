package com.android.server.soundtrigger;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerDbHelper extends android.database.sqlite.SQLiteOpenHelper {
    private static final java.lang.String CREATE_TABLE_ST_SOUND_MODEL = "CREATE TABLE st_sound_model(model_uuid TEXT PRIMARY KEY,vendor_uuid TEXT,data BLOB,model_version INTEGER )";
    static final boolean DBG = false;
    private static final java.lang.String NAME = "st_sound_model.db";
    static final java.lang.String TAG = "SoundTriggerDbHelper";
    private static final int VERSION = 2;

    public interface GenericSoundModelContract {
        public static final java.lang.String KEY_DATA = "data";
        public static final java.lang.String KEY_MODEL_UUID = "model_uuid";
        public static final java.lang.String KEY_MODEL_VERSION = "model_version";
        public static final java.lang.String KEY_VENDOR_UUID = "vendor_uuid";
        public static final java.lang.String TABLE = "st_sound_model";
    }

    public SoundTriggerDbHelper(android.content.Context context) {
        super(context, NAME, (android.database.sqlite.SQLiteDatabase.CursorFactory) null, 2);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ST_SOUND_MODEL);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            android.util.Slog.d(TAG, "Adding model version column");
            db.execSQL("ALTER TABLE st_sound_model ADD COLUMN model_version INTEGER DEFAULT -1");
            int i = oldVersion + 1;
        }
    }

    public boolean updateGenericSoundModel(android.hardware.soundtrigger.SoundTrigger.GenericSoundModel soundModel) {
        boolean z;
        synchronized (this) {
            android.database.sqlite.SQLiteDatabase db = getWritableDatabase();
            android.content.ContentValues values = new android.content.ContentValues();
            values.put("model_uuid", soundModel.getUuid().toString());
            values.put("vendor_uuid", soundModel.getVendorUuid().toString());
            values.put("data", soundModel.getData());
            values.put("model_version", java.lang.Integer.valueOf(soundModel.getVersion()));
            try {
                z = db.insertWithOnConflict(com.android.server.soundtrigger.SoundTriggerDbHelper.GenericSoundModelContract.TABLE, null, values, 5) != -1;
            } finally {
                db.close();
            }
        }
        return z;
    }

    public android.hardware.soundtrigger.SoundTrigger.GenericSoundModel getGenericSoundModel(java.util.UUID model_uuid) {
        synchronized (this) {
            java.lang.String selectQuery = "SELECT  * FROM st_sound_model WHERE model_uuid= '" + model_uuid + "'";
            android.database.sqlite.SQLiteDatabase db = getReadableDatabase();
            android.database.Cursor c = db.rawQuery(selectQuery, null);
            try {
                if (!c.moveToFirst()) {
                    return null;
                }
                byte[] data = c.getBlob(c.getColumnIndex("data"));
                java.lang.String vendor_uuid = c.getString(c.getColumnIndex("vendor_uuid"));
                int version = c.getInt(c.getColumnIndex("model_version"));
                return new android.hardware.soundtrigger.SoundTrigger.GenericSoundModel(model_uuid, java.util.UUID.fromString(vendor_uuid), data, version);
            } finally {
                c.close();
                db.close();
            }
        }
    }

    public boolean deleteGenericSoundModel(java.util.UUID model_uuid) {
        synchronized (this) {
            android.hardware.soundtrigger.SoundTrigger.GenericSoundModel soundModel = getGenericSoundModel(model_uuid);
            if (soundModel == null) {
                return false;
            }
            android.database.sqlite.SQLiteDatabase db = getWritableDatabase();
            java.lang.String soundModelClause = "model_uuid='" + soundModel.getUuid().toString() + "'";
            try {
                return db.delete(com.android.server.soundtrigger.SoundTriggerDbHelper.GenericSoundModelContract.TABLE, soundModelClause, null) != 0;
            } finally {
                db.close();
            }
        }
    }

    public void dump(java.io.PrintWriter pw) {
        synchronized (this) {
            android.database.sqlite.SQLiteDatabase db = getReadableDatabase();
            android.database.Cursor c = db.rawQuery("SELECT  * FROM st_sound_model", null);
            try {
                pw.println("  Enrolled GenericSoundModels:");
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
