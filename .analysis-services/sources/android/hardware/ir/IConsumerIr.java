package android.hardware.ir;

/* JADX INFO: loaded from: classes.dex */
public interface IConsumerIr extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$ir$IConsumerIr".replace('$', '.');
    public static final java.lang.String HASH = "3e04aed366e96850c6164287eaf78a8e4ab071b0";
    public static final int VERSION = 1;

    android.hardware.ir.ConsumerIrFreqRange[] getCarrierFreqs() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void transmit(int i, int[] iArr) throws android.os.RemoteException;

    public static class Default implements android.hardware.ir.IConsumerIr {
        @Override // android.hardware.ir.IConsumerIr
        public android.hardware.ir.ConsumerIrFreqRange[] getCarrierFreqs() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.ir.IConsumerIr
        public void transmit(int carrierFreqHz, int[] pattern) throws android.os.RemoteException {
        }

        @Override // android.hardware.ir.IConsumerIr
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.ir.IConsumerIr
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.ir.IConsumerIr {
        static final int TRANSACTION_getCarrierFreqs = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_transmit = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.ir.IConsumerIr asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.ir.IConsumerIr)) {
                return (android.hardware.ir.IConsumerIr) iin;
            }
            return new android.hardware.ir.IConsumerIr.Stub.Proxy(obj);
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
                    android.hardware.ir.ConsumerIrFreqRange[] _result = getCarrierFreqs();
                    reply.writeNoException();
                    reply.writeTypedArray(_result, 1);
                    return true;
                case 2:
                    int _arg0 = data.readInt();
                    int[] _arg1 = data.createIntArray();
                    data.enforceNoDataAvail();
                    transmit(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.ir.IConsumerIr {
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

            @Override // android.hardware.ir.IConsumerIr
            public android.hardware.ir.ConsumerIrFreqRange[] getCarrierFreqs() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getCarrierFreqs is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.ir.ConsumerIrFreqRange[] _result = (android.hardware.ir.ConsumerIrFreqRange[]) _reply.createTypedArray(android.hardware.ir.ConsumerIrFreqRange.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ir.IConsumerIr
            public void transmit(int carrierFreqHz, int[] pattern) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(carrierFreqHz);
                    _data.writeIntArray(pattern);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method transmit is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ir.IConsumerIr
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

            @Override // android.hardware.ir.IConsumerIr
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.ir.IConsumerIr.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
