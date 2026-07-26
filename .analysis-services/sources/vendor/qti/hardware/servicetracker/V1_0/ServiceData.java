package vendor.qti.hardware.servicetracker.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class ServiceData {
    public java.lang.String packageName = new java.lang.String();
    public java.lang.String processName = new java.lang.String();
    public int pid = 0;
    public double lastActivity = 0.0d;
    public boolean serviceB = false;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.qti.hardware.servicetracker.V1_0.ServiceData.class) {
            return false;
        }
        vendor.qti.hardware.servicetracker.V1_0.ServiceData other = (vendor.qti.hardware.servicetracker.V1_0.ServiceData) otherObject;
        if (android.os.HidlSupport.deepEquals(this.packageName, other.packageName) && android.os.HidlSupport.deepEquals(this.processName, other.processName) && this.pid == other.pid && this.lastActivity == other.lastActivity && this.serviceB == other.serviceB) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.packageName)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.processName)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.pid))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Double.valueOf(this.lastActivity))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.serviceB))));
    }

    public final java.lang.String toString() {
        return "{.packageName = " + this.packageName + ", .processName = " + this.processName + ", .pid = " + this.pid + ", .lastActivity = " + this.lastActivity + ", .serviceB = " + this.serviceB + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(56L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ServiceData> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ServiceData> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 56, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.qti.hardware.servicetracker.V1_0.ServiceData _hidl_vec_element = new vendor.qti.hardware.servicetracker.V1_0.ServiceData();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 56);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.packageName = _hidl_blob.getString(_hidl_offset + 0);
        parcel.readEmbeddedBuffer(this.packageName.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 0 + 0, false);
        this.processName = _hidl_blob.getString(_hidl_offset + 16);
        parcel.readEmbeddedBuffer(this.processName.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, false);
        this.pid = _hidl_blob.getInt32(_hidl_offset + 32);
        this.lastActivity = _hidl_blob.getDouble(_hidl_offset + 40);
        this.serviceB = _hidl_blob.getBool(_hidl_offset + 48);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(56);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ServiceData> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 56);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 56);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putString(0 + _hidl_offset, this.packageName);
        _hidl_blob.putString(16 + _hidl_offset, this.processName);
        _hidl_blob.putInt32(32 + _hidl_offset, this.pid);
        _hidl_blob.putDouble(40 + _hidl_offset, this.lastActivity);
        _hidl_blob.putBool(48 + _hidl_offset, this.serviceB);
    }
}
