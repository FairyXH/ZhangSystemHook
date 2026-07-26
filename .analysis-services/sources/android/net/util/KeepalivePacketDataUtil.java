package android.net.util;

/* JADX INFO: loaded from: classes.dex */
public final class KeepalivePacketDataUtil {
    private static final int IPV4_HEADER_LENGTH = 20;
    private static final int IPV6_HEADER_LENGTH = 40;
    private static final java.lang.String TAG = android.net.util.KeepalivePacketDataUtil.class.getSimpleName();

    public static android.net.NattKeepalivePacketDataParcelable toStableParcelable(android.net.NattKeepalivePacketData pkt) {
        android.net.NattKeepalivePacketDataParcelable parcel = new android.net.NattKeepalivePacketDataParcelable();
        java.net.InetAddress srcAddress = pkt.getSrcAddress();
        java.net.InetAddress dstAddress = pkt.getDstAddress();
        parcel.srcAddress = srcAddress.getAddress();
        parcel.srcPort = pkt.getSrcPort();
        parcel.dstAddress = dstAddress.getAddress();
        parcel.dstPort = pkt.getDstPort();
        return parcel;
    }

    public static android.net.TcpKeepalivePacketDataParcelable toStableParcelable(android.net.TcpKeepalivePacketData pkt) {
        android.net.TcpKeepalivePacketDataParcelable parcel = new android.net.TcpKeepalivePacketDataParcelable();
        java.net.InetAddress srcAddress = pkt.getSrcAddress();
        java.net.InetAddress dstAddress = pkt.getDstAddress();
        parcel.srcAddress = srcAddress.getAddress();
        parcel.srcPort = pkt.getSrcPort();
        parcel.dstAddress = dstAddress.getAddress();
        parcel.dstPort = pkt.getDstPort();
        parcel.seq = pkt.getTcpSeq();
        parcel.ack = pkt.getTcpAck();
        parcel.rcvWnd = pkt.getTcpWindow();
        parcel.rcvWndScale = pkt.getTcpWindowScale();
        parcel.tos = pkt.getIpTos();
        parcel.ttl = pkt.getIpTtl();
        return parcel;
    }

    @java.lang.Deprecated
    public static android.net.TcpKeepalivePacketDataParcelable parseTcpKeepalivePacketData(android.net.KeepalivePacketData data) {
        if (data == null) {
            return null;
        }
        android.util.Log.wtf(TAG, "parseTcpKeepalivePacketData should not be used after R, use TcpKeepalivePacketData instead.");
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(data.getPacket());
        buffer.order(java.nio.ByteOrder.BIG_ENDIAN);
        try {
            int tcpSeq = buffer.getInt(24);
            int tcpAck = buffer.getInt(28);
            int wndSize = buffer.getShort(34);
            int ipTos = buffer.get(1);
            int ttl = buffer.get(8);
            android.net.TcpKeepalivePacketDataParcelable p = new android.net.TcpKeepalivePacketDataParcelable();
            p.srcAddress = data.getSrcAddress().getAddress();
            p.srcPort = data.getSrcPort();
            p.dstAddress = data.getDstAddress().getAddress();
            p.dstPort = data.getDstPort();
            p.seq = tcpSeq;
            p.ack = tcpAck;
            p.rcvWnd = wndSize;
            p.rcvWndScale = 0;
            p.tos = ipTos;
            p.ttl = ttl;
            return p;
        } catch (java.lang.IndexOutOfBoundsException e) {
            return null;
        }
    }
}
