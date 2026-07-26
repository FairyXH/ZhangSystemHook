package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class DynamicSystemService extends android.os.image.IDynamicSystemService.Stub {
    private static final int GSID_ROUGH_TIMEOUT_MS = 8192;
    private static final long MINIMUM_SD_MB = 30720;
    private static final java.lang.String PATH_DEFAULT = "/data/gsi/dsu/";
    private static final java.lang.String TAG = "DynamicSystemService";
    private android.content.Context mContext;
    private java.lang.String mDsuSlot;
    private volatile android.gsi.IGsiService mGsiService;
    private java.lang.String mInstallPath;

    DynamicSystemService(android.content.Context context) {
        this.mContext = context;
    }

    private android.gsi.IGsiService getGsiService() {
        if (this.mGsiService != null) {
            return this.mGsiService;
        }
        return android.gsi.IGsiService.Stub.asInterface(android.os.ServiceManager.waitForService("gsiservice"));
    }

    class GsiServiceCallback extends android.gsi.IGsiServiceCallback.Stub {
        private int mResult = -1;

        GsiServiceCallback() {
        }

        @Override // android.gsi.IGsiServiceCallback
        public synchronized void onResult(int result) {
            this.mResult = result;
            notify();
        }

        public int getResult() {
            return this.mResult;
        }
    }

    public boolean startInstallation(java.lang.String dsuSlot) throws android.os.RemoteException {
        super.startInstallation_enforcePermission();
        android.gsi.IGsiService service = getGsiService();
        this.mGsiService = service;
        java.lang.String path = android.os.SystemProperties.get("os.aot.path");
        if (path.isEmpty()) {
            int userId = android.os.UserHandle.myUserId();
            android.os.storage.StorageManager sm = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
            for (android.os.storage.VolumeInfo volume : sm.getVolumes()) {
                if (volume.getType() == 0 && volume.isMountedWritable() && "vfat".equalsIgnoreCase(volume.fsType)) {
                    android.os.storage.DiskInfo disk = volume.getDisk();
                    long mega = disk.size >> 20;
                    android.util.Slog.i(TAG, volume.getPath() + ": " + mega + " MB");
                    if (mega < MINIMUM_SD_MB) {
                        android.util.Slog.i(TAG, volume.getPath() + ": insufficient storage");
                    } else {
                        java.io.File sd_internal = volume.getInternalPathForUser(userId);
                        if (sd_internal != null) {
                            path = new java.io.File(sd_internal, dsuSlot).getPath();
                        }
                    }
                }
            }
            if (path.isEmpty()) {
                path = PATH_DEFAULT + dsuSlot;
            }
            android.util.Slog.i(TAG, "startInstallation -> " + path);
        }
        this.mInstallPath = path;
        this.mDsuSlot = dsuSlot;
        if (service.openInstall(path) != 0) {
            android.util.Slog.i(TAG, "Failed to open " + path);
            return false;
        }
        return true;
    }

    public int createPartition(java.lang.String name, long size, boolean readOnly) throws android.os.RemoteException {
        super.createPartition_enforcePermission();
        android.gsi.IGsiService service = getGsiService();
        int status = service.createPartition(name, size, readOnly);
        if (status != 0) {
            android.util.Slog.i(TAG, "Failed to create partition: " + name);
        }
        return status;
    }

    public boolean closePartition() throws android.os.RemoteException {
        super.closePartition_enforcePermission();
        android.gsi.IGsiService service = getGsiService();
        if (service.closePartition() != 0) {
            android.util.Slog.i(TAG, "Partition installation completes with error");
            return false;
        }
        return true;
    }

    public boolean finishInstallation() throws android.os.RemoteException {
        super.finishInstallation_enforcePermission();
        android.gsi.IGsiService service = getGsiService();
        if (service.closeInstall() != 0) {
            android.util.Slog.i(TAG, "Failed to finish installation");
            return false;
        }
        return true;
    }

    public android.gsi.GsiProgress getInstallationProgress() throws android.os.RemoteException {
        super.getInstallationProgress_enforcePermission();
        return getGsiService().getInstallProgress();
    }

    public boolean abort() throws android.os.RemoteException {
        super.abort_enforcePermission();
        return getGsiService().cancelGsiInstall();
    }

    public boolean isInUse() {
        return android.os.SystemProperties.getBoolean("ro.gsid.image_running", false);
    }

    public boolean isInstalled() {
        boolean installed = android.os.SystemProperties.getBoolean("gsid.image_installed", false);
        android.util.Slog.i(TAG, "isInstalled(): " + installed);
        return installed;
    }

    public boolean isEnabled() throws android.os.RemoteException {
        super.isEnabled_enforcePermission();
        return getGsiService().isGsiEnabled();
    }

    public boolean remove() throws android.os.RemoteException {
        super.remove_enforcePermission();
        try {
            com.android.server.DynamicSystemService.GsiServiceCallback callback = new com.android.server.DynamicSystemService.GsiServiceCallback();
            synchronized (callback) {
                getGsiService().removeGsiAsync(callback);
                callback.wait(8192L);
            }
            return callback.getResult() == 0;
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.e(TAG, "remove() was interrupted");
            return false;
        }
    }

    public boolean setEnable(boolean enable, boolean oneShot) throws android.os.RemoteException {
        super.setEnable_enforcePermission();
        android.gsi.IGsiService gsiService = getGsiService();
        if (enable) {
            try {
                getActiveDsuSlot();
                com.android.server.DynamicSystemService.GsiServiceCallback callback = new com.android.server.DynamicSystemService.GsiServiceCallback();
                synchronized (callback) {
                    gsiService.enableGsiAsync(oneShot, this.mDsuSlot, callback);
                    callback.wait(8192L);
                }
                return callback.getResult() == 0;
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.e(TAG, "setEnable() was interrupted");
                return false;
            }
        }
        return gsiService.disableGsi();
    }

    public boolean setAshmem(android.os.ParcelFileDescriptor ashmem, long size) {
        super.setAshmem_enforcePermission();
        try {
            return getGsiService().setGsiAshmem(ashmem, size);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e.toString());
        }
    }

    public boolean submitFromAshmem(long size) {
        super.submitFromAshmem_enforcePermission();
        try {
            return getGsiService().commitGsiChunkFromAshmem(size);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e.toString());
        }
    }

    public boolean getAvbPublicKey(android.gsi.AvbPublicKey dst) {
        super.getAvbPublicKey_enforcePermission();
        try {
            return getGsiService().getAvbPublicKey(dst) == 0;
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e.toString());
        }
    }

    public long suggestScratchSize() throws android.os.RemoteException {
        super.suggestScratchSize_enforcePermission();
        return getGsiService().suggestScratchSize();
    }

    public java.lang.String getActiveDsuSlot() throws android.os.RemoteException {
        super.getActiveDsuSlot_enforcePermission();
        if (this.mDsuSlot == null) {
            this.mDsuSlot = getGsiService().getActiveDsuSlot();
        }
        return this.mDsuSlot;
    }
}
