package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class PendingPackageBroadcasts {
    private final java.lang.Object mLock = new com.android.server.pm.PackageManagerTracedLock();
    final android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>>> mUidMap = new android.util.SparseArray<>(2);

    public boolean hasPackage(int userId, java.lang.String packageName) {
        boolean z;
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> packages = this.mUidMap.get(userId);
            z = packages != null && packages.containsKey(packageName);
        }
        return z;
    }

    public void put(int userId, java.lang.String packageName, java.util.ArrayList<java.lang.String> components) {
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> packages = getOrAllocate(userId);
            packages.put(packageName, components);
        }
    }

    public void addComponent(int userId, java.lang.String packageName, java.lang.String componentClassName) {
        synchronized (this.mLock) {
            java.util.ArrayList<java.lang.String> components = getOrAllocate(userId, packageName);
            if (!components.contains(componentClassName)) {
                components.add(componentClassName);
            }
        }
    }

    public void addComponents(int userId, java.lang.String packageName, java.util.List<java.lang.String> componentClassNames) {
        synchronized (this.mLock) {
            java.util.ArrayList<java.lang.String> components = getOrAllocate(userId, packageName);
            for (int index = 0; index < componentClassNames.size(); index++) {
                java.lang.String componentClassName = componentClassNames.get(index);
                if (!components.contains(componentClassName)) {
                    components.add(componentClassName);
                }
            }
        }
    }

    public void remove(int userId, java.lang.String packageName) {
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> packages = this.mUidMap.get(userId);
            if (packages != null) {
                packages.remove(packageName);
            }
        }
    }

    public void remove(int userId) {
        synchronized (this.mLock) {
            this.mUidMap.remove(userId);
        }
    }

    public android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>>> copiedMap() {
        android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>>> copy;
        synchronized (this.mLock) {
            copy = new android.util.SparseArray<>();
            for (int userIdIndex = 0; userIdIndex < this.mUidMap.size(); userIdIndex++) {
                android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> packages = this.mUidMap.valueAt(userIdIndex);
                android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> packagesCopy = new android.util.ArrayMap<>();
                for (int packagesIndex = 0; packagesIndex < packages.size(); packagesIndex++) {
                    packagesCopy.put(packages.keyAt(packagesIndex), new java.util.ArrayList<>(packages.valueAt(packagesIndex)));
                }
                copy.put(this.mUidMap.keyAt(userIdIndex), packagesCopy);
            }
        }
        return copy;
    }

    public void clear() {
        synchronized (this.mLock) {
            this.mUidMap.clear();
        }
    }

    private android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> getOrAllocate(int userId) {
        android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> map;
        synchronized (this.mLock) {
            map = this.mUidMap.get(userId);
            if (map == null) {
                map = new android.util.ArrayMap<>();
                this.mUidMap.put(userId, map);
            }
        }
        return map;
    }

    private java.util.ArrayList<java.lang.String> getOrAllocate(int userId, java.lang.String packageName) {
        java.util.ArrayList<java.lang.String> arrayListComputeIfAbsent;
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> map = this.mUidMap.get(userId);
            if (map == null) {
                map = new android.util.ArrayMap<>();
                this.mUidMap.put(userId, map);
            }
            arrayListComputeIfAbsent = map.computeIfAbsent(packageName, new java.util.function.Function() { // from class: com.android.server.pm.PendingPackageBroadcasts$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.pm.PendingPackageBroadcasts.lambda$getOrAllocate$0((java.lang.String) obj);
                }
            });
        }
        return arrayListComputeIfAbsent;
    }

    static /* synthetic */ java.util.ArrayList lambda$getOrAllocate$0(java.lang.String k) {
        return new java.util.ArrayList();
    }
}
