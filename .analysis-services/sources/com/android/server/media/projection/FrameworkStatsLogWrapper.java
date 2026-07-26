package com.android.server.media.projection;

/* JADX INFO: loaded from: classes2.dex */
public class FrameworkStatsLogWrapper {
    public void writeStateChanged(int code, int sessionId, int state, int previousState, int hostUid, int targetUid, int timeSinceLastActive, int creationSource) {
        com.android.internal.util.FrameworkStatsLog.write(code, sessionId, state, previousState, hostUid, targetUid, timeSinceLastActive, creationSource);
    }

    public void writeTargetChanged(int code, int sessionId, int targetType, int hostUid, int targetUid, int windowingMode) {
        com.android.internal.util.FrameworkStatsLog.write(code, sessionId, targetType, hostUid, targetUid, windowingMode);
    }
}
