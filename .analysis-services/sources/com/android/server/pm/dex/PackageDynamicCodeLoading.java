package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
class PackageDynamicCodeLoading extends com.android.server.pm.AbstractStatsBase<java.lang.Void> {
    private static final char FIELD_SEPARATOR = ':';
    static final int FILE_TYPE_DEX = 68;
    static final int FILE_TYPE_NATIVE = 78;
    private static final java.lang.String FILE_VERSION_HEADER = "DCL1";
    static final int MAX_FILES_PER_OWNER = 100;
    private static final java.util.regex.Pattern PACKAGE_LINE_PATTERN = java.util.regex.Pattern.compile("([A-Z]):([0-9]+):([^:]*):(.*)");
    private static final java.lang.String PACKAGE_PREFIX = "P:";
    private static final java.lang.String PACKAGE_SEPARATOR = ",";
    private static final java.lang.String TAG = "PackageDynamicCodeLoading";
    private final java.lang.Object mLock;
    private java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode> mPackageMap;

    PackageDynamicCodeLoading() {
        super("package-dcl.list", "PackageDynamicCodeLoading_DiskWriter", false);
        this.mLock = new java.lang.Object();
        this.mPackageMap = new java.util.HashMap();
    }

    boolean record(java.lang.String owningPackageName, java.lang.String filePath, int fileType, int ownerUserId, java.lang.String loadingPackageName) {
        boolean zAdd;
        if (!isValidFileType(fileType)) {
            throw new java.lang.IllegalArgumentException("Bad file type: " + fileType);
        }
        synchronized (this.mLock) {
            com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode packageInfo = this.mPackageMap.get(owningPackageName);
            if (packageInfo == null) {
                packageInfo = new com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode();
                this.mPackageMap.put(owningPackageName, packageInfo);
            }
            zAdd = packageInfo.add(filePath, (char) fileType, ownerUserId, loadingPackageName);
        }
        return zAdd;
    }

    private static boolean isValidFileType(int fileType) {
        return fileType == 68 || fileType == 78;
    }

    java.util.Set<java.lang.String> getAllPackagesWithDynamicCodeLoading() {
        java.util.HashSet hashSet;
        synchronized (this.mLock) {
            hashSet = new java.util.HashSet(this.mPackageMap.keySet());
        }
        return hashSet;
    }

    /* JADX WARN: Multi-variable type inference failed */
    com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode getPackageDynamicCodeInfo(java.lang.String str) {
        com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode packageDynamicCode;
        synchronized (this.mLock) {
            com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode packageDynamicCode2 = this.mPackageMap.get(str);
            packageDynamicCode = null;
            java.lang.Object[] objArr = 0;
            if (packageDynamicCode2 != null) {
                packageDynamicCode = new com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode(packageDynamicCode2);
            }
        }
        return packageDynamicCode;
    }

    void clear() {
        synchronized (this.mLock) {
            this.mPackageMap.clear();
        }
    }

    boolean removePackage(java.lang.String packageName) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPackageMap.remove(packageName) != null;
        }
        return z;
    }

    boolean removeUserPackage(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode packageDynamicCode = this.mPackageMap.get(packageName);
            if (packageDynamicCode == null) {
                return false;
            }
            if (!packageDynamicCode.removeUser(userId)) {
                return false;
            }
            if (packageDynamicCode.mFileUsageMap.isEmpty()) {
                this.mPackageMap.remove(packageName);
            }
            return true;
        }
    }

    boolean removeFile(java.lang.String packageName, java.lang.String filePath, int userId) {
        synchronized (this.mLock) {
            com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode packageDynamicCode = this.mPackageMap.get(packageName);
            if (packageDynamicCode == null) {
                return false;
            }
            if (!packageDynamicCode.removeFile(filePath, userId)) {
                return false;
            }
            if (packageDynamicCode.mFileUsageMap.isEmpty()) {
                this.mPackageMap.remove(packageName);
            }
            return true;
        }
    }

    void syncData(java.util.Map<java.lang.String, java.util.Set<java.lang.Integer>> packageToUsersMap) {
        synchronized (this.mLock) {
            java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode>> it = this.mPackageMap.entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode> entry = it.next();
                java.util.Set<java.lang.Integer> packageUsers = packageToUsersMap.get(entry.getKey());
                if (packageUsers == null) {
                    it.remove();
                } else {
                    com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode packageDynamicCode = entry.getValue();
                    packageDynamicCode.syncData(packageToUsersMap, packageUsers);
                    if (packageDynamicCode.mFileUsageMap.isEmpty()) {
                        it.remove();
                    }
                }
            }
        }
    }

    void maybeWriteAsync() {
        super.maybeWriteAsync(null);
    }

    void writeNow() {
        super.writeNow(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public final void writeInternal(java.lang.Void data) {
        android.util.AtomicFile file = getFile();
        java.io.FileOutputStream output = null;
        try {
            output = file.startWrite();
            write(output);
            file.finishWrite(output);
        } catch (java.io.IOException e) {
            file.failWrite(output);
            android.util.Slog.e(TAG, "Failed to write dynamic usage for secondary code files.", e);
        }
    }

    void write(java.io.OutputStream output) throws java.io.IOException {
        java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode> copiedMap;
        synchronized (this.mLock) {
            copiedMap = new java.util.HashMap<>(this.mPackageMap.size());
            for (java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode> entry : this.mPackageMap.entrySet()) {
                com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode copiedValue = new com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode(entry.getValue());
                copiedMap.put(entry.getKey(), copiedValue);
            }
        }
        write(output, copiedMap);
    }

    private static void write(java.io.OutputStream output, java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode> packageMap) throws java.io.IOException {
        com.android.internal.util.FastPrintWriter fastPrintWriter = new com.android.internal.util.FastPrintWriter(output);
        fastPrintWriter.println(FILE_VERSION_HEADER);
        for (java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode> packageEntry : packageMap.entrySet()) {
            fastPrintWriter.print(PACKAGE_PREFIX);
            fastPrintWriter.println(packageEntry.getKey());
            java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile> mFileUsageMap = packageEntry.getValue().mFileUsageMap;
            for (java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile> fileEntry : mFileUsageMap.entrySet()) {
                java.lang.String path = fileEntry.getKey();
                com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile dynamicCodeFile = fileEntry.getValue();
                fastPrintWriter.print(dynamicCodeFile.mFileType);
                fastPrintWriter.print(FIELD_SEPARATOR);
                fastPrintWriter.print(dynamicCodeFile.mUserId);
                fastPrintWriter.print(FIELD_SEPARATOR);
                java.lang.String prefix = "";
                for (java.lang.String packageName : dynamicCodeFile.mLoadingPackages) {
                    fastPrintWriter.print(prefix);
                    fastPrintWriter.print(packageName);
                    prefix = PACKAGE_SEPARATOR;
                }
                fastPrintWriter.print(FIELD_SEPARATOR);
                fastPrintWriter.println(escape(path));
            }
        }
        fastPrintWriter.flush();
        if (fastPrintWriter.checkError()) {
            throw new java.io.IOException("Writer failed");
        }
    }

    void read() {
        super.read((java.lang.Object) null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public final void readInternal(java.lang.Void data) {
        android.util.AtomicFile file = getFile();
        java.io.FileInputStream stream = null;
        try {
            try {
                stream = file.openRead();
                read((java.io.InputStream) stream);
            } catch (java.io.FileNotFoundException e) {
            } catch (java.io.IOException e2) {
                android.util.Slog.w(TAG, "Failed to parse dynamic usage for secondary code files.", e2);
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(stream);
        }
    }

    void read(java.io.InputStream stream) throws java.io.IOException {
        java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode> newPackageMap = new java.util.HashMap<>();
        read(stream, newPackageMap);
        synchronized (this.mLock) {
            this.mPackageMap = newPackageMap;
        }
    }

    private static void read(java.io.InputStream stream, java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode> packageMap) throws java.io.IOException {
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(stream));
        java.lang.String versionLine = reader.readLine();
        if (!FILE_VERSION_HEADER.equals(versionLine)) {
            throw new java.io.IOException("Incorrect version line: " + versionLine);
        }
        java.lang.String line = reader.readLine();
        if (line != null && !line.startsWith(PACKAGE_PREFIX)) {
            throw new java.io.IOException("Malformed line: " + line);
        }
        while (line != null) {
            java.lang.String packageName = line.substring(PACKAGE_PREFIX.length());
            com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode packageInfo = new com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode();
            while (true) {
                line = reader.readLine();
                if (line == null || line.startsWith(PACKAGE_PREFIX)) {
                    break;
                } else {
                    readFileInfo(line, packageInfo);
                }
            }
            if (!packageInfo.mFileUsageMap.isEmpty()) {
                packageMap.put(packageName, packageInfo);
            }
        }
    }

    private static void readFileInfo(java.lang.String line, com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode output) throws java.io.IOException {
        try {
            java.util.regex.Matcher matcher = PACKAGE_LINE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                throw new java.io.IOException("Malformed line: " + line);
            }
            char type = matcher.group(1).charAt(0);
            int user = java.lang.Integer.parseInt(matcher.group(2));
            java.lang.String[] packages = matcher.group(3).split(PACKAGE_SEPARATOR);
            java.lang.String path = unescape(matcher.group(4));
            if (packages.length == 0) {
                throw new java.io.IOException("Malformed line: " + line);
            }
            if (!isValidFileType(type)) {
                throw new java.io.IOException("Unknown file type: " + line);
            }
            output.mFileUsageMap.put(path, new com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile(type, user, packages));
        } catch (java.lang.RuntimeException e) {
            throw new java.io.IOException("Unable to parse line: " + line, e);
        }
    }

    static java.lang.String escape(java.lang.String path) {
        if (path.indexOf(92) == -1 && path.indexOf(10) == -1 && path.indexOf(13) == -1) {
            return path;
        }
        java.lang.StringBuilder result = new java.lang.StringBuilder(path.length() + 10);
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            switch (c) {
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                default:
                    result.append(c);
                    break;
            }
        }
        return result.toString();
    }

    static java.lang.String unescape(java.lang.String escaped) throws java.io.IOException {
        int start = 0;
        int finish = escaped.indexOf(92);
        if (finish == -1) {
            return escaped;
        }
        java.lang.StringBuilder result = new java.lang.StringBuilder(escaped.length());
        while (finish < escaped.length() - 1) {
            result.append((java.lang.CharSequence) escaped, start, finish);
            switch (escaped.charAt(finish + 1)) {
                case '\\':
                    result.append('\\');
                    break;
                case 'n':
                    result.append('\n');
                    break;
                case 'r':
                    result.append('\r');
                    break;
                default:
                    throw new java.io.IOException("Bad escape in: " + escaped);
            }
            start = finish + 2;
            finish = escaped.indexOf(92, start);
            if (finish == -1) {
                result.append((java.lang.CharSequence) escaped, start, escaped.length());
                return result.toString();
            }
        }
        throw new java.io.IOException("Unexpected \\ in: " + escaped);
    }

    static class PackageDynamicCode {
        final java.util.Map<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile> mFileUsageMap;

        private PackageDynamicCode() {
            this.mFileUsageMap = new java.util.HashMap();
        }

        private PackageDynamicCode(com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode original) {
            this.mFileUsageMap = new java.util.HashMap(original.mFileUsageMap.size());
            for (java.util.Map.Entry<java.lang.String, com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile> entry : original.mFileUsageMap.entrySet()) {
                com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile newValue = new com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile(entry.getValue());
                this.mFileUsageMap.put(entry.getKey(), newValue);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean add(java.lang.String path, char fileType, int userId, java.lang.String loadingPackage) {
            com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile fileInfo = this.mFileUsageMap.get(path);
            if (fileInfo == null) {
                if (this.mFileUsageMap.size() >= 100) {
                    return false;
                }
                this.mFileUsageMap.put(path, new com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile(fileType, userId, new java.lang.String[]{loadingPackage}));
                return true;
            }
            if (fileInfo.mUserId != userId) {
                return false;
            }
            return fileInfo.mLoadingPackages.add(loadingPackage);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean removeUser(int userId) {
            boolean updated = false;
            java.util.Iterator<com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile> it = this.mFileUsageMap.values().iterator();
            while (it.hasNext()) {
                com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile fileInfo = it.next();
                if (fileInfo.mUserId == userId) {
                    it.remove();
                    updated = true;
                }
            }
            return updated;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean removeFile(java.lang.String filePath, int userId) {
            com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile fileInfo = this.mFileUsageMap.get(filePath);
            if (fileInfo == null || fileInfo.mUserId != userId) {
                return false;
            }
            this.mFileUsageMap.remove(filePath);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void syncData(java.util.Map<java.lang.String, java.util.Set<java.lang.Integer>> packageToUsersMap, java.util.Set<java.lang.Integer> owningPackageUsers) {
            java.util.Iterator<com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile> fileIt = this.mFileUsageMap.values().iterator();
            while (fileIt.hasNext()) {
                com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile fileInfo = fileIt.next();
                int fileUserId = fileInfo.mUserId;
                if (!owningPackageUsers.contains(java.lang.Integer.valueOf(fileUserId))) {
                    fileIt.remove();
                } else {
                    java.util.Iterator<java.lang.String> loaderIt = fileInfo.mLoadingPackages.iterator();
                    while (loaderIt.hasNext()) {
                        java.lang.String loader = loaderIt.next();
                        java.util.Set<java.lang.Integer> loadingPackageUsers = packageToUsersMap.get(loader);
                        if (loadingPackageUsers == null || !loadingPackageUsers.contains(java.lang.Integer.valueOf(fileUserId))) {
                            loaderIt.remove();
                        }
                    }
                    if (fileInfo.mLoadingPackages.isEmpty()) {
                        fileIt.remove();
                    }
                }
            }
        }
    }

    static class DynamicCodeFile {
        final char mFileType;
        final java.util.Set<java.lang.String> mLoadingPackages;
        final int mUserId;

        private DynamicCodeFile(char type, int user, java.lang.String... packages) {
            this.mFileType = type;
            this.mUserId = user;
            this.mLoadingPackages = new java.util.HashSet(java.util.Arrays.asList(packages));
        }

        private DynamicCodeFile(com.android.server.pm.dex.PackageDynamicCodeLoading.DynamicCodeFile original) {
            this.mFileType = original.mFileType;
            this.mUserId = original.mUserId;
            this.mLoadingPackages = new java.util.HashSet(original.mLoadingPackages);
        }
    }
}
