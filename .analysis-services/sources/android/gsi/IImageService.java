package android.gsi;

/* JADX INFO: loaded from: classes.dex */
public interface IImageService extends android.os.IInterface {
    public static final int CREATE_IMAGE_DEFAULT = 0;
    public static final int CREATE_IMAGE_READONLY = 1;
    public static final int CREATE_IMAGE_ZERO_FILL = 2;
    public static final java.lang.String DESCRIPTOR = "android.gsi.IImageService";
    public static final int IMAGE_ERROR = 1;
    public static final int IMAGE_OK = 0;

    boolean backingImageExists(java.lang.String str) throws android.os.RemoteException;

    void createBackingImage(java.lang.String str, long j, int i, android.gsi.IProgressCallback iProgressCallback) throws android.os.RemoteException;

    void deleteBackingImage(java.lang.String str) throws android.os.RemoteException;

    void disableImage(java.lang.String str) throws android.os.RemoteException;

    java.util.List<java.lang.String> getAllBackingImages() throws android.os.RemoteException;

    int getAvbPublicKey(java.lang.String str, android.gsi.AvbPublicKey avbPublicKey) throws android.os.RemoteException;

    java.lang.String getMappedImageDevice(java.lang.String str) throws android.os.RemoteException;

    boolean isImageDisabled(java.lang.String str) throws android.os.RemoteException;

    boolean isImageMapped(java.lang.String str) throws android.os.RemoteException;

    void mapImageDevice(java.lang.String str, int i, android.gsi.MappedImage mappedImage) throws android.os.RemoteException;

    void removeAllImages() throws android.os.RemoteException;

    void removeDisabledImages() throws android.os.RemoteException;

    void unmapImageDevice(java.lang.String str) throws android.os.RemoteException;

    void zeroFillNewImage(java.lang.String str, long j) throws android.os.RemoteException;

    public static class Default implements android.gsi.IImageService {
        @Override // android.gsi.IImageService
        public void createBackingImage(java.lang.String name, long size, int flags, android.gsi.IProgressCallback on_progress) throws android.os.RemoteException {
        }

        @Override // android.gsi.IImageService
        public void deleteBackingImage(java.lang.String name) throws android.os.RemoteException {
        }

        @Override // android.gsi.IImageService
        public void mapImageDevice(java.lang.String name, int timeout_ms, android.gsi.MappedImage mapping) throws android.os.RemoteException {
        }

        @Override // android.gsi.IImageService
        public void unmapImageDevice(java.lang.String name) throws android.os.RemoteException {
        }

        @Override // android.gsi.IImageService
        public boolean backingImageExists(java.lang.String name) throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IImageService
        public boolean isImageMapped(java.lang.String name) throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IImageService
        public int getAvbPublicKey(java.lang.String name, android.gsi.AvbPublicKey dst) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.gsi.IImageService
        public java.util.List<java.lang.String> getAllBackingImages() throws android.os.RemoteException {
            return null;
        }

        @Override // android.gsi.IImageService
        public void zeroFillNewImage(java.lang.String name, long bytes) throws android.os.RemoteException {
        }

        @Override // android.gsi.IImageService
        public void removeAllImages() throws android.os.RemoteException {
        }

        @Override // android.gsi.IImageService
        public void disableImage(java.lang.String name) throws android.os.RemoteException {
        }

        @Override // android.gsi.IImageService
        public void removeDisabledImages() throws android.os.RemoteException {
        }

        @Override // android.gsi.IImageService
        public boolean isImageDisabled(java.lang.String name) throws android.os.RemoteException {
            return false;
        }

        @Override // android.gsi.IImageService
        public java.lang.String getMappedImageDevice(java.lang.String name) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.gsi.IImageService {
        static final int TRANSACTION_backingImageExists = 5;
        static final int TRANSACTION_createBackingImage = 1;
        static final int TRANSACTION_deleteBackingImage = 2;
        static final int TRANSACTION_disableImage = 11;
        static final int TRANSACTION_getAllBackingImages = 8;
        static final int TRANSACTION_getAvbPublicKey = 7;
        static final int TRANSACTION_getMappedImageDevice = 14;
        static final int TRANSACTION_isImageDisabled = 13;
        static final int TRANSACTION_isImageMapped = 6;
        static final int TRANSACTION_mapImageDevice = 3;
        static final int TRANSACTION_removeAllImages = 10;
        static final int TRANSACTION_removeDisabledImages = 12;
        static final int TRANSACTION_unmapImageDevice = 4;
        static final int TRANSACTION_zeroFillNewImage = 9;

        public Stub() {
            attachInterface(this, android.gsi.IImageService.DESCRIPTOR);
        }

        public static android.gsi.IImageService asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.gsi.IImageService.DESCRIPTOR);
            if (iin != null && (iin instanceof android.gsi.IImageService)) {
                return (android.gsi.IImageService) iin;
            }
            return new android.gsi.IImageService.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(android.gsi.IImageService.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.gsi.IImageService.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    java.lang.String _arg0 = data.readString();
                    long _arg1 = data.readLong();
                    int _arg2 = data.readInt();
                    android.gsi.IProgressCallback _arg3 = android.gsi.IProgressCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    createBackingImage(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 2:
                    java.lang.String _arg02 = data.readString();
                    data.enforceNoDataAvail();
                    deleteBackingImage(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    java.lang.String _arg03 = data.readString();
                    int _arg12 = data.readInt();
                    android.gsi.MappedImage _arg22 = new android.gsi.MappedImage();
                    data.enforceNoDataAvail();
                    mapImageDevice(_arg03, _arg12, _arg22);
                    reply.writeNoException();
                    reply.writeTypedObject(_arg22, 1);
                    return true;
                case 4:
                    java.lang.String _arg04 = data.readString();
                    data.enforceNoDataAvail();
                    unmapImageDevice(_arg04);
                    reply.writeNoException();
                    return true;
                case 5:
                    java.lang.String _arg05 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result = backingImageExists(_arg05);
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    return true;
                case 6:
                    java.lang.String _arg06 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result2 = isImageMapped(_arg06);
                    reply.writeNoException();
                    reply.writeBoolean(_result2);
                    return true;
                case 7:
                    java.lang.String _arg07 = data.readString();
                    android.gsi.AvbPublicKey _arg13 = new android.gsi.AvbPublicKey();
                    data.enforceNoDataAvail();
                    int _result3 = getAvbPublicKey(_arg07, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    reply.writeTypedObject(_arg13, 1);
                    return true;
                case 8:
                    java.util.List<java.lang.String> _result4 = getAllBackingImages();
                    reply.writeNoException();
                    reply.writeStringList(_result4);
                    return true;
                case 9:
                    java.lang.String _arg08 = data.readString();
                    long _arg14 = data.readLong();
                    data.enforceNoDataAvail();
                    zeroFillNewImage(_arg08, _arg14);
                    reply.writeNoException();
                    return true;
                case 10:
                    removeAllImages();
                    reply.writeNoException();
                    return true;
                case 11:
                    java.lang.String _arg09 = data.readString();
                    data.enforceNoDataAvail();
                    disableImage(_arg09);
                    reply.writeNoException();
                    return true;
                case 12:
                    removeDisabledImages();
                    reply.writeNoException();
                    return true;
                case 13:
                    java.lang.String _arg010 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result5 = isImageDisabled(_arg010);
                    reply.writeNoException();
                    reply.writeBoolean(_result5);
                    return true;
                case 14:
                    java.lang.String _arg011 = data.readString();
                    data.enforceNoDataAvail();
                    java.lang.String _result6 = getMappedImageDevice(_arg011);
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.gsi.IImageService {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.gsi.IImageService.DESCRIPTOR;
            }

            @Override // android.gsi.IImageService
            public void createBackingImage(java.lang.String name, long size, int flags, android.gsi.IProgressCallback on_progress) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeLong(size);
                    _data.writeInt(flags);
                    _data.writeStrongInterface(on_progress);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public void deleteBackingImage(java.lang.String name) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public void mapImageDevice(java.lang.String name, int timeout_ms, android.gsi.MappedImage mapping) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(timeout_ms);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        mapping.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public void unmapImageDevice(java.lang.String name) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public boolean backingImageExists(java.lang.String name) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public boolean isImageMapped(java.lang.String name) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public int getAvbPublicKey(java.lang.String name, android.gsi.AvbPublicKey dst) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(7, _data, _reply, 0);
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

            @Override // android.gsi.IImageService
            public java.util.List<java.lang.String> getAllBackingImages() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    java.util.List<java.lang.String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public void zeroFillNewImage(java.lang.String name, long bytes) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeLong(bytes);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public void removeAllImages() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public void disableImage(java.lang.String name) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public void removeDisabledImages() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public boolean isImageDisabled(java.lang.String name) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.gsi.IImageService
            public java.lang.String getMappedImageDevice(java.lang.String name) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.gsi.IImageService.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
