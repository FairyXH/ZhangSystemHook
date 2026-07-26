package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class VendorKeyValue implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.VendorKeyValue> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.VendorKeyValue>() { // from class: android.hardware.broadcastradio.VendorKeyValue.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.VendorKeyValue createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.VendorKeyValue _aidl_out = new android.hardware.broadcastradio.VendorKeyValue();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.VendorKeyValue[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.VendorKeyValue[_aidl_size];
        }
    };
    public java.lang.String key;
    public java.lang.String value;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeString(this.key);
        _aidl_parcel.writeString(this.value);
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
            this.key = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.value = _aidl_parcel.readString();
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
        _aidl_sj.add("key: " + java.util.Objects.toString(this.key));
        _aidl_sj.add("value: " + java.util.Objects.toString(this.value));
        return "VendorKeyValue" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.VendorKeyValue)) {
            return false;
        }
        android.hardware.broadcastradio.VendorKeyValue that = (android.hardware.broadcastradio.VendorKeyValue) other;
        if (java.util.Objects.deepEquals(this.key, that.key) && java.util.Objects.deepEquals(this.value, that.value)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.key, this.value).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
