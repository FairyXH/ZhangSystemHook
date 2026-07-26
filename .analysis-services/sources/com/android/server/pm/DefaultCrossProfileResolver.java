package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class DefaultCrossProfileResolver extends com.android.server.pm.CrossProfileResolver {
    private final com.android.server.pm.verify.domain.DomainVerificationManagerInternal mDomainVerificationManager;

    public DefaultCrossProfileResolver(com.android.server.pm.resolution.ComponentResolverApi componentResolver, com.android.server.pm.UserManagerService userManager, com.android.server.pm.verify.domain.DomainVerificationManagerInternal domainVerificationManager) {
        super(componentResolver, userManager);
        this.mDomainVerificationManager = domainVerificationManager;
    }

    @Override // com.android.server.pm.CrossProfileResolver
    public java.util.List<com.android.server.pm.CrossProfileDomainInfo> resolveIntent(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, int userId, int targetUserId, long flags, java.lang.String pkgName, java.util.List<com.android.server.pm.CrossProfileIntentFilter> matchingFilters, boolean hasNonNegativePriorityResult, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        java.util.List<com.android.server.pm.CrossProfileDomainInfo> xpResult = new java.util.ArrayList<>();
        if (pkgName != null) {
            return xpResult;
        }
        com.android.server.pm.CrossProfileDomainInfo skipProfileInfo = querySkipCurrentProfileIntents(computer, matchingFilters, intent, resolvedType, flags, userId, pkgSettingFunction);
        if (skipProfileInfo != null) {
            xpResult.add(skipProfileInfo);
            return filterIfNotSystemUser(xpResult, userId);
        }
        com.android.server.pm.CrossProfileDomainInfo specificXpInfo = queryCrossProfileIntents(computer, matchingFilters, intent, resolvedType, flags, userId, hasNonNegativePriorityResult, pkgSettingFunction);
        if (intent.hasWebURI()) {
            com.android.server.pm.CrossProfileDomainInfo generalXpInfo = null;
            android.content.pm.UserInfo parent = getProfileParent(userId);
            if (parent != null) {
                generalXpInfo = computer.getCrossProfileDomainPreferredLpr(intent, resolvedType, flags, userId, parent.id);
            }
            com.android.server.pm.CrossProfileDomainInfo prioritizedXpInfo = generalXpInfo != null ? generalXpInfo : specificXpInfo;
            if (prioritizedXpInfo != null) {
                xpResult.add(prioritizedXpInfo);
            }
        } else if (specificXpInfo != null) {
            xpResult.add(specificXpInfo);
        }
        return xpResult;
    }

    @Override // com.android.server.pm.CrossProfileResolver
    public java.util.List<com.android.server.pm.CrossProfileDomainInfo> filterResolveInfoWithDomainPreferredActivity(android.content.Intent intent, java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos, long flags, int sourceUserId, int targetUserId, int highestApprovalLevel) {
        java.util.List<com.android.server.pm.CrossProfileDomainInfo> filteredCrossProfileDomainInfos = new java.util.ArrayList<>();
        if (crossProfileDomainInfos != null && !crossProfileDomainInfos.isEmpty()) {
            for (int index = 0; index < crossProfileDomainInfos.size(); index++) {
                com.android.server.pm.CrossProfileDomainInfo crossProfileDomainInfo = crossProfileDomainInfos.get(index);
                if (crossProfileDomainInfo.mHighestApprovalLevel > highestApprovalLevel) {
                    filteredCrossProfileDomainInfos.add(crossProfileDomainInfo);
                }
            }
        }
        return filteredCrossProfileDomainInfos;
    }

    private com.android.server.pm.CrossProfileDomainInfo querySkipCurrentProfileIntents(com.android.server.pm.Computer computer, java.util.List<com.android.server.pm.CrossProfileIntentFilter> matchingFilters, android.content.Intent intent, java.lang.String resolvedType, long flags, int sourceUserId, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        com.android.server.pm.CrossProfileDomainInfo info;
        if (matchingFilters != null) {
            int size = matchingFilters.size();
            for (int i = 0; i < size; i++) {
                com.android.server.pm.CrossProfileIntentFilter filter = matchingFilters.get(i);
                if ((filter.getFlags() & 2) != 0 && (info = createForwardingResolveInfo(computer, filter, intent, resolvedType, flags, sourceUserId, pkgSettingFunction)) != null) {
                    return info;
                }
            }
            return null;
        }
        return null;
    }

    private com.android.server.pm.CrossProfileDomainInfo queryCrossProfileIntents(com.android.server.pm.Computer computer, java.util.List<com.android.server.pm.CrossProfileIntentFilter> matchingFilters, android.content.Intent intent, java.lang.String resolvedType, long flags, int sourceUserId, boolean matchInCurrentProfile, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        java.util.List<com.android.server.pm.CrossProfileIntentFilter> list = matchingFilters;
        if (list == null) {
            return null;
        }
        android.util.SparseBooleanArray alreadyTriedUserIds = new android.util.SparseBooleanArray();
        com.android.server.pm.CrossProfileDomainInfo resultInfo = null;
        int size = matchingFilters.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            com.android.server.pm.CrossProfileIntentFilter filter = list.get(i);
            int targetUserId = filter.getTargetUserId();
            boolean skipCurrentProfile = (filter.getFlags() & 2) != 0;
            boolean skipCurrentProfileIfNoMatchFound = (filter.getFlags() & 4) != 0;
            if (!skipCurrentProfile && !alreadyTriedUserIds.get(targetUserId)) {
                if (!skipCurrentProfileIfNoMatchFound || !matchInCurrentProfile) {
                    com.android.server.pm.CrossProfileDomainInfo info = createForwardingResolveInfo(computer, filter, intent, resolvedType, flags, sourceUserId, pkgSettingFunction);
                    if (info != null) {
                        resultInfo = info;
                        break;
                    }
                    alreadyTriedUserIds.put(targetUserId, true);
                }
            }
            i++;
            list = matchingFilters;
        }
        if (resultInfo != null) {
            android.content.pm.ResolveInfo forwardingResolveInfo = resultInfo.mResolveInfo;
            if (isUserEnabled(forwardingResolveInfo.targetUserId)) {
                java.util.List<com.android.server.pm.CrossProfileDomainInfo> filteredResult = filterIfNotSystemUser(java.util.Collections.singletonList(resultInfo), sourceUserId);
                if (filteredResult.isEmpty()) {
                    return null;
                }
                return resultInfo;
            }
            return null;
        }
        return null;
    }

    protected com.android.server.pm.CrossProfileDomainInfo createForwardingResolveInfo(com.android.server.pm.Computer computer, com.android.server.pm.CrossProfileIntentFilter filter, android.content.Intent intent, java.lang.String resolvedType, long flags, int sourceUserId, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        android.content.pm.ResolveInfo forwardingInfo;
        int targetUserId = filter.getTargetUserId();
        if (!isUserEnabled(targetUserId)) {
            return null;
        }
        java.util.List<android.content.pm.ResolveInfo> resultTargetUser = this.mComponentResolver.queryActivities(computer, intent, resolvedType, flags, targetUserId);
        if (com.android.internal.util.CollectionUtils.isEmpty(resultTargetUser)) {
            return null;
        }
        int i = resultTargetUser.size() - 1;
        while (true) {
            if (i < 0) {
                forwardingInfo = null;
                break;
            }
            android.content.pm.ResolveInfo targetUserResolveInfo = resultTargetUser.get(i);
            if ((targetUserResolveInfo.activityInfo.applicationInfo.flags & 1073741824) == 0) {
                android.content.pm.ResolveInfo forwardingInfo2 = computer.createForwardingResolveInfoUnchecked(filter, sourceUserId, targetUserId);
                forwardingInfo = forwardingInfo2;
                break;
            }
            i--;
        }
        if (forwardingInfo == null) {
            return null;
        }
        int size = resultTargetUser.size();
        int highestApprovalLevel = 0;
        for (int i2 = 0; i2 < size; i2++) {
            android.content.pm.ResolveInfo riTargetUser = resultTargetUser.get(i2);
            if (!riTargetUser.handleAllWebDataURI) {
                java.lang.String packageName = riTargetUser.activityInfo.packageName;
                com.android.server.pm.pkg.PackageStateInternal ps = pkgSettingFunction.apply(packageName);
                if (ps != null) {
                    highestApprovalLevel = java.lang.Math.max(highestApprovalLevel, this.mDomainVerificationManager.approvalLevelForDomain(ps, intent, flags, targetUserId));
                }
            }
        }
        return new com.android.server.pm.CrossProfileDomainInfo(forwardingInfo, highestApprovalLevel, targetUserId);
    }
}
