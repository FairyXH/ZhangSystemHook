package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class AbUpdateInstaller extends com.android.server.devicepolicy.UpdateInstaller {
    private static final int DOWNLOAD_STATE_INITIALIZATION_ERROR = 20;
    private static final int OFFSET_TO_FILE_NAME = 30;
    private static final java.lang.String PAYLOAD_BIN = "payload.bin";
    private static final java.lang.String PAYLOAD_PROPERTIES_TXT = "payload_properties.txt";
    public static final java.lang.String UNKNOWN_ERROR = "Unknown error with error code = ";
    private static final java.util.Map<java.lang.Integer, java.lang.Integer> errorCodesMap = buildErrorCodesMap();
    private static final java.util.Map<java.lang.Integer, java.lang.String> errorStringsMap = buildErrorStringsMap();
    private java.util.Enumeration<? extends java.util.zip.ZipEntry> mEntries;
    private long mOffsetForUpdate;
    private java.util.zip.ZipFile mPackedUpdateFile;
    private java.util.List<java.lang.String> mProperties;
    private long mSizeForUpdate;
    private boolean mUpdateInstalled;

    private static java.util.Map<java.lang.Integer, java.lang.Integer> buildErrorCodesMap() {
        java.util.Map<java.lang.Integer, java.lang.Integer> map = new java.util.HashMap<>();
        map.put(1, 1);
        map.put(20, 2);
        map.put(51, 2);
        map.put(12, 3);
        map.put(11, 3);
        map.put(6, 3);
        map.put(10, 3);
        map.put(26, 3);
        map.put(5, 1);
        map.put(7, 1);
        map.put(9, 1);
        map.put(52, 1);
        return map;
    }

    private static java.util.Map<java.lang.Integer, java.lang.String> buildErrorStringsMap() {
        java.util.Map<java.lang.Integer, java.lang.String> map = new java.util.HashMap<>();
        map.put(1, UNKNOWN_ERROR);
        map.put(20, "The delta update payload was targeted for another version or the source partitionwas modified after it was installed");
        map.put(5, "Failed to finish the configured postinstall works.");
        map.put(7, "Failed to open one of the partitions it tried to write to or read data from.");
        map.put(6, "Payload mismatch error.");
        map.put(9, "Failed to read the payload data from the given URL.");
        map.put(10, "Payload hash error.");
        map.put(11, "Payload size mismatch error.");
        map.put(12, "Failed to verify the signature of the payload.");
        map.put(52, "The payload has been successfully installed,but the active slot was not flipped.");
        return map;
    }

    AbUpdateInstaller(android.content.Context context, android.os.ParcelFileDescriptor updateFileDescriptor, android.app.admin.StartInstallingUpdateCallback callback, com.android.server.devicepolicy.DevicePolicyManagerService.Injector injector, com.android.server.devicepolicy.DevicePolicyConstants constants) {
        super(context, updateFileDescriptor, callback, injector, constants);
        this.mUpdateInstalled = false;
    }

    @Override // com.android.server.devicepolicy.UpdateInstaller
    public void installUpdateInThread() {
        if (this.mUpdateInstalled) {
            throw new java.lang.IllegalStateException("installUpdateInThread can be called only once.");
        }
        try {
            setState();
            applyPayload(java.nio.file.Paths.get(this.mCopiedUpdateFile.getAbsolutePath(), new java.lang.String[0]).toUri().toString());
        } catch (java.util.zip.ZipException e) {
            android.util.Log.w("UpdateInstaller", e);
            notifyCallbackOnError(3, android.util.Log.getStackTraceString(e));
        } catch (java.io.IOException e2) {
            android.util.Log.w("UpdateInstaller", e2);
            notifyCallbackOnError(1, android.util.Log.getStackTraceString(e2));
        }
    }

    private void setState() throws java.io.IOException {
        this.mUpdateInstalled = true;
        this.mPackedUpdateFile = new java.util.zip.ZipFile(this.mCopiedUpdateFile);
        this.mProperties = new java.util.ArrayList();
        this.mSizeForUpdate = -1L;
        this.mOffsetForUpdate = 0L;
        this.mEntries = this.mPackedUpdateFile.entries();
    }

    private android.os.UpdateEngine buildBoundUpdateEngine() {
        android.os.UpdateEngine updateEngine = new android.os.UpdateEngine();
        updateEngine.bind(new com.android.server.devicepolicy.AbUpdateInstaller.DelegatingUpdateEngineCallback(this, updateEngine));
        return updateEngine;
    }

    private void applyPayload(java.lang.String updatePath) throws java.io.IOException {
        if (!updateStateForPayload()) {
            return;
        }
        java.lang.String[] headerKeyValuePairs = (java.lang.String[]) this.mProperties.stream().toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.AbUpdateInstaller$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.AbUpdateInstaller.lambda$applyPayload$0(i);
            }
        });
        if (this.mSizeForUpdate == -1) {
            android.util.Log.w("UpdateInstaller", "Failed to find payload entry in the given package.");
            notifyCallbackOnError(3, "Failed to find payload entry in the given package.");
            return;
        }
        android.os.UpdateEngine updateEngine = buildBoundUpdateEngine();
        try {
            updateEngine.applyPayload(updatePath, this.mOffsetForUpdate, this.mSizeForUpdate, headerKeyValuePairs);
        } catch (java.lang.Exception e) {
            android.util.Log.w("UpdateInstaller", "Failed to install update from file.", e);
            notifyCallbackOnError(1, "Failed to install update from file.");
        }
    }

    static /* synthetic */ java.lang.String[] lambda$applyPayload$0(int x$0) {
        return new java.lang.String[x$0];
    }

    private boolean updateStateForPayload() throws java.io.IOException {
        long offset = 0;
        while (this.mEntries.hasMoreElements()) {
            java.util.zip.ZipEntry entry = this.mEntries.nextElement();
            java.lang.String name = entry.getName();
            offset += buildOffsetForEntry(entry, name);
            if (entry.isDirectory()) {
                offset -= entry.getCompressedSize();
            } else if (PAYLOAD_BIN.equals(name)) {
                if (entry.getMethod() != 0) {
                    android.util.Log.w("UpdateInstaller", "Invalid compression method.");
                    notifyCallbackOnError(3, "Invalid compression method.");
                    return false;
                }
                this.mSizeForUpdate = entry.getCompressedSize();
                this.mOffsetForUpdate = offset - entry.getCompressedSize();
            } else if (PAYLOAD_PROPERTIES_TXT.equals(name)) {
                updatePropertiesForEntry(entry);
            }
        }
        return true;
    }

    private long buildOffsetForEntry(java.util.zip.ZipEntry entry, java.lang.String name) {
        return ((long) (name.length() + 30)) + entry.getCompressedSize() + ((long) (entry.getExtra() == null ? 0 : entry.getExtra().length));
    }

    private void updatePropertiesForEntry(java.util.zip.ZipEntry entry) throws java.io.IOException {
        java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(this.mPackedUpdateFile.getInputStream(entry)));
        while (true) {
            try {
                java.lang.String line = bufferedReader.readLine();
                if (line != null) {
                    this.mProperties.add(line);
                } else {
                    bufferedReader.close();
                    return;
                }
            } catch (java.lang.Throwable th) {
                try {
                    bufferedReader.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    private static class DelegatingUpdateEngineCallback extends android.os.UpdateEngineCallback {
        private android.os.UpdateEngine mUpdateEngine;
        private com.android.server.devicepolicy.UpdateInstaller mUpdateInstaller;

        DelegatingUpdateEngineCallback(com.android.server.devicepolicy.UpdateInstaller updateInstaller, android.os.UpdateEngine updateEngine) {
            this.mUpdateInstaller = updateInstaller;
            this.mUpdateEngine = updateEngine;
        }

        public void onStatusUpdate(int statusCode, float percentage) {
        }

        public void onPayloadApplicationComplete(int errorCode) {
            this.mUpdateEngine.unbind();
            if (errorCode == 0) {
                this.mUpdateInstaller.notifyCallbackOnSuccess();
            } else {
                this.mUpdateInstaller.notifyCallbackOnError(((java.lang.Integer) com.android.server.devicepolicy.AbUpdateInstaller.errorCodesMap.getOrDefault(java.lang.Integer.valueOf(errorCode), 1)).intValue(), (java.lang.String) com.android.server.devicepolicy.AbUpdateInstaller.errorStringsMap.getOrDefault(java.lang.Integer.valueOf(errorCode), com.android.server.devicepolicy.AbUpdateInstaller.UNKNOWN_ERROR + errorCode));
            }
        }
    }
}
