package android.hardware.boot.V1_1;

/* JADX INFO: loaded from: classes.dex */
public interface IBootControl extends android.hardware.boot.V1_0.IBootControl {
    public static final java.lang.String kInterfaceName = "android.hardware.boot@1.1::IBootControl";

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    int getSnapshotMergeStatus() throws android.os.RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    boolean setSnapshotMergeStatus(int i) throws android.os.RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static android.hardware.boot.V1_1.IBootControl asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof android.hardware.boot.V1_1.IBootControl)) {
            return (android.hardware.boot.V1_1.IBootControl) iface;
        }
        android.hardware.boot.V1_1.IBootControl proxy = new android.hardware.boot.V1_1.IBootControl.Proxy(binder);
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

    static android.hardware.boot.V1_1.IBootControl castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static android.hardware.boot.V1_1.IBootControl getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static android.hardware.boot.V1_1.IBootControl getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static android.hardware.boot.V1_1.IBootControl getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static android.hardware.boot.V1_1.IBootControl getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements android.hardware.boot.V1_1.IBootControl {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of android.hardware.boot@1.1::IBootControl]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public int getNumberSlots() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_numSlots = _hidl_reply.readInt32();
                return _hidl_out_numSlots;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public int getCurrentSlot() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_slot = _hidl_reply.readInt32();
                return _hidl_out_slot;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public android.hardware.boot.V1_0.CommandResult markBootSuccessful() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                android.hardware.boot.V1_0.CommandResult _hidl_out_error = new android.hardware.boot.V1_0.CommandResult();
                _hidl_out_error.readFromParcel(_hidl_reply);
                return _hidl_out_error;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public android.hardware.boot.V1_0.CommandResult setActiveBootSlot(int slot) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            _hidl_request.writeInt32(slot);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                android.hardware.boot.V1_0.CommandResult _hidl_out_error = new android.hardware.boot.V1_0.CommandResult();
                _hidl_out_error.readFromParcel(_hidl_reply);
                return _hidl_out_error;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public android.hardware.boot.V1_0.CommandResult setSlotAsUnbootable(int slot) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            _hidl_request.writeInt32(slot);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                android.hardware.boot.V1_0.CommandResult _hidl_out_error = new android.hardware.boot.V1_0.CommandResult();
                _hidl_out_error.readFromParcel(_hidl_reply);
                return _hidl_out_error;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public int isSlotBootable(int slot) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            _hidl_request.writeInt32(slot);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_bootable = _hidl_reply.readInt32();
                return _hidl_out_bootable;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public int isSlotMarkedSuccessful(int slot) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            _hidl_request.writeInt32(slot);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_successful = _hidl_reply.readInt32();
                return _hidl_out_successful;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public java.lang.String getSuffix(int slot) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            _hidl_request.writeInt32(slot);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.lang.String _hidl_out_slotSuffix = _hidl_reply.readString();
                return _hidl_out_slotSuffix;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_1.IBootControl
        public boolean setSnapshotMergeStatus(int status) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_1.IBootControl.kInterfaceName);
            _hidl_request.writeInt32(status);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                boolean _hidl_out_success = _hidl_reply.readBool();
                return _hidl_out_success;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_1.IBootControl
        public int getSnapshotMergeStatus() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.boot.V1_1.IBootControl.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_status = _hidl_reply.readInt32();
                return _hidl_out_status;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements android.hardware.boot.V1_1.IBootControl {
        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(android.hardware.boot.V1_1.IBootControl.kInterfaceName, android.hardware.boot.V1_0.IBootControl.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return android.hardware.boot.V1_1.IBootControl.kInterfaceName;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{7, -48, -94, 82, -78, -40, -6, 53, -120, 121, 8, -87, -106, -70, 57, 92, -13, -110, -106, com.android.server.usb.descriptors.UsbASFormat.EXT_FORMAT_TYPE_III, -107, -4, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_ENDPOINT_COMPANION, -81, -85, 121, 31, 70, -32, -62, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_SUPERSPEED_HUB, 82}, new byte[]{113, -110, -41, 86, -82, -70, 0, -85, -93, 47, 69, 4, -104, 29, -8, 23, 47, -4, -88, 62, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_HID, 12, 72, 56, -38, -65, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_HUB, 94, 83, -23, 53, -112}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (android.hardware.boot.V1_1.IBootControl.kInterfaceName.equals(descriptor)) {
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

        public void onTransact(int _hidl_code, android.os.HwParcel _hidl_request, android.os.HwParcel _hidl_reply, int _hidl_flags) throws android.os.RemoteException {
            switch (_hidl_code) {
                case 1:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int _hidl_out_numSlots = getNumberSlots();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_numSlots);
                    _hidl_reply.send();
                    return;
                case 2:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int _hidl_out_slot = getCurrentSlot();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_slot);
                    _hidl_reply.send();
                    return;
                case 3:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    android.hardware.boot.V1_0.CommandResult _hidl_out_error = markBootSuccessful();
                    _hidl_reply.writeStatus(0);
                    _hidl_out_error.writeToParcel(_hidl_reply);
                    _hidl_reply.send();
                    return;
                case 4:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int slot = _hidl_request.readInt32();
                    android.hardware.boot.V1_0.CommandResult _hidl_out_error2 = setActiveBootSlot(slot);
                    _hidl_reply.writeStatus(0);
                    _hidl_out_error2.writeToParcel(_hidl_reply);
                    _hidl_reply.send();
                    return;
                case 5:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int slot2 = _hidl_request.readInt32();
                    android.hardware.boot.V1_0.CommandResult _hidl_out_error3 = setSlotAsUnbootable(slot2);
                    _hidl_reply.writeStatus(0);
                    _hidl_out_error3.writeToParcel(_hidl_reply);
                    _hidl_reply.send();
                    return;
                case 6:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int slot3 = _hidl_request.readInt32();
                    int _hidl_out_bootable = isSlotBootable(slot3);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_bootable);
                    _hidl_reply.send();
                    return;
                case 7:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int slot4 = _hidl_request.readInt32();
                    int _hidl_out_successful = isSlotMarkedSuccessful(slot4);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_successful);
                    _hidl_reply.send();
                    return;
                case 8:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int slot5 = _hidl_request.readInt32();
                    java.lang.String _hidl_out_slotSuffix = getSuffix(slot5);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeString(_hidl_out_slotSuffix);
                    _hidl_reply.send();
                    return;
                case 9:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_1.IBootControl.kInterfaceName);
                    int status = _hidl_request.readInt32();
                    boolean _hidl_out_success = setSnapshotMergeStatus(status);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeBool(_hidl_out_success);
                    _hidl_reply.send();
                    return;
                case 10:
                    _hidl_request.enforceInterface(android.hardware.boot.V1_1.IBootControl.kInterfaceName);
                    int _hidl_out_status = getSnapshotMergeStatus();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_status);
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
