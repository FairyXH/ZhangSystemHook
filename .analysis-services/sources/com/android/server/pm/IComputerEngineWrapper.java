package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IComputerEngineWrapper {
    default java.util.List<android.content.pm.ResolveInfo> filterIfNotSystemUser(java.util.List<android.content.pm.ResolveInfo> resolveInfos, int userId) {
        return resolveInfos;
    }
}
