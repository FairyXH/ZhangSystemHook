package android.hardware.security.authgraph;

/* JADX INFO: loaded from: classes.dex */
public class KeInitResult implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.hardware.security.authgraph.KeInitResult> CREATOR = new android.os.Parcelable.Creator<android.hardware.security.authgraph.KeInitResult>() { // from class: android.hardware.security.authgraph.KeInitResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.security.authgraph.KeInitResult createFromParcel(android.os.Parcel _aidl_source) {
            android.hardware.security.authgraph.KeInitResult _aidl_out = new android.hardware.security.authgraph.KeInitResult();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.hardware.security.authgraph.KeInitResult[] newArray(int _aidl_size) {
            return new android.hardware.security.authgraph.KeInitResult[_aidl_size];
        }
    };
    public android.hardware.security.authgraph.SessionInfo sessionInfo;
    public android.hardware.security.authgraph.SessionInitiationInfo sessionInitiationInfo;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeTypedObject(this.sessionInitiationInfo, _aidl_flag);
        _aidl_parcel.writeTypedObject(this.sessionInfo, _aidl_flag);
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
            this.sessionInitiationInfo = (android.hardware.security.authgraph.SessionInitiationInfo) _aidl_parcel.readTypedObject(android.hardware.security.authgraph.SessionInitiationInfo.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.sessionInfo = (android.hardware.security.authgraph.SessionInfo) _aidl_parcel.readTypedObject(android.hardware.security.authgraph.SessionInfo.CREATOR);
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

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.sessionInitiationInfo);
        return _mask | describeContents(this.sessionInfo);
    }

    private int describeContents(java.lang.Object _v) {
        if (_v == null || !(_v instanceof android.os.Parcelable)) {
            return 0;
        }
        return ((android.os.Parcelable) _v).describeContents();
    }
}
