package android.hardware.weaver;

/* JADX INFO: loaded from: classes.dex */
public interface IWeaver extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$weaver$IWeaver".replace('$', '.');
    public static final java.lang.String HASH = "0d60d74c2704ad281e219244514516db8482ef3d";
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_INCORRECT_KEY = 2;
    public static final int STATUS_THROTTLE = 3;
    public static final int VERSION = 2;

    android.hardware.weaver.WeaverConfig getConfig() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    android.hardware.weaver.WeaverReadResponse read(int i, byte[] bArr) throws android.os.RemoteException;

    void write(int i, byte[] bArr, byte[] bArr2) throws android.os.RemoteException;

    public static class Default implements android.hardware.weaver.IWeaver {
        @Override // android.hardware.weaver.IWeaver
        public android.hardware.weaver.WeaverConfig getConfig() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.weaver.IWeaver
        public android.hardware.weaver.WeaverReadResponse read(int slotId, byte[] key) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.weaver.IWeaver
        public void write(int slotId, byte[] key, byte[] value) throws android.os.RemoteException {
        }

        @Override // android.hardware.weaver.IWeaver
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.weaver.IWeaver
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.weaver.IWeaver {
        static final int TRANSACTION_getConfig = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_read = 2;
        static final int TRANSACTION_write = 3;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.weaver.IWeaver asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.weaver.IWeaver)) {
                return (android.hardware.weaver.IWeaver) iin;
            }
            return new android.hardware.weaver.IWeaver.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getConfig";
                case 2:
                    return "read";
                case 3:
                    return "write";
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
                    android.hardware.weaver.WeaverConfig _result = getConfig();
                    reply.writeNoException();
                    reply.writeTypedObject(_result, 1);
                    return true;
                case 2:
                    int _arg0 = data.readInt();
                    byte[] _arg1 = data.createByteArray();
                    data.enforceNoDataAvail();
                    android.hardware.weaver.WeaverReadResponse _result2 = read(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedObject(_result2, 1);
                    return true;
                case 3:
                    int _arg02 = data.readInt();
                    byte[] _arg12 = data.createByteArray();
                    byte[] _arg2 = data.createByteArray();
                    data.enforceNoDataAvail();
                    write(_arg02, _arg12, _arg2);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.weaver.IWeaver {
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

            @Override // android.hardware.weaver.IWeaver
            public android.hardware.weaver.WeaverConfig getConfig() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getConfig is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.weaver.WeaverConfig _result = (android.hardware.weaver.WeaverConfig) _reply.readTypedObject(android.hardware.weaver.WeaverConfig.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.weaver.IWeaver
            public android.hardware.weaver.WeaverReadResponse read(int slotId, byte[] key) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeByteArray(key);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method read is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.weaver.WeaverReadResponse _result = (android.hardware.weaver.WeaverReadResponse) _reply.readTypedObject(android.hardware.weaver.WeaverReadResponse.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.weaver.IWeaver
            public void write(int slotId, byte[] key, byte[] value) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeByteArray(key);
                    _data.writeByteArray(value);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method write is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.weaver.IWeaver
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

            @Override // android.hardware.weaver.IWeaver
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.weaver.IWeaver.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
