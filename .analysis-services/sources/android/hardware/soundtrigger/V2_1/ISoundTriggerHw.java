package android.hardware.soundtrigger.V2_1;

/* JADX INFO: loaded from: classes.dex */
public interface ISoundTriggerHw extends android.hardware.soundtrigger.V2_0.ISoundTriggerHw {
    public static final java.lang.String kInterfaceName = "android.hardware.soundtrigger@2.1::ISoundTriggerHw";

    @java.lang.FunctionalInterface
    public interface loadPhraseSoundModel_2_1Callback {
        void onValues(int i, int i2);
    }

    @java.lang.FunctionalInterface
    public interface loadSoundModel_2_1Callback {
        void onValues(int i, int i2);
    }

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    void loadPhraseSoundModel_2_1(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel phraseSoundModel, android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback iSoundTriggerHwCallback, int i, android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadPhraseSoundModel_2_1Callback loadphrasesoundmodel_2_1callback) throws android.os.RemoteException;

    void loadSoundModel_2_1(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel soundModel, android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback iSoundTriggerHwCallback, int i, android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadSoundModel_2_1Callback loadsoundmodel_2_1callback) throws android.os.RemoteException;

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    int startRecognition_2_1(int i, android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig recognitionConfig, android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback iSoundTriggerHwCallback, int i2) throws android.os.RemoteException;

    @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static android.hardware.soundtrigger.V2_1.ISoundTriggerHw asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof android.hardware.soundtrigger.V2_1.ISoundTriggerHw)) {
            return (android.hardware.soundtrigger.V2_1.ISoundTriggerHw) iface;
        }
        android.hardware.soundtrigger.V2_1.ISoundTriggerHw proxy = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.Proxy(binder);
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

    static android.hardware.soundtrigger.V2_1.ISoundTriggerHw castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static android.hardware.soundtrigger.V2_1.ISoundTriggerHw getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static android.hardware.soundtrigger.V2_1.ISoundTriggerHw getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static android.hardware.soundtrigger.V2_1.ISoundTriggerHw getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static android.hardware.soundtrigger.V2_1.ISoundTriggerHw getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class SoundModel {
        public android.hardware.soundtrigger.V2_0.ISoundTriggerHw.SoundModel header = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.SoundModel();
        public android.os.HidlMemory data = null;

        public final java.lang.String toString() {
            return "{.header = " + this.header + ", .data = " + this.data + "}";
        }

        public final void readFromParcel(android.os.HwParcel parcel) {
            android.os.HwBlob blob = parcel.readBuffer(96L);
            readEmbeddedFromParcel(parcel, blob, 0L);
        }

        public static final java.util.ArrayList<android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel> readVectorFromParcel(android.os.HwParcel parcel) {
            java.util.ArrayList<android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel> _hidl_vec = new java.util.ArrayList<>();
            android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
            int _hidl_vec_size = _hidl_blob.getInt32(8L);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 96, _hidl_blob.handle(), 0L, true);
            _hidl_vec.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel _hidl_vec_element = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 96);
                _hidl_vec.add(_hidl_vec_element);
            }
            return _hidl_vec;
        }

        public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.header.readEmbeddedFromParcel(parcel, _hidl_blob, 0 + _hidl_offset);
            try {
                this.data = parcel.readEmbeddedHidlMemory(_hidl_blob.getFieldHandle(_hidl_offset + 56), _hidl_blob.handle(), _hidl_offset + 56).dup();
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        public final void writeToParcel(android.os.HwParcel parcel) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(96);
            writeEmbeddedToBlob(_hidl_blob, 0L);
            parcel.writeBuffer(_hidl_blob);
        }

        public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel> _hidl_vec) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
            int _hidl_vec_size = _hidl_vec.size();
            _hidl_blob.putInt32(8L, _hidl_vec_size);
            _hidl_blob.putBool(12L, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 96);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 96);
            }
            _hidl_blob.putBlob(0L, childBlob);
            parcel.writeBuffer(_hidl_blob);
        }

        public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.header.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
            _hidl_blob.putHidlMemory(56 + _hidl_offset, this.data);
        }
    }

    public static final class PhraseSoundModel {
        public android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel common = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel();
        public java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Phrase> phrases = new java.util.ArrayList<>();

        public final java.lang.String toString() {
            return "{.common = " + this.common + ", .phrases = " + this.phrases + "}";
        }

        public final void readFromParcel(android.os.HwParcel parcel) {
            android.os.HwBlob blob = parcel.readBuffer(112L);
            readEmbeddedFromParcel(parcel, blob, 0L);
        }

        public static final java.util.ArrayList<android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel> readVectorFromParcel(android.os.HwParcel parcel) {
            java.util.ArrayList<android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel> _hidl_vec = new java.util.ArrayList<>();
            android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
            int _hidl_vec_size = _hidl_blob.getInt32(8L);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 112, _hidl_blob.handle(), 0L, true);
            _hidl_vec.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel _hidl_vec_element = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 112);
                _hidl_vec.add(_hidl_vec_element);
            }
            return _hidl_vec;
        }

        public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.common.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
            int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 96 + 8);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 56, _hidl_blob.handle(), _hidl_offset + 96 + 0, true);
            this.phrases.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Phrase _hidl_vec_element = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Phrase();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 56);
                this.phrases.add(_hidl_vec_element);
            }
        }

        public final void writeToParcel(android.os.HwParcel parcel) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(112);
            writeEmbeddedToBlob(_hidl_blob, 0L);
            parcel.writeBuffer(_hidl_blob);
        }

        public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel> _hidl_vec) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
            int _hidl_vec_size = _hidl_vec.size();
            _hidl_blob.putInt32(8L, _hidl_vec_size);
            _hidl_blob.putBool(12L, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 112);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 112);
            }
            _hidl_blob.putBlob(0L, childBlob);
            parcel.writeBuffer(_hidl_blob);
        }

        public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.common.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 0);
            int _hidl_vec_size = this.phrases.size();
            _hidl_blob.putInt32(_hidl_offset + 96 + 8, _hidl_vec_size);
            _hidl_blob.putBool(_hidl_offset + 96 + 12, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 56);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                this.phrases.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 56);
            }
            _hidl_blob.putBlob(96 + _hidl_offset + 0, childBlob);
        }
    }

    public static final class RecognitionConfig {
        public android.hardware.soundtrigger.V2_0.ISoundTriggerHw.RecognitionConfig header = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.RecognitionConfig();
        public android.os.HidlMemory data = null;

        public final java.lang.String toString() {
            return "{.header = " + this.header + ", .data = " + this.data + "}";
        }

        public final void readFromParcel(android.os.HwParcel parcel) {
            android.os.HwBlob blob = parcel.readBuffer(88L);
            readEmbeddedFromParcel(parcel, blob, 0L);
        }

        public static final java.util.ArrayList<android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig> readVectorFromParcel(android.os.HwParcel parcel) {
            java.util.ArrayList<android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig> _hidl_vec = new java.util.ArrayList<>();
            android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
            int _hidl_vec_size = _hidl_blob.getInt32(8L);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 88, _hidl_blob.handle(), 0L, true);
            _hidl_vec.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig _hidl_vec_element = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 88);
                _hidl_vec.add(_hidl_vec_element);
            }
            return _hidl_vec;
        }

        public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.header.readEmbeddedFromParcel(parcel, _hidl_blob, 0 + _hidl_offset);
            try {
                this.data = parcel.readEmbeddedHidlMemory(_hidl_blob.getFieldHandle(_hidl_offset + 48), _hidl_blob.handle(), _hidl_offset + 48).dup();
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        public final void writeToParcel(android.os.HwParcel parcel) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(88);
            writeEmbeddedToBlob(_hidl_blob, 0L);
            parcel.writeBuffer(_hidl_blob);
        }

        public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig> _hidl_vec) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
            int _hidl_vec_size = _hidl_vec.size();
            _hidl_blob.putInt32(8L, _hidl_vec_size);
            _hidl_blob.putBool(12L, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 88);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 88);
            }
            _hidl_blob.putBlob(0L, childBlob);
            parcel.writeBuffer(_hidl_blob);
        }

        public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.header.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
            _hidl_blob.putHidlMemory(48 + _hidl_offset, this.data);
        }
    }

    public static final class Proxy implements android.hardware.soundtrigger.V2_1.ISoundTriggerHw {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of android.hardware.soundtrigger@2.1::ISoundTriggerHw]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw
        public void getProperties(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.getPropertiesCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties _hidl_out_properties = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties();
                _hidl_out_properties.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_retval, _hidl_out_properties);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw
        public void loadSoundModel(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.SoundModel soundModel, android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback callback, int cookie, android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadSoundModelCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
            soundModel.writeToParcel(_hidl_request);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            _hidl_request.writeInt32(cookie);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                int _hidl_out_modelHandle = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_retval, _hidl_out_modelHandle);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw
        public void loadPhraseSoundModel(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.PhraseSoundModel soundModel, android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback callback, int cookie, android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadPhraseSoundModelCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
            soundModel.writeToParcel(_hidl_request);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            _hidl_request.writeInt32(cookie);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                int _hidl_out_modelHandle = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_retval, _hidl_out_modelHandle);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw
        public int unloadSoundModel(int modelHandle) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
            _hidl_request.writeInt32(modelHandle);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                return _hidl_out_retval;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw
        public int startRecognition(int modelHandle, android.hardware.soundtrigger.V2_0.ISoundTriggerHw.RecognitionConfig config, android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback callback, int cookie) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
            _hidl_request.writeInt32(modelHandle);
            config.writeToParcel(_hidl_request);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            _hidl_request.writeInt32(cookie);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                return _hidl_out_retval;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw
        public int stopRecognition(int modelHandle) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
            _hidl_request.writeInt32(modelHandle);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                return _hidl_out_retval;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw
        public int stopAllRecognitions() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                return _hidl_out_retval;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw
        public void loadSoundModel_2_1(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel soundModel, android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback callback, int cookie, android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadSoundModel_2_1Callback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.kInterfaceName);
            soundModel.writeToParcel(_hidl_request);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            _hidl_request.writeInt32(cookie);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                int _hidl_out_modelHandle = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_retval, _hidl_out_modelHandle);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw
        public void loadPhraseSoundModel_2_1(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel soundModel, android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback callback, int cookie, android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadPhraseSoundModel_2_1Callback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.kInterfaceName);
            soundModel.writeToParcel(_hidl_request);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            _hidl_request.writeInt32(cookie);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                int _hidl_out_modelHandle = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_retval, _hidl_out_modelHandle);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw
        public int startRecognition_2_1(int modelHandle, android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig config, android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback callback, int cookie) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.kInterfaceName);
            _hidl_request.writeInt32(modelHandle);
            config.writeToParcel(_hidl_request);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            _hidl_request.writeInt32(cookie);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_retval = _hidl_reply.readInt32();
                return _hidl_out_retval;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements android.hardware.soundtrigger.V2_1.ISoundTriggerHw {
        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.kInterfaceName, android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return android.hardware.soundtrigger.V2_1.ISoundTriggerHw.kInterfaceName;
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{-76, -11, 7, -76, -36, -101, 92, -43, -16, -28, 68, 89, 38, -84, -73, -39, 69, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_ENDPOINT, -82, 96, -36, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_ENDPOINT_COMPANION, 123, 57, 81, 20, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_REPORT, com.android.server.usb.descriptors.UsbASFormat.EXT_FORMAT_TYPE_III, 99, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_REPORT, 7, -74}, new byte[]{91, -17, -64, 25, -53, -23, 73, 83, 102, 30, 44, -37, -107, -29, -49, 100, -11, -27, 101, -62, -108, 3, -31, -62, -38, -20, -62, -66, 68, -32, -91, 92}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw, android.hardware.soundtrigger.V2_0.ISoundTriggerHw, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (android.hardware.soundtrigger.V2_1.ISoundTriggerHw.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
                    getProperties(new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.getPropertiesCallback() { // from class: android.hardware.soundtrigger.V2_1.ISoundTriggerHw.Stub.1
                        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw.getPropertiesCallback
                        public void onValues(int retval, android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties properties) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(retval);
                            properties.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 2:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
                    android.hardware.soundtrigger.V2_0.ISoundTriggerHw.SoundModel soundModel = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.SoundModel();
                    soundModel.readFromParcel(_hidl_request);
                    android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback callback = android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.asInterface(_hidl_request.readStrongBinder());
                    int cookie = _hidl_request.readInt32();
                    loadSoundModel(soundModel, callback, cookie, new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadSoundModelCallback() { // from class: android.hardware.soundtrigger.V2_1.ISoundTriggerHw.Stub.2
                        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadSoundModelCallback
                        public void onValues(int retval, int modelHandle) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(retval);
                            _hidl_reply.writeInt32(modelHandle);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 3:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
                    android.hardware.soundtrigger.V2_0.ISoundTriggerHw.PhraseSoundModel soundModel2 = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.PhraseSoundModel();
                    soundModel2.readFromParcel(_hidl_request);
                    android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback callback2 = android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.asInterface(_hidl_request.readStrongBinder());
                    int cookie2 = _hidl_request.readInt32();
                    loadPhraseSoundModel(soundModel2, callback2, cookie2, new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadPhraseSoundModelCallback() { // from class: android.hardware.soundtrigger.V2_1.ISoundTriggerHw.Stub.3
                        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHw.loadPhraseSoundModelCallback
                        public void onValues(int retval, int modelHandle) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(retval);
                            _hidl_reply.writeInt32(modelHandle);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 4:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
                    int modelHandle = _hidl_request.readInt32();
                    int _hidl_out_retval = unloadSoundModel(modelHandle);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_retval);
                    _hidl_reply.send();
                    return;
                case 5:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
                    int modelHandle2 = _hidl_request.readInt32();
                    android.hardware.soundtrigger.V2_0.ISoundTriggerHw.RecognitionConfig config = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.RecognitionConfig();
                    config.readFromParcel(_hidl_request);
                    android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback callback3 = android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.asInterface(_hidl_request.readStrongBinder());
                    int cookie3 = _hidl_request.readInt32();
                    int _hidl_out_retval2 = startRecognition(modelHandle2, config, callback3, cookie3);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_retval2);
                    _hidl_reply.send();
                    return;
                case 6:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
                    int modelHandle3 = _hidl_request.readInt32();
                    int _hidl_out_retval3 = stopRecognition(modelHandle3);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_retval3);
                    _hidl_reply.send();
                    return;
                case 7:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.kInterfaceName);
                    int _hidl_out_retval4 = stopAllRecognitions();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_retval4);
                    _hidl_reply.send();
                    return;
                case 8:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.kInterfaceName);
                    android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel soundModel3 = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel();
                    soundModel3.readFromParcel(_hidl_request);
                    android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback callback4 = android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.asInterface(_hidl_request.readStrongBinder());
                    int cookie4 = _hidl_request.readInt32();
                    loadSoundModel_2_1(soundModel3, callback4, cookie4, new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadSoundModel_2_1Callback() { // from class: android.hardware.soundtrigger.V2_1.ISoundTriggerHw.Stub.4
                        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadSoundModel_2_1Callback
                        public void onValues(int retval, int modelHandle4) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(retval);
                            _hidl_reply.writeInt32(modelHandle4);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 9:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.kInterfaceName);
                    android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel soundModel4 = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel();
                    soundModel4.readFromParcel(_hidl_request);
                    android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback callback5 = android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.asInterface(_hidl_request.readStrongBinder());
                    int cookie5 = _hidl_request.readInt32();
                    loadPhraseSoundModel_2_1(soundModel4, callback5, cookie5, new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadPhraseSoundModel_2_1Callback() { // from class: android.hardware.soundtrigger.V2_1.ISoundTriggerHw.Stub.5
                        @Override // android.hardware.soundtrigger.V2_1.ISoundTriggerHw.loadPhraseSoundModel_2_1Callback
                        public void onValues(int retval, int modelHandle4) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(retval);
                            _hidl_reply.writeInt32(modelHandle4);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 10:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.kInterfaceName);
                    int modelHandle4 = _hidl_request.readInt32();
                    android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig config2 = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig();
                    config2.readFromParcel(_hidl_request);
                    android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback callback6 = android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.asInterface(_hidl_request.readStrongBinder());
                    int cookie6 = _hidl_request.readInt32();
                    int _hidl_out_retval5 = startRecognition_2_1(modelHandle4, config2, callback6, cookie6);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_retval5);
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
