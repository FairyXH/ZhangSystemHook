package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
@java.lang.Deprecated
public class ComponentAliasResolver {
    private static final java.lang.String ALIAS_FILTER_ACTION = "com.android.intent.action.EXPERIMENTAL_IS_ALIAS";
    private static final java.lang.String ALIAS_FILTER_ACTION_ALT = "android.intent.action.EXPERIMENTAL_IS_ALIAS";
    private static final boolean DEBUG = true;
    private static final java.lang.String META_DATA_ALIAS_TARGET = "alias_target";
    private static final java.lang.String OPT_IN_PROPERTY = "com.android.EXPERIMENTAL_COMPONENT_ALIAS_OPT_IN";
    private static final int PACKAGE_QUERY_FLAGS = 4989056;
    private static final java.lang.String TAG = "ComponentAliasResolver";
    public static final long USE_EXPERIMENTAL_COMPONENT_ALIAS = 196254758;
    private final com.android.server.am.ActivityManagerService mAm;
    private final android.content.Context mContext;
    private boolean mEnabled;
    private boolean mEnabledByDeviceConfig;
    private java.lang.String mOverrideString;
    private com.android.server.compat.PlatformCompat mPlatformCompat;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<android.content.ComponentName, android.content.ComponentName> mFromTo = new android.util.ArrayMap<>();
    final com.android.internal.content.PackageMonitor mPackageMonitor = new com.android.internal.content.PackageMonitor() { // from class: com.android.server.am.ComponentAliasResolver.1
        public void onPackageModified(java.lang.String packageName) {
            com.android.server.am.ComponentAliasResolver.this.refresh();
        }

        public void onPackageAdded(java.lang.String packageName, int uid) {
            com.android.server.am.ComponentAliasResolver.this.refresh();
        }

        public void onPackageRemoved(java.lang.String packageName, int uid) {
            com.android.server.am.ComponentAliasResolver.this.refresh();
        }
    };

    public ComponentAliasResolver(com.android.server.am.ActivityManagerService service) {
        this.mAm = service;
        this.mContext = service.mContext;
    }

    public boolean isEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mEnabled;
        }
        return z;
    }

    public void onSystemReady(boolean enabledByDeviceConfig, java.lang.String overrides) {
        synchronized (this.mLock) {
            this.mPlatformCompat = (com.android.server.compat.PlatformCompat) android.os.ServiceManager.getService("platform_compat");
        }
        android.util.Slog.d(TAG, "Compat listener set.");
        update(enabledByDeviceConfig, overrides);
    }

    public void update(boolean enabledByDeviceConfig, java.lang.String overrides) {
        synchronized (this.mLock) {
            if (this.mPlatformCompat == null) {
                return;
            }
            if (this.mEnabled) {
                android.util.Slog.i(TAG, "Disabling component aliases...");
                com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.am.ComponentAliasResolver$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$update$0();
                    }
                });
            }
            this.mEnabled = false;
            this.mEnabledByDeviceConfig = enabledByDeviceConfig;
            this.mOverrideString = overrides;
            if (this.mEnabled) {
                refreshLocked();
            } else {
                this.mFromTo.clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$0() {
        this.mPackageMonitor.unregister();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refresh() {
        synchronized (this.mLock) {
            update(this.mEnabledByDeviceConfig, this.mOverrideString);
        }
    }

    private void refreshLocked() {
        android.util.Slog.d(TAG, "Refreshing aliases...");
        this.mFromTo.clear();
        loadFromMetadataLocked();
        loadOverridesLocked();
    }

    private void loadFromMetadataLocked() {
        android.util.Slog.d(TAG, "Scanning service aliases...");
        loadFromMetadataLockedInner(new android.content.Intent(ALIAS_FILTER_ACTION_ALT));
        loadFromMetadataLockedInner(new android.content.Intent(ALIAS_FILTER_ACTION));
    }

    private void loadFromMetadataLockedInner(android.content.Intent i) {
        java.util.List services = this.mContext.getPackageManager().queryIntentServicesAsUser(i, PACKAGE_QUERY_FLAGS, 0);
        extractAliasesLocked(services);
        android.util.Slog.d(TAG, "Scanning receiver aliases...");
        java.util.List receivers = this.mContext.getPackageManager().queryBroadcastReceiversAsUser(i, PACKAGE_QUERY_FLAGS, 0);
        extractAliasesLocked(receivers);
    }

    private boolean isEnabledForPackageLocked(java.lang.String packageName) {
        boolean enabled = false;
        try {
            android.content.pm.PackageManager.Property p = this.mContext.getPackageManager().getProperty(OPT_IN_PROPERTY, packageName);
            enabled = p.getBoolean();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
        if (!enabled) {
            android.util.Slog.w(TAG, "USE_EXPERIMENTAL_COMPONENT_ALIAS not enabled for " + packageName);
        }
        return enabled;
    }

    private static boolean validateAlias(android.content.ComponentName from, android.content.ComponentName to) {
        java.lang.String fromPackage = from.getPackageName();
        java.lang.String toPackage = to.getPackageName();
        if (java.util.Objects.equals(fromPackage, toPackage) || toPackage.startsWith(fromPackage + ".")) {
            return true;
        }
        android.util.Slog.w(TAG, "Invalid alias: " + from.flattenToShortString() + " -> " + to.flattenToShortString());
        return false;
    }

    private void validateAndAddAliasLocked(android.content.ComponentName from, android.content.ComponentName to) {
        android.util.Slog.d(TAG, "" + from.flattenToShortString() + " -> " + to.flattenToShortString());
        if (!validateAlias(from, to) || !isEnabledForPackageLocked(from.getPackageName()) || !isEnabledForPackageLocked(to.getPackageName())) {
            return;
        }
        this.mFromTo.put(from, to);
    }

    private void extractAliasesLocked(java.util.List<android.content.pm.ResolveInfo> components) {
        for (android.content.pm.ResolveInfo ri : components) {
            android.content.pm.ComponentInfo ci = ri.getComponentInfo();
            android.content.ComponentName from = ci.getComponentName();
            android.content.ComponentName to = unflatten(ci.metaData.getString(META_DATA_ALIAS_TARGET));
            if (to != null) {
                validateAndAddAliasLocked(from, to);
            }
        }
    }

    private void loadOverridesLocked() {
        android.content.ComponentName from;
        android.util.Slog.d(TAG, "Loading aliases overrides ...");
        for (java.lang.String line : this.mOverrideString.split("\\,+")) {
            java.lang.String[] fields = line.split("\\:+", 2);
            if (!android.text.TextUtils.isEmpty(fields[0]) && (from = unflatten(fields[0])) != null) {
                if (fields.length == 1) {
                    android.util.Slog.d(TAG, "" + from.flattenToShortString() + " [removed]");
                    this.mFromTo.remove(from);
                } else {
                    android.content.ComponentName to = unflatten(fields[1]);
                    if (to != null) {
                        validateAndAddAliasLocked(from, to);
                    }
                }
            }
        }
    }

    private static android.content.ComponentName unflatten(java.lang.String name) {
        android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(name);
        if (cn != null) {
            return cn;
        }
        android.util.Slog.e(TAG, "Invalid component name detected: " + name);
        return null;
    }

    public void dump(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("ACTIVITY MANAGER COMPONENT-ALIAS (dumpsys activity component-alias)");
            pw.print("  Enabled: ");
            pw.println(this.mEnabled);
            pw.println("  Aliases:");
            for (int i = 0; i < this.mFromTo.size(); i++) {
                android.content.ComponentName from = this.mFromTo.keyAt(i);
                android.content.ComponentName to = this.mFromTo.valueAt(i);
                pw.print("    ");
                pw.print(from.flattenToShortString());
                pw.print(" -> ");
                pw.print(to.flattenToShortString());
                pw.println();
            }
            pw.println();
        }
    }

    public static class Resolution<T> {
        public final T resolved;
        public final T source;

        public Resolution(T source, T resolved) {
            this.source = source;
            this.resolved = resolved;
        }

        public boolean isAlias() {
            return this.resolved != null;
        }

        public T getAlias() {
            if (isAlias()) {
                return this.source;
            }
            return null;
        }

        public T getTarget() {
            if (isAlias()) {
                return this.resolved;
            }
            return null;
        }
    }

    public com.android.server.am.ComponentAliasResolver.Resolution<android.content.ComponentName> resolveComponentAlias(java.util.function.Supplier<android.content.ComponentName> aliasSupplier) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (!this.mEnabled) {
                    return new com.android.server.am.ComponentAliasResolver.Resolution<>(null, null);
                }
                android.content.ComponentName alias = aliasSupplier.get();
                android.content.ComponentName target = this.mFromTo.get(alias);
                if (target != null) {
                    java.lang.Exception stacktrace = null;
                    if (android.util.Log.isLoggable(TAG, 2)) {
                        stacktrace = new java.lang.RuntimeException("STACKTRACE");
                    }
                    android.util.Slog.d(TAG, "Alias resolved: " + alias.flattenToShortString() + " -> " + target.flattenToShortString(), stacktrace);
                }
                return new com.android.server.am.ComponentAliasResolver.Resolution<>(alias, target);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public com.android.server.am.ComponentAliasResolver.Resolution<android.content.ComponentName> resolveService(final android.content.Intent service, final java.lang.String resolvedType, final int packageFlags, final int userId, final int callingUid) {
        com.android.server.am.ComponentAliasResolver.Resolution<android.content.ComponentName> result = resolveComponentAlias(new java.util.function.Supplier() { // from class: com.android.server.am.ComponentAliasResolver$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.am.ComponentAliasResolver.lambda$resolveService$1(service, resolvedType, packageFlags, userId, callingUid);
            }
        });
        if (result != null && result.isAlias()) {
            service.setOriginalIntent(new android.content.Intent(service));
            service.setPackage(null);
            service.setComponent(result.getTarget());
        }
        return result;
    }

    static /* synthetic */ android.content.ComponentName lambda$resolveService$1(android.content.Intent service, java.lang.String resolvedType, int packageFlags, int userId, int callingUid) {
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        android.content.pm.ResolveInfo rInfo = pmi.resolveService(service, resolvedType, packageFlags, userId, callingUid);
        android.content.pm.ServiceInfo sInfo = rInfo != null ? rInfo.serviceInfo : null;
        if (sInfo == null) {
            return null;
        }
        return new android.content.ComponentName(sInfo.applicationInfo.packageName, sInfo.name);
    }

    public com.android.server.am.ComponentAliasResolver.Resolution<android.content.pm.ResolveInfo> resolveReceiver(android.content.Intent intent, final android.content.pm.ResolveInfo receiver, java.lang.String resolvedType, long packageFlags, int userId, int callingUid, int callingPid) {
        com.android.server.am.ComponentAliasResolver.Resolution<android.content.ComponentName> resolution = resolveComponentAlias(new java.util.function.Supplier() { // from class: com.android.server.am.ComponentAliasResolver$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return receiver.activityInfo.getComponentName();
            }
        });
        android.content.ComponentName target = resolution.getTarget();
        if (target == null) {
            return new com.android.server.am.ComponentAliasResolver.Resolution<>(receiver, null);
        }
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        android.content.Intent i = new android.content.Intent(intent);
        i.setPackage(null);
        i.setComponent(resolution.getTarget());
        java.util.List<android.content.pm.ResolveInfo> resolved = pmi.queryIntentReceivers(i, resolvedType, packageFlags, callingUid, callingPid, userId, true);
        if (resolved != null && resolved.size() != 0) {
            return new com.android.server.am.ComponentAliasResolver.Resolution<>(receiver, resolved.get(0));
        }
        android.util.Slog.w(TAG, "Alias target " + target.flattenToShortString() + " not found");
        return null;
    }
}
