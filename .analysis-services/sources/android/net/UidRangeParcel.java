package android.net;

/* JADX INFO: loaded from: classes.dex */
public class UidRangeParcel implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.UidRangeParcel> CREATOR = new android.os.Parcelable.Creator<android.net.UidRangeParcel>() { // from class: android.net.UidRangeParcel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.UidRangeParcel createFromParcel(android.os.Parcel _aidl_source) {
            return android.net.UidRangeParcel.internalCreateFromParcel(_aidl_source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.UidRangeParcel[] newArray(int _aidl_size) {
            return new android.net.UidRangeParcel[_aidl_size];
        }
    };
    public final int start;
    public final int stop;

    public static final class Builder {
        private int start = 0;
        private int stop = 0;

        public android.net.UidRangeParcel.Builder setStart(int start) {
            this.start = start;
            return this;
        }

        public android.net.UidRangeParcel.Builder setStop(int stop) {
            this.stop = stop;
            return this;
        }

        public android.net.UidRangeParcel build() {
            return new android.net.UidRangeParcel(this.start, this.stop);
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.start);
        _aidl_parcel.writeInt(this.stop);
        int _aidl_end_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.setDataPosition(_aidl_start_pos);
        _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
        _aidl_parcel.setDataPosition(_aidl_end_pos);
    }

    public UidRangeParcel(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.net.UidRangeParcel internalCreateFromParcel(android.os.Parcel _aidl_parcel) {
        int i;
        android.net.UidRangeParcel.Builder _aidl_parcelable_builder = new android.net.UidRangeParcel.Builder();
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
            int _aidl_temp_start = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setStart(_aidl_temp_start);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            int _aidl_temp_stop = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setStop(_aidl_temp_stop);
            if (_aidl_start_pos > i) {
                throw new android.os.BadParcelableException(str);
            }
        }
        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
        return _aidl_parcelable_builder.build();
    }

    public java.lang.String toString() {
        java.util.StringJoiner _aidl_sj = new java.util.StringJoiner(", ", "{", "}");
        _aidl_sj.add("start: " + this.start);
        _aidl_sj.add("stop: " + this.stop);
        return "UidRangeParcel" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.net.UidRangeParcel)) {
            return false;
        }
        android.net.UidRangeParcel that = (android.net.UidRangeParcel) other;
        if (java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.start), java.lang.Integer.valueOf(that.start)) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.stop), java.lang.Integer.valueOf(that.stop))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(java.lang.Integer.valueOf(this.start), java.lang.Integer.valueOf(this.stop)).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
