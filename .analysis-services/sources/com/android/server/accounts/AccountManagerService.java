package com.android.server.accounts;

/* JADX INFO: loaded from: classes.dex */
public class AccountManagerService extends android.accounts.IAccountManager.Stub implements android.content.pm.RegisteredServicesCacheListener<android.accounts.AuthenticatorDescription> {
    private static final android.accounts.Account[] EMPTY_ACCOUNT_ARRAY;
    private static final long ENFORCE_PACKAGE_VISIBILITY_FILTERING = 154726397;
    private static final int MESSAGE_COPY_SHARED_ACCOUNT = 4;
    private static final int MESSAGE_TIMED_OUT = 3;
    private static final java.lang.String PRE_N_DATABASE_NAME = "accounts.db";
    private static final int SIGNATURE_CHECK_MATCH = 1;
    private static final int SIGNATURE_CHECK_MISMATCH = 0;
    private static final int SIGNATURE_CHECK_UID_MATCH = 2;
    private static final java.lang.String TAG = "AccountManagerService";
    private static final int TIMEOUT_DELAY_MS = 900000;
    private static com.android.modules.expresslog.Histogram sResponseLatency;
    private static java.util.concurrent.atomic.AtomicReference<com.android.server.accounts.AccountManagerService> sThis;
    private final android.app.AppOpsManager mAppOpsManager;
    private final com.android.server.accounts.IAccountAuthenticatorCache mAuthenticatorCache;
    final android.content.Context mContext;
    final com.android.server.accounts.AccountManagerService.MessageHandler mHandler;
    private final com.android.server.accounts.AccountManagerService.Injector mInjector;
    private final android.content.pm.PackageManager mPackageManager;
    private android.os.UserManager mUserManager;
    private static final android.os.Bundle ACCOUNTS_CHANGED_OPTIONS = new android.app.BroadcastOptions().setDeliveryGroupPolicy(1).toBundle();
    private static final android.content.Intent ACCOUNTS_CHANGED_INTENT = new android.content.Intent("android.accounts.LOGIN_ACCOUNTS_CHANGED");
    public com.android.server.accounts.IAccountManagerServiceExt mAccountManagerServiceExt = (com.android.server.accounts.IAccountManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.accounts.IAccountManagerServiceExt.class).base(this).create();
    private final java.util.LinkedHashMap<java.lang.String, com.android.server.accounts.AccountManagerService.Session> mSessions = new java.util.LinkedHashMap<>();
    private final android.util.SparseArray<com.android.server.accounts.AccountManagerService.UserAccounts> mUsers = new android.util.SparseArray<>();
    private final android.util.SparseBooleanArray mLocalUnlockedUsers = new android.util.SparseBooleanArray();
    private final java.text.SimpleDateFormat mDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private java.util.concurrent.CopyOnWriteArrayList<android.accounts.AccountManagerInternal.OnAppPermissionChangeListener> mAppPermissionChangeListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.accounts.AccountManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mService = new com.android.server.accounts.AccountManagerService(new com.android.server.accounts.AccountManagerService.Injector(getContext()));
            publishBinderService("account", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            this.mService.onUnlockUser(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopped(com.android.server.SystemService.TargetUser user) {
            android.util.Slog.i(com.android.server.accounts.AccountManagerService.TAG, "onUserStopped " + user);
            this.mService.purgeUserData(user.getUserIdentifier());
        }
    }

    static {
        ACCOUNTS_CHANGED_INTENT.setFlags(android.hardware.audio.common.V2_0.AudioFormat.HE_AAC_V1);
        sThis = new java.util.concurrent.atomic.AtomicReference<>();
        EMPTY_ACCOUNT_ARRAY = new android.accounts.Account[0];
        sResponseLatency = new com.android.modules.expresslog.Histogram("app.value_high_authenticator_response_latency", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(20, 10000, 10000.0f, 1.5f));
    }

    static class UserAccounts {
        final com.android.server.accounts.AccountsDb accountsDb;
        private final int userId;
        private final java.util.HashMap<android.util.Pair<android.util.Pair<android.accounts.Account, java.lang.String>, java.lang.Integer>, com.android.server.accounts.AccountManagerService.NotificationId> credentialsPermissionNotificationIds = new java.util.HashMap<>();
        private final java.util.HashMap<android.accounts.Account, com.android.server.accounts.AccountManagerService.NotificationId> signinRequiredNotificationIds = new java.util.HashMap<>();
        final java.lang.Object cacheLock = new java.lang.Object();
        final java.lang.Object dbLock = new java.lang.Object();
        final java.util.HashMap<java.lang.String, android.accounts.Account[]> accountCache = new java.util.LinkedHashMap();
        private final java.util.Map<android.accounts.Account, java.util.Map<java.lang.String, java.lang.String>> userDataCache = new java.util.HashMap();
        private final java.util.Map<android.accounts.Account, java.util.Map<java.lang.String, java.lang.String>> authTokenCache = new java.util.HashMap();
        private final com.android.server.accounts.TokenCache accountTokenCaches = new com.android.server.accounts.TokenCache();
        private final java.util.Map<android.accounts.Account, java.util.Map<java.lang.String, java.lang.Integer>> visibilityCache = new java.util.HashMap();
        private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> mReceiversForType = new java.util.HashMap();
        private final java.util.HashMap<android.accounts.Account, java.util.concurrent.atomic.AtomicReference<java.lang.String>> previousNameCache = new java.util.HashMap<>();

        UserAccounts(android.content.Context context, int userId, java.io.File preNDbFile, java.io.File deDbFile) {
            this.userId = userId;
            synchronized (this.dbLock) {
                synchronized (this.cacheLock) {
                    this.accountsDb = com.android.server.accounts.AccountsDb.create(context, userId, preNDbFile, deDbFile);
                }
            }
        }
    }

    public static com.android.server.accounts.AccountManagerService getSingleton() {
        return sThis.get();
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [com.android.server.accounts.AccountManagerService$3] */
    public AccountManagerService(com.android.server.accounts.AccountManagerService.Injector injector) {
        this.mInjector = injector;
        this.mContext = injector.getContext();
        this.mPackageManager = this.mContext.getPackageManager();
        this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mHandler = new com.android.server.accounts.AccountManagerService.MessageHandler(injector.getMessageHandlerLooper());
        this.mAuthenticatorCache = this.mInjector.getAccountAuthenticatorCache();
        this.mAuthenticatorCache.setListener(this, this.mHandler);
        sThis.set(this);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.accounts.AccountManagerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context1, android.content.Intent intent) {
                if (!intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                    final java.lang.String removedPackageName = intent.getData().getSchemeSpecificPart();
                    java.lang.Runnable purgingRunnable = new java.lang.Runnable() { // from class: com.android.server.accounts.AccountManagerService.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            com.android.server.accounts.AccountManagerService.this.purgeOldGrantsAll();
                            com.android.server.accounts.AccountManagerService.this.removeVisibilityValuesForPackage(removedPackageName);
                        }
                    };
                    com.android.server.accounts.AccountManagerService.this.mHandler.post(purgingRunnable);
                }
            }
        }, intentFilter);
        injector.addLocalService(new com.android.server.accounts.AccountManagerService.AccountManagerInternalImpl());
        android.content.IntentFilter userFilter = new android.content.IntentFilter();
        userFilter.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.accounts.AccountManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                int userId;
                java.lang.String action = intent.getAction();
                if (!"android.intent.action.USER_REMOVED".equals(action) || (userId = intent.getIntExtra("android.intent.extra.user_handle", -1)) < 1) {
                    return;
                }
                android.util.Slog.i(com.android.server.accounts.AccountManagerService.TAG, "User " + userId + " removed");
                com.android.server.accounts.AccountManagerService.this.purgeUserData(userId);
            }
        }, android.os.UserHandle.ALL, userFilter, null, null);
        new com.android.internal.content.PackageMonitor() { // from class: com.android.server.accounts.AccountManagerService.3
            public void onPackageAdded(java.lang.String packageName, int uid) {
                try {
                    com.android.server.accounts.AccountManagerService.UserAccounts accounts = com.android.server.accounts.AccountManagerService.this.getUserAccounts(android.os.UserHandle.getUserId(uid));
                    com.android.server.accounts.AccountManagerService.this.cancelAccountAccessRequestNotificationIfNeeded(uid, true, accounts);
                } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e) {
                    android.util.Log.w(com.android.server.accounts.AccountManagerService.TAG, "Can't read accounts database", e);
                }
            }

            public void onPackageUpdateFinished(java.lang.String packageName, int uid) {
                try {
                    com.android.server.accounts.AccountManagerService.UserAccounts accounts = com.android.server.accounts.AccountManagerService.this.getUserAccounts(android.os.UserHandle.getUserId(uid));
                    com.android.server.accounts.AccountManagerService.this.cancelAccountAccessRequestNotificationIfNeeded(uid, true, accounts);
                } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e) {
                    android.util.Log.w(com.android.server.accounts.AccountManagerService.TAG, "Can't read accounts database", e);
                }
            }
        }.register(this.mContext, this.mHandler.getLooper(), android.os.UserHandle.ALL, true);
        this.mAppOpsManager.startWatchingMode(62, (java.lang.String) null, (android.app.AppOpsManager.OnOpChangedListener) new android.app.AppOpsManager.OnOpChangedInternalListener() { // from class: com.android.server.accounts.AccountManagerService.4
            public void onOpChanged(int op, java.lang.String packageName) {
                try {
                    int userId = android.app.ActivityManager.getCurrentUser();
                    int uid = com.android.server.accounts.AccountManagerService.this.mPackageManager.getPackageUidAsUser(packageName, userId);
                    int mode = com.android.server.accounts.AccountManagerService.this.mAppOpsManager.checkOpNoThrow(62, uid, packageName);
                    if (mode == 0) {
                        long identity = android.os.Binder.clearCallingIdentity();
                        try {
                            com.android.server.accounts.AccountManagerService.UserAccounts accounts = com.android.server.accounts.AccountManagerService.this.getUserAccounts(userId);
                            com.android.server.accounts.AccountManagerService.this.cancelAccountAccessRequestNotificationIfNeeded(packageName, uid, true, accounts);
                            android.os.Binder.restoreCallingIdentity(identity);
                        } catch (java.lang.Throwable th) {
                            android.os.Binder.restoreCallingIdentity(identity);
                            throw th;
                        }
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e2) {
                    android.util.Log.w(com.android.server.accounts.AccountManagerService.TAG, "Can't read accounts database", e2);
                }
            }
        });
        this.mPackageManager.addOnPermissionsChangeListener(new android.content.pm.PackageManager.OnPermissionsChangedListener() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda5
            public final void onPermissionsChanged(int i) throws java.lang.Throwable {
                this.f$0.lambda$new$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int uid) throws java.lang.Throwable {
        android.accounts.Account[] accounts;
        android.accounts.AccountManager.invalidateLocalAccountsDataCaches();
        android.accounts.Account[] accounts2 = null;
        java.lang.String[] packageNames = this.mPackageManager.getPackagesForUid(uid);
        if (packageNames != null) {
            int userId = android.os.UserHandle.getUserId(uid);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    for (java.lang.String packageName : packageNames) {
                        if (this.mPackageManager.checkPermission("android.permission.GET_ACCOUNTS", packageName) == 0) {
                            if (accounts2 == null) {
                                android.accounts.Account[] accounts3 = getAccountsOrEmptyArray(null, userId, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                                if (com.android.internal.util.ArrayUtils.isEmpty(accounts3)) {
                                    android.os.Binder.restoreCallingIdentity(identity);
                                    return;
                                }
                                accounts = accounts3;
                            } else {
                                accounts = accounts2;
                            }
                            try {
                                com.android.server.accounts.AccountManagerService.UserAccounts userAccounts = getUserAccounts(android.os.UserHandle.getUserId(uid));
                                int length = accounts.length;
                                int i = 0;
                                while (i < length) {
                                    android.accounts.Account account = accounts[i];
                                    int i2 = i;
                                    int i3 = length;
                                    android.accounts.Account[] accounts4 = accounts;
                                    try {
                                        cancelAccountAccessRequestNotificationIfNeeded(account, uid, packageName, true, userAccounts);
                                        i = i2 + 1;
                                        length = i3;
                                        accounts = accounts4;
                                    } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e) {
                                        e = e;
                                        android.util.Slog.e(TAG, "trying to query account db that already been deleted: ", e);
                                        android.os.Binder.restoreCallingIdentity(identity);
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        android.os.Binder.restoreCallingIdentity(identity);
                                        throw th;
                                    }
                                }
                                accounts2 = accounts;
                            } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e2) {
                                e = e2;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e3) {
                e = e3;
            }
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    boolean getBindInstantServiceAllowed(int userId) {
        return this.mAuthenticatorCache.getBindInstantServiceAllowed(userId);
    }

    void setBindInstantServiceAllowed(int userId, boolean allowed) {
        this.mAuthenticatorCache.setBindInstantServiceAllowed(userId, allowed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAccountAccessRequestNotificationIfNeeded(int uid, boolean checkAccess, com.android.server.accounts.AccountManagerService.UserAccounts userAccounts) {
        android.accounts.Account[] accounts = getAccountsOrEmptyArray(null, android.os.UserHandle.getUserId(uid), com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
        for (android.accounts.Account account : accounts) {
            cancelAccountAccessRequestNotificationIfNeeded(account, uid, checkAccess, userAccounts);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAccountAccessRequestNotificationIfNeeded(java.lang.String packageName, int uid, boolean checkAccess, com.android.server.accounts.AccountManagerService.UserAccounts userAccounts) {
        android.accounts.Account[] accounts = getAccountsOrEmptyArray(null, android.os.UserHandle.getUserId(uid), com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
        for (android.accounts.Account account : accounts) {
            cancelAccountAccessRequestNotificationIfNeeded(account, uid, packageName, checkAccess, userAccounts);
        }
    }

    private void cancelAccountAccessRequestNotificationIfNeeded(android.accounts.Account account, int uid, boolean checkAccess, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        java.lang.String[] packageNames = this.mPackageManager.getPackagesForUid(uid);
        if (packageNames != null) {
            for (java.lang.String packageName : packageNames) {
                cancelAccountAccessRequestNotificationIfNeeded(account, uid, packageName, checkAccess, accounts);
            }
        }
    }

    private void cancelAccountAccessRequestNotificationIfNeeded(android.accounts.Account account, int uid, java.lang.String packageName, boolean checkAccess, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        if (!checkAccess || hasAccountAccess(account, packageName, android.os.UserHandle.getUserHandleForUid(uid))) {
            cancelNotification(getCredentialPermissionNotificationId(account, "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", uid, accounts), accounts);
        }
    }

    public boolean addAccountExplicitlyWithVisibility(android.accounts.Account account, java.lang.String password, android.os.Bundle extras, java.util.Map packageToVisibility, java.lang.String opPackageName) {
        android.os.Bundle.setDefusable(extras, true);
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        java.util.Objects.requireNonNull(account, "account cannot be null");
        android.util.Log.v(TAG, "addAccountExplicitly: caller's uid=" + callingUid + ", pid=" + android.os.Binder.getCallingPid() + ", packageName=" + opPackageName + ", accountType=" + account.type);
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg = java.lang.String.format("uid=%s, package=%s cannot explicitly add accounts of type: %s", java.lang.Integer.valueOf(callingUid), opPackageName, account.type);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            return addAccountInternal(accounts, account, password, extras, callingUid, packageToVisibility, opPackageName);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public java.util.Map<android.accounts.Account, java.lang.Integer> getAccountsAndVisibilityForPackage(java.lang.String packageName, java.lang.String accountType) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        boolean isSystemUid = android.os.UserHandle.isSameApp(callingUid, 1000);
        java.util.List<java.lang.String> managedTypes = getTypesForCaller(callingUid, userId, isSystemUid);
        if ((accountType != null && !managedTypes.contains(accountType)) || (accountType == null && !isSystemUid)) {
            throw new java.lang.SecurityException("getAccountsAndVisibilityForPackage() called from unauthorized uid " + callingUid + " with packageName=" + packageName);
        }
        if (accountType != null) {
            managedTypes = new java.util.ArrayList();
            managedTypes.add(accountType);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            return getAccountsAndVisibilityForPackage(packageName, managedTypes, java.lang.Integer.valueOf(callingUid), accounts);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private java.util.Map<android.accounts.Account, java.lang.Integer> getAccountsAndVisibilityForPackage(java.lang.String packageName, java.util.List<java.lang.String> accountTypes, java.lang.Integer callingUid, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        if (!canCallerAccessPackage(packageName, callingUid.intValue(), accounts.userId)) {
            android.util.Log.w(TAG, "getAccountsAndVisibilityForPackage#Package not found " + packageName);
            return new java.util.LinkedHashMap();
        }
        java.util.Map<android.accounts.Account, java.lang.Integer> result = new java.util.LinkedHashMap<>();
        for (java.lang.String accountType : accountTypes) {
            synchronized (accounts.dbLock) {
                synchronized (accounts.cacheLock) {
                    android.accounts.Account[] accountsOfType = accounts.accountCache.get(accountType);
                    if (accountsOfType != null) {
                        for (android.accounts.Account account : accountsOfType) {
                            result.put(account, resolveAccountVisibility(account, packageName, accounts));
                        }
                    }
                }
            }
        }
        return filterSharedAccounts(accounts, result, callingUid.intValue(), packageName);
    }

    public java.util.Map<java.lang.String, java.lang.Integer> getPackagesAndVisibilityForAccount(android.accounts.Account account) {
        java.util.Map<java.lang.String, java.lang.Integer> packagesAndVisibilityForAccountLocked;
        java.util.Objects.requireNonNull(account, "account cannot be null");
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId) && !isSystemUid(callingUid)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot get secrets for account %s", java.lang.Integer.valueOf(callingUid), account);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            synchronized (accounts.dbLock) {
                synchronized (accounts.cacheLock) {
                    packagesAndVisibilityForAccountLocked = getPackagesAndVisibilityForAccountLocked(account, accounts);
                }
            }
            return packagesAndVisibilityForAccountLocked;
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private java.util.Map<java.lang.String, java.lang.Integer> getPackagesAndVisibilityForAccountLocked(android.accounts.Account account, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        java.util.Map<java.lang.String, java.lang.Integer> accountVisibility = (java.util.Map) accounts.visibilityCache.get(account);
        if (accountVisibility == null) {
            android.util.Log.d(TAG, "Visibility was not initialized");
            java.util.HashMap map = new java.util.HashMap();
            accounts.visibilityCache.put(account, map);
            android.accounts.AccountManager.invalidateLocalAccountsDataCaches();
            return map;
        }
        return accountVisibility;
    }

    public int getAccountVisibility(android.accounts.Account account, java.lang.String packageName) {
        java.util.Objects.requireNonNull(account, "account cannot be null");
        java.util.Objects.requireNonNull(packageName, "packageName cannot be null");
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId) && !isSystemUid(callingUid)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot get secrets for accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            if ("android:accounts:key_legacy_visible".equals(packageName)) {
                int visibility = getAccountVisibilityFromCache(account, packageName, accounts);
                if (visibility != 0) {
                    return visibility;
                }
                restoreCallingIdentity(identityToken);
                return 2;
            }
            if (!"android:accounts:key_legacy_not_visible".equals(packageName)) {
                if (canCallerAccessPackage(packageName, callingUid, accounts.userId)) {
                    return resolveAccountVisibility(account, packageName, accounts).intValue();
                }
                restoreCallingIdentity(identityToken);
                return 3;
            }
            int visibility2 = getAccountVisibilityFromCache(account, packageName, accounts);
            if (visibility2 != 0) {
                return visibility2;
            }
            restoreCallingIdentity(identityToken);
            return 4;
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private int getAccountVisibilityFromCache(android.accounts.Account account, java.lang.String packageName, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        int iIntValue;
        synchronized (accounts.cacheLock) {
            java.util.Map<java.lang.String, java.lang.Integer> accountVisibility = getPackagesAndVisibilityForAccountLocked(account, accounts);
            java.lang.Integer visibility = accountVisibility.get(packageName);
            iIntValue = visibility != null ? visibility.intValue() : 0;
        }
        return iIntValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.Integer resolveAccountVisibility(android.accounts.Account account, java.lang.String packageName, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        int signatureCheckResult;
        int visibility;
        java.util.Objects.requireNonNull(packageName, "packageName cannot be null");
        try {
            long identityToken = clearCallingIdentity();
            try {
                int uid = this.mPackageManager.getPackageUidAsUser(packageName, accounts.userId);
                if (!android.os.UserHandle.isSameApp(uid, 1000) && (signatureCheckResult = checkPackageSignature(account.type, uid, accounts.userId)) != 2) {
                    int visibility2 = getAccountVisibilityFromCache(account, packageName, accounts);
                    if (visibility2 != 0) {
                        return java.lang.Integer.valueOf(visibility2);
                    }
                    boolean isPrivileged = isPermittedForPackage(packageName, accounts.userId, "android.permission.GET_ACCOUNTS_PRIVILEGED");
                    if (isProfileOwner(uid)) {
                        return 1;
                    }
                    boolean preO = isPreOApplication(packageName);
                    if (signatureCheckResult != 0 || ((preO && checkGetAccountsPermission(packageName, accounts.userId)) || ((checkReadContactsPermission(packageName, accounts.userId) && accountTypeManagesContacts(account.type, accounts.userId)) || isPrivileged))) {
                        visibility = getAccountVisibilityFromCache(account, "android:accounts:key_legacy_visible", accounts);
                        if (visibility == 0) {
                            visibility = 2;
                        }
                    } else {
                        visibility = getAccountVisibilityFromCache(account, "android:accounts:key_legacy_not_visible", accounts);
                        if (visibility == 0) {
                            visibility = 4;
                        }
                    }
                    return java.lang.Integer.valueOf(visibility);
                }
                return 1;
            } finally {
                restoreCallingIdentity(identityToken);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.w(TAG, "resolveAccountVisibility#Package not found " + e.getMessage());
            return 3;
        }
    }

    private boolean isPreOApplication(java.lang.String packageName) {
        try {
            long identityToken = clearCallingIdentity();
            try {
                android.content.pm.ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(packageName, 0);
                if (applicationInfo == null) {
                    return true;
                }
                int version = applicationInfo.targetSdkVersion;
                return version < 26;
            } finally {
                restoreCallingIdentity(identityToken);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.w(TAG, "isPreOApplication#Package not found " + e.getMessage());
            return true;
        }
    }

    public boolean setAccountVisibility(android.accounts.Account account, java.lang.String packageName, int newVisibility) {
        java.util.Objects.requireNonNull(account, "account cannot be null");
        java.util.Objects.requireNonNull(packageName, "packageName cannot be null");
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId) && !isSystemUid(callingUid)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot get secrets for accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            return setAccountVisibility(account, packageName, newVisibility, true, accounts, callingUid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private boolean isVisible(int visibility) {
        return visibility == 1 || visibility == 2;
    }

    private boolean setAccountVisibility(android.accounts.Account account, java.lang.String packageName, int newVisibility, boolean notify, com.android.server.accounts.AccountManagerService.UserAccounts accounts, int callingUid) throws java.lang.Throwable {
        java.util.Map<java.lang.String, java.lang.Integer> packagesToVisibility;
        java.util.List<java.lang.String> accountRemovedReceivers;
        boolean notify2;
        java.lang.Integer oldVisibility;
        synchronized (accounts.dbLock) {
            try {
                try {
                    synchronized (accounts.cacheLock) {
                        try {
                            try {
                                if (notify) {
                                    if (!isSpecialPackageKey(packageName)) {
                                        if (!canCallerAccessPackage(packageName, callingUid, accounts.userId)) {
                                            return false;
                                        }
                                        packagesToVisibility = new java.util.HashMap<>();
                                        packagesToVisibility.put(packageName, resolveAccountVisibility(account, packageName, accounts));
                                        accountRemovedReceivers = new java.util.ArrayList<>();
                                        if (shouldNotifyPackageOnAccountRemoval(account, packageName, accounts)) {
                                            accountRemovedReceivers.add(packageName);
                                        }
                                    } else {
                                        packagesToVisibility = getRequestingPackages(account, accounts);
                                        accountRemovedReceivers = getAccountRemovedReceivers(account, accounts);
                                    }
                                } else {
                                    if (!isSpecialPackageKey(packageName) && !canCallerAccessPackage(packageName, callingUid, accounts.userId)) {
                                        return false;
                                    }
                                    packagesToVisibility = java.util.Collections.emptyMap();
                                    accountRemovedReceivers = java.util.Collections.emptyList();
                                }
                                if (notify && (oldVisibility = accounts.accountsDb.findAccountVisibility(account, packageName)) != null && oldVisibility.intValue() == newVisibility) {
                                    notify2 = false;
                                } else {
                                    notify2 = notify;
                                }
                                if (!updateAccountVisibilityLocked(account, packageName, newVisibility, accounts)) {
                                    return false;
                                }
                                if (notify2) {
                                    android.util.Log.i(TAG, "Notifying visibility changed for package=" + packageName);
                                    java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.Integer>> it = packagesToVisibility.entrySet().iterator();
                                    while (it.hasNext()) {
                                        java.util.Map.Entry<java.lang.String, java.lang.Integer> packageToVisibility = it.next();
                                        int oldVisibility2 = packageToVisibility.getValue().intValue();
                                        int currentVisibility = resolveAccountVisibility(account, packageName, accounts).intValue();
                                        java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.Integer>> it2 = it;
                                        if (isVisible(oldVisibility2) != isVisible(currentVisibility)) {
                                            notifyPackage(packageToVisibility.getKey(), accounts);
                                        }
                                        it = it2;
                                    }
                                    for (java.lang.String packageNameToNotify : accountRemovedReceivers) {
                                        int currentVisibility2 = resolveAccountVisibility(account, packageNameToNotify, accounts).intValue();
                                        if (!isVisible(currentVisibility2)) {
                                            sendAccountRemovedBroadcast(account, packageNameToNotify, accounts.userId, "setAccountVisibility");
                                        }
                                    }
                                    sendAccountsChangedBroadcast(accounts.userId, account.type, "setAccountVisibility");
                                }
                                return true;
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            throw th;
                        }
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    private boolean updateAccountVisibilityLocked(android.accounts.Account account, java.lang.String packageName, int newVisibility, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        long accountId = accounts.accountsDb.findDeAccountId(account);
        if (accountId < 0) {
            return false;
        }
        android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
        try {
            if (!accounts.accountsDb.setAccountVisibility(accountId, packageName, newVisibility)) {
                return false;
            }
            android.os.StrictMode.setThreadPolicy(oldPolicy);
            java.util.Map<java.lang.String, java.lang.Integer> accountVisibility = getPackagesAndVisibilityForAccountLocked(account, accounts);
            accountVisibility.put(packageName, java.lang.Integer.valueOf(newVisibility));
            android.accounts.AccountManager.invalidateLocalAccountsDataCaches();
            return true;
        } finally {
            android.os.StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public void registerAccountListener(java.lang.String[] accountTypes, java.lang.String opPackageName) {
        int callingUid = android.os.Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, opPackageName);
        int userId = android.os.UserHandle.getCallingUserId();
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            registerAccountListener(accountTypes, opPackageName, accounts);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0011 A[Catch: all -> 0x0047, TryCatch #0 {, blocks: (B:6:0x0008, B:7:0x000d, B:9:0x0011, B:11:0x001f, B:12:0x002c, B:14:0x0035, B:15:0x003a, B:16:0x0045), top: B:21:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void registerAccountListener(java.lang.String[] r9, java.lang.String r10, com.android.server.accounts.AccountManagerService.UserAccounts r11) {
        /*
            r8 = this;
            java.util.Map r0 = com.android.server.accounts.AccountManagerService.UserAccounts.m927$$Nest$fgetmReceiversForType(r11)
            monitor-enter(r0)
            if (r9 != 0) goto Ld
            r1 = 0
            java.lang.String[] r1 = new java.lang.String[]{r1}     // Catch: java.lang.Throwable -> L47
            r9 = r1
        Ld:
            int r1 = r9.length     // Catch: java.lang.Throwable -> L47
            r2 = 0
        Lf:
            if (r2 >= r1) goto L45
            r3 = r9[r2]     // Catch: java.lang.Throwable -> L47
            java.util.Map r4 = com.android.server.accounts.AccountManagerService.UserAccounts.m927$$Nest$fgetmReceiversForType(r11)     // Catch: java.lang.Throwable -> L47
            java.lang.Object r4 = r4.get(r3)     // Catch: java.lang.Throwable -> L47
            java.util.Map r4 = (java.util.Map) r4     // Catch: java.lang.Throwable -> L47
            if (r4 != 0) goto L2c
            java.util.HashMap r5 = new java.util.HashMap     // Catch: java.lang.Throwable -> L47
            r5.<init>()     // Catch: java.lang.Throwable -> L47
            r4 = r5
            java.util.Map r5 = com.android.server.accounts.AccountManagerService.UserAccounts.m927$$Nest$fgetmReceiversForType(r11)     // Catch: java.lang.Throwable -> L47
            r5.put(r3, r4)     // Catch: java.lang.Throwable -> L47
        L2c:
            java.lang.Object r5 = r4.get(r10)     // Catch: java.lang.Throwable -> L47
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch: java.lang.Throwable -> L47
            r6 = 1
            if (r5 == 0) goto L3a
            int r7 = r5.intValue()     // Catch: java.lang.Throwable -> L47
            int r6 = r6 + r7
        L3a:
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.Throwable -> L47
            r4.put(r10, r6)     // Catch: java.lang.Throwable -> L47
            int r2 = r2 + 1
            goto Lf
        L45:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L47
            return
        L47:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L47
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accounts.AccountManagerService.registerAccountListener(java.lang.String[], java.lang.String, com.android.server.accounts.AccountManagerService$UserAccounts):void");
    }

    public void unregisterAccountListener(java.lang.String[] accountTypes, java.lang.String opPackageName) {
        int callingUid = android.os.Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, opPackageName);
        int userId = android.os.UserHandle.getCallingUserId();
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            unregisterAccountListener(accountTypes, opPackageName, accounts);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private void unregisterAccountListener(java.lang.String[] accountTypes, java.lang.String opPackageName, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        synchronized (accounts.mReceiversForType) {
            if (accountTypes == null) {
                accountTypes = new java.lang.String[]{null};
            }
            for (java.lang.String type : accountTypes) {
                java.util.Map<java.lang.String, java.lang.Integer> receivers = (java.util.Map) accounts.mReceiversForType.get(type);
                if (receivers == null || receivers.get(opPackageName) == null) {
                    throw new java.lang.IllegalArgumentException("attempt to unregister wrong receiver");
                }
                java.lang.Integer cnt = receivers.get(opPackageName);
                if (cnt.intValue() == 1) {
                    receivers.remove(opPackageName);
                } else {
                    receivers.put(opPackageName, java.lang.Integer.valueOf(cnt.intValue() - 1));
                }
            }
        }
    }

    private void sendNotificationAccountUpdated(android.accounts.Account account, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        java.util.Map<java.lang.String, java.lang.Integer> packagesToVisibility = getRequestingPackages(account, accounts);
        for (java.util.Map.Entry<java.lang.String, java.lang.Integer> packageToVisibility : packagesToVisibility.entrySet()) {
            if (packageToVisibility.getValue().intValue() != 3 && packageToVisibility.getValue().intValue() != 4) {
                notifyPackage(packageToVisibility.getKey(), accounts);
            }
        }
    }

    private void notifyPackage(java.lang.String packageName, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        android.util.Log.i(TAG, "notifying package=" + packageName + " for userId=" + accounts.userId + ", sending broadcast of android.accounts.action.VISIBLE_ACCOUNTS_CHANGED");
        android.content.Intent intent = new android.content.Intent("android.accounts.action.VISIBLE_ACCOUNTS_CHANGED");
        intent.setPackage(packageName);
        intent.setFlags(1073741824);
        this.mContext.sendBroadcastAsUser(intent, new android.os.UserHandle(accounts.userId));
    }

    private java.util.Map<java.lang.String, java.lang.Integer> getRequestingPackages(android.accounts.Account account, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        java.util.Set<java.lang.String> packages = new java.util.HashSet<>();
        synchronized (accounts.mReceiversForType) {
            java.lang.String[] strArr = {account.type, null};
            for (int i = 0; i < 2; i++) {
                java.lang.String type = strArr[i];
                java.util.Map<java.lang.String, java.lang.Integer> receivers = (java.util.Map) accounts.mReceiversForType.get(type);
                if (receivers != null) {
                    packages.addAll(receivers.keySet());
                }
            }
        }
        java.util.Map<java.lang.String, java.lang.Integer> result = new java.util.HashMap<>();
        for (java.lang.String packageName : packages) {
            result.put(packageName, resolveAccountVisibility(account, packageName, accounts));
        }
        return result;
    }

    private java.util.List<java.lang.String> getAccountRemovedReceivers(android.accounts.Account account, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        android.content.Intent intent = new android.content.Intent("android.accounts.action.ACCOUNT_REMOVED");
        intent.setFlags(16777216);
        java.util.List<android.content.pm.ResolveInfo> receivers = this.mPackageManager.queryBroadcastReceiversAsUser(intent, 0, accounts.userId);
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        if (receivers == null) {
            return result;
        }
        for (android.content.pm.ResolveInfo resolveInfo : receivers) {
            java.lang.String packageName = resolveInfo.activityInfo.applicationInfo.packageName;
            int visibility = resolveAccountVisibility(account, packageName, accounts).intValue();
            if (visibility == 1 || visibility == 2) {
                result.add(packageName);
            }
        }
        return result;
    }

    private boolean shouldNotifyPackageOnAccountRemoval(android.accounts.Account account, java.lang.String packageName, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        int visibility = resolveAccountVisibility(account, packageName, accounts).intValue();
        if (visibility != 1 && visibility != 2) {
            return false;
        }
        android.content.Intent intent = new android.content.Intent("android.accounts.action.ACCOUNT_REMOVED");
        intent.setFlags(16777216);
        intent.setPackage(packageName);
        java.util.List<android.content.pm.ResolveInfo> receivers = this.mPackageManager.queryBroadcastReceiversAsUser(intent, 0, accounts.userId);
        return receivers != null && receivers.size() > 0;
    }

    private boolean isSpecialPackageKey(java.lang.String packageName) {
        return "android:accounts:key_legacy_visible".equals(packageName) || "android:accounts:key_legacy_not_visible".equals(packageName);
    }

    private void sendAccountsChangedBroadcast(int userId, java.lang.String accountType, java.lang.String useCase) {
        java.util.Objects.requireNonNull(useCase, "useCase can't be null");
        android.util.Log.i(TAG, "the accountType= " + (accountType == null ? "" : accountType) + " changed with useCase=" + useCase + " for userId=" + userId + ", sending broadcast of " + ACCOUNTS_CHANGED_INTENT.getAction());
        this.mContext.sendBroadcastAsUser(ACCOUNTS_CHANGED_INTENT, new android.os.UserHandle(userId), null, ACCOUNTS_CHANGED_OPTIONS);
    }

    private void sendAccountRemovedBroadcast(android.accounts.Account account, java.lang.String packageName, int userId, java.lang.String useCase) {
        java.util.Objects.requireNonNull(useCase, "useCase can't be null");
        android.util.Log.i(TAG, "the account with type=" + account.type + " removed while useCase=" + useCase + " for userId=" + userId + ", sending broadcast of android.accounts.action.ACCOUNT_REMOVED");
        android.content.Intent intent = new android.content.Intent("android.accounts.action.ACCOUNT_REMOVED");
        intent.setFlags(16777216);
        intent.setPackage(packageName);
        intent.putExtra("authAccount", account.name);
        intent.putExtra("accountType", account.type);
        this.mContext.sendBroadcastAsUser(intent, new android.os.UserHandle(userId));
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (java.lang.RuntimeException e) {
            if (!(e instanceof java.lang.SecurityException) && !(e instanceof java.lang.IllegalArgumentException)) {
                android.util.Slog.wtf(TAG, "Account Manager Crash", e);
            }
            throw e;
        }
    }

    private android.os.UserManager getUserManager() {
        if (this.mUserManager == null) {
            this.mUserManager = android.os.UserManager.get(this.mContext);
        }
        return this.mUserManager;
    }

    public void validateAccounts(int userId) {
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
        validateAccountsInternal(accounts, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:110:0x036e A[Catch: all -> 0x0383, TRY_ENTER, TryCatch #0 {all -> 0x0383, blocks: (B:110:0x036e, B:112:0x037b, B:99:0x034e, B:100:0x035a, B:115:0x0381), top: B:125:0x0072 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void validateAccountsInternal(com.android.server.accounts.AccountManagerService.UserAccounts r28, boolean r29) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 910
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accounts.AccountManagerService.validateAccountsInternal(com.android.server.accounts.AccountManagerService$UserAccounts, boolean):void");
    }

    private android.util.SparseBooleanArray getUidsOfInstalledOrUpdatedPackagesAsUser(int userId) {
        java.util.List<android.content.pm.PackageInfo> pkgsWithData = this.mPackageManager.getInstalledPackagesAsUser(8192, userId);
        android.util.SparseBooleanArray knownUids = new android.util.SparseBooleanArray(pkgsWithData.size());
        for (android.content.pm.PackageInfo pkgInfo : pkgsWithData) {
            if (pkgInfo.applicationInfo != null && (pkgInfo.applicationInfo.flags & 8388608) != 0) {
                knownUids.put(pkgInfo.applicationInfo.uid, true);
            }
        }
        return knownUids;
    }

    static java.util.HashMap<java.lang.String, java.lang.Integer> getAuthenticatorTypeAndUIDForUser(android.content.Context context, int userId) {
        com.android.server.accounts.AccountAuthenticatorCache authCache = new com.android.server.accounts.AccountAuthenticatorCache(context);
        return getAuthenticatorTypeAndUIDForUser(authCache, userId);
    }

    private static java.util.HashMap<java.lang.String, java.lang.Integer> getAuthenticatorTypeAndUIDForUser(com.android.server.accounts.IAccountAuthenticatorCache authCache, int userId) {
        java.util.HashMap<java.lang.String, java.lang.Integer> knownAuth = new java.util.LinkedHashMap<>();
        for (android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription> service : authCache.getAllServices(userId)) {
            knownAuth.put(((android.accounts.AuthenticatorDescription) service.type).type, java.lang.Integer.valueOf(service.uid));
        }
        return knownAuth;
    }

    private com.android.server.accounts.AccountManagerService.UserAccounts getUserAccountsForCaller() {
        return getUserAccounts(android.os.UserHandle.getCallingUserId());
    }

    protected com.android.server.accounts.AccountManagerService.UserAccounts getUserAccounts(int userId) {
        try {
            return getUserAccountsNotChecked(userId);
        } catch (java.lang.RuntimeException e) {
            if (!this.mPackageManager.hasSystemFeature("android.hardware.type.automotive")) {
                throw e;
            }
            android.util.Slog.wtf(TAG, "Removing user " + userId + " due to exception (" + e + ") reading its account database");
            if (userId == android.app.ActivityManager.getCurrentUser() && userId != 0) {
                android.util.Slog.i(TAG, "Switching to system user first");
                try {
                    android.app.ActivityManager.getService().switchUser(0);
                } catch (android.os.RemoteException re) {
                    android.util.Slog.e(TAG, "Could not switch to 0: " + re);
                }
            }
            if (!getUserManager().removeUserEvenWhenDisallowed(userId)) {
                android.util.Slog.e(TAG, "could not remove user " + userId);
                throw e;
            }
            throw e;
        }
    }

    private com.android.server.accounts.AccountManagerService.UserAccounts getUserAccountsNotChecked(int userId) {
        com.android.server.accounts.AccountManagerService.UserAccounts accounts;
        synchronized (this.mUsers) {
            accounts = this.mUsers.get(userId);
            boolean validateAccounts = false;
            if (accounts == null) {
                java.io.File preNDbFile = new java.io.File(this.mInjector.getPreNDatabaseName(userId));
                java.io.File deDbFile = new java.io.File(this.mInjector.getDeDatabaseName(userId));
                accounts = new com.android.server.accounts.AccountManagerService.UserAccounts(this.mContext, userId, preNDbFile, deDbFile);
                this.mUsers.append(userId, accounts);
                purgeOldGrants(accounts);
                android.accounts.AccountManager.invalidateLocalAccountsDataCaches();
                validateAccounts = true;
            }
            if (!accounts.accountsDb.isCeDatabaseAttached() && this.mLocalUnlockedUsers.get(userId)) {
                android.util.Log.i(TAG, "User " + userId + " is unlocked - opening CE database");
                synchronized (accounts.dbLock) {
                    synchronized (accounts.cacheLock) {
                        java.io.File ceDatabaseFile = new java.io.File(this.mInjector.getCeDatabaseName(userId));
                        accounts.accountsDb.attachCeDatabase(ceDatabaseFile);
                    }
                }
                syncDeCeAccountsLocked(accounts);
            }
            if (validateAccounts) {
                validateAccountsInternal(accounts, true);
            }
        }
        return accounts;
    }

    private void syncDeCeAccountsLocked(com.android.server.accounts.AccountManagerService.UserAccounts accounts) throws java.lang.Throwable {
        com.android.internal.util.Preconditions.checkState(java.lang.Thread.holdsLock(this.mUsers), "mUsers lock must be held");
        java.util.List<android.accounts.Account> accountsToRemove = accounts.accountsDb.findCeAccountsNotInDe();
        if (!accountsToRemove.isEmpty()) {
            android.util.Slog.i(TAG, accountsToRemove.size() + " accounts were previously deleted while user " + accounts.userId + " was locked. Removing accounts from CE tables");
            logRecord(accounts, com.android.server.accounts.AccountsDb.DEBUG_ACTION_SYNC_DE_CE_ACCOUNTS, "accounts");
            for (android.accounts.Account account : accountsToRemove) {
                removeAccountInternal(accounts, account, 1000);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void purgeOldGrantsAll() {
        synchronized (this.mUsers) {
            for (int i = 0; i < this.mUsers.size(); i++) {
                purgeOldGrants(this.mUsers.valueAt(i));
            }
        }
    }

    private void purgeOldGrants(com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        synchronized (accounts.dbLock) {
            synchronized (accounts.cacheLock) {
                try {
                    java.util.List<java.lang.Integer> uids = accounts.accountsDb.findAllUidGrants();
                    java.util.Iterator<java.lang.Integer> it = uids.iterator();
                    while (it.hasNext()) {
                        int uid = it.next().intValue();
                        boolean packageExists = this.mPackageManager.getPackagesForUid(uid) != null;
                        if (!packageExists) {
                            android.util.Log.d(TAG, "deleting grants for UID " + uid + " because its package is no longer installed");
                            accounts.accountsDb.deleteGrantsByUid(uid);
                        }
                    }
                } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e) {
                    android.util.Log.w(TAG, "Could not delete grants for user = " + accounts.userId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeVisibilityValuesForPackage(java.lang.String packageName) {
        if (isSpecialPackageKey(packageName)) {
            return;
        }
        synchronized (this.mUsers) {
            int numberOfUsers = this.mUsers.size();
            for (int i = 0; i < numberOfUsers; i++) {
                com.android.server.accounts.AccountManagerService.UserAccounts accounts = this.mUsers.valueAt(i);
                try {
                    this.mPackageManager.getPackageUidAsUser(packageName, accounts.userId);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    try {
                        accounts.accountsDb.deleteAccountVisibilityForPackage(packageName);
                        synchronized (accounts.dbLock) {
                            synchronized (accounts.cacheLock) {
                                for (android.accounts.Account account : accounts.visibilityCache.keySet()) {
                                    java.util.Map<java.lang.String, java.lang.Integer> accountVisibility = getPackagesAndVisibilityForAccountLocked(account, accounts);
                                    accountVisibility.remove(packageName);
                                }
                                android.accounts.AccountManager.invalidateLocalAccountsDataCaches();
                            }
                        }
                    } catch (android.database.sqlite.SQLiteCantOpenDatabaseException | android.database.sqlite.SQLiteReadOnlyDatabaseException sqlException) {
                        android.util.Log.w(TAG, "Could not delete account visibility for user = " + accounts.userId, sqlException);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void purgeUserData(int userId) {
        com.android.server.accounts.AccountManagerService.UserAccounts accounts;
        synchronized (this.mUsers) {
            accounts = this.mUsers.get(userId);
            this.mUsers.remove(userId);
            this.mLocalUnlockedUsers.delete(userId);
            android.accounts.AccountManager.invalidateLocalAccountsDataCaches();
        }
        if (accounts != null) {
            synchronized (accounts.dbLock) {
                synchronized (accounts.cacheLock) {
                    accounts.accountsDb.closeDebugStatement();
                    accounts.accountsDb.close();
                }
            }
        }
    }

    void onUserUnlocked(android.content.Intent intent) {
        onUnlockUser(intent.getIntExtra("android.intent.extra.user_handle", -1));
    }

    void onUnlockUser(final int userId) {
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "onUserUnlocked " + userId);
        }
        synchronized (this.mUsers) {
            this.mLocalUnlockedUsers.put(userId, true);
        }
        if (userId < 1) {
            return;
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$onUnlockUser$1(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: syncSharedAccounts, reason: merged with bridge method [inline-methods] */
    public void lambda$onUnlockUser$1(int userId) throws java.lang.Throwable {
        try {
            android.accounts.Account[] sharedAccounts = getSharedAccountsAsUser(userId);
            if (sharedAccounts != null && sharedAccounts.length != 0) {
                android.accounts.Account[] accounts = getAccountsAsUser(null, userId, this.mContext.getOpPackageName());
                for (android.accounts.Account sa : sharedAccounts) {
                    if (!com.android.internal.util.ArrayUtils.contains(accounts, sa)) {
                        copyAccountToUser(null, sa, 0, userId);
                    }
                }
            }
        } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e) {
            android.util.Log.e(TAG, "error while get accounts", e);
        }
    }

    public void onServiceChanged(android.accounts.AuthenticatorDescription desc, int userId, boolean removed) throws java.lang.Throwable {
        android.content.pm.UserInfo user = getUserManager().getUserInfo(userId);
        if (user == null) {
            android.util.Log.w(TAG, "onServiceChanged: ignore removed user " + userId);
            return;
        }
        try {
            validateAccountsInternal(getUserAccounts(userId), false);
        } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e) {
            android.util.Log.e(TAG, "User " + userId + " may be removed when " + desc + " removed = " + removed, e);
        }
    }

    public java.lang.String getPassword(android.accounts.Account account) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "getPassword: " + account + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid());
        }
        if (account == null) {
            throw new java.lang.IllegalArgumentException("account is null");
        }
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot get secrets for accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            return readPasswordInternal(accounts, account);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private java.lang.String readPasswordInternal(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account) {
        java.lang.String strFindAccountPasswordByNameAndType;
        if (account == null) {
            return null;
        }
        if (!isLocalUnlockedUser(accounts.userId)) {
            android.util.Log.w(TAG, "Password is not available - user " + accounts.userId + " data is locked");
            return null;
        }
        synchronized (accounts.dbLock) {
            synchronized (accounts.cacheLock) {
                strFindAccountPasswordByNameAndType = accounts.accountsDb.findAccountPasswordByNameAndType(account.name, account.type);
            }
        }
        return strFindAccountPasswordByNameAndType;
    }

    public java.lang.String getPreviousName(android.accounts.Account account) {
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "getPreviousName: " + account + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid());
        }
        java.util.Objects.requireNonNull(account, "account cannot be null");
        int userId = android.os.UserHandle.getCallingUserId();
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            return readPreviousNameInternal(accounts, account);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private java.lang.String readPreviousNameInternal(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account) {
        if (account == null) {
            return null;
        }
        synchronized (accounts.dbLock) {
            synchronized (accounts.cacheLock) {
                java.util.concurrent.atomic.AtomicReference<java.lang.String> previousNameRef = (java.util.concurrent.atomic.AtomicReference) accounts.previousNameCache.get(account);
                if (previousNameRef == null) {
                    java.lang.String previousName = accounts.accountsDb.findDeAccountPreviousName(account);
                    accounts.previousNameCache.put(account, new java.util.concurrent.atomic.AtomicReference<>(previousName));
                    return previousName;
                }
                return previousNameRef.get();
            }
        }
    }

    public java.lang.String getUserData(android.accounts.Account account, java.lang.String key) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            java.lang.String msg = java.lang.String.format("getUserData( account: %s, key: %s, callerUid: %s, pid: %s", account, key, java.lang.Integer.valueOf(callingUid), java.lang.Integer.valueOf(android.os.Binder.getCallingPid()));
            android.util.Log.v(TAG, msg);
        }
        java.util.Objects.requireNonNull(account, "account cannot be null");
        java.util.Objects.requireNonNull(key, "key cannot be null");
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg2 = java.lang.String.format("uid %s cannot get user data for accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg2);
        }
        if (!isLocalUnlockedUser(userId)) {
            android.util.Log.w(TAG, "User " + userId + " data is locked. callingUid " + callingUid);
            return null;
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            if (accountExistsCache(accounts, account)) {
                return readUserDataInternal(accounts, account, key);
            }
            return null;
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public android.accounts.AuthenticatorDescription[] getAuthenticatorTypes(int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "getAuthenticatorTypes: for user id " + userId + " caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        if (isCrossUser(callingUid, userId) && !this.mAccountManagerServiceExt.isMultiAppUserId(userId)) {
            throw new java.lang.SecurityException(java.lang.String.format("User %s tying to get authenticator types for %s", java.lang.Integer.valueOf(android.os.UserHandle.getCallingUserId()), java.lang.Integer.valueOf(userId)));
        }
        long identityToken = clearCallingIdentity();
        try {
            return getAuthenticatorTypesInternal(userId, callingUid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private android.accounts.AuthenticatorDescription[] getAuthenticatorTypesInternal(int userId, int callingUid) {
        this.mAuthenticatorCache.updateServices(userId);
        java.util.Collection<android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription>> authenticatorCollection = this.mAuthenticatorCache.getAllServices(userId);
        java.util.List<android.accounts.AuthenticatorDescription> types = new java.util.ArrayList<>(authenticatorCollection.size());
        for (android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription> authenticator : authenticatorCollection) {
            if (canCallerAccessPackage(((android.accounts.AuthenticatorDescription) authenticator.type).packageName, callingUid, userId)) {
                types.add((android.accounts.AuthenticatorDescription) authenticator.type);
            }
        }
        return (android.accounts.AuthenticatorDescription[]) types.toArray(new android.accounts.AuthenticatorDescription[types.size()]);
    }

    private boolean isCrossUser(int callingUid, int userId) {
        return (userId == android.os.UserHandle.getCallingUserId() || callingUid == 1000 || this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0) ? false : true;
    }

    public boolean addAccountExplicitly(android.accounts.Account account, java.lang.String password, android.os.Bundle extras, java.lang.String opPackageName) {
        return addAccountExplicitlyWithVisibility(account, password, extras, null, opPackageName);
    }

    public void copyAccountToUser(final android.accounts.IAccountManagerResponse response, final android.accounts.Account account, final int userFrom, int userTo) throws java.lang.Throwable {
        int callingUid = android.os.Binder.getCallingUid();
        if (isCrossUser(callingUid, -1)) {
            throw new java.lang.SecurityException("Calling copyAccountToUser requires android.permission.INTERACT_ACROSS_USERS_FULL");
        }
        com.android.server.accounts.AccountManagerService.UserAccounts fromAccounts = getUserAccounts(userFrom);
        final com.android.server.accounts.AccountManagerService.UserAccounts toAccounts = getUserAccounts(userTo);
        if (fromAccounts != null && toAccounts != null) {
            android.util.Slog.d(TAG, "Copying account " + account.toSafeString() + " from user " + userFrom + " to user " + userTo);
            long identityToken = clearCallingIdentity();
            try {
                try {
                    new com.android.server.accounts.AccountManagerService.Session(fromAccounts, response, account.type, false, false, account.name, false) { // from class: com.android.server.accounts.AccountManagerService.5
                        @Override // com.android.server.accounts.AccountManagerService.Session
                        protected java.lang.String toDebugString(long now) {
                            return super.toDebugString(now) + ", getAccountCredentialsForClone, " + account.type;
                        }

                        @Override // com.android.server.accounts.AccountManagerService.Session
                        public void run() throws android.os.RemoteException {
                            this.mAuthenticator.getAccountCredentialsForCloning(this, account);
                        }

                        @Override // com.android.server.accounts.AccountManagerService.Session
                        public void onResult(android.os.Bundle result) {
                            android.os.Bundle.setDefusable(result, true);
                            if (result != null && result.getBoolean("booleanResult", false)) {
                                com.android.server.accounts.AccountManagerService.this.completeCloningAccount(response, result, account, toAccounts, userFrom);
                            } else {
                                super.onResult(result);
                            }
                        }
                    }.bind();
                    restoreCallingIdentity(identityToken);
                } catch (java.lang.Throwable th) {
                    th = th;
                    restoreCallingIdentity(identityToken);
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        } else if (response != null) {
            android.os.Bundle result = new android.os.Bundle();
            result.putBoolean("booleanResult", false);
            try {
                response.onResult(result);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to report error back to the client." + e);
            }
        }
    }

    public boolean accountAuthenticated(android.accounts.Account account) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            java.lang.String msg = java.lang.String.format("accountAuthenticated( account: %s, callerUid: %s)", account, java.lang.Integer.valueOf(callingUid));
            android.util.Log.v(TAG, msg);
        }
        java.util.Objects.requireNonNull(account, "account cannot be null");
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg2 = java.lang.String.format("uid %s cannot notify authentication for accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg2);
        }
        if (!canUserModifyAccounts(userId, callingUid) || !canUserModifyAccountsForType(userId, account.type, callingUid)) {
            return false;
        }
        long identityToken = clearCallingIdentity();
        try {
            getUserAccounts(userId);
            return updateLastAuthenticatedTime(account);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateLastAuthenticatedTime(android.accounts.Account account) {
        boolean zUpdateAccountLastAuthenticatedTime;
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccountsForCaller();
        synchronized (accounts.dbLock) {
            synchronized (accounts.cacheLock) {
                zUpdateAccountLastAuthenticatedTime = accounts.accountsDb.updateAccountLastAuthenticatedTime(account);
            }
        }
        return zUpdateAccountLastAuthenticatedTime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void completeCloningAccount(android.accounts.IAccountManagerResponse response, final android.os.Bundle accountCredentials, final android.accounts.Account account, com.android.server.accounts.AccountManagerService.UserAccounts targetUser, final int parentUserId) {
        android.os.Bundle.setDefusable(accountCredentials, true);
        long id = clearCallingIdentity();
        try {
            new com.android.server.accounts.AccountManagerService.Session(targetUser, response, account.type, false, false, account.name, false) { // from class: com.android.server.accounts.AccountManagerService.6
                @Override // com.android.server.accounts.AccountManagerService.Session
                protected java.lang.String toDebugString(long now) {
                    return super.toDebugString(now) + ", getAccountCredentialsForClone, " + account.type;
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws android.os.RemoteException {
                    for (android.accounts.Account acc : com.android.server.accounts.AccountManagerService.this.getAccounts(parentUserId, com.android.server.accounts.AccountManagerService.this.mContext.getOpPackageName())) {
                        if (acc.equals(account)) {
                            this.mAuthenticator.addAccountFromCredentials(this, account, accountCredentials);
                            return;
                        }
                    }
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void onResult(android.os.Bundle result) {
                    android.os.Bundle.setDefusable(result, true);
                    super.onResult(result);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void onError(int errorCode, java.lang.String errorMessage) {
                    super.onError(errorCode, errorMessage);
                }
            }.bind();
        } finally {
            restoreCallingIdentity(id);
        }
    }

    private boolean addAccountInternal(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String password, android.os.Bundle extras, int callingUid, java.util.Map<java.lang.String, java.lang.Integer> packageToVisibility, java.lang.String opPackageName) throws java.lang.Throwable {
        long accountId;
        android.os.Bundle.setDefusable(extras, true);
        if (account == null) {
            return false;
        }
        if (account.name != null && account.name.length() > 200) {
            android.util.Log.w(TAG, "Account cannot be added - Name longer than 200 chars");
            return false;
        }
        if (account.type != null && account.type.length() > 200) {
            android.util.Log.w(TAG, "Account cannot be added - Name longer than 200 chars");
            return false;
        }
        if (!isLocalUnlockedUser(accounts.userId)) {
            android.util.Log.w(TAG, "Account " + account.toSafeString() + " cannot be added - user " + accounts.userId + " is locked. callingUid=" + callingUid);
            return false;
        }
        synchronized (accounts.dbLock) {
            try {
                try {
                    try {
                        synchronized (accounts.cacheLock) {
                            try {
                                accounts.accountsDb.beginTransaction();
                                try {
                                    if (accounts.accountsDb.findCeAccountId(account) >= 0) {
                                        android.util.Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping since the account already exists");
                                        accounts.accountsDb.endTransaction();
                                        return false;
                                    }
                                    if (accounts.accountsDb.findAllDeAccounts().size() > 100) {
                                        android.util.Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping since more than 100 accounts on device exist");
                                        accounts.accountsDb.endTransaction();
                                        return false;
                                    }
                                    long accountId2 = accounts.accountsDb.insertCeAccount(account, password);
                                    if (accountId2 < 0) {
                                        android.util.Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping the DB insert failed");
                                        accounts.accountsDb.endTransaction();
                                        return false;
                                    }
                                    if (accounts.accountsDb.insertDeAccount(account, accountId2) < 0) {
                                        android.util.Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping the DB insert failed");
                                        accounts.accountsDb.endTransaction();
                                        return false;
                                    }
                                    if (extras != null) {
                                        for (java.lang.String key : extras.keySet()) {
                                            java.lang.String value = extras.getString(key);
                                            if (accounts.accountsDb.insertExtra(accountId2, key, value) < 0) {
                                                android.util.Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping since insertExtra failed for key " + key);
                                                accounts.accountsDb.endTransaction();
                                                return false;
                                            }
                                            android.accounts.AccountManager.invalidateLocalAccountUserDataCaches();
                                        }
                                    }
                                    if (packageToVisibility != null) {
                                        for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : packageToVisibility.entrySet()) {
                                            setAccountVisibility(account, entry.getKey(), entry.getValue().intValue(), false, accounts, callingUid);
                                            accountId2 = accountId2;
                                        }
                                        accountId = accountId2;
                                    } else {
                                        accountId = accountId2;
                                    }
                                    accounts.accountsDb.setTransactionSuccessful();
                                    logRecord(com.android.server.accounts.AccountsDb.DEBUG_ACTION_ACCOUNT_ADD, "accounts", accountId, accounts, callingUid);
                                    insertAccountIntoCacheLocked(accounts, account);
                                    accounts.accountsDb.endTransaction();
                                    if (getUserManager().getUserInfo(accounts.userId).canHaveProfile()) {
                                        addAccountToLinkedRestrictedUsers(account, accounts.userId);
                                    }
                                    sendNotificationAccountUpdated(account, accounts);
                                    android.util.Log.i(TAG, "callingUid=" + callingUid + ", userId=" + accounts.userId + " added account");
                                    sendAccountsChangedBroadcast(accounts.userId, account.type, "addAccount");
                                    logAddAccountExplicitlyMetrics(opPackageName, account.type, packageToVisibility);
                                    return true;
                                } catch (java.lang.Throwable th) {
                                    accounts.accountsDb.endTransaction();
                                    throw th;
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                    throw th;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
                throw th;
            }
        }
    }

    private void logAddAccountExplicitlyMetrics(java.lang.String callerPackage, java.lang.String accountType, java.util.Map<java.lang.String, java.lang.Integer> accountVisibility) {
        android.app.admin.DevicePolicyEventLogger.createEvent(203).setStrings(android.text.TextUtils.emptyIfNull(accountType), android.text.TextUtils.emptyIfNull(callerPackage), findPackagesPerVisibility(accountVisibility)).write();
    }

    private java.lang.String[] findPackagesPerVisibility(java.util.Map<java.lang.String, java.lang.Integer> accountVisibility) {
        java.util.Map<java.lang.Integer, java.util.Set<java.lang.String>> packagesPerVisibility = new java.util.HashMap<>();
        if (accountVisibility != null) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : accountVisibility.entrySet()) {
                if (!packagesPerVisibility.containsKey(entry.getValue())) {
                    packagesPerVisibility.put(entry.getValue(), new java.util.HashSet<>());
                }
                packagesPerVisibility.get(entry.getValue()).add(entry.getKey());
            }
        }
        java.lang.String[] packagesPerVisibilityStr = {getPackagesForVisibilityStr(0, packagesPerVisibility), getPackagesForVisibilityStr(1, packagesPerVisibility), getPackagesForVisibilityStr(2, packagesPerVisibility), getPackagesForVisibilityStr(3, packagesPerVisibility), getPackagesForVisibilityStr(4, packagesPerVisibility)};
        return packagesPerVisibilityStr;
    }

    private java.lang.String getPackagesForVisibilityStr(int visibility, java.util.Map<java.lang.Integer, java.util.Set<java.lang.String>> packagesPerVisibility) {
        java.lang.String strJoin;
        java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append(visibility).append(":");
        if (packagesPerVisibility.containsKey(java.lang.Integer.valueOf(visibility))) {
            strJoin = android.text.TextUtils.join(",", packagesPerVisibility.get(java.lang.Integer.valueOf(visibility)));
        } else {
            strJoin = "";
        }
        return sbAppend.append(strJoin).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLocalUnlockedUser(int userId) {
        boolean z;
        synchronized (this.mUsers) {
            z = this.mLocalUnlockedUsers.get(userId);
        }
        return z;
    }

    private void addAccountToLinkedRestrictedUsers(android.accounts.Account account, int parentUserId) {
        java.util.List<android.content.pm.UserInfo> users = getUserManager().getUsers();
        for (android.content.pm.UserInfo user : users) {
            if (user.isRestricted() && parentUserId == user.restrictedProfileParentId) {
                addSharedAccountAsUser(account, user.id);
                if (isLocalUnlockedUser(user.id)) {
                    this.mHandler.sendMessage(this.mHandler.obtainMessage(4, parentUserId, user.id, account));
                }
            }
        }
    }

    public void hasFeatures(android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String[] features, int userId, java.lang.String opPackageName) {
        int callingUid = android.os.Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, opPackageName);
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "hasFeatures: " + account + ", response " + response + ", features " + java.util.Arrays.toString(features) + ", caller's uid " + callingUid + ", userId " + userId + ", pid " + android.os.Binder.getCallingPid());
        }
        com.android.internal.util.Preconditions.checkArgument(account != null, "account cannot be null");
        com.android.internal.util.Preconditions.checkArgument(response != null, "response cannot be null");
        com.android.internal.util.Preconditions.checkArgument(features != null, "features cannot be null");
        if (userId != android.os.UserHandle.getCallingUserId() && callingUid != 1000 && this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") != 0) {
            throw new java.lang.SecurityException("User " + android.os.UserHandle.getCallingUserId() + " trying to check account features for " + userId);
        }
        checkReadAccountsPermitted(callingUid, account.type, userId, opPackageName);
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            new com.android.server.accounts.AccountManagerService.TestFeaturesSession(accounts, response, account, features).bind();
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private class TestFeaturesSession extends com.android.server.accounts.AccountManagerService.Session {
        private final android.accounts.Account mAccount;
        private final java.lang.String[] mFeatures;

        public TestFeaturesSession(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String[] features) {
            super(com.android.server.accounts.AccountManagerService.this, accounts, response, account.type, false, true, account.name, false);
            this.mFeatures = features;
            this.mAccount = account;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void run() throws android.os.RemoteException {
            try {
                this.mAuthenticator.hasFeatures(this, this.mAccount, this.mFeatures);
            } catch (android.os.RemoteException e) {
                onError(1, "remote exception");
            }
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void onResult(android.os.Bundle result) {
            android.os.Bundle.setDefusable(result, true);
            android.accounts.IAccountManagerResponse response = getResponseAndClose();
            if (response != null) {
                try {
                    if (result == null) {
                        response.onError(5, "null bundle");
                        return;
                    }
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + response);
                    }
                    android.os.Bundle newResult = new android.os.Bundle();
                    newResult.putBoolean("booleanResult", result.getBoolean("booleanResult", false));
                    response.onResult(newResult);
                } catch (android.os.RemoteException e) {
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "failure while notifying response", e);
                    }
                }
            }
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        protected java.lang.String toDebugString(long now) {
            return super.toDebugString(now) + ", hasFeatures, " + this.mAccount + ", " + (this.mFeatures != null ? android.text.TextUtils.join(",", this.mFeatures) : null);
        }
    }

    public void renameAccount(android.accounts.IAccountManagerResponse response, android.accounts.Account accountToRename, java.lang.String newName) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "renameAccount: " + accountToRename + " -> " + newName + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        if (accountToRename == null) {
            throw new java.lang.IllegalArgumentException("account is null");
        }
        if (newName != null && newName.length() > 200) {
            android.util.Log.e(TAG, "renameAccount failed - account name longer than 200");
            throw new java.lang.IllegalArgumentException("account name longer than 200");
        }
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(accountToRename.type, callingUid, userId)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot rename accounts of type: %s", java.lang.Integer.valueOf(callingUid), accountToRename.type);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            android.util.Log.i(TAG, "callingUid=" + callingUid + ", userId=" + accounts.userId + " performing rename account");
            android.accounts.Account resultingAccount = renameAccountInternal(accounts, accountToRename, newName);
            android.os.Bundle result = new android.os.Bundle();
            result.putString("authAccount", resultingAccount.name);
            result.putString("accountType", resultingAccount.type);
            result.putString("accountAccessId", resultingAccount.getAccessId());
            try {
                response.onResult(result);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(TAG, e.getMessage());
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private android.accounts.Account renameAccountInternal(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account accountToRename, java.lang.String newName) {
        android.accounts.Account renamedAccount;
        cancelNotification(getSigninRequiredNotificationId(accounts, accountToRename), accounts);
        synchronized (accounts.credentialsPermissionNotificationIds) {
            for (android.util.Pair<android.util.Pair<android.accounts.Account, java.lang.String>, java.lang.Integer> pair : accounts.credentialsPermissionNotificationIds.keySet()) {
                if (accountToRename.equals(((android.util.Pair) pair.first).first)) {
                    com.android.server.accounts.AccountManagerService.NotificationId id = (com.android.server.accounts.AccountManagerService.NotificationId) accounts.credentialsPermissionNotificationIds.get(pair);
                    cancelNotification(id, accounts);
                }
            }
        }
        synchronized (accounts.dbLock) {
            synchronized (accounts.cacheLock) {
                java.util.List<java.lang.String> accountRemovedReceivers = getAccountRemovedReceivers(accountToRename, accounts);
                accounts.accountsDb.beginTransaction();
                android.accounts.Account renamedAccount2 = new android.accounts.Account(newName, accountToRename.type);
                try {
                    if (accounts.accountsDb.findCeAccountId(renamedAccount2) >= 0) {
                        android.util.Log.e(TAG, "renameAccount failed - account with new name already exists");
                        return null;
                    }
                    long accountId = accounts.accountsDb.findDeAccountId(accountToRename);
                    if (accountId < 0) {
                        android.util.Log.e(TAG, "renameAccount failed - old account does not exist");
                        return null;
                    }
                    accounts.accountsDb.renameCeAccount(accountId, newName);
                    if (!accounts.accountsDb.renameDeAccount(accountId, newName, accountToRename.name)) {
                        android.util.Log.e(TAG, "renameAccount failed");
                        return null;
                    }
                    accounts.accountsDb.setTransactionSuccessful();
                    accounts.accountsDb.endTransaction();
                    android.accounts.Account renamedAccount3 = insertAccountIntoCacheLocked(accounts, renamedAccount2);
                    java.util.Map<java.lang.String, java.lang.String> tmpData = (java.util.Map) accounts.userDataCache.get(accountToRename);
                    java.util.Map<java.lang.String, java.lang.String> tmpTokens = (java.util.Map) accounts.authTokenCache.get(accountToRename);
                    java.util.Map<java.lang.String, java.lang.Integer> tmpVisibility = (java.util.Map) accounts.visibilityCache.get(accountToRename);
                    removeAccountFromCacheLocked(accounts, accountToRename);
                    accounts.userDataCache.put(renamedAccount3, tmpData);
                    accounts.authTokenCache.put(renamedAccount3, tmpTokens);
                    accounts.visibilityCache.put(renamedAccount3, tmpVisibility);
                    accounts.previousNameCache.put(renamedAccount3, new java.util.concurrent.atomic.AtomicReference(accountToRename.name));
                    int parentUserId = accounts.userId;
                    if (canHaveProfile(parentUserId)) {
                        java.util.List<android.content.pm.UserInfo> users = getUserManager().getAliveUsers();
                        for (android.content.pm.UserInfo user : users) {
                            if (user.isRestricted()) {
                                renamedAccount = renamedAccount3;
                                if (user.restrictedProfileParentId == parentUserId) {
                                    renameSharedAccountAsUser(accountToRename, newName, user.id);
                                }
                            } else {
                                renamedAccount = renamedAccount3;
                            }
                            renamedAccount3 = renamedAccount;
                        }
                    }
                    sendNotificationAccountUpdated(renamedAccount3, accounts);
                    sendAccountsChangedBroadcast(accounts.userId, accountToRename.type, "renameAccount");
                    for (java.lang.String packageName : accountRemovedReceivers) {
                        sendAccountRemovedBroadcast(accountToRename, packageName, accounts.userId, "renameAccount");
                    }
                    android.accounts.AccountManager.invalidateLocalAccountsDataCaches();
                    android.accounts.AccountManager.invalidateLocalAccountUserDataCaches();
                    return renamedAccount3;
                } finally {
                    accounts.accountsDb.endTransaction();
                }
            }
        }
    }

    private boolean canHaveProfile(int parentUserId) {
        android.content.pm.UserInfo userInfo = getUserManager().getUserInfo(parentUserId);
        return userInfo != null && userInfo.canHaveProfile();
    }

    public void removeAccountAsUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, boolean expectActivityLaunch, int userId) throws java.lang.Throwable {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "removeAccount: " + account + ", response " + response + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid() + ", for user id " + userId);
        }
        com.android.internal.util.Preconditions.checkArgument(account != null, "account cannot be null");
        com.android.internal.util.Preconditions.checkArgument(response != null, "response cannot be null");
        if (isCrossUser(callingUid, userId)) {
            throw new java.lang.SecurityException(java.lang.String.format("User %s tying remove account for %s", java.lang.Integer.valueOf(android.os.UserHandle.getCallingUserId()), java.lang.Integer.valueOf(userId)));
        }
        android.os.UserHandle user = android.os.UserHandle.of(userId);
        if (!isAccountManagedByCaller(account.type, callingUid, user.getIdentifier()) && !isSystemUid(callingUid) && !isProfileOwner(callingUid)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot remove accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        if (canUserModifyAccounts(userId, callingUid)) {
            if (!canUserModifyAccountsForType(userId, account.type, callingUid)) {
                try {
                    response.onError(101, "User cannot modify accounts of this type (policy).");
                    return;
                } catch (android.os.RemoteException re) {
                    android.util.Log.w(TAG, "RemoteException while removing account", re);
                    return;
                }
            }
            if (isFirstAccountRemovalDisabled(account)) {
                try {
                    response.onError(101, "User cannot remove the first " + account.type + " account on the device.");
                    return;
                } catch (android.os.RemoteException re2) {
                    android.util.Log.w(TAG, "RemoteException while removing account", re2);
                    return;
                }
            }
            int callingPid = android.os.Binder.getCallingPid();
            long identityToken = clearCallingIdentity();
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            cancelNotification(getSigninRequiredNotificationId(accounts, account), accounts);
            synchronized (accounts.credentialsPermissionNotificationIds) {
                try {
                    for (android.util.Pair<android.util.Pair<android.accounts.Account, java.lang.String>, java.lang.Integer> pair : accounts.credentialsPermissionNotificationIds.keySet()) {
                        try {
                            if (account.equals(((android.util.Pair) pair.first).first)) {
                                com.android.server.accounts.AccountManagerService.NotificationId id = (com.android.server.accounts.AccountManagerService.NotificationId) accounts.credentialsPermissionNotificationIds.get(pair);
                                cancelNotification(id, accounts);
                            }
                        } catch (java.lang.Throwable th) {
                            th = th;
                            while (true) {
                                try {
                                    throw th;
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                }
                            }
                        }
                    }
                    long accountId = accounts.accountsDb.findDeAccountId(account);
                    logRecord(com.android.server.accounts.AccountsDb.DEBUG_ACTION_CALLED_ACCOUNT_REMOVE, "accounts", accountId, accounts, callingUid);
                    try {
                        new com.android.server.accounts.AccountManagerService.RemoveAccountSession(accounts, response, account, expectActivityLaunch).bind(callingUid, callingPid);
                    } finally {
                        restoreCallingIdentity(identityToken);
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            }
        } else {
            try {
                response.onError(100, "User cannot modify accounts");
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public boolean removeAccountExplicitly(android.accounts.Account account) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "removeAccountExplicitly: " + account + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        int userId = android.os.Binder.getCallingUserHandle().getIdentifier();
        if (account == null) {
            android.util.Log.e(TAG, "account is null");
            return false;
        }
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot explicitly remove accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        if (isFirstAccountRemovalDisabled(account)) {
            android.util.Log.e(TAG, "Cannot remove the first " + account.type + " account on the device.");
            return false;
        }
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccountsForCaller();
        long accountId = accounts.accountsDb.findDeAccountId(account);
        logRecord(com.android.server.accounts.AccountsDb.DEBUG_ACTION_CALLED_ACCOUNT_REMOVE, "accounts", accountId, accounts, callingUid);
        long identityToken = clearCallingIdentity();
        try {
            return removeAccountInternal(accounts, account, callingUid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private class RemoveAccountSession extends com.android.server.accounts.AccountManagerService.Session {
        final android.accounts.Account mAccount;

        public RemoveAccountSession(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.IAccountManagerResponse response, android.accounts.Account account, boolean expectActivityLaunch) {
            super(com.android.server.accounts.AccountManagerService.this, accounts, response, account.type, expectActivityLaunch, true, account.name, false);
            this.mAccount = account;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        protected java.lang.String toDebugString(long now) {
            return super.toDebugString(now) + ", removeAccount, account " + this.mAccount;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void run() throws android.os.RemoteException {
            this.mAuthenticator.getAccountRemovalAllowed(this, this.mAccount);
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void onResult(android.os.Bundle result) throws java.lang.Throwable {
            android.os.Bundle.setDefusable(result, true);
            if (result != null && result.containsKey("booleanResult") && !result.containsKey("intent")) {
                boolean removalAllowed = result.getBoolean("booleanResult");
                if (removalAllowed) {
                    com.android.server.accounts.AccountManagerService.this.removeAccountInternal(this.mAccounts, this.mAccount, getCallingUid());
                }
                android.accounts.IAccountManagerResponse response = getResponseAndClose();
                if (response != null) {
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + response);
                    }
                    try {
                        response.onResult(result);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.accounts.AccountManagerService.TAG, "Error calling onResult()", e);
                    }
                }
            }
            super.onResult(result);
        }
    }

    protected void removeAccountInternal(android.accounts.Account account) throws java.lang.Throwable {
        removeAccountInternal(getUserAccountsForCaller(), account, getCallingUid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeAccountInternal(final com.android.server.accounts.AccountManagerService.UserAccounts accounts, final android.accounts.Account account, int callingUid) throws java.lang.Throwable {
        java.util.Map<java.lang.String, java.lang.Integer> packagesToVisibility;
        java.util.List<java.lang.String> accountRemovedReceivers;
        boolean isChanged;
        boolean userUnlocked = isLocalUnlockedUser(accounts.userId);
        if (!userUnlocked) {
            android.util.Slog.i(TAG, "Removing account " + account.toSafeString() + " while user " + accounts.userId + " is still locked. CE data will be removed later");
        }
        synchronized (accounts.dbLock) {
            try {
                synchronized (accounts.cacheLock) {
                    try {
                        packagesToVisibility = getRequestingPackages(account, accounts);
                        accountRemovedReceivers = getAccountRemovedReceivers(account, accounts);
                        accounts.accountsDb.beginTransaction();
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                    try {
                        long accountId = accounts.accountsDb.findDeAccountId(account);
                        if (accountId >= 0) {
                            try {
                                boolean isChanged2 = accounts.accountsDb.deleteDeAccount(accountId);
                                isChanged = isChanged2;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                accounts.accountsDb.endTransaction();
                                throw th;
                            }
                        } else {
                            isChanged = false;
                        }
                        if (userUnlocked) {
                            try {
                                long ceAccountId = accounts.accountsDb.findCeAccountId(account);
                                if (ceAccountId >= 0) {
                                    accounts.accountsDb.deleteCeAccount(ceAccountId);
                                }
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                                accounts.accountsDb.endTransaction();
                                throw th;
                            }
                        }
                        try {
                            accounts.accountsDb.setTransactionSuccessful();
                            try {
                                accounts.accountsDb.endTransaction();
                                if (isChanged) {
                                    removeAccountFromCacheLocked(accounts, account);
                                    for (java.util.Map.Entry<java.lang.String, java.lang.Integer> packageToVisibility : packagesToVisibility.entrySet()) {
                                        if (packageToVisibility.getValue().intValue() == 1 || packageToVisibility.getValue().intValue() == 2) {
                                            notifyPackage(packageToVisibility.getKey(), accounts);
                                        }
                                    }
                                    android.util.Log.i(TAG, "callingUid=" + callingUid + ", userId=" + accounts.userId + " removed account");
                                    sendAccountsChangedBroadcast(accounts.userId, account.type, "removeAccount");
                                    for (java.lang.String packageName : accountRemovedReceivers) {
                                        sendAccountRemovedBroadcast(account, packageName, accounts.userId, "removeAccount");
                                    }
                                    java.lang.String action = userUnlocked ? com.android.server.accounts.AccountsDb.DEBUG_ACTION_ACCOUNT_REMOVE : com.android.server.accounts.AccountsDb.DEBUG_ACTION_ACCOUNT_REMOVE_DE;
                                    logRecord(action, "accounts", accountId, accounts);
                                }
                                try {
                                    long id = android.os.Binder.clearCallingIdentity();
                                    try {
                                        int parentUserId = accounts.userId;
                                        if (canHaveProfile(parentUserId)) {
                                            java.util.List<android.content.pm.UserInfo> users = getUserManager().getAliveUsers();
                                            for (android.content.pm.UserInfo user : users) {
                                                if (user.isRestricted() && parentUserId == user.restrictedProfileParentId) {
                                                    removeSharedAccountAsUser(account, user.id, callingUid);
                                                }
                                            }
                                        }
                                        if (isChanged) {
                                            synchronized (accounts.credentialsPermissionNotificationIds) {
                                                for (android.util.Pair<android.util.Pair<android.accounts.Account, java.lang.String>, java.lang.Integer> key : accounts.credentialsPermissionNotificationIds.keySet()) {
                                                    if (account.equals(((android.util.Pair) key.first).first) && "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE".equals(((android.util.Pair) key.first).second)) {
                                                        final int uid = ((java.lang.Integer) key.second).intValue();
                                                        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda3
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                this.f$0.lambda$removeAccountInternal$2(account, uid, accounts);
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                        android.accounts.AccountManager.invalidateLocalAccountUserDataCaches();
                                        return isChanged;
                                    } finally {
                                        android.os.Binder.restoreCallingIdentity(id);
                                    }
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                    throw th;
                                }
                            } catch (java.lang.Throwable th5) {
                                th = th5;
                                throw th;
                            }
                        } catch (java.lang.Throwable th6) {
                            th = th6;
                            accounts.accountsDb.endTransaction();
                            throw th;
                        }
                    } catch (java.lang.Throwable th7) {
                        th = th7;
                    }
                }
            } catch (java.lang.Throwable th8) {
                th = th8;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAccountInternal$2(android.accounts.Account account, int uid, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        cancelAccountAccessRequestNotificationIfNeeded(account, uid, false, accounts);
    }

    public void invalidateAuthToken(java.lang.String accountType, java.lang.String authToken) {
        int callerUid = android.os.Binder.getCallingUid();
        java.util.Objects.requireNonNull(accountType, "accountType cannot be null");
        java.util.Objects.requireNonNull(authToken, "authToken cannot be null");
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "invalidateAuthToken: accountType " + accountType + ", caller's uid " + callerUid + ", pid " + android.os.Binder.getCallingPid());
        }
        int userId = android.os.UserHandle.getCallingUserId();
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            synchronized (accounts.dbLock) {
                accounts.accountsDb.beginTransaction();
                try {
                    java.util.List<android.util.Pair<android.accounts.Account, java.lang.String>> deletedTokens = invalidateAuthTokenLocked(accounts, accountType, authToken);
                    accounts.accountsDb.setTransactionSuccessful();
                    accounts.accountsDb.endTransaction();
                    synchronized (accounts.cacheLock) {
                        for (android.util.Pair<android.accounts.Account, java.lang.String> tokenInfo : deletedTokens) {
                            android.accounts.Account act = (android.accounts.Account) tokenInfo.first;
                            java.lang.String tokenType = (java.lang.String) tokenInfo.second;
                            writeAuthTokenIntoCacheLocked(accounts, act, tokenType, null);
                        }
                        accounts.accountTokenCaches.remove(accountType, authToken);
                    }
                } catch (java.lang.Throwable th) {
                    accounts.accountsDb.endTransaction();
                    throw th;
                }
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private java.util.List<android.util.Pair<android.accounts.Account, java.lang.String>> invalidateAuthTokenLocked(com.android.server.accounts.AccountManagerService.UserAccounts accounts, java.lang.String accountType, java.lang.String authToken) {
        java.util.List<android.util.Pair<android.accounts.Account, java.lang.String>> results = new java.util.ArrayList<>();
        android.database.Cursor cursor = accounts.accountsDb.findAuthtokenForAllAccounts(accountType, authToken);
        while (cursor.moveToNext()) {
            try {
                java.lang.String authTokenId = cursor.getString(0);
                java.lang.String accountName = cursor.getString(1);
                java.lang.String authTokenType = cursor.getString(2);
                accounts.accountsDb.deleteAuthToken(authTokenId);
                results.add(android.util.Pair.create(new android.accounts.Account(accountName, accountType), authTokenType));
            } finally {
                cursor.close();
            }
        }
        return results;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCachedToken(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String callerPkg, byte[] callerSigDigest, java.lang.String tokenType, java.lang.String token, long expiryMillis) {
        if (account == null || tokenType == null || callerPkg == null) {
            return;
        }
        if (callerSigDigest == null) {
            return;
        }
        cancelNotification(getSigninRequiredNotificationId(accounts, account), accounts);
        synchronized (accounts.cacheLock) {
            accounts.accountTokenCaches.put(account, token, tokenType, callerPkg, callerSigDigest, expiryMillis);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean saveAuthTokenToDatabase(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String type, java.lang.String authToken) {
        if (account == null || type == null) {
            return false;
        }
        cancelNotification(getSigninRequiredNotificationId(accounts, account), accounts);
        synchronized (accounts.dbLock) {
            accounts.accountsDb.beginTransaction();
            try {
                long accountId = accounts.accountsDb.findDeAccountId(account);
                if (accountId < 0) {
                    accounts.accountsDb.endTransaction();
                    if (0 != 0) {
                        synchronized (accounts.cacheLock) {
                            try {
                                writeAuthTokenIntoCacheLocked(accounts, account, type, authToken);
                            } catch (java.lang.Throwable th) {
                                throw th;
                            }
                        }
                    }
                    return false;
                }
                accounts.accountsDb.deleteAuthtokensByAccountIdAndType(accountId, type);
                if (accounts.accountsDb.insertAuthToken(accountId, type, authToken) < 0) {
                    accounts.accountsDb.endTransaction();
                    if (0 != 0) {
                        synchronized (accounts.cacheLock) {
                            writeAuthTokenIntoCacheLocked(accounts, account, type, authToken);
                        }
                    }
                    return false;
                }
                accounts.accountsDb.setTransactionSuccessful();
                accounts.accountsDb.endTransaction();
                if (1 != 0) {
                    synchronized (accounts.cacheLock) {
                        writeAuthTokenIntoCacheLocked(accounts, account, type, authToken);
                    }
                }
                return true;
            } catch (java.lang.Throwable th2) {
                accounts.accountsDb.endTransaction();
                if (0 != 0) {
                    synchronized (accounts.cacheLock) {
                        writeAuthTokenIntoCacheLocked(accounts, account, type, authToken);
                    }
                }
                throw th2;
            }
        }
    }

    public java.lang.String peekAuthToken(android.accounts.Account account, java.lang.String authTokenType) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "peekAuthToken: " + account + ", authTokenType " + authTokenType + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        java.util.Objects.requireNonNull(account, "account cannot be null");
        java.util.Objects.requireNonNull(authTokenType, "authTokenType cannot be null");
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot peek the authtokens associated with accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        if (!isLocalUnlockedUser(userId)) {
            android.util.Log.w(TAG, "Authtoken not available - user " + userId + " data is locked. callingUid " + callingUid);
            return null;
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            return readAuthTokenInternal(accounts, account, authTokenType);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void setAuthToken(android.accounts.Account account, java.lang.String authTokenType, java.lang.String authToken) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "setAuthToken: " + account + ", authTokenType " + authTokenType + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        java.util.Objects.requireNonNull(account, "account cannot be null");
        java.util.Objects.requireNonNull(authTokenType, "authTokenType cannot be null");
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot set auth tokens associated with accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            saveAuthTokenToDatabase(accounts, account, authTokenType, authToken);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void setPassword(android.accounts.Account account, java.lang.String password) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "setAuthToken: " + account + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        java.util.Objects.requireNonNull(account, "account cannot be null");
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot set secrets for accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            setPasswordInternal(accounts, account, password, callingUid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private void setPasswordInternal(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String password, int callingUid) {
        java.lang.String action;
        if (account == null) {
            return;
        }
        boolean isChanged = false;
        synchronized (accounts.dbLock) {
            synchronized (accounts.cacheLock) {
                accounts.accountsDb.beginTransaction();
                try {
                    long accountId = accounts.accountsDb.findDeAccountId(account);
                    if (accountId >= 0) {
                        accounts.accountsDb.updateCeAccountPassword(accountId, password);
                        accounts.accountsDb.deleteAuthTokensByAccountId(accountId);
                        accounts.authTokenCache.remove(account);
                        accounts.accountTokenCaches.remove(account);
                        accounts.accountsDb.setTransactionSuccessful();
                        if (password == null) {
                            action = com.android.server.accounts.AccountsDb.DEBUG_ACTION_CLEAR_PASSWORD;
                            logRecord(action, "accounts", accountId, accounts, callingUid);
                            isChanged = true;
                        } else {
                            try {
                                if (password.length() == 0) {
                                    action = com.android.server.accounts.AccountsDb.DEBUG_ACTION_CLEAR_PASSWORD;
                                    logRecord(action, "accounts", accountId, accounts, callingUid);
                                    isChanged = true;
                                } else {
                                    action = com.android.server.accounts.AccountsDb.DEBUG_ACTION_SET_PASSWORD;
                                    logRecord(action, "accounts", accountId, accounts, callingUid);
                                    isChanged = true;
                                }
                            } catch (java.lang.Throwable th) {
                                th = th;
                                isChanged = true;
                                accounts.accountsDb.endTransaction();
                                if (isChanged) {
                                    sendNotificationAccountUpdated(account, accounts);
                                    android.util.Log.i(TAG, "callingUid=" + callingUid + " changed password");
                                    sendAccountsChangedBroadcast(accounts.userId, account.type, "setPassword");
                                }
                                throw th;
                            }
                        }
                    }
                    accounts.accountsDb.endTransaction();
                    if (isChanged) {
                        sendNotificationAccountUpdated(account, accounts);
                        android.util.Log.i(TAG, "callingUid=" + callingUid + " changed password");
                        sendAccountsChangedBroadcast(accounts.userId, account.type, "setPassword");
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }
    }

    public void clearPassword(android.accounts.Account account) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "clearPassword: " + account + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        java.util.Objects.requireNonNull(account, "account cannot be null");
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot clear passwords for accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            setPasswordInternal(accounts, account, null, callingUid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void setUserData(android.accounts.Account account, java.lang.String key, java.lang.String value) {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "setUserData: " + account + ", key " + key + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        if (key == null) {
            throw new java.lang.IllegalArgumentException("key is null");
        }
        if (account == null) {
            throw new java.lang.IllegalArgumentException("account is null");
        }
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, userId)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot set user data for accounts of type: %s", java.lang.Integer.valueOf(callingUid), account.type);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            if (!accountExistsCache(accounts, account)) {
                return;
            }
            setUserdataInternal(accounts, account, key, value);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private boolean accountExistsCache(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account) {
        synchronized (accounts.cacheLock) {
            if (accounts.accountCache.containsKey(account.type)) {
                for (android.accounts.Account acc : accounts.accountCache.get(account.type)) {
                    if (acc.name.equals(account.name)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private void setUserdataInternal(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String key, java.lang.String value) {
        synchronized (accounts.dbLock) {
            accounts.accountsDb.beginTransaction();
            try {
                long accountId = accounts.accountsDb.findDeAccountId(account);
                if (accountId < 0) {
                    return;
                }
                long extrasId = accounts.accountsDb.findExtrasIdByAccountId(accountId, key);
                if (extrasId < 0) {
                    if (accounts.accountsDb.insertExtra(accountId, key, value) < 0) {
                        return;
                    }
                } else if (!accounts.accountsDb.updateExtra(extrasId, value)) {
                    return;
                }
                accounts.accountsDb.setTransactionSuccessful();
                accounts.accountsDb.endTransaction();
                synchronized (accounts.cacheLock) {
                    writeUserDataIntoCacheLocked(accounts, account, key, value);
                    android.accounts.AccountManager.invalidateLocalAccountUserDataCaches();
                }
            } finally {
                accounts.accountsDb.endTransaction();
            }
        }
    }

    private void onResult(android.accounts.IAccountManagerResponse response, android.os.Bundle result) {
        if (result == null) {
            android.util.Log.e(TAG, "the result is unexpectedly null", new java.lang.Exception());
        }
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, getClass().getSimpleName() + " calling onResult() on response " + response);
        }
        try {
            response.onResult(result);
        } catch (android.os.RemoteException e) {
            if (android.util.Log.isLoggable(TAG, 2)) {
                android.util.Log.v(TAG, "failure while notifying response", e);
            }
        }
    }

    public void getAuthTokenLabel(android.accounts.IAccountManagerResponse response, final java.lang.String accountType, final java.lang.String authTokenType) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkArgument(accountType != null, "accountType cannot be null");
        com.android.internal.util.Preconditions.checkArgument(authTokenType != null, "authTokenType cannot be null");
        int callingUid = getCallingUid();
        clearCallingIdentity();
        if (android.os.UserHandle.getAppId(callingUid) != 1000) {
            throw new java.lang.SecurityException("can only call from system");
        }
        int userId = android.os.UserHandle.getUserId(callingUid);
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            new com.android.server.accounts.AccountManagerService.Session(accounts, response, accountType, false, false, null, false) { // from class: com.android.server.accounts.AccountManagerService.7
                @Override // com.android.server.accounts.AccountManagerService.Session
                protected java.lang.String toDebugString(long now) {
                    return super.toDebugString(now) + ", getAuthTokenLabel, " + accountType + ", authTokenType " + authTokenType;
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws android.os.RemoteException {
                    this.mAuthenticator.getAuthTokenLabel(this, authTokenType);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void onResult(android.os.Bundle result) {
                    android.os.Bundle.setDefusable(result, true);
                    if (result != null) {
                        java.lang.String label = result.getString("authTokenLabelKey");
                        android.os.Bundle bundle = new android.os.Bundle();
                        bundle.putString("authTokenLabelKey", label);
                        super.onResult(bundle);
                        return;
                    }
                    super.onResult(result);
                }
            }.bind();
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void getAuthToken(android.accounts.IAccountManagerResponse response, final android.accounts.Account account, final java.lang.String authTokenType, final boolean notifyOnAuthFailure, boolean expectActivityLaunch, final android.os.Bundle loginOptions) throws java.lang.Throwable {
        final byte[] callerPkgSigDigest;
        int callerUid;
        com.android.server.accounts.AccountManagerService.UserAccounts accounts;
        int callerUid2;
        java.lang.String callerPkg;
        android.os.Bundle.setDefusable(loginOptions, true);
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "getAuthToken: " + account + ", response " + response + ", authTokenType " + authTokenType + ", notifyOnAuthFailure " + notifyOnAuthFailure + ", expectActivityLaunch " + expectActivityLaunch + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid());
        }
        com.android.internal.util.Preconditions.checkArgument(response != null, "response cannot be null");
        try {
            if (account == null) {
                android.util.Slog.w(TAG, "getAuthToken called with null account");
                response.onError(7, "account is null");
                return;
            }
            if (authTokenType == null) {
                android.util.Slog.w(TAG, "getAuthToken called with null authTokenType");
                response.onError(7, "authTokenType is null");
                return;
            }
            int userId = android.os.UserHandle.getCallingUserId();
            long ident2 = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.accounts.AccountManagerService.UserAccounts accounts2 = getUserAccounts(userId);
                android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription> authenticatorInfo = this.mAuthenticatorCache.getServiceInfo(android.accounts.AuthenticatorDescription.newKey(account.type), accounts2.userId);
                final boolean customTokens = authenticatorInfo != null && ((android.accounts.AuthenticatorDescription) authenticatorInfo.type).customTokens;
                int callerUid3 = android.os.Binder.getCallingUid();
                final boolean permissionGranted = customTokens || permissionIsGranted(account, authTokenType, callerUid3, userId);
                java.lang.String callerPkg2 = loginOptions.getString("androidPackageName");
                ident2 = android.os.Binder.clearCallingIdentity();
                try {
                    java.lang.String[] callerOwnedPackageNames = this.mPackageManager.getPackagesForUid(callerUid3);
                    if (callerPkg2 == null || callerOwnedPackageNames == null || !com.android.internal.util.ArrayUtils.contains(callerOwnedPackageNames, callerPkg2)) {
                        int callerUid4 = callerUid3;
                        java.lang.String callerPkg3 = callerPkg2;
                        java.lang.String msg = java.lang.String.format("Uid %s is attempting to illegally masquerade as package %s!", java.lang.Integer.valueOf(callerUid4), callerPkg3);
                        throw new java.lang.SecurityException(msg);
                    }
                    loginOptions.putInt("callerUid", callerUid3);
                    loginOptions.putInt("callerPid", android.os.Binder.getCallingPid());
                    if (notifyOnAuthFailure) {
                        loginOptions.putBoolean("notifyOnAuthFailure", true);
                    }
                    long identityToken = clearCallingIdentity();
                    try {
                        callerPkgSigDigest = calculatePackageSignatureDigest(callerPkg2, userId);
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                    if (!customTokens && permissionGranted) {
                        try {
                            java.lang.String authToken = readAuthTokenInternal(accounts2, account, authTokenType);
                            callerUid = callerUid3;
                            if (authToken != null) {
                                try {
                                    logGetAuthTokenMetrics(callerPkg2, account.type);
                                    android.os.Bundle result = new android.os.Bundle();
                                    result.putString("authtoken", authToken);
                                    result.putString("authAccount", account.name);
                                    result.putString("accountType", account.type);
                                    onResult(response, result);
                                    restoreCallingIdentity(identityToken);
                                    return;
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                }
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                        restoreCallingIdentity(identityToken);
                        throw th;
                    }
                    callerUid = callerUid3;
                    try {
                        if (customTokens) {
                            callerUid2 = callerUid;
                            accounts = accounts2;
                            try {
                                com.android.server.accounts.TokenCache.Value cachedToken = readCachedTokenInternal(accounts2, account, authTokenType, callerPkg2, callerPkgSigDigest);
                                if (cachedToken != null) {
                                    logGetAuthTokenMetrics(callerPkg2, account.type);
                                    if (android.util.Log.isLoggable(TAG, 2)) {
                                        android.util.Log.v(TAG, "getAuthToken: cache hit ofr custom token authenticator.");
                                    }
                                    android.os.Bundle result2 = new android.os.Bundle();
                                    result2.putString("authtoken", cachedToken.token);
                                    result2.putLong("android.accounts.expiry", cachedToken.expiryEpochMillis);
                                    result2.putString("authAccount", account.name);
                                    result2.putString("accountType", account.type);
                                    onResult(response, result2);
                                    restoreCallingIdentity(identityToken);
                                    return;
                                }
                                callerPkg = callerPkg2;
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                            }
                        } else {
                            accounts = accounts2;
                            callerUid2 = callerUid;
                            callerPkg = callerPkg2;
                        }
                        final java.lang.String str = callerPkg;
                        final int i = callerUid2;
                        final com.android.server.accounts.AccountManagerService.UserAccounts userAccounts = accounts;
                        new com.android.server.accounts.AccountManagerService.Session(accounts, response, account.type, expectActivityLaunch, false, account.name, false) { // from class: com.android.server.accounts.AccountManagerService.8
                            @Override // com.android.server.accounts.AccountManagerService.Session
                            protected java.lang.String toDebugString(long now) {
                                if (loginOptions != null) {
                                    loginOptions.keySet();
                                }
                                return super.toDebugString(now) + ", getAuthToken, " + account.toSafeString() + ", authTokenType " + authTokenType + ", loginOptions " + loginOptions + ", notifyOnAuthFailure " + notifyOnAuthFailure;
                            }

                            @Override // com.android.server.accounts.AccountManagerService.Session
                            public void run() throws android.os.RemoteException {
                                if (!permissionGranted) {
                                    this.mAuthenticator.getAuthTokenLabel(this, authTokenType);
                                } else {
                                    this.mAuthenticator.getAuthToken(this, account, authTokenType, loginOptions);
                                    com.android.server.accounts.AccountManagerService.this.logGetAuthTokenMetrics(str, account.type);
                                }
                            }

                            @Override // com.android.server.accounts.AccountManagerService.Session
                            public void onResult(android.os.Bundle result3) throws java.lang.Throwable {
                                android.os.Bundle.setDefusable(result3, true);
                                if (result3 != null) {
                                    if (result3.containsKey("authTokenLabelKey")) {
                                        android.content.Intent intent = com.android.server.accounts.AccountManagerService.this.newGrantCredentialsPermissionIntent(account, null, i, new android.accounts.AccountAuthenticatorResponse((android.accounts.IAccountAuthenticatorResponse) this), authTokenType, true);
                                        this.mCanStartAccountManagerActivity = true;
                                        android.os.Bundle bundle = new android.os.Bundle();
                                        bundle.putParcelable("intent", intent);
                                        onResult(bundle);
                                        return;
                                    }
                                    java.lang.String authToken2 = result3.getString("authtoken");
                                    if (authToken2 != null) {
                                        java.lang.String name = result3.getString("authAccount");
                                        java.lang.String type = result3.getString("accountType");
                                        if (android.text.TextUtils.isEmpty(type) || android.text.TextUtils.isEmpty(name)) {
                                            onError(5, "the type and name should not be empty");
                                            return;
                                        }
                                        if (!type.equals(this.mAccountType)) {
                                            onError(5, "incorrect account type");
                                            return;
                                        }
                                        android.accounts.Account resultAccount = new android.accounts.Account(name, type);
                                        if (!customTokens) {
                                            com.android.server.accounts.AccountManagerService.this.saveAuthTokenToDatabase(this.mAccounts, resultAccount, authTokenType, authToken2);
                                        }
                                        long expiryMillis = result3.getLong("android.accounts.expiry", 0L);
                                        if (customTokens && expiryMillis > java.lang.System.currentTimeMillis()) {
                                            com.android.server.accounts.AccountManagerService.this.saveCachedToken(this.mAccounts, account, str, callerPkgSigDigest, authTokenType, authToken2, expiryMillis);
                                        }
                                    }
                                    android.content.Intent intent2 = (android.content.Intent) result3.getParcelable("intent", android.content.Intent.class);
                                    if (intent2 != null && notifyOnAuthFailure && !customTokens) {
                                        if (!checkKeyIntent(android.os.Binder.getCallingUid(), result3)) {
                                            onError(5, "invalid intent in bundle returned");
                                            return;
                                        }
                                        com.android.server.accounts.AccountManagerService.this.doNotification(this.mAccounts, account, result3.getString("authFailedMessage"), intent2, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, userAccounts.userId);
                                    }
                                }
                                super.onResult(result3);
                            }
                        }.bind();
                        restoreCallingIdentity(identityToken);
                    } catch (java.lang.Throwable th5) {
                        th = th5;
                    }
                } finally {
                }
            } finally {
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to report error back to the client." + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logGetAuthTokenMetrics(java.lang.String callerPackage, java.lang.String accountType) {
        android.app.admin.DevicePolicyEventLogger.createEvent(204).setStrings(new java.lang.String[]{android.text.TextUtils.emptyIfNull(callerPackage), android.text.TextUtils.emptyIfNull(accountType)}).write();
    }

    private byte[] calculatePackageSignatureDigest(java.lang.String callerPkg, int userId) {
        java.security.MessageDigest digester;
        try {
            digester = java.security.MessageDigest.getInstance("SHA-256");
            android.content.pm.PackageInfo pkgInfo = this.mPackageManager.getPackageInfoAsUser(callerPkg, 64, userId);
            for (android.content.pm.Signature sig : pkgInfo.signatures) {
                digester.update(sig.toByteArray());
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.w(TAG, "Could not find packageinfo for: " + callerPkg);
            digester = null;
        } catch (java.security.NoSuchAlgorithmException x) {
            android.util.Log.wtf(TAG, "SHA-256 should be available", x);
            digester = null;
        }
        if (digester == null) {
            return null;
        }
        return digester.digest();
    }

    private void createNoCredentialsPermissionNotification(android.accounts.Account account, android.content.Intent intent, java.lang.String packageName, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        java.lang.String title;
        java.lang.String subtitle;
        int userId = accounts.userId;
        int uid = intent.getIntExtra("uid", -1);
        java.lang.String authTokenType = intent.getStringExtra("authTokenType");
        java.lang.String titleAndSubtitle = this.mContext.getString(android.R.string.permgroupdesc_microphone, getApplicationLabel(packageName, userId), account.name);
        int index = titleAndSubtitle.indexOf(10);
        if (index <= 0) {
            title = titleAndSubtitle;
            subtitle = "";
        } else {
            java.lang.String title2 = titleAndSubtitle.substring(0, index);
            java.lang.String subtitle2 = titleAndSubtitle.substring(index + 1);
            title = title2;
            subtitle = subtitle2;
        }
        android.os.UserHandle user = android.os.UserHandle.of(userId);
        android.content.Context contextForUser = getContextForUser(user);
        android.app.Notification n = new android.app.Notification.Builder(contextForUser, com.android.internal.notification.SystemNotificationChannels.ACCOUNT).setSmallIcon(android.R.drawable.stat_sys_warning).setWhen(0L).setColor(contextForUser.getColor(android.R.color.system_notification_accent_color)).setContentTitle(title).setContentText(subtitle).setContentIntent(android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF, null, user)).build();
        installNotification(getCredentialPermissionNotificationId(account, authTokenType, uid, accounts), n, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, user.getIdentifier());
    }

    private java.lang.String getApplicationLabel(java.lang.String packageName, int userId) {
        try {
            return this.mPackageManager.getApplicationLabel(this.mPackageManager.getApplicationInfoAsUser(packageName, 0, userId)).toString();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.Intent newGrantCredentialsPermissionIntent(android.accounts.Account account, java.lang.String packageName, int uid, android.accounts.AccountAuthenticatorResponse response, java.lang.String authTokenType, boolean startInNewTask) {
        android.content.Intent intent = new android.content.Intent(this.mContext, (java.lang.Class<?>) android.accounts.GrantCredentialsPermissionActivity.class);
        if (startInNewTask) {
            intent.setFlags(268435456);
        }
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(android.os.UserHandle.getUserId(uid));
        intent.addCategory(getCredentialPermissionNotificationId(account, authTokenType, uid, accounts).mTag + (packageName != null ? packageName : ""));
        intent.putExtra("account", account);
        intent.putExtra("authTokenType", authTokenType);
        intent.putExtra("response", response);
        intent.putExtra("uid", uid);
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.accounts.AccountManagerService.NotificationId getCredentialPermissionNotificationId(android.accounts.Account account, java.lang.String authTokenType, int uid, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        com.android.server.accounts.AccountManagerService.NotificationId nId;
        synchronized (accounts.credentialsPermissionNotificationIds) {
            android.util.Pair<android.util.Pair<android.accounts.Account, java.lang.String>, java.lang.Integer> key = new android.util.Pair<>(new android.util.Pair(account, authTokenType), java.lang.Integer.valueOf(uid));
            nId = (com.android.server.accounts.AccountManagerService.NotificationId) accounts.credentialsPermissionNotificationIds.get(key);
            if (nId == null) {
                java.lang.String tag = "AccountManagerService:38:" + account.hashCode() + ":" + authTokenType.hashCode() + ":" + uid;
                nId = new com.android.server.accounts.AccountManagerService.NotificationId(tag, 38);
                accounts.credentialsPermissionNotificationIds.put(key, nId);
            }
        }
        return nId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.accounts.AccountManagerService.NotificationId getSigninRequiredNotificationId(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account) {
        com.android.server.accounts.AccountManagerService.NotificationId nId;
        synchronized (accounts.signinRequiredNotificationIds) {
            nId = (com.android.server.accounts.AccountManagerService.NotificationId) accounts.signinRequiredNotificationIds.get(account);
            if (nId == null) {
                java.lang.String tag = "AccountManagerService:37:" + account.hashCode();
                nId = new com.android.server.accounts.AccountManagerService.NotificationId(tag, 37);
                accounts.signinRequiredNotificationIds.put(account, nId);
            }
        }
        return nId;
    }

    public void addAccount(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle optionsIn) throws java.lang.Throwable {
        android.os.Bundle.setDefusable(optionsIn, true);
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "addAccount: accountType " + accountType + ", response " + response + ", authTokenType " + authTokenType + ", requiredFeatures " + java.util.Arrays.toString(requiredFeatures) + ", expectActivityLaunch " + expectActivityLaunch + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid());
        }
        if (response == null) {
            throw new java.lang.IllegalArgumentException("response is null");
        }
        if (accountType == null) {
            throw new java.lang.IllegalArgumentException("accountType is null");
        }
        int uid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(uid);
        if (!canUserModifyAccounts(userId, uid)) {
            try {
                response.onError(100, "User is not allowed to add an account!");
            } catch (android.os.RemoteException e) {
            }
            showCantAddAccount(100, userId);
        } else if (!canUserModifyAccountsForType(userId, accountType, uid)) {
            try {
                response.onError(101, "User cannot modify accounts of this type (policy).");
            } catch (android.os.RemoteException e2) {
            }
            showCantAddAccount(101, userId);
        } else {
            addAccountAndLogMetrics(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, optionsIn, userId);
        }
    }

    public void addAccountAsUser(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle optionsIn, int userId) throws java.lang.Throwable {
        boolean z;
        boolean z2 = true;
        android.os.Bundle.setDefusable(optionsIn, true);
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "addAccount: accountType " + accountType + ", response " + response + ", authTokenType " + authTokenType + ", requiredFeatures " + java.util.Arrays.toString(requiredFeatures) + ", expectActivityLaunch " + expectActivityLaunch + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid() + ", for user id " + userId);
        }
        if (response != null) {
            z = true;
        } else {
            z = false;
        }
        com.android.internal.util.Preconditions.checkArgument(z, "response cannot be null");
        if (accountType == null) {
            z2 = false;
        }
        com.android.internal.util.Preconditions.checkArgument(z2, "accountType cannot be null");
        if (isCrossUser(callingUid, userId)) {
            throw new java.lang.SecurityException(java.lang.String.format("User %s trying to add account for %s", java.lang.Integer.valueOf(android.os.UserHandle.getCallingUserId()), java.lang.Integer.valueOf(userId)));
        }
        if (!canUserModifyAccounts(userId, callingUid)) {
            try {
                response.onError(100, "User is not allowed to add an account!");
            } catch (android.os.RemoteException e) {
            }
            showCantAddAccount(100, userId);
        } else if (!canUserModifyAccountsForType(userId, accountType, callingUid)) {
            try {
                response.onError(101, "User cannot modify accounts of this type (policy).");
            } catch (android.os.RemoteException e2) {
            }
            showCantAddAccount(101, userId);
        } else {
            addAccountAndLogMetrics(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, optionsIn, userId);
        }
    }

    private void addAccountAndLogMetrics(android.accounts.IAccountManagerResponse response, final java.lang.String accountType, final java.lang.String authTokenType, final java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle optionsIn, int userId) throws java.lang.Throwable {
        int pid = android.os.Binder.getCallingPid();
        int uid = android.os.Binder.getCallingUid();
        final android.os.Bundle options = optionsIn == null ? new android.os.Bundle() : optionsIn;
        options.putInt("callerUid", uid);
        options.putInt("callerPid", pid);
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            logRecordWithUid(accounts, com.android.server.accounts.AccountsDb.DEBUG_ACTION_CALLED_ACCOUNT_ADD, "accounts", uid);
            try {
                try {
                    new com.android.server.accounts.AccountManagerService.Session(accounts, response, accountType, expectActivityLaunch, true, null, false, true) { // from class: com.android.server.accounts.AccountManagerService.9
                        @Override // com.android.server.accounts.AccountManagerService.Session
                        public void run() throws android.os.RemoteException {
                            this.mAuthenticator.addAccount(this, this.mAccountType, authTokenType, requiredFeatures, options);
                            java.lang.String callerPackage = options.getString("androidPackageName");
                            com.android.server.accounts.AccountManagerService.this.logAddAccountMetrics(callerPackage, accountType, requiredFeatures, authTokenType);
                        }

                        @Override // com.android.server.accounts.AccountManagerService.Session
                        protected java.lang.String toDebugString(long now) {
                            java.lang.String strJoin;
                            java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append(super.toDebugString(now)).append(", addAccount, accountType ").append(accountType).append(", requiredFeatures ");
                            if (requiredFeatures != null) {
                                strJoin = android.text.TextUtils.join(",", requiredFeatures);
                            } else {
                                strJoin = null;
                            }
                            return sbAppend.append(strJoin).toString();
                        }
                    }.bind(uid, pid);
                    restoreCallingIdentity(identityToken);
                } catch (java.lang.Throwable th) {
                    th = th;
                    restoreCallingIdentity(identityToken);
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAddAccountMetrics(java.lang.String callerPackage, java.lang.String accountType, java.lang.String[] requiredFeatures, java.lang.String authTokenType) {
        java.lang.String strJoin;
        android.app.admin.DevicePolicyEventLogger devicePolicyEventLoggerCreateEvent = android.app.admin.DevicePolicyEventLogger.createEvent(202);
        java.lang.String strEmptyIfNull = android.text.TextUtils.emptyIfNull(accountType);
        java.lang.String strEmptyIfNull2 = android.text.TextUtils.emptyIfNull(callerPackage);
        java.lang.String strEmptyIfNull3 = android.text.TextUtils.emptyIfNull(authTokenType);
        if (requiredFeatures == null) {
            strJoin = "";
        } else {
            strJoin = android.text.TextUtils.join(";", requiredFeatures);
        }
        devicePolicyEventLoggerCreateEvent.setStrings(new java.lang.String[]{strEmptyIfNull, strEmptyIfNull2, strEmptyIfNull3, strJoin}).write();
    }

    public void startAddAccountSession(android.accounts.IAccountManagerResponse response, final java.lang.String accountType, final java.lang.String authTokenType, final java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle optionsIn) throws java.lang.Throwable {
        android.os.Bundle.setDefusable(optionsIn, true);
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "startAddAccountSession: accountType " + accountType + ", response " + response + ", authTokenType " + authTokenType + ", requiredFeatures " + java.util.Arrays.toString(requiredFeatures) + ", expectActivityLaunch " + expectActivityLaunch + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid());
        }
        com.android.internal.util.Preconditions.checkArgument(response != null, "response cannot be null");
        com.android.internal.util.Preconditions.checkArgument(accountType != null, "accountType cannot be null");
        int uid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(uid);
        if (!canUserModifyAccounts(userId, uid)) {
            try {
                response.onError(100, "User is not allowed to add an account!");
            } catch (android.os.RemoteException e) {
            }
            showCantAddAccount(100, userId);
            return;
        }
        if (!canUserModifyAccountsForType(userId, accountType, uid)) {
            try {
                response.onError(101, "User cannot modify accounts of this type (policy).");
            } catch (android.os.RemoteException e2) {
            }
            showCantAddAccount(101, userId);
            return;
        }
        int pid = android.os.Binder.getCallingPid();
        final android.os.Bundle options = optionsIn == null ? new android.os.Bundle() : optionsIn;
        options.putInt("callerUid", uid);
        options.putInt("callerPid", pid);
        final java.lang.String callerPkg = options.getString("androidPackageName");
        boolean isPasswordForwardingAllowed = checkPermissionAndNote(callerPkg, uid, "android.permission.GET_PASSWORD");
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            logRecordWithUid(accounts, com.android.server.accounts.AccountsDb.DEBUG_ACTION_CALLED_START_ACCOUNT_ADD, "accounts", uid);
            try {
                try {
                    new com.android.server.accounts.AccountManagerService.StartAccountSession(accounts, response, accountType, expectActivityLaunch, null, false, true, isPasswordForwardingAllowed) { // from class: com.android.server.accounts.AccountManagerService.10
                        @Override // com.android.server.accounts.AccountManagerService.Session
                        public void run() throws android.os.RemoteException {
                            this.mAuthenticator.startAddAccountSession(this, this.mAccountType, authTokenType, requiredFeatures, options);
                            com.android.server.accounts.AccountManagerService.this.logAddAccountMetrics(callerPkg, accountType, requiredFeatures, authTokenType);
                        }

                        @Override // com.android.server.accounts.AccountManagerService.Session
                        protected java.lang.String toDebugString(long now) {
                            return super.toDebugString(now) + ", startAddAccountSession, accountType " + accountType + ", requiredFeatures " + (requiredFeatures != null ? android.text.TextUtils.join(",", requiredFeatures) : "null");
                        }
                    }.bind(uid, pid);
                    restoreCallingIdentity(identityToken);
                } catch (java.lang.Throwable th) {
                    th = th;
                    restoreCallingIdentity(identityToken);
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
    }

    private abstract class StartAccountSession extends com.android.server.accounts.AccountManagerService.Session {
        private final boolean mIsPasswordForwardingAllowed;

        public StartAccountSession(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.IAccountManagerResponse response, java.lang.String accountType, boolean expectActivityLaunch, java.lang.String accountName, boolean authDetailsRequired, boolean updateLastAuthenticationTime, boolean isPasswordForwardingAllowed) {
            super(accounts, response, accountType, expectActivityLaunch, true, accountName, authDetailsRequired, updateLastAuthenticationTime);
            this.mIsPasswordForwardingAllowed = isPasswordForwardingAllowed;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void onResult(android.os.Bundle result) {
            android.accounts.IAccountManagerResponse response;
            android.os.Bundle.setDefusable(result, true);
            this.mNumResults++;
            if (result != null && !checkKeyIntent(android.os.Binder.getCallingUid(), result)) {
                onError(5, "invalid intent in bundle returned");
                return;
            }
            if (this.mExpectActivityLaunch && result != null && result.containsKey("intent")) {
                response = this.mResponse;
            } else {
                response = getResponseAndClose();
            }
            if (response == null) {
                return;
            }
            if (result == null) {
                if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                    android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, getClass().getSimpleName() + " calling onError() on response " + response);
                }
                com.android.server.accounts.AccountManagerService.this.sendErrorResponse(response, 5, "null bundle returned");
                return;
            }
            if (result.getInt("errorCode", -1) > 0 && 0 == 0) {
                com.android.server.accounts.AccountManagerService.this.sendErrorResponse(response, result.getInt("errorCode"), result.getString("errorMessage"));
                return;
            }
            if (!this.mIsPasswordForwardingAllowed) {
                result.remove(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PASSWORD);
            }
            result.remove("authtoken");
            if (!checkKeyIntent(android.os.Binder.getCallingUid(), result)) {
                onError(5, "invalid intent in bundle returned");
                return;
            }
            if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + response);
            }
            android.os.Bundle sessionBundle = result.getBundle("accountSessionBundle");
            if (sessionBundle != null) {
                java.lang.String accountType = sessionBundle.getString("accountType");
                if (android.text.TextUtils.isEmpty(accountType) || !this.mAccountType.equalsIgnoreCase(accountType)) {
                    android.util.Log.w(com.android.server.accounts.AccountManagerService.TAG, "Account type in session bundle doesn't match request.");
                }
                sessionBundle.putString("accountType", this.mAccountType);
                try {
                    com.android.server.accounts.CryptoHelper cryptoHelper = com.android.server.accounts.CryptoHelper.getInstance();
                    android.os.Bundle encryptedBundle = cryptoHelper.encryptBundle(sessionBundle);
                    result.putBundle("accountSessionBundle", encryptedBundle);
                } catch (java.security.GeneralSecurityException e) {
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 3)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "Failed to encrypt session bundle!", e);
                    }
                    com.android.server.accounts.AccountManagerService.this.sendErrorResponse(response, 5, "failed to encrypt session bundle");
                    return;
                }
            }
            com.android.server.accounts.AccountManagerService.this.sendResponse(response, result);
        }
    }

    public void finishSessionAsUser(android.accounts.IAccountManagerResponse response, android.os.Bundle sessionBundle, boolean expectActivityLaunch, android.os.Bundle appInfo, int userId) throws java.lang.Throwable {
        com.android.server.accounts.AccountManagerService.UserAccounts accounts;
        android.os.Bundle.setDefusable(sessionBundle, true);
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "finishSession: response " + response + ", expectActivityLaunch " + expectActivityLaunch + ", caller's uid " + callingUid + ", caller's user id " + android.os.UserHandle.getCallingUserId() + ", pid " + android.os.Binder.getCallingPid() + ", for user id " + userId);
        }
        com.android.internal.util.Preconditions.checkArgument(response != null, "response cannot be null");
        if (sessionBundle == null || sessionBundle.size() == 0) {
            throw new java.lang.IllegalArgumentException("sessionBundle is empty");
        }
        if (isCrossUser(callingUid, userId)) {
            throw new java.lang.SecurityException(java.lang.String.format("User %s trying to finish session for %s without cross user permission", java.lang.Integer.valueOf(android.os.UserHandle.getCallingUserId()), java.lang.Integer.valueOf(userId)));
        }
        if (!canUserModifyAccounts(userId, callingUid)) {
            sendErrorResponse(response, 100, "User is not allowed to add an account!");
            showCantAddAccount(100, userId);
            return;
        }
        int pid = android.os.Binder.getCallingPid();
        try {
            com.android.server.accounts.CryptoHelper cryptoHelper = com.android.server.accounts.CryptoHelper.getInstance();
            final android.os.Bundle decryptedBundle = cryptoHelper.decryptBundle(sessionBundle);
            try {
                if (decryptedBundle == null) {
                    sendErrorResponse(response, 8, "failed to decrypt session bundle");
                    return;
                }
                final java.lang.String accountType = decryptedBundle.getString("accountType");
                if (android.text.TextUtils.isEmpty(accountType)) {
                    sendErrorResponse(response, 7, "accountType is empty");
                    return;
                }
                if (appInfo != null) {
                    decryptedBundle.putAll(appInfo);
                }
                decryptedBundle.putInt("callerUid", callingUid);
                decryptedBundle.putInt("callerPid", pid);
                if (!canUserModifyAccountsForType(userId, accountType, callingUid)) {
                    sendErrorResponse(response, 101, "User cannot modify accounts of this type (policy).");
                    showCantAddAccount(101, userId);
                    return;
                }
                long identityToken = clearCallingIdentity();
                try {
                    accounts = getUserAccounts(userId);
                    logRecordWithUid(accounts, com.android.server.accounts.AccountsDb.DEBUG_ACTION_CALLED_ACCOUNT_SESSION_FINISH, "accounts", callingUid);
                } catch (java.lang.Throwable th) {
                    th = th;
                }
                try {
                    new com.android.server.accounts.AccountManagerService.Session(accounts, response, accountType, expectActivityLaunch, true, null, false, true) { // from class: com.android.server.accounts.AccountManagerService.11
                        @Override // com.android.server.accounts.AccountManagerService.Session
                        public void run() throws android.os.RemoteException {
                            this.mAuthenticator.finishSession(this, this.mAccountType, decryptedBundle);
                        }

                        @Override // com.android.server.accounts.AccountManagerService.Session
                        protected java.lang.String toDebugString(long now) {
                            return super.toDebugString(now) + ", finishSession, accountType " + accountType;
                        }
                    }.bind();
                    restoreCallingIdentity(identityToken);
                    return;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    restoreCallingIdentity(identityToken);
                    throw th;
                }
            } catch (java.security.GeneralSecurityException e) {
                e = e;
            }
        } catch (java.security.GeneralSecurityException e2) {
            e = e2;
        }
        if (android.util.Log.isLoggable(TAG, 3)) {
            android.util.Log.v(TAG, "Failed to decrypt session bundle!", e);
        }
        sendErrorResponse(response, 8, "failed to decrypt session bundle");
    }

    private void showCantAddAccount(int errorCode, int userId) {
        android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        android.content.Intent intent = null;
        if (dpmi == null) {
            intent = getDefaultCantAddAccountIntent(errorCode);
        } else if (errorCode == 100) {
            intent = dpmi.createUserRestrictionSupportIntent(userId, "no_modify_accounts");
        } else if (errorCode == 101) {
            intent = dpmi.createShowAdminSupportIntent(userId, false);
        }
        if (intent == null) {
            intent = getDefaultCantAddAccountIntent(errorCode);
        }
        long identityToken = clearCallingIdentity();
        try {
            this.mContext.startActivityAsUser(intent, new android.os.UserHandle(userId));
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private android.content.Intent getDefaultCantAddAccountIntent(int errorCode) {
        android.content.Intent cantAddAccount = new android.content.Intent(this.mContext, (java.lang.Class<?>) android.accounts.CantAddAccountActivity.class);
        cantAddAccount.putExtra("android.accounts.extra.ERROR_CODE", errorCode);
        cantAddAccount.addFlags(268435456);
        return cantAddAccount;
    }

    public void confirmCredentialsAsUser(android.accounts.IAccountManagerResponse response, final android.accounts.Account account, final android.os.Bundle options, boolean expectActivityLaunch, int userId) throws java.lang.Throwable {
        android.os.Bundle.setDefusable(options, true);
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "confirmCredentials: " + account + ", response " + response + ", expectActivityLaunch " + expectActivityLaunch + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        if (isCrossUser(callingUid, userId)) {
            throw new java.lang.SecurityException(java.lang.String.format("User %s trying to confirm account credentials for %s", java.lang.Integer.valueOf(android.os.UserHandle.getCallingUserId()), java.lang.Integer.valueOf(userId)));
        }
        if (response == null) {
            throw new java.lang.IllegalArgumentException("response is null");
        }
        if (account == null) {
            throw new java.lang.IllegalArgumentException("account is null");
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            try {
                new com.android.server.accounts.AccountManagerService.Session(accounts, response, account.type, expectActivityLaunch, true, account.name, true, true) { // from class: com.android.server.accounts.AccountManagerService.12
                    @Override // com.android.server.accounts.AccountManagerService.Session
                    public void run() throws android.os.RemoteException {
                        this.mAuthenticator.confirmCredentials(this, account, options);
                    }

                    @Override // com.android.server.accounts.AccountManagerService.Session
                    protected java.lang.String toDebugString(long now) {
                        return super.toDebugString(now) + ", confirmCredentials, " + account.toSafeString();
                    }
                }.bind();
                restoreCallingIdentity(identityToken);
            } catch (java.lang.Throwable th) {
                th = th;
                restoreCallingIdentity(identityToken);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    public void updateCredentials(android.accounts.IAccountManagerResponse response, final android.accounts.Account account, final java.lang.String authTokenType, boolean expectActivityLaunch, final android.os.Bundle loginOptions) throws java.lang.Throwable {
        android.os.Bundle.setDefusable(loginOptions, true);
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "updateCredentials: " + account + ", response " + response + ", authTokenType " + authTokenType + ", expectActivityLaunch " + expectActivityLaunch + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid());
        }
        if (response == null) {
            throw new java.lang.IllegalArgumentException("response is null");
        }
        if (account == null) {
            throw new java.lang.IllegalArgumentException("account is null");
        }
        int userId = android.os.UserHandle.getCallingUserId();
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            try {
                new com.android.server.accounts.AccountManagerService.Session(accounts, response, account.type, expectActivityLaunch, true, account.name, false, true) { // from class: com.android.server.accounts.AccountManagerService.13
                    @Override // com.android.server.accounts.AccountManagerService.Session
                    public void run() throws android.os.RemoteException {
                        this.mAuthenticator.updateCredentials(this, account, authTokenType, loginOptions);
                    }

                    @Override // com.android.server.accounts.AccountManagerService.Session
                    protected java.lang.String toDebugString(long now) {
                        if (loginOptions != null) {
                            loginOptions.keySet();
                        }
                        return super.toDebugString(now) + ", updateCredentials, " + account.toSafeString() + ", authTokenType " + authTokenType + ", loginOptions " + loginOptions;
                    }
                }.bind();
                restoreCallingIdentity(identityToken);
            } catch (java.lang.Throwable th) {
                th = th;
                restoreCallingIdentity(identityToken);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    public void startUpdateCredentialsSession(android.accounts.IAccountManagerResponse response, final android.accounts.Account account, final java.lang.String authTokenType, boolean expectActivityLaunch, final android.os.Bundle loginOptions) throws java.lang.Throwable {
        android.os.Bundle.setDefusable(loginOptions, true);
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "startUpdateCredentialsSession: " + account + ", response " + response + ", authTokenType " + authTokenType + ", expectActivityLaunch " + expectActivityLaunch + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid());
        }
        if (response == null) {
            throw new java.lang.IllegalArgumentException("response is null");
        }
        if (account == null) {
            throw new java.lang.IllegalArgumentException("account is null");
        }
        int uid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        java.lang.String callerPkg = loginOptions.getString("androidPackageName");
        boolean isPasswordForwardingAllowed = checkPermissionAndNote(callerPkg, uid, "android.permission.GET_PASSWORD");
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            try {
                new com.android.server.accounts.AccountManagerService.StartAccountSession(accounts, response, account.type, expectActivityLaunch, account.name, false, true, isPasswordForwardingAllowed) { // from class: com.android.server.accounts.AccountManagerService.14
                    @Override // com.android.server.accounts.AccountManagerService.Session
                    public void run() throws android.os.RemoteException {
                        this.mAuthenticator.startUpdateCredentialsSession(this, account, authTokenType, loginOptions);
                    }

                    @Override // com.android.server.accounts.AccountManagerService.Session
                    protected java.lang.String toDebugString(long now) {
                        if (loginOptions != null) {
                            loginOptions.keySet();
                        }
                        return super.toDebugString(now) + ", startUpdateCredentialsSession, " + account.toSafeString() + ", authTokenType " + authTokenType + ", loginOptions " + loginOptions;
                    }
                }.bind();
                restoreCallingIdentity(identityToken);
            } catch (java.lang.Throwable th) {
                th = th;
                restoreCallingIdentity(identityToken);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    public void isCredentialsUpdateSuggested(android.accounts.IAccountManagerResponse response, final android.accounts.Account account, final java.lang.String statusToken) {
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "isCredentialsUpdateSuggested: " + account + ", response " + response + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid());
        }
        if (response == null) {
            throw new java.lang.IllegalArgumentException("response is null");
        }
        if (account == null) {
            throw new java.lang.IllegalArgumentException("account is null");
        }
        if (android.text.TextUtils.isEmpty(statusToken)) {
            throw new java.lang.IllegalArgumentException("status token is empty");
        }
        int usrId = android.os.UserHandle.getCallingUserId();
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(usrId);
            new com.android.server.accounts.AccountManagerService.Session(accounts, response, account.type, false, false, account.name, false) { // from class: com.android.server.accounts.AccountManagerService.15
                @Override // com.android.server.accounts.AccountManagerService.Session
                protected java.lang.String toDebugString(long now) {
                    return super.toDebugString(now) + ", isCredentialsUpdateSuggested, " + account.toSafeString();
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws android.os.RemoteException {
                    this.mAuthenticator.isCredentialsUpdateSuggested(this, account, statusToken);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void onResult(android.os.Bundle result) {
                    android.os.Bundle.setDefusable(result, true);
                    android.accounts.IAccountManagerResponse response2 = getResponseAndClose();
                    if (response2 == null) {
                        return;
                    }
                    if (result == null) {
                        com.android.server.accounts.AccountManagerService.this.sendErrorResponse(response2, 5, "null bundle");
                        return;
                    }
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + response2);
                    }
                    if (result.getInt("errorCode", -1) > 0) {
                        com.android.server.accounts.AccountManagerService.this.sendErrorResponse(response2, result.getInt("errorCode"), result.getString("errorMessage"));
                    } else {
                        if (!result.containsKey("booleanResult")) {
                            com.android.server.accounts.AccountManagerService.this.sendErrorResponse(response2, 5, "no result in response");
                            return;
                        }
                        android.os.Bundle newResult = new android.os.Bundle();
                        newResult.putBoolean("booleanResult", result.getBoolean("booleanResult", false));
                        com.android.server.accounts.AccountManagerService.this.sendResponse(response2, newResult);
                    }
                }
            }.bind();
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void editProperties(android.accounts.IAccountManagerResponse response, final java.lang.String accountType, boolean expectActivityLaunch) throws java.lang.Throwable {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "editProperties: accountType " + accountType + ", response " + response + ", expectActivityLaunch " + expectActivityLaunch + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        if (response == null) {
            throw new java.lang.IllegalArgumentException("response is null");
        }
        if (accountType == null) {
            throw new java.lang.IllegalArgumentException("accountType is null");
        }
        int userId = android.os.UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(accountType, callingUid, userId) && !isSystemUid(callingUid)) {
            java.lang.String msg = java.lang.String.format("uid %s cannot edit authenticator properites for account type: %s", java.lang.Integer.valueOf(callingUid), accountType);
            throw new java.lang.SecurityException(msg);
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            try {
                new com.android.server.accounts.AccountManagerService.Session(accounts, response, accountType, expectActivityLaunch, true, null, false) { // from class: com.android.server.accounts.AccountManagerService.16
                    @Override // com.android.server.accounts.AccountManagerService.Session
                    public void run() throws android.os.RemoteException {
                        this.mAuthenticator.editProperties(this, this.mAccountType);
                    }

                    @Override // com.android.server.accounts.AccountManagerService.Session
                    protected java.lang.String toDebugString(long now) {
                        return super.toDebugString(now) + ", editProperties, accountType " + accountType;
                    }
                }.bind();
                restoreCallingIdentity(identityToken);
            } catch (java.lang.Throwable th) {
                th = th;
                restoreCallingIdentity(identityToken);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    public boolean hasAccountAccess(android.accounts.Account account, java.lang.String packageName, android.os.UserHandle userHandle) {
        if (android.os.UserHandle.getAppId(android.os.Binder.getCallingUid()) != 1000) {
            throw new java.lang.SecurityException("Can be called only by system UID");
        }
        java.util.Objects.requireNonNull(account, "account cannot be null");
        java.util.Objects.requireNonNull(packageName, "packageName cannot be null");
        java.util.Objects.requireNonNull(userHandle, "userHandle cannot be null");
        int userId = userHandle.getIdentifier();
        com.android.internal.util.Preconditions.checkArgumentInRange(userId, 0, Integer.MAX_VALUE, "user must be concrete");
        try {
            int uid = this.mPackageManager.getPackageUidAsUser(packageName, userId);
            return hasAccountAccess(account, packageName, uid);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.w(TAG, "hasAccountAccess#Package not found " + e.getMessage());
            return false;
        }
    }

    private java.lang.String getPackageNameForUid(int uid) {
        int version;
        java.lang.String[] packageNames = this.mPackageManager.getPackagesForUid(uid);
        if (com.android.internal.util.ArrayUtils.isEmpty(packageNames)) {
            return null;
        }
        java.lang.String packageName = packageNames[0];
        if (packageNames.length == 1) {
            return packageName;
        }
        int oldestVersion = Integer.MAX_VALUE;
        for (java.lang.String name : packageNames) {
            try {
                android.content.pm.ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(name, 0);
                if (applicationInfo != null && (version = applicationInfo.targetSdkVersion) < oldestVersion) {
                    oldestVersion = version;
                    packageName = name;
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
        return packageName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasAccountAccess(android.accounts.Account account, java.lang.String packageName, int uid) {
        if (packageName == null && (packageName = getPackageNameForUid(uid)) == null) {
            return false;
        }
        if (permissionIsGranted(account, null, uid, android.os.UserHandle.getUserId(uid))) {
            return true;
        }
        int visibility = resolveAccountVisibility(account, packageName, getUserAccounts(android.os.UserHandle.getUserId(uid))).intValue();
        return visibility == 1 || visibility == 2;
    }

    public android.content.IntentSender createRequestAccountAccessIntentSenderAsUser(android.accounts.Account account, java.lang.String packageName, android.os.UserHandle userHandle) {
        if (android.os.UserHandle.getAppId(android.os.Binder.getCallingUid()) != 1000) {
            throw new java.lang.SecurityException("Can be called only by system UID");
        }
        java.util.Objects.requireNonNull(account, "account cannot be null");
        java.util.Objects.requireNonNull(packageName, "packageName cannot be null");
        java.util.Objects.requireNonNull(userHandle, "userHandle cannot be null");
        int userId = userHandle.getIdentifier();
        com.android.internal.util.Preconditions.checkArgumentInRange(userId, 0, Integer.MAX_VALUE, "user must be concrete");
        try {
            int uid = this.mPackageManager.getPackageUidAsUser(packageName, userId);
            android.content.Intent intent = newRequestAccountAccessIntent(account, packageName, uid, null);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, 1409286144, null, new android.os.UserHandle(userId)).getIntentSender();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Unknown package " + packageName);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.Intent newRequestAccountAccessIntent(final android.accounts.Account account, java.lang.String packageName, final int uid, final android.os.RemoteCallback callback) {
        return newGrantCredentialsPermissionIntent(account, packageName, uid, new android.accounts.AccountAuthenticatorResponse((android.accounts.IAccountAuthenticatorResponse) new android.accounts.IAccountAuthenticatorResponse.Stub() { // from class: com.android.server.accounts.AccountManagerService.17
            public void onResult(android.os.Bundle value) throws android.os.RemoteException {
                handleAuthenticatorResponse(true);
            }

            public void onRequestContinued() {
            }

            public void onError(int errorCode, java.lang.String errorMessage) throws android.os.RemoteException {
                handleAuthenticatorResponse(false);
            }

            private void handleAuthenticatorResponse(boolean accessGranted) throws android.os.RemoteException {
                com.android.server.accounts.AccountManagerService.UserAccounts userAccounts = com.android.server.accounts.AccountManagerService.this.getUserAccounts(android.os.UserHandle.getUserId(uid));
                com.android.server.accounts.AccountManagerService.this.cancelNotification(com.android.server.accounts.AccountManagerService.this.getCredentialPermissionNotificationId(account, "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", uid, userAccounts), userAccounts);
                if (callback != null) {
                    android.os.Bundle result = new android.os.Bundle();
                    result.putBoolean("booleanResult", accessGranted);
                    callback.sendResult(result);
                }
            }
        }), "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", false);
    }

    public boolean someUserHasAccount(android.accounts.Account account) {
        if (!android.os.UserHandle.isSameApp(1000, android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("Only system can check for accounts across users");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.accounts.AccountAndUser[] allAccounts = getAllAccountsForSystemProcess();
            for (int i = allAccounts.length - 1; i >= 0; i--) {
                if (allAccounts[i].account.equals(account)) {
                    return true;
                }
            }
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private class GetAccountsByTypeAndFeatureSession extends com.android.server.accounts.AccountManagerService.Session {
        private volatile android.accounts.Account[] mAccountsOfType;
        private volatile java.util.ArrayList<android.accounts.Account> mAccountsWithFeatures;
        private final int mCallingUid;
        private volatile int mCurrentAccount;
        private final java.lang.String[] mFeatures;
        private final boolean mIncludeManagedNotVisible;
        private final java.lang.String mPackageName;

        public GetAccountsByTypeAndFeatureSession(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.IAccountManagerResponse response, java.lang.String type, java.lang.String[] features, int callingUid, java.lang.String packageName, boolean includeManagedNotVisible) {
            super(com.android.server.accounts.AccountManagerService.this, accounts, response, type, false, true, null, false);
            this.mAccountsOfType = null;
            this.mAccountsWithFeatures = null;
            this.mCurrentAccount = 0;
            this.mCallingUid = callingUid;
            this.mFeatures = features;
            this.mPackageName = packageName;
            this.mIncludeManagedNotVisible = includeManagedNotVisible;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void run() throws android.os.RemoteException {
            this.mAccountsOfType = com.android.server.accounts.AccountManagerService.this.getAccountsFromCache(this.mAccounts, this.mAccountType, this.mCallingUid, this.mPackageName, this.mIncludeManagedNotVisible);
            this.mAccountsWithFeatures = new java.util.ArrayList<>(this.mAccountsOfType.length);
            this.mCurrentAccount = 0;
            checkAccount();
        }

        public void checkAccount() {
            if (this.mCurrentAccount >= this.mAccountsOfType.length) {
                sendResult();
                return;
            }
            android.accounts.IAccountAuthenticator accountAuthenticator = this.mAuthenticator;
            if (accountAuthenticator == null) {
                if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                    android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "checkAccount: aborting session since we are no longer connected to the authenticator, " + toDebugString());
                }
            } else {
                try {
                    accountAuthenticator.hasFeatures(this, this.mAccountsOfType[this.mCurrentAccount], this.mFeatures);
                } catch (android.os.RemoteException e) {
                    onError(1, "remote exception");
                }
            }
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void onResult(android.os.Bundle result) {
            android.os.Bundle.setDefusable(result, true);
            this.mNumResults++;
            if (result == null) {
                onError(5, "null bundle");
                return;
            }
            if (result.getBoolean("booleanResult", false)) {
                this.mAccountsWithFeatures.add(this.mAccountsOfType[this.mCurrentAccount]);
            }
            this.mCurrentAccount++;
            checkAccount();
        }

        public void sendResult() {
            android.accounts.IAccountManagerResponse response = getResponseAndClose();
            if (response != null) {
                try {
                    android.accounts.Account[] accounts = new android.accounts.Account[this.mAccountsWithFeatures.size()];
                    for (int i = 0; i < accounts.length; i++) {
                        accounts[i] = this.mAccountsWithFeatures.get(i);
                    }
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + response);
                    }
                    android.os.Bundle result = new android.os.Bundle();
                    result.putParcelableArray("accounts", accounts);
                    response.onResult(result);
                } catch (android.os.RemoteException e) {
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "failure while notifying response", e);
                    }
                }
            }
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        protected java.lang.String toDebugString(long now) {
            return super.toDebugString(now) + ", getAccountsByTypeAndFeatures, " + (this.mFeatures != null ? android.text.TextUtils.join(",", this.mFeatures) : null);
        }
    }

    public android.accounts.Account[] getAccounts(int userId, java.lang.String opPackageName) {
        int callingUid = android.os.Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, opPackageName);
        java.util.List<java.lang.String> visibleAccountTypes = getTypesVisibleToCaller(callingUid, userId, opPackageName);
        if (visibleAccountTypes.isEmpty()) {
            return EMPTY_ACCOUNT_ARRAY;
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            return getAccountsInternal(accounts, callingUid, opPackageName, visibleAccountTypes, false);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public android.accounts.AccountAndUser[] getRunningAccountsForSystem() {
        try {
            int[] runningUserIds = android.app.ActivityManager.getService().getRunningUserIds();
            return getAccountsForSystem(runningUserIds);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public android.accounts.AccountAndUser[] getAllAccountsForSystemProcess() {
        java.util.List<android.content.pm.UserInfo> users = getUserManager().getAliveUsers();
        int[] userIds = new int[users.size()];
        for (int i = 0; i < userIds.length; i++) {
            userIds[i] = users.get(i).id;
        }
        return getAccountsForSystem(userIds);
    }

    private android.accounts.AccountAndUser[] getAccountsForSystem(int[] userIds) {
        java.util.ArrayList<android.accounts.AccountAndUser> runningAccounts = com.google.android.collect.Lists.newArrayList();
        for (int userId : userIds) {
            com.android.server.accounts.AccountManagerService.UserAccounts userAccounts = getUserAccounts(userId);
            if (userAccounts != null) {
                android.accounts.Account[] accounts = getAccountsFromCache(userAccounts, null, android.os.Binder.getCallingUid(), com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, false);
                for (android.accounts.Account account : accounts) {
                    runningAccounts.add(new android.accounts.AccountAndUser(account, userId));
                }
            }
        }
        android.accounts.AccountAndUser[] accountsArray = new android.accounts.AccountAndUser[runningAccounts.size()];
        return (android.accounts.AccountAndUser[]) runningAccounts.toArray(accountsArray);
    }

    public android.accounts.Account[] getAccountsAsUser(java.lang.String type, int userId, java.lang.String opPackageName) {
        int callingUid = android.os.Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, opPackageName);
        try {
            return getAccountsAsUserForPackage(type, userId, opPackageName, -1, opPackageName, false);
        } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e) {
            android.util.Log.e(TAG, "Could not get accounts for user " + userId, e);
            return new android.accounts.Account[0];
        }
    }

    private android.accounts.Account[] getAccountsOrEmptyArray(java.lang.String type, int userId, java.lang.String opPackageName) {
        try {
            return getAccountsAsUser(type, userId, opPackageName);
        } catch (android.database.sqlite.SQLiteCantOpenDatabaseException e) {
            android.util.Log.w(TAG, "Could not get accounts for user " + userId, e);
            return new android.accounts.Account[0];
        }
    }

    private android.accounts.Account[] getAccountsAsUserForPackage(java.lang.String type, int userId, java.lang.String callingPackage, int packageUid, java.lang.String opPackageName, boolean includeUserManagedNotVisible) {
        java.lang.String opPackageName2;
        int callingUid;
        java.util.List<java.lang.String> visibleAccountTypes;
        int callingUid2 = android.os.Binder.getCallingUid();
        if (userId != android.os.UserHandle.getCallingUserId() && callingUid2 != 1000 && this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") != 0) {
            throw new java.lang.SecurityException("User " + android.os.UserHandle.getCallingUserId() + " trying to get account for " + userId);
        }
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "getAccounts: accountType " + type + ", caller's uid " + android.os.Binder.getCallingUid() + ", pid " + android.os.Binder.getCallingPid());
        }
        java.util.List<java.lang.String> managedTypes = getTypesManagedByCaller(callingUid2, android.os.UserHandle.getUserId(callingUid2));
        if (packageUid != -1 && (android.os.UserHandle.isSameApp(callingUid2, 1000) || (type != null && managedTypes.contains(type)))) {
            callingUid = packageUid;
            opPackageName2 = callingPackage;
        } else {
            opPackageName2 = opPackageName;
            callingUid = callingUid2;
        }
        java.util.List<java.lang.String> visibleAccountTypes2 = getTypesVisibleToCaller(callingUid, userId, opPackageName2);
        if (visibleAccountTypes2.isEmpty() || (type != null && !visibleAccountTypes2.contains(type))) {
            return EMPTY_ACCOUNT_ARRAY;
        }
        if (!visibleAccountTypes2.contains(type)) {
            visibleAccountTypes = visibleAccountTypes2;
        } else {
            java.util.List<java.lang.String> visibleAccountTypes3 = new java.util.ArrayList<>();
            visibleAccountTypes3.add(type);
            visibleAccountTypes = visibleAccountTypes3;
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(userId);
            return getAccountsInternal(accounts, callingUid, opPackageName2, visibleAccountTypes, includeUserManagedNotVisible);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private android.accounts.Account[] getAccountsInternal(com.android.server.accounts.AccountManagerService.UserAccounts userAccounts, int callingUid, java.lang.String callingPackage, java.util.List<java.lang.String> visibleAccountTypes, boolean includeUserManagedNotVisible) {
        java.util.ArrayList<android.accounts.Account> visibleAccounts = new java.util.ArrayList<>();
        for (java.lang.String visibleType : visibleAccountTypes) {
            android.accounts.Account[] accountsForType = getAccountsFromCache(userAccounts, visibleType, callingUid, callingPackage, includeUserManagedNotVisible);
            if (accountsForType != null) {
                visibleAccounts.addAll(java.util.Arrays.asList(accountsForType));
            }
        }
        android.accounts.Account[] result = new android.accounts.Account[visibleAccounts.size()];
        for (int i = 0; i < visibleAccounts.size(); i++) {
            result[i] = visibleAccounts.get(i);
        }
        return result;
    }

    public void addSharedAccountsFromParentUser(int parentUserId, int userId, java.lang.String opPackageName) {
        checkManageOrCreateUsersPermission("addSharedAccountsFromParentUser");
        android.accounts.Account[] accounts = getAccountsOrEmptyArray(null, parentUserId, opPackageName);
        for (android.accounts.Account account : accounts) {
            addSharedAccountAsUser(account, userId);
        }
    }

    private boolean addSharedAccountAsUser(android.accounts.Account account, int userId) {
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(handleIncomingUser(userId));
        accounts.accountsDb.deleteSharedAccount(account);
        long accountId = accounts.accountsDb.insertSharedAccount(account);
        if (accountId < 0) {
            android.util.Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping the DB insert failed");
            return false;
        }
        logRecord(com.android.server.accounts.AccountsDb.DEBUG_ACTION_ACCOUNT_ADD, "shared_accounts", accountId, accounts);
        return true;
    }

    public boolean renameSharedAccountAsUser(android.accounts.Account account, java.lang.String newName, int userId) {
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(handleIncomingUser(userId));
        long sharedTableAccountId = accounts.accountsDb.findSharedAccountId(account);
        int r = accounts.accountsDb.renameSharedAccount(account, newName);
        if (r > 0) {
            int callingUid = getCallingUid();
            logRecord(com.android.server.accounts.AccountsDb.DEBUG_ACTION_ACCOUNT_RENAME, "shared_accounts", sharedTableAccountId, accounts, callingUid);
            renameAccountInternal(accounts, account, newName);
        }
        return r > 0;
    }

    public boolean removeSharedAccountAsUser(android.accounts.Account account, int userId) {
        return removeSharedAccountAsUser(account, userId, getCallingUid());
    }

    private boolean removeSharedAccountAsUser(android.accounts.Account account, int userId, int callingUid) throws java.lang.Throwable {
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(handleIncomingUser(userId));
        long sharedTableAccountId = accounts.accountsDb.findSharedAccountId(account);
        boolean deleted = accounts.accountsDb.deleteSharedAccount(account);
        if (deleted) {
            logRecord(com.android.server.accounts.AccountsDb.DEBUG_ACTION_ACCOUNT_REMOVE, "shared_accounts", sharedTableAccountId, accounts, callingUid);
            removeAccountInternal(accounts, account, callingUid);
        }
        return deleted;
    }

    public android.accounts.Account[] getSharedAccountsAsUser(int userId) {
        android.accounts.Account[] accountArray;
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(handleIncomingUser(userId));
        synchronized (accounts.dbLock) {
            java.util.List<android.accounts.Account> accountList = accounts.accountsDb.getSharedAccounts();
            accountArray = new android.accounts.Account[accountList.size()];
            accountList.toArray(accountArray);
        }
        return accountArray;
    }

    public android.accounts.Account[] getAccountsForPackage(java.lang.String packageName, int uid, java.lang.String opPackageName) {
        int callingUid = android.os.Binder.getCallingUid();
        if (!android.os.UserHandle.isSameApp(callingUid, 1000)) {
            throw new java.lang.SecurityException("getAccountsForPackage() called from unauthorized uid " + callingUid + " with uid=" + uid);
        }
        return getAccountsAsUserForPackage(null, android.os.UserHandle.getCallingUserId(), packageName, uid, opPackageName, true);
    }

    public android.accounts.Account[] getAccountsByTypeForPackage(java.lang.String type, java.lang.String packageName, java.lang.String opPackageName) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        this.mAppOpsManager.checkPackage(callingUid, opPackageName);
        try {
            int packageUid = this.mPackageManager.getPackageUidAsUser(packageName, userId);
            if (!android.os.UserHandle.isSameApp(callingUid, 1000) && type != null && !isAccountManagedByCaller(type, callingUid, userId)) {
                return EMPTY_ACCOUNT_ARRAY;
            }
            if (!android.os.UserHandle.isSameApp(callingUid, 1000) && type == null) {
                return getAccountsAsUserForPackage(type, userId, packageName, packageUid, opPackageName, false);
            }
            return getAccountsAsUserForPackage(type, userId, packageName, packageUid, opPackageName, true);
        } catch (android.content.pm.PackageManager.NameNotFoundException re) {
            android.util.Slog.e(TAG, "Couldn't determine the packageUid for " + packageName + re);
            return EMPTY_ACCOUNT_ARRAY;
        }
    }

    private boolean needToStartChooseAccountActivity(android.accounts.Account[] accounts, java.lang.String callingPackage) {
        if (accounts.length < 1) {
            return false;
        }
        if (accounts.length > 1) {
            return true;
        }
        android.accounts.Account account = accounts[0];
        com.android.server.accounts.AccountManagerService.UserAccounts userAccounts = getUserAccounts(android.os.UserHandle.getCallingUserId());
        int visibility = resolveAccountVisibility(account, callingPackage, userAccounts).intValue();
        return visibility == 4;
    }

    private void startChooseAccountActivityWithAccounts(android.accounts.IAccountManagerResponse response, android.accounts.Account[] accounts, java.lang.String callingPackage) {
        android.content.Intent intent = new android.content.Intent(this.mContext, (java.lang.Class<?>) android.accounts.ChooseAccountActivity.class);
        intent.putExtra("accounts", accounts);
        intent.putExtra("accountManagerResponse", (android.os.Parcelable) new android.accounts.AccountManagerResponse(response));
        intent.putExtra("androidPackageName", callingPackage);
        this.mContext.startActivityAsUser(intent, android.os.UserHandle.of(android.os.UserHandle.getCallingUserId()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGetAccountsResult(android.accounts.IAccountManagerResponse response, android.accounts.Account[] accounts, java.lang.String callingPackage) {
        if (needToStartChooseAccountActivity(accounts, callingPackage)) {
            startChooseAccountActivityWithAccounts(response, accounts, callingPackage);
            return;
        }
        if (accounts.length == 1) {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putString("authAccount", accounts[0].name);
            bundle.putString("accountType", accounts[0].type);
            onResult(response, bundle);
            return;
        }
        onResult(response, new android.os.Bundle());
    }

    public void getAccountByTypeAndFeatures(final android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String[] features, final java.lang.String opPackageName) throws java.lang.Throwable {
        int callingUid = android.os.Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, opPackageName);
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "getAccount: accountType " + accountType + ", response " + response + ", features " + java.util.Arrays.toString(features) + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        if (response == null) {
            throw new java.lang.IllegalArgumentException("response is null");
        }
        if (accountType == null) {
            throw new java.lang.IllegalArgumentException("accountType is null");
        }
        int userId = android.os.UserHandle.getCallingUserId();
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts userAccounts = getUserAccounts(userId);
            if (com.android.internal.util.ArrayUtils.isEmpty(features)) {
                try {
                    android.accounts.Account[] accountsWithManagedNotVisible = getAccountsFromCache(userAccounts, accountType, callingUid, opPackageName, true);
                    handleGetAccountsResult(response, accountsWithManagedNotVisible, opPackageName);
                    restoreCallingIdentity(identityToken);
                    return;
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            } else {
                try {
                    new com.android.server.accounts.AccountManagerService.GetAccountsByTypeAndFeatureSession(userAccounts, new android.accounts.IAccountManagerResponse.Stub() { // from class: com.android.server.accounts.AccountManagerService.18
                        public void onResult(android.os.Bundle value) throws android.os.RemoteException {
                            android.os.Parcelable[] parcelables = value.getParcelableArray("accounts");
                            android.accounts.Account[] accounts = new android.accounts.Account[parcelables.length];
                            for (int i = 0; i < parcelables.length; i++) {
                                accounts[i] = (android.accounts.Account) parcelables[i];
                            }
                            com.android.server.accounts.AccountManagerService.this.handleGetAccountsResult(response, accounts, opPackageName);
                        }

                        public void onError(int errorCode, java.lang.String errorMessage) throws android.os.RemoteException {
                        }
                    }, accountType, features, callingUid, opPackageName, true).bind();
                    restoreCallingIdentity(identityToken);
                    return;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
        restoreCallingIdentity(identityToken);
        throw th;
    }

    public void getAccountsByFeatures(android.accounts.IAccountManagerResponse response, java.lang.String type, java.lang.String[] features, java.lang.String opPackageName) throws java.lang.Throwable {
        int callingUid = android.os.Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, opPackageName);
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "getAccounts: accountType " + type + ", response " + response + ", features " + java.util.Arrays.toString(features) + ", caller's uid " + callingUid + ", pid " + android.os.Binder.getCallingPid());
        }
        if (response == null) {
            throw new java.lang.IllegalArgumentException("response is null");
        }
        if (type == null) {
            throw new java.lang.IllegalArgumentException("accountType is null");
        }
        int userId = android.os.UserHandle.getCallingUserId();
        java.util.List<java.lang.String> visibleAccountTypes = getTypesVisibleToCaller(callingUid, userId, opPackageName);
        if (!visibleAccountTypes.contains(type)) {
            android.os.Bundle result = new android.os.Bundle();
            result.putParcelableArray("accounts", EMPTY_ACCOUNT_ARRAY);
            try {
                response.onResult(result);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Cannot respond to caller do to exception.", e);
                return;
            }
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.accounts.AccountManagerService.UserAccounts userAccounts = getUserAccounts(userId);
            try {
                if (features == null || features.length == 0) {
                    android.accounts.Account[] accounts = getAccountsFromCache(userAccounts, type, callingUid, opPackageName, false);
                    android.os.Bundle result2 = new android.os.Bundle();
                    result2.putParcelableArray("accounts", accounts);
                    onResult(response, result2);
                    restoreCallingIdentity(identityToken);
                    return;
                }
                new com.android.server.accounts.AccountManagerService.GetAccountsByTypeAndFeatureSession(userAccounts, response, type, features, callingUid, opPackageName, false).bind();
                restoreCallingIdentity(identityToken);
                return;
            } catch (java.lang.Throwable th) {
                th = th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
        restoreCallingIdentity(identityToken);
        throw th;
    }

    public void onAccountAccessed(java.lang.String token) throws android.os.RemoteException {
        int uid = android.os.Binder.getCallingUid();
        if (android.os.UserHandle.getAppId(uid) == 1000) {
            return;
        }
        int userId = android.os.UserHandle.getCallingUserId();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            for (android.accounts.Account account : getAccounts(userId, this.mContext.getOpPackageName())) {
                if (java.util.Objects.equals(account.getAccessId(), token) && !hasAccountAccess(account, (java.lang.String) null, uid)) {
                    updateAppPermission(account, "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", uid, true);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.accounts.AccountManagerServiceShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    private abstract class Session extends android.accounts.IAccountAuthenticatorResponse.Stub implements android.os.IBinder.DeathRecipient, android.content.ServiceConnection {
        final java.lang.String mAccountName;
        final java.lang.String mAccountType;
        protected final com.android.server.accounts.AccountManagerService.UserAccounts mAccounts;
        final boolean mAuthDetailsRequired;
        android.accounts.IAccountAuthenticator mAuthenticator;
        private int mAuthenticatorUid;
        private long mBindingStartTime;
        protected boolean mCanStartAccountManagerActivity;
        final long mCreationTime;
        final boolean mExpectActivityLaunch;
        private int mNumErrors;
        private int mNumRequestContinued;
        public int mNumResults;
        private int mRealCallingPid;
        private int mRealCallingUid;
        android.accounts.IAccountManagerResponse mResponse;
        private final java.lang.Object mSessionLock;
        private final boolean mStripAuthTokenFromResult;
        final boolean mUpdateLastAuthenticatedTime;

        public abstract void run() throws android.os.RemoteException;

        public Session(com.android.server.accounts.AccountManagerService accountManagerService, com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.IAccountManagerResponse response, java.lang.String accountType, boolean expectActivityLaunch, boolean stripAuthTokenFromResult, java.lang.String accountName, boolean authDetailsRequired) {
            this(accounts, response, accountType, expectActivityLaunch, stripAuthTokenFromResult, accountName, authDetailsRequired, false);
        }

        public Session(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.IAccountManagerResponse response, java.lang.String accountType, boolean expectActivityLaunch, boolean stripAuthTokenFromResult, java.lang.String accountName, boolean authDetailsRequired, boolean updateLastAuthenticatedTime) {
            this.mSessionLock = new java.lang.Object();
            this.mRealCallingUid = -1;
            this.mRealCallingPid = -1;
            this.mNumResults = 0;
            this.mNumRequestContinued = 0;
            this.mNumErrors = 0;
            this.mAuthenticator = null;
            this.mCanStartAccountManagerActivity = false;
            if (accountType == null) {
                throw new java.lang.IllegalArgumentException("accountType is null");
            }
            this.mAccounts = accounts;
            this.mStripAuthTokenFromResult = stripAuthTokenFromResult;
            this.mAccountType = accountType;
            this.mExpectActivityLaunch = expectActivityLaunch;
            this.mCreationTime = android.os.SystemClock.elapsedRealtime();
            this.mAccountName = accountName;
            this.mAuthDetailsRequired = authDetailsRequired;
            this.mUpdateLastAuthenticatedTime = updateLastAuthenticatedTime;
            synchronized (com.android.server.accounts.AccountManagerService.this.mSessions) {
                com.android.server.accounts.AccountManagerService.this.mSessions.put(toString(), this);
            }
            scheduleTimeout();
            if (response != null) {
                try {
                    response.asBinder().linkToDeath(this, 0);
                    this.mResponse = response;
                } catch (android.os.RemoteException e) {
                    binderDied();
                }
            }
        }

        android.accounts.IAccountManagerResponse getResponseAndClose() {
            if (this.mAuthenticatorUid != 0 && this.mBindingStartTime > 0) {
                com.android.server.accounts.AccountManagerService.sResponseLatency.logSampleWithUid(this.mAuthenticatorUid, android.os.SystemClock.uptimeMillis() - this.mBindingStartTime);
            }
            if (this.mResponse == null) {
                close();
                return null;
            }
            android.accounts.IAccountManagerResponse response = this.mResponse;
            close();
            return response;
        }

        protected boolean checkKeyIntent(int authUid, android.os.Bundle bundle) throws java.lang.Throwable {
            if (!checkKeyIntentParceledCorrectly(bundle)) {
                android.util.EventLog.writeEvent(1397638484, "250588548", java.lang.Integer.valueOf(authUid), "");
                return false;
            }
            android.content.Intent intent = (android.content.Intent) bundle.getParcelable("intent", android.content.Intent.class);
            if (intent == null) {
                return true;
            }
            if (intent.getClipData() == null) {
                intent.setClipData(android.content.ClipData.newPlainText(null, null));
            }
            long bid = android.os.Binder.clearCallingIdentity();
            try {
                android.content.pm.PackageManager pm = com.android.server.accounts.AccountManagerService.this.mContext.getPackageManager();
                android.content.pm.ResolveInfo resolveInfo = pm.resolveActivityAsUser(intent, 0, this.mAccounts.userId);
                if (resolveInfo == null) {
                    android.os.Binder.restoreCallingIdentity(bid);
                    return false;
                }
                if (com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(intent.getScheme())) {
                    android.os.Binder.restoreCallingIdentity(bid);
                    return false;
                }
                android.content.pm.ActivityInfo targetActivityInfo = resolveInfo.activityInfo;
                int targetUid = targetActivityInfo.applicationInfo.uid;
                android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                if (!isExportedSystemActivity(targetActivityInfo)) {
                    try {
                        if (!pmi.hasSignatureCapability(targetUid, authUid, 16)) {
                            java.lang.String pkgName = targetActivityInfo.packageName;
                            java.lang.String activityName = targetActivityInfo.name;
                            android.util.Log.e(com.android.server.accounts.AccountManagerService.TAG, java.lang.String.format("KEY_INTENT resolved to an Activity (%s) in a package (%s) that does not share a signature with the supplying authenticator (%s).", activityName, pkgName, this.mAccountType));
                            android.os.Binder.restoreCallingIdentity(bid);
                            return false;
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                }
                intent.setComponent(targetActivityInfo.getComponentName());
                bundle.putParcelable("intent", intent);
                android.os.Binder.restoreCallingIdentity(bid);
                return true;
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
            android.os.Binder.restoreCallingIdentity(bid);
            throw th;
        }

        private boolean checkKeyIntentParceledCorrectly(android.os.Bundle bundle) {
            android.os.Parcel p = android.os.Parcel.obtain();
            p.writeBundle(bundle);
            p.setDataPosition(0);
            android.os.Bundle simulateBundle = p.readBundle();
            p.recycle();
            android.content.Intent intent = (android.content.Intent) bundle.getParcelable("intent", android.content.Intent.class);
            android.content.Intent simulateIntent = (android.content.Intent) simulateBundle.getParcelable("intent", android.content.Intent.class);
            if (intent == null) {
                if (simulateIntent != null) {
                    return false;
                }
                return true;
            }
            if (intent.getClass() != android.content.Intent.class || simulateIntent.getClass() != android.content.Intent.class || !intent.filterEquals(simulateIntent) || intent.getSelector() != simulateIntent.getSelector() || (simulateIntent.getFlags() & 195) != 0) {
                return false;
            }
            return true;
        }

        private boolean isExportedSystemActivity(android.content.pm.ActivityInfo activityInfo) {
            java.lang.String className = activityInfo.name;
            if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(activityInfo.packageName)) {
                return (this.mCanStartAccountManagerActivity && android.accounts.GrantCredentialsPermissionActivity.class.getName().equals(className)) || android.accounts.CantAddAccountActivity.class.getName().equals(className);
            }
            return false;
        }

        private void close() {
            synchronized (com.android.server.accounts.AccountManagerService.this.mSessions) {
                if (com.android.server.accounts.AccountManagerService.this.mSessions.remove(toString()) == null) {
                    return;
                }
                if (this.mResponse != null) {
                    try {
                        this.mResponse.asBinder().unlinkToDeath(this, 0);
                    } catch (java.util.NoSuchElementException e) {
                        android.util.Slog.e(com.android.server.accounts.AccountManagerService.TAG, "error unlinking to death", e);
                    }
                    this.mResponse = null;
                }
                cancelTimeout();
                unbind();
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mResponse = null;
            close();
        }

        protected java.lang.String toDebugString() {
            return toDebugString(android.os.SystemClock.elapsedRealtime());
        }

        protected java.lang.String toDebugString(long now) {
            return "Session: expectLaunch " + this.mExpectActivityLaunch + ", connected " + (this.mAuthenticator != null) + ", stats (" + this.mNumResults + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mNumRequestContinued + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mNumErrors + "), lifetime " + ((now - this.mCreationTime) / 1000.0d);
        }

        void bind() {
            if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "initiating bind to authenticator type " + this.mAccountType);
            }
            if (!bindToAuthenticator(this.mAccountType)) {
                android.util.Log.w(com.android.server.accounts.AccountManagerService.TAG, "bind attempt failed for " + toDebugString());
                onError(1, "bind failure");
            }
        }

        void bind(int callingUid, int callingPid) {
            this.mRealCallingUid = callingUid;
            this.mRealCallingPid = callingPid;
            bind();
        }

        private void unbind() {
            synchronized (this.mSessionLock) {
                if (this.mAuthenticator != null) {
                    this.mAuthenticator = null;
                    com.android.server.accounts.AccountManagerService.this.mContext.unbindService(this);
                }
            }
        }

        private void scheduleTimeout() {
            com.android.server.accounts.AccountManagerService.this.mHandler.sendMessageDelayed(com.android.server.accounts.AccountManagerService.this.mHandler.obtainMessage(3, this), 900000L);
        }

        public void cancelTimeout() {
            com.android.server.accounts.AccountManagerService.this.mHandler.removeMessages(3, this);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized (this.mSessionLock) {
                this.mAuthenticator = android.accounts.IAccountAuthenticator.Stub.asInterface(service);
                try {
                    run();
                } catch (android.os.RemoteException e) {
                    onError(1, "remote exception");
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            android.accounts.IAccountManagerResponse response = getResponseAndClose();
            if (response != null) {
                try {
                    response.onError(1, "disconnected");
                } catch (android.os.RemoteException e) {
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "Session.onServiceDisconnected: caught RemoteException while responding", e);
                    }
                }
            }
        }

        public void onTimedOut() {
            android.accounts.IAccountManagerResponse response = getResponseAndClose();
            if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "Session.onTimedOut");
            }
            if (response != null) {
                try {
                    response.onError(1, "timeout");
                } catch (android.os.RemoteException e) {
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "Session.onTimedOut: caught RemoteException while responding", e);
                    }
                }
            }
        }

        public void onResult(android.os.Bundle result) {
            android.accounts.IAccountManagerResponse response;
            boolean needUpdate = true;
            android.os.Bundle.setDefusable(result, true);
            this.mNumResults++;
            if (result != null) {
                boolean isSuccessfulConfirmCreds = result.getBoolean("booleanResult", false);
                boolean isSuccessfulUpdateCredsOrAddAccount = result.containsKey("authAccount") && result.containsKey("accountType");
                if (!this.mUpdateLastAuthenticatedTime || (!isSuccessfulConfirmCreds && !isSuccessfulUpdateCredsOrAddAccount)) {
                    needUpdate = false;
                }
                if (needUpdate || this.mAuthDetailsRequired) {
                    boolean accountPresent = com.android.server.accounts.AccountManagerService.this.isAccountPresentForCaller(this.mAccountName, this.mAccountType);
                    if (needUpdate && accountPresent) {
                        com.android.server.accounts.AccountManagerService.this.updateLastAuthenticatedTime(new android.accounts.Account(this.mAccountName, this.mAccountType));
                    }
                    if (this.mAuthDetailsRequired) {
                        long lastAuthenticatedTime = -1;
                        if (accountPresent) {
                            lastAuthenticatedTime = this.mAccounts.accountsDb.findAccountLastAuthenticatedTime(new android.accounts.Account(this.mAccountName, this.mAccountType));
                        }
                        result.putLong("lastAuthenticatedTime", lastAuthenticatedTime);
                    }
                }
            }
            if (result != null && !checkKeyIntent(android.os.Binder.getCallingUid(), result)) {
                onError(5, "invalid intent in bundle returned");
                return;
            }
            if (result != null && !android.text.TextUtils.isEmpty(result.getString("authtoken"))) {
                java.lang.String accountName = result.getString("authAccount");
                java.lang.String accountType = result.getString("accountType");
                if (!android.text.TextUtils.isEmpty(accountName) && !android.text.TextUtils.isEmpty(accountType)) {
                    android.accounts.Account account = new android.accounts.Account(accountName, accountType);
                    com.android.server.accounts.AccountManagerService.this.cancelNotification(com.android.server.accounts.AccountManagerService.this.getSigninRequiredNotificationId(this.mAccounts, account), this.mAccounts);
                }
            }
            if (this.mExpectActivityLaunch && result != null && result.containsKey("intent")) {
                response = this.mResponse;
            } else {
                response = getResponseAndClose();
            }
            if (response != null) {
                try {
                    if (result == null) {
                        if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                            android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, getClass().getSimpleName() + " calling onError() on response " + response);
                        }
                        response.onError(5, "null bundle returned");
                        return;
                    }
                    if (this.mStripAuthTokenFromResult) {
                        result.remove("authtoken");
                        if (!checkKeyIntent(android.os.Binder.getCallingUid(), result)) {
                            onError(5, "invalid intent in bundle returned");
                            return;
                        }
                    }
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + response);
                    }
                    if (result.getInt("errorCode", -1) > 0 && 0 == 0) {
                        response.onError(result.getInt("errorCode"), result.getString("errorMessage"));
                    } else {
                        response.onResult(result);
                    }
                } catch (android.os.RemoteException e) {
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "failure while notifying response", e);
                    }
                }
            }
        }

        public void onRequestContinued() {
            this.mNumRequestContinued++;
        }

        public void onError(int errorCode, java.lang.String errorMessage) {
            this.mNumErrors++;
            android.accounts.IAccountManagerResponse response = getResponseAndClose();
            if (response != null) {
                if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                    android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, getClass().getSimpleName() + " calling onError() on response " + response);
                }
                try {
                    response.onError(errorCode, errorMessage);
                    return;
                } catch (android.os.RemoteException e) {
                    if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                        android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "Session.onError: caught RemoteException while responding", e);
                        return;
                    }
                    return;
                }
            }
            if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "Session.onError: already closed");
            }
        }

        private boolean bindToAuthenticator(java.lang.String authenticatorType) {
            android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription> authenticatorInfo = com.android.server.accounts.AccountManagerService.this.mAuthenticatorCache.getServiceInfo(android.accounts.AuthenticatorDescription.newKey(authenticatorType), this.mAccounts.userId);
            if (authenticatorInfo == null) {
                android.util.Log.w(com.android.server.accounts.AccountManagerService.TAG, "there is no authenticator for " + authenticatorType + ", bailing out");
                return false;
            }
            if (!com.android.server.accounts.AccountManagerService.this.isLocalUnlockedUser(this.mAccounts.userId) && !authenticatorInfo.componentInfo.directBootAware) {
                android.util.Slog.w(com.android.server.accounts.AccountManagerService.TAG, "Blocking binding to authenticator " + authenticatorInfo.componentName + " which isn't encryption aware");
                return false;
            }
            android.content.Intent intent = new android.content.Intent();
            intent.setAction("android.accounts.AccountAuthenticator");
            intent.setComponent(authenticatorInfo.componentName);
            intent.putExtra("realCallingUid", this.mRealCallingUid);
            intent.putExtra("realCallingPid", this.mRealCallingPid);
            if (android.util.Log.isLoggable(com.android.server.accounts.AccountManagerService.TAG, 2)) {
                android.util.Log.v(com.android.server.accounts.AccountManagerService.TAG, "performing bindService to " + authenticatorInfo.componentName);
            }
            long flags = com.android.server.accounts.AccountManagerService.this.mAuthenticatorCache.getBindInstantServiceAllowed(this.mAccounts.userId) ? 1 | 4194304 : 1L;
            if (!com.android.server.accounts.AccountManagerService.this.mContext.bindServiceAsUser(intent, this, android.content.Context.BindServiceFlags.of(flags), android.os.UserHandle.of(this.mAccounts.userId))) {
                android.util.Log.w(com.android.server.accounts.AccountManagerService.TAG, "bindService to " + authenticatorInfo.componentName + " failed");
                com.android.server.accounts.AccountManagerService.this.mContext.unbindService(this);
                return false;
            }
            this.mAuthenticatorUid = authenticatorInfo.uid;
            this.mBindingStartTime = android.os.SystemClock.uptimeMillis();
            return true;
        }
    }

    class MessageHandler extends android.os.Handler {
        MessageHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 3:
                    com.android.server.accounts.AccountManagerService.Session session = (com.android.server.accounts.AccountManagerService.Session) msg.obj;
                    session.onTimedOut();
                    return;
                case 4:
                    com.android.server.accounts.AccountManagerService.this.copyAccountToUser(null, (android.accounts.Account) msg.obj, msg.arg1, msg.arg2);
                    return;
                default:
                    throw new java.lang.IllegalStateException("unhandled message: " + msg.what);
            }
        }
    }

    private void logRecord(com.android.server.accounts.AccountManagerService.UserAccounts accounts, java.lang.String action, java.lang.String tableName) {
        logRecord(action, tableName, -1L, accounts);
    }

    private void logRecordWithUid(com.android.server.accounts.AccountManagerService.UserAccounts accounts, java.lang.String action, java.lang.String tableName, int uid) {
        logRecord(action, tableName, -1L, accounts, uid);
    }

    private void logRecord(java.lang.String action, java.lang.String tableName, long accountId, com.android.server.accounts.AccountManagerService.UserAccounts userAccount) {
        logRecord(action, tableName, accountId, userAccount, getCallingUid());
    }

    private void logRecord(java.lang.String action, java.lang.String tableName, long accountId, com.android.server.accounts.AccountManagerService.UserAccounts userAccount, int callingUid) {
        long insertionPoint = userAccount.accountsDb.reserveDebugDbInsertionPoint();
        if (insertionPoint != -1) {
            this.mHandler.post(new java.lang.Runnable(action, tableName, accountId, userAccount, callingUid, insertionPoint) { // from class: com.android.server.accounts.AccountManagerService.1LogRecordTask
                private final long accountId;
                private final java.lang.String action;
                private final int callingUid;
                private final java.lang.String tableName;
                private final com.android.server.accounts.AccountManagerService.UserAccounts userAccount;
                private final long userDebugDbInsertionPoint;

                {
                    this.action = action;
                    this.tableName = tableName;
                    this.accountId = accountId;
                    this.userAccount = userAccount;
                    this.callingUid = callingUid;
                    this.userDebugDbInsertionPoint = insertionPoint;
                }

                @Override // java.lang.Runnable
                public void run() {
                    synchronized (this.userAccount.accountsDb.mDebugStatementLock) {
                        android.database.sqlite.SQLiteStatement logStatement = this.userAccount.accountsDb.getStatementForLogging();
                        if (logStatement == null) {
                            return;
                        }
                        logStatement.bindLong(1, this.accountId);
                        logStatement.bindString(2, this.action);
                        logStatement.bindString(3, com.android.server.accounts.AccountManagerService.this.mDateFormat.format(new java.util.Date()));
                        logStatement.bindLong(4, this.callingUid);
                        logStatement.bindString(5, this.tableName);
                        logStatement.bindLong(6, this.userDebugDbInsertionPoint);
                        try {
                            try {
                                logStatement.execute();
                            } catch (android.database.sqlite.SQLiteFullException | java.lang.IllegalStateException e) {
                                android.util.Slog.w(com.android.server.accounts.AccountManagerService.TAG, "Failed to insert a log record. accountId=" + this.accountId + " action=" + this.action + " tableName=" + this.tableName + " Error: " + e);
                            }
                        } finally {
                            logStatement.clearBindings();
                        }
                    }
                }
            });
        }
    }

    public android.os.IBinder onBind(android.content.Intent intent) {
        return asBinder();
    }

    private static boolean scanArgs(java.lang.String[] args, java.lang.String value) {
        if (args != null) {
            for (java.lang.String arg : args) {
                if (value.equals(arg)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) throws java.lang.Throwable {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, fout)) {
            boolean isCheckinRequest = scanArgs(args, "--checkin") || scanArgs(args, "-c");
            com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(fout, "  ");
            java.util.List<android.content.pm.UserInfo> users = getUserManager().getUsers();
            for (android.content.pm.UserInfo user : users) {
                ipw.println("User " + user + ":");
                ipw.increaseIndent();
                dumpUser(getUserAccounts(user.id), fd, ipw, args, isCheckinRequest);
                ipw.println();
                ipw.decreaseIndent();
            }
        }
    }

    private void dumpUser(com.android.server.accounts.AccountManagerService.UserAccounts userAccounts, java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args, boolean isCheckinRequest) throws java.lang.Throwable {
        boolean isUserUnlocked;
        if (isCheckinRequest) {
            synchronized (userAccounts.dbLock) {
                userAccounts.accountsDb.dumpDeAccountsTable(fout);
            }
            return;
        }
        android.accounts.Account[] accounts = getAccountsFromCache(userAccounts, null, 1000, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, false);
        fout.println("Accounts: " + accounts.length);
        for (android.accounts.Account account : accounts) {
            fout.println("  " + account.toString());
        }
        fout.println();
        synchronized (userAccounts.dbLock) {
            try {
                userAccounts.accountsDb.dumpDebugTable(fout);
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        fout.println();
        synchronized (this.mSessions) {
            try {
                long now = android.os.SystemClock.elapsedRealtime();
                fout.println("Active Sessions: " + this.mSessions.size());
                for (com.android.server.accounts.AccountManagerService.Session session : this.mSessions.values()) {
                    fout.println("  " + session.toDebugString(now));
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                }
            }
        }
        fout.println();
        this.mAuthenticatorCache.dump(fd, fout, args, userAccounts.userId);
        synchronized (this.mUsers) {
            isUserUnlocked = isLocalUnlockedUser(userAccounts.userId);
        }
        if (!isUserUnlocked) {
            return;
        }
        fout.println();
        synchronized (userAccounts.dbLock) {
            java.util.Map<android.accounts.Account, java.util.Map<java.lang.String, java.lang.Integer>> allVisibilityValues = userAccounts.accountsDb.findAllVisibilityValues();
            fout.println("Account visibility:");
            for (android.accounts.Account account2 : allVisibilityValues.keySet()) {
                fout.println("  " + account2.name);
                java.util.Map<java.lang.String, java.lang.Integer> visibilities = allVisibilityValues.get(account2);
                for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : visibilities.entrySet()) {
                    fout.println("    " + entry.getKey() + ", " + entry.getValue());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doNotification(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.CharSequence message, android.content.Intent intent, java.lang.String packageName, int userId) throws java.lang.Throwable {
        long identityToken = clearCallingIdentity();
        try {
            if (android.util.Log.isLoggable(TAG, 2)) {
                android.util.Log.v(TAG, "doNotification: " + ((java.lang.Object) message) + " intent:" + intent);
            }
            try {
                if (intent.getComponent() != null) {
                    if (android.accounts.GrantCredentialsPermissionActivity.class.getName().equals(intent.getComponent().getClassName())) {
                        createNoCredentialsPermissionNotification(account, intent, packageName, accounts);
                        restoreCallingIdentity(identityToken);
                    }
                }
                android.content.Context contextForUser = getContextForUser(new android.os.UserHandle(userId));
                com.android.server.accounts.AccountManagerService.NotificationId id = getSigninRequiredNotificationId(accounts, account);
                intent.addCategory(id.mTag);
                java.lang.String notificationTitleFormat = contextForUser.getText(android.R.string.notification_channel_usb).toString();
                android.app.Notification n = new android.app.Notification.Builder(contextForUser, com.android.internal.notification.SystemNotificationChannels.ACCOUNT).setWhen(0L).setSmallIcon(android.R.drawable.stat_sys_warning).setColor(contextForUser.getColor(android.R.color.system_notification_accent_color)).setContentTitle(java.lang.String.format(notificationTitleFormat, account.name)).setContentText(message).setContentIntent(android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF, null, new android.os.UserHandle(userId))).build();
                installNotification(id, n, packageName, userId);
                restoreCallingIdentity(identityToken);
            } catch (java.lang.Throwable th) {
                th = th;
                restoreCallingIdentity(identityToken);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    private void installNotification(com.android.server.accounts.AccountManagerService.NotificationId id, android.app.Notification notification, java.lang.String packageName, int userId) {
        long token = clearCallingIdentity();
        try {
            android.app.INotificationManager notificationManager = this.mInjector.getNotificationManager();
            try {
                notificationManager.enqueueNotificationWithTag(packageName, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, id.mTag, id.mId, notification, userId);
            } catch (android.os.RemoteException e) {
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelNotification(com.android.server.accounts.AccountManagerService.NotificationId id, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        cancelNotification(id, this.mContext.getPackageName(), accounts);
    }

    private void cancelNotification(com.android.server.accounts.AccountManagerService.NotificationId id, java.lang.String packageName, com.android.server.accounts.AccountManagerService.UserAccounts accounts) {
        long identityToken = clearCallingIdentity();
        try {
            android.app.INotificationManager service = this.mInjector.getNotificationManager();
            service.cancelNotificationWithTag(packageName, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, id.mTag, id.mId, android.os.UserHandle.of(accounts.userId).getIdentifier());
        } catch (android.os.RemoteException e) {
        } catch (java.lang.Throwable th) {
            restoreCallingIdentity(identityToken);
            throw th;
        }
        restoreCallingIdentity(identityToken);
    }

    private boolean isPermittedForPackage(java.lang.String packageName, int userId, java.lang.String... permissions) {
        int opCode;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int uid = this.mPackageManager.getPackageUidAsUser(packageName, userId);
            android.content.pm.IPackageManager pm = android.app.ActivityThread.getPackageManager();
            for (java.lang.String perm : permissions) {
                if (pm.checkPermission(perm, packageName, userId) == 0 && ((opCode = android.app.AppOpsManager.permissionToOpCode(perm)) == -1 || this.mAppOpsManager.checkOpNoThrow(opCode, uid, packageName) == 0)) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    return true;
                }
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException | android.os.RemoteException e) {
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
        android.os.Binder.restoreCallingIdentity(identity);
        return false;
    }

    private boolean checkPermissionAndNote(java.lang.String opPackageName, int callingUid, java.lang.String... permissions) {
        for (java.lang.String perm : permissions) {
            if (this.mContext.checkCallingOrSelfPermission(perm) == 0) {
                if (android.util.Log.isLoggable(TAG, 2)) {
                    android.util.Log.v(TAG, "  caller uid " + callingUid + " has " + perm);
                }
                int opCode = android.app.AppOpsManager.permissionToOpCode(perm);
                if (opCode == -1 || this.mAppOpsManager.noteOpNoThrow(opCode, callingUid, opPackageName) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int handleIncomingUser(int userId) {
        try {
            return android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, true, true, "", (java.lang.String) null);
        } catch (android.os.RemoteException e) {
            return userId;
        }
    }

    private boolean isPrivileged(int callingUid) {
        android.content.pm.PackageInfo packageInfo;
        long identityToken = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String[] packages = this.mPackageManager.getPackagesForUid(callingUid);
            if (packages == null) {
                android.util.Log.d(TAG, "No packages for callingUid " + callingUid);
                return false;
            }
            for (java.lang.String name : packages) {
                try {
                    packageInfo = this.mPackageManager.getPackageInfo(name, 0);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Log.w(TAG, "isPrivileged#Package not found " + e.getMessage());
                }
                if (packageInfo != null && (packageInfo.applicationInfo.privateFlags & 8) != 0) {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                    return true;
                }
            }
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(identityToken);
        }
    }

    private boolean permissionIsGranted(android.accounts.Account account, java.lang.String authTokenType, int callerUid, int userId) {
        if (android.os.UserHandle.getAppId(callerUid) == 1000) {
            if (android.util.Log.isLoggable(TAG, 2)) {
                android.util.Log.v(TAG, "Access to " + account + " granted calling uid is system");
            }
            return true;
        }
        if (isPrivileged(callerUid)) {
            if (android.util.Log.isLoggable(TAG, 2)) {
                android.util.Log.v(TAG, "Access to " + account + " granted calling uid " + callerUid + " privileged");
            }
            return true;
        }
        if (account != null && isAccountManagedByCaller(account.type, callerUid, userId)) {
            if (android.util.Log.isLoggable(TAG, 2)) {
                android.util.Log.v(TAG, "Access to " + account + " granted calling uid " + callerUid + " manages the account");
            }
            return true;
        }
        if (account != null && hasExplicitlyGrantedPermission(account, authTokenType, callerUid)) {
            if (android.util.Log.isLoggable(TAG, 2)) {
                android.util.Log.v(TAG, "Access to " + account + " granted calling uid " + callerUid + " user granted access");
            }
            return true;
        }
        if (android.util.Log.isLoggable(TAG, 2)) {
            android.util.Log.v(TAG, "Access to " + account + " not granted for uid " + callerUid);
            return false;
        }
        return false;
    }

    private boolean isAccountVisibleToCaller(java.lang.String accountType, int callingUid, int userId, java.lang.String opPackageName) {
        if (accountType == null) {
            return false;
        }
        return getTypesVisibleToCaller(callingUid, userId, opPackageName).contains(accountType);
    }

    private boolean checkGetAccountsPermission(java.lang.String packageName, int userId) {
        return isPermittedForPackage(packageName, userId, "android.permission.GET_ACCOUNTS", "android.permission.GET_ACCOUNTS_PRIVILEGED");
    }

    private boolean checkReadContactsPermission(java.lang.String packageName, int userId) {
        return isPermittedForPackage(packageName, userId, "android.permission.READ_CONTACTS");
    }

    private boolean accountTypeManagesContacts(java.lang.String accountType, int userId) {
        if (accountType == null) {
            return false;
        }
        long identityToken = android.os.Binder.clearCallingIdentity();
        try {
            java.util.Collection<android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription>> serviceInfos = this.mAuthenticatorCache.getAllServices(userId);
            android.os.Binder.restoreCallingIdentity(identityToken);
            for (android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription> serviceInfo : serviceInfos) {
                if (accountType.equals(((android.accounts.AuthenticatorDescription) serviceInfo.type).type)) {
                    return isPermittedForPackage(((android.accounts.AuthenticatorDescription) serviceInfo.type).packageName, userId, "android.permission.WRITE_CONTACTS");
                }
            }
            return false;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identityToken);
            throw th;
        }
    }

    private int checkPackageSignature(java.lang.String accountType, int callingUid, int userId) {
        if (accountType == null) {
            return 0;
        }
        long identityToken = android.os.Binder.clearCallingIdentity();
        try {
            java.util.Collection<android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription>> serviceInfos = this.mAuthenticatorCache.getAllServices(userId);
            android.os.Binder.restoreCallingIdentity(identityToken);
            android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            for (android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription> serviceInfo : serviceInfos) {
                if (accountType.equals(((android.accounts.AuthenticatorDescription) serviceInfo.type).type)) {
                    if (serviceInfo.uid == callingUid) {
                        return 2;
                    }
                    if (pmi.hasSignatureCapability(serviceInfo.uid, callingUid, 16)) {
                        return 1;
                    }
                }
            }
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identityToken);
            throw th;
        }
    }

    private boolean isAccountManagedByCaller(java.lang.String accountType, int callingUid, int userId) {
        if (accountType == null) {
            return false;
        }
        return getTypesManagedByCaller(callingUid, userId).contains(accountType);
    }

    private java.util.List<java.lang.String> getTypesVisibleToCaller(int callingUid, int userId, java.lang.String opPackageName) {
        return getTypesForCaller(callingUid, userId, true);
    }

    private java.util.List<java.lang.String> getTypesManagedByCaller(int callingUid, int userId) {
        return getTypesForCaller(callingUid, userId, false);
    }

    private java.util.List<java.lang.String> getTypesForCaller(int callingUid, int userId, boolean isOtherwisePermitted) {
        java.util.List<java.lang.String> managedAccountTypes = new java.util.ArrayList<>();
        long identityToken = android.os.Binder.clearCallingIdentity();
        try {
            java.util.Collection<android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription>> serviceInfos = this.mAuthenticatorCache.getAllServices(userId);
            android.os.Binder.restoreCallingIdentity(identityToken);
            android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            for (android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription> serviceInfo : serviceInfos) {
                if (isOtherwisePermitted || pmi.hasSignatureCapability(serviceInfo.uid, callingUid, 16)) {
                    managedAccountTypes.add(((android.accounts.AuthenticatorDescription) serviceInfo.type).type);
                }
            }
            return managedAccountTypes;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identityToken);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAccountPresentForCaller(java.lang.String accountName, java.lang.String accountType) {
        if (getUserAccountsForCaller().accountCache.containsKey(accountType)) {
            for (android.accounts.Account account : getUserAccountsForCaller().accountCache.get(accountType)) {
                if (account.name.equals(accountName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void checkManageUsersPermission(java.lang.String message) {
        if (android.app.ActivityManager.checkComponentPermission("android.permission.MANAGE_USERS", android.os.Binder.getCallingUid(), -1, true) != 0) {
            throw new java.lang.SecurityException("You need MANAGE_USERS permission to: " + message);
        }
    }

    private static void checkManageOrCreateUsersPermission(java.lang.String message) {
        if (android.app.ActivityManager.checkComponentPermission("android.permission.MANAGE_USERS", android.os.Binder.getCallingUid(), -1, true) != 0 && android.app.ActivityManager.checkComponentPermission("android.permission.CREATE_USERS", android.os.Binder.getCallingUid(), -1, true) != 0) {
            throw new java.lang.SecurityException("You need MANAGE_USERS or CREATE_USERS permission to: " + message);
        }
    }

    private boolean hasExplicitlyGrantedPermission(android.accounts.Account account, java.lang.String authTokenType, int callerUid) {
        long grantsCount;
        if (android.os.UserHandle.getAppId(callerUid) == 1000) {
            return true;
        }
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(android.os.UserHandle.getUserId(callerUid));
        synchronized (accounts.dbLock) {
            synchronized (accounts.cacheLock) {
                if (authTokenType != null) {
                    grantsCount = accounts.accountsDb.findMatchingGrantsCount(callerUid, authTokenType, account);
                } else {
                    grantsCount = accounts.accountsDb.findMatchingGrantsCountAnyToken(callerUid, account);
                }
                boolean permissionGranted = grantsCount > 0;
                if (permissionGranted || !android.app.ActivityManager.isRunningInTestHarness()) {
                    return permissionGranted;
                }
                android.util.Log.d(TAG, "no credentials permission for usage of " + account.toSafeString() + ", " + authTokenType + " by uid " + callerUid + " but ignoring since device is in test harness.");
                return true;
            }
        }
    }

    private boolean isSystemUid(int callingUid) {
        android.content.pm.PackageInfo packageInfo;
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String[] packages = this.mPackageManager.getPackagesForUid(callingUid);
            if (packages == null) {
                android.util.Log.w(TAG, "No known packages with uid " + callingUid);
            } else {
                for (java.lang.String name : packages) {
                    try {
                        packageInfo = this.mPackageManager.getPackageInfo(name, 0);
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        android.util.Log.w(TAG, java.lang.String.format("Could not find package [%s]", name), e);
                    }
                    if (packageInfo != null && (packageInfo.applicationInfo.flags & 1) != 0) {
                        return true;
                    }
                }
            }
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void checkReadAccountsPermitted(int callingUid, java.lang.String accountType, int userId, java.lang.String opPackageName) {
        if (!isAccountVisibleToCaller(accountType, callingUid, userId, opPackageName)) {
            java.lang.String msg = java.lang.String.format("caller uid %s cannot access %s accounts", java.lang.Integer.valueOf(callingUid), accountType);
            android.util.Log.w(TAG, "  " + msg);
            throw new java.lang.SecurityException(msg);
        }
    }

    private boolean canUserModifyAccounts(int userId, int callingUid) {
        return isProfileOwner(callingUid) || !getUserManager().getUserRestrictions(new android.os.UserHandle(userId)).getBoolean("no_modify_accounts");
    }

    private boolean canUserModifyAccountsForType(final int userId, final java.lang.String accountType, final int callingUid) {
        return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda4
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$canUserModifyAccountsForType$3(callingUid, userId, accountType);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$canUserModifyAccountsForType$3(int callingUid, int userId, java.lang.String accountType) throws java.lang.Exception {
        if (isProfileOwner(callingUid)) {
            return true;
        }
        android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) this.mContext.getSystemService("device_policy");
        java.lang.String[] typesArray = dpm.getAccountTypesWithManagementDisabledAsUser(userId);
        if (typesArray == null) {
            return true;
        }
        for (java.lang.String forbiddenType : typesArray) {
            if (forbiddenType.equals(accountType)) {
                return false;
            }
        }
        return true;
    }

    private boolean isProfileOwner(int uid) {
        android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        return dpmi != null && (dpmi.isActiveProfileOwner(uid) || dpmi.isActiveDeviceOwner(uid));
    }

    private boolean canCallerAccessPackage(java.lang.String targetPkgName, int callingUid, int userId) {
        android.content.pm.PackageManagerInternal pmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        if (!android.app.compat.CompatChanges.isChangeEnabled(ENFORCE_PACKAGE_VISIBILITY_FILTERING, callingUid)) {
            return pmInternal.getPackageUid(targetPkgName, 0L, userId) != -1;
        }
        boolean canAccess = !pmInternal.filterAppAccess(targetPkgName, callingUid, userId);
        if (!canAccess && android.util.Log.isLoggable(TAG, 3)) {
            android.util.Log.d(TAG, "Package " + targetPkgName + " is not visible to caller " + callingUid + " for user " + userId);
        }
        return canAccess;
    }

    public void updateAppPermission(android.accounts.Account account, java.lang.String authTokenType, int uid, boolean value) throws android.os.RemoteException {
        int callingUid = getCallingUid();
        if (android.os.UserHandle.getAppId(callingUid) != 1000) {
            throw new java.lang.SecurityException();
        }
        if (value) {
            grantAppPermission(account, authTokenType, uid);
        } else {
            revokeAppPermission(account, authTokenType, uid);
        }
    }

    void grantAppPermission(final android.accounts.Account account, java.lang.String authTokenType, final int uid) {
        if (account == null || authTokenType == null) {
            android.util.Log.e(TAG, "grantAppPermission: called with invalid arguments", new java.lang.Exception());
            return;
        }
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(android.os.UserHandle.getUserId(uid));
        synchronized (accounts.dbLock) {
            synchronized (accounts.cacheLock) {
                long accountId = accounts.accountsDb.findDeAccountId(account);
                if (accountId >= 0) {
                    accounts.accountsDb.insertGrant(accountId, authTokenType, uid);
                }
                cancelNotification(getCredentialPermissionNotificationId(account, authTokenType, uid, accounts), accounts);
                cancelAccountAccessRequestNotificationIfNeeded(account, uid, true, accounts);
            }
        }
        for (final android.accounts.AccountManagerInternal.OnAppPermissionChangeListener listener : this.mAppPermissionChangeListeners) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    listener.onAppPermissionChanged(account, uid);
                }
            });
        }
    }

    private void revokeAppPermission(final android.accounts.Account account, java.lang.String authTokenType, final int uid) {
        if (account == null || authTokenType == null) {
            android.util.Log.e(TAG, "revokeAppPermission: called with invalid arguments", new java.lang.Exception());
            return;
        }
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(android.os.UserHandle.getUserId(uid));
        synchronized (accounts.dbLock) {
            synchronized (accounts.cacheLock) {
                accounts.accountsDb.beginTransaction();
                try {
                    long accountId = accounts.accountsDb.findDeAccountId(account);
                    if (accountId >= 0) {
                        accounts.accountsDb.deleteGrantsByAccountIdAuthTokenTypeAndUid(accountId, authTokenType, uid);
                        accounts.accountsDb.setTransactionSuccessful();
                    }
                    accounts.accountsDb.endTransaction();
                    cancelNotification(getCredentialPermissionNotificationId(account, authTokenType, uid, accounts), accounts);
                } catch (java.lang.Throwable th) {
                    accounts.accountsDb.endTransaction();
                    throw th;
                }
            }
        }
        for (final android.accounts.AccountManagerInternal.OnAppPermissionChangeListener listener : this.mAppPermissionChangeListeners) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    listener.onAppPermissionChanged(account, uid);
                }
            });
        }
    }

    private void removeAccountFromCacheLocked(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account) {
        android.accounts.Account[] oldAccountsForType = accounts.accountCache.get(account.type);
        if (oldAccountsForType != null) {
            java.util.ArrayList<android.accounts.Account> newAccountsList = new java.util.ArrayList<>();
            for (android.accounts.Account curAccount : oldAccountsForType) {
                if (!curAccount.equals(account)) {
                    newAccountsList.add(curAccount);
                }
            }
            if (newAccountsList.isEmpty()) {
                accounts.accountCache.remove(account.type);
            } else {
                android.accounts.Account[] newAccountsForType = new android.accounts.Account[newAccountsList.size()];
                accounts.accountCache.put(account.type, (android.accounts.Account[]) newAccountsList.toArray(newAccountsForType));
            }
        }
        accounts.userDataCache.remove(account);
        accounts.authTokenCache.remove(account);
        accounts.previousNameCache.remove(account);
        accounts.visibilityCache.remove(account);
        android.accounts.AccountManager.invalidateLocalAccountsDataCaches();
    }

    private android.accounts.Account insertAccountIntoCacheLocked(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account) {
        android.accounts.Account[] accountsForType = accounts.accountCache.get(account.type);
        int oldLength = accountsForType != null ? accountsForType.length : 0;
        android.accounts.Account[] newAccountsForType = new android.accounts.Account[oldLength + 1];
        if (accountsForType != null) {
            java.lang.System.arraycopy(accountsForType, 0, newAccountsForType, 0, oldLength);
        }
        java.lang.String token = account.getAccessId() != null ? account.getAccessId() : java.util.UUID.randomUUID().toString();
        newAccountsForType[oldLength] = new android.accounts.Account(account, token);
        accounts.accountCache.put(account.type, newAccountsForType);
        android.accounts.AccountManager.invalidateLocalAccountsDataCaches();
        return newAccountsForType[oldLength];
    }

    private android.accounts.Account[] filterAccounts(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account[] unfiltered, int callingUid, java.lang.String callingPackage, boolean includeManagedNotVisible) {
        java.lang.String visibilityFilterPackage = callingPackage;
        if (visibilityFilterPackage == null) {
            visibilityFilterPackage = getPackageNameForUid(callingUid);
        }
        java.util.Map<android.accounts.Account, java.lang.Integer> firstPass = new java.util.LinkedHashMap<>();
        for (android.accounts.Account account : unfiltered) {
            int visibility = resolveAccountVisibility(account, visibilityFilterPackage, accounts).intValue();
            if (visibility == 1 || visibility == 2 || (includeManagedNotVisible && visibility == 4)) {
                firstPass.put(account, java.lang.Integer.valueOf(visibility));
            }
        }
        java.util.Map<android.accounts.Account, java.lang.Integer> secondPass = filterSharedAccounts(accounts, firstPass, callingUid, callingPackage);
        android.accounts.Account[] filtered = new android.accounts.Account[secondPass.size()];
        return (android.accounts.Account[]) secondPass.keySet().toArray(filtered);
    }

    private java.util.Map<android.accounts.Account, java.lang.Integer> filterSharedAccounts(com.android.server.accounts.AccountManagerService.UserAccounts userAccounts, java.util.Map<android.accounts.Account, java.lang.Integer> unfiltered, int callingUid, java.lang.String callingPackage) {
        android.content.pm.UserInfo user;
        java.lang.String[] packages;
        if (getUserManager() != null && userAccounts != null && userAccounts.userId >= 0 && callingUid != 1000 && (user = getUserManager().getUserInfo(userAccounts.userId)) != null && user.isRestricted()) {
            java.lang.String[] packages2 = this.mPackageManager.getPackagesForUid(callingUid);
            int i = 0;
            if (packages2 != null) {
                packages = packages2;
            } else {
                packages = new java.lang.String[0];
            }
            java.lang.String visibleList = this.mContext.getResources().getString(android.R.string.config_batterymeterPerimeterPath);
            for (java.lang.String packageName : packages) {
                if (visibleList.contains(";" + packageName + ";")) {
                    return unfiltered;
                }
            }
            android.accounts.Account[] sharedAccounts = getSharedAccountsAsUser(userAccounts.userId);
            if (com.android.internal.util.ArrayUtils.isEmpty(sharedAccounts)) {
                return unfiltered;
            }
            java.lang.String requiredAccountType = "";
            try {
                if (callingPackage != null) {
                    android.content.pm.PackageInfo pi = this.mPackageManager.getPackageInfo(callingPackage, 0);
                    if (pi != null && pi.restrictedAccountType != null) {
                        requiredAccountType = pi.restrictedAccountType;
                    }
                } else {
                    int length = packages.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        }
                        java.lang.String packageName2 = packages[i2];
                        android.content.pm.PackageInfo pi2 = this.mPackageManager.getPackageInfo(packageName2, 0);
                        if (pi2 == null || pi2.restrictedAccountType == null) {
                            i2++;
                        } else {
                            requiredAccountType = pi2.restrictedAccountType;
                            break;
                        }
                    }
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.w(TAG, "filterSharedAccounts#Package not found " + e.getMessage());
            }
            java.util.Map<android.accounts.Account, java.lang.Integer> filtered = new java.util.LinkedHashMap<>();
            for (java.util.Map.Entry<android.accounts.Account, java.lang.Integer> entry : unfiltered.entrySet()) {
                android.accounts.Account account = entry.getKey();
                if (account.type.equals(requiredAccountType)) {
                    filtered.put(account, entry.getValue());
                } else {
                    boolean found = false;
                    int length2 = sharedAccounts.length;
                    int i3 = i;
                    while (true) {
                        if (i3 >= length2) {
                            break;
                        }
                        android.accounts.Account shared = sharedAccounts[i3];
                        if (!shared.equals(account)) {
                            i3++;
                        } else {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        filtered.put(account, entry.getValue());
                    }
                }
                i = 0;
            }
            return filtered;
        }
        return unfiltered;
    }

    protected android.accounts.Account[] getAccountsFromCache(com.android.server.accounts.AccountManagerService.UserAccounts userAccounts, java.lang.String accountType, int callingUid, java.lang.String callingPackage, boolean includeManagedNotVisible) {
        android.accounts.Account[] accounts;
        com.android.internal.util.Preconditions.checkState(!java.lang.Thread.holdsLock(userAccounts.cacheLock), "Method should not be called with cacheLock");
        if (accountType != null) {
            synchronized (userAccounts.cacheLock) {
                accounts = userAccounts.accountCache.get(accountType);
            }
            if (accounts == null) {
                return EMPTY_ACCOUNT_ARRAY;
            }
            return filterAccounts(userAccounts, (android.accounts.Account[]) java.util.Arrays.copyOf(accounts, accounts.length), callingUid, callingPackage, includeManagedNotVisible);
        }
        int totalLength = 0;
        synchronized (userAccounts.cacheLock) {
            java.util.Iterator<android.accounts.Account[]> it = userAccounts.accountCache.values().iterator();
            while (it.hasNext()) {
                totalLength += it.next().length;
            }
            if (totalLength == 0) {
                return EMPTY_ACCOUNT_ARRAY;
            }
            android.accounts.Account[] accountsArray = new android.accounts.Account[totalLength];
            int totalLength2 = 0;
            for (android.accounts.Account[] accountsOfType : userAccounts.accountCache.values()) {
                java.lang.System.arraycopy(accountsOfType, 0, accountsArray, totalLength2, accountsOfType.length);
                totalLength2 += accountsOfType.length;
            }
            return filterAccounts(userAccounts, accountsArray, callingUid, callingPackage, includeManagedNotVisible);
        }
    }

    protected void writeUserDataIntoCacheLocked(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String key, java.lang.String value) {
        java.util.Map<java.lang.String, java.lang.String> userDataForAccount = (java.util.Map) accounts.userDataCache.get(account);
        if (userDataForAccount == null) {
            userDataForAccount = accounts.accountsDb.findUserExtrasForAccount(account);
            accounts.userDataCache.put(account, userDataForAccount);
        }
        if (value == null) {
            userDataForAccount.remove(key);
        } else {
            userDataForAccount.put(key, value);
        }
    }

    protected com.android.server.accounts.TokenCache.Value readCachedTokenInternal(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String tokenType, java.lang.String callingPackage, byte[] pkgSigDigest) {
        com.android.server.accounts.TokenCache.Value value;
        synchronized (accounts.cacheLock) {
            value = accounts.accountTokenCaches.get(account, tokenType, callingPackage, pkgSigDigest);
        }
        return value;
    }

    protected void writeAuthTokenIntoCacheLocked(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String key, java.lang.String value) {
        java.util.Map<java.lang.String, java.lang.String> authTokensForAccount = (java.util.Map) accounts.authTokenCache.get(account);
        if (authTokensForAccount == null) {
            authTokensForAccount = accounts.accountsDb.findAuthTokensByAccount(account);
            accounts.authTokenCache.put(account, authTokensForAccount);
        }
        if (value == null) {
            authTokensForAccount.remove(key);
        } else {
            authTokensForAccount.put(key, value);
        }
    }

    protected java.lang.String readAuthTokenInternal(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String authTokenType) {
        java.lang.String str;
        synchronized (accounts.cacheLock) {
            java.util.Map<java.lang.String, java.lang.String> authTokensForAccount = (java.util.Map) accounts.authTokenCache.get(account);
            if (authTokensForAccount != null) {
                return authTokensForAccount.get(authTokenType);
            }
            synchronized (accounts.dbLock) {
                synchronized (accounts.cacheLock) {
                    java.util.Map<java.lang.String, java.lang.String> authTokensForAccount2 = (java.util.Map) accounts.authTokenCache.get(account);
                    if (authTokensForAccount2 == null) {
                        authTokensForAccount2 = accounts.accountsDb.findAuthTokensByAccount(account);
                        accounts.authTokenCache.put(account, authTokensForAccount2);
                    }
                    str = authTokensForAccount2.get(authTokenType);
                }
            }
            return str;
        }
    }

    private java.lang.String readUserDataInternal(com.android.server.accounts.AccountManagerService.UserAccounts accounts, android.accounts.Account account, java.lang.String key) {
        java.util.Map<java.lang.String, java.lang.String> userDataForAccount;
        synchronized (accounts.cacheLock) {
            userDataForAccount = (java.util.Map) accounts.userDataCache.get(account);
        }
        if (userDataForAccount == null) {
            synchronized (accounts.dbLock) {
                synchronized (accounts.cacheLock) {
                    userDataForAccount = (java.util.Map) accounts.userDataCache.get(account);
                    if (userDataForAccount == null) {
                        userDataForAccount = accounts.accountsDb.findUserExtrasForAccount(account);
                        accounts.userDataCache.put(account, userDataForAccount);
                    }
                }
            }
        }
        return userDataForAccount.get(key);
    }

    private android.content.Context getContextForUser(android.os.UserHandle user) {
        try {
            return this.mContext.createPackageContextAsUser(this.mContext.getPackageName(), 0, user);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return this.mContext;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendResponse(android.accounts.IAccountManagerResponse response, android.os.Bundle result) {
        try {
            response.onResult(result);
        } catch (android.os.RemoteException e) {
            if (android.util.Log.isLoggable(TAG, 2)) {
                android.util.Log.v(TAG, "failure while notifying response", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendErrorResponse(android.accounts.IAccountManagerResponse response, int errorCode, java.lang.String errorMessage) {
        try {
            response.onError(errorCode, errorMessage);
        } catch (android.os.RemoteException e) {
            if (android.util.Log.isLoggable(TAG, 2)) {
                android.util.Log.v(TAG, "failure while notifying response", e);
            }
        }
    }

    private boolean isFirstAccountRemovalDisabled(android.accounts.Account account) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (userId != 0 || this.mContext.getResources().getBoolean(android.R.bool.config_canSwitchToHeadlessSystemUser) || android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "allow_primary_gaia_account_removal_for_tests", 0, 0) != 0) {
            return false;
        }
        java.lang.String typeToKeep = this.mContext.getResources().getString(android.R.string.config_batterySaverScheduleProvider);
        if (typeToKeep.isEmpty() || !typeToKeep.equals(account.type)) {
            return false;
        }
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = getUserAccounts(0);
        android.accounts.Account[] accountsOfType = getAccountsFromCache(accounts, typeToKeep, 1000, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, false);
        return accountsOfType.length > 0 && accountsOfType[0].equals(account);
    }

    private final class AccountManagerInternalImpl extends android.accounts.AccountManagerInternal {
        private com.android.server.accounts.AccountManagerBackupHelper mBackupHelper;
        private final java.lang.Object mLock;

        private AccountManagerInternalImpl() {
            this.mLock = new java.lang.Object();
        }

        public void requestAccountAccess(android.accounts.Account account, java.lang.String packageName, int userId, android.os.RemoteCallback callback) throws java.lang.Throwable {
            com.android.server.accounts.AccountManagerService.UserAccounts userAccounts;
            if (account == null) {
                android.util.Slog.w(com.android.server.accounts.AccountManagerService.TAG, "account cannot be null");
                return;
            }
            if (packageName == null) {
                android.util.Slog.w(com.android.server.accounts.AccountManagerService.TAG, "packageName cannot be null");
                return;
            }
            if (userId < 0) {
                android.util.Slog.w(com.android.server.accounts.AccountManagerService.TAG, "user id must be concrete");
                return;
            }
            if (callback == null) {
                android.util.Slog.w(com.android.server.accounts.AccountManagerService.TAG, "callback cannot be null");
                return;
            }
            int visibility = com.android.server.accounts.AccountManagerService.this.resolveAccountVisibility(account, packageName, com.android.server.accounts.AccountManagerService.this.getUserAccounts(userId)).intValue();
            if (visibility == 3) {
                android.util.Slog.w(com.android.server.accounts.AccountManagerService.TAG, "requestAccountAccess: account is hidden");
                return;
            }
            if (com.android.server.accounts.AccountManagerService.this.hasAccountAccess(account, packageName, new android.os.UserHandle(userId))) {
                android.os.Bundle result = new android.os.Bundle();
                result.putBoolean("booleanResult", true);
                callback.sendResult(result);
                return;
            }
            try {
                long identityToken = android.os.Binder.clearCallingIdentity();
                try {
                    int uid = com.android.server.accounts.AccountManagerService.this.mPackageManager.getPackageUidAsUser(packageName, userId);
                    android.content.Intent intent = com.android.server.accounts.AccountManagerService.this.newRequestAccountAccessIntent(account, packageName, uid, callback);
                    synchronized (com.android.server.accounts.AccountManagerService.this.mUsers) {
                        userAccounts = (com.android.server.accounts.AccountManagerService.UserAccounts) com.android.server.accounts.AccountManagerService.this.mUsers.get(userId);
                    }
                    com.android.internal.notification.SystemNotificationChannels.createAccountChannelForPackage(packageName, uid, com.android.server.accounts.AccountManagerService.this.mContext);
                    com.android.server.accounts.AccountManagerService.this.doNotification(userAccounts, account, null, intent, packageName, userId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identityToken);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.e(com.android.server.accounts.AccountManagerService.TAG, "Unknown package " + packageName);
            }
        }

        public void addOnAppPermissionChangeListener(android.accounts.AccountManagerInternal.OnAppPermissionChangeListener listener) {
            com.android.server.accounts.AccountManagerService.this.mAppPermissionChangeListeners.add(listener);
        }

        public boolean hasAccountAccess(android.accounts.Account account, int uid) {
            return com.android.server.accounts.AccountManagerService.this.hasAccountAccess(account, (java.lang.String) null, uid);
        }

        public byte[] backupAccountAccessPermissions(int userId) {
            byte[] bArrBackupAccountAccessPermissions;
            synchronized (this.mLock) {
                if (this.mBackupHelper == null) {
                    this.mBackupHelper = new com.android.server.accounts.AccountManagerBackupHelper(com.android.server.accounts.AccountManagerService.this, this);
                }
                bArrBackupAccountAccessPermissions = this.mBackupHelper.backupAccountAccessPermissions(userId);
            }
            return bArrBackupAccountAccessPermissions;
        }

        public void restoreAccountAccessPermissions(byte[] data, int userId) {
            synchronized (this.mLock) {
                if (this.mBackupHelper == null) {
                    this.mBackupHelper = new com.android.server.accounts.AccountManagerBackupHelper(com.android.server.accounts.AccountManagerService.this, this);
                }
                this.mBackupHelper.restoreAccountAccessPermissions(data, userId);
            }
        }
    }

    static class Injector {
        private final android.content.Context mContext;

        public Injector(android.content.Context context) {
            this.mContext = context;
        }

        android.os.Looper getMessageHandlerLooper() {
            com.android.server.ServiceThread serviceThread = new com.android.server.ServiceThread(com.android.server.accounts.AccountManagerService.TAG, -2, true);
            serviceThread.start();
            return serviceThread.getLooper();
        }

        android.content.Context getContext() {
            return this.mContext;
        }

        void addLocalService(android.accounts.AccountManagerInternal service) {
            com.android.server.LocalServices.addService(android.accounts.AccountManagerInternal.class, service);
        }

        java.lang.String getDeDatabaseName(int userId) {
            java.io.File databaseFile = new java.io.File(android.os.Environment.getDataSystemDeDirectory(userId), "accounts_de.db");
            return databaseFile.getPath();
        }

        java.lang.String getCeDatabaseName(int userId) {
            java.io.File databaseFile = new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), "accounts_ce.db");
            return databaseFile.getPath();
        }

        java.lang.String getPreNDatabaseName(int userId) {
            java.io.File systemDir = android.os.Environment.getDataSystemDirectory();
            java.io.File databaseFile = new java.io.File(android.os.Environment.getUserSystemDirectory(userId), com.android.server.accounts.AccountManagerService.PRE_N_DATABASE_NAME);
            if (userId == 0) {
                java.io.File oldFile = new java.io.File(systemDir, com.android.server.accounts.AccountManagerService.PRE_N_DATABASE_NAME);
                if (oldFile.exists() && !databaseFile.exists()) {
                    java.io.File userDir = android.os.Environment.getUserSystemDirectory(userId);
                    if (!userDir.exists() && !userDir.mkdirs()) {
                        throw new java.lang.IllegalStateException("User dir cannot be created: " + userDir);
                    }
                    if (!oldFile.renameTo(databaseFile)) {
                        throw new java.lang.IllegalStateException("User dir cannot be migrated: " + databaseFile);
                    }
                }
            }
            return databaseFile.getPath();
        }

        com.android.server.accounts.IAccountAuthenticatorCache getAccountAuthenticatorCache() {
            return new com.android.server.accounts.AccountAuthenticatorCache(this.mContext);
        }

        android.app.INotificationManager getNotificationManager() {
            return android.app.NotificationManager.getService();
        }
    }

    private static class NotificationId {
        private final int mId;
        final java.lang.String mTag;

        NotificationId(java.lang.String tag, int type) {
            this.mTag = tag;
            this.mId = type;
        }
    }
}
