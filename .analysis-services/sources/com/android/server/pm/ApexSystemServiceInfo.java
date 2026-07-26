package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class ApexSystemServiceInfo implements java.lang.Comparable<com.android.server.pm.ApexSystemServiceInfo> {
    final int mInitOrder;
    final java.lang.String mJarPath;
    final java.lang.String mName;

    public ApexSystemServiceInfo(java.lang.String name, java.lang.String jarPath, int initOrder) {
        this.mName = name;
        this.mJarPath = jarPath;
        this.mInitOrder = initOrder;
    }

    public java.lang.String getName() {
        return this.mName;
    }

    public java.lang.String getJarPath() {
        return this.mJarPath;
    }

    public int getInitOrder() {
        return this.mInitOrder;
    }

    @Override // java.lang.Comparable
    public int compareTo(com.android.server.pm.ApexSystemServiceInfo other) {
        if (this.mInitOrder == other.mInitOrder) {
            return this.mName.compareTo(other.mName);
        }
        return -java.lang.Integer.compare(this.mInitOrder, other.mInitOrder);
    }
}
