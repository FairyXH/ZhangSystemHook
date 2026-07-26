package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public final class DexoptOptions {
    public static final int DEXOPT_AS_SHARED_LIBRARY = 64;
    public static final int DEXOPT_BOOT_COMPLETE = 4;
    public static final int DEXOPT_CHECK_FOR_PROFILES_UPDATES = 1;
    public static final int DEXOPT_DOWNGRADE = 32;
    public static final int DEXOPT_FORCE = 2;
    public static final int DEXOPT_FOR_RESTORE = 2048;
    public static final int DEXOPT_IDLE_BACKGROUND_JOB = 512;
    public static final int DEXOPT_INSTALL_WITH_DEX_METADATA_FILE = 1024;
    public static final int DEXOPT_ONLY_SECONDARY_DEX = 8;
    private static final java.lang.String TAG = "DexoptOptions";
    private final int mCompilationReason;
    private final java.lang.String mCompilerFilter;
    private final int mFlags;
    private final java.lang.String mPackageName;
    private final java.lang.String mSplitName;

    public DexoptOptions(java.lang.String packageName, java.lang.String compilerFilter, int flags) {
        this(packageName, -1, compilerFilter, null, flags);
    }

    public DexoptOptions(java.lang.String packageName, int compilationReason, int flags) {
        this(packageName, compilationReason, com.android.server.pm.PackageManagerServiceCompilerMapping.getCompilerFilterForReason(compilationReason), null, flags);
    }

    public DexoptOptions(java.lang.String packageName, int compilationReason, java.lang.String compilerFilter, java.lang.String splitName, int flags) {
        if (((~3695) & flags) != 0) {
            throw new java.lang.IllegalArgumentException("Invalid flags : " + java.lang.Integer.toHexString(flags));
        }
        this.mPackageName = packageName;
        this.mCompilerFilter = compilerFilter;
        this.mFlags = flags;
        this.mSplitName = splitName;
        this.mCompilationReason = compilationReason;
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public boolean isCheckForProfileUpdates() {
        return (this.mFlags & 1) != 0;
    }

    public java.lang.String getCompilerFilter() {
        return this.mCompilerFilter;
    }

    public boolean isForce() {
        return (this.mFlags & 2) != 0;
    }

    public boolean isBootComplete() {
        return (this.mFlags & 4) != 0;
    }

    public boolean isDexoptOnlySecondaryDex() {
        return (this.mFlags & 8) != 0;
    }

    public boolean isDowngrade() {
        return (this.mFlags & 32) != 0;
    }

    public boolean isDexoptAsSharedLibrary() {
        return (this.mFlags & 64) != 0;
    }

    public boolean isDexoptIdleBackgroundJob() {
        return (this.mFlags & 512) != 0;
    }

    public boolean isDexoptInstallWithDexMetadata() {
        return (this.mFlags & 1024) != 0;
    }

    public boolean isDexoptInstallForRestore() {
        return (this.mFlags & 2048) != 0;
    }

    public java.lang.String getSplitName() {
        return this.mSplitName;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public int getCompilationReason() {
        return this.mCompilationReason;
    }

    public com.android.server.pm.dex.DexoptOptions overrideCompilerFilter(java.lang.String newCompilerFilter) {
        return new com.android.server.pm.dex.DexoptOptions(this.mPackageName, this.mCompilationReason, newCompilerFilter, this.mSplitName, this.mFlags);
    }

    public static java.lang.String convertToArtServiceDexoptReason(int pmDexoptReason) {
        switch (pmDexoptReason) {
            case 0:
                return "first-boot";
            case 1:
                return "boot-after-ota";
            case 2:
            case 10:
                throw new java.lang.UnsupportedOperationException("ART Service unsupported compilation reason " + pmDexoptReason);
            case 3:
                return "install";
            case 4:
                return "install-fast";
            case 5:
                return "install-bulk";
            case 6:
                return "install-bulk-secondary";
            case 7:
                return "install-bulk-downgraded";
            case 8:
                return "install-bulk-secondary-downgraded";
            case 9:
                return "bg-dexopt";
            case 11:
                return "inactive";
            case 12:
                return "cmdline";
            case 13:
                return "boot-after-mainline-update";
            default:
                throw new java.lang.IllegalArgumentException("Invalid compilation reason " + pmDexoptReason);
        }
    }

    public com.android.server.art.model.DexoptParams convertToDexoptParams(int extraFlags) {
        int flags;
        int priority;
        if (this.mSplitName != null) {
            throw new java.lang.UnsupportedOperationException("Request to optimize only split " + this.mSplitName + " for " + this.mPackageName);
        }
        int flags2 = extraFlags;
        if ((this.mFlags & 1) == 0 && dalvik.system.DexFile.isProfileGuidedCompilerFilter(this.mCompilerFilter)) {
            throw new java.lang.IllegalArgumentException("DEXOPT_CHECK_FOR_PROFILES_UPDATES must be set with profile guided filter");
        }
        if ((this.mFlags & 2) != 0) {
            flags2 |= 16;
        }
        if ((this.mFlags & 8) != 0) {
            flags = flags2 | 2;
        } else {
            flags = flags2 | 1;
        }
        if ((this.mFlags & 32) != 0) {
            flags |= 8;
        }
        if ((this.mFlags & 1024) == 0) {
            android.util.Log.w(TAG, "DEXOPT_INSTALL_WITH_DEX_METADATA_FILE not set in request to optimise " + this.mPackageName + " - ART Service will unconditionally use a DM file if present.");
        }
        if ((this.mFlags & 4) != 0) {
            if ((this.mFlags & 2048) != 0) {
                priority = 80;
            } else {
                int priority2 = this.mFlags;
                if ((priority2 & 512) != 0) {
                    priority = 40;
                } else {
                    priority = 60;
                }
            }
        } else {
            priority = 100;
        }
        return new com.android.server.art.model.DexoptParams.Builder(convertToArtServiceDexoptReason(this.mCompilationReason), flags).setCompilerFilter(this.mCompilerFilter).setPriorityClass(priority).build();
    }
}
