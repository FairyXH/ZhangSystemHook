package android.hardware.health;

/* JADX INFO: loaded from: classes.dex */
public interface IHealth extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$health$IHealth".replace('$', '.');
    public static final java.lang.String HASH = "3bab6273a5491102b29c9d7a1f0efa749533f46d";
    public static final int STATUS_CALLBACK_DIED = 4;
    public static final int STATUS_UNKNOWN = 2;
    public static final int VERSION = 3;

    android.hardware.health.BatteryHealthData getBatteryHealthData() throws android.os.RemoteException;

    int getCapacity() throws android.os.RemoteException;

    int getChargeCounterUah() throws android.os.RemoteException;

    int getChargeStatus() throws android.os.RemoteException;

    int getChargingPolicy() throws android.os.RemoteException;

    int getCurrentAverageMicroamps() throws android.os.RemoteException;

    int getCurrentNowMicroamps() throws android.os.RemoteException;

    android.hardware.health.DiskStats[] getDiskStats() throws android.os.RemoteException;

    long getEnergyCounterNwh() throws android.os.RemoteException;

    android.hardware.health.HealthInfo getHealthInfo() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    android.hardware.health.StorageInfo[] getStorageInfo() throws android.os.RemoteException;

    void registerCallback(android.hardware.health.IHealthInfoCallback iHealthInfoCallback) throws android.os.RemoteException;

    void setChargingPolicy(int i) throws android.os.RemoteException;

    void unregisterCallback(android.hardware.health.IHealthInfoCallback iHealthInfoCallback) throws android.os.RemoteException;

    void update() throws android.os.RemoteException;

    public static class Default implements android.hardware.health.IHealth {
        @Override // android.hardware.health.IHealth
        public void registerCallback(android.hardware.health.IHealthInfoCallback callback) throws android.os.RemoteException {
        }

        @Override // android.hardware.health.IHealth
        public void unregisterCallback(android.hardware.health.IHealthInfoCallback callback) throws android.os.RemoteException {
        }

        @Override // android.hardware.health.IHealth
        public void update() throws android.os.RemoteException {
        }

        @Override // android.hardware.health.IHealth
        public int getChargeCounterUah() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public int getCurrentNowMicroamps() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public int getCurrentAverageMicroamps() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public int getCapacity() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public long getEnergyCounterNwh() throws android.os.RemoteException {
            return 0L;
        }

        @Override // android.hardware.health.IHealth
        public int getChargeStatus() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public android.hardware.health.StorageInfo[] getStorageInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.health.IHealth
        public android.hardware.health.DiskStats[] getDiskStats() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.health.IHealth
        public android.hardware.health.HealthInfo getHealthInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.health.IHealth
        public void setChargingPolicy(int in_value) throws android.os.RemoteException {
        }

        @Override // android.hardware.health.IHealth
        public int getChargingPolicy() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public android.hardware.health.BatteryHealthData getBatteryHealthData() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.health.IHealth
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.health.IHealth
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.health.IHealth {
        static final int TRANSACTION_getBatteryHealthData = 15;
        static final int TRANSACTION_getCapacity = 7;
        static final int TRANSACTION_getChargeCounterUah = 4;
        static final int TRANSACTION_getChargeStatus = 9;
        static final int TRANSACTION_getChargingPolicy = 14;
        static final int TRANSACTION_getCurrentAverageMicroamps = 6;
        static final int TRANSACTION_getCurrentNowMicroamps = 5;
        static final int TRANSACTION_getDiskStats = 11;
        static final int TRANSACTION_getEnergyCounterNwh = 8;
        static final int TRANSACTION_getHealthInfo = 12;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getStorageInfo = 10;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_setChargingPolicy = 13;
        static final int TRANSACTION_unregisterCallback = 2;
        static final int TRANSACTION_update = 3;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.health.IHealth asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.health.IHealth)) {
                return (android.hardware.health.IHealth) iin;
            }
            return new android.hardware.health.IHealth.Stub.Proxy(obj);
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
                    android.hardware.health.IHealthInfoCallback _arg0 = android.hardware.health.IHealthInfoCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    registerCallback(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    android.hardware.health.IHealthInfoCallback _arg02 = android.hardware.health.IHealthInfoCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    unregisterCallback(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    update();
                    reply.writeNoException();
                    return true;
                case 4:
                    int _result = getChargeCounterUah();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 5:
                    int _result2 = getCurrentNowMicroamps();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 6:
                    int _result3 = getCurrentAverageMicroamps();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 7:
                    int _result4 = getCapacity();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 8:
                    long _result5 = getEnergyCounterNwh();
                    reply.writeNoException();
                    reply.writeLong(_result5);
                    return true;
                case 9:
                    int _result6 = getChargeStatus();
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 10:
                    android.hardware.health.StorageInfo[] _result7 = getStorageInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result7, 1);
                    return true;
                case 11:
                    android.hardware.health.DiskStats[] _result8 = getDiskStats();
                    reply.writeNoException();
                    reply.writeTypedArray(_result8, 1);
                    return true;
                case 12:
                    android.hardware.health.HealthInfo _result9 = getHealthInfo();
                    reply.writeNoException();
                    reply.writeTypedObject(_result9, 1);
                    return true;
                case 13:
                    int _arg03 = data.readInt();
                    data.enforceNoDataAvail();
                    setChargingPolicy(_arg03);
                    reply.writeNoException();
                    return true;
                case 14:
                    int _result10 = getChargingPolicy();
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 15:
                    android.hardware.health.BatteryHealthData _result11 = getBatteryHealthData();
                    reply.writeNoException();
                    reply.writeTypedObject(_result11, 1);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.health.IHealth {
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

            @Override // android.hardware.health.IHealth
            public void registerCallback(android.hardware.health.IHealthInfoCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method registerCallback is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public void unregisterCallback(android.hardware.health.IHealthInfoCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method unregisterCallback is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public void update() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method update is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getChargeCounterUah() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChargeCounterUah is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getCurrentNowMicroamps() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getCurrentNowMicroamps is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getCurrentAverageMicroamps() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getCurrentAverageMicroamps is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getCapacity() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getCapacity is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public long getEnergyCounterNwh() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getEnergyCounterNwh is unimplemented.");
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getChargeStatus() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChargeStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public android.hardware.health.StorageInfo[] getStorageInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getStorageInfo is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.health.StorageInfo[] _result = (android.hardware.health.StorageInfo[]) _reply.createTypedArray(android.hardware.health.StorageInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public android.hardware.health.DiskStats[] getDiskStats() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getDiskStats is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.health.DiskStats[] _result = (android.hardware.health.DiskStats[]) _reply.createTypedArray(android.hardware.health.DiskStats.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public android.hardware.health.HealthInfo getHealthInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getHealthInfo is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.health.HealthInfo _result = (android.hardware.health.HealthInfo) _reply.readTypedObject(android.hardware.health.HealthInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public void setChargingPolicy(int in_value) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(in_value);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChargingPolicy is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public int getChargingPolicy() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChargingPolicy is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
            public android.hardware.health.BatteryHealthData getBatteryHealthData() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBatteryHealthData is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.health.BatteryHealthData _result = (android.hardware.health.BatteryHealthData) _reply.readTypedObject(android.hardware.health.BatteryHealthData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.health.IHealth
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

            @Override // android.hardware.health.IHealth
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.health.IHealth.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
