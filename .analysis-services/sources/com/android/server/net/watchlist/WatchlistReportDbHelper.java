package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
class WatchlistReportDbHelper extends android.database.sqlite.SQLiteOpenHelper {
    private static final java.lang.String CREATE_TABLE_MODEL = "CREATE TABLE records(app_digest BLOB,cnc_domain TEXT,timestamp INTEGER DEFAULT 0 )";
    private static final java.lang.String[] DIGEST_DOMAIN_PROJECTION = {"app_digest", "cnc_domain"};
    private static final int IDLE_CONNECTION_TIMEOUT_MS = 30000;
    private static final int INDEX_CNC_DOMAIN = 1;
    private static final int INDEX_DIGEST = 0;
    private static final int INDEX_TIMESTAMP = 2;
    private static final java.lang.String NAME = "watchlist_report.db";
    private static final java.lang.String TAG = "WatchlistReportDbHelper";
    private static final int VERSION = 2;
    private static com.android.server.net.watchlist.WatchlistReportDbHelper sInstance;

    private static class WhiteListReportContract {
        private static final java.lang.String APP_DIGEST = "app_digest";
        private static final java.lang.String CNC_DOMAIN = "cnc_domain";
        private static final java.lang.String TABLE = "records";
        private static final java.lang.String TIMESTAMP = "timestamp";

        private WhiteListReportContract() {
        }
    }

    public static class AggregatedResult {
        final java.util.HashMap<java.lang.String, java.lang.String> appDigestCNCList;
        final java.util.Set<java.lang.String> appDigestList;
        final java.lang.String cncDomainVisited;

        public AggregatedResult(java.util.Set<java.lang.String> appDigestList, java.lang.String cncDomainVisited, java.util.HashMap<java.lang.String, java.lang.String> appDigestCNCList) {
            this.appDigestList = appDigestList;
            this.cncDomainVisited = cncDomainVisited;
            this.appDigestCNCList = appDigestCNCList;
        }
    }

    static java.io.File getSystemWatchlistDbFile() {
        return new java.io.File(android.os.Environment.getDataSystemDirectory(), NAME);
    }

    private WatchlistReportDbHelper(android.content.Context context) {
        super(context, getSystemWatchlistDbFile().getAbsolutePath(), (android.database.sqlite.SQLiteDatabase.CursorFactory) null, 2);
        setIdleConnectionTimeout(30000L);
    }

    public static synchronized com.android.server.net.watchlist.WatchlistReportDbHelper getInstance(android.content.Context context) {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = new com.android.server.net.watchlist.WatchlistReportDbHelper(context);
        return sInstance;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MODEL);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS records");
        onCreate(db);
    }

    public boolean insertNewRecord(byte[] appDigest, java.lang.String cncDomain, long timestamp) {
        try {
            android.database.sqlite.SQLiteDatabase db = getWritableDatabase();
            android.content.ContentValues values = new android.content.ContentValues();
            values.put("app_digest", appDigest);
            values.put("cnc_domain", cncDomain);
            values.put(com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP, java.lang.Long.valueOf(timestamp));
            return db.insert("records", null, values) != -1;
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Slog.e(TAG, "Error opening the database to insert a new record", e);
            return false;
        }
    }

    public com.android.server.net.watchlist.WatchlistReportDbHelper.AggregatedResult getAggregatedRecords(long untilTimestamp) {
        try {
            android.database.sqlite.SQLiteDatabase db = getReadableDatabase();
            android.database.Cursor c = null;
            try {
                c = db.query(true, "records", DIGEST_DOMAIN_PROJECTION, "timestamp < ?", new java.lang.String[]{java.lang.Long.toString(untilTimestamp)}, null, null, null, null);
                if (c != null && c.getCount() > 0) {
                    java.util.HashSet<java.lang.String> appDigestList = new java.util.HashSet<>();
                    java.util.HashMap<java.lang.String, java.lang.String> appDigestCNCList = new java.util.HashMap<>();
                    java.lang.String cncDomainVisited = null;
                    while (c.moveToNext()) {
                        java.lang.String digestHexStr = com.android.internal.util.HexDump.toHexString(c.getBlob(0));
                        java.lang.String cncDomain = c.getString(1);
                        appDigestList.add(digestHexStr);
                        if (cncDomainVisited != null) {
                            cncDomainVisited = cncDomain;
                        }
                        appDigestCNCList.put(digestHexStr, cncDomain);
                    }
                    return new com.android.server.net.watchlist.WatchlistReportDbHelper.AggregatedResult(appDigestList, cncDomainVisited, appDigestCNCList);
                }
                if (c != null) {
                    c.close();
                }
                return null;
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Slog.e(TAG, "Error opening the database", e);
            return null;
        }
    }

    public boolean cleanup(long untilTimestamp) {
        try {
            android.database.sqlite.SQLiteDatabase db = getWritableDatabase();
            java.lang.String clause = "timestamp< " + untilTimestamp;
            return db.delete("records", clause, null) != 0;
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Slog.e(TAG, "Error opening the database to cleanup", e);
            return false;
        }
    }
}
