package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public interface StateChangedListener {
    void onControllerStateChanged(android.util.ArraySet<com.android.server.job.controllers.JobStatus> arraySet);

    void onDeviceIdleStateChanged(boolean z);

    void onNetworkChanged(com.android.server.job.controllers.JobStatus jobStatus, android.net.Network network);

    void onRestrictedBucketChanged(java.util.List<com.android.server.job.controllers.JobStatus> list);

    void onRestrictionStateChanged(com.android.server.job.restrictions.JobRestriction jobRestriction, boolean z);

    void onRunJobNow(com.android.server.job.controllers.JobStatus jobStatus);
}
