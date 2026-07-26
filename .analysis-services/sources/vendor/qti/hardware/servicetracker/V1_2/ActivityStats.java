package vendor.qti.hardware.servicetracker.V1_2;

/* JADX INFO: loaded from: classes4.dex */
public final class ActivityStats {
    public long createTime = 0;
    public long lastVisibleTime = 0;
    public int launchCount = 0;
    public long lastLaunchTime = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.qti.hardware.servicetracker.V1_2.ActivityStats.class) {
            return false;
        }
        vendor.qti.hardware.servicetracker.V1_2.ActivityStats other = (vendor.qti.hardware.servicetracker.V1_2.ActivityStats) otherObject;
        if (this.createTime == other.createTime && this.lastVisibleTime == other.lastVisibleTime && this.launchCount == other.launchCount && this.lastLaunchTime == other.lastLaunchTime) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.createTime))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.lastVisibleTime))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.launchCount))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.lastLaunchTime))));
    }

    public final java.lang.String toString() {
        return "{.createTime = " + this.createTime + ", .lastVisibleTime = " + this.lastVisibleTime + ", .launchCount = " + this.launchCount + ", .lastLaunchTime = " + this.lastLaunchTime + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(32L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_2.ActivityStats> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_2.ActivityStats> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.qti.hardware.servicetracker.V1_2.ActivityStats _hidl_vec_element = new vendor.qti.hardware.servicetracker.V1_2.ActivityStats();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 32);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.createTime = _hidl_blob.getInt64(0 + _hidl_offset);
        this.lastVisibleTime = _hidl_blob.getInt64(8 + _hidl_offset);
        this.launchCount = _hidl_blob.getInt32(16 + _hidl_offset);
        this.lastLaunchTime = _hidl_blob.getInt64(24 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(32);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_2.ActivityStats> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 32);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 32);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt64(0 + _hidl_offset, this.createTime);
        _hidl_blob.putInt64(8 + _hidl_offset, this.lastVisibleTime);
        _hidl_blob.putInt32(16 + _hidl_offset, this.launchCount);
        _hidl_blob.putInt64(24 + _hidl_offset, this.lastLaunchTime);
    }
}
