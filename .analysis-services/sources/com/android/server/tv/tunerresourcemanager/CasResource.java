package com.android.server.tv.tunerresourcemanager;

/* JADX INFO: loaded from: classes3.dex */
public class CasResource {
    private int mAvailableSessionNum;
    private int mMaxSessionNum;
    private java.util.Map<java.lang.Integer, java.lang.Integer> mOwnerClientIdsToSessionNum = new java.util.HashMap();
    private final int mSystemId;

    CasResource(com.android.server.tv.tunerresourcemanager.CasResource.Builder builder) {
        this.mSystemId = builder.mSystemId;
        this.mMaxSessionNum = builder.mMaxSessionNum;
        this.mAvailableSessionNum = builder.mMaxSessionNum;
    }

    public int getSystemId() {
        return this.mSystemId;
    }

    public int getMaxSessionNum() {
        return this.mMaxSessionNum;
    }

    public int getUsedSessionNum() {
        return this.mMaxSessionNum - this.mAvailableSessionNum;
    }

    public boolean isFullyUsed() {
        return this.mAvailableSessionNum == 0;
    }

    public void updateMaxSessionNum(int maxSessionNum) {
        this.mAvailableSessionNum = java.lang.Math.max(0, this.mAvailableSessionNum + (maxSessionNum - this.mMaxSessionNum));
        this.mMaxSessionNum = maxSessionNum;
    }

    public void setOwner(int ownerId) {
        int sessionNum = this.mOwnerClientIdsToSessionNum.get(java.lang.Integer.valueOf(ownerId)) == null ? 1 : this.mOwnerClientIdsToSessionNum.get(java.lang.Integer.valueOf(ownerId)).intValue() + 1;
        this.mOwnerClientIdsToSessionNum.put(java.lang.Integer.valueOf(ownerId), java.lang.Integer.valueOf(sessionNum));
        this.mAvailableSessionNum--;
    }

    public void removeOwner(int ownerId) {
        if (this.mOwnerClientIdsToSessionNum.containsKey(java.lang.Integer.valueOf(ownerId))) {
            this.mAvailableSessionNum += this.mOwnerClientIdsToSessionNum.get(java.lang.Integer.valueOf(ownerId)).intValue();
            this.mOwnerClientIdsToSessionNum.remove(java.lang.Integer.valueOf(ownerId));
        }
    }

    public void removeSession(int ownerId) {
        int sessionNum;
        if (this.mOwnerClientIdsToSessionNum.containsKey(java.lang.Integer.valueOf(ownerId)) && (sessionNum = this.mOwnerClientIdsToSessionNum.get(java.lang.Integer.valueOf(ownerId)).intValue()) > 0) {
            this.mOwnerClientIdsToSessionNum.put(java.lang.Integer.valueOf(ownerId), java.lang.Integer.valueOf(sessionNum - 1));
            this.mAvailableSessionNum++;
        }
    }

    public boolean hasOpenSessions(int ownerId) {
        return this.mOwnerClientIdsToSessionNum.get(java.lang.Integer.valueOf(ownerId)).intValue() > 0;
    }

    public java.util.Set<java.lang.Integer> getOwnerClientIds() {
        return this.mOwnerClientIdsToSessionNum.keySet();
    }

    public java.lang.String toString() {
        return "CasResource[systemId=" + this.mSystemId + ", isFullyUsed=" + (this.mAvailableSessionNum == 0) + ", maxSessionNum=" + this.mMaxSessionNum + ", ownerClients=" + ownersMapToString() + "]";
    }

    public static class Builder {
        protected int mMaxSessionNum;
        private int mSystemId;

        Builder(int systemId) {
            this.mSystemId = systemId;
        }

        public com.android.server.tv.tunerresourcemanager.CasResource.Builder maxSessionNum(int maxSessionNum) {
            this.mMaxSessionNum = maxSessionNum;
            return this;
        }

        public com.android.server.tv.tunerresourcemanager.CasResource build() {
            com.android.server.tv.tunerresourcemanager.CasResource cas = new com.android.server.tv.tunerresourcemanager.CasResource(this);
            return cas;
        }
    }

    protected java.lang.String ownersMapToString() {
        java.lang.StringBuilder string = new java.lang.StringBuilder("{");
        java.util.Iterator<java.lang.Integer> it = this.mOwnerClientIdsToSessionNum.keySet().iterator();
        while (it.hasNext()) {
            int clienId = it.next().intValue();
            string.append(" clientId=").append(clienId).append(", owns session num=").append(this.mOwnerClientIdsToSessionNum.get(java.lang.Integer.valueOf(clienId))).append(",");
        }
        return string.append("}").toString();
    }
}
