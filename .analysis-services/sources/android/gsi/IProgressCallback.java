package android.gsi;

/* JADX INFO: loaded from: classes.dex */
public interface IProgressCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android.gsi.IProgressCallback";

    void onProgress(long j, long j2) throws android.os.RemoteException;

    public static class Default implements android.gsi.IProgressCallback {
        @Override // android.gsi.IProgressCallback
        public void onProgress(long current, long total) throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.gsi.IProgressCallback {
        static final int TRANSACTION_onProgress = 1;

        public Stub() {
            attachInterface(this, android.gsi.IProgressCallback.DESCRIPTOR);
        }

        public static android.gsi.IProgressCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.gsi.IProgressCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof android.gsi.IProgressCallback)) {
                return (android.gsi.IProgressCallback) iin;
            }
            return new android.gsi.IProgressCallback.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(android.gsi.IProgressCallback.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.gsi.IProgressCallback.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    long _arg0 = data.readLong();
                    long _arg1 = data.readLong();
                    data.enforceNoDataAvail();
                    onProgress(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.gsi.IProgressCallback {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.gsi.IProgressCallback.DESCRIPTOR;
            }

            @Override // android.gsi.IProgressCallback
            public void onProgress(long current, long total) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IProgressCallback.DESCRIPTOR);
                    _data.writeLong(current);
                    _data.writeLong(total);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
