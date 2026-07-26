package android.hardware.usb;

/* JADX INFO: loaded from: classes.dex */
public final class AltModeData implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.usb.AltModeData> CREATOR = new android.os.Parcelable.Creator<android.hardware.usb.AltModeData>() { // from class: android.hardware.usb.AltModeData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.usb.AltModeData createFromParcel(android.os.Parcel _aidl_source) {
            return new android.hardware.usb.AltModeData(_aidl_source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.usb.AltModeData[] newArray(int _aidl_size) {
            return new android.hardware.usb.AltModeData[_aidl_size];
        }
    };
    public static final int displayPortAltModeData = 0;
    private int _tag;
    private java.lang.Object _value;

    public @interface Tag {
        public static final int displayPortAltModeData = 0;
    }

    public AltModeData() {
        this._tag = 0;
        this._value = null;
    }

    private AltModeData(android.os.Parcel _aidl_parcel) {
        readFromParcel(_aidl_parcel);
    }

    private AltModeData(int _tag, java.lang.Object _value) {
        this._tag = _tag;
        this._value = _value;
    }

    public int getTag() {
        return this._tag;
    }

    public static android.hardware.usb.AltModeData displayPortAltModeData(android.hardware.usb.AltModeData.DisplayPortAltModeData _value) {
        return new android.hardware.usb.AltModeData(0, _value);
    }

    public android.hardware.usb.AltModeData.DisplayPortAltModeData getDisplayPortAltModeData() {
        _assertTag(0);
        return (android.hardware.usb.AltModeData.DisplayPortAltModeData) this._value;
    }

    public void setDisplayPortAltModeData(android.hardware.usb.AltModeData.DisplayPortAltModeData _value) {
        _set(0, _value);
    }

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        _aidl_parcel.writeInt(this._tag);
        switch (this._tag) {
            case 0:
                _aidl_parcel.writeTypedObject(getDisplayPortAltModeData(), _aidl_flag);
                break;
        }
    }

    public void readFromParcel(android.os.Parcel _aidl_parcel) {
        int _aidl_tag = _aidl_parcel.readInt();
        switch (_aidl_tag) {
            case 0:
                android.hardware.usb.AltModeData.DisplayPortAltModeData _aidl_value = (android.hardware.usb.AltModeData.DisplayPortAltModeData) _aidl_parcel.readTypedObject(android.hardware.usb.AltModeData.DisplayPortAltModeData.CREATOR);
                _set(_aidl_tag, _aidl_value);
                return;
            default:
                throw new java.lang.IllegalArgumentException("union: unknown tag: " + _aidl_tag);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        switch (getTag()) {
            case 0:
                int _mask = 0 | describeContents(getDisplayPortAltModeData());
                return _mask;
            default:
                return 0;
        }
    }

    private int describeContents(java.lang.Object _v) {
        if (_v == null || !(_v instanceof android.os.Parcelable)) {
            return 0;
        }
        return ((android.os.Parcelable) _v).describeContents();
    }

    private void _assertTag(int tag) {
        if (getTag() != tag) {
            throw new java.lang.IllegalStateException("bad access: " + _tagString(tag) + ", " + _tagString(getTag()) + " is available.");
        }
    }

    private java.lang.String _tagString(int _tag) {
        switch (_tag) {
            case 0:
                return "displayPortAltModeData";
            default:
                throw new java.lang.IllegalStateException("unknown field: " + _tag);
        }
    }

    private void _set(int _tag, java.lang.Object _value) {
        this._tag = _tag;
        this._value = _value;
    }

    public static class DisplayPortAltModeData implements android.os.Parcelable {
        public static final android.os.Parcelable.Creator<android.hardware.usb.AltModeData.DisplayPortAltModeData> CREATOR = new android.os.Parcelable.Creator<android.hardware.usb.AltModeData.DisplayPortAltModeData>() { // from class: android.hardware.usb.AltModeData.DisplayPortAltModeData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public android.hardware.usb.AltModeData.DisplayPortAltModeData createFromParcel(android.os.Parcel _aidl_source) {
                android.hardware.usb.AltModeData.DisplayPortAltModeData _aidl_out = new android.hardware.usb.AltModeData.DisplayPortAltModeData();
                _aidl_out.readFromParcel(_aidl_source);
                return _aidl_out;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public android.hardware.usb.AltModeData.DisplayPortAltModeData[] newArray(int _aidl_size) {
                return new android.hardware.usb.AltModeData.DisplayPortAltModeData[_aidl_size];
            }
        };
        public int partnerSinkStatus = 0;
        public int cableStatus = 0;
        public int pinAssignment = 0;
        public boolean hpd = false;
        public int linkTrainingStatus = 0;

        public final int getStability() {
            return 1;
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
            int _aidl_start_pos = _aidl_parcel.dataPosition();
            _aidl_parcel.writeInt(0);
            _aidl_parcel.writeInt(this.partnerSinkStatus);
            _aidl_parcel.writeInt(this.cableStatus);
            _aidl_parcel.writeInt(this.pinAssignment);
            _aidl_parcel.writeBoolean(this.hpd);
            _aidl_parcel.writeInt(this.linkTrainingStatus);
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
                this.partnerSinkStatus = _aidl_parcel.readInt();
                if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                    if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                        throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                    }
                    _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                    return;
                }
                this.cableStatus = _aidl_parcel.readInt();
                if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                    if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                        throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                    }
                    _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                    return;
                }
                this.pinAssignment = _aidl_parcel.readInt();
                if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                    if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                        throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                    }
                    _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                    return;
                }
                this.hpd = _aidl_parcel.readBoolean();
                if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                    if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                        throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                    }
                    _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                } else {
                    this.linkTrainingStatus = _aidl_parcel.readInt();
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
}
