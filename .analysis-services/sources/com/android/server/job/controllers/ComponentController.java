package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public class ComponentController extends com.android.server.job.controllers.StateController {
    private static final boolean DEBUG;
    private static final java.lang.String TAG = "JobScheduler.Component";
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final com.android.server.job.controllers.ComponentController.ComponentStateUpdateFunctor mComponentStateUpdateFunctor;
    private final android.util.SparseArrayMap<android.content.ComponentName, java.lang.String> mServiceProcessCache;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    public ComponentController(com.android.server.job.JobSchedulerService service) {
        super(service);
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.job.controllers.ComponentController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                byte b;
                java.lang.String action = intent.getAction();
                if (action == null) {
                    android.util.Slog.wtf(com.android.server.job.controllers.ComponentController.TAG, "Intent action was null");
                }
                switch (action.hashCode()) {
                    case -742246786:
                        b = !action.equals("android.intent.action.USER_STOPPED") ? (byte) -1 : (byte) 3;
                        break;
                    case 172491798:
                        b = !action.equals("android.intent.action.PACKAGE_CHANGED") ? (byte) -1 : (byte) 1;
                        break;
                    case 833559602:
                        b = !action.equals("android.intent.action.USER_UNLOCKED") ? (byte) -1 : (byte) 2;
                        break;
                    case 1544582882:
                        b = !action.equals("android.intent.action.PACKAGE_ADDED") ? (byte) -1 : (byte) 0;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                            android.net.Uri uri = intent.getData();
                            java.lang.String pkg = uri != null ? uri.getSchemeSpecificPart() : null;
                            if (pkg != null) {
                                int pkgUid = intent.getIntExtra("android.intent.extra.UID", -1);
                                int userId = android.os.UserHandle.getUserId(pkgUid);
                                com.android.server.job.controllers.ComponentController.this.updateComponentStateForPackage(userId, pkg);
                            }
                        }
                        break;
                    case 1:
                        android.net.Uri uri2 = intent.getData();
                        java.lang.String pkg2 = uri2 != null ? uri2.getSchemeSpecificPart() : null;
                        java.lang.String[] changedComponents = intent.getStringArrayExtra("android.intent.extra.changed_component_name_list");
                        if (pkg2 != null && changedComponents != null && changedComponents.length > 0) {
                            int pkgUid2 = intent.getIntExtra("android.intent.extra.UID", -1);
                            int userId2 = android.os.UserHandle.getUserId(pkgUid2);
                            com.android.server.job.controllers.ComponentController.this.updateComponentStateForPackage(userId2, pkg2);
                            break;
                        }
                        break;
                    case 2:
                    case 3:
                        int userId3 = intent.getIntExtra("android.intent.extra.user_handle", 0);
                        com.android.server.job.controllers.ComponentController.this.updateComponentStateForUser(userId3);
                        break;
                }
            }
        };
        this.mServiceProcessCache = new android.util.SparseArrayMap<>();
        this.mComponentStateUpdateFunctor = new com.android.server.job.controllers.ComponentController.ComponentStateUpdateFunctor();
    }

    @Override // com.android.server.job.controllers.StateController
    public void startTrackingLocked() {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_CHANGED");
        filter.addDataScheme("package");
        filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_ADDED@PACKAGE=REPLACING");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, filter, null, null);
        android.content.IntentFilter userFilter = new android.content.IntentFilter();
        userFilter.addAction("android.intent.action.USER_UNLOCKED");
        userFilter.addAction("android.intent.action.USER_STOPPED");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, userFilter, null, null);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus lastJob) {
        updateComponentEnabledStateLocked(jobStatus);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus incomingJob) {
    }

    @Override // com.android.server.job.controllers.StateController
    public void onAppRemovedLocked(java.lang.String packageName, int uid) {
        clearComponentsForPackageLocked(android.os.UserHandle.getUserId(uid), packageName);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUserRemovedLocked(int userId) {
        this.mServiceProcessCache.delete(userId);
    }

    private java.lang.String getServiceProcessLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        android.content.pm.ServiceInfo si;
        android.content.ComponentName service = jobStatus.getServiceComponent();
        int userId = jobStatus.getUserId();
        if (this.mServiceProcessCache.contains(userId, service)) {
            return (java.lang.String) this.mServiceProcessCache.get(userId, service);
        }
        try {
            si = this.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0).getPackageManager().getServiceInfo(service, 268435456);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            if (this.mService.areUsersStartedLocked(jobStatus)) {
                android.util.Slog.e(TAG, "Job exists for non-existent package: " + service.getPackageName());
            }
            si = null;
        }
        java.lang.String processName = si == null ? null : si.processName;
        this.mServiceProcessCache.add(userId, service, processName);
        return processName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateComponentEnabledStateLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        java.lang.String processName = getServiceProcessLocked(jobStatus);
        if (DEBUG && processName == null) {
            android.util.Slog.v(TAG, jobStatus.toShortString() + " component not present");
        }
        java.lang.String ogProcess = jobStatus.serviceProcessName;
        jobStatus.serviceProcessName = processName;
        return !java.util.Objects.equals(ogProcess, processName);
    }

    private void clearComponentsForPackageLocked(int userId, java.lang.String pkg) {
        int uIdx = this.mServiceProcessCache.indexOfKey(userId);
        for (int c = this.mServiceProcessCache.numElementsForKey(userId) - 1; c >= 0; c--) {
            android.content.ComponentName cn = (android.content.ComponentName) this.mServiceProcessCache.keyAt(uIdx, c);
            if (cn.getPackageName().equals(pkg)) {
                this.mServiceProcessCache.delete(userId, cn);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateComponentStateForPackage(final int userId, final java.lang.String pkg) {
        synchronized (this.mLock) {
            clearComponentsForPackageLocked(userId, pkg);
            updateComponentStatesLocked(new java.util.function.Predicate() { // from class: com.android.server.job.controllers.ComponentController$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.job.controllers.ComponentController.lambda$updateComponentStateForPackage$0(userId, pkg, (com.android.server.job.controllers.JobStatus) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$updateComponentStateForPackage$0(int userId, java.lang.String pkg, com.android.server.job.controllers.JobStatus jobStatus) {
        return jobStatus.getUserId() == userId && jobStatus.getServiceComponent().getPackageName().equals(pkg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateComponentStateForUser(final int userId) {
        synchronized (this.mLock) {
            this.mServiceProcessCache.delete(userId);
            updateComponentStatesLocked(new java.util.function.Predicate() { // from class: com.android.server.job.controllers.ComponentController$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.job.controllers.ComponentController.lambda$updateComponentStateForUser$1(userId, (com.android.server.job.controllers.JobStatus) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$updateComponentStateForUser$1(int userId, com.android.server.job.controllers.JobStatus jobStatus) {
        return jobStatus.getUserId() == userId;
    }

    private void updateComponentStatesLocked(java.util.function.Predicate<com.android.server.job.controllers.JobStatus> filter) {
        this.mComponentStateUpdateFunctor.reset();
        this.mService.getJobStore().forEachJob(filter, this.mComponentStateUpdateFunctor);
        if (this.mComponentStateUpdateFunctor.mChangedJobs.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(this.mComponentStateUpdateFunctor.mChangedJobs);
        }
    }

    final class ComponentStateUpdateFunctor implements java.util.function.Consumer<com.android.server.job.controllers.JobStatus> {
        final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mChangedJobs = new android.util.ArraySet<>();

        ComponentStateUpdateFunctor() {
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.job.controllers.JobStatus jobStatus) {
            if (com.android.server.job.controllers.ComponentController.this.updateComponentEnabledStateLocked(jobStatus)) {
                this.mChangedJobs.add(jobStatus);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reset() {
            this.mChangedJobs.clear();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        for (int u = 0; u < this.mServiceProcessCache.numMaps(); u++) {
            int userId = this.mServiceProcessCache.keyAt(u);
            for (int p = 0; p < this.mServiceProcessCache.numElementsForKey(userId); p++) {
                android.content.ComponentName componentName = (android.content.ComponentName) this.mServiceProcessCache.keyAt(u, p);
                pw.print(userId);
                pw.print("-");
                pw.print(componentName);
                pw.print(": ");
                pw.print((java.lang.String) this.mServiceProcessCache.valueAt(u, p));
                pw.println();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
    }
}
