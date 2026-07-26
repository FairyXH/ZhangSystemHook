package android.hardware.biometrics.fingerprint.V2_1;

/* JADX INFO: loaded from: classes.dex */
public final class FingerprintAuthenticated {
    public android.hardware.biometrics.fingerprint.V2_1.FingerprintFingerId finger = new android.hardware.biometrics.fingerprint.V2_1.FingerprintFingerId();
    public byte[] hat = new byte[69];

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.biometrics.fingerprint.V2_1.FingerprintAuthenticated.class) {
            return false;
        }
        android.hardware.biometrics.fingerprint.V2_1.FingerprintAuthenticated other = (android.hardware.biometrics.fingerprint.V2_1.FingerprintAuthenticated) otherObject;
        if (android.os.HidlSupport.deepEquals(this.finger, other.finger) && android.os.HidlSupport.deepEquals(this.hat, other.hat)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.finger)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.hat)));
    }

    public final java.lang.String toString() {
        return "{.finger = " + this.finger + ", .hat = " + java.util.Arrays.toString(this.hat) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(80L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.biometrics.fingerprint.V2_1.FingerprintAuthenticated> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.biometrics.fingerprint.V2_1.FingerprintAuthenticated> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 80, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.biometrics.fingerprint.V2_1.FingerprintAuthenticated _hidl_vec_element = new android.hardware.biometrics.fingerprint.V2_1.FingerprintAuthenticated();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 80);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.finger.readEmbeddedFromParcel(parcel, _hidl_blob, 0 + _hidl_offset);
        long _hidl_array_offset_0 = 8 + _hidl_offset;
        _hidl_blob.copyToInt8Array(_hidl_array_offset_0, this.hat, 69);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(80);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.biometrics.fingerprint.V2_1.FingerprintAuthenticated> _hidl_vec) {
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
        this.finger.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        long _hidl_array_offset_0 = 8 + _hidl_offset;
        byte[] _hidl_array_item_0 = this.hat;
        if (_hidl_array_item_0 == null || _hidl_array_item_0.length != 69) {
            throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
        }
        _hidl_blob.putInt8Array(_hidl_array_offset_0, _hidl_array_item_0);
    }
}
