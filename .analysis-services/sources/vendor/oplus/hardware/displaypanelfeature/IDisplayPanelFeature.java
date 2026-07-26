package vendor.oplus.hardware.displaypanelfeature;

/* JADX INFO: loaded from: classes4.dex */
public interface IDisplayPanelFeature extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$oplus$hardware$displaypanelfeature$IDisplayPanelFeature".replace('$', '.');
    public static final java.lang.String HASH = "39afca22ac77253421ad681eeb2bae0c6ff62c13";
    public static final int VERSION = 1;

    int getDisplayPanelFeatureValue(int i, int[] iArr) throws android.os.RemoteException;

    int getDisplayPanelInfo(int i, java.util.List<java.lang.String> list) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    int setDisplayPanelFeatureValue(int i, int[] iArr) throws android.os.RemoteException;

    public static class Default implements vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature {
        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public int getDisplayPanelFeatureValue(int FeatureId, int[] FeatureValues) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public int setDisplayPanelFeatureValue(int FeatureId, int[] FeatureValues) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public int getDisplayPanelInfo(int FeatureId, java.util.List<java.lang.String> PanelInfo) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature {
        static final int TRANSACTION_getDisplayPanelFeatureValue = 1;
        static final int TRANSACTION_getDisplayPanelInfo = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_setDisplayPanelFeatureValue = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature)) {
                return (vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature) iin;
            }
            return new vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getDisplayPanelFeatureValue";
                case 2:
                    return "setDisplayPanelFeatureValue";
                case 3:
                    return "getDisplayPanelInfo";
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
            int[] _arg1;
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
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new int[_arg1_length];
                    }
                    data.enforceNoDataAvail();
                    int _result = getDisplayPanelFeatureValue(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    reply.writeIntArray(_arg1);
                    return true;
                case 2:
                    int _arg02 = data.readInt();
                    int[] _arg12 = data.createIntArray();
                    data.enforceNoDataAvail();
                    int _result2 = setDisplayPanelFeatureValue(_arg02, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    int _arg03 = data.readInt();
                    java.util.List<java.lang.String> _arg13 = new java.util.ArrayList<>();
                    data.enforceNoDataAvail();
                    int _result3 = getDisplayPanelInfo(_arg03, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    reply.writeStringList(_arg13);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature {
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

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
            public int getDisplayPanelFeatureValue(int FeatureId, int[] FeatureValues) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(FeatureId);
                    _data.writeInt(FeatureValues.length);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getDisplayPanelFeatureValue is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readIntArray(FeatureValues);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
            public int setDisplayPanelFeatureValue(int FeatureId, int[] FeatureValues) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(FeatureId);
                    _data.writeIntArray(FeatureValues);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setDisplayPanelFeatureValue is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
            public int getDisplayPanelInfo(int FeatureId, java.util.List<java.lang.String> PanelInfo) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(FeatureId);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getDisplayPanelInfo is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readStringList(PanelInfo);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
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

            @Override // vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
