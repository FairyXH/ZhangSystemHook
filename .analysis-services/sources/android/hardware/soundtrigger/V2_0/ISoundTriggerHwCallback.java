package android.hardware.soundtrigger.V2_0;

/* JADX INFO: loaded from: classes.dex */
public interface ISoundTriggerHwCallback extends android.hidl.base.V1_0.IBase {
    public static final java.lang.String kInterfaceName = "android.hardware.soundtrigger@2.0::ISoundTriggerHwCallback";

    @Override // android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    void phraseRecognitionCallback(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent phraseRecognitionEvent, int i) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    void recognitionCallback(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent recognitionEvent, int i) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    void soundModelCallback(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent modelEvent, int i) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback)) {
            return (android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback) iface;
        }
        android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback proxy = new android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.Proxy(binder);
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

    static android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class RecognitionStatus {
        public static final int ABORT = 1;
        public static final int FAILURE = 2;
        public static final int SUCCESS = 0;

        public static final java.lang.String toString(int o) {
            if (o == 0) {
                return "SUCCESS";
            }
            if (o == 1) {
                return "ABORT";
            }
            if (o == 2) {
                return "FAILURE";
            }
            return "0x" + java.lang.Integer.toHexString(o);
        }

        public static final java.lang.String dumpBitfield(int o) {
            java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
            int flipped = 0;
            list.add("SUCCESS");
            if ((o & 1) == 1) {
                list.add("ABORT");
                flipped = 0 | 1;
            }
            if ((o & 2) == 2) {
                list.add("FAILURE");
                flipped |= 2;
            }
            if (o != flipped) {
                list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
            }
            return java.lang.String.join(" | ", list);
        }
    }

    public static final class SoundModelStatus {
        public static final int UPDATED = 0;

        public static final java.lang.String toString(int o) {
            if (o == 0) {
                return "UPDATED";
            }
            return "0x" + java.lang.Integer.toHexString(o);
        }

        public static final java.lang.String dumpBitfield(int o) {
            java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
            list.add("UPDATED");
            if (o != 0) {
                list.add("0x" + java.lang.Integer.toHexString((~0) & o));
            }
            return java.lang.String.join(" | ", list);
        }
    }

    public static final class RecognitionEvent {
        public int status = 0;
        public int type = 0;
        public int model = 0;
        public boolean captureAvailable = false;
        public int captureSession = 0;
        public int captureDelayMs = 0;
        public int capturePreambleMs = 0;
        public boolean triggerInData = false;
        public android.hardware.audio.common.V2_0.AudioConfig audioConfig = new android.hardware.audio.common.V2_0.AudioConfig();
        public java.util.ArrayList<java.lang.Byte> data = new java.util.ArrayList<>();

        public final boolean equals(java.lang.Object otherObject) {
            if (this == otherObject) {
                return true;
            }
            if (otherObject == null || otherObject.getClass() != android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent.class) {
                return false;
            }
            android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent other = (android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent) otherObject;
            if (this.status == other.status && this.type == other.type && this.model == other.model && this.captureAvailable == other.captureAvailable && this.captureSession == other.captureSession && this.captureDelayMs == other.captureDelayMs && this.capturePreambleMs == other.capturePreambleMs && this.triggerInData == other.triggerInData && android.os.HidlSupport.deepEquals(this.audioConfig, other.audioConfig) && android.os.HidlSupport.deepEquals(this.data, other.data)) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.status))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.type))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.model))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.captureAvailable))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.captureSession))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.captureDelayMs))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.capturePreambleMs))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.triggerInData))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.audioConfig)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.data)));
        }

        public final java.lang.String toString() {
            return "{.status = " + android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionStatus.toString(this.status) + ", .type = " + android.hardware.soundtrigger.V2_0.SoundModelType.toString(this.type) + ", .model = " + this.model + ", .captureAvailable = " + this.captureAvailable + ", .captureSession = " + this.captureSession + ", .captureDelayMs = " + this.captureDelayMs + ", .capturePreambleMs = " + this.capturePreambleMs + ", .triggerInData = " + this.triggerInData + ", .audioConfig = " + this.audioConfig + ", .data = " + this.data + "}";
        }

        public final void readFromParcel(android.os.HwParcel parcel) {
            android.os.HwBlob blob = parcel.readBuffer(120L);
            readEmbeddedFromParcel(parcel, blob, 0L);
        }

        public static final java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent> readVectorFromParcel(android.os.HwParcel parcel) {
            java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent> _hidl_vec = new java.util.ArrayList<>();
            android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
            int _hidl_vec_size = _hidl_blob.getInt32(8L);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 120, _hidl_blob.handle(), 0L, true);
            _hidl_vec.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent _hidl_vec_element = new android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 120);
                _hidl_vec.add(_hidl_vec_element);
            }
            return _hidl_vec;
        }

        public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.status = _hidl_blob.getInt32(_hidl_offset + 0);
            this.type = _hidl_blob.getInt32(_hidl_offset + 4);
            this.model = _hidl_blob.getInt32(_hidl_offset + 8);
            this.captureAvailable = _hidl_blob.getBool(_hidl_offset + 12);
            this.captureSession = _hidl_blob.getInt32(_hidl_offset + 16);
            this.captureDelayMs = _hidl_blob.getInt32(_hidl_offset + 20);
            this.capturePreambleMs = _hidl_blob.getInt32(_hidl_offset + 24);
            this.triggerInData = _hidl_blob.getBool(_hidl_offset + 28);
            this.audioConfig.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 32);
            int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 104 + 8);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 1, _hidl_blob.handle(), 0 + _hidl_offset + 104, true);
            this.data.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                byte _hidl_vec_element = childBlob.getInt8(_hidl_index_0 * 1);
                this.data.add(java.lang.Byte.valueOf(_hidl_vec_element));
            }
        }

        public final void writeToParcel(android.os.HwParcel parcel) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(120);
            writeEmbeddedToBlob(_hidl_blob, 0L);
            parcel.writeBuffer(_hidl_blob);
        }

        public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent> _hidl_vec) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
            int _hidl_vec_size = _hidl_vec.size();
            _hidl_blob.putInt32(8L, _hidl_vec_size);
            _hidl_blob.putBool(12L, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 120);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 120);
            }
            _hidl_blob.putBlob(0L, childBlob);
            parcel.writeBuffer(_hidl_blob);
        }

        public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
            _hidl_blob.putInt32(_hidl_offset + 0, this.status);
            _hidl_blob.putInt32(4 + _hidl_offset, this.type);
            _hidl_blob.putInt32(_hidl_offset + 8, this.model);
            _hidl_blob.putBool(_hidl_offset + 12, this.captureAvailable);
            _hidl_blob.putInt32(16 + _hidl_offset, this.captureSession);
            _hidl_blob.putInt32(20 + _hidl_offset, this.captureDelayMs);
            _hidl_blob.putInt32(24 + _hidl_offset, this.capturePreambleMs);
            _hidl_blob.putBool(28 + _hidl_offset, this.triggerInData);
            this.audioConfig.writeEmbeddedToBlob(_hidl_blob, 32 + _hidl_offset);
            int _hidl_vec_size = this.data.size();
            _hidl_blob.putInt32(_hidl_offset + 104 + 8, _hidl_vec_size);
            _hidl_blob.putBool(_hidl_offset + 104 + 12, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 1);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                childBlob.putInt8(_hidl_index_0 * 1, this.data.get(_hidl_index_0).byteValue());
            }
            _hidl_blob.putBlob(104 + _hidl_offset + 0, childBlob);
        }
    }

    public static final class PhraseRecognitionEvent {
        public android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent common = new android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent();
        public java.util.ArrayList<android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra> phraseExtras = new java.util.ArrayList<>();

        public final boolean equals(java.lang.Object otherObject) {
            if (this == otherObject) {
                return true;
            }
            if (otherObject == null || otherObject.getClass() != android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent.class) {
                return false;
            }
            android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent other = (android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent) otherObject;
            if (android.os.HidlSupport.deepEquals(this.common, other.common) && android.os.HidlSupport.deepEquals(this.phraseExtras, other.phraseExtras)) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.common)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.phraseExtras)));
        }

        public final java.lang.String toString() {
            return "{.common = " + this.common + ", .phraseExtras = " + this.phraseExtras + "}";
        }

        public final void readFromParcel(android.os.HwParcel parcel) {
            android.os.HwBlob blob = parcel.readBuffer(136L);
            readEmbeddedFromParcel(parcel, blob, 0L);
        }

        public static final java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent> readVectorFromParcel(android.os.HwParcel parcel) {
            java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent> _hidl_vec = new java.util.ArrayList<>();
            android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
            int _hidl_vec_size = _hidl_blob.getInt32(8L);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 136, _hidl_blob.handle(), 0L, true);
            _hidl_vec.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent _hidl_vec_element = new android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 136);
                _hidl_vec.add(_hidl_vec_element);
            }
            return _hidl_vec;
        }

        public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.common.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
            int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 120 + 8);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), _hidl_offset + 120 + 0, true);
            this.phraseExtras.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra _hidl_vec_element = new android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 32);
                this.phraseExtras.add(_hidl_vec_element);
            }
        }

        public final void writeToParcel(android.os.HwParcel parcel) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(136);
            writeEmbeddedToBlob(_hidl_blob, 0L);
            parcel.writeBuffer(_hidl_blob);
        }

        public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent> _hidl_vec) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
            int _hidl_vec_size = _hidl_vec.size();
            _hidl_blob.putInt32(8L, _hidl_vec_size);
            _hidl_blob.putBool(12L, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 136);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 136);
            }
            _hidl_blob.putBlob(0L, childBlob);
            parcel.writeBuffer(_hidl_blob);
        }

        public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.common.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 0);
            int _hidl_vec_size = this.phraseExtras.size();
            _hidl_blob.putInt32(_hidl_offset + 120 + 8, _hidl_vec_size);
            _hidl_blob.putBool(_hidl_offset + 120 + 12, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 32);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                this.phraseExtras.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 32);
            }
            _hidl_blob.putBlob(120 + _hidl_offset + 0, childBlob);
        }
    }

    public static final class ModelEvent {
        public int status = 0;
        public int model = 0;
        public java.util.ArrayList<java.lang.Byte> data = new java.util.ArrayList<>();

        public final boolean equals(java.lang.Object otherObject) {
            if (this == otherObject) {
                return true;
            }
            if (otherObject == null || otherObject.getClass() != android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent.class) {
                return false;
            }
            android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent other = (android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent) otherObject;
            if (this.status == other.status && this.model == other.model && android.os.HidlSupport.deepEquals(this.data, other.data)) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.status))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.model))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.data)));
        }

        public final java.lang.String toString() {
            return "{.status = " + android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.SoundModelStatus.toString(this.status) + ", .model = " + this.model + ", .data = " + this.data + "}";
        }

        public final void readFromParcel(android.os.HwParcel parcel) {
            android.os.HwBlob blob = parcel.readBuffer(24L);
            readEmbeddedFromParcel(parcel, blob, 0L);
        }

        public static final java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent> readVectorFromParcel(android.os.HwParcel parcel) {
            java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent> _hidl_vec = new java.util.ArrayList<>();
            android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
            int _hidl_vec_size = _hidl_blob.getInt32(8L);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
            _hidl_vec.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent _hidl_vec_element = new android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent();
                _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
                _hidl_vec.add(_hidl_vec_element);
            }
            return _hidl_vec;
        }

        public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
            this.status = _hidl_blob.getInt32(_hidl_offset + 0);
            this.model = _hidl_blob.getInt32(_hidl_offset + 4);
            int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 8 + 8);
            android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 1, _hidl_blob.handle(), _hidl_offset + 8 + 0, true);
            this.data.clear();
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                byte _hidl_vec_element = childBlob.getInt8(_hidl_index_0 * 1);
                this.data.add(java.lang.Byte.valueOf(_hidl_vec_element));
            }
        }

        public final void writeToParcel(android.os.HwParcel parcel) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(24);
            writeEmbeddedToBlob(_hidl_blob, 0L);
            parcel.writeBuffer(_hidl_blob);
        }

        public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent> _hidl_vec) {
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
            int _hidl_vec_size = _hidl_vec.size();
            _hidl_blob.putInt32(8L, _hidl_vec_size);
            _hidl_blob.putBool(12L, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 24);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 24);
            }
            _hidl_blob.putBlob(0L, childBlob);
            parcel.writeBuffer(_hidl_blob);
        }

        public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
            _hidl_blob.putInt32(_hidl_offset + 0, this.status);
            _hidl_blob.putInt32(4 + _hidl_offset, this.model);
            int _hidl_vec_size = this.data.size();
            _hidl_blob.putInt32(_hidl_offset + 8 + 8, _hidl_vec_size);
            _hidl_blob.putBool(_hidl_offset + 8 + 12, false);
            android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 1);
            for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                childBlob.putInt8(_hidl_index_0 * 1, this.data.get(_hidl_index_0).byteValue());
            }
            _hidl_blob.putBlob(8 + _hidl_offset + 0, childBlob);
        }
    }

    public static final class Proxy implements android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of android.hardware.soundtrigger@2.0::ISoundTriggerHwCallback]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback
        public void recognitionCallback(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent event, int cookie) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.kInterfaceName);
            event.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(cookie);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback
        public void phraseRecognitionCallback(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent event, int cookie) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.kInterfaceName);
            event.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(cookie);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback
        public void soundModelCallback(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent event, int cookie) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.kInterfaceName);
            event.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(cookie);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback {
        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.kInterfaceName;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{26, 110, 43, -46, -119, -14, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_HUB, 49, -59, 38, -78, 25, 22, -111, 15, 29, 76, 67, 107, 122, -53, -107, 86, -28, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, 61, -28, -50, -114, 108, -62, -28}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.kInterfaceName);
                    android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent event = new android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent();
                    event.readFromParcel(_hidl_request);
                    int cookie = _hidl_request.readInt32();
                    recognitionCallback(event, cookie);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 2:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.kInterfaceName);
                    android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent event2 = new android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent();
                    event2.readFromParcel(_hidl_request);
                    int cookie2 = _hidl_request.readInt32();
                    phraseRecognitionCallback(event2, cookie2);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 3:
                    _hidl_request.enforceInterface(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.kInterfaceName);
                    android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent event3 = new android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.ModelEvent();
                    event3.readFromParcel(_hidl_request);
                    int cookie3 = _hidl_request.readInt32();
                    soundModelCallback(event3, cookie3);
                    _hidl_reply.writeStatus(0);
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
