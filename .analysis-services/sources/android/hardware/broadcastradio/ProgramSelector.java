package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class ProgramSelector implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramSelector> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramSelector>() { // from class: android.hardware.broadcastradio.ProgramSelector.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramSelector createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.ProgramSelector _aidl_out = new android.hardware.broadcastradio.ProgramSelector();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramSelector[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.ProgramSelector[_aidl_size];
        }
    };
    public android.hardware.broadcastradio.ProgramIdentifier primaryId;
    public android.hardware.broadcastradio.ProgramIdentifier[] secondaryIds;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeTypedObject(this.primaryId, _aidl_flag);
        _aidl_parcel.writeTypedArray(this.secondaryIds, _aidl_flag);
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
            this.primaryId = (android.hardware.broadcastradio.ProgramIdentifier) _aidl_parcel.readTypedObject(android.hardware.broadcastradio.ProgramIdentifier.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.secondaryIds = (android.hardware.broadcastradio.ProgramIdentifier[]) _aidl_parcel.createTypedArray(android.hardware.broadcastradio.ProgramIdentifier.CREATOR);
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
        _aidl_sj.add("primaryId: " + java.util.Objects.toString(this.primaryId));
        _aidl_sj.add("secondaryIds: " + java.util.Arrays.toString(this.secondaryIds));
        return "ProgramSelector" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.ProgramSelector)) {
            return false;
        }
        android.hardware.broadcastradio.ProgramSelector that = (android.hardware.broadcastradio.ProgramSelector) other;
        if (java.util.Objects.deepEquals(this.primaryId, that.primaryId) && java.util.Objects.deepEquals(this.secondaryIds, that.secondaryIds)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.primaryId, this.secondaryIds).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.primaryId);
        return _mask | describeContents(this.secondaryIds);
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
