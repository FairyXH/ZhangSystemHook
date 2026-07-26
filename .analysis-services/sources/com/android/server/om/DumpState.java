package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
public final class DumpState {
    private java.lang.String mField;
    private java.lang.String mOverlayName;
    private java.lang.String mPackageName;
    private int mUserId = -1;
    private boolean mVerbose;

    public void setUserId(int userId) {
        this.mUserId = userId;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public void setOverlyIdentifier(java.lang.String overlayIdentifier) {
        android.content.om.OverlayIdentifier overlay = android.content.om.OverlayIdentifier.fromString(overlayIdentifier);
        this.mPackageName = overlay.getPackageName();
        this.mOverlayName = overlay.getOverlayName();
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public java.lang.String getOverlayName() {
        return this.mOverlayName;
    }

    public void setField(java.lang.String field) {
        this.mField = field;
    }

    public java.lang.String getField() {
        return this.mField;
    }

    public void setVerbose(boolean verbose) {
        this.mVerbose = verbose;
    }

    public boolean isVerbose() {
        return this.mVerbose;
    }
}
