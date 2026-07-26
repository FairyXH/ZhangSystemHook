package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusStorageManagerFeature extends android.common.IOplusCommonFeature {
    public static final com.android.server.IOplusStorageManagerFeature DEFAULT = new com.android.server.IOplusStorageManagerFeature() { // from class: com.android.server.IOplusStorageManagerFeature.1
    };
    public static final int H_FSTRIM = 4;
    public static final int H_SHUTDOWN = 3;
    public static final int H_VOLUME_MOUNT = 5;
    public static final java.lang.String NAME = "IOplusStorageManagerFeature";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusStorageManagerFeature;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void initFillNode() {
        android.util.Log.d(NAME, "default InitFillNode");
    }

    default int getFragScore() {
        android.util.Log.d(NAME, "default getFragScore");
        return 0;
    }

    default long getLastCalcTime() {
        android.util.Log.d(NAME, "default getLastCalcTime");
        return 0L;
    }

    default void setLastCalcTime(long lastCalcTime) {
        android.util.Log.d(NAME, "default setLastCalcTime");
    }

    default void setStorageManagerHandler(android.os.Handler handler) {
        android.util.Log.d(NAME, "default setStorageManagerHandler");
    }

    default void setOplusStorageManagerCallback(com.android.server.IOplusStorageManagerCallback callback) {
        android.util.Log.d(NAME, "default setOplusStorageManagerCallback");
    }

    default boolean shouldHandleKeyguardStateChange(boolean isSecureKeyguardShowing) {
        android.util.Log.d(NAME, "default shouldHandleKeyguardStateChange");
        return false;
    }

    default boolean changeVolumeReadOnlyStateLocked(android.os.storage.VolumeInfo vol, int newState, int unlockedUsersSize) {
        android.util.Log.d(NAME, "default changeVolumeReadOnlyStateLocked");
        return false;
    }

    default boolean shouldNotifyVolumeStateChanged(java.lang.String newStateEnv, int userId, android.os.storage.VolumeInfo vol) {
        android.util.Log.d(NAME, "default shouldNotifyVolumeStateChanged");
        return false;
    }

    default void onVolumeCheckingLocked(android.os.storage.VolumeInfo vol, int currentUserId) {
        android.util.Log.d(NAME, "default onVolumeCheckingLocked");
    }

    default void onUnlockUser(int userId) {
        android.util.Log.d(NAME, "default onUnlockUser");
    }

    default boolean onStorageManagerMessageHandle(android.os.Message msg) {
        android.util.Log.d(NAME, "default onStorageManagerMessageHandle");
        return true;
    }

    default void onDiskStateChangedLocked(android.os.storage.DiskInfo disk, int volumesSize, int unlockedUsersSize) {
        android.util.Log.d(NAME, "default onDiskStateChanged");
    }

    default boolean idleMaintable() {
        android.util.Log.d(NAME, "default idleMaintable");
        return true;
    }

    default boolean maintAborted() {
        android.util.Log.d(NAME, "default maintAborted");
        return false;
    }

    default boolean isDeviceIdle() {
        android.util.Log.d(NAME, "default isDeviceIdle");
        return false;
    }

    default void setMaintPrepared(boolean MaintPrepared) {
        android.util.Log.d(NAME, "default setMaintPrepared");
    }

    default void setMaintAborted(boolean MaintAborted) {
        android.util.Log.d(NAME, "default setMaintAborted");
    }

    default android.content.BroadcastReceiver getScreenReceiver() {
        android.util.Log.d(NAME, "default getScreenReceiver");
        return null;
    }

    default void killInputMethods(android.content.Context context, int userId, java.lang.String reason) {
        android.util.Log.d(NAME, "default killInputMethods");
    }

    default int getStorageData() {
        android.util.Log.d(NAME, "default getStorageData");
        return -1;
    }

    default int setSDLockPassword(java.lang.String pw) {
        android.util.Log.d(NAME, "default setSDLockPassword");
        return -1;
    }

    default int clearSDLockPassword() {
        android.util.Log.d(NAME, "default clearSDLockPassword");
        return -1;
    }

    default int unlockSDCard(java.lang.String pw) {
        android.util.Log.d(NAME, "default unlockSDCard");
        return -1;
    }

    default long getUnlockSdcardDeadline() {
        android.util.Log.d(NAME, "default getUnlockSdcardDeadline");
        return -1L;
    }

    default java.lang.String getSDCardId() {
        android.util.Log.d(NAME, "default getSDCardId");
        return null;
    }

    default int getSDLockState() {
        android.util.Log.d(NAME, "default getSDLockState");
        return -1;
    }

    default void eraseSDLock() {
        android.util.Log.d(NAME, "default eraseSDLock");
    }

    default byte[] exportSensitveFileBeKey(int userId, int sensitiveType) throws android.os.RemoteException {
        android.util.Log.d(NAME, "default exportSensitveFileBeKey");
        return null;
    }

    default void unlockAndExportAllSensitiveFileKey(int userId, int serialNumber, byte[] token, byte[] secret) {
        android.util.Log.d(NAME, "default unlockAndExportAllSensitiveFileKey");
    }

    default void clearSensitiveKey(boolean secureKeyguardShowing) {
        android.util.Log.d(NAME, "default clearSensitiveKey");
    }

    default void addAuthResultInfo(int uid, int pid, int permBits, java.lang.String packageName) {
        android.util.Log.d(NAME, "default addAuthResultInfo");
    }

    default java.util.Map<java.lang.String, byte[]> encryptDek(byte[] dek, int protectType, byte[] protectedKek, byte[] deviceNonce, byte[] kekID) {
        android.util.Log.d(NAME, "default encryptDek");
        return null;
    }

    default byte[] decryptDek(byte[] protectedDek, int protectType, byte[] protectedKek, byte[] deviceNonce, byte[] kekID, byte[] appNonce) {
        android.util.Log.d(NAME, "default decryptDek");
        return null;
    }

    default java.util.Map<java.lang.String, byte[]> initAeKek() {
        android.util.Log.d(NAME, "default initAeKek");
        return null;
    }

    default java.util.Map<java.lang.String, byte[]> initBeKek() {
        android.util.Log.d(NAME, "default initBeKek");
        return null;
    }

    default void initOplusStorageFeature(com.android.server.IOplusStorageManagerCallback callback, android.os.Handler handler) {
        android.util.Log.d(NAME, "default initOplusStorageFeature");
    }

    default long setLastMaintenance(long mLastMaintenance, java.io.File mLastMaintenanceFile) {
        android.util.Log.d(NAME, "default setLastMaintenance");
        return 1L;
    }

    default void voldTBExt() {
        android.util.Log.d(NAME, "default voldTBExt");
    }

    default void schedulePreFstrim() {
        android.util.Log.d(NAME, "default schedulePreFstrim");
    }

    default android.os.ParcelFileDescriptor mountDfsFuse(java.lang.String path, java.lang.String opts) {
        android.util.Log.d(NAME, "default mountDfsFuse");
        return null;
    }

    default int umountDfsFuse(java.lang.String path) {
        android.util.Log.d(NAME, "default umountDfsFuse");
        return -1;
    }

    default int configDfsFuse(java.lang.String path, int readAheadBlocks, int maxDirtyRatio) {
        android.util.Log.d(NAME, "default configDfsFuse");
        return -1;
    }

    default int mountTmpStor(java.lang.String mountPoint, int maxSize, int connectid) {
        android.util.Log.d(NAME, "TmpStor: default mountTmpStor");
        return 0;
    }

    default boolean unmountTmpStor(java.lang.String mountPoint, int connectid) {
        android.util.Log.d(NAME, "TmpStor: default unmountTmpStor");
        return false;
    }

    default void checkMultiAppExternalStorageState(android.os.storage.StorageVolume storageVolume) {
    }
}
