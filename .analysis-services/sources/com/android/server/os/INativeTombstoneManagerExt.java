package com.android.server.os;

/* JADX INFO: loaded from: classes2.dex */
public interface INativeTombstoneManagerExt {
    default boolean isOverLimitSize(java.io.File path) {
        return false;
    }
}
