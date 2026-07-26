package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class BackgroundInstallControlService extends com.android.server.SystemService {
    private static final java.lang.String DISK_DIR_NAME = "bic";
    private static final java.lang.String DISK_FILE_NAME = "states";
    private static final java.lang.String ENFORCE_PERMISSION_ERROR_MSG = "User is not permitted to call service: ";
    private static final int MAX_FOREGROUND_TIME_FRAMES_SIZE = 10;
    private static final int MSG_PACKAGE_ADDED = 1;
    private static final int MSG_PACKAGE_REMOVED = 2;
    private static final int MSG_USAGE_EVENT_RECEIVED = 0;
    private static final java.lang.String TAG = "BackgroundInstallControlService";
    private android.util.SparseSetArray<java.lang.String> mBackgroundInstalledPackages;
    private final com.android.server.pm.BackgroundInstallControlService.BinderService mBinderService;
    private final com.android.server.pm.BackgroundInstallControlCallbackHelper mCallbackHelper;
    private final android.content.Context mContext;
    private final java.io.File mDiskFile;
    private final android.os.Handler mHandler;
    private final android.util.SparseArrayMap<java.lang.String, java.util.TreeSet<com.android.server.pm.BackgroundInstallControlService.ForegroundTimeFrame>> mInstallerForegroundTimeFrames;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    protected final android.content.pm.PackageManagerInternal.PackageListObserver mPackageObserver;
    private final com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManager;

    interface Injector {
        com.android.server.pm.BackgroundInstallControlCallbackHelper getBackgroundInstallControlCallbackHelper();

        android.content.Context getContext();

        java.io.File getDiskFile();

        android.os.Looper getLooper();

        android.content.pm.PackageManager getPackageManager();

        android.content.pm.PackageManagerInternal getPackageManagerInternal();

        com.android.server.pm.permission.PermissionManagerServiceInternal getPermissionManager();

        android.app.usage.UsageStatsManagerInternal getUsageStatsManagerInternal();
    }

    public BackgroundInstallControlService(android.content.Context context) {
        this(new com.android.server.pm.BackgroundInstallControlService.InjectorImpl(context));
    }

    BackgroundInstallControlService(com.android.server.pm.BackgroundInstallControlService.Injector injector) {
        super(injector.getContext());
        this.mBackgroundInstalledPackages = null;
        this.mInstallerForegroundTimeFrames = new android.util.SparseArrayMap<>();
        this.mPackageObserver = new android.content.pm.PackageManagerInternal.PackageListObserver() { // from class: com.android.server.pm.BackgroundInstallControlService.1
            @Override // android.content.pm.PackageManagerInternal.PackageListObserver
            public void onPackageAdded(java.lang.String packageName, int uid) {
                int userId = android.os.UserHandle.getUserId(uid);
                com.android.server.pm.BackgroundInstallControlService.this.mHandler.obtainMessage(1, userId, 0, packageName).sendToTarget();
            }

            @Override // android.content.pm.PackageManagerInternal.PackageListObserver
            public void onPackageRemoved(java.lang.String packageName, int uid) {
                int userId = android.os.UserHandle.getUserId(uid);
                com.android.server.pm.BackgroundInstallControlService.this.mHandler.obtainMessage(2, userId, 0, packageName).sendToTarget();
            }
        };
        this.mPackageManager = injector.getPackageManager();
        this.mPackageManagerInternal = injector.getPackageManagerInternal();
        this.mPermissionManager = injector.getPermissionManager();
        this.mHandler = new com.android.server.pm.BackgroundInstallControlService.EventHandler(injector.getLooper(), this);
        this.mDiskFile = injector.getDiskFile();
        this.mContext = injector.getContext();
        this.mCallbackHelper = injector.getBackgroundInstallControlCallbackHelper();
        android.app.usage.UsageStatsManagerInternal usageStatsManagerInternal = injector.getUsageStatsManagerInternal();
        usageStatsManagerInternal.registerListener(new android.app.usage.UsageStatsManagerInternal.UsageEventListener() { // from class: com.android.server.pm.BackgroundInstallControlService$$ExternalSyntheticLambda2
            @Override // android.app.usage.UsageStatsManagerInternal.UsageEventListener
            public final void onUsageEvent(int i, android.app.usage.UsageEvents.Event event) {
                this.f$0.lambda$new$0(i, event);
            }
        });
        this.mBinderService = new com.android.server.pm.BackgroundInstallControlService.BinderService(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int userId, android.app.usage.UsageEvents.Event event) {
        this.mHandler.obtainMessage(0, userId, 0, event).sendToTarget();
    }

    private static final class BinderService extends android.content.pm.IBackgroundInstallControlService.Stub {
        final com.android.server.pm.BackgroundInstallControlService mService;

        BinderService(com.android.server.pm.BackgroundInstallControlService service) {
            this.mService = service;
        }

        public android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getBackgroundInstalledPackages(long flags, int userId) {
            if (android.app.Flags.bicClient()) {
                this.mService.enforceCallerPermissions();
            }
            if (!android.os.Build.IS_DEBUGGABLE) {
                return this.mService.getBackgroundInstalledPackages(flags, userId);
            }
            java.lang.String propertyString = android.os.SystemProperties.get("debug.transparency.bg-install-apps");
            if (android.text.TextUtils.isEmpty(propertyString)) {
                return this.mService.getBackgroundInstalledPackages(flags, userId);
            }
            return this.mService.getMockBackgroundInstalledPackages(propertyString);
        }

        public void registerBackgroundInstallCallback(android.os.IRemoteCallback callback) {
            this.mService.mCallbackHelper.registerBackgroundInstallCallback(callback);
        }

        public void unregisterBackgroundInstallCallback(android.os.IRemoteCallback callback) {
            this.mService.mCallbackHelper.unregisterBackgroundInstallCallback(callback);
        }
    }

    void enforceCallerPermissions() throws java.lang.SecurityException {
        this.mContext.enforceCallingOrSelfPermission("android.permission.GET_BACKGROUND_INSTALLED_PACKAGES", "User is not permitted to call service: android.permission.GET_BACKGROUND_INSTALLED_PACKAGES");
    }

    android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getBackgroundInstalledPackages(long flags, int userId) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            java.util.List<android.content.pm.PackageInfo> packages = this.mPackageManager.getInstalledPackagesAsUser(android.content.pm.PackageManager.PackageInfoFlags.of(flags), userId);
            initBackgroundInstalledPackages();
            java.util.ListIterator<android.content.pm.PackageInfo> iter = packages.listIterator();
            while (iter.hasNext()) {
                java.lang.String packageName = iter.next().packageName;
                if (!this.mBackgroundInstalledPackages.contains(userId, packageName)) {
                    iter.remove();
                }
            }
            return new android.content.pm.ParceledListSlice<>(packages);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getMockBackgroundInstalledPackages(java.lang.String propertyString) {
        java.lang.String[] mockPackageNames = propertyString.split(",");
        java.util.List<android.content.pm.PackageInfo> mockPackages = new java.util.ArrayList<>();
        for (java.lang.String name : mockPackageNames) {
            try {
                android.content.pm.PackageInfo packageInfo = this.mPackageManager.getPackageInfo(name, android.content.pm.PackageManager.PackageInfoFlags.of(131072L));
                mockPackages.add(packageInfo);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.w(TAG, "Package's PackageInfo not found " + name);
            }
        }
        return new android.content.pm.ParceledListSlice<>(mockPackages);
    }

    private static class EventHandler extends android.os.Handler {
        private final com.android.server.pm.BackgroundInstallControlService mService;

        EventHandler(android.os.Looper looper, com.android.server.pm.BackgroundInstallControlService service) {
            super(looper);
            this.mService = service;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    this.mService.handleUsageEvent((android.app.usage.UsageEvents.Event) msg.obj, msg.arg1);
                    break;
                case 1:
                    this.mService.handlePackageAdd((java.lang.String) msg.obj, msg.arg1);
                    break;
                case 2:
                    this.mService.handlePackageRemove((java.lang.String) msg.obj, msg.arg1);
                    break;
                default:
                    android.util.Slog.w(com.android.server.pm.BackgroundInstallControlService.TAG, "Unknown message: " + msg.what);
                    break;
            }
        }
    }

    void handlePackageAdd(java.lang.String packageName, int userId) {
        try {
            android.content.pm.ApplicationInfo appInfo = this.mPackageManager.getApplicationInfoAsUser(packageName, android.content.pm.PackageManager.ApplicationInfoFlags.of(0L), userId);
            try {
                android.content.pm.InstallSourceInfo installInfo = this.mPackageManager.getInstallSourceInfo(packageName);
                java.lang.String installerPackageName = installInfo.getInstallingPackageName();
                java.lang.String initiatingPackageName = installInfo.getInitiatingPackageName();
                if (this.mPermissionManager.checkPermission(installerPackageName, "android.permission.INSTALL_PACKAGES", "default:0", userId) != 0) {
                    return;
                }
                long installTimestamp = java.lang.System.currentTimeMillis() - (android.os.SystemClock.uptimeMillis() - retrieveInstallStartTimestamp(packageName, userId, appInfo));
                if (installedByAdb(initiatingPackageName) || wasForegroundInstallation(installerPackageName, userId, installTimestamp)) {
                    return;
                }
                initBackgroundInstalledPackages();
                this.mBackgroundInstalledPackages.add(userId, packageName);
                this.mCallbackHelper.notifyAllCallbacks(userId, packageName);
                writeBackgroundInstalledPackagesToDisk();
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.w(TAG, "Package's installer not found " + packageName);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            android.util.Slog.w(TAG, "Package's appInfo not found " + packageName);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private long retrieveInstallStartTimestamp(java.lang.String str, int i, android.content.pm.ApplicationInfo applicationInfo) {
        java.lang.String str2 = TAG;
        long j = applicationInfo.createTimestamp;
        try {
            java.util.Optional<android.content.pm.PackageInstaller.SessionInfo> latestInstallSession = getLatestInstallSession(str, i);
            if (latestInstallSession.isEmpty()) {
                android.util.Slog.w(TAG, "Package's historical install session not found, falling back to appInfo.createTimestamp: " + str);
                str2 = str2;
                j = j;
            } else {
                long createdMillis = latestInstallSession.get().getCreatedMillis();
                j = createdMillis;
                str2 = createdMillis;
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w(str2, "Retrieval of install time from historical session failed, falling back to appInfo.createTimestamp");
            android.util.Slog.w(str2, android.util.Log.getStackTraceString(e));
        }
        return j;
    }

    private java.util.Optional<android.content.pm.PackageInstaller.SessionInfo> getLatestInstallSession(final java.lang.String packageName, int userId) {
        java.util.List<android.content.pm.PackageInstaller.SessionInfo> historicalSessions = this.mPackageManagerInternal.getHistoricalSessions(userId).getList();
        return historicalSessions.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.pm.BackgroundInstallControlService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return packageName.equals(((android.content.pm.PackageInstaller.SessionInfo) obj).getAppPackageName());
            }
        }).max(java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.pm.BackgroundInstallControlService$$ExternalSyntheticLambda1
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(java.lang.Object obj) {
                return ((android.content.pm.PackageInstaller.SessionInfo) obj).getCreatedMillis();
            }
        }));
    }

    private boolean installedByAdb(java.lang.String initiatingPackageName) {
        return com.android.server.pm.PackageManagerServiceUtils.isInstalledByAdb(initiatingPackageName);
    }

    private boolean wasForegroundInstallation(java.lang.String installerPackageName, int userId, long installTimestamp) {
        java.util.TreeSet<com.android.server.pm.BackgroundInstallControlService.ForegroundTimeFrame> foregroundTimeFrames = (java.util.TreeSet) this.mInstallerForegroundTimeFrames.get(userId, installerPackageName);
        if (foregroundTimeFrames == null) {
            return false;
        }
        for (com.android.server.pm.BackgroundInstallControlService.ForegroundTimeFrame foregroundTimeFrame : foregroundTimeFrames) {
            if (foregroundTimeFrame.startTimeStampMillis <= installTimestamp && (!foregroundTimeFrame.isDone() || installTimestamp <= foregroundTimeFrame.endTimeStampMillis)) {
                return true;
            }
        }
        return false;
    }

    void handlePackageRemove(java.lang.String packageName, int userId) {
        initBackgroundInstalledPackages();
        this.mBackgroundInstalledPackages.remove(userId, packageName);
        writeBackgroundInstalledPackagesToDisk();
    }

    void handleUsageEvent(android.app.usage.UsageEvents.Event event, int userId) {
        if ((event.mEventType != 1 && event.mEventType != 2 && event.mEventType != 23) || !isInstaller(event.mPackage, userId)) {
            return;
        }
        if (!this.mInstallerForegroundTimeFrames.contains(userId, event.mPackage)) {
            this.mInstallerForegroundTimeFrames.add(userId, event.mPackage, new java.util.TreeSet());
        }
        java.util.TreeSet<com.android.server.pm.BackgroundInstallControlService.ForegroundTimeFrame> foregroundTimeFrames = (java.util.TreeSet) this.mInstallerForegroundTimeFrames.get(userId, event.mPackage);
        if (foregroundTimeFrames.size() == 0 || foregroundTimeFrames.last().isDone()) {
            if (event.mEventType != 1) {
                return;
            } else {
                foregroundTimeFrames.add(new com.android.server.pm.BackgroundInstallControlService.ForegroundTimeFrame(event.mTimeStamp));
            }
        }
        foregroundTimeFrames.last().addEvent(event);
        if (foregroundTimeFrames.size() > 10) {
            foregroundTimeFrames.pollFirst();
        }
    }

    void writeBackgroundInstalledPackagesToDisk() {
        android.util.AtomicFile atomicFile = new android.util.AtomicFile(this.mDiskFile);
        try {
            java.io.FileOutputStream fileOutputStream = atomicFile.startWrite();
            try {
                android.util.proto.ProtoOutputStream protoOutputStream = new android.util.proto.ProtoOutputStream(fileOutputStream);
                for (int i = 0; i < this.mBackgroundInstalledPackages.size(); i++) {
                    int userId = this.mBackgroundInstalledPackages.keyAt(i);
                    for (java.lang.String packageName : this.mBackgroundInstalledPackages.get(userId)) {
                        long token = protoOutputStream.start(2246267895809L);
                        protoOutputStream.write(1138166333441L, packageName);
                        protoOutputStream.write(1120986464258L, userId + 1);
                        protoOutputStream.end(token);
                    }
                }
                protoOutputStream.flush();
                atomicFile.finishWrite(fileOutputStream);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to finish write to states protobuf.", e);
                atomicFile.failWrite(fileOutputStream);
            }
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Failed to start write to states protobuf.", e2);
        }
    }

    void initBackgroundInstalledPackages() {
        if (this.mBackgroundInstalledPackages != null) {
            return;
        }
        this.mBackgroundInstalledPackages = new android.util.SparseSetArray<>();
        if (!this.mDiskFile.exists()) {
            return;
        }
        android.util.AtomicFile atomicFile = new android.util.AtomicFile(this.mDiskFile);
        try {
            java.io.FileInputStream fileInputStream = atomicFile.openRead();
            try {
                android.util.proto.ProtoInputStream protoInputStream = new android.util.proto.ProtoInputStream(fileInputStream);
                while (protoInputStream.nextField() != -1) {
                    if (protoInputStream.getFieldNumber() == 1) {
                        long token = protoInputStream.start(2246267895809L);
                        java.lang.String packageName = null;
                        int userId = -10000;
                        while (protoInputStream.nextField() != -1) {
                            switch (protoInputStream.getFieldNumber()) {
                                case 1:
                                    packageName = protoInputStream.readString(1138166333441L);
                                    break;
                                case 2:
                                    userId = protoInputStream.readInt(1120986464258L) - 1;
                                    break;
                                default:
                                    android.util.Slog.w(TAG, "Undefined field in proto: " + protoInputStream.getFieldNumber());
                                    break;
                            }
                        }
                        protoInputStream.end(token);
                        if (packageName == null || userId == -10000) {
                            android.util.Slog.w(TAG, "Fails to get packageName or UserId from proto file");
                        } else {
                            this.mBackgroundInstalledPackages.add(userId, packageName);
                        }
                    }
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "Error reading state from the disk", e);
        }
    }

    android.util.SparseSetArray<java.lang.String> getBackgroundInstalledPackages() {
        return this.mBackgroundInstalledPackages;
    }

    android.util.SparseArrayMap<java.lang.String, java.util.TreeSet<com.android.server.pm.BackgroundInstallControlService.ForegroundTimeFrame>> getInstallerForegroundTimeFrames() {
        return this.mInstallerForegroundTimeFrames;
    }

    private boolean isInstaller(java.lang.String pkgName, int userId) {
        return this.mInstallerForegroundTimeFrames.contains(userId, pkgName) || this.mPermissionManager.checkPermission(pkgName, "android.permission.INSTALL_PACKAGES", "default:0", userId) == 0;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        onStart(false);
    }

    void onStart(boolean isForTesting) {
        if (!isForTesting) {
            publishBinderService("background_install_control", this.mBinderService);
        }
        this.mPackageManagerInternal.getPackageList(this.mPackageObserver);
    }

    static final class ForegroundTimeFrame implements java.lang.Comparable<com.android.server.pm.BackgroundInstallControlService.ForegroundTimeFrame> {
        public final long startTimeStampMillis;
        public long endTimeStampMillis = 0;
        public final java.util.Set<java.lang.Integer> activities = new android.util.ArraySet();

        @Override // java.lang.Comparable
        public int compareTo(com.android.server.pm.BackgroundInstallControlService.ForegroundTimeFrame o) {
            int comp = java.lang.Long.compare(this.startTimeStampMillis, o.startTimeStampMillis);
            return comp != 0 ? comp : java.lang.Integer.compare(hashCode(), o.hashCode());
        }

        ForegroundTimeFrame(long startTimeStampMillis) {
            this.startTimeStampMillis = startTimeStampMillis;
        }

        public boolean isDone() {
            return this.endTimeStampMillis != 0;
        }

        public void addEvent(android.app.usage.UsageEvents.Event event) {
            switch (event.mEventType) {
                case 1:
                    this.activities.add(java.lang.Integer.valueOf(event.mInstanceId));
                    break;
                case 2:
                case 23:
                    if (this.activities.contains(java.lang.Integer.valueOf(event.mInstanceId))) {
                        this.activities.remove(java.lang.Integer.valueOf(event.mInstanceId));
                        if (this.activities.size() == 0) {
                            this.endTimeStampMillis = event.mTimeStamp;
                        }
                    }
                    break;
            }
        }
    }

    private static final class InjectorImpl implements com.android.server.pm.BackgroundInstallControlService.Injector {
        private final android.content.Context mContext;

        InjectorImpl(android.content.Context context) {
            this.mContext = context;
        }

        @Override // com.android.server.pm.BackgroundInstallControlService.Injector
        public android.content.Context getContext() {
            return this.mContext;
        }

        @Override // com.android.server.pm.BackgroundInstallControlService.Injector
        public android.content.pm.PackageManager getPackageManager() {
            return this.mContext.getPackageManager();
        }

        @Override // com.android.server.pm.BackgroundInstallControlService.Injector
        public android.content.pm.PackageManagerInternal getPackageManagerInternal() {
            return (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        }

        @Override // com.android.server.pm.BackgroundInstallControlService.Injector
        public android.app.usage.UsageStatsManagerInternal getUsageStatsManagerInternal() {
            return (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
        }

        @Override // com.android.server.pm.BackgroundInstallControlService.Injector
        public com.android.server.pm.permission.PermissionManagerServiceInternal getPermissionManager() {
            return (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
        }

        @Override // com.android.server.pm.BackgroundInstallControlService.Injector
        public android.os.Looper getLooper() {
            com.android.server.ServiceThread serviceThread = new com.android.server.ServiceThread(com.android.server.pm.BackgroundInstallControlService.TAG, -2, true);
            serviceThread.start();
            return serviceThread.getLooper();
        }

        @Override // com.android.server.pm.BackgroundInstallControlService.Injector
        public java.io.File getDiskFile() {
            java.io.File dir = new java.io.File(android.os.Environment.getDataSystemDirectory(), com.android.server.pm.BackgroundInstallControlService.DISK_DIR_NAME);
            java.io.File file = new java.io.File(dir, com.android.server.pm.BackgroundInstallControlService.DISK_FILE_NAME);
            return file;
        }

        @Override // com.android.server.pm.BackgroundInstallControlService.Injector
        public com.android.server.pm.BackgroundInstallControlCallbackHelper getBackgroundInstallControlCallbackHelper() {
            return new com.android.server.pm.BackgroundInstallControlCallbackHelper();
        }
    }
}
