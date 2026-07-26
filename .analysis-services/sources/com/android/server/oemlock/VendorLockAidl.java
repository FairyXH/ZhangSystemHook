package com.android.server.oemlock;

/* JADX INFO: loaded from: classes2.dex */
class VendorLockAidl extends com.android.server.oemlock.OemLock {
    private static final java.lang.String TAG = "OemLock";
    private android.hardware.oemlock.IOemLock mOemLock = getOemLockHalService();

    static android.hardware.oemlock.IOemLock getOemLockHalService() {
        return android.hardware.oemlock.IOemLock.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(android.hardware.oemlock.IOemLock.DESCRIPTOR + "/default"));
    }

    VendorLockAidl(android.content.Context context) {
    }

    @Override // com.android.server.oemlock.OemLock
    java.lang.String getLockName() {
        try {
            return this.mOemLock.getName();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to get name from HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // com.android.server.oemlock.OemLock
    void setOemUnlockAllowedByCarrier(boolean allowed, byte[] signature) {
        int status;
        try {
            if (signature == null) {
                status = this.mOemLock.setOemUnlockAllowedByCarrier(allowed, new byte[0]);
            } else {
                status = this.mOemLock.setOemUnlockAllowedByCarrier(allowed, signature);
            }
            switch (status) {
                case 0:
                    android.util.Slog.i(TAG, "Updated carrier allows OEM lock state to: " + allowed);
                    return;
                case 1:
                    break;
                case 2:
                    if (signature == null) {
                        throw new java.lang.IllegalArgumentException("Signature required for carrier unlock");
                    }
                    throw new java.lang.SecurityException("Invalid signature used in attempt to carrier unlock");
                default:
                    android.util.Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
                    break;
            }
            throw new java.lang.RuntimeException("Failed to set carrier OEM unlock state");
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to set carrier state with HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // com.android.server.oemlock.OemLock
    boolean isOemUnlockAllowedByCarrier() {
        try {
            return this.mOemLock.isOemUnlockAllowedByCarrier();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to get carrier state from HAL");
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // com.android.server.oemlock.OemLock
    void setOemUnlockAllowedByDevice(boolean allowedByDevice) {
        try {
            this.mOemLock.setOemUnlockAllowedByDevice(allowedByDevice);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to set device state with HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // com.android.server.oemlock.OemLock
    boolean isOemUnlockAllowedByDevice() {
        try {
            return this.mOemLock.isOemUnlockAllowedByDevice();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to get devie state from HAL");
            throw e.rethrowFromSystemServer();
        }
    }
}
