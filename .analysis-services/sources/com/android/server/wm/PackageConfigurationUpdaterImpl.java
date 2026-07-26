package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class PackageConfigurationUpdaterImpl implements com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater {
    private static final java.lang.String TAG = "PackageConfigurationUpdaterImpl";
    private com.android.server.wm.ActivityTaskManagerService mAtm;
    private int mGrammaticalGender;
    private android.os.LocaleList mLocales;
    private java.lang.Integer mNightMode;
    private java.lang.String mPackageName;
    private final java.util.Optional<java.lang.Integer> mPid;
    private int mUserId;

    PackageConfigurationUpdaterImpl(int pid, com.android.server.wm.ActivityTaskManagerService atm) {
        this.mPid = java.util.Optional.of(java.lang.Integer.valueOf(pid));
        this.mAtm = atm;
    }

    PackageConfigurationUpdaterImpl(java.lang.String packageName, int userId, com.android.server.wm.ActivityTaskManagerService atm) {
        this.mPackageName = packageName;
        this.mUserId = userId;
        this.mAtm = atm;
        this.mPid = java.util.Optional.empty();
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater
    public com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater setNightMode(int nightMode) {
        synchronized (this) {
            this.mNightMode = java.lang.Integer.valueOf(nightMode);
        }
        return this;
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater
    public com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater setLocales(android.os.LocaleList locales) {
        synchronized (this) {
            this.mLocales = locales;
        }
        return this;
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater
    public com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater setGrammaticalGender(int gender) {
        synchronized (this) {
            this.mGrammaticalGender = gender;
        }
        return this;
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater
    public boolean commit() {
        int uid;
        synchronized (this) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    long ident = android.os.Binder.clearCallingIdentity();
                    try {
                        if (this.mPid.isPresent()) {
                            com.android.server.wm.WindowProcessController wpc = this.mAtm.mProcessMap.getProcess(this.mPid.get().intValue());
                            if (wpc == null) {
                                android.util.Slog.w(TAG, "commit: Override application configuration failed: cannot find pid " + this.mPid);
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return false;
                            }
                            uid = wpc.mUid;
                            this.mUserId = wpc.mUserId;
                            this.mPackageName = wpc.mInfo.packageName;
                        } else {
                            int uid2 = this.mAtm.getPackageManagerInternalLocked().getPackageUid(this.mPackageName, 131072L, this.mUserId);
                            if (uid2 < 0) {
                                android.util.Slog.w(TAG, "commit: update of application configuration failed: userId or packageName not valid " + this.mUserId);
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return false;
                            }
                            uid = uid2;
                        }
                        updateConfig(uid, this.mPackageName);
                        boolean zUpdateFromImpl = this.mAtm.mPackageConfigPersister.updateFromImpl(this.mPackageName, this.mUserId, this);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return zUpdateFromImpl;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(ident);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
    }

    private void updateConfig(int uid, java.lang.String packageName) {
        android.util.ArraySet<com.android.server.wm.WindowProcessController> processes = this.mAtm.mProcessMap.getProcesses(uid);
        if (processes == null || processes.isEmpty()) {
            return;
        }
        android.os.LocaleList localesOverride = com.android.server.wm.LocaleOverlayHelper.combineLocalesIfOverlayExists(this.mLocales, this.mAtm.getGlobalConfiguration().getLocales());
        for (int i = processes.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowProcessController wpc = processes.valueAt(i);
            if (wpc.mInfo.packageName.equals(packageName)) {
                wpc.applyAppSpecificConfig(this.mNightMode, localesOverride, java.lang.Integer.valueOf(this.mGrammaticalGender));
            }
            wpc.updateAppSpecificSettingsForAllActivitiesInPackage(packageName, this.mNightMode, localesOverride, this.mGrammaticalGender);
        }
    }

    java.lang.Integer getNightMode() {
        return this.mNightMode;
    }

    android.os.LocaleList getLocales() {
        return this.mLocales;
    }

    java.lang.Integer getGrammaticalGender() {
        return java.lang.Integer.valueOf(this.mGrammaticalGender);
    }
}
