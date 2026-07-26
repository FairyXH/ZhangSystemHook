package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class PackageSuspender {
    private final java.util.List<java.lang.String> mExemptedPackages;
    private final android.content.pm.PackageManagerInternal mPackageManager;
    private final java.util.Set<java.lang.String> mSuspendedPackageAfter;
    private final java.util.Set<java.lang.String> mSuspendedPackageBefore;
    private final int mUserId;

    public PackageSuspender(java.util.Set<java.lang.String> suspendedPackageBefore, java.util.Set<java.lang.String> suspendedPackageAfter, java.util.List<java.lang.String> exemptedPackages, android.content.pm.PackageManagerInternal pmi, int userId) {
        this.mSuspendedPackageBefore = suspendedPackageBefore != null ? suspendedPackageBefore : java.util.Collections.emptySet();
        this.mSuspendedPackageAfter = suspendedPackageAfter != null ? suspendedPackageAfter : java.util.Collections.emptySet();
        this.mExemptedPackages = exemptedPackages;
        this.mPackageManager = pmi;
        this.mUserId = userId;
    }

    public java.lang.String[] suspend(java.util.Set<java.lang.String> packages) {
        java.util.Set<java.lang.String> result = suspendWithExemption(packages);
        return (java.lang.String[]) result.toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.PackageSuspender$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.PackageSuspender.lambda$suspend$0(i);
            }
        });
    }

    static /* synthetic */ java.lang.String[] lambda$suspend$0(int x$0) {
        return new java.lang.String[x$0];
    }

    private java.util.Set<java.lang.String> suspendWithExemption(java.util.Set<java.lang.String> packages) {
        java.util.Set<java.lang.String> packagesToSuspend = new android.util.ArraySet<>(packages);
        java.util.Set<java.lang.String> result = new android.util.ArraySet<>(this.mExemptedPackages);
        result.retainAll(packagesToSuspend);
        packagesToSuspend.removeAll(this.mExemptedPackages);
        java.lang.String[] failedPackages = this.mPackageManager.setPackagesSuspendedByAdmin(this.mUserId, (java.lang.String[]) packagesToSuspend.toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.PackageSuspender$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.PackageSuspender.lambda$suspendWithExemption$1(i);
            }
        }), true);
        if (failedPackages == null) {
            com.android.server.utils.Slogf.w("DevicePolicyManager", "PM failed to suspend packages (%s)", packages);
            return packages;
        }
        result.addAll(java.util.Arrays.asList(failedPackages));
        return result;
    }

    static /* synthetic */ java.lang.String[] lambda$suspendWithExemption$1(int x$0) {
        return new java.lang.String[x$0];
    }

    public java.lang.String[] unsuspend(java.util.Set<java.lang.String> packages) {
        java.util.Set<java.lang.String> packagesToUnsuspend = new android.util.ArraySet<>(this.mSuspendedPackageBefore);
        packagesToUnsuspend.removeAll(this.mSuspendedPackageAfter);
        java.util.Set<java.lang.String> result = new android.util.ArraySet<>(packages);
        result.retainAll(this.mSuspendedPackageAfter);
        result.removeAll(this.mExemptedPackages);
        result.addAll(unsuspendWithExemption(packagesToUnsuspend));
        return (java.lang.String[]) result.toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.PackageSuspender$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.PackageSuspender.lambda$unsuspend$2(i);
            }
        });
    }

    static /* synthetic */ java.lang.String[] lambda$unsuspend$2(int x$0) {
        return new java.lang.String[x$0];
    }

    private java.util.Set<java.lang.String> unsuspendWithExemption(java.util.Set<java.lang.String> packages) {
        java.lang.String[] failedPackages = this.mPackageManager.setPackagesSuspendedByAdmin(this.mUserId, (java.lang.String[]) packages.toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.PackageSuspender$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.PackageSuspender.lambda$unsuspendWithExemption$3(i);
            }
        }), false);
        if (failedPackages == null) {
            com.android.server.utils.Slogf.w("DevicePolicyManager", "PM failed to unsuspend packages (%s)", packages);
        }
        return new android.util.ArraySet(failedPackages);
    }

    static /* synthetic */ java.lang.String[] lambda$unsuspendWithExemption$3(int x$0) {
        return new java.lang.String[x$0];
    }
}
