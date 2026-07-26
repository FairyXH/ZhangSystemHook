package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class Properties implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.Properties> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.Properties>() { // from class: android.hardware.broadcastradio.Properties.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.Properties createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.Properties _aidl_out = new android.hardware.broadcastradio.Properties();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.Properties[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.Properties[_aidl_size];
        }
    };
    public java.lang.String maker;
    public java.lang.String product;
    public java.lang.String serial;
    public int[] supportedIdentifierTypes;
    public android.hardware.broadcastradio.VendorKeyValue[] vendorInfo;
    public java.lang.String version;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeString(this.maker);
        _aidl_parcel.writeString(this.product);
        _aidl_parcel.writeString(this.version);
        _aidl_parcel.writeString(this.serial);
        _aidl_parcel.writeIntArray(this.supportedIdentifierTypes);
        _aidl_parcel.writeTypedArray(this.vendorInfo, _aidl_flag);
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
            this.maker = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.product = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.version = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.serial = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.supportedIdentifierTypes = _aidl_parcel.createIntArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.vendorInfo = (android.hardware.broadcastradio.VendorKeyValue[]) _aidl_parcel.createTypedArray(android.hardware.broadcastradio.VendorKeyValue.CREATOR);
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
        _aidl_sj.add("maker: " + java.util.Objects.toString(this.maker));
        _aidl_sj.add("product: " + java.util.Objects.toString(this.product));
        _aidl_sj.add("version: " + java.util.Objects.toString(this.version));
        _aidl_sj.add("serial: " + java.util.Objects.toString(this.serial));
        _aidl_sj.add("supportedIdentifierTypes: " + android.hardware.broadcastradio.IdentifierType$$.arrayToString(this.supportedIdentifierTypes));
        _aidl_sj.add("vendorInfo: " + java.util.Arrays.toString(this.vendorInfo));
        return "Properties" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.Properties)) {
            return false;
        }
        android.hardware.broadcastradio.Properties that = (android.hardware.broadcastradio.Properties) other;
        if (java.util.Objects.deepEquals(this.maker, that.maker) && java.util.Objects.deepEquals(this.product, that.product) && java.util.Objects.deepEquals(this.version, that.version) && java.util.Objects.deepEquals(this.serial, that.serial) && java.util.Objects.deepEquals(this.supportedIdentifierTypes, that.supportedIdentifierTypes) && java.util.Objects.deepEquals(this.vendorInfo, that.vendorInfo)) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.maker, this.product, this.version, this.serial, this.supportedIdentifierTypes, this.vendorInfo).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.vendorInfo);
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
