package vendor.oplus.hardware.touch;

/* JADX INFO: loaded from: classes4.dex */
public interface IOplusTouchEventCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$oplus$hardware$touch$IOplusTouchEventCallback".replace('$', '.');
    public static final java.lang.String HASH = "ef41c5fab372bb5ad6a417f606845c87cf0e9b17";
    public static final int VERSION = 2;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void touchSendCommand(int i, vendor.oplus.hardware.touch.OplusTouchInfo oplusTouchInfo) throws android.os.RemoteException;

    void touchSendCommandOneWay(int i, vendor.oplus.hardware.touch.OplusTouchInfo oplusTouchInfo) throws android.os.RemoteException;

    public static class Default implements vendor.oplus.hardware.touch.IOplusTouchEventCallback {
        @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
        public void touchSendCommand(int clientFlag, vendor.oplus.hardware.touch.OplusTouchInfo info) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
        public void touchSendCommandOneWay(int clientFlag, vendor.oplus.hardware.touch.OplusTouchInfo info) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.oplus.hardware.touch.IOplusTouchEventCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_touchSendCommand = 1;
        static final int TRANSACTION_touchSendCommandOneWay = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.oplus.hardware.touch.IOplusTouchEventCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.oplus.hardware.touch.IOplusTouchEventCallback)) {
                return (vendor.oplus.hardware.touch.IOplusTouchEventCallback) iin;
            }
            return new vendor.oplus.hardware.touch.IOplusTouchEventCallback.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "touchSendCommand";
                case 2:
                    return "touchSendCommandOneWay";
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
                    int _arg0 = data.readInt();
                    vendor.oplus.hardware.touch.OplusTouchInfo _arg1 = (vendor.oplus.hardware.touch.OplusTouchInfo) data.readTypedObject(vendor.oplus.hardware.touch.OplusTouchInfo.CREATOR);
                    data.enforceNoDataAvail();
                    touchSendCommand(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    int _arg02 = data.readInt();
                    vendor.oplus.hardware.touch.OplusTouchInfo _arg12 = (vendor.oplus.hardware.touch.OplusTouchInfo) data.readTypedObject(vendor.oplus.hardware.touch.OplusTouchInfo.CREATOR);
                    data.enforceNoDataAvail();
                    touchSendCommandOneWay(_arg02, _arg12);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements vendor.oplus.hardware.touch.IOplusTouchEventCallback {
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

            @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
            public void touchSendCommand(int clientFlag, vendor.oplus.hardware.touch.OplusTouchInfo info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(clientFlag);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method touchSendCommand is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
            public void touchSendCommandOneWay(int clientFlag, vendor.oplus.hardware.touch.OplusTouchInfo info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(clientFlag);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method touchSendCommandOneWay is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
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

            @Override // vendor.oplus.hardware.touch.IOplusTouchEventCallback
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.oplus.hardware.touch.IOplusTouchEventCallback.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
