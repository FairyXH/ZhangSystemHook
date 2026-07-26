package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public class PackageDexUsage extends com.android.server.pm.AbstractStatsBase<java.lang.Void> {
    private static final java.lang.String CODE_PATH_LINE_CHAR = "+";
    private static final java.lang.String DEX_LINE_CHAR = "#";
    private static final java.lang.String LOADING_PACKAGE_CHAR = "@";
    static final int MAX_SECONDARY_FILES_PER_OWNER = 100;
    private static final int PACKAGE_DEX_USAGE_VERSION = 2;
    private static final java.lang.String PACKAGE_DEX_USAGE_VERSION_HEADER = "PACKAGE_MANAGER__PACKAGE_DEX_USAGE__";
    private static final java.lang.String SPLIT_CHAR = ",";
    private static final java.lang.String TAG = "PackageDexUsage";
    static final java.lang.String UNSUPPORTED_CLASS_LOADER_CONTEXT = "=UnsupportedClassLoaderContext=";
    static final java.lang.String VARIABLE_CLASS_LOADER_CONTEXT = "=VariableClassLoaderContext=";
    private final java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo> mPackageUseInfoMap;

    PackageDexUsage() {
        super("package-dex-usage.list", "PackageDexUsage_DiskWriter", false);
        this.mPackageUseInfoMap = new java.util.HashMap();
    }

    boolean record(java.lang.String owningPackageName, java.lang.String dexPath, int ownerUserId, java.lang.String loaderIsa, boolean primaryOrSplit, java.lang.String loadingPackageName, java.lang.String classLoaderContext, boolean overwriteCLC) throws java.lang.Throwable {
        if (!com.android.server.pm.PackageManagerServiceUtils.checkISA(loaderIsa)) {
            throw new java.lang.IllegalArgumentException("loaderIsa " + loaderIsa + " is unsupported");
        }
        if (classLoaderContext == null) {
            throw new java.lang.IllegalArgumentException("Null classLoaderContext");
        }
        if (classLoaderContext.equals(UNSUPPORTED_CLASS_LOADER_CONTEXT)) {
            android.util.Slog.e(TAG, "Unsupported context?");
            return false;
        }
        boolean isUsedByOtherApps = !owningPackageName.equals(loadingPackageName);
        synchronized (this.mPackageUseInfoMap) {
            try {
                try {
                    com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo = this.mPackageUseInfoMap.get(owningPackageName);
                    if (packageUseInfo == null) {
                        com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo2 = new com.android.server.pm.dex.PackageDexUsage.PackageUseInfo(owningPackageName);
                        if (primaryOrSplit) {
                            packageUseInfo2.mergePrimaryCodePaths(dexPath, loadingPackageName);
                        } else {
                            com.android.server.pm.dex.PackageDexUsage.DexUseInfo newData = new com.android.server.pm.dex.PackageDexUsage.DexUseInfo(isUsedByOtherApps, ownerUserId, classLoaderContext, loaderIsa);
                            packageUseInfo2.mDexUseInfoMap.put(dexPath, newData);
                            maybeAddLoadingPackage(owningPackageName, loadingPackageName, newData.mLoadingPackages);
                        }
                        this.mPackageUseInfoMap.put(owningPackageName, packageUseInfo2);
                        return true;
                    }
                    if (primaryOrSplit) {
                        return packageUseInfo.mergePrimaryCodePaths(dexPath, loadingPackageName);
                    }
                    com.android.server.pm.dex.PackageDexUsage.DexUseInfo newData2 = new com.android.server.pm.dex.PackageDexUsage.DexUseInfo(isUsedByOtherApps, ownerUserId, classLoaderContext, loaderIsa);
                    boolean updateLoadingPackages = maybeAddLoadingPackage(owningPackageName, loadingPackageName, newData2.mLoadingPackages);
                    com.android.server.pm.dex.PackageDexUsage.DexUseInfo existingData = (com.android.server.pm.dex.PackageDexUsage.DexUseInfo) packageUseInfo.mDexUseInfoMap.get(dexPath);
                    if (existingData == null) {
                        if (packageUseInfo.mDexUseInfoMap.size() >= 100) {
                            return updateLoadingPackages;
                        }
                        packageUseInfo.mDexUseInfoMap.put(dexPath, newData2);
                        return true;
                    }
                    if (ownerUserId == existingData.mOwnerUserId) {
                        return existingData.merge(newData2, overwriteCLC) || updateLoadingPackages;
                    }
                    throw new java.lang.IllegalArgumentException("Trying to change ownerUserId for  dex path " + dexPath + " from " + existingData.mOwnerUserId + " to " + ownerUserId);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    void read() {
        read((java.lang.Object) null);
    }

    void maybeWriteAsync() {
        maybeWriteAsync(null);
    }

    void writeNow() {
        writeInternal((java.lang.Void) null);
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
            android.util.Slog.e(TAG, "Failed to write usage for dex files", e);
        }
    }

    void write(java.io.Writer out) {
        java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo> packageUseInfoMapClone = clonePackageUseInfoMap();
        com.android.internal.util.FastPrintWriter fpw = new com.android.internal.util.FastPrintWriter(out);
        fpw.print(PACKAGE_DEX_USAGE_VERSION_HEADER);
        int i = 2;
        fpw.println(2);
        for (java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo> pEntry : packageUseInfoMapClone.entrySet()) {
            java.lang.String packageName = pEntry.getKey();
            com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo = pEntry.getValue();
            fpw.println(packageName);
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> codeEntry : packageUseInfo.mPrimaryCodePaths.entrySet()) {
                java.lang.String codePath = codeEntry.getKey();
                java.util.Set<java.lang.String> loadingPackages = codeEntry.getValue();
                fpw.println(CODE_PATH_LINE_CHAR + codePath);
                fpw.println(LOADING_PACKAGE_CHAR + java.lang.String.join(SPLIT_CHAR, loadingPackages));
            }
            for (java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDexUsage.DexUseInfo> dEntry : packageUseInfo.mDexUseInfoMap.entrySet()) {
                java.lang.String dexPath = dEntry.getKey();
                com.android.server.pm.dex.PackageDexUsage.DexUseInfo dexUseInfo = dEntry.getValue();
                fpw.println(DEX_LINE_CHAR + dexPath);
                java.lang.CharSequence[] charSequenceArr = new java.lang.CharSequence[i];
                charSequenceArr[0] = java.lang.Integer.toString(dexUseInfo.mOwnerUserId);
                charSequenceArr[1] = writeBoolean(dexUseInfo.mIsUsedByOtherApps);
                fpw.print(java.lang.String.join(SPLIT_CHAR, charSequenceArr));
                for (java.lang.String isa : dexUseInfo.mLoaderIsas) {
                    fpw.print(SPLIT_CHAR + isa);
                    packageUseInfoMapClone = packageUseInfoMapClone;
                }
                fpw.println();
                fpw.println(LOADING_PACKAGE_CHAR + java.lang.String.join(SPLIT_CHAR, dexUseInfo.mLoadingPackages));
                fpw.println(dexUseInfo.getClassLoaderContext());
                packageUseInfoMapClone = packageUseInfoMapClone;
                i = 2;
            }
            i = 2;
        }
        fpw.flush();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public void readInternal(java.lang.Void data) {
        android.util.AtomicFile file = getFile();
        java.io.BufferedReader in = null;
        try {
            try {
                in = new java.io.BufferedReader(new java.io.InputStreamReader(file.openRead()));
                read((java.io.Reader) in);
            } catch (java.io.FileNotFoundException e) {
            } catch (java.io.IOException e2) {
                android.util.Slog.w(TAG, "Failed to parse package dex usage.", e2);
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(in);
        }
    }

    void read(java.io.Reader reader) throws java.io.IOException {
        java.lang.String currentPackage;
        java.util.Set<java.lang.String> loadingPackages;
        java.lang.String classLoaderContext;
        java.lang.String[] elems;
        java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo> data = new java.util.HashMap<>();
        java.io.BufferedReader in = new java.io.BufferedReader(reader);
        java.lang.String versionLine = in.readLine();
        if (versionLine == null) {
            throw new java.lang.IllegalStateException("No version line found.");
        }
        if (!versionLine.startsWith(PACKAGE_DEX_USAGE_VERSION_HEADER)) {
            throw new java.lang.IllegalStateException("Invalid version line: " + versionLine);
        }
        int version = java.lang.Integer.parseInt(versionLine.substring(PACKAGE_DEX_USAGE_VERSION_HEADER.length()));
        if (!isSupportedVersion(version)) {
            android.util.Slog.w(TAG, "Unexpected package-dex-use version: " + version + ". Not reading from it");
            return;
        }
        java.util.Set<java.lang.String> supportedIsas = new java.util.HashSet<>();
        char c = 0;
        for (java.lang.String abi : android.os.Build.SUPPORTED_ABIS) {
            supportedIsas.add(dalvik.system.VMRuntime.getInstructionSet(abi));
        }
        com.android.server.pm.dex.PackageDexUsage.PackageUseInfo currentPackageData = null;
        java.lang.String currentPackage2 = null;
        while (true) {
            java.lang.String line = in.readLine();
            if (line == null) {
                synchronized (this.mPackageUseInfoMap) {
                    this.mPackageUseInfoMap.clear();
                    this.mPackageUseInfoMap.putAll(data);
                }
                return;
            }
            if (line.startsWith(DEX_LINE_CHAR)) {
                if (currentPackage2 == null) {
                    throw new java.lang.IllegalStateException("Malformed PackageDexUsage file. Expected package line before dex line.");
                }
                java.lang.String dexPath = line.substring(DEX_LINE_CHAR.length());
                java.lang.String line2 = in.readLine();
                if (line2 == null) {
                    throw new java.lang.IllegalStateException("Could not find dexUseInfo line");
                }
                java.lang.String[] elems2 = line2.split(SPLIT_CHAR);
                if (elems2.length < 3) {
                    throw new java.lang.IllegalStateException("Invalid PackageDexUsage line: " + line2);
                }
                java.util.Set<java.lang.String> loadingPackages2 = readLoadingPackages(in, version);
                java.lang.String classLoaderContext2 = readClassLoaderContext(in, version);
                if (UNSUPPORTED_CLASS_LOADER_CONTEXT.equals(classLoaderContext2)) {
                    currentPackage = currentPackage2;
                } else {
                    int ownerUserId = java.lang.Integer.parseInt(elems2[c]);
                    boolean isUsedByOtherApps = readBoolean(elems2[1]);
                    currentPackage = currentPackage2;
                    com.android.server.pm.dex.PackageDexUsage.DexUseInfo dexUseInfo = new com.android.server.pm.dex.PackageDexUsage.DexUseInfo(isUsedByOtherApps, ownerUserId, classLoaderContext2, null);
                    dexUseInfo.mLoadingPackages.addAll(loadingPackages2);
                    int i = 2;
                    while (true) {
                        boolean isUsedByOtherApps2 = isUsedByOtherApps;
                        if (i >= elems2.length) {
                            break;
                        }
                        java.lang.String isa = elems2[i];
                        if (supportedIsas.contains(isa)) {
                            loadingPackages = loadingPackages2;
                            classLoaderContext = classLoaderContext2;
                            dexUseInfo.mLoaderIsas.add(elems2[i]);
                            elems = elems2;
                        } else {
                            loadingPackages = loadingPackages2;
                            classLoaderContext = classLoaderContext2;
                            elems = elems2;
                            android.util.Slog.wtf(TAG, "Unsupported ISA when parsing PackageDexUsage: " + isa);
                        }
                        i++;
                        isUsedByOtherApps = isUsedByOtherApps2;
                        loadingPackages2 = loadingPackages;
                        classLoaderContext2 = classLoaderContext;
                        elems2 = elems;
                    }
                    if (supportedIsas.isEmpty()) {
                        android.util.Slog.wtf(TAG, "Ignore dexPath when parsing PackageDexUsage because of unsupported isas. dexPath=" + dexPath);
                    } else {
                        currentPackageData.mDexUseInfoMap.put(dexPath, dexUseInfo);
                    }
                }
            } else {
                currentPackage = currentPackage2;
                if (line.startsWith(CODE_PATH_LINE_CHAR)) {
                    java.lang.String codePath = line.substring(CODE_PATH_LINE_CHAR.length());
                    currentPackageData.mPrimaryCodePaths.put(codePath, readLoadingPackages(in, version));
                } else {
                    currentPackage2 = line;
                    currentPackageData = new com.android.server.pm.dex.PackageDexUsage.PackageUseInfo(currentPackage2);
                    data.put(currentPackage2, currentPackageData);
                    c = 0;
                }
            }
            currentPackage2 = currentPackage;
            c = 0;
        }
    }

    private java.lang.String readClassLoaderContext(java.io.BufferedReader in, int version) throws java.io.IOException {
        java.lang.String context = in.readLine();
        if (context == null) {
            throw new java.lang.IllegalStateException("Could not find the classLoaderContext line.");
        }
        return context;
    }

    private java.util.Set<java.lang.String> readLoadingPackages(java.io.BufferedReader in, int version) throws java.io.IOException {
        java.lang.String line = in.readLine();
        if (line == null) {
            throw new java.lang.IllegalStateException("Could not find the loadingPackages line.");
        }
        java.util.Set<java.lang.String> result = new java.util.HashSet<>();
        if (line.length() != LOADING_PACKAGE_CHAR.length()) {
            java.util.Collections.addAll(result, line.substring(LOADING_PACKAGE_CHAR.length()).split(SPLIT_CHAR));
        }
        return result;
    }

    private boolean maybeAddLoadingPackage(java.lang.String owningPackage, java.lang.String loadingPackage, java.util.Set<java.lang.String> loadingPackages) {
        return !owningPackage.equals(loadingPackage) && loadingPackages.add(loadingPackage);
    }

    private boolean isSupportedVersion(int version) {
        return version == 2;
    }

    void syncData(java.util.Map<java.lang.String, java.util.Set<java.lang.Integer>> packageToUsersMap, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> packageToCodePaths, java.util.List<java.lang.String> packagesToKeepDataAbout) {
        synchronized (this.mPackageUseInfoMap) {
            try {
                try {
                    java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo>> pIt = this.mPackageUseInfoMap.entrySet().iterator();
                    while (pIt.hasNext()) {
                        java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo> pEntry = pIt.next();
                        java.lang.String packageName = pEntry.getKey();
                        if (!packagesToKeepDataAbout.contains(packageName)) {
                            com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo = pEntry.getValue();
                            java.util.Set<java.lang.Integer> users = packageToUsersMap.get(packageName);
                            if (users == null) {
                                pIt.remove();
                            } else {
                                java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDexUsage.DexUseInfo>> dIt = packageUseInfo.mDexUseInfoMap.entrySet().iterator();
                                while (dIt.hasNext()) {
                                    com.android.server.pm.dex.PackageDexUsage.DexUseInfo dexUseInfo = dIt.next().getValue();
                                    if (!users.contains(java.lang.Integer.valueOf(dexUseInfo.mOwnerUserId))) {
                                        dIt.remove();
                                    }
                                }
                                java.util.Set<java.lang.String> codePaths = packageToCodePaths.get(packageName);
                                java.util.Iterator<java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>>> recordedIt = packageUseInfo.mPrimaryCodePaths.entrySet().iterator();
                                while (recordedIt.hasNext()) {
                                    java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry = recordedIt.next();
                                    java.lang.String recordedCodePath = entry.getKey();
                                    if (!codePaths.contains(recordedCodePath)) {
                                        recordedIt.remove();
                                    } else {
                                        java.util.Set<java.lang.String> recordedLoadingPackages = entry.getValue();
                                        java.util.Iterator<java.lang.String> recordedLoadingPackagesIt = recordedLoadingPackages.iterator();
                                        while (recordedLoadingPackagesIt.hasNext()) {
                                            java.lang.String recordedLoadingPackage = recordedLoadingPackagesIt.next();
                                            if (!packagesToKeepDataAbout.contains(recordedLoadingPackage) && !packageToUsersMap.containsKey(recordedLoadingPackage)) {
                                                recordedLoadingPackagesIt.remove();
                                            }
                                        }
                                    }
                                }
                                if (!packageUseInfo.isAnyCodePathUsedByOtherApps() && packageUseInfo.mDexUseInfoMap.isEmpty()) {
                                    pIt.remove();
                                }
                            }
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    boolean clearUsedByOtherApps(java.lang.String packageName) {
        synchronized (this.mPackageUseInfoMap) {
            com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo = this.mPackageUseInfoMap.get(packageName);
            if (packageUseInfo == null) {
                return false;
            }
            return packageUseInfo.clearCodePathUsedByOtherApps();
        }
    }

    boolean removePackage(java.lang.String packageName) {
        boolean z;
        synchronized (this.mPackageUseInfoMap) {
            z = this.mPackageUseInfoMap.remove(packageName) != null;
        }
        return z;
    }

    boolean removeUserPackage(java.lang.String packageName, int userId) {
        synchronized (this.mPackageUseInfoMap) {
            com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo = this.mPackageUseInfoMap.get(packageName);
            if (packageUseInfo == null) {
                return false;
            }
            boolean updated = false;
            java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDexUsage.DexUseInfo>> dIt = packageUseInfo.mDexUseInfoMap.entrySet().iterator();
            while (dIt.hasNext()) {
                com.android.server.pm.dex.PackageDexUsage.DexUseInfo dexUseInfo = dIt.next().getValue();
                if (dexUseInfo.mOwnerUserId == userId) {
                    dIt.remove();
                    updated = true;
                }
            }
            if (packageUseInfo.mDexUseInfoMap.isEmpty() && !packageUseInfo.isAnyCodePathUsedByOtherApps()) {
                this.mPackageUseInfoMap.remove(packageName);
                updated = true;
            }
            return updated;
        }
    }

    boolean removeDexFile(java.lang.String packageName, java.lang.String dexFile, int userId) {
        synchronized (this.mPackageUseInfoMap) {
            com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo = this.mPackageUseInfoMap.get(packageName);
            if (packageUseInfo == null) {
                return false;
            }
            return removeDexFile(packageUseInfo, dexFile, userId);
        }
    }

    private boolean removeDexFile(com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo, java.lang.String dexFile, int userId) {
        com.android.server.pm.dex.PackageDexUsage.DexUseInfo dexUseInfo = (com.android.server.pm.dex.PackageDexUsage.DexUseInfo) packageUseInfo.mDexUseInfoMap.get(dexFile);
        if (dexUseInfo == null || dexUseInfo.mOwnerUserId != userId) {
            return false;
        }
        packageUseInfo.mDexUseInfoMap.remove(dexFile);
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    com.android.server.pm.dex.PackageDexUsage.PackageUseInfo getPackageUseInfo(java.lang.String str) {
        com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo;
        synchronized (this.mPackageUseInfoMap) {
            com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo2 = this.mPackageUseInfoMap.get(str);
            packageUseInfo = null;
            java.lang.Object[] objArr = 0;
            if (packageUseInfo2 != null) {
                packageUseInfo = new com.android.server.pm.dex.PackageDexUsage.PackageUseInfo(packageUseInfo2);
            }
        }
        return packageUseInfo;
    }

    java.util.Set<java.lang.String> getAllPackagesWithSecondaryDexFiles() {
        java.util.Set<java.lang.String> packages = new java.util.HashSet<>();
        synchronized (this.mPackageUseInfoMap) {
            for (java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo> entry : this.mPackageUseInfoMap.entrySet()) {
                if (!entry.getValue().mDexUseInfoMap.isEmpty()) {
                    packages.add(entry.getKey());
                }
            }
        }
        return packages;
    }

    void clear() {
        synchronized (this.mPackageUseInfoMap) {
            this.mPackageUseInfoMap.clear();
        }
    }

    private java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo> clonePackageUseInfoMap() {
        java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo> clone = new java.util.HashMap<>();
        synchronized (this.mPackageUseInfoMap) {
            for (java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo> e : this.mPackageUseInfoMap.entrySet()) {
                clone.put(e.getKey(), new com.android.server.pm.dex.PackageDexUsage.PackageUseInfo(e.getValue()));
            }
        }
        return clone;
    }

    private java.lang.String writeBoolean(boolean bool) {
        return bool ? "1" : "0";
    }

    private boolean readBoolean(java.lang.String bool) {
        if ("0".equals(bool)) {
            return false;
        }
        if ("1".equals(bool)) {
            return true;
        }
        throw new java.lang.IllegalArgumentException("Unknown bool encoding: " + bool);
    }

    java.lang.String dump() {
        java.io.StringWriter sw = new java.io.StringWriter();
        write(sw);
        return sw.toString();
    }

    public static class PackageUseInfo {
        private final java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDexUsage.DexUseInfo> mDexUseInfoMap;
        private final java.lang.String mPackageName;
        private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> mPrimaryCodePaths;

        PackageUseInfo(java.lang.String packageName) {
            this.mPrimaryCodePaths = new java.util.HashMap();
            this.mDexUseInfoMap = new java.util.HashMap();
            this.mPackageName = packageName;
        }

        private PackageUseInfo(com.android.server.pm.dex.PackageDexUsage.PackageUseInfo other) {
            this.mPackageName = other.mPackageName;
            this.mPrimaryCodePaths = new java.util.HashMap();
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> e : other.mPrimaryCodePaths.entrySet()) {
                this.mPrimaryCodePaths.put(e.getKey(), new java.util.HashSet(e.getValue()));
            }
            this.mDexUseInfoMap = new java.util.HashMap();
            for (java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDexUsage.DexUseInfo> e2 : other.mDexUseInfoMap.entrySet()) {
                this.mDexUseInfoMap.put(e2.getKey(), new com.android.server.pm.dex.PackageDexUsage.DexUseInfo(e2.getValue()));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean mergePrimaryCodePaths(java.lang.String codePath, java.lang.String loadingPackage) {
            java.util.Set<java.lang.String> loadingPackages = this.mPrimaryCodePaths.get(codePath);
            if (loadingPackages == null) {
                loadingPackages = new java.util.HashSet();
                this.mPrimaryCodePaths.put(codePath, loadingPackages);
            }
            return loadingPackages.add(loadingPackage);
        }

        public boolean isUsedByOtherApps(java.lang.String codePath) {
            if (!this.mPrimaryCodePaths.containsKey(codePath)) {
                return false;
            }
            java.util.Set<java.lang.String> loadingPackages = this.mPrimaryCodePaths.get(codePath);
            if (loadingPackages.contains(this.mPackageName)) {
                return loadingPackages.size() > 1;
            }
            return !loadingPackages.isEmpty();
        }

        public java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDexUsage.DexUseInfo> getDexUseInfoMap() {
            return this.mDexUseInfoMap;
        }

        public java.util.Set<java.lang.String> getLoadingPackages(java.lang.String codePath) {
            return this.mPrimaryCodePaths.getOrDefault(codePath, null);
        }

        public boolean isAnyCodePathUsedByOtherApps() {
            return !this.mPrimaryCodePaths.isEmpty();
        }

        boolean clearCodePathUsedByOtherApps() {
            boolean updated = false;
            java.util.List<java.lang.String> retainOnlyOwningPackage = new java.util.ArrayList<>(1);
            retainOnlyOwningPackage.add(this.mPackageName);
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : this.mPrimaryCodePaths.entrySet()) {
                if (entry.getValue().retainAll(retainOnlyOwningPackage)) {
                    updated = true;
                }
            }
            return updated;
        }
    }

    public static class DexUseInfo {
        private java.lang.String mClassLoaderContext;
        private boolean mIsUsedByOtherApps;
        private final java.util.Set<java.lang.String> mLoaderIsas;
        private final java.util.Set<java.lang.String> mLoadingPackages;
        private final int mOwnerUserId;

        DexUseInfo(boolean isUsedByOtherApps, int ownerUserId, java.lang.String classLoaderContext, java.lang.String loaderIsa) {
            this.mIsUsedByOtherApps = isUsedByOtherApps;
            this.mOwnerUserId = ownerUserId;
            this.mClassLoaderContext = classLoaderContext;
            this.mLoaderIsas = new java.util.HashSet();
            if (loaderIsa != null) {
                this.mLoaderIsas.add(loaderIsa);
            }
            this.mLoadingPackages = new java.util.HashSet();
        }

        private DexUseInfo(com.android.server.pm.dex.PackageDexUsage.DexUseInfo other) {
            this.mIsUsedByOtherApps = other.mIsUsedByOtherApps;
            this.mOwnerUserId = other.mOwnerUserId;
            this.mClassLoaderContext = other.mClassLoaderContext;
            this.mLoaderIsas = new java.util.HashSet(other.mLoaderIsas);
            this.mLoadingPackages = new java.util.HashSet(other.mLoadingPackages);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean merge(com.android.server.pm.dex.PackageDexUsage.DexUseInfo dexUseInfo, boolean overwriteCLC) {
            boolean oldIsUsedByOtherApps = this.mIsUsedByOtherApps;
            this.mIsUsedByOtherApps = this.mIsUsedByOtherApps || dexUseInfo.mIsUsedByOtherApps;
            boolean updateIsas = this.mLoaderIsas.addAll(dexUseInfo.mLoaderIsas);
            boolean updateLoadingPackages = this.mLoadingPackages.addAll(dexUseInfo.mLoadingPackages);
            java.lang.String oldClassLoaderContext = this.mClassLoaderContext;
            if (overwriteCLC || isUnsupportedContext(this.mClassLoaderContext)) {
                this.mClassLoaderContext = dexUseInfo.mClassLoaderContext;
            } else if (!java.util.Objects.equals(this.mClassLoaderContext, dexUseInfo.mClassLoaderContext)) {
                this.mClassLoaderContext = com.android.server.pm.dex.PackageDexUsage.VARIABLE_CLASS_LOADER_CONTEXT;
            }
            return updateIsas || oldIsUsedByOtherApps != this.mIsUsedByOtherApps || updateLoadingPackages || !java.util.Objects.equals(oldClassLoaderContext, this.mClassLoaderContext);
        }

        private static boolean isUnsupportedContext(java.lang.String context) {
            return com.android.server.pm.dex.PackageDexUsage.UNSUPPORTED_CLASS_LOADER_CONTEXT.equals(context);
        }

        public boolean isUsedByOtherApps() {
            return this.mIsUsedByOtherApps;
        }

        int getOwnerUserId() {
            return this.mOwnerUserId;
        }

        public java.util.Set<java.lang.String> getLoaderIsas() {
            return this.mLoaderIsas;
        }

        public java.util.Set<java.lang.String> getLoadingPackages() {
            return this.mLoadingPackages;
        }

        public java.lang.String getClassLoaderContext() {
            return this.mClassLoaderContext;
        }

        public boolean isUnsupportedClassLoaderContext() {
            return isUnsupportedContext(this.mClassLoaderContext);
        }

        public boolean isVariableClassLoaderContext() {
            return com.android.server.pm.dex.PackageDexUsage.VARIABLE_CLASS_LOADER_CONTEXT.equals(this.mClassLoaderContext);
        }
    }
}
