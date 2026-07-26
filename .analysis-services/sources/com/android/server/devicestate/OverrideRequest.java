package com.android.server.devicestate;

/* JADX INFO: loaded from: classes.dex */
final class OverrideRequest {
    public static final int OVERRIDE_REQUEST_TYPE_BASE_STATE = 1;
    public static final int OVERRIDE_REQUEST_TYPE_EMULATED_STATE = 0;
    private final int mFlags;
    private final int mPid;
    private final int mRequestType;
    private final android.hardware.devicestate.DeviceState mRequestedState;
    private final android.os.IBinder mToken;
    private final int mUid;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface OverrideRequestType {
    }

    OverrideRequest(android.os.IBinder token, int pid, int uid, android.hardware.devicestate.DeviceState requestedState, int flags, int requestType) {
        this.mToken = token;
        this.mPid = pid;
        this.mUid = uid;
        this.mRequestedState = requestedState;
        this.mFlags = flags;
        this.mRequestType = requestType;
    }

    android.os.IBinder getToken() {
        return this.mToken;
    }

    int getPid() {
        return this.mPid;
    }

    int getUid() {
        return this.mUid;
    }

    android.hardware.devicestate.DeviceState getRequestedDeviceState() {
        return this.mRequestedState;
    }

    int getRequestedStateIdentifier() {
        return this.mRequestedState.getIdentifier();
    }

    int getFlags() {
        return this.mFlags;
    }

    int getRequestType() {
        return this.mRequestType;
    }
}
