package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IGestureLauncherServiceExt {
    default boolean interceptPowerKeyDownForCamera() {
        return false;
    }
}
