package vendor.pixelworks.hardware.display.V1_2;

/* JADX INFO: loaded from: classes4.dex */
public final class DisplayConfigVariableInfo_1_2 {
    public vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo base = new vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo();
    public int config = 0;
    public java.util.ArrayList<java.lang.Integer> reserved = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2.class) {
            return false;
        }
        vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2 other = (vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2) otherObject;
        if (android.os.HidlSupport.deepEquals(this.base, other.base) && this.config == other.config && android.os.HidlSupport.deepEquals(this.reserved, other.reserved)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.base)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.config))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.reserved)));
    }

    public final java.lang.String toString() {
        return "{.base = " + this.base + ", .config = " + this.config + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(72L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 72, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2 _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 72);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.base.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        this.config = _hidl_blob.getInt32(_hidl_offset + 48);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 56 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), _hidl_offset + 56 + 0, true);
        this.reserved.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            int _hidl_vec_element = childBlob.getInt32(_hidl_index_0 * 4);
            this.reserved.add(java.lang.Integer.valueOf(_hidl_vec_element));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(72);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2> _hidl_vec) {
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
        this.base.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 0);
        _hidl_blob.putInt32(48 + _hidl_offset, this.config);
        int _hidl_vec_size = this.reserved.size();
        _hidl_blob.putInt32(_hidl_offset + 56 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 56 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt32(_hidl_index_0 * 4, this.reserved.get(_hidl_index_0).intValue());
        }
        _hidl_blob.putBlob(56 + _hidl_offset + 0, childBlob);
    }
}
