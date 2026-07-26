package com.android.server.profcollect;

/* JADX INFO: loaded from: classes3.dex */
public interface IProfCollectd extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "com.android.server.profcollect.IProfCollectd";

    java.lang.String get_supported_provider() throws android.os.RemoteException;

    void process() throws android.os.RemoteException;

    void registerProviderStatusCallback(com.android.server.profcollect.IProviderStatusCallback iProviderStatusCallback) throws android.os.RemoteException;

    java.lang.String report(int i) throws android.os.RemoteException;

    void schedule() throws android.os.RemoteException;

    void terminate() throws android.os.RemoteException;

    void trace_once(java.lang.String str) throws android.os.RemoteException;

    public static class Default implements com.android.server.profcollect.IProfCollectd {
        @Override // com.android.server.profcollect.IProfCollectd
        public void schedule() throws android.os.RemoteException {
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public void terminate() throws android.os.RemoteException {
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public void trace_once(java.lang.String tag) throws android.os.RemoteException {
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public void process() throws android.os.RemoteException {
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public java.lang.String report(int usageSetting) throws android.os.RemoteException {
            return null;
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public java.lang.String get_supported_provider() throws android.os.RemoteException {
            return null;
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public void registerProviderStatusCallback(com.android.server.profcollect.IProviderStatusCallback cb) throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements com.android.server.profcollect.IProfCollectd {
        static final int TRANSACTION_get_supported_provider = 6;
        static final int TRANSACTION_process = 4;
        static final int TRANSACTION_registerProviderStatusCallback = 7;
        static final int TRANSACTION_report = 5;
        static final int TRANSACTION_schedule = 1;
        static final int TRANSACTION_terminate = 2;
        static final int TRANSACTION_trace_once = 3;

        public Stub() {
            attachInterface(this, com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
        }

        public static com.android.server.profcollect.IProfCollectd asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
            if (iin != null && (iin instanceof com.android.server.profcollect.IProfCollectd)) {
                return (com.android.server.profcollect.IProfCollectd) iin;
            }
            return new com.android.server.profcollect.IProfCollectd.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    schedule();
                    reply.writeNoException();
                    return true;
                case 2:
                    terminate();
                    reply.writeNoException();
                    return true;
                case 3:
                    java.lang.String _arg0 = data.readString();
                    data.enforceNoDataAvail();
                    trace_once(_arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    process();
                    reply.writeNoException();
                    return true;
                case 5:
                    int _arg02 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result = report(_arg02);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 6:
                    java.lang.String _result2 = get_supported_provider();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 7:
                    com.android.server.profcollect.IProviderStatusCallback _arg03 = com.android.server.profcollect.IProviderStatusCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    registerProviderStatusCallback(_arg03);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements com.android.server.profcollect.IProfCollectd {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return com.android.server.profcollect.IProfCollectd.DESCRIPTOR;
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void schedule() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void terminate() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void trace_once(java.lang.String tag) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
                    _data.writeString(tag);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void process() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public java.lang.String report(int usageSetting) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
                    _data.writeInt(usageSetting);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public java.lang.String get_supported_provider() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void registerProviderStatusCallback(com.android.server.profcollect.IProviderStatusCallback cb) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(com.android.server.profcollect.IProfCollectd.DESCRIPTOR);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
