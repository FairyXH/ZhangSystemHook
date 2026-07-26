package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MediaSessionRecordImpl {
    private static final java.util.concurrent.atomic.AtomicInteger sNextMediaSessionRecordId = new java.util.concurrent.atomic.AtomicInteger(1);
    private final int mUniqueId = sNextMediaSessionRecordId.getAndIncrement();

    public abstract void adjustVolume(java.lang.String str, java.lang.String str2, int i, int i2, boolean z, int i3, int i4, boolean z2);

    public abstract boolean canHandleVolumeKey();

    public abstract boolean checkPlaybackActiveState(boolean z);

    public abstract void close();

    public abstract void dump(java.io.PrintWriter printWriter, java.lang.String str);

    public abstract void expireTempEngaged();

    public abstract android.app.ForegroundServiceDelegationOptions getForegroundServiceDelegationOptions();

    public abstract java.lang.String getPackageName();

    public abstract int getSessionPolicies();

    public abstract int getUid();

    public abstract int getUserId();

    public abstract boolean isActive();

    public abstract boolean isClosed();

    abstract boolean isLinkedToNotification(android.app.Notification notification);

    public abstract boolean isPlaybackTypeLocal();

    public abstract boolean isSystemPriority();

    public abstract boolean sendMediaButton(java.lang.String str, int i, int i2, boolean z, android.view.KeyEvent keyEvent, int i3, android.os.ResultReceiver resultReceiver);

    public abstract void setSessionPolicies(int i);

    protected MediaSessionRecordImpl() {
    }

    public int getUniqueId() {
        return this.mUniqueId;
    }

    public final boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof com.android.server.media.MediaSessionRecordImpl)) {
            return false;
        }
        com.android.server.media.MediaSessionRecordImpl that = (com.android.server.media.MediaSessionRecordImpl) o;
        if (this.mUniqueId == that.mUniqueId) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mUniqueId));
    }
}
