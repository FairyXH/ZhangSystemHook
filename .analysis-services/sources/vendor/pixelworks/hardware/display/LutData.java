package vendor.pixelworks.hardware.display;

/* JADX INFO: loaded from: classes4.dex */
public class LutData implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<vendor.pixelworks.hardware.display.LutData> CREATOR = new android.os.Parcelable.Creator<vendor.pixelworks.hardware.display.LutData>() { // from class: vendor.pixelworks.hardware.display.LutData.1
        @Override // android.os.Parcelable.Creator
        public vendor.pixelworks.hardware.display.LutData createFromParcel(android.os.Parcel _aidl_source) {
            vendor.pixelworks.hardware.display.LutData _aidl_out = new vendor.pixelworks.hardware.display.LutData();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        @Override // android.os.Parcelable.Creator
        public vendor.pixelworks.hardware.display.LutData[] newArray(int _aidl_size) {
            return new vendor.pixelworks.hardware.display.LutData[_aidl_size];
        }
    };
    public int[] gridEntries;
    public int[] lutEntries;
    public int dim = 0;
    public int gridSize = 0;
    public boolean validLutEntries = false;
    public boolean validGridEntries = false;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.dim);
        _aidl_parcel.writeInt(this.gridSize);
        _aidl_parcel.writeIntArray(this.lutEntries);
        _aidl_parcel.writeBoolean(this.validLutEntries);
        _aidl_parcel.writeIntArray(this.gridEntries);
        _aidl_parcel.writeBoolean(this.validGridEntries);
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
            this.dim = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.gridSize = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.lutEntries = _aidl_parcel.createIntArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.validLutEntries = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.gridEntries = _aidl_parcel.createIntArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.validGridEntries = _aidl_parcel.readBoolean();
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
