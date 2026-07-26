package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public interface IMediaSessionServiceExt {
    default void init(android.content.Context context) {
    }

    default void setLastMediaButtonReceiver(java.lang.Object holder, int userId) {
    }

    default boolean isInHistoryPlayInfoWhiteList(java.lang.String pkgName) {
        return false;
    }

    default boolean isInMediaBlackList(java.lang.String pkgName) {
        return false;
    }

    default boolean isMediaControlSupported() {
        return false;
    }

    default java.lang.String checkAndResetReceiverInfo(java.lang.String mediaButtonReceiverInfo) {
        return "";
    }
}
