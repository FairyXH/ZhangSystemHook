package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public class AppOpsCheckingServiceImpl implements com.android.server.appop.AppOpsCheckingServiceInterface {
    static final int CURRENT_VERSION = 4;
    private static final boolean DEBUG = false;
    private static final int NO_FILE_VERSION = -2;
    private static final int NO_VERSION = -1;
    static final java.lang.String TAG = "LegacyAppOpsServiceInterfaceImpl";
    private static final long WRITE_DELAY = 1800000;
    final android.content.Context mContext;
    boolean mFastWriteScheduled;
    final android.util.AtomicFile mFile;
    final android.os.Handler mHandler;
    final java.lang.Object mLock;
    final android.util.SparseArray<int[]> mSwitchedOps;
    boolean mWriteScheduled;
    private int mVersionAtBoot = -2;
    final android.util.SparseArray<android.util.SparseIntArray> mUidModes = new android.util.SparseArray<>();
    final android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> mUserPackageModes = new android.util.SparseArray<>();
    private final com.android.server.appop.LegacyAppOpStateParser mAppOpsStateParser = new com.android.server.appop.LegacyAppOpStateParser();
    private java.util.List<com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener> mModeChangedListeners = new java.util.ArrayList();
    final java.lang.Runnable mWriteRunner = new java.lang.Runnable() { // from class: com.android.server.appop.AppOpsCheckingServiceImpl.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.appop.AppOpsCheckingServiceImpl.this.mLock) {
                com.android.server.appop.AppOpsCheckingServiceImpl.this.mWriteScheduled = false;
                com.android.server.appop.AppOpsCheckingServiceImpl.this.mFastWriteScheduled = false;
                android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Void> task = new android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Void>() { // from class: com.android.server.appop.AppOpsCheckingServiceImpl.1.1
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // android.os.AsyncTask
                    public java.lang.Void doInBackground(java.lang.Void... params) {
                        com.android.server.appop.AppOpsCheckingServiceImpl.this.writeState();
                        return null;
                    }
                };
                task.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR, null);
            }
        }
    };

    AppOpsCheckingServiceImpl(java.io.File storageFile, java.lang.Object lock, android.os.Handler handler, android.content.Context context, android.util.SparseArray<int[]> switchedOps) {
        this.mFile = new android.util.AtomicFile(storageFile);
        this.mLock = lock;
        this.mHandler = handler;
        this.mContext = context;
        this.mSwitchedOps = switchedOps;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void systemReady() {
        synchronized (this.mLock) {
            upgradeLocked(this.mVersionAtBoot);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultUidModes(int uid, java.lang.String persistentDeviceId) {
        synchronized (this.mLock) {
            android.util.SparseIntArray opModes = this.mUidModes.get(uid, null);
            if (opModes == null) {
                return new android.util.SparseIntArray();
            }
            return opModes.clone();
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultPackageModes(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes = this.mUserPackageModes.get(userId);
            if (packageModes == null) {
                return new android.util.SparseIntArray();
            }
            android.util.SparseIntArray opModes = packageModes.get(packageName);
            if (opModes == null) {
                return new android.util.SparseIntArray();
            }
            return opModes.clone();
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getUidMode(int uid, java.lang.String persistentDeviceId, int op) {
        synchronized (this.mLock) {
            android.util.SparseIntArray opModes = this.mUidModes.get(uid, null);
            if (opModes == null) {
                return android.app.AppOpsManager.opToDefaultMode(op);
            }
            return opModes.get(op, android.app.AppOpsManager.opToDefaultMode(op));
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean setUidMode(int uid, java.lang.String persistentDeviceId, int op, int mode) {
        int defaultMode = android.app.AppOpsManager.opToDefaultMode(op);
        synchronized (this.mLock) {
            android.util.SparseIntArray opModes = this.mUidModes.get(uid, null);
            int previousMode = defaultMode;
            if (opModes != null) {
                previousMode = opModes.get(op, defaultMode);
            }
            if (mode == previousMode) {
                return false;
            }
            if (mode == defaultMode) {
                opModes.delete(op);
                if (opModes.size() == 0) {
                    this.mUidModes.remove(uid);
                }
            } else {
                if (opModes == null) {
                    opModes = new android.util.SparseIntArray();
                    this.mUidModes.put(uid, opModes);
                }
                opModes.put(op, mode);
            }
            scheduleWriteLocked();
            java.util.List<com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener> listenersCopy = new java.util.ArrayList<>(this.mModeChangedListeners);
            for (int i = 0; i < listenersCopy.size(); i++) {
                listenersCopy.get(i).onUidModeChanged(uid, op, mode, persistentDeviceId);
            }
            return true;
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getPackageMode(java.lang.String packageName, int op, int userId) {
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes = this.mUserPackageModes.get(userId, null);
            if (packageModes == null) {
                return android.app.AppOpsManager.opToDefaultMode(op);
            }
            android.util.SparseIntArray opModes = packageModes.getOrDefault(packageName, null);
            if (opModes == null) {
                return android.app.AppOpsManager.opToDefaultMode(op);
            }
            return opModes.get(op, android.app.AppOpsManager.opToDefaultMode(op));
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void setPackageMode(java.lang.String packageName, int op, int mode, int userId) {
        int defaultMode = android.app.AppOpsManager.opToDefaultMode(op);
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes = this.mUserPackageModes.get(userId, null);
            if (packageModes == null && mode != defaultMode) {
                packageModes = new android.util.ArrayMap<>();
                this.mUserPackageModes.put(userId, packageModes);
            }
            android.util.SparseIntArray opModes = null;
            int previousMode = defaultMode;
            if (packageModes != null && (opModes = packageModes.get(packageName)) != null) {
                previousMode = opModes.get(op, defaultMode);
            }
            if (mode == previousMode) {
                return;
            }
            if (mode == defaultMode) {
                opModes.delete(op);
                if (opModes.size() == 0) {
                    packageModes.remove(packageName);
                    if (packageModes.size() == 0) {
                        this.mUserPackageModes.remove(userId);
                    }
                }
            } else {
                if (packageModes == null) {
                    packageModes = new android.util.ArrayMap<>();
                    this.mUserPackageModes.put(userId, packageModes);
                }
                if (opModes == null) {
                    opModes = new android.util.SparseIntArray();
                    packageModes.put(packageName, opModes);
                }
                opModes.put(op, mode);
            }
            scheduleFastWriteLocked();
            java.util.List<com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener> listenersCopy = new java.util.ArrayList<>(this.mModeChangedListeners);
            for (int i = 0; i < listenersCopy.size(); i++) {
                listenersCopy.get(i).onPackageModeChanged(packageName, userId, op, mode);
            }
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void removeUid(int uid) {
        synchronized (this.mLock) {
            android.util.SparseIntArray opModes = this.mUidModes.get(uid);
            if (opModes == null) {
                return;
            }
            this.mUidModes.remove(uid);
            scheduleFastWriteLocked();
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removePackage(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes = this.mUserPackageModes.get(userId, null);
            if (packageModes == null) {
                return false;
            }
            android.util.SparseIntArray ops = packageModes.remove(packageName);
            if (ops == null) {
                return false;
            }
            scheduleFastWriteLocked();
            return true;
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void clearAllModes() {
        synchronized (this.mLock) {
            this.mUidModes.clear();
            this.mUserPackageModes.clear();
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(int uid, java.lang.String persistentDeviceId) {
        android.util.SparseBooleanArray result = new android.util.SparseBooleanArray();
        synchronized (this.mLock) {
            android.util.SparseIntArray modes = this.mUidModes.get(uid);
            if (modes == null) {
                return result;
            }
            for (int i = 0; i < modes.size(); i++) {
                if (modes.valueAt(i) == 4) {
                    result.put(modes.keyAt(i), true);
                }
            }
            return result;
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(java.lang.String packageName, int userId) {
        android.util.SparseBooleanArray result = new android.util.SparseBooleanArray();
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes = this.mUserPackageModes.get(userId);
            if (packageModes == null) {
                return result;
            }
            android.util.SparseIntArray modes = packageModes.get(packageName);
            if (modes == null) {
                return result;
            }
            for (int i = 0; i < modes.size(); i++) {
                if (modes.valueAt(i) == 4) {
                    result.put(modes.keyAt(i), true);
                }
            }
            return result;
        }
    }

    private void scheduleWriteLocked() {
        if (!this.mWriteScheduled) {
            this.mWriteScheduled = true;
            this.mHandler.postDelayed(this.mWriteRunner, 1800000L);
        }
    }

    private void scheduleFastWriteLocked() {
        if (!this.mFastWriteScheduled) {
            this.mWriteScheduled = true;
            this.mFastWriteScheduled = true;
            this.mHandler.removeCallbacks(this.mWriteRunner);
            this.mHandler.postDelayed(this.mWriteRunner, 10000L);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void writeState() {
        com.android.modules.utils.TypedXmlSerializer out;
        android.util.SparseArray<android.util.SparseIntArray> uidModesCopy;
        android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> userPackageModesCopy;
        synchronized (this.mFile) {
            try {
                try {
                    java.io.FileOutputStream stream = this.mFile.startWrite();
                    try {
                        out = android.util.Xml.resolveSerializer(stream);
                        out.startDocument((java.lang.String) null, true);
                        out.startTag((java.lang.String) null, "app-ops");
                        out.attributeInt((java.lang.String) null, "v", 4);
                        uidModesCopy = new android.util.SparseArray<>();
                        userPackageModesCopy = new android.util.SparseArray<>();
                    } catch (java.io.IOException e) {
                        android.util.Slog.w(TAG, "Failed to write state, restoring backup.", e);
                        this.mFile.failWrite(stream);
                    }
                    synchronized (this.mLock) {
                        try {
                            int uidModesSize = this.mUidModes.size();
                            for (int uidIdx = 0; uidIdx < uidModesSize; uidIdx++) {
                                try {
                                    int uid = this.mUidModes.keyAt(uidIdx);
                                    uidModesCopy.put(uid, this.mUidModes.valueAt(uidIdx).clone());
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    while (true) {
                                        try {
                                            throw th;
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                        }
                                    }
                                }
                            }
                            int usersSize = this.mUserPackageModes.size();
                            for (int userIdx = 0; userIdx < usersSize; userIdx++) {
                                int user = this.mUserPackageModes.keyAt(userIdx);
                                android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes = this.mUserPackageModes.valueAt(userIdx);
                                android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModesCopy = new android.util.ArrayMap<>();
                                userPackageModesCopy.put(user, packageModesCopy);
                                int pkgIdx = 0;
                                int packageModesSize = packageModes.size();
                                while (pkgIdx < packageModesSize) {
                                    java.lang.String pkg = packageModes.keyAt(pkgIdx);
                                    int user2 = user;
                                    packageModesCopy.put(pkg, packageModes.valueAt(pkgIdx).clone());
                                    pkgIdx++;
                                    user = user2;
                                }
                            }
                            int uidStateNum = 0;
                            while (uidStateNum < uidModesSize) {
                                int uid2 = uidModesCopy.keyAt(uidStateNum);
                                android.util.SparseIntArray modes = uidModesCopy.valueAt(uidStateNum);
                                out.startTag((java.lang.String) null, "uid");
                                out.attributeInt((java.lang.String) null, "n", uid2);
                                int modesSize = modes.size();
                                int modeIdx = 0;
                                while (modeIdx < modesSize) {
                                    int op = modes.keyAt(modeIdx);
                                    int mode = modes.valueAt(modeIdx);
                                    out.startTag((java.lang.String) null, "op");
                                    out.attributeInt((java.lang.String) null, "n", op);
                                    out.attributeInt((java.lang.String) null, "m", mode);
                                    out.endTag((java.lang.String) null, "op");
                                    modeIdx++;
                                    uidModesSize = uidModesSize;
                                }
                                out.endTag((java.lang.String) null, "uid");
                                uidStateNum++;
                                uidModesSize = uidModesSize;
                            }
                            int userIdx2 = 0;
                            while (userIdx2 < usersSize) {
                                int userId = userPackageModesCopy.keyAt(userIdx2);
                                android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes2 = userPackageModesCopy.valueAt(userIdx2);
                                out.startTag((java.lang.String) null, "user");
                                out.attributeInt((java.lang.String) null, "n", userId);
                                int packageModesSize2 = packageModes2.size();
                                int pkgIdx2 = 0;
                                while (pkgIdx2 < packageModesSize2) {
                                    java.lang.String pkg2 = packageModes2.keyAt(pkgIdx2);
                                    android.util.SparseIntArray modes2 = packageModes2.valueAt(pkgIdx2);
                                    out.startTag((java.lang.String) null, "pkg");
                                    out.attribute((java.lang.String) null, "n", pkg2);
                                    int modesSize2 = modes2.size();
                                    int modeIdx2 = 0;
                                    while (modeIdx2 < modesSize2) {
                                        int op2 = modes2.keyAt(modeIdx2);
                                        int mode2 = modes2.valueAt(modeIdx2);
                                        out.startTag((java.lang.String) null, "op");
                                        out.attributeInt((java.lang.String) null, "n", op2);
                                        out.attributeInt((java.lang.String) null, "m", mode2);
                                        out.endTag((java.lang.String) null, "op");
                                        modeIdx2++;
                                        userId = userId;
                                        uidModesCopy = uidModesCopy;
                                        userPackageModesCopy = userPackageModesCopy;
                                    }
                                    out.endTag((java.lang.String) null, "pkg");
                                    pkgIdx2++;
                                    userId = userId;
                                    uidModesCopy = uidModesCopy;
                                    userPackageModesCopy = userPackageModesCopy;
                                }
                                out.endTag((java.lang.String) null, "user");
                                userIdx2++;
                                uidModesCopy = uidModesCopy;
                                userPackageModesCopy = userPackageModesCopy;
                            }
                            out.endTag((java.lang.String) null, "app-ops");
                            out.endDocument();
                            this.mFile.finishWrite(stream);
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    }
                } catch (java.io.IOException e2) {
                    android.util.Slog.w(TAG, "Failed to write state: " + e2);
                }
            } catch (java.lang.Throwable th4) {
                throw th4;
            }
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void readState() {
        synchronized (this.mFile) {
            synchronized (this.mLock) {
                this.mVersionAtBoot = this.mAppOpsStateParser.readState(this.mFile, this.mUidModes, this.mUserPackageModes);
            }
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void shutdown() {
        boolean doWrite = false;
        synchronized (this) {
            if (this.mWriteScheduled) {
                this.mWriteScheduled = false;
                this.mFastWriteScheduled = false;
                this.mHandler.removeCallbacks(this.mWriteRunner);
                doWrite = true;
            }
        }
        if (doWrite) {
            writeState();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void upgradeLocked(int oldVersion) {
        if (oldVersion == -2 || oldVersion >= 4) {
            return;
        }
        android.util.Slog.d(TAG, "Upgrading app-ops xml from version " + oldVersion + " to 4");
        switch (oldVersion) {
            case -1:
                upgradeRunAnyInBackgroundLocked();
                upgradeScheduleExactAlarmLocked();
                resetUseFullScreenIntentLocked();
                break;
            case 1:
                upgradeScheduleExactAlarmLocked();
                resetUseFullScreenIntentLocked();
                break;
            case 2:
            case 3:
                resetUseFullScreenIntentLocked();
                break;
        }
        scheduleFastWriteLocked();
    }

    void upgradeRunAnyInBackgroundLocked() {
        int uidModesSize = this.mUidModes.size();
        for (int uidIdx = 0; uidIdx < uidModesSize; uidIdx++) {
            android.util.SparseIntArray modesForUid = this.mUidModes.valueAt(uidIdx);
            int idx = modesForUid.indexOfKey(63);
            if (idx >= 0) {
                modesForUid.put(70, modesForUid.valueAt(idx));
            }
        }
        int usersSize = this.mUserPackageModes.size();
        for (int userIdx = 0; userIdx < usersSize; userIdx++) {
            android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes = this.mUserPackageModes.valueAt(userIdx);
            int packageModesSize = packageModes.size();
            for (int pkgIdx = 0; pkgIdx < packageModesSize; pkgIdx++) {
                android.util.SparseIntArray modes = packageModes.valueAt(pkgIdx);
                int idx2 = modes.indexOfKey(63);
                if (idx2 >= 0) {
                    modes.put(70, modes.valueAt(idx2));
                }
            }
        }
    }

    void upgradeScheduleExactAlarmLocked() {
        com.android.server.pm.permission.PermissionManagerServiceInternal pmsi = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        java.lang.String[] packagesDeclaringPermission = pmsi.getAppOpPermissionPackages(android.app.AppOpsManager.opToPermission(107));
        int[] userIds = umi.getUserIds();
        int length = packagesDeclaringPermission.length;
        int i = 0;
        while (i < length) {
            java.lang.String pkg = packagesDeclaringPermission[i];
            int length2 = userIds.length;
            int i2 = 0;
            while (i2 < length2) {
                int userId = userIds[i2];
                int uid = pmi.getPackageUid(pkg, 0L, userId);
                int oldMode = getUidMode(uid, "default:0", 107);
                com.android.server.pm.permission.PermissionManagerServiceInternal pmsi2 = pmsi;
                if (oldMode == android.app.AppOpsManager.opToDefaultMode(107)) {
                    setUidMode(uid, "default:0", 107, 0);
                }
                i2++;
                pmsi = pmsi2;
            }
            i++;
            pmsi = pmsi;
        }
    }

    void resetUseFullScreenIntentLocked() {
        com.android.server.pm.permission.PermissionManagerServiceInternal pmsi;
        com.android.server.pm.UserManagerInternal umi;
        com.android.server.pm.permission.PermissionManagerServiceInternal pmsi2 = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
        com.android.server.pm.UserManagerInternal umi2 = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        android.permission.PermissionManager permissionManager = (android.permission.PermissionManager) this.mContext.getSystemService(android.permission.PermissionManager.class);
        java.lang.String permissionName = android.app.AppOpsManager.opToPermission(133);
        java.lang.String[] packagesDeclaringPermission = pmsi2.getAppOpPermissionPackages(permissionName);
        int[] userIds = umi2.getUserIds();
        int length = packagesDeclaringPermission.length;
        int i = 0;
        while (i < length) {
            java.lang.String pkg = packagesDeclaringPermission[i];
            int length2 = userIds.length;
            int i2 = 0;
            while (i2 < length2) {
                int userId = userIds[i2];
                int i3 = i;
                int uid = pmi.getPackageUid(pkg, 0L, userId);
                int flags = permissionManager.getPermissionFlags(pkg, permissionName, android.os.UserHandle.of(userId));
                if ((flags & 1) != 0) {
                    pmsi = pmsi2;
                    umi = umi2;
                } else {
                    pmsi = pmsi2;
                    umi = umi2;
                    setUidMode(uid, "default:0", 133, android.app.AppOpsManager.opToDefaultMode(133));
                }
                i2++;
                i = i3;
                pmsi2 = pmsi;
                umi2 = umi;
            }
            i++;
        }
    }

    java.util.List<java.lang.Integer> getUidsWithNonDefaultModes() {
        java.util.List<java.lang.Integer> result = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mUidModes.size(); i++) {
                android.util.SparseIntArray modes = this.mUidModes.valueAt(i);
                if (modes.size() > 0) {
                    result.add(java.lang.Integer.valueOf(this.mUidModes.keyAt(i)));
                }
            }
        }
        return result;
    }

    java.util.List<android.content.pm.UserPackage> getPackagesWithNonDefaultModes() {
        java.util.List<android.content.pm.UserPackage> result = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mUserPackageModes.size(); i++) {
                android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> packageModes = this.mUserPackageModes.valueAt(i);
                for (int j = 0; j < packageModes.size(); j++) {
                    android.util.SparseIntArray modes = packageModes.valueAt(j);
                    if (modes.size() > 0) {
                        result.add(android.content.pm.UserPackage.of(this.mUserPackageModes.keyAt(i), packageModes.keyAt(j)));
                    }
                }
            }
        }
        return result;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean addAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        boolean zAdd;
        synchronized (this.mLock) {
            zAdd = this.mModeChangedListeners.add(listener);
        }
        return zAdd;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removeAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        boolean zRemove;
        synchronized (this.mLock) {
            zRemove = this.mModeChangedListeners.remove(listener);
        }
        return zRemove;
    }
}
