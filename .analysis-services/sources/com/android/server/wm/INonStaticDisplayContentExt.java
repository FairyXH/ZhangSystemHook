package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface INonStaticDisplayContentExt {
    default void setPuttDisplay(boolean puttDisplay) {
    }

    default boolean isPuttDisplay() {
        return false;
    }

    default void updateRequestedOverrideConfiguration(android.content.res.Configuration overrideConfiguration) {
    }
}
