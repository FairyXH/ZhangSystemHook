package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ConditionProviders extends com.android.server.notification.ManagedServices {
    static final java.lang.String TAG_ENABLED_DND_APPS = "dnd_apps";
    private com.android.server.notification.ConditionProviders.Callback mCallback;
    private final java.util.ArrayList<com.android.server.notification.ConditionProviders.ConditionRecord> mRecords;
    private final android.util.ArraySet<java.lang.String> mSystemConditionProviderNames;
    private final android.util.ArraySet<com.android.server.notification.SystemConditionProviderService> mSystemConditionProviders;

    public interface Callback {
        void onBootComplete();

        void onConditionChanged(android.net.Uri uri, android.service.notification.Condition condition);

        void onServiceAdded(android.content.ComponentName componentName);

        void onUserSwitched();
    }

    public ConditionProviders(android.content.Context context, com.android.server.notification.ManagedServices.UserProfiles userProfiles, android.content.pm.IPackageManager pm) {
        super(context, new java.lang.Object(), userProfiles, pm);
        this.mRecords = new java.util.ArrayList<>();
        this.mSystemConditionProviders = new android.util.ArraySet<>();
        this.mSystemConditionProviderNames = safeSet(com.android.server.notification.PropConfig.getStringArray(this.mContext, "system.condition.providers", android.R.array.config_sfps_sensor_props));
        this.mApprovalLevel = 0;
    }

    public void setCallback(com.android.server.notification.ConditionProviders.Callback callback) {
        this.mCallback = callback;
    }

    public boolean isSystemProviderEnabled(java.lang.String path) {
        return this.mSystemConditionProviderNames.contains(path);
    }

    public void addSystemProvider(com.android.server.notification.SystemConditionProviderService service) {
        this.mSystemConditionProviders.add(service);
        service.attachBase(this.mContext);
        registerSystemService(service.asInterface(), service.getComponent(), 0, 1000);
    }

    public java.lang.Iterable<com.android.server.notification.SystemConditionProviderService> getSystemProviders() {
        return this.mSystemConditionProviders;
    }

    @Override // com.android.server.notification.ManagedServices
    protected android.util.ArrayMap<java.lang.Boolean, java.util.ArrayList<android.content.ComponentName>> resetComponents(java.lang.String packageName, int userId) {
        resetPackage(packageName, userId);
        android.util.ArrayMap<java.lang.Boolean, java.util.ArrayList<android.content.ComponentName>> changes = new android.util.ArrayMap<>();
        changes.put(true, new java.util.ArrayList<>(0));
        changes.put(false, new java.util.ArrayList<>(0));
        return changes;
    }

    boolean resetPackage(java.lang.String packageName, int userId) {
        boolean isAllowed = super.isPackageOrComponentAllowed(packageName, userId);
        boolean isDefault = super.isDefaultComponentOrPackage(packageName);
        if (!isAllowed && isDefault) {
            setPackageOrComponentEnabled(packageName, userId, true, true);
        }
        if (isAllowed && !isDefault) {
            setPackageOrComponentEnabled(packageName, userId, true, false);
        }
        if (!isAllowed && isDefault) {
            return true;
        }
        return false;
    }

    @Override // com.android.server.notification.ManagedServices
    void writeDefaults(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
        synchronized (this.mDefaultsLock) {
            java.lang.String defaults = java.lang.String.join(":", this.mDefaultPackages);
            out.attribute((java.lang.String) null, "defaults", defaults);
        }
    }

    @Override // com.android.server.notification.ManagedServices
    protected com.android.server.notification.ManagedServices.Config getConfig() {
        com.android.server.notification.ManagedServices.Config c = new com.android.server.notification.ManagedServices.Config();
        c.caption = "condition provider";
        c.serviceInterface = "android.service.notification.ConditionProviderService";
        c.secureSettingName = null;
        c.xmlTag = TAG_ENABLED_DND_APPS;
        c.secondarySettingName = "enabled_notification_listeners";
        c.bindPermission = "android.permission.BIND_CONDITION_PROVIDER_SERVICE";
        c.settingsAction = "android.settings.ACTION_CONDITION_PROVIDER_SETTINGS";
        c.clientLabel = android.R.string.config_appsNotReportingCrashes;
        return c;
    }

    @Override // com.android.server.notification.ManagedServices
    public void dump(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        super.dump(pw, filter);
        synchronized (this.mMutex) {
            pw.print("    mRecords(");
            pw.print(this.mRecords.size());
            pw.println("):");
            for (int i = 0; i < this.mRecords.size(); i++) {
                com.android.server.notification.ConditionProviders.ConditionRecord r = this.mRecords.get(i);
                if (filter == null || filter.matches(r.component)) {
                    pw.print("      ");
                    pw.println(r);
                    java.lang.String countdownDesc = com.android.server.notification.CountdownConditionProvider.tryParseDescription(r.id);
                    if (countdownDesc != null) {
                        pw.print("        (");
                        pw.print(countdownDesc);
                        pw.println(")");
                    }
                }
            }
        }
        pw.print("    mSystemConditionProviders: ");
        pw.println(this.mSystemConditionProviderNames);
        for (int i2 = 0; i2 < this.mSystemConditionProviders.size(); i2++) {
            this.mSystemConditionProviders.valueAt(i2).dump(pw, filter);
        }
    }

    @Override // com.android.server.notification.ManagedServices
    protected android.os.IInterface asInterface(android.os.IBinder binder) {
        return android.service.notification.IConditionProvider.Stub.asInterface(binder);
    }

    @Override // com.android.server.notification.ManagedServices
    protected boolean checkType(android.os.IInterface service) {
        return service instanceof android.service.notification.IConditionProvider;
    }

    @Override // com.android.server.notification.ManagedServices
    public void onBootPhaseAppsCanStart() {
        super.onBootPhaseAppsCanStart();
        for (int i = 0; i < this.mSystemConditionProviders.size(); i++) {
            this.mSystemConditionProviders.valueAt(i).onBootComplete();
        }
        if (this.mCallback != null) {
            this.mCallback.onBootComplete();
        }
    }

    @Override // com.android.server.notification.ManagedServices
    public void onUserSwitched(int user) {
        super.onUserSwitched(user);
        if (this.mCallback != null) {
            this.mCallback.onUserSwitched();
        }
    }

    @Override // com.android.server.notification.ManagedServices
    protected void onServiceAdded(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
        android.service.notification.IConditionProvider provider = provider(info);
        try {
            provider.onConnected();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(this.TAG, "can't connect to service " + info, e);
        }
        if (this.mCallback != null) {
            this.mCallback.onServiceAdded(info.component);
        }
    }

    @Override // com.android.server.notification.ManagedServices
    protected void ensureFilters(android.content.pm.ServiceInfo si, int userId) {
    }

    @Override // com.android.server.notification.ManagedServices
    protected void loadDefaultsFromConfig() {
        java.lang.String defaultDndAccess = this.mContext.getResources().getString(android.R.string.config_defaultNearbySharingComponent);
        if (defaultDndAccess != null) {
            java.lang.String[] dnds = defaultDndAccess.split(":");
            for (int i = 0; i < dnds.length; i++) {
                if (!android.text.TextUtils.isEmpty(dnds[i])) {
                    addDefaultComponentOrPackage(dnds[i]);
                }
            }
        }
    }

    @Override // com.android.server.notification.ManagedServices
    protected void onServiceRemovedLocked(com.android.server.notification.ManagedServices.ManagedServiceInfo removed) {
        if (removed == null) {
            return;
        }
        for (int i = this.mRecords.size() - 1; i >= 0; i--) {
            com.android.server.notification.ConditionProviders.ConditionRecord r = this.mRecords.get(i);
            if (r.component.equals(removed.component)) {
                this.mRecords.remove(i);
            }
        }
    }

    @Override // com.android.server.notification.ManagedServices
    public void onPackagesChanged(boolean removingPackage, java.lang.String[] pkgList, int[] uid) {
        if (removingPackage) {
            android.app.INotificationManager inm = android.app.NotificationManager.getService();
            if (pkgList != null && pkgList.length > 0) {
                for (java.lang.String pkgName : pkgList) {
                    try {
                        inm.removeAutomaticZenRules(pkgName, false);
                        inm.setNotificationPolicyAccessGranted(pkgName, false);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(this.TAG, "Failed to clean up rules for " + pkgName, e);
                    }
                }
            }
        }
        super.onPackagesChanged(removingPackage, pkgList, uid);
    }

    @Override // com.android.server.notification.ManagedServices
    protected boolean isValidEntry(java.lang.String packageOrComponent, int userId) {
        return true;
    }

    @Override // com.android.server.notification.ManagedServices
    protected boolean allowRebindForParentUser() {
        return true;
    }

    @Override // com.android.server.notification.ManagedServices
    protected java.lang.String getRequiredPermission() {
        return null;
    }

    public com.android.server.notification.ManagedServices.ManagedServiceInfo checkServiceToken(android.service.notification.IConditionProvider provider) {
        com.android.server.notification.ManagedServices.ManagedServiceInfo managedServiceInfoCheckServiceTokenLocked;
        synchronized (this.mMutex) {
            managedServiceInfoCheckServiceTokenLocked = checkServiceTokenLocked(provider);
        }
        return managedServiceInfoCheckServiceTokenLocked;
    }

    private android.service.notification.Condition[] getValidConditions(java.lang.String pkg, android.service.notification.Condition[] conditions) {
        if (conditions == null || conditions.length == 0) {
            return null;
        }
        int N = conditions.length;
        android.util.ArrayMap<android.net.Uri, android.service.notification.Condition> valid = new android.util.ArrayMap<>(N);
        for (int i = 0; i < N; i++) {
            if (conditions[i] == null) {
                android.util.Slog.w(this.TAG, "Ignoring null condition from " + pkg);
            } else {
                android.net.Uri id = conditions[i].id;
                if (valid.containsKey(id)) {
                    android.util.Slog.w(this.TAG, "Ignoring condition from " + pkg + " for duplicate id: " + id);
                } else {
                    valid.put(id, conditions[i]);
                }
            }
        }
        int i2 = valid.size();
        if (i2 == 0) {
            return null;
        }
        if (valid.size() == N) {
            return conditions;
        }
        android.service.notification.Condition[] rt = new android.service.notification.Condition[valid.size()];
        for (int i3 = 0; i3 < rt.length; i3++) {
            rt[i3] = valid.valueAt(i3);
        }
        return rt;
    }

    private com.android.server.notification.ConditionProviders.ConditionRecord getRecordLocked(android.net.Uri id, android.content.ComponentName component, boolean create) {
        if (id == null || component == null) {
            return null;
        }
        int N = this.mRecords.size();
        for (int i = 0; i < N; i++) {
            com.android.server.notification.ConditionProviders.ConditionRecord r = this.mRecords.get(i);
            if (r.id.equals(id) && r.component.equals(component)) {
                return r;
            }
        }
        if (!create) {
            return null;
        }
        com.android.server.notification.ConditionProviders.ConditionRecord r2 = new com.android.server.notification.ConditionProviders.ConditionRecord(id, component);
        this.mRecords.add(r2);
        return r2;
    }

    public void notifyConditions(java.lang.String pkg, com.android.server.notification.ManagedServices.ManagedServiceInfo info, android.service.notification.Condition[] conditions) {
        synchronized (this.mMutex) {
            if (this.DEBUG) {
                android.util.Slog.d(this.TAG, "notifyConditions pkg=" + pkg + " info=" + info + " conditions=" + (conditions == null ? null : java.util.Arrays.asList(conditions)));
            }
            android.service.notification.Condition[] conditions2 = getValidConditions(pkg, conditions);
            if (conditions2 != null && conditions2.length != 0) {
                for (android.service.notification.Condition c : conditions2) {
                    com.android.server.notification.ConditionProviders.ConditionRecord r = getRecordLocked(c.id, info.component, true);
                    r.info = info;
                    if (android.app.Flags.modesUi()) {
                        if (r.condition != null && r.condition.source == 3) {
                            if (r.condition.state == 1 && c.state == 1) {
                                r.condition = c;
                            }
                        } else {
                            r.condition = c;
                        }
                    } else {
                        r.condition = c;
                    }
                }
                for (android.service.notification.Condition c2 : conditions2) {
                    if (this.mCallback != null) {
                        this.mCallback.onConditionChanged(c2.id, c2);
                    }
                }
            }
        }
    }

    public android.service.notification.IConditionProvider findConditionProvider(android.content.ComponentName component) {
        if (component == null) {
            return null;
        }
        for (com.android.server.notification.ManagedServices.ManagedServiceInfo service : getServices()) {
            if (component.equals(service.component)) {
                return provider(service);
            }
        }
        return null;
    }

    public android.service.notification.Condition findCondition(android.content.ComponentName component, android.net.Uri conditionId) {
        android.service.notification.Condition condition;
        if (component == null || conditionId == null) {
            return null;
        }
        synchronized (this.mMutex) {
            com.android.server.notification.ConditionProviders.ConditionRecord r = getRecordLocked(conditionId, component, false);
            condition = r != null ? r.condition : null;
        }
        return condition;
    }

    public void ensureRecordExists(android.content.ComponentName component, android.net.Uri conditionId, android.service.notification.IConditionProvider provider) {
        synchronized (this.mMutex) {
            com.android.server.notification.ConditionProviders.ConditionRecord r = getRecordLocked(conditionId, component, true);
            if (r.info == null) {
                r.info = checkServiceTokenLocked(provider);
            }
        }
    }

    public boolean subscribeIfNecessary(android.content.ComponentName component, android.net.Uri conditionId) {
        synchronized (this.mMutex) {
            com.android.server.notification.ConditionProviders.ConditionRecord r = getRecordLocked(conditionId, component, false);
            if (r == null) {
                android.util.Slog.w(this.TAG, "Unable to subscribe to " + component + " " + conditionId);
                return false;
            }
            if (r.subscribed) {
                return true;
            }
            subscribeLocked(r);
            return r.subscribed;
        }
    }

    public void unsubscribeIfNecessary(android.content.ComponentName component, android.net.Uri conditionId) {
        synchronized (this.mMutex) {
            com.android.server.notification.ConditionProviders.ConditionRecord r = getRecordLocked(conditionId, component, false);
            if (r == null) {
                android.util.Slog.w(this.TAG, "Unable to unsubscribe to " + component + " " + conditionId);
            } else if (r.subscribed) {
                unsubscribeLocked(r);
            }
        }
    }

    private void subscribeLocked(com.android.server.notification.ConditionProviders.ConditionRecord r) {
        if (this.DEBUG) {
            android.util.Slog.d(this.TAG, "subscribeLocked " + r);
        }
        android.service.notification.IConditionProvider provider = provider(r);
        android.os.RemoteException re = null;
        if (provider != null) {
            try {
                android.util.Slog.d(this.TAG, "Subscribing to " + r.id + " with " + r.component);
                provider.onSubscribe(r.id);
                r.subscribed = true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(this.TAG, "Error subscribing to " + r, e);
                re = e;
            }
        }
        com.android.server.notification.ZenLog.traceSubscribe(r != null ? r.id : null, provider, re);
    }

    @java.lang.SafeVarargs
    private static <T> android.util.ArraySet<T> safeSet(T... items) {
        android.util.ArraySet<T> rt = new android.util.ArraySet<>();
        if (items == null || items.length == 0) {
            return rt;
        }
        for (T item : items) {
            if (item != null) {
                rt.add(item);
            }
        }
        return rt;
    }

    private void unsubscribeLocked(com.android.server.notification.ConditionProviders.ConditionRecord r) {
        if (this.DEBUG) {
            android.util.Slog.d(this.TAG, "unsubscribeLocked " + r);
        }
        android.service.notification.IConditionProvider provider = provider(r);
        android.os.RemoteException re = null;
        if (provider != null) {
            try {
                provider.onUnsubscribe(r.id);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(this.TAG, "Error unsubscribing to " + r, e);
                re = e;
            }
            r.subscribed = false;
        }
        com.android.server.notification.ZenLog.traceUnsubscribe(r != null ? r.id : null, provider, re);
    }

    private static android.service.notification.IConditionProvider provider(com.android.server.notification.ConditionProviders.ConditionRecord r) {
        if (r == null) {
            return null;
        }
        return provider(r.info);
    }

    private static android.service.notification.IConditionProvider provider(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
        if (info == null) {
            return null;
        }
        return info.service;
    }

    void resetDefaultFromConfig() {
        synchronized (this.mDefaultsLock) {
            this.mDefaultComponents.clear();
            this.mDefaultPackages.clear();
        }
        loadDefaultsFromConfig();
    }

    boolean removeDefaultFromConfig(int userId) {
        boolean removed = false;
        java.lang.String defaultDndDenied = this.mContext.getResources().getString(android.R.string.config_defaultNearbySharingSliceUri);
        if (defaultDndDenied != null) {
            java.lang.String[] dnds = defaultDndDenied.split(":");
            for (int i = 0; i < dnds.length; i++) {
                if (!android.text.TextUtils.isEmpty(dnds[i])) {
                    removed |= removePackageFromApprovedLists(userId, dnds[i], "remove from config");
                }
            }
        }
        return removed;
    }

    private boolean removePackageFromApprovedLists(int userId, java.lang.String pkg, java.lang.String reason) throws java.lang.Throwable {
        boolean removed = false;
        synchronized (this.mApproved) {
            try {
                android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedByType = this.mApproved.get(java.lang.Integer.valueOf(userId));
                if (approvedByType != null) {
                    int approvedByTypeSize = approvedByType.size();
                    for (int i = 0; i < approvedByTypeSize; i++) {
                        android.util.ArraySet<java.lang.String> approved = approvedByType.valueAt(i);
                        int approvedSize = approved.size();
                        for (int j = approvedSize - 1; j >= 0; j--) {
                            java.lang.String packageOrComponent = approved.valueAt(j);
                            java.lang.String packageName = getPackageName(packageOrComponent);
                            try {
                                if (android.text.TextUtils.equals(pkg, packageName)) {
                                    approved.removeAt(j);
                                    removed = true;
                                    if (this.DEBUG) {
                                        try {
                                            android.util.Slog.v(this.TAG, "Removing " + packageOrComponent + " from approved list; " + reason);
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            throw th;
                                        }
                                    }
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        }
                    }
                }
                return removed;
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    private static class ConditionRecord {
        public final android.content.ComponentName component;
        public android.service.notification.Condition condition;
        public final android.net.Uri id;
        public com.android.server.notification.ManagedServices.ManagedServiceInfo info;
        public boolean subscribed;

        private ConditionRecord(android.net.Uri id, android.content.ComponentName component) {
            this.id = id;
            this.component = component;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("ConditionRecord[id=").append(this.id).append(",component=").append(this.component).append(",subscribed=").append(this.subscribed);
            return sb.append(']').toString();
        }
    }
}
