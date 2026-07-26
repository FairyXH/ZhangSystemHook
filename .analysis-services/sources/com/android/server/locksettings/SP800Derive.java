package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class SP800Derive {
    private final byte[] mKeyBytes;

    SP800Derive(byte[] keyBytes) {
        this.mKeyBytes = keyBytes;
    }

    private javax.crypto.Mac getMac() {
        try {
            javax.crypto.Mac m = javax.crypto.Mac.getInstance("HmacSHA256");
            m.init(new javax.crypto.spec.SecretKeySpec(this.mKeyBytes, m.getAlgorithm()));
            return m;
        } catch (java.security.InvalidKeyException | java.security.NoSuchAlgorithmException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private static void update32(javax.crypto.Mac m, int v) {
        m.update(java.nio.ByteBuffer.allocate(4).putInt(v).array());
    }

    public byte[] fixedInput(byte[] fixedInput) {
        javax.crypto.Mac m = getMac();
        update32(m, 1);
        m.update(fixedInput);
        return m.doFinal();
    }

    public byte[] withContext(byte[] label, byte[] context) {
        javax.crypto.Mac m = getMac();
        update32(m, 1);
        m.update(label);
        m.update((byte) 0);
        m.update(context);
        update32(m, context.length * 8);
        update32(m, 256);
        return m.doFinal();
    }
}
