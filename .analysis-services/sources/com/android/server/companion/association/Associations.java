package com.android.server.companion.association;

/* JADX INFO: loaded from: classes.dex */
public class Associations {
    private int mVersion = 0;
    private java.util.List<android.companion.AssociationInfo> mAssociations = new java.util.ArrayList();
    private int mMaxId = 0;

    public void setVersion(int version) {
        this.mVersion = version;
    }

    public void addAssociation(android.companion.AssociationInfo association) {
        this.mAssociations.add(association);
    }

    public void setMaxId(int maxId) {
        this.mMaxId = maxId;
    }

    public void setAssociations(java.util.List<android.companion.AssociationInfo> associations) {
        this.mAssociations = java.util.List.copyOf(associations);
    }

    public int getVersion() {
        return this.mVersion;
    }

    public int getMaxId() {
        return this.mMaxId;
    }

    public java.util.List<android.companion.AssociationInfo> getAssociations() {
        return this.mAssociations;
    }
}
