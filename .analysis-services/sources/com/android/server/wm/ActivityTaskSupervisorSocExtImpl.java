package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityTaskSupervisorSocExtImpl implements com.android.server.wm.IActivityTaskSupervisorSocExt {
    static final boolean DEBUG_SERVICETRACKER = false;
    private static final java.lang.String TAG = "ActivityTaskSupervisorSocExtImpl";
    private vendor.qti.hardware.servicetracker.V1_2.IServicetracker mServicetracker;
    com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    public android.util.BoostFramework mPerfBoost = new android.util.BoostFramework();
    public android.util.BoostFramework mUxPerf = new android.util.BoostFramework();

    public ActivityTaskSupervisorSocExtImpl(java.lang.Object service) {
        this.mSupervisor = (com.android.server.wm.ActivityTaskSupervisor) service;
    }

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void acquireAppLaunchPerfLock(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityTaskManagerService service) {
        int i;
        com.android.server.wm.WindowProcessController wpc;
        if (this.mPerfBoost != null) {
            int pkgType = this.mPerfBoost.perfGetFeedback(5633, r.packageName);
            int wpcPid = -1;
            if (service != null && r != null && r.info != null && r.info.applicationInfo != null && (wpc = service.getProcessController(r.processName, r.info.applicationInfo.uid)) != null && wpc.hasThread()) {
                wpcPid = wpc.getPid();
            }
            if (this.mPerfBoost.getPerfHalVersion() >= 2.299999952316284d) {
                this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, -1, 1, 2, new int[]{pkgType, wpcPid});
                com.android.server.wm.ActivityTaskSupervisor.mPerfSendTapHint = true;
                this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, -1, 2, 2, new int[]{pkgType, wpcPid});
                if (wpcPid != -1) {
                    this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, wpcPid, 103, 2, new int[]{pkgType, wpcPid});
                }
                if (pkgType == 2) {
                    com.android.server.wm.ActivityTaskSupervisor.mPerfHandle = this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, -1, 4, 2, new int[]{pkgType, wpcPid});
                    i = -1;
                } else {
                    com.android.server.wm.ActivityTaskSupervisor.mPerfHandle = this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, -1, 3, 2, new int[]{pkgType, wpcPid});
                    i = -1;
                }
            } else {
                i = -1;
                this.mPerfBoost.perfHint(4225, r.packageName, -1, 1);
                com.android.server.wm.ActivityTaskSupervisor.mPerfSendTapHint = true;
                this.mPerfBoost.perfHint(4225, r.packageName, -1, 2);
                if (wpcPid != -1) {
                    this.mPerfBoost.perfHint(4225, r.packageName, wpcPid, 103);
                }
                if (pkgType == 2) {
                    com.android.server.wm.ActivityTaskSupervisor.mPerfHandle = this.mPerfBoost.perfHint(4225, r.packageName, -1, 4);
                } else {
                    com.android.server.wm.ActivityTaskSupervisor.mPerfHandle = this.mPerfBoost.perfHint(4225, r.packageName, -1, 3);
                }
            }
            if (com.android.server.wm.ActivityTaskSupervisor.mPerfHandle > 0) {
                com.android.server.wm.ActivityTaskSupervisor.mIsPerfBoostAcquired = true;
            }
            if (r.info.applicationInfo != null && r.info.applicationInfo.sourceDir != null && this.mPerfBoost.board_first_api_lvl < 33 && this.mPerfBoost.board_api_lvl < 33 && !service.getWrapper().isIOPreloadPkg(r.packageName, r.mUserId)) {
                this.mPerfBoost.perfIOPrefetchStart(i, r.packageName, r.info.applicationInfo.sourceDir.substring(0, r.info.applicationInfo.sourceDir.lastIndexOf(47)));
            }
        }
    }

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void startSpecificActivityPerfHint(java.lang.String tag, com.android.server.wm.ActivityRecord r, int pid) {
        if (this.mPerfBoost != null) {
            android.util.Slog.i(tag, "The Process " + r.processName + " Already Exists in BG. So sending its PID: " + pid);
            this.mPerfBoost.perfHint(4225, r.processName, pid, 102);
        }
    }

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void reportActivityLaunchedPerfHint(com.android.server.wm.ActivityRecord r) {
        if (this.mPerfBoost != null && r.app != null) {
            this.mPerfBoost.perfHint(4162, r.packageName, r.app.getPid(), 1);
        }
    }

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void startPreferredApps(java.lang.String tag, com.android.server.wm.ActivityTaskManagerService service) {
        try {
            new com.android.server.wm.ActivityTaskSupervisorSocExtImpl.PreferredAppsTask(service).execute(new java.lang.Void[0]);
        } catch (java.lang.Exception e) {
            android.util.Slog.v(tag, "Exception while calling PreferredAppsTask: " + e);
        }
    }

    class PreferredAppsTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Void> {
        com.android.server.wm.ActivityTaskManagerService mService;

        public PreferredAppsTask(com.android.server.wm.ActivityTaskManagerService service) {
            this.mService = service;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public java.lang.Void doInBackground(java.lang.Void... params) {
            java.lang.String res;
            new android.content.Intent("android.intent.action.MAIN");
            try {
                int trimLevel = android.app.ActivityManager.getService().getMemoryTrimLevel();
                if (com.android.server.wm.ActivityTaskSupervisorSocExtImpl.this.mUxPerf != null && trimLevel < 3) {
                    if (com.android.server.wm.ActivityTaskSupervisorSocExtImpl.this.mUxPerf.board_first_api_lvl < 33 && com.android.server.wm.ActivityTaskSupervisorSocExtImpl.this.mUxPerf.board_api_lvl < 33) {
                        res = com.android.server.wm.ActivityTaskSupervisorSocExtImpl.this.mUxPerf.perfUXEngine_trigger(1);
                    } else {
                        res = com.android.server.wm.ActivityTaskSupervisorSocExtImpl.this.mUxPerf.perfSyncRequest(5636);
                    }
                    if (res == null) {
                        return null;
                    }
                    java.lang.String[] p_apps = res.trim().split(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                    if (p_apps.length != 0) {
                        java.util.ArrayList<java.lang.String> apps_l = new java.util.ArrayList<>(java.util.Arrays.asList(p_apps));
                        android.os.Bundle bParams = new android.os.Bundle();
                        bParams.putStringArrayList("start_empty_apps", apps_l);
                        android.os.Message msg = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.ActivityTaskSupervisorSocExtImpl$PreferredAppsTask$$ExternalSyntheticLambda0
                            @Override // java.util.function.BiConsumer
                            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                                ((android.app.ActivityManagerInternal) obj).startActivityAsUserEmpty((android.os.Bundle) obj2);
                            }
                        }, this.mService.mAmInternal, bParams);
                        this.mService.mH.sendMessage(msg);
                    }
                }
                return null;
            } catch (android.os.RemoteException e) {
                return null;
            }
        }
    }

    public vendor.qti.hardware.servicetracker.V1_2.IServicetracker getServicetrackerInstance() {
        if (this.mServicetracker == null) {
            try {
                this.mServicetracker = vendor.qti.hardware.servicetracker.V1_2.IServicetracker.getService(false);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to get servicetracker interface", e);
                return null;
            } catch (java.util.NoSuchElementException e2) {
            }
            if (this.mServicetracker == null) {
                android.util.Slog.w(TAG, "servicetracker HIDL not available");
                return null;
            }
        }
        return this.mServicetracker;
    }

    public void destroyServicetrackerInstance() {
        this.mServicetracker = null;
    }

    @Override // com.android.server.wm.IActivityTaskSupervisorSocExt
    public void notifyServiceTracker(com.android.server.wm.ActivityRecord.State state, boolean early_notify, com.android.server.wm.ActivityRecord r, long createTime) {
        vendor.qti.hardware.servicetracker.V1_2.ActivityDetails aDetails = new vendor.qti.hardware.servicetracker.V1_2.ActivityDetails();
        vendor.qti.hardware.servicetracker.V1_2.ActivityStats aStats = new vendor.qti.hardware.servicetracker.V1_2.ActivityStats();
        int aState = 11;
        aDetails.launchedFromPid = r.launchedFromPid;
        aDetails.launchedFromUid = r.launchedFromUid;
        aDetails.packageName = r.packageName;
        aDetails.processName = r.processName != null ? r.processName : "none";
        aDetails.intent = r.intent.getComponent().toString();
        aDetails.className = r.intent.getComponent().getClassName();
        aDetails.versioncode = r.info.applicationInfo.versionCode;
        aStats.createTime = createTime;
        aStats.lastVisibleTime = r.lastVisibleTime;
        aStats.launchCount = r.launchCount;
        aStats.lastLaunchTime = r.lastLaunchTime;
        switch (state) {
            case INITIALIZING:
                aState = 0;
                break;
            case STARTED:
                aState = 1;
                break;
            case RESUMED:
                aState = 2;
                break;
            case PAUSING:
                aState = 3;
                break;
            case PAUSED:
                aState = 4;
                break;
            case STOPPING:
                aState = 5;
                break;
            case STOPPED:
                aState = 6;
                break;
            case FINISHING:
                aState = 7;
                break;
            case DESTROYING:
                aState = 8;
                break;
            case DESTROYED:
                aState = 9;
                break;
            case RESTARTING_PROCESS:
                aState = 10;
                break;
        }
        try {
            vendor.qti.hardware.servicetracker.V1_2.IServicetracker mServicetracker = getServicetrackerInstance();
            if (mServicetracker == null) {
                android.util.Slog.e(TAG, "Unable to get servicetracker HAL instance");
            } else {
                mServicetracker.OnActivityStateChange(aState, aDetails, aStats, early_notify);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to send activity state change details to servicetracker HAL", e);
            destroyServicetrackerInstance();
        }
    }
}
