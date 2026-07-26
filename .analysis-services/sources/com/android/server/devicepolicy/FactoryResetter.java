package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
@java.lang.Deprecated
public final class FactoryResetter {
    private static final java.lang.String TAG = com.android.server.devicepolicy.FactoryResetter.class.getSimpleName();
    private final android.content.Context mContext;
    private final boolean mForce;
    private final java.lang.String mReason;
    private final android.app.admin.DevicePolicySafetyChecker mSafetyChecker;
    private final boolean mShutdown;
    private final boolean mWipeAdoptableStorage;
    private final boolean mWipeEuicc;
    private final boolean mWipeFactoryResetProtection;

    public boolean factoryReset() throws java.io.IOException {
        com.android.internal.util.Preconditions.checkCallAuthorization(this.mContext.checkCallingOrSelfPermission("android.permission.MASTER_CLEAR") == 0);
        com.android.server.FactoryResetter.setFactoryResetting(this.mContext);
        if (this.mSafetyChecker == null) {
            factoryResetInternalUnchecked();
            return true;
        }
        com.android.internal.os.IResultReceiver.Stub stub = new com.android.internal.os.IResultReceiver.Stub() { // from class: com.android.server.devicepolicy.FactoryResetter.1
            public void send(int resultCode, android.os.Bundle resultData) throws android.os.RemoteException {
                com.android.server.utils.Slogf.i(com.android.server.devicepolicy.FactoryResetter.TAG, "Factory reset confirmed by %s, proceeding", com.android.server.devicepolicy.FactoryResetter.this.mSafetyChecker);
                try {
                    com.android.server.devicepolicy.FactoryResetter.this.factoryResetInternalUnchecked();
                } catch (java.io.IOException e) {
                    com.android.server.utils.Slogf.wtf(com.android.server.devicepolicy.FactoryResetter.TAG, e, "IOException calling underlying systems", new java.lang.Object[0]);
                }
            }
        };
        com.android.server.utils.Slogf.i(TAG, "Delaying factory reset until %s confirms", this.mSafetyChecker);
        this.mSafetyChecker.onFactoryReset(stub);
        return false;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder("FactoryResetter[");
        if (this.mReason == null) {
            builder.append("no_reason");
        } else {
            builder.append("reason='").append(this.mReason).append("'");
        }
        if (this.mSafetyChecker != null) {
            builder.append(",hasSafetyChecker");
        }
        if (this.mShutdown) {
            builder.append(",shutdown");
        }
        if (this.mForce) {
            builder.append(",force");
        }
        if (this.mWipeEuicc) {
            builder.append(",wipeEuicc");
        }
        if (this.mWipeAdoptableStorage) {
            builder.append(",wipeAdoptableStorage");
        }
        if (this.mWipeFactoryResetProtection) {
            builder.append(",ipeFactoryResetProtection");
        }
        return builder.append(']').toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void factoryResetInternalUnchecked() throws java.io.IOException {
        com.android.server.utils.Slogf.i(TAG, "factoryReset(): reason=%s, shutdown=%b, force=%b, wipeEuicc=%b, wipeAdoptableStorage=%b, wipeFRP=%b", this.mReason, java.lang.Boolean.valueOf(this.mShutdown), java.lang.Boolean.valueOf(this.mForce), java.lang.Boolean.valueOf(this.mWipeEuicc), java.lang.Boolean.valueOf(this.mWipeAdoptableStorage), java.lang.Boolean.valueOf(this.mWipeFactoryResetProtection));
        android.os.UserManager um = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        if (!this.mForce && um.hasUserRestriction("no_factory_reset")) {
            throw new java.lang.SecurityException("Factory reset is not allowed for this user.");
        }
        if (this.mWipeFactoryResetProtection) {
            android.service.persistentdata.PersistentDataBlockManager manager = (android.service.persistentdata.PersistentDataBlockManager) this.mContext.getSystemService(android.service.persistentdata.PersistentDataBlockManager.class);
            if (manager != null) {
                com.android.server.utils.Slogf.w(TAG, "Wiping factory reset protection");
                manager.wipe();
            } else {
                com.android.server.utils.Slogf.w(TAG, "No need to wipe factory reset protection");
            }
        }
        if (this.mWipeAdoptableStorage) {
            com.android.server.utils.Slogf.w(TAG, "Wiping adoptable storage");
            android.os.storage.StorageManager sm = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
            sm.wipeAdoptableDisks();
        }
        android.os.RecoverySystem.rebootWipeUserData(this.mContext, this.mShutdown, this.mReason, this.mForce, this.mWipeEuicc);
    }

    private FactoryResetter(com.android.server.devicepolicy.FactoryResetter.Builder builder) {
        this.mContext = builder.mContext;
        this.mSafetyChecker = builder.mSafetyChecker;
        this.mReason = builder.mReason;
        this.mShutdown = builder.mShutdown;
        this.mForce = builder.mForce;
        this.mWipeEuicc = builder.mWipeEuicc;
        this.mWipeAdoptableStorage = builder.mWipeAdoptableStorage;
        this.mWipeFactoryResetProtection = builder.mWipeFactoryResetProtection;
    }

    public static com.android.server.devicepolicy.FactoryResetter.Builder newBuilder(android.content.Context context) {
        return new com.android.server.devicepolicy.FactoryResetter.Builder(context);
    }

    public static final class Builder {
        private final android.content.Context mContext;
        private boolean mForce;
        private java.lang.String mReason;
        private android.app.admin.DevicePolicySafetyChecker mSafetyChecker;
        private boolean mShutdown;
        private boolean mWipeAdoptableStorage;
        private boolean mWipeEuicc;
        private boolean mWipeFactoryResetProtection;

        private Builder(android.content.Context context) {
            this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        }

        public com.android.server.devicepolicy.FactoryResetter.Builder setSafetyChecker(android.app.admin.DevicePolicySafetyChecker safetyChecker) {
            this.mSafetyChecker = safetyChecker;
            return this;
        }

        public com.android.server.devicepolicy.FactoryResetter.Builder setReason(java.lang.String reason) {
            this.mReason = (java.lang.String) java.util.Objects.requireNonNull(reason);
            return this;
        }

        public com.android.server.devicepolicy.FactoryResetter.Builder setShutdown(boolean value) {
            this.mShutdown = value;
            return this;
        }

        public com.android.server.devicepolicy.FactoryResetter.Builder setForce(boolean value) {
            this.mForce = value;
            return this;
        }

        public com.android.server.devicepolicy.FactoryResetter.Builder setWipeEuicc(boolean value) {
            this.mWipeEuicc = value;
            return this;
        }

        public com.android.server.devicepolicy.FactoryResetter.Builder setWipeAdoptableStorage(boolean value) {
            this.mWipeAdoptableStorage = value;
            return this;
        }

        public com.android.server.devicepolicy.FactoryResetter.Builder setWipeFactoryResetProtection(boolean value) {
            this.mWipeFactoryResetProtection = value;
            return this;
        }

        public com.android.server.devicepolicy.FactoryResetter build() {
            return new com.android.server.devicepolicy.FactoryResetter(this);
        }
    }
}
