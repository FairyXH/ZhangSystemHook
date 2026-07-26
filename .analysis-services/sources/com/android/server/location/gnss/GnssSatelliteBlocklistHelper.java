package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
class GnssSatelliteBlocklistHelper {
    private static final java.lang.String BLOCKLIST_DELIMITER = ",";
    private static final java.lang.String TAG = "GnssBlocklistHelper";
    private final com.android.server.location.gnss.GnssSatelliteBlocklistHelper.GnssSatelliteBlocklistCallback mCallback;
    private final android.content.Context mContext;

    interface GnssSatelliteBlocklistCallback {
        void onUpdateSatelliteBlocklist(int[] iArr, int[] iArr2);
    }

    GnssSatelliteBlocklistHelper(android.content.Context context, android.os.Looper looper, com.android.server.location.gnss.GnssSatelliteBlocklistHelper.GnssSatelliteBlocklistCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        android.database.ContentObserver contentObserver = new android.database.ContentObserver(new android.os.Handler(looper)) { // from class: com.android.server.location.gnss.GnssSatelliteBlocklistHelper.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.location.gnss.GnssSatelliteBlocklistHelper.this.updateSatelliteBlocklist();
            }
        };
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("gnss_satellite_blocklist"), true, contentObserver, -1);
    }

    void updateSatelliteBlocklist() {
        android.content.ContentResolver resolver = this.mContext.getContentResolver();
        java.lang.String blocklist = android.provider.Settings.Global.getString(resolver, "gnss_satellite_blocklist");
        if (blocklist == null) {
            blocklist = "";
        }
        android.util.Log.i(TAG, java.lang.String.format("Update GNSS satellite blocklist: %s", blocklist));
        try {
            java.util.List<java.lang.Integer> blocklistValues = parseSatelliteBlocklist(blocklist);
            if (blocklistValues.size() % 2 != 0) {
                android.util.Log.e(TAG, "blocklist string has odd number of values.Aborting updateSatelliteBlocklist");
                return;
            }
            int length = blocklistValues.size() / 2;
            int[] constellations = new int[length];
            int[] svids = new int[length];
            for (int i = 0; i < length; i++) {
                constellations[i] = blocklistValues.get(i * 2).intValue();
                svids[i] = blocklistValues.get((i * 2) + 1).intValue();
            }
            this.mCallback.onUpdateSatelliteBlocklist(constellations, svids);
        } catch (java.lang.NumberFormatException e) {
            android.util.Log.e(TAG, "Exception thrown when parsing blocklist string.", e);
        }
    }

    static java.util.List<java.lang.Integer> parseSatelliteBlocklist(java.lang.String blocklist) throws java.lang.NumberFormatException {
        java.lang.String[] strings = blocklist.split(BLOCKLIST_DELIMITER);
        java.util.List<java.lang.Integer> parsed = new java.util.ArrayList<>(strings.length);
        for (java.lang.String str : strings) {
            java.lang.String string = str.trim();
            if (!"".equals(string)) {
                int value = java.lang.Integer.parseInt(string);
                if (value < 0) {
                    throw new java.lang.NumberFormatException("Negative value is invalid.");
                }
                parsed.add(java.lang.Integer.valueOf(value));
            }
        }
        return parsed;
    }
}
