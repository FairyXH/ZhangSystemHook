package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class JobIdManager {
    public static int getJobIdForUserId(int minJobId, int maxJobId, int userId) {
        if (minJobId + userId > maxJobId) {
            throw new java.lang.RuntimeException("No job IDs available in the given range");
        }
        return minJobId + userId;
    }
}
