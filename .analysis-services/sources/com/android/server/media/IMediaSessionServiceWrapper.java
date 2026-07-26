package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public interface IMediaSessionServiceWrapper {
    default com.android.server.media.IMediaSessionServiceExt getExtImpl() {
        return new com.android.server.media.IMediaSessionServiceExt() { // from class: com.android.server.media.IMediaSessionServiceWrapper.1
        };
    }

    default void updateMediaButtonReceiverInfo(android.content.ContentResolver contentResolver, com.android.server.media.MediaButtonReceiverHolder receiverHolder, int fullUserId) {
    }
}
