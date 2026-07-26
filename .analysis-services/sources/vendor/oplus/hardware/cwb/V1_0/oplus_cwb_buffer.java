package vendor.oplus.hardware.cwb.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class oplus_cwb_buffer {
    public android.os.NativeHandle bufferHandler = new android.os.NativeHandle();
    public int bufferSize = 0;

    public final java.lang.String toString() {
        return "{.bufferHandler = " + this.bufferHandler + ", .bufferSize = " + this.bufferSize + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.oplus.hardware.cwb.V1_0.oplus_cwb_buffer> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.oplus.hardware.cwb.V1_0.oplus_cwb_buffer> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.oplus.hardware.cwb.V1_0.oplus_cwb_buffer _hidl_vec_element = new vendor.oplus.hardware.cwb.V1_0.oplus_cwb_buffer();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.bufferHandler = parcel.readEmbeddedNativeHandle(_hidl_blob.handle(), _hidl_offset + 0 + 0);
        this.bufferSize = _hidl_blob.getInt32(16 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.oplus.hardware.cwb.V1_0.oplus_cwb_buffer> _hidl_vec) {
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
        _hidl_blob.putNativeHandle(0 + _hidl_offset, this.bufferHandler);
        _hidl_blob.putInt32(16 + _hidl_offset, this.bufferSize);
    }
}
