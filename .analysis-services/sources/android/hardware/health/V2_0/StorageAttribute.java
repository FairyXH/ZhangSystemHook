package android.hardware.health.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class StorageAttribute {
    public boolean isInternal = false;
    public boolean isBootDevice = false;
    public java.lang.String name = new java.lang.String();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.health.V2_0.StorageAttribute.class) {
            return false;
        }
        android.hardware.health.V2_0.StorageAttribute other = (android.hardware.health.V2_0.StorageAttribute) otherObject;
        if (this.isInternal == other.isInternal && this.isBootDevice == other.isBootDevice && android.os.HidlSupport.deepEquals(this.name, other.name)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.isInternal))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.isBootDevice))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.name)));
    }

    public final java.lang.String toString() {
        return "{.isInternal = " + this.isInternal + ", .isBootDevice = " + this.isBootDevice + ", .name = " + this.name + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.health.V2_0.StorageAttribute> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.health.V2_0.StorageAttribute> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.health.V2_0.StorageAttribute _hidl_vec_element = new android.hardware.health.V2_0.StorageAttribute();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.isInternal = _hidl_blob.getBool(_hidl_offset + 0);
        this.isBootDevice = _hidl_blob.getBool(_hidl_offset + 1);
        this.name = _hidl_blob.getString(_hidl_offset + 8);
        parcel.readEmbeddedBuffer(this.name.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 8 + 0, false);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.health.V2_0.StorageAttribute> _hidl_vec) {
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
        _hidl_blob.putBool(0 + _hidl_offset, this.isInternal);
        _hidl_blob.putBool(1 + _hidl_offset, this.isBootDevice);
        _hidl_blob.putString(8 + _hidl_offset, this.name);
    }
}
