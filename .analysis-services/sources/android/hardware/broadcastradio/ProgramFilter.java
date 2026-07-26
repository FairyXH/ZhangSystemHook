package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class ProgramFilter implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramFilter> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramFilter>() { // from class: android.hardware.broadcastradio.ProgramFilter.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramFilter createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.ProgramFilter _aidl_out = new android.hardware.broadcastradio.ProgramFilter();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramFilter[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.ProgramFilter[_aidl_size];
        }
    };
    public int[] identifierTypes;
    public android.hardware.broadcastradio.ProgramIdentifier[] identifiers;
    public boolean includeCategories = false;
    public boolean excludeModifications = false;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeIntArray(this.identifierTypes);
        _aidl_parcel.writeTypedArray(this.identifiers, _aidl_flag);
        _aidl_parcel.writeBoolean(this.includeCategories);
        _aidl_parcel.writeBoolean(this.excludeModifications);
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
            this.identifierTypes = _aidl_parcel.createIntArray();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.identifiers = (android.hardware.broadcastradio.ProgramIdentifier[]) _aidl_parcel.createTypedArray(android.hardware.broadcastradio.ProgramIdentifier.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.includeCategories = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.excludeModifications = _aidl_parcel.readBoolean();
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
        _aidl_sj.add("identifierTypes: " + android.hardware.broadcastradio.IdentifierType$$.arrayToString(this.identifierTypes));
        _aidl_sj.add("identifiers: " + java.util.Arrays.toString(this.identifiers));
        _aidl_sj.add("includeCategories: " + this.includeCategories);
        _aidl_sj.add("excludeModifications: " + this.excludeModifications);
        return "ProgramFilter" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.ProgramFilter)) {
            return false;
        }
        android.hardware.broadcastradio.ProgramFilter that = (android.hardware.broadcastradio.ProgramFilter) other;
        if (java.util.Objects.deepEquals(this.identifierTypes, that.identifierTypes) && java.util.Objects.deepEquals(this.identifiers, that.identifiers) && java.util.Objects.deepEquals(java.lang.Boolean.valueOf(this.includeCategories), java.lang.Boolean.valueOf(that.includeCategories)) && java.util.Objects.deepEquals(java.lang.Boolean.valueOf(this.excludeModifications), java.lang.Boolean.valueOf(that.excludeModifications))) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(this.identifierTypes, this.identifiers, java.lang.Boolean.valueOf(this.includeCategories), java.lang.Boolean.valueOf(this.excludeModifications)).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.identifiers);
        return _mask;
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
