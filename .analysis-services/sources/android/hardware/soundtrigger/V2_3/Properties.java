package android.hardware.soundtrigger.V2_3;

/* JADX INFO: loaded from: classes.dex */
public final class Properties {
    public int audioCapabilities;
    public android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties base = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties();
    public java.lang.String supportedModelArch = new java.lang.String();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.soundtrigger.V2_3.Properties.class) {
            return false;
        }
        android.hardware.soundtrigger.V2_3.Properties other = (android.hardware.soundtrigger.V2_3.Properties) otherObject;
        if (android.os.HidlSupport.deepEquals(this.base, other.base) && android.os.HidlSupport.deepEquals(this.supportedModelArch, other.supportedModelArch) && android.os.HidlSupport.deepEquals(java.lang.Integer.valueOf(this.audioCapabilities), java.lang.Integer.valueOf(other.audioCapabilities))) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.base)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.supportedModelArch)), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.audioCapabilities))));
    }

    public final java.lang.String toString() {
        return "{.base = " + this.base + ", .supportedModelArch = " + this.supportedModelArch + ", .audioCapabilities = " + android.hardware.soundtrigger.V2_3.AudioCapabilities.dumpBitfield(this.audioCapabilities) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(112L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.soundtrigger.V2_3.Properties> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.soundtrigger.V2_3.Properties> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 112, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.soundtrigger.V2_3.Properties _hidl_vec_element = new android.hardware.soundtrigger.V2_3.Properties();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 112);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.base.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        this.supportedModelArch = _hidl_blob.getString(_hidl_offset + 88);
        parcel.readEmbeddedBuffer(this.supportedModelArch.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 88 + 0, false);
        this.audioCapabilities = _hidl_blob.getInt32(_hidl_offset + 104);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(112);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.soundtrigger.V2_3.Properties> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 112);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 112);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.base.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        _hidl_blob.putString(88 + _hidl_offset, this.supportedModelArch);
        _hidl_blob.putInt32(104 + _hidl_offset, this.audioCapabilities);
    }
}
