package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
class BroadcastEvent {
    private long mIdForResponseEvent;
    private int mSourceUid;
    private java.lang.String mTargetPackage;
    private int mTargetUserId;
    private final android.util.LongArrayQueue mTimestampsMs = new android.util.LongArrayQueue();

    BroadcastEvent(int sourceUid, java.lang.String targetPackage, int targetUserId, long idForResponseEvent) {
        this.mSourceUid = sourceUid;
        this.mTargetPackage = targetPackage;
        this.mTargetUserId = targetUserId;
        this.mIdForResponseEvent = idForResponseEvent;
    }

    public int getSourceUid() {
        return this.mSourceUid;
    }

    public java.lang.String getTargetPackage() {
        return this.mTargetPackage;
    }

    public int getTargetUserId() {
        return this.mTargetUserId;
    }

    public long getIdForResponseEvent() {
        return this.mIdForResponseEvent;
    }

    public android.util.LongArrayQueue getTimestampsMs() {
        return this.mTimestampsMs;
    }

    public void addTimestampMs(long timestampMs) {
        this.mTimestampsMs.addLast(timestampMs);
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof com.android.server.usage.BroadcastEvent)) {
            return false;
        }
        com.android.server.usage.BroadcastEvent other = (com.android.server.usage.BroadcastEvent) obj;
        if (this.mSourceUid == other.mSourceUid && this.mIdForResponseEvent == other.mIdForResponseEvent && this.mTargetUserId == other.mTargetUserId && this.mTargetPackage.equals(other.mTargetPackage)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mSourceUid), this.mTargetPackage, java.lang.Integer.valueOf(this.mTargetUserId), java.lang.Long.valueOf(this.mIdForResponseEvent));
    }

    public java.lang.String toString() {
        return "BroadcastEvent {srcUid=" + this.mSourceUid + ",tgtPkg=" + this.mTargetPackage + ",tgtUser=" + this.mTargetUserId + ",id=" + this.mIdForResponseEvent + "}";
    }
}
