package com.android.server.infra;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AbstractMasterSystemService<M extends com.android.server.infra.AbstractMasterSystemService<M, S>, S extends com.android.server.infra.AbstractPerUserSystemService<S, M>> extends com.android.server.SystemService {
    public static final int PACKAGE_RESTART_POLICY_NO_REFRESH = 16;
    public static final int PACKAGE_RESTART_POLICY_REFRESH_EAGER = 64;
    public static final int PACKAGE_RESTART_POLICY_REFRESH_LAZY = 32;
    public static final int PACKAGE_UPDATE_POLICY_NO_REFRESH = 1;
    public static final int PACKAGE_UPDATE_POLICY_REFRESH_EAGER = 4;
    public static final int PACKAGE_UPDATE_POLICY_REFRESH_LAZY = 2;
    public boolean debug;
    protected boolean mAllowInstantService;
    private final android.util.SparseBooleanArray mDisabledByUserRestriction;
    protected final java.lang.Object mLock;
    protected int mMaxTime;
    protected final com.android.server.infra.ServiceNameResolver mServiceNameResolver;
    private final int mServicePackagePolicyFlags;
    private final android.util.SparseArray<java.util.List<S>> mServicesCacheList;
    protected final java.lang.String mTag;
    private com.android.server.pm.UserManagerInternal mUm;
    protected boolean mUpdated;
    private android.util.SparseArray<java.lang.String> mUpdatingPackageNames;
    public boolean verbose;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ServicePackagePolicyFlags {
    }

    public interface Visitor<S> {
        void visit(S s);
    }

    protected abstract S newServiceLocked(int i, boolean z);

    protected AbstractMasterSystemService(android.content.Context context, com.android.server.infra.ServiceNameResolver serviceNameResolver, java.lang.String disallowProperty) {
        this(context, serviceNameResolver, disallowProperty, 34);
    }

    protected AbstractMasterSystemService(android.content.Context context, com.android.server.infra.ServiceNameResolver serviceNameResolver, final java.lang.String disallowProperty, int servicePackagePolicyFlags) {
        super(context);
        this.mTag = getClass().getSimpleName();
        this.mLock = new java.lang.Object();
        this.verbose = false;
        this.debug = false;
        this.mServicesCacheList = new android.util.SparseArray<>();
        this.mMaxTime = 3;
        this.mUpdated = false;
        servicePackagePolicyFlags = (servicePackagePolicyFlags & 7) == 0 ? servicePackagePolicyFlags | 2 : servicePackagePolicyFlags;
        this.mServicePackagePolicyFlags = (servicePackagePolicyFlags & 112) == 0 ? servicePackagePolicyFlags | 32 : servicePackagePolicyFlags;
        this.mServiceNameResolver = serviceNameResolver;
        if (this.mServiceNameResolver != null) {
            this.mServiceNameResolver.setOnTemporaryServiceNameChangedCallback(new com.android.server.infra.ServiceNameResolver.NameResolverListener() { // from class: com.android.server.infra.AbstractMasterSystemService$$ExternalSyntheticLambda1
                @Override // com.android.server.infra.ServiceNameResolver.NameResolverListener
                public final void onNameResolved(int i, java.lang.String str, boolean z) {
                    this.f$0.onServiceNameChanged(i, str, z);
                }
            });
        }
        if (disallowProperty == null) {
            this.mDisabledByUserRestriction = null;
        } else {
            this.mDisabledByUserRestriction = new android.util.SparseBooleanArray();
            com.android.server.pm.UserManagerInternal umi = getUserManagerInternal();
            java.util.List<android.content.pm.UserInfo> users = getSupportedUsers();
            for (int i = 0; i < users.size(); i++) {
                int userId = users.get(i).id;
                boolean disabled = umi.getUserRestriction(userId, disallowProperty);
                if (disabled) {
                    android.util.Slog.i(this.mTag, "Disabling by restrictions user " + userId);
                    this.mDisabledByUserRestriction.put(userId, disabled);
                }
            }
            umi.addUserRestrictionsListener(new com.android.server.pm.UserManagerInternal.UserRestrictionsListener() { // from class: com.android.server.infra.AbstractMasterSystemService$$ExternalSyntheticLambda2
                @Override // com.android.server.pm.UserManagerInternal.UserRestrictionsListener
                public final void onUserRestrictionsChanged(int i2, android.os.Bundle bundle, android.os.Bundle bundle2) {
                    this.f$0.lambda$new$0(disallowProperty, i2, bundle, bundle2);
                }
            });
        }
        startTrackingPackageChanges();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.lang.String disallowProperty, int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
        boolean disabledNow = newRestrictions.getBoolean(disallowProperty, false);
        synchronized (this.mLock) {
            boolean disabledBefore = this.mDisabledByUserRestriction.get(userId);
            if (disabledBefore == disabledNow && this.debug) {
                android.util.Slog.d(this.mTag, "Restriction did not change for user " + userId);
                return;
            }
            android.util.Slog.i(this.mTag, "Updating for user " + userId + ": disabled=" + disabledNow);
            this.mDisabledByUserRestriction.put(userId, disabledNow);
            updateCachedServiceLocked(userId, disabledNow);
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 600) {
            new com.android.server.infra.AbstractMasterSystemService.SettingsObserver(com.android.internal.os.BackgroundThread.getHandler());
        }
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            updateCachedServiceLocked(user.getUserIdentifier());
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStopped(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            removeCachedServiceListLocked(user.getUserIdentifier());
        }
    }

    public final boolean getAllowInstantService() {
        boolean z;
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            z = this.mAllowInstantService;
        }
        return z;
    }

    public final boolean isBindInstantServiceAllowed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mAllowInstantService;
        }
        return z;
    }

    public final void setAllowInstantService(boolean mode) {
        android.util.Slog.i(this.mTag, "setAllowInstantService(): " + mode);
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            this.mAllowInstantService = mode;
        }
    }

    public final void setTemporaryService(int userId, java.lang.String componentName, int durationMs) {
        android.util.Slog.i(this.mTag, "setTemporaryService(" + userId + ") to " + componentName + " for " + durationMs + "ms");
        if (this.mServiceNameResolver == null) {
            return;
        }
        enforceCallingPermissionForManagement();
        java.util.Objects.requireNonNull(componentName);
        int maxDurationMs = getMaximumTemporaryServiceDurationMs();
        if (durationMs > maxDurationMs) {
            throw new java.lang.IllegalArgumentException("Max duration is " + maxDurationMs + " (called with " + durationMs + ")");
        }
        synchronized (this.mLock) {
            com.android.server.infra.AbstractPerUserSystemService abstractPerUserSystemServicePeekServiceForUserLocked = peekServiceForUserLocked(userId);
            if (abstractPerUserSystemServicePeekServiceForUserLocked != null) {
                abstractPerUserSystemServicePeekServiceForUserLocked.removeSelfFromCache();
            }
            this.mServiceNameResolver.setTemporaryService(userId, componentName, durationMs);
        }
    }

    public final void setTemporaryServices(int userId, java.lang.String[] componentNames, int durationMs) {
        android.util.Slog.i(this.mTag, "setTemporaryService(" + userId + ") to " + java.util.Arrays.toString(componentNames) + " for " + durationMs + "ms");
        if (this.mServiceNameResolver == null) {
            return;
        }
        enforceCallingPermissionForManagement();
        java.util.Objects.requireNonNull(componentNames);
        int maxDurationMs = getMaximumTemporaryServiceDurationMs();
        if (durationMs > maxDurationMs) {
            throw new java.lang.IllegalArgumentException("Max duration is " + maxDurationMs + " (called with " + durationMs + ")");
        }
        synchronized (this.mLock) {
            com.android.server.infra.AbstractPerUserSystemService abstractPerUserSystemServicePeekServiceForUserLocked = peekServiceForUserLocked(userId);
            if (abstractPerUserSystemServicePeekServiceForUserLocked != null) {
                abstractPerUserSystemServicePeekServiceForUserLocked.removeSelfFromCache();
            }
            this.mServiceNameResolver.setTemporaryServices(userId, componentNames, durationMs);
        }
    }

    public final boolean setDefaultServiceEnabled(int userId, boolean enabled) {
        android.util.Slog.i(this.mTag, "setDefaultServiceEnabled() for userId " + userId + ": " + enabled);
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            if (this.mServiceNameResolver == null) {
                return false;
            }
            boolean changed = this.mServiceNameResolver.setDefaultServiceEnabled(userId, enabled);
            if (!changed) {
                if (this.verbose) {
                    android.util.Slog.v(this.mTag, "setDefaultServiceEnabled(" + userId + "): already " + enabled);
                }
                return false;
            }
            com.android.server.infra.AbstractPerUserSystemService abstractPerUserSystemServicePeekServiceForUserLocked = peekServiceForUserLocked(userId);
            if (abstractPerUserSystemServicePeekServiceForUserLocked != null) {
                abstractPerUserSystemServicePeekServiceForUserLocked.removeSelfFromCache();
            }
            updateCachedServiceLocked(userId);
            return true;
        }
    }

    public final boolean isDefaultServiceEnabled(int userId) {
        boolean zIsDefaultServiceEnabled;
        enforceCallingPermissionForManagement();
        if (this.mServiceNameResolver == null) {
            return false;
        }
        synchronized (this.mLock) {
            zIsDefaultServiceEnabled = this.mServiceNameResolver.isDefaultServiceEnabled(userId);
        }
        return zIsDefaultServiceEnabled;
    }

    protected int getMaximumTemporaryServiceDurationMs() {
        throw new java.lang.UnsupportedOperationException("Not implemented by " + getClass());
    }

    public final void resetTemporaryService(int userId) {
        android.util.Slog.i(this.mTag, "resetTemporaryService(): " + userId);
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            com.android.server.infra.AbstractPerUserSystemService serviceForUserLocked = getServiceForUserLocked(userId);
            if (serviceForUserLocked != null) {
                serviceForUserLocked.resetTemporaryServiceLocked();
            }
        }
    }

    protected void enforceCallingPermissionForManagement() {
        throw new java.lang.UnsupportedOperationException("Not implemented by " + getClass());
    }

    protected java.util.List<S> newServiceListLocked(int resolvedUserId, boolean disabled, java.lang.String[] serviceNames) {
        throw new java.lang.UnsupportedOperationException("newServiceListLocked not implemented. ");
    }

    protected void registerForExtraSettingsChanges(android.content.ContentResolver resolver, android.database.ContentObserver observer) {
    }

    protected void onSettingsChanged(int userId, java.lang.String property) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public S getServiceForUserLocked(int userId) {
        java.util.List<S> services = getServiceListForUserLocked(userId);
        if (services == null || services.size() == 0) {
            return null;
        }
        return services.get(0);
    }

    protected java.util.List<S> getServiceListForUserLocked(int i) {
        int iHandleIncomingUser = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), i, false, false, null, null);
        java.util.ArrayList arrayList = this.mServicesCacheList.get(iHandleIncomingUser);
        if (arrayList == null || arrayList.size() == 0) {
            boolean zIsDisabledLocked = isDisabledLocked(i);
            if (this.mServiceNameResolver != null && this.mServiceNameResolver.isConfiguredInMultipleMode()) {
                arrayList = newServiceListLocked(iHandleIncomingUser, zIsDisabledLocked, this.mServiceNameResolver.getServiceNameList(i));
            } else {
                arrayList = new java.util.ArrayList();
                arrayList.add(newServiceLocked(iHandleIncomingUser, zIsDisabledLocked));
            }
            if (!zIsDisabledLocked) {
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    onServiceEnabledLocked((com.android.server.infra.AbstractPerUserSystemService) arrayList.get(i2), iHandleIncomingUser);
                }
            }
            this.mServicesCacheList.put(i, (java.util.List<S>) arrayList);
        }
        return (java.util.List<S>) arrayList;
    }

    protected S peekServiceForUserLocked(int userId) {
        java.util.List<S> serviceList = peekServiceListForUserLocked(userId);
        if (serviceList == null || serviceList.size() == 0) {
            return null;
        }
        return serviceList.get(0);
    }

    protected java.util.List<S> peekServiceListForUserLocked(int userId) {
        int resolvedUserId = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, null, null);
        return this.mServicesCacheList.get(resolvedUserId);
    }

    protected void updateCachedServiceLocked(int userId) {
        updateCachedServiceListLocked(userId, isDisabledLocked(userId));
    }

    protected boolean isDisabledLocked(int userId) {
        return this.mDisabledByUserRestriction != null && this.mDisabledByUserRestriction.get(userId);
    }

    protected void updateService(final int userId) {
        if (this.mMaxTime > 0 && !this.mUpdated) {
            this.mUpdated = true;
            com.android.internal.os.BackgroundThread.getHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.infra.AbstractMasterSystemService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateService$1(userId);
                }
            }, 10000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateService$1(int userId) {
        synchronized (this.mLock) {
            this.mMaxTime--;
            this.mUpdated = false;
            android.util.Slog.e(this.mTag, "updateCachedServiceLocked, mMaxTime: " + this.mMaxTime);
            updateCachedServiceLocked(userId);
        }
    }

    protected S updateCachedServiceLocked(int i, boolean z) {
        S s = (S) getServiceForUserLocked(i);
        updateCachedServiceListLocked(i, z);
        return s;
    }

    protected java.util.List<S> updateCachedServiceListLocked(int userId, boolean disabled) {
        if (this.mServiceNameResolver != null && this.mServiceNameResolver.isConfiguredInMultipleMode()) {
            return updateCachedServiceListMultiModeLocked(userId, disabled);
        }
        java.util.List<S> services = getServiceListForUserLocked(userId);
        if (services == null) {
            return null;
        }
        for (int i = 0; i < services.size(); i++) {
            S service = services.get(i);
            if (service != null) {
                synchronized (service.mLock) {
                    service.updateLocked(disabled);
                    if (!service.isEnabledLocked()) {
                        removeCachedServiceListLocked(userId);
                    } else {
                        onServiceEnabledLocked(services.get(i), userId);
                    }
                }
            }
        }
        return services;
    }

    private java.util.List<S> updateCachedServiceListMultiModeLocked(int userId, boolean disabled) {
        java.util.List<S> services;
        int resolvedUserId = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, null, null);
        new java.util.ArrayList();
        synchronized (this.mLock) {
            removeCachedServiceListLocked(resolvedUserId);
            services = getServiceListForUserLocked(userId);
        }
        return services;
    }

    protected java.lang.String getServiceSettingsProperty() {
        return null;
    }

    protected void onServiceEnabledLocked(S service, int userId) {
    }

    protected final java.util.List<S> removeCachedServiceListLocked(int userId) {
        java.util.List<S> services = peekServiceListForUserLocked(userId);
        if (services != null) {
            this.mServicesCacheList.delete(userId);
            for (int i = 0; i < services.size(); i++) {
                onServiceRemoved(services.get(i), userId);
            }
        }
        return services;
    }

    protected void onServicePackageUpdatingLocked(int userId) {
        if (this.verbose) {
            android.util.Slog.v(this.mTag, "onServicePackageUpdatingLocked(" + userId + ")");
        }
    }

    protected void onServicePackageUpdatedLocked(int userId) {
        if (this.verbose) {
            android.util.Slog.v(this.mTag, "onServicePackageUpdated(" + userId + ")");
        }
    }

    protected void onServicePackageDataClearedLocked(int userId) {
        if (this.verbose) {
            android.util.Slog.v(this.mTag, "onServicePackageDataCleared(" + userId + ")");
        }
    }

    protected void onServicePackageRestartedLocked(int userId) {
        if (this.verbose) {
            android.util.Slog.v(this.mTag, "onServicePackageRestarted(" + userId + ")");
        }
    }

    protected void onServiceRemoved(S service, int userId) {
    }

    protected void onServiceNameChanged(int userId, java.lang.String serviceName, boolean isTemporary) {
        synchronized (this.mLock) {
            updateCachedServiceListLocked(userId, isDisabledLocked(userId));
        }
    }

    protected void onServiceNameListChanged(int userId, java.lang.String[] serviceNames, boolean isTemporary) {
        synchronized (this.mLock) {
            updateCachedServiceListLocked(userId, isDisabledLocked(userId));
        }
    }

    protected void visitServicesLocked(com.android.server.infra.AbstractMasterSystemService.Visitor<S> visitor) {
        int size = this.mServicesCacheList.size();
        for (int i = 0; i < size; i++) {
            java.util.List<S> services = this.mServicesCacheList.valueAt(i);
            for (int j = 0; j < services.size(); j++) {
                visitor.visit(services.get(j));
            }
        }
    }

    protected void clearCacheLocked() {
        this.mServicesCacheList.clear();
    }

    protected com.android.server.pm.UserManagerInternal getUserManagerInternal() {
        if (this.mUm == null) {
            if (this.verbose) {
                android.util.Slog.v(this.mTag, "lazy-loading UserManagerInternal");
            }
            this.mUm = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        }
        return this.mUm;
    }

    protected java.util.List<android.content.pm.UserInfo> getSupportedUsers() {
        android.content.pm.UserInfo[] allUsers = getUserManagerInternal().getUserInfos();
        int size = allUsers.length;
        java.util.List<android.content.pm.UserInfo> supportedUsers = new java.util.ArrayList<>(size);
        for (android.content.pm.UserInfo userInfo : allUsers) {
            if (isUserSupported(new com.android.server.SystemService.TargetUser(userInfo))) {
                supportedUsers.add(userInfo);
            }
        }
        return supportedUsers;
    }

    protected void assertCalledByPackageOwner(java.lang.String packageName) {
        java.util.Objects.requireNonNull(packageName);
        int uid = android.os.Binder.getCallingUid();
        java.lang.String[] packages = getContext().getPackageManager().getPackagesForUid(uid);
        if (packages != null) {
            for (java.lang.String candidate : packages) {
                if (packageName.equals(candidate)) {
                    return;
                }
            }
        }
        throw new java.lang.SecurityException("UID " + uid + " does not own " + packageName);
    }

    protected void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        boolean realDebug = this.debug;
        boolean realVerbose = this.verbose;
        try {
            this.verbose = true;
            this.debug = true;
            int size = this.mServicesCacheList.size();
            pw.print(prefix);
            pw.print("Debug: ");
            pw.print(realDebug);
            pw.print(" Verbose: ");
            pw.println(realVerbose);
            pw.print("Package policy flags: ");
            pw.println(this.mServicePackagePolicyFlags);
            if (this.mUpdatingPackageNames != null) {
                pw.print("Packages being updated: ");
                pw.println(this.mUpdatingPackageNames);
            }
            dumpSupportedUsers(pw, prefix);
            if (this.mServiceNameResolver != null) {
                pw.print(prefix);
                pw.print("Name resolver: ");
                this.mServiceNameResolver.dumpShort(pw);
                pw.println();
                java.util.List<android.content.pm.UserInfo> users = getSupportedUsers();
                for (int i = 0; i < users.size(); i++) {
                    int userId = users.get(i).id;
                    pw.print("    ");
                    pw.print(userId);
                    pw.print(": ");
                    this.mServiceNameResolver.dumpShort(pw, userId);
                    pw.println();
                }
            }
            pw.print(prefix);
            pw.print("Users disabled by restriction: ");
            pw.println(this.mDisabledByUserRestriction);
            pw.print(prefix);
            pw.print("Allow instant service: ");
            pw.println(this.mAllowInstantService);
            java.lang.String settingsProperty = getServiceSettingsProperty();
            if (settingsProperty != null) {
                pw.print(prefix);
                pw.print("Settings property: ");
                pw.println(settingsProperty);
            }
            pw.print(prefix);
            pw.print("Cached services: ");
            if (size == 0) {
                pw.println("none");
            } else {
                pw.println(size);
                for (int i2 = 0; i2 < size; i2++) {
                    pw.print(prefix);
                    pw.print("Service at ");
                    pw.print(i2);
                    pw.println(": ");
                    java.util.List<S> services = this.mServicesCacheList.valueAt(i2);
                    for (int j = 0; j < services.size(); j++) {
                        S service = services.get(j);
                        synchronized (service.mLock) {
                            service.dumpLocked("    ", pw);
                        }
                    }
                    pw.println();
                }
            }
        } finally {
            this.debug = realDebug;
            this.verbose = realVerbose;
        }
    }

    /* JADX INFO: renamed from: com.android.server.infra.AbstractMasterSystemService$1, reason: invalid class name */
    class AnonymousClass1 extends com.android.internal.content.PackageMonitor {
        AnonymousClass1(boolean supportsPackageRestartQuery) {
            super(supportsPackageRestartQuery);
        }

        public void onPackageUpdateStarted(java.lang.String packageName, int uid) {
            if (com.android.server.infra.AbstractMasterSystemService.this.verbose) {
                android.util.Slog.v(com.android.server.infra.AbstractMasterSystemService.this.mTag, "onPackageUpdateStarted(): " + packageName);
            }
            java.lang.String activePackageName = getActiveServicePackageNameLocked();
            if (packageName.equals(activePackageName)) {
                int userId = getChangingUserId();
                synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                    if (com.android.server.infra.AbstractMasterSystemService.this.mUpdatingPackageNames == null) {
                        com.android.server.infra.AbstractMasterSystemService.this.mUpdatingPackageNames = new android.util.SparseArray(com.android.server.infra.AbstractMasterSystemService.this.mServicesCacheList.size());
                    }
                    com.android.server.infra.AbstractMasterSystemService.this.mUpdatingPackageNames.put(userId, packageName);
                    com.android.server.infra.AbstractMasterSystemService.this.onServicePackageUpdatingLocked(userId);
                    if ((com.android.server.infra.AbstractMasterSystemService.this.mServicePackagePolicyFlags & 1) != 0) {
                        if (com.android.server.infra.AbstractMasterSystemService.this.debug) {
                            android.util.Slog.d(com.android.server.infra.AbstractMasterSystemService.this.mTag, "Holding service for user " + userId + " while package " + activePackageName + " is being updated");
                        }
                    } else {
                        if (com.android.server.infra.AbstractMasterSystemService.this.debug) {
                            android.util.Slog.d(com.android.server.infra.AbstractMasterSystemService.this.mTag, "Removing service for user " + userId + " because package " + activePackageName + " is being updated");
                        }
                        com.android.server.infra.AbstractMasterSystemService.this.removeCachedServiceListLocked(userId);
                        if ((com.android.server.infra.AbstractMasterSystemService.this.mServicePackagePolicyFlags & 4) != 0) {
                            if (com.android.server.infra.AbstractMasterSystemService.this.debug) {
                                android.util.Slog.d(com.android.server.infra.AbstractMasterSystemService.this.mTag, "Eagerly recreating service for user " + userId);
                            }
                            com.android.server.infra.AbstractMasterSystemService.this.getServiceForUserLocked(userId);
                        }
                    }
                }
            }
        }

        public void onPackageUpdateFinished(java.lang.String packageName, int uid) {
            if (com.android.server.infra.AbstractMasterSystemService.this.verbose) {
                android.util.Slog.v(com.android.server.infra.AbstractMasterSystemService.this.mTag, "onPackageUpdateFinished(): " + packageName);
            }
            int userId = getChangingUserId();
            synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                java.lang.String activePackageName = com.android.server.infra.AbstractMasterSystemService.this.mUpdatingPackageNames == null ? null : (java.lang.String) com.android.server.infra.AbstractMasterSystemService.this.mUpdatingPackageNames.get(userId);
                if (packageName.equals(activePackageName)) {
                    if (com.android.server.infra.AbstractMasterSystemService.this.mUpdatingPackageNames != null) {
                        com.android.server.infra.AbstractMasterSystemService.this.mUpdatingPackageNames.remove(userId);
                        if (com.android.server.infra.AbstractMasterSystemService.this.mUpdatingPackageNames.size() == 0) {
                            com.android.server.infra.AbstractMasterSystemService.this.mUpdatingPackageNames = null;
                        }
                    }
                    com.android.server.infra.AbstractMasterSystemService.this.onServicePackageUpdatedLocked(userId);
                } else {
                    handlePackageUpdateLocked(packageName);
                }
            }
        }

        public void onPackageRemoved(java.lang.String packageName, int uid) {
            android.content.ComponentName componentName;
            if (com.android.server.infra.AbstractMasterSystemService.this.mServiceNameResolver != null && com.android.server.infra.AbstractMasterSystemService.this.mServiceNameResolver.isConfiguredInMultipleMode()) {
                int userId = getChangingUserId();
                synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                    com.android.server.infra.AbstractMasterSystemService.this.handlePackageRemovedMultiModeLocked(packageName, userId);
                }
                return;
            }
            synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                int userId2 = getChangingUserId();
                com.android.server.infra.AbstractPerUserSystemService abstractPerUserSystemServicePeekServiceForUserLocked = com.android.server.infra.AbstractMasterSystemService.this.peekServiceForUserLocked(userId2);
                if (abstractPerUserSystemServicePeekServiceForUserLocked != null && (componentName = abstractPerUserSystemServicePeekServiceForUserLocked.getServiceComponentName()) != null && packageName.equals(componentName.getPackageName())) {
                    handleActiveServiceRemoved(userId2);
                }
            }
        }

        public boolean onHandleForceStop(android.content.Intent intent, java.lang.String[] packages, int uid, boolean doit) {
            synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                java.lang.String activePackageName = getActiveServicePackageNameLocked();
                for (java.lang.String pkg : packages) {
                    if (pkg.equals(activePackageName)) {
                        if (!doit) {
                            return true;
                        }
                        java.lang.String action = intent.getAction();
                        int userId = getChangingUserId();
                        if ("android.intent.action.PACKAGE_RESTARTED".equals(action)) {
                            handleActiveServiceRestartedLocked(activePackageName, userId);
                        } else {
                            com.android.server.infra.AbstractMasterSystemService.this.removeCachedServiceListLocked(userId);
                        }
                    } else {
                        handlePackageUpdateLocked(pkg);
                    }
                }
                return false;
            }
        }

        public void onPackageDataCleared(java.lang.String packageName, int uid) {
            android.content.ComponentName componentName;
            if (com.android.server.infra.AbstractMasterSystemService.this.verbose) {
                android.util.Slog.v(com.android.server.infra.AbstractMasterSystemService.this.mTag, "onPackageDataCleared(): " + packageName);
            }
            int userId = getChangingUserId();
            if (com.android.server.infra.AbstractMasterSystemService.this.mServiceNameResolver != null && com.android.server.infra.AbstractMasterSystemService.this.mServiceNameResolver.isConfiguredInMultipleMode()) {
                synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                    com.android.server.infra.AbstractMasterSystemService.this.onServicePackageDataClearedMultiModeLocked(packageName, userId);
                }
                return;
            }
            synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                com.android.server.infra.AbstractPerUserSystemService abstractPerUserSystemServicePeekServiceForUserLocked = com.android.server.infra.AbstractMasterSystemService.this.peekServiceForUserLocked(userId);
                if (abstractPerUserSystemServicePeekServiceForUserLocked != null && (componentName = abstractPerUserSystemServicePeekServiceForUserLocked.getServiceComponentName()) != null && packageName.equals(componentName.getPackageName())) {
                    com.android.server.infra.AbstractMasterSystemService.this.onServicePackageDataClearedLocked(userId);
                }
            }
        }

        private void handleActiveServiceRemoved(int userId) {
            synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                com.android.server.infra.AbstractMasterSystemService.this.removeCachedServiceListLocked(userId);
            }
            java.lang.String serviceSettingsProperty = com.android.server.infra.AbstractMasterSystemService.this.getServiceSettingsProperty();
            if (serviceSettingsProperty != null) {
                android.provider.Settings.Secure.putStringForUser(com.android.server.infra.AbstractMasterSystemService.this.getContext().getContentResolver(), serviceSettingsProperty, null, userId);
            }
        }

        private void handleActiveServiceRestartedLocked(java.lang.String activePackageName, int userId) {
            if ((com.android.server.infra.AbstractMasterSystemService.this.mServicePackagePolicyFlags & 16) != 0) {
                if (com.android.server.infra.AbstractMasterSystemService.this.debug) {
                    android.util.Slog.d(com.android.server.infra.AbstractMasterSystemService.this.mTag, "Holding service for user " + userId + " while package " + activePackageName + " is being restarted");
                }
            } else {
                if (com.android.server.infra.AbstractMasterSystemService.this.debug) {
                    android.util.Slog.d(com.android.server.infra.AbstractMasterSystemService.this.mTag, "Removing service for user " + userId + " because package " + activePackageName + " is being restarted");
                }
                com.android.server.infra.AbstractMasterSystemService.this.removeCachedServiceListLocked(userId);
                if ((com.android.server.infra.AbstractMasterSystemService.this.mServicePackagePolicyFlags & 64) != 0) {
                    if (com.android.server.infra.AbstractMasterSystemService.this.debug) {
                        android.util.Slog.d(com.android.server.infra.AbstractMasterSystemService.this.mTag, "Eagerly recreating service for user " + userId);
                    }
                    com.android.server.infra.AbstractMasterSystemService.this.updateCachedServiceLocked(userId);
                }
            }
            com.android.server.infra.AbstractMasterSystemService.this.onServicePackageRestartedLocked(userId);
        }

        public void onPackageModified(java.lang.String packageName) {
            synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                if (com.android.server.infra.AbstractMasterSystemService.this.verbose) {
                    android.util.Slog.v(com.android.server.infra.AbstractMasterSystemService.this.mTag, "onPackageModified(): " + packageName);
                }
                if (com.android.server.infra.AbstractMasterSystemService.this.mServiceNameResolver == null) {
                    return;
                }
                int userId = getChangingUserId();
                java.lang.String[] serviceNames = com.android.server.infra.AbstractMasterSystemService.this.mServiceNameResolver.getDefaultServiceNameList(userId);
                if (serviceNames != null) {
                    for (java.lang.String str : serviceNames) {
                        peekAndUpdateCachedServiceLocked(packageName, userId, str);
                    }
                }
            }
        }

        private void peekAndUpdateCachedServiceLocked(java.lang.String packageName, int userId, java.lang.String serviceName) {
            android.content.ComponentName serviceComponentName;
            com.android.server.infra.AbstractPerUserSystemService abstractPerUserSystemServicePeekServiceForUserLocked;
            if (serviceName != null && (serviceComponentName = android.content.ComponentName.unflattenFromString(serviceName)) != null && serviceComponentName.getPackageName().equals(packageName) && (abstractPerUserSystemServicePeekServiceForUserLocked = com.android.server.infra.AbstractMasterSystemService.this.peekServiceForUserLocked(userId)) != null) {
                android.content.ComponentName componentName = abstractPerUserSystemServicePeekServiceForUserLocked.getServiceComponentName();
                if (componentName == null) {
                    if (com.android.server.infra.AbstractMasterSystemService.this.verbose) {
                        android.util.Slog.v(com.android.server.infra.AbstractMasterSystemService.this.mTag, "update cached");
                    }
                    com.android.server.infra.AbstractMasterSystemService.this.updateCachedServiceLocked(userId);
                }
            }
        }

        private java.lang.String getActiveServicePackageNameLocked() {
            android.content.ComponentName serviceComponent;
            int userId = getChangingUserId();
            com.android.server.infra.AbstractPerUserSystemService abstractPerUserSystemServicePeekServiceForUserLocked = com.android.server.infra.AbstractMasterSystemService.this.peekServiceForUserLocked(userId);
            if (abstractPerUserSystemServicePeekServiceForUserLocked == null || (serviceComponent = abstractPerUserSystemServicePeekServiceForUserLocked.getServiceComponentName()) == null) {
                return null;
            }
            return serviceComponent.getPackageName();
        }

        private void handlePackageUpdateLocked(final java.lang.String packageName) {
            com.android.server.infra.AbstractMasterSystemService.this.visitServicesLocked(new com.android.server.infra.AbstractMasterSystemService.Visitor() { // from class: com.android.server.infra.AbstractMasterSystemService$1$$ExternalSyntheticLambda0
                @Override // com.android.server.infra.AbstractMasterSystemService.Visitor
                public final void visit(java.lang.Object obj) {
                    ((com.android.server.infra.AbstractPerUserSystemService) obj).handlePackageUpdateLocked(packageName);
                }
            });
        }
    }

    private void startTrackingPackageChanges() {
        com.android.internal.content.PackageMonitor monitor = new com.android.server.infra.AbstractMasterSystemService.AnonymousClass1(true);
        monitor.register(getContext(), (android.os.Looper) null, android.os.UserHandle.ALL, true);
    }

    protected void onServicePackageDataClearedMultiModeLocked(java.lang.String packageName, int userId) {
        if (this.verbose) {
            android.util.Slog.v(this.mTag, "onServicePackageDataClearedMultiModeLocked(" + userId + ")");
        }
    }

    protected void handlePackageRemovedMultiModeLocked(java.lang.String packageName, int userId) {
        if (this.verbose) {
            android.util.Slog.v(this.mTag, "handlePackageRemovedMultiModeLocked(" + userId + ")");
        }
    }

    protected void removeServiceFromCache(S service, int userId) {
        if (this.mServicesCacheList.get(userId) != null) {
            this.mServicesCacheList.get(userId).remove(service);
        }
    }

    protected void removeServiceFromMultiModeSettings(java.lang.String serviceComponentName, int userId) {
        java.lang.String serviceSettingsProperty = getServiceSettingsProperty();
        if (serviceSettingsProperty == null || this.mServiceNameResolver == null || !this.mServiceNameResolver.isConfiguredInMultipleMode()) {
            if (this.verbose) {
                android.util.Slog.v(this.mTag, "removeServiceFromSettings not implemented  for single backend implementation");
                return;
            }
            return;
        }
        java.lang.String[] settingComponentNames = this.mServiceNameResolver.getServiceNameList(userId);
        java.util.List<java.lang.String> remainingServices = new java.util.ArrayList<>();
        for (java.lang.String settingComponentName : settingComponentNames) {
            if (!settingComponentName.equals(serviceComponentName)) {
                remainingServices.add(settingComponentName);
            }
        }
        this.mServiceNameResolver.setServiceNameList(remainingServices, userId);
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
            android.content.ContentResolver resolver = com.android.server.infra.AbstractMasterSystemService.this.getContext().getContentResolver();
            java.lang.String serviceProperty = com.android.server.infra.AbstractMasterSystemService.this.getServiceSettingsProperty();
            if (serviceProperty != null) {
                resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor(serviceProperty), false, this, -1);
            }
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("user_setup_complete"), false, this, -1);
            com.android.server.infra.AbstractMasterSystemService.this.registerForExtraSettingsChanges(resolver, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            if (com.android.server.infra.AbstractMasterSystemService.this.verbose) {
                android.util.Slog.v(com.android.server.infra.AbstractMasterSystemService.this.mTag, "onChange(): uri=" + uri + ", userId=" + userId);
            }
            java.lang.String property = uri.getLastPathSegment();
            if (property == null) {
                return;
            }
            if (property.equals(com.android.server.infra.AbstractMasterSystemService.this.getServiceSettingsProperty()) || property.equals("user_setup_complete")) {
                synchronized (com.android.server.infra.AbstractMasterSystemService.this.mLock) {
                    com.android.server.infra.AbstractMasterSystemService.this.updateCachedServiceLocked(userId);
                }
                return;
            }
            com.android.server.infra.AbstractMasterSystemService.this.onSettingsChanged(userId, property);
        }
    }
}
