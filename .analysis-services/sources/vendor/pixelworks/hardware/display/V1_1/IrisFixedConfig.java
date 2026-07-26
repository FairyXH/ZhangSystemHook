package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class IrisFixedConfig {
    public int hdrFormal = 0;
    public java.util.ArrayList<java.lang.Integer> hdrLut = new java.util.ArrayList<>();
    public int memcEnable = 0;
    public int memcLevel = 0;
    public int dualChannel = 0;
    public int movingLayer = 0;
    public int memcVideoLayer = 0;
    public int inMemcState = 0;
    public int videoFps = 0;
    public int videoInMemory = 0;
    public int gameMode = 0;
    public int captureDisable = 0;
    public int dualPrepare = 0;
    public int inOsdSwitch = 0;
    public int dualPreload = 0;
    public int metadataDone = 0;
    public java.util.ArrayList<java.lang.Integer> reserved = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig.class) {
            return false;
        }
        vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig other = (vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig) otherObject;
        if (this.hdrFormal == other.hdrFormal && android.os.HidlSupport.deepEquals(this.hdrLut, other.hdrLut) && this.memcEnable == other.memcEnable && this.memcLevel == other.memcLevel && this.dualChannel == other.dualChannel && this.movingLayer == other.movingLayer && this.memcVideoLayer == other.memcVideoLayer && this.inMemcState == other.inMemcState && this.videoFps == other.videoFps && this.videoInMemory == other.videoInMemory && this.gameMode == other.gameMode && this.captureDisable == other.captureDisable && this.dualPrepare == other.dualPrepare && this.inOsdSwitch == other.inOsdSwitch && this.dualPreload == other.dualPreload && this.metadataDone == other.metadataDone && android.os.HidlSupport.deepEquals(this.reserved, other.reserved)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.hdrFormal))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.hdrLut)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.memcEnable))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.memcLevel))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.dualChannel))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.movingLayer))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.memcVideoLayer))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.inMemcState))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.videoFps))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.videoInMemory))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.gameMode))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.captureDisable))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.dualPrepare))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.inOsdSwitch))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.dualPreload))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.metadataDone))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.reserved)));
    }

    public final java.lang.String toString() {
        return "{.hdrFormal = " + this.hdrFormal + ", .hdrLut = " + this.hdrLut + ", .memcEnable = " + this.memcEnable + ", .memcLevel = " + this.memcLevel + ", .dualChannel = " + this.dualChannel + ", .movingLayer = " + this.movingLayer + ", .memcVideoLayer = " + this.memcVideoLayer + ", .inMemcState = " + this.inMemcState + ", .videoFps = " + this.videoFps + ", .videoInMemory = " + this.videoInMemory + ", .gameMode = " + this.gameMode + ", .captureDisable = " + this.captureDisable + ", .dualPrepare = " + this.dualPrepare + ", .inOsdSwitch = " + this.inOsdSwitch + ", .dualPreload = " + this.dualPreload + ", .metadataDone = " + this.metadataDone + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(96L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 96, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 96);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.hdrFormal = _hidl_blob.getInt32(_hidl_offset + 0);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 8 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), _hidl_offset + 8 + 0, true);
        this.hdrLut.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            int _hidl_vec_element = childBlob.getInt32(_hidl_index_0 * 4);
            this.hdrLut.add(java.lang.Integer.valueOf(_hidl_vec_element));
        }
        this.memcEnable = _hidl_blob.getInt32(_hidl_offset + 24);
        this.memcLevel = _hidl_blob.getInt32(_hidl_offset + 28);
        this.dualChannel = _hidl_blob.getInt32(_hidl_offset + 32);
        this.movingLayer = _hidl_blob.getInt32(_hidl_offset + 36);
        this.memcVideoLayer = _hidl_blob.getInt32(_hidl_offset + 40);
        this.inMemcState = _hidl_blob.getInt32(_hidl_offset + 44);
        this.videoFps = _hidl_blob.getInt32(_hidl_offset + 48);
        this.videoInMemory = _hidl_blob.getInt32(_hidl_offset + 52);
        this.gameMode = _hidl_blob.getInt32(_hidl_offset + 56);
        this.captureDisable = _hidl_blob.getInt32(_hidl_offset + 60);
        this.dualPrepare = _hidl_blob.getInt32(_hidl_offset + 64);
        this.inOsdSwitch = _hidl_blob.getInt32(_hidl_offset + 68);
        this.dualPreload = _hidl_blob.getInt32(_hidl_offset + 72);
        this.metadataDone = _hidl_blob.getInt32(_hidl_offset + 76);
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 80 + 8);
        android.os.HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 4, _hidl_blob.handle(), _hidl_offset + 80 + 0, true);
        this.reserved.clear();
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            int _hidl_vec_element2 = childBlob2.getInt32(_hidl_index_02 * 4);
            this.reserved.add(java.lang.Integer.valueOf(_hidl_vec_element2));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(96);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 96);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 96);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(_hidl_offset + 0, this.hdrFormal);
        int _hidl_vec_size = this.hdrLut.size();
        _hidl_blob.putInt32(_hidl_offset + 8 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 8 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt32(_hidl_index_0 * 4, this.hdrLut.get(_hidl_index_0).intValue());
        }
        _hidl_blob.putBlob(_hidl_offset + 8 + 0, childBlob);
        _hidl_blob.putInt32(_hidl_offset + 24, this.memcEnable);
        _hidl_blob.putInt32(_hidl_offset + 28, this.memcLevel);
        _hidl_blob.putInt32(_hidl_offset + 32, this.dualChannel);
        _hidl_blob.putInt32(_hidl_offset + 36, this.movingLayer);
        _hidl_blob.putInt32(_hidl_offset + 40, this.memcVideoLayer);
        _hidl_blob.putInt32(_hidl_offset + 44, this.inMemcState);
        _hidl_blob.putInt32(_hidl_offset + 48, this.videoFps);
        _hidl_blob.putInt32(_hidl_offset + 52, this.videoInMemory);
        _hidl_blob.putInt32(_hidl_offset + 56, this.gameMode);
        _hidl_blob.putInt32(_hidl_offset + 60, this.captureDisable);
        _hidl_blob.putInt32(_hidl_offset + 64, this.dualPrepare);
        _hidl_blob.putInt32(_hidl_offset + 68, this.inOsdSwitch);
        _hidl_blob.putInt32(_hidl_offset + 72, this.dualPreload);
        _hidl_blob.putInt32(_hidl_offset + 76, this.metadataDone);
        int _hidl_vec_size2 = this.reserved.size();
        _hidl_blob.putInt32(_hidl_offset + 80 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 80 + 12, false);
        android.os.HwBlob childBlob2 = new android.os.HwBlob(_hidl_vec_size2 * 4);
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            childBlob2.putInt32(_hidl_index_02 * 4, this.reserved.get(_hidl_index_02).intValue());
        }
        _hidl_blob.putBlob(_hidl_offset + 80 + 0, childBlob2);
    }
}
