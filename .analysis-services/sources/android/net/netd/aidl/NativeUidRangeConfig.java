package android.net.netd.aidl;

/* JADX INFO: loaded from: classes.dex */
public class NativeUidRangeConfig implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.netd.aidl.NativeUidRangeConfig> CREATOR = new android.os.Parcelable.Creator<android.net.netd.aidl.NativeUidRangeConfig>() { // from class: android.net.netd.aidl.NativeUidRangeConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.netd.aidl.NativeUidRangeConfig createFromParcel(android.os.Parcel _aidl_source) {
            return android.net.netd.aidl.NativeUidRangeConfig.internalCreateFromParcel(_aidl_source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.netd.aidl.NativeUidRangeConfig[] newArray(int _aidl_size) {
            return new android.net.netd.aidl.NativeUidRangeConfig[_aidl_size];
        }
    };
    public final int netId;
    public final int subPriority;
    public final android.net.UidRangeParcel[] uidRanges;

    public static final class Builder {
        private int netId = 0;
        private int subPriority = 0;
        private android.net.UidRangeParcel[] uidRanges;

        public android.net.netd.aidl.NativeUidRangeConfig.Builder setNetId(int netId) {
            this.netId = netId;
            return this;
        }

        public android.net.netd.aidl.NativeUidRangeConfig.Builder setUidRanges(android.net.UidRangeParcel[] uidRanges) {
            this.uidRanges = uidRanges;
            return this;
        }

        public android.net.netd.aidl.NativeUidRangeConfig.Builder setSubPriority(int subPriority) {
            this.subPriority = subPriority;
            return this;
        }

        public android.net.netd.aidl.NativeUidRangeConfig build() {
            return new android.net.netd.aidl.NativeUidRangeConfig(this.netId, this.uidRanges, this.subPriority);
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.netId);
        _aidl_parcel.writeTypedArray(this.uidRanges, _aidl_flag);
        _aidl_parcel.writeInt(this.subPriority);
        int _aidl_end_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.setDataPosition(_aidl_start_pos);
        _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
        _aidl_parcel.setDataPosition(_aidl_end_pos);
    }

    public NativeUidRangeConfig(int netId, android.net.UidRangeParcel[] uidRanges, int subPriority) {
        this.netId = netId;
        this.uidRanges = uidRanges;
        this.subPriority = subPriority;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.net.netd.aidl.NativeUidRangeConfig internalCreateFromParcel(android.os.Parcel _aidl_parcel) {
        int i;
        android.net.netd.aidl.NativeUidRangeConfig.Builder _aidl_parcelable_builder = new android.net.netd.aidl.NativeUidRangeConfig.Builder();
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        int _aidl_parcelable_size = _aidl_parcel.readInt();
        try {
        } finally {
            if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                android.os.BadParcelableException badParcelableException = new android.os.BadParcelableException("Overflow in the size of parcelable");
            }
        }
        if (_aidl_parcelable_size < 4) {
            throw new android.os.BadParcelableException("Parcelable too small");
        }
        _aidl_parcelable_builder.build();
        if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
            _aidl_parcelable_builder.build();
            if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                throw new android.os.BadParcelableException("Overflow in the size of parcelable");
            }
        } else {
            int _aidl_temp_netId = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setNetId(_aidl_temp_netId);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            android.net.UidRangeParcel[] _aidl_temp_uidRanges = (android.net.UidRangeParcel[]) _aidl_parcel.createTypedArray(android.net.UidRangeParcel.CREATOR);
            _aidl_parcelable_builder.setUidRanges(_aidl_temp_uidRanges);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            int _aidl_temp_subPriority = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setSubPriority(_aidl_temp_subPriority);
            if (_aidl_start_pos > i) {
                throw new android.os.BadParcelableException(str);
            }
        }
        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
        return _aidl_parcelable_builder.build();
    }

    public java.lang.String toString() {
        java.util.StringJoiner _aidl_sj = new java.util.StringJoiner(", ", "{", "}");
        _aidl_sj.add("netId: " + this.netId);
        _aidl_sj.add("uidRanges: " + java.util.Arrays.toString(this.uidRanges));
        _aidl_sj.add("subPriority: " + this.subPriority);
        return "NativeUidRangeConfig" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.net.netd.aidl.NativeUidRangeConfig)) {
            return false;
        }
        android.net.netd.aidl.NativeUidRangeConfig that = (android.net.netd.aidl.NativeUidRangeConfig) other;
        if (java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.netId), java.lang.Integer.valueOf(that.netId)) && java.util.Objects.deepEquals(this.uidRanges, that.uidRanges) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.subPriority), java.lang.Integer.valueOf(that.subPriority))) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(java.lang.Integer.valueOf(this.netId), this.uidRanges, java.lang.Integer.valueOf(this.subPriority)).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.uidRanges);
        return _mask;
    }

    private int describeContents(java.lang.Object _v) {
        if (_v == null) {
            return 0;
        }
        if (_v instanceof java.lang.Object[]) {
            int _mask = 0;
            for (java.lang.Object o : (java.lang.Object[]) _v) {
                _mask |= describeContents(o);
            }
            return _mask;
        }
        if (!(_v instanceof android.os.Parcelable)) {
            return 0;
        }
        return ((android.os.Parcelable) _v).describeContents();
    }
}
