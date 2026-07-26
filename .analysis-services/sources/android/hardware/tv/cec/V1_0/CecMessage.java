package android.hardware.tv.cec.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class CecMessage {
    public int initiator = 0;
    public int destination = 0;
    public java.util.ArrayList<java.lang.Byte> body = new java.util.ArrayList<>();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.tv.cec.V1_0.CecMessage.class) {
            return false;
        }
        android.hardware.tv.cec.V1_0.CecMessage other = (android.hardware.tv.cec.V1_0.CecMessage) otherObject;
        if (this.initiator == other.initiator && this.destination == other.destination && android.os.HidlSupport.deepEquals(this.body, other.body)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.initiator))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.destination))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.body)));
    }

    public final java.lang.String toString() {
        return "{.initiator = " + android.hardware.tv.cec.V1_0.CecLogicalAddress.toString(this.initiator) + ", .destination = " + android.hardware.tv.cec.V1_0.CecLogicalAddress.toString(this.destination) + ", .body = " + this.body + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.tv.cec.V1_0.CecMessage> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.tv.cec.V1_0.CecMessage> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.tv.cec.V1_0.CecMessage _hidl_vec_element = new android.hardware.tv.cec.V1_0.CecMessage();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.initiator = _hidl_blob.getInt32(_hidl_offset + 0);
        this.destination = _hidl_blob.getInt32(_hidl_offset + 4);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 8 + 8);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 1, _hidl_blob.handle(), _hidl_offset + 8 + 0, true);
        this.body.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            byte _hidl_vec_element = childBlob.getInt8(_hidl_index_0 * 1);
            this.body.add(java.lang.Byte.valueOf(_hidl_vec_element));
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.tv.cec.V1_0.CecMessage> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 24);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 24);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(_hidl_offset + 0, this.initiator);
        _hidl_blob.putInt32(4 + _hidl_offset, this.destination);
        int _hidl_vec_size = this.body.size();
        _hidl_blob.putInt32(_hidl_offset + 8 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 8 + 12, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 1);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt8(_hidl_index_0 * 1, this.body.get(_hidl_index_0).byteValue());
        }
        _hidl_blob.putBlob(8 + _hidl_offset + 0, childBlob);
    }
}
