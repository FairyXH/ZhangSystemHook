package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationTimeComparator implements java.util.Comparator<com.android.server.notification.NotificationRecord> {
    @Override // java.util.Comparator
    public int compare(com.android.server.notification.NotificationRecord left, com.android.server.notification.NotificationRecord right) {
        return java.lang.Long.compare(left.getRankingTimeMs(), right.getRankingTimeMs()) * (-1);
    }
}
