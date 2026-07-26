package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ApkChecksums {
    static final java.lang.String ALGO_MD5 = "MD5";
    static final java.lang.String ALGO_SHA1 = "SHA1";
    static final java.lang.String ALGO_SHA256 = "SHA256";
    static final java.lang.String ALGO_SHA512 = "SHA512";
    private static final java.lang.String DIGESTS_FILE_EXTENSION = ".digests";
    private static final java.lang.String DIGESTS_SIGNATURE_FILE_EXTENSION = ".signature";
    private static final java.security.cert.Certificate[] EMPTY_CERTIFICATE_ARRAY = new java.security.cert.Certificate[0];
    static final int MAX_BUFFER_SIZE = 131072;
    private static final int MAX_SIGNATURE_SIZE_BYTES = 35840;
    static final int MIN_BUFFER_SIZE = 4096;
    private static final long PROCESS_REQUIRED_CHECKSUMS_DELAY_MILLIS = 1000;
    private static final long PROCESS_REQUIRED_CHECKSUMS_TIMEOUT_MILLIS = 86400000;
    static final java.lang.String TAG = "ApkChecksums";

    static class Injector {
        private final com.android.server.pm.ApkChecksums.Injector.Producer<android.content.Context> mContext;
        private final com.android.server.pm.ApkChecksums.Injector.Producer<android.os.Handler> mHandlerProducer;
        private final com.android.server.pm.ApkChecksums.Injector.Producer<android.os.incremental.IncrementalManager> mIncrementalManagerProducer;
        private final com.android.server.pm.ApkChecksums.Injector.Producer<android.content.pm.PackageManagerInternal> mPackageManagerInternalProducer;

        interface Producer<T> {
            T produce();
        }

        Injector(com.android.server.pm.ApkChecksums.Injector.Producer<android.content.Context> context, com.android.server.pm.ApkChecksums.Injector.Producer<android.os.Handler> handlerProducer, com.android.server.pm.ApkChecksums.Injector.Producer<android.os.incremental.IncrementalManager> incrementalManagerProducer, com.android.server.pm.ApkChecksums.Injector.Producer<android.content.pm.PackageManagerInternal> packageManagerInternalProducer) {
            this.mContext = context;
            this.mHandlerProducer = handlerProducer;
            this.mIncrementalManagerProducer = incrementalManagerProducer;
            this.mPackageManagerInternalProducer = packageManagerInternalProducer;
        }

        public android.content.Context getContext() {
            return this.mContext.produce();
        }

        public android.os.Handler getHandler() {
            return this.mHandlerProducer.produce();
        }

        public android.os.incremental.IncrementalManager getIncrementalManager() {
            return this.mIncrementalManagerProducer.produce();
        }

        public android.content.pm.PackageManagerInternal getPackageManagerInternal() {
            return this.mPackageManagerInternalProducer.produce();
        }
    }

    public static java.lang.String buildDigestsPathForApk(java.lang.String codePath) {
        if (!android.content.pm.parsing.ApkLiteParseUtils.isApkPath(codePath)) {
            throw new java.lang.IllegalStateException("Code path is not an apk " + codePath);
        }
        return codePath.substring(0, codePath.length() - ".apk".length()) + DIGESTS_FILE_EXTENSION;
    }

    public static java.lang.String buildSignaturePathForDigests(java.lang.String digestsPath) {
        return digestsPath + DIGESTS_SIGNATURE_FILE_EXTENSION;
    }

    public static boolean isDigestOrDigestSignatureFile(java.io.File file) {
        java.lang.String name = file.getName();
        return name.endsWith(DIGESTS_FILE_EXTENSION) || name.endsWith(DIGESTS_SIGNATURE_FILE_EXTENSION);
    }

    public static java.io.File findDigestsForFile(java.io.File targetFile) {
        java.lang.String digestsPath = buildDigestsPathForApk(targetFile.getAbsolutePath());
        java.io.File digestsFile = new java.io.File(digestsPath);
        if (digestsFile.exists()) {
            return digestsFile;
        }
        return null;
    }

    public static java.io.File findSignatureForDigests(java.io.File digestsFile) {
        java.lang.String signaturePath = buildSignaturePathForDigests(digestsFile.getAbsolutePath());
        java.io.File signatureFile = new java.io.File(signaturePath);
        if (signatureFile.exists()) {
            return signatureFile;
        }
        return null;
    }

    public static void writeChecksums(java.io.OutputStream os, android.content.pm.Checksum[] checksums) throws java.io.IOException {
        java.io.DataOutputStream dos = new java.io.DataOutputStream(os);
        try {
            for (android.content.pm.Checksum checksum : checksums) {
                android.content.pm.Checksum.writeToStream(dos, checksum);
            }
            dos.close();
        } catch (java.lang.Throwable th) {
            try {
                dos.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static android.content.pm.Checksum[] readChecksums(java.io.File file) throws java.io.IOException {
        java.io.InputStream is = new java.io.FileInputStream(file);
        try {
            android.content.pm.Checksum[] checksums = readChecksums(is);
            is.close();
            return checksums;
        } catch (java.lang.Throwable th) {
            try {
                is.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static android.content.pm.Checksum[] readChecksums(java.io.InputStream is) throws java.io.IOException {
        java.io.DataInputStream dis = new java.io.DataInputStream(is);
        try {
            java.util.ArrayList<android.content.pm.Checksum> checksums = new java.util.ArrayList<>();
            for (int i = 0; i < 100; i++) {
                try {
                    checksums.add(android.content.pm.Checksum.readFromStream(dis));
                } catch (java.io.EOFException e) {
                }
            }
            android.content.pm.Checksum[] checksumArr = (android.content.pm.Checksum[]) checksums.toArray(new android.content.pm.Checksum[checksums.size()]);
            dis.close();
            return checksumArr;
        } catch (java.lang.Throwable th) {
            try {
                dis.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static java.security.cert.Certificate[] verifySignature(android.content.pm.Checksum[] checksums, byte[] signature) throws java.security.SignatureException, java.security.NoSuchAlgorithmException, java.io.IOException {
        if (signature == null || signature.length > MAX_SIGNATURE_SIZE_BYTES) {
            throw new java.security.SignatureException("Invalid signature");
        }
        java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
        try {
            writeChecksums(os, checksums);
            byte[] blob = os.toByteArray();
            os.close();
            sun.security.pkcs.PKCS7 pkcs7 = new sun.security.pkcs.PKCS7(signature);
            java.security.cert.Certificate[] certs = pkcs7.getCertificates();
            if (certs == null || certs.length == 0) {
                throw new java.security.SignatureException("Signature missing certificates");
            }
            sun.security.pkcs.SignerInfo[] signerInfos = pkcs7.verify(blob);
            if (signerInfos == null || signerInfos.length == 0) {
                throw new java.security.SignatureException("Verification failed");
            }
            java.util.ArrayList<java.security.cert.Certificate> certificates = new java.util.ArrayList<>(signerInfos.length);
            for (sun.security.pkcs.SignerInfo signerInfo : signerInfos) {
                java.util.ArrayList<java.security.cert.X509Certificate> chain = signerInfo.getCertificateChain(pkcs7);
                if (chain == null) {
                    throw new java.security.SignatureException("Verification passed, but certification chain is empty.");
                }
                certificates.addAll(chain);
            }
            return (java.security.cert.Certificate[]) certificates.toArray(new java.security.cert.Certificate[certificates.size()]);
        } catch (java.lang.Throwable th) {
            try {
                os.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static void getChecksums(java.util.List<android.util.Pair<java.lang.String, java.io.File>> filesToChecksum, int optional, int required, java.lang.String installerPackageName, java.security.cert.Certificate[] trustedInstallers, android.content.pm.IOnChecksumsReadyListener onChecksumsReadyListener, com.android.server.pm.ApkChecksums.Injector injector) {
        android.os.Trace.traceBegin(262144L, "PackageManagerBg getChecksums");
        java.util.List<java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum>> result = new java.util.ArrayList<>(filesToChecksum.size());
        int size = filesToChecksum.size();
        for (int i = 0; i < size; i++) {
            java.lang.String split = (java.lang.String) filesToChecksum.get(i).first;
            java.io.File file = (java.io.File) filesToChecksum.get(i).second;
            java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> checksums = new android.util.ArrayMap<>();
            result.add(checksums);
            try {
                getAvailableApkChecksums(split, file, optional | required, installerPackageName, trustedInstallers, checksums, injector);
            } catch (java.lang.Throwable e) {
                android.util.Slog.e(TAG, "Preferred checksum calculation error", e);
            }
        }
        long startTime = android.os.SystemClock.uptimeMillis();
        processRequiredChecksums(filesToChecksum, result, required, onChecksumsReadyListener, injector, startTime);
        android.os.Trace.traceEnd(262144L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1 */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r13v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v6 */
    /* JADX WARN: Type inference failed for: r13v7 */
    /* JADX WARN: Type inference failed for: r13v8 */
    /* JADX WARN: Type inference failed for: r18v1 */
    /* JADX WARN: Type inference failed for: r18v10 */
    /* JADX WARN: Type inference failed for: r18v2 */
    /* JADX WARN: Type inference failed for: r18v3 */
    /* JADX WARN: Type inference failed for: r18v4 */
    /* JADX WARN: Type inference failed for: r18v5 */
    /* JADX WARN: Type inference failed for: r18v6 */
    /* JADX WARN: Type inference failed for: r18v7 */
    /* JADX WARN: Type inference failed for: r18v8 */
    /* JADX WARN: Type inference failed for: r18v9 */
    public static void processRequiredChecksums(final java.util.List<android.util.Pair<java.lang.String, java.io.File>> list, final java.util.List<java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum>> list2, final int i, final android.content.pm.IOnChecksumsReadyListener iOnChecksumsReadyListener, final com.android.server.pm.ApkChecksums.Injector injector, final long j) {
        int i2;
        ?? r18;
        java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> map;
        java.util.List<android.util.Pair<java.lang.String, java.io.File>> list3 = list;
        android.os.Trace.traceBegin(262144L, "PackageManagerBg processRequiredChecksums");
        ?? r13 = android.os.SystemClock.uptimeMillis() - j >= 86400000;
        java.util.ArrayList arrayList = new java.util.ArrayList();
        int size = list.size();
        int i3 = 0;
        while (i3 < size) {
            java.lang.String str = (java.lang.String) list3.get(i3).first;
            java.io.File file = (java.io.File) list3.get(i3).second;
            java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> map2 = list2.get(i3);
            if (r13 == 0 || i != 0) {
                try {
                    if (needToWait(file, i, map2, injector)) {
                        android.os.Handler handler = injector.getHandler();
                        i2 = i3;
                        r18 = r13;
                        r13 = TAG;
                        handler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.pm.ApkChecksums$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                com.android.server.pm.ApkChecksums.processRequiredChecksums(list, list2, i, iOnChecksumsReadyListener, injector, j);
                            }
                        }, 1000L);
                        return;
                    }
                    try {
                        map = map2;
                        i2 = i3;
                        r18 = r13;
                        getRequiredApkChecksums(str, file, i, map);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.util.Slog.e((java.lang.String) r13, "Required checksum calculation error", th);
                    }
                    th = th;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    i2 = i3;
                    r18 = r13;
                    r13 = TAG;
                }
                android.util.Slog.e((java.lang.String) r13, "Required checksum calculation error", th);
                i3 = i2 + 1;
                list3 = list;
                r13 = r18 == true ? 1 : 0;
            } else {
                map = map2;
                i2 = i3;
                r18 = r13;
            }
            arrayList.addAll(map.values());
            i3 = i2 + 1;
            list3 = list;
            r13 = r18 == true ? 1 : 0;
        }
        try {
            iOnChecksumsReadyListener.onChecksumsReady(arrayList);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, e);
        }
        android.os.Trace.traceEnd(262144L);
    }

    private static void getAvailableApkChecksums(java.lang.String split, java.io.File file, int types, java.lang.String installerPackageName, java.security.cert.Certificate[] trustedInstallers, java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> checksums, com.android.server.pm.ApkChecksums.Injector injector) {
        java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> v2v3checksums;
        android.content.pm.ApkChecksum checksum;
        if (!file.exists()) {
            return;
        }
        java.lang.String filePath = file.getAbsolutePath();
        if (isRequired(1, types, checksums) && (checksum = extractHashFromFS(split, filePath)) != null) {
            checksums.put(java.lang.Integer.valueOf(checksum.getType()), checksum);
        }
        if ((isRequired(32, types, checksums) || isRequired(64, types, checksums)) && (v2v3checksums = extractHashFromV2V3Signature(split, filePath, types)) != null) {
            checksums.putAll(v2v3checksums);
        }
        getInstallerChecksums(split, file, types, installerPackageName, trustedInstallers, checksums, injector);
    }

    private static void getInstallerChecksums(java.lang.String split, java.io.File file, int types, java.lang.String installerPackageName, java.security.cert.Certificate[] trustedInstallers, java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> checksums, com.android.server.pm.ApkChecksums.Injector injector) {
        java.io.File digestsFile;
        android.content.pm.Signature[] certs;
        android.content.pm.Signature[] pastCerts;
        int i;
        if (com.android.server.pm.PackageManagerServiceUtils.isInstalledByAdb(installerPackageName)) {
            return;
        }
        if ((trustedInstallers != null && trustedInstallers.length == 0) || (digestsFile = findDigestsForFile(file)) == null) {
            return;
        }
        java.io.File signatureFile = findSignatureForDigests(digestsFile);
        try {
            android.content.pm.Checksum[] digests = readChecksums(digestsFile);
            if (signatureFile == null) {
                com.android.server.pm.pkg.AndroidPackage installer = injector.getPackageManagerInternal().getPackage(installerPackageName);
                if (installer == null) {
                    android.util.Slog.e(TAG, "Installer package not found.");
                    return;
                } else {
                    certs = installer.getSigningDetails().getSignatures();
                    pastCerts = installer.getSigningDetails().getPastSigningCertificates();
                }
            } else {
                java.security.cert.Certificate[] certificates = verifySignature(digests, java.nio.file.Files.readAllBytes(signatureFile.toPath()));
                if (certificates != null && certificates.length != 0) {
                    certs = new android.content.pm.Signature[certificates.length];
                    int size = certificates.length;
                    for (int i2 = 0; i2 < size; i2++) {
                        certs[i2] = new android.content.pm.Signature(certificates[i2].getEncoded());
                    }
                    pastCerts = null;
                }
                android.util.Slog.e(TAG, "Error validating signature");
                return;
            }
            try {
                if (certs == null || certs.length == 0 || certs[0] == null) {
                    android.util.Slog.e(TAG, "Can't obtain certificates.");
                    return;
                }
                byte[] trustedCertBytes = certs[0].toByteArray();
                java.util.Set<android.content.pm.Signature> trusted = convertToSet(trustedInstallers);
                if (trusted != null && !trusted.isEmpty()) {
                    android.content.pm.Signature trustedCert = isTrusted(certs, trusted);
                    if (trustedCert == null) {
                        trustedCert = isTrusted(pastCerts, trusted);
                    }
                    if (trustedCert == null) {
                        return;
                    } else {
                        trustedCertBytes = trustedCert.toByteArray();
                    }
                }
                for (android.content.pm.Checksum digest : digests) {
                    android.content.pm.ApkChecksum system2 = checksums.get(java.lang.Integer.valueOf(digest.getType()));
                    if (system2 != null && !java.util.Arrays.equals(system2.getValue(), digest.getValue())) {
                        throw new java.security.InvalidParameterException("System digest " + digest.getType() + " mismatch, can't bind installer-provided digests to the APK.");
                    }
                }
                int length = digests.length;
                int i3 = 0;
                while (i3 < length) {
                    android.content.pm.Checksum digest2 = digests[i3];
                    try {
                        if (isRequired(digest2.getType(), types, checksums)) {
                            i = length;
                            checksums.put(java.lang.Integer.valueOf(digest2.getType()), new android.content.pm.ApkChecksum(split, digest2, installerPackageName, trustedCertBytes));
                        } else {
                            i = length;
                        }
                        i3++;
                        length = i;
                    } catch (java.io.IOException e) {
                        e = e;
                        android.util.Slog.e(TAG, "Error reading .digests or .signature", e);
                        return;
                    } catch (java.security.InvalidParameterException | java.security.NoSuchAlgorithmException | java.security.SignatureException e2) {
                        e = e2;
                        android.util.Slog.e(TAG, "Error validating digests. Invalid digests will be removed", e);
                        try {
                            java.nio.file.Files.deleteIfExists(digestsFile.toPath());
                            if (signatureFile != null) {
                                java.nio.file.Files.deleteIfExists(signatureFile.toPath());
                                return;
                            }
                            return;
                        } catch (java.io.IOException e3) {
                            return;
                        }
                    } catch (java.security.cert.CertificateEncodingException e4) {
                        e = e4;
                        android.util.Slog.e(TAG, "Error encoding trustedInstallers", e);
                        return;
                    }
                }
            } catch (java.io.IOException e5) {
                e = e5;
            } catch (java.security.InvalidParameterException | java.security.NoSuchAlgorithmException | java.security.SignatureException e6) {
                e = e6;
            } catch (java.security.cert.CertificateEncodingException e7) {
                e = e7;
            }
        } catch (java.io.IOException e8) {
            e = e8;
        } catch (java.security.InvalidParameterException | java.security.NoSuchAlgorithmException | java.security.SignatureException e9) {
            e = e9;
        } catch (java.security.cert.CertificateEncodingException e10) {
            e = e10;
        }
    }

    private static boolean needToWait(java.io.File file, int types, java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> checksums, com.android.server.pm.ApkChecksums.Injector injector) throws java.io.IOException {
        if (!isRequired(1, types, checksums) && !isRequired(2, types, checksums) && !isRequired(4, types, checksums) && !isRequired(8, types, checksums) && !isRequired(16, types, checksums) && !isRequired(32, types, checksums) && !isRequired(64, types, checksums)) {
            return false;
        }
        java.lang.String filePath = file.getAbsolutePath();
        if (!android.os.incremental.IncrementalManager.isIncrementalPath(filePath)) {
            return false;
        }
        android.os.incremental.IncrementalManager manager = injector.getIncrementalManager();
        if (manager == null) {
            android.util.Slog.e(TAG, "IncrementalManager is missing.");
            return false;
        }
        android.os.incremental.IncrementalStorage storage = manager.openStorage(filePath);
        if (storage != null) {
            return true ^ storage.isFileFullyLoaded(filePath);
        }
        android.util.Slog.e(TAG, "IncrementalStorage is missing for a path on IncFs: " + filePath);
        return false;
    }

    private static void getRequiredApkChecksums(java.lang.String split, java.io.File file, int types, java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> checksums) {
        java.lang.String filePath = file.getAbsolutePath();
        if (isRequired(1, types, checksums)) {
            try {
                byte[] generatedRootHash = android.util.apk.VerityBuilder.generateFsVerityRootHash(filePath, (byte[]) null, new android.util.apk.ByteBufferFactory() { // from class: com.android.server.pm.ApkChecksums.1
                    public java.nio.ByteBuffer create(int capacity) {
                        return java.nio.ByteBuffer.allocate(capacity);
                    }
                });
                checksums.put(1, new android.content.pm.ApkChecksum(split, 1, verityHashForFile(file, generatedRootHash)));
            } catch (java.io.IOException | java.security.DigestException | java.security.NoSuchAlgorithmException e) {
                android.util.Slog.e(TAG, "Error calculating WHOLE_MERKLE_ROOT_4K_SHA256", e);
            }
        }
        calculateChecksumIfRequested(checksums, split, file, types, 2);
        calculateChecksumIfRequested(checksums, split, file, types, 4);
        calculateChecksumIfRequested(checksums, split, file, types, 8);
        calculateChecksumIfRequested(checksums, split, file, types, 16);
        calculatePartialChecksumsIfRequested(checksums, split, file, types);
    }

    private static boolean isRequired(int type, int types, java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> checksums) {
        return ((types & type) == 0 || checksums.containsKey(java.lang.Integer.valueOf(type))) ? false : true;
    }

    private static java.util.Set<android.content.pm.Signature> convertToSet(java.security.cert.Certificate[] array) throws java.security.cert.CertificateEncodingException {
        if (array == null) {
            return null;
        }
        java.util.Set<android.content.pm.Signature> set = new android.util.ArraySet<>(array.length);
        for (java.security.cert.Certificate item : array) {
            set.add(new android.content.pm.Signature(item.getEncoded()));
        }
        return set;
    }

    private static android.content.pm.Signature isTrusted(android.content.pm.Signature[] signatures, java.util.Set<android.content.pm.Signature> trusted) {
        if (signatures == null) {
            return null;
        }
        for (android.content.pm.Signature signature : signatures) {
            if (trusted.contains(signature)) {
                return signature;
            }
        }
        return null;
    }

    private static android.content.pm.ApkChecksum extractHashFromFS(java.lang.String split, java.lang.String filePath) {
        byte[] verityHash;
        if (com.android.internal.security.VerityUtils.hasFsverity(filePath) && (verityHash = com.android.internal.security.VerityUtils.getFsverityDigest(filePath)) != null) {
            return new android.content.pm.ApkChecksum(split, 1, verityHash);
        }
        try {
            android.util.apk.ApkSignatureSchemeV4Verifier.VerifiedSigner signer = android.util.apk.ApkSignatureSchemeV4Verifier.extractCertificates(filePath);
            byte[] rootHash = (byte[]) signer.contentDigests.getOrDefault(3, null);
            if (rootHash != null) {
                return new android.content.pm.ApkChecksum(split, 1, verityHashForFile(new java.io.File(filePath), rootHash));
            }
        } catch (java.lang.SecurityException | java.security.SignatureException e) {
            android.util.Slog.e(TAG, "V4 signature error", e);
        } catch (android.util.apk.SignatureNotFoundException e2) {
        }
        return null;
    }

    static byte[] verityHashForFile(java.io.File file, byte[] rootHash) {
        try {
            java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(256);
            buffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.put((byte) 1);
            buffer.put((byte) 1);
            buffer.put((byte) 12);
            buffer.put((byte) 0);
            buffer.putInt(0);
            buffer.putLong(file.length());
            buffer.put(rootHash);
            for (int i = 0; i < 208; i++) {
                buffer.put((byte) 0);
            }
            buffer.flip();
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(ALGO_SHA256);
            md.update(buffer);
            return md.digest();
        } catch (java.security.NoSuchAlgorithmException e) {
            android.util.Slog.e(TAG, "Device does not support MessageDigest algorithm", e);
            return null;
        }
    }

    private static java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> extractHashFromV2V3Signature(java.lang.String split, java.lang.String filePath, int types) {
        byte[] hash;
        byte[] hash2;
        java.util.Map<java.lang.Integer, byte[]> contentDigests = null;
        android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
        android.content.pm.parsing.result.ParseResult<android.util.apk.ApkSignatureVerifier.SigningDetailsWithDigests> result = android.util.apk.ApkSignatureVerifier.verifySignaturesInternal(input, filePath, 2, false);
        if (result.isError()) {
            if (!(result.getException() instanceof android.util.apk.SignatureNotFoundException)) {
                android.util.Slog.e(TAG, "Signature verification error", result.getException());
            }
        } else {
            contentDigests = ((android.util.apk.ApkSignatureVerifier.SigningDetailsWithDigests) result.getResult()).contentDigests;
        }
        if (contentDigests == null) {
            return null;
        }
        java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> checksums = new android.util.ArrayMap<>();
        if ((types & 32) != 0 && (hash2 = contentDigests.getOrDefault(1, null)) != null) {
            checksums.put(32, new android.content.pm.ApkChecksum(split, 32, hash2));
        }
        if ((types & 64) != 0 && (hash = contentDigests.getOrDefault(2, null)) != null) {
            checksums.put(64, new android.content.pm.ApkChecksum(split, 64, hash));
        }
        return checksums;
    }

    private static java.lang.String getMessageDigestAlgoForChecksumKind(int type) throws java.security.NoSuchAlgorithmException {
        switch (type) {
            case 2:
                return ALGO_MD5;
            case 4:
                return ALGO_SHA1;
            case 8:
                return ALGO_SHA256;
            case 16:
                return ALGO_SHA512;
            default:
                throw new java.security.NoSuchAlgorithmException("Invalid checksum type: " + type);
        }
    }

    private static void calculateChecksumIfRequested(java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> checksums, java.lang.String split, java.io.File file, int required, int type) {
        byte[] checksum;
        if ((required & type) != 0 && !checksums.containsKey(java.lang.Integer.valueOf(type)) && (checksum = getApkChecksum(file, type)) != null) {
            checksums.put(java.lang.Integer.valueOf(type), new android.content.pm.ApkChecksum(split, type, checksum));
        }
    }

    private static byte[] getApkChecksum(java.io.File file, int type) {
        java.io.FileInputStream fis;
        byte[] buffer;
        java.security.MessageDigest md;
        int bufferSize = (int) java.lang.Math.max(4096L, java.lang.Math.min(131072L, file.length()));
        try {
            fis = new java.io.FileInputStream(file);
            try {
                buffer = new byte[bufferSize];
                java.lang.String algo = getMessageDigestAlgoForChecksumKind(type);
                md = java.security.MessageDigest.getInstance(algo);
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Error reading " + file.getAbsolutePath() + " to compute hash.", e);
            return null;
        } catch (java.security.NoSuchAlgorithmException e2) {
            android.util.Slog.e(TAG, "Device does not support MessageDigest algorithm", e2);
            return null;
        }
        while (true) {
            int nread = fis.read(buffer);
            if (nread != -1) {
                md.update(buffer, 0, nread);
            } else {
                byte[] bArrDigest = md.digest();
                fis.close();
                return bArrDigest;
            }
            android.util.Slog.e(TAG, "Error reading " + file.getAbsolutePath() + " to compute hash.", e);
            return null;
        }
    }

    private static int[] getContentDigestAlgos(boolean needSignatureSha256, boolean needSignatureSha512) {
        if (needSignatureSha256 && needSignatureSha512) {
            return new int[]{1, 2};
        }
        if (needSignatureSha256) {
            return new int[]{1};
        }
        return new int[]{2};
    }

    private static int getChecksumKindForContentDigestAlgo(int contentDigestAlgo) {
        switch (contentDigestAlgo) {
            case 1:
                return 32;
            case 2:
                return 64;
            default:
                return -1;
        }
    }

    private static void calculatePartialChecksumsIfRequested(java.util.Map<java.lang.Integer, android.content.pm.ApkChecksum> checksums, java.lang.String split, java.io.File file, int required) {
        java.lang.Throwable th;
        boolean needSignatureSha512 = false;
        boolean needSignatureSha256 = ((required & 32) == 0 || checksums.containsKey(32)) ? false : true;
        if ((required & 64) != 0 && !checksums.containsKey(64)) {
            needSignatureSha512 = true;
        }
        if (needSignatureSha256 || needSignatureSha512) {
            try {
                try {
                    java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
                    android.util.apk.SignatureInfo signatureInfo = null;
                    try {
                        try {
                            try {
                                signatureInfo = android.util.apk.ApkSignatureSchemeV3Verifier.findSignature(raf);
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                th = th;
                                try {
                                    raf.close();
                                    throw th;
                                } catch (java.lang.Throwable th3) {
                                    th.addSuppressed(th3);
                                    throw th;
                                }
                            }
                        } catch (android.util.apk.SignatureNotFoundException e) {
                            try {
                                signatureInfo = android.util.apk.ApkSignatureSchemeV2Verifier.findSignature(raf);
                            } catch (android.util.apk.SignatureNotFoundException e2) {
                            }
                        }
                        if (signatureInfo == null) {
                            android.util.Slog.e(TAG, "V2/V3 signatures not found in " + file.getAbsolutePath());
                            raf.close();
                            return;
                        }
                        int[] digestAlgos = getContentDigestAlgos(needSignatureSha256, needSignatureSha512);
                        byte[][] digests = android.util.apk.ApkSigningBlockUtils.computeContentDigestsPer1MbChunk(digestAlgos, raf.getFD(), signatureInfo);
                        int size = digestAlgos.length;
                        for (int i = 0; i < size; i++) {
                            int checksumKind = getChecksumKindForContentDigestAlgo(digestAlgos[i]);
                            if (checksumKind != -1) {
                                try {
                                    checksums.put(java.lang.Integer.valueOf(checksumKind), new android.content.pm.ApkChecksum(split, checksumKind, digests[i]));
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                    th = th;
                                    raf.close();
                                    throw th;
                                }
                            }
                        }
                        raf.close();
                        return;
                    } catch (java.io.IOException | java.security.DigestException e3) {
                        e = e3;
                    }
                } catch (java.io.IOException | java.security.DigestException e4) {
                    e = e4;
                }
            } catch (java.io.IOException | java.security.DigestException e5) {
                e = e5;
            }
            android.util.Slog.e(TAG, "Error computing hash.", e);
        }
    }
}
