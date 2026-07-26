package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public class ProgramListChunk implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramListChunk> CREATOR = new android.os.Parcelable.Creator<android.hardware.broadcastradio.ProgramListChunk>() { // from class: android.hardware.broadcastradio.ProgramListChunk.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramListChunk createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.broadcastradio.ProgramListChunk _aidl_out = new android.hardware.broadcastradio.ProgramListChunk();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.broadcastradio.ProgramListChunk[] newArray(int _aidl_size) {
            return new android.hardware.broadcastradio.ProgramListChunk[_aidl_size];
        }
    };
    public android.hardware.broadcastradio.ProgramInfo[] modified;
    public android.hardware.broadcastradio.ProgramIdentifier[] removed;
    public boolean purge = false;
    public boolean complete = false;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeBoolean(this.purge);
        _aidl_parcel.writeBoolean(this.complete);
        _aidl_parcel.writeTypedArray(this.modified, _aidl_flag);
        _aidl_parcel.writeTypedArray(this.removed, _aidl_flag);
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
            this.purge = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.complete = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.modified = (android.hardware.broadcastradio.ProgramInfo[]) _aidl_parcel.createTypedArray(android.hardware.broadcastradio.ProgramInfo.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.removed = (android.hardware.broadcastradio.ProgramIdentifier[]) _aidl_parcel.createTypedArray(android.hardware.broadcastradio.ProgramIdentifier.CREATOR);
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
        _aidl_sj.add("purge: " + this.purge);
        _aidl_sj.add("complete: " + this.complete);
        _aidl_sj.add("modified: " + java.util.Arrays.toString(this.modified));
        _aidl_sj.add("removed: " + java.util.Arrays.toString(this.removed));
        return "ProgramListChunk" + _aidl_sj.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof android.hardware.broadcastradio.ProgramListChunk)) {
            return false;
        }
        android.hardware.broadcastradio.ProgramListChunk that = (android.hardware.broadcastradio.ProgramListChunk) other;
        if (java.util.Objects.deepEquals(java.lang.Boolean.valueOf(this.purge), java.lang.Boolean.valueOf(that.purge)) && java.util.Objects.deepEquals(java.lang.Boolean.valueOf(this.complete), java.lang.Boolean.valueOf(that.complete)) && java.util.Objects.deepEquals(this.modified, that.modified) && java.util.Objects.deepEquals(this.removed, that.removed)) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        return java.util.Arrays.deepHashCode(java.util.Arrays.asList(java.lang.Boolean.valueOf(this.purge), java.lang.Boolean.valueOf(this.complete), this.modified, this.removed).toArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.modified);
        return _mask | describeContents(this.removed);
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
