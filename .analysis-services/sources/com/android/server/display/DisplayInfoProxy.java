package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayInfoProxy {
    private android.view.DisplayInfo mInfo;

    public DisplayInfoProxy(android.view.DisplayInfo info) {
        this.mInfo = info;
    }

    public void set(android.view.DisplayInfo info) {
        this.mInfo = info;
        android.hardware.display.DisplayManagerGlobal.invalidateLocalDisplayInfoCaches();
    }

    public android.view.DisplayInfo get() {
        return this.mInfo;
    }
}
