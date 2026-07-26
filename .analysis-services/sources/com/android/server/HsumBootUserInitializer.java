package com.android.server;

/* JADX INFO: loaded from: classes.dex */
final class HsumBootUserInitializer {
    private static final java.lang.String TAG = com.android.server.HsumBootUserInitializer.class.getSimpleName();
    private final com.android.server.am.ActivityManagerService mAms;
    private final android.content.ContentResolver mContentResolver;
    private final android.database.ContentObserver mDeviceProvisionedObserver = new android.database.ContentObserver(new android.os.Handler(android.os.Looper.getMainLooper())) { // from class: com.android.server.HsumBootUserInitializer.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            if (com.android.server.HsumBootUserInitializer.this.isDeviceProvisioned()) {
                com.android.server.utils.Slogf.i(com.android.server.HsumBootUserInitializer.TAG, "Marking USER_SETUP_COMPLETE for system user");
                android.provider.Settings.Secure.putInt(com.android.server.HsumBootUserInitializer.this.mContentResolver, "user_setup_complete", 1);
                com.android.server.HsumBootUserInitializer.this.mContentResolver.unregisterContentObserver(com.android.server.HsumBootUserInitializer.this.mDeviceProvisionedObserver);
            }
        }
    };
    private final com.android.server.pm.PackageManagerService mPms;
    private final boolean mShouldAlwaysHaveMainUser;
    private final com.android.server.pm.UserManagerInternal mUmi;

    public static com.android.server.HsumBootUserInitializer createInstance(com.android.server.am.ActivityManagerService am, com.android.server.pm.PackageManagerService pms, android.content.ContentResolver contentResolver, boolean shouldAlwaysHaveMainUser) {
        if (!android.os.UserManager.isHeadlessSystemUserMode()) {
            return null;
        }
        return new com.android.server.HsumBootUserInitializer((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class), am, pms, contentResolver, shouldAlwaysHaveMainUser);
    }

    private HsumBootUserInitializer(com.android.server.pm.UserManagerInternal umi, com.android.server.am.ActivityManagerService am, com.android.server.pm.PackageManagerService pms, android.content.ContentResolver contentResolver, boolean shouldAlwaysHaveMainUser) {
        this.mUmi = umi;
        this.mAms = am;
        this.mPms = pms;
        this.mContentResolver = contentResolver;
        this.mShouldAlwaysHaveMainUser = shouldAlwaysHaveMainUser;
    }

    public void init(com.android.server.utils.TimingsTraceAndSlog t) {
        com.android.server.utils.Slogf.i(TAG, "init())");
        if (this.mShouldAlwaysHaveMainUser) {
            t.traceBegin("createMainUserIfNeeded");
            createMainUserIfNeeded();
            t.traceEnd();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    private void createMainUserIfNeeded() {
        int mainUser = this.mUmi.getMainUserId();
        if (mainUser != -10000) {
            com.android.server.utils.Slogf.d(TAG, "Found existing MainUser, userId=%d", java.lang.Integer.valueOf(mainUser));
            return;
        }
        com.android.server.utils.Slogf.d(TAG, "Creating a new MainUser");
        try {
            android.content.pm.UserInfo newInitialUser = this.mUmi.createUserEvenWhenDisallowed(null, "android.os.usertype.full.SECONDARY", 16386, null, null);
            com.android.server.utils.Slogf.i(TAG, "Successfully created MainUser, userId=%d", java.lang.Integer.valueOf(newInitialUser.id));
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            com.android.server.utils.Slogf.wtf(TAG, "Initial bootable MainUser creation failed", (java.lang.Throwable) e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    public void systemRunning(com.android.server.utils.TimingsTraceAndSlog t) {
        observeDeviceProvisioning();
        unlockSystemUser(t);
        try {
            t.traceBegin("getBootUser");
            int bootUser = this.mUmi.getBootUser(this.mPms.hasSystemFeature("android.hardware.type.automotive", 0));
            t.traceEnd();
            t.traceBegin("switchToBootUser-" + bootUser);
            switchToBootUser(bootUser);
            t.traceEnd();
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            com.android.server.utils.Slogf.wtf(TAG, "Failed to switch to boot user since there isn't one.");
        }
    }

    private void observeDeviceProvisioning() {
        if (isDeviceProvisioned()) {
            return;
        }
        this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("device_provisioned"), false, this.mDeviceProvisionedObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceProvisioned() {
        try {
            return android.provider.Settings.Global.getInt(this.mContentResolver, "device_provisioned") == 1;
        } catch (java.lang.Exception e) {
            com.android.server.utils.Slogf.wtf(TAG, "DEVICE_PROVISIONED setting not found.", e);
            return false;
        }
    }

    private void unlockSystemUser(com.android.server.utils.TimingsTraceAndSlog t) {
        com.android.server.utils.Slogf.i(TAG, "Unlocking system user");
        t.traceBegin("unlock-system-user");
        try {
            t.traceBegin("am.startUser");
            boolean started = this.mAms.startUserInBackgroundWithListener(0, null);
            t.traceEnd();
            if (!started) {
                com.android.server.utils.Slogf.w(TAG, "could not restart system user in background; trying unlock instead");
                t.traceBegin("am.unlockUser");
                boolean unlocked = this.mAms.unlockUser(0, null, null, null);
                t.traceEnd();
                if (!unlocked) {
                    com.android.server.utils.Slogf.w(TAG, "could not unlock system user either");
                }
            }
        } finally {
            t.traceEnd();
        }
    }

    private void switchToBootUser(int bootUserId) {
        com.android.server.utils.Slogf.i(TAG, "Switching to boot user %d", java.lang.Integer.valueOf(bootUserId));
        boolean started = this.mAms.startUserInForegroundWithListener(bootUserId, null);
        if (!started) {
            com.android.server.utils.Slogf.wtf(TAG, "Failed to start user %d in foreground", java.lang.Integer.valueOf(bootUserId));
        }
    }
}
