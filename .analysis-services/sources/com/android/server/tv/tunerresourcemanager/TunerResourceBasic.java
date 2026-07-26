package com.android.server.tv.tunerresourcemanager;

/* JADX INFO: loaded from: classes3.dex */
public class TunerResourceBasic {
    final int mHandle;
    boolean mIsInUse;
    int mOwnerClientId = -1;

    TunerResourceBasic(com.android.server.tv.tunerresourcemanager.TunerResourceBasic.Builder builder) {
        this.mHandle = builder.mHandle;
    }

    public int getHandle() {
        return this.mHandle;
    }

    public boolean isInUse() {
        return this.mIsInUse;
    }

    public int getOwnerClientId() {
        return this.mOwnerClientId;
    }

    public void setOwner(int ownerClientId) {
        this.mIsInUse = true;
        this.mOwnerClientId = ownerClientId;
    }

    public void removeOwner() {
        this.mIsInUse = false;
        this.mOwnerClientId = -1;
    }

    public static class Builder {
        private final int mHandle;

        Builder(int handle) {
            this.mHandle = handle;
        }

        public com.android.server.tv.tunerresourcemanager.TunerResourceBasic build() {
            com.android.server.tv.tunerresourcemanager.TunerResourceBasic resource = new com.android.server.tv.tunerresourcemanager.TunerResourceBasic(this);
            return resource;
        }
    }
}
