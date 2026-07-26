package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageManagerServiceSocExtImpl implements com.android.server.pm.IPackageManagerServiceSocExt {
    private static boolean DEBUG_PMS = true;
    private static final java.lang.String PACKAGE_MIME_TYPE = "application/vnd.android.package-archive";
    private static final java.lang.String TAG = "PackageManager";
    private static final int VENDOR_DATA_UID = 2918;
    private java.lang.String mOptionalVerifierPackage;
    private com.android.server.pm.PackageManagerService mPms;

    public PackageManagerServiceSocExtImpl(java.lang.Object service) {
        this.mPms = (com.android.server.pm.PackageManagerService) service;
        android.util.Slog.d(TAG, "PackageManagerServiceSocExtImpl instance create!");
    }

    @Override // com.android.server.pm.IPackageManagerServiceSocExt
    public void createBoostFrameworkOnSystemReady() {
        new android.util.BoostFramework(this.mPms.mContext, true);
    }

    @Override // com.android.server.pm.IPackageManagerServiceSocExt
    public void acquireUxPerfLockPkgUninstall(java.lang.String packageName, int userId, boolean res) {
        if (res && packageName != null) {
            android.util.BoostFramework uxPerf = new android.util.BoostFramework();
            if (uxPerf.board_first_api_lvl < 33 && uxPerf.board_api_lvl < 33) {
                uxPerf.perfUXEngine_events(7, 0, packageName, userId);
            } else {
                uxPerf.perfEvent(4260, packageName, 2, new int[]{userId, 0});
            }
        }
    }

    @Override // com.android.server.pm.IPackageManagerServiceSocExt
    public void acquireUxPerfLockPkgInstall(java.lang.String packageName) {
        if (packageName != null) {
            android.util.BoostFramework uxPerf = new android.util.BoostFramework();
            if (uxPerf.board_first_api_lvl < 33 && uxPerf.board_api_lvl < 33) {
                uxPerf.perfUXEngine_events(8, 0, packageName, 0);
            } else {
                uxPerf.perfEvent(4259, packageName, 2, new int[]{0, 0});
            }
        }
    }

    @Override // com.android.server.pm.IPackageManagerServiceSocExt
    public void acquireUxPerfLockPkgUpdate(java.lang.String packageName) {
        if (packageName != null) {
            android.util.BoostFramework uxPerf = new android.util.BoostFramework();
            if (uxPerf.board_first_api_lvl < 33 && uxPerf.board_api_lvl < 33) {
                uxPerf.perfUXEngine_events(8, 0, packageName, 1);
            }
            uxPerf.perfEvent(4242, packageName, 2, new int[]{0, -1});
        }
    }

    @Override // com.android.server.pm.IPackageManagerServiceSocExt
    public void addVendorDataUid(com.android.server.pm.Settings settings) {
        if (settings != null) {
            settings.addSharedUserLPw("android.uid.vendordata", VENDOR_DATA_UID, 262144, 8);
        }
    }
}
