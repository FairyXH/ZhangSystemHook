package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class ResolveIntentHelper {
    private final android.content.Context mContext;
    private final com.android.server.pm.verify.domain.DomainVerificationManagerInternal mDomainVerificationManager;
    private final java.util.function.Supplier<android.content.pm.ActivityInfo> mInstantAppInstallerActivitySupplier;
    private final com.android.server.compat.PlatformCompat mPlatformCompat;
    private final com.android.server.pm.PreferredActivityHelper mPreferredActivityHelper;
    private final java.util.function.Supplier<android.content.pm.ResolveInfo> mResolveInfoSupplier;
    private final com.android.server.pm.IResolveIntentHelperExt mResolveIntentHelperExt = (com.android.server.pm.IResolveIntentHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IResolveIntentHelperExt.class).base(this).create();
    private final com.android.server.pm.UserManagerService mUserManager;
    private final com.android.server.pm.UserNeedsBadgingCache mUserNeedsBadging;

    ResolveIntentHelper(android.content.Context context, com.android.server.pm.PreferredActivityHelper preferredActivityHelper, com.android.server.compat.PlatformCompat platformCompat, com.android.server.pm.UserManagerService userManager, com.android.server.pm.verify.domain.DomainVerificationManagerInternal domainVerificationManager, com.android.server.pm.UserNeedsBadgingCache userNeedsBadgingCache, java.util.function.Supplier<android.content.pm.ResolveInfo> resolveInfoSupplier, java.util.function.Supplier<android.content.pm.ActivityInfo> instantAppInstallerActivitySupplier) {
        this.mContext = context;
        this.mPreferredActivityHelper = preferredActivityHelper;
        this.mPlatformCompat = platformCompat;
        this.mUserManager = userManager;
        this.mDomainVerificationManager = domainVerificationManager;
        this.mUserNeedsBadging = userNeedsBadgingCache;
        this.mResolveInfoSupplier = resolveInfoSupplier;
        this.mInstantAppInstallerActivitySupplier = instantAppInstallerActivitySupplier;
    }

    public android.content.pm.ResolveInfo resolveIntentInternal(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, long privateResolveFlags, int userId, boolean resolveForStart, int filterCallingUid, int callingPid) {
        long j;
        try {
            android.os.Trace.traceBegin(262144L, "resolveIntent");
            if (!this.mUserManager.exists(userId)) {
                android.os.Trace.traceEnd(262144L);
                return null;
            }
            int callingUid = android.os.Binder.getCallingUid();
            long flags2 = computer.updateFlagsForResolve(flags, userId, filterCallingUid, resolveForStart, computer.isImplicitImageCaptureIntentAndNotSetByDpc(intent, userId, resolvedType, flags));
            try {
                computer.enforceCrossUserPermission(callingUid, userId, false, false, "resolve intent");
                this.mResolveIntentHelperExt.interceptHandler(intent);
                long flags3 = this.mResolveIntentHelperExt.interceptHttpAppDetails(intent) ? 0L : flags2;
                try {
                    android.os.Trace.traceBegin(262144L, "queryIntentActivities");
                    j = 262144;
                    try {
                        java.util.List<android.content.pm.ResolveInfo> query = computer.queryIntentActivitiesInternal(intent, resolvedType, flags3, privateResolveFlags, filterCallingUid, callingPid, userId, resolveForStart, true);
                        android.os.Trace.traceEnd(262144L);
                        if (resolveForStart) {
                            com.android.server.pm.SaferIntentUtils.IntentArgs args = new com.android.server.pm.SaferIntentUtils.IntentArgs(intent, resolvedType, false, true, filterCallingUid, callingPid);
                            args.platformCompat = this.mPlatformCompat;
                            com.android.server.pm.SaferIntentUtils.filterNonExportedComponents(args, query);
                        }
                        boolean z = true;
                        boolean queryMayBeFiltered = android.os.UserHandle.getAppId(filterCallingUid) >= 10000 && !resolveForStart;
                        try {
                            this.mResolveIntentHelperExt.filterResolveInfoForMultiApp(intent, query, userId, callingPid);
                            android.content.pm.ResolveInfo bestChoice = chooseBestActivity(computer, intent, resolvedType, flags3, privateResolveFlags, query, userId, queryMayBeFiltered);
                            if ((privateResolveFlags & 1) == 0) {
                                z = false;
                            }
                            boolean nonBrowserOnly = z;
                            if (nonBrowserOnly && bestChoice != null) {
                                if (bestChoice.handleAllWebDataURI) {
                                    android.os.Trace.traceEnd(262144L);
                                    return null;
                                }
                            }
                            android.os.Trace.traceEnd(262144L);
                            return bestChoice;
                        } catch (java.lang.Throwable th) {
                            th = th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    j = 262144;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
                j = 262144;
            }
        } catch (java.lang.Throwable th5) {
            th = th5;
            j = 262144;
        }
        android.os.Trace.traceEnd(j);
        throw th;
    }

    private android.content.pm.ResolveInfo chooseBestActivity(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, long privateResolveFlags, java.util.List<android.content.pm.ResolveInfo> query, int userId, boolean queryMayBeFiltered) {
        int userId2;
        boolean z;
        int browserCount;
        if (query != null) {
            int n = query.size();
            if (n == 1) {
                return query.get(0);
            }
            if (n > 1) {
                boolean debug = (intent.getFlags() & 8) != 0;
                android.content.pm.ResolveInfo interceptMarketInfo = this.mResolveIntentHelperExt.interceptAppDetailsToMarket(intent, query, computer, userId);
                if (interceptMarketInfo != null) {
                    return interceptMarketInfo;
                }
                android.content.pm.ResolveInfo r0 = query.get(0);
                android.content.pm.ResolveInfo r1 = query.get(1);
                if (com.android.server.pm.PackageManagerService.DEBUG_INTENT_MATCHING || debug) {
                    android.util.Slog.v("PackageManager", r0.activityInfo.name + "=" + r0.priority + " vs " + r1.activityInfo.name + "=" + r1.priority);
                }
                if (r0.priority != r1.priority || r0.preferredOrder != r1.preferredOrder || r0.isDefault != r1.isDefault) {
                    int i = 0;
                    return query.get(i);
                }
                android.content.pm.ResolveInfo priorChoose = this.mResolveIntentHelperExt.findPriorBeforeUsePreferenceInChooseBestActivity(intent, query);
                if (priorChoose != null) {
                    return priorChoose;
                }
                int userId3 = this.mResolveIntentHelperExt.changeUserIdInChooseBestActivity(userId, r0);
                android.content.pm.ResolveInfo ri = this.mResolveIntentHelperExt.adjustQueryAndResultForUsePrefInChooseBestActivity(computer, intent, query, this.mPreferredActivityHelper.findPreferredActivityNotLocked(computer, intent, resolvedType, flags, query, true, false, debug, userId3, queryMayBeFiltered));
                if (ri != null) {
                    return ri;
                }
                int n2 = query.size();
                if (n2 == 0) {
                    return null;
                }
                int browserCount2 = 0;
                int i2 = 0;
                while (i2 < n2) {
                    android.content.pm.ResolveInfo ri2 = query.get(i2);
                    if (!ri2.handleAllWebDataURI) {
                        browserCount = browserCount2;
                    } else {
                        browserCount = browserCount2 + 1;
                    }
                    if (ri2.activityInfo.applicationInfo.isInstantApp()) {
                        java.lang.String packageName = ri2.activityInfo.packageName;
                        com.android.server.pm.pkg.PackageStateInternal ps = computer.getPackageStateInternal(packageName);
                        if (ps != null && com.android.server.pm.PackageManagerServiceUtils.hasAnyDomainApproval(this.mDomainVerificationManager, ps, intent, flags, userId3)) {
                            return ri2;
                        }
                    }
                    i2++;
                    browserCount2 = browserCount;
                }
                if ((privateResolveFlags & 2) != 0) {
                    return null;
                }
                android.content.pm.ResolveInfo ri3 = new android.content.pm.ResolveInfo(this.mResolveInfoSupplier.get());
                ri3.handleAllWebDataURI = browserCount2 == n2;
                ri3.activityInfo = new android.content.pm.ActivityInfo(ri3.activityInfo);
                ri3.activityInfo.labelRes = com.android.internal.app.ResolverActivity.getLabelRes(intent.getAction());
                if (ri3.userHandle == null) {
                    ri3.userHandle = android.os.UserHandle.of(userId3);
                }
                java.lang.String intentPackage = intent.getPackage();
                if (android.text.TextUtils.isEmpty(intentPackage) || !allHavePackage(query, intentPackage)) {
                    userId2 = userId3;
                    z = true;
                } else {
                    android.content.pm.ApplicationInfo appi = query.get(0).activityInfo.applicationInfo;
                    ri3.resolvePackageName = intentPackage;
                    userId2 = userId3;
                    if (this.mUserNeedsBadging.get(userId2)) {
                        z = true;
                        ri3.noResourceId = true;
                    } else {
                        z = true;
                        ri3.icon = appi.icon;
                    }
                    ri3.iconResourceId = appi.icon;
                    ri3.labelRes = appi.labelRes;
                }
                ri3.activityInfo.applicationInfo = new android.content.pm.ApplicationInfo(ri3.activityInfo.applicationInfo);
                if (userId2 != 0) {
                    ri3.activityInfo.applicationInfo.uid = android.os.UserHandle.getUid(userId2, android.os.UserHandle.getAppId(ri3.activityInfo.applicationInfo.uid));
                }
                if (ri3.activityInfo.metaData == null) {
                    ri3.activityInfo.metaData = new android.os.Bundle();
                }
                ri3.activityInfo.metaData.putBoolean("android.dock_home", z);
                return ri3;
            }
        }
        return null;
    }

    private boolean allHavePackage(java.util.List<android.content.pm.ResolveInfo> list, java.lang.String packageName) {
        if (com.android.internal.util.ArrayUtils.isEmpty(list)) {
            return false;
        }
        int n = list.size();
        for (int i = 0; i < n; i++) {
            android.content.pm.ResolveInfo ri = list.get(i);
            android.content.pm.ActivityInfo ai = ri != null ? ri.activityInfo : null;
            if (ai == null || !packageName.equals(ai.packageName)) {
                return false;
            }
        }
        return true;
    }

    public android.content.IntentSender getLaunchIntentSenderForPackage(com.android.server.pm.Computer computer, java.lang.String packageName, java.lang.String callingPackage, java.lang.String featureId, int userId) throws android.os.RemoteException {
        android.content.Intent intentToResolve;
        java.util.List<android.content.pm.ResolveInfo> ris;
        java.util.Objects.requireNonNull(packageName);
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        computer.enforceCrossUserPermission(callingUid, userId, false, false, "get launch intent sender for package");
        int packageUid = computer.getPackageUid(callingPackage, 0L, userId);
        if (!android.os.UserHandle.isSameApp(callingUid, packageUid)) {
            throw new java.lang.SecurityException("getLaunchIntentSenderForPackage() from calling uid: " + callingUid + " does not own package: " + callingPackage);
        }
        android.content.Intent intentToResolve2 = new android.content.Intent("android.intent.action.MAIN");
        intentToResolve2.addCategory("android.intent.category.INFO");
        intentToResolve2.setPackage(packageName);
        android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
        java.lang.String resolvedType = intentToResolve2.resolveTypeIfNeeded(contentResolver);
        java.util.List<android.content.pm.ResolveInfo> ris2 = computer.queryIntentActivitiesInternal(intentToResolve2, resolvedType, 0L, 0L, callingUid, callingPid, userId, true, false);
        if (ris2 == null || ris2.size() <= 0) {
            intentToResolve2.removeCategory("android.intent.category.INFO");
            intentToResolve2.addCategory("android.intent.category.LAUNCHER");
            intentToResolve2.setPackage(packageName);
            resolvedType = intentToResolve2.resolveTypeIfNeeded(contentResolver);
            intentToResolve = intentToResolve2;
            ris = computer.queryIntentActivitiesInternal(intentToResolve2, resolvedType, 0L, 0L, callingUid, callingPid, userId, true, false);
        } else {
            ris = ris2;
            intentToResolve = intentToResolve2;
        }
        android.content.Intent intent = new android.content.Intent(intentToResolve);
        intent.setFlags(268435456);
        if (ris != null && !ris.isEmpty()) {
            intent.setPackage(null);
            intent.setClassName(ris.get(0).activityInfo.packageName, ris.get(0).activityInfo.name);
        }
        android.content.IIntentSender target = android.app.ActivityManager.getService().getIntentSenderWithFeature(2, callingPackage, featureId, (android.os.IBinder) null, (java.lang.String) null, 1, new android.content.Intent[]{intent}, resolvedType != null ? new java.lang.String[]{resolvedType} : null, 67108864, (android.os.Bundle) null, userId);
        return new android.content.IntentSender(target);
    }

    public java.util.List<android.content.pm.ResolveInfo> queryIntentReceiversInternal(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId, int queryingUid) {
        return queryIntentReceiversInternal(computer, intent, resolvedType, flags, userId, queryingUid, -1, false);
    }

    public java.util.List<android.content.pm.ResolveInfo> queryIntentReceiversInternal(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId, int filterCallingUid, int callingPid, boolean forSend) {
        android.content.Intent intent2;
        android.content.ComponentName comp;
        android.content.Intent originalIntent;
        com.android.server.pm.SaferIntentUtils.IntentArgs args;
        java.util.List<android.content.pm.ResolveInfo> list;
        java.util.List<android.content.pm.ResolveInfo> list2;
        android.content.Intent originalIntent2;
        if (!this.mUserManager.exists(userId)) {
            return java.util.Collections.emptyList();
        }
        int queryingUid = forSend ? 1000 : filterCallingUid;
        computer.enforceCrossUserPermission(queryingUid, userId, false, false, "query intent receivers");
        java.lang.String instantAppPkgName = computer.getInstantAppPackageName(queryingUid);
        long flags2 = computer.updateFlagsForResolve(flags, userId, queryingUid, false, computer.isImplicitImageCaptureIntentAndNotSetByDpc(intent, userId, resolvedType, flags));
        android.content.Intent originalIntent3 = null;
        android.content.ComponentName comp2 = intent.getComponent();
        if (comp2 == null && intent.getSelector() != null) {
            originalIntent3 = intent;
            android.content.Intent intent3 = intent.getSelector();
            comp = intent3.getComponent();
            intent2 = intent3;
        } else {
            intent2 = intent;
            comp = comp2;
        }
        com.android.server.pm.resolution.ComponentResolverApi componentResolver = computer.getComponentResolver();
        java.util.List<android.content.pm.ResolveInfo> list3 = java.util.Collections.emptyList();
        android.content.ComponentName comp3 = comp;
        com.android.server.pm.SaferIntentUtils.IntentArgs args2 = new com.android.server.pm.SaferIntentUtils.IntentArgs(intent2, resolvedType, true, forSend, filterCallingUid, callingPid);
        args2.platformCompat = this.mPlatformCompat;
        args2.snapshot = computer;
        if (comp3 != null) {
            android.content.pm.ActivityInfo ai = computer.getReceiverInfo(comp3, flags2, userId);
            if (ai == null) {
                originalIntent2 = originalIntent3;
            } else {
                boolean matchInstantApp = (8388608 & flags2) != 0;
                boolean matchVisibleToInstantAppOnly = (flags2 & 16777216) != 0;
                boolean matchExplicitlyVisibleOnly = (flags2 & 33554432) != 0;
                boolean isCallerInstantApp = instantAppPkgName != null;
                boolean isTargetSameInstantApp = comp3.getPackageName().equals(instantAppPkgName);
                boolean isTargetInstantApp = (ai.applicationInfo.privateFlags & 128) != 0;
                originalIntent2 = originalIntent3;
                boolean isTargetVisibleToInstantApp = (ai.flags & 1048576) != 0;
                boolean isTargetExplicitlyVisibleToInstantApp = isTargetVisibleToInstantApp && (ai.flags & 2097152) == 0;
                boolean isTargetHiddenFromInstantApp = !isTargetVisibleToInstantApp || (matchExplicitlyVisibleOnly && !isTargetExplicitlyVisibleToInstantApp);
                boolean blockResolution = !isTargetSameInstantApp && (!(matchInstantApp || isCallerInstantApp || !isTargetInstantApp) || (matchVisibleToInstantAppOnly && isCallerInstantApp && isTargetHiddenFromInstantApp));
                if (!blockResolution) {
                    android.content.pm.ResolveInfo ri = new android.content.pm.ResolveInfo();
                    ri.activityInfo = ai;
                    java.util.List<android.content.pm.ResolveInfo> list4 = new java.util.ArrayList<>(1);
                    list4.add(ri);
                    com.android.server.pm.SaferIntentUtils.enforceIntentFilterMatching(args2, list4);
                    list3 = list4;
                }
            }
            args = args2;
            list2 = list3;
            originalIntent = originalIntent2;
        } else {
            android.content.Intent originalIntent4 = originalIntent3;
            java.lang.String pkgName = intent2.getPackage();
            if (pkgName == null) {
                originalIntent = originalIntent4;
                java.util.List<android.content.pm.ResolveInfo> result = componentResolver.queryReceivers(computer, intent2, resolvedType, flags2, userId);
                if (result != null) {
                    list3 = result;
                }
            } else {
                originalIntent = originalIntent4;
            }
            com.android.server.pm.pkg.AndroidPackage pkg = computer.getPackage(pkgName);
            if (pkg == null) {
                args = args2;
            } else {
                args = args2;
                java.util.List<android.content.pm.ResolveInfo> result2 = componentResolver.queryReceivers(computer, intent2, resolvedType, flags2, pkg.getReceivers(), userId);
                if (result2 != null) {
                    list = result2;
                }
                com.android.server.pm.SaferIntentUtils.blockNullAction(args, list);
                list2 = list;
            }
            list = list3;
            com.android.server.pm.SaferIntentUtils.blockNullAction(args, list);
            list2 = list;
        }
        if (originalIntent != null) {
            args.intent = originalIntent;
            com.android.server.pm.SaferIntentUtils.enforceIntentFilterMatching(args, list2);
        }
        this.mResolveIntentHelperExt.adjustResultBeforeApplyPostResolutionFilter(intent2, list2);
        return computer.applyPostResolutionFilter(list2, instantAppPkgName, false, queryingUid, false, userId, intent2);
    }

    public android.content.pm.ResolveInfo resolveServiceInternal(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId, int callingUid, int callingPid, boolean resolveForStart) {
        java.util.List<android.content.pm.ResolveInfo> query;
        if (this.mUserManager.exists(userId) && (query = computer.queryIntentServicesInternal(intent, resolvedType, computer.updateFlagsForResolve(flags, userId, callingUid, false, false), userId, callingUid, callingPid, false, resolveForStart)) != null && query.size() >= 1) {
            return query.get(0);
        }
        return null;
    }

    public java.util.List<android.content.pm.ResolveInfo> queryIntentContentProvidersInternal(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        android.content.Intent intent2;
        android.content.ComponentName comp;
        if (!this.mUserManager.exists(userId)) {
            return java.util.Collections.emptyList();
        }
        int callingUid = android.os.Binder.getCallingUid();
        java.lang.String instantAppPkgName = computer.getInstantAppPackageName(callingUid);
        long flags2 = computer.updateFlagsForResolve(flags, userId, callingUid, false, false);
        android.content.ComponentName comp2 = intent.getComponent();
        if (comp2 == null && intent.getSelector() != null) {
            android.content.Intent intent3 = intent.getSelector();
            comp = intent3.getComponent();
            intent2 = intent3;
        } else {
            intent2 = intent;
            comp = comp2;
        }
        if (comp != null) {
            java.util.List<android.content.pm.ResolveInfo> list = new java.util.ArrayList<>(1);
            android.content.pm.ProviderInfo pi = computer.getProviderInfo(comp, flags2, userId);
            if (pi != null) {
                boolean matchInstantApp = (8388608 & flags2) != 0;
                boolean matchVisibleToInstantAppOnly = (flags2 & 16777216) != 0;
                boolean isCallerInstantApp = instantAppPkgName != null;
                boolean isTargetSameInstantApp = comp.getPackageName().equals(instantAppPkgName);
                boolean isTargetInstantApp = (pi.applicationInfo.privateFlags & 128) != 0;
                boolean isTargetHiddenFromInstantApp = (pi.flags & 1048576) == 0;
                boolean blockResolution = !isTargetSameInstantApp && (!(matchInstantApp || isCallerInstantApp || !isTargetInstantApp) || (matchVisibleToInstantAppOnly && isCallerInstantApp && isTargetHiddenFromInstantApp));
                if (!isTargetInstantApp && !isCallerInstantApp) {
                    boolean blockNormalResolution = computer.shouldFilterApplication(computer.getPackageStateInternal(pi.applicationInfo.packageName, 1000), callingUid, userId);
                    if (!blockResolution && !blockNormalResolution) {
                        android.content.pm.ResolveInfo ri = new android.content.pm.ResolveInfo();
                        ri.providerInfo = pi;
                        list.add(ri);
                    }
                }
                if (!blockResolution) {
                    android.content.pm.ResolveInfo ri2 = new android.content.pm.ResolveInfo();
                    ri2.providerInfo = pi;
                    list.add(ri2);
                }
            }
            return list;
        }
        com.android.server.pm.resolution.ComponentResolverApi componentResolver = computer.getComponentResolver();
        java.lang.String pkgName = intent2.getPackage();
        if (pkgName == null) {
            java.util.List<android.content.pm.ResolveInfo> resolveInfos = componentResolver.queryProviders(computer, intent2, resolvedType, flags2, userId);
            if (resolveInfos == null) {
                return java.util.Collections.emptyList();
            }
            return applyPostContentProviderResolutionFilter(computer, resolveInfos, instantAppPkgName, userId, callingUid);
        }
        com.android.server.pm.pkg.AndroidPackage pkg = computer.getPackage(pkgName);
        if (pkg != null) {
            java.util.List<android.content.pm.ResolveInfo> resolveInfos2 = componentResolver.queryProviders(computer, intent2, resolvedType, flags2, pkg.getProviders(), userId);
            if (resolveInfos2 == null) {
                return java.util.Collections.emptyList();
            }
            return applyPostContentProviderResolutionFilter(computer, resolveInfos2, instantAppPkgName, userId, callingUid);
        }
        return java.util.Collections.emptyList();
    }

    private java.util.List<android.content.pm.ResolveInfo> applyPostContentProviderResolutionFilter(com.android.server.pm.Computer computer, java.util.List<android.content.pm.ResolveInfo> resolveInfos, java.lang.String instantAppPkgName, int userId, int callingUid) {
        com.android.server.pm.Computer computer2 = computer;
        int i = resolveInfos.size() - 1;
        while (i >= 0) {
            android.content.pm.ResolveInfo info = resolveInfos.get(i);
            if (instantAppPkgName == null) {
                com.android.server.pm.pkg.PackageStateInternal resolvedSetting = computer2.getPackageStateInternal(info.providerInfo.packageName, 0);
                if (!computer2.shouldFilterApplication(resolvedSetting, callingUid, userId)) {
                }
                i--;
                computer2 = computer;
            }
            boolean isEphemeralApp = info.providerInfo.applicationInfo.isInstantApp();
            if (isEphemeralApp && instantAppPkgName.equals(info.providerInfo.packageName)) {
                if (info.providerInfo.splitName != null && !com.android.internal.util.ArrayUtils.contains(info.providerInfo.applicationInfo.splitNames, info.providerInfo.splitName)) {
                    if (this.mInstantAppInstallerActivitySupplier.get() == null) {
                        if (com.android.server.pm.PackageManagerService.DEBUG_INSTANT) {
                            android.util.Slog.v("PackageManager", "No installer - not adding it to the ResolveInfo list");
                        }
                        resolveInfos.remove(i);
                    } else {
                        if (com.android.server.pm.PackageManagerService.DEBUG_INSTANT) {
                            android.util.Slog.v("PackageManager", "Adding ephemeral installer to the ResolveInfo list");
                        }
                        android.content.pm.ResolveInfo installerInfo = new android.content.pm.ResolveInfo(computer.getInstantAppInstallerInfo());
                        installerInfo.auxiliaryInfo = new android.content.pm.AuxiliaryResolveInfo((android.content.ComponentName) null, info.providerInfo.packageName, info.providerInfo.applicationInfo.longVersionCode, info.providerInfo.splitName);
                        installerInfo.filter = new android.content.IntentFilter();
                        installerInfo.resolvePackageName = info.getComponentInfo().packageName;
                        resolveInfos.set(i, installerInfo);
                    }
                }
            } else if (isEphemeralApp || (info.providerInfo.flags & 1048576) == 0) {
                resolveInfos.remove(i);
            }
            i--;
            computer2 = computer;
        }
        return resolveInfos;
    }

    /* JADX WARN: Incorrect condition in loop: B:14:0x0080 */
    /* JADX WARN: Removed duplicated region for block: B:41:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0242  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivityOptionsInternal(com.android.server.pm.Computer r40, android.content.ComponentName r41, android.content.Intent[] r42, java.lang.String[] r43, android.content.Intent r44, java.lang.String r45, long r46, int r48) {
        /*
            Method dump skipped, instruction units count: 909
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ResolveIntentHelper.queryIntentActivityOptionsInternal(com.android.server.pm.Computer, android.content.ComponentName, android.content.Intent[], java.lang.String[], android.content.Intent, java.lang.String, long, int):java.util.List");
    }
}
