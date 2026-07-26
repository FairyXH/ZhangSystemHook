package com.android.server.permission.access.appop;

/* JADX INFO: compiled from: AppIdAppOpUpgrade.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\"\u0010\u0005\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/android/server/permission/access/appop/AppIdAppOpUpgrade;", "", "policy", "Lcom/android/server/permission/access/appop/AppIdAppOpPolicy;", "(Lcom/android/server/permission/access/appop/AppIdAppOpPolicy;)V", "upgradePackageState", "", "Lcom/android/server/permission/access/MutateStateScope;", "packageState", "Lcom/android/server/pm/pkg/PackageState;", "userId", "", "version", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppIdAppOpUpgrade {
    private final com.android.server.permission.access.appop.AppIdAppOpPolicy policy;

    public AppIdAppOpUpgrade(com.android.server.permission.access.appop.AppIdAppOpPolicy policy) {
        this.policy = policy;
    }

    public final void upgradePackageState(com.android.server.permission.access.MutateStateScope $this$upgradePackageState, com.android.server.pm.pkg.PackageState packageState, int userId, int version) {
        if (version <= 2) {
            com.android.server.permission.access.appop.AppIdAppOpPolicy $this$upgradePackageState_u24lambda_u240 = this.policy;
            int appOpMode = $this$upgradePackageState_u24lambda_u240.getAppOpMode($this$upgradePackageState, packageState.getAppId(), userId, "android:run_in_background");
            $this$upgradePackageState_u24lambda_u240.setAppOpMode($this$upgradePackageState, packageState.getAppId(), userId, "android:run_any_in_background", appOpMode);
        }
        if (version <= 13) {
            java.lang.String permissionName = android.app.AppOpsManager.opToPermission(107);
            com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
            if (androidPackage.getRequestedPermissions().contains(permissionName)) {
                com.android.server.permission.access.appop.AppIdAppOpPolicy $this$upgradePackageState_u24lambda_u241 = this.policy;
                int appOpMode2 = $this$upgradePackageState_u24lambda_u241.getAppOpMode($this$upgradePackageState, packageState.getAppId(), userId, "android:schedule_exact_alarm");
                int defaultAppOpMode = android.app.AppOpsManager.opToDefaultMode(107);
                if (appOpMode2 == defaultAppOpMode) {
                    $this$upgradePackageState_u24lambda_u241.setAppOpMode($this$upgradePackageState, packageState.getAppId(), userId, "android:schedule_exact_alarm", 0);
                }
            }
        }
    }
}
