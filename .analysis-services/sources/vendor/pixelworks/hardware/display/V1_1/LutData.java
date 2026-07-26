package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class LutData {
    public short dim = 0;
    public short gridSize = 0;
    public java.util.ArrayList<java.lang.Integer> lutEntries = new java.util.ArrayList<>();
    public boolean validLutEntries = false;
    public java.util.ArrayList<java.lang.Short> gridEntries = new java.util.ArrayList<>();
    public boolean validGridEntries = false;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != vendor.pixelworks.hardware.display.V1_1.LutData.class) {
            return false;
        }
        vendor.pixelworks.hardware.display.V1_1.LutData other = (vendor.pixelworks.hardware.display.V1_1.LutData) otherObject;
        if (this.dim == other.dim && this.gridSize == other.gridSize && android.os.HidlSupport.deepEquals(this.lutEntries, other.lutEntries) && this.validLutEntries == other.validLutEntries && android.os.HidlSupport.deepEquals(this.gridEntries, other.gridEntries) && this.validGridEntries == other.validGridEntries) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Short.valueOf(this.dim))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Short.valueOf(this.gridSize))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.lutEntries)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.validLutEntries))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.gridEntries)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.validGridEntries))));
    }

    public final java.lang.String toString() {
        return "{.dim = " + ((int) this.dim) + ", .gridSize = " + ((int) this.gridSize) + ", .lutEntries = " + this.lutEntries + ", .validLutEntries = " + this.validLutEntries + ", .gridEntries = " + this.gridEntries + ", .validGridEntries = " + this.validGridEntries + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(56L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.LutData> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.LutData> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 56, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_1.LutData _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_1.LutData();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 56);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.dim = _hidl_blob.getInt16(_hidl_offset + 0);
        this.gridSize = _hidl_blob.getInt16(_hidl_offset + 2);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 8 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), _hidl_offset + 8 + 0, true);
        this.lutEntries.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            int _hidl_vec_element = childBlob.getInt32(_hidl_index_0 * 4);
            this.lutEntries.add(java.lang.Integer.valueOf(_hidl_vec_element));
        }
        this.validLutEntries = _hidl_blob.getBool(_hidl_offset + 24);
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 32 + 8);
        android.os.HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 2, _hidl_blob.handle(), _hidl_offset + 32 + 0, true);
        this.gridEntries.clear();
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            short _hidl_vec_element2 = childBlob2.getInt16(_hidl_index_02 * 2);
            this.gridEntries.add(java.lang.Short.valueOf(_hidl_vec_element2));
        }
        this.validGridEntries = _hidl_blob.getBool(_hidl_offset + 48);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(56);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.LutData> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 56);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 56);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt16(_hidl_offset + 0, this.dim);
        _hidl_blob.putInt16(_hidl_offset + 2, this.gridSize);
        int _hidl_vec_size = this.lutEntries.size();
        _hidl_blob.putInt32(_hidl_offset + 8 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 8 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt32(_hidl_index_0 * 4, this.lutEntries.get(_hidl_index_0).intValue());
        }
        _hidl_blob.putBlob(_hidl_offset + 8 + 0, childBlob);
        _hidl_blob.putBool(_hidl_offset + 24, this.validLutEntries);
        int _hidl_vec_size2 = this.gridEntries.size();
        _hidl_blob.putInt32(_hidl_offset + 32 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 32 + 12, false);
        android.os.HwBlob childBlob2 = new android.os.HwBlob(_hidl_vec_size2 * 2);
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            childBlob2.putInt16(_hidl_index_02 * 2, this.gridEntries.get(_hidl_index_02).shortValue());
        }
        _hidl_blob.putBlob(_hidl_offset + 32 + 0, childBlob2);
        _hidl_blob.putBool(_hidl_offset + 48, this.validGridEntries);
    }
}
