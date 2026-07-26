package android.net.dhcp;

/* JADX INFO: loaded from: classes.dex */
public interface IDhcpServer extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$dhcp$IDhcpServer".replace('$', '.');
    public static final java.lang.String HASH = "9bd9d687ddb816baf1faabcad0d56ac15b22c56e";
    public static final int STATUS_INVALID_ARGUMENT = 2;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_UNKNOWN = 0;
    public static final int STATUS_UNKNOWN_ERROR = 3;
    public static final int VERSION = 21;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void start(android.net.INetworkStackStatusCallback iNetworkStackStatusCallback) throws android.os.RemoteException;

    void startWithCallbacks(android.net.INetworkStackStatusCallback iNetworkStackStatusCallback, android.net.dhcp.IDhcpEventCallbacks iDhcpEventCallbacks) throws android.os.RemoteException;

    void stop(android.net.INetworkStackStatusCallback iNetworkStackStatusCallback) throws android.os.RemoteException;

    void updateParams(android.net.dhcp.DhcpServingParamsParcel dhcpServingParamsParcel, android.net.INetworkStackStatusCallback iNetworkStackStatusCallback) throws android.os.RemoteException;

    public static class Default implements android.net.dhcp.IDhcpServer {
        @Override // android.net.dhcp.IDhcpServer
        public void start(android.net.INetworkStackStatusCallback cb) throws android.os.RemoteException {
        }

        @Override // android.net.dhcp.IDhcpServer
        public void startWithCallbacks(android.net.INetworkStackStatusCallback statusCb, android.net.dhcp.IDhcpEventCallbacks eventCb) throws android.os.RemoteException {
        }

        @Override // android.net.dhcp.IDhcpServer
        public void updateParams(android.net.dhcp.DhcpServingParamsParcel params, android.net.INetworkStackStatusCallback cb) throws android.os.RemoteException {
        }

        @Override // android.net.dhcp.IDhcpServer
        public void stop(android.net.INetworkStackStatusCallback cb) throws android.os.RemoteException {
        }

        @Override // android.net.dhcp.IDhcpServer
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.dhcp.IDhcpServer
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.dhcp.IDhcpServer {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_start = 1;
        static final int TRANSACTION_startWithCallbacks = 4;
        static final int TRANSACTION_stop = 3;
        static final int TRANSACTION_updateParams = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.dhcp.IDhcpServer asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.dhcp.IDhcpServer)) {
                return (android.net.dhcp.IDhcpServer) iin;
            }
            return new android.net.dhcp.IDhcpServer.Stub.Proxy(obj);
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
                    android.net.INetworkStackStatusCallback _arg0 = android.net.INetworkStackStatusCallback.Stub.asInterface(data.readStrongBinder());
                    start(_arg0);
                    return true;
                case 2:
                    android.net.dhcp.DhcpServingParamsParcel _arg02 = (android.net.dhcp.DhcpServingParamsParcel) data.readTypedObject(android.net.dhcp.DhcpServingParamsParcel.CREATOR);
                    android.net.INetworkStackStatusCallback _arg1 = android.net.INetworkStackStatusCallback.Stub.asInterface(data.readStrongBinder());
                    updateParams(_arg02, _arg1);
                    return true;
                case 3:
                    android.net.INetworkStackStatusCallback _arg03 = android.net.INetworkStackStatusCallback.Stub.asInterface(data.readStrongBinder());
                    stop(_arg03);
                    return true;
                case 4:
                    android.net.INetworkStackStatusCallback _arg04 = android.net.INetworkStackStatusCallback.Stub.asInterface(data.readStrongBinder());
                    android.net.dhcp.IDhcpEventCallbacks _arg12 = android.net.dhcp.IDhcpEventCallbacks.Stub.asInterface(data.readStrongBinder());
                    startWithCallbacks(_arg04, _arg12);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.dhcp.IDhcpServer {
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

            @Override // android.net.dhcp.IDhcpServer
            public void start(android.net.INetworkStackStatusCallback cb) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method start is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.dhcp.IDhcpServer
            public void startWithCallbacks(android.net.INetworkStackStatusCallback statusCb, android.net.dhcp.IDhcpEventCallbacks eventCb) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(statusCb);
                    _data.writeStrongInterface(eventCb);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method startWithCallbacks is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.dhcp.IDhcpServer
            public void updateParams(android.net.dhcp.DhcpServingParamsParcel params, android.net.INetworkStackStatusCallback cb) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(params, 0);
                    _data.writeStrongInterface(cb);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method updateParams is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.dhcp.IDhcpServer
            public void stop(android.net.INetworkStackStatusCallback cb) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method stop is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.dhcp.IDhcpServer
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

            @Override // android.net.dhcp.IDhcpServer
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.dhcp.IDhcpServer.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
