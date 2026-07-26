package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class ProgramInfo implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramInfo> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramInfo>() { // from class: android.hardware.broadcastradio.ProgramInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramInfo createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.ProgramInfo _aidl_out = new android.hardware.broadcastradio.ProgramInfo();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramInfo[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.ProgramInfo[_aidl_size];
        }
    };
    public static final int FLAG_HD_AUDIO_ACQUISITION = 256;
    public static final int FLAG_HD_SIS_ACQUISITION = 128;
    public static final int FLAG_LIVE = 1;
    public static final int FLAG_MUTED = 2;
    public static final int FLAG_SIGNAL_ACQUISITION = 64;
    public static final int FLAG_STEREO = 32;
    public static final int FLAG_TRAFFIC_ANNOUNCEMENT = 8;
    public static final int FLAG_TRAFFIC_PROGRAM = 4;
    public static final int FLAG_TUNABLE = 16;
    public android.hardware.broadcastradio.ProgramIdentifier logicallyTunedTo;
    public android.hardware.broadcastradio.Metadata[] metadata;
    public android.hardware.broadcastradio.ProgramIdentifier physicallyTunedTo;
    public android.hardware.broadcastradio.ProgramIdentifier[] relatedContent;
    public android.hardware.broadcastradio.ProgramSelector selector;
    public android.hardware.broadcastradio.VendorKeyValue[] vendorInfo;
    public int infoFlags = 0;
    public int signalQuality = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeTypedObject(this.selector, _aidl_flag);
        _aidl_parcel.writeTypedObject(this.logicallyTunedTo, _aidl_flag);
        _aidl_parcel.writeTypedObject(this.physicallyTunedTo, _aidl_flag);
        _aidl_parcel.writeTypedArray(this.relatedContent, _aidl_flag);
        _aidl_parcel.writeInt(this.infoFlags);
        _aidl_parcel.writeInt(this.signalQuality);
        _aidl_parcel.writeTypedArray(this.metadata, _aidl_flag);
        _aidl_parcel.writeTypedArray(this.vendorInfo, _aidl_flag);
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
            this.selector = (android.hardware.broadcastradio.ProgramSelector) _aidl_parcel.readTypedObject(android.hardware.broadcastradio.ProgramSelector.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.logicallyTunedTo = (android.hardware.broadcastradio.ProgramIdentifier) _aidl_parcel.readTypedObject(android.hardware.broadcastradio.ProgramIdentifier.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.physicallyTunedTo = (android.hardware.broadcastradio.ProgramIdentifier) _aidl_parcel.readTypedObject(android.hardware.broadcastradio.ProgramIdentifier.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.relatedContent = (android.hardware.broadcastradio.ProgramIdentifier[]) _aidl_parcel.createTypedArray(android.hardware.broadcastradio.ProgramIdentifier.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.infoFlags = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.signalQuality = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.metadata = (android.hardware.broadcastradio.Metadata[]) _aidl_parcel.createTypedArray(android.hardware.broadcastradio.Metadata.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.vendorInfo = (android.hardware.broadcastradio.VendorKeyValue[]) _aidl_parcel.createTypedArray(android.hardware.broadcastradio.VendorKeyValue.CREATOR);
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
        _aidl_sj.add("selector: " + java.util.Objects.toString(this.selector));
        _aidl_sj.add("logicallyTunedTo: " + java.util.Objects.toString(this.logicallyTunedTo));
        _aidl_sj.add("physicallyTunedTo: " + java.util.Objects.toString(this.physicallyTunedTo));
        _aidl_sj.add("relatedContent: " + java.util.Arrays.toString(this.relatedContent));
        _aidl_sj.add("infoFlags: " + this.infoFlags);
        _aidl_sj.add("signalQuality: " + this.signalQuality);
        _aidl_sj.add("metadata: " + java.util.Arrays.toString(this.metadata));
        _aidl_sj.add("vendorInfo: " + java.util.Arrays.toString(this.vendorInfo));
        return "ProgramInfo" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.ProgramInfo)) {
            return false;
        }
        android.hardware.broadcastradio.ProgramInfo that = (android.hardware.broadcastradio.ProgramInfo) other;
        if (java.util.Objects.deepEquals(this.selector, that.selector) && java.util.Objects.deepEquals(this.logicallyTunedTo, that.logicallyTunedTo) && java.util.Objects.deepEquals(this.physicallyTunedTo, that.physicallyTunedTo) && java.util.Objects.deepEquals(this.relatedContent, that.relatedContent) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.infoFlags), java.lang.Integer.valueOf(that.infoFlags)) && java.util.Objects.deepEquals(java.lang.Integer.valueOf(this.signalQuality), java.lang.Integer.valueOf(that.signalQuality)) && java.util.Objects.deepEquals(this.metadata, that.metadata) && java.util.Objects.deepEquals(this.vendorInfo, that.vendorInfo)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.selector, this.logicallyTunedTo, this.physicallyTunedTo, this.relatedContent, java.lang.Integer.valueOf(this.infoFlags), java.lang.Integer.valueOf(this.signalQuality), this.metadata, this.vendorInfo).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.selector);
        return _mask | describeContents(this.logicallyTunedTo) | describeContents(this.physicallyTunedTo) | describeContents(this.relatedContent) | describeContents(this.metadata) | describeContents(this.vendorInfo);
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
