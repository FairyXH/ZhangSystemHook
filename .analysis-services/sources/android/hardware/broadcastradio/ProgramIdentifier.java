package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class ProgramIdentifier implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramIdentifier> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramIdentifier>() { // from class: android.hardware.broadcastradio.ProgramIdentifier.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramIdentifier createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.ProgramIdentifier _aidl_out = new android.hardware.broadcastradio.ProgramIdentifier();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramIdentifier[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.ProgramIdentifier[_aidl_size];
        }
    };
    public int type = 0;
    public long value = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.type);
        _aidl_parcel.writeLong(this.value);
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
            this.type = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.value = _aidl_parcel.readLong();
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
        _aidl_sj.add("type: " + android.hardware.broadcastradio.IdentifierType$$.toString(this.type));
        _aidl_sj.add("value: " + this.value);
        return "ProgramIdentifier" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.ProgramIdentifier)) {
            return false;
        }
        android.hardware.broadcastradio.ProgramIdentifier that = (android.hardware.broadcastradio.ProgramIdentifier) other;
        if (java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.type), java.lang.Integer.valueOf(that.type)) && java.util.Objects.deepEquals(java.lang.Long.valueOf(this.value), java.lang.Long.valueOf(that.value))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(java.lang.Integer.valueOf(this.type), java.lang.Long.valueOf(this.value)).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
