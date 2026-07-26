package android.security.keystore;

/* JADX INFO: loaded from: classes.dex */
public interface IKeyAttestationApplicationIdProvider extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android.security.keystore.IKeyAttestationApplicationIdProvider";
    public static final int ERROR_GET_ATTESTATION_APPLICATION_ID_FAILED = 1;

    android.security.keystore.KeyAttestationApplicationId getKeyAttestationApplicationId(int i) throws android.os.RemoteException;

    public static class Default implements android.security.keystore.IKeyAttestationApplicationIdProvider {
        @Override // android.security.keystore.IKeyAttestationApplicationIdProvider
        public android.security.keystore.KeyAttestationApplicationId getKeyAttestationApplicationId(int uid) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.security.keystore.IKeyAttestationApplicationIdProvider {
        static final int TRANSACTION_getKeyAttestationApplicationId = 1;

        public Stub() {
            attachInterface(this, android.security.keystore.IKeyAttestationApplicationIdProvider.DESCRIPTOR);
        }

        public static android.security.keystore.IKeyAttestationApplicationIdProvider asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.security.keystore.IKeyAttestationApplicationIdProvider.DESCRIPTOR);
            if (iin != null && (iin instanceof android.security.keystore.IKeyAttestationApplicationIdProvider)) {
                return (android.security.keystore.IKeyAttestationApplicationIdProvider) iin;
            }
            return new android.security.keystore.IKeyAttestationApplicationIdProvider.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(android.security.keystore.IKeyAttestationApplicationIdProvider.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.security.keystore.IKeyAttestationApplicationIdProvider.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    data.enforceNoDataAvail();
                    android.security.keystore.KeyAttestationApplicationId _result = getKeyAttestationApplicationId(_arg0);
                    reply.writeNoException();
                    reply.writeTypedObject(_result, 1);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.security.keystore.IKeyAttestationApplicationIdProvider {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.security.keystore.IKeyAttestationApplicationIdProvider.DESCRIPTOR;
            }

            @Override // android.security.keystore.IKeyAttestationApplicationIdProvider
            public android.security.keystore.KeyAttestationApplicationId getKeyAttestationApplicationId(int uid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.security.keystore.IKeyAttestationApplicationIdProvider.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    android.security.keystore.KeyAttestationApplicationId _result = (android.security.keystore.KeyAttestationApplicationId) _reply.readTypedObject(android.security.keystore.KeyAttestationApplicationId.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
