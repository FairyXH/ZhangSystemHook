package com.android.server.pm.parsing;

/* JADX INFO: loaded from: classes2.dex */
public class PackageCacher implements com.android.internal.pm.parsing.IPackageCacher {
    private static final java.lang.String TAG = "PackageCacher";
    public static final java.util.concurrent.atomic.AtomicInteger sCachedPackageReadCount = new java.util.concurrent.atomic.AtomicInteger();
    private final java.io.File mCacheDir;
    private final com.android.internal.pm.parsing.PackageParser2.Callback mCallback;

    public PackageCacher(java.io.File cacheDir) {
        this(cacheDir, null);
    }

    public PackageCacher(java.io.File cacheDir, com.android.internal.pm.parsing.PackageParser2.Callback callback) {
        this.mCacheDir = cacheDir;
        this.mCallback = callback;
    }

    private java.lang.String getCacheKey(java.io.File packageFile, int flags) {
        return packageFile.getName() + '-' + flags + '-' + packageFile.getAbsolutePath().hashCode();
    }

    protected com.android.internal.pm.parsing.pkg.ParsedPackage fromCacheEntry(byte[] bytes) {
        return fromCacheEntryStatic(bytes, this.mCallback);
    }

    public static com.android.internal.pm.parsing.pkg.ParsedPackage fromCacheEntryStatic(byte[] bytes) {
        return fromCacheEntryStatic(bytes, null);
    }

    private static com.android.internal.pm.parsing.pkg.ParsedPackage fromCacheEntryStatic(byte[] bytes, com.android.internal.pm.pkg.parsing.ParsingPackageUtils.Callback callback) {
        android.os.Parcel p = android.os.Parcel.obtain();
        p.unmarshall(bytes, 0, bytes.length);
        p.setDataPosition(0);
        android.content.pm.PackageParserCacheHelper.ReadHelper helper = new android.content.pm.PackageParserCacheHelper.ReadHelper(p);
        helper.startAndInstall();
        com.android.internal.pm.parsing.pkg.PackageImpl packageImpl = new com.android.internal.pm.parsing.pkg.PackageImpl(p, callback);
        p.recycle();
        sCachedPackageReadCount.incrementAndGet();
        return packageImpl;
    }

    protected byte[] toCacheEntry(com.android.internal.pm.parsing.pkg.ParsedPackage pkg) {
        return toCacheEntryStatic(pkg);
    }

    public static byte[] toCacheEntryStatic(com.android.internal.pm.parsing.pkg.ParsedPackage pkg) {
        android.os.Parcel p = android.os.Parcel.obtain();
        android.content.pm.PackageParserCacheHelper.WriteHelper helper = new android.content.pm.PackageParserCacheHelper.WriteHelper(p);
        ((com.android.internal.pm.parsing.pkg.PackageImpl) pkg).writeToParcel(p, 0);
        helper.finishAndUninstall();
        byte[] serialized = p.marshall();
        p.recycle();
        return serialized;
    }

    private static boolean isCacheUpToDate(java.io.File packageFile, java.io.File cacheFile) {
        try {
            if (packageFile.toPath().startsWith(android.os.Environment.getApexDirectory().toPath())) {
                java.io.File backingApexFile = com.android.server.pm.ApexManager.getInstance().getBackingApexFile(packageFile);
                if (backingApexFile == null) {
                    android.util.Slog.w(TAG, "Failed to find APEX file backing " + packageFile.getAbsolutePath());
                } else {
                    packageFile = backingApexFile;
                }
            }
            android.system.StructStat pkg = android.system.Os.stat(packageFile.getAbsolutePath());
            android.system.StructStat cache = android.system.Os.stat(cacheFile.getAbsolutePath());
            return pkg.st_mtime < cache.st_mtime;
        } catch (android.system.ErrnoException ee) {
            if (ee.errno != android.system.OsConstants.ENOENT) {
                android.util.Slog.w("Error while stating package cache : ", ee);
            }
            return false;
        }
    }

    public com.android.internal.pm.parsing.pkg.ParsedPackage getCachedResult(java.io.File packageFile, int flags) {
        java.lang.String cacheKey = getCacheKey(packageFile, flags);
        java.io.File cacheFile = new java.io.File(this.mCacheDir, cacheKey);
        try {
            if (!isCacheUpToDate(packageFile, cacheFile)) {
                return null;
            }
            byte[] bytes = libcore.io.IoUtils.readFileAsByteArray(cacheFile.getAbsolutePath());
            com.android.internal.pm.parsing.pkg.ParsedPackage parsed = fromCacheEntry(bytes);
            if (!packageFile.getAbsolutePath().equals(parsed.getPath())) {
                return null;
            }
            return parsed;
        } catch (java.lang.Throwable e) {
            android.util.Slog.w(TAG, "Error reading package cache: ", e);
            cacheFile.delete();
            return null;
        }
    }

    public void cacheResult(java.io.File packageFile, int flags, com.android.internal.pm.parsing.pkg.ParsedPackage parsed) {
        try {
            java.lang.String cacheKey = getCacheKey(packageFile, flags);
            java.io.File cacheFile = new java.io.File(this.mCacheDir, cacheKey);
            if (cacheFile.exists() && !cacheFile.delete()) {
                android.util.Slog.e(TAG, "Unable to delete cache file: " + cacheFile);
            }
            byte[] cacheEntry = toCacheEntry(parsed);
            if (cacheEntry == null) {
                return;
            }
            try {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(cacheFile);
                try {
                    fos.write(cacheEntry);
                    fos.close();
                } catch (java.lang.Throwable th) {
                    try {
                        fos.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (java.io.IOException ioe) {
                android.util.Slog.w(TAG, "Error writing cache entry.", ioe);
                cacheFile.delete();
            }
        } catch (java.lang.Throwable e) {
            android.util.Slog.w(TAG, "Error saving package cache.", e);
        }
    }

    public void cleanCachedResult(java.io.File packageFile) {
        final java.lang.String packageName = packageFile.getName();
        java.io.File[] files = android.os.FileUtils.listFilesOrEmpty(this.mCacheDir, new java.io.FilenameFilter() { // from class: com.android.server.pm.parsing.PackageCacher$$ExternalSyntheticLambda0
            @Override // java.io.FilenameFilter
            public final boolean accept(java.io.File file, java.lang.String str) {
                return str.startsWith(packageName);
            }
        });
        for (java.io.File file : files) {
            if (!file.delete()) {
                android.util.Slog.e(TAG, "Unable to clean cache file: " + file);
            }
        }
    }
}
