package oplus.os;

/* JADX INFO: loaded from: classes.dex */
public interface IDefrag extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "oplus.os.IDefrag";

    void abortDefragFiles(oplus.os.IDefragTaskListener iDefragTaskListener) throws android.os.RemoteException;

    void abortScan(oplus.os.IDefragTaskListener iDefragTaskListener) throws android.os.RemoteException;

    void defragFiles(int i, oplus.os.IDefragTaskListener iDefragTaskListener) throws android.os.RemoteException;

    void startScan(oplus.os.IDefragTaskListener iDefragTaskListener) throws android.os.RemoteException;

    public static class Default implements oplus.os.IDefrag {
        @Override // oplus.os.IDefrag
        public void defragFiles(int foregroundUser, oplus.os.IDefragTaskListener listener) throws android.os.RemoteException {
        }

        @Override // oplus.os.IDefrag
        public void abortDefragFiles(oplus.os.IDefragTaskListener listener) throws android.os.RemoteException {
        }

        @Override // oplus.os.IDefrag
        public void startScan(oplus.os.IDefragTaskListener listener) throws android.os.RemoteException {
        }

        @Override // oplus.os.IDefrag
        public void abortScan(oplus.os.IDefragTaskListener listener) throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements oplus.os.IDefrag {
        static final int TRANSACTION_abortDefragFiles = 2;
        static final int TRANSACTION_abortScan = 4;
        static final int TRANSACTION_defragFiles = 1;
        static final int TRANSACTION_startScan = 3;

        public Stub() {
            attachInterface(this, oplus.os.IDefrag.DESCRIPTOR);
        }

        public static oplus.os.IDefrag asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(oplus.os.IDefrag.DESCRIPTOR);
            if (iin != null && (iin instanceof oplus.os.IDefrag)) {
                return (oplus.os.IDefrag) iin;
            }
            return new oplus.os.IDefrag.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(oplus.os.IDefrag.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(oplus.os.IDefrag.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    oplus.os.IDefragTaskListener _arg1 = oplus.os.IDefragTaskListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    defragFiles(_arg0, _arg1);
                    return true;
                case 2:
                    oplus.os.IDefragTaskListener _arg02 = oplus.os.IDefragTaskListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    abortDefragFiles(_arg02);
                    return true;
                case 3:
                    oplus.os.IDefragTaskListener _arg03 = oplus.os.IDefragTaskListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    startScan(_arg03);
                    return true;
                case 4:
                    oplus.os.IDefragTaskListener _arg04 = oplus.os.IDefragTaskListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    abortScan(_arg04);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements oplus.os.IDefrag {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return oplus.os.IDefrag.DESCRIPTOR;
            }

            @Override // oplus.os.IDefrag
            public void defragFiles(int foregroundUser, oplus.os.IDefragTaskListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(oplus.os.IDefrag.DESCRIPTOR);
                    _data.writeInt(foregroundUser);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // oplus.os.IDefrag
            public void abortDefragFiles(oplus.os.IDefragTaskListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(oplus.os.IDefrag.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // oplus.os.IDefrag
            public void startScan(oplus.os.IDefragTaskListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(oplus.os.IDefrag.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // oplus.os.IDefrag
            public void abortScan(oplus.os.IDefragTaskListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(oplus.os.IDefrag.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
