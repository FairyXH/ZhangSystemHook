package com.android.server.tv.tunerresourcemanager;

/* JADX INFO: loaded from: classes3.dex */
public final class FrontendResource extends com.android.server.tv.tunerresourcemanager.TunerResourceBasic {
    private final int mExclusiveGroupId;
    private java.util.Set<java.lang.Integer> mExclusiveGroupMemberHandles;
    private final int mType;

    private FrontendResource(com.android.server.tv.tunerresourcemanager.FrontendResource.Builder builder) {
        super(builder);
        this.mExclusiveGroupMemberHandles = new java.util.HashSet();
        this.mType = builder.mType;
        this.mExclusiveGroupId = builder.mExclusiveGroupId;
    }

    public int getType() {
        return this.mType;
    }

    public int getExclusiveGroupId() {
        return this.mExclusiveGroupId;
    }

    public java.util.Set<java.lang.Integer> getExclusiveGroupMemberFeHandles() {
        return this.mExclusiveGroupMemberHandles;
    }

    public void addExclusiveGroupMemberFeHandle(int handle) {
        this.mExclusiveGroupMemberHandles.add(java.lang.Integer.valueOf(handle));
    }

    public void addExclusiveGroupMemberFeHandles(java.util.Collection<java.lang.Integer> handles) {
        this.mExclusiveGroupMemberHandles.addAll(handles);
    }

    public void removeExclusiveGroupMemberFeId(int handle) {
        this.mExclusiveGroupMemberHandles.remove(java.lang.Integer.valueOf(handle));
    }

    public java.lang.String toString() {
        return "FrontendResource[handle=" + this.mHandle + ", type=" + this.mType + ", exclusiveGId=" + this.mExclusiveGroupId + ", exclusiveGMemeberHandles=" + this.mExclusiveGroupMemberHandles + ", isInUse=" + this.mIsInUse + ", ownerClientId=" + this.mOwnerClientId + "]";
    }

    public static class Builder extends com.android.server.tv.tunerresourcemanager.TunerResourceBasic.Builder {
        private int mExclusiveGroupId;
        private int mType;

        Builder(int handle) {
            super(handle);
        }

        public com.android.server.tv.tunerresourcemanager.FrontendResource.Builder type(int type) {
            this.mType = type;
            return this;
        }

        public com.android.server.tv.tunerresourcemanager.FrontendResource.Builder exclusiveGroupId(int exclusiveGroupId) {
            this.mExclusiveGroupId = exclusiveGroupId;
            return this;
        }

        @Override // com.android.server.tv.tunerresourcemanager.TunerResourceBasic.Builder
        public com.android.server.tv.tunerresourcemanager.FrontendResource build() {
            com.android.server.tv.tunerresourcemanager.FrontendResource frontendResource = new com.android.server.tv.tunerresourcemanager.FrontendResource(this);
            return frontendResource;
        }
    }
}
