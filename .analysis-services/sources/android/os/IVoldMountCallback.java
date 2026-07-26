package android.os;

/* JADX INFO: loaded from: classes.dex */
public interface IVoldMountCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android.os.IVoldMountCallback";

    boolean onVolumeChecking(java.io.FileDescriptor fileDescriptor, java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    public static class Default implements android.os.IVoldMountCallback {
        @Override // android.os.IVoldMountCallback
        public boolean onVolumeChecking(java.io.FileDescriptor fuseFd, java.lang.String path, java.lang.String internalPath) throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.os.IVoldMountCallback {
        static final int TRANSACTION_onVolumeChecking = 1;

        public Stub() {
            attachInterface(this, android.os.IVoldMountCallback.DESCRIPTOR);
        }

        public static android.os.IVoldMountCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.os.IVoldMountCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof android.os.IVoldMountCallback)) {
                return (android.os.IVoldMountCallback) iin;
            }
            return new android.os.IVoldMountCallback.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(android.os.IVoldMountCallback.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.os.IVoldMountCallback.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    java.io.FileDescriptor _arg0 = data.readRawFileDescriptor();
                    java.lang.String _arg1 = data.readString();
                    java.lang.String _arg2 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result = onVolumeChecking(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.os.IVoldMountCallback {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.IVoldMountCallback.DESCRIPTOR;
            }

            @Override // android.os.IVoldMountCallback
            public boolean onVolumeChecking(java.io.FileDescriptor fuseFd, java.lang.String path, java.lang.String internalPath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVoldMountCallback.DESCRIPTOR);
                    _data.writeRawFileDescriptor(fuseFd);
                    _data.writeString(path);
                    _data.writeString(internalPath);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
