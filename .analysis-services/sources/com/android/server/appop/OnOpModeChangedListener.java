package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public abstract class OnOpModeChangedListener {
    private static final int UID_ANY = -2;
    private int mCallingPid;
    private int mCallingUid;
    private int mFlags;
    private int mWatchedOpCode;
    private int mWatchingUid;

    public abstract void onOpModeChanged(int i, int i2, java.lang.String str) throws android.os.RemoteException;

    public abstract java.lang.String toString();

    OnOpModeChangedListener(int watchingUid, int flags, int watchedOpCode, int callingUid, int callingPid) {
        this.mWatchingUid = watchingUid;
        this.mFlags = flags;
        this.mWatchedOpCode = watchedOpCode;
        this.mCallingUid = callingUid;
        this.mCallingPid = callingPid;
    }

    public int getWatchingUid() {
        return this.mWatchingUid;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public int getWatchedOpCode() {
        return this.mWatchedOpCode;
    }

    public int getCallingUid() {
        return this.mCallingUid;
    }

    public int getCallingPid() {
        return this.mCallingPid;
    }

    public boolean isWatchingUid(int uid) {
        return uid == -2 || this.mWatchingUid < 0 || this.mWatchingUid == uid;
    }

    public void onOpModeChanged(int op, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) throws android.os.RemoteException {
        if (java.util.Objects.equals(persistentDeviceId, "default:0")) {
            onOpModeChanged(op, uid, packageName);
        }
    }
}
