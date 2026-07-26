package com.android.server.servicewatcher;

/* JADX INFO: loaded from: classes3.dex */
public final class CurrentUserServiceSupplier extends android.content.BroadcastReceiver implements com.android.server.servicewatcher.ServiceWatcher.ServiceSupplier<com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo> {
    private static final java.lang.String EXTRA_SERVICE_IS_MULTIUSER = "serviceIsMultiuser";
    private static final java.lang.String EXTRA_SERVICE_VERSION = "serviceVersion";
    private static final java.lang.String NO_MATCH_PACKAGE = "";
    private static final java.lang.String TAG = "CurrentUserServiceSupplier";
    private static final java.util.Comparator<com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo> sBoundServiceInfoComparator = new java.util.Comparator() { // from class: com.android.server.servicewatcher.CurrentUserServiceSupplier$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
            return com.android.server.servicewatcher.CurrentUserServiceSupplier.lambda$static$0((com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo) obj, (com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo) obj2);
        }
    };
    private final android.app.ActivityManagerInternal mActivityManager = (android.app.ActivityManagerInternal) java.util.Objects.requireNonNull((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class));
    private final java.lang.String mCallerPermission;
    private final android.content.Context mContext;
    private final android.content.Intent mIntent;
    private volatile com.android.server.servicewatcher.ServiceWatcher.ServiceChangedListener mListener;
    private final boolean mMatchSystemAppsOnly;
    private final java.lang.String mServicePermission;

    static /* synthetic */ int lambda$static$0(com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo o1, com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        int ret = java.lang.Integer.compare(o1.getVersion(), o2.getVersion());
        if (ret == 0) {
            if (o1.getUserId() != 0 && o2.getUserId() == 0) {
                return -1;
            }
            if (o1.getUserId() == 0 && o2.getUserId() != 0) {
                return 1;
            }
            return ret;
        }
        return ret;
    }

    public static class BoundServiceInfo extends com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo {
        private final android.os.Bundle mMetadata;
        private final int mVersion;

        private static int parseUid(android.content.pm.ResolveInfo resolveInfo) {
            int uid = resolveInfo.serviceInfo.applicationInfo.uid;
            android.os.Bundle metadata = resolveInfo.serviceInfo.metaData;
            if (metadata != null && metadata.getBoolean(com.android.server.servicewatcher.CurrentUserServiceSupplier.EXTRA_SERVICE_IS_MULTIUSER, false)) {
                return android.os.UserHandle.getUid(0, android.os.UserHandle.getAppId(uid));
            }
            return uid;
        }

        private static int parseVersion(android.content.pm.ResolveInfo resolveInfo) {
            if (resolveInfo.serviceInfo.metaData != null) {
                int version = resolveInfo.serviceInfo.metaData.getInt(com.android.server.servicewatcher.CurrentUserServiceSupplier.EXTRA_SERVICE_VERSION, Integer.MIN_VALUE);
                return version;
            }
            return Integer.MIN_VALUE;
        }

        protected BoundServiceInfo(java.lang.String action, android.content.pm.ResolveInfo resolveInfo) {
            this(action, parseUid(resolveInfo), resolveInfo.serviceInfo.getComponentName(), parseVersion(resolveInfo), resolveInfo.serviceInfo.metaData);
        }

        protected BoundServiceInfo(java.lang.String action, int uid, android.content.ComponentName componentName, int version, android.os.Bundle metadata) {
            super(action, uid, componentName);
            this.mVersion = version;
            this.mMetadata = metadata;
        }

        public int getVersion() {
            return this.mVersion;
        }

        public android.os.Bundle getMetadata() {
            return this.mMetadata;
        }

        @Override // com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo
        public java.lang.String toString() {
            return super.toString() + "@" + this.mVersion;
        }
    }

    public static com.android.server.servicewatcher.CurrentUserServiceSupplier createFromConfig(android.content.Context context, java.lang.String action, int enableOverlayResId, int nonOverlayPackageResId) {
        java.lang.String explicitPackage = retrieveExplicitPackage(context, enableOverlayResId, nonOverlayPackageResId);
        return create(context, action, explicitPackage, null, null);
    }

    public static com.android.server.servicewatcher.CurrentUserServiceSupplier create(android.content.Context context, java.lang.String action, java.lang.String explicitPackage, java.lang.String callerPermission, java.lang.String servicePermission) {
        return new com.android.server.servicewatcher.CurrentUserServiceSupplier(context, action, explicitPackage, callerPermission, servicePermission, true);
    }

    public static com.android.server.servicewatcher.CurrentUserServiceSupplier createUnsafeForTestsOnly(android.content.Context context, java.lang.String action, java.lang.String explicitPackage, java.lang.String callerPermission, java.lang.String servicePermission) {
        return new com.android.server.servicewatcher.CurrentUserServiceSupplier(context, action, explicitPackage, callerPermission, servicePermission, false);
    }

    private static java.lang.String retrieveExplicitPackage(android.content.Context context, int enableOverlayResId, int nonOverlayPackageResId) {
        android.content.res.Resources resources = context.getResources();
        boolean enableOverlay = resources.getBoolean(enableOverlayResId);
        if (!enableOverlay) {
            if (android.location.flags.Flags.fixServiceWatcher()) {
                android.util.TypedValue out = new android.util.TypedValue();
                resources.getValue(nonOverlayPackageResId, out, true);
                java.lang.CharSequence explicitPackage = out.coerceToString();
                if (explicitPackage == null) {
                    return "";
                }
                return explicitPackage.toString();
            }
            return resources.getString(nonOverlayPackageResId);
        }
        return null;
    }

    private CurrentUserServiceSupplier(android.content.Context context, java.lang.String action, java.lang.String explicitPackage, java.lang.String callerPermission, java.lang.String servicePermission, boolean matchSystemAppsOnly) {
        this.mContext = context;
        this.mIntent = new android.content.Intent(action);
        if (explicitPackage != null) {
            this.mIntent.setPackage(explicitPackage);
        }
        this.mCallerPermission = callerPermission;
        this.mServicePermission = servicePermission;
        this.mMatchSystemAppsOnly = matchSystemAppsOnly;
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceSupplier
    public boolean hasMatchingService() {
        if (android.location.flags.Flags.fixServiceWatcher() && "".equals(this.mIntent.getPackage())) {
            return false;
        }
        int intentQueryFlags = com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED;
        if (this.mMatchSystemAppsOnly) {
            intentQueryFlags = 786432 | 1048576;
        }
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = this.mContext.getPackageManager().queryIntentServicesAsUser(this.mIntent, intentQueryFlags, 0);
        return !resolveInfos.isEmpty();
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceSupplier
    public void register(com.android.server.servicewatcher.ServiceWatcher.ServiceChangedListener listener) {
        com.android.internal.util.Preconditions.checkState(this.mListener == null);
        this.mListener = listener;
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter.setPriority(1000);
        this.mContext.registerReceiverAsUser(this, android.os.UserHandle.ALL, intentFilter, null, com.android.server.FgThread.getHandler());
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceSupplier
    public void unregister() {
        com.android.internal.util.Preconditions.checkArgument(this.mListener != null);
        this.mListener = null;
        this.mContext.unregisterReceiver(this);
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceSupplier
    public com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo getServiceInfo() {
        if (android.location.flags.Flags.fixServiceWatcher() && "".equals(this.mIntent.getPackage())) {
            return null;
        }
        com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo bestServiceInfo = null;
        int intentQueryFlags = this.mMatchSystemAppsOnly ? 268435584 | 1048576 : 268435584;
        int currentUserId = this.mActivityManager.getCurrentUserId();
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = this.mContext.getPackageManager().queryIntentServicesAsUser(this.mIntent, intentQueryFlags, currentUserId);
        for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
            android.content.pm.ServiceInfo service = (android.content.pm.ServiceInfo) java.util.Objects.requireNonNull(resolveInfo.serviceInfo);
            if (this.mCallerPermission != null && !this.mCallerPermission.equals(service.permission)) {
                android.util.Log.d(TAG, service.getComponentName().flattenToShortString() + " disqualified due to not requiring " + this.mCallerPermission);
            } else {
                com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo serviceInfo = new com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo(this.mIntent.getAction(), resolveInfo);
                if (this.mServicePermission != null && this.mContext.checkPermission(this.mServicePermission, -1, serviceInfo.mUid) != 0) {
                    android.util.Log.d(TAG, serviceInfo.getComponentName().flattenToShortString() + " disqualified due to not holding " + this.mCallerPermission);
                } else if (sBoundServiceInfoComparator.compare(serviceInfo, bestServiceInfo) > 0) {
                    bestServiceInfo = serviceInfo;
                }
            }
        }
        return bestServiceInfo;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0033  */
    @Override // android.content.BroadcastReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onReceive(android.content.Context r5, android.content.Intent r6) {
        /*
            r4 = this;
            java.lang.String r0 = r6.getAction()
            if (r0 != 0) goto L7
            return
        L7:
            java.lang.String r1 = "android.intent.extra.user_handle"
            r2 = -10000(0xffffffffffffd8f0, float:NaN)
            int r1 = r6.getIntExtra(r1, r2)
            if (r1 != r2) goto L12
            return
        L12:
            com.android.server.servicewatcher.ServiceWatcher$ServiceChangedListener r2 = r4.mListener
            if (r2 != 0) goto L17
            return
        L17:
            int r3 = r0.hashCode()
            switch(r3) {
                case 833559602: goto L29;
                case 959232034: goto L1f;
                default: goto L1e;
            }
        L1e:
            goto L33
        L1f:
            java.lang.String r3 = "android.intent.action.USER_SWITCHED"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L1e
            r3 = 0
            goto L34
        L29:
            java.lang.String r3 = "android.intent.action.USER_UNLOCKED"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L1e
            r3 = 1
            goto L34
        L33:
            r3 = -1
        L34:
            switch(r3) {
                case 0: goto L44;
                case 1: goto L38;
                default: goto L37;
            }
        L37:
            goto L48
        L38:
            android.app.ActivityManagerInternal r3 = r4.mActivityManager
            int r3 = r3.getCurrentUserId()
            if (r1 != r3) goto L48
            r2.onServiceChanged()
            goto L48
        L44:
            r2.onServiceChanged()
        L48:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.servicewatcher.CurrentUserServiceSupplier.onReceive(android.content.Context, android.content.Intent):void");
    }
}
