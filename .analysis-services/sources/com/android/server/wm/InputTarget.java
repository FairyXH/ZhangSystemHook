package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
interface InputTarget {
    boolean canScreenshotIme();

    void dumpProto(android.util.proto.ProtoOutputStream protoOutputStream, long j, int i);

    com.android.server.wm.ActivityRecord getActivityRecord();

    com.android.server.wm.DisplayContent getDisplayContent();

    int getDisplayId();

    com.android.server.wm.InsetsControlTarget getImeControlTarget();

    int getPid();

    int getUid();

    com.android.server.wm.WindowState getWindowState();

    android.os.IBinder getWindowToken();

    void handleTapOutsideFocusInsideSelf();

    void handleTapOutsideFocusOutsideSelf();

    boolean isInputMethodClientFocus(int i, int i2);

    boolean receiveFocusFromTapOutside();

    boolean shouldControlIme();
}
