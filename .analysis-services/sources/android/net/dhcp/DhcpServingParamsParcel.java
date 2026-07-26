package android.net.dhcp;

/* JADX INFO: loaded from: classes.dex */
public class DhcpServingParamsParcel implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.dhcp.DhcpServingParamsParcel> CREATOR = new android.os.Parcelable.Creator<android.net.dhcp.DhcpServingParamsParcel>() { // from class: android.net.dhcp.DhcpServingParamsParcel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.dhcp.DhcpServingParamsParcel createFromParcel(android.os.Parcel _aidl_source) {
            android.net.dhcp.DhcpServingParamsParcel _aidl_out = new android.net.dhcp.DhcpServingParamsParcel();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.dhcp.DhcpServingParamsParcel[] newArray(int _aidl_size) {
            return new android.net.dhcp.DhcpServingParamsParcel[_aidl_size];
        }
    };
    public int[] defaultRouters;
    public int[] dnsServers;
    public int[] excludedAddrs;
    public int serverAddr = 0;
    public int serverAddrPrefixLength = 0;
    public long dhcpLeaseTimeSecs = 0;
    public int linkMtu = 0;
    public boolean metered = false;
    public int singleClientAddr = 0;
    public boolean changePrefixOnDecline = false;
    public int leasesSubnetPrefixLength = 0;

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.serverAddr);
        _aidl_parcel.writeInt(this.serverAddrPrefixLength);
        _aidl_parcel.writeIntArray(this.defaultRouters);
        _aidl_parcel.writeIntArray(this.dnsServers);
        _aidl_parcel.writeIntArray(this.excludedAddrs);
        _aidl_parcel.writeLong(this.dhcpLeaseTimeSecs);
        _aidl_parcel.writeInt(this.linkMtu);
        _aidl_parcel.writeBoolean(this.metered);
        _aidl_parcel.writeInt(this.singleClientAddr);
        _aidl_parcel.writeBoolean(this.changePrefixOnDecline);
        _aidl_parcel.writeInt(this.leasesSubnetPrefixLength);
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
            this.serverAddr = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.serverAddrPrefixLength = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.defaultRouters = _aidl_parcel.createIntArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.dnsServers = _aidl_parcel.createIntArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.excludedAddrs = _aidl_parcel.createIntArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.dhcpLeaseTimeSecs = _aidl_parcel.readLong();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.linkMtu = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.metered = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.singleClientAddr = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.changePrefixOnDecline = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.leasesSubnetPrefixLength = _aidl_parcel.readInt();
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
        _aidl_sj.add("serverAddr: " + this.serverAddr);
        _aidl_sj.add("serverAddrPrefixLength: " + this.serverAddrPrefixLength);
        _aidl_sj.add("defaultRouters: " + java.util.Arrays.toString(this.defaultRouters));
        _aidl_sj.add("dnsServers: " + java.util.Arrays.toString(this.dnsServers));
        _aidl_sj.add("excludedAddrs: " + java.util.Arrays.toString(this.excludedAddrs));
        _aidl_sj.add("dhcpLeaseTimeSecs: " + this.dhcpLeaseTimeSecs);
        _aidl_sj.add("linkMtu: " + this.linkMtu);
        _aidl_sj.add("metered: " + this.metered);
        _aidl_sj.add("singleClientAddr: " + this.singleClientAddr);
        _aidl_sj.add("changePrefixOnDecline: " + this.changePrefixOnDecline);
        _aidl_sj.add("leasesSubnetPrefixLength: " + this.leasesSubnetPrefixLength);
        return "DhcpServingParamsParcel" + _aidl_sj.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
