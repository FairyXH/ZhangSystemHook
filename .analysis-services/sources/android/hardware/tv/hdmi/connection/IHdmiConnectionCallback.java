package android.hardware.tv.hdmi.connection;

/* JADX INFO: loaded from: classes.dex */
public interface IHdmiConnectionCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$tv$hdmi$connection$IHdmiConnectionCallback".replace('$', '.');
    public static final java.lang.String HASH = "85c26fa47f3c3062aa93ffc8bb0897a85c8cb118";
    public static final int VERSION = 1;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onHotplugEvent(boolean z, int i) throws android.os.RemoteException;

    public static class Default implements android.hardware.tv.hdmi.connection.IHdmiConnectionCallback {
        @Override // android.hardware.tv.hdmi.connection.IHdmiConnectionCallback
        public void onHotplugEvent(boolean connected, int portId) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnectionCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnectionCallback
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.tv.hdmi.connection.IHdmiConnectionCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onHotplugEvent = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.tv.hdmi.connection.IHdmiConnectionCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.tv.hdmi.connection.IHdmiConnectionCallback)) {
                return (android.hardware.tv.hdmi.connection.IHdmiConnectionCallback) iin;
            }
            return new android.hardware.tv.hdmi.connection.IHdmiConnectionCallback.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(descriptor);
            }
            if (code == 1598968902) {
                reply.writeString(descriptor);
                return true;
            }
            if (code == 16777215) {
                reply.writeNoException();
                reply.writeInt(getInterfaceVersion());
                return true;
            }
            if (code == TRANSACTION_getInterfaceHash) {
                reply.writeNoException();
                reply.writeString(getInterfaceHash());
                return true;
            }
            switch (code) {
                case 1:
                    boolean _arg0 = data.readBoolean();
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    onHotplugEvent(_arg0, _arg1);
                    break;
            }
            return true;
        }

        private static class Proxy implements android.hardware.tv.hdmi.connection.IHdmiConnectionCallback {
            private android.os.IBinder mRemote;
            private int mCachedVersion = -1;
            private java.lang.String mCachedHash = "-1";

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnectionCallback
            public void onHotplugEvent(boolean connected, int portId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(connected);
                    _data.writeInt(portId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onHotplugEvent is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnectionCallback
            public int getInterfaceVersion() throws android.os.RemoteException {
                if (this.mCachedVersion == -1) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(16777215, data, reply, 0);
                        reply.readException();
                        this.mCachedVersion = reply.readInt();
                    } finally {
                        reply.recycle();
                        data.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnectionCallback
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.tv.hdmi.connection.IHdmiConnectionCallback.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
                        reply.readException();
                        this.mCachedHash = reply.readString();
                        reply.recycle();
                        data.recycle();
                    } catch (java.lang.Throwable th) {
                        reply.recycle();
                        data.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }
}
