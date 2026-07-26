package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class ShortcutPackageInfo {
    private static final java.lang.String ATTR_BACKUP_ALLOWED = "allow-backup";
    private static final java.lang.String ATTR_BACKUP_ALLOWED_INITIALIZED = "allow-backup-initialized";
    private static final java.lang.String ATTR_BACKUP_SOURCE_BACKUP_ALLOWED = "bk_src_backup-allowed";
    private static final java.lang.String ATTR_BACKUP_SOURCE_VERSION = "bk_src_version";
    private static final java.lang.String ATTR_LAST_UPDATE_TIME = "last_udpate_time";
    private static final java.lang.String ATTR_SHADOW = "shadow";
    private static final java.lang.String ATTR_SIGNATURE_HASH = "hash";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final java.lang.String TAG = "ShortcutService";
    static final java.lang.String TAG_ROOT = "package-info";
    private static final java.lang.String TAG_SIGNATURE = "signature";
    private boolean mBackupAllowedInitialized;
    private boolean mIsShadow;
    private long mLastUpdateTime;
    private java.util.ArrayList<byte[]> mSigHashes;
    private long mVersionCode;
    private long mBackupSourceVersionCode = -1;
    private boolean mBackupAllowed = false;
    private boolean mBackupSourceBackupAllowed = false;

    private ShortcutPackageInfo(long versionCode, long lastUpdateTime, java.util.ArrayList<byte[]> sigHashes, boolean isShadow) {
        this.mVersionCode = -1L;
        this.mVersionCode = versionCode;
        this.mLastUpdateTime = lastUpdateTime;
        this.mIsShadow = isShadow;
        this.mSigHashes = sigHashes;
    }

    public static com.android.server.pm.ShortcutPackageInfo newEmpty() {
        return new com.android.server.pm.ShortcutPackageInfo(-1L, 0L, new java.util.ArrayList(0), false);
    }

    public boolean isShadow() {
        return this.mIsShadow;
    }

    public void setShadow(boolean shadow) {
        this.mIsShadow = shadow;
    }

    public long getVersionCode() {
        return this.mVersionCode;
    }

    public long getBackupSourceVersionCode() {
        return this.mBackupSourceVersionCode;
    }

    public boolean isBackupSourceBackupAllowed() {
        return this.mBackupSourceBackupAllowed;
    }

    public long getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    public boolean isBackupAllowed() {
        return this.mBackupAllowed;
    }

    public void updateFromPackageInfo(android.content.pm.PackageInfo pi) {
        if (pi != null) {
            this.mVersionCode = pi.getLongVersionCode();
            this.mLastUpdateTime = pi.lastUpdateTime;
            this.mBackupAllowed = com.android.server.pm.ShortcutService.shouldBackupApp(pi);
            this.mBackupAllowedInitialized = true;
        }
    }

    public boolean hasSignatures() {
        return this.mSigHashes.size() > 0;
    }

    public int canRestoreTo(com.android.server.pm.ShortcutService s, android.content.pm.PackageInfo currentPackage, boolean anyVersionOkay) {
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        if (!com.android.server.backup.BackupUtils.signaturesMatch(this.mSigHashes, currentPackage, pmi)) {
            android.util.Slog.w(TAG, "Can't restore: Package signature mismatch");
            return 102;
        }
        if (!com.android.server.pm.ShortcutService.shouldBackupApp(currentPackage) || !this.mBackupSourceBackupAllowed) {
            android.util.Slog.w(TAG, "Can't restore: package didn't or doesn't allow backup");
            return 101;
        }
        if (!anyVersionOkay && currentPackage.getLongVersionCode() < this.mBackupSourceVersionCode) {
            android.util.Slog.w(TAG, java.lang.String.format("Can't restore: package current version %d < backed up version %d", java.lang.Long.valueOf(currentPackage.getLongVersionCode()), java.lang.Long.valueOf(this.mBackupSourceVersionCode)));
            return 100;
        }
        return 0;
    }

    public static com.android.server.pm.ShortcutPackageInfo generateForInstalledPackageForTest(com.android.server.pm.ShortcutService s, java.lang.String packageName, int packageUserId) {
        android.content.pm.PackageInfo pi = s.getPackageInfoWithSignatures(packageName, packageUserId);
        android.content.pm.SigningInfo signingInfo = pi.signingInfo;
        if (signingInfo == null) {
            android.util.Slog.e(TAG, "Can't get signatures: package=" + packageName);
            return null;
        }
        android.content.pm.Signature[] signatures = signingInfo.getApkContentsSigners();
        com.android.server.pm.ShortcutPackageInfo ret = new com.android.server.pm.ShortcutPackageInfo(pi.getLongVersionCode(), pi.lastUpdateTime, com.android.server.backup.BackupUtils.hashSignatureArray(signatures), false);
        ret.mBackupSourceBackupAllowed = com.android.server.pm.ShortcutService.shouldBackupApp(pi);
        ret.mBackupSourceVersionCode = pi.getLongVersionCode();
        return ret;
    }

    public void refreshSignature(com.android.server.pm.ShortcutService s, com.android.server.pm.ShortcutPackageItem pkg) {
        if (this.mIsShadow) {
            s.wtf("Attempted to refresh package info for shadow package " + pkg.getPackageName() + ", user=" + pkg.getOwnerUserId());
            return;
        }
        android.content.pm.PackageInfo pi = s.getPackageInfoWithSignatures(pkg.getPackageName(), pkg.getPackageUserId());
        if (pi == null) {
            android.util.Slog.w(TAG, "Package not found: " + pkg.getPackageName());
            return;
        }
        android.content.pm.SigningInfo signingInfo = pi.signingInfo;
        if (signingInfo == null) {
            android.util.Slog.w(TAG, "Not refreshing signature for " + pkg.getPackageName() + " since it appears to have no signing info.");
        } else {
            android.content.pm.Signature[] signatures = signingInfo.getApkContentsSigners();
            this.mSigHashes = com.android.server.backup.BackupUtils.hashSignatureArray(signatures);
        }
    }

    public void saveToXml(com.android.server.pm.ShortcutService s, com.android.modules.utils.TypedXmlSerializer out, boolean forBackup) throws java.io.IOException {
        if (forBackup && !this.mBackupAllowedInitialized) {
            s.wtf("Backup happened before mBackupAllowed is initialized.");
        }
        out.startTag((java.lang.String) null, TAG_ROOT);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_VERSION, this.mVersionCode);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_LAST_UPDATE_TIME, this.mLastUpdateTime);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_SHADOW, this.mIsShadow);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_BACKUP_ALLOWED, this.mBackupAllowed);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_BACKUP_ALLOWED_INITIALIZED, this.mBackupAllowedInitialized);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_BACKUP_SOURCE_VERSION, this.mBackupSourceVersionCode);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_BACKUP_SOURCE_BACKUP_ALLOWED, this.mBackupSourceBackupAllowed);
        for (int i = 0; i < this.mSigHashes.size(); i++) {
            out.startTag((java.lang.String) null, TAG_SIGNATURE);
            java.lang.String encoded = java.util.Base64.getEncoder().encodeToString(this.mSigHashes.get(i));
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_SIGNATURE_HASH, encoded);
            out.endTag((java.lang.String) null, TAG_SIGNATURE);
        }
        out.endTag((java.lang.String) null, TAG_ROOT);
    }

    public void loadFromXml(com.android.modules.utils.TypedXmlPullParser parser, boolean fromBackup) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        byte b;
        com.android.modules.utils.TypedXmlPullParser typedXmlPullParser = parser;
        long versionCode = com.android.server.pm.ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_VERSION, -1L);
        long lastUpdateTime = com.android.server.pm.ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_LAST_UPDATE_TIME);
        int i = 1;
        boolean shadow = fromBackup || com.android.server.pm.ShortcutService.parseBooleanAttribute(typedXmlPullParser, ATTR_SHADOW);
        long backupSourceVersion = com.android.server.pm.ShortcutService.parseLongAttribute(typedXmlPullParser, ATTR_BACKUP_SOURCE_VERSION, -1L);
        boolean backupAllowed = com.android.server.pm.ShortcutService.parseBooleanAttribute(typedXmlPullParser, ATTR_BACKUP_ALLOWED, true);
        boolean backupSourceBackupAllowed = com.android.server.pm.ShortcutService.parseBooleanAttribute(typedXmlPullParser, ATTR_BACKUP_SOURCE_BACKUP_ALLOWED, true);
        java.util.ArrayList<byte[]> hashes = new java.util.ArrayList<>();
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != i && (type != 3 || parser.getDepth() > outerDepth)) {
                if (type == 2) {
                    int depth = parser.getDepth();
                    java.lang.String tag = parser.getName();
                    if (depth == outerDepth + 1) {
                        switch (tag.hashCode()) {
                            case 1073584312:
                                if (tag.equals(TAG_SIGNATURE)) {
                                    b = 0;
                                    break;
                                }
                            default:
                                b = -1;
                                break;
                        }
                        switch (b) {
                            case 0:
                                java.lang.String hash = com.android.server.pm.ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_SIGNATURE_HASH);
                                byte[] decoded = java.util.Base64.getDecoder().decode(hash);
                                hashes.add(decoded);
                                typedXmlPullParser = parser;
                                i = 1;
                                continue;
                        }
                    }
                    com.android.server.pm.ShortcutService.warnForInvalidTag(depth, tag);
                    typedXmlPullParser = parser;
                    i = 1;
                }
            }
        }
        if (fromBackup) {
            this.mVersionCode = -1L;
            this.mBackupSourceVersionCode = versionCode;
            this.mBackupSourceBackupAllowed = backupAllowed;
        } else {
            this.mVersionCode = versionCode;
            this.mBackupSourceVersionCode = backupSourceVersion;
            this.mBackupSourceBackupAllowed = backupSourceBackupAllowed;
        }
        this.mLastUpdateTime = lastUpdateTime;
        this.mIsShadow = shadow;
        this.mSigHashes = hashes;
        this.mBackupAllowed = false;
        this.mBackupAllowedInitialized = false;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println();
        pw.print(prefix);
        pw.println("PackageInfo:");
        pw.print(prefix);
        pw.print("  IsShadow: ");
        pw.print(this.mIsShadow);
        pw.print(this.mIsShadow ? " (not installed)" : " (installed)");
        pw.println();
        pw.print(prefix);
        pw.print("  Version: ");
        pw.print(this.mVersionCode);
        pw.println();
        if (this.mBackupAllowedInitialized) {
            pw.print(prefix);
            pw.print("  Backup Allowed: ");
            pw.print(this.mBackupAllowed);
            pw.println();
        }
        if (this.mBackupSourceVersionCode != -1) {
            pw.print(prefix);
            pw.print("  Backup source version: ");
            pw.print(this.mBackupSourceVersionCode);
            pw.println();
            pw.print(prefix);
            pw.print("  Backup source backup allowed: ");
            pw.print(this.mBackupSourceBackupAllowed);
            pw.println();
        }
        pw.print(prefix);
        pw.print("  Last package update time: ");
        pw.print(this.mLastUpdateTime);
        pw.println();
        for (int i = 0; i < this.mSigHashes.size(); i++) {
            pw.print(prefix);
            pw.print("    ");
            pw.print("SigHash: ");
            pw.println(libcore.util.HexEncoding.encode(this.mSigHashes.get(i)));
        }
    }
}
