package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public interface IJobStatusWrapper {
    default com.android.server.job.controllers.IJobStatusExt getExtImpl() {
        return new com.android.server.job.controllers.IJobStatusExt() { // from class: com.android.server.job.controllers.IJobStatusWrapper.1
        };
    }

    default boolean hasConstraint(int constraint) {
        return false;
    }
}
