package vendor.oplus.hardware.osense.client;

/* JADX INFO: loaded from: classes4.dex */
public class OsenseControlInfo implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<vendor.oplus.hardware.osense.client.OsenseControlInfo> CREATOR = new android.os.Parcelable.Creator<vendor.oplus.hardware.osense.client.OsenseControlInfo>() { // from class: vendor.oplus.hardware.osense.client.OsenseControlInfo.1
        @Override // android.os.Parcelable.Creator
        public vendor.oplus.hardware.osense.client.OsenseControlInfo createFromParcel(android.os.Parcel _aidl_source) {
            vendor.oplus.hardware.osense.client.OsenseControlInfo _aidl_out = new vendor.oplus.hardware.osense.client.OsenseControlInfo();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        @Override // android.os.Parcelable.Creator
        public vendor.oplus.hardware.osense.client.OsenseControlInfo[] newArray(int _aidl_size) {
            return new vendor.oplus.hardware.osense.client.OsenseControlInfo[_aidl_size];
        }
    };
    public vendor.oplus.hardware.osense.client.OsenseCpuControlData[] cpuParam1;
    public vendor.oplus.hardware.osense.client.OsenseCpuMIGData[] cpu_mig_data;
    public vendor.oplus.hardware.osense.client.OsenseGpuControlData[] param1;
    public int cpu_cluster_num = 0;
    public int gpu_cluster_num = 0;
    public int control_mask = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.cpu_cluster_num);
        _aidl_parcel.writeInt(this.gpu_cluster_num);
        _aidl_parcel.writeTypedArray(this.cpuParam1, _aidl_flag);
        _aidl_parcel.writeTypedArray(this.param1, _aidl_flag);
        _aidl_parcel.writeTypedArray(this.cpu_mig_data, _aidl_flag);
        _aidl_parcel.writeInt(this.control_mask);
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
            this.cpu_cluster_num = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.gpu_cluster_num = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.cpuParam1 = (vendor.oplus.hardware.osense.client.OsenseCpuControlData[]) _aidl_parcel.createTypedArray(vendor.oplus.hardware.osense.client.OsenseCpuControlData.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.param1 = (vendor.oplus.hardware.osense.client.OsenseGpuControlData[]) _aidl_parcel.createTypedArray(vendor.oplus.hardware.osense.client.OsenseGpuControlData.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.cpu_mig_data = (vendor.oplus.hardware.osense.client.OsenseCpuMIGData[]) _aidl_parcel.createTypedArray(vendor.oplus.hardware.osense.client.OsenseCpuMIGData.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.control_mask = _aidl_parcel.readInt();
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
        int _mask = 0 | describeContents(this.cpuParam1);
        return _mask | describeContents(this.param1) | describeContents(this.cpu_mig_data);
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
