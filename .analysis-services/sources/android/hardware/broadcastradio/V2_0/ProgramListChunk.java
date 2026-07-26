package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class ProgramListChunk {
    public boolean purge = false;
    public boolean complete = false;
    public java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramInfo> modified = new java.util.ArrayList<>();
    public java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramIdentifier> removed = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.broadcastradio.V2_0.ProgramListChunk.class) {
            return false;
        }
        android.hardware.broadcastradio.V2_0.ProgramListChunk other = (android.hardware.broadcastradio.V2_0.ProgramListChunk) otherObject;
        if (this.purge == other.purge && this.complete == other.complete && android.os.HidlSupport.deepEquals(this.modified, other.modified) && android.os.HidlSupport.deepEquals(this.removed, other.removed)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.purge))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Boolean.valueOf(this.complete))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.modified)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.removed)));
    }

    public final java.lang.String toString() {
        return "{.purge = " + this.purge + ", .complete = " + this.complete + ", .modified = " + this.modified + ", .removed = " + this.removed + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(40L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramListChunk> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramListChunk> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 40, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.broadcastradio.V2_0.ProgramListChunk _hidl_vec_element = new android.hardware.broadcastradio.V2_0.ProgramListChunk();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 40);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.purge = _hidl_blob.getBool(_hidl_offset + 0);
        this.complete = _hidl_blob.getBool(_hidl_offset + 1);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 8 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 120, _hidl_blob.handle(), _hidl_offset + 8 + 0, true);
        this.modified.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.broadcastradio.V2_0.ProgramInfo _hidl_vec_element = new android.hardware.broadcastradio.V2_0.ProgramInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 120);
            this.modified.add(_hidl_vec_element);
        }
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 24 + 8);
        android.os.HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 16, _hidl_blob.handle(), _hidl_offset + 24 + 0, true);
        this.removed.clear();
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            android.hardware.broadcastradio.V2_0.ProgramIdentifier _hidl_vec_element2 = new android.hardware.broadcastradio.V2_0.ProgramIdentifier();
            _hidl_vec_element2.readEmbeddedFromParcel(parcel, childBlob2, _hidl_index_02 * 16);
            this.removed.add(_hidl_vec_element2);
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(40);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramListChunk> _hidl_vec) {
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
        _hidl_blob.putBool(_hidl_offset + 0, this.purge);
        _hidl_blob.putBool(_hidl_offset + 1, this.complete);
        int _hidl_vec_size = this.modified.size();
        _hidl_blob.putInt32(_hidl_offset + 8 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 8 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 120);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.modified.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 120);
        }
        _hidl_blob.putBlob(_hidl_offset + 8 + 0, childBlob);
        int _hidl_vec_size2 = this.removed.size();
        _hidl_blob.putInt32(_hidl_offset + 24 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 24 + 12, false);
        android.os.HwBlob childBlob2 = new android.os.HwBlob(_hidl_vec_size2 * 16);
        for (int _hidl_index_02 = 0; _hidl_index_02 < _hidl_vec_size2; _hidl_index_02++) {
            this.removed.get(_hidl_index_02).writeEmbeddedToBlob(childBlob2, _hidl_index_02 * 16);
        }
        _hidl_blob.putBlob(_hidl_offset + 24 + 0, childBlob2);
    }
}
