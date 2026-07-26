package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IRecordingStateWrapper {
    default boolean getIsActive() {
        return false;
    }
}
