package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public interface AppOpsRestrictions {

    public interface AppOpsRestrictionRemovedListener {
        void onAppOpsRestrictionRemoved(int i);
    }

    boolean clearGlobalRestrictions(java.lang.Object obj);

    boolean clearUserRestrictions(java.lang.Object obj);

    boolean clearUserRestrictions(java.lang.Object obj, java.lang.Integer num);

    void dumpRestrictions(java.io.PrintWriter printWriter, int i, java.lang.String str, boolean z);

    boolean getGlobalRestriction(java.lang.Object obj, int i);

    boolean getUserRestriction(java.lang.Object obj, int i, int i2, java.lang.String str, java.lang.String str2, boolean z);

    android.os.PackageTagsList getUserRestrictionExclusions(java.lang.Object obj, int i);

    boolean hasGlobalRestrictions(java.lang.Object obj);

    boolean hasUserRestrictions(java.lang.Object obj);

    boolean setGlobalRestriction(java.lang.Object obj, int i, boolean z);

    boolean setUserRestriction(java.lang.Object obj, int i, int i2, boolean z, android.os.PackageTagsList packageTagsList);
}
