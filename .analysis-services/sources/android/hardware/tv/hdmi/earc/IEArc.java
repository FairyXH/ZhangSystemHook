package android.hardware.tv.hdmi.earc;

/* JADX INFO: loaded from: classes.dex */
public interface IEArc extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$tv$hdmi$earc$IEArc".replace('$', '.');
    public static final java.lang.String HASH = "101230f18c7b8438921e517e80eea4ccc7c1e463";
    public static final int VERSION = 1;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    byte[] getLastReportedAudioCapabilities(int i) throws android.os.RemoteException;

    byte getState(int i) throws android.os.RemoteException;

    boolean isEArcEnabled() throws android.os.RemoteException;

    void setCallback(android.hardware.tv.hdmi.earc.IEArcCallback iEArcCallback) throws android.os.RemoteException;

    void setEArcEnabled(boolean z) throws android.os.RemoteException;

    public static class Default implements android.hardware.tv.hdmi.earc.IEArc {
        @Override // android.hardware.tv.hdmi.earc.IEArc
        public void setEArcEnabled(boolean enabled) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public boolean isEArcEnabled() throws android.os.RemoteException {
            return false;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public void setCallback(android.hardware.tv.hdmi.earc.IEArcCallback callback) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public byte getState(int portId) throws android.os.RemoteException {
            return (byte) 0;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public byte[] getLastReportedAudioCapabilities(int portId) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.earc.IEArc
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.tv.hdmi.earc.IEArc {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getLastReportedAudioCapabilities = 5;
        static final int TRANSACTION_getState = 4;
        static final int TRANSACTION_isEArcEnabled = 2;
        static final int TRANSACTION_setCallback = 3;
        static final int TRANSACTION_setEArcEnabled = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.tv.hdmi.earc.IEArc asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.tv.hdmi.earc.IEArc)) {
                return (android.hardware.tv.hdmi.earc.IEArc) iin;
            }
            return new android.hardware.tv.hdmi.earc.IEArc.Stub.Proxy(obj);
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
                    data.enforceNoDataAvail();
                    setEArcEnabled(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    boolean _result = isEArcEnabled();
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    return true;
                case 3:
                    android.hardware.tv.hdmi.earc.IEArcCallback _arg02 = android.hardware.tv.hdmi.earc.IEArcCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setCallback(_arg02);
                    reply.writeNoException();
                    return true;
                case 4:
                    int _arg03 = data.readInt();
                    data.enforceNoDataAvail();
                    byte _result2 = getState(_arg03);
                    reply.writeNoException();
                    reply.writeByte(_result2);
                    return true;
                case 5:
                    int _arg04 = data.readInt();
                    data.enforceNoDataAvail();
                    byte[] _result3 = getLastReportedAudioCapabilities(_arg04);
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.tv.hdmi.earc.IEArc {
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

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public void setEArcEnabled(boolean enabled) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setEArcEnabled is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public boolean isEArcEnabled() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isEArcEnabled is unimplemented.");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public void setCallback(android.hardware.tv.hdmi.earc.IEArcCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setCallback is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public byte getState(int portId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(portId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getState is unimplemented.");
                    }
                    _reply.readException();
                    byte _result = _reply.readByte();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public byte[] getLastReportedAudioCapabilities(int portId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(portId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getLastReportedAudioCapabilities is unimplemented.");
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.earc.IEArc
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

            @Override // android.hardware.tv.hdmi.earc.IEArc
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.tv.hdmi.earc.IEArc.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
