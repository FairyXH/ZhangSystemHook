package android.hardware.power.stats;

/* JADX INFO: loaded from: classes.dex */
public interface IPowerStats extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$power$stats$IPowerStats".replace('$', '.');
    public static final java.lang.String HASH = "c3e113101b731c666717eb579492efa287a8f529";
    public static final int VERSION = 2;

    android.hardware.power.stats.EnergyConsumerResult[] getEnergyConsumed(int[] iArr) throws android.os.RemoteException;

    android.hardware.power.stats.EnergyConsumer[] getEnergyConsumerInfo() throws android.os.RemoteException;

    android.hardware.power.stats.Channel[] getEnergyMeterInfo() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    android.hardware.power.stats.PowerEntity[] getPowerEntityInfo() throws android.os.RemoteException;

    android.hardware.power.stats.StateResidencyResult[] getStateResidency(int[] iArr) throws android.os.RemoteException;

    android.hardware.power.stats.EnergyMeasurement[] readEnergyMeter(int[] iArr) throws android.os.RemoteException;

    public static class Default implements android.hardware.power.stats.IPowerStats {
        @Override // android.hardware.power.stats.IPowerStats
        public android.hardware.power.stats.PowerEntity[] getPowerEntityInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public android.hardware.power.stats.StateResidencyResult[] getStateResidency(int[] powerEntityIds) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public android.hardware.power.stats.EnergyConsumer[] getEnergyConsumerInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public android.hardware.power.stats.EnergyConsumerResult[] getEnergyConsumed(int[] energyConsumerIds) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public android.hardware.power.stats.Channel[] getEnergyMeterInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public android.hardware.power.stats.EnergyMeasurement[] readEnergyMeter(int[] channelIds) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.power.stats.IPowerStats
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.power.stats.IPowerStats {
        static final int TRANSACTION_getEnergyConsumed = 4;
        static final int TRANSACTION_getEnergyConsumerInfo = 3;
        static final int TRANSACTION_getEnergyMeterInfo = 5;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPowerEntityInfo = 1;
        static final int TRANSACTION_getStateResidency = 2;
        static final int TRANSACTION_readEnergyMeter = 6;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.power.stats.IPowerStats asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.power.stats.IPowerStats)) {
                return (android.hardware.power.stats.IPowerStats) iin;
            }
            return new android.hardware.power.stats.IPowerStats.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getPowerEntityInfo";
                case 2:
                    return "getStateResidency";
                case 3:
                    return "getEnergyConsumerInfo";
                case 4:
                    return "getEnergyConsumed";
                case 5:
                    return "getEnergyMeterInfo";
                case 6:
                    return "readEnergyMeter";
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
                    android.hardware.power.stats.PowerEntity[] _result = getPowerEntityInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result, 1);
                    return true;
                case 2:
                    int[] _arg0 = data.createIntArray();
                    data.enforceNoDataAvail();
                    android.hardware.power.stats.StateResidencyResult[] _result2 = getStateResidency(_arg0);
                    reply.writeNoException();
                    reply.writeTypedArray(_result2, 1);
                    return true;
                case 3:
                    android.hardware.power.stats.EnergyConsumer[] _result3 = getEnergyConsumerInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result3, 1);
                    return true;
                case 4:
                    int[] _arg02 = data.createIntArray();
                    data.enforceNoDataAvail();
                    android.hardware.power.stats.EnergyConsumerResult[] _result4 = getEnergyConsumed(_arg02);
                    reply.writeNoException();
                    reply.writeTypedArray(_result4, 1);
                    return true;
                case 5:
                    android.hardware.power.stats.Channel[] _result5 = getEnergyMeterInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result5, 1);
                    return true;
                case 6:
                    int[] _arg03 = data.createIntArray();
                    data.enforceNoDataAvail();
                    android.hardware.power.stats.EnergyMeasurement[] _result6 = readEnergyMeter(_arg03);
                    reply.writeNoException();
                    reply.writeTypedArray(_result6, 1);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.power.stats.IPowerStats {
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

            @Override // android.hardware.power.stats.IPowerStats
            public android.hardware.power.stats.PowerEntity[] getPowerEntityInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPowerEntityInfo is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.power.stats.PowerEntity[] _result = (android.hardware.power.stats.PowerEntity[]) _reply.createTypedArray(android.hardware.power.stats.PowerEntity.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public android.hardware.power.stats.StateResidencyResult[] getStateResidency(int[] powerEntityIds) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeIntArray(powerEntityIds);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getStateResidency is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.power.stats.StateResidencyResult[] _result = (android.hardware.power.stats.StateResidencyResult[]) _reply.createTypedArray(android.hardware.power.stats.StateResidencyResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public android.hardware.power.stats.EnergyConsumer[] getEnergyConsumerInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getEnergyConsumerInfo is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.power.stats.EnergyConsumer[] _result = (android.hardware.power.stats.EnergyConsumer[]) _reply.createTypedArray(android.hardware.power.stats.EnergyConsumer.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public android.hardware.power.stats.EnergyConsumerResult[] getEnergyConsumed(int[] energyConsumerIds) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeIntArray(energyConsumerIds);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getEnergyConsumed is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.power.stats.EnergyConsumerResult[] _result = (android.hardware.power.stats.EnergyConsumerResult[]) _reply.createTypedArray(android.hardware.power.stats.EnergyConsumerResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public android.hardware.power.stats.Channel[] getEnergyMeterInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getEnergyMeterInfo is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.power.stats.Channel[] _result = (android.hardware.power.stats.Channel[]) _reply.createTypedArray(android.hardware.power.stats.Channel.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
            public android.hardware.power.stats.EnergyMeasurement[] readEnergyMeter(int[] channelIds) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeIntArray(channelIds);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method readEnergyMeter is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.power.stats.EnergyMeasurement[] _result = (android.hardware.power.stats.EnergyMeasurement[]) _reply.createTypedArray(android.hardware.power.stats.EnergyMeasurement.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.power.stats.IPowerStats
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

            @Override // android.hardware.power.stats.IPowerStats
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.power.stats.IPowerStats.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
