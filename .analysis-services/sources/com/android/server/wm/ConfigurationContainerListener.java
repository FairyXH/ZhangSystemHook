package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ConfigurationContainerListener {
    default void onRequestedOverrideConfigurationChanged(android.content.res.Configuration overrideConfiguration) {
    }

    default void onMergedOverrideConfigurationChanged(android.content.res.Configuration mergedOverrideConfiguration) {
    }
}
