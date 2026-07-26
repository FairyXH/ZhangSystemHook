package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class CrossProfileIntentResolverEngine {
    private com.android.internal.config.appcloning.AppCloningDeviceConfigHelper mAppCloningDeviceConfigHelper;
    private final android.content.Context mContext;
    private final com.android.server.pm.DefaultAppProvider mDefaultAppProvider;
    private final com.android.server.pm.verify.domain.DomainVerificationManagerInternal mDomainVerificationManager;
    private final com.android.server.pm.UserManagerService mUserManager;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
    private com.android.server.pm.ICrossProfileIntentResolverEngineExt mCrossProfileIntentResolverEngineExt = (com.android.server.pm.ICrossProfileIntentResolverEngineExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.ICrossProfileIntentResolverEngineExt.class).base(this).create();

    public CrossProfileIntentResolverEngine(com.android.server.pm.UserManagerService userManager, com.android.server.pm.verify.domain.DomainVerificationManagerInternal domainVerificationManager, com.android.server.pm.DefaultAppProvider defaultAppProvider, android.content.Context context) {
        this.mUserManager = userManager;
        this.mDomainVerificationManager = domainVerificationManager;
        this.mDefaultAppProvider = defaultAppProvider;
        this.mContext = context;
    }

    public java.util.List<com.android.server.pm.CrossProfileDomainInfo> resolveIntent(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, int userId, long flags, java.lang.String pkgName, boolean hasNonNegativePriorityResult, boolean resolveForStart, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        return resolveIntentInternal(computer, intent, resolvedType, userId, userId, flags, pkgName, hasNonNegativePriorityResult, resolveForStart, pkgSettingFunction, null);
    }

    private java.util.List<com.android.server.pm.CrossProfileDomainInfo> resolveIntentInternal(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, int sourceUserId, int userId, long flags, java.lang.String pkgName, boolean hasNonNegativePriorityResult, boolean resolveForStart, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction, java.util.Set<java.lang.Integer> visitedUserIds) {
        com.android.server.pm.CrossProfileDomainInfo generalizedCrossProfileDomainInfo;
        int i;
        java.util.Set<java.lang.Integer> visitedUserIds2;
        java.util.List<com.android.server.pm.CrossProfileIntentFilter> matchingFilters;
        int index;
        android.util.SparseArray<java.util.List<com.android.server.pm.CrossProfileIntentFilter>> crossProfileIntentFiltersByUser;
        java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos;
        android.util.SparseArray<java.util.List<com.android.server.pm.CrossProfileIntentFilter>> crossProfileIntentFiltersByUser2;
        int index2;
        boolean allowChainedResolution;
        com.android.server.pm.CrossProfileIntentResolverEngine crossProfileIntentResolverEngine = this;
        int i2 = userId;
        java.util.Set<java.lang.Integer> visitedUserIds3 = visitedUserIds;
        if (visitedUserIds3 != null) {
            visitedUserIds3.add(java.lang.Integer.valueOf(userId));
        }
        java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos2 = new java.util.ArrayList<>();
        java.util.List<com.android.server.pm.CrossProfileIntentFilter> matchingFilters2 = computer.getMatchingCrossProfileIntentFilters(intent, resolvedType, i2);
        if (matchingFilters2 == null || matchingFilters2.isEmpty()) {
            java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos3 = crossProfileDomainInfos2;
            int i3 = i2;
            if (sourceUserId == i3 && intent.hasWebURI()) {
                android.content.pm.UserInfo parent = computer.getProfileParent(i3);
                if (parent != null && (generalizedCrossProfileDomainInfo = computer.getCrossProfileDomainPreferredLpr(intent, resolvedType, flags, userId, parent.id)) != null) {
                    crossProfileDomainInfos3.add(generalizedCrossProfileDomainInfo);
                }
            }
            return crossProfileDomainInfos3;
        }
        android.util.SparseArray<java.util.List<com.android.server.pm.CrossProfileIntentFilter>> crossProfileIntentFiltersByUser3 = new android.util.SparseArray<>();
        for (int index3 = 0; index3 < matchingFilters2.size(); index3++) {
            com.android.server.pm.CrossProfileIntentFilter crossProfileIntentFilter = matchingFilters2.get(index3);
            if (!crossProfileIntentFiltersByUser3.contains(crossProfileIntentFilter.mTargetUserId)) {
                crossProfileIntentFiltersByUser3.put(crossProfileIntentFilter.mTargetUserId, new java.util.ArrayList<>());
            }
            crossProfileIntentFiltersByUser3.get(crossProfileIntentFilter.mTargetUserId).add(crossProfileIntentFilter);
        }
        if (visitedUserIds3 == null) {
            java.util.Set<java.lang.Integer> visitedUserIds4 = new java.util.HashSet<>();
            visitedUserIds4.add(java.lang.Integer.valueOf(userId));
            visitedUserIds3 = visitedUserIds4;
        }
        int index4 = 0;
        while (index4 < crossProfileIntentFiltersByUser3.size()) {
            int targetUserId = crossProfileIntentFiltersByUser3.keyAt(index4);
            if (visitedUserIds3.contains(java.lang.Integer.valueOf(targetUserId))) {
                crossProfileIntentFiltersByUser = crossProfileIntentFiltersByUser3;
                index = index4;
                visitedUserIds2 = visitedUserIds3;
                matchingFilters = matchingFilters2;
                crossProfileDomainInfos = crossProfileDomainInfos2;
                i = i2;
            } else {
                android.util.SparseArray<java.util.List<com.android.server.pm.CrossProfileIntentFilter>> crossProfileIntentFiltersByUser4 = crossProfileIntentFiltersByUser3;
                int index5 = index4;
                com.android.server.pm.CrossProfileResolver crossProfileResolver = chooseCrossProfileResolver(computer, userId, targetUserId, resolveForStart, flags);
                if (crossProfileResolver == null) {
                    i = userId;
                    visitedUserIds2 = visitedUserIds3;
                    matchingFilters = matchingFilters2;
                    index = index5;
                    crossProfileIntentFiltersByUser = crossProfileIntentFiltersByUser4;
                    crossProfileDomainInfos = crossProfileDomainInfos2;
                } else {
                    java.util.List<com.android.server.pm.CrossProfileIntentFilter> listValueAt = crossProfileIntentFiltersByUser4.valueAt(index5);
                    int index6 = index5;
                    java.util.Set<java.lang.Integer> visitedUserIds5 = visitedUserIds3;
                    matchingFilters = matchingFilters2;
                    android.util.SparseArray<java.util.List<com.android.server.pm.CrossProfileIntentFilter>> crossProfileIntentFiltersByUser5 = crossProfileIntentFiltersByUser4;
                    crossProfileDomainInfos = crossProfileDomainInfos2;
                    java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileInfos = crossProfileResolver.resolveIntent(computer, intent, resolvedType, userId, targetUserId, flags, pkgName, listValueAt, hasNonNegativePriorityResult, pkgSettingFunction);
                    crossProfileIntentResolverEngine.mCrossProfileIntentResolverEngineExt.checkIfSkipCrossProfile(sourceUserId, targetUserId, crossProfileInfos);
                    crossProfileDomainInfos.addAll(crossProfileInfos);
                    visitedUserIds5.add(java.lang.Integer.valueOf(targetUserId));
                    int filterIndex = 0;
                    while (true) {
                        crossProfileIntentFiltersByUser2 = crossProfileIntentFiltersByUser5;
                        index2 = index6;
                        if (filterIndex >= crossProfileIntentFiltersByUser2.valueAt(index2).size()) {
                            allowChainedResolution = false;
                            break;
                        }
                        if ((crossProfileIntentFiltersByUser2.valueAt(index2).get(filterIndex).mFlags & 16) == 0) {
                            filterIndex++;
                            index6 = index2;
                            crossProfileIntentFiltersByUser5 = crossProfileIntentFiltersByUser2;
                        } else {
                            allowChainedResolution = true;
                            break;
                        }
                    }
                    if (!allowChainedResolution) {
                        i = userId;
                        index = index2;
                        crossProfileIntentFiltersByUser = crossProfileIntentFiltersByUser2;
                        visitedUserIds2 = visitedUserIds5;
                    } else {
                        boolean zHasNonNegativePriority = crossProfileIntentResolverEngine.hasNonNegativePriority(crossProfileInfos);
                        index = index2;
                        crossProfileIntentFiltersByUser = crossProfileIntentFiltersByUser2;
                        i = userId;
                        visitedUserIds2 = visitedUserIds5;
                        crossProfileDomainInfos.addAll(resolveIntentInternal(computer, intent, resolvedType, sourceUserId, targetUserId, flags, pkgName, zHasNonNegativePriority, resolveForStart, pkgSettingFunction, visitedUserIds5));
                    }
                }
            }
            index4 = index + 1;
            i2 = i;
            crossProfileDomainInfos2 = crossProfileDomainInfos;
            matchingFilters2 = matchingFilters;
            crossProfileIntentFiltersByUser3 = crossProfileIntentFiltersByUser;
            visitedUserIds3 = visitedUserIds2;
            crossProfileIntentResolverEngine = this;
        }
        return crossProfileDomainInfos2;
    }

    private com.android.server.pm.CrossProfileResolver chooseCrossProfileResolver(com.android.server.pm.Computer computer, int sourceUserId, int targetUserId, boolean resolveForStart, long flags) {
        if (shouldUseNoFilteringResolver(sourceUserId, targetUserId)) {
            if (this.mAppCloningDeviceConfigHelper == null) {
                this.mAppCloningDeviceConfigHelper = com.android.internal.config.appcloning.AppCloningDeviceConfigHelper.getInstance(this.mContext);
            }
            if (com.android.server.pm.NoFilteringResolver.isIntentRedirectionAllowed(this.mContext, this.mAppCloningDeviceConfigHelper, resolveForStart, flags)) {
                return new com.android.server.pm.NoFilteringResolver(computer.getComponentResolver(), this.mUserManager);
            }
            return null;
        }
        return new com.android.server.pm.DefaultCrossProfileResolver(computer.getComponentResolver(), this.mUserManager, this.mDomainVerificationManager);
    }

    public boolean canReachTo(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, int sourceUserId, int targetUserId) {
        java.util.Set<java.lang.Integer> visitedUserIds = new java.util.HashSet<>();
        return canReachToInternal(computer, intent, resolvedType, sourceUserId, targetUserId, visitedUserIds);
    }

    private boolean canReachToInternal(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, int sourceUserId, int targetUserId, java.util.Set<java.lang.Integer> visitedUserIds) {
        if (sourceUserId == targetUserId) {
            return true;
        }
        visitedUserIds.add(java.lang.Integer.valueOf(sourceUserId));
        java.util.List<com.android.server.pm.CrossProfileIntentFilter> matches = computer.getMatchingCrossProfileIntentFilters(intent, resolvedType, sourceUserId);
        if (matches != null) {
            for (int index = 0; index < matches.size(); index++) {
                com.android.server.pm.CrossProfileIntentFilter crossProfileIntentFilter = matches.get(index);
                if (crossProfileIntentFilter.mTargetUserId == targetUserId) {
                    return true;
                }
                if (!visitedUserIds.contains(java.lang.Integer.valueOf(crossProfileIntentFilter.mTargetUserId)) && (crossProfileIntentFilter.mFlags & 16) != 0) {
                    visitedUserIds.add(java.lang.Integer.valueOf(crossProfileIntentFilter.mTargetUserId));
                    if (canReachToInternal(computer, intent, resolvedType, crossProfileIntentFilter.mTargetUserId, targetUserId, visitedUserIds)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public boolean shouldSkipCurrentProfile(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, int sourceUserId) {
        java.util.List<com.android.server.pm.CrossProfileIntentFilter> matches = computer.getMatchingCrossProfileIntentFilters(intent, resolvedType, sourceUserId);
        if (matches != null) {
            for (int matchIndex = 0; matchIndex < matches.size(); matchIndex++) {
                com.android.server.pm.CrossProfileIntentFilter crossProfileIntentFilter = matches.get(matchIndex);
                if ((crossProfileIntentFilter.getFlags() & 2) != 0) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public com.android.server.pm.QueryIntentActivitiesResult combineFilterAndCreateQueryActivitiesResponse(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, java.lang.String instantAppPkgName, java.lang.String pkgName, boolean allowDynamicSplits, long matchFlags, int userId, int filterCallingUid, boolean resolveForStart, java.util.List<android.content.pm.ResolveInfo> candidates, java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileCandidates, boolean areWebInstantAppsDisabled, boolean addInstant, boolean sortResult, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        int i;
        android.content.Intent intent2;
        java.util.List<android.content.pm.ResolveInfo> candidates2 = candidates;
        if (shouldSkipCurrentProfile(computer, intent, resolvedType, userId)) {
            return new com.android.server.pm.QueryIntentActivitiesResult(computer.applyPostResolutionFilter(resolveInfoFromCrossProfileDomainInfo(crossProfileCandidates), instantAppPkgName, allowDynamicSplits, filterCallingUid, resolveForStart, userId, intent));
        }
        if (pkgName != null || !intent.hasWebURI()) {
            i = 1;
            candidates2.addAll(resolveInfoFromCrossProfileDomainInfo(crossProfileCandidates));
        } else if (addInstant || ((candidates.size() > 1 || !crossProfileCandidates.isEmpty()) && (!candidates.isEmpty() || crossProfileCandidates.isEmpty()))) {
            i = 1;
            candidates2 = filterCandidatesWithDomainPreferredActivitiesLPr(computer, intent, matchFlags, candidates, crossProfileCandidates, userId, areWebInstantAppsDisabled, resolveForStart, pkgSettingFunction);
        } else {
            candidates2.addAll(resolveInfoFromCrossProfileDomainInfo(crossProfileCandidates));
            return new com.android.server.pm.QueryIntentActivitiesResult(computer.applyPostResolutionFilter(candidates, instantAppPkgName, allowDynamicSplits, filterCallingUid, resolveForStart, userId, intent));
        }
        if (candidates2.size() != i || android.os.UserHandle.of(userId).equals(candidates2.get(0).userHandle) || !isNoFilteringPropertyConfiguredForUser(userId)) {
            intent2 = intent;
        } else {
            intent2 = intent;
            intent2.prepareToLeaveUser(userId);
        }
        this.mCrossProfileIntentResolverEngineExt.filterDuplicateCandidatesByMultiAppFlag(crossProfileCandidates, candidates2, intent2);
        return new com.android.server.pm.QueryIntentActivitiesResult(sortResult, addInstant, candidates2);
    }

    private java.util.List<android.content.pm.ResolveInfo> filterCandidatesWithDomainPreferredActivitiesLPr(com.android.server.pm.Computer computer, android.content.Intent intent, long matchFlags, java.util.List<android.content.pm.ResolveInfo> candidates, java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileCandidates, int userId, boolean areWebInstantAppsDisabled, boolean resolveForStart, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        boolean debug = (intent.getFlags() & 8) != 0;
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || com.android.server.pm.PackageManagerService.DEBUG_DOMAIN_VERIFICATION) {
            android.util.Slog.v("PackageManager", "Filtering results with preferred activities. Candidates count: " + candidates.size());
        }
        java.util.List<android.content.pm.ResolveInfo> result = filterCandidatesWithDomainPreferredActivitiesLPrBody(computer, intent, matchFlags, candidates, crossProfileCandidates, userId, areWebInstantAppsDisabled, debug, resolveForStart, pkgSettingFunction);
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || com.android.server.pm.PackageManagerService.DEBUG_DOMAIN_VERIFICATION) {
            android.util.Slog.v("PackageManager", "Filtered results with preferred activities. New candidates count: " + result.size());
            for (android.content.pm.ResolveInfo info : result) {
                android.util.Slog.v("PackageManager", " + " + info.activityInfo);
            }
        }
        return result;
    }

    private java.util.List<android.content.pm.ResolveInfo> filterCandidatesWithDomainPreferredActivitiesLPrBody(com.android.server.pm.Computer computer, android.content.Intent intent, long matchFlags, java.util.List<android.content.pm.ResolveInfo> candidates, java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileCandidates, final int userId, boolean areWebInstantAppsDisabled, boolean debug, boolean resolveForStart, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        boolean includeBrowser;
        java.util.ArrayList<android.content.pm.ResolveInfo> result = new java.util.ArrayList<>();
        java.util.ArrayList<android.content.pm.ResolveInfo> matchAllList = new java.util.ArrayList<>();
        java.util.ArrayList<android.content.pm.ResolveInfo> undefinedList = new java.util.ArrayList<>();
        boolean blockInstant = intent.isWebIntent() && areWebInstantAppsDisabled;
        int count = candidates.size();
        for (int n = 0; n < count; n++) {
            android.content.pm.ResolveInfo info = candidates.get(n);
            if (!blockInstant || (!info.isInstantAppAvailable && !computer.isInstantAppInternal(info.activityInfo.packageName, userId, 1000))) {
                if (info.handleAllWebDataURI) {
                    matchAllList.add(info);
                } else {
                    undefinedList.add(info);
                }
            }
        }
        android.util.SparseArray<java.util.List<com.android.server.pm.CrossProfileDomainInfo>> categorizeResolveInfoByTargetUser = new android.util.SparseArray<>();
        if (crossProfileCandidates != null && !crossProfileCandidates.isEmpty()) {
            for (int index = 0; index < crossProfileCandidates.size(); index++) {
                com.android.server.pm.CrossProfileDomainInfo crossProfileDomainInfo = crossProfileCandidates.get(index);
                if (!categorizeResolveInfoByTargetUser.contains(crossProfileDomainInfo.mTargetUserId)) {
                    categorizeResolveInfoByTargetUser.put(crossProfileDomainInfo.mTargetUserId, new java.util.ArrayList());
                }
                categorizeResolveInfoByTargetUser.get(crossProfileDomainInfo.mTargetUserId).add(crossProfileDomainInfo);
            }
        }
        if (!com.android.server.pm.verify.domain.DomainVerificationUtils.isDomainVerificationIntent(intent, matchFlags)) {
            result.addAll(undefinedList);
            result.addAll(filterCrossProfileCandidatesWithDomainPreferredActivities(computer, intent, matchFlags, categorizeResolveInfoByTargetUser, userId, 0, resolveForStart));
            includeBrowser = true;
        } else {
            android.util.Pair<java.util.List<android.content.pm.ResolveInfo>, java.lang.Integer> infosAndLevel = this.mDomainVerificationManager.filterToApprovedApp(intent, undefinedList, userId, pkgSettingFunction);
            java.util.List<android.content.pm.ResolveInfo> approvedInfos = (java.util.List) infosAndLevel.first;
            java.lang.Integer highestApproval = (java.lang.Integer) infosAndLevel.second;
            if (approvedInfos.isEmpty()) {
                result.addAll(filterCrossProfileCandidatesWithDomainPreferredActivities(computer, intent, matchFlags, categorizeResolveInfoByTargetUser, userId, 0, resolveForStart));
                includeBrowser = true;
            } else {
                result.addAll(approvedInfos);
                result.addAll(filterCrossProfileCandidatesWithDomainPreferredActivities(computer, intent, matchFlags, categorizeResolveInfoByTargetUser, userId, highestApproval.intValue(), resolveForStart));
                includeBrowser = false;
            }
        }
        if (includeBrowser) {
            if (com.android.server.pm.PackageManagerService.DEBUG_DOMAIN_VERIFICATION) {
                android.util.Slog.v("PackageManager", " ...including browsers in candidate set");
            }
            if ((matchFlags & 131072) != 0) {
                result.addAll(matchAllList);
            } else {
                java.lang.String defaultBrowserPackageName = this.mDefaultAppProvider.getDefaultBrowser(userId);
                int maxMatchPrio = 0;
                android.content.pm.ResolveInfo defaultBrowserMatch = null;
                int numCandidates = matchAllList.size();
                for (int n2 = 0; n2 < numCandidates; n2++) {
                    android.content.pm.ResolveInfo info2 = matchAllList.get(n2);
                    if (info2.priority > maxMatchPrio) {
                        maxMatchPrio = info2.priority;
                    }
                    if (info2.activityInfo.packageName.equals(defaultBrowserPackageName) && (defaultBrowserMatch == null || defaultBrowserMatch.priority < info2.priority)) {
                        if (debug) {
                            android.util.Slog.v("PackageManager", "Considering default browser match " + info2);
                        }
                        defaultBrowserMatch = info2;
                    }
                }
                if (defaultBrowserMatch != null && defaultBrowserMatch.priority >= maxMatchPrio && !android.text.TextUtils.isEmpty(defaultBrowserPackageName)) {
                    if (debug) {
                        android.util.Slog.v("PackageManager", "Default browser match " + defaultBrowserMatch);
                    }
                    result.add(defaultBrowserMatch);
                } else {
                    result.addAll(matchAllList);
                }
            }
            if (result.size() == 0) {
                result.addAll(candidates);
            }
        }
        try {
            final java.util.Set<java.lang.String> systemUserPackageSet = new java.util.HashSet<>();
            result.forEach(new java.util.function.Consumer() { // from class: com.android.server.pm.CrossProfileIntentResolverEngine$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.pm.CrossProfileIntentResolverEngine.lambda$filterCandidatesWithDomainPreferredActivitiesLPrBody$0(userId, systemUserPackageSet, (android.content.pm.ResolveInfo) obj);
                }
            });
            if (!systemUserPackageSet.isEmpty()) {
                java.util.Iterator<android.content.pm.ResolveInfo> infoIterator = result.iterator();
                while (infoIterator.hasNext()) {
                    android.content.pm.ResolveInfo candidateInfo = infoIterator.next();
                    if (candidateInfo.userHandle != null && candidateInfo.userHandle.getIdentifier() != userId && candidateInfo.userHandle.getIdentifier() == 999 && !systemUserPackageSet.contains(candidateInfo.getComponentInfo().packageName)) {
                        infoIterator.remove();
                    }
                }
            }
        } catch (java.lang.IllegalStateException e) {
        }
        return result;
    }

    static /* synthetic */ void lambda$filterCandidatesWithDomainPreferredActivitiesLPrBody$0(int userId, java.util.Set systemUserPackageSet, android.content.pm.ResolveInfo resolveInfo) {
        if (resolveInfo.userHandle != null && resolveInfo.userHandle.getIdentifier() == userId) {
            systemUserPackageSet.add(resolveInfo.getComponentInfo().packageName);
        }
    }

    private java.util.List<android.content.pm.ResolveInfo> filterCrossProfileCandidatesWithDomainPreferredActivities(com.android.server.pm.Computer computer, android.content.Intent intent, long flags, android.util.SparseArray<java.util.List<com.android.server.pm.CrossProfileDomainInfo>> categorizeResolveInfoByTargetUser, int sourceUserId, int highestApprovalLevel, boolean resolveForStart) {
        java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos = new java.util.ArrayList<>();
        for (int index = 0; index < categorizeResolveInfoByTargetUser.size(); index++) {
            if (categorizeResolveInfoByTargetUser.keyAt(index) == -2) {
                crossProfileDomainInfos.addAll(categorizeResolveInfoByTargetUser.valueAt(index));
            } else {
                com.android.server.pm.CrossProfileResolver crossProfileIntentResolver = chooseCrossProfileResolver(computer, sourceUserId, categorizeResolveInfoByTargetUser.keyAt(index), resolveForStart, flags);
                if (crossProfileIntentResolver != null) {
                    crossProfileDomainInfos.addAll(crossProfileIntentResolver.filterResolveInfoWithDomainPreferredActivity(intent, categorizeResolveInfoByTargetUser.valueAt(index), flags, sourceUserId, categorizeResolveInfoByTargetUser.keyAt(index), highestApprovalLevel));
                } else {
                    crossProfileDomainInfos.addAll(categorizeResolveInfoByTargetUser.valueAt(index));
                }
            }
        }
        return resolveInfoFromCrossProfileDomainInfo(crossProfileDomainInfos);
    }

    private java.util.List<android.content.pm.ResolveInfo> resolveInfoFromCrossProfileDomainInfo(java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos) {
        java.util.List<android.content.pm.ResolveInfo> resolveInfoList = new java.util.ArrayList<>();
        for (int infoIndex = 0; infoIndex < crossProfileDomainInfos.size(); infoIndex++) {
            resolveInfoList.add(crossProfileDomainInfos.get(infoIndex).mResolveInfo);
        }
        return resolveInfoList;
    }

    private boolean hasNonNegativePriority(java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos) {
        return crossProfileDomainInfos.size() > 0 && crossProfileDomainInfos.get(0).mResolveInfo != null && crossProfileDomainInfos.get(0).mResolveInfo.priority >= 0;
    }

    private boolean shouldUseNoFilteringResolver(int sourceUserId, int targetUserId) {
        return isNoFilteringPropertyConfiguredForUser(sourceUserId) || isNoFilteringPropertyConfiguredForUser(targetUserId);
    }

    private boolean isNoFilteringPropertyConfiguredForUser(int userId) {
        android.content.pm.UserProperties userProperties;
        return this.mUserManager.isProfile(userId) && (userProperties = this.mUserManagerInternal.getUserProperties(userId)) != null && userProperties.getCrossProfileIntentResolutionStrategy() == 1;
    }
}
