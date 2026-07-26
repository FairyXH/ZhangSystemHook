package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class Metadata {
    public int key = 0;
    public long intValue = 0;
    public java.lang.String stringValue = new java.lang.String();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.broadcastradio.V2_0.Metadata.class) {
            return false;
        }
        android.hardware.broadcastradio.V2_0.Metadata other = (android.hardware.broadcastradio.V2_0.Metadata) otherObject;
        if (this.key == other.key && this.intValue == other.intValue && android.os.HidlSupport.deepEquals(this.stringValue, other.stringValue)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.key))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.intValue))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.stringValue)));
    }

    public final java.lang.String toString() {
        return "{.key = " + this.key + ", .intValue = " + this.intValue + ", .stringValue = " + this.stringValue + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(32L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.broadcastradio.V2_0.Metadata> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.broadcastradio.V2_0.Metadata> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.broadcastradio.V2_0.Metadata _hidl_vec_element = new android.hardware.broadcastradio.V2_0.Metadata();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 32);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.key = _hidl_blob.getInt32(_hidl_offset + 0);
        this.intValue = _hidl_blob.getInt64(_hidl_offset + 8);
        this.stringValue = _hidl_blob.getString(_hidl_offset + 16);
        parcel.readEmbeddedBuffer(this.stringValue.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, false);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(32);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.broadcastradio.V2_0.Metadata> _hidl_vec) {
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
        _hidl_blob.putInt32(0 + _hidl_offset, this.key);
        _hidl_blob.putInt64(8 + _hidl_offset, this.intValue);
        _hidl_blob.putString(16 + _hidl_offset, this.stringValue);
    }
}
