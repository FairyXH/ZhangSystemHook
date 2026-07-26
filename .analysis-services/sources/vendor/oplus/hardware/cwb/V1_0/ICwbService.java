package vendor.oplus.hardware.cwb.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public interface ICwbService extends android.hidl.base.V1_0.IBase {
    public static final java.lang.String kInterfaceName = "vendor.oplus.hardware.cwb@1.0::ICwbService";

    @java.lang.FunctionalInterface
    public interface getHistogramValueCallback {
        void onValues(int i, int[][] iArr);
    }

    @java.lang.FunctionalInterface
    public interface getLumasValueCallback {
        void onValues(int i, float f);
    }

    @java.lang.FunctionalInterface
    public interface getRGBValueCallback {
        void onValues(int i, byte[] bArr);
    }

    @java.lang.FunctionalInterface
    public interface getRGBValuesCallback {
        void onValues(int i, java.util.ArrayList<byte[]> arrayList);
    }

    @Override // android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    boolean disable() throws android.os.RemoteException;

    boolean enable() throws android.os.RemoteException;

    int getCwbBuffer(vendor.oplus.hardware.cwb.V1_0.CwbRect cwbRect, vendor.oplus.hardware.cwb.V1_0.oplus_cwb_buffer oplus_cwb_bufferVar) throws android.os.RemoteException;

    boolean getCwbPostProcessStatus() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    void getHistogramValue(vendor.oplus.hardware.cwb.V1_0.CwbRect cwbRect, vendor.oplus.hardware.cwb.V1_0.ICwbService.getHistogramValueCallback gethistogramvaluecallback) throws android.os.RemoteException;

    void getLumasValue(vendor.oplus.hardware.cwb.V1_0.CwbRect cwbRect, vendor.oplus.hardware.cwb.V1_0.ICwbService.getLumasValueCallback getlumasvaluecallback) throws android.os.RemoteException;

    void getRGBValue(vendor.oplus.hardware.cwb.V1_0.CwbRect cwbRect, vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValueCallback getrgbvaluecallback) throws android.os.RemoteException;

    void getRGBValues(java.util.ArrayList<vendor.oplus.hardware.cwb.V1_0.CwbRect> arrayList, vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValuesCallback getrgbvaluescallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    int registerCallback(vendor.oplus.hardware.cwb.V1_0.ICwbCallback iCwbCallback) throws android.os.RemoteException;

    int setCwbPostProcessStatus(boolean z) throws android.os.RemoteException;

    int setDebug(java.util.ArrayList<java.lang.Integer> arrayList) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    int unregisterCallback(vendor.oplus.hardware.cwb.V1_0.ICwbCallback iCwbCallback) throws android.os.RemoteException;

    static vendor.oplus.hardware.cwb.V1_0.ICwbService asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof vendor.oplus.hardware.cwb.V1_0.ICwbService)) {
            return (vendor.oplus.hardware.cwb.V1_0.ICwbService) iface;
        }
        vendor.oplus.hardware.cwb.V1_0.ICwbService proxy = new vendor.oplus.hardware.cwb.V1_0.ICwbService.Proxy(binder);
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

    static vendor.oplus.hardware.cwb.V1_0.ICwbService castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static vendor.oplus.hardware.cwb.V1_0.ICwbService getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static vendor.oplus.hardware.cwb.V1_0.ICwbService getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static vendor.oplus.hardware.cwb.V1_0.ICwbService getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static vendor.oplus.hardware.cwb.V1_0.ICwbService getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements vendor.oplus.hardware.cwb.V1_0.ICwbService {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of vendor.oplus.hardware.cwb@1.0::ICwbService]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public void getRGBValue(vendor.oplus.hardware.cwb.V1_0.CwbRect rect, vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValueCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            rect.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_error = _hidl_reply.readInt32();
                byte[] _hidl_out_rgb = new byte[3];
                android.os.HwBlob _hidl_blob = _hidl_reply.readBuffer(3L);
                _hidl_blob.copyToInt8Array(0L, _hidl_out_rgb, 3);
                _hidl_cb.onValues(_hidl_out_error, _hidl_out_rgb);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public void getRGBValues(java.util.ArrayList<vendor.oplus.hardware.cwb.V1_0.CwbRect> rects, vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValuesCallback _hidl_cb) throws java.lang.Throwable {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            vendor.oplus.hardware.cwb.V1_0.CwbRect.writeVectorToParcel(_hidl_request, rects);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_error = _hidl_reply.readInt32();
                java.util.ArrayList<byte[]> _hidl_out_rgbs = new java.util.ArrayList<>();
                android.os.HwBlob _hidl_blob = _hidl_reply.readBuffer(16L);
                int _hidl_vec_size = _hidl_blob.getInt32(8L);
                android.os.HwBlob childBlob = _hidl_reply.readEmbeddedBuffer(_hidl_vec_size * 3, _hidl_blob.handle(), 0L, true);
                _hidl_out_rgbs.clear();
                for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                    byte[] _hidl_vec_element = new byte[3];
                    long _hidl_array_offset_1 = _hidl_index_0 * 3;
                    childBlob.copyToInt8Array(_hidl_array_offset_1, _hidl_vec_element, 3);
                    _hidl_out_rgbs.add(_hidl_vec_element);
                }
                try {
                    _hidl_cb.onValues(_hidl_out_error, _hidl_out_rgbs);
                    _hidl_reply.release();
                } catch (java.lang.Throwable th) {
                    th = th;
                    _hidl_reply.release();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public void getLumasValue(vendor.oplus.hardware.cwb.V1_0.CwbRect rect, vendor.oplus.hardware.cwb.V1_0.ICwbService.getLumasValueCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            rect.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_error = _hidl_reply.readInt32();
                float _hidl_out_lumas = _hidl_reply.readFloat();
                _hidl_cb.onValues(_hidl_out_error, _hidl_out_lumas);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public void getHistogramValue(vendor.oplus.hardware.cwb.V1_0.CwbRect rect, vendor.oplus.hardware.cwb.V1_0.ICwbService.getHistogramValueCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            rect.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_error = _hidl_reply.readInt32();
                int[][] _hidl_out_histogram = (int[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Integer.TYPE, 256, 3);
                android.os.HwBlob _hidl_blob = _hidl_reply.readBuffer(3072L);
                long _hidl_array_offset_0 = 0;
                for (int _hidl_index_0_0 = 0; _hidl_index_0_0 < 256; _hidl_index_0_0++) {
                    _hidl_blob.copyToInt32Array(_hidl_array_offset_0, _hidl_out_histogram[_hidl_index_0_0], 3);
                    _hidl_array_offset_0 += 12;
                }
                _hidl_cb.onValues(_hidl_out_error, _hidl_out_histogram);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int getCwbBuffer(vendor.oplus.hardware.cwb.V1_0.CwbRect rect, vendor.oplus.hardware.cwb.V1_0.oplus_cwb_buffer buffer) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            rect.writeToParcel(_hidl_request);
            buffer.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_error = _hidl_reply.readInt32();
                return _hidl_out_error;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public boolean getCwbPostProcessStatus() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                boolean _hidl_out_status = _hidl_reply.readBool();
                return _hidl_out_status;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int setCwbPostProcessStatus(boolean status) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            _hidl_request.writeBool(status);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_error = _hidl_reply.readInt32();
                return _hidl_out_error;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int registerCallback(vendor.oplus.hardware.cwb.V1_0.ICwbCallback callback) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_error = _hidl_reply.readInt32();
                return _hidl_out_error;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int unregisterCallback(vendor.oplus.hardware.cwb.V1_0.ICwbCallback callback) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_error = _hidl_reply.readInt32();
                return _hidl_out_error;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public boolean enable() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                boolean _hidl_out_status = _hidl_reply.readBool();
                return _hidl_out_status;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public boolean disable() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                boolean _hidl_out_status = _hidl_reply.readBool();
                return _hidl_out_status;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int setDebug(java.util.ArrayList<java.lang.Integer> values) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
            _hidl_request.writeInt32Vector(values);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_error = _hidl_reply.readInt32();
                return _hidl_out_error;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements vendor.oplus.hardware.cwb.V1_0.ICwbService {
        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{-55, 1, -17, 6, 26, 56, -27, 20, 60, -98, -91, -1, -4, 98, 68, 7, 68, 77, -31, -29, 6, -118, 106, -29, -31, -16, -117, -101, 109, -58, -82, 67}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    vendor.oplus.hardware.cwb.V1_0.CwbRect rect = new vendor.oplus.hardware.cwb.V1_0.CwbRect();
                    rect.readFromParcel(_hidl_request);
                    getRGBValue(rect, new vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValueCallback() { // from class: vendor.oplus.hardware.cwb.V1_0.ICwbService.Stub.1
                        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValueCallback
                        public void onValues(int error, byte[] rgb) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(error);
                            android.os.HwBlob _hidl_blob = new android.os.HwBlob(3);
                            if (rgb == null || rgb.length != 3) {
                                throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
                            }
                            _hidl_blob.putInt8Array(0L, rgb);
                            _hidl_reply.writeBuffer(_hidl_blob);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 2:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    java.util.ArrayList<vendor.oplus.hardware.cwb.V1_0.CwbRect> rects = vendor.oplus.hardware.cwb.V1_0.CwbRect.readVectorFromParcel(_hidl_request);
                    getRGBValues(rects, new vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValuesCallback() { // from class: vendor.oplus.hardware.cwb.V1_0.ICwbService.Stub.2
                        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValuesCallback
                        public void onValues(int error, java.util.ArrayList<byte[]> rgbs) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(error);
                            android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
                            int _hidl_vec_size = rgbs.size();
                            _hidl_blob.putInt32(8L, _hidl_vec_size);
                            _hidl_blob.putBool(12L, false);
                            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 3);
                            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                                long _hidl_array_offset_1 = _hidl_index_0 * 3;
                                byte[] _hidl_array_item_1 = rgbs.get(_hidl_index_0);
                                if (_hidl_array_item_1 == null || _hidl_array_item_1.length != 3) {
                                    throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
                                }
                                childBlob.putInt8Array(_hidl_array_offset_1, _hidl_array_item_1);
                            }
                            _hidl_blob.putBlob(0L, childBlob);
                            _hidl_reply.writeBuffer(_hidl_blob);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 3:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    vendor.oplus.hardware.cwb.V1_0.CwbRect rect2 = new vendor.oplus.hardware.cwb.V1_0.CwbRect();
                    rect2.readFromParcel(_hidl_request);
                    getLumasValue(rect2, new vendor.oplus.hardware.cwb.V1_0.ICwbService.getLumasValueCallback() { // from class: vendor.oplus.hardware.cwb.V1_0.ICwbService.Stub.3
                        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService.getLumasValueCallback
                        public void onValues(int error, float lumas) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(error);
                            _hidl_reply.writeFloat(lumas);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 4:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    vendor.oplus.hardware.cwb.V1_0.CwbRect rect3 = new vendor.oplus.hardware.cwb.V1_0.CwbRect();
                    rect3.readFromParcel(_hidl_request);
                    getHistogramValue(rect3, new vendor.oplus.hardware.cwb.V1_0.ICwbService.getHistogramValueCallback() { // from class: vendor.oplus.hardware.cwb.V1_0.ICwbService.Stub.4
                        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService.getHistogramValueCallback
                        public void onValues(int error, int[][] histogram) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(error);
                            android.os.HwBlob _hidl_blob = new android.os.HwBlob(3072);
                            long _hidl_array_offset_0 = 0;
                            for (int _hidl_index_0_0 = 0; _hidl_index_0_0 < 256; _hidl_index_0_0++) {
                                int[] _hidl_array_item_0 = histogram[_hidl_index_0_0];
                                if (_hidl_array_item_0 == null || _hidl_array_item_0.length != 3) {
                                    throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
                                }
                                _hidl_blob.putInt32Array(_hidl_array_offset_0, _hidl_array_item_0);
                                _hidl_array_offset_0 += 12;
                            }
                            _hidl_reply.writeBuffer(_hidl_blob);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 5:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    vendor.oplus.hardware.cwb.V1_0.CwbRect rect4 = new vendor.oplus.hardware.cwb.V1_0.CwbRect();
                    rect4.readFromParcel(_hidl_request);
                    vendor.oplus.hardware.cwb.V1_0.oplus_cwb_buffer buffer = new vendor.oplus.hardware.cwb.V1_0.oplus_cwb_buffer();
                    buffer.readFromParcel(_hidl_request);
                    int _hidl_out_error = getCwbBuffer(rect4, buffer);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_error);
                    _hidl_reply.send();
                    return;
                case 6:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    boolean _hidl_out_status = getCwbPostProcessStatus();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeBool(_hidl_out_status);
                    _hidl_reply.send();
                    return;
                case 7:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    boolean status = _hidl_request.readBool();
                    int _hidl_out_error2 = setCwbPostProcessStatus(status);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_error2);
                    _hidl_reply.send();
                    return;
                case 8:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    vendor.oplus.hardware.cwb.V1_0.ICwbCallback callback = vendor.oplus.hardware.cwb.V1_0.ICwbCallback.asInterface(_hidl_request.readStrongBinder());
                    int _hidl_out_error3 = registerCallback(callback);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_error3);
                    _hidl_reply.send();
                    return;
                case 9:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    vendor.oplus.hardware.cwb.V1_0.ICwbCallback callback2 = vendor.oplus.hardware.cwb.V1_0.ICwbCallback.asInterface(_hidl_request.readStrongBinder());
                    int _hidl_out_error4 = unregisterCallback(callback2);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_error4);
                    _hidl_reply.send();
                    return;
                case 10:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    boolean _hidl_out_status2 = enable();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeBool(_hidl_out_status2);
                    _hidl_reply.send();
                    return;
                case 11:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    boolean _hidl_out_status3 = disable();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeBool(_hidl_out_status3);
                    _hidl_reply.send();
                    return;
                case 12:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.cwb.V1_0.ICwbService.kInterfaceName);
                    java.util.ArrayList<java.lang.Integer> values = _hidl_request.readInt32Vector();
                    int _hidl_out_error5 = setDebug(values);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_error5);
                    _hidl_reply.send();
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
