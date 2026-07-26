package android.hardware.tv.cec.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class HdmiPortInfo {
    public int type = 0;
    public int portId = 0;
    public boolean cecSupported = false;
    public boolean arcSupported = false;
    public short physicalAddress = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.tv.cec.V1_0.HdmiPortInfo.class) {
            return false;
        }
        android.hardware.tv.cec.V1_0.HdmiPortInfo other = (android.hardware.tv.cec.V1_0.HdmiPortInfo) otherObject;
        if (this.type == other.type && this.portId == other.portId && this.cecSupported == other.cecSupported && this.arcSupported == other.arcSupported && this.physicalAddress == other.physicalAddress) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.type))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.portId))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.cecSupported))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.arcSupported))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Short.valueOf(this.physicalAddress))));
    }

    public final java.lang.String toString() {
        return "{.type = " + android.hardware.tv.cec.V1_0.HdmiPortType.toString(this.type) + ", .portId = " + this.portId + ", .cecSupported = " + this.cecSupported + ", .arcSupported = " + this.arcSupported + ", .physicalAddress = " + ((int) this.physicalAddress) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(12L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.tv.cec.V1_0.HdmiPortInfo> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.tv.cec.V1_0.HdmiPortInfo> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 12, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.tv.cec.V1_0.HdmiPortInfo _hidl_vec_element = new android.hardware.tv.cec.V1_0.HdmiPortInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 12);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.type = _hidl_blob.getInt32(0 + _hidl_offset);
        this.portId = _hidl_blob.getInt32(4 + _hidl_offset);
        this.cecSupported = _hidl_blob.getBool(8 + _hidl_offset);
        this.arcSupported = _hidl_blob.getBool(9 + _hidl_offset);
        this.physicalAddress = _hidl_blob.getInt16(10 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(12);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.tv.cec.V1_0.HdmiPortInfo> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 12);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 12);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.type);
        _hidl_blob.putInt32(4 + _hidl_offset, this.portId);
        _hidl_blob.putBool(8 + _hidl_offset, this.cecSupported);
        _hidl_blob.putBool(9 + _hidl_offset, this.arcSupported);
        _hidl_blob.putInt16(10 + _hidl_offset, this.physicalAddress);
    }
}
