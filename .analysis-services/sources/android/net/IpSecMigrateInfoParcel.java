package android.net;

/* JADX INFO: loaded from: classes.dex */
public class IpSecMigrateInfoParcel implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.IpSecMigrateInfoParcel> CREATOR = new android.os.Parcelable.Creator<android.net.IpSecMigrateInfoParcel>() { // from class: android.net.IpSecMigrateInfoParcel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.IpSecMigrateInfoParcel createFromParcel(android.os.Parcel _aidl_source) {
            return android.net.IpSecMigrateInfoParcel.internalCreateFromParcel(_aidl_source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.IpSecMigrateInfoParcel[] newArray(int _aidl_size) {
            return new android.net.IpSecMigrateInfoParcel[_aidl_size];
        }
    };
    public final int direction;
    public final int interfaceId;
    public final java.lang.String newDestinationAddress;
    public final java.lang.String newSourceAddress;
    public final java.lang.String oldDestinationAddress;
    public final java.lang.String oldSourceAddress;
    public final int requestId;
    public final int selAddrFamily;

    public static final class Builder {
        private java.lang.String newDestinationAddress;
        private java.lang.String newSourceAddress;
        private java.lang.String oldDestinationAddress;
        private java.lang.String oldSourceAddress;
        private int requestId = 0;
        private int selAddrFamily = 0;
        private int direction = 0;
        private int interfaceId = 0;

        public android.net.IpSecMigrateInfoParcel.Builder setRequestId(int requestId) {
            this.requestId = requestId;
            return this;
        }

        public android.net.IpSecMigrateInfoParcel.Builder setSelAddrFamily(int selAddrFamily) {
            this.selAddrFamily = selAddrFamily;
            return this;
        }

        public android.net.IpSecMigrateInfoParcel.Builder setDirection(int direction) {
            this.direction = direction;
            return this;
        }

        public android.net.IpSecMigrateInfoParcel.Builder setOldSourceAddress(java.lang.String oldSourceAddress) {
            this.oldSourceAddress = oldSourceAddress;
            return this;
        }

        public android.net.IpSecMigrateInfoParcel.Builder setOldDestinationAddress(java.lang.String oldDestinationAddress) {
            this.oldDestinationAddress = oldDestinationAddress;
            return this;
        }

        public android.net.IpSecMigrateInfoParcel.Builder setNewSourceAddress(java.lang.String newSourceAddress) {
            this.newSourceAddress = newSourceAddress;
            return this;
        }

        public android.net.IpSecMigrateInfoParcel.Builder setNewDestinationAddress(java.lang.String newDestinationAddress) {
            this.newDestinationAddress = newDestinationAddress;
            return this;
        }

        public android.net.IpSecMigrateInfoParcel.Builder setInterfaceId(int interfaceId) {
            this.interfaceId = interfaceId;
            return this;
        }

        public android.net.IpSecMigrateInfoParcel build() {
            return new android.net.IpSecMigrateInfoParcel(this.requestId, this.selAddrFamily, this.direction, this.oldSourceAddress, this.oldDestinationAddress, this.newSourceAddress, this.newDestinationAddress, this.interfaceId);
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeInt(this.requestId);
        _aidl_parcel.writeInt(this.selAddrFamily);
        _aidl_parcel.writeInt(this.direction);
        _aidl_parcel.writeString(this.oldSourceAddress);
        _aidl_parcel.writeString(this.oldDestinationAddress);
        _aidl_parcel.writeString(this.newSourceAddress);
        _aidl_parcel.writeString(this.newDestinationAddress);
        _aidl_parcel.writeInt(this.interfaceId);
        int _aidl_end_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.setDataPosition(_aidl_start_pos);
        _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
        _aidl_parcel.setDataPosition(_aidl_end_pos);
    }

    public IpSecMigrateInfoParcel(int requestId, int selAddrFamily, int direction, java.lang.String oldSourceAddress, java.lang.String oldDestinationAddress, java.lang.String newSourceAddress, java.lang.String newDestinationAddress, int interfaceId) {
        this.requestId = requestId;
        this.selAddrFamily = selAddrFamily;
        this.direction = direction;
        this.oldSourceAddress = oldSourceAddress;
        this.oldDestinationAddress = oldDestinationAddress;
        this.newSourceAddress = newSourceAddress;
        this.newDestinationAddress = newDestinationAddress;
        this.interfaceId = interfaceId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.net.IpSecMigrateInfoParcel internalCreateFromParcel(android.os.Parcel _aidl_parcel) {
        int i;
        android.net.IpSecMigrateInfoParcel.Builder _aidl_parcelable_builder = new android.net.IpSecMigrateInfoParcel.Builder();
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
            int _aidl_temp_requestId = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setRequestId(_aidl_temp_requestId);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            int _aidl_temp_selAddrFamily = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setSelAddrFamily(_aidl_temp_selAddrFamily);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            int _aidl_temp_direction = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setDirection(_aidl_temp_direction);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            java.lang.String _aidl_temp_oldSourceAddress = _aidl_parcel.readString();
            _aidl_parcelable_builder.setOldSourceAddress(_aidl_temp_oldSourceAddress);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            java.lang.String _aidl_temp_oldDestinationAddress = _aidl_parcel.readString();
            _aidl_parcelable_builder.setOldDestinationAddress(_aidl_temp_oldDestinationAddress);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            java.lang.String _aidl_temp_newSourceAddress = _aidl_parcel.readString();
            _aidl_parcelable_builder.setNewSourceAddress(_aidl_temp_newSourceAddress);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            java.lang.String _aidl_temp_newDestinationAddress = _aidl_parcel.readString();
            _aidl_parcelable_builder.setNewDestinationAddress(_aidl_temp_newDestinationAddress);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            int _aidl_temp_interfaceId = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setInterfaceId(_aidl_temp_interfaceId);
            if (_aidl_start_pos > i) {
                throw new android.os.BadParcelableException(str);
            }
        }
        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
        return _aidl_parcelable_builder.build();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
