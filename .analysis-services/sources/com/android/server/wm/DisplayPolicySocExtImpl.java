package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayPolicySocExtImpl implements com.android.server.wm.IDisplayPolicySocExt {
    private static final java.lang.String TAG = "DisplayPolicySocExtImpl";
    private boolean mIsPerfBoostFlingAcquired;
    com.android.server.wm.DisplayPolicy mService;
    private static boolean SCROLL_BOOST_SS_ENABLE = false;
    private static boolean SILKY_SCROLLS_ENABLE = false;
    private static boolean isLowRAM = false;
    android.util.BoostFramework mPerfBoostDrag = null;
    android.util.BoostFramework mPerfBoostFling = null;
    android.util.BoostFramework mPerfBoostPrefling = null;
    android.util.BoostFramework mPerf = new android.util.BoostFramework();

    public DisplayPolicySocExtImpl(java.lang.Object service) {
        this.mService = (com.android.server.wm.DisplayPolicy) service;
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void hookOnVerticalFling(int duration) {
        java.lang.String currentPackage = getAppPackageName();
        if (currentPackage == null) {
            android.util.Slog.e(TAG, "Error: package name null");
            return;
        }
        if (SCROLL_BOOST_SS_ENABLE) {
            if (this.mPerfBoostFling == null) {
                this.mPerfBoostFling = new android.util.BoostFramework();
                this.mIsPerfBoostFlingAcquired = false;
            }
            if (this.mPerfBoostFling == null) {
                android.util.Slog.e(TAG, "Error: boost object null");
                return;
            }
            boolean isGame = isTopAppGame(currentPackage, this.mPerfBoostFling);
            if (!isGame) {
                this.mPerfBoostFling.perfHint(4224, currentPackage, duration + 160, 1);
                this.mIsPerfBoostFlingAcquired = true;
            }
        }
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void hookOnHorizontalFling(int duration) {
        java.lang.String currentPackage = getAppPackageName();
        if (currentPackage == null) {
            android.util.Slog.e(TAG, "Error: package name null");
            return;
        }
        if (SCROLL_BOOST_SS_ENABLE) {
            if (this.mPerfBoostFling == null) {
                this.mPerfBoostFling = new android.util.BoostFramework();
                this.mIsPerfBoostFlingAcquired = false;
            }
            if (this.mPerfBoostFling == null) {
                android.util.Slog.e(TAG, "Error: boost object null");
                return;
            }
            boolean isGame = isTopAppGame(currentPackage, this.mPerfBoostFling);
            if (!isGame) {
                this.mPerfBoostFling.perfHint(4224, currentPackage, duration + 160, 2);
                this.mIsPerfBoostFlingAcquired = true;
            }
        }
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void hookOnScroll(boolean started) {
        java.lang.String currentPackage = getAppPackageName();
        if (currentPackage == null) {
            android.util.Slog.e(TAG, "Error: package name null");
            return;
        }
        if (this.mPerfBoostDrag == null) {
            this.mPerfBoostDrag = new android.util.BoostFramework();
        }
        if (this.mPerfBoostDrag == null) {
            android.util.Slog.e(TAG, "Error: boost object null");
            return;
        }
        if (SCROLL_BOOST_SS_ENABLE && started) {
            if (this.mPerfBoostPrefling == null) {
                this.mPerfBoostPrefling = new android.util.BoostFramework();
            }
            if (this.mPerfBoostPrefling == null) {
                android.util.Slog.e(TAG, "Error: boost object null");
                return;
            } else {
                boolean isGame = isTopAppGame(currentPackage, this.mPerfBoostPrefling);
                if (!isGame) {
                    this.mPerfBoostPrefling.perfHint(4224, currentPackage, -1, 4);
                }
            }
        }
        boolean isGame2 = isTopAppGame(currentPackage, this.mPerfBoostDrag);
        if (!isGame2 && started) {
            if (SILKY_SCROLLS_ENABLE) {
                this.mPerfBoostDrag.perfEvent(4177, currentPackage);
            }
            this.mPerfBoostDrag.perfHint(4231, currentPackage, -1, 1);
        } else {
            if (SILKY_SCROLLS_ENABLE) {
                this.mPerfBoostDrag.perfEvent(4178, currentPackage);
            }
            this.mPerfBoostDrag.perfLockRelease();
        }
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void hookOnDown() {
        if (SCROLL_BOOST_SS_ENABLE && this.mPerfBoostFling != null && this.mIsPerfBoostFlingAcquired) {
            this.mPerfBoostFling.perfLockRelease();
            this.mIsPerfBoostFlingAcquired = false;
        }
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void loadConfig() {
        if (this.mPerf != null) {
            SCROLL_BOOST_SS_ENABLE = java.lang.Boolean.parseBoolean(this.mPerf.perfGetProp("vendor.perf.gestureflingboost.enable", "false"));
            SILKY_SCROLLS_ENABLE = java.lang.Boolean.parseBoolean(this.mPerf.perfGetProp("ro.vendor.perf.ss", "false"));
        }
        isLowRAM = android.os.SystemProperties.getBoolean("ro.config.low_ram", false);
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public java.lang.String getAppPackageName() {
        try {
            android.app.ActivityManager.RunningTaskInfo rti = (android.app.ActivityManager.RunningTaskInfo) android.app.ActivityTaskManager.getService().getTasks(1, false, false, 0).get(0);
            java.lang.String currentPackage = rti.topActivity.getPackageName();
            return currentPackage;
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public boolean isTopAppGame(java.lang.String currentPackage, android.util.BoostFramework BoostType) {
        if (isLowRAM) {
            try {
                android.content.pm.ApplicationInfo ai = this.mService.getContext().getPackageManager().getApplicationInfo(currentPackage, 0);
                if (ai == null) {
                    return false;
                }
                if (ai.category != 0) {
                    if ((ai.flags & 33554432) != 33554432) {
                        z = false;
                    }
                }
                boolean isGame = z;
                return isGame;
            } catch (java.lang.Exception e) {
                return false;
            }
        }
        boolean isGame2 = BoostType.perfGetFeedback(5633, currentPackage) == 2;
        return isGame2;
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public boolean isSupportPerfBoost() {
        return true;
    }
}
