package com.android.server.accounts;

/* JADX INFO: loaded from: classes.dex */
public final class AccountManagerBackupHelper {
    private static final java.lang.String ATTR_ACCOUNT_SHA_256 = "account-sha-256";
    private static final java.lang.String ATTR_DIGEST = "digest";
    private static final java.lang.String ATTR_PACKAGE = "package";
    private static final long PENDING_RESTORE_TIMEOUT_MILLIS = 3600000;
    private static final java.lang.String TAG = "AccountManagerBackupHelper";
    private static final java.lang.String TAG_PERMISSION = "permission";
    private static final java.lang.String TAG_PERMISSIONS = "permissions";
    private final android.accounts.AccountManagerInternal mAccountManagerInternal;
    private final com.android.server.accounts.AccountManagerService mAccountManagerService;
    private final java.lang.Object mLock = new java.lang.Object();
    private java.lang.Runnable mRestoreCancelCommand;
    private com.android.server.accounts.AccountManagerBackupHelper.RestorePackageMonitor mRestorePackageMonitor;
    private java.util.List<com.android.server.accounts.AccountManagerBackupHelper.PendingAppPermission> mRestorePendingAppPermissions;

    public AccountManagerBackupHelper(com.android.server.accounts.AccountManagerService accountManagerService, android.accounts.AccountManagerInternal accountManagerInternal) {
        this.mAccountManagerService = accountManagerService;
        this.mAccountManagerInternal = accountManagerInternal;
    }

    private final class PendingAppPermission {
        private final java.lang.String accountDigest;
        private final java.lang.String certDigest;
        private final java.lang.String packageName;
        private final int userId;

        public PendingAppPermission(java.lang.String accountDigest, java.lang.String packageName, java.lang.String certDigest, int userId) {
            this.accountDigest = accountDigest;
            this.packageName = packageName;
            this.certDigest = certDigest;
            this.userId = userId;
        }

        public boolean apply(android.content.pm.PackageManager packageManager) {
            android.accounts.Account account = null;
            com.android.server.accounts.AccountManagerService.UserAccounts accounts = com.android.server.accounts.AccountManagerBackupHelper.this.mAccountManagerService.getUserAccounts(this.userId);
            synchronized (accounts.dbLock) {
                synchronized (accounts.cacheLock) {
                    for (android.accounts.Account[] accountsPerType : accounts.accountCache.values()) {
                        int length = accountsPerType.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                break;
                            }
                            android.accounts.Account accountPerType = accountsPerType[i];
                            if (!this.accountDigest.equals(android.util.PackageUtils.computeSha256Digest(accountPerType.name.getBytes()))) {
                                i++;
                            } else {
                                account = accountPerType;
                                break;
                            }
                        }
                        if (account != null) {
                            break;
                        }
                    }
                }
            }
            if (account == null) {
                return false;
            }
            try {
                android.content.pm.PackageInfo packageInfo = packageManager.getPackageInfoAsUser(this.packageName, 64, this.userId);
                java.lang.String[] signaturesSha256Digests = android.util.PackageUtils.computeSignaturesSha256Digests(packageInfo.signatures);
                java.lang.String signaturesSha256Digest = android.util.PackageUtils.computeSignaturesSha256Digest(signaturesSha256Digests);
                if (!this.certDigest.equals(signaturesSha256Digest) && (packageInfo.signatures.length <= 1 || !this.certDigest.equals(signaturesSha256Digests[0]))) {
                    return false;
                }
                int uid = packageInfo.applicationInfo.uid;
                if (!com.android.server.accounts.AccountManagerBackupHelper.this.mAccountManagerInternal.hasAccountAccess(account, uid)) {
                    com.android.server.accounts.AccountManagerBackupHelper.this.mAccountManagerService.grantAppPermission(account, "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", uid);
                }
                return true;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return false;
            }
        }
    }

    public byte[] backupAccountAccessPermissions(int userId) throws java.lang.Throwable {
        java.util.List<android.util.Pair<java.lang.String, java.lang.Integer>> allAccountGrants;
        int i;
        int i2 = userId;
        com.android.server.accounts.AccountManagerService.UserAccounts accounts = this.mAccountManagerService.getUserAccounts(i2);
        synchronized (accounts.dbLock) {
            try {
                try {
                    try {
                    } catch (java.lang.Throwable th) {
                        e = th;
                    }
                    synchronized (accounts.cacheLock) {
                        try {
                            java.util.List<android.util.Pair<java.lang.String, java.lang.Integer>> allAccountGrants2 = accounts.accountsDb.findAllAccountGrants();
                            if (!allAccountGrants2.isEmpty()) {
                                try {
                                    java.io.ByteArrayOutputStream dataStream = new java.io.ByteArrayOutputStream();
                                    com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.newFastSerializer();
                                    serializer.setOutput(dataStream, java.nio.charset.StandardCharsets.UTF_8.name());
                                    serializer.startDocument((java.lang.String) null, true);
                                    serializer.startTag((java.lang.String) null, TAG_PERMISSIONS);
                                    android.content.pm.PackageManager packageManager = this.mAccountManagerService.mContext.getPackageManager();
                                    for (android.util.Pair<java.lang.String, java.lang.Integer> grant : allAccountGrants2) {
                                        java.lang.String accountName = (java.lang.String) grant.first;
                                        int uid = ((java.lang.Integer) grant.second).intValue();
                                        java.lang.String[] packageNames = packageManager.getPackagesForUid(uid);
                                        if (packageNames != null) {
                                            int length = packageNames.length;
                                            int i3 = 0;
                                            while (i3 < length) {
                                                java.lang.String packageName = packageNames[i3];
                                                com.android.server.accounts.AccountManagerService.UserAccounts accounts2 = accounts;
                                                try {
                                                    try {
                                                        android.content.pm.PackageInfo packageInfo = packageManager.getPackageInfoAsUser(packageName, 64, i2);
                                                        java.lang.String digest = android.util.PackageUtils.computeSignaturesSha256Digest(packageInfo.signatures);
                                                        if (digest != null) {
                                                            allAccountGrants = allAccountGrants2;
                                                            try {
                                                                serializer.startTag((java.lang.String) null, "permission");
                                                                i = length;
                                                                serializer.attribute((java.lang.String) null, ATTR_ACCOUNT_SHA_256, android.util.PackageUtils.computeSha256Digest(accountName.getBytes()));
                                                                serializer.attribute((java.lang.String) null, "package", packageName);
                                                                serializer.attribute((java.lang.String) null, ATTR_DIGEST, digest);
                                                                serializer.endTag((java.lang.String) null, "permission");
                                                            } catch (java.io.IOException e) {
                                                                e = e;
                                                                android.util.Log.e(TAG, "Error backing up account access grants", e);
                                                                return null;
                                                            }
                                                        } else {
                                                            allAccountGrants = allAccountGrants2;
                                                            i = length;
                                                        }
                                                    } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                                                        allAccountGrants = allAccountGrants2;
                                                        i = length;
                                                        android.util.Slog.i(TAG, "Skipping backup of account access grant for non-existing package: " + packageName);
                                                    }
                                                    i3++;
                                                    i2 = userId;
                                                    accounts = accounts2;
                                                    allAccountGrants2 = allAccountGrants;
                                                    length = i;
                                                } catch (java.io.IOException e3) {
                                                    e = e3;
                                                    android.util.Log.e(TAG, "Error backing up account access grants", e);
                                                    return null;
                                                }
                                            }
                                            i2 = userId;
                                        }
                                    }
                                    serializer.endTag((java.lang.String) null, TAG_PERMISSIONS);
                                    serializer.endDocument();
                                    serializer.flush();
                                    return dataStream.toByteArray();
                                } catch (java.io.IOException e4) {
                                    e = e4;
                                }
                            } else {
                                try {
                                    try {
                                        return null;
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                        throw th;
                                    }
                                } catch (java.lang.Throwable th3) {
                                    e = th3;
                                }
                            }
                        } catch (java.lang.Throwable th4) {
                            e = th4;
                        }
                        throw e;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [com.android.server.accounts.AccountManagerBackupHelper$RestorePackageMonitor-IA, java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v2 */
    /* JADX WARN: Type inference failed for: r13v3 */
    public void restoreAccountAccessPermissions(byte[] data, int userId) {
        try {
            try {
                java.io.ByteArrayInputStream dataStream = new java.io.ByteArrayInputStream(data);
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newFastPullParser();
                parser.setInput(dataStream, java.nio.charset.StandardCharsets.UTF_8.name());
                android.content.pm.PackageManager packageManager = this.mAccountManagerService.mContext.getPackageManager();
                int permissionsOuterDepth = parser.getDepth();
                while (true) {
                    ?? r13 = 0;
                    if (!com.android.internal.util.XmlUtils.nextElementWithin(parser, permissionsOuterDepth)) {
                        this.mRestoreCancelCommand = new com.android.server.accounts.AccountManagerBackupHelper.CancelRestoreCommand();
                        this.mAccountManagerService.mHandler.postDelayed(this.mRestoreCancelCommand, 3600000L);
                        return;
                    }
                    if (TAG_PERMISSIONS.equals(parser.getName())) {
                        int permissionOuterDepth = parser.getDepth();
                        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, permissionOuterDepth)) {
                            if ("permission".equals(parser.getName())) {
                                java.lang.String accountDigest = parser.getAttributeValue((java.lang.String) r13, ATTR_ACCOUNT_SHA_256);
                                if (android.text.TextUtils.isEmpty(accountDigest)) {
                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                }
                                java.lang.String packageName = parser.getAttributeValue((java.lang.String) r13, "package");
                                if (android.text.TextUtils.isEmpty(packageName)) {
                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                }
                                java.lang.String digest = parser.getAttributeValue((java.lang.String) r13, ATTR_DIGEST);
                                if (android.text.TextUtils.isEmpty(digest)) {
                                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                }
                                com.android.server.accounts.AccountManagerBackupHelper.PendingAppPermission pendingAppPermission = new com.android.server.accounts.AccountManagerBackupHelper.PendingAppPermission(accountDigest, packageName, digest, userId);
                                if (!pendingAppPermission.apply(packageManager)) {
                                    synchronized (this.mLock) {
                                        if (this.mRestorePackageMonitor == null) {
                                            this.mRestorePackageMonitor = new com.android.server.accounts.AccountManagerBackupHelper.RestorePackageMonitor();
                                            this.mRestorePackageMonitor.register(this.mAccountManagerService.mContext, this.mAccountManagerService.mHandler.getLooper(), true);
                                        }
                                        if (this.mRestorePendingAppPermissions == null) {
                                            this.mRestorePendingAppPermissions = new java.util.ArrayList();
                                        }
                                        this.mRestorePendingAppPermissions.add(pendingAppPermission);
                                    }
                                }
                                r13 = 0;
                            }
                        }
                    }
                }
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                e = e;
                android.util.Log.e(TAG, "Error restoring app permissions", e);
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e2) {
            e = e2;
        }
    }

    private final class RestorePackageMonitor extends com.android.internal.content.PackageMonitor {
        private RestorePackageMonitor() {
        }

        public void onPackageAdded(java.lang.String packageName, int uid) {
            synchronized (com.android.server.accounts.AccountManagerBackupHelper.this.mLock) {
                if (com.android.server.accounts.AccountManagerBackupHelper.this.mRestorePendingAppPermissions == null) {
                    return;
                }
                if (android.os.UserHandle.getUserId(uid) != 0) {
                    return;
                }
                int count = com.android.server.accounts.AccountManagerBackupHelper.this.mRestorePendingAppPermissions.size();
                for (int i = count - 1; i >= 0; i--) {
                    com.android.server.accounts.AccountManagerBackupHelper.PendingAppPermission pendingAppPermission = (com.android.server.accounts.AccountManagerBackupHelper.PendingAppPermission) com.android.server.accounts.AccountManagerBackupHelper.this.mRestorePendingAppPermissions.get(i);
                    if (pendingAppPermission.packageName.equals(packageName) && pendingAppPermission.apply(com.android.server.accounts.AccountManagerBackupHelper.this.mAccountManagerService.mContext.getPackageManager())) {
                        com.android.server.accounts.AccountManagerBackupHelper.this.mRestorePendingAppPermissions.remove(i);
                    }
                }
                if (com.android.server.accounts.AccountManagerBackupHelper.this.mRestorePendingAppPermissions.isEmpty() && com.android.server.accounts.AccountManagerBackupHelper.this.mRestoreCancelCommand != null) {
                    com.android.server.accounts.AccountManagerBackupHelper.this.mAccountManagerService.mHandler.removeCallbacks(com.android.server.accounts.AccountManagerBackupHelper.this.mRestoreCancelCommand);
                    com.android.server.accounts.AccountManagerBackupHelper.this.mRestoreCancelCommand.run();
                    com.android.server.accounts.AccountManagerBackupHelper.this.mRestoreCancelCommand = null;
                }
            }
        }
    }

    private final class CancelRestoreCommand implements java.lang.Runnable {
        private CancelRestoreCommand() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.accounts.AccountManagerBackupHelper.this.mLock) {
                com.android.server.accounts.AccountManagerBackupHelper.this.mRestorePendingAppPermissions = null;
                if (com.android.server.accounts.AccountManagerBackupHelper.this.mRestorePackageMonitor != null) {
                    com.android.server.accounts.AccountManagerBackupHelper.this.mRestorePackageMonitor.unregister();
                    com.android.server.accounts.AccountManagerBackupHelper.this.mRestorePackageMonitor = null;
                }
            }
        }
    }
}
