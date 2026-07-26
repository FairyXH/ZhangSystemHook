package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class Properties {
    public java.lang.String maker = new java.lang.String();
    public java.lang.String product = new java.lang.String();
    public java.lang.String version = new java.lang.String();
    public java.lang.String serial = new java.lang.String();
    public java.util.ArrayList<java.lang.Integer> supportedIdentifierTypes = new java.util.ArrayList<>();
    public java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> vendorInfo = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.broadcastradio.V2_0.Properties.class) {
            return false;
        }
        android.hardware.broadcastradio.V2_0.Properties other = (android.hardware.broadcastradio.V2_0.Properties) otherObject;
        if (android.os.HidlSupport.deepEquals(this.maker, other.maker) && android.os.HidlSupport.deepEquals(this.product, other.product) && android.os.HidlSupport.deepEquals(this.version, other.version) && android.os.HidlSupport.deepEquals(this.serial, other.serial) && android.os.HidlSupport.deepEquals(this.supportedIdentifierTypes, other.supportedIdentifierTypes) && android.os.HidlSupport.deepEquals(this.vendorInfo, other.vendorInfo)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.maker)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.product)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.version)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.serial)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.supportedIdentifierTypes)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.vendorInfo)));
    }

    public final java.lang.String toString() {
        return "{.maker = " + this.maker + ", .product = " + this.product + ", .version = " + this.version + ", .serial = " + this.serial + ", .supportedIdentifierTypes = " + this.supportedIdentifierTypes + ", .vendorInfo = " + this.vendorInfo + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(96L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.broadcastradio.V2_0.Properties> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.broadcastradio.V2_0.Properties> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 96, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.broadcastradio.V2_0.Properties _hidl_vec_element = new android.hardware.broadcastradio.V2_0.Properties();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 96);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.maker = _hidl_blob.getString(_hidl_offset + 0);
        parcel.readEmbeddedBuffer(this.maker.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 0 + 0, false);
        this.product = _hidl_blob.getString(_hidl_offset + 16);
        parcel.readEmbeddedBuffer(this.product.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, false);
        this.version = _hidl_blob.getString(_hidl_offset + 32);
        parcel.readEmbeddedBuffer(this.version.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 32 + 0, false);
        this.serial = _hidl_blob.getString(_hidl_offset + 48);
        parcel.readEmbeddedBuffer(this.serial.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 48 + 0, false);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 64 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), _hidl_offset + 64 + 0, true);
        this.supportedIdentifierTypes.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.supportedIdentifierTypes.add(java.lang.Integer.valueOf(childBlob.getInt32(_hidl_index_0 * 4)));
        }
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 80 + 8);
        android.os.HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 32, _hidl_blob.handle(), _hidl_offset + 80 + 0, true);
        this.vendorInfo.clear();
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            android.hardware.broadcastradio.V2_0.VendorKeyValue _hidl_vec_element = new android.hardware.broadcastradio.V2_0.VendorKeyValue();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob2, _hidl_index_02 * 32);
            this.vendorInfo.add(_hidl_vec_element);
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(96);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.broadcastradio.V2_0.Properties> _hidl_vec) {
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
        _hidl_blob.putString(_hidl_offset + 0, this.maker);
        _hidl_blob.putString(_hidl_offset + 16, this.product);
        _hidl_blob.putString(_hidl_offset + 32, this.version);
        _hidl_blob.putString(_hidl_offset + 48, this.serial);
        int _hidl_vec_size = this.supportedIdentifierTypes.size();
        _hidl_blob.putInt32(_hidl_offset + 64 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 64 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt32(_hidl_index_0 * 4, this.supportedIdentifierTypes.get(_hidl_index_0).intValue());
        }
        _hidl_blob.putBlob(_hidl_offset + 64 + 0, childBlob);
        int _hidl_vec_size2 = this.vendorInfo.size();
        _hidl_blob.putInt32(_hidl_offset + 80 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 80 + 12, false);
        android.os.HwBlob childBlob2 = new android.os.HwBlob(_hidl_vec_size2 * 32);
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            this.vendorInfo.get(_hidl_index_02).writeEmbeddedToBlob(childBlob2, _hidl_index_02 * 32);
        }
        _hidl_blob.putBlob(_hidl_offset + 80 + 0, childBlob2);
    }
}
