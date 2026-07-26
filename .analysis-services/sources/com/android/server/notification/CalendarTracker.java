package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class CalendarTracker {
    private static final java.lang.String ATTENDEE_SELECTION = "event_id = ? AND attendeeEmail = ?";
    private static final boolean DEBUG_ATTENDEES = false;
    private static final int EVENT_CHECK_LOOKAHEAD = 86400000;
    private static final java.lang.String INSTANCE_ORDER_BY = "begin ASC";
    private static final java.lang.String TAG = "ConditionProviders.CT";
    private com.android.server.notification.CalendarTracker.Callback mCallback;
    private final android.database.ContentObserver mObserver = new android.database.ContentObserver(null) { // from class: com.android.server.notification.CalendarTracker.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri u) {
            if (com.android.server.notification.CalendarTracker.DEBUG) {
                android.util.Log.d(com.android.server.notification.CalendarTracker.TAG, "onChange selfChange=" + selfChange + " uri=" + u + " u=" + com.android.server.notification.CalendarTracker.this.mUserContext.getUserId());
            }
            com.android.server.notification.CalendarTracker.this.mCallback.onChanged();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            if (com.android.server.notification.CalendarTracker.DEBUG) {
                android.util.Log.d(com.android.server.notification.CalendarTracker.TAG, "onChange selfChange=" + selfChange);
            }
        }
    };
    private boolean mRegistered;
    private final android.content.Context mSystemContext;
    private final android.content.Context mUserContext;
    private static final boolean DEBUG = android.util.Log.isLoggable("ConditionProviders", 3);
    private static final java.lang.String[] INSTANCE_PROJECTION = {"begin", "end", "title", com.android.server.wm.ActivityTaskManagerService.DUMP_VISIBLE_ACTIVITIES, "event_id", "calendar_displayName", "ownerAccount", "calendar_id", "availability"};
    private static final java.lang.String[] ATTENDEE_PROJECTION = {"event_id", "attendeeEmail", "attendeeStatus"};

    public interface Callback {
        void onChanged();
    }

    public static class CheckEventResult {
        public boolean inEvent;
        public long recheckAt;
    }

    public CalendarTracker(android.content.Context systemContext, android.content.Context userContext) {
        this.mSystemContext = systemContext;
        this.mUserContext = userContext;
    }

    public void setCallback(com.android.server.notification.CalendarTracker.Callback callback) {
        if (this.mCallback == callback) {
            return;
        }
        this.mCallback = callback;
        setRegistered(this.mCallback != null);
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("mCallback=");
        pw.println(this.mCallback);
        pw.print(prefix);
        pw.print("mRegistered=");
        pw.println(this.mRegistered);
        pw.print(prefix);
        pw.print("u=");
        pw.println(this.mUserContext.getUserId());
    }

    private android.util.ArraySet<java.lang.Long> getCalendarsWithAccess() {
        long start = java.lang.System.currentTimeMillis();
        android.util.ArraySet<java.lang.Long> rt = new android.util.ArraySet<>();
        java.lang.String[] projection = {"_id"};
        android.database.Cursor cursor = null;
        try {
            try {
                cursor = this.mUserContext.getContentResolver().query(android.provider.CalendarContract.Calendars.CONTENT_URI, projection, "calendar_access_level >= 500 AND sync_events = 1", null, null);
                while (cursor != null) {
                    if (!cursor.moveToNext()) {
                        break;
                    }
                    rt.add(java.lang.Long.valueOf(cursor.getLong(0)));
                }
            } catch (android.database.sqlite.SQLiteException e) {
                android.util.Slog.w(TAG, "error querying calendar content provider", e);
                if (cursor != null) {
                }
            }
            if (DEBUG) {
                android.util.Log.d(TAG, "getCalendarsWithAccess took " + (java.lang.System.currentTimeMillis() - start));
            }
            return rt;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x01a2  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x024f  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0118  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x011d A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.android.server.notification.CalendarTracker.CheckEventResult checkEvent(android.service.notification.ZenModeConfig.EventInfo r35, long r36) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 595
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.CalendarTracker.checkEvent(android.service.notification.ZenModeConfig$EventInfo, long):com.android.server.notification.CalendarTracker$CheckEventResult");
    }

    private boolean meetsAttendee(android.service.notification.ZenModeConfig.EventInfo filter, int eventId, java.lang.String email) {
        java.lang.String[] selectionArgs;
        java.lang.String selection;
        long start = java.lang.System.currentTimeMillis();
        java.lang.String selection2 = ATTENDEE_SELECTION;
        java.lang.String[] selectionArgs2 = {java.lang.Integer.toString(eventId), email};
        android.database.Cursor cursor = null;
        int i = 0;
        try {
            try {
                cursor = this.mUserContext.getContentResolver().query(android.provider.CalendarContract.Attendees.CONTENT_URI, ATTENDEE_PROJECTION, ATTENDEE_SELECTION, selectionArgs2, null);
                int i2 = 1;
                try {
                    if (cursor == null || cursor.getCount() == 0) {
                        if (DEBUG) {
                            android.util.Log.d(TAG, "No attendees found");
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        if (!DEBUG) {
                            return true;
                        }
                        android.util.Log.d(TAG, "meetsAttendee took " + (java.lang.System.currentTimeMillis() - start));
                        return true;
                    }
                    boolean rt = false;
                    while (cursor != null) {
                        if (!cursor.moveToNext()) {
                            break;
                        }
                        long rowEventId = cursor.getLong(i);
                        java.lang.String rowEmail = cursor.getString(i2);
                        int status = cursor.getInt(2);
                        boolean meetsReply = meetsReply(filter.reply, status);
                        if (DEBUG) {
                            selectionArgs = selectionArgs2;
                            try {
                                selection = selection2;
                            } catch (android.database.sqlite.SQLiteException e) {
                                e = e;
                            } catch (java.lang.Throwable th) {
                                e = th;
                            }
                            try {
                                android.util.Log.d(TAG, "" + java.lang.String.format("status=%s, meetsReply=%s", attendeeStatusToString(status), java.lang.Boolean.valueOf(meetsReply)));
                            } catch (android.database.sqlite.SQLiteException e2) {
                                e = e2;
                            } catch (java.lang.Throwable th2) {
                                e = th2;
                                if (cursor != null) {
                                    cursor.close();
                                }
                                if (DEBUG) {
                                    android.util.Log.d(TAG, "meetsAttendee took " + (java.lang.System.currentTimeMillis() - start));
                                }
                                throw e;
                            }
                        } else {
                            selectionArgs = selectionArgs2;
                            selection = selection2;
                        }
                        boolean eventMeets = rowEventId == ((long) eventId) && java.util.Objects.equals(rowEmail, email) && meetsReply;
                        rt |= eventMeets;
                        selectionArgs2 = selectionArgs;
                        selection2 = selection;
                        i2 = 1;
                        i = 0;
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    if (DEBUG) {
                        android.util.Log.d(TAG, "meetsAttendee took " + (java.lang.System.currentTimeMillis() - start));
                    }
                    return rt;
                } catch (android.database.sqlite.SQLiteException e3) {
                    e = e3;
                }
            } catch (java.lang.Throwable th3) {
                e = th3;
            }
        } catch (android.database.sqlite.SQLiteException e4) {
            e = e4;
        } catch (java.lang.Throwable th4) {
            e = th4;
        }
        android.util.Slog.w(TAG, "error querying attendees content provider", e);
        if (cursor != null) {
            cursor.close();
        }
        if (!DEBUG) {
            return false;
        }
        android.util.Log.d(TAG, "meetsAttendee took " + (java.lang.System.currentTimeMillis() - start));
        return false;
    }

    private void setRegistered(boolean registered) {
        if (this.mRegistered == registered) {
            return;
        }
        android.content.ContentResolver cr = this.mSystemContext.getContentResolver();
        int userId = this.mUserContext.getUserId();
        if (this.mRegistered) {
            if (DEBUG) {
                android.util.Log.d(TAG, "unregister content observer u=" + userId);
            }
            cr.unregisterContentObserver(this.mObserver);
        }
        this.mRegistered = registered;
        if (DEBUG) {
            android.util.Log.d(TAG, "mRegistered = " + registered + " u=" + userId);
        }
        if (this.mRegistered) {
            if (DEBUG) {
                android.util.Log.d(TAG, "register content observer u=" + userId);
            }
            cr.registerContentObserver(android.provider.CalendarContract.Instances.CONTENT_URI, true, this.mObserver, userId);
            cr.registerContentObserver(android.provider.CalendarContract.Events.CONTENT_URI, true, this.mObserver, userId);
            cr.registerContentObserver(android.provider.CalendarContract.Calendars.CONTENT_URI, true, this.mObserver, userId);
        }
    }

    private static java.lang.String attendeeStatusToString(int status) {
        switch (status) {
            case 0:
                return "ATTENDEE_STATUS_NONE";
            case 1:
                return "ATTENDEE_STATUS_ACCEPTED";
            case 2:
                return "ATTENDEE_STATUS_DECLINED";
            case 3:
                return "ATTENDEE_STATUS_INVITED";
            case 4:
                return "ATTENDEE_STATUS_TENTATIVE";
            default:
                return "ATTENDEE_STATUS_UNKNOWN_" + status;
        }
    }

    private static java.lang.String availabilityToString(int availability) {
        switch (availability) {
            case 0:
                return "AVAILABILITY_BUSY";
            case 1:
                return "AVAILABILITY_FREE";
            case 2:
                return "AVAILABILITY_TENTATIVE";
            default:
                return "AVAILABILITY_UNKNOWN_" + availability;
        }
    }

    private static boolean meetsReply(int reply, int attendeeStatus) {
        switch (reply) {
            case 0:
                return attendeeStatus != 2;
            case 1:
                return attendeeStatus == 1 || attendeeStatus == 4;
            case 2:
                return attendeeStatus == 1;
            default:
                return false;
        }
    }
}
