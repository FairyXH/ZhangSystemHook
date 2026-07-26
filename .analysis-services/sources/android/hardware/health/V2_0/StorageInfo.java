package android.hardware.health.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class StorageInfo {
    public android.hardware.health.V2_0.StorageAttribute attr = new android.hardware.health.V2_0.StorageAttribute();
    public short eol = 0;
    public short lifetimeA = 0;
    public short lifetimeB = 0;
    public java.lang.String version = new java.lang.String();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.health.V2_0.StorageInfo.class) {
            return false;
        }
        android.hardware.health.V2_0.StorageInfo other = (android.hardware.health.V2_0.StorageInfo) otherObject;
        if (android.os.HidlSupport.deepEquals(this.attr, other.attr) && this.eol == other.eol && this.lifetimeA == other.lifetimeA && this.lifetimeB == other.lifetimeB && android.os.HidlSupport.deepEquals(this.version, other.version)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.attr)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Short.valueOf(this.eol))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Short.valueOf(this.lifetimeA))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Short.valueOf(this.lifetimeB))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.version)));
    }

    public final java.lang.String toString() {
        return "{.attr = " + this.attr + ", .eol = " + ((int) this.eol) + ", .lifetimeA = " + ((int) this.lifetimeA) + ", .lifetimeB = " + ((int) this.lifetimeB) + ", .version = " + this.version + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(48L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.health.V2_0.StorageInfo> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.health.V2_0.StorageInfo> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 48, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.health.V2_0.StorageInfo _hidl_vec_element = new android.hardware.health.V2_0.StorageInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 48);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.attr.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        this.eol = _hidl_blob.getInt16(_hidl_offset + 24);
        this.lifetimeA = _hidl_blob.getInt16(_hidl_offset + 26);
        this.lifetimeB = _hidl_blob.getInt16(_hidl_offset + 28);
        this.version = _hidl_blob.getString(_hidl_offset + 32);
        parcel.readEmbeddedBuffer(this.version.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 32 + 0, false);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(48);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.health.V2_0.StorageInfo> _hidl_vec) {
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
        this.attr.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        _hidl_blob.putInt16(24 + _hidl_offset, this.eol);
        _hidl_blob.putInt16(26 + _hidl_offset, this.lifetimeA);
        _hidl_blob.putInt16(28 + _hidl_offset, this.lifetimeB);
        _hidl_blob.putString(32 + _hidl_offset, this.version);
    }
}
