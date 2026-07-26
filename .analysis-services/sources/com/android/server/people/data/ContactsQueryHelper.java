package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class ContactsQueryHelper {
    private static final java.lang.String TAG = "ContactsQueryHelper";
    private android.net.Uri mContactUri;
    private final android.content.Context mContext;
    private boolean mIsStarred;
    private long mLastUpdatedTimestamp;
    private java.lang.String mPhoneNumber;

    ContactsQueryHelper(android.content.Context context) {
        this.mContext = context;
    }

    boolean query(java.lang.String contactUri) {
        if (android.text.TextUtils.isEmpty(contactUri)) {
            return false;
        }
        android.net.Uri uri = android.net.Uri.parse(contactUri);
        if ("tel".equals(uri.getScheme())) {
            return queryWithPhoneNumber(uri.getSchemeSpecificPart());
        }
        if ("mailto".equals(uri.getScheme())) {
            return queryWithEmail(uri.getSchemeSpecificPart());
        }
        if (contactUri.startsWith(android.provider.ContactsContract.Contacts.CONTENT_LOOKUP_URI.toString())) {
            return queryWithUri(uri);
        }
        return false;
    }

    boolean querySince(long sinceTime) {
        java.lang.String[] projection = {"_id", "lookup", "starred", "has_phone_number", "contact_last_updated_timestamp"};
        java.lang.String[] selectionArgs = {java.lang.Long.toString(sinceTime)};
        return queryContact(android.provider.ContactsContract.Contacts.CONTENT_URI, projection, "contact_last_updated_timestamp > ?", selectionArgs);
    }

    android.net.Uri getContactUri() {
        return this.mContactUri;
    }

    boolean isStarred() {
        return this.mIsStarred;
    }

    java.lang.String getPhoneNumber() {
        return this.mPhoneNumber;
    }

    long getLastUpdatedTimestamp() {
        return this.mLastUpdatedTimestamp;
    }

    private boolean queryWithPhoneNumber(java.lang.String phoneNumber) {
        android.net.Uri phoneUri = android.net.Uri.withAppendedPath(android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI, android.net.Uri.encode(phoneNumber));
        return queryWithUri(phoneUri);
    }

    private boolean queryWithEmail(java.lang.String email) {
        android.net.Uri emailUri = android.net.Uri.withAppendedPath(android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI, android.net.Uri.encode(email));
        return queryWithUri(emailUri);
    }

    private boolean queryWithUri(android.net.Uri uri) {
        java.lang.String[] projection = {"_id", "lookup", "starred", "has_phone_number"};
        return queryContact(uri, projection, null, null);
    }

    private boolean queryContact(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs) {
        android.database.Cursor cursor;
        java.lang.String lookupKey = null;
        boolean hasPhoneNumber = false;
        boolean found = false;
        try {
            cursor = this.mContext.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            try {
            } finally {
                if (!found) {
                }
                return found;
            }
        } catch (android.database.sqlite.SQLiteException exception) {
            android.util.Slog.w("SQLite exception when querying contacts.", exception);
        } catch (java.lang.IllegalArgumentException exception2) {
            android.util.Slog.w("Illegal Argument exception when querying contacts.", exception2);
        }
        if (cursor == null) {
            android.util.Slog.w(TAG, "Cursor is null when querying contact.");
            if (cursor != null) {
                cursor.close();
            }
            return false;
        }
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("_id");
            long contactId = cursor.getLong(idIndex);
            int lookupKeyIndex = cursor.getColumnIndex("lookup");
            lookupKey = cursor.getString(lookupKeyIndex);
            this.mContactUri = android.provider.ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
            int starredIndex = cursor.getColumnIndex("starred");
            boolean z = true;
            this.mIsStarred = cursor.getInt(starredIndex) != 0;
            int hasPhoneNumIndex = cursor.getColumnIndex("has_phone_number");
            if (cursor.getInt(hasPhoneNumIndex) == 0) {
                z = false;
            }
            hasPhoneNumber = z;
            int lastUpdatedTimestampIndex = cursor.getColumnIndex("contact_last_updated_timestamp");
            if (lastUpdatedTimestampIndex >= 0) {
                this.mLastUpdatedTimestamp = cursor.getLong(lastUpdatedTimestampIndex);
            }
            found = true;
        }
        if (cursor != null) {
            cursor.close();
        }
        if (!found && lookupKey != null && hasPhoneNumber) {
            return queryPhoneNumber(lookupKey);
        }
        return found;
    }

    private boolean queryPhoneNumber(java.lang.String lookupKey) {
        java.lang.String[] projection = {"data4"};
        java.lang.String[] selectionArgs = {lookupKey};
        try {
            android.database.Cursor cursor = this.mContext.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, "lookup = ?", selectionArgs, null);
            try {
                if (cursor == null) {
                    android.util.Slog.w(TAG, "Cursor is null when querying contact phone number.");
                    if (cursor != null) {
                        cursor.close();
                    }
                    return false;
                }
                while (cursor.moveToNext()) {
                    int phoneNumIdx = cursor.getColumnIndex("data4");
                    if (phoneNumIdx >= 0) {
                        this.mPhoneNumber = cursor.getString(phoneNumIdx);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                    return true;
                }
                return true;
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "queryPhoneNumber error:" + e);
            return false;
        }
    }
}
