package android.os;

/* JADX INFO: loaded from: classes.dex */
public interface IVoldTaskListener extends android.os.IInterface {
    void onFinished(int i, android.os.PersistableBundle persistableBundle) throws android.os.RemoteException;

    void onStatus(int i, android.os.PersistableBundle persistableBundle) throws android.os.RemoteException;

    public static class Default implements android.os.IVoldTaskListener {
        @Override // android.os.IVoldTaskListener
        public void onStatus(int status, android.os.PersistableBundle extras) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldTaskListener
        public void onFinished(int status, android.os.PersistableBundle extras) throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.os.IVoldTaskListener {
        public static final java.lang.String DESCRIPTOR = "android.os.IVoldTaskListener";
        static final int TRANSACTION_onFinished = 2;
        static final int TRANSACTION_onStatus = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.os.IVoldTaskListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.os.IVoldTaskListener)) {
                return (android.os.IVoldTaskListener) iin;
            }
            return new android.os.IVoldTaskListener.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    android.os.PersistableBundle _arg1 = (android.os.PersistableBundle) data.readTypedObject(android.os.PersistableBundle.CREATOR);
                    data.enforceNoDataAvail();
                    onStatus(_arg0, _arg1);
                    return true;
                case 2:
                    int _arg02 = data.readInt();
                    android.os.PersistableBundle _arg12 = (android.os.PersistableBundle) data.readTypedObject(android.os.PersistableBundle.CREATOR);
                    data.enforceNoDataAvail();
                    onFinished(_arg02, _arg12);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.os.IVoldTaskListener {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.IVoldTaskListener.Stub.DESCRIPTOR;
            }

            @Override // android.os.IVoldTaskListener
            public void onStatus(int status, android.os.PersistableBundle extras) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldTaskListener.Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeTypedObject(extras, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldTaskListener
            public void onFinished(int status, android.os.PersistableBundle extras) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldTaskListener.Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeTypedObject(extras, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
