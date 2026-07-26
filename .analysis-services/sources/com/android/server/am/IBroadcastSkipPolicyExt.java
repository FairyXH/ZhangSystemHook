package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastSkipPolicyExt {
    default java.lang.String shouldSkipMessage(com.android.server.am.BroadcastRecord r, com.android.server.am.BroadcastFilter filter) {
        return null;
    }

    default java.lang.String shouldSkipMessage(com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo filter) {
        return null;
    }
}
