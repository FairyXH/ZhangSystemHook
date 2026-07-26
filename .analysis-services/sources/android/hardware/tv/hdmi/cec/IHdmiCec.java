package android.hardware.tv.hdmi.cec;

/* JADX INFO: loaded from: classes.dex */
public interface IHdmiCec extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$tv$hdmi$cec$IHdmiCec".replace('$', '.');
    public static final java.lang.String HASH = "cd956e3a0c2e6ade71693c85e9f0aeffa221ea26";
    public static final int VERSION = 1;

    byte addLogicalAddress(byte b) throws android.os.RemoteException;

    void clearLogicalAddress() throws android.os.RemoteException;

    void enableAudioReturnChannel(int i, boolean z) throws android.os.RemoteException;

    void enableCec(boolean z) throws android.os.RemoteException;

    void enableSystemCecControl(boolean z) throws android.os.RemoteException;

    void enableWakeupByOtp(boolean z) throws android.os.RemoteException;

    int getCecVersion() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    int getPhysicalAddress() throws android.os.RemoteException;

    int getVendorId() throws android.os.RemoteException;

    byte sendMessage(android.hardware.tv.hdmi.cec.CecMessage cecMessage) throws android.os.RemoteException;

    void setCallback(android.hardware.tv.hdmi.cec.IHdmiCecCallback iHdmiCecCallback) throws android.os.RemoteException;

    void setLanguage(java.lang.String str) throws android.os.RemoteException;

    public static class Default implements android.hardware.tv.hdmi.cec.IHdmiCec {
        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public byte addLogicalAddress(byte addr) throws android.os.RemoteException {
            return (byte) 0;
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public void clearLogicalAddress() throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public void enableAudioReturnChannel(int portId, boolean enable) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public int getCecVersion() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public int getPhysicalAddress() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public int getVendorId() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public byte sendMessage(android.hardware.tv.hdmi.cec.CecMessage message) throws android.os.RemoteException {
            return (byte) 0;
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public void setCallback(android.hardware.tv.hdmi.cec.IHdmiCecCallback callback) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public void setLanguage(java.lang.String language) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public void enableWakeupByOtp(boolean value) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public void enableCec(boolean value) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public void enableSystemCecControl(boolean value) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCec
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.tv.hdmi.cec.IHdmiCec {
        static final int TRANSACTION_addLogicalAddress = 1;
        static final int TRANSACTION_clearLogicalAddress = 2;
        static final int TRANSACTION_enableAudioReturnChannel = 3;
        static final int TRANSACTION_enableCec = 11;
        static final int TRANSACTION_enableSystemCecControl = 12;
        static final int TRANSACTION_enableWakeupByOtp = 10;
        static final int TRANSACTION_getCecVersion = 4;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPhysicalAddress = 5;
        static final int TRANSACTION_getVendorId = 6;
        static final int TRANSACTION_sendMessage = 7;
        static final int TRANSACTION_setCallback = 8;
        static final int TRANSACTION_setLanguage = 9;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.tv.hdmi.cec.IHdmiCec asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.tv.hdmi.cec.IHdmiCec)) {
                return (android.hardware.tv.hdmi.cec.IHdmiCec) iin;
            }
            return new android.hardware.tv.hdmi.cec.IHdmiCec.Stub.Proxy(obj);
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
                    data.enforceNoDataAvail();
                    byte _result = addLogicalAddress(_arg0);
                    reply.writeNoException();
                    reply.writeByte(_result);
                    return true;
                case 2:
                    clearLogicalAddress();
                    reply.writeNoException();
                    return true;
                case 3:
                    int _arg02 = data.readInt();
                    boolean _arg1 = data.readBoolean();
                    data.enforceNoDataAvail();
                    enableAudioReturnChannel(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 4:
                    int _result2 = getCecVersion();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 5:
                    int _result3 = getPhysicalAddress();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 6:
                    int _result4 = getVendorId();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 7:
                    android.hardware.tv.hdmi.cec.CecMessage _arg03 = (android.hardware.tv.hdmi.cec.CecMessage) data.readTypedObject(android.hardware.tv.hdmi.cec.CecMessage.CREATOR);
                    data.enforceNoDataAvail();
                    byte _result5 = sendMessage(_arg03);
                    reply.writeNoException();
                    reply.writeByte(_result5);
                    return true;
                case 8:
                    android.hardware.tv.hdmi.cec.IHdmiCecCallback _arg04 = android.hardware.tv.hdmi.cec.IHdmiCecCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setCallback(_arg04);
                    reply.writeNoException();
                    return true;
                case 9:
                    java.lang.String _arg05 = data.readString();
                    data.enforceNoDataAvail();
                    setLanguage(_arg05);
                    reply.writeNoException();
                    return true;
                case 10:
                    boolean _arg06 = data.readBoolean();
                    data.enforceNoDataAvail();
                    enableWakeupByOtp(_arg06);
                    reply.writeNoException();
                    return true;
                case 11:
                    boolean _arg07 = data.readBoolean();
                    data.enforceNoDataAvail();
                    enableCec(_arg07);
                    reply.writeNoException();
                    return true;
                case 12:
                    boolean _arg08 = data.readBoolean();
                    data.enforceNoDataAvail();
                    enableSystemCecControl(_arg08);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.tv.hdmi.cec.IHdmiCec {
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

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public byte addLogicalAddress(byte addr) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByte(addr);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method addLogicalAddress is unimplemented.");
                    }
                    _reply.readException();
                    byte _result = _reply.readByte();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public void clearLogicalAddress() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method clearLogicalAddress is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public void enableAudioReturnChannel(int portId, boolean enable) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(portId);
                    _data.writeBoolean(enable);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method enableAudioReturnChannel is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public int getCecVersion() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getCecVersion is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public int getPhysicalAddress() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPhysicalAddress is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public int getVendorId() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getVendorId is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public byte sendMessage(android.hardware.tv.hdmi.cec.CecMessage message) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(message, 0);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method sendMessage is unimplemented.");
                    }
                    _reply.readException();
                    byte _result = _reply.readByte();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public void setCallback(android.hardware.tv.hdmi.cec.IHdmiCecCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setCallback is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public void setLanguage(java.lang.String language) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(language);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLanguage is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public void enableWakeupByOtp(boolean value) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(value);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method enableWakeupByOtp is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public void enableCec(boolean value) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(value);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method enableCec is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public void enableSystemCecControl(boolean value) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(value);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method enableSystemCecControl is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
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

            @Override // android.hardware.tv.hdmi.cec.IHdmiCec
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.tv.hdmi.cec.IHdmiCec.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
