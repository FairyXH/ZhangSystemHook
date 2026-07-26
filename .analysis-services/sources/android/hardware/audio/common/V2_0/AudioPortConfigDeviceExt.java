package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioPortConfigDeviceExt {
    public int hwModule = 0;
    public int type = 0;
    public byte[] address = new byte[32];

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.audio.common.V2_0.AudioPortConfigDeviceExt.class) {
            return false;
        }
        android.hardware.audio.common.V2_0.AudioPortConfigDeviceExt other = (android.hardware.audio.common.V2_0.AudioPortConfigDeviceExt) otherObject;
        if (this.hwModule == other.hwModule && this.type == other.type && android.os.HidlSupport.deepEquals(this.address, other.address)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.hwModule))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.type))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.address)));
    }

    public final java.lang.String toString() {
        return "{.hwModule = " + this.hwModule + ", .type = " + android.hardware.audio.common.V2_0.AudioDevice.toString(this.type) + ", .address = " + java.util.Arrays.toString(this.address) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(40L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.audio.common.V2_0.AudioPortConfigDeviceExt> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.audio.common.V2_0.AudioPortConfigDeviceExt> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 40, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.audio.common.V2_0.AudioPortConfigDeviceExt _hidl_vec_element = new android.hardware.audio.common.V2_0.AudioPortConfigDeviceExt();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 40);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.hwModule = _hidl_blob.getInt32(0 + _hidl_offset);
        this.type = _hidl_blob.getInt32(4 + _hidl_offset);
        long _hidl_array_offset_0 = 8 + _hidl_offset;
        _hidl_blob.copyToInt8Array(_hidl_array_offset_0, this.address, 32);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(40);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.audio.common.V2_0.AudioPortConfigDeviceExt> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 40);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 40);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.hwModule);
        _hidl_blob.putInt32(4 + _hidl_offset, this.type);
        long _hidl_array_offset_0 = 8 + _hidl_offset;
        byte[] _hidl_array_item_0 = this.address;
        if (_hidl_array_item_0 == null || _hidl_array_item_0.length != 32) {
            throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
        }
        _hidl_blob.putInt8Array(_hidl_array_offset_0, _hidl_array_item_0);
    }
}
