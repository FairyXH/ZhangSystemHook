package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public interface IOnBlobRetrievedListener extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$ipmemorystore$IOnBlobRetrievedListener".replace('$', '.');
    public static final java.lang.String HASH = "d5ea5eb3ddbdaa9a986ce6ba70b0804ca3e39b0c";
    public static final int VERSION = 10;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onBlobRetrieved(android.net.ipmemorystore.StatusParcelable statusParcelable, java.lang.String str, java.lang.String str2, android.net.ipmemorystore.Blob blob) throws android.os.RemoteException;

    public static class Default implements android.net.ipmemorystore.IOnBlobRetrievedListener {
        @Override // android.net.ipmemorystore.IOnBlobRetrievedListener
        public void onBlobRetrieved(android.net.ipmemorystore.StatusParcelable status, java.lang.String l2Key, java.lang.String name, android.net.ipmemorystore.Blob data) throws android.os.RemoteException {
        }

        @Override // android.net.ipmemorystore.IOnBlobRetrievedListener
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.ipmemorystore.IOnBlobRetrievedListener
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.ipmemorystore.IOnBlobRetrievedListener {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onBlobRetrieved = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.ipmemorystore.IOnBlobRetrievedListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.ipmemorystore.IOnBlobRetrievedListener)) {
                return (android.net.ipmemorystore.IOnBlobRetrievedListener) iin;
            }
            return new android.net.ipmemorystore.IOnBlobRetrievedListener.Stub.Proxy(obj);
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
                    android.net.ipmemorystore.StatusParcelable _arg0 = (android.net.ipmemorystore.StatusParcelable) data.readTypedObject(android.net.ipmemorystore.StatusParcelable.CREATOR);
                    java.lang.String _arg1 = data.readString();
                    java.lang.String _arg2 = data.readString();
                    android.net.ipmemorystore.Blob _arg3 = (android.net.ipmemorystore.Blob) data.readTypedObject(android.net.ipmemorystore.Blob.CREATOR);
                    onBlobRetrieved(_arg0, _arg1, _arg2, _arg3);
                    break;
            }
            return true;
        }

        private static class Proxy implements android.net.ipmemorystore.IOnBlobRetrievedListener {
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

            @Override // android.net.ipmemorystore.IOnBlobRetrievedListener
            public void onBlobRetrieved(android.net.ipmemorystore.StatusParcelable status, java.lang.String l2Key, java.lang.String name, android.net.ipmemorystore.Blob data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(status, 0);
                    _data.writeString(l2Key);
                    _data.writeString(name);
                    _data.writeTypedObject(data, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onBlobRetrieved is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ipmemorystore.IOnBlobRetrievedListener
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

            @Override // android.net.ipmemorystore.IOnBlobRetrievedListener
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.ipmemorystore.IOnBlobRetrievedListener.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
