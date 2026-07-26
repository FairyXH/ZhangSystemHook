package com.android.server.tv.tunerresourcemanager;

/* JADX INFO: loaded from: classes3.dex */
public final class ClientProfile {
    public static final int INVALID_GROUP_ID = -1;
    public static final int INVALID_RESOURCE_ID = -1;
    private int mGroupId;
    private final int mId;
    private boolean mIsPriorityOverwritten;
    private int mNiceValue;
    private int mPrimaryUsingFrontendHandle;
    private int mPriority;
    private final int mProcessId;
    private java.util.Set<java.lang.Integer> mShareFeClientIds;
    private java.lang.Integer mShareeFeClientId;
    private final java.lang.String mTvInputSessionId;
    private final int mUseCase;
    private int mUsingCasSystemId;
    private int mUsingCiCamId;
    private java.util.Set<java.lang.Integer> mUsingDemuxHandles;
    private java.util.Set<java.lang.Integer> mUsingFrontendHandles;
    private java.util.Set<java.lang.Integer> mUsingLnbHandles;

    private ClientProfile(com.android.server.tv.tunerresourcemanager.ClientProfile.Builder builder) {
        this.mGroupId = -1;
        this.mPrimaryUsingFrontendHandle = -1;
        this.mUsingFrontendHandles = new java.util.HashSet();
        this.mShareFeClientIds = new java.util.HashSet();
        this.mUsingDemuxHandles = new java.util.HashSet();
        this.mShareeFeClientId = -1;
        this.mUsingLnbHandles = new java.util.HashSet();
        this.mUsingCasSystemId = -1;
        this.mUsingCiCamId = -1;
        this.mIsPriorityOverwritten = false;
        this.mId = builder.mId;
        this.mTvInputSessionId = builder.mTvInputSessionId;
        this.mUseCase = builder.mUseCase;
        this.mProcessId = builder.mProcessId;
    }

    public int getId() {
        return this.mId;
    }

    public java.lang.String getTvInputSessionId() {
        return this.mTvInputSessionId;
    }

    public int getUseCase() {
        return this.mUseCase;
    }

    public int getProcessId() {
        return this.mProcessId;
    }

    public boolean isPriorityOverwritten() {
        return this.mIsPriorityOverwritten;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public int getPriority() {
        return this.mPriority - this.mNiceValue;
    }

    public void setGroupId(int groupId) {
        this.mGroupId = groupId;
    }

    public void setPriority(int priority) {
        if (priority < 0) {
            return;
        }
        this.mPriority = priority;
    }

    public void overwritePriority(int priority) {
        if (priority < 0) {
            return;
        }
        this.mIsPriorityOverwritten = true;
        this.mPriority = priority;
    }

    public void setNiceValue(int niceValue) {
        this.mNiceValue = niceValue;
    }

    public void useFrontend(int frontendHandle) {
        this.mUsingFrontendHandles.add(java.lang.Integer.valueOf(frontendHandle));
    }

    public void setPrimaryFrontend(int frontendHandle) {
        this.mPrimaryUsingFrontendHandle = frontendHandle;
    }

    public int getPrimaryFrontend() {
        return this.mPrimaryUsingFrontendHandle;
    }

    public void shareFrontend(int clientId) {
        this.mShareFeClientIds.add(java.lang.Integer.valueOf(clientId));
    }

    public void stopSharingFrontend(int clientId) {
        this.mShareFeClientIds.remove(java.lang.Integer.valueOf(clientId));
    }

    public java.util.Set<java.lang.Integer> getInUseFrontendHandles() {
        return this.mUsingFrontendHandles;
    }

    public java.util.Set<java.lang.Integer> getShareFeClientIds() {
        return this.mShareFeClientIds;
    }

    public java.lang.Integer getShareeFeClientId() {
        return this.mShareeFeClientId;
    }

    public void setShareeFeClientId(java.lang.Integer shareeFeClientId) {
        this.mShareeFeClientId = shareeFeClientId;
    }

    public void releaseFrontend() {
        this.mUsingFrontendHandles.clear();
        this.mShareFeClientIds.clear();
        this.mShareeFeClientId = -1;
        this.mPrimaryUsingFrontendHandle = -1;
    }

    public void useDemux(int demuxHandle) {
        this.mUsingDemuxHandles.add(java.lang.Integer.valueOf(demuxHandle));
    }

    public java.util.Set<java.lang.Integer> getInUseDemuxHandles() {
        return this.mUsingDemuxHandles;
    }

    public void releaseDemux(int demuxHandle) {
        this.mUsingDemuxHandles.remove(java.lang.Integer.valueOf(demuxHandle));
    }

    public void useLnb(int lnbHandle) {
        this.mUsingLnbHandles.add(java.lang.Integer.valueOf(lnbHandle));
    }

    public java.util.Set<java.lang.Integer> getInUseLnbHandles() {
        return this.mUsingLnbHandles;
    }

    public void releaseLnb(int lnbHandle) {
        this.mUsingLnbHandles.remove(java.lang.Integer.valueOf(lnbHandle));
    }

    public void useCas(int casSystemId) {
        this.mUsingCasSystemId = casSystemId;
    }

    public int getInUseCasSystemId() {
        return this.mUsingCasSystemId;
    }

    public void releaseCas() {
        this.mUsingCasSystemId = -1;
    }

    public void useCiCam(int ciCamId) {
        this.mUsingCiCamId = ciCamId;
    }

    public int getInUseCiCamId() {
        return this.mUsingCiCamId;
    }

    public void releaseCiCam() {
        this.mUsingCiCamId = -1;
    }

    public void reclaimAllResources() {
        this.mUsingFrontendHandles.clear();
        this.mShareFeClientIds.clear();
        this.mPrimaryUsingFrontendHandle = -1;
        this.mUsingLnbHandles.clear();
        this.mUsingCasSystemId = -1;
        this.mUsingCiCamId = -1;
    }

    public java.lang.String toString() {
        return "ClientProfile[id=" + this.mId + ", tvInputSessionId=" + this.mTvInputSessionId + ", useCase=" + this.mUseCase + ", processId=" + this.mProcessId + "]";
    }

    public static class Builder {
        private final int mId;
        private int mProcessId;
        private java.lang.String mTvInputSessionId;
        private int mUseCase;

        Builder(int id) {
            this.mId = id;
        }

        public com.android.server.tv.tunerresourcemanager.ClientProfile.Builder useCase(int useCase) {
            this.mUseCase = useCase;
            return this;
        }

        public com.android.server.tv.tunerresourcemanager.ClientProfile.Builder tvInputSessionId(java.lang.String tvInputSessionId) {
            this.mTvInputSessionId = tvInputSessionId;
            return this;
        }

        public com.android.server.tv.tunerresourcemanager.ClientProfile.Builder processId(int processId) {
            this.mProcessId = processId;
            return this;
        }

        public com.android.server.tv.tunerresourcemanager.ClientProfile build() {
            com.android.server.tv.tunerresourcemanager.ClientProfile clientProfile = new com.android.server.tv.tunerresourcemanager.ClientProfile(this);
            return clientProfile;
        }
    }
}
