package android.gsi;

/* JADX INFO: loaded from: classes.dex */
public interface IGsiServiceCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android.gsi.IGsiServiceCallback";

    void onResult(int i) throws android.os.RemoteException;

    public static class Default implements android.gsi.IGsiServiceCallback {
        @Override // android.gsi.IGsiServiceCallback
        public void onResult(int result) throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.gsi.IGsiServiceCallback {
        static final int TRANSACTION_onResult = 1;

        public Stub() {
            attachInterface(this, android.gsi.IGsiServiceCallback.DESCRIPTOR);
        }

        public static android.gsi.IGsiServiceCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.gsi.IGsiServiceCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof android.gsi.IGsiServiceCallback)) {
                return (android.gsi.IGsiServiceCallback) iin;
            }
            return new android.gsi.IGsiServiceCallback.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(android.gsi.IGsiServiceCallback.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.gsi.IGsiServiceCallback.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    data.enforceNoDataAvail();
                    onResult(_arg0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.gsi.IGsiServiceCallback {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.gsi.IGsiServiceCallback.DESCRIPTOR;
            }

            @Override // android.gsi.IGsiServiceCallback
            public void onResult(int result) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiServiceCallback.DESCRIPTOR);
                    _data.writeInt(result);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
