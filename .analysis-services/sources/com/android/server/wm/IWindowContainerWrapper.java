package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowContainerWrapper {
    default com.android.server.wm.IWindowContainerExt getExtImpl() {
        return new com.android.server.wm.IWindowContainerExt() { // from class: com.android.server.wm.IWindowContainerWrapper.1
        };
    }

    default int syncTransactionCommitCallbackDepth() {
        return 0;
    }

    default java.util.List<com.android.server.wm.WindowContainerListener> getWindowContainerListener() {
        return new java.util.ArrayList();
    }
}
