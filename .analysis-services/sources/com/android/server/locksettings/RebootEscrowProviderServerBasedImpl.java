package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class RebootEscrowProviderServerBasedImpl implements com.android.server.locksettings.RebootEscrowProviderInterface {
    private static final long DEFAULT_SERVER_BLOB_LIFETIME_IN_MILLIS = 600000;
    private static final long DEFAULT_SERVICE_TIMEOUT_IN_SECONDS = 10;
    private static final java.lang.String TAG = "RebootEscrowProviderServerBased";
    private final com.android.server.locksettings.RebootEscrowProviderServerBasedImpl.Injector mInjector;
    private byte[] mServerBlob;
    private final com.android.server.locksettings.LockSettingsStorage mStorage;

    static class Injector {
        private com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection mServiceConnection;

        Injector(android.content.Context context) {
            this.mServiceConnection = null;
            this.mServiceConnection = new com.android.server.locksettings.ResumeOnRebootServiceProvider(context).getServiceConnection();
            if (this.mServiceConnection == null) {
                android.util.Slog.e(com.android.server.locksettings.RebootEscrowProviderServerBasedImpl.TAG, "Failed to resolve resume on reboot server service.");
            }
        }

        Injector(com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection serviceConnection) {
            this.mServiceConnection = null;
            this.mServiceConnection = serviceConnection;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection getServiceConnection() {
            return this.mServiceConnection;
        }

        long getServiceTimeoutInSeconds() {
            return android.provider.DeviceConfig.getLong("ota", "server_based_service_timeout_in_seconds", com.android.server.locksettings.RebootEscrowProviderServerBasedImpl.DEFAULT_SERVICE_TIMEOUT_IN_SECONDS);
        }

        long getServerBlobLifetimeInMillis() {
            return android.provider.DeviceConfig.getLong("ota", "server_based_server_blob_lifetime_in_millis", 600000L);
        }
    }

    RebootEscrowProviderServerBasedImpl(android.content.Context context, com.android.server.locksettings.LockSettingsStorage storage) {
        this(storage, new com.android.server.locksettings.RebootEscrowProviderServerBasedImpl.Injector(context));
    }

    RebootEscrowProviderServerBasedImpl(com.android.server.locksettings.LockSettingsStorage storage, com.android.server.locksettings.RebootEscrowProviderServerBasedImpl.Injector injector) {
        this.mStorage = storage;
        this.mInjector = injector;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public int getType() {
        return 1;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public boolean hasRebootEscrowSupport() {
        return this.mInjector.getServiceConnection() != null;
    }

    private byte[] unwrapServerBlob(byte[] serverBlob, javax.crypto.SecretKey decryptionKey) throws java.util.concurrent.TimeoutException, java.io.IOException, android.os.RemoteException {
        com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection serviceConnection = this.mInjector.getServiceConnection();
        if (serviceConnection == null) {
            android.util.Slog.w(TAG, "Had reboot escrow data for users, but resume on reboot server service is unavailable");
            return null;
        }
        byte[] decryptedBlob = com.android.server.locksettings.AesEncryptionUtil.decrypt(decryptionKey, serverBlob);
        if (decryptedBlob == null) {
            android.util.Slog.w(TAG, "Decrypted server blob should not be null");
            return null;
        }
        serviceConnection.bindToService(this.mInjector.getServiceTimeoutInSeconds());
        byte[] escrowKeyBytes = serviceConnection.unwrap(decryptedBlob, this.mInjector.getServiceTimeoutInSeconds());
        serviceConnection.unbindService();
        return escrowKeyBytes;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public com.android.server.locksettings.RebootEscrowKey getAndClearRebootEscrowKey(javax.crypto.SecretKey decryptionKey) throws java.io.IOException {
        if (this.mServerBlob == null) {
            this.mServerBlob = this.mStorage.readRebootEscrowServerBlob();
        }
        this.mStorage.removeRebootEscrowServerBlob();
        if (this.mServerBlob == null) {
            android.util.Slog.w(TAG, "Failed to read reboot escrow server blob from storage");
            return null;
        }
        if (decryptionKey == null) {
            android.util.Slog.w(TAG, "Failed to decrypt the escrow key; decryption key from keystore is null.");
            return null;
        }
        android.util.Slog.i(TAG, "Loaded reboot escrow server blob from storage");
        try {
            byte[] escrowKeyBytes = unwrapServerBlob(this.mServerBlob, decryptionKey);
            if (escrowKeyBytes == null) {
                android.util.Slog.w(TAG, "Decrypted reboot escrow key bytes should not be null");
                return null;
            }
            if (escrowKeyBytes.length != 32) {
                android.util.Slog.e(TAG, "Decrypted reboot escrow key has incorrect size " + escrowKeyBytes.length);
                return null;
            }
            return com.android.server.locksettings.RebootEscrowKey.fromKeyBytes(escrowKeyBytes);
        } catch (android.os.RemoteException | java.util.concurrent.TimeoutException e) {
            android.util.Slog.w(TAG, "Failed to decrypt the server blob ", e);
            return null;
        }
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public void clearRebootEscrowKey() {
        this.mStorage.removeRebootEscrowServerBlob();
    }

    private byte[] wrapEscrowKey(byte[] escrowKeyBytes, javax.crypto.SecretKey encryptionKey) throws java.util.concurrent.TimeoutException, android.os.RemoteException, java.io.IOException {
        com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection serviceConnection = this.mInjector.getServiceConnection();
        if (serviceConnection == null) {
            android.util.Slog.w(TAG, "Failed to encrypt the reboot escrow key: resume on reboot server service is unavailable");
            return null;
        }
        serviceConnection.bindToService(this.mInjector.getServiceTimeoutInSeconds());
        byte[] serverEncryptedBlob = serviceConnection.wrapBlob(escrowKeyBytes, this.mInjector.getServerBlobLifetimeInMillis(), this.mInjector.getServiceTimeoutInSeconds());
        serviceConnection.unbindService();
        if (serverEncryptedBlob == null) {
            android.util.Slog.w(TAG, "Server encrypted reboot escrow key cannot be null");
            return null;
        }
        return com.android.server.locksettings.AesEncryptionUtil.encrypt(encryptionKey, serverEncryptedBlob);
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public boolean storeRebootEscrowKey(com.android.server.locksettings.RebootEscrowKey escrowKey, javax.crypto.SecretKey encryptionKey) {
        this.mStorage.removeRebootEscrowServerBlob();
        try {
            byte[] wrappedBlob = wrapEscrowKey(escrowKey.getKeyBytes(), encryptionKey);
            if (wrappedBlob == null) {
                android.util.Slog.w(TAG, "Failed to encrypt the reboot escrow key");
                return false;
            }
            this.mStorage.writeRebootEscrowServerBlob(wrappedBlob);
            android.util.Slog.i(TAG, "Reboot escrow key encrypted and stored.");
            return true;
        } catch (android.os.RemoteException | java.io.IOException | java.util.concurrent.TimeoutException e) {
            android.util.Slog.w(TAG, "Failed to encrypt the reboot escrow key ", e);
            return false;
        }
    }
}
