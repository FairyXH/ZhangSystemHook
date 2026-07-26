package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class SmsQueryHelper {
    private static final android.util.SparseIntArray SMS_TYPE_TO_EVENT_TYPE = new android.util.SparseIntArray();
    private static final java.lang.String TAG = "SmsQueryHelper";
    private final android.content.Context mContext;
    private final java.lang.String mCurrentCountryIso;
    private final java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> mEventConsumer;
    private long mLastMessageTimestamp;

    static {
        SMS_TYPE_TO_EVENT_TYPE.put(1, 9);
        SMS_TYPE_TO_EVENT_TYPE.put(2, 8);
    }

    SmsQueryHelper(android.content.Context context, java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> eventConsumer) {
        this.mContext = context;
        this.mEventConsumer = eventConsumer;
        this.mCurrentCountryIso = com.android.server.people.data.Utils.getCurrentCountryIso(this.mContext);
    }

    boolean querySince(long sinceTime) {
        android.database.Cursor cursor;
        java.lang.String address = "_id";
        java.lang.String str = "date";
        java.lang.String str2 = "type";
        java.lang.String[] projection = {"_id", "date", "type", "address"};
        java.lang.String[] selectionArgs = {java.lang.Long.toString(sinceTime)};
        boolean hasResults = false;
        android.os.Binder.allowBlockingForCurrentThread();
        try {
            try {
                cursor = this.mContext.getContentResolver().query(android.provider.Telephony.Sms.CONTENT_URI, projection, "date > ?", selectionArgs, null);
                try {
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
            } finally {
                android.os.Binder.defaultBlockingForCurrentThread();
            }
        } catch (android.database.sqlite.SQLiteException ex) {
            android.util.Slog.e(TAG, "querySince exception", ex);
        }
        if (cursor == null) {
            android.util.Slog.w(TAG, "Cursor is null when querying SMS table.");
            if (cursor != null) {
                cursor.close();
            }
            android.os.Binder.defaultBlockingForCurrentThread();
            return false;
        }
        while (cursor.moveToNext()) {
            int msgIdIndex = cursor.getColumnIndex(address);
            cursor.getString(msgIdIndex);
            int dateIndex = cursor.getColumnIndex(str);
            long date = cursor.getLong(dateIndex);
            int typeIndex = cursor.getColumnIndex(str2);
            java.lang.String str3 = address;
            int type = cursor.getInt(typeIndex);
            int addressIndex = cursor.getColumnIndex("address");
            java.lang.String str4 = str;
            java.lang.String address2 = android.telephony.PhoneNumberUtils.formatNumberToE164(cursor.getString(addressIndex), this.mCurrentCountryIso);
            java.lang.String str5 = str2;
            this.mLastMessageTimestamp = java.lang.Math.max(this.mLastMessageTimestamp, date);
            if (address2 != null && addEvent(address2, date, type)) {
                hasResults = true;
            }
            address = str3;
            str = str4;
            str2 = str5;
        }
        if (cursor != null) {
            cursor.close();
        }
        return hasResults;
    }

    long getLastMessageTimestamp() {
        return this.mLastMessageTimestamp;
    }

    private boolean addEvent(java.lang.String phoneNumber, long date, int type) {
        if (!validateEvent(phoneNumber, date, type)) {
            return false;
        }
        int eventType = SMS_TYPE_TO_EVENT_TYPE.get(type);
        this.mEventConsumer.accept(phoneNumber, new com.android.server.people.data.Event(date, eventType));
        return true;
    }

    private boolean validateEvent(java.lang.String phoneNumber, long date, int type) {
        return !android.text.TextUtils.isEmpty(phoneNumber) && date > 0 && SMS_TYPE_TO_EVENT_TYPE.indexOfKey(type) >= 0;
    }
}
