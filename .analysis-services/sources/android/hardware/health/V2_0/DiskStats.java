package android.hardware.health.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class DiskStats {
    public long reads = 0;
    public long readMerges = 0;
    public long readSectors = 0;
    public long readTicks = 0;
    public long writes = 0;
    public long writeMerges = 0;
    public long writeSectors = 0;
    public long writeTicks = 0;
    public long ioInFlight = 0;
    public long ioTicks = 0;
    public long ioInQueue = 0;
    public android.hardware.health.V2_0.StorageAttribute attr = new android.hardware.health.V2_0.StorageAttribute();

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.health.V2_0.DiskStats.class) {
            return false;
        }
        android.hardware.health.V2_0.DiskStats other = (android.hardware.health.V2_0.DiskStats) otherObject;
        if (this.reads == other.reads && this.readMerges == other.readMerges && this.readSectors == other.readSectors && this.readTicks == other.readTicks && this.writes == other.writes && this.writeMerges == other.writeMerges && this.writeSectors == other.writeSectors && this.writeTicks == other.writeTicks && this.ioInFlight == other.ioInFlight && this.ioTicks == other.ioTicks && this.ioInQueue == other.ioInQueue && android.os.HidlSupport.deepEquals(this.attr, other.attr)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.reads))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.readMerges))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.readSectors))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.readTicks))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.writes))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.writeMerges))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.writeSectors))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.writeTicks))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.ioInFlight))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.ioTicks))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.ioInQueue))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.attr)));
    }

    public final java.lang.String toString() {
        return "{.reads = " + this.reads + ", .readMerges = " + this.readMerges + ", .readSectors = " + this.readSectors + ", .readTicks = " + this.readTicks + ", .writes = " + this.writes + ", .writeMerges = " + this.writeMerges + ", .writeSectors = " + this.writeSectors + ", .writeTicks = " + this.writeTicks + ", .ioInFlight = " + this.ioInFlight + ", .ioTicks = " + this.ioTicks + ", .ioInQueue = " + this.ioInQueue + ", .attr = " + this.attr + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(112L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.health.V2_0.DiskStats> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.health.V2_0.DiskStats> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 112, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.health.V2_0.DiskStats _hidl_vec_element = new android.hardware.health.V2_0.DiskStats();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 112);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.reads = _hidl_blob.getInt64(0 + _hidl_offset);
        this.readMerges = _hidl_blob.getInt64(8 + _hidl_offset);
        this.readSectors = _hidl_blob.getInt64(16 + _hidl_offset);
        this.readTicks = _hidl_blob.getInt64(24 + _hidl_offset);
        this.writes = _hidl_blob.getInt64(32 + _hidl_offset);
        this.writeMerges = _hidl_blob.getInt64(40 + _hidl_offset);
        this.writeSectors = _hidl_blob.getInt64(48 + _hidl_offset);
        this.writeTicks = _hidl_blob.getInt64(56 + _hidl_offset);
        this.ioInFlight = _hidl_blob.getInt64(64 + _hidl_offset);
        this.ioTicks = _hidl_blob.getInt64(72 + _hidl_offset);
        this.ioInQueue = _hidl_blob.getInt64(80 + _hidl_offset);
        this.attr.readEmbeddedFromParcel(parcel, _hidl_blob, 88 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(112);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.health.V2_0.DiskStats> _hidl_vec) {
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
        _hidl_blob.putInt64(0 + _hidl_offset, this.reads);
        _hidl_blob.putInt64(8 + _hidl_offset, this.readMerges);
        _hidl_blob.putInt64(16 + _hidl_offset, this.readSectors);
        _hidl_blob.putInt64(24 + _hidl_offset, this.readTicks);
        _hidl_blob.putInt64(32 + _hidl_offset, this.writes);
        _hidl_blob.putInt64(40 + _hidl_offset, this.writeMerges);
        _hidl_blob.putInt64(48 + _hidl_offset, this.writeSectors);
        _hidl_blob.putInt64(56 + _hidl_offset, this.writeTicks);
        _hidl_blob.putInt64(64 + _hidl_offset, this.ioInFlight);
        _hidl_blob.putInt64(72 + _hidl_offset, this.ioTicks);
        _hidl_blob.putInt64(80 + _hidl_offset, this.ioInQueue);
        this.attr.writeEmbeddedToBlob(_hidl_blob, 88 + _hidl_offset);
    }
}
