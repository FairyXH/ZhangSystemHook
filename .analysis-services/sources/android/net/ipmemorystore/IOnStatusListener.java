package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public interface IOnStatusListener extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$ipmemorystore$IOnStatusListener".replace('$', '.');
    public static final java.lang.String HASH = "d5ea5eb3ddbdaa9a986ce6ba70b0804ca3e39b0c";
    public static final int VERSION = 10;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onComplete(android.net.ipmemorystore.StatusParcelable statusParcelable) throws android.os.RemoteException;

    public static class Default implements android.net.ipmemorystore.IOnStatusListener {
        @Override // android.net.ipmemorystore.IOnStatusListener
        public void onComplete(android.net.ipmemorystore.StatusParcelable status) throws android.os.RemoteException {
        }

        @Override // android.net.ipmemorystore.IOnStatusListener
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.ipmemorystore.IOnStatusListener
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.ipmemorystore.IOnStatusListener {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onComplete = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.ipmemorystore.IOnStatusListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.ipmemorystore.IOnStatusListener)) {
                return (android.net.ipmemorystore.IOnStatusListener) iin;
            }
            return new android.net.ipmemorystore.IOnStatusListener.Stub.Proxy(obj);
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
                    onComplete(_arg0);
                    break;
            }
            return true;
        }

        private static class Proxy implements android.net.ipmemorystore.IOnStatusListener {
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

            @Override // android.net.ipmemorystore.IOnStatusListener
            public void onComplete(android.net.ipmemorystore.StatusParcelable status) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(status, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onComplete is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ipmemorystore.IOnStatusListener
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

            @Override // android.net.ipmemorystore.IOnStatusListener
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.ipmemorystore.IOnStatusListener.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
