package android.hardware.health.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class HealthConfig {
    public int periodicChoresIntervalFast = 0;
    public int periodicChoresIntervalSlow = 0;
    public java.lang.String batteryStatusPath = new java.lang.String();
    public java.lang.String batteryHealthPath = new java.lang.String();
    public java.lang.String batteryPresentPath = new java.lang.String();
    public java.lang.String batteryCapacityPath = new java.lang.String();
    public java.lang.String batteryVoltagePath = new java.lang.String();
    public java.lang.String batteryTemperaturePath = new java.lang.String();
    public java.lang.String batteryTechnologyPath = new java.lang.String();
    public java.lang.String batteryCurrentNowPath = new java.lang.String();
    public java.lang.String batteryCurrentAvgPath = new java.lang.String();
    public java.lang.String batteryChargeCounterPath = new java.lang.String();
    public java.lang.String batteryFullChargePath = new java.lang.String();
    public java.lang.String batteryCycleCountPath = new java.lang.String();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.health.V1_0.HealthConfig.class) {
            return false;
        }
        android.hardware.health.V1_0.HealthConfig other = (android.hardware.health.V1_0.HealthConfig) otherObject;
        if (this.periodicChoresIntervalFast == other.periodicChoresIntervalFast && this.periodicChoresIntervalSlow == other.periodicChoresIntervalSlow && android.os.HidlSupport.deepEquals(this.batteryStatusPath, other.batteryStatusPath) && android.os.HidlSupport.deepEquals(this.batteryHealthPath, other.batteryHealthPath) && android.os.HidlSupport.deepEquals(this.batteryPresentPath, other.batteryPresentPath) && android.os.HidlSupport.deepEquals(this.batteryCapacityPath, other.batteryCapacityPath) && android.os.HidlSupport.deepEquals(this.batteryVoltagePath, other.batteryVoltagePath) && android.os.HidlSupport.deepEquals(this.batteryTemperaturePath, other.batteryTemperaturePath) && android.os.HidlSupport.deepEquals(this.batteryTechnologyPath, other.batteryTechnologyPath) && android.os.HidlSupport.deepEquals(this.batteryCurrentNowPath, other.batteryCurrentNowPath) && android.os.HidlSupport.deepEquals(this.batteryCurrentAvgPath, other.batteryCurrentAvgPath) && android.os.HidlSupport.deepEquals(this.batteryChargeCounterPath, other.batteryChargeCounterPath) && android.os.HidlSupport.deepEquals(this.batteryFullChargePath, other.batteryFullChargePath) && android.os.HidlSupport.deepEquals(this.batteryCycleCountPath, other.batteryCycleCountPath)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.periodicChoresIntervalFast))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.periodicChoresIntervalSlow))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryStatusPath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryHealthPath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryPresentPath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryCapacityPath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryVoltagePath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryTemperaturePath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryTechnologyPath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryCurrentNowPath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryCurrentAvgPath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryChargeCounterPath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryFullChargePath)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.batteryCycleCountPath)));
    }

    public final java.lang.String toString() {
        return "{.periodicChoresIntervalFast = " + this.periodicChoresIntervalFast + ", .periodicChoresIntervalSlow = " + this.periodicChoresIntervalSlow + ", .batteryStatusPath = " + this.batteryStatusPath + ", .batteryHealthPath = " + this.batteryHealthPath + ", .batteryPresentPath = " + this.batteryPresentPath + ", .batteryCapacityPath = " + this.batteryCapacityPath + ", .batteryVoltagePath = " + this.batteryVoltagePath + ", .batteryTemperaturePath = " + this.batteryTemperaturePath + ", .batteryTechnologyPath = " + this.batteryTechnologyPath + ", .batteryCurrentNowPath = " + this.batteryCurrentNowPath + ", .batteryCurrentAvgPath = " + this.batteryCurrentAvgPath + ", .batteryChargeCounterPath = " + this.batteryChargeCounterPath + ", .batteryFullChargePath = " + this.batteryFullChargePath + ", .batteryCycleCountPath = " + this.batteryCycleCountPath + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(200L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.health.V1_0.HealthConfig> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.health.V1_0.HealthConfig> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 200, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.health.V1_0.HealthConfig _hidl_vec_element = new android.hardware.health.V1_0.HealthConfig();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 200);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.periodicChoresIntervalFast = _hidl_blob.getInt32(_hidl_offset + 0);
        this.periodicChoresIntervalSlow = _hidl_blob.getInt32(_hidl_offset + 4);
        this.batteryStatusPath = _hidl_blob.getString(_hidl_offset + 8);
        parcel.readEmbeddedBuffer(this.batteryStatusPath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 8 + 0, false);
        this.batteryHealthPath = _hidl_blob.getString(_hidl_offset + 24);
        parcel.readEmbeddedBuffer(this.batteryHealthPath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 24 + 0, false);
        this.batteryPresentPath = _hidl_blob.getString(_hidl_offset + 40);
        parcel.readEmbeddedBuffer(this.batteryPresentPath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 40 + 0, false);
        this.batteryCapacityPath = _hidl_blob.getString(_hidl_offset + 56);
        parcel.readEmbeddedBuffer(this.batteryCapacityPath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 56 + 0, false);
        this.batteryVoltagePath = _hidl_blob.getString(_hidl_offset + 72);
        parcel.readEmbeddedBuffer(this.batteryVoltagePath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 72 + 0, false);
        this.batteryTemperaturePath = _hidl_blob.getString(_hidl_offset + 88);
        parcel.readEmbeddedBuffer(this.batteryTemperaturePath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 88 + 0, false);
        this.batteryTechnologyPath = _hidl_blob.getString(_hidl_offset + 104);
        parcel.readEmbeddedBuffer(this.batteryTechnologyPath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 104 + 0, false);
        this.batteryCurrentNowPath = _hidl_blob.getString(_hidl_offset + 120);
        parcel.readEmbeddedBuffer(this.batteryCurrentNowPath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 120 + 0, false);
        this.batteryCurrentAvgPath = _hidl_blob.getString(_hidl_offset + 136);
        parcel.readEmbeddedBuffer(this.batteryCurrentAvgPath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 136 + 0, false);
        this.batteryChargeCounterPath = _hidl_blob.getString(_hidl_offset + 152);
        parcel.readEmbeddedBuffer(this.batteryChargeCounterPath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 152 + 0, false);
        this.batteryFullChargePath = _hidl_blob.getString(_hidl_offset + 168);
        parcel.readEmbeddedBuffer(this.batteryFullChargePath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 168 + 0, false);
        this.batteryCycleCountPath = _hidl_blob.getString(_hidl_offset + 184);
        parcel.readEmbeddedBuffer(this.batteryCycleCountPath.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 184 + 0, false);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(200);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.health.V1_0.HealthConfig> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 200);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 200);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.periodicChoresIntervalFast);
        _hidl_blob.putInt32(4 + _hidl_offset, this.periodicChoresIntervalSlow);
        _hidl_blob.putString(8 + _hidl_offset, this.batteryStatusPath);
        _hidl_blob.putString(24 + _hidl_offset, this.batteryHealthPath);
        _hidl_blob.putString(40 + _hidl_offset, this.batteryPresentPath);
        _hidl_blob.putString(56 + _hidl_offset, this.batteryCapacityPath);
        _hidl_blob.putString(72 + _hidl_offset, this.batteryVoltagePath);
        _hidl_blob.putString(88 + _hidl_offset, this.batteryTemperaturePath);
        _hidl_blob.putString(104 + _hidl_offset, this.batteryTechnologyPath);
        _hidl_blob.putString(120 + _hidl_offset, this.batteryCurrentNowPath);
        _hidl_blob.putString(136 + _hidl_offset, this.batteryCurrentAvgPath);
        _hidl_blob.putString(152 + _hidl_offset, this.batteryChargeCounterPath);
        _hidl_blob.putString(168 + _hidl_offset, this.batteryFullChargePath);
        _hidl_blob.putString(184 + _hidl_offset, this.batteryCycleCountPath);
    }
}
