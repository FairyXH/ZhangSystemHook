package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioConfig {
    public int sampleRateHz = 0;
    public int channelMask = 0;
    public int format = 0;
    public android.hardware.audio.common.V2_0.AudioOffloadInfo offloadInfo = new android.hardware.audio.common.V2_0.AudioOffloadInfo();
    public long frameCount = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.audio.common.V2_0.AudioConfig.class) {
            return false;
        }
        android.hardware.audio.common.V2_0.AudioConfig other = (android.hardware.audio.common.V2_0.AudioConfig) otherObject;
        if (this.sampleRateHz == other.sampleRateHz && this.channelMask == other.channelMask && this.format == other.format && android.os.HidlSupport.deepEquals(this.offloadInfo, other.offloadInfo) && this.frameCount == other.frameCount) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.sampleRateHz))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.channelMask))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.format))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.offloadInfo)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.frameCount))));
    }

    public final java.lang.String toString() {
        return "{.sampleRateHz = " + this.sampleRateHz + ", .channelMask = " + android.hardware.audio.common.V2_0.AudioChannelMask.toString(this.channelMask) + ", .format = " + android.hardware.audio.common.V2_0.AudioFormat.toString(this.format) + ", .offloadInfo = " + this.offloadInfo + ", .frameCount = " + this.frameCount + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(72L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.audio.common.V2_0.AudioConfig> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.audio.common.V2_0.AudioConfig> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 72, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.audio.common.V2_0.AudioConfig _hidl_vec_element = new android.hardware.audio.common.V2_0.AudioConfig();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 72);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.sampleRateHz = _hidl_blob.getInt32(0 + _hidl_offset);
        this.channelMask = _hidl_blob.getInt32(4 + _hidl_offset);
        this.format = _hidl_blob.getInt32(8 + _hidl_offset);
        this.offloadInfo.readEmbeddedFromParcel(parcel, _hidl_blob, 16 + _hidl_offset);
        this.frameCount = _hidl_blob.getInt64(64 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(72);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.audio.common.V2_0.AudioConfig> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 72);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 72);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.sampleRateHz);
        _hidl_blob.putInt32(4 + _hidl_offset, this.channelMask);
        _hidl_blob.putInt32(8 + _hidl_offset, this.format);
        this.offloadInfo.writeEmbeddedToBlob(_hidl_blob, 16 + _hidl_offset);
        _hidl_blob.putInt64(64 + _hidl_offset, this.frameCount);
    }
}
