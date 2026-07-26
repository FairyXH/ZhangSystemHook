package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public interface IUsageStatsServiceExt {
    default android.os.HandlerThread getBackgroundHandlerThread() {
        return null;
    }
}
