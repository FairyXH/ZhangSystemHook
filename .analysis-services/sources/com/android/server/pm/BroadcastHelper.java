package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class BroadcastHelper {
    public static boolean DEBUG_BROADCASTS = false;
    private static final java.lang.String[] INSTANT_APP_BROADCAST_PERMISSION = {"android.permission.ACCESS_INSTANT_APPS"};
    private final android.app.ActivityManagerInternal mAmInternal;
    private final com.android.server.pm.AppsFilterSnapshot mAppsFilter;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final com.android.server.pm.PackageMonitorCallbackHelper mPackageMonitorCallbackHelper;
    private final com.android.server.pm.UserManagerInternal mUmInternal;
    private final com.android.server.pm.IBroadcastHelperExt mBroadcastHelperExt = (com.android.server.pm.IBroadcastHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IBroadcastHelperExt.class).create();
    private final com.android.server.pm.IBroadcastHelperWrapper mWrapper = new com.android.server.pm.BroadcastHelper.BroadcastHelperWrapper();

    BroadcastHelper(com.android.server.pm.PackageManagerServiceInjector injector) {
        this.mUmInternal = injector.getUserManagerInternal();
        this.mAmInternal = injector.getActivityManagerInternal();
        this.mContext = injector.getContext();
        this.mHandler = injector.getHandler();
        this.mPackageMonitorCallbackHelper = injector.getPackageMonitorCallbackHelper();
        this.mAppsFilter = injector.getAppsFilter();
    }

    void sendPackageBroadcastWithIntent(android.content.Intent intent, int userId, boolean isInstantApp, int flags, int[] visibilityAllowList, android.content.IIntentReceiver finishedReceiver, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver, android.os.Bundle bOptions) {
        intent.addFlags(flags | 67108864);
        android.util.SparseArray<int[]> broadcastAllowList = new android.util.SparseArray<>();
        broadcastAllowList.put(userId, visibilityAllowList);
        broadcastIntent(intent, finishedReceiver, isInstantApp, userId, broadcastAllowList, filterExtrasForReceiver, bOptions);
    }

    void sendPackageBroadcast(java.lang.String action, java.lang.String pkg, android.os.Bundle extras, int flags, java.lang.String targetPkg, android.content.IIntentReceiver finishedReceiver, int[] userIds, int[] instantUserIds, android.util.SparseArray<int[]> broadcastAllowList, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver, android.os.Bundle bOptions) {
        int[] resolvedUserIds;
        try {
            android.app.IActivityManager am = android.app.ActivityManager.getService();
            if (am == null) {
                return;
            }
            if (userIds == null) {
                resolvedUserIds = am.getRunningUserIds();
            } else {
                resolvedUserIds = userIds;
            }
            if (com.android.internal.util.ArrayUtils.isEmpty(instantUserIds)) {
                doSendBroadcast(action, pkg, extras, flags, targetPkg, finishedReceiver, resolvedUserIds, false, broadcastAllowList, filterExtrasForReceiver, bOptions);
            } else {
                doSendBroadcast(action, pkg, extras, flags, targetPkg, finishedReceiver, instantUserIds, true, null, null, bOptions);
            }
        } catch (android.os.RemoteException e) {
        }
    }

    private void doSendBroadcast(java.lang.String action, java.lang.String pkg, android.os.Bundle extras, int flags, java.lang.String targetPkg, android.content.IIntentReceiver finishedReceiver, int[] userIds, boolean isInstantApp, android.util.SparseArray<int[]> broadcastAllowList, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver, android.os.Bundle bOptions) {
        for (int userId : userIds) {
            android.content.Intent intent = new android.content.Intent(action, pkg != null ? android.net.Uri.fromParts("package", pkg, null) : null);
            if (extras != null) {
                intent.putExtras(extras);
            }
            if (targetPkg != null) {
                intent.setPackage(targetPkg);
            }
            this.mBroadcastHelperExt.insertPackageAddedBroadcastData(action, this.mContext, intent, pkg);
            int uid = intent.getIntExtra("android.intent.extra.UID", -1);
            if (uid >= 0 && android.os.UserHandle.getUserId(uid) != userId) {
                intent.putExtra("android.intent.extra.UID", android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(uid)));
            }
            if (broadcastAllowList != null && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(targetPkg)) {
                intent.putExtra("android.intent.extra.VISIBILITY_ALLOW_LIST", broadcastAllowList.get(userId));
            }
            intent.putExtra("android.intent.extra.user_handle", userId);
            intent.addFlags(flags | 67108864);
            this.mBroadcastHelperExt.sendCustomizedBroadcastInDoSendBroadcast(intent, action, userId, targetPkg, finishedReceiver);
            broadcastIntent(intent, finishedReceiver, isInstantApp, userId, broadcastAllowList, filterExtrasForReceiver, bOptions);
        }
    }

    private void broadcastIntent(android.content.Intent intent, android.content.IIntentReceiver finishedReceiver, boolean isInstantApp, int userId, android.util.SparseArray<int[]> broadcastAllowList, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver, android.os.Bundle bOptions) {
        java.lang.String[] requiredPermissions = isInstantApp ? INSTANT_APP_BROADCAST_PERMISSION : null;
        if (DEBUG_BROADCASTS) {
            java.lang.RuntimeException here = new java.lang.RuntimeException("here");
            here.fillInStackTrace();
            android.util.Slog.d("PackageManager", "Sending to user " + userId + ": " + intent.toShortString(false, true, false, false) + " " + intent.getExtras(), here);
        }
        this.mAmInternal.broadcastIntentWithCallback(intent, finishedReceiver, requiredPermissions, userId, broadcastAllowList != null ? broadcastAllowList.get(userId) : null, filterExtrasForReceiver, bOptions);
    }

    void sendResourcesChangedBroadcast(final com.android.server.pm.Computer snapshot, boolean mediaStatus, boolean replacing, java.lang.String[] pkgNames, int[] uids) {
        if (!com.android.internal.util.ArrayUtils.isEmpty(pkgNames) && !com.android.internal.util.ArrayUtils.isEmpty(uids)) {
            android.os.Bundle extras = new android.os.Bundle();
            extras.putStringArray("android.intent.extra.changed_package_list", pkgNames);
            extras.putIntArray("android.intent.extra.changed_uid_list", uids);
            if (replacing) {
                extras.putBoolean("android.intent.extra.REPLACING", replacing);
            }
            java.lang.String action = mediaStatus ? "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE" : "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
            sendPackageBroadcast(action, null, extras, 0, null, null, null, null, null, new java.util.function.BiFunction() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda3
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.pm.BroadcastHelper.filterExtrasChangedPackageList(snapshot, ((java.lang.Integer) obj).intValue(), (android.os.Bundle) obj2);
                }
            }, null);
        }
    }

    private void sendBootCompletedBroadcastToSystemApp(java.lang.String packageName, boolean includeStopped, int userId) {
        if (!this.mUmInternal.isUserRunning(userId)) {
            return;
        }
        android.app.IActivityManager am = android.app.ActivityManager.getService();
        try {
            android.content.Intent lockedBcIntent = new android.content.Intent("android.intent.action.LOCKED_BOOT_COMPLETED").setPackage(packageName);
            lockedBcIntent.putExtra("android.intent.extra.user_handle", userId);
            if (includeStopped) {
                lockedBcIntent.addFlags(32);
            }
            java.lang.String[] requiredPermissions = {"android.permission.RECEIVE_BOOT_COMPLETED"};
            android.app.BroadcastOptions bOptions = getTemporaryAppAllowlistBroadcastOptions(202);
            try {
                am.broadcastIntentWithFeature((android.app.IApplicationThread) null, (java.lang.String) null, lockedBcIntent, (java.lang.String) null, (android.content.IIntentReceiver) null, 0, (java.lang.String) null, (android.os.Bundle) null, requiredPermissions, (java.lang.String[]) null, (java.lang.String[]) null, -1, bOptions.toBundle(), false, false, userId);
                try {
                    if (this.mUmInternal.isUserUnlockingOrUnlocked(userId)) {
                        android.content.Intent bcIntent = new android.content.Intent("android.intent.action.BOOT_COMPLETED").setPackage(packageName);
                        bcIntent.putExtra("android.intent.extra.user_handle", userId);
                        if (includeStopped) {
                            bcIntent.addFlags(32);
                        }
                        am.broadcastIntentWithFeature((android.app.IApplicationThread) null, (java.lang.String) null, bcIntent, (java.lang.String) null, (android.content.IIntentReceiver) null, 0, (java.lang.String) null, (android.os.Bundle) null, requiredPermissions, (java.lang.String[]) null, (java.lang.String[]) null, -1, bOptions.toBundle(), false, false, userId);
                    }
                } catch (android.os.RemoteException e) {
                    e = e;
                    throw e.rethrowFromSystemServer();
                }
            } catch (android.os.RemoteException e2) {
                e = e2;
            }
        } catch (android.os.RemoteException e3) {
            e = e3;
        }
    }

    private android.app.BroadcastOptions getTemporaryAppAllowlistBroadcastOptions(int reasonCode) {
        long duration = 10000;
        if (this.mAmInternal != null) {
            duration = this.mAmInternal.getBootTimeTempAllowListDuration();
        }
        android.app.BroadcastOptions bOptions = android.app.BroadcastOptions.makeBasic();
        bOptions.setTemporaryAppAllowlist(duration, 0, reasonCode, "");
        return bOptions;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: sendPackageChangedBroadcast, reason: merged with bridge method [inline-methods] */
    public void lambda$sendPackageChangedBroadcast$4(java.lang.String packageName, boolean dontKillApp, java.util.ArrayList<java.lang.String> componentNames, int packageUid, java.lang.String reason, int[] userIds, int[] instantUserIds, android.util.SparseArray<int[]> broadcastAllowList) {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Log.v("PackageManager", "Sending package changed: package=" + packageName + " components=" + componentNames);
        }
        android.os.Bundle extras = new android.os.Bundle(4);
        int i = 0;
        extras.putString("android.intent.extra.changed_component_name", componentNames.get(0));
        java.lang.String[] nameList = new java.lang.String[componentNames.size()];
        componentNames.toArray(nameList);
        extras.putStringArray("android.intent.extra.changed_component_name_list", nameList);
        extras.putBoolean("android.intent.extra.DONT_KILL_APP", dontKillApp);
        extras.putInt("android.intent.extra.UID", packageUid);
        if (reason != null) {
            extras.putString("android.intent.extra.REASON", reason);
        }
        if (!componentNames.contains(packageName)) {
            i = 1073741824;
        }
        int flags = i;
        sendPackageBroadcast("android.intent.action.PACKAGE_CHANGED", packageName, extras, flags, null, null, userIds, instantUserIds, broadcastAllowList, null, null);
    }

    static void sendDeviceCustomizationReadyBroadcast() {
        android.content.Intent intent = new android.content.Intent("android.intent.action.DEVICE_CUSTOMIZATION_READY");
        intent.setFlags(16777216);
        android.app.IActivityManager am = android.app.ActivityManager.getService();
        java.lang.String[] requiredPermissions = {"android.permission.RECEIVE_DEVICE_CUSTOMIZATION_READY"};
        try {
            am.broadcastIntentWithFeature((android.app.IApplicationThread) null, (java.lang.String) null, intent, (java.lang.String) null, (android.content.IIntentReceiver) null, 0, (java.lang.String) null, (android.os.Bundle) null, requiredPermissions, (java.lang.String[]) null, (java.lang.String[]) null, -1, (android.os.Bundle) null, false, false, -1);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    void sendSessionCommitBroadcast(com.android.server.pm.Computer snapshot, android.content.pm.PackageInstaller.SessionInfo sessionInfo, int userId, java.lang.String appPredictionServicePackage) {
        com.android.server.pm.UserManagerService ums = com.android.server.pm.UserManagerService.getInstance();
        if (ums == null || sessionInfo.isStaged()) {
            return;
        }
        android.content.pm.UserInfo parent = ums.getProfileParent(userId);
        int launcherUserId = parent != null ? parent.id : userId;
        android.content.ComponentName launcherComponent = snapshot.getDefaultHomeActivity(launcherUserId);
        if (launcherComponent != null && canLauncherAccessProfile(launcherComponent, userId)) {
            android.content.Intent launcherIntent = new android.content.Intent("android.content.pm.action.SESSION_COMMITTED").putExtra("android.content.pm.extra.SESSION", sessionInfo).putExtra("android.intent.extra.USER", android.os.UserHandle.of(userId)).setPackage(launcherComponent.getPackageName());
            this.mContext.sendBroadcastAsUser(launcherIntent, android.os.UserHandle.of(launcherUserId));
        }
        if (appPredictionServicePackage != null) {
            android.content.Intent predictorIntent = new android.content.Intent("android.content.pm.action.SESSION_COMMITTED").putExtra("android.content.pm.extra.SESSION", sessionInfo).putExtra("android.intent.extra.USER", android.os.UserHandle.of(userId)).setPackage(appPredictionServicePackage);
            this.mContext.sendBroadcastAsUser(predictorIntent, android.os.UserHandle.of(launcherUserId));
        }
    }

    boolean canLauncherAccessProfile(android.content.ComponentName launcherComponent, int userId) {
        return (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePermissionToAccessHiddenProfiles() && android.multiuser.Flags.enablePrivateSpaceFeatures() && this.mUmInternal.getUserProperties(userId).getProfileApiVisibility() == 1 && this.mContext.getPackageManager().checkPermission("android.permission.ACCESS_HIDDEN_PROFILES_FULL", launcherComponent.getPackageName()) != 0 && this.mContext.getPackageManager().checkPermission("android.permission.ACCESS_HIDDEN_PROFILES", launcherComponent.getPackageName()) != 0) ? false : true;
    }

    void sendPreferredActivityChangedBroadcast(final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.pm.BroadcastHelper.lambda$sendPreferredActivityChangedBroadcast$1(userId);
            }
        });
    }

    static /* synthetic */ void lambda$sendPreferredActivityChangedBroadcast$1(int userId) {
        android.app.IActivityManager am = android.app.ActivityManager.getService();
        if (am == null) {
            return;
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.ACTION_PREFERRED_ACTIVITY_CHANGED");
        intent.putExtra("android.intent.extra.user_handle", userId);
        intent.addFlags(67108864);
        try {
            am.broadcastIntentWithFeature((android.app.IApplicationThread) null, (java.lang.String) null, intent, (java.lang.String) null, (android.content.IIntentReceiver) null, 0, (java.lang.String) null, (android.os.Bundle) null, (java.lang.String[]) null, (java.lang.String[]) null, (java.lang.String[]) null, -1, (android.os.Bundle) null, false, false, userId);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r30v0 */
    /* JADX WARN: Type inference failed for: r30v1, types: [int] */
    /* JADX WARN: Type inference failed for: r30v2 */
    /* JADX WARN: Type inference failed for: r31v0 */
    /* JADX WARN: Type inference failed for: r31v1, types: [int] */
    /* JADX WARN: Type inference failed for: r31v2 */
    /* JADX WARN: Type inference failed for: r9v11 */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v22 */
    /* JADX WARN: Type inference failed for: r9v23 */
    /* JADX WARN: Type inference failed for: r9v6 */
    /* JADX WARN: Type inference failed for: r9v7 */
    void sendPostInstallBroadcasts(com.android.server.pm.Computer computer, com.android.server.pm.InstallRequest installRequest, java.lang.String str, java.lang.String str2, java.lang.String[] strArr, java.lang.String str3, com.android.server.pm.PackageSender packageSender, boolean z, boolean z2, boolean z3, boolean z4) throws java.lang.Throwable {
        java.lang.String installerPackageName;
        android.os.Bundle bundle;
        java.lang.String str4;
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        boolean z5;
        java.lang.String str5;
        java.lang.String str6;
        java.lang.String str7;
        boolean z6;
        ?? r30;
        int i;
        ?? r31;
        int i2;
        if (installRequest.getRemovedInfo() != null) {
            if (installRequest.getRemovedInfo().mIsExternal) {
                if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                    android.util.Slog.i("PackageManager", "upgrading pkg " + installRequest.getRemovedInfo().mRemovedPackage + " is ASEC-hosted -> UNAVAILABLE");
                }
                java.lang.String[] strArr2 = {installRequest.getRemovedInfo().mRemovedPackage};
                int[] iArr4 = {installRequest.getRemovedInfo().mUid};
                notifyResourcesChanged(false, true, strArr2, iArr4);
                sendResourcesChangedBroadcast(computer, false, true, strArr2, iArr4);
            }
            sendPackageRemovedBroadcasts(installRequest.getRemovedInfo(), packageSender, z2, false, false);
        }
        int[] firstTimeBroadcastUserIds = installRequest.getFirstTimeBroadcastUserIds();
        int[] firstTimeBroadcastInstantUserIds = installRequest.getFirstTimeBroadcastInstantUserIds();
        int[] updateBroadcastUserIds = installRequest.getUpdateBroadcastUserIds();
        int[] updateBroadcastInstantUserIds = installRequest.getUpdateBroadcastInstantUserIds();
        if (installRequest.getInstallerPackageName() != null) {
            installerPackageName = installRequest.getInstallerPackageName();
        } else if (installRequest.getRemovedInfo() != null) {
            installerPackageName = installRequest.getRemovedInfo().mInstallerPackageName;
        } else {
            installerPackageName = null;
        }
        java.lang.String str8 = installerPackageName;
        android.os.Bundle bundle2 = new android.os.Bundle();
        bundle2.putInt("android.intent.extra.UID", installRequest.getAppId());
        if (z3) {
            bundle2.putBoolean("android.intent.extra.REPLACING", true);
        }
        if (z4) {
            bundle2.putBoolean("android.intent.extra.ARCHIVAL", true);
        }
        bundle2.putInt("android.content.pm.extra.DATA_LOADER_TYPE", installRequest.getDataLoaderType());
        java.lang.String staticSharedLibraryName = installRequest.getPkg().getStaticSharedLibraryName();
        if (str8 == null || staticSharedLibraryName == null) {
            bundle = bundle2;
            str4 = str8;
            iArr = updateBroadcastUserIds;
            iArr2 = firstTimeBroadcastInstantUserIds;
            iArr3 = firstTimeBroadcastUserIds;
            z5 = false;
            str5 = "upgrading pkg ";
            str6 = "PackageManager";
        } else {
            bundle = bundle2;
            str4 = str8;
            iArr = updateBroadcastUserIds;
            iArr2 = firstTimeBroadcastInstantUserIds;
            iArr3 = firstTimeBroadcastUserIds;
            z5 = false;
            str5 = "upgrading pkg ";
            str6 = "PackageManager";
            sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_ADDED", str, bundle2, 0, str4, null, installRequest.getNewUsers(), null, null, null);
        }
        if (staticSharedLibraryName == null) {
            int appId = android.os.UserHandle.getAppId(installRequest.getAppId());
            boolean zIsInstallSystem = installRequest.isInstallSystem();
            boolean z7 = (installRequest.getInstallFlags() & 65536) != 0 ? true : z5;
            sendPackageAddedForNewUsers(computer, str, (zIsInstallSystem || z7) ? true : z5, z7, appId, iArr3, iArr2, z4, installRequest.getDataLoaderType());
            int[] iArr5 = iArr;
            sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_ADDED", str, bundle, 0, null, null, iArr5, updateBroadcastInstantUserIds, this.mAppsFilter.getVisibilityAllowList(computer, computer.getPackageStateInternal(str, 1000), iArr5, computer.getPackageStates()), null);
            java.lang.String str9 = str4;
            if (str9 == null) {
                str7 = str9;
            } else {
                str7 = str9;
                sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_ADDED", str, bundle, 0, str9, null, iArr5, updateBroadcastInstantUserIds, null, null);
            }
            if (isPrivacySafetyLabelChangeNotificationsEnabled(this.mContext)) {
                sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_ADDED", str, bundle, 0, str2, null, iArr5, updateBroadcastInstantUserIds, null, null);
            }
            int length = strArr.length;
            ?? r9 = z5;
            while (r9 < length) {
                java.lang.String str10 = strArr[r9];
                if (str10 == null || str10.equals(str7)) {
                    r31 = r9;
                    i2 = length;
                } else {
                    r31 = r9;
                    i2 = length;
                    sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_ADDED", str, bundle, 0, str10, null, iArr5, updateBroadcastInstantUserIds, null, null);
                }
                length = i2;
                r9 = r31 + 1;
            }
            sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_ADDED", str, bundle, 16777216, str3, null, iArr3, updateBroadcastInstantUserIds, null, null);
            if (z3) {
                sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_REPLACED", str, bundle, 0, null, null, iArr5, updateBroadcastInstantUserIds, installRequest.getRemovedInfo().mBroadcastAllowList, null);
                if (str7 != null) {
                    sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_REPLACED", str, bundle, 0, str7, null, iArr5, updateBroadcastInstantUserIds, null, null);
                }
                int length2 = strArr.length;
                ?? r92 = z5;
                while (r92 < length2) {
                    java.lang.String str11 = strArr[r92];
                    if (str11 == null || str11.equals(str7)) {
                        r30 = r92;
                        i = length2;
                    } else {
                        r30 = r92;
                        i = length2;
                        sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_REPLACED", str, bundle, 0, str11, null, iArr5, updateBroadcastInstantUserIds, null, null);
                    }
                    length2 = i;
                    r92 = r30 + 1;
                }
                sendPackageBroadcastAndNotify("android.intent.action.MY_PACKAGE_REPLACED", null, null, 0, str, null, iArr5, updateBroadcastInstantUserIds, null, getTemporaryAppAllowlistBroadcastOptions(311).toBundle());
            } else if (z && !installRequest.isInstallSystem()) {
                if (com.android.server.pm.PackageManagerService.DEBUG_BACKUP) {
                    android.util.Slog.i(str6, "Post-restore of " + str + " sending FIRST_LAUNCH in " + java.util.Arrays.toString(iArr3));
                }
                sendFirstLaunchBroadcast(str, str7, iArr3, iArr2);
            }
            if (installRequest.getPkg().isExternalStorage()) {
                if (z3) {
                    z6 = true;
                } else {
                    z6 = true;
                    int packageExternalStorageType = com.android.server.pm.PackageManagerServiceUtils.getPackageExternalStorageType(((android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class)).findVolumeByUuid(android.os.storage.StorageManager.convert(installRequest.getPkg().getVolumeUuid()).toString()), true);
                    if (packageExternalStorageType != 0) {
                        com.android.internal.util.FrameworkStatsLog.write(181, packageExternalStorageType, str);
                    }
                }
                if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                    android.util.Slog.i(str6, str5 + str + " is external");
                }
                if (!z4) {
                    java.lang.String[] strArr3 = {str};
                    int[] iArr6 = {installRequest.getPkg().getUid()};
                    sendResourcesChangedBroadcast(computer, true, true, strArr3, iArr6);
                    notifyResourcesChanged(z6, z6, strArr3, iArr6);
                    return;
                }
                return;
            }
            return;
        }
        java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> libraryConsumers = installRequest.getLibraryConsumers();
        if (!com.android.internal.util.ArrayUtils.isEmpty(libraryConsumers)) {
            boolean z8 = !z3;
            for (int i3 = 0; i3 < libraryConsumers.size(); i3++) {
                com.android.server.pm.pkg.AndroidPackage androidPackage = libraryConsumers.get(i3);
                sendPackageChangedBroadcast(computer, androidPackage.getPackageName(), z8, new java.util.ArrayList<>(java.util.Collections.singletonList(androidPackage.getPackageName())), androidPackage.getUid(), null);
            }
        }
    }

    private void sendPackageAddedForNewUsers(com.android.server.pm.Computer snapshot, final java.lang.String packageName, boolean sendBootCompleted, final boolean includeStopped, final int appId, final int[] userIds, final int[] instantUserIds, final boolean isArchived, final int dataLoaderType) throws java.lang.Throwable {
        if (com.android.internal.util.ArrayUtils.isEmpty(userIds) && com.android.internal.util.ArrayUtils.isEmpty(instantUserIds)) {
            return;
        }
        final android.util.SparseArray<int[]> broadcastAllowList = this.mAppsFilter.getVisibilityAllowList(snapshot, snapshot.getPackageStateInternal(packageName, 1000), userIds, snapshot.getPackageStates());
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendPackageAddedForNewUsers$2(packageName, appId, userIds, instantUserIds, isArchived, dataLoaderType, broadcastAllowList);
            }
        });
        this.mPackageMonitorCallbackHelper.notifyPackageAddedForNewUsers(packageName, appId, userIds, instantUserIds, isArchived, dataLoaderType, broadcastAllowList, this.mHandler);
        if (sendBootCompleted && !com.android.internal.util.ArrayUtils.isEmpty(userIds)) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendPackageAddedForNewUsers$3(userIds, packageName, includeStopped);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendPackageAddedForNewUsers$3(int[] userIds, java.lang.String packageName, boolean includeStopped) {
        for (int userId : userIds) {
            sendBootCompletedBroadcastToSystemApp(packageName, includeStopped, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: sendPackageAddedForNewUsers, reason: merged with bridge method [inline-methods] */
    public void lambda$sendPackageAddedForNewUsers$2(java.lang.String packageName, int appId, int[] userIds, int[] instantUserIds, boolean isArchived, int dataLoaderType, android.util.SparseArray<int[]> broadcastAllowlist) {
        android.os.Bundle extras = new android.os.Bundle(1);
        int uid = android.os.UserHandle.getUid(com.android.internal.util.ArrayUtils.isEmpty(userIds) ? instantUserIds[0] : userIds[0], appId);
        extras.putInt("android.intent.extra.UID", uid);
        if (isArchived) {
            extras.putBoolean("android.intent.extra.ARCHIVAL", true);
        }
        extras.putInt("android.content.pm.extra.DATA_LOADER_TYPE", dataLoaderType);
        sendPackageBroadcast("android.intent.action.PACKAGE_ADDED", packageName, extras, 0, null, null, userIds, instantUserIds, broadcastAllowlist, null, null);
        if (isPrivacySafetyLabelChangeNotificationsEnabled(this.mContext)) {
            sendPackageBroadcast("android.intent.action.PACKAGE_ADDED", packageName, extras, 0, this.mContext.getPackageManager().getPermissionControllerPackageName(), null, userIds, instantUserIds, broadcastAllowlist, null, null);
        }
    }

    void sendPackageAddedForUser(com.android.server.pm.Computer snapshot, java.lang.String packageName, com.android.server.pm.pkg.PackageStateInternal packageState, int userId, boolean isArchived, int dataLoaderType, java.lang.String appPredictionServicePackage) throws java.lang.Throwable {
        com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
        boolean isSystem = packageState.isSystem();
        boolean isInstantApp = userState.isInstantApp();
        int[] userIds = isInstantApp ? com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY : new int[]{userId};
        int[] instantUserIds = isInstantApp ? new int[]{userId} : com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        sendPackageAddedForNewUsers(snapshot, packageName, isSystem, false, packageState.getAppId(), userIds, instantUserIds, isArchived, dataLoaderType);
        android.content.pm.PackageInstaller.SessionInfo info = new android.content.pm.PackageInstaller.SessionInfo();
        info.installReason = userState.getInstallReason();
        info.appPackageName = packageName;
        sendSessionCommitBroadcast(snapshot, info, userId, appPredictionServicePackage);
    }

    void sendFirstLaunchBroadcast(java.lang.String pkgName, java.lang.String installerPkg, int[] userIds, int[] instantUserIds) {
        sendPackageBroadcast("android.intent.action.PACKAGE_FIRST_LAUNCH", pkgName, null, 0, installerPkg, null, userIds, instantUserIds, null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.os.Bundle filterExtrasChangedPackageList(com.android.server.pm.Computer snapshot, int callingUid, android.os.Bundle extras) {
        if (android.os.UserHandle.isCore(callingUid)) {
            return extras;
        }
        java.lang.String[] pkgs = extras.getStringArray("android.intent.extra.changed_package_list");
        if (com.android.internal.util.ArrayUtils.isEmpty(pkgs)) {
            return extras;
        }
        int userId = extras.getInt("android.intent.extra.user_handle", android.os.UserHandle.getUserId(callingUid));
        int[] uids = extras.getIntArray("android.intent.extra.changed_uid_list");
        android.util.Pair<java.lang.String[], int[]> filteredPkgs = filterPackages(snapshot, pkgs, uids, callingUid, userId);
        if (com.android.internal.util.ArrayUtils.isEmpty((java.lang.String[]) filteredPkgs.first)) {
            return null;
        }
        android.os.Bundle filteredExtras = new android.os.Bundle(extras);
        filteredExtras.putStringArray("android.intent.extra.changed_package_list", (java.lang.String[]) filteredPkgs.first);
        filteredExtras.putIntArray("android.intent.extra.changed_uid_list", (int[]) filteredPkgs.second);
        return filteredExtras;
    }

    private static boolean isPrivacySafetyLabelChangeNotificationsEnabled(android.content.Context context) {
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        return (!android.provider.DeviceConfig.getBoolean("privacy", "safety_label_change_notifications_enabled", true) || packageManager.hasSystemFeature("android.hardware.type.automotive") || packageManager.hasSystemFeature("android.software.leanback") || packageManager.hasSystemFeature("android.hardware.type.watch")) ? false : true;
    }

    private static android.util.Pair<java.lang.String[], int[]> filterPackages(com.android.server.pm.Computer snapshot, java.lang.String[] pkgs, int[] uids, int callingUid, int userId) {
        int pkgSize = pkgs.length;
        int uidSize = !com.android.internal.util.ArrayUtils.isEmpty(uids) ? uids.length : 0;
        java.util.ArrayList<java.lang.String> pkgList = new java.util.ArrayList<>(pkgSize);
        int[] array = null;
        android.util.IntArray uidList = uidSize > 0 ? new android.util.IntArray(uidSize) : null;
        for (int i = 0; i < pkgSize; i++) {
            java.lang.String packageName = pkgs[i];
            if (!snapshot.shouldFilterApplication(snapshot.getPackageStateInternal(packageName), callingUid, userId)) {
                pkgList.add(packageName);
                if (uidList != null && i < uidSize) {
                    uidList.add(uids[i]);
                }
            }
        }
        java.lang.String[] strArr = pkgList.size() > 0 ? (java.lang.String[]) pkgList.toArray(new java.lang.String[pkgList.size()]) : null;
        if (uidList != null && uidList.size() > 0) {
            array = uidList.toArray();
        }
        return new android.util.Pair<>(strArr, array);
    }

    void sendApplicationHiddenForUser(java.lang.String packageName, com.android.server.pm.pkg.PackageStateInternal packageState, int userId, com.android.server.pm.PackageSender packageSender) throws java.lang.Throwable {
        com.android.server.pm.PackageRemovedInfo info = new com.android.server.pm.PackageRemovedInfo();
        info.mRemovedPackage = packageName;
        info.mInstallerPackageName = packageState.getInstallSource().mInstallerPackageName;
        info.mRemovedUsers = new int[]{userId};
        info.mBroadcastUsers = new int[]{userId};
        info.mUid = android.os.UserHandle.getUid(userId, packageState.getAppId());
        info.mRemovedPackageVersionCode = packageState.getVersionCode();
        sendPackageRemovedBroadcasts(info, packageSender, true, false, false);
    }

    void sendPackageChangedBroadcast(com.android.server.pm.Computer snapshot, final java.lang.String packageName, final boolean dontKillApp, final java.util.ArrayList<java.lang.String> componentNames, final int packageUid, final java.lang.String reason) {
        com.android.server.pm.pkg.PackageStateInternal setting = snapshot.getPackageStateInternal(packageName, 1000);
        if (setting == null) {
            return;
        }
        int userId = android.os.UserHandle.getUserId(packageUid);
        boolean isInstantApp = snapshot.isInstantAppInternal(packageName, userId, 1000);
        final int[] userIds = isInstantApp ? com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY : new int[]{userId};
        final int[] instantUserIds = isInstantApp ? new int[]{userId} : com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        final android.util.SparseArray<int[]> broadcastAllowList = isInstantApp ? null : snapshot.getVisibilityAllowLists(packageName, userIds);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendPackageChangedBroadcast$4(packageName, dontKillApp, componentNames, packageUid, reason, userIds, instantUserIds, broadcastAllowList);
            }
        });
        this.mPackageMonitorCallbackHelper.notifyPackageChanged(packageName, dontKillApp, componentNames, packageUid, reason, userIds, instantUserIds, broadcastAllowList, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendPackageBroadcastAndNotify$5(java.lang.String action, java.lang.String pkg, android.os.Bundle extras, int flags, java.lang.String targetPkg, android.content.IIntentReceiver finishedReceiver, int[] userIds, int[] instantUserIds, android.util.SparseArray broadcastAllowList, android.os.Bundle bOptions) {
        sendPackageBroadcast(action, pkg, extras, flags, targetPkg, finishedReceiver, userIds, instantUserIds, broadcastAllowList, null, bOptions);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendPackageBroadcastAndNotify(final java.lang.String action, final java.lang.String pkg, final android.os.Bundle extras, final int flags, final java.lang.String targetPkg, final android.content.IIntentReceiver finishedReceiver, final int[] userIds, final int[] instantUserIds, final android.util.SparseArray<int[]> broadcastAllowList, final android.os.Bundle bOptions) throws java.lang.Throwable {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendPackageBroadcastAndNotify$5(action, pkg, extras, flags, targetPkg, finishedReceiver, userIds, instantUserIds, broadcastAllowList, bOptions);
            }
        });
        if (targetPkg == null) {
            notifyPackageMonitor(action, pkg, extras, userIds, instantUserIds, broadcastAllowList, null);
        }
    }

    void sendSystemPackageUpdatedBroadcasts(com.android.server.pm.PackageRemovedInfo packageRemovedInfo) throws java.lang.Throwable {
        if (!packageRemovedInfo.mIsRemovedPackageSystemUpdate) {
            return;
        }
        java.lang.String removedPackage = packageRemovedInfo.mRemovedPackage;
        java.lang.String installerPackageName = packageRemovedInfo.mInstallerPackageName;
        android.util.SparseArray<int[]> broadcastAllowList = packageRemovedInfo.mBroadcastAllowList;
        android.os.Bundle extras = new android.os.Bundle(2);
        extras.putInt("android.intent.extra.UID", packageRemovedInfo.mUid);
        extras.putBoolean("android.intent.extra.REPLACING", true);
        sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_ADDED", removedPackage, extras, 0, null, null, null, null, broadcastAllowList, null);
        if (installerPackageName != null) {
            sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_ADDED", removedPackage, extras, 0, installerPackageName, null, null, null, null, null);
            sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_REPLACED", removedPackage, extras, 0, installerPackageName, null, null, null, null, null);
        }
        sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_REPLACED", removedPackage, extras, 0, null, null, null, null, broadcastAllowList, null);
        sendPackageBroadcastAndNotify("android.intent.action.MY_PACKAGE_REPLACED", null, null, 0, removedPackage, null, null, null, null, getTemporaryBroadcastOptionsForSystemPackageUpdate(311).toBundle());
    }

    private android.app.BroadcastOptions getTemporaryBroadcastOptionsForSystemPackageUpdate(int reasonCode) {
        long duration = 10000;
        if (this.mAmInternal != null) {
            duration = this.mAmInternal.getBootTimeTempAllowListDuration();
        }
        android.app.BroadcastOptions bOptions = android.app.BroadcastOptions.makeBasic();
        bOptions.setTemporaryAppAllowlist(duration, 0, reasonCode, "");
        return bOptions;
    }

    void sendPackageRemovedBroadcasts(com.android.server.pm.PackageRemovedInfo packageRemovedInfo, com.android.server.pm.PackageSender packageSender, boolean killApp, boolean removedBySystem, boolean isArchived) throws java.lang.Throwable {
        android.os.Bundle extras;
        boolean isStaticSharedLib;
        boolean isRemovedPackageSystemUpdate;
        boolean dataRemoved;
        android.util.SparseArray<int[]> broadcastAllowList;
        android.os.Bundle extras2;
        java.lang.String removedPackage = packageRemovedInfo.mRemovedPackage;
        java.lang.String installerPackageName = packageRemovedInfo.mInstallerPackageName;
        int[] broadcastUserIds = packageRemovedInfo.mBroadcastUsers;
        int[] instantUserIds = packageRemovedInfo.mInstantUserIds;
        android.util.SparseArray<int[]> broadcastAllowList2 = packageRemovedInfo.mBroadcastAllowList;
        boolean dataRemoved2 = packageRemovedInfo.mDataRemoved;
        boolean isUpdate = packageRemovedInfo.mIsUpdate;
        boolean isRemovedPackageSystemUpdate2 = packageRemovedInfo.mIsRemovedPackageSystemUpdate;
        boolean isRemovedForAllUsers = packageRemovedInfo.mRemovedForAllUsers;
        boolean isStaticSharedLib2 = packageRemovedInfo.mIsStaticSharedLib;
        android.os.Bundle extras3 = new android.os.Bundle();
        extras3.putInt("android.intent.extra.UID", packageRemovedInfo.mUid);
        extras3.putBoolean("android.intent.extra.DATA_REMOVED", dataRemoved2);
        extras3.putBoolean("android.intent.extra.SYSTEM_UPDATE_UNINSTALL", isRemovedPackageSystemUpdate2);
        extras3.putBoolean("android.intent.extra.DONT_KILL_APP", !killApp);
        extras3.putBoolean("android.intent.extra.USER_INITIATED", !removedBySystem);
        boolean isReplace = isUpdate || isRemovedPackageSystemUpdate2;
        if (isReplace || isArchived) {
            extras3.putBoolean("android.intent.extra.REPLACING", true);
        }
        if (isArchived) {
            extras3.putBoolean("android.intent.extra.ARCHIVAL", true);
        }
        extras3.putBoolean("android.intent.extra.REMOVED_FOR_ALL_USERS", isRemovedForAllUsers);
        if (removedPackage == null || installerPackageName == null) {
            extras = extras3;
            isStaticSharedLib = isStaticSharedLib2;
            isRemovedPackageSystemUpdate = isRemovedPackageSystemUpdate2;
            dataRemoved = dataRemoved2;
            broadcastAllowList = broadcastAllowList2;
        } else {
            extras = extras3;
            isStaticSharedLib = isStaticSharedLib2;
            isRemovedPackageSystemUpdate = isRemovedPackageSystemUpdate2;
            dataRemoved = dataRemoved2;
            broadcastAllowList = broadcastAllowList2;
            sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_REMOVED", removedPackage, extras3, 0, installerPackageName, null, broadcastUserIds, instantUserIds, null, null);
        }
        if (isStaticSharedLib) {
            return;
        }
        if (removedPackage != null) {
            android.os.Bundle bundle = extras;
            android.util.SparseArray<int[]> sparseArray = broadcastAllowList;
            sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_REMOVED", removedPackage, bundle, 0, null, null, broadcastUserIds, instantUserIds, sparseArray, null);
            sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_REMOVED_INTERNAL", removedPackage, bundle, 0, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, null, broadcastUserIds, instantUserIds, sparseArray, null);
            if (dataRemoved && !isRemovedPackageSystemUpdate) {
                sendPackageBroadcastAndNotify("android.intent.action.PACKAGE_FULLY_REMOVED", removedPackage, extras, 16777216, null, null, broadcastUserIds, instantUserIds, broadcastAllowList, null);
                packageSender.notifyPackageRemoved(removedPackage, packageRemovedInfo.mUid);
            }
        }
        if (packageRemovedInfo.mIsAppIdRemoved) {
            if (isReplace || isArchived) {
                extras2 = extras;
                extras2.putString("android.intent.extra.PACKAGE_NAME", removedPackage);
            } else {
                extras2 = extras;
            }
            sendPackageBroadcastAndNotify("android.intent.action.UID_REMOVED", null, extras2, 16777216, null, null, broadcastUserIds, instantUserIds, broadcastAllowList, null);
        }
    }

    void sendPackagesSuspendedOrUnsuspendedForUser(final com.android.server.pm.Computer snapshot, final java.lang.String intent, java.lang.String[] pkgList, int[] uidList, boolean quarantined, final int userId) throws java.lang.Throwable {
        final android.os.Bundle extras = new android.os.Bundle(3);
        extras.putStringArray("android.intent.extra.changed_package_list", pkgList);
        extras.putIntArray("android.intent.extra.changed_uid_list", uidList);
        if (quarantined) {
            extras.putBoolean("android.intent.extra.quarantined", true);
        }
        final android.os.Bundle options = new android.app.BroadcastOptions().setDeferralPolicy(2).toBundle();
        final java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver = new java.util.function.BiFunction() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda7
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.pm.BroadcastHelper.filterExtrasChangedPackageList(snapshot, ((java.lang.Integer) obj).intValue(), (android.os.Bundle) obj2);
            }
        };
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendPackagesSuspendedOrUnsuspendedForUser$7(intent, extras, userId, filterExtrasForReceiver, options);
            }
        });
        notifyPackageMonitor(intent, null, extras, new int[]{userId}, null, null, filterExtrasForReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendPackagesSuspendedOrUnsuspendedForUser$7(java.lang.String intent, android.os.Bundle extras, int userId, java.util.function.BiFunction filterExtrasForReceiver, android.os.Bundle options) {
        sendPackageBroadcast(intent, null, extras, 1342177280, null, null, new int[]{userId}, null, null, filterExtrasForReceiver, options);
    }

    void sendMyPackageSuspendedOrUnsuspended(final com.android.server.pm.Computer snapshot, final java.lang.String[] affectedPackages, final boolean suspended, final int userId) {
        final java.lang.String action;
        if (suspended) {
            action = "android.intent.action.MY_PACKAGE_SUSPENDED";
        } else {
            action = "android.intent.action.MY_PACKAGE_UNSUSPENDED";
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendMyPackageSuspendedOrUnsuspended$8(suspended, userId, affectedPackages, snapshot, action);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMyPackageSuspendedOrUnsuspended$8(boolean suspended, int userId, java.lang.String[] affectedPackages, com.android.server.pm.Computer snapshot, java.lang.String action) {
        android.os.Bundle intentExtras;
        android.app.IActivityManager am = android.app.ActivityManager.getService();
        if (am == null) {
            android.util.Slog.wtf("PackageManager", "IActivityManager null. Cannot send MY_PACKAGE_ " + (suspended ? "" : "UN") + "SUSPENDED broadcasts");
            return;
        }
        int[] targetUserIds = {userId};
        for (java.lang.String packageName : affectedPackages) {
            android.os.Bundle appExtras = suspended ? com.android.server.pm.SuspendPackageHelper.getSuspendedPackageAppExtras(snapshot, packageName, userId, 1000) : null;
            if (appExtras != null) {
                intentExtras = new android.os.Bundle(1);
                intentExtras.putBundle("android.intent.extra.SUSPENDED_PACKAGE_EXTRAS", appExtras);
            } else {
                intentExtras = null;
            }
            doSendBroadcast(action, null, intentExtras, 16777248, packageName, null, targetUserIds, false, null, null, null);
        }
    }

    void sendDistractingPackagesChanged(final com.android.server.pm.Computer snapshot, java.lang.String[] pkgList, int[] uidList, final int userId, int distractionFlags) {
        final android.os.Bundle extras = new android.os.Bundle();
        extras.putStringArray("android.intent.extra.changed_package_list", pkgList);
        extras.putIntArray("android.intent.extra.changed_uid_list", uidList);
        extras.putInt("android.intent.extra.distraction_restrictions", distractionFlags);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendDistractingPackagesChanged$10(extras, userId, snapshot);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendDistractingPackagesChanged$10(android.os.Bundle extras, int userId, final com.android.server.pm.Computer snapshot) {
        sendPackageBroadcast("android.intent.action.DISTRACTING_PACKAGES_CHANGED", null, extras, 1073741824, null, null, new int[]{userId}, null, null, new java.util.function.BiFunction() { // from class: com.android.server.pm.BroadcastHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.pm.BroadcastHelper.filterExtrasChangedPackageList(snapshot, ((java.lang.Integer) obj).intValue(), (android.os.Bundle) obj2);
            }
        }, null);
    }

    void sendResourcesChangedBroadcastAndNotify(com.android.server.pm.Computer snapshot, boolean mediaStatus, boolean replacing, java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> packages) throws java.lang.Throwable {
        int size = packages.size();
        java.lang.String[] packageNames = new java.lang.String[size];
        int[] packageUids = new int[size];
        for (int i = 0; i < size; i++) {
            com.android.server.pm.pkg.AndroidPackage pkg = packages.get(i);
            packageNames[i] = pkg.getPackageName();
            packageUids[i] = pkg.getUid();
        }
        sendResourcesChangedBroadcast(snapshot, mediaStatus, replacing, packageNames, packageUids);
        notifyResourcesChanged(mediaStatus, replacing, packageNames, packageUids);
    }

    private void notifyPackageMonitor(java.lang.String action, java.lang.String pkg, android.os.Bundle extras, int[] userIds, int[] instantUserIds, android.util.SparseArray<int[]> broadcastAllowList, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtras) throws java.lang.Throwable {
        this.mPackageMonitorCallbackHelper.notifyPackageMonitor(action, pkg, extras, userIds, instantUserIds, broadcastAllowList, this.mHandler, filterExtras);
    }

    private void notifyResourcesChanged(boolean mediaStatus, boolean replacing, java.lang.String[] pkgNames, int[] uids) throws java.lang.Throwable {
        this.mPackageMonitorCallbackHelper.notifyResourcesChanged(mediaStatus, replacing, pkgNames, uids, this.mHandler);
    }

    public com.android.server.pm.IBroadcastHelperWrapper getWrapper() {
        return this.mWrapper;
    }

    private class BroadcastHelperWrapper implements com.android.server.pm.IBroadcastHelperWrapper {
        private BroadcastHelperWrapper() {
        }

        @Override // com.android.server.pm.IBroadcastHelperWrapper
        public void sendPackageBroadcastAndNotify(java.lang.String action, java.lang.String pkg, android.os.Bundle extras, int flags, java.lang.String targetPkg, android.content.IIntentReceiver finishedReceiver, int[] userIds, int[] instantUserIds, android.util.SparseArray<int[]> broadcastAllowList, android.os.Bundle bOptions) throws java.lang.Throwable {
            com.android.server.pm.BroadcastHelper.this.sendPackageBroadcastAndNotify(action, pkg, extras, flags, targetPkg, finishedReceiver, userIds, instantUserIds, broadcastAllowList, bOptions);
        }

        @Override // com.android.server.pm.IBroadcastHelperWrapper
        public android.os.Handler getHandler() {
            return com.android.server.pm.BroadcastHelper.this.mHandler;
        }
    }
}
