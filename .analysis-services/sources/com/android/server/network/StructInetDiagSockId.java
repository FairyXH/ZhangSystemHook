package com.android.server.network;

/* JADX INFO: loaded from: classes2.dex */
public class StructInetDiagSockId {
    private static final byte[] INET_DIAG_NOCOOKIE = {-1, -1, -1, -1, -1, -1, -1, -1};
    private static final byte[] IPV4_PADDING = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final int STRUCT_SIZE = 48;
    private final java.net.InetSocketAddress mLocSocketAddress;
    private final java.net.InetSocketAddress mRemSocketAddress;

    public StructInetDiagSockId(java.net.InetSocketAddress loc, java.net.InetSocketAddress rem) {
        this.mLocSocketAddress = loc;
        this.mRemSocketAddress = rem;
    }

    public void pack(java.nio.ByteBuffer byteBuffer) {
        byteBuffer.order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.putShort((short) this.mLocSocketAddress.getPort());
        byteBuffer.putShort((short) this.mRemSocketAddress.getPort());
        byteBuffer.put(this.mLocSocketAddress.getAddress().getAddress());
        if (this.mLocSocketAddress.getAddress() instanceof java.net.Inet4Address) {
            byteBuffer.put(IPV4_PADDING);
        }
        byteBuffer.put(this.mRemSocketAddress.getAddress().getAddress());
        if (this.mRemSocketAddress.getAddress() instanceof java.net.Inet4Address) {
            byteBuffer.put(IPV4_PADDING);
        }
        byteBuffer.order(java.nio.ByteOrder.nativeOrder());
        byteBuffer.putInt(0);
        byteBuffer.put(INET_DIAG_NOCOOKIE);
    }

    public java.lang.String toString() {
        return "StructInetDiagSockId{ idiag_sport{" + this.mLocSocketAddress.getPort() + "}, idiag_dport{" + this.mRemSocketAddress.getPort() + "}, idiag_src{" + this.mLocSocketAddress.getAddress().getHostAddress() + "}, idiag_dst{" + this.mRemSocketAddress.getAddress().getHostAddress() + "}, idiag_if{0} idiag_cookie{INET_DIAG_NOCOOKIE}}";
    }
}
