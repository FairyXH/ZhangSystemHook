package android.hardware.security.secretkeeper;

/* JADX INFO: loaded from: classes.dex */
public interface ISecretkeeper extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$security$secretkeeper$ISecretkeeper".replace('$', '.');
    public static final int ERROR_INTERNAL_ERROR = 2;
    public static final int ERROR_REQUEST_MALFORMED = 3;
    public static final int ERROR_UNKNOWN_KEY_ID = 1;
    public static final java.lang.String HASH = "347439bd6088bd24a72e789a616a1586863e43b8";
    public static final int VERSION = 1;

    void deleteAll() throws android.os.RemoteException;

    void deleteIds(android.hardware.security.secretkeeper.SecretId[] secretIdArr) throws android.os.RemoteException;

    android.hardware.security.authgraph.IAuthGraphKeyExchange getAuthGraphKe() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    byte[] processSecretManagementRequest(byte[] bArr) throws android.os.RemoteException;

    public static class Default implements android.hardware.security.secretkeeper.ISecretkeeper {
        @Override // android.hardware.security.secretkeeper.ISecretkeeper
        public android.hardware.security.authgraph.IAuthGraphKeyExchange getAuthGraphKe() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.secretkeeper.ISecretkeeper
        public byte[] processSecretManagementRequest(byte[] request) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.secretkeeper.ISecretkeeper
        public void deleteIds(android.hardware.security.secretkeeper.SecretId[] ids) throws android.os.RemoteException {
        }

        @Override // android.hardware.security.secretkeeper.ISecretkeeper
        public void deleteAll() throws android.os.RemoteException {
        }

        @Override // android.hardware.security.secretkeeper.ISecretkeeper
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.security.secretkeeper.ISecretkeeper
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.security.secretkeeper.ISecretkeeper {
        static final int TRANSACTION_deleteAll = 4;
        static final int TRANSACTION_deleteIds = 3;
        static final int TRANSACTION_getAuthGraphKe = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_processSecretManagementRequest = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.security.secretkeeper.ISecretkeeper asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.security.secretkeeper.ISecretkeeper)) {
                return (android.hardware.security.secretkeeper.ISecretkeeper) iin;
            }
            return new android.hardware.security.secretkeeper.ISecretkeeper.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAuthGraphKe";
                case 2:
                    return "processSecretManagementRequest";
                case 3:
                    return "deleteIds";
                case 4:
                    return "deleteAll";
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    return "getInterfaceHash";
                case 16777215:
                    return "getInterfaceVersion";
                default:
                    return null;
            }
        }

        public java.lang.String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
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
                    android.hardware.security.authgraph.IAuthGraphKeyExchange _result = getAuthGraphKe();
                    reply.writeNoException();
                    reply.writeStrongInterface(_result);
                    return true;
                case 2:
                    byte[] _arg0 = data.createByteArray();
                    data.enforceNoDataAvail();
                    byte[] _result2 = processSecretManagementRequest(_arg0);
                    reply.writeNoException();
                    reply.writeByteArray(_result2);
                    return true;
                case 3:
                    android.hardware.security.secretkeeper.SecretId[] _arg02 = (android.hardware.security.secretkeeper.SecretId[]) data.createTypedArray(android.hardware.security.secretkeeper.SecretId.CREATOR);
                    data.enforceNoDataAvail();
                    deleteIds(_arg02);
                    reply.writeNoException();
                    return true;
                case 4:
                    deleteAll();
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.security.secretkeeper.ISecretkeeper {
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

            @Override // android.hardware.security.secretkeeper.ISecretkeeper
            public android.hardware.security.authgraph.IAuthGraphKeyExchange getAuthGraphKe() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getAuthGraphKe is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.security.authgraph.IAuthGraphKeyExchange _result = android.hardware.security.authgraph.IAuthGraphKeyExchange.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.secretkeeper.ISecretkeeper
            public byte[] processSecretManagementRequest(byte[] request) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByteArray(request);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method processSecretManagementRequest is unimplemented.");
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.secretkeeper.ISecretkeeper
            public void deleteIds(android.hardware.security.secretkeeper.SecretId[] ids) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedArray(ids, 0);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method deleteIds is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.secretkeeper.ISecretkeeper
            public void deleteAll() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method deleteAll is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.secretkeeper.ISecretkeeper
            public int getInterfaceVersion() throws android.os.RemoteException {
                if (this.mCachedVersion == -1) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
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

            @Override // android.hardware.security.secretkeeper.ISecretkeeper
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.security.secretkeeper.ISecretkeeper.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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

        public int getMaxTransactionId() {
            return TRANSACTION_getInterfaceHash;
        }
    }
}
