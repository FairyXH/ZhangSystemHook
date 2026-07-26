package com.android.server.graphics.fonts;

/* JADX INFO: loaded from: classes2.dex */
public final class FontManagerService extends com.android.internal.graphics.fonts.IFontManager.Stub {
    private static final java.lang.String CONFIG_XML_FILE = "/data/fonts/config/config.xml";
    private static final java.lang.String FONT_FILES_DIR = "/data/fonts/files";
    private static final java.lang.String TAG = "FontManagerService";
    private final android.content.Context mContext;
    private java.lang.String mDebugCertFilePath;
    private final boolean mIsSafeMode;
    private android.os.SharedMemory mSerializedFontMap;
    private final java.lang.Object mSerializedFontMapLock;
    private com.android.server.graphics.fonts.UpdatableFontDir mUpdatableFontDir;
    private final java.lang.Object mUpdatableFontDirLock;

    public android.text.FontConfig getFontConfig() {
        super.getFontConfig_enforcePermission();
        return getSystemFontConfig();
    }

    public int updateFontFamily(java.util.List<android.graphics.fonts.FontUpdateRequest> requests, int baseVersion) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        try {
            com.android.internal.util.Preconditions.checkArgumentNonnegative(baseVersion);
            java.util.Objects.requireNonNull(requests);
            getContext().enforceCallingPermission("android.permission.UPDATE_FONTS", "UPDATE_FONTS permission required.");
            try {
                update(baseVersion, requests);
                closeFileDescriptors(requests);
                return 0;
            } catch (com.android.server.graphics.fonts.FontManagerService.SystemFontException e) {
                android.util.Slog.e(TAG, "Failed to update font family", e);
                int errorCode = e.getErrorCode();
                closeFileDescriptors(requests);
                return errorCode;
            }
        } catch (java.lang.Throwable e2) {
            closeFileDescriptors(requests);
            throw e2;
        }
    }

    private static void closeFileDescriptors(java.util.List<android.graphics.fonts.FontUpdateRequest> requests) {
        android.os.ParcelFileDescriptor fd;
        if (requests == null) {
            return;
        }
        for (android.graphics.fonts.FontUpdateRequest request : requests) {
            if (request != null && (fd = request.getFd()) != null) {
                try {
                    fd.close();
                } catch (java.io.IOException e) {
                    android.util.Slog.w(TAG, "Failed to close fd", e);
                }
            }
        }
    }

    static class SystemFontException extends android.util.AndroidException {
        private final int mErrorCode;

        SystemFontException(int errorCode, java.lang.String msg, java.lang.Throwable cause) {
            super(msg, cause);
            this.mErrorCode = errorCode;
        }

        SystemFontException(int errorCode, java.lang.String msg) {
            super(msg);
            this.mErrorCode = errorCode;
        }

        int getErrorCode() {
            return this.mErrorCode;
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private final com.android.server.graphics.fonts.FontManagerService mService;
        private final java.util.concurrent.CompletableFuture<java.lang.Void> mServiceStarted;

        public Lifecycle(android.content.Context context, boolean safeMode) {
            super(context);
            this.mServiceStarted = new java.util.concurrent.CompletableFuture<>();
            this.mService = new com.android.server.graphics.fonts.FontManagerService(context, safeMode, this.mServiceStarted);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            com.android.server.LocalServices.addService(com.android.server.graphics.fonts.FontManagerInternal.class, new com.android.server.graphics.fonts.FontManagerInternal() { // from class: com.android.server.graphics.fonts.FontManagerService.Lifecycle.1
                @Override // com.android.server.graphics.fonts.FontManagerInternal
                public android.os.SharedMemory getSerializedSystemFontMap() {
                    com.android.server.graphics.fonts.FontManagerService.Lifecycle.this.mServiceStarted.join();
                    return com.android.server.graphics.fonts.FontManagerService.Lifecycle.this.mService.getCurrentFontMap();
                }
            });
            publishBinderService("font", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            int latestFontLoadBootPhase;
            if (com.android.text.flags.Flags.completeFontLoadInSystemServicesReady()) {
                latestFontLoadBootPhase = com.android.server.SystemService.PHASE_LOCK_SETTINGS_READY;
            } else {
                latestFontLoadBootPhase = 550;
            }
            if (phase == latestFontLoadBootPhase) {
                this.mServiceStarted.join();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
            super.onUserSwitching(from, to);
            android.util.Slog.d(com.android.server.graphics.fonts.FontManagerService.TAG, "onUserSwitching " + from + " to " + to);
            com.android.server.IoThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.graphics.fonts.FontManagerService$Lifecycle$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUserSwitching$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserSwitching$0() {
            this.mService.updateSerializedFontMap();
        }
    }

    private static class FsverityUtilImpl implements com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil {
        private final java.lang.String[] mDerCertPaths;

        FsverityUtilImpl(java.lang.String[] derCertPaths) {
            this.mDerCertPaths = derCertPaths;
        }

        @Override // com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil
        public boolean isFromTrustedProvider(java.lang.String fontPath, byte[] pkcs7Signature) {
            java.io.InputStream is;
            byte[] digest = com.android.internal.security.VerityUtils.getFsverityDigest(fontPath);
            if (digest == null) {
                android.util.Log.w(com.android.server.graphics.fonts.FontManagerService.TAG, "Failed to get fs-verity digest for " + fontPath);
                return false;
            }
            for (java.lang.String certPath : this.mDerCertPaths) {
                try {
                    is = new java.io.FileInputStream(certPath);
                    try {
                    } finally {
                    }
                } catch (java.io.IOException e) {
                    android.util.Log.w(com.android.server.graphics.fonts.FontManagerService.TAG, "Failed to read certificate file: " + certPath);
                }
                if (com.android.internal.security.VerityUtils.verifyPkcs7DetachedSignature(pkcs7Signature, digest, is)) {
                    is.close();
                    return true;
                }
                is.close();
            }
            return false;
        }

        @Override // com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil
        public void setUpFsverity(java.lang.String filePath) throws java.io.IOException {
            com.android.internal.security.VerityUtils.setUpFsverity(filePath);
        }

        @Override // com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil
        public boolean rename(java.io.File src, java.io.File dest) {
            return src.renameTo(dest);
        }
    }

    private FontManagerService(android.content.Context context, boolean safeMode, final java.util.concurrent.CompletableFuture<java.lang.Void> serviceStarted) {
        this.mUpdatableFontDirLock = new java.lang.Object();
        this.mDebugCertFilePath = null;
        this.mSerializedFontMapLock = new java.lang.Object();
        this.mSerializedFontMap = null;
        if (safeMode) {
            android.util.Slog.i(TAG, "Entering safe mode. Deleting all font updates.");
            com.android.server.graphics.fonts.UpdatableFontDir.deleteAllFiles(new java.io.File(FONT_FILES_DIR), new java.io.File(CONFIG_XML_FILE));
        }
        this.mContext = context;
        this.mIsSafeMode = safeMode;
        if (com.android.text.flags.Flags.useOptimizedBoottimeFontLoading()) {
            android.util.Slog.i(TAG, "Using optimized boot-time font loading.");
            com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.graphics.fonts.FontManagerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0(serviceStarted);
                }
            }, "FontManagerService_create");
        } else {
            android.util.Slog.i(TAG, "Not using optimized boot-time font loading.");
            initialize();
            setSystemFontMap();
            serviceStarted.complete(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.util.concurrent.CompletableFuture serviceStarted) {
        ((oplus.android.IOplusExtPluginFactoryExt) system.ext.loader.core.ExtLoader.type(oplus.android.IOplusExtPluginFactoryExt.class).base(this).create()).getFeature(android.graphics.ITypefaceExt.DEFAULT, new java.lang.Object[]{android.graphics.Typeface.DEFAULT}).initFontsForserializeFontMap();
        initialize();
        synchronized (this.mUpdatableFontDirLock) {
            if (this.mUpdatableFontDir != null) {
                setSystemFontMap();
            }
        }
        serviceStarted.complete(null);
    }

    private void setSystemFontMap() {
        try {
            android.graphics.Typeface.setSystemFontMap(getCurrentFontMap());
        } catch (android.system.ErrnoException | java.io.IOException e) {
            android.util.Slog.w(TAG, "Failed to set system font map of system_server");
        }
    }

    private com.android.server.graphics.fonts.UpdatableFontDir createUpdatableFontDir() {
        if (this.mIsSafeMode || !com.android.internal.security.VerityUtils.isFsVeritySupported()) {
            return null;
        }
        java.lang.String[] certs = this.mContext.getResources().getStringArray(android.R.array.config_fillBuiltInDisplayCutoutArray);
        if (this.mDebugCertFilePath != null && android.os.Build.IS_DEBUGGABLE) {
            java.lang.String[] tmp = new java.lang.String[certs.length + 1];
            java.lang.System.arraycopy(certs, 0, tmp, 0, certs.length);
            tmp[certs.length] = this.mDebugCertFilePath;
            certs = tmp;
        }
        return new com.android.server.graphics.fonts.UpdatableFontDir(new java.io.File(FONT_FILES_DIR), new com.android.server.graphics.fonts.OtfFontFileParser(), new com.android.server.graphics.fonts.FontManagerService.FsverityUtilImpl(certs), new java.io.File(CONFIG_XML_FILE));
    }

    public void addDebugCertificate(java.lang.String debugCertPath) {
        this.mDebugCertFilePath = debugCertPath;
    }

    private void initialize() {
        synchronized (this.mUpdatableFontDirLock) {
            this.mUpdatableFontDir = createUpdatableFontDir();
            if (this.mUpdatableFontDir == null) {
                if (com.android.text.flags.Flags.useOptimizedBoottimeFontLoading()) {
                    android.graphics.Typeface.loadPreinstalledSystemFontMap();
                }
                setSerializedFontMap(serializeSystemServerFontMap());
            } else {
                this.mUpdatableFontDir.loadFontFileMap();
                updateSerializedFontMap();
            }
        }
    }

    public android.content.Context getContext() {
        return this.mContext;
    }

    android.os.SharedMemory getCurrentFontMap() {
        android.os.SharedMemory sharedMemory;
        synchronized (this.mSerializedFontMapLock) {
            sharedMemory = this.mSerializedFontMap;
        }
        return sharedMemory;
    }

    void update(int baseVersion, java.util.List<android.graphics.fonts.FontUpdateRequest> requests) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        synchronized (this.mUpdatableFontDirLock) {
            if (this.mUpdatableFontDir == null) {
                throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-7, "The font updater is disabled.");
            }
            if (baseVersion != -1 && this.mUpdatableFontDir.getConfigVersion() != baseVersion) {
                throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-8, "The base config version is older than current.");
            }
            this.mUpdatableFontDir.update(requests);
            updateSerializedFontMap();
        }
    }

    void clearUpdates() {
        com.android.server.graphics.fonts.UpdatableFontDir.deleteAllFiles(new java.io.File(FONT_FILES_DIR), new java.io.File(CONFIG_XML_FILE));
        initialize();
    }

    void restart() {
        initialize();
    }

    java.util.Map<java.lang.String, java.io.File> getFontFileMap() {
        synchronized (this.mUpdatableFontDirLock) {
            if (this.mUpdatableFontDir == null) {
                return java.util.Collections.emptyMap();
            }
            return this.mUpdatableFontDir.getPostScriptMap();
        }
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, writer)) {
            new com.android.server.graphics.fonts.FontManagerShellCommand(this).dumpAll(new android.util.IndentingPrintWriter(writer, "  "));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver result) {
        new com.android.server.graphics.fonts.FontManagerShellCommand(this).exec(this, in, out, err, args, callback, result);
    }

    public android.text.FontConfig getSystemFontConfig() {
        synchronized (this.mUpdatableFontDirLock) {
            if (this.mUpdatableFontDir == null) {
                return android.graphics.fonts.SystemFonts.getSystemPreinstalledFontConfig();
            }
            return this.mUpdatableFontDir.getSystemFontConfig();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSerializedFontMap() {
        android.os.SharedMemory serializedFontMap = serializeFontMap(getSystemFontConfig());
        if (serializedFontMap == null) {
            serializedFontMap = serializeSystemServerFontMap();
        }
        setSerializedFontMap(serializedFontMap);
    }

    private static android.os.SharedMemory serializeFontMap(android.text.FontConfig fontConfig) {
        android.util.ArrayMap<java.lang.String, java.nio.ByteBuffer> bufferCache = new android.util.ArrayMap<>();
        try {
            try {
                java.util.Map<java.lang.String, android.graphics.fonts.FontFamily[]> fallback = android.graphics.fonts.SystemFonts.buildSystemFallback(fontConfig, bufferCache);
                java.util.Map<java.lang.String, android.graphics.Typeface> typefaceMap = android.graphics.fonts.SystemFonts.buildSystemTypefaces(fontConfig, fallback);
                android.os.SharedMemory sharedMemorySerializeFontMap = android.graphics.Typeface.serializeFontMap(typefaceMap);
                for (java.nio.ByteBuffer buffer : bufferCache.values()) {
                    if (buffer instanceof java.nio.DirectByteBuffer) {
                        java.nio.NioUtils.freeDirectBuffer(buffer);
                    }
                }
                return sharedMemorySerializeFontMap;
            } catch (android.system.ErrnoException | java.io.IOException e) {
                android.util.Slog.w(TAG, "Failed to serialize updatable font map. Retrying with system image fonts.", e);
                for (java.nio.ByteBuffer buffer2 : bufferCache.values()) {
                    if (buffer2 instanceof java.nio.DirectByteBuffer) {
                        java.nio.NioUtils.freeDirectBuffer(buffer2);
                    }
                }
                return null;
            }
        } catch (java.lang.Throwable th) {
            for (java.nio.ByteBuffer buffer3 : bufferCache.values()) {
                if (buffer3 instanceof java.nio.DirectByteBuffer) {
                    java.nio.NioUtils.freeDirectBuffer(buffer3);
                }
            }
            throw th;
        }
    }

    private static android.os.SharedMemory serializeSystemServerFontMap() {
        try {
            return android.graphics.Typeface.serializeFontMap(android.graphics.Typeface.getSystemFontMap());
        } catch (android.system.ErrnoException | java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to serialize SystemServer system font map", e);
            return null;
        }
    }

    private void setSerializedFontMap(android.os.SharedMemory serializedFontMap) {
        android.os.SharedMemory oldFontMap;
        synchronized (this.mSerializedFontMapLock) {
            oldFontMap = this.mSerializedFontMap;
            this.mSerializedFontMap = serializedFontMap;
        }
        if (oldFontMap != null) {
            oldFontMap.close();
        }
    }
}
