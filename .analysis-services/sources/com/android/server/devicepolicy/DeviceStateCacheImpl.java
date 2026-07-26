package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class DeviceStateCacheImpl extends android.app.admin.DeviceStateCache {
    public static final int NO_DEVICE_OWNER = -1;
    private final java.lang.Object mLock = new java.lang.Object();
    private java.util.concurrent.atomic.AtomicInteger mDeviceOwnerType = new java.util.concurrent.atomic.AtomicInteger(-1);
    private java.util.Map<java.lang.Integer, java.lang.Boolean> mHasProfileOwner = new java.util.concurrent.ConcurrentHashMap();
    private java.util.Map<java.lang.Integer, java.lang.Boolean> mAffiliationWithDevice = new java.util.concurrent.ConcurrentHashMap();
    private boolean mIsDeviceProvisioned = false;

    public boolean isDeviceProvisioned() {
        return this.mIsDeviceProvisioned;
    }

    public void setDeviceProvisioned(boolean provisioned) {
        synchronized (this.mLock) {
            this.mIsDeviceProvisioned = provisioned;
        }
    }

    void setDeviceOwnerType(int deviceOwnerType) {
        this.mDeviceOwnerType.set(deviceOwnerType);
    }

    void setHasProfileOwner(int userId, boolean hasProfileOwner) {
        if (hasProfileOwner) {
            this.mHasProfileOwner.put(java.lang.Integer.valueOf(userId), true);
        } else {
            this.mHasProfileOwner.remove(java.lang.Integer.valueOf(userId));
        }
    }

    void setHasAffiliationWithDevice(int userId, java.lang.Boolean hasAffiliateProfileOwner) {
        if (hasAffiliateProfileOwner.booleanValue()) {
            this.mAffiliationWithDevice.put(java.lang.Integer.valueOf(userId), true);
        } else {
            this.mAffiliationWithDevice.remove(java.lang.Integer.valueOf(userId));
        }
    }

    public boolean hasAffiliationWithDevice(int userId) {
        return this.mAffiliationWithDevice.getOrDefault(java.lang.Integer.valueOf(userId), false).booleanValue();
    }

    public boolean isUserOrganizationManaged(int userHandle) {
        return this.mHasProfileOwner.getOrDefault(java.lang.Integer.valueOf(userHandle), false).booleanValue() || hasEnterpriseDeviceOwner();
    }

    private boolean hasEnterpriseDeviceOwner() {
        return this.mDeviceOwnerType.get() == 0;
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        pw.println("Device state cache:");
        pw.increaseIndent();
        pw.println("Device provisioned: " + this.mIsDeviceProvisioned);
        pw.println("Device Owner Type: " + this.mDeviceOwnerType.get());
        pw.println("Has PO:");
        for (java.lang.Integer id : this.mHasProfileOwner.keySet()) {
            pw.println("User " + id + ": " + this.mHasProfileOwner.get(id));
        }
        pw.decreaseIndent();
    }
}
