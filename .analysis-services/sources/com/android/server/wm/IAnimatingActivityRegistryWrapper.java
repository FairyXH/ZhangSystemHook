package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IAnimatingActivityRegistryWrapper {
    default java.util.LinkedHashMap<com.android.server.wm.ActivityRecord, java.lang.Runnable> getFinishedTokens() {
        return new java.util.LinkedHashMap<>();
    }
}
