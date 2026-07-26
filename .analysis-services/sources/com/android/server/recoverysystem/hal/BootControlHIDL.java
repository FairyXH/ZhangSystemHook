package com.android.server.recoverysystem.hal;

/* JADX INFO: loaded from: classes3.dex */
public class BootControlHIDL implements android.hardware.boot.IBootControl {
    private static final java.lang.String TAG = "BootControlHIDL";
    final android.hardware.boot.V1_1.IBootControl v1_1_hal;
    final android.hardware.boot.V1_2.IBootControl v1_2_hal;
    final android.hardware.boot.V1_0.IBootControl v1_hal;

    public static boolean isServicePresent() {
        try {
            android.hardware.boot.V1_0.IBootControl.getService(true);
            return true;
        } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isV1_2ServicePresent() {
        try {
            android.hardware.boot.V1_2.IBootControl.getService(true);
            return true;
        } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
            return false;
        }
    }

    public static com.android.server.recoverysystem.hal.BootControlHIDL getService() throws android.os.RemoteException {
        android.hardware.boot.V1_0.IBootControl v1_hal = android.hardware.boot.V1_0.IBootControl.getService(true);
        android.hardware.boot.V1_1.IBootControl v1_1_hal = android.hardware.boot.V1_1.IBootControl.castFrom((android.os.IHwInterface) v1_hal);
        android.hardware.boot.V1_2.IBootControl v1_2_hal = android.hardware.boot.V1_2.IBootControl.castFrom((android.os.IHwInterface) v1_hal);
        return new com.android.server.recoverysystem.hal.BootControlHIDL(v1_hal, v1_1_hal, v1_2_hal);
    }

    private BootControlHIDL(android.hardware.boot.V1_0.IBootControl v1_hal, android.hardware.boot.V1_1.IBootControl v1_1_hal, android.hardware.boot.V1_2.IBootControl v1_2_hal) throws android.os.RemoteException {
        this.v1_hal = v1_hal;
        this.v1_1_hal = v1_1_hal;
        this.v1_2_hal = v1_2_hal;
        if (v1_hal == null) {
            throw new android.os.RemoteException("Failed to find V1.0 BootControl HIDL");
        }
        if (v1_2_hal != null) {
            android.util.Slog.i(TAG, "V1.2 version of BootControl HIDL HAL available, using V1.2");
        } else if (v1_1_hal != null) {
            android.util.Slog.i(TAG, "V1.1 version of BootControl HIDL HAL available, using V1.1");
        } else {
            android.util.Slog.i(TAG, "V1.0 version of BootControl HIDL HAL available, using V1.0");
        }
    }

    @Override // android.os.IInterface
    public android.os.IBinder asBinder() {
        return null;
    }

    @Override // android.hardware.boot.IBootControl
    public int getActiveBootSlot() throws android.os.RemoteException {
        if (this.v1_2_hal == null) {
            throw new android.os.RemoteException("getActiveBootSlot() requires V1.2 BootControl HAL");
        }
        return this.v1_2_hal.getActiveBootSlot();
    }

    @Override // android.hardware.boot.IBootControl
    public int getCurrentSlot() throws android.os.RemoteException {
        return this.v1_hal.getCurrentSlot();
    }

    @Override // android.hardware.boot.IBootControl
    public int getNumberSlots() throws android.os.RemoteException {
        return this.v1_hal.getNumberSlots();
    }

    @Override // android.hardware.boot.IBootControl
    public int getSnapshotMergeStatus() throws android.os.RemoteException {
        if (this.v1_1_hal == null) {
            throw new android.os.RemoteException("getSnapshotMergeStatus() requires V1.1 BootControl HAL");
        }
        return this.v1_1_hal.getSnapshotMergeStatus();
    }

    @Override // android.hardware.boot.IBootControl
    public java.lang.String getSuffix(int slot) throws android.os.RemoteException {
        return this.v1_hal.getSuffix(slot);
    }

    @Override // android.hardware.boot.IBootControl
    public boolean isSlotBootable(int slot) throws android.os.RemoteException {
        int ret = this.v1_hal.isSlotBootable(slot);
        if (ret != -1) {
            return ret != 0;
        }
        throw new android.os.RemoteException("isSlotBootable() failed, Slot %d might be invalid.".formatted(java.lang.Integer.valueOf(slot)));
    }

    @Override // android.hardware.boot.IBootControl
    public boolean isSlotMarkedSuccessful(int slot) throws android.os.RemoteException {
        int ret = this.v1_hal.isSlotMarkedSuccessful(slot);
        if (ret != -1) {
            return ret != 0;
        }
        throw new android.os.RemoteException("isSlotMarkedSuccessful() failed, Slot %d might be invalid.".formatted(java.lang.Integer.valueOf(slot)));
    }

    @Override // android.hardware.boot.IBootControl
    public void markBootSuccessful() throws android.os.RemoteException {
        android.hardware.boot.V1_0.CommandResult res = this.v1_hal.markBootSuccessful();
        if (!res.success) {
            throw new android.os.RemoteException("Error markBootSuccessful() " + res.errMsg);
        }
    }

    @Override // android.hardware.boot.IBootControl
    public void setActiveBootSlot(int slot) throws android.os.RemoteException {
        android.hardware.boot.V1_0.CommandResult res = this.v1_hal.setActiveBootSlot(slot);
        if (!res.success) {
            throw new android.os.RemoteException("Error setActiveBootSlot(%d) %s".formatted(java.lang.Integer.valueOf(slot), res.errMsg));
        }
    }

    @Override // android.hardware.boot.IBootControl
    public void setSlotAsUnbootable(int slot) throws android.os.RemoteException {
        android.hardware.boot.V1_0.CommandResult res = this.v1_hal.setSlotAsUnbootable(slot);
        if (!res.success) {
            throw new android.os.RemoteException("Error setSlotAsUnbootable(%d) %s".formatted(java.lang.Integer.valueOf(slot), res.errMsg));
        }
    }

    @Override // android.hardware.boot.IBootControl
    public void setSnapshotMergeStatus(int status) throws android.os.RemoteException {
        if (this.v1_1_hal == null) {
            throw new android.os.RemoteException("getSnapshotMergeStatus() requires V1.1 BootControl HAL");
        }
        if (!this.v1_1_hal.setSnapshotMergeStatus(status)) {
            throw new android.os.RemoteException("Error setSnapshotMergeStatus(%d)".formatted(java.lang.Integer.valueOf(status)));
        }
    }

    @Override // android.hardware.boot.IBootControl
    public int getInterfaceVersion() throws android.os.RemoteException {
        return 1;
    }

    @Override // android.hardware.boot.IBootControl
    public java.lang.String getInterfaceHash() throws android.os.RemoteException {
        return this.v1_hal.interfaceDescriptor();
    }
}
