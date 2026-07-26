package android.net;

/* JADX INFO: loaded from: classes.dex */
public class NativeNetworkConfig implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.NativeNetworkConfig> CREATOR = new android.os.Parcelable.Creator<android.net.NativeNetworkConfig>() { // from class: android.net.NativeNetworkConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.NativeNetworkConfig createFromParcel(android.os.Parcel _aidl_source) {
            return android.net.NativeNetworkConfig.internalCreateFromParcel(_aidl_source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.NativeNetworkConfig[] newArray(int _aidl_size) {
            return new android.net.NativeNetworkConfig[_aidl_size];
        }
    };
    public final boolean excludeLocalRoutes;
    public final int netId;
    public final int networkType;
    public final int permission;
    public final boolean secure;
    public final int vpnType;

    public static final class Builder {
        private int netId = 0;
        private int networkType = 0;
        private int permission = 0;
        private boolean secure = false;
        private int vpnType = 2;
        private boolean excludeLocalRoutes = false;

        public android.net.NativeNetworkConfig.Builder setNetId(int netId) {
            this.netId = netId;
            return this;
        }

        public android.net.NativeNetworkConfig.Builder setNetworkType(int networkType) {
            this.networkType = networkType;
            return this;
        }

        public android.net.NativeNetworkConfig.Builder setPermission(int permission) {
            this.permission = permission;
            return this;
        }

        public android.net.NativeNetworkConfig.Builder setSecure(boolean secure) {
            this.secure = secure;
            return this;
        }

        public android.net.NativeNetworkConfig.Builder setVpnType(int vpnType) {
            this.vpnType = vpnType;
            return this;
        }

        public android.net.NativeNetworkConfig.Builder setExcludeLocalRoutes(boolean excludeLocalRoutes) {
            this.excludeLocalRoutes = excludeLocalRoutes;
            return this;
        }

        public android.net.NativeNetworkConfig build() {
            return new android.net.NativeNetworkConfig(this.netId, this.networkType, this.permission, this.secure, this.vpnType, this.excludeLocalRoutes);
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.netId);
        _aidl_parcel.writeInt(this.networkType);
        _aidl_parcel.writeInt(this.permission);
        _aidl_parcel.writeBoolean(this.secure);
        _aidl_parcel.writeInt(this.vpnType);
        _aidl_parcel.writeBoolean(this.excludeLocalRoutes);
        int _aidl_end_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.setDataPosition(_aidl_start_pos);
        _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
        _aidl_parcel.setDataPosition(_aidl_end_pos);
    }

    public NativeNetworkConfig(int netId, int networkType, int permission, boolean secure, int vpnType, boolean excludeLocalRoutes) {
        this.netId = netId;
        this.networkType = networkType;
        this.permission = permission;
        this.secure = secure;
        this.vpnType = vpnType;
        this.excludeLocalRoutes = excludeLocalRoutes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.net.NativeNetworkConfig internalCreateFromParcel(android.os.Parcel _aidl_parcel) {
        int i;
        android.net.NativeNetworkConfig.Builder _aidl_parcelable_builder = new android.net.NativeNetworkConfig.Builder();
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        int _aidl_parcelable_size = _aidl_parcel.readInt();
        try {
        } finally {
            if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                android.os.BadParcelableException badParcelableException = new android.os.BadParcelableException("Overflow in the size of parcelable");
            }
        }
        if (_aidl_parcelable_size < 4) {
            throw new android.os.BadParcelableException("Parcelable too small");
        }
        _aidl_parcelable_builder.build();
        if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
            _aidl_parcelable_builder.build();
            if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                throw new android.os.BadParcelableException("Overflow in the size of parcelable");
            }
        } else {
            int _aidl_temp_netId = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setNetId(_aidl_temp_netId);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            int _aidl_temp_networkType = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setNetworkType(_aidl_temp_networkType);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            int _aidl_temp_permission = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setPermission(_aidl_temp_permission);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            boolean _aidl_temp_secure = _aidl_parcel.readBoolean();
            _aidl_parcelable_builder.setSecure(_aidl_temp_secure);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            int _aidl_temp_vpnType = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setVpnType(_aidl_temp_vpnType);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            boolean _aidl_temp_excludeLocalRoutes = _aidl_parcel.readBoolean();
            _aidl_parcelable_builder.setExcludeLocalRoutes(_aidl_temp_excludeLocalRoutes);
            if (_aidl_start_pos > i) {
                throw new android.os.BadParcelableException(str);
            }
        }
        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
        return _aidl_parcelable_builder.build();
    }

    public java.lang.String toString() {
        java.util.StringJoiner _aidl_sj = new java.util.StringJoiner(", ", "{", "}");
        _aidl_sj.add("netId: " + this.netId);
        _aidl_sj.add("networkType: " + this.networkType);
        _aidl_sj.add("permission: " + this.permission);
        _aidl_sj.add("secure: " + this.secure);
        _aidl_sj.add("vpnType: " + this.vpnType);
        _aidl_sj.add("excludeLocalRoutes: " + this.excludeLocalRoutes);
        return "NativeNetworkConfig" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.net.NativeNetworkConfig)) {
            return false;
        }
        android.net.NativeNetworkConfig that = (android.net.NativeNetworkConfig) other;
        if (java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.netId), java.lang.Integer.valueOf(that.netId)) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.networkType), java.lang.Integer.valueOf(that.networkType)) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.permission), java.lang.Integer.valueOf(that.permission)) && java.util.Objects.deepEquals(java.lang.Boolean.valueOf(this.secure), java.lang.Boolean.valueOf(that.secure)) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.vpnType), java.lang.Integer.valueOf(that.vpnType)) && java.util.Objects.deepEquals(java.lang.Boolean.valueOf(this.excludeLocalRoutes), java.lang.Boolean.valueOf(that.excludeLocalRoutes))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(java.lang.Integer.valueOf(this.netId), java.lang.Integer.valueOf(this.networkType), java.lang.Integer.valueOf(this.permission), java.lang.Boolean.valueOf(this.secure), java.lang.Integer.valueOf(this.vpnType), java.lang.Boolean.valueOf(this.excludeLocalRoutes)).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
