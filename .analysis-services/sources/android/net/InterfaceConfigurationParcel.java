package android.net;

/* JADX INFO: loaded from: classes.dex */
public class InterfaceConfigurationParcel implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.InterfaceConfigurationParcel> CREATOR = new android.os.Parcelable.Creator<android.net.InterfaceConfigurationParcel>() { // from class: android.net.InterfaceConfigurationParcel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.InterfaceConfigurationParcel createFromParcel(android.os.Parcel _aidl_source) {
            android.net.InterfaceConfigurationParcel _aidl_out = new android.net.InterfaceConfigurationParcel();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.InterfaceConfigurationParcel[] newArray(int _aidl_size) {
            return new android.net.InterfaceConfigurationParcel[_aidl_size];
        }
    };
    public java.lang.String[] flags;
    public java.lang.String hwAddr;
    public java.lang.String ifName;
    public java.lang.String ipv4Addr;
    public int prefixLength = 0;

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeString(this.ifName);
        _aidl_parcel.writeString(this.hwAddr);
        _aidl_parcel.writeString(this.ipv4Addr);
        _aidl_parcel.writeInt(this.prefixLength);
        _aidl_parcel.writeStringArray(this.flags);
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
            this.ifName = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.hwAddr = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.ipv4Addr = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.prefixLength = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.flags = _aidl_parcel.createStringArray();
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
