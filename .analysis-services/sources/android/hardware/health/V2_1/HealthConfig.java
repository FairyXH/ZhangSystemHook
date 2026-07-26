package android.hardware.health.V2_1;

/* JADX INFO: loaded from: classes.dex */
public final class HealthConfig {
    public android.hardware.health.V1_0.HealthConfig battery = new android.hardware.health.V1_0.HealthConfig();
    public int bootMinCap = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.health.V2_1.HealthConfig.class) {
            return false;
        }
        android.hardware.health.V2_1.HealthConfig other = (android.hardware.health.V2_1.HealthConfig) otherObject;
        if (android.os.HidlSupport.deepEquals(this.battery, other.battery) && this.bootMinCap == other.bootMinCap) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.battery)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.bootMinCap))));
    }

    public final java.lang.String toString() {
        return "{.battery = " + this.battery + ", .bootMinCap = " + this.bootMinCap + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(208L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.health.V2_1.HealthConfig> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.health.V2_1.HealthConfig> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 208, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.health.V2_1.HealthConfig _hidl_vec_element = new android.hardware.health.V2_1.HealthConfig();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 208);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.battery.readEmbeddedFromParcel(parcel, _hidl_blob, 0 + _hidl_offset);
        this.bootMinCap = _hidl_blob.getInt32(200 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(208);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.health.V2_1.HealthConfig> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 208);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 208);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.battery.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        _hidl_blob.putInt32(200 + _hidl_offset, this.bootMinCap);
    }
}
