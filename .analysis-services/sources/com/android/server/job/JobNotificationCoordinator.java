package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
class JobNotificationCoordinator {
    private static final java.lang.String TAG = "JobNotificationCoordinator";
    private final java.lang.Object mUijLock = new java.lang.Object();
    private final android.util.ArrayMap<android.content.pm.UserPackage, android.util.SparseSetArray<com.android.server.job.JobServiceContext>> mCurrentAssociations = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<com.android.server.job.JobServiceContext, com.android.server.job.JobNotificationCoordinator.NotificationDetails> mNotificationDetails = new android.util.ArrayMap<>();
    private final android.util.SparseArrayMap<java.lang.String, android.util.IntArray> mUijNotifications = new android.util.SparseArrayMap<>();
    private final android.util.SparseArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> mUijNotificationChannels = new android.util.SparseArrayMap<>();
    private final com.android.server.notification.NotificationManagerInternal mNotificationManagerInternal = (com.android.server.notification.NotificationManagerInternal) com.android.server.LocalServices.getService(com.android.server.notification.NotificationManagerInternal.class);

    private static final class NotificationDetails {
        public final int appPid;
        public final int appUid;
        public final int jobEndNotificationPolicy;
        public final java.lang.String notificationChannel;
        public final int notificationId;
        public final android.content.pm.UserPackage userPackage;

        NotificationDetails(android.content.pm.UserPackage userPackage, int appPid, int appUid, int notificationId, java.lang.String notificationChannel, int jobEndNotificationPolicy) {
            this.userPackage = userPackage;
            this.notificationId = notificationId;
            this.notificationChannel = notificationChannel;
            this.appPid = appPid;
            this.appUid = appUid;
            this.jobEndNotificationPolicy = jobEndNotificationPolicy;
        }
    }

    JobNotificationCoordinator() {
    }

    void enqueueNotification(com.android.server.job.JobServiceContext hostingContext, java.lang.String packageName, int callingPid, int callingUid, int notificationId, android.app.Notification notification, int jobEndNotificationPolicy) {
        android.util.SparseSetArray<com.android.server.job.JobServiceContext> appNotifications;
        validateNotification(packageName, callingUid, notification, jobEndNotificationPolicy);
        com.android.server.job.controllers.JobStatus jobStatus = hostingContext.getRunningJobLocked();
        if (jobStatus == null) {
            android.util.Slog.wtfStack(TAG, "enqueueNotification called with no running job");
            return;
        }
        com.android.server.job.JobNotificationCoordinator.NotificationDetails oldDetails = this.mNotificationDetails.get(hostingContext);
        if (oldDetails == null) {
            if (jobStatus.startedAsUserInitiatedJob) {
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_initial_set_notification_call_required", jobStatus.getUid());
            } else {
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_initial_set_notification_call_optional", jobStatus.getUid());
            }
        } else {
            if (jobStatus.startedAsUserInitiatedJob) {
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_subsequent_set_notification_call_required", jobStatus.getUid());
            } else {
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_subsequent_set_notification_call_optional", jobStatus.getUid());
            }
            if (oldDetails.notificationId != notificationId) {
                removeNotificationAssociation(hostingContext, 0, jobStatus);
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_set_notification_changed_notification_ids", jobStatus.getUid());
            }
        }
        int userId = android.os.UserHandle.getUserId(callingUid);
        if (jobStatus != null && jobStatus.startedAsUserInitiatedJob) {
            notification.flags |= 32768;
            synchronized (this.mUijLock) {
                maybeCreateUijNotificationSetsLocked(userId, packageName);
                android.util.IntArray notificationIds = (android.util.IntArray) this.mUijNotifications.get(userId, packageName);
                if (notificationIds.indexOf(notificationId) == -1) {
                    notificationIds.add(notificationId);
                }
                ((android.util.ArraySet) this.mUijNotificationChannels.get(userId, packageName)).add(notification.getChannelId());
            }
        }
        android.content.pm.UserPackage userPackage = android.content.pm.UserPackage.of(userId, packageName);
        com.android.server.job.JobNotificationCoordinator.NotificationDetails details = new com.android.server.job.JobNotificationCoordinator.NotificationDetails(userPackage, callingPid, callingUid, notificationId, notification.getChannelId(), jobEndNotificationPolicy);
        android.util.SparseSetArray<com.android.server.job.JobServiceContext> appNotifications2 = this.mCurrentAssociations.get(userPackage);
        if (appNotifications2 != null) {
            appNotifications = appNotifications2;
        } else {
            android.util.SparseSetArray<com.android.server.job.JobServiceContext> appNotifications3 = new android.util.SparseSetArray<>();
            this.mCurrentAssociations.put(userPackage, appNotifications3);
            appNotifications = appNotifications3;
        }
        appNotifications.add(notificationId, hostingContext);
        this.mNotificationDetails.put(hostingContext, details);
        this.mNotificationManagerInternal.enqueueNotification(packageName, packageName, callingUid, callingPid, null, notificationId, notification, userId);
    }

    void removeNotificationAssociation(com.android.server.job.JobServiceContext hostingContext, int stopReason, com.android.server.job.controllers.JobStatus completedJob) {
        int notificationId;
        java.lang.String packageName;
        int userId;
        int notificationId2;
        java.lang.String packageName2;
        int userId2;
        com.android.server.job.JobNotificationCoordinator.NotificationDetails details = this.mNotificationDetails.remove(hostingContext);
        if (details == null) {
            return;
        }
        android.util.SparseSetArray<com.android.server.job.JobServiceContext> associations = this.mCurrentAssociations.get(details.userPackage);
        if (associations == null || !associations.remove(details.notificationId, hostingContext)) {
            android.util.Slog.wtf(TAG, "Association data structures not in sync");
            return;
        }
        int userId3 = android.os.UserHandle.getUserId(details.appUid);
        java.lang.String packageName3 = details.userPackage.packageName;
        int notificationId3 = details.notificationId;
        boolean stripUijFlag = true;
        android.util.ArraySet<com.android.server.job.JobServiceContext> associatedContexts = associations.get(notificationId3);
        if (associatedContexts == null || associatedContexts.isEmpty()) {
            if (details.jobEndNotificationPolicy == 1 || stopReason == 11 || stopReason == 13) {
                try {
                    notificationId = notificationId3;
                    packageName = packageName3;
                    userId = userId3;
                    try {
                        this.mNotificationManagerInternal.cancelNotification(packageName3, packageName3, details.appUid, details.appPid, null, notificationId, userId3);
                    } catch (java.lang.SecurityException e) {
                        android.util.Slog.e(TAG, "removeNotificationAssociation failed, probably the app has been uninstalled");
                    }
                } catch (java.lang.SecurityException e2) {
                    notificationId = notificationId3;
                    packageName = packageName3;
                    userId = userId3;
                }
                stripUijFlag = false;
            } else {
                notificationId = notificationId3;
                packageName = packageName3;
                userId = userId3;
            }
        } else {
            stripUijFlag = true ^ isNotificationUsedForAnyUij(userId3, packageName3, notificationId3);
            notificationId = notificationId3;
            packageName = packageName3;
            userId = userId3;
        }
        if (!stripUijFlag) {
            notificationId2 = notificationId;
            packageName2 = packageName;
            userId2 = userId;
        } else {
            notificationId2 = notificationId;
            packageName2 = packageName;
            userId2 = userId;
            this.mNotificationManagerInternal.removeUserInitiatedJobFlagFromNotification(packageName2, notificationId2, userId2);
        }
        if (completedJob != null && completedJob.startedAsUserInitiatedJob) {
            maybeDeleteNotificationIdAssociation(userId2, packageName2, notificationId2);
            maybeDeleteNotificationChannelAssociation(userId2, packageName2, details.notificationChannel);
        }
    }

    boolean isNotificationAssociatedWithAnyUserInitiatedJobs(int notificationId, int userId, java.lang.String packageName) {
        synchronized (this.mUijLock) {
            android.util.IntArray notifications = (android.util.IntArray) this.mUijNotifications.get(userId, packageName);
            if (notifications == null) {
                return false;
            }
            return notifications.indexOf(notificationId) != -1;
        }
    }

    boolean isNotificationChannelAssociatedWithAnyUserInitiatedJobs(java.lang.String notificationChannel, int userId, java.lang.String packageName) {
        synchronized (this.mUijLock) {
            android.util.ArraySet<java.lang.String> channels = (android.util.ArraySet) this.mUijNotificationChannels.get(userId, packageName);
            if (channels == null) {
                return false;
            }
            return channels.contains(notificationChannel);
        }
    }

    private boolean isNotificationUsedForAnyUij(int userId, java.lang.String packageName, int notificationId) {
        android.util.ArraySet<com.android.server.job.JobServiceContext> associatedContexts;
        android.content.pm.UserPackage pkgDetails = android.content.pm.UserPackage.of(userId, packageName);
        android.util.SparseSetArray<com.android.server.job.JobServiceContext> associations = this.mCurrentAssociations.get(pkgDetails);
        if (associations == null || (associatedContexts = associations.get(notificationId)) == null) {
            return false;
        }
        for (int i = associatedContexts.size() - 1; i >= 0; i--) {
            com.android.server.job.controllers.JobStatus jobStatus = associatedContexts.valueAt(i).getRunningJobLocked();
            if (jobStatus != null && jobStatus.startedAsUserInitiatedJob) {
                return true;
            }
        }
        return false;
    }

    private void maybeDeleteNotificationIdAssociation(int userId, java.lang.String packageName, int notificationId) {
        if (isNotificationUsedForAnyUij(userId, packageName, notificationId)) {
            return;
        }
        synchronized (this.mUijLock) {
            android.util.IntArray notifications = (android.util.IntArray) this.mUijNotifications.get(userId, packageName);
            if (notifications != null) {
                notifications.remove(notifications.indexOf(notificationId));
                if (notifications.size() == 0) {
                    this.mUijNotifications.delete(userId, packageName);
                }
            }
        }
    }

    private void maybeDeleteNotificationChannelAssociation(int userId, java.lang.String packageName, java.lang.String notificationChannel) {
        com.android.server.job.controllers.JobStatus jobStatus;
        for (int i = this.mNotificationDetails.size() - 1; i >= 0; i--) {
            com.android.server.job.JobServiceContext jsc = this.mNotificationDetails.keyAt(i);
            com.android.server.job.JobNotificationCoordinator.NotificationDetails details = this.mNotificationDetails.get(jsc);
            if (details != null && android.os.UserHandle.getUserId(details.appUid) == userId && details.userPackage.packageName.equals(packageName) && details.notificationChannel.equals(notificationChannel) && (jobStatus = jsc.getRunningJobLocked()) != null && jobStatus.startedAsUserInitiatedJob) {
                return;
            }
        }
        synchronized (this.mUijLock) {
            android.util.ArraySet<java.lang.String> channels = (android.util.ArraySet) this.mUijNotificationChannels.get(userId, packageName);
            if (channels != null) {
                channels.remove(notificationChannel);
                if (channels.isEmpty()) {
                    this.mUijNotificationChannels.delete(userId, packageName);
                }
            }
        }
    }

    private void maybeCreateUijNotificationSetsLocked(int userId, java.lang.String packageName) {
        if (this.mUijNotifications.get(userId, packageName) == null) {
            this.mUijNotifications.add(userId, packageName, new android.util.IntArray());
        }
        if (this.mUijNotificationChannels.get(userId, packageName) == null) {
            this.mUijNotificationChannels.add(userId, packageName, new android.util.ArraySet());
        }
    }

    private void validateNotification(java.lang.String packageName, int callingUid, android.app.Notification notification, int jobEndNotificationPolicy) {
        if (notification == null) {
            throw new java.lang.NullPointerException("notification");
        }
        if (notification.getSmallIcon() == null) {
            throw new java.lang.IllegalArgumentException("small icon required");
        }
        if (this.mNotificationManagerInternal.getNotificationChannel(packageName, callingUid, notification.getChannelId()) == null) {
            throw new java.lang.IllegalArgumentException("invalid notification channel");
        }
        if (jobEndNotificationPolicy != 0 && jobEndNotificationPolicy != 1) {
            throw new java.lang.IllegalArgumentException("invalid job end notification policy");
        }
    }
}
