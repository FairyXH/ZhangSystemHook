package com.android.server.deviceidle;

/* JADX INFO: loaded from: classes.dex */
public class DeviceIdleConstraintTracker {
    public final int minState;
    public final java.lang.String name;
    public boolean active = false;
    public boolean monitoring = false;

    public DeviceIdleConstraintTracker(java.lang.String name, int minState) {
        this.name = name;
        this.minState = minState;
    }
}
