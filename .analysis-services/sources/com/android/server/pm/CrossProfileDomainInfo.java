package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class CrossProfileDomainInfo {
    int mHighestApprovalLevel;
    final android.content.pm.ResolveInfo mResolveInfo;
    final int mTargetUserId;

    CrossProfileDomainInfo(android.content.pm.ResolveInfo resolveInfo, int highestApprovalLevel, int targetUserId) {
        this.mResolveInfo = resolveInfo;
        this.mHighestApprovalLevel = highestApprovalLevel;
        this.mTargetUserId = targetUserId;
    }

    CrossProfileDomainInfo(android.content.pm.ResolveInfo resolveInfo, int highestApprovalLevel) {
        this.mResolveInfo = resolveInfo;
        this.mHighestApprovalLevel = highestApprovalLevel;
        this.mTargetUserId = -2;
    }

    public java.lang.String toString() {
        return "CrossProfileDomainInfo{resolveInfo=" + this.mResolveInfo + ", highestApprovalLevel=" + this.mHighestApprovalLevel + ", targetUserId= " + this.mTargetUserId + '}';
    }
}
