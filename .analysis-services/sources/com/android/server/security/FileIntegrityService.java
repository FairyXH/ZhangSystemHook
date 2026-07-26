package com.android.server.security;

/* JADX INFO: loaded from: classes3.dex */
public class FileIntegrityService extends com.android.server.SystemService {
    private static final int MAX_SIGNATURE_FILE_SIZE_BYTES = 8192;
    private static final java.lang.String TAG = "FileIntegrityService";
    private static java.security.cert.CertificateFactory sCertFactory;
    private final android.os.IBinder mService;
    private final java.util.ArrayList<java.security.cert.X509Certificate> mTrustedCertificates;

    public static com.android.server.security.FileIntegrityService getService() {
        return (com.android.server.security.FileIntegrityService) com.android.server.LocalServices.getService(com.android.server.security.FileIntegrityService.class);
    }

    private final class BinderService extends android.security.IFileIntegrityService.Stub {
        BinderService(android.content.Context context) {
            super(android.os.PermissionEnforcer.fromContext(context));
        }

        public boolean isApkVeritySupported() {
            return com.android.internal.security.VerityUtils.isFsVeritySupported();
        }

        public boolean isAppSourceCertificateTrusted(byte[] certificateBytes, java.lang.String packageName) {
            boolean zContains;
            checkCallerPermission(packageName);
            if (android.security.Flags.deprecateFsvSig()) {
                return false;
            }
            try {
                if (!com.android.internal.security.VerityUtils.isFsVeritySupported()) {
                    return false;
                }
                if (certificateBytes == null) {
                    android.util.Slog.w(com.android.server.security.FileIntegrityService.TAG, "Received a null certificate");
                    return false;
                }
                synchronized (com.android.server.security.FileIntegrityService.this.mTrustedCertificates) {
                    zContains = com.android.server.security.FileIntegrityService.this.mTrustedCertificates.contains(com.android.server.security.FileIntegrityService.toCertificate(certificateBytes));
                }
                return zContains;
            } catch (java.security.cert.CertificateException e) {
                android.util.Slog.e(com.android.server.security.FileIntegrityService.TAG, "Failed to convert the certificate: " + e);
                return false;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.security.FileIntegrityService.FileIntegrityServiceShellCommand().exec(this, in, out, err, args, callback, resultReceiver);
        }

        private void checkCallerPackageName(java.lang.String packageName) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            android.content.pm.PackageManagerInternal packageManager = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            int packageUid = packageManager.getPackageUid(packageName, 0L, callingUserId);
            if (callingUid != packageUid) {
                throw new java.lang.SecurityException("Calling uid " + callingUid + " does not own package " + packageName);
            }
        }

        private void checkCallerPermission(java.lang.String packageName) {
            checkCallerPackageName(packageName);
            if (com.android.server.security.FileIntegrityService.this.getContext().checkCallingPermission("android.permission.INSTALL_PACKAGES") == 0) {
                return;
            }
            android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) com.android.server.security.FileIntegrityService.this.getContext().getSystemService(android.app.AppOpsManager.class);
            int mode = appOpsManager.checkOpNoThrow(66, android.os.Binder.getCallingUid(), packageName);
            if (mode != 0) {
                throw new java.lang.SecurityException("Caller should have INSTALL_PACKAGES or REQUEST_INSTALL_PACKAGES");
            }
        }

        public android.os.IInstalld.IFsveritySetupAuthToken createAuthToken(android.os.ParcelFileDescriptor authFd) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(authFd);
            try {
                android.os.IInstalld.IFsveritySetupAuthToken authToken = com.android.server.security.FileIntegrityService.this.getStorageManagerInternal().createFsveritySetupAuthToken(authFd, android.os.Binder.getCallingUid());
                authFd.close();
                return authToken;
            } catch (java.io.IOException e) {
                throw new android.os.RemoteException(e);
            }
        }

        public int setupFsverity(android.os.IInstalld.IFsveritySetupAuthToken authToken, java.lang.String filePath, java.lang.String packageName) throws android.os.RemoteException {
            setupFsverity_enforcePermission();
            java.util.Objects.requireNonNull(authToken);
            java.util.Objects.requireNonNull(filePath);
            java.util.Objects.requireNonNull(packageName);
            checkCallerPackageName(packageName);
            try {
                return com.android.server.security.FileIntegrityService.this.getStorageManagerInternal().enableFsverity(authToken, filePath, packageName);
            } catch (java.io.IOException e) {
                throw new android.os.RemoteException(e);
            }
        }
    }

    public FileIntegrityService(android.content.Context context) {
        super(context);
        this.mTrustedCertificates = new java.util.ArrayList<>();
        this.mService = new com.android.server.security.FileIntegrityService.BinderService(context);
        try {
            sCertFactory = java.security.cert.CertificateFactory.getInstance("X.509");
        } catch (java.security.cert.CertificateException e) {
            android.util.Slog.wtf(TAG, "Cannot get an instance of X.509 certificate factory");
        }
        com.android.server.LocalServices.addService(com.android.server.security.FileIntegrityService.class, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.storage.StorageManagerInternal getStorageManagerInternal() {
        return (android.os.storage.StorageManagerInternal) com.android.server.LocalServices.getService(android.os.storage.StorageManagerInternal.class);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        loadAllCertificates();
        publishBinderService("file_integrity", this.mService);
    }

    public boolean verifyPkcs7DetachedSignature(java.lang.String signaturePath, java.lang.String filePath) throws java.io.IOException {
        byte[] derEncoded;
        if (java.nio.file.Files.size(java.nio.file.Paths.get(signaturePath, new java.lang.String[0])) > 8192) {
            throw new java.lang.SecurityException("Signature file is unexpectedly large: " + signaturePath);
        }
        byte[] signatureBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(signaturePath, new java.lang.String[0]));
        byte[] digest = com.android.internal.security.VerityUtils.getFsverityDigest(filePath);
        synchronized (this.mTrustedCertificates) {
            for (java.security.cert.X509Certificate cert : this.mTrustedCertificates) {
                try {
                    derEncoded = cert.getEncoded();
                } catch (java.security.cert.CertificateEncodingException e) {
                    android.util.Slog.w(TAG, "Ignoring ill-formed certificate: " + e);
                }
                if (com.android.internal.security.VerityUtils.verifyPkcs7DetachedSignature(signatureBytes, digest, new java.io.ByteArrayInputStream(derEncoded))) {
                    return true;
                }
            }
            return false;
        }
    }

    private void loadAllCertificates() {
        loadCertificatesFromDirectory(android.os.Environment.getRootDirectory().toPath().resolve("etc/security/fsverity"));
        loadCertificatesFromDirectory(android.os.Environment.getProductDirectory().toPath().resolve("etc/security/fsverity"));
    }

    private void loadCertificatesFromDirectory(java.nio.file.Path path) {
        try {
            java.io.File[] files = path.toFile().listFiles();
            if (files == null) {
                return;
            }
            for (java.io.File cert : files) {
                byte[] certificateBytes = java.nio.file.Files.readAllBytes(cert.toPath());
                collectCertificate(certificateBytes);
            }
        } catch (java.io.IOException e) {
            android.util.Slog.wtf(TAG, "Failed to load fs-verity certificate from " + path, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void collectCertificate(byte[] bytes) {
        try {
            synchronized (this.mTrustedCertificates) {
                this.mTrustedCertificates.add(toCertificate(bytes));
            }
        } catch (java.security.cert.CertificateException e) {
            android.util.Slog.e(TAG, "Invalid certificate, ignored: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.security.cert.X509Certificate toCertificate(byte[] bytes) throws java.security.cert.CertificateException {
        java.security.cert.Certificate certificate = sCertFactory.generateCertificate(new java.io.ByteArrayInputStream(bytes));
        if (!(certificate instanceof java.security.cert.X509Certificate)) {
            throw new java.security.cert.CertificateException("Expected to contain an X.509 certificate");
        }
        return (java.security.cert.X509Certificate) certificate;
    }

    private class FileIntegrityServiceShellCommand extends android.os.ShellCommand {
        private FileIntegrityServiceShellCommand() {
        }

        public int onCommand(java.lang.String cmd) {
            byte b;
            if (!android.os.Build.IS_DEBUGGABLE) {
                return -1;
            }
            if (cmd == null) {
                return handleDefaultCommands(cmd);
            }
            java.io.PrintWriter pw = getOutPrintWriter();
            switch (cmd.hashCode()) {
                case -1932837641:
                    b = !cmd.equals("append-cert") ? (byte) -1 : (byte) 0;
                    break;
                case 755125490:
                    b = !cmd.equals("remove-last-cert") ? (byte) -1 : (byte) 1;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    java.lang.String nextArg = getNextArg();
                    if (nextArg == null) {
                        pw.println("Invalid argument");
                        pw.println("");
                        onHelp();
                        return -1;
                    }
                    android.os.ParcelFileDescriptor pfd = openFileForSystem(nextArg, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
                    if (pfd == null) {
                        pw.println("Cannot open the file");
                        return -1;
                    }
                    java.io.InputStream is = new android.os.ParcelFileDescriptor.AutoCloseInputStream(pfd);
                    try {
                        com.android.server.security.FileIntegrityService.this.collectCertificate(is.readAllBytes());
                        pw.println("Certificate is added successfully");
                        return 0;
                    } catch (java.io.IOException e) {
                        pw.println("Failed to add certificate: " + e);
                        return -1;
                    }
                case 1:
                    synchronized (com.android.server.security.FileIntegrityService.this.mTrustedCertificates) {
                        if (com.android.server.security.FileIntegrityService.this.mTrustedCertificates.size() == 0) {
                            pw.println("Certificate list is already empty");
                            return -1;
                        }
                        com.android.server.security.FileIntegrityService.this.mTrustedCertificates.remove(com.android.server.security.FileIntegrityService.this.mTrustedCertificates.size() - 1);
                        pw.println("Certificate is removed successfully");
                        return 0;
                    }
                default:
                    pw.println("Unknown action");
                    pw.println("");
                    onHelp();
                    return -1;
            }
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("File integrity service commands:");
            pw.println("  help");
            pw.println("    Print this help text.");
            pw.println("  append-cert path/to/cert.der");
            pw.println("    Add the DER-encoded certificate (only in debug builds)");
            pw.println("  remove-last-cert");
            pw.println("    Remove the last certificate in the key list (only in debug builds)");
            pw.println("");
        }
    }
}
