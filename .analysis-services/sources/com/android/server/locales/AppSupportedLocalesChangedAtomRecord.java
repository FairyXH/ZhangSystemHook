package com.android.server.locales;

/* JADX INFO: loaded from: classes2.dex */
public final class AppSupportedLocalesChangedAtomRecord {
    final int mCallingUid;
    int mTargetUid = -1;
    int mNumLocales = -1;
    boolean mOverrideRemoved = false;
    boolean mSameAsResConfig = false;
    boolean mSameAsPrevConfig = false;
    int mStatus = 0;

    AppSupportedLocalesChangedAtomRecord(int callingUid) {
        this.mCallingUid = callingUid;
    }

    void setTargetUid(int targetUid) {
        this.mTargetUid = targetUid;
    }

    void setNumLocales(int numLocales) {
        this.mNumLocales = numLocales;
    }

    void setOverrideRemoved(boolean overrideRemoved) {
        this.mOverrideRemoved = overrideRemoved;
    }

    void setSameAsResConfig(boolean sameAsResConfig) {
        this.mSameAsResConfig = sameAsResConfig;
    }

    void setSameAsPrevConfig(boolean sameAsPrevConfig) {
        this.mSameAsPrevConfig = sameAsPrevConfig;
    }

    void setStatus(int status) {
        this.mStatus = status;
    }
}
