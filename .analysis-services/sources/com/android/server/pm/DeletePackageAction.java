package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class DeletePackageAction {
    public final com.android.server.pm.PackageSetting mDeletingPs;
    public final com.android.server.pm.PackageSetting mDisabledPs;
    public final int mFlags;
    public final com.android.server.pm.PackageRemovedInfo mRemovedInfo;
    public final android.os.UserHandle mUser;

    DeletePackageAction(com.android.server.pm.PackageSetting deletingPs, com.android.server.pm.PackageSetting disabledPs, com.android.server.pm.PackageRemovedInfo removedInfo, int flags, android.os.UserHandle user) {
        this.mDeletingPs = deletingPs;
        this.mDisabledPs = disabledPs;
        this.mRemovedInfo = removedInfo;
        this.mFlags = flags;
        this.mUser = user;
    }
}
