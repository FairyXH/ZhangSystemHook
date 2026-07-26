package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public final class StorageSessionController {
    private static final java.lang.String TAG = "StorageSessionController";
    private final android.content.Context mContext;
    private volatile int mExternalStorageServiceAppId;
    private volatile android.content.ComponentName mExternalStorageServiceComponent;
    private volatile java.lang.String mExternalStorageServicePackageName;
    private volatile boolean mIsResetting;
    private final android.os.UserManager mUserManager;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.storage.StorageUserConnection> mConnections = new android.util.SparseArray<>();

    public StorageSessionController(android.content.Context context) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
    }

    public int getConnectionUserIdForVolume(android.os.storage.VolumeInfo vol) {
        android.content.Context volumeUserContext = this.mContext.createContextAsUser(android.os.UserHandle.of(vol.mountUserId), 0);
        boolean isMediaSharedWithParent = ((android.os.UserManager) volumeUserContext.getSystemService(android.os.UserManager.class)).isMediaSharedWithParent();
        android.content.pm.UserInfo userInfo = this.mUserManager.getUserInfo(vol.mountUserId);
        if (userInfo != null && isMediaSharedWithParent) {
            return userInfo.profileGroupId;
        }
        return vol.mountUserId;
    }

    public void onVolumeMount(android.os.ParcelFileDescriptor deviceFd, android.os.storage.VolumeInfo vol) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        com.android.server.storage.StorageUserConnection connection;
        if (!shouldHandle(vol)) {
            return;
        }
        android.util.Slog.i(TAG, "On volume mount " + vol);
        java.lang.String sessionId = vol.getId();
        int userId = getConnectionUserIdForVolume(vol);
        synchronized (this.mLock) {
            connection = this.mConnections.get(userId);
            if (connection == null) {
                android.util.Slog.i(TAG, "Creating connection for user: " + userId);
                connection = new com.android.server.storage.StorageUserConnection(this.mContext, userId, this);
                this.mConnections.put(userId, connection);
            }
            android.util.Slog.i(TAG, "onVolumeMount session:" + sessionId + " started!");
        }
        android.util.Slog.i(TAG, "Creating and starting session with id: " + sessionId);
        connection.startSession(sessionId, deviceFd, vol.getPath().getPath(), vol.getInternalPath().getPath());
    }

    public void notifyVolumeStateChanged(android.os.storage.VolumeInfo vol) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        if (!shouldHandle(vol)) {
            return;
        }
        java.lang.String sessionId = vol.getId();
        int connectionUserId = getConnectionUserIdForVolume(vol);
        synchronized (this.mLock) {
            com.android.server.storage.StorageUserConnection connection = this.mConnections.get(connectionUserId);
            if (connection != null) {
                android.util.Slog.i(TAG, "Notifying volume state changed for session with id: " + sessionId);
                connection.notifyVolumeStateChanged(sessionId, vol.buildStorageVolume(this.mContext, vol.getMountUserId(), false));
            } else {
                android.util.Slog.w(TAG, "No available storage user connection for userId : " + connectionUserId);
            }
        }
    }

    public void freeCache(java.lang.String volumeUuid, long bytes) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        synchronized (this.mLock) {
            int size = this.mConnections.size();
            for (int i = 0; i < size; i++) {
                int key = this.mConnections.keyAt(i);
                com.android.server.storage.StorageUserConnection connection = this.mConnections.get(key);
                if (connection != null) {
                    connection.freeCache(volumeUuid, bytes);
                }
            }
        }
    }

    public void notifyAnrDelayStarted(java.lang.String packageName, int uid, int tid, int reason) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        com.android.server.storage.StorageUserConnection connection;
        int userId = android.os.UserHandle.getUserId(uid);
        synchronized (this.mLock) {
            connection = this.mConnections.get(userId);
        }
        if (connection != null) {
            connection.notifyAnrDelayStarted(packageName, uid, tid, reason);
        }
    }

    public com.android.server.storage.StorageUserConnection onVolumeRemove(android.os.storage.VolumeInfo vol) {
        if (!shouldHandle(vol)) {
            return null;
        }
        android.util.Slog.i(TAG, "On volume remove " + vol);
        java.lang.String sessionId = vol.getId();
        int userId = getConnectionUserIdForVolume(vol);
        synchronized (this.mLock) {
            com.android.server.storage.StorageUserConnection connection = this.mConnections.get(userId);
            if (connection != null) {
                android.util.Slog.i(TAG, "Removed session for vol with id: " + sessionId);
                if (vol.type == 0) {
                    android.util.Slog.i(TAG, "removeSessionAndWait public volume");
                    try {
                        connection.removeSessionAndWait(sessionId);
                    } catch (com.android.server.storage.StorageSessionController.ExternalStorageServiceException e) {
                        android.util.Slog.e(TAG, "Failed to end session for vol with id: " + sessionId, e);
                    }
                } else {
                    connection.removeSession(sessionId);
                }
                return connection;
            }
            android.util.Slog.w(TAG, "Session already removed for vol with id: " + sessionId);
            return null;
        }
    }

    public void onVolumeUnmount(android.os.storage.VolumeInfo vol) {
        java.lang.String sessionId = vol.getId();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                com.android.server.storage.StorageUserConnection connection = onVolumeRemove(vol);
                android.util.Slog.i(TAG, "On volume unmount " + vol);
                if (connection != null) {
                    connection.removeSessionAndWait(sessionId);
                }
            } catch (com.android.server.storage.StorageSessionController.ExternalStorageServiceException e) {
                android.util.Slog.e(TAG, "Failed to end session for vol with id: " + sessionId, e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void onUnlockUser(int userId) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        android.util.Slog.i(TAG, "On user unlock " + userId);
        if (userId == 0 || userId == 888) {
            initExternalStorageServiceComponent(userId);
        }
    }

    public void onUserStopping(int userId) {
        com.android.server.storage.StorageUserConnection connection;
        if (!shouldHandle(null)) {
            return;
        }
        synchronized (this.mLock) {
            connection = this.mConnections.get(userId);
        }
        if (connection != null) {
            android.util.Slog.i(TAG, "Removing all sessions for user: " + userId);
            connection.removeAllSessions();
        } else {
            android.util.Slog.w(TAG, "No connection found for user: " + userId);
        }
    }

    public void onReset(android.os.IVold vold, java.lang.Runnable resetHandlerRunnable) {
        if (!shouldHandle(null)) {
            return;
        }
        android.util.SparseArray<com.android.server.storage.StorageUserConnection> connections = new android.util.SparseArray<>();
        synchronized (this.mLock) {
            this.mIsResetting = true;
            android.util.Slog.i(TAG, "Started resetting external storage service...");
            for (int i = 0; i < this.mConnections.size(); i++) {
                connections.put(this.mConnections.keyAt(i), this.mConnections.valueAt(i));
            }
        }
        for (int i2 = 0; i2 < connections.size(); i2++) {
            com.android.server.storage.StorageUserConnection connection = connections.valueAt(i2);
            for (java.lang.String sessionId : connection.getAllSessionIds()) {
                try {
                    android.util.Slog.i(TAG, "Unmounting " + sessionId);
                    vold.unmount(sessionId);
                    android.util.Slog.i(TAG, "Unmounted " + sessionId);
                } catch (android.os.ServiceSpecificException | android.os.RemoteException | java.lang.NullPointerException e) {
                    android.util.Slog.e(TAG, "Failed to unmount volume: " + sessionId, e);
                }
                try {
                    android.util.Slog.i(TAG, "Exiting " + sessionId);
                    connection.removeSessionAndWait(sessionId);
                    android.util.Slog.i(TAG, "Exited " + sessionId);
                } catch (com.android.server.storage.StorageSessionController.ExternalStorageServiceException | java.lang.IllegalStateException e2) {
                    android.util.Slog.e(TAG, "Failed to exit session: " + sessionId + ". Killing MediaProvider...", e2);
                    killExternalStorageService(connections.keyAt(i2));
                }
            }
            connection.close();
        }
        resetHandlerRunnable.run();
        synchronized (this.mLock) {
            this.mConnections.clear();
            this.mIsResetting = false;
            android.util.Slog.i(TAG, "Finished resetting external storage service");
        }
    }

    private void initExternalStorageServiceComponent(int userId) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        android.content.pm.ProviderInfo provider;
        android.util.Slog.i(TAG, "Initialialising...");
        if (userId != 888) {
            provider = this.mContext.getPackageManager().resolveContentProvider("media", 1835008);
        } else {
            this.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0);
            provider = this.mContext.getPackageManager().resolveContentProviderAsUser("media", 1835008, 888);
        }
        if (provider == null) {
            throw new com.android.server.storage.StorageSessionController.ExternalStorageServiceException("No valid MediaStore provider found");
        }
        this.mExternalStorageServicePackageName = provider.applicationInfo.packageName;
        this.mExternalStorageServiceAppId = android.os.UserHandle.getAppId(provider.applicationInfo.uid);
        android.content.pm.ServiceInfo serviceInfo = resolveExternalStorageServiceAsUser(userId);
        if (serviceInfo == null) {
            throw new com.android.server.storage.StorageSessionController.ExternalStorageServiceException("No valid ExternalStorageService component found");
        }
        android.content.ComponentName name = new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
        if (!"android.permission.BIND_EXTERNAL_STORAGE_SERVICE".equals(serviceInfo.permission)) {
            throw new com.android.server.storage.StorageSessionController.ExternalStorageServiceException(name.flattenToShortString() + " does not require permission android.permission.BIND_EXTERNAL_STORAGE_SERVICE");
        }
        this.mExternalStorageServiceComponent = name;
    }

    public android.content.ComponentName getExternalStorageServiceComponentName() {
        return this.mExternalStorageServiceComponent;
    }

    public void notifyAppIoBlocked(java.lang.String volumeUuid, int uid, int tid, int reason) {
        com.android.server.storage.StorageUserConnection connection;
        int userId = android.os.UserHandle.getUserId(uid);
        synchronized (this.mLock) {
            connection = this.mConnections.get(userId);
        }
        if (connection != null) {
            connection.notifyAppIoBlocked(volumeUuid, uid, tid, reason);
        }
    }

    public void notifyAppIoResumed(java.lang.String volumeUuid, int uid, int tid, int reason) {
        com.android.server.storage.StorageUserConnection connection;
        int userId = android.os.UserHandle.getUserId(uid);
        synchronized (this.mLock) {
            connection = this.mConnections.get(userId);
        }
        if (connection != null) {
            connection.notifyAppIoResumed(volumeUuid, uid, tid, reason);
        }
    }

    public boolean isAppIoBlocked(int uid) {
        com.android.server.storage.StorageUserConnection connection;
        int userId = android.os.UserHandle.getUserId(uid);
        synchronized (this.mLock) {
            connection = this.mConnections.get(userId);
        }
        if (connection != null) {
            return connection.isAppIoBlocked(uid);
        }
        return false;
    }

    private void killExternalStorageService(int userId) {
        android.app.IActivityManager am = android.app.ActivityManager.getService();
        try {
            am.killApplication(this.mExternalStorageServicePackageName, this.mExternalStorageServiceAppId, userId, "storage_session_controller reset", 13);
        } catch (android.os.RemoteException e) {
            android.util.Slog.i(TAG, "Failed to kill the ExtenalStorageService for user " + userId);
        }
    }

    public static boolean isEmulatedOrPublic(android.os.storage.VolumeInfo vol) {
        return vol.type == 2 || (vol.type == 0 && vol.isVisible());
    }

    public static class ExternalStorageServiceException extends java.lang.Exception {
        public ExternalStorageServiceException(java.lang.Throwable cause) {
            super(cause);
        }

        public ExternalStorageServiceException(java.lang.String message) {
            super(message);
        }

        public ExternalStorageServiceException(java.lang.String message, java.lang.Throwable cause) {
            super(message, cause);
        }
    }

    private static boolean isSupportedVolume(android.os.storage.VolumeInfo vol) {
        return isEmulatedOrPublic(vol) || vol.type == 5;
    }

    private boolean shouldHandle(android.os.storage.VolumeInfo vol) {
        return !this.mIsResetting && (vol == null || isSupportedVolume(vol));
    }

    public boolean supportsExternalStorage(int userId) {
        return resolveExternalStorageServiceAsUser(userId) != null;
    }

    private android.content.pm.ServiceInfo resolveExternalStorageServiceAsUser(int userId) {
        android.content.Intent intent = new android.content.Intent("android.service.storage.ExternalStorageService");
        intent.setPackage(this.mExternalStorageServicePackageName);
        android.content.pm.ResolveInfo resolveInfo = this.mContext.getPackageManager().resolveServiceAsUser(intent, 132, userId);
        if (resolveInfo == null) {
            return null;
        }
        return resolveInfo.serviceInfo;
    }
}
