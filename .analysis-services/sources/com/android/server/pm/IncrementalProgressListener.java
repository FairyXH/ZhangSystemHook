package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class IncrementalProgressListener extends android.content.pm.IPackageLoadingProgressCallback.Stub {
    private final java.lang.String mPackageName;
    private final com.android.server.pm.PackageManagerService mPm;

    IncrementalProgressListener(java.lang.String packageName, com.android.server.pm.PackageManagerService pm) {
        this.mPackageName = packageName;
        this.mPm = pm;
    }

    public void onPackageLoadingProgressChanged(final float progress) {
        com.android.server.pm.pkg.PackageStateInternal packageState = this.mPm.snapshotComputer().getPackageStateInternal(this.mPackageName);
        if (packageState == null) {
            return;
        }
        boolean wasLoading = packageState.isLoading();
        if (wasLoading) {
            this.mPm.commitPackageStateMutation(null, this.mPackageName, new java.util.function.Consumer() { // from class: com.android.server.pm.IncrementalProgressListener$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setLoadingProgress(progress);
                }
            });
            if (java.lang.Math.abs(1.0f - progress) < 1.0E-8f) {
                this.mPm.commitPackageStateMutation(null, this.mPackageName, new java.util.function.Consumer() { // from class: com.android.server.pm.IncrementalProgressListener$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setLoadingCompletedTime(java.lang.System.currentTimeMillis());
                    }
                });
                this.mPm.mIncrementalManager.unregisterLoadingProgressCallbacks(packageState.getPathString());
                this.mPm.scheduleWriteSettings();
            }
        }
    }
}
