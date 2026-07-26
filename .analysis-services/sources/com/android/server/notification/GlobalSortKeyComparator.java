package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class GlobalSortKeyComparator implements java.util.Comparator<com.android.server.notification.NotificationRecord> {
    private static final java.lang.String TAG = "GlobalSortComp";

    @Override // java.util.Comparator
    public int compare(com.android.server.notification.NotificationRecord left, com.android.server.notification.NotificationRecord right) {
        if (left.getGlobalSortKey() == null) {
            android.util.Slog.wtf(TAG, "Missing left global sort key: " + left);
            return 1;
        }
        if (right.getGlobalSortKey() == null) {
            android.util.Slog.wtf(TAG, "Missing right global sort key: " + right);
            return -1;
        }
        return left.getGlobalSortKey().compareTo(right.getGlobalSortKey());
    }
}
