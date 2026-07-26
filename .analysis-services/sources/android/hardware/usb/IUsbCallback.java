package android.hardware.usb;

/* JADX INFO: loaded from: classes.dex */
public interface IUsbCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$usb$IUsbCallback".replace('$', '.');
    public static final java.lang.String HASH = "7fe46e9531884739d925b8caeee9dba5c411e228";
    public static final int VERSION = 3;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void notifyContaminantEnabledStatus(java.lang.String str, boolean z, int i, long j) throws android.os.RemoteException;

    void notifyEnableUsbDataStatus(java.lang.String str, boolean z, int i, long j) throws android.os.RemoteException;

    void notifyEnableUsbDataWhileDockedStatus(java.lang.String str, int i, long j) throws android.os.RemoteException;

    void notifyLimitPowerTransferStatus(java.lang.String str, boolean z, int i, long j) throws android.os.RemoteException;

    void notifyPortStatusChange(android.hardware.usb.PortStatus[] portStatusArr, int i) throws android.os.RemoteException;

    void notifyQueryPortStatus(java.lang.String str, int i, long j) throws android.os.RemoteException;

    void notifyResetUsbPortStatus(java.lang.String str, int i, long j) throws android.os.RemoteException;

    void notifyRoleSwitchStatus(java.lang.String str, android.hardware.usb.PortRole portRole, int i, long j) throws android.os.RemoteException;

    public static class Default implements android.hardware.usb.IUsbCallback {
        @Override // android.hardware.usb.IUsbCallback
        public void notifyPortStatusChange(android.hardware.usb.PortStatus[] currentPortStatus, int retval) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyRoleSwitchStatus(java.lang.String portName, android.hardware.usb.PortRole newRole, int retval, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyEnableUsbDataStatus(java.lang.String portName, boolean enable, int retval, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyEnableUsbDataWhileDockedStatus(java.lang.String portName, int retval, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyContaminantEnabledStatus(java.lang.String portName, boolean enable, int retval, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyQueryPortStatus(java.lang.String portName, int retval, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyLimitPowerTransferStatus(java.lang.String portName, boolean limit, int retval, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyResetUsbPortStatus(java.lang.String portName, int retval, long transactionId) throws android.os.RemoteException {
        }

        @Override // android.hardware.usb.IUsbCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.usb.IUsbCallback
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.usb.IUsbCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_notifyContaminantEnabledStatus = 5;
        static final int TRANSACTION_notifyEnableUsbDataStatus = 3;
        static final int TRANSACTION_notifyEnableUsbDataWhileDockedStatus = 4;
        static final int TRANSACTION_notifyLimitPowerTransferStatus = 7;
        static final int TRANSACTION_notifyPortStatusChange = 1;
        static final int TRANSACTION_notifyQueryPortStatus = 6;
        static final int TRANSACTION_notifyResetUsbPortStatus = 8;
        static final int TRANSACTION_notifyRoleSwitchStatus = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.usb.IUsbCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.usb.IUsbCallback)) {
                return (android.hardware.usb.IUsbCallback) iin;
            }
            return new android.hardware.usb.IUsbCallback.Stub.Proxy(obj);
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
                    android.hardware.usb.PortStatus[] _arg0 = (android.hardware.usb.PortStatus[]) data.createTypedArray(android.hardware.usb.PortStatus.CREATOR);
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    notifyPortStatusChange(_arg0, _arg1);
                    return true;
                case 2:
                    java.lang.String _arg02 = data.readString();
                    android.hardware.usb.PortRole _arg12 = (android.hardware.usb.PortRole) data.readTypedObject(android.hardware.usb.PortRole.CREATOR);
                    int _arg2 = data.readInt();
                    long _arg3 = data.readLong();
                    data.enforceNoDataAvail();
                    notifyRoleSwitchStatus(_arg02, _arg12, _arg2, _arg3);
                    return true;
                case 3:
                    java.lang.String _arg03 = data.readString();
                    boolean _arg13 = data.readBoolean();
                    int _arg22 = data.readInt();
                    long _arg32 = data.readLong();
                    data.enforceNoDataAvail();
                    notifyEnableUsbDataStatus(_arg03, _arg13, _arg22, _arg32);
                    return true;
                case 4:
                    java.lang.String _arg04 = data.readString();
                    int _arg14 = data.readInt();
                    long _arg23 = data.readLong();
                    data.enforceNoDataAvail();
                    notifyEnableUsbDataWhileDockedStatus(_arg04, _arg14, _arg23);
                    return true;
                case 5:
                    java.lang.String _arg05 = data.readString();
                    boolean _arg15 = data.readBoolean();
                    int _arg24 = data.readInt();
                    long _arg33 = data.readLong();
                    data.enforceNoDataAvail();
                    notifyContaminantEnabledStatus(_arg05, _arg15, _arg24, _arg33);
                    return true;
                case 6:
                    java.lang.String _arg06 = data.readString();
                    int _arg16 = data.readInt();
                    long _arg25 = data.readLong();
                    data.enforceNoDataAvail();
                    notifyQueryPortStatus(_arg06, _arg16, _arg25);
                    return true;
                case 7:
                    java.lang.String _arg07 = data.readString();
                    boolean _arg17 = data.readBoolean();
                    int _arg26 = data.readInt();
                    long _arg34 = data.readLong();
                    data.enforceNoDataAvail();
                    notifyLimitPowerTransferStatus(_arg07, _arg17, _arg26, _arg34);
                    return true;
                case 8:
                    java.lang.String _arg08 = data.readString();
                    int _arg18 = data.readInt();
                    long _arg27 = data.readLong();
                    data.enforceNoDataAvail();
                    notifyResetUsbPortStatus(_arg08, _arg18, _arg27);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.usb.IUsbCallback {
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

            @Override // android.hardware.usb.IUsbCallback
            public void notifyPortStatusChange(android.hardware.usb.PortStatus[] currentPortStatus, int retval) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedArray(currentPortStatus, 0);
                    _data.writeInt(retval);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyPortStatusChange is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyRoleSwitchStatus(java.lang.String portName, android.hardware.usb.PortRole newRole, int retval, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeTypedObject(newRole, 0);
                    _data.writeInt(retval);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyRoleSwitchStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyEnableUsbDataStatus(java.lang.String portName, boolean enable, int retval, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeBoolean(enable);
                    _data.writeInt(retval);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyEnableUsbDataStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyEnableUsbDataWhileDockedStatus(java.lang.String portName, int retval, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeInt(retval);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyEnableUsbDataWhileDockedStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyContaminantEnabledStatus(java.lang.String portName, boolean enable, int retval, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeBoolean(enable);
                    _data.writeInt(retval);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyContaminantEnabledStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyQueryPortStatus(java.lang.String portName, int retval, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeInt(retval);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyQueryPortStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyLimitPowerTransferStatus(java.lang.String portName, boolean limit, int retval, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeBoolean(limit);
                    _data.writeInt(retval);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyLimitPowerTransferStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
            public void notifyResetUsbPortStatus(java.lang.String portName, int retval, long transactionId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(portName);
                    _data.writeInt(retval);
                    _data.writeLong(transactionId);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyResetUsbPortStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.usb.IUsbCallback
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

            @Override // android.hardware.usb.IUsbCallback
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.usb.IUsbCallback.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
