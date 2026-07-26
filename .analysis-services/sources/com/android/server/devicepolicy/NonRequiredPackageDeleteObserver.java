package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class NonRequiredPackageDeleteObserver extends android.content.pm.IPackageDeleteObserver.Stub {
    private static final int PACKAGE_DELETE_TIMEOUT_SEC = 40;
    private boolean mFailed = false;
    private final java.util.concurrent.CountDownLatch mLatch;

    NonRequiredPackageDeleteObserver(int packageCount) {
        this.mLatch = new java.util.concurrent.CountDownLatch(packageCount);
    }

    public void packageDeleted(java.lang.String packageName, int returnCode) {
        if (returnCode != 1) {
            android.util.Slog.e("DevicePolicyManager", "Failed to delete package: " + packageName);
            this.mFailed = true;
        }
        this.mLatch.countDown();
    }

    public boolean awaitPackagesDeletion() {
        try {
            if (!this.mLatch.await(40L, java.util.concurrent.TimeUnit.SECONDS)) {
                android.util.Slog.i("DevicePolicyManager", "Waiting time elapsed before all package deletion finished");
                return false;
            }
            if (!this.mFailed) {
                android.util.Slog.i("DevicePolicyManager", "All non-required system apps with launcher icon, and all disallowed apps have been uninstalled.");
            }
            return !this.mFailed;
        } catch (java.lang.InterruptedException e) {
            android.util.Log.w("DevicePolicyManager", "Interrupted while waiting for package deletion", e);
            java.lang.Thread.currentThread().interrupt();
            return false;
        }
    }
}
