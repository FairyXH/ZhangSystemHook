package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class LogMteState {
    public static void register(android.content.Context context) {
        ((android.app.StatsManager) context.getSystemService(android.app.StatsManager.class)).setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.MTE_STATE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.LogMteState.1
            public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
                int i;
                if (atomTag != 10181) {
                    throw new java.lang.UnsupportedOperationException("Unknown tagId=" + atomTag);
                }
                if (com.android.internal.os.Zygote.nativeSupportsMemoryTagging()) {
                    i = 2;
                } else {
                    i = 1;
                }
                data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.MTE_STATE, i));
                return 0;
            }
        });
    }
}
