package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class BufferInfo {
    public long id = 0;
    public int format = 0;
    public int type = 0;
    public int flags = 0;
    public int width = 0;
    public int height = 0;
    public float refreshRate = 0.0f;
    public int frcEnable = 0;
    public int frcCounter = 0;
    public long frcTimestamp = 0;
    public java.util.ArrayList<java.lang.Integer> reserved = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.pixelworks.hardware.display.V1_1.BufferInfo.class) {
            return false;
        }
        vendor.pixelworks.hardware.display.V1_1.BufferInfo other = (vendor.pixelworks.hardware.display.V1_1.BufferInfo) otherObject;
        if (this.id == other.id && this.format == other.format && this.type == other.type && this.flags == other.flags && this.width == other.width && this.height == other.height && this.refreshRate == other.refreshRate && this.frcEnable == other.frcEnable && this.frcCounter == other.frcCounter && this.frcTimestamp == other.frcTimestamp && android.os.HidlSupport.deepEquals(this.reserved, other.reserved)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.id))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.format))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.type))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.flags))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.width))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.height))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Float.valueOf(this.refreshRate))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.frcEnable))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.frcCounter))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.frcTimestamp))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.reserved)));
    }

    public final java.lang.String toString() {
        return "{.id = " + this.id + ", .format = " + this.format + ", .type = " + this.type + ", .flags = " + this.flags + ", .width = " + this.width + ", .height = " + this.height + ", .refreshRate = " + this.refreshRate + ", .frcEnable = " + this.frcEnable + ", .frcCounter = " + this.frcCounter + ", .frcTimestamp = " + this.frcTimestamp + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(64L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.BufferInfo> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.BufferInfo> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 64, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_1.BufferInfo _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_1.BufferInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 64);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.id = _hidl_blob.getInt64(_hidl_offset + 0);
        this.format = _hidl_blob.getInt32(_hidl_offset + 8);
        this.type = _hidl_blob.getInt32(_hidl_offset + 12);
        this.flags = _hidl_blob.getInt32(_hidl_offset + 16);
        this.width = _hidl_blob.getInt32(_hidl_offset + 20);
        this.height = _hidl_blob.getInt32(_hidl_offset + 24);
        this.refreshRate = _hidl_blob.getFloat(_hidl_offset + 28);
        this.frcEnable = _hidl_blob.getInt32(_hidl_offset + 32);
        this.frcCounter = _hidl_blob.getInt32(_hidl_offset + 36);
        this.frcTimestamp = _hidl_blob.getInt64(_hidl_offset + 40);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 48 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), _hidl_offset + 48 + 0, true);
        this.reserved.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            int _hidl_vec_element = childBlob.getInt32(_hidl_index_0 * 4);
            this.reserved.add(java.lang.Integer.valueOf(_hidl_vec_element));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(64);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.BufferInfo> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 64);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 64);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt64(_hidl_offset + 0, this.id);
        _hidl_blob.putInt32(_hidl_offset + 8, this.format);
        _hidl_blob.putInt32(_hidl_offset + 12, this.type);
        _hidl_blob.putInt32(16 + _hidl_offset, this.flags);
        _hidl_blob.putInt32(20 + _hidl_offset, this.width);
        _hidl_blob.putInt32(24 + _hidl_offset, this.height);
        _hidl_blob.putFloat(28 + _hidl_offset, this.refreshRate);
        _hidl_blob.putInt32(32 + _hidl_offset, this.frcEnable);
        _hidl_blob.putInt32(36 + _hidl_offset, this.frcCounter);
        _hidl_blob.putInt64(40 + _hidl_offset, this.frcTimestamp);
        int _hidl_vec_size = this.reserved.size();
        _hidl_blob.putInt32(_hidl_offset + 48 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 48 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt32(_hidl_index_0 * 4, this.reserved.get(_hidl_index_0).intValue());
        }
        _hidl_blob.putBlob(48 + _hidl_offset + 0, childBlob);
    }
}
