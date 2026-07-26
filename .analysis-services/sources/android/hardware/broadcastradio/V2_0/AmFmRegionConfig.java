package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AmFmRegionConfig {
    public byte fmDeemphasis;
    public byte fmRds;
    public java.util.ArrayList<android.hardware.broadcastradio.V2_0.AmFmBandRange> ranges = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.broadcastradio.V2_0.AmFmRegionConfig.class) {
            return false;
        }
        android.hardware.broadcastradio.V2_0.AmFmRegionConfig other = (android.hardware.broadcastradio.V2_0.AmFmRegionConfig) otherObject;
        if (android.os.HidlSupport.deepEquals(this.ranges, other.ranges) && android.os.HidlSupport.deepEquals(java.lang.Byte.valueOf(this.fmDeemphasis), java.lang.Byte.valueOf(other.fmDeemphasis)) && android.os.HidlSupport.deepEquals(java.lang.Byte.valueOf(this.fmRds), java.lang.Byte.valueOf(other.fmRds))) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.ranges)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Byte.valueOf(this.fmDeemphasis))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Byte.valueOf(this.fmRds))));
    }

    public final java.lang.String toString() {
        return "{.ranges = " + this.ranges + ", .fmDeemphasis = " + android.hardware.broadcastradio.V2_0.Deemphasis.dumpBitfield(this.fmDeemphasis) + ", .fmRds = " + android.hardware.broadcastradio.V2_0.Rds.dumpBitfield(this.fmRds) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.broadcastradio.V2_0.AmFmRegionConfig> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.broadcastradio.V2_0.AmFmRegionConfig> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.broadcastradio.V2_0.AmFmRegionConfig _hidl_vec_element = new android.hardware.broadcastradio.V2_0.AmFmRegionConfig();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 0 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 16, _hidl_blob.handle(), _hidl_offset + 0 + 0, true);
        this.ranges.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.broadcastradio.V2_0.AmFmBandRange _hidl_vec_element = new android.hardware.broadcastradio.V2_0.AmFmBandRange();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 16);
            this.ranges.add(_hidl_vec_element);
        }
        this.fmDeemphasis = _hidl_blob.getInt8(_hidl_offset + 16);
        this.fmRds = _hidl_blob.getInt8(_hidl_offset + 17);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.broadcastradio.V2_0.AmFmRegionConfig> _hidl_vec) {
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
        int _hidl_vec_size = this.ranges.size();
        _hidl_blob.putInt32(_hidl_offset + 0 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 0 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 16);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.ranges.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 16);
        }
        _hidl_blob.putBlob(_hidl_offset + 0 + 0, childBlob);
        _hidl_blob.putInt8(16 + _hidl_offset, this.fmDeemphasis);
        _hidl_blob.putInt8(17 + _hidl_offset, this.fmRds);
    }
}
