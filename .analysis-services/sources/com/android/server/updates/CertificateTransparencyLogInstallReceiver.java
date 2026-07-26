package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class CertificateTransparencyLogInstallReceiver extends com.android.server.updates.ConfigUpdateInstallReceiver {
    private static final java.lang.String LOGDIR_PREFIX = "logs-";
    private static final java.lang.String TAG = "CTLogInstallReceiver";

    public CertificateTransparencyLogInstallReceiver() {
        super("/data/misc/keychain/trusted_ct_logs/", "ct_logs", "metadata/", "version");
    }

    @Override // com.android.server.updates.ConfigUpdateInstallReceiver
    protected void install(java.io.InputStream inputStream, int version) throws java.lang.Exception {
        this.updateDir.mkdir();
        if (!this.updateDir.isDirectory()) {
            throw new java.io.IOException("Unable to make directory " + this.updateDir.getCanonicalPath());
        }
        if (!this.updateDir.setReadable(true, false)) {
            throw new java.io.IOException("Unable to set permissions on " + this.updateDir.getCanonicalPath());
        }
        java.io.File currentSymlink = new java.io.File(this.updateDir, "current");
        java.io.File newVersion = new java.io.File(this.updateDir, LOGDIR_PREFIX + java.lang.String.valueOf(version));
        if (newVersion.exists()) {
            if (newVersion.getCanonicalPath().equals(currentSymlink.getCanonicalPath())) {
                writeUpdate(this.updateDir, this.updateVersion, new java.io.ByteArrayInputStream(java.lang.Long.toString(version).getBytes()));
                deleteOldLogDirectories();
                return;
            }
            android.os.FileUtils.deleteContentsAndDir(newVersion);
        }
        try {
            newVersion.mkdir();
            if (!newVersion.isDirectory()) {
                throw new java.io.IOException("Unable to make directory " + newVersion.getCanonicalPath());
            }
            if (!newVersion.setReadable(true, false)) {
                throw new java.io.IOException("Failed to set " + newVersion.getCanonicalPath() + " readable");
            }
            try {
                byte[] content = libcore.io.Streams.readFullyNoClose(inputStream);
                org.json.JSONObject json = new org.json.JSONObject(new java.lang.String(content, java.nio.charset.StandardCharsets.UTF_8));
                org.json.JSONArray logs = json.getJSONArray("logs");
                for (int i = 0; i < logs.length(); i++) {
                    org.json.JSONObject log = logs.getJSONObject(i);
                    installLog(newVersion, log);
                }
                java.io.File tempSymlink = new java.io.File(this.updateDir, "new_symlink");
                try {
                    android.system.Os.symlink(newVersion.getCanonicalPath(), tempSymlink.getCanonicalPath());
                    tempSymlink.renameTo(currentSymlink.getAbsoluteFile());
                    android.util.Slog.i(TAG, "CT log directory updated to " + newVersion.getAbsolutePath());
                    writeUpdate(this.updateDir, this.updateVersion, new java.io.ByteArrayInputStream(java.lang.Long.toString(version).getBytes()));
                    deleteOldLogDirectories();
                } catch (android.system.ErrnoException e) {
                    throw new java.io.IOException("Failed to create symlink", e);
                }
            } catch (org.json.JSONException e2) {
                throw new java.io.IOException("Failed to parse logs", e2);
            }
        } catch (java.io.IOException | java.lang.RuntimeException e3) {
            android.os.FileUtils.deleteContentsAndDir(newVersion);
            throw e3;
        }
    }

    private void installLog(java.io.File directory, org.json.JSONObject logObject) throws java.io.IOException {
        try {
            java.lang.String logFilename = getLogFileName(logObject.getString("key"));
            java.io.File file = new java.io.File(directory, logFilename);
            java.io.OutputStreamWriter out = new java.io.OutputStreamWriter(new java.io.FileOutputStream(file), java.nio.charset.StandardCharsets.UTF_8);
            try {
                writeLogEntry(out, "key", logObject.getString("key"));
                writeLogEntry(out, "url", logObject.getString("url"));
                writeLogEntry(out, "description", logObject.getString("description"));
                out.close();
                if (!file.setReadable(true, false)) {
                    throw new java.io.IOException("Failed to set permissions on " + file.getCanonicalPath());
                }
            } finally {
            }
        } catch (org.json.JSONException e) {
            throw new java.io.IOException("Failed to parse log", e);
        }
    }

    private java.lang.String getLogFileName(java.lang.String base64PublicKey) {
        byte[] keyBytes = android.util.Base64.decode(base64PublicKey, 0);
        try {
            byte[] id = java.security.MessageDigest.getInstance("SHA-256").digest(keyBytes);
            return com.android.internal.util.HexDump.toHexString(id, false);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private void writeLogEntry(java.io.OutputStreamWriter out, java.lang.String key, java.lang.String value) throws java.io.IOException {
        out.write(key + ":" + value + "\n");
    }

    private void deleteOldLogDirectories() throws java.io.IOException {
        if (!this.updateDir.exists()) {
            return;
        }
        final java.io.File currentTarget = new java.io.File(this.updateDir, "current").getCanonicalFile();
        java.io.FileFilter filter = new java.io.FileFilter() { // from class: com.android.server.updates.CertificateTransparencyLogInstallReceiver.1
            @Override // java.io.FileFilter
            public boolean accept(java.io.File file) {
                return !currentTarget.equals(file) && file.getName().startsWith(com.android.server.updates.CertificateTransparencyLogInstallReceiver.LOGDIR_PREFIX);
            }
        };
        for (java.io.File f : this.updateDir.listFiles(filter)) {
            android.os.FileUtils.deleteContentsAndDir(f);
        }
    }
}
