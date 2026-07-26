package vendor.pixelworks.hardware.display;

/* JADX INFO: loaded from: classes4.dex */
public interface IIrisCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$pixelworks$hardware$display$IIrisCallback".replace('$', '.');
    public static final java.lang.String HASH = "02c8c5526cbde39f502b3bf8cccaf196c81de25f";
    public static final int VERSION = 1;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    int onCalibratePatternChanged(long j, int i) throws android.os.RemoteException;

    vendor.pixelworks.hardware.display.ContentSamples onContentSamplingRequested(long j, int i, long j2) throws android.os.RemoteException;

    void onDisplayPowerChanged(long j, int i) throws android.os.RemoteException;

    void onFeatureChanged(int i, int[] iArr) throws android.os.RemoteException;

    void onRefreshRequested(long j) throws android.os.RemoteException;

    public static class Default implements vendor.pixelworks.hardware.display.IIrisCallback {
        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onFeatureChanged(int type, int[] values) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onRefreshRequested(long display) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public int onCalibratePatternChanged(long display, int pattern) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onDisplayPowerChanged(long display, int mode) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public vendor.pixelworks.hardware.display.ContentSamples onContentSamplingRequested(long display, int action, long maxFrames) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.pixelworks.hardware.display.IIrisCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onCalibratePatternChanged = 3;
        static final int TRANSACTION_onContentSamplingRequested = 5;
        static final int TRANSACTION_onDisplayPowerChanged = 4;
        static final int TRANSACTION_onFeatureChanged = 1;
        static final int TRANSACTION_onRefreshRequested = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.pixelworks.hardware.display.IIrisCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.pixelworks.hardware.display.IIrisCallback)) {
                return (vendor.pixelworks.hardware.display.IIrisCallback) iin;
            }
            return new vendor.pixelworks.hardware.display.IIrisCallback.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onFeatureChanged";
                case 2:
                    return "onRefreshRequested";
                case 3:
                    return "onCalibratePatternChanged";
                case 4:
                    return "onDisplayPowerChanged";
                case 5:
                    return "onContentSamplingRequested";
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    return "getInterfaceHash";
                case 16777215:
                    return "getInterfaceVersion";
                default:
                    return null;
            }
        }

        public java.lang.String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
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
                    int[] _arg1 = data.createIntArray();
                    data.enforceNoDataAvail();
                    onFeatureChanged(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    long _arg02 = data.readLong();
                    data.enforceNoDataAvail();
                    onRefreshRequested(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    long _arg03 = data.readLong();
                    int _arg12 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result = onCalibratePatternChanged(_arg03, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 4:
                    long _arg04 = data.readLong();
                    int _arg13 = data.readInt();
                    data.enforceNoDataAvail();
                    onDisplayPowerChanged(_arg04, _arg13);
                    reply.writeNoException();
                    return true;
                case 5:
                    long _arg05 = data.readLong();
                    int _arg14 = data.readInt();
                    long _arg2 = data.readLong();
                    data.enforceNoDataAvail();
                    vendor.pixelworks.hardware.display.ContentSamples _result2 = onContentSamplingRequested(_arg05, _arg14, _arg2);
                    reply.writeNoException();
                    reply.writeTypedObject(_result2, 1);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements vendor.pixelworks.hardware.display.IIrisCallback {
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

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public void onFeatureChanged(int type, int[] values) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(values);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onFeatureChanged is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public void onRefreshRequested(long display) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onRefreshRequested is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public int onCalibratePatternChanged(long display, int pattern) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(pattern);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onCalibratePatternChanged is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public void onDisplayPowerChanged(long display, int mode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onDisplayPowerChanged is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public vendor.pixelworks.hardware.display.ContentSamples onContentSamplingRequested(long display, int action, long maxFrames) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(action);
                    _data.writeLong(maxFrames);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onContentSamplingRequested is unimplemented.");
                    }
                    _reply.readException();
                    vendor.pixelworks.hardware.display.ContentSamples _result = (vendor.pixelworks.hardware.display.ContentSamples) _reply.readTypedObject(vendor.pixelworks.hardware.display.ContentSamples.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
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

            @Override // vendor.pixelworks.hardware.display.IIrisCallback
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.pixelworks.hardware.display.IIrisCallback.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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

        public int getMaxTransactionId() {
            return TRANSACTION_getInterfaceHash;
        }
    }
}
