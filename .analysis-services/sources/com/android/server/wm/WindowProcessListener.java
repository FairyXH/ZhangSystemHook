package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface WindowProcessListener {
    void appDied(java.lang.String str);

    void clearProfilerIfNeeded();

    void dumpDebug(android.util.proto.ProtoOutputStream protoOutputStream, long j);

    long getCpuTime();

    boolean isCached();

    boolean isRemoved();

    void onStartActivity(int i, boolean z, java.lang.String str, long j);

    void setPendingUiClean(boolean z);

    void setPendingUiCleanAndForceProcessStateUpTo(int i);

    void setRunningRemoteAnimation(boolean z);

    void updateProcessInfo(boolean z, boolean z2, boolean z3);

    void updateServiceConnectionActivities();
}
