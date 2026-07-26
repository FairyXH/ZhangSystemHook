package android.net;

/* JADX INFO: loaded from: classes.dex */
public interface IIpMemoryStore extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$IIpMemoryStore".replace('$', '.');
    public static final java.lang.String HASH = "d5ea5eb3ddbdaa9a986ce6ba70b0804ca3e39b0c";
    public static final int VERSION = 10;

    void delete(java.lang.String str, boolean z, android.net.ipmemorystore.IOnStatusAndCountListener iOnStatusAndCountListener) throws android.os.RemoteException;

    void deleteCluster(java.lang.String str, boolean z, android.net.ipmemorystore.IOnStatusAndCountListener iOnStatusAndCountListener) throws android.os.RemoteException;

    void factoryReset() throws android.os.RemoteException;

    void findL2Key(android.net.ipmemorystore.NetworkAttributesParcelable networkAttributesParcelable, android.net.ipmemorystore.IOnL2KeyResponseListener iOnL2KeyResponseListener) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void isSameNetwork(java.lang.String str, java.lang.String str2, android.net.ipmemorystore.IOnSameL3NetworkResponseListener iOnSameL3NetworkResponseListener) throws android.os.RemoteException;

    void retrieveBlob(java.lang.String str, java.lang.String str2, java.lang.String str3, android.net.ipmemorystore.IOnBlobRetrievedListener iOnBlobRetrievedListener) throws android.os.RemoteException;

    void retrieveNetworkAttributes(java.lang.String str, android.net.ipmemorystore.IOnNetworkAttributesRetrievedListener iOnNetworkAttributesRetrievedListener) throws android.os.RemoteException;

    void storeBlob(java.lang.String str, java.lang.String str2, java.lang.String str3, android.net.ipmemorystore.Blob blob, android.net.ipmemorystore.IOnStatusListener iOnStatusListener) throws android.os.RemoteException;

    void storeNetworkAttributes(java.lang.String str, android.net.ipmemorystore.NetworkAttributesParcelable networkAttributesParcelable, android.net.ipmemorystore.IOnStatusListener iOnStatusListener) throws android.os.RemoteException;

    public static class Default implements android.net.IIpMemoryStore {
        @Override // android.net.IIpMemoryStore
        public void storeNetworkAttributes(java.lang.String l2Key, android.net.ipmemorystore.NetworkAttributesParcelable attributes, android.net.ipmemorystore.IOnStatusListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IIpMemoryStore
        public void storeBlob(java.lang.String l2Key, java.lang.String clientId, java.lang.String name, android.net.ipmemorystore.Blob data, android.net.ipmemorystore.IOnStatusListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IIpMemoryStore
        public void findL2Key(android.net.ipmemorystore.NetworkAttributesParcelable attributes, android.net.ipmemorystore.IOnL2KeyResponseListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IIpMemoryStore
        public void isSameNetwork(java.lang.String l2Key1, java.lang.String l2Key2, android.net.ipmemorystore.IOnSameL3NetworkResponseListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IIpMemoryStore
        public void retrieveNetworkAttributes(java.lang.String l2Key, android.net.ipmemorystore.IOnNetworkAttributesRetrievedListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IIpMemoryStore
        public void retrieveBlob(java.lang.String l2Key, java.lang.String clientId, java.lang.String name, android.net.ipmemorystore.IOnBlobRetrievedListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IIpMemoryStore
        public void factoryReset() throws android.os.RemoteException {
        }

        @Override // android.net.IIpMemoryStore
        public void delete(java.lang.String l2Key, boolean needWipe, android.net.ipmemorystore.IOnStatusAndCountListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IIpMemoryStore
        public void deleteCluster(java.lang.String cluster, boolean needWipe, android.net.ipmemorystore.IOnStatusAndCountListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IIpMemoryStore
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.IIpMemoryStore
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.IIpMemoryStore {
        static final int TRANSACTION_delete = 8;
        static final int TRANSACTION_deleteCluster = 9;
        static final int TRANSACTION_factoryReset = 7;
        static final int TRANSACTION_findL2Key = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_isSameNetwork = 4;
        static final int TRANSACTION_retrieveBlob = 6;
        static final int TRANSACTION_retrieveNetworkAttributes = 5;
        static final int TRANSACTION_storeBlob = 2;
        static final int TRANSACTION_storeNetworkAttributes = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.IIpMemoryStore asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.IIpMemoryStore)) {
                return (android.net.IIpMemoryStore) iin;
            }
            return new android.net.IIpMemoryStore.Stub.Proxy(obj);
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
                    android.net.ipmemorystore.NetworkAttributesParcelable _arg1 = (android.net.ipmemorystore.NetworkAttributesParcelable) data.readTypedObject(android.net.ipmemorystore.NetworkAttributesParcelable.CREATOR);
                    android.net.ipmemorystore.IOnStatusListener _arg2 = android.net.ipmemorystore.IOnStatusListener.Stub.asInterface(data.readStrongBinder());
                    storeNetworkAttributes(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    java.lang.String _arg02 = data.readString();
                    java.lang.String _arg12 = data.readString();
                    java.lang.String _arg22 = data.readString();
                    android.net.ipmemorystore.Blob _arg3 = (android.net.ipmemorystore.Blob) data.readTypedObject(android.net.ipmemorystore.Blob.CREATOR);
                    android.net.ipmemorystore.IOnStatusListener _arg4 = android.net.ipmemorystore.IOnStatusListener.Stub.asInterface(data.readStrongBinder());
                    storeBlob(_arg02, _arg12, _arg22, _arg3, _arg4);
                    return true;
                case 3:
                    android.net.ipmemorystore.NetworkAttributesParcelable _arg03 = (android.net.ipmemorystore.NetworkAttributesParcelable) data.readTypedObject(android.net.ipmemorystore.NetworkAttributesParcelable.CREATOR);
                    android.net.ipmemorystore.IOnL2KeyResponseListener _arg13 = android.net.ipmemorystore.IOnL2KeyResponseListener.Stub.asInterface(data.readStrongBinder());
                    findL2Key(_arg03, _arg13);
                    return true;
                case 4:
                    java.lang.String _arg04 = data.readString();
                    java.lang.String _arg14 = data.readString();
                    android.net.ipmemorystore.IOnSameL3NetworkResponseListener _arg23 = android.net.ipmemorystore.IOnSameL3NetworkResponseListener.Stub.asInterface(data.readStrongBinder());
                    isSameNetwork(_arg04, _arg14, _arg23);
                    return true;
                case 5:
                    java.lang.String _arg05 = data.readString();
                    android.net.ipmemorystore.IOnNetworkAttributesRetrievedListener _arg15 = android.net.ipmemorystore.IOnNetworkAttributesRetrievedListener.Stub.asInterface(data.readStrongBinder());
                    retrieveNetworkAttributes(_arg05, _arg15);
                    return true;
                case 6:
                    java.lang.String _arg06 = data.readString();
                    java.lang.String _arg16 = data.readString();
                    java.lang.String _arg24 = data.readString();
                    android.net.ipmemorystore.IOnBlobRetrievedListener _arg32 = android.net.ipmemorystore.IOnBlobRetrievedListener.Stub.asInterface(data.readStrongBinder());
                    retrieveBlob(_arg06, _arg16, _arg24, _arg32);
                    return true;
                case 7:
                    factoryReset();
                    return true;
                case 8:
                    java.lang.String _arg07 = data.readString();
                    boolean _arg17 = data.readBoolean();
                    android.net.ipmemorystore.IOnStatusAndCountListener _arg25 = android.net.ipmemorystore.IOnStatusAndCountListener.Stub.asInterface(data.readStrongBinder());
                    delete(_arg07, _arg17, _arg25);
                    return true;
                case 9:
                    java.lang.String _arg08 = data.readString();
                    boolean _arg18 = data.readBoolean();
                    android.net.ipmemorystore.IOnStatusAndCountListener _arg26 = android.net.ipmemorystore.IOnStatusAndCountListener.Stub.asInterface(data.readStrongBinder());
                    deleteCluster(_arg08, _arg18, _arg26);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.IIpMemoryStore {
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

            @Override // android.net.IIpMemoryStore
            public void storeNetworkAttributes(java.lang.String l2Key, android.net.ipmemorystore.NetworkAttributesParcelable attributes, android.net.ipmemorystore.IOnStatusListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(l2Key);
                    _data.writeTypedObject(attributes, 0);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method storeNetworkAttributes is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.IIpMemoryStore
            public void storeBlob(java.lang.String l2Key, java.lang.String clientId, java.lang.String name, android.net.ipmemorystore.Blob data, android.net.ipmemorystore.IOnStatusListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(l2Key);
                    _data.writeString(clientId);
                    _data.writeString(name);
                    _data.writeTypedObject(data, 0);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method storeBlob is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.IIpMemoryStore
            public void findL2Key(android.net.ipmemorystore.NetworkAttributesParcelable attributes, android.net.ipmemorystore.IOnL2KeyResponseListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(attributes, 0);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method findL2Key is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.IIpMemoryStore
            public void isSameNetwork(java.lang.String l2Key1, java.lang.String l2Key2, android.net.ipmemorystore.IOnSameL3NetworkResponseListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(l2Key1);
                    _data.writeString(l2Key2);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isSameNetwork is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.IIpMemoryStore
            public void retrieveNetworkAttributes(java.lang.String l2Key, android.net.ipmemorystore.IOnNetworkAttributesRetrievedListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(l2Key);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method retrieveNetworkAttributes is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.IIpMemoryStore
            public void retrieveBlob(java.lang.String l2Key, java.lang.String clientId, java.lang.String name, android.net.ipmemorystore.IOnBlobRetrievedListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(l2Key);
                    _data.writeString(clientId);
                    _data.writeString(name);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method retrieveBlob is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.IIpMemoryStore
            public void factoryReset() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method factoryReset is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.IIpMemoryStore
            public void delete(java.lang.String l2Key, boolean needWipe, android.net.ipmemorystore.IOnStatusAndCountListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(l2Key);
                    _data.writeBoolean(needWipe);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method delete is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.IIpMemoryStore
            public void deleteCluster(java.lang.String cluster, boolean needWipe, android.net.ipmemorystore.IOnStatusAndCountListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(cluster);
                    _data.writeBoolean(needWipe);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method deleteCluster is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.IIpMemoryStore
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

            @Override // android.net.IIpMemoryStore
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.IIpMemoryStore.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
