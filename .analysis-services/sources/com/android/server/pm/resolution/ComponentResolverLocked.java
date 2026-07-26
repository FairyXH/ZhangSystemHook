package com.android.server.pm.resolution;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ComponentResolverLocked extends com.android.server.pm.resolution.ComponentResolverBase {
    protected final com.android.server.pm.PackageManagerTracedLock mLock;

    protected ComponentResolverLocked(com.android.server.pm.UserManagerService userManager) {
        super(userManager);
        this.mLock = new com.android.server.pm.PackageManagerTracedLock();
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public boolean componentExists(android.content.ComponentName componentName) {
        boolean zComponentExists;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zComponentExists = super.componentExists(componentName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zComponentExists;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public com.android.internal.pm.pkg.component.ParsedActivity getActivity(android.content.ComponentName component) {
        com.android.internal.pm.pkg.component.ParsedActivity activity;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                activity = super.getActivity(component);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return activity;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public com.android.internal.pm.pkg.component.ParsedProvider getProvider(android.content.ComponentName component) {
        com.android.internal.pm.pkg.component.ParsedProvider provider;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                provider = super.getProvider(component);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return provider;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public com.android.internal.pm.pkg.component.ParsedActivity getReceiver(android.content.ComponentName component) {
        com.android.internal.pm.pkg.component.ParsedActivity receiver;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                receiver = super.getReceiver(component);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return receiver;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public com.android.internal.pm.pkg.component.ParsedService getService(android.content.ComponentName component) {
        com.android.internal.pm.pkg.component.ParsedService service;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                service = super.getService(component);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return service;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public boolean isActivityDefined(android.content.ComponentName component) {
        boolean zIsActivityDefined;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                zIsActivityDefined = super.isActivityDefined(component);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return zIsActivityDefined;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryActivities(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        java.util.List<android.content.pm.ResolveInfo> listQueryActivities;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                listQueryActivities = super.queryActivities(computer, intent, resolvedType, flags, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return listQueryActivities;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryActivities(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> activities, int userId) {
        java.util.List<android.content.pm.ResolveInfo> listQueryActivities;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                listQueryActivities = super.queryActivities(computer, intent, resolvedType, flags, activities, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return listQueryActivities;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public android.content.pm.ProviderInfo queryProvider(com.android.server.pm.Computer computer, java.lang.String authority, long flags, int userId) {
        android.content.pm.ProviderInfo providerInfoQueryProvider;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                providerInfoQueryProvider = super.queryProvider(computer, authority, flags, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return providerInfoQueryProvider;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryProviders(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        java.util.List<android.content.pm.ResolveInfo> listQueryProviders;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                listQueryProviders = super.queryProviders(computer, intent, resolvedType, flags, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return listQueryProviders;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryProviders(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedProvider> providers, int userId) {
        java.util.List<android.content.pm.ResolveInfo> listQueryProviders;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                listQueryProviders = super.queryProviders(computer, intent, resolvedType, flags, providers, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return listQueryProviders;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ProviderInfo> queryProviders(com.android.server.pm.Computer computer, java.lang.String processName, java.lang.String metaDataKey, int uid, long flags, int userId) {
        java.util.List<android.content.pm.ProviderInfo> listQueryProviders;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                listQueryProviders = super.queryProviders(computer, processName, metaDataKey, uid, flags, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return listQueryProviders;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryReceivers(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        java.util.List<android.content.pm.ResolveInfo> listQueryReceivers;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                listQueryReceivers = super.queryReceivers(computer, intent, resolvedType, flags, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return listQueryReceivers;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryReceivers(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> receivers, int userId) {
        java.util.List<android.content.pm.ResolveInfo> listQueryReceivers;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                listQueryReceivers = super.queryReceivers(computer, intent, resolvedType, flags, receivers, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return listQueryReceivers;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryServices(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        java.util.List<android.content.pm.ResolveInfo> listQueryServices;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                listQueryServices = super.queryServices(computer, intent, resolvedType, flags, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return listQueryServices;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public java.util.List<android.content.pm.ResolveInfo> queryServices(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<com.android.internal.pm.pkg.component.ParsedService> services, int userId) {
        java.util.List<android.content.pm.ResolveInfo> listQueryServices;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                listQueryServices = super.queryServices(computer, intent, resolvedType, flags, services, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return listQueryServices;
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public void querySyncProviders(com.android.server.pm.Computer computer, java.util.List<java.lang.String> outNames, java.util.List<android.content.pm.ProviderInfo> outInfo, boolean safeMode, int userId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.querySyncProviders(computer, outNames, outInfo, safeMode, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public void dumpActivityResolvers(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpActivityResolvers(pw, dumpState, packageName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public void dumpProviderResolvers(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpProviderResolvers(pw, dumpState, packageName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public void dumpReceiverResolvers(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpReceiverResolvers(pw, dumpState, packageName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public void dumpServiceResolvers(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpServiceResolvers(pw, dumpState, packageName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public void dumpContentProviders(com.android.server.pm.Computer computer, java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpContentProviders(computer, pw, dumpState, packageName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.resolution.ComponentResolverBase, com.android.server.pm.resolution.ComponentResolverApi
    public void dumpServicePermissions(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                super.dumpServicePermissions(pw, dumpState);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }
}
