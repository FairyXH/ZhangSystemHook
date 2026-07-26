package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class SyntheticPasswordManager {
    private static final int INVALID_WEAVER_SLOT = -1;
    public static final long NULL_PROTECTOR_ID = 0;
    private static final java.lang.String PASSWORD_DATA_NAME = "pwd";
    private static final java.lang.String PASSWORD_METRICS_NAME = "metrics";
    private static final int PASSWORD_SALT_LENGTH = 16;
    private static final int PASSWORD_SCRYPT_LOG_N = 11;
    private static final int PASSWORD_SCRYPT_LOG_P = 1;
    private static final int PASSWORD_SCRYPT_LOG_R = 3;
    private static final java.lang.String PROTECTOR_KEY_ALIAS_PREFIX = "synthetic_password_";
    private static final byte PROTECTOR_TYPE_LSKF_BASED = 0;
    private static final byte PROTECTOR_TYPE_STRONG_TOKEN_BASED = 1;
    private static final byte PROTECTOR_TYPE_WEAK_TOKEN_BASED = 2;
    private static final int SECDISCARDABLE_LENGTH = 16384;
    private static final java.lang.String SECDISCARDABLE_NAME = "secdis";
    private static final java.lang.String SP_BLOB_NAME = "spblob";
    private static final java.lang.String SP_E0_NAME = "e0";
    private static final java.lang.String SP_HANDLE_NAME = "handle";
    private static final java.lang.String SP_P1_NAME = "p1";
    private static final int STRETCHED_LSKF_LENGTH = 32;
    private static final int SYNTHETIC_PASSWORD_SECURITY_STRENGTH = 32;
    private static final byte SYNTHETIC_PASSWORD_VERSION_V1 = 1;
    private static final byte SYNTHETIC_PASSWORD_VERSION_V2 = 2;
    private static final byte SYNTHETIC_PASSWORD_VERSION_V3 = 3;
    private static final java.lang.String TAG = "SyntheticPasswordManager";
    static final int TOKEN_TYPE_STRONG = 0;
    static final int TOKEN_TYPE_WEAK = 1;
    private static final java.lang.String VENDOR_AUTH_SECRET_NAME = "vendor_auth_secret";
    private static final java.lang.String WEAVER_SLOT_NAME = "weaver";
    private static final byte WEAVER_VERSION = 1;
    private final android.content.Context mContext;
    private com.android.server.locksettings.PasswordSlotManager mPasswordSlotManager;
    private com.android.server.locksettings.LockSettingsStorage mStorage;
    private final android.os.UserManager mUserManager;
    private volatile android.hardware.weaver.IWeaver mWeaver;
    private android.hardware.weaver.WeaverConfig mWeaverConfig;
    private static final byte[] DEFAULT_PASSWORD = "default-password".getBytes();
    private static final byte[] PERSONALIZATION_SECDISCARDABLE = "secdiscardable-transform".getBytes();
    private static final byte[] PERSONALIZATION_KEY_STORE_PASSWORD = "keystore-password".getBytes();
    private static final byte[] PERSONALIZATION_USER_GK_AUTH = "user-gk-authentication".getBytes();
    private static final byte[] PERSONALIZATION_SP_GK_AUTH = "sp-gk-authentication".getBytes();
    private static final byte[] PERSONALIZATION_FBE_KEY = "fbe-key".getBytes();
    private static final byte[] PERSONALIZATION_AUTHSECRET_KEY = "authsecret-hal".getBytes();
    private static final byte[] PERSONALIZATION_AUTHSECRET_ENCRYPTION_KEY = "vendor-authsecret-encryption-key".getBytes();
    private static final byte[] PERSONALIZATION_SP_SPLIT = "sp-split".getBytes();
    private static final byte[] PERSONALIZATION_PASSWORD_HASH = "pw-hash".getBytes();
    private static final byte[] PERSONALIZATION_E0 = "e0-encryption".getBytes();
    private static final byte[] PERSONALIZATION_WEAVER_PASSWORD = "weaver-pwd".getBytes();
    private static final byte[] PERSONALIZATION_WEAVER_KEY = "weaver-key".getBytes();
    private static final byte[] PERSONALIZATION_WEAVER_TOKEN = "weaver-token".getBytes();
    private static final byte[] PERSONALIZATION_PASSWORD_METRICS = "password-metrics".getBytes();
    private static final byte[] PERSONALIZATION_CONTEXT = "android-synthetic-password-personalization-context".getBytes();
    private com.android.server.locksettings.ISyntheticPasswordManagerExt mSyntheticPasswordManagerExt = (com.android.server.locksettings.ISyntheticPasswordManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.locksettings.ISyntheticPasswordManagerExt.class).base(this).create();
    private final com.android.server.locksettings.ISyntheticPasswordManagerWrapper mSyntheticPasswordManagerWrapper = new com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordManagerWrapper();
    private final android.os.RemoteCallbackList<com.android.internal.widget.IWeakEscrowTokenRemovedListener> mListeners = new android.os.RemoteCallbackList<>();
    private android.util.ArrayMap<java.lang.Integer, android.util.ArrayMap<java.lang.Long, com.android.server.locksettings.SyntheticPasswordManager.TokenData>> tokenMap = new android.util.ArrayMap<>();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface TokenType {
    }

    private native long nativeSidFromPasswordHandle(byte[] bArr);

    public com.android.server.locksettings.ISyntheticPasswordManagerWrapper getWrapper() {
        return this.mSyntheticPasswordManagerWrapper;
    }

    private class SyntheticPasswordManagerWrapper implements com.android.server.locksettings.ISyntheticPasswordManagerWrapper {
        private SyntheticPasswordManagerWrapper() {
        }

        @Override // com.android.server.locksettings.ISyntheticPasswordManagerWrapper
        public com.android.server.locksettings.ISyntheticPasswordManagerExt getExtImpl() {
            return com.android.server.locksettings.SyntheticPasswordManager.this.mSyntheticPasswordManagerExt;
        }

        @Override // com.android.server.locksettings.ISyntheticPasswordManagerWrapper
        public android.hardware.weaver.IWeaver getWeaver() {
            return com.android.server.locksettings.SyntheticPasswordManager.this.getWeaverService();
        }
    }

    static class AuthenticationResult {
        public com.android.internal.widget.VerifyCredentialResponse gkResponse;
        public com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword syntheticPassword;

        AuthenticationResult() {
        }
    }

    static class SyntheticPassword {
        private byte[] mEncryptedEscrowSplit0;
        private byte[] mEscrowSplit1;
        private byte[] mSyntheticPassword;
        private final byte mVersion;

        SyntheticPassword(byte version) {
            this.mVersion = version;
        }

        private byte[] deriveSubkey(byte[] personalization) {
            if (this.mVersion == 3) {
                return new com.android.server.locksettings.SP800Derive(this.mSyntheticPassword).withContext(personalization, com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_CONTEXT);
            }
            return com.android.server.locksettings.SyntheticPasswordCrypto.personalizedHash(personalization, this.mSyntheticPassword);
        }

        public byte[] deriveKeyStorePassword() {
            return com.android.server.locksettings.SyntheticPasswordManager.bytesToHex(deriveSubkey(com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_KEY_STORE_PASSWORD));
        }

        public byte[] deriveGkPassword() {
            return deriveSubkey(com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_SP_GK_AUTH);
        }

        public byte[] deriveFileBasedEncryptionKey() {
            return deriveSubkey(com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_FBE_KEY);
        }

        public byte[] deriveVendorAuthSecret() {
            return deriveSubkey(com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_AUTHSECRET_KEY);
        }

        public byte[] derivePasswordHashFactor() {
            return deriveSubkey(com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_PASSWORD_HASH);
        }

        public byte[] deriveMetricsKey() {
            return deriveSubkey(com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_PASSWORD_METRICS);
        }

        public byte[] deriveVendorAuthSecretEncryptionKey() {
            return deriveSubkey(com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_AUTHSECRET_ENCRYPTION_KEY);
        }

        public void setEscrowData(byte[] encryptedEscrowSplit0, byte[] escrowSplit1) {
            this.mEncryptedEscrowSplit0 = encryptedEscrowSplit0;
            this.mEscrowSplit1 = escrowSplit1;
        }

        public void recreateFromEscrow(byte[] escrowSplit0) {
            java.util.Objects.requireNonNull(this.mEscrowSplit1);
            java.util.Objects.requireNonNull(this.mEncryptedEscrowSplit0);
            recreate(escrowSplit0, this.mEscrowSplit1);
        }

        public void recreateDirectly(byte[] syntheticPassword) {
            this.mSyntheticPassword = java.util.Arrays.copyOf(syntheticPassword, syntheticPassword.length);
        }

        static com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword create() {
            com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword result = new com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword((byte) 3);
            byte[] escrowSplit0 = com.android.server.locksettings.SecureRandomUtils.randomBytes(32);
            byte[] escrowSplit1 = com.android.server.locksettings.SecureRandomUtils.randomBytes(32);
            result.recreate(escrowSplit0, escrowSplit1);
            byte[] encrypteEscrowSplit0 = com.android.server.locksettings.SyntheticPasswordCrypto.encrypt(result.mSyntheticPassword, com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_E0, escrowSplit0);
            result.setEscrowData(encrypteEscrowSplit0, escrowSplit1);
            return result;
        }

        private void recreate(byte[] escrowSplit0, byte[] escrowSplit1) {
            this.mSyntheticPassword = com.android.server.locksettings.SyntheticPasswordManager.bytesToHex(com.android.server.locksettings.SyntheticPasswordCrypto.personalizedHash(com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_SP_SPLIT, escrowSplit0, escrowSplit1));
        }

        public byte[] getEscrowSecret() {
            if (this.mEncryptedEscrowSplit0 == null) {
                return null;
            }
            return com.android.server.locksettings.SyntheticPasswordCrypto.decrypt(this.mSyntheticPassword, com.android.server.locksettings.SyntheticPasswordManager.PERSONALIZATION_E0, this.mEncryptedEscrowSplit0);
        }

        public byte[] getSyntheticPassword() {
            return this.mSyntheticPassword;
        }

        public byte getVersion() {
            return this.mVersion;
        }
    }

    static class PasswordData {
        public int credentialType;
        public byte[] passwordHandle;
        public int pinLength;
        byte[] salt;
        byte scryptLogN;
        byte scryptLogP;
        byte scryptLogR;

        PasswordData() {
        }

        public static com.android.server.locksettings.SyntheticPasswordManager.PasswordData create(int credentialType, int pinLength) {
            com.android.server.locksettings.SyntheticPasswordManager.PasswordData result = new com.android.server.locksettings.SyntheticPasswordManager.PasswordData();
            result.scryptLogN = (byte) 11;
            result.scryptLogR = (byte) 3;
            result.scryptLogP = (byte) 1;
            result.credentialType = credentialType;
            result.pinLength = pinLength;
            result.salt = com.android.server.locksettings.SecureRandomUtils.randomBytes(16);
            return result;
        }

        public static boolean isBadFormatFromAndroid14Beta(byte[] data) {
            return data != null && data.length >= 2 && data[0] == 0 && data[1] == 2;
        }

        public static com.android.server.locksettings.SyntheticPasswordManager.PasswordData fromBytes(byte[] data) {
            com.android.server.locksettings.SyntheticPasswordManager.PasswordData result = new com.android.server.locksettings.SyntheticPasswordManager.PasswordData();
            java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(data.length);
            buffer.put(data, 0, data.length);
            buffer.flip();
            result.credentialType = (short) buffer.getInt();
            result.scryptLogN = buffer.get();
            result.scryptLogR = buffer.get();
            result.scryptLogP = buffer.get();
            int saltLen = buffer.getInt();
            result.salt = new byte[saltLen];
            buffer.get(result.salt);
            int handleLen = buffer.getInt();
            if (handleLen > 0) {
                result.passwordHandle = new byte[handleLen];
                buffer.get(result.passwordHandle);
            } else {
                result.passwordHandle = null;
            }
            if (buffer.remaining() >= 4) {
                result.pinLength = buffer.getInt();
            } else {
                result.pinLength = -1;
            }
            return result;
        }

        public byte[] toBytes() {
            java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(this.salt.length + 11 + 4 + (this.passwordHandle != null ? this.passwordHandle.length : 0) + 4);
            if (this.credentialType < -32768 || this.credentialType > 32767) {
                throw new java.lang.IllegalArgumentException("Unknown credential type: " + this.credentialType);
            }
            buffer.putInt(this.credentialType);
            buffer.put(this.scryptLogN);
            buffer.put(this.scryptLogR);
            buffer.put(this.scryptLogP);
            buffer.putInt(this.salt.length);
            buffer.put(this.salt);
            if (this.passwordHandle != null && this.passwordHandle.length > 0) {
                buffer.putInt(this.passwordHandle.length);
                buffer.put(this.passwordHandle);
            } else {
                buffer.putInt(0);
            }
            buffer.putInt(this.pinLength);
            return buffer.array();
        }
    }

    private static class SyntheticPasswordBlob {
        byte[] mContent;
        byte mProtectorType;
        byte mVersion;

        private SyntheticPasswordBlob() {
        }

        public static com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob create(byte version, byte protectorType, byte[] content) {
            com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob result = new com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob();
            result.mVersion = version;
            result.mProtectorType = protectorType;
            result.mContent = content;
            return result;
        }

        public static com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob fromBytes(byte[] data) {
            com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob result = new com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob();
            result.mVersion = data[0];
            result.mProtectorType = data[1];
            result.mContent = java.util.Arrays.copyOfRange(data, 2, data.length);
            return result;
        }

        public byte[] toByte() {
            byte[] blob = new byte[this.mContent.length + 1 + 1];
            blob[0] = this.mVersion;
            blob[1] = this.mProtectorType;
            java.lang.System.arraycopy(this.mContent, 0, blob, 2, this.mContent.length);
            return blob;
        }
    }

    private static class TokenData {
        byte[] aggregatedSecret;
        com.android.internal.widget.LockPatternUtils.EscrowTokenStateChangeCallback mCallback;
        int mType;
        byte[] secdiscardableOnDisk;
        byte[] weaverSecret;

        private TokenData() {
        }
    }

    public SyntheticPasswordManager(android.content.Context context, com.android.server.locksettings.LockSettingsStorage storage, android.os.UserManager userManager, com.android.server.locksettings.PasswordSlotManager passwordSlotManager) {
        this.mContext = context;
        this.mStorage = storage;
        this.mUserManager = userManager;
        this.mPasswordSlotManager = passwordSlotManager;
    }

    private boolean isDeviceProvisioned() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
    }

    protected android.hardware.weaver.V1_0.IWeaver getWeaverHidlService() throws android.os.RemoteException {
        try {
            return android.hardware.weaver.V1_0.IWeaver.getService(true);
        } catch (java.util.NoSuchElementException e) {
            return null;
        }
    }

    private class WeaverDiedRecipient implements android.os.IBinder.DeathRecipient {
        private WeaverDiedRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Slog.wtf(com.android.server.locksettings.SyntheticPasswordManager.TAG, "Weaver service has died");
            com.android.server.locksettings.SyntheticPasswordManager.this.mWeaver.asBinder().unlinkToDeath(this, 0);
            com.android.server.locksettings.SyntheticPasswordManager.this.mWeaver = null;
        }
    }

    private android.hardware.weaver.IWeaver getWeaverAidlService() {
        try {
            android.hardware.weaver.IWeaver aidlWeaver = android.hardware.weaver.IWeaver.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(android.hardware.weaver.IWeaver.DESCRIPTOR + "/default"));
            if (aidlWeaver == null) {
                return null;
            }
            try {
                int aidlVersion = aidlWeaver.getInterfaceVersion();
                if (aidlVersion < 2) {
                    android.util.Slog.w(TAG, "Ignoring AIDL weaver service v" + aidlVersion + " because only v2 and later are supported");
                    return null;
                }
                android.util.Slog.i(TAG, "Found AIDL weaver service v" + aidlVersion);
                return aidlWeaver;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Cannot get AIDL weaver service version", e);
                return null;
            }
        } catch (java.lang.SecurityException e2) {
            android.util.Slog.w(TAG, "Does not have permissions to get AIDL weaver service");
            return null;
        }
    }

    private android.hardware.weaver.IWeaver getWeaverServiceInternal() {
        android.hardware.weaver.IWeaver aidlWeaver = getWeaverAidlService();
        if (aidlWeaver != null) {
            android.util.Slog.i(TAG, "Using AIDL weaver service");
            try {
                aidlWeaver.asBinder().linkToDeath(new com.android.server.locksettings.SyntheticPasswordManager.WeaverDiedRecipient(), 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Unable to register Weaver death recipient", e);
            }
            return aidlWeaver;
        }
        try {
            android.hardware.weaver.V1_0.IWeaver hidlWeaver = getWeaverHidlService();
            if (hidlWeaver != null) {
                android.util.Slog.i(TAG, "Using HIDL weaver service");
                return new com.android.server.locksettings.WeaverHidlAdapter(hidlWeaver);
            }
        } catch (android.os.RemoteException e2) {
            android.util.Slog.w(TAG, "Failed to get HIDL weaver service.", e2);
        }
        android.util.Slog.w(TAG, "Device does not support weaver");
        return null;
    }

    public boolean isAutoPinConfirmationFeatureAvailable() {
        return com.android.internal.widget.LockPatternUtils.isAutoPinConfirmFeatureAvailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized android.hardware.weaver.IWeaver getWeaverService() {
        android.hardware.weaver.IWeaver weaver = this.mWeaver;
        if (weaver != null) {
            return weaver;
        }
        android.hardware.weaver.IWeaver weaver2 = getWeaverServiceInternal();
        if (weaver2 == null) {
            return null;
        }
        try {
            android.hardware.weaver.WeaverConfig weaverConfig = weaver2.getConfig();
            if (weaverConfig != null && weaverConfig.slots > 0) {
                this.mWeaver = weaver2;
                this.mWeaverConfig = weaverConfig;
                this.mPasswordSlotManager.refreshActiveSlots(getUsedWeaverSlots());
                android.util.Slog.i(TAG, "Weaver service initialized");
                return weaver2;
            }
            android.util.Slog.e(TAG, "Invalid weaver config");
            return null;
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            android.util.Slog.e(TAG, "Failed to get weaver config", e);
            return null;
        }
    }

    private byte[] weaverEnroll(android.hardware.weaver.IWeaver weaver, int slot, byte[] key, byte[] value) {
        if (slot == -1 || slot >= this.mWeaverConfig.slots) {
            throw new java.lang.IllegalArgumentException("Invalid slot for weaver");
        }
        if (key == null) {
            key = new byte[this.mWeaverConfig.keySize];
        } else if (key.length != this.mWeaverConfig.keySize) {
            throw new java.lang.IllegalArgumentException("Invalid key size for weaver");
        }
        if (value == null) {
            value = com.android.server.locksettings.SecureRandomUtils.randomBytes(this.mWeaverConfig.valueSize);
        }
        try {
            weaver.write(slot, key, value);
            return value;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "weaver write binder call failed, slot: " + slot, e);
            return null;
        } catch (android.os.ServiceSpecificException e2) {
            android.util.Slog.e(TAG, "weaver write failed, slot: " + slot, e2);
            return null;
        }
    }

    private static com.android.internal.widget.VerifyCredentialResponse responseFromTimeout(android.hardware.weaver.WeaverReadResponse response) {
        int timeout;
        if (response.timeout > 2147483647L || response.timeout < 0) {
            timeout = Integer.MAX_VALUE;
        } else {
            timeout = (int) response.timeout;
        }
        return com.android.internal.widget.VerifyCredentialResponse.fromTimeout(timeout);
    }

    private com.android.internal.widget.VerifyCredentialResponse weaverVerify(android.hardware.weaver.IWeaver weaver, int slot, byte[] key) {
        if (slot == -1 || slot >= this.mWeaverConfig.slots) {
            throw new java.lang.IllegalArgumentException("Invalid slot for weaver");
        }
        if (key == null) {
            key = new byte[this.mWeaverConfig.keySize];
        } else if (key.length != this.mWeaverConfig.keySize) {
            throw new java.lang.IllegalArgumentException("Invalid key size for weaver");
        }
        try {
            android.hardware.weaver.WeaverReadResponse readResponse = weaver.read(slot, key);
            switch (readResponse.status) {
                case 0:
                    return new com.android.internal.widget.VerifyCredentialResponse.Builder().setGatekeeperHAT(readResponse.value).build();
                case 1:
                    android.util.Slog.e(TAG, "weaver read failed (FAILED), slot: " + slot);
                    return com.android.internal.widget.VerifyCredentialResponse.ERROR;
                case 2:
                    if (readResponse.timeout == 0) {
                        android.util.Slog.e(TAG, "weaver read failed (INCORRECT_KEY), slot: " + slot);
                        return com.android.internal.widget.VerifyCredentialResponse.ERROR;
                    }
                    android.util.Slog.e(TAG, "weaver read failed (INCORRECT_KEY/THROTTLE), slot: " + slot);
                    return responseFromTimeout(readResponse);
                case 3:
                    android.util.Slog.e(TAG, "weaver read failed (THROTTLE), slot: " + slot);
                    return responseFromTimeout(readResponse);
                default:
                    android.util.Slog.e(TAG, "weaver read unknown status " + readResponse.status + ", slot: " + slot);
                    return com.android.internal.widget.VerifyCredentialResponse.ERROR;
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "weaver read failed, slot: " + slot, e);
            return com.android.internal.widget.VerifyCredentialResponse.ERROR;
        }
    }

    public void removeUser(android.service.gatekeeper.IGateKeeperService gatekeeper, int userId) {
        java.util.Iterator<java.lang.Long> it = this.mStorage.listSyntheticPasswordProtectorsForUser(SP_BLOB_NAME, userId).iterator();
        while (it.hasNext()) {
            long protectorId = it.next().longValue();
            destroyWeaverSlot(protectorId, userId);
            destroyProtectorKey(getProtectorKeyAlias(protectorId));
        }
        try {
            gatekeeper.clearSecureUserId(fakeUserId(userId));
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to clear SID from gatekeeper");
        }
    }

    int getPinLength(long protectorId, int userId) {
        byte[] passwordData = loadState(PASSWORD_DATA_NAME, protectorId, userId);
        if (passwordData == null) {
            return -1;
        }
        return com.android.server.locksettings.SyntheticPasswordManager.PasswordData.fromBytes(passwordData).pinLength;
    }

    int getCredentialType(long protectorId, int userId) {
        byte[] passwordData = loadState(PASSWORD_DATA_NAME, protectorId, userId);
        if (passwordData == null) {
            return -1;
        }
        return com.android.server.locksettings.SyntheticPasswordManager.PasswordData.fromBytes(passwordData).credentialType;
    }

    int getSpecialUserCredentialType(int userId) {
        com.android.server.locksettings.LockSettingsStorage.PersistentData data = getSpecialUserPersistentData(userId);
        if ((data.type != 1 && data.type != 2) || data.payload == null) {
            return -1;
        }
        int credentialType = com.android.server.locksettings.SyntheticPasswordManager.PasswordData.fromBytes(data.payload).credentialType;
        if (credentialType != 2) {
            return credentialType;
        }
        return com.android.internal.widget.LockPatternUtils.pinOrPasswordQualityToCredentialType(data.qualityForUi);
    }

    private com.android.server.locksettings.LockSettingsStorage.PersistentData getSpecialUserPersistentData(int userId) {
        if (userId == -9999) {
            return this.mStorage.readPersistentDataBlock();
        }
        if (userId == -9998) {
            return this.mStorage.readRepairModePersistentData();
        }
        throw new java.lang.IllegalArgumentException("Unknown special user id " + userId);
    }

    com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword newSyntheticPassword(int userId) {
        clearSidForUser(userId);
        com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword result = com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword.create();
        saveEscrowData(result, userId);
        return result;
    }

    public void newSidForUser(android.service.gatekeeper.IGateKeeperService gatekeeper, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userId) {
        try {
            android.service.gatekeeper.GateKeeperResponse response = gatekeeper.enroll(userId, (byte[]) null, (byte[]) null, sp.deriveGkPassword());
            android.util.Slog.d(TAG, "[newSidForUser]  userId = " + userId);
            if (response.getResponseCode() != 0) {
                throw new java.lang.IllegalStateException("Fail to create new SID for user " + userId + " response: " + response.getResponseCode());
            }
            saveSyntheticPasswordHandle(response.getPayload(), userId);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Failed to create new SID for user", e);
        }
    }

    public void clearSidForUser(int userId) {
        destroyState(SP_HANDLE_NAME, 0L, userId);
    }

    public boolean hasSidForUser(int userId) {
        return hasState(SP_HANDLE_NAME, 0L, userId);
    }

    private byte[] loadSyntheticPasswordHandle(int userId) {
        return loadState(SP_HANDLE_NAME, 0L, userId);
    }

    private void saveSyntheticPasswordHandle(byte[] spHandle, int userId) {
        saveState(SP_HANDLE_NAME, spHandle, 0L, userId);
        syncState(userId);
    }

    private boolean loadEscrowData(com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userId) {
        byte[] e0 = loadState(SP_E0_NAME, 0L, userId);
        byte[] p1 = loadState(SP_P1_NAME, 0L, userId);
        sp.setEscrowData(e0, p1);
        return (e0 == null || p1 == null) ? false : true;
    }

    private void saveEscrowData(com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userId) {
        saveState(SP_E0_NAME, sp.mEncryptedEscrowSplit0, 0L, userId);
        saveState(SP_P1_NAME, sp.mEscrowSplit1, 0L, userId);
    }

    public boolean hasEscrowData(int userId) {
        return hasState(SP_E0_NAME, 0L, userId) && hasState(SP_P1_NAME, 0L, userId);
    }

    public boolean hasAnyEscrowData(int userId) {
        return hasState(SP_E0_NAME, 0L, userId) || hasState(SP_P1_NAME, 0L, userId);
    }

    public void destroyEscrowData(int userId) {
        destroyState(SP_E0_NAME, 0L, userId);
        destroyState(SP_P1_NAME, 0L, userId);
    }

    private int loadWeaverSlot(long protectorId, int userId) {
        byte[] data = loadState(WEAVER_SLOT_NAME, protectorId, userId);
        if (data == null || data.length != 5) {
            return -1;
        }
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(5);
        buffer.put(data, 0, data.length);
        buffer.flip();
        if (buffer.get() != 1) {
            android.util.Slog.e(TAG, "Invalid weaver slot version for protector " + protectorId);
            return -1;
        }
        return buffer.getInt();
    }

    private void saveWeaverSlot(int slot, long protectorId, int userId) {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(5);
        buffer.put((byte) 1);
        buffer.putInt(slot);
        saveState(WEAVER_SLOT_NAME, buffer.array(), protectorId, userId);
    }

    private void destroyWeaverSlot(long protectorId, int userId) {
        int slot = loadWeaverSlot(protectorId, userId);
        destroyState(WEAVER_SLOT_NAME, protectorId, userId);
        if (slot != -1) {
            android.hardware.weaver.IWeaver weaver = getWeaverService();
            if (weaver == null) {
                android.util.Slog.e(TAG, "Cannot erase Weaver slot because Weaver is unavailable");
                return;
            }
            java.util.Set<java.lang.Integer> usedSlots = getUsedWeaverSlots();
            if (!usedSlots.contains(java.lang.Integer.valueOf(slot))) {
                com.android.server.utils.Slogf.i(TAG, "Erasing Weaver slot %d", java.lang.Integer.valueOf(slot));
                weaverEnroll(weaver, slot, null, null);
                this.mPasswordSlotManager.markSlotDeleted(slot);
                return;
            }
            com.android.server.utils.Slogf.i(TAG, "Weaver slot %d was already reused; not erasing it", java.lang.Integer.valueOf(slot));
        }
    }

    private java.util.Set<java.lang.Integer> getUsedWeaverSlots() {
        java.util.Map<java.lang.Integer, java.util.List<java.lang.Long>> protectorIds = this.mStorage.listSyntheticPasswordProtectorsForAllUsers(WEAVER_SLOT_NAME);
        java.util.HashSet<java.lang.Integer> slots = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.Integer, java.util.List<java.lang.Long>> entry : protectorIds.entrySet()) {
            for (java.lang.Long protectorId : entry.getValue()) {
                int slot = loadWeaverSlot(protectorId.longValue(), entry.getKey().intValue());
                slots.add(java.lang.Integer.valueOf(slot));
            }
        }
        return slots;
    }

    private int getNextAvailableWeaverSlot() {
        com.android.server.locksettings.LockSettingsStorage.PersistentData persistentData;
        java.util.Set<java.lang.Integer> usedSlots = getUsedWeaverSlots();
        usedSlots.addAll(this.mPasswordSlotManager.getUsedSlots());
        if (!isDeviceProvisioned() && (persistentData = this.mStorage.readPersistentDataBlock()) != null && persistentData.type == 2) {
            int slot = persistentData.userId;
            usedSlots.add(java.lang.Integer.valueOf(slot));
        }
        for (int i = 0; i < this.mWeaverConfig.slots; i++) {
            if (!usedSlots.contains(java.lang.Integer.valueOf(i))) {
                return i;
            }
        }
        throw new java.lang.IllegalStateException("Run out of weaver slots.");
    }

    public long createLskfBasedProtector(android.service.gatekeeper.IGateKeeperService gatekeeper, com.android.internal.widget.LockscreenCredential credential, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userId) {
        int pinLength;
        byte[] protectorSecret;
        long sid;
        long protectorId = generateProtectorId();
        if (isAutoPinConfirmationFeatureAvailable()) {
            int pinLength2 = derivePinLength(credential.size(), credential.isPin(), userId);
            pinLength = pinLength2;
        } else {
            pinLength = -1;
        }
        com.android.server.locksettings.SyntheticPasswordManager.PasswordData pwd = credential.isNone() ? null : com.android.server.locksettings.SyntheticPasswordManager.PasswordData.create(credential.getType(), pinLength);
        byte[] stretchedLskf = stretchLskf(credential, pwd);
        long sid2 = 0;
        com.android.server.utils.Slogf.i(TAG, "Creating LSKF-based protector %016x for user %d", java.lang.Long.valueOf(protectorId), java.lang.Integer.valueOf(userId));
        android.hardware.weaver.IWeaver weaver = getWeaverService();
        if (weaver != null) {
            int weaverSlot = getNextAvailableWeaverSlot();
            com.android.server.utils.Slogf.i(TAG, "Enrolling LSKF for user %d into Weaver slot %d", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(weaverSlot));
            byte[] weaverSecret = weaverEnroll(weaver, weaverSlot, stretchedLskfToWeaverKey(stretchedLskf), null);
            if (weaverSecret == null) {
                throw new java.lang.IllegalStateException("Fail to enroll user password under weaver " + userId);
            }
            saveWeaverSlot(weaverSlot, protectorId, userId);
            this.mPasswordSlotManager.markSlotInUse(weaverSlot);
            synchronizeWeaverFrpPassword(pwd, 0, userId, weaverSlot);
            protectorSecret = transformUnderWeaverSecret(stretchedLskf, weaverSecret);
            sid = 0;
        } else {
            if (!credential.isNone()) {
                try {
                    gatekeeper.clearSecureUserId(fakeUserId(userId));
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to clear SID from gatekeeper");
                }
                com.android.server.utils.Slogf.i(TAG, "Enrolling LSKF for user %d into Gatekeeper", java.lang.Integer.valueOf(userId));
                try {
                    android.service.gatekeeper.GateKeeperResponse response = gatekeeper.enroll(fakeUserId(userId), (byte[]) null, (byte[]) null, stretchedLskfToGkPassword(stretchedLskf));
                    if (response.getResponseCode() != 0) {
                        throw new java.lang.IllegalStateException("Failed to enroll LSKF for new SP protector for user " + userId);
                    }
                    pwd.passwordHandle = response.getPayload();
                    sid2 = sidFromPasswordHandle(pwd.passwordHandle);
                } catch (android.os.RemoteException e2) {
                    throw new java.lang.IllegalStateException("Failed to enroll LSKF for new SP protector for user " + userId, e2);
                }
            }
            protectorSecret = transformUnderSecdiscardable(stretchedLskf, createSecdiscardable(protectorId, userId));
            synchronizeGatekeeperFrpPassword(pwd, 0, userId);
            sid = sid2;
        }
        if (!credential.isNone()) {
            saveState(PASSWORD_DATA_NAME, pwd.toBytes(), protectorId, userId);
            savePasswordMetrics(credential, sp, protectorId, userId);
        }
        createSyntheticPasswordBlob(protectorId, (byte) 0, sp, protectorSecret, sid, userId);
        this.mSyntheticPasswordManagerExt.updateCreateParam(this.mContext, credential.getCredential(), pwd, userId, protectorId, credential.size());
        syncState(userId);
        return protectorId;
    }

    private int derivePinLength(int sizeOfCredential, boolean isPinCredential, int userId) {
        if (!isPinCredential || !this.mStorage.isAutoPinConfirmSettingEnabled(userId) || sizeOfCredential < 6) {
            return -1;
        }
        return sizeOfCredential;
    }

    public com.android.internal.widget.VerifyCredentialResponse verifySpecialUserCredential(int sourceUserId, android.service.gatekeeper.IGateKeeperService gatekeeper, com.android.internal.widget.LockscreenCredential userCredential, com.android.internal.widget.ICheckCredentialProgressCallback progressCallback) {
        com.android.server.locksettings.LockSettingsStorage.PersistentData persistentData = getSpecialUserPersistentData(sourceUserId);
        if (persistentData.type == 1) {
            com.android.server.locksettings.SyntheticPasswordManager.PasswordData pwd = com.android.server.locksettings.SyntheticPasswordManager.PasswordData.fromBytes(persistentData.payload);
            byte[] stretchedLskf = stretchLskf(userCredential, pwd);
            try {
                android.service.gatekeeper.GateKeeperResponse response = gatekeeper.verifyChallenge(fakeUserId(persistentData.userId), 0L, pwd.passwordHandle, stretchedLskfToGkPassword(stretchedLskf));
                return com.android.internal.widget.VerifyCredentialResponse.fromGateKeeperResponse(response);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Persistent data credential verifyChallenge failed", e);
                return com.android.internal.widget.VerifyCredentialResponse.ERROR;
            }
        }
        if (persistentData.type == 2) {
            android.hardware.weaver.IWeaver weaver = getWeaverService();
            if (weaver == null) {
                android.util.Slog.e(TAG, "No weaver service to verify SP-based persistent data credential");
                return com.android.internal.widget.VerifyCredentialResponse.ERROR;
            }
            byte[] stretchedLskf2 = stretchLskf(userCredential, com.android.server.locksettings.SyntheticPasswordManager.PasswordData.fromBytes(persistentData.payload));
            int weaverSlot = persistentData.userId;
            return weaverVerify(weaver, weaverSlot, stretchedLskfToWeaverKey(stretchedLskf2)).stripPayload();
        }
        android.util.Slog.e(TAG, "persistentData.type must be TYPE_SP_GATEKEEPER or TYPE_SP_WEAVER, but is " + persistentData.type);
        return com.android.internal.widget.VerifyCredentialResponse.ERROR;
    }

    public void migrateFrpPasswordLocked(long protectorId, android.content.pm.UserInfo userInfo, int requestedQuality) {
        if (this.mStorage.getPersistentDataBlockManager() != null && com.android.internal.widget.LockPatternUtils.userOwnsFrpCredential(this.mContext, userInfo) && getCredentialType(protectorId, userInfo.id) != -1) {
            android.util.Slog.i(TAG, "Migrating FRP credential to persistent data block");
            com.android.server.locksettings.SyntheticPasswordManager.PasswordData pwd = com.android.server.locksettings.SyntheticPasswordManager.PasswordData.fromBytes(loadState(PASSWORD_DATA_NAME, protectorId, userInfo.id));
            int weaverSlot = loadWeaverSlot(protectorId, userInfo.id);
            if (weaverSlot != -1) {
                synchronizeWeaverFrpPassword(pwd, requestedQuality, userInfo.id, weaverSlot);
            } else {
                synchronizeGatekeeperFrpPassword(pwd, requestedQuality, userInfo.id);
            }
        }
    }

    private static boolean isNoneCredential(com.android.server.locksettings.SyntheticPasswordManager.PasswordData pwd) {
        return pwd == null || pwd.credentialType == -1;
    }

    private boolean shouldSynchronizeFrpCredential(com.android.server.locksettings.SyntheticPasswordManager.PasswordData pwd, int userId) {
        if (this.mStorage.getPersistentDataBlockManager() == null) {
            return false;
        }
        android.content.pm.UserInfo userInfo = this.mUserManager.getUserInfo(userId);
        if (!com.android.internal.widget.LockPatternUtils.userOwnsFrpCredential(this.mContext, userInfo)) {
            return false;
        }
        if (isNoneCredential(pwd) && !isDeviceProvisioned()) {
            android.util.Slog.d(TAG, "Not clearing FRP credential yet because device is not yet provisioned");
            return false;
        }
        return true;
    }

    private void synchronizeGatekeeperFrpPassword(com.android.server.locksettings.SyntheticPasswordManager.PasswordData pwd, int requestedQuality, int userId) {
        if (shouldSynchronizeFrpCredential(pwd, userId)) {
            com.android.server.utils.Slogf.d(TAG, "Syncing Gatekeeper-based FRP credential tied to user %d", java.lang.Integer.valueOf(userId));
            if (!isNoneCredential(pwd)) {
                this.mStorage.writePersistentDataBlock(1, userId, requestedQuality, pwd.toBytes());
            } else {
                this.mStorage.writePersistentDataBlock(0, userId, 0, null);
            }
        }
    }

    private void synchronizeWeaverFrpPassword(com.android.server.locksettings.SyntheticPasswordManager.PasswordData pwd, int requestedQuality, int userId, int weaverSlot) {
        if (shouldSynchronizeFrpCredential(pwd, userId)) {
            com.android.server.utils.Slogf.d(TAG, "Syncing Weaver-based FRP credential tied to user %d", java.lang.Integer.valueOf(userId));
            if (!isNoneCredential(pwd)) {
                this.mStorage.writePersistentDataBlock(2, weaverSlot, requestedQuality, pwd.toBytes());
            } else {
                this.mStorage.writePersistentDataBlock(0, 0, 0, null);
            }
        }
    }

    public boolean writeRepairModeCredentialLocked(long protectorId, int userId) {
        if (!shouldWriteRepairModeCredential(userId)) {
            return false;
        }
        byte[] data = loadState(PASSWORD_DATA_NAME, protectorId, userId);
        if (data == null) {
            com.android.server.utils.Slogf.w(TAG, "Password data not found for user %d", java.lang.Integer.valueOf(userId));
            return false;
        }
        com.android.server.locksettings.SyntheticPasswordManager.PasswordData pwd = com.android.server.locksettings.SyntheticPasswordManager.PasswordData.fromBytes(data);
        if (isNoneCredential(pwd)) {
            com.android.server.utils.Slogf.w(TAG, "User %d has NONE credential", java.lang.Integer.valueOf(userId));
            return false;
        }
        com.android.server.utils.Slogf.d(TAG, "Writing repair mode credential tied to user %d", java.lang.Integer.valueOf(userId));
        int weaverSlot = loadWeaverSlot(protectorId, userId);
        if (weaverSlot != -1) {
            this.mStorage.writeRepairModePersistentData(2, weaverSlot, pwd.toBytes());
        } else {
            this.mStorage.writeRepairModePersistentData(1, userId, pwd.toBytes());
        }
        return true;
    }

    private boolean shouldWriteRepairModeCredential(int userId) {
        android.content.pm.UserInfo userInfo = this.mUserManager.getUserInfo(userId);
        if (!com.android.internal.widget.LockPatternUtils.canUserEnterRepairMode(this.mContext, userInfo)) {
            com.android.server.utils.Slogf.w(TAG, "User %d can't enter repair mode", java.lang.Integer.valueOf(userId));
            return false;
        }
        if (com.android.internal.widget.LockPatternUtils.isRepairModeActive(this.mContext)) {
            android.util.Slog.w(TAG, "Can't write repair mode credential while repair mode is already active");
            return false;
        }
        if (com.android.internal.widget.LockPatternUtils.isGsiRunning()) {
            android.util.Slog.w(TAG, "Can't write repair mode credential while GSI is running");
            return false;
        }
        return true;
    }

    public long addPendingToken(byte[] token, int type, int userId, com.android.internal.widget.LockPatternUtils.EscrowTokenStateChangeCallback changeCallback) {
        long tokenHandle = generateProtectorId();
        if (!this.tokenMap.containsKey(java.lang.Integer.valueOf(userId))) {
            this.tokenMap.put(java.lang.Integer.valueOf(userId), new android.util.ArrayMap<>());
        }
        com.android.server.locksettings.SyntheticPasswordManager.TokenData tokenData = new com.android.server.locksettings.SyntheticPasswordManager.TokenData();
        tokenData.mType = type;
        byte[] secdiscardable = com.android.server.locksettings.SecureRandomUtils.randomBytes(16384);
        if (getWeaverService() != null) {
            tokenData.weaverSecret = com.android.server.locksettings.SecureRandomUtils.randomBytes(this.mWeaverConfig.valueSize);
            tokenData.secdiscardableOnDisk = com.android.server.locksettings.SyntheticPasswordCrypto.encrypt(tokenData.weaverSecret, PERSONALIZATION_WEAVER_TOKEN, secdiscardable);
        } else {
            tokenData.secdiscardableOnDisk = secdiscardable;
            tokenData.weaverSecret = null;
        }
        tokenData.aggregatedSecret = transformUnderSecdiscardable(token, secdiscardable);
        tokenData.mCallback = changeCallback;
        this.tokenMap.get(java.lang.Integer.valueOf(userId)).put(java.lang.Long.valueOf(tokenHandle), tokenData);
        return tokenHandle;
    }

    public java.util.Set<java.lang.Long> getPendingTokensForUser(int userId) {
        if (!this.tokenMap.containsKey(java.lang.Integer.valueOf(userId))) {
            return java.util.Collections.emptySet();
        }
        android.util.Slog.d(TAG, "[getPendingTokensForUser]  userId = " + userId);
        return new android.util.ArraySet(this.tokenMap.get(java.lang.Integer.valueOf(userId)).keySet());
    }

    public boolean removePendingToken(long tokenHandle, int userId) {
        return this.tokenMap.containsKey(java.lang.Integer.valueOf(userId)) && this.tokenMap.get(java.lang.Integer.valueOf(userId)).remove(java.lang.Long.valueOf(tokenHandle)) != null;
    }

    public boolean createTokenBasedProtector(long tokenHandle, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userId) {
        com.android.server.locksettings.SyntheticPasswordManager.TokenData tokenData;
        if (!this.tokenMap.containsKey(java.lang.Integer.valueOf(userId)) || (tokenData = this.tokenMap.get(java.lang.Integer.valueOf(userId)).get(java.lang.Long.valueOf(tokenHandle))) == null) {
            return false;
        }
        if (!loadEscrowData(sp, userId)) {
            android.util.Slog.w(TAG, "User is not escrowable");
            return false;
        }
        com.android.server.utils.Slogf.i(TAG, "Creating token-based protector %016x for user %d", java.lang.Long.valueOf(tokenHandle), java.lang.Integer.valueOf(userId));
        android.hardware.weaver.IWeaver weaver = getWeaverService();
        if (weaver != null) {
            int slot = getNextAvailableWeaverSlot();
            com.android.server.utils.Slogf.i(TAG, "Using Weaver slot %d for new token-based protector", java.lang.Integer.valueOf(slot));
            if (weaverEnroll(weaver, slot, null, tokenData.weaverSecret) == null) {
                android.util.Slog.e(TAG, "Failed to enroll weaver secret when activating token");
                return false;
            }
            saveWeaverSlot(slot, tokenHandle, userId);
            this.mPasswordSlotManager.markSlotInUse(slot);
        }
        saveSecdiscardable(tokenHandle, tokenData.secdiscardableOnDisk, userId);
        createSyntheticPasswordBlob(tokenHandle, getTokenBasedProtectorType(tokenData.mType), sp, tokenData.aggregatedSecret, 0L, userId);
        syncState(userId);
        this.tokenMap.get(java.lang.Integer.valueOf(userId)).remove(java.lang.Long.valueOf(tokenHandle));
        if (tokenData.mCallback != null) {
            tokenData.mCallback.onEscrowTokenActivated(tokenHandle, userId);
        }
        android.util.Slog.d(TAG, "[activateTokenBasedSyntheticPassword]  userId = " + userId + ", tokenHandle = " + tokenHandle);
        return true;
    }

    private void createSyntheticPasswordBlob(long protectorId, byte protectorType, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, byte[] protectorSecret, long sid, int userId) {
        byte[] spSecret;
        if (protectorType == 1 || protectorType == 2) {
            spSecret = sp.getEscrowSecret();
        } else {
            spSecret = sp.getSyntheticPassword();
        }
        byte[] content = createSpBlob(getProtectorKeyAlias(protectorId), spSecret, protectorSecret, sid);
        byte version = sp.mVersion == 3 ? (byte) 3 : (byte) 2;
        com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob blob = com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob.create(version, protectorType, content);
        saveState(SP_BLOB_NAME, blob.toByte(), protectorId, userId);
    }

    /* JADX WARN: Removed duplicated region for block: B:66:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01d1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult unlockLskfBasedProtector(android.service.gatekeeper.IGateKeeperService r26, long r27, com.android.internal.widget.LockscreenCredential r29, int r30, com.android.internal.widget.ICheckCredentialProgressCallback r31) {
        /*
            Method dump skipped, instruction units count: 620
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.locksettings.SyntheticPasswordManager.unlockLskfBasedProtector(android.service.gatekeeper.IGateKeeperService, long, com.android.internal.widget.LockscreenCredential, int, com.android.internal.widget.ICheckCredentialProgressCallback):com.android.server.locksettings.SyntheticPasswordManager$AuthenticationResult");
    }

    public boolean refreshPinLengthOnDisk(android.app.admin.PasswordMetrics passwordMetrics, long protectorId, int userId) {
        byte[] pwdDataBytes;
        if (!isAutoPinConfirmationFeatureAvailable() || (pwdDataBytes = loadState(PASSWORD_DATA_NAME, protectorId, userId)) == null) {
            return false;
        }
        com.android.server.locksettings.SyntheticPasswordManager.PasswordData pwd = com.android.server.locksettings.SyntheticPasswordManager.PasswordData.fromBytes(pwdDataBytes);
        int pinLength = derivePinLength(passwordMetrics.length, passwordMetrics.credType == 3, userId);
        if (pwd.pinLength != pinLength) {
            pwd.pinLength = pinLength;
            saveState(PASSWORD_DATA_NAME, pwd.toBytes(), protectorId, userId);
            syncState(userId);
        }
        return true;
    }

    public com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult unlockTokenBasedProtector(android.service.gatekeeper.IGateKeeperService gatekeeper, long protectorId, byte[] token, int userId) {
        byte[] data = loadState(SP_BLOB_NAME, protectorId, userId);
        if (data == null) {
            com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult result = new com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult();
            result.gkResponse = com.android.internal.widget.VerifyCredentialResponse.ERROR;
            com.android.server.utils.Slogf.w(TAG, "spblob not found for protector %016x, user %d", java.lang.Long.valueOf(protectorId), java.lang.Integer.valueOf(userId));
            return result;
        }
        com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob blob = com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob.fromBytes(data);
        return unlockTokenBasedProtectorInternal(gatekeeper, protectorId, blob.mProtectorType, token, userId);
    }

    public com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult unlockStrongTokenBasedProtector(android.service.gatekeeper.IGateKeeperService gatekeeper, long protectorId, byte[] token, int userId) {
        return unlockTokenBasedProtectorInternal(gatekeeper, protectorId, (byte) 1, token, userId);
    }

    public com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult unlockWeakTokenBasedProtector(android.service.gatekeeper.IGateKeeperService gatekeeper, long protectorId, byte[] token, int userId) {
        return unlockTokenBasedProtectorInternal(gatekeeper, protectorId, (byte) 2, token, userId);
    }

    private com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult unlockTokenBasedProtectorInternal(android.service.gatekeeper.IGateKeeperService gatekeeper, long protectorId, byte expectedProtectorType, byte[] token, int userId) {
        byte[] secdiscardable;
        com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult result = new com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult();
        byte[] secdiscardable2 = loadSecdiscardable(protectorId, userId);
        if (secdiscardable2 == null) {
            android.util.Slog.e(TAG, "secdiscardable file not found");
            result.gkResponse = com.android.internal.widget.VerifyCredentialResponse.ERROR;
            return result;
        }
        int slotId = loadWeaverSlot(protectorId, userId);
        if (slotId == -1) {
            secdiscardable = secdiscardable2;
        } else {
            android.hardware.weaver.IWeaver weaver = getWeaverService();
            if (weaver != null) {
                com.android.internal.widget.VerifyCredentialResponse response = weaverVerify(weaver, slotId, null);
                if (response.getResponseCode() != 0 || response.getGatekeeperHAT() == null) {
                    android.util.Slog.e(TAG, "Failed to retrieve Weaver secret when unlocking token-based protector");
                    result.gkResponse = com.android.internal.widget.VerifyCredentialResponse.ERROR;
                    return result;
                }
                secdiscardable = com.android.server.locksettings.SyntheticPasswordCrypto.decrypt(response.getGatekeeperHAT(), PERSONALIZATION_WEAVER_TOKEN, secdiscardable2);
            } else {
                android.util.Slog.e(TAG, "Protector uses Weaver, but Weaver is unavailable");
                result.gkResponse = com.android.internal.widget.VerifyCredentialResponse.ERROR;
                return result;
            }
        }
        byte[] protectorSecret = transformUnderSecdiscardable(token, secdiscardable);
        result.syntheticPassword = unwrapSyntheticPasswordBlob(protectorId, expectedProtectorType, protectorSecret, 0L, userId);
        if (result.syntheticPassword != null) {
            result.gkResponse = verifyChallenge(gatekeeper, result.syntheticPassword, 0L, userId);
            if (result.gkResponse == null) {
                result.gkResponse = com.android.internal.widget.VerifyCredentialResponse.OK;
            }
        } else {
            result.gkResponse = com.android.internal.widget.VerifyCredentialResponse.ERROR;
        }
        return result;
    }

    private com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword unwrapSyntheticPasswordBlob(long protectorId, byte expectedProtectorType, byte[] protectorSecret, long sid, int userId) {
        byte[] spSecret;
        byte[] data = loadState(SP_BLOB_NAME, protectorId, userId);
        if (data == null) {
            return null;
        }
        com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob blob = com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob.fromBytes(data);
        if (blob.mVersion != 3 && blob.mVersion != 2 && blob.mVersion != 1) {
            throw new java.lang.IllegalArgumentException("Unknown blob version: " + ((int) blob.mVersion));
        }
        if (blob.mProtectorType != expectedProtectorType) {
            throw new java.lang.IllegalArgumentException("Invalid protector type: " + ((int) blob.mProtectorType));
        }
        if (blob.mVersion != 1) {
            spSecret = decryptSpBlob(getProtectorKeyAlias(protectorId), blob.mContent, protectorSecret);
        } else {
            spSecret = com.android.server.locksettings.SyntheticPasswordCrypto.decryptBlobV1(getProtectorKeyAlias(protectorId), blob.mContent, protectorSecret);
        }
        if (spSecret == null) {
            android.util.Slog.e(TAG, "Fail to decrypt SP for user " + userId);
            return null;
        }
        com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword result = new com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword(blob.mVersion);
        if (blob.mProtectorType == 1 || blob.mProtectorType == 2) {
            if (!loadEscrowData(result, userId)) {
                android.util.Slog.e(TAG, "User is not escrowable: " + userId);
                return null;
            }
            result.recreateFromEscrow(spSecret);
        } else {
            result.recreateDirectly(spSecret);
        }
        if (blob.mVersion != 1) {
            return result;
        }
        android.util.Slog.i(TAG, "Upgrading v1 SP blob for user " + userId + ", protectorType = " + ((int) blob.mProtectorType));
        createSyntheticPasswordBlob(protectorId, blob.mProtectorType, result, protectorSecret, sid, userId);
        syncState(userId);
        return result;
    }

    public com.android.internal.widget.VerifyCredentialResponse verifyChallenge(android.service.gatekeeper.IGateKeeperService gatekeeper, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, long challenge, int userId) {
        return verifyChallengeInternal(gatekeeper, sp.deriveGkPassword(), challenge, userId);
    }

    protected com.android.internal.widget.VerifyCredentialResponse verifyChallengeInternal(android.service.gatekeeper.IGateKeeperService gatekeeper, byte[] gatekeeperPassword, long challenge, int userId) {
        android.service.gatekeeper.GateKeeperResponse response;
        byte[] spHandle = loadSyntheticPasswordHandle(userId);
        if (spHandle == null) {
            return null;
        }
        try {
            android.service.gatekeeper.GateKeeperResponse response2 = gatekeeper.verifyChallenge(userId, challenge, spHandle, gatekeeperPassword);
            int responseCode = response2.getResponseCode();
            if (responseCode == 0) {
                com.android.internal.widget.VerifyCredentialResponse result = new com.android.internal.widget.VerifyCredentialResponse.Builder().setGatekeeperHAT(response2.getPayload()).build();
                if (response2.getShouldReEnroll()) {
                    try {
                        response = gatekeeper.enroll(userId, spHandle, spHandle, gatekeeperPassword);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(TAG, "Failed to invoke gatekeeper.enroll", e);
                        response = android.service.gatekeeper.GateKeeperResponse.ERROR;
                    }
                    if (response.getResponseCode() != 0) {
                        android.util.Slog.w(TAG, "Fail to re-enroll SP handle for user " + userId);
                    } else {
                        saveSyntheticPasswordHandle(response.getPayload(), userId);
                        return verifyChallengeInternal(gatekeeper, gatekeeperPassword, challenge, userId);
                    }
                }
                return result;
            }
            if (responseCode == 1) {
                android.util.Slog.e(TAG, "Gatekeeper verification of synthetic password failed with RESPONSE_RETRY");
                return com.android.internal.widget.VerifyCredentialResponse.fromTimeout(response2.getTimeout());
            }
            android.util.Slog.e(TAG, "Gatekeeper verification of synthetic password failed with RESPONSE_ERROR");
            return com.android.internal.widget.VerifyCredentialResponse.ERROR;
        } catch (android.os.RemoteException e2) {
            android.util.Slog.e(TAG, "Fail to verify with gatekeeper " + userId, e2);
            return com.android.internal.widget.VerifyCredentialResponse.ERROR;
        }
    }

    public boolean protectorExists(long protectorId, int userId) {
        return hasState(SP_BLOB_NAME, protectorId, userId);
    }

    public void destroyTokenBasedProtector(long protectorId, int userId) {
        com.android.server.utils.Slogf.i(TAG, "Destroying token-based protector %016x for user %d", java.lang.Long.valueOf(protectorId), java.lang.Integer.valueOf(userId));
        com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob blob = com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob.fromBytes(loadState(SP_BLOB_NAME, protectorId, userId));
        destroyProtectorCommon(protectorId, userId);
        if (blob.mProtectorType == 2) {
            notifyWeakEscrowTokenRemovedListeners(protectorId, userId);
        }
    }

    public void destroyAllWeakTokenBasedProtectors(int userId) {
        java.util.List<java.lang.Long> protectorIds = this.mStorage.listSyntheticPasswordProtectorsForUser(SP_BLOB_NAME, userId);
        java.util.Iterator<java.lang.Long> it = protectorIds.iterator();
        while (it.hasNext()) {
            long protectorId = it.next().longValue();
            com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob blob = com.android.server.locksettings.SyntheticPasswordManager.SyntheticPasswordBlob.fromBytes(loadState(SP_BLOB_NAME, protectorId, userId));
            if (blob.mProtectorType == 2) {
                destroyTokenBasedProtector(protectorId, userId);
            }
        }
    }

    public void destroyLskfBasedProtector(long protectorId, int userId) {
        com.android.server.utils.Slogf.i(TAG, "Destroying LSKF-based protector %016x for user %d", java.lang.Long.valueOf(protectorId), java.lang.Integer.valueOf(userId));
        destroyProtectorCommon(protectorId, userId);
        destroyState(PASSWORD_DATA_NAME, protectorId, userId);
        destroyState(PASSWORD_METRICS_NAME, protectorId, userId);
    }

    private void destroyProtectorCommon(long protectorId, int userId) {
        destroyState(SP_BLOB_NAME, protectorId, userId);
        destroyProtectorKey(getProtectorKeyAlias(protectorId));
        destroyState(SECDISCARDABLE_NAME, protectorId, userId);
        if (hasState(WEAVER_SLOT_NAME, protectorId, userId)) {
            destroyWeaverSlot(protectorId, userId);
        }
    }

    private byte[] transformUnderWeaverSecret(byte[] data, byte[] secret) {
        byte[] weaverSecret = com.android.server.locksettings.SyntheticPasswordCrypto.personalizedHash(PERSONALIZATION_WEAVER_PASSWORD, secret);
        return com.android.internal.util.ArrayUtils.concat(new byte[][]{data, weaverSecret});
    }

    private byte[] transformUnderSecdiscardable(byte[] data, byte[] rawSecdiscardable) {
        byte[] secdiscardable = com.android.server.locksettings.SyntheticPasswordCrypto.personalizedHash(PERSONALIZATION_SECDISCARDABLE, rawSecdiscardable);
        return com.android.internal.util.ArrayUtils.concat(new byte[][]{data, secdiscardable});
    }

    private byte[] createSecdiscardable(long protectorId, int userId) {
        byte[] data = com.android.server.locksettings.SecureRandomUtils.randomBytes(16384);
        saveSecdiscardable(protectorId, data, userId);
        return data;
    }

    private void saveSecdiscardable(long protectorId, byte[] secdiscardable, int userId) {
        saveState(SECDISCARDABLE_NAME, secdiscardable, protectorId, userId);
    }

    private byte[] loadSecdiscardable(long protectorId, int userId) {
        return loadState(SECDISCARDABLE_NAME, protectorId, userId);
    }

    private byte getTokenBasedProtectorType(int type) {
        switch (type) {
            case 1:
                return (byte) 2;
            default:
                return (byte) 1;
        }
    }

    boolean hasPasswordData(long protectorId, int userId) {
        return hasState(PASSWORD_DATA_NAME, protectorId, userId);
    }

    public android.app.admin.PasswordMetrics getPasswordMetrics(com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, long protectorId, int userId) {
        byte[] encrypted = loadState(PASSWORD_METRICS_NAME, protectorId, userId);
        if (encrypted == null) {
            com.android.server.utils.Slogf.e(TAG, "Failed to read password metrics file for user %d", java.lang.Integer.valueOf(userId));
            return null;
        }
        byte[] decrypted = com.android.server.locksettings.SyntheticPasswordCrypto.decrypt(sp.deriveMetricsKey(), new byte[0], encrypted);
        if (decrypted == null) {
            com.android.server.utils.Slogf.e(TAG, "Failed to decrypt password metrics file for user %d", java.lang.Integer.valueOf(userId));
            return null;
        }
        return com.android.server.locksettings.VersionedPasswordMetrics.deserialize(decrypted).getMetrics();
    }

    private void savePasswordMetrics(com.android.internal.widget.LockscreenCredential credential, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, long protectorId, int userId) {
        byte[] encrypted = com.android.server.locksettings.SyntheticPasswordCrypto.encrypt(sp.deriveMetricsKey(), new byte[0], new com.android.server.locksettings.VersionedPasswordMetrics(credential).serialize());
        saveState(PASSWORD_METRICS_NAME, encrypted, protectorId, userId);
    }

    boolean hasPasswordMetrics(long protectorId, int userId) {
        return hasState(PASSWORD_METRICS_NAME, protectorId, userId);
    }

    private boolean hasState(java.lang.String stateName, long protectorId, int userId) {
        return !com.android.internal.util.ArrayUtils.isEmpty(loadState(stateName, protectorId, userId));
    }

    private byte[] loadState(java.lang.String stateName, long protectorId, int userId) {
        return this.mStorage.readSyntheticPasswordState(userId, protectorId, stateName);
    }

    private void saveState(java.lang.String stateName, byte[] data, long protectorId, int userId) {
        this.mStorage.writeSyntheticPasswordState(userId, protectorId, stateName, data);
    }

    private void syncState(int userId) {
        this.mStorage.syncSyntheticPasswordState(userId);
    }

    private void destroyState(java.lang.String stateName, long protectorId, int userId) {
        this.mStorage.deleteSyntheticPasswordState(userId, protectorId, stateName);
    }

    protected byte[] decryptSpBlob(java.lang.String protectorKeyAlias, byte[] blob, byte[] protectorSecret) {
        return com.android.server.locksettings.SyntheticPasswordCrypto.decryptBlob(protectorKeyAlias, blob, protectorSecret);
    }

    protected byte[] createSpBlob(java.lang.String protectorKeyAlias, byte[] data, byte[] protectorSecret, long sid) {
        return com.android.server.locksettings.SyntheticPasswordCrypto.createBlob(protectorKeyAlias, data, protectorSecret, sid);
    }

    protected void destroyProtectorKey(java.lang.String keyAlias) {
        com.android.server.locksettings.SyntheticPasswordCrypto.destroyProtectorKey(keyAlias);
    }

    private static long generateProtectorId() {
        long result;
        do {
            result = com.android.server.locksettings.SecureRandomUtils.randomLong();
        } while (result == 0);
        return result;
    }

    static int fakeUserId(int userId) {
        return 100000 + userId;
    }

    private java.lang.String getProtectorKeyAlias(long protectorId) {
        return android.text.TextUtils.formatSimple("%s%x", new java.lang.Object[]{PROTECTOR_KEY_ALIAS_PREFIX, java.lang.Long.valueOf(protectorId)});
    }

    byte[] stretchLskf(com.android.internal.widget.LockscreenCredential credential, com.android.server.locksettings.SyntheticPasswordManager.PasswordData data) {
        byte[] password = credential.isNone() ? DEFAULT_PASSWORD : credential.getCredential();
        if (data == null) {
            android.util.Slog.d(TAG, "[stretchLskf] PasswordData is null");
            com.android.internal.util.Preconditions.checkArgument(credential.isNone());
            return java.util.Arrays.copyOf(password, 32);
        }
        return scrypt(password, data.salt, 1 << data.scryptLogN, 1 << data.scryptLogR, 1 << data.scryptLogP, 32);
    }

    private byte[] stretchedLskfToGkPassword(byte[] stretchedLskf) {
        return com.android.server.locksettings.SyntheticPasswordCrypto.personalizedHash(PERSONALIZATION_USER_GK_AUTH, stretchedLskf);
    }

    private byte[] stretchedLskfToWeaverKey(byte[] stretchedLskf) {
        byte[] key = com.android.server.locksettings.SyntheticPasswordCrypto.personalizedHash(PERSONALIZATION_WEAVER_KEY, stretchedLskf);
        if (key.length < this.mWeaverConfig.keySize) {
            throw new java.lang.IllegalArgumentException("weaver key length too small");
        }
        return java.util.Arrays.copyOf(key, this.mWeaverConfig.keySize);
    }

    protected long sidFromPasswordHandle(byte[] handle) {
        return nativeSidFromPasswordHandle(handle);
    }

    protected byte[] scrypt(byte[] password, byte[] salt, int n, int r, int p, int outLen) {
        return new android.security.Scrypt().scrypt(password, salt, n, r, p, outLen);
    }

    static byte[] bytesToHex(byte[] bytes) {
        return libcore.util.HexEncoding.encodeToString(bytes).getBytes();
    }

    public boolean migrateKeyNamespace() {
        boolean success = true;
        java.util.Map<java.lang.Integer, java.util.List<java.lang.Long>> allProtectors = this.mStorage.listSyntheticPasswordProtectorsForAllUsers(SP_BLOB_NAME);
        for (java.util.List<java.lang.Long> userProtectors : allProtectors.values()) {
            java.util.Iterator<java.lang.Long> it = userProtectors.iterator();
            while (it.hasNext()) {
                long protectorId = it.next().longValue();
                success &= com.android.server.locksettings.SyntheticPasswordCrypto.migrateLockSettingsKey(getProtectorKeyAlias(protectorId));
            }
        }
        return success;
    }

    public boolean registerWeakEscrowTokenRemovedListener(com.android.internal.widget.IWeakEscrowTokenRemovedListener listener) {
        return this.mListeners.register(listener);
    }

    public boolean unregisterWeakEscrowTokenRemovedListener(com.android.internal.widget.IWeakEscrowTokenRemovedListener listener) {
        return this.mListeners.unregister(listener);
    }

    private void notifyWeakEscrowTokenRemovedListeners(long protectorId, int userId) {
        int i = this.mListeners.beginBroadcast();
        while (i > 0) {
            i--;
            try {
                try {
                    this.mListeners.getBroadcastItem(i).onWeakEscrowTokenRemoved(protectorId, userId);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Exception while notifying WeakEscrowTokenRemovedListener.", e);
                }
            } finally {
                this.mListeners.finishBroadcast();
            }
        }
    }

    public void writeVendorAuthSecret(byte[] vendorAuthSecret, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userId) {
        byte[] encrypted = com.android.server.locksettings.SyntheticPasswordCrypto.encrypt(sp.deriveVendorAuthSecretEncryptionKey(), new byte[0], vendorAuthSecret);
        saveState(VENDOR_AUTH_SECRET_NAME, encrypted, 0L, userId);
        syncState(userId);
    }

    public byte[] readVendorAuthSecret(com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userId) {
        byte[] encrypted = loadState(VENDOR_AUTH_SECRET_NAME, 0L, userId);
        if (encrypted == null) {
            return null;
        }
        return com.android.server.locksettings.SyntheticPasswordCrypto.decrypt(sp.deriveVendorAuthSecretEncryptionKey(), new byte[0], encrypted);
    }
}
