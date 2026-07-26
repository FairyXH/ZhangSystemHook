package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public class RequestId {
    static final int MAGIC_NUMBER = 5000;
    static final int MAX_PRIMARY_REQUEST_ID = 32766;
    static final int MAX_REQUEST_ID = 32767;
    static final int MAX_SECONDARY_REQUEST_ID = 32767;
    static final int MAX_START_ID = 1000;
    static final int MIN_PRIMARY_REQUEST_ID = 2;
    static final int MIN_REQUEST_ID = 2;
    static final int MIN_SECONDARY_REQUEST_ID = 3;
    private static final java.lang.String TAG = "RequestId";
    private java.util.concurrent.atomic.AtomicInteger sIdCounter;

    RequestId(int startId) {
        if (startId < 2 || startId > 32767) {
            throw new java.lang.IllegalArgumentException("startId must be between 2 and 32767");
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "RequestId(int): startId= " + startId);
        }
        this.sIdCounter = new java.util.concurrent.atomic.AtomicInteger(startId);
    }

    int getRequestId() {
        return this.sIdCounter.get();
    }

    public RequestId() {
        java.util.Random random = new java.util.Random();
        int startId = random.nextInt(1001 - 2) + 2;
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "RequestId(): startId= " + startId);
        }
        this.sIdCounter = new java.util.concurrent.atomic.AtomicInteger(startId);
    }

    public static int getLastRequestIdIndex(java.util.List<java.lang.Integer> requestIds) {
        if (requestIds.size() == 1) {
            return 0;
        }
        boolean wrapHasHappened = false;
        int latestRequestIdIndex = -1;
        int i = 0;
        while (true) {
            if (i >= requestIds.size() - 1) {
                break;
            }
            if (requestIds.get(i + 1).intValue() - requestIds.get(i).intValue() <= 5000) {
                i++;
            } else {
                wrapHasHappened = true;
                latestRequestIdIndex = i;
                break;
            }
        }
        if (!wrapHasHappened) {
            latestRequestIdIndex = requestIds.size() - 1;
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "getLastRequestIdIndex(): latestRequestIdIndex = " + latestRequestIdIndex);
        }
        return latestRequestIdIndex;
    }

    public int nextId(boolean isSecondary) {
        int requestId;
        do {
            requestId = this.sIdCounter.incrementAndGet() % 32768;
            if (requestId < 2) {
                requestId = 2;
            }
            this.sIdCounter.set(requestId);
        } while (isSecondaryProvider(requestId) != isSecondary);
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "nextId(): requestId = " + requestId);
        }
        return requestId;
    }

    public static boolean isSecondaryProvider(int requestId) {
        return requestId % 2 == 1;
    }
}
