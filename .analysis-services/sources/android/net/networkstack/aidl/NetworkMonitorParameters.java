package android.net.networkstack.aidl;

/* JADX INFO: loaded from: classes.dex */
public class NetworkMonitorParameters implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.networkstack.aidl.NetworkMonitorParameters> CREATOR = new android.os.Parcelable.Creator<android.net.networkstack.aidl.NetworkMonitorParameters>() { // from class: android.net.networkstack.aidl.NetworkMonitorParameters.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.networkstack.aidl.NetworkMonitorParameters createFromParcel(android.os.Parcel _aidl_source) {
            android.net.networkstack.aidl.NetworkMonitorParameters _aidl_out = new android.net.networkstack.aidl.NetworkMonitorParameters();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.networkstack.aidl.NetworkMonitorParameters[] newArray(int _aidl_size) {
            return new android.net.networkstack.aidl.NetworkMonitorParameters[_aidl_size];
        }
    };
    public android.net.LinkProperties linkProperties;
    public android.net.NetworkAgentConfig networkAgentConfig;
    public android.net.NetworkCapabilities networkCapabilities;

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeTypedObject(this.networkAgentConfig, _aidl_flag);
        _aidl_parcel.writeTypedObject(this.networkCapabilities, _aidl_flag);
        _aidl_parcel.writeTypedObject(this.linkProperties, _aidl_flag);
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
            this.networkAgentConfig = (android.net.NetworkAgentConfig) _aidl_parcel.readTypedObject(android.net.NetworkAgentConfig.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.networkCapabilities = (android.net.NetworkCapabilities) _aidl_parcel.readTypedObject(android.net.NetworkCapabilities.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.linkProperties = (android.net.LinkProperties) _aidl_parcel.readTypedObject(android.net.LinkProperties.CREATOR);
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
        _aidl_sj.add("networkAgentConfig: " + java.util.Objects.toString(this.networkAgentConfig));
        _aidl_sj.add("networkCapabilities: " + java.util.Objects.toString(this.networkCapabilities));
        _aidl_sj.add("linkProperties: " + java.util.Objects.toString(this.linkProperties));
        return "NetworkMonitorParameters" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.net.networkstack.aidl.NetworkMonitorParameters)) {
            return false;
        }
        android.net.networkstack.aidl.NetworkMonitorParameters that = (android.net.networkstack.aidl.NetworkMonitorParameters) other;
        if (java.util.Objects.deepEquals(this.networkAgentConfig, that.networkAgentConfig) && java.util.Objects.deepEquals(this.networkCapabilities, that.networkCapabilities) && java.util.Objects.deepEquals(this.linkProperties, that.linkProperties)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.networkAgentConfig, this.networkCapabilities, this.linkProperties).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.networkAgentConfig);
        return _mask | describeContents(this.networkCapabilities) | describeContents(this.linkProperties);
    }

    private int describeContents(java.lang.Object _v) {
        if (_v == null || !(_v instanceof android.os.Parcelable)) {
            return 0;
        }
        return ((android.os.Parcelable) _v).describeContents();
    }
}
