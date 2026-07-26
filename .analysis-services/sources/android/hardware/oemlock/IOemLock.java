package android.hardware.oemlock;

/* JADX INFO: loaded from: classes.dex */
public interface IOemLock extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$oemlock$IOemLock".replace('$', '.');
    public static final java.lang.String HASH = "782d36d56fbdca1105672dd96b8e955b6a81dadf";
    public static final int VERSION = 1;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    java.lang.String getName() throws android.os.RemoteException;

    boolean isOemUnlockAllowedByCarrier() throws android.os.RemoteException;

    boolean isOemUnlockAllowedByDevice() throws android.os.RemoteException;

    int setOemUnlockAllowedByCarrier(boolean z, byte[] bArr) throws android.os.RemoteException;

    void setOemUnlockAllowedByDevice(boolean z) throws android.os.RemoteException;

    public static class Default implements android.hardware.oemlock.IOemLock {
        @Override // android.hardware.oemlock.IOemLock
        public java.lang.String getName() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.oemlock.IOemLock
        public boolean isOemUnlockAllowedByCarrier() throws android.os.RemoteException {
            return false;
        }

        @Override // android.hardware.oemlock.IOemLock
        public boolean isOemUnlockAllowedByDevice() throws android.os.RemoteException {
            return false;
        }

        @Override // android.hardware.oemlock.IOemLock
        public int setOemUnlockAllowedByCarrier(boolean allowed, byte[] signature) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.oemlock.IOemLock
        public void setOemUnlockAllowedByDevice(boolean allowed) throws android.os.RemoteException {
        }

        @Override // android.hardware.oemlock.IOemLock
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.oemlock.IOemLock
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.oemlock.IOemLock {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getName = 1;
        static final int TRANSACTION_isOemUnlockAllowedByCarrier = 2;
        static final int TRANSACTION_isOemUnlockAllowedByDevice = 3;
        static final int TRANSACTION_setOemUnlockAllowedByCarrier = 4;
        static final int TRANSACTION_setOemUnlockAllowedByDevice = 5;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.oemlock.IOemLock asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.oemlock.IOemLock)) {
                return (android.hardware.oemlock.IOemLock) iin;
            }
            return new android.hardware.oemlock.IOemLock.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getName";
                case 2:
                    return "isOemUnlockAllowedByCarrier";
                case 3:
                    return "isOemUnlockAllowedByDevice";
                case 4:
                    return "setOemUnlockAllowedByCarrier";
                case 5:
                    return "setOemUnlockAllowedByDevice";
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
                    java.lang.String _result = getName();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 2:
                    boolean _result2 = isOemUnlockAllowedByCarrier();
                    reply.writeNoException();
                    reply.writeBoolean(_result2);
                    return true;
                case 3:
                    boolean _result3 = isOemUnlockAllowedByDevice();
                    reply.writeNoException();
                    reply.writeBoolean(_result3);
                    return true;
                case 4:
                    boolean _arg0 = data.readBoolean();
                    byte[] _arg1 = data.createByteArray();
                    data.enforceNoDataAvail();
                    int _result4 = setOemUnlockAllowedByCarrier(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    boolean _arg02 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setOemUnlockAllowedByDevice(_arg02);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.oemlock.IOemLock {
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

            @Override // android.hardware.oemlock.IOemLock
            public java.lang.String getName() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getName is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.oemlock.IOemLock
            public boolean isOemUnlockAllowedByCarrier() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isOemUnlockAllowedByCarrier is unimplemented.");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.oemlock.IOemLock
            public boolean isOemUnlockAllowedByDevice() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isOemUnlockAllowedByDevice is unimplemented.");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.oemlock.IOemLock
            public int setOemUnlockAllowedByCarrier(boolean allowed, byte[] signature) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(allowed);
                    _data.writeByteArray(signature);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setOemUnlockAllowedByCarrier is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.oemlock.IOemLock
            public void setOemUnlockAllowedByDevice(boolean allowed) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(allowed);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setOemUnlockAllowedByDevice is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.oemlock.IOemLock
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

            @Override // android.hardware.oemlock.IOemLock
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.oemlock.IOemLock.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
