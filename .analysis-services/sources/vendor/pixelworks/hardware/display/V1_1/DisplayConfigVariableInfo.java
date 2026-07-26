package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class DisplayConfigVariableInfo {
    public boolean valid = false;
    public int xPixels = 0;
    public int yPixels = 0;
    public float xDpi = 0.0f;
    public float yDpi = 0.0f;
    public int fps = 0;
    public int vsyncPeriodNs = 0;
    public boolean isYuv = false;
    public boolean smartPanel = false;
    public java.util.ArrayList<java.lang.Integer> reserved = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo.class) {
            return false;
        }
        vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo other = (vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo) otherObject;
        if (this.valid == other.valid && this.xPixels == other.xPixels && this.yPixels == other.yPixels && this.xDpi == other.xDpi && this.yDpi == other.yDpi && this.fps == other.fps && this.vsyncPeriodNs == other.vsyncPeriodNs && this.isYuv == other.isYuv && this.smartPanel == other.smartPanel && android.os.HidlSupport.deepEquals(this.reserved, other.reserved)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.valid))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.xPixels))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.yPixels))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Float.valueOf(this.xDpi))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Float.valueOf(this.yDpi))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.fps))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.vsyncPeriodNs))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.isYuv))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.smartPanel))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.reserved)));
    }

    public final java.lang.String toString() {
        return "{.valid = " + this.valid + ", .xPixels = " + this.xPixels + ", .yPixels = " + this.yPixels + ", .xDpi = " + this.xDpi + ", .yDpi = " + this.yDpi + ", .fps = " + this.fps + ", .vsyncPeriodNs = " + this.vsyncPeriodNs + ", .isYuv = " + this.isYuv + ", .smartPanel = " + this.smartPanel + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(48L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 48, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 48);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.valid = _hidl_blob.getBool(_hidl_offset + 0);
        this.xPixels = _hidl_blob.getInt32(_hidl_offset + 4);
        this.yPixels = _hidl_blob.getInt32(_hidl_offset + 8);
        this.xDpi = _hidl_blob.getFloat(_hidl_offset + 12);
        this.yDpi = _hidl_blob.getFloat(_hidl_offset + 16);
        this.fps = _hidl_blob.getInt32(_hidl_offset + 20);
        this.vsyncPeriodNs = _hidl_blob.getInt32(_hidl_offset + 24);
        this.isYuv = _hidl_blob.getBool(_hidl_offset + 28);
        this.smartPanel = _hidl_blob.getBool(_hidl_offset + 29);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 32 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), _hidl_offset + 32 + 0, true);
        this.reserved.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            int _hidl_vec_element = childBlob.getInt32(_hidl_index_0 * 4);
            this.reserved.add(java.lang.Integer.valueOf(_hidl_vec_element));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(48);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo> _hidl_vec) {
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
        _hidl_blob.putBool(_hidl_offset + 0, this.valid);
        _hidl_blob.putInt32(4 + _hidl_offset, this.xPixels);
        _hidl_blob.putInt32(_hidl_offset + 8, this.yPixels);
        _hidl_blob.putFloat(_hidl_offset + 12, this.xDpi);
        _hidl_blob.putFloat(16 + _hidl_offset, this.yDpi);
        _hidl_blob.putInt32(20 + _hidl_offset, this.fps);
        _hidl_blob.putInt32(24 + _hidl_offset, this.vsyncPeriodNs);
        _hidl_blob.putBool(28 + _hidl_offset, this.isYuv);
        _hidl_blob.putBool(29 + _hidl_offset, this.smartPanel);
        int _hidl_vec_size = this.reserved.size();
        _hidl_blob.putInt32(_hidl_offset + 32 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 32 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt32(_hidl_index_0 * 4, this.reserved.get(_hidl_index_0).intValue());
        }
        _hidl_blob.putBlob(32 + _hidl_offset + 0, childBlob);
    }
}
