package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class VerificationInfo {
    final int mInstallerUid;
    final int mOriginatingUid;
    final android.net.Uri mOriginatingUri;
    final android.net.Uri mReferrer;

    VerificationInfo(android.net.Uri originatingUri, android.net.Uri referrer, int originatingUid, int installerUid) {
        this.mOriginatingUri = originatingUri;
        this.mReferrer = referrer;
        this.mOriginatingUid = originatingUid;
        this.mInstallerUid = installerUid;
    }
}
