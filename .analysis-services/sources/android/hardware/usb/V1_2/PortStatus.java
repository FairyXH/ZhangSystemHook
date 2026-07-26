package android.hardware.usb.V1_2;

/* JADX INFO: loaded from: classes.dex */
public final class PortStatus {
    public int supportedContaminantProtectionModes;
    public android.hardware.usb.V1_1.PortStatus_1_1 status_1_1 = new android.hardware.usb.V1_1.PortStatus_1_1();
    public boolean supportsEnableContaminantPresenceProtection = false;
    public int contaminantProtectionStatus = 0;
    public boolean supportsEnableContaminantPresenceDetection = false;
    public int contaminantDetectionStatus = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.usb.V1_2.PortStatus.class) {
            return false;
        }
        android.hardware.usb.V1_2.PortStatus other = (android.hardware.usb.V1_2.PortStatus) otherObject;
        if (android.os.HidlSupport.deepEquals(this.status_1_1, other.status_1_1) && android.os.HidlSupport.deepEquals(java.lang.Integer.valueOf(this.supportedContaminantProtectionModes), java.lang.Integer.valueOf(other.supportedContaminantProtectionModes)) && this.supportsEnableContaminantPresenceProtection == other.supportsEnableContaminantPresenceProtection && this.contaminantProtectionStatus == other.contaminantProtectionStatus && this.supportsEnableContaminantPresenceDetection == other.supportsEnableContaminantPresenceDetection && this.contaminantDetectionStatus == other.contaminantDetectionStatus) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.status_1_1)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.supportedContaminantProtectionModes))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.supportsEnableContaminantPresenceProtection))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.contaminantProtectionStatus))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.supportsEnableContaminantPresenceDetection))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.contaminantDetectionStatus))));
    }

    public final java.lang.String toString() {
        return "{.status_1_1 = " + this.status_1_1 + ", .supportedContaminantProtectionModes = " + android.hardware.usb.V1_2.ContaminantProtectionMode.dumpBitfield(this.supportedContaminantProtectionModes) + ", .supportsEnableContaminantPresenceProtection = " + this.supportsEnableContaminantPresenceProtection + ", .contaminantProtectionStatus = " + android.hardware.usb.V1_2.ContaminantProtectionStatus.toString(this.contaminantProtectionStatus) + ", .supportsEnableContaminantPresenceDetection = " + this.supportsEnableContaminantPresenceDetection + ", .contaminantDetectionStatus = " + android.hardware.usb.V1_2.ContaminantDetectionStatus.toString(this.contaminantDetectionStatus) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(72L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.usb.V1_2.PortStatus> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.usb.V1_2.PortStatus> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 72, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.usb.V1_2.PortStatus _hidl_vec_element = new android.hardware.usb.V1_2.PortStatus();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 72);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.status_1_1.readEmbeddedFromParcel(parcel, _hidl_blob, 0 + _hidl_offset);
        this.supportedContaminantProtectionModes = _hidl_blob.getInt32(48 + _hidl_offset);
        this.supportsEnableContaminantPresenceProtection = _hidl_blob.getBool(52 + _hidl_offset);
        this.contaminantProtectionStatus = _hidl_blob.getInt32(56 + _hidl_offset);
        this.supportsEnableContaminantPresenceDetection = _hidl_blob.getBool(60 + _hidl_offset);
        this.contaminantDetectionStatus = _hidl_blob.getInt32(64 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(72);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.usb.V1_2.PortStatus> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 72);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 72);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.status_1_1.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        _hidl_blob.putInt32(48 + _hidl_offset, this.supportedContaminantProtectionModes);
        _hidl_blob.putBool(52 + _hidl_offset, this.supportsEnableContaminantPresenceProtection);
        _hidl_blob.putInt32(56 + _hidl_offset, this.contaminantProtectionStatus);
        _hidl_blob.putBool(60 + _hidl_offset, this.supportsEnableContaminantPresenceDetection);
        _hidl_blob.putInt32(64 + _hidl_offset, this.contaminantDetectionStatus);
    }
}
