package vendor.oplus.hardware.touch;

/* JADX INFO: loaded from: classes4.dex */
public interface IOplusTouch extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$oplus$hardware$touch$IOplusTouch".replace('$', '.');
    public static final java.lang.String HASH = "ef41c5fab372bb5ad6a417f606845c87cf0e9b17";
    public static final int VERSION = 2;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    int initialize() throws android.os.RemoteException;

    int isTouchNodeSupport(int i, int i2) throws android.os.RemoteException;

    int registerEventCallback(vendor.oplus.hardware.touch.IOplusTouchEventCallback iOplusTouchEventCallback) throws android.os.RemoteException;

    int touchNotifyClient(int i, vendor.oplus.hardware.touch.OplusTouchInfo oplusTouchInfo) throws android.os.RemoteException;

    java.lang.String touchReadNodeFile(int i, int i2) throws android.os.RemoteException;

    int touchWriteBtInfo(int i, int i2, java.lang.String str) throws android.os.RemoteException;

    int touchWriteNodeFile(int i, int i2, java.lang.String str) throws android.os.RemoteException;

    void touchWriteNodeFileOneWay(int i, int i2, java.lang.String str) throws android.os.RemoteException;

    int unregisterEventCallback(vendor.oplus.hardware.touch.IOplusTouchEventCallback iOplusTouchEventCallback) throws android.os.RemoteException;

    public static class Default implements vendor.oplus.hardware.touch.IOplusTouch {
        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int initialize() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int isTouchNodeSupport(int deviceId, int nodeFlag) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public java.lang.String touchReadNodeFile(int deviceId, int nodeFlag) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int touchWriteNodeFile(int deviceId, int nodeFlag, java.lang.String info) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int touchWriteBtInfo(int deviceId, int nodeFlag, java.lang.String info) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public void touchWriteNodeFileOneWay(int deviceId, int nodeFlag, java.lang.String info) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int touchNotifyClient(int clientFlag, vendor.oplus.hardware.touch.OplusTouchInfo info) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int registerEventCallback(vendor.oplus.hardware.touch.IOplusTouchEventCallback callback) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int unregisterEventCallback(vendor.oplus.hardware.touch.IOplusTouchEventCallback callback) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouch
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.oplus.hardware.touch.IOplusTouch {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_initialize = 1;
        static final int TRANSACTION_isTouchNodeSupport = 2;
        static final int TRANSACTION_registerEventCallback = 8;
        static final int TRANSACTION_touchNotifyClient = 7;
        static final int TRANSACTION_touchReadNodeFile = 3;
        static final int TRANSACTION_touchWriteBtInfo = 5;
        static final int TRANSACTION_touchWriteNodeFile = 4;
        static final int TRANSACTION_touchWriteNodeFileOneWay = 6;
        static final int TRANSACTION_unregisterEventCallback = 9;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.oplus.hardware.touch.IOplusTouch asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.oplus.hardware.touch.IOplusTouch)) {
                return (vendor.oplus.hardware.touch.IOplusTouch) iin;
            }
            return new vendor.oplus.hardware.touch.IOplusTouch.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "initialize";
                case 2:
                    return "isTouchNodeSupport";
                case 3:
                    return "touchReadNodeFile";
                case 4:
                    return "touchWriteNodeFile";
                case 5:
                    return "touchWriteBtInfo";
                case 6:
                    return "touchWriteNodeFileOneWay";
                case 7:
                    return "touchNotifyClient";
                case 8:
                    return "registerEventCallback";
                case 9:
                    return "unregisterEventCallback";
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
                    int _result = initialize();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result2 = isTouchNodeSupport(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    int _arg02 = data.readInt();
                    int _arg12 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result3 = touchReadNodeFile(_arg02, _arg12);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 4:
                    int _arg03 = data.readInt();
                    int _arg13 = data.readInt();
                    java.lang.String _arg2 = data.readString();
                    data.enforceNoDataAvail();
                    int _result4 = touchWriteNodeFile(_arg03, _arg13, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    int _arg04 = data.readInt();
                    int _arg14 = data.readInt();
                    java.lang.String _arg22 = data.readString();
                    data.enforceNoDataAvail();
                    int _result5 = touchWriteBtInfo(_arg04, _arg14, _arg22);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 6:
                    int _arg05 = data.readInt();
                    int _arg15 = data.readInt();
                    java.lang.String _arg23 = data.readString();
                    data.enforceNoDataAvail();
                    touchWriteNodeFileOneWay(_arg05, _arg15, _arg23);
                    return true;
                case 7:
                    int _arg06 = data.readInt();
                    vendor.oplus.hardware.touch.OplusTouchInfo _arg16 = (vendor.oplus.hardware.touch.OplusTouchInfo) data.readTypedObject(vendor.oplus.hardware.touch.OplusTouchInfo.CREATOR);
                    data.enforceNoDataAvail();
                    int _result6 = touchNotifyClient(_arg06, _arg16);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 8:
                    vendor.oplus.hardware.touch.IOplusTouchEventCallback _arg07 = vendor.oplus.hardware.touch.IOplusTouchEventCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    int _result7 = registerEventCallback(_arg07);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 9:
                    vendor.oplus.hardware.touch.IOplusTouchEventCallback _arg08 = vendor.oplus.hardware.touch.IOplusTouchEventCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    int _result8 = unregisterEventCallback(_arg08);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements vendor.oplus.hardware.touch.IOplusTouch {
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

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int initialize() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method initialize is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int isTouchNodeSupport(int deviceId, int nodeFlag) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isTouchNodeSupport is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public java.lang.String touchReadNodeFile(int deviceId, int nodeFlag) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method touchReadNodeFile is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int touchWriteNodeFile(int deviceId, int nodeFlag, java.lang.String info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method touchWriteNodeFile is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int touchWriteBtInfo(int deviceId, int nodeFlag, java.lang.String info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method touchWriteBtInfo is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public void touchWriteNodeFileOneWay(int deviceId, int nodeFlag, java.lang.String info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method touchWriteNodeFileOneWay is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int touchNotifyClient(int clientFlag, vendor.oplus.hardware.touch.OplusTouchInfo info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(clientFlag);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method touchNotifyClient is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int registerEventCallback(vendor.oplus.hardware.touch.IOplusTouchEventCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method registerEventCallback is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public int unregisterEventCallback(vendor.oplus.hardware.touch.IOplusTouchEventCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method unregisterEventCallback is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouch
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

            @Override // vendor.oplus.hardware.touch.IOplusTouch
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.oplus.hardware.touch.IOplusTouch.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
