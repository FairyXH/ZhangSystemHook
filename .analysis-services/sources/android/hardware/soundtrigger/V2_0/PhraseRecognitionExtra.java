package android.hardware.soundtrigger.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class PhraseRecognitionExtra {
    public int id = 0;
    public int recognitionModes = 0;
    public int confidenceLevel = 0;
    public java.util.ArrayList<android.hardware.soundtrigger.V2_0.ConfidenceLevel> levels = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra.class) {
            return false;
        }
        android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra other = (android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra) otherObject;
        if (this.id == other.id && this.recognitionModes == other.recognitionModes && this.confidenceLevel == other.confidenceLevel && android.os.HidlSupport.deepEquals(this.levels, other.levels)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.id))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.recognitionModes))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.confidenceLevel))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.levels)));
    }

    public final java.lang.String toString() {
        return "{.id = " + this.id + ", .recognitionModes = " + this.recognitionModes + ", .confidenceLevel = " + this.confidenceLevel + ", .levels = " + this.levels + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(32L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra _hidl_vec_element = new android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 32);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.id = _hidl_blob.getInt32(_hidl_offset + 0);
        this.recognitionModes = _hidl_blob.getInt32(_hidl_offset + 4);
        this.confidenceLevel = _hidl_blob.getInt32(_hidl_offset + 8);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 16 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 8, _hidl_blob.handle(), _hidl_offset + 16 + 0, true);
        this.levels.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.soundtrigger.V2_0.ConfidenceLevel _hidl_vec_element = new android.hardware.soundtrigger.V2_0.ConfidenceLevel();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 8);
            this.levels.add(_hidl_vec_element);
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(32);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 32);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 32);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(_hidl_offset + 0, this.id);
        _hidl_blob.putInt32(4 + _hidl_offset, this.recognitionModes);
        _hidl_blob.putInt32(_hidl_offset + 8, this.confidenceLevel);
        int _hidl_vec_size = this.levels.size();
        _hidl_blob.putInt32(_hidl_offset + 16 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 16 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 8);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.levels.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 8);
        }
        _hidl_blob.putBlob(16 + _hidl_offset + 0, childBlob);
    }
}
