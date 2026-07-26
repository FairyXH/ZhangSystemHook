package android.os;

/* JADX INFO: loaded from: classes.dex */
public interface IDumpstateListener extends android.os.IInterface {
    public static final int BUGREPORT_ERROR_ANOTHER_REPORT_IN_PROGRESS = 5;
    public static final int BUGREPORT_ERROR_INVALID_INPUT = 1;
    public static final int BUGREPORT_ERROR_NO_BUGREPORT_TO_RETRIEVE = 6;
    public static final int BUGREPORT_ERROR_RUNTIME_ERROR = 2;
    public static final int BUGREPORT_ERROR_USER_CONSENT_TIMED_OUT = 4;
    public static final int BUGREPORT_ERROR_USER_DENIED_CONSENT = 3;
    public static final java.lang.String DESCRIPTOR = "android.os.IDumpstateListener";

    void onError(int i) throws android.os.RemoteException;

    void onFinished(java.lang.String str) throws android.os.RemoteException;

    void onProgress(int i) throws android.os.RemoteException;

    void onScreenshotTaken(boolean z) throws android.os.RemoteException;

    void onUiIntensiveBugreportDumpsFinished() throws android.os.RemoteException;

    public static class Default implements android.os.IDumpstateListener {
        @Override // android.os.IDumpstateListener
        public void onProgress(int progress) throws android.os.RemoteException {
        }

        @Override // android.os.IDumpstateListener
        public void onError(int errorCode) throws android.os.RemoteException {
        }

        @Override // android.os.IDumpstateListener
        public void onFinished(java.lang.String bugreportFile) throws android.os.RemoteException {
        }

        @Override // android.os.IDumpstateListener
        public void onScreenshotTaken(boolean success) throws android.os.RemoteException {
        }

        @Override // android.os.IDumpstateListener
        public void onUiIntensiveBugreportDumpsFinished() throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.os.IDumpstateListener {
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onFinished = 3;
        static final int TRANSACTION_onProgress = 1;
        static final int TRANSACTION_onScreenshotTaken = 4;
        static final int TRANSACTION_onUiIntensiveBugreportDumpsFinished = 5;

        public Stub() {
            attachInterface(this, android.os.IDumpstateListener.DESCRIPTOR);
        }

        public static android.os.IDumpstateListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.os.IDumpstateListener.DESCRIPTOR);
            if (iin != null && (iin instanceof android.os.IDumpstateListener)) {
                return (android.os.IDumpstateListener) iin;
            }
            return new android.os.IDumpstateListener.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(android.os.IDumpstateListener.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.os.IDumpstateListener.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    data.enforceNoDataAvail();
                    onProgress(_arg0);
                    return true;
                case 2:
                    int _arg02 = data.readInt();
                    data.enforceNoDataAvail();
                    onError(_arg02);
                    return true;
                case 3:
                    java.lang.String _arg03 = data.readString();
                    data.enforceNoDataAvail();
                    onFinished(_arg03);
                    return true;
                case 4:
                    boolean _arg04 = data.readBoolean();
                    data.enforceNoDataAvail();
                    onScreenshotTaken(_arg04);
                    return true;
                case 5:
                    onUiIntensiveBugreportDumpsFinished();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.os.IDumpstateListener {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.IDumpstateListener.DESCRIPTOR;
            }

            @Override // android.os.IDumpstateListener
            public void onProgress(int progress) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IDumpstateListener.DESCRIPTOR);
                    _data.writeInt(progress);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IDumpstateListener
            public void onError(int errorCode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IDumpstateListener.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IDumpstateListener
            public void onFinished(java.lang.String bugreportFile) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IDumpstateListener.DESCRIPTOR);
                    _data.writeString(bugreportFile);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IDumpstateListener
            public void onScreenshotTaken(boolean success) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IDumpstateListener.DESCRIPTOR);
                    _data.writeBoolean(success);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IDumpstateListener
            public void onUiIntensiveBugreportDumpsFinished() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IDumpstateListener.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
