package android.hardware.usb;

/* JADX INFO: loaded from: classes.dex */
public interface IUsb extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$usb$IUsb".replace('$', '.');
    public static final java.lang.String HASH = "7fe46e9531884739d925b8caeee9dba5c411e228";
    public static final int VERSION = 3;

    void enableContaminantPresenceDetection(java.lang.String str, boolean z, long j) throws android.os.RemoteException;

    void enableUsbData(java.lang.String str, boolean z, long j) throws android.os.RemoteException;

    void enableUsbDataWhileDocked(java.lang.String str, long j) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void limitPowerTransfer(java.lang.String str, boolean z, long j) throws android.os.RemoteException;

    void queryPortStatus(long j) throws android.os.RemoteException;

    void resetUsbPort(java.lang.String str, long j) throws android.os.RemoteException;

    void setCallback(android.hardware.usb.IUsbCallback iUsbCallback) throws android.os.RemoteException;

    void switchRole(java.lang.String str, android.hardware.usb.PortRole portRole, long j) throws android.os.RemoteException;

    public static class Default implements android.hardware.usb.IUsb {
        @Override // android.hardware.usb.IUsb
        public void enableContaminantPresenceDetection(java.lang.String portName, boolean enable, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void enableUsbData(java.lang.String portName, boolean enable, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void enableUsbDataWhileDocked(java.lang.String portName, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void queryPortStatus(long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void setCallback(android.hardware.usb.IUsbCallback callback) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void switchRole(java.lang.String portName, android.hardware.usb.PortRole role, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void limitPowerTransfer(java.lang.String portName, boolean limit, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public void resetUsbPort(java.lang.String portName, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsb
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.usb.IUsb
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.usb.IUsb {
        static final int TRANSACTION_enableContaminantPresenceDetection = 1;
        static final int TRANSACTION_enableUsbData = 2;
        static final int TRANSACTION_enableUsbDataWhileDocked = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_limitPowerTransfer = 7;
        static final int TRANSACTION_queryPortStatus = 4;
        static final int TRANSACTION_resetUsbPort = 8;
        static final int TRANSACTION_setCallback = 5;
        static final int TRANSACTION_switchRole = 6;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.usb.IUsb asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.usb.IUsb)) {
                return (android.hardware.usb.IUsb) iin;
            }
            return new android.hardware.usb.IUsb.Stub.Proxy(obj);
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
                    java.lang.String _arg0 = data.readString();
                    boolean _arg1 = data.readBoolean();
                    long _arg2 = data.readLong();
                    data.enforceNoDataAvail();
                    enableContaminantPresenceDetection(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    java.lang.String _arg02 = data.readString();
                    boolean _arg12 = data.readBoolean();
                    long _arg22 = data.readLong();
                    data.enforceNoDataAvail();
                    enableUsbData(_arg02, _arg12, _arg22);
                    return true;
                case 3:
                    java.lang.String _arg03 = data.readString();
                    long _arg13 = data.readLong();
                    data.enforceNoDataAvail();
                    enableUsbDataWhileDocked(_arg03, _arg13);
                    return true;
                case 4:
                    long _arg04 = data.readLong();
                    data.enforceNoDataAvail();
                    queryPortStatus(_arg04);
                    return true;
                case 5:
                    android.hardware.usb.IUsbCallback _arg05 = android.hardware.usb.IUsbCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setCallback(_arg05);
                    return true;
                case 6:
                    java.lang.String _arg06 = data.readString();
                    android.hardware.usb.PortRole _arg14 = (android.hardware.usb.PortRole) data.readTypedObject(android.hardware.usb.PortRole.CREATOR);
                    long _arg23 = data.readLong();
                    data.enforceNoDataAvail();
                    switchRole(_arg06, _arg14, _arg23);
                    return true;
                case 7:
                    java.lang.String _arg07 = data.readString();
                    boolean _arg15 = data.readBoolean();
                    long _arg24 = data.readLong();
                    data.enforceNoDataAvail();
                    limitPowerTransfer(_arg07, _arg15, _arg24);
                    return true;
                case 8:
                    java.lang.String _arg08 = data.readString();
                    long _arg16 = data.readLong();
                    data.enforceNoDataAvail();
                    resetUsbPort(_arg08, _arg16);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.usb.IUsb {
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

            @Override // android.hardware.usb.IUsb
            public void enableContaminantPresenceDetection(java.lang.String portName, boolean enable, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeBoolean(enable);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method enableContaminantPresenceDetection is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void enableUsbData(java.lang.String portName, boolean enable, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeBoolean(enable);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method enableUsbData is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void enableUsbDataWhileDocked(java.lang.String portName, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method enableUsbDataWhileDocked is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void queryPortStatus(long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method queryPortStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void setCallback(android.hardware.usb.IUsbCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setCallback is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void switchRole(java.lang.String portName, android.hardware.usb.PortRole role, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeTypedObject(role, 0);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method switchRole is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void limitPowerTransfer(java.lang.String portName, boolean limit, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeBoolean(limit);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method limitPowerTransfer is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
            public void resetUsbPort(java.lang.String portName, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method resetUsbPort is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsb
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

            @Override // android.hardware.usb.IUsb
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.usb.IUsb.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
