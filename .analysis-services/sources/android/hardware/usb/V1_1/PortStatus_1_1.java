package android.hardware.usb.V1_1;

/* JADX INFO: loaded from: classes.dex */
public final class PortStatus_1_1 {
    public int supportedModes;
    public android.hardware.usb.V1_0.PortStatus status = new android.hardware.usb.V1_0.PortStatus();
    public int currentMode = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.usb.V1_1.PortStatus_1_1.class) {
            return false;
        }
        android.hardware.usb.V1_1.PortStatus_1_1 other = (android.hardware.usb.V1_1.PortStatus_1_1) otherObject;
        if (android.os.HidlSupport.deepEquals(this.status, other.status) && android.os.HidlSupport.deepEquals(java.lang.Integer.valueOf(this.supportedModes), java.lang.Integer.valueOf(other.supportedModes)) && this.currentMode == other.currentMode) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.status)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.supportedModes))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.currentMode))));
    }

    public final java.lang.String toString() {
        return "{.status = " + this.status + ", .supportedModes = " + android.hardware.usb.V1_1.PortMode_1_1.dumpBitfield(this.supportedModes) + ", .currentMode = " + android.hardware.usb.V1_1.PortMode_1_1.toString(this.currentMode) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(48L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.usb.V1_1.PortStatus_1_1> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.usb.V1_1.PortStatus_1_1> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 48, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.usb.V1_1.PortStatus_1_1 _hidl_vec_element = new android.hardware.usb.V1_1.PortStatus_1_1();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 48);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.status.readEmbeddedFromParcel(parcel, _hidl_blob, 0 + _hidl_offset);
        this.supportedModes = _hidl_blob.getInt32(40 + _hidl_offset);
        this.currentMode = _hidl_blob.getInt32(44 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(48);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.usb.V1_1.PortStatus_1_1> _hidl_vec) {
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
        this.status.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        _hidl_blob.putInt32(40 + _hidl_offset, this.supportedModes);
        _hidl_blob.putInt32(44 + _hidl_offset, this.currentMode);
    }
}
