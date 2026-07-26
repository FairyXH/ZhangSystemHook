package vendor.pixelworks.hardware.display;

/* JADX INFO: loaded from: classes4.dex */
public interface ISoftIrisClient extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$pixelworks$hardware$display$ISoftIrisClient".replace('$', '.');
    public static final java.lang.String HASH = "02c8c5526cbde39f502b3bf8cccaf196c81de25f";
    public static final int VERSION = 1;

    int[] getConfig(long j, int i, int[] iArr) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    java.lang.String setBatchConfig(int i, java.lang.String str) throws android.os.RemoteException;

    int setConfig(long j, int i, int[] iArr) throws android.os.RemoteException;

    public static class Default implements vendor.pixelworks.hardware.display.ISoftIrisClient {
        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public int setConfig(long display, int type, int[] values) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public int[] getConfig(long display, int type, int[] values) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public java.lang.String setBatchConfig(int type, java.lang.String json) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.pixelworks.hardware.display.ISoftIrisClient {
        static final int TRANSACTION_getConfig = 2;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_setBatchConfig = 3;
        static final int TRANSACTION_setConfig = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.pixelworks.hardware.display.ISoftIrisClient asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.pixelworks.hardware.display.ISoftIrisClient)) {
                return (vendor.pixelworks.hardware.display.ISoftIrisClient) iin;
            }
            return new vendor.pixelworks.hardware.display.ISoftIrisClient.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setConfig";
                case 2:
                    return "getConfig";
                case 3:
                    return "setBatchConfig";
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
                    long _arg0 = data.readLong();
                    int _arg1 = data.readInt();
                    int[] _arg2 = data.createIntArray();
                    data.enforceNoDataAvail();
                    int _result = setConfig(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    long _arg02 = data.readLong();
                    int _arg12 = data.readInt();
                    int[] _arg22 = data.createIntArray();
                    data.enforceNoDataAvail();
                    int[] _result2 = getConfig(_arg02, _arg12, _arg22);
                    reply.writeNoException();
                    reply.writeIntArray(_result2);
                    return true;
                case 3:
                    int _arg03 = data.readInt();
                    java.lang.String _arg13 = data.readString();
                    data.enforceNoDataAvail();
                    java.lang.String _result3 = setBatchConfig(_arg03, _arg13);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements vendor.pixelworks.hardware.display.ISoftIrisClient {
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

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
            public int setConfig(long display, int type, int[] values) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(type);
                    _data.writeIntArray(values);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setConfig is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
            public int[] getConfig(long display, int type, int[] values) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(type);
                    _data.writeIntArray(values);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getConfig is unimplemented.");
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
            public java.lang.String setBatchConfig(int type, java.lang.String json) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(json);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setBatchConfig is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
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

            @Override // vendor.pixelworks.hardware.display.ISoftIrisClient
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.pixelworks.hardware.display.ISoftIrisClient.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
