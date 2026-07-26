package com.android.server.tv.tunerresourcemanager;

/* JADX INFO: loaded from: classes3.dex */
public final class DemuxResource extends com.android.server.tv.tunerresourcemanager.TunerResourceBasic {
    private final int mFilterTypes;

    private DemuxResource(com.android.server.tv.tunerresourcemanager.DemuxResource.Builder builder) {
        super(builder);
        this.mFilterTypes = builder.mFilterTypes;
    }

    public int getFilterTypes() {
        return this.mFilterTypes;
    }

    public java.lang.String toString() {
        return "DemuxResource[handle=" + this.mHandle + ", filterTypes=" + this.mFilterTypes + ", isInUse=" + this.mIsInUse + ", ownerClientId=" + this.mOwnerClientId + "]";
    }

    public boolean hasSufficientCaps(int desiredCaps) {
        return desiredCaps == (this.mFilterTypes & desiredCaps);
    }

    public int getNumOfCaps() {
        int mask = 1;
        int numOfCaps = 0;
        for (int i = 0; i < 32; i++) {
            if ((this.mFilterTypes & mask) == mask) {
                numOfCaps++;
            }
            mask <<= 1;
        }
        return numOfCaps;
    }

    public static class Builder extends com.android.server.tv.tunerresourcemanager.TunerResourceBasic.Builder {
        private int mFilterTypes;

        Builder(int handle) {
            super(handle);
        }

        public com.android.server.tv.tunerresourcemanager.DemuxResource.Builder filterTypes(int filterTypes) {
            this.mFilterTypes = filterTypes;
            return this;
        }

        @Override // com.android.server.tv.tunerresourcemanager.TunerResourceBasic.Builder
        public com.android.server.tv.tunerresourcemanager.DemuxResource build() {
            com.android.server.tv.tunerresourcemanager.DemuxResource demux = new com.android.server.tv.tunerresourcemanager.DemuxResource(this);
            return demux;
        }
    }
}
