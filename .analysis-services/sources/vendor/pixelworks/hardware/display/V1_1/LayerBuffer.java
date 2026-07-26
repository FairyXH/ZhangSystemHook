package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public final class LayerBuffer {
    public int width = 0;
    public int height = 0;
    public int unalignedWidth = 0;
    public int unalignedHeight = 0;
    public vendor.pixelworks.hardware.display.V1_1.ColorMetaData colorMetadata = new vendor.pixelworks.hardware.display.V1_1.ColorMetaData();
    public int acquireFenceFd = 0;
    public int releaseFenceFd = 0;
    public int flags = 0;
    public android.os.NativeHandle bufferHandle = new android.os.NativeHandle();
    public vendor.pixelworks.hardware.display.V1_1.BufferInfo bufferInfo = new vendor.pixelworks.hardware.display.V1_1.BufferInfo();

    public final java.lang.String toString() {
        return "{.width = " + this.width + ", .height = " + this.height + ", .unalignedWidth = " + this.unalignedWidth + ", .unalignedHeight = " + this.unalignedHeight + ", .colorMetadata = " + this.colorMetadata + ", .acquireFenceFd = " + this.acquireFenceFd + ", .releaseFenceFd = " + this.releaseFenceFd + ", .flags = " + this.flags + ", .bufferHandle = " + this.bufferHandle + ", .bufferInfo = " + this.bufferInfo + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(160L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.LayerBuffer> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.LayerBuffer> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 160, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            vendor.pixelworks.hardware.display.V1_1.LayerBuffer _hidl_vec_element = new vendor.pixelworks.hardware.display.V1_1.LayerBuffer();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 160);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.width = _hidl_blob.getInt32(_hidl_offset + 0);
        this.height = _hidl_blob.getInt32(4 + _hidl_offset);
        this.unalignedWidth = _hidl_blob.getInt32(8 + _hidl_offset);
        this.unalignedHeight = _hidl_blob.getInt32(12 + _hidl_offset);
        this.colorMetadata.readEmbeddedFromParcel(parcel, _hidl_blob, 16 + _hidl_offset);
        this.acquireFenceFd = _hidl_blob.getInt32(64 + _hidl_offset);
        this.releaseFenceFd = _hidl_blob.getInt32(68 + _hidl_offset);
        this.flags = _hidl_blob.getInt32(72 + _hidl_offset);
        this.bufferHandle = parcel.readEmbeddedNativeHandle(_hidl_blob.handle(), 80 + _hidl_offset + 0);
        this.bufferInfo.readEmbeddedFromParcel(parcel, _hidl_blob, 96 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(160);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<vendor.pixelworks.hardware.display.V1_1.LayerBuffer> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 160);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 160);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.width);
        _hidl_blob.putInt32(4 + _hidl_offset, this.height);
        _hidl_blob.putInt32(8 + _hidl_offset, this.unalignedWidth);
        _hidl_blob.putInt32(12 + _hidl_offset, this.unalignedHeight);
        this.colorMetadata.writeEmbeddedToBlob(_hidl_blob, 16 + _hidl_offset);
        _hidl_blob.putInt32(64 + _hidl_offset, this.acquireFenceFd);
        _hidl_blob.putInt32(68 + _hidl_offset, this.releaseFenceFd);
        _hidl_blob.putInt32(72 + _hidl_offset, this.flags);
        _hidl_blob.putNativeHandle(80 + _hidl_offset, this.bufferHandle);
        this.bufferInfo.writeEmbeddedToBlob(_hidl_blob, 96 + _hidl_offset);
    }
}
