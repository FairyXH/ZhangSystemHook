package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioGainConfig {
    public int index = 0;
    public int mode = 0;
    public int channelMask = 0;
    public int[] values = new int[32];
    public int rampDurationMs = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.audio.common.V2_0.AudioGainConfig.class) {
            return false;
        }
        android.hardware.audio.common.V2_0.AudioGainConfig other = (android.hardware.audio.common.V2_0.AudioGainConfig) otherObject;
        if (this.index == other.index && this.mode == other.mode && this.channelMask == other.channelMask && android.os.HidlSupport.deepEquals(this.values, other.values) && this.rampDurationMs == other.rampDurationMs) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.index))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.mode))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.channelMask))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.values)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.rampDurationMs))));
    }

    public final java.lang.String toString() {
        return "{.index = " + this.index + ", .mode = " + android.hardware.audio.common.V2_0.AudioGainMode.toString(this.mode) + ", .channelMask = " + android.hardware.audio.common.V2_0.AudioChannelMask.toString(this.channelMask) + ", .values = " + java.util.Arrays.toString(this.values) + ", .rampDurationMs = " + this.rampDurationMs + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(144L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.audio.common.V2_0.AudioGainConfig> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.audio.common.V2_0.AudioGainConfig> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 144, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.audio.common.V2_0.AudioGainConfig _hidl_vec_element = new android.hardware.audio.common.V2_0.AudioGainConfig();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 144);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.index = _hidl_blob.getInt32(0 + _hidl_offset);
        this.mode = _hidl_blob.getInt32(4 + _hidl_offset);
        this.channelMask = _hidl_blob.getInt32(8 + _hidl_offset);
        long _hidl_array_offset_0 = 12 + _hidl_offset;
        _hidl_blob.copyToInt32Array(_hidl_array_offset_0, this.values, 32);
        this.rampDurationMs = _hidl_blob.getInt32(140 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(144);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.audio.common.V2_0.AudioGainConfig> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 144);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 144);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.index);
        _hidl_blob.putInt32(4 + _hidl_offset, this.mode);
        _hidl_blob.putInt32(8 + _hidl_offset, this.channelMask);
        long _hidl_array_offset_0 = 12 + _hidl_offset;
        int[] _hidl_array_item_0 = this.values;
        if (_hidl_array_item_0 == null || _hidl_array_item_0.length != 32) {
            throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
        }
        _hidl_blob.putInt32Array(_hidl_array_offset_0, _hidl_array_item_0);
        _hidl_blob.putInt32(140 + _hidl_offset, this.rampDurationMs);
    }
}
