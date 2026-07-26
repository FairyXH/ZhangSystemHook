package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class RebootEscrowData {
    private static final int CURRENT_VERSION = 2;
    private static final int LEGACY_SINGLE_ENCRYPTED_VERSION = 1;
    private final byte[] mBlob;
    private final com.android.server.locksettings.RebootEscrowKey mKey;
    private final byte mSpVersion;
    private final byte[] mSyntheticPassword;

    private RebootEscrowData(byte spVersion, byte[] syntheticPassword, byte[] blob, com.android.server.locksettings.RebootEscrowKey key) {
        this.mSpVersion = spVersion;
        this.mSyntheticPassword = syntheticPassword;
        this.mBlob = blob;
        this.mKey = key;
    }

    public byte getSpVersion() {
        return this.mSpVersion;
    }

    public byte[] getSyntheticPassword() {
        return this.mSyntheticPassword;
    }

    public byte[] getBlob() {
        return this.mBlob;
    }

    public com.android.server.locksettings.RebootEscrowKey getKey() {
        return this.mKey;
    }

    private static byte[] decryptBlobCurrentVersion(javax.crypto.SecretKey kk, com.android.server.locksettings.RebootEscrowKey ks, java.io.DataInputStream dis) throws java.io.IOException {
        if (kk == null) {
            throw new java.io.IOException("Failed to find wrapper key in keystore, cannot decrypt the escrow data");
        }
        byte[] ksEncryptedBlob = com.android.server.locksettings.AesEncryptionUtil.decrypt(kk, dis);
        return com.android.server.locksettings.AesEncryptionUtil.decrypt(ks.getKey(), ksEncryptedBlob);
    }

    static com.android.server.locksettings.RebootEscrowData fromEncryptedData(com.android.server.locksettings.RebootEscrowKey ks, byte[] blob, javax.crypto.SecretKey kk) throws java.io.IOException {
        java.util.Objects.requireNonNull(ks);
        java.util.Objects.requireNonNull(blob);
        java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.ByteArrayInputStream(blob));
        int version = dis.readInt();
        byte spVersion = dis.readByte();
        switch (version) {
            case 1:
                byte[] syntheticPassword = com.android.server.locksettings.AesEncryptionUtil.decrypt(ks.getKey(), dis);
                return new com.android.server.locksettings.RebootEscrowData(spVersion, syntheticPassword, blob, ks);
            case 2:
                byte[] syntheticPassword2 = decryptBlobCurrentVersion(kk, ks, dis);
                return new com.android.server.locksettings.RebootEscrowData(spVersion, syntheticPassword2, blob, ks);
            default:
                throw new java.io.IOException("Unsupported version " + version);
        }
    }

    static com.android.server.locksettings.RebootEscrowData fromSyntheticPassword(com.android.server.locksettings.RebootEscrowKey ks, byte spVersion, byte[] syntheticPassword, javax.crypto.SecretKey kk) throws java.io.IOException {
        java.util.Objects.requireNonNull(syntheticPassword);
        byte[] ksEncryptedBlob = com.android.server.locksettings.AesEncryptionUtil.encrypt(ks.getKey(), syntheticPassword);
        byte[] kkEncryptedBlob = com.android.server.locksettings.AesEncryptionUtil.encrypt(kk, ksEncryptedBlob);
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream dos = new java.io.DataOutputStream(bos);
        dos.writeInt(2);
        dos.writeByte(spVersion);
        dos.write(kkEncryptedBlob);
        return new com.android.server.locksettings.RebootEscrowData(spVersion, syntheticPassword, bos.toByteArray(), ks);
    }
}
