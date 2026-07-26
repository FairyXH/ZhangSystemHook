package android.hardware.boot;

/* JADX INFO: loaded from: classes.dex */
public interface IBootControl extends android.os.IInterface {
    public static final int COMMAND_FAILED = -2;
    public static final java.lang.String DESCRIPTOR = "android$hardware$boot$IBootControl".replace('$', '.');
    public static final java.lang.String HASH = "2400346954240a5de495a1debc81429dd012d7b7";
    public static final int INVALID_SLOT = -1;
    public static final int VERSION = 1;

    int getActiveBootSlot() throws android.os.RemoteException;

    int getCurrentSlot() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    int getNumberSlots() throws android.os.RemoteException;

    int getSnapshotMergeStatus() throws android.os.RemoteException;

    java.lang.String getSuffix(int i) throws android.os.RemoteException;

    boolean isSlotBootable(int i) throws android.os.RemoteException;

    boolean isSlotMarkedSuccessful(int i) throws android.os.RemoteException;

    void markBootSuccessful() throws android.os.RemoteException;

    void setActiveBootSlot(int i) throws android.os.RemoteException;

    void setSlotAsUnbootable(int i) throws android.os.RemoteException;

    void setSnapshotMergeStatus(int i) throws android.os.RemoteException;

    public static class Default implements android.hardware.boot.IBootControl {
        @Override // android.hardware.boot.IBootControl
        public int getActiveBootSlot() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public int getCurrentSlot() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public int getNumberSlots() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public int getSnapshotMergeStatus() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public java.lang.String getSuffix(int slot) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.boot.IBootControl
        public boolean isSlotBootable(int slot) throws android.os.RemoteException {
            return false;
        }

        @Override // android.hardware.boot.IBootControl
        public boolean isSlotMarkedSuccessful(int slot) throws android.os.RemoteException {
            return false;
        }

        @Override // android.hardware.boot.IBootControl
        public void markBootSuccessful() throws android.os.RemoteException {
        }

        @Override // android.hardware.boot.IBootControl
        public void setActiveBootSlot(int slot) throws android.os.RemoteException {
        }

        @Override // android.hardware.boot.IBootControl
        public void setSlotAsUnbootable(int slot) throws android.os.RemoteException {
        }

        @Override // android.hardware.boot.IBootControl
        public void setSnapshotMergeStatus(int status) throws android.os.RemoteException {
        }

        @Override // android.hardware.boot.IBootControl
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.boot.IBootControl
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.boot.IBootControl {
        static final int TRANSACTION_getActiveBootSlot = 1;
        static final int TRANSACTION_getCurrentSlot = 2;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getNumberSlots = 3;
        static final int TRANSACTION_getSnapshotMergeStatus = 4;
        static final int TRANSACTION_getSuffix = 5;
        static final int TRANSACTION_isSlotBootable = 6;
        static final int TRANSACTION_isSlotMarkedSuccessful = 7;
        static final int TRANSACTION_markBootSuccessful = 8;
        static final int TRANSACTION_setActiveBootSlot = 9;
        static final int TRANSACTION_setSlotAsUnbootable = 10;
        static final int TRANSACTION_setSnapshotMergeStatus = 11;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.boot.IBootControl asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.boot.IBootControl)) {
                return (android.hardware.boot.IBootControl) iin;
            }
            return new android.hardware.boot.IBootControl.Stub.Proxy(obj);
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
                    int _result = getActiveBootSlot();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    int _result2 = getCurrentSlot();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    int _result3 = getNumberSlots();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 4:
                    int _result4 = getSnapshotMergeStatus();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    int _arg0 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result5 = getSuffix(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 6:
                    int _arg02 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result6 = isSlotBootable(_arg02);
                    reply.writeNoException();
                    reply.writeBoolean(_result6);
                    return true;
                case 7:
                    int _arg03 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result7 = isSlotMarkedSuccessful(_arg03);
                    reply.writeNoException();
                    reply.writeBoolean(_result7);
                    return true;
                case 8:
                    markBootSuccessful();
                    reply.writeNoException();
                    return true;
                case 9:
                    int _arg04 = data.readInt();
                    data.enforceNoDataAvail();
                    setActiveBootSlot(_arg04);
                    reply.writeNoException();
                    return true;
                case 10:
                    int _arg05 = data.readInt();
                    data.enforceNoDataAvail();
                    setSlotAsUnbootable(_arg05);
                    reply.writeNoException();
                    return true;
                case 11:
                    int _arg06 = data.readInt();
                    data.enforceNoDataAvail();
                    setSnapshotMergeStatus(_arg06);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.boot.IBootControl {
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

            @Override // android.hardware.boot.IBootControl
            public int getActiveBootSlot() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getActiveBootSlot is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public int getCurrentSlot() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getCurrentSlot is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public int getNumberSlots() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getNumberSlots is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public int getSnapshotMergeStatus() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getSnapshotMergeStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public java.lang.String getSuffix(int slot) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slot);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getSuffix is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public boolean isSlotBootable(int slot) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slot);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isSlotBootable is unimplemented.");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public boolean isSlotMarkedSuccessful(int slot) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slot);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isSlotMarkedSuccessful is unimplemented.");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public void markBootSuccessful() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method markBootSuccessful is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public void setActiveBootSlot(int slot) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slot);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setActiveBootSlot is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public void setSlotAsUnbootable(int slot) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slot);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setSlotAsUnbootable is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
            public void setSnapshotMergeStatus(int status) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setSnapshotMergeStatus is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.boot.IBootControl
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

            @Override // android.hardware.boot.IBootControl
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.boot.IBootControl.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
