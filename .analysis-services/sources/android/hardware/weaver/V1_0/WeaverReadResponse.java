package android.hardware.weaver.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class WeaverReadResponse {
    public int timeout = 0;
    public java.util.ArrayList<java.lang.Byte> value = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.weaver.V1_0.WeaverReadResponse.class) {
            return false;
        }
        android.hardware.weaver.V1_0.WeaverReadResponse other = (android.hardware.weaver.V1_0.WeaverReadResponse) otherObject;
        if (this.timeout == other.timeout && android.os.HidlSupport.deepEquals(this.value, other.value)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.timeout))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.value)));
    }

    public final java.lang.String toString() {
        return "{.timeout = " + this.timeout + ", .value = " + this.value + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.weaver.V1_0.WeaverReadResponse> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.weaver.V1_0.WeaverReadResponse> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.weaver.V1_0.WeaverReadResponse _hidl_vec_element = new android.hardware.weaver.V1_0.WeaverReadResponse();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.timeout = _hidl_blob.getInt32(_hidl_offset + 0);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 8 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 1, _hidl_blob.handle(), _hidl_offset + 8 + 0, true);
        this.value.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            byte _hidl_vec_element = childBlob.getInt8(_hidl_index_0 * 1);
            this.value.add(java.lang.Byte.valueOf(_hidl_vec_element));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.weaver.V1_0.WeaverReadResponse> _hidl_vec) {
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
        _hidl_blob.putInt32(_hidl_offset + 0, this.timeout);
        int _hidl_vec_size = this.value.size();
        _hidl_blob.putInt32(_hidl_offset + 8 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 8 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 1);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt8(_hidl_index_0 * 1, this.value.get(_hidl_index_0).byteValue());
        }
        _hidl_blob.putBlob(8 + _hidl_offset + 0, childBlob);
    }
}
