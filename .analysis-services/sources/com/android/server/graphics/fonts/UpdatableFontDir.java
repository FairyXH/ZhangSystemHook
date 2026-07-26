package com.android.server.graphics.fonts;

/* JADX INFO: loaded from: classes2.dex */
final class UpdatableFontDir {
    private static final java.lang.String FONT_SIGNATURE_FILE = "font.fsv_sig";
    private static final java.lang.String RANDOM_DIR_PREFIX = "~~";
    private static final java.lang.String TAG = "UpdatableFontDir";
    private final android.util.AtomicFile mConfigFile;
    private final java.util.function.Function<java.util.Map<java.lang.String, java.io.File>, android.text.FontConfig> mConfigSupplier;
    private int mConfigVersion;
    private final java.util.function.Supplier<java.lang.Long> mCurrentTimeSupplier;
    private final java.io.File mFilesDir;
    private com.android.server.graphics.fonts.IUpdatableFontDirExt mFontDirExt;
    private final android.util.ArrayMap<java.lang.String, com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo> mFontFileInfoMap;
    private final com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil mFsverityUtil;
    private long mLastModifiedMillis;
    private final com.android.server.graphics.fonts.UpdatableFontDir.FontFileParser mParser;

    interface FontFileParser {
        java.lang.String buildFontFileName(java.io.File file) throws java.io.IOException;

        java.lang.String getPostScriptName(java.io.File file) throws java.io.IOException;

        long getRevision(java.io.File file) throws java.io.IOException;

        void tryToCreateTypeface(java.io.File file) throws java.lang.Throwable;
    }

    interface FsverityUtil {
        boolean isFromTrustedProvider(java.lang.String str, byte[] bArr);

        boolean rename(java.io.File file, java.io.File file2);

        void setUpFsverity(java.lang.String str) throws java.io.IOException;
    }

    private static final class FontFileInfo {
        private final java.io.File mFile;
        private final java.lang.String mPsName;
        private final long mRevision;

        FontFileInfo(java.io.File file, java.lang.String psName, long revision) {
            this.mFile = file;
            this.mPsName = psName;
            this.mRevision = revision;
        }

        public java.io.File getFile() {
            return this.mFile;
        }

        public java.lang.String getPostScriptName() {
            return this.mPsName;
        }

        public java.io.File getRandomizedFontDir() {
            return this.mFile.getParentFile();
        }

        public long getRevision() {
            return this.mRevision;
        }

        public java.lang.String toString() {
            return "FontFileInfo{mFile=" + this.mFile + ", psName=" + this.mPsName + ", mRevision=" + this.mRevision + '}';
        }
    }

    UpdatableFontDir(java.io.File filesDir, com.android.server.graphics.fonts.UpdatableFontDir.FontFileParser parser, com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil fsverityUtil, java.io.File configFile) {
        this(filesDir, parser, fsverityUtil, configFile, new java.util.function.Supplier() { // from class: com.android.server.graphics.fonts.UpdatableFontDir$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return java.lang.Long.valueOf(java.lang.System.currentTimeMillis());
            }
        }, new java.util.function.Function() { // from class: com.android.server.graphics.fonts.UpdatableFontDir$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.graphics.fonts.SystemFonts.getSystemFontConfig((java.util.Map) obj, 0L, 0);
            }
        });
    }

    UpdatableFontDir(java.io.File filesDir, com.android.server.graphics.fonts.UpdatableFontDir.FontFileParser parser, com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil fsverityUtil, java.io.File configFile, java.util.function.Supplier<java.lang.Long> currentTimeSupplier, java.util.function.Function<java.util.Map<java.lang.String, java.io.File>, android.text.FontConfig> configSupplier) {
        this.mFontDirExt = (com.android.server.graphics.fonts.IUpdatableFontDirExt) system.ext.loader.core.ExtLoader.type(com.android.server.graphics.fonts.IUpdatableFontDirExt.class).base(this).create();
        this.mFontFileInfoMap = new android.util.ArrayMap<>();
        this.mFilesDir = filesDir;
        this.mParser = parser;
        this.mFsverityUtil = fsverityUtil;
        this.mConfigFile = new android.util.AtomicFile(configFile);
        this.mCurrentTimeSupplier = currentTimeSupplier;
        this.mConfigSupplier = configSupplier;
    }

    void loadFontFileMap() {
        com.android.server.graphics.fonts.PersistentSystemFontConfig.Config config;
        java.io.File[] dirs;
        boolean zFixFontUpdateFailure;
        this.mFontFileInfoMap.clear();
        long j = 0;
        this.mLastModifiedMillis = 0L;
        char c = 1;
        this.mConfigVersion = 1;
        try {
            config = readPersistentConfig();
            this.mLastModifiedMillis = config.lastModifiedMillis;
            dirs = this.mFilesDir.listFiles();
        } catch (java.lang.Throwable t) {
            try {
                android.util.Slog.e(TAG, "Failed to load font mappings.", t);
                if (0 != 0) {
                    return;
                }
                this.mFontFileInfoMap.clear();
                this.mLastModifiedMillis = 0L;
                android.os.FileUtils.deleteContents(this.mFilesDir);
                if (!com.android.text.flags.Flags.fixFontUpdateFailure()) {
                    return;
                }
            } finally {
                if (0 == 0) {
                    this.mFontFileInfoMap.clear();
                    this.mLastModifiedMillis = 0L;
                    android.os.FileUtils.deleteContents(this.mFilesDir);
                    if (com.android.text.flags.Flags.fixFontUpdateFailure()) {
                        this.mConfigFile.delete();
                    }
                }
            }
        }
        if (dirs == null) {
            android.util.Slog.e(TAG, "Could not read: " + this.mFilesDir);
            if (0 == 0) {
                this.mFontFileInfoMap.clear();
                this.mLastModifiedMillis = 0L;
                android.os.FileUtils.deleteContents(this.mFilesDir);
                if (com.android.text.flags.Flags.fixFontUpdateFailure()) {
                    this.mConfigFile.delete();
                    return;
                }
                return;
            }
            return;
        }
        android.text.FontConfig fontConfig = null;
        int length = dirs.length;
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            java.io.File dir = dirs[i2];
            if (!dir.getName().startsWith(RANDOM_DIR_PREFIX)) {
                android.util.Slog.e(TAG, "Unexpected dir found: " + dir);
                if (0 == 0) {
                    this.mFontFileInfoMap.clear();
                    this.mLastModifiedMillis = j;
                    android.os.FileUtils.deleteContents(this.mFilesDir);
                    if (com.android.text.flags.Flags.fixFontUpdateFailure()) {
                        this.mConfigFile.delete();
                        return;
                    }
                    return;
                }
                return;
            }
            if (config.updatedFontDirs.contains(dir.getName())) {
                java.io.File signatureFile = new java.io.File(dir, FONT_SIGNATURE_FILE);
                if (signatureFile.exists()) {
                    try {
                        byte[] signature = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(signatureFile.getAbsolutePath(), new java.lang.String[i]));
                        java.io.File[] files = dir.listFiles();
                        if (files != null && files.length == 2) {
                            java.io.File fontFile = files[i].equals(signatureFile) ? files[c] : files[i];
                            com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo fontFileInfo = validateFontFile(fontFile, signature);
                            if (fontConfig == null) {
                                fontConfig = com.android.text.flags.Flags.fixFontUpdateFailure() ? this.mConfigSupplier.apply(java.util.Collections.emptyMap()) : getSystemFontConfig();
                            }
                            c = 1;
                            addFileToMapIfSameOrNewer(fontFileInfo, fontConfig, true);
                        }
                        android.util.Slog.e(TAG, "Unexpected files in dir: " + dir);
                        if (success) {
                            return;
                        }
                        if (zFixFontUpdateFailure) {
                            return;
                        } else {
                            return;
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Failed to read signature file.");
                        if (0 == 0) {
                            this.mFontFileInfoMap.clear();
                            this.mLastModifiedMillis = 0L;
                            android.os.FileUtils.deleteContents(this.mFilesDir);
                            if (com.android.text.flags.Flags.fixFontUpdateFailure()) {
                                this.mConfigFile.delete();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                }
                android.util.Slog.i(TAG, "The signature file is missing.");
                if (com.android.text.flags.Flags.fixFontUpdateFailure()) {
                    if (0 == 0) {
                        this.mFontFileInfoMap.clear();
                        this.mLastModifiedMillis = j;
                        android.os.FileUtils.deleteContents(this.mFilesDir);
                        if (com.android.text.flags.Flags.fixFontUpdateFailure()) {
                            this.mConfigFile.delete();
                            return;
                        }
                        return;
                    }
                    return;
                }
                android.os.FileUtils.deleteContentsAndDir(dir);
            } else {
                android.util.Slog.i(TAG, "Deleting obsolete dir: " + dir);
                android.os.FileUtils.deleteContentsAndDir(dir);
            }
            i2++;
            j = 0;
            i = 0;
        }
        if (com.android.text.flags.Flags.fixFontUpdateFailure()) {
            for (int i3 = 0; i3 < config.fontFamilies.size(); i3++) {
                android.graphics.fonts.FontUpdateRequest.Family family = config.fontFamilies.get(i3);
                for (int j2 = 0; j2 < family.getFonts().size(); j2++) {
                    android.graphics.fonts.FontUpdateRequest.Font font = (android.graphics.fonts.FontUpdateRequest.Font) family.getFonts().get(j2);
                    if (!this.mFontFileInfoMap.containsKey(font.getPostScriptName())) {
                        if (fontConfig == null) {
                            fontConfig = this.mConfigSupplier.apply(java.util.Collections.emptyMap());
                        }
                        if (getFontByPostScriptName(font.getPostScriptName(), fontConfig) == null) {
                            android.util.Slog.e(TAG, "Unknown font that has PostScript name " + font.getPostScriptName() + " is requested in FontFamily " + family.getName());
                            if (0 == 0) {
                                this.mFontFileInfoMap.clear();
                                this.mLastModifiedMillis = 0L;
                                android.os.FileUtils.deleteContents(this.mFilesDir);
                                if (com.android.text.flags.Flags.fixFontUpdateFailure()) {
                                    this.mConfigFile.delete();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                    }
                }
            }
        }
        if (1 == 0) {
            this.mFontFileInfoMap.clear();
            this.mLastModifiedMillis = 0L;
            android.os.FileUtils.deleteContents(this.mFilesDir);
            if (!com.android.text.flags.Flags.fixFontUpdateFailure()) {
                return;
            }
            this.mConfigFile.delete();
        }
    }

    public void update(java.util.List<android.graphics.fonts.FontUpdateRequest> requests) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        for (android.graphics.fonts.FontUpdateRequest request : requests) {
            switch (request.getType()) {
                case 0:
                    java.util.Objects.requireNonNull(request.getFd());
                    java.util.Objects.requireNonNull(request.getSignature());
                    break;
                case 1:
                    java.util.Objects.requireNonNull(request.getFontFamily());
                    java.util.Objects.requireNonNull(request.getFontFamily().getName());
                    break;
            }
        }
        android.util.ArrayMap<java.lang.String, com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo> backupMap = new android.util.ArrayMap<>(this.mFontFileInfoMap);
        com.android.server.graphics.fonts.PersistentSystemFontConfig.Config curConfig = readPersistentConfig();
        java.util.Map<java.lang.String, android.graphics.fonts.FontUpdateRequest.Family> familyMap = new java.util.HashMap<>();
        for (int i = 0; i < curConfig.fontFamilies.size(); i++) {
            android.graphics.fonts.FontUpdateRequest.Family family = curConfig.fontFamilies.get(i);
            familyMap.put(family.getName(), family);
        }
        long backupLastModifiedDate = this.mLastModifiedMillis;
        boolean success = false;
        try {
            for (android.graphics.fonts.FontUpdateRequest request2 : requests) {
                switch (request2.getType()) {
                    case 0:
                        installFontFile(request2.getFd().getFileDescriptor(), request2.getSignature());
                        break;
                    case 1:
                        android.graphics.fonts.FontUpdateRequest.Family family2 = request2.getFontFamily();
                        familyMap.put(family2.getName(), family2);
                        break;
                }
            }
            java.util.Iterator<android.graphics.fonts.FontUpdateRequest.Family> it = familyMap.values().iterator();
            while (it.hasNext()) {
                if (resolveFontFilesForNamedFamily(it.next()) == null) {
                    throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-9, "Required fonts are not available");
                }
            }
            this.mLastModifiedMillis = this.mCurrentTimeSupplier.get().longValue();
            com.android.server.graphics.fonts.PersistentSystemFontConfig.Config newConfig = new com.android.server.graphics.fonts.PersistentSystemFontConfig.Config();
            newConfig.lastModifiedMillis = this.mLastModifiedMillis;
            for (com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo info : this.mFontFileInfoMap.values()) {
                newConfig.updatedFontDirs.add(info.getRandomizedFontDir().getName());
            }
            newConfig.fontFamilies.addAll(familyMap.values());
            writePersistentConfig(newConfig);
            this.mConfigVersion++;
            success = true;
        } finally {
            if (!success) {
                this.mFontFileInfoMap.clear();
                this.mFontFileInfoMap.putAll((android.util.ArrayMap<? extends java.lang.String, ? extends com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo>) backupMap);
                this.mLastModifiedMillis = backupLastModifiedDate;
            }
        }
    }

    private void installFontFile(java.io.FileDescriptor fd, byte[] pkcs7Signature) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        java.io.File newDir = getRandomDir(this.mFilesDir);
        if (!newDir.mkdir()) {
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-1, "Failed to create font directory.");
        }
        try {
            android.system.Os.chmod(newDir.getAbsolutePath(), com.android.internal.art.ArtStatsLog.ISOLATED_COMPILATION_SCHEDULED);
            boolean success = false;
            try {
                java.io.File tempNewFontFile = new java.io.File(newDir, "font.ttf");
                try {
                    java.io.FileOutputStream out = new java.io.FileOutputStream(tempNewFontFile);
                    try {
                        android.os.FileUtils.copy(fd, out.getFD());
                        out.close();
                        try {
                            this.mFsverityUtil.setUpFsverity(tempNewFontFile.getAbsolutePath());
                            try {
                                java.lang.String fontFileName = this.mParser.buildFontFileName(tempNewFontFile);
                                if (fontFileName == null) {
                                    throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-4, "Failed to read PostScript name from font file");
                                }
                                java.io.File newFontFile = new java.io.File(newDir, fontFileName);
                                if (!this.mFsverityUtil.rename(tempNewFontFile, newFontFile)) {
                                    throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-1, "Failed to move verified font file.");
                                }
                                try {
                                    android.system.Os.chmod(newFontFile.getAbsolutePath(), com.android.internal.util.FrameworkStatsLog.VBMETA_DIGEST_REPORTED);
                                    java.io.File signatureFile = new java.io.File(newDir, FONT_SIGNATURE_FILE);
                                    try {
                                        java.io.FileOutputStream out2 = new java.io.FileOutputStream(signatureFile);
                                        try {
                                            out2.write(pkcs7Signature);
                                            out2.close();
                                            try {
                                                android.system.Os.chmod(signatureFile.getAbsolutePath(), com.android.internal.util.FrameworkStatsLog.NON_A11Y_TOOL_SERVICE_WARNING_REPORT);
                                                com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo fontFileInfo = validateFontFile(newFontFile, pkcs7Signature);
                                                try {
                                                    this.mParser.tryToCreateTypeface(fontFileInfo.getFile());
                                                    android.text.FontConfig fontConfig = getSystemFontConfig();
                                                    if (!addFileToMapIfSameOrNewer(fontFileInfo, fontConfig, false)) {
                                                        throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-5, "Downgrading font file is forbidden.");
                                                    }
                                                    success = true;
                                                } catch (java.lang.Throwable t) {
                                                    throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-3, "Failed to create Typeface from file", t);
                                                }
                                            } catch (android.system.ErrnoException e) {
                                                throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-1, "Failed to change the signature file mode to 600", e);
                                            }
                                        } catch (java.lang.Throwable e2) {
                                            try {
                                                out2.close();
                                            } catch (java.lang.Throwable th) {
                                                e2.addSuppressed(th);
                                            }
                                            throw e2;
                                        }
                                    } catch (java.io.IOException e3) {
                                        throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-1, "Failed to write font signature file to storage.", e3);
                                    }
                                } catch (android.system.ErrnoException e4) {
                                    throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-1, "Failed to change font file mode to 644", e4);
                                }
                            } catch (java.io.IOException e5) {
                                throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-3, "Failed to read PostScript name from font file", e5);
                            }
                        } catch (java.io.IOException e6) {
                            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-2, "Failed to setup fs-verity.", e6);
                        }
                    } catch (java.lang.Throwable e7) {
                        try {
                            out.close();
                        } catch (java.lang.Throwable th2) {
                            e7.addSuppressed(th2);
                        }
                        throw e7;
                    }
                } catch (java.io.IOException e8) {
                    throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-1, "Failed to write font file to storage.", e8);
                }
            } finally {
                if (!success) {
                    android.os.FileUtils.deleteContentsAndDir(newDir);
                }
            }
        } catch (android.system.ErrnoException e9) {
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-1, "Failed to change mode to 711", e9);
        }
    }

    private static java.io.File getRandomDir(java.io.File parent) {
        java.io.File dir;
        java.security.SecureRandom random = new java.security.SecureRandom();
        byte[] bytes = new byte[16];
        do {
            random.nextBytes(bytes);
            java.lang.String dirName = RANDOM_DIR_PREFIX + android.util.Base64.encodeToString(bytes, 10);
            dir = new java.io.File(parent, dirName);
        } while (dir.exists());
        return dir;
    }

    private com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo lookupFontFileInfo(java.lang.String psName) {
        return this.mFontFileInfoMap.get(psName);
    }

    private void putFontFileInfo(com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo info) {
        this.mFontFileInfoMap.put(info.getPostScriptName(), info);
    }

    private boolean addFileToMapIfSameOrNewer(com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo fontFileInfo, android.text.FontConfig fontConfig, boolean deleteOldFile) {
        com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo existingInfo = lookupFontFileInfo(fontFileInfo.getPostScriptName());
        boolean shouldAddToMap = true;
        if (existingInfo == null) {
            long preInstalledRev = getPreinstalledFontRevision(fontFileInfo, fontConfig);
            if (preInstalledRev > fontFileInfo.getRevision()) {
                shouldAddToMap = false;
            }
        } else if (existingInfo.getRevision() > fontFileInfo.getRevision()) {
            shouldAddToMap = false;
        }
        if (shouldAddToMap) {
            if (deleteOldFile && existingInfo != null) {
                android.os.FileUtils.deleteContentsAndDir(existingInfo.getRandomizedFontDir());
            }
            putFontFileInfo(fontFileInfo);
        } else if (deleteOldFile) {
            android.os.FileUtils.deleteContentsAndDir(fontFileInfo.getRandomizedFontDir());
        }
        return shouldAddToMap;
    }

    private android.text.FontConfig.Font getFontByPostScriptName(java.lang.String psName, android.text.FontConfig fontConfig) {
        android.text.FontConfig.Font targetFont = null;
        for (int i = 0; i < fontConfig.getFontFamilies().size(); i++) {
            android.text.FontConfig.FontFamily family = (android.text.FontConfig.FontFamily) fontConfig.getFontFamilies().get(i);
            int j = 0;
            while (true) {
                if (j < family.getFontList().size()) {
                    android.text.FontConfig.Font font = (android.text.FontConfig.Font) family.getFontList().get(j);
                    if (!font.getPostScriptName().equals(psName)) {
                        j++;
                    } else {
                        targetFont = font;
                        break;
                    }
                }
            }
        }
        for (int i2 = 0; i2 < fontConfig.getNamedFamilyLists().size(); i2++) {
            android.text.FontConfig.NamedFamilyList namedFamilyList = (android.text.FontConfig.NamedFamilyList) fontConfig.getNamedFamilyLists().get(i2);
            for (int j2 = 0; j2 < namedFamilyList.getFamilies().size(); j2++) {
                android.text.FontConfig.FontFamily family2 = (android.text.FontConfig.FontFamily) namedFamilyList.getFamilies().get(j2);
                int k = 0;
                while (true) {
                    if (k < family2.getFontList().size()) {
                        android.text.FontConfig.Font font2 = (android.text.FontConfig.Font) family2.getFontList().get(k);
                        if (!font2.getPostScriptName().equals(psName)) {
                            k++;
                        } else {
                            targetFont = font2;
                            break;
                        }
                    }
                }
            }
        }
        return targetFont;
    }

    private long getPreinstalledFontRevision(com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo info, android.text.FontConfig fontConfig) {
        java.lang.String psName = info.getPostScriptName();
        android.text.FontConfig.Font targetFont = getFontByPostScriptName(psName, fontConfig);
        if (targetFont == null) {
            return -1L;
        }
        java.io.File preinstalledFontFile = targetFont.getOriginalFile() != null ? targetFont.getOriginalFile() : targetFont.getFile();
        if (!preinstalledFontFile.exists()) {
            return -1L;
        }
        long revision = getFontRevision(preinstalledFontFile);
        if (revision == -1) {
            android.util.Slog.w(TAG, "Invalid preinstalled font file");
        }
        return revision;
    }

    private com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo validateFontFile(java.io.File file, byte[] pkcs7Signature) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        if (!this.mFsverityUtil.isFromTrustedProvider(file.getAbsolutePath(), pkcs7Signature)) {
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-2, "Font validation failed. Fs-verity is not enabled: " + file);
        }
        try {
            java.lang.String psName = this.mParser.getPostScriptName(file);
            long revision = getFontRevision(file);
            if (revision == -1) {
                throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-3, "Font validation failed. Could not read font revision: " + file);
            }
            return new com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo(file, psName, revision);
        } catch (java.io.IOException e) {
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-4, "Font validation failed. Could not read PostScript name name: " + file);
        }
    }

    private long getFontRevision(java.io.File file) {
        try {
            return this.mParser.getRevision(file);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to read font file", e);
            return -1L;
        }
    }

    private android.text.FontConfig.NamedFamilyList resolveFontFilesForNamedFamily(android.graphics.fonts.FontUpdateRequest.Family fontFamily) {
        java.util.List<android.graphics.fonts.FontUpdateRequest.Font> fontList = fontFamily.getFonts();
        java.util.List<android.text.FontConfig.Font> resolvedFonts = new java.util.ArrayList<>(fontList.size());
        for (int i = 0; i < fontList.size(); i++) {
            android.graphics.fonts.FontUpdateRequest.Font font = fontList.get(i);
            com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo info = this.mFontFileInfoMap.get(font.getPostScriptName());
            if (info == null) {
                android.util.Slog.e(TAG, "Failed to lookup font file that has " + font.getPostScriptName());
                return null;
            }
            resolvedFonts.add(new android.text.FontConfig.Font(info.mFile, (java.io.File) null, info.getPostScriptName(), font.getFontStyle(), font.getIndex(), font.getFontVariationSettings(), (java.lang.String) null, 0));
        }
        android.text.FontConfig.FontFamily family = new android.text.FontConfig.FontFamily(resolvedFonts, android.os.LocaleList.getEmptyLocaleList(), 0);
        return new android.text.FontConfig.NamedFamilyList(java.util.Collections.singletonList(family), fontFamily.getName());
    }

    java.util.Map<java.lang.String, java.io.File> getPostScriptMap() {
        java.util.Map<java.lang.String, java.io.File> map = new android.util.ArrayMap<>();
        for (int i = 0; i < this.mFontFileInfoMap.size(); i++) {
            com.android.server.graphics.fonts.UpdatableFontDir.FontFileInfo info = this.mFontFileInfoMap.valueAt(i);
            map.put(info.getPostScriptName(), info.getFile());
        }
        return map;
    }

    android.text.FontConfig getSystemFontConfig() {
        android.text.FontConfig config = this.mConfigSupplier.apply(getPostScriptMap());
        com.android.server.graphics.fonts.PersistentSystemFontConfig.Config persistentConfig = readPersistentConfig();
        java.util.List<android.graphics.fonts.FontUpdateRequest.Family> families = persistentConfig.fontFamilies;
        java.util.List<android.text.FontConfig.NamedFamilyList> mergedFamilies = new java.util.ArrayList<>(config.getNamedFamilyLists().size() + families.size());
        mergedFamilies.addAll(config.getNamedFamilyLists());
        for (int i = 0; i < families.size(); i++) {
            android.text.FontConfig.NamedFamilyList family = resolveFontFilesForNamedFamily(families.get(i));
            if (family != null) {
                mergedFamilies.add(family);
            }
        }
        this.mFontDirExt.apendIndividualFontFamily(mergedFamilies);
        return new android.text.FontConfig(config.getFontFamilies(), config.getAliases(), mergedFamilies, config.getLocaleFallbackCustomizations(), this.mLastModifiedMillis, this.mConfigVersion);
    }

    private com.android.server.graphics.fonts.PersistentSystemFontConfig.Config readPersistentConfig() {
        com.android.server.graphics.fonts.PersistentSystemFontConfig.Config config = new com.android.server.graphics.fonts.PersistentSystemFontConfig.Config();
        try {
            java.io.FileInputStream fis = this.mConfigFile.openRead();
            try {
                com.android.server.graphics.fonts.PersistentSystemFontConfig.loadFromXml(fis, config);
                if (fis != null) {
                    fis.close();
                }
            } catch (java.lang.Throwable th) {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
        }
        return config;
    }

    private void writePersistentConfig(com.android.server.graphics.fonts.PersistentSystemFontConfig.Config config) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        java.io.FileOutputStream fos = null;
        try {
            fos = this.mConfigFile.startWrite();
            com.android.server.graphics.fonts.PersistentSystemFontConfig.writeToXml(fos, config);
            this.mConfigFile.finishWrite(fos);
        } catch (java.io.IOException e) {
            if (fos != null) {
                this.mConfigFile.failWrite(fos);
            }
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-6, "Failed to write config XML.", e);
        }
    }

    int getConfigVersion() {
        return this.mConfigVersion;
    }

    public java.util.Map<java.lang.String, android.text.FontConfig.NamedFamilyList> getFontFamilyMap() {
        com.android.server.graphics.fonts.PersistentSystemFontConfig.Config curConfig = readPersistentConfig();
        java.util.Map<java.lang.String, android.text.FontConfig.NamedFamilyList> familyMap = new java.util.HashMap<>();
        for (int i = 0; i < curConfig.fontFamilies.size(); i++) {
            android.graphics.fonts.FontUpdateRequest.Family family = curConfig.fontFamilies.get(i);
            android.text.FontConfig.NamedFamilyList resolvedFamily = resolveFontFilesForNamedFamily(family);
            if (resolvedFamily != null) {
                familyMap.put(family.getName(), resolvedFamily);
            }
        }
        return familyMap;
    }

    static void deleteAllFiles(java.io.File filesDir, java.io.File configFile) {
        try {
            new android.util.AtomicFile(configFile).delete();
        } catch (java.lang.Throwable th) {
            android.util.Slog.w(TAG, "Failed to delete " + configFile);
        }
        try {
            android.os.FileUtils.deleteContents(filesDir);
        } catch (java.lang.Throwable th2) {
            android.util.Slog.w(TAG, "Failed to delete " + filesDir);
        }
    }
}
