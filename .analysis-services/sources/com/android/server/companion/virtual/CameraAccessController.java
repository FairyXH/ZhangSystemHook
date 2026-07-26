package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
class CameraAccessController extends android.hardware.camera2.CameraManager.AvailabilityCallback implements java.lang.AutoCloseable {
    private static final java.lang.String TAG = "CameraAccessController";
    private final com.android.server.companion.virtual.CameraAccessController.CameraAccessBlockedCallback mBlockedCallback;
    private final android.hardware.camera2.CameraManager mCameraManager;
    private final android.content.Context mContext;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.os.UserManager mUserManager;
    private final com.android.server.companion.virtual.VirtualDeviceManagerInternal mVirtualDeviceManagerInternal;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.lang.Object mObserverLock = new java.lang.Object();
    private int mObserverCount = 0;
    private android.util.ArrayMap<java.lang.String, com.android.server.companion.virtual.CameraAccessController.InjectionSessionData> mPackageToSessionData = new android.util.ArrayMap<>();
    private android.util.ArrayMap<java.lang.String, com.android.server.companion.virtual.CameraAccessController.OpenCameraInfo> mAppsToBlockOnVirtualDevice = new android.util.ArrayMap<>();

    interface CameraAccessBlockedCallback {
        void onCameraAccessBlocked(int i);
    }

    static class InjectionSessionData {
        public int appUid;
        public android.util.ArrayMap<java.lang.String, android.hardware.camera2.CameraInjectionSession> cameraIdToSession = new android.util.ArrayMap<>();

        InjectionSessionData() {
        }
    }

    static class OpenCameraInfo {
        public java.lang.String packageName;
        public java.util.Set<java.lang.Integer> packageUids;

        OpenCameraInfo() {
        }
    }

    CameraAccessController(android.content.Context context, com.android.server.companion.virtual.VirtualDeviceManagerInternal virtualDeviceManagerInternal, com.android.server.companion.virtual.CameraAccessController.CameraAccessBlockedCallback blockedCallback) {
        this.mContext = context;
        this.mVirtualDeviceManagerInternal = virtualDeviceManagerInternal;
        this.mBlockedCallback = blockedCallback;
        this.mCameraManager = (android.hardware.camera2.CameraManager) this.mContext.getSystemService(android.hardware.camera2.CameraManager.class);
        this.mPackageManager = this.mContext.getPackageManager();
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
    }

    public int getUserId() {
        return this.mContext.getUserId();
    }

    public int getObserverCount() {
        int i;
        synchronized (this.mObserverLock) {
            i = this.mObserverCount;
        }
        return i;
    }

    public void startObservingIfNeeded() {
        synchronized (this.mObserverLock) {
            if (this.mObserverCount == 0) {
                this.mCameraManager.registerAvailabilityCallback(this.mContext.getMainExecutor(), this);
            }
            this.mObserverCount++;
        }
    }

    public void stopObservingIfNeeded() {
        synchronized (this.mObserverLock) {
            this.mObserverCount--;
            if (this.mObserverCount <= 0) {
                close();
            }
        }
    }

    public void blockCameraAccessIfNeeded(java.util.Set<java.lang.Integer> runningUids) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mAppsToBlockOnVirtualDevice.size(); i++) {
                java.lang.String cameraId = this.mAppsToBlockOnVirtualDevice.keyAt(i);
                com.android.server.companion.virtual.CameraAccessController.OpenCameraInfo openCameraInfo = this.mAppsToBlockOnVirtualDevice.get(cameraId);
                java.lang.String packageName = openCameraInfo.packageName;
                java.util.Iterator<java.lang.Integer> it = openCameraInfo.packageUids.iterator();
                while (true) {
                    if (it.hasNext()) {
                        int packageUid = it.next().intValue();
                        if (runningUids.contains(java.lang.Integer.valueOf(packageUid))) {
                            if (this.mPackageToSessionData.get(packageName) == null) {
                                com.android.server.companion.virtual.CameraAccessController.InjectionSessionData data = new com.android.server.companion.virtual.CameraAccessController.InjectionSessionData();
                                data.appUid = packageUid;
                                this.mPackageToSessionData.put(packageName, data);
                            }
                            startBlocking(packageName, cameraId);
                        }
                    }
                }
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        synchronized (this.mObserverLock) {
            if (this.mObserverCount < 0) {
                android.util.Slog.wtf(TAG, "Unexpected negative mObserverCount: " + this.mObserverCount);
            } else if (this.mObserverCount > 0) {
                android.util.Slog.w(TAG, "Unexpected close with observers remaining: " + this.mObserverCount);
            }
        }
        this.mCameraManager.unregisterAvailabilityCallback(this);
    }

    public void onCameraOpened(java.lang.String cameraId, java.lang.String packageName) {
        synchronized (this.mLock) {
            com.android.server.companion.virtual.CameraAccessController.InjectionSessionData data = this.mPackageToSessionData.get(packageName);
            java.util.List<android.content.pm.UserInfo> aliveUsers = this.mUserManager.getAliveUsers();
            android.util.ArraySet<java.lang.Integer> packageUids = new android.util.ArraySet<>();
            for (android.content.pm.UserInfo user : aliveUsers) {
                int userId = user.getUserHandle().getIdentifier();
                int appUid = queryUidFromPackageName(userId, packageName);
                if (this.mVirtualDeviceManagerInternal.isAppRunningOnAnyVirtualDevice(appUid)) {
                    if (data == null) {
                        data = new com.android.server.companion.virtual.CameraAccessController.InjectionSessionData();
                        data.appUid = appUid;
                        this.mPackageToSessionData.put(packageName, data);
                    }
                    if (data.cameraIdToSession.containsKey(cameraId)) {
                        return;
                    }
                    startBlocking(packageName, cameraId);
                    return;
                }
                if (appUid != -1) {
                    packageUids.add(java.lang.Integer.valueOf(appUid));
                }
            }
            com.android.server.companion.virtual.CameraAccessController.OpenCameraInfo openCameraInfo = new com.android.server.companion.virtual.CameraAccessController.OpenCameraInfo();
            openCameraInfo.packageName = packageName;
            openCameraInfo.packageUids = packageUids;
            this.mAppsToBlockOnVirtualDevice.put(cameraId, openCameraInfo);
            android.hardware.camera2.CameraInjectionSession existingSession = data != null ? data.cameraIdToSession.get(cameraId) : null;
            if (existingSession != null) {
                existingSession.close();
                data.cameraIdToSession.remove(cameraId);
                if (data.cameraIdToSession.isEmpty()) {
                    this.mPackageToSessionData.remove(packageName);
                }
            }
        }
    }

    public void onCameraClosed(java.lang.String cameraId) {
        synchronized (this.mLock) {
            this.mAppsToBlockOnVirtualDevice.remove(cameraId);
            for (int i = this.mPackageToSessionData.size() - 1; i >= 0; i--) {
                com.android.server.companion.virtual.CameraAccessController.InjectionSessionData data = this.mPackageToSessionData.valueAt(i);
                android.hardware.camera2.CameraInjectionSession session = data.cameraIdToSession.get(cameraId);
                if (session != null) {
                    session.close();
                    data.cameraIdToSession.remove(cameraId);
                    if (data.cameraIdToSession.isEmpty()) {
                        this.mPackageToSessionData.removeAt(i);
                    }
                }
            }
        }
    }

    private void startBlocking(final java.lang.String packageName, final java.lang.String cameraId) {
        try {
            android.util.Slog.d(TAG, "startBlocking() cameraId: " + cameraId + " packageName: " + packageName);
            this.mCameraManager.injectCamera(packageName, cameraId, "", this.mContext.getMainExecutor(), new android.hardware.camera2.CameraInjectionSession.InjectionStatusCallback() { // from class: com.android.server.companion.virtual.CameraAccessController.1
                public void onInjectionSucceeded(android.hardware.camera2.CameraInjectionSession session) {
                    com.android.server.companion.virtual.CameraAccessController.this.onInjectionSucceeded(cameraId, packageName, session);
                }

                public void onInjectionError(int errorCode) {
                    com.android.server.companion.virtual.CameraAccessController.this.onInjectionError(cameraId, packageName, errorCode);
                }
            });
        } catch (android.hardware.camera2.CameraAccessException e) {
            android.util.Slog.e(TAG, "Failed to injectCamera for cameraId:" + cameraId + " package:" + packageName, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInjectionSucceeded(java.lang.String cameraId, java.lang.String packageName, android.hardware.camera2.CameraInjectionSession session) {
        synchronized (this.mLock) {
            com.android.server.companion.virtual.CameraAccessController.InjectionSessionData data = this.mPackageToSessionData.get(packageName);
            if (data == null) {
                android.util.Slog.e(TAG, "onInjectionSucceeded didn't find expected entry for package " + packageName);
                session.close();
            } else {
                android.hardware.camera2.CameraInjectionSession existingSession = data.cameraIdToSession.put(cameraId, session);
                if (existingSession != null) {
                    android.util.Slog.e(TAG, "onInjectionSucceeded found unexpected existing session for camera " + cameraId);
                    existingSession.close();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInjectionError(java.lang.String cameraId, java.lang.String packageName, int errorCode) {
        if (errorCode != 2) {
            android.util.Slog.e(TAG, "Unexpected injection error code:" + errorCode + " for camera:" + cameraId + " and package:" + packageName);
            return;
        }
        synchronized (this.mLock) {
            com.android.server.companion.virtual.CameraAccessController.InjectionSessionData data = this.mPackageToSessionData.get(packageName);
            if (data != null) {
                this.mBlockedCallback.onCameraAccessBlocked(data.appUid);
            }
        }
    }

    private int queryUidFromPackageName(int userId, java.lang.String packageName) {
        try {
            android.content.pm.ApplicationInfo ainfo = this.mPackageManager.getApplicationInfoAsUser(packageName, 1, userId);
            return ainfo.uid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, "queryUidFromPackageName - unknown package " + packageName, e);
            return -1;
        }
    }
}
