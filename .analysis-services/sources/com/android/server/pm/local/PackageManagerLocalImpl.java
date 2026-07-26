package com.android.server.pm.local;

/* JADX INFO: loaded from: classes2.dex */
public class PackageManagerLocalImpl implements com.android.server.pm.PackageManagerLocal {
    private final com.android.server.pm.PackageManagerService mService;

    public PackageManagerLocalImpl(com.android.server.pm.PackageManagerService service) {
        this.mService = service;
    }

    @Override // com.android.server.pm.PackageManagerLocal
    public void reconcileSdkData(java.lang.String volumeUuid, java.lang.String packageName, java.util.List<java.lang.String> subDirNames, int userId, int appId, int previousAppId, java.lang.String seInfo, int flags) throws java.io.IOException {
        this.mService.reconcileSdkData(volumeUuid, packageName, subDirNames, userId, appId, previousAppId, seInfo, flags);
    }

    @Override // com.android.server.pm.PackageManagerLocal
    public com.android.server.pm.local.PackageManagerLocalImpl.UnfilteredSnapshotImpl withUnfilteredSnapshot() {
        return new com.android.server.pm.local.PackageManagerLocalImpl.UnfilteredSnapshotImpl(this.mService.snapshotComputer(false));
    }

    @Override // com.android.server.pm.PackageManagerLocal
    public com.android.server.pm.local.PackageManagerLocalImpl.FilteredSnapshotImpl withFilteredSnapshot() {
        return withFilteredSnapshot(android.os.Binder.getCallingUid(), android.os.Binder.getCallingUserHandle());
    }

    @Override // com.android.server.pm.PackageManagerLocal
    public com.android.server.pm.local.PackageManagerLocalImpl.FilteredSnapshotImpl withFilteredSnapshot(int callingUid, android.os.UserHandle user) {
        return new com.android.server.pm.local.PackageManagerLocalImpl.FilteredSnapshotImpl(callingUid, user, this.mService.snapshotComputer(false), null);
    }

    @Override // com.android.server.pm.PackageManagerLocal
    public void addOverrideSigningDetails(android.content.pm.SigningDetails oldSigningDetails, android.content.pm.SigningDetails newSigningDetails) {
        if (!android.os.Build.isDebuggable()) {
            throw new java.lang.SecurityException("This test API is only available on debuggable builds");
        }
        android.util.apk.ApkSignatureVerifier.addOverrideSigningDetails(oldSigningDetails, newSigningDetails);
    }

    @Override // com.android.server.pm.PackageManagerLocal
    public void removeOverrideSigningDetails(android.content.pm.SigningDetails oldSigningDetails) {
        if (!android.os.Build.isDebuggable()) {
            throw new java.lang.SecurityException("This test API is only available on debuggable builds");
        }
        android.util.apk.ApkSignatureVerifier.removeOverrideSigningDetails(oldSigningDetails);
    }

    @Override // com.android.server.pm.PackageManagerLocal
    public void clearOverrideSigningDetails() {
        if (!android.os.Build.isDebuggable()) {
            throw new java.lang.SecurityException("This test API is only available on debuggable builds");
        }
        android.util.apk.ApkSignatureVerifier.clearOverrideSigningDetails();
    }

    private static abstract class BaseSnapshotImpl implements java.lang.AutoCloseable {
        private boolean mClosed;
        protected com.android.server.pm.Computer mSnapshot;

        private BaseSnapshotImpl(com.android.server.pm.snapshot.PackageDataSnapshot snapshot) {
            this.mSnapshot = (com.android.server.pm.Computer) snapshot;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            this.mClosed = true;
            this.mSnapshot = null;
        }

        protected void checkClosed() {
            if (this.mClosed) {
                throw new java.lang.IllegalStateException("Snapshot already closed");
            }
        }
    }

    private static class UnfilteredSnapshotImpl extends com.android.server.pm.local.PackageManagerLocalImpl.BaseSnapshotImpl implements com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot {
        private java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mCachedUnmodifiableDisabledSystemPackageStates;
        private java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mCachedUnmodifiablePackageStates;
        private java.util.Map<java.lang.String, com.android.server.pm.pkg.SharedUserApi> mCachedUnmodifiableSharedUsers;

        private UnfilteredSnapshotImpl(com.android.server.pm.snapshot.PackageDataSnapshot snapshot) {
            super(snapshot);
        }

        @Override // com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot
        public com.android.server.pm.PackageManagerLocal.FilteredSnapshot filtered(int callingUid, android.os.UserHandle user) {
            return new com.android.server.pm.local.PackageManagerLocalImpl.FilteredSnapshotImpl(callingUid, user, this.mSnapshot, this);
        }

        @Override // com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot
        public java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> getPackageStates() {
            checkClosed();
            if (this.mCachedUnmodifiablePackageStates == null) {
                this.mCachedUnmodifiablePackageStates = java.util.Collections.unmodifiableMap(this.mSnapshot.getPackageStates());
            }
            return this.mCachedUnmodifiablePackageStates;
        }

        @Override // com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot
        public java.util.Map<java.lang.String, com.android.server.pm.pkg.SharedUserApi> getSharedUsers() {
            checkClosed();
            if (this.mCachedUnmodifiableSharedUsers == null) {
                this.mCachedUnmodifiableSharedUsers = java.util.Collections.unmodifiableMap(this.mSnapshot.getSharedUsers());
            }
            return this.mCachedUnmodifiableSharedUsers;
        }

        @Override // com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot
        public java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> getDisabledSystemPackageStates() {
            checkClosed();
            if (this.mCachedUnmodifiableDisabledSystemPackageStates == null) {
                this.mCachedUnmodifiableDisabledSystemPackageStates = java.util.Collections.unmodifiableMap(this.mSnapshot.getDisabledSystemPackageStates());
            }
            return this.mCachedUnmodifiableDisabledSystemPackageStates;
        }

        @Override // com.android.server.pm.local.PackageManagerLocalImpl.BaseSnapshotImpl, java.lang.AutoCloseable
        public void close() {
            super.close();
            this.mCachedUnmodifiablePackageStates = null;
            this.mCachedUnmodifiableDisabledSystemPackageStates = null;
        }
    }

    private static class FilteredSnapshotImpl extends com.android.server.pm.local.PackageManagerLocalImpl.BaseSnapshotImpl implements com.android.server.pm.PackageManagerLocal.FilteredSnapshot {
        private final int mCallingUid;
        private java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mFilteredPackageStates;
        private final com.android.server.pm.local.PackageManagerLocalImpl.UnfilteredSnapshotImpl mParentSnapshot;
        private final int mUserId;

        private FilteredSnapshotImpl(int callingUid, android.os.UserHandle user, com.android.server.pm.snapshot.PackageDataSnapshot snapshot, com.android.server.pm.local.PackageManagerLocalImpl.UnfilteredSnapshotImpl parentSnapshot) {
            super(snapshot);
            this.mCallingUid = callingUid;
            this.mUserId = user.getIdentifier();
            this.mParentSnapshot = parentSnapshot;
        }

        @Override // com.android.server.pm.local.PackageManagerLocalImpl.BaseSnapshotImpl
        protected void checkClosed() {
            if (this.mParentSnapshot != null) {
                this.mParentSnapshot.checkClosed();
            }
            super.checkClosed();
        }

        @Override // com.android.server.pm.local.PackageManagerLocalImpl.BaseSnapshotImpl, java.lang.AutoCloseable
        public void close() {
            super.close();
            this.mFilteredPackageStates = null;
        }

        @Override // com.android.server.pm.PackageManagerLocal.FilteredSnapshot
        public com.android.server.pm.pkg.PackageState getPackageState(java.lang.String packageName) {
            checkClosed();
            return this.mSnapshot.getPackageStateFiltered(packageName, this.mCallingUid, this.mUserId);
        }

        @Override // com.android.server.pm.PackageManagerLocal.FilteredSnapshot
        public java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> getPackageStates() {
            checkClosed();
            if (this.mFilteredPackageStates == null) {
                android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = this.mSnapshot.getPackageStates();
                android.util.ArrayMap<java.lang.String, com.android.server.pm.pkg.PackageState> filteredPackageStates = new android.util.ArrayMap<>();
                int size = packageStates.size();
                for (int index = 0; index < size; index++) {
                    com.android.server.pm.pkg.PackageStateInternal packageState = packageStates.valueAt(index);
                    if (!this.mSnapshot.shouldFilterApplication(packageState, this.mCallingUid, this.mUserId)) {
                        filteredPackageStates.put(packageStates.keyAt(index), packageState);
                    }
                }
                this.mFilteredPackageStates = java.util.Collections.unmodifiableMap(filteredPackageStates);
            }
            return this.mFilteredPackageStates;
        }
    }
}
