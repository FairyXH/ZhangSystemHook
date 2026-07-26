package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class ColorMetaData {
    public int colorPrimaries = 0;
    public int range = 0;
    public int transfer = 0;
    public boolean lightLevelSEIEnabled = false;
    public int maxContentLightLevel = 0;
    public int minPicAverageLightLevel = 0;
    public boolean dynamicMetaDataValid = false;
    public int dynamicMetaDataLen = 0;
    public java.util.ArrayList<java.lang.Byte> dynamicMetaDataPayload = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.pixelworks.hardware.display.V1_1.ColorMetaData.class) {
            return false;
        }
        vendor.pixelworks.hardware.display.V1_1.ColorMetaData other = (vendor.pixelworks.hardware.display.V1_1.ColorMetaData) otherObject;
        if (this.colorPrimaries == other.colorPrimaries && this.range == other.range && this.transfer == other.transfer && this.lightLevelSEIEnabled == other.lightLevelSEIEnabled && this.maxContentLightLevel == other.maxContentLightLevel && this.minPicAverageLightLevel == other.minPicAverageLightLevel && this.dynamicMetaDataValid == other.dynamicMetaDataValid && this.dynamicMetaDataLen == other.dynamicMetaDataLen && android.os.HidlSupport.deepEquals(this.dynamicMetaDataPayload, other.dynamicMetaDataPayload)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.colorPrimaries))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.range))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.transfer))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.lightLevelSEIEnabled))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.maxContentLightLevel))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.minPicAverageLightLevel))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.dynamicMetaDataValid))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.dynamicMetaDataLen))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.dynamicMetaDataPayload)));
    }

    public final java.lang.String toString() {
        return "{.colorPrimaries = " + this.colorPrimaries + ", .range = " + this.range + ", .transfer = " + this.transfer + ", .lightLevelSEIEnabled = " + this.lightLevelSEIEnabled + ", .maxContentLightLevel = " + this.maxContentLightLevel + ", .minPicAverageLightLevel = " + this.minPicAverageLightLevel + ", .dynamicMetaDataValid = " + this.dynamicMetaDataValid + ", .dynamicMetaDataLen = " + this.dynamicMetaDataLen + ", .dynamicMetaDataPayload = " + this.dynamicMetaDataPayload + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(48L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.ColorMetaData> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.ColorMetaData> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 48, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_1.ColorMetaData _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_1.ColorMetaData();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 48);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.colorPrimaries = _hidl_blob.getInt32(_hidl_offset + 0);
        this.range = _hidl_blob.getInt32(_hidl_offset + 4);
        this.transfer = _hidl_blob.getInt32(_hidl_offset + 8);
        this.lightLevelSEIEnabled = _hidl_blob.getBool(_hidl_offset + 12);
        this.maxContentLightLevel = _hidl_blob.getInt32(_hidl_offset + 16);
        this.minPicAverageLightLevel = _hidl_blob.getInt32(_hidl_offset + 20);
        this.dynamicMetaDataValid = _hidl_blob.getBool(_hidl_offset + 24);
        this.dynamicMetaDataLen = _hidl_blob.getInt32(_hidl_offset + 28);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 32 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 1, _hidl_blob.handle(), _hidl_offset + 32 + 0, true);
        this.dynamicMetaDataPayload.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            byte _hidl_vec_element = childBlob.getInt8(_hidl_index_0 * 1);
            this.dynamicMetaDataPayload.add(java.lang.Byte.valueOf(_hidl_vec_element));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(48);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.ColorMetaData> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 48);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 48);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(_hidl_offset + 0, this.colorPrimaries);
        _hidl_blob.putInt32(4 + _hidl_offset, this.range);
        _hidl_blob.putInt32(_hidl_offset + 8, this.transfer);
        _hidl_blob.putBool(_hidl_offset + 12, this.lightLevelSEIEnabled);
        _hidl_blob.putInt32(16 + _hidl_offset, this.maxContentLightLevel);
        _hidl_blob.putInt32(20 + _hidl_offset, this.minPicAverageLightLevel);
        _hidl_blob.putBool(24 + _hidl_offset, this.dynamicMetaDataValid);
        _hidl_blob.putInt32(28 + _hidl_offset, this.dynamicMetaDataLen);
        int _hidl_vec_size = this.dynamicMetaDataPayload.size();
        _hidl_blob.putInt32(_hidl_offset + 32 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 32 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 1);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt8(_hidl_index_0 * 1, this.dynamicMetaDataPayload.get(_hidl_index_0).byteValue());
        }
        _hidl_blob.putBlob(32 + _hidl_offset + 0, childBlob);
    }
}
