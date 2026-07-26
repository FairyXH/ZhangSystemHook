package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface ICrossProfileIntentResolverEngineExt {
    default void filterDuplicateCandidatesByMultiAppFlag(java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileCandidates, java.util.List<android.content.pm.ResolveInfo> candidates, android.content.Intent intent) {
    }

    default void checkIfSkipCrossProfile(int sourceUserId, int targetUserId, java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileInfos) {
    }
}
