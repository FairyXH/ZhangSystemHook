package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class CleanUpArgs {
    private final java.io.File mCodeFile;
    private final java.lang.String[] mInstructionSets;
    private final java.lang.String mPackageName;

    CleanUpArgs(java.lang.String packageName, java.lang.String codePath, java.lang.String[] instructionSets) {
        this.mPackageName = packageName;
        this.mCodeFile = new java.io.File(codePath);
        this.mInstructionSets = instructionSets;
    }

    java.lang.String getPackageName() {
        return this.mPackageName;
    }

    java.io.File getCodeFile() {
        return this.mCodeFile;
    }

    java.lang.String getCodePath() {
        return this.mCodeFile.getAbsolutePath();
    }

    java.lang.String[] getInstructionSets() {
        return this.mInstructionSets;
    }
}
