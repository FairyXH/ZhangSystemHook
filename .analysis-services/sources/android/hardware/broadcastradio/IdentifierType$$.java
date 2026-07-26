package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public interface IdentifierType$$ {
    static java.lang.String toString(int _aidl_v) {
        return _aidl_v == 1000 ? "VENDOR_START" : _aidl_v == 1999 ? "VENDOR_END" : _aidl_v == 0 ? "INVALID" : _aidl_v == 1 ? "AMFM_FREQUENCY_KHZ" : _aidl_v == 2 ? "RDS_PI" : _aidl_v == 3 ? "HD_STATION_ID_EXT" : _aidl_v == 4 ? "HD_STATION_NAME" : _aidl_v == 5 ? "DAB_SID_EXT" : _aidl_v == 6 ? "DAB_ENSEMBLE" : _aidl_v == 7 ? "DAB_SCID" : _aidl_v == 8 ? "DAB_FREQUENCY_KHZ" : _aidl_v == 9 ? "DRMO_SERVICE_ID" : _aidl_v == 10 ? "DRMO_FREQUENCY_KHZ" : _aidl_v == 12 ? "SXM_SERVICE_ID" : _aidl_v == 13 ? "SXM_CHANNEL" : _aidl_v == 14 ? "HD_STATION_LOCATION" : java.lang.Integer.toString(_aidl_v);
    }

    static java.lang.String arrayToString(java.lang.Object _aidl_v) {
        if (_aidl_v == null) {
            return "null";
        }
        java.lang.Class<?> _aidl_cls = _aidl_v.getClass();
        if (!_aidl_cls.isArray()) {
            throw new java.lang.IllegalArgumentException("not an array: " + _aidl_v);
        }
        java.lang.Class<?> comp = _aidl_cls.getComponentType();
        java.util.StringJoiner _aidl_sj = new java.util.StringJoiner(", ", "[", "]");
        if (comp.isArray()) {
            for (int _aidl_i = 0; _aidl_i < java.lang.reflect.Array.getLength(_aidl_v); _aidl_i++) {
                _aidl_sj.add(arrayToString(java.lang.reflect.Array.get(_aidl_v, _aidl_i)));
            }
        } else {
            if (_aidl_cls != int[].class) {
                throw new java.lang.IllegalArgumentException("wrong type: " + _aidl_cls);
            }
            for (int e : (int[]) _aidl_v) {
                _aidl_sj.add(toString(e));
            }
        }
        return _aidl_sj.toString();
    }
}
