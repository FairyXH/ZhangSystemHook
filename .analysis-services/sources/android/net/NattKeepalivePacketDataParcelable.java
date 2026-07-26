package android.net;

/* JADX INFO: loaded from: classes.dex */
public class NattKeepalivePacketDataParcelable implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.NattKeepalivePacketDataParcelable> CREATOR = new android.os.Parcelable.Creator<android.net.NattKeepalivePacketDataParcelable>() { // from class: android.net.NattKeepalivePacketDataParcelable.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.NattKeepalivePacketDataParcelable createFromParcel(android.os.Parcel _aidl_source) {
            android.net.NattKeepalivePacketDataParcelable _aidl_out = new android.net.NattKeepalivePacketDataParcelable();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.NattKeepalivePacketDataParcelable[] newArray(int _aidl_size) {
            return new android.net.NattKeepalivePacketDataParcelable[_aidl_size];
        }
    };
    public byte[] dstAddress;
    public byte[] srcAddress;
    public int srcPort = 0;
    public int dstPort = 0;

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeByteArray(this.srcAddress);
        _aidl_parcel.writeInt(this.srcPort);
        _aidl_parcel.writeByteArray(this.dstAddress);
        _aidl_parcel.writeInt(this.dstPort);
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
            this.srcAddress = _aidl_parcel.createByteArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.srcPort = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.dstAddress = _aidl_parcel.createByteArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.dstPort = _aidl_parcel.readInt();
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
        _aidl_sj.add("srcAddress: " + java.util.Arrays.toString(this.srcAddress));
        _aidl_sj.add("srcPort: " + this.srcPort);
        _aidl_sj.add("dstAddress: " + java.util.Arrays.toString(this.dstAddress));
        _aidl_sj.add("dstPort: " + this.dstPort);
        return "NattKeepalivePacketDataParcelable" + _aidl_sj.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
