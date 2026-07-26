package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public interface ISyntheticPasswordManagerExt {
    default boolean isMemoryLow() {
        return false;
    }

    default boolean isBootFromOTA() {
        return false;
    }

    default boolean isLightOS(android.content.Context context) {
        return false;
    }

    default boolean updateVerifyParam(android.content.Context context, com.android.internal.widget.LockscreenCredential credential, com.android.server.locksettings.SyntheticPasswordManager.PasswordData passwordData, int userId, long handle, com.android.server.locksettings.LockSettingsStorage storage) {
        return false;
    }

    default boolean updateCreateParam(android.content.Context context, byte[] credential, com.android.server.locksettings.SyntheticPasswordManager.PasswordData passwordData, int userId, long handle, int size) {
        return false;
    }

    default void triggerChannelOpen() {
    }
}
