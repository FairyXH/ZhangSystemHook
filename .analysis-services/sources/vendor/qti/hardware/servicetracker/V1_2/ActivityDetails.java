package vendor.qti.hardware.servicetracker.V1_2;

/* JADX INFO: loaded from: classes4.dex */
public final class ActivityDetails {
    public int launchedFromPid = 0;
    public int launchedFromUid = 0;
    public java.lang.String packageName = new java.lang.String();
    public java.lang.String processName = new java.lang.String();
    public java.lang.String intent = new java.lang.String();
    public java.lang.String className = new java.lang.String();
    public int versioncode = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.qti.hardware.servicetracker.V1_2.ActivityDetails.class) {
            return false;
        }
        vendor.qti.hardware.servicetracker.V1_2.ActivityDetails other = (vendor.qti.hardware.servicetracker.V1_2.ActivityDetails) otherObject;
        if (this.launchedFromPid == other.launchedFromPid && this.launchedFromUid == other.launchedFromUid && android.os.HidlSupport.deepEquals(this.packageName, other.packageName) && android.os.HidlSupport.deepEquals(this.processName, other.processName) && android.os.HidlSupport.deepEquals(this.intent, other.intent) && android.os.HidlSupport.deepEquals(this.className, other.className) && this.versioncode == other.versioncode) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.launchedFromPid))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.launchedFromUid))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.packageName)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.processName)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.intent)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.className)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.versioncode))));
    }

    public final java.lang.String toString() {
        return "{.launchedFromPid = " + this.launchedFromPid + ", .launchedFromUid = " + this.launchedFromUid + ", .packageName = " + this.packageName + ", .processName = " + this.processName + ", .intent = " + this.intent + ", .className = " + this.className + ", .versioncode = " + this.versioncode + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(80L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_2.ActivityDetails> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_2.ActivityDetails> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 80, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.qti.hardware.servicetracker.V1_2.ActivityDetails _hidl_vec_element = new vendor.qti.hardware.servicetracker.V1_2.ActivityDetails();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 80);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.launchedFromPid = _hidl_blob.getInt32(_hidl_offset + 0);
        this.launchedFromUid = _hidl_blob.getInt32(_hidl_offset + 4);
        this.packageName = _hidl_blob.getString(_hidl_offset + 8);
        parcel.readEmbeddedBuffer(this.packageName.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 8 + 0, false);
        this.processName = _hidl_blob.getString(_hidl_offset + 24);
        parcel.readEmbeddedBuffer(this.processName.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 24 + 0, false);
        this.intent = _hidl_blob.getString(_hidl_offset + 40);
        parcel.readEmbeddedBuffer(this.intent.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 40 + 0, false);
        this.className = _hidl_blob.getString(_hidl_offset + 56);
        parcel.readEmbeddedBuffer(this.className.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 56 + 0, false);
        this.versioncode = _hidl_blob.getInt32(_hidl_offset + 72);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(80);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_2.ActivityDetails> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 80);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 80);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.launchedFromPid);
        _hidl_blob.putInt32(4 + _hidl_offset, this.launchedFromUid);
        _hidl_blob.putString(8 + _hidl_offset, this.packageName);
        _hidl_blob.putString(24 + _hidl_offset, this.processName);
        _hidl_blob.putString(40 + _hidl_offset, this.intent);
        _hidl_blob.putString(56 + _hidl_offset, this.className);
        _hidl_blob.putInt32(72 + _hidl_offset, this.versioncode);
    }
}
