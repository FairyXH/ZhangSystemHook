package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public final class DefaultPermissionGrantPolicy {
    private static final java.lang.String ACTION_TRACK = "com.android.fitness.TRACK";
    private static final java.util.Set<java.lang.String> ACTIVITY_RECOGNITION_PERMISSIONS;
    private static final java.util.Set<java.lang.String> ALWAYS_LOCATION_PERMISSIONS;
    private static final java.lang.String ATTR_CERT = "cert";
    private static final java.lang.String ATTR_FIXED = "fixed";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_PACKAGE = "package";
    private static final java.lang.String ATTR_WHITELISTED = "whitelisted";
    private static final java.lang.String AUDIO_MIME_TYPE = "audio/mpeg";
    private static final java.util.Set<java.lang.String> CALENDAR_PERMISSIONS;
    private static final java.util.Set<java.lang.String> CALL_LOG_PERMISSIONS;
    private static final java.util.Set<java.lang.String> CAMERA_PERMISSIONS;
    private static final java.util.Set<java.lang.String> COARSE_BACKGROUND_LOCATION_PERMISSIONS;
    private static final java.util.Set<java.lang.String> CONTACTS_PERMISSIONS;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_INTENT_QUERY_FLAGS = 794624;
    private static final int DEFAULT_PACKAGE_INFO_QUERY_FLAGS = 536915968;
    private static final java.util.Set<java.lang.String> FINE_LOCATION_PERMISSIONS;
    private static final java.util.Set<java.lang.String> FOREGROUND_LOCATION_PERMISSIONS;
    private static final java.util.Set<java.lang.String> MICROPHONE_PERMISSIONS;
    private static final int MSG_READ_DEFAULT_PERMISSION_EXCEPTIONS = 1;
    private static final java.util.Set<java.lang.String> NEARBY_DEVICES_PERMISSIONS;
    private static final java.util.Set<java.lang.String> NOTIFICATION_PERMISSIONS;
    private static final java.util.Set<java.lang.String> PHONE_PERMISSIONS = new android.util.ArraySet();
    private static final java.util.Set<java.lang.String> SENSORS_PERMISSIONS;
    private static final java.util.Set<java.lang.String> SMS_PERMISSIONS;
    private static final java.util.Set<java.lang.String> STORAGE_PERMISSIONS;
    private static final java.lang.String TAG = "DefaultPermGrantPolicy";
    private static final java.lang.String TAG_EXCEPTION = "exception";
    private static final java.lang.String TAG_EXCEPTIONS = "exceptions";
    private static final java.lang.String TAG_PERMISSION = "permission";
    private final android.content.Context mContext;
    private com.android.server.pm.permission.IDefaultPermissionGrantPolicyExt mDefaultPermissionGrantPolicyExt;
    private com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider mDialerAppPackagesProvider;
    private android.util.ArrayMap<java.lang.String, java.util.List<com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant>> mGrantExceptions;
    private final android.os.Handler mHandler;
    private com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider mLocationExtraPackagesProvider;
    private com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider mLocationPackagesProvider;
    private final android.content.pm.PackageManagerInternal mServiceInternal;
    private com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider mSimCallManagerPackagesProvider;
    private com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider mSmsAppPackagesProvider;
    private com.android.server.pm.permission.LegacyPermissionManagerInternal.SyncAdapterPackagesProvider mSyncAdapterPackagesProvider;
    private com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider mUseOpenWifiAppPackagesProvider;
    private com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider mVoiceInteractionPackagesProvider;
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper NO_PM_CACHE = new com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper() { // from class: com.android.server.pm.permission.DefaultPermissionGrantPolicy.1
        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public int getPermissionFlags(java.lang.String permission, android.content.pm.PackageInfo pkg, android.os.UserHandle user) {
            return com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mContext.getPackageManager().getPermissionFlags(permission, pkg.packageName, user);
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void updatePermissionFlags(java.lang.String permission, android.content.pm.PackageInfo pkg, int flagMask, int flagValues, android.os.UserHandle user) {
            com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mContext.getPackageManager().updatePermissionFlags(permission, pkg.packageName, flagMask, flagValues, user);
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void grantPermission(java.lang.String permission, android.content.pm.PackageInfo pkg, android.os.UserHandle user) {
            com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mContext.getPackageManager().grantRuntimePermission(pkg.packageName, permission, user);
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void revokePermission(java.lang.String permission, android.content.pm.PackageInfo pkg, android.os.UserHandle user) {
            com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mContext.getPackageManager().revokeRuntimePermission(pkg.packageName, permission, user);
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public boolean isGranted(java.lang.String permission, android.content.pm.PackageInfo pkg, android.os.UserHandle user) {
            return com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mContext.createContextAsUser(user, 0).getPackageManager().checkPermission(permission, pkg.packageName) == 0;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String permissionName) {
            if (permissionName == null) {
                return null;
            }
            try {
                return com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mContext.getPackageManager().getPermissionInfo(permissionName, 0);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.w(com.android.server.pm.permission.DefaultPermissionGrantPolicy.TAG, "Permission not found: " + permissionName);
                return null;
            }
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public android.content.pm.PackageInfo getPackageInfo(java.lang.String pkg) {
            if (pkg == null) {
                return null;
            }
            try {
                return com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mContext.getPackageManager().getPackageInfo(pkg, com.android.server.pm.permission.DefaultPermissionGrantPolicy.DEFAULT_PACKAGE_INFO_QUERY_FLAGS);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.e(com.android.server.pm.permission.DefaultPermissionGrantPolicy.TAG, "Package not found: " + pkg);
                return null;
            }
        }
    };
    private final com.android.server.pm.permission.IDefaultPermissionGrantPolicyWrapper mWrapper = new com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrantPolicyWrapper();

    static {
        PHONE_PERMISSIONS.add("android.permission.READ_PHONE_STATE");
        PHONE_PERMISSIONS.add("android.permission.CALL_PHONE");
        PHONE_PERMISSIONS.add("android.permission.READ_CALL_LOG");
        PHONE_PERMISSIONS.add("android.permission.WRITE_CALL_LOG");
        PHONE_PERMISSIONS.add("com.android.voicemail.permission.ADD_VOICEMAIL");
        PHONE_PERMISSIONS.add("android.permission.USE_SIP");
        PHONE_PERMISSIONS.add("android.permission.PROCESS_OUTGOING_CALLS");
        CONTACTS_PERMISSIONS = new android.util.ArraySet();
        CONTACTS_PERMISSIONS.add("android.permission.READ_CONTACTS");
        CONTACTS_PERMISSIONS.add("android.permission.WRITE_CONTACTS");
        CONTACTS_PERMISSIONS.add("android.permission.GET_ACCOUNTS");
        CALL_LOG_PERMISSIONS = new android.util.ArraySet();
        CALL_LOG_PERMISSIONS.add("android.permission.READ_CALL_LOG");
        CALL_LOG_PERMISSIONS.add("android.permission.WRITE_CALL_LOG");
        ALWAYS_LOCATION_PERMISSIONS = new android.util.ArraySet();
        ALWAYS_LOCATION_PERMISSIONS.add("android.permission.ACCESS_FINE_LOCATION");
        ALWAYS_LOCATION_PERMISSIONS.add("android.permission.ACCESS_COARSE_LOCATION");
        ALWAYS_LOCATION_PERMISSIONS.add("android.permission.ACCESS_BACKGROUND_LOCATION");
        FOREGROUND_LOCATION_PERMISSIONS = new android.util.ArraySet();
        FOREGROUND_LOCATION_PERMISSIONS.add("android.permission.ACCESS_FINE_LOCATION");
        FOREGROUND_LOCATION_PERMISSIONS.add("android.permission.ACCESS_COARSE_LOCATION");
        COARSE_BACKGROUND_LOCATION_PERMISSIONS = new android.util.ArraySet();
        COARSE_BACKGROUND_LOCATION_PERMISSIONS.add("android.permission.ACCESS_COARSE_LOCATION");
        COARSE_BACKGROUND_LOCATION_PERMISSIONS.add("android.permission.ACCESS_BACKGROUND_LOCATION");
        FINE_LOCATION_PERMISSIONS = new android.util.ArraySet();
        FINE_LOCATION_PERMISSIONS.add("android.permission.ACCESS_FINE_LOCATION");
        ACTIVITY_RECOGNITION_PERMISSIONS = new android.util.ArraySet();
        ACTIVITY_RECOGNITION_PERMISSIONS.add("android.permission.ACTIVITY_RECOGNITION");
        CALENDAR_PERMISSIONS = new android.util.ArraySet();
        CALENDAR_PERMISSIONS.add("android.permission.READ_CALENDAR");
        CALENDAR_PERMISSIONS.add("android.permission.WRITE_CALENDAR");
        SMS_PERMISSIONS = new android.util.ArraySet();
        SMS_PERMISSIONS.add("android.permission.SEND_SMS");
        SMS_PERMISSIONS.add("android.permission.RECEIVE_SMS");
        SMS_PERMISSIONS.add("android.permission.READ_SMS");
        SMS_PERMISSIONS.add("android.permission.RECEIVE_WAP_PUSH");
        SMS_PERMISSIONS.add("android.permission.RECEIVE_MMS");
        SMS_PERMISSIONS.add("android.permission.READ_CELL_BROADCASTS");
        MICROPHONE_PERMISSIONS = new android.util.ArraySet();
        MICROPHONE_PERMISSIONS.add("android.permission.RECORD_AUDIO");
        CAMERA_PERMISSIONS = new android.util.ArraySet();
        CAMERA_PERMISSIONS.add("android.permission.CAMERA");
        SENSORS_PERMISSIONS = new android.util.ArraySet();
        SENSORS_PERMISSIONS.add("android.permission.BODY_SENSORS");
        SENSORS_PERMISSIONS.add("android.permission.BODY_SENSORS_BACKGROUND");
        STORAGE_PERMISSIONS = new android.util.ArraySet();
        STORAGE_PERMISSIONS.add("android.permission.READ_EXTERNAL_STORAGE");
        STORAGE_PERMISSIONS.add("android.permission.WRITE_EXTERNAL_STORAGE");
        STORAGE_PERMISSIONS.add("android.permission.ACCESS_MEDIA_LOCATION");
        STORAGE_PERMISSIONS.add("android.permission.READ_MEDIA_AUDIO");
        STORAGE_PERMISSIONS.add("android.permission.READ_MEDIA_VIDEO");
        STORAGE_PERMISSIONS.add("android.permission.READ_MEDIA_IMAGES");
        STORAGE_PERMISSIONS.add("android.permission.READ_MEDIA_VISUAL_USER_SELECTED");
        NEARBY_DEVICES_PERMISSIONS = new android.util.ArraySet();
        NEARBY_DEVICES_PERMISSIONS.add("android.permission.BLUETOOTH_ADVERTISE");
        NEARBY_DEVICES_PERMISSIONS.add("android.permission.BLUETOOTH_CONNECT");
        NEARBY_DEVICES_PERMISSIONS.add("android.permission.BLUETOOTH_SCAN");
        NEARBY_DEVICES_PERMISSIONS.add("android.permission.UWB_RANGING");
        NEARBY_DEVICES_PERMISSIONS.add("android.permission.NEARBY_WIFI_DEVICES");
        NOTIFICATION_PERMISSIONS = new android.util.ArraySet();
        NOTIFICATION_PERMISSIONS.add("android.permission.POST_NOTIFICATIONS");
    }

    DefaultPermissionGrantPolicy(android.content.Context context) {
        this.mContext = context;
        android.os.HandlerThread handlerThread = new com.android.server.ServiceThread(TAG, 10, true);
        handlerThread.start();
        this.mHandler = new android.os.Handler(handlerThread.getLooper()) { // from class: com.android.server.pm.permission.DefaultPermissionGrantPolicy.2
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 1) {
                    synchronized (com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mLock) {
                        if (com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mGrantExceptions == null) {
                            com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mGrantExceptions = com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.readDefaultPermissionExceptionsLocked(com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE);
                        }
                    }
                }
            }
        };
        this.mServiceInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mDefaultPermissionGrantPolicyExt = (com.android.server.pm.permission.IDefaultPermissionGrantPolicyExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.permission.IDefaultPermissionGrantPolicyExt.class).create();
    }

    public void setLocationPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
        synchronized (this.mLock) {
            this.mLocationPackagesProvider = provider;
        }
    }

    public void setLocationExtraPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
        synchronized (this.mLock) {
            this.mLocationExtraPackagesProvider = provider;
        }
    }

    public void setVoiceInteractionPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
        synchronized (this.mLock) {
            this.mVoiceInteractionPackagesProvider = provider;
        }
    }

    public void setSmsAppPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
        synchronized (this.mLock) {
            this.mSmsAppPackagesProvider = provider;
        }
    }

    public void setDialerAppPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
        synchronized (this.mLock) {
            this.mDialerAppPackagesProvider = provider;
        }
    }

    public void setSimCallManagerPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
        synchronized (this.mLock) {
            this.mSimCallManagerPackagesProvider = provider;
        }
    }

    public void setUseOpenWifiAppPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
        synchronized (this.mLock) {
            this.mUseOpenWifiAppPackagesProvider = provider;
        }
    }

    public void setSyncAdapterPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.SyncAdapterPackagesProvider provider) {
        synchronized (this.mLock) {
            this.mSyncAdapterPackagesProvider = provider;
        }
    }

    public void grantDefaultPermissions(int userId) {
        com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache pm = new com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache();
        grantPermissionsToSysComponentsAndPrivApps(pm, userId);
        grantDefaultSystemHandlerPermissions(pm, userId);
        grantSignatureAppsNotificationPermissions(pm, userId);
        grantDefaultPermissionExceptions(pm, userId);
        pm.apply();
    }

    private void grantSignatureAppsNotificationPermissions(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, int userId) {
        android.util.Log.i(TAG, "Granting Notification permissions to platform signature apps for user " + userId);
        java.util.List<android.content.pm.PackageInfo> packages = this.mContext.getPackageManager().getInstalledPackagesAsUser(DEFAULT_PACKAGE_INFO_QUERY_FLAGS, 0);
        for (android.content.pm.PackageInfo pkg : packages) {
            if (pkg != null && pkg.applicationInfo.isSystemApp() && pkg.applicationInfo.isSignedWithPlatformKey()) {
                grantRuntimePermissionsForSystemPackage(pm, userId, pkg, NOTIFICATION_PERMISSIONS);
            }
        }
    }

    private void grantRuntimePermissionsForSystemPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, int userId, android.content.pm.PackageInfo pkg) {
        grantRuntimePermissionsForSystemPackage(pm, userId, pkg, null);
    }

    private void grantRuntimePermissionsForSystemPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, int userId, android.content.pm.PackageInfo pkg, java.util.Set<java.lang.String> filterPermissions) {
        if (com.android.internal.util.ArrayUtils.isEmpty(pkg.requestedPermissions)) {
            return;
        }
        java.util.Set<java.lang.String> permissions = new android.util.ArraySet<>();
        for (java.lang.String permission : pkg.requestedPermissions) {
            android.content.pm.PermissionInfo perm = pm.getPermissionInfo(permission);
            if (perm != null && ((filterPermissions == null || filterPermissions.contains(permission)) && perm.isRuntime())) {
                permissions.add(permission);
            }
        }
        if (!permissions.isEmpty()) {
            grantRuntimePermissions(pm, pkg, permissions, true, userId);
        }
    }

    public void scheduleReadDefaultPermissionExceptions() {
        this.mHandler.sendEmptyMessage(1);
    }

    private void grantPermissionsToSysComponentsAndPrivApps(com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache pm, int userId) {
        android.util.Log.i(TAG, "Granting permissions to platform components for user " + userId);
        java.util.List<android.content.pm.PackageInfo> packages = this.mContext.getPackageManager().getInstalledPackagesAsUser(DEFAULT_PACKAGE_INFO_QUERY_FLAGS, 0);
        for (android.content.pm.PackageInfo pkg : packages) {
            if (pkg != null) {
                pm.addPackageInfo(pkg.packageName, pkg);
                if (pm.isSysComponentOrPersistentPlatformSignedPrivApp(pkg) && doesPackageSupportRuntimePermissions(pkg) && !com.android.internal.util.ArrayUtils.isEmpty(pkg.requestedPermissions)) {
                    grantRuntimePermissionsForSystemPackage(pm, userId, pkg);
                }
            }
        }
        for (android.content.pm.PackageInfo pkg2 : packages) {
            if (pkg2 != null && doesPackageSupportRuntimePermissions(pkg2) && !com.android.internal.util.ArrayUtils.isEmpty(pkg2.requestedPermissions) && pm.isGranted("android.permission.READ_PRIVILEGED_PHONE_STATE", pkg2, android.os.UserHandle.of(userId)) && pm.isGranted("android.permission.READ_PHONE_STATE", pkg2, android.os.UserHandle.of(userId)) && !pm.isSysComponentOrPersistentPlatformSignedPrivApp(pkg2)) {
                pm.updatePermissionFlags("android.permission.READ_PHONE_STATE", pkg2, 16, 0, android.os.UserHandle.of(userId));
            }
        }
    }

    @java.lang.SafeVarargs
    private final void grantIgnoringSystemPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String packageName, int userId, java.util.Set<java.lang.String>... permissionGroups) {
        grantPermissionsToPackage(pm, packageName, userId, true, true, permissionGroups);
    }

    @java.lang.SafeVarargs
    private final void grantSystemFixedPermissionsToSystemPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String packageName, int userId, java.util.Set<java.lang.String>... permissionGroups) {
        grantPermissionsToSystemPackage(pm, packageName, userId, true, permissionGroups);
    }

    @java.lang.SafeVarargs
    private final void grantPermissionsToSystemPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String packageName, int userId, java.util.Set<java.lang.String>... permissionGroups) {
        grantPermissionsToSystemPackage(pm, packageName, userId, false, permissionGroups);
    }

    @java.lang.SafeVarargs
    private final void grantPermissionsToSystemPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String packageName, int userId, boolean systemFixed, java.util.Set<java.lang.String>... permissionGroups) {
        if (!pm.isSystemPackage(packageName)) {
            return;
        }
        grantPermissionsToPackage(pm, pm.getSystemPackageInfo(packageName), userId, systemFixed, false, true, permissionGroups);
    }

    @java.lang.SafeVarargs
    private final void grantPermissionsToPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String packageName, int userId, boolean ignoreSystemPackage, boolean whitelistRestrictedPermissions, java.util.Set<java.lang.String>... permissionGroups) {
        grantPermissionsToPackage(pm, pm.getPackageInfo(packageName), userId, false, ignoreSystemPackage, whitelistRestrictedPermissions, permissionGroups);
    }

    @java.lang.SafeVarargs
    private final void grantPermissionsToPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, android.content.pm.PackageInfo packageInfo, int userId, boolean systemFixed, boolean ignoreSystemPackage, boolean whitelistRestrictedPermissions, java.util.Set<java.lang.String>... permissionGroups) {
        if (packageInfo != null && doesPackageSupportRuntimePermissions(packageInfo)) {
            for (java.util.Set<java.lang.String> permissionGroup : permissionGroups) {
                grantRuntimePermissions(pm, packageInfo, permissionGroup, systemFixed, ignoreSystemPackage, whitelistRestrictedPermissions, userId);
            }
        }
    }

    private void grantDefaultSystemHandlerPermissions(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, int userId) {
        com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider locationPackagesProvider;
        com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider locationExtraPackagesProvider;
        com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider voiceInteractionPackagesProvider;
        com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider smsAppPackagesProvider;
        com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider dialerAppPackagesProvider;
        com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider simCallManagerPackagesProvider;
        com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider useOpenWifiAppPackagesProvider;
        com.android.server.pm.permission.LegacyPermissionManagerInternal.SyncAdapterPackagesProvider syncAdapterPackagesProvider;
        java.lang.String[] simCallManagerPackageNames;
        java.lang.String browserPackage;
        int i;
        int i2;
        char c;
        android.util.Log.i(TAG, "Granting permissions to default platform handlers for user " + userId);
        synchronized (this.mLock) {
            locationPackagesProvider = this.mLocationPackagesProvider;
            locationExtraPackagesProvider = this.mLocationExtraPackagesProvider;
            voiceInteractionPackagesProvider = this.mVoiceInteractionPackagesProvider;
            smsAppPackagesProvider = this.mSmsAppPackagesProvider;
            dialerAppPackagesProvider = this.mDialerAppPackagesProvider;
            simCallManagerPackagesProvider = this.mSimCallManagerPackagesProvider;
            useOpenWifiAppPackagesProvider = this.mUseOpenWifiAppPackagesProvider;
            syncAdapterPackagesProvider = this.mSyncAdapterPackagesProvider;
        }
        java.lang.String[] voiceInteractPackageNames = voiceInteractionPackagesProvider != null ? voiceInteractionPackagesProvider.getPackages(userId) : null;
        java.lang.String[] locationPackageNames = locationPackagesProvider != null ? locationPackagesProvider.getPackages(userId) : null;
        java.lang.String[] locationExtraPackageNames = locationExtraPackagesProvider != null ? locationExtraPackagesProvider.getPackages(userId) : null;
        java.lang.String[] smsAppPackageNames = smsAppPackagesProvider != null ? smsAppPackagesProvider.getPackages(userId) : null;
        java.lang.String[] dialerAppPackageNames = dialerAppPackagesProvider != null ? dialerAppPackagesProvider.getPackages(userId) : null;
        java.lang.String[] simCallManagerPackageNames2 = simCallManagerPackagesProvider != null ? simCallManagerPackagesProvider.getPackages(userId) : null;
        java.lang.String[] useOpenWifiAppPackageNames = useOpenWifiAppPackagesProvider != null ? useOpenWifiAppPackagesProvider.getPackages(userId) : null;
        java.lang.String[] contactsSyncAdapterPackages = syncAdapterPackagesProvider != null ? syncAdapterPackagesProvider.getPackages("com.android.contacts", userId) : null;
        java.lang.String[] calendarSyncAdapterPackages = syncAdapterPackagesProvider != null ? syncAdapterPackagesProvider.getPackages("com.android.calendar", userId) : null;
        grantSystemFixedPermissionsToSystemPackage(pm, this.mContext.getPackageManager().getPermissionControllerPackageName(), userId, NOTIFICATION_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, (java.lang.String) com.android.internal.util.ArrayUtils.firstOrNull(getKnownPackages(2, userId)), userId, STORAGE_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        java.lang.String verifier = (java.lang.String) com.android.internal.util.ArrayUtils.firstOrNull(getKnownPackages(4, userId));
        grantSystemFixedPermissionsToSystemPackage(pm, verifier, userId, STORAGE_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, verifier, userId, PHONE_PERMISSIONS, SMS_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        java.lang.String setupWizardPackage = (java.lang.String) com.android.internal.util.ArrayUtils.firstOrNull(getKnownPackages(1, userId));
        grantPermissionsToSystemPackage(pm, setupWizardPackage, userId, PHONE_PERMISSIONS, CONTACTS_PERMISSIONS, ALWAYS_LOCATION_PERMISSIONS, CAMERA_PERMISSIONS, NEARBY_DEVICES_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, setupWizardPackage, userId, NOTIFICATION_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, getDefaultSearchSelectorPackage(), userId, NOTIFICATION_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, getDefaultCaptivePortalLoginPackage(), userId, NOTIFICATION_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, getDefaultDockManagerPackage(), userId, NOTIFICATION_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, "android.media.action.IMAGE_CAPTURE", userId), userId, CAMERA_PERMISSIONS, MICROPHONE_PERMISSIONS, STORAGE_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, "android.provider.MediaStore.RECORD_SOUND", userId), userId, MICROPHONE_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, getDefaultProviderAuthorityPackage("media", userId), userId, STORAGE_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, getDefaultProviderAuthorityPackage("downloads", userId), userId, STORAGE_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, "android.intent.action.VIEW_DOWNLOADS", userId), userId, STORAGE_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, getDefaultProviderAuthorityPackage("com.android.externalstorage.documents", userId), userId, STORAGE_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, "android.credentials.INSTALL", userId), userId, STORAGE_PERMISSIONS);
        if (dialerAppPackageNames == null) {
            java.lang.String dialerPackage = getDefaultSystemHandlerActivityPackage(pm, "android.intent.action.DIAL", userId);
            grantDefaultPermissionsToDefaultSystemDialerApp(pm, dialerPackage, userId);
        } else {
            for (java.lang.String dialerAppPackageName : dialerAppPackageNames) {
                grantDefaultPermissionsToDefaultSystemDialerApp(pm, dialerAppPackageName, userId);
            }
        }
        if (simCallManagerPackageNames2 != null) {
            int length = simCallManagerPackageNames2.length;
            int i3 = 0;
            while (i3 < length) {
                int i4 = length;
                java.lang.String simCallManagerPackageName = simCallManagerPackageNames2[i3];
                grantDefaultPermissionsToDefaultSystemSimCallManager(pm, simCallManagerPackageName, userId);
                i3++;
                length = i4;
            }
        }
        if (useOpenWifiAppPackageNames == null) {
            simCallManagerPackageNames = simCallManagerPackageNames2;
        } else {
            int length2 = useOpenWifiAppPackageNames.length;
            simCallManagerPackageNames = simCallManagerPackageNames2;
            int i5 = 0;
            while (i5 < length2) {
                int i6 = length2;
                java.lang.String useOpenWifiPackageName = useOpenWifiAppPackageNames[i5];
                grantDefaultPermissionsToDefaultSystemUseOpenWifiApp(pm, useOpenWifiPackageName, userId);
                i5++;
                length2 = i6;
            }
        }
        if (smsAppPackageNames == null) {
            java.lang.String smsPackage = getDefaultSystemHandlerActivityPackageForCategory(pm, "android.intent.category.APP_MESSAGING", userId);
            grantDefaultPermissionsToDefaultSystemSmsApp(pm, smsPackage, userId);
        } else {
            int length3 = smsAppPackageNames.length;
            int i7 = 0;
            while (i7 < length3) {
                int i8 = length3;
                java.lang.String smsPackage2 = smsAppPackageNames[i7];
                grantDefaultPermissionsToDefaultSystemSmsApp(pm, smsPackage2, userId);
                i7++;
                length3 = i8;
            }
        }
        grantSystemFixedPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, "android.provider.Telephony.SMS_CB_RECEIVED", userId), userId, SMS_PERMISSIONS, NEARBY_DEVICES_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerServicePackage(pm, "android.provider.Telephony.SMS_CARRIER_PROVISION", userId), userId, SMS_PERMISSIONS);
        java.util.Set<java.lang.String> toBeGrantContacts = this.mDefaultPermissionGrantPolicyExt.hookGrantDefaultSystemHandlerPermissions(CONTACTS_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackageForCategory(pm, "android.intent.category.APP_CALENDAR", userId), userId, CALENDAR_PERMISSIONS, toBeGrantContacts, NOTIFICATION_PERMISSIONS);
        java.lang.String calendarProvider = getDefaultProviderAuthorityPackage("com.android.calendar", userId);
        grantPermissionsToSystemPackage(pm, calendarProvider, userId, CONTACTS_PERMISSIONS, STORAGE_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, calendarProvider, userId, CALENDAR_PERMISSIONS);
        if (calendarSyncAdapterPackages != null) {
            grantPermissionToEachSystemPackage(pm, getHeadlessSyncAdapterPackages(pm, calendarSyncAdapterPackages, userId), userId, CALENDAR_PERMISSIONS);
        }
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackageForCategory(pm, "android.intent.category.APP_CONTACTS", userId), userId, CONTACTS_PERMISSIONS, PHONE_PERMISSIONS);
        if (contactsSyncAdapterPackages != null) {
            grantPermissionToEachSystemPackage(pm, getHeadlessSyncAdapterPackages(pm, contactsSyncAdapterPackages, userId), userId, CONTACTS_PERMISSIONS);
        }
        java.lang.String contactsProviderPackage = getDefaultProviderAuthorityPackage("com.android.contacts", userId);
        grantSystemFixedPermissionsToSystemPackage(pm, contactsProviderPackage, userId, CONTACTS_PERMISSIONS, PHONE_PERMISSIONS, CALL_LOG_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, contactsProviderPackage, userId, STORAGE_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, "android.app.action.PROVISION_MANAGED_DEVICE", userId), userId, CONTACTS_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive", 0)) {
            grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackageForCategory(pm, "android.intent.category.APP_MAPS", userId), userId, FOREGROUND_LOCATION_PERMISSIONS);
        }
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackageForCategory(pm, "android.intent.category.APP_EMAIL", userId), userId, CONTACTS_PERMISSIONS, CALENDAR_PERMISSIONS);
        java.lang.String browserPackage2 = (java.lang.String) com.android.internal.util.ArrayUtils.firstOrNull(getKnownPackages(5, userId));
        if (browserPackage2 == null) {
            java.lang.String browserPackage3 = getDefaultSystemHandlerActivityPackageForCategory(pm, "android.intent.category.APP_BROWSER", userId);
            if (pm.isSystemPackage(browserPackage3)) {
                browserPackage = browserPackage3;
            } else {
                browserPackage = null;
            }
        } else {
            browserPackage = browserPackage2;
        }
        grantPermissionsToPackage(pm, browserPackage, userId, false, true, FOREGROUND_LOCATION_PERMISSIONS);
        int i9 = 8;
        char c2 = 6;
        if (voiceInteractPackageNames == null) {
            i = 8;
            i2 = 5;
        } else {
            int length4 = voiceInteractPackageNames.length;
            int i10 = 0;
            while (i10 < length4) {
                java.lang.String voiceInteractPackageName = voiceInteractPackageNames[i10];
                java.util.Set<java.lang.String>[] setArr = new java.util.Set[i9];
                setArr[0] = CONTACTS_PERMISSIONS;
                setArr[1] = CALENDAR_PERMISSIONS;
                setArr[2] = MICROPHONE_PERMISSIONS;
                setArr[3] = PHONE_PERMISSIONS;
                setArr[4] = SMS_PERMISSIONS;
                setArr[5] = COARSE_BACKGROUND_LOCATION_PERMISSIONS;
                setArr[c2] = NEARBY_DEVICES_PERMISSIONS;
                setArr[7] = NOTIFICATION_PERMISSIONS;
                grantPermissionsToSystemPackage(pm, voiceInteractPackageName, userId, setArr);
                revokeRuntimePermissions(pm, voiceInteractPackageName, FINE_LOCATION_PERMISSIONS, false, userId);
                i10++;
                i9 = i9;
                length4 = length4;
                c2 = 6;
            }
            i = i9;
            i2 = 5;
        }
        if (android.app.ActivityManager.isLowRamDeviceStatic()) {
            java.lang.String defaultSystemHandlerActivityPackage = getDefaultSystemHandlerActivityPackage(pm, "android.search.action.GLOBAL_SEARCH", userId);
            java.util.Set<java.lang.String>[] setArr2 = new java.util.Set[i2];
            setArr2[0] = MICROPHONE_PERMISSIONS;
            setArr2[1] = ALWAYS_LOCATION_PERMISSIONS;
            setArr2[2] = NOTIFICATION_PERMISSIONS;
            setArr2[3] = PHONE_PERMISSIONS;
            setArr2[4] = CALENDAR_PERMISSIONS;
            grantPermissionsToSystemPackage(pm, defaultSystemHandlerActivityPackage, userId, setArr2);
        }
        android.content.Intent voiceRecoIntent = new android.content.Intent("android.speech.RecognitionService").addCategory("android.intent.category.DEFAULT");
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerServicePackage(pm, voiceRecoIntent, userId), userId, MICROPHONE_PERMISSIONS);
        if (locationPackageNames != null) {
            int length5 = locationPackageNames.length;
            int i11 = 0;
            while (i11 < length5) {
                java.lang.String packageName = locationPackageNames[i11];
                java.util.Set<java.lang.String>[] setArr3 = new java.util.Set[10];
                setArr3[0] = CONTACTS_PERMISSIONS;
                setArr3[1] = CALENDAR_PERMISSIONS;
                setArr3[2] = MICROPHONE_PERMISSIONS;
                setArr3[3] = PHONE_PERMISSIONS;
                setArr3[4] = SMS_PERMISSIONS;
                setArr3[i2] = CAMERA_PERMISSIONS;
                setArr3[6] = SENSORS_PERMISSIONS;
                setArr3[7] = STORAGE_PERMISSIONS;
                setArr3[i] = NEARBY_DEVICES_PERMISSIONS;
                setArr3[9] = NOTIFICATION_PERMISSIONS;
                grantPermissionsToSystemPackage(pm, packageName, userId, setArr3);
                grantSystemFixedPermissionsToSystemPackage(pm, packageName, userId, ALWAYS_LOCATION_PERMISSIONS, ACTIVITY_RECOGNITION_PERMISSIONS);
                i11++;
                i2 = 5;
            }
        }
        if (locationExtraPackageNames != null) {
            for (java.lang.String packageName2 : locationExtraPackageNames) {
                grantPermissionsToSystemPackage(pm, packageName2, userId, ALWAYS_LOCATION_PERMISSIONS, NEARBY_DEVICES_PERMISSIONS);
                grantSystemFixedPermissionsToSystemPackage(pm, packageName2, userId, ACTIVITY_RECOGNITION_PERMISSIONS);
            }
        }
        android.content.Intent musicIntent = new android.content.Intent("android.intent.action.VIEW").addCategory("android.intent.category.DEFAULT").setDataAndType(android.net.Uri.fromFile(new java.io.File("foo.mp3")), AUDIO_MIME_TYPE);
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, musicIntent, userId), userId, STORAGE_PERMISSIONS);
        android.content.Intent homeIntent = new android.content.Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME").addCategory("android.intent.category.LAUNCHER_APP");
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, homeIntent, userId), userId, ALWAYS_LOCATION_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch", 0)) {
            java.lang.String wearPackage = getDefaultSystemHandlerActivityPackageForCategory(pm, "android.intent.category.HOME_MAIN", userId);
            grantPermissionsToSystemPackage(pm, wearPackage, userId, CONTACTS_PERMISSIONS, MICROPHONE_PERMISSIONS, ALWAYS_LOCATION_PERMISSIONS);
            grantSystemFixedPermissionsToSystemPackage(pm, wearPackage, userId, PHONE_PERMISSIONS, ACTIVITY_RECOGNITION_PERMISSIONS);
            if (this.mContext.getResources().getBoolean(android.R.bool.config_supportMicNearUltrasound)) {
                android.util.Log.d(TAG, "Wear: Skipping permission grant for Default fitness tracker app : " + wearPackage);
                c = 0;
            } else {
                c = 0;
                grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, ACTION_TRACK, userId), userId, SENSORS_PERMISSIONS);
            }
        } else {
            c = 0;
        }
        java.util.Set<java.lang.String>[] setArr4 = new java.util.Set[2];
        setArr4[c] = ALWAYS_LOCATION_PERMISSIONS;
        setArr4[1] = NOTIFICATION_PERMISSIONS;
        grantSystemFixedPermissionsToSystemPackage(pm, "com.android.printspooler", userId, setArr4);
        java.lang.String defaultSystemHandlerActivityPackage2 = getDefaultSystemHandlerActivityPackage(pm, "android.telephony.action.EMERGENCY_ASSISTANCE", userId);
        java.util.Set<java.lang.String>[] setArr5 = new java.util.Set[2];
        setArr5[c] = CONTACTS_PERMISSIONS;
        setArr5[1] = PHONE_PERMISSIONS;
        grantSystemFixedPermissionsToSystemPackage(pm, defaultSystemHandlerActivityPackage2, userId, setArr5);
        android.content.Intent nfcTagIntent = new android.content.Intent("android.intent.action.VIEW").setType("vnd.android.cursor.item/ndef_msg");
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, nfcTagIntent, userId), userId, CONTACTS_PERMISSIONS, PHONE_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, "android.os.storage.action.MANAGE_STORAGE", userId), userId, STORAGE_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, getDefaultCompanionDeviceManagerPackage(), userId, ALWAYS_LOCATION_PERMISSIONS, NEARBY_DEVICES_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, getDefaultSystemHandlerActivityPackage(pm, "android.intent.action.RINGTONE_PICKER", userId), userId, STORAGE_PERMISSIONS);
        java.lang.String[] knownPackages = getKnownPackages(6, userId);
        int length6 = knownPackages.length;
        int i12 = 0;
        while (i12 < length6) {
            java.lang.String textClassifierPackage = knownPackages[i12];
            grantPermissionsToSystemPackage(pm, textClassifierPackage, userId, COARSE_BACKGROUND_LOCATION_PERMISSIONS, CONTACTS_PERMISSIONS);
            i12++;
            knownPackages = knownPackages;
            voiceRecoIntent = voiceRecoIntent;
        }
        grantSystemFixedPermissionsToSystemPackage(pm, com.android.server.backup.UserBackupManagerService.SHARED_BACKUP_AGENT_PACKAGE, userId, STORAGE_PERMISSIONS);
        grantSystemFixedPermissionsToSystemPackage(pm, "com.android.bluetoothmidiservice", userId, NEARBY_DEVICES_PERMISSIONS);
        grantPermissionsToSystemPackage(pm, getDefaultSystemHandlerServicePackage(pm, "android.adservices.AD_SERVICES_COMMON_SERVICE", userId), userId, NOTIFICATION_PERMISSIONS);
    }

    private java.lang.String getDefaultSystemHandlerActivityPackageForCategory(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String category, int userId) {
        return getDefaultSystemHandlerActivityPackage(pm, new android.content.Intent("android.intent.action.MAIN").addCategory(category), userId);
    }

    private java.lang.String getDefaultSearchSelectorPackage() {
        return this.mContext.getString(android.R.string.config_defaultWellbeingPackage);
    }

    private java.lang.String getDefaultCaptivePortalLoginPackage() {
        return this.mContext.getString(android.R.string.config_defaultDisplayCompatHostActivity);
    }

    private java.lang.String getDefaultDockManagerPackage() {
        return this.mContext.getString(android.R.string.config_defaultNetworkRecommendationProviderPackage);
    }

    private java.lang.String getDefaultCompanionDeviceManagerPackage() {
        return this.mContext.getString(android.R.string.config_customVpnConfirmDialogComponent);
    }

    @java.lang.SafeVarargs
    private final void grantPermissionToEachSystemPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.util.ArrayList<java.lang.String> packages, int userId, java.util.Set<java.lang.String>... permissions) {
        if (packages == null) {
            return;
        }
        int count = packages.size();
        for (int i = 0; i < count; i++) {
            grantPermissionsToSystemPackage(pm, packages.get(i), userId, permissions);
        }
    }

    private java.lang.String[] getKnownPackages(int knownPkgId, int userId) {
        return this.mServiceInternal.getKnownPackageNames(knownPkgId, userId);
    }

    private void grantDefaultPermissionsToDefaultSystemDialerApp(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String dialerPackage, int userId) {
        if (dialerPackage == null) {
            return;
        }
        boolean isPhonePermFixed = this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch", 0);
        if (isPhonePermFixed) {
            grantSystemFixedPermissionsToSystemPackage(pm, dialerPackage, userId, PHONE_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        } else {
            grantPermissionsToSystemPackage(pm, dialerPackage, userId, PHONE_PERMISSIONS);
        }
        grantPermissionsToSystemPackage(pm, dialerPackage, userId, CONTACTS_PERMISSIONS, SMS_PERMISSIONS, MICROPHONE_PERMISSIONS, CAMERA_PERMISSIONS, NOTIFICATION_PERMISSIONS);
        boolean isAndroidAutomotive = this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive", 0);
        if (isAndroidAutomotive) {
            grantPermissionsToSystemPackage(pm, dialerPackage, userId, NEARBY_DEVICES_PERMISSIONS);
        }
    }

    private void grantDefaultPermissionsToDefaultSystemSmsApp(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String smsPackage, int userId) {
        grantPermissionsToSystemPackage(pm, smsPackage, userId, PHONE_PERMISSIONS, CONTACTS_PERMISSIONS, SMS_PERMISSIONS, STORAGE_PERMISSIONS, MICROPHONE_PERMISSIONS, CAMERA_PERMISSIONS, NOTIFICATION_PERMISSIONS);
    }

    private void grantDefaultPermissionsToDefaultSystemUseOpenWifiApp(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String useOpenWifiPackage, int userId) {
        grantPermissionsToSystemPackage(pm, useOpenWifiPackage, userId, ALWAYS_LOCATION_PERMISSIONS);
    }

    public void grantDefaultPermissionsToDefaultUseOpenWifiApp(java.lang.String packageName, int userId) {
        android.util.Log.i(TAG, "Granting permissions to default Use Open WiFi app for user:" + userId);
        grantIgnoringSystemPackage(this.NO_PM_CACHE, packageName, userId, ALWAYS_LOCATION_PERMISSIONS);
    }

    public void grantDefaultPermissionsToDefaultSimCallManager(java.lang.String packageName, int userId) {
        grantDefaultPermissionsToDefaultSimCallManager(this.NO_PM_CACHE, packageName, userId);
    }

    private void grantDefaultPermissionsToDefaultSimCallManager(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String packageName, int userId) {
        if (packageName == null) {
            return;
        }
        android.util.Log.i(TAG, "Granting permissions to sim call manager for user:" + userId);
        grantPermissionsToPackage(pm, packageName, userId, false, true, PHONE_PERMISSIONS, MICROPHONE_PERMISSIONS);
    }

    private void grantDefaultPermissionsToDefaultSystemSimCallManager(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String packageName, int userId) {
        if (pm.isSystemPackage(packageName)) {
            grantDefaultPermissionsToDefaultSimCallManager(pm, packageName, userId);
        }
    }

    public void grantDefaultPermissionsToEnabledCarrierApps(java.lang.String[] packageNames, int userId) {
        android.util.Log.i(TAG, "Granting permissions to enabled carrier apps for user:" + userId);
        if (packageNames == null) {
            return;
        }
        for (java.lang.String packageName : packageNames) {
            grantPermissionsToSystemPackage(this.NO_PM_CACHE, packageName, userId, PHONE_PERMISSIONS, ALWAYS_LOCATION_PERMISSIONS, SMS_PERMISSIONS);
        }
    }

    public void grantDefaultPermissionsToEnabledImsServices(java.lang.String[] packageNames, int userId) {
        android.util.Log.i(TAG, "Granting permissions to enabled ImsServices for user:" + userId);
        if (packageNames == null) {
            return;
        }
        for (java.lang.String packageName : packageNames) {
            grantPermissionsToSystemPackage(this.NO_PM_CACHE, packageName, userId, PHONE_PERMISSIONS, MICROPHONE_PERMISSIONS, ALWAYS_LOCATION_PERMISSIONS, CAMERA_PERMISSIONS, CONTACTS_PERMISSIONS);
        }
    }

    public void grantDefaultPermissionsToEnabledTelephonyDataServices(java.lang.String[] packageNames, int userId) {
        android.util.Log.i(TAG, "Granting permissions to enabled data services for user:" + userId);
        if (packageNames == null) {
            return;
        }
        for (java.lang.String packageName : packageNames) {
            grantSystemFixedPermissionsToSystemPackage(this.NO_PM_CACHE, packageName, userId, PHONE_PERMISSIONS, ALWAYS_LOCATION_PERMISSIONS);
        }
    }

    public void revokeDefaultPermissionsFromDisabledTelephonyDataServices(java.lang.String[] packageNames, int userId) {
        android.util.Log.i(TAG, "Revoking permissions from disabled data services for user:" + userId);
        if (packageNames == null) {
            return;
        }
        for (java.lang.String packageName : packageNames) {
            android.content.pm.PackageInfo pkg = this.NO_PM_CACHE.getSystemPackageInfo(packageName);
            if (this.NO_PM_CACHE.isSystemPackage(pkg) && doesPackageSupportRuntimePermissions(pkg)) {
                revokeRuntimePermissions(this.NO_PM_CACHE, packageName, PHONE_PERMISSIONS, true, userId);
                revokeRuntimePermissions(this.NO_PM_CACHE, packageName, ALWAYS_LOCATION_PERMISSIONS, true, userId);
            }
        }
    }

    public void grantDefaultPermissionsToActiveLuiApp(java.lang.String packageName, int userId) {
        android.util.Log.i(TAG, "Granting permissions to active LUI app for user:" + userId);
        grantSystemFixedPermissionsToSystemPackage(this.NO_PM_CACHE, packageName, userId, CAMERA_PERMISSIONS, NOTIFICATION_PERMISSIONS);
    }

    public void revokeDefaultPermissionsFromLuiApps(java.lang.String[] packageNames, int userId) {
        android.util.Log.i(TAG, "Revoke permissions from LUI apps for user:" + userId);
        if (packageNames == null) {
            return;
        }
        for (java.lang.String packageName : packageNames) {
            android.content.pm.PackageInfo pkg = this.NO_PM_CACHE.getSystemPackageInfo(packageName);
            if (this.NO_PM_CACHE.isSystemPackage(pkg) && doesPackageSupportRuntimePermissions(pkg)) {
                revokeRuntimePermissions(this.NO_PM_CACHE, packageName, CAMERA_PERMISSIONS, true, userId);
            }
        }
    }

    public void grantDefaultPermissionsToCarrierServiceApp(java.lang.String packageName, int userId) {
        android.util.Log.i(TAG, "Grant permissions to Carrier Service app " + packageName + " for user:" + userId);
        grantPermissionsToPackage(this.NO_PM_CACHE, packageName, userId, false, true, NOTIFICATION_PERMISSIONS);
    }

    private java.lang.String getDefaultSystemHandlerActivityPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String intentAction, int userId) {
        return getDefaultSystemHandlerActivityPackage(pm, new android.content.Intent(intentAction), userId);
    }

    private java.lang.String getDefaultSystemHandlerActivityPackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, android.content.Intent intent, int userId) {
        android.content.pm.ResolveInfo handler = this.mContext.getPackageManager().resolveActivityAsUser(intent, DEFAULT_INTENT_QUERY_FLAGS, userId);
        if (handler == null || handler.activityInfo == null || this.mServiceInternal.isResolveActivityComponent(handler.activityInfo)) {
            return null;
        }
        java.lang.String packageName = handler.activityInfo.packageName;
        if (pm.isSystemPackage(packageName)) {
            return packageName;
        }
        return null;
    }

    private java.lang.String getDefaultSystemHandlerServicePackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String intentAction, int userId) {
        return getDefaultSystemHandlerServicePackage(pm, new android.content.Intent(intentAction), userId);
    }

    private java.lang.String getDefaultSystemHandlerServicePackage(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, android.content.Intent intent, int userId) {
        java.util.List<android.content.pm.ResolveInfo> handlers = this.mContext.getPackageManager().queryIntentServicesAsUser(intent, DEFAULT_INTENT_QUERY_FLAGS, userId);
        if (handlers == null) {
            return null;
        }
        int handlerCount = handlers.size();
        for (int i = 0; i < handlerCount; i++) {
            android.content.pm.ResolveInfo handler = handlers.get(i);
            java.lang.String handlerPackage = handler.serviceInfo.packageName;
            if (pm.isSystemPackage(handlerPackage)) {
                return handlerPackage;
            }
        }
        return null;
    }

    private java.util.ArrayList<java.lang.String> getHeadlessSyncAdapterPackages(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String[] syncAdapterPackageNames, int userId) {
        java.util.ArrayList<java.lang.String> syncAdapterPackages = new java.util.ArrayList<>();
        android.content.Intent homeIntent = new android.content.Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER");
        for (java.lang.String syncAdapterPackageName : syncAdapterPackageNames) {
            homeIntent.setPackage(syncAdapterPackageName);
            android.content.pm.ResolveInfo homeActivity = this.mContext.getPackageManager().resolveActivityAsUser(homeIntent, DEFAULT_INTENT_QUERY_FLAGS, userId);
            if (homeActivity == null && pm.isSystemPackage(syncAdapterPackageName)) {
                syncAdapterPackages.add(syncAdapterPackageName);
            }
        }
        return syncAdapterPackages;
    }

    private java.lang.String getDefaultProviderAuthorityPackage(java.lang.String authority, int userId) {
        android.content.pm.ProviderInfo provider = this.mContext.getPackageManager().resolveContentProviderAsUser(authority, DEFAULT_INTENT_QUERY_FLAGS, userId);
        if (provider != null) {
            return provider.packageName;
        }
        return null;
    }

    private void grantRuntimePermissions(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, android.content.pm.PackageInfo pkg, java.util.Set<java.lang.String> permissions, boolean systemFixed, int userId) {
        grantRuntimePermissions(pm, pkg, permissions, systemFixed, false, true, userId);
    }

    private void revokeRuntimePermissions(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String packageName, java.util.Set<java.lang.String> permissions, boolean systemFixed, int userId) {
        android.content.pm.PackageInfo pkg = pm.getSystemPackageInfo(packageName);
        if (pkg == null || com.android.internal.util.ArrayUtils.isEmpty(pkg.requestedPermissions)) {
            return;
        }
        java.util.Set<java.lang.String> revokablePermissions = new android.util.ArraySet<>(java.util.Arrays.asList(pkg.requestedPermissions));
        for (java.lang.String permission : permissions) {
            if (revokablePermissions.contains(permission)) {
                android.os.UserHandle user = android.os.UserHandle.of(userId);
                int flags = pm.getPermissionFlags(permission, pm.getPackageInfo(packageName), user);
                if ((flags & 32) != 0 && (flags & 4) == 0 && ((flags & 16) == 0 || systemFixed)) {
                    pm.revokePermission(permission, pkg, user);
                    pm.updatePermissionFlags(permission, pkg, 32, 0, user);
                }
            }
        }
    }

    private boolean isFixedOrUserSet(int flags) {
        return (flags & 23) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void grantRuntimePermissions(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper r31, android.content.pm.PackageInfo r32, java.util.Set<java.lang.String> r33, boolean r34, boolean r35, boolean r36, int r37) {
        /*
            Method dump skipped, instruction units count: 498
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.permission.DefaultPermissionGrantPolicy.grantRuntimePermissions(com.android.server.pm.permission.DefaultPermissionGrantPolicy$PackageManagerWrapper, android.content.pm.PackageInfo, java.util.Set, boolean, boolean, boolean, int):void");
    }

    static /* synthetic */ java.lang.String[] lambda$grantRuntimePermissions$0(int x$0) {
        return new java.lang.String[x$0];
    }

    private void grantDefaultPermissionExceptions(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, int userId) {
        int j;
        int permissionGrantCount;
        this.mHandler.removeMessages(1);
        synchronized (this.mLock) {
            if (this.mGrantExceptions == null) {
                this.mGrantExceptions = readDefaultPermissionExceptionsLocked(pm);
            }
        }
        java.util.Set<java.lang.String> permissions = null;
        int exceptionCount = this.mGrantExceptions.size();
        for (int i = 0; i < exceptionCount; i++) {
            java.lang.String packageName = this.mGrantExceptions.keyAt(i);
            android.content.pm.PackageInfo pkg = pm.getPackageInfo(packageName);
            java.util.List<com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant> permissionGrants = this.mGrantExceptions.valueAt(i);
            int permissionGrantCount2 = permissionGrants.size();
            int j2 = 0;
            while (j2 < permissionGrantCount2) {
                com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant permissionGrant = permissionGrants.get(j2);
                if (!pm.isPermissionDangerous(permissionGrant.name)) {
                    android.util.Log.w(TAG, "Ignoring permission " + permissionGrant.name + " which isn't dangerous");
                    j = j2;
                    permissionGrantCount = permissionGrantCount2;
                } else {
                    if (permissions == null) {
                        permissions = new android.util.ArraySet<>();
                    } else {
                        permissions.clear();
                    }
                    permissions.add(permissionGrant.name);
                    j = j2;
                    permissionGrantCount = permissionGrantCount2;
                    grantRuntimePermissions(pm, pkg, permissions, permissionGrant.fixed, permissionGrant.whitelisted, true, userId);
                }
                j2 = j + 1;
                permissionGrantCount2 = permissionGrantCount;
            }
        }
    }

    private java.io.File[] getDefaultPermissionFiles() {
        java.util.ArrayList<java.io.File> ret = new java.util.ArrayList<>();
        java.io.File dir = new java.io.File(android.os.Environment.getRootDirectory(), "etc/default-permissions");
        if (dir.isDirectory() && dir.canRead()) {
            java.util.Collections.addAll(ret, dir.listFiles());
        }
        java.io.File dir2 = new java.io.File(android.os.Environment.getVendorDirectory(), "etc/default-permissions");
        if (dir2.isDirectory() && dir2.canRead()) {
            java.util.Collections.addAll(ret, dir2.listFiles());
        }
        java.io.File dir3 = new java.io.File(android.os.Environment.getOdmDirectory(), "etc/default-permissions");
        if (dir3.isDirectory() && dir3.canRead()) {
            java.util.Collections.addAll(ret, dir3.listFiles());
        }
        java.io.File dir4 = new java.io.File(android.os.Environment.getProductDirectory(), "etc/default-permissions");
        if (dir4.isDirectory() && dir4.canRead()) {
            java.util.Collections.addAll(ret, dir4.listFiles());
        }
        java.io.File dir5 = new java.io.File(android.os.Environment.getSystemExtDirectory(), "etc/default-permissions");
        if (dir5.isDirectory() && dir5.canRead()) {
            java.util.Collections.addAll(ret, dir5.listFiles());
        }
        java.io.File dir6 = new java.io.File(android.os.Environment.getOemDirectory(), "etc/default-permissions");
        if (dir6.isDirectory() && dir6.canRead()) {
            java.util.Collections.addAll(ret, dir6.listFiles());
        }
        this.mDefaultPermissionGrantPolicyExt.hookGetDefaultPermissionFiles(ret, dir6);
        if (ret.isEmpty()) {
            return null;
        }
        return (java.io.File[]) ret.toArray(new java.io.File[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.ArrayMap<java.lang.String, java.util.List<com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant>> readDefaultPermissionExceptionsLocked(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm) {
        java.io.File[] files = getDefaultPermissionFiles();
        if (files == null) {
            return new android.util.ArrayMap<>(0);
        }
        android.util.ArrayMap<java.lang.String, java.util.List<com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant>> grantExceptions = new android.util.ArrayMap<>();
        for (java.io.File file : files) {
            if (!file.getPath().endsWith(".xml")) {
                android.util.Slog.i(TAG, "Non-xml file " + file + " in " + file.getParent() + " directory, ignoring");
            } else if (!file.canRead()) {
                android.util.Slog.w(TAG, "Default permissions file " + file + " cannot be read");
            } else {
                try {
                    java.io.InputStream str = new java.io.FileInputStream(file);
                    try {
                        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(str);
                        parse(pm, parser, grantExceptions);
                        str.close();
                    } catch (java.lang.Throwable th) {
                        try {
                            str.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    android.util.Slog.w(TAG, "Error reading default permissions file " + file, e);
                }
            }
        }
        return grantExceptions;
    }

    private void parse(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, com.android.modules.utils.TypedXmlPullParser parser, java.util.Map<java.lang.String, java.util.List<com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant>> outGrantExceptions) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        if (TAG_EXCEPTIONS.equals(parser.getName())) {
                            parseExceptions(pm, parser, outGrantExceptions);
                        } else {
                            android.util.Log.e(TAG, "Unknown tag " + parser.getName());
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void parseExceptions(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, com.android.modules.utils.TypedXmlPullParser parser, java.util.Map<java.lang.String, java.util.List<com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant>> outGrantExceptions) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        if (TAG_EXCEPTION.equals(parser.getName())) {
                            java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, "package");
                            java.lang.String cert = parser.getAttributeValue((java.lang.String) null, ATTR_CERT);
                            java.util.List<com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant> packageExceptions = outGrantExceptions.get(packageName);
                            if (packageExceptions == null) {
                                android.content.pm.PackageInfo packageInfo = pm.getPackageInfo(packageName);
                                if (packageInfo == null) {
                                    android.util.Log.w(TAG, "No such package:" + packageName);
                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                } else if (!isSystemOrCertificateMatchingPackage(packageInfo, cert)) {
                                    android.util.Log.w(TAG, "Not system or certificate-matching package: " + packageName);
                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                } else if (!doesPackageSupportRuntimePermissions(packageInfo)) {
                                    android.util.Log.w(TAG, "Skipping non supporting runtime permissions package:" + packageName);
                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                } else {
                                    packageExceptions = new java.util.ArrayList();
                                    outGrantExceptions.put(packageName, packageExceptions);
                                }
                            }
                            parsePermission(parser, packageExceptions);
                        } else {
                            android.util.Log.e(TAG, "Unknown tag " + parser.getName() + "under <exceptions>");
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void parsePermission(com.android.modules.utils.TypedXmlPullParser parser, java.util.List<com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant> outPackageExceptions) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        if ("permission".contains(parser.getName())) {
                            java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
                            if (name == null) {
                                android.util.Log.w(TAG, "Mandatory name attribute missing for permission tag");
                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                            } else {
                                boolean fixed = parser.getAttributeBoolean((java.lang.String) null, ATTR_FIXED, false);
                                boolean whitelisted = parser.getAttributeBoolean((java.lang.String) null, ATTR_WHITELISTED, false);
                                com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant exception = new com.android.server.pm.permission.DefaultPermissionGrantPolicy.DefaultPermissionGrant(name, fixed, whitelisted);
                                outPackageExceptions.add(exception);
                            }
                        } else {
                            android.util.Log.e(TAG, "Unknown tag " + parser.getName() + "under <exception>");
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private boolean isSystemOrCertificateMatchingPackage(android.content.pm.PackageInfo pi, java.lang.String cert) {
        if (cert == null) {
            return pi.applicationInfo.isSystemApp();
        }
        return this.mContext.getPackageManager().hasSigningCertificate(pi.packageName, libcore.util.HexEncoding.decode(cert.replace(":", "")), 1);
    }

    private static boolean doesPackageSupportRuntimePermissions(android.content.pm.PackageInfo pkg) {
        return pkg.applicationInfo != null && pkg.applicationInfo.targetSdkVersion > 22;
    }

    private abstract class PackageManagerWrapper {
        abstract android.content.pm.PackageInfo getPackageInfo(java.lang.String str);

        abstract int getPermissionFlags(java.lang.String str, android.content.pm.PackageInfo packageInfo, android.os.UserHandle userHandle);

        abstract android.content.pm.PermissionInfo getPermissionInfo(java.lang.String str);

        abstract void grantPermission(java.lang.String str, android.content.pm.PackageInfo packageInfo, android.os.UserHandle userHandle);

        abstract boolean isGranted(java.lang.String str, android.content.pm.PackageInfo packageInfo, android.os.UserHandle userHandle);

        abstract void revokePermission(java.lang.String str, android.content.pm.PackageInfo packageInfo, android.os.UserHandle userHandle);

        abstract void updatePermissionFlags(java.lang.String str, android.content.pm.PackageInfo packageInfo, int i, int i2, android.os.UserHandle userHandle);

        private PackageManagerWrapper() {
        }

        android.content.pm.PackageInfo getSystemPackageInfo(java.lang.String pkg) {
            android.content.pm.PackageInfo pi = getPackageInfo(pkg);
            if (pi == null || !pi.applicationInfo.isSystemApp()) {
                return null;
            }
            return pi;
        }

        boolean isPermissionRestricted(java.lang.String name) {
            android.content.pm.PermissionInfo pi = getPermissionInfo(name);
            if (pi == null) {
                return false;
            }
            return pi.isRestricted();
        }

        boolean isPermissionDangerous(java.lang.String name) {
            android.content.pm.PermissionInfo pi = getPermissionInfo(name);
            return pi != null && pi.getProtection() == 1;
        }

        java.lang.String getBackgroundPermission(java.lang.String permission) {
            android.content.pm.PermissionInfo pi = getPermissionInfo(permission);
            if (pi == null) {
                return null;
            }
            return pi.backgroundPermission;
        }

        boolean isSystemPackage(java.lang.String packageName) {
            return isSystemPackage(getPackageInfo(packageName));
        }

        boolean isSystemPackage(android.content.pm.PackageInfo pkg) {
            return (pkg == null || !pkg.applicationInfo.isSystemApp() || isSysComponentOrPersistentPlatformSignedPrivApp(pkg)) ? false : true;
        }

        boolean isSysComponentOrPersistentPlatformSignedPrivApp(android.content.pm.PackageInfo pkg) {
            if (android.os.UserHandle.getAppId(pkg.applicationInfo.uid) < 10000) {
                return true;
            }
            if (!pkg.applicationInfo.isPrivilegedApp()) {
                return false;
            }
            android.content.pm.PackageInfo disabledPkg = getSystemPackageInfo(com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mServiceInternal.getDisabledSystemPackageName(pkg.applicationInfo.packageName));
            if (disabledPkg != null) {
                android.content.pm.ApplicationInfo disabledPackageAppInfo = disabledPkg.applicationInfo;
                if (disabledPackageAppInfo != null && (disabledPackageAppInfo.flags & 8) == 0) {
                    return false;
                }
            } else if ((pkg.applicationInfo.flags & 8) == 0) {
                return false;
            }
            return com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mServiceInternal.isPlatformSigned(pkg.packageName);
        }
    }

    private class DelayingPackageManagerCache extends com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper {
        private final android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState>> mDelayedPermissionState;
        private final android.util.ArrayMap<java.lang.String, android.content.pm.PackageInfo> mPackageInfos;
        private final android.util.ArrayMap<java.lang.String, android.content.pm.PermissionInfo> mPermissionInfos;
        private final android.util.SparseArray<android.content.Context> mUserContexts;

        private DelayingPackageManagerCache() {
            super();
            this.mDelayedPermissionState = new android.util.SparseArray<>();
            this.mUserContexts = new android.util.SparseArray<>();
            this.mPermissionInfos = new android.util.ArrayMap<>();
            this.mPackageInfos = new android.util.ArrayMap<>();
        }

        void apply() {
            android.content.pm.PackageManager.corkPackageInfoCache();
            for (int uidIdx = 0; uidIdx < this.mDelayedPermissionState.size(); uidIdx++) {
                for (int permIdx = 0; permIdx < this.mDelayedPermissionState.valueAt(uidIdx).size(); permIdx++) {
                    try {
                        this.mDelayedPermissionState.valueAt(uidIdx).valueAt(permIdx).apply();
                    } catch (java.lang.IllegalArgumentException e) {
                        android.util.Slog.w(com.android.server.pm.permission.DefaultPermissionGrantPolicy.TAG, "Cannot set permission " + this.mDelayedPermissionState.valueAt(uidIdx).keyAt(permIdx) + " of uid " + this.mDelayedPermissionState.keyAt(uidIdx), e);
                    }
                }
            }
            android.content.pm.PackageManager.uncorkPackageInfoCache();
        }

        void addPackageInfo(java.lang.String packageName, android.content.pm.PackageInfo pkg) {
            this.mPackageInfos.put(packageName, pkg);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.content.Context createContextAsUser(android.os.UserHandle user) {
            int index = this.mUserContexts.indexOfKey(user.getIdentifier());
            if (index >= 0) {
                return this.mUserContexts.valueAt(index);
            }
            android.content.Context uc = com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.mContext.createContextAsUser(user, 0);
            this.mUserContexts.put(user.getIdentifier(), uc);
            return uc;
        }

        private com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState getPermissionState(java.lang.String permission, android.content.pm.PackageInfo pkg, android.os.UserHandle user) {
            android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState> uidState;
            int uid = android.os.UserHandle.getUid(user.getIdentifier(), android.os.UserHandle.getAppId(pkg.applicationInfo.uid));
            int uidIdx = this.mDelayedPermissionState.indexOfKey(uid);
            if (uidIdx >= 0) {
                uidState = this.mDelayedPermissionState.valueAt(uidIdx);
            } else {
                uidState = new android.util.ArrayMap<>();
                this.mDelayedPermissionState.put(uid, uidState);
            }
            int permIdx = uidState.indexOfKey(permission);
            if (permIdx >= 0) {
                com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState permState = uidState.valueAt(permIdx);
                if (!com.android.internal.util.ArrayUtils.contains(permState.mPkgRequestingPerm.requestedPermissions, permission) && com.android.internal.util.ArrayUtils.contains(pkg.requestedPermissions, permission)) {
                    permState.mPkgRequestingPerm = pkg;
                    return permState;
                }
                return permState;
            }
            com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState permState2 = new com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState(permission, pkg, user);
            uidState.put(permission, permState2);
            return permState2;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public int getPermissionFlags(java.lang.String permission, android.content.pm.PackageInfo pkg, android.os.UserHandle user) {
            com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState state = getPermissionState(permission, pkg, user);
            state.initFlags();
            return state.newFlags.intValue();
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void updatePermissionFlags(java.lang.String permission, android.content.pm.PackageInfo pkg, int flagMask, int flagValues, android.os.UserHandle user) {
            com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState state = getPermissionState(permission, pkg, user);
            state.initFlags();
            state.newFlags = java.lang.Integer.valueOf((state.newFlags.intValue() & (~flagMask)) | (flagValues & flagMask));
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void grantPermission(java.lang.String permission, android.content.pm.PackageInfo pkg, android.os.UserHandle user) {
            com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState state = getPermissionState(permission, pkg, user);
            state.initGranted();
            state.newGranted = true;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public void revokePermission(java.lang.String permission, android.content.pm.PackageInfo pkg, android.os.UserHandle user) {
            com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState state = getPermissionState(permission, pkg, user);
            state.initGranted();
            state.newGranted = false;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public boolean isGranted(java.lang.String permission, android.content.pm.PackageInfo pkg, android.os.UserHandle user) {
            com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.PermissionState state = getPermissionState(permission, pkg, user);
            state.initGranted();
            return state.newGranted.booleanValue();
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String permissionName) {
            int index = this.mPermissionInfos.indexOfKey(permissionName);
            if (index >= 0) {
                return this.mPermissionInfos.valueAt(index);
            }
            android.content.pm.PermissionInfo pi = com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE.getPermissionInfo(permissionName);
            this.mPermissionInfos.put(permissionName, pi);
            return pi;
        }

        @Override // com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper
        public android.content.pm.PackageInfo getPackageInfo(java.lang.String pkg) {
            int index = this.mPackageInfos.indexOfKey(pkg);
            if (index >= 0) {
                return this.mPackageInfos.valueAt(index);
            }
            android.content.pm.PackageInfo pi = com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE.getPackageInfo(pkg);
            this.mPackageInfos.put(pkg, pi);
            return pi;
        }

        private class PermissionState {
            private java.lang.Integer mOriginalFlags;
            private java.lang.Boolean mOriginalGranted;
            private final java.lang.String mPermission;
            private android.content.pm.PackageInfo mPkgRequestingPerm;
            private final android.os.UserHandle mUser;
            java.lang.Integer newFlags;
            java.lang.Boolean newGranted;

            private PermissionState(java.lang.String permission, android.content.pm.PackageInfo pkgRequestingPerm, android.os.UserHandle user) {
                this.mPermission = permission;
                this.mPkgRequestingPerm = pkgRequestingPerm;
                this.mUser = user;
            }

            void apply() {
                int flagsToRemove;
                int flagsToAdd = 0;
                if (this.newFlags == null) {
                    flagsToRemove = 0;
                } else {
                    flagsToAdd = this.newFlags.intValue() & (~this.mOriginalFlags.intValue());
                    int flagsToRemove2 = this.mOriginalFlags.intValue() & (~this.newFlags.intValue());
                    flagsToRemove = flagsToRemove2;
                }
                if (flagsToRemove != 0) {
                    com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE.updatePermissionFlags(this.mPermission, this.mPkgRequestingPerm, flagsToRemove, 0, this.mUser);
                }
                if ((flagsToAdd & 14336) != 0) {
                    int newRestrictionExcemptFlags = flagsToAdd & 14336;
                    com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE.updatePermissionFlags(this.mPermission, this.mPkgRequestingPerm, newRestrictionExcemptFlags, -1, this.mUser);
                }
                if (this.newGranted != null && !java.util.Objects.equals(this.newGranted, this.mOriginalGranted)) {
                    if (this.newGranted.booleanValue()) {
                        com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE.grantPermission(this.mPermission, this.mPkgRequestingPerm, this.mUser);
                    } else {
                        com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE.revokePermission(this.mPermission, this.mPkgRequestingPerm, this.mUser);
                    }
                }
                if ((flagsToAdd & (-14337)) != 0) {
                    int newFlags = flagsToAdd & (-14337);
                    com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE.updatePermissionFlags(this.mPermission, this.mPkgRequestingPerm, newFlags, -1, this.mUser);
                }
            }

            void initFlags() {
                if (this.newFlags == null) {
                    this.mOriginalFlags = java.lang.Integer.valueOf(com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE.getPermissionFlags(this.mPermission, this.mPkgRequestingPerm, this.mUser));
                    this.newFlags = this.mOriginalFlags;
                }
            }

            void initGranted() {
                if (this.newGranted == null) {
                    this.mOriginalGranted = java.lang.Boolean.valueOf(com.android.server.pm.permission.DefaultPermissionGrantPolicy.DelayingPackageManagerCache.this.createContextAsUser(this.mUser).getPackageManager().checkPermission(this.mPermission, this.mPkgRequestingPerm.packageName) == 0);
                    this.newGranted = this.mOriginalGranted;
                }
            }
        }
    }

    private static final class DefaultPermissionGrant {
        final boolean fixed;
        final java.lang.String name;
        final boolean whitelisted;

        public DefaultPermissionGrant(java.lang.String name, boolean fixed, boolean whitelisted) {
            this.name = name;
            this.fixed = fixed;
            this.whitelisted = whitelisted;
        }
    }

    public com.android.server.pm.permission.IDefaultPermissionGrantPolicyWrapper getWrapper() {
        return this.mWrapper;
    }

    private class DefaultPermissionGrantPolicyWrapper implements com.android.server.pm.permission.IDefaultPermissionGrantPolicyWrapper {
        private DefaultPermissionGrantPolicyWrapper() {
        }

        @Override // com.android.server.pm.permission.IDefaultPermissionGrantPolicyWrapper
        public java.lang.Object getNoPmCache() {
            return com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.NO_PM_CACHE;
        }

        @Override // com.android.server.pm.permission.IDefaultPermissionGrantPolicyWrapper
        public void grantRuntimePermissions(java.lang.Object pm, android.content.pm.PackageInfo pkg, java.util.Set<java.lang.String> permissionsWithoutSplits, boolean systemFixed, boolean ignoreSystemPackage, boolean whitelistRestrictedPermissions, int userId) {
            if (pm instanceof com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper) {
                com.android.server.pm.permission.DefaultPermissionGrantPolicy.this.grantRuntimePermissions((com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper) pm, pkg, permissionsWithoutSplits, systemFixed, ignoreSystemPackage, whitelistRestrictedPermissions, userId);
            } else {
                android.util.Slog.e(com.android.server.pm.permission.DefaultPermissionGrantPolicy.TAG, "wrapp -> grantRuntimePermissions, wrong argument");
            }
        }
    }

    private boolean needRepairReadPhoneStatePermission(com.android.server.pm.permission.DefaultPermissionGrantPolicy.PackageManagerWrapper pm, java.lang.String permission, android.content.pm.PackageInfo pkg) {
        if (permission == null || !permission.equals("android.permission.READ_PHONE_STATE") || !pm.isSysComponentOrPersistentPlatformSignedPrivApp(pkg)) {
            return false;
        }
        return true;
    }
}
