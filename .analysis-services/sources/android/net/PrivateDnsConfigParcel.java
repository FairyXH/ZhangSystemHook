package android.net;

/* JADX INFO: loaded from: classes.dex */
public class PrivateDnsConfigParcel implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.PrivateDnsConfigParcel> CREATOR = new android.os.Parcelable.Creator<android.net.PrivateDnsConfigParcel>() { // from class: android.net.PrivateDnsConfigParcel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.PrivateDnsConfigParcel createFromParcel(android.os.Parcel _aidl_source) {
            android.net.PrivateDnsConfigParcel _aidl_out = new android.net.PrivateDnsConfigParcel();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.PrivateDnsConfigParcel[] newArray(int _aidl_size) {
            return new android.net.PrivateDnsConfigParcel[_aidl_size];
        }
    };
    public java.lang.String hostname;
    public java.lang.String[] ips;
    public int privateDnsMode = -1;
    public java.lang.String dohName = "";
    public java.lang.String[] dohIps = new java.lang.String[0];
    public java.lang.String dohPath = "";
    public int dohPort = -1;

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeString(this.hostname);
        _aidl_parcel.writeStringArray(this.ips);
        _aidl_parcel.writeInt(this.privateDnsMode);
        _aidl_parcel.writeString(this.dohName);
        _aidl_parcel.writeStringArray(this.dohIps);
        _aidl_parcel.writeString(this.dohPath);
        _aidl_parcel.writeInt(this.dohPort);
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
            this.hostname = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.ips = _aidl_parcel.createStringArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.privateDnsMode = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.dohName = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.dohIps = _aidl_parcel.createStringArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.dohPath = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.dohPort = _aidl_parcel.readInt();
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
        _aidl_sj.add("hostname: " + java.util.Objects.toString(this.hostname));
        _aidl_sj.add("ips: " + java.util.Arrays.toString(this.ips));
        _aidl_sj.add("privateDnsMode: " + this.privateDnsMode);
        _aidl_sj.add("dohName: " + java.util.Objects.toString(this.dohName));
        _aidl_sj.add("dohIps: " + java.util.Arrays.toString(this.dohIps));
        _aidl_sj.add("dohPath: " + java.util.Objects.toString(this.dohPath));
        _aidl_sj.add("dohPort: " + this.dohPort);
        return "PrivateDnsConfigParcel" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.net.PrivateDnsConfigParcel)) {
            return false;
        }
        android.net.PrivateDnsConfigParcel that = (android.net.PrivateDnsConfigParcel) other;
        if (java.util.Objects.deepEquals(this.hostname, that.hostname) && java.util.Objects.deepEquals(this.ips, that.ips) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.privateDnsMode), java.lang.Integer.valueOf(that.privateDnsMode)) && java.util.Objects.deepEquals(this.dohName, that.dohName) && java.util.Objects.deepEquals(this.dohIps, that.dohIps) && java.util.Objects.deepEquals(this.dohPath, that.dohPath) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.dohPort), java.lang.Integer.valueOf(that.dohPort))) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.hostname, this.ips, java.lang.Integer.valueOf(this.privateDnsMode), this.dohName, this.dohIps, this.dohPath, java.lang.Integer.valueOf(this.dohPort)).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
