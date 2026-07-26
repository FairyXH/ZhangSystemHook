package android.companion.virtualcamera;

/* JADX INFO: loaded from: classes.dex */
public interface IVirtualCameraService extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android.companion.virtualcamera.IVirtualCameraService";

    java.lang.String getCameraId(android.os.IBinder iBinder) throws android.os.RemoteException;

    boolean registerCamera(android.os.IBinder iBinder, android.companion.virtualcamera.VirtualCameraConfiguration virtualCameraConfiguration, int i) throws android.os.RemoteException;

    void unregisterCamera(android.os.IBinder iBinder) throws android.os.RemoteException;

    public static class Default implements android.companion.virtualcamera.IVirtualCameraService {
        @Override // android.companion.virtualcamera.IVirtualCameraService
        public boolean registerCamera(android.os.IBinder token, android.companion.virtualcamera.VirtualCameraConfiguration configuration, int deviceId) throws android.os.RemoteException {
            return false;
        }

        @Override // android.companion.virtualcamera.IVirtualCameraService
        public void unregisterCamera(android.os.IBinder token) throws android.os.RemoteException {
        }

        @Override // android.companion.virtualcamera.IVirtualCameraService
        public java.lang.String getCameraId(android.os.IBinder token) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.companion.virtualcamera.IVirtualCameraService {
        static final int TRANSACTION_getCameraId = 3;
        static final int TRANSACTION_registerCamera = 1;
        static final int TRANSACTION_unregisterCamera = 2;

        public Stub() {
            attachInterface(this, android.companion.virtualcamera.IVirtualCameraService.DESCRIPTOR);
        }

        public static android.companion.virtualcamera.IVirtualCameraService asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.companion.virtualcamera.IVirtualCameraService.DESCRIPTOR);
            if (iin != null && (iin instanceof android.companion.virtualcamera.IVirtualCameraService)) {
                return (android.companion.virtualcamera.IVirtualCameraService) iin;
            }
            return new android.companion.virtualcamera.IVirtualCameraService.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerCamera";
                case 2:
                    return "unregisterCamera";
                case 3:
                    return "getCameraId";
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
                data.enforceInterface(android.companion.virtualcamera.IVirtualCameraService.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.companion.virtualcamera.IVirtualCameraService.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    android.os.IBinder _arg0 = data.readStrongBinder();
                    android.companion.virtualcamera.VirtualCameraConfiguration _arg1 = (android.companion.virtualcamera.VirtualCameraConfiguration) data.readTypedObject(android.companion.virtualcamera.VirtualCameraConfiguration.CREATOR);
                    int _arg2 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result = registerCamera(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    return true;
                case 2:
                    android.os.IBinder _arg02 = data.readStrongBinder();
                    data.enforceNoDataAvail();
                    unregisterCamera(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    android.os.IBinder _arg03 = data.readStrongBinder();
                    data.enforceNoDataAvail();
                    java.lang.String _result2 = getCameraId(_arg03);
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.companion.virtualcamera.IVirtualCameraService {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.companion.virtualcamera.IVirtualCameraService.DESCRIPTOR;
            }

            @Override // android.companion.virtualcamera.IVirtualCameraService
            public boolean registerCamera(android.os.IBinder token, android.companion.virtualcamera.VirtualCameraConfiguration configuration, int deviceId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.companion.virtualcamera.IVirtualCameraService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeTypedObject(configuration, 0);
                    _data.writeInt(deviceId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.companion.virtualcamera.IVirtualCameraService
            public void unregisterCamera(android.os.IBinder token) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.companion.virtualcamera.IVirtualCameraService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.companion.virtualcamera.IVirtualCameraService
            public java.lang.String getCameraId(android.os.IBinder token) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.companion.virtualcamera.IVirtualCameraService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
