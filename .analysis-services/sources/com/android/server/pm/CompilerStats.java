package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class CompilerStats extends com.android.server.pm.AbstractStatsBase<java.lang.Void> {
    private static final int COMPILER_STATS_VERSION = 1;
    private static final java.lang.String COMPILER_STATS_VERSION_HEADER = "PACKAGE_MANAGER__COMPILER_STATS__";
    private final java.util.Map<java.lang.String, com.android.server.pm.CompilerStats.PackageStats> packageStats;

    static class PackageStats {
        private final java.util.Map<java.lang.String, java.lang.Long> compileTimePerCodePath = new android.util.ArrayMap(2);
        private final java.lang.String packageName;

        public PackageStats(java.lang.String packageName) {
            this.packageName = packageName;
        }

        public java.lang.String getPackageName() {
            return this.packageName;
        }

        public long getCompileTime(java.lang.String codePath) {
            java.lang.String storagePath = getStoredPathFromCodePath(codePath);
            synchronized (this.compileTimePerCodePath) {
                java.lang.Long l = this.compileTimePerCodePath.get(storagePath);
                if (l == null) {
                    return 0L;
                }
                return l.longValue();
            }
        }

        public void setCompileTime(java.lang.String codePath, long compileTimeInMs) {
            java.lang.String storagePath = getStoredPathFromCodePath(codePath);
            synchronized (this.compileTimePerCodePath) {
                if (compileTimeInMs <= 0) {
                    this.compileTimePerCodePath.remove(storagePath);
                } else {
                    this.compileTimePerCodePath.put(storagePath, java.lang.Long.valueOf(compileTimeInMs));
                }
            }
        }

        private static java.lang.String getStoredPathFromCodePath(java.lang.String codePath) {
            int lastSlash = codePath.lastIndexOf(java.io.File.separatorChar);
            return codePath.substring(lastSlash + 1);
        }

        public void dump(com.android.internal.util.IndentingPrintWriter ipw) {
            synchronized (this.compileTimePerCodePath) {
                if (this.compileTimePerCodePath.size() == 0) {
                    ipw.println("(No recorded stats)");
                } else {
                    for (java.util.Map.Entry<java.lang.String, java.lang.Long> e : this.compileTimePerCodePath.entrySet()) {
                        ipw.println(" " + e.getKey() + " - " + e.getValue());
                    }
                }
            }
        }
    }

    public CompilerStats() {
        super("package-cstats.list", "CompilerStats_DiskWriter", false);
        this.packageStats = new java.util.HashMap();
    }

    public com.android.server.pm.CompilerStats.PackageStats getPackageStats(java.lang.String packageName) {
        com.android.server.pm.CompilerStats.PackageStats packageStats;
        synchronized (this.packageStats) {
            packageStats = this.packageStats.get(packageName);
        }
        return packageStats;
    }

    public void setPackageStats(java.lang.String packageName, com.android.server.pm.CompilerStats.PackageStats stats) {
        synchronized (this.packageStats) {
            this.packageStats.put(packageName, stats);
        }
    }

    public com.android.server.pm.CompilerStats.PackageStats createPackageStats(java.lang.String packageName) {
        com.android.server.pm.CompilerStats.PackageStats newStats;
        synchronized (this.packageStats) {
            newStats = new com.android.server.pm.CompilerStats.PackageStats(packageName);
            this.packageStats.put(packageName, newStats);
        }
        return newStats;
    }

    public com.android.server.pm.CompilerStats.PackageStats getOrCreatePackageStats(java.lang.String packageName) {
        synchronized (this.packageStats) {
            com.android.server.pm.CompilerStats.PackageStats existingStats = this.packageStats.get(packageName);
            if (existingStats != null) {
                return existingStats;
            }
            return createPackageStats(packageName);
        }
    }

    public void deletePackageStats(java.lang.String packageName) {
        synchronized (this.packageStats) {
            this.packageStats.remove(packageName);
        }
    }

    public void write(java.io.Writer out) {
        com.android.internal.util.FastPrintWriter fpw = new com.android.internal.util.FastPrintWriter(out);
        fpw.print(COMPILER_STATS_VERSION_HEADER);
        fpw.println(1);
        synchronized (this.packageStats) {
            for (com.android.server.pm.CompilerStats.PackageStats pkg : this.packageStats.values()) {
                synchronized (pkg.compileTimePerCodePath) {
                    if (!pkg.compileTimePerCodePath.isEmpty()) {
                        fpw.println(pkg.getPackageName());
                        for (java.util.Map.Entry<java.lang.String, java.lang.Long> e : pkg.compileTimePerCodePath.entrySet()) {
                            fpw.println("-" + e.getKey() + ":" + e.getValue());
                        }
                    }
                }
            }
        }
        fpw.flush();
    }

    public boolean read(java.io.Reader r) {
        java.lang.String s;
        synchronized (this.packageStats) {
            this.packageStats.clear();
            try {
                java.io.BufferedReader in = new java.io.BufferedReader(r);
                java.lang.String versionLine = in.readLine();
                if (versionLine == null) {
                    throw new java.lang.IllegalArgumentException("No version line found.");
                }
                if (!versionLine.startsWith(COMPILER_STATS_VERSION_HEADER)) {
                    throw new java.lang.IllegalArgumentException("Invalid version line: " + versionLine);
                }
                int version = java.lang.Integer.parseInt(versionLine.substring(COMPILER_STATS_VERSION_HEADER.length()));
                if (version != 1) {
                    throw new java.lang.IllegalArgumentException("Unexpected version: " + version);
                }
                com.android.server.pm.CompilerStats.PackageStats currentPackage = new com.android.server.pm.CompilerStats.PackageStats("fake package");
                while (true) {
                    s = in.readLine();
                    if (s != null) {
                        if (s.startsWith("-")) {
                            int colonIndex = s.indexOf(58);
                            if (colonIndex == -1 || colonIndex == 1) {
                                break;
                            }
                            java.lang.String codePath = s.substring(1, colonIndex);
                            long time = java.lang.Long.parseLong(s.substring(colonIndex + 1));
                            currentPackage.setCompileTime(codePath, time);
                        } else {
                            currentPackage = getOrCreatePackageStats(s);
                        }
                    }
                }
                throw new java.lang.IllegalArgumentException("Could not parse data " + s);
            } catch (java.lang.Exception e) {
                android.util.Log.e("PackageManager", "Error parsing compiler stats", e);
                return false;
            }
        }
        return true;
    }

    void writeNow() {
        writeNow(null);
    }

    boolean maybeWriteAsync() {
        return maybeWriteAsync(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public void writeInternal(java.lang.Void data) {
        android.util.AtomicFile file = getFile();
        java.io.FileOutputStream f = null;
        try {
            f = file.startWrite();
            java.io.OutputStreamWriter osw = new java.io.OutputStreamWriter(f);
            write(osw);
            osw.flush();
            file.finishWrite(f);
        } catch (java.io.IOException e) {
            if (f != null) {
                file.failWrite(f);
            }
            android.util.Log.e("PackageManager", "Failed to write compiler stats", e);
        }
    }

    void read() {
        read((java.lang.Object) null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public void readInternal(java.lang.Void data) {
        android.util.AtomicFile file = getFile();
        java.io.BufferedReader in = null;
        try {
            in = new java.io.BufferedReader(new java.io.InputStreamReader(file.openRead()));
            read((java.io.Reader) in);
        } catch (java.io.FileNotFoundException e) {
        } catch (java.lang.Throwable th) {
            libcore.io.IoUtils.closeQuietly(in);
            throw th;
        }
        libcore.io.IoUtils.closeQuietly(in);
    }
}
