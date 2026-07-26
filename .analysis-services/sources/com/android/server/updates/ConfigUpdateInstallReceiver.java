package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class ConfigUpdateInstallReceiver extends android.content.BroadcastReceiver {
    private static final java.lang.String EXTRA_REQUIRED_HASH = "REQUIRED_HASH";
    private static final java.lang.String EXTRA_VERSION_NUMBER = "VERSION";
    private static final java.lang.String TAG = "ConfigUpdateInstallReceiver";
    protected final java.io.File updateContent;
    protected final java.io.File updateDir;
    protected final java.io.File updateVersion;

    public ConfigUpdateInstallReceiver(java.lang.String updateDir, java.lang.String updateContentPath, java.lang.String updateMetadataPath, java.lang.String updateVersionPath) {
        this.updateDir = new java.io.File(updateDir);
        this.updateContent = new java.io.File(updateDir, updateContentPath);
        java.io.File updateMetadataDir = new java.io.File(updateDir, updateMetadataPath);
        this.updateVersion = new java.io.File(updateMetadataDir, updateVersionPath);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.updates.ConfigUpdateInstallReceiver$1] */
    @Override // android.content.BroadcastReceiver
    public void onReceive(final android.content.Context context, final android.content.Intent intent) {
        new java.lang.Thread() { // from class: com.android.server.updates.ConfigUpdateInstallReceiver.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    int altVersion = com.android.server.updates.ConfigUpdateInstallReceiver.this.getVersionFromIntent(intent);
                    java.lang.String altRequiredHash = com.android.server.updates.ConfigUpdateInstallReceiver.this.getRequiredHashFromIntent(intent);
                    int currentVersion = com.android.server.updates.ConfigUpdateInstallReceiver.this.getCurrentVersion();
                    java.lang.String currentHash = com.android.server.updates.ConfigUpdateInstallReceiver.getCurrentHash(com.android.server.updates.ConfigUpdateInstallReceiver.this.getCurrentContent());
                    if (!com.android.server.updates.ConfigUpdateInstallReceiver.this.verifyVersion(currentVersion, altVersion)) {
                        android.util.Slog.i(com.android.server.updates.ConfigUpdateInstallReceiver.TAG, "Not installing, new version is <= current version");
                        return;
                    }
                    if (com.android.server.updates.ConfigUpdateInstallReceiver.this.verifyPreviousHash(currentHash, altRequiredHash)) {
                        android.util.Slog.i(com.android.server.updates.ConfigUpdateInstallReceiver.TAG, "Found new update, installing...");
                        java.io.BufferedInputStream altContent = com.android.server.updates.ConfigUpdateInstallReceiver.this.getAltContent(context, intent);
                        try {
                            com.android.server.updates.ConfigUpdateInstallReceiver.this.install(altContent, altVersion);
                            if (altContent != null) {
                                altContent.close();
                            }
                            android.util.Slog.i(com.android.server.updates.ConfigUpdateInstallReceiver.TAG, "Installation successful");
                            com.android.server.updates.ConfigUpdateInstallReceiver.this.postInstall(context, intent);
                            return;
                        } finally {
                        }
                    }
                    android.util.EventLog.writeEvent(com.android.server.EventLogTags.CONFIG_INSTALL_FAILED, "Current hash did not match required value");
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(com.android.server.updates.ConfigUpdateInstallReceiver.TAG, "Could not update content!", e);
                    java.lang.String errMsg = e.toString();
                    if (errMsg.length() > 100) {
                        errMsg = errMsg.substring(0, 99);
                    }
                    android.util.EventLog.writeEvent(com.android.server.EventLogTags.CONFIG_INSTALL_FAILED, errMsg);
                }
            }
        }.start();
    }

    private android.net.Uri getContentFromIntent(android.content.Intent i) {
        android.net.Uri data = i.getData();
        if (data == null) {
            throw new java.lang.IllegalStateException("Missing required content path, ignoring.");
        }
        return data;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVersionFromIntent(android.content.Intent i) throws java.lang.NumberFormatException {
        java.lang.String extraValue = i.getStringExtra(EXTRA_VERSION_NUMBER);
        if (extraValue == null) {
            throw new java.lang.IllegalStateException("Missing required version number, ignoring.");
        }
        return java.lang.Integer.parseInt(extraValue.trim());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getRequiredHashFromIntent(android.content.Intent i) {
        java.lang.String extraValue = i.getStringExtra(EXTRA_REQUIRED_HASH);
        if (extraValue == null) {
            throw new java.lang.IllegalStateException("Missing required previous hash, ignoring.");
        }
        return extraValue.trim();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentVersion() throws java.lang.NumberFormatException {
        try {
            java.lang.String strVersion = libcore.io.IoUtils.readFileAsString(this.updateVersion.getCanonicalPath()).trim();
            return java.lang.Integer.parseInt(strVersion);
        } catch (java.io.IOException e) {
            android.util.Slog.i(TAG, "Couldn't find current metadata, assuming first update");
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.io.BufferedInputStream getAltContent(android.content.Context c, android.content.Intent i) throws java.io.IOException {
        android.net.Uri content = getContentFromIntent(i);
        android.os.Binder.allowBlockingForCurrentThread();
        try {
            return new java.io.BufferedInputStream(c.getContentResolver().openInputStream(content));
        } finally {
            android.os.Binder.defaultBlockingForCurrentThread();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] getCurrentContent() {
        try {
            return libcore.io.IoUtils.readFileAsByteArray(this.updateContent.getCanonicalPath());
        } catch (java.io.IOException e) {
            android.util.Slog.i(TAG, "Failed to read current content, assuming first update!");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getCurrentHash(byte[] content) {
        if (content == null) {
            return "0";
        }
        try {
            java.security.MessageDigest dgst = java.security.MessageDigest.getInstance("SHA512");
            byte[] fingerprint = dgst.digest(content);
            return com.android.internal.util.HexDump.toHexString(fingerprint, false);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.lang.AssertionError(e);
        }
    }

    protected boolean verifyVersion(int current, int alternative) {
        return current < alternative;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean verifyPreviousHash(java.lang.String current, java.lang.String required) {
        if (required.equals("NONE")) {
            return true;
        }
        return current.equals(required);
    }

    protected void writeUpdate(java.io.File dir, java.io.File file, java.io.InputStream inputStream) throws java.io.IOException {
        java.io.FileOutputStream out = null;
        java.io.File tmp = null;
        try {
            java.io.File parent = file.getParentFile();
            parent.mkdirs();
            if (!parent.exists()) {
                throw new java.io.IOException("Failed to create directory " + parent.getCanonicalPath());
            }
            while (!parent.equals(this.updateDir)) {
                parent.setExecutable(true, false);
                parent = parent.getParentFile();
            }
            tmp = java.io.File.createTempFile("journal", "", dir);
            tmp.setReadable(true, false);
            out = new java.io.FileOutputStream(tmp);
            libcore.io.Streams.copy(inputStream, out);
            out.getFD().sync();
            if (!tmp.renameTo(file)) {
                throw new java.io.IOException("Failed to atomically rename " + file.getCanonicalPath());
            }
        } finally {
            if (tmp != null) {
                tmp.delete();
            }
            libcore.io.IoUtils.closeQuietly(out);
        }
    }

    protected void install(java.io.InputStream inputStream, int version) throws java.io.IOException {
        writeUpdate(this.updateDir, this.updateContent, inputStream);
        writeUpdate(this.updateDir, this.updateVersion, new java.io.ByteArrayInputStream(java.lang.Long.toString(version).getBytes()));
    }

    protected void postInstall(android.content.Context context, android.content.Intent intent) {
    }
}
