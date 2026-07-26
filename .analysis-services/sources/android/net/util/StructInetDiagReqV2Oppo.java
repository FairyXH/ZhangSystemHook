package android.net.util;

/* JADX INFO: loaded from: classes.dex */
public class StructInetDiagReqV2Oppo {
    public static final int INET_DIAG_REQ_V2_ALL_STATES = -1;
    public static final int STRUCT_SIZE = 56;
    private final com.android.server.network.StructInetDiagSockId mId;
    private final byte mIdiagExt;
    private final byte mPad;
    private final byte mSdiagFamily;
    private final byte mSdiagProtocol;
    private final int mState;

    public StructInetDiagReqV2Oppo(int protocol, java.net.InetSocketAddress local, java.net.InetSocketAddress remote, int family) {
        this(protocol, local, remote, family, 0, 0, -1);
    }

    public StructInetDiagReqV2Oppo(int protocol, java.net.InetSocketAddress local, java.net.InetSocketAddress remote, int family, int pad, int extension, int state) throws java.lang.NullPointerException {
        this.mSdiagFamily = (byte) family;
        this.mSdiagProtocol = (byte) protocol;
        if ((local == null) != (remote == null)) {
            throw new java.lang.NullPointerException("Local and remote must be both null or both non-null");
        }
        this.mId = (local == null || remote == null) ? null : new com.android.server.network.StructInetDiagSockId(local, remote);
        this.mPad = (byte) pad;
        this.mIdiagExt = (byte) extension;
        this.mState = state;
    }

    public void pack(java.nio.ByteBuffer byteBuffer) {
        byteBuffer.put(this.mSdiagFamily);
        byteBuffer.put(this.mSdiagProtocol);
        byteBuffer.put(this.mIdiagExt);
        byteBuffer.put(this.mPad);
        byteBuffer.putInt(this.mState);
        if (this.mId != null) {
            this.mId.pack(byteBuffer);
        }
    }

    public static java.lang.String stringForAddressFamily(int family) {
        return family == android.system.OsConstants.AF_INET ? "AF_INET" : family == android.system.OsConstants.AF_INET6 ? "AF_INET6" : family == android.system.OsConstants.AF_NETLINK ? "AF_NETLINK" : family == android.system.OsConstants.AF_UNSPEC ? "AF_UNSPEC" : java.lang.String.valueOf(family);
    }

    public java.lang.String toString() {
        java.lang.String familyStr = stringForAddressFamily(this.mSdiagFamily);
        java.lang.String protocolStr = stringForAddressFamily(this.mSdiagProtocol);
        return "StructInetDiagReqV2Oppo{ sdiag_family{" + familyStr + "}, sdiag_protocol{" + protocolStr + "}, idiag_ext{" + ((int) this.mIdiagExt) + ")}, pad{" + ((int) this.mPad) + "}, idiag_states{" + java.lang.Integer.toHexString(this.mState) + "}, " + (this.mId != null ? this.mId.toString() : "inet_diag_sockid=null") + "}";
    }
}
