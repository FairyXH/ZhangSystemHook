package android.hardware.security.authgraph;

/* JADX INFO: loaded from: classes.dex */
public final class PubKey implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.security.authgraph.PubKey> CREATOR = new android.os.Parcelable.Creator<android.hardware.security.authgraph.PubKey>() { // from class: android.hardware.security.authgraph.PubKey.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.security.authgraph.PubKey createFromParcel(android.os.Parcel _aidl_source) {
            return new android.hardware.security.authgraph.PubKey(_aidl_source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.security.authgraph.PubKey[] newArray(int _aidl_size) {
            return new android.hardware.security.authgraph.PubKey[_aidl_size];
        }
    };
    public static final int plainKey = 0;
    public static final int signedKey = 1;
    private int _tag;
    private java.lang.Object _value;

    public @interface Tag {
        public static final int plainKey = 0;
        public static final int signedKey = 1;
    }

    public PubKey() {
        this._tag = 0;
        this._value = null;
    }

    private PubKey(android.os.Parcel _aidl_parcel) {
        readFromParcel(_aidl_parcel);
    }

    private PubKey(int _tag, java.lang.Object _value) {
        this._tag = _tag;
        this._value = _value;
    }

    public int getTag() {
        return this._tag;
    }

    public static android.hardware.security.authgraph.PubKey plainKey(android.hardware.security.authgraph.PlainPubKey _value) {
        return new android.hardware.security.authgraph.PubKey(0, _value);
    }

    public android.hardware.security.authgraph.PlainPubKey getPlainKey() {
        _assertTag(0);
        return (android.hardware.security.authgraph.PlainPubKey) this._value;
    }

    public void setPlainKey(android.hardware.security.authgraph.PlainPubKey _value) {
        _set(0, _value);
    }

    public static android.hardware.security.authgraph.PubKey signedKey(android.hardware.security.authgraph.SignedPubKey _value) {
        return new android.hardware.security.authgraph.PubKey(1, _value);
    }

    public android.hardware.security.authgraph.SignedPubKey getSignedKey() {
        _assertTag(1);
        return (android.hardware.security.authgraph.SignedPubKey) this._value;
    }

    public void setSignedKey(android.hardware.security.authgraph.SignedPubKey _value) {
        _set(1, _value);
    }

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        _aidl_parcel.writeInt(this._tag);
        switch (this._tag) {
            case 0:
                _aidl_parcel.writeTypedObject(getPlainKey(), _aidl_flag);
                break;
            case 1:
                _aidl_parcel.writeTypedObject(getSignedKey(), _aidl_flag);
                break;
        }
    }

    public void readFromParcel(android.os.Parcel _aidl_parcel) {
        int _aidl_tag = _aidl_parcel.readInt();
        switch (_aidl_tag) {
            case 0:
                android.hardware.security.authgraph.PlainPubKey _aidl_value = (android.hardware.security.authgraph.PlainPubKey) _aidl_parcel.readTypedObject(android.hardware.security.authgraph.PlainPubKey.CREATOR);
                _set(_aidl_tag, _aidl_value);
                return;
            case 1:
                android.hardware.security.authgraph.SignedPubKey _aidl_value2 = (android.hardware.security.authgraph.SignedPubKey) _aidl_parcel.readTypedObject(android.hardware.security.authgraph.SignedPubKey.CREATOR);
                _set(_aidl_tag, _aidl_value2);
                return;
            default:
                throw new java.lang.IllegalArgumentException("union: unknown tag: " + _aidl_tag);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        switch (getTag()) {
            case 0:
                int _mask = 0 | describeContents(getPlainKey());
                return _mask;
            case 1:
                int _mask2 = 0 | describeContents(getSignedKey());
                return _mask2;
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
                return "plainKey";
            case 1:
                return "signedKey";
            default:
                throw new java.lang.IllegalStateException("unknown field: " + _tag);
        }
    }

    private void _set(int _tag, java.lang.Object _value) {
        this._tag = _tag;
        this._value = _value;
    }
}
