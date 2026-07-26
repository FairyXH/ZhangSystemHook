package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class AppWarnings {
    private static final java.lang.String CONFIG_FILE_NAME = "packages-warnings.xml";
    public static final int FLAG_HIDE_COMPILE_SDK = 2;
    public static final int FLAG_HIDE_DEPRECATED_ABI = 8;
    public static final int FLAG_HIDE_DEPRECATED_SDK = 4;
    public static final int FLAG_HIDE_DISPLAY_SIZE = 1;
    private static final java.lang.String TAG = "AppWarnings";
    private final com.android.server.wm.ActivityTaskManagerService mAtm;
    private final android.util.AtomicFile mConfigFile;
    private android.util.SparseArray<com.android.server.wm.DeprecatedAbiDialog> mDeprecatedAbiDialogs;
    private android.util.SparseArray<com.android.server.wm.DeprecatedTargetSdkVersionDialog> mDeprecatedTargetSdkVersionDialogs;
    private final com.android.server.wm.AppWarnings.UiHandler mUiHandler;
    private android.util.SparseArray<com.android.server.wm.UnsupportedCompileSdkDialog> mUnsupportedCompileSdkDialogs;
    private android.util.SparseArray<com.android.server.wm.UnsupportedDisplaySizeDialog> mUnsupportedDisplaySizeDialogs;
    private com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private final android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, java.lang.Integer> mPackageFlags = new android.util.ArrayMap<>();
    private final android.util.ArraySet<android.content.ComponentName> mAlwaysShowUnsupportedCompileSdkWarningActivities = new android.util.ArraySet<>();
    private final com.android.server.wm.AppWarnings.WriteConfigTask mWriteConfigTask = new com.android.server.wm.AppWarnings.WriteConfigTask();

    void alwaysShowUnsupportedCompileSdkWarning(android.content.ComponentName activity) {
        this.mAlwaysShowUnsupportedCompileSdkWarningActivities.add(activity);
    }

    public AppWarnings(com.android.server.wm.ActivityTaskManagerService atm, android.content.Context uiContext, android.os.Handler handler, android.os.Handler uiHandler, java.io.File systemDir) {
        this.mAtm = atm;
        this.mUiHandler = new com.android.server.wm.AppWarnings.UiHandler(uiHandler.getLooper());
        this.mConfigFile = new android.util.AtomicFile(new java.io.File(systemDir, CONFIG_FILE_NAME), "warnings-config");
    }

    void onSystemReady() {
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        readConfigFromFileAmsThread();
        if (!android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
            return;
        }
        this.mUserManagerInternal.addUserLifecycleListener(new com.android.server.pm.UserManagerInternal.UserLifecycleListener() { // from class: com.android.server.wm.AppWarnings.1
            @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
            public void onUserRemoved(android.content.pm.UserInfo user) {
                if (!user.isFull()) {
                    return;
                }
                com.android.server.wm.AppWarnings.this.mUiHandler.hideDialogsForPackage(null, user.id);
                com.android.server.wm.AppWarnings.this.clearAllPackageFlagsForUser(user.id);
            }
        });
    }

    public void showUnsupportedDisplaySizeDialogIfNeeded(com.android.server.wm.ActivityRecord r) {
        android.content.res.Configuration globalConfig = this.mAtm.getGlobalConfiguration();
        if (globalConfig.densityDpi != android.util.DisplayMetrics.DENSITY_DEVICE_STABLE && r.info.applicationInfo.requiresSmallestWidthDp > globalConfig.smallestScreenWidthDp) {
            this.mUiHandler.showUnsupportedDisplaySizeDialog(r);
        }
    }

    public void showUnsupportedCompileSdkDialogIfNeeded(com.android.server.wm.ActivityRecord r) {
        if (r.info.applicationInfo.compileSdkVersion == 0 || r.info.applicationInfo.compileSdkVersionCodename == null || !this.mAlwaysShowUnsupportedCompileSdkWarningActivities.contains(r.mActivityComponent)) {
            return;
        }
        int compileSdk = r.info.applicationInfo.compileSdkVersion;
        int platformSdk = android.os.Build.VERSION.SDK_INT;
        boolean isCompileSdkPreview = !"REL".equals(r.info.applicationInfo.compileSdkVersionCodename);
        boolean isPlatformSdkPreview = !"REL".equals(android.os.Build.VERSION.CODENAME);
        if ((isCompileSdkPreview && compileSdk < platformSdk) || ((isPlatformSdkPreview && platformSdk < compileSdk) || (isCompileSdkPreview && isPlatformSdkPreview && platformSdk == compileSdk && !android.os.Build.VERSION.CODENAME.equals(r.info.applicationInfo.compileSdkVersionCodename)))) {
            this.mUiHandler.showUnsupportedCompileSdkDialog(r);
        }
    }

    public void showDeprecatedTargetDialogIfNeeded(com.android.server.wm.ActivityRecord r) {
        boolean disableDeprecatedTargetSdkDialog = android.os.SystemProperties.getBoolean("debug.wm.disable_deprecated_target_sdk_dialog", false);
        if (r.info.applicationInfo.targetSdkVersion < android.os.Build.VERSION.MIN_SUPPORTED_TARGET_SDK_INT && !disableDeprecatedTargetSdkDialog) {
            this.mUiHandler.showDeprecatedTargetDialog(r);
        }
    }

    public void showDeprecatedAbiDialogIfNeeded(com.android.server.wm.ActivityRecord r) {
        boolean isUsingAbiOverride = (r.info.applicationInfo.privateFlagsExt & 32) != 0;
        if (isUsingAbiOverride) {
            return;
        }
        boolean disableDeprecatedAbiDialog = android.os.SystemProperties.getBoolean("debug.wm.disable_deprecated_abi_dialog", false);
        if (disableDeprecatedAbiDialog) {
            return;
        }
        java.lang.String appPrimaryAbi = r.info.applicationInfo.primaryCpuAbi;
        java.lang.String appSecondaryAbi = r.info.applicationInfo.secondaryCpuAbi;
        boolean appContainsOnly32bitLibraries = (appPrimaryAbi == null || appSecondaryAbi != null || appPrimaryAbi.contains("64")) ? false : true;
        boolean is64BitDevice = com.android.internal.util.ArrayUtils.find(android.os.Build.SUPPORTED_ABIS, new java.util.function.Predicate() { // from class: com.android.server.wm.AppWarnings$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((java.lang.String) obj).contains("64");
            }
        }) != null;
        if (is64BitDevice && appContainsOnly32bitLibraries) {
            this.mUiHandler.showDeprecatedAbiDialog(r);
        }
    }

    public void onStartActivity(com.android.server.wm.ActivityRecord r) {
        showUnsupportedCompileSdkDialogIfNeeded(r);
        showUnsupportedDisplaySizeDialogIfNeeded(r);
        showDeprecatedTargetDialogIfNeeded(r);
        showDeprecatedAbiDialogIfNeeded(r);
    }

    public void onResumeActivity(com.android.server.wm.ActivityRecord r) {
        showUnsupportedDisplaySizeDialogIfNeeded(r);
    }

    public void onPackageDataCleared(java.lang.String name, int userId) {
        removePackageAndHideDialogs(name, userId);
    }

    public void onPackageUninstalled(java.lang.String name, int userId) {
        removePackageAndHideDialogs(name, userId);
    }

    public void onDensityChanged() {
        this.mUiHandler.hideUnsupportedDisplaySizeDialog();
    }

    private void removePackageAndHideDialogs(java.lang.String name, int userId) {
        int userId2;
        if (!android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
            userId2 = 0;
        } else {
            userId2 = this.mUserManagerInternal.getProfileParentId(userId);
        }
        this.mUiHandler.hideDialogsForPackage(name, userId2);
        synchronized (this.mPackageFlags) {
            android.util.Pair<java.lang.Integer, java.lang.String> packageKey = android.util.Pair.create(java.lang.Integer.valueOf(userId2), name);
            if (this.mPackageFlags.remove(packageKey) != null) {
                this.mWriteConfigTask.schedule();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideUnsupportedDisplaySizeDialogUiThread() {
        if (this.mUnsupportedDisplaySizeDialogs == null) {
            return;
        }
        for (int i = 0; i < this.mUnsupportedDisplaySizeDialogs.size(); i++) {
            this.mUnsupportedDisplaySizeDialogs.valueAt(i).dismiss();
        }
        this.mUnsupportedDisplaySizeDialogs.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUnsupportedDisplaySizeDialogUiThread(com.android.server.wm.ActivityRecord ar) {
        com.android.server.wm.UnsupportedDisplaySizeDialog unsupportedDisplaySizeDialog;
        int userId = getUserIdForActivity(ar);
        if (this.mUnsupportedDisplaySizeDialogs != null && (unsupportedDisplaySizeDialog = this.mUnsupportedDisplaySizeDialogs.get(userId)) != null) {
            unsupportedDisplaySizeDialog.dismiss();
            this.mUnsupportedDisplaySizeDialogs.remove(userId);
        }
        if (!hasPackageFlag(userId, ar.packageName, 1)) {
            com.android.server.wm.UnsupportedDisplaySizeDialog unsupportedDisplaySizeDialog2 = new com.android.server.wm.UnsupportedDisplaySizeDialog(this, getUiContextForActivity(ar), ar.info.applicationInfo, userId);
            unsupportedDisplaySizeDialog2.show();
            if (this.mUnsupportedDisplaySizeDialogs == null) {
                this.mUnsupportedDisplaySizeDialogs = new android.util.SparseArray<>();
            }
            this.mUnsupportedDisplaySizeDialogs.put(userId, unsupportedDisplaySizeDialog2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUnsupportedCompileSdkDialogUiThread(com.android.server.wm.ActivityRecord ar) {
        com.android.server.wm.UnsupportedCompileSdkDialog unsupportedCompileSdkDialog;
        int userId = getUserIdForActivity(ar);
        if (this.mUnsupportedCompileSdkDialogs != null && (unsupportedCompileSdkDialog = this.mUnsupportedCompileSdkDialogs.get(userId)) != null) {
            unsupportedCompileSdkDialog.dismiss();
            this.mUnsupportedCompileSdkDialogs.remove(userId);
        }
        if (!hasPackageFlag(userId, ar.packageName, 2)) {
            com.android.server.wm.UnsupportedCompileSdkDialog unsupportedCompileSdkDialog2 = new com.android.server.wm.UnsupportedCompileSdkDialog(this, getUiContextForActivity(ar), ar.info.applicationInfo, userId);
            unsupportedCompileSdkDialog2.show();
            if (this.mUnsupportedCompileSdkDialogs == null) {
                this.mUnsupportedCompileSdkDialogs = new android.util.SparseArray<>();
            }
            this.mUnsupportedCompileSdkDialogs.put(userId, unsupportedCompileSdkDialog2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDeprecatedTargetSdkDialogUiThread(com.android.server.wm.ActivityRecord ar) {
        com.android.server.wm.DeprecatedTargetSdkVersionDialog deprecatedTargetSdkVersionDialog;
        int userId = getUserIdForActivity(ar);
        if (this.mDeprecatedTargetSdkVersionDialogs != null && (deprecatedTargetSdkVersionDialog = this.mDeprecatedTargetSdkVersionDialogs.get(userId)) != null) {
            deprecatedTargetSdkVersionDialog.dismiss();
            this.mDeprecatedTargetSdkVersionDialogs.remove(userId);
        }
        if (!hasPackageFlag(userId, ar.packageName, 4)) {
            com.android.server.wm.DeprecatedTargetSdkVersionDialog deprecatedTargetSdkVersionDialog2 = new com.android.server.wm.DeprecatedTargetSdkVersionDialog(this, getUiContextForActivity(ar), ar.info.applicationInfo, userId);
            deprecatedTargetSdkVersionDialog2.show();
            if (this.mDeprecatedTargetSdkVersionDialogs == null) {
                this.mDeprecatedTargetSdkVersionDialogs = new android.util.SparseArray<>();
            }
            this.mDeprecatedTargetSdkVersionDialogs.put(userId, deprecatedTargetSdkVersionDialog2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDeprecatedAbiDialogUiThread(com.android.server.wm.ActivityRecord ar) {
        com.android.server.wm.DeprecatedAbiDialog deprecatedAbiDialog;
        int userId = getUserIdForActivity(ar);
        if (this.mDeprecatedAbiDialogs != null && (deprecatedAbiDialog = this.mDeprecatedAbiDialogs.get(userId)) != null) {
            deprecatedAbiDialog.dismiss();
            this.mDeprecatedAbiDialogs.remove(userId);
        }
        if (!hasPackageFlag(userId, ar.packageName, 8)) {
            com.android.server.wm.DeprecatedAbiDialog deprecatedAbiDialog2 = new com.android.server.wm.DeprecatedAbiDialog(this, getUiContextForActivity(ar), ar.info.applicationInfo, userId);
            deprecatedAbiDialog2.show();
            if (this.mDeprecatedAbiDialogs == null) {
                this.mDeprecatedAbiDialogs = new android.util.SparseArray<>();
            }
            this.mDeprecatedAbiDialogs.put(userId, deprecatedAbiDialog2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideDialogsForPackageUiThread(java.lang.String name, int userId) {
        com.android.server.wm.DeprecatedAbiDialog deprecatedAbiDialog;
        com.android.server.wm.DeprecatedTargetSdkVersionDialog deprecatedTargetSdkVersionDialog;
        com.android.server.wm.UnsupportedCompileSdkDialog unsupportedCompileSdkDialog;
        com.android.server.wm.UnsupportedDisplaySizeDialog unsupportedDisplaySizeDialog;
        if (this.mUnsupportedDisplaySizeDialogs != null && (unsupportedDisplaySizeDialog = this.mUnsupportedDisplaySizeDialogs.get(userId)) != null && (name == null || name.equals(unsupportedDisplaySizeDialog.mPackageName))) {
            unsupportedDisplaySizeDialog.dismiss();
            this.mUnsupportedDisplaySizeDialogs.remove(userId);
        }
        if (this.mUnsupportedCompileSdkDialogs != null && (unsupportedCompileSdkDialog = this.mUnsupportedCompileSdkDialogs.get(userId)) != null && (name == null || name.equals(unsupportedCompileSdkDialog.mPackageName))) {
            unsupportedCompileSdkDialog.dismiss();
            this.mUnsupportedCompileSdkDialogs.remove(userId);
        }
        if (this.mDeprecatedTargetSdkVersionDialogs != null && (deprecatedTargetSdkVersionDialog = this.mDeprecatedTargetSdkVersionDialogs.get(userId)) != null && (name == null || name.equals(deprecatedTargetSdkVersionDialog.mPackageName))) {
            deprecatedTargetSdkVersionDialog.dismiss();
            this.mDeprecatedTargetSdkVersionDialogs.remove(userId);
        }
        if (this.mDeprecatedAbiDialogs == null || (deprecatedAbiDialog = this.mDeprecatedAbiDialogs.get(userId)) == null) {
            return;
        }
        if (name == null || name.equals(deprecatedAbiDialog.mPackageName)) {
            deprecatedAbiDialog.dismiss();
            this.mDeprecatedAbiDialogs.remove(userId);
        }
    }

    boolean hasPackageFlag(int userId, java.lang.String name, int flag) {
        return (getPackageFlags(userId, name) & flag) == flag;
    }

    void setPackageFlag(int userId, java.lang.String name, int flag, boolean enabled) {
        synchronized (this.mPackageFlags) {
            int curFlags = getPackageFlags(userId, name);
            int newFlags = enabled ? curFlags | flag : (~flag) & curFlags;
            if (curFlags != newFlags) {
                android.util.Pair<java.lang.Integer, java.lang.String> packageKey = android.util.Pair.create(java.lang.Integer.valueOf(userId), name);
                if (newFlags != 0) {
                    this.mPackageFlags.put(packageKey, java.lang.Integer.valueOf(newFlags));
                } else {
                    this.mPackageFlags.remove(packageKey);
                }
                this.mWriteConfigTask.schedule();
            }
        }
    }

    private int getPackageFlags(int userId, java.lang.String packageName) {
        int iIntValue;
        synchronized (this.mPackageFlags) {
            android.util.Pair<java.lang.Integer, java.lang.String> packageKey = android.util.Pair.create(java.lang.Integer.valueOf(userId), packageName);
            iIntValue = this.mPackageFlags.getOrDefault(packageKey, 0).intValue();
        }
        return iIntValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearAllPackageFlagsForUser(int userId) {
        synchronized (this.mPackageFlags) {
            boolean hasPackageFlagsForUser = false;
            for (int i = this.mPackageFlags.size() - 1; i >= 0; i--) {
                android.util.Pair<java.lang.Integer, java.lang.String> key = this.mPackageFlags.keyAt(i);
                if (((java.lang.Integer) key.first).intValue() == userId) {
                    hasPackageFlagsForUser = true;
                    this.mPackageFlags.remove(key);
                }
            }
            if (hasPackageFlagsForUser) {
                this.mWriteConfigTask.schedule();
            }
        }
    }

    private int getUserIdForActivity(com.android.server.wm.ActivityRecord ar) {
        if (!android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
            return 0;
        }
        if (ar.mUserId == 0) {
            return getUserAssignedToDisplay(ar.mDisplayContent.getDisplayId());
        }
        return this.mUserManagerInternal.getProfileParentId(ar.mUserId);
    }

    private android.content.Context getUiContextForActivity(com.android.server.wm.ActivityRecord ar) {
        if (!android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
            if (!android.os.UserManager.isHeadlessSystemUserMode()) {
                return this.mAtm.getUiContext();
            }
            android.content.Context uiContextForCurrentUser = this.mAtm.getUiContext().createContextAsUser(new android.os.UserHandle(this.mAtm.getCurrentUserId()), 0);
            return uiContextForCurrentUser;
        }
        com.android.server.wm.DisplayContent dc = ar.mDisplayContent;
        android.content.Context systemUiContext = dc.getDisplayPolicy().getSystemUiContext();
        int assignedUser = getUserAssignedToDisplay(dc.getDisplayId());
        android.content.Context uiContextForUser = systemUiContext.createContextAsUser(new android.os.UserHandle(assignedUser), 0);
        return uiContextForUser;
    }

    private int getUserAssignedToDisplay(int displayId) {
        return this.mUserManagerInternal.getUserAssignedToDisplay(displayId);
    }

    private final class UiHandler extends android.os.Handler {
        private static final int MSG_HIDE_DIALOGS_FOR_PACKAGE = 4;
        private static final int MSG_HIDE_UNSUPPORTED_DISPLAY_SIZE_DIALOG = 2;
        private static final int MSG_SHOW_DEPRECATED_ABI_DIALOG = 6;
        private static final int MSG_SHOW_DEPRECATED_TARGET_SDK_DIALOG = 5;
        private static final int MSG_SHOW_UNSUPPORTED_COMPILE_SDK_DIALOG = 3;
        private static final int MSG_SHOW_UNSUPPORTED_DISPLAY_SIZE_DIALOG = 1;

        public UiHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.wm.ActivityRecord ar = (com.android.server.wm.ActivityRecord) msg.obj;
                    com.android.server.wm.AppWarnings.this.showUnsupportedDisplaySizeDialogUiThread(ar);
                    break;
                case 2:
                    com.android.server.wm.AppWarnings.this.hideUnsupportedDisplaySizeDialogUiThread();
                    break;
                case 3:
                    com.android.server.wm.ActivityRecord ar2 = (com.android.server.wm.ActivityRecord) msg.obj;
                    com.android.server.wm.AppWarnings.this.showUnsupportedCompileSdkDialogUiThread(ar2);
                    break;
                case 4:
                    java.lang.String name = (java.lang.String) msg.obj;
                    int userId = msg.arg1;
                    com.android.server.wm.AppWarnings.this.hideDialogsForPackageUiThread(name, userId);
                    break;
                case 5:
                    com.android.server.wm.ActivityRecord ar3 = (com.android.server.wm.ActivityRecord) msg.obj;
                    com.android.server.wm.AppWarnings.this.showDeprecatedTargetSdkDialogUiThread(ar3);
                    break;
                case 6:
                    com.android.server.wm.ActivityRecord ar4 = (com.android.server.wm.ActivityRecord) msg.obj;
                    com.android.server.wm.AppWarnings.this.showDeprecatedAbiDialogUiThread(ar4);
                    break;
            }
        }

        public void showUnsupportedDisplaySizeDialog(com.android.server.wm.ActivityRecord r) {
            removeMessages(1);
            obtainMessage(1, r).sendToTarget();
        }

        public void hideUnsupportedDisplaySizeDialog() {
            removeMessages(2);
            sendEmptyMessage(2);
        }

        public void showUnsupportedCompileSdkDialog(com.android.server.wm.ActivityRecord r) {
            removeMessages(3);
            obtainMessage(3, r).sendToTarget();
        }

        public void showDeprecatedTargetDialog(com.android.server.wm.ActivityRecord r) {
            removeMessages(5);
            obtainMessage(5, r).sendToTarget();
        }

        public void showDeprecatedAbiDialog(com.android.server.wm.ActivityRecord r) {
            removeMessages(6);
            obtainMessage(6, r).sendToTarget();
        }

        public void hideDialogsForPackage(java.lang.String name, int userId) {
            obtainMessage(4, userId, 0, name).sendToTarget();
        }
    }

    static class BaseDialog {
        private android.content.BroadcastReceiver mCloseReceiver;
        android.app.AlertDialog mDialog;
        final com.android.server.wm.AppWarnings mManager;
        final java.lang.String mPackageName;
        final android.content.Context mUiContext;
        final int mUserId;

        BaseDialog(com.android.server.wm.AppWarnings manager, android.content.Context uiContext, java.lang.String packageName, int userId) {
            this.mManager = manager;
            this.mUiContext = uiContext;
            this.mPackageName = packageName;
            this.mUserId = userId;
        }

        void show() {
            if (this.mDialog == null) {
                return;
            }
            if (this.mCloseReceiver == null) {
                this.mCloseReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.wm.AppWarnings.BaseDialog.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(android.content.Context context, android.content.Intent intent) {
                        if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                            com.android.server.wm.AppWarnings.BaseDialog.this.mManager.mUiHandler.hideDialogsForPackage(com.android.server.wm.AppWarnings.BaseDialog.this.mPackageName, com.android.server.wm.AppWarnings.BaseDialog.this.mUserId);
                        }
                    }
                };
                this.mUiContext.registerReceiver(this.mCloseReceiver, new android.content.IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"), 2);
            }
            android.util.Slog.w(com.android.server.wm.AppWarnings.TAG, "Showing " + getClass().getSimpleName() + " for package " + this.mPackageName);
            this.mDialog.show();
        }

        void dismiss() {
            if (this.mDialog == null) {
                return;
            }
            if (this.mCloseReceiver != null) {
                this.mUiContext.unregisterReceiver(this.mCloseReceiver);
                this.mCloseReceiver = null;
            }
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    private final class WriteConfigTask implements java.lang.Runnable {
        private static final long WRITE_CONFIG_DELAY_MS = 10000;
        final java.util.concurrent.atomic.AtomicReference<android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, java.lang.Integer>> mPendingPackageFlags;

        private WriteConfigTask() {
            this.mPendingPackageFlags = new java.util.concurrent.atomic.AtomicReference<>();
        }

        @Override // java.lang.Runnable
        public void run() {
            android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, java.lang.Integer> packageFlags = this.mPendingPackageFlags.getAndSet(null);
            if (packageFlags != null) {
                com.android.server.wm.AppWarnings.this.writeConfigToFile(packageFlags);
            }
        }

        void schedule() {
            if (this.mPendingPackageFlags.getAndSet(new android.util.ArrayMap<>(com.android.server.wm.AppWarnings.this.mPackageFlags)) == null) {
                com.android.server.IoThread.getHandler().postDelayed(this, 10000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeConfigToFile(android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, java.lang.Integer> packageFlags) {
        java.io.FileOutputStream fos = null;
        try {
            fos = this.mConfigFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
            out.startDocument((java.lang.String) null, true);
            out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            out.startTag((java.lang.String) null, "packages");
            for (int i = 0; i < packageFlags.size(); i++) {
                android.util.Pair<java.lang.Integer, java.lang.String> key = packageFlags.keyAt(i);
                int userId = ((java.lang.Integer) key.first).intValue();
                java.lang.String packageName = (java.lang.String) key.second;
                int mode = packageFlags.valueAt(i).intValue();
                if (mode != 0) {
                    out.startTag((java.lang.String) null, "package");
                    out.attributeInt((java.lang.String) null, "user", userId);
                    out.attribute((java.lang.String) null, "name", packageName);
                    out.attributeInt((java.lang.String) null, "flags", mode);
                    out.endTag((java.lang.String) null, "package");
                }
            }
            out.endTag((java.lang.String) null, "packages");
            out.endDocument();
            this.mConfigFile.finishWrite(fos);
        } catch (java.io.IOException e1) {
            android.util.Slog.w(TAG, "Error writing package metadata", e1);
            if (fos != null) {
                this.mConfigFile.failWrite(fos);
            }
        }
    }

    private void readConfigFromFileAmsThread() {
        com.android.modules.utils.TypedXmlPullParser parser;
        int eventType;
        int i;
        int eventType2;
        java.io.FileInputStream fis = null;
        try {
            try {
                try {
                    fis = this.mConfigFile.openRead();
                    parser = android.util.Xml.resolvePullParser(fis);
                    int eventType3 = parser.getEventType();
                    while (true) {
                        eventType = eventType3;
                        i = 2;
                        if (eventType == 2 || eventType == 1) {
                            break;
                        } else {
                            eventType3 = parser.next();
                        }
                    }
                } finally {
                }
            } catch (java.io.IOException e) {
                if (fis != null) {
                    android.util.Slog.w(TAG, "Error reading package metadata", e);
                }
                if (fis == null) {
                    return;
                } else {
                    fis.close();
                }
            } catch (org.xmlpull.v1.XmlPullParserException e2) {
                android.util.Slog.w(TAG, "Error reading package metadata", e2);
                if (fis == null) {
                    return;
                } else {
                    fis.close();
                }
            }
            if (eventType == 1) {
                if (fis != null) {
                    try {
                        fis.close();
                        return;
                    } catch (java.io.IOException e3) {
                        return;
                    }
                }
                return;
            }
            java.lang.String tagName = parser.getName();
            if ("packages".equals(tagName)) {
                int eventType4 = parser.next();
                boolean writeConfigToFileNeeded = false;
                while (true) {
                    if (eventType4 == i) {
                        java.lang.String tagName2 = parser.getName();
                        if (parser.getDepth() == i && "package".equals(tagName2)) {
                            int userId = parser.getAttributeInt((java.lang.String) null, "user", -10000);
                            java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
                            if (name != null) {
                                int i2 = 0;
                                int flagsInt = parser.getAttributeInt((java.lang.String) null, "flags", 0);
                                if (userId != -10000) {
                                    android.util.Pair<java.lang.Integer, java.lang.String> packageKey = android.util.Pair.create(java.lang.Integer.valueOf(userId), name);
                                    this.mPackageFlags.put(packageKey, java.lang.Integer.valueOf(flagsInt));
                                } else {
                                    writeConfigToFileNeeded = true;
                                    if (android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
                                        android.content.pm.UserInfo[] users = this.mUserManagerInternal.getUserInfos();
                                        int length = users.length;
                                        while (i2 < length) {
                                            android.content.pm.UserInfo userInfo = users[i2];
                                            if (userInfo.isFull()) {
                                                android.util.Pair<java.lang.Integer, java.lang.String> packageKey2 = android.util.Pair.create(java.lang.Integer.valueOf(userInfo.id), name);
                                                eventType2 = eventType4;
                                                this.mPackageFlags.put(packageKey2, java.lang.Integer.valueOf(flagsInt));
                                            } else {
                                                eventType2 = eventType4;
                                            }
                                            i2++;
                                            eventType4 = eventType2;
                                        }
                                    } else {
                                        android.util.Pair<java.lang.Integer, java.lang.String> packageKey3 = android.util.Pair.create(0, name);
                                        this.mPackageFlags.put(packageKey3, java.lang.Integer.valueOf(flagsInt));
                                    }
                                }
                            }
                        }
                    }
                    eventType4 = parser.next();
                    if (eventType4 == 1) {
                        break;
                    } else {
                        i = 2;
                    }
                }
                if (writeConfigToFileNeeded) {
                    this.mWriteConfigTask.schedule();
                }
            }
            if (fis != null) {
                fis.close();
            }
        } catch (java.io.IOException e4) {
        }
    }
}
