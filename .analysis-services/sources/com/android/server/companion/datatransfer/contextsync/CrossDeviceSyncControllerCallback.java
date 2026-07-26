package com.android.server.companion.datatransfer.contextsync;

/* JADX INFO: loaded from: classes.dex */
public abstract class CrossDeviceSyncControllerCallback {
    static final int TYPE_CONNECTION_SERVICE = 1;
    static final int TYPE_IN_CALL_SERVICE = 2;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Type {
    }

    void processContextSyncMessage(int associationId, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData callMetadataSyncData) {
    }

    void requestCrossDeviceSync(android.companion.AssociationInfo associationInfo) {
    }

    void updateNumberOfActiveSyncAssociations(int userId, boolean added) {
    }

    void cleanUpCallIds(java.util.Set<java.lang.String> callIds) {
    }
}
