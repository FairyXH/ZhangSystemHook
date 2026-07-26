package com.android.server.tv.tunerresourcemanager;

/* JADX INFO: loaded from: classes3.dex */
public final class CiCamResource extends com.android.server.tv.tunerresourcemanager.CasResource {
    private CiCamResource(com.android.server.tv.tunerresourcemanager.CiCamResource.Builder builder) {
        super(builder);
    }

    @Override // com.android.server.tv.tunerresourcemanager.CasResource
    public java.lang.String toString() {
        return "CiCamResource[systemId=" + getSystemId() + ", isFullyUsed=" + isFullyUsed() + ", maxSessionNum=" + getMaxSessionNum() + ", ownerClients=" + ownersMapToString() + "]";
    }

    public int getCiCamId() {
        return getSystemId();
    }

    public static class Builder extends com.android.server.tv.tunerresourcemanager.CasResource.Builder {
        Builder(int systemId) {
            super(systemId);
        }

        @Override // com.android.server.tv.tunerresourcemanager.CasResource.Builder
        public com.android.server.tv.tunerresourcemanager.CiCamResource.Builder maxSessionNum(int maxSessionNum) {
            this.mMaxSessionNum = maxSessionNum;
            return this;
        }

        @Override // com.android.server.tv.tunerresourcemanager.CasResource.Builder
        public com.android.server.tv.tunerresourcemanager.CiCamResource build() {
            com.android.server.tv.tunerresourcemanager.CiCamResource ciCam = new com.android.server.tv.tunerresourcemanager.CiCamResource(this);
            return ciCam;
        }
    }
}
