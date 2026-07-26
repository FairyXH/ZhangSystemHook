package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusStorageManagerCallback {
    byte[] exportSensitveFileKey(int i, int i2, boolean z);

    int getSystemUnlockedUserIdByIndexLocked(int i);

    android.os.storage.VolumeInfo getVolumeInfoByIndexLocked(int i);

    void onCheckBeforeMount(java.lang.String str);

    void onFsyncCtrl(java.lang.String str);

    void oplusAbortIdleMaintenance();

    boolean oplusIsFuseEnabled();

    void unlockSensitiveFileKey(int i, int i2, byte[] bArr, byte[] bArr2, int i3);
}
