package android.hardware.tv.hdmi.connection;

/* JADX INFO: loaded from: classes.dex */
public interface IHdmiConnection extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$tv$hdmi$connection$IHdmiConnection".replace('$', '.');
    public static final java.lang.String HASH = "85c26fa47f3c3062aa93ffc8bb0897a85c8cb118";
    public static final int VERSION = 1;

    byte getHpdSignal(int i) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    android.hardware.tv.hdmi.connection.HdmiPortInfo[] getPortInfo() throws android.os.RemoteException;

    boolean isConnected(int i) throws android.os.RemoteException;

    void setCallback(android.hardware.tv.hdmi.connection.IHdmiConnectionCallback iHdmiConnectionCallback) throws android.os.RemoteException;

    void setHpdSignal(byte b, int i) throws android.os.RemoteException;

    public static class Default implements android.hardware.tv.hdmi.connection.IHdmiConnection {
        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public android.hardware.tv.hdmi.connection.HdmiPortInfo[] getPortInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public boolean isConnected(int portId) throws android.os.RemoteException {
            return false;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public void setCallback(android.hardware.tv.hdmi.connection.IHdmiConnectionCallback callback) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public void setHpdSignal(byte signal, int portId) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public byte getHpdSignal(int portId) throws android.os.RemoteException {
            return (byte) 0;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.tv.hdmi.connection.IHdmiConnection {
        static final int TRANSACTION_getHpdSignal = 5;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPortInfo = 1;
        static final int TRANSACTION_isConnected = 2;
        static final int TRANSACTION_setCallback = 3;
        static final int TRANSACTION_setHpdSignal = 4;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.tv.hdmi.connection.IHdmiConnection asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.tv.hdmi.connection.IHdmiConnection)) {
                return (android.hardware.tv.hdmi.connection.IHdmiConnection) iin;
            }
            return new android.hardware.tv.hdmi.connection.IHdmiConnection.Stub.Proxy(obj);
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
                    android.hardware.tv.hdmi.connection.HdmiPortInfo[] _result = getPortInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result, 1);
                    return true;
                case 2:
                    int _arg0 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result2 = isConnected(_arg0);
                    reply.writeNoException();
                    reply.writeBoolean(_result2);
                    return true;
                case 3:
                    android.hardware.tv.hdmi.connection.IHdmiConnectionCallback _arg02 = android.hardware.tv.hdmi.connection.IHdmiConnectionCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setCallback(_arg02);
                    reply.writeNoException();
                    return true;
                case 4:
                    byte _arg03 = data.readByte();
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    setHpdSignal(_arg03, _arg1);
                    reply.writeNoException();
                    return true;
                case 5:
                    int _arg04 = data.readInt();
                    data.enforceNoDataAvail();
                    byte _result3 = getHpdSignal(_arg04);
                    reply.writeNoException();
                    reply.writeByte(_result3);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.tv.hdmi.connection.IHdmiConnection {
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

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public android.hardware.tv.hdmi.connection.HdmiPortInfo[] getPortInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPortInfo is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.tv.hdmi.connection.HdmiPortInfo[] _result = (android.hardware.tv.hdmi.connection.HdmiPortInfo[]) _reply.createTypedArray(android.hardware.tv.hdmi.connection.HdmiPortInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public boolean isConnected(int portId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(portId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isConnected is unimplemented.");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public void setCallback(android.hardware.tv.hdmi.connection.IHdmiConnectionCallback callback) throws android.os.RemoteException {
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

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public void setHpdSignal(byte signal, int portId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByte(signal);
                    _data.writeInt(portId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setHpdSignal is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public byte getHpdSignal(int portId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(portId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getHpdSignal is unimplemented.");
                    }
                    _reply.readException();
                    byte _result = _reply.readByte();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
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

            @Override // android.hardware.tv.hdmi.connection.IHdmiConnection
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.tv.hdmi.connection.IHdmiConnection.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
