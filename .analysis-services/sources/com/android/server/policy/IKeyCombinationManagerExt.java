package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface IKeyCombinationManagerExt {
    default boolean canAODScreenshot(android.view.KeyEvent event) {
        return false;
    }
}
