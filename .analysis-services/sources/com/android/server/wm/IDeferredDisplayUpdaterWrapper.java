package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDeferredDisplayUpdaterWrapper {
    default com.android.server.wm.IDeferredDisplayUpdaterExt getExtImpl() {
        return new com.android.server.wm.IDeferredDisplayUpdaterExt() { // from class: com.android.server.wm.IDeferredDisplayUpdaterWrapper.1
        };
    }

    default com.android.server.wm.DisplayContent getDisplayContent() {
        return null;
    }

    default android.view.DisplayInfo getLastDisplayInfo() {
        return null;
    }
}
