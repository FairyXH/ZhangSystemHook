package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class PackageList {
    private final android.util.ArrayMap<java.lang.String, com.android.internal.app.procstats.ProcessStats.ProcessStateHolder> mPkgList = new android.util.ArrayMap<>();
    private final com.android.server.am.ProcessRecord mProcess;

    PackageList(com.android.server.am.ProcessRecord app) {
        this.mProcess = app;
    }

    com.android.internal.app.procstats.ProcessStats.ProcessStateHolder put(java.lang.String key, com.android.internal.app.procstats.ProcessStats.ProcessStateHolder value) {
        com.android.internal.app.procstats.ProcessStats.ProcessStateHolder processStateHolderPut;
        synchronized (this) {
            this.mProcess.getWindowProcessController().addPackage(key);
            processStateHolderPut = this.mPkgList.put(key, value);
        }
        return processStateHolderPut;
    }

    void clear() {
        synchronized (this) {
            this.mPkgList.clear();
            this.mProcess.getWindowProcessController().clearPackageList();
        }
    }

    public int size() {
        int size;
        synchronized (this) {
            size = this.mPkgList.size();
        }
        return size;
    }

    boolean containsKey(java.lang.Object key) {
        boolean zContainsKey;
        synchronized (this) {
            zContainsKey = this.mPkgList.containsKey(key);
        }
        return zContainsKey;
    }

    com.android.internal.app.procstats.ProcessStats.ProcessStateHolder get(java.lang.String pkgName) {
        com.android.internal.app.procstats.ProcessStats.ProcessStateHolder processStateHolder;
        synchronized (this) {
            processStateHolder = this.mPkgList.get(pkgName);
        }
        return processStateHolder;
    }

    void forEachPackage(java.util.function.Consumer<java.lang.String> callback) {
        synchronized (this) {
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                callback.accept(this.mPkgList.keyAt(i));
            }
        }
    }

    void forEachPackage(java.util.function.BiConsumer<java.lang.String, com.android.internal.app.procstats.ProcessStats.ProcessStateHolder> callback) {
        synchronized (this) {
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                callback.accept(this.mPkgList.keyAt(i), this.mPkgList.valueAt(i));
            }
        }
    }

    <R> R searchEachPackage(java.util.function.Function<java.lang.String, R> callback) {
        synchronized (this) {
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                R r = callback.apply(this.mPkgList.keyAt(i));
                if (r != null) {
                    return r;
                }
            }
            return null;
        }
    }

    void forEachPackageProcessStats(java.util.function.Consumer<com.android.internal.app.procstats.ProcessStats.ProcessStateHolder> callback) {
        synchronized (this) {
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                callback.accept(this.mPkgList.valueAt(i));
            }
        }
    }

    android.util.ArrayMap<java.lang.String, com.android.internal.app.procstats.ProcessStats.ProcessStateHolder> getPackageListLocked() {
        return this.mPkgList;
    }

    java.lang.String[] getPackageList() {
        synchronized (this) {
            int size = this.mPkgList.size();
            if (size == 0) {
                return null;
            }
            java.lang.String[] list = new java.lang.String[size];
            for (int i = 0; i < size; i++) {
                list[i] = this.mPkgList.keyAt(i);
            }
            return list;
        }
    }

    java.util.List<android.content.pm.VersionedPackage> getPackageListWithVersionCode() {
        synchronized (this) {
            int size = this.mPkgList.size();
            if (size == 0) {
                return null;
            }
            java.util.List<android.content.pm.VersionedPackage> list = new java.util.ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(new android.content.pm.VersionedPackage(this.mPkgList.keyAt(i), this.mPkgList.valueAt(i).appVersion));
            }
            return list;
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (this) {
            pw.print(prefix);
            pw.print("packageList={");
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    pw.print(", ");
                }
                pw.print(this.mPkgList.keyAt(i));
            }
            pw.println("}");
        }
    }

    public java.lang.String keyAt(int index) {
        java.lang.String strKeyAt;
        synchronized (this) {
            strKeyAt = this.mPkgList.keyAt(index);
        }
        return strKeyAt;
    }
}
