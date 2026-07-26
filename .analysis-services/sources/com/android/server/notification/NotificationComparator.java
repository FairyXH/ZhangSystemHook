package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
class NotificationComparator implements java.util.Comparator<com.android.server.notification.NotificationRecord> {
    private final android.content.Context mContext;
    private java.lang.String mDefaultPhoneApp;
    private final com.android.internal.util.NotificationMessagingUtil mMessagingUtil;
    public final java.lang.Object mStateLock = new java.lang.Object();
    private final android.content.BroadcastReceiver mPhoneAppBroadcastReceiver = new com.android.server.notification.NotificationComparator.AnonymousClass1();

    public NotificationComparator(android.content.Context context) {
        this.mContext = context;
        this.mContext.registerReceiver(this.mPhoneAppBroadcastReceiver, new android.content.IntentFilter("android.telecom.action.DEFAULT_DIALER_CHANGED"));
        this.mMessagingUtil = new com.android.internal.util.NotificationMessagingUtil(this.mContext, this.mStateLock);
    }

    @Override // java.util.Comparator
    public int compare(com.android.server.notification.NotificationRecord left, com.android.server.notification.NotificationRecord right) {
        int leftImportance = left.getImportance();
        int rightImportance = right.getImportance();
        boolean isLeftHighImportance = leftImportance >= 3;
        boolean isRightHighImportance = rightImportance >= 3;
        if (isLeftHighImportance != isRightHighImportance) {
            return java.lang.Boolean.compare(isLeftHighImportance, isRightHighImportance) * (-1);
        }
        if (left.getRankingScore() != right.getRankingScore()) {
            return java.lang.Float.compare(left.getRankingScore(), right.getRankingScore()) * (-1);
        }
        boolean leftImportantColorized = isImportantColorized(left);
        boolean rightImportantColorized = isImportantColorized(right);
        if (leftImportantColorized != rightImportantColorized) {
            return java.lang.Boolean.compare(leftImportantColorized, rightImportantColorized) * (-1);
        }
        boolean leftImportantOngoing = isImportantOngoing(left);
        boolean rightImportantOngoing = isImportantOngoing(right);
        if (leftImportantOngoing != rightImportantOngoing) {
            return java.lang.Boolean.compare(leftImportantOngoing, rightImportantOngoing) * (-1);
        }
        boolean leftMessaging = isImportantMessaging(left);
        boolean rightMessaging = isImportantMessaging(right);
        if (leftMessaging != rightMessaging) {
            return java.lang.Boolean.compare(leftMessaging, rightMessaging) * (-1);
        }
        boolean leftPeople = isImportantPeople(left);
        boolean rightPeople = isImportantPeople(right);
        int contactAffinityComparison = java.lang.Float.compare(left.getContactAffinity(), right.getContactAffinity());
        if (leftPeople && rightPeople) {
            if (contactAffinityComparison != 0) {
                return contactAffinityComparison * (-1);
            }
        } else if (leftPeople != rightPeople) {
            return java.lang.Boolean.compare(leftPeople, rightPeople) * (-1);
        }
        boolean leftSystemMax = isSystemMax(left);
        boolean rightSystemMax = isSystemMax(right);
        if (leftSystemMax != rightSystemMax) {
            return java.lang.Boolean.compare(leftSystemMax, rightSystemMax) * (-1);
        }
        if (leftImportance != rightImportance) {
            return java.lang.Integer.compare(leftImportance, rightImportance) * (-1);
        }
        if (contactAffinityComparison != 0) {
            return contactAffinityComparison * (-1);
        }
        int leftPackagePriority = left.getPackagePriority();
        int rightPackagePriority = right.getPackagePriority();
        if (leftPackagePriority != rightPackagePriority) {
            return java.lang.Integer.compare(leftPackagePriority, rightPackagePriority) * (-1);
        }
        int leftPriority = left.getSbn().getNotification().priority;
        int rightPriority = right.getSbn().getNotification().priority;
        return leftPriority != rightPriority ? java.lang.Integer.compare(leftPriority, rightPriority) * (-1) : java.lang.Long.compare(left.getRankingTimeMs(), right.getRankingTimeMs()) * (-1);
    }

    private boolean isImportantColorized(com.android.server.notification.NotificationRecord record) {
        if (record.getImportance() < 2) {
            return false;
        }
        return record.getNotification().isColorized();
    }

    private boolean isImportantOngoing(com.android.server.notification.NotificationRecord record) {
        if (record.getImportance() < 2) {
            return false;
        }
        if (isCallStyle(record)) {
            return true;
        }
        if (record.getNotification().isFgsOrUij()) {
            return isCallCategory(record) || isMediaNotification(record);
        }
        return false;
    }

    protected boolean isImportantPeople(com.android.server.notification.NotificationRecord record) {
        return record.getImportance() >= 2 && record.getContactAffinity() > 0.0f;
    }

    protected boolean isImportantMessaging(com.android.server.notification.NotificationRecord record) {
        return this.mMessagingUtil.isImportantMessaging(record.getSbn(), record.getImportance());
    }

    protected boolean isSystemMax(com.android.server.notification.NotificationRecord record) {
        if (record.getImportance() < 4) {
            return false;
        }
        java.lang.String packageName = record.getSbn().getPackageName();
        return com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName) || "com.android.systemui".equals(packageName);
    }

    private boolean isMediaNotification(com.android.server.notification.NotificationRecord record) {
        return record.getNotification().isMediaNotification();
    }

    private boolean isCallCategory(com.android.server.notification.NotificationRecord record) {
        return record.isCategory("call") && isDefaultPhoneApp(record.getSbn().getPackageName());
    }

    private boolean isCallStyle(com.android.server.notification.NotificationRecord record) {
        return record.getNotification().isStyle(android.app.Notification.CallStyle.class);
    }

    private boolean isDefaultPhoneApp(java.lang.String pkg) {
        if (this.mDefaultPhoneApp == null) {
            android.telecom.TelecomManager telecomm = (android.telecom.TelecomManager) this.mContext.getSystemService("telecom");
            this.mDefaultPhoneApp = telecomm != null ? telecomm.getDefaultDialerPackage() : null;
        }
        return java.util.Objects.equals(pkg, this.mDefaultPhoneApp);
    }

    /* JADX INFO: renamed from: com.android.server.notification.NotificationComparator$1, reason: invalid class name */
    class AnonymousClass1 extends android.content.BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, final android.content.Intent intent) {
            com.android.internal.os.BackgroundThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationComparator$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0(intent);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(android.content.Intent intent) {
            synchronized (com.android.server.notification.NotificationComparator.this.mStateLock) {
                com.android.server.notification.NotificationComparator.this.mDefaultPhoneApp = intent.getStringExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME");
            }
        }
    }
}
