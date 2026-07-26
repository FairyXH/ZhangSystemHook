package android.frameworks.vibrator;

/* JADX INFO: loaded from: classes.dex */
public interface IVibratorControlService extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$frameworks$vibrator$IVibratorControlService".replace('$', '.');
    public static final java.lang.String HASH = "eb095ed3034973273898ca9e37bbc72566392b8a";
    public static final int VERSION = 1;

    void clearVibrationParams(int i, android.frameworks.vibrator.IVibratorController iVibratorController) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onRequestVibrationParamsComplete(android.os.IBinder iBinder, android.frameworks.vibrator.VibrationParam[] vibrationParamArr) throws android.os.RemoteException;

    void registerVibratorController(android.frameworks.vibrator.IVibratorController iVibratorController) throws android.os.RemoteException;

    void setVibrationParams(android.frameworks.vibrator.VibrationParam[] vibrationParamArr, android.frameworks.vibrator.IVibratorController iVibratorController) throws android.os.RemoteException;

    void unregisterVibratorController(android.frameworks.vibrator.IVibratorController iVibratorController) throws android.os.RemoteException;

    public static class Default implements android.frameworks.vibrator.IVibratorControlService {
        @Override // android.frameworks.vibrator.IVibratorControlService
        public void registerVibratorController(android.frameworks.vibrator.IVibratorController controller) throws android.os.RemoteException {
        }

        @Override // android.frameworks.vibrator.IVibratorControlService
        public void unregisterVibratorController(android.frameworks.vibrator.IVibratorController controller) throws android.os.RemoteException {
        }

        @Override // android.frameworks.vibrator.IVibratorControlService
        public void setVibrationParams(android.frameworks.vibrator.VibrationParam[] params, android.frameworks.vibrator.IVibratorController token) throws android.os.RemoteException {
        }

        @Override // android.frameworks.vibrator.IVibratorControlService
        public void clearVibrationParams(int typesMask, android.frameworks.vibrator.IVibratorController token) throws android.os.RemoteException {
        }

        @Override // android.frameworks.vibrator.IVibratorControlService
        public void onRequestVibrationParamsComplete(android.os.IBinder requestToken, android.frameworks.vibrator.VibrationParam[] result) throws android.os.RemoteException {
        }

        @Override // android.frameworks.vibrator.IVibratorControlService
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.frameworks.vibrator.IVibratorControlService
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.frameworks.vibrator.IVibratorControlService {
        static final int TRANSACTION_clearVibrationParams = 4;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onRequestVibrationParamsComplete = 5;
        static final int TRANSACTION_registerVibratorController = 1;
        static final int TRANSACTION_setVibrationParams = 3;
        static final int TRANSACTION_unregisterVibratorController = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.frameworks.vibrator.IVibratorControlService asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.frameworks.vibrator.IVibratorControlService)) {
                return (android.frameworks.vibrator.IVibratorControlService) iin;
            }
            return new android.frameworks.vibrator.IVibratorControlService.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerVibratorController";
                case 2:
                    return "unregisterVibratorController";
                case 3:
                    return "setVibrationParams";
                case 4:
                    return "clearVibrationParams";
                case 5:
                    return "onRequestVibrationParamsComplete";
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
                    android.frameworks.vibrator.IVibratorController _arg0 = android.frameworks.vibrator.IVibratorController.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    registerVibratorController(_arg0);
                    return true;
                case 2:
                    android.frameworks.vibrator.IVibratorController _arg02 = android.frameworks.vibrator.IVibratorController.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    unregisterVibratorController(_arg02);
                    return true;
                case 3:
                    android.frameworks.vibrator.VibrationParam[] _arg03 = (android.frameworks.vibrator.VibrationParam[]) data.createTypedArray(android.frameworks.vibrator.VibrationParam.CREATOR);
                    android.frameworks.vibrator.IVibratorController _arg1 = android.frameworks.vibrator.IVibratorController.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setVibrationParams(_arg03, _arg1);
                    return true;
                case 4:
                    int _arg04 = data.readInt();
                    android.frameworks.vibrator.IVibratorController _arg12 = android.frameworks.vibrator.IVibratorController.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    clearVibrationParams(_arg04, _arg12);
                    return true;
                case 5:
                    android.os.IBinder _arg05 = data.readStrongBinder();
                    android.frameworks.vibrator.VibrationParam[] _arg13 = (android.frameworks.vibrator.VibrationParam[]) data.createTypedArray(android.frameworks.vibrator.VibrationParam.CREATOR);
                    data.enforceNoDataAvail();
                    onRequestVibrationParamsComplete(_arg05, _arg13);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.frameworks.vibrator.IVibratorControlService {
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

            @Override // android.frameworks.vibrator.IVibratorControlService
            public void registerVibratorController(android.frameworks.vibrator.IVibratorController controller) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(controller);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method registerVibratorController is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.frameworks.vibrator.IVibratorControlService
            public void unregisterVibratorController(android.frameworks.vibrator.IVibratorController controller) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(controller);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method unregisterVibratorController is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.frameworks.vibrator.IVibratorControlService
            public void setVibrationParams(android.frameworks.vibrator.VibrationParam[] params, android.frameworks.vibrator.IVibratorController token) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedArray(params, 0);
                    _data.writeStrongInterface(token);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setVibrationParams is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.frameworks.vibrator.IVibratorControlService
            public void clearVibrationParams(int typesMask, android.frameworks.vibrator.IVibratorController token) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(typesMask);
                    _data.writeStrongInterface(token);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method clearVibrationParams is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.frameworks.vibrator.IVibratorControlService
            public void onRequestVibrationParamsComplete(android.os.IBinder requestToken, android.frameworks.vibrator.VibrationParam[] result) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder(requestToken);
                    _data.writeTypedArray(result, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onRequestVibrationParamsComplete is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.frameworks.vibrator.IVibratorControlService
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

            @Override // android.frameworks.vibrator.IVibratorControlService
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.frameworks.vibrator.IVibratorControlService.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
