package android.frameworks.vibrator;

/* JADX INFO: loaded from: classes.dex */
public final class VibrationParam implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.frameworks.vibrator.VibrationParam> CREATOR = new android.os.Parcelable.Creator<android.frameworks.vibrator.VibrationParam>() { // from class: android.frameworks.vibrator.VibrationParam.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.frameworks.vibrator.VibrationParam createFromParcel(android.os.Parcel _aidl_source) {
            return new android.frameworks.vibrator.VibrationParam(_aidl_source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.frameworks.vibrator.VibrationParam[] newArray(int _aidl_size) {
            return new android.frameworks.vibrator.VibrationParam[_aidl_size];
        }
    };
    public static final int scale = 0;
    private int _tag;
    private java.lang.Object _value;

    public @interface Tag {
        public static final int scale = 0;
    }

    public VibrationParam() {
        this._tag = 0;
        this._value = null;
    }

    private VibrationParam(android.os.Parcel _aidl_parcel) {
        readFromParcel(_aidl_parcel);
    }

    private VibrationParam(int _tag, java.lang.Object _value) {
        this._tag = _tag;
        this._value = _value;
    }

    public int getTag() {
        return this._tag;
    }

    public static android.frameworks.vibrator.VibrationParam scale(android.frameworks.vibrator.ScaleParam _value) {
        return new android.frameworks.vibrator.VibrationParam(0, _value);
    }

    public android.frameworks.vibrator.ScaleParam getScale() {
        _assertTag(0);
        return (android.frameworks.vibrator.ScaleParam) this._value;
    }

    public void setScale(android.frameworks.vibrator.ScaleParam _value) {
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
                _aidl_parcel.writeTypedObject(getScale(), _aidl_flag);
                break;
        }
    }

    public void readFromParcel(android.os.Parcel _aidl_parcel) {
        int _aidl_tag = _aidl_parcel.readInt();
        switch (_aidl_tag) {
            case 0:
                android.frameworks.vibrator.ScaleParam _aidl_value = (android.frameworks.vibrator.ScaleParam) _aidl_parcel.readTypedObject(android.frameworks.vibrator.ScaleParam.CREATOR);
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
                int _mask = 0 | describeContents(getScale());
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
                return "scale";
            default:
                throw new java.lang.IllegalStateException("unknown field: " + _tag);
        }
    }

    private void _set(int _tag, java.lang.Object _value) {
        this._tag = _tag;
        this._value = _value;
    }
}
