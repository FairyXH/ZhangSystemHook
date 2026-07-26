package android.net.ip;

/* JADX INFO: loaded from: classes.dex */
public interface IIpClientCallbacks extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$ip$IIpClientCallbacks".replace('$', '.');
    public static final int DTIM_MULTIPLIER_RESET = 0;
    public static final java.lang.String HASH = "9bd9d687ddb816baf1faabcad0d56ac15b22c56e";
    public static final int VERSION = 21;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void installPacketFilter(byte[] bArr) throws android.os.RemoteException;

    void onIpClientCreated(android.net.ip.IIpClient iIpClient) throws android.os.RemoteException;

    void onLinkPropertiesChange(android.net.LinkProperties linkProperties) throws android.os.RemoteException;

    void onNewDhcpResults(android.net.DhcpResultsParcelable dhcpResultsParcelable) throws android.os.RemoteException;

    void onPostDhcpAction() throws android.os.RemoteException;

    void onPreDhcpAction() throws android.os.RemoteException;

    void onPreconnectionStart(java.util.List<android.net.Layer2PacketParcelable> list) throws android.os.RemoteException;

    void onProvisioningFailure(android.net.LinkProperties linkProperties) throws android.os.RemoteException;

    void onProvisioningSuccess(android.net.LinkProperties linkProperties) throws android.os.RemoteException;

    void onQuit() throws android.os.RemoteException;

    void onReachabilityFailure(android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable reachabilityLossInfoParcelable) throws android.os.RemoteException;

    void onReachabilityLost(java.lang.String str) throws android.os.RemoteException;

    void setFallbackMulticastFilter(boolean z) throws android.os.RemoteException;

    void setMaxDtimMultiplier(int i) throws android.os.RemoteException;

    void setNeighborDiscoveryOffload(boolean z) throws android.os.RemoteException;

    void startReadPacketFilter() throws android.os.RemoteException;

    public static class Default implements android.net.ip.IIpClientCallbacks {
        @Override // android.net.ip.IIpClientCallbacks
        public void onIpClientCreated(android.net.ip.IIpClient ipClient) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onPreDhcpAction() throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onPostDhcpAction() throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onNewDhcpResults(android.net.DhcpResultsParcelable dhcpResults) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onProvisioningSuccess(android.net.LinkProperties newLp) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onProvisioningFailure(android.net.LinkProperties newLp) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onLinkPropertiesChange(android.net.LinkProperties newLp) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onReachabilityLost(java.lang.String logMsg) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onQuit() throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void installPacketFilter(byte[] filter) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void startReadPacketFilter() throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void setFallbackMulticastFilter(boolean enabled) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void setNeighborDiscoveryOffload(boolean enable) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onPreconnectionStart(java.util.List<android.net.Layer2PacketParcelable> packets) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onReachabilityFailure(android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable lossInfo) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void setMaxDtimMultiplier(int multiplier) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClientCallbacks
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.ip.IIpClientCallbacks
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.ip.IIpClientCallbacks {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_installPacketFilter = 10;
        static final int TRANSACTION_onIpClientCreated = 1;
        static final int TRANSACTION_onLinkPropertiesChange = 7;
        static final int TRANSACTION_onNewDhcpResults = 4;
        static final int TRANSACTION_onPostDhcpAction = 3;
        static final int TRANSACTION_onPreDhcpAction = 2;
        static final int TRANSACTION_onPreconnectionStart = 14;
        static final int TRANSACTION_onProvisioningFailure = 6;
        static final int TRANSACTION_onProvisioningSuccess = 5;
        static final int TRANSACTION_onQuit = 9;
        static final int TRANSACTION_onReachabilityFailure = 15;
        static final int TRANSACTION_onReachabilityLost = 8;
        static final int TRANSACTION_setFallbackMulticastFilter = 12;
        static final int TRANSACTION_setMaxDtimMultiplier = 16;
        static final int TRANSACTION_setNeighborDiscoveryOffload = 13;
        static final int TRANSACTION_startReadPacketFilter = 11;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.ip.IIpClientCallbacks asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.ip.IIpClientCallbacks)) {
                return (android.net.ip.IIpClientCallbacks) iin;
            }
            return new android.net.ip.IIpClientCallbacks.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(descriptor);
            }
            if (code == 1598968902) {
                reply.writeString(descriptor);
                return true;
            }
            if (code == 16777215) {
                reply.writeNoException();
                reply.writeInt(getInterfaceVersion());
                return true;
            }
            if (code == TRANSACTION_getInterfaceHash) {
                reply.writeNoException();
                reply.writeString(getInterfaceHash());
                return true;
            }
            switch (code) {
                case 1:
                    android.net.ip.IIpClient _arg0 = android.net.ip.IIpClient.Stub.asInterface(data.readStrongBinder());
                    onIpClientCreated(_arg0);
                    return true;
                case 2:
                    onPreDhcpAction();
                    return true;
                case 3:
                    onPostDhcpAction();
                    return true;
                case 4:
                    android.net.DhcpResultsParcelable _arg02 = (android.net.DhcpResultsParcelable) data.readTypedObject(android.net.DhcpResultsParcelable.CREATOR);
                    onNewDhcpResults(_arg02);
                    return true;
                case 5:
                    android.net.LinkProperties _arg03 = (android.net.LinkProperties) data.readTypedObject(android.net.LinkProperties.CREATOR);
                    onProvisioningSuccess(_arg03);
                    return true;
                case 6:
                    android.net.LinkProperties _arg04 = (android.net.LinkProperties) data.readTypedObject(android.net.LinkProperties.CREATOR);
                    onProvisioningFailure(_arg04);
                    return true;
                case 7:
                    android.net.LinkProperties _arg05 = (android.net.LinkProperties) data.readTypedObject(android.net.LinkProperties.CREATOR);
                    onLinkPropertiesChange(_arg05);
                    return true;
                case 8:
                    java.lang.String _arg06 = data.readString();
                    onReachabilityLost(_arg06);
                    return true;
                case 9:
                    onQuit();
                    return true;
                case 10:
                    byte[] _arg07 = data.createByteArray();
                    installPacketFilter(_arg07);
                    return true;
                case 11:
                    startReadPacketFilter();
                    return true;
                case 12:
                    boolean _arg08 = data.readBoolean();
                    setFallbackMulticastFilter(_arg08);
                    return true;
                case 13:
                    boolean _arg09 = data.readBoolean();
                    setNeighborDiscoveryOffload(_arg09);
                    return true;
                case 14:
                    java.util.List<android.net.Layer2PacketParcelable> _arg010 = data.createTypedArrayList(android.net.Layer2PacketParcelable.CREATOR);
                    onPreconnectionStart(_arg010);
                    return true;
                case 15:
                    android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable _arg011 = (android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable) data.readTypedObject(android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable.CREATOR);
                    onReachabilityFailure(_arg011);
                    return true;
                case 16:
                    int _arg012 = data.readInt();
                    setMaxDtimMultiplier(_arg012);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.ip.IIpClientCallbacks {
            private android.os.IBinder mRemote;
            private int mCachedVersion = -1;
            private java.lang.String mCachedHash = "-1";

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onIpClientCreated(android.net.ip.IIpClient ipClient) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(ipClient);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onIpClientCreated is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onPreDhcpAction() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onPreDhcpAction is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onPostDhcpAction() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onPostDhcpAction is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onNewDhcpResults(android.net.DhcpResultsParcelable dhcpResults) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(dhcpResults, 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onNewDhcpResults is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onProvisioningSuccess(android.net.LinkProperties newLp) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(newLp, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onProvisioningSuccess is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onProvisioningFailure(android.net.LinkProperties newLp) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(newLp, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onProvisioningFailure is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onLinkPropertiesChange(android.net.LinkProperties newLp) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(newLp, 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onLinkPropertiesChange is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onReachabilityLost(java.lang.String logMsg) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(logMsg);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onReachabilityLost is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onQuit() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onQuit is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void installPacketFilter(byte[] filter) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByteArray(filter);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method installPacketFilter is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void startReadPacketFilter() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method startReadPacketFilter is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void setFallbackMulticastFilter(boolean enabled) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setFallbackMulticastFilter is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void setNeighborDiscoveryOffload(boolean enable) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(enable);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setNeighborDiscoveryOffload is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onPreconnectionStart(java.util.List<android.net.Layer2PacketParcelable> packets) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    android.net.ip.IIpClientCallbacks._Parcel.writeTypedList(_data, packets, 0);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onPreconnectionStart is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void onReachabilityFailure(android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable lossInfo) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(lossInfo, 0);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onReachabilityFailure is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public void setMaxDtimMultiplier(int multiplier) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(multiplier);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setMaxDtimMultiplier is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClientCallbacks
            public int getInterfaceVersion() throws android.os.RemoteException {
                if (this.mCachedVersion == -1) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(16777215, data, reply, 0);
                        reply.readException();
                        this.mCachedVersion = reply.readInt();
                    } finally {
                        reply.recycle();
                        data.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // android.net.ip.IIpClientCallbacks
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.ip.IIpClientCallbacks.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
                        reply.readException();
                        this.mCachedHash = reply.readString();
                        reply.recycle();
                        data.recycle();
                    } catch (java.lang.Throwable th) {
                        reply.recycle();
                        data.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }

    public static class _Parcel {
        /* JADX INFO: Access modifiers changed from: private */
        public static <T extends android.os.Parcelable> void writeTypedList(android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
            if (value == null) {
                parcel.writeInt(-1);
                return;
            }
            int N = value.size();
            parcel.writeInt(N);
            for (int i = 0; i < N; i++) {
                parcel.writeTypedObject(value.get(i), parcelableFlags);
            }
        }
    }
}
