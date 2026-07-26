package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class LauncherAppsService extends com.android.server.SystemService {
    private static final java.lang.String PS_SETTINGS_INTENT = "com.android.settings.action.OPEN_PRIVATE_SPACE_SETTINGS";
    private static final java.lang.String VC_FILE_SUFFIX = ".vc";
    private static final java.lang.String WM_TRACE_DIR = "/data/misc/wmtrace/";
    private static final java.util.Set<java.nio.file.attribute.PosixFilePermission> WM_TRACE_FILE_PERMISSIONS = java.util.Set.of(java.nio.file.attribute.PosixFilePermission.OWNER_WRITE, java.nio.file.attribute.PosixFilePermission.GROUP_READ, java.nio.file.attribute.PosixFilePermission.OTHERS_READ, java.nio.file.attribute.PosixFilePermission.OWNER_READ);
    private static com.android.server.pm.ILauncherAppsServiceExt mLauncherAppsServiceExt = (com.android.server.pm.ILauncherAppsServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.ILauncherAppsServiceExt.class).base((java.lang.Object) null).create();
    private final com.android.server.pm.LauncherAppsService.LauncherAppsImpl mLauncherAppsImpl;

    public static abstract class LauncherAppsServiceInternal {
        public abstract boolean startShortcut(int i, int i2, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, android.graphics.Rect rect, android.os.Bundle bundle, int i3);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public LauncherAppsService(android.content.Context context) {
        super(context);
        this.mLauncherAppsImpl = new com.android.server.pm.LauncherAppsService.LauncherAppsImpl(context);
        mLauncherAppsServiceExt.hookLauncherApps(this.mLauncherAppsImpl, context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("launcherapps", this.mLauncherAppsImpl);
        this.mLauncherAppsImpl.registerLoadingProgressForIncrementalApps();
        com.android.server.LocalServices.addService(com.android.server.pm.LauncherAppsService.LauncherAppsServiceInternal.class, this.mLauncherAppsImpl.mInternal);
    }

    static class BroadcastCookie {
        public final int callingPid;
        public final int callingUid;
        public final java.lang.String packageName;
        public final android.os.UserHandle user;

        BroadcastCookie(android.os.UserHandle userHandle, java.lang.String packageName, int callingPid, int callingUid) {
            this.user = userHandle;
            this.packageName = packageName;
            this.callingUid = callingUid;
            this.callingPid = callingPid;
        }
    }

    static class LauncherAppsImpl extends android.content.pm.ILauncherApps.Stub {
        private static final boolean DEBUG = false;
        private static final java.lang.String FLAG_NON_SYSTEM_ACCESS_TO_HIDDEN_PROFILES = "allow_3p_launchers_access_via_launcher_apps_apis";
        private static final int MULTI_APP = 999;
        private static final java.lang.String NAMESPACE_MULTIUSER = "multiuser";
        private static final java.lang.String TAG = "LauncherAppsService";
        private final android.app.AppOpsManager mAppOpsManager;
        private final android.os.Handler mCallbackHandler;
        private final android.content.Context mContext;
        private final android.app.admin.DevicePolicyManager mDpm;
        final com.android.server.pm.LauncherAppsService.LauncherAppsServiceInternal mInternal;
        private com.android.server.pm.PackageInstallerService mPackageInstallerService;
        private final android.app.role.RoleManager mRoleManager;
        private final com.android.server.pm.LauncherAppsService.LauncherAppsImpl.ShortcutChangeHandler mShortcutChangeHandler;
        private final android.os.UserManager mUm;
        private final com.android.server.pm.LauncherAppsService.LauncherAppsImpl.PackageCallbackList<android.content.pm.IOnAppsChangedListener> mListeners = new com.android.server.pm.LauncherAppsService.LauncherAppsImpl.PackageCallbackList<>();
        private final com.android.server.pm.LauncherAppsService.LauncherAppsImpl.PackageRemovedListener mPackageRemovedListener = new com.android.server.pm.LauncherAppsService.LauncherAppsImpl.PackageRemovedListener();
        private final com.android.server.pm.LauncherAppsService.LauncherAppsImpl.MyPackageMonitor mPackageMonitor = new com.android.server.pm.LauncherAppsService.LauncherAppsImpl.MyOplusPackageMonitor();
        private boolean mIsWatchingPackageBroadcasts = false;
        private final java.util.concurrent.ExecutorService mOnDumpExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();
        private final android.os.RemoteCallbackList<android.window.IDumpCallback> mDumpCallbacks = new android.os.RemoteCallbackList<>();
        private final android.content.pm.IPackageManager mIPM = android.app.AppGlobals.getPackageManager();
        private final com.android.server.pm.UserManagerInternal mUserManagerInternal = (com.android.server.pm.UserManagerInternal) java.util.Objects.requireNonNull((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class));
        private final android.app.usage.UsageStatsManagerInternal mUsageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) java.util.Objects.requireNonNull((android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class));
        private final android.app.ActivityManagerInternal mActivityManagerInternal = (android.app.ActivityManagerInternal) java.util.Objects.requireNonNull((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class));
        private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) java.util.Objects.requireNonNull((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class));
        private final android.content.pm.ShortcutServiceInternal mShortcutServiceInternal = (android.content.pm.ShortcutServiceInternal) java.util.Objects.requireNonNull((android.content.pm.ShortcutServiceInternal) com.android.server.LocalServices.getService(android.content.pm.ShortcutServiceInternal.class));
        private final android.content.pm.PackageManagerInternal mPackageManagerInternal = (android.content.pm.PackageManagerInternal) java.util.Objects.requireNonNull((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class));

        /* JADX WARN: Multi-variable type inference failed */
        public LauncherAppsImpl(android.content.Context context) {
            this.mContext = context;
            this.mUm = (android.os.UserManager) this.mContext.getSystemService("user");
            this.mRoleManager = (android.app.role.RoleManager) this.mContext.getSystemService(android.app.role.RoleManager.class);
            this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
            this.mShortcutServiceInternal.addListener(this.mPackageMonitor);
            this.mShortcutChangeHandler = new com.android.server.pm.LauncherAppsService.LauncherAppsImpl.ShortcutChangeHandler(this.mUserManagerInternal);
            this.mShortcutServiceInternal.addShortcutChangeCallback(this.mShortcutChangeHandler);
            this.mCallbackHandler = com.android.server.pm.LauncherAppsService.mLauncherAppsServiceExt.getFgHandler(com.android.internal.os.BackgroundThread.getHandler());
            this.mDpm = (android.app.admin.DevicePolicyManager) this.mContext.getSystemService("device_policy");
            this.mInternal = new com.android.server.pm.LauncherAppsService.LauncherAppsImpl.LocalService();
        }

        int injectBinderCallingUid() {
            return getCallingUid();
        }

        int injectBinderCallingPid() {
            return getCallingPid();
        }

        final int injectCallingUserId() {
            return android.os.UserHandle.getUserId(injectBinderCallingUid());
        }

        long injectClearCallingIdentity() {
            return android.os.Binder.clearCallingIdentity();
        }

        void injectRestoreCallingIdentity(long token) {
            android.os.Binder.restoreCallingIdentity(token);
        }

        private int getCallingUserId() {
            return android.os.UserHandle.getUserId(injectBinderCallingUid());
        }

        public void addOnAppsChangedListener(java.lang.String callingPackage, android.content.pm.IOnAppsChangedListener listener) throws android.os.RemoteException {
            verifyCallingPackage(callingPackage);
            synchronized (this.mListeners) {
                if (this.mListeners.getRegisteredCallbackCount() == 0) {
                    startWatchingPackageBroadcasts();
                }
                this.mListeners.unregister(listener);
                this.mListeners.register(listener, new com.android.server.pm.LauncherAppsService.BroadcastCookie(android.os.UserHandle.of(getCallingUserId()), callingPackage, injectBinderCallingPid(), injectBinderCallingUid()));
            }
        }

        public void removeOnAppsChangedListener(android.content.pm.IOnAppsChangedListener listener) throws android.os.RemoteException {
            synchronized (this.mListeners) {
                this.mListeners.unregister(listener);
                if (this.mListeners.getRegisteredCallbackCount() == 0) {
                    stopWatchingPackageBroadcasts();
                }
            }
        }

        public void registerPackageInstallerCallback(java.lang.String callingPackage, android.content.pm.IPackageInstallerCallback callback) {
            verifyCallingPackage(callingPackage);
            final com.android.server.pm.LauncherAppsService.BroadcastCookie callerInfo = new com.android.server.pm.LauncherAppsService.BroadcastCookie(new android.os.UserHandle(getCallingUserId()), callingPackage, getCallingPid(), getCallingUid());
            getPackageInstallerService().registerCallback(callback, new java.util.function.IntPredicate() { // from class: com.android.server.pm.LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda4
                @Override // java.util.function.IntPredicate
                public final boolean test(int i) {
                    return this.f$0.lambda$registerPackageInstallerCallback$0(callerInfo, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$registerPackageInstallerCallback$0(com.android.server.pm.LauncherAppsService.BroadcastCookie callerInfo, int eventUserId) {
            return isEnabledProfileOf(callerInfo, new android.os.UserHandle(eventUserId), "shouldReceiveEvent");
        }

        public java.util.List<android.os.UserHandle> getUserProfiles() {
            int[] userIds;
            if (!canAccessHiddenProfile(getCallingUid(), getCallingPid())) {
                userIds = this.mUm.getProfileIdsExcludingHidden(getCallingUserId(), true);
            } else {
                userIds = this.mUm.getEnabledProfileIds(getCallingUserId());
            }
            java.util.List<android.os.UserHandle> result = new java.util.ArrayList<>(userIds.length);
            for (int userId : userIds) {
                result.add(android.os.UserHandle.of(userId));
            }
            return result;
        }

        public android.content.pm.ParceledListSlice<android.content.pm.PackageInstaller.SessionInfo> getAllSessions(java.lang.String callingPackage) {
            int[] userIds;
            verifyCallingPackage(callingPackage);
            java.util.List<android.content.pm.PackageInstaller.SessionInfo> sessionInfos = new java.util.ArrayList<>();
            final int callingUid = android.os.Binder.getCallingUid();
            if (!canAccessHiddenProfile(callingUid, android.os.Binder.getCallingPid())) {
                userIds = this.mUm.getProfileIdsExcludingHidden(getCallingUserId(), true);
            } else {
                userIds = this.mUm.getEnabledProfileIds(getCallingUserId());
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                for (int userId : userIds) {
                    sessionInfos.addAll(getPackageInstallerService().getAllSessions(userId).getList());
                }
                android.os.Binder.restoreCallingIdentity(token);
                sessionInfos.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda6
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return this.f$0.lambda$getAllSessions$1(callingUid, (android.content.pm.PackageInstaller.SessionInfo) obj);
                    }
                });
                return new android.content.pm.ParceledListSlice<>(sessionInfos);
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: shouldFilterSession, reason: merged with bridge method [inline-methods] */
        public boolean lambda$getAllSessions$1(int uid, android.content.pm.PackageInstaller.SessionInfo session) {
            return (session == null || uid == session.getInstallerUid() || this.mPackageManagerInternal.canQueryPackage(uid, session.getAppPackageName())) ? false : true;
        }

        private com.android.server.pm.PackageInstallerService getPackageInstallerService() {
            if (this.mPackageInstallerService == null) {
                try {
                    this.mPackageInstallerService = android.os.ServiceManager.getService("package").getPackageInstaller();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.wtf(TAG, "Error getting IPackageInstaller", e);
                }
            }
            return this.mPackageInstallerService;
        }

        private void startWatchingPackageBroadcasts() {
            if (!this.mIsWatchingPackageBroadcasts) {
                android.content.IntentFilter filter = new android.content.IntentFilter();
                filter.addAction("android.intent.action.PACKAGE_REMOVED_INTERNAL");
                filter.addDataScheme("package");
                this.mContext.registerReceiverAsUser(this.mPackageRemovedListener, android.os.UserHandle.ALL, filter, null, this.mCallbackHandler);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    this.mPackageMonitor.register(this.mContext, android.os.UserHandle.ALL, this.mCallbackHandler);
                    android.os.Binder.restoreCallingIdentity(identity);
                    this.mIsWatchingPackageBroadcasts = true;
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
            }
        }

        private void stopWatchingPackageBroadcasts() {
            if (this.mIsWatchingPackageBroadcasts) {
                this.mContext.unregisterReceiver(this.mPackageRemovedListener);
                this.mPackageMonitor.unregister();
                this.mIsWatchingPackageBroadcasts = false;
            }
        }

        void checkCallbackCount() {
            synchronized (this.mListeners) {
                if (this.mListeners.getRegisteredCallbackCount() == 0) {
                    stopWatchingPackageBroadcasts();
                }
            }
        }

        private boolean canAccessProfile(int targetUserId, java.lang.String message) {
            return canAccessProfile(injectBinderCallingUid(), injectCallingUserId(), injectBinderCallingPid(), targetUserId, message);
        }

        private boolean canAccessProfile(int callingUid, int callingUserId, int callingPid, int targetUserId, java.lang.String message) {
            if (targetUserId == callingUserId || injectHasInteractAcrossUsersFullPermission(callingPid, callingUid)) {
                return true;
            }
            long ident = injectClearCallingIdentity();
            try {
                android.content.pm.UserInfo callingUserInfo = this.mUm.getUserInfo(callingUserId);
                if (callingUserInfo != null && callingUserInfo.isProfile()) {
                    android.util.Slog.w(TAG, message + " for another profile " + targetUserId + " from " + callingUserId + " not allowed");
                    return false;
                }
                injectRestoreCallingIdentity(ident);
                if (!isHiddenProfile(android.os.UserHandle.of(targetUserId)) || canAccessHiddenProfile(callingUid, callingPid)) {
                    return this.mUserManagerInternal.isProfileAccessible(callingUserId, targetUserId, message, true);
                }
                return false;
            } finally {
                injectRestoreCallingIdentity(ident);
            }
        }

        private boolean isHiddenProfile(android.os.UserHandle targetUser) {
            if (!android.multiuser.Flags.enableLauncherAppsHiddenProfileChecks()) {
                return false;
            }
            long identity = injectClearCallingIdentity();
            try {
                android.content.pm.UserProperties properties = this.mUm.getUserProperties(targetUser);
                if (properties == null) {
                    return false;
                }
                return properties.getProfileApiVisibility() == 1;
            } catch (java.lang.IllegalArgumentException e) {
                return false;
            } finally {
                injectRestoreCallingIdentity(identity);
            }
        }

        private void verifyCallingPackage(java.lang.String callingPackage) {
            verifyCallingPackage(callingPackage, injectBinderCallingUid());
        }

        private boolean canAccessHiddenProfile(int callingUid, int callingPid) {
            if (!areHiddenApisChecksEnabled()) {
                return true;
            }
            long ident = injectClearCallingIdentity();
            try {
                com.android.server.pm.pkg.AndroidPackage callingPackage = this.mPackageManagerInternal.getPackage(callingUid);
                if (callingPackage == null) {
                    return false;
                }
                if (this.mContext.checkPermission("android.permission.ACCESS_HIDDEN_PROFILES_FULL", callingPid, callingUid) == 0) {
                    return true;
                }
                if (isAccessToHiddenProfilesForNonSystemAppsForbidden()) {
                    return false;
                }
                if (this.mRoleManager.getRoleHoldersAsUser("android.app.role.HOME", android.os.UserHandle.getUserHandleForUid(callingUid)).contains(callingPackage.getPackageName())) {
                    return this.mContext.checkPermission("android.permission.ACCESS_HIDDEN_PROFILES", callingPid, callingUid) == 0;
                }
                return false;
            } finally {
                injectRestoreCallingIdentity(ident);
            }
        }

        private boolean isAccessToHiddenProfilesForNonSystemAppsForbidden() {
            return !android.provider.DeviceConfig.getBoolean(NAMESPACE_MULTIUSER, FLAG_NON_SYSTEM_ACCESS_TO_HIDDEN_PROFILES, true);
        }

        private boolean areHiddenApisChecksEnabled() {
            return com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enableHidingProfiles() && android.multiuser.Flags.enableLauncherAppsHiddenProfileChecks() && android.multiuser.Flags.enablePermissionToAccessHiddenProfiles() && android.multiuser.Flags.enablePrivateSpaceFeatures();
        }

        void verifyCallingPackage(java.lang.String callingPackage, int callerUid) {
            int packageUid = -1;
            try {
                packageUid = this.mIPM.getPackageUid(callingPackage, 794624L, android.os.UserHandle.getUserId(callerUid));
            } catch (android.os.RemoteException e) {
            }
            if (packageUid < 0) {
                android.util.Log.e(TAG, "Package not found: " + callingPackage);
            }
            if (packageUid != callerUid) {
                throw new java.lang.SecurityException("Calling package name mismatch");
            }
        }

        private android.content.pm.LauncherActivityInfoInternal getHiddenAppActivityInfo(java.lang.String packageName, int callingUid, android.os.UserHandle user) {
            android.content.Intent intent = new android.content.Intent();
            intent.setComponent(new android.content.ComponentName(packageName, android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME));
            java.util.List<android.content.pm.LauncherActivityInfoInternal> apps = queryIntentLauncherActivities(intent, callingUid, user);
            if (apps.size() > 0) {
                return apps.get(0);
            }
            return null;
        }

        public boolean shouldHideFromSuggestions(java.lang.String packageName, android.os.UserHandle user) {
            int userId = user.getIdentifier();
            if (!canAccessProfile(userId, "cannot get shouldHideFromSuggestions")) {
                return false;
            }
            if (com.android.server.pm.PackageArchiver.isArchivingEnabled() && packageName != null && isPackageArchived(packageName, user)) {
                return true;
            }
            if (this.mPackageManagerInternal.filterAppAccess(packageName, android.os.Binder.getCallingUid(), userId)) {
                return false;
            }
            int flags = this.mPackageManagerInternal.getDistractingPackageRestrictions(packageName, userId);
            return (flags & 1) != 0;
        }

        public android.content.pm.ParceledListSlice<android.content.pm.LauncherActivityInfoInternal> getLauncherActivities(java.lang.String callingPackage, java.lang.String packageName, android.os.UserHandle user) throws android.os.RemoteException {
            android.content.pm.LauncherActivityInfoInternal info;
            android.content.pm.ParceledListSlice<android.content.pm.LauncherActivityInfoInternal> launcherActivities = queryActivitiesForUser(callingPackage, new android.content.Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER").setPackage(packageName), user);
            if (com.android.server.pm.PackageArchiver.isArchivingEnabled()) {
                launcherActivities = getActivitiesForArchivedApp(packageName, user, launcherActivities);
            }
            if (android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "show_hidden_icon_apps_enabled", 1) == 0) {
                return launcherActivities;
            }
            if (launcherActivities == null) {
                return null;
            }
            int callingUid = injectBinderCallingUid();
            long ident = injectClearCallingIdentity();
            try {
                if (this.mUm.getUserInfo(user.getIdentifier()) == null) {
                    android.util.Slog.e(TAG, "User " + user.getIdentifier() + "do not exist");
                    return null;
                }
                if (this.mUm.getUserInfo(user.getIdentifier()).isManagedProfile()) {
                    return launcherActivities;
                }
                if (this.mDpm.getDeviceOwnerComponentOnAnyUser() != null) {
                    return launcherActivities;
                }
                java.util.ArrayList<android.content.pm.LauncherActivityInfoInternal> result = new java.util.ArrayList<>(launcherActivities.getList());
                if (packageName != null) {
                    if (result.size() > 0) {
                        return launcherActivities;
                    }
                    android.content.pm.ApplicationInfo appInfo = this.mPackageManagerInternal.getApplicationInfo(packageName, 0L, callingUid, user.getIdentifier());
                    if (shouldShowSyntheticActivity(user, appInfo) && (info = getHiddenAppActivityInfo(packageName, callingUid, user)) != null) {
                        result.add(info);
                    }
                    return new android.content.pm.ParceledListSlice<>(result);
                }
                java.util.HashSet<java.lang.String> visiblePackages = new java.util.HashSet<>();
                java.util.Iterator<android.content.pm.LauncherActivityInfoInternal> it = result.iterator();
                while (it.hasNext()) {
                    visiblePackages.add(it.next().getActivityInfo().packageName);
                }
                java.util.List<android.content.pm.ApplicationInfo> installedPackages = this.mPackageManagerInternal.getInstalledApplications(0L, user.getIdentifier(), callingUid);
                for (android.content.pm.ApplicationInfo applicationInfo : installedPackages) {
                    if (!visiblePackages.contains(applicationInfo.packageName)) {
                        if (shouldShowSyntheticActivity(user, applicationInfo)) {
                            android.content.pm.LauncherActivityInfoInternal info2 = getHiddenAppActivityInfo(applicationInfo.packageName, callingUid, user);
                            if (info2 != null) {
                                result.add(info2);
                            }
                        }
                    }
                }
                return new android.content.pm.ParceledListSlice<>(result);
            } finally {
                injectRestoreCallingIdentity(ident);
            }
        }

        private android.content.pm.ParceledListSlice<android.content.pm.LauncherActivityInfoInternal> getActivitiesForArchivedApp(java.lang.String packageName, android.os.UserHandle user, android.content.pm.ParceledListSlice<android.content.pm.LauncherActivityInfoInternal> launcherActivities) {
            java.util.List<android.content.pm.LauncherActivityInfoInternal> archivedActivities = generateLauncherActivitiesForArchivedApp(packageName, user);
            if (archivedActivities.isEmpty()) {
                return launcherActivities;
            }
            if (launcherActivities == null) {
                return new android.content.pm.ParceledListSlice<>(archivedActivities);
            }
            java.util.List<android.content.pm.LauncherActivityInfoInternal> result = launcherActivities.getList();
            result.addAll(archivedActivities);
            return new android.content.pm.ParceledListSlice<>(result);
        }

        private boolean shouldShowSyntheticActivity(android.os.UserHandle user, android.content.pm.ApplicationInfo appInfo) {
            com.android.server.pm.pkg.AndroidPackage pkg;
            return (appInfo == null || appInfo.isSystemApp() || appInfo.isUpdatedSystemApp() || isManagedProfileAdmin(user, appInfo.packageName) || (pkg = this.mPackageManagerInternal.getPackage(appInfo.packageName)) == null || !requestsPermissions(pkg) || !hasDefaultEnableLauncherActivity(appInfo.packageName)) ? false : true;
        }

        private boolean requestsPermissions(com.android.server.pm.pkg.AndroidPackage pkg) {
            return !com.android.internal.util.ArrayUtils.isEmpty(pkg.getRequestedPermissions());
        }

        private boolean hasDefaultEnableLauncherActivity(java.lang.String packageName) {
            android.content.Intent matchIntent = new android.content.Intent("android.intent.action.MAIN");
            matchIntent.addCategory("android.intent.category.LAUNCHER");
            matchIntent.setPackage(packageName);
            java.util.List<android.content.pm.ResolveInfo> infoList = this.mPackageManagerInternal.queryIntentActivities(matchIntent, matchIntent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 512L, android.os.Binder.getCallingUid(), getCallingUserId());
            int size = infoList.size();
            for (int i = 0; i < size; i++) {
                if (infoList.get(i).activityInfo.enabled) {
                    return true;
                }
            }
            return false;
        }

        private boolean isManagedProfileAdmin(android.os.UserHandle user, java.lang.String packageName) {
            android.content.ComponentName componentName;
            java.util.List<android.content.pm.UserInfo> userInfoList = this.mUm.getProfiles(user.getIdentifier());
            for (int i = 0; i < userInfoList.size(); i++) {
                android.content.pm.UserInfo userInfo = userInfoList.get(i);
                if (userInfo.isManagedProfile() && (componentName = this.mDpm.getProfileOwnerAsUser(userInfo.getUserHandle())) != null && componentName.getPackageName().equals(packageName)) {
                    return true;
                }
            }
            return false;
        }

        public android.content.pm.LauncherActivityInfoInternal resolveLauncherActivityInternal(java.lang.String callingPackage, android.content.ComponentName component, android.os.UserHandle user) throws android.os.RemoteException {
            if (!canAccessProfile(user.getIdentifier(), "Cannot resolve activity") || component == null || component.getPackageName() == null) {
                return null;
            }
            int callingUid = injectBinderCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.content.pm.ActivityInfo activityInfo = this.mPackageManagerInternal.getActivityInfo(component, 786432L, callingUid, user.getIdentifier());
                if (activityInfo == null) {
                    if (com.android.server.pm.PackageArchiver.isArchivingEnabled()) {
                        return getMatchingArchivedAppActivityInfo(component, user);
                    }
                    return null;
                }
                android.content.pm.IncrementalStatesInfo incrementalStatesInfo = this.mPackageManagerInternal.getIncrementalStatesInfo(component.getPackageName(), callingUid, user.getIdentifier());
                if (incrementalStatesInfo == null) {
                    return null;
                }
                return new android.content.pm.LauncherActivityInfoInternal(activityInfo, incrementalStatesInfo, user);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        private android.content.pm.LauncherActivityInfoInternal getMatchingArchivedAppActivityInfo(android.content.ComponentName component, android.os.UserHandle user) {
            java.util.List<android.content.pm.LauncherActivityInfoInternal> archivedActivities = generateLauncherActivitiesForArchivedApp(component.getPackageName(), user);
            if (archivedActivities.isEmpty()) {
                return null;
            }
            for (int i = 0; i < archivedActivities.size(); i++) {
                if (archivedActivities.get(i).getComponentName().equals(component)) {
                    return archivedActivities.get(i);
                }
            }
            android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Expected archived app component name: %s is not available!", new java.lang.Object[]{component}));
            return null;
        }

        public android.content.pm.ParceledListSlice getShortcutConfigActivities(java.lang.String callingPackage, java.lang.String packageName, android.os.UserHandle user) throws android.os.RemoteException {
            if (!this.mShortcutServiceInternal.areShortcutsSupportedOnHomeScreen(user.getIdentifier())) {
                return null;
            }
            return queryActivitiesForUser(callingPackage, new android.content.Intent("android.intent.action.CREATE_SHORTCUT").setPackage(packageName), user);
        }

        private android.content.pm.ParceledListSlice<android.content.pm.LauncherActivityInfoInternal> queryActivitiesForUser(java.lang.String callingPackage, android.content.Intent intent, android.os.UserHandle user) {
            if (!canAccessProfile(user.getIdentifier(), "Cannot retrieve activities")) {
                return null;
            }
            int callingUid = injectBinderCallingUid();
            long ident = injectClearCallingIdentity();
            try {
                return new android.content.pm.ParceledListSlice<>(queryIntentLauncherActivities(intent, callingUid, user));
            } finally {
                injectRestoreCallingIdentity(ident);
            }
        }

        private boolean isPackageArchived(java.lang.String packageName, android.os.UserHandle user) {
            return !getApplicationInfoForArchivedApp(packageName, user).isEmpty();
        }

        private java.util.List<android.content.pm.LauncherActivityInfoInternal> generateLauncherActivitiesForArchivedApp(java.lang.String packageName, android.os.UserHandle user) {
            java.util.List<android.content.pm.ApplicationInfo> applicationInfoList;
            if (!canAccessProfile(user.getIdentifier(), "Cannot retrieve activities")) {
                return java.util.List.of();
            }
            if (packageName == null) {
                applicationInfoList = getApplicationInfoListForAllArchivedApps(user);
            } else {
                applicationInfoList = getApplicationInfoForArchivedApp(packageName, user);
            }
            java.util.List<android.content.pm.LauncherActivityInfoInternal> launcherActivityList = new java.util.ArrayList<>();
            for (int i = 0; i < applicationInfoList.size(); i++) {
                android.content.pm.ApplicationInfo applicationInfo = applicationInfoList.get(i);
                com.android.server.pm.pkg.PackageStateInternal packageState = this.mPackageManagerInternal.getPackageStateInternal(applicationInfo.packageName);
                if (packageState != null) {
                    com.android.server.pm.pkg.ArchiveState archiveState = packageState.getUserStateOrDefault(user.getIdentifier()).getArchiveState();
                    if (archiveState == null) {
                        android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Expected package: %s to be archived but missing ArchiveState in PackageState.", new java.lang.Object[]{applicationInfo.packageName}));
                    } else {
                        java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> archiveActivityInfoList = archiveState.getActivityInfos();
                        for (int j = 0; j < archiveActivityInfoList.size(); j++) {
                            launcherActivityList.add(constructLauncherActivityInfoForArchivedApp(user, applicationInfo, archiveActivityInfoList.get(j)));
                        }
                    }
                }
            }
            return launcherActivityList;
        }

        private static android.content.pm.LauncherActivityInfoInternal constructLauncherActivityInfoForArchivedApp(android.os.UserHandle user, android.content.pm.ApplicationInfo applicationInfo, com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo archiveActivityInfo) {
            android.content.pm.ActivityInfo activityInfo = new android.content.pm.ActivityInfo();
            activityInfo.isArchived = applicationInfo.isArchived;
            activityInfo.applicationInfo = applicationInfo;
            activityInfo.packageName = archiveActivityInfo.getOriginalComponentName().getPackageName();
            activityInfo.name = archiveActivityInfo.getOriginalComponentName().getClassName();
            activityInfo.nonLocalizedLabel = archiveActivityInfo.getTitle();
            return new android.content.pm.LauncherActivityInfoInternal(activityInfo, new android.content.pm.IncrementalStatesInfo(false, 0.0f, 0L), user);
        }

        private java.util.List<android.content.pm.ApplicationInfo> getApplicationInfoListForAllArchivedApps(android.os.UserHandle user) {
            int callingUid = injectBinderCallingUid();
            java.util.List<android.content.pm.ApplicationInfo> installedApplicationInfoList = this.mPackageManagerInternal.getInstalledApplicationsCrossUser(4294967296L, user.getIdentifier(), callingUid);
            java.util.List<android.content.pm.ApplicationInfo> archivedApplicationInfos = new java.util.ArrayList<>();
            for (int i = 0; i < installedApplicationInfoList.size(); i++) {
                android.content.pm.ApplicationInfo installedApplicationInfo = installedApplicationInfoList.get(i);
                if (installedApplicationInfo != null && installedApplicationInfo.isArchived) {
                    archivedApplicationInfos.add(installedApplicationInfo);
                }
            }
            return archivedApplicationInfos;
        }

        private java.util.List<android.content.pm.ApplicationInfo> getApplicationInfoForArchivedApp(final java.lang.String packageName, final android.os.UserHandle user) {
            final int callingUid = injectBinderCallingUid();
            android.content.pm.ApplicationInfo applicationInfo = (android.content.pm.ApplicationInfo) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda3
                public final java.lang.Object getOrThrow() {
                    return this.f$0.lambda$getApplicationInfoForArchivedApp$2(packageName, callingUid, user);
                }
            });
            if (applicationInfo == null || !applicationInfo.isArchived) {
                return java.util.Collections.EMPTY_LIST;
            }
            return java.util.List.of(applicationInfo);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ android.content.pm.ApplicationInfo lambda$getApplicationInfoForArchivedApp$2(java.lang.String packageName, int callingUid, android.os.UserHandle user) throws java.lang.Exception {
            return this.mPackageManagerInternal.getApplicationInfo(packageName, 4294967296L, callingUid, user.getIdentifier());
        }

        private java.util.List<android.content.pm.LauncherActivityInfoInternal> queryIntentLauncherActivities(android.content.Intent intent, int callingUid, android.os.UserHandle user) {
            android.content.pm.IncrementalStatesInfo incrementalStatesInfo;
            java.util.List<android.content.pm.ResolveInfo> apps = this.mPackageManagerInternal.queryIntentActivities(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 786432L, callingUid, user.getIdentifier());
            int numResolveInfos = apps.size();
            java.util.List<android.content.pm.LauncherActivityInfoInternal> results = new java.util.ArrayList<>();
            for (int i = 0; i < numResolveInfos; i++) {
                android.content.pm.ResolveInfo ri = apps.get(i);
                java.lang.String packageName = ri.activityInfo.packageName;
                if (packageName != null && (incrementalStatesInfo = this.mPackageManagerInternal.getIncrementalStatesInfo(packageName, callingUid, user.getIdentifier())) != null) {
                    results.add(new android.content.pm.LauncherActivityInfoInternal(ri.activityInfo, incrementalStatesInfo, user));
                }
            }
            return results;
        }

        public android.content.IntentSender getShortcutConfigActivityIntent(java.lang.String callingPackage, final android.content.ComponentName component, android.os.UserHandle user) throws android.os.RemoteException {
            ensureShortcutPermission(callingPackage);
            android.content.IntentSender intentSender = null;
            if (!canAccessProfile(user.getIdentifier(), "Cannot check package")) {
                return null;
            }
            java.util.Objects.requireNonNull(component);
            int callingUid = injectBinderCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.content.Intent packageIntent = new android.content.Intent("android.intent.action.CREATE_SHORTCUT").setPackage(component.getPackageName());
                java.util.List<android.content.pm.ResolveInfo> apps = this.mPackageManagerInternal.queryIntentActivities(packageIntent, packageIntent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 786432L, callingUid, user.getIdentifier());
                if (!apps.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.pm.LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda7
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return component.getClassName().equals(((android.content.pm.ResolveInfo) obj).activityInfo.name);
                    }
                })) {
                    return null;
                }
                android.content.Intent intent = new android.content.Intent("android.intent.action.CREATE_SHORTCUT").setComponent(component);
                android.app.PendingIntent pi = android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, 1409286144, null, user);
                if (pi != null) {
                    intentSender = pi.getIntentSender();
                }
                return intentSender;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public android.app.PendingIntent getShortcutIntent(java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, android.os.Bundle opts, android.os.UserHandle user) throws java.lang.Throwable {
            java.util.Objects.requireNonNull(callingPackage);
            java.util.Objects.requireNonNull(packageName);
            java.util.Objects.requireNonNull(shortcutId);
            java.util.Objects.requireNonNull(user);
            ensureShortcutPermission(callingPackage);
            if (!canAccessProfile(user.getIdentifier(), "Cannot get shortcuts")) {
                return null;
            }
            com.android.internal.infra.AndroidFuture<android.content.Intent[]> ret = new com.android.internal.infra.AndroidFuture<>();
            this.mShortcutServiceInternal.createShortcutIntentsAsync(getCallingUserId(), callingPackage, packageName, shortcutId, user.getIdentifier(), injectBinderCallingPid(), injectBinderCallingUid(), ret);
            try {
                android.content.Intent[] intents = (android.content.Intent[]) ret.get();
                if (intents == null || intents.length == 0) {
                    return null;
                }
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    try {
                        android.app.PendingIntent pendingIntentInjectCreatePendingIntent = injectCreatePendingIntent(0, intents, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, opts, packageName, this.mPackageManagerInternal.getPackageUid(packageName, 268435456L, user.getIdentifier()));
                        android.os.Binder.restoreCallingIdentity(ident);
                        return pendingIntentInjectCreatePendingIntent;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(ident);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                return null;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0043  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean isPackageEnabled(java.lang.String r13, java.lang.String r14, android.os.UserHandle r15) throws android.os.RemoteException {
            /*
                r12 = this;
                int r0 = r15.getIdentifier()
                java.lang.String r1 = "Cannot check package"
                boolean r0 = r12.canAccessProfile(r0, r1)
                r1 = 0
                if (r0 != 0) goto Le
                return r1
            Le:
                int r0 = r12.injectBinderCallingUid()
                long r8 = android.os.Binder.clearCallingIdentity()
                r2 = 786432(0xc0000, double:3.88549E-318)
                boolean r4 = com.android.server.pm.PackageArchiver.isArchivingEnabled()     // Catch: java.lang.Throwable -> L48
                if (r4 == 0) goto L27
                r4 = 4294967296(0x100000000, double:2.121995791E-314)
                long r2 = r2 | r4
                r10 = r2
                goto L28
            L27:
                r10 = r2
            L28:
                android.content.pm.PackageManagerInternal r2 = r12.mPackageManagerInternal     // Catch: java.lang.Throwable -> L48
                int r7 = r15.getIdentifier()     // Catch: java.lang.Throwable -> L48
                r3 = r14
                r4 = r10
                r6 = r0
                android.content.pm.PackageInfo r2 = r2.getPackageInfo(r3, r4, r6, r7)     // Catch: java.lang.Throwable -> L48
                if (r2 == 0) goto L44
                android.content.pm.ApplicationInfo r3 = r2.applicationInfo     // Catch: java.lang.Throwable -> L48
                boolean r3 = r3.enabled     // Catch: java.lang.Throwable -> L48
                if (r3 != 0) goto L43
                android.content.pm.ApplicationInfo r3 = r2.applicationInfo     // Catch: java.lang.Throwable -> L48
                boolean r3 = r3.isArchived     // Catch: java.lang.Throwable -> L48
                if (r3 == 0) goto L44
            L43:
                r1 = 1
            L44:
                android.os.Binder.restoreCallingIdentity(r8)
                return r1
            L48:
                r1 = move-exception
                android.os.Binder.restoreCallingIdentity(r8)
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.LauncherAppsService.LauncherAppsImpl.isPackageEnabled(java.lang.String, java.lang.String, android.os.UserHandle):boolean");
        }

        public android.os.Bundle getSuspendedPackageLauncherExtras(java.lang.String packageName, android.os.UserHandle user) {
            int callingUid = injectBinderCallingUid();
            int userId = user.getIdentifier();
            if (canAccessProfile(userId, "Cannot get launcher extras") && !this.mPackageManagerInternal.filterAppAccess(packageName, callingUid, userId)) {
                return this.mPackageManagerInternal.getSuspendedPackageLauncherExtras(packageName, userId);
            }
            return null;
        }

        public android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String callingPackage, java.lang.String packageName, int flags, android.os.UserHandle user) throws android.os.RemoteException {
            if (!canAccessProfile(user.getIdentifier(), "Cannot check package")) {
                return null;
            }
            int callingUid = injectBinderCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.content.pm.ApplicationInfo info = this.mPackageManagerInternal.getApplicationInfo(packageName, flags, callingUid, user.getIdentifier());
                return info;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public android.content.pm.LauncherApps.AppUsageLimit getAppUsageLimit(java.lang.String callingPackage, java.lang.String packageName, android.os.UserHandle user) {
            verifyCallingPackage(callingPackage);
            if (!canAccessProfile(user.getIdentifier(), "Cannot access usage limit")) {
                return null;
            }
            if (!this.mActivityTaskManagerInternal.isCallerRecents(android.os.Binder.getCallingUid())) {
                throw new java.lang.SecurityException("Caller is not the recents app");
            }
            android.app.usage.UsageStatsManagerInternal.AppUsageLimitData data = this.mUsageStatsManagerInternal.getAppUsageLimit(packageName, user);
            if (data == null) {
                return null;
            }
            return new android.content.pm.LauncherApps.AppUsageLimit(data.getTotalUsageLimit(), data.getUsageRemaining());
        }

        private void ensureShortcutPermission(java.lang.String callingPackage) {
            ensureShortcutPermission(injectBinderCallingUid(), injectBinderCallingPid(), callingPackage);
        }

        private void ensureShortcutPermission(int callerUid, int callerPid, java.lang.String callingPackage) {
            verifyCallingPackage(callingPackage, callerUid);
            if (!this.mShortcutServiceInternal.hasShortcutHostPermission(android.os.UserHandle.getUserId(callerUid), callingPackage, callerPid, callerUid)) {
                throw new java.lang.SecurityException("Caller can't access shortcut information");
            }
        }

        private void ensureStrictAccessShortcutsPermission(java.lang.String callingPackage) {
            verifyCallingPackage(callingPackage);
            if (!injectHasAccessShortcutsPermission(injectBinderCallingPid(), injectBinderCallingUid())) {
                throw new java.lang.SecurityException("Caller can't access shortcut information");
            }
        }

        boolean injectHasAccessShortcutsPermission(int callingPid, int callingUid) {
            return this.mContext.checkPermission("android.permission.ACCESS_SHORTCUTS", callingPid, callingUid) == 0;
        }

        boolean injectHasInteractAcrossUsersFullPermission(int callingPid, int callingUid) {
            return this.mContext.checkPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingPid, callingUid) == 0;
        }

        android.app.PendingIntent injectCreatePendingIntent(int requestCode, android.content.Intent[] intents, int flags, android.os.Bundle options, java.lang.String ownerPackage, int ownerUserId) {
            return this.mActivityManagerInternal.getPendingIntentActivityAsApp(requestCode, intents, flags, (android.os.Bundle) null, ownerPackage, ownerUserId);
        }

        public android.content.pm.ParceledListSlice getShortcuts(java.lang.String callingPackage, android.content.pm.ShortcutQueryWrapper query, android.os.UserHandle targetUser) {
            ensureShortcutPermission(callingPackage);
            if (!canAccessProfile(targetUser.getIdentifier(), "Cannot get shortcuts")) {
                android.util.Log.e(TAG, "return empty shortcuts because callingPackage " + callingPackage + " cannot access user " + targetUser.getIdentifier());
                return new android.content.pm.ParceledListSlice(java.util.Collections.EMPTY_LIST);
            }
            long changedSince = query.getChangedSince();
            java.lang.String packageName = query.getPackage();
            java.util.List<java.lang.String> shortcutIds = query.getShortcutIds();
            java.util.List<android.content.LocusId> locusIds = query.getLocusIds();
            android.content.ComponentName componentName = query.getActivity();
            int flags = query.getQueryFlags();
            if (shortcutIds != null && packageName == null) {
                throw new java.lang.IllegalArgumentException("To query by shortcut ID, package name must also be set");
            }
            if (locusIds != null && packageName == null) {
                throw new java.lang.IllegalArgumentException("To query by locus ID, package name must also be set");
            }
            if ((query.getQueryFlags() & 2048) != 0) {
                ensureStrictAccessShortcutsPermission(callingPackage);
            }
            return new android.content.pm.ParceledListSlice(this.mShortcutServiceInternal.getShortcuts(getCallingUserId(), callingPackage, changedSince, packageName, shortcutIds, locusIds, componentName, flags, targetUser.getIdentifier(), injectBinderCallingPid(), injectBinderCallingUid()));
        }

        public void getShortcutsAsync(java.lang.String callingPackage, android.content.pm.ShortcutQueryWrapper query, android.os.UserHandle targetUser, com.android.internal.infra.AndroidFuture<java.util.List<android.content.pm.ShortcutInfo>> cb) {
            ensureShortcutPermission(callingPackage);
            if (!canAccessProfile(targetUser.getIdentifier(), "Cannot get shortcuts")) {
                cb.complete(java.util.Collections.EMPTY_LIST);
                return;
            }
            long changedSince = query.getChangedSince();
            java.lang.String packageName = query.getPackage();
            java.util.List<java.lang.String> shortcutIds = query.getShortcutIds();
            java.util.List<android.content.LocusId> locusIds = query.getLocusIds();
            android.content.ComponentName componentName = query.getActivity();
            int flags = query.getQueryFlags();
            if (shortcutIds != null && packageName == null) {
                throw new java.lang.IllegalArgumentException("To query by shortcut ID, package name must also be set");
            }
            if (locusIds != null && packageName == null) {
                throw new java.lang.IllegalArgumentException("To query by locus ID, package name must also be set");
            }
            if ((query.getQueryFlags() & 2048) != 0) {
                ensureStrictAccessShortcutsPermission(callingPackage);
            }
            this.mShortcutServiceInternal.getShortcutsAsync(getCallingUserId(), callingPackage, changedSince, packageName, shortcutIds, locusIds, componentName, flags, targetUser.getIdentifier(), injectBinderCallingPid(), injectBinderCallingUid(), cb);
        }

        public void registerShortcutChangeCallback(java.lang.String callingPackage, android.content.pm.ShortcutQueryWrapper query, android.content.pm.IShortcutChangeCallback callback) {
            ensureShortcutPermission(callingPackage);
            if (query.getShortcutIds() != null && query.getPackage() == null) {
                throw new java.lang.IllegalArgumentException("To query by shortcut ID, package name must also be set");
            }
            if (query.getLocusIds() != null && query.getPackage() == null) {
                throw new java.lang.IllegalArgumentException("To query by locus ID, package name must also be set");
            }
            android.os.UserHandle user = android.os.UserHandle.of(injectCallingUserId());
            if (injectHasInteractAcrossUsersFullPermission(injectBinderCallingPid(), injectBinderCallingUid())) {
                user = null;
            }
            this.mShortcutChangeHandler.addShortcutChangeCallback(callback, query, user);
        }

        public void unregisterShortcutChangeCallback(java.lang.String callingPackage, android.content.pm.IShortcutChangeCallback callback) {
            ensureShortcutPermission(callingPackage);
            this.mShortcutChangeHandler.removeShortcutChangeCallback(callback);
        }

        public void pinShortcuts(java.lang.String callingPackage, java.lang.String packageName, java.util.List<java.lang.String> ids, android.os.UserHandle targetUser) {
            if (!this.mShortcutServiceInternal.areShortcutsSupportedOnHomeScreen(targetUser.getIdentifier())) {
                ensureStrictAccessShortcutsPermission(callingPackage);
            } else {
                ensureShortcutPermission(callingPackage);
            }
            ensureShortcutPermission(callingPackage);
            if (!canAccessProfile(targetUser.getIdentifier(), "Cannot pin shortcuts")) {
                return;
            }
            this.mShortcutServiceInternal.pinShortcuts(getCallingUserId(), callingPackage, packageName, ids, targetUser.getIdentifier());
        }

        public void cacheShortcuts(java.lang.String callingPackage, java.lang.String packageName, java.util.List<java.lang.String> ids, android.os.UserHandle targetUser, int cacheFlags) {
            ensureStrictAccessShortcutsPermission(callingPackage);
            if (!canAccessProfile(targetUser.getIdentifier(), "Cannot cache shortcuts")) {
                return;
            }
            this.mShortcutServiceInternal.cacheShortcuts(getCallingUserId(), callingPackage, packageName, ids, targetUser.getIdentifier(), toShortcutsCacheFlags(cacheFlags));
        }

        public void uncacheShortcuts(java.lang.String callingPackage, java.lang.String packageName, java.util.List<java.lang.String> ids, android.os.UserHandle targetUser, int cacheFlags) {
            ensureStrictAccessShortcutsPermission(callingPackage);
            if (!canAccessProfile(targetUser.getIdentifier(), "Cannot uncache shortcuts")) {
                return;
            }
            this.mShortcutServiceInternal.uncacheShortcuts(getCallingUserId(), callingPackage, packageName, ids, targetUser.getIdentifier(), toShortcutsCacheFlags(cacheFlags));
        }

        public int getShortcutIconResId(java.lang.String callingPackage, java.lang.String packageName, java.lang.String id, int targetUserId) {
            ensureShortcutPermission(callingPackage);
            if (!canAccessProfile(targetUserId, "Cannot access shortcuts")) {
                return 0;
            }
            return this.mShortcutServiceInternal.getShortcutIconResId(getCallingUserId(), callingPackage, packageName, id, targetUserId);
        }

        public android.os.ParcelFileDescriptor getShortcutIconFd(java.lang.String callingPackage, java.lang.String packageName, java.lang.String id, int targetUserId) {
            ensureShortcutPermission(callingPackage);
            if (!canAccessProfile(targetUserId, "Cannot access shortcuts")) {
                return null;
            }
            com.android.internal.infra.AndroidFuture<android.os.ParcelFileDescriptor> ret = new com.android.internal.infra.AndroidFuture<>();
            this.mShortcutServiceInternal.getShortcutIconFdAsync(getCallingUserId(), callingPackage, packageName, id, targetUserId, ret);
            try {
                return (android.os.ParcelFileDescriptor) ret.get();
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        public java.lang.String getShortcutIconUri(java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId) {
            ensureShortcutPermission(callingPackage);
            if (!canAccessProfile(userId, "Cannot access shortcuts")) {
                return null;
            }
            com.android.internal.infra.AndroidFuture<java.lang.String> ret = new com.android.internal.infra.AndroidFuture<>();
            this.mShortcutServiceInternal.getShortcutIconUriAsync(getCallingUserId(), callingPackage, packageName, shortcutId, userId, ret);
            try {
                return (java.lang.String) ret.get();
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        public boolean hasShortcutHostPermission(java.lang.String callingPackage) {
            verifyCallingPackage(callingPackage);
            return this.mShortcutServiceInternal.hasShortcutHostPermission(getCallingUserId(), callingPackage, injectBinderCallingPid(), injectBinderCallingUid());
        }

        public java.util.Map<java.lang.String, android.content.pm.LauncherActivityInfoInternal> getActivityOverrides(java.lang.String callingPackage, int userId) {
            ensureShortcutPermission(callingPackage);
            int callingUid = android.os.Binder.getCallingUid();
            long callerIdentity = android.os.Binder.clearCallingIdentity();
            try {
                java.util.Map<java.lang.String, android.content.pm.LauncherActivityInfoInternal> shortcutOverridesInfo = new android.util.ArrayMap<>();
                android.os.UserHandle managedUserHandle = getManagedProfile(userId);
                if (managedUserHandle == null) {
                    return shortcutOverridesInfo;
                }
                java.util.Map<java.lang.String, java.lang.String> packagesToOverride = android.app.admin.DevicePolicyCache.getInstance().getLauncherShortcutOverrides();
                for (java.util.Map.Entry<java.lang.String, java.lang.String> packageNames : packagesToOverride.entrySet()) {
                    android.content.Intent intent = new android.content.Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER").setPackage(packageNames.getValue());
                    java.util.List<android.content.pm.LauncherActivityInfoInternal> possibleShortcutOverrides = queryIntentLauncherActivities(intent, callingUid, managedUserHandle);
                    if (!possibleShortcutOverrides.isEmpty()) {
                        shortcutOverridesInfo.put(packageNames.getKey(), possibleShortcutOverrides.get(0));
                    }
                }
                return shortcutOverridesInfo;
            } finally {
                android.os.Binder.restoreCallingIdentity(callerIdentity);
            }
        }

        private android.os.UserHandle getManagedProfile(int userId) {
            for (android.content.pm.UserInfo profile : this.mUm.getProfiles(userId)) {
                if (profile.isManagedProfile()) {
                    return profile.getUserHandle();
                }
            }
            return null;
        }

        public boolean startShortcut(java.lang.String callingPackage, java.lang.String packageName, java.lang.String featureId, java.lang.String shortcutId, android.graphics.Rect sourceBounds, android.os.Bundle startActivityOptions, int targetUserId) {
            return startShortcutInner(injectBinderCallingUid(), injectBinderCallingPid(), injectCallingUserId(), callingPackage, packageName, featureId, shortcutId, sourceBounds, startActivityOptions, targetUserId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean startShortcutInner(int callerUid, int callerPid, int callingUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String featureId, java.lang.String shortcutId, android.graphics.Rect sourceBounds, android.os.Bundle startActivityOptions, int targetUserId) {
            android.os.Bundle startActivityOptions2;
            verifyCallingPackage(callingPackage, callerUid);
            if (!canAccessProfile(targetUserId, "Cannot start activity")) {
                return false;
            }
            if (!this.mShortcutServiceInternal.isPinnedByCaller(callingUserId, callingPackage, packageName, shortcutId, targetUserId)) {
                ensureShortcutPermission(callerUid, callerPid, callingPackage);
            }
            com.android.internal.infra.AndroidFuture<android.content.Intent[]> ret = new com.android.internal.infra.AndroidFuture<>();
            this.mShortcutServiceInternal.createShortcutIntentsAsync(getCallingUserId(), callingPackage, packageName, shortcutId, targetUserId, injectBinderCallingPid(), injectBinderCallingUid(), ret);
            try {
                android.content.Intent[] intents = (android.content.Intent[]) ret.get();
                if (intents != null && intents.length != 0) {
                    android.app.ActivityOptions options = android.app.ActivityOptions.fromBundle(startActivityOptions);
                    if (options != null) {
                        if (options.isApplyActivityFlagsForBubbles()) {
                            intents[0].addFlags(524288);
                            intents[0].addFlags(134217728);
                        }
                        if (options.isApplyMultipleTaskFlagForShortcut()) {
                            intents[0].addFlags(134217728);
                        }
                        if (options.isApplyNoUserActionFlagForShortcut()) {
                            intents[0].addFlags(262144);
                        }
                    }
                    intents[0].addFlags(268435456);
                    intents[0].setSourceBounds(sourceBounds);
                    java.lang.String splashScreenThemeResName = this.mShortcutServiceInternal.getShortcutStartingThemeResName(callingUserId, callingPackage, packageName, shortcutId, targetUserId);
                    if (splashScreenThemeResName != null && !splashScreenThemeResName.isEmpty()) {
                        if (startActivityOptions != null) {
                            startActivityOptions2 = startActivityOptions;
                        } else {
                            startActivityOptions2 = new android.os.Bundle();
                        }
                        startActivityOptions2.putString("android.activity.splashScreenTheme", splashScreenThemeResName);
                    } else {
                        startActivityOptions2 = startActivityOptions;
                    }
                    android.os.Bundle startActivityOptions3 = com.android.server.wm.OplusPairTaskManager.prepareOptionsBeforeStartShortcut(startActivityOptions2, this.mShortcutServiceInternal, shortcutId, getCallingUserId(), callingPackage, packageName, targetUserId, injectBinderCallingPid(), injectBinderCallingUid());
                    if (intents.length > 1 && startActivityOptions3 != null && (startActivityOptions3.getBoolean("isSplitScreenCombination", false) || startActivityOptions3.getBoolean("isPsSplitScreenCombination", false))) {
                        if (startActivityOptions3.getInt("userId1") == 999) {
                            intents[0].addOplusFlags(4096);
                            intents[1].addOplusFlags(2048);
                        }
                        if (startActivityOptions3.getInt("userId2") == 999) {
                            intents[1].addOplusFlags(4096);
                            intents[0].addOplusFlags(2048);
                        }
                    }
                    return startShortcutIntentsAsPublisher(intents, packageName, featureId, startActivityOptions3, targetUserId);
                }
                return false;
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                return false;
            }
        }

        private boolean startShortcutIntentsAsPublisher(android.content.Intent[] intents, java.lang.String publisherPackage, java.lang.String publishedFeatureId, android.os.Bundle startActivityOptions, int userId) {
            try {
                int code = this.mActivityTaskManagerInternal.startActivitiesAsPackage(publisherPackage, publishedFeatureId, userId, intents, getActivityOptionsForLauncher(startActivityOptions));
                if (android.app.ActivityManager.isStartResultSuccessful(code)) {
                    return true;
                }
                android.util.Log.e(TAG, "Couldn't start activity, code=" + code);
                return false;
            } catch (java.lang.SecurityException e) {
                return false;
            }
        }

        private android.os.Bundle getActivityOptionsForLauncher(android.os.Bundle startActivityOptions) {
            if (startActivityOptions == null) {
                return android.app.ActivityOptions.makeBasic().setPendingIntentBackgroundActivityStartMode(1).toBundle();
            }
            android.app.ActivityOptions activityOptions = android.app.ActivityOptions.fromBundle(startActivityOptions);
            if (activityOptions.getPendingIntentBackgroundActivityStartMode() == 0) {
                if (startActivityOptions.getBoolean("isSplitScreenCombination", false) || startActivityOptions.getBoolean("isPsSplitScreenCombination", false)) {
                    startActivityOptions.putInt("android.pendingIntent.backgroundActivityAllowed", 1);
                    return startActivityOptions;
                }
                return activityOptions.setPendingIntentBackgroundActivityStartMode(1).toBundle();
            }
            return startActivityOptions;
        }

        public boolean isActivityEnabled(java.lang.String callingPackage, android.content.ComponentName component, android.os.UserHandle user) throws android.os.RemoteException {
            boolean z = false;
            if (!canAccessProfile(user.getIdentifier(), "Cannot check component")) {
                return false;
            }
            if (com.android.server.pm.PackageArchiver.isArchivingEnabled() && component != null && component.getPackageName() != null) {
                java.util.List<android.content.pm.LauncherActivityInfoInternal> archiveActivities = generateLauncherActivitiesForArchivedApp(component.getPackageName(), user);
                if (!archiveActivities.isEmpty()) {
                    for (int i = 0; i < archiveActivities.size(); i++) {
                        if (archiveActivities.get(i).getComponentName().equals(component)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            int callingUid = injectBinderCallingUid();
            int state = this.mPackageManagerInternal.getComponentEnabledSetting(component, callingUid, user.getIdentifier());
            switch (state) {
                case 0:
                default:
                    long ident = android.os.Binder.clearCallingIdentity();
                    try {
                        android.content.pm.ActivityInfo info = this.mPackageManagerInternal.getActivityInfo(component, 786432L, callingUid, user.getIdentifier());
                        if (info != null) {
                            if (info.isEnabled()) {
                                z = true;
                            }
                        }
                        return z;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(ident);
                    }
                case 1:
                    return true;
                case 2:
                case 3:
                case 4:
                    return false;
            }
        }

        public void startSessionDetailsActivityAsUser(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.pm.PackageInstaller.SessionInfo sessionInfo, android.graphics.Rect sourceBounds, android.os.Bundle opts, android.os.UserHandle userHandle) throws android.os.RemoteException {
            int userId = userHandle.getIdentifier();
            if (!canAccessProfile(userId, "Cannot start details activity")) {
                return;
            }
            android.content.Intent i = new android.content.Intent("android.intent.action.VIEW").setData(new android.net.Uri.Builder().scheme("market").authority("details").appendQueryParameter("id", sessionInfo.appPackageName).build()).putExtra("android.intent.extra.REFERRER", new android.net.Uri.Builder().scheme("android-app").authority(callingPackage).build());
            i.setSourceBounds(sourceBounds);
            this.mActivityTaskManagerInternal.startActivityAsUser(caller, callingPackage, callingFeatureId, i, null, 268435456, getActivityOptionsForLauncher(opts), userId);
        }

        public android.app.PendingIntent getActivityLaunchIntent(java.lang.String callingPackage, android.content.ComponentName component, android.os.UserHandle user) {
            if (this.mContext.checkPermission("android.permission.START_TASKS_FROM_RECENTS", injectBinderCallingPid(), injectBinderCallingUid()) != 0) {
                throw new java.lang.SecurityException("Permission START_TASKS_FROM_RECENTS required");
            }
            if (!canAccessProfile(user.getIdentifier(), "Cannot start activity")) {
                throw new android.content.ActivityNotFoundException("Activity could not be found");
            }
            android.content.Intent launchIntent = getMainActivityLaunchIntent(component, user, false);
            if (launchIntent == null) {
                throw new java.lang.SecurityException("Attempt to launch activity without  category Intent.CATEGORY_LAUNCHER " + component);
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return android.app.PendingIntent.getActivityAsUser(this.mContext, 0, launchIntent, 33554432, null, user);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public android.content.pm.LauncherUserInfo getLauncherUserInfo(android.os.UserHandle user) {
            if (!canAccessProfile(user.getIdentifier(), "Can't access LauncherUserInfo for another user")) {
                return null;
            }
            long ident = injectClearCallingIdentity();
            try {
                return this.mUserManagerInternal.getLauncherUserInfo(user.getIdentifier());
            } finally {
                injectRestoreCallingIdentity(ident);
            }
        }

        public java.util.List<java.lang.String> getPreInstalledSystemPackages(android.os.UserHandle user) {
            if (!canAccessProfile(user.getIdentifier(), "Can't access preinstalled packages for another user")) {
                return new java.util.ArrayList();
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                java.lang.String userType = this.mUm.getUserInfo(user.getIdentifier()).userType;
                java.util.Set<java.lang.String> preInstalledPackages = this.mUm.getPreInstallableSystemPackages(userType);
                if (preInstalledPackages == null) {
                    return new java.util.ArrayList();
                }
                return java.util.List.copyOf(preInstalledPackages);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public android.content.IntentSender getAppMarketActivityIntent(java.lang.String callingPackage, java.lang.String packageName, android.os.UserHandle user) {
            if (!canAccessProfile(user.getIdentifier(), "Can't access AppMarketActivity for another user")) {
                return null;
            }
            int callingUser = getCallingUserId();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (packageName == null) {
                    return buildAppMarketIntentSenderForUser(user);
                }
                java.lang.String installerPackageName = getInstallerPackage(packageName, callingUser);
                if (installerPackageName != null && this.mPackageManagerInternal.getPackageUid(installerPackageName, 0L, user.getIdentifier()) >= 0) {
                    android.content.Intent packageInfoIntent = buildMarketPackageInfoIntent(packageName, installerPackageName, callingPackage);
                    return this.mPackageManagerInternal.queryIntentActivities(packageInfoIntent, packageInfoIntent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 131072L, android.os.Process.myUid(), user.getIdentifier()).isEmpty() ? buildAppMarketIntentSenderForUser(user) : buildIntentSenderForUser(packageInfoIntent, user);
                }
                return buildAppMarketIntentSenderForUser(user);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public android.content.IntentSender getPrivateSpaceSettingsIntent() {
            android.content.IntentSender intentSender = null;
            if (!canAccessHiddenProfile(getCallingUid(), getCallingPid())) {
                android.util.Slog.e(TAG, "Caller cannot access hidden profiles");
                return null;
            }
            int callingUser = getCallingUserId();
            int callingUid = getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.content.Intent psSettingsIntent = new android.content.Intent(com.android.server.pm.LauncherAppsService.PS_SETTINGS_INTENT);
                psSettingsIntent.setFlags(268468224);
                java.util.List<android.content.pm.ResolveInfo> ri = this.mPackageManagerInternal.queryIntentActivities(psSettingsIntent, psSettingsIntent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 1048576L, callingUid, callingUser);
                if (ri.isEmpty()) {
                    return null;
                }
                android.app.PendingIntent pi = android.app.PendingIntent.getActivityAsUser(this.mContext, 0, psSettingsIntent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, null, android.os.UserHandle.of(callingUser));
                if (pi != null) {
                    intentSender = pi.getIntentSender();
                }
                return intentSender;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private android.content.IntentSender buildAppMarketIntentSenderForUser(android.os.UserHandle user) {
            android.content.Intent appMarketIntent = new android.content.Intent("android.intent.action.MAIN");
            appMarketIntent.addCategory("android.intent.category.APP_MARKET");
            appMarketIntent.setFlags(268468224);
            return buildIntentSenderForUser(appMarketIntent, user);
        }

        private android.content.IntentSender buildIntentSenderForUser(android.content.Intent intent, android.os.UserHandle user) {
            android.app.PendingIntent pi = android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, null, user);
            if (pi == null) {
                return null;
            }
            return pi.getIntentSender();
        }

        private java.lang.String getInstallerPackage(java.lang.String packageName, int callingUserId) {
            try {
                android.content.pm.InstallSourceInfo info = this.mIPM.getInstallSourceInfo(packageName, callingUserId);
                if (info == null) {
                    return null;
                }
                java.lang.String installerPackageName = info.getInstallingPackageName();
                return installerPackageName;
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(TAG, "Couldn't find installer for " + packageName, re);
                return null;
            }
        }

        private android.content.Intent buildMarketPackageInfoIntent(java.lang.String packageName, java.lang.String installerPackageName, java.lang.String callingPackage) {
            return new android.content.Intent("android.intent.action.VIEW").setData(new android.net.Uri.Builder().scheme("market").authority("details").appendQueryParameter("id", packageName).build()).putExtra("android.intent.extra.REFERRER", new android.net.Uri.Builder().scheme("android-app").authority(callingPackage).build()).setPackage(installerPackageName).setFlags(268435456);
        }

        public void startActivityAsUser(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.ComponentName component, android.graphics.Rect sourceBounds, android.os.Bundle opts, android.os.UserHandle user) throws android.os.RemoteException {
            if (!canAccessProfile(user.getIdentifier(), "Cannot start activity") || com.android.server.pm.LauncherAppsService.mLauncherAppsServiceExt.checkMultiAppUserState(this.mContext, user)) {
                return;
            }
            android.content.Intent launchIntent = getMainActivityLaunchIntent(component, user, true);
            if (launchIntent == null) {
                throw new java.lang.SecurityException("Attempt to launch activity without  category Intent.CATEGORY_LAUNCHER " + component);
            }
            launchIntent.setSourceBounds(sourceBounds);
            this.mActivityTaskManagerInternal.startActivityAsUser(caller, callingPackage, callingFeatureId, launchIntent, null, 268435456, getActivityOptionsForLauncher(opts), user.getIdentifier());
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x008a, code lost:
        
            if (r1 != false) goto L24;
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x008c, code lost:
        
            if (r15 == false) goto L24;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0092, code lost:
        
            if (com.android.server.pm.PackageArchiver.isArchivingEnabled() == false) goto L24;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x0098, code lost:
        
            if (getMatchingArchivedAppActivityInfo(r13, r14) == null) goto L24;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x009a, code lost:
        
            r0.setPackage(null);
            r0.setComponent(r13);
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x00a0, code lost:
        
            r1 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x00a1, code lost:
        
            if (r1 != false) goto L27;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x00a7, code lost:
        
            return null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x00ac, code lost:
        
            return r0;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private android.content.Intent getMainActivityLaunchIntent(android.content.ComponentName r13, android.os.UserHandle r14, boolean r15) {
            /*
                r12 = this;
                android.content.Intent r0 = new android.content.Intent
                java.lang.String r1 = "android.intent.action.MAIN"
                r0.<init>(r1)
                java.lang.String r1 = "android.intent.category.LAUNCHER"
                r0.addCategory(r1)
                r1 = 270532608(0x10200000, float:3.1554436E-29)
                r0.addFlags(r1)
                java.lang.String r1 = r13.getPackageName()
                r0.setPackage(r1)
                r1 = 0
                int r9 = r12.injectBinderCallingUid()
                long r10 = android.os.Binder.clearCallingIdentity()
                android.content.pm.PackageManagerInternal r2 = r12.mPackageManagerInternal     // Catch: java.lang.Throwable -> Lad
                android.content.Context r3 = r12.mContext     // Catch: java.lang.Throwable -> Lad
                android.content.ContentResolver r3 = r3.getContentResolver()     // Catch: java.lang.Throwable -> Lad
                java.lang.String r4 = r0.resolveTypeIfNeeded(r3)     // Catch: java.lang.Throwable -> Lad
                int r8 = r14.getIdentifier()     // Catch: java.lang.Throwable -> Lad
                r5 = 786432(0xc0000, double:3.88549E-318)
                r3 = r0
                r7 = r9
                java.util.List r2 = r2.queryIntentActivities(r3, r4, r5, r7, r8)     // Catch: java.lang.Throwable -> Lad
                int r3 = r2.size()     // Catch: java.lang.Throwable -> Lad
                r4 = 0
            L3f:
                r5 = 0
                if (r4 >= r3) goto L8a
                java.lang.Object r6 = r2.get(r4)     // Catch: java.lang.Throwable -> Lad
                android.content.pm.ResolveInfo r6 = (android.content.pm.ResolveInfo) r6     // Catch: java.lang.Throwable -> Lad
                android.content.pm.ActivityInfo r6 = r6.activityInfo     // Catch: java.lang.Throwable -> Lad
                java.lang.String r7 = r6.packageName     // Catch: java.lang.Throwable -> Lad
                java.lang.String r8 = r13.getPackageName()     // Catch: java.lang.Throwable -> Lad
                boolean r7 = r7.equals(r8)     // Catch: java.lang.Throwable -> Lad
                if (r7 == 0) goto L87
                java.lang.String r7 = r6.name     // Catch: java.lang.Throwable -> Lad
                java.lang.String r8 = r13.getClassName()     // Catch: java.lang.Throwable -> Lad
                boolean r7 = r7.equals(r8)     // Catch: java.lang.Throwable -> Lad
                if (r7 == 0) goto L87
                boolean r7 = r6.exported     // Catch: java.lang.Throwable -> Lad
                if (r7 == 0) goto L6e
                r0.setPackage(r5)     // Catch: java.lang.Throwable -> Lad
                r0.setComponent(r13)     // Catch: java.lang.Throwable -> Lad
                r1 = 1
                goto L8a
            L6e:
                java.lang.SecurityException r5 = new java.lang.SecurityException     // Catch: java.lang.Throwable -> Lad
                java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lad
                r7.<init>()     // Catch: java.lang.Throwable -> Lad
                java.lang.String r8 = "Cannot launch non-exported components "
                java.lang.StringBuilder r7 = r7.append(r8)     // Catch: java.lang.Throwable -> Lad
                java.lang.StringBuilder r7 = r7.append(r13)     // Catch: java.lang.Throwable -> Lad
                java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> Lad
                r5.<init>(r7)     // Catch: java.lang.Throwable -> Lad
                throw r5     // Catch: java.lang.Throwable -> Lad
            L87:
                int r4 = r4 + 1
                goto L3f
            L8a:
                if (r1 != 0) goto La1
                if (r15 == 0) goto La1
                boolean r4 = com.android.server.pm.PackageArchiver.isArchivingEnabled()     // Catch: java.lang.Throwable -> Lad
                if (r4 == 0) goto La1
                android.content.pm.LauncherActivityInfoInternal r4 = r12.getMatchingArchivedAppActivityInfo(r13, r14)     // Catch: java.lang.Throwable -> Lad
                if (r4 == 0) goto La1
                r0.setPackage(r5)     // Catch: java.lang.Throwable -> Lad
                r0.setComponent(r13)     // Catch: java.lang.Throwable -> Lad
                r1 = 1
            La1:
                if (r1 != 0) goto La8
            La4:
                android.os.Binder.restoreCallingIdentity(r10)
                return r5
            La8:
                android.os.Binder.restoreCallingIdentity(r10)
                return r0
            Lad:
                r2 = move-exception
                android.os.Binder.restoreCallingIdentity(r10)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.LauncherAppsService.LauncherAppsImpl.getMainActivityLaunchIntent(android.content.ComponentName, android.os.UserHandle, boolean):android.content.Intent");
        }

        public void showAppDetailsAsUser(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.ComponentName component, android.graphics.Rect sourceBounds, android.os.Bundle opts, android.os.UserHandle user) throws java.lang.Throwable {
            if (!canAccessProfile(user.getIdentifier(), "Cannot show app details")) {
                return;
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                java.lang.String packageName = component.getPackageName();
                int uId = -1;
                try {
                    uId = this.mContext.getPackageManager().getApplicationInfo(packageName, 4194304).uid;
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Log.d(TAG, "package not found: " + e);
                }
                android.content.Intent intent = new android.content.Intent("android.settings.APPLICATION_DETAILS_SETTINGS", android.net.Uri.fromParts("package", packageName, null));
                intent.putExtra("uId", uId);
                intent.setFlags(268468224);
                try {
                    intent.setSourceBounds(sourceBounds);
                    com.android.server.pm.LauncherAppsService.mLauncherAppsServiceExt.addExtraUserHandle(intent, user);
                    android.os.Binder.restoreCallingIdentity(ident);
                    this.mActivityTaskManagerInternal.startActivityAsUser(caller, callingPackage, callingFeatureId, intent, null, 268435456, getActivityOptionsForLauncher(opts), user.getIdentifier());
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Binder.restoreCallingIdentity(ident);
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                android.os.Binder.restoreCallingIdentity(ident);
                throw th;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback cb, android.os.ResultReceiver receiver) {
            int callingUid = injectBinderCallingUid();
            if (callingUid != 2000 && callingUid != 0) {
                throw new java.lang.SecurityException("Caller must be shell");
            }
            long token = injectClearCallingIdentity();
            try {
                int status = new com.android.server.pm.LauncherAppsService.LauncherAppsImpl.LauncherAppsShellCommand().exec(this, in, out, err, args, cb, receiver);
                if (receiver != null) {
                    receiver.send(status, null);
                }
            } finally {
                injectRestoreCallingIdentity(token);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        class LauncherAppsShellCommand extends android.os.ShellCommand {
            private LauncherAppsShellCommand() {
            }

            public int onCommand(java.lang.String cmd) {
                if ("dump-view-hierarchies".equals(cmd)) {
                    dumpViewCaptureDataToShell();
                    return 0;
                }
                return handleDefaultCommands(cmd);
            }

            private void dumpViewCaptureDataToShell() {
                try {
                    final java.util.zip.ZipOutputStream zipOs = new java.util.zip.ZipOutputStream(getRawOutputStream());
                    try {
                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.forEachViewCaptureWindow(new java.util.function.BiConsumer() { // from class: com.android.server.pm.LauncherAppsService$LauncherAppsImpl$LauncherAppsShellCommand$$ExternalSyntheticLambda0
                            @Override // java.util.function.BiConsumer
                            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                                this.f$0.lambda$dumpViewCaptureDataToShell$0(zipOs, (java.lang.String) obj, (java.io.InputStream) obj2);
                            }
                        });
                        zipOs.close();
                    } finally {
                    }
                } catch (java.io.IOException e) {
                    getErrPrintWriter().write("Failed to create or close zip output stream: " + e.getMessage());
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$dumpViewCaptureDataToShell$0(java.util.zip.ZipOutputStream zipOs, java.lang.String fileName, java.io.InputStream is) {
                try {
                    zipOs.putNextEntry(new java.util.zip.ZipEntry("FS" + fileName));
                    com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.transferViewCaptureData(is, zipOs);
                    zipOs.closeEntry();
                } catch (java.io.IOException e) {
                    getErrPrintWriter().write("Failed to output " + fileName + " data to shell: " + e.getMessage());
                }
            }

            public void onHelp() {
                java.io.PrintWriter pw = getOutPrintWriter();
                pw.println("Usage: cmd launcherapps COMMAND [options ...]");
                pw.println();
                pw.println("cmd launcherapps dump-view-hierarchies");
                pw.println("    Output captured view hierarchies. Files will be generated in ");
                pw.println("    `/data/misc/wmtrace/`. After pulling the data to your device,");
                pw.println("     you can upload / visualize it at `go/winscope`.");
                pw.println();
            }
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            super.dump(fd, pw, args);
            forEachViewCaptureWindow(new com.android.server.pm.LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda2(this));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpViewCaptureDataToWmTrace(java.lang.String fileName, java.io.InputStream is) {
            java.nio.file.Path outPath = java.nio.file.Paths.get(fileName, new java.lang.String[0]);
            try {
                java.io.OutputStream os = java.nio.file.Files.newOutputStream(outPath, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
                try {
                    transferViewCaptureData(is, os);
                    java.nio.file.Files.setPosixFilePermissions(outPath, com.android.server.pm.LauncherAppsService.WM_TRACE_FILE_PERMISSIONS);
                    if (os != null) {
                        os.close();
                    }
                } finally {
                }
            } catch (java.io.IOException e) {
                android.util.Log.d(TAG, "failed to write data to " + fileName + " in wmtrace dir", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void transferViewCaptureData(java.io.InputStream is, java.io.OutputStream os) throws java.io.IOException {
            java.io.DataInputStream dataInputStream = new java.io.DataInputStream(is);
            new com.android.internal.util.SizedInputStream(dataInputStream, dataInputStream.readInt()).transferTo(os);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void forEachViewCaptureWindow(final java.util.function.BiConsumer<java.lang.String, java.io.InputStream> outputtingConsumer) {
            try {
                this.mOnDumpExecutor.submit(new java.lang.Runnable() { // from class: com.android.server.pm.LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$forEachViewCaptureWindow$4(outputtingConsumer);
                    }
                }).get();
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                android.util.Log.e(TAG, "background work was interrupted", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$forEachViewCaptureWindow$4(java.util.function.BiConsumer outputtingConsumer) {
            try {
                for (int i = this.mDumpCallbacks.beginBroadcast() - 1; i >= 0; i--) {
                    java.lang.String packageName = (java.lang.String) this.mDumpCallbacks.getBroadcastCookie(i);
                    java.lang.String fileName = com.android.server.pm.LauncherAppsService.WM_TRACE_DIR + packageName + "_" + i + com.android.server.pm.LauncherAppsService.VC_FILE_SUFFIX;
                    try {
                        android.os.ParcelFileDescriptor[] pipe = android.os.ParcelFileDescriptor.createPipe();
                        this.mDumpCallbacks.getBroadcastItem(i).onDump(pipe[1]);
                        android.os.ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new android.os.ParcelFileDescriptor.AutoCloseInputStream(pipe[0]);
                        outputtingConsumer.accept(fileName, autoCloseInputStream);
                        autoCloseInputStream.close();
                    } catch (java.lang.Exception e) {
                        android.util.Log.d(TAG, "failed to pipe view capture data", e);
                    }
                }
            } finally {
                this.mDumpCallbacks.finishBroadcast();
            }
        }

        public void saveViewCaptureData() {
            int status = android.content.PermissionChecker.checkCallingOrSelfPermissionForPreflight(this.mContext, "android.permission.READ_FRAME_BUFFER");
            if (status == 0) {
                forEachViewCaptureWindow(new com.android.server.pm.LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda2(this));
            } else {
                android.util.Log.w(TAG, "caller lacks permissions to save view capture data");
            }
        }

        public void registerDumpCallback(android.window.IDumpCallback cb) {
            int status = android.content.PermissionChecker.checkCallingOrSelfPermissionForPreflight(this.mContext, "android.permission.READ_FRAME_BUFFER");
            if (status == 0) {
                java.lang.String name = this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid());
                this.mDumpCallbacks.register(cb, name);
            } else {
                android.util.Log.w(TAG, "caller lacks permissions to registerDumpCallback");
            }
        }

        public void unRegisterDumpCallback(android.window.IDumpCallback cb) {
            int status = android.content.PermissionChecker.checkCallingOrSelfPermissionForPreflight(this.mContext, "android.permission.READ_FRAME_BUFFER");
            if (status == 0) {
                this.mDumpCallbacks.unregister(cb);
            } else {
                android.util.Log.w(TAG, "caller lacks permissions to unRegisterDumpCallback");
            }
        }

        public void setArchiveCompatibilityOptions(final boolean enableIconOverlay, final boolean enableUnarchivalConfirmation) {
            final int callingUid = android.os.Binder.getCallingUid();
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda1
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$setArchiveCompatibilityOptions$5(callingUid, enableIconOverlay, enableUnarchivalConfirmation);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setArchiveCompatibilityOptions$5(int i, boolean z, boolean z2) throws java.lang.Exception {
            this.mAppOpsManager.setUidMode(145, i, !z ? 1 : 0);
            this.mAppOpsManager.setUidMode(146, i, !z2 ? 1 : 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEnabledProfileOf(com.android.server.pm.LauncherAppsService.BroadcastCookie cookie, android.os.UserHandle user, java.lang.String debugMsg) {
            if (!isHiddenProfile(user) || canAccessHiddenProfile(cookie.callingUid, cookie.callingPid)) {
                return this.mUserManagerInternal.isProfileAccessible(cookie.user.getIdentifier(), user.getIdentifier(), debugMsg, false);
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isPackageVisibleToListener(java.lang.String packageName, com.android.server.pm.LauncherAppsService.BroadcastCookie cookie, android.os.UserHandle user) {
            return !this.mPackageManagerInternal.filterAppAccess(packageName, cookie.callingUid, user.getIdentifier(), false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isCallingAppIdAllowed(int[] appIdAllowList, int appId) {
            return appIdAllowList == null || appId < 10000 || java.util.Arrays.binarySearch(appIdAllowList, appId) > -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String[] getFilteredPackageNames(java.lang.String[] packageNames, com.android.server.pm.LauncherAppsService.BroadcastCookie cookie, android.os.UserHandle user) {
            java.util.List<java.lang.String> filteredPackageNames = new java.util.ArrayList<>();
            for (java.lang.String packageName : packageNames) {
                if (isPackageVisibleToListener(packageName, cookie, user)) {
                    filteredPackageNames.add(packageName);
                }
            }
            return (java.lang.String[]) filteredPackageNames.toArray(new java.lang.String[filteredPackageNames.size()]);
        }

        private int toShortcutsCacheFlags(int cacheFlags) {
            int ret = 0;
            if (cacheFlags == 0) {
                ret = 16384;
            } else if (cacheFlags == 1) {
                ret = 1073741824;
            } else if (cacheFlags == 2) {
                ret = 536870912;
            }
            com.android.internal.util.Preconditions.checkArgumentPositive(ret, "Invalid cache owner");
            return ret;
        }

        void postToPackageMonitorHandler(java.lang.Runnable r) {
            this.mCallbackHandler.post(r);
        }

        void registerLoadingProgressForIncrementalApps() {
            java.util.List<android.os.UserHandle> users = this.mUm.getUserProfiles();
            if (users == null) {
                return;
            }
            for (final android.os.UserHandle user : users) {
                this.mPackageManagerInternal.forEachInstalledPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$registerLoadingProgressForIncrementalApps$6(user, (com.android.server.pm.pkg.AndroidPackage) obj);
                    }
                }, user.getIdentifier());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$registerLoadingProgressForIncrementalApps$6(android.os.UserHandle user, com.android.server.pm.pkg.AndroidPackage pkg) {
            java.lang.String packageName = pkg.getPackageName();
            android.content.pm.IncrementalStatesInfo info = this.mPackageManagerInternal.getIncrementalStatesInfo(packageName, android.os.Process.myUid(), user.getIdentifier());
            if (info != null && info.isLoading()) {
                this.mPackageManagerInternal.registerInstalledLoadingProgressCallback(packageName, new com.android.server.pm.LauncherAppsService.LauncherAppsImpl.PackageLoadingProgressCallback(packageName, user), user.getIdentifier());
            }
        }

        public static class ShortcutChangeHandler implements android.content.pm.LauncherApps.ShortcutChangeCallback {
            private final android.os.RemoteCallbackList<android.content.pm.IShortcutChangeCallback> mCallbacks = new android.os.RemoteCallbackList<>();
            private final com.android.server.pm.UserManagerInternal mUserManagerInternal;

            ShortcutChangeHandler(com.android.server.pm.UserManagerInternal userManager) {
                this.mUserManagerInternal = userManager;
            }

            public synchronized void addShortcutChangeCallback(android.content.pm.IShortcutChangeCallback callback, android.content.pm.ShortcutQueryWrapper query, android.os.UserHandle user) {
                this.mCallbacks.unregister(callback);
                this.mCallbacks.register(callback, new android.util.Pair(query, user));
            }

            public synchronized void removeShortcutChangeCallback(android.content.pm.IShortcutChangeCallback callback) {
                this.mCallbacks.unregister(callback);
            }

            public void onShortcutsAddedOrUpdated(java.lang.String packageName, java.util.List<android.content.pm.ShortcutInfo> shortcuts, android.os.UserHandle user) {
                onShortcutEvent(packageName, shortcuts, user, false);
            }

            public void onShortcutsRemoved(java.lang.String packageName, java.util.List<android.content.pm.ShortcutInfo> shortcuts, android.os.UserHandle user) {
                onShortcutEvent(packageName, shortcuts, user, true);
            }

            private void onShortcutEvent(java.lang.String packageName, java.util.List<android.content.pm.ShortcutInfo> shortcuts, android.os.UserHandle user, boolean shortcutsRemoved) {
                int count = this.mCallbacks.beginBroadcast();
                for (int i = 0; i < count; i++) {
                    android.content.pm.IShortcutChangeCallback callback = this.mCallbacks.getBroadcastItem(i);
                    android.util.Pair<android.content.pm.ShortcutQueryWrapper, android.os.UserHandle> cookie = (android.util.Pair) this.mCallbacks.getBroadcastCookie(i);
                    android.os.UserHandle callbackUser = (android.os.UserHandle) cookie.second;
                    if (callbackUser == null || hasUserAccess(callbackUser, user)) {
                        java.util.List<android.content.pm.ShortcutInfo> matchedList = filterShortcutsByQuery(packageName, shortcuts, (android.content.pm.ShortcutQueryWrapper) cookie.first, shortcutsRemoved);
                        if (!com.android.internal.util.CollectionUtils.isEmpty(matchedList)) {
                            if (shortcutsRemoved) {
                                try {
                                    callback.onShortcutsRemoved(packageName, matchedList, user);
                                } catch (android.os.RemoteException e) {
                                }
                            } else {
                                callback.onShortcutsAddedOrUpdated(packageName, matchedList, user);
                            }
                        }
                    }
                }
                this.mCallbacks.finishBroadcast();
            }

            public static java.util.List<android.content.pm.ShortcutInfo> filterShortcutsByQuery(java.lang.String packageName, java.util.List<android.content.pm.ShortcutInfo> shortcuts, android.content.pm.ShortcutQueryWrapper query, boolean shortcutsRemoved) {
                int flags;
                long changedSince = query.getChangedSince();
                java.lang.String queryPackage = query.getPackage();
                java.util.List<java.lang.String> shortcutIds = query.getShortcutIds();
                java.util.List<android.content.LocusId> locusIds = query.getLocusIds();
                android.content.ComponentName activity = query.getActivity();
                int flags2 = query.getQueryFlags();
                if (queryPackage != null && !queryPackage.equals(packageName)) {
                    return null;
                }
                java.util.List<android.content.pm.ShortcutInfo> matches = new java.util.ArrayList<>();
                boolean matchDynamic = (flags2 & 1) != 0;
                boolean matchPinned = (flags2 & 2) != 0;
                boolean matchManifest = (flags2 & 8) != 0;
                boolean matchCached = (flags2 & 16) != 0;
                int shortcutFlags = (matchDynamic ? 1 : 0) | (matchPinned ? 2 : 0) | (matchManifest ? 32 : 0) | (matchCached ? 1610629120 : 0);
                int i = 0;
                while (i < shortcuts.size()) {
                    java.lang.String queryPackage2 = queryPackage;
                    android.content.pm.ShortcutInfo si = shortcuts.get(i);
                    if (activity != null) {
                        flags = flags2;
                        if (!activity.equals(si.getActivity())) {
                        }
                        i++;
                        flags2 = flags;
                        queryPackage = queryPackage2;
                    } else {
                        flags = flags2;
                    }
                    if ((changedSince == 0 || changedSince <= si.getLastChangedTimestamp()) && ((shortcutIds == null || shortcutIds.contains(si.getId())) && ((locusIds == null || locusIds.contains(si.getLocusId())) && (shortcutsRemoved || (si.getFlags() & shortcutFlags) != 0)))) {
                        matches.add(si);
                    }
                    i++;
                    flags2 = flags;
                    queryPackage = queryPackage2;
                }
                return matches;
            }

            private boolean hasUserAccess(android.os.UserHandle callbackUser, android.os.UserHandle shortcutUser) {
                int callbackUserId = callbackUser.getIdentifier();
                int shortcutUserId = shortcutUser.getIdentifier();
                if (shortcutUser == callbackUser) {
                    return true;
                }
                return this.mUserManagerInternal.isProfileAccessible(callbackUserId, shortcutUserId, null, false);
            }
        }

        private class PackageRemovedListener extends android.content.BroadcastReceiver {
            private PackageRemovedListener() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                if (userId == -10000) {
                    android.util.Slog.w(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Intent broadcast does not contain user handle: " + intent);
                    return;
                }
                java.lang.String action = intent.getAction();
                if ("android.intent.action.PACKAGE_REMOVED_INTERNAL".equals(action)) {
                    java.lang.String packageName = getPackageName(intent);
                    int[] appIdAllowList = intent.getIntArrayExtra("android.intent.extra.VISIBILITY_ALLOW_LIST");
                    if (packageName != null && !intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                        android.os.UserHandle user = new android.os.UserHandle(userId);
                        int n = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                        for (int i = 0; i < n; i++) {
                            try {
                                android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i);
                                com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i);
                                if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, user, "onPackageRemoved") && com.android.server.pm.LauncherAppsService.LauncherAppsImpl.isCallingAppIdAllowed(appIdAllowList, android.os.UserHandle.getAppId(cookie.callingUid)) && !com.android.server.pm.LauncherAppsService.mLauncherAppsServiceExt.isOhideAndLauncherCookie(intent.getExtras(), cookie.packageName)) {
                                    try {
                                        listener.onPackageRemoved(user, packageName);
                                    } catch (android.os.RemoteException re) {
                                        android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Callback failed ", re);
                                    }
                                }
                            } finally {
                                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                            }
                        }
                    }
                }
            }

            private java.lang.String getPackageName(android.content.Intent intent) {
                android.net.Uri uri = intent.getData();
                if (uri == null) {
                    return null;
                }
                java.lang.String pkg = uri.getSchemeSpecificPart();
                return pkg;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        class MyPackageMonitor extends com.android.internal.content.PackageMonitor implements android.content.pm.ShortcutServiceInternal.ShortcutChangeListener {
            private MyPackageMonitor() {
            }

            public void onPackageAdded(java.lang.String packageName, int uid) {
                android.os.UserHandle user = new android.os.UserHandle(getChangingUserId());
                int n = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    try {
                        android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i);
                        com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i);
                        if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, user, "onPackageAdded") && com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isPackageVisibleToListener(packageName, cookie, user)) {
                            try {
                                listener.onPackageAdded(user, packageName);
                            } catch (android.os.RemoteException re) {
                                android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Callback failed ", re);
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                        throw th;
                    }
                }
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                super.onPackageAdded(packageName, uid);
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mPackageManagerInternal.registerInstalledLoadingProgressCallback(packageName, com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.new PackageLoadingProgressCallback(packageName, user), user.getIdentifier());
            }

            public void onPackageModified(java.lang.String packageName) {
                onPackageChanged(packageName);
                super.onPackageModified(packageName);
            }

            private void onPackageChanged(java.lang.String packageName) {
                android.os.UserHandle user = new android.os.UserHandle(getChangingUserId());
                int n = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    try {
                        android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i);
                        com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i);
                        if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, user, "onPackageModified") && com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isPackageVisibleToListener(packageName, cookie, user)) {
                            try {
                                listener.onPackageChanged(user, packageName);
                            } catch (android.os.RemoteException re) {
                                android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Callback failed ", re);
                            }
                        }
                    } finally {
                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                    }
                }
            }

            public void onPackagesAvailable(java.lang.String[] packages) {
                android.os.UserHandle user = new android.os.UserHandle(getChangingUserId());
                int n = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    try {
                        android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i);
                        com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i);
                        if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, user, "onPackagesAvailable")) {
                            java.lang.String[] filteredPackages = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.getFilteredPackageNames(packages, cookie, user);
                            if (!com.android.internal.util.ArrayUtils.isEmpty(filteredPackages)) {
                                try {
                                    listener.onPackagesAvailable(user, filteredPackages, isReplacing());
                                } catch (android.os.RemoteException re) {
                                    android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Callback failed ", re);
                                }
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                        throw th;
                    }
                }
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                super.onPackagesAvailable(packages);
            }

            public void onPackagesUnavailable(java.lang.String[] packages) {
                android.os.UserHandle user = new android.os.UserHandle(getChangingUserId());
                int n = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    try {
                        android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i);
                        com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i);
                        if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, user, "onPackagesUnavailable")) {
                            java.lang.String[] filteredPackages = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.getFilteredPackageNames(packages, cookie, user);
                            if (!com.android.internal.util.ArrayUtils.isEmpty(filteredPackages)) {
                                try {
                                    listener.onPackagesUnavailable(user, filteredPackages, isReplacing());
                                } catch (android.os.RemoteException re) {
                                    android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Callback failed ", re);
                                }
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                        throw th;
                    }
                }
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                super.onPackagesUnavailable(packages);
            }

            public void onPackagesSuspended(java.lang.String[] packages) {
                android.os.UserHandle user = new android.os.UserHandle(getChangingUserId());
                java.util.ArrayList<android.util.Pair<java.lang.String, android.os.Bundle>> packagesWithExtras = new java.util.ArrayList<>();
                java.util.ArrayList<java.lang.String> packagesWithoutExtras = new java.util.ArrayList<>();
                for (java.lang.String pkg : packages) {
                    android.os.Bundle launcherExtras = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mPackageManagerInternal.getSuspendedPackageLauncherExtras(pkg, user.getIdentifier());
                    if (launcherExtras != null) {
                        packagesWithExtras.add(new android.util.Pair<>(pkg, launcherExtras));
                    } else {
                        packagesWithoutExtras.add(pkg);
                    }
                }
                java.lang.String[] packagesNullExtras = (java.lang.String[]) packagesWithoutExtras.toArray(new java.lang.String[packagesWithoutExtras.size()]);
                int n = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    try {
                        android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i);
                        com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i);
                        if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, user, "onPackagesSuspended")) {
                            java.lang.String[] filteredPackagesWithoutExtras = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.getFilteredPackageNames(packagesNullExtras, cookie, user);
                            try {
                                if (!com.android.internal.util.ArrayUtils.isEmpty(filteredPackagesWithoutExtras)) {
                                    listener.onPackagesSuspended(user, filteredPackagesWithoutExtras, (android.os.Bundle) null);
                                }
                                for (int idx = 0; idx < packagesWithExtras.size(); idx++) {
                                    android.util.Pair<java.lang.String, android.os.Bundle> packageExtraPair = packagesWithExtras.get(idx);
                                    if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isPackageVisibleToListener((java.lang.String) packageExtraPair.first, cookie, user)) {
                                        listener.onPackagesSuspended(user, new java.lang.String[]{(java.lang.String) packageExtraPair.first}, (android.os.Bundle) packageExtraPair.second);
                                    }
                                }
                            } catch (android.os.RemoteException re) {
                                android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Callback failed ", re);
                            }
                        }
                    } finally {
                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                    }
                }
            }

            public void onPackagesUnsuspended(java.lang.String[] packages) {
                android.os.UserHandle user = new android.os.UserHandle(getChangingUserId());
                int n = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    try {
                        android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i);
                        com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i);
                        if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, user, "onPackagesUnsuspended")) {
                            java.lang.String[] filteredPackages = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.getFilteredPackageNames(packages, cookie, user);
                            if (!com.android.internal.util.ArrayUtils.isEmpty(filteredPackages)) {
                                try {
                                    listener.onPackagesUnsuspended(user, filteredPackages);
                                } catch (android.os.RemoteException re) {
                                    android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Callback failed ", re);
                                }
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                        throw th;
                    }
                }
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                super.onPackagesUnsuspended(packages);
            }

            public void onShortcutChanged(final java.lang.String packageName, final int userId) {
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.postToPackageMonitorHandler(new java.lang.Runnable() { // from class: com.android.server.pm.LauncherAppsService$LauncherAppsImpl$MyPackageMonitor$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() throws java.lang.Throwable {
                        this.f$0.lambda$onShortcutChanged$0(packageName, userId);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* JADX INFO: renamed from: onShortcutChangedInner, reason: merged with bridge method [inline-methods] */
            public void lambda$onShortcutChanged$0(java.lang.String packageName, int userId) throws java.lang.Throwable {
                java.lang.String str;
                int i;
                int n;
                java.lang.String str2;
                android.os.UserHandle user;
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.MyPackageMonitor myPackageMonitor = this;
                java.lang.String str3 = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG;
                int n2 = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                try {
                    android.os.UserHandle user2 = android.os.UserHandle.of(userId);
                    int i2 = 0;
                    while (i2 < n2) {
                        try {
                            android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i2);
                            com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i2);
                            if (!com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, user2, "onShortcutChanged")) {
                                i = i2;
                                user = user2;
                                n = n2;
                                str2 = str3;
                            } else if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isPackageVisibleToListener(packageName, cookie, user2)) {
                                int launcherUserId = cookie.user.getIdentifier();
                                if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mShortcutServiceInternal.hasShortcutHostPermission(launcherUserId, cookie.packageName, cookie.callingPid, cookie.callingUid)) {
                                    android.content.pm.ShortcutServiceInternal shortcutServiceInternal = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mShortcutServiceInternal;
                                    i = i2;
                                    android.os.UserHandle user3 = user2;
                                    n = n2;
                                    str2 = str3;
                                    try {
                                        java.util.List<android.content.pm.ShortcutInfo> list = shortcutServiceInternal.getShortcuts(launcherUserId, cookie.packageName, 0L, packageName, (java.util.List) null, (java.util.List) null, (android.content.ComponentName) null, 1055, userId, cookie.callingPid, cookie.callingUid);
                                        try {
                                            user = user3;
                                            try {
                                                listener.onShortcutChanged(user, packageName, new android.content.pm.ParceledListSlice(list));
                                            } catch (android.os.RemoteException e) {
                                                re = e;
                                                android.util.Slog.d(str2, "Callback failed ", re);
                                            }
                                        } catch (android.os.RemoteException e2) {
                                            re = e2;
                                            user = user3;
                                        }
                                    } catch (java.lang.RuntimeException e3) {
                                        e = e3;
                                        str = str2;
                                        myPackageMonitor = this;
                                        try {
                                            android.util.Log.w(str, e.getMessage(), e);
                                            com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                                            throw th;
                                        }
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                        myPackageMonitor = this;
                                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                                        throw th;
                                    }
                                } else {
                                    i = i2;
                                    user = user2;
                                    n = n2;
                                    str2 = str3;
                                }
                            } else {
                                i = i2;
                                user = user2;
                                n = n2;
                                str2 = str3;
                            }
                            i2 = i + 1;
                            str3 = str2;
                            user2 = user;
                            n2 = n;
                            myPackageMonitor = this;
                        } catch (java.lang.RuntimeException e4) {
                            e = e4;
                            str = str3;
                            myPackageMonitor = this;
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                            myPackageMonitor = this;
                        }
                    }
                    myPackageMonitor = this;
                } catch (java.lang.RuntimeException e5) {
                    e = e5;
                    str = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG;
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
            }

            public void onPackageStateChanged(java.lang.String packageName, int uid) {
                onPackageChanged(packageName);
                super.onPackageStateChanged(packageName, uid);
            }
        }

        private class MyOplusPackageMonitor extends com.android.server.pm.LauncherAppsService.LauncherAppsImpl.MyPackageMonitor implements android.content.pm.ShortcutServiceInternal.ShortcutChangeListener {
            private MyOplusPackageMonitor() {
                super();
            }

            @Override // com.android.server.pm.LauncherAppsService.LauncherAppsImpl.MyPackageMonitor
            public void onPackageAdded(java.lang.String packageName, int uid) {
            }

            public void onPackageAddedWithExtras(java.lang.String packageName, int uid, android.os.Bundle extras) {
                android.os.UserHandle user = new android.os.UserHandle(getChangingUserId());
                int n = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    try {
                        android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i);
                        com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i);
                        android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "onPackageAdded, packageName: " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + user + " uid: " + uid + " notify to: " + cookie.packageName);
                        if (!com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, user, "onPackageAdded")) {
                            android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "User " + user + "can not access profile, the package is: " + packageName);
                        } else if (!com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isPackageVisibleToListener(packageName, cookie, user)) {
                            android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Package " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + user + " is not visible to listener");
                        } else if (!com.android.server.pm.LauncherAppsService.mLauncherAppsServiceExt.isOhideAndLauncherCookie(extras, cookie.packageName)) {
                            try {
                                listener.onPackageAdded(user, packageName);
                            } catch (android.os.RemoteException re) {
                                android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Callback failed ", re);
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                        throw th;
                    }
                }
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mPackageManagerInternal.registerInstalledLoadingProgressCallback(packageName, com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.new PackageLoadingProgressCallback(packageName, user), user.getIdentifier());
            }
        }

        class PackageCallbackList<T extends android.os.IInterface> extends android.os.RemoteCallbackList<T> {
            PackageCallbackList() {
            }

            @Override // android.os.RemoteCallbackList
            public void onCallbackDied(T callback, java.lang.Object cookie) {
                com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.checkCallbackCount();
            }
        }

        class PackageLoadingProgressCallback extends android.content.pm.PackageManagerInternal.InstalledLoadingProgressCallback {
            private final java.lang.String mPackageName;
            private final android.os.UserHandle mUser;

            PackageLoadingProgressCallback(java.lang.String packageName, android.os.UserHandle user) {
                super(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mCallbackHandler);
                this.mPackageName = packageName;
                this.mUser = user;
            }

            @Override // android.content.pm.PackageManagerInternal.InstalledLoadingProgressCallback
            public void onLoadingProgressChanged(float progress) {
                int n = com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    try {
                        android.content.pm.IOnAppsChangedListener listener = (android.content.pm.IOnAppsChangedListener) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastItem(i);
                        com.android.server.pm.LauncherAppsService.BroadcastCookie cookie = (com.android.server.pm.LauncherAppsService.BroadcastCookie) com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.getBroadcastCookie(i);
                        if (com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isEnabledProfileOf(cookie, this.mUser, "onLoadingProgressChanged") && com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.isPackageVisibleToListener(this.mPackageName, cookie, this.mUser)) {
                            try {
                                listener.onPackageLoadingProgressChanged(this.mUser, this.mPackageName, progress);
                            } catch (android.os.RemoteException re) {
                                android.util.Slog.d(com.android.server.pm.LauncherAppsService.LauncherAppsImpl.TAG, "Callback failed ", re);
                            }
                        }
                    } finally {
                        com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.mListeners.finishBroadcast();
                    }
                }
            }
        }

        final class LocalService extends com.android.server.pm.LauncherAppsService.LauncherAppsServiceInternal {
            LocalService() {
            }

            @Override // com.android.server.pm.LauncherAppsService.LauncherAppsServiceInternal
            public boolean startShortcut(int callerUid, int callerPid, java.lang.String callingPackage, java.lang.String packageName, java.lang.String featureId, java.lang.String shortcutId, android.graphics.Rect sourceBounds, android.os.Bundle startActivityOptions, int targetUserId) {
                return com.android.server.pm.LauncherAppsService.LauncherAppsImpl.this.startShortcutInner(callerUid, callerPid, android.os.UserHandle.getUserId(callerUid), callingPackage, packageName, featureId, shortcutId, sourceBounds, startActivityOptions, targetUserId);
            }
        }
    }
}
