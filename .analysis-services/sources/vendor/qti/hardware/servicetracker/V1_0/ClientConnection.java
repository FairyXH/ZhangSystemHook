package vendor.qti.hardware.servicetracker.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public final class ClientConnection {
    public java.lang.String serviceName = new java.lang.String();
    public int servicePid = 0;
    public int count = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.qti.hardware.servicetracker.V1_0.ClientConnection.class) {
            return false;
        }
        vendor.qti.hardware.servicetracker.V1_0.ClientConnection other = (vendor.qti.hardware.servicetracker.V1_0.ClientConnection) otherObject;
        if (android.os.HidlSupport.deepEquals(this.serviceName, other.serviceName) && this.servicePid == other.servicePid && this.count == other.count) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.serviceName)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.servicePid))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.count))));
    }

    public final java.lang.String toString() {
        return "{.serviceName = " + this.serviceName + ", .servicePid = " + this.servicePid + ", .count = " + this.count + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ClientConnection> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ClientConnection> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.qti.hardware.servicetracker.V1_0.ClientConnection _hidl_vec_element = new vendor.qti.hardware.servicetracker.V1_0.ClientConnection();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.serviceName = _hidl_blob.getString(_hidl_offset + 0);
        parcel.readEmbeddedBuffer(this.serviceName.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 0 + 0, false);
        this.servicePid = _hidl_blob.getInt32(16 + _hidl_offset);
        this.count = _hidl_blob.getInt32(20 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ClientConnection> _hidl_vec) {
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
        _hidl_blob.putString(0 + _hidl_offset, this.serviceName);
        _hidl_blob.putInt32(16 + _hidl_offset, this.servicePid);
        _hidl_blob.putInt32(20 + _hidl_offset, this.count);
    }
}
