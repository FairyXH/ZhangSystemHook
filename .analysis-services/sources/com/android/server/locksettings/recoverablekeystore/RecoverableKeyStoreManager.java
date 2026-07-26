package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class RecoverableKeyStoreManager {
    private static final int INVALID_REMOTE_GUESS_LIMIT = 5;
    private static final long SYNC_DELAY_MILLIS = 2000;
    private static final java.lang.String TAG = "RecoverableKeyStoreMgr";
    private static com.android.server.locksettings.recoverablekeystore.RecoverableKeyStoreManager mInstance;
    private final com.android.server.locksettings.recoverablekeystore.storage.ApplicationKeyStorage mApplicationKeyStorage;
    private final com.android.server.locksettings.recoverablekeystore.storage.CleanupManager mCleanupManager;
    private final android.content.Context mContext;
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb mDatabase;
    private final java.util.concurrent.ScheduledExecutorService mExecutorService;
    private final com.android.server.locksettings.recoverablekeystore.RecoverySnapshotListenersStorage mListenersStorage;
    private final com.android.server.locksettings.recoverablekeystore.PlatformKeyManager mPlatformKeyManager;
    private final com.android.server.locksettings.recoverablekeystore.RecoverableKeyGenerator mRecoverableKeyGenerator;
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage mRecoverySessionStorage;
    private final com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage mRemoteLockscreenValidationSessionStorage;
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage mSnapshotStorage;
    private final com.android.server.locksettings.recoverablekeystore.TestOnlyInsecureCertificateHelper mTestCertHelper;

    public static synchronized com.android.server.locksettings.recoverablekeystore.RecoverableKeyStoreManager getInstance(android.content.Context context) {
        com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage lockscreenCheckSessions;
        if (mInstance == null) {
            com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb db = com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb.newInstance(context);
            if (android.util.FeatureFlagUtils.isEnabled(context, "settings_enable_lockscreen_transfer_api")) {
                lockscreenCheckSessions = new com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage();
            } else {
                lockscreenCheckSessions = null;
            }
            try {
                com.android.server.locksettings.recoverablekeystore.PlatformKeyManager platformKeyManager = com.android.server.locksettings.recoverablekeystore.PlatformKeyManager.getInstance(context, db);
                com.android.server.locksettings.recoverablekeystore.storage.ApplicationKeyStorage applicationKeyStorage = com.android.server.locksettings.recoverablekeystore.storage.ApplicationKeyStorage.getInstance();
                com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage snapshotStorage = com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage.newInstance();
                com.android.server.locksettings.recoverablekeystore.storage.CleanupManager cleanupManager = com.android.server.locksettings.recoverablekeystore.storage.CleanupManager.getInstance(context.getApplicationContext(), snapshotStorage, db, applicationKeyStorage);
                mInstance = new com.android.server.locksettings.recoverablekeystore.RecoverableKeyStoreManager(context.getApplicationContext(), db, new com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage(), java.util.concurrent.Executors.newScheduledThreadPool(1), snapshotStorage, new com.android.server.locksettings.recoverablekeystore.RecoverySnapshotListenersStorage(), platformKeyManager, applicationKeyStorage, new com.android.server.locksettings.recoverablekeystore.TestOnlyInsecureCertificateHelper(), cleanupManager, lockscreenCheckSessions);
            } catch (java.security.KeyStoreException e) {
                throw new android.os.ServiceSpecificException(22, e.getMessage());
            } catch (java.security.NoSuchAlgorithmException e2) {
                throw new java.lang.RuntimeException(e2);
            }
        }
        return mInstance;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    RecoverableKeyStoreManager(android.content.Context context, com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb recoverableKeyStoreDb, com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage recoverySessionStorage, java.util.concurrent.ScheduledExecutorService executorService, com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage snapshotStorage, com.android.server.locksettings.recoverablekeystore.RecoverySnapshotListenersStorage listenersStorage, com.android.server.locksettings.recoverablekeystore.PlatformKeyManager platformKeyManager, com.android.server.locksettings.recoverablekeystore.storage.ApplicationKeyStorage applicationKeyStorage, com.android.server.locksettings.recoverablekeystore.TestOnlyInsecureCertificateHelper testOnlyInsecureCertificateHelper, com.android.server.locksettings.recoverablekeystore.storage.CleanupManager cleanupManager, com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage remoteLockscreenValidationSessionStorage) throws android.os.ServiceSpecificException {
        this.mContext = context;
        this.mDatabase = recoverableKeyStoreDb;
        this.mRecoverySessionStorage = recoverySessionStorage;
        this.mExecutorService = executorService;
        this.mListenersStorage = listenersStorage;
        this.mSnapshotStorage = snapshotStorage;
        this.mPlatformKeyManager = platformKeyManager;
        this.mApplicationKeyStorage = applicationKeyStorage;
        this.mTestCertHelper = testOnlyInsecureCertificateHelper;
        this.mCleanupManager = cleanupManager;
        try {
            this.mCleanupManager.verifyKnownUsers();
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Failed to verify known users", e);
        }
        try {
            this.mRecoverableKeyGenerator = com.android.server.locksettings.recoverablekeystore.RecoverableKeyGenerator.newInstance(this.mDatabase);
            this.mRemoteLockscreenValidationSessionStorage = remoteLockscreenValidationSessionStorage;
        } catch (java.security.NoSuchAlgorithmException e2) {
            android.util.Log.wtf(TAG, "AES keygen algorithm not available. AOSP must support this.", e2);
            throw new android.os.ServiceSpecificException(22, e2.getMessage());
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r10v2 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    void initRecoveryService(java.lang.String rootCertificateAlias, byte[] recoveryServiceCertFile) throws android.os.RemoteException, android.os.ServiceSpecificException {
        checkRecoverKeyStorePermission();
        int userId = android.os.UserHandle.getCallingUserId();
        int uid = android.os.Binder.getCallingUid();
        int defaultCertificateAliasIfEmpty = this.mTestCertHelper.getDefaultCertificateAliasIfEmpty(rootCertificateAlias);
        if (!this.mTestCertHelper.isValidRootCertificateAlias(defaultCertificateAliasIfEmpty)) {
            throw new android.os.ServiceSpecificException(28, "Invalid root certificate alias");
        }
        java.lang.String activeRootOfTrust = this.mDatabase.getActiveRootOfTrust(userId, uid);
        if (activeRootOfTrust == 0) {
            android.util.Log.d(TAG, "Root of trust for recovery agent + " + uid + " is assigned for the first time to " + ((java.lang.String) defaultCertificateAliasIfEmpty));
        } else if (!activeRootOfTrust.equals(defaultCertificateAliasIfEmpty)) {
            android.util.Log.i(TAG, "Root of trust for recovery agent " + uid + " is changed to " + ((java.lang.String) defaultCertificateAliasIfEmpty) + " from  " + activeRootOfTrust);
        }
        long updatedRows = this.mDatabase.setActiveRootOfTrust(userId, uid, defaultCertificateAliasIfEmpty);
        if (updatedRows < 0) {
            throw new android.os.ServiceSpecificException(22, "Failed to set the root of trust in the local DB.");
        }
        try {
            com.android.server.locksettings.recoverablekeystore.certificate.CertXml certXml = com.android.server.locksettings.recoverablekeystore.certificate.CertXml.parse(recoveryServiceCertFile);
            long newSerial = certXml.getSerial();
            java.lang.Long oldSerial = this.mDatabase.getRecoveryServiceCertSerial(userId, uid, defaultCertificateAliasIfEmpty);
            if (oldSerial != null && oldSerial.longValue() >= newSerial && !this.mTestCertHelper.isTestOnlyCertificateAlias(defaultCertificateAliasIfEmpty)) {
                if (oldSerial.longValue() == newSerial) {
                    android.util.Log.i(TAG, "The cert file serial number is the same, so skip updating.");
                    return;
                } else {
                    android.util.Log.e(TAG, "The cert file serial number is older than the one in database.");
                    throw new android.os.ServiceSpecificException(29, "The cert file serial number is older than the one in database.");
                }
            }
            android.util.Log.i(TAG, "Updating the certificate with the new serial number " + newSerial);
            java.security.cert.X509Certificate rootCert = this.mTestCertHelper.getRootCertificate(defaultCertificateAliasIfEmpty);
            java.util.Date validationDate = this.mTestCertHelper.getValidationDate(defaultCertificateAliasIfEmpty);
            try {
                android.util.Log.d(TAG, "Getting and validating a random endpoint certificate");
                java.security.cert.CertPath certPath = certXml.getRandomEndpointCert(rootCert, validationDate);
                try {
                    android.util.Log.d(TAG, "Saving the randomly chosen endpoint certificate to database");
                    long updatedCertPathRows = this.mDatabase.setRecoveryServiceCertPath(userId, uid, defaultCertificateAliasIfEmpty, certPath);
                    try {
                        if (updatedCertPathRows <= 0) {
                            if (updatedCertPathRows < 0) {
                                throw new android.os.ServiceSpecificException(22, "Failed to set the certificate path in the local DB.");
                            }
                            return;
                        }
                        long updatedCertSerialRows = this.mDatabase.setRecoveryServiceCertSerial(userId, uid, defaultCertificateAliasIfEmpty, newSerial);
                        if (updatedCertSerialRows < 0) {
                            throw new android.os.ServiceSpecificException(22, "Failed to set the certificate serial number in the local DB.");
                        }
                        if (this.mDatabase.getSnapshotVersion(userId, uid) == null) {
                            android.util.Log.i(TAG, "This is a certificate change. Snapshot didn't exist");
                        } else {
                            this.mDatabase.setShouldCreateSnapshot(userId, uid, true);
                            android.util.Log.i(TAG, "This is a certificate change. Snapshot must be updated");
                        }
                        long updatedCounterIdRows = this.mDatabase.setCounterId(userId, uid, new java.security.SecureRandom().nextLong());
                        if (updatedCounterIdRows < 0) {
                            android.util.Log.e(TAG, "Failed to set the counter id in the local DB.");
                            return;
                        }
                        return;
                    } catch (java.security.cert.CertificateEncodingException e) {
                        e = e;
                    }
                } catch (java.security.cert.CertificateEncodingException e2) {
                    e = e2;
                    defaultCertificateAliasIfEmpty = 25;
                }
                android.util.Log.e(TAG, "Failed to encode CertPath", e);
                throw new android.os.ServiceSpecificException(defaultCertificateAliasIfEmpty, e.getMessage());
            } catch (com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException e3) {
                android.util.Log.e(TAG, "Invalid endpoint cert", e3);
                throw new android.os.ServiceSpecificException(28, e3.getMessage());
            }
        } catch (com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException e4) {
            android.util.Log.d(TAG, "Failed to parse the input as a cert file: " + com.android.internal.util.HexDump.toHexString(recoveryServiceCertFile));
            throw new android.os.ServiceSpecificException(25, e4.getMessage());
        }
    }

    public void initRecoveryServiceWithSigFile(java.lang.String rootCertificateAlias, byte[] recoveryServiceCertFile, byte[] recoveryServiceSigFile) throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        java.lang.String rootCertificateAlias2 = this.mTestCertHelper.getDefaultCertificateAliasIfEmpty(rootCertificateAlias);
        java.util.Objects.requireNonNull(recoveryServiceCertFile, "recoveryServiceCertFile is null");
        java.util.Objects.requireNonNull(recoveryServiceSigFile, "recoveryServiceSigFile is null");
        try {
            com.android.server.locksettings.recoverablekeystore.certificate.SigXml sigXml = com.android.server.locksettings.recoverablekeystore.certificate.SigXml.parse(recoveryServiceSigFile);
            java.security.cert.X509Certificate rootCert = this.mTestCertHelper.getRootCertificate(rootCertificateAlias2);
            java.util.Date validationDate = this.mTestCertHelper.getValidationDate(rootCertificateAlias2);
            try {
                sigXml.verifyFileSignature(rootCert, recoveryServiceCertFile, validationDate);
                initRecoveryService(rootCertificateAlias2, recoveryServiceCertFile);
            } catch (com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException e) {
                android.util.Log.e(TAG, "The signature over the cert file is invalid. Cert: " + com.android.internal.util.HexDump.toHexString(recoveryServiceCertFile) + " Sig: " + com.android.internal.util.HexDump.toHexString(recoveryServiceSigFile));
                throw new android.os.ServiceSpecificException(28, e.getMessage());
            }
        } catch (com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException e2) {
            android.util.Log.d(TAG, "Failed to parse the sig file: " + com.android.internal.util.HexDump.toHexString(recoveryServiceSigFile));
            throw new android.os.ServiceSpecificException(25, e2.getMessage());
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public android.security.keystore.recovery.KeyChainSnapshot getKeyChainSnapshot() throws android.os.RemoteException, android.os.ServiceSpecificException {
        checkRecoverKeyStorePermission();
        int uid = android.os.Binder.getCallingUid();
        android.security.keystore.recovery.KeyChainSnapshot snapshot = this.mSnapshotStorage.get(uid);
        if (snapshot == null) {
            throw new android.os.ServiceSpecificException(21);
        }
        return snapshot;
    }

    public void setSnapshotCreatedPendingIntent(android.app.PendingIntent intent) throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        int uid = android.os.Binder.getCallingUid();
        this.mListenersStorage.setSnapshotListener(uid, intent);
    }

    public void setServerParams(byte[] serverParams) throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        int userId = android.os.UserHandle.getCallingUserId();
        int uid = android.os.Binder.getCallingUid();
        byte[] currentServerParams = this.mDatabase.getServerParams(userId, uid);
        if (java.util.Arrays.equals(serverParams, currentServerParams)) {
            android.util.Log.v(TAG, "Not updating server params - same as old value.");
            return;
        }
        long updatedRows = this.mDatabase.setServerParams(userId, uid, serverParams);
        if (updatedRows < 0) {
            throw new android.os.ServiceSpecificException(22, "Database failure trying to set server params.");
        }
        if (currentServerParams == null) {
            android.util.Log.i(TAG, "Initialized server params.");
        } else if (this.mDatabase.getSnapshotVersion(userId, uid) != null) {
            this.mDatabase.setShouldCreateSnapshot(userId, uid, true);
            android.util.Log.i(TAG, "Updated server params. Snapshot must be updated");
        } else {
            android.util.Log.i(TAG, "Updated server params. Snapshot didn't exist");
        }
    }

    public void setRecoveryStatus(java.lang.String alias, int status) throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        java.util.Objects.requireNonNull(alias, "alias is null");
        long updatedRows = this.mDatabase.setRecoveryStatus(android.os.Binder.getCallingUid(), alias, status);
        if (updatedRows < 0) {
            throw new android.os.ServiceSpecificException(22, "Failed to set the key recovery status in the local DB.");
        }
    }

    public java.util.Map<java.lang.String, java.lang.Integer> getRecoveryStatus() throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        return this.mDatabase.getStatusForAllKeys(android.os.Binder.getCallingUid());
    }

    public void setRecoverySecretTypes(int[] secretTypes) throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        java.util.Objects.requireNonNull(secretTypes, "secretTypes is null");
        int userId = android.os.UserHandle.getCallingUserId();
        int uid = android.os.Binder.getCallingUid();
        int[] currentSecretTypes = this.mDatabase.getRecoverySecretTypes(userId, uid);
        if (java.util.Arrays.equals(secretTypes, currentSecretTypes)) {
            android.util.Log.v(TAG, "Not updating secret types - same as old value.");
            return;
        }
        long updatedRows = this.mDatabase.setRecoverySecretTypes(userId, uid, secretTypes);
        if (updatedRows < 0) {
            throw new android.os.ServiceSpecificException(22, "Database error trying to set secret types.");
        }
        if (currentSecretTypes.length == 0) {
            android.util.Log.i(TAG, "Initialized secret types.");
            return;
        }
        android.util.Log.i(TAG, "Updated secret types. Snapshot pending.");
        if (this.mDatabase.getSnapshotVersion(userId, uid) != null) {
            this.mDatabase.setShouldCreateSnapshot(userId, uid, true);
            android.util.Log.i(TAG, "Updated secret types. Snapshot must be updated");
        } else {
            android.util.Log.i(TAG, "Updated secret types. Snapshot didn't exist");
        }
    }

    public int[] getRecoverySecretTypes() throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        return this.mDatabase.getRecoverySecretTypes(android.os.UserHandle.getCallingUserId(), android.os.Binder.getCallingUid());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    byte[] startRecoverySession(java.lang.String sessionId, byte[] verifierPublicKey, byte[] vaultParams, byte[] vaultChallenge, java.util.List<android.security.keystore.recovery.KeyChainProtectionParams> secrets) throws android.os.RemoteException, android.os.ServiceSpecificException {
        checkRecoverKeyStorePermission();
        int uid = android.os.Binder.getCallingUid();
        if (secrets.size() != 1) {
            throw new java.lang.UnsupportedOperationException("Only a single KeyChainProtectionParams is supported");
        }
        try {
            java.security.PublicKey publicKey = com.android.server.locksettings.recoverablekeystore.KeySyncUtils.deserializePublicKey(verifierPublicKey);
            if (!publicKeysMatch(publicKey, vaultParams)) {
                throw new android.os.ServiceSpecificException(28, "The public keys given in verifierPublicKey and vaultParams do not match.");
            }
            byte[] keyClaimant = com.android.server.locksettings.recoverablekeystore.KeySyncUtils.generateKeyClaimant();
            byte[] kfHash = secrets.get(0).getSecret();
            this.mRecoverySessionStorage.add(uid, new com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry(sessionId, kfHash, keyClaimant, vaultParams));
            android.util.Log.i(TAG, "Received VaultParams for recovery: " + com.android.internal.util.HexDump.toHexString(vaultParams));
            try {
                byte[] thmKfHash = com.android.server.locksettings.recoverablekeystore.KeySyncUtils.calculateThmKfHash(kfHash);
                return com.android.server.locksettings.recoverablekeystore.KeySyncUtils.encryptRecoveryClaim(publicKey, vaultParams, vaultChallenge, thmKfHash, keyClaimant);
            } catch (java.security.InvalidKeyException e) {
                throw new android.os.ServiceSpecificException(25, e.getMessage());
            } catch (java.security.NoSuchAlgorithmException e2) {
                android.util.Log.wtf(TAG, "SecureBox algorithm missing. AOSP must support this.", e2);
                throw new android.os.ServiceSpecificException(22, e2.getMessage());
            }
        } catch (java.security.spec.InvalidKeySpecException e3) {
            throw new android.os.ServiceSpecificException(25, e3.getMessage());
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public byte[] startRecoverySessionWithCertPath(java.lang.String sessionId, java.lang.String rootCertificateAlias, android.security.keystore.recovery.RecoveryCertPath verifierCertPath, byte[] vaultParams, byte[] vaultChallenge, java.util.List<android.security.keystore.recovery.KeyChainProtectionParams> secrets) throws android.os.RemoteException, android.os.ServiceSpecificException {
        checkRecoverKeyStorePermission();
        java.lang.String rootCertificateAlias2 = this.mTestCertHelper.getDefaultCertificateAliasIfEmpty(rootCertificateAlias);
        java.util.Objects.requireNonNull(sessionId, "invalid session");
        java.util.Objects.requireNonNull(verifierCertPath, "verifierCertPath is null");
        java.util.Objects.requireNonNull(vaultParams, "vaultParams is null");
        java.util.Objects.requireNonNull(vaultChallenge, "vaultChallenge is null");
        java.util.Objects.requireNonNull(secrets, "secrets is null");
        try {
            java.security.cert.CertPath certPath = verifierCertPath.getCertPath();
            try {
                java.util.Date validationDate = this.mTestCertHelper.getValidationDate(rootCertificateAlias2);
                com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.validateCertPath(this.mTestCertHelper.getRootCertificate(rootCertificateAlias2), certPath, validationDate);
                byte[] verifierPublicKey = certPath.getCertificates().get(0).getPublicKey().getEncoded();
                if (verifierPublicKey == null) {
                    android.util.Log.e(TAG, "Failed to encode verifierPublicKey");
                    throw new android.os.ServiceSpecificException(25, "Failed to encode verifierPublicKey");
                }
                return startRecoverySession(sessionId, verifierPublicKey, vaultParams, vaultChallenge, secrets);
            } catch (com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException e) {
                android.util.Log.e(TAG, "Failed to validate the given cert path", e);
                throw new android.os.ServiceSpecificException(28, e.getMessage());
            }
        } catch (java.security.cert.CertificateException e2) {
            throw new android.os.ServiceSpecificException(25, e2.getMessage());
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public java.util.Map<java.lang.String, java.lang.String> recoverKeyChainSnapshot(java.lang.String sessionId, byte[] encryptedRecoveryKey, java.util.List<android.security.keystore.recovery.WrappedApplicationKey> applicationKeys) throws android.os.RemoteException, android.os.ServiceSpecificException {
        checkRecoverKeyStorePermission();
        int userId = android.os.UserHandle.getCallingUserId();
        int uid = android.os.Binder.getCallingUid();
        com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry sessionEntry = this.mRecoverySessionStorage.get(uid, sessionId);
        try {
            if (sessionEntry == null) {
                throw new android.os.ServiceSpecificException(24, java.lang.String.format(java.util.Locale.US, "Application uid=%d does not have pending session '%s'", java.lang.Integer.valueOf(uid), sessionId));
            }
            try {
                byte[] recoveryKey = decryptRecoveryKey(sessionEntry, encryptedRecoveryKey);
                java.util.Map<java.lang.String, byte[]> keysByAlias = recoverApplicationKeys(recoveryKey, applicationKeys);
                return importKeyMaterials(userId, uid, keysByAlias);
            } catch (java.security.KeyStoreException e) {
                throw new android.os.ServiceSpecificException(22, e.getMessage());
            }
        } finally {
            sessionEntry.destroy();
            this.mRecoverySessionStorage.remove(uid);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    private java.util.Map<java.lang.String, java.lang.String> importKeyMaterials(int userId, int uid, java.util.Map<java.lang.String, byte[]> keysByAlias) throws java.security.KeyStoreException, android.os.ServiceSpecificException {
        android.util.ArrayMap<java.lang.String, java.lang.String> grantAliasesByAlias = new android.util.ArrayMap<>(keysByAlias.size());
        for (java.lang.String alias : keysByAlias.keySet()) {
            this.mApplicationKeyStorage.setSymmetricKeyEntry(userId, uid, alias, keysByAlias.get(alias));
            java.lang.String grantAlias = getAlias(userId, uid, alias);
            android.util.Log.i(TAG, java.lang.String.format(java.util.Locale.US, "Import %s -> %s", alias, grantAlias));
            grantAliasesByAlias.put(alias, grantAlias);
        }
        return grantAliasesByAlias;
    }

    private java.lang.String getAlias(int userId, int uid, java.lang.String alias) {
        return this.mApplicationKeyStorage.getGrantAlias(userId, uid, alias);
    }

    public void closeSession(java.lang.String sessionId) throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        java.util.Objects.requireNonNull(sessionId, "invalid session");
        this.mRecoverySessionStorage.remove(android.os.Binder.getCallingUid(), sessionId);
    }

    public void removeKey(java.lang.String alias) throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        java.util.Objects.requireNonNull(alias, "alias is null");
        int uid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        boolean wasRemoved = this.mDatabase.removeKey(uid, alias);
        if (wasRemoved) {
            this.mDatabase.setShouldCreateSnapshot(userId, uid, true);
            this.mApplicationKeyStorage.deleteEntry(userId, uid, alias);
        }
    }

    @java.lang.Deprecated
    public java.lang.String generateKey(java.lang.String alias) throws android.os.RemoteException {
        return generateKeyWithMetadata(alias, null);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public java.lang.String generateKeyWithMetadata(java.lang.String alias, byte[] metadata) throws android.os.RemoteException, android.os.ServiceSpecificException {
        checkRecoverKeyStorePermission();
        java.util.Objects.requireNonNull(alias, "alias is null");
        int uid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        try {
            com.android.server.locksettings.recoverablekeystore.PlatformEncryptionKey encryptionKey = this.mPlatformKeyManager.getEncryptKey(userId);
            try {
                byte[] secretKey = this.mRecoverableKeyGenerator.generateAndStoreKey(encryptionKey, userId, uid, alias, metadata);
                this.mApplicationKeyStorage.setSymmetricKeyEntry(userId, uid, alias, secretKey);
                return getAlias(userId, uid, alias);
            } catch (com.android.server.locksettings.recoverablekeystore.RecoverableKeyStorageException | java.security.InvalidKeyException | java.security.KeyStoreException e) {
                throw new android.os.ServiceSpecificException(22, e.getMessage());
            }
        } catch (com.android.server.locksettings.recoverablekeystore.InsecureUserException e2) {
            throw new android.os.ServiceSpecificException(23, e2.getMessage());
        } catch (java.io.IOException | java.security.KeyStoreException | java.security.UnrecoverableKeyException e3) {
            throw new android.os.ServiceSpecificException(22, e3.getMessage());
        } catch (java.security.NoSuchAlgorithmException e4) {
            throw new java.lang.RuntimeException(e4);
        }
    }

    @java.lang.Deprecated
    public java.lang.String importKey(java.lang.String alias, byte[] keyBytes) throws android.os.RemoteException {
        return importKeyWithMetadata(alias, keyBytes, null);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public java.lang.String importKeyWithMetadata(java.lang.String alias, byte[] keyBytes, byte[] metadata) throws android.os.RemoteException, android.os.ServiceSpecificException {
        checkRecoverKeyStorePermission();
        java.util.Objects.requireNonNull(alias, "alias is null");
        java.util.Objects.requireNonNull(keyBytes, "keyBytes is null");
        if (keyBytes.length != 32) {
            android.util.Log.e(TAG, "The given key for import doesn't have the required length 256");
            throw new android.os.ServiceSpecificException(27, "The given key does not contain 256 bits.");
        }
        int uid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        try {
            com.android.server.locksettings.recoverablekeystore.PlatformEncryptionKey encryptionKey = this.mPlatformKeyManager.getEncryptKey(userId);
            try {
                this.mRecoverableKeyGenerator.importKey(encryptionKey, userId, uid, alias, keyBytes, metadata);
                this.mApplicationKeyStorage.setSymmetricKeyEntry(userId, uid, alias, keyBytes);
                return getAlias(userId, uid, alias);
            } catch (com.android.server.locksettings.recoverablekeystore.RecoverableKeyStorageException | java.security.InvalidKeyException | java.security.KeyStoreException e) {
                throw new android.os.ServiceSpecificException(22, e.getMessage());
            }
        } catch (com.android.server.locksettings.recoverablekeystore.InsecureUserException e2) {
            throw new android.os.ServiceSpecificException(23, e2.getMessage());
        } catch (java.io.IOException | java.security.KeyStoreException | java.security.UnrecoverableKeyException e3) {
            throw new android.os.ServiceSpecificException(22, e3.getMessage());
        } catch (java.security.NoSuchAlgorithmException e4) {
            throw new java.lang.RuntimeException(e4);
        }
    }

    public java.lang.String getKey(java.lang.String alias) throws android.os.RemoteException {
        checkRecoverKeyStorePermission();
        java.util.Objects.requireNonNull(alias, "alias is null");
        int uid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        return getAlias(userId, uid, alias);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    private byte[] decryptRecoveryKey(com.android.server.locksettings.recoverablekeystore.storage.RecoverySessionStorage.Entry sessionEntry, byte[] encryptedClaimResponse) throws android.os.RemoteException, android.os.ServiceSpecificException {
        try {
            byte[] locallyEncryptedKey = com.android.server.locksettings.recoverablekeystore.KeySyncUtils.decryptRecoveryClaimResponse(sessionEntry.getKeyClaimant(), sessionEntry.getVaultParams(), encryptedClaimResponse);
            try {
                return com.android.server.locksettings.recoverablekeystore.KeySyncUtils.decryptRecoveryKey(sessionEntry.getLskfHash(), locallyEncryptedKey);
            } catch (java.security.InvalidKeyException e) {
                android.util.Log.e(TAG, "Got InvalidKeyException during decrypting recovery key", e);
                throw new android.os.ServiceSpecificException(26, "Failed to decrypt recovery key " + e.getMessage());
            } catch (java.security.NoSuchAlgorithmException e2) {
                throw new android.os.ServiceSpecificException(22, e2.getMessage());
            } catch (javax.crypto.AEADBadTagException e3) {
                android.util.Log.e(TAG, "Got AEADBadTagException during decrypting recovery key", e3);
                throw new android.os.ServiceSpecificException(26, "Failed to decrypt recovery key " + e3.getMessage());
            }
        } catch (java.security.InvalidKeyException e4) {
            android.util.Log.e(TAG, "Got InvalidKeyException during decrypting recovery claim response", e4);
            throw new android.os.ServiceSpecificException(26, "Failed to decrypt recovery key " + e4.getMessage());
        } catch (java.security.NoSuchAlgorithmException e5) {
            throw new android.os.ServiceSpecificException(22, e5.getMessage());
        } catch (javax.crypto.AEADBadTagException e6) {
            android.util.Log.e(TAG, "Got AEADBadTagException during decrypting recovery claim response", e6);
            throw new android.os.ServiceSpecificException(26, "Failed to decrypt recovery key " + e6.getMessage());
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    private java.util.Map<java.lang.String, byte[]> recoverApplicationKeys(byte[] recoveryKey, java.util.List<android.security.keystore.recovery.WrappedApplicationKey> applicationKeys) throws android.os.RemoteException, android.os.ServiceSpecificException {
        java.util.HashMap<java.lang.String, byte[]> keyMaterialByAlias = new java.util.HashMap<>();
        for (android.security.keystore.recovery.WrappedApplicationKey applicationKey : applicationKeys) {
            java.lang.String alias = applicationKey.getAlias();
            byte[] encryptedKeyMaterial = applicationKey.getEncryptedKeyMaterial();
            byte[] keyMetadata = applicationKey.getMetadata();
            try {
                byte[] keyMaterial = com.android.server.locksettings.recoverablekeystore.KeySyncUtils.decryptApplicationKey(recoveryKey, encryptedKeyMaterial, keyMetadata);
                keyMaterialByAlias.put(alias, keyMaterial);
            } catch (java.security.InvalidKeyException e) {
                android.util.Log.e(TAG, "Got InvalidKeyException during decrypting application key with alias: " + alias, e);
                throw new android.os.ServiceSpecificException(26, "Failed to recover key with alias '" + alias + "': " + e.getMessage());
            } catch (java.security.NoSuchAlgorithmException e2) {
                android.util.Log.wtf(TAG, "Missing SecureBox algorithm. AOSP required to support this.", e2);
                throw new android.os.ServiceSpecificException(22, e2.getMessage());
            } catch (javax.crypto.AEADBadTagException e3) {
                android.util.Log.e(TAG, "Got AEADBadTagException during decrypting application key with alias: " + alias, e3);
            }
        }
        if (!applicationKeys.isEmpty() && keyMaterialByAlias.isEmpty()) {
            android.util.Log.e(TAG, "Failed to recover any of the application keys.");
            throw new android.os.ServiceSpecificException(26, "Failed to recover any of the application keys.");
        }
        return keyMaterialByAlias;
    }

    public void lockScreenSecretAvailable(int credentialType, byte[] credential, int userId) {
        try {
            this.mExecutorService.schedule(com.android.server.locksettings.recoverablekeystore.KeySyncTask.newInstance(this.mContext, this.mDatabase, this.mSnapshotStorage, this.mListenersStorage, userId, credentialType, credential, false), SYNC_DELAY_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (com.android.server.locksettings.recoverablekeystore.InsecureUserException e) {
            android.util.Log.wtf(TAG, "Impossible - insecure user, but user just entered lock screen", e);
        } catch (java.security.KeyStoreException e2) {
            android.util.Log.e(TAG, "Key store error encountered during recoverable key sync", e2);
        } catch (java.security.NoSuchAlgorithmException e3) {
            android.util.Log.wtf(TAG, "Should never happen - algorithm unavailable for KeySync", e3);
        }
    }

    public void lockScreenSecretChanged(int credentialType, byte[] credential, int userId) {
        try {
            this.mExecutorService.schedule(com.android.server.locksettings.recoverablekeystore.KeySyncTask.newInstance(this.mContext, this.mDatabase, this.mSnapshotStorage, this.mListenersStorage, userId, credentialType, credential, true), SYNC_DELAY_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (com.android.server.locksettings.recoverablekeystore.InsecureUserException e) {
            android.util.Log.e(TAG, "InsecureUserException during lock screen secret update", e);
        } catch (java.security.KeyStoreException e2) {
            android.util.Log.e(TAG, "Key store error encountered during recoverable key sync", e2);
        } catch (java.security.NoSuchAlgorithmException e3) {
            android.util.Log.wtf(TAG, "Should never happen - algorithm unavailable for KeySync", e3);
        }
    }

    public android.app.RemoteLockscreenValidationSession startRemoteLockscreenValidation(com.android.server.locksettings.LockSettingsService lockSettingsService) {
        if (this.mRemoteLockscreenValidationSessionStorage == null) {
            throw new java.lang.UnsupportedOperationException("Under development");
        }
        checkVerifyRemoteLockscreenPermission();
        int userId = android.os.UserHandle.getCallingUserId();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            int savedCredentialType = lockSettingsService.getCredentialType(userId);
            android.os.Binder.restoreCallingIdentity(token);
            int keyguardCredentialsType = lockPatternUtilsToKeyguardType(savedCredentialType);
            com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.LockscreenVerificationSession session = this.mRemoteLockscreenValidationSessionStorage.startSession(userId);
            java.security.PublicKey publicKey = session.getKeyPair().getPublic();
            byte[] encodedPublicKey = com.android.security.SecureBox.encodePublicKey(publicKey);
            int badGuesses = this.mDatabase.getBadRemoteGuessCounter(userId);
            int remainingAttempts = java.lang.Math.max(5 - badGuesses, 0);
            return new android.app.RemoteLockscreenValidationSession.Builder().setLockType(keyguardCredentialsType).setRemainingAttempts(remainingAttempts).setSourcePublicKey(encodedPublicKey).build();
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public synchronized android.app.RemoteLockscreenValidationResult validateRemoteLockscreen(byte[] encryptedCredential, com.android.server.locksettings.LockSettingsService lockSettingsService) {
        checkVerifyRemoteLockscreenPermission();
        int userId = android.os.UserHandle.getCallingUserId();
        com.android.server.locksettings.recoverablekeystore.storage.RemoteLockscreenValidationSessionStorage.LockscreenVerificationSession session = this.mRemoteLockscreenValidationSessionStorage.get(userId);
        int badGuesses = this.mDatabase.getBadRemoteGuessCounter(userId);
        int remainingAttempts = 5 - badGuesses;
        if (remainingAttempts <= 0) {
            return new android.app.RemoteLockscreenValidationResult.Builder().setResultCode(4).build();
        }
        if (session == null) {
            return new android.app.RemoteLockscreenValidationResult.Builder().setResultCode(5).build();
        }
        try {
            try {
                byte[] decryptedCredentials = com.android.security.SecureBox.decrypt(session.getKeyPair().getPrivate(), null, com.android.internal.widget.LockPatternUtils.ENCRYPTED_REMOTE_CREDENTIALS_HEADER, encryptedCredential);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    int savedCredentialType = lockSettingsService.getCredentialType(userId);
                    int keyguardCredentialsType = lockPatternUtilsToKeyguardType(savedCredentialType);
                    com.android.internal.widget.LockscreenCredential credential = createLockscreenCredential(keyguardCredentialsType, decryptedCredentials);
                    try {
                        try {
                            java.util.Arrays.fill(decryptedCredentials, (byte) 0);
                            com.android.internal.widget.VerifyCredentialResponse verifyResponse = lockSettingsService.verifyCredential(credential, userId, 0);
                            android.app.RemoteLockscreenValidationResult remoteLockscreenValidationResultHandleVerifyCredentialResponse = handleVerifyCredentialResponse(verifyResponse, userId);
                            if (credential != null) {
                                credential.close();
                            }
                            android.os.Binder.restoreCallingIdentity(token);
                            return remoteLockscreenValidationResultHandleVerifyCredentialResponse;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Binder.restoreCallingIdentity(token);
                            throw th;
                        }
                    } finally {
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.security.NoSuchAlgorithmException e) {
                android.util.Log.wtf(TAG, "Missing SecureBox algorithm. AOSP required to support this.", e);
                throw new java.lang.IllegalStateException(e);
            }
        } catch (java.security.InvalidKeyException e2) {
            android.util.Log.e(TAG, "Got InvalidKeyException during lock screen credentials decryption");
            throw new java.lang.IllegalStateException(e2);
        } catch (javax.crypto.AEADBadTagException e3) {
            throw new java.lang.IllegalStateException("Could not decrypt credentials guess", e3);
        }
    }

    private android.app.RemoteLockscreenValidationResult handleVerifyCredentialResponse(com.android.internal.widget.VerifyCredentialResponse response, int userId) {
        if (response.getResponseCode() == 0) {
            this.mDatabase.setBadRemoteGuessCounter(userId, 0);
            this.mRemoteLockscreenValidationSessionStorage.finishSession(userId);
            return new android.app.RemoteLockscreenValidationResult.Builder().setResultCode(1).build();
        }
        if (response.getResponseCode() == 1) {
            long timeout = response.getTimeout();
            return new android.app.RemoteLockscreenValidationResult.Builder().setResultCode(3).setTimeoutMillis(timeout).build();
        }
        int badGuesses = this.mDatabase.getBadRemoteGuessCounter(userId);
        this.mDatabase.setBadRemoteGuessCounter(userId, badGuesses + 1);
        return new android.app.RemoteLockscreenValidationResult.Builder().setResultCode(2).build();
    }

    private com.android.internal.widget.LockscreenCredential createLockscreenCredential(int lockType, byte[] password) {
        switch (lockType) {
            case 0:
                java.lang.CharSequence passwordStr = new java.lang.String(password, java.nio.charset.StandardCharsets.UTF_8);
                return com.android.internal.widget.LockscreenCredential.createPassword(passwordStr);
            case 1:
                java.lang.CharSequence pinStr = new java.lang.String(password);
                return com.android.internal.widget.LockscreenCredential.createPin(pinStr);
            case 2:
                java.util.List<com.android.internal.widget.LockPatternView.Cell> pattern = com.android.internal.widget.LockPatternUtils.byteArrayToPattern(password);
                return com.android.internal.widget.LockscreenCredential.createPattern(pattern);
            default:
                throw new java.lang.IllegalStateException("Lockscreen is not set");
        }
    }

    private void checkVerifyRemoteLockscreenPermission() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.CHECK_REMOTE_LOCKSCREEN", "Caller " + android.os.Binder.getCallingUid() + " doesn't have CHECK_REMOTE_LOCKSCREEN permission.");
        int userId = android.os.UserHandle.getCallingUserId();
        int uid = android.os.Binder.getCallingUid();
        this.mCleanupManager.registerRecoveryAgent(userId, uid);
    }

    private int lockPatternUtilsToKeyguardType(int credentialsType) {
        switch (credentialsType) {
            case -1:
                throw new java.lang.IllegalStateException("Screen lock is not set");
            case 0:
            case 2:
            default:
                throw new java.lang.IllegalStateException("Screen lock is not set");
            case 1:
                return 2;
            case 3:
                return 1;
            case 4:
                return 0;
        }
    }

    private void checkRecoverKeyStorePermission() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.RECOVER_KEYSTORE", "Caller " + android.os.Binder.getCallingUid() + " doesn't have RecoverKeyStore permission.");
        int userId = android.os.UserHandle.getCallingUserId();
        int uid = android.os.Binder.getCallingUid();
        this.mCleanupManager.registerRecoveryAgent(userId, uid);
    }

    private boolean publicKeysMatch(java.security.PublicKey publicKey, byte[] vaultParams) {
        byte[] encodedPublicKey = com.android.security.SecureBox.encodePublicKey(publicKey);
        return java.util.Arrays.equals(encodedPublicKey, java.util.Arrays.copyOf(vaultParams, encodedPublicKey.length));
    }
}
