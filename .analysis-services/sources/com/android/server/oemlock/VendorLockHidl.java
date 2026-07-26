package com.android.server.oemlock;

/* JADX INFO: loaded from: classes2.dex */
class VendorLockHidl extends com.android.server.oemlock.OemLock {
    private static final java.lang.String TAG = "OemLock";
    private android.content.Context mContext;
    private android.hardware.oemlock.V1_0.IOemLock mOemLock = getOemLockHalService();

    static android.hardware.oemlock.V1_0.IOemLock getOemLockHalService() {
        try {
            return android.hardware.oemlock.V1_0.IOemLock.getService(true);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (java.util.NoSuchElementException e2) {
            android.util.Slog.i(TAG, "OemLock Hidl HAL not present on device");
            return null;
        }
    }

    VendorLockHidl(android.content.Context context) {
        this.mContext = context;
    }

    @Override // com.android.server.oemlock.OemLock
    java.lang.String getLockName() {
        final java.lang.String[] lockName = new java.lang.String[1];
        final java.lang.Integer[] requestStatus = new java.lang.Integer[1];
        try {
            this.mOemLock.getName(new android.hardware.oemlock.V1_0.IOemLock.getNameCallback() { // from class: com.android.server.oemlock.VendorLockHidl$$ExternalSyntheticLambda1
                @Override // android.hardware.oemlock.V1_0.IOemLock.getNameCallback
                public final void onValues(int i, java.lang.String str) {
                    com.android.server.oemlock.VendorLockHidl.lambda$getLockName$0(requestStatus, lockName, i, str);
                }
            });
            switch (requestStatus[0].intValue()) {
                case 0:
                    return lockName[0];
                case 1:
                    android.util.Slog.e(TAG, "Failed to get OEM lock name.");
                    return null;
                default:
                    android.util.Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
                    return null;
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to get name from HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    static /* synthetic */ void lambda$getLockName$0(java.lang.Integer[] requestStatus, java.lang.String[] lockName, int status, java.lang.String name) {
        requestStatus[0] = java.lang.Integer.valueOf(status);
        lockName[0] = name;
    }

    @Override // com.android.server.oemlock.OemLock
    void setOemUnlockAllowedByCarrier(boolean allowed, byte[] signature) {
        try {
            java.util.ArrayList<java.lang.Byte> signatureBytes = toByteArrayList(signature);
            switch (this.mOemLock.setOemUnlockAllowedByCarrier(allowed, signatureBytes)) {
                case 0:
                    android.util.Slog.i(TAG, "Updated carrier allows OEM lock state to: " + allowed);
                    return;
                case 1:
                    break;
                case 2:
                    if (signatureBytes.isEmpty()) {
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
        final java.lang.Boolean[] allowedByCarrier = new java.lang.Boolean[1];
        final java.lang.Integer[] requestStatus = new java.lang.Integer[1];
        try {
            this.mOemLock.isOemUnlockAllowedByCarrier(new android.hardware.oemlock.V1_0.IOemLock.isOemUnlockAllowedByCarrierCallback() { // from class: com.android.server.oemlock.VendorLockHidl$$ExternalSyntheticLambda0
                @Override // android.hardware.oemlock.V1_0.IOemLock.isOemUnlockAllowedByCarrierCallback
                public final void onValues(int i, boolean z) {
                    com.android.server.oemlock.VendorLockHidl.lambda$isOemUnlockAllowedByCarrier$1(requestStatus, allowedByCarrier, i, z);
                }
            });
            switch (requestStatus[0].intValue()) {
                case 0:
                    return allowedByCarrier[0].booleanValue();
                case 1:
                    break;
                default:
                    android.util.Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
                    break;
            }
            throw new java.lang.RuntimeException("Failed to get carrier OEM unlock state");
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to get carrier state from HAL");
            throw e.rethrowFromSystemServer();
        }
    }

    static /* synthetic */ void lambda$isOemUnlockAllowedByCarrier$1(java.lang.Integer[] requestStatus, java.lang.Boolean[] allowedByCarrier, int status, boolean allowed) {
        requestStatus[0] = java.lang.Integer.valueOf(status);
        allowedByCarrier[0] = java.lang.Boolean.valueOf(allowed);
    }

    @Override // com.android.server.oemlock.OemLock
    void setOemUnlockAllowedByDevice(boolean allowedByDevice) {
        try {
            switch (this.mOemLock.setOemUnlockAllowedByDevice(allowedByDevice)) {
                case 0:
                    android.util.Slog.i(TAG, "Updated device allows OEM lock state to: " + allowedByDevice);
                    return;
                case 1:
                    break;
                default:
                    android.util.Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
                    break;
            }
            throw new java.lang.RuntimeException("Failed to set device OEM unlock state");
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to set device state with HAL", e);
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // com.android.server.oemlock.OemLock
    boolean isOemUnlockAllowedByDevice() {
        final java.lang.Boolean[] allowedByDevice = new java.lang.Boolean[1];
        final java.lang.Integer[] requestStatus = new java.lang.Integer[1];
        try {
            this.mOemLock.isOemUnlockAllowedByDevice(new android.hardware.oemlock.V1_0.IOemLock.isOemUnlockAllowedByDeviceCallback() { // from class: com.android.server.oemlock.VendorLockHidl$$ExternalSyntheticLambda2
                @Override // android.hardware.oemlock.V1_0.IOemLock.isOemUnlockAllowedByDeviceCallback
                public final void onValues(int i, boolean z) {
                    com.android.server.oemlock.VendorLockHidl.lambda$isOemUnlockAllowedByDevice$2(requestStatus, allowedByDevice, i, z);
                }
            });
            switch (requestStatus[0].intValue()) {
                case 0:
                    return allowedByDevice[0].booleanValue();
                case 1:
                    break;
                default:
                    android.util.Slog.e(TAG, "Unknown return value indicates code is out of sync with HAL");
                    break;
            }
            throw new java.lang.RuntimeException("Failed to get device OEM unlock state");
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to get devie state from HAL");
            throw e.rethrowFromSystemServer();
        }
    }

    static /* synthetic */ void lambda$isOemUnlockAllowedByDevice$2(java.lang.Integer[] requestStatus, java.lang.Boolean[] allowedByDevice, int status, boolean allowed) {
        requestStatus[0] = java.lang.Integer.valueOf(status);
        allowedByDevice[0] = java.lang.Boolean.valueOf(allowed);
    }

    private java.util.ArrayList<java.lang.Byte> toByteArrayList(byte[] data) {
        if (data == null) {
            return new java.util.ArrayList<>();
        }
        java.util.ArrayList<java.lang.Byte> result = new java.util.ArrayList<>(data.length);
        for (byte b : data) {
            result.add(java.lang.Byte.valueOf(b));
        }
        return result;
    }
}
