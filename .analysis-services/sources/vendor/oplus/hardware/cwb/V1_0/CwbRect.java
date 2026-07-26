package vendor.oplus.hardware.cwb.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class CwbRect {
    public int left = 0;
    public int top = 0;
    public int right = 0;
    public int bottom = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.oplus.hardware.cwb.V1_0.CwbRect.class) {
            return false;
        }
        vendor.oplus.hardware.cwb.V1_0.CwbRect other = (vendor.oplus.hardware.cwb.V1_0.CwbRect) otherObject;
        if (this.left == other.left && this.top == other.top && this.right == other.right && this.bottom == other.bottom) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.left))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.top))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.right))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.bottom))));
    }

    public final java.lang.String toString() {
        return "{.left = " + this.left + ", .top = " + this.top + ", .right = " + this.right + ", .bottom = " + this.bottom + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(16L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.oplus.hardware.cwb.V1_0.CwbRect> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.oplus.hardware.cwb.V1_0.CwbRect> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 16, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.oplus.hardware.cwb.V1_0.CwbRect _hidl_vec_element = new vendor.oplus.hardware.cwb.V1_0.CwbRect();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 16);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.left = _hidl_blob.getInt32(0 + _hidl_offset);
        this.top = _hidl_blob.getInt32(4 + _hidl_offset);
        this.right = _hidl_blob.getInt32(8 + _hidl_offset);
        this.bottom = _hidl_blob.getInt32(12 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.oplus.hardware.cwb.V1_0.CwbRect> _hidl_vec) {
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
        _hidl_blob.putInt32(0 + _hidl_offset, this.left);
        _hidl_blob.putInt32(4 + _hidl_offset, this.top);
        _hidl_blob.putInt32(8 + _hidl_offset, this.right);
        _hidl_blob.putInt32(12 + _hidl_offset, this.bottom);
    }
}
