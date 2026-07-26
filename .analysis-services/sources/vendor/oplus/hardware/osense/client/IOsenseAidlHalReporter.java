package vendor.oplus.hardware.osense.client;

/* JADX INFO: loaded from: classes4.dex */
public interface IOsenseAidlHalReporter extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$oplus$hardware$osense$client$IOsenseAidlHalReporter".replace('$', '.');
    public static final java.lang.String HASH = "ec186a0b12479cb8b761545e0f28fb272053f7bc";
    public static final int VERSION = 1;

    int checkAccessPermission(java.lang.String str) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void osenseClrSceneAction(java.lang.String str, long j) throws android.os.RemoteException;

    void osenseResetCtrlData(java.lang.String str) throws android.os.RemoteException;

    void osenseSetCtrlData(java.lang.String str, vendor.oplus.hardware.osense.client.OsenseControlInfo osenseControlInfo) throws android.os.RemoteException;

    void osenseSetNotification(java.lang.String str, vendor.oplus.hardware.osense.client.OsenseAidlHalNotifyRequest osenseAidlHalNotifyRequest) throws android.os.RemoteException;

    void osenseSetSceneAction(java.lang.String str, vendor.oplus.hardware.osense.client.OsenseAidlHalSaRequest osenseAidlHalSaRequest) throws android.os.RemoteException;

    public static class Default implements vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter {
        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public int checkAccessPermission(java.lang.String identity) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseClrSceneAction(java.lang.String identity, long request) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseSetNotification(java.lang.String identity, vendor.oplus.hardware.osense.client.OsenseAidlHalNotifyRequest osenseHalNotifyRequest) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseSetSceneAction(java.lang.String identity, vendor.oplus.hardware.osense.client.OsenseAidlHalSaRequest osenseHalSaRequest) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseResetCtrlData(java.lang.String identity) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public void osenseSetCtrlData(java.lang.String identity, vendor.oplus.hardware.osense.client.OsenseControlInfo ctrldata) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter {
        static final int TRANSACTION_checkAccessPermission = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_osenseClrSceneAction = 2;
        static final int TRANSACTION_osenseResetCtrlData = 5;
        static final int TRANSACTION_osenseSetCtrlData = 6;
        static final int TRANSACTION_osenseSetNotification = 3;
        static final int TRANSACTION_osenseSetSceneAction = 4;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter)) {
                return (vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter) iin;
            }
            return new vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "checkAccessPermission";
                case 2:
                    return "osenseClrSceneAction";
                case 3:
                    return "osenseSetNotification";
                case 4:
                    return "osenseSetSceneAction";
                case 5:
                    return "osenseResetCtrlData";
                case 6:
                    return "osenseSetCtrlData";
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
                    java.lang.String _arg0 = data.readString();
                    data.enforceNoDataAvail();
                    int _result = checkAccessPermission(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    java.lang.String _arg02 = data.readString();
                    long _arg1 = data.readLong();
                    data.enforceNoDataAvail();
                    osenseClrSceneAction(_arg02, _arg1);
                    return true;
                case 3:
                    java.lang.String _arg03 = data.readString();
                    vendor.oplus.hardware.osense.client.OsenseAidlHalNotifyRequest _arg12 = (vendor.oplus.hardware.osense.client.OsenseAidlHalNotifyRequest) data.readTypedObject(vendor.oplus.hardware.osense.client.OsenseAidlHalNotifyRequest.CREATOR);
                    data.enforceNoDataAvail();
                    osenseSetNotification(_arg03, _arg12);
                    return true;
                case 4:
                    java.lang.String _arg04 = data.readString();
                    vendor.oplus.hardware.osense.client.OsenseAidlHalSaRequest _arg13 = (vendor.oplus.hardware.osense.client.OsenseAidlHalSaRequest) data.readTypedObject(vendor.oplus.hardware.osense.client.OsenseAidlHalSaRequest.CREATOR);
                    data.enforceNoDataAvail();
                    osenseSetSceneAction(_arg04, _arg13);
                    return true;
                case 5:
                    java.lang.String _arg05 = data.readString();
                    data.enforceNoDataAvail();
                    osenseResetCtrlData(_arg05);
                    return true;
                case 6:
                    java.lang.String _arg06 = data.readString();
                    vendor.oplus.hardware.osense.client.OsenseControlInfo _arg14 = (vendor.oplus.hardware.osense.client.OsenseControlInfo) data.readTypedObject(vendor.oplus.hardware.osense.client.OsenseControlInfo.CREATOR);
                    data.enforceNoDataAvail();
                    osenseSetCtrlData(_arg06, _arg14);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter {
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

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public int checkAccessPermission(java.lang.String identity) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(identity);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method checkAccessPermission is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseClrSceneAction(java.lang.String identity, long request) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(identity);
                    _data.writeLong(request);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method osenseClrSceneAction is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseSetNotification(java.lang.String identity, vendor.oplus.hardware.osense.client.OsenseAidlHalNotifyRequest osenseHalNotifyRequest) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(identity);
                    _data.writeTypedObject(osenseHalNotifyRequest, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method osenseSetNotification is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseSetSceneAction(java.lang.String identity, vendor.oplus.hardware.osense.client.OsenseAidlHalSaRequest osenseHalSaRequest) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(identity);
                    _data.writeTypedObject(osenseHalSaRequest, 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method osenseSetSceneAction is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseResetCtrlData(java.lang.String identity) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(identity);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method osenseResetCtrlData is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public void osenseSetCtrlData(java.lang.String identity, vendor.oplus.hardware.osense.client.OsenseControlInfo ctrldata) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(identity);
                    _data.writeTypedObject(ctrldata, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method osenseSetCtrlData is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
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

            @Override // vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.oplus.hardware.osense.client.IOsenseAidlHalReporter.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
