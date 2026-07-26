package android.hardware.tv.hdmi.earc;

/* JADX INFO: loaded from: classes.dex */
public interface IEArcCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$tv$hdmi$earc$IEArcCallback".replace('$', '.');
    public static final java.lang.String HASH = "101230f18c7b8438921e517e80eea4ccc7c1e463";
    public static final int VERSION = 1;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onCapabilitiesReported(byte[] bArr, int i) throws android.os.RemoteException;

    void onStateChange(byte b, int i) throws android.os.RemoteException;

    public static class Default implements android.hardware.tv.hdmi.earc.IEArcCallback {
        @Override // android.hardware.tv.hdmi.earc.IEArcCallback
        public void onStateChange(byte status, int portId) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.earc.IEArcCallback
        public void onCapabilitiesReported(byte[] rawCapabilities, int portId) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.earc.IEArcCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArcCallback
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.tv.hdmi.earc.IEArcCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onCapabilitiesReported = 2;
        static final int TRANSACTION_onStateChange = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.tv.hdmi.earc.IEArcCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.tv.hdmi.earc.IEArcCallback)) {
                return (android.hardware.tv.hdmi.earc.IEArcCallback) iin;
            }
            return new android.hardware.tv.hdmi.earc.IEArcCallback.Stub.Proxy(obj);
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
                    byte _arg0 = data.readByte();
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    onStateChange(_arg0, _arg1);
                    return true;
                case 2:
                    byte[] _arg02 = data.createByteArray();
                    int _arg12 = data.readInt();
                    data.enforceNoDataAvail();
                    onCapabilitiesReported(_arg02, _arg12);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.tv.hdmi.earc.IEArcCallback {
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

            @Override // android.hardware.tv.hdmi.earc.IEArcCallback
            public void onStateChange(byte status, int portId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByte(status);
                    _data.writeInt(portId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onStateChange is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArcCallback
            public void onCapabilitiesReported(byte[] rawCapabilities, int portId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByteArray(rawCapabilities);
                    _data.writeInt(portId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onCapabilitiesReported is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArcCallback
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

            @Override // android.hardware.tv.hdmi.earc.IEArcCallback
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.tv.hdmi.earc.IEArcCallback.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
