package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public class AppFuseBridge implements java.lang.Runnable {
    private static final java.lang.String APPFUSE_MOUNT_NAME_TEMPLATE = "/mnt/appfuse/%d_%d";
    public static final java.lang.String TAG = "AppFuseBridge";
    private final android.util.SparseArray<com.android.server.storage.AppFuseBridge.MountScope> mScopes = new android.util.SparseArray<>();
    private long mNativeLoop = native_new();

    private native int native_add_bridge(long j, int i, int i2);

    private native void native_delete(long j);

    private native void native_lock();

    private native long native_new();

    private native void native_start_loop(long j);

    private native void native_unlock();

    public android.os.ParcelFileDescriptor addBridge(com.android.server.storage.AppFuseBridge.MountScope mountScope) throws com.android.internal.os.FuseUnavailableMountException, com.android.server.AppFuseMountException {
        android.os.ParcelFileDescriptor result;
        native_lock();
        try {
            synchronized (this) {
                com.android.internal.util.Preconditions.checkArgument(this.mScopes.indexOfKey(mountScope.mountId) < 0);
                if (this.mNativeLoop == 0) {
                    throw new com.android.internal.os.FuseUnavailableMountException(mountScope.mountId);
                }
                int fd = native_add_bridge(this.mNativeLoop, mountScope.mountId, mountScope.open().detachFd());
                if (fd == -1) {
                    throw new com.android.internal.os.FuseUnavailableMountException(mountScope.mountId);
                }
                result = android.os.ParcelFileDescriptor.adoptFd(fd);
                this.mScopes.put(mountScope.mountId, mountScope);
                mountScope = null;
            }
            return result;
        } finally {
            native_unlock();
            libcore.io.IoUtils.closeQuietly(mountScope);
        }
    }

    public void startserviceAppFuse(com.android.server.storage.AppFuseBridge.MountScope mountScope) throws com.android.internal.os.FuseUnavailableMountException, com.android.server.AppFuseMountException {
        try {
            synchronized (this) {
                mountScope.startserviceAppFuse();
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(mountScope);
        }
    }

    public void stopserviceAppFuse(com.android.server.storage.AppFuseBridge.MountScope mountScope) throws com.android.internal.os.FuseUnavailableMountException, com.android.server.AppFuseMountException {
        try {
            synchronized (this) {
                mountScope.stopserviceAppFuse();
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(mountScope);
        }
    }

    public void clearCache(com.android.server.storage.AppFuseBridge.MountScope mountScope) throws com.android.internal.os.FuseUnavailableMountException, com.android.server.AppFuseMountException {
        try {
            synchronized (this) {
                mountScope.clearCache();
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(mountScope);
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        native_start_loop(this.mNativeLoop);
        synchronized (this) {
            native_delete(this.mNativeLoop);
            this.mNativeLoop = 0L;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.os.FuseUnavailableMountException */
    public android.os.ParcelFileDescriptor openFile(int mountId, int fileId, int mode) throws java.lang.InterruptedException, com.android.internal.os.FuseUnavailableMountException {
        com.android.server.storage.AppFuseBridge.MountScope scope;
        synchronized (this) {
            scope = this.mScopes.get(mountId);
            if (scope == null) {
                throw new com.android.internal.os.FuseUnavailableMountException(mountId);
            }
        }
        boolean result = scope.waitForMount();
        if (!result) {
            throw new com.android.internal.os.FuseUnavailableMountException(mountId);
        }
        try {
            int flags = android.os.FileUtils.translateModePfdToPosix(mode);
            return scope.openFile(mountId, fileId, flags);
        } catch (com.android.server.AppFuseMountException e) {
            throw new com.android.internal.os.FuseUnavailableMountException(mountId);
        }
    }

    private synchronized void onMount(int mountId) {
        com.android.server.storage.AppFuseBridge.MountScope scope = this.mScopes.get(mountId);
        if (scope != null) {
            scope.setMountResultLocked(true);
        }
    }

    private synchronized void onClosed(int mountId) {
        com.android.server.storage.AppFuseBridge.MountScope scope = this.mScopes.get(mountId);
        if (scope != null) {
            scope.setMountResultLocked(false);
            libcore.io.IoUtils.closeQuietly(scope);
            this.mScopes.remove(mountId);
        }
    }

    public static abstract class MountScope implements java.lang.AutoCloseable {
        public final int mountId;
        public final int uid;
        private final java.util.concurrent.CountDownLatch mMounted = new java.util.concurrent.CountDownLatch(1);
        private boolean mMountResult = false;

        public abstract void clearCache();

        public abstract android.os.ParcelFileDescriptor open() throws com.android.server.AppFuseMountException;

        public abstract android.os.ParcelFileDescriptor openFile(int i, int i2, int i3) throws com.android.server.AppFuseMountException;

        public abstract void startserviceAppFuse();

        public abstract void stopserviceAppFuse();

        public MountScope(int uid, int mountId) {
            this.uid = uid;
            this.mountId = mountId;
        }

        void setMountResultLocked(boolean result) {
            if (this.mMounted.getCount() == 0) {
                return;
            }
            this.mMountResult = result;
            this.mMounted.countDown();
        }

        boolean waitForMount() throws java.lang.InterruptedException {
            this.mMounted.await();
            return this.mMountResult;
        }
    }
}
