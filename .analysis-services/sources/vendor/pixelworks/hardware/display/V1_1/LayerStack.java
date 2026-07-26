package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class LayerStack {
    public java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.Layer> layers = new java.util.ArrayList<>();
    public int layerStackFlags = 0;
    public java.util.ArrayList<java.lang.Integer> reserved = new java.util.ArrayList<>();

    public final java.lang.String toString() {
        return "{.layers = " + this.layers + ", .layerStackFlags = " + this.layerStackFlags + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(40L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.LayerStack> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.LayerStack> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 40, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_1.LayerStack _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_1.LayerStack();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 40);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 0 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY, _hidl_blob.handle(), _hidl_offset + 0 + 0, true);
        this.layers.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_1.Layer _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_1.Layer();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY);
            this.layers.add(_hidl_vec_element);
        }
        this.layerStackFlags = _hidl_blob.getInt32(_hidl_offset + 16);
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 24 + 8);
        android.os.HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 4, _hidl_blob.handle(), _hidl_offset + 24 + 0, true);
        this.reserved.clear();
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            this.reserved.add(java.lang.Integer.valueOf(childBlob2.getInt32(_hidl_index_02 * 4)));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(40);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.LayerStack> _hidl_vec) {
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
        int _hidl_vec_size = this.layers.size();
        _hidl_blob.putInt32(_hidl_offset + 0 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 0 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.layers.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY);
        }
        _hidl_blob.putBlob(_hidl_offset + 0 + 0, childBlob);
        _hidl_blob.putInt32(_hidl_offset + 16, this.layerStackFlags);
        int _hidl_vec_size2 = this.reserved.size();
        _hidl_blob.putInt32(_hidl_offset + 24 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 24 + 12, false);
        android.os.HwBlob childBlob2 = new android.os.HwBlob(_hidl_vec_size2 * 4);
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            childBlob2.putInt32(_hidl_index_02 * 4, this.reserved.get(_hidl_index_02).intValue());
        }
        _hidl_blob.putBlob(_hidl_offset + 24 + 0, childBlob2);
    }
}
