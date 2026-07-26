package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioGain {
    public int mode = 0;
    public int channelMask = 0;
    public int minValue = 0;
    public int maxValue = 0;
    public int defaultValue = 0;
    public int stepValue = 0;
    public int minRampMs = 0;
    public int maxRampMs = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.audio.common.V2_0.AudioGain.class) {
            return false;
        }
        android.hardware.audio.common.V2_0.AudioGain other = (android.hardware.audio.common.V2_0.AudioGain) otherObject;
        if (this.mode == other.mode && this.channelMask == other.channelMask && this.minValue == other.minValue && this.maxValue == other.maxValue && this.defaultValue == other.defaultValue && this.stepValue == other.stepValue && this.minRampMs == other.minRampMs && this.maxRampMs == other.maxRampMs) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.mode))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.channelMask))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.minValue))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.maxValue))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.defaultValue))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.stepValue))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.minRampMs))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.maxRampMs))));
    }

    public final java.lang.String toString() {
        return "{.mode = " + android.hardware.audio.common.V2_0.AudioGainMode.toString(this.mode) + ", .channelMask = " + android.hardware.audio.common.V2_0.AudioChannelMask.toString(this.channelMask) + ", .minValue = " + this.minValue + ", .maxValue = " + this.maxValue + ", .defaultValue = " + this.defaultValue + ", .stepValue = " + this.stepValue + ", .minRampMs = " + this.minRampMs + ", .maxRampMs = " + this.maxRampMs + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(32L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.audio.common.V2_0.AudioGain> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.audio.common.V2_0.AudioGain> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.audio.common.V2_0.AudioGain _hidl_vec_element = new android.hardware.audio.common.V2_0.AudioGain();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 32);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.mode = _hidl_blob.getInt32(0 + _hidl_offset);
        this.channelMask = _hidl_blob.getInt32(4 + _hidl_offset);
        this.minValue = _hidl_blob.getInt32(8 + _hidl_offset);
        this.maxValue = _hidl_blob.getInt32(12 + _hidl_offset);
        this.defaultValue = _hidl_blob.getInt32(16 + _hidl_offset);
        this.stepValue = _hidl_blob.getInt32(20 + _hidl_offset);
        this.minRampMs = _hidl_blob.getInt32(24 + _hidl_offset);
        this.maxRampMs = _hidl_blob.getInt32(28 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(32);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.audio.common.V2_0.AudioGain> _hidl_vec) {
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
        _hidl_blob.putInt32(0 + _hidl_offset, this.mode);
        _hidl_blob.putInt32(4 + _hidl_offset, this.channelMask);
        _hidl_blob.putInt32(8 + _hidl_offset, this.minValue);
        _hidl_blob.putInt32(12 + _hidl_offset, this.maxValue);
        _hidl_blob.putInt32(16 + _hidl_offset, this.defaultValue);
        _hidl_blob.putInt32(20 + _hidl_offset, this.stepValue);
        _hidl_blob.putInt32(24 + _hidl_offset, this.minRampMs);
        _hidl_blob.putInt32(28 + _hidl_offset, this.maxRampMs);
    }
}
