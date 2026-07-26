package android.net.dhcp;

/* JADX INFO: loaded from: classes.dex */
public interface IDhcpEventCallbacks extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$dhcp$IDhcpEventCallbacks".replace('$', '.');
    public static final java.lang.String HASH = "9bd9d687ddb816baf1faabcad0d56ac15b22c56e";
    public static final int VERSION = 21;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onLeasesChanged(java.util.List<android.net.dhcp.DhcpLeaseParcelable> list) throws android.os.RemoteException;

    void onNewPrefixRequest(android.net.IpPrefix ipPrefix) throws android.os.RemoteException;

    public static class Default implements android.net.dhcp.IDhcpEventCallbacks {
        @Override // android.net.dhcp.IDhcpEventCallbacks
        public void onLeasesChanged(java.util.List<android.net.dhcp.DhcpLeaseParcelable> newLeases) throws android.os.RemoteException {
        }

        @Override // android.net.dhcp.IDhcpEventCallbacks
        public void onNewPrefixRequest(android.net.IpPrefix currentPrefix) throws android.os.RemoteException {
        }

        @Override // android.net.dhcp.IDhcpEventCallbacks
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.dhcp.IDhcpEventCallbacks
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.dhcp.IDhcpEventCallbacks {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onLeasesChanged = 1;
        static final int TRANSACTION_onNewPrefixRequest = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.dhcp.IDhcpEventCallbacks asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.dhcp.IDhcpEventCallbacks)) {
                return (android.net.dhcp.IDhcpEventCallbacks) iin;
            }
            return new android.net.dhcp.IDhcpEventCallbacks.Stub.Proxy(obj);
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
                    java.util.List<android.net.dhcp.DhcpLeaseParcelable> _arg0 = data.createTypedArrayList(android.net.dhcp.DhcpLeaseParcelable.CREATOR);
                    onLeasesChanged(_arg0);
                    return true;
                case 2:
                    android.net.IpPrefix _arg02 = (android.net.IpPrefix) data.readTypedObject(android.net.IpPrefix.CREATOR);
                    onNewPrefixRequest(_arg02);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.dhcp.IDhcpEventCallbacks {
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

            @Override // android.net.dhcp.IDhcpEventCallbacks
            public void onLeasesChanged(java.util.List<android.net.dhcp.DhcpLeaseParcelable> newLeases) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    android.net.dhcp.IDhcpEventCallbacks._Parcel.writeTypedList(_data, newLeases, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onLeasesChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.dhcp.IDhcpEventCallbacks
            public void onNewPrefixRequest(android.net.IpPrefix currentPrefix) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(currentPrefix, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onNewPrefixRequest is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.dhcp.IDhcpEventCallbacks
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

            @Override // android.net.dhcp.IDhcpEventCallbacks
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.dhcp.IDhcpEventCallbacks.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
