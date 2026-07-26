package vendor.oplus.hardware.commondcs;

/* JADX INFO: loaded from: classes4.dex */
public interface ICommonDcsAidlHalService extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$oplus$hardware$commondcs$ICommonDcsAidlHalService".replace('$', '.');
    public static final java.lang.String HASH = "f44e47daf162ccd62e12b02208b18999e3197d96";
    public static final int VERSION = 1;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    int notifyMsgToCommonDcs(java.util.List<vendor.oplus.hardware.commondcs.StringPair> list, java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    public static class Default implements vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService {
        @Override // vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService
        public int notifyMsgToCommonDcs(java.util.List<vendor.oplus.hardware.commondcs.StringPair> data, java.lang.String logTag, java.lang.String eventId) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_notifyMsgToCommonDcs = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService)) {
                return (vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService) iin;
            }
            return new vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "notifyMsgToCommonDcs";
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
                    java.util.List<vendor.oplus.hardware.commondcs.StringPair> _arg0 = data.createTypedArrayList(vendor.oplus.hardware.commondcs.StringPair.CREATOR);
                    java.lang.String _arg1 = data.readString();
                    java.lang.String _arg2 = data.readString();
                    data.enforceNoDataAvail();
                    int _result = notifyMsgToCommonDcs(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    break;
            }
            return true;
        }

        private static class Proxy implements vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService {
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

            @Override // vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService
            public int notifyMsgToCommonDcs(java.util.List<vendor.oplus.hardware.commondcs.StringPair> data, java.lang.String logTag, java.lang.String eventId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedList(data, 0);
                    _data.writeString(logTag);
                    _data.writeString(eventId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyMsgToCommonDcs is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService
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

            @Override // vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.oplus.hardware.commondcs.ICommonDcsAidlHalService.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
