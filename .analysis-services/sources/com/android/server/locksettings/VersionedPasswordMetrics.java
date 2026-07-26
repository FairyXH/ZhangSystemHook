package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class VersionedPasswordMetrics {
    private static final int VERSION_1 = 1;
    private final android.app.admin.PasswordMetrics mMetrics;
    private final int mVersion;

    private VersionedPasswordMetrics(int version, android.app.admin.PasswordMetrics metrics) {
        this.mMetrics = metrics;
        this.mVersion = version;
    }

    public VersionedPasswordMetrics(com.android.internal.widget.LockscreenCredential credential) {
        this(1, android.app.admin.PasswordMetrics.computeForCredential(credential));
    }

    public int getVersion() {
        return this.mVersion;
    }

    public android.app.admin.PasswordMetrics getMetrics() {
        return this.mMetrics;
    }

    public byte[] serialize() {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(44);
        buffer.putInt(this.mVersion);
        buffer.putInt(this.mMetrics.credType);
        buffer.putInt(this.mMetrics.length);
        buffer.putInt(this.mMetrics.letters);
        buffer.putInt(this.mMetrics.upperCase);
        buffer.putInt(this.mMetrics.lowerCase);
        buffer.putInt(this.mMetrics.numeric);
        buffer.putInt(this.mMetrics.symbols);
        buffer.putInt(this.mMetrics.nonLetter);
        buffer.putInt(this.mMetrics.nonNumeric);
        buffer.putInt(this.mMetrics.seqLength);
        return buffer.array();
    }

    public static com.android.server.locksettings.VersionedPasswordMetrics deserialize(byte[] data) {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(data.length);
        buffer.put(data, 0, data.length);
        buffer.flip();
        int version = buffer.getInt();
        android.app.admin.PasswordMetrics metrics = new android.app.admin.PasswordMetrics(buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt());
        return new com.android.server.locksettings.VersionedPasswordMetrics(version, metrics);
    }
}
