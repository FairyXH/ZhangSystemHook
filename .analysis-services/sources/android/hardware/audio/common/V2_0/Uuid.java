package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class Uuid {
    public int timeLow = 0;
    public short timeMid = 0;
    public short versionAndTimeHigh = 0;
    public short variantAndClockSeqHigh = 0;
    public byte[] node = new byte[6];

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.audio.common.V2_0.Uuid.class) {
            return false;
        }
        android.hardware.audio.common.V2_0.Uuid other = (android.hardware.audio.common.V2_0.Uuid) otherObject;
        if (this.timeLow == other.timeLow && this.timeMid == other.timeMid && this.versionAndTimeHigh == other.versionAndTimeHigh && this.variantAndClockSeqHigh == other.variantAndClockSeqHigh && android.os.HidlSupport.deepEquals(this.node, other.node)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.timeLow))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Short.valueOf(this.timeMid))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Short.valueOf(this.versionAndTimeHigh))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Short.valueOf(this.variantAndClockSeqHigh))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.node)));
    }

    public final java.lang.String toString() {
        return "{.timeLow = " + this.timeLow + ", .timeMid = " + ((int) this.timeMid) + ", .versionAndTimeHigh = " + ((int) this.versionAndTimeHigh) + ", .variantAndClockSeqHigh = " + ((int) this.variantAndClockSeqHigh) + ", .node = " + java.util.Arrays.toString(this.node) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(16L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.audio.common.V2_0.Uuid> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.audio.common.V2_0.Uuid> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 16, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.audio.common.V2_0.Uuid _hidl_vec_element = new android.hardware.audio.common.V2_0.Uuid();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 16);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.timeLow = _hidl_blob.getInt32(0 + _hidl_offset);
        this.timeMid = _hidl_blob.getInt16(4 + _hidl_offset);
        this.versionAndTimeHigh = _hidl_blob.getInt16(6 + _hidl_offset);
        this.variantAndClockSeqHigh = _hidl_blob.getInt16(8 + _hidl_offset);
        long _hidl_array_offset_0 = 10 + _hidl_offset;
        _hidl_blob.copyToInt8Array(_hidl_array_offset_0, this.node, 6);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.audio.common.V2_0.Uuid> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 16);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 16);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.timeLow);
        _hidl_blob.putInt16(4 + _hidl_offset, this.timeMid);
        _hidl_blob.putInt16(6 + _hidl_offset, this.versionAndTimeHigh);
        _hidl_blob.putInt16(8 + _hidl_offset, this.variantAndClockSeqHigh);
        long _hidl_array_offset_0 = 10 + _hidl_offset;
        byte[] _hidl_array_item_0 = this.node;
        if (_hidl_array_item_0 == null || _hidl_array_item_0.length != 6) {
            throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
        }
        _hidl_blob.putInt8Array(_hidl_array_offset_0, _hidl_array_item_0);
    }
}
