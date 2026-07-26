package com.android.server.oemlock;

/* JADX INFO: loaded from: classes2.dex */
class PersistentDataBlockLock extends com.android.server.oemlock.OemLock {
    private static final java.lang.String TAG = "OemLock";
    private android.content.Context mContext;

    PersistentDataBlockLock(android.content.Context context) {
        this.mContext = context;
    }

    @Override // com.android.server.oemlock.OemLock
    java.lang.String getLockName() {
        return "";
    }

    @Override // com.android.server.oemlock.OemLock
    void setOemUnlockAllowedByCarrier(boolean allowed, byte[] signature) {
        if (signature != null) {
            android.util.Slog.w(TAG, "Signature provided but is not being used");
        }
        android.os.UserManager.get(this.mContext).setUserRestriction("no_oem_unlock", !allowed, android.os.UserHandle.SYSTEM);
        if (!allowed) {
            disallowUnlockIfNotUnlocked();
        }
    }

    @Override // com.android.server.oemlock.OemLock
    boolean isOemUnlockAllowedByCarrier() {
        return !android.os.UserManager.get(this.mContext).hasUserRestriction("no_oem_unlock", android.os.UserHandle.SYSTEM);
    }

    @Override // com.android.server.oemlock.OemLock
    void setOemUnlockAllowedByDevice(boolean allowedByDevice) {
        android.service.persistentdata.PersistentDataBlockManager pdbm = (android.service.persistentdata.PersistentDataBlockManager) this.mContext.getSystemService("persistent_data_block");
        if (pdbm == null) {
            android.util.Slog.w(TAG, "PersistentDataBlock is not supported on this device");
        } else {
            pdbm.setOemUnlockEnabled(allowedByDevice);
        }
    }

    @Override // com.android.server.oemlock.OemLock
    boolean isOemUnlockAllowedByDevice() {
        android.service.persistentdata.PersistentDataBlockManager pdbm = (android.service.persistentdata.PersistentDataBlockManager) this.mContext.getSystemService("persistent_data_block");
        if (pdbm == null) {
            android.util.Slog.w(TAG, "PersistentDataBlock is not supported on this device");
            return false;
        }
        return pdbm.getOemUnlockEnabled();
    }

    private void disallowUnlockIfNotUnlocked() {
        android.service.persistentdata.PersistentDataBlockManager pdbm = (android.service.persistentdata.PersistentDataBlockManager) this.mContext.getSystemService("persistent_data_block");
        if (pdbm == null) {
            android.util.Slog.w(TAG, "PersistentDataBlock is not supported on this device");
        } else if (pdbm.getFlashLockState() != 0) {
            pdbm.setOemUnlockEnabled(false);
        }
    }
}
