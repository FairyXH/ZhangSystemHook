package vendor.pixelworks.hardware.display;

/* JADX INFO: loaded from: classes4.dex */
public class LayerStack implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<vendor.pixelworks.hardware.display.LayerStack> CREATOR = new android.os.Parcelable.Creator<vendor.pixelworks.hardware.display.LayerStack>() { // from class: vendor.pixelworks.hardware.display.LayerStack.1
        @Override // android.os.Parcelable.Creator
        public vendor.pixelworks.hardware.display.LayerStack createFromParcel(android.os.Parcel _aidl_source) {
            vendor.pixelworks.hardware.display.LayerStack _aidl_out = new vendor.pixelworks.hardware.display.LayerStack();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        @Override // android.os.Parcelable.Creator
        public vendor.pixelworks.hardware.display.LayerStack[] newArray(int _aidl_size) {
            return new vendor.pixelworks.hardware.display.LayerStack[_aidl_size];
        }
    };
    public int layerStackFlags = 0;
    public vendor.pixelworks.hardware.display.Layer[] layers;
    public int[] reserved;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeTypedArray(this.layers, _aidl_flag);
        _aidl_parcel.writeInt(this.layerStackFlags);
        _aidl_parcel.writeIntArray(this.reserved);
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
            this.layers = (vendor.pixelworks.hardware.display.Layer[]) _aidl_parcel.createTypedArray(vendor.pixelworks.hardware.display.Layer.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.layerStackFlags = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.reserved = _aidl_parcel.createIntArray();
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
        int _mask = 0 | describeContents(this.layers);
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
