package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class MoveInfo {
    final int mAppId;
    final java.lang.String mFromCodePath;
    final java.lang.String mFromUuid;
    final int mMoveId;
    final java.lang.String mPackageName;
    final java.lang.String mSeInfo;
    final int mTargetSdkVersion;
    final java.lang.String mToUuid;

    MoveInfo(int moveId, java.lang.String fromUuid, java.lang.String toUuid, java.lang.String packageName, int appId, java.lang.String seInfo, int targetSdkVersion, java.lang.String fromCodePath) {
        this.mMoveId = moveId;
        this.mFromUuid = fromUuid;
        this.mToUuid = toUuid;
        this.mPackageName = packageName;
        this.mAppId = appId;
        this.mSeInfo = seInfo;
        this.mTargetSdkVersion = targetSdkVersion;
        this.mFromCodePath = fromCodePath;
    }
}
