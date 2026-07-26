package android.net.networkstack.aidl.quirks;

/* JADX INFO: loaded from: classes.dex */
public class IPv6ProvisioningLossQuirkParcelable implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable> CREATOR = new android.os.Parcelable.Creator<android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable>() { // from class: android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable createFromParcel(android.os.Parcel _aidl_source) {
            android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable _aidl_out = new android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable[] newArray(int _aidl_size) {
            return new android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable[_aidl_size];
        }
    };
    public int detectionCount = 0;
    public long quirkExpiry = 0;

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.detectionCount);
        _aidl_parcel.writeLong(this.quirkExpiry);
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
            this.detectionCount = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.quirkExpiry = _aidl_parcel.readLong();
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
        _aidl_sj.add("detectionCount: " + this.detectionCount);
        _aidl_sj.add("quirkExpiry: " + this.quirkExpiry);
        return "IPv6ProvisioningLossQuirkParcelable" + _aidl_sj.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
