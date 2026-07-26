package com.android.server.oemlock;

/* JADX INFO: loaded from: classes2.dex */
public class OemLockService extends com.android.server.SystemService {
    private static final java.lang.String FLASH_LOCK_PROP = "ro.boot.flash.locked";
    private static final java.lang.String FLASH_LOCK_UNLOCKED = "0";
    private static final java.lang.String TAG = "OemLock";
    private android.content.Context mContext;
    private com.android.server.oemlock.OemLock mOemLock;
    private final android.os.IBinder mService;
    private final com.android.server.pm.UserManagerInternal.UserRestrictionsListener mUserRestrictionsListener;

    public static boolean isHalPresent() {
        return (com.android.server.oemlock.VendorLockHidl.getOemLockHalService() == null && com.android.server.oemlock.VendorLockAidl.getOemLockHalService() == null) ? false : true;
    }

    private static com.android.server.oemlock.OemLock getOemLock(android.content.Context context) {
        if (com.android.server.oemlock.VendorLockAidl.getOemLockHalService() != null) {
            android.util.Slog.i(TAG, "Using vendor lock via the HAL(aidl)");
            return new com.android.server.oemlock.VendorLockAidl(context);
        }
        if (com.android.server.oemlock.VendorLockHidl.getOemLockHalService() != null) {
            android.util.Slog.i(TAG, "Using vendor lock via the HAL(hidl)");
            return new com.android.server.oemlock.VendorLockHidl(context);
        }
        android.util.Slog.i(TAG, "Using persistent data block based lock");
        return new com.android.server.oemlock.PersistentDataBlockLock(context);
    }

    public OemLockService(android.content.Context context) {
        this(context, getOemLock(context));
    }

    OemLockService(android.content.Context context, com.android.server.oemlock.OemLock oemLock) {
        super(context);
        this.mUserRestrictionsListener = new com.android.server.pm.UserManagerInternal.UserRestrictionsListener() { // from class: com.android.server.oemlock.OemLockService.1
            @Override // com.android.server.pm.UserManagerInternal.UserRestrictionsListener
            public void onUserRestrictionsChanged(int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
                if (com.android.server.pm.UserRestrictionsUtils.restrictionsChanged(prevRestrictions, newRestrictions, "no_factory_reset")) {
                    boolean unlockAllowedByAdmin = !newRestrictions.getBoolean("no_factory_reset");
                    if (!unlockAllowedByAdmin) {
                        com.android.server.oemlock.OemLockService.this.mOemLock.setOemUnlockAllowedByDevice(false);
                        com.android.server.oemlock.OemLockService.this.setPersistentDataBlockOemUnlockAllowedBit(false);
                    }
                }
            }
        };
        this.mService = new android.service.oemlock.IOemLockService.Stub() { // from class: com.android.server.oemlock.OemLockService.2
            public java.lang.String getLockName() {
                super.getLockName_enforcePermission();
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    return com.android.server.oemlock.OemLockService.this.mOemLock.getLockName();
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public void setOemUnlockAllowedByCarrier(boolean allowed, byte[] signature) {
                super.setOemUnlockAllowedByCarrier_enforcePermission();
                com.android.server.oemlock.OemLockService.this.enforceUserIsAdmin();
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.oemlock.OemLockService.this.mOemLock.setOemUnlockAllowedByCarrier(allowed, signature);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public boolean isOemUnlockAllowedByCarrier() {
                super.isOemUnlockAllowedByCarrier_enforcePermission();
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    return com.android.server.oemlock.OemLockService.this.mOemLock.isOemUnlockAllowedByCarrier();
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public void setOemUnlockAllowedByUser(boolean allowedByUser) {
                super.setOemUnlockAllowedByUser_enforcePermission();
                if (android.app.ActivityManager.isUserAMonkey()) {
                    return;
                }
                com.android.server.oemlock.OemLockService.this.enforceUserIsAdmin();
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    if (!com.android.server.oemlock.OemLockService.this.isOemUnlockAllowedByAdmin()) {
                        throw new java.lang.SecurityException("Admin does not allow OEM unlock");
                    }
                    if (!com.android.server.oemlock.OemLockService.this.mOemLock.isOemUnlockAllowedByCarrier()) {
                        throw new java.lang.SecurityException("Carrier does not allow OEM unlock");
                    }
                    com.android.server.oemlock.OemLockService.this.mOemLock.setOemUnlockAllowedByDevice(allowedByUser);
                    com.android.server.oemlock.OemLockService.this.setPersistentDataBlockOemUnlockAllowedBit(allowedByUser);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public boolean isOemUnlockAllowedByUser() {
                super.isOemUnlockAllowedByUser_enforcePermission();
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    return com.android.server.oemlock.OemLockService.this.mOemLock.isOemUnlockAllowedByDevice();
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public boolean isOemUnlockAllowed() {
                super.isOemUnlockAllowed_enforcePermission();
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    boolean allowed = com.android.server.oemlock.OemLockService.this.mOemLock.isOemUnlockAllowedByCarrier() && com.android.server.oemlock.OemLockService.this.mOemLock.isOemUnlockAllowedByDevice();
                    com.android.server.oemlock.OemLockService.this.setPersistentDataBlockOemUnlockAllowedBit(allowed);
                    return allowed;
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public boolean isDeviceOemUnlocked() {
                byte b;
                super.isDeviceOemUnlocked_enforcePermission();
                java.lang.String locked = android.os.SystemProperties.get(com.android.server.oemlock.OemLockService.FLASH_LOCK_PROP);
                switch (locked.hashCode()) {
                    case 48:
                        if (locked.equals(com.android.server.oemlock.OemLockService.FLASH_LOCK_UNLOCKED)) {
                            b = 0;
                            break;
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        return true;
                    default:
                        return false;
                }
            }
        };
        this.mContext = context;
        this.mOemLock = oemLock;
        ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).addUserRestrictionsListener(this.mUserRestrictionsListener);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("oem_lock", this.mService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPersistentDataBlockOemUnlockAllowedBit(boolean allowed) {
        com.android.server.pdb.PersistentDataBlockManagerInternal pdbmi = (com.android.server.pdb.PersistentDataBlockManagerInternal) com.android.server.LocalServices.getService(com.android.server.pdb.PersistentDataBlockManagerInternal.class);
        if (pdbmi != null && !(this.mOemLock instanceof com.android.server.oemlock.PersistentDataBlockLock)) {
            android.util.Slog.i(TAG, "Update OEM Unlock bit in pst partition to " + allowed);
            pdbmi.forceOemUnlockEnabled(allowed);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOemUnlockAllowedByAdmin() {
        return !android.os.UserManager.get(this.mContext).hasUserRestriction("no_factory_reset", android.os.UserHandle.SYSTEM);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceUserIsAdmin() {
        int userId = android.os.UserHandle.getCallingUserId();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (!android.os.UserManager.get(this.mContext).isUserAdmin(userId)) {
                throw new java.lang.SecurityException("Must be an admin user");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }
}
