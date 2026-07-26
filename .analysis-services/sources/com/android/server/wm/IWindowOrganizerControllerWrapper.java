package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowOrganizerControllerWrapper {
    default com.android.server.wm.IWindowOrganizerControllerExt getExtImpl() {
        return new com.android.server.wm.IWindowOrganizerControllerExt() { // from class: com.android.server.wm.IWindowOrganizerControllerWrapper.1
        };
    }
}
