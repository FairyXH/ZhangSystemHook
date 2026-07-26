package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public class ArtStatsLogUtils {
    private static final int ART_COMPILATION_FILTER_FAKE_RUN_FROM_APK_FALLBACK = 14;
    private static final int ART_COMPILATION_FILTER_FAKE_RUN_FROM_VDEX_FALLBACK = 15;
    private static final int ART_COMPILATION_REASON_INSTALL_BULK_DOWNGRADED = 15;
    private static final int ART_COMPILATION_REASON_INSTALL_BULK_SECONDARY = 14;
    private static final int ART_COMPILATION_REASON_INSTALL_BULK_SECONDARY_DOWNGRADED = 16;
    private static final java.util.Map<java.lang.String, java.lang.Integer> COMPILE_FILTER_MAP;
    private static final java.util.Map<java.lang.String, java.lang.Integer> ISA_MAP;
    private static final java.lang.String PROFILE_DEX_METADATA = "primary.prof";
    private static final java.lang.String VDEX_DEX_METADATA = "primary.vdex";
    private static final java.lang.String TAG = com.android.server.pm.dex.ArtStatsLogUtils.class.getSimpleName();
    private static final java.util.Map<java.lang.Integer, java.lang.Integer> COMPILATION_REASON_MAP = new java.util.HashMap();

    static {
        COMPILATION_REASON_MAP.put(0, 3);
        COMPILATION_REASON_MAP.put(1, 17);
        COMPILATION_REASON_MAP.put(2, 11);
        COMPILATION_REASON_MAP.put(3, 5);
        COMPILATION_REASON_MAP.put(4, 12);
        COMPILATION_REASON_MAP.put(5, 13);
        COMPILATION_REASON_MAP.put(6, 14);
        COMPILATION_REASON_MAP.put(7, 15);
        COMPILATION_REASON_MAP.put(8, 16);
        COMPILATION_REASON_MAP.put(9, 6);
        COMPILATION_REASON_MAP.put(10, 7);
        COMPILATION_REASON_MAP.put(11, 8);
        COMPILATION_REASON_MAP.put(12, 19);
        COMPILATION_REASON_MAP.put(java.lang.Integer.valueOf(com.android.server.pm.PackageManagerService.REASON_SHARED), 9);
        COMPILE_FILTER_MAP = new java.util.HashMap();
        COMPILE_FILTER_MAP.put("error", 1);
        COMPILE_FILTER_MAP.put("unknown", 2);
        COMPILE_FILTER_MAP.put("assume-verified", 3);
        COMPILE_FILTER_MAP.put("extract", 4);
        COMPILE_FILTER_MAP.put("verify", 5);
        COMPILE_FILTER_MAP.put("quicken", 6);
        COMPILE_FILTER_MAP.put("space-profile", 7);
        COMPILE_FILTER_MAP.put("space", 8);
        COMPILE_FILTER_MAP.put("speed-profile", 9);
        COMPILE_FILTER_MAP.put("speed", 10);
        COMPILE_FILTER_MAP.put("everything-profile", 11);
        COMPILE_FILTER_MAP.put("everything", 12);
        COMPILE_FILTER_MAP.put("run-from-apk", 13);
        COMPILE_FILTER_MAP.put("run-from-apk-fallback", 14);
        COMPILE_FILTER_MAP.put("run-from-vdex-fallback", 15);
        ISA_MAP = new java.util.HashMap();
        ISA_MAP.put("arm", 1);
        ISA_MAP.put("arm64", 2);
        ISA_MAP.put("x86", 3);
        ISA_MAP.put("x86_64", 4);
        ISA_MAP.put("mips", 5);
        ISA_MAP.put("mips64", 6);
    }

    public static void writeStatsLog(com.android.server.pm.dex.ArtStatsLogUtils.ArtStatsLogger logger, long sessionId, java.lang.String compilerFilter, int uid, long compileTime, java.lang.String dexMetadataPath, int compilationReason, int result, int apkType, java.lang.String isa, java.lang.String apkPath) {
        int dexMetadataType = getDexMetadataType(dexMetadataPath);
        logger.write(sessionId, uid, compilationReason, compilerFilter, 10, result, dexMetadataType, apkType, isa);
        logger.write(sessionId, uid, compilationReason, compilerFilter, 11, getDexBytes(apkPath), dexMetadataType, apkType, isa);
        logger.write(sessionId, uid, compilationReason, compilerFilter, 12, compileTime, dexMetadataType, apkType, isa);
    }

    public static int getApkType(final java.lang.String path, java.lang.String baseApkPath, java.lang.String[] splitApkPaths) {
        if (path.equals(baseApkPath)) {
            return 1;
        }
        if (java.util.Arrays.stream(splitApkPaths).anyMatch(new java.util.function.Predicate() { // from class: com.android.server.pm.dex.ArtStatsLogUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((java.lang.String) obj).equals(path);
            }
        })) {
            return 2;
        }
        return 0;
    }

    private static long getDexBytes(java.lang.String apkPath) {
        android.util.jar.StrictJarFile jarFile = null;
        long dexBytes = 0;
        try {
            try {
                jarFile = new android.util.jar.StrictJarFile(apkPath, false, false);
                java.util.regex.Pattern p = java.util.regex.Pattern.compile("classes(\\d)*[.]dex");
                java.util.regex.Matcher m = p.matcher("");
                for (java.util.zip.ZipEntry entry : jarFile) {
                    m.reset(entry.getName());
                    if (m.matches()) {
                        dexBytes += entry.getSize();
                    }
                }
                try {
                    jarFile.close();
                } catch (java.io.IOException e) {
                }
                return dexBytes;
            } catch (java.lang.Throwable th) {
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (java.io.IOException e2) {
                    }
                }
                throw th;
            }
        } catch (java.io.IOException e3) {
            android.util.Slog.e(TAG, "Error when parsing APK " + apkPath);
            if (jarFile == null) {
                return -1L;
            }
            try {
                jarFile.close();
                return -1L;
            } catch (java.io.IOException e4) {
                return -1L;
            }
        }
    }

    private static int getDexMetadataType(java.lang.String dexMetadataPath) {
        if (dexMetadataPath == null) {
            return 4;
        }
        android.util.jar.StrictJarFile jarFile = null;
        try {
            try {
                jarFile = new android.util.jar.StrictJarFile(dexMetadataPath, false, false);
                boolean hasProfile = findFileName(jarFile, PROFILE_DEX_METADATA);
                boolean hasVdex = findFileName(jarFile, VDEX_DEX_METADATA);
                if (hasProfile && hasVdex) {
                    try {
                        jarFile.close();
                        return 3;
                    } catch (java.io.IOException e) {
                        return 3;
                    }
                }
                if (hasProfile) {
                    try {
                        jarFile.close();
                        return 1;
                    } catch (java.io.IOException e2) {
                        return 1;
                    }
                }
                if (!hasVdex) {
                    try {
                        jarFile.close();
                    } catch (java.io.IOException e3) {
                    }
                    return 0;
                }
                try {
                    jarFile.close();
                    return 2;
                } catch (java.io.IOException e4) {
                    return 2;
                }
            } catch (java.io.IOException e5) {
                android.util.Slog.e(TAG, "Error when parsing dex metadata " + dexMetadataPath);
                if (jarFile == null) {
                    return 5;
                }
                try {
                    jarFile.close();
                    return 5;
                } catch (java.io.IOException e6) {
                    return 5;
                }
            }
        } catch (java.lang.Throwable th) {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (java.io.IOException e7) {
                }
            }
            throw th;
        }
    }

    private static boolean findFileName(android.util.jar.StrictJarFile jarFile, java.lang.String filename) throws java.io.IOException {
        for (java.util.zip.ZipEntry entry : jarFile) {
            if (entry.getName().equals(filename)) {
                return true;
            }
        }
        return false;
    }

    public static class ArtStatsLogger {
        public void write(long sessionId, int uid, int compilationReason, java.lang.String compilerFilter, int kind, long value, int dexMetadataType, int apkType, java.lang.String isa) {
            com.android.internal.art.ArtStatsLog.write(com.android.internal.art.ArtStatsLog.ART_DATUM_REPORTED, sessionId, uid, ((java.lang.Integer) com.android.server.pm.dex.ArtStatsLogUtils.COMPILE_FILTER_MAP.getOrDefault(compilerFilter, 2)).intValue(), ((java.lang.Integer) com.android.server.pm.dex.ArtStatsLogUtils.COMPILATION_REASON_MAP.getOrDefault(java.lang.Integer.valueOf(compilationReason), 2)).intValue(), android.os.SystemClock.uptimeMillis(), 1, kind, value, dexMetadataType, apkType, ((java.lang.Integer) com.android.server.pm.dex.ArtStatsLogUtils.ISA_MAP.getOrDefault(isa, 0)).intValue(), 0, 0);
        }
    }
}
