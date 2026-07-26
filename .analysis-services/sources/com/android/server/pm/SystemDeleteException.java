package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class SystemDeleteException extends java.lang.Exception {
    final com.android.server.pm.PackageManagerException mReason;

    SystemDeleteException(com.android.server.pm.PackageManagerException reason) {
        this.mReason = reason;
    }
}
