package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusWifiDisplayAdapterExt {
    default void reportWfdState(java.lang.String reportData) {
    }

    default boolean isWfdReportSwitchOn() {
        return false;
    }
}
