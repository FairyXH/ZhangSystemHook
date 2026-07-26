package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public class TarBackupReader {
    private static final int TAR_HEADER_LENGTH_FILESIZE = 12;
    private static final int TAR_HEADER_LENGTH_MODE = 8;
    private static final int TAR_HEADER_LENGTH_MODTIME = 12;
    private static final int TAR_HEADER_LENGTH_PATH = 100;
    private static final int TAR_HEADER_LENGTH_PATH_PREFIX = 155;
    private static final int TAR_HEADER_LONG_RADIX = 8;
    private static final int TAR_HEADER_OFFSET_FILESIZE = 124;
    private static final int TAR_HEADER_OFFSET_MODE = 100;
    private static final int TAR_HEADER_OFFSET_MODTIME = 136;
    private static final int TAR_HEADER_OFFSET_PATH = 0;
    private static final int TAR_HEADER_OFFSET_PATH_PREFIX = 345;
    private static final int TAR_HEADER_OFFSET_TYPE_CHAR = 156;
    private com.android.server.backup.utils.BackupManagerMonitorEventSender mBackupManagerMonitorEventSender;
    private final com.android.server.backup.utils.BytesReadListener mBytesReadListener;
    private final java.io.InputStream mInputStream;
    private byte[] mWidgetData = null;

    public TarBackupReader(java.io.InputStream inputStream, com.android.server.backup.utils.BytesReadListener bytesReadListener, android.app.backup.IBackupManagerMonitor monitor) {
        this.mInputStream = inputStream;
        this.mBytesReadListener = bytesReadListener;
        this.mBackupManagerMonitorEventSender = new com.android.server.backup.utils.BackupManagerMonitorEventSender(monitor);
    }

    public com.android.server.backup.FileMetadata readTarHeaders() throws java.io.IOException {
        byte[] block = new byte[512];
        com.android.server.backup.FileMetadata info = null;
        if (readTarHeader(block)) {
            try {
                info = new com.android.server.backup.FileMetadata();
                info.size = extractRadix(block, 124, 12, 8);
                info.mtime = extractRadix(block, 136, 12, 8);
                info.mode = extractRadix(block, 100, 8, 8);
                info.path = extractString(block, 345, 155);
                java.lang.String path = extractString(block, 0, 100);
                if (path.length() > 0) {
                    if (info.path.length() > 0) {
                        info.path += '/';
                    }
                    info.path += path;
                }
                int typeChar = block[156];
                if (typeChar == 120) {
                    boolean gotHeader = readPaxExtendedHeader(info);
                    if (gotHeader) {
                        gotHeader = readTarHeader(block);
                    }
                    if (!gotHeader) {
                        throw new java.io.IOException("Bad or missing pax header");
                    }
                    typeChar = block[156];
                }
                switch (typeChar) {
                    case 0:
                        return null;
                    case 48:
                        info.type = 1;
                        break;
                    case 53:
                        info.type = 2;
                        if (info.size != 0) {
                            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Directory entry with nonzero size in header");
                            info.size = 0L;
                        }
                        break;
                    default:
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unknown tar entity type: " + typeChar);
                        throw new java.io.IOException("Unknown entity type " + typeChar);
                }
                if ("shared/".regionMatches(0, info.path, 0, "shared/".length())) {
                    info.path = info.path.substring("shared/".length());
                    info.packageName = com.android.server.backup.UserBackupManagerService.SHARED_BACKUP_AGENT_PACKAGE;
                    info.domain = "shared";
                    android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "File in shared storage: " + info.path);
                } else if ("apps/".regionMatches(0, info.path, 0, "apps/".length())) {
                    info.path = info.path.substring("apps/".length());
                    int slash = info.path.indexOf(47);
                    if (slash >= 0) {
                        info.packageName = info.path.substring(0, slash);
                        info.path = info.path.substring(slash + 1);
                        if (!info.path.equals(com.android.server.backup.UserBackupManagerService.BACKUP_MANIFEST_FILENAME) && !info.path.equals(com.android.server.backup.UserBackupManagerService.BACKUP_METADATA_FILENAME)) {
                            int slash2 = info.path.indexOf(47);
                            if (slash2 >= 0) {
                                info.domain = info.path.substring(0, slash2);
                                info.path = info.path.substring(slash2 + 1);
                            } else {
                                throw new java.io.IOException("Illegal semantic path in non-manifest " + info.path);
                            }
                        }
                    } else {
                        throw new java.io.IOException("Illegal semantic path in " + info.path);
                    }
                }
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Parse error in header: " + e.getMessage());
                throw e;
            }
        }
        return info;
    }

    private static int readExactly(java.io.InputStream in, byte[] buffer, int offset, int size) throws java.io.IOException {
        if (size <= 0) {
            throw new java.lang.IllegalArgumentException("size must be > 0");
        }
        int soFar = 0;
        while (soFar < size) {
            int nRead = in.read(buffer, offset + soFar, size - soFar);
            if (nRead <= 0) {
                break;
            }
            soFar += nRead;
        }
        return soFar;
    }

    public android.content.pm.Signature[] readAppManifestAndReturnSignatures(com.android.server.backup.FileMetadata info) throws java.io.IOException {
        if (info.size > 65536) {
            throw new java.io.IOException("Restore manifest too big; corrupt? size=" + info.size);
        }
        byte[] buffer = new byte[(int) info.size];
        if (readExactly(this.mInputStream, buffer, 0, (int) info.size) == info.size) {
            this.mBytesReadListener.onBytesRead(info.size);
            java.lang.String[] str = new java.lang.String[1];
            try {
                int offset = extractLine(buffer, 0, str);
                int version = java.lang.Integer.parseInt(str[0]);
                if (version == 1) {
                    int offset2 = extractLine(buffer, offset, str);
                    java.lang.String manifestPackage = str[0];
                    if (manifestPackage.equals(info.packageName)) {
                        int offset3 = extractLine(buffer, offset2, str);
                        info.version = java.lang.Integer.parseInt(str[0]);
                        int offset4 = extractLine(buffer, offset3, str);
                        java.lang.Integer.parseInt(str[0]);
                        int offset5 = extractLine(buffer, offset4, str);
                        info.installerPackageName = str[0].length() > 0 ? str[0] : null;
                        int offset6 = extractLine(buffer, offset5, str);
                        info.hasApk = str[0].equals("1");
                        int offset7 = extractLine(buffer, offset6, str);
                        int numSigs = java.lang.Integer.parseInt(str[0]);
                        if (numSigs > 0) {
                            android.content.pm.Signature[] sigs = new android.content.pm.Signature[numSigs];
                            for (int i = 0; i < numSigs; i++) {
                                offset7 = extractLine(buffer, offset7, str);
                                sigs[i] = new android.content.pm.Signature(str[0]);
                            }
                            return sigs;
                        }
                        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Missing signature on backed-up package " + info.packageName);
                        this.mBackupManagerMonitorEventSender.monitorEvent(42, null, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", info.packageName));
                    } else {
                        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Expected package " + info.packageName + " but restore manifest claims " + manifestPackage);
                        android.os.Bundle monitoringExtras = com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", info.packageName);
                        this.mBackupManagerMonitorEventSender.monitorEvent(43, null, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra(monitoringExtras, "android.app.backup.extra.LOG_MANIFEST_PACKAGE_NAME", manifestPackage));
                    }
                } else {
                    android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Unknown restore manifest version " + version + " for package " + info.packageName);
                    android.os.Bundle monitoringExtras2 = com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", info.packageName);
                    this.mBackupManagerMonitorEventSender.monitorEvent(44, null, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra(monitoringExtras2, "android.app.backup.extra.LOG_EVENT_PACKAGE_VERSION", version));
                }
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Corrupt restore manifest for package " + info.packageName);
                this.mBackupManagerMonitorEventSender.monitorEvent(46, null, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", info.packageName));
            } catch (java.lang.IllegalArgumentException e2) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, e2.getMessage());
            }
            return null;
        }
        throw new java.io.IOException("Unexpected EOF in manifest");
    }

    public com.android.server.backup.restore.RestorePolicy chooseRestorePolicy(android.content.pm.PackageManager packageManager, boolean allowApks, com.android.server.backup.FileMetadata info, android.content.pm.Signature[] signatures, android.content.pm.PackageManagerInternal pmi, int userId, android.content.Context context) {
        return chooseRestorePolicy(packageManager, allowApks, info, signatures, pmi, userId, com.android.server.backup.utils.BackupEligibilityRules.forBackup(packageManager, pmi, userId, context), context);
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01f9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.android.server.backup.restore.RestorePolicy chooseRestorePolicy(android.content.pm.PackageManager r19, boolean r20, com.android.server.backup.FileMetadata r21, android.content.pm.Signature[] r22, android.content.pm.PackageManagerInternal r23, int r24, com.android.server.backup.utils.BackupEligibilityRules r25, android.content.Context r26) {
        /*
            Method dump skipped, instruction units count: 555
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.utils.TarBackupReader.chooseRestorePolicy(android.content.pm.PackageManager, boolean, com.android.server.backup.FileMetadata, android.content.pm.Signature[], android.content.pm.PackageManagerInternal, int, com.android.server.backup.utils.BackupEligibilityRules, android.content.Context):com.android.server.backup.restore.RestorePolicy");
    }

    public void skipTarPadding(long size) throws java.io.IOException {
        long partial = (size + 512) % 512;
        if (partial > 0) {
            int needed = 512 - ((int) partial);
            byte[] buffer = new byte[needed];
            if (readExactly(this.mInputStream, buffer, 0, needed) == needed) {
                this.mBytesReadListener.onBytesRead(needed);
                return;
            }
            throw new java.io.IOException("Unexpected EOF in padding");
        }
    }

    public void readMetadata(com.android.server.backup.FileMetadata info) throws java.io.IOException {
        if (info.size > 65536) {
            throw new java.io.IOException("Metadata too big; corrupt? size=" + info.size);
        }
        byte[] buffer = new byte[(int) info.size];
        if (readExactly(this.mInputStream, buffer, 0, (int) info.size) == info.size) {
            this.mBytesReadListener.onBytesRead(info.size);
            java.lang.String[] str = new java.lang.String[1];
            int offset = extractLine(buffer, 0, str);
            int version = java.lang.Integer.parseInt(str[0]);
            if (version == 1) {
                int offset2 = extractLine(buffer, offset, str);
                java.lang.String pkg = str[0];
                if (info.packageName.equals(pkg)) {
                    java.io.ByteArrayInputStream bin = new java.io.ByteArrayInputStream(buffer, offset2, buffer.length - offset2);
                    java.io.DataInputStream in = new java.io.DataInputStream(bin);
                    while (bin.available() > 0) {
                        int token = in.readInt();
                        int size = in.readInt();
                        if (size > 65536) {
                            throw new java.io.IOException("Datum " + java.lang.Integer.toHexString(token) + " too big; corrupt? size=" + info.size);
                        }
                        switch (token) {
                            case com.android.server.backup.UserBackupManagerService.BACKUP_WIDGET_METADATA_TOKEN /* 33549569 */:
                                this.mWidgetData = new byte[size];
                                in.read(this.mWidgetData);
                                break;
                            default:
                                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Ignoring metadata blob " + java.lang.Integer.toHexString(token) + " for " + info.packageName);
                                in.skipBytes(size);
                                break;
                        }
                    }
                    return;
                }
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Metadata mismatch: package " + info.packageName + " but widget data for " + pkg);
                android.os.Bundle monitoringExtras = com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", info.packageName);
                this.mBackupManagerMonitorEventSender.monitorEvent(47, null, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra(monitoringExtras, "android.app.backup.extra.LOG_WIDGET_PACKAGE_NAME", pkg));
                return;
            }
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Unsupported metadata version " + version);
            android.os.Bundle monitoringExtras2 = com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", info.packageName);
            this.mBackupManagerMonitorEventSender.monitorEvent(48, null, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra(monitoringExtras2, "android.app.backup.extra.LOG_EVENT_PACKAGE_VERSION", version));
            return;
        }
        throw new java.io.IOException("Unexpected EOF in widget data");
    }

    private static int extractLine(byte[] buffer, int offset, java.lang.String[] outStr) throws java.io.IOException {
        int end = buffer.length;
        if (offset >= end) {
            throw new java.io.IOException("Incomplete data");
        }
        int pos = offset;
        while (pos < end) {
            byte c = buffer[pos];
            if (c == 10) {
                break;
            }
            pos++;
        }
        outStr[0] = new java.lang.String(buffer, offset, pos - offset);
        return pos + 1;
    }

    private boolean readTarHeader(byte[] block) throws java.io.IOException {
        int got = readExactly(this.mInputStream, block, 0, 512);
        if (got == 0) {
            return false;
        }
        if (got < 512) {
            throw new java.io.IOException("Unable to read full block header");
        }
        this.mBytesReadListener.onBytesRead(512L);
        return true;
    }

    private boolean readPaxExtendedHeader(com.android.server.backup.FileMetadata info) throws java.io.IOException {
        if (info.size > 32768) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Suspiciously large pax header size " + info.size + " - aborting");
            throw new java.io.IOException("Sanity failure: pax header size " + info.size);
        }
        int numBlocks = (int) ((info.size + 511) >> 9);
        byte[] data = new byte[numBlocks * 512];
        if (readExactly(this.mInputStream, data, 0, data.length) < data.length) {
            throw new java.io.IOException("Unable to read full pax header");
        }
        this.mBytesReadListener.onBytesRead(data.length);
        int contentSize = (int) info.size;
        int offset = 0;
        do {
            int eol = offset + 1;
            while (eol < contentSize && data[eol] != 32) {
                eol++;
            }
            if (eol >= contentSize) {
                throw new java.io.IOException("Invalid pax data");
            }
            int linelen = (int) extractRadix(data, offset, eol - offset, 10);
            int key = eol + 1;
            int eol2 = (offset + linelen) - 1;
            int value = key + 1;
            while (data[value] != 61 && value <= eol2) {
                value++;
            }
            if (value > eol2) {
                throw new java.io.IOException("Invalid pax declaration");
            }
            java.lang.String keyStr = new java.lang.String(data, key, value - key, "UTF-8");
            java.lang.String valStr = new java.lang.String(data, value + 1, (eol2 - value) - 1, "UTF-8");
            if ("path".equals(keyStr)) {
                info.path = valStr;
            } else if ("size".equals(keyStr)) {
                info.size = java.lang.Long.parseLong(valStr);
            } else {
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Unhandled pax key: " + key);
            }
            offset += linelen;
        } while (offset < contentSize);
        return true;
    }

    private boolean isAllowlistedForVToURestore(com.android.server.backup.FileMetadata backupFileInfo, android.content.pm.PackageInfo installedPackageInfo, int userId, android.content.Context context) {
        java.lang.String vToUAllowlist = getVToUAllowlist(context, userId);
        java.util.List<java.lang.String> mVToUAllowlist = java.util.Arrays.asList(vToUAllowlist.split(","));
        return com.android.server.backup.Flags.enableVToURestoreForSystemComponentsInAllowlist() && installedPackageInfo.getLongVersionCode() == 34 && backupFileInfo.version > 34 && mVToUAllowlist.contains(installedPackageInfo.packageName);
    }

    private java.lang.String getVToUAllowlist(android.content.Context context, int userId) {
        java.lang.String allowlist = android.provider.Settings.Secure.getStringForUser(context.getContentResolver(), "v_to_u_restore_allowlist", userId);
        return allowlist == null ? "" : allowlist;
    }

    private static long extractRadix(byte[] data, int offset, int maxChars, int radix) throws java.io.IOException {
        long value = 0;
        int end = offset + maxChars;
        for (int i = offset; i < end; i++) {
            byte b = data[i];
            if (b == 0 || b == 32) {
                break;
            }
            if (b < 48 || b > (radix + 48) - 1) {
                throw new java.io.IOException("Invalid number in header: '" + ((char) b) + "' for radix " + radix);
            }
            value = (((long) radix) * value) + ((long) (b - 48));
        }
        return value;
    }

    private static java.lang.String extractString(byte[] data, int offset, int maxChars) throws java.io.IOException {
        int end = offset + maxChars;
        int eos = offset;
        while (eos < end && data[eos] != 0) {
            eos++;
        }
        return new java.lang.String(data, offset, eos - offset, "US-ASCII");
    }

    private static void hexLog(byte[] block) {
        int offset = 0;
        int remaining = block.length;
        java.lang.StringBuilder buf = new java.lang.StringBuilder(64);
        while (remaining > 0) {
            buf.append(java.lang.String.format("%04x   ", java.lang.Integer.valueOf(offset)));
            int numThisLine = remaining <= 16 ? remaining : 16;
            for (int i = 0; i < numThisLine; i++) {
                buf.append(java.lang.String.format("%02x ", java.lang.Byte.valueOf(block[offset + i])));
            }
            android.util.Slog.i("hexdump", buf.toString());
            buf.setLength(0);
            remaining -= numThisLine;
            offset += numThisLine;
        }
    }

    public android.app.backup.IBackupManagerMonitor getMonitor() {
        return this.mBackupManagerMonitorEventSender.getMonitor();
    }

    public byte[] getWidgetData() {
        return this.mWidgetData;
    }
}
