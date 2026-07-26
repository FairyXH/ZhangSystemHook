package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class PackageMetrics {
    public static final int STEP_COMMIT = 4;
    public static final int STEP_DEXOPT = 5;
    public static final int STEP_FREEZE_INSTALL = 6;
    public static final int STEP_PREPARE = 1;
    public static final int STEP_RECONCILE = 3;
    public static final int STEP_SCAN = 2;
    private static final java.lang.String TAG = "PackageMetrics";
    private final com.android.server.pm.InstallRequest mInstallRequest;
    private final android.util.SparseArray<com.android.server.pm.PackageMetrics.InstallStep> mInstallSteps = new android.util.SparseArray<>();
    private final long mInstallStartTimestampMillis = java.lang.System.currentTimeMillis();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface StepInt {
    }

    PackageMetrics(com.android.server.pm.InstallRequest installRequest) {
        this.mInstallRequest = installRequest;
    }

    public void onInstallSucceed() {
        reportInstallationToSecurityLog(this.mInstallRequest.getUserId());
        reportInstallationStats(true);
    }

    public void onInstallFailed() {
        reportInstallationStats(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0086  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void reportInstallationStats(boolean r44) {
        /*
            Method dump skipped, instruction units count: 269
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageMetrics.reportInstallationStats(boolean):void");
    }

    private static int getUid(int appId, int userId) {
        if (userId == -1) {
            userId = android.app.ActivityManager.getCurrentUser();
        }
        return android.os.UserHandle.getUid(userId, appId);
    }

    private long getApksSize(final java.io.File apkDir) {
        final java.util.concurrent.atomic.AtomicLong apksSize = new java.util.concurrent.atomic.AtomicLong();
        try {
            java.nio.file.Files.walkFileTree(apkDir.toPath(), new java.nio.file.SimpleFileVisitor<java.nio.file.Path>() { // from class: com.android.server.pm.PackageMetrics.1
                @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
                public java.nio.file.FileVisitResult preVisitDirectory(java.nio.file.Path dir, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
                    if (dir.equals(apkDir.toPath())) {
                        return java.nio.file.FileVisitResult.CONTINUE;
                    }
                    return java.nio.file.FileVisitResult.SKIP_SUBTREE;
                }

                @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
                public java.nio.file.FileVisitResult visitFile(java.nio.file.Path file, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
                    if (file.toFile().isFile() && android.content.pm.parsing.ApkLiteParseUtils.isApkFile(file.toFile())) {
                        apksSize.addAndGet(file.toFile().length());
                    }
                    return java.nio.file.FileVisitResult.CONTINUE;
                }
            });
        } catch (java.io.IOException e) {
        }
        return apksSize.get();
    }

    public void onStepStarted(int step) {
        this.mInstallSteps.put(step, new com.android.server.pm.PackageMetrics.InstallStep());
    }

    public void onStepFinished(int step) {
        com.android.server.pm.PackageMetrics.InstallStep installStep = this.mInstallSteps.get(step);
        if (installStep != null) {
            installStep.finish();
        }
    }

    public void onStepFinished(int step, long durationMillis) {
        this.mInstallSteps.put(step, new com.android.server.pm.PackageMetrics.InstallStep(durationMillis));
    }

    private android.util.Pair<int[], long[]> getInstallStepDurations() {
        java.util.ArrayList<java.lang.Integer> steps = new java.util.ArrayList<>();
        java.util.ArrayList<java.lang.Long> durations = new java.util.ArrayList<>();
        for (int i = 0; i < this.mInstallSteps.size(); i++) {
            long duration = this.mInstallSteps.valueAt(i).getDurationMillis();
            if (duration >= 0) {
                steps.add(java.lang.Integer.valueOf(this.mInstallSteps.keyAt(i)));
                durations.add(java.lang.Long.valueOf(this.mInstallSteps.valueAt(i).getDurationMillis()));
            }
        }
        int i2 = steps.size();
        int[] stepsArray = new int[i2];
        long[] durationsArray = new long[durations.size()];
        for (int i3 = 0; i3 < stepsArray.length; i3++) {
            stepsArray[i3] = steps.get(i3).intValue();
            durationsArray[i3] = durations.get(i3).longValue();
        }
        return new android.util.Pair<>(stepsArray, durationsArray);
    }

    private static class InstallStep {
        private long mDurationMillis;
        private final long mStartTimestampMillis;

        InstallStep() {
            this.mDurationMillis = -1L;
            this.mStartTimestampMillis = java.lang.System.currentTimeMillis();
        }

        InstallStep(long durationMillis) {
            this.mDurationMillis = -1L;
            this.mStartTimestampMillis = -1L;
            this.mDurationMillis = durationMillis;
        }

        void finish() {
            this.mDurationMillis = java.lang.System.currentTimeMillis() - this.mStartTimestampMillis;
        }

        long getDurationMillis() {
            return this.mDurationMillis;
        }
    }

    public static void onUninstallSucceeded(com.android.server.pm.PackageRemovedInfo info, int deleteFlags, int userId) {
        com.android.server.pm.UserManagerInternal userManagerInternal;
        if (info.mIsUpdate || (userManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)) == null) {
            return;
        }
        int[] removedUsers = info.mRemovedUsers;
        int[] removedUserTypes = userManagerInternal.getUserTypesForStatsd(removedUsers);
        int[] originalUsers = info.mOrigUsers;
        int[] originalUserTypes = userManagerInternal.getUserTypesForStatsd(originalUsers);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PACKAGE_UNINSTALLATION_REPORTED, getUid(info.mUid, userId), removedUsers, removedUserTypes, originalUsers, originalUserTypes, deleteFlags, 1, info.mIsRemovedPackageSystemUpdate, !info.mRemovedForAllUsers);
        java.lang.String packageName = info.mRemovedPackage;
        long versionCode = info.mRemovedPackageVersionCode;
        reportUninstallationToSecurityLog(packageName, versionCode, userId);
    }

    public static void onVerificationFailed(com.android.server.pm.VerifyingSession verifyingSession) {
        com.android.internal.util.FrameworkStatsLog.write(524, verifyingSession.getSessionId(), (java.lang.String) null, -1, (int[]) null, (int[]) null, (int[]) null, (int[]) null, verifyingSession.getRet(), 0, 0L, 0L, (int[]) null, (long[]) null, 0L, 0, verifyingSession.getInstallerPackageUid(), -1, verifyingSession.getDataLoaderType(), verifyingSession.getUserActionRequiredType(), verifyingSession.isInstant(), false, false, verifyingSession.isInherit(), false, false, verifyingSession.isStaged());
    }

    private void reportInstallationToSecurityLog(int userId) {
        if (!android.app.admin.SecurityLog.isLoggingEnabled()) {
            return;
        }
        try {
            com.android.server.pm.PackageSetting ps = this.mInstallRequest.getScannedPackageSetting();
            if (ps == null) {
                return;
            }
            java.lang.String packageName = ps.getPackageName();
            long versionCode = ps.getVersionCode();
            if (!this.mInstallRequest.isInstallReplace()) {
                android.app.admin.SecurityLog.writeEvent(210041, new java.lang.Object[]{packageName, java.lang.Long.valueOf(versionCode), java.lang.Integer.valueOf(userId)});
            } else {
                android.app.admin.SecurityLog.writeEvent(210042, new java.lang.Object[]{packageName, java.lang.Long.valueOf(versionCode), java.lang.Integer.valueOf(userId)});
            }
        } catch (java.lang.IllegalStateException | java.lang.NullPointerException e) {
        }
    }

    private static void reportUninstallationToSecurityLog(java.lang.String packageName, long versionCode, int userId) {
        if (!android.app.admin.SecurityLog.isLoggingEnabled()) {
            return;
        }
        android.app.admin.SecurityLog.writeEvent(210043, new java.lang.Object[]{packageName, java.lang.Long.valueOf(versionCode), java.lang.Integer.valueOf(userId)});
    }

    public static class ComponentStateMetrics {
        public int mCallingUid;
        private java.lang.String mClassName;
        public int mComponentNewState;
        public int mComponentOldState;
        public boolean mIsForWholeApp;
        private java.lang.String mPackageName;
        public int mUid;

        ComponentStateMetrics(android.content.pm.PackageManager.ComponentEnabledSetting setting, int uid, int componentOldState, int callingUid) {
            this.mUid = uid;
            this.mComponentOldState = componentOldState;
            this.mComponentNewState = setting.getEnabledState();
            this.mIsForWholeApp = !setting.isComponent();
            this.mPackageName = setting.getPackageName();
            this.mClassName = setting.getClassName();
            this.mCallingUid = callingUid;
        }

        public boolean isLauncherActivity(com.android.server.pm.Computer computer, int userId) {
            if (this.mIsForWholeApp) {
                return false;
            }
            android.content.Intent intent = new android.content.Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setPackage(this.mPackageName);
            java.util.List<android.content.pm.ResolveInfo> launcherActivities = computer.queryIntentActivitiesInternal(intent, null, 787008L, 1000, userId);
            int launcherActivitiesSize = launcherActivities != null ? launcherActivities.size() : 0;
            for (int i = 0; i < launcherActivitiesSize; i++) {
                android.content.pm.ResolveInfo resolveInfo = launcherActivities.get(i);
                if (isSameComponent(resolveInfo.activityInfo)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isSameComponent(android.content.pm.ActivityInfo activityInfo) {
            if (activityInfo == null) {
                return false;
            }
            return this.mIsForWholeApp ? android.text.TextUtils.equals(activityInfo.packageName, this.mPackageName) : activityInfo.getComponentName().equals(new android.content.ComponentName(this.mPackageName, this.mClassName));
        }
    }

    public static void reportComponentStateChanged(com.android.server.pm.Computer computer, java.util.List<com.android.server.pm.PackageMetrics.ComponentStateMetrics> componentStateMetricsList, int userId) {
        if (!com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.componentStateChangedMetrics()) {
            return;
        }
        if (componentStateMetricsList == null || componentStateMetricsList.isEmpty()) {
            android.util.Slog.d(TAG, "Fail to report component state due to metrics is empty");
            return;
        }
        int metricsSize = componentStateMetricsList.size();
        for (int i = 0; i < metricsSize; i++) {
            com.android.server.pm.PackageMetrics.ComponentStateMetrics componentStateMetrics = componentStateMetricsList.get(i);
            reportComponentStateChanged(componentStateMetrics.mUid, componentStateMetrics.mComponentOldState, componentStateMetrics.mComponentNewState, componentStateMetrics.isLauncherActivity(computer, userId), componentStateMetrics.mIsForWholeApp, componentStateMetrics.mCallingUid);
        }
    }

    private static void reportComponentStateChanged(int uid, int componentOldState, int componentNewState, boolean isLauncher, boolean isForWholeApp, int callingUid) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.COMPONENT_STATE_CHANGED_REPORTED, uid, componentOldState, componentNewState, isLauncher, isForWholeApp, callingUid);
    }
}
