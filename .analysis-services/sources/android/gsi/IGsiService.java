package android.gsi;

/* JADX INFO: loaded from: classes.dex */
public interface IGsiService extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android.gsi.IGsiService";
    public static final int INSTALL_ERROR_FILE_SYSTEM_CLUTTERED = 3;
    public static final int INSTALL_ERROR_GENERIC = 1;
    public static final int INSTALL_ERROR_NO_SPACE = 2;
    public static final int INSTALL_OK = 0;
    public static final int STATUS_COMPLETE = 2;
    public static final int STATUS_NO_OPERATION = 0;
    public static final int STATUS_WORKING = 1;

    boolean cancelGsiInstall() throws android.os.RemoteException;

    int closeInstall() throws android.os.RemoteException;

    int closePartition() throws android.os.RemoteException;

    boolean commitGsiChunkFromAshmem(long j) throws android.os.RemoteException;

    boolean commitGsiChunkFromStream(android.os.ParcelFileDescriptor parcelFileDescriptor, long j) throws android.os.RemoteException;

    int createPartition(java.lang.String str, long j, boolean z) throws android.os.RemoteException;

    boolean disableGsi() throws android.os.RemoteException;

    java.lang.String dumpDeviceMapperDevices() throws android.os.RemoteException;

    int enableGsi(boolean z, java.lang.String str) throws android.os.RemoteException;

    void enableGsiAsync(boolean z, java.lang.String str, android.gsi.IGsiServiceCallback iGsiServiceCallback) throws android.os.RemoteException;

    java.lang.String getActiveDsuSlot() throws android.os.RemoteException;

    int getAvbPublicKey(android.gsi.AvbPublicKey avbPublicKey) throws android.os.RemoteException;

    android.gsi.GsiProgress getInstallProgress() throws android.os.RemoteException;

    java.util.List<java.lang.String> getInstalledDsuSlots() throws android.os.RemoteException;

    java.lang.String getInstalledGsiImageDir() throws android.os.RemoteException;

    boolean isGsiEnabled() throws android.os.RemoteException;

    boolean isGsiInstallInProgress() throws android.os.RemoteException;

    boolean isGsiInstalled() throws android.os.RemoteException;

    boolean isGsiRunning() throws android.os.RemoteException;

    android.gsi.IImageService openImageService(java.lang.String str) throws android.os.RemoteException;

    int openInstall(java.lang.String str) throws android.os.RemoteException;

    boolean removeGsi() throws android.os.RemoteException;

    void removeGsiAsync(android.gsi.IGsiServiceCallback iGsiServiceCallback) throws android.os.RemoteException;

    boolean setGsiAshmem(android.os.ParcelFileDescriptor parcelFileDescriptor, long j) throws android.os.RemoteException;

    long suggestScratchSize() throws android.os.RemoteException;

    int zeroPartition(java.lang.String str) throws android.os.RemoteException;

    public static class Default implements android.gsi.IGsiService {
        @Override // android.gsi.IGsiService
        public boolean commitGsiChunkFromStream(android.os.ParcelFileDescriptor stream, long bytes) throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public android.gsi.GsiProgress getInstallProgress() throws android.os.RemoteException {
            return null;
        }

        @Override // android.gsi.IGsiService
        public boolean setGsiAshmem(android.os.ParcelFileDescriptor stream, long size) throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public boolean commitGsiChunkFromAshmem(long bytes) throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public int enableGsi(boolean oneShot, java.lang.String dsuSlot) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.gsi.IGsiService
        public void enableGsiAsync(boolean oneShot, java.lang.String dsuSlot, android.gsi.IGsiServiceCallback result) throws android.os.RemoteException {
        }

        @Override // android.gsi.IGsiService
        public boolean isGsiEnabled() throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public boolean cancelGsiInstall() throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public boolean isGsiInstallInProgress() throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public boolean removeGsi() throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public void removeGsiAsync(android.gsi.IGsiServiceCallback result) throws android.os.RemoteException {
        }

        @Override // android.gsi.IGsiService
        public boolean disableGsi() throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public boolean isGsiInstalled() throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public boolean isGsiRunning() throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IGsiService
        public java.lang.String getActiveDsuSlot() throws android.os.RemoteException {
            return null;
        }

        @Override // android.gsi.IGsiService
        public java.lang.String getInstalledGsiImageDir() throws android.os.RemoteException {
            return null;
        }

        @Override // android.gsi.IGsiService
        public java.util.List<java.lang.String> getInstalledDsuSlots() throws android.os.RemoteException {
            return null;
        }

        @Override // android.gsi.IGsiService
        public int openInstall(java.lang.String installDir) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.gsi.IGsiService
        public int closeInstall() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.gsi.IGsiService
        public int createPartition(java.lang.String name, long size, boolean readOnly) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.gsi.IGsiService
        public int closePartition() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.gsi.IGsiService
        public int zeroPartition(java.lang.String name) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.gsi.IGsiService
        public android.gsi.IImageService openImageService(java.lang.String prefix) throws android.os.RemoteException {
            return null;
        }

        @Override // android.gsi.IGsiService
        public java.lang.String dumpDeviceMapperDevices() throws android.os.RemoteException {
            return null;
        }

        @Override // android.gsi.IGsiService
        public int getAvbPublicKey(android.gsi.AvbPublicKey dst) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.gsi.IGsiService
        public long suggestScratchSize() throws android.os.RemoteException {
            return 0L;
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.gsi.IGsiService {
        static final int TRANSACTION_cancelGsiInstall = 8;
        static final int TRANSACTION_closeInstall = 19;
        static final int TRANSACTION_closePartition = 21;
        static final int TRANSACTION_commitGsiChunkFromAshmem = 4;
        static final int TRANSACTION_commitGsiChunkFromStream = 1;
        static final int TRANSACTION_createPartition = 20;
        static final int TRANSACTION_disableGsi = 12;
        static final int TRANSACTION_dumpDeviceMapperDevices = 24;
        static final int TRANSACTION_enableGsi = 5;
        static final int TRANSACTION_enableGsiAsync = 6;
        static final int TRANSACTION_getActiveDsuSlot = 15;
        static final int TRANSACTION_getAvbPublicKey = 25;
        static final int TRANSACTION_getInstallProgress = 2;
        static final int TRANSACTION_getInstalledDsuSlots = 17;
        static final int TRANSACTION_getInstalledGsiImageDir = 16;
        static final int TRANSACTION_isGsiEnabled = 7;
        static final int TRANSACTION_isGsiInstallInProgress = 9;
        static final int TRANSACTION_isGsiInstalled = 13;
        static final int TRANSACTION_isGsiRunning = 14;
        static final int TRANSACTION_openImageService = 23;
        static final int TRANSACTION_openInstall = 18;
        static final int TRANSACTION_removeGsi = 10;
        static final int TRANSACTION_removeGsiAsync = 11;
        static final int TRANSACTION_setGsiAshmem = 3;
        static final int TRANSACTION_suggestScratchSize = 26;
        static final int TRANSACTION_zeroPartition = 22;

        public Stub() {
            attachInterface(this, android.gsi.IGsiService.DESCRIPTOR);
        }

        public static android.gsi.IGsiService asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.gsi.IGsiService.DESCRIPTOR);
            if (iin != null && (iin instanceof android.gsi.IGsiService)) {
                return (android.gsi.IGsiService) iin;
            }
            return new android.gsi.IGsiService.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(android.gsi.IGsiService.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.gsi.IGsiService.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    android.os.ParcelFileDescriptor _arg0 = (android.os.ParcelFileDescriptor) data.readTypedObject(android.os.ParcelFileDescriptor.CREATOR);
                    long _arg1 = data.readLong();
                    data.enforceNoDataAvail();
                    boolean _result = commitGsiChunkFromStream(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    return true;
                case 2:
                    android.gsi.GsiProgress _result2 = getInstallProgress();
                    reply.writeNoException();
                    reply.writeTypedObject(_result2, 1);
                    return true;
                case 3:
                    android.os.ParcelFileDescriptor _arg02 = (android.os.ParcelFileDescriptor) data.readTypedObject(android.os.ParcelFileDescriptor.CREATOR);
                    long _arg12 = data.readLong();
                    data.enforceNoDataAvail();
                    boolean _result3 = setGsiAshmem(_arg02, _arg12);
                    reply.writeNoException();
                    reply.writeBoolean(_result3);
                    return true;
                case 4:
                    long _arg03 = data.readLong();
                    data.enforceNoDataAvail();
                    boolean _result4 = commitGsiChunkFromAshmem(_arg03);
                    reply.writeNoException();
                    reply.writeBoolean(_result4);
                    return true;
                case 5:
                    boolean _arg04 = data.readBoolean();
                    java.lang.String _arg13 = data.readString();
                    data.enforceNoDataAvail();
                    int _result5 = enableGsi(_arg04, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 6:
                    boolean _arg05 = data.readBoolean();
                    java.lang.String _arg14 = data.readString();
                    android.gsi.IGsiServiceCallback _arg2 = android.gsi.IGsiServiceCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    enableGsiAsync(_arg05, _arg14, _arg2);
                    return true;
                case 7:
                    boolean _result6 = isGsiEnabled();
                    reply.writeNoException();
                    reply.writeBoolean(_result6);
                    return true;
                case 8:
                    boolean _result7 = cancelGsiInstall();
                    reply.writeNoException();
                    reply.writeBoolean(_result7);
                    return true;
                case 9:
                    boolean _result8 = isGsiInstallInProgress();
                    reply.writeNoException();
                    reply.writeBoolean(_result8);
                    return true;
                case 10:
                    boolean _result9 = removeGsi();
                    reply.writeNoException();
                    reply.writeBoolean(_result9);
                    return true;
                case 11:
                    android.gsi.IGsiServiceCallback _arg06 = android.gsi.IGsiServiceCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    removeGsiAsync(_arg06);
                    return true;
                case 12:
                    boolean _result10 = disableGsi();
                    reply.writeNoException();
                    reply.writeBoolean(_result10);
                    return true;
                case 13:
                    boolean _result11 = isGsiInstalled();
                    reply.writeNoException();
                    reply.writeBoolean(_result11);
                    return true;
                case 14:
                    boolean _result12 = isGsiRunning();
                    reply.writeNoException();
                    reply.writeBoolean(_result12);
                    return true;
                case 15:
                    java.lang.String _result13 = getActiveDsuSlot();
                    reply.writeNoException();
                    reply.writeString(_result13);
                    return true;
                case 16:
                    java.lang.String _result14 = getInstalledGsiImageDir();
                    reply.writeNoException();
                    reply.writeString(_result14);
                    return true;
                case 17:
                    java.util.List<java.lang.String> _result15 = getInstalledDsuSlots();
                    reply.writeNoException();
                    reply.writeStringList(_result15);
                    return true;
                case 18:
                    java.lang.String _arg07 = data.readString();
                    data.enforceNoDataAvail();
                    int _result16 = openInstall(_arg07);
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 19:
                    int _result17 = closeInstall();
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 20:
                    java.lang.String _arg08 = data.readString();
                    long _arg15 = data.readLong();
                    boolean _arg22 = data.readBoolean();
                    data.enforceNoDataAvail();
                    int _result18 = createPartition(_arg08, _arg15, _arg22);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 21:
                    int _result19 = closePartition();
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 22:
                    java.lang.String _arg09 = data.readString();
                    data.enforceNoDataAvail();
                    int _result20 = zeroPartition(_arg09);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 23:
                    java.lang.String _arg010 = data.readString();
                    data.enforceNoDataAvail();
                    android.gsi.IImageService _result21 = openImageService(_arg010);
                    reply.writeNoException();
                    reply.writeStrongInterface(_result21);
                    return true;
                case 24:
                    java.lang.String _result22 = dumpDeviceMapperDevices();
                    reply.writeNoException();
                    reply.writeString(_result22);
                    return true;
                case 25:
                    android.gsi.AvbPublicKey _arg011 = new android.gsi.AvbPublicKey();
                    data.enforceNoDataAvail();
                    int _result23 = getAvbPublicKey(_arg011);
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    reply.writeTypedObject(_arg011, 1);
                    return true;
                case 26:
                    long _result24 = suggestScratchSize();
                    reply.writeNoException();
                    reply.writeLong(_result24);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.gsi.IGsiService {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.gsi.IGsiService.DESCRIPTOR;
            }

            @Override // android.gsi.IGsiService
            public boolean commitGsiChunkFromStream(android.os.ParcelFileDescriptor stream, long bytes) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeTypedObject(stream, 0);
                    _data.writeLong(bytes);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public android.gsi.GsiProgress getInstallProgress() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    android.gsi.GsiProgress _result = (android.gsi.GsiProgress) _reply.readTypedObject(android.gsi.GsiProgress.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public boolean setGsiAshmem(android.os.ParcelFileDescriptor stream, long size) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeTypedObject(stream, 0);
                    _data.writeLong(size);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public boolean commitGsiChunkFromAshmem(long bytes) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeLong(bytes);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public int enableGsi(boolean oneShot, java.lang.String dsuSlot) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeBoolean(oneShot);
                    _data.writeString(dsuSlot);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public void enableGsiAsync(boolean oneShot, java.lang.String dsuSlot, android.gsi.IGsiServiceCallback result) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeBoolean(oneShot);
                    _data.writeString(dsuSlot);
                    _data.writeStrongInterface(result);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public boolean isGsiEnabled() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public boolean cancelGsiInstall() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public boolean isGsiInstallInProgress() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public boolean removeGsi() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public void removeGsiAsync(android.gsi.IGsiServiceCallback result) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeStrongInterface(result);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public boolean disableGsi() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public boolean isGsiInstalled() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public boolean isGsiRunning() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public java.lang.String getActiveDsuSlot() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public java.lang.String getInstalledGsiImageDir() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public java.util.List<java.lang.String> getInstalledDsuSlots() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    java.util.List<java.lang.String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public int openInstall(java.lang.String installDir) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeString(installDir);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public int closeInstall() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public int createPartition(java.lang.String name, long size, boolean readOnly) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeLong(size);
                    _data.writeBoolean(readOnly);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public int closePartition() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public int zeroPartition(java.lang.String name) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public android.gsi.IImageService openImageService(java.lang.String prefix) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    _data.writeString(prefix);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    android.gsi.IImageService _result = android.gsi.IImageService.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public java.lang.String dumpDeviceMapperDevices() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public int getAvbPublicKey(android.gsi.AvbPublicKey dst) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        dst.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IGsiService
            public long suggestScratchSize() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IGsiService.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
