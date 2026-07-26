package com.android.server.companion;

/* JADX INFO: loaded from: classes.dex */
public interface CompanionDeviceManagerServiceInternal {
    void addSelfOwnedCallId(java.lang.String str);

    void crossDeviceSync(int i, java.util.Collection<com.android.server.companion.datatransfer.contextsync.CrossDeviceCall> collection);

    void crossDeviceSync(android.companion.AssociationInfo associationInfo, java.util.Collection<com.android.server.companion.datatransfer.contextsync.CrossDeviceCall> collection);

    void registerCallMetadataSyncCallback(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback crossDeviceSyncControllerCallback, int i);

    void removeInactiveSelfManagedAssociations();

    void removeSelfOwnedCallId(java.lang.String str);

    void sendCrossDeviceSyncMessage(int i, byte[] bArr);

    void sendCrossDeviceSyncMessageToAllDevices(int i, byte[] bArr);
}
