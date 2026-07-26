package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class ProgramInfo {
    public int infoFlags;
    public android.hardware.broadcastradio.V2_0.ProgramSelector selector = new android.hardware.broadcastradio.V2_0.ProgramSelector();
    public android.hardware.broadcastradio.V2_0.ProgramIdentifier logicallyTunedTo = new android.hardware.broadcastradio.V2_0.ProgramIdentifier();
    public android.hardware.broadcastradio.V2_0.ProgramIdentifier physicallyTunedTo = new android.hardware.broadcastradio.V2_0.ProgramIdentifier();
    public java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramIdentifier> relatedContent = new java.util.ArrayList<>();
    public int signalQuality = 0;
    public java.util.ArrayList<android.hardware.broadcastradio.V2_0.Metadata> metadata = new java.util.ArrayList<>();
    public java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> vendorInfo = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.broadcastradio.V2_0.ProgramInfo.class) {
            return false;
        }
        android.hardware.broadcastradio.V2_0.ProgramInfo other = (android.hardware.broadcastradio.V2_0.ProgramInfo) otherObject;
        if (android.os.HidlSupport.deepEquals(this.selector, other.selector) && android.os.HidlSupport.deepEquals(this.logicallyTunedTo, other.logicallyTunedTo) && android.os.HidlSupport.deepEquals(this.physicallyTunedTo, other.physicallyTunedTo) && android.os.HidlSupport.deepEquals(this.relatedContent, other.relatedContent) && android.os.HidlSupport.deepEquals(java.lang.Integer.valueOf(this.infoFlags), java.lang.Integer.valueOf(other.infoFlags)) && this.signalQuality == other.signalQuality && android.os.HidlSupport.deepEquals(this.metadata, other.metadata) && android.os.HidlSupport.deepEquals(this.vendorInfo, other.vendorInfo)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.selector)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.logicallyTunedTo)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.physicallyTunedTo)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.relatedContent)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.infoFlags))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.signalQuality))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.metadata)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.vendorInfo)));
    }

    public final java.lang.String toString() {
        return "{.selector = " + this.selector + ", .logicallyTunedTo = " + this.logicallyTunedTo + ", .physicallyTunedTo = " + this.physicallyTunedTo + ", .relatedContent = " + this.relatedContent + ", .infoFlags = " + android.hardware.broadcastradio.V2_0.ProgramInfoFlags.dumpBitfield(this.infoFlags) + ", .signalQuality = " + this.signalQuality + ", .metadata = " + this.metadata + ", .vendorInfo = " + this.vendorInfo + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(120L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramInfo> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramInfo> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 120, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.broadcastradio.V2_0.ProgramInfo _hidl_vec_element = new android.hardware.broadcastradio.V2_0.ProgramInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 120);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.selector.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        this.logicallyTunedTo.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 32);
        this.physicallyTunedTo.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 48);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 64 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 16, _hidl_blob.handle(), _hidl_offset + 64 + 0, true);
        this.relatedContent.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.broadcastradio.V2_0.ProgramIdentifier _hidl_vec_element = new android.hardware.broadcastradio.V2_0.ProgramIdentifier();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 16);
            this.relatedContent.add(_hidl_vec_element);
        }
        this.infoFlags = _hidl_blob.getInt32(_hidl_offset + 80);
        this.signalQuality = _hidl_blob.getInt32(_hidl_offset + 84);
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 88 + 8);
        android.os.HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 32, _hidl_blob.handle(), _hidl_offset + 88 + 0, true);
        this.metadata.clear();
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            android.hardware.broadcastradio.V2_0.Metadata _hidl_vec_element2 = new android.hardware.broadcastradio.V2_0.Metadata();
            _hidl_vec_element2.readEmbeddedFromParcel(parcel, childBlob2, _hidl_index_02 * 32);
            this.metadata.add(_hidl_vec_element2);
        }
        int _hidl_vec_size3 = _hidl_blob.getInt32(_hidl_offset + 104 + 8);
        android.os.HwBlob childBlob3 = parcel.readEmbeddedBuffer(_hidl_vec_size3 * 32, _hidl_blob.handle(), _hidl_offset + 104 + 0, true);
        this.vendorInfo.clear();
        for (int _hidl_index_03 = 0; _hidl_index_03 < _hidl_vec_size3; _hidl_index_03++) {
            android.hardware.broadcastradio.V2_0.VendorKeyValue _hidl_vec_element3 = new android.hardware.broadcastradio.V2_0.VendorKeyValue();
            _hidl_vec_element3.readEmbeddedFromParcel(parcel, childBlob3, _hidl_index_03 * 32);
            this.vendorInfo.add(_hidl_vec_element3);
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(120);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramInfo> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 120);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 120);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.selector.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 0);
        this.logicallyTunedTo.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 32);
        this.physicallyTunedTo.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 48);
        int _hidl_vec_size = this.relatedContent.size();
        _hidl_blob.putInt32(_hidl_offset + 64 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 64 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 16);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.relatedContent.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 16);
        }
        _hidl_blob.putBlob(_hidl_offset + 64 + 0, childBlob);
        _hidl_blob.putInt32(_hidl_offset + 80, this.infoFlags);
        _hidl_blob.putInt32(_hidl_offset + 84, this.signalQuality);
        int _hidl_vec_size2 = this.metadata.size();
        _hidl_blob.putInt32(_hidl_offset + 88 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 88 + 12, false);
        android.os.HwBlob childBlob2 = new android.os.HwBlob(_hidl_vec_size2 * 32);
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            this.metadata.get(_hidl_index_02).writeEmbeddedToBlob(childBlob2, _hidl_index_02 * 32);
        }
        _hidl_blob.putBlob(_hidl_offset + 88 + 0, childBlob2);
        int _hidl_vec_size3 = this.vendorInfo.size();
        _hidl_blob.putInt32(_hidl_offset + 104 + 8, _hidl_vec_size3);
        _hidl_blob.putBool(_hidl_offset + 104 + 12, false);
        android.os.HwBlob childBlob3 = new android.os.HwBlob(_hidl_vec_size3 * 32);
        for (int _hidl_index_03 = 0; _hidl_index_03 < _hidl_vec_size3; _hidl_index_03++) {
            this.vendorInfo.get(_hidl_index_03).writeEmbeddedToBlob(childBlob3, _hidl_index_03 * 32);
        }
        _hidl_blob.putBlob(_hidl_offset + 104 + 0, childBlob3);
    }
}
