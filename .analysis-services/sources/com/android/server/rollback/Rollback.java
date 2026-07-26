package com.android.server.rollback;

/* JADX INFO: loaded from: classes3.dex */
class Rollback {
    static final int ROLLBACK_STATE_AVAILABLE = 1;
    static final int ROLLBACK_STATE_COMMITTED = 3;
    static final int ROLLBACK_STATE_DELETED = 4;
    static final int ROLLBACK_STATE_ENABLING = 0;
    private static final java.lang.String TAG = "RollbackManager";
    public final android.content.rollback.RollbackInfo info;
    private final java.io.File mBackupDir;
    private final android.util.SparseIntArray mExtensionVersions;
    private final android.os.Handler mHandler;
    private final java.lang.String mInstallerPackageName;
    private final int mOriginalSessionId;
    private final int[] mPackageSessionIds;
    private boolean mRestoreUserDataInProgress;
    private long mRollbackLifetimeMillis;
    private int mState;
    private java.lang.String mStateDescription;
    private java.time.Instant mTimestamp;
    private final int mUserId;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface RollbackState {
    }

    Rollback(int rollbackId, java.io.File backupDir, int originalSessionId, boolean isStaged, int userId, java.lang.String installerPackageName, int[] packageSessionIds, android.util.SparseIntArray extensionVersions) {
        this.mStateDescription = "";
        this.mRestoreUserDataInProgress = false;
        this.mRollbackLifetimeMillis = 0L;
        this.info = new android.content.rollback.RollbackInfo(rollbackId, new java.util.ArrayList(), isStaged, new java.util.ArrayList(), -1, 0);
        this.mUserId = userId;
        this.mInstallerPackageName = installerPackageName;
        this.mBackupDir = backupDir;
        this.mOriginalSessionId = originalSessionId;
        this.mState = 0;
        this.mTimestamp = java.time.Instant.now();
        this.mPackageSessionIds = packageSessionIds != null ? packageSessionIds : new int[0];
        this.mExtensionVersions = (android.util.SparseIntArray) java.util.Objects.requireNonNull(extensionVersions);
        this.mHandler = android.os.Looper.myLooper() != null ? new android.os.Handler(android.os.Looper.myLooper()) : null;
    }

    Rollback(android.content.rollback.RollbackInfo info, java.io.File backupDir, java.time.Instant timestamp, int originalSessionId, int state, java.lang.String stateDescription, boolean restoreUserDataInProgress, int userId, java.lang.String installerPackageName, android.util.SparseIntArray extensionVersions) {
        this.mStateDescription = "";
        this.mRestoreUserDataInProgress = false;
        this.mRollbackLifetimeMillis = 0L;
        this.info = info;
        this.mUserId = userId;
        this.mInstallerPackageName = installerPackageName;
        this.mBackupDir = backupDir;
        this.mTimestamp = timestamp;
        this.mOriginalSessionId = originalSessionId;
        this.mState = state;
        this.mStateDescription = stateDescription;
        this.mRestoreUserDataInProgress = restoreUserDataInProgress;
        this.mExtensionVersions = (android.util.SparseIntArray) java.util.Objects.requireNonNull(extensionVersions);
        this.mPackageSessionIds = new int[0];
        this.mHandler = android.os.Looper.myLooper() != null ? new android.os.Handler(android.os.Looper.myLooper()) : null;
    }

    private void assertInWorkerThread() {
        com.android.internal.util.Preconditions.checkState(this.mHandler == null || this.mHandler.getLooper().isCurrentThread());
    }

    boolean isStaged() {
        return this.info.isStaged();
    }

    java.io.File getBackupDir() {
        return this.mBackupDir;
    }

    java.time.Instant getTimestamp() {
        assertInWorkerThread();
        return this.mTimestamp;
    }

    void setTimestamp(java.time.Instant timestamp) {
        assertInWorkerThread();
        this.mTimestamp = timestamp;
        com.android.server.rollback.RollbackStore.saveRollback(this);
    }

    void setRollbackLifetimeMillis(long lifetimeMillis) {
        assertInWorkerThread();
        this.mRollbackLifetimeMillis = lifetimeMillis;
    }

    long getRollbackLifetimeMillis() {
        assertInWorkerThread();
        return this.mRollbackLifetimeMillis;
    }

    int getOriginalSessionId() {
        return this.mOriginalSessionId;
    }

    int getUserId() {
        return this.mUserId;
    }

    java.lang.String getInstallerPackageName() {
        return this.mInstallerPackageName;
    }

    android.util.SparseIntArray getExtensionVersions() {
        return this.mExtensionVersions;
    }

    boolean isEnabling() {
        assertInWorkerThread();
        return this.mState == 0;
    }

    boolean isAvailable() {
        assertInWorkerThread();
        return this.mState == 1;
    }

    boolean isCommitted() {
        assertInWorkerThread();
        return this.mState == 3;
    }

    boolean isDeleted() {
        assertInWorkerThread();
        return this.mState == 4;
    }

    void saveRollback() {
        assertInWorkerThread();
        com.android.server.rollback.RollbackStore.saveRollback(this);
    }

    boolean enableForPackage(java.lang.String packageName, long newVersion, long installedVersion, boolean isApex, java.lang.String sourceDir, java.lang.String[] splitSourceDirs, int rollbackDataPolicy, int rollbackImpactLevel) {
        assertInWorkerThread();
        try {
            com.android.server.rollback.RollbackStore.backupPackageCodePath(this, packageName, sourceDir);
            if (!com.android.internal.util.ArrayUtils.isEmpty(splitSourceDirs)) {
                for (java.lang.String dir : splitSourceDirs) {
                    com.android.server.rollback.RollbackStore.backupPackageCodePath(this, packageName, dir);
                }
            }
            android.content.rollback.PackageRollbackInfo packageRollbackInfo = new android.content.rollback.PackageRollbackInfo(new android.content.pm.VersionedPackage(packageName, newVersion), new android.content.pm.VersionedPackage(packageName, installedVersion), new java.util.ArrayList(), new java.util.ArrayList(), isApex, false, new java.util.ArrayList(), rollbackDataPolicy);
            this.info.getPackages().add(packageRollbackInfo);
            if (this.info.getRollbackImpactLevel() < rollbackImpactLevel) {
                this.info.setRollbackImpactLevel(rollbackImpactLevel);
                return true;
            }
            return true;
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Unable to copy package for rollback for " + packageName, e);
            return false;
        }
    }

    boolean enableForPackageInApex(java.lang.String packageName, long installedVersion, int rollbackDataPolicy) {
        assertInWorkerThread();
        android.content.rollback.PackageRollbackInfo packageRollbackInfo = new android.content.rollback.PackageRollbackInfo(new android.content.pm.VersionedPackage(packageName, 0), new android.content.pm.VersionedPackage(packageName, installedVersion), new java.util.ArrayList(), new java.util.ArrayList(), false, true, new java.util.ArrayList(), rollbackDataPolicy);
        this.info.getPackages().add(packageRollbackInfo);
        return true;
    }

    private static void addAll(java.util.List<java.lang.Integer> list, int[] arr) {
        for (int i : arr) {
            list.add(java.lang.Integer.valueOf(i));
        }
    }

    void snapshotUserData(java.lang.String packageName, int[] userIds, com.android.server.rollback.AppDataRollbackHelper dataHelper) {
        assertInWorkerThread();
        if (!isEnabling()) {
            return;
        }
        for (android.content.rollback.PackageRollbackInfo pkgRollbackInfo : this.info.getPackages()) {
            if (pkgRollbackInfo.getPackageName().equals(packageName)) {
                if (pkgRollbackInfo.getRollbackDataPolicy() == 0) {
                    dataHelper.snapshotAppData(this.info.getRollbackId(), pkgRollbackInfo, userIds);
                    addAll(pkgRollbackInfo.getSnapshottedUsers(), userIds);
                    com.android.server.rollback.RollbackStore.saveRollback(this);
                    return;
                }
                return;
            }
        }
    }

    void commitPendingBackupAndRestoreForUser(int userId, com.android.server.rollback.AppDataRollbackHelper dataHelper) {
        assertInWorkerThread();
        if (dataHelper.commitPendingBackupAndRestoreForUser(userId, this)) {
            com.android.server.rollback.RollbackStore.saveRollback(this);
        }
    }

    void makeAvailable() {
        assertInWorkerThread();
        if (isDeleted()) {
            android.util.Slog.w(TAG, "Cannot make deleted rollback available.");
            return;
        }
        setState(1, "");
        this.mTimestamp = java.time.Instant.now();
        com.android.server.rollback.RollbackStore.saveRollback(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void commit(final android.content.Context context, final java.util.List<android.content.pm.VersionedPackage> list, java.lang.String str, final android.content.IntentSender intentSender) {
        int iCreateSession;
        android.content.pm.PackageInstaller.Session sessionOpenSession;
        android.content.pm.PackageInstaller.SessionParams sessionParams;
        java.lang.Throwable th;
        int i;
        android.content.pm.PackageInstaller.Session session;
        java.lang.Object[] objArr;
        assertInWorkerThread();
        if (!isAvailable()) {
            com.android.server.rollback.RollbackManagerServiceImpl.sendFailure(context, intentSender, 2, "Rollback unavailable");
            return;
        }
        boolean z = true;
        if (containsApex() && wasCreatedAtLowerExtensionVersion()) {
            if (extensionVersionReductionWouldViolateConstraint(this.mExtensionVersions, (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class))) {
                com.android.server.rollback.RollbackManagerServiceImpl.sendFailure(context, intentSender, 1, "Rollback may violate a minExtensionVersion constraint");
                return;
            }
        }
        try {
            android.content.Context contextCreatePackageContextAsUser = context.createPackageContextAsUser(str, 0, android.os.UserHandle.of(this.mUserId));
            android.content.pm.PackageManager packageManager = contextCreatePackageContextAsUser.getPackageManager();
            try {
                android.content.pm.PackageInstaller packageInstaller = packageManager.getPackageInstaller();
                android.content.pm.PackageInstaller.SessionParams sessionParams2 = new android.content.pm.PackageInstaller.SessionParams(1);
                sessionParams2.setRequestDowngrade(true);
                sessionParams2.setMultiPackage();
                if (isStaged()) {
                    try {
                        sessionParams2.setStaged();
                    } catch (java.io.IOException e) {
                        e = e;
                        android.util.Slog.e(TAG, "Rollback failed", e);
                        com.android.server.rollback.RollbackManagerServiceImpl.sendFailure(context, intentSender, 1, "IOException: " + e.toString());
                    }
                }
                sessionParams2.setInstallReason(5);
                iCreateSession = packageInstaller.createSession(sessionParams2);
                sessionOpenSession = packageInstaller.openSession(iCreateSession);
                java.util.ArrayList arrayList = new java.util.ArrayList(this.info.getPackages().size());
                for (android.content.rollback.PackageRollbackInfo packageRollbackInfo : this.info.getPackages()) {
                    try {
                        arrayList.add(packageRollbackInfo.getPackageName());
                        if (!packageRollbackInfo.isApkInApex()) {
                            android.content.pm.PackageInstaller.SessionParams sessionParams3 = new android.content.pm.PackageInstaller.SessionParams(z ? 1 : 0);
                            java.lang.String installerPackageName = android.text.TextUtils.isEmpty(this.mInstallerPackageName) ? packageManager.getInstallerPackageName(packageRollbackInfo.getPackageName()) : this.mInstallerPackageName;
                            if (installerPackageName != null) {
                                sessionParams = sessionParams3;
                                sessionParams.setInstallerPackageName(installerPackageName);
                            } else {
                                sessionParams = sessionParams3;
                            }
                            sessionParams.setRequestDowngrade(z);
                            sessionParams.setRequiredInstalledVersionCode(packageRollbackInfo.getVersionRolledBackFrom().getLongVersionCode());
                            sessionParams.setInstallReason(5);
                            if (isStaged()) {
                                sessionParams.setStaged();
                            }
                            if (packageRollbackInfo.isApex()) {
                                sessionParams.setInstallAsApex();
                            }
                            int iCreateSession2 = packageInstaller.createSession(sessionParams);
                            android.content.pm.PackageInstaller.Session sessionOpenSession2 = packageInstaller.openSession(iCreateSession2);
                            java.io.File[] packageCodePaths = com.android.server.rollback.RollbackStore.getPackageCodePaths(this, packageRollbackInfo.getPackageName());
                            if (packageCodePaths == null) {
                                com.android.server.rollback.RollbackManagerServiceImpl.sendFailure(context, intentSender, 1, "Backup copy of package: " + packageRollbackInfo.getPackageName() + " is inaccessible");
                                return;
                            }
                            int length = packageCodePaths.length;
                            android.content.Context context2 = contextCreatePackageContextAsUser;
                            int i2 = 0;
                            while (i2 < length) {
                                try {
                                    java.io.File file = packageCodePaths[i2];
                                    java.io.File[] fileArr = packageCodePaths;
                                    android.os.ParcelFileDescriptor parcelFileDescriptorOpen = android.os.ParcelFileDescriptor.open(file, 268435456);
                                    try {
                                        long jClearCallingIdentity = android.os.Binder.clearCallingIdentity();
                                        try {
                                            i = length;
                                            session = sessionOpenSession2;
                                            try {
                                                try {
                                                    session.stageViaHardLink(file.getAbsolutePath());
                                                    objArr = false;
                                                } catch (java.lang.Exception e2) {
                                                    objArr = true;
                                                }
                                            } catch (java.lang.Throwable th2) {
                                                th = th2;
                                                android.os.Binder.restoreCallingIdentity(jClearCallingIdentity);
                                                throw th;
                                            }
                                        } catch (java.lang.Exception e3) {
                                            i = length;
                                            session = sessionOpenSession2;
                                        } catch (java.lang.Throwable th3) {
                                            th = th3;
                                        }
                                        if (objArr != false) {
                                            session.write(file.getName(), 0L, file.length(), parcelFileDescriptorOpen);
                                        }
                                        try {
                                            android.os.Binder.restoreCallingIdentity(jClearCallingIdentity);
                                            if (parcelFileDescriptorOpen != null) {
                                                parcelFileDescriptorOpen.close();
                                            }
                                            i2++;
                                            sessionOpenSession2 = session;
                                            packageCodePaths = fileArr;
                                            length = i;
                                        } catch (java.lang.Throwable th4) {
                                            th = th4;
                                            if (parcelFileDescriptorOpen == null) {
                                                throw th;
                                            }
                                            try {
                                                parcelFileDescriptorOpen.close();
                                                throw th;
                                            } catch (java.lang.Throwable th5) {
                                                th.addSuppressed(th5);
                                                throw th;
                                            }
                                        }
                                    } catch (java.lang.Throwable th6) {
                                        th = th6;
                                    }
                                } catch (java.io.IOException e4) {
                                    e = e4;
                                    android.util.Slog.e(TAG, "Rollback failed", e);
                                    com.android.server.rollback.RollbackManagerServiceImpl.sendFailure(context, intentSender, 1, "IOException: " + e.toString());
                                }
                            }
                            sessionOpenSession.addChildSessionId(iCreateSession2);
                            contextCreatePackageContextAsUser = context2;
                            z = true;
                        }
                    } catch (java.io.IOException e5) {
                        e = e5;
                    }
                }
                com.android.server.RescueParty.resetDeviceConfigForPackages(arrayList);
            } catch (java.io.IOException e6) {
                e = e6;
            }
            try {
                com.android.server.rollback.LocalIntentReceiver localIntentReceiver = new com.android.server.rollback.LocalIntentReceiver(new java.util.function.Consumer() { // from class: com.android.server.rollback.Rollback$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$commit$1(context, intentSender, list, (android.content.Intent) obj);
                    }
                });
                setState(3, "");
                this.info.setCommittedSessionId(iCreateSession);
                this.mRestoreUserDataInProgress = true;
                sessionOpenSession.commit(localIntentReceiver.getIntentSender());
            } catch (java.io.IOException e7) {
                e = e7;
                android.util.Slog.e(TAG, "Rollback failed", e);
                com.android.server.rollback.RollbackManagerServiceImpl.sendFailure(context, intentSender, 1, "IOException: " + e.toString());
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e8) {
            com.android.server.rollback.RollbackManagerServiceImpl.sendFailure(context, intentSender, 1, "Invalid callerPackageName");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$commit$1(final android.content.Context context, final android.content.IntentSender statusReceiver, final java.util.List causePackages, final android.content.Intent result) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.Rollback$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$commit$0(result, context, statusReceiver, causePackages);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$commit$0(android.content.Intent result, android.content.Context context, android.content.IntentSender statusReceiver, java.util.List causePackages) {
        assertInWorkerThread();
        int status = result.getIntExtra("android.content.pm.extra.STATUS", 1);
        if (status != 0) {
            setState(1, "Commit failed");
            this.mRestoreUserDataInProgress = false;
            this.info.setCommittedSessionId(-1);
            com.android.server.rollback.RollbackManagerServiceImpl.sendFailure(context, statusReceiver, 3, "Rollback downgrade install failed: " + result.getStringExtra("android.content.pm.extra.STATUS_MESSAGE"));
            return;
        }
        if (!isStaged()) {
            this.mRestoreUserDataInProgress = false;
        }
        this.info.getCausePackages().addAll(causePackages);
        com.android.server.rollback.RollbackStore.deletePackageCodePaths(this);
        com.android.server.rollback.RollbackStore.saveRollback(this);
        try {
            android.content.Intent fillIn = new android.content.Intent();
            fillIn.putExtra("android.content.rollback.extra.STATUS", 0);
            statusReceiver.sendIntent(context, 0, fillIn, null, null);
        } catch (android.content.IntentSender.SendIntentException e) {
        }
        android.content.Intent broadcast = new android.content.Intent("android.intent.action.ROLLBACK_COMMITTED");
        android.os.UserManager userManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        for (android.os.UserHandle user : userManager.getUserHandles(true)) {
            context.sendBroadcastAsUser(broadcast, user, "android.permission.MANAGE_ROLLBACKS");
        }
    }

    boolean restoreUserDataForPackageIfInProgress(java.lang.String packageName, int[] userIds, int appId, java.lang.String seInfo, com.android.server.rollback.AppDataRollbackHelper dataHelper) {
        assertInWorkerThread();
        if (!isRestoreUserDataInProgress()) {
            return false;
        }
        boolean foundPackage = false;
        java.util.Iterator it = this.info.getPackages().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.content.rollback.PackageRollbackInfo pkgRollbackInfo = (android.content.rollback.PackageRollbackInfo) it.next();
            if (pkgRollbackInfo.getPackageName().equals(packageName)) {
                foundPackage = true;
                boolean changedRollback = false;
                for (int userId : userIds) {
                    changedRollback |= dataHelper.restoreAppData(this.info.getRollbackId(), pkgRollbackInfo, userId, appId, seInfo);
                }
                if (changedRollback) {
                    com.android.server.rollback.RollbackStore.saveRollback(this);
                }
            }
        }
        return foundPackage;
    }

    void delete(com.android.server.rollback.AppDataRollbackHelper dataHelper, java.lang.String reason) {
        assertInWorkerThread();
        boolean containsApex = false;
        java.util.Set<java.lang.Integer> apexUsers = new android.util.ArraySet<>();
        for (android.content.rollback.PackageRollbackInfo pkgInfo : this.info.getPackages()) {
            java.util.List<java.lang.Integer> snapshottedUsers = pkgInfo.getSnapshottedUsers();
            if (pkgInfo.isApex()) {
                containsApex = true;
                apexUsers.addAll(snapshottedUsers);
            } else {
                for (int i = 0; i < snapshottedUsers.size(); i++) {
                    int userId = snapshottedUsers.get(i).intValue();
                    dataHelper.destroyAppDataSnapshot(this.info.getRollbackId(), pkgInfo, userId);
                }
            }
        }
        if (containsApex) {
            dataHelper.destroyApexDeSnapshots(this.info.getRollbackId());
            java.util.Iterator<java.lang.Integer> it = apexUsers.iterator();
            while (it.hasNext()) {
                int user = it.next().intValue();
                dataHelper.destroyApexCeSnapshots(user, this.info.getRollbackId());
            }
        }
        com.android.server.rollback.RollbackStore.deleteRollback(this);
        setState(4, reason);
    }

    boolean isRestoreUserDataInProgress() {
        assertInWorkerThread();
        return this.mRestoreUserDataInProgress;
    }

    void setRestoreUserDataInProgress(boolean restoreUserDataInProgress) {
        assertInWorkerThread();
        this.mRestoreUserDataInProgress = restoreUserDataInProgress;
        com.android.server.rollback.RollbackStore.saveRollback(this);
    }

    boolean includesPackage(java.lang.String packageName) {
        assertInWorkerThread();
        for (android.content.rollback.PackageRollbackInfo packageRollbackInfo : this.info.getPackages()) {
            if (packageRollbackInfo.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    boolean includesPackageWithDifferentVersion(java.lang.String packageName, long versionCode) {
        assertInWorkerThread();
        for (android.content.rollback.PackageRollbackInfo pkgRollbackInfo : this.info.getPackages()) {
            if (pkgRollbackInfo.getPackageName().equals(packageName) && pkgRollbackInfo.getVersionRolledBackFrom().getLongVersionCode() != versionCode) {
                return true;
            }
        }
        return false;
    }

    java.util.List<java.lang.String> getPackageNames() {
        assertInWorkerThread();
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        for (android.content.rollback.PackageRollbackInfo pkgRollbackInfo : this.info.getPackages()) {
            result.add(pkgRollbackInfo.getPackageName());
        }
        return result;
    }

    java.util.List<java.lang.String> getApexPackageNames() {
        assertInWorkerThread();
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        for (android.content.rollback.PackageRollbackInfo pkgRollbackInfo : this.info.getPackages()) {
            if (pkgRollbackInfo.isApex()) {
                result.add(pkgRollbackInfo.getPackageName());
            }
        }
        return result;
    }

    boolean containsSessionId(int packageSessionId) {
        for (int id : this.mPackageSessionIds) {
            if (id == packageSessionId) {
                return true;
            }
        }
        return false;
    }

    boolean allPackagesEnabled() {
        assertInWorkerThread();
        int packagesWithoutApkInApex = 0;
        for (android.content.rollback.PackageRollbackInfo rollbackInfo : this.info.getPackages()) {
            if (!rollbackInfo.isApkInApex()) {
                packagesWithoutApkInApex++;
            }
        }
        return packagesWithoutApkInApex == this.mPackageSessionIds.length;
    }

    static java.lang.String rollbackStateToString(int state) {
        switch (state) {
            case 0:
                return "enabling";
            case 1:
                return "available";
            case 2:
            default:
                throw new java.lang.AssertionError("Invalid rollback state: " + state);
            case 3:
                return "committed";
            case 4:
                return "deleted";
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0033  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static int rollbackStateFromString(java.lang.String r4) throws java.text.ParseException {
        /*
            int r0 = r4.hashCode()
            r1 = 3
            r2 = 1
            r3 = 0
            switch(r0) {
                case -1491142788: goto L29;
                case -733902135: goto L1f;
                case 1550463001: goto L15;
                case 1642196352: goto Lb;
                default: goto La;
            }
        La:
            goto L33
        Lb:
            java.lang.String r0 = "enabling"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = r3
            goto L34
        L15:
            java.lang.String r0 = "deleted"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = r1
            goto L34
        L1f:
            java.lang.String r0 = "available"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = r2
            goto L34
        L29:
            java.lang.String r0 = "committed"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = 2
            goto L34
        L33:
            r0 = -1
        L34:
            switch(r0) {
                case 0: goto L54;
                case 1: goto L53;
                case 2: goto L52;
                case 3: goto L50;
                default: goto L37;
            }
        L37:
            java.text.ParseException r0 = new java.text.ParseException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Invalid rollback state: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1, r3)
            throw r0
        L50:
            r0 = 4
            return r0
        L52:
            return r1
        L53:
            return r2
        L54:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.rollback.Rollback.rollbackStateFromString(java.lang.String):int");
    }

    java.lang.String getStateAsString() {
        assertInWorkerThread();
        return rollbackStateToString(this.mState);
    }

    static boolean extensionVersionReductionWouldViolateConstraint(android.util.SparseIntArray rollbackExtVers, android.content.pm.PackageManagerInternal pmi) {
        if (rollbackExtVers.size() == 0) {
            return false;
        }
        java.util.List<java.lang.String> packages = pmi.getPackageList().getPackageNames();
        for (int i = 0; i < packages.size(); i++) {
            com.android.server.pm.pkg.AndroidPackage pkg = pmi.getPackage(packages.get(i));
            android.util.SparseIntArray minExtVers = pkg.getMinExtensionVersions();
            if (minExtVers != null) {
                for (int j = 0; j < rollbackExtVers.size(); j++) {
                    int minExt = minExtVers.get(rollbackExtVers.keyAt(j), -1);
                    if (rollbackExtVers.valueAt(j) < minExt) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean wasCreatedAtLowerExtensionVersion() {
        for (int i = 0; i < this.mExtensionVersions.size(); i++) {
            if (android.os.ext.SdkExtensions.getExtensionVersion(this.mExtensionVersions.keyAt(i)) > this.mExtensionVersions.valueAt(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsApex() {
        for (android.content.rollback.PackageRollbackInfo pkgInfo : this.info.getPackages()) {
            if (pkgInfo.isApex()) {
                return true;
            }
        }
        return false;
    }

    void dump(com.android.internal.util.IndentingPrintWriter ipw) {
        assertInWorkerThread();
        ipw.println(this.info.getRollbackId() + ":");
        ipw.increaseIndent();
        ipw.println("-state: " + getStateAsString());
        ipw.println("-stateDescription: " + this.mStateDescription);
        ipw.println("-timestamp: " + getTimestamp());
        ipw.println("-rollbackLifetimeMillis: " + getRollbackLifetimeMillis());
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.recoverabilityDetection()) {
            ipw.println("-rollbackImpactLevel: " + this.info.getRollbackImpactLevel());
        }
        ipw.println("-isStaged: " + isStaged());
        ipw.println("-originalSessionId: " + getOriginalSessionId());
        ipw.println("-packages:");
        ipw.increaseIndent();
        for (android.content.rollback.PackageRollbackInfo pkg : this.info.getPackages()) {
            ipw.println(pkg.getPackageName() + " " + pkg.getVersionRolledBackFrom().getLongVersionCode() + " -> " + pkg.getVersionRolledBackTo().getLongVersionCode() + " [" + pkg.getRollbackDataPolicy() + "]");
        }
        ipw.decreaseIndent();
        if (isCommitted()) {
            ipw.println("-causePackages:");
            ipw.increaseIndent();
            for (android.content.pm.VersionedPackage cPkg : this.info.getCausePackages()) {
                ipw.println(cPkg.getPackageName() + " " + cPkg.getLongVersionCode());
            }
            ipw.decreaseIndent();
            ipw.println("-committedSessionId: " + this.info.getCommittedSessionId());
        }
        if (this.mExtensionVersions.size() > 0) {
            ipw.println("-extensionVersions:");
            ipw.increaseIndent();
            ipw.println(this.mExtensionVersions.toString());
            ipw.decreaseIndent();
        }
        ipw.decreaseIndent();
    }

    java.lang.String getStateDescription() {
        assertInWorkerThread();
        return this.mStateDescription;
    }

    void setState(int state, java.lang.String description) {
        assertInWorkerThread();
        this.mState = state;
        this.mStateDescription = description;
    }
}
