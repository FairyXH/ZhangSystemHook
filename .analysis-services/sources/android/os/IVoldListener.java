package android.os;

/* JADX INFO: loaded from: classes.dex */
public interface IVoldListener extends android.os.IInterface {
    void onDiskCreated(java.lang.String str, int i) throws android.os.RemoteException;

    void onDiskDestroyed(java.lang.String str) throws android.os.RemoteException;

    void onDiskMetadataChanged(java.lang.String str, long j, java.lang.String str2, java.lang.String str3) throws android.os.RemoteException;

    void onDiskScanned(java.lang.String str) throws android.os.RemoteException;

    void onDiskStateChanged(java.lang.String str) throws android.os.RemoteException;

    void onVolumeChecked(java.lang.String str, int i, java.lang.String str2, java.lang.String str3) throws android.os.RemoteException;

    void onVolumeCreated(java.lang.String str, int i, java.lang.String str2, java.lang.String str3, int i2) throws android.os.RemoteException;

    void onVolumeDestroyed(java.lang.String str) throws android.os.RemoteException;

    void onVolumeInternalPathChanged(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void onVolumeMetadataChanged(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) throws android.os.RemoteException;

    void onVolumePathChanged(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void onVolumeStateChanged(java.lang.String str, int i, int i2) throws android.os.RemoteException;

    public static class Default implements android.os.IVoldListener {
        @Override // android.os.IVoldListener
        public void onDiskCreated(java.lang.String diskId, int flags) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onDiskScanned(java.lang.String diskId) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onDiskMetadataChanged(java.lang.String diskId, long sizeBytes, java.lang.String label, java.lang.String sysPath) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onDiskDestroyed(java.lang.String diskId) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onVolumeCreated(java.lang.String volId, int type, java.lang.String diskId, java.lang.String partGuid, int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onVolumeStateChanged(java.lang.String volId, int state, int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onVolumeMetadataChanged(java.lang.String volId, java.lang.String fsType, java.lang.String fsUuid, java.lang.String fsLabel) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onVolumePathChanged(java.lang.String volId, java.lang.String path) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onVolumeInternalPathChanged(java.lang.String volId, java.lang.String internalPath) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onVolumeDestroyed(java.lang.String volId) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onDiskStateChanged(java.lang.String volId) throws android.os.RemoteException {
        }

        @Override // android.os.IVoldListener
        public void onVolumeChecked(java.lang.String volId, int type, java.lang.String diskId, java.lang.String partGuid) throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.os.IVoldListener {
        public static final java.lang.String DESCRIPTOR = "android.os.IVoldListener";
        static final int TRANSACTION_onDiskCreated = 1;
        static final int TRANSACTION_onDiskDestroyed = 4;
        static final int TRANSACTION_onDiskMetadataChanged = 3;
        static final int TRANSACTION_onDiskScanned = 2;
        static final int TRANSACTION_onDiskStateChanged = 11;
        static final int TRANSACTION_onVolumeChecked = 12;
        static final int TRANSACTION_onVolumeCreated = 5;
        static final int TRANSACTION_onVolumeDestroyed = 10;
        static final int TRANSACTION_onVolumeInternalPathChanged = 9;
        static final int TRANSACTION_onVolumeMetadataChanged = 7;
        static final int TRANSACTION_onVolumePathChanged = 8;
        static final int TRANSACTION_onVolumeStateChanged = 6;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.os.IVoldListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.os.IVoldListener)) {
                return (android.os.IVoldListener) iin;
            }
            return new android.os.IVoldListener.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    java.lang.String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    onDiskCreated(_arg0, _arg1);
                    return true;
                case 2:
                    java.lang.String _arg02 = data.readString();
                    data.enforceNoDataAvail();
                    onDiskScanned(_arg02);
                    return true;
                case 3:
                    java.lang.String _arg03 = data.readString();
                    long _arg12 = data.readLong();
                    java.lang.String _arg2 = data.readString();
                    java.lang.String _arg3 = data.readString();
                    data.enforceNoDataAvail();
                    onDiskMetadataChanged(_arg03, _arg12, _arg2, _arg3);
                    return true;
                case 4:
                    java.lang.String _arg04 = data.readString();
                    data.enforceNoDataAvail();
                    onDiskDestroyed(_arg04);
                    return true;
                case 5:
                    java.lang.String _arg05 = data.readString();
                    int _arg13 = data.readInt();
                    java.lang.String _arg22 = data.readString();
                    java.lang.String _arg32 = data.readString();
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    onVolumeCreated(_arg05, _arg13, _arg22, _arg32, _arg4);
                    return true;
                case 6:
                    java.lang.String _arg06 = data.readString();
                    int _arg14 = data.readInt();
                    int _arg23 = data.readInt();
                    data.enforceNoDataAvail();
                    onVolumeStateChanged(_arg06, _arg14, _arg23);
                    return true;
                case 7:
                    java.lang.String _arg07 = data.readString();
                    java.lang.String _arg15 = data.readString();
                    java.lang.String _arg24 = data.readString();
                    java.lang.String _arg33 = data.readString();
                    data.enforceNoDataAvail();
                    onVolumeMetadataChanged(_arg07, _arg15, _arg24, _arg33);
                    return true;
                case 8:
                    java.lang.String _arg08 = data.readString();
                    java.lang.String _arg16 = data.readString();
                    data.enforceNoDataAvail();
                    onVolumePathChanged(_arg08, _arg16);
                    return true;
                case 9:
                    java.lang.String _arg09 = data.readString();
                    java.lang.String _arg17 = data.readString();
                    data.enforceNoDataAvail();
                    onVolumeInternalPathChanged(_arg09, _arg17);
                    return true;
                case 10:
                    java.lang.String _arg010 = data.readString();
                    data.enforceNoDataAvail();
                    onVolumeDestroyed(_arg010);
                    return true;
                case 11:
                    java.lang.String _arg011 = data.readString();
                    data.enforceNoDataAvail();
                    onDiskStateChanged(_arg011);
                    return true;
                case 12:
                    java.lang.String _arg012 = data.readString();
                    int _arg18 = data.readInt();
                    java.lang.String _arg25 = data.readString();
                    java.lang.String _arg34 = data.readString();
                    data.enforceNoDataAvail();
                    onVolumeChecked(_arg012, _arg18, _arg25, _arg34);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.os.IVoldListener {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.IVoldListener.Stub.DESCRIPTOR;
            }

            @Override // android.os.IVoldListener
            public void onDiskCreated(java.lang.String diskId, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeInt(flags);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onDiskScanned(java.lang.String diskId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onDiskMetadataChanged(java.lang.String diskId, long sizeBytes, java.lang.String label, java.lang.String sysPath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeLong(sizeBytes);
                    _data.writeString(label);
                    _data.writeString(sysPath);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onDiskDestroyed(java.lang.String diskId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onVolumeCreated(java.lang.String volId, int type, java.lang.String diskId, java.lang.String partGuid, int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeInt(type);
                    _data.writeString(diskId);
                    _data.writeString(partGuid);
                    _data.writeInt(userId);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onVolumeStateChanged(java.lang.String volId, int state, int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeInt(state);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onVolumeMetadataChanged(java.lang.String volId, java.lang.String fsType, java.lang.String fsUuid, java.lang.String fsLabel) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeString(fsType);
                    _data.writeString(fsUuid);
                    _data.writeString(fsLabel);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onVolumePathChanged(java.lang.String volId, java.lang.String path) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeString(path);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onVolumeInternalPathChanged(java.lang.String volId, java.lang.String internalPath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeString(internalPath);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onVolumeDestroyed(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onDiskStateChanged(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IVoldListener
            public void onVolumeChecked(java.lang.String volId, int type, java.lang.String diskId, java.lang.String partGuid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.os.IVoldListener.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeInt(type);
                    _data.writeString(diskId);
                    _data.writeString(partGuid);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
