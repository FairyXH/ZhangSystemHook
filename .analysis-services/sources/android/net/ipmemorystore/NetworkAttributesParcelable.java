package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public class NetworkAttributesParcelable implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.ipmemorystore.NetworkAttributesParcelable> CREATOR = new android.os.Parcelable.Creator<android.net.ipmemorystore.NetworkAttributesParcelable>() { // from class: android.net.ipmemorystore.NetworkAttributesParcelable.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.ipmemorystore.NetworkAttributesParcelable createFromParcel(android.os.Parcel _aidl_source) {
            android.net.ipmemorystore.NetworkAttributesParcelable _aidl_out = new android.net.ipmemorystore.NetworkAttributesParcelable();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.ipmemorystore.NetworkAttributesParcelable[] newArray(int _aidl_size) {
            return new android.net.ipmemorystore.NetworkAttributesParcelable[_aidl_size];
        }
    };
    public byte[] assignedV4Address;
    public java.lang.String cluster;
    public android.net.ipmemorystore.Blob[] dnsAddresses;
    public android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable ipv6ProvisioningLossQuirk;
    public long assignedV4AddressExpiry = 0;
    public int mtu = 0;

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeByteArray(this.assignedV4Address);
        _aidl_parcel.writeLong(this.assignedV4AddressExpiry);
        _aidl_parcel.writeString(this.cluster);
        _aidl_parcel.writeTypedArray(this.dnsAddresses, _aidl_flag);
        _aidl_parcel.writeInt(this.mtu);
        _aidl_parcel.writeTypedObject(this.ipv6ProvisioningLossQuirk, _aidl_flag);
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
            this.assignedV4Address = _aidl_parcel.createByteArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.assignedV4AddressExpiry = _aidl_parcel.readLong();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.cluster = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.dnsAddresses = (android.net.ipmemorystore.Blob[]) _aidl_parcel.createTypedArray(android.net.ipmemorystore.Blob.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.mtu = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.ipv6ProvisioningLossQuirk = (android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable) _aidl_parcel.readTypedObject(android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable.CREATOR);
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
        _aidl_sj.add("assignedV4Address: " + java.util.Arrays.toString(this.assignedV4Address));
        _aidl_sj.add("assignedV4AddressExpiry: " + this.assignedV4AddressExpiry);
        _aidl_sj.add("cluster: " + java.util.Objects.toString(this.cluster));
        _aidl_sj.add("dnsAddresses: " + java.util.Arrays.toString(this.dnsAddresses));
        _aidl_sj.add("mtu: " + this.mtu);
        _aidl_sj.add("ipv6ProvisioningLossQuirk: " + java.util.Objects.toString(this.ipv6ProvisioningLossQuirk));
        return "NetworkAttributesParcelable" + _aidl_sj.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.dnsAddresses);
        return _mask | describeContents(this.ipv6ProvisioningLossQuirk);
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
