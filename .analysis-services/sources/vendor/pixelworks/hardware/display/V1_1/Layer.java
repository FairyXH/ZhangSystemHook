package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class Layer {
    public vendor.pixelworks.hardware.display.V1_1.LayerBuffer inputBuffer = new vendor.pixelworks.hardware.display.V1_1.LayerBuffer();
    public int composition = 0;
    public vendor.pixelworks.hardware.display.V1_1.LayerRect dstRect = new vendor.pixelworks.hardware.display.V1_1.LayerRect();
    public vendor.pixelworks.hardware.display.V1_1.LayerTransform transform = new vendor.pixelworks.hardware.display.V1_1.LayerTransform();
    public byte planeAlpha = 0;
    public int layerFlags = 0;
    public java.util.ArrayList<java.lang.Integer> reserved = new java.util.ArrayList<>();

    public final java.lang.String toString() {
        return "{.inputBuffer = " + this.inputBuffer + ", .composition = " + this.composition + ", .dstRect = " + this.dstRect + ", .transform = " + this.transform + ", .planeAlpha = " + ((int) this.planeAlpha) + ", .layerFlags = " + this.layerFlags + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(216L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.Layer> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.Layer> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_1.Layer _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_1.Layer();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.inputBuffer.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        this.composition = _hidl_blob.getInt32(_hidl_offset + 160);
        this.dstRect.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 164);
        this.transform.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 180);
        this.planeAlpha = _hidl_blob.getInt8(_hidl_offset + 188);
        this.layerFlags = _hidl_blob.getInt32(_hidl_offset + 192);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 200 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), _hidl_offset + 200 + 0, true);
        this.reserved.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            int _hidl_vec_element = childBlob.getInt32(_hidl_index_0 * 4);
            this.reserved.add(java.lang.Integer.valueOf(_hidl_vec_element));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.Layer> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.inputBuffer.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 0);
        _hidl_blob.putInt32(160 + _hidl_offset, this.composition);
        this.dstRect.writeEmbeddedToBlob(_hidl_blob, 164 + _hidl_offset);
        this.transform.writeEmbeddedToBlob(_hidl_blob, 180 + _hidl_offset);
        _hidl_blob.putInt8(188 + _hidl_offset, this.planeAlpha);
        _hidl_blob.putInt32(192 + _hidl_offset, this.layerFlags);
        int _hidl_vec_size = this.reserved.size();
        _hidl_blob.putInt32(_hidl_offset + 200 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 200 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt32(_hidl_index_0 * 4, this.reserved.get(_hidl_index_0).intValue());
        }
        _hidl_blob.putBlob(200 + _hidl_offset + 0, childBlob);
    }
}
