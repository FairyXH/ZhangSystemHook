package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class SharedUserSetting extends com.android.server.pm.SettingBase implements com.android.server.pm.pkg.SharedUserApi {
    int mAppId;
    final com.android.server.utils.WatchedArraySet<com.android.server.pm.PackageSetting> mDisabledPackages;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArraySet<com.android.server.pm.PackageSetting>> mDisabledPackagesSnapshot;
    private final com.android.server.utils.Watcher mObserver;
    private final com.android.server.utils.WatchedArraySet<com.android.server.pm.PackageSetting> mPackages;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArraySet<com.android.server.pm.PackageSetting>> mPackagesSnapshot;
    private final com.android.server.utils.SnapshotCache<com.android.server.pm.SharedUserSetting> mSnapshot;
    final java.lang.String name;
    final android.util.ArrayMap<java.lang.String, com.android.internal.pm.pkg.component.ParsedProcess> processes;
    int seInfoTargetSdkVersion;
    final com.android.server.pm.PackageSignatures signatures;
    java.lang.Boolean signaturesChanged;
    int uidFlags;
    int uidPrivateFlags;

    private com.android.server.utils.SnapshotCache<com.android.server.pm.SharedUserSetting> makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.SharedUserSetting>(this, this) { // from class: com.android.server.pm.SharedUserSetting.2
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.SharedUserSetting createSnapshot() {
                return new com.android.server.pm.SharedUserSetting();
            }
        };
    }

    SharedUserSetting(java.lang.String _name, int _pkgFlags, int _pkgPrivateFlags) {
        super(_pkgFlags, _pkgPrivateFlags);
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.SharedUserSetting.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.SharedUserSetting.this.onChanged();
            }
        };
        this.signatures = new com.android.server.pm.PackageSignatures();
        this.uidFlags = _pkgFlags;
        this.uidPrivateFlags = _pkgPrivateFlags;
        this.name = _name;
        this.seInfoTargetSdkVersion = 10000;
        this.mPackages = new com.android.server.utils.WatchedArraySet<>();
        this.mPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPackages, this.mPackages, "SharedUserSetting.packages");
        this.mDisabledPackages = new com.android.server.utils.WatchedArraySet<>();
        this.mDisabledPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mDisabledPackages, this.mDisabledPackages, "SharedUserSetting.mDisabledPackages");
        this.processes = new android.util.ArrayMap<>();
        registerObservers();
        this.mSnapshot = makeCache();
    }

    private SharedUserSetting(com.android.server.pm.SharedUserSetting orig) {
        super(orig);
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.SharedUserSetting.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.SharedUserSetting.this.onChanged();
            }
        };
        this.signatures = new com.android.server.pm.PackageSignatures();
        this.name = orig.name;
        this.mAppId = orig.mAppId;
        this.uidFlags = orig.uidFlags;
        this.uidPrivateFlags = orig.uidPrivateFlags;
        this.mPackages = orig.mPackagesSnapshot.snapshot();
        this.mPackagesSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mDisabledPackages = orig.mDisabledPackagesSnapshot.snapshot();
        this.mDisabledPackagesSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.signatures.mSigningDetails = orig.signatures.mSigningDetails;
        this.signaturesChanged = orig.signaturesChanged;
        this.processes = new android.util.ArrayMap<>(orig.processes);
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    private void registerObservers() {
        this.mPackages.registerObserver(this.mObserver);
        this.mDisabledPackages.registerObserver(this.mObserver);
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.SharedUserSetting snapshot() {
        return this.mSnapshot.snapshot();
    }

    public java.lang.String toString() {
        return "SharedUserSetting{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + this.name + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mAppId + "}";
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.mAppId);
        proto.write(1138166333442L, this.name);
        proto.end(token);
    }

    void addProcesses(java.util.Map<java.lang.String, com.android.internal.pm.pkg.component.ParsedProcess> newProcs) {
        if (newProcs != null) {
            for (java.lang.String key : newProcs.keySet()) {
                com.android.internal.pm.pkg.component.ParsedProcess newProc = newProcs.get(key);
                com.android.internal.pm.pkg.component.ParsedProcess proc = this.processes.get(newProc.getName());
                if (proc == null) {
                    this.processes.put(newProc.getName(), new com.android.internal.pm.pkg.component.ParsedProcessImpl(newProc));
                } else {
                    com.android.internal.pm.pkg.component.ComponentMutateUtils.addStateFrom(proc, newProc);
                }
            }
            onChanged();
        }
    }

    boolean removePackage(com.android.server.pm.PackageSetting packageSetting) {
        if (!this.mPackages.remove(packageSetting)) {
            return false;
        }
        if ((getFlags() & packageSetting.getFlags()) != 0) {
            int aggregatedFlags = this.uidFlags;
            for (int i = 0; i < this.mPackages.size(); i++) {
                com.android.server.pm.PackageSetting ps = this.mPackages.valueAt(i);
                aggregatedFlags |= ps.getFlags();
            }
            setFlags(aggregatedFlags);
        }
        int aggregatedFlags2 = getPrivateFlags();
        if ((aggregatedFlags2 & packageSetting.getPrivateFlags()) != 0) {
            int aggregatedPrivateFlags = this.uidPrivateFlags;
            for (int i2 = 0; i2 < this.mPackages.size(); i2++) {
                com.android.server.pm.PackageSetting ps2 = this.mPackages.valueAt(i2);
                aggregatedPrivateFlags |= ps2.getPrivateFlags();
            }
            setPrivateFlags(aggregatedPrivateFlags);
        }
        updateProcesses();
        onChanged();
        return true;
    }

    void addPackage(com.android.server.pm.PackageSetting packageSetting) {
        if (this.mPackages.size() == 0 && packageSetting.getPkg() != null) {
            this.seInfoTargetSdkVersion = packageSetting.getPkg().getTargetSdkVersion();
        }
        if (this.mPackages.add(packageSetting)) {
            setFlags(getFlags() | packageSetting.getFlags());
            setPrivateFlags(getPrivateFlags() | packageSetting.getPrivateFlags());
            onChanged();
        }
        if (packageSetting.getPkg() != null) {
            addProcesses(packageSetting.getPkg().getProcesses());
        }
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public java.util.List<com.android.server.pm.pkg.AndroidPackage> getPackages() {
        if (this.mPackages == null || this.mPackages.size() == 0) {
            return java.util.Collections.emptyList();
        }
        java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> pkgList = new java.util.ArrayList<>(this.mPackages.size());
        for (int i = 0; i < this.mPackages.size(); i++) {
            com.android.server.pm.PackageSetting ps = this.mPackages.valueAt(i);
            if (ps != null && ps.getPkg() != null) {
                pkgList.add(ps.getPkg());
            }
        }
        return pkgList;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public boolean isPrivileged() {
        return (getPrivateFlags() & 8) != 0;
    }

    public boolean isSingleUser() {
        if (this.mPackages.size() != 1 || this.mDisabledPackages.size() > 1) {
            return false;
        }
        if (this.mDisabledPackages.size() != 1) {
            return true;
        }
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = this.mDisabledPackages.valueAt(0).getPkg();
        return pkg != null && pkg.isLeavingSharedUser();
    }

    public void fixSeInfoLocked() {
        if (this.mPackages == null || this.mPackages.size() == 0) {
            return;
        }
        for (int i = 0; i < this.mPackages.size(); i++) {
            com.android.server.pm.PackageSetting ps = this.mPackages.valueAt(i);
            if (ps != null && ps.getPkg() != null && ps.getPkg().getTargetSdkVersion() >= 0 && ps.getPkg().getTargetSdkVersion() < this.seInfoTargetSdkVersion) {
                this.seInfoTargetSdkVersion = ps.getPkg().getTargetSdkVersion();
                onChanged();
            }
        }
        for (int i2 = 0; i2 < this.mPackages.size(); i2++) {
            com.android.server.pm.PackageSetting ps2 = this.mPackages.valueAt(i2);
            if (ps2 != null && ps2.getPkg() != null) {
                boolean isPrivileged = isPrivileged() | ps2.isPrivileged();
                ps2.getPkgState().setOverrideSeInfo(com.android.server.pm.SELinuxMMAC.getSeInfo((com.android.server.pm.pkg.PackageState) ps2, (com.android.server.pm.pkg.AndroidPackage) ps2.getPkg(), isPrivileged, this.seInfoTargetSdkVersion));
                onChanged();
            }
        }
    }

    public void updateProcesses() {
        this.processes.clear();
        for (int i = this.mPackages.size() - 1; i >= 0; i--) {
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = this.mPackages.valueAt(i).getPkg();
            if (pkg != null) {
                addProcesses(pkg.getProcesses());
            }
        }
    }

    public int[] getNotInstalledUserIds() {
        int[] excludedUserIds = null;
        for (int i = 0; i < this.mPackages.size(); i++) {
            com.android.server.pm.PackageSetting ps = this.mPackages.valueAt(i);
            int[] userIds = ps.getNotInstalledUserIds();
            if (excludedUserIds == null) {
                excludedUserIds = userIds;
            } else {
                int[] excludedUserIds2 = excludedUserIds;
                for (int userId : excludedUserIds) {
                    if (!com.android.internal.util.ArrayUtils.contains(userIds, userId)) {
                        excludedUserIds2 = com.android.internal.util.ArrayUtils.removeInt(excludedUserIds2, userId);
                    }
                }
                excludedUserIds = excludedUserIds2;
            }
        }
        return excludedUserIds == null ? libcore.util.EmptyArray.INT : excludedUserIds;
    }

    public com.android.server.pm.SharedUserSetting updateFrom(com.android.server.pm.SharedUserSetting sharedUser) {
        super.copySettingBase(sharedUser);
        this.mAppId = sharedUser.mAppId;
        this.uidFlags = sharedUser.uidFlags;
        this.uidPrivateFlags = sharedUser.uidPrivateFlags;
        this.seInfoTargetSdkVersion = sharedUser.seInfoTargetSdkVersion;
        this.mPackages.clear();
        this.mPackages.addAll(sharedUser.mPackages);
        this.signaturesChanged = sharedUser.signaturesChanged;
        if (sharedUser.processes != null) {
            int numProcs = sharedUser.processes.size();
            this.processes.clear();
            this.processes.ensureCapacity(numProcs);
            for (int i = 0; i < numProcs; i++) {
                com.android.internal.pm.pkg.component.ParsedProcess proc = new com.android.internal.pm.pkg.component.ParsedProcessImpl(sharedUser.processes.valueAt(i));
                this.processes.put(proc.getName(), proc);
            }
        } else {
            this.processes.clear();
        }
        onChanged();
        return this;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public java.lang.String getName() {
        return this.name;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public int getAppId() {
        return this.mAppId;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public int getUidFlags() {
        return this.uidFlags;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public int getPrivateUidFlags() {
        return this.uidPrivateFlags;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public int getSeInfoTargetSdkVersion() {
        return this.seInfoTargetSdkVersion;
    }

    public com.android.server.utils.WatchedArraySet<com.android.server.pm.PackageSetting> getPackageSettings() {
        return this.mPackages;
    }

    public com.android.server.utils.WatchedArraySet<com.android.server.pm.PackageSetting> getDisabledPackageSettings() {
        return this.mDisabledPackages;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> getPackageStates() {
        return this.mPackages.untrackedStorage();
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> getDisabledPackageStates() {
        return this.mDisabledPackages.untrackedStorage();
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public android.content.pm.SigningDetails getSigningDetails() {
        return this.signatures.mSigningDetails;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public android.util.ArrayMap<java.lang.String, com.android.internal.pm.pkg.component.ParsedProcess> getProcesses() {
        return this.processes;
    }

    @Override // com.android.server.pm.pkg.SharedUserApi
    public com.android.server.pm.permission.LegacyPermissionState getSharedUserLegacyPermissionState() {
        return super.getLegacyPermissionState();
    }
}
