package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class PackageUsage extends com.android.server.pm.AbstractStatsBase<java.util.Map<java.lang.String, com.android.server.pm.PackageSetting>> {
    private static final java.lang.String USAGE_FILE_MAGIC = "PACKAGE_USAGE__VERSION_";
    private static final java.lang.String USAGE_FILE_MAGIC_VERSION_1 = "PACKAGE_USAGE__VERSION_1";
    private boolean mIsHistoricalPackageUsageAvailable;

    PackageUsage() {
        super("package-usage.list", "PackageUsage_DiskWriter", true);
        this.mIsHistoricalPackageUsageAvailable = true;
    }

    boolean isHistoricalPackageUsageAvailable() {
        return this.mIsHistoricalPackageUsageAvailable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public void writeInternal(java.util.Map<java.lang.String, com.android.server.pm.PackageSetting> pkgSettings) {
        android.util.AtomicFile file = getFile();
        java.io.FileOutputStream f = null;
        try {
            f = file.startWrite();
            java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(f);
            android.os.FileUtils.setPermissions(file.getBaseFile().getPath(), com.android.internal.util.FrameworkStatsLog.DISPLAY_HBM_STATE_CHANGED, 1000, 1032);
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(USAGE_FILE_MAGIC_VERSION_1);
            sb.append('\n');
            out.write(sb.toString().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
            for (com.android.server.pm.PackageSetting pkgSetting : pkgSettings.values()) {
                if (pkgSetting != null && pkgSetting.getPkgState() != null && pkgSetting.getPkgState().getLatestPackageUseTimeInMills() != 0) {
                    sb.setLength(0);
                    sb.append(pkgSetting.getPackageName());
                    for (long usageTimeInMillis : pkgSetting.getPkgState().getLastPackageUsageTimeInMills()) {
                        sb.append(' ');
                        sb.append(usageTimeInMillis);
                    }
                    sb.append('\n');
                    out.write(sb.toString().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
                }
            }
            out.flush();
            file.finishWrite(f);
        } catch (java.io.IOException e) {
            if (f != null) {
                file.failWrite(f);
            }
            android.util.Log.e("PackageManager", "Failed to write package usage times", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public void readInternal(java.util.Map<java.lang.String, com.android.server.pm.PackageSetting> pkgSettings) {
        android.util.AtomicFile file = getFile();
        java.io.BufferedInputStream in = null;
        try {
            try {
                in = new java.io.BufferedInputStream(file.openRead());
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                java.lang.String firstLine = readLine(in, sb);
                if (firstLine != null) {
                    if (USAGE_FILE_MAGIC_VERSION_1.equals(firstLine)) {
                        readVersion1LP(pkgSettings, in, sb);
                    } else {
                        readVersion0LP(pkgSettings, in, sb, firstLine);
                    }
                }
            } catch (java.io.FileNotFoundException e) {
                this.mIsHistoricalPackageUsageAvailable = false;
            } catch (java.io.IOException e2) {
                android.util.Log.w("PackageManager", "Failed to read package usage times", e2);
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(in);
        }
    }

    private void readVersion0LP(java.util.Map<java.lang.String, com.android.server.pm.PackageSetting> pkgSettings, java.io.InputStream in, java.lang.StringBuilder sb, java.lang.String firstLine) throws java.io.IOException {
        java.lang.String line = firstLine;
        while (line != null) {
            java.lang.String[] tokens = line.split(" ");
            if (tokens.length != 2) {
                throw new java.io.IOException("Failed to parse " + line + " as package-timestamp pair.");
            }
            java.lang.String packageName = tokens[0];
            com.android.server.pm.PackageSetting pkgSetting = pkgSettings.get(packageName);
            if (pkgSetting != null) {
                long timestamp = parseAsLong(tokens[1]);
                for (int reason = 0; reason < 8; reason++) {
                    pkgSetting.getPkgState().setLastPackageUsageTimeInMills(reason, timestamp);
                }
            }
            line = readLine(in, sb);
        }
    }

    private void readVersion1LP(java.util.Map<java.lang.String, com.android.server.pm.PackageSetting> pkgSettings, java.io.InputStream in, java.lang.StringBuilder sb) throws java.io.IOException {
        while (true) {
            java.lang.String line = readLine(in, sb);
            if (line != null) {
                java.lang.String[] tokens = line.split(" ");
                if (tokens.length != 9) {
                    throw new java.io.IOException("Failed to parse " + line + " as a timestamp array.");
                }
                java.lang.String packageName = tokens[0];
                com.android.server.pm.PackageSetting pkgSetting = pkgSettings.get(packageName);
                if (pkgSetting != null) {
                    for (int reason = 0; reason < 8; reason++) {
                        pkgSetting.getPkgState().setLastPackageUsageTimeInMills(reason, parseAsLong(tokens[reason + 1]));
                    }
                }
            } else {
                return;
            }
        }
    }

    private long parseAsLong(java.lang.String token) throws java.io.IOException {
        try {
            return java.lang.Long.parseLong(token);
        } catch (java.lang.NumberFormatException e) {
            throw new java.io.IOException("Failed to parse " + token + " as a long.", e);
        }
    }

    private java.lang.String readLine(java.io.InputStream in, java.lang.StringBuilder sb) throws java.io.IOException {
        return readToken(in, sb, '\n');
    }

    private java.lang.String readToken(java.io.InputStream in, java.lang.StringBuilder sb, char endOfToken) throws java.io.IOException {
        sb.setLength(0);
        while (true) {
            int ch = in.read();
            if (ch == -1) {
                if (sb.length() == 0) {
                    return null;
                }
                throw new java.io.IOException("Unexpected EOF");
            }
            if (ch == endOfToken) {
                return sb.toString();
            }
            sb.append((char) ch);
        }
    }
}
