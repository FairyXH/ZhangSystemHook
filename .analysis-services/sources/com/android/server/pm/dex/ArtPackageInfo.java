package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public class ArtPackageInfo {
    private final java.util.List<java.lang.String> mCodePaths;
    private final java.util.List<java.lang.String> mInstructionSets;
    private final java.lang.String mOatDir;
    private final java.lang.String mPackageName;

    public ArtPackageInfo(java.lang.String packageName, java.util.List<java.lang.String> instructionSets, java.util.List<java.lang.String> codePaths, java.lang.String oatDir) {
        this.mPackageName = packageName;
        this.mInstructionSets = instructionSets;
        this.mCodePaths = codePaths;
        this.mOatDir = oatDir;
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public java.util.List<java.lang.String> getInstructionSets() {
        return this.mInstructionSets;
    }

    public java.util.List<java.lang.String> getCodePaths() {
        return this.mCodePaths;
    }

    public java.lang.String getOatDir() {
        return this.mOatDir;
    }
}
