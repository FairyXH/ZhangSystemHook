package android.net;

/* JADX INFO: loaded from: classes.dex */
public interface INetworkStackConnector extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$INetworkStackConnector".replace('$', '.');
    public static final java.lang.String HASH = "9bd9d687ddb816baf1faabcad0d56ac15b22c56e";
    public static final int VERSION = 21;

    void allowTestUid(int i, android.net.INetworkStackStatusCallback iNetworkStackStatusCallback) throws android.os.RemoteException;

    void fetchIpMemoryStore(android.net.IIpMemoryStoreCallbacks iIpMemoryStoreCallbacks) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void makeDhcpServer(java.lang.String str, android.net.dhcp.DhcpServingParamsParcel dhcpServingParamsParcel, android.net.dhcp.IDhcpServerCallbacks iDhcpServerCallbacks) throws android.os.RemoteException;

    void makeIpClient(java.lang.String str, android.net.ip.IIpClientCallbacks iIpClientCallbacks) throws android.os.RemoteException;

    void makeNetworkMonitor(android.net.Network network, java.lang.String str, android.net.INetworkMonitorCallbacks iNetworkMonitorCallbacks) throws android.os.RemoteException;

    public static class Default implements android.net.INetworkStackConnector {
        @Override // android.net.INetworkStackConnector
        public void makeDhcpServer(java.lang.String ifName, android.net.dhcp.DhcpServingParamsParcel params, android.net.dhcp.IDhcpServerCallbacks cb) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkStackConnector
        public void makeNetworkMonitor(android.net.Network network, java.lang.String name, android.net.INetworkMonitorCallbacks cb) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkStackConnector
        public void makeIpClient(java.lang.String ifName, android.net.ip.IIpClientCallbacks callbacks) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkStackConnector
        public void fetchIpMemoryStore(android.net.IIpMemoryStoreCallbacks cb) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkStackConnector
        public void allowTestUid(int uid, android.net.INetworkStackStatusCallback cb) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkStackConnector
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.INetworkStackConnector
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.INetworkStackConnector {
        static final int TRANSACTION_allowTestUid = 5;
        static final int TRANSACTION_fetchIpMemoryStore = 4;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_makeDhcpServer = 1;
        static final int TRANSACTION_makeIpClient = 3;
        static final int TRANSACTION_makeNetworkMonitor = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.INetworkStackConnector asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.INetworkStackConnector)) {
                return (android.net.INetworkStackConnector) iin;
            }
            return new android.net.INetworkStackConnector.Stub.Proxy(obj);
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
                    java.lang.String _arg0 = data.readString();
                    android.net.dhcp.DhcpServingParamsParcel _arg1 = (android.net.dhcp.DhcpServingParamsParcel) data.readTypedObject(android.net.dhcp.DhcpServingParamsParcel.CREATOR);
                    android.net.dhcp.IDhcpServerCallbacks _arg2 = android.net.dhcp.IDhcpServerCallbacks.Stub.asInterface(data.readStrongBinder());
                    makeDhcpServer(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    android.net.Network _arg02 = (android.net.Network) data.readTypedObject(android.net.Network.CREATOR);
                    java.lang.String _arg12 = data.readString();
                    android.net.INetworkMonitorCallbacks _arg22 = android.net.INetworkMonitorCallbacks.Stub.asInterface(data.readStrongBinder());
                    makeNetworkMonitor(_arg02, _arg12, _arg22);
                    return true;
                case 3:
                    java.lang.String _arg03 = data.readString();
                    android.net.ip.IIpClientCallbacks _arg13 = android.net.ip.IIpClientCallbacks.Stub.asInterface(data.readStrongBinder());
                    makeIpClient(_arg03, _arg13);
                    return true;
                case 4:
                    android.net.IIpMemoryStoreCallbacks _arg04 = android.net.IIpMemoryStoreCallbacks.Stub.asInterface(data.readStrongBinder());
                    fetchIpMemoryStore(_arg04);
                    return true;
                case 5:
                    int _arg05 = data.readInt();
                    android.net.INetworkStackStatusCallback _arg14 = android.net.INetworkStackStatusCallback.Stub.asInterface(data.readStrongBinder());
                    allowTestUid(_arg05, _arg14);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.INetworkStackConnector {
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

            @Override // android.net.INetworkStackConnector
            public void makeDhcpServer(java.lang.String ifName, android.net.dhcp.DhcpServingParamsParcel params, android.net.dhcp.IDhcpServerCallbacks cb) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(ifName);
                    _data.writeTypedObject(params, 0);
                    _data.writeStrongInterface(cb);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method makeDhcpServer is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkStackConnector
            public void makeNetworkMonitor(android.net.Network network, java.lang.String name, android.net.INetworkMonitorCallbacks cb) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(network, 0);
                    _data.writeString(name);
                    _data.writeStrongInterface(cb);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method makeNetworkMonitor is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkStackConnector
            public void makeIpClient(java.lang.String ifName, android.net.ip.IIpClientCallbacks callbacks) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(ifName);
                    _data.writeStrongInterface(callbacks);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method makeIpClient is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkStackConnector
            public void fetchIpMemoryStore(android.net.IIpMemoryStoreCallbacks cb) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method fetchIpMemoryStore is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkStackConnector
            public void allowTestUid(int uid, android.net.INetworkStackStatusCallback cb) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeStrongInterface(cb);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method allowTestUid is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkStackConnector
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

            @Override // android.net.INetworkStackConnector
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.INetworkStackConnector.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
}
