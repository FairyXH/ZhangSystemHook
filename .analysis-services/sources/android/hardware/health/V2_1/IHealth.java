package android.hardware.health.V2_1;

/* JADX INFO: loaded from: classes.dex */
public interface IHealth extends android.hardware.health.V2_0.IHealth {
    public static final java.lang.String kInterfaceName = "android.hardware.health@2.1::IHealth";

    @java.lang.FunctionalInterface
    public interface getHealthConfigCallback {
        void onValues(int i, android.hardware.health.V2_1.HealthConfig healthConfig);
    }

    @java.lang.FunctionalInterface
    public interface getHealthInfo_2_1Callback {
        void onValues(int i, android.hardware.health.V2_1.HealthInfo healthInfo);
    }

    @java.lang.FunctionalInterface
    public interface shouldKeepScreenOnCallback {
        void onValues(int i, boolean z);
    }

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    void getHealthConfig(android.hardware.health.V2_1.IHealth.getHealthConfigCallback gethealthconfigcallback) throws android.os.RemoteException;

    void getHealthInfo_2_1(android.hardware.health.V2_1.IHealth.getHealthInfo_2_1Callback gethealthinfo_2_1callback) throws android.os.RemoteException;

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    void shouldKeepScreenOn(android.hardware.health.V2_1.IHealth.shouldKeepScreenOnCallback shouldkeepscreenoncallback) throws android.os.RemoteException;

    @Override // android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static android.hardware.health.V2_1.IHealth asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof android.hardware.health.V2_1.IHealth)) {
            return (android.hardware.health.V2_1.IHealth) iface;
        }
        android.hardware.health.V2_1.IHealth proxy = new android.hardware.health.V2_1.IHealth.Proxy(binder);
        try {
            for (java.lang.String descriptor : proxy.interfaceChain()) {
                if (descriptor.equals(kInterfaceName)) {
                    return proxy;
                }
            }
        } catch (android.os.RemoteException e) {
        }
        return null;
    }

    static android.hardware.health.V2_1.IHealth castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static android.hardware.health.V2_1.IHealth getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static android.hardware.health.V2_1.IHealth getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static android.hardware.health.V2_1.IHealth getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static android.hardware.health.V2_1.IHealth getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements android.hardware.health.V2_1.IHealth {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of android.hardware.health@2.1::IHealth]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.health.V2_0.IHealth
        public int registerCallback(android.hardware.health.V2_0.IHealthInfoCallback callback) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public int unregisterCallback(android.hardware.health.V2_0.IHealthInfoCallback callback) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public int update() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public void getChargeCounter(android.hardware.health.V2_0.IHealth.getChargeCounterCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                int _hidl_out_value = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public void getCurrentNow(android.hardware.health.V2_0.IHealth.getCurrentNowCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                int _hidl_out_value = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public void getCurrentAverage(android.hardware.health.V2_0.IHealth.getCurrentAverageCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                int _hidl_out_value = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public void getCapacity(android.hardware.health.V2_0.IHealth.getCapacityCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                int _hidl_out_value = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public void getEnergyCounter(android.hardware.health.V2_0.IHealth.getEnergyCounterCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                long _hidl_out_value = _hidl_reply.readInt64();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public void getChargeStatus(android.hardware.health.V2_0.IHealth.getChargeStatusCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                int _hidl_out_value = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public void getStorageInfo(android.hardware.health.V2_0.IHealth.getStorageInfoCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                java.util.ArrayList<android.hardware.health.V2_0.StorageInfo> _hidl_out_value = android.hardware.health.V2_0.StorageInfo.readVectorFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public void getDiskStats(android.hardware.health.V2_0.IHealth.getDiskStatsCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                java.util.ArrayList<android.hardware.health.V2_0.DiskStats> _hidl_out_value = android.hardware.health.V2_0.DiskStats.readVectorFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_0.IHealth
        public void getHealthInfo(android.hardware.health.V2_0.IHealth.getHealthInfoCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_0.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                android.hardware.health.V2_0.HealthInfo _hidl_out_value = new android.hardware.health.V2_0.HealthInfo();
                _hidl_out_value.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth
        public void getHealthConfig(android.hardware.health.V2_1.IHealth.getHealthConfigCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_1.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(13, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                android.hardware.health.V2_1.HealthConfig _hidl_out_config = new android.hardware.health.V2_1.HealthConfig();
                _hidl_out_config.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_config);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth
        public void getHealthInfo_2_1(android.hardware.health.V2_1.IHealth.getHealthInfo_2_1Callback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_1.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(14, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                android.hardware.health.V2_1.HealthInfo _hidl_out_value = new android.hardware.health.V2_1.HealthInfo();
                _hidl_out_value.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth
        public void shouldKeepScreenOn(android.hardware.health.V2_1.IHealth.shouldKeepScreenOnCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.health.V2_1.IHealth.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(15, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                boolean _hidl_out_value = _hidl_reply.readBool();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256067662, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<java.lang.String> _hidl_out_descriptors = _hidl_reply.readStringVector();
                return _hidl_out_descriptors;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            _hidl_request.writeNativeHandle(fd);
            _hidl_request.writeStringVector(options);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256131655, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public java.lang.String interfaceDescriptor() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256136003, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.lang.String _hidl_out_descriptor = _hidl_reply.readString();
                return _hidl_out_descriptor;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256398152, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<byte[]> _hidl_out_hashchain = new java.util.ArrayList<>();
                android.os.HwBlob _hidl_blob = _hidl_reply.readBuffer(16L);
                int _hidl_vec_size = _hidl_blob.getInt32(8L);
                android.os.HwBlob childBlob = _hidl_reply.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
                _hidl_out_hashchain.clear();
                for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                    byte[] _hidl_vec_element = new byte[32];
                    long _hidl_array_offset_1 = _hidl_index_0 * 32;
                    childBlob.copyToInt8Array(_hidl_array_offset_1, _hidl_vec_element, 32);
                    _hidl_out_hashchain.add(_hidl_vec_element);
                }
                return _hidl_out_hashchain;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public void setHALInstrumentation() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256462420, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public void ping() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256921159, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(257049926, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                android.hidl.base.V1_0.DebugInfo _hidl_out_info = new android.hidl.base.V1_0.DebugInfo();
                _hidl_out_info.readFromParcel(_hidl_reply);
                return _hidl_out_info;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public void notifySyspropsChanged() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(257120595, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements android.hardware.health.V2_1.IHealth {
        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(android.hardware.health.V2_1.IHealth.kInterfaceName, android.hardware.health.V2_0.IHealth.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return android.hardware.health.V2_1.IHealth.kInterfaceName;
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{-50, -115, -66, 118, -21, -98, -23, 75, 70, -17, -104, -9, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_ENDPOINT, -66, -103, 46, 118, 10, 87, 81, 7, 61, 79, 73, 18, 72, 64, 38, 84, 19, 113, -13}, new byte[]{103, 86, com.android.server.usb.descriptors.UsbASFormat.EXT_FORMAT_TYPE_II, -35, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_ENDPOINT_COMPANION, 7, -128, 92, -104, 94, -86, -20, -111, 97, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_SUPERSPEED_HUB, -68, -120, -12, -62, 91, 52, 49, -5, -124, 7, 11, 117, -124, -95, -89, 65, -5}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // android.hardware.health.V2_1.IHealth, android.hardware.health.V2_0.IHealth, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (android.hardware.health.V2_1.IHealth.kInterfaceName.equals(descriptor)) {
                return this;
            }
            return null;
        }

        public void registerAsService(java.lang.String serviceName) throws android.os.RemoteException {
            registerService(serviceName);
        }

        public java.lang.String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        public void onTransact(int _hidl_code, android.os.HwParcel _hidl_request, final android.os.HwParcel _hidl_reply, int _hidl_flags) throws android.os.RemoteException {
            switch (_hidl_code) {
                case 1:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    android.hardware.health.V2_0.IHealthInfoCallback callback = android.hardware.health.V2_0.IHealthInfoCallback.asInterface(_hidl_request.readStrongBinder());
                    int _hidl_out_result = registerCallback(callback);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result);
                    _hidl_reply.send();
                    return;
                case 2:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    android.hardware.health.V2_0.IHealthInfoCallback callback2 = android.hardware.health.V2_0.IHealthInfoCallback.asInterface(_hidl_request.readStrongBinder());
                    int _hidl_out_result2 = unregisterCallback(callback2);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result2);
                    _hidl_reply.send();
                    return;
                case 3:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    int _hidl_out_result3 = update();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result3);
                    _hidl_reply.send();
                    return;
                case 4:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    getChargeCounter(new android.hardware.health.V2_0.IHealth.getChargeCounterCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.1
                        @Override // android.hardware.health.V2_0.IHealth.getChargeCounterCallback
                        public void onValues(int result, int value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt32(value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 5:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    getCurrentNow(new android.hardware.health.V2_0.IHealth.getCurrentNowCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.2
                        @Override // android.hardware.health.V2_0.IHealth.getCurrentNowCallback
                        public void onValues(int result, int value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt32(value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 6:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    getCurrentAverage(new android.hardware.health.V2_0.IHealth.getCurrentAverageCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.3
                        @Override // android.hardware.health.V2_0.IHealth.getCurrentAverageCallback
                        public void onValues(int result, int value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt32(value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 7:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    getCapacity(new android.hardware.health.V2_0.IHealth.getCapacityCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.4
                        @Override // android.hardware.health.V2_0.IHealth.getCapacityCallback
                        public void onValues(int result, int value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt32(value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 8:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    getEnergyCounter(new android.hardware.health.V2_0.IHealth.getEnergyCounterCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.5
                        @Override // android.hardware.health.V2_0.IHealth.getEnergyCounterCallback
                        public void onValues(int result, long value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt64(value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 9:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    getChargeStatus(new android.hardware.health.V2_0.IHealth.getChargeStatusCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.6
                        @Override // android.hardware.health.V2_0.IHealth.getChargeStatusCallback
                        public void onValues(int result, int value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt32(value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 10:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    getStorageInfo(new android.hardware.health.V2_0.IHealth.getStorageInfoCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.7
                        @Override // android.hardware.health.V2_0.IHealth.getStorageInfoCallback
                        public void onValues(int result, java.util.ArrayList<android.hardware.health.V2_0.StorageInfo> value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            android.hardware.health.V2_0.StorageInfo.writeVectorToParcel(_hidl_reply, value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 11:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    getDiskStats(new android.hardware.health.V2_0.IHealth.getDiskStatsCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.8
                        @Override // android.hardware.health.V2_0.IHealth.getDiskStatsCallback
                        public void onValues(int result, java.util.ArrayList<android.hardware.health.V2_0.DiskStats> value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            android.hardware.health.V2_0.DiskStats.writeVectorToParcel(_hidl_reply, value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 12:
                    _hidl_request.enforceInterface(android.hardware.health.V2_0.IHealth.kInterfaceName);
                    getHealthInfo(new android.hardware.health.V2_0.IHealth.getHealthInfoCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.9
                        @Override // android.hardware.health.V2_0.IHealth.getHealthInfoCallback
                        public void onValues(int result, android.hardware.health.V2_0.HealthInfo value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            value.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 13:
                    _hidl_request.enforceInterface(android.hardware.health.V2_1.IHealth.kInterfaceName);
                    getHealthConfig(new android.hardware.health.V2_1.IHealth.getHealthConfigCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.10
                        @Override // android.hardware.health.V2_1.IHealth.getHealthConfigCallback
                        public void onValues(int result, android.hardware.health.V2_1.HealthConfig config) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            config.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 14:
                    _hidl_request.enforceInterface(android.hardware.health.V2_1.IHealth.kInterfaceName);
                    getHealthInfo_2_1(new android.hardware.health.V2_1.IHealth.getHealthInfo_2_1Callback() { // from class: android.hardware.health.V2_1.IHealth.Stub.11
                        @Override // android.hardware.health.V2_1.IHealth.getHealthInfo_2_1Callback
                        public void onValues(int result, android.hardware.health.V2_1.HealthInfo value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            value.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 15:
                    _hidl_request.enforceInterface(android.hardware.health.V2_1.IHealth.kInterfaceName);
                    shouldKeepScreenOn(new android.hardware.health.V2_1.IHealth.shouldKeepScreenOnCallback() { // from class: android.hardware.health.V2_1.IHealth.Stub.12
                        @Override // android.hardware.health.V2_1.IHealth.shouldKeepScreenOnCallback
                        public void onValues(int result, boolean value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeBool(value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 256067662:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    java.util.ArrayList<java.lang.String> _hidl_out_descriptors = interfaceChain();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeStringVector(_hidl_out_descriptors);
                    _hidl_reply.send();
                    return;
                case 256131655:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    android.os.NativeHandle fd = _hidl_request.readNativeHandle();
                    java.util.ArrayList<java.lang.String> options = _hidl_request.readStringVector();
                    debug(fd, options);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 256136003:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    java.lang.String _hidl_out_descriptor = interfaceDescriptor();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeString(_hidl_out_descriptor);
                    _hidl_reply.send();
                    return;
                case 256398152:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    java.util.ArrayList<byte[]> _hidl_out_hashchain = getHashChain();
                    _hidl_reply.writeStatus(0);
                    android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
                    int _hidl_vec_size = _hidl_out_hashchain.size();
                    _hidl_blob.putInt32(8L, _hidl_vec_size);
                    _hidl_blob.putBool(12L, false);
                    android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 32);
                    for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                        long _hidl_array_offset_1 = _hidl_index_0 * 32;
                        byte[] _hidl_array_item_1 = _hidl_out_hashchain.get(_hidl_index_0);
                        if (_hidl_array_item_1 == null || _hidl_array_item_1.length != 32) {
                            throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
                        }
                        childBlob.putInt8Array(_hidl_array_offset_1, _hidl_array_item_1);
                    }
                    _hidl_blob.putBlob(0L, childBlob);
                    _hidl_reply.writeBuffer(_hidl_blob);
                    _hidl_reply.send();
                    return;
                case 256462420:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    setHALInstrumentation();
                    return;
                case 256660548:
                default:
                    return;
                case 256921159:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    ping();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 257049926:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    android.hidl.base.V1_0.DebugInfo _hidl_out_info = getDebugInfo();
                    _hidl_reply.writeStatus(0);
                    _hidl_out_info.writeToParcel(_hidl_reply);
                    _hidl_reply.send();
                    return;
                case 257120595:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    notifySyspropsChanged();
                    return;
            }
        }
    }
}
