package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class AmFmRegionConfig implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.AmFmRegionConfig> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.AmFmRegionConfig>() { // from class: android.hardware.broadcastradio.AmFmRegionConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.AmFmRegionConfig createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.AmFmRegionConfig _aidl_out = new android.hardware.broadcastradio.AmFmRegionConfig();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.AmFmRegionConfig[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.AmFmRegionConfig[_aidl_size];
        }
    };
    public static final int DEEMPHASIS_D50 = 1;
    public static final int DEEMPHASIS_D75 = 2;
    public static final int RBDS = 2;
    public static final int RDS = 1;
    public int fmDeemphasis = 0;
    public int fmRds = 0;
    public android.hardware.broadcastradio.AmFmBandRange[] ranges;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeTypedArray(this.ranges, _aidl_flag);
        _aidl_parcel.writeInt(this.fmDeemphasis);
        _aidl_parcel.writeInt(this.fmRds);
        int _aidl_end_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.setDataPosition(_aidl_start_pos);
        _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
        _aidl_parcel.setDataPosition(_aidl_end_pos);
    }

    public final void readFromParcel(android.os.Parcel _aidl_parcel) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        int _aidl_parcelable_size = _aidl_parcel.readInt();
        try {
            if (_aidl_parcelable_size < 4) {
                throw new android.os.BadParcelableException("Parcelable too small");
            }
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.ranges = (android.hardware.broadcastradio.AmFmBandRange[]) _aidl_parcel.createTypedArray(android.hardware.broadcastradio.AmFmBandRange.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.fmDeemphasis = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.fmRds = _aidl_parcel.readInt();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            }
        } catch (java.lang.Throwable th) {
            if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                throw new android.os.BadParcelableException("Overflow in the size of parcelable");
            }
            _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            throw th;
        }
    }

    public java.lang.String toString() {
        java.util.StringJoiner _aidl_sj = new java.util.StringJoiner(", ", "{", "}");
        _aidl_sj.add("ranges: " + java.util.Arrays.toString(this.ranges));
        _aidl_sj.add("fmDeemphasis: " + this.fmDeemphasis);
        _aidl_sj.add("fmRds: " + this.fmRds);
        return "AmFmRegionConfig" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.AmFmRegionConfig)) {
            return false;
        }
        android.hardware.broadcastradio.AmFmRegionConfig that = (android.hardware.broadcastradio.AmFmRegionConfig) other;
        if (java.util.Objects.deepEquals(this.ranges, that.ranges) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.fmDeemphasis), java.lang.Integer.valueOf(that.fmDeemphasis)) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.fmRds), java.lang.Integer.valueOf(that.fmRds))) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.ranges, java.lang.Integer.valueOf(this.fmDeemphasis), java.lang.Integer.valueOf(this.fmRds)).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.ranges);
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
