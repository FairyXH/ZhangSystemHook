package android.net.networkstack.aidl.ip;

/* JADX INFO: loaded from: classes.dex */
public class ReachabilityLossInfoParcelable implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable> CREATOR = new android.os.Parcelable.Creator<android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable>() { // from class: android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable createFromParcel(android.os.Parcel _aidl_source) {
            return android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable.internalCreateFromParcel(_aidl_source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable[] newArray(int _aidl_size) {
            return new android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable[_aidl_size];
        }
    };
    public final java.lang.String message;
    public final int reason;

    public static final class Builder {
        private java.lang.String message;
        private int reason;

        public android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable.Builder setMessage(java.lang.String message) {
            this.message = message;
            return this;
        }

        public android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable.Builder setReason(int reason) {
            this.reason = reason;
            return this;
        }

        public android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable build() {
            return new android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable(this.message, this.reason);
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeString(this.message);
        _aidl_parcel.writeInt(this.reason);
        int _aidl_end_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.setDataPosition(_aidl_start_pos);
        _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
        _aidl_parcel.setDataPosition(_aidl_end_pos);
    }

    public ReachabilityLossInfoParcelable(java.lang.String message, int reason) {
        this.message = message;
        this.reason = reason;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable internalCreateFromParcel(android.os.Parcel _aidl_parcel) {
        int i;
        android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable.Builder _aidl_parcelable_builder = new android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable.Builder();
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
            java.lang.String _aidl_temp_message = _aidl_parcel.readString();
            _aidl_parcelable_builder.setMessage(_aidl_temp_message);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                _aidl_parcelable_builder.build();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return _aidl_parcelable_builder.build();
            }
            int _aidl_temp_reason = _aidl_parcel.readInt();
            _aidl_parcelable_builder.setReason(_aidl_temp_reason);
            if (_aidl_start_pos > i) {
                throw new android.os.BadParcelableException(str);
            }
        }
        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
        return _aidl_parcelable_builder.build();
    }

    public java.lang.String toString() {
        java.util.StringJoiner _aidl_sj = new java.util.StringJoiner(", ", "{", "}");
        _aidl_sj.add("message: " + java.util.Objects.toString(this.message));
        _aidl_sj.add("reason: " + this.reason);
        return "ReachabilityLossInfoParcelable" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable)) {
            return false;
        }
        android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable that = (android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable) other;
        if (java.util.Objects.deepEquals(this.message, that.message) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.reason), java.lang.Integer.valueOf(that.reason))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.message, java.lang.Integer.valueOf(this.reason)).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
