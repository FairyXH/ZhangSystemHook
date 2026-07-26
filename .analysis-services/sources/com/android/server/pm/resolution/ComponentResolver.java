package com.android.server.pm.resolution;

/* JADX INFO: loaded from: classes2.dex */
public class ComponentResolver extends com.android.server.pm.resolution.ComponentResolverLocked implements com.android.server.utils.Snappable<com.android.server.pm.resolution.ComponentResolverApi> {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_FILTERS = false;
    private static final boolean DEBUG_SHOW_INFO = false;
    private static final java.util.Set<java.lang.String> PROTECTED_ACTIONS = new android.util.ArraySet();
    public static final java.util.Comparator<android.content.pm.ResolveInfo> RESOLVE_PRIORITY_SORTER;
    private static final java.lang.String TAG = "PackageManager";
    private static final com.android.server.pm.resolution.IComponentResolverExt.IStaticExt mStaticComponentResolverExt;
    private final com.android.server.pm.resolution.IComponentResolverExt mComponentResolverExt;
    boolean mDeferProtectedFilters;
    java.util.List<android.util.Pair<com.android.internal.pm.pkg.component.ParsedMainComponent, com.android.internal.pm.pkg.component.ParsedIntentInfo>> mProtectedFilters;
    final com.android.server.utils.SnapshotCache<com.android.server.pm.resolution.ComponentResolverApi> mSnapshot;

    private void onChanged() {
        dispatchChange(this);
    }

    static {
        PROTECTED_ACTIONS.add("android.intent.action.SEND");
        PROTECTED_ACTIONS.add("android.intent.action.SENDTO");
        PROTECTED_ACTIONS.add("android.intent.action.SEND_MULTIPLE");
        PROTECTED_ACTIONS.add("android.intent.action.VIEW");
        RESOLVE_PRIORITY_SORTER = new java.util.Comparator() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda4
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.pm.resolution.ComponentResolver.lambda$static$0((android.content.pm.ResolveInfo) obj, (android.content.pm.ResolveInfo) obj2);
            }
        };
        mStaticComponentResolverExt = (com.android.server.pm.resolution.IComponentResolverExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.resolution.IComponentResolverExt.IStaticExt.class).create();
    }

    static /* synthetic */ int lambda$static$0(android.content.pm.ResolveInfo r1, android.content.pm.ResolveInfo r2) {
        int v1 = r1.priority;
        int v2 = r2.priority;
        if (v1 != v2) {
            return v1 > v2 ? -1 : 1;
        }
        int v12 = r1.preferredOrder;
        int v22 = r2.preferredOrder;
        if (v12 != v22) {
            return v12 > v22 ? -1 : 1;
        }
        if (r1.isDefault != r2.isDefault) {
            return r1.isDefault ? -1 : 1;
        }
        int v13 = r1.match;
        int v23 = r2.match;
        if (v13 != v23) {
            return v13 > v23 ? -1 : 1;
        }
        if (r1.system != r2.system) {
            return r1.system ? -1 : 1;
        }
        if (r1.activityInfo != null) {
            return r1.activityInfo.packageName.compareTo(r2.activityInfo.packageName);
        }
        if (r1.serviceInfo != null) {
            return r1.serviceInfo.packageName.compareTo(r2.serviceInfo.packageName);
        }
        if (r1.providerInfo != null) {
            return r1.providerInfo.packageName.compareTo(r2.providerInfo.packageName);
        }
        return 0;
    }

    public ComponentResolver(com.android.server.pm.UserManagerService userManager, final com.android.server.pm.UserNeedsBadgingCache userNeedsBadgingCache) {
        super(userManager);
        this.mDeferProtectedFilters = true;
        this.mComponentResolverExt = (com.android.server.pm.resolution.IComponentResolverExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.resolution.IComponentResolverExt.class).base(this).create();
        this.mActivities = new com.android.server.pm.resolution.ComponentResolver.ActivityIntentResolver(userManager, userNeedsBadgingCache);
        this.mProviders = new com.android.server.pm.resolution.ComponentResolver.ProviderIntentResolver(userManager);
        this.mReceivers = new com.android.server.pm.resolution.ComponentResolver.ReceiverIntentResolver(userManager, userNeedsBadgingCache);
        this.mServices = new com.android.server.pm.resolution.ComponentResolver.ServiceIntentResolver(userManager);
        this.mProvidersByAuthority = new android.util.ArrayMap<>();
        this.mDeferProtectedFilters = true;
        this.mSnapshot = new com.android.server.utils.SnapshotCache<com.android.server.pm.resolution.ComponentResolverApi>(this, this) { // from class: com.android.server.pm.resolution.ComponentResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.resolution.ComponentResolverApi createSnapshot() {
                com.android.server.pm.resolution.ComponentResolverSnapshot componentResolverSnapshot;
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.resolution.ComponentResolver.this.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        componentResolverSnapshot = new com.android.server.pm.resolution.ComponentResolverSnapshot(com.android.server.pm.resolution.ComponentResolver.this, userNeedsBadgingCache);
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return componentResolverSnapshot;
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.resolution.ComponentResolverApi snapshot() {
        return this.mSnapshot.snapshot();
    }

    public void addAllComponents(com.android.server.pm.pkg.AndroidPackage pkg, boolean chatty, java.lang.String setupWizardPackage, com.android.server.pm.Computer computer) {
        java.util.ArrayList<android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo>> newIntents = new java.util.ArrayList<>();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                addActivitiesLocked(computer, pkg, newIntents, chatty);
                addReceiversLocked(computer, pkg, chatty);
                addProvidersLocked(computer, pkg, chatty);
                addServicesLocked(computer, pkg, chatty);
                onChanged();
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        for (int i = newIntents.size() - 1; i >= 0; i--) {
            android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair = newIntents.get(i);
            com.android.server.pm.pkg.PackageStateInternal disabledPkgSetting = computer.getDisabledSystemPackage(((com.android.internal.pm.pkg.component.ParsedActivity) pair.first).getPackageName());
            java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> activities = null;
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg2 = disabledPkgSetting == null ? null : disabledPkgSetting.getPkg();
            if (pkg2 != null) {
                activities = pkg2.getActivities();
            }
            java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> systemActivities = activities;
            adjustPriority(computer, systemActivities, (com.android.internal.pm.pkg.component.ParsedActivity) pair.first, (com.android.internal.pm.pkg.component.ParsedIntentInfo) pair.second, setupWizardPackage);
            onChanged();
        }
    }

    public void removeAllComponents(com.android.server.pm.pkg.AndroidPackage pkg, boolean chatty) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                removeAllComponentsLocked(pkg, chatty);
                onChanged();
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    public void fixProtectedFilterPriorities(java.lang.String setupWizardPackage) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                if (!this.mDeferProtectedFilters) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                this.mDeferProtectedFilters = false;
                if (this.mProtectedFilters != null && this.mProtectedFilters.size() != 0) {
                    java.util.List<android.util.Pair<com.android.internal.pm.pkg.component.ParsedMainComponent, com.android.internal.pm.pkg.component.ParsedIntentInfo>> protectedFilters = this.mProtectedFilters;
                    this.mProtectedFilters = null;
                    for (int i = protectedFilters.size() - 1; i >= 0; i--) {
                        android.util.Pair<com.android.internal.pm.pkg.component.ParsedMainComponent, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair = protectedFilters.get(i);
                        com.android.internal.pm.pkg.component.ParsedMainComponent component = (com.android.internal.pm.pkg.component.ParsedMainComponent) pair.first;
                        com.android.internal.pm.pkg.component.ParsedIntentInfo intentInfo = (com.android.internal.pm.pkg.component.ParsedIntentInfo) pair.second;
                        android.content.IntentFilter filter = intentInfo.getIntentFilter();
                        java.lang.String packageName = component.getPackageName();
                        component.getClassName();
                        if (!packageName.equals(setupWizardPackage)) {
                            filter.setPriority(0);
                        }
                    }
                    onChanged();
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    private void addActivitiesLocked(com.android.server.pm.Computer computer, com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo>> newIntents, boolean chatty) {
        int activitiesSize = com.android.internal.util.ArrayUtils.size(pkg.getActivities());
        java.lang.StringBuilder r = null;
        for (int i = 0; i < activitiesSize; i++) {
            com.android.internal.pm.pkg.component.ParsedActivity a = (com.android.internal.pm.pkg.component.ParsedActivity) pkg.getActivities().get(i);
            this.mActivities.addActivity(computer, a, com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY, newIntents);
            this.mComponentResolverExt.onAddActivitiesLocked(a, pkg);
            if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING && chatty) {
                if (r == null) {
                    r = new java.lang.StringBuilder(256);
                } else {
                    r.append(' ');
                }
                r.append(a.getName());
            }
        }
        if (!com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING || !chatty) {
            return;
        }
        android.util.Log.d(TAG, "  Activities: " + (r == null ? "<NONE>" : r));
    }

    private void addProvidersLocked(com.android.server.pm.Computer computer, com.android.server.pm.pkg.AndroidPackage pkg, boolean chatty) {
        int providersSize = com.android.internal.util.ArrayUtils.size(pkg.getProviders());
        java.lang.StringBuilder r = null;
        for (int i = 0; i < providersSize; i++) {
            com.android.internal.pm.pkg.component.ParsedProvider p = (com.android.internal.pm.pkg.component.ParsedProvider) pkg.getProviders().get(i);
            this.mProviders.addProvider(computer, p);
            this.mComponentResolverExt.onAddProvidersLocked(p, pkg);
            if (p.getAuthority() != null) {
                java.lang.String[] names = p.getAuthority().split(";");
                android.content.ComponentName componentName = null;
                com.android.internal.pm.pkg.component.ComponentMutateUtils.setAuthority(p, (java.lang.String) null);
                int j = 0;
                while (j < names.length) {
                    if (j == 1 && p.isSyncable()) {
                        p = new com.android.internal.pm.pkg.component.ParsedProviderImpl(p);
                        com.android.internal.pm.pkg.component.ComponentMutateUtils.setSyncable(p, false);
                    }
                    if (!this.mProvidersByAuthority.containsKey(names[j]) || this.mComponentResolverExt.shouldOverrideProviderByAuthority(names[j], pkg, this.mProvidersByAuthority.get(names[j]))) {
                        this.mProvidersByAuthority.put(names[j], p);
                        if (p.getAuthority() == null) {
                            com.android.internal.pm.pkg.component.ComponentMutateUtils.setAuthority(p, names[j]);
                        } else {
                            com.android.internal.pm.pkg.component.ComponentMutateUtils.setAuthority(p, p.getAuthority() + ";" + names[j]);
                        }
                        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING && chatty) {
                            android.util.Log.d(TAG, "Registered content provider: " + names[j] + ", className = " + p.getName() + ", isSyncable = " + p.isSyncable());
                        }
                    } else {
                        com.android.internal.pm.pkg.component.ParsedProvider other = this.mProvidersByAuthority.get(names[j]);
                        android.content.ComponentName component = (other == null || other.getComponentName() == null) ? componentName : other.getComponentName();
                        java.lang.String packageName = component != null ? component.getPackageName() : "?";
                        android.util.Slog.w(TAG, "Skipping provider name " + names[j] + " (in package " + pkg.getPackageName() + "): name already used by " + packageName);
                    }
                    j++;
                    componentName = null;
                }
            }
            if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING && chatty) {
                if (r == null) {
                    r = new java.lang.StringBuilder(256);
                } else {
                    r.append(' ');
                }
                r.append(p.getName());
            }
        }
        if (!com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING || !chatty) {
            return;
        }
        android.util.Log.d(TAG, "  Providers: " + (r == null ? "<NONE>" : r));
    }

    private void addReceiversLocked(com.android.server.pm.Computer computer, com.android.server.pm.pkg.AndroidPackage pkg, boolean chatty) {
        int receiversSize = com.android.internal.util.ArrayUtils.size(pkg.getReceivers());
        java.lang.StringBuilder r = null;
        for (int i = 0; i < receiversSize; i++) {
            com.android.internal.pm.pkg.component.ParsedActivity a = (com.android.internal.pm.pkg.component.ParsedActivity) pkg.getReceivers().get(i);
            this.mReceivers.addActivity(computer, a, "receiver", null);
            this.mComponentResolverExt.onAddReceiversLocked(a, pkg);
            if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING && chatty) {
                if (r == null) {
                    r = new java.lang.StringBuilder(256);
                } else {
                    r.append(' ');
                }
                r.append(a.getName());
            }
        }
        if (!com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING || !chatty) {
            return;
        }
        android.util.Log.d(TAG, "  Receivers: " + (r == null ? "<NONE>" : r));
    }

    private void addServicesLocked(com.android.server.pm.Computer computer, com.android.server.pm.pkg.AndroidPackage pkg, boolean chatty) {
        int servicesSize = com.android.internal.util.ArrayUtils.size(pkg.getServices());
        java.lang.StringBuilder r = null;
        for (int i = 0; i < servicesSize; i++) {
            com.android.internal.pm.pkg.component.ParsedService s = (com.android.internal.pm.pkg.component.ParsedService) pkg.getServices().get(i);
            this.mServices.addService(computer, s);
            this.mComponentResolverExt.onAddServicesLocked(s, pkg);
            if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING && chatty) {
                if (r == null) {
                    r = new java.lang.StringBuilder(256);
                } else {
                    r.append(' ');
                }
                r.append(s.getName());
            }
        }
        if (!com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING || !chatty) {
            return;
        }
        android.util.Log.d(TAG, "  Services: " + (r == null ? "<NONE>" : r));
    }

    private static <T> void getIntentListSubset(java.util.List<com.android.internal.pm.pkg.component.ParsedIntentInfo> intentList, java.util.function.Function<android.content.IntentFilter, java.util.Iterator<T>> generator, java.util.Iterator<T> searchIterator) {
        while (searchIterator.hasNext() && intentList.size() != 0) {
            T searchAction = searchIterator.next();
            java.util.Iterator<com.android.internal.pm.pkg.component.ParsedIntentInfo> intentIter = intentList.iterator();
            while (intentIter.hasNext()) {
                com.android.internal.pm.pkg.component.ParsedIntentInfo intentInfo = intentIter.next();
                boolean selectionFound = false;
                java.util.Iterator<T> intentSelectionIter = generator.apply(intentInfo.getIntentFilter());
                while (true) {
                    if (intentSelectionIter != null && intentSelectionIter.hasNext()) {
                        T intentSelection = intentSelectionIter.next();
                        if (intentSelection != null && intentSelection.equals(searchAction)) {
                            selectionFound = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (!selectionFound) {
                    intentIter.remove();
                }
            }
        }
    }

    private static boolean isProtectedAction(android.content.IntentFilter filter) {
        java.util.Iterator<java.lang.String> actionsIter = filter.actionsIterator();
        while (actionsIter != null && actionsIter.hasNext()) {
            java.lang.String filterAction = actionsIter.next();
            if (PROTECTED_ACTIONS.contains(filterAction)) {
                return true;
            }
        }
        return false;
    }

    private static com.android.internal.pm.pkg.component.ParsedActivity findMatchingActivity(java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> activityList, com.android.internal.pm.pkg.component.ParsedActivity activityInfo) {
        for (com.android.internal.pm.pkg.component.ParsedActivity sysActivity : activityList) {
            if (sysActivity.getName().equals(activityInfo.getName())) {
                return sysActivity;
            }
            if (sysActivity.getName().equals(activityInfo.getTargetActivity())) {
                return sysActivity;
            }
            if (sysActivity.getTargetActivity() != null) {
                if (sysActivity.getTargetActivity().equals(activityInfo.getName())) {
                    return sysActivity;
                }
                if (sysActivity.getTargetActivity().equals(activityInfo.getTargetActivity())) {
                    return sysActivity;
                }
            }
        }
        return null;
    }

    private void adjustPriority(com.android.server.pm.Computer computer, java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> systemActivities, com.android.internal.pm.pkg.component.ParsedActivity activity, com.android.internal.pm.pkg.component.ParsedIntentInfo intentInfo, java.lang.String setupWizardPackage) {
        android.content.IntentFilter intentFilter = intentInfo.getIntentFilter();
        if (intentFilter.getPriority() <= 0) {
            return;
        }
        java.lang.String packageName = activity.getPackageName();
        com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateInternal(packageName);
        boolean privilegedApp = packageState.isPrivileged();
        activity.getClassName();
        if (!privilegedApp) {
            intentFilter.setPriority(0);
            return;
        }
        if (isProtectedAction(intentFilter)) {
            if (this.mDeferProtectedFilters) {
                if (this.mProtectedFilters == null) {
                    this.mProtectedFilters = new java.util.ArrayList();
                }
                this.mProtectedFilters.add(android.util.Pair.create(activity, intentInfo));
                return;
            } else {
                if (packageName.equals(setupWizardPackage)) {
                    return;
                }
                intentFilter.setPriority(0);
                return;
            }
        }
        if (systemActivities == null) {
            return;
        }
        com.android.internal.pm.pkg.component.ParsedActivity foundActivity = findMatchingActivity(systemActivities, activity);
        if (foundActivity == null) {
            intentFilter.setPriority(0);
            return;
        }
        java.util.List<com.android.internal.pm.pkg.component.ParsedIntentInfo> intentListCopy = new java.util.ArrayList<>(foundActivity.getIntents());
        java.util.Iterator<java.lang.String> actionsIterator = intentFilter.actionsIterator();
        if (actionsIterator != null) {
            getIntentListSubset(intentListCopy, new java.util.function.Function() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((android.content.IntentFilter) obj).actionsIterator();
                }
            }, actionsIterator);
            if (intentListCopy.size() == 0) {
                intentFilter.setPriority(0);
                return;
            }
        }
        java.util.Iterator<java.lang.String> categoriesIterator = intentFilter.categoriesIterator();
        if (categoriesIterator != null) {
            getIntentListSubset(intentListCopy, new java.util.function.Function() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((android.content.IntentFilter) obj).categoriesIterator();
                }
            }, categoriesIterator);
            if (intentListCopy.size() == 0) {
                intentFilter.setPriority(0);
                return;
            }
        }
        java.util.Iterator<java.lang.String> schemesIterator = intentFilter.schemesIterator();
        if (schemesIterator != null) {
            getIntentListSubset(intentListCopy, new java.util.function.Function() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((android.content.IntentFilter) obj).schemesIterator();
                }
            }, schemesIterator);
            if (intentListCopy.size() == 0) {
                intentFilter.setPriority(0);
                return;
            }
        }
        java.util.Iterator<android.content.IntentFilter.AuthorityEntry> authoritiesIterator = intentFilter.authoritiesIterator();
        if (authoritiesIterator != null) {
            getIntentListSubset(intentListCopy, new java.util.function.Function() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((android.content.IntentFilter) obj).authoritiesIterator();
                }
            }, authoritiesIterator);
            if (intentListCopy.size() == 0) {
                intentFilter.setPriority(0);
                return;
            }
        }
        int cappedPriority = 0;
        for (int i = intentListCopy.size() - 1; i >= 0; i--) {
            cappedPriority = java.lang.Math.max(cappedPriority, intentListCopy.get(i).getIntentFilter().getPriority());
        }
        if (intentFilter.getPriority() > cappedPriority) {
            intentFilter.setPriority(cappedPriority);
        }
    }

    private void removeAllComponentsLocked(com.android.server.pm.pkg.AndroidPackage pkg, boolean chatty) {
        int componentSize = com.android.internal.util.ArrayUtils.size(pkg.getActivities());
        java.lang.StringBuilder r = null;
        for (int i = 0; i < componentSize; i++) {
            com.android.internal.pm.pkg.component.ParsedActivity a = (com.android.internal.pm.pkg.component.ParsedActivity) pkg.getActivities().get(i);
            this.mActivities.removeActivity(a, com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
                if (r == null) {
                    r = new java.lang.StringBuilder(256);
                } else {
                    r.append(' ');
                }
                r.append(a.getName());
            }
        }
        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
            android.util.Log.d(TAG, "  Activities: " + (r == null ? "<NONE>" : r));
        }
        int componentSize2 = com.android.internal.util.ArrayUtils.size(pkg.getProviders());
        java.lang.StringBuilder r2 = null;
        for (int i2 = 0; i2 < componentSize2; i2++) {
            com.android.internal.pm.pkg.component.ParsedProvider p = (com.android.internal.pm.pkg.component.ParsedProvider) pkg.getProviders().get(i2);
            this.mProviders.removeProvider(p);
            if (p.getAuthority() != null) {
                java.lang.String[] names = p.getAuthority().split(";");
                for (int j = 0; j < names.length; j++) {
                    if (this.mProvidersByAuthority.get(names[j]) == p) {
                        this.mProvidersByAuthority.remove(names[j]);
                        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
                            android.util.Log.d(TAG, "Unregistered content provider: " + names[j] + ", className = " + p.getName() + ", isSyncable = " + p.isSyncable());
                        }
                    }
                }
                if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
                    if (r2 == null) {
                        r2 = new java.lang.StringBuilder(256);
                    } else {
                        r2.append(' ');
                    }
                    r2.append(p.getName());
                }
            }
        }
        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
            android.util.Log.d(TAG, "  Providers: " + (r2 == null ? "<NONE>" : r2));
        }
        int componentSize3 = com.android.internal.util.ArrayUtils.size(pkg.getReceivers());
        java.lang.StringBuilder r3 = null;
        for (int i3 = 0; i3 < componentSize3; i3++) {
            com.android.internal.pm.pkg.component.ParsedActivity a2 = (com.android.internal.pm.pkg.component.ParsedActivity) pkg.getReceivers().get(i3);
            this.mReceivers.removeActivity(a2, "receiver");
            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
                if (r3 == null) {
                    r3 = new java.lang.StringBuilder(256);
                } else {
                    r3.append(' ');
                }
                r3.append(a2.getName());
            }
        }
        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
            android.util.Log.d(TAG, "  Receivers: " + (r3 == null ? "<NONE>" : r3));
        }
        int componentSize4 = com.android.internal.util.ArrayUtils.size(pkg.getServices());
        java.lang.StringBuilder r4 = null;
        for (int i4 = 0; i4 < componentSize4; i4++) {
            com.android.internal.pm.pkg.component.ParsedService s = (com.android.internal.pm.pkg.component.ParsedService) pkg.getServices().get(i4);
            this.mServices.removeService(s);
            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
                if (r4 == null) {
                    r4 = new java.lang.StringBuilder(256);
                } else {
                    r4.append(' ');
                }
                r4.append(s.getName());
            }
        }
        if (!com.android.server.pm.PackageManagerService.DEBUG_REMOVE || !chatty) {
            return;
        }
        android.util.Log.d(TAG, "  Services: " + (r4 != null ? r4 : "<NONE>"));
    }

    public void assertProvidersNotDefined(com.android.server.pm.pkg.AndroidPackage pkg) throws com.android.server.pm.PackageManagerException {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                int providersSize = com.android.internal.util.ArrayUtils.size(pkg.getProviders());
                for (int i = 0; i < providersSize; i++) {
                    com.android.internal.pm.pkg.component.ParsedProvider p = (com.android.internal.pm.pkg.component.ParsedProvider) pkg.getProviders().get(i);
                    if (p.getAuthority() != null) {
                        java.lang.String[] names = p.getAuthority().split(";");
                        for (int j = 0; j < names.length; j++) {
                            if (this.mProvidersByAuthority.containsKey(names[j])) {
                                com.android.internal.pm.pkg.component.ParsedProvider other = this.mProvidersByAuthority.get(names[j]);
                                java.lang.String otherPackageName = (other == null || other.getComponentName() == null) ? "?" : other.getComponentName().getPackageName();
                                if (!otherPackageName.equals(pkg.getPackageName())) {
                                    throw new com.android.server.pm.PackageManagerException(-13, "Can't install because provider name " + names[j] + " (in package " + pkg.getPackageName() + ") is already used by " + otherPackageName);
                                }
                            }
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private static abstract class MimeGroupsAwareIntentResolver<F extends android.util.Pair<? extends com.android.internal.pm.pkg.component.ParsedComponent, com.android.internal.pm.pkg.component.ParsedIntentInfo>, R> extends com.android.server.IntentResolver<F, R> {
        private boolean mIsUpdatingMimeGroup;
        private final android.util.ArrayMap<java.lang.String, F[]> mMimeGroupToFilter;
        protected final com.android.server.pm.UserManagerService mUserManager;

        MimeGroupsAwareIntentResolver(com.android.server.pm.UserManagerService userManager) {
            this.mMimeGroupToFilter = new android.util.ArrayMap<>();
            this.mIsUpdatingMimeGroup = false;
            this.mUserManager = userManager;
        }

        MimeGroupsAwareIntentResolver(com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver<F, R> orig, com.android.server.pm.UserManagerService userManager) {
            this.mMimeGroupToFilter = new android.util.ArrayMap<>();
            this.mIsUpdatingMimeGroup = false;
            this.mUserManager = userManager;
            copyFrom(orig);
            copyInto(this.mMimeGroupToFilter, orig.mMimeGroupToFilter);
            this.mIsUpdatingMimeGroup = orig.mIsUpdatingMimeGroup;
        }

        @Override // com.android.server.IntentResolver
        public void addFilter(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, F f) {
            android.content.IntentFilter intentFilter = getIntentFilter(f);
            applyMimeGroups((com.android.server.pm.Computer) snapshot, f);
            super.addFilter(snapshot, f);
            if (!this.mIsUpdatingMimeGroup) {
                register_intent_filter(f, intentFilter.mimeGroupsIterator(), this.mMimeGroupToFilter, "      MimeGroup: ");
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public void removeFilterInternal(F f) {
            android.content.IntentFilter intentFilter = getIntentFilter(f);
            if (!this.mIsUpdatingMimeGroup) {
                unregister_intent_filter(f, intentFilter.mimeGroupsIterator(), this.mMimeGroupToFilter, "      MimeGroup: ");
            }
            super.removeFilterInternal(f);
            intentFilter.clearDynamicDataTypes();
        }

        public boolean updateMimeGroup(com.android.server.pm.Computer computer, java.lang.String packageName, java.lang.String mimeGroup) {
            F[] filters = this.mMimeGroupToFilter.get(mimeGroup);
            int n = filters != null ? filters.length : 0;
            this.mIsUpdatingMimeGroup = true;
            boolean hasChanges = false;
            for (int i = 0; i < n; i++) {
                F filter = filters[i];
                if (filter == null) {
                    break;
                }
                if (isPackageForFilter(packageName, filter)) {
                    hasChanges |= updateFilter(computer, filter);
                }
            }
            this.mIsUpdatingMimeGroup = false;
            return hasChanges;
        }

        private boolean updateFilter(com.android.server.pm.Computer computer, F f) {
            android.content.IntentFilter intentFilter = getIntentFilter(f);
            java.util.List oldTypes = intentFilter.dataTypes();
            removeFilter(f);
            addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) computer, (android.util.Pair) f);
            java.util.List newTypes = intentFilter.dataTypes();
            return !equalLists(oldTypes, newTypes);
        }

        private boolean equalLists(java.util.List<java.lang.String> first, java.util.List<java.lang.String> second) {
            if (first == null) {
                return second == null;
            }
            if (second == null || first.size() != second.size()) {
                return false;
            }
            java.util.Collections.sort(first);
            java.util.Collections.sort(second);
            return first.equals(second);
        }

        private void applyMimeGroups(com.android.server.pm.Computer computer, F f) {
            java.util.Collection<java.lang.String> mimeTypes;
            android.content.IntentFilter filter = getIntentFilter(f);
            for (int i = filter.countMimeGroups() - 1; i >= 0; i--) {
                com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateInternal(((com.android.internal.pm.pkg.component.ParsedComponent) ((android.util.Pair) f).first).getPackageName());
                if (packageState == null) {
                    mimeTypes = java.util.Collections.emptyList();
                } else {
                    mimeTypes = packageState.getMimeGroups().get(filter.getMimeGroup(i));
                }
                for (java.lang.String mimeType : mimeTypes) {
                    try {
                        filter.addDynamicDataType(mimeType);
                    } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public boolean isFilterStopped(com.android.server.pm.Computer computer, F filter, int userId) {
            if (!this.mUserManager.exists(userId)) {
                return true;
            }
            com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateInternal(((com.android.internal.pm.pkg.component.ParsedComponent) ((android.util.Pair) filter).first).getPackageName());
            if (packageState == null || packageState.getPkg() == null) {
                return false;
            }
            if (packageState.isSystem()) {
                boolean isStopped = packageState.isScannedAsStoppedSystemApp() && packageState.getUserStateOrDefault(userId).isStopped();
                return com.android.server.pm.resolution.ComponentResolver.mStaticComponentResolverExt.onIsFilterStopped(packageState, isStopped);
            }
            return packageState.getUserStateOrDefault(userId).isStopped();
        }
    }

    public static class ActivityIntentResolver extends com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver<android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo>, android.content.pm.ResolveInfo> {
        protected final android.util.ArrayMap<android.content.ComponentName, com.android.internal.pm.pkg.component.ParsedActivity> mActivities;
        private final com.android.server.pm.UserNeedsBadgingCache mUserNeedsBadging;

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ void addFilter(com.android.server.pm.snapshot.PackageDataSnapshot packageDataSnapshot, android.util.Pair pair) {
            super.addFilter(packageDataSnapshot, pair);
        }

        @Override // com.android.server.IntentResolver
        protected /* bridge */ /* synthetic */ boolean allowFilterResult(java.lang.Object obj, java.util.List list) {
            return allowFilterResult((android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo>) obj, (java.util.List<android.content.pm.ResolveInfo>) list);
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ boolean updateMimeGroup(com.android.server.pm.Computer computer, java.lang.String str, java.lang.String str2) {
            return super.updateMimeGroup(computer, str, str2);
        }

        ActivityIntentResolver(com.android.server.pm.UserManagerService userManager, com.android.server.pm.UserNeedsBadgingCache userNeedsBadgingCache) {
            super(userManager);
            this.mActivities = new android.util.ArrayMap<>();
            this.mUserNeedsBadging = userNeedsBadgingCache;
        }

        ActivityIntentResolver(com.android.server.pm.resolution.ComponentResolver.ActivityIntentResolver orig, com.android.server.pm.UserManagerService userManager, com.android.server.pm.UserNeedsBadgingCache userNeedsBadgingCache) {
            super(orig, userManager);
            this.mActivities = new android.util.ArrayMap<>();
            this.mActivities.putAll((android.util.ArrayMap<? extends android.content.ComponentName, ? extends com.android.internal.pm.pkg.component.ParsedActivity>) orig.mActivities);
            this.mUserNeedsBadging = userNeedsBadgingCache;
        }

        @Override // com.android.server.IntentResolver
        public java.util.List<android.content.pm.ResolveInfo> queryIntent(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, android.content.Intent intent, java.lang.String resolvedType, boolean defaultOnly, int userId) {
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            long flags = defaultOnly ? 65536 : 0;
            return super.queryIntent(snapshot, intent, resolvedType, defaultOnly, userId, flags);
        }

        java.util.List<android.content.pm.ResolveInfo> queryIntent(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
            if (this.mUserManager.exists(userId)) {
                return super.queryIntent(computer, intent, resolvedType, (65536 & flags) != 0, userId, flags);
            }
            return null;
        }

        java.util.List<android.content.pm.ResolveInfo> queryIntentForPackage(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> packageActivities, int userId) {
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            if (packageActivities == null) {
                return java.util.Collections.emptyList();
            }
            boolean defaultOnly = (flags & 65536) != 0;
            int activitiesSize = packageActivities.size();
            java.util.ArrayList<android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo>[]> listCut = new java.util.ArrayList<>(activitiesSize);
            for (int i = 0; i < activitiesSize; i++) {
                com.android.internal.pm.pkg.component.ParsedActivity activity = packageActivities.get(i);
                java.util.List<com.android.internal.pm.pkg.component.ParsedIntentInfo> intentFilters = activity.getIntents();
                if (!intentFilters.isEmpty()) {
                    android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo>[] array = newArray(intentFilters.size());
                    for (int arrayIndex = 0; arrayIndex < intentFilters.size(); arrayIndex++) {
                        array[arrayIndex] = android.util.Pair.create(activity, intentFilters.get(arrayIndex));
                    }
                    listCut.add(array);
                }
            }
            return super.queryIntentFromList(computer, intent, resolvedType, defaultOnly, listCut, userId, flags);
        }

        protected void addActivity(com.android.server.pm.Computer computer, com.android.internal.pm.pkg.component.ParsedActivity a, java.lang.String type, java.util.List<android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo>> newIntents) {
            this.mActivities.put(a.getComponentName(), a);
            int intentsSize = a.getIntents().size();
            for (int j = 0; j < intentsSize; j++) {
                com.android.internal.pm.pkg.component.ParsedIntentInfo intent = (com.android.internal.pm.pkg.component.ParsedIntentInfo) a.getIntents().get(j);
                android.content.IntentFilter intentFilter = intent.getIntentFilter();
                if (newIntents != null && com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY.equals(type)) {
                    newIntents.add(android.util.Pair.create(a, intent));
                }
                if (!intentFilter.debugCheck()) {
                    android.util.Log.w(com.android.server.pm.resolution.ComponentResolver.TAG, "==> For Activity " + a.getName());
                }
                addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) computer, android.util.Pair.create(a, intent));
            }
        }

        protected void removeActivity(com.android.internal.pm.pkg.component.ParsedActivity a, java.lang.String type) {
            this.mActivities.remove(a.getComponentName());
            int intentsSize = a.getIntents().size();
            for (int j = 0; j < intentsSize; j++) {
                com.android.internal.pm.pkg.component.ParsedIntentInfo intent = (com.android.internal.pm.pkg.component.ParsedIntentInfo) a.getIntents().get(j);
                intent.getIntentFilter();
                removeFilter(android.util.Pair.create(a, intent));
            }
        }

        protected boolean allowFilterResult(android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo> filter, java.util.List<android.content.pm.ResolveInfo> dest) {
            for (int i = dest.size() - 1; i >= 0; i--) {
                android.content.pm.ActivityInfo destAi = dest.get(i).activityInfo;
                if (java.util.Objects.equals(destAi.name, ((com.android.internal.pm.pkg.component.ParsedActivity) filter.first).getName()) && java.util.Objects.equals(destAi.packageName, ((com.android.internal.pm.pkg.component.ParsedActivity) filter.first).getPackageName())) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo>[] newArray(int size) {
            return new android.util.Pair[size];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public boolean isPackageForFilter(java.lang.String packageName, android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo> info) {
            return packageName.equals(((com.android.internal.pm.pkg.component.ParsedActivity) info.first).getPackageName());
        }

        private void log(java.lang.String reason, com.android.internal.pm.pkg.component.ParsedIntentInfo info, int match, int userId) {
            android.util.Slog.w(com.android.server.pm.resolution.ComponentResolver.TAG, reason + "; match: " + android.util.DebugUtils.flagsToString(android.content.IntentFilter.class, "MATCH_", match) + "; userId: " + userId + "; intent info: " + info);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.content.pm.ResolveInfo newResult(com.android.server.pm.Computer computer, android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair, int match, int userId, long customFlags) {
            com.android.internal.pm.pkg.component.ParsedActivity activity = (com.android.internal.pm.pkg.component.ParsedActivity) pair.first;
            com.android.internal.pm.pkg.component.ParsedIntentInfo info = (com.android.internal.pm.pkg.component.ParsedIntentInfo) pair.second;
            android.content.IntentFilter intentFilter = info.getIntentFilter();
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateInternal(activity.getPackageName());
            if (packageState == null || packageState.getPkg() == null || !com.android.server.pm.pkg.PackageStateUtils.isEnabledAndMatches(packageState, (com.android.internal.pm.pkg.component.ParsedMainComponent) activity, customFlags, userId)) {
                return null;
            }
            com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
            android.content.pm.ActivityInfo ai = com.android.server.pm.parsing.PackageInfoUtils.generateActivityInfo(packageState.getPkg(), activity, customFlags, userState, userId, packageState);
            if (ai == null) {
                return null;
            }
            boolean matchExplicitlyVisibleOnly = (33554432 & customFlags) != 0;
            boolean matchVisibleToInstantApp = (customFlags & 16777216) != 0;
            boolean componentVisible = matchVisibleToInstantApp && intentFilter.isVisibleToInstantApp() && (!matchExplicitlyVisibleOnly || intentFilter.isExplicitlyVisibleToInstantApp());
            boolean matchInstantApp = (customFlags & 8388608) != 0;
            if (matchVisibleToInstantApp && !componentVisible && !userState.isInstantApp()) {
                return null;
            }
            if (!matchInstantApp && userState.isInstantApp()) {
                return null;
            }
            if (userState.isInstantApp() && packageState.isUpdateAvailable()) {
                return null;
            }
            android.content.pm.ResolveInfo res = new android.content.pm.ResolveInfo(intentFilter.hasCategory("android.intent.category.BROWSABLE"));
            res.activityInfo = ai;
            if ((customFlags & 64) != 0) {
                res.filter = intentFilter;
            }
            res.handleAllWebDataURI = intentFilter.handleAllWebDataURI();
            res.priority = intentFilter.getPriority();
            res.match = match;
            res.isDefault = info.isHasDefault();
            res.labelRes = info.getLabelRes();
            res.nonLocalizedLabel = info.getNonLocalizedLabel();
            if (this.mUserNeedsBadging.get(userId)) {
                res.noResourceId = true;
            } else {
                res.icon = info.getIcon();
            }
            res.iconResourceId = info.getIcon();
            res.system = res.activityInfo.applicationInfo.isSystemApp();
            res.isInstantAppAvailable = userState.isInstantApp();
            res.userHandle = android.os.UserHandle.of(userId);
            return res;
        }

        @Override // com.android.server.IntentResolver
        protected void sortResults(java.util.List<android.content.pm.ResolveInfo> results) {
            results.sort(com.android.server.pm.resolution.ComponentResolver.RESOLVE_PRIORITY_SORTER);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public void dumpFilter(java.io.PrintWriter out, java.lang.String prefix, android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair) {
            com.android.internal.pm.pkg.component.ParsedActivity activity = (com.android.internal.pm.pkg.component.ParsedActivity) pair.first;
            com.android.internal.pm.pkg.component.ParsedIntentInfo filter = (com.android.internal.pm.pkg.component.ParsedIntentInfo) pair.second;
            out.print(prefix);
            out.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(activity)));
            out.print(' ');
            android.content.ComponentName.printShortString(out, activity.getPackageName(), activity.getClassName());
            out.print(" filter ");
            out.println(java.lang.Integer.toHexString(java.lang.System.identityHashCode(filter)));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public java.lang.Object filterToLabel(android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo> filter) {
            return filter;
        }

        @Override // com.android.server.IntentResolver
        protected void dumpFilterLabel(java.io.PrintWriter out, java.lang.String prefix, java.lang.Object label, int count) {
            android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair = (android.util.Pair) label;
            out.print(prefix);
            out.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(pair.first)));
            out.print(' ');
            android.content.ComponentName.printShortString(out, ((com.android.internal.pm.pkg.component.ParsedActivity) pair.first).getPackageName(), ((com.android.internal.pm.pkg.component.ParsedActivity) pair.first).getClassName());
            if (count > 1) {
                out.print(" (");
                out.print(count);
                out.print(" filters)");
            }
            out.println();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.content.IntentFilter getIntentFilter(android.util.Pair<com.android.internal.pm.pkg.component.ParsedActivity, com.android.internal.pm.pkg.component.ParsedIntentInfo> input) {
            return ((com.android.internal.pm.pkg.component.ParsedIntentInfo) input.second).getIntentFilter();
        }

        protected java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> getResolveList(com.android.server.pm.pkg.AndroidPackage pkg) {
            return pkg.getActivities();
        }
    }

    public static final class ReceiverIntentResolver extends com.android.server.pm.resolution.ComponentResolver.ActivityIntentResolver {
        ReceiverIntentResolver(com.android.server.pm.UserManagerService userManager, com.android.server.pm.UserNeedsBadgingCache userNeedsBadgingCache) {
            super(userManager, userNeedsBadgingCache);
        }

        ReceiverIntentResolver(com.android.server.pm.resolution.ComponentResolver.ReceiverIntentResolver orig, com.android.server.pm.UserManagerService userManager, com.android.server.pm.UserNeedsBadgingCache userNeedsBadgingCache) {
            super(orig, userManager, userNeedsBadgingCache);
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.ActivityIntentResolver
        protected java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> getResolveList(com.android.server.pm.pkg.AndroidPackage pkg) {
            return pkg.getReceivers();
        }
    }

    public static final class ProviderIntentResolver extends com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver<android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo>, android.content.pm.ResolveInfo> {
        final android.util.ArrayMap<android.content.ComponentName, com.android.internal.pm.pkg.component.ParsedProvider> mProviders;

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ void addFilter(com.android.server.pm.snapshot.PackageDataSnapshot packageDataSnapshot, android.util.Pair pair) {
            super.addFilter(packageDataSnapshot, pair);
        }

        @Override // com.android.server.IntentResolver
        protected /* bridge */ /* synthetic */ boolean allowFilterResult(java.lang.Object obj, java.util.List list) {
            return allowFilterResult((android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo>) obj, (java.util.List<android.content.pm.ResolveInfo>) list);
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ boolean updateMimeGroup(com.android.server.pm.Computer computer, java.lang.String str, java.lang.String str2) {
            return super.updateMimeGroup(computer, str, str2);
        }

        ProviderIntentResolver(com.android.server.pm.UserManagerService userManager) {
            super(userManager);
            this.mProviders = new android.util.ArrayMap<>();
        }

        ProviderIntentResolver(com.android.server.pm.resolution.ComponentResolver.ProviderIntentResolver orig, com.android.server.pm.UserManagerService userManager) {
            super(orig, userManager);
            this.mProviders = new android.util.ArrayMap<>();
            this.mProviders.putAll((android.util.ArrayMap<? extends android.content.ComponentName, ? extends com.android.internal.pm.pkg.component.ParsedProvider>) orig.mProviders);
        }

        @Override // com.android.server.IntentResolver
        public java.util.List<android.content.pm.ResolveInfo> queryIntent(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, android.content.Intent intent, java.lang.String resolvedType, boolean defaultOnly, int userId) {
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            long flags = defaultOnly ? 65536L : 0L;
            return super.queryIntent(snapshot, intent, resolvedType, defaultOnly, userId, flags);
        }

        java.util.List<android.content.pm.ResolveInfo> queryIntent(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
            if (this.mUserManager.exists(userId)) {
                return super.queryIntent(computer, intent, resolvedType, (65536 & flags) != 0, userId, flags);
            }
            return null;
        }

        java.util.List<android.content.pm.ResolveInfo> queryIntentForPackage(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedProvider> packageProviders, int userId) {
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            if (packageProviders == null) {
                return java.util.Collections.emptyList();
            }
            boolean defaultOnly = (flags & 65536) != 0;
            int providersSize = packageProviders.size();
            java.util.ArrayList<android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo>[]> listCut = new java.util.ArrayList<>(providersSize);
            for (int i = 0; i < providersSize; i++) {
                com.android.internal.pm.pkg.component.ParsedProvider provider = packageProviders.get(i);
                java.util.List<com.android.internal.pm.pkg.component.ParsedIntentInfo> intentFilters = provider.getIntents();
                if (!intentFilters.isEmpty()) {
                    android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo>[] array = newArray(intentFilters.size());
                    for (int arrayIndex = 0; arrayIndex < intentFilters.size(); arrayIndex++) {
                        array[arrayIndex] = android.util.Pair.create(provider, intentFilters.get(arrayIndex));
                    }
                    listCut.add(array);
                }
            }
            return super.queryIntentFromList(computer, intent, resolvedType, defaultOnly, listCut, userId, flags);
        }

        void addProvider(com.android.server.pm.Computer computer, com.android.internal.pm.pkg.component.ParsedProvider p) {
            if (this.mProviders.containsKey(p.getComponentName())) {
                android.util.Slog.w(com.android.server.pm.resolution.ComponentResolver.TAG, "Provider " + p.getComponentName() + " already defined; ignoring");
                return;
            }
            this.mProviders.put(p.getComponentName(), p);
            int intentsSize = p.getIntents().size();
            for (int j = 0; j < intentsSize; j++) {
                com.android.internal.pm.pkg.component.ParsedIntentInfo intent = (com.android.internal.pm.pkg.component.ParsedIntentInfo) p.getIntents().get(j);
                android.content.IntentFilter intentFilter = intent.getIntentFilter();
                if (!intentFilter.debugCheck()) {
                    android.util.Log.w(com.android.server.pm.resolution.ComponentResolver.TAG, "==> For Provider " + p.getName());
                }
                addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) computer, android.util.Pair.create(p, intent));
            }
        }

        void removeProvider(com.android.internal.pm.pkg.component.ParsedProvider p) {
            this.mProviders.remove(p.getComponentName());
            int intentsSize = p.getIntents().size();
            for (int j = 0; j < intentsSize; j++) {
                com.android.internal.pm.pkg.component.ParsedIntentInfo intent = (com.android.internal.pm.pkg.component.ParsedIntentInfo) p.getIntents().get(j);
                intent.getIntentFilter();
                removeFilter(android.util.Pair.create(p, intent));
            }
        }

        protected boolean allowFilterResult(android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo> filter, java.util.List<android.content.pm.ResolveInfo> dest) {
            for (int i = dest.size() - 1; i >= 0; i--) {
                android.content.pm.ProviderInfo destPi = dest.get(i).providerInfo;
                if (java.util.Objects.equals(destPi.name, ((com.android.internal.pm.pkg.component.ParsedProvider) filter.first).getClassName()) && java.util.Objects.equals(destPi.packageName, ((com.android.internal.pm.pkg.component.ParsedProvider) filter.first).getPackageName())) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo>[] newArray(int size) {
            return new android.util.Pair[size];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public boolean isPackageForFilter(java.lang.String packageName, android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo> info) {
            return packageName.equals(((com.android.internal.pm.pkg.component.ParsedProvider) info.first).getPackageName());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.content.pm.ResolveInfo newResult(com.android.server.pm.Computer computer, android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair, int match, int userId, long customFlags) {
            android.content.pm.ApplicationInfo appInfo;
            android.content.pm.ProviderInfo pi;
            android.content.IntentFilter filter;
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            com.android.internal.pm.pkg.component.ParsedProvider provider = (com.android.internal.pm.pkg.component.ParsedProvider) pair.first;
            com.android.internal.pm.pkg.component.ParsedIntentInfo intentInfo = (com.android.internal.pm.pkg.component.ParsedIntentInfo) pair.second;
            android.content.IntentFilter filter2 = intentInfo.getIntentFilter();
            com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateInternal(provider.getPackageName());
            if (packageState == null || packageState.getPkg() == null || !com.android.server.pm.pkg.PackageStateUtils.isEnabledAndMatches(packageState, (com.android.internal.pm.pkg.component.ParsedMainComponent) provider, customFlags, userId)) {
                return null;
            }
            com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
            boolean matchVisibleToInstantApp = (16777216 & customFlags) != 0;
            boolean isInstantApp = (8388608 & customFlags) != 0;
            if (matchVisibleToInstantApp && !filter2.isVisibleToInstantApp() && !userState.isInstantApp()) {
                return null;
            }
            if (!isInstantApp && userState.isInstantApp()) {
                return null;
            }
            if ((userState.isInstantApp() && packageState.isUpdateAvailable()) || (appInfo = com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(packageState.getPkg(), customFlags, userState, userId, packageState)) == null || (pi = com.android.server.pm.parsing.PackageInfoUtils.generateProviderInfo(packageState.getPkg(), provider, customFlags, userState, appInfo, userId, packageState)) == null) {
                return null;
            }
            android.content.pm.ResolveInfo res = new android.content.pm.ResolveInfo();
            res.providerInfo = pi;
            if ((64 & customFlags) == 0) {
                filter = filter2;
            } else {
                filter = filter2;
                res.filter = filter;
            }
            res.priority = filter.getPriority();
            res.match = match;
            res.isDefault = intentInfo.isHasDefault();
            res.labelRes = intentInfo.getLabelRes();
            res.nonLocalizedLabel = intentInfo.getNonLocalizedLabel();
            res.icon = intentInfo.getIcon();
            res.system = res.providerInfo.applicationInfo.isSystemApp();
            return res;
        }

        @Override // com.android.server.IntentResolver
        protected void sortResults(java.util.List<android.content.pm.ResolveInfo> results) {
            results.sort(com.android.server.pm.resolution.ComponentResolver.RESOLVE_PRIORITY_SORTER);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public void dumpFilter(java.io.PrintWriter out, java.lang.String prefix, android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair) {
            com.android.internal.pm.pkg.component.ParsedProvider provider = (com.android.internal.pm.pkg.component.ParsedProvider) pair.first;
            com.android.internal.pm.pkg.component.ParsedIntentInfo filter = (com.android.internal.pm.pkg.component.ParsedIntentInfo) pair.second;
            out.print(prefix);
            out.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(provider)));
            out.print(' ');
            android.content.ComponentName.printShortString(out, provider.getPackageName(), provider.getClassName());
            out.print(" filter ");
            out.println(java.lang.Integer.toHexString(java.lang.System.identityHashCode(filter)));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public java.lang.Object filterToLabel(android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo> filter) {
            return filter;
        }

        @Override // com.android.server.IntentResolver
        protected void dumpFilterLabel(java.io.PrintWriter out, java.lang.String prefix, java.lang.Object label, int count) {
            android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair = (android.util.Pair) label;
            out.print(prefix);
            out.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(pair.first)));
            out.print(' ');
            android.content.ComponentName.printShortString(out, ((com.android.internal.pm.pkg.component.ParsedProvider) pair.first).getPackageName(), ((com.android.internal.pm.pkg.component.ParsedProvider) pair.first).getClassName());
            if (count > 1) {
                out.print(" (");
                out.print(count);
                out.print(" filters)");
            }
            out.println();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.content.IntentFilter getIntentFilter(android.util.Pair<com.android.internal.pm.pkg.component.ParsedProvider, com.android.internal.pm.pkg.component.ParsedIntentInfo> input) {
            return ((com.android.internal.pm.pkg.component.ParsedIntentInfo) input.second).getIntentFilter();
        }
    }

    public static final class ServiceIntentResolver extends com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver<android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo>, android.content.pm.ResolveInfo> {
        final android.util.ArrayMap<android.content.ComponentName, com.android.internal.pm.pkg.component.ParsedService> mServices;

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ void addFilter(com.android.server.pm.snapshot.PackageDataSnapshot packageDataSnapshot, android.util.Pair pair) {
            super.addFilter(packageDataSnapshot, pair);
        }

        @Override // com.android.server.IntentResolver
        protected /* bridge */ /* synthetic */ boolean allowFilterResult(java.lang.Object obj, java.util.List list) {
            return allowFilterResult((android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo>) obj, (java.util.List<android.content.pm.ResolveInfo>) list);
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ boolean updateMimeGroup(com.android.server.pm.Computer computer, java.lang.String str, java.lang.String str2) {
            return super.updateMimeGroup(computer, str, str2);
        }

        ServiceIntentResolver(com.android.server.pm.UserManagerService userManager) {
            super(userManager);
            this.mServices = new android.util.ArrayMap<>();
        }

        ServiceIntentResolver(com.android.server.pm.resolution.ComponentResolver.ServiceIntentResolver orig, com.android.server.pm.UserManagerService userManager) {
            super(orig, userManager);
            this.mServices = new android.util.ArrayMap<>();
            this.mServices.putAll((android.util.ArrayMap<? extends android.content.ComponentName, ? extends com.android.internal.pm.pkg.component.ParsedService>) orig.mServices);
        }

        @Override // com.android.server.IntentResolver
        public java.util.List<android.content.pm.ResolveInfo> queryIntent(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, android.content.Intent intent, java.lang.String resolvedType, boolean defaultOnly, int userId) {
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            long flags = defaultOnly ? 65536L : 0L;
            return super.queryIntent(snapshot, intent, resolvedType, defaultOnly, userId, flags);
        }

        java.util.List<android.content.pm.ResolveInfo> queryIntent(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
            if (this.mUserManager.exists(userId)) {
                return super.queryIntent(computer, intent, resolvedType, (65536 & flags) != 0, userId, flags);
            }
            return null;
        }

        java.util.List<android.content.pm.ResolveInfo> queryIntentForPackage(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedService> packageServices, int userId) {
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            if (packageServices == null) {
                return java.util.Collections.emptyList();
            }
            boolean defaultOnly = (flags & 65536) != 0;
            int servicesSize = packageServices.size();
            java.util.ArrayList<android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo>[]> listCut = new java.util.ArrayList<>(servicesSize);
            for (int i = 0; i < servicesSize; i++) {
                com.android.internal.pm.pkg.component.ParsedService service = packageServices.get(i);
                java.util.List<com.android.internal.pm.pkg.component.ParsedIntentInfo> intentFilters = service.getIntents();
                if (intentFilters.size() > 0) {
                    android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo>[] array = newArray(intentFilters.size());
                    for (int arrayIndex = 0; arrayIndex < intentFilters.size(); arrayIndex++) {
                        array[arrayIndex] = android.util.Pair.create(service, intentFilters.get(arrayIndex));
                    }
                    listCut.add(array);
                }
            }
            return super.queryIntentFromList(computer, intent, resolvedType, defaultOnly, listCut, userId, flags);
        }

        void addService(com.android.server.pm.Computer computer, com.android.internal.pm.pkg.component.ParsedService s) {
            this.mServices.put(s.getComponentName(), s);
            int intentsSize = s.getIntents().size();
            for (int j = 0; j < intentsSize; j++) {
                com.android.internal.pm.pkg.component.ParsedIntentInfo intent = (com.android.internal.pm.pkg.component.ParsedIntentInfo) s.getIntents().get(j);
                android.content.IntentFilter intentFilter = intent.getIntentFilter();
                if (!intentFilter.debugCheck()) {
                    android.util.Log.w(com.android.server.pm.resolution.ComponentResolver.TAG, "==> For Service " + s.getName());
                }
                addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) computer, android.util.Pair.create(s, intent));
            }
        }

        void removeService(com.android.internal.pm.pkg.component.ParsedService s) {
            this.mServices.remove(s.getComponentName());
            int intentsSize = s.getIntents().size();
            for (int j = 0; j < intentsSize; j++) {
                com.android.internal.pm.pkg.component.ParsedIntentInfo intent = (com.android.internal.pm.pkg.component.ParsedIntentInfo) s.getIntents().get(j);
                intent.getIntentFilter();
                removeFilter(android.util.Pair.create(s, intent));
            }
        }

        protected boolean allowFilterResult(android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo> filter, java.util.List<android.content.pm.ResolveInfo> dest) {
            for (int i = dest.size() - 1; i >= 0; i--) {
                android.content.pm.ServiceInfo destAi = dest.get(i).serviceInfo;
                if (java.util.Objects.equals(destAi.name, ((com.android.internal.pm.pkg.component.ParsedService) filter.first).getClassName()) && java.util.Objects.equals(destAi.packageName, ((com.android.internal.pm.pkg.component.ParsedService) filter.first).getPackageName())) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo>[] newArray(int size) {
            return new android.util.Pair[size];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public boolean isPackageForFilter(java.lang.String packageName, android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo> info) {
            return packageName.equals(((com.android.internal.pm.pkg.component.ParsedService) info.first).getPackageName());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.content.pm.ResolveInfo newResult(com.android.server.pm.Computer computer, android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair, int match, int userId, long customFlags) {
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            com.android.internal.pm.pkg.component.ParsedService service = (com.android.internal.pm.pkg.component.ParsedService) pair.first;
            com.android.internal.pm.pkg.component.ParsedIntentInfo intentInfo = (com.android.internal.pm.pkg.component.ParsedIntentInfo) pair.second;
            android.content.IntentFilter filter = intentInfo.getIntentFilter();
            com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateInternal(service.getPackageName());
            if (packageState == null || packageState.getPkg() == null || !com.android.server.pm.pkg.PackageStateUtils.isEnabledAndMatches(packageState, (com.android.internal.pm.pkg.component.ParsedMainComponent) service, customFlags, userId)) {
                return null;
            }
            com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
            android.content.pm.ServiceInfo si = com.android.server.pm.parsing.PackageInfoUtils.generateServiceInfo(packageState.getPkg(), service, customFlags, userState, userId, packageState);
            if (si == null) {
                return null;
            }
            boolean matchVisibleToInstantApp = (16777216 & customFlags) != 0;
            boolean isInstantApp = (customFlags & 8388608) != 0;
            if (matchVisibleToInstantApp && !filter.isVisibleToInstantApp() && !userState.isInstantApp()) {
                return null;
            }
            if (!isInstantApp && userState.isInstantApp()) {
                return null;
            }
            if (userState.isInstantApp() && packageState.isUpdateAvailable()) {
                return null;
            }
            android.content.pm.ResolveInfo res = new android.content.pm.ResolveInfo();
            res.serviceInfo = si;
            if ((customFlags & 64) != 0) {
                res.filter = filter;
            }
            res.priority = filter.getPriority();
            res.match = match;
            res.isDefault = intentInfo.isHasDefault();
            res.labelRes = intentInfo.getLabelRes();
            res.nonLocalizedLabel = intentInfo.getNonLocalizedLabel();
            res.icon = intentInfo.getIcon();
            res.system = res.serviceInfo.applicationInfo.isSystemApp();
            return res;
        }

        @Override // com.android.server.IntentResolver
        protected void sortResults(java.util.List<android.content.pm.ResolveInfo> results) {
            results.sort(com.android.server.pm.resolution.ComponentResolver.RESOLVE_PRIORITY_SORTER);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public void dumpFilter(java.io.PrintWriter out, java.lang.String prefix, android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair) {
            com.android.internal.pm.pkg.component.ParsedService service = (com.android.internal.pm.pkg.component.ParsedService) pair.first;
            com.android.internal.pm.pkg.component.ParsedIntentInfo filter = (com.android.internal.pm.pkg.component.ParsedIntentInfo) pair.second;
            out.print(prefix);
            out.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(service)));
            out.print(' ');
            android.content.ComponentName.printShortString(out, service.getPackageName(), service.getClassName());
            out.print(" filter ");
            out.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(filter)));
            if (service.getPermission() != null) {
                out.print(" permission ");
                out.println(service.getPermission());
            } else {
                out.println();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public java.lang.Object filterToLabel(android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo> filter) {
            return filter;
        }

        @Override // com.android.server.IntentResolver
        protected void dumpFilterLabel(java.io.PrintWriter out, java.lang.String prefix, java.lang.Object label, int count) {
            android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair = (android.util.Pair) label;
            out.print(prefix);
            out.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(pair.first)));
            out.print(' ');
            android.content.ComponentName.printShortString(out, ((com.android.internal.pm.pkg.component.ParsedService) pair.first).getPackageName(), ((com.android.internal.pm.pkg.component.ParsedService) pair.first).getClassName());
            if (count > 1) {
                out.print(" (");
                out.print(count);
                out.print(" filters)");
            }
            out.println();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.content.IntentFilter getIntentFilter(android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo> input) {
            return ((com.android.internal.pm.pkg.component.ParsedIntentInfo) input.second).getIntentFilter();
        }
    }

    public static final class InstantAppIntentResolver extends com.android.server.IntentResolver<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter, android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> {
        final android.util.ArrayMap<java.lang.String, android.util.Pair<java.lang.Integer, android.content.pm.InstantAppResolveInfo>> mOrderResult = new android.util.ArrayMap<>();
        private final com.android.server.pm.UserManagerService mUserManager;

        public InstantAppIntentResolver(com.android.server.pm.UserManagerService userManager) {
            this.mUserManager = userManager;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.IntentResolver
        public android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter[] newArray(int size) {
            return new android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter[size];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public boolean isPackageForFilter(java.lang.String packageName, android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter responseObj) {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter newResult(com.android.server.pm.Computer computer, android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter responseObj, int match, int userId, long customFlags) {
            if (!this.mUserManager.exists(userId)) {
                return null;
            }
            java.lang.String packageName = responseObj.resolveInfo.getPackageName();
            java.lang.Integer order = java.lang.Integer.valueOf(responseObj.getOrder());
            android.util.Pair<java.lang.Integer, android.content.pm.InstantAppResolveInfo> lastOrderResult = this.mOrderResult.get(packageName);
            if (lastOrderResult != null && ((java.lang.Integer) lastOrderResult.first).intValue() >= order.intValue()) {
                return null;
            }
            android.content.pm.InstantAppResolveInfo res = responseObj.resolveInfo;
            if (order.intValue() > 0) {
                this.mOrderResult.put(packageName, new android.util.Pair<>(order, res));
            }
            return responseObj;
        }

        @Override // com.android.server.IntentResolver
        protected void filterResults(java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> results) {
            if (this.mOrderResult.size() == 0) {
                return;
            }
            int resultSize = results.size();
            int i = 0;
            while (i < resultSize) {
                android.content.pm.InstantAppResolveInfo info = results.get(i).resolveInfo;
                java.lang.String packageName = info.getPackageName();
                android.util.Pair<java.lang.Integer, android.content.pm.InstantAppResolveInfo> savedInfo = this.mOrderResult.get(packageName);
                if (savedInfo != null) {
                    if (savedInfo.second == info) {
                        this.mOrderResult.remove(packageName);
                        if (this.mOrderResult.size() == 0) {
                            return;
                        }
                    } else {
                        results.remove(i);
                        resultSize--;
                        i--;
                    }
                }
                i++;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.IntentResolver
        public android.content.IntentFilter getIntentFilter(android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter input) {
            return input;
        }
    }

    public boolean updateMimeGroup(com.android.server.pm.Computer computer, java.lang.String packageName, java.lang.String group) {
        boolean hasChanges;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                hasChanges = false | this.mActivities.updateMimeGroup(computer, packageName, group) | this.mProviders.updateMimeGroup(computer, packageName, group) | this.mReceivers.updateMimeGroup(computer, packageName, group) | this.mServices.updateMimeGroup(computer, packageName, group);
                if (hasChanges) {
                    onChanged();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return hasChanges;
    }
}
