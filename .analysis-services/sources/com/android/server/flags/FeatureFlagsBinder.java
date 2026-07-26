package com.android.server.flags;

/* JADX INFO: loaded from: classes2.dex */
class FeatureFlagsBinder extends android.flags.IFeatureFlags.Stub {
    private final com.android.server.flags.DynamicFlagBinderDelegate mDynamicFlagDelegate;
    private final com.android.server.flags.FlagCache<java.lang.String> mFlagCache = new com.android.server.flags.FlagCache<>();
    private final com.android.server.flags.FlagOverrideStore mFlagStore;
    private final com.android.server.flags.FeatureFlagsService.PermissionsChecker mPermissionsChecker;
    private final com.android.server.flags.FlagsShellCommand mShellCommand;

    FeatureFlagsBinder(com.android.server.flags.FlagOverrideStore flagStore, com.android.server.flags.FlagsShellCommand shellCommand, com.android.server.flags.FeatureFlagsService.PermissionsChecker permissionsChecker) {
        this.mFlagStore = flagStore;
        this.mShellCommand = shellCommand;
        this.mDynamicFlagDelegate = new com.android.server.flags.DynamicFlagBinderDelegate(flagStore);
        this.mPermissionsChecker = permissionsChecker;
    }

    public void registerCallback(android.flags.IFeatureFlagsCallback callback) {
        this.mDynamicFlagDelegate.registerCallback(getCallingPid(), callback);
    }

    public void unregisterCallback(android.flags.IFeatureFlagsCallback callback) {
        this.mDynamicFlagDelegate.unregisterCallback(getCallingPid(), callback);
    }

    public java.util.List<android.flags.SyncableFlag> syncFlags(java.util.List<android.flags.SyncableFlag> incomingFlags) {
        android.flags.SyncableFlag outFlag;
        android.flags.SyncableFlag outFlag2;
        int pid = getCallingPid();
        java.util.List<android.flags.SyncableFlag> outputFlags = new java.util.ArrayList<>();
        boolean hasFullSyncPrivileges = false;
        java.lang.SecurityException permissionFailureException = null;
        try {
            assertSyncPermission();
            hasFullSyncPrivileges = true;
        } catch (java.lang.SecurityException e) {
            permissionFailureException = e;
        }
        for (android.flags.SyncableFlag sf : incomingFlags) {
            if (!hasFullSyncPrivileges && !com.android.internal.flags.CoreFlags.isCoreFlag(sf)) {
                throw permissionFailureException;
            }
            java.lang.String ns = sf.getNamespace();
            java.lang.String name = sf.getName();
            if (sf.isDynamic()) {
                outFlag2 = this.mDynamicFlagDelegate.syncDynamicFlag(pid, sf);
            } else {
                synchronized (this.mFlagCache) {
                    java.lang.String value = this.mFlagCache.getOrNull(ns, name);
                    if (value == null) {
                        java.lang.String overrideValue = android.os.Build.IS_USER ? null : this.mFlagStore.get(ns, name);
                        value = overrideValue != null ? overrideValue : sf.getValue();
                        this.mFlagCache.setIfChanged(ns, name, value);
                    }
                    outFlag = new android.flags.SyncableFlag(sf.getNamespace(), sf.getName(), value, false);
                }
                outFlag2 = outFlag;
            }
            outputFlags.add(outFlag2);
        }
        return outputFlags;
    }

    public void overrideFlag(android.flags.SyncableFlag flag) {
        assertWritePermission();
        this.mFlagStore.set(flag.getNamespace(), flag.getName(), flag.getValue());
    }

    public void resetFlag(android.flags.SyncableFlag flag) {
        assertWritePermission();
        this.mFlagStore.erase(flag.getNamespace(), flag.getName());
    }

    public java.util.List<android.flags.SyncableFlag> queryFlags(java.util.List<android.flags.SyncableFlag> incomingFlags) {
        java.lang.String value;
        assertSyncPermission();
        java.util.List<android.flags.SyncableFlag> outputFlags = new java.util.ArrayList<>();
        for (android.flags.SyncableFlag sf : incomingFlags) {
            java.lang.String ns = sf.getNamespace();
            java.lang.String name = sf.getName();
            java.lang.String storeValue = this.mFlagStore.get(ns, name);
            boolean overridden = storeValue != null;
            if (sf.isDynamic()) {
                value = this.mDynamicFlagDelegate.getFlagValue(ns, name, sf.getValue());
            } else {
                value = this.mFlagCache.getOrNull(ns, name);
                if (value == null) {
                    value = android.os.Build.IS_USER ? null : storeValue;
                    if (value == null) {
                        value = sf.getValue();
                    }
                }
            }
            outputFlags.add(new android.flags.SyncableFlag(sf.getNamespace(), sf.getName(), value, sf.isDynamic(), overridden));
        }
        return outputFlags;
    }

    private void assertSyncPermission() {
        this.mPermissionsChecker.assertSyncPermission();
        clearCallingIdentity();
    }

    private void assertWritePermission() {
        this.mPermissionsChecker.assertWritePermission();
        clearCallingIdentity();
    }

    @android.annotation.SystemApi
    public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
        java.io.FileOutputStream fout = new java.io.FileOutputStream(out.getFileDescriptor());
        java.io.FileOutputStream ferr = new java.io.FileOutputStream(err.getFileDescriptor());
        return this.mShellCommand.process(args, fout, ferr);
    }
}
