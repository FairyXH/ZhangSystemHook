package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public interface ITunerCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$broadcastradio$ITunerCallback".replace('$', '.');
    public static final java.lang.String HASH = "bff68a8bc8b7cc191ab62bee10f7df8e79494467";
    public static final int VERSION = 2;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onAntennaStateChange(boolean z) throws android.os.RemoteException;

    void onConfigFlagUpdated(int i, boolean z) throws android.os.RemoteException;

    void onCurrentProgramInfoChanged(android.hardware.broadcastradio.ProgramInfo programInfo) throws android.os.RemoteException;

    void onParametersUpdated(android.hardware.broadcastradio.VendorKeyValue[] vendorKeyValueArr) throws android.os.RemoteException;

    void onProgramListUpdated(android.hardware.broadcastradio.ProgramListChunk programListChunk) throws android.os.RemoteException;

    void onTuneFailed(int i, android.hardware.broadcastradio.ProgramSelector programSelector) throws android.os.RemoteException;

    public static class Default implements android.hardware.broadcastradio.ITunerCallback {
        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onTuneFailed(int result, android.hardware.broadcastradio.ProgramSelector selector) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onCurrentProgramInfoChanged(android.hardware.broadcastradio.ProgramInfo info) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onProgramListUpdated(android.hardware.broadcastradio.ProgramListChunk chunk) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onAntennaStateChange(boolean connected) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onConfigFlagUpdated(int flag, boolean value) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onParametersUpdated(android.hardware.broadcastradio.VendorKeyValue[] parameters) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.broadcastradio.ITunerCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onAntennaStateChange = 4;
        static final int TRANSACTION_onConfigFlagUpdated = 5;
        static final int TRANSACTION_onCurrentProgramInfoChanged = 2;
        static final int TRANSACTION_onParametersUpdated = 6;
        static final int TRANSACTION_onProgramListUpdated = 3;
        static final int TRANSACTION_onTuneFailed = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.broadcastradio.ITunerCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.broadcastradio.ITunerCallback)) {
                return (android.hardware.broadcastradio.ITunerCallback) iin;
            }
            return new android.hardware.broadcastradio.ITunerCallback.Stub.Proxy(obj);
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
                    int _arg0 = data.readInt();
                    android.hardware.broadcastradio.ProgramSelector _arg1 = (android.hardware.broadcastradio.ProgramSelector) data.readTypedObject(android.hardware.broadcastradio.ProgramSelector.CREATOR);
                    data.enforceNoDataAvail();
                    onTuneFailed(_arg0, _arg1);
                    return true;
                case 2:
                    android.hardware.broadcastradio.ProgramInfo _arg02 = (android.hardware.broadcastradio.ProgramInfo) data.readTypedObject(android.hardware.broadcastradio.ProgramInfo.CREATOR);
                    data.enforceNoDataAvail();
                    onCurrentProgramInfoChanged(_arg02);
                    return true;
                case 3:
                    android.hardware.broadcastradio.ProgramListChunk _arg03 = (android.hardware.broadcastradio.ProgramListChunk) data.readTypedObject(android.hardware.broadcastradio.ProgramListChunk.CREATOR);
                    data.enforceNoDataAvail();
                    onProgramListUpdated(_arg03);
                    return true;
                case 4:
                    boolean _arg04 = data.readBoolean();
                    data.enforceNoDataAvail();
                    onAntennaStateChange(_arg04);
                    return true;
                case 5:
                    int _arg05 = data.readInt();
                    boolean _arg12 = data.readBoolean();
                    data.enforceNoDataAvail();
                    onConfigFlagUpdated(_arg05, _arg12);
                    return true;
                case 6:
                    android.hardware.broadcastradio.VendorKeyValue[] _arg06 = (android.hardware.broadcastradio.VendorKeyValue[]) data.createTypedArray(android.hardware.broadcastradio.VendorKeyValue.CREATOR);
                    data.enforceNoDataAvail();
                    onParametersUpdated(_arg06);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.broadcastradio.ITunerCallback {
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

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onTuneFailed(int result, android.hardware.broadcastradio.ProgramSelector selector) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeTypedObject(selector, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onTuneFailed is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onCurrentProgramInfoChanged(android.hardware.broadcastradio.ProgramInfo info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onCurrentProgramInfoChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onProgramListUpdated(android.hardware.broadcastradio.ProgramListChunk chunk) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(chunk, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onProgramListUpdated is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onAntennaStateChange(boolean connected) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(connected);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onAntennaStateChange is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onConfigFlagUpdated(int flag, boolean value) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(flag);
                    _data.writeBoolean(value);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onConfigFlagUpdated is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
            public void onParametersUpdated(android.hardware.broadcastradio.VendorKeyValue[] parameters) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedArray(parameters, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onParametersUpdated is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.ITunerCallback
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

            @Override // android.hardware.broadcastradio.ITunerCallback
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.broadcastradio.ITunerCallback.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
