package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public interface ILockSettingsServiceWrapper {
    default boolean isSyntheticPasswordBasedCredentialLocked(int userId) {
        return false;
    }

    default android.service.gatekeeper.IGateKeeperService getGateKeeperService() {
        return null;
    }

    default boolean hasUnifiedChallenge(int userId) {
        return false;
    }

    default boolean migrateProfileLockKeys() {
        return false;
    }

    default com.android.server.locksettings.SyntheticPasswordManager getSpManager() {
        return null;
    }

    default boolean unlockUserWithToken(long tokenHandle, byte[] token, int userId) {
        return false;
    }
}
