package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
final class NotificationHistoryProtoHelper {
    private static final java.lang.String TAG = "NotifHistoryProto";

    private NotificationHistoryProtoHelper() {
    }

    private static java.util.List<java.lang.String> readStringPool(android.util.proto.ProtoInputStream proto) throws java.io.IOException {
        java.util.List<java.lang.String> stringPool;
        long token = proto.start(1146756268033L);
        if (proto.nextField(1120986464257L)) {
            stringPool = new java.util.ArrayList<>(proto.readInt(1120986464257L));
        } else {
            stringPool = new java.util.ArrayList<>();
        }
        while (proto.nextField() != -1) {
            switch (proto.getFieldNumber()) {
                case 2:
                    stringPool.add(proto.readString(2237677961218L));
                    break;
            }
        }
        proto.end(token);
        return stringPool;
    }

    private static void writeStringPool(android.util.proto.ProtoOutputStream proto, android.app.NotificationHistory notifications) {
        long token = proto.start(1146756268033L);
        java.lang.String[] pooledStrings = notifications.getPooledStringsToWrite();
        proto.write(1120986464257L, pooledStrings.length);
        for (java.lang.String str : pooledStrings) {
            proto.write(2237677961218L, str);
        }
        proto.end(token);
    }

    private static void readNotification(android.util.proto.ProtoInputStream proto, java.util.List<java.lang.String> stringPool, android.app.NotificationHistory notifications, com.android.server.notification.NotificationHistoryFilter filter) throws java.io.IOException {
        long token = proto.start(2246267895811L);
        try {
            try {
                android.app.NotificationHistory.HistoricalNotification notification = readNotification(proto, stringPool);
                if (filter.matchesPackageAndChannelFilter(notification) && filter.matchesCountFilter(notifications)) {
                    notifications.addNotificationToWrite(notification);
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Error reading notification", e);
            }
        } finally {
            proto.end(token);
        }
    }

    private static android.app.NotificationHistory.HistoricalNotification readNotification(android.util.proto.ProtoInputStream parser, java.util.List<java.lang.String> stringPool) throws java.io.IOException {
        android.app.NotificationHistory.HistoricalNotification.Builder notification = new android.app.NotificationHistory.HistoricalNotification.Builder();
        java.lang.String pkg = null;
        while (true) {
            switch (parser.nextField()) {
                case -1:
                    return notification.build();
                case 1:
                    pkg = parser.readString(1138166333441L);
                    notification.setPackage(pkg);
                    stringPool.add(pkg);
                    break;
                case 2:
                    java.lang.String pkg2 = stringPool.get(parser.readInt(1120986464258L) - 1);
                    pkg = pkg2;
                    notification.setPackage(pkg);
                    break;
                case 3:
                    java.lang.String channelName = parser.readString(1138166333443L);
                    notification.setChannelName(channelName);
                    stringPool.add(channelName);
                    break;
                case 4:
                    notification.setChannelName(stringPool.get(parser.readInt(1120986464260L) - 1));
                    break;
                case 5:
                    java.lang.String channelId = parser.readString(1138166333445L);
                    notification.setChannelId(channelId);
                    stringPool.add(channelId);
                    break;
                case 6:
                    notification.setChannelId(stringPool.get(parser.readInt(1120986464262L) - 1));
                    break;
                case 7:
                    notification.setUid(parser.readInt(1120986464263L));
                    break;
                case 8:
                    notification.setUserId(parser.readInt(1120986464264L));
                    break;
                case 9:
                    notification.setPostedTimeMs(parser.readLong(1112396529673L));
                    break;
                case 10:
                    notification.setTitle(parser.readString(1138166333450L));
                    break;
                case 11:
                    notification.setText(parser.readString(1138166333451L));
                    break;
                case 12:
                    long iconToken = parser.start(1146756268044L);
                    loadIcon(parser, notification, pkg);
                    parser.end(iconToken);
                    break;
                case 13:
                    java.lang.String conversationId = parser.readString(1138166333453L);
                    notification.setConversationId(conversationId);
                    stringPool.add(conversationId);
                    break;
                case 14:
                    notification.setConversationId(stringPool.get(parser.readInt(1120986464270L) - 1));
                    break;
            }
        }
    }

    private static void loadIcon(android.util.proto.ProtoInputStream parser, android.app.NotificationHistory.HistoricalNotification.Builder notification, java.lang.String pkg) throws java.io.IOException {
        java.lang.String str;
        int iconType = 0;
        int imageResourceId = 0;
        java.lang.String imageResourceIdPackage = null;
        byte[] imageByteData = null;
        int imageByteDataLength = 0;
        int imageByteDataOffset = 0;
        java.lang.String imageUri = null;
        while (true) {
            switch (parser.nextField()) {
                case -1:
                    if (iconType == 3) {
                        if (imageByteData != null) {
                            notification.setIcon(android.graphics.drawable.Icon.createWithData(imageByteData, imageByteDataOffset, imageByteDataLength));
                            return;
                        }
                        return;
                    } else {
                        if (iconType == 2) {
                            if (imageResourceId != 0) {
                                if (imageResourceIdPackage != null) {
                                    str = imageResourceIdPackage;
                                } else {
                                    str = pkg;
                                }
                                notification.setIcon(android.graphics.drawable.Icon.createWithResource(str, imageResourceId));
                                return;
                            }
                            return;
                        }
                        if (iconType == 4 && imageUri != null) {
                            notification.setIcon(android.graphics.drawable.Icon.createWithContentUri(imageUri));
                            return;
                        }
                        return;
                    }
                case 1:
                    iconType = parser.readInt(1159641169921L);
                    break;
                case 2:
                    parser.readString(1138166333442L);
                    break;
                case 3:
                    imageResourceId = parser.readInt(1120986464259L);
                    break;
                case 4:
                    imageResourceIdPackage = parser.readString(1138166333444L);
                    break;
                case 5:
                    imageByteData = parser.readBytes(1151051235333L);
                    break;
                case 6:
                    imageByteDataLength = parser.readInt(1120986464262L);
                    break;
                case 7:
                    imageByteDataOffset = parser.readInt(1120986464263L);
                    break;
                case 8:
                    imageUri = parser.readString(1138166333448L);
                    break;
            }
        }
    }

    private static void writeIcon(android.util.proto.ProtoOutputStream proto, android.app.NotificationHistory.HistoricalNotification notification) {
        long token = proto.start(1146756268044L);
        proto.write(1159641169921L, notification.getIcon().getType());
        switch (notification.getIcon().getType()) {
            case 2:
                proto.write(1120986464259L, notification.getIcon().getResId());
                if (!notification.getPackage().equals(notification.getIcon().getResPackage())) {
                    proto.write(1138166333444L, notification.getIcon().getResPackage());
                }
                break;
            case 3:
                proto.write(1151051235333L, notification.getIcon().getDataBytes());
                proto.write(1120986464262L, notification.getIcon().getDataLength());
                proto.write(1120986464263L, notification.getIcon().getDataOffset());
                break;
            case 4:
                proto.write(1138166333448L, notification.getIcon().getUriString());
                break;
        }
        proto.end(token);
    }

    private static void writeNotification(android.util.proto.ProtoOutputStream proto, java.lang.String[] stringPool, android.app.NotificationHistory.HistoricalNotification notification) {
        long token = proto.start(2246267895811L);
        int packageIndex = java.util.Arrays.binarySearch(stringPool, notification.getPackage());
        if (packageIndex < 0) {
            android.util.Slog.w(TAG, "notification package name (" + notification.getPackage() + ") not found in string cache");
            proto.write(1138166333441L, notification.getPackage());
        } else {
            proto.write(1120986464258L, packageIndex + 1);
        }
        int channelNameIndex = java.util.Arrays.binarySearch(stringPool, notification.getChannelName());
        if (channelNameIndex < 0) {
            android.util.Slog.w(TAG, "notification channel name (" + notification.getChannelName() + ") not found in string cache");
            proto.write(1138166333443L, notification.getChannelName());
        } else {
            proto.write(1120986464260L, channelNameIndex + 1);
        }
        int channelIdIndex = java.util.Arrays.binarySearch(stringPool, notification.getChannelId());
        if (channelIdIndex < 0) {
            android.util.Slog.w(TAG, "notification channel id (" + notification.getChannelId() + ") not found in string cache");
            proto.write(1138166333445L, notification.getChannelId());
        } else {
            proto.write(1120986464262L, channelIdIndex + 1);
        }
        if (!android.text.TextUtils.isEmpty(notification.getConversationId())) {
            int conversationIdIndex = java.util.Arrays.binarySearch(stringPool, notification.getConversationId());
            if (conversationIdIndex < 0) {
                android.util.Slog.w(TAG, "notification conversation id (" + notification.getConversationId() + ") not found in string cache");
                proto.write(1138166333453L, notification.getConversationId());
            } else {
                proto.write(1120986464270L, conversationIdIndex + 1);
            }
        }
        proto.write(1120986464263L, notification.getUid());
        proto.write(1120986464264L, notification.getUserId());
        proto.write(1112396529673L, notification.getPostedTimeMs());
        proto.write(1138166333450L, notification.getTitle());
        proto.write(1138166333451L, notification.getText());
        writeIcon(proto, notification);
        proto.end(token);
    }

    public static void read(java.io.InputStream in, android.app.NotificationHistory notifications, com.android.server.notification.NotificationHistoryFilter filter) throws java.io.IOException {
        android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(in);
        java.util.List<java.lang.String> stringPool = new java.util.ArrayList<>();
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    if (filter.isFiltering()) {
                        notifications.poolStringsFromNotifications();
                        return;
                    } else {
                        notifications.addPooledStrings(stringPool);
                        return;
                    }
                case 1:
                    stringPool = readStringPool(proto);
                    break;
                case 3:
                    readNotification(proto, stringPool, notifications, filter);
                    break;
            }
        }
    }

    public static void write(java.io.OutputStream out, android.app.NotificationHistory notifications, int version) {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
        proto.write(1120986464258L, version);
        writeStringPool(proto, notifications);
        java.util.List<android.app.NotificationHistory.HistoricalNotification> notificationsToWrite = notifications.getNotificationsToWrite();
        int count = notificationsToWrite.size();
        for (int i = 0; i < count; i++) {
            writeNotification(proto, notifications.getPooledStringsToWrite(), notificationsToWrite.get(i));
        }
        proto.flush();
    }
}
