package android.hidl.base.V1_0;

/* JADX INFO: loaded from: classes.dex */
public final class DebugInfo {
    public int pid = 0;
    public long ptr = 0;
    public int arch = 0;

    public static final class Architecture {
        public static final int IS_32BIT = 2;
        public static final int IS_64BIT = 1;
        public static final int UNKNOWN = 0;

        public static final java.lang.String toString(int o) {
            if (o == 0) {
                return "UNKNOWN";
            }
            if (o == 1) {
                return "IS_64BIT";
            }
            if (o == 2) {
                return "IS_32BIT";
            }
            return "0x" + java.lang.Integer.toHexString(o);
        }

        public static final java.lang.String dumpBitfield(int o) {
            java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
            int flipped = 0;
            list.add("UNKNOWN");
            if ((o & 1) == 1) {
                list.add("IS_64BIT");
                flipped = 0 | 1;
            }
            if ((o & 2) == 2) {
                list.add("IS_32BIT");
                flipped |= 2;
            }
            if (o != flipped) {
                list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
            }
            return java.lang.String.join(" | ", list);
        }
    }

    public final boolean equals(java.lang.Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != android.hidl.base.V1_0.DebugInfo.class) {
            return false;
        }
        android.hidl.base.V1_0.DebugInfo other = (android.hidl.base.V1_0.DebugInfo) otherObject;
        if (this.pid == other.pid && this.ptr == other.ptr && this.arch == other.arch) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.pid))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Long.valueOf(this.ptr))), java.lang.Integer.valueOf(android.os.HidlSupport.deepHashCode(java.lang.Integer.valueOf(this.arch))));
    }

    public final java.lang.String toString() {
        return "{.pid = " + this.pid + ", .ptr = " + this.ptr + ", .arch = " + android.hidl.base.V1_0.DebugInfo.Architecture.toString(this.arch) + "}";
    }

    public final void readFromParcel(android.os.HwParcel parcel) {
        android.os.HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final java.util.ArrayList<android.hidl.base.V1_0.DebugInfo> readVectorFromParcel(android.os.HwParcel parcel) {
        java.util.ArrayList<android.hidl.base.V1_0.DebugInfo> _hidl_vec = new java.util.ArrayList<>();
        android.os.HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        android.os.HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            android.hidl.base.V1_0.DebugInfo _hidl_vec_element = new android.hidl.base.V1_0.DebugInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(android.os.HwParcel parcel, android.os.HwBlob _hidl_blob, long _hidl_offset) {
        this.pid = _hidl_blob.getInt32(0 + _hidl_offset);
        this.ptr = _hidl_blob.getInt64(8 + _hidl_offset);
        this.arch = _hidl_blob.getInt32(16 + _hidl_offset);
    }

    public final void writeToParcel(android.os.HwParcel parcel) {
        android.os.HwBlob _hidl_blob = new android.os.HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(android.os.HwParcel parcel, java.util.ArrayList<android.hidl.base.V1_0.DebugInfo> _hidl_vec) {
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
        _hidl_blob.putInt32(0 + _hidl_offset, this.pid);
        _hidl_blob.putInt64(8 + _hidl_offset, this.ptr);
        _hidl_blob.putInt32(16 + _hidl_offset, this.arch);
    }
}
