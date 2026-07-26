package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public class SameL3NetworkResponseParcelable implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.ipmemorystore.SameL3NetworkResponseParcelable> CREATOR = new android.os.Parcelable.Creator<android.net.ipmemorystore.SameL3NetworkResponseParcelable>() { // from class: android.net.ipmemorystore.SameL3NetworkResponseParcelable.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.ipmemorystore.SameL3NetworkResponseParcelable createFromParcel(android.os.Parcel _aidl_source) {
            android.net.ipmemorystore.SameL3NetworkResponseParcelable _aidl_out = new android.net.ipmemorystore.SameL3NetworkResponseParcelable();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.ipmemorystore.SameL3NetworkResponseParcelable[] newArray(int _aidl_size) {
            return new android.net.ipmemorystore.SameL3NetworkResponseParcelable[_aidl_size];
        }
    };
    public float confidence = 0.0f;
    public java.lang.String l2Key1;
    public java.lang.String l2Key2;

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeString(this.l2Key1);
        _aidl_parcel.writeString(this.l2Key2);
        _aidl_parcel.writeFloat(this.confidence);
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
            this.l2Key1 = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.l2Key2 = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.confidence = _aidl_parcel.readFloat();
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
        _aidl_sj.add("l2Key1: " + java.util.Objects.toString(this.l2Key1));
        _aidl_sj.add("l2Key2: " + java.util.Objects.toString(this.l2Key2));
        _aidl_sj.add("confidence: " + this.confidence);
        return "SameL3NetworkResponseParcelable" + _aidl_sj.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
