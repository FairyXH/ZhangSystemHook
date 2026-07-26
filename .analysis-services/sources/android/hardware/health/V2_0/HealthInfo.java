package android.hardware.health.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class HealthInfo {
    public android.hardware.health.V1_0.HealthInfo legacy = new android.hardware.health.V1_0.HealthInfo();
    public int batteryCurrentAverage = 0;
    public java.util.ArrayList<android.hardware.health.V2_0.DiskStats> diskStats = new java.util.ArrayList<>();
    public java.util.ArrayList<android.hardware.health.V2_0.StorageInfo> storageInfos = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.health.V2_0.HealthInfo.class) {
            return false;
        }
        android.hardware.health.V2_0.HealthInfo other = (android.hardware.health.V2_0.HealthInfo) otherObject;
        if (android.os.HidlSupport.deepEquals(this.legacy, other.legacy) && this.batteryCurrentAverage == other.batteryCurrentAverage && android.os.HidlSupport.deepEquals(this.diskStats, other.diskStats) && android.os.HidlSupport.deepEquals(this.storageInfos, other.storageInfos)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.legacy)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.batteryCurrentAverage))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.diskStats)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.storageInfos)));
    }

    public final java.lang.String toString() {
        return "{.legacy = " + this.legacy + ", .batteryCurrentAverage = " + this.batteryCurrentAverage + ", .diskStats = " + this.diskStats + ", .storageInfos = " + this.storageInfos + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(112L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.health.V2_0.HealthInfo> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.health.V2_0.HealthInfo> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 112, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.health.V2_0.HealthInfo _hidl_vec_element = new android.hardware.health.V2_0.HealthInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 112);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.legacy.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        this.batteryCurrentAverage = _hidl_blob.getInt32(_hidl_offset + 72);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 80 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 112, _hidl_blob.handle(), _hidl_offset + 80 + 0, true);
        this.diskStats.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.health.V2_0.DiskStats _hidl_vec_element = new android.hardware.health.V2_0.DiskStats();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 112);
            this.diskStats.add(_hidl_vec_element);
        }
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 96 + 8);
        android.os.HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 48, _hidl_blob.handle(), _hidl_offset + 96 + 0, true);
        this.storageInfos.clear();
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            android.hardware.health.V2_0.StorageInfo _hidl_vec_element2 = new android.hardware.health.V2_0.StorageInfo();
            _hidl_vec_element2.readEmbeddedFromParcel(parcel, childBlob2, _hidl_index_02 * 48);
            this.storageInfos.add(_hidl_vec_element2);
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(112);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.health.V2_0.HealthInfo> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 112);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 112);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.legacy.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 0);
        _hidl_blob.putInt32(_hidl_offset + 72, this.batteryCurrentAverage);
        int _hidl_vec_size = this.diskStats.size();
        _hidl_blob.putInt32(_hidl_offset + 80 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 80 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 112);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.diskStats.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 112);
        }
        _hidl_blob.putBlob(_hidl_offset + 80 + 0, childBlob);
        int _hidl_vec_size2 = this.storageInfos.size();
        _hidl_blob.putInt32(_hidl_offset + 96 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 96 + 12, false);
        android.os.HwBlob childBlob2 = new android.os.HwBlob(_hidl_vec_size2 * 48);
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            this.storageInfos.get(_hidl_index_02).writeEmbeddedToBlob(childBlob2, _hidl_index_02 * 48);
        }
        _hidl_blob.putBlob(_hidl_offset + 96 + 0, childBlob2);
    }
}
