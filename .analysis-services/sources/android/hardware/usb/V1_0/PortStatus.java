package android.hardware.usb.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class PortStatus {
    public java.lang.String portName = new java.lang.String();
    public int currentDataRole = 0;
    public int currentPowerRole = 0;
    public int currentMode = 0;
    public boolean canChangeMode = false;
    public boolean canChangeDataRole = false;
    public boolean canChangePowerRole = false;
    public int supportedModes = 0;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.usb.V1_0.PortStatus.class) {
            return false;
        }
        android.hardware.usb.V1_0.PortStatus other = (android.hardware.usb.V1_0.PortStatus) otherObject;
        if (android.os.HidlSupport.deepEquals(this.portName, other.portName) && this.currentDataRole == other.currentDataRole && this.currentPowerRole == other.currentPowerRole && this.currentMode == other.currentMode && this.canChangeMode == other.canChangeMode && this.canChangeDataRole == other.canChangeDataRole && this.canChangePowerRole == other.canChangePowerRole && this.supportedModes == other.supportedModes) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.portName)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.currentDataRole))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.currentPowerRole))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.currentMode))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.canChangeMode))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.canChangeDataRole))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.canChangePowerRole))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.supportedModes))));
    }

    public final java.lang.String toString() {
        return "{.portName = " + this.portName + ", .currentDataRole = " + android.hardware.usb.V1_0.PortDataRole.toString(this.currentDataRole) + ", .currentPowerRole = " + android.hardware.usb.V1_0.PortPowerRole.toString(this.currentPowerRole) + ", .currentMode = " + android.hardware.usb.V1_0.PortMode.toString(this.currentMode) + ", .canChangeMode = " + this.canChangeMode + ", .canChangeDataRole = " + this.canChangeDataRole + ", .canChangePowerRole = " + this.canChangePowerRole + ", .supportedModes = " + android.hardware.usb.V1_0.PortMode.toString(this.supportedModes) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(40L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.usb.V1_0.PortStatus> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.usb.V1_0.PortStatus> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 40, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.usb.V1_0.PortStatus _hidl_vec_element = new android.hardware.usb.V1_0.PortStatus();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 40);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.portName = _hidl_blob.getString(_hidl_offset + 0);
        parcel.readEmbeddedBuffer(this.portName.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 0 + 0, false);
        this.currentDataRole = _hidl_blob.getInt32(16 + _hidl_offset);
        this.currentPowerRole = _hidl_blob.getInt32(20 + _hidl_offset);
        this.currentMode = _hidl_blob.getInt32(24 + _hidl_offset);
        this.canChangeMode = _hidl_blob.getBool(28 + _hidl_offset);
        this.canChangeDataRole = _hidl_blob.getBool(29 + _hidl_offset);
        this.canChangePowerRole = _hidl_blob.getBool(30 + _hidl_offset);
        this.supportedModes = _hidl_blob.getInt32(32 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(40);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.usb.V1_0.PortStatus> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 40);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 40);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putString(0 + _hidl_offset, this.portName);
        _hidl_blob.putInt32(16 + _hidl_offset, this.currentDataRole);
        _hidl_blob.putInt32(20 + _hidl_offset, this.currentPowerRole);
        _hidl_blob.putInt32(24 + _hidl_offset, this.currentMode);
        _hidl_blob.putBool(28 + _hidl_offset, this.canChangeMode);
        _hidl_blob.putBool(29 + _hidl_offset, this.canChangeDataRole);
        _hidl_blob.putBool(30 + _hidl_offset, this.canChangePowerRole);
        _hidl_blob.putInt32(32 + _hidl_offset, this.supportedModes);
    }
}
