package android.hardware.usb;

/* JADX INFO: loaded from: classes.dex */
public final class PortRole implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.usb.PortRole> CREATOR = new android.os.Parcelable.Creator<android.hardware.usb.PortRole>() { // from class: android.hardware.usb.PortRole.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.usb.PortRole createFromParcel(android.os.Parcel _aidl_source) {
            return new android.hardware.usb.PortRole(_aidl_source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.usb.PortRole[] newArray(int _aidl_size) {
            return new android.hardware.usb.PortRole[_aidl_size];
        }
    };
    public static final int dataRole = 1;
    public static final int mode = 2;
    public static final int powerRole = 0;
    private int _tag;
    private java.lang.Object _value;

    public @interface Tag {
        public static final int dataRole = 1;
        public static final int mode = 2;
        public static final int powerRole = 0;
    }

    public PortRole() {
        this._tag = 0;
        this._value = (byte) 0;
    }

    private PortRole(android.os.Parcel _aidl_parcel) {
        readFromParcel(_aidl_parcel);
    }

    private PortRole(int _tag, java.lang.Object _value) {
        this._tag = _tag;
        this._value = _value;
    }

    public int getTag() {
        return this._tag;
    }

    public static android.hardware.usb.PortRole powerRole(byte _value) {
        return new android.hardware.usb.PortRole(0, java.lang.Byte.valueOf(_value));
    }

    public byte getPowerRole() {
        _assertTag(0);
        return ((java.lang.Byte) this._value).byteValue();
    }

    public void setPowerRole(byte _value) {
        _set(0, java.lang.Byte.valueOf(_value));
    }

    public static android.hardware.usb.PortRole dataRole(byte _value) {
        return new android.hardware.usb.PortRole(1, java.lang.Byte.valueOf(_value));
    }

    public byte getDataRole() {
        _assertTag(1);
        return ((java.lang.Byte) this._value).byteValue();
    }

    public void setDataRole(byte _value) {
        _set(1, java.lang.Byte.valueOf(_value));
    }

    public static android.hardware.usb.PortRole mode(byte _value) {
        return new android.hardware.usb.PortRole(2, java.lang.Byte.valueOf(_value));
    }

    public byte getMode() {
        _assertTag(2);
        return ((java.lang.Byte) this._value).byteValue();
    }

    public void setMode(byte _value) {
        _set(2, java.lang.Byte.valueOf(_value));
    }

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        _aidl_parcel.writeInt(this._tag);
        switch (this._tag) {
            case 0:
                _aidl_parcel.writeByte(getPowerRole());
                break;
            case 1:
                _aidl_parcel.writeByte(getDataRole());
                break;
            case 2:
                _aidl_parcel.writeByte(getMode());
                break;
        }
    }

    public void readFromParcel(android.os.Parcel _aidl_parcel) {
        int _aidl_tag = _aidl_parcel.readInt();
        switch (_aidl_tag) {
            case 0:
                byte _aidl_value = _aidl_parcel.readByte();
                _set(_aidl_tag, java.lang.Byte.valueOf(_aidl_value));
                return;
            case 1:
                byte _aidl_value2 = _aidl_parcel.readByte();
                _set(_aidl_tag, java.lang.Byte.valueOf(_aidl_value2));
                return;
            case 2:
                byte _aidl_value3 = _aidl_parcel.readByte();
                _set(_aidl_tag, java.lang.Byte.valueOf(_aidl_value3));
                return;
            default:
                throw new java.lang.IllegalArgumentException("union: unknown tag: " + _aidl_tag);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        getTag();
        return 0;
    }

    private void _assertTag(int tag) {
        if (getTag() != tag) {
            throw new java.lang.IllegalStateException("bad access: " + _tagString(tag) + ", " + _tagString(getTag()) + " is available.");
        }
    }

    private java.lang.String _tagString(int _tag) {
        switch (_tag) {
            case 0:
                return "powerRole";
            case 1:
                return "dataRole";
            case 2:
                return com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY;
            default:
                throw new java.lang.IllegalStateException("unknown field: " + _tag);
        }
    }

    private void _set(int _tag, java.lang.Object _value) {
        this._tag = _tag;
        this._value = _value;
    }
}
