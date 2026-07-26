package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioOffloadInfo {
    public int sampleRateHz = 0;
    public int channelMask = 0;
    public int format = 0;
    public int streamType = 0;
    public int bitRatePerSecond = 0;
    public long durationMicroseconds = 0;
    public boolean hasVideo = false;
    public boolean isStreaming = false;
    public int bitWidth = 0;
    public int bufferSize = 0;
    public int usage = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.audio.common.V2_0.AudioOffloadInfo.class) {
            return false;
        }
        android.hardware.audio.common.V2_0.AudioOffloadInfo other = (android.hardware.audio.common.V2_0.AudioOffloadInfo) otherObject;
        if (this.sampleRateHz == other.sampleRateHz && this.channelMask == other.channelMask && this.format == other.format && this.streamType == other.streamType && this.bitRatePerSecond == other.bitRatePerSecond && this.durationMicroseconds == other.durationMicroseconds && this.hasVideo == other.hasVideo && this.isStreaming == other.isStreaming && this.bitWidth == other.bitWidth && this.bufferSize == other.bufferSize && this.usage == other.usage) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.sampleRateHz))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.channelMask))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.format))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.streamType))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.bitRatePerSecond))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.durationMicroseconds))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.hasVideo))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.isStreaming))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.bitWidth))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.bufferSize))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.usage))));
    }

    public final java.lang.String toString() {
        return "{.sampleRateHz = " + this.sampleRateHz + ", .channelMask = " + android.hardware.audio.common.V2_0.AudioChannelMask.toString(this.channelMask) + ", .format = " + android.hardware.audio.common.V2_0.AudioFormat.toString(this.format) + ", .streamType = " + android.hardware.audio.common.V2_0.AudioStreamType.toString(this.streamType) + ", .bitRatePerSecond = " + this.bitRatePerSecond + ", .durationMicroseconds = " + this.durationMicroseconds + ", .hasVideo = " + this.hasVideo + ", .isStreaming = " + this.isStreaming + ", .bitWidth = " + this.bitWidth + ", .bufferSize = " + this.bufferSize + ", .usage = " + android.hardware.audio.common.V2_0.AudioUsage.toString(this.usage) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(48L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.audio.common.V2_0.AudioOffloadInfo> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.audio.common.V2_0.AudioOffloadInfo> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 48, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.audio.common.V2_0.AudioOffloadInfo _hidl_vec_element = new android.hardware.audio.common.V2_0.AudioOffloadInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 48);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.sampleRateHz = _hidl_blob.getInt32(0 + _hidl_offset);
        this.channelMask = _hidl_blob.getInt32(4 + _hidl_offset);
        this.format = _hidl_blob.getInt32(8 + _hidl_offset);
        this.streamType = _hidl_blob.getInt32(12 + _hidl_offset);
        this.bitRatePerSecond = _hidl_blob.getInt32(16 + _hidl_offset);
        this.durationMicroseconds = _hidl_blob.getInt64(24 + _hidl_offset);
        this.hasVideo = _hidl_blob.getBool(32 + _hidl_offset);
        this.isStreaming = _hidl_blob.getBool(33 + _hidl_offset);
        this.bitWidth = _hidl_blob.getInt32(36 + _hidl_offset);
        this.bufferSize = _hidl_blob.getInt32(40 + _hidl_offset);
        this.usage = _hidl_blob.getInt32(44 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(48);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.audio.common.V2_0.AudioOffloadInfo> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 48);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 48);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.sampleRateHz);
        _hidl_blob.putInt32(4 + _hidl_offset, this.channelMask);
        _hidl_blob.putInt32(8 + _hidl_offset, this.format);
        _hidl_blob.putInt32(12 + _hidl_offset, this.streamType);
        _hidl_blob.putInt32(16 + _hidl_offset, this.bitRatePerSecond);
        _hidl_blob.putInt64(24 + _hidl_offset, this.durationMicroseconds);
        _hidl_blob.putBool(32 + _hidl_offset, this.hasVideo);
        _hidl_blob.putBool(33 + _hidl_offset, this.isStreaming);
        _hidl_blob.putInt32(36 + _hidl_offset, this.bitWidth);
        _hidl_blob.putInt32(40 + _hidl_offset, this.bufferSize);
        _hidl_blob.putInt32(44 + _hidl_offset, this.usage);
    }
}
