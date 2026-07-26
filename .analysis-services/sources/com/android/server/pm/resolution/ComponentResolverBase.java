package com.android.server.pm.resolution;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ComponentResolverBase extends com.android.server.utils.WatchableImpl implements com.android.server.pm.resolution.ComponentResolverApi {
    protected com.android.server.pm.resolution.ComponentResolver.ActivityIntentResolver mActivities;
    protected com.android.server.pm.resolution.ComponentResolver.ProviderIntentResolver mProviders;
    protected android.util.ArrayMap<java.lang.String, com.android.internal.pm.pkg.component.ParsedProvider> mProvidersByAuthority;
    protected com.android.server.pm.resolution.ComponentResolver.ReceiverIntentResolver mReceivers;
    protected com.android.server.pm.resolution.ComponentResolver.ServiceIntentResolver mServices;
    protected final com.android.server.pm.UserManagerService mUserManager;

    protected ComponentResolverBase(com.android.server.pm.UserManagerService userManager) {
        this.mUserManager = userManager;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public boolean componentExists(android.content.ComponentName componentName) {
        com.android.internal.pm.pkg.component.ParsedMainComponent component = this.mActivities.mActivities.get(componentName);
        if (component != null) {
            return true;
        }
        com.android.internal.pm.pkg.component.ParsedMainComponent component2 = this.mReceivers.mActivities.get(componentName);
        if (component2 != null) {
            return true;
        }
        com.android.internal.pm.pkg.component.ParsedMainComponent component3 = this.mServices.mServices.get(componentName);
        return (component3 == null && this.mProviders.mProviders.get(componentName) == null) ? false : true;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public com.android.internal.pm.pkg.component.ParsedActivity getActivity(android.content.ComponentName component) {
        return this.mActivities.mActivities.get(component);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public com.android.internal.pm.pkg.component.ParsedProvider getProvider(android.content.ComponentName component) {
        return this.mProviders.mProviders.get(component);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public com.android.internal.pm.pkg.component.ParsedActivity getReceiver(android.content.ComponentName component) {
        return this.mReceivers.mActivities.get(component);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public com.android.internal.pm.pkg.component.ParsedService getService(android.content.ComponentName component) {
        return this.mServices.mServices.get(component);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public boolean isActivityDefined(android.content.ComponentName component) {
        return this.mActivities.mActivities.get(component) != null;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryActivities(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return this.mActivities.queryIntent(computer, intent, resolvedType, flags, userId);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryActivities(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> activities, int userId) {
        return this.mActivities.queryIntentForPackage(computer, intent, resolvedType, flags, activities, userId);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public android.content.pm.ProviderInfo queryProvider(com.android.server.pm.Computer computer, java.lang.String authority, long flags, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageState;
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg;
        com.android.server.pm.pkg.PackageUserStateInternal state;
        android.content.pm.ApplicationInfo appInfo;
        com.android.internal.pm.pkg.component.ParsedProvider p = this.mProvidersByAuthority.get(authority);
        if (p == null || (packageState = computer.getPackageStateInternal(p.getPackageName())) == null || (pkg = packageState.getPkg()) == null || (appInfo = com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(pkg, flags, (state = packageState.getUserStateOrDefault(userId)), userId, packageState)) == null) {
            return null;
        }
        return com.android.server.pm.parsing.PackageInfoUtils.generateProviderInfo(pkg, p, flags, state, appInfo, userId, packageState);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryProviders(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return this.mProviders.queryIntent(computer, intent, resolvedType, flags, userId);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryProviders(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedProvider> providers, int userId) {
        return this.mProviders.queryIntentForPackage(computer, intent, resolvedType, flags, providers, userId);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ProviderInfo> queryProviders(com.android.server.pm.Computer computer, java.lang.String processName, java.lang.String metaDataKey, int uid, long flags, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        com.android.server.pm.pkg.AndroidPackage pkg;
        com.android.server.pm.parsing.PackageInfoUtils.CachedApplicationInfoGenerator appInfoGenerator;
        android.content.pm.ProviderInfo info;
        if (!this.mUserManager.exists(userId)) {
            return null;
        }
        com.android.server.pm.parsing.PackageInfoUtils.CachedApplicationInfoGenerator appInfoGenerator2 = null;
        java.util.List<android.content.pm.ProviderInfo> providerList = null;
        for (int i = this.mProviders.mProviders.size() - 1; i >= 0; i--) {
            com.android.internal.pm.pkg.component.ParsedProvider p = this.mProviders.mProviders.valueAt(i);
            if (p.getAuthority() != null && (ps = computer.getPackageStateInternal(p.getPackageName())) != null && (pkg = ps.getPkg()) != null && (processName == null || (p.getProcessName().equals(processName) && android.os.UserHandle.isSameApp(pkg.getUid(), uid)))) {
                if (metaDataKey == null || p.getMetaData().containsKey(metaDataKey)) {
                    if (appInfoGenerator2 != null) {
                        appInfoGenerator = appInfoGenerator2;
                    } else {
                        com.android.server.pm.parsing.PackageInfoUtils.CachedApplicationInfoGenerator appInfoGenerator3 = new com.android.server.pm.parsing.PackageInfoUtils.CachedApplicationInfoGenerator();
                        appInfoGenerator = appInfoGenerator3;
                    }
                    com.android.server.pm.pkg.PackageUserStateInternal state = ps.getUserStateOrDefault(userId);
                    android.content.pm.ApplicationInfo appInfo = appInfoGenerator.generate(pkg, flags, state, userId, ps);
                    if (appInfo == null || (info = com.android.server.pm.parsing.PackageInfoUtils.generateProviderInfo(pkg, p, flags, state, appInfo, userId, ps)) == null) {
                        appInfoGenerator2 = appInfoGenerator;
                    } else {
                        if (providerList == null) {
                            providerList = new java.util.ArrayList<>(i + 1);
                        }
                        providerList.add(info);
                        appInfoGenerator2 = appInfoGenerator;
                    }
                }
            }
        }
        return providerList;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryReceivers(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return this.mReceivers.queryIntent(computer, intent, resolvedType, flags, userId);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryReceivers(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> receivers, int userId) {
        return this.mReceivers.queryIntentForPackage(computer, intent, resolvedType, flags, receivers, userId);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryServices(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return this.mServices.queryIntent(computer, intent, resolvedType, flags, userId);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryServices(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedService> services, int userId) {
        return this.mServices.queryIntentForPackage(computer, intent, resolvedType, flags, services, userId);
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public void querySyncProviders(com.android.server.pm.Computer computer, java.util.List<java.lang.String> outNames, java.util.List<android.content.pm.ProviderInfo> outInfo, boolean safeMode, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        com.android.server.pm.pkg.AndroidPackage pkg;
        android.content.pm.ProviderInfo info;
        com.android.server.pm.parsing.PackageInfoUtils.CachedApplicationInfoGenerator appInfoGenerator = null;
        for (int i = this.mProvidersByAuthority.size() - 1; i >= 0; i--) {
            com.android.internal.pm.pkg.component.ParsedProvider p = this.mProvidersByAuthority.valueAt(i);
            if (p.isSyncable() && (ps = computer.getPackageStateInternal(p.getPackageName())) != null && (pkg = ps.getPkg()) != null && (!safeMode || ps.isSystem())) {
                if (appInfoGenerator == null) {
                    appInfoGenerator = new com.android.server.pm.parsing.PackageInfoUtils.CachedApplicationInfoGenerator();
                }
                com.android.server.pm.pkg.PackageUserStateInternal state = ps.getUserStateOrDefault(userId);
                android.content.pm.ApplicationInfo appInfo = appInfoGenerator.generate(pkg, 0L, state, userId, ps);
                if (appInfo != null && (info = com.android.server.pm.parsing.PackageInfoUtils.generateProviderInfo(pkg, p, 0L, state, appInfo, userId, ps)) != null) {
                    outNames.add(this.mProvidersByAuthority.keyAt(i));
                    outInfo.add(info);
                }
            }
        }
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public void dumpActivityResolvers(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        if (this.mActivities.dump(pw, dumpState.getTitlePrinted() ? "\nActivity Resolver Table:" : "Activity Resolver Table:", "  ", packageName, dumpState.isOptionEnabled(1), true)) {
            dumpState.setTitlePrinted(true);
        }
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public void dumpProviderResolvers(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        if (this.mProviders.dump(pw, dumpState.getTitlePrinted() ? "\nProvider Resolver Table:" : "Provider Resolver Table:", "  ", packageName, dumpState.isOptionEnabled(1), true)) {
            dumpState.setTitlePrinted(true);
        }
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public void dumpReceiverResolvers(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        if (this.mReceivers.dump(pw, dumpState.getTitlePrinted() ? "\nReceiver Resolver Table:" : "Receiver Resolver Table:", "  ", packageName, dumpState.isOptionEnabled(1), true)) {
            dumpState.setTitlePrinted(true);
        }
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public void dumpServiceResolvers(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        if (this.mServices.dump(pw, dumpState.getTitlePrinted() ? "\nService Resolver Table:" : "Service Resolver Table:", "  ", packageName, dumpState.isOptionEnabled(1), true)) {
            dumpState.setTitlePrinted(true);
        }
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public void dumpContentProviders(com.android.server.pm.Computer computer, java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        boolean printedSomething = false;
        for (com.android.internal.pm.pkg.component.ParsedProvider p : this.mProviders.mProviders.values()) {
            if (packageName == null || packageName.equals(p.getPackageName())) {
                if (!printedSomething) {
                    if (dumpState.onTitlePrinted()) {
                        pw.println();
                    }
                    pw.println("Registered ContentProviders:");
                    printedSomething = true;
                }
                pw.print("  ");
                android.content.ComponentName.printShortString(pw, p.getPackageName(), p.getName());
                pw.println(":");
                pw.print("    ");
                pw.println(p.toString());
            }
        }
        boolean printedSomething2 = false;
        for (java.util.Map.Entry<java.lang.String, com.android.internal.pm.pkg.component.ParsedProvider> entry : this.mProvidersByAuthority.entrySet()) {
            com.android.internal.pm.pkg.component.ParsedProvider p2 = entry.getValue();
            if (packageName == null || packageName.equals(p2.getPackageName())) {
                if (!printedSomething2) {
                    if (dumpState.onTitlePrinted()) {
                        pw.println();
                    }
                    pw.println("ContentProvider Authorities:");
                    printedSomething2 = true;
                }
                pw.print("  [");
                pw.print(entry.getKey());
                pw.println("]:");
                pw.print("    ");
                pw.println(p2.toString());
                com.android.server.pm.pkg.AndroidPackage pkg = computer.getPackage(p2.getPackageName());
                if (pkg != null) {
                    pw.print("      applicationInfo=");
                    pw.println(com.android.server.pm.parsing.pkg.AndroidPackageUtils.generateAppInfoWithoutState(pkg));
                }
            }
        }
    }

    @Override // com.android.server.pm.resolution.ComponentResolverApi
    public void dumpServicePermissions(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState) {
        if (dumpState.onTitlePrinted()) {
            pw.println();
        }
        pw.println("Service permissions:");
        java.util.Iterator<F> itFilterIterator = this.mServices.filterIterator();
        while (itFilterIterator.hasNext()) {
            android.util.Pair<com.android.internal.pm.pkg.component.ParsedService, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair = (android.util.Pair) itFilterIterator.next();
            com.android.internal.pm.pkg.component.ParsedService service = (com.android.internal.pm.pkg.component.ParsedService) pair.first;
            java.lang.String permission = service.getPermission();
            if (permission != null) {
                pw.print("    ");
                pw.print(service.getComponentName().flattenToShortString());
                pw.print(": ");
                pw.println(permission);
            }
        }
    }
}
