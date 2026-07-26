package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class CallLogQueryHelper {
    private static final android.util.SparseIntArray CALL_TYPE_TO_EVENT_TYPE = new android.util.SparseIntArray();
    private static final java.lang.String TAG = "CallLogQueryHelper";
    private final android.content.Context mContext;
    private final java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> mEventConsumer;
    private long mLastCallTimestamp;

    static {
        CALL_TYPE_TO_EVENT_TYPE.put(1, 11);
        CALL_TYPE_TO_EVENT_TYPE.put(2, 10);
        CALL_TYPE_TO_EVENT_TYPE.put(3, 12);
    }

    CallLogQueryHelper(android.content.Context context, java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> eventConsumer) {
        this.mContext = context;
        this.mEventConsumer = eventConsumer;
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00a9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:? A[Catch: SecurityException -> 0x00b3, SYNTHETIC, TRY_LEAVE, TryCatch #0 {SecurityException -> 0x00b3, blocks: (B:3:0x001f, B:8:0x003a, B:34:0x00b2, B:33:0x00af, B:29:0x00a9), top: B:38:0x001f, inners: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean querySince(long r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 203
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.people.data.CallLogQueryHelper.querySince(long):boolean");
    }

    long getLastCallTimestamp() {
        return this.mLastCallTimestamp;
    }

    private boolean addEvent(java.lang.String phoneNumber, long date, long durationSeconds, int callType) {
        if (!validateEvent(phoneNumber, date, callType)) {
            return false;
        }
        int eventType = CALL_TYPE_TO_EVENT_TYPE.get(callType);
        com.android.server.people.data.Event event = new com.android.server.people.data.Event.Builder(date, eventType).setDurationSeconds((int) durationSeconds).build();
        this.mEventConsumer.accept(phoneNumber, event);
        return true;
    }

    private boolean validateEvent(java.lang.String phoneNumber, long date, int callType) {
        return !android.text.TextUtils.isEmpty(phoneNumber) && date > 0 && CALL_TYPE_TO_EVENT_TYPE.indexOfKey(callType) >= 0;
    }
}
