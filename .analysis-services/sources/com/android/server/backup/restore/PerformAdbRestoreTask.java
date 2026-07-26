package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public class PerformAdbRestoreTask implements java.lang.Runnable {
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final java.lang.String mCurrentPassword;
    private final java.lang.String mDecryptPassword;
    private final android.os.ParcelFileDescriptor mInputFile;
    private final java.util.concurrent.atomic.AtomicBoolean mLatchObject;
    private final com.android.server.backup.fullbackup.FullBackupObbConnection mObbConnection;
    private android.app.backup.IFullBackupRestoreObserver mObserver;
    private final com.android.server.backup.OperationStorage mOperationStorage;

    public PerformAdbRestoreTask(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.OperationStorage operationStorage, android.os.ParcelFileDescriptor fd, java.lang.String curPassword, java.lang.String decryptPassword, android.app.backup.IFullBackupRestoreObserver observer, java.util.concurrent.atomic.AtomicBoolean latch) {
        this.mBackupManagerService = backupManagerService;
        this.mOperationStorage = operationStorage;
        this.mInputFile = fd;
        this.mCurrentPassword = curPassword;
        this.mDecryptPassword = decryptPassword;
        this.mObserver = observer;
        this.mLatchObject = latch;
        this.mObbConnection = new com.android.server.backup.fullbackup.FullBackupObbConnection(backupManagerService);
    }

    /* JADX WARN: Removed duplicated region for block: B:94:0x013e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0181 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            Method dump skipped, instruction units count: 429
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.restore.PerformAdbRestoreTask.run():void");
    }

    private static void readFullyOrThrow(java.io.InputStream in, byte[] buffer) throws java.io.IOException {
        int offset = 0;
        while (offset < buffer.length) {
            int bytesRead = in.read(buffer, offset, buffer.length - offset);
            if (bytesRead <= 0) {
                throw new java.io.IOException("Couldn't fully read data");
            }
            offset += bytesRead;
        }
    }

    public static java.io.InputStream parseBackupFileHeaderAndReturnTarStream(java.io.InputStream rawInputStream, java.lang.String decryptPassword) throws java.io.IOException {
        boolean compressed = false;
        java.io.InputStream preCompressStream = rawInputStream;
        boolean okay = false;
        int headerLen = com.android.server.backup.UserBackupManagerService.BACKUP_FILE_HEADER_MAGIC.length();
        byte[] streamHeader = new byte[headerLen];
        readFullyOrThrow(rawInputStream, streamHeader);
        byte[] magicBytes = com.android.server.backup.UserBackupManagerService.BACKUP_FILE_HEADER_MAGIC.getBytes("UTF-8");
        if (java.util.Arrays.equals(magicBytes, streamHeader)) {
            java.lang.String s = readHeaderLine(rawInputStream);
            int archiveVersion = java.lang.Integer.parseInt(s);
            if (archiveVersion <= 5) {
                boolean pbkdf2Fallback = archiveVersion == 1;
                compressed = java.lang.Integer.parseInt(readHeaderLine(rawInputStream)) != 0;
                java.lang.String s2 = readHeaderLine(rawInputStream);
                if (s2.equals("none")) {
                    okay = true;
                } else if (decryptPassword != null && decryptPassword.length() > 0) {
                    preCompressStream = decodeAesHeaderAndInitialize(decryptPassword, s2, pbkdf2Fallback, rawInputStream);
                    if (preCompressStream != null) {
                        okay = true;
                    }
                } else {
                    android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Archive is encrypted but no password given");
                }
            } else {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Wrong header version: " + s);
            }
        } else {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Didn't read the right header magic");
        }
        if (okay) {
            return compressed ? new java.util.zip.InflaterInputStream(preCompressStream) : preCompressStream;
        }
        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Invalid restore data; aborting.");
        return null;
    }

    private static java.lang.String readHeaderLine(java.io.InputStream in) throws java.io.IOException {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder(80);
        while (true) {
            int c = in.read();
            if (c < 0 || c == 10) {
                break;
            }
            buffer.append((char) c);
        }
        return buffer.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x00ee  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0137  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.io.InputStream attemptEncryptionKeyDecryption(java.lang.String r20, java.lang.String r21, byte[] r22, byte[] r23, int r24, java.lang.String r25, java.lang.String r26, java.io.InputStream r27, boolean r28) {
        /*
            Method dump skipped, instruction units count: 320
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.restore.PerformAdbRestoreTask.attemptEncryptionKeyDecryption(java.lang.String, java.lang.String, byte[], byte[], int, java.lang.String, java.lang.String, java.io.InputStream, boolean):java.io.InputStream");
    }

    private static java.io.InputStream decodeAesHeaderAndInitialize(java.lang.String decryptPassword, java.lang.String encryptionName, boolean pbkdf2Fallback, java.io.InputStream rawInStream) {
        java.io.InputStream result = null;
        try {
            if (!encryptionName.equals(com.android.server.backup.utils.PasswordUtils.ENCRYPTION_ALGORITHM_NAME)) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Unsupported encryption method: " + encryptionName);
            } else {
                java.lang.String userSaltHex = readHeaderLine(rawInStream);
                byte[] userSalt = com.android.server.backup.utils.PasswordUtils.hexToByteArray(userSaltHex);
                java.lang.String ckSaltHex = readHeaderLine(rawInStream);
                byte[] ckSalt = com.android.server.backup.utils.PasswordUtils.hexToByteArray(ckSaltHex);
                int rounds = java.lang.Integer.parseInt(readHeaderLine(rawInStream));
                java.lang.String userIvHex = readHeaderLine(rawInStream);
                java.lang.String encryptionKeyBlobHex = readHeaderLine(rawInStream);
                result = attemptEncryptionKeyDecryption(decryptPassword, com.android.server.backup.BackupPasswordManager.PBKDF_CURRENT, userSalt, ckSalt, rounds, userIvHex, encryptionKeyBlobHex, rawInStream, false);
                if (result == null && pbkdf2Fallback) {
                    result = attemptEncryptionKeyDecryption(decryptPassword, com.android.server.backup.BackupPasswordManager.PBKDF_FALLBACK, userSalt, ckSalt, rounds, userIvHex, encryptionKeyBlobHex, rawInStream, true);
                }
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Can't read input header");
        } catch (java.lang.NumberFormatException e2) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Can't parse restore data header");
        }
        return result;
    }
}
