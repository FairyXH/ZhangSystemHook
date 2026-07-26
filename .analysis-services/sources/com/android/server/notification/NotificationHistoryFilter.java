package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class NotificationHistoryFilter {
    private java.lang.String mChannel;
    private int mNotificationCount;
    private java.lang.String mPackage;

    private NotificationHistoryFilter() {
    }

    public java.lang.String getPackage() {
        return this.mPackage;
    }

    public java.lang.String getChannel() {
        return this.mChannel;
    }

    public int getMaxNotifications() {
        return this.mNotificationCount;
    }

    public boolean isFiltering() {
        return (getPackage() == null && getChannel() == null && this.mNotificationCount >= Integer.MAX_VALUE) ? false : true;
    }

    public boolean matchesPackageAndChannelFilter(android.app.NotificationHistory.HistoricalNotification notification) {
        if (!android.text.TextUtils.isEmpty(getPackage())) {
            if (getPackage().equals(notification.getPackage())) {
                return android.text.TextUtils.isEmpty(getChannel()) || getChannel().equals(notification.getChannelId());
            }
            return false;
        }
        return true;
    }

    public boolean matchesCountFilter(android.app.NotificationHistory notifications) {
        return notifications.getHistoryCount() < this.mNotificationCount;
    }

    public static final class Builder {
        private java.lang.String mPackage = null;
        private java.lang.String mChannel = null;
        private int mNotificationCount = Integer.MAX_VALUE;

        public com.android.server.notification.NotificationHistoryFilter.Builder setPackage(java.lang.String aPackage) {
            this.mPackage = aPackage;
            return this;
        }

        public com.android.server.notification.NotificationHistoryFilter.Builder setChannel(java.lang.String pkg, java.lang.String channel) {
            setPackage(pkg);
            this.mChannel = channel;
            return this;
        }

        public com.android.server.notification.NotificationHistoryFilter.Builder setMaxNotifications(int notificationCount) {
            this.mNotificationCount = notificationCount;
            return this;
        }

        public com.android.server.notification.NotificationHistoryFilter build() {
            com.android.server.notification.NotificationHistoryFilter filter = new com.android.server.notification.NotificationHistoryFilter();
            filter.mPackage = this.mPackage;
            filter.mChannel = this.mChannel;
            filter.mNotificationCount = this.mNotificationCount;
            return filter;
        }
    }
}
