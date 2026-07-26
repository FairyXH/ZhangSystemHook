package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
interface WindowContainerListener extends com.android.server.wm.ConfigurationContainerListener {
    default void onDisplayChanged(com.android.server.wm.DisplayContent dc) {
    }

    default void onRemoved() {
    }

    default void onVisibleRequestedChanged(boolean isVisibleRequested) {
    }
}
