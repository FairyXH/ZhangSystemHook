package android.frameworks.vibrator;

/* JADX INFO: loaded from: classes.dex */
public interface IVibratorController extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$frameworks$vibrator$IVibratorController".replace('$', '.');
    public static final java.lang.String HASH = "eb095ed3034973273898ca9e37bbc72566392b8a";
    public static final int VERSION = 1;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void requestVibrationParams(int i, long j, android.os.IBinder iBinder) throws android.os.RemoteException;

    public static class Default implements android.frameworks.vibrator.IVibratorController {
        @Override // android.frameworks.vibrator.IVibratorController
        public void requestVibrationParams(int typesMask, long deadlineElapsedRealtimeMillis, android.os.IBinder requestToken) throws android.os.RemoteException {
        }

        @Override // android.frameworks.vibrator.IVibratorController
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.frameworks.vibrator.IVibratorController
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.frameworks.vibrator.IVibratorController {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_requestVibrationParams = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.frameworks.vibrator.IVibratorController asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.frameworks.vibrator.IVibratorController)) {
                return (android.frameworks.vibrator.IVibratorController) iin;
            }
            return new android.frameworks.vibrator.IVibratorController.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "requestVibrationParams";
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
                    long _arg1 = data.readLong();
                    android.os.IBinder _arg2 = data.readStrongBinder();
                    data.enforceNoDataAvail();
                    requestVibrationParams(_arg0, _arg1, _arg2);
                    break;
            }
            return true;
        }

        private static class Proxy implements android.frameworks.vibrator.IVibratorController {
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

            @Override // android.frameworks.vibrator.IVibratorController
            public void requestVibrationParams(int typesMask, long deadlineElapsedRealtimeMillis, android.os.IBinder requestToken) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(typesMask);
                    _data.writeLong(deadlineElapsedRealtimeMillis);
                    _data.writeStrongBinder(requestToken);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method requestVibrationParams is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.frameworks.vibrator.IVibratorController
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

            @Override // android.frameworks.vibrator.IVibratorController
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.frameworks.vibrator.IVibratorController.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
