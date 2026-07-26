package com.android.server.sensors;

/* JADX INFO: loaded from: classes3.dex */
public interface ISensorServiceExt {
    @java.lang.Deprecated
    default void switchADFRState(boolean state) {
    }

    default void scheduleRecordProxUsage() {
    }

    default void onBootPhase(int phase) {
    }
}
