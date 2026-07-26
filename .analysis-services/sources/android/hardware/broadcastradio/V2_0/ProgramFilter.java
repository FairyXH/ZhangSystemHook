package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class ProgramFilter {
    public java.util.ArrayList<java.lang.Integer> identifierTypes = new java.util.ArrayList<>();
    public java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramIdentifier> identifiers = new java.util.ArrayList<>();
    public boolean includeCategories = false;
    public boolean excludeModifications = false;

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.broadcastradio.V2_0.ProgramFilter.class) {
            return false;
        }
        android.hardware.broadcastradio.V2_0.ProgramFilter other = (android.hardware.broadcastradio.V2_0.ProgramFilter) otherObject;
        if (android.os.HidlSupport.deepEquals(this.identifierTypes, other.identifierTypes) && android.os.HidlSupport.deepEquals(this.identifiers, other.identifiers) && this.includeCategories == other.includeCategories && this.excludeModifications == other.excludeModifications) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.identifierTypes)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.identifiers)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.includeCategories))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.excludeModifications))));
    }

    public final java.lang.String toString() {
        return "{.identifierTypes = " + this.identifierTypes + ", .identifiers = " + this.identifiers + ", .includeCategories = " + this.includeCategories + ", .excludeModifications = " + this.excludeModifications + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(40L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramFilter> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramFilter> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 40, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.broadcastradio.V2_0.ProgramFilter _hidl_vec_element = new android.hardware.broadcastradio.V2_0.ProgramFilter();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 40);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 0 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 4, _hidl_blob.handle(), _hidl_offset + 0 + 0, true);
        this.identifierTypes.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.identifierTypes.add(java.lang.Integer.valueOf(childBlob.getInt32(_hidl_index_0 * 4)));
        }
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 16 + 8);
        android.os.HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 16, _hidl_blob.handle(), _hidl_offset + 16 + 0, true);
        this.identifiers.clear();
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            android.hardware.broadcastradio.V2_0.ProgramIdentifier _hidl_vec_element = new android.hardware.broadcastradio.V2_0.ProgramIdentifier();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob2, _hidl_index_02 * 16);
            this.identifiers.add(_hidl_vec_element);
        }
        this.includeCategories = _hidl_blob.getBool(_hidl_offset + 32);
        this.excludeModifications = _hidl_blob.getBool(_hidl_offset + 33);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(40);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramFilter> _hidl_vec) {
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
        int _hidl_vec_size = this.identifierTypes.size();
        _hidl_blob.putInt32(_hidl_offset + 0 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 0 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 4);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt32(_hidl_index_0 * 4, this.identifierTypes.get(_hidl_index_0).intValue());
        }
        _hidl_blob.putBlob(_hidl_offset + 0 + 0, childBlob);
        int _hidl_vec_size2 = this.identifiers.size();
        _hidl_blob.putInt32(_hidl_offset + 16 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 16 + 12, false);
        android.os.HwBlob childBlob2 = new android.os.HwBlob(_hidl_vec_size2 * 16);
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            this.identifiers.get(_hidl_index_02).writeEmbeddedToBlob(childBlob2, _hidl_index_02 * 16);
        }
        _hidl_blob.putBlob(_hidl_offset + 16 + 0, childBlob2);
        _hidl_blob.putBool(_hidl_offset + 32, this.includeCategories);
        _hidl_blob.putBool(_hidl_offset + 33, this.excludeModifications);
    }
}
