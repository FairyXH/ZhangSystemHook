package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationHistoryDatabase {
    private static final boolean DEBUG = com.android.server.notification.NotificationManagerService.DBG;
    private static final int DEFAULT_CURRENT_VERSION = 1;
    private static final int HISTORY_RETENTION_DAYS = 1;
    private static final long INVALID_FILE_TIME_MS = -1;
    private static final java.lang.String TAG = "NotiHistoryDatabase";
    private static final long WRITE_BUFFER_INTERVAL_MS = 1200000;
    private final android.os.Handler mFileWriteHandler;
    private final java.io.File mHistoryDir;
    private final java.io.File mVersionFile;
    private final java.lang.Object mLock = new java.lang.Object();
    private int mCurrentVersion = 1;
    final java.util.List<android.util.AtomicFile> mHistoryFiles = new java.util.ArrayList();
    android.app.NotificationHistory mBuffer = new android.app.NotificationHistory();
    private final com.android.server.notification.NotificationHistoryDatabase.WriteBufferRunnable mWriteBufferRunnable = new com.android.server.notification.NotificationHistoryDatabase.WriteBufferRunnable();

    public NotificationHistoryDatabase(android.os.Handler fileWriteHandler, java.io.File dir) {
        this.mFileWriteHandler = fileWriteHandler;
        this.mVersionFile = new java.io.File(dir, "version");
        this.mHistoryDir = new java.io.File(dir, "history");
    }

    public void init() {
        synchronized (this.mLock) {
            try {
                if (!this.mHistoryDir.exists() && !this.mHistoryDir.mkdir()) {
                    throw new java.lang.IllegalStateException("could not create history directory");
                }
                this.mVersionFile.createNewFile();
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "could not create needed files", e);
            }
            checkVersionAndBuildLocked();
            indexFilesLocked();
            prune();
        }
    }

    private void indexFilesLocked() {
        this.mHistoryFiles.clear();
        java.io.File[] files = this.mHistoryDir.listFiles();
        if (files == null) {
            return;
        }
        java.util.Arrays.sort(files, new java.util.Comparator() { // from class: com.android.server.notification.NotificationHistoryDatabase$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return java.lang.Long.compare(com.android.server.notification.NotificationHistoryDatabase.safeParseLong(((java.io.File) obj2).getName()), com.android.server.notification.NotificationHistoryDatabase.safeParseLong(((java.io.File) obj).getName()));
            }
        });
        for (java.io.File file : files) {
            this.mHistoryFiles.add(new android.util.AtomicFile(file));
        }
    }

    private void checkVersionAndBuildLocked() {
        int version;
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(this.mVersionFile));
            try {
                version = java.lang.Integer.parseInt(reader.readLine());
                reader.close();
            } catch (java.lang.Throwable th) {
                try {
                    reader.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | java.lang.NumberFormatException e) {
            version = 0;
        }
        if (version != this.mCurrentVersion && this.mVersionFile.exists()) {
            try {
                java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(this.mVersionFile));
                try {
                    writer.write(java.lang.Integer.toString(this.mCurrentVersion));
                    writer.write("\n");
                    writer.flush();
                    writer.close();
                } finally {
                }
            } catch (java.io.IOException e2) {
                android.util.Slog.e(TAG, "Failed to write new version");
                throw new java.lang.RuntimeException(e2);
            }
        }
    }

    public void forceWriteToDisk() {
        this.mFileWriteHandler.post(this.mWriteBufferRunnable);
    }

    public void onPackageRemoved(java.lang.String packageName) {
        com.android.server.notification.NotificationHistoryDatabase.RemovePackageRunnable rpr = new com.android.server.notification.NotificationHistoryDatabase.RemovePackageRunnable(packageName);
        this.mFileWriteHandler.post(rpr);
    }

    public void deleteNotificationHistoryItem(java.lang.String pkg, long postedTime) {
        com.android.server.notification.NotificationHistoryDatabase.RemoveNotificationRunnable rnr = new com.android.server.notification.NotificationHistoryDatabase.RemoveNotificationRunnable(pkg, postedTime);
        this.mFileWriteHandler.post(rnr);
    }

    public void deleteConversations(java.lang.String pkg, java.util.Set<java.lang.String> conversationIds) {
        com.android.server.notification.NotificationHistoryDatabase.RemoveConversationRunnable rcr = new com.android.server.notification.NotificationHistoryDatabase.RemoveConversationRunnable(pkg, conversationIds);
        this.mFileWriteHandler.post(rcr);
    }

    public void deleteNotificationChannel(java.lang.String pkg, java.lang.String channelId) {
        com.android.server.notification.NotificationHistoryDatabase.RemoveChannelRunnable rcr = new com.android.server.notification.NotificationHistoryDatabase.RemoveChannelRunnable(pkg, channelId);
        this.mFileWriteHandler.post(rcr);
    }

    public void addNotification(android.app.NotificationHistory.HistoricalNotification notification) {
        synchronized (this.mLock) {
            this.mBuffer.addNewNotificationToWrite(notification);
            if (this.mBuffer.getHistoryCount() == 1) {
                this.mFileWriteHandler.postDelayed(this.mWriteBufferRunnable, WRITE_BUFFER_INTERVAL_MS);
            }
        }
    }

    public android.app.NotificationHistory readNotificationHistory() {
        android.app.NotificationHistory notifications;
        synchronized (this.mLock) {
            notifications = new android.app.NotificationHistory();
            notifications.addNotificationsToWrite(this.mBuffer);
            for (android.util.AtomicFile file : this.mHistoryFiles) {
                try {
                    readLocked(file, notifications, new com.android.server.notification.NotificationHistoryFilter.Builder().build());
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, "error reading " + file.getBaseFile().getAbsolutePath(), e);
                }
            }
        }
        return notifications;
    }

    public android.app.NotificationHistory readNotificationHistory(java.lang.String packageName, java.lang.String channelId, int maxNotifications) {
        android.app.NotificationHistory notifications;
        synchronized (this.mLock) {
            notifications = new android.app.NotificationHistory();
            for (android.util.AtomicFile file : this.mHistoryFiles) {
                try {
                    readLocked(file, notifications, new com.android.server.notification.NotificationHistoryFilter.Builder().setPackage(packageName).setChannel(packageName, channelId).setMaxNotifications(maxNotifications).build());
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, "error reading " + file.getBaseFile().getAbsolutePath(), e);
                }
                if (maxNotifications == notifications.getHistoryCount()) {
                    break;
                }
            }
        }
        return notifications;
    }

    public void disableHistory() {
        synchronized (this.mLock) {
            for (android.util.AtomicFile file : this.mHistoryFiles) {
                file.delete();
            }
            this.mHistoryDir.delete();
            this.mHistoryFiles.clear();
        }
    }

    void prune() {
        prune(1, java.lang.System.currentTimeMillis());
    }

    void prune(int retentionDays, long currentTimeMillis) {
        synchronized (this.mLock) {
            java.util.GregorianCalendar retentionBoundary = new java.util.GregorianCalendar();
            retentionBoundary.setTimeInMillis(currentTimeMillis);
            retentionBoundary.add(5, retentionDays * (-1));
            for (int i = this.mHistoryFiles.size() - 1; i >= 0; i--) {
                android.util.AtomicFile currentOldestFile = this.mHistoryFiles.get(i);
                long creationTime = safeParseLong(currentOldestFile.getBaseFile().getName());
                if (DEBUG) {
                    android.util.Slog.d(TAG, "File " + currentOldestFile.getBaseFile().getName() + " created on " + creationTime);
                }
                if (creationTime <= retentionBoundary.getTimeInMillis()) {
                    deleteFile(currentOldestFile);
                }
            }
        }
    }

    void removeFilePathFromHistory(java.lang.String filePath) {
        if (filePath == null) {
            return;
        }
        java.util.Iterator<android.util.AtomicFile> historyFileItr = this.mHistoryFiles.iterator();
        while (historyFileItr.hasNext()) {
            android.util.AtomicFile af = historyFileItr.next();
            if (af != null && filePath.equals(af.getBaseFile().getAbsolutePath())) {
                historyFileItr.remove();
                return;
            }
        }
    }

    private void deleteFile(android.util.AtomicFile file) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Removed " + file.getBaseFile().getName());
        }
        file.delete();
        removeFilePathFromHistory(file.getBaseFile().getAbsolutePath());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeLocked(android.util.AtomicFile file, android.app.NotificationHistory notifications) throws java.io.IOException {
        java.io.FileOutputStream fos = file.startWrite();
        try {
            com.android.server.notification.NotificationHistoryProtoHelper.write(fos, notifications, this.mCurrentVersion);
            file.finishWrite(fos);
            fos = null;
        } finally {
            file.failWrite(fos);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void readLocked(android.util.AtomicFile file, android.app.NotificationHistory notificationsOut, com.android.server.notification.NotificationHistoryFilter filter) throws java.io.IOException {
        java.io.FileInputStream in = null;
        try {
            try {
                in = file.openRead();
                com.android.server.notification.NotificationHistoryProtoHelper.read(in, notificationsOut, filter);
            } catch (java.io.FileNotFoundException e) {
                android.util.Slog.e(TAG, "Cannot open " + file.getBaseFile().getAbsolutePath(), e);
                throw e;
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private static long safeParseLong(java.lang.String fileName) {
        try {
            return java.lang.Long.parseLong(fileName);
        } catch (java.lang.NumberFormatException e) {
            return -1L;
        }
    }

    final class WriteBufferRunnable implements java.lang.Runnable {
        WriteBufferRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            long time = java.lang.System.currentTimeMillis();
            run(new android.util.AtomicFile(new java.io.File(com.android.server.notification.NotificationHistoryDatabase.this.mHistoryDir, java.lang.String.valueOf(time))));
        }

        void run(android.util.AtomicFile file) {
            synchronized (com.android.server.notification.NotificationHistoryDatabase.this.mLock) {
                if (com.android.server.notification.NotificationHistoryDatabase.DEBUG) {
                    android.util.Slog.d(com.android.server.notification.NotificationHistoryDatabase.TAG, "WriteBufferRunnable " + file.getBaseFile().getAbsolutePath());
                }
                try {
                    com.android.server.notification.NotificationHistoryDatabase.this.writeLocked(file, com.android.server.notification.NotificationHistoryDatabase.this.mBuffer);
                    com.android.server.notification.NotificationHistoryDatabase.this.mHistoryFiles.add(0, file);
                    com.android.server.notification.NotificationHistoryDatabase.this.mBuffer = new android.app.NotificationHistory();
                } catch (java.io.IOException e) {
                    android.util.Slog.e(com.android.server.notification.NotificationHistoryDatabase.TAG, "Failed to write buffer to disk. not flushing buffer", e);
                }
            }
        }
    }

    private final class RemovePackageRunnable implements java.lang.Runnable {
        private java.lang.String mPkg;

        public RemovePackageRunnable(java.lang.String pkg) {
            this.mPkg = pkg;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.notification.NotificationHistoryDatabase.DEBUG) {
                android.util.Slog.d(com.android.server.notification.NotificationHistoryDatabase.TAG, "RemovePackageRunnable " + this.mPkg);
            }
            synchronized (com.android.server.notification.NotificationHistoryDatabase.this.mLock) {
                com.android.server.notification.NotificationHistoryDatabase.this.mBuffer.removeNotificationsFromWrite(this.mPkg);
                for (android.util.AtomicFile af : com.android.server.notification.NotificationHistoryDatabase.this.mHistoryFiles) {
                    try {
                        android.app.NotificationHistory notifications = new android.app.NotificationHistory();
                        com.android.server.notification.NotificationHistoryDatabase.readLocked(af, notifications, new com.android.server.notification.NotificationHistoryFilter.Builder().build());
                        notifications.removeNotificationsFromWrite(this.mPkg);
                        com.android.server.notification.NotificationHistoryDatabase.this.writeLocked(af, notifications);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.notification.NotificationHistoryDatabase.TAG, "Cannot clean up file on pkg removal " + af.getBaseFile().getAbsolutePath(), e);
                    }
                }
            }
        }
    }

    final class RemoveNotificationRunnable implements java.lang.Runnable {
        private android.app.NotificationHistory mNotificationHistory;
        private java.lang.String mPkg;
        private long mPostedTime;

        public RemoveNotificationRunnable(java.lang.String pkg, long postedTime) {
            this.mPkg = pkg;
            this.mPostedTime = postedTime;
        }

        void setNotificationHistory(android.app.NotificationHistory nh) {
            this.mNotificationHistory = nh;
        }

        @Override // java.lang.Runnable
        public void run() {
            android.app.NotificationHistory notificationHistory;
            if (com.android.server.notification.NotificationHistoryDatabase.DEBUG) {
                android.util.Slog.d(com.android.server.notification.NotificationHistoryDatabase.TAG, "RemoveNotificationRunnable");
            }
            synchronized (com.android.server.notification.NotificationHistoryDatabase.this.mLock) {
                com.android.server.notification.NotificationHistoryDatabase.this.mBuffer.removeNotificationFromWrite(this.mPkg, this.mPostedTime);
                for (android.util.AtomicFile af : com.android.server.notification.NotificationHistoryDatabase.this.mHistoryFiles) {
                    try {
                        if (this.mNotificationHistory != null) {
                            notificationHistory = this.mNotificationHistory;
                        } else {
                            notificationHistory = new android.app.NotificationHistory();
                        }
                        com.android.server.notification.NotificationHistoryDatabase.readLocked(af, notificationHistory, new com.android.server.notification.NotificationHistoryFilter.Builder().build());
                        if (notificationHistory.removeNotificationFromWrite(this.mPkg, this.mPostedTime)) {
                            com.android.server.notification.NotificationHistoryDatabase.this.writeLocked(af, notificationHistory);
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.notification.NotificationHistoryDatabase.TAG, "Cannot clean up file on notification removal " + af.getBaseFile().getName(), e);
                    }
                }
            }
        }
    }

    final class RemoveConversationRunnable implements java.lang.Runnable {
        private java.util.Set<java.lang.String> mConversationIds;
        private android.app.NotificationHistory mNotificationHistory;
        private java.lang.String mPkg;

        public RemoveConversationRunnable(java.lang.String pkg, java.util.Set<java.lang.String> conversationIds) {
            this.mPkg = pkg;
            this.mConversationIds = conversationIds;
        }

        void setNotificationHistory(android.app.NotificationHistory nh) {
            this.mNotificationHistory = nh;
        }

        @Override // java.lang.Runnable
        public void run() {
            android.app.NotificationHistory notificationHistory;
            if (com.android.server.notification.NotificationHistoryDatabase.DEBUG) {
                android.util.Slog.d(com.android.server.notification.NotificationHistoryDatabase.TAG, "RemoveConversationRunnable " + this.mPkg + " " + this.mConversationIds);
            }
            synchronized (com.android.server.notification.NotificationHistoryDatabase.this.mLock) {
                com.android.server.notification.NotificationHistoryDatabase.this.mBuffer.removeConversationsFromWrite(this.mPkg, this.mConversationIds);
                for (android.util.AtomicFile af : com.android.server.notification.NotificationHistoryDatabase.this.mHistoryFiles) {
                    try {
                        if (this.mNotificationHistory != null) {
                            notificationHistory = this.mNotificationHistory;
                        } else {
                            notificationHistory = new android.app.NotificationHistory();
                        }
                        com.android.server.notification.NotificationHistoryDatabase.readLocked(af, notificationHistory, new com.android.server.notification.NotificationHistoryFilter.Builder().build());
                        if (notificationHistory.removeConversationsFromWrite(this.mPkg, this.mConversationIds)) {
                            com.android.server.notification.NotificationHistoryDatabase.this.writeLocked(af, notificationHistory);
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.notification.NotificationHistoryDatabase.TAG, "Cannot clean up file on conversation removal " + af.getBaseFile().getName(), e);
                    }
                }
            }
        }
    }

    final class RemoveChannelRunnable implements java.lang.Runnable {
        private java.lang.String mChannelId;
        private android.app.NotificationHistory mNotificationHistory;
        private java.lang.String mPkg;

        RemoveChannelRunnable(java.lang.String pkg, java.lang.String channelId) {
            this.mPkg = pkg;
            this.mChannelId = channelId;
        }

        void setNotificationHistory(android.app.NotificationHistory nh) {
            this.mNotificationHistory = nh;
        }

        @Override // java.lang.Runnable
        public void run() {
            android.app.NotificationHistory notificationHistory;
            if (com.android.server.notification.NotificationHistoryDatabase.DEBUG) {
                android.util.Slog.d(com.android.server.notification.NotificationHistoryDatabase.TAG, "RemoveChannelRunnable");
            }
            synchronized (com.android.server.notification.NotificationHistoryDatabase.this.mLock) {
                com.android.server.notification.NotificationHistoryDatabase.this.mBuffer.removeChannelFromWrite(this.mPkg, this.mChannelId);
                for (android.util.AtomicFile af : com.android.server.notification.NotificationHistoryDatabase.this.mHistoryFiles) {
                    try {
                        if (this.mNotificationHistory != null) {
                            notificationHistory = this.mNotificationHistory;
                        } else {
                            notificationHistory = new android.app.NotificationHistory();
                        }
                        com.android.server.notification.NotificationHistoryDatabase.readLocked(af, notificationHistory, new com.android.server.notification.NotificationHistoryFilter.Builder().build());
                        if (notificationHistory.removeChannelFromWrite(this.mPkg, this.mChannelId)) {
                            com.android.server.notification.NotificationHistoryDatabase.this.writeLocked(af, notificationHistory);
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.notification.NotificationHistoryDatabase.TAG, "Cannot clean up file on channel removal " + af.getBaseFile().getName(), e);
                    }
                }
            }
        }
    }
}
