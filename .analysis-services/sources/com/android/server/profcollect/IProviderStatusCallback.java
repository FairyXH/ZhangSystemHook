package com.android.server.profcollect;

/* JADX INFO: loaded from: classes3.dex */
public interface IProviderStatusCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "com.android.server.profcollect.IProviderStatusCallback";

    void onProviderReady() throws android.os.RemoteException;

    public static class Default implements com.android.server.profcollect.IProviderStatusCallback {
        @Override // com.android.server.profcollect.IProviderStatusCallback
        public void onProviderReady() throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements com.android.server.profcollect.IProviderStatusCallback {
        static final int TRANSACTION_onProviderReady = 1;

        public Stub() {
            attachInterface(this, com.android.server.profcollect.IProviderStatusCallback.DESCRIPTOR);
        }

        public static com.android.server.profcollect.IProviderStatusCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(com.android.server.profcollect.IProviderStatusCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof com.android.server.profcollect.IProviderStatusCallback)) {
                return (com.android.server.profcollect.IProviderStatusCallback) iin;
            }
            return new com.android.server.profcollect.IProviderStatusCallback.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(com.android.server.profcollect.IProviderStatusCallback.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(com.android.server.profcollect.IProviderStatusCallback.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    onProviderReady();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements com.android.server.profcollect.IProviderStatusCallback {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return com.android.server.profcollect.IProviderStatusCallback.DESCRIPTOR;
            }

            @Override // com.android.server.profcollect.IProviderStatusCallback
            public void onProviderReady() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(com.android.server.profcollect.IProviderStatusCallback.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
