package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class Announcement implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.Announcement> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.Announcement>() { // from class: android.hardware.broadcastradio.Announcement.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.Announcement createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.Announcement _aidl_out = new android.hardware.broadcastradio.Announcement();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.Announcement[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.Announcement[_aidl_size];
        }
    };
    public android.hardware.broadcastradio.ProgramSelector selector;
    public byte type = 0;
    public android.hardware.broadcastradio.VendorKeyValue[] vendorInfo;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeTypedObject(this.selector, _aidl_flag);
        _aidl_parcel.writeByte(this.type);
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
            this.selector = (android.hardware.broadcastradio.ProgramSelector) _aidl_parcel.readTypedObject(android.hardware.broadcastradio.ProgramSelector.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.type = _aidl_parcel.readByte();
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
        _aidl_sj.add("selector: " + java.util.Objects.toString(this.selector));
        _aidl_sj.add("type: " + android.hardware.broadcastradio.AnnouncementType$$.toString(this.type));
        _aidl_sj.add("vendorInfo: " + java.util.Arrays.toString(this.vendorInfo));
        return "Announcement" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.Announcement)) {
            return false;
        }
        android.hardware.broadcastradio.Announcement that = (android.hardware.broadcastradio.Announcement) other;
        if (java.util.Objects.deepEquals(this.selector, that.selector) && java.util.Objects.deepEquals(java.lang.Byte.valueOf(this.type), java.lang.Byte.valueOf(that.type)) && java.util.Objects.deepEquals(this.vendorInfo, that.vendorInfo)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.selector, java.lang.Byte.valueOf(this.type), this.vendorInfo).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.selector);
        return _mask | describeContents(this.vendorInfo);
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
