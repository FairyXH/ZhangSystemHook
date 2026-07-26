package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityTaskSupervisorWrapper {
    default com.android.server.wm.IActivityTaskSupervisorExt getExtImpl() {
        return new com.android.server.wm.IActivityTaskSupervisorExt() { // from class: com.android.server.wm.IActivityTaskSupervisorWrapper.1
        };
    }

    default android.content.pm.ResolveInfo resolveIntent(android.content.Intent intent, java.lang.String resolvedType, int userId, int flags, int filterCallingUid, int callingPid) {
        return new android.content.pm.ResolveInfo();
    }
}
