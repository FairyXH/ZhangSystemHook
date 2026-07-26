package vendor.pixelworks.hardware.display.V1_2;

/* JADX INFO: loaded from: classes4.dex */
public final class IrisFixedConfig_1_2 {
    public vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig base = new vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig();
    public int memcToPt = 0;
    public int clientCompRequest = 0;
    public int hdrRequest = 0;
    public int motionLayerIdUsing = 0;
    public int testOption = 0;
    public int activeTask = 0;
    public int emvMvdId = 0;
    public int emvGameId = 0;
    public int pqSwitchType = 0;
    public java.util.ArrayList<java.lang.Integer> reserved = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2.class) {
            return false;
        }
        vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2 other = (vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2) otherObject;
        if (android.os.HidlSupport.deepEquals(this.base, other.base) && this.memcToPt == other.memcToPt && this.clientCompRequest == other.clientCompRequest && this.hdrRequest == other.hdrRequest && this.motionLayerIdUsing == other.motionLayerIdUsing && this.testOption == other.testOption && this.activeTask == other.activeTask && this.emvMvdId == other.emvMvdId && this.emvGameId == other.emvGameId && this.pqSwitchType == other.pqSwitchType && android.os.HidlSupport.deepEquals(this.reserved, other.reserved)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.base)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.memcToPt))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.clientCompRequest))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.hdrRequest))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.motionLayerIdUsing))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.testOption))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.activeTask))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.emvMvdId))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.emvGameId))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.pqSwitchType))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.reserved)));
    }

    public final java.lang.String toString() {
        return "{.base = " + this.base + ", .memcToPt = " + this.memcToPt + ", .clientCompRequest = " + this.clientCompRequest + ", .hdrRequest = " + this.hdrRequest + ", .motionLayerIdUsing = " + this.motionLayerIdUsing + ", .testOption = " + this.testOption + ", .activeTask = " + this.activeTask + ", .emvMvdId = " + this.emvMvdId + ", .emvGameId = " + this.emvGameId + ", .pqSwitchType = " + this.pqSwitchType + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(152L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 152, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2 _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 152);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.base.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        this.memcToPt = _hidl_blob.getInt32(_hidl_offset + 96);
        this.clientCompRequest = _hidl_blob.getInt32(_hidl_offset + 100);
        this.hdrRequest = _hidl_blob.getInt32(_hidl_offset + 104);
        this.motionLayerIdUsing = _hidl_blob.getInt32(_hidl_offset + 108);
        this.testOption = _hidl_blob.getInt32(_hidl_offset + 112);
        this.activeTask = _hidl_blob.getInt32(_hidl_offset + 116);
        this.emvMvdId = _hidl_blob.getInt32(_hidl_offset + 120);
        this.emvGameId = _hidl_blob.getInt32(_hidl_offset + 124);
        this.pqSwitchType = _hidl_blob.getInt32(_hidl_offset + 128);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 136 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), _hidl_offset + 136 + 0, true);
        this.reserved.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            int _hidl_vec_element = childBlob.getInt32(_hidl_index_0 * 4);
            this.reserved.add(java.lang.Integer.valueOf(_hidl_vec_element));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(152);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 152);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 152);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.base.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 0);
        _hidl_blob.putInt32(96 + _hidl_offset, this.memcToPt);
        _hidl_blob.putInt32(100 + _hidl_offset, this.clientCompRequest);
        _hidl_blob.putInt32(104 + _hidl_offset, this.hdrRequest);
        _hidl_blob.putInt32(108 + _hidl_offset, this.motionLayerIdUsing);
        _hidl_blob.putInt32(112 + _hidl_offset, this.testOption);
        _hidl_blob.putInt32(116 + _hidl_offset, this.activeTask);
        _hidl_blob.putInt32(120 + _hidl_offset, this.emvMvdId);
        _hidl_blob.putInt32(124 + _hidl_offset, this.emvGameId);
        _hidl_blob.putInt32(128 + _hidl_offset, this.pqSwitchType);
        int _hidl_vec_size = this.reserved.size();
        _hidl_blob.putInt32(_hidl_offset + 136 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 136 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt32(_hidl_index_0 * 4, this.reserved.get(_hidl_index_0).intValue());
        }
        _hidl_blob.putBlob(136 + _hidl_offset + 0, childBlob);
    }
}
