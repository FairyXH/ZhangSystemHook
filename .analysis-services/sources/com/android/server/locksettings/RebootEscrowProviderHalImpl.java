package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class RebootEscrowProviderHalImpl implements com.android.server.locksettings.RebootEscrowProviderInterface {
    private static final java.lang.String TAG = "RebootEscrowProviderHal";
    private final com.android.server.locksettings.RebootEscrowProviderHalImpl.Injector mInjector;

    static class Injector {
        Injector() {
        }

        public android.hardware.rebootescrow.IRebootEscrow getRebootEscrow() {
            try {
                return android.hardware.rebootescrow.IRebootEscrow.Stub.asInterface(android.os.ServiceManager.getService("android.hardware.rebootescrow.IRebootEscrow/default"));
            } catch (java.util.NoSuchElementException e) {
                android.util.Slog.i(com.android.server.locksettings.RebootEscrowProviderHalImpl.TAG, "Device doesn't implement RebootEscrow HAL");
                return null;
            }
        }
    }

    RebootEscrowProviderHalImpl() {
        this.mInjector = new com.android.server.locksettings.RebootEscrowProviderHalImpl.Injector();
    }

    RebootEscrowProviderHalImpl(com.android.server.locksettings.RebootEscrowProviderHalImpl.Injector injector) {
        this.mInjector = injector;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public int getType() {
        return 0;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public boolean hasRebootEscrowSupport() {
        return this.mInjector.getRebootEscrow() != null;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public com.android.server.locksettings.RebootEscrowKey getAndClearRebootEscrowKey(javax.crypto.SecretKey decryptionKey) {
        android.hardware.rebootescrow.IRebootEscrow rebootEscrow = this.mInjector.getRebootEscrow();
        if (rebootEscrow == null) {
            android.util.Slog.w(TAG, "Had reboot escrow data for users, but RebootEscrow HAL is unavailable");
            return null;
        }
        try {
            byte[] escrowKeyBytes = rebootEscrow.retrieveKey();
            if (escrowKeyBytes == null) {
                android.util.Slog.w(TAG, "Had reboot escrow data for users, but could not retrieve key");
                return null;
            }
            if (escrowKeyBytes.length != 32) {
                android.util.Slog.e(TAG, "IRebootEscrow returned key of incorrect size " + escrowKeyBytes.length);
                return null;
            }
            int zero = 0;
            for (int i : escrowKeyBytes) {
                zero |= i;
            }
            if (zero == 0) {
                android.util.Slog.w(TAG, "IRebootEscrow returned an all-zeroes key");
                return null;
            }
            rebootEscrow.storeKey(new byte[32]);
            return com.android.server.locksettings.RebootEscrowKey.fromKeyBytes(escrowKeyBytes);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Could not retrieve escrow data");
            return null;
        } catch (android.os.ServiceSpecificException e2) {
            android.util.Slog.w(TAG, "Got service-specific exception: " + e2.errorCode);
            return null;
        }
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public void clearRebootEscrowKey() {
        android.hardware.rebootescrow.IRebootEscrow rebootEscrow = this.mInjector.getRebootEscrow();
        if (rebootEscrow == null) {
            return;
        }
        try {
            rebootEscrow.storeKey(new byte[32]);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            android.util.Slog.w(TAG, "Could not call RebootEscrow HAL to shred key");
        }
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public boolean storeRebootEscrowKey(com.android.server.locksettings.RebootEscrowKey escrowKey, javax.crypto.SecretKey encryptionKey) {
        android.hardware.rebootescrow.IRebootEscrow rebootEscrow = this.mInjector.getRebootEscrow();
        if (rebootEscrow == null) {
            android.util.Slog.w(TAG, "Escrow marked as ready, but RebootEscrow HAL is unavailable");
            return false;
        }
        try {
            rebootEscrow.storeKey(escrowKey.getKeyBytes());
            android.util.Slog.i(TAG, "Reboot escrow key stored with RebootEscrow HAL");
            return true;
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            android.util.Slog.e(TAG, "Failed escrow secret to RebootEscrow HAL", e);
            return false;
        }
    }
}
