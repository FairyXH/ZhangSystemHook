package android.hardware.biometrics.fingerprint.V2_1;

/* JADX INFO: loaded from: classes.dex */
public final class FingerprintAcquired {
    public int acquiredInfo = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.biometrics.fingerprint.V2_1.FingerprintAcquired.class) {
            return false;
        }
        android.hardware.biometrics.fingerprint.V2_1.FingerprintAcquired other = (android.hardware.biometrics.fingerprint.V2_1.FingerprintAcquired) otherObject;
        if (this.acquiredInfo == other.acquiredInfo) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.acquiredInfo))));
    }

    public final java.lang.String toString() {
        return "{.acquiredInfo = " + android.hardware.biometrics.fingerprint.V2_1.FingerprintAcquiredInfo.toString(this.acquiredInfo) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(4L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.biometrics.fingerprint.V2_1.FingerprintAcquired> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.biometrics.fingerprint.V2_1.FingerprintAcquired> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.biometrics.fingerprint.V2_1.FingerprintAcquired _hidl_vec_element = new android.hardware.biometrics.fingerprint.V2_1.FingerprintAcquired();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 4);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.acquiredInfo = _hidl_blob.getInt32(0 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(4);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.biometrics.fingerprint.V2_1.FingerprintAcquired> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 4);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.acquiredInfo);
    }
}
