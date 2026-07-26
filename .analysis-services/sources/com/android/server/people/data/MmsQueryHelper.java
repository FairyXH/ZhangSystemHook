package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class MmsQueryHelper {
    private static final long MILLIS_PER_SECONDS = 1000;
    private static final android.util.SparseIntArray MSG_BOX_TO_EVENT_TYPE = new android.util.SparseIntArray();
    private static final java.lang.String TAG = "MmsQueryHelper";
    private final android.content.Context mContext;
    private java.lang.String mCurrentCountryIso;
    private final java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> mEventConsumer;
    private long mLastMessageTimestamp;

    static {
        MSG_BOX_TO_EVENT_TYPE.put(1, 9);
        MSG_BOX_TO_EVENT_TYPE.put(2, 8);
    }

    MmsQueryHelper(android.content.Context context, java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> eventConsumer) {
        this.mContext = context;
        this.mEventConsumer = eventConsumer;
        this.mCurrentCountryIso = com.android.server.people.data.Utils.getCurrentCountryIso(this.mContext);
    }

    boolean querySince(long sinceTime) throws java.lang.Throwable {
        android.database.Cursor cursor;
        java.lang.Throwable th;
        java.lang.String[] projection = {"_id", "date", "msg_box"};
        long j = 1000;
        java.lang.String[] selectionArgs = {java.lang.Long.toString(sinceTime / 1000)};
        boolean hasResults = false;
        android.os.Binder.allowBlockingForCurrentThread();
        try {
            try {
                cursor = this.mContext.getContentResolver().query(android.provider.Telephony.Mms.CONTENT_URI, projection, "date > ?", selectionArgs, null);
                try {
                } catch (android.database.sqlite.SQLiteException e) {
                    ex = e;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        } catch (android.database.sqlite.SQLiteException e2) {
            ex = e2;
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    int msgIdIndex = cursor.getColumnIndex("_id");
                    java.lang.String msgId = cursor.getString(msgIdIndex);
                    int dateIndex = cursor.getColumnIndex("date");
                    java.lang.String[] projection2 = projection;
                    long date = cursor.getLong(dateIndex) * j;
                    try {
                        int msgBoxIndex = cursor.getColumnIndex("msg_box");
                        int msgBox = cursor.getInt(msgBoxIndex);
                        this.mLastMessageTimestamp = java.lang.Math.max(this.mLastMessageTimestamp, date);
                        java.lang.String address = getMmsAddress(msgId, msgBox);
                        if (address != null && addEvent(address, date, msgBox)) {
                            hasResults = true;
                        }
                        projection = projection2;
                        j = 1000;
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            android.os.Binder.defaultBlockingForCurrentThread();
            return hasResults;
        }
        try {
            android.util.Slog.w(TAG, "Cursor is null when querying MMS table.");
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (android.database.sqlite.SQLiteException e3) {
                    ex = e3;
                    android.util.Slog.e(TAG, "querySince exception", ex);
                    android.os.Binder.defaultBlockingForCurrentThread();
                    return hasResults;
                } catch (java.lang.Throwable th6) {
                    th = th6;
                    android.os.Binder.defaultBlockingForCurrentThread();
                    throw th;
                }
            }
            android.os.Binder.defaultBlockingForCurrentThread();
            return false;
        } catch (java.lang.Throwable th7) {
            th = th7;
        }
        if (cursor == null) {
            throw th;
        }
        try {
            cursor.close();
            throw th;
        } catch (java.lang.Throwable th8) {
            th.addSuppressed(th8);
            throw th;
        }
    }

    long getLastMessageTimestamp() {
        return this.mLastMessageTimestamp;
    }

    private java.lang.String getMmsAddress(java.lang.String msgId, int msgBox) {
        android.database.Cursor cursor;
        android.net.Uri addressUri = android.provider.Telephony.Mms.Addr.getAddrUriForMessage(msgId);
        java.lang.String[] projection = {"address", "type"};
        java.lang.String address = null;
        try {
            cursor = this.mContext.getContentResolver().query(addressUri, projection, null, null, null);
            try {
            } finally {
            }
        } catch (android.database.sqlite.SQLiteException ex) {
            android.util.Slog.e(TAG, "getMmsAddress exception", ex);
        }
        if (cursor == null) {
            android.util.Slog.w(TAG, "Cursor is null when querying MMS address table.");
            if (cursor != null) {
                cursor.close();
            }
            return null;
        }
        while (cursor.moveToNext()) {
            int typeIndex = cursor.getColumnIndex("type");
            int type = cursor.getInt(typeIndex);
            if ((msgBox == 1 && type == 137) || (msgBox == 2 && type == 151)) {
                int addrIndex = cursor.getColumnIndex("address");
                address = cursor.getString(addrIndex);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        if (!android.provider.Telephony.Mms.isPhoneNumber(address)) {
            return null;
        }
        return android.telephony.PhoneNumberUtils.formatNumberToE164(address, this.mCurrentCountryIso);
    }

    private boolean addEvent(java.lang.String phoneNumber, long date, int msgBox) {
        if (!validateEvent(phoneNumber, date, msgBox)) {
            return false;
        }
        int eventType = MSG_BOX_TO_EVENT_TYPE.get(msgBox);
        this.mEventConsumer.accept(phoneNumber, new com.android.server.people.data.Event(date, eventType));
        return true;
    }

    private boolean validateEvent(java.lang.String phoneNumber, long date, int msgBox) {
        return !android.text.TextUtils.isEmpty(phoneNumber) && date > 0 && MSG_BOX_TO_EVENT_TYPE.indexOfKey(msgBox) >= 0;
    }
}
