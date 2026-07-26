package com.android.server.utils.quota;

/* JADX INFO: loaded from: classes3.dex */
class UptcMap<T> {
    private final android.util.SparseArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, T>> mData = new android.util.SparseArrayMap<>();

    interface UptcDataConsumer<D> {
        void accept(int i, java.lang.String str, java.lang.String str2, D d);
    }

    UptcMap() {
    }

    public void add(int userId, java.lang.String packageName, java.lang.String tag, T obj) {
        android.util.ArrayMap<java.lang.String, T> data = (android.util.ArrayMap) this.mData.get(userId, packageName);
        if (data == null) {
            data = new android.util.ArrayMap<>();
            this.mData.add(userId, packageName, data);
        }
        data.put(tag, obj);
    }

    public void clear() {
        this.mData.clear();
    }

    public boolean contains(int userId, java.lang.String packageName) {
        return this.mData.contains(userId, packageName);
    }

    public boolean contains(int userId, java.lang.String packageName, java.lang.String tag) {
        android.util.ArrayMap<java.lang.String, T> data = (android.util.ArrayMap) this.mData.get(userId, packageName);
        return data != null && data.containsKey(tag);
    }

    public void delete(int userId) {
        this.mData.delete(userId);
    }

    public void delete(int userId, java.lang.String packageName, java.lang.String tag) {
        android.util.ArrayMap<java.lang.String, T> data = (android.util.ArrayMap) this.mData.get(userId, packageName);
        if (data != null) {
            data.remove(tag);
            if (data.size() == 0) {
                this.mData.delete(userId, packageName);
            }
        }
    }

    public android.util.ArrayMap<java.lang.String, T> delete(int userId, java.lang.String packageName) {
        return (android.util.ArrayMap) this.mData.delete(userId, packageName);
    }

    public android.util.ArrayMap<java.lang.String, T> get(int userId, java.lang.String packageName) {
        return (android.util.ArrayMap) this.mData.get(userId, packageName);
    }

    public T get(int userId, java.lang.String packageName, java.lang.String tag) {
        android.util.ArrayMap<java.lang.String, T> data = (android.util.ArrayMap) this.mData.get(userId, packageName);
        if (data != null) {
            return data.get(tag);
        }
        return null;
    }

    public T getOrCreate(int userId, java.lang.String packageName, java.lang.String tag, java.util.function.Function<java.lang.Void, T> creator) {
        android.util.ArrayMap<java.lang.String, T> data = (android.util.ArrayMap) this.mData.get(userId, packageName);
        if (data == null || !data.containsKey(tag)) {
            T val = creator.apply(null);
            add(userId, packageName, tag, val);
            return val;
        }
        return data.get(tag);
    }

    private int getUserIdAtIndex(int index) {
        return this.mData.keyAt(index);
    }

    private java.lang.String getPackageNameAtIndex(int userIndex, int packageIndex) {
        return (java.lang.String) this.mData.keyAt(userIndex, packageIndex);
    }

    private java.lang.String getTagAtIndex(int userIndex, int packageIndex, int tagIndex) {
        return (java.lang.String) ((android.util.ArrayMap) this.mData.valueAt(userIndex, packageIndex)).keyAt(tagIndex);
    }

    public int userCount() {
        return this.mData.numMaps();
    }

    public int packageCountForUser(int userId) {
        return this.mData.numElementsForKey(userId);
    }

    public int tagCountForUserAndPackage(int userId, java.lang.String packageName) {
        android.util.ArrayMap data = (android.util.ArrayMap) this.mData.get(userId, packageName);
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    public T valueAt(int userIndex, int packageIndex, int tagIndex) {
        android.util.ArrayMap<java.lang.String, T> data = (android.util.ArrayMap) this.mData.valueAt(userIndex, packageIndex);
        if (data != null) {
            return data.valueAt(tagIndex);
        }
        return null;
    }

    public void forEach(final java.util.function.Consumer<T> consumer) {
        this.mData.forEach(new java.util.function.Consumer() { // from class: com.android.server.utils.quota.UptcMap$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.utils.quota.UptcMap.lambda$forEach$0(consumer, (android.util.ArrayMap) obj);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void lambda$forEach$0(java.util.function.Consumer consumer, android.util.ArrayMap tagMap) {
        for (int i = tagMap.size() - 1; i >= 0; i--) {
            consumer.accept(tagMap.valueAt(i));
        }
    }

    public void forEach(com.android.server.utils.quota.UptcMap.UptcDataConsumer<T> consumer) {
        int uCount = userCount();
        for (int u = 0; u < uCount; u++) {
            int userId = getUserIdAtIndex(u);
            int pkgCount = packageCountForUser(userId);
            for (int p = 0; p < pkgCount; p++) {
                java.lang.String pkgName = getPackageNameAtIndex(u, p);
                int tagCount = tagCountForUserAndPackage(userId, pkgName);
                for (int t = 0; t < tagCount; t++) {
                    java.lang.String tag = getTagAtIndex(u, p, t);
                    consumer.accept(userId, pkgName, tag, get(userId, pkgName, tag));
                }
            }
        }
    }
}
