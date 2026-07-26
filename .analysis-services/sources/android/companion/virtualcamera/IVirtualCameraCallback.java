package android.companion.virtualcamera;

/* JADX INFO: loaded from: classes.dex */
public interface IVirtualCameraCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android.companion.virtualcamera.IVirtualCameraCallback";

    void onProcessCaptureRequest(int i, int i2) throws android.os.RemoteException;

    void onStreamClosed(int i) throws android.os.RemoteException;

    void onStreamConfigured(int i, android.view.Surface surface, int i2, int i3, int i4) throws android.os.RemoteException;

    public static class Default implements android.companion.virtualcamera.IVirtualCameraCallback {
        @Override // android.companion.virtualcamera.IVirtualCameraCallback
        public void onStreamConfigured(int streamId, android.view.Surface surface, int width, int height, int pixelFormat) throws android.os.RemoteException {
        }

        @Override // android.companion.virtualcamera.IVirtualCameraCallback
        public void onProcessCaptureRequest(int streamId, int frameId) throws android.os.RemoteException {
        }

        @Override // android.companion.virtualcamera.IVirtualCameraCallback
        public void onStreamClosed(int streamId) throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.companion.virtualcamera.IVirtualCameraCallback {
        static final int TRANSACTION_onProcessCaptureRequest = 2;
        static final int TRANSACTION_onStreamClosed = 3;
        static final int TRANSACTION_onStreamConfigured = 1;

        public Stub() {
            attachInterface(this, android.companion.virtualcamera.IVirtualCameraCallback.DESCRIPTOR);
        }

        public static android.companion.virtualcamera.IVirtualCameraCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.companion.virtualcamera.IVirtualCameraCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof android.companion.virtualcamera.IVirtualCameraCallback)) {
                return (android.companion.virtualcamera.IVirtualCameraCallback) iin;
            }
            return new android.companion.virtualcamera.IVirtualCameraCallback.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onStreamConfigured";
                case 2:
                    return "onProcessCaptureRequest";
                case 3:
                    return "onStreamClosed";
                default:
                    return null;
            }
        }

        public java.lang.String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(android.companion.virtualcamera.IVirtualCameraCallback.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.companion.virtualcamera.IVirtualCameraCallback.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    android.view.Surface _arg1 = (android.view.Surface) data.readTypedObject(android.view.Surface.CREATOR);
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    onStreamConfigured(_arg0, _arg1, _arg2, _arg3, _arg4);
                    return true;
                case 2:
                    int _arg02 = data.readInt();
                    int _arg12 = data.readInt();
                    data.enforceNoDataAvail();
                    onProcessCaptureRequest(_arg02, _arg12);
                    return true;
                case 3:
                    int _arg03 = data.readInt();
                    data.enforceNoDataAvail();
                    onStreamClosed(_arg03);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.companion.virtualcamera.IVirtualCameraCallback {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.companion.virtualcamera.IVirtualCameraCallback.DESCRIPTOR;
            }

            @Override // android.companion.virtualcamera.IVirtualCameraCallback
            public void onStreamConfigured(int streamId, android.view.Surface surface, int width, int height, int pixelFormat) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.companion.virtualcamera.IVirtualCameraCallback.DESCRIPTOR);
                    _data.writeInt(streamId);
                    _data.writeTypedObject(surface, 0);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeInt(pixelFormat);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.companion.virtualcamera.IVirtualCameraCallback
            public void onProcessCaptureRequest(int streamId, int frameId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.companion.virtualcamera.IVirtualCameraCallback.DESCRIPTOR);
                    _data.writeInt(streamId);
                    _data.writeInt(frameId);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.companion.virtualcamera.IVirtualCameraCallback
            public void onStreamClosed(int streamId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.companion.virtualcamera.IVirtualCameraCallback.DESCRIPTOR);
                    _data.writeInt(streamId);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
