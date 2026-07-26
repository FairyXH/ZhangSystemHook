package vendor.oplus.hardware.osense.client;

/* JADX INFO: loaded from: classes4.dex */
public class OsenseGpuControlData implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<vendor.oplus.hardware.osense.client.OsenseGpuControlData> CREATOR = new android.os.Parcelable.Creator<vendor.oplus.hardware.osense.client.OsenseGpuControlData>() { // from class: vendor.oplus.hardware.osense.client.OsenseGpuControlData.1
        @Override // android.os.Parcelable.Creator
        public vendor.oplus.hardware.osense.client.OsenseGpuControlData createFromParcel(android.os.Parcel _aidl_source) {
            vendor.oplus.hardware.osense.client.OsenseGpuControlData _aidl_out = new vendor.oplus.hardware.osense.client.OsenseGpuControlData();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        @Override // android.os.Parcelable.Creator
        public vendor.oplus.hardware.osense.client.OsenseGpuControlData[] newArray(int _aidl_size) {
            return new vendor.oplus.hardware.osense.client.OsenseGpuControlData[_aidl_size];
        }
    };
    public int control_type = 0;
    public vendor.oplus.hardware.osense.client.OsenseDataRange core;
    public vendor.oplus.hardware.osense.client.OsenseDataRange freq;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeTypedObject(this.core, _aidl_flag);
        _aidl_parcel.writeTypedObject(this.freq, _aidl_flag);
        _aidl_parcel.writeInt(this.control_type);
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
            this.core = (vendor.oplus.hardware.osense.client.OsenseDataRange) _aidl_parcel.readTypedObject(vendor.oplus.hardware.osense.client.OsenseDataRange.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.freq = (vendor.oplus.hardware.osense.client.OsenseDataRange) _aidl_parcel.readTypedObject(vendor.oplus.hardware.osense.client.OsenseDataRange.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.control_type = _aidl_parcel.readInt();
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
        int _mask = 0 | describeContents(this.core);
        return _mask | describeContents(this.freq);
    }

    private int describeContents(java.lang.Object _v) {
        if (_v == null || !(_v instanceof android.os.Parcelable)) {
            return 0;
        }
        return ((android.os.Parcelable) _v).describeContents();
    }
}
