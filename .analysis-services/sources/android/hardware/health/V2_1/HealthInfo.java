package android.hardware.health.V2_1;

/* JADX INFO: loaded from: classes.dex */
public final class HealthInfo {
    public android.hardware.health.V2_0.HealthInfo legacy = new android.hardware.health.V2_0.HealthInfo();
    public int batteryCapacityLevel = 0;
    public long batteryChargeTimeToFullNowSeconds = 0;
    public int batteryFullChargeDesignCapacityUah = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.health.V2_1.HealthInfo.class) {
            return false;
        }
        android.hardware.health.V2_1.HealthInfo other = (android.hardware.health.V2_1.HealthInfo) otherObject;
        if (android.os.HidlSupport.deepEquals(this.legacy, other.legacy) && this.batteryCapacityLevel == other.batteryCapacityLevel && this.batteryChargeTimeToFullNowSeconds == other.batteryChargeTimeToFullNowSeconds && this.batteryFullChargeDesignCapacityUah == other.batteryFullChargeDesignCapacityUah) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.legacy)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.batteryCapacityLevel))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.batteryChargeTimeToFullNowSeconds))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.batteryFullChargeDesignCapacityUah))));
    }

    public final java.lang.String toString() {
        return "{.legacy = " + this.legacy + ", .batteryCapacityLevel = " + android.hardware.health.V2_1.BatteryCapacityLevel.toString(this.batteryCapacityLevel) + ", .batteryChargeTimeToFullNowSeconds = " + this.batteryChargeTimeToFullNowSeconds + ", .batteryFullChargeDesignCapacityUah = " + this.batteryFullChargeDesignCapacityUah + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(136L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.health.V2_1.HealthInfo> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.health.V2_1.HealthInfo> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 136, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.health.V2_1.HealthInfo _hidl_vec_element = new android.hardware.health.V2_1.HealthInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 136);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.legacy.readEmbeddedFromParcel(parcel, _hidl_blob, 0 + _hidl_offset);
        this.batteryCapacityLevel = _hidl_blob.getInt32(112 + _hidl_offset);
        this.batteryChargeTimeToFullNowSeconds = _hidl_blob.getInt64(120 + _hidl_offset);
        this.batteryFullChargeDesignCapacityUah = _hidl_blob.getInt32(128 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(136);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.health.V2_1.HealthInfo> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 136);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 136);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.legacy.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        _hidl_blob.putInt32(112 + _hidl_offset, this.batteryCapacityLevel);
        _hidl_blob.putInt64(120 + _hidl_offset, this.batteryChargeTimeToFullNowSeconds);
        _hidl_blob.putInt32(128 + _hidl_offset, this.batteryFullChargeDesignCapacityUah);
    }
}
