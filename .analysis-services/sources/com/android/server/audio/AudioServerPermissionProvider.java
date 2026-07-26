package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class AudioServerPermissionProvider {
    static final java.lang.String[] MONITORED_PERMS = new java.lang.String[4];
    private com.android.media.permission.INativePermissionController mDest;
    private final java.util.Map<java.lang.Integer, java.util.Set<java.lang.String>> mPackageMap;
    private final java.util.function.BiPredicate<java.lang.Integer, java.lang.String> mPermissionPredicate;
    private final java.util.function.Supplier<int[]> mUserIdSupplier;
    private final java.lang.Object mLock = new java.lang.Object();
    private final int[][] mPermMap = new int[4][];
    private boolean mIsUpdateDeferred = true;

    static {
        MONITORED_PERMS[0] = "android.permission.MODIFY_AUDIO_ROUTING";
        MONITORED_PERMS[1] = "android.permission.MODIFY_PHONE_STATE";
        MONITORED_PERMS[3] = "android.permission.RECORD_AUDIO";
        MONITORED_PERMS[2] = "android.permission.CALL_AUDIO_INTERCEPTION";
    }

    public AudioServerPermissionProvider(java.util.Collection<com.android.server.pm.pkg.PackageState> appInfos, java.util.function.BiPredicate<java.lang.Integer, java.lang.String> permissionPredicate, java.util.function.Supplier<int[]> userIdSupplier) {
        for (int i = 0; i < 4; i++) {
            java.util.Objects.requireNonNull(MONITORED_PERMS[i]);
        }
        this.mUserIdSupplier = userIdSupplier;
        this.mPermissionPredicate = permissionPredicate;
        this.mPackageMap = generatePackageMappings(appInfos);
    }

    public void onServiceStart(com.android.media.permission.INativePermissionController pc) {
        if (pc == null) {
            return;
        }
        synchronized (this.mLock) {
            this.mDest = pc;
            resetNativePackageState();
            for (byte i = 0; i < 4; i = (byte) (i + 1)) {
                try {
                    if (this.mIsUpdateDeferred) {
                        this.mPermMap[i] = getUidsHoldingPerm(MONITORED_PERMS[i]);
                    }
                    this.mDest.populatePermissionState(i, this.mPermMap[i]);
                } catch (android.os.RemoteException e) {
                    this.mDest = null;
                }
            }
            this.mIsUpdateDeferred = false;
        }
    }

    public void onModifyPackageState(int uid, java.lang.String packageName, boolean isRemove) {
        java.util.Set<java.lang.String> packages;
        int uid2 = android.os.UserHandle.getAppId(uid);
        synchronized (this.mLock) {
            if (!isRemove) {
                packages = this.mPackageMap.computeIfAbsent(java.lang.Integer.valueOf(uid2), new java.util.function.Function() { // from class: com.android.server.audio.AudioServerPermissionProvider$$ExternalSyntheticLambda5
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.audio.AudioServerPermissionProvider.lambda$onModifyPackageState$0((java.lang.Integer) obj);
                    }
                });
                packages.add(packageName);
            } else {
                packages = this.mPackageMap.get(java.lang.Integer.valueOf(uid2));
                if (packages != null) {
                    packages.remove(packageName);
                    if (packages.isEmpty()) {
                        this.mPackageMap.remove(java.lang.Integer.valueOf(uid2));
                    }
                }
            }
            if (this.mDest == null) {
                return;
            }
            com.android.media.permission.UidPackageState state = new com.android.media.permission.UidPackageState();
            state.uid = uid2;
            state.packageNames = packages != null ? java.util.List.copyOf(packages) : java.util.Collections.emptyList();
            try {
                this.mDest.updatePackagesForUid(state);
            } catch (android.os.RemoteException e) {
                this.mDest = null;
            }
        }
    }

    static /* synthetic */ java.util.Set lambda$onModifyPackageState$0(java.lang.Integer unused) {
        return new android.util.ArraySet(1);
    }

    public void onPermissionStateChanged() {
        synchronized (this.mLock) {
            if (this.mDest == null) {
                this.mIsUpdateDeferred = true;
                return;
            }
            for (byte i = 0; i < 4; i = (byte) (i + 1)) {
                try {
                    int[] newPerms = getUidsHoldingPerm(MONITORED_PERMS[i]);
                    if (!java.util.Arrays.equals(newPerms, this.mPermMap[i])) {
                        this.mPermMap[i] = newPerms;
                        this.mDest.populatePermissionState(i, newPerms);
                    }
                } catch (android.os.RemoteException e) {
                    this.mDest = null;
                    this.mIsUpdateDeferred = true;
                }
            }
        }
    }

    private void resetNativePackageState() {
        if (this.mDest == null) {
            return;
        }
        java.util.List<com.android.media.permission.UidPackageState> states = this.mPackageMap.entrySet().stream().map(new java.util.function.Function() { // from class: com.android.server.audio.AudioServerPermissionProvider$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.audio.AudioServerPermissionProvider.lambda$resetNativePackageState$1((java.util.Map.Entry) obj);
            }
        }).toList();
        try {
            this.mDest.populatePackagesForUids(states);
        } catch (android.os.RemoteException e) {
            this.mDest = null;
        }
    }

    static /* synthetic */ com.android.media.permission.UidPackageState lambda$resetNativePackageState$1(java.util.Map.Entry entry) {
        com.android.media.permission.UidPackageState state = new com.android.media.permission.UidPackageState();
        state.uid = ((java.lang.Integer) entry.getKey()).intValue();
        state.packageNames = java.util.List.copyOf((java.util.Collection) entry.getValue());
        return state;
    }

    private int[] getUidsHoldingPerm(java.lang.String perm) {
        android.util.IntArray acc = new android.util.IntArray();
        for (int userId : this.mUserIdSupplier.get()) {
            java.util.Iterator<java.lang.Integer> it = this.mPackageMap.keySet().iterator();
            while (it.hasNext()) {
                int appId = it.next().intValue();
                int uid = android.os.UserHandle.getUid(userId, appId);
                if (this.mPermissionPredicate.test(java.lang.Integer.valueOf(uid), perm)) {
                    acc.add(uid);
                }
            }
        }
        int[] unwrapped = acc.toArray();
        java.util.Arrays.sort(unwrapped);
        return unwrapped;
    }

    private static java.util.Map<java.lang.Integer, java.util.Set<java.lang.String>> generatePackageMappings(java.util.Collection<com.android.server.pm.pkg.PackageState> appInfos) {
        java.util.stream.Collector<com.android.server.pm.pkg.PackageState, java.lang.Object, java.util.Set<java.lang.String>> reducer = java.util.stream.Collectors.mapping(new java.util.function.Function() { // from class: com.android.server.audio.AudioServerPermissionProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.pm.pkg.PackageState) obj).getPackageName();
            }
        }, java.util.stream.Collectors.toCollection(new java.util.function.Supplier() { // from class: com.android.server.audio.AudioServerPermissionProvider$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.audio.AudioServerPermissionProvider.lambda$generatePackageMappings$3();
            }
        }));
        return (java.util.Map) appInfos.stream().collect(java.util.stream.Collectors.groupingBy(new java.util.function.Function() { // from class: com.android.server.audio.AudioServerPermissionProvider$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(((com.android.server.pm.pkg.PackageState) obj).getAppId());
            }
        }, new java.util.function.Supplier() { // from class: com.android.server.audio.AudioServerPermissionProvider$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return new java.util.HashMap();
            }
        }, reducer));
    }

    static /* synthetic */ android.util.ArraySet lambda$generatePackageMappings$3() {
        return new android.util.ArraySet(1);
    }
}
