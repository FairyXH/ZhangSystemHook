package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class OriginInfo {
    final boolean mExisting;
    final java.io.File mFile;
    final java.io.File mResolvedFile;
    final java.lang.String mResolvedPath;
    final boolean mStaged;

    static com.android.server.pm.OriginInfo fromNothing() {
        return new com.android.server.pm.OriginInfo(null, false, false);
    }

    static com.android.server.pm.OriginInfo fromExistingFile(java.io.File file) {
        return new com.android.server.pm.OriginInfo(file, false, true);
    }

    static com.android.server.pm.OriginInfo fromStagedFile(java.io.File file) {
        return new com.android.server.pm.OriginInfo(file, true, false);
    }

    private OriginInfo(java.io.File file, boolean staged, boolean existing) {
        this.mFile = file;
        this.mStaged = staged;
        this.mExisting = existing;
        if (file != null) {
            this.mResolvedPath = file.getAbsolutePath();
            this.mResolvedFile = file;
        } else {
            this.mResolvedPath = null;
            this.mResolvedFile = null;
        }
    }
}
