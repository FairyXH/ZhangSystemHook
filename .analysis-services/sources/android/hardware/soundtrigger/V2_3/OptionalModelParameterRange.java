package android.hardware.soundtrigger.V2_3;

/* JADX INFO: loaded from: classes.dex */
public final class OptionalModelParameterRange {
    private byte hidl_d = 0;
    private java.lang.Object hidl_o;

    public OptionalModelParameterRange() {
        this.hidl_o = null;
        this.hidl_o = new android.hidl.safe_union.V1_0.Monostate();
    }

    public static final class hidl_discriminator {
        public static final byte noinit = 0;
        public static final byte range = 1;

        public static final java.lang.String getName(byte value) {
            switch (value) {
                case 0:
                    return "noinit";
                case 1:
                    return "range";
                default:
                    return "Unknown";
            }
        }

        private hidl_discriminator() {
        }
    }

    public void noinit(android.hidl.safe_union.V1_0.Monostate noinit) {
        this.hidl_d = (byte) 0;
        this.hidl_o = noinit;
    }

    public android.hidl.safe_union.V1_0.Monostate noinit() {
        if (this.hidl_d != 0) {
            java.lang.String className = this.hidl_o != null ? this.hidl_o.getClass().getName() : "null";
            throw new java.lang.IllegalStateException("Read access to inactive union components is disallowed. Discriminator value is " + ((int) this.hidl_d) + " (corresponding to " + android.hardware.soundtrigger.V2_3.OptionalModelParameterRange.hidl_discriminator.getName(this.hidl_d) + "), and hidl_o is of type " + className + ".");
        }
        if (this.hidl_o != null && !android.hidl.safe_union.V1_0.Monostate.class.isInstance(this.hidl_o)) {
            throw new java.lang.Error("Union is in a corrupted state.");
        }
        return (android.hidl.safe_union.V1_0.Monostate) this.hidl_o;
    }

    public void range(android.hardware.soundtrigger.V2_3.ModelParameterRange range) {
        this.hidl_d = (byte) 1;
        this.hidl_o = range;
    }

    public android.hardware.soundtrigger.V2_3.ModelParameterRange range() {
        if (this.hidl_d != 1) {
            java.lang.String className = this.hidl_o != null ? this.hidl_o.getClass().getName() : "null";
            throw new java.lang.IllegalStateException("Read access to inactive union components is disallowed. Discriminator value is " + ((int) this.hidl_d) + " (corresponding to " + android.hardware.soundtrigger.V2_3.OptionalModelParameterRange.hidl_discriminator.getName(this.hidl_d) + "), and hidl_o is of type " + className + ".");
        }
        if (this.hidl_o != null && !android.hardware.soundtrigger.V2_3.ModelParameterRange.class.isInstance(this.hidl_o)) {
            throw new java.lang.Error("Union is in a corrupted state.");
        }
        return (android.hardware.soundtrigger.V2_3.ModelParameterRange) this.hidl_o;
    }

    public byte getDiscriminator() {
        return this.hidl_d;
    }

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hardware.soundtrigger.V2_3.OptionalModelParameterRange.class) {
            return false;
        }
        android.hardware.soundtrigger.V2_3.OptionalModelParameterRange other = (android.hardware.soundtrigger.V2_3.OptionalModelParameterRange) otherObject;
        if (this.hidl_d == other.hidl_d && android.os.HidlSupport.deepEquals(this.hidl_o, other.hidl_o)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(this.hidl_o)), java.lang.Integer.valueOf(java.util.Objects.hashCode(java.lang.Byte.valueOf(this.hidl_d))));
    }

    public final java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("{");
        switch (this.hidl_d) {
            case 0:
                builder.append(".noinit = ");
                builder.append(noinit());
                break;
            case 1:
                builder.append(".range = ");
                builder.append(range());
                break;
            default:
                throw new java.lang.Error("Unknown union discriminator (value: " + ((int) this.hidl_d) + ").");
        }
        builder.append("}");
        return builder.toString();
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(12L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hardware.soundtrigger.V2_3.OptionalModelParameterRange> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hardware.soundtrigger.V2_3.OptionalModelParameterRange> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 12, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hardware.soundtrigger.V2_3.OptionalModelParameterRange _hidl_vec_element = new android.hardware.soundtrigger.V2_3.OptionalModelParameterRange();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 12);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.hidl_d = _hidl_blob.getInt8(0 + _hidl_offset);
        switch (this.hidl_d) {
            case 0:
                this.hidl_o = new android.hidl.safe_union.V1_0.Monostate();
                ((android.hidl.safe_union.V1_0.Monostate) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 4 + _hidl_offset);
                return;
            case 1:
                this.hidl_o = new android.hardware.soundtrigger.V2_3.ModelParameterRange();
                ((android.hardware.soundtrigger.V2_3.ModelParameterRange) this.hidl_o).readEmbeddedFromParcel(parcel, _hidl_blob, 4 + _hidl_offset);
                return;
            default:
                throw new java.lang.IllegalStateException("Unknown union discriminator (value: " + ((int) this.hidl_d) + ").");
        }
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(12);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hardware.soundtrigger.V2_3.OptionalModelParameterRange> _hidl_vec) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 12);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 12);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(android.os.HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt8(0 + _hidl_offset, this.hidl_d);
        switch (this.hidl_d) {
            case 0:
                noinit().writeEmbeddedToBlob(_hidl_blob, 4 + _hidl_offset);
                return;
            case 1:
                range().writeEmbeddedToBlob(_hidl_blob, 4 + _hidl_offset);
                return;
            default:
                throw new java.lang.Error("Unknown union discriminator (value: " + ((int) this.hidl_d) + ").");
        }
    }
}
