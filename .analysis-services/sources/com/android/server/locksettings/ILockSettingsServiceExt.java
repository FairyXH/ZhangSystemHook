package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public interface ILockSettingsServiceExt {
    default void setBinderExtension(android.os.Binder extensionService) {
    }

    default void notifyPasswordDerivation(com.android.internal.widget.LockscreenCredential credential, int userId) {
    }

    default void notifyPasswordChanged(com.android.internal.widget.LockscreenCredential credential, int userId) {
    }

    default void init(com.android.server.locksettings.SyntheticPasswordManager syntheticPasswordManager, android.content.Context context, com.android.server.locksettings.LockSettingsStorage storage) {
    }

    default void hookOnStart() {
    }

    default void hookOnSystemReady() {
    }

    default long getSyntheticPasswordHandle(int userId) {
        return 0L;
    }

    default boolean isSyntheticPasswordBasedCredential(int userId) {
        return false;
    }

    default void notifyVoldDecryptAEKey(int userId, byte[] token, byte[] secret) {
    }

    default void notifyCredentialVerified(com.android.internal.widget.ICheckCredentialProgressCallback progressCallback) {
    }

    default void writeSecretToTee(com.android.internal.widget.VerifyCredentialResponse response, com.android.internal.widget.LockscreenCredential credential, int credentialType, int userId) {
    }

    default void resetTimeoutFlag(com.android.internal.widget.VerifyCredentialResponse verifyCredentialResponse) {
    }

    default void setLong(java.lang.String key, long value, int userId) {
    }

    default boolean setLockCredential(com.android.internal.widget.LockscreenCredential credential, com.android.internal.widget.LockscreenCredential savedCredential, int userId) {
        return false;
    }

    default boolean hooktieManagedProfileLockIfNecessary(int managedUserId, com.android.internal.widget.LockscreenCredential managedUserPassword) {
        return false;
    }

    default android.service.gatekeeper.IGateKeeperService getGateKeeperService() {
        return null;
    }

    default boolean hookShouldUnlockProfile(int userId) {
        return true;
    }

    default boolean isOplusMultiAppUserId(int userId) {
        return false;
    }

    default void tryRemoveLockscreenCredentialForMultiApp(int userId, boolean isUserSecure) {
    }

    default void ensureMigrateMultiAppUserLockKeys() {
    }

    default boolean hookCheckOnePlusMultiAppUser(int userId) {
        return false;
    }

    default boolean escrowtokenSupport() {
        return false;
    }

    default void dumpRedLog(java.io.PrintWriter printWriter) {
    }
}
