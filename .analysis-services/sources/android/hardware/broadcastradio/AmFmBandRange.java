package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class AmFmBandRange implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.AmFmBandRange> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.AmFmBandRange>() { // from class: android.hardware.broadcastradio.AmFmBandRange.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.AmFmBandRange createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.AmFmBandRange _aidl_out = new android.hardware.broadcastradio.AmFmBandRange();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.AmFmBandRange[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.AmFmBandRange[_aidl_size];
        }
    };
    public int lowerBound = 0;
    public int upperBound = 0;
    public int spacing = 0;
    public int seekSpacing = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.lowerBound);
        _aidl_parcel.writeInt(this.upperBound);
        _aidl_parcel.writeInt(this.spacing);
        _aidl_parcel.writeInt(this.seekSpacing);
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
            this.lowerBound = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.upperBound = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.spacing = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.seekSpacing = _aidl_parcel.readInt();
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
        _aidl_sj.add("lowerBound: " + this.lowerBound);
        _aidl_sj.add("upperBound: " + this.upperBound);
        _aidl_sj.add("spacing: " + this.spacing);
        _aidl_sj.add("seekSpacing: " + this.seekSpacing);
        return "AmFmBandRange" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.AmFmBandRange)) {
            return false;
        }
        android.hardware.broadcastradio.AmFmBandRange that = (android.hardware.broadcastradio.AmFmBandRange) other;
        if (java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.lowerBound), java.lang.Integer.valueOf(that.lowerBound)) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.upperBound), java.lang.Integer.valueOf(that.upperBound)) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.spacing), java.lang.Integer.valueOf(that.spacing)) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.seekSpacing), java.lang.Integer.valueOf(that.seekSpacing))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(java.lang.Integer.valueOf(this.lowerBound), java.lang.Integer.valueOf(this.upperBound), java.lang.Integer.valueOf(this.spacing), java.lang.Integer.valueOf(this.seekSpacing)).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
